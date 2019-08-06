package com.ryyanj.focus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public class FocusMain {

    private static final Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static void main(String[] args)  throws IOException, InterruptedException, URISyntaxException {

        configureTinyLogger();
        Logger.info(PathFactory.get(PathEnum.LOG_FILES)+"javalog.txt");

        //we cant execute scripts inside the zip so move them to an external folder
        //located at PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR)
        FocusUtils.copyAllProcessesToExternalFolder();
        Logger.info("done attempting to copy processes to external files");

        deletePrcoessFolderWhenJVMTerminates();

        //setup watchservice to watch for file creation and changes
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(PathFactory.get(PathEnum.WATCH_SERVICE));
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
        Logger.info("done setting up watchservice to watch for file changes");

        //setup executor service for threads
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CompletionService<Integer> executorCompletionService= new ExecutorCompletionService<>(executorService);
        Logger.info("done setting up executorservice for thread pool");

        //process and load all saved plans
        File[] files = new File[0];
        File file = new File(PathFactory.get(PathEnum.HOME_SERVICE));
        if(file.exists()) {
            files = file.listFiles();
            Logger.info("found saved plans to pick up and run");
        }

        for(int i = 0; i < files.length; i++) {

            if(!files[i].isFile()) continue;
            String filename = files[i].getName();

            Task task;
            Plan plan;
            PlanFile planFile;
            try {
                planFile = mapper.readValue(new File(PathFactory.get(PathEnum.HOME_SERVICE)+ filename), PlanFile.class);

                plan = new Plan(filename, planFile.endtime);
                task = new Task(plan,concurrentSet);
                concurrentSet.add(filename);
                executorCompletionService.submit(task);
            } catch (Exception e) {
                Logger.info(e, "problem picking up file");
                continue;
            }

        }

        WatchKey key;
        Logger.info("waiting for a file to change in: " + Paths.get(PathFactory.get(PathEnum.WATCH_SERVICE)) );
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                String fileName = event.context().toString();

                validateFileSize(fileName);

                Logger.info("detected change in file: " + fileName);

                //if or more plans are already running then dont allow anymore to run(DDOS prevention)
                if(concurrentSet.size() >= 10) continue;

                //if its not a yaml file ignore it
                if(isYamlFile(fileName)==false) continue;

                Task task;
                Plan plan;
                PlanFile planFile;

                try {
                    planFile = mapper.readValue(new File(PathFactory.get(PathEnum.WATCH_SERVICE) + fileName), PlanFile.class);
                } catch (JsonParseException | JsonMappingException e ) {
                    Logger.info(e, "problem picking up plan cause it couldnt be parsed, continue to next plan");
                    continue;
                }

                //if the plan is not active dont run it
                if(planFile.available==false) continue;

                //if the plan is already running then add any new blacklist items
                if(concurrentSet.contains(fileName)) {
                    updateBlackList(fileName);
                    continue;
                }

                plan = new Plan(fileName, planFile.available,planFile.duration);
                task = new Task(plan,concurrentSet);

                concurrentSet.add(fileName);

                executorCompletionService.submit(task);

                FocusUtils.replacePattern(PathFactory.get(PathEnum.WATCH_SERVICE) + fileName, "True", "false");
                FocusUtils.replacePattern(PathFactory.get(PathEnum.WATCH_SERVICE) + fileName, "true", "false");
                FocusUtils.copyFile(fileName);
                FocusUtils.appendToFile(fileName, "\nendtime: " + plan.endtime);

            }
            key.reset();
        }


    }

    private static void validateFileSize(String filename) throws URISyntaxException {
        File planFile = new File(PathFactory.get(PathEnum.WATCH_SERVICE) + filename);
        long fileSize = planFile.length();
        int limit = 25000;
        if(fileSize > limit)
            throw new IllegalArgumentException("file too large, size is: " + planFile.length() + "  limit is: " + limit);

    }

    private static boolean isYamlFile(String filename) {

        if(FilenameUtils.getExtension(filename).equals("yml") || FilenameUtils.getExtension(filename).equals("yaml"))
            return true;

        return false;


    }

    private static void configureTinyLogger() throws URISyntaxException {

        Configurator.defaultConfig()
                .addWriter(new FileWriter(PathFactory.get(PathEnum.LOG_FILES)+"javalogs.txt"))
                .level(Level.INFO)
                .activate();

    }

    private static void updateBlackList(String filename) {
        try {

            //load the file in home service into an object
            PlanFile homeServicePlanFile = mapper.readValue(new File(PathFactory.get(PathEnum.HOME_SERVICE) + filename), PlanFile.class);

            //load the file in watch service into an object
            PlanFile watchServicePlanFile = mapper.readValue(new File(PathFactory.get(PathEnum.WATCH_SERVICE) + filename), PlanFile.class);

            //update homserviceplanfile with new blacklist urls
            Set<String> homeServiceUrlPlanSet = new HashSet<>();
            homeServiceUrlPlanSet.addAll(homeServicePlanFile.getUrlblacklist());
            homeServiceUrlPlanSet.addAll(watchServicePlanFile.getUrlblacklist());
            homeServicePlanFile.setUrlblacklist(new ArrayList<>(homeServiceUrlPlanSet));

            //update homserviceplanfile with new blacklist apps
            Set<String> homeServiceAppPlanSet = new HashSet<>();
            homeServiceAppPlanSet.addAll(homeServicePlanFile.getAppblacklist());
            homeServiceAppPlanSet.addAll(watchServicePlanFile.getAppblacklist());
            homeServicePlanFile.setAppblacklist(new ArrayList<>(homeServiceAppPlanSet));

            //write the modified object back to home service
            mapper.writeValue(new File(PathFactory.get(PathEnum.HOME_SERVICE) + filename), homeServicePlanFile);

        } catch (Exception e) {
            Logger.info(e, "had an error updating url blacklist");
        }
    }

    static void deletePrcoessFolderWhenJVMTerminates() {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {

                try {
                    FileUtils.deleteQuietly(new File(PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR)));
                } catch (URISyntaxException e) {
                    Logger.info(e, "deleting focusbin directory");
                }
                Logger.info("Shutting down and deleting focusbin directory!");
            }
        });
    }


}
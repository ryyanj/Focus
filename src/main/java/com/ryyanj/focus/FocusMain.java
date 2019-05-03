package com.ryyanj.focus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.*;
import java.util.Set;
import java.util.concurrent.*;


public class FocusMain {

    private static final Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static void main(String[] args)  throws Exception {



        //we can execute scripts inside the zip so move them to an external folder
        //located at PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR)
        FileUtil.copyAllProcessesToExternalFolder();
        deletePrcoessFolderWhenJVMTerminates();

        //setup watchservice to watch for file creation and changes
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(PathFactory.get(PathEnum.WATCH_SERVICE));
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        //setup executor service for threads
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CompletionService<Integer> executorCompletionService= new ExecutorCompletionService<>(executorService);

        //process and load all saved plans
        File[] files = new File[0];
        File file = new File(PathFactory.get(PathEnum.HOME_SERVICE));
        if(file.exists()) {
            files = file.listFiles();
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
                e.printStackTrace();
                continue;
            }

        }

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {

                String fileName = event.context().toString();
                //if the plan is already being run dont process it again
                if(concurrentSet.contains(fileName)) continue;

                Task task;
                Plan plan;
                PlanFile planFile;
                try {
                    planFile = mapper.readValue(new File(PathFactory.get(PathEnum.WATCH_SERVICE) + fileName), PlanFile.class);
                    plan = new Plan(fileName, planFile.available,planFile.duration);
                    task = new Task(plan,concurrentSet);
                    concurrentSet.add(fileName);
                    executorCompletionService.submit(task);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                FileUtil.replacePattern(PathFactory.get(PathEnum.WATCH_SERVICE) + fileName, "true", "false");
                FileUtil.copyFile(fileName);
                FileUtil.appendToFile(fileName, "\nendtime: " + plan.endtime);

            }
            key.reset();
        }


    }


    static void deletePrcoessFolderWhenJVMTerminates() {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {

                FileUtils.deleteQuietly(new File(PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR)));
                System.out.println("Shutting down and deleting focusbin directory!");
            }
        });
    }


}
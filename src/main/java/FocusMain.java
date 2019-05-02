import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.nio.file.*;
import java.util.Set;
import java.util.concurrent.*;


public class FocusMain {

    private static final Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static void main(String[] args)  throws Exception {


        //setup watchservice to watch for file creation and changes
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(PathConstants.WATCH_PATH);
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        //setup executor service for threads
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CompletionService<Integer> executorCompletionService= new ExecutorCompletionService<>(executorService);

        //process plans that were previously running
        File[] files = new File(PathConstants.HOME_PATH).listFiles();
        for(int i = 0; i < files.length; i++) {

            if(!files[i].isFile()) continue;

            String filename = files[i].getName();

            Task task;
            Plan plan;
            PlanFile planFile;
            try {
                planFile = mapper.readValue(new File(PathConstants.HOME_PATH + filename), PlanFile.class);

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
                    planFile = mapper.readValue(new File(PathConstants.WATCH_PATH + fileName), PlanFile.class);
                    plan = new Plan(fileName, planFile.available,planFile.duration);
                    task = new Task(plan,concurrentSet);
                    concurrentSet.add(fileName);
                    executorCompletionService.submit(task);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                FileUtil.replacePattern(PathConstants.WATCH_PATH + fileName, "true", "false");
                FileUtil.copyFile(fileName);
                FileUtil.appendToFile(fileName, "\nendtime: " + plan.endtime);

            }
            key.reset();
        }

    }


}
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SandBox {

    public static void main(String[] args) throws Exception {


//        MyThreadPool pool = new MyThreadPool();
//
//        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
//
//        Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
//
//
//        pool.addPlanToPool(new Plan(concurrentSet,"plan1",2000));
//        pool.addPlanToPool(new Plan(concurrentSet,"plan2",5000));
//        pool.addPlanToPool(new Plan(concurrentSet,"plan3",1000));
//        pool.addPlanToPool(new Plan(concurrentSet,"plan4",6000));

        //validations
        //file cant be larger than 2000 bytes
        //must be yaml file with the proper propertes
        //must be a boolean
        //must be a short
        //value must be less than 1440(1 day)

//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//                Time time = new Time();
//                try {
//                     System.out.println(new File("test.yml").length());
//                     time = mapper.readValue(new File("test.yml"), Time.class);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                System.out.println(time.isActive());
//                System.out.println(time.duration);

        //replacePattern("test2.yml", "true", "false");
        //replacePattern("test2.yml","^time: [0-9]*","time: value");

        //String content = "\ntime: value";
        //appendToFile("test2.yml",content);










    }

    static void appendToFile(String filename, String content) {

        try {
            Files.write(
                    Paths.get(filename),
                    content.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void replacePattern(String filename, String original, String replace) {

        try {
            Path path = Paths.get(filename);
            Stream<String> lines = Files.lines(path);
            List<String> replaced = lines.map(line -> line.replaceAll(original, replace)).collect(Collectors.toList());
            Files.write(path, replaced);
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Time {
        boolean active;
        Short duration;


        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(short duration) {
            this.duration = duration;
        }
    }

//    static class MyThreadPool {
//
//        CompletionService<Integer> executorCompletionService= new ExecutorCompletionService<>(Executors.newFixedThreadPool(5));
//
//        static {
//            System.out.println("thread pool starting");
//        }
//
//
//        MyThreadPool(){};
//
//        void addPlanToPool(Plan p) {
//
//            executorCompletionService.submit(p);
//
//        };
//
//
//
//
//    }
//
//    static class Plan implements Callable {
//
//        Set concurrentSet;
//        String planName;
//        long sleepTime;
//
//        Plan(Set concurrentSet, String planName, long sleepTime) {
//             this.concurrentSet = concurrentSet;
//             this.planName=planName;
//             this.sleepTime=sleepTime;
//        }
//
//        @Override
//        public String call() throws Exception {
//            System.out.println("Started taskName: "+planName);
//
//            Thread.sleep(sleepTime);
//            System.out.println("Completed taskName: "+planName);
//
//            this.concurrentSet.add(planName);
//            this.concurrentSet.
//            return planName;
//        }
//
//
//    }
//

}

package com.ryyanj.focus;

import net.time4j.SystemClock;
import net.time4j.TemporalType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Plan {

    String filename;
    String planname;
    Clock clock = TemporalType.CLOCK.from(SystemClock.MONOTONIC);
    long starttime;
    long endtime;

    Plan(String filename, boolean available, long duration) throws URISyntaxException {
        validateFileSize(filename);
        this.filename = filename;
        this.starttime = clock.instant().toEpochMilli();
        this.endtime = Clock.offset(clock, Duration.ofMinutes(duration)).instant().toEpochMilli();
        this.planname = FilenameUtils.removeExtension(filename);

    }

    Plan(String filename, long endtime) throws URISyntaxException {
        validateFileSize(filename);
        this.filename = filename;
        this.starttime = clock.instant().toEpochMilli();
        this.endtime = endtime;
        this.planname = FilenameUtils.removeExtension(filename);
    }

    void execute() throws URISyntaxException, IOException, InterruptedException {

        long currenttime = this.starttime;
        Logger.info("the plane name is " + planname);

        String timeleftPath = "";

        while(currenttime < this.endtime) {
            Logger.info("getting ready to run applescript");

            Process process = Runtime.getRuntime().exec(
                    "osascript " +
                    PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR) + "browserregex.scpt "
                    + this.filename + " "
                    + PathFactory.get(PathEnum.HOME_SERVICE) + " "
                    + PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR)
            );

            process.waitFor();
            Logger.info("Process exited with value " + process.exitValue());
            if(process.exitValue()!=0) {
                Logger.info("Process error stream is " + readProcessInputStream(process.getErrorStream()));
                throw new RuntimeException("applescript executed with an error");
            }



            String timeleft = convertTimeLeftToHoursAndMinutes(endtime-currenttime);
            Logger.info("time left in plan " + this.planname + " is " + timeleft);


            timeleftPath = PathFactory.get(PathEnum.WATCH_SERVICE) + this.planname + "_timeleft.txt";

            FileUtils.writeStringToFile(new File(timeleftPath), "time left: " + timeleft, "UTF-8");

            Thread.sleep(5000);
            currenttime = clock.instant().toEpochMilli();
        }

        FileUtils.deleteQuietly(new File(timeleftPath));

    }

    private String convertTimeLeftToHoursAndMinutes(long timeleft) {

        long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(timeleft);
        long hours = minutesLeft/60;
        long minutes = minutesLeft%60;
        String result = hours + ":" + minutes;
        return result;

    }

    private void validateFileSize(String filename) throws URISyntaxException {
        if(new File(PathFactory.get(PathEnum.WATCH_SERVICE) + filename).length() > 2000)
            throw new IllegalArgumentException("file too large");
    }

    private String readProcessInputStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line = null;
        while((line = br.readLine())!=null) {
            sb.append(line);
        }

        return sb.toString();

    }


}




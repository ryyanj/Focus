package com.ryyanj.focus;

import net.time4j.SystemClock;
import net.time4j.clock.SntpConnector;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;


public class Plan {

    String filename;
    String planname;
    long starttime;
    long endtime;
    SntpConnector con;
    //reference link below for time servers
    //http://support.ntp.org/bin/view/Servers/PublicTimeServer000217
    //String TIME_SERVER = "ptbtime1.ptb.de";
    String TIME_SERVER = "rustime01.rus.uni-stuttgart.de";

    Plan(String filename, boolean available, long duration) throws URISyntaxException, InterruptedException, IOException {
        validateFileSize(filename);
        this.filename = filename;
        con = new SntpConnector(TIME_SERVER);
        while(true) {
            Thread.sleep(1000);
            try {
                con.connect();
                break;
            } catch (IOException e) {
                Logger.info("couldnt connect to time server, get on the internet");
            }

        }
        this.starttime = SystemClock.MONOTONIC.synchronizedWith(con).currentTimeInMicros();
        this.endtime = this.starttime + TimeUnit.MINUTES.toMicros(duration);
        this.planname = FilenameUtils.removeExtension(filename);

    }

    Plan(String filename, long endtime) throws URISyntaxException, InterruptedException {
        Logger.info("running constructor used for files saved on download");
        validateFileSize(filename);
        this.filename = filename;
        con = new SntpConnector(TIME_SERVER);
        while(true) {
            Thread.sleep(1000);
            try {
                con.connect();
                break;
            } catch (IOException e) {
                Logger.info("couldnt connect to time server, get on the internet");
            }

        }

        this.starttime = SystemClock.MONOTONIC.synchronizedWith(con).currentTimeInMicros();
        this.endtime = endtime;
        this.planname = FilenameUtils.removeExtension(filename);
    }

    void execute() throws URISyntaxException, IOException, InterruptedException {

        long currenttime = this.starttime;
        Logger.info("the plan name is " + planname);

        String timeleftPath = PathFactory.get(PathEnum.WATCH_SERVICE) + this.planname + "_timeleft.txt";

        if(currenttime >= this.endtime) {
            Logger.info("current time is greater than end time so plan wont execute");
        }

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
                Logger.error("Process error stream is " + readProcessInputStream(process.getErrorStream()));
            }



            String timeleft = convertTimeLeftToHoursAndMinutes(endtime-currenttime);
            Logger.info("time left in plan " + this.planname + " is " + timeleft);

            FileUtils.writeStringToFile(new File(timeleftPath), "time left: " + timeleft, "UTF-8");
            Thread.sleep(300);
            while(true) {
                Thread.sleep(300);
                try {
                    con.connect();
                    break;
                } catch (IOException e) {
                    Logger.info(e);
                    Logger.info("couldnt connect to time server, get on the internet");
                }

            }
            currenttime = SystemClock.MONOTONIC.synchronizedWith(con).currentTimeInMicros();
        }

        Logger.info("the plan is ending: " + planname);
        Logger.info("about to delete quietly the file: " + filename + " at the location " + timeleftPath);
        FileUtils.deleteQuietly(new File(timeleftPath));
        Logger.info(("deleted quietly: " + filename + " at the location: " + timeleftPath));
        Logger.info("completely done with the plan " + planname);

    }



    private String convertTimeLeftToHoursAndMinutes(long timeleft) {

        //long secondsLeft = TimeUnit.MILLISECONDS.toSeconds(timeleft);
        long secondsLeft = TimeUnit.MICROSECONDS.toSeconds(timeleft);
        long hours = secondsLeft/3600;
        secondsLeft-=3600*hours;
        long minutes = secondsLeft/60;
        secondsLeft-=60*minutes;
        long seconds = secondsLeft;
        String result = hours + ":" + minutes + ":" + seconds;
        return result;

    }

    private void validateFileSize(String filename) throws URISyntaxException {
        File planFile = new File(PathFactory.get(PathEnum.WATCH_SERVICE) + filename);
        long fileSize = planFile.length();
        int limit = 25000;
        if(fileSize > limit)
            throw new IllegalArgumentException("file too large, size is: " + planFile.length() + "  limit is: " + limit);

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




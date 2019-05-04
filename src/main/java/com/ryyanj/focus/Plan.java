package com.ryyanj.focus;

import net.time4j.SystemClock;
import net.time4j.TemporalType;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Clock;
import java.time.Duration;

public class Plan {

    String filename;
    Clock clock = TemporalType.CLOCK.from(SystemClock.MONOTONIC);
    long starttime;
    long endtime;

    Plan(String filename, boolean available, long duration) throws URISyntaxException {
        validateFileSize(filename);
        this.filename = filename;
        this.starttime = clock.instant().toEpochMilli();
        this.endtime = Clock.offset(clock, Duration.ofMinutes(duration)).instant().toEpochMilli();
    }

    Plan(String filename, long endtime) throws URISyntaxException {
        validateFileSize(filename);
        this.filename = filename;
        this.starttime = clock.instant().toEpochMilli();
        this.endtime = endtime;
    }

    void execute() throws URISyntaxException, IOException, InterruptedException {

        long starttime = this.starttime;

        while(starttime < this.endtime) {
            Logger.info("getting ready to run applescript");

            Process process = Runtime.getRuntime().exec(
                    "osascript " +
                    PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR) + "browserregex.scpt "
                    + this.filename + " "
                    + PathFactory.get(PathEnum.HOME_SERVICE) + " "
                    + PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR)
            );
            Logger.info("Process exited with value " + process.exitValue());

            Thread.sleep(5000);
            starttime = clock.instant().toEpochMilli();
        }

    }

    private void validateFileSize(String filename) throws URISyntaxException {
        if(new File(PathFactory.get(PathEnum.WATCH_SERVICE) + filename).length() > 2000)
            throw new IllegalArgumentException("file too large");
    }


}




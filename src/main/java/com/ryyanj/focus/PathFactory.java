package com.ryyanj.focus;

import java.io.*;
import java.net.URISyntaxException;

public class PathFactory {

    static String get(PathEnum pathEnum)  {

        String jarpath = "";
        try {
             jarpath = PathFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        } catch(Exception e) {
            e.printStackTrace();
        }

        String jarparentpath = new File(jarpath).getParent();




        switch(pathEnum) {
            case WATCH_SERVICE:
                return System.getProperty("watchservicepath");
            case HOME_SERVICE:
                return jarparentpath+"/homeservice/";
            case PROCESSES_OUTSIDE_JAR:
                return jarparentpath+"/focusbin/";
            default:
                break;
        }

        throw new IllegalArgumentException("invalid pathenum");
    }

}

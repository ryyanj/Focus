package com.ryyanj.focus;

import java.io.*;
import java.net.URISyntaxException;

public class PathFactory {

    static String get(PathEnum pathEnum) throws URISyntaxException  {

        String jarpath = "";

        jarpath = PathFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

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

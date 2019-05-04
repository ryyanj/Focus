package com.ryyanj.focus;

import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    static void appendToFile(String filename, String content) throws IOException, URISyntaxException {

            Logger.info("attemping to write to file + " + filename);
            Files.write(
                    Paths.get(PathFactory.get(PathEnum.HOME_SERVICE)+ filename),
                    content.getBytes(),
                    StandardOpenOption.APPEND);
            Logger.info("done writing to file " + filename);

    }

    static void replacePattern(String filelocation, String original, String replace)throws IOException {


            Logger.info("preparing to replace text in file " + filelocation);
            Path path = Paths.get(filelocation);
            Stream<String> lines = Files.lines(path);
            List<String> replaced = lines.map(line -> line.replaceAll(original, replace)).collect(Collectors.toList());
            Files.write(path, replaced);
            lines.close();
            Logger.info("succesfully replaced text in file " + filelocation);

    }

    static void copyFile(String fileName) throws IOException, URISyntaxException {

        Logger.info("copying file " + fileName + " from " + PathFactory.get(PathEnum.WATCH_SERVICE) + " " + "to " + PathFactory.get(PathEnum.HOME_SERVICE));
        File src = new File(PathFactory.get(PathEnum.WATCH_SERVICE) + fileName);
        File dst = new File(PathFactory.get(PathEnum.HOME_SERVICE) + fileName);
        FileUtils.copyFile(src,dst);
        Logger.info("successfully copied file to new location");
    }

    static void copyAllProcessesToExternalFolder() throws IOException, URISyntaxException  {

            Logger.info("beginning to attempt to copy all processes to external folder");
            copyProcessToExternalFolder("browserregex.scpt");
            copyProcessToExternalFolder("processkiller.py");
            copyProcessToExternalFolder("tabkiller.py");
            Logger.info("successfully copied all processes to external folder");



    }

    static void copyProcessToExternalFolder(String processname) throws IOException, URISyntaxException  {

        InputStream in = FileUtil.class.getResourceAsStream("/focusbin/" + processname);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        File file = new File(PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR) + processname);
        FileUtils.copyInputStreamToFile(in, file);

        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);

        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);



        Files.setPosixFilePermissions(file.toPath(), perms);


    }


}

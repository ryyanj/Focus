package com.ryyanj.focus;

import org.apache.commons.io.FileUtils;

import java.io.*;
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

    static void appendToFile(String filename, String content) {

        try {
            Files.write(
                    Paths.get(PathFactory.get(PathEnum.HOME_SERVICE)+ filename),
                    content.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void replacePattern(String filelocation, String original, String replace) {

        try {
            Path path = Paths.get(filelocation);
            Stream<String> lines = Files.lines(path);
            List<String> replaced = lines.map(line -> line.replaceAll(original, replace)).collect(Collectors.toList());
            Files.write(path, replaced);
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void copyFile(String fileName) throws Exception {
        File src = new File(PathFactory.get(PathEnum.WATCH_SERVICE) + fileName);
        File dst = new File(PathFactory.get(PathEnum.HOME_SERVICE) + fileName);
        FileUtils.copyFile(src,dst);
    }

    static void copyAllProcessesToExternalFolder() throws Exception {

        copyProcessToExternalFolder("browserregex.scpt");
        copyProcessToExternalFolder("processkiller.py");
        copyProcessToExternalFolder("tabkiller.py");

    }

    static void copyProcessToExternalFolder(String processname) throws Exception {

        InputStream in = FileUtil.class.getResourceAsStream("/focusbin/" + processname);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        File file = new File(PathFactory.get(PathEnum.PROCESSES_OUTSIDE_JAR) + processname);
        FileUtils.copyInputStreamToFile(in, file);

        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        Files.setPosixFilePermissions(file.toPath(), perms);


    }


}

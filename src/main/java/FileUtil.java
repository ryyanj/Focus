import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    static void appendToFile(String filename, String content) {

        try {
            Files.write(
                    Paths.get(PathConstants.HOME_PATH + filename),
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

    static void copyFile(String fileName) throws IOException {
        File src = new File(PathConstants.WATCH_PATH + fileName);
        File dst = new File(PathConstants.HOME_PATH + fileName);
        FileUtils.copyFile(src,dst);
    }


}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class fileorg {

    public static void organize(String mainDir) throws IOException {
        Path imagesDir = Paths.get(mainDir, "IMAGENS");
        Path videosDir = Paths.get(mainDir, "VIDEOS");
        Path othersDir = Paths.get(mainDir, "OUTROS");

        Files.createDirectories(imagesDir);
        Files.createDirectories(videosDir);
        Files.createDirectories(othersDir);

        File mainDirectory = new File(mainDir);
        File[] filesList = mainDirectory.listFiles();

        if (filesList != null) {
            for (File file : filesList) {
                if (file.isDirectory()) {
                    if (file.getName().equals("IMAGENS") || file.getName().equals("VIDEOS") || file.getName().equals("OUTROS")) {
                        System.out.println("PULAR PASTA: " + file.getName());
                    } else {
                        File[] subFiles = file.listFiles();
                        if (subFiles != null) {
                            for (File subFile : subFiles) {
                                moveFileBasedOnMimeType(subFile, imagesDir, videosDir, othersDir);
                            }
                        }
                        if (file.list().length == 0) {
                            file.delete();
                            System.out.println("DELETANDO PASTA: " + file.getName());
                        }
                    }
                } else {
                    moveFileBasedOnMimeType(file, imagesDir, videosDir, othersDir);
                }
            }
        }
    }

    private static void moveFileBasedOnMimeType(File file, Path imagesDir, Path videosDir, Path othersDir) throws IOException {
        try {
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType != null) {
                if (mimeType.startsWith("image")) {
                    Files.move(file.toPath(), imagesDir.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                } else if (mimeType.startsWith("video")) {
                    Files.move(file.toPath(), videosDir.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.move(file.toPath(), othersDir.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to move file: " + file.getName());
        }
    }

    public static void main(String[] args) {
        try {
            String mainDir = "/home/kidfromsanta/Desktop/testez (copy)"; // Update this path
            organize(mainDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

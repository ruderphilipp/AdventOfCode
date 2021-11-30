package org.github.ruderphilipp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileHelper {
    public static List<String> getFileContent(String fileName) {
        Path basePath;
        try {
            basePath = Path.of(FileHelper.class.getResource("..").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        int stop = 15;
        File resourceDir = basePath.toFile();
        do {
            resourceDir = resourceDir.getParentFile();
            stop--;
            if (stop < 0)
                throw new RuntimeException("could not find gradle base path");
        } while (!resourceDir.getName().equals("build"));
        resourceDir = Path.of(resourceDir.getAbsolutePath(), "resources", "test").toFile();
        if (!resourceDir.exists() || !resourceDir.isDirectory())
            throw new RuntimeException("Resource directory does not exists at " + resourceDir.getAbsolutePath());

        Path filePath = Path.of(resourceDir.getAbsolutePath(), fileName);
        if (!filePath.toFile().exists() || !filePath.toFile().canRead())
            throw new RuntimeException("cannot read " + filePath.toAbsolutePath());

        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

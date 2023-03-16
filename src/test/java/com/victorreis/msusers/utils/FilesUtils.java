package com.victorreis.msusers.utils;

import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;

public final class FilesUtils {

    private FilesUtils() {
    }


    public static String getJsonFromFile(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource(fileName);

        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }
}

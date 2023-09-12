package com.health.scan.util.file;

import java.util.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {
    public String toBase64(String path, String type) throws IOException {
        File file = new File(path);
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return "data:"+type+";base64,"+Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + file, e);
        }
    }
}

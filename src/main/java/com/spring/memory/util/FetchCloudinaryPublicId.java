package com.spring.memory.util;

public class FetchCloudinaryPublicId {
    public static String extractPublicId(String url) {
        String[] parts = url.split("memory/");
        String nameWithExt = parts[1];                // hzcpj3ca937vhvlgyjvf.png
        return "memory/" + nameWithExt.replaceFirst("\\.[^.]+$", "");
    }
}

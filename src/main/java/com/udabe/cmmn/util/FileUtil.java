package com.udabe.cmmn.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public FileUtil() {

        throw new IllegalStateException("Utility class");
    }

    public static String getUploadPath() {
        if (StringUtils.isEmpty(System.getProperty("uploadDirectory"))) {
            File file = new File(System.getProperty("webRoot"));
            System.setProperty("uploadDirectory", file.getParentFile().getParentFile().getAbsolutePath() + File.separator + "upload");
        }
        return System.getProperty("uploadDirectory");
    }

    public static String getUploadImagePath() {
        if (StringUtils.isEmpty(System.getProperty("uploadDirectory"))) {
            File file = new File(System.getProperty("webRoot"));
            System.setProperty("uploadDirectory", file.getParentFile().getParentFile().getAbsolutePath() + File.separator + "uploadImage");
        }
        return System.getProperty("uploadDirectory");
    }

    public static String getUploadMainContentPath() {
        if (StringUtils.isEmpty(System.getProperty("uploadDirectory"))) {
            File file = new File(System.getProperty("webRoot"));
            System.setProperty("uploadDirectory", file.getParentFile().getParentFile().getAbsolutePath() + File.separator + "uploadMainContent");
        }
        return System.getProperty("uploadDirectory");
    }

    public static String getUploadPathNote() {
        if (StringUtils.isEmpty(System.getProperty("uploadDirectory"))) {
            File file = new File(System.getProperty("webRoot"));
            System.setProperty("uploadDirectory", file.getParentFile().getParentFile().getAbsolutePath() + File.separator + "uploadFileContent");
        }
        return System.getProperty("uploadDirectory");
    }

    public static boolean deleteFile(String path) {
        Path filePath = FileSystems.getDefault().getPath(path);

        try {
            Files.delete(filePath);
        } catch (IOException | SecurityException e) {
            return false;
        }

        return true;
    }

    public static boolean deleteFile(File file) {
        try {
            Files.delete(file.toPath());
        } catch (IOException | SecurityException e) {
            return false;
        }

        return true;
    }
}

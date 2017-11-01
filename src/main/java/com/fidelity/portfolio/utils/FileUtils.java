package com.fidelity.portfolio.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    public static File makeDirectory(String path) {
        LOG.info("Creating dir [" + path + "]");
        File file = new File(path);
        file.mkdirs();
        return file;
    }
}

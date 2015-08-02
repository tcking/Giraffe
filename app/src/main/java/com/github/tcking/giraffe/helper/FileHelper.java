package com.github.tcking.giraffe.helper;

import java.io.File;

/**
 * Created by tc(mytcking@gmail.com) on 15/8/2.
 */
public class FileHelper {

    public static void createDir(String dir) {
        File d = new File(dir);
        if (!d.exists()) {
            d.mkdirs();
        }
    }
}

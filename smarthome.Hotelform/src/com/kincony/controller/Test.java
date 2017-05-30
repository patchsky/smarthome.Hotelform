package com.kincony.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import com.kincony.util.FileSizeConversionUtil;

public class Test {
	public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            System.out.println("文件不存在");
            return -1;
        }
        return file.length();
    }
    
    
    public static String sormetFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
    public static void main(String[] args) {
        long size = FileSizeConversionUtil.getFileSize("d:/3D打印机.zip");
        System.out.println("java.txt文件大小为: " + size);
        String sormetFileSize = FileSizeConversionUtil.sormetFileSize(size);
        System.err.println(sormetFileSize);
    }
}

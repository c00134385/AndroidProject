package com.hjq.demo.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import timber.log.Timber;

public class Tools {
    private static final String TAG = Tools.class.getName();
    private static final String MEM_INFO_PATH = "/proc/meminfo";
    public static final String MEMTOTAL = "MemTotal";
    public static final String MEMFREE = "MemFree";

    /**
     * 得到中内存大小
     *
     * @param context
     * @return
     */
    public static String getTotalMemory(Context context) {
        return getMemInfoIype(context, MEMTOTAL);
    }

    /**
     * 得到可用内存大小
     *
     * @param context
     * @return
     */
    public static String getMemoryFree(Context context) {
        return getMemInfoIype(context, MEMFREE);
    }

    /**
     * 得到type info
     *
     * @param context
     * @param type
     * @return
     */
    public static String getMemInfoIype(Context context, String type) {
        try {
            FileReader fileReader = new FileReader(MEM_INFO_PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader, 4 * 1024);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                if (str.contains(type)) {
                    break;
                }
            }
            bufferedReader.close();
            /* \\s表示   空格,回车,换行等空白符,
            +号表示一个或多个的意思     */
            String[] array = str.split("\\s+");
            // 获得系统总内存，单位是KB，乘以1024转换为Byte
            int length = Integer.valueOf(array[1]).intValue() * 1024;
            return Formatter.formatFileSize(context, length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到内置存储空间的总容量
     * @param context
     * @return
     */
    public static String getInternalToatalSpace(Context context){
        String path = Environment.getDataDirectory().getPath();
        Log.d(TAG,"root path is "+path);
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long totalBlocks = statFs.getBlockCount();
        long availableBlocks = statFs.getAvailableBlocks();
        long useBlocks  = totalBlocks - availableBlocks;

        long rom_length = totalBlocks*blockSize;

        return Formatter.formatFileSize(context,rom_length);
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获得机身内容总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    public static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2="";
        String[] cpuInfo={"",""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }

    public static String  readSystem(Context context) {
        File root = Environment.getRootDirectory();
        StatFs sf = new  StatFs(root.getPath());
        long  blockSize = sf.getBlockSize();
        long  totalBlocks = sf.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
//        Log.d("" ,  "block大小:" + blockSize+ ",block数目:" + blockCount+ ",总大小:" +blockSize*blockCount/ 1024 + "KB" );
//        Log.d("" ,  "可用的block数目：:" + availCount+ ",可用大小:" + availCount*blockSize/ 1024 + "KB" );
    }

    public static void getDetailInfo(Context context) {
        getInfo(context, Environment.getDataDirectory());
        getInfo(context, Environment.getDownloadCacheDirectory());
        getInfo(context, Environment.getExternalStorageDirectory());
        getInfo(context, Environment.getRootDirectory());
    }

    public static void getInfo(Context context, File file) {
        StatFs sf = new StatFs(file.getPath());
        long  blockSize = sf.getBlockSize();
        long  totalBlocks = sf.getBlockCount();

        Timber.d("file:%s, %s", file.getAbsolutePath(), Formatter.formatFileSize(context, blockSize * totalBlocks));
    }
}

package com.yl.acoreui.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by 寻水的鱼 on 2018/9/12.
 */
public class CrashLogUtil {
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault());

    /**
     * 将日志写入文件。
     *
     * @param tag
     * @param message
     */
    public static synchronized void writeLog(File logFile, String tag, String message) {

        logFile.getParentFile().mkdirs();
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String time = timeFormat.format(Calendar.getInstance().getTime());
        Log.e("CrashLogUtil", "time--->" + time);
        synchronized (logFile) {
            FileWriter fileWriter = null;
            BufferedWriter bufdWriter = null;
            PrintWriter printWriter = null;
            try {
                fileWriter = new FileWriter(logFile, true);
                bufdWriter = new BufferedWriter(fileWriter);
                printWriter = new PrintWriter(fileWriter);
                bufdWriter.append(time).append(" ").append("E").append('/').append(tag).append(" ").append(message).append('\n');
                bufdWriter.flush();
                printWriter.flush();
                fileWriter.flush();
                Log.e("CrashLogUtil", "write CrashLog succeed!");
            } catch (IOException e) {
                Log.e("CrashLogUtil", "write CrashLog failed!");
                e.printStackTrace();
                closeQuietly(fileWriter);
                closeQuietly(bufdWriter);
                closeQuietly(printWriter);
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ioe) {
                // ignore
            }
        }
    }
}

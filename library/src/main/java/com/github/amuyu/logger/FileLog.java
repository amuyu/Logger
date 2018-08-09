package com.github.amuyu.logger;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by amuyu on 2017. 5. 31..
 */

public class FileLog {

    private BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private Looper looper;
    private String logFile;

    public FileLog(String logFile) {
        this.logFile = logFile;
        if (logFile != null) {
            File file = new File(logFile);
            try {
                if (!file.exists()) {
                    logQueue.put("DATE TIME LEVEL:THREAD/TAG: [METHOD(LINE)] MESSAGE\n");
                    logQueue.put("---- ---- ----------------- -------------- -------\n");
                } else {
                    logQueue.put("\nAPP RESTART ---------------------------------------------\n\n");
                }
            } catch (InterruptedException ignored) {
            }
        }

    }

    public void fileLog(int level, String tag, String message) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);
        StringBuilder log = new StringBuilder()
                .append(dateFormat.format(new Date()))
                .append(" ")
                .append(getLogLevelName(level))
                .append(":")
                .append(Thread.currentThread().getId())
                .append("/")
                .append(tag)
                .append(": ")
                .append(message);

        try {
            logQueue.put(log.append("\n").toString());
        } catch (InterruptedException ignored) {
        }
        synchronized (this) {
            if (looper == null || !looper.isAlive()) {
                looper = new Looper();
                looper.start();
            }
        }
    }

    private String getLogLevelName(int level) {
        switch (level) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.ERROR:
                return "E";
            case Log.INFO:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ASSERT:
                return "A";
            default:
                return "U";
        }
    }

    private class Looper extends Thread {
        public void run() {
            OutputStream os = null;
            try {
                os = new FileOutputStream(logFile, true);

                while (logFile != null) {
                    String log = null;
                    try {
                        log = logQueue.poll(1, TimeUnit.MINUTES);
                    } catch (InterruptedException ignored) {
                    }
                    if (log == null) break;
                    os.write(log.getBytes());
                    os.flush();
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            } finally {
                try {
                    if( os != null) os.close();
                } catch (IOException ignored) {
                }
            }

        }
    }
}

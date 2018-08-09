package com.github.amuyu.logger;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class AbstractLogPrinter {

    protected static final int MAX_LOG_LENGTH = 4000;
    protected static final int MAX_TAG_LENGTH = 23;
    protected static final int CALL_STACK_INDEX = 4;
    protected static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    private Context context;
    private boolean writeLogFile;
    private String logFileName;
    private FileLog fileLog;
    final ThreadLocal<String> explicitTag = new ThreadLocal<>();

    public AbstractLogPrinter(Context context) {
        this.context = context;
    }

    public AbstractLogPrinter writeFileLog(boolean writeFileLog) {
        this.writeLogFile = writeFileLog;
        return this;
    }

    public AbstractLogPrinter logFileName(String logFileName) {
        this.logFileName = logFileName;
        return this;
    }

    String getTag(StackTraceElement element) {
        String tag = explicitTag.get();
        if (tag != null) {
            return tag;
        }

        if(element != null) tag = createStackElementTag(element);
        else tag = "unknown";
        return tag;
    }


    protected void log(int priority, String message, Throwable t, Object... args) {
        StackTraceElement element = getStackTraceElement();
        String tag = getTag(element);

        if (message != null && message.length() == 0) {
            message = "";
        }
        if (message == null) {
            if (t == null) {
                return; // Swallow message if it's null and there's no throwable.
            }
            message = Utils.getStackTraceString(t);
        } else {
            if (args.length > 0) {
                message = formatMessage(message, args);
            }
            if (t != null) {
                message += "\n" + Utils.getStackTraceString(t);
            }
        }
        message = createStackElementMessage(element) + message;
        logPrint(priority, tag, message);
    }

    abstract void logPrint(int priority, String tag, String message);


    protected void fileLog(int priority, String tag, String message) {
        if(fileLog == null) createFileLog();
        fileLog.fileLog(priority,tag,message);
    }

    protected void androidLog(int priority, String tag, String message) {
        if (message.length() < MAX_LOG_LENGTH) {
            Log.println(priority, tag, message);
            return ;
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                String part = message.substring(i, end);
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, part);
                } else {
                    Log.println(priority, tag, part);
                }
                i = end;
            } while (i < newline);
        }
    }


    /**
     * Formats a log message with optional arguments.
     */
    private String formatMessage(String message, Object[] args) {
        return String.format(message, args);
    }


    private StackTraceElement getStackTraceElement() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= CALL_STACK_INDEX) {
            return null;
        }
        return stackTrace[CALL_STACK_INDEX];
    }

    private void createFileLog() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.US);

        byte[] encryptKey = null;
        String ext = (logFileName!=null)?'-'+logFileName+".log" : ".log";
        String cachePath = context.getExternalCacheDir().getPath();
        File logFile = new File(cachePath, dateFormat.format(new Date()) + ext);
        fileLog = new FileLog(logFile.getPath());
    }

    protected abstract String createStackElementTag(StackTraceElement element);

    protected abstract String createStackElementMessage(StackTraceElement element);


}

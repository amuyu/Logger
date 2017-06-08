package com.amuyu.logger;

import android.content.Context;

import java.util.regex.Matcher;

/**
 * Created by amuyu on 2017. 5. 31..
 */

public class DefaultLogPrinter extends AbstractLogPrinter {


    public DefaultLogPrinter(Context context) {
        super(context);
    }

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        String className = element.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(className);
        if (m.find()) {
            className = m.replaceAll("");
        }
        className = className.substring(className.lastIndexOf('.') + 1);
        className = className.length() > MAX_TAG_CLASS_LENGTH ? className.substring(0, MAX_TAG_CLASS_LENGTH) : className;
        String threadName = Thread.currentThread().getName();
        threadName = threadName.length() > MAX_TAG_THREAD_LEGTH ? threadName.substring(0, MAX_TAG_THREAD_LEGTH) : threadName;
        String tag = className + '#' + threadName;
        return tag.length() > MAX_TAG_LENGTH ? tag.substring(0, MAX_TAG_LENGTH) : tag;
    }

    protected String createStackElementMessage(StackTraceElement element) {
        if(element != null) {
            String methodName = element.getMethodName();
            int lineNumber = element.getLineNumber();
            return  methodName + '(' +lineNumber + ") ";
        }
        return "";
    }

    @Override
    protected void logPrint(int priority, String tag, String message) {
        androidLog(priority, tag, message);
        fileLog(priority, tag, message);
    }
}

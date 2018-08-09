package com.github.amuyu.logger;

/**
 * base android.util.Log
 */


public final class Logger {

    private static LogAdapter logAdapter = new LogAdapter();

    private Logger() {
        throw new AssertionError("No instances.");
    }

    public static void addLogPrinter(AbstractLogPrinter logPrinter) {
        if (logPrinter == null) throw  new NullPointerException("logPrinter is null");
        logAdapter.add(logPrinter);
    }

    public static void removeLogPrint(AbstractLogPrinter logPrinter) {
        if (logPrinter == null) throw  new NullPointerException("logPrinter is null");
        logAdapter.remove(logPrinter);
    }

    public static void clearLogPrint() {
        logAdapter.clear();
    }


    public static void v(String msg, Object...args) {
        logAdapter.v(msg, args);
    }


    public static void v(String msg, Throwable tr, Object...args) {
        logAdapter.v(msg, tr, args);
    }


    public static void d(String msg, Object...args) {
        logAdapter.d(msg, args);
    }

    public static void d(String msg, Throwable tr, Object...args) {
        logAdapter.d(msg, tr, args);
    }

    public static void i(String msg, Object...args) {
        logAdapter.i(msg, args);
    }


    public static void i(String msg, Throwable tr, Object...args) {
        logAdapter.i(msg, tr, args);
    }


    public static void w(String msg, Object...args) {
        logAdapter.w(msg, args);
    }


    public static void w(String msg, Throwable tr, Object...args) {
        logAdapter.w(msg, tr, args);
    }

    public static void w(Throwable tr, Object...args) {
        logAdapter.w(tr, args);
    }

    public static void e(String msg, Object...args) {
        logAdapter.e(msg, args);
    }

    public static void e(String msg, Throwable tr, Object...args) {
        logAdapter.e(msg, tr, args);
    }

    public static void e(Throwable tr, Object...args) {
        logAdapter.e(tr, args);
    }

    public static void d(Object object) {
        logAdapter.d(object);
    }

    public static void json(String json) {
        logAdapter.json(null, json);
    }

    public static void json(String name, String json) {
        logAdapter.json(name, json);
    }

}

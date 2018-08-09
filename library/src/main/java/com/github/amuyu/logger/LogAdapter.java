package com.github.amuyu.logger;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Created by amuyu on 2017. 5. 31..
 */

public class LogAdapter implements LogInterface {

    private static final int JSON_INDENT = 2;

    private static final AbstractLogPrinter[] LOG_PRINTERS_EMPTY = new AbstractLogPrinter[0];
    private static final List<AbstractLogPrinter> LOG_PRINTERS = new ArrayList<>();
    static volatile AbstractLogPrinter[] logPrintersAsArray = LOG_PRINTERS_EMPTY;

    public synchronized void add(AbstractLogPrinter logPrinter) {
        LOG_PRINTERS.add(logPrinter);
        logPrintersAsArray = LOG_PRINTERS.toArray(new AbstractLogPrinter[LOG_PRINTERS.size()]);
    }

    public synchronized void remove(AbstractLogPrinter logPrinter) {
        if(!LOG_PRINTERS.remove(logPrinter))
            throw new IllegalArgumentException("Cannot remove logPrinter");
        logPrintersAsArray = LOG_PRINTERS.toArray(new AbstractLogPrinter[LOG_PRINTERS.size()]);
    }

    public synchronized void clear() {
        LOG_PRINTERS.clear();
        logPrintersAsArray = LOG_PRINTERS_EMPTY;
    }

    public synchronized List<AbstractLogPrinter> logPrinters() {
        return unmodifiableList(new ArrayList<>(LOG_PRINTERS));
    }

    public int getCount() {
        return LOG_PRINTERS.size();
    }

    @Override
    public void v(String msg, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.VERBOSE, msg, null, args);
        }
    }

    @Override
    public void v(String msg, Throwable tr, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.VERBOSE, msg, tr, args);
        }
    }

    @Override
    public void d(String msg, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.DEBUG, msg, null, args);
        }
    }

    @Override
    public void d(String msg, Throwable tr, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.DEBUG, msg, tr, args);
        }
    }

    @Override
    public void d(Object object) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.DEBUG, Utils.toString(object), null);
        }
    }

    @Override
    public void json(String name, String json) {
        String message = "Empty/Null json content";
        try {
            if (!Utils.isEmpty(json)) {
                json = json.trim();
                if (json.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(json);
                    message = jsonObject.toString(JSON_INDENT);
                }
                if (json.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(json);
                    message = jsonArray.toString(JSON_INDENT);
                }
            }
        } catch (JSONException e) {
            message = "Invalid Json";
        }

        message = (name==null)?message : name + ':' + message;
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.DEBUG, message, null);
        }
    }

    @Override
    public void i(String msg, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.INFO, msg, null, args);
        }
    }

    @Override
    public void i(String msg, Throwable tr, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.INFO, msg, tr, args);
        }
    }

    @Override
    public void w(String msg, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.WARN, msg, null, args);
        }
    }

    @Override
    public void w(String msg, Throwable tr, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.WARN, msg, tr, args);
        }
    }

    @Override
    public void w(Throwable tr, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.WARN, null, tr, args);
        }
    }

    @Override
    public void e(String msg, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.ERROR, msg, null, args);
        }
    }

    @Override
    public void e(String msg, Throwable tr, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.ERROR, msg, tr, args);
        }
    }

    @Override
    public void e(Throwable tr, Object... args) {
        AbstractLogPrinter[] logPrinters = logPrintersAsArray;
        for (int i=0, count = logPrinters.length; i< count; i++) {
            logPrinters[i].log(Log.ERROR, null, tr, args);
        }
    }


}

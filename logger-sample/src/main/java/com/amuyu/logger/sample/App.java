package com.amuyu.logger.sample;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.github.amuyu.logger.DefaultLogPrinter;
import com.github.amuyu.logger.Logger;

/**
 * Created by amuyu on 2017. 5. 31..
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.addLogPrinter(new DefaultLogPrinter(this)
                                .writeFileLog(true));

        Logger.addLogPrinter(new CrashReport(this));
    }

    private class CrashReport extends DefaultLogPrinter {

        public CrashReport(Context context) {
            super(context);
            writeFileLog(true);
            logFileName("crash");
        }

        @Override
        protected void logPrint(int priority, String tag, String message) {
            if(priority == Log.ERROR)
                fileLog(priority,tag,message);
        }
    }
}

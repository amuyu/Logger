package com.amuyu.logger;

import android.content.Context;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.fail;
import static org.fest.assertions.api.Assertions.assertThat;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class LoggerTest {

    @Before @After public void setUpAndTearDown() {
        Logger.clearLogPrint();
    }

    @Test public void debugTagAndMessage() {
        Context context = RuntimeEnvironment.application;
        Logger.addLogPrinter(new DefaultLogPrinter(context));
        String message = "Hello, world!";
        Logger.d(message);
        assertLog()
                .hasDebugMessage("LoggerTest#main", "debugTagAndMessage(43) "+message);
    }

    @Test public void dontAddLogPrinter() {
        String message = "Hello, world!";
        Logger.d(message);
        assertLog()
                .hasNoMoreMessages();
    }

    @Test public void writeToFile() {
        Context context = RuntimeEnvironment.application;
        Logger.addLogPrinter(new DefaultLogPrinter(context).writeFileLog(true));
        Logger.d("Hello, world!");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.US);
        String cachePath = context.getExternalCacheDir().getPath();
        String ext = ".log";
        File logFile = new File(cachePath, dateFormat.format(new Date()) + ext);
        if(logFile.exists()) {

            try {
                InputStream in = new FileInputStream(logFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuilder builder = new StringBuilder();
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String expected = "LoggerTest#main: writeToFile(58) Hello, world!";
                String message = builder.toString();
                message = message.substring(message.length()-expected.length(), message.length());
                assertThat(message).isEqualTo(expected);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Test public void writeToCrashFile() {
        String fileName = "crash";
        Context context = RuntimeEnvironment.application;
        Logger.addLogPrinter(new DefaultLogPrinter(context) {
            @Override
            protected void logPrint(int priority, String tag, String message) {
                if(priority == Log.ERROR)
                    fileLog(priority,tag,message);
            }
        }.writeFileLog(true).logFileName(fileName));
        Logger.e("Hello, world!");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd", Locale.US);
        String cachePath = context.getExternalCacheDir().getPath();
        String ext = '-'+fileName+".log";
        File logFile = new File(cachePath, dateFormat.format(new Date()) + ext);
        if(logFile.exists()) {
            try {
                InputStream in = new FileInputStream(logFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuilder builder = new StringBuilder();
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String expected = "LoggerTest#main: writeToCrashFile(96) Hello, world!";
                String message = builder.toString();
                message = message.substring(message.length()-expected.length(), message.length());
                assertThat(message).isEqualTo(expected);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            fail("not exists file");
        }
        assertLog()
                .hasNoMoreMessages();
    }

    private static LogAssert assertLog() {
        return new LogAssert(ShadowLog.getLogs());
    }

    private static final class LogAssert {
        private final List<ShadowLog.LogItem> items;
        private int index = 0;

        private LogAssert(List<ShadowLog.LogItem> items) {
            this.items = items;
        }

        public LogAssert hasVerboseMessage(String tag, String message) {
            return hasMessage(Log.VERBOSE, tag, message);
        }

        public LogAssert hasDebugMessage(String tag, String message) {
            return hasMessage(Log.DEBUG, tag, message);
        }

        public LogAssert hasInfoMessage(String tag, String message) {
            return hasMessage(Log.INFO, tag, message);
        }

        public LogAssert hasWarnMessage(String tag, String message) {
            return hasMessage(Log.WARN, tag, message);
        }

        public LogAssert hasErrorMessage(String tag, String message) {
            return hasMessage(Log.ERROR, tag, message);
        }

        public LogAssert hasAssertMessage(String tag, String message) {
            return hasMessage(Log.ASSERT, tag, message);
        }

        private LogAssert hasMessage(int priority, String tag, String message) {
            ShadowLog.LogItem item = items.get(index++);
            assertThat(item.type).isEqualTo(priority);
            assertThat(item.tag).contains(tag);
            assertThat(item.msg).contains(message);
            return this;
        }

        public void hasNoMoreMessages() {
            assertThat(items).hasSize(index);
        }
    }

}
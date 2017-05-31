package com.amuyu.logger;

/**
 * Created by amuyu on 2017. 5. 30..
 */

public interface LogInterface {

    void v(String msg, Object... args);
    void v(String msg, Throwable tr, Object... args);
    void d(String msg, Object... args);
    void d(String msg, Throwable tr, Object... args);
    void i(String msg, Object... args);
    void i(String msg, Throwable tr, Object... args);
    void w(String msg, Object... args);
    void w(String msg, Throwable tr, Object... args);
    void w(Throwable tr, Object... args);
    void e(String msg, Object... args);
    void e(String msg, Throwable tr, Object... args);
    void e(Throwable tr, Object... args);
    void d(Object object);
    void json(String name, String json);

}

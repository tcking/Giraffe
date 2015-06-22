package com.github.tcking.giraffe.core;

import com.github.tcking.giraffe.helper.FormattingTuple;
import com.github.tcking.giraffe.helper.MessageFormatter;
import com.google.code.microlog4android.Level;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 日志记录器
 * Created by tc on 15/6/16.
 */
public class Log {
    private static Level currentLevel=Level.DEBUG;

    public static void setLevel(String level) {
        if ("debug".equalsIgnoreCase(level)) {
            currentLevel=Level.DEBUG;
        }else if ("info".equalsIgnoreCase(level)) {
            currentLevel=Level.INFO;
        }else if ("error".equalsIgnoreCase(level)) {
            currentLevel=Level.ERROR;
        }else if ("off".equalsIgnoreCase(level)) {
            currentLevel=Level.OFF;
        }else {
            throw new RuntimeException("not support log level:" + level);
        }
    }

    public static final Logger logger = LoggerFactory.getLogger();

    public static boolean isDebugEnabled() {
        return isLevelEnabled(Level.DEBUG);
    }

    public static boolean isInfoEnabled() {
        return isLevelEnabled(Level.INFO);
    }

    private static boolean isLevelEnabled(Level level) {
        return level.toInt()>=currentLevel.toInt();
    }

    public static boolean isErrorEnabled() {
        return isLevelEnabled(Level.ERROR);
    }


    private static void formatAndLog(Level level, String msg) {
        if (!isLevelEnabled(level)) {
            return;
        }
        logger.log(level, msg);
    }

    private static void formatAndLog(Level level, String format,Object arg1,Object arg2) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        logger.log(level, tp.getMessage(), tp.getThrowable());
    }

    private static void formatAndLog(Level level, String format, Object... arguments) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        logger.log(level, tp.getMessage(), tp.getThrowable());
    }

    /*------------------debug------------------------*/

    public static void d(String msg) {
        formatAndLog(Level.DEBUG, msg);
    }


    public static void d(String format,Object arg1) {
        formatAndLog(Level.DEBUG, format, arg1);
    }

    public static void d(String format,Object arg1,Object arg2) {
        formatAndLog(Level.DEBUG, format, arg1, arg2);
    }

    public static void d(String format,Object... arguments) {
        formatAndLog(Level.DEBUG, format, arguments);
    }

    public static void d(String msg,Throwable error) {
        formatAndLog(Level.DEBUG, msg, error);
    }

    /*------------------info------------------------*/

    public static void i(String msg) {
        formatAndLog(Level.INFO, msg);
    }


    public static void i(String format,Object arg1) {
        formatAndLog(Level.INFO, format, arg1);
    }

    public static void i(String format,Object arg1,Object arg2) {
        formatAndLog(Level.INFO, format, arg1, arg2);
    }

    public static void i(String format,Object... arguments) {
        formatAndLog(Level.INFO, format, arguments);
    }

    public static void i(String msg,Throwable error) {
        formatAndLog(Level.INFO, msg, error);
    }

    /*------------------error------------------------*/

    public static void e(String msg) {
        formatAndLog(Level.ERROR, msg);
    }


    public static void e(String format,Object arg1) {
        formatAndLog(Level.ERROR, format, arg1);
    }

    public static void e(String format,Object arg1,Object arg2) {
        formatAndLog(Level.ERROR, format, arg1, arg2);
    }

    public static void e(String format,Object... arguments) {
        formatAndLog(Level.ERROR, format, arguments);
    }

    public static void e(String msg,Throwable error) {
        formatAndLog(Level.ERROR, msg, error);
    }


}

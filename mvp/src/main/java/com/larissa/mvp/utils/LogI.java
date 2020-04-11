package com.larissa.mvp.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/6/29.
 */

public class LogI {

    /**
     * LogUtils工具说明:
     * 1 只输出等级大于等于LEVEL的日志
     * 所以在开发和产品发布后通过修改LEVEL来选择性输出日志.
     * 当LEVEL=NOTHING则屏蔽了所有的日志.
     * 2 v,d,i,w,e均对应两个方法.
     * 若不设置TAG或者TAG为空则为设置默认TAG
     */
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int HTTP = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int CHECK = 7;
    public static final int LEVEL = VERBOSE;//VERBOSE开启所有  NOTHING关闭所有
    public static final String SEPARATOR = ",";
    public static String TAG = "DRIVER_DEBUG_DIANDIAN";

    public static void v(String message) {
        if (LEVEL <= VERBOSE) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
//            String tag = getDefaultTag(stackTraceElement);
            Log.v(TAG, getLogInfo(stackTraceElement) + message);
            LogToFileUtils.write(getLogInfo(stackTraceElement) + message);
        }
    }


    public static void d(String message) {
        if (LEVEL <= DEBUG) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
//            String tag = getDefaultTag(stackTraceElement);
            Log.i(TAG, getLogInfo(stackTraceElement) + message);
            LogToFileUtils.write(getLogInfo(stackTraceElement) + message);
        }
    }

    public static void i() {
        if (LEVEL <= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(TAG,getLogInfo(stackTraceElement));
            LogToFileUtils.write(getLogInfo(stackTraceElement));
        }
    }

    public static void i(String message) {
        if (LEVEL <= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];

            int segmentSize = 3 * 1024;
            if (EmptyUtils.isNotEmpty(message)){
                long length = message.length();
                if (length <= segmentSize ) {// 长度小于等于限制直接打印
                    Log.i(TAG, getLogInfo(stackTraceElement) + message);
                }else {
                    while (message.length() > segmentSize ) {// 循环分段打印日志
                        String logContent = message.substring(0, segmentSize );
                        message = message.replace(logContent, "");
                        Log.i(TAG, logContent);
                    }
                    Log.i(TAG, getLogInfo(stackTraceElement) + message);// 打印剩余日志
                }
            }

//            Log.i(TAG,getLogInfo(stackTraceElement) + message);
            LogToFileUtils.write(getLogInfo(stackTraceElement) + message);
        }
    }

    public static void i(double message) {
        if (LEVEL <= INFO) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(TAG,getLogInfo(stackTraceElement) + message);
            LogToFileUtils.write(getLogInfo(stackTraceElement) + message);
        }
    }

    public static void h(String message) {
        if (LEVEL <= HTTP) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            int segmentSize = 3 * 1024;
            long length = message.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                Log.i(TAG, getLogInfo(stackTraceElement) + message);
            }else {
                while (message.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = message.substring(0, segmentSize );
                    message = message.replace(logContent, "");
                    Log.i(TAG, logContent);
                }
                Log.i(TAG, getLogInfo(stackTraceElement) + message);// 打印剩余日志
            }


//            Log.i(TAG,getLogInfo(stackTraceElement) + message);
            LogToFileUtils.write(getLogInfo(stackTraceElement) + message);
        }
    }

    public static void a(String message) {
        if (LEVEL <= CHECK) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
//                String tag = getDefaultTag(stackTraceElement);
            Log.i(TAG,getLogInfo(stackTraceElement) + message);
            LogToFileUtils.write(getLogInfo(stackTraceElement) + message);
        }
    }

    public static void w(String message) {
        if (LEVEL <= WARN) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
//            String tag = getDefaultTag(stackTraceElement);
            Log.i(TAG,getLogInfo(stackTraceElement) + message);
            LogToFileUtils.write(getLogInfo(stackTraceElement) + message);
        }
    }


    public static void e(String message) {
        if (LEVEL <= ERROR) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(TAG,getLogInfo(stackTraceElement) + message);
            LogToFileUtils.write(getLogInfo(stackTraceElement) + message);
        }
    }

    /**
     * 获取默认的TAG名称.
     * 比如在MainActivity.java中调用了日志输出.
     * 则TAG为MainActivity
     */
    public static String getDefaultTag(StackTraceElement stackTraceElement) {
        String fileName = stackTraceElement.getFileName();
        String stringArray[] = fileName.split("\\.");
        String tag = stringArray[0];
        return tag;
    }

    /**
     * 输出日志所包含的信息
     */
    public static String getLogInfo(StackTraceElement stackTraceElement) {
        StringBuilder logInfoStringBuilder = new StringBuilder();
        // 获取线程名
//            String threadName = Thread.currentThread().getName();
//            // 获取线程ID
//            long threadID = Thread.currentThread().getId();
//            // 获取文件名.即xxx.java
//            String fileName = stackTraceElement.getFileName();
//            // 获取类名.即包名+类名
//            String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取生日输出行数
//            int lineNumber = stackTraceElement.getLineNumber();

        logInfoStringBuilder.append("[ ");
//            logInfoStringBuilder.append("threadID=" + threadID).append(SEPARATOR);
//            logInfoStringBuilder.append("thread=" + threadName).append(SEPARATOR);
//            logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
//            logInfoStringBuilder.append("class=" + className).append(SEPARATOR);
        logInfoStringBuilder.append("method=" + methodName).append(SEPARATOR);
//            logInfoStringBuilder.append("lineNumber=" + lineNumber);
        logInfoStringBuilder.append(" ] ");
        return logInfoStringBuilder.toString();
    }

}

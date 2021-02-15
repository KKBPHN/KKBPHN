package com.miui.internal.log;

import com.miui.internal.log.appender.Appender;
import com.miui.internal.log.message.Message;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Logger {
    public static final int MAX_LOG_LENGTH = 8192;
    private static final String TAG = "Logger";
    private CopyOnWriteArrayList mAppenders = new CopyOnWriteArrayList();
    private Level mLevel = Level.VERBOSE;
    private String mName;

    public Logger(String str) {
        this.mName = str;
    }

    private void doLog(Level level, String str, String str2, Throwable th, Message message) {
        Level level2 = level;
        if (level.compareTo(this.mLevel) >= 0) {
            long currentTimeMillis = System.currentTimeMillis();
            Iterator it = this.mAppenders.iterator();
            while (it.hasNext()) {
                Appender appender = (Appender) it.next();
                String str3 = this.mName;
                String str4 = str;
                long j = currentTimeMillis;
                Level level3 = level;
                if (str2 == null) {
                    appender.append(str3, str4, j, level3, message);
                } else {
                    appender.append(str3, str4, j, level3, str2, th);
                }
            }
        }
    }

    public void addAppender(Appender appender) {
        if (appender != null) {
            this.mAppenders.addIfAbsent(appender);
            return;
        }
        throw new IllegalArgumentException("Appender not allowed to be null");
    }

    public Appender getAppenderAt(int i) {
        return (Appender) this.mAppenders.get(i);
    }

    public int getAppenderCount() {
        return this.mAppenders.size();
    }

    public Level getLevel() {
        return this.mLevel;
    }

    public void log(Level level, String str, Message message) {
        doLog(level, str, null, null, message);
    }

    public void log(Level level, String str, String str2) {
        doLog(level, str, str2, null, null);
    }

    public void log(Level level, String str, String str2, Throwable th) {
        doLog(level, str, str2, th, null);
    }

    public void removeAppender(Appender appender) {
        if (appender != null) {
            appender.close();
            this.mAppenders.remove(appender);
            return;
        }
        throw new IllegalArgumentException("The appender must not be null.");
    }

    public void setLevel(Level level) {
        this.mLevel = level;
    }

    public void shutdown() {
        Iterator it = this.mAppenders.iterator();
        while (it.hasNext()) {
            ((Appender) it.next()).close();
        }
        this.mAppenders.clear();
    }
}

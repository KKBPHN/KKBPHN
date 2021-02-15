package miui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.miui.internal.log.Level;
import com.miui.internal.log.Logger;
import com.miui.internal.log.LoggerFactory;
import com.miui.internal.log.message.Message;
import com.miui.internal.log.message.StringFormattedMessage;
import com.miui.internal.log.receiver.DumpReceiver;

public class Log {

    public class DumpLogReceiver extends BroadcastReceiver {
        private DumpReceiver mReceiver = new DumpReceiver();

        public void onReceive(Context context, Intent intent) {
            this.mReceiver.onReceive(context, intent);
        }
    }

    public class Facade {
        private static final String TAG = "LogcatFacade";
        private Logger mLogger;

        private Facade(Logger logger) {
            this.mLogger = logger;
        }

        private void log(Level level, String str, String str2, Throwable th) {
            doLog(level, str, str2, th, null);
        }

        private void logf(Level level, String str, String str2, Object... objArr) {
            doLog(level, str, null, null, StringFormattedMessage.obtain().setFormat(str2).setParameters(objArr));
        }

        public void debug(String str, String str2) {
            log(Level.DEBUG, str, str2, null);
        }

        public void debug(String str, String str2, Throwable th) {
            log(Level.DEBUG, str, str2, th);
        }

        public void debug(String str, String str2, Object... objArr) {
            logf(Level.DEBUG, str, str2, objArr);
        }

        /* access modifiers changed from: protected */
        public void doLog(Level level, String str, String str2, Throwable th, Message message) {
            Logger logger = this.mLogger;
            if (logger == null) {
                android.util.Log.e(TAG, "mLogger is null");
            } else if (message == null) {
                logger.log(level, str, str2, th);
            } else {
                logger.log(level, str, message);
                message.recycle();
            }
        }

        public void error(String str, String str2) {
            log(Level.ERROR, str, str2, null);
        }

        public void error(String str, String str2, Throwable th) {
            log(Level.ERROR, str, str2, th);
        }

        public void error(String str, String str2, Object... objArr) {
            logf(Level.ERROR, str, str2, objArr);
        }

        public void fatal(String str, String str2) {
            log(Level.FATAL, str, str2, null);
        }

        public void fatal(String str, String str2, Throwable th) {
            log(Level.FATAL, str, str2, th);
        }

        public void fatal(String str, String str2, Object... objArr) {
            logf(Level.FATAL, str, str2, objArr);
        }

        public void info(String str, String str2) {
            log(Level.INFO, str, str2, null);
        }

        public void info(String str, String str2, Throwable th) {
            log(Level.INFO, str, str2, th);
        }

        public void info(String str, String str2, Object... objArr) {
            logf(Level.INFO, str, str2, objArr);
        }

        public void verbose(String str, String str2) {
            log(Level.VERBOSE, str, str2, null);
        }

        public void verbose(String str, String str2, Throwable th) {
            log(Level.VERBOSE, str, str2, th);
        }

        public void verbose(String str, String str2, Object... objArr) {
            logf(Level.VERBOSE, str, str2, objArr);
        }

        public void warn(String str, String str2) {
            log(Level.WARNING, str, str2, null);
        }

        public void warn(String str, String str2, Throwable th) {
            log(Level.WARNING, str, str2, th);
        }

        public void warn(String str, String str2, Object... objArr) {
            logf(Level.WARNING, str, str2, objArr);
        }
    }

    class FileLoggerInstance {
        static final Facade INSTANCE = new Facade(LoggerFactory.getFileLogger());

        private FileLoggerInstance() {
        }
    }

    class FullFacade extends Facade {
        private FullFacade() {
            super(null);
        }

        /* access modifiers changed from: protected */
        public void doLog(Level level, String str, String str2, Throwable th, Message message) {
            LogcatLoggerInstance.INSTANCE.doLog(level, str, str2, th, message);
            FileLoggerInstance.INSTANCE.doLog(level, str, str2, th, message);
        }
    }

    class FullLoggerInstance {
        static final Facade INSTANCE = new FullFacade();

        private FullLoggerInstance() {
        }
    }

    class LogcatLoggerInstance {
        static final Facade INSTANCE = new Facade(LoggerFactory.getLogcatLogger());

        private LogcatLoggerInstance() {
        }
    }

    protected Log() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static void d(String str, String str2) {
        android.util.Log.d(str, str2);
    }

    public static void d(String str, String str2, Throwable th) {
        android.util.Log.d(str, str2, th);
    }

    public static void debug(String str, String str2) {
        LogcatLoggerInstance.INSTANCE.debug(str, str2);
    }

    public static void debug(String str, String str2, Throwable th) {
        LogcatLoggerInstance.INSTANCE.debug(str, str2, th);
    }

    public static void debug(String str, String str2, Object... objArr) {
        LogcatLoggerInstance.INSTANCE.debug(str, str2, objArr);
    }

    public static void e(String str, String str2) {
        android.util.Log.e(str, str2);
    }

    public static void e(String str, String str2, Throwable th) {
        android.util.Log.e(str, str2, th);
    }

    public static void error(String str, String str2) {
        LogcatLoggerInstance.INSTANCE.error(str, str2);
    }

    public static void error(String str, String str2, Throwable th) {
        LogcatLoggerInstance.INSTANCE.error(str, str2, th);
    }

    public static void error(String str, String str2, Object... objArr) {
        LogcatLoggerInstance.INSTANCE.error(str, str2, objArr);
    }

    public static void fatal(String str, String str2) {
        LogcatLoggerInstance.INSTANCE.fatal(str, str2);
    }

    public static void fatal(String str, String str2, Throwable th) {
        LogcatLoggerInstance.INSTANCE.fatal(str, str2, th);
    }

    public static void fatal(String str, String str2, Object... objArr) {
        LogcatLoggerInstance.INSTANCE.fatal(str, str2, objArr);
    }

    public static Facade getFileLogger() {
        return FileLoggerInstance.INSTANCE;
    }

    public static Facade getFullLogger() {
        return FullLoggerInstance.INSTANCE;
    }

    public static Facade getLogcatLogger() {
        return LogcatLoggerInstance.INSTANCE;
    }

    public static void i(String str, String str2) {
        android.util.Log.i(str, str2);
    }

    public static void i(String str, String str2, Throwable th) {
        android.util.Log.i(str, str2, th);
    }

    public static void info(String str, String str2) {
        LogcatLoggerInstance.INSTANCE.info(str, str2);
    }

    public static void info(String str, String str2, Throwable th) {
        LogcatLoggerInstance.INSTANCE.info(str, str2, th);
    }

    public static void info(String str, String str2, Object... objArr) {
        LogcatLoggerInstance.INSTANCE.info(str, str2, objArr);
    }

    public static void v(String str, String str2) {
        android.util.Log.v(str, str2);
    }

    public static void v(String str, String str2, Throwable th) {
        android.util.Log.v(str, str2, th);
    }

    public static void verbose(String str, String str2) {
        LogcatLoggerInstance.INSTANCE.verbose(str, str2);
    }

    public static void verbose(String str, String str2, Throwable th) {
        LogcatLoggerInstance.INSTANCE.verbose(str, str2, th);
    }

    public static void verbose(String str, String str2, Object... objArr) {
        LogcatLoggerInstance.INSTANCE.verbose(str, str2, objArr);
    }

    public static void w(String str, String str2) {
        android.util.Log.w(str, str2);
    }

    public static void w(String str, String str2, Throwable th) {
        android.util.Log.w(str, str2, th);
    }

    public static void warn(String str, String str2) {
        LogcatLoggerInstance.INSTANCE.warn(str, str2);
    }

    public static void warn(String str, String str2, Throwable th) {
        LogcatLoggerInstance.INSTANCE.warn(str, str2, th);
    }

    public static void warn(String str, String str2, Object... objArr) {
        LogcatLoggerInstance.INSTANCE.warn(str, str2, objArr);
    }

    public static void wtf(String str, String str2) {
        android.util.Log.wtf(str, str2);
    }

    public static void wtf(String str, String str2, Throwable th) {
        android.util.Log.wtf(str, str2, th);
    }
}

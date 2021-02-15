package com.android.camera.log;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.android.camera.CameraAppImpl;
import com.android.camera.Util;
import com.miui.internal.log.Level;
import com.miui.internal.log.Logger;
import com.miui.internal.log.appender.FileAppender;
import com.miui.internal.log.appender.rolling.FileRolloverStrategy;
import com.miui.internal.log.appender.rolling.RollingFileManager;
import com.miui.internal.log.format.SimpleFormatter;
import com.miui.internal.log.receiver.DumpReceiver;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileLogger {
    public static final String CAMERA_FILE_LOG_PATH = com.miui.internal.log.util.Config.getDefaultCacheLogDir(CameraAppImpl.getAndroidContext().getPackageName());
    public static final String MIUI_284_LOG_DIR_PATH;
    public static final String MIUI_FEEDBACK_LOG_DIR_PATH = com.miui.internal.log.util.Config.getDefaultSdcardLogDir(CameraAppImpl.getAndroidContext().getPackageName());
    private static final String TAG = "FileLogger";
    private static Config mConfig;
    private static final ExecutorService mExecutor;
    private static Logger mLogger;
    private static final ThreadFactory sThreadFactory = O0000o00.INSTANCE;

    public class Config {
        /* access modifiers changed from: private */
        public String logDir;
        /* access modifiers changed from: private */
        public String logName;
        /* access modifiers changed from: private */
        public int maxBackUpCount = 4;
        /* access modifiers changed from: private */
        public int maxFileSize = 1048576;

        public class Build {
            private String logDir;
            private String logName;
            private int maxBackUpCount = 4;
            private int maxFileSize = 1048576;

            public Config build() {
                Config config = new Config();
                config.logDir = this.logDir;
                config.logName = this.logName;
                config.maxBackUpCount = this.maxBackUpCount;
                config.maxFileSize = this.maxFileSize;
                return config;
            }

            public Build setLogDir(String str) {
                this.logDir = str;
                return this;
            }

            public Build setLogName(String str) {
                this.logName = str;
                return this;
            }

            public Build setMaxBackUpCount(int i) {
                this.maxBackUpCount = i;
                return this;
            }

            public Build setMaxFileSize(int i) {
                this.maxFileSize = i;
                return this;
            }
        }

        public static Build newBuild() {
            return new Build();
        }

        public String getLogDir() {
            return this.logDir;
        }

        public String getLogName() {
            return this.logName;
        }

        public int getMaxBackUpCount() {
            return this.maxBackUpCount;
        }

        public long getMaxFileSize() {
            return (long) this.maxFileSize;
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/MIUI/debug_log/common/");
        sb.append(CameraAppImpl.getAndroidContext().getPackageName());
        MIUI_284_LOG_DIR_PATH = sb.toString();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue(), sThreadFactory, O00000o.INSTANCE);
        mExecutor = threadPoolExecutor;
    }

    private FileLogger() {
    }

    static /* synthetic */ Thread O000000o(Runnable runnable) {
        Thread thread = new Thread(runnable, "file-logger");
        thread.setPriority(1);
        return thread;
    }

    static /* synthetic */ void O000000o(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        StringBuilder sb = new StringBuilder();
        sb.append("rejectedExecution ");
        sb.append(runnable);
        Log.w(TAG, sb.toString());
    }

    public static void c(String str, String str2) {
        if (mLogger != null && !TextUtils.isEmpty(str2)) {
            mExecutor.execute(new O0000Oo(str, str2));
        }
    }

    public static void d(String str, String str2) {
        if (mLogger != null && !TextUtils.isEmpty(str2)) {
            mExecutor.execute(new O00000Oo(str, str2));
        }
    }

    public static void d(String str, String str2, Throwable th) {
        if (mLogger != null) {
            if (!TextUtils.isEmpty(str2) || th != null) {
                if (TextUtils.isEmpty(str2)) {
                    str2 = th.getMessage();
                    if (TextUtils.isEmpty(str2)) {
                        return;
                    }
                }
                mExecutor.execute(new C0340O0000OoO(str, str2, th));
            }
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        d(str, String.format(str2, objArr));
    }

    public static void e(String str, String str2) {
        if (mLogger != null && !TextUtils.isEmpty(str2)) {
            mExecutor.execute(new C0339O00000oo(str, str2));
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (mLogger != null) {
            if (!TextUtils.isEmpty(str2) || th != null) {
                if (TextUtils.isEmpty(str2)) {
                    str2 = th.getMessage();
                    if (TextUtils.isEmpty(str2)) {
                        return;
                    }
                }
                mExecutor.execute(new O0000O0o(str, str2, th));
            }
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        e(str, String.format(str2, objArr));
    }

    public static void i(String str, String str2) {
        if (mLogger != null && !TextUtils.isEmpty(str2)) {
            mExecutor.execute(new C0338O00000oO(str, str2));
        }
    }

    public static void i(String str, String str2, Throwable th) {
        if (mLogger != null) {
            if (!TextUtils.isEmpty(str2) || th != null) {
                if (TextUtils.isEmpty(str2)) {
                    str2 = th.getMessage();
                    if (TextUtils.isEmpty(str2)) {
                        return;
                    }
                }
                mExecutor.execute(new C0341O0000Ooo(str, str2, th));
            }
        }
    }

    public static void init(Context context, Config config) {
        mConfig = config;
        if (mConfig.logDir == null) {
            mConfig.logDir = CAMERA_FILE_LOG_PATH;
        }
        if (mConfig.logName == null) {
            mConfig.logName = context.getPackageName();
        }
        Logger logger = new Logger(mConfig.logName);
        FileAppender fileAppender = new FileAppender();
        fileAppender.setFormatter(new SimpleFormatter());
        FileRolloverStrategy fileRolloverStrategy = new FileRolloverStrategy();
        fileRolloverStrategy.setMaxBackupIndex(mConfig.maxBackUpCount);
        fileRolloverStrategy.setMaxFileSize(mConfig.maxFileSize);
        RollingFileManager rollingFileManager = new RollingFileManager(mConfig.logDir, mConfig.logName);
        rollingFileManager.setRolloverStrategy(fileRolloverStrategy);
        fileAppender.setFileManager(rollingFileManager);
        logger.addAppender(fileAppender);
        logger.setLevel(Util.isDebugOsBuild() ? Level.DEBUG : Level.INFO);
        registerDumpLogReceiver();
        mLogger = logger;
    }

    private static void registerDumpLogReceiver() {
        Log.d(TAG, "registerDumpLogReceiver: ");
        CameraAppImpl.getAndroidContext().registerReceiver(new DumpReceiver(mConfig.logDir, MIUI_FEEDBACK_LOG_DIR_PATH), new IntentFilter(DumpReceiver.INTENT_ACTION_DUMP_CACHED_LOG), "miui.permission.DUMP_CACHED_LOG", null);
        Dump284LogReceiver dump284LogReceiver = new Dump284LogReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SECRET_CODE");
        intentFilter.addDataScheme("android_secret_code");
        intentFilter.addDataAuthority("284", null);
        CameraAppImpl.getAndroidContext().registerReceiver(dump284LogReceiver, intentFilter);
    }

    public static void v(String str, String str2) {
        if (mLogger != null && !TextUtils.isEmpty(str2)) {
            mExecutor.execute(new O000000o(str, str2));
        }
    }

    public static void v(String str, String str2, Throwable th) {
        if (mLogger != null && !TextUtils.isEmpty(str2)) {
            mExecutor.execute(new O0000OOo(str, str2, th));
        }
    }

    public static void w(String str, String str2) {
        if (mLogger != null && !TextUtils.isEmpty(str2)) {
            mExecutor.execute(new O0000Oo0(str, str2));
        }
    }

    public static void w(String str, String str2, Throwable th) {
        if (mLogger != null) {
            if (!TextUtils.isEmpty(str2) || th != null) {
                if (TextUtils.isEmpty(str2)) {
                    str2 = th.getMessage();
                    if (TextUtils.isEmpty(str2)) {
                        return;
                    }
                }
                mExecutor.execute(new O0000o0(str, str2, th));
            }
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        w(str, String.format(str2, objArr));
    }

    public static void w(String str, Throwable th) {
        if (mLogger != null && th != null) {
            mExecutor.execute(new O00000o0(str, th));
        }
    }
}

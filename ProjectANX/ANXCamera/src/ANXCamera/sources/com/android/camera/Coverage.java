package com.android.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.camera.log.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jacoco.agent.rt.RT;

public class Coverage {
    private static final String COLLECT_FILE_EC_ACTION = "android.intent.action.GET_COVERAGE_EC";
    private static final String DEFAULT_COVERAGE_FILE_PATH = "/data/local/tmp";
    private static final boolean ENABLED;
    private static final String TAG = "Coverage";

    class CoverageCommand extends BroadcastReceiver {
        CoverageCommand() {
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x006f A[SYNTHETIC, Splitter:B:16:0x006f] */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x007e A[SYNTHETIC, Splitter:B:21:0x007e] */
        /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void generateCoverageReport() {
            FileOutputStream fileOutputStream;
            Exception e;
            String str = Coverage.TAG;
            String format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS").format(new Date());
            StringBuilder sb = new StringBuilder();
            sb.append("/data/local/tmp/coverage_");
            sb.append(format);
            sb.append(".ec");
            File file = new File(sb.toString());
            try {
                fileOutputStream = new FileOutputStream(file, false);
                try {
                    fileOutputStream.write(RT.getAgent().getExecutionData(false));
                    fileOutputStream.flush();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("save file: ");
                    sb2.append(file);
                    Log.d(str, sb2.toString());
                    try {
                        fileOutputStream.close();
                    } catch (IOException e2) {
                        Log.e(str, e2.getMessage());
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        Log.d(str, e.toString());
                        if (fileOutputStream == null) {
                            fileOutputStream.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (fileOutputStream != null) {
                        }
                        throw th;
                    }
                }
            } catch (Exception e4) {
                fileOutputStream = null;
                e = e4;
                Log.d(str, e.toString());
                if (fileOutputStream == null) {
                }
            } catch (Throwable th2) {
                fileOutputStream = null;
                th = th2;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e5) {
                        Log.e(str, e5.getMessage());
                    }
                }
                throw th;
            }
        }

        public void onReceive(Context context, Intent intent) {
            if (Coverage.COLLECT_FILE_EC_ACTION.equalsIgnoreCase(intent.getAction())) {
                new Thread(new Runnable() {
                    public void run() {
                        CoverageCommand.this.generateCoverageReport();
                    }
                }).start();
            }
        }
    }

    static {
        boolean z;
        try {
            Class.forName("org.jacoco.agent.rt.RT");
            z = true;
        } catch (ClassNotFoundException unused) {
            z = false;
        }
        ENABLED = z;
    }

    static void initCoverageService(Context context) {
        if (ENABLED) {
            context.registerReceiver(new CoverageCommand(), new IntentFilter(COLLECT_FILE_EC_ACTION));
        }
    }
}

package com.miui.internal.hybrid;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SecurityManager {
    private static final String KEY_FILE_NAME = "hybrid_key.pem";
    private static String sPublicKey;
    private Config mConfig;
    private long mExpiredTime;
    private String mSign;
    private Boolean mValidSignature;

    public SecurityManager(Config config, Context context) {
        this.mConfig = config;
        if (!(config == null || config.getSecurity() == null)) {
            this.mExpiredTime = config.getSecurity().getTimestamp();
            this.mSign = config.getSecurity().getSignature();
        }
        if (sPublicKey == null) {
            sPublicKey = getPublicKey(context);
        }
    }

    private File getHybridBaseFolder(Context context) {
        return new File(context.getFilesDir(), "miuisdk");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:17|16|19|20|21) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0042, code lost:
        r3 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0052, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0053, code lost:
        r4.printStackTrace();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0044 */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004e A[SYNTHETIC, Splitter:B:23:0x004e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String getPublicKey(Context context) {
        File file = new File(getHybridBaseFolder(context), KEY_FILE_NAME);
        BufferedReader bufferedReader = null;
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(file.exists() ? new FileInputStream(file) : context.getResources().getAssets().open("keys/hybrid_key.pem")));
        try {
            String readPublicKey = readPublicKey(bufferedReader2);
            try {
                bufferedReader2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return readPublicKey;
        } catch (IOException unused) {
            bufferedReader = bufferedReader2;
            throw new IllegalStateException("cannot read hybrid key.");
        } catch (Throwable th) {
            th = th;
            bufferedReader = bufferedReader2;
            if (bufferedReader != null) {
            }
            throw th;
        }
    }

    private boolean isValidSignature(String str, String str2) {
        return str2 != null && SignUtils.verify(str, SignUtils.getPublicKey(sPublicKey), str2);
    }

    private String readPublicKey(BufferedReader bufferedReader) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return sb.substring(0, sb.length() - 1);
            }
            if (!"".equals(readLine.trim())) {
                if (!readLine.startsWith("-----")) {
                    sb.append(readLine);
                    sb.append(13);
                }
            }
        }
    }

    public boolean isExpired() {
        long j = this.mExpiredTime;
        return 0 < j && j < System.currentTimeMillis();
    }

    public boolean isValidSignature() {
        if (this.mValidSignature == null) {
            try {
                this.mValidSignature = Boolean.valueOf(isValidSignature(ConfigUtils.getRawConfig(this.mConfig), this.mSign));
            } catch (Exception unused) {
                this.mValidSignature = Boolean.valueOf(false);
            }
        }
        return this.mValidSignature.booleanValue();
    }
}

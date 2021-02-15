package com.xiaomi.asr.engine.record;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XSpeedStreamingFileRecorderSource extends AudioSource {
    private static final String TAG = "XSpeedStreamingFileRecorderSource";
    private String _fileName;
    private final AudioType a;
    private InputStream mInputStream = null;

    public XSpeedStreamingFileRecorderSource(AudioType audioType, String str) {
        this.a = audioType;
        this._fileName = str;
    }

    private void initData() {
        String str = "";
        this.mInputStream = null;
        try {
            this.mInputStream = new FileInputStream(new File(this._fileName));
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(this.mInputStream.available());
            Log.e(str2, sb.toString());
        } catch (IOException e) {
            this.mInputStream = null;
            Log.e(TAG, str, e);
        }
    }

    private static void slientSleep(long j) {
        try {
            Thread.sleep(j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        initData();
    }

    public int read(byte[] bArr, int i, int i2) {
        InputStream inputStream = this.mInputStream;
        int i3 = -1;
        if (inputStream == null) {
            return -1;
        }
        try {
            i3 = inputStream.read(bArr, i, i2);
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
        return i3;
    }

    public void release() {
        InputStream inputStream = this.mInputStream;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        }
    }

    public void startRecording() {
    }
}

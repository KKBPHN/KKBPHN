package com.xiaomi.asr.engine.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.google.android.apps.photos.api.PhotosOemApi;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SaveVoiceUtil {
    /* access modifiers changed from: private */
    public static String RECORD_DIR = "/sdcard/miasr/voiceprint/record";
    private static final String TAG = "SaveVoiceUtil";
    /* access modifiers changed from: private */
    public FileOutputStream mOutputStream;
    private Handler mSaveHandle;
    private HandlerThread mSaveThread = new HandlerThread("save_voice");

    class SaveVoiceCallback implements Callback {
        SaveVoiceCallback() {
        }

        public boolean handleMessage(Message message) {
            StringBuilder sb;
            String str;
            String str2;
            int i = message.what;
            String str3 = SaveVoiceUtil.TAG;
            if (i != 1) {
                if (i == 2) {
                    byte[] byteArray = message.getData().getByteArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
                    if (SaveVoiceUtil.this.mOutputStream != null) {
                        try {
                            SaveVoiceUtil.this.mOutputStream.write(byteArray);
                        } catch (IOException e) {
                            e = e;
                            e.printStackTrace();
                            sb = new StringBuilder();
                            str2 = "write error:";
                        }
                    }
                } else if (i == 3 && SaveVoiceUtil.this.mOutputStream != null) {
                    try {
                        SaveVoiceUtil.this.mOutputStream.close();
                        SaveVoiceUtil.this.mOutputStream = null;
                    } catch (IOException e2) {
                        e = e2;
                        e.printStackTrace();
                        sb = new StringBuilder();
                        str2 = "close error:";
                    }
                }
            } else if (SaveVoiceUtil.this.checkAndCreateDir(SaveVoiceUtil.RECORD_DIR)) {
                try {
                    SaveVoiceUtil saveVoiceUtil = SaveVoiceUtil.this;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(SaveVoiceUtil.RECORD_DIR);
                    sb2.append("/");
                    sb2.append(SaveVoiceUtil.this.getCurrentTime());
                    sb2.append("_");
                    sb2.append((String) message.obj);
                    sb2.append(".cmvn");
                    saveVoiceUtil.mOutputStream = new FileOutputStream(sb2.toString());
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                    sb = new StringBuilder();
                    sb.append("create error:");
                    str = e3.getMessage();
                }
            }
            return false;
            sb.append(str2);
            str = e.getMessage();
            sb.append(str);
            Log.e(str3, sb.toString());
            return false;
            sb.append(str);
            Log.e(str3, sb.toString());
            return false;
        }
    }

    public SaveVoiceUtil() {
        this.mSaveThread.start();
        this.mSaveHandle = new Handler(this.mSaveThread.getLooper(), new SaveVoiceCallback());
    }

    /* access modifiers changed from: private */
    public boolean checkAndCreateDir(String str) {
        File file = new File(str);
        return file.exists() || file.mkdirs();
    }

    /* access modifiers changed from: private */
    public String getCurrentTime() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(1);
        int i2 = instance.get(2);
        int i3 = instance.get(5);
        int i4 = instance.get(11);
        int i5 = instance.get(12);
        int i6 = instance.get(13);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(i);
        String str = "-";
        stringBuffer.append(str);
        stringBuffer.append(i2 + 1);
        stringBuffer.append(str);
        stringBuffer.append(i3);
        stringBuffer.append(str);
        stringBuffer.append(i4);
        stringBuffer.append(str);
        stringBuffer.append(i5);
        stringBuffer.append(str);
        stringBuffer.append(i6);
        return stringBuffer.toString();
    }

    public void closeFile() {
        this.mSaveHandle.sendMessage(this.mSaveHandle.obtainMessage(3));
    }

    public void createFile(String str) {
        Message obtainMessage = this.mSaveHandle.obtainMessage(1);
        obtainMessage.obj = str;
        this.mSaveHandle.sendMessage(obtainMessage);
    }

    public void setSaveDir(String str) {
        RECORD_DIR = str;
    }

    public void writeFile(byte[] bArr) {
        if (bArr != null && bArr.length > 0) {
            Message obtainMessage = this.mSaveHandle.obtainMessage(2);
            Bundle bundle = new Bundle();
            bundle.putByteArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, bArr);
            obtainMessage.setData(bundle);
            this.mSaveHandle.sendMessage(obtainMessage);
        }
    }
}

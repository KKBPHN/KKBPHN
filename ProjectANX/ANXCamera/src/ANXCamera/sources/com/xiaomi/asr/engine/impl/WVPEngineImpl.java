package com.xiaomi.asr.engine.impl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.apps.photos.api.PhotosOemApi;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import com.xiaomi.asr.engine.PhraseWakeupResult;
import com.xiaomi.asr.engine.WVPListener;
import com.xiaomi.asr.engine.jni.WakeupEngineInterface;
import com.xiaomi.asr.engine.record.VoiceRecord;
import com.xiaomi.asr.engine.record.VoiceRecord.RecordListener;
import com.xiaomi.asr.engine.utils.CircleBuffer;
import com.xiaomi.asr.engine.utils.SaveVoiceUtil;

public class WVPEngineImpl {
    private static final int FEED_RESULT_DETECTED_END = 2;
    private static final int FEED_RESULT_DETECTED_LEVEL_ONE = 1;
    private static final String SDK_VERSION = "wvp_sdk_202006011636";
    private static final String TAG = "WVPEngineImpl";
    private static final int WAKEUP_BUFFER_SIZE = 640;
    private static WVPEngineImpl mInstance;
    /* access modifiers changed from: private */
    public boolean init = false;
    /* access modifiers changed from: private */
    public boolean isPrintLog = false;
    /* access modifiers changed from: private */
    public boolean isWakeup;
    /* access modifiers changed from: private */
    public boolean isWriteBackward;
    /* access modifiers changed from: private */
    public CircleBuffer mBackwardData;
    /* access modifiers changed from: private */
    public byte[] mCacheData;
    private String mDebugPath;
    /* access modifiers changed from: private */
    public CircleBuffer mForwardData;
    /* access modifiers changed from: private */
    public WVPListener mOutListener;
    /* access modifiers changed from: private */
    public int mReturnIndex = 0;
    /* access modifiers changed from: private */
    public String mSoundVerifyStr;
    /* access modifiers changed from: private */
    public long mSplitDataHandleTime;
    /* access modifiers changed from: private */
    public long mVoiceDataSize;
    /* access modifiers changed from: private */
    public VoicePrintManager mVoicePrintManager;
    /* access modifiers changed from: private */
    public VoiceRecord mVoiceRecord;
    /* access modifiers changed from: private */
    public WakeupEngineInterface mWakeupEngineInterface;
    /* access modifiers changed from: private */
    public long mWakeupLevelOneEndTime;
    /* access modifiers changed from: private */
    public long mWakeupLevelOneHandleTime;
    /* access modifiers changed from: private */
    public Handler mWorkHandler;
    private HandlerThread mWorkHandlerThread;
    /* access modifiers changed from: private */
    public boolean openVoicePrint;
    /* access modifiers changed from: private */
    public boolean pause = false;
    /* access modifiers changed from: private */
    public boolean running = false;
    /* access modifiers changed from: private */
    public boolean saveRecord = false;
    /* access modifiers changed from: private */
    public SaveVoiceUtil saveVoiceUtil;

    class HandlerCallback implements Callback {
        HandlerCallback() {
        }

        public boolean handleMessage(Message message) {
            int i;
            Message message2 = message;
            boolean access$200 = WVPEngineImpl.this.isPrintLog;
            String str = WVPEngineImpl.TAG;
            if (access$200) {
                StringBuilder sb = new StringBuilder();
                sb.append("start msg.what:");
                sb.append(message2.what);
                Log.d(str, sb.toString());
            }
            int i2 = message2.what;
            if (i2 != 16) {
                String str2 = "";
                switch (i2) {
                    case 1:
                        Bundle data = message.getData();
                        String string = data.getString(ComposerHelper.CONFIG_PATH);
                        String string2 = data.getString("name");
                        if (WVPEngineImpl.this.isPrintLog) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("vp_model_path:");
                            sb2.append(string);
                            sb2.append(", vp_speaker:");
                            sb2.append(string2);
                            Log.d(str, sb2.toString());
                        }
                        if (WVPEngineImpl.this.mWakeupEngineInterface.wakeupInit(str2) + WVPEngineImpl.this.mVoicePrintManager.init(string, string2) == 0) {
                            WVPEngineImpl.this.init = true;
                        }
                        if (!WVPEngineImpl.this.init) {
                            Log.e(str, "init failed");
                        }
                        if (WVPEngineImpl.this.mOutListener != null) {
                            WVPEngineImpl.this.mOutListener.onInit(WVPEngineImpl.this.init);
                            break;
                        }
                        break;
                    case 2:
                        if (WVPEngineImpl.this.init) {
                            WVPEngineImpl.this.mWorkHandler.removeMessages(6);
                            if (!WVPEngineImpl.this.running) {
                                WVPEngineImpl.this.mVoiceRecord.startRecord();
                                WVPEngineImpl.this.running = true;
                            }
                            WVPEngineImpl.this.pause = false;
                            break;
                        }
                        break;
                    case 3:
                        if (WVPEngineImpl.this.init && !WVPEngineImpl.this.pause) {
                            byte[] byteArray = message.getData().getByteArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
                            int i3 = message2.arg1;
                            long currentTimeMillis = System.currentTimeMillis();
                            int i4 = i3 / 2;
                            byte[] bArr = new byte[i4];
                            int i5 = 0;
                            int i6 = 0;
                            while (i5 < i3) {
                                bArr[i6] = byteArray[i5];
                                bArr[i6 + 1] = byteArray[i5 + 1];
                                i5 += 4;
                                i6 += 2;
                            }
                            if (WVPEngineImpl.this.isPrintLog) {
                                WVPEngineImpl wVPEngineImpl = WVPEngineImpl.this;
                                wVPEngineImpl.mSplitDataHandleTime = wVPEngineImpl.mSplitDataHandleTime + (System.currentTimeMillis() - currentTimeMillis);
                            }
                            WVPEngineImpl.this.mForwardData.writeBuffer(bArr);
                            long currentTimeMillis2 = System.currentTimeMillis();
                            int wakeupFeedData = WVPEngineImpl.this.mWakeupEngineInterface.wakeupFeedData(bArr, i4, 0);
                            if (WVPEngineImpl.this.isPrintLog) {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("wakeup feed return:");
                                sb3.append(wakeupFeedData);
                                Log.d(str, sb3.toString());
                                WVPEngineImpl wVPEngineImpl2 = WVPEngineImpl.this;
                                wVPEngineImpl2.mWakeupLevelOneHandleTime = wVPEngineImpl2.mWakeupLevelOneHandleTime + (System.currentTimeMillis() - currentTimeMillis2);
                                WVPEngineImpl wVPEngineImpl3 = WVPEngineImpl.this;
                                wVPEngineImpl3.mVoiceDataSize = wVPEngineImpl3.mVoiceDataSize + ((long) i4);
                            }
                            String str3 = "\n";
                            if (wakeupFeedData == 1) {
                                if (WVPEngineImpl.this.isPrintLog) {
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("wakeup-voice-level-one, voice data size:");
                                    sb4.append(WVPEngineImpl.this.mVoiceDataSize);
                                    sb4.append(", voice handle time:");
                                    sb4.append(WVPEngineImpl.this.mWakeupLevelOneHandleTime);
                                    sb4.append(", rtf:");
                                    String str4 = "%.3f";
                                    sb4.append(String.format(str4, new Object[]{Double.valueOf(((double) WVPEngineImpl.this.mWakeupLevelOneHandleTime) / (((double) WVPEngineImpl.this.mVoiceDataSize) / 32.0d))}));
                                    sb4.append(", split data handle time:");
                                    sb4.append(WVPEngineImpl.this.mSplitDataHandleTime);
                                    sb4.append(", split rtf:");
                                    sb4.append(String.format(str4, new Object[]{Double.valueOf(((double) WVPEngineImpl.this.mSplitDataHandleTime) / (((double) WVPEngineImpl.this.mVoiceDataSize) / 32.0d))}));
                                    String sb5 = sb4.toString();
                                    if (WVPEngineImpl.this.mOutListener != null) {
                                        WVPListener access$300 = WVPEngineImpl.this.mOutListener;
                                        StringBuilder sb6 = new StringBuilder();
                                        sb6.append(sb5);
                                        sb6.append(str3);
                                        access$300.onDebug(sb6.toString());
                                    }
                                    WVPEngineImpl.this.mWakeupLevelOneEndTime = System.currentTimeMillis();
                                    WVPEngineImpl.this.mWakeupLevelOneHandleTime = 0;
                                    WVPEngineImpl.this.mSplitDataHandleTime = 0;
                                    WVPEngineImpl.this.mVoiceDataSize = 0;
                                }
                                WVPEngineImpl.this.mReturnIndex = 0;
                                WVPEngineImpl.this.isWakeup = false;
                                WVPEngineImpl.this.mSoundVerifyStr = str2;
                                WVPEngineImpl.this.voicePrintReco();
                                WVPEngineImpl wVPEngineImpl4 = WVPEngineImpl.this;
                                wVPEngineImpl4.mCacheData = wVPEngineImpl4.mForwardData.readBuffer();
                                WVPEngineImpl.this.mForwardData.reset();
                                WVPEngineImpl.this.isWriteBackward = true;
                                i = 2;
                            } else {
                                i = 2;
                            }
                            if (wakeupFeedData == i) {
                                if (WVPEngineImpl.this.isPrintLog) {
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append("wakeup-voice-level-two, handle time:");
                                    sb7.append(System.currentTimeMillis() - WVPEngineImpl.this.mWakeupLevelOneEndTime);
                                    String sb8 = sb7.toString();
                                    if (WVPEngineImpl.this.mOutListener != null) {
                                        WVPListener access$3002 = WVPEngineImpl.this.mOutListener;
                                        StringBuilder sb9 = new StringBuilder();
                                        sb9.append(sb8);
                                        sb9.append(str3);
                                        access$3002.onDebug(sb9.toString());
                                    }
                                }
                                WVPEngineImpl.this.isWakeup = true;
                                Message obtainMessage = WVPEngineImpl.this.mWorkHandler.obtainMessage(7);
                                obtainMessage.arg1 = 1;
                                obtainMessage.obj = "keyword detected";
                                WVPEngineImpl.this.mWorkHandler.sendMessage(obtainMessage);
                            }
                            if (WVPEngineImpl.this.isWriteBackward) {
                                WVPEngineImpl.this.mBackwardData.writeBuffer(bArr);
                            }
                            if (WVPEngineImpl.this.mBackwardData.isFull()) {
                                if (!WVPEngineImpl.this.isWakeup) {
                                    if (WVPEngineImpl.this.mOutListener != null) {
                                        PhraseWakeupResult phraseWakeupResult = new PhraseWakeupResult();
                                        phraseWakeupResult.setVoconWakeupPassed(false);
                                        phraseWakeupResult.setVoconPhrase(str2);
                                        WVPEngineImpl.this.mOutListener.onPhraseSpotted(phraseWakeupResult);
                                    }
                                    WVPEngineImpl.this.mWakeupEngineInterface.wakeupReset();
                                }
                                byte[] readBuffer = WVPEngineImpl.this.mBackwardData.readBuffer();
                                if (WVPEngineImpl.this.mOutListener != null) {
                                    byte[] bArr2 = new byte[(WVPEngineImpl.this.mCacheData.length + readBuffer.length)];
                                    System.arraycopy(WVPEngineImpl.this.mCacheData, 0, bArr2, 0, WVPEngineImpl.this.mCacheData.length);
                                    System.arraycopy(readBuffer, 0, bArr2, WVPEngineImpl.this.mCacheData.length, readBuffer.length);
                                    WVPEngineImpl.this.mOutListener.onAudioData(bArr2);
                                }
                                WVPEngineImpl.this.mBackwardData.reset();
                                WVPEngineImpl.this.mCacheData = null;
                                WVPEngineImpl.this.isWriteBackward = false;
                                break;
                            }
                        }
                        break;
                    case 4:
                        if (WVPEngineImpl.this.init) {
                            WVPEngineImpl.this.pause = true;
                            break;
                        }
                        break;
                    case 5:
                        if (WVPEngineImpl.this.init) {
                            if (WVPEngineImpl.this.running) {
                                WVPEngineImpl.this.mVoiceRecord.stopRecord();
                                WVPEngineImpl.this.running = false;
                            }
                            WVPEngineImpl.this.mWakeupEngineInterface.wakeupDestroy();
                            WVPEngineImpl.this.mVoicePrintManager.release();
                            WVPEngineImpl.this.init = false;
                            WVPEngineImpl.this.pause = false;
                            if (WVPEngineImpl.this.mOutListener != null) {
                                WVPEngineImpl.this.mOutListener.onRelease();
                                break;
                            }
                        }
                        break;
                    case 6:
                        if (WVPEngineImpl.this.running) {
                            WVPEngineImpl.this.mVoiceRecord.stopRecord();
                            WVPEngineImpl.this.running = false;
                            break;
                        }
                        break;
                    case 7:
                        if (message2.arg1 != 1) {
                            Log.d(str, "end cal sv");
                            WVPEngineImpl.this.mSoundVerifyStr = (String) message2.obj;
                        } else if (!TextUtils.isEmpty((String) message2.obj)) {
                            Log.d(str, "wakeup level two");
                            WVPEngineImpl.this.isWakeup = true;
                        }
                        WVPEngineImpl wVPEngineImpl5 = WVPEngineImpl.this;
                        wVPEngineImpl5.mReturnIndex = wVPEngineImpl5.mReturnIndex + 1;
                        if (WVPEngineImpl.this.mReturnIndex >= 2 && WVPEngineImpl.this.isWakeup) {
                            if (WVPEngineImpl.this.mSoundVerifyStr.contains("stranger") || WVPEngineImpl.this.mSoundVerifyStr.contains("error:empty") || WVPEngineImpl.this.mSoundVerifyStr.contains("recognition failed")) {
                                if (WVPEngineImpl.this.isPrintLog) {
                                    StringBuilder sb10 = new StringBuilder();
                                    sb10.append("sv wakeup fail, sv res:");
                                    sb10.append(WVPEngineImpl.this.mSoundVerifyStr);
                                    Log.d(str, sb10.toString());
                                    break;
                                }
                            } else {
                                if (WVPEngineImpl.this.isPrintLog) {
                                    StringBuilder sb11 = new StringBuilder();
                                    sb11.append("sv-wakeup, res:");
                                    sb11.append(WVPEngineImpl.this.mSoundVerifyStr);
                                    Log.d(str, sb11.toString());
                                }
                                if (WVPEngineImpl.this.mOutListener != null) {
                                    PhraseWakeupResult phraseWakeupResult2 = new PhraseWakeupResult();
                                    phraseWakeupResult2.setVoconWakeupPassed(true);
                                    phraseWakeupResult2.setVoconPhrase("小爱同学");
                                    phraseWakeupResult2.setScore(0.0f);
                                    phraseWakeupResult2.setAec(false);
                                    WVPEngineImpl.this.mOutListener.onPhraseSpotted(phraseWakeupResult2);
                                    break;
                                }
                            }
                        }
                        break;
                    case 8:
                        if (WVPEngineImpl.this.init) {
                            WVPEngineImpl.this.mVoicePrintManager.startEnrollment(message2.arg1);
                            break;
                        }
                        break;
                    case 9:
                        if (WVPEngineImpl.this.init) {
                            WVPEngineImpl.this.mVoicePrintManager.removeAllRegister();
                            break;
                        }
                        break;
                }
            } else if (WVPEngineImpl.this.init) {
                WVPEngineImpl.this.mVoicePrintManager.generateModel();
            }
            if (WVPEngineImpl.this.isPrintLog) {
                StringBuilder sb12 = new StringBuilder();
                sb12.append("end msg.what:");
                sb12.append(message2.what);
                Log.d(str, sb12.toString());
            }
            return false;
        }
    }

    class VoiceRecordListener implements RecordListener {
        VoiceRecordListener() {
        }

        public void onAudioSessionId(int i) {
        }

        public void onRecordCreateError() {
            if (WVPEngineImpl.this.mOutListener != null) {
                WVPEngineImpl.this.mOutListener.onConflictAudio();
            }
        }

        public void onRecordRelease() {
            if (WVPEngineImpl.this.mOutListener != null) {
                WVPEngineImpl.this.mOutListener.onStopAudio();
            }
        }

        public void onRecording(byte[] bArr, int i) {
            byte[] bArr2 = new byte[i];
            System.arraycopy(bArr, 0, bArr2, 0, i);
            Message obtainMessage = WVPEngineImpl.this.mWorkHandler.obtainMessage(3);
            Bundle bundle = new Bundle();
            bundle.putByteArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, bArr2);
            obtainMessage.setData(bundle);
            obtainMessage.arg1 = i;
            WVPEngineImpl.this.mWorkHandler.sendMessage(obtainMessage);
            if (WVPEngineImpl.this.saveRecord) {
                WVPEngineImpl.this.saveVoiceUtil.writeFile(bArr2);
            }
        }

        public void onRecordingEnd() {
            if (WVPEngineImpl.this.saveRecord) {
                WVPEngineImpl.this.saveVoiceUtil.closeFile();
            }
        }

        public void onRecordingFailed() {
            if (WVPEngineImpl.this.mOutListener != null) {
                WVPEngineImpl.this.mOutListener.onConflictAudio();
            }
        }

        public void onRecordingStart() {
            if (WVPEngineImpl.this.mOutListener != null) {
                WVPEngineImpl.this.mOutListener.onStartAudio();
            }
            if (WVPEngineImpl.this.saveRecord) {
                WVPEngineImpl.this.saveVoiceUtil.createFile("record");
            }
        }
    }

    private WVPEngineImpl() {
        String str = "";
        this.mSoundVerifyStr = str;
        this.isWakeup = false;
        this.openVoicePrint = false;
        this.mDebugPath = str;
        this.mWakeupLevelOneHandleTime = 0;
        this.mVoiceDataSize = 0;
        this.mWakeupLevelOneEndTime = 0;
        this.mSplitDataHandleTime = 0;
        WakeupEngineInterface.loadLibrary("wakeup-engine");
        this.mWakeupEngineInterface = new WakeupEngineInterface();
        this.mVoiceRecord = new VoiceRecord(new VoiceRecordListener());
        this.mVoiceRecord.setParam(1, 1);
        this.mVoiceRecord.setParam(2, WAKEUP_BUFFER_SIZE);
        this.mVoiceRecord.setParam(0, 12);
        this.mWorkHandlerThread = new HandlerThread("wvp-engine-impl-thread");
        this.mWorkHandlerThread.start();
        this.mWorkHandler = new Handler(this.mWorkHandlerThread.getLooper(), new HandlerCallback());
        this.mForwardData = new CircleBuffer(64000);
        this.mBackwardData = new CircleBuffer(19200);
        this.mVoicePrintManager = new VoicePrintManager("voiceprint-engine-lab");
        this.saveVoiceUtil = new SaveVoiceUtil();
    }

    public static WVPEngineImpl getInstance() {
        if (mInstance == null) {
            synchronized (WVPEngineImpl.class) {
                if (mInstance == null) {
                    mInstance = new WVPEngineImpl();
                }
            }
        }
        return mInstance;
    }

    /* access modifiers changed from: private */
    public void voicePrintReco() {
        int wakeupGetFrameSize = this.mWakeupEngineInterface.wakeupGetFrameSize() * m.ch;
        byte[] readBuffer = this.mForwardData.readBuffer();
        final byte[] bArr = new byte[wakeupGetFrameSize];
        System.arraycopy(readBuffer, readBuffer.length - wakeupGetFrameSize, bArr, 0, wakeupGetFrameSize);
        new Thread(new Runnable() {
            public void run() {
                String str;
                if (WVPEngineImpl.this.openVoicePrint) {
                    long currentTimeMillis = System.currentTimeMillis();
                    str = WVPEngineImpl.this.mVoicePrintManager.recognizeVoice(bArr, -1, 0);
                    if (WVPEngineImpl.this.isPrintLog) {
                        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                        if (WVPEngineImpl.this.mOutListener != null) {
                            WVPListener access$300 = WVPEngineImpl.this.mOutListener;
                            StringBuilder sb = new StringBuilder();
                            sb.append(str);
                            sb.append(", data len:");
                            sb.append(bArr.length);
                            sb.append(", handle time:");
                            sb.append(currentTimeMillis2);
                            sb.append(", rtf:");
                            sb.append(String.format("%.3f", new Object[]{Double.valueOf(((double) currentTimeMillis2) / (((double) bArr.length) / 32.0d))}));
                            sb.append("\n");
                            access$300.onDebug(sb.toString());
                        }
                    }
                } else {
                    str = "zhang_san:0.7813";
                }
                Message obtainMessage = WVPEngineImpl.this.mWorkHandler.obtainMessage(7);
                obtainMessage.arg1 = 2;
                obtainMessage.obj = str;
                WVPEngineImpl.this.mWorkHandler.sendMessageAtFrontOfQueue(obtainMessage);
            }
        }).start();
    }

    public void abortEnrollment() {
        this.mVoicePrintManager.abortEnrollment();
    }

    public void cancelEnrollment() {
        this.mVoicePrintManager.cancelEnrollment();
    }

    public void commitEnrollment() {
        this.mVoicePrintManager.commitEnrollment();
    }

    public void generateModel() {
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(16));
    }

    public String getAllRegister() {
        return this.mVoicePrintManager.getAllRegister();
    }

    public void init(String str, String str2) {
        String str3 = "";
        if (TextUtils.isEmpty(str)) {
            str = str3;
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = str3;
        }
        Message obtainMessage = this.mWorkHandler.obtainMessage(1);
        Bundle bundle = new Bundle();
        bundle.putString(ComposerHelper.CONFIG_PATH, str);
        bundle.putString("name", str2);
        obtainMessage.setData(bundle);
        this.mWorkHandler.sendMessage(obtainMessage);
    }

    public void openLog(boolean z) {
        this.isPrintLog = z;
    }

    public void openVoicePrint(boolean z) {
        this.openVoicePrint = z;
    }

    public void release() {
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(5));
        this.mWorkHandler.removeMessages(6);
    }

    public void removeAllRegister() {
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(9));
    }

    public void restart() {
        stop(0);
        start(100);
    }

    public void saveRecord(boolean z) {
        this.saveRecord = z;
    }

    public void setDebugPath(String str) {
        this.mDebugPath = str;
        this.saveVoiceUtil.setSaveDir(this.mDebugPath);
    }

    public void setListener(WVPListener wVPListener) {
        this.mOutListener = wVPListener;
        this.mVoicePrintManager.setListener(wVPListener);
    }

    public void start(int i) {
        this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(2), (long) i);
    }

    public void startEnrollment(int i) {
        Message obtainMessage = this.mWorkHandler.obtainMessage(8);
        obtainMessage.arg1 = i;
        this.mWorkHandler.sendMessage(obtainMessage);
    }

    public void stop(long j) {
        if (this.isPrintLog) {
            StringBuilder sb = new StringBuilder();
            sb.append("call stop, stopRecordMs:");
            sb.append(j);
            Log.d(TAG, sb.toString());
        }
        this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(6), j);
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(4));
    }

    public String version() {
        if (!this.init) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("wvp_sdk_202006011636 ");
        sb.append(this.mWakeupEngineInterface.wakeupVersion());
        sb.append(" ");
        sb.append(this.mVoicePrintManager.version());
        return sb.toString();
    }
}

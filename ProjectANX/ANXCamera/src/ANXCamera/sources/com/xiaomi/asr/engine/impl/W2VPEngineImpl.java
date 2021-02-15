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
import com.xiaomi.asr.engine.PhraseWakeupResult.PhraseWakeupResultDebugInfo;
import com.xiaomi.asr.engine.WVPListener;
import com.xiaomi.asr.engine.jni.WakeupEngineInterface;
import com.xiaomi.asr.engine.record.AudioSource;
import com.xiaomi.asr.engine.record.DSPVoiceRecord;
import com.xiaomi.asr.engine.record.DSPVoiceRecord.RecordListener;
import com.xiaomi.asr.engine.utils.DSPWakeupInfoParser;
import com.xiaomi.asr.engine.utils.SaveVoiceUtil;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Calendar;

public class W2VPEngineImpl {
    private static final int FEED_RESULT_DETECTED_END = 2;
    private static final int RECORD_BUFFER_SIZE = 320;
    private static final String SDK_VERSION = "w2vp_sdk_202006011636";
    private static final String TAG = "W2VPEngineImpl";
    private static W2VPEngineImpl mInstance;
    /* access modifiers changed from: private */
    public boolean init = false;
    /* access modifiers changed from: private */
    public boolean isPrintLog;
    /* access modifiers changed from: private */
    public boolean isWakeup;
    /* access modifiers changed from: private */
    public boolean isWakeupInfoParser;
    /* access modifiers changed from: private */
    public AudioSource mAudioSource = null;
    /* access modifiers changed from: private */
    public int mBspDataHandleTime;
    /* access modifiers changed from: private */
    public ByteArrayOutputStream mCacheCMVNData;
    /* access modifiers changed from: private */
    public ByteArrayOutputStream mCacheSVData;
    /* access modifiers changed from: private */
    public int mCaptureSession;
    /* access modifiers changed from: private */
    public DSPWakeupInfoParser mDSPWakeupInfoParser;
    /* access modifiers changed from: private */
    public String mDebugPath;
    /* access modifiers changed from: private */
    public WVPListener mOutListener;
    /* access modifiers changed from: private */
    public ByteArrayOutputStream mOutputStream;
    /* access modifiers changed from: private */
    public int mReturnIndex = 0;
    /* access modifiers changed from: private */
    public String mSoundVerifyStr;
    /* access modifiers changed from: private */
    public VoicePrintManager mVoicePrintManager;
    /* access modifiers changed from: private */
    public DSPVoiceRecord mVoiceRecord;
    /* access modifiers changed from: private */
    public WakeupEngineInterface mWakeupEngineInterface;
    /* access modifiers changed from: private */
    public long mWakeupHandleTime;
    /* access modifiers changed from: private */
    public Handler mWorkHandler;
    private HandlerThread mWorkHandlerThread;
    /* access modifiers changed from: private */
    public boolean openVoicePrint;
    /* access modifiers changed from: private */
    public boolean running = false;
    /* access modifiers changed from: private */
    public boolean saveRecord;
    /* access modifiers changed from: private */
    public SaveVoiceUtil saveVoiceUtil;

    class HandlerCallback implements Callback {
        HandlerCallback() {
        }

        public boolean handleMessage(Message message) {
            int i = message.what;
            String str = W2VPEngineImpl.TAG;
            boolean z = true;
            if (i == 9) {
                String str2 = "sv-wakeup, res:";
                if (message.arg1 != 1) {
                    W2VPEngineImpl.this.mSoundVerifyStr = (String) message.obj;
                    StringBuilder sb = new StringBuilder();
                    sb.append("声纹结果返回 sv-wakeup, res:");
                    sb.append(W2VPEngineImpl.this.mSoundVerifyStr);
                    Log.d(str, sb.toString());
                    if (W2VPEngineImpl.this.openVoicePrint && W2VPEngineImpl.this.mOutListener != null) {
                        WVPListener access$300 = W2VPEngineImpl.this.mOutListener;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(W2VPEngineImpl.this.mSoundVerifyStr);
                        sb2.append("\n");
                        access$300.onDebug(sb2.toString());
                    }
                } else if (!TextUtils.isEmpty((String) message.obj)) {
                    W2VPEngineImpl.this.isWakeup = true;
                }
                W2VPEngineImpl w2VPEngineImpl = W2VPEngineImpl.this;
                w2VPEngineImpl.mReturnIndex = w2VPEngineImpl.mReturnIndex + 1;
                if (W2VPEngineImpl.this.mReturnIndex >= 2) {
                    if (W2VPEngineImpl.this.isPrintLog) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str2);
                        sb3.append(W2VPEngineImpl.this.mSoundVerifyStr);
                        Log.d(str, sb3.toString());
                    }
                    PhraseWakeupResult phraseWakeupResult = new PhraseWakeupResult();
                    phraseWakeupResult.setVoconWakeupPassed(W2VPEngineImpl.this.isWakeup);
                    phraseWakeupResult.setAec(W2VPEngineImpl.this.mDSPWakeupInfoParser.isWakeupAec());
                    phraseWakeupResult.setWakeupStartTime(W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupStartTime());
                    phraseWakeupResult.setWakeupEndTime(W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime());
                    phraseWakeupResult.setScore((float) W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupScore());
                    phraseWakeupResult.setVoconPhrase("小爱同学");
                    phraseWakeupResult.setVBPassed(!W2VPEngineImpl.this.mSoundVerifyStr.contains("error:"));
                    PhraseWakeupResultDebugInfo phraseWakeupResultDebugInfo = new PhraseWakeupResultDebugInfo();
                    phraseWakeupResultDebugInfo.setCacheCMVNData(W2VPEngineImpl.this.mCacheCMVNData.toByteArray());
                    phraseWakeupResultDebugInfo.setCacheSVData(W2VPEngineImpl.this.mCacheSVData.toByteArray());
                    phraseWakeupResult.setDebugInfo(phraseWakeupResultDebugInfo);
                    W2VPEngineImpl.this.mOutListener.onPhraseSpotted(phraseWakeupResult);
                    if (W2VPEngineImpl.this.mOutListener != null) {
                        W2VPEngineImpl.this.mOutListener.onPhraseSpotted(phraseWakeupResult);
                    }
                }
            } else if (i != 8192) {
                if (i != 16) {
                    if (i != 17) {
                        switch (i) {
                            case 1:
                                if (W2VPEngineImpl.this.isPrintLog) {
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("on init init:");
                                    sb4.append(W2VPEngineImpl.this.init);
                                    Log.d(str, sb4.toString());
                                }
                                if (!W2VPEngineImpl.this.init) {
                                    Bundle data = message.getData();
                                    String string = data.getString(ComposerHelper.CONFIG_PATH);
                                    String string2 = data.getString("name");
                                    if (W2VPEngineImpl.this.isPrintLog) {
                                        StringBuilder sb5 = new StringBuilder();
                                        sb5.append("vp_model_path:");
                                        sb5.append(string);
                                        sb5.append(", vp_speaker:");
                                        sb5.append(string2);
                                        Log.d(str, sb5.toString());
                                    }
                                    if (W2VPEngineImpl.this.mWakeupEngineInterface.wakeupInit("") + W2VPEngineImpl.this.mVoicePrintManager.init(string, string2) == 0) {
                                        W2VPEngineImpl.this.init = true;
                                    }
                                    if (!W2VPEngineImpl.this.init) {
                                        Log.e(str, "init failed");
                                    }
                                }
                                if (W2VPEngineImpl.this.mOutListener != null) {
                                    W2VPEngineImpl.this.mOutListener.onInit(W2VPEngineImpl.this.init);
                                    break;
                                }
                                break;
                            case 2:
                                if (W2VPEngineImpl.this.init) {
                                    W2VPEngineImpl.this.mVoicePrintManager.startEnrollment(message.arg1);
                                    break;
                                }
                                break;
                            case 3:
                                if (W2VPEngineImpl.this.init) {
                                    if (!W2VPEngineImpl.this.running) {
                                        W2VPEngineImpl.this.mVoiceRecord.startRecord(W2VPEngineImpl.this.mCaptureSession);
                                        W2VPEngineImpl.this.running = true;
                                    }
                                    W2VPEngineImpl.this.isWakeupInfoParser = false;
                                    break;
                                }
                                break;
                            case 4:
                                if (W2VPEngineImpl.this.init) {
                                    if (W2VPEngineImpl.this.isPrintLog) {
                                        Log.d(str, "wakeup feed handle");
                                    }
                                    byte[] byteArray = message.getData().getByteArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
                                    W2VPEngineImpl w2VPEngineImpl2 = W2VPEngineImpl.this;
                                    int i2 = message.arg1;
                                    if (message.arg2 != 1) {
                                        z = false;
                                    }
                                    w2VPEngineImpl2.checkWakeup(byteArray, i2, z);
                                    break;
                                }
                                break;
                            case 5:
                                if (W2VPEngineImpl.this.init && W2VPEngineImpl.this.running) {
                                    W2VPEngineImpl.this.mVoiceRecord.stopRecord();
                                    W2VPEngineImpl.this.running = false;
                                    break;
                                }
                            case 6:
                                if (W2VPEngineImpl.this.init) {
                                    W2VPEngineImpl.this.mVoicePrintManager.removeAllRegister();
                                    break;
                                }
                                break;
                            case 7:
                                if (W2VPEngineImpl.this.isPrintLog) {
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("on release init:");
                                    sb6.append(W2VPEngineImpl.this.init);
                                    Log.d(str, sb6.toString());
                                }
                                if (W2VPEngineImpl.this.init) {
                                    if (W2VPEngineImpl.this.running) {
                                        W2VPEngineImpl.this.mVoiceRecord.stopRecord();
                                        W2VPEngineImpl.this.running = false;
                                    }
                                    W2VPEngineImpl.this.mWakeupEngineInterface.wakeupDestroy();
                                    W2VPEngineImpl.this.mVoicePrintManager.release();
                                    W2VPEngineImpl.this.init = false;
                                    if (W2VPEngineImpl.this.mOutListener != null) {
                                        W2VPEngineImpl.this.mOutListener.onRelease();
                                        break;
                                    }
                                }
                                break;
                        }
                    } else if (W2VPEngineImpl.this.init && W2VPEngineImpl.this.mOutListener != null) {
                        W2VPEngineImpl.this.mOutListener.onAudioData(W2VPEngineImpl.this.mOutputStream.toByteArray());
                    }
                } else if (W2VPEngineImpl.this.init) {
                    W2VPEngineImpl.this.mVoicePrintManager.generateModel();
                }
            } else if (W2VPEngineImpl.this.init) {
                W2VPEngineImpl.this.mVoicePrintManager.startEnrollmentFileRecorded(W2VPEngineImpl.this.mAudioSource, message.arg1);
            }
            return false;
        }
    }

    class VoiceRecordListener implements RecordListener {
        private final long STATIC_WAKEUP_BUFFER = 300;

        VoiceRecordListener() {
        }

        public void onAudioSessionId(int i) {
        }

        public void onRecordCreateError() {
            if (W2VPEngineImpl.this.mOutListener != null) {
                W2VPEngineImpl.this.mOutListener.onConflictAudio();
            }
        }

        public void onRecordRelease() {
            if (W2VPEngineImpl.this.mOutListener != null) {
                W2VPEngineImpl.this.mOutListener.onStopAudio();
            }
        }

        public void onRecording(byte[] bArr, int i) {
            byte[] bArr2 = new byte[i];
            System.arraycopy(bArr, 0, bArr2, 0, i);
            if (!W2VPEngineImpl.this.isWakeupInfoParser) {
                W2VPEngineImpl.this.parserWakeupInfo(bArr2, i);
                W2VPEngineImpl.this.mBspDataHandleTime = 0;
                W2VPEngineImpl.this.mReturnIndex = 0;
                W2VPEngineImpl.this.isWakeup = false;
                W2VPEngineImpl.this.mSoundVerifyStr = "";
                W2VPEngineImpl.this.mWakeupHandleTime = 0;
                W2VPEngineImpl.this.mCacheSVData.reset();
                W2VPEngineImpl.this.mCacheCMVNData.reset();
                W2VPEngineImpl.this.mOutputStream.reset();
                if (W2VPEngineImpl.this.isWakeupInfoParser && W2VPEngineImpl.this.isPrintLog) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("wakeup start time:");
                    sb.append(W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupStartTime());
                    sb.append(", wakeup end time:");
                    sb.append(W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime());
                    sb.append(", wakeup score:");
                    sb.append(W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupScore());
                    sb.append(", wakeup pcm length:");
                    sb.append(W2VPEngineImpl.this.mDSPWakeupInfoParser.getPcmLength());
                    sb.append(", wakeup aec:");
                    sb.append(W2VPEngineImpl.this.mDSPWakeupInfoParser.isWakeupAec());
                    sb.append("\n");
                    String sb2 = sb.toString();
                    if (W2VPEngineImpl.this.isPrintLog && W2VPEngineImpl.this.mOutListener != null) {
                        W2VPEngineImpl.this.mOutListener.onDebug(sb2);
                    }
                }
            } else {
                W2VPEngineImpl w2VPEngineImpl = W2VPEngineImpl.this;
                w2VPEngineImpl.mBspDataHandleTime = w2VPEngineImpl.mBspDataHandleTime + 10;
                if (((long) W2VPEngineImpl.this.mBspDataHandleTime) > W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupStartTime() && ((long) W2VPEngineImpl.this.mBspDataHandleTime) <= W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime()) {
                    try {
                        W2VPEngineImpl.this.mCacheSVData.write(Arrays.copyOfRange(bArr2, 160, 320));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (((long) W2VPEngineImpl.this.mBspDataHandleTime) == W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime()) {
                        if (W2VPEngineImpl.this.isPrintLog && W2VPEngineImpl.this.mOutListener != null) {
                            W2VPEngineImpl.this.mOutListener.onDebug("read wakeup level one end time.\n");
                        }
                        W2VPEngineImpl.this.voicePrintReco();
                    }
                }
                if (((long) W2VPEngineImpl.this.mBspDataHandleTime) <= W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime() + 300) {
                    Message obtainMessage = W2VPEngineImpl.this.mWorkHandler.obtainMessage(4);
                    Bundle bundle = new Bundle();
                    bundle.putByteArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, Arrays.copyOfRange(bArr2, 0, 160));
                    obtainMessage.setData(bundle);
                    obtainMessage.arg1 = 160;
                    obtainMessage.arg2 = 0;
                    if (((long) W2VPEngineImpl.this.mBspDataHandleTime) == W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime() + 300) {
                        obtainMessage.arg2 = 1;
                        if (W2VPEngineImpl.this.isPrintLog && W2VPEngineImpl.this.mOutListener != null) {
                            W2VPEngineImpl.this.mOutListener.onDebug("read wakeup level one end time + 300ms.\n");
                        }
                    }
                    W2VPEngineImpl.this.mWorkHandler.sendMessage(obtainMessage);
                } else if (((long) W2VPEngineImpl.this.mBspDataHandleTime) <= W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime() + 300 + ((long) W2VPEngineImpl.this.mDSPWakeupInfoParser.getPcmLength())) {
                    try {
                        W2VPEngineImpl.this.mOutputStream.write(bArr);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    if (((long) W2VPEngineImpl.this.mBspDataHandleTime) == W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime() + 300 + ((long) W2VPEngineImpl.this.mDSPWakeupInfoParser.getPcmLength())) {
                        if (W2VPEngineImpl.this.isPrintLog && W2VPEngineImpl.this.mOutListener != null) {
                            W2VPEngineImpl.this.mOutListener.onDebug("read pcm data end.\n");
                        }
                        W2VPEngineImpl.this.mWorkHandler.sendMessage(W2VPEngineImpl.this.mWorkHandler.obtainMessage(17));
                        W2VPEngineImpl.this.mVoiceRecord.stopRecord();
                        W2VPEngineImpl.this.running = false;
                    }
                }
            }
        }

        public void onRecordingEnd() {
        }

        public void onRecordingFailed() {
            if (W2VPEngineImpl.this.mOutListener != null) {
                W2VPEngineImpl.this.mOutListener.onConflictAudio();
            }
        }

        public void onRecordingStart() {
            if (W2VPEngineImpl.this.mOutListener != null) {
                W2VPEngineImpl.this.mOutListener.onStartAudio();
            }
            if (W2VPEngineImpl.this.saveRecord) {
                W2VPEngineImpl.this.saveVoiceUtil.createFile("w2vp");
            }
        }
    }

    private W2VPEngineImpl() {
        String str = "";
        this.mSoundVerifyStr = str;
        this.isWakeup = false;
        this.isPrintLog = false;
        this.saveRecord = false;
        this.isWakeupInfoParser = false;
        this.openVoicePrint = false;
        this.mBspDataHandleTime = 0;
        this.mDebugPath = str;
        this.mWakeupHandleTime = 0;
        WakeupEngineInterface.loadLibrary("wakeup-engine-level2");
        this.mWakeupEngineInterface = new WakeupEngineInterface();
        this.mDSPWakeupInfoParser = new DSPWakeupInfoParser();
        this.mCacheSVData = new ByteArrayOutputStream();
        this.mCacheCMVNData = new ByteArrayOutputStream();
        this.mVoiceRecord = new DSPVoiceRecord(new VoiceRecordListener());
        this.mVoiceRecord.setParam(2, 320);
        this.mVoiceRecord.setParam(1, 1999);
        this.mWorkHandlerThread = new HandlerThread("wakeup-engine-impl-thread");
        this.mWorkHandlerThread.start();
        this.mWorkHandler = new Handler(this.mWorkHandlerThread.getLooper(), new HandlerCallback());
        this.saveVoiceUtil = new SaveVoiceUtil();
        this.mVoicePrintManager = new VoicePrintManager("voiceprint-engine-bsp");
        this.mVoicePrintManager.setRecordSource(1999);
        this.mOutputStream = new ByteArrayOutputStream();
    }

    /* access modifiers changed from: private */
    public void checkWakeup(byte[] bArr, int i, boolean z) {
        if (!this.isWakeup) {
            if (this.saveRecord) {
                this.saveVoiceUtil.writeFile(bArr);
                try {
                    this.mCacheCMVNData.write(Arrays.copyOfRange(bArr, 0, i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (z && this.saveRecord) {
                this.saveVoiceUtil.closeFile();
            }
            long currentTimeMillis = System.currentTimeMillis();
            int wakeupFeedData = this.mWakeupEngineInterface.wakeupFeedData(bArr, i, 1);
            this.mWakeupHandleTime += System.currentTimeMillis() - currentTimeMillis;
            if (this.isPrintLog) {
                StringBuilder sb = new StringBuilder();
                sb.append("wakeup feed return:");
                sb.append(wakeupFeedData);
                sb.append(", data len:");
                sb.append(i);
                sb.append(", isLastFrame:");
                sb.append(z);
                Log.d(TAG, sb.toString());
            }
            if (wakeupFeedData == 2) {
                this.isWakeup = true;
                WVPListener wVPListener = this.mOutListener;
                if (wVPListener != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("wakeup two has wakeup. wakeup handle time:");
                    sb2.append(this.mWakeupHandleTime);
                    sb2.append("\n");
                    wVPListener.onDebug(sb2.toString());
                }
                Message obtainMessage = this.mWorkHandler.obtainMessage(9);
                obtainMessage.arg1 = 1;
                obtainMessage.obj = "keyword detected";
                this.mWorkHandler.sendMessageAtFrontOfQueue(obtainMessage);
            }
            if (z && !this.isWakeup) {
                PhraseWakeupResult phraseWakeupResult = new PhraseWakeupResult();
                phraseWakeupResult.setVoconWakeupPassed(false);
                phraseWakeupResult.setAec(this.mDSPWakeupInfoParser.isWakeupAec());
                phraseWakeupResult.setWakeupStartTime(this.mDSPWakeupInfoParser.getWakeupStartTime());
                phraseWakeupResult.setWakeupEndTime(this.mDSPWakeupInfoParser.getWakeupEndTime());
                phraseWakeupResult.setScore((float) this.mDSPWakeupInfoParser.getWakeupScore());
                phraseWakeupResult.setVoconPhrase("小爱同学");
                phraseWakeupResult.setVBPassed(!this.mSoundVerifyStr.contains("error:"));
                PhraseWakeupResultDebugInfo phraseWakeupResultDebugInfo = new PhraseWakeupResultDebugInfo();
                phraseWakeupResultDebugInfo.setCacheCMVNData(this.mCacheCMVNData.toByteArray());
                phraseWakeupResultDebugInfo.setCacheSVData(this.mCacheSVData.toByteArray());
                phraseWakeupResult.setDebugInfo(phraseWakeupResultDebugInfo);
                WVPListener wVPListener2 = this.mOutListener;
                if (wVPListener2 != null) {
                    wVPListener2.onPhraseSpotted(phraseWakeupResult);
                }
                this.mWakeupEngineInterface.wakeupReset();
            }
        }
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

    public static W2VPEngineImpl getInstance() {
        if (mInstance == null) {
            synchronized (W2VPEngineImpl.class) {
                if (mInstance == null) {
                    mInstance = new W2VPEngineImpl();
                }
            }
        }
        return mInstance;
    }

    /* access modifiers changed from: private */
    public void parserWakeupInfo(byte[] bArr, int i) {
        short[] sArr = new short[(i / 2)];
        ByteBuffer.wrap(bArr).order(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN).asShortBuffer().get(sArr);
        this.mDSPWakeupInfoParser.parse(sArr);
        this.isWakeupInfoParser = this.mDSPWakeupInfoParser.isInfoValid();
        boolean isInfoValid = this.mDSPWakeupInfoParser.isInfoValid();
        String str = TAG;
        if (isInfoValid) {
            StringBuilder sb = new StringBuilder();
            sb.append("wakeup start time:");
            sb.append(this.mDSPWakeupInfoParser.getWakeupStartTime());
            sb.append(", wakeup end time:");
            sb.append(this.mDSPWakeupInfoParser.getWakeupEndTime());
            sb.append(", wakeup score:");
            sb.append(this.mDSPWakeupInfoParser.getWakeupScore());
            sb.append(", pcm length:");
            sb.append(this.mDSPWakeupInfoParser.getPcmLength());
            sb.append(", wakeup aec:");
            sb.append(this.mDSPWakeupInfoParser.isWakeupAec());
            Log.d(str, sb.toString());
            return;
        }
        Log.e(str, "dsp parser fail.");
    }

    /* access modifiers changed from: private */
    public void voicePrintReco() {
        final byte[] byteArray = this.mCacheSVData.toByteArray();
        if (this.isPrintLog) {
            StringBuilder sb = new StringBuilder();
            sb.append("sv data len:");
            sb.append(byteArray.length);
            Log.d(TAG, sb.toString());
        }
        new Thread(new Runnable() {
            public void run() {
                String str;
                if (W2VPEngineImpl.this.openVoicePrint) {
                    long currentTimeMillis = System.currentTimeMillis();
                    str = W2VPEngineImpl.this.mVoicePrintManager.recognizeVoice(byteArray, W2VPEngineImpl.this.mDSPWakeupInfoParser.isWakeupAec() ? 0 : -1, 1);
                    if (W2VPEngineImpl.this.mOutListener != null) {
                        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                        float wakeupEndTime = (float) (W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupEndTime() - W2VPEngineImpl.this.mDSPWakeupInfoParser.getWakeupStartTime());
                        WVPListener access$300 = W2VPEngineImpl.this.mOutListener;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(", sv handle data len:");
                        sb.append(byteArray.length);
                        sb.append(", handle time:");
                        sb.append(currentTimeMillis2);
                        sb.append(", rtf:");
                        sb.append(String.format("%.3f", new Object[]{Float.valueOf(((float) currentTimeMillis2) / wakeupEndTime)}));
                        sb.append("\n");
                        access$300.onDebug(sb.toString());
                    }
                } else {
                    str = "zhang_san:0.7813";
                }
                Message obtainMessage = W2VPEngineImpl.this.mWorkHandler.obtainMessage(9);
                obtainMessage.arg1 = 2;
                obtainMessage.obj = str;
                W2VPEngineImpl.this.mWorkHandler.sendMessageAtFrontOfQueue(obtainMessage);
                if (W2VPEngineImpl.this.saveRecord) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(W2VPEngineImpl.this.mDebugPath);
                    sb2.append("/");
                    sb2.append(W2VPEngineImpl.this.getCurrentTime());
                    sb2.append("_sv.mfcc");
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(sb2.toString());
                        fileOutputStream.write(byteArray);
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
        return this.init ? this.mVoicePrintManager.getAllRegister() : "";
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
        DSPVoiceRecord dSPVoiceRecord = this.mVoiceRecord;
        if (dSPVoiceRecord != null) {
            dSPVoiceRecord.setParam(5, z ? 1 : 0);
        }
    }

    public void openVoicePrint(boolean z) {
        this.openVoicePrint = z;
    }

    public void release() {
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(7));
    }

    public void removeAllRegister() {
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(6));
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
        this.mCaptureSession = i;
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(3));
    }

    public void startEnrollment(int i) {
        Message obtainMessage = this.mWorkHandler.obtainMessage(2);
        obtainMessage.arg1 = i;
        this.mWorkHandler.sendMessage(obtainMessage);
    }

    public void startEnrollmentStreamingFileRecorder(AudioSource audioSource, int i) {
        Log.i(TAG, "preStartAudio");
        this.mAudioSource = audioSource;
        Message obtainMessage = this.mWorkHandler.obtainMessage(8192);
        obtainMessage.arg1 = i;
        this.mWorkHandler.sendMessage(obtainMessage);
    }

    public void stop() {
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(5));
    }

    public String version() {
        if (!this.init) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("w2vp_sdk_202006011636 ");
        sb.append(this.mWakeupEngineInterface.wakeupVersion());
        sb.append(" ");
        sb.append(this.mVoicePrintManager.version());
        return sb.toString();
    }
}

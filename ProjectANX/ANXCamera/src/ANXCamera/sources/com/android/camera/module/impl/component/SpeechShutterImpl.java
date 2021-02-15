package com.android.camera.module.impl.component;

import android.content.Intent;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseProtocol;
import com.android.camera.protocol.ModeProtocol.SpeechShutterDetect;
import com.xiaomi.asr.engine.MultiWakeupEngine;
import com.xiaomi.asr.engine.PhraseWakeupResult;
import com.xiaomi.asr.engine.WVPListener;

public class SpeechShutterImpl implements SpeechShutterDetect {
    private static final boolean DEBUG = true;
    /* access modifiers changed from: private */
    public static final String TAG = "SpeechShutterImpl";
    private boolean mIsInit;

    class WVPCallback implements WVPListener {
        WVPCallback() {
        }

        public void onAbortEnrollmentComplete() {
            Log.d(SpeechShutterImpl.TAG, "onAbortEnrollmentComplete");
        }

        public void onAudioData(byte[] bArr) {
            String access$000 = SpeechShutterImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAudioData:");
            sb.append(bArr.length);
            Log.d(access$000, sb.toString());
        }

        public void onCommitEnrollmentComplete() {
            Log.d(SpeechShutterImpl.TAG, "onCommitEnrollmentComplete");
        }

        public void onConflictAudio() {
            Log.d(SpeechShutterImpl.TAG, "onConflictAudio");
        }

        public void onDebug(String str) {
            String access$000 = SpeechShutterImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onDebug:");
            sb.append(str);
            Log.d(access$000, sb.toString());
        }

        public void onEndOfSpeech() {
            Log.d(SpeechShutterImpl.TAG, "onEndOfSpeech");
        }

        public void onEnergyLevelAvailable(float f, boolean z) {
            String access$000 = SpeechShutterImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnergyLevelAvailable volume:");
            sb.append(f);
            sb.append(", hasSpeech:");
            sb.append(z);
            Log.d(access$000, sb.toString());
        }

        public void onEnrollAudioBufferAvailable(byte[] bArr, boolean z) {
            String access$000 = SpeechShutterImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollAudioBufferAvailable:");
            sb.append(bArr.length);
            sb.append(", hasSpeech:");
            sb.append(z);
            Log.d(access$000, sb.toString());
        }

        public void onEnrollmentComplete(boolean z, boolean z2, float f, int i) {
            String access$000 = SpeechShutterImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollmentComplete wakeup:");
            sb.append(z);
            sb.append(", voicePrint:");
            sb.append(z2);
            sb.append(", type:");
            sb.append(i);
            Log.d(access$000, sb.toString());
        }

        public void onGenerateModel(boolean z, String str) {
            String access$000 = SpeechShutterImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onGenerateModel success:");
            sb.append(z);
            sb.append(", modelFilePath:");
            sb.append(str);
            Log.d(access$000, sb.toString());
        }

        public void onGrammarUpdated(boolean z) {
            String access$000 = SpeechShutterImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onGrammarUpdated update:");
            sb.append(z);
            Log.d(access$000, sb.toString());
        }

        public void onInit(boolean z) {
            if (z) {
                String wakeupVersion = MultiWakeupEngine.wakeupVersion();
                String access$000 = SpeechShutterImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("version:");
                sb.append(wakeupVersion);
                Log.d(access$000, sb.toString());
            }
        }

        public void onPhraseSpotted(PhraseWakeupResult phraseWakeupResult) {
            String access$000 = SpeechShutterImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPhraseSpotted ");
            sb.append(phraseWakeupResult.toString());
            Log.d(access$000, sb.toString());
            CameraAppImpl.getAndroidContext().sendBroadcast(new Intent(CameraIntentManager.ACTION_SPEECH_SHUTTER));
        }

        public void onRelease() {
            Log.d(SpeechShutterImpl.TAG, "onRelease");
        }

        public void onStartAudio() {
            Log.d(SpeechShutterImpl.TAG, "onStartAudio");
        }

        public void onStartOfSpeech() {
            Log.d(SpeechShutterImpl.TAG, "onStartOfSpeech");
        }

        public void onStopAudio() {
            Log.d(SpeechShutterImpl.TAG, "onStopAudio");
        }
    }

    public static BaseProtocol create() {
        return new SpeechShutterImpl();
    }

    public void onDestroy() {
        if (this.mIsInit) {
            Log.d(TAG, "onDestroy");
            MultiWakeupEngine.release();
            this.mIsInit = false;
        }
    }

    public void processingSpeechShutter(boolean z) {
        String str;
        String str2;
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("processingSpeechShutter ");
        sb.append(z);
        Log.d(str3, sb.toString());
        if (z) {
            if (!this.mIsInit) {
                Log.d(TAG, "init start");
                MultiWakeupEngine.setListener(new WVPCallback());
                MultiWakeupEngine.init();
                Log.d(TAG, "init end ");
                MultiWakeupEngine.start();
                Log.d(TAG, "processingSpeechShutter start");
                this.mIsInit = true;
                return;
            }
            MultiWakeupEngine.restart();
            str2 = TAG;
            str = "processingSpeechShutter restart";
        } else if (this.mIsInit) {
            MultiWakeupEngine.stop(0);
            str2 = TAG;
            str = "processingSpeechShutter stop";
        } else {
            return;
        }
        Log.d(str2, str);
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(255, this);
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(255, this);
        onDestroy();
    }
}

package com.xiaomi.asr.engine;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum ReEnrollmentAction {
    ReEnrollment;
    
    private static final String ENROLL_AUDIO_PREFIX = "enroll_";
    private static final String ENROLL_AUDIO_SUFFIX = ".pcm";
    private static final String TAG = "ReEnrollmentAction";
    private FilenameFilter enrollAudioFilter;
    private Comparator fileNameComparator;
    /* access modifiers changed from: private */
    public String mBlobFilePath;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mCurrentProcess;
    private String mEnrollBackupFolder;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public List mReEnrollAudioFileList;
    /* access modifiers changed from: private */
    public List mReEnrollAudioFileListCache;
    /* access modifiers changed from: private */
    public String mReEnrollPhrase;
    /* access modifiers changed from: private */
    public List mReEnrollPhraseCommittedList;
    private List mReEnrollPhraseList;
    /* access modifiers changed from: private */
    public Runnable mReEnrollRunnable;
    /* access modifiers changed from: private */
    public ReEnrollmentActionListener mReEnrollmentActionListener;

    public interface ReEnrollmentActionListener {
        void onAudioQualityChecked(String str, boolean z);

        void onEngineVersionChecked(String str);

        void onFinished(boolean z, List list, String str);
    }

    class WVPCallback implements WVPListener {
        private WVPCallback() {
        }

        private void logText(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(str);
            Log.d(ReEnrollmentAction.TAG, sb.toString());
        }

        public void onAbortEnrollmentComplete() {
            Log.i(ReEnrollmentAction.TAG, "Aborted re-enroll with backup audio");
            if (ReEnrollmentAction.this.mCurrentProcess == 3) {
                ReEnrollmentAction.this.mCurrentProcess = 4;
                W2VPEngine.release();
            } else if (ReEnrollmentAction.this.mCurrentProcess == 6) {
                ReEnrollmentAction.this.reEnrollmentForOnePhraseWithCheck();
            }
        }

        public void onAudioData(byte[] bArr) {
        }

        public void onCommitEnrollmentComplete() {
            ReEnrollmentActionListener access$100;
            String access$900;
            boolean z;
            Log.i(ReEnrollmentAction.TAG, "Committed re-enroll with backup audio");
            if (ReEnrollmentAction.this.mCurrentProcess == 3) {
                access$100 = ReEnrollmentAction.this.mReEnrollmentActionListener;
                access$900 = ReEnrollmentAction.this.mReEnrollPhrase;
                z = true;
            } else {
                if (ReEnrollmentAction.this.mCurrentProcess == 6) {
                    access$100 = ReEnrollmentAction.this.mReEnrollmentActionListener;
                    access$900 = ReEnrollmentAction.this.mReEnrollPhrase;
                    z = false;
                }
                ReEnrollmentAction.this.mReEnrollPhraseCommittedList.add(ReEnrollmentAction.this.mReEnrollPhrase);
                ReEnrollmentAction.this.reEnrollmentForOnePhraseWithCheck();
            }
            access$100.onAudioQualityChecked(access$900, z);
            ReEnrollmentAction.this.mReEnrollPhraseCommittedList.add(ReEnrollmentAction.this.mReEnrollPhrase);
            ReEnrollmentAction.this.reEnrollmentForOnePhraseWithCheck();
        }

        public void onConflictAudio() {
        }

        public void onDebug(String str) {
        }

        public void onEndOfSpeech() {
        }

        public void onEnergyLevelAvailable(float f, boolean z) {
        }

        public void onEnrollAudioBufferAvailable(byte[] bArr, boolean z) {
            StringBuilder sb = new StringBuilder();
            sb.append("onEnrollAudioBufferAvailable: ");
            sb.append(bArr.length);
            sb.append(" var: ");
            sb.append(z);
            logText(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(ReEnrollmentAction.this.mContext.getExternalFilesDir(null));
            sb2.append(File.separator);
            sb2.append(System.currentTimeMillis());
            sb2.append("_enroll.pcm");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(sb2.toString());
                fileOutputStream.write(bArr);
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onEnrollmentComplete(boolean z, boolean z2, float f, int i) {
            Log.i(ReEnrollmentAction.TAG, "Finshed re-enroll with backup audio");
            if (!z) {
                W2VPEngine.abortEnrollment();
            } else if (ReEnrollmentAction.this.mReEnrollAudioFileList.size() > 0) {
                ReEnrollmentAction.this.mReEnrollRunnable.run();
            } else {
                W2VPEngine.commitEnrollment();
            }
        }

        public void onGenerateModel(boolean z, String str) {
            StringBuilder sb = new StringBuilder();
            sb.append("onGenerateModel, success:");
            sb.append(z);
            sb.append(", modelFilePath:");
            sb.append(str);
            logText(sb.toString());
            ReEnrollmentAction.this.notifyFinished(z, str);
        }

        public void onGrammarUpdated(boolean z) {
            logText("onGrammarUpdated");
            if (z) {
                ReEnrollmentAction.this.mReEnrollAudioFileList.clear();
                ReEnrollmentAction.this.mReEnrollAudioFileList.addAll(ReEnrollmentAction.this.mReEnrollAudioFileListCache);
                ReEnrollmentAction.this.mReEnrollRunnable.run();
                return;
            }
            ReEnrollmentAction.this.notifyFinished(false, null);
        }

        public void onInit(boolean z) {
            ReEnrollmentAction reEnrollmentAction;
            int i;
            if (z) {
                String version = W2VPEngine.version();
                if (!TextUtils.isEmpty(version)) {
                    String[] split = version.split(" ");
                    StringBuilder sb = new StringBuilder();
                    sb.append("onInit:sdk版本号:");
                    sb.append(split[0]);
                    String str = "\n";
                    sb.append(str);
                    logText(sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("唤醒引擎版本号:");
                    sb2.append(split[1]);
                    sb2.append(str);
                    logText(sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("声纹引擎版本号:");
                    sb3.append(split[2]);
                    sb3.append(str);
                    logText(sb3.toString());
                }
                ReEnrollmentAction.this.mReEnrollmentActionListener.onEngineVersionChecked(String.valueOf(version));
                if (ReEnrollmentAction.this.mCurrentProcess == 2) {
                    reEnrollmentAction = ReEnrollmentAction.this;
                    i = 3;
                } else {
                    if (ReEnrollmentAction.this.mCurrentProcess == 5) {
                        reEnrollmentAction = ReEnrollmentAction.this;
                        i = 6;
                    }
                    onGrammarUpdated(true);
                    return;
                }
                reEnrollmentAction.mCurrentProcess = i;
                onGrammarUpdated(true);
                return;
            }
            ReEnrollmentAction.this.notifyFinished(false, null);
        }

        public void onPhraseSpotted(PhraseWakeupResult phraseWakeupResult) {
        }

        public void onRelease() {
            ReEnrollmentAction reEnrollmentAction;
            int i;
            logText("onRelease");
            StringBuilder sb = new StringBuilder();
            sb.append("this.mCurrentProcess =  ");
            sb.append(ReEnrollmentAction.this.mCurrentProcess);
            Log.d(ReEnrollmentAction.TAG, sb.toString());
            String str = "reEnroll";
            if (ReEnrollmentAction.this.mCurrentProcess == 1) {
                reEnrollmentAction = ReEnrollmentAction.this;
                i = 2;
            } else if (ReEnrollmentAction.this.mCurrentProcess == 4) {
                reEnrollmentAction = ReEnrollmentAction.this;
                i = 5;
            } else {
                return;
            }
            reEnrollmentAction.mCurrentProcess = i;
            W2VPEngine.init(ReEnrollmentAction.this.mBlobFilePath, str);
        }

        public void onStartAudio() {
        }

        public void onStartOfSpeech() {
        }

        public void onStopAudio() {
        }
    }

    /* access modifiers changed from: private */
    public void notifyFinished(boolean z, String str) {
        this.mContext = null;
        this.mReEnrollmentActionListener.onFinished(z, this.mReEnrollPhraseCommittedList, str);
        this.mReEnrollmentActionListener = null;
        W2VPEngine.setListener(null);
    }

    /* access modifiers changed from: private */
    public void reEnrollmentForOnePhraseWithCheck() {
        if (this.mReEnrollPhraseList.size() > 0) {
            this.mReEnrollPhrase = (String) this.mReEnrollPhraseList.remove(0);
            if (reEnrollmentFromPath(this.mEnrollBackupFolder)) {
                return;
            }
        } else if (this.mReEnrollPhraseCommittedList.size() > 0) {
            W2VPEngine.generateModel();
            return;
        }
        notifyFinished(false, null);
    }

    private boolean reEnrollmentFromPath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("reEnrollmentFromPath phrase:");
        sb.append(this.mReEnrollPhrase);
        sb.append(" enrollAudioPath:");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.i(str2, sb2);
        boolean z = false;
        this.mReEnrollAudioFileListCache = scanFile(new File(str), this.enrollAudioFilter, false);
        Collections.sort(this.mReEnrollAudioFileListCache, this.fileNameComparator);
        if (this.mReEnrollAudioFileListCache.size() > 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(File.separator);
            sb3.append("wakeup_phrase.txt");
            try {
                FileInputStream fileInputStream = new FileInputStream(sb3.toString());
                if (this.mReEnrollPhrase.equalsIgnoreCase(new BufferedReader(new InputStreamReader(fileInputStream)).readLine())) {
                    try {
                        W2VPEngine.setListener(new WVPCallback());
                        this.mCurrentProcess = 1;
                        W2VPEngine.release();
                        z = true;
                    } catch (IOException e) {
                        e = e;
                        z = true;
                        Log.d(str2, e.getLocalizedMessage());
                        return z;
                    }
                }
                fileInputStream.close();
            } catch (IOException e2) {
                e = e2;
            }
        }
        return z;
    }

    private ArrayList scanFile(File file, FilenameFilter filenameFilter, boolean z) {
        File[] listFiles;
        ArrayList arrayList = new ArrayList();
        if (file.exists() && file.listFiles() != null) {
            for (File file2 : file.listFiles()) {
                if (filenameFilter.accept(file2, file2.getName())) {
                    arrayList.add(file2.getPath());
                }
                if (file2.isDirectory() && z) {
                    arrayList.addAll(scanFile(file2, filenameFilter, true));
                }
            }
        }
        return arrayList;
    }

    public void reEnrollmentWithBackupAudio(Context context, String str, String str2, String str3, ReEnrollmentActionListener reEnrollmentActionListener) {
        StringBuilder sb = new StringBuilder();
        sb.append(" phrase:");
        sb.append(str);
        sb.append(" blobFilePath:");
        sb.append(str3);
        Log.i(TAG, sb.toString());
        this.mContext = context;
        this.mReEnrollPhraseList.clear();
        this.mReEnrollPhraseCommittedList.clear();
        this.mReEnrollPhraseList.add(str);
        this.mBlobFilePath = str3;
        this.mEnrollBackupFolder = str2;
        this.mReEnrollmentActionListener = reEnrollmentActionListener;
        reEnrollmentForOnePhraseWithCheck();
    }
}

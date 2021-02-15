package com.android.camera.tts;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.VisibleForTesting;
import com.android.camera.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TTSHelper {
    /* access modifiers changed from: private */
    public static boolean DBG = Log.isLoggable(TAG, 3);
    private static final long DEFAULT_SHUTDOWN_DELAY_MILLIS = 60000;
    private static final String TAG = "TTSHelper";
    private static final char UTTERANCE_ID_SEPARATOR = ';';
    /* access modifiers changed from: private */
    public String currentBatchId;
    private final AudioManager mAudioManager;
    private final Context mContext;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private int mInitStatus;
    /* access modifiers changed from: private */
    public final Map mListeners;
    private final Runnable mMaybeShutdownRunnable;
    private final OnAudioFocusChangeListener mNoOpAFChangeListener;
    /* access modifiers changed from: private */
    public SpeechRequest mPendingRequest;
    private final UtteranceProgressListener mProgressListener;
    /* access modifiers changed from: private */
    public final long mShutdownDelayMillis;
    /* access modifiers changed from: private */
    public TTSEngine mTTSEngine;

    class BatchListener {
        private boolean mBatchStarted = false;
        private final Listener mListener;

        BatchListener(Listener listener) {
            this.mListener = listener;
        }

        private void handleBatchFinished(Pair pair, boolean z) {
            TTSHelper.this.onTtsStopped(this.mListener, z);
            TTSHelper.this.mListeners.remove(pair.first);
        }

        /* access modifiers changed from: 0000 */
        public void onDone(Pair pair) {
            if (((Integer) pair.second).intValue() == 0) {
                handleBatchFinished(pair, false);
            }
        }

        /* access modifiers changed from: 0000 */
        public void onError(Pair pair) {
            if (((String) pair.first).equals(TTSHelper.this.currentBatchId)) {
                TTSHelper.this.mTTSEngine.stop();
            }
            handleBatchFinished(pair, true);
        }

        /* access modifiers changed from: 0000 */
        public void onStart() {
            if (!this.mBatchStarted) {
                this.mBatchStarted = true;
                this.mListener.onTTSStarted();
            }
        }

        /* access modifiers changed from: 0000 */
        public void onStop(Pair pair) {
            handleBatchFinished(pair, false);
        }
    }

    public interface Listener {
        void onAudioFocusFailed();

        void onTTSStarted();

        void onTTSStopped(boolean z);
    }

    class SpeechRequest {
        final Listener mListener;
        final List mTextToSpeak;

        SpeechRequest(List list, Listener listener) {
            this.mTextToSpeak = list;
            this.mListener = listener;
        }
    }

    public TTSHelper(Context context) {
        this(context, new AndroidTTSEngine(), DEFAULT_SHUTDOWN_DELAY_MILLIS);
    }

    @VisibleForTesting
    TTSHelper(Context context, TTSEngine tTSEngine, long j) {
        this.mHandler = new Handler();
        this.mNoOpAFChangeListener = C0414O00000oo.INSTANCE;
        this.mListeners = new HashMap();
        this.mMaybeShutdownRunnable = new Runnable() {
            public void run() {
                if (TTSHelper.this.mListeners.isEmpty() || TTSHelper.this.mPendingRequest == null) {
                    TTSHelper.this.shutdownEngine();
                } else {
                    TTSHelper.this.mHandler.postDelayed(this, TTSHelper.this.mShutdownDelayMillis);
                }
            }
        };
        this.mProgressListener = new UtteranceProgressListener() {
            private void safeInvokeAsync(String str, BiConsumer biConsumer) {
                TTSHelper.this.mHandler.post(new O00000o(this, str, biConsumer));
            }

            public /* synthetic */ void O000000o(String str, BiConsumer biConsumer) {
                Pair access$600 = TTSHelper.parse(str);
                BatchListener batchListener = (BatchListener) TTSHelper.this.mListeners.get(access$600.first);
                if (batchListener != null) {
                    biConsumer.accept(batchListener, access$600);
                } else if (TTSHelper.DBG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Missing batch listener: ");
                    sb.append(str);
                    com.android.camera.log.Log.d(TTSHelper.TAG, sb.toString());
                }
            }

            public void onDone(String str) {
                if (TTSHelper.DBG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TTS onDone: ");
                    sb.append(str);
                    com.android.camera.log.Log.d(TTSHelper.TAG, sb.toString());
                }
                safeInvokeAsync(str, O00000Oo.INSTANCE);
            }

            public void onError(String str) {
                if (TTSHelper.DBG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TTS onError: ");
                    sb.append(str);
                    com.android.camera.log.Log.d(TTSHelper.TAG, sb.toString());
                }
                safeInvokeAsync(str, O0000OOo.INSTANCE);
            }

            public void onStart(String str) {
                if (TTSHelper.DBG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TTS onStart: ");
                    sb.append(str);
                    com.android.camera.log.Log.d(TTSHelper.TAG, sb.toString());
                }
                safeInvokeAsync(str, O00000o0.INSTANCE);
            }

            public void onStop(String str, boolean z) {
                if (TTSHelper.DBG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TTS onStop: ");
                    sb.append(str);
                    com.android.camera.log.Log.d(TTSHelper.TAG, sb.toString());
                }
                safeInvokeAsync(str, O000000o.INSTANCE);
            }
        };
        this.mContext = context;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mTTSEngine = tTSEngine;
        this.mShutdownDelayMillis = j;
        this.mInitStatus = -2;
    }

    static /* synthetic */ void O00000Oo(int i) {
    }

    /* access modifiers changed from: private */
    public void handleInitCompleted(int i) {
        if (DBG) {
            StringBuilder sb = new StringBuilder();
            sb.append("buildRecognizeDataHolder completed: ");
            sb.append(i);
            com.android.camera.log.Log.d(TAG, sb.toString());
        }
        this.mInitStatus = i;
        SpeechRequest speechRequest = this.mPendingRequest;
        if (speechRequest != null) {
            playInternal(speechRequest.mTextToSpeak, speechRequest.mListener, null);
            this.mPendingRequest = null;
        }
    }

    private void initMaybeAndKeepAlive() {
        if (!this.mTTSEngine.isInitialized()) {
            if (DBG) {
                com.android.camera.log.Log.d(TAG, "Initializing TTS Engine");
            }
            this.mTTSEngine.initialize(this.mContext, new C0413O00000oO(this));
            this.mTTSEngine.setOnUtteranceProgressListener(this.mProgressListener);
        }
        this.mHandler.removeCallbacks(this.mMaybeShutdownRunnable);
        this.mHandler.postDelayed(this.mMaybeShutdownRunnable, this.mShutdownDelayMillis);
    }

    /* access modifiers changed from: private */
    public void onTtsStopped(Listener listener, boolean z) {
        this.mAudioManager.abandonAudioFocus(this.mNoOpAFChangeListener);
        this.mHandler.post(new O0000O0o(listener, z));
    }

    /* access modifiers changed from: private */
    public static Pair parse(String str) {
        int indexOf = str.indexOf(59);
        return Pair.create(str.substring(0, indexOf), Integer.valueOf(Integer.parseInt(str.substring(indexOf + 1))));
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.CharSequence>, for r9v0, types: [java.util.List, java.util.List<java.lang.CharSequence>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void playInternal(List<CharSequence> list, Listener listener, Bundle bundle) {
        int i = this.mInitStatus;
        String str = TAG;
        if (i == -1) {
            com.android.camera.log.Log.e(str, "TTS setup failed!");
            onTtsStopped(listener, true);
            return;
        }
        this.mTTSEngine.stop();
        this.currentBatchId = Integer.toString(listener.hashCode());
        int size = list.size() - 1;
        for (CharSequence charSequence : list) {
            String format = String.format("%s%c%d", new Object[]{this.currentBatchId, Character.valueOf(UTTERANCE_ID_SEPARATOR), Integer.valueOf(size)});
            if (DBG) {
                com.android.camera.log.Log.d(str, String.format("Queueing tts: '%s' [%s]", new Object[]{charSequence, format}));
            }
            if (this.mTTSEngine.speak(charSequence, 1, bundle, format) != 0) {
                this.mTTSEngine.stop();
                this.currentBatchId = null;
                com.android.camera.log.Log.e(str, "Queuing text failed!");
                onTtsStopped(listener, true);
                return;
            }
            size--;
        }
        this.mListeners.put(this.currentBatchId, new BatchListener(listener));
    }

    /* access modifiers changed from: private */
    public void shutdownEngine() {
        if (this.mTTSEngine.isInitialized()) {
            if (DBG) {
                com.android.camera.log.Log.d(TAG, "Shutting down TTS Engine");
            }
            this.mTTSEngine.stop();
            this.mTTSEngine.shutdown();
            this.mInitStatus = -2;
        }
    }

    public static void speakingTextInTalkbackMode(TTSHelper tTSHelper, String str) {
        if (Util.isAccessible()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            Bundle bundle = new Bundle();
            bundle.putFloat("volume", 1.0f);
            tTSHelper.requestStop();
            tTSHelper.requestPlay(arrayList, new Listener() {
                public void onAudioFocusFailed() {
                    com.android.camera.log.Log.d(TTSHelper.TAG, "failed to require audio focus.");
                }

                public void onTTSStarted() {
                    com.android.camera.log.Log.d(TTSHelper.TAG, "onTTSStarted.");
                }

                public void onTTSStopped(boolean z) {
                    com.android.camera.log.Log.d(TTSHelper.TAG, "onTTSStopped.");
                }
            }, bundle);
        }
    }

    public void cleanup() {
        this.mHandler.removeCallbacksAndMessages(null);
        shutdownEngine();
    }

    public int getStream() {
        return this.mTTSEngine.getStream();
    }

    public boolean isSpeaking() {
        return this.mTTSEngine.isSpeaking();
    }

    public void requestPlay(List list, Listener listener, Bundle bundle) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Empty/null textToSpeak");
        } else if (this.mAudioManager.requestAudioFocus(this.mNoOpAFChangeListener, getStream(), 2) != 1) {
            listener.onAudioFocusFailed();
        } else {
            initMaybeAndKeepAlive();
            if (this.mInitStatus == -2) {
                SpeechRequest speechRequest = this.mPendingRequest;
                if (speechRequest != null) {
                    onTtsStopped(speechRequest.mListener, false);
                }
                this.mPendingRequest = new SpeechRequest(list, listener);
            } else {
                playInternal(list, listener, bundle);
            }
        }
    }

    public void requestStop() {
        this.mTTSEngine.stop();
        this.currentBatchId = null;
    }
}

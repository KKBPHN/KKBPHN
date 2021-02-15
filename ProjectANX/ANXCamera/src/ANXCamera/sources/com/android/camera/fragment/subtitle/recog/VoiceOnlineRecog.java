package com.android.camera.fragment.subtitle.recog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import com.android.camera.fragment.subtitle.FragmentSubtitle.RecognitionListener;
import com.android.camera.fragment.subtitle.recog.record.PcmRecorder;
import com.android.camera.fragment.subtitle.recog.record.PcmRecorder.PcmRecordListener;
import com.android.camera.log.Log;
import com.android.camera.resource.RequestHelper;
import com.android.camera.statistic.CameraStatUtils;
import com.google.android.apps.photos.api.PhotosOemApi;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaomi.stat.d;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class VoiceOnlineRecog {
    private static final String FINAL_RESULT_TYPE = "0";
    private static final String HOSTURL = "wss://xiaomi-ist-api.xfyun.cn/v2/ist";
    private static final int SOCKET_TIME_OUT = 20000;
    /* access modifiers changed from: private */
    public static final String TAG = "VoiceOnlineRecog";
    private final int SAMPLE_RATE = 16000;
    public final int StatusContinueFrame = 1;
    public final int StatusFirstFrame = 0;
    public final int StatusLastFrame = 2;
    private Context context;
    /* access modifiers changed from: private */
    public int currentStatus = 0;
    protected Handler handler = new Handler();
    /* access modifiers changed from: private */
    public boolean isPauseRecording;
    /* access modifiers changed from: private */
    public boolean isPcmRecorderWorking;
    /* access modifiers changed from: private */
    public boolean isStopRecording;
    public final Gson json = new Gson();
    /* access modifiers changed from: private */
    public boolean mCanStartRecord = true;
    /* access modifiers changed from: private */
    public String mEdTime;
    private long mNeedRemoveTime;
    /* access modifiers changed from: private */
    public long mPauseRecordingTime;
    private PcmRecordListener mPcmRecordListener = new PcmRecordListener() {
        public void onError(int i) {
            VoiceOnlineRecog.this.mCanStartRecord = false;
        }

        public void onRecordBuffer(byte[] bArr, int i, int i2, int i3) {
            if (bArr != null) {
                try {
                    JsonObject jsonObject = new JsonObject();
                    if (VoiceOnlineRecog.this.currentStatus == 0) {
                        JsonObject jsonObject2 = new JsonObject();
                        JsonObject jsonObject3 = new JsonObject();
                        String subtitleAccessAppID = RequestHelper.getSubtitleAccessAppID();
                        String access$100 = VoiceOnlineRecog.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onRecordBuffer accessAppID: ");
                        sb.append(subtitleAccessAppID);
                        Log.d(access$100, sb.toString());
                        jsonObject3.addProperty("app_id", subtitleAccessAppID);
                        jsonObject2.addProperty("aue", "raw");
                        jsonObject2.addProperty("language", "cn_en");
                        jsonObject2.addProperty("accent", "mandarin");
                        jsonObject2.addProperty("domain", "xiaomi");
                        jsonObject2.addProperty("rf", "deserted");
                        jsonObject2.addProperty("dwa", "wpgs");
                        jsonObject2.addProperty("rate", "16000");
                        jsonObject2.addProperty("vgap", Integer.valueOf(15));
                        jsonObject.add("common", jsonObject3);
                        jsonObject.add("business", jsonObject2);
                    }
                    JsonObject jsonObject4 = new JsonObject();
                    jsonObject4.addProperty("status", Integer.valueOf(VoiceOnlineRecog.this.currentStatus));
                    jsonObject4.addProperty("audio", Base64.getEncoder().encodeToString(Arrays.copyOf(bArr, i2)));
                    jsonObject.add(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, jsonObject4);
                    VoiceOnlineRecog.this.webSocket.send(jsonObject.toString());
                    VoiceOnlineRecog.this.currentStatus = 1;
                } catch (Exception e) {
                    String access$1002 = VoiceOnlineRecog.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onRecordBuffer Exception: ");
                    sb2.append(e);
                    Log.e(access$1002, sb2.toString());
                }
            } else {
                Log.e(VoiceOnlineRecog.TAG, "onRecordBuffer data was null");
            }
        }

        public void onRecordReleased() {
            VoiceOnlineRecog.this.mCanStartRecord = true;
            Log.d(VoiceOnlineRecog.TAG, "onRecordReleased ");
            JsonObject jsonObject = new JsonObject();
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("status", Integer.valueOf(2));
            jsonObject2.addProperty("encoding", "raw");
            jsonObject.add(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, jsonObject2);
            VoiceOnlineRecog.this.webSocket.send(jsonObject.toString());
            VoiceOnlineRecog.this.currentStatus = 0;
        }

        public void onRecordStarted(boolean z) {
            if (z) {
                VoiceOnlineRecog.this.mCanStartRecord = false;
            }
        }
    };
    private PcmRecorder mPcmRecorder;
    /* access modifiers changed from: private */
    public String mRecordStopSid;
    private CompletableEmitter mResultEmitter;
    /* access modifiers changed from: private */
    public long mResumeRecordingTime;
    /* access modifiers changed from: private */
    public String mSid;
    /* access modifiers changed from: private */
    public String mStTime;
    /* access modifiers changed from: private */
    public long mStartRecordingTime;
    /* access modifiers changed from: private */
    public String mType = "";
    /* access modifiers changed from: private */
    public RecognitionListener recognitionListener;
    /* access modifiers changed from: private */
    public StringBuilder srtBuilder = new StringBuilder();
    /* access modifiers changed from: private */
    public int srtRowNum = 1;
    /* access modifiers changed from: private */
    public WebSocket webSocket;

    class Listener extends WebSocketListener {
        Listener() {
        }

        public void onClosed(WebSocket webSocket, int i, String str) {
            super.onClosed(webSocket, i, str);
            String access$100 = VoiceOnlineRecog.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClosed: ");
            sb.append(str);
            Log.d(access$100, sb.toString());
        }

        public void onClosing(WebSocket webSocket, int i, String str) {
            super.onClosing(webSocket, i, str);
            String access$100 = VoiceOnlineRecog.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClosing: ");
            sb.append(str);
            Log.d(access$100, sb.toString());
        }

        public void onFailure(WebSocket webSocket, Throwable th, Response response) {
            super.onFailure(webSocket, th, response);
            String access$100 = VoiceOnlineRecog.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onFailure: throwable ");
            sb.append(th);
            Log.d(access$100, sb.toString());
            if (VoiceOnlineRecog.this.isPcmRecorderWorking) {
                VoiceOnlineRecog.this.stopPcmRecorder();
            }
            if (!VoiceOnlineRecog.this.isStopRecording) {
                VoiceOnlineRecog.this.recognitionListener.onFailure();
                if (response != null) {
                    try {
                        String access$1002 = VoiceOnlineRecog.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(response.code());
                        sb2.append("");
                        Log.d(access$1002, sb2.toString());
                        Log.d(VoiceOnlineRecog.TAG, response.body().string());
                    } catch (IOException e) {
                        String access$1003 = VoiceOnlineRecog.TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("IOException: ");
                        sb3.append(e);
                        Log.e(access$1003, sb3.toString());
                    }
                }
            }
        }

        public void onMessage(WebSocket webSocket, String str) {
            String access$100;
            String str2;
            super.onMessage(webSocket, str);
            String access$800 = VoiceOnlineRecog.this.getContent(str);
            if (TextUtils.isEmpty(access$800)) {
                access$100 = VoiceOnlineRecog.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("data is empty , original message is: ");
                sb.append(str);
                str2 = sb.toString();
            } else {
                String str3 = "0";
                if (str3.equals(VoiceOnlineRecog.this.mType)) {
                    long access$1000 = VoiceOnlineRecog.this.mPauseRecordingTime - VoiceOnlineRecog.this.mStartRecordingTime;
                    long access$1200 = VoiceOnlineRecog.this.mResumeRecordingTime - VoiceOnlineRecog.this.mStartRecordingTime;
                    Long valueOf = Long.valueOf(VoiceOnlineRecog.this.mEdTime);
                    Long valueOf2 = Long.valueOf(VoiceOnlineRecog.this.mStTime);
                    if ((VoiceOnlineRecog.this.isPauseRecording && valueOf2.longValue() > access$1000) || valueOf.longValue() < access$1200) {
                        access$100 = VoiceOnlineRecog.TAG;
                        str2 = "Subtitles in pause: ";
                    }
                }
                String trim = access$800.trim();
                String str4 = "";
                String replaceAll = trim.replaceAll("[^a-z^A-Z^0-9]", str4);
                int i = 30;
                if (replaceAll.length() != 0) {
                    i = (trim.length() - replaceAll.length()) + trim.split("\\s+").length == 30 ? trim.length() : 53;
                }
                if (trim.length() > i) {
                    trim = trim.substring(0, i);
                }
                String replace = trim.replace("。", str4);
                String str5 = ".";
                if (replace.endsWith(str5)) {
                    replace = replace.substring(0, replace.length() - 1);
                }
                if (replace.startsWith(str5)) {
                    replace = replace.substring(1);
                }
                if (VoiceOnlineRecog.this.mSid != null && !VoiceOnlineRecog.this.mSid.equals(VoiceOnlineRecog.this.mRecordStopSid)) {
                    VoiceOnlineRecog.this.showSubtitleContent(replace);
                }
                if (str3.equals(VoiceOnlineRecog.this.mType)) {
                    CameraStatUtils.trackTriggerSubtitle();
                    VoiceOnlineRecog voiceOnlineRecog = VoiceOnlineRecog.this;
                    String access$1900 = voiceOnlineRecog.getTime(voiceOnlineRecog.mStTime);
                    VoiceOnlineRecog voiceOnlineRecog2 = VoiceOnlineRecog.this;
                    String access$19002 = voiceOnlineRecog2.getTime(voiceOnlineRecog2.mEdTime);
                    StringBuilder access$2100 = VoiceOnlineRecog.this.srtBuilder;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(VoiceOnlineRecog.this.srtRowNum = VoiceOnlineRecog.this.srtRowNum + 1);
                    String str6 = "\n";
                    sb2.append(str6);
                    access$2100.append(sb2.toString());
                    VoiceOnlineRecog.this.srtBuilder.append(String.format("%s --> %s\n", new Object[]{access$1900, access$19002}));
                    VoiceOnlineRecog.this.srtBuilder.append(String.format("%s\n", new Object[]{replace}));
                    VoiceOnlineRecog.this.srtBuilder.append(str6);
                    if (VoiceOnlineRecog.this.isStopRecording && VoiceOnlineRecog.this.isPcmRecorderWorking) {
                        Log.d(VoiceOnlineRecog.TAG, "final message return, stop recording: ");
                        VoiceOnlineRecog.this.stopPcmRecorder();
                    }
                }
                return;
            }
            Log.e(access$100, str2);
        }

        public void onMessage(WebSocket webSocket, ByteString byteString) {
            super.onMessage(webSocket, byteString);
        }

        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
        }
    }

    public VoiceOnlineRecog(Context context2, RecognitionListener recognitionListener2) {
        this.context = context2;
        this.recognitionListener = recognitionListener2;
    }

    /* access modifiers changed from: private */
    public String getContent(String str) {
        JsonObject asJsonObject = new JsonParser().parse(str).getAsJsonObject();
        String str2 = "";
        if (asJsonObject == null) {
            return str2;
        }
        String asString = asJsonObject.getAsJsonPrimitive(d.g).getAsString();
        if (!TextUtils.isEmpty(asString) && !asString.equals(this.mSid)) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("subtitle sid : ");
            sb.append(asString);
            Log.d(str3, sb.toString());
            this.mSid = asString;
        }
        JsonObject asJsonObject2 = asJsonObject.getAsJsonObject(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
        if (asJsonObject2 == null) {
            return str2;
        }
        JsonObject asJsonObject3 = asJsonObject2.getAsJsonObject("result");
        if (asJsonObject3 == null) {
            return str2;
        }
        JsonObject asJsonObject4 = asJsonObject3.getAsJsonObject("cn");
        if (asJsonObject4 == null) {
            return str2;
        }
        JsonObject asJsonObject5 = asJsonObject4.getAsJsonObject(d.n);
        if (asJsonObject5 == null) {
            return str2;
        }
        this.mType = asJsonObject5.getAsJsonPrimitive("type").getAsString();
        this.mStTime = asJsonObject5.getAsJsonPrimitive("bg").getAsString();
        this.mEdTime = asJsonObject5.getAsJsonPrimitive("ed").getAsString();
        JsonArray asJsonArray = asJsonObject5.getAsJsonArray("rt").get(0).getAsJsonObject().getAsJsonArray("ws");
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < asJsonArray.size(); i++) {
            sb2.append(asJsonArray.get(i).getAsJsonObject().getAsJsonArray("cw").get(0).getAsJsonObject().getAsJsonPrimitive("w").getAsString());
        }
        return sb2.toString();
    }

    /* access modifiers changed from: private */
    public String getTime(String str) {
        long longValue = Long.valueOf(str).longValue();
        long j = this.mNeedRemoveTime;
        if (longValue > j) {
            longValue -= j;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss,SSS");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return simpleDateFormat.format(Long.valueOf(longValue));
    }

    private void initWebSocket() {
        String subtitleAccessAppKey = RequestHelper.getSubtitleAccessAppKey();
        String subtitleAccessAppSecret = RequestHelper.getSubtitleAccessAppSecret();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initWebSocket:accessAppKey ");
        sb.append(subtitleAccessAppKey);
        sb.append(" accessAppSecret ");
        sb.append(subtitleAccessAppSecret);
        Log.d(str, sb.toString());
        this.webSocket = new Builder().connectTimeout(20000, TimeUnit.MILLISECONDS).readTimeout(20000, TimeUnit.MILLISECONDS).build().newWebSocket(new Request.Builder().url(AuthUtils.assembleRequestUrl(HOSTURL, subtitleAccessAppKey, subtitleAccessAppSecret)).build(), new Listener());
    }

    /* access modifiers changed from: private */
    public void showSubtitleContent(final String str) {
        if (!this.isPauseRecording) {
            ((Activity) this.context).runOnUiThread(new Runnable() {
                public void run() {
                    VoiceOnlineRecog.this.recognitionListener.onRecognitionListener(str);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void stopPcmRecorder() {
        Log.d(TAG, "stopPcmRecorder ");
        this.isPcmRecorderWorking = false;
        this.webSocket.cancel();
        this.mPcmRecorder.stopRecord(true);
        this.mPcmRecorder = null;
        CompletableEmitter completableEmitter = this.mResultEmitter;
        if (completableEmitter != null) {
            completableEmitter.onComplete();
        }
    }

    public /* synthetic */ void O000000o(CompletableEmitter completableEmitter) {
        this.mResultEmitter = completableEmitter;
    }

    public String getSubtitleContent() {
        String sb = this.srtBuilder.toString();
        StringBuilder sb2 = this.srtBuilder;
        sb2.delete(0, sb2.length());
        return sb;
    }

    public void getSubtitleContentAsync(final com.android.camera.protocol.ModeProtocol.SubtitleRecording.Listener listener, long j) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getSubtitleContentAsync ");
        sb.append(this.isPcmRecorderWorking);
        Log.d(str, sb.toString());
        if (this.isPcmRecorderWorking) {
            Completable.create(new O000000o(this)).timeout(j, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe((CompletableObserver) new CompletableObserver() {
                public void onComplete() {
                    Log.d(VoiceOnlineRecog.TAG, "onComplete ");
                    com.android.camera.protocol.ModeProtocol.SubtitleRecording.Listener listener = listener;
                    if (listener != null) {
                        listener.onResult(VoiceOnlineRecog.this.getSubtitleContent());
                    }
                }

                public void onError(@NonNull Throwable th) {
                    String access$100 = VoiceOnlineRecog.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onError ");
                    sb.append(th);
                    Log.d(access$100, sb.toString());
                    com.android.camera.protocol.ModeProtocol.SubtitleRecording.Listener listener = listener;
                    if (listener != null && (th instanceof TimeoutException)) {
                        listener.onTimeout();
                    }
                }

                public void onSubscribe(@NonNull Disposable disposable) {
                }
            });
        } else if (listener != null) {
            listener.onResult(getSubtitleContent());
        }
    }

    public void onDestroy() {
        this.isPauseRecording = true;
    }

    public void pauseRecording() {
        this.mPauseRecordingTime = System.currentTimeMillis();
        this.isPauseRecording = true;
    }

    public void resumeRecording() {
        this.mResumeRecordingTime = System.currentTimeMillis();
        this.mNeedRemoveTime += this.mResumeRecordingTime - this.mPauseRecordingTime;
        this.isPauseRecording = false;
    }

    public void startRecording() {
        this.mStartRecordingTime = System.currentTimeMillis();
        this.isPauseRecording = false;
        this.isStopRecording = false;
        this.mNeedRemoveTime = 0;
        if (this.srtBuilder.length() != 0) {
            StringBuilder sb = this.srtBuilder;
            sb.delete(0, sb.length());
        }
        this.srtRowNum = 1;
        if (this.mCanStartRecord) {
            try {
                if (this.isPcmRecorderWorking) {
                    stopPcmRecorder();
                }
                initWebSocket();
                this.mPcmRecorder = new PcmRecorder(16000, 40);
                this.mPcmRecorder.startRecording(this.mPcmRecordListener);
                this.isPcmRecorderWorking = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecording() {
        this.mCanStartRecord = true;
        this.isStopRecording = true;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopRecording:current subtitle type : ");
        sb.append(this.mType);
        Log.d(str, sb.toString());
        if ("0".equals(this.mType) && this.isPcmRecorderWorking) {
            stopPcmRecorder();
        }
        this.mRecordStopSid = this.mSid;
    }
}

package com.android.camera.features.mimoji2.module.impl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.Surface;
import com.android.camera.ActivityBase;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiBottomList;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiEditor2.MimojiEmoticon;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiFullScreenProtocol;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiVideoEditor;
import com.android.camera.features.mimoji2.utils.MimojiViewUtil;
import com.android.camera.features.mimoji2.widget.helper.MimojiHelper2;
import com.android.camera.features.mimoji2.widget.helper.MimojiStatusManager2;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.storage.Storage;
import com.android.camera.ui.TextureVideoView;
import com.android.camera.ui.TextureVideoView.MediaPlayerCallback;
import com.arcsoft.avatar2.emoticon.EmoInfo;
import com.xiaomi.Video2GifEditer.MediaProcess;
import com.xiaomi.Video2GifEditer.MediaProcess.Callback;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.majorkernelpanic.streaming.rtcp.SenderReport;

public class MimojiVideoEditorImpl implements MimojiVideoEditor {
    /* access modifiers changed from: private */
    public static final String TAG = "MimojiVideoEditorImpl";
    /* access modifiers changed from: private */
    public long composeTime;
    private Context mContext;
    private int mEncodeHeight = 1080;
    private int mEncodeWidth = 1920;
    private String mGifSourcePath;
    /* access modifiers changed from: private */
    public boolean mIsComposing = false;
    private boolean mIsRelease = false;
    private MimojiChangeTimbreCallback mMimojiChangeTimbreCallback;
    private MimojiMediaPlayerCallback mMimojiMediaPlayerCallback;
    private MimojiVideo2GifCallback mMimojiVideo2GifCallback;
    private int mOrientation;
    /* access modifiers changed from: private */
    public TextureVideoView mTextureVideoView;
    private String mVideoSavePath;
    private Map mVoiceMap;
    /* access modifiers changed from: private */
    public boolean mWaitingResultSurfaceTexture;

    class MimojiChangeTimbreCallback implements Callback {
        private int stopRecordType;

        private MimojiChangeTimbreCallback() {
        }

        public void OnConvertProgress(int i) {
            if (i == 100) {
                MimojiVideoEditorImpl.this.onSuccess(MimojiHelper2.VIDEO_DEAL_CACHE_FILE, this.stopRecordType);
                long currentTimeMillis = System.currentTimeMillis() - MimojiVideoEditorImpl.this.composeTime;
                String access$400 = MimojiVideoEditorImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mimoji void combineVideoAudio[savePath] time  ");
                sb.append(currentTimeMillis);
                Log.d(access$400, sb.toString());
            }
            String access$4002 = MimojiVideoEditorImpl.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("progress value: ");
            sb2.append(i);
            Log.d(access$4002, sb2.toString());
        }

        public int getStopRecordType() {
            return this.stopRecordType;
        }

        public void setStopRecordType(int i) {
            this.stopRecordType = i;
        }
    }

    class MimojiMediaPlayerCallback implements MediaPlayerCallback {
        private MimojiMediaPlayerCallback() {
        }

        public /* synthetic */ void O00oO00o() {
            MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
            if (mimojiFullScreenProtocol != null) {
                mimojiFullScreenProtocol.showPreviewCover(false);
                Log.d(MimojiVideoEditorImpl.TAG, "mimoji void onPreviewPixelsRead[pixels, width, height] bitmap mPreviewCover null");
            }
            MimojiBottomList mimojiBottomList = (MimojiBottomList) ModeCoordinatorImpl.getInstance().getAttachProtocol(248);
            if (mimojiBottomList != null) {
                mimojiBottomList.hideTimbreProgress();
            }
            MimojiVideoEditorImpl.this.mIsComposing = false;
        }

        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            String access$400 = MimojiVideoEditorImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mimoji void onBufferingUpdate[mp, percent]");
            sb.append(i);
            Log.d(access$400, sb.toString());
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            Log.d(MimojiVideoEditorImpl.TAG, "mimoji void onCompletion[mp]");
        }

        public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
            String access$400 = MimojiVideoEditorImpl.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mimoji boolean onError[mp, what, extra]");
            sb.append(i);
            sb.append("  ");
            sb.append(i2);
            Log.e(access$400, sb.toString());
            return false;
        }

        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
            String str = "  ";
            if (MimojiVideoEditorImpl.this.mTextureVideoView != null) {
                String access$400 = MimojiVideoEditorImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mimoji boolean onInfo[mp, what, extra] delay ");
                sb.append(i);
                sb.append(str);
                sb.append(i2);
                Log.d(access$400, sb.toString());
                if (MimojiViewUtil.getViewIsVisible(MimojiVideoEditorImpl.this.mTextureVideoView)) {
                    MimojiVideoEditorImpl.this.mTextureVideoView.postDelayed(new C0280O0000oO0(this), 200);
                    return false;
                }
            } else {
                String access$4002 = MimojiVideoEditorImpl.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("mimoji boolean onInfo[mp, what, extra] ");
                sb2.append(i);
                sb2.append(str);
                sb2.append(i2);
                Log.d(access$4002, sb2.toString());
                MimojiVideoEditorImpl.this.mIsComposing = false;
            }
            MimojiVideoEditorImpl.this.onFail();
            return false;
        }

        public void onPrepared(MediaPlayer mediaPlayer) {
            Log.d(MimojiVideoEditorImpl.TAG, "mimoji void onPrepared[mp]");
            MimojiViewUtil.setViewVisible(MimojiVideoEditorImpl.this.mTextureVideoView, true);
        }

        public void onSurfaceReady(Surface surface) {
            Log.d(MimojiVideoEditorImpl.TAG, "mimoji void onSurfaceReady[surface]");
            if (MimojiVideoEditorImpl.this.mWaitingResultSurfaceTexture) {
                MimojiVideoEditorImpl.this.startPlay(surface);
            }
        }

        public void onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        }

        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
            Log.d(MimojiVideoEditorImpl.TAG, "mimoji void onVideoSizeChanged[mp, width, height]");
        }
    }

    class MimojiVideo2GifCallback implements Callback {
        private int count;
        private int index;

        private MimojiVideo2GifCallback() {
        }

        public void OnConvertProgress(int i) {
            if (i != 100) {
                String access$400 = MimojiVideoEditorImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mimoji void video2gif[]  Video  ");
                sb.append(this.index);
                sb.append(" convert GIF progress : ");
                sb.append(i);
                sb.append("%");
                Log.d(access$400, sb.toString());
            } else if (this.index == this.count - 1) {
                MimojiEmoticon mimojiEmoticon = (MimojiEmoticon) ModeCoordinatorImpl.getInstance().getAttachProtocol(250);
                if (mimojiEmoticon != null) {
                    MimojiVideoEditorImpl.this.mIsComposing = false;
                    mimojiEmoticon.coverEmoticonSuccess();
                }
            }
        }

        public int getCount() {
            return this.count;
        }

        public int getIndex() {
            return this.index;
        }

        public void setCount(int i) {
            this.count = i;
        }

        public void setIndex(int i) {
            this.index = i;
        }
    }

    static {
        System.loadLibrary("vvc++_shared");
        System.loadLibrary("ffmpeg");
        System.loadLibrary("mimoji_video2gif");
    }

    private MimojiVideoEditorImpl(ActivityBase activityBase) {
        this.mContext = activityBase.getCameraAppImpl().getApplicationContext();
    }

    public static MimojiVideoEditorImpl create(ActivityBase activityBase) {
        return new MimojiVideoEditorImpl(activityBase);
    }

    /* access modifiers changed from: private */
    public synchronized void onFail() {
        Log.d(TAG, "mimoji void onFail[]");
        FileUtils.deleteFile(MimojiHelper2.VIDEO_DEAL_CACHE_FILE);
        MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
        if (mimojiFullScreenProtocol != null) {
            mimojiFullScreenProtocol.onCombineError();
        } else {
            Log.e(TAG, "mimoji void onFail null");
        }
        this.mIsComposing = false;
    }

    /* access modifiers changed from: private */
    public synchronized void onSuccess(String str, int i) {
        MimojiFullScreenProtocol mimojiFullScreenProtocol = (MimojiFullScreenProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(249);
        if (mimojiFullScreenProtocol != null) {
            if (i == 0) {
                mimojiFullScreenProtocol.concatResult(str, i);
            } else {
                String str2 = null;
                try {
                    if (str.contains("mimoji_normal") || str.contains("mimoji_deal")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(Storage.DIRECTORY);
                        sb.append(File.separator);
                        sb.append(FileUtils.createtFileName("MIMOJI_", "mp4"));
                        str2 = sb.toString();
                        FileUtils.copyFile(new File(str), new File(str2));
                    }
                    if (TextUtils.isEmpty(str2)) {
                        onFail();
                    } else {
                        mimojiFullScreenProtocol.onCombineSuccess(str2);
                    }
                } catch (Exception e) {
                    String str3 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("mimoji void cnSuccess[savePath] Exception ");
                    sb2.append(e.getMessage());
                    Log.e(str3, sb2.toString());
                    onFail();
                }
            }
            this.mIsComposing = false;
        } else {
            Log.e(TAG, "mimoji void cnSuccess[savePath] null");
            onFail();
        }
    }

    private void release() {
        this.mIsRelease = true;
        cancelVideo2gif();
        this.mGifSourcePath = null;
        if (this.mMimojiVideo2GifCallback != null) {
            this.mMimojiVideo2GifCallback = null;
        }
        if (this.mMimojiChangeTimbreCallback != null) {
            this.mMimojiChangeTimbreCallback = null;
        }
        TextureVideoView textureVideoView = this.mTextureVideoView;
        if (textureVideoView != null) {
            textureVideoView.stop();
            this.mMimojiMediaPlayerCallback = null;
            this.mTextureVideoView.setMediaPlayerCallback(null);
            this.mTextureVideoView = null;
        }
    }

    /* access modifiers changed from: private */
    public void startPlay(Surface surface) {
        float f;
        TextureVideoView textureVideoView;
        Log.d(TAG, "mimoji void startPlay[surface]");
        if (!MimojiViewUtil.getViewIsVisible(this.mTextureVideoView) || surface == null || this.mIsRelease) {
            release();
            return;
        }
        this.mWaitingResultSurfaceTexture = false;
        int i = this.mOrientation;
        if (i == 0 || i == 180) {
            this.mTextureVideoView.setScaleType(3);
            textureVideoView = this.mTextureVideoView;
            f = (float) this.mOrientation;
        } else {
            this.mTextureVideoView.setScaleType(7);
            textureVideoView = this.mTextureVideoView;
            f = this.mOrientation == 270 ? 0.0f : 180.0f;
        }
        textureVideoView.setRotation(f);
        this.mTextureVideoView.setLoop(true);
        this.mTextureVideoView.setClearSurface(false);
        this.mTextureVideoView.setVideoPath(this.mVideoSavePath);
        this.mTextureVideoView.setVideoSpecifiedSize(this.mEncodeWidth, this.mEncodeHeight);
        this.mTextureVideoView.start();
    }

    public /* synthetic */ void O00oO0() {
        if (FileUtils.checkFileConsist(MimojiHelper2.VIDEO_NORMAL_CACHE_FILE)) {
            pausePlay();
            FileUtils.deleteFile(MimojiHelper2.VIDEO_DEAL_CACHE_FILE);
            FileUtils.makeNoMediaDir(MimojiHelper2.VIDEO_CACHE_DIR);
            combineVideoAudio(MimojiHelper2.VIDEO_NORMAL_CACHE_FILE, 0);
        }
    }

    public void cancelVideo2gif() {
        if (FileUtils.checkFileConsist(this.mGifSourcePath) && isComposing()) {
            MediaProcess.CancelGifConvert(this.mGifSourcePath);
        }
    }

    public void changeTimbre() {
        new Thread(new C0279O0000oO(this)).start();
    }

    public synchronized void combineVideoAudio(String str, int i) {
        int i2 = i;
        synchronized (this) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mimoji void combineVideoAudio[savePath]");
            sb.append(str);
            Log.d(str2, sb.toString());
            this.composeTime = System.currentTimeMillis();
            this.mIsComposing = true;
            if (i2 == 0) {
                DataRepository.dataItemLive().getMimojiStatusManager2().setMode(10);
                this.mIsRelease = false;
            }
            if (this.mMimojiChangeTimbreCallback == null) {
                this.mMimojiChangeTimbreCallback = new MimojiChangeTimbreCallback();
            }
            MimojiStatusManager2 mimojiStatusManager2 = DataRepository.dataItemLive().getMimojiStatusManager2();
            if (mimojiStatusManager2.getCurrentMimojiTimbreInfo() != null) {
                Log.d(TAG, "mimoji void startPlay[surface]  timbre start");
                if (this.mVoiceMap == null) {
                    this.mVoiceMap = new HashMap();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(mimojiStatusManager2.getCurrentMimojiTimbreInfo().getTimbreId());
                sb2.append("");
                this.mVoiceMap.put("mode", sb2.toString());
                MediaProcess.AddVoiceChangeFilter(this.mVoiceMap);
                this.mMimojiChangeTimbreCallback.setStopRecordType(i2);
                MediaProcess.Convert(str, 2000, MimojiHelper2.VIDEO_DEAL_CACHE_FILE, true, 30, SenderReport.MTU, 0, 0, 1.0f, this.mMimojiChangeTimbreCallback);
            } else {
                onSuccess(MimojiHelper2.VIDEO_NORMAL_CACHE_FILE, i2);
            }
        }
    }

    public boolean init(TextureVideoView textureVideoView, String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mimoji videoeditor init videoUri ");
        sb.append(str);
        Log.v(str2, sb.toString());
        this.mWaitingResultSurfaceTexture = true;
        this.mTextureVideoView = textureVideoView;
        this.mVideoSavePath = str;
        this.mGifSourcePath = null;
        if (this.mMimojiChangeTimbreCallback == null) {
            this.mMimojiChangeTimbreCallback = new MimojiChangeTimbreCallback();
        }
        if (this.mMimojiVideo2GifCallback == null) {
            this.mMimojiVideo2GifCallback = new MimojiVideo2GifCallback();
        }
        if (this.mMimojiMediaPlayerCallback == null) {
            this.mMimojiMediaPlayerCallback = new MimojiMediaPlayerCallback();
        }
        this.mTextureVideoView.setMediaPlayerCallback(this.mMimojiMediaPlayerCallback);
        this.mTextureVideoView.setIsNeedAudio(false);
        return true;
    }

    public boolean isAvaliable() {
        return MimojiViewUtil.getViewIsVisible(this.mTextureVideoView) && !isComposing();
    }

    public boolean isComposing() {
        return this.mIsComposing;
    }

    public boolean isPlaying() {
        TextureVideoView textureVideoView = this.mTextureVideoView;
        if (textureVideoView != null) {
            return textureVideoView.isPlaying();
        }
        return false;
    }

    public void onDestory() {
        if (DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPreviewPlay()) {
            DataRepository.dataItemLive().getMimojiStatusManager2().setMode(2);
            Log.d(TAG, "mimoji void onDestory[]");
        }
        this.mIsComposing = false;
        release();
    }

    public boolean pausePlay() {
        if (this.mTextureVideoView == null) {
            return false;
        }
        Log.d(TAG, "mimoji void pausePlay[]");
        this.mTextureVideoView.stop();
        return true;
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(252, this);
    }

    public boolean resumePlay() {
        if (this.mTextureVideoView != null) {
            Log.d(TAG, "mimoji void resumePlay[]");
            this.mTextureVideoView.resume();
        }
        return false;
    }

    public void setRecordParameter(int i, int i2, int i3) {
        this.mOrientation = Math.max(0, i3);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setRecordParameter:  ");
        sb.append(i);
        String str2 = " | ";
        sb.append(str2);
        sb.append(i2);
        sb.append(str2);
        sb.append(this.mOrientation);
        Log.d(str, sb.toString());
        this.mEncodeWidth = i;
        this.mEncodeHeight = i2;
    }

    public void startPlay() {
        if (this.mVideoSavePath != null) {
            if (this.mTextureVideoView.getPreviewSurface() == null) {
                this.mWaitingResultSurfaceTexture = true;
                this.mTextureVideoView.start();
            } else {
                startPlay(this.mTextureVideoView.getPreviewSurface());
            }
        }
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(252, this);
        release();
    }

    public void video2gif(List list) {
        List list2 = list;
        this.mGifSourcePath = null;
        if (list2 == null || list.size() == 0) {
            MimojiEmoticon mimojiEmoticon = (MimojiEmoticon) ModeCoordinatorImpl.getInstance().getAttachProtocol(250);
            if (mimojiEmoticon != null) {
                mimojiEmoticon.coverEmoticonSuccess();
            }
            return;
        }
        if (this.mMimojiVideo2GifCallback == null) {
            this.mMimojiVideo2GifCallback = new MimojiVideo2GifCallback();
        }
        this.mMimojiVideo2GifCallback.setCount(list.size());
        this.mIsComposing = true;
        for (int i = 0; i < list.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(MimojiHelper2.EMOTICON_MP4_CACHE_DIR);
            sb.append(((EmoInfo) list2.get(i)).getEmoName());
            sb.append(".mp4");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
            sb3.append(((EmoInfo) list2.get(i)).getEmoName());
            sb3.append(".gif");
            String sb4 = sb3.toString();
            FileUtils.makeDir(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
            String str = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(" source :");
            sb5.append(sb2);
            sb5.append("\n target : ");
            sb5.append(sb4);
            Log.d(str, sb5.toString());
            this.mGifSourcePath = sb2;
            this.mMimojiVideo2GifCallback.setIndex(i);
            if (MediaProcess.ConvertGif(sb2, sb4, 20, 100000000, 0, 5000, 1.0f, this.mMimojiVideo2GifCallback) != 0) {
                Log.d(TAG, "mimoji void video2gif[] cover fail");
                MimojiEmoticon mimojiEmoticon2 = (MimojiEmoticon) ModeCoordinatorImpl.getInstance().getAttachProtocol(250);
                if (mimojiEmoticon2 != null) {
                    this.mIsComposing = false;
                    mimojiEmoticon2.coverEmoticonError();
                }
            }
        }
    }
}

package com.android.camera.features.mimoji2.module.protocol;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import androidx.annotation.StringRes;
import com.android.camera.SurfaceTextureScreenNail.ExternalFrameProcessor;
import com.android.camera.features.mimoji2.bean.MimojiBgInfo;
import com.android.camera.features.mimoji2.bean.MimojiInfo2;
import com.android.camera.features.mimoji2.bean.MimojiTimbreInfo;
import com.android.camera.protocol.ModeProtocol;
import com.android.camera.protocol.ModeProtocol.BaseProtocol;
import com.android.camera.ui.TextureVideoView;
import com.android.camera2.Camera2Proxy.PreviewCallback;
import com.arcsoft.avatar2.emoticon.EmoInfo;
import java.util.List;

public interface MimojiModeProtocol extends ModeProtocol {

    public interface MimojiAvatarEngine2 extends BaseProtocol, ExternalFrameProcessor, PreviewCallback {
        public static final int TYPE_TAG = 246;

        void backToPreview(boolean z, boolean z2);

        void changeToGif(boolean z);

        boolean deleteMimojiCache(int i);

        Bitmap getGifBitmap();

        String getTimeValue();

        String getVideoCache();

        void initAvatarEngine(int i, int i2, int i3, int i4, boolean z);

        void initMimojiResource();

        boolean isAvatarInited();

        boolean isOnCreateMimoji();

        boolean isRecordStopping();

        boolean isRecording();

        void onCaptureImage();

        boolean onCreateCapture();

        void onDeviceRotationChange(int i);

        void onDrawFrame(Rect rect, int i, int i2, boolean z);

        void onMimojiChangeBg(MimojiBgInfo mimojiBgInfo);

        void onMimojiChangeTimbre(MimojiTimbreInfo mimojiTimbreInfo, int i);

        void onMimojiCreate();

        void onMimojiDeleted();

        void onMimojiInitFinish();

        void onMimojiSelect(MimojiInfo2 mimojiInfo2, boolean z);

        void onRecordStart();

        void onRecordStop(int i);

        void release();

        void reloadConfig();

        void setDetectSuccess(boolean z);

        void setDisableSingleTapUp(boolean z);
    }

    public interface MimojiBottomList extends BaseProtocol {
        public static final int TYPE_TAG = 248;

        void firstProgressShow(boolean z);

        int hideTimbreProgress();

        int refreshMimojiList();

        int refreshMimojiList(int i);

        int switchMimojiList();
    }

    public interface MimojiEditor2 extends BaseProtocol {
        public static final int TYPE_TAG = 247;

        public interface MimojiEmoticon extends BaseProtocol {
            public static final int TYPE_TAG = 250;

            void backToPreview(boolean z);

            void coverEmoticonError();

            void coverEmoticonSuccess();

            void release();

            void setIsBackToPreview(boolean z);

            void updateEmoticonGifProgress(int i);

            void updateEmoticonPictureProgress(String str, EmoInfo emoInfo, boolean z);

            void updateEmoticonThumbnailProgress(int i, EmoInfo emoInfo, int i2);
        }

        void createEmoticonPicture(List list);

        void createEmoticonThumbnail();

        void createEmoticonVideo(List list);

        void directlyEnterEditMode(MimojiInfo2 mimojiInfo2, int i);

        void onDeviceRotationChange(int i);

        void onTypeConfigSelect(int i);

        void quitAndSaveEdit(boolean z);

        void quitCoverEmoticon();

        void reInitMimojiEdit();

        void releaseRender();

        void requestRender(boolean z);

        void resetClickEnable(boolean z);

        void resetConfig();

        void showEmoticon();

        void startMimojiEdit(int i);
    }

    public interface MimojiFullScreenProtocol extends BaseProtocol {
        public static final int TYPE_TAG = 249;

        void alertTop(int i, @StringRes int i2, long j);

        void concatResult(String str, int i);

        String getMimojiVideoSavePath();

        boolean isMimojiRecordPreviewShowing();

        void onCombineError();

        void onCombineSuccess(String... strArr);

        void onMimojiSaveToLocalFinished(Uri uri);

        void setPreviewCover(Bitmap bitmap);

        void showPreviewCover(boolean z);

        void startMimojiRecordPreview();

        void startMimojiRecordSaving();
    }

    public interface MimojiVideoEditor extends BaseProtocol {
        public static final int TYPE_TAG = 252;

        void cancelVideo2gif();

        void changeTimbre();

        void combineVideoAudio(String str, int i);

        boolean init(TextureVideoView textureVideoView, String str);

        boolean isAvaliable();

        boolean isComposing();

        boolean isPlaying();

        void onDestory();

        boolean pausePlay();

        boolean resumePlay();

        void setRecordParameter(int i, int i2, int i3);

        void startPlay();

        void video2gif(List list);
    }
}

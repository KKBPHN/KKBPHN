package com.android.camera.features.mimoji2.widget.helper;

import com.android.camera.CameraSettings;
import com.android.camera.data.data.DataItemBase;
import com.android.camera.features.mimoji2.bean.MimojiBgInfo;
import com.android.camera.features.mimoji2.bean.MimojiInfo2;
import com.android.camera.features.mimoji2.bean.MimojiTimbreInfo;

public class MimojiStatusManager2 extends DataItemBase {
    public static final String DATA_MIMOJI_RECORD_STATE = "mimoji_record_state";
    public static final String DATA_MIMOJI_RECORD_STATE_FROM_GIF = "mimoji_record_state_from_gif";
    private static final String KEY = "camera_settings_mimoji";
    public static final int MIMOJI_CREATE = 4;
    public static final int MIMOJI_EDIT = 6;
    public static final int MIMOJI_EMOTICON = 8;
    public static final int MIMOJI_NONE = 0;
    public static final int MIMOJI_PANEL_AVATAR = 1;
    public static final int MIMOJI_PANEL_AVATAR_CARTOON = 101;
    public static final int MIMOJI_PANEL_AVATAR_HUMAN = 100;
    public static final int MIMOJI_PANEL_BG = 2;
    public static final int MIMOJI_PANEL_CLOSE = 0;
    public static final int MIMOJI_PANEL_TIMBRE = 3;
    public static final int MIMOJI_PHOTO = 0;
    public static final int MIMOJI_PREVIEW = 2;
    public static final int MIMOJI_PREVIEW_PLAY = 10;
    public static final int MIMOJI_VIDEO = 1;
    private int mAvatarPanelState = 100;
    private MimojiBgInfo mCurrentMimojiBgInfo;
    private MimojiInfo2 mCurrentMimojiInfo2;
    private MimojiTimbreInfo mCurrentMimojiTimbreInfo;
    private volatile boolean mIsLoading = false;
    private int mMode = 0;
    private int mPanelState = 0;

    public boolean IsLoading() {
        return this.mIsLoading;
    }

    public int getAvatarPanelState() {
        return this.mAvatarPanelState;
    }

    public MimojiBgInfo getCurrentMimojiBgInfo() {
        return this.mCurrentMimojiBgInfo;
    }

    public MimojiInfo2 getCurrentMimojiInfo() {
        return this.mCurrentMimojiInfo2;
    }

    public synchronized String getCurrentMimojiState() {
        if (this.mCurrentMimojiInfo2 == null) {
            return "close_state";
        }
        String str = this.mCurrentMimojiInfo2.mConfigPath;
        if (str != null && !str.isEmpty()) {
            return str;
        }
        return "close_state";
    }

    public MimojiTimbreInfo getCurrentMimojiTimbreInfo() {
        return this.mCurrentMimojiTimbreInfo;
    }

    public int getMimojiPanelState() {
        return this.mPanelState;
    }

    public int getMimojiRecordState() {
        return getInt(DATA_MIMOJI_RECORD_STATE, 0);
    }

    public int getMimojiRecordStateFromGif() {
        return getInt(DATA_MIMOJI_RECORD_STATE_FROM_GIF, 0);
    }

    public synchronized int getMode() {
        return this.mMode;
    }

    public boolean isInMimojiCreate() {
        return this.mMode == 4;
    }

    public boolean isInMimojiEdit() {
        return this.mMode == 6;
    }

    public boolean isInMimojiEmoticon() {
        return this.mMode == 8;
    }

    public boolean isInMimojiPhoto() {
        return getMimojiRecordState() == 0;
    }

    public boolean isInMimojiPreview() {
        return this.mMode <= 2;
    }

    public boolean isInMimojiPreviewPlay() {
        return this.mMode == 10;
    }

    public boolean isInPreviewSurface() {
        return this.mMode <= 4;
    }

    public boolean isTransient() {
        return false;
    }

    public String provideKey() {
        return KEY;
    }

    public synchronized void reset() {
        this.mMode = 0;
        setMimojiPanelState(0);
        this.mCurrentMimojiBgInfo = null;
        this.mCurrentMimojiInfo2 = null;
        this.mCurrentMimojiTimbreInfo = null;
        if (!CameraSettings.retainCameraMode()) {
            setMimojiRecordState(0);
        }
    }

    public void setAvatarPanelState(int i) {
        this.mAvatarPanelState = i;
    }

    public void setCurrentMimojiBgInfo(MimojiBgInfo mimojiBgInfo) {
        this.mCurrentMimojiBgInfo = mimojiBgInfo;
    }

    public void setCurrentMimojiCloseState() {
        if (this.mCurrentMimojiInfo2 == null) {
            this.mCurrentMimojiInfo2 = new MimojiInfo2();
        }
        this.mCurrentMimojiInfo2.mConfigPath = "close_state";
    }

    public void setCurrentMimojiInfo(MimojiInfo2 mimojiInfo2) {
        this.mCurrentMimojiInfo2 = mimojiInfo2;
    }

    public void setCurrentMimojiTimbreInfo(MimojiTimbreInfo mimojiTimbreInfo) {
        this.mCurrentMimojiTimbreInfo = mimojiTimbreInfo;
    }

    public synchronized void setIsLoading(boolean z) {
        this.mIsLoading = z;
    }

    public void setMimojiPanelState(int i) {
        this.mPanelState = i;
    }

    public void setMimojiRecordState(int i) {
        editor().putInt(DATA_MIMOJI_RECORD_STATE, i).apply();
    }

    public void setMimojiRecordStateFromGif(int i) {
        editor().putInt(DATA_MIMOJI_RECORD_STATE_FROM_GIF, i).apply();
    }

    public synchronized void setMode(int i) {
        this.mMode = i;
    }
}

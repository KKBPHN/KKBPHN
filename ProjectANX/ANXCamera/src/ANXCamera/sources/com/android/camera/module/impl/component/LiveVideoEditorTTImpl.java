package com.android.camera.module.impl.component;

import android.content.Context;
import android.view.SurfaceView;
import android.view.TextureView;
import com.android.camera.ActivityBase;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.LiveVideoEditor;
import com.ss.android.vesdk.VECommonCallback;
import com.ss.android.vesdk.VEEditor;
import com.ss.android.vesdk.VEEditor.SCALE_MODE;
import com.ss.android.vesdk.VEEditor.SEEK_MODE;
import com.ss.android.vesdk.VEEditor.VIDEO_RATIO;
import com.ss.android.vesdk.VEVideoEncodeSettings.Builder;
import com.ss.android.vesdk.VEVideoEncodeSettings.COMPILE_TYPE;
import com.ss.android.vesdk.VEVideoEncodeSettings.ENCODE_BITRATE_MODE;

public class LiveVideoEditorTTImpl implements LiveVideoEditor {
    private static final String TAG = "LiveVideoEditorTTImpl";
    private Context mContext;
    private VEEditor mEditor;
    private int mEncodeHeight;
    private int mEncodeWidth;
    private boolean mNeedPrepare = true;
    private int mOrientation;
    private SurfaceView mSurfaceView;
    private String mVideoSavePath;

    private LiveVideoEditorTTImpl(ActivityBase activityBase) {
        this.mContext = activityBase.getCameraAppImpl().getApplicationContext();
    }

    public static LiveVideoEditorTTImpl create(ActivityBase activityBase) {
        return new LiveVideoEditorTTImpl(activityBase);
    }

    public void combineVideoAudio(String str, VECommonCallback vECommonCallback, VECommonCallback vECommonCallback2) {
        if (this.mEditor == null) {
            Log.d(TAG, "mEditor destroyed. skip");
            return;
        }
        this.mNeedPrepare = true;
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("combine video, width = ");
        sb.append(this.mEncodeWidth);
        sb.append(", height = ");
        sb.append(this.mEncodeHeight);
        sb.append("orientation = ");
        sb.append(this.mOrientation);
        Log.d(str2, sb.toString());
        this.mEditor.compile(str, null, new Builder(2).setCompileType(COMPILE_TYPE.COMPILE_TYPE_MP4).setVideoRes(this.mEncodeWidth, this.mEncodeHeight).setRotate(this.mOrientation).setHwEnc(true).setGopSize(30).setVideoBitrate(ENCODE_BITRATE_MODE.ENCODE_BITRATE_ABR, 4194304).setFps(30).build());
    }

    public boolean init(TextureView textureView, String str, String str2, VECommonCallback vECommonCallback, VECommonCallback vECommonCallback2) {
        Log.v(TAG, "VEEditor init");
        this.mEditor = new VEEditor(FileUtils.ROOT_DIR, textureView);
        int init = this.mEditor.init(new String[]{str}, null, str2.equals("") ^ true ? new String[]{str2} : null, VIDEO_RATIO.VIDEO_OUT_RATIO_ORIGINAL);
        if (init != 0) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Video editor init failed, ret = ");
            sb.append(init);
            Log.e(str3, sb.toString());
            VEEditor vEEditor = this.mEditor;
            if (vEEditor != null) {
                vEEditor.destroy();
                this.mEditor = null;
            }
            return false;
        }
        this.mEditor.setOnInfoListener(vECommonCallback);
        this.mEditor.setOnErrorListener(vECommonCallback2);
        this.mEditor.setLoopPlay(false);
        this.mEditor.setScaleMode(SCALE_MODE.SCALE_MODE_CENTER_CROP);
        this.mEditor.prepare();
        this.mNeedPrepare = false;
        this.mEditor.addAudioTrack(str2, 0, 15000, true);
        this.mEditor.seek(0, SEEK_MODE.EDITOR_SEEK_FLAG_LastSeek);
        return true;
    }

    public void onDestory() {
        Log.v(TAG, "VEEditor onDestory");
        VEEditor vEEditor = this.mEditor;
        if (vEEditor != null) {
            vEEditor.setOnErrorListener(null);
            this.mEditor.setOnInfoListener(null);
            this.mEditor.destroy();
            this.mEditor = null;
        }
    }

    public void pausePlay() {
        VEEditor vEEditor = this.mEditor;
        if (vEEditor != null) {
            vEEditor.pause();
        }
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(209, this);
    }

    public void resumePlay() {
        VEEditor vEEditor = this.mEditor;
        if (vEEditor != null) {
            vEEditor.seek(0, SEEK_MODE.EDITOR_SEEK_FLAG_LastSeek);
            this.mEditor.play();
        }
    }

    public void setRecordParameter(int i, int i2, int i3) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setRecordParameter:  ");
        sb.append(i);
        String str2 = " | ";
        sb.append(str2);
        sb.append(i2);
        sb.append(str2);
        sb.append(i3);
        Log.d(str, sb.toString());
        this.mOrientation = Math.max(0, i3);
        int i4 = this.mOrientation;
        if (i4 == 90 || i4 == 270) {
            this.mEncodeWidth = i2;
            this.mEncodeHeight = i;
            return;
        }
        this.mEncodeWidth = i;
        this.mEncodeHeight = i2;
    }

    public void startPlay() {
        VEEditor vEEditor = this.mEditor;
        if (vEEditor != null) {
            if (this.mNeedPrepare) {
                vEEditor.prepare();
                this.mNeedPrepare = false;
            }
            this.mEditor.setLoopPlay(true);
            this.mEditor.play();
        }
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(209, this);
        onDestory();
    }
}

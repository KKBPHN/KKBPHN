package com.xiaomi.mediaprocess;

import android.util.Log;

public class MediaComposeFile {
    private static String TAG = "MediaComposeFile";
    private long mComposeFile;
    private MediaEffectGraph mMediaEffectGraph;

    public MediaComposeFile(MediaEffectGraph mediaEffectGraph) {
        this.mMediaEffectGraph = mediaEffectGraph;
    }

    private static native void BeginComposeFileJni();

    private static native void CancelComposeFileJni();

    private static native boolean ConstructMediaComposeFileJni(long j, int i, int i2, int i3, int i4);

    private static native void DestructMediaComposeFileJni();

    private static native void SetComposeFileNameJni(String str);

    private static native void SetComposeNotifyJni(EffectNotifier effectNotifier);

    public void BeginComposeFile() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("begin mComposefile:");
        sb.append(this.mComposeFile);
        Log.d(str, sb.toString());
        BeginComposeFileJni();
    }

    public void CancelComposeFile() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("cancel mComposefile:");
        sb.append(this.mComposeFile);
        Log.d(str, sb.toString());
        CancelComposeFileJni();
    }

    public boolean ConstructMediaComposeFile(int i, int i2, int i3, int i4) {
        MediaEffectGraph mediaEffectGraph = this.mMediaEffectGraph;
        if (mediaEffectGraph == null) {
            Log.e(TAG, "effect graph is null, failed!");
            return false;
        }
        ConstructMediaComposeFileJni(mediaEffectGraph.GetGraphLine(), i, i2, i3, i4);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("construct compose file: ");
        sb.append(this.mComposeFile);
        Log.d(str, sb.toString());
        return true;
    }

    public void DestructMediaComposeFile() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("destruct mComposefile:");
        sb.append(this.mComposeFile);
        Log.d(str, sb.toString());
        DestructMediaComposeFileJni();
    }

    public void SetComposeFileName(String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("SetComposeFileName ");
        sb.append(str);
        Log.d(str2, sb.toString());
        SetComposeFileNameJni(str);
    }

    public void SetComposeNotify(EffectNotifier effectNotifier) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("SetComposeNotify mComposefile:");
        sb.append(this.mComposeFile);
        Log.d(str, sb.toString());
        SetComposeNotifyJni(effectNotifier);
    }

    public void SetMediaEffectGraph(MediaEffectGraph mediaEffectGraph) {
        this.mMediaEffectGraph = mediaEffectGraph;
    }
}

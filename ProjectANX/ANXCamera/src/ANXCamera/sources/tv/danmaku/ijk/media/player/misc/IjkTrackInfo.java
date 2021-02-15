package tv.danmaku.ijk.media.player.misc;

import android.text.TextUtils;
import com.android.camera.CameraIntentManager.CameraMode;
import tv.danmaku.ijk.media.player.IjkMediaMeta.IjkStreamMeta;

public class IjkTrackInfo implements ITrackInfo {
    private IjkStreamMeta mStreamMeta;
    private int mTrackType = 0;

    public IjkTrackInfo(IjkStreamMeta ijkStreamMeta) {
        this.mStreamMeta = ijkStreamMeta;
    }

    public IMediaFormat getFormat() {
        return new IjkMediaFormat(this.mStreamMeta);
    }

    public String getInfoInline() {
        String resolutionInline;
        StringBuilder sb = new StringBuilder(128);
        int i = this.mTrackType;
        String str = ", ";
        if (i == 1) {
            sb.append(CameraMode.VIDEO);
            sb.append(str);
            sb.append(this.mStreamMeta.getCodecShortNameInline());
            sb.append(str);
            sb.append(this.mStreamMeta.getBitrateInline());
            sb.append(str);
            resolutionInline = this.mStreamMeta.getResolutionInline();
        } else if (i == 2) {
            sb.append("AUDIO");
            sb.append(str);
            sb.append(this.mStreamMeta.getCodecShortNameInline());
            sb.append(str);
            sb.append(this.mStreamMeta.getBitrateInline());
            sb.append(str);
            resolutionInline = this.mStreamMeta.getSampleRateInline();
        } else if (i != 3) {
            resolutionInline = i != 4 ? "UNKNOWN" : "SUBTITLE";
        } else {
            sb.append("TIMEDTEXT");
            sb.append(str);
            resolutionInline = this.mStreamMeta.mLanguage;
        }
        sb.append(resolutionInline);
        return sb.toString();
    }

    public String getLanguage() {
        IjkStreamMeta ijkStreamMeta = this.mStreamMeta;
        return (ijkStreamMeta == null || TextUtils.isEmpty(ijkStreamMeta.mLanguage)) ? "und" : this.mStreamMeta.mLanguage;
    }

    public int getTrackType() {
        return this.mTrackType;
    }

    public void setMediaMeta(IjkStreamMeta ijkStreamMeta) {
        this.mStreamMeta = ijkStreamMeta;
    }

    public void setTrackType(int i) {
        this.mTrackType = i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(IjkTrackInfo.class.getSimpleName());
        sb.append('{');
        sb.append(getInfoInline());
        sb.append("}");
        return sb.toString();
    }
}

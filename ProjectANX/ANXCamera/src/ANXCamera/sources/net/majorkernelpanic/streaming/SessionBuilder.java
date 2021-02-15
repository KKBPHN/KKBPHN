package net.majorkernelpanic.streaming;

import android.content.Context;
import android.preference.PreferenceManager;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import net.majorkernelpanic.streaming.Session.Callback;
import net.majorkernelpanic.streaming.audio.AACStream;
import net.majorkernelpanic.streaming.audio.AMRNBStream;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.audio.AudioStream;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.video.H263Stream;
import net.majorkernelpanic.streaming.video.H264Stream;
import net.majorkernelpanic.streaming.video.VideoQuality;
import net.majorkernelpanic.streaming.video.VideoStream;

public class SessionBuilder {
    public static final int AUDIO_AAC = 5;
    public static final int AUDIO_AMRNB = 3;
    public static final int AUDIO_NONE = 0;
    public static final String TAG = "SessionBuilder";
    public static final int VIDEO_H263 = 2;
    public static final int VIDEO_H264 = 1;
    public static final int VIDEO_NONE = 0;
    private static volatile SessionBuilder sInstance;
    private int mAudioEncoder = 3;
    private AudioQuality mAudioQuality = AudioQuality.DEFAULT_AUDIO_QUALITY;
    private Callback mCallback = null;
    private int mCamera = 0;
    private Context mContext;
    private String mDestination = null;
    private boolean mFlash = false;
    private int mOrientation = 0;
    private String mOrigin = null;
    private SurfaceView mSurfaceView = null;
    private int mTimeToLive = 64;
    private int mVideoEncoder = 2;
    private VideoQuality mVideoQuality = VideoQuality.DEFAULT_VIDEO_QUALITY;

    private SessionBuilder() {
    }

    public static SessionBuilder copyOf(SessionBuilder sessionBuilder) {
        return new SessionBuilder().setDestination(sessionBuilder.mDestination).setOrigin(sessionBuilder.mOrigin).setSurfaceView(sessionBuilder.mSurfaceView).setPreviewOrientation(sessionBuilder.mOrientation).setVideoQuality(sessionBuilder.mVideoQuality).setVideoEncoder(sessionBuilder.mVideoEncoder).setFlashEnabled(sessionBuilder.mFlash).setCamera(sessionBuilder.mCamera).setTimeToLive(sessionBuilder.mTimeToLive).setAudioEncoder(sessionBuilder.mAudioEncoder).setAudioQuality(sessionBuilder.mAudioQuality).setContext(sessionBuilder.mContext).setCallback(sessionBuilder.mCallback);
    }

    public static final SessionBuilder getInstance() {
        if (sInstance == null) {
            synchronized (SessionBuilder.class) {
                if (sInstance == null) {
                    sInstance = new SessionBuilder();
                }
            }
        }
        return sInstance;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x008e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Session build() {
        VideoStream h264Stream;
        Session session = new Session();
        session.setOrigin(this.mOrigin);
        session.setDestination(this.mDestination);
        session.setTimeToLive(this.mTimeToLive);
        session.setCallback(this.mCallback);
        int i = this.mAudioEncoder;
        if (i == 3) {
            session.addAudioTrack(new AMRNBStream());
        } else if (i == 5) {
            AACStream aACStream = new AACStream();
            session.addAudioTrack(aACStream);
            Context context = this.mContext;
            if (context != null) {
                aACStream.setPreferences(PreferenceManager.getDefaultSharedPreferences(context));
            }
        }
        int i2 = this.mVideoEncoder;
        if (i2 != 1) {
            if (i2 == 2) {
                h264Stream = new H263Stream(this.mCamera);
            }
            if (session.getVideoTrack() != null) {
                VideoStream videoTrack = session.getVideoTrack();
                videoTrack.setFlashState(this.mFlash);
                videoTrack.setVideoQuality(this.mVideoQuality);
                videoTrack.setSurfaceView(this.mSurfaceView);
                videoTrack.setPreviewOrientation(this.mOrientation);
                videoTrack.setDestinationPorts(CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_MFSR);
            }
            if (session.getAudioTrack() != null) {
                AudioStream audioTrack = session.getAudioTrack();
                audioTrack.setAudioQuality(this.mAudioQuality);
                audioTrack.setDestinationPorts(5004);
            }
            return session;
        }
        h264Stream = new H264Stream(this.mCamera);
        Context context2 = this.mContext;
        if (context2 != null) {
            h264Stream.setPreferences(PreferenceManager.getDefaultSharedPreferences(context2));
        }
        session.addVideoTrack(h264Stream);
        if (session.getVideoTrack() != null) {
        }
        if (session.getAudioTrack() != null) {
        }
        return session;
    }

    public int getAudioEncoder() {
        return this.mAudioEncoder;
    }

    public AudioQuality getAudioQuality() {
        return this.mAudioQuality;
    }

    public int getCamera() {
        return this.mCamera;
    }

    public Context getContext() {
        return this.mContext;
    }

    public String getDestination() {
        return this.mDestination;
    }

    public boolean getFlashState() {
        return this.mFlash;
    }

    public String getOrigin() {
        return this.mOrigin;
    }

    public SurfaceView getSurfaceView() {
        return this.mSurfaceView;
    }

    public int getTimeToLive() {
        return this.mTimeToLive;
    }

    public int getVideoEncoder() {
        return this.mVideoEncoder;
    }

    public VideoQuality getVideoQuality() {
        return this.mVideoQuality;
    }

    public SessionBuilder setAudioEncoder(int i) {
        this.mAudioEncoder = i;
        return this;
    }

    public SessionBuilder setAudioQuality(AudioQuality audioQuality) {
        this.mAudioQuality = AudioQuality.copyOf(audioQuality);
        return this;
    }

    public SessionBuilder setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    public SessionBuilder setCamera(int i) {
        this.mCamera = i;
        return this;
    }

    public SessionBuilder setContext(Context context) {
        this.mContext = context;
        return this;
    }

    public SessionBuilder setDestination(String str) {
        this.mDestination = str;
        return this;
    }

    public SessionBuilder setFlashEnabled(boolean z) {
        this.mFlash = z;
        return this;
    }

    public SessionBuilder setOrigin(String str) {
        this.mOrigin = str;
        return this;
    }

    public SessionBuilder setPreviewOrientation(int i) {
        this.mOrientation = i;
        return this;
    }

    public SessionBuilder setSurfaceView(SurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
        return this;
    }

    public SessionBuilder setTimeToLive(int i) {
        this.mTimeToLive = i;
        return this;
    }

    public SessionBuilder setVideoEncoder(int i) {
        this.mVideoEncoder = i;
        return this;
    }

    public SessionBuilder setVideoQuality(VideoQuality videoQuality) {
        this.mVideoQuality = VideoQuality.copyOf(videoQuality);
        return this;
    }
}

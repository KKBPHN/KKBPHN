package net.majorkernelpanic.streaming;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.audio.AudioStream;
import net.majorkernelpanic.streaming.exceptions.CameraInUseException;
import net.majorkernelpanic.streaming.exceptions.ConfNotSupportedException;
import net.majorkernelpanic.streaming.exceptions.InvalidSurfaceException;
import net.majorkernelpanic.streaming.exceptions.StorageUnavailableException;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.video.VideoQuality;
import net.majorkernelpanic.streaming.video.VideoStream;

public class Session {
    public static final int ERROR_CAMERA_ALREADY_IN_USE = 0;
    public static final int ERROR_CAMERA_HAS_NO_FLASH = 3;
    public static final int ERROR_CONFIGURATION_NOT_SUPPORTED = 1;
    public static final int ERROR_INVALID_SURFACE = 4;
    public static final int ERROR_OTHER = 6;
    public static final int ERROR_STORAGE_NOT_READY = 2;
    public static final int ERROR_UNKNOWN_HOST = 5;
    public static final int STREAM_AUDIO = 0;
    public static final int STREAM_VIDEO = 1;
    public static final String TAG = "Session";
    private AudioStream mAudioStream = null;
    /* access modifiers changed from: private */
    public Callback mCallback;
    private String mDestination;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private Handler mMainHandler;
    private String mOrigin;
    private int mTimeToLive = 64;
    private long mTimestamp;
    /* access modifiers changed from: private */
    public Runnable mUpdateBitrate = new Runnable() {
        public void run() {
            if (Session.this.isStreaming()) {
                Session session = Session.this;
                session.postBitRate(session.getBitrate());
                Session.this.mHandler.postDelayed(Session.this.mUpdateBitrate, 500);
                return;
            }
            Session.this.postBitRate(0);
        }
    };
    /* access modifiers changed from: private */
    public VideoStream mVideoStream = null;

    public interface Callback {
        void onBitrateUpdate(long j);

        void onPreviewStarted();

        void onSessionConfigured();

        void onSessionError(int i, int i2, Exception exc);

        void onSessionStarted();

        void onSessionStopped();
    }

    public Session() {
        long currentTimeMillis = System.currentTimeMillis();
        HandlerThread handlerThread = new HandlerThread("net.majorkernelpanic.streaming.Session");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
        this.mMainHandler = new Handler(Looper.getMainLooper());
        long j = currentTimeMillis / 1000;
        this.mTimestamp = (((currentTimeMillis - (j * 1000)) >> 32) / 1000) & (j << 32);
        this.mOrigin = "127.0.0.1";
    }

    /* access modifiers changed from: private */
    public void postBitRate(final long j) {
        this.mMainHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mCallback != null) {
                    Session.this.mCallback.onBitrateUpdate(j);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void postError(final int i, final int i2, final Exception exc) {
        this.mMainHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mCallback != null) {
                    Session.this.mCallback.onSessionError(i, i2, exc);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void postPreviewStarted() {
        this.mMainHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mCallback != null) {
                    Session.this.mCallback.onPreviewStarted();
                }
            }
        });
    }

    private void postSessionConfigured() {
        this.mMainHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mCallback != null) {
                    Session.this.mCallback.onSessionConfigured();
                }
            }
        });
    }

    private void postSessionStarted() {
        this.mMainHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mCallback != null) {
                    Session.this.mCallback.onSessionStarted();
                }
            }
        });
    }

    private void postSessionStopped() {
        this.mMainHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mCallback != null) {
                    Session.this.mCallback.onSessionStopped();
                }
            }
        });
    }

    private void syncStop(int i) {
        Stream stream = i == 0 ? this.mAudioStream : this.mVideoStream;
        if (stream != null) {
            stream.stop();
        }
    }

    /* access modifiers changed from: 0000 */
    public void addAudioTrack(AudioStream audioStream) {
        removeAudioTrack();
        this.mAudioStream = audioStream;
    }

    /* access modifiers changed from: 0000 */
    public void addVideoTrack(VideoStream videoStream) {
        removeVideoTrack();
        this.mVideoStream = videoStream;
    }

    public void configure() {
        this.mHandler.post(new Runnable() {
            public void run() {
                try {
                    Session.this.syncConfigure();
                } catch (Exception unused) {
                }
            }
        });
    }

    public AudioStream getAudioTrack() {
        return this.mAudioStream;
    }

    public long getBitrate() {
        AudioStream audioStream = this.mAudioStream;
        long j = 0;
        if (audioStream != null) {
            j = 0 + audioStream.getBitrate();
        }
        VideoStream videoStream = this.mVideoStream;
        return videoStream != null ? j + videoStream.getBitrate() : j;
    }

    public Callback getCallback() {
        return this.mCallback;
    }

    public int getCamera() {
        VideoStream videoStream = this.mVideoStream;
        if (videoStream != null) {
            return videoStream.getCamera();
        }
        return 0;
    }

    public String getDestination() {
        return this.mDestination;
    }

    public long getId() {
        return this.mTimestamp;
    }

    public String getSessionDescription() {
        StringBuilder sb = new StringBuilder();
        if (this.mDestination != null) {
            sb.append("v=0\r\n");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("o=- ");
            sb2.append(this.mTimestamp);
            sb2.append(" ");
            sb2.append(this.mTimestamp);
            sb2.append(" IN IP4 ");
            sb2.append(this.mOrigin);
            String str = "\r\n";
            sb2.append(str);
            sb.append(sb2.toString());
            sb.append("s=Unnamed\r\n");
            sb.append("i=N/A\r\n");
            StringBuilder sb3 = new StringBuilder();
            sb3.append("c=IN IP4 ");
            sb3.append(this.mDestination);
            sb3.append(str);
            sb.append(sb3.toString());
            sb.append("t=0 0\r\n");
            sb.append("a=recvonly\r\n");
            AudioStream audioStream = this.mAudioStream;
            if (audioStream != null) {
                sb.append(audioStream.getSessionDescription());
                sb.append("a=control:trackID=0\r\n");
            }
            VideoStream videoStream = this.mVideoStream;
            if (videoStream != null) {
                sb.append(videoStream.getSessionDescription());
                sb.append("a=control:trackID=1\r\n");
            }
            return sb.toString();
        }
        throw new IllegalStateException("setDestination() has not been called !");
    }

    public Stream getTrack(int i) {
        return i == 0 ? this.mAudioStream : this.mVideoStream;
    }

    public VideoStream getVideoTrack() {
        return this.mVideoStream;
    }

    public boolean isStreaming() {
        AudioStream audioStream = this.mAudioStream;
        if (audioStream == null || !audioStream.isStreaming()) {
            VideoStream videoStream = this.mVideoStream;
            if (videoStream == null || !videoStream.isStreaming()) {
                return false;
            }
        }
        return true;
    }

    public void release() {
        removeAudioTrack();
        removeVideoTrack();
        this.mHandler.getLooper().quit();
    }

    /* access modifiers changed from: 0000 */
    public void removeAudioTrack() {
        AudioStream audioStream = this.mAudioStream;
        if (audioStream != null) {
            audioStream.stop();
            this.mAudioStream = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeVideoTrack() {
        VideoStream videoStream = this.mVideoStream;
        if (videoStream != null) {
            videoStream.stopPreview();
            this.mVideoStream = null;
        }
    }

    public void setAudioQuality(AudioQuality audioQuality) {
        AudioStream audioStream = this.mAudioStream;
        if (audioStream != null) {
            audioStream.setAudioQuality(audioQuality);
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setDestination(String str) {
        this.mDestination = str;
    }

    public void setOrigin(String str) {
        this.mOrigin = str;
    }

    public void setPreviewOrientation(int i) {
        VideoStream videoStream = this.mVideoStream;
        if (videoStream != null) {
            videoStream.setPreviewOrientation(i);
        }
    }

    public void setSurfaceView(final SurfaceView surfaceView) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mVideoStream != null) {
                    Session.this.mVideoStream.setSurfaceView(surfaceView);
                }
            }
        });
    }

    public void setTimeToLive(int i) {
        this.mTimeToLive = i;
    }

    public void setVideoQuality(VideoQuality videoQuality) {
        VideoStream videoStream = this.mVideoStream;
        if (videoStream != null) {
            videoStream.setVideoQuality(videoQuality);
        }
    }

    public void start() {
        this.mHandler.post(new Runnable() {
            public void run() {
                try {
                    Session.this.syncStart();
                } catch (Exception unused) {
                }
            }
        });
    }

    public void startPreview() {
        this.mHandler.post(new Runnable() {
            public void run() {
                Session session;
                int i;
                if (Session.this.mVideoStream != null) {
                    try {
                        Session.this.mVideoStream.startPreview();
                        Session.this.postPreviewStarted();
                        Session.this.mVideoStream.configure();
                        return;
                    } catch (CameraInUseException e) {
                        e = e;
                        session = Session.this;
                        i = 0;
                    } catch (ConfNotSupportedException e2) {
                        Session.this.postError(1, 1, e2);
                        return;
                    } catch (InvalidSurfaceException e3) {
                        e = e3;
                        session = Session.this;
                        i = 4;
                    } catch (IOException | RuntimeException e4) {
                        Session.this.postError(6, 1, e4);
                        return;
                    } catch (StorageUnavailableException e5) {
                        e = e5;
                        session = Session.this;
                        i = 2;
                    }
                } else {
                    return;
                }
                session.postError(i, 1, e);
            }
        });
    }

    public void stop() {
        this.mHandler.post(new Runnable() {
            public void run() {
                Session.this.syncStop();
            }
        });
    }

    public void stopPreview() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mVideoStream != null) {
                    Session.this.mVideoStream.stopPreview();
                }
            }
        });
    }

    public void switchCamera() {
        this.mHandler.post(new Runnable() {
            public void run() {
                Session session;
                int i;
                if (Session.this.mVideoStream != null) {
                    try {
                        Session.this.mVideoStream.switchCamera();
                        Session.this.postPreviewStarted();
                        return;
                    } catch (CameraInUseException e) {
                        e = e;
                        session = Session.this;
                        i = 0;
                    } catch (ConfNotSupportedException e2) {
                        Session.this.postError(1, 1, e2);
                        return;
                    } catch (InvalidSurfaceException e3) {
                        e = e3;
                        session = Session.this;
                        i = 4;
                    } catch (IOException | RuntimeException e4) {
                        Session.this.postError(6, 1, e4);
                        return;
                    }
                } else {
                    return;
                }
                session.postError(i, 1, e);
            }
        });
    }

    public void syncConfigure() {
        int i = 0;
        while (i < 2) {
            Stream stream = i == 0 ? this.mAudioStream : this.mVideoStream;
            if (stream != null && !stream.isStreaming()) {
                try {
                    stream.configure();
                } catch (CameraInUseException e) {
                    postError(0, i, e);
                    throw e;
                } catch (StorageUnavailableException e2) {
                    postError(2, i, e2);
                    throw e2;
                } catch (ConfNotSupportedException e3) {
                    postError(1, i, e3);
                    throw e3;
                } catch (InvalidSurfaceException e4) {
                    postError(4, i, e4);
                    throw e4;
                } catch (IOException e5) {
                    postError(6, i, e5);
                    throw e5;
                } catch (RuntimeException e6) {
                    postError(6, i, e6);
                    throw e6;
                }
            }
            i++;
        }
        postSessionConfigured();
    }

    public void syncStart() {
        syncStart(1);
        try {
            syncStart(0);
        } catch (RuntimeException e) {
            syncStop(1);
            throw e;
        } catch (IOException e2) {
            syncStop(1);
            throw e2;
        }
    }

    public void syncStart(int i) {
        Stream stream = i == 0 ? this.mAudioStream : this.mVideoStream;
        if (stream != null && !stream.isStreaming()) {
            try {
                InetAddress byName = InetAddress.getByName(this.mDestination);
                stream.setTimeToLive(this.mTimeToLive);
                stream.setDestinationAddress(byName);
                stream.start();
                int i2 = 1 - i;
                if (getTrack(i2) == null || getTrack(i2).isStreaming()) {
                    postSessionStarted();
                }
                if (getTrack(i2) == null || !getTrack(i2).isStreaming()) {
                    this.mHandler.post(this.mUpdateBitrate);
                }
            } catch (UnknownHostException e) {
                postError(5, i, e);
                throw e;
            } catch (CameraInUseException e2) {
                postError(0, i, e2);
                throw e2;
            } catch (StorageUnavailableException e3) {
                postError(2, i, e3);
                throw e3;
            } catch (ConfNotSupportedException e4) {
                postError(1, i, e4);
                throw e4;
            } catch (InvalidSurfaceException e5) {
                postError(4, i, e5);
                throw e5;
            } catch (IOException e6) {
                postError(6, i, e6);
                throw e6;
            } catch (RuntimeException e7) {
                postError(6, i, e7);
                throw e7;
            }
        }
    }

    public void syncStop() {
        syncStop(0);
        syncStop(1);
        postSessionStopped();
    }

    public void toggleFlash() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (Session.this.mVideoStream != null) {
                    try {
                        Session.this.mVideoStream.toggleFlash();
                    } catch (RuntimeException e) {
                        Session.this.postError(3, 1, e);
                    }
                }
            }
        });
    }

    public boolean trackExists(int i) {
        boolean z = true;
        if (i == 0) {
            if (this.mAudioStream == null) {
                z = false;
            }
            return z;
        }
        if (this.mVideoStream == null) {
            z = false;
        }
        return z;
    }
}

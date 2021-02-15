package com.arcsoft.avatar2.recoder;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import com.xiaomi.asr.engine.record.AudioType.Frequency;
import java.io.IOException;
import java.nio.ByteBuffer;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class AudioEncoder extends BaseEncoder {
    private static final String u = "Arc_BaseEncoder";
    private static final String v = "Arc_Audio_Encoder";
    private final int A;
    private final int B;
    private final String C;
    private final int D;
    private int E;
    private int F;
    private int G;
    private int H;
    private int I;
    private int J;
    /* access modifiers changed from: private */
    public int K;
    private Object L;
    private long M;
    private boolean N;
    public final String NAME = "ARC_S";
    /* access modifiers changed from: private */
    public AudioRecord w;
    private Thread x;
    private final int y;
    private final int z;

    public AudioEncoder(MuxerWrapper muxerWrapper, Object obj, RecordingListener recordingListener) {
        super(muxerWrapper, obj, recordingListener);
        int i = 44100;
        this.y = 44100;
        this.z = 16;
        this.A = 2;
        this.B = 1;
        this.C = "audio/mp4a-latm";
        this.D = 2000000;
        if (VERSION.SDK_INT > 28) {
            i = Frequency.FREQ_22KHZ;
        }
        this.E = i;
        this.F = 16;
        this.G = 2;
        this.H = 1;
        this.I = 2000000;
        this.L = new Object();
        this.c = false;
        this.M = 0;
    }

    private boolean b() {
        this.J = AudioRecord.getMinBufferSize(this.E, this.F, this.G);
        try {
            AudioRecord audioRecord = new AudioRecord(this.H, this.E, this.F, this.G, this.J);
            this.w = audioRecord;
        } catch (Exception e) {
            RecordingListener recordingListener = this.o;
            if (recordingListener != null) {
                recordingListener.onRecordingListener(625, Integer.valueOf(0));
            }
            e.printStackTrace();
        }
        AudioRecord audioRecord2 = this.w;
        if (audioRecord2 == null || 1 != audioRecord2.getState()) {
            this.w = null;
            return false;
        }
        this.K = this.J;
        return true;
    }

    private boolean c() {
        String str = "audio/mp4a-latm";
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(str, this.E, this.F);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("channel-count", d());
        createAudioFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, this.I);
        try {
            this.i = MediaCodec.createEncoderByType(str);
        } catch (IOException e) {
            this.i = null;
            RecordingListener recordingListener = this.o;
            if (recordingListener != null) {
                recordingListener.onRecordingListener(545, Integer.valueOf(0));
            }
            e.printStackTrace();
        }
        MediaCodec mediaCodec = this.i;
        if (mediaCodec == null) {
            return false;
        }
        try {
            mediaCodec.configure(createAudioFormat, null, null, 1);
        } catch (Exception e2) {
            e2.printStackTrace();
            RecordingListener recordingListener2 = this.o;
            if (recordingListener2 != null) {
                recordingListener2.onRecordingListener(546, Integer.valueOf(0));
            }
        }
        return true;
    }

    private int d() {
        return 12 == this.G ? 2 : 1;
    }

    public void notifyNewFrameAvailable() {
    }

    public void prepare(boolean z2) {
        if (b() && c()) {
            this.N = true;
        }
    }

    public void release(boolean z2) {
        Thread thread = this.x;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable th) {
                this.x = null;
                throw th;
            }
            this.x = null;
        }
        this.w.release();
        this.N = false;
        super.release(z2);
    }

    public void startRecording() {
        if (this.N) {
            if (this.x == null) {
                super.startRecording();
                this.x = new Thread(v) {
                    public void run() {
                        super.run();
                        setName("ARC_S");
                        try {
                            AudioEncoder.this.w.startRecording();
                        } catch (Exception e) {
                            e.printStackTrace();
                            RecordingListener recordingListener = AudioEncoder.this.o;
                            if (recordingListener != null) {
                                recordingListener.onRecordingListener(626, Integer.valueOf(0));
                            }
                        }
                        try {
                            AudioEncoder.this.i.start();
                        } catch (Exception unused) {
                            RecordingListener recordingListener2 = AudioEncoder.this.o;
                            if (recordingListener2 != null) {
                                recordingListener2.onRecordingListener(547, Integer.valueOf(0));
                            }
                        }
                        long nanoTime = System.nanoTime();
                        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(AudioEncoder.this.K);
                        while (true) {
                            AudioEncoder audioEncoder = AudioEncoder.this;
                            if (!audioEncoder.d) {
                                if (audioEncoder.e) {
                                    long nanoTime2 = System.nanoTime();
                                    synchronized (AudioEncoder.this.f) {
                                        if (AudioEncoder.this.e) {
                                            try {
                                                AudioEncoder.this.w.stop();
                                            } catch (Exception e2) {
                                                e2.printStackTrace();
                                                if (AudioEncoder.this.o != null) {
                                                    AudioEncoder.this.o.onRecordingListener(627, Integer.valueOf(0));
                                                }
                                            }
                                            try {
                                                AudioEncoder.this.f.wait();
                                                try {
                                                    AudioEncoder.this.w.startRecording();
                                                } catch (Exception e3) {
                                                    e3.printStackTrace();
                                                    if (AudioEncoder.this.o != null) {
                                                        AudioEncoder.this.o.onRecordingListener(626, Integer.valueOf(0));
                                                    }
                                                }
                                                AudioEncoder.this.g += System.nanoTime() - nanoTime2;
                                                AudioEncoder.this.n.add(Long.valueOf(AudioEncoder.this.g));
                                            } catch (InterruptedException e4) {
                                                e4.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                allocateDirect.clear();
                                if (AudioEncoder.this.w.read(allocateDirect, AudioEncoder.this.K) > 0) {
                                    AudioEncoder.this.encode(allocateDirect, ((System.nanoTime() - nanoTime) - AudioEncoder.this.g) / 1000);
                                    AudioEncoder.this.drain();
                                }
                            } else {
                                try {
                                    audioEncoder.w.stop();
                                } catch (Exception e5) {
                                    e5.printStackTrace();
                                    RecordingListener recordingListener3 = AudioEncoder.this.o;
                                    if (recordingListener3 != null) {
                                        recordingListener3.onRecordingListener(627, Integer.valueOf(0));
                                    }
                                }
                                AudioEncoder.this.encode(null, 0);
                                AudioEncoder.this.drain();
                                return;
                            }
                        }
                    }
                };
                this.x.start();
                return;
            }
            throw new RuntimeException("Audio encoder thread has been started already, can not start twice.");
        }
    }
}

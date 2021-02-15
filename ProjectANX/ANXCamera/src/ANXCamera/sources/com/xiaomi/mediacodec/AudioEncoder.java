package com.xiaomi.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import com.android.camera.SensorStateManager;
import com.xiaomi.asr.engine.record.AudioType.Frequency;
import com.xiaomi.mediacodec.MoviePlayer.MediaFrame;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class AudioEncoder {
    private int aacsamplerate = 4;
    private int audioSamplerate = 0;
    private MediaCodec encoder = null;
    private MediaFormat encoderFormat = null;
    private MiHWEncoder encodercallback;
    private boolean first_frame = false;
    private BufferInfo info = null;
    private boolean initmediacodec = false;
    private long last_aac_timestamp = 0;
    private double last_pcm_timestamp = 0.0d;
    private FileOutputStream mAudioFile;
    private byte[] outByteBuffer = null;
    private FileOutputStream outputStream;
    private MediaFormat outputencoderFormat = null;
    private int perpcmsize = 0;
    private double recordTime = 0.0d;

    private void addADtsHeader(byte[] bArr, int i, int i2) {
        bArr[0] = -1;
        bArr[1] = -7;
        bArr[2] = (byte) (64 + (i2 << 2) + 0);
        bArr[3] = (byte) (128 + (i >> 11));
        bArr[4] = (byte) ((i & SensorStateManager.SENSOR_ALL) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    private int getADTSsamplerate(int i) {
        switch (i) {
            case 7350:
                return 12;
            case 8000:
                return 11;
            case Frequency.FREQ_11KHZ /*11025*/:
                return 10;
            case 12000:
                return 9;
            case 16000:
                return 8;
            case Frequency.FREQ_22KHZ /*22050*/:
                return 7;
            case 24000:
                return 6;
            case 32000:
                return 5;
            case Frequency.FREQ_48KHZ /*48000*/:
                return 3;
            case 64000:
                return 2;
            case 88200:
                return 1;
            case 96000:
                return 0;
            default:
                return 4;
        }
    }

    public void SetEncoderDataCallback(MiHWEncoder miHWEncoder) {
        this.encodercallback = miHWEncoder;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x010c  */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean encodecPcmToAAc(int i, byte[] bArr, double d) {
        int i2;
        if (bArr != null) {
            MediaCodec mediaCodec = this.encoder;
            if (mediaCodec != null) {
                i2 = mediaCodec.dequeueInputBuffer(0);
                if (i2 >= 0) {
                    ByteBuffer byteBuffer = this.encoder.getInputBuffers()[i2];
                    byteBuffer.clear();
                    byteBuffer.put(bArr, 0, i);
                    this.encoder.queueInputBuffer(i2, 0, i, (long) (d * 1000.0d), 0);
                }
                int dequeueOutputBuffer = this.encoder.dequeueOutputBuffer(this.info, 300);
                if (dequeueOutputBuffer == -2) {
                    if (this.encodercallback != null) {
                        this.outputencoderFormat = this.encoder.getOutputFormat();
                        this.encodercallback.addAudioFormat(this.outputencoderFormat);
                    } else {
                        Logg.LogI("MediaCodec.INFO_OUTPUT_FORMAT_CHANGED");
                    }
                }
                while (dequeueOutputBuffer >= 0) {
                    try {
                        ByteBuffer byteBuffer2 = this.encoder.getOutputBuffers()[dequeueOutputBuffer];
                        byteBuffer2.position(this.info.offset);
                        byteBuffer2.limit(this.info.offset + this.info.size);
                        ByteBuffer allocate = ByteBuffer.allocate(byteBuffer2.capacity());
                        allocate.clear();
                        allocate.position(this.info.offset);
                        byte[] bArr2 = new byte[this.info.size];
                        byteBuffer2.get(bArr2, 0, this.info.size);
                        allocate.put(bArr2, 0, this.info.size);
                        allocate.position(this.info.offset);
                        allocate.limit(this.info.offset + this.info.size);
                        MoviePlayer moviePlayer = new MoviePlayer();
                        moviePlayer.getClass();
                        MediaFrame mediaFrame = new MediaFrame();
                        mediaFrame.buffer = allocate;
                        mediaFrame.info = new BufferInfo();
                        mediaFrame.info.set(this.info.offset, this.info.size, this.info.presentationTimeUs, this.info.flags);
                        if ((this.info.flags & 2) != 0) {
                            this.info.size = 0;
                            Logg.LogI("ignoring BUFFER_FLAG_CODEC_CONFIG");
                        }
                        if (!(this.encodercallback == null || this.info.size == 0)) {
                            this.encodercallback.addAudioFrame(mediaFrame);
                        }
                        this.encoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                        dequeueOutputBuffer = this.encoder.dequeueOutputBuffer(this.info, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return i2 < 0;
            }
        }
        i2 = 0;
        if (i2 < 0) {
        }
    }

    public void initMediacodec(int i) {
        String str = "audio/mp4a-latm";
        try {
            if (this.initmediacodec) {
                Logg.LogI("AudioEncoder craete audio encoder initMediacodec has success");
                return;
            }
            this.aacsamplerate = getADTSsamplerate(i);
            this.encoderFormat = MediaFormat.createAudioFormat(str, i, 2);
            this.encoderFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, 96000);
            this.encoderFormat.setInteger("aac-profile", 2);
            this.encoderFormat.setInteger("max-input-size", 4096);
            this.encoder = MediaCodec.createEncoderByType(str);
            this.info = new BufferInfo();
            if (this.encoder == null) {
                Logg.LogI("craete encoder wrong");
                return;
            }
            this.recordTime = 0.0d;
            this.encoder.configure(this.encoderFormat, null, null, 1);
            this.encoder.start();
            this.initmediacodec = true;
            Logg.LogI("craete audio encoder initMediacodec success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseMedicacodec() {
        MediaCodec mediaCodec = this.encoder;
        if (mediaCodec != null) {
            try {
                this.recordTime = 0.0d;
                mediaCodec.stop();
                this.encoder.release();
                this.encoder = null;
                this.encoderFormat = null;
                this.info = null;
                this.initmediacodec = false;
                Logg.LogI("AudioEncoder end!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

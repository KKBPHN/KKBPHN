package com.xiaomi.asr.engine.record;

public abstract class AudioSource {
    public static final int ERR_INIT_ONE_SHOT_RECORDER_FAILED = 4;
    public static final int ERR_INIT_RECORDER_AUDIO_OCCUPIED = 2;
    public static final int ERR_INIT_RECORDER_INVALID_BUFFER_SIZE = 1;
    public static final int ERR_INIT_RECORDER_NO_AUDIO_PERMISSION = 3;
    public static final int REC_SRC_FILE = 3;

    public abstract void init();

    public abstract int read(byte[] bArr, int i, int i2);

    public abstract void release();

    public abstract void startRecording();
}

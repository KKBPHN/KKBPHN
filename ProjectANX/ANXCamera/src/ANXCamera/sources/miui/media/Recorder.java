package miui.media;

public interface Recorder {

    public interface ErrorCode {
        public static final int COULD_NOT_START = 1006;
        public static final int ENCODE_PCM_FAILED = 1004;
        public static final int FILE_NOT_EXIST = 1009;
        public static final int ILLEGAL_STATE = 1001;
        public static final int MAX_DURATION_REACHED = 1008;
        public static final int MAX_SIZE_REACHED = 1007;
        public static final int OUT_STREAM_NOT_READY = 1002;
        public static final int RECORD_PCM_FAILED = 1003;
        public static final int SERVER_DIED = 1010;
        public static final int UNKNOWN = 999;
        public static final int WRITE_TO_FILE = 1005;
    }

    public interface OnErrorListener {
        void onError(Recorder recorder, int i);
    }

    boolean canPause();

    int getMaxAmplitude();

    long getRecordingTimeInMillis();

    boolean isPaused();

    void pause();

    void prepare();

    void release();

    void reset();

    void resume();

    void setAudioChannel(int i);

    void setAudioEncoder(int i);

    void setAudioEncodingBitRate(int i);

    void setAudioSamplingRate(int i);

    void setAudioSource(int i);

    void setExtraParameters(String str);

    void setMaxDuration(int i);

    void setMaxFileSize(long j);

    void setOnErrorListener(OnErrorListener onErrorListener);

    void setOutputFile(String str);

    void setOutputFormat(int i);

    void setQuality(int i);

    void start();

    void stop();
}

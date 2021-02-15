package miui.media;

public class RecorderFactory {

    public interface AudioFormat {
        public static final int AAC = 1;
        public static final int AMR = 3;
        public static final int DEFAULT = 1;
        public static final int MP3 = 2;
        public static final int THREE_GPP = 4;
        public static final int WAV = 5;
    }

    private RecorderFactory() {
    }

    public static Recorder getRecorder(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? new LocalAACRecorder() : new LocalWavRecorder() : new LocalMediaRecorder() : new LocalAMRRecorder() : new LocalMp3Recorder() : new LocalAACRecorder();
    }
}

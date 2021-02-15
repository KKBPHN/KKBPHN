package miui.media;

import miui.media.Mp3Recorder.RecordingErrorListener;
import miui.media.Recorder.OnErrorListener;

class LocalMp3Recorder extends Mp3Recorder implements Recorder {
    /* access modifiers changed from: private */
    public OnErrorListener mOnErrorListener;

    LocalMp3Recorder() {
    }

    public boolean canPause() {
        return true;
    }

    public void setAudioEncoder(int i) {
    }

    public void setAudioEncodingBitRate(int i) {
        super.setOutBitRate(i);
    }

    public void setMaxDuration(int i) {
        super.setMaxDuration((long) i);
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
        super.setOnErrorListener(new RecordingErrorListener() {
            public void onError(Mp3Recorder mp3Recorder, int i) {
                LocalMp3Recorder.this.mOnErrorListener.onError(LocalMp3Recorder.this, RecorderUtils.convertErrorCode(i, false));
            }
        });
    }

    public void setOutputFormat(int i) {
    }
}

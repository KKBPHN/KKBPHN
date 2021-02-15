package miui.media;

class LocalAACRecorder extends LocalMediaRecorder {
    LocalAACRecorder() {
    }

    public boolean canPause() {
        return true;
    }

    public void setAudioSource(int i) {
        super.setAudioSource(i);
        setOutputFormat(6);
        setAudioEncoder(3);
    }
}

package miui.media;

class LocalAMRRecorder extends LocalMediaRecorder {
    private static final String AMR_NB_HEADER = "#!AMR\n";
    private static final String AMR_WB_HEADER = "#!AMR-WB\n";
    private boolean mIsAmrWB;

    LocalAMRRecorder() {
    }

    public boolean canPause() {
        return true;
    }

    /* access modifiers changed from: protected */
    public int getHeaderLen() {
        return this.mIsAmrWB ? 9 : 6;
    }

    public void release() {
        super.release();
        this.mIsAmrWB = false;
    }

    public void reset() {
        super.reset();
        this.mIsAmrWB = false;
    }

    public void setAudioEncoder(int i) {
        super.setAudioEncoder(i);
        if (i == 2) {
            this.mIsAmrWB = true;
        } else if (i == 1) {
            this.mIsAmrWB = false;
        }
    }
}

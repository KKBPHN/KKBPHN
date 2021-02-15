package com.xiaomi.asr.engine;

public class PhraseWakeupResult {
    private boolean isAec;
    private boolean isVBPassed;
    private boolean isVoconWakeupPassed;
    private PhraseWakeupResultDebugInfo mDebugInfo = new PhraseWakeupResultDebugInfo();
    private float mScore;
    private String mVoconPhrase;
    private long mWakeupEndTime;
    private long mWakeupStartTime;

    public final class PhraseWakeupResultDebugInfo {
        private byte[] mCacheCMVNData;
        private byte[] mCacheSVData;

        public byte[] getCacheCMVNData() {
            return this.mCacheCMVNData;
        }

        public byte[] getCacheSVData() {
            return this.mCacheSVData;
        }

        public void setCacheCMVNData(byte[] bArr) {
            this.mCacheCMVNData = bArr;
        }

        public void setCacheSVData(byte[] bArr) {
            this.mCacheSVData = bArr;
        }
    }

    public PhraseWakeupResultDebugInfo getDebugInfo() {
        return this.mDebugInfo;
    }

    public float getScore() {
        return this.mScore;
    }

    public String getVoconPhrase() {
        return this.mVoconPhrase;
    }

    public long getWakeupEndTime() {
        return this.mWakeupEndTime;
    }

    public long getWakeupStartTime() {
        return this.mWakeupStartTime;
    }

    public boolean isAec() {
        return this.isAec;
    }

    public boolean isVBPassed() {
        return this.isVBPassed;
    }

    public boolean isVoconWakeupPassed() {
        return this.isVoconWakeupPassed;
    }

    public void setAec(boolean z) {
        this.isAec = z;
    }

    public void setDebugInfo(PhraseWakeupResultDebugInfo phraseWakeupResultDebugInfo) {
        this.mDebugInfo = phraseWakeupResultDebugInfo;
    }

    public void setScore(float f) {
        this.mScore = f;
    }

    public void setVBPassed(boolean z) {
        this.isVBPassed = z;
    }

    public void setVoconPhrase(String str) {
        this.mVoconPhrase = str;
    }

    public void setVoconWakeupPassed(boolean z) {
        this.isVoconWakeupPassed = z;
    }

    public void setWakeupEndTime(long j) {
        this.mWakeupEndTime = j;
    }

    public void setWakeupStartTime(long j) {
        this.mWakeupStartTime = j;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PhraseWakeupResult{mVoconPhrase='");
        sb.append(this.mVoconPhrase);
        sb.append('\'');
        sb.append(", isVoconWakeupPassed=");
        sb.append(this.isVoconWakeupPassed);
        sb.append(", mWakeupStartTime=");
        sb.append(this.mWakeupStartTime);
        sb.append(", mWakeupEndTime=");
        sb.append(this.mWakeupEndTime);
        sb.append(", mScore=");
        sb.append(this.mScore);
        sb.append(", isAec=");
        sb.append(this.isAec);
        sb.append(", isVBPassed=");
        sb.append(this.isVBPassed);
        sb.append('}');
        return sb.toString();
    }
}

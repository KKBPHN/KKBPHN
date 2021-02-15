package com.xiaomi.engine;

public class ResultData {
    private static final String TAG = "ResultData";
    private int[] mCropRegion;
    private int mFlawResult;
    private int mResultId;
    private long mTimeStamp;

    public int[] getCropRegion() {
        return this.mCropRegion;
    }

    public int getFlawResult() {
        return this.mFlawResult;
    }

    public int getResultId() {
        return this.mResultId;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setCropRegion(int[] iArr) {
        this.mCropRegion = iArr;
    }

    public void setFlawResult(int i) {
        this.mFlawResult = i;
    }

    public void setResultId(int i) {
        this.mResultId = i;
    }

    public void setTimeStamp(long j) {
        this.mTimeStamp = j;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResultData{ mResultId=");
        sb.append(this.mResultId);
        sb.append(", mFlawResult=");
        sb.append(this.mFlawResult);
        sb.append('}');
        return sb.toString();
    }
}

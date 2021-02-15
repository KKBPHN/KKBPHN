package miuix.animation.utils;

import android.os.SystemClock;
import java.util.Arrays;
import java.util.LinkedList;

public class VelocityMonitor {
    private static final long MAX_DELTA = 100;
    private static final int MAX_RECORD_COUNT = 10;
    private static final long MIN_DELTA = 30;
    private static final long TIME_THRESHOLD = 50;
    private LinkedList mHistory = new LinkedList();
    private float[] mVelocity;

    class MoveRecord {
        long timeStamp;
        double[] values;

        private MoveRecord() {
        }
    }

    private void addAndUpdate(MoveRecord moveRecord) {
        this.mHistory.add(moveRecord);
        if (this.mHistory.size() > 10) {
            this.mHistory.remove(0);
        }
        updateVelocity();
    }

    private float calVelocity(int i, MoveRecord moveRecord, MoveRecord moveRecord2) {
        long j;
        MoveRecord moveRecord3;
        float f;
        MoveRecord moveRecord4 = moveRecord;
        MoveRecord moveRecord5 = moveRecord2;
        double d = moveRecord4.values[i];
        long j2 = moveRecord4.timeStamp;
        double velocity = (double) getVelocity(d, moveRecord5.values[i], j2 - moveRecord5.timeStamp);
        int size = this.mHistory.size() - 2;
        long j3 = 0;
        MoveRecord moveRecord6 = null;
        while (true) {
            if (size < 0) {
                j = j3;
                moveRecord3 = moveRecord6;
                f = Float.MAX_VALUE;
                break;
            }
            moveRecord3 = (MoveRecord) this.mHistory.get(size);
            long j4 = j2 - moveRecord3.timeStamp;
            if (j4 <= MIN_DELTA || j4 >= MAX_DELTA) {
                if (j4 > MAX_DELTA) {
                    this.mHistory.remove(size);
                }
                size--;
                moveRecord6 = moveRecord3;
                j3 = j4;
            } else {
                f = getVelocity(d, moveRecord3.values[i], j4);
                double d2 = (double) f;
                if (velocity * d2 > 0.0d) {
                    f = (float) (f > 0.0f ? Math.max(velocity, d2) : Math.min(velocity, d2));
                }
                j = j4;
            }
        }
        if (f != Float.MAX_VALUE || moveRecord3 == null) {
            return f;
        }
        return getVelocity(d, moveRecord3.values[i], j);
    }

    private void clearVelocity() {
        float[] fArr = this.mVelocity;
        if (fArr != null) {
            Arrays.fill(fArr, 0.0f);
        }
    }

    private MoveRecord getMoveRecord() {
        MoveRecord moveRecord = new MoveRecord();
        moveRecord.timeStamp = SystemClock.uptimeMillis();
        return moveRecord;
    }

    private float getVelocity(double d, double d2, long j) {
        return (float) (j == 0 ? 0.0d : (d - d2) / ((double) (((float) j) / 1000.0f)));
    }

    private void updateVelocity() {
        int size = this.mHistory.size();
        if (size >= 2) {
            MoveRecord moveRecord = (MoveRecord) this.mHistory.getLast();
            MoveRecord moveRecord2 = (MoveRecord) this.mHistory.get(size - 2);
            float[] fArr = this.mVelocity;
            if (fArr == null || fArr.length < moveRecord.values.length) {
                this.mVelocity = new float[moveRecord.values.length];
            }
            for (int i = 0; i < moveRecord.values.length; i++) {
                this.mVelocity[i] = calVelocity(i, moveRecord, moveRecord2);
            }
            return;
        }
        clearVelocity();
    }

    public void clear() {
        this.mHistory.clear();
        clearVelocity();
    }

    public float getVelocity(int i) {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (this.mHistory.size() > 0 && Math.abs(uptimeMillis - ((MoveRecord) this.mHistory.getLast()).timeStamp) > TIME_THRESHOLD) {
            return 0.0f;
        }
        float[] fArr = this.mVelocity;
        if (fArr == null || fArr.length <= i) {
            return 0.0f;
        }
        return fArr[i];
    }

    public void update(double... dArr) {
        if (dArr != null && dArr.length != 0) {
            MoveRecord moveRecord = getMoveRecord();
            moveRecord.values = dArr;
            addAndUpdate(moveRecord);
        }
    }

    public void update(float... fArr) {
        if (fArr != null && fArr.length != 0) {
            MoveRecord moveRecord = getMoveRecord();
            moveRecord.values = new double[fArr.length];
            for (int i = 0; i < fArr.length; i++) {
                moveRecord.values[i] = (double) fArr[i];
            }
            addAndUpdate(moveRecord);
        }
    }
}

package com.xiaomi.asr.engine.utils;

import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DSPWakeupInfoParser {
    private static Pattern DSP_WAKEUP_INFO_PATTERN = Pattern.compile("^FMT:(\\d+);VENDOR:NUANCE;START:(\\d+);FTT_LENGTH:(\\d+);SCORE:(\\d+);AEC:(\\d+);PCM_LENGTH:(\\d+)$");
    boolean mIsValideInfo = false;
    private boolean mMxeWakeupAec = false;
    private long mMxeWakeupEndTime = -1;
    private int mMxeWakeupSocre = -1;
    private long mMxeWakeupStartTime = -1;
    private int mPcmLength = -1;
    private long mWakeupRequiredLeadingSilence = -1;

    public int getPcmLength() {
        return this.mPcmLength;
    }

    public long getWakeupEndTime() {
        return this.mMxeWakeupEndTime;
    }

    public int getWakeupScore() {
        return this.mMxeWakeupSocre;
    }

    public long getWakeupStartTime() {
        return this.mMxeWakeupStartTime;
    }

    public boolean isInfoValid() {
        return this.mIsValideInfo;
    }

    public boolean isWakeupAec() {
        return this.mMxeWakeupAec;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00f1, code lost:
        r13 = r18;
        r1 = r20;
     */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00bb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parse(short[] sArr) {
        String str;
        int i;
        long j;
        long j2;
        int i2;
        int i3;
        StringBuilder sb;
        boolean z;
        int i4;
        short[] sArr2 = sArr;
        int length = sArr2.length;
        StringBuilder sb2 = new StringBuilder();
        int length2 = sArr2.length;
        int i5 = -1;
        int i6 = -1;
        long j3 = -1;
        long j4 = -1;
        int i7 = 0;
        boolean z2 = false;
        short s = 0;
        int i8 = 0;
        short s2 = 0;
        short s3 = 0;
        while (true) {
            str = "DSPWakeupInfoParser";
            int i9 = i5;
            if (i7 >= length2) {
                i = i6;
                break;
            }
            short s4 = sArr2[i7];
            byte b = (byte) s4;
            int i10 = length2;
            byte b2 = (byte) (s4 >> 8);
            i = i6;
            if (!z2) {
                if (b != 0) {
                    sb2.append((char) b);
                    if (b2 != 0) {
                        sb2.append((char) b2);
                        if (!z2) {
                            String sb3 = sb2.toString();
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("Dsp header is ");
                            sb4.append(sb3);
                            Log.i(str, sb4.toString());
                            Matcher matcher = DSP_WAKEUP_INFO_PATTERN.matcher(sb3);
                            if (!matcher.find()) {
                                Log.i(str, "format pattern not valid");
                                break;
                            }
                            int parseInt = Integer.parseInt(matcher.group(1));
                            j3 = Long.parseLong(matcher.group(2));
                            j4 = Long.parseLong(matcher.group(3));
                            i2 = Integer.parseInt(matcher.group(4));
                            sb = sb2;
                            int parseInt2 = Integer.parseInt(matcher.group(5));
                            boolean z3 = z2;
                            i3 = Integer.parseInt(matcher.group(6));
                            if (parseInt2 > 0) {
                                i4 = 1;
                                this.mMxeWakeupAec = true;
                                z = false;
                            } else {
                                i4 = 1;
                                z = false;
                                this.mMxeWakeupAec = false;
                            }
                            if (parseInt != i4) {
                                Log.i(str, "format version not valid");
                                break;
                            }
                            i9 = i2;
                            i6 = i3;
                            boolean z4 = z;
                            z2 = z3;
                        } else {
                            sb = sb2;
                            boolean z5 = z2;
                            i6 = i;
                        }
                    }
                }
                z2 = true;
                if (!z2) {
                }
            } else {
                sb = sb2;
                i6 = i;
            }
            if (i8 % 2 == 0) {
                s = s4;
            } else {
                short s5 = ((s << 16) & 0) | (-1 & s4);
                if (i8 < length - 2) {
                    s2 = s5 ^ s2;
                } else {
                    s3 = s5;
                }
            }
            i8++;
            i7++;
            sArr2 = sArr;
            i5 = i9;
            length2 = i10;
            sb2 = sb;
        }
        if (s3 == 0 || j2 <= 0 || j <= 0 || i2 < 0 || i3 <= 0) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("format check sum not valid verify ");
            sb5.append(s2);
            sb5.append(" checksum ");
            sb5.append(s3);
            Log.i(str, sb5.toString());
            return;
        }
        StringBuilder sb6 = new StringBuilder();
        sb6.append("format valid ");
        sb6.append(s3);
        Log.i(str, sb6.toString());
        this.mIsValideInfo = true;
        this.mMxeWakeupStartTime = j2;
        this.mMxeWakeupEndTime = this.mMxeWakeupStartTime + j;
        this.mMxeWakeupSocre = i2;
        this.mPcmLength = i3;
    }

    public DSPWakeupInfoParser reset() {
        this.mIsValideInfo = false;
        this.mMxeWakeupStartTime = -1;
        this.mMxeWakeupEndTime = -1;
        this.mMxeWakeupSocre = -1;
        this.mWakeupRequiredLeadingSilence = -1;
        this.mMxeWakeupAec = false;
        this.mPcmLength = -1;
        return this;
    }
}

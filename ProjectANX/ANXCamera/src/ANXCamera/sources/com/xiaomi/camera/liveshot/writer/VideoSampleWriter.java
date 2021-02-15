package com.xiaomi.camera.liveshot.writer;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaMuxer;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.xiaomi.camera.liveshot.LivePhotoResult;
import com.xiaomi.camera.liveshot.encoder.CircularMediaEncoder.Sample;
import com.xiaomi.camera.liveshot.encoder.CircularMediaEncoder.Snapshot;
import com.xiaomi.camera.liveshot.writer.SampleWriter.StatusNotifier;
import java.nio.ByteBuffer;

public class VideoSampleWriter extends SampleWriter {
    private static final boolean DEBUG = true;
    private static final long MIN_DURATION = 500000;
    private static final String TAG = "VideoSampleWriter";
    private final MediaMuxer mMediaMuxer;
    private final StatusNotifier mVideoFirstKeyFrameArrivedNotifier;
    private final Snapshot mVideoSnapshot;
    private final int mVideoTrackId;

    public VideoSampleWriter(MediaMuxer mediaMuxer, Snapshot snapshot, int i, StatusNotifier statusNotifier) {
        this.mMediaMuxer = mediaMuxer;
        this.mVideoSnapshot = snapshot;
        this.mVideoTrackId = i;
        this.mVideoFirstKeyFrameArrivedNotifier = statusNotifier;
    }

    /* access modifiers changed from: protected */
    public void writeSample() {
        boolean z;
        boolean z2;
        long j;
        long j2;
        long j3;
        boolean z3;
        boolean z4;
        BufferInfo bufferInfo;
        String str;
        StringBuilder sb;
        String str2;
        Log.d(TAG, "writeVideoSamples: E");
        Snapshot snapshot = this.mVideoSnapshot;
        long j4 = snapshot.head;
        long j5 = snapshot.tail;
        long j6 = snapshot.time;
        int i = snapshot.filterId;
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("writeVideoSamples: head timestamp: ");
        sb2.append(this.mVideoSnapshot.head);
        String str4 = ":";
        sb2.append(str4);
        sb2.append(j4);
        Log.d(str3, sb2.toString());
        String str5 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("writeVideoSamples: snap timestamp: ");
        sb3.append(this.mVideoSnapshot.time);
        sb3.append(str4);
        sb3.append(j6);
        Log.d(str5, sb3.toString());
        String str6 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("writeVideoSamples: tail timestamp: ");
        sb4.append(this.mVideoSnapshot.tail);
        sb4.append(str4);
        sb4.append(j5);
        Log.d(str6, sb4.toString());
        String str7 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("writeVideoSamples: curr filterId: ");
        sb5.append(this.mVideoSnapshot.filterId);
        sb5.append(str4);
        sb5.append(i);
        Log.d(str7, sb5.toString());
        long j7 = -1;
        boolean z5 = false;
        long j8 = 0;
        boolean z6 = false;
        boolean z7 = false;
        loop0:
        while (true) {
            boolean z8 = false;
            while (true) {
                if (z2) {
                    break loop0;
                }
                Log.d(TAG, "writeVideoSamples: take: E");
                try {
                    Sample sample = (Sample) this.mVideoSnapshot.samples.take();
                    Log.d(TAG, "writeVideoSamples: take: X");
                    if (sample == null) {
                        Log.e(TAG, "sample null return");
                        break loop0;
                    }
                    ByteBuffer byteBuffer = sample.data;
                    bufferInfo = sample.info;
                    LivePhotoResult livePhotoResult = sample.livePhotoResult;
                    z3 = z2;
                    String str8 = TAG;
                    j3 = j;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("writeVideoSamples: livePhotoResult ");
                    sb6.append(livePhotoResult);
                    Log.d(str8, sb6.toString());
                    if (byteBuffer.limit() == 0 || (bufferInfo.flags & 4) != 0) {
                        Log.d(TAG, "writeVideoSamples: EOF");
                    } else if (j6 - bufferInfo.presentationTimeUs >= MIN_DURATION && !z) {
                        boolean isLivePhotoStable = Util.isLivePhotoStable(livePhotoResult, i);
                        if (!z8) {
                            if (!isLivePhotoStable) {
                                str2 = TAG;
                                sb = new StringBuilder();
                                str = "writeVideoSamples: drop non-stable frame sample timestamp: ";
                                break;
                            }
                            String str9 = TAG;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("writeVideoSamples: drop first stable frame sample timestamp: ");
                            sb7.append(bufferInfo.presentationTimeUs);
                            Log.d(str9, sb7.toString());
                            z2 = z3;
                            j = j3;
                            z = false;
                            z8 = true;
                        } else if (!isLivePhotoStable) {
                            str2 = TAG;
                            sb = new StringBuilder();
                            str = "writeVideoSamples: drop second non-stable frame sample timestamp: ";
                            break;
                        } else {
                            String str10 = TAG;
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("writeVideoSamples: drop first and second stable frame sample timestamp: ");
                            sb8.append(bufferInfo.presentationTimeUs);
                            Log.d(str10, sb8.toString());
                            z2 = z3;
                            j = j3;
                            z = true;
                            z8 = true;
                        }
                    } else if ((bufferInfo.flags & 1) != 0 || z7) {
                        j2 = j6;
                        long j9 = bufferInfo.presentationTimeUs;
                        if (j9 >= j4 && j7 < j9 - j8) {
                            if (!z7) {
                                Snapshot snapshot2 = this.mVideoSnapshot;
                                snapshot2.offset = j9 - snapshot2.head;
                                StatusNotifier statusNotifier = this.mVideoFirstKeyFrameArrivedNotifier;
                                if (statusNotifier != null) {
                                    statusNotifier.notify(Long.valueOf(snapshot2.offset));
                                }
                                String str11 = TAG;
                                StringBuilder sb9 = new StringBuilder();
                                sb9.append("writeVideoSamples: first video sample timestamp: ");
                                sb9.append(j9);
                                Log.d(str11, sb9.toString());
                                z7 = true;
                            } else {
                                j9 = j8;
                            }
                            if (bufferInfo.presentationTimeUs >= j3) {
                                Log.d(TAG, "writeVideoSamples: stop writing as reaching the ending timestamp");
                                bufferInfo.flags = 4;
                            }
                            bufferInfo.presentationTimeUs -= j9;
                            this.mMediaMuxer.writeSampleData(this.mVideoTrackId, byteBuffer, bufferInfo);
                            long j10 = bufferInfo.presentationTimeUs;
                            String str12 = TAG;
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append("writeVideoSamples: video sample timestamp: ");
                            sb10.append(bufferInfo.presentationTimeUs + j9);
                            Log.d(str12, sb10.toString());
                            j8 = j9;
                            j7 = j10;
                        }
                        z4 = byteBuffer.limit() == 0 || (bufferInfo.flags & 4) != 0;
                        j = j3;
                        j6 = j2;
                    } else {
                        String str13 = TAG;
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("writeVideoSamples: drop non-key frame sample timestamp: ");
                        sb11.append(bufferInfo.presentationTimeUs);
                        Log.d(str13, sb11.toString());
                        j2 = j6;
                        z4 = z3;
                        j = j3;
                        j6 = j2;
                    }
                } catch (InterruptedException unused) {
                    j3 = j;
                    j2 = j6;
                    z3 = z2;
                    Log.d(TAG, "writeVideoSamples: take: meet interrupted exception");
                }
            }
            sb.append(str);
            sb.append(bufferInfo.presentationTimeUs);
            Log.d(str2, sb.toString());
            z5 = z3;
            j5 = j3;
            z6 = false;
        }
        Log.d(TAG, "writeVideoSamples: EOF");
        Snapshot snapshot3 = this.mVideoSnapshot;
        snapshot3.time = Math.max(0, snapshot3.time - j8);
        String str14 = TAG;
        StringBuilder sb12 = new StringBuilder();
        sb12.append("writeVideoSamples: cover frame timestamp: ");
        sb12.append(this.mVideoSnapshot.time);
        Log.d(str14, sb12.toString());
        String str15 = TAG;
        StringBuilder sb13 = new StringBuilder();
        sb13.append("writeVideoSamples: X: duration: ");
        sb13.append(j7);
        Log.d(str15, sb13.toString());
        String str16 = TAG;
        StringBuilder sb14 = new StringBuilder();
        sb14.append("writeVideoSamples: X: offset: ");
        sb14.append(this.mVideoSnapshot.offset);
        Log.d(str16, sb14.toString());
        this.mVideoSnapshot.clear();
    }
}

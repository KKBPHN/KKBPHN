package com.xiaomi.camera.liveshot.writer;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaMuxer;
import com.android.camera.log.Log;
import com.xiaomi.camera.liveshot.encoder.CircularMediaEncoder.Sample;
import com.xiaomi.camera.liveshot.encoder.CircularMediaEncoder.Snapshot;
import com.xiaomi.camera.liveshot.writer.SampleWriter.StatusNotifier;
import java.nio.ByteBuffer;

public class AudioSampleWriter extends SampleWriter {
    private static final boolean DEBUG = true;
    private static final String TAG = "AudioSampleWriter";
    private final Snapshot mAudioSnapshot;
    private final int mAudioTrackId;
    private final MediaMuxer mMediaMuxer;
    private final StatusNotifier mVideoFirstKeyFrameArrivedNotifier;

    public AudioSampleWriter(MediaMuxer mediaMuxer, Snapshot snapshot, int i, StatusNotifier statusNotifier) {
        this.mMediaMuxer = mediaMuxer;
        this.mAudioSnapshot = snapshot;
        this.mAudioTrackId = i;
        this.mVideoFirstKeyFrameArrivedNotifier = statusNotifier;
    }

    /* access modifiers changed from: protected */
    public void writeSample() {
        ByteBuffer byteBuffer;
        Log.d(TAG, "writeAudioSamples: E");
        StatusNotifier statusNotifier = this.mVideoFirstKeyFrameArrivedNotifier;
        long longValue = statusNotifier != null ? ((Long) statusNotifier.getStatus()).longValue() : 0;
        long j = this.mAudioSnapshot.head;
        if (longValue < 0) {
            longValue = 0;
        }
        long j2 = j + longValue;
        long j3 = this.mAudioSnapshot.tail;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("writeAudioSamples: head timestamp: ");
        sb.append(this.mAudioSnapshot.head);
        String str2 = ":";
        sb.append(str2);
        sb.append(j2);
        Log.d(str, sb.toString());
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("writeAudioSamples: tail timestamp: ");
        sb2.append(this.mAudioSnapshot.tail);
        sb2.append(str2);
        sb2.append(j3);
        Log.d(str3, sb2.toString());
        long j4 = -1;
        long j5 = 0;
        boolean z = false;
        boolean z2 = false;
        while (!z) {
            Log.d(TAG, "writeAudioSamples: take: E");
            try {
                Sample sample = (Sample) this.mAudioSnapshot.samples.take();
                Log.d(TAG, "writeAudioSamples: take: X");
                ByteBuffer byteBuffer2 = sample.data;
                BufferInfo bufferInfo = sample.info;
                long j6 = bufferInfo.presentationTimeUs;
                if (j6 < j2 || j4 >= j6 - j5) {
                    byteBuffer = byteBuffer2;
                } else {
                    if (!z2) {
                        Snapshot snapshot = this.mAudioSnapshot;
                        snapshot.offset = j6 - snapshot.head;
                        String str4 = TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("writeAudioSamples: first audio sample timestamp: ");
                        sb3.append(j6);
                        Log.d(str4, sb3.toString());
                        j5 = j6;
                        z2 = true;
                    }
                    if (bufferInfo.presentationTimeUs >= j3) {
                        Log.d(TAG, "writeAudioSamples: stop writing as reaching the ending timestamp");
                        bufferInfo.flags = 4;
                    }
                    bufferInfo.presentationTimeUs -= j5;
                    this.mMediaMuxer.writeSampleData(this.mAudioTrackId, byteBuffer2, bufferInfo);
                    j4 = bufferInfo.presentationTimeUs;
                    String str5 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("writeAudioSamples: audio sample timestamp: ");
                    byteBuffer = byteBuffer2;
                    sb4.append(bufferInfo.presentationTimeUs + j5);
                    Log.d(str5, sb4.toString());
                }
                z = byteBuffer.limit() == 0 || (bufferInfo.flags & 4) != 0;
            } catch (InterruptedException unused) {
                Log.d(TAG, "writeAudioSamples: take: meet interrupted exception");
            }
        }
        String str6 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("writeAudioSamples: X: duration: ");
        sb5.append(j4);
        Log.d(str6, sb5.toString());
        String str7 = TAG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("writeAudioSamples: X: offset: ");
        sb6.append(this.mAudioSnapshot.offset);
        Log.d(str7, sb6.toString());
        this.mAudioSnapshot.clear();
    }
}

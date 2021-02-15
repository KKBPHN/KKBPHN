package com.miui.extravideo.common;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaMuxer;
import java.nio.ByteBuffer;
import tv.danmaku.ijk.media.player.misc.IMediaFormat;

public class MediaUtils {
    private static void addVideoToTrack(MediaExtractor mediaExtractor, MediaMuxer mediaMuxer, int i) {
        ByteBuffer allocate = ByteBuffer.allocate(mediaExtractor.getTrackFormat(mediaExtractor.getSampleTrackIndex()).getInteger("max-input-size"));
        allocate.position(0);
        BufferInfo bufferInfo = new BufferInfo();
        while (true) {
            readBufferByExtractor(allocate, bufferInfo, mediaExtractor);
            if (bufferInfo.size > 0) {
                allocate.position(0);
                mediaMuxer.writeSampleData(i, allocate, bufferInfo);
                if ((bufferInfo.flags & 4) == 0) {
                    mediaExtractor.advance();
                    allocate.position(0);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public static long computePresentationTime(int i, int i2) {
        return (long) (((i * 1000000) / i2) + 132);
    }

    private static MediaExtractor generateExtractor(String str) {
        MediaExtractor mediaExtractor = new MediaExtractor();
        mediaExtractor.setDataSource(str);
        int i = 0;
        while (true) {
            if (i >= mediaExtractor.getTrackCount()) {
                break;
            } else if (mediaExtractor.getTrackFormat(i).getString(IMediaFormat.KEY_MIME).startsWith("video/")) {
                mediaExtractor.selectTrack(i);
                break;
            } else {
                i++;
            }
        }
        return mediaExtractor;
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r1v0, types: [android.media.MediaMuxer] */
    /* JADX WARNING: type inference failed for: r0v1, types: [android.media.MediaExtractor] */
    /* JADX WARNING: type inference failed for: r5v1, types: [android.media.MediaExtractor] */
    /* JADX WARNING: type inference failed for: r0v2, types: [android.media.MediaMuxer] */
    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r1v3, types: [android.media.MediaMuxer] */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r0v7, types: [android.media.MediaExtractor] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r0v9 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r5v4
  assigns: []
  uses: []
  mth insns count: 70
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0082  */
    /* JADX WARNING: Unknown variable types count: 6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int mixVideo(String str, String str2, String str3, int i) {
        MediaExtractor mediaExtractor;
        ? r1;
        ? r5;
        ? r0;
        ? r52;
        ? r02;
        ? r03 = 0;
        try {
            r1 = new MediaMuxer(str, 0);
            try {
                r1.setOrientationHint(i);
                mediaExtractor = generateExtractor(str2);
            } catch (Exception e) {
                e = e;
                mediaExtractor = null;
                r52 = 0;
                r0 = r1;
                r5 = r52;
                try {
                    e.printStackTrace();
                    if (r0 != 0) {
                        r0.stop();
                        r0.release();
                    }
                    if (mediaExtractor != null) {
                        mediaExtractor.release();
                    }
                    if (r5 != 0) {
                        r5.release();
                    }
                    return -1;
                } catch (Throwable th) {
                    th = th;
                    r1 = r0;
                    r03 = r5;
                    if (r1 != 0) {
                        r1.stop();
                        r1.release();
                    }
                    if (mediaExtractor != null) {
                        mediaExtractor.release();
                    }
                    if (r03 != 0) {
                        r03.release();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                mediaExtractor = null;
                r1 = r1;
                if (r1 != 0) {
                }
                if (mediaExtractor != null) {
                }
                if (r03 != 0) {
                }
                throw th;
            }
            try {
                r02 = r03;
                r03 = generateExtractor(str3);
                int addTrack = r1.addTrack(mediaExtractor.getTrackFormat(mediaExtractor.getSampleTrackIndex()));
                int addTrack2 = r1.addTrack(r03.getTrackFormat(r03.getSampleTrackIndex()));
                r1.start();
                addVideoToTrack(mediaExtractor, r1, addTrack);
                addVideoToTrack(r03, r1, addTrack2);
                r02 = r03;
                r1.stop();
                r1.release();
                if (mediaExtractor != null) {
                    mediaExtractor.release();
                }
                if (r03 == 0) {
                    return addTrack2;
                }
                r03.release();
                return addTrack2;
            } catch (Exception e2) {
                e = e2;
                r52 = r03;
                r0 = r1;
                r5 = r52;
                e.printStackTrace();
                if (r0 != 0) {
                }
                if (mediaExtractor != null) {
                }
                if (r5 != 0) {
                }
                return -1;
            } catch (Throwable th3) {
                th = th3;
                r03 = r02;
                if (r1 != 0) {
                }
                if (mediaExtractor != null) {
                }
                if (r03 != 0) {
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            mediaExtractor = null;
            r5 = 0;
            e.printStackTrace();
            if (r0 != 0) {
            }
            if (mediaExtractor != null) {
            }
            if (r5 != 0) {
            }
            return -1;
        } catch (Throwable th4) {
            th = th4;
            mediaExtractor = null;
            r1 = 0;
            if (r1 != 0) {
            }
            if (mediaExtractor != null) {
            }
            if (r03 != 0) {
            }
            throw th;
        }
    }

    private static void readBufferByExtractor(ByteBuffer byteBuffer, BufferInfo bufferInfo, MediaExtractor mediaExtractor) {
        bufferInfo.size = mediaExtractor.readSampleData(byteBuffer, 0);
        bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
        bufferInfo.flags = mediaExtractor.getSampleFlags();
        bufferInfo.offset = 0;
    }
}

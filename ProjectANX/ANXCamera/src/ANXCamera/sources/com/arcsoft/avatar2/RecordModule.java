package com.arcsoft.avatar2;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.storage.Storage;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarProcessInfo;
import com.arcsoft.avatar2.extrascene.ExtraSceneEngine;
import com.arcsoft.avatar2.extrascene.ExtraSceneEngine.AvatarExtraSceneTriggerCallback;
import com.arcsoft.avatar2.gl.GLFramebuffer;
import com.arcsoft.avatar2.gl.GLRenderEngine;
import com.arcsoft.avatar2.gl.TextureHelper;
import com.arcsoft.avatar2.recoder.MediaManager;
import com.arcsoft.avatar2.recoder.RecordingListener;
import com.arcsoft.avatar2.util.ASVLOFFSCREEN;
import com.arcsoft.avatar2.util.AsvloffscreenUtil;
import com.arcsoft.avatar2.util.LOG;
import com.arcsoft.avatar2.util.TimeConsumingUtil;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import miui.text.ExtraTextUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class RecordModule {
    private static final String a = "RecordModule";
    private static final int ae = 50;
    private static final String ag = "arcsoft_enable_log";
    private static final String ah = "arcsoft_performance";
    private static final String ai = "arcsoft_version";
    private static final String al = "Arcsoft_AvatarJar_V.2.0.0.12";
    private static final int b = 1;
    private static final int c = 512000;
    private boolean A;
    private boolean B;
    private int C = 270;
    private long D = 0;
    private final int E = 1000000;
    private long F = 0;
    private long G = 0;
    private AvatarEngine H;
    private MediaManager I;
    private volatile boolean J;
    private volatile boolean K;
    private boolean L;
    private RecordingListener M = null;
    private ASAvatarProcessInfo N = null;
    private MediaResultCallback O = null;
    private boolean P = false;
    private boolean[] Q = new boolean[3];
    private int R = 0;
    private Boolean S;
    private Bitmap T;
    private ASVLOFFSCREEN U;
    private ASVLOFFSCREEN V;
    private volatile boolean W = true;
    private volatile boolean X = false;
    private volatile boolean Y = false;
    private volatile boolean Z = false;
    private int aa = 0;
    private int ab = 0;
    private int ac = 0;
    private long ad = 0;
    private boolean af = false;
    private ExtraSceneEngine aj = null;
    private volatile boolean ak = false;
    private float[] am = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    private Context d;
    private Lock e = new ReentrantLock();
    private Lock f = new ReentrantLock();
    private Lock g = new ReentrantLock();
    private volatile boolean h = false;
    private volatile boolean i = false;
    private volatile boolean j = false;
    private volatile boolean k = false;
    private boolean l;
    private EGLDisplay m;
    private EGLContext n;
    private EGLSurface o;
    private int p;
    private int q;
    private int r;
    private int s;
    private int t;
    private int u;
    private Queue v;
    private GLRenderEngine w;
    private GLRenderEngine x;
    private GLFramebuffer y;
    private TextureHelper z;

    public interface MediaResultCallback {
        void onCaptureResult(ByteBuffer byteBuffer);

        void onVideoResult(boolean z);
    }

    public RecordModule(Context context, MediaResultCallback mediaResultCallback) {
        this.d = context;
        this.O = mediaResultCallback;
    }

    private long a(long j2) {
        if (j2 <= 0) {
            return 0;
        }
        long j3 = j2 - 512000;
        if (j3 <= 0) {
            j3 = 1;
        }
        return j3;
    }

    private String a(String str, String str2) {
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls, new Object[]{str, str2});
        } catch (Exception e2) {
            e2.printStackTrace();
            return str2;
        }
    }

    private void a() {
        long muxerTimeElapsed = this.I.getMuxerTimeElapsed();
        long muxerSizeRecorded = this.I.getMuxerSizeRecorded();
        long a2 = a(this.D);
        long j2 = muxerTimeElapsed / ExtraTextUtils.MB;
        if (j2 > this.F) {
            this.F = j2;
            RecordingListener recordingListener = this.M;
            if (recordingListener != null) {
                recordingListener.onRecordingListener(258, Long.valueOf(muxerTimeElapsed));
                this.M.onRecordingListener(260, Long.valueOf(muxerSizeRecorded));
            }
        }
        long j3 = this.G;
        if (j3 > 0 && muxerTimeElapsed > j3) {
            stopRecording();
            RecordingListener recordingListener2 = this.M;
            if (recordingListener2 != null) {
                recordingListener2.onRecordingListener(257, Long.valueOf(muxerTimeElapsed));
            }
        }
        if (a2 > 0 && muxerSizeRecorded > a2) {
            stopRecording();
            RecordingListener recordingListener3 = this.M;
            if (recordingListener3 != null) {
                recordingListener3.onRecordingListener(259, Long.valueOf(muxerSizeRecorded));
            }
        }
    }

    private void a(int i2) {
        if (i2 <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("recordingIfNeed textureId = ");
            sb.append(i2);
            LOG.d(a, sb.toString());
        } else if (!this.J && this.I != null && this.L && (!this.af || !c())) {
            this.I.drawSurfaceWithTextureId(i2);
            a();
        }
    }

    private void a(int i2, int i3, int i4, int i5, boolean z2) {
        if ((!this.K || !z2 || !(i3 == i4 || i3 == i5)) && this.K && i2 > 0) {
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.r * this.s * 4);
            allocateDirect.order(ByteOrder.nativeOrder());
            GLES20.glReadPixels(this.t, this.u, this.r, this.s, 6408, 5121, allocateDirect);
            this.K = false;
            MediaResultCallback mediaResultCallback = this.O;
            if (mediaResultCallback != null) {
                mediaResultCallback.onCaptureResult(allocateDirect);
            }
        }
    }

    private void a(ASVLOFFSCREEN asvloffscreen) {
        if (this.v.size() >= 1) {
            LOG.d("CKK", "mPreviewQueue.poll()");
            this.v.poll();
        }
        try {
            this.v.offer(asvloffscreen);
        } catch (ClassCastException e2) {
            e2.printStackTrace();
        } catch (NullPointerException e3) {
            e3.printStackTrace();
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
        }
    }

    private void a(boolean z2) {
        int i2 = 0;
        int i3 = z2 ? this.t : 0;
        if (z2) {
            i2 = this.u;
        }
        GLES20.glViewport(i3, i2, this.r, this.s);
    }

    private byte[] a(byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        String str = "cropNV21";
        TimeConsumingUtil.startTheTimer(str);
        if (i4 > i2 || i5 > i3) {
            return null;
        }
        int i8 = (i4 / 2) * 2;
        int i9 = (i5 / 2) * 2;
        int i10 = (i6 / 2) * 2;
        int i11 = (i7 / 2) * 2;
        int i12 = i10 * i11;
        byte[] bArr2 = new byte[((i12 * 3) >> 1)];
        int i13 = i9 * i2;
        int i14 = 0;
        int i15 = i12 - ((i9 / 2) * i10);
        int i16 = (i3 * i2) + i8;
        for (int i17 = i9; i17 < i9 + i11; i17++) {
            System.arraycopy(bArr, i13 + i8, bArr2, i14, i10);
            i13 += i2;
            i14 += i10;
            if ((i17 & 1) == 0) {
                System.arraycopy(bArr, i16, bArr2, i15, i10);
                i16 += i2;
                i15 += i10;
            }
        }
        TimeConsumingUtil.stopTiming("performance", str);
        return bArr2;
    }

    private void b(ASVLOFFSCREEN asvloffscreen) {
        if (asvloffscreen.getPixelFormat() == 2050) {
            int width = asvloffscreen.getWidth();
            int height = asvloffscreen.getHeight();
            asvloffscreen.setYData(a(asvloffscreen.getYData(), asvloffscreen.getRowStride()[0], height, (width - height) / 2, 0, height, height));
            asvloffscreen.setWidth(asvloffscreen.getHeight());
            asvloffscreen.setRowStride(asvloffscreen.getHeight());
        } else if (asvloffscreen.getPixelFormat() != 773) {
        } else {
            if (asvloffscreen.getWidth() > asvloffscreen.getHeight()) {
                asvloffscreen.setRGBA8888(b(asvloffscreen.getRGBA8888(), asvloffscreen.getWidth(), asvloffscreen.getHeight(), (asvloffscreen.getWidth() - asvloffscreen.getHeight()) / 2, 0, asvloffscreen.getHeight(), asvloffscreen.getHeight()));
                asvloffscreen.setWidth(asvloffscreen.getHeight());
                return;
            }
            asvloffscreen.setRGBA8888(b(asvloffscreen.getRGBA8888(), asvloffscreen.getWidth(), asvloffscreen.getHeight(), 0, (asvloffscreen.getHeight() - asvloffscreen.getWidth()) / 2, asvloffscreen.getWidth(), asvloffscreen.getWidth()));
            asvloffscreen.setHeight(asvloffscreen.getWidth());
        }
    }

    private boolean b() {
        String str;
        ASAvatarProcessInfo aSAvatarProcessInfo = this.N;
        String str2 = "CheckOutLine";
        if (aSAvatarProcessInfo == null) {
            str = "null";
        } else if (aSAvatarProcessInfo.shelterIsNull()) {
            str = "shelterFlags == null";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("faceCount = ");
            sb.append(this.N.getFaceCount());
            LOG.d(str2, sb.toString());
            if (this.N.getFaceCount() > 0) {
                boolean checkFaceBlocking = this.N.checkFaceBlocking();
                if (this.R > 2) {
                    this.R = 0;
                }
                boolean[] zArr = this.Q;
                int i2 = this.R;
                zArr[i2] = checkFaceBlocking;
                this.R = i2 + 1;
                if (!zArr[0] || !zArr[1] || !zArr[2]) {
                    boolean[] zArr2 = this.Q;
                    if (!zArr2[0] && !zArr2[1] && !zArr2[2]) {
                        this.P = false;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("--- > mBlockingFaces <---");
                    sb2.append(this.P);
                    LOG.d(str2, sb2.toString());
                    return this.P;
                }
            }
            this.P = true;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("--- > mBlockingFaces <---");
            sb22.append(this.P);
            LOG.d(str2, sb22.toString());
            return this.P;
        }
        LOG.d(str2, str);
        this.P = true;
        StringBuilder sb222 = new StringBuilder();
        sb222.append("--- > mBlockingFaces <---");
        sb222.append(this.P);
        LOG.d(str2, sb222.toString());
        return this.P;
    }

    private byte[] b(byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        String str = "cropRGBA";
        TimeConsumingUtil.startTheTimer(str);
        if (i4 > i2 || i5 > i3) {
            return null;
        }
        int i8 = (i4 / 2) * 2;
        int i9 = (i5 / 2) * 2;
        int i10 = (i6 / 2) * 2;
        int i11 = (i7 / 2) * 2;
        int i12 = i10 * i11 * 4;
        byte[] bArr2 = new byte[i12];
        int i13 = i9 * i2 * 4;
        int i14 = 0;
        if (i2 > i3) {
            for (int i15 = i9; i15 < i9 + i11; i15++) {
                int i16 = i10 * 4;
                System.arraycopy(bArr, (i8 * 4) + i13, bArr2, i14, i16);
                i13 += i2 * 4;
                i14 += i16;
            }
        } else {
            System.arraycopy(bArr, i13 + (i8 * 4), bArr2, 0, i12);
        }
        TimeConsumingUtil.stopTiming("performance", str);
        return bArr2;
    }

    private boolean c() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.ad == 0) {
            this.ad = currentTimeMillis;
        }
        long j2 = (long) (this.ac * 50);
        long j3 = currentTimeMillis - this.ad;
        StringBuilder sb = new StringBuilder();
        sb.append("  mFrameCount = ");
        sb.append(this.ac);
        sb.append("time = ");
        sb.append(j2);
        sb.append(" - ");
        sb.append(j3);
        LOG.d(a, sb.toString());
        this.ac++;
        if (j2 - j3 < 50) {
            return false;
        }
        this.ac = 1;
        this.ad = currentTimeMillis;
        return true;
    }

    private void d() {
        StringBuilder sb = new StringBuilder();
        sb.append("setBmpRenderScene -> path = ");
        sb.append(this.H.getTemplatePath());
        LOG.d(a, sb.toString());
        if (!this.Z || !this.H.getTemplatePath().contains("cartoon_xiaomi")) {
            this.H.setRenderScene(false, 1.0f);
            return;
        }
        float[] fArr = new float[3];
        fArr[1] = -7.0f;
        this.H.setRenderScene3F(false, 1.0f, fArr);
    }

    private float[] e() {
        if (!this.Z) {
            return null;
        }
        float width = ((((float) (this.U.getWidth() - this.U.getHeight())) * 1.0f) / 2.0f) / ((float) this.U.getWidth());
        float[] fArr = this.am;
        fArr[5] = width;
        fArr[1] = width;
        float f2 = 1.0f - width;
        fArr[7] = f2;
        fArr[3] = f2;
        return fArr;
    }

    public void capture() {
        try {
            this.e.lock();
            if (this.h) {
                this.e.unlock();
                this.K = true;
            }
        } finally {
            this.e.unlock();
        }
    }

    public void changeHumanTemplate(String str, String str2) {
        this.X = true;
        this.H.setTemplatePath(str);
        this.H.loadConfig(str2);
        this.X = false;
    }

    public AvatarEngine getAvatarEngine() {
        return this.H;
    }

    public ArrayList getBackgroundBmpInfo(String str) {
        String str2;
        String str3 = a;
        String str4 = "/";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("getBgXmlInfo = ");
            sb.append(str);
            LOG.d(str3, sb.toString());
            ArrayList arrayList = new ArrayList();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(str4);
            sb2.append(BackgroundInfo.getXMLName());
            FileInputStream fileInputStream = new FileInputStream(sb2.toString());
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(fileInputStream, "UTF-8");
            String str5 = null;
            BackgroundInfo backgroundInfo = null;
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType != 0) {
                    if (eventType == 2) {
                        str2 = newPullParser.getName();
                        if (str2.equals(BackgroundInfo.getXMLInfoTag())) {
                            backgroundInfo = new BackgroundInfo();
                        }
                    } else if (eventType == 3) {
                        if (newPullParser.getName().equals(BackgroundInfo.getXMLInfoTag())) {
                            ArrayList arrayList2 = new ArrayList();
                            for (int i2 = 0; i2 < backgroundInfo.getCount(); i2++) {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str);
                                sb3.append(str4);
                                sb3.append(backgroundInfo.getName());
                                sb3.append(str4);
                                sb3.append(backgroundInfo.getName());
                                sb3.append("_");
                                sb3.append(i2);
                                sb3.append(Storage.JPEG_SUFFIX);
                                String sb4 = sb3.toString();
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("bmpPath = ");
                                sb5.append(sb4);
                                LOG.d(str3, sb5.toString());
                                arrayList2.add(sb4);
                            }
                            backgroundInfo.setResolution_FullSize_PathList(arrayList2);
                            arrayList.add(backgroundInfo);
                        }
                        str2 = "";
                    } else if (eventType == 4) {
                        if (str5.equals(BackgroundInfo.getXMLNameTag())) {
                            backgroundInfo.setName(newPullParser.getText());
                        } else if (str5.equals(BackgroundInfo.getXMLCountTag())) {
                            backgroundInfo.setCount(Integer.parseInt(newPullParser.getText()));
                        } else if (str5.equals(BackgroundInfo.getXMLDelayTag())) {
                            backgroundInfo.setDelayMillis(Integer.parseInt(newPullParser.getText()));
                        } else if (str5.equals(BackgroundInfo.getXMLCrop4_3_XY())) {
                            backgroundInfo.setCrop4_3_XY(newPullParser.getText());
                        } else if (str5.equals(BackgroundInfo.getXMLCrop16_9_XY())) {
                            backgroundInfo.setCrop16_9_XY(newPullParser.getText());
                        }
                    }
                    str5 = str2;
                }
            }
            return arrayList;
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
            return null;
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            return null;
        } catch (IOException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public ArrayList getPreHumanConfigInfo(String str) {
        String str2;
        String str3 = "/";
        String str4 = a;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("getPreXmlInfo = ");
            sb.append(str);
            LOG.d(str4, sb.toString());
            ArrayList arrayList = new ArrayList();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(str3);
            sb2.append("preconfig.xml");
            FileInputStream fileInputStream = new FileInputStream(sb2.toString());
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(fileInputStream, "UTF-8");
            String str5 = null;
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType != 0) {
                    if (eventType == 2) {
                        str2 = newPullParser.getName();
                    } else if (eventType == 3) {
                        str2 = "";
                    } else if (eventType == 4) {
                        if (str5.equals("name")) {
                            PreHumanConfigInfo preHumanConfigInfo = new PreHumanConfigInfo();
                            preHumanConfigInfo.setPreHumanName(newPullParser.getText());
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append(str3);
                            sb3.append(newPullParser.getText());
                            sb3.append(FileUtils.FILTER_FILE_SUFFIX);
                            preHumanConfigInfo.setPreHumanThumbnailPath(sb3.toString());
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str);
                            sb4.append(str3);
                            sb4.append(newPullParser.getText());
                            sb4.append(".dat");
                            preHumanConfigInfo.setPreHumanConfigPath(sb4.toString());
                            arrayList.add(preHumanConfigInfo);
                        }
                    }
                    str5 = str2;
                }
            }
            return arrayList;
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
            return null;
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            return null;
        } catch (IOException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public void init(int i2, int i3, int i4, AvatarEngine avatarEngine, boolean z2) {
        String str = "0";
        boolean z3 = false;
        this.h = false;
        this.i = false;
        this.j = false;
        this.k = false;
        this.A = false;
        this.L = false;
        this.J = false;
        this.p = i3;
        this.q = i4;
        this.H = avatarEngine;
        this.v = new LinkedList();
        this.v.clear();
        this.B = z2;
        this.C = i2;
        this.l = false;
        this.m = EGL14.EGL_NO_DISPLAY;
        this.n = EGL14.EGL_NO_CONTEXT;
        this.o = EGL14.EGL_NO_SURFACE;
        this.N = new ASAvatarProcessInfo();
        this.h = true;
        try {
            LOG.DEBUG = Integer.parseInt(a(ag, str)) == 1;
            if (Integer.parseInt(a(ah, str)) == 1) {
                z3 = true;
            }
            TimeConsumingUtil.DEBUG = z3;
            if (Integer.parseInt(a(ai, str)) == 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("Arcsoft_AvatarJar_V.2.0.0.12\n");
                sb.append(this.H.getVersion());
                Log.d("avatar_version", sb.toString());
            }
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
        }
    }

    public boolean isRequestPause() {
        return this.J;
    }

    public void pause() {
        this.U = null;
    }

    public void pauseRecording() {
        try {
            this.e.lock();
            if (this.h) {
                this.e.unlock();
                MediaManager mediaManager = this.I;
                if (mediaManager != null && this.L) {
                    this.J = true;
                    mediaManager.pauseRecording();
                }
            }
        } finally {
            this.e.unlock();
        }
    }

    public void releaseRecordGLInfo() {
        if (this.I != null) {
            LOG.d("stopRecording", "-- releaseFrameQueue --");
            this.I.releaseFrameQueue();
            this.I = null;
        }
    }

    public void reset() {
        this.W = true;
    }

    public void resetExtraScene() {
        ExtraSceneEngine extraSceneEngine = this.aj;
        if (extraSceneEngine != null) {
            extraSceneEngine.resetExtraScene();
        }
    }

    public void resumeRecording() {
        try {
            this.e.lock();
            if (this.h) {
                this.e.unlock();
                if (this.I != null && this.L && this.J) {
                    this.I.resumeRecording();
                    this.J = false;
                }
            }
        } finally {
            this.e.unlock();
        }
    }

    public void setAvatarEngine(AvatarEngine avatarEngine) {
        this.H = avatarEngine;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0051 A[Catch:{ all -> 0x00d6 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setBackground(Bitmap bitmap, BackgroundInfo backgroundInfo) {
        int i2;
        int i3;
        int i4;
        int i5;
        String str = "setBackground";
        if (bitmap != null) {
            try {
                this.g.lock();
                this.V = AsvloffscreenUtil.buildRGBA(bitmap);
                TimeConsumingUtil.startTheTimer(str);
                int resolutionMode = backgroundInfo.getResolutionMode();
                int width = this.V.getWidth();
                if (resolutionMode == 2) {
                    i5 = (((width * 16) / 9) / 2) * 2;
                    i4 = backgroundInfo.getCrop16_9_XY()[0];
                    i3 = backgroundInfo.getCrop16_9_XY()[1];
                } else if (resolutionMode == 1) {
                    i5 = (((width * 4) / 3) / 2) * 2;
                    i4 = backgroundInfo.getCrop4_3_XY()[0];
                    i3 = backgroundInfo.getCrop4_3_XY()[1];
                } else {
                    i4 = 0;
                    i3 = 0;
                    i2 = 0;
                    if (resolutionMode != 3) {
                        int i6 = i4 + width > this.V.getWidth() ? 0 : i4;
                        int i7 = i3 + i2 > this.V.getHeight() ? 0 : i3;
                        byte[] b2 = b(this.V.getRGBA8888(), this.V.getWidth(), this.V.getHeight(), i6, i7, width, i2);
                        this.V.setHeight(i2);
                        this.V.setRGBA8888(b2);
                        StringBuilder sb = new StringBuilder();
                        sb.append("mode = ");
                        sb.append(resolutionMode);
                        sb.append(", cropX = ");
                        sb.append(i6);
                        sb.append(", cropY = ");
                        sb.append(i7);
                        sb.append(", w = ");
                        sb.append(width);
                        sb.append(", h = ");
                        sb.append(i2);
                        LOG.d(str, sb.toString());
                    }
                    d();
                    TimeConsumingUtil.stopTiming("zhangs0997", str);
                }
                i2 = i5;
                if (resolutionMode != 3) {
                }
                d();
                TimeConsumingUtil.stopTiming("zhangs0997", str);
            } finally {
                this.g.unlock();
                this.Y = true;
            }
        } else {
            this.Y = false;
        }
    }

    public void setBackgroundToSquare(boolean z2) {
        this.Z = z2;
        this.af = z2;
    }

    public void setDrawScope(int i2, int i3, int i4, int i5) {
        this.t = i2;
        this.u = i3;
        this.r = i4;
        this.s = i5;
    }

    public void setExtraSceneTemplatePath(String str, AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback) {
        if (!TextUtils.isEmpty(str)) {
            this.aj = new ExtraSceneEngine(str, avatarExtraSceneTriggerCallback);
        }
    }

    public void setMirror(boolean z2) {
        this.B = z2;
    }

    public void setPreviewSize(int i2, int i3) {
        this.p = i2;
        this.q = i3;
        this.W = true;
    }

    public void setmImageOrientation(int i2) {
        this.C = i2;
    }

    public boolean startProcess(@NonNull ASVLOFFSCREEN asvloffscreen, int i2, boolean z2) {
        String str = "avatarProcessWithInfo";
        String str2 = "lock -> process unlock";
        try {
            this.e.lock();
            boolean z3 = this.h;
            String str3 = a;
            if (!z3) {
                LOG.d(str3, "startProcess_1() failed, engine is not inited. ");
                return true;
            }
            this.e.unlock();
            if (asvloffscreen == null) {
                return true;
            }
            try {
                this.j = true;
                LOG.d(str3, "lock -> process lock");
                this.f.lock();
                this.U = asvloffscreen;
                this.f.unlock();
                LOG.d(str3, str2);
                this.i = true;
                if (!z2) {
                    this.j = false;
                    return true;
                }
                TimeConsumingUtil.startTheTimer(str);
                if (this.H.nativeProcessWithInfoToPreview(asvloffscreen, 90, this.B, i2, this.N, this.Y)) {
                    if (this.aj != null) {
                        this.aj.checkExtraScene(this.N);
                    }
                    this.H.setProcessInfo(this.N);
                }
                TimeConsumingUtil.stopTiming("performance", str);
                this.W = this.Y ? false : b();
                this.j = false;
                return this.W;
            } catch (Exception e2) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("e -> ");
                    sb.append(e2.toString());
                    LOG.d(str3, sb.toString());
                    e2.printStackTrace();
                } catch (Throwable th) {
                    this.j = false;
                    throw th;
                }
            } catch (Throwable th2) {
                this.f.unlock();
                LOG.d(str3, str2);
                throw th2;
            }
        } finally {
            this.e.unlock();
        }
    }

    public boolean startProcess(@NonNull byte[] bArr, int i2, int i3, int i4, boolean z2) {
        byte[] bArr2 = bArr;
        int i5 = i3;
        int i6 = i4;
        String str = "avatarProcessWithInfo";
        String str2 = "lock -> process unlock";
        try {
            this.e.lock();
            boolean z3 = this.h;
            String str3 = a;
            if (!z3) {
                LOG.d(str3, "startProcess_1() failed, engine is not inited. ");
                return true;
            }
            this.e.unlock();
            if (bArr2 == null || bArr2.length <= 0 || i5 <= 0 || i6 <= 0) {
                return true;
            }
            try {
                this.j = true;
                LOG.d(str3, "lock -> process lock");
                this.f.lock();
                this.U = new ASVLOFFSCREEN(bArr, i5, i5, i6);
                this.f.unlock();
                LOG.d(str3, str2);
                this.i = true;
                if (!z2) {
                    this.j = false;
                    return true;
                }
                TimeConsumingUtil.startTheTimer(str);
                this.H.avatarProcessWithInfo(bArr, i3, i4, i3, 90, this.B, i2, this.N);
                TimeConsumingUtil.stopTiming("performance", str);
                this.W = b();
                this.j = false;
                return this.W;
            } catch (Exception e2) {
                try {
                    e2.printStackTrace();
                } catch (Throwable th) {
                    this.j = false;
                    throw th;
                }
            } catch (Throwable th2) {
                this.f.unlock();
                LOG.d(str3, str2);
                throw th2;
            }
        } finally {
            this.e.unlock();
        }
    }

    public boolean startRecording(@NonNull FileDescriptor fileDescriptor, RecordingListener recordingListener, int i2, @NonNull int i3, @NonNull int i4, int i5, String str) {
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        try {
            this.e.lock();
            if (!this.h) {
                LOG.d(a, "startRecording-> StickerApi is not init.");
                return false;
            }
            this.e.unlock();
            if (i7 == 0 || i8 == 0 || fileDescriptor == null || this.I != null) {
                return false;
            }
            if (i6 != 0 && 90 != i6 && 180 != i6 && 270 != i6) {
                return false;
            }
            this.ak = false;
            this.M = recordingListener;
            if (EGL14.EGL_NO_CONTEXT == this.n) {
                this.n = EGL14.eglGetCurrentContext();
            }
            float f2 = 0.0f;
            if (i7 == i8) {
                f2 = ((((float) (this.U.getWidth() - this.U.getHeight())) * 1.0f) / 2.0f) / ((float) this.U.getWidth());
            }
            float f3 = f2;
            MediaManager mediaManager = new MediaManager(fileDescriptor, i3, i4, this.C, this.B, i2, recordingListener);
            this.I = mediaManager;
            this.I.setEncoderCount(2);
            this.I.initVideoEncoderWithSharedContext(this.n, i5, true, str);
            this.I.initAudioEncoder();
            this.I.setCropFactor(f3);
            this.I.startRecording();
            this.L = true;
            this.J = false;
            return true;
        } finally {
            this.e.unlock();
        }
    }

    public boolean startRecording(@NonNull String str, RecordingListener recordingListener, int i2, @NonNull int i3, @NonNull int i4, int i5, String str2) {
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        try {
            this.e.lock();
            if (!this.h) {
                LOG.d(a, "startRecording-> StickerApi is not init.");
                return false;
            }
            this.e.unlock();
            if (i7 == 0 || i8 == 0 || str.length() == 0 || this.I != null) {
                return false;
            }
            if (i6 != 0 && 90 != i6 && 180 != i6 && 270 != i6) {
                return false;
            }
            this.ak = false;
            this.M = recordingListener;
            if (EGL14.EGL_NO_CONTEXT == this.n) {
                this.n = EGL14.eglGetCurrentContext();
            }
            float f2 = 0.0f;
            if (i7 == i8) {
                f2 = ((((float) (this.U.getWidth() - this.U.getHeight())) * 1.0f) / 2.0f) / ((float) this.U.getWidth());
            }
            float f3 = f2;
            MediaManager mediaManager = new MediaManager(str, i3, i4, this.C, this.B, i2, recordingListener);
            this.I = mediaManager;
            this.I.setEncoderCount(2);
            this.I.initVideoEncoderWithSharedContext(this.n, i5, true, str2);
            this.I.initAudioEncoder();
            this.I.setCropFactor(f3);
            this.I.startRecording();
            this.L = true;
            this.J = false;
            return true;
        } finally {
            this.e.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00df A[Catch:{ all -> 0x0042, Exception -> 0x0118, all -> 0x0115 }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0105  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startRender(int i2, boolean z2, int i3, int i4, boolean z3, int[] iArr, byte[] bArr, boolean z4) {
        boolean z5;
        int i5;
        boolean z6;
        int i6;
        ASVLOFFSCREEN asvloffscreen;
        boolean z7;
        int[] iArr2 = iArr;
        try {
            this.e.lock();
            boolean z8 = this.h;
            String str = a;
            if (z8) {
                if (this.i) {
                    this.e.unlock();
                    if (this.U == null) {
                        LOG.d(str, "mBackgroundBuffer == null");
                        return;
                    }
                    try {
                        this.k = true;
                        if (this.Y) {
                            this.g.lock();
                            asvloffscreen = (ASVLOFFSCREEN) this.V.clone();
                            this.g.unlock();
                            i6 = 0;
                            z6 = false;
                            i5 = 0;
                        } else {
                            try {
                                this.f.lock();
                                asvloffscreen = (ASVLOFFSCREEN) this.U.clone();
                                this.f.unlock();
                                i6 = i2;
                                z6 = z2;
                                i5 = i3;
                            } catch (Exception e2) {
                                e = e2;
                            } catch (Throwable th) {
                                z5 = false;
                                this.f.unlock();
                                throw th;
                            }
                        }
                        this.aa = this.U.getHeight();
                        this.ab = this.U.getWidth();
                        if (asvloffscreen != null && asvloffscreen.getHeight() > 0) {
                            if (asvloffscreen.getWidth() > 0) {
                                if (this.ab == 0 || this.aa == 0) {
                                    this.aa = this.U.getHeight();
                                    this.ab = this.U.getWidth();
                                }
                                if (z4 && !this.W) {
                                    if (!this.X) {
                                        if (this.K) {
                                            this.aa = this.U.getHeight() * 2;
                                            this.ab = this.U.getWidth() * 2;
                                        }
                                        this.ak = true;
                                        this.H.renderWithBackground(null, asvloffscreen, i6, z6, i5, this.aa, this.ab, i4, z3, iArr, bArr, this.Y);
                                        z7 = true;
                                        if (iArr2 == null) {
                                            this.H.renderBackgroundWithTexture(iArr2[0], 0, false, e());
                                            z5 = false;
                                            a(iArr2[0], asvloffscreen.getHeight(), this.aa, this.ab, z7);
                                            a(iArr2[0]);
                                        } else {
                                            z5 = false;
                                        }
                                        this.k = z5;
                                        return;
                                    }
                                }
                                this.H.renderBackgroundWithImageData(asvloffscreen, i6, z6, iArr2);
                                z7 = false;
                                if (iArr2 == null) {
                                }
                                this.k = z5;
                                return;
                            }
                        }
                        this.k = false;
                        return;
                    } catch (Exception e3) {
                        e = e3;
                        z5 = false;
                        try {
                            e.printStackTrace();
                            this.k = z5;
                            return;
                        } catch (Throwable th2) {
                            th = th2;
                            this.k = r9;
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        boolean z9 = false;
                        this.k = z9;
                        throw th;
                    }
                }
            }
            LOG.d(str, "startRender() failed, engine is not inited or startRender process not ready! ");
            this.e.unlock();
        } catch (Throwable th4) {
            this.e.unlock();
            throw th4;
        }
    }

    public boolean stopRecording() {
        try {
            this.e.lock();
            if (!this.h) {
                this.e.unlock();
                return false;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } catch (Throwable th) {
            this.e.unlock();
            throw th;
        }
        this.e.unlock();
        if (!this.L) {
            return false;
        }
        this.ad = 0;
        this.ac = 0;
        if (this.I == null) {
            return false;
        }
        resumeRecording();
        this.L = false;
        LOG.d("stopRecording", "-- stopRecording --");
        this.I.stopRecording();
        this.J = false;
        this.G = 0;
        this.F = 0;
        this.D = 0;
        MediaResultCallback mediaResultCallback = this.O;
        if (mediaResultCallback != null) {
            mediaResultCallback.onVideoResult(this.ak);
        }
        return true;
    }

    public void unInit() {
        stopRecording();
        try {
            this.e.lock();
            if (!this.h) {
                LOG.d(a, "uninit () failed, engine is not inited. ");
                this.e.unlock();
                return;
            }
            this.h = false;
            int i2 = 100;
            while (true) {
                if (!this.j && !this.k) {
                    break;
                }
                i2--;
                if (i2 <= 0) {
                    break;
                }
                Thread.sleep(1);
            }
            this.l = false;
            this.K = false;
            this.v.clear();
            this.v = null;
            if (this.T != null && !this.T.isRecycled()) {
                this.T.isRecycled();
                this.T = null;
            }
            if (this.z != null) {
                this.z.deleteTexture();
                this.z = null;
            }
            if (this.y != null) {
                this.y.unInit();
                this.y = null;
            }
            if (this.w != null) {
                this.w.unInit();
                this.w = null;
            }
            if (this.x != null) {
                this.x.unInit();
                this.x = null;
            }
            this.U = null;
            this.n = EGL14.EGL_NO_CONTEXT;
            this.o = EGL14.EGL_NO_SURFACE;
            this.m = EGL14.EGL_NO_DISPLAY;
            this.r = 0;
            this.s = 0;
            this.j = false;
            this.k = false;
            this.i = false;
            this.e.unlock();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        } catch (Throwable th) {
            this.e.unlock();
            throw th;
        }
    }

    public void updateAvatarConfigInfo(AvatarEngine avatarEngine) {
        ExtraSceneEngine extraSceneEngine = this.aj;
        if (extraSceneEngine != null) {
            extraSceneEngine.setAvatarEngine(avatarEngine);
        }
    }
}

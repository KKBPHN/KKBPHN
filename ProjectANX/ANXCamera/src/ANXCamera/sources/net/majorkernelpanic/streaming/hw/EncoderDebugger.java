package net.majorkernelpanic.streaming.hw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

@SuppressLint({"NewApi"})
public class EncoderDebugger {
    private static final boolean ALWAYS_RECHECK = false;
    private static final int BITRATE = 1000000;
    private static final int FRAMERATE = 20;
    private static final String MIME_TYPE = "video/avc";
    private static final int NB_DECODED = 34;
    private static final int NB_ENCODED = 50;
    private static final String PREF_PREFIX = "libstreaming-";
    public static final String TAG = "EncoderDebugger";
    private static final boolean VERBOSE = true;
    private static final int VERSION = 3;
    private String mB64PPS;
    private String mB64SPS;
    private byte[] mData;
    private MediaFormat mDecOutputFormat;
    private byte[][] mDecodedVideo;
    private MediaCodec mDecoder;
    private int mDecoderColorFormat;
    private String mDecoderName;
    private MediaCodec mEncoder;
    private int mEncoderColorFormat;
    private String mEncoderName;
    private String mErrorLog;
    private int mHeight;
    private byte[] mInitialImage;
    private NV21Convertor mNV21;
    private byte[] mPPS;
    private SharedPreferences mPreferences;
    private byte[] mSPS;
    private int mSize;
    private byte[][] mVideo;
    private int mWidth;

    private EncoderDebugger(SharedPreferences sharedPreferences, int i, int i2) {
        this.mPreferences = sharedPreferences;
        this.mWidth = i;
        this.mHeight = i2;
        this.mSize = i * i2;
        reset();
    }

    public static synchronized void asyncDebug(final Context context, final int i, final int i2) {
        synchronized (EncoderDebugger.class) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EncoderDebugger.debug(PreferenceManager.getDefaultSharedPreferences(context), i, i2);
                    } catch (Exception unused) {
                    }
                }
            }).start();
        }
    }

    private void check(boolean z, String str) {
        if (!z) {
            Log.e(TAG, str);
            throw new IllegalStateException(str);
        }
    }

    private int checkPaddingNeeded() {
        int i = ((this.mSize * 3) / 2) - 1;
        int[] iArr = new int[34];
        int i2 = 0;
        int i3 = 0;
        while (i2 < 34) {
            if (this.mDecodedVideo[i2] != null) {
                int i4 = 0;
                while (i4 < i && (this.mDecodedVideo[i2][i - i4] & -1) < 50) {
                    i4 += 2;
                }
                String str = TAG;
                if (i4 > 0) {
                    iArr[i2] = (i4 >> 6) << 6;
                    if (iArr[i2] > i3) {
                        i3 = iArr[i2];
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Padding needed: ");
                    sb.append(iArr[i2]);
                    Log.e(str, sb.toString());
                } else {
                    Log.v(str, "No padding needed.");
                }
            }
            i2++;
        }
        return (i3 >> 6) << 6;
    }

    private boolean checkTestNeeded() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mWidth);
        sb.append("x");
        sb.append(this.mHeight);
        sb.append("-");
        String sb2 = sb.toString();
        SharedPreferences sharedPreferences = this.mPreferences;
        if (sharedPreferences == null) {
            return true;
        }
        StringBuilder sb3 = new StringBuilder();
        String str = PREF_PREFIX;
        sb3.append(str);
        sb3.append(sb2);
        String str2 = "lastSdk";
        sb3.append(str2);
        if (sharedPreferences.contains(sb3.toString())) {
            SharedPreferences sharedPreferences2 = this.mPreferences;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append(sb2);
            sb4.append(str2);
            int i = sharedPreferences2.getInt(sb4.toString(), 0);
            SharedPreferences sharedPreferences3 = this.mPreferences;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str);
            sb5.append(sb2);
            sb5.append("lastVersion");
            return VERSION.SDK_INT > i || 3 > sharedPreferences3.getInt(sb5.toString(), 0);
        }
    }

    private boolean compareChromaPanes(boolean z) {
        int i = 0;
        for (int i2 = 0; i2 < 34; i2++) {
            if (this.mDecodedVideo[i2] != null) {
                int i3 = this.mSize;
                if (!z) {
                    while (true) {
                        if (i3 >= (this.mSize * 3) / 2) {
                            break;
                        }
                        int i4 = (this.mInitialImage[i3] & -1) - (this.mDecodedVideo[i2][i3] & -1);
                        if (i4 < 0) {
                            i4 = -i4;
                        }
                        if (i4 > 50) {
                            i++;
                            break;
                        }
                        i3++;
                    }
                } else {
                    while (i3 < (this.mSize * 3) / 2) {
                        int i5 = (this.mInitialImage[i3] & -1) - (this.mDecodedVideo[i2][i3 + 1] & -1);
                        if (i5 < 0) {
                            i5 = -i5;
                        }
                        if (i5 > 50) {
                            i++;
                        }
                        i3 += 2;
                    }
                }
            }
        }
        return i <= 17;
    }

    private boolean compareLumaPanes() {
        int i = 0;
        for (int i2 = 0; i2 < 34; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= this.mSize) {
                    break;
                }
                byte[] bArr = this.mInitialImage;
                byte b = bArr[i3] & -1;
                byte[][] bArr2 = this.mDecodedVideo;
                int i4 = b - (bArr2[i2][i3] & -1);
                int i5 = i3 + 1;
                int i6 = (bArr[i5] & -1) - (bArr2[i2][i5] & -1);
                if (i4 < 0) {
                    i4 = -i4;
                }
                if (i6 < 0) {
                    i6 = -i6;
                }
                if (i4 > 50 && i6 > 50) {
                    this.mDecodedVideo[i2] = null;
                    i++;
                    break;
                }
                i3 += 10;
            }
        }
        return i <= 17;
    }

    private void configureDecoder() {
        byte[] bArr = {0, 0, 0, 1};
        ByteBuffer allocate = ByteBuffer.allocate(this.mSPS.length + 4 + 4 + this.mPPS.length);
        allocate.put(new byte[]{0, 0, 0, 1});
        allocate.put(this.mSPS);
        allocate.put(new byte[]{0, 0, 0, 1});
        allocate.put(this.mPPS);
        this.mDecoder = MediaCodec.createByCodecName(this.mDecoderName);
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(MIME_TYPE, this.mWidth, this.mHeight);
        createVideoFormat.setByteBuffer("csd-0", allocate);
        createVideoFormat.setInteger("color-format", this.mDecoderColorFormat);
        this.mDecoder.configure(createVideoFormat, null, null, 0);
        this.mDecoder.start();
        ByteBuffer[] inputBuffers = this.mDecoder.getInputBuffers();
        int dequeueInputBuffer = this.mDecoder.dequeueInputBuffer(50000);
        String str = "No buffer available !";
        String str2 = TAG;
        if (dequeueInputBuffer >= 0) {
            inputBuffers[dequeueInputBuffer].clear();
            inputBuffers[dequeueInputBuffer].put(bArr);
            inputBuffers[dequeueInputBuffer].put(this.mSPS);
            this.mDecoder.queueInputBuffer(dequeueInputBuffer, 0, inputBuffers[dequeueInputBuffer].position(), timestamp(), 0);
        } else {
            Log.e(str2, str);
        }
        int dequeueInputBuffer2 = this.mDecoder.dequeueInputBuffer(50000);
        if (dequeueInputBuffer2 >= 0) {
            inputBuffers[dequeueInputBuffer2].clear();
            inputBuffers[dequeueInputBuffer2].put(bArr);
            inputBuffers[dequeueInputBuffer2].put(this.mPPS);
            this.mDecoder.queueInputBuffer(dequeueInputBuffer2, 0, inputBuffers[dequeueInputBuffer2].position(), timestamp(), 0);
            return;
        }
        Log.e(str2, str);
    }

    private void configureEncoder() {
        this.mEncoder = MediaCodec.createByCodecName(this.mEncoderName);
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(MIME_TYPE, this.mWidth, this.mHeight);
        createVideoFormat.setInteger(IjkMediaMeta.IJKM_KEY_BITRATE, 1000000);
        createVideoFormat.setInteger("frame-rate", 20);
        createVideoFormat.setInteger("color-format", this.mEncoderColorFormat);
        createVideoFormat.setInteger("i-frame-interval", 1);
        this.mEncoder.configure(createVideoFormat, null, null, 1);
        this.mEncoder.start();
    }

    private void convertToNV21(int i) {
        boolean z;
        byte[] bArr = new byte[((this.mSize * 3) / 2)];
        int i2 = this.mWidth;
        int i3 = this.mHeight;
        int i4 = this.mDecoderColorFormat;
        MediaFormat mediaFormat = this.mDecOutputFormat;
        if (!(mediaFormat == null || mediaFormat == null)) {
            String str = "slice-height";
            if (mediaFormat.containsKey(str)) {
                i3 = mediaFormat.getInteger(str);
                int i5 = this.mHeight;
                if (i3 < i5) {
                    i3 = i5;
                }
            }
            String str2 = "stride";
            if (mediaFormat.containsKey(str2)) {
                i2 = mediaFormat.getInteger(str2);
                int i6 = this.mWidth;
                if (i2 < i6) {
                    i2 = i6;
                }
            }
            String str3 = "color-format";
            if (mediaFormat.containsKey(str3) && mediaFormat.getInteger(str3) > 0) {
                i4 = mediaFormat.getInteger(str3);
            }
        }
        int i7 = 0;
        if (!(i4 == 39 || i4 == 2130706688)) {
            switch (i4) {
                case 19:
                case 20:
                    z = true;
                    break;
            }
        }
        z = false;
        int i8 = 0;
        while (i8 < this.mSize) {
            int i9 = this.mWidth;
            if (i8 % i9 == 0) {
                i8 += i2 - i9;
            }
            bArr[i8] = this.mDecodedVideo[i][i8];
            i8++;
        }
        if (!z) {
            int i10 = 0;
            while (i7 < this.mSize / 4) {
                int i11 = this.mWidth;
                if ((i10 % i11) / 2 == 0) {
                    i10 += (i2 - i11) / 2;
                }
                int i12 = this.mSize;
                int i13 = i7 * 2;
                int i14 = i12 + i13 + 1;
                byte[][] bArr2 = this.mDecodedVideo;
                int i15 = (i2 * i3) + (i10 * 2);
                bArr[i14] = bArr2[i][i15];
                bArr[i12 + i13] = bArr2[i][i15 + 1];
                i10++;
                i7++;
            }
        } else {
            int i16 = 0;
            while (i7 < this.mSize / 4) {
                int i17 = this.mWidth;
                if ((i16 % i17) / 2 == 0) {
                    i16 += (i2 - i17) / 2;
                }
                int i18 = this.mSize;
                int i19 = i7 * 2;
                int i20 = i18 + i19 + 1;
                byte[][] bArr3 = this.mDecodedVideo;
                int i21 = i2 * i3;
                bArr[i20] = bArr3[i][i21 + i16];
                bArr[i18 + i19] = bArr3[i][((i21 * 5) / 4) + i16];
                i16++;
                i7++;
            }
        }
        this.mDecodedVideo[i] = bArr;
    }

    private void createTestImage() {
        int i;
        this.mInitialImage = new byte[((this.mSize * 3) / 2)];
        int i2 = 0;
        while (true) {
            i = this.mSize;
            if (i2 >= i) {
                break;
            }
            this.mInitialImage[i2] = (byte) ((i2 % 199) + 40);
            i2++;
        }
        while (i < (this.mSize * 3) / 2) {
            byte[] bArr = this.mInitialImage;
            bArr[i] = (byte) ((i % 200) + 40);
            bArr[i + 1] = (byte) (((i + 99) % 200) + 40);
            i += 2;
        }
    }

    public static synchronized EncoderDebugger debug(Context context, int i, int i2) {
        EncoderDebugger debug;
        synchronized (EncoderDebugger.class) {
            debug = debug(PreferenceManager.getDefaultSharedPreferences(context), i, i2);
        }
        return debug;
    }

    public static synchronized EncoderDebugger debug(SharedPreferences sharedPreferences, int i, int i2) {
        EncoderDebugger encoderDebugger;
        synchronized (EncoderDebugger.class) {
            encoderDebugger = new EncoderDebugger(sharedPreferences, i, i2);
            encoderDebugger.debug();
        }
        return encoderDebugger;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: type inference failed for: r4v0 */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r9v3, types: [boolean] */
    /* JADX WARNING: type inference failed for: r11v3 */
    /* JADX WARNING: type inference failed for: r11v4, types: [int] */
    /* JADX WARNING: type inference failed for: r4v5, types: [int] */
    /* JADX WARNING: type inference failed for: r11v5, types: [int] */
    /* JADX WARNING: type inference failed for: r4v7 */
    /* JADX WARNING: type inference failed for: r0v33 */
    /* JADX WARNING: type inference failed for: r12v23 */
    /* JADX WARNING: type inference failed for: r12v24, types: [int] */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r0v34 */
    /* JADX WARNING: type inference failed for: r16v0 */
    /* JADX WARNING: type inference failed for: r15v3 */
    /* JADX WARNING: type inference failed for: r16v1 */
    /* JADX WARNING: type inference failed for: r15v4, types: [int] */
    /* JADX WARNING: type inference failed for: r12v25, types: [int] */
    /* JADX WARNING: type inference failed for: r0v52 */
    /* JADX WARNING: type inference failed for: r4v23 */
    /* JADX WARNING: type inference failed for: r16v2 */
    /* JADX WARNING: type inference failed for: r15v5, types: [int] */
    /* JADX WARNING: type inference failed for: r16v3 */
    /* JADX WARNING: type inference failed for: r4v34 */
    /* JADX WARNING: type inference failed for: r4v37 */
    /* JADX WARNING: type inference failed for: r4v38 */
    /* JADX WARNING: type inference failed for: r4v39 */
    /* JADX WARNING: type inference failed for: r11v6 */
    /* JADX WARNING: type inference failed for: r4v40 */
    /* JADX WARNING: type inference failed for: r0v73 */
    /* JADX WARNING: type inference failed for: r16v4 */
    /* JADX WARNING: type inference failed for: r12v26 */
    /* JADX WARNING: type inference failed for: r4v41 */
    /* JADX WARNING: type inference failed for: r16v5 */
    /* JADX WARNING: type inference failed for: r15v6 */
    /* JADX WARNING: type inference failed for: r16v6 */
    /* JADX WARNING: type inference failed for: r4v42 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r16v2
  assigns: []
  uses: []
  mth insns count: 457
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
    /* JADX WARNING: Unknown variable types count: 19 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void debug() {
        boolean z;
        ? r16;
        boolean z2;
        String str = ")";
        String str2 = "x";
        ? r4 = 0;
        if (!checkTestNeeded()) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mWidth);
            sb.append(str2);
            sb.append(this.mHeight);
            sb.append("-");
            String sb2 = sb.toString();
            SharedPreferences sharedPreferences = this.mPreferences;
            StringBuilder sb3 = new StringBuilder();
            String str3 = PREF_PREFIX;
            sb3.append(str3);
            sb3.append(sb2);
            sb3.append(BaseEvent.VALUE_SUCCESS);
            if (sharedPreferences.getBoolean(sb3.toString(), false)) {
                this.mNV21.setSize(this.mWidth, this.mHeight);
                NV21Convertor nV21Convertor = this.mNV21;
                SharedPreferences sharedPreferences2 = this.mPreferences;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str3);
                sb4.append(sb2);
                sb4.append("sliceHeight");
                nV21Convertor.setSliceHeigth(sharedPreferences2.getInt(sb4.toString(), 0));
                NV21Convertor nV21Convertor2 = this.mNV21;
                SharedPreferences sharedPreferences3 = this.mPreferences;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str3);
                sb5.append(sb2);
                sb5.append("stride");
                nV21Convertor2.setStride(sharedPreferences3.getInt(sb5.toString(), 0));
                NV21Convertor nV21Convertor3 = this.mNV21;
                SharedPreferences sharedPreferences4 = this.mPreferences;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str3);
                sb6.append(sb2);
                sb6.append("padding");
                nV21Convertor3.setYPadding(sharedPreferences4.getInt(sb6.toString(), 0));
                NV21Convertor nV21Convertor4 = this.mNV21;
                SharedPreferences sharedPreferences5 = this.mPreferences;
                StringBuilder sb7 = new StringBuilder();
                sb7.append(str3);
                sb7.append(sb2);
                sb7.append("planar");
                nV21Convertor4.setPlanar(sharedPreferences5.getBoolean(sb7.toString(), false));
                NV21Convertor nV21Convertor5 = this.mNV21;
                SharedPreferences sharedPreferences6 = this.mPreferences;
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str3);
                sb8.append(sb2);
                sb8.append("reversed");
                nV21Convertor5.setColorPanesReversed(sharedPreferences6.getBoolean(sb8.toString(), false));
                SharedPreferences sharedPreferences7 = this.mPreferences;
                StringBuilder sb9 = new StringBuilder();
                sb9.append(str3);
                sb9.append(sb2);
                sb9.append("encoderName");
                String str4 = "";
                this.mEncoderName = sharedPreferences7.getString(sb9.toString(), str4);
                SharedPreferences sharedPreferences8 = this.mPreferences;
                StringBuilder sb10 = new StringBuilder();
                sb10.append(str3);
                sb10.append(sb2);
                sb10.append("colorFormat");
                this.mEncoderColorFormat = sharedPreferences8.getInt(sb10.toString(), 0);
                SharedPreferences sharedPreferences9 = this.mPreferences;
                StringBuilder sb11 = new StringBuilder();
                sb11.append(str3);
                sb11.append(sb2);
                sb11.append("pps");
                this.mB64PPS = sharedPreferences9.getString(sb11.toString(), str4);
                SharedPreferences sharedPreferences10 = this.mPreferences;
                StringBuilder sb12 = new StringBuilder();
                sb12.append(str3);
                sb12.append(sb2);
                sb12.append("sps");
                this.mB64SPS = sharedPreferences10.getString(sb12.toString(), str4);
                return;
            }
            StringBuilder sb13 = new StringBuilder();
            sb13.append("Phone not supported with this resolution (");
            sb13.append(this.mWidth);
            sb13.append(str2);
            sb13.append(this.mHeight);
            sb13.append(str);
            throw new RuntimeException(sb13.toString());
        }
        StringBuilder sb14 = new StringBuilder();
        sb14.append(">>>> Testing the phone for resolution ");
        sb14.append(this.mWidth);
        sb14.append(str2);
        sb14.append(this.mHeight);
        String sb15 = sb14.toString();
        String str5 = TAG;
        Log.d(str5, sb15);
        String str6 = MIME_TYPE;
        Codec[] findEncodersForMimeType = CodecManager.findEncodersForMimeType(str6);
        Codec[] findDecodersForMimeType = CodecManager.findDecodersForMimeType(str6);
        int i = 0;
        for (int i2 = 0; i2 < findEncodersForMimeType.length; i2++) {
            if (findEncodersForMimeType[i2].isHardwareAccelerated) {
                i += findEncodersForMimeType[i2].formats.length;
            }
        }
        boolean z3 = true;
        int i3 = 0;
        int i4 = 1;
        ? r42 = r4;
        while (i3 < findEncodersForMimeType.length) {
            if (findEncodersForMimeType[i3].isHardwareAccelerated) {
                ? r11 = r42;
                ? r43 = r42;
                while (r11 < findEncodersForMimeType[i3].formats.length) {
                    reset();
                    this.mEncoderName = findEncodersForMimeType[i3].name;
                    this.mEncoderColorFormat = findEncodersForMimeType[i3].formats[r11].intValue();
                    StringBuilder sb16 = new StringBuilder();
                    sb16.append(">> Test ");
                    int i5 = i4 + 1;
                    sb16.append(i4);
                    sb16.append("/");
                    sb16.append(i);
                    sb16.append(": ");
                    sb16.append(this.mEncoderName);
                    sb16.append(" with color format ");
                    sb16.append(this.mEncoderColorFormat);
                    String str7 = " at ";
                    sb16.append(str7);
                    sb16.append(this.mWidth);
                    sb16.append(str2);
                    sb16.append(this.mHeight);
                    Log.v(str5, sb16.toString());
                    this.mNV21.setSize(this.mWidth, this.mHeight);
                    this.mNV21.setSliceHeigth(this.mHeight);
                    this.mNV21.setStride(this.mWidth);
                    this.mNV21.setYPadding(r43);
                    this.mNV21.setEncoderColorFormat(this.mEncoderColorFormat);
                    createTestImage();
                    this.mData = this.mNV21.convert(this.mInitialImage);
                    try {
                        configureEncoder();
                        searchSPSandPPS();
                        StringBuilder sb17 = new StringBuilder();
                        sb17.append("SPS and PPS in b64: SPS=");
                        sb17.append(this.mB64SPS);
                        sb17.append(", PPS=");
                        sb17.append(this.mB64PPS);
                        Log.v(str5, sb17.toString());
                        encode();
                        ? r0 = r43;
                        ? r12 = r0;
                        ? r44 = r43;
                        ? r02 = r0;
                        while (r12 < findDecodersForMimeType.length && r02 == 0) {
                            ? r162 = r02;
                            ? r15 = r44;
                            while (r15 < findDecodersForMimeType[r12].formats.length && r162 == 0) {
                                this.mDecoderName = findDecodersForMimeType[r12].name;
                                this.mDecoderColorFormat = findDecodersForMimeType[r12].formats[r15].intValue();
                                try {
                                    configureDecoder();
                                    try {
                                        decode(z3);
                                        StringBuilder sb18 = new StringBuilder();
                                        sb18.append(this.mDecoderName);
                                        sb18.append(" successfully decoded the NALs (color format ");
                                        sb18.append(this.mDecoderColorFormat);
                                        sb18.append(str);
                                        Log.d(str5, sb18.toString());
                                        try {
                                            releaseDecoder();
                                            r16 = 1;
                                        } catch (Exception e) {
                                            e = e;
                                            z = true;
                                            try {
                                                StringWriter stringWriter = new StringWriter();
                                                e.printStackTrace(new PrintWriter(stringWriter));
                                                String stringWriter2 = stringWriter.toString();
                                                StringBuilder sb19 = new StringBuilder();
                                                sb19.append("Encoder ");
                                                sb19.append(this.mEncoderName);
                                                sb19.append(" cannot be used with color format ");
                                                sb19.append(this.mEncoderColorFormat);
                                                String sb20 = sb19.toString();
                                                Log.e(str5, sb20, e);
                                                StringBuilder sb21 = new StringBuilder();
                                                sb21.append(this.mErrorLog);
                                                sb21.append(sb20);
                                                sb21.append("\n");
                                                sb21.append(stringWriter2);
                                                this.mErrorLog = sb21.toString();
                                                e.printStackTrace();
                                                releaseEncoder();
                                                z3 = z;
                                                i4 = i5;
                                                r11++;
                                                r43 = 0;
                                            } catch (Throwable th) {
                                                releaseEncoder();
                                                throw th;
                                            }
                                        } catch (Throwable th2) {
                                            releaseDecoder();
                                            throw th2;
                                        }
                                    } catch (Exception e2) {
                                        StringBuilder sb22 = new StringBuilder();
                                        sb22.append(this.mDecoderName);
                                        sb22.append(" failed to decode the NALs");
                                        Log.e(str5, sb22.toString());
                                        e2.printStackTrace();
                                        releaseDecoder();
                                        r16 = r162;
                                    }
                                    z3 = true;
                                    r162 = r16;
                                    r15++;
                                } catch (Exception unused) {
                                    StringBuilder sb23 = new StringBuilder();
                                    sb23.append(this.mDecoderName);
                                    sb23.append(" can't be used with ");
                                    sb23.append(this.mDecoderColorFormat);
                                    sb23.append(str7);
                                    sb23.append(this.mWidth);
                                    sb23.append(str2);
                                    sb23.append(this.mHeight);
                                    Log.d(str5, sb23.toString());
                                    releaseDecoder();
                                }
                            }
                            r02 = r162;
                            z3 = true;
                            r12++;
                            r44 = 0;
                        }
                        if (r02 != 0) {
                            compareLumaPanes();
                            int checkPaddingNeeded = checkPaddingNeeded();
                            if (checkPaddingNeeded > 0) {
                                if (checkPaddingNeeded < 4096) {
                                    StringBuilder sb24 = new StringBuilder();
                                    sb24.append("Some padding is needed: ");
                                    sb24.append(checkPaddingNeeded);
                                    Log.d(str5, sb24.toString());
                                    this.mNV21.setYPadding(checkPaddingNeeded);
                                    createTestImage();
                                    this.mData = this.mNV21.convert(this.mInitialImage);
                                    encodeDecode();
                                } else {
                                    throw new RuntimeException("It is likely that sliceHeight!=height");
                                }
                            }
                            createTestImage();
                            if (!compareChromaPanes(false)) {
                                z = true;
                                try {
                                    if (compareChromaPanes(true)) {
                                        this.mNV21.setColorPanesReversed(true);
                                        Log.d(str5, "U and V pane are reversed");
                                    } else {
                                        throw new RuntimeException("Incorrect U or V pane...");
                                    }
                                } catch (Exception e3) {
                                    e = e3;
                                }
                            }
                            z = true;
                            saveTestResult(true);
                            StringBuilder sb25 = new StringBuilder();
                            sb25.append("The encoder ");
                            sb25.append(this.mEncoderName);
                            sb25.append(" is usable with resolution ");
                            sb25.append(this.mWidth);
                            sb25.append(str2);
                            sb25.append(this.mHeight);
                            Log.v(str5, sb25.toString());
                            releaseEncoder();
                            return;
                        }
                        z = true;
                        throw new RuntimeException("Failed to decode NALs from the encoder.");
                    } catch (Exception e4) {
                        e = e4;
                        z = z3;
                        StringWriter stringWriter3 = new StringWriter();
                        e.printStackTrace(new PrintWriter(stringWriter3));
                        String stringWriter22 = stringWriter3.toString();
                        StringBuilder sb192 = new StringBuilder();
                        sb192.append("Encoder ");
                        sb192.append(this.mEncoderName);
                        sb192.append(" cannot be used with color format ");
                        sb192.append(this.mEncoderColorFormat);
                        String sb202 = sb192.toString();
                        Log.e(str5, sb202, e);
                        StringBuilder sb212 = new StringBuilder();
                        sb212.append(this.mErrorLog);
                        sb212.append(sb202);
                        sb212.append("\n");
                        sb212.append(stringWriter22);
                        this.mErrorLog = sb212.toString();
                        e.printStackTrace();
                        releaseEncoder();
                        z3 = z;
                        i4 = i5;
                        r11++;
                        r43 = 0;
                    }
                }
            }
            i3++;
            z3 = z2;
            r42 = 0;
        }
        saveTestResult(r42);
        StringBuilder sb26 = new StringBuilder();
        String str8 = "No usable encoder were found on the phone for resolution ";
        sb26.append(str8);
        sb26.append(this.mWidth);
        sb26.append(str2);
        sb26.append(this.mHeight);
        Log.e(str5, sb26.toString());
        StringBuilder sb27 = new StringBuilder();
        sb27.append(str8);
        sb27.append(this.mWidth);
        sb27.append(str2);
        sb27.append(this.mHeight);
        throw new RuntimeException(sb27.toString());
    }

    private long decode(boolean z) {
        long j;
        long j2;
        int i;
        byte[] bArr;
        int i2;
        ByteBuffer byteBuffer;
        long timestamp = timestamp();
        ByteBuffer[] inputBuffers = this.mDecoder.getInputBuffers();
        ByteBuffer[] outputBuffers = this.mDecoder.getOutputBuffers();
        BufferInfo bufferInfo = new BufferInfo();
        long j3 = 0;
        ByteBuffer[] byteBufferArr = outputBuffers;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (j3 < 3000000) {
            String str = TAG;
            if (i3 < 50) {
                int dequeueInputBuffer = this.mDecoder.dequeueInputBuffer(50000);
                if (dequeueInputBuffer >= 0) {
                    int capacity = inputBuffers[dequeueInputBuffer].capacity();
                    int length = this.mVideo[i3].length;
                    inputBuffers[dequeueInputBuffer].clear();
                    String str2 = ").";
                    String str3 = ", capacity=";
                    j2 = timestamp;
                    String str4 = "The decoder input buffer is not big enough (nal=";
                    if ((!z || !hasPrefix(this.mVideo[i3])) && (z || hasPrefix(this.mVideo[i3]))) {
                        j = j3;
                        if (!z || hasPrefix(this.mVideo[i3])) {
                            if (!z && hasPrefix(this.mVideo[i3])) {
                                int i6 = length - 4;
                                boolean z2 = capacity >= i6;
                                StringBuilder sb = new StringBuilder();
                                sb.append(str4);
                                sb.append(i6);
                                sb.append(str3);
                                sb.append(capacity);
                                sb.append(str2);
                                check(z2, sb.toString());
                                byteBuffer = inputBuffers[dequeueInputBuffer];
                                byte[][] bArr2 = this.mVideo;
                                bArr = bArr2[i3];
                                i = 4;
                                i2 = bArr2[i3].length - 4;
                                byteBuffer.put(bArr, i, i2);
                            }
                            this.mDecoder.queueInputBuffer(dequeueInputBuffer, 0, length, timestamp(), 0);
                            i3++;
                        } else {
                            int i7 = length + 4;
                            boolean z3 = capacity >= i7;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str4);
                            sb2.append(i7);
                            sb2.append(str3);
                            sb2.append(capacity);
                            sb2.append(str2);
                            check(z3, sb2.toString());
                            inputBuffers[dequeueInputBuffer].put(new byte[]{0, 0, 0, 1});
                            byteBuffer = inputBuffers[dequeueInputBuffer];
                            byte[][] bArr3 = this.mVideo;
                            bArr = bArr3[i3];
                            i2 = bArr3[i3].length;
                        }
                    } else {
                        j = j3;
                        boolean z4 = capacity >= length;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str4);
                        sb3.append(length);
                        sb3.append(str3);
                        sb3.append(capacity);
                        sb3.append(str2);
                        check(z4, sb3.toString());
                        byteBuffer = inputBuffers[dequeueInputBuffer];
                        byte[][] bArr4 = this.mVideo;
                        bArr = bArr4[i3];
                        i2 = bArr4[i3].length;
                    }
                    i = 0;
                    byteBuffer.put(bArr, i, i2);
                    this.mDecoder.queueInputBuffer(dequeueInputBuffer, 0, length, timestamp(), 0);
                    i3++;
                } else {
                    j2 = timestamp;
                    j = j3;
                    Log.d(str, "No buffer available !");
                }
            } else {
                j2 = timestamp;
                j = j3;
            }
            int dequeueOutputBuffer = this.mDecoder.dequeueOutputBuffer(bufferInfo, 50000);
            if (dequeueOutputBuffer == -3) {
                byteBufferArr = this.mDecoder.getOutputBuffers();
            } else if (dequeueOutputBuffer == -2) {
                this.mDecOutputFormat = this.mDecoder.getOutputFormat();
            } else if (dequeueOutputBuffer >= 0) {
                if (i4 > 2) {
                    int i8 = bufferInfo.size;
                    this.mDecodedVideo[i5] = new byte[i8];
                    byteBufferArr[dequeueOutputBuffer].clear();
                    byteBufferArr[dequeueOutputBuffer].get(this.mDecodedVideo[i5], 0, i8);
                    convertToNV21(i5);
                    if (i5 >= 33) {
                        flushMediaCodec(this.mDecoder);
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Decoding ");
                        sb4.append(i4);
                        sb4.append(" frames took ");
                        sb4.append(j / 1000);
                        sb4.append(" ms");
                        Log.v(str, sb4.toString());
                        return j;
                    }
                    i5++;
                }
                this.mDecoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                i4++;
                j3 = timestamp() - j2;
                timestamp = j2;
            }
            j3 = timestamp() - j2;
            timestamp = j2;
        }
        throw new RuntimeException("The decoder did not decode anything.");
    }

    private long encode() {
        long timestamp = timestamp();
        BufferInfo bufferInfo = new BufferInfo();
        ByteBuffer[] inputBuffers = this.mEncoder.getInputBuffers();
        long j = 0;
        ByteBuffer[] outputBuffers = this.mEncoder.getOutputBuffers();
        int i = 0;
        while (j < 5000000) {
            int dequeueInputBuffer = this.mEncoder.dequeueInputBuffer(50000);
            if (dequeueInputBuffer >= 0) {
                check(inputBuffers[dequeueInputBuffer].capacity() >= this.mData.length, "The input buffer is not big enough.");
                inputBuffers[dequeueInputBuffer].clear();
                ByteBuffer byteBuffer = inputBuffers[dequeueInputBuffer];
                byte[] bArr = this.mData;
                byteBuffer.put(bArr, 0, bArr.length);
                this.mEncoder.queueInputBuffer(dequeueInputBuffer, 0, this.mData.length, timestamp(), 0);
            } else {
                Log.d(TAG, "No buffer available !");
            }
            int dequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(bufferInfo, 50000);
            if (dequeueOutputBuffer == -3) {
                outputBuffers = this.mEncoder.getOutputBuffers();
            } else if (dequeueOutputBuffer >= 0) {
                this.mVideo[i] = new byte[bufferInfo.size];
                outputBuffers[dequeueOutputBuffer].clear();
                int i2 = i + 1;
                outputBuffers[dequeueOutputBuffer].get(this.mVideo[i], 0, bufferInfo.size);
                this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                if (i2 >= 50) {
                    flushMediaCodec(this.mEncoder);
                    return j;
                }
                i = i2;
            } else {
                continue;
            }
            j = timestamp() - timestamp;
        }
        throw new RuntimeException("The encoder is too slow.");
    }

    private void encodeDecode() {
        encode();
        try {
            configureDecoder();
            decode(true);
        } finally {
            releaseDecoder();
        }
    }

    private void flushMediaCodec(MediaCodec mediaCodec) {
        BufferInfo bufferInfo = new BufferInfo();
        int i = 0;
        while (i != -1) {
            i = mediaCodec.dequeueOutputBuffer(bufferInfo, 50000);
            if (i >= 0) {
                mediaCodec.releaseOutputBuffer(i, false);
            }
        }
    }

    private boolean hasPrefix(byte[] bArr) {
        return bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 1;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|4|5|7) */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0007 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void releaseDecoder() {
        MediaCodec mediaCodec = this.mDecoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mDecoder.release();
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|4|5|7) */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0007 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void releaseEncoder() {
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mEncoder.release();
        }
    }

    private void reset() {
        this.mNV21 = new NV21Convertor();
        this.mVideo = new byte[50][];
        this.mDecodedVideo = new byte[34][];
        this.mErrorLog = "";
        this.mPPS = null;
        this.mSPS = null;
    }

    private void saveTestResult(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mWidth);
        sb.append("x");
        sb.append(this.mHeight);
        sb.append("-");
        String sb2 = sb.toString();
        Editor edit = this.mPreferences.edit();
        StringBuilder sb3 = new StringBuilder();
        String str = PREF_PREFIX;
        sb3.append(str);
        sb3.append(sb2);
        sb3.append(BaseEvent.VALUE_SUCCESS);
        edit.putBoolean(sb3.toString(), z);
        if (z) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append(sb2);
            sb4.append("lastSdk");
            edit.putInt(sb4.toString(), VERSION.SDK_INT);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str);
            sb5.append(sb2);
            sb5.append("lastVersion");
            edit.putInt(sb5.toString(), 3);
            StringBuilder sb6 = new StringBuilder();
            sb6.append(str);
            sb6.append(sb2);
            sb6.append("sliceHeight");
            edit.putInt(sb6.toString(), this.mNV21.getSliceHeigth());
            StringBuilder sb7 = new StringBuilder();
            sb7.append(str);
            sb7.append(sb2);
            sb7.append("stride");
            edit.putInt(sb7.toString(), this.mNV21.getStride());
            StringBuilder sb8 = new StringBuilder();
            sb8.append(str);
            sb8.append(sb2);
            sb8.append("padding");
            edit.putInt(sb8.toString(), this.mNV21.getYPadding());
            StringBuilder sb9 = new StringBuilder();
            sb9.append(str);
            sb9.append(sb2);
            sb9.append("planar");
            edit.putBoolean(sb9.toString(), this.mNV21.getPlanar());
            StringBuilder sb10 = new StringBuilder();
            sb10.append(str);
            sb10.append(sb2);
            sb10.append("reversed");
            edit.putBoolean(sb10.toString(), this.mNV21.getUVPanesReversed());
            StringBuilder sb11 = new StringBuilder();
            sb11.append(str);
            sb11.append(sb2);
            String str2 = "encoderName";
            sb11.append(str2);
            edit.putString(sb11.toString(), this.mEncoderName);
            StringBuilder sb12 = new StringBuilder();
            sb12.append(str);
            sb12.append(sb2);
            sb12.append("colorFormat");
            edit.putInt(sb12.toString(), this.mEncoderColorFormat);
            StringBuilder sb13 = new StringBuilder();
            sb13.append(str);
            sb13.append(sb2);
            sb13.append(str2);
            edit.putString(sb13.toString(), this.mEncoderName);
            StringBuilder sb14 = new StringBuilder();
            sb14.append(str);
            sb14.append(sb2);
            sb14.append("pps");
            edit.putString(sb14.toString(), this.mB64PPS);
            StringBuilder sb15 = new StringBuilder();
            sb15.append(str);
            sb15.append(sb2);
            sb15.append("sps");
            edit.putString(sb15.toString(), this.mB64SPS);
        }
        edit.commit();
    }

    private long searchSPSandPPS() {
        byte[] bArr;
        char c;
        byte[] bArr2;
        int i;
        ByteBuffer[] inputBuffers = this.mEncoder.getInputBuffers();
        ByteBuffer[] outputBuffers = this.mEncoder.getOutputBuffers();
        BufferInfo bufferInfo = new BufferInfo();
        byte[] bArr3 = new byte[128];
        long timestamp = timestamp();
        long j = 0;
        int i2 = 4;
        int i3 = 4;
        while (true) {
            if (j >= 3000000 || (this.mSPS != null && this.mPPS != null)) {
                break;
            }
            byte[] bArr4 = bArr3;
            int dequeueInputBuffer = this.mEncoder.dequeueInputBuffer(50000);
            if (dequeueInputBuffer >= 0) {
                check(inputBuffers[dequeueInputBuffer].capacity() >= this.mData.length, "The input buffer is not big enough.");
                inputBuffers[dequeueInputBuffer].clear();
                ByteBuffer byteBuffer = inputBuffers[dequeueInputBuffer];
                byte[] bArr5 = this.mData;
                byteBuffer.put(bArr5, 0, bArr5.length);
                this.mEncoder.queueInputBuffer(dequeueInputBuffer, 0, this.mData.length, timestamp(), 0);
            } else {
                Log.e(TAG, "No buffer available !");
            }
            int dequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(bufferInfo, 50000);
            if (dequeueOutputBuffer == -2) {
                MediaFormat outputFormat = this.mEncoder.getOutputFormat();
                ByteBuffer byteBuffer2 = outputFormat.getByteBuffer("csd-0");
                ByteBuffer byteBuffer3 = outputFormat.getByteBuffer("csd-1");
                this.mSPS = new byte[(byteBuffer2.capacity() - 4)];
                byteBuffer2.position(4);
                byte[] bArr6 = this.mSPS;
                byteBuffer2.get(bArr6, 0, bArr6.length);
                this.mPPS = new byte[(byteBuffer3.capacity() - 4)];
                byteBuffer3.position(4);
                byte[] bArr7 = this.mPPS;
                byteBuffer3.get(bArr7, 0, bArr7.length);
                break;
            }
            if (dequeueOutputBuffer == -3) {
                outputBuffers = this.mEncoder.getOutputBuffers();
            } else if (dequeueOutputBuffer >= 0) {
                int i4 = bufferInfo.size;
                c = 128;
                if (i4 < 128) {
                    bArr = bArr4;
                    outputBuffers[dequeueOutputBuffer].get(bArr, 0, i4);
                    if (i4 > 0 && bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 1) {
                        while (i2 < i4) {
                            while (true) {
                                if (bArr[i2 + 0] == 0 && bArr[i2 + 1] == 0 && bArr[i2 + 2] == 0) {
                                    if (bArr[i2 + 3] == 1) {
                                        break;
                                    }
                                }
                                if (i2 + 3 >= i4) {
                                    break;
                                }
                                i2++;
                            }
                            if (i2 + 3 >= i4) {
                                i2 = i4;
                            }
                            if ((bArr[i3] & 31) == 7) {
                                i = i2 - i3;
                                this.mSPS = new byte[i];
                                bArr2 = this.mSPS;
                            } else {
                                i = i2 - i3;
                                this.mPPS = new byte[i];
                                bArr2 = this.mPPS;
                            }
                            System.arraycopy(bArr, i3, bArr2, 0, i);
                            i3 = i2 + 4;
                            int i5 = i3;
                        }
                    }
                } else {
                    bArr = bArr4;
                }
                this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                j = timestamp() - timestamp;
                char c2 = c;
                bArr3 = bArr;
            }
            bArr = bArr4;
            c = 128;
            j = timestamp() - timestamp;
            char c22 = c;
            bArr3 = bArr;
        }
        boolean z = true;
        if (this.mPPS == null || this.mSPS == null) {
            z = false;
        }
        check(z, "Could not determine the SPS & PPS.");
        byte[] bArr8 = this.mPPS;
        this.mB64PPS = Base64.encodeToString(bArr8, 0, bArr8.length, 2);
        byte[] bArr9 = this.mSPS;
        this.mB64SPS = Base64.encodeToString(bArr9, 0, bArr9.length, 2);
        return j;
    }

    private long timestamp() {
        return System.nanoTime() / 1000;
    }

    public String getB64PPS() {
        return this.mB64PPS;
    }

    public String getB64SPS() {
        return this.mB64SPS;
    }

    public int getEncoderColorFormat() {
        return this.mEncoderColorFormat;
    }

    public String getEncoderName() {
        return this.mEncoderName;
    }

    public String getErrorLog() {
        return this.mErrorLog;
    }

    public NV21Convertor getNV21Convertor() {
        return this.mNV21;
    }
}

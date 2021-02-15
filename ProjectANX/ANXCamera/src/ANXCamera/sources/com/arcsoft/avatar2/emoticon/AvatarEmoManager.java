package com.arcsoft.avatar2.emoticon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Size;
import com.android.camera.module.impl.component.FileUtils;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarProcessInfo;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarTongueAnimationParam;
import com.arcsoft.avatar2.AvatarEngine;
import com.arcsoft.avatar2.emoticon.EmoInfo.EmoExtraInfo;
import com.arcsoft.avatar2.recoder.MediaManager;
import com.arcsoft.avatar2.recoder.RecordingListener;
import com.arcsoft.avatar2.util.ASVLOFFSCREEN;
import com.arcsoft.avatar2.util.AsvloffscreenUtil;
import com.arcsoft.avatar2.util.LOG;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class AvatarEmoManager {
    private static final String A = "RIGHT_EYE_ROTATE";
    private static final String B = "TONGUE_STATE";
    private static final String C = "MORPH_SIZE";
    private static final String D = "MORPH_VALUE";
    private static final String E = "KEY_X";
    public static final int EMO_GIF_MODE = 1;
    public static final int EMO_VIDEO_MODE = 0;
    private static final String F = "KEY_Y";
    private static final String G = "KEY_COLOR";
    private static final String H = "BACK_GROUND";
    private static final String I = "THUMB_INDEX";
    private static final String J = "SCALE_LEVEL";
    private static final String a = "AvatarEmoManager";
    private static final String p = "emo";
    private static final String q = "emo_map.xml";
    private static final String r = "foreground";
    private static final String s = "background";
    private static final String t = "thumbnail";
    private static final String u = "ANIM_NAME";
    private static final String v = "KEY_SIZE";
    private static final String w = "KEY_INDEX";
    private static final String x = "KEY_TIME";
    private static final String y = "HEAD_ROTATE";
    private static final String z = "LEFT_EYE_ROTATE";
    private AvatarEmoResCallback b;
    private AvatarEngine c = null;
    private int[] d = new int[1];
    private boolean e = false;
    private MediaManager f;
    private EGLContext g;
    private int h = 0;
    private ArrayList i = null;
    private volatile boolean j = false;
    private int k = -1;
    private ASVLOFFSCREEN l = null;
    private byte[] m = null;
    private Size n = null;
    private int o = -1;

    public interface AvatarEmoResCallback {
        void onFrameRefresh(EmoExtraInfo emoExtraInfo);

        void onMakeMediaEnd();
    }

    public AvatarEmoManager(AvatarEngine avatarEngine, String str, int i2, AvatarEmoResCallback avatarEmoResCallback) {
        this.b = avatarEmoResCallback;
        this.c = avatarEngine;
        this.g = EGL14.EGL_NO_CONTEXT;
        this.i = new ArrayList();
        this.k = i2 + 1;
        StringBuilder sb = new StringBuilder();
        sb.append("floderPath = ");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = a;
        LOG.d(str2, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("faceColorId = ");
        sb3.append(i2);
        LOG.d(str2, sb3.toString());
        this.c.setAnimationParam(false, 0);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str);
        String str3 = "/";
        sb4.append(str3);
        sb4.append(q);
        ArrayList d2 = d(sb4.toString());
        if (d2 != null && d2.size() > 0) {
            Iterator it = d2.iterator();
            while (it.hasNext()) {
                String str4 = (String) it.next();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str);
                sb5.append(str3);
                sb5.append(str4);
                String sb6 = sb5.toString();
                StringBuilder sb7 = new StringBuilder();
                sb7.append("file = ");
                sb7.append(sb6);
                LOG.d(str2, sb7.toString());
                File file = new File(sb6);
                if (file.exists() && file.isDirectory()) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(sb6);
                    sb8.append(str3);
                    sb8.append(str4);
                    sb8.append(".txt");
                    EmoInfo a2 = a(sb8.toString(), sb6);
                    if (a2 != null) {
                        this.i.add(a2);
                    }
                }
            }
        }
    }

    private int a() {
        return this.h;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:140:0x038a, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x038b, code lost:
        r16 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x0391, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x0392, code lost:
        r3 = r7;
        r4 = r8;
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x0396, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0397, code lost:
        r3 = r7;
        r4 = r8;
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x039b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x039c, code lost:
        r3 = r7;
        r4 = r8;
        r1 = r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x038a A[ExcHandler: NullPointerException (e java.lang.NullPointerException), PHI: r7 r8 
  PHI: (r7v1 java.lang.String) = (r7v0 java.lang.String), (r7v0 java.lang.String), (r7v0 java.lang.String), (r7v0 java.lang.String), (r7v5 java.lang.String) binds: [B:1:0x0010, B:2:?, B:3:0x0012, B:4:?, B:6:0x0040] A[DONT_GENERATE, DONT_INLINE]
  PHI: (r8v2 java.lang.String) = (r8v0 java.lang.String), (r8v0 java.lang.String), (r8v0 java.lang.String), (r8v0 java.lang.String), (r8v6 java.lang.String) binds: [B:1:0x0010, B:2:?, B:3:0x0012, B:4:?, B:6:0x0040] A[DONT_GENERATE, DONT_INLINE], Splitter:B:1:0x0010] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private EmoInfo a(String str, String str2) {
        String str3;
        String str4;
        FileNotFoundException fileNotFoundException;
        String str5;
        String str6;
        IOException iOException;
        String str7;
        String str8;
        NumberFormatException numberFormatException;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        String str14;
        String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        BufferedReader bufferedReader;
        InputStreamReader inputStreamReader;
        FileInputStream fileInputStream;
        String sb;
        String str20 = str;
        String str21 = str2;
        String str22 = "thumbnail";
        String str23 = w;
        String str24 = u;
        String str25 = "error = ";
        String str26 = a;
        try {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("infoFilePath = ");
            sb2.append(str20);
            sb2.append("\n parentPath = ");
            sb2.append(str21);
            LOG.d(str26, sb2.toString());
            FileInputStream fileInputStream2 = new FileInputStream(str20);
            InputStreamReader inputStreamReader2 = new InputStreamReader(fileInputStream2);
            BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader2);
            String str27 = "";
            String str28 = str27;
            EmoInfo emoInfo = null;
            EmoExtraInfo emoExtraInfo = null;
            while (true) {
                String readLine = bufferedReader2.readLine();
                if (readLine != null) {
                    String str29 = str27;
                    StringBuilder sb3 = new StringBuilder();
                    String str30 = str25;
                    try {
                        sb3.append("read_file line -> ");
                        sb3.append(readLine);
                        LOG.d(str26, sb3.toString());
                        boolean isEmpty = TextUtils.isEmpty(readLine);
                        String str31 = D;
                        if (!isEmpty) {
                            String trim = readLine.trim();
                            boolean equals = trim.equals(str24);
                            str9 = str26;
                            String str32 = s;
                            fileInputStream = fileInputStream2;
                            String str33 = r;
                            inputStreamReader = inputStreamReader2;
                            String str34 = FileUtils.FILTER_FILE_SUFFIX;
                            bufferedReader = bufferedReader2;
                            String str35 = "/";
                            if (equals) {
                                try {
                                    EmoInfo emoInfo2 = new EmoInfo();
                                    emoInfo2.setFilePath(str21);
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append(str21);
                                    sb4.append(str35);
                                    sb4.append(str33);
                                    sb4.append(str35);
                                    sb4.append(str22);
                                    sb4.append(str34);
                                    emoInfo2.setThumbForGroundPath(sb4.toString());
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append(str21);
                                    sb5.append(str35);
                                    sb5.append(str32);
                                    sb5.append(str35);
                                    sb5.append(str22);
                                    sb5.append(str34);
                                    emoInfo2.setThumbBgGroundPath(sb5.toString());
                                    str19 = str22;
                                    emoInfo = emoInfo2;
                                } catch (FileNotFoundException e2) {
                                    fileNotFoundException = e2;
                                    str4 = str30;
                                    str3 = str16;
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(str4);
                                    sb6.append(fileNotFoundException.toString());
                                    LOG.d(str3, sb6.toString());
                                    fileNotFoundException.printStackTrace();
                                    return null;
                                } catch (IOException e3) {
                                    iOException = e3;
                                    str6 = str30;
                                    str5 = str17;
                                    StringBuilder sb7 = new StringBuilder();
                                    sb7.append(str6);
                                    sb7.append(iOException.toString());
                                    LOG.d(str5, sb7.toString());
                                    iOException.printStackTrace();
                                    return null;
                                } catch (NumberFormatException e4) {
                                    numberFormatException = e4;
                                    str8 = str30;
                                    str7 = str18;
                                    StringBuilder sb8 = new StringBuilder();
                                    sb8.append(str8);
                                    sb8.append(numberFormatException.toString());
                                    LOG.d(str7, sb8.toString());
                                    numberFormatException.printStackTrace();
                                    return null;
                                } catch (NullPointerException e5) {
                                    e = e5;
                                    NullPointerException nullPointerException = e;
                                    StringBuilder sb9 = new StringBuilder();
                                    sb9.append(str30);
                                    sb9.append(nullPointerException.toString());
                                    LOG.d(str9, sb9.toString());
                                    nullPointerException.printStackTrace();
                                    return null;
                                }
                            } else {
                                if (trim.equals(str23)) {
                                    emoInfo.getClass();
                                    emoExtraInfo = new EmoExtraInfo();
                                } else if (str28.equals(str24)) {
                                    emoInfo.setEmoName(trim);
                                } else if (str28.equals(v)) {
                                    emoInfo.setEmoMaxCount(Integer.parseInt(trim));
                                } else if (str28.equals(E)) {
                                    emoInfo.setTranslationX(Float.parseFloat(trim));
                                } else if (str28.equals(F)) {
                                    emoInfo.setTranslationY(Float.parseFloat(trim));
                                } else {
                                    str19 = str22;
                                    boolean z2 = true;
                                    if (str28.equals(G)) {
                                        if (Integer.parseInt(trim) != 1) {
                                            z2 = false;
                                        }
                                        emoInfo.needFaceColor(z2);
                                    } else if (str28.equals(H)) {
                                        if (Integer.parseInt(trim) != 1) {
                                            z2 = false;
                                        }
                                        emoInfo.setMultipleBG(z2);
                                    } else if (str28.equals(I)) {
                                        emoInfo.setThumbFrameIndex(Integer.parseInt(trim));
                                    } else if (str28.equals(J)) {
                                        emoInfo.setScaleLevel(Float.parseFloat(trim));
                                    } else if (str28.equals(str23)) {
                                        emoExtraInfo.index = Integer.parseInt(trim);
                                        StringBuilder sb10 = new StringBuilder();
                                        sb10.append(str21);
                                        sb10.append(str35);
                                        sb10.append(str33);
                                        sb10.append(str35);
                                        sb10.append(emoExtraInfo.index + 1);
                                        sb10.append(str34);
                                        String sb11 = sb10.toString();
                                        if (emoInfo.isNeedFaceColor()) {
                                            StringBuilder sb12 = new StringBuilder();
                                            sb12.append(str21);
                                            sb12.append(str35);
                                            sb12.append(str33);
                                            sb12.append("/skin_");
                                            sb12.append(this.k);
                                            sb12.append(str35);
                                            sb12.append(emoExtraInfo.index + 1);
                                            sb12.append(str34);
                                            sb11 = sb12.toString();
                                        }
                                        emoExtraInfo.foreGroundPath = sb11;
                                        if (emoInfo.getEmoImageSize() == null) {
                                            emoInfo.setEmoImageSize(c(sb11));
                                        }
                                        if (emoInfo.isMultipleBG()) {
                                            StringBuilder sb13 = new StringBuilder();
                                            sb13.append(str21);
                                            sb13.append(str35);
                                            sb13.append(str32);
                                            sb13.append(str35);
                                            sb13.append(emoExtraInfo.index + 1);
                                            sb13.append(str34);
                                            sb = sb13.toString();
                                        } else if (this.l == null) {
                                            StringBuilder sb14 = new StringBuilder();
                                            sb14.append(str21);
                                            sb14.append(str35);
                                            sb14.append(str32);
                                            sb14.append(str35);
                                            sb14.append(emoExtraInfo.index + 1);
                                            sb14.append(str34);
                                            sb = sb14.toString();
                                            this.l = b(sb);
                                        }
                                        emoExtraInfo.backGroundPath = sb;
                                    } else if (str28.equals(x)) {
                                        emoExtraInfo.time = Float.parseFloat(trim);
                                    } else {
                                        String str36 = ",";
                                        if (str28.equals(y)) {
                                            String[] split = trim.split(str36);
                                            float[] fArr = new float[split.length];
                                            for (int i2 = 0; i2 < split.length; i2++) {
                                                fArr[i2] = Float.parseFloat(split[i2]);
                                            }
                                            emoExtraInfo.processInfo.setOrientations(fArr);
                                        } else if (str28.equals(z)) {
                                            String[] split2 = trim.split(str36);
                                            float[] fArr2 = new float[split2.length];
                                            for (int i3 = 0; i3 < split2.length; i3++) {
                                                fArr2[i3] = Float.parseFloat(split2[i3]);
                                            }
                                            emoExtraInfo.processInfo.setOrientationLeftEyes(fArr2);
                                        } else if (str28.equals(A)) {
                                            String[] split3 = trim.split(str36);
                                            float[] fArr3 = new float[split3.length];
                                            for (int i4 = 0; i4 < split3.length; i4++) {
                                                fArr3[i4] = Float.parseFloat(split3[i4]);
                                            }
                                            emoExtraInfo.processInfo.setOrientationRightEyes(fArr3);
                                        } else if (str28.equals(B)) {
                                            emoExtraInfo.processInfo.setTongueStatus(Integer.parseInt(trim));
                                        } else if (!str28.equals(C)) {
                                            if (str28.equals(str31)) {
                                                float[] fArr4 = new float[ASAvatarProcessInfo.getMaxExpressNum()];
                                                String[] split4 = trim.split(";");
                                                for (String split5 : split4) {
                                                    String[] split6 = split5.split(str36);
                                                    fArr4[Integer.parseInt(split6[1])] = Float.parseFloat(split6[0]);
                                                }
                                                emoExtraInfo.processInfo.setExpWeights(fArr4);
                                                emoInfo.getEmoExtraInfoList().add(emoExtraInfo);
                                            }
                                        }
                                    }
                                }
                                str19 = str22;
                            }
                            str27 = str29;
                            str28 = trim;
                        } else {
                            inputStreamReader = inputStreamReader2;
                            str19 = str22;
                            str9 = str26;
                            fileInputStream = fileInputStream2;
                            bufferedReader = bufferedReader2;
                            if (str28.equals(str31)) {
                                emoExtraInfo.processInfo.setExpWeights(new float[ASAvatarProcessInfo.getMaxExpressNum()]);
                                emoInfo.getEmoExtraInfoList().add(emoExtraInfo);
                                str27 = str29;
                                str28 = str27;
                            } else {
                                str27 = str29;
                            }
                        }
                        str25 = str30;
                        str26 = str9;
                        fileInputStream2 = fileInputStream;
                        inputStreamReader2 = inputStreamReader;
                        bufferedReader2 = bufferedReader;
                        str22 = str19;
                    } catch (FileNotFoundException e6) {
                        fileNotFoundException = e6;
                        str3 = str26;
                        str4 = str30;
                        StringBuilder sb62 = new StringBuilder();
                        sb62.append(str4);
                        sb62.append(fileNotFoundException.toString());
                        LOG.d(str3, sb62.toString());
                        fileNotFoundException.printStackTrace();
                        return null;
                    } catch (IOException e7) {
                        iOException = e7;
                        str5 = str26;
                        str6 = str30;
                        StringBuilder sb72 = new StringBuilder();
                        sb72.append(str6);
                        sb72.append(iOException.toString());
                        LOG.d(str5, sb72.toString());
                        iOException.printStackTrace();
                        return null;
                    } catch (NumberFormatException e8) {
                        numberFormatException = e8;
                        str7 = str26;
                        str8 = str30;
                        StringBuilder sb82 = new StringBuilder();
                        sb82.append(str8);
                        sb82.append(numberFormatException.toString());
                        LOG.d(str7, sb82.toString());
                        numberFormatException.printStackTrace();
                        return null;
                    } catch (NullPointerException e9) {
                        e = e9;
                        str9 = str26;
                        NullPointerException nullPointerException2 = e;
                        StringBuilder sb92 = new StringBuilder();
                        sb92.append(str30);
                        sb92.append(nullPointerException2.toString());
                        LOG.d(str9, sb92.toString());
                        nullPointerException2.printStackTrace();
                        return null;
                    }
                } else {
                    InputStreamReader inputStreamReader3 = inputStreamReader2;
                    String str37 = str25;
                    String str38 = str26;
                    FileInputStream fileInputStream3 = fileInputStream2;
                    bufferedReader2.close();
                    inputStreamReader3.close();
                    fileInputStream3.close();
                    return emoInfo;
                }
            }
        } catch (FileNotFoundException e10) {
            fileNotFoundException = e10;
            str4 = str11;
            str3 = str10;
            StringBuilder sb622 = new StringBuilder();
            sb622.append(str4);
            sb622.append(fileNotFoundException.toString());
            LOG.d(str3, sb622.toString());
            fileNotFoundException.printStackTrace();
            return null;
        } catch (IOException e11) {
            iOException = e11;
            str6 = str13;
            str5 = str12;
            StringBuilder sb722 = new StringBuilder();
            sb722.append(str6);
            sb722.append(iOException.toString());
            LOG.d(str5, sb722.toString());
            iOException.printStackTrace();
            return null;
        } catch (NumberFormatException e12) {
            numberFormatException = e12;
            str8 = str15;
            str7 = str14;
            StringBuilder sb822 = new StringBuilder();
            sb822.append(str8);
            sb822.append(numberFormatException.toString());
            LOG.d(str7, sb822.toString());
            numberFormatException.printStackTrace();
            return null;
        } catch (NullPointerException e13) {
        }
    }

    private ASVLOFFSCREEN a(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("getBackGroundInfo -> ");
        sb.append(str);
        LOG.d(a, sb.toString());
        Bitmap decodeFile = BitmapFactory.decodeFile(str);
        ASVLOFFSCREEN buildRGBA = AsvloffscreenUtil.buildRGBA(decodeFile);
        if (decodeFile != null) {
            decodeFile.recycle();
        }
        return buildRGBA;
    }

    private void a(int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("mode -> ");
        sb.append(i2);
        LOG.d(a, sb.toString());
        this.h = i2;
    }

    private ASVLOFFSCREEN b(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("getRGBAInfo -> ");
        sb.append(str);
        LOG.d(a, sb.toString());
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int i2 = options.outWidth;
        int i3 = options.outHeight;
        byte[] bArr = new byte[(i2 * i3 * 4)];
        this.c.readRGBA(str, i2, i3, bArr);
        return new ASVLOFFSCREEN(i2, i3, bArr);
    }

    private void b(int i2) {
        if (i2 > 0) {
            MediaManager mediaManager = this.f;
            if (mediaManager != null && this.e) {
                mediaManager.drawSurfaceWithTextureId(i2);
            }
        }
    }

    private Size c(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("getBitmapSize -> : ");
        sb.append(str);
        LOG.d(a, sb.toString());
        Options options = new Options();
        Bitmap decodeFile = BitmapFactory.decodeFile(str);
        options.inJustDecodeBounds = true;
        return new Size(decodeFile.getWidth(), decodeFile.getHeight());
    }

    private void c(int i2) {
        ASAvatarTongueAnimationParam aSAvatarTongueAnimationParam = new ASAvatarTongueAnimationParam(i2, i2 > 0 ? 1.0f : 0.0f, i2 > 0 ? 1.0f : 0.0f, i2 > 0 ? 1 : 0, i2 > 0 ? 1.0f : 0.0f);
        this.c.setTongueAnimationParam(aSAvatarTongueAnimationParam);
    }

    private ArrayList d(String str) {
        String str2 = a;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("getFileNameList = ");
            sb.append(str);
            LOG.d(str2, sb.toString());
            ArrayList arrayList = new ArrayList();
            FileInputStream fileInputStream = new FileInputStream(str);
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(fileInputStream, "UTF-8");
            int eventType = newPullParser.getEventType();
            String str3 = null;
            while (eventType != 1) {
                if (eventType != 0) {
                    if (eventType == 2 || eventType == 3) {
                        str3 = newPullParser.getName();
                    } else if (eventType == 4) {
                        if (str3.equals(p)) {
                            arrayList.add(newPullParser.getText());
                        }
                    }
                }
                eventType = newPullParser.next();
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

    private boolean e(String str) {
        try {
            File file = new File(str);
            if (file.isDirectory()) {
                return false;
            }
            return file.exists();
        } catch (NullPointerException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public void emoGLRender(EmoExtraInfo emoExtraInfo) {
        if (this.c != null) {
            if (this.h == 1) {
                int width = this.n.getWidth() * this.n.getHeight() * 4;
                byte[] bArr = this.m;
                if (bArr == null || bArr.length != width) {
                    this.m = new byte[width];
                }
            }
            AvatarEngine avatarEngine = this.c;
            String str = emoExtraInfo.foreGroundPath;
            ASVLOFFSCREEN asvloffscreen = emoExtraInfo.asBackGround;
            if (asvloffscreen == null) {
                asvloffscreen = this.l;
            }
            avatarEngine.renderWithBackground(str, asvloffscreen, 0, false, 0, this.n.getWidth() * 2, this.n.getHeight() * 2, 0, false, this.d, this.m, false);
            if (this.h == 0) {
                int[] iArr = this.d;
                if (iArr != null) {
                    b(iArr[0]);
                }
            }
            emoExtraInfo.asForeGround = null;
            emoExtraInfo.asBackGround = null;
        }
    }

    public void emoProcess(EmoInfo emoInfo) {
        if (this.c != null) {
            float[] fArr = new float[3];
            fArr[0] = emoInfo.getTranslationX();
            fArr[1] = emoInfo.getTranslationY();
            this.c.setRenderScene3F(false, emoInfo.getScaleLevel(), fArr);
            this.c.setAnimationParam(false, 0);
        }
        this.n = new Size(emoInfo.getEmoImageSize().getWidth(), emoInfo.getEmoImageSize().getHeight());
        Iterator it = emoInfo.getEmoExtraInfoList().iterator();
        long j2 = 0;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            EmoExtraInfo emoExtraInfo = (EmoExtraInfo) it.next();
            boolean z2 = this.j;
            String str = a;
            if (z2) {
                LOG.d(str, "process_render -> emoProcess release & break");
                break;
            }
            int i2 = (j2 > 0 ? 1 : (j2 == 0 ? 0 : -1));
            if (i2 > 0 && this.h == 0) {
                if (i2 < 0) {
                    j2 = 0;
                }
                SystemClock.sleep(j2);
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (emoInfo.isMultipleBG()) {
                emoExtraInfo.asBackGround = a(emoExtraInfo.backGroundPath);
            }
            this.c.setAnimationParam(false, 0);
            if (this.o != emoExtraInfo.processInfo.getTongueStatus()) {
                StringBuilder sb = new StringBuilder();
                sb.append("mTongueState = ");
                sb.append(this.o);
                sb.append(", info tong = ");
                sb.append(emoExtraInfo.processInfo.getTongueStatus());
                LOG.d(str, sb.toString());
                c(emoExtraInfo.processInfo.getTongueStatus());
                this.o = emoExtraInfo.processInfo.getTongueStatus();
            }
            this.c.setProcessInfo(emoExtraInfo.processInfo);
            j2 = currentTimeMillis + (40 - System.currentTimeMillis());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("sleepTime = ");
            sb2.append(j2);
            LOG.d(str, sb2.toString());
            AvatarEmoResCallback avatarEmoResCallback = this.b;
            if (avatarEmoResCallback != null) {
                avatarEmoResCallback.onFrameRefresh(emoExtraInfo);
            }
        }
        AvatarEmoResCallback avatarEmoResCallback2 = this.b;
        if (avatarEmoResCallback2 != null) {
            avatarEmoResCallback2.onMakeMediaEnd();
        }
    }

    public ArrayList getEmoList() {
        return this.i;
    }

    public int getFaceColorId() {
        return this.k;
    }

    public byte[] getImageData(EmoInfo emoInfo, int i2, int i3) {
        if (this.c == null || emoInfo == null || i2 <= 0 || i3 <= 0 || emoInfo.getThumbFrameIndex() > emoInfo.getEmoMaxCount()) {
            return null;
        }
        EmoExtraInfo emoExtraInfo = (EmoExtraInfo) emoInfo.getEmoExtraInfoList().get(emoInfo.getThumbFrameIndex());
        if (emoExtraInfo == null) {
            return null;
        }
        float[] fArr = new float[3];
        fArr[0] = emoInfo.getTranslationX();
        fArr[1] = emoInfo.getTranslationY();
        this.c.setRenderScene3F(false, emoInfo.getScaleLevel(), fArr);
        this.c.setAnimationParam(false, 0);
        byte[] bArr = new byte[(i2 * i3 * 4)];
        if (emoInfo.isMultipleBG()) {
            emoExtraInfo.asBackGround = a(e(emoInfo.getThumbBgGroundPath()) ? emoInfo.getThumbBgGroundPath() : emoExtraInfo.backGroundPath);
        }
        c(emoExtraInfo.processInfo.getTongueStatus());
        this.c.setProcessInfo(emoExtraInfo.processInfo);
        AvatarEngine avatarEngine = this.c;
        String thumbForGroundPath = e(emoInfo.getThumbForGroundPath()) ? emoInfo.getThumbForGroundPath() : emoExtraInfo.foreGroundPath;
        ASVLOFFSCREEN asvloffscreen = emoExtraInfo.asBackGround;
        if (asvloffscreen == null) {
            asvloffscreen = this.l;
        }
        avatarEngine.renderWithBackground(thumbForGroundPath, asvloffscreen, 0, false, 0, i2, i3, 0, false, this.d, bArr, false);
        emoExtraInfo.asBackGround = null;
        return bArr;
    }

    public boolean isRelease() {
        return this.j;
    }

    public void release() {
        LOG.d(a, "-> AvatarEmoManager release");
        this.j = true;
    }

    public boolean renderEmoThumb(EmoInfo emoInfo, int i2, int i3) {
        EmoInfo emoInfo2 = emoInfo;
        if (this.c == null || emoInfo2 == null || i2 <= 0 || i3 <= 0 || emoInfo.getThumbFrameIndex() > emoInfo.getEmoMaxCount()) {
            return false;
        }
        EmoExtraInfo emoExtraInfo = (EmoExtraInfo) emoInfo.getEmoExtraInfoList().get(emoInfo.getThumbFrameIndex());
        if (emoExtraInfo == null) {
            return false;
        }
        float[] fArr = new float[3];
        fArr[0] = emoInfo.getTranslationX();
        fArr[1] = emoInfo.getTranslationY();
        this.c.setRenderScene3F(false, emoInfo.getScaleLevel(), fArr);
        this.c.setAnimationParam(false, 0);
        byte[] bArr = new byte[(i2 * i3 * 4)];
        if (emoInfo.isMultipleBG()) {
            emoExtraInfo.asBackGround = a(e(emoInfo.getThumbBgGroundPath()) ? emoInfo.getThumbBgGroundPath() : emoExtraInfo.backGroundPath);
        }
        c(emoExtraInfo.processInfo.getTongueStatus());
        this.c.setProcessInfo(emoExtraInfo.processInfo);
        AvatarEngine avatarEngine = this.c;
        String thumbForGroundPath = e(emoInfo.getThumbForGroundPath()) ? emoInfo.getThumbForGroundPath() : emoExtraInfo.foreGroundPath;
        ASVLOFFSCREEN asvloffscreen = emoExtraInfo.asBackGround;
        if (asvloffscreen == null) {
            asvloffscreen = this.l;
        }
        EmoExtraInfo emoExtraInfo2 = emoExtraInfo;
        byte[] bArr2 = bArr;
        avatarEngine.renderWithBackground(thumbForGroundPath, asvloffscreen, 0, false, 0, i2, i3, 0, false, this.d, bArr, false);
        emoInfo2.setThumbnailData(bArr2);
        emoExtraInfo2.asBackGround = null;
        return true;
    }

    public ByteBuffer renderImageData(EmoInfo emoInfo, int i2, int i3) {
        if (this.c == null || emoInfo == null || i2 <= 0 || i3 <= 0 || emoInfo.getThumbFrameIndex() > emoInfo.getEmoMaxCount()) {
            return null;
        }
        EmoExtraInfo emoExtraInfo = (EmoExtraInfo) emoInfo.getEmoExtraInfoList().get(emoInfo.getThumbFrameIndex());
        if (emoExtraInfo == null) {
            return null;
        }
        float[] fArr = new float[3];
        fArr[0] = emoInfo.getTranslationX();
        fArr[1] = emoInfo.getTranslationY();
        this.c.setRenderScene3F(false, emoInfo.getScaleLevel(), fArr);
        this.c.setAnimationParam(false, 0);
        if (emoInfo.isMultipleBG()) {
            emoExtraInfo.asBackGround = a(e(emoInfo.getThumbBgGroundPath()) ? emoInfo.getThumbBgGroundPath() : emoExtraInfo.backGroundPath);
        }
        c(emoExtraInfo.processInfo.getTongueStatus());
        this.c.setProcessInfo(emoExtraInfo.processInfo);
        AvatarEngine avatarEngine = this.c;
        String thumbForGroundPath = e(emoInfo.getThumbForGroundPath()) ? emoInfo.getThumbForGroundPath() : emoExtraInfo.foreGroundPath;
        ASVLOFFSCREEN asvloffscreen = emoExtraInfo.asBackGround;
        if (asvloffscreen == null) {
            asvloffscreen = this.l;
        }
        avatarEngine.renderWithBackground(thumbForGroundPath, asvloffscreen, 0, false, 0, i2 * 2, i3 * 2, 0, false, this.d, null, false);
        this.c.renderBackgroundWithTexture(this.d[0], 0, false, null);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i2 * i3 * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, i2, i3, 6408, 5121, allocateDirect);
        emoExtraInfo.asBackGround = null;
        return allocateDirect;
    }

    public void reset() {
        AvatarEngine avatarEngine = this.c;
        if (avatarEngine != null) {
            avatarEngine.setRenderScene(false, 1.0f);
        }
        this.o = -1;
        this.j = false;
    }

    public void resumeRecording() {
        MediaManager mediaManager = this.f;
        if (mediaManager != null && this.e) {
            mediaManager.resumeRecording();
        }
    }

    public void setEmoList(ArrayList arrayList) {
        this.i = arrayList;
    }

    public void setFaceColorId(int i2) {
        if (i2 != this.k - 1) {
            this.k = i2 + 1;
            ArrayList arrayList = this.i;
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    EmoInfo emoInfo = (EmoInfo) it.next();
                    if (emoInfo.isNeedFaceColor()) {
                        Iterator it2 = emoInfo.getEmoExtraInfoList().iterator();
                        while (it2.hasNext()) {
                            EmoExtraInfo emoExtraInfo = (EmoExtraInfo) it2.next();
                            StringBuilder sb = new StringBuilder();
                            sb.append(emoInfo.getFilePath());
                            String str = "/";
                            sb.append(str);
                            sb.append(r);
                            sb.append("/skin_");
                            sb.append(this.k);
                            sb.append(str);
                            sb.append(emoExtraInfo.index + 1);
                            sb.append(FileUtils.FILTER_FILE_SUFFIX);
                            emoExtraInfo.foreGroundPath = sb.toString();
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("setFaceColorId -> ");
                            sb2.append(emoExtraInfo.foreGroundPath);
                            LOG.d(a, sb2.toString());
                        }
                    }
                }
            }
        }
    }

    public void startRecording(@NonNull FileDescriptor fileDescriptor, int i2, @NonNull int i3, @NonNull int i4, int i5, String str) {
        if (i3 != 0 && i4 != 0 && fileDescriptor != null) {
            if (i3 % 2 != 0) {
                i3++;
            }
            int i6 = i3;
            if (i4 % 2 != 0) {
                i4++;
            }
            int i7 = i4;
            if (this.f != null) {
                throw new RuntimeException("Recording has been started already.");
            } else if (i2 == 0 || 90 == i2 || 180 == i2 || 270 == i2) {
                if (EGL14.EGL_NO_CONTEXT == this.g) {
                    this.g = EGL14.eglGetCurrentContext();
                }
                MediaManager mediaManager = new MediaManager(fileDescriptor, i6, i7, 90, false, i2, (RecordingListener) null);
                this.f = mediaManager;
                this.f.setEncoderCount(1);
                this.f.initVideoEncoderWithSharedContext(this.g, i5, true, str);
                this.f.startRecording();
                this.e = true;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("StickerApi-> startRecording(...) screenOrientation = ");
                sb.append(i2);
                sb.append(" is invalid");
                throw new RuntimeException(sb.toString());
            }
        }
    }

    public void startRecording(@NonNull String str, int i2, @NonNull int i3, @NonNull int i4, int i5, String str2) {
        if (i3 != 0 && i4 != 0 && str.length() != 0) {
            if (i3 % 2 != 0) {
                i3++;
            }
            int i6 = i3;
            if (i4 % 2 != 0) {
                i4++;
            }
            int i7 = i4;
            if (this.f != null) {
                throw new RuntimeException("Recording has been started already.");
            } else if (i2 == 0 || 90 == i2 || 180 == i2 || 270 == i2) {
                if (EGL14.EGL_NO_CONTEXT == this.g) {
                    this.g = EGL14.eglGetCurrentContext();
                }
                MediaManager mediaManager = new MediaManager(str, i6, i7, 90, false, i2, (RecordingListener) null);
                this.f = mediaManager;
                this.f.setEncoderCount(1);
                this.f.initVideoEncoderWithSharedContext(this.g, i5, true, str2);
                this.f.startRecording();
                this.e = true;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("StickerApi-> startRecording(...) screenOrientation = ");
                sb.append(i2);
                sb.append(" is invalid");
                throw new RuntimeException(sb.toString());
            }
        }
    }

    public void stopRecording() {
        String str = a;
        LOG.d(str, "process_render -> stopRecording 0");
        if (this.e) {
            if (this.f != null) {
                LOG.d(str, "process_render -> stopRecording 1");
                resumeRecording();
                this.e = false;
                this.f.stopRecording();
                this.f.releaseFrameQueue();
                this.f = null;
            }
            LOG.d(str, "process_render -> stopRecording 2");
        }
    }
}

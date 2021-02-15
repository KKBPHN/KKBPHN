package com.miui.filtersdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;

public class OpenGlUtils {
    private static final boolean DEBUG_LUT = false;
    public static final int NOT_INIT = -1;
    public static final int NO_TEXTURE = -1;
    public static final int ON_DRAWN = 1;
    private static final String PRIMARY_STORAGE_PATH = Environment.getExternalStorageDirectory().toString();
    private static final String TAG = "OpenGlUtils";

    public static void checkGlError(String str) {
        int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": glError 0x");
            sb.append(Integer.toHexString(glGetError));
            String sb2 = sb.toString();
            Log.e(TAG, sb2);
            throw new RuntimeException(sb2);
        }
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r2v3, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r2v5, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r2v6 */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r2v8, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: type inference failed for: r0v4, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r2v10, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r2v12 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v3
  assigns: [?[OBJECT, ARRAY], ?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.graphics.Bitmap]
  uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], java.io.InputStream, ?[OBJECT, ARRAY]]
  mth insns count: 39
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
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0031 A[SYNTHETIC, Splitter:B:21:0x0031] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003c A[SYNTHETIC, Splitter:B:26:0x003c] */
    /* JADX WARNING: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Bitmap getImageFromAssetsFile(Context context, String str) {
        ? r2;
        ? r22;
        ? r0;
        ? r02 = 0;
        try {
            ? open = context.getResources().getAssets().open(str);
            try {
                r0 = r02;
                ? decodeStream = BitmapFactory.decodeStream(open);
                open.close();
                r0 = decodeStream;
                if (open != 0) {
                    try {
                        open.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return decodeStream;
            } catch (IOException e2) {
                e = e2;
                ? r1 = r0;
                r02 = open;
                r22 = r1;
                try {
                    e.printStackTrace();
                    if (r02 != 0) {
                    }
                } catch (Throwable th) {
                    th = th;
                    r2 = r02;
                    if (r2 != 0) {
                        try {
                            r2.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                r2 = open;
                if (r2 != 0) {
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            r22 = 0;
            e.printStackTrace();
            if (r02 != 0) {
                return r22;
            }
            try {
                r02.close();
                return r22;
            } catch (IOException e5) {
                e5.printStackTrace();
                return r22;
            }
        }
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r0v3, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r1v0, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r1v2, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: type inference failed for: r0v8, types: [android.graphics.Bitmap] */
    /* JADX WARNING: type inference failed for: r0v9 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v0
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], ?[OBJECT, ARRAY], android.graphics.Bitmap]
  uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], java.io.FileInputStream]
  mth insns count: 28
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
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x001e A[SYNTHETIC, Splitter:B:16:0x001e] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0026 A[SYNTHETIC, Splitter:B:22:0x0026] */
    /* JADX WARNING: Unknown variable types count: 5 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Bitmap getImageFromPath(String str) {
        ? r0;
        ? r1;
        ? r02;
        ? r03 = 0;
        try {
            ? fileInputStream = new FileInputStream(str);
            try {
                r02 = r03;
                r0 = BitmapFactory.decodeStream(fileInputStream);
            } catch (IOException e) {
                e = e;
                r1 = fileInputStream;
                r03 = r02;
                try {
                    e.printStackTrace();
                    if (r1 != 0) {
                    }
                    return r0;
                } catch (Throwable th) {
                    th = th;
                    r03 = r1;
                    if (r03 != 0) {
                        try {
                            r03.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
            try {
                fileInputStream.close();
                r0 = r0;
            } catch (IOException e3) {
                e3.printStackTrace();
                r0 = r0;
            }
        } catch (IOException e4) {
            e = e4;
            r1 = 0;
            e.printStackTrace();
            if (r1 != 0) {
                r0 = r0;
                r1.close();
            }
            return r0;
        } catch (Throwable th2) {
            th = th2;
            if (r03 != 0) {
            }
            throw th;
        }
        return r0;
    }

    public static int initEffectTexture(int i, int i2, int[] iArr, int i3) {
        if (iArr == null || iArr.length < 1) {
            return -1;
        }
        if (iArr[0] == -1) {
            GLES20.glGenTextures(1, iArr, 0);
            GLES20.glBindTexture(i3, iArr[0]);
            GLES20.glTexParameterf(i3, 10240, 9729.0f);
            GLES20.glTexParameterf(i3, 10241, 9729.0f);
            GLES20.glTexParameterf(i3, 10242, 33071.0f);
            GLES20.glTexParameterf(i3, 10243, 33071.0f);
            GLES20.glTexImage2D(i3, 0, 6408, i, i2, 0, 6408, 5121, null);
        } else {
            GLES20.glBindTexture(i3, iArr[0]);
            GLES20.glTexImage2D(i3, 0, 6408, i, i2, 0, 6408, 5121, null);
        }
        return iArr[0];
    }

    public static int loadProgram(String str, String str2) {
        String str3;
        int[] iArr = new int[1];
        int loadShader = loadShader(str, 35633);
        String str4 = "Load Program";
        if (loadShader == 0) {
            str3 = "Vertex Shader Failed";
        } else {
            int loadShader2 = loadShader(str2, 35632);
            if (loadShader2 == 0) {
                str3 = "Fragment Shader Failed";
            } else {
                int glCreateProgram = GLES20.glCreateProgram();
                GLES20.glAttachShader(glCreateProgram, loadShader);
                GLES20.glAttachShader(glCreateProgram, loadShader2);
                GLES20.glLinkProgram(glCreateProgram);
                GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
                if (iArr[0] <= 0) {
                    str3 = "Linking Failed";
                } else {
                    GLES20.glDeleteShader(loadShader);
                    GLES20.glDeleteShader(loadShader2);
                    return glCreateProgram;
                }
            }
        }
        Log.d(str4, str3);
        return 0;
    }

    private static int loadShader(String str, int i) {
        int[] iArr = new int[1];
        int glCreateShader = GLES20.glCreateShader(i);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Compilation\n");
        sb.append(GLES20.glGetShaderInfoLog(glCreateShader));
        Log.e("Load Shader Failed", sb.toString());
        return 0;
    }

    public static int loadTexture(Context context, String str) {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        if (iArr[0] != 0) {
            Bitmap imageFromAssetsFile = getImageFromAssetsFile(context, str);
            GLES20.glBindTexture(3553, iArr[0]);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLUtils.texImage2D(3553, 0, imageFromAssetsFile, 0);
            imageFromAssetsFile.recycle();
        }
        if (iArr[0] != 0) {
            return iArr[0];
        }
        Log.e(TAG, String.format("loadTexture failed,path:%s", new Object[]{str}));
        return -1;
    }

    public static int loadTexture(Bitmap bitmap, int i) {
        return loadTexture(bitmap, i, false);
    }

    public static int loadTexture(Bitmap bitmap, int i, boolean z) {
        if (bitmap == null) {
            return -1;
        }
        int[] iArr = new int[1];
        if (i == -1) {
            GLES20.glGenTextures(1, iArr, 0);
            GLES20.glBindTexture(3553, iArr[0]);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
        } else {
            GLES20.glBindTexture(3553, i);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            iArr[0] = i;
        }
        if (z) {
            bitmap.recycle();
        }
        return iArr[0];
    }

    public static int loadTexture(Buffer buffer, int i, int i2, int i3) {
        int i4 = i3;
        if (buffer == null) {
            return -1;
        }
        int[] iArr = new int[1];
        if (i4 == -1) {
            GLES20.glGenTextures(1, iArr, 0);
            GLES20.glBindTexture(3553, iArr[0]);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLES20.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, buffer);
        } else {
            GLES20.glBindTexture(3553, i4);
            GLES20.glTexSubImage2D(3553, 0, 0, 0, i, i2, 6408, 5121, buffer);
            iArr[0] = i4;
        }
        return iArr[0];
    }

    public static int loadTexture(Buffer buffer, int i, int i2, int i3, int i4) {
        int i5 = i3;
        if (buffer == null) {
            return -1;
        }
        int[] iArr = new int[1];
        if (i5 == -1) {
            GLES20.glGenTextures(1, iArr, 0);
            GLES20.glBindTexture(3553, iArr[0]);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10241, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLES20.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, i4, buffer);
        } else {
            GLES20.glBindTexture(3553, i5);
            GLES20.glTexSubImage2D(3553, 0, 0, 0, i, i2, 6408, i4, buffer);
            iArr[0] = i5;
        }
        return iArr[0];
    }

    public static int loadTextureFromPath(String str) {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        if (iArr[0] != 0) {
            Bitmap imageFromPath = getImageFromPath(str);
            GLES20.glBindTexture(3553, iArr[0]);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLUtils.texImage2D(3553, 0, imageFromPath, 0);
            imageFromPath.recycle();
        }
        if (iArr[0] != 0) {
            return iArr[0];
        }
        throw new RuntimeException("Error loading texture.");
    }

    public static void loadYuvToTextures(Buffer buffer, Buffer buffer2, int i, int i2, int[] iArr) {
        float f;
        float f2;
        int i3;
        int[] iArr2 = iArr;
        if (buffer != null && buffer2 != null && iArr2 != null && iArr2.length >= 2) {
            if (iArr2[0] == -1) {
                GLES20.glGenTextures(1, iArr2, 0);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, iArr2[0]);
                i3 = 3553;
                f = 9729.0f;
                f2 = 33071.0f;
                GLES20.glTexImage2D(3553, 0, 6409, i, i2, 0, 6409, 5121, buffer);
                GLES20.glTexParameterf(3553, 10240, 9729.0f);
                GLES20.glTexParameterf(3553, 10241, 9729.0f);
                GLES20.glTexParameterf(3553, 10242, 33071.0f);
                GLES20.glTexParameterf(3553, 10243, 33071.0f);
            } else {
                i3 = 3553;
                f = 9729.0f;
                f2 = 33071.0f;
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, iArr2[0]);
                GLES20.glTexImage2D(3553, 0, 6409, i, i2, 0, 6409, 5121, buffer);
            }
            if (iArr2[1] == -1) {
                GLES20.glGenTextures(1, iArr2, 1);
                GLES20.glActiveTexture(33985);
                GLES20.glBindTexture(i3, iArr2[1]);
                GLES20.glTexImage2D(3553, 0, 6410, i / 2, i2 / 2, 0, 6410, 5121, buffer2);
                GLES20.glTexParameterf(i3, 10240, f);
                GLES20.glTexParameterf(i3, 10241, f);
                GLES20.glTexParameterf(i3, 10242, f2);
                GLES20.glTexParameterf(i3, 10243, f2);
                return;
            }
            GLES20.glActiveTexture(33985);
            GLES20.glBindTexture(i3, iArr2[1]);
            GLES20.glTexImage2D(3553, 0, 6410, i / 2, i2 / 2, 0, 6410, 5121, buffer2);
        }
    }
}

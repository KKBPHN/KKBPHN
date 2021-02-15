package com.airbnb.lottie;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.annotation.WorkerThread;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000o0.C0043O0000oOo;
import com.airbnb.lottie.model.O0000O0o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.module.impl.component.FileUtils;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import okio.Okio;
import org.json.JSONObject;

/* renamed from: com.airbnb.lottie.O00oOooo reason: case insensitive filesystem */
public class C0096O00oOooo {
    /* access modifiers changed from: private */
    public static final Map O000oo0o = new HashMap();

    private C0096O00oOooo() {
    }

    private static C0086O000Ooo O000000o(O00000Oo o00000Oo, @Nullable String str, boolean z) {
        try {
            C0064O0000o0O O00000Oo2 = C0043O0000oOo.O00000Oo(o00000Oo);
            if (str != null) {
                O0000O0o.getInstance().O000000o(str, O00000Oo2);
            }
            C0086O000Ooo o000Ooo = new C0086O000Ooo((Object) O00000Oo2);
            if (z) {
                O0000OOo.closeQuietly(o00000Oo);
            }
            return o000Ooo;
        } catch (Exception e) {
            C0086O000Ooo o000Ooo2 = new C0086O000Ooo((Throwable) e);
            if (z) {
                O0000OOo.closeQuietly(o00000Oo);
            }
            return o000Ooo2;
        } catch (Throwable th) {
            if (z) {
                O0000OOo.closeQuietly(o00000Oo);
            }
            throw th;
        }
    }

    @WorkerThread
    private static C0086O000Ooo O000000o(InputStream inputStream, @Nullable String str, boolean z) {
        try {
            return O00000Oo(O00000Oo.O000000o(Okio.buffer(Okio.source(inputStream))), str);
        } finally {
            if (z) {
                O0000OOo.closeQuietly(inputStream);
            }
        }
    }

    public static O000o000 O000000o(Context context, @RawRes int i) {
        return O000000o(context, i, O00000o0(context, i));
    }

    public static O000o000 O000000o(Context context, @RawRes int i, @Nullable String str) {
        return O000000o(str, (Callable) new C0068O0000oOO(new WeakReference(context), context.getApplicationContext(), i));
    }

    public static O000o000 O000000o(Context context, String str, @Nullable String str2) {
        return O000000o(str2, (Callable) new C0066O0000oO(context.getApplicationContext(), str, str2));
    }

    public static O000o000 O000000o(O00000Oo o00000Oo, @Nullable String str) {
        return O000000o(str, (Callable) new C0072O0000ooO(o00000Oo, str));
    }

    private static O000o000 O000000o(@Nullable String str, Callable callable) {
        C0064O0000o0O o0000o0O = str == null ? null : O0000O0o.getInstance().get(str);
        if (o0000o0O != null) {
            return new O000o000(new C0095O00oOooO(o0000o0O));
        }
        if (str != null && O000oo0o.containsKey(str)) {
            return (O000o000) O000oo0o.get(str);
        }
        O000o000 o000o000 = new O000o000(callable);
        if (str != null) {
            o000o000.O00000o0(new C0065O0000o0o(str));
            o000o000.O00000Oo(new O0000o(str));
            O000oo0o.put(str, o000o000);
        }
        return o000o000;
    }

    public static O000o000 O000000o(ZipInputStream zipInputStream, @Nullable String str) {
        return O000000o(str, (Callable) new C0073O0000ooo(zipInputStream, str));
    }

    @Deprecated
    public static O000o000 O000000o(JSONObject jSONObject, @Nullable String str) {
        return O000000o(str, (Callable) new C0071O0000oo0(jSONObject, str));
    }

    @Nullable
    private static O00O0Oo O000000o(C0064O0000o0O o0000o0O, String str) {
        for (O00O0Oo o00O0Oo : o0000o0O.O00O0o0o().values()) {
            if (o00O0Oo.getFileName().equals(str)) {
                return o00O0Oo;
            }
        }
        return null;
    }

    @WorkerThread
    public static C0086O000Ooo O00000Oo(Context context, @RawRes int i) {
        return O00000Oo(context, i, O00000o0(context, i));
    }

    @WorkerThread
    public static C0086O000Ooo O00000Oo(Context context, @RawRes int i, @Nullable String str) {
        try {
            return O00000o0(context.getResources().openRawResource(i), str);
        } catch (NotFoundException e) {
            return new C0086O000Ooo((Throwable) e);
        }
    }

    @WorkerThread
    public static C0086O000Ooo O00000Oo(Context context, String str, @Nullable String str2) {
        try {
            return str.endsWith(".zip") ? O00000Oo(new ZipInputStream(context.getAssets().open(str)), str2) : O00000o0(context.getAssets().open(str), str2);
        } catch (IOException e) {
            return new C0086O000Ooo((Throwable) e);
        }
    }

    @WorkerThread
    public static C0086O000Ooo O00000Oo(O00000Oo o00000Oo, @Nullable String str) {
        return O000000o(o00000Oo, str, true);
    }

    @WorkerThread
    public static C0086O000Ooo O00000Oo(ZipInputStream zipInputStream, @Nullable String str) {
        try {
            return O00000o0(zipInputStream, str);
        } finally {
            O0000OOo.closeQuietly(zipInputStream);
        }
    }

    @WorkerThread
    @Deprecated
    public static C0086O000Ooo O00000Oo(JSONObject jSONObject, @Nullable String str) {
        return O00000o(jSONObject.toString(), str);
    }

    public static O000o000 O00000Oo(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("asset_");
        sb.append(str);
        return O000000o(context, str, sb.toString());
    }

    public static O000o000 O00000Oo(InputStream inputStream, @Nullable String str) {
        return O000000o(str, (Callable) new C0069O0000oOo(inputStream, str));
    }

    @WorkerThread
    public static C0086O000Ooo O00000o(String str, @Nullable String str2) {
        return O00000Oo(O00000Oo.O000000o(Okio.buffer(Okio.source((InputStream) new ByteArrayInputStream(str.getBytes())))), str2);
    }

    public static O000o000 O00000o(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("url_");
        sb.append(str);
        return O00000o0(context, str, sb.toString());
    }

    @WorkerThread
    public static C0086O000Ooo O00000o0(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("asset_");
        sb.append(str);
        return O00000Oo(context, str, sb.toString());
    }

    @WorkerThread
    public static C0086O000Ooo O00000o0(InputStream inputStream, @Nullable String str) {
        return O000000o(inputStream, str, true);
    }

    @WorkerThread
    private static C0086O000Ooo O00000o0(ZipInputStream zipInputStream, @Nullable String str) {
        HashMap hashMap = new HashMap();
        try {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            C0064O0000o0O o0000o0O = null;
            while (nextEntry != null) {
                String name = nextEntry.getName();
                if (!name.contains("__MACOSX")) {
                    if (nextEntry.getName().contains(SplitConstants.DOT_JSON)) {
                        o0000o0O = (C0064O0000o0O) O000000o(O00000Oo.O000000o(Okio.buffer(Okio.source((InputStream) zipInputStream))), (String) null, false).getValue();
                    } else if (name.contains(FileUtils.FILTER_FILE_SUFFIX) || name.contains(".webp")) {
                        String[] split = name.split("/");
                        hashMap.put(split[split.length - 1], BitmapFactory.decodeStream(zipInputStream));
                    }
                    nextEntry = zipInputStream.getNextEntry();
                }
                zipInputStream.closeEntry();
                nextEntry = zipInputStream.getNextEntry();
            }
            if (o0000o0O == null) {
                return new C0086O000Ooo((Throwable) new IllegalArgumentException("Unable to parse composition"));
            }
            for (Entry entry : hashMap.entrySet()) {
                O00O0Oo O000000o2 = O000000o(o0000o0O, (String) entry.getKey());
                if (O000000o2 != null) {
                    O000000o2.setBitmap(O0000OOo.O000000o((Bitmap) entry.getValue(), O000000o2.getWidth(), O000000o2.getHeight()));
                }
            }
            for (Entry entry2 : o0000o0O.O00O0o0o().entrySet()) {
                if (((O00O0Oo) entry2.getValue()).getBitmap() == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("There is no image for ");
                    sb.append(((O00O0Oo) entry2.getValue()).getFileName());
                    return new C0086O000Ooo((Throwable) new IllegalStateException(sb.toString()));
                }
            }
            if (str != null) {
                O0000O0o.getInstance().O000000o(str, o0000o0O);
            }
            return new C0086O000Ooo((Object) o0000o0O);
        } catch (IOException e) {
            return new C0086O000Ooo((Throwable) e);
        }
    }

    public static O000o000 O00000o0(Context context, String str, @Nullable String str2) {
        return O000000o(str2, (Callable) new C0067O0000oO0(context, str));
    }

    public static O000o000 O00000o0(String str, @Nullable String str2) {
        return O000000o(str2, (Callable) new C0070O0000oo(str, str2));
    }

    private static String O00000o0(Context context, @RawRes int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("rawRes");
        sb.append(isNightMode(context) ? "_night_" : "_day_");
        sb.append(i);
        return sb.toString();
    }

    @WorkerThread
    public static C0086O000Ooo O00000oO(Context context, String str) {
        return com.airbnb.lottie.network.O00000Oo.O00000oo(context, str);
    }

    public static void O0000o00(int i) {
        O0000O0o.getInstance().resize(i);
    }

    private static boolean isNightMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & 48) == 32;
    }
}

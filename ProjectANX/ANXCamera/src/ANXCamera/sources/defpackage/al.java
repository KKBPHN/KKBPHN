package defpackage;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* renamed from: al reason: default package */
public final class al {
    public static final String a;
    public static final String b;
    public static final List c = Collections.unmodifiableList(Arrays.asList(new String[]{"Pixel", "Pixel XL", "walleye", "Pixel 2", "taimen", "Pixel 2 XL", "blueline", "Pixel 3", "crosshatch", "Pixel 3 XL", "bonito", "Pixel 3a XL", "sargo", "Pixel 3a", "flame", "Pixel 4", "coral", "Pixel 4 XL", "Pixel 4a", "sunfish"}));
    private static final bi h;
    public final Context d;
    public final List e = new ArrayList();
    public bi f;
    public boolean g;
    private final PackageManager i;

    static {
        String str = "com.google.android.googlequicksearchbox.GsaPublicContentProvider";
        a = String.format("content://%s/publicvalue/lens_oem_availability", new Object[]{str});
        b = String.format("content://%s/publicvalue/ar_stickers_availability", new Object[]{str});
        bf bfVar = (bf) bi.f.e();
        if (bfVar.c) {
            bfVar.b();
            bfVar.c = false;
        }
        bi biVar = (bi) bfVar.b;
        String str2 = "1.2.0";
        str2.getClass();
        biVar.a = 1 | biVar.a;
        biVar.b = str2;
        bi biVar2 = (bi) bfVar.b;
        String str3 = "";
        str3.getClass();
        biVar2.a |= 2;
        biVar2.c = str3;
        int i2 = bh.a;
        if (bfVar.c) {
            bfVar.b();
            bfVar.c = false;
        }
        bi biVar3 = (bi) bfVar.b;
        int i3 = i2 - 2;
        if (i2 != 0) {
            biVar3.d = i3;
            biVar3.a |= 4;
            int i4 = bh.a;
            if (bfVar.c) {
                bfVar.b();
                bfVar.c = false;
            }
            bi biVar4 = (bi) bfVar.b;
            int i5 = i4 - 2;
            if (i4 != 0) {
                biVar4.e = i5;
                biVar4.a |= 8;
                h = (bi) bfVar.h();
                return;
            }
            throw null;
        }
        throw null;
    }

    public al(Context context) {
        PackageManager packageManager = context.getPackageManager();
        this.d = context;
        this.i = packageManager;
        this.g = false;
        this.f = h;
        try {
            PackageInfo packageInfo = this.i.getPackageInfo("com.google.android.googlequicksearchbox", 0);
            if (packageInfo != null) {
                bi biVar = h;
                da daVar = (da) biVar.b(5);
                daVar.O000000o((de) biVar);
                bf bfVar = (bf) daVar;
                String str = packageInfo.versionName;
                if (bfVar.c) {
                    bfVar.b();
                    bfVar.c = false;
                }
                bi biVar2 = (bi) bfVar.b;
                bi biVar3 = bi.f;
                str.getClass();
                biVar2.a |= 2;
                biVar2.c = str;
                this.f = (bi) bfVar.h();
            }
        } catch (NameNotFoundException unused) {
            Log.e("LensSdkParamsReader", "Unable to find agsa package: com.google.android.googlequicksearchbox");
        }
        new ak(this).execute(new Void[0]);
    }

    public final void O000000o(ai aiVar) {
        if (!this.g) {
            this.e.add(aiVar);
        } else {
            aiVar.O000000o(this.f);
        }
    }

    public final boolean a() {
        try {
            return this.i.getApplicationInfo("com.google.android.googlequicksearchbox", 0).enabled;
        } catch (NameNotFoundException unused) {
            Log.e("LensSdkParamsReader", "Unable to find agsa package: com.google.android.googlequicksearchbox");
            return false;
        }
    }
}

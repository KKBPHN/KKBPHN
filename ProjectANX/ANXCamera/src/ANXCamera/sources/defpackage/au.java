package defpackage;

import android.content.Intent;
import android.util.Log;

/* renamed from: au reason: default package */
final /* synthetic */ class au implements ai {
    private final ba a;

    public au(ba baVar) {
        this.a = baVar;
    }

    public final void O000000o(bi biVar) {
        String str = "LensServiceConnImpl";
        ba baVar = this.a;
        int a2 = bh.a(biVar.d);
        if (a2 == 0) {
            a2 = bh.a;
        }
        if (a2 == bh.b) {
            Intent intent = new Intent("com.google.android.apps.gsa.publicsearch.IPublicSearchService");
            intent.setPackage("com.google.android.googlequicksearchbox");
            try {
                if (!baVar.b.bindService(intent, baVar, 65)) {
                    Log.e(str, "Unable to bind Lens service.");
                    baVar.h = bh.j;
                    baVar.a(7);
                    return;
                }
                baVar.a(3);
            } catch (SecurityException e) {
                Log.e(str, "Unable to bind Lens service due to security exception.", e);
                baVar.h = bh.j;
                baVar.a(7);
            }
        } else {
            int a3 = bh.a(biVar.d);
            if (a3 == 0) {
                a3 = bh.a;
            }
            baVar.h = a3;
            baVar.a(6);
        }
    }
}

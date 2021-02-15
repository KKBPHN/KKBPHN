package com.airbnb.lottie.O00000Oo;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable.Callback;
import android.view.View;
import androidx.annotation.Nullable;
import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.O00000o0;
import com.airbnb.lottie.model.O0000Oo0;
import java.util.HashMap;
import java.util.Map;

public class O000000o {
    private final Map O00o0o = new HashMap();
    private final O0000Oo0 O00o0o0O = new O0000Oo0();
    private final Map O00o0o0o = new HashMap();
    private String O00o0oO0 = ".ttf";
    private final AssetManager assetManager;
    @Nullable
    private O00000o0 delegate;

    public O000000o(Callback callback, @Nullable O00000o0 o00000o0) {
        AssetManager assets;
        this.delegate = o00000o0;
        if (!(callback instanceof View)) {
            O00000o.warning("LottieDrawable must be inside of a view for images to work.");
            assets = null;
        } else {
            assets = ((View) callback).getContext().getAssets();
        }
        this.assetManager = assets;
    }

    private Typeface O000000o(Typeface typeface, String str) {
        boolean contains = str.contains("Italic");
        boolean contains2 = str.contains("Bold");
        int i = (!contains || !contains2) ? contains ? 2 : contains2 ? 1 : 0 : 3;
        return typeface.getStyle() == i ? typeface : Typeface.create(typeface, i);
    }

    private Typeface O000Oo0o(String str) {
        Typeface typeface = (Typeface) this.O00o0o.get(str);
        if (typeface != null) {
            return typeface;
        }
        Typeface typeface2 = null;
        O00000o0 o00000o0 = this.delegate;
        if (o00000o0 != null) {
            typeface2 = o00000o0.O0000oO(str);
        }
        O00000o0 o00000o02 = this.delegate;
        if (o00000o02 != null && typeface2 == null) {
            String O0000oOO = o00000o02.O0000oOO(str);
            if (O0000oOO != null) {
                typeface2 = Typeface.createFromAsset(this.assetManager, O0000oOO);
            }
        }
        if (typeface2 == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("fonts/");
            sb.append(str);
            sb.append(this.O00o0oO0);
            typeface2 = Typeface.createFromAsset(this.assetManager, sb.toString());
        }
        this.O00o0o.put(str, typeface2);
        return typeface2;
    }

    public Typeface O000000o(String str, String str2) {
        this.O00o0o0O.set(str, str2);
        Typeface typeface = (Typeface) this.O00o0o0o.get(this.O00o0o0O);
        if (typeface != null) {
            return typeface;
        }
        Typeface O000000o2 = O000000o(O000Oo0o(str), str2);
        this.O00o0o0o.put(this.O00o0o0O, O000000o2);
        return O000000o2;
    }

    public void O00000Oo(@Nullable O00000o0 o00000o0) {
        this.delegate = o00000o0;
    }

    public void O000O00o(String str) {
        this.O00o0oO0 = str;
    }
}

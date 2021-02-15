package com.airbnb.lottie.O00000Oo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable.Callback;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import androidx.annotation.Nullable;
import com.airbnb.lottie.O00000o;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00O0Oo;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class O00000Oo {
    private static final Object O00o0oOo = new Object();
    private String O00o0oO;
    private final Map O00o0oOO;
    private final Context context;
    @Nullable
    private O00000o delegate;

    public O00000Oo(Callback callback, String str, O00000o o00000o, Map map) {
        this.O00o0oO = str;
        if (!TextUtils.isEmpty(str)) {
            String str2 = this.O00o0oO;
            if (str2.charAt(str2.length() - 1) != '/') {
                StringBuilder sb = new StringBuilder();
                sb.append(this.O00o0oO);
                sb.append('/');
                this.O00o0oO = sb.toString();
            }
        }
        if (!(callback instanceof View)) {
            com.airbnb.lottie.O00000o.O00000o.warning("LottieDrawable must be inside of a view for images to work.");
            this.O00o0oOO = new HashMap();
            this.context = null;
            return;
        }
        this.context = ((View) callback).getContext();
        this.O00o0oOO = map;
        O00000Oo(o00000o);
    }

    private Bitmap putBitmap(String str, @Nullable Bitmap bitmap) {
        synchronized (O00o0oOo) {
            ((O00O0Oo) this.O00o0oOO.get(str)).setBitmap(bitmap);
        }
        return bitmap;
    }

    @Nullable
    public Bitmap O000000o(String str, @Nullable Bitmap bitmap) {
        if (bitmap == null) {
            O00O0Oo o00O0Oo = (O00O0Oo) this.O00o0oOO.get(str);
            Bitmap bitmap2 = o00O0Oo.getBitmap();
            o00O0Oo.setBitmap(null);
            return bitmap2;
        }
        Bitmap bitmap3 = ((O00O0Oo) this.O00o0oOO.get(str)).getBitmap();
        putBitmap(str, bitmap);
        return bitmap3;
    }

    public boolean O000000o(Context context2) {
        return (context2 == null && this.context == null) || this.context.equals(context2);
    }

    public void O00000Oo(@Nullable O00000o o00000o) {
        this.delegate = o00000o;
    }

    @Nullable
    public Bitmap O000O0OO(String str) {
        String str2;
        Bitmap O000000o2;
        O00O0Oo o00O0Oo = (O00O0Oo) this.O00o0oOO.get(str);
        if (o00O0Oo == null) {
            return null;
        }
        Bitmap bitmap = o00O0Oo.getBitmap();
        if (bitmap != null) {
            return bitmap;
        }
        O00000o o00000o = this.delegate;
        if (o00000o != null) {
            Bitmap O000000o3 = o00000o.O000000o(o00O0Oo);
            if (O000000o3 != null) {
                putBitmap(str, O000000o3);
            }
            return O000000o3;
        }
        String fileName = o00O0Oo.getFileName();
        Options options = new Options();
        options.inScaled = true;
        options.inDensity = 160;
        if (!fileName.startsWith("data:") || fileName.indexOf("base64,") <= 0) {
            try {
                if (!TextUtils.isEmpty(this.O00o0oO)) {
                    AssetManager assets = this.context.getAssets();
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.O00o0oO);
                    sb.append(fileName);
                    O000000o2 = O0000OOo.O000000o(BitmapFactory.decodeStream(assets.open(sb.toString()), null, options), o00O0Oo.getWidth(), o00O0Oo.getHeight());
                } else {
                    throw new IllegalStateException("You must set an images folder before loading an image. Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
                }
            } catch (IOException e) {
                e = e;
                str2 = "Unable to open asset.";
                com.airbnb.lottie.O00000o.O00000o.O00000Oo(str2, e);
                return null;
            }
        } else {
            try {
                byte[] decode = Base64.decode(fileName.substring(fileName.indexOf(44) + 1), 0);
                O000000o2 = BitmapFactory.decodeByteArray(decode, 0, decode.length, options);
            } catch (IllegalArgumentException e2) {
                e = e2;
                str2 = "data URL did not have correct base64 format.";
                com.airbnb.lottie.O00000o.O00000o.O00000Oo(str2, e);
                return null;
            }
        }
        putBitmap(str, O000000o2);
        return O000000o2;
    }
}

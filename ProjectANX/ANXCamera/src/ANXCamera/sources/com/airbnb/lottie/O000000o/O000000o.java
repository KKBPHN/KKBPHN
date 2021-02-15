package com.airbnb.lottie.O000000o;

import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.LocaleList;
import androidx.annotation.NonNull;

public class O000000o extends Paint {
    public O000000o() {
    }

    public O000000o(int i) {
        super(i);
    }

    public O000000o(int i, Mode mode) {
        super(i);
        setXfermode(new PorterDuffXfermode(mode));
    }

    public O000000o(Mode mode) {
        setXfermode(new PorterDuffXfermode(mode));
    }

    public void setTextLocales(@NonNull LocaleList localeList) {
    }
}

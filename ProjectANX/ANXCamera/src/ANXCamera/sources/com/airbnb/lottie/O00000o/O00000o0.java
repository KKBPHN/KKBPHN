package com.airbnb.lottie.O00000o;

import android.util.Log;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.C0084O000OoOO;
import java.util.HashSet;
import java.util.Set;

public class O00000o0 implements C0084O000OoOO {
    private static final Set O0OO0oO = new HashSet();

    public void O000000o(String str, Throwable th) {
        if (C0053O00000oO.DBG) {
            Log.d(C0053O00000oO.TAG, str, th);
        }
    }

    public void O00000Oo(String str, Throwable th) {
        if (!O0OO0oO.contains(str)) {
            Log.w(C0053O00000oO.TAG, str, th);
            O0OO0oO.add(str);
        }
    }

    public void O00000o0(String str, Throwable th) {
        if (C0053O00000oO.DBG) {
            Log.d(C0053O00000oO.TAG, str, th);
        }
    }

    public void debug(String str) {
        O00000o0(str, null);
    }

    public void warning(String str) {
        O00000Oo(str, null);
    }
}

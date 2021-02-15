package com.airbnb.lottie;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.annotation.WorkerThread;
import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import java.io.InputStream;
import org.json.JSONObject;

@Deprecated
public class O0000o0 {
    private O0000o0() {
    }

    @Deprecated
    public static O00000Oo O000000o(Context context, @RawRes int i, O000o00 o000o00) {
        O0000o00 o0000o00 = new O0000o00(o000o00);
        C0096O00oOooo.O000000o(context, i).O00000o0(o0000o00);
        return o0000o00;
    }

    @Deprecated
    public static O00000Oo O000000o(Context context, String str, O000o00 o000o00) {
        O0000o00 o0000o00 = new O0000o00(o000o00);
        C0096O00oOooo.O00000Oo(context, str).O00000o0(o0000o00);
        return o0000o00;
    }

    @Deprecated
    public static O00000Oo O000000o(O00000Oo o00000Oo, O000o00 o000o00) {
        O0000o00 o0000o00 = new O0000o00(o000o00);
        C0096O00oOooo.O000000o(o00000Oo, (String) null).O00000o0(o0000o00);
        return o0000o00;
    }

    @Deprecated
    public static O00000Oo O000000o(InputStream inputStream, O000o00 o000o00) {
        O0000o00 o0000o00 = new O0000o00(o000o00);
        C0096O00oOooo.O00000Oo(inputStream, (String) null).O00000o0(o0000o00);
        return o0000o00;
    }

    @Deprecated
    public static O00000Oo O000000o(String str, O000o00 o000o00) {
        O0000o00 o0000o00 = new O0000o00(o000o00);
        C0096O00oOooo.O00000o0(str, (String) null).O00000o0(o0000o00);
        return o0000o00;
    }

    @WorkerThread
    @Deprecated
    @Nullable
    public static C0064O0000o0O O000000o(Context context, String str) {
        return (C0064O0000o0O) C0096O00oOooo.O00000o0(context, str).getValue();
    }

    @WorkerThread
    @Deprecated
    @Nullable
    public static C0064O0000o0O O000000o(Resources resources, JSONObject jSONObject) {
        return (C0064O0000o0O) C0096O00oOooo.O00000Oo(jSONObject, (String) null).getValue();
    }

    @WorkerThread
    @Deprecated
    @Nullable
    public static C0064O0000o0O O000000o(O00000Oo o00000Oo) {
        return (C0064O0000o0O) C0096O00oOooo.O00000Oo(o00000Oo, (String) null).getValue();
    }

    @WorkerThread
    @Deprecated
    @Nullable
    public static C0064O0000o0O O000000o(InputStream inputStream) {
        return (C0064O0000o0O) C0096O00oOooo.O00000o0(inputStream, (String) null).getValue();
    }

    @WorkerThread
    @Deprecated
    @Nullable
    public static C0064O0000o0O O000000o(InputStream inputStream, boolean z) {
        if (z) {
            O00000o.warning("Lottie now auto-closes input stream!");
        }
        return (C0064O0000o0O) C0096O00oOooo.O00000o0(inputStream, (String) null).getValue();
    }

    @WorkerThread
    @Deprecated
    @Nullable
    public static C0064O0000o0O O0000oo0(String str) {
        return (C0064O0000o0O) C0096O00oOooo.O00000o(str, (String) null).getValue();
    }
}

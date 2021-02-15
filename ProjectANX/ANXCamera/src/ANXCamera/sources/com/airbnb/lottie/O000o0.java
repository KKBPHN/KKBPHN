package com.airbnb.lottie;

import android.util.Log;
import androidx.collection.ArraySet;
import androidx.core.util.Pair;
import com.airbnb.lottie.O00000o.C0033O00000oo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class O000o0 {
    private final Set O00O0O0o = new ArraySet();
    private final Map O00O0OO = new HashMap();
    private final Comparator O00O0OOo = new C0090O000o00O(this);
    private boolean enabled = false;

    public void O000000o(C0091O000o00o o000o00o) {
        this.O00O0O0o.add(o000o00o);
    }

    public void O000000o(String str, float f) {
        if (this.enabled) {
            C0033O00000oo o00000oo = (C0033O00000oo) this.O00O0OO.get(str);
            if (o00000oo == null) {
                o00000oo = new C0033O00000oo();
                this.O00O0OO.put(str, o00000oo);
            }
            o00000oo.add(f);
            if (str.equals("__container")) {
                for (C0091O000o00o O00000o02 : this.O00O0O0o) {
                    O00000o02.O00000o0(f);
                }
            }
        }
    }

    public void O00000Oo(C0091O000o00o o000o00o) {
        this.O00O0O0o.remove(o000o00o);
    }

    public void O00O0ooO() {
        this.O00O0OO.clear();
    }

    public List O00O0ooo() {
        if (!this.enabled) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(this.O00O0OO.size());
        for (Entry entry : this.O00O0OO.entrySet()) {
            arrayList.add(new Pair(entry.getKey(), Float.valueOf(((C0033O00000oo) entry.getValue()).getMean())));
        }
        Collections.sort(arrayList, this.O00O0OOo);
        return arrayList;
    }

    public void O00OO0O() {
        if (this.enabled) {
            List O00O0ooo = O00O0ooo();
            String str = C0053O00000oO.TAG;
            Log.d(str, "Render times:");
            for (int i = 0; i < O00O0ooo.size(); i++) {
                Pair pair = (Pair) O00O0ooo.get(i);
                Log.d(str, String.format("\t\t%30s:%.2f", new Object[]{pair.first, pair.second}));
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setEnabled(boolean z) {
        this.enabled = z;
    }
}

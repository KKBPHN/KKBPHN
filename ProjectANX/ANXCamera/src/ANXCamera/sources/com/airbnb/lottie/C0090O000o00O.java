package com.airbnb.lottie;

import androidx.core.util.Pair;
import java.util.Comparator;

/* renamed from: com.airbnb.lottie.O000o00O reason: case insensitive filesystem */
class C0090O000o00O implements Comparator {
    final /* synthetic */ O000o0 this$0;

    C0090O000o00O(O000o0 o000o0) {
        this.this$0 = o000o0;
    }

    /* renamed from: O000000o */
    public int compare(Pair pair, Pair pair2) {
        float floatValue = ((Float) pair.second).floatValue();
        float floatValue2 = ((Float) pair2.second).floatValue();
        if (floatValue2 > floatValue) {
            return 1;
        }
        return floatValue > floatValue2 ? -1 : 0;
    }
}

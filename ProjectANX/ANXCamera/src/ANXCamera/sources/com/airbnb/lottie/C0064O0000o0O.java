package com.airbnb.lottie;

import android.graphics.Rect;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.model.O0000OOo;
import com.airbnb.lottie.model.layer.O0000O0o;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* renamed from: com.airbnb.lottie.O0000o0O reason: case insensitive filesystem */
public class C0064O0000o0O {
    private List O000o;
    private final O000o0 O000o0Oo = new O000o0();
    private Map O000o0o;
    private final HashSet O000o0o0 = new HashSet();
    private Map O000o0oo;
    private float O000oO;
    private LongSparseArray O000oO0;
    private SparseArrayCompat O000oO00;
    private float O000oO0O;
    private float O000oO0o;
    private int O000oOO = 0;
    private boolean O000oOO0;
    private Rect bounds;
    private Map images;
    private List layers;

    @RestrictTo({Scope.LIBRARY})
    public O0000O0o O000000o(long j) {
        return (O0000O0o) this.O000oO0.get(j);
    }

    @RestrictTo({Scope.LIBRARY})
    public void O000000o(Rect rect, float f, float f2, float f3, List list, LongSparseArray longSparseArray, Map map, Map map2, SparseArrayCompat sparseArrayCompat, Map map3, List list2) {
        this.bounds = rect;
        this.O000oO0O = f;
        this.O000oO0o = f2;
        this.O000oO = f3;
        this.layers = list;
        this.O000oO0 = longSparseArray;
        this.O000o0o = map;
        this.images = map2;
        this.O000oO00 = sparseArrayCompat;
        this.O000o0oo = map3;
        this.O000o = list2;
    }

    public void O0000Oo0(boolean z) {
        this.O000o0Oo.setEnabled(z);
    }

    @RestrictTo({Scope.LIBRARY})
    public void O0000Ooo(int i) {
        this.O000oOO += i;
    }

    @RestrictTo({Scope.LIBRARY})
    public void O0000oO0(boolean z) {
        this.O000oOO0 = z;
    }

    @RestrictTo({Scope.LIBRARY})
    public void O0000oo(String str) {
        O00000o.warning(str);
        this.O000o0o0.add(str);
    }

    @Nullable
    public O0000OOo O0000ooO(String str) {
        this.O000o.size();
        for (int i = 0; i < this.O000o.size(); i++) {
            O0000OOo o0000OOo = (O0000OOo) this.O000o.get(i);
            if (o0000OOo.O00oOoOo(str)) {
                return o0000OOo;
            }
        }
        return null;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY})
    public List O0000ooo(String str) {
        return (List) this.O000o0o.get(str);
    }

    public List O00O0o() {
        return this.O000o;
    }

    public float O00O0o0() {
        return this.O000oO0o - this.O000oO0O;
    }

    @RestrictTo({Scope.LIBRARY})
    public float O00O0o0O() {
        return this.O000oO0o;
    }

    public Map O00O0o0o() {
        return this.images;
    }

    @RestrictTo({Scope.LIBRARY})
    public int O00O0oO0() {
        return this.O000oOO;
    }

    @RestrictTo({Scope.LIBRARY})
    public float O00O0oOO() {
        return this.O000oO0O;
    }

    @RestrictTo({Scope.LIBRARY})
    public boolean O00O0oOo() {
        return this.O000oOO0;
    }

    public boolean O00O0oo0() {
        return !this.images.isEmpty();
    }

    public O000o0 O00oOoOo() {
        return this.O000o0Oo;
    }

    public Rect getBounds() {
        return this.bounds;
    }

    public SparseArrayCompat getCharacters() {
        return this.O000oO00;
    }

    public float getDuration() {
        return (float) ((long) ((O00O0o0() / this.O000oO) * 1000.0f));
    }

    public Map getFonts() {
        return this.O000o0oo;
    }

    public float getFrameRate() {
        return this.O000oO;
    }

    public List getLayers() {
        return this.layers;
    }

    public ArrayList getWarnings() {
        HashSet hashSet = this.O000o0o0;
        return new ArrayList(Arrays.asList(hashSet.toArray(new String[hashSet.size()])));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("LottieComposition:\n");
        for (O0000O0o o0000O0o : this.layers) {
            sb.append(o0000O0o.toString("\t"));
        }
        return sb.toString();
    }
}

package com.airbnb.lottie.model.layer;

import androidx.annotation.Nullable;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.C0099O0000OoO;
import com.airbnb.lottie.model.O000000o.C0100O0000Ooo;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.O000000o.O0000Oo;
import java.util.List;
import java.util.Locale;

public class O0000O0o {
    private final C0064O0000o0O O00000oo;
    private final float O000oO0O;
    private final String O00O000o;
    private final C0100O0000Ooo O00Ooo0O;
    private final List O00o0OO0;
    private final List O00oO00O;
    @Nullable
    private final O00000Oo O00ooO0O;
    private final long O00oooOO;
    @Nullable
    private final String O00oooOo;
    private final int O00oooo;
    private final int O00oooo0;
    private final float O00ooooo;
    @Nullable
    private final C0099O0000OoO O0O000o;
    private final List O0O00OO;
    private final Layer$MatteType O0O00Oo;
    private final int O0O00oO;
    private final boolean hidden;
    private final Layer$LayerType layerType;
    private final int ooooooo;
    private final long parentId;
    private final int solidColor;
    @Nullable
    private final O0000Oo text;

    public O0000O0o(List list, C0064O0000o0O o0000o0O, String str, long j, Layer$LayerType layer$LayerType, long j2, @Nullable String str2, List list2, C0100O0000Ooo o0000Ooo, int i, int i2, int i3, float f, float f2, int i4, int i5, @Nullable O0000Oo o0000Oo, @Nullable C0099O0000OoO o0000OoO, List list3, Layer$MatteType layer$MatteType, @Nullable O00000Oo o00000Oo, boolean z) {
        this.O00oO00O = list;
        this.O00000oo = o0000o0O;
        this.O00O000o = str;
        this.O00oooOO = j;
        this.layerType = layer$LayerType;
        this.parentId = j2;
        this.O00oooOo = str2;
        this.O00o0OO0 = list2;
        this.O00Ooo0O = o0000Ooo;
        this.O00oooo0 = i;
        this.O00oooo = i2;
        this.solidColor = i3;
        this.O00ooooo = f;
        this.O000oO0O = f2;
        this.ooooooo = i4;
        this.O0O00oO = i5;
        this.text = o0000Oo;
        this.O0O000o = o0000OoO;
        this.O0O00OO = list3;
        this.O0O00Oo = layer$MatteType;
        this.O00ooO0O = o00000Oo;
        this.hidden = z;
    }

    /* access modifiers changed from: 0000 */
    public C0064O0000o0O O000O0OO() {
        return this.O00000oo;
    }

    /* access modifiers changed from: 0000 */
    public List O00Oo() {
        return this.O00oO00O;
    }

    /* access modifiers changed from: 0000 */
    public List O00Oo0() {
        return this.O00o0OO0;
    }

    /* access modifiers changed from: 0000 */
    public float O00o0() {
        return this.O00ooooo;
    }

    /* access modifiers changed from: 0000 */
    public Layer$MatteType O00o00() {
        return this.O0O00Oo;
    }

    /* access modifiers changed from: 0000 */
    public List O00o000o() {
        return this.O0O00OO;
    }

    /* access modifiers changed from: 0000 */
    public int O00o00O() {
        return this.ooooooo;
    }

    /* access modifiers changed from: 0000 */
    public int O00o00O0() {
        return this.O0O00oO;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String O00o00OO() {
        return this.O00oooOo;
    }

    /* access modifiers changed from: 0000 */
    public int O00o00Oo() {
        return this.O00oooo;
    }

    /* access modifiers changed from: 0000 */
    public float O00o00o() {
        return this.O000oO0O / this.O00000oo.O00O0o0();
    }

    /* access modifiers changed from: 0000 */
    public int O00o00o0() {
        return this.O00oooo0;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public C0099O0000OoO O00o00oO() {
        return this.O0O000o;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public O00000Oo O00o00oo() {
        return this.O00ooO0O;
    }

    public long getId() {
        return this.O00oooOO;
    }

    public Layer$LayerType getLayerType() {
        return this.layerType;
    }

    /* access modifiers changed from: 0000 */
    public String getName() {
        return this.O00O000o;
    }

    /* access modifiers changed from: 0000 */
    public long getParentId() {
        return this.parentId;
    }

    /* access modifiers changed from: 0000 */
    public int getSolidColor() {
        return this.solidColor;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public O0000Oo getText() {
        return this.text;
    }

    /* access modifiers changed from: 0000 */
    public C0100O0000Ooo getTransform() {
        return this.O00Ooo0O;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(getName());
        String str2 = "\n";
        sb.append(str2);
        O0000O0o O000000o2 = this.O00000oo.O000000o(getParentId());
        if (O000000o2 != null) {
            String str3 = "\t\tParents: ";
            while (true) {
                sb.append(str3);
                sb.append(O000000o2.getName());
                O000000o2 = this.O00000oo.O000000o(O000000o2.getParentId());
                if (O000000o2 == null) {
                    break;
                }
                str3 = "->";
            }
            sb.append(str);
            sb.append(str2);
        }
        if (!O00Oo0().isEmpty()) {
            sb.append(str);
            sb.append("\tMasks: ");
            sb.append(O00Oo0().size());
            sb.append(str2);
        }
        if (!(O00o00o0() == 0 || O00o00Oo() == 0)) {
            sb.append(str);
            sb.append("\tBackground: ");
            sb.append(String.format(Locale.US, "%dx%d %X\n", new Object[]{Integer.valueOf(O00o00o0()), Integer.valueOf(O00o00Oo()), Integer.valueOf(getSolidColor())}));
        }
        if (!this.O00oO00O.isEmpty()) {
            sb.append(str);
            sb.append("\tShapes:\n");
            for (Object next : this.O00oO00O) {
                sb.append(str);
                sb.append("\t\t");
                sb.append(next);
                sb.append(str2);
            }
        }
        return sb.toString();
    }
}

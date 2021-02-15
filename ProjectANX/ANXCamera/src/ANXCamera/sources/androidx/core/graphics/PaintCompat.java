package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build.VERSION;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.core.util.Pair;

public final class PaintCompat {
    private static final String EM_STRING = "m";
    private static final String TOFU_STRING = "󟿽";
    private static final ThreadLocal sRectThreadLocal = new ThreadLocal();

    /* renamed from: androidx.core.graphics.PaintCompat$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$core$graphics$BlendModeCompat = new int[BlendModeCompat.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(58:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|(3:57|58|60)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(60:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|60) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x007a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0086 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0092 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x009e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00b6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00c2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00ce */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00da */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00e6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00f2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00fe */
        /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x010a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x0116 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x0122 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x012e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x013a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x0146 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x0152 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.CLEAR.ordinal()] = 1;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.SRC.ordinal()] = 2;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.DST.ordinal()] = 3;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.SRC_OVER.ordinal()] = 4;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.DST_OVER.ordinal()] = 5;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.SRC_IN.ordinal()] = 6;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.DST_IN.ordinal()] = 7;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.SRC_OUT.ordinal()] = 8;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.DST_OUT.ordinal()] = 9;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.SRC_ATOP.ordinal()] = 10;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.DST_ATOP.ordinal()] = 11;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.XOR.ordinal()] = 12;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.PLUS.ordinal()] = 13;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.MODULATE.ordinal()] = 14;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.SCREEN.ordinal()] = 15;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.OVERLAY.ordinal()] = 16;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.DARKEN.ordinal()] = 17;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.LIGHTEN.ordinal()] = 18;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.COLOR_DODGE.ordinal()] = 19;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.COLOR_BURN.ordinal()] = 20;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.HARD_LIGHT.ordinal()] = 21;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.SOFT_LIGHT.ordinal()] = 22;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.DIFFERENCE.ordinal()] = 23;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.EXCLUSION.ordinal()] = 24;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.MULTIPLY.ordinal()] = 25;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.HUE.ordinal()] = 26;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.SATURATION.ordinal()] = 27;
            $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.COLOR.ordinal()] = 28;
            try {
                $SwitchMap$androidx$core$graphics$BlendModeCompat[BlendModeCompat.LUMINOSITY.ordinal()] = 29;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private PaintCompat() {
    }

    public static boolean hasGlyph(@NonNull Paint paint, @NonNull String str) {
        if (VERSION.SDK_INT >= 23) {
            return paint.hasGlyph(str);
        }
        int length = str.length();
        if (length == 1 && Character.isWhitespace(str.charAt(0))) {
            return true;
        }
        String str2 = TOFU_STRING;
        float measureText = paint.measureText(str2);
        float measureText2 = paint.measureText("m");
        float measureText3 = paint.measureText(str);
        float f = 0.0f;
        if (measureText3 == 0.0f) {
            return false;
        }
        if (str.codePointCount(0, str.length()) > 1) {
            if (measureText3 > measureText2 * 2.0f) {
                return false;
            }
            int i = 0;
            while (i < length) {
                int charCount = Character.charCount(str.codePointAt(i)) + i;
                f += paint.measureText(str, i, charCount);
                i = charCount;
            }
            if (measureText3 >= f) {
                return false;
            }
        }
        if (measureText3 != measureText) {
            return true;
        }
        Pair obtainEmptyRects = obtainEmptyRects();
        paint.getTextBounds(str2, 0, 2, (Rect) obtainEmptyRects.first);
        paint.getTextBounds(str, 0, length, (Rect) obtainEmptyRects.second);
        return !((Rect) obtainEmptyRects.first).equals(obtainEmptyRects.second);
    }

    @RequiresApi(29)
    @VisibleForTesting
    @Nullable
    static BlendMode obtainBlendModeFromCompat(@NonNull BlendModeCompat blendModeCompat) {
        switch (AnonymousClass1.$SwitchMap$androidx$core$graphics$BlendModeCompat[blendModeCompat.ordinal()]) {
            case 1:
                return BlendMode.CLEAR;
            case 2:
                return BlendMode.SRC;
            case 3:
                return BlendMode.DST;
            case 4:
                return BlendMode.SRC_OVER;
            case 5:
                return BlendMode.SRC_OVER;
            case 6:
                return BlendMode.SRC_IN;
            case 7:
                return BlendMode.DST_IN;
            case 8:
                return BlendMode.SRC_OUT;
            case 9:
                return BlendMode.DST_OUT;
            case 10:
                return BlendMode.SRC_ATOP;
            case 11:
                return BlendMode.DST_ATOP;
            case 12:
                return BlendMode.XOR;
            case 13:
                return BlendMode.PLUS;
            case 14:
                return BlendMode.MODULATE;
            case 15:
                return BlendMode.SCREEN;
            case 16:
                return BlendMode.OVERLAY;
            case 17:
                return BlendMode.DARKEN;
            case 18:
                return BlendMode.LIGHTEN;
            case 19:
                return BlendMode.COLOR_DODGE;
            case 20:
                return BlendMode.COLOR_BURN;
            case 21:
                return BlendMode.HARD_LIGHT;
            case 22:
                return BlendMode.SOFT_LIGHT;
            case 23:
                return BlendMode.DIFFERENCE;
            case 24:
                return BlendMode.EXCLUSION;
            case 25:
                return BlendMode.MULTIPLY;
            case 26:
                return BlendMode.HUE;
            case 27:
                return BlendMode.SATURATION;
            case 28:
                return BlendMode.COLOR;
            case 29:
                return BlendMode.LUMINOSITY;
            default:
                return null;
        }
    }

    private static Pair obtainEmptyRects() {
        Pair pair = (Pair) sRectThreadLocal.get();
        if (pair == null) {
            Pair pair2 = new Pair(new Rect(), new Rect());
            sRectThreadLocal.set(pair2);
            return pair2;
        }
        ((Rect) pair.first).setEmpty();
        ((Rect) pair.second).setEmpty();
        return pair;
    }

    @VisibleForTesting
    @Nullable
    static Mode obtainPorterDuffFromCompat(@Nullable BlendModeCompat blendModeCompat) {
        if (blendModeCompat == null) {
            return null;
        }
        switch (AnonymousClass1.$SwitchMap$androidx$core$graphics$BlendModeCompat[blendModeCompat.ordinal()]) {
            case 1:
                return Mode.CLEAR;
            case 2:
                return Mode.SRC;
            case 3:
                return Mode.DST;
            case 4:
                return Mode.SRC_OVER;
            case 5:
                return Mode.DST_OVER;
            case 6:
                return Mode.SRC_IN;
            case 7:
                return Mode.DST_IN;
            case 8:
                return Mode.SRC_OUT;
            case 9:
                return Mode.DST_OUT;
            case 10:
                return Mode.SRC_ATOP;
            case 11:
                return Mode.DST_ATOP;
            case 12:
                return Mode.XOR;
            case 13:
                return Mode.ADD;
            case 14:
                return Mode.MULTIPLY;
            case 15:
                return Mode.SCREEN;
            case 16:
                return Mode.OVERLAY;
            case 17:
                return Mode.DARKEN;
            case 18:
                return Mode.LIGHTEN;
            default:
                return null;
        }
    }

    public static boolean setBlendMode(@NonNull Paint paint, @Nullable BlendModeCompat blendModeCompat) {
        boolean z = true;
        BlendMode blendMode = null;
        if (VERSION.SDK_INT >= 29) {
            if (blendModeCompat != null) {
                blendMode = obtainBlendModeFromCompat(blendModeCompat);
            }
            paint.setBlendMode(blendMode);
            return true;
        } else if (blendModeCompat != null) {
            Mode obtainPorterDuffFromCompat = obtainPorterDuffFromCompat(blendModeCompat);
            if (obtainPorterDuffFromCompat != null) {
                blendMode = new PorterDuffXfermode(obtainPorterDuffFromCompat);
            }
            paint.setXfermode(blendMode);
            if (obtainPorterDuffFromCompat == null) {
                z = false;
            }
            return z;
        } else {
            paint.setXfermode(null);
            return true;
        }
    }

    public static boolean setBlendModeColorFilter(@NonNull Paint paint, @ColorInt int i, @Nullable BlendModeCompat blendModeCompat) {
        boolean z = true;
        PorterDuffColorFilter porterDuffColorFilter = null;
        if (VERSION.SDK_INT >= 29) {
            BlendMode obtainBlendModeFromCompat = obtainBlendModeFromCompat(blendModeCompat);
            if (obtainBlendModeFromCompat != null) {
                paint.setColorFilter(new BlendModeColorFilter(i, obtainBlendModeFromCompat));
            } else {
                paint.setColorFilter(null);
            }
            return true;
        } else if (blendModeCompat != null) {
            Mode obtainPorterDuffFromCompat = obtainPorterDuffFromCompat(blendModeCompat);
            if (obtainPorterDuffFromCompat != null) {
                porterDuffColorFilter = new PorterDuffColorFilter(i, obtainPorterDuffFromCompat);
            }
            paint.setColorFilter(porterDuffColorFilter);
            if (obtainPorterDuffFromCompat == null) {
                z = false;
            }
            return z;
        } else {
            paint.setColorFilter(null);
            return true;
        }
    }
}

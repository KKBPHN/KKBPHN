package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Xml;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.R;
import com.android.camera.statistic.MistatsConstants.Manual;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
final class GradientColorInflaterCompat {
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;

    final class ColorStops {
        final int[] mColors;
        final float[] mOffsets;

        ColorStops(@ColorInt int i, @ColorInt int i2) {
            this.mColors = new int[]{i, i2};
            this.mOffsets = new float[]{0.0f, 1.0f};
        }

        ColorStops(@ColorInt int i, @ColorInt int i2, @ColorInt int i3) {
            this.mColors = new int[]{i, i2, i3};
            this.mOffsets = new float[]{0.0f, 0.5f, 1.0f};
        }

        ColorStops(@NonNull List list, @NonNull List list2) {
            int size = list.size();
            this.mColors = new int[size];
            this.mOffsets = new float[size];
            for (int i = 0; i < size; i++) {
                this.mColors[i] = ((Integer) list.get(i)).intValue();
                this.mOffsets[i] = ((Float) list2.get(i)).floatValue();
            }
        }
    }

    private GradientColorInflaterCompat() {
    }

    private static ColorStops checkColors(@Nullable ColorStops colorStops, @ColorInt int i, @ColorInt int i2, boolean z, @ColorInt int i3) {
        ColorStops colorStops2;
        if (colorStops != null) {
            return colorStops;
        }
        if (z) {
            colorStops2 = new ColorStops(i, i3, i2);
            return colorStops2;
        }
        colorStops2 = new ColorStops(i, i2);
        return colorStops2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0011  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0016  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static Shader createFromXml(@NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @Nullable Theme theme) {
        int next;
        AttributeSet asAttributeSet = Xml.asAttributeSet(xmlPullParser);
        while (true) {
            next = xmlPullParser.next();
            if (next == 2 || next == 1) {
                if (next != 2) {
                    return createFromXmlInner(resources, xmlPullParser, asAttributeSet, theme);
                }
                throw new XmlPullParserException("No start tag found");
            }
        }
        if (next != 2) {
        }
    }

    static Shader createFromXmlInner(@NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Theme theme) {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        String name = xmlPullParser.getName();
        if (name.equals(Manual.GRADIENT)) {
            Theme theme2 = theme;
            TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme2, attributeSet, R.styleable.GradientColor);
            float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "startX", R.styleable.GradientColor_android_startX, 0.0f);
            float namedFloat2 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "startY", R.styleable.GradientColor_android_startY, 0.0f);
            float namedFloat3 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "endX", R.styleable.GradientColor_android_endX, 0.0f);
            float namedFloat4 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "endY", R.styleable.GradientColor_android_endY, 0.0f);
            float namedFloat5 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "centerX", R.styleable.GradientColor_android_centerX, 0.0f);
            float namedFloat6 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "centerY", R.styleable.GradientColor_android_centerY, 0.0f);
            int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser2, "type", R.styleable.GradientColor_android_type, 0);
            int namedColor = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser2, "startColor", R.styleable.GradientColor_android_startColor, 0);
            String str = "centerColor";
            boolean hasAttribute = TypedArrayUtils.hasAttribute(xmlPullParser2, str);
            int namedColor2 = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser2, str, R.styleable.GradientColor_android_centerColor, 0);
            int namedColor3 = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser2, "endColor", R.styleable.GradientColor_android_endColor, 0);
            int namedInt2 = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser2, "tileMode", R.styleable.GradientColor_android_tileMode, 0);
            float f = namedFloat5;
            float namedFloat7 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser2, "gradientRadius", R.styleable.GradientColor_android_gradientRadius, 0.0f);
            obtainAttributes.recycle();
            ColorStops checkColors = checkColors(inflateChildElements(resources, xmlPullParser, attributeSet, theme), namedColor, namedColor3, hasAttribute, namedColor2);
            if (namedInt == 1) {
                float f2 = f;
                if (namedFloat7 > 0.0f) {
                    int[] iArr = checkColors.mColors;
                    RadialGradient radialGradient = new RadialGradient(f2, namedFloat6, namedFloat7, iArr, checkColors.mOffsets, parseTileMode(namedInt2));
                    return radialGradient;
                }
                throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
            } else if (namedInt != 2) {
                LinearGradient linearGradient = new LinearGradient(namedFloat, namedFloat2, namedFloat3, namedFloat4, checkColors.mColors, checkColors.mOffsets, parseTileMode(namedInt2));
                return linearGradient;
            } else {
                return new SweepGradient(f, namedFloat6, checkColors.mColors, checkColors.mOffsets);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(xmlPullParser.getPositionDescription());
            sb.append(": invalid gradient color tag ");
            sb.append(name);
            throw new XmlPullParserException(sb.toString());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0084, code lost:
        throw new org.xmlpull.v1.XmlPullParserException(r10.toString());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static ColorStops inflateChildElements(@NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Theme theme) {
        int depth = xmlPullParser.getDepth() + 1;
        ArrayList arrayList = new ArrayList(20);
        ArrayList arrayList2 = new ArrayList(20);
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                break;
            }
            int depth2 = xmlPullParser.getDepth();
            if (depth2 < depth && next == 3) {
                break;
            } else if (next == 2) {
                if (depth2 > depth) {
                    continue;
                } else if (xmlPullParser.getName().equals("item")) {
                    TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, R.styleable.GradientColorItem);
                    boolean hasValue = obtainAttributes.hasValue(R.styleable.GradientColorItem_android_color);
                    boolean hasValue2 = obtainAttributes.hasValue(R.styleable.GradientColorItem_android_offset);
                    if (!hasValue || !hasValue2) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(xmlPullParser.getPositionDescription());
                        sb.append(": <item> tag requires a 'color' attribute and a 'offset' attribute!");
                    } else {
                        int color = obtainAttributes.getColor(R.styleable.GradientColorItem_android_color, 0);
                        float f = obtainAttributes.getFloat(R.styleable.GradientColorItem_android_offset, 0.0f);
                        obtainAttributes.recycle();
                        arrayList2.add(Integer.valueOf(color));
                        arrayList.add(Float.valueOf(f));
                    }
                }
            }
        }
        if (arrayList2.size() > 0) {
            return new ColorStops((List) arrayList2, (List) arrayList);
        }
        return null;
    }

    private static TileMode parseTileMode(int i) {
        return i != 1 ? i != 2 ? TileMode.CLAMP : TileMode.MIRROR : TileMode.REPEAT;
    }
}

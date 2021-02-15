package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.XmlRes;
import androidx.core.R;
import androidx.core.view.ViewCompat;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public final class ColorStateListInflaterCompat {
    private ColorStateListInflaterCompat() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0011  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0016  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @NonNull
    public static ColorStateList createFromXml(@NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @Nullable Theme theme) {
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

    @NonNull
    public static ColorStateList createFromXmlInner(@NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Theme theme) {
        String name = xmlPullParser.getName();
        if (name.equals("selector")) {
            return inflate(resources, xmlPullParser, attributeSet, theme);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(xmlPullParser.getPositionDescription());
        sb.append(": invalid color state list tag ");
        sb.append(name);
        throw new XmlPullParserException(sb.toString());
    }

    @Nullable
    public static ColorStateList inflate(@NonNull Resources resources, @XmlRes int i, @Nullable Theme theme) {
        try {
            return createFromXml(resources, resources.getXml(i), theme);
        } catch (Exception e) {
            Log.e("CSLCompat", "Failed to inflate ColorStateList.", e);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x006f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static ColorStateList inflate(@NonNull Resources resources, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Theme theme) {
        int attributeCount;
        int i;
        int i2;
        AttributeSet attributeSet2 = attributeSet;
        int i3 = 1;
        int depth = xmlPullParser.getDepth() + 1;
        int[][] iArr = new int[20][];
        int[] iArr2 = new int[iArr.length];
        int i4 = 0;
        while (true) {
            int next = xmlPullParser.next();
            if (next == i3) {
                break;
            }
            int depth2 = xmlPullParser.getDepth();
            if (depth2 < depth && next == 3) {
                break;
            }
            if (next != 2 || depth2 > depth || !xmlPullParser.getName().equals("item")) {
                Resources resources2 = resources;
                Theme theme2 = theme;
            } else {
                TypedArray obtainAttributes = obtainAttributes(resources, theme, attributeSet2, R.styleable.ColorStateListItem);
                int color = obtainAttributes.getColor(R.styleable.ColorStateListItem_android_color, -65281);
                float f = 1.0f;
                if (obtainAttributes.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
                    i2 = R.styleable.ColorStateListItem_android_alpha;
                } else {
                    if (obtainAttributes.hasValue(R.styleable.ColorStateListItem_alpha)) {
                        i2 = R.styleable.ColorStateListItem_alpha;
                    }
                    obtainAttributes.recycle();
                    attributeCount = attributeSet.getAttributeCount();
                    int[] iArr3 = new int[attributeCount];
                    int i5 = 0;
                    for (i = 0; i < attributeCount; i++) {
                        int attributeNameResource = attributeSet2.getAttributeNameResource(i);
                        if (!(attributeNameResource == 16843173 || attributeNameResource == 16843551 || attributeNameResource == R.attr.alpha)) {
                            int i6 = i5 + 1;
                            if (!attributeSet2.getAttributeBooleanValue(i, false)) {
                                attributeNameResource = -attributeNameResource;
                            }
                            iArr3[i5] = attributeNameResource;
                            i5 = i6;
                        }
                    }
                    int[] trimStateSet = StateSet.trimStateSet(iArr3, i5);
                    iArr2 = GrowingArrayUtils.append(iArr2, i4, modulateColorAlpha(color, f));
                    iArr = (int[][]) GrowingArrayUtils.append((Object[]) iArr, i4, (Object) trimStateSet);
                    i4++;
                }
                f = obtainAttributes.getFloat(i2, 1.0f);
                obtainAttributes.recycle();
                attributeCount = attributeSet.getAttributeCount();
                int[] iArr32 = new int[attributeCount];
                int i52 = 0;
                while (i < attributeCount) {
                }
                int[] trimStateSet2 = StateSet.trimStateSet(iArr32, i52);
                iArr2 = GrowingArrayUtils.append(iArr2, i4, modulateColorAlpha(color, f));
                iArr = (int[][]) GrowingArrayUtils.append((Object[]) iArr, i4, (Object) trimStateSet2);
                i4++;
            }
            i3 = 1;
        }
        int[] iArr4 = new int[i4];
        int[][] iArr5 = new int[i4][];
        System.arraycopy(iArr2, 0, iArr4, 0, i4);
        System.arraycopy(iArr, 0, iArr5, 0, i4);
        return new ColorStateList(iArr5, iArr4);
    }

    @ColorInt
    private static int modulateColorAlpha(@ColorInt int i, @FloatRange(from = 0.0d, to = 1.0d) float f) {
        return (i & ViewCompat.MEASURED_SIZE_MASK) | (Math.round(((float) Color.alpha(i)) * f) << 24);
    }

    private static TypedArray obtainAttributes(Resources resources, Theme theme, AttributeSet attributeSet, int[] iArr) {
        return theme == null ? resources.obtainAttributes(attributeSet, iArr) : theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }
}

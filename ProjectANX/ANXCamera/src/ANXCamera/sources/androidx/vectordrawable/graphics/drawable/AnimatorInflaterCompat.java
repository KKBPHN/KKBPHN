package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import androidx.annotation.AnimatorRes;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import androidx.core.graphics.PathParser.PathDataNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class AnimatorInflaterCompat {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;

    class PathDataEvaluator implements TypeEvaluator {
        private PathDataNode[] mNodeArray;

        PathDataEvaluator() {
        }

        PathDataEvaluator(PathDataNode[] pathDataNodeArr) {
            this.mNodeArray = pathDataNodeArr;
        }

        public PathDataNode[] evaluate(float f, PathDataNode[] pathDataNodeArr, PathDataNode[] pathDataNodeArr2) {
            if (PathParser.canMorph(pathDataNodeArr, pathDataNodeArr2)) {
                if (!PathParser.canMorph(this.mNodeArray, pathDataNodeArr)) {
                    this.mNodeArray = PathParser.deepCopyNodes(pathDataNodeArr);
                }
                for (int i = 0; i < pathDataNodeArr.length; i++) {
                    this.mNodeArray[i].interpolatePathDataNode(pathDataNodeArr[i], pathDataNodeArr2[i], f);
                }
                return this.mNodeArray;
            }
            throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
        }
    }

    private AnimatorInflaterCompat() {
    }

    private static Animator createAnimatorFromXml(Context context, Resources resources, Theme theme, XmlPullParser xmlPullParser, float f) {
        return createAnimatorFromXml(context, resources, theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser), null, 0, f);
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r15v0, types: [android.animation.AnimatorSet] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00b8  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Animator createAnimatorFromXml(Context context, Resources resources, Theme theme, XmlPullParser xmlPullParser, AttributeSet attributeSet, AnimatorSet animatorSet, int i, float f) {
        ValueAnimator valueAnimator;
        int i2;
        Resources resources2 = resources;
        Theme theme2 = theme;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        AnimatorSet animatorSet2 = animatorSet;
        int depth = xmlPullParser.getDepth();
        ArrayList arrayList = null;
        ValueAnimator valueAnimator2 = 0;
        while (true) {
            int next = xmlPullParser.next();
            i2 = 0;
            if ((next != 3 || xmlPullParser.getDepth() > depth) && next != 1) {
                if (next == 2) {
                    String name = xmlPullParser.getName();
                    if (name.equals("objectAnimator")) {
                        valueAnimator = loadObjectAnimator(context, resources, theme, attributeSet, f, xmlPullParser);
                    } else if (name.equals("animator")) {
                        valueAnimator = loadAnimator(context, resources, theme, attributeSet, null, f, xmlPullParser);
                    } else {
                        if (name.equals("set")) {
                            ? animatorSet3 = new AnimatorSet();
                            TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources2, theme2, attributeSet, AndroidResources.STYLEABLE_ANIMATOR_SET);
                            TypedArray typedArray = obtainAttributes;
                            createAnimatorFromXml(context, resources, theme, xmlPullParser, attributeSet, animatorSet3, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser2, "ordering", 0, 0), f);
                            typedArray.recycle();
                            Context context2 = context;
                            valueAnimator = animatorSet3;
                        } else if (name.equals("propertyValuesHolder")) {
                            PropertyValuesHolder[] loadValues = loadValues(context, resources2, theme2, xmlPullParser2, Xml.asAttributeSet(xmlPullParser));
                            if (loadValues != null && (valueAnimator2 instanceof ValueAnimator)) {
                                valueAnimator2.setValues(loadValues);
                            }
                            i2 = 1;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Unknown animator name: ");
                            sb.append(xmlPullParser.getName());
                            throw new RuntimeException(sb.toString());
                        }
                        if (animatorSet2 != null && i2 == 0) {
                            if (arrayList == null) {
                                arrayList = new ArrayList();
                            }
                            arrayList.add(valueAnimator2);
                        }
                    }
                    Context context3 = context;
                    if (arrayList == null) {
                    }
                    arrayList.add(valueAnimator2);
                }
            }
        }
        if (!(animatorSet2 == null || arrayList == null)) {
            Animator[] animatorArr = new Animator[arrayList.size()];
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                int i3 = i2 + 1;
                animatorArr[i2] = (Animator) it.next();
                i2 = i3;
            }
            if (i == 0) {
                animatorSet2.playTogether(animatorArr);
            } else {
                animatorSet2.playSequentially(animatorArr);
            }
        }
        return valueAnimator2;
    }

    private static Keyframe createNewKeyframe(Keyframe keyframe, float f) {
        return keyframe.getType() == Float.TYPE ? Keyframe.ofFloat(f) : keyframe.getType() == Integer.TYPE ? Keyframe.ofInt(f) : Keyframe.ofObject(f);
    }

    private static void distributeKeyframes(Keyframe[] keyframeArr, float f, int i, int i2) {
        float f2 = f / ((float) ((i2 - i) + 2));
        while (i <= i2) {
            keyframeArr[i].setFraction(keyframeArr[i - 1].getFraction() + f2);
            i++;
        }
    }

    private static void dumpKeyframes(Object[] objArr, String str) {
        if (objArr != null && objArr.length != 0) {
            String str2 = TAG;
            Log.d(str2, str);
            int length = objArr.length;
            for (int i = 0; i < length; i++) {
                Keyframe keyframe = objArr[i];
                StringBuilder sb = new StringBuilder();
                sb.append("Keyframe ");
                sb.append(i);
                sb.append(": fraction ");
                Object obj = "null";
                sb.append(keyframe.getFraction() < 0.0f ? obj : Float.valueOf(keyframe.getFraction()));
                sb.append(", , value : ");
                if (keyframe.hasValue()) {
                    obj = keyframe.getValue();
                }
                sb.append(obj);
                Log.d(str2, sb.toString());
            }
        }
    }

    private static PropertyValuesHolder getPVH(TypedArray typedArray, int i, int i2, int i3, String str) {
        PropertyValuesHolder propertyValuesHolder;
        Object[] objArr;
        TypedValue peekValue = typedArray.peekValue(i2);
        boolean z = peekValue != null;
        int i4 = z ? peekValue.type : 0;
        TypedValue peekValue2 = typedArray.peekValue(i3);
        boolean z2 = peekValue2 != null;
        int i5 = z2 ? peekValue2.type : 0;
        if (i == 4) {
            i = ((!z || !isColorType(i4)) && (!z2 || !isColorType(i5))) ? 0 : 3;
        }
        boolean z3 = i == 0;
        PropertyValuesHolder propertyValuesHolder2 = null;
        if (i == 2) {
            String string = typedArray.getString(i2);
            String string2 = typedArray.getString(i3);
            PathDataNode[] createNodesFromPathData = PathParser.createNodesFromPathData(string);
            PathDataNode[] createNodesFromPathData2 = PathParser.createNodesFromPathData(string2);
            if (createNodesFromPathData == null && createNodesFromPathData2 == null) {
                return null;
            }
            if (createNodesFromPathData != null) {
                PathDataEvaluator pathDataEvaluator = new PathDataEvaluator();
                if (createNodesFromPathData2 == null) {
                    objArr = new Object[]{createNodesFromPathData};
                } else if (PathParser.canMorph(createNodesFromPathData, createNodesFromPathData2)) {
                    objArr = new Object[]{createNodesFromPathData, createNodesFromPathData2};
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" Can't morph from ");
                    sb.append(string);
                    sb.append(" to ");
                    sb.append(string2);
                    throw new InflateException(sb.toString());
                }
                return PropertyValuesHolder.ofObject(str, pathDataEvaluator, objArr);
            } else if (createNodesFromPathData2 == null) {
                return null;
            } else {
                return PropertyValuesHolder.ofObject(str, new PathDataEvaluator(), new Object[]{createNodesFromPathData2});
            }
        } else {
            TypeEvaluator instance = i == 3 ? ArgbEvaluator.getInstance() : null;
            if (z3) {
                if (z) {
                    float dimension = i4 == 5 ? typedArray.getDimension(i2, 0.0f) : typedArray.getFloat(i2, 0.0f);
                    if (z2) {
                        propertyValuesHolder = PropertyValuesHolder.ofFloat(str, new float[]{dimension, i5 == 5 ? typedArray.getDimension(i3, 0.0f) : typedArray.getFloat(i3, 0.0f)});
                    } else {
                        propertyValuesHolder = PropertyValuesHolder.ofFloat(str, new float[]{dimension});
                    }
                } else {
                    propertyValuesHolder = PropertyValuesHolder.ofFloat(str, new float[]{i5 == 5 ? typedArray.getDimension(i3, 0.0f) : typedArray.getFloat(i3, 0.0f)});
                }
                propertyValuesHolder2 = propertyValuesHolder;
            } else if (z) {
                int i6 = i4 == 5 ? (int) typedArray.getDimension(i2, 0.0f) : isColorType(i4) ? typedArray.getColor(i2, 0) : typedArray.getInt(i2, 0);
                if (z2) {
                    int i7 = i5 == 5 ? (int) typedArray.getDimension(i3, 0.0f) : isColorType(i5) ? typedArray.getColor(i3, 0) : typedArray.getInt(i3, 0);
                    propertyValuesHolder2 = PropertyValuesHolder.ofInt(str, new int[]{i6, i7});
                } else {
                    propertyValuesHolder2 = PropertyValuesHolder.ofInt(str, new int[]{i6});
                }
            } else if (z2) {
                int i8 = i5 == 5 ? (int) typedArray.getDimension(i3, 0.0f) : isColorType(i5) ? typedArray.getColor(i3, 0) : typedArray.getInt(i3, 0);
                propertyValuesHolder2 = PropertyValuesHolder.ofInt(str, new int[]{i8});
            }
            if (propertyValuesHolder2 == null || instance == null) {
                return propertyValuesHolder2;
            }
            propertyValuesHolder2.setEvaluator(instance);
            return propertyValuesHolder2;
        }
    }

    private static int inferValueTypeFromValues(TypedArray typedArray, int i, int i2) {
        TypedValue peekValue = typedArray.peekValue(i);
        boolean z = true;
        boolean z2 = peekValue != null;
        int i3 = z2 ? peekValue.type : 0;
        TypedValue peekValue2 = typedArray.peekValue(i2);
        if (peekValue2 == null) {
            z = false;
        }
        return ((!z2 || !isColorType(i3)) && (!z || !isColorType(z ? peekValue2.type : 0))) ? 0 : 3;
    }

    private static int inferValueTypeOfKeyframe(Resources resources, Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_KEYFRAME);
        int i = 0;
        TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, xmlPullParser, "value", 0);
        if ((peekNamedValue != null) && isColorType(peekNamedValue.type)) {
            i = 3;
        }
        obtainAttributes.recycle();
        return i;
    }

    private static boolean isColorType(int i) {
        return i >= 28 && i <= 31;
    }

    public static Animator loadAnimator(Context context, @AnimatorRes int i) {
        return VERSION.SDK_INT >= 24 ? AnimatorInflater.loadAnimator(context, i) : loadAnimator(context, context.getResources(), context.getTheme(), i);
    }

    public static Animator loadAnimator(Context context, Resources resources, Theme theme, @AnimatorRes int i) {
        return loadAnimator(context, resources, theme, i, 1.0f);
    }

    public static Animator loadAnimator(Context context, Resources resources, Theme theme, @AnimatorRes int i, float f) {
        String str = "Can't load animation resource ID #0x";
        XmlResourceParser xmlResourceParser = null;
        try {
            xmlResourceParser = resources.getAnimation(i);
            Animator createAnimatorFromXml = createAnimatorFromXml(context, resources, theme, xmlResourceParser, f);
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            return createAnimatorFromXml;
        } catch (XmlPullParserException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(Integer.toHexString(i));
            NotFoundException notFoundException = new NotFoundException(sb.toString());
            notFoundException.initCause(e);
            throw notFoundException;
        } catch (IOException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(Integer.toHexString(i));
            NotFoundException notFoundException2 = new NotFoundException(sb2.toString());
            notFoundException2.initCause(e2);
            throw notFoundException2;
        } catch (Throwable th) {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            throw th;
        }
    }

    private static ValueAnimator loadAnimator(Context context, Resources resources, Theme theme, AttributeSet attributeSet, ValueAnimator valueAnimator, float f, XmlPullParser xmlPullParser) {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATOR);
        TypedArray obtainAttributes2 = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        if (valueAnimator == null) {
            valueAnimator = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(valueAnimator, obtainAttributes, obtainAttributes2, f, xmlPullParser);
        int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, xmlPullParser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            valueAnimator.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        if (obtainAttributes2 != null) {
            obtainAttributes2.recycle();
        }
        return valueAnimator;
    }

    private static Keyframe loadKeyframe(Context context, Resources resources, Theme theme, AttributeSet attributeSet, int i, XmlPullParser xmlPullParser) {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_KEYFRAME);
        float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "fraction", 3, -1.0f);
        String str = "value";
        TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, xmlPullParser, str, 0);
        boolean z = peekNamedValue != null;
        if (i == 4) {
            i = (!z || !isColorType(peekNamedValue.type)) ? 0 : 3;
        }
        Keyframe keyframe = z ? i != 0 ? (i == 1 || i == 3) ? Keyframe.ofInt(namedFloat, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, str, 0, 0)) : null : Keyframe.ofFloat(namedFloat, TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, str, 0, 0.0f)) : i == 0 ? Keyframe.ofFloat(namedFloat) : Keyframe.ofInt(namedFloat);
        int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, xmlPullParser, "interpolator", 1, 0);
        if (namedResourceId > 0) {
            keyframe.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        return keyframe;
    }

    private static ObjectAnimator loadObjectAnimator(Context context, Resources resources, Theme theme, AttributeSet attributeSet, float f, XmlPullParser xmlPullParser) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        loadAnimator(context, resources, theme, attributeSet, objectAnimator, f, xmlPullParser);
        return objectAnimator;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static PropertyValuesHolder loadPvh(Context context, Resources resources, Theme theme, XmlPullParser xmlPullParser, String str, int i) {
        PropertyValuesHolder propertyValuesHolder = null;
        int i2 = i;
        ArrayList arrayList = null;
        while (true) {
            int next = xmlPullParser.next();
            if (next == 3 || next == 1) {
                if (arrayList != null) {
                    int size = arrayList.size();
                    if (size > 0) {
                        Keyframe keyframe = (Keyframe) arrayList.get(0);
                        Keyframe keyframe2 = (Keyframe) arrayList.get(size - 1);
                        float fraction = keyframe2.getFraction();
                        if (fraction < 1.0f) {
                            if (fraction < 0.0f) {
                                keyframe2.setFraction(1.0f);
                            } else {
                                arrayList.add(arrayList.size(), createNewKeyframe(keyframe2, 1.0f));
                                size++;
                            }
                        }
                        float fraction2 = keyframe.getFraction();
                        if (fraction2 != 0.0f) {
                            if (fraction2 < 0.0f) {
                                keyframe.setFraction(0.0f);
                            } else {
                                arrayList.add(0, createNewKeyframe(keyframe, 0.0f));
                                size++;
                            }
                        }
                        Keyframe[] keyframeArr = new Keyframe[size];
                        arrayList.toArray(keyframeArr);
                        for (int i3 = 0; i3 < size; i3++) {
                            Keyframe keyframe3 = keyframeArr[i3];
                            if (keyframe3.getFraction() < 0.0f) {
                                if (i3 == 0) {
                                    keyframe3.setFraction(0.0f);
                                } else {
                                    int i4 = size - 1;
                                    if (i3 == i4) {
                                        keyframe3.setFraction(1.0f);
                                    } else {
                                        int i5 = i3 + 1;
                                        int i6 = i3;
                                        while (i5 < i4 && keyframeArr[i5].getFraction() < 0.0f) {
                                            i6 = i5;
                                            i5++;
                                        }
                                        distributeKeyframes(keyframeArr, keyframeArr[i6 + 1].getFraction() - keyframeArr[i3 - 1].getFraction(), i3, i6);
                                    }
                                }
                            }
                        }
                        propertyValuesHolder = PropertyValuesHolder.ofKeyframe(str, keyframeArr);
                        if (i2 == 3) {
                            propertyValuesHolder.setEvaluator(ArgbEvaluator.getInstance());
                        }
                    }
                }
            } else if (xmlPullParser.getName().equals("keyframe")) {
                if (i2 == 4) {
                    i2 = inferValueTypeOfKeyframe(resources, theme, Xml.asAttributeSet(xmlPullParser), xmlPullParser);
                }
                Keyframe loadKeyframe = loadKeyframe(context, resources, theme, Xml.asAttributeSet(xmlPullParser), i2, xmlPullParser);
                if (loadKeyframe != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(loadKeyframe);
                }
                xmlPullParser.next();
            }
        }
        if (arrayList != null) {
        }
        return propertyValuesHolder;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x006c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static PropertyValuesHolder[] loadValues(Context context, Resources resources, Theme theme, XmlPullParser xmlPullParser, AttributeSet attributeSet) {
        XmlPullParser xmlPullParser2 = xmlPullParser;
        PropertyValuesHolder[] propertyValuesHolderArr = null;
        ArrayList arrayList = null;
        while (true) {
            int eventType = xmlPullParser.getEventType();
            if (eventType != 3 && eventType != 1) {
                if (eventType == 2) {
                    if (xmlPullParser.getName().equals("propertyValuesHolder")) {
                        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                        String namedString = TypedArrayUtils.getNamedString(obtainAttributes, xmlPullParser2, "propertyName", 3);
                        int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser2, "valueType", 2, 4);
                        int i = namedInt;
                        PropertyValuesHolder loadPvh = loadPvh(context, resources, theme, xmlPullParser, namedString, namedInt);
                        if (loadPvh == null) {
                            loadPvh = getPVH(obtainAttributes, i, 0, 1, namedString);
                        }
                        if (loadPvh != null) {
                            if (arrayList == null) {
                                arrayList = new ArrayList();
                            }
                            arrayList.add(loadPvh);
                        }
                        obtainAttributes.recycle();
                    } else {
                        Resources resources2 = resources;
                        Theme theme2 = theme;
                        AttributeSet attributeSet2 = attributeSet;
                    }
                }
                xmlPullParser.next();
            } else if (arrayList != null) {
                int size = arrayList.size();
                propertyValuesHolderArr = new PropertyValuesHolder[size];
                for (int i2 = 0; i2 < size; i2++) {
                    propertyValuesHolderArr[i2] = (PropertyValuesHolder) arrayList.get(i2);
                }
            }
        }
        if (arrayList != null) {
        }
        return propertyValuesHolderArr;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator valueAnimator, TypedArray typedArray, TypedArray typedArray2, float f, XmlPullParser xmlPullParser) {
        long namedInt = (long) TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "duration", 1, 300);
        long namedInt2 = (long) TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "startOffset", 2, 0);
        int namedInt3 = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueFrom") && TypedArrayUtils.hasAttribute(xmlPullParser, "valueTo")) {
            if (namedInt3 == 4) {
                namedInt3 = inferValueTypeFromValues(typedArray, 5, 6);
            }
            PropertyValuesHolder pvh = getPVH(typedArray, namedInt3, 5, 6, "");
            if (pvh != null) {
                valueAnimator.setValues(new PropertyValuesHolder[]{pvh});
            }
        }
        valueAnimator.setDuration(namedInt);
        valueAnimator.setStartDelay(namedInt2);
        valueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatCount", 3, 0));
        valueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatMode", 4, 1));
        if (typedArray2 != null) {
            setupObjectAnimator(valueAnimator, typedArray2, namedInt3, f, xmlPullParser);
        }
    }

    private static void setupObjectAnimator(ValueAnimator valueAnimator, TypedArray typedArray, int i, float f, XmlPullParser xmlPullParser) {
        ObjectAnimator objectAnimator = (ObjectAnimator) valueAnimator;
        String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "pathData", 1);
        if (namedString != null) {
            String namedString2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyXName", 2);
            String namedString3 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyYName", 3);
            if (i != 2) {
            }
            if (namedString2 == null && namedString3 == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(typedArray.getPositionDescription());
                sb.append(" propertyXName or propertyYName is needed for PathData");
                throw new InflateException(sb.toString());
            }
            setupPathMotion(PathParser.createPathFromPathData(namedString), objectAnimator, f * 0.5f, namedString2, namedString3);
            return;
        }
        objectAnimator.setPropertyName(TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyName", 0));
    }

    private static void setupPathMotion(Path path, ObjectAnimator objectAnimator, float f, String str, String str2) {
        PropertyValuesHolder propertyValuesHolder;
        Path path2 = path;
        ObjectAnimator objectAnimator2 = objectAnimator;
        String str3 = str;
        String str4 = str2;
        PathMeasure pathMeasure = new PathMeasure(path2, false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(Float.valueOf(0.0f));
        float f2 = 0.0f;
        do {
            f2 += pathMeasure.getLength();
            arrayList.add(Float.valueOf(f2));
        } while (pathMeasure.nextContour());
        PathMeasure pathMeasure2 = new PathMeasure(path2, false);
        int min = Math.min(100, ((int) (f2 / f)) + 1);
        float[] fArr = new float[min];
        float[] fArr2 = new float[min];
        float[] fArr3 = new float[2];
        float f3 = f2 / ((float) (min - 1));
        int i = 0;
        float f4 = 0.0f;
        int i2 = 0;
        while (true) {
            propertyValuesHolder = null;
            if (i2 >= min) {
                break;
            }
            pathMeasure2.getPosTan(f4 - ((Float) arrayList.get(i)).floatValue(), fArr3, null);
            fArr[i2] = fArr3[0];
            fArr2[i2] = fArr3[1];
            f4 += f3;
            int i3 = i + 1;
            if (i3 < arrayList.size() && f4 > ((Float) arrayList.get(i3)).floatValue()) {
                pathMeasure2.nextContour();
                i = i3;
            }
            i2++;
        }
        PropertyValuesHolder ofFloat = str3 != null ? PropertyValuesHolder.ofFloat(str3, fArr) : null;
        if (str4 != null) {
            propertyValuesHolder = PropertyValuesHolder.ofFloat(str4, fArr2);
        }
        if (ofFloat == null) {
            objectAnimator2.setValues(new PropertyValuesHolder[]{propertyValuesHolder});
        } else if (propertyValuesHolder == null) {
            objectAnimator2.setValues(new PropertyValuesHolder[]{ofFloat});
        } else {
            objectAnimator2.setValues(new PropertyValuesHolder[]{ofFloat, propertyValuesHolder});
        }
    }
}

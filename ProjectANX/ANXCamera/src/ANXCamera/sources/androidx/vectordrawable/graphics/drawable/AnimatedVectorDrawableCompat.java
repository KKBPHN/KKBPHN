package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.collection.ArrayMap;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable2Compat {
    private static final String ANIMATED_VECTOR = "animated-vector";
    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
    private static final String LOGTAG = "AnimatedVDCompat";
    private static final String TARGET = "target";
    private AnimatedVectorDrawableCompatState mAnimatedVectorState;
    ArrayList mAnimationCallbacks;
    private AnimatorListener mAnimatorListener;
    private ArgbEvaluator mArgbEvaluator;
    AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
    final Callback mCallback;
    private Context mContext;

    class AnimatedVectorDrawableCompatState extends ConstantState {
        AnimatorSet mAnimatorSet;
        ArrayList mAnimators;
        int mChangingConfigurations;
        ArrayMap mTargetNameMap;
        VectorDrawableCompat mVectorDrawable;

        public AnimatedVectorDrawableCompatState(Context context, AnimatedVectorDrawableCompatState animatedVectorDrawableCompatState, Callback callback, Resources resources) {
            if (animatedVectorDrawableCompatState != null) {
                this.mChangingConfigurations = animatedVectorDrawableCompatState.mChangingConfigurations;
                VectorDrawableCompat vectorDrawableCompat = animatedVectorDrawableCompatState.mVectorDrawable;
                if (vectorDrawableCompat != null) {
                    ConstantState constantState = vectorDrawableCompat.getConstantState();
                    this.mVectorDrawable = (VectorDrawableCompat) (resources != null ? constantState.newDrawable(resources) : constantState.newDrawable());
                    this.mVectorDrawable = (VectorDrawableCompat) this.mVectorDrawable.mutate();
                    this.mVectorDrawable.setCallback(callback);
                    this.mVectorDrawable.setBounds(animatedVectorDrawableCompatState.mVectorDrawable.getBounds());
                    this.mVectorDrawable.setAllowCaching(false);
                }
                ArrayList arrayList = animatedVectorDrawableCompatState.mAnimators;
                if (arrayList != null) {
                    int size = arrayList.size();
                    this.mAnimators = new ArrayList(size);
                    this.mTargetNameMap = new ArrayMap(size);
                    for (int i = 0; i < size; i++) {
                        Animator animator = (Animator) animatedVectorDrawableCompatState.mAnimators.get(i);
                        Animator clone = animator.clone();
                        String str = (String) animatedVectorDrawableCompatState.mTargetNameMap.get(animator);
                        clone.setTarget(this.mVectorDrawable.getTargetByName(str));
                        this.mAnimators.add(clone);
                        this.mTargetNameMap.put(clone, str);
                    }
                    setupAnimatorSet();
                }
            }
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public Drawable newDrawable() {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        public Drawable newDrawable(Resources resources) {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        public void setupAnimatorSet() {
            if (this.mAnimatorSet == null) {
                this.mAnimatorSet = new AnimatorSet();
            }
            this.mAnimatorSet.playTogether(this.mAnimators);
        }
    }

    @RequiresApi(24)
    class AnimatedVectorDrawableDelegateState extends ConstantState {
        private final ConstantState mDelegateState;

        public AnimatedVectorDrawableDelegateState(ConstantState constantState) {
            this.mDelegateState = constantState;
        }

        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }

        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }

        public Drawable newDrawable() {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable();
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }

        public Drawable newDrawable(Resources resources) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources);
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }

        public Drawable newDrawable(Resources resources, Theme theme) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources, theme);
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }
    }

    AnimatedVectorDrawableCompat() {
        this(null, null, null);
    }

    private AnimatedVectorDrawableCompat(@Nullable Context context) {
        this(context, null, null);
    }

    private AnimatedVectorDrawableCompat(@Nullable Context context, @Nullable AnimatedVectorDrawableCompatState animatedVectorDrawableCompatState, @Nullable Resources resources) {
        this.mArgbEvaluator = null;
        this.mAnimatorListener = null;
        this.mAnimationCallbacks = null;
        this.mCallback = new Callback() {
            public void invalidateDrawable(Drawable drawable) {
                AnimatedVectorDrawableCompat.this.invalidateSelf();
            }

            public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
                AnimatedVectorDrawableCompat.this.scheduleSelf(runnable, j);
            }

            public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
                AnimatedVectorDrawableCompat.this.unscheduleSelf(runnable);
            }
        };
        this.mContext = context;
        if (animatedVectorDrawableCompatState != null) {
            this.mAnimatedVectorState = animatedVectorDrawableCompatState;
        } else {
            this.mAnimatedVectorState = new AnimatedVectorDrawableCompatState(context, animatedVectorDrawableCompatState, this.mCallback, resources);
        }
    }

    public static void clearAnimationCallbacks(Drawable drawable) {
        if (drawable instanceof Animatable) {
            if (VERSION.SDK_INT >= 24) {
                ((AnimatedVectorDrawable) drawable).clearAnimationCallbacks();
            } else {
                ((AnimatedVectorDrawableCompat) drawable).clearAnimationCallbacks();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x004b A[Catch:{ IOException | XmlPullParserException -> 0x0060 }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0058 A[Catch:{ IOException | XmlPullParserException -> 0x0060 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    public static AnimatedVectorDrawableCompat create(@NonNull Context context, @DrawableRes int i) {
        int next;
        String str = "parser error";
        String str2 = LOGTAG;
        if (VERSION.SDK_INT >= 24) {
            AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(context);
            animatedVectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(context.getResources(), i, context.getTheme());
            animatedVectorDrawableCompat.mDelegateDrawable.setCallback(animatedVectorDrawableCompat.mCallback);
            animatedVectorDrawableCompat.mCachedConstantStateDelegate = new AnimatedVectorDrawableDelegateState(animatedVectorDrawableCompat.mDelegateDrawable.getConstantState());
            return animatedVectorDrawableCompat;
        }
        try {
            XmlResourceParser xml = context.getResources().getXml(i);
            AttributeSet asAttributeSet = Xml.asAttributeSet(xml);
            while (true) {
                next = xml.next();
                if (next == 2 || next == 1) {
                    if (next != 2) {
                        return createFromXmlInner(context, context.getResources(), xml, asAttributeSet, context.getTheme());
                    }
                    throw new XmlPullParserException("No start tag found");
                }
            }
            if (next != 2) {
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e(str2, str, e);
            return null;
        }
    }

    public static AnimatedVectorDrawableCompat createFromXmlInner(Context context, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) {
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(context);
        animatedVectorDrawableCompat.inflate(resources, xmlPullParser, attributeSet, theme);
        return animatedVectorDrawableCompat;
    }

    public static void registerAnimationCallback(Drawable drawable, AnimationCallback animationCallback) {
        if (drawable != null && animationCallback != null && (drawable instanceof Animatable)) {
            if (VERSION.SDK_INT >= 24) {
                registerPlatformCallback((AnimatedVectorDrawable) drawable, animationCallback);
            } else {
                ((AnimatedVectorDrawableCompat) drawable).registerAnimationCallback(animationCallback);
            }
        }
    }

    @RequiresApi(23)
    private static void registerPlatformCallback(@NonNull AnimatedVectorDrawable animatedVectorDrawable, @NonNull AnimationCallback animationCallback) {
        animatedVectorDrawable.registerAnimationCallback(animationCallback.getPlatformCallback());
    }

    private void removeAnimatorSetListener() {
        AnimatorListener animatorListener = this.mAnimatorListener;
        if (animatorListener != null) {
            this.mAnimatedVectorState.mAnimatorSet.removeListener(animatorListener);
            this.mAnimatorListener = null;
        }
    }

    private void setupAnimatorsForTarget(String str, Animator animator) {
        animator.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(str));
        if (VERSION.SDK_INT < 21) {
            setupColorAnimator(animator);
        }
        AnimatedVectorDrawableCompatState animatedVectorDrawableCompatState = this.mAnimatedVectorState;
        if (animatedVectorDrawableCompatState.mAnimators == null) {
            animatedVectorDrawableCompatState.mAnimators = new ArrayList();
            this.mAnimatedVectorState.mTargetNameMap = new ArrayMap();
        }
        this.mAnimatedVectorState.mAnimators.add(animator);
        this.mAnimatedVectorState.mTargetNameMap.put(animator, str);
    }

    private void setupColorAnimator(Animator animator) {
        if (animator instanceof AnimatorSet) {
            ArrayList childAnimations = ((AnimatorSet) animator).getChildAnimations();
            if (childAnimations != null) {
                for (int i = 0; i < childAnimations.size(); i++) {
                    setupColorAnimator((Animator) childAnimations.get(i));
                }
            }
        }
        if (animator instanceof ObjectAnimator) {
            ObjectAnimator objectAnimator = (ObjectAnimator) animator;
            String propertyName = objectAnimator.getPropertyName();
            if ("fillColor".equals(propertyName) || "strokeColor".equals(propertyName)) {
                if (this.mArgbEvaluator == null) {
                    this.mArgbEvaluator = new ArgbEvaluator();
                }
                objectAnimator.setEvaluator(this.mArgbEvaluator);
            }
        }
    }

    public static boolean unregisterAnimationCallback(Drawable drawable, AnimationCallback animationCallback) {
        if (drawable == null || animationCallback == null || !(drawable instanceof Animatable)) {
            return false;
        }
        return VERSION.SDK_INT >= 24 ? unregisterPlatformCallback((AnimatedVectorDrawable) drawable, animationCallback) : ((AnimatedVectorDrawableCompat) drawable).unregisterAnimationCallback(animationCallback);
    }

    @RequiresApi(23)
    private static boolean unregisterPlatformCallback(AnimatedVectorDrawable animatedVectorDrawable, AnimationCallback animationCallback) {
        return animatedVectorDrawable.unregisterAnimationCallback(animationCallback.getPlatformCallback());
    }

    public void applyTheme(Theme theme) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.applyTheme(drawable, theme);
        }
    }

    public boolean canApplyTheme() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            return DrawableCompat.canApplyTheme(drawable);
        }
        return false;
    }

    public void clearAnimationCallbacks() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            ((AnimatedVectorDrawable) drawable).clearAnimationCallbacks();
            return;
        }
        removeAnimatorSetListener();
        ArrayList arrayList = this.mAnimationCallbacks;
        if (arrayList != null) {
            arrayList.clear();
        }
    }

    public /* bridge */ /* synthetic */ void clearColorFilter() {
        super.clearColorFilter();
    }

    public void draw(Canvas canvas) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.draw(canvas);
        if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            invalidateSelf();
        }
    }

    public int getAlpha() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? DrawableCompat.getAlpha(drawable) : this.mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    public int getChangingConfigurations() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            return drawable.getChangingConfigurations();
        }
        return this.mAnimatedVectorState.mChangingConfigurations | super.getChangingConfigurations();
    }

    public ColorFilter getColorFilter() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? DrawableCompat.getColorFilter(drawable) : this.mAnimatedVectorState.mVectorDrawable.getColorFilter();
    }

    public ConstantState getConstantState() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable == null || VERSION.SDK_INT < 24) {
            return null;
        }
        return new AnimatedVectorDrawableDelegateState(drawable.getConstantState());
    }

    public /* bridge */ /* synthetic */ Drawable getCurrent() {
        return super.getCurrent();
    }

    public int getIntrinsicHeight() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getIntrinsicHeight() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getIntrinsicWidth() : this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    public /* bridge */ /* synthetic */ int getMinimumHeight() {
        return super.getMinimumHeight();
    }

    public /* bridge */ /* synthetic */ int getMinimumWidth() {
        return super.getMinimumWidth();
    }

    public int getOpacity() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getOpacity() : this.mAnimatedVectorState.mVectorDrawable.getOpacity();
    }

    public /* bridge */ /* synthetic */ boolean getPadding(Rect rect) {
        return super.getPadding(rect);
    }

    public /* bridge */ /* synthetic */ int[] getState() {
        return super.getState();
    }

    public /* bridge */ /* synthetic */ Region getTransparentRegion() {
        return super.getTransparentRegion();
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) {
        inflate(resources, xmlPullParser, attributeSet, null);
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) {
        TypedArray obtainAttributes;
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.inflate(drawable, resources, xmlPullParser, attributeSet, theme);
            return;
        }
        int eventType = xmlPullParser.getEventType();
        int depth = xmlPullParser.getDepth() + 1;
        while (eventType != 1 && (xmlPullParser.getDepth() >= depth || eventType != 3)) {
            if (eventType == 2) {
                String name = xmlPullParser.getName();
                if (ANIMATED_VECTOR.equals(name)) {
                    obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE);
                    int resourceId = obtainAttributes.getResourceId(0, 0);
                    if (resourceId != 0) {
                        VectorDrawableCompat create = VectorDrawableCompat.create(resources, resourceId, theme);
                        create.setAllowCaching(false);
                        create.setCallback(this.mCallback);
                        VectorDrawableCompat vectorDrawableCompat = this.mAnimatedVectorState.mVectorDrawable;
                        if (vectorDrawableCompat != null) {
                            vectorDrawableCompat.setCallback(null);
                        }
                        this.mAnimatedVectorState.mVectorDrawable = create;
                    }
                } else if (TARGET.equals(name)) {
                    obtainAttributes = resources.obtainAttributes(attributeSet, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE_TARGET);
                    String string = obtainAttributes.getString(0);
                    int resourceId2 = obtainAttributes.getResourceId(1, 0);
                    if (resourceId2 != 0) {
                        Context context = this.mContext;
                        if (context != null) {
                            setupAnimatorsForTarget(string, AnimatorInflaterCompat.loadAnimator(context, resourceId2));
                        } else {
                            obtainAttributes.recycle();
                            throw new IllegalStateException("Context can't be null when inflating animators");
                        }
                    }
                } else {
                    continue;
                }
                obtainAttributes.recycle();
            }
            eventType = xmlPullParser.next();
        }
        this.mAnimatedVectorState.setupAnimatorSet();
    }

    public boolean isAutoMirrored() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? DrawableCompat.isAutoMirrored(drawable) : this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
    }

    public boolean isRunning() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? ((AnimatedVectorDrawable) drawable).isRunning() : this.mAnimatedVectorState.mAnimatorSet.isRunning();
    }

    public boolean isStateful() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.isStateful() : this.mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    public /* bridge */ /* synthetic */ void jumpToCurrentState() {
        super.jumpToCurrentState();
    }

    public Drawable mutate() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.mutate();
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.setBounds(rect);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setBounds(rect);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int i) {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.setLevel(i) : this.mAnimatedVectorState.mVectorDrawable.setLevel(i);
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.setState(iArr) : this.mAnimatedVectorState.mVectorDrawable.setState(iArr);
    }

    public void registerAnimationCallback(@NonNull AnimationCallback animationCallback) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            registerPlatformCallback((AnimatedVectorDrawable) drawable, animationCallback);
        } else if (animationCallback != null) {
            if (this.mAnimationCallbacks == null) {
                this.mAnimationCallbacks = new ArrayList();
            }
            if (!this.mAnimationCallbacks.contains(animationCallback)) {
                this.mAnimationCallbacks.add(animationCallback);
                if (this.mAnimatorListener == null) {
                    this.mAnimatorListener = new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            ArrayList arrayList = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                            int size = arrayList.size();
                            for (int i = 0; i < size; i++) {
                                ((AnimationCallback) arrayList.get(i)).onAnimationEnd(AnimatedVectorDrawableCompat.this);
                            }
                        }

                        public void onAnimationStart(Animator animator) {
                            ArrayList arrayList = new ArrayList(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                            int size = arrayList.size();
                            for (int i = 0; i < size; i++) {
                                ((AnimationCallback) arrayList.get(i)).onAnimationStart(AnimatedVectorDrawableCompat.this);
                            }
                        }
                    };
                }
                this.mAnimatedVectorState.mAnimatorSet.addListener(this.mAnimatorListener);
            }
        }
    }

    public void setAlpha(int i) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.setAlpha(i);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setAlpha(i);
        }
    }

    public void setAutoMirrored(boolean z) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.setAutoMirrored(drawable, z);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(z);
        }
    }

    public /* bridge */ /* synthetic */ void setChangingConfigurations(int i) {
        super.setChangingConfigurations(i);
    }

    public /* bridge */ /* synthetic */ void setColorFilter(int i, Mode mode) {
        super.setColorFilter(i, mode);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
        }
    }

    public /* bridge */ /* synthetic */ void setFilterBitmap(boolean z) {
        super.setFilterBitmap(z);
    }

    public /* bridge */ /* synthetic */ void setHotspot(float f, float f2) {
        super.setHotspot(f, f2);
    }

    public /* bridge */ /* synthetic */ void setHotspotBounds(int i, int i2, int i3, int i4) {
        super.setHotspotBounds(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ boolean setState(int[] iArr) {
        return super.setState(iArr);
    }

    public void setTint(int i) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.setTint(drawable, i);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setTint(i);
        }
    }

    public void setTintList(ColorStateList colorStateList) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, colorStateList);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setTintList(colorStateList);
        }
    }

    public void setTintMode(Mode mode) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.setTintMode(drawable, mode);
        } else {
            this.mAnimatedVectorState.mVectorDrawable.setTintMode(mode);
        }
    }

    public boolean setVisible(boolean z, boolean z2) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            return drawable.setVisible(z, z2);
        }
        this.mAnimatedVectorState.mVectorDrawable.setVisible(z, z2);
        return super.setVisible(z, z2);
    }

    public void start() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            ((AnimatedVectorDrawable) drawable).start();
        } else if (!this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            this.mAnimatedVectorState.mAnimatorSet.start();
            invalidateSelf();
        }
    }

    public void stop() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            ((AnimatedVectorDrawable) drawable).stop();
        } else {
            this.mAnimatedVectorState.mAnimatorSet.end();
        }
    }

    public boolean unregisterAnimationCallback(@NonNull AnimationCallback animationCallback) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            unregisterPlatformCallback((AnimatedVectorDrawable) drawable, animationCallback);
        }
        ArrayList arrayList = this.mAnimationCallbacks;
        if (arrayList == null || animationCallback == null) {
            return false;
        }
        boolean remove = arrayList.remove(animationCallback);
        if (this.mAnimationCallbacks.size() == 0) {
            removeAnimatorSetListener();
        }
        return remove;
    }
}

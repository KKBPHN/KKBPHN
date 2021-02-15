package com.bumptech.glide.request;

import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.CachedHashCodeArrayMap;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.Map;

public abstract class BaseRequestOptions implements Cloneable {
    private static final int DISK_CACHE_STRATEGY = 4;
    private static final int ERROR_ID = 32;
    private static final int ERROR_PLACEHOLDER = 16;
    private static final int FALLBACK = 8192;
    private static final int FALLBACK_ID = 16384;
    private static final int IS_CACHEABLE = 256;
    private static final int ONLY_RETRIEVE_FROM_CACHE = 524288;
    private static final int OVERRIDE = 512;
    private static final int PLACEHOLDER = 64;
    private static final int PLACEHOLDER_ID = 128;
    private static final int PRIORITY = 8;
    private static final int RESOURCE_CLASS = 4096;
    private static final int SIGNATURE = 1024;
    private static final int SIZE_MULTIPLIER = 2;
    private static final int THEME = 32768;
    private static final int TRANSFORMATION = 2048;
    private static final int TRANSFORMATION_ALLOWED = 65536;
    private static final int TRANSFORMATION_REQUIRED = 131072;
    private static final int UNSET = -1;
    private static final int USE_ANIMATION_POOL = 1048576;
    private static final int USE_UNLIMITED_SOURCE_GENERATORS_POOL = 262144;
    @NonNull
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
    private int errorId;
    @Nullable
    private Drawable errorPlaceholder;
    @Nullable
    private Drawable fallbackDrawable;
    private int fallbackId;
    private int fields;
    private boolean isAutoCloneEnabled;
    private boolean isCacheable = true;
    private boolean isLocked;
    private boolean isScaleOnlyOrNoTransform = true;
    private boolean isTransformationAllowed = true;
    private boolean isTransformationRequired;
    private boolean onlyRetrieveFromCache;
    @NonNull
    private Options options = new Options();
    private int overrideHeight = -1;
    private int overrideWidth = -1;
    @Nullable
    private Drawable placeholderDrawable;
    private int placeholderId;
    @NonNull
    private Priority priority = Priority.NORMAL;
    @NonNull
    private Class resourceClass = Object.class;
    @NonNull
    private Key signature = EmptySignature.obtain();
    private float sizeMultiplier = 1.0f;
    @Nullable
    private Theme theme;
    @NonNull
    private Map transformations = new CachedHashCodeArrayMap();
    private boolean useAnimationPool;
    private boolean useUnlimitedSourceGeneratorsPool;

    private boolean isSet(int i) {
        return isSet(this.fields, i);
    }

    private static boolean isSet(int i, int i2) {
        return (i & i2) != 0;
    }

    @NonNull
    private BaseRequestOptions optionalScaleOnlyTransform(@NonNull DownsampleStrategy downsampleStrategy, @NonNull Transformation transformation) {
        return scaleOnlyTransform(downsampleStrategy, transformation, false);
    }

    @NonNull
    private BaseRequestOptions scaleOnlyTransform(@NonNull DownsampleStrategy downsampleStrategy, @NonNull Transformation transformation) {
        return scaleOnlyTransform(downsampleStrategy, transformation, true);
    }

    @NonNull
    private BaseRequestOptions scaleOnlyTransform(@NonNull DownsampleStrategy downsampleStrategy, @NonNull Transformation transformation, boolean z) {
        BaseRequestOptions transform = z ? transform(downsampleStrategy, transformation) : optionalTransform(downsampleStrategy, transformation);
        transform.isScaleOnlyOrNoTransform = true;
        return transform;
    }

    private BaseRequestOptions self() {
        return this;
    }

    @NonNull
    private BaseRequestOptions selfOrThrowIfLocked() {
        if (!this.isLocked) {
            self();
            return this;
        }
        throw new IllegalStateException("You cannot modify locked T, consider clone()");
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions apply(@NonNull BaseRequestOptions baseRequestOptions) {
        if (this.isAutoCloneEnabled) {
            return clone().apply(baseRequestOptions);
        }
        if (isSet(baseRequestOptions.fields, 2)) {
            this.sizeMultiplier = baseRequestOptions.sizeMultiplier;
        }
        if (isSet(baseRequestOptions.fields, 262144)) {
            this.useUnlimitedSourceGeneratorsPool = baseRequestOptions.useUnlimitedSourceGeneratorsPool;
        }
        if (isSet(baseRequestOptions.fields, 1048576)) {
            this.useAnimationPool = baseRequestOptions.useAnimationPool;
        }
        if (isSet(baseRequestOptions.fields, 4)) {
            this.diskCacheStrategy = baseRequestOptions.diskCacheStrategy;
        }
        if (isSet(baseRequestOptions.fields, 8)) {
            this.priority = baseRequestOptions.priority;
        }
        if (isSet(baseRequestOptions.fields, 16)) {
            this.errorPlaceholder = baseRequestOptions.errorPlaceholder;
            this.errorId = 0;
            this.fields &= -33;
        }
        if (isSet(baseRequestOptions.fields, 32)) {
            this.errorId = baseRequestOptions.errorId;
            this.errorPlaceholder = null;
            this.fields &= -17;
        }
        if (isSet(baseRequestOptions.fields, 64)) {
            this.placeholderDrawable = baseRequestOptions.placeholderDrawable;
            this.placeholderId = 0;
            this.fields &= -129;
        }
        if (isSet(baseRequestOptions.fields, 128)) {
            this.placeholderId = baseRequestOptions.placeholderId;
            this.placeholderDrawable = null;
            this.fields &= -65;
        }
        if (isSet(baseRequestOptions.fields, 256)) {
            this.isCacheable = baseRequestOptions.isCacheable;
        }
        if (isSet(baseRequestOptions.fields, 512)) {
            this.overrideWidth = baseRequestOptions.overrideWidth;
            this.overrideHeight = baseRequestOptions.overrideHeight;
        }
        if (isSet(baseRequestOptions.fields, 1024)) {
            this.signature = baseRequestOptions.signature;
        }
        if (isSet(baseRequestOptions.fields, 4096)) {
            this.resourceClass = baseRequestOptions.resourceClass;
        }
        if (isSet(baseRequestOptions.fields, 8192)) {
            this.fallbackDrawable = baseRequestOptions.fallbackDrawable;
            this.fallbackId = 0;
            this.fields &= -16385;
        }
        if (isSet(baseRequestOptions.fields, 16384)) {
            this.fallbackId = baseRequestOptions.fallbackId;
            this.fallbackDrawable = null;
            this.fields &= -8193;
        }
        if (isSet(baseRequestOptions.fields, 32768)) {
            this.theme = baseRequestOptions.theme;
        }
        if (isSet(baseRequestOptions.fields, 65536)) {
            this.isTransformationAllowed = baseRequestOptions.isTransformationAllowed;
        }
        if (isSet(baseRequestOptions.fields, 131072)) {
            this.isTransformationRequired = baseRequestOptions.isTransformationRequired;
        }
        if (isSet(baseRequestOptions.fields, 2048)) {
            this.transformations.putAll(baseRequestOptions.transformations);
            this.isScaleOnlyOrNoTransform = baseRequestOptions.isScaleOnlyOrNoTransform;
        }
        if (isSet(baseRequestOptions.fields, 524288)) {
            this.onlyRetrieveFromCache = baseRequestOptions.onlyRetrieveFromCache;
        }
        if (!this.isTransformationAllowed) {
            this.transformations.clear();
            this.fields &= -2049;
            this.isTransformationRequired = false;
            this.fields &= -131073;
            this.isScaleOnlyOrNoTransform = true;
        }
        this.fields |= baseRequestOptions.fields;
        this.options.putAll(baseRequestOptions.options);
        selfOrThrowIfLocked();
        return this;
    }

    @NonNull
    public BaseRequestOptions autoClone() {
        if (!this.isLocked || this.isAutoCloneEnabled) {
            this.isAutoCloneEnabled = true;
            return lock();
        }
        throw new IllegalStateException("You cannot auto lock an already locked options object, try clone() first");
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions centerCrop() {
        return transform(DownsampleStrategy.CENTER_OUTSIDE, (Transformation) new CenterCrop());
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions centerInside() {
        return scaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions circleCrop() {
        return transform(DownsampleStrategy.CENTER_INSIDE, (Transformation) new CircleCrop());
    }

    @CheckResult
    public BaseRequestOptions clone() {
        try {
            BaseRequestOptions baseRequestOptions = (BaseRequestOptions) super.clone();
            baseRequestOptions.options = new Options();
            baseRequestOptions.options.putAll(this.options);
            baseRequestOptions.transformations = new CachedHashCodeArrayMap();
            baseRequestOptions.transformations.putAll(this.transformations);
            baseRequestOptions.isLocked = false;
            baseRequestOptions.isAutoCloneEnabled = false;
            return baseRequestOptions;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions decode(@NonNull Class cls) {
        if (this.isAutoCloneEnabled) {
            return clone().decode(cls);
        }
        Preconditions.checkNotNull(cls);
        this.resourceClass = cls;
        this.fields |= 4096;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions disallowHardwareConfig() {
        return set(Downsampler.ALLOW_HARDWARE_CONFIG, Boolean.valueOf(false));
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions diskCacheStrategy(@NonNull DiskCacheStrategy diskCacheStrategy2) {
        if (this.isAutoCloneEnabled) {
            return clone().diskCacheStrategy(diskCacheStrategy2);
        }
        Preconditions.checkNotNull(diskCacheStrategy2);
        this.diskCacheStrategy = diskCacheStrategy2;
        this.fields |= 4;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions dontAnimate() {
        return set(GifOptions.DISABLE_ANIMATION, Boolean.valueOf(true));
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions dontTransform() {
        if (this.isAutoCloneEnabled) {
            return clone().dontTransform();
        }
        this.transformations.clear();
        this.fields &= -2049;
        this.isTransformationRequired = false;
        this.fields &= -131073;
        this.isTransformationAllowed = false;
        this.fields |= 65536;
        this.isScaleOnlyOrNoTransform = true;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions downsample(@NonNull DownsampleStrategy downsampleStrategy) {
        Option option = DownsampleStrategy.OPTION;
        Preconditions.checkNotNull(downsampleStrategy);
        return set(option, downsampleStrategy);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions encodeFormat(@NonNull CompressFormat compressFormat) {
        Option option = BitmapEncoder.COMPRESSION_FORMAT;
        Preconditions.checkNotNull(compressFormat);
        return set(option, compressFormat);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions encodeQuality(@IntRange(from = 0, to = 100) int i) {
        return set(BitmapEncoder.COMPRESSION_QUALITY, Integer.valueOf(i));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BaseRequestOptions)) {
            return false;
        }
        BaseRequestOptions baseRequestOptions = (BaseRequestOptions) obj;
        return Float.compare(baseRequestOptions.sizeMultiplier, this.sizeMultiplier) == 0 && this.errorId == baseRequestOptions.errorId && Util.bothNullOrEqual(this.errorPlaceholder, baseRequestOptions.errorPlaceholder) && this.placeholderId == baseRequestOptions.placeholderId && Util.bothNullOrEqual(this.placeholderDrawable, baseRequestOptions.placeholderDrawable) && this.fallbackId == baseRequestOptions.fallbackId && Util.bothNullOrEqual(this.fallbackDrawable, baseRequestOptions.fallbackDrawable) && this.isCacheable == baseRequestOptions.isCacheable && this.overrideHeight == baseRequestOptions.overrideHeight && this.overrideWidth == baseRequestOptions.overrideWidth && this.isTransformationRequired == baseRequestOptions.isTransformationRequired && this.isTransformationAllowed == baseRequestOptions.isTransformationAllowed && this.useUnlimitedSourceGeneratorsPool == baseRequestOptions.useUnlimitedSourceGeneratorsPool && this.onlyRetrieveFromCache == baseRequestOptions.onlyRetrieveFromCache && this.diskCacheStrategy.equals(baseRequestOptions.diskCacheStrategy) && this.priority == baseRequestOptions.priority && this.options.equals(baseRequestOptions.options) && this.transformations.equals(baseRequestOptions.transformations) && this.resourceClass.equals(baseRequestOptions.resourceClass) && Util.bothNullOrEqual(this.signature, baseRequestOptions.signature) && Util.bothNullOrEqual(this.theme, baseRequestOptions.theme);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions error(@DrawableRes int i) {
        if (this.isAutoCloneEnabled) {
            return clone().error(i);
        }
        this.errorId = i;
        this.fields |= 32;
        this.errorPlaceholder = null;
        this.fields &= -17;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions error(@Nullable Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return clone().error(drawable);
        }
        this.errorPlaceholder = drawable;
        this.fields |= 16;
        this.errorId = 0;
        this.fields &= -33;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions fallback(@DrawableRes int i) {
        if (this.isAutoCloneEnabled) {
            return clone().fallback(i);
        }
        this.fallbackId = i;
        this.fields |= 16384;
        this.fallbackDrawable = null;
        this.fields &= -8193;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions fallback(@Nullable Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return clone().fallback(drawable);
        }
        this.fallbackDrawable = drawable;
        this.fields |= 8192;
        this.fallbackId = 0;
        this.fields &= -16385;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions fitCenter() {
        return scaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions format(@NonNull DecodeFormat decodeFormat) {
        Preconditions.checkNotNull(decodeFormat);
        return set(Downsampler.DECODE_FORMAT, decodeFormat).set(GifOptions.DECODE_FORMAT, decodeFormat);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions frame(@IntRange(from = 0) long j) {
        return set(VideoDecoder.TARGET_FRAME, Long.valueOf(j));
    }

    @NonNull
    public final DiskCacheStrategy getDiskCacheStrategy() {
        return this.diskCacheStrategy;
    }

    public final int getErrorId() {
        return this.errorId;
    }

    @Nullable
    public final Drawable getErrorPlaceholder() {
        return this.errorPlaceholder;
    }

    @Nullable
    public final Drawable getFallbackDrawable() {
        return this.fallbackDrawable;
    }

    public final int getFallbackId() {
        return this.fallbackId;
    }

    public final boolean getOnlyRetrieveFromCache() {
        return this.onlyRetrieveFromCache;
    }

    @NonNull
    public final Options getOptions() {
        return this.options;
    }

    public final int getOverrideHeight() {
        return this.overrideHeight;
    }

    public final int getOverrideWidth() {
        return this.overrideWidth;
    }

    @Nullable
    public final Drawable getPlaceholderDrawable() {
        return this.placeholderDrawable;
    }

    public final int getPlaceholderId() {
        return this.placeholderId;
    }

    @NonNull
    public final Priority getPriority() {
        return this.priority;
    }

    @NonNull
    public final Class getResourceClass() {
        return this.resourceClass;
    }

    @NonNull
    public final Key getSignature() {
        return this.signature;
    }

    public final float getSizeMultiplier() {
        return this.sizeMultiplier;
    }

    @Nullable
    public final Theme getTheme() {
        return this.theme;
    }

    @NonNull
    public final Map getTransformations() {
        return this.transformations;
    }

    public final boolean getUseAnimationPool() {
        return this.useAnimationPool;
    }

    public final boolean getUseUnlimitedSourceGeneratorsPool() {
        return this.useUnlimitedSourceGeneratorsPool;
    }

    public int hashCode() {
        return Util.hashCode((Object) this.theme, Util.hashCode((Object) this.signature, Util.hashCode((Object) this.resourceClass, Util.hashCode((Object) this.transformations, Util.hashCode((Object) this.options, Util.hashCode((Object) this.priority, Util.hashCode((Object) this.diskCacheStrategy, Util.hashCode(this.onlyRetrieveFromCache, Util.hashCode(this.useUnlimitedSourceGeneratorsPool, Util.hashCode(this.isTransformationAllowed, Util.hashCode(this.isTransformationRequired, Util.hashCode(this.overrideWidth, Util.hashCode(this.overrideHeight, Util.hashCode(this.isCacheable, Util.hashCode((Object) this.fallbackDrawable, Util.hashCode(this.fallbackId, Util.hashCode((Object) this.placeholderDrawable, Util.hashCode(this.placeholderId, Util.hashCode((Object) this.errorPlaceholder, Util.hashCode(this.errorId, Util.hashCode(this.sizeMultiplier)))))))))))))))))))));
    }

    /* access modifiers changed from: protected */
    public boolean isAutoCloneEnabled() {
        return this.isAutoCloneEnabled;
    }

    public final boolean isDiskCacheStrategySet() {
        return isSet(4);
    }

    public final boolean isLocked() {
        return this.isLocked;
    }

    public final boolean isMemoryCacheable() {
        return this.isCacheable;
    }

    public final boolean isPrioritySet() {
        return isSet(8);
    }

    /* access modifiers changed from: 0000 */
    public boolean isScaleOnlyOrNoTransform() {
        return this.isScaleOnlyOrNoTransform;
    }

    public final boolean isSkipMemoryCacheSet() {
        return isSet(256);
    }

    public final boolean isTransformationAllowed() {
        return this.isTransformationAllowed;
    }

    public final boolean isTransformationRequired() {
        return this.isTransformationRequired;
    }

    public final boolean isTransformationSet() {
        return isSet(2048);
    }

    public final boolean isValidOverride() {
        return Util.isValidDimensions(this.overrideWidth, this.overrideHeight);
    }

    @NonNull
    public BaseRequestOptions lock() {
        this.isLocked = true;
        self();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions onlyRetrieveFromCache(boolean z) {
        if (this.isAutoCloneEnabled) {
            return clone().onlyRetrieveFromCache(z);
        }
        this.onlyRetrieveFromCache = z;
        this.fields |= 524288;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions optionalCenterCrop() {
        return optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, (Transformation) new CenterCrop());
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions optionalCenterInside() {
        return optionalScaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions optionalCircleCrop() {
        return optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, (Transformation) new CircleCrop());
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions optionalFitCenter() {
        return optionalScaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions optionalTransform(@NonNull Transformation transformation) {
        return transform(transformation, false);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public final BaseRequestOptions optionalTransform(@NonNull DownsampleStrategy downsampleStrategy, @NonNull Transformation transformation) {
        if (this.isAutoCloneEnabled) {
            return clone().optionalTransform(downsampleStrategy, transformation);
        }
        downsample(downsampleStrategy);
        return transform(transformation, false);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions optionalTransform(@NonNull Class cls, @NonNull Transformation transformation) {
        return transform(cls, transformation, false);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions override(int i) {
        return override(i, i);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions override(int i, int i2) {
        if (this.isAutoCloneEnabled) {
            return clone().override(i, i2);
        }
        this.overrideWidth = i;
        this.overrideHeight = i2;
        this.fields |= 512;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions placeholder(@DrawableRes int i) {
        if (this.isAutoCloneEnabled) {
            return clone().placeholder(i);
        }
        this.placeholderId = i;
        this.fields |= 128;
        this.placeholderDrawable = null;
        this.fields &= -65;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions placeholder(@Nullable Drawable drawable) {
        if (this.isAutoCloneEnabled) {
            return clone().placeholder(drawable);
        }
        this.placeholderDrawable = drawable;
        this.fields |= 64;
        this.placeholderId = 0;
        this.fields &= -129;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions priority(@NonNull Priority priority2) {
        if (this.isAutoCloneEnabled) {
            return clone().priority(priority2);
        }
        Preconditions.checkNotNull(priority2);
        this.priority = priority2;
        this.fields |= 8;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions set(@NonNull Option option, @NonNull Object obj) {
        if (this.isAutoCloneEnabled) {
            return clone().set(option, obj);
        }
        Preconditions.checkNotNull(option);
        Preconditions.checkNotNull(obj);
        this.options.set(option, obj);
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions signature(@NonNull Key key) {
        if (this.isAutoCloneEnabled) {
            return clone().signature(key);
        }
        Preconditions.checkNotNull(key);
        this.signature = key;
        this.fields |= 1024;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions sizeMultiplier(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (this.isAutoCloneEnabled) {
            return clone().sizeMultiplier(f);
        }
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.sizeMultiplier = f;
        this.fields |= 2;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions skipMemoryCache(boolean z) {
        if (this.isAutoCloneEnabled) {
            return clone().skipMemoryCache(true);
        }
        this.isCacheable = !z;
        this.fields |= 256;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions theme(@Nullable Theme theme2) {
        if (this.isAutoCloneEnabled) {
            return clone().theme(theme2);
        }
        this.theme = theme2;
        this.fields |= 32768;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions timeout(@IntRange(from = 0) int i) {
        return set(HttpGlideUrlLoader.TIMEOUT, Integer.valueOf(i));
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions transform(@NonNull Transformation transformation) {
        return transform(transformation, true);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public BaseRequestOptions transform(@NonNull Transformation transformation, boolean z) {
        if (this.isAutoCloneEnabled) {
            return clone().transform(transformation, z);
        }
        DrawableTransformation drawableTransformation = new DrawableTransformation(transformation, z);
        transform(Bitmap.class, transformation, z);
        transform(Drawable.class, drawableTransformation, z);
        transform(BitmapDrawable.class, drawableTransformation.asBitmapDrawable(), z);
        transform(GifDrawable.class, new GifDrawableTransformation(transformation), z);
        selfOrThrowIfLocked();
        return this;
    }

    /* access modifiers changed from: 0000 */
    @CheckResult
    @NonNull
    public final BaseRequestOptions transform(@NonNull DownsampleStrategy downsampleStrategy, @NonNull Transformation transformation) {
        if (this.isAutoCloneEnabled) {
            return clone().transform(downsampleStrategy, transformation);
        }
        downsample(downsampleStrategy);
        return transform(transformation);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions transform(@NonNull Class cls, @NonNull Transformation transformation) {
        return transform(cls, transformation, true);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public BaseRequestOptions transform(@NonNull Class cls, @NonNull Transformation transformation, boolean z) {
        if (this.isAutoCloneEnabled) {
            return clone().transform(cls, transformation, z);
        }
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(transformation);
        this.transformations.put(cls, transformation);
        this.fields |= 2048;
        this.isTransformationAllowed = true;
        this.fields |= 65536;
        this.isScaleOnlyOrNoTransform = false;
        if (z) {
            this.fields |= 131072;
            this.isTransformationRequired = true;
        }
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions transform(@NonNull Transformation... transformationArr) {
        if (transformationArr.length > 1) {
            return transform((Transformation) new MultiTransformation(transformationArr), true);
        }
        if (transformationArr.length == 1) {
            return transform(transformationArr[0]);
        }
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    @Deprecated
    public BaseRequestOptions transforms(@NonNull Transformation... transformationArr) {
        return transform((Transformation) new MultiTransformation(transformationArr), true);
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions useAnimationPool(boolean z) {
        if (this.isAutoCloneEnabled) {
            return clone().useAnimationPool(z);
        }
        this.useAnimationPool = z;
        this.fields |= 1048576;
        selfOrThrowIfLocked();
        return this;
    }

    @CheckResult
    @NonNull
    public BaseRequestOptions useUnlimitedSourceGeneratorsPool(boolean z) {
        if (this.isAutoCloneEnabled) {
            return clone().useUnlimitedSourceGeneratorsPool(z);
        }
        this.useUnlimitedSourceGeneratorsPool = z;
        this.fields |= 262144;
        selfOrThrowIfLocked();
        return this;
    }
}

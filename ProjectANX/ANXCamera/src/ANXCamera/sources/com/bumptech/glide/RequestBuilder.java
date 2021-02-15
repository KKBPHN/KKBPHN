package com.bumptech.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.ErrorRequestCoordinator;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class RequestBuilder extends BaseRequestOptions implements Cloneable, ModelTypes {
    protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS = ((RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).priority(Priority.LOW)).skipMemoryCache(true));
    private final Context context;
    @Nullable
    private RequestBuilder errorBuilder;
    private final Glide glide;
    private final GlideContext glideContext;
    private boolean isDefaultTransitionOptionsSet;
    private boolean isModelSet;
    private boolean isThumbnailBuilt;
    @Nullable
    private Object model;
    @Nullable
    private List requestListeners;
    private final RequestManager requestManager;
    @Nullable
    private Float thumbSizeMultiplier;
    @Nullable
    private RequestBuilder thumbnailBuilder;
    private final Class transcodeClass;
    @NonNull
    private TransitionOptions transitionOptions;

    /* renamed from: com.bumptech.glide.RequestBuilder$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$Priority = new int[Priority.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0048 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0052 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0066 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0071 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x007c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0087 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.LOW.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.HIGH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[Priority.IMMEDIATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 1;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 2;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 3;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 4;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 5;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 6;
            $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 7;
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    @SuppressLint({"CheckResult"})
    protected RequestBuilder(@NonNull Glide glide2, RequestManager requestManager2, Class cls, Context context2) {
        this.isDefaultTransitionOptionsSet = true;
        this.glide = glide2;
        this.requestManager = requestManager2;
        this.transcodeClass = cls;
        this.context = context2;
        this.transitionOptions = requestManager2.getDefaultTransitionOptions(cls);
        this.glideContext = glide2.getGlideContext();
        initRequestListeners(requestManager2.getDefaultRequestListeners());
        apply((BaseRequestOptions) requestManager2.getDefaultRequestOptions());
    }

    @SuppressLint({"CheckResult"})
    protected RequestBuilder(Class cls, RequestBuilder requestBuilder) {
        this(requestBuilder.glide, requestBuilder.requestManager, cls, requestBuilder.context);
        this.model = requestBuilder.model;
        this.isModelSet = requestBuilder.isModelSet;
        apply((BaseRequestOptions) requestBuilder);
    }

    private Request buildRequest(Target target, @Nullable RequestListener requestListener, BaseRequestOptions baseRequestOptions, Executor executor) {
        return buildRequestRecursive(new Object(), target, requestListener, null, this.transitionOptions, baseRequestOptions.getPriority(), baseRequestOptions.getOverrideWidth(), baseRequestOptions.getOverrideHeight(), baseRequestOptions, executor);
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [com.bumptech.glide.request.RequestCoordinator] */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r0v4, types: [com.bumptech.glide.request.ErrorRequestCoordinator] */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Request buildRequestRecursive(Object obj, Target target, @Nullable RequestListener requestListener, @Nullable RequestCoordinator requestCoordinator, TransitionOptions transitionOptions2, Priority priority, int i, int i2, BaseRequestOptions baseRequestOptions, Executor executor) {
        ErrorRequestCoordinator errorRequestCoordinator;
        ? r4;
        if (this.errorBuilder != null) {
            Object obj2 = obj;
            ? errorRequestCoordinator2 = new ErrorRequestCoordinator(obj, requestCoordinator);
            errorRequestCoordinator = errorRequestCoordinator2;
            r4 = errorRequestCoordinator2;
        } else {
            Object obj3 = obj;
            errorRequestCoordinator = 0;
            r4 = requestCoordinator;
        }
        Request buildThumbnailRequestRecursive = buildThumbnailRequestRecursive(obj, target, requestListener, r4, transitionOptions2, priority, i, i2, baseRequestOptions, executor);
        if (errorRequestCoordinator == 0) {
            return buildThumbnailRequestRecursive;
        }
        int overrideWidth = this.errorBuilder.getOverrideWidth();
        int overrideHeight = this.errorBuilder.getOverrideHeight();
        if (Util.isValidDimensions(i, i2) && !this.errorBuilder.isValidOverride()) {
            overrideWidth = baseRequestOptions.getOverrideWidth();
            overrideHeight = baseRequestOptions.getOverrideHeight();
        }
        int i3 = overrideWidth;
        int i4 = overrideHeight;
        RequestBuilder requestBuilder = this.errorBuilder;
        errorRequestCoordinator.setRequests(buildThumbnailRequestRecursive, requestBuilder.buildRequestRecursive(obj, target, requestListener, errorRequestCoordinator, requestBuilder.transitionOptions, requestBuilder.getPriority(), i3, i4, this.errorBuilder, executor));
        return errorRequestCoordinator;
    }

    private Request buildThumbnailRequestRecursive(Object obj, Target target, RequestListener requestListener, @Nullable RequestCoordinator requestCoordinator, TransitionOptions transitionOptions2, Priority priority, int i, int i2, BaseRequestOptions baseRequestOptions, Executor executor) {
        Object obj2 = obj;
        RequestCoordinator requestCoordinator2 = requestCoordinator;
        Priority priority2 = priority;
        RequestBuilder requestBuilder = this.thumbnailBuilder;
        if (requestBuilder != null) {
            if (!this.isThumbnailBuilt) {
                TransitionOptions transitionOptions3 = requestBuilder.isDefaultTransitionOptionsSet ? transitionOptions2 : requestBuilder.transitionOptions;
                Priority priority3 = this.thumbnailBuilder.isPrioritySet() ? this.thumbnailBuilder.getPriority() : getThumbnailPriority(priority2);
                int overrideWidth = this.thumbnailBuilder.getOverrideWidth();
                int overrideHeight = this.thumbnailBuilder.getOverrideHeight();
                if (Util.isValidDimensions(i, i2) && !this.thumbnailBuilder.isValidOverride()) {
                    overrideWidth = baseRequestOptions.getOverrideWidth();
                    overrideHeight = baseRequestOptions.getOverrideHeight();
                }
                int i3 = overrideWidth;
                int i4 = overrideHeight;
                ThumbnailRequestCoordinator thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(obj2, requestCoordinator2);
                Object obj3 = obj;
                Target target2 = target;
                RequestListener requestListener2 = requestListener;
                ThumbnailRequestCoordinator thumbnailRequestCoordinator2 = thumbnailRequestCoordinator;
                Request obtainRequest = obtainRequest(obj3, target2, requestListener2, baseRequestOptions, thumbnailRequestCoordinator, transitionOptions2, priority, i, i2, executor);
                this.isThumbnailBuilt = true;
                RequestBuilder requestBuilder2 = this.thumbnailBuilder;
                Request request = obtainRequest;
                Request buildRequestRecursive = requestBuilder2.buildRequestRecursive(obj3, target2, requestListener2, thumbnailRequestCoordinator2, transitionOptions3, priority3, i3, i4, requestBuilder2, executor);
                this.isThumbnailBuilt = false;
                thumbnailRequestCoordinator2.setRequests(request, buildRequestRecursive);
                return thumbnailRequestCoordinator2;
            }
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
        } else if (this.thumbSizeMultiplier == null) {
            return obtainRequest(obj, target, requestListener, baseRequestOptions, requestCoordinator, transitionOptions2, priority, i, i2, executor);
        } else {
            ThumbnailRequestCoordinator thumbnailRequestCoordinator3 = new ThumbnailRequestCoordinator(obj2, requestCoordinator2);
            Target target3 = target;
            RequestListener requestListener3 = requestListener;
            ThumbnailRequestCoordinator thumbnailRequestCoordinator4 = thumbnailRequestCoordinator3;
            TransitionOptions transitionOptions4 = transitionOptions2;
            int i5 = i;
            int i6 = i2;
            Executor executor2 = executor;
            thumbnailRequestCoordinator3.setRequests(obtainRequest(obj, target3, requestListener3, baseRequestOptions, thumbnailRequestCoordinator4, transitionOptions4, priority, i5, i6, executor2), obtainRequest(obj, target3, requestListener3, baseRequestOptions.clone().sizeMultiplier(this.thumbSizeMultiplier.floatValue()), thumbnailRequestCoordinator4, transitionOptions4, getThumbnailPriority(priority2), i5, i6, executor2));
            return thumbnailRequestCoordinator3;
        }
    }

    @NonNull
    private Priority getThumbnailPriority(@NonNull Priority priority) {
        int i = AnonymousClass1.$SwitchMap$com$bumptech$glide$Priority[priority.ordinal()];
        if (i == 1) {
            return Priority.NORMAL;
        }
        if (i == 2) {
            return Priority.HIGH;
        }
        if (i == 3 || i == 4) {
            return Priority.IMMEDIATE;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("unknown priority: ");
        sb.append(getPriority());
        throw new IllegalArgumentException(sb.toString());
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.bumptech.glide.request.RequestListener>, for r2v0, types: [java.util.List<com.bumptech.glide.request.RequestListener>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @SuppressLint({"CheckResult"})
    private void initRequestListeners(List<RequestListener> list) {
        for (RequestListener addListener : list) {
            addListener(addListener);
        }
    }

    private Target into(@NonNull Target target, @Nullable RequestListener requestListener, BaseRequestOptions baseRequestOptions, Executor executor) {
        Preconditions.checkNotNull(target);
        if (this.isModelSet) {
            Request buildRequest = buildRequest(target, requestListener, baseRequestOptions, executor);
            Request request = target.getRequest();
            if (!buildRequest.isEquivalentTo(request) || isSkipMemoryCacheWithCompletePreviousRequest(baseRequestOptions, request)) {
                this.requestManager.clear(target);
                target.setRequest(buildRequest);
                this.requestManager.track(target, buildRequest);
                return target;
            }
            Preconditions.checkNotNull(request);
            if (!request.isRunning()) {
                request.begin();
            }
            return target;
        }
        throw new IllegalArgumentException("You must call #load() before calling #into()");
    }

    private boolean isSkipMemoryCacheWithCompletePreviousRequest(BaseRequestOptions baseRequestOptions, Request request) {
        return !baseRequestOptions.isMemoryCacheable() && request.isComplete();
    }

    @NonNull
    private RequestBuilder loadGeneric(@Nullable Object obj) {
        this.model = obj;
        this.isModelSet = true;
        return this;
    }

    private Request obtainRequest(Object obj, Target target, RequestListener requestListener, BaseRequestOptions baseRequestOptions, RequestCoordinator requestCoordinator, TransitionOptions transitionOptions2, Priority priority, int i, int i2, Executor executor) {
        Context context2 = this.context;
        GlideContext glideContext2 = this.glideContext;
        return SingleRequest.obtain(context2, glideContext2, obj, this.model, this.transcodeClass, baseRequestOptions, i, i2, priority, target, requestListener, this.requestListeners, requestCoordinator, glideContext2.getEngine(), transitionOptions2.getTransitionFactory(), executor);
    }

    @CheckResult
    @NonNull
    public RequestBuilder addListener(@Nullable RequestListener requestListener) {
        if (requestListener != null) {
            if (this.requestListeners == null) {
                this.requestListeners = new ArrayList();
            }
            this.requestListeners.add(requestListener);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public RequestBuilder apply(@NonNull BaseRequestOptions baseRequestOptions) {
        Preconditions.checkNotNull(baseRequestOptions);
        return (RequestBuilder) super.apply(baseRequestOptions);
    }

    @CheckResult
    public RequestBuilder clone() {
        RequestBuilder requestBuilder = (RequestBuilder) super.clone();
        requestBuilder.transitionOptions = requestBuilder.transitionOptions.clone();
        return requestBuilder;
    }

    @CheckResult
    @Deprecated
    public FutureTarget downloadOnly(int i, int i2) {
        return getDownloadOnlyRequest().submit(i, i2);
    }

    @CheckResult
    @Deprecated
    public Target downloadOnly(@NonNull Target target) {
        return getDownloadOnlyRequest().into(target);
    }

    @NonNull
    public RequestBuilder error(@Nullable RequestBuilder requestBuilder) {
        this.errorBuilder = requestBuilder;
        return this;
    }

    /* access modifiers changed from: protected */
    @CheckResult
    @NonNull
    public RequestBuilder getDownloadOnlyRequest() {
        return new RequestBuilder(File.class, this).apply((BaseRequestOptions) DOWNLOAD_ONLY_OPTIONS);
    }

    @Deprecated
    public FutureTarget into(int i, int i2) {
        return submit(i, i2);
    }

    @NonNull
    public Target into(@NonNull Target target) {
        return into(target, null, Executors.mainThreadExecutor());
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public Target into(@NonNull Target target, @Nullable RequestListener requestListener, Executor executor) {
        into(target, requestListener, this, executor);
        return target;
    }

    @NonNull
    public ViewTarget into(@NonNull ImageView imageView) {
        BaseRequestOptions baseRequestOptions;
        Util.assertMainThread();
        Preconditions.checkNotNull(imageView);
        if (!isTransformationSet() && isTransformationAllowed() && imageView.getScaleType() != null) {
            switch (AnonymousClass1.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
                case 1:
                    baseRequestOptions = clone().optionalCenterCrop();
                    break;
                case 2:
                case 6:
                    baseRequestOptions = clone().optionalCenterInside();
                    break;
                case 3:
                case 4:
                case 5:
                    baseRequestOptions = clone().optionalFitCenter();
                    break;
            }
        }
        baseRequestOptions = this;
        ViewTarget buildImageViewTarget = this.glideContext.buildImageViewTarget(imageView, this.transcodeClass);
        into(buildImageViewTarget, null, baseRequestOptions, Executors.mainThreadExecutor());
        return buildImageViewTarget;
    }

    @CheckResult
    @NonNull
    public RequestBuilder listener(@Nullable RequestListener requestListener) {
        this.requestListeners = null;
        return addListener(requestListener);
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable Bitmap bitmap) {
        loadGeneric(bitmap);
        return apply((BaseRequestOptions) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable Drawable drawable) {
        loadGeneric(drawable);
        return apply((BaseRequestOptions) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable Uri uri) {
        loadGeneric(uri);
        return this;
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable File file) {
        loadGeneric(file);
        return this;
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@RawRes @DrawableRes @Nullable Integer num) {
        loadGeneric(num);
        return apply((BaseRequestOptions) RequestOptions.signatureOf(AndroidResourceSignature.obtain(this.context)));
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable Object obj) {
        loadGeneric(obj);
        return this;
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable String str) {
        loadGeneric(str);
        return this;
    }

    @CheckResult
    @Deprecated
    public RequestBuilder load(@Nullable URL url) {
        loadGeneric(url);
        return this;
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable byte[] bArr) {
        loadGeneric(bArr);
        if (!isDiskCacheStrategySet()) {
            this = apply((BaseRequestOptions) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
        }
        return !this.isSkipMemoryCacheSet() ? this.apply((BaseRequestOptions) RequestOptions.skipMemoryCacheOf(true)) : this;
    }

    @NonNull
    public Target preload() {
        return preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @NonNull
    public Target preload(int i, int i2) {
        return into((Target) PreloadTarget.obtain(this.requestManager, i, i2));
    }

    @NonNull
    public FutureTarget submit() {
        return submit(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    @NonNull
    public FutureTarget submit(int i, int i2) {
        RequestFutureTarget requestFutureTarget = new RequestFutureTarget(i, i2);
        return (FutureTarget) into(requestFutureTarget, requestFutureTarget, Executors.directExecutor());
    }

    @CheckResult
    @NonNull
    public RequestBuilder thumbnail(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
        }
        this.thumbSizeMultiplier = Float.valueOf(f);
        return this;
    }

    @CheckResult
    @NonNull
    public RequestBuilder thumbnail(@Nullable RequestBuilder requestBuilder) {
        this.thumbnailBuilder = requestBuilder;
        return this;
    }

    @CheckResult
    @NonNull
    public RequestBuilder thumbnail(@Nullable RequestBuilder... requestBuilderArr) {
        RequestBuilder requestBuilder = null;
        if (requestBuilderArr == null || requestBuilderArr.length == 0) {
            return thumbnail((RequestBuilder) null);
        }
        for (int length = requestBuilderArr.length - 1; length >= 0; length--) {
            RequestBuilder requestBuilder2 = requestBuilderArr[length];
            if (requestBuilder2 != null) {
                requestBuilder = requestBuilder == null ? requestBuilder2 : requestBuilder2.thumbnail(requestBuilder);
            }
        }
        return thumbnail(requestBuilder);
    }

    @CheckResult
    @NonNull
    public RequestBuilder transition(@NonNull TransitionOptions transitionOptions2) {
        Preconditions.checkNotNull(transitionOptions2);
        this.transitionOptions = transitionOptions2;
        this.isDefaultTransitionOptionsSet = false;
        return this;
    }
}

package com.bumptech.glide.request;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.annotation.DrawableRes;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Engine.LoadStatus;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.List;
import java.util.concurrent.Executor;

public final class SingleRequest implements Request, SizeReadyCallback, ResourceCallback {
    private static final String GLIDE_TAG = "Glide";
    private static final boolean IS_VERBOSE_LOGGABLE = Log.isLoggable(TAG, 2);
    private static final String TAG = "Request";
    private final TransitionFactory animationFactory;
    private final Executor callbackExecutor;
    private final Context context;
    private volatile Engine engine;
    @GuardedBy("requestLock")
    @Nullable
    private Drawable errorDrawable;
    @GuardedBy("requestLock")
    @Nullable
    private Drawable fallbackDrawable;
    private final GlideContext glideContext;
    @GuardedBy("requestLock")
    private int height;
    @GuardedBy("requestLock")
    private boolean isCallingCallbacks;
    @GuardedBy("requestLock")
    private LoadStatus loadStatus;
    @Nullable
    private final Object model;
    private final int overrideHeight;
    private final int overrideWidth;
    @GuardedBy("requestLock")
    @Nullable
    private Drawable placeholderDrawable;
    private final Priority priority;
    private final RequestCoordinator requestCoordinator;
    @Nullable
    private final List requestListeners;
    private final Object requestLock;
    private final BaseRequestOptions requestOptions;
    @Nullable
    private RuntimeException requestOrigin;
    @GuardedBy("requestLock")
    private Resource resource;
    @GuardedBy("requestLock")
    private long startTime;
    private final StateVerifier stateVerifier;
    @GuardedBy("requestLock")
    private Status status;
    @Nullable
    private final String tag;
    private final Target target;
    @Nullable
    private final RequestListener targetListener;
    private final Class transcodeClass;
    @GuardedBy("requestLock")
    private int width;

    enum Status {
        PENDING,
        RUNNING,
        WAITING_FOR_SIZE,
        COMPLETE,
        FAILED,
        CLEARED
    }

    private SingleRequest(Context context2, GlideContext glideContext2, @NonNull Object obj, @Nullable Object obj2, Class cls, BaseRequestOptions baseRequestOptions, int i, int i2, Priority priority2, Target target2, @Nullable RequestListener requestListener, @Nullable List list, RequestCoordinator requestCoordinator2, Engine engine2, TransitionFactory transitionFactory, Executor executor) {
        this.tag = IS_VERBOSE_LOGGABLE ? String.valueOf(super.hashCode()) : null;
        this.stateVerifier = StateVerifier.newInstance();
        this.requestLock = obj;
        this.context = context2;
        this.glideContext = glideContext2;
        this.model = obj2;
        this.transcodeClass = cls;
        this.requestOptions = baseRequestOptions;
        this.overrideWidth = i;
        this.overrideHeight = i2;
        this.priority = priority2;
        this.target = target2;
        this.targetListener = requestListener;
        this.requestListeners = list;
        this.requestCoordinator = requestCoordinator2;
        this.engine = engine2;
        this.animationFactory = transitionFactory;
        this.callbackExecutor = executor;
        this.status = Status.PENDING;
        if (this.requestOrigin == null && glideContext2.isLoggingRequestOriginsEnabled()) {
            this.requestOrigin = new RuntimeException("Glide request origin trace");
        }
    }

    @GuardedBy("requestLock")
    private void assertNotCallingCallbacks() {
        if (this.isCallingCallbacks) {
            throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you're trying to start a fallback request when a load fails, use RequestBuilder#error(RequestBuilder). Otherwise consider posting your into() or clear() calls to the main thread using a Handler instead.");
        }
    }

    @GuardedBy("requestLock")
    private boolean canNotifyCleared() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        return requestCoordinator2 == null || requestCoordinator2.canNotifyCleared(this);
    }

    @GuardedBy("requestLock")
    private boolean canNotifyStatusChanged() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        return requestCoordinator2 == null || requestCoordinator2.canNotifyStatusChanged(this);
    }

    @GuardedBy("requestLock")
    private boolean canSetResource() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        return requestCoordinator2 == null || requestCoordinator2.canSetImage(this);
    }

    @GuardedBy("requestLock")
    private void cancel() {
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.target.removeCallback(this);
        LoadStatus loadStatus2 = this.loadStatus;
        if (loadStatus2 != null) {
            loadStatus2.cancel();
            this.loadStatus = null;
        }
    }

    @GuardedBy("requestLock")
    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null) {
            this.errorDrawable = this.requestOptions.getErrorPlaceholder();
            if (this.errorDrawable == null && this.requestOptions.getErrorId() > 0) {
                this.errorDrawable = loadDrawable(this.requestOptions.getErrorId());
            }
        }
        return this.errorDrawable;
    }

    @GuardedBy("requestLock")
    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null) {
            this.fallbackDrawable = this.requestOptions.getFallbackDrawable();
            if (this.fallbackDrawable == null && this.requestOptions.getFallbackId() > 0) {
                this.fallbackDrawable = loadDrawable(this.requestOptions.getFallbackId());
            }
        }
        return this.fallbackDrawable;
    }

    @GuardedBy("requestLock")
    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null) {
            this.placeholderDrawable = this.requestOptions.getPlaceholderDrawable();
            if (this.placeholderDrawable == null && this.requestOptions.getPlaceholderId() > 0) {
                this.placeholderDrawable = loadDrawable(this.requestOptions.getPlaceholderId());
            }
        }
        return this.placeholderDrawable;
    }

    @GuardedBy("requestLock")
    private boolean isFirstReadyResource() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        return requestCoordinator2 == null || !requestCoordinator2.getRoot().isAnyResourceSet();
    }

    @GuardedBy("requestLock")
    private Drawable loadDrawable(@DrawableRes int i) {
        return DrawableDecoderCompat.getDrawable((Context) this.glideContext, i, this.requestOptions.getTheme() != null ? this.requestOptions.getTheme() : this.context.getTheme());
    }

    private void logV(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" this: ");
        sb.append(this.tag);
        Log.v(TAG, sb.toString());
    }

    private static int maybeApplySizeMultiplier(int i, float f) {
        return i == Integer.MIN_VALUE ? i : Math.round(f * ((float) i));
    }

    @GuardedBy("requestLock")
    private void notifyLoadFailed() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        if (requestCoordinator2 != null) {
            requestCoordinator2.onRequestFailed(this);
        }
    }

    @GuardedBy("requestLock")
    private void notifyLoadSuccess() {
        RequestCoordinator requestCoordinator2 = this.requestCoordinator;
        if (requestCoordinator2 != null) {
            requestCoordinator2.onRequestSuccess(this);
        }
    }

    public static SingleRequest obtain(Context context2, GlideContext glideContext2, Object obj, Object obj2, Class cls, BaseRequestOptions baseRequestOptions, int i, int i2, Priority priority2, Target target2, RequestListener requestListener, @Nullable List list, RequestCoordinator requestCoordinator2, Engine engine2, TransitionFactory transitionFactory, Executor executor) {
        SingleRequest singleRequest = new SingleRequest(context2, glideContext2, obj, obj2, cls, baseRequestOptions, i, i2, priority2, target2, requestListener, list, requestCoordinator2, engine2, transitionFactory, executor);
        return singleRequest;
    }

    /* JADX INFO: finally extract failed */
    private void onLoadFailed(GlideException glideException, int i) {
        boolean z;
        this.stateVerifier.throwIfRecycled();
        synchronized (this.requestLock) {
            glideException.setOrigin(this.requestOrigin);
            int logLevel = this.glideContext.getLogLevel();
            if (logLevel <= i) {
                String str = GLIDE_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Load failed for ");
                sb.append(this.model);
                sb.append(" with size [");
                sb.append(this.width);
                sb.append("x");
                sb.append(this.height);
                sb.append("]");
                Log.w(str, sb.toString(), glideException);
                if (logLevel <= 4) {
                    glideException.logRootCauses(GLIDE_TAG);
                }
            }
            this.loadStatus = null;
            this.status = Status.FAILED;
            boolean z2 = true;
            this.isCallingCallbacks = true;
            try {
                if (this.requestListeners != null) {
                    z = false;
                    for (RequestListener onLoadFailed : this.requestListeners) {
                        z |= onLoadFailed.onLoadFailed(glideException, this.model, this.target, isFirstReadyResource());
                    }
                } else {
                    z = false;
                }
                if (this.targetListener == null || !this.targetListener.onLoadFailed(glideException, this.model, this.target, isFirstReadyResource())) {
                    z2 = false;
                }
                if (!z && !z2) {
                    setErrorPlaceholder();
                }
                this.isCallingCallbacks = false;
                notifyLoadFailed();
            } catch (Throwable th) {
                this.isCallingCallbacks = false;
                throw th;
            }
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00ac A[Catch:{ all -> 0x00bd }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @GuardedBy("requestLock")
    private void onResourceReady(Resource resource2, Object obj, DataSource dataSource) {
        boolean z;
        boolean isFirstReadyResource = isFirstReadyResource();
        this.status = Status.COMPLETE;
        this.resource = resource2;
        if (this.glideContext.getLogLevel() <= 3) {
            StringBuilder sb = new StringBuilder();
            sb.append("Finished loading ");
            sb.append(obj.getClass().getSimpleName());
            sb.append(" from ");
            sb.append(dataSource);
            sb.append(" for ");
            sb.append(this.model);
            sb.append(" with size [");
            sb.append(this.width);
            sb.append("x");
            sb.append(this.height);
            sb.append("] in ");
            sb.append(LogTime.getElapsedMillis(this.startTime));
            sb.append(" ms");
            Log.d(GLIDE_TAG, sb.toString());
        }
        boolean z2 = true;
        this.isCallingCallbacks = true;
        try {
            if (this.requestListeners != null) {
                z = false;
                for (RequestListener onResourceReady : this.requestListeners) {
                    z |= onResourceReady.onResourceReady(obj, this.model, this.target, dataSource, isFirstReadyResource);
                }
            } else {
                z = false;
            }
            if (this.targetListener != null) {
                if (this.targetListener.onResourceReady(obj, this.model, this.target, dataSource, isFirstReadyResource)) {
                    if (!z2 && !z) {
                        this.target.onResourceReady(obj, this.animationFactory.build(dataSource, isFirstReadyResource));
                    }
                    this.isCallingCallbacks = false;
                    notifyLoadSuccess();
                }
            }
            z2 = false;
            if (!z2 && !z) {
            }
            this.isCallingCallbacks = false;
            notifyLoadSuccess();
        } catch (Throwable th) {
            this.isCallingCallbacks = false;
            throw th;
        }
    }

    @GuardedBy("requestLock")
    private void setErrorPlaceholder() {
        if (canNotifyStatusChanged()) {
            Drawable drawable = null;
            if (this.model == null) {
                drawable = getFallbackDrawable();
            }
            if (drawable == null) {
                drawable = getErrorDrawable();
            }
            if (drawable == null) {
                drawable = getPlaceholderDrawable();
            }
            this.target.onLoadFailed(drawable);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00a6, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void begin() {
        synchronized (this.requestLock) {
            assertNotCallingCallbacks();
            this.stateVerifier.throwIfRecycled();
            this.startTime = LogTime.getLogTime();
            if (this.model == null) {
                if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                    this.width = this.overrideWidth;
                    this.height = this.overrideHeight;
                }
                onLoadFailed(new GlideException("Received null model"), getFallbackDrawable() == null ? 5 : 3);
            } else if (this.status == Status.RUNNING) {
                throw new IllegalArgumentException("Cannot restart a running request");
            } else if (this.status == Status.COMPLETE) {
                onResourceReady(this.resource, DataSource.MEMORY_CACHE);
            } else {
                this.status = Status.WAITING_FOR_SIZE;
                if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                    onSizeReady(this.overrideWidth, this.overrideHeight);
                } else {
                    this.target.getSize(this);
                }
                if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && canNotifyStatusChanged()) {
                    this.target.onLoadStarted(getPlaceholderDrawable());
                }
                if (IS_VERBOSE_LOGGABLE) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("finished run method in ");
                    sb.append(LogTime.getElapsedMillis(this.startTime));
                    logV(sb.toString());
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
        if (r1 == null) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        r4.engine.release(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clear() {
        Resource resource2;
        synchronized (this.requestLock) {
            assertNotCallingCallbacks();
            this.stateVerifier.throwIfRecycled();
            if (this.status != Status.CLEARED) {
                cancel();
                if (this.resource != null) {
                    resource2 = this.resource;
                    this.resource = null;
                } else {
                    resource2 = null;
                }
                if (canNotifyCleared()) {
                    this.target.onLoadCleared(getPlaceholderDrawable());
                }
                this.status = Status.CLEARED;
            }
        }
    }

    public Object getLock() {
        this.stateVerifier.throwIfRecycled();
        return this.requestLock;
    }

    public boolean isAnyResourceSet() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.status == Status.COMPLETE;
        }
        return z;
    }

    public boolean isCleared() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.status == Status.CLEARED;
        }
        return z;
    }

    public boolean isComplete() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.status == Status.COMPLETE;
        }
        return z;
    }

    public boolean isEquivalentTo(Request request) {
        int i;
        int i2;
        Object obj;
        Class cls;
        BaseRequestOptions baseRequestOptions;
        Priority priority2;
        int size;
        int i3;
        int i4;
        Object obj2;
        Class cls2;
        BaseRequestOptions baseRequestOptions2;
        Priority priority3;
        int size2;
        Request request2 = request;
        if (!(request2 instanceof SingleRequest)) {
            return false;
        }
        synchronized (this.requestLock) {
            i = this.overrideWidth;
            i2 = this.overrideHeight;
            obj = this.model;
            cls = this.transcodeClass;
            baseRequestOptions = this.requestOptions;
            priority2 = this.priority;
            size = this.requestListeners != null ? this.requestListeners.size() : 0;
        }
        SingleRequest singleRequest = (SingleRequest) request2;
        synchronized (singleRequest.requestLock) {
            i3 = singleRequest.overrideWidth;
            i4 = singleRequest.overrideHeight;
            obj2 = singleRequest.model;
            cls2 = singleRequest.transcodeClass;
            baseRequestOptions2 = singleRequest.requestOptions;
            priority3 = singleRequest.priority;
            size2 = singleRequest.requestListeners != null ? singleRequest.requestListeners.size() : 0;
        }
        boolean z = i == i3 && i2 == i4 && Util.bothModelsNullEquivalentOrEquals(obj, obj2) && cls.equals(cls2) && baseRequestOptions.equals(baseRequestOptions2) && priority2 == priority3 && size == size2;
        return z;
    }

    public boolean isRunning() {
        boolean z;
        synchronized (this.requestLock) {
            if (this.status != Status.RUNNING) {
                if (this.status != Status.WAITING_FOR_SIZE) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public void onLoadFailed(GlideException glideException) {
        onLoadFailed(glideException, 5);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004f, code lost:
        if (r6 == null) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0051, code lost:
        r5.engine.release(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0056, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ad, code lost:
        if (r6 == null) goto L_0x00b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00af, code lost:
        r5.engine.release(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00b4, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onResourceReady(Resource resource2, DataSource dataSource) {
        this.stateVerifier.throwIfRecycled();
        Resource resource3 = null;
        try {
            synchronized (this.requestLock) {
                try {
                    this.loadStatus = null;
                    if (resource2 == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Expected to receive a Resource<R> with an object of ");
                        sb.append(this.transcodeClass);
                        sb.append(" inside, but instead got null.");
                        onLoadFailed(new GlideException(sb.toString()));
                        return;
                    }
                    Object obj = resource2.get();
                    if (obj != null) {
                        if (this.transcodeClass.isAssignableFrom(obj.getClass())) {
                            if (!canSetResource()) {
                                this.resource = null;
                                this.status = Status.COMPLETE;
                            } else {
                                onResourceReady(resource2, obj, dataSource);
                                return;
                            }
                        }
                    }
                    try {
                        this.resource = null;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Expected to receive an object of ");
                        sb2.append(this.transcodeClass);
                        sb2.append(" but instead got ");
                        sb2.append(obj != null ? obj.getClass() : "");
                        sb2.append("{");
                        sb2.append(obj);
                        sb2.append("} inside Resource{");
                        sb2.append(resource2);
                        sb2.append("}.");
                        sb2.append(obj != null ? "" : " To indicate failure return a null Resource object, rather than a Resource object containing null data.");
                        onLoadFailed(new GlideException(sb2.toString()));
                    } catch (Throwable th) {
                        th = th;
                        try {
                            throw th;
                        } catch (Throwable th2) {
                            th = th2;
                            resource3 = resource2;
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    resource2 = null;
                    throw th;
                }
            }
        } catch (Throwable th4) {
            th = th4;
            if (resource3 != null) {
                this.engine.release(resource3);
            }
            throw th;
        }
    }

    public void onSizeReady(int i, int i2) {
        Object obj;
        this.stateVerifier.throwIfRecycled();
        Object obj2 = this.requestLock;
        synchronized (obj2) {
            try {
                if (IS_VERBOSE_LOGGABLE) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Got onSizeReady in ");
                    sb.append(LogTime.getElapsedMillis(this.startTime));
                    logV(sb.toString());
                }
                if (this.status == Status.WAITING_FOR_SIZE) {
                    this.status = Status.RUNNING;
                    float sizeMultiplier = this.requestOptions.getSizeMultiplier();
                    this.width = maybeApplySizeMultiplier(i, sizeMultiplier);
                    this.height = maybeApplySizeMultiplier(i2, sizeMultiplier);
                    if (IS_VERBOSE_LOGGABLE) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("finished setup for calling load in ");
                        sb2.append(LogTime.getElapsedMillis(this.startTime));
                        logV(sb2.toString());
                    }
                    obj = obj2;
                    this.loadStatus = this.engine.load(this.glideContext, this.model, this.requestOptions.getSignature(), this.width, this.height, this.requestOptions.getResourceClass(), this.transcodeClass, this.priority, this.requestOptions.getDiskCacheStrategy(), this.requestOptions.getTransformations(), this.requestOptions.isTransformationRequired(), this.requestOptions.isScaleOnlyOrNoTransform(), this.requestOptions.getOptions(), this.requestOptions.isMemoryCacheable(), this.requestOptions.getUseUnlimitedSourceGeneratorsPool(), this.requestOptions.getUseAnimationPool(), this.requestOptions.getOnlyRetrieveFromCache(), this, this.callbackExecutor);
                    if (this.status != Status.RUNNING) {
                        this.loadStatus = null;
                    }
                    if (IS_VERBOSE_LOGGABLE) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("finished onSizeReady in ");
                        sb3.append(LogTime.getElapsedMillis(this.startTime));
                        logV(sb3.toString());
                    }
                }
            } catch (Throwable th) {
                th = th;
                throw th;
            }
        }
    }

    public void pause() {
        synchronized (this.requestLock) {
            if (isRunning()) {
                clear();
            }
        }
    }
}

package com.bumptech.glide;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitor.ConnectivityListener;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RequestManager implements ComponentCallbacks2, LifecycleListener, ModelTypes {
    private static final RequestOptions DECODE_TYPE_BITMAP = ((RequestOptions) RequestOptions.decodeTypeOf(Bitmap.class).lock());
    private static final RequestOptions DECODE_TYPE_GIF = ((RequestOptions) RequestOptions.decodeTypeOf(GifDrawable.class).lock());
    private static final RequestOptions DOWNLOAD_ONLY_OPTIONS = ((RequestOptions) ((RequestOptions) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA).priority(Priority.LOW)).skipMemoryCache(true));
    private final Runnable addSelfToLifecycle;
    private final ConnectivityMonitor connectivityMonitor;
    protected final Context context;
    private final CopyOnWriteArrayList defaultRequestListeners;
    protected final Glide glide;
    final Lifecycle lifecycle;
    private final Handler mainHandler;
    private boolean pauseAllRequestsOnTrimMemoryModerate;
    @GuardedBy("this")
    private RequestOptions requestOptions;
    @GuardedBy("this")
    private final RequestTracker requestTracker;
    @GuardedBy("this")
    private final TargetTracker targetTracker;
    @GuardedBy("this")
    private final RequestManagerTreeNode treeNode;

    class ClearTarget extends CustomViewTarget {
        ClearTarget(@NonNull View view) {
            super(view);
        }

        public void onLoadFailed(@Nullable Drawable drawable) {
        }

        /* access modifiers changed from: protected */
        public void onResourceCleared(@Nullable Drawable drawable) {
        }

        public void onResourceReady(@NonNull Object obj, @Nullable Transition transition) {
        }
    }

    class RequestManagerConnectivityListener implements ConnectivityListener {
        @GuardedBy("RequestManager.this")
        private final RequestTracker requestTracker;

        RequestManagerConnectivityListener(@NonNull RequestTracker requestTracker2) {
            this.requestTracker = requestTracker2;
        }

        public void onConnectivityChanged(boolean z) {
            if (z) {
                synchronized (RequestManager.this) {
                    this.requestTracker.restartRequests();
                }
            }
        }
    }

    public RequestManager(@NonNull Glide glide2, @NonNull Lifecycle lifecycle2, @NonNull RequestManagerTreeNode requestManagerTreeNode, @NonNull Context context2) {
        this(glide2, lifecycle2, requestManagerTreeNode, new RequestTracker(), glide2.getConnectivityMonitorFactory(), context2);
    }

    RequestManager(Glide glide2, Lifecycle lifecycle2, RequestManagerTreeNode requestManagerTreeNode, RequestTracker requestTracker2, ConnectivityMonitorFactory connectivityMonitorFactory, Context context2) {
        this.targetTracker = new TargetTracker();
        this.addSelfToLifecycle = new Runnable() {
            public void run() {
                RequestManager requestManager = RequestManager.this;
                requestManager.lifecycle.addListener(requestManager);
            }
        };
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.glide = glide2;
        this.lifecycle = lifecycle2;
        this.treeNode = requestManagerTreeNode;
        this.requestTracker = requestTracker2;
        this.context = context2;
        this.connectivityMonitor = connectivityMonitorFactory.build(context2.getApplicationContext(), new RequestManagerConnectivityListener(requestTracker2));
        if (Util.isOnBackgroundThread()) {
            this.mainHandler.post(this.addSelfToLifecycle);
        } else {
            lifecycle2.addListener(this);
        }
        lifecycle2.addListener(this.connectivityMonitor);
        this.defaultRequestListeners = new CopyOnWriteArrayList(glide2.getGlideContext().getDefaultRequestListeners());
        setRequestOptions(glide2.getGlideContext().getDefaultRequestOptions());
        glide2.registerRequestManager(this);
    }

    private void untrackOrDelegate(@NonNull Target target) {
        boolean untrack = untrack(target);
        Request request = target.getRequest();
        if (!untrack && !this.glide.removeFromManagers(target) && request != null) {
            target.setRequest(null);
            request.clear();
        }
    }

    private synchronized void updateRequestOptions(@NonNull RequestOptions requestOptions2) {
        this.requestOptions = (RequestOptions) this.requestOptions.apply(requestOptions2);
    }

    public RequestManager addDefaultRequestListener(RequestListener requestListener) {
        this.defaultRequestListeners.add(requestListener);
        return this;
    }

    @NonNull
    public synchronized RequestManager applyDefaultRequestOptions(@NonNull RequestOptions requestOptions2) {
        updateRequestOptions(requestOptions2);
        return this;
    }

    @CheckResult
    @NonNull
    public RequestBuilder as(@NonNull Class cls) {
        return new RequestBuilder(this.glide, this, cls, this.context);
    }

    @CheckResult
    @NonNull
    public RequestBuilder asBitmap() {
        return as(Bitmap.class).apply((BaseRequestOptions) DECODE_TYPE_BITMAP);
    }

    @CheckResult
    @NonNull
    public RequestBuilder asDrawable() {
        return as(Drawable.class);
    }

    @CheckResult
    @NonNull
    public RequestBuilder asFile() {
        return as(File.class).apply((BaseRequestOptions) RequestOptions.skipMemoryCacheOf(true));
    }

    @CheckResult
    @NonNull
    public RequestBuilder asGif() {
        return as(GifDrawable.class).apply((BaseRequestOptions) DECODE_TYPE_GIF);
    }

    public void clear(@NonNull View view) {
        clear((Target) new ClearTarget(view));
    }

    public void clear(@Nullable Target target) {
        if (target != null) {
            untrackOrDelegate(target);
        }
    }

    @CheckResult
    @NonNull
    public RequestBuilder download(@Nullable Object obj) {
        return downloadOnly().load(obj);
    }

    @CheckResult
    @NonNull
    public RequestBuilder downloadOnly() {
        return as(File.class).apply((BaseRequestOptions) DOWNLOAD_ONLY_OPTIONS);
    }

    /* access modifiers changed from: 0000 */
    public List getDefaultRequestListeners() {
        return this.defaultRequestListeners;
    }

    /* access modifiers changed from: 0000 */
    public synchronized RequestOptions getDefaultRequestOptions() {
        return this.requestOptions;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public TransitionOptions getDefaultTransitionOptions(Class cls) {
        return this.glide.getGlideContext().getDefaultTransitionOptions(cls);
    }

    public synchronized boolean isPaused() {
        return this.requestTracker.isPaused();
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable Bitmap bitmap) {
        return asDrawable().load(bitmap);
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable Drawable drawable) {
        return asDrawable().load(drawable);
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable Uri uri) {
        return asDrawable().load(uri);
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable File file) {
        return asDrawable().load(file);
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@RawRes @DrawableRes @Nullable Integer num) {
        return asDrawable().load(num);
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable Object obj) {
        return asDrawable().load(obj);
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable String str) {
        return asDrawable().load(str);
    }

    @CheckResult
    @Deprecated
    public RequestBuilder load(@Nullable URL url) {
        return asDrawable().load(url);
    }

    @CheckResult
    @NonNull
    public RequestBuilder load(@Nullable byte[] bArr) {
        return asDrawable().load(bArr);
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public synchronized void onDestroy() {
        this.targetTracker.onDestroy();
        for (Target clear : this.targetTracker.getAll()) {
            clear(clear);
        }
        this.targetTracker.clear();
        this.requestTracker.clearRequests();
        this.lifecycle.removeListener(this);
        this.lifecycle.removeListener(this.connectivityMonitor);
        this.mainHandler.removeCallbacks(this.addSelfToLifecycle);
        this.glide.unregisterRequestManager(this);
    }

    public void onLowMemory() {
    }

    public synchronized void onStart() {
        resumeRequests();
        this.targetTracker.onStart();
    }

    public synchronized void onStop() {
        pauseRequests();
        this.targetTracker.onStop();
    }

    public void onTrimMemory(int i) {
        if (i == 60 && this.pauseAllRequestsOnTrimMemoryModerate) {
            pauseAllRequestsRecursive();
        }
    }

    public synchronized void pauseAllRequests() {
        this.requestTracker.pauseAllRequests();
    }

    public synchronized void pauseAllRequestsRecursive() {
        pauseAllRequests();
        for (RequestManager pauseAllRequests : this.treeNode.getDescendants()) {
            pauseAllRequests.pauseAllRequests();
        }
    }

    public synchronized void pauseRequests() {
        this.requestTracker.pauseRequests();
    }

    public synchronized void pauseRequestsRecursive() {
        pauseRequests();
        for (RequestManager pauseRequests : this.treeNode.getDescendants()) {
            pauseRequests.pauseRequests();
        }
    }

    public synchronized void resumeRequests() {
        this.requestTracker.resumeRequests();
    }

    public synchronized void resumeRequestsRecursive() {
        Util.assertMainThread();
        resumeRequests();
        for (RequestManager resumeRequests : this.treeNode.getDescendants()) {
            resumeRequests.resumeRequests();
        }
    }

    @NonNull
    public synchronized RequestManager setDefaultRequestOptions(@NonNull RequestOptions requestOptions2) {
        setRequestOptions(requestOptions2);
        return this;
    }

    public void setPauseAllRequestsOnTrimMemoryModerate(boolean z) {
        this.pauseAllRequestsOnTrimMemoryModerate = z;
    }

    /* access modifiers changed from: protected */
    public synchronized void setRequestOptions(@NonNull RequestOptions requestOptions2) {
        this.requestOptions = (RequestOptions) ((RequestOptions) requestOptions2.clone()).autoClone();
    }

    public synchronized String toString() {
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("{tracker=");
        sb.append(this.requestTracker);
        sb.append(", treeNode=");
        sb.append(this.treeNode);
        sb.append("}");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void track(@NonNull Target target, @NonNull Request request) {
        this.targetTracker.track(target);
        this.requestTracker.runRequest(request);
    }

    /* access modifiers changed from: 0000 */
    public synchronized boolean untrack(@NonNull Target target) {
        Request request = target.getRequest();
        if (request == null) {
            return true;
        }
        if (!this.requestTracker.clearAndRemove(request)) {
            return false;
        }
        this.targetTracker.untrack(target);
        target.setRequest(null);
        return true;
    }
}

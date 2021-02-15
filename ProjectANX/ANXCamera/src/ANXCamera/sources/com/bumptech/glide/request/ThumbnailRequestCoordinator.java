package com.bumptech.glide.request;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import com.bumptech.glide.request.RequestCoordinator.RequestState;

public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {
    private volatile Request full;
    @GuardedBy("requestLock")
    private RequestState fullState;
    @GuardedBy("requestLock")
    private boolean isRunningDuringBegin;
    @Nullable
    private final RequestCoordinator parent;
    private final Object requestLock;
    private volatile Request thumb;
    @GuardedBy("requestLock")
    private RequestState thumbState;

    public ThumbnailRequestCoordinator(Object obj, @Nullable RequestCoordinator requestCoordinator) {
        RequestState requestState = RequestState.CLEARED;
        this.fullState = requestState;
        this.thumbState = requestState;
        this.requestLock = obj;
        this.parent = requestCoordinator;
    }

    @GuardedBy("requestLock")
    private boolean parentCanNotifyCleared() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canNotifyCleared(this);
    }

    @GuardedBy("requestLock")
    private boolean parentCanNotifyStatusChanged() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this);
    }

    @GuardedBy("requestLock")
    private boolean parentCanSetImage() {
        RequestCoordinator requestCoordinator = this.parent;
        return requestCoordinator == null || requestCoordinator.canSetImage(this);
    }

    public void begin() {
        synchronized (this.requestLock) {
            this.isRunningDuringBegin = true;
            try {
                if (!(this.fullState == RequestState.SUCCESS || this.thumbState == RequestState.RUNNING)) {
                    this.thumbState = RequestState.RUNNING;
                    this.thumb.begin();
                }
                if (this.isRunningDuringBegin && this.fullState != RequestState.RUNNING) {
                    this.fullState = RequestState.RUNNING;
                    this.full.begin();
                }
            } finally {
                this.isRunningDuringBegin = false;
            }
        }
    }

    public boolean canNotifyCleared(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanNotifyCleared() && request.equals(this.full) && this.fullState != RequestState.PAUSED;
        }
        return z;
    }

    public boolean canNotifyStatusChanged(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanNotifyStatusChanged() && request.equals(this.full) && !isAnyResourceSet();
        }
        return z;
    }

    public boolean canSetImage(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanSetImage() && (request.equals(this.full) || this.fullState != RequestState.SUCCESS);
        }
        return z;
    }

    public void clear() {
        synchronized (this.requestLock) {
            this.isRunningDuringBegin = false;
            this.fullState = RequestState.CLEARED;
            this.thumbState = RequestState.CLEARED;
            this.thumb.clear();
            this.full.clear();
        }
    }

    /* JADX WARNING: type inference failed for: r2v4, types: [com.bumptech.glide.request.RequestCoordinator] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public RequestCoordinator getRoot() {
        synchronized (this.requestLock) {
            if (this.parent != null) {
                r2 = this.parent.getRoot();
            }
        }
        return r2;
    }

    public boolean isAnyResourceSet() {
        boolean z;
        synchronized (this.requestLock) {
            if (!this.thumb.isAnyResourceSet()) {
                if (!this.full.isAnyResourceSet()) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public boolean isCleared() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.fullState == RequestState.CLEARED;
        }
        return z;
    }

    public boolean isComplete() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.fullState == RequestState.SUCCESS;
        }
        return z;
    }

    public boolean isEquivalentTo(Request request) {
        if (!(request instanceof ThumbnailRequestCoordinator)) {
            return false;
        }
        ThumbnailRequestCoordinator thumbnailRequestCoordinator = (ThumbnailRequestCoordinator) request;
        if (this.full == null) {
            if (thumbnailRequestCoordinator.full != null) {
                return false;
            }
        } else if (!this.full.isEquivalentTo(thumbnailRequestCoordinator.full)) {
            return false;
        }
        if (this.thumb == null) {
            if (thumbnailRequestCoordinator.thumb != null) {
                return false;
            }
        } else if (!this.thumb.isEquivalentTo(thumbnailRequestCoordinator.thumb)) {
            return false;
        }
        return true;
    }

    public boolean isRunning() {
        boolean z;
        synchronized (this.requestLock) {
            z = this.fullState == RequestState.RUNNING;
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onRequestFailed(Request request) {
        synchronized (this.requestLock) {
            if (!request.equals(this.full)) {
                this.thumbState = RequestState.FAILED;
                return;
            }
            this.fullState = RequestState.FAILED;
            if (this.parent != null) {
                this.parent.onRequestFailed(this);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onRequestSuccess(Request request) {
        synchronized (this.requestLock) {
            if (request.equals(this.thumb)) {
                this.thumbState = RequestState.SUCCESS;
                return;
            }
            this.fullState = RequestState.SUCCESS;
            if (this.parent != null) {
                this.parent.onRequestSuccess(this);
            }
            if (!this.thumbState.isComplete()) {
                this.thumb.clear();
            }
        }
    }

    public void pause() {
        synchronized (this.requestLock) {
            if (!this.thumbState.isComplete()) {
                this.thumbState = RequestState.PAUSED;
                this.thumb.pause();
            }
            if (!this.fullState.isComplete()) {
                this.fullState = RequestState.PAUSED;
                this.full.pause();
            }
        }
    }

    public void setRequests(Request request, Request request2) {
        this.full = request;
        this.thumb = request2;
    }
}

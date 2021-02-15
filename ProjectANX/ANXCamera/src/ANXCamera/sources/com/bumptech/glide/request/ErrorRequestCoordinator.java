package com.bumptech.glide.request;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import com.bumptech.glide.request.RequestCoordinator.RequestState;

public final class ErrorRequestCoordinator implements RequestCoordinator, Request {
    private volatile Request error;
    @GuardedBy("requestLock")
    private RequestState errorState;
    @Nullable
    private final RequestCoordinator parent;
    private volatile Request primary;
    @GuardedBy("requestLock")
    private RequestState primaryState;
    private final Object requestLock;

    public ErrorRequestCoordinator(Object obj, @Nullable RequestCoordinator requestCoordinator) {
        RequestState requestState = RequestState.CLEARED;
        this.primaryState = requestState;
        this.errorState = requestState;
        this.requestLock = obj;
        this.parent = requestCoordinator;
    }

    @GuardedBy("requestLock")
    private boolean isValidRequest(Request request) {
        return request.equals(this.primary) || (this.primaryState == RequestState.FAILED && request.equals(this.error));
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
            if (this.primaryState != RequestState.RUNNING) {
                this.primaryState = RequestState.RUNNING;
                this.primary.begin();
            }
        }
    }

    public boolean canNotifyCleared(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanNotifyCleared() && isValidRequest(request);
        }
        return z;
    }

    public boolean canNotifyStatusChanged(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanNotifyStatusChanged() && isValidRequest(request);
        }
        return z;
    }

    public boolean canSetImage(Request request) {
        boolean z;
        synchronized (this.requestLock) {
            z = parentCanSetImage() && isValidRequest(request);
        }
        return z;
    }

    public void clear() {
        synchronized (this.requestLock) {
            this.primaryState = RequestState.CLEARED;
            this.primary.clear();
            if (this.errorState != RequestState.CLEARED) {
                this.errorState = RequestState.CLEARED;
                this.error.clear();
            }
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
            if (!this.primary.isAnyResourceSet()) {
                if (!this.error.isAnyResourceSet()) {
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
            z = this.primaryState == RequestState.CLEARED && this.errorState == RequestState.CLEARED;
        }
        return z;
    }

    public boolean isComplete() {
        boolean z;
        synchronized (this.requestLock) {
            if (this.primaryState != RequestState.SUCCESS) {
                if (this.errorState != RequestState.SUCCESS) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public boolean isEquivalentTo(Request request) {
        if (!(request instanceof ErrorRequestCoordinator)) {
            return false;
        }
        ErrorRequestCoordinator errorRequestCoordinator = (ErrorRequestCoordinator) request;
        return this.primary.isEquivalentTo(errorRequestCoordinator.primary) && this.error.isEquivalentTo(errorRequestCoordinator.error);
    }

    public boolean isRunning() {
        boolean z;
        synchronized (this.requestLock) {
            if (this.primaryState != RequestState.RUNNING) {
                if (this.errorState != RequestState.RUNNING) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002e, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onRequestFailed(Request request) {
        synchronized (this.requestLock) {
            if (!request.equals(this.error)) {
                this.primaryState = RequestState.FAILED;
                if (this.errorState != RequestState.RUNNING) {
                    this.errorState = RequestState.RUNNING;
                    this.error.begin();
                }
            } else {
                this.errorState = RequestState.FAILED;
                if (this.parent != null) {
                    this.parent.onRequestFailed(this);
                }
            }
        }
    }

    public void onRequestSuccess(Request request) {
        synchronized (this.requestLock) {
            if (request.equals(this.primary)) {
                this.primaryState = RequestState.SUCCESS;
            } else if (request.equals(this.error)) {
                this.errorState = RequestState.SUCCESS;
            }
            if (this.parent != null) {
                this.parent.onRequestSuccess(this);
            }
        }
    }

    public void pause() {
        synchronized (this.requestLock) {
            if (this.primaryState == RequestState.RUNNING) {
                this.primaryState = RequestState.PAUSED;
                this.primary.pause();
            }
            if (this.errorState == RequestState.RUNNING) {
                this.errorState = RequestState.PAUSED;
                this.error.pause();
            }
        }
    }

    public void setRequests(Request request, Request request2) {
        this.primary = request;
        this.error = request2;
    }
}

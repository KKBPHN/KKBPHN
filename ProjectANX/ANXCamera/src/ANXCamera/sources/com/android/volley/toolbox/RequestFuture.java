package com.android.volley.toolbox;

import android.os.SystemClock;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestFuture implements Future, Listener, ErrorListener {
    private VolleyError mException;
    private Request mRequest;
    private Object mResult;
    private boolean mResultReceived = false;

    private RequestFuture() {
    }

    private synchronized Object doGet(Long l) {
        if (this.mException != null) {
            throw new ExecutionException(this.mException);
        } else if (this.mResultReceived) {
            return this.mResult;
        } else {
            if (l == null) {
                while (!isDone()) {
                    wait(0);
                }
            } else if (l.longValue() > 0) {
                long uptimeMillis = SystemClock.uptimeMillis();
                long longValue = l.longValue() + uptimeMillis;
                while (!isDone() && uptimeMillis < longValue) {
                    wait(longValue - uptimeMillis);
                    uptimeMillis = SystemClock.uptimeMillis();
                }
            }
            if (this.mException != null) {
                throw new ExecutionException(this.mException);
            } else if (this.mResultReceived) {
                return this.mResult;
            } else {
                throw new TimeoutException();
            }
        }
    }

    public static RequestFuture newFuture() {
        return new RequestFuture();
    }

    public synchronized boolean cancel(boolean z) {
        if (this.mRequest == null) {
            return false;
        }
        if (isDone()) {
            return false;
        }
        this.mRequest.cancel();
        return true;
    }

    public Object get() {
        try {
            return doGet(null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    public Object get(long j, TimeUnit timeUnit) {
        return doGet(Long.valueOf(TimeUnit.MILLISECONDS.convert(j, timeUnit)));
    }

    public boolean isCancelled() {
        Request request = this.mRequest;
        if (request == null) {
            return false;
        }
        return request.isCanceled();
    }

    public synchronized boolean isDone() {
        boolean z;
        z = this.mResultReceived || this.mException != null || isCancelled();
        return z;
    }

    public synchronized void onErrorResponse(VolleyError volleyError) {
        this.mException = volleyError;
        notifyAll();
    }

    public synchronized void onResponse(Object obj) {
        this.mResultReceived = true;
        this.mResult = obj;
        notifyAll();
    }

    public void setRequest(Request request) {
        this.mRequest = request;
    }
}

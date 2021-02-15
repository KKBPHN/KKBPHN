package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class GifFrameLoader {
    private final BitmapPool bitmapPool;
    private final List callbacks;
    private DelayTarget current;
    private Bitmap firstFrame;
    private int firstFrameSize;
    private final GifDecoder gifDecoder;
    private final Handler handler;
    private int height;
    private boolean isCleared;
    private boolean isLoadPending;
    private boolean isRunning;
    private DelayTarget next;
    @Nullable
    private OnEveryFrameListener onEveryFrameListener;
    private DelayTarget pendingTarget;
    private RequestBuilder requestBuilder;
    final RequestManager requestManager;
    private boolean startFromFirstFrame;
    private Transformation transformation;
    private int width;

    @VisibleForTesting
    class DelayTarget extends CustomTarget {
        private final Handler handler;
        final int index;
        private Bitmap resource;
        private final long targetTime;

        DelayTarget(Handler handler2, int i, long j) {
            this.handler = handler2;
            this.index = i;
            this.targetTime = j;
        }

        /* access modifiers changed from: 0000 */
        public Bitmap getResource() {
            return this.resource;
        }

        public void onLoadCleared(@Nullable Drawable drawable) {
            this.resource = null;
        }

        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition transition) {
            this.resource = bitmap;
            this.handler.sendMessageAtTime(this.handler.obtainMessage(1, this), this.targetTime);
        }
    }

    public interface FrameCallback {
        void onFrameReady();
    }

    class FrameLoaderCallback implements Callback {
        static final int MSG_CLEAR = 2;
        static final int MSG_DELAY = 1;

        FrameLoaderCallback() {
        }

        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                GifFrameLoader.this.onFrameReady((DelayTarget) message.obj);
                return true;
            }
            if (i == 2) {
                GifFrameLoader.this.requestManager.clear((Target) (DelayTarget) message.obj);
            }
            return false;
        }
    }

    @VisibleForTesting
    interface OnEveryFrameListener {
        void onFrameReady();
    }

    GifFrameLoader(Glide glide, GifDecoder gifDecoder2, int i, int i2, Transformation transformation2, Bitmap bitmap) {
        this(glide.getBitmapPool(), Glide.with(glide.getContext()), gifDecoder2, null, getRequestBuilder(Glide.with(glide.getContext()), i, i2), transformation2, bitmap);
    }

    GifFrameLoader(BitmapPool bitmapPool2, RequestManager requestManager2, GifDecoder gifDecoder2, Handler handler2, RequestBuilder requestBuilder2, Transformation transformation2, Bitmap bitmap) {
        this.callbacks = new ArrayList();
        this.requestManager = requestManager2;
        if (handler2 == null) {
            handler2 = new Handler(Looper.getMainLooper(), new FrameLoaderCallback());
        }
        this.bitmapPool = bitmapPool2;
        this.handler = handler2;
        this.requestBuilder = requestBuilder2;
        this.gifDecoder = gifDecoder2;
        setFrameTransformation(transformation2, bitmap);
    }

    private static Key getFrameSignature() {
        return new ObjectKey(Double.valueOf(Math.random()));
    }

    private static RequestBuilder getRequestBuilder(RequestManager requestManager2, int i, int i2) {
        return requestManager2.asBitmap().apply(((RequestOptions) ((RequestOptions) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).useAnimationPool(true)).skipMemoryCache(true)).override(i, i2));
    }

    private void loadNextFrame() {
        if (this.isRunning && !this.isLoadPending) {
            if (this.startFromFirstFrame) {
                Preconditions.checkArgument(this.pendingTarget == null, "Pending target must be null when starting from the first frame");
                this.gifDecoder.resetFrameIndex();
                this.startFromFirstFrame = false;
            }
            DelayTarget delayTarget = this.pendingTarget;
            if (delayTarget != null) {
                this.pendingTarget = null;
                onFrameReady(delayTarget);
                return;
            }
            this.isLoadPending = true;
            long uptimeMillis = SystemClock.uptimeMillis() + ((long) this.gifDecoder.getNextDelay());
            this.gifDecoder.advance();
            this.next = new DelayTarget(this.handler, this.gifDecoder.getCurrentFrameIndex(), uptimeMillis);
            this.requestBuilder.apply((BaseRequestOptions) RequestOptions.signatureOf(getFrameSignature())).load((Object) this.gifDecoder).into((Target) this.next);
        }
    }

    private void recycleFirstFrame() {
        Bitmap bitmap = this.firstFrame;
        if (bitmap != null) {
            this.bitmapPool.put(bitmap);
            this.firstFrame = null;
        }
    }

    private void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.isCleared = false;
            loadNextFrame();
        }
    }

    private void stop() {
        this.isRunning = false;
    }

    /* access modifiers changed from: 0000 */
    public void clear() {
        this.callbacks.clear();
        recycleFirstFrame();
        stop();
        DelayTarget delayTarget = this.current;
        if (delayTarget != null) {
            this.requestManager.clear((Target) delayTarget);
            this.current = null;
        }
        DelayTarget delayTarget2 = this.next;
        if (delayTarget2 != null) {
            this.requestManager.clear((Target) delayTarget2);
            this.next = null;
        }
        DelayTarget delayTarget3 = this.pendingTarget;
        if (delayTarget3 != null) {
            this.requestManager.clear((Target) delayTarget3);
            this.pendingTarget = null;
        }
        this.gifDecoder.clear();
        this.isCleared = true;
    }

    /* access modifiers changed from: 0000 */
    public ByteBuffer getBuffer() {
        return this.gifDecoder.getData().asReadOnlyBuffer();
    }

    /* access modifiers changed from: 0000 */
    public Bitmap getCurrentFrame() {
        DelayTarget delayTarget = this.current;
        return delayTarget != null ? delayTarget.getResource() : this.firstFrame;
    }

    /* access modifiers changed from: 0000 */
    public int getCurrentIndex() {
        DelayTarget delayTarget = this.current;
        if (delayTarget != null) {
            return delayTarget.index;
        }
        return -1;
    }

    /* access modifiers changed from: 0000 */
    public Bitmap getFirstFrame() {
        return this.firstFrame;
    }

    /* access modifiers changed from: 0000 */
    public int getFrameCount() {
        return this.gifDecoder.getFrameCount();
    }

    /* access modifiers changed from: 0000 */
    public Transformation getFrameTransformation() {
        return this.transformation;
    }

    /* access modifiers changed from: 0000 */
    public int getHeight() {
        return this.height;
    }

    /* access modifiers changed from: 0000 */
    public int getLoopCount() {
        return this.gifDecoder.getTotalIterationCount();
    }

    /* access modifiers changed from: 0000 */
    public int getSize() {
        return this.gifDecoder.getByteSize() + this.firstFrameSize;
    }

    /* access modifiers changed from: 0000 */
    public int getWidth() {
        return this.width;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void onFrameReady(DelayTarget delayTarget) {
        OnEveryFrameListener onEveryFrameListener2 = this.onEveryFrameListener;
        if (onEveryFrameListener2 != null) {
            onEveryFrameListener2.onFrameReady();
        }
        this.isLoadPending = false;
        if (this.isCleared) {
            this.handler.obtainMessage(2, delayTarget).sendToTarget();
        } else if (!this.isRunning) {
            this.pendingTarget = delayTarget;
        } else {
            if (delayTarget.getResource() != null) {
                recycleFirstFrame();
                DelayTarget delayTarget2 = this.current;
                this.current = delayTarget;
                for (int size = this.callbacks.size() - 1; size >= 0; size--) {
                    ((FrameCallback) this.callbacks.get(size)).onFrameReady();
                }
                if (delayTarget2 != null) {
                    this.handler.obtainMessage(2, delayTarget2).sendToTarget();
                }
            }
            loadNextFrame();
        }
    }

    /* access modifiers changed from: 0000 */
    public void setFrameTransformation(Transformation transformation2, Bitmap bitmap) {
        Preconditions.checkNotNull(transformation2);
        this.transformation = transformation2;
        Preconditions.checkNotNull(bitmap);
        this.firstFrame = bitmap;
        this.requestBuilder = this.requestBuilder.apply(new RequestOptions().transform(transformation2));
        this.firstFrameSize = Util.getBitmapByteSize(bitmap);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    /* access modifiers changed from: 0000 */
    public void setNextStartFromFirstFrame() {
        Preconditions.checkArgument(!this.isRunning, "Can't restart a running animation");
        this.startFromFirstFrame = true;
        DelayTarget delayTarget = this.pendingTarget;
        if (delayTarget != null) {
            this.requestManager.clear((Target) delayTarget);
            this.pendingTarget = null;
        }
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void setOnEveryFrameReadyListener(@Nullable OnEveryFrameListener onEveryFrameListener2) {
        this.onEveryFrameListener = onEveryFrameListener2;
    }

    /* access modifiers changed from: 0000 */
    public void subscribe(FrameCallback frameCallback) {
        if (this.isCleared) {
            throw new IllegalStateException("Cannot subscribe to a cleared frame loader");
        } else if (!this.callbacks.contains(frameCallback)) {
            boolean isEmpty = this.callbacks.isEmpty();
            this.callbacks.add(frameCallback);
            if (isEmpty) {
                start();
            }
        } else {
            throw new IllegalStateException("Cannot subscribe twice in a row");
        }
    }

    /* access modifiers changed from: 0000 */
    public void unsubscribe(FrameCallback frameCallback) {
        this.callbacks.remove(frameCallback);
        if (this.callbacks.isEmpty()) {
            stop();
        }
    }
}

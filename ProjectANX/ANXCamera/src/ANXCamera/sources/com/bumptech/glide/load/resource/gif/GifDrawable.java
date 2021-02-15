package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat.AnimationCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.gif.GifFrameLoader.FrameCallback;
import com.bumptech.glide.util.Preconditions;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class GifDrawable extends Drawable implements FrameCallback, Animatable, Animatable2Compat {
    private static final int GRAVITY = 119;
    public static final int LOOP_FOREVER = -1;
    public static final int LOOP_INTRINSIC = 0;
    private List animationCallbacks;
    private boolean applyGravity;
    private Rect destRect;
    private boolean isRecycled;
    private boolean isRunning;
    private boolean isStarted;
    private boolean isVisible;
    private int loopCount;
    private int maxLoopCount;
    private Paint paint;
    private final GifState state;

    final class GifState extends ConstantState {
        @VisibleForTesting
        final GifFrameLoader frameLoader;

        GifState(GifFrameLoader gifFrameLoader) {
            this.frameLoader = gifFrameLoader;
        }

        public int getChangingConfigurations() {
            return 0;
        }

        @NonNull
        public Drawable newDrawable() {
            return new GifDrawable(this);
        }

        @NonNull
        public Drawable newDrawable(Resources resources) {
            return newDrawable();
        }
    }

    public GifDrawable(Context context, GifDecoder gifDecoder, Transformation transformation, int i, int i2, Bitmap bitmap) {
        GifFrameLoader gifFrameLoader = new GifFrameLoader(Glide.get(context), gifDecoder, i, i2, transformation, bitmap);
        this(new GifState(gifFrameLoader));
    }

    @Deprecated
    public GifDrawable(Context context, GifDecoder gifDecoder, BitmapPool bitmapPool, Transformation transformation, int i, int i2, Bitmap bitmap) {
        this(context, gifDecoder, transformation, i, i2, bitmap);
    }

    GifDrawable(GifState gifState) {
        this.isVisible = true;
        this.maxLoopCount = -1;
        Preconditions.checkNotNull(gifState);
        this.state = gifState;
    }

    @VisibleForTesting
    GifDrawable(GifFrameLoader gifFrameLoader, Paint paint2) {
        this(new GifState(gifFrameLoader));
        this.paint = paint2;
    }

    private Callback findCallback() {
        while (true) {
            Callback callback = this.getCallback();
            if (!(callback instanceof Drawable)) {
                return callback;
            }
            this = (Drawable) callback;
        }
    }

    private Rect getDestRect() {
        if (this.destRect == null) {
            this.destRect = new Rect();
        }
        return this.destRect;
    }

    private Paint getPaint() {
        if (this.paint == null) {
            this.paint = new Paint(2);
        }
        return this.paint;
    }

    private void notifyAnimationEndToListeners() {
        List list = this.animationCallbacks;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ((AnimationCallback) this.animationCallbacks.get(i)).onAnimationEnd(this);
            }
        }
    }

    private void resetLoopCount() {
        this.loopCount = 0;
    }

    private void startRunning() {
        Preconditions.checkArgument(!this.isRecycled, "You cannot start a recycled Drawable. Ensure thatyou clear any references to the Drawable when clearing the corresponding request.");
        if (this.state.frameLoader.getFrameCount() != 1) {
            if (!this.isRunning) {
                this.isRunning = true;
                this.state.frameLoader.subscribe(this);
            } else {
                return;
            }
        }
        invalidateSelf();
    }

    private void stopRunning() {
        this.isRunning = false;
        this.state.frameLoader.unsubscribe(this);
    }

    public void clearAnimationCallbacks() {
        List list = this.animationCallbacks;
        if (list != null) {
            list.clear();
        }
    }

    public void draw(@NonNull Canvas canvas) {
        if (!this.isRecycled) {
            if (this.applyGravity) {
                Gravity.apply(119, getIntrinsicWidth(), getIntrinsicHeight(), getBounds(), getDestRect());
                this.applyGravity = false;
            }
            canvas.drawBitmap(this.state.frameLoader.getCurrentFrame(), null, getDestRect(), getPaint());
        }
    }

    public ByteBuffer getBuffer() {
        return this.state.frameLoader.getBuffer();
    }

    public ConstantState getConstantState() {
        return this.state;
    }

    public Bitmap getFirstFrame() {
        return this.state.frameLoader.getFirstFrame();
    }

    public int getFrameCount() {
        return this.state.frameLoader.getFrameCount();
    }

    public int getFrameIndex() {
        return this.state.frameLoader.getCurrentIndex();
    }

    public Transformation getFrameTransformation() {
        return this.state.frameLoader.getFrameTransformation();
    }

    public int getIntrinsicHeight() {
        return this.state.frameLoader.getHeight();
    }

    public int getIntrinsicWidth() {
        return this.state.frameLoader.getWidth();
    }

    public int getOpacity() {
        return -2;
    }

    public int getSize() {
        return this.state.frameLoader.getSize();
    }

    /* access modifiers changed from: 0000 */
    public boolean isRecycled() {
        return this.isRecycled;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.applyGravity = true;
    }

    public void onFrameReady() {
        if (findCallback() == null) {
            stop();
            invalidateSelf();
            return;
        }
        invalidateSelf();
        if (getFrameIndex() == getFrameCount() - 1) {
            this.loopCount++;
        }
        int i = this.maxLoopCount;
        if (i != -1 && this.loopCount >= i) {
            notifyAnimationEndToListeners();
            stop();
        }
    }

    public void recycle() {
        this.isRecycled = true;
        this.state.frameLoader.clear();
    }

    public void registerAnimationCallback(@NonNull AnimationCallback animationCallback) {
        if (animationCallback != null) {
            if (this.animationCallbacks == null) {
                this.animationCallbacks = new ArrayList();
            }
            this.animationCallbacks.add(animationCallback);
        }
    }

    public void setAlpha(int i) {
        getPaint().setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        getPaint().setColorFilter(colorFilter);
    }

    public void setFrameTransformation(Transformation transformation, Bitmap bitmap) {
        this.state.frameLoader.setFrameTransformation(transformation, bitmap);
    }

    /* access modifiers changed from: 0000 */
    public void setIsRunning(boolean z) {
        this.isRunning = z;
    }

    public void setLoopCount(int i) {
        if (i > 0 || i == -1 || i == 0) {
            if (i == 0) {
                i = this.state.frameLoader.getLoopCount();
                if (i == 0) {
                    i = -1;
                }
            }
            this.maxLoopCount = i;
            return;
        }
        throw new IllegalArgumentException("Loop count must be greater than 0, or equal to GlideDrawable.LOOP_FOREVER, or equal to GlideDrawable.LOOP_INTRINSIC");
    }

    public boolean setVisible(boolean z, boolean z2) {
        Preconditions.checkArgument(!this.isRecycled, "Cannot change the visibility of a recycled resource. Ensure that you unset the Drawable from your View before changing the View's visibility.");
        this.isVisible = z;
        if (!z) {
            stopRunning();
        } else if (this.isStarted) {
            startRunning();
        }
        return super.setVisible(z, z2);
    }

    public void start() {
        this.isStarted = true;
        resetLoopCount();
        if (this.isVisible) {
            startRunning();
        }
    }

    public void startFromFirstFrame() {
        Preconditions.checkArgument(!this.isRunning, "You cannot restart a currently running animation.");
        this.state.frameLoader.setNextStartFromFirstFrame();
        start();
    }

    public void stop() {
        this.isStarted = false;
        stopRunning();
    }

    public boolean unregisterAnimationCallback(@NonNull AnimationCallback animationCallback) {
        List list = this.animationCallbacks;
        if (list == null || animationCallback == null) {
            return false;
        }
        return list.remove(animationCallback);
    }
}

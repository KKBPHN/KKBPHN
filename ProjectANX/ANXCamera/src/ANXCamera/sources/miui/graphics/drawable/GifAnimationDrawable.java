package miui.graphics.drawable;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.net.Uri;
import android.os.SystemClock;
import com.miui.internal.graphics.gif.DecodeGifImageHelper;
import com.miui.internal.graphics.gif.DecodeGifImageHelper.GifDecodeResult;
import com.miui.internal.graphics.gif.DecodeGifImageHelper.GifFrame;
import com.miui.internal.graphics.gif.GifDecoder;
import java.util.ArrayList;
import miui.io.ResettableInputStream;

public class GifAnimationDrawable extends AnimationDrawable {
    private int mCurrentFrame;
    private DrawableContainerState mDrawableContainerState;
    private ArrayList mDurations = new ArrayList();
    private final DecodeGifImageHelper mHelper = new DecodeGifImageHelper();
    private ArrayList mOriginalDurations = new ArrayList();
    private Resources mResources;

    private boolean handleFirstDecodeResult(GifDecodeResult gifDecodeResult) {
        GifDecoder gifDecoder = gifDecodeResult.mGifDecoder;
        if (gifDecoder == null || !gifDecodeResult.mIsDecodeOk) {
            return false;
        }
        this.mHelper.mDecodedAllFrames = gifDecoder.isDecodeToTheEnd();
        int frameCount = gifDecoder.getFrameCount();
        if (frameCount <= 0) {
            return false;
        }
        for (int i = 0; i < frameCount; i++) {
            if (this.mHelper.mDecodedAllFrames) {
                addFrame(new BitmapDrawable(this.mResources, gifDecoder.getFrame(i)), gifDecoder.getDelay(i));
            } else {
                this.mHelper.mFrames.add(new GifFrame(gifDecoder.getFrame(i), gifDecoder.getDelay(i), i));
            }
        }
        DecodeGifImageHelper decodeGifImageHelper = this.mHelper;
        if (!decodeGifImageHelper.mDecodedAllFrames) {
            decodeGifImageHelper.firstDecodeNextFrames();
            GifFrame gifFrame = (GifFrame) this.mHelper.mFrames.get(0);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(this.mResources, gifFrame.mImage);
            addFrame(bitmapDrawable, gifFrame.mDuration);
            addFrame(bitmapDrawable, gifFrame.mDuration);
        }
        setOneShot(false);
        super.selectDrawable(0);
        return true;
    }

    private boolean internalLoad(Resources resources, ResettableInputStream resettableInputStream) {
        this.mResources = resources;
        DecodeGifImageHelper decodeGifImageHelper = this.mHelper;
        decodeGifImageHelper.mGifSource = resettableInputStream;
        return handleFirstDecodeResult(decodeGifImageHelper.decodeFrom(0));
    }

    private void preSelectDrawable(int i) {
        if (!this.mHelper.mFrames.isEmpty()) {
            GifFrame gifFrame = (GifFrame) this.mHelper.mFrames.get(0);
            if (this.mHelper.mFrames.size() > 1) {
                this.mHelper.mFrames.remove(0);
            }
            this.mHelper.decodeNextFrames();
            this.mDrawableContainerState.getChildren()[i] = new BitmapDrawable(this.mResources, gifFrame.mImage);
            this.mDurations.add(i, Integer.valueOf(gifFrame.mDuration));
        }
    }

    public final void addFrame(Drawable drawable, int i) {
        super.addFrame(drawable, i);
        this.mDurations.add(Integer.valueOf(i));
        this.mOriginalDurations.add(Integer.valueOf(i));
    }

    public final int getDuration(int i) {
        return ((Integer) this.mDurations.get(i)).intValue();
    }

    public boolean load(Context context, AssetManager assetManager, String str) {
        return internalLoad(context.getResources(), new ResettableInputStream(assetManager, str));
    }

    public boolean load(Context context, Uri uri) {
        return internalLoad(context.getResources(), new ResettableInputStream(context, uri));
    }

    public boolean load(Context context, String str) {
        return internalLoad(context.getResources(), new ResettableInputStream(str));
    }

    public final void scheduleSelf(Runnable runnable, long j) {
        if (j == SystemClock.uptimeMillis() + ((long) ((Integer) this.mOriginalDurations.get(this.mCurrentFrame)).intValue())) {
            j = SystemClock.uptimeMillis() + ((long) ((Integer) this.mDurations.get(this.mCurrentFrame)).intValue());
        }
        super.scheduleSelf(runnable, j);
    }

    public final boolean selectDrawable(int i) {
        preSelectDrawable(i);
        this.mCurrentFrame = i;
        return super.selectDrawable(i);
    }

    /* access modifiers changed from: protected */
    public final void setConstantState(DrawableContainerState drawableContainerState) {
        super.setConstantState(drawableContainerState);
        this.mDrawableContainerState = drawableContainerState;
    }

    public final void setMaxDecodeSize(long j) {
        this.mHelper.mMaxDecodeSize = j;
    }
}

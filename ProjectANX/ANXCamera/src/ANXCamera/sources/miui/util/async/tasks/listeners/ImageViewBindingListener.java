package miui.util.async.tasks.listeners;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import miui.util.AppConstants;
import miui.util.async.Task;
import miui.util.async.TaskManager;

public class ImageViewBindingListener extends BaseTaskListener {
    private static final LinkedHashSet ALL_LISTENERS = new LinkedHashSet();
    private WeakReference mCurrentTask;
    private Drawable mError;
    private final WeakReference mImageView;
    private Drawable mPlaceHolder;

    public ImageViewBindingListener(ImageView imageView) {
        this.mImageView = new WeakReference(imageView);
    }

    private boolean isSameImageView(ImageViewBindingListener imageViewBindingListener) {
        return this.mImageView.get() == imageViewBindingListener.mImageView.get();
    }

    public final ImageView getImageView() {
        return (ImageView) this.mImageView.get();
    }

    public void onException(TaskManager taskManager, Task task, Exception exc) {
        if (this.mError != null) {
            ImageView imageView = (ImageView) this.mImageView.get();
            if (imageView != null) {
                imageView.setImageDrawable(this.mError);
            }
        }
    }

    public void onFinalize(TaskManager taskManager, Task task) {
        ALL_LISTENERS.remove(this);
    }

    public void onPrepare(TaskManager taskManager, Task task) {
        WeakReference weakReference = this.mCurrentTask;
        if (weakReference != null) {
            Task task2 = (Task) weakReference.get();
            if (task2 != null) {
                task2.cancel();
            }
        }
        this.mCurrentTask = new WeakReference(task);
        Task task3 = null;
        Iterator it = ALL_LISTENERS.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ImageViewBindingListener imageViewBindingListener = (ImageViewBindingListener) it.next();
            if (imageViewBindingListener != this && isSameImageView(imageViewBindingListener)) {
                task3 = (Task) imageViewBindingListener.mCurrentTask.get();
                break;
            }
        }
        if (task3 != null) {
            task3.cancel();
        }
        ALL_LISTENERS.add(this);
        if (this.mPlaceHolder != null) {
            ImageView imageView = (ImageView) this.mImageView.get();
            if (imageView != null) {
                imageView.setImageDrawable(this.mPlaceHolder);
            }
        }
    }

    public Object onResult(TaskManager taskManager, Task task, Object obj) {
        ImageView imageView = (ImageView) this.mImageView.get();
        if (imageView != null) {
            Drawable[] drawableArr = {imageView.getDrawable(), null};
            if (drawableArr[0] != null) {
                Resources resources = AppConstants.getCurrentApplication().getResources();
                if (obj instanceof Bitmap) {
                    drawableArr[1] = new BitmapDrawable(resources, (Bitmap) obj);
                } else {
                    drawableArr[1] = (Drawable) obj;
                }
                TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
                transitionDrawable.setCrossFadeEnabled(true);
                imageView.setImageDrawable(transitionDrawable);
                transitionDrawable.startTransition(resources.getInteger(17694720));
            } else if (obj instanceof Bitmap) {
                imageView.setImageBitmap((Bitmap) obj);
            } else {
                imageView.setImageDrawable((Drawable) obj);
            }
        }
        return obj;
    }

    public ImageViewBindingListener setErrorDrawable(Drawable drawable) {
        this.mError = drawable;
        return this;
    }

    public ImageViewBindingListener setPlaceHolderDrawable(Drawable drawable) {
        this.mPlaceHolder = drawable;
        return this;
    }
}

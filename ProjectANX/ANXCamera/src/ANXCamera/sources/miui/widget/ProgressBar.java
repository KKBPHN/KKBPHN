package miui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.widget.RemoteViews.RemoteView;
import com.miui.internal.widget.ProgressBarDelegate;

@RemoteView
public class ProgressBar extends android.widget.ProgressBar {
    private ProgressBarDelegate mDelegate;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842871);
    }

    public ProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        getDelegate().postConstruct(attributeSet, i);
    }

    private synchronized ProgressBarDelegate getDelegate() {
        if (this.mDelegate == null) {
            this.mDelegate = ProgressBarDelegate.create(this, android.widget.ProgressBar.class);
        }
        return this.mDelegate;
    }

    public Drawable getProgressMaskDrawable() {
        return getDelegate().getProgressMaskDrawable();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        getDelegate().onSizeChanged(i, i2, i3, i4);
    }

    @RemotableViewMethod
    public synchronized void setIndeterminate(boolean z) {
        getDelegate().setIndeterminate(z);
    }

    @RemotableViewMethod
    public void setIndeterminateDrawable(Drawable drawable) {
        getDelegate().setIndeterminateDrawable(drawable);
    }

    public void setProgressDrawable(Drawable drawable) {
        getDelegate().setProgressDrawable(drawable);
    }

    public synchronized void setProgressMaskDrawable(Drawable drawable) {
        getDelegate().setProgressMaskDrawable(drawable);
    }
}

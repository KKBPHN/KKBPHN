package miui.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.miui.internal.R;
import miui.widget.AnimatedImageView;

public class AnimatedPreference extends CheckBoxPreference {
    private static final int STATE_ANIMATE_FINISH = 4;
    private static final int STATE_ANIMATE_INVISIBLE = 0;
    private static final int STATE_ANIMATE_VISIBLE = 1;
    private static final int STATE_ANIMATING = 2;
    private Drawable mAnimateDrawable;
    private Drawable mAnimateFinishDrawable;
    private int mAnimateState;

    public AnimatedPreference(Context context) {
        this(context, null);
    }

    public AnimatedPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAnimateState = 0;
        setWidgetLayoutResource(R.layout.preference_widget_animated);
    }

    public AnimatedPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAnimateState = 0;
        setWidgetLayoutResource(R.layout.preference_widget_animated);
    }

    private void updateAnimateState(int i, boolean z) {
        int i2;
        if (z) {
            i2 = i | this.mAnimateState;
        } else {
            i2 = (~i) & this.mAnimateState;
        }
        this.mAnimateState = i2;
        notifyChanged();
    }

    public boolean isAnimateFinish() {
        return (this.mAnimateState & 4) != 0;
    }

    public boolean isAnimateVisible() {
        return (this.mAnimateState & 1) != 0;
    }

    public boolean isAnimating() {
        return (this.mAnimateState & 2) != 0;
    }

    public void onBindView(View view) {
        super.onBindView(view);
        AnimatedImageView animatedImageView = (AnimatedImageView) view.findViewById(R.id.animate);
        ImageView imageView = (ImageView) view.findViewById(R.id.animate_finish);
        Drawable drawable = this.mAnimateDrawable;
        if (drawable != null) {
            animatedImageView.setImageDrawable(drawable);
        }
        Drawable drawable2 = this.mAnimateFinishDrawable;
        if (drawable2 != null) {
            imageView.setImageDrawable(drawable2);
        }
        int i = 0;
        animatedImageView.setVisibility(isAnimateVisible() ? 0 : 8);
        animatedImageView.setAnimating(isAnimating());
        if (!isAnimateFinish()) {
            i = 8;
        }
        imageView.setVisibility(i);
        ((ImageView) view.findViewById(16908294)).setAlpha(isEnabled() ? 1.0f : 0.3f);
    }

    public void setAnimateDrawable(int i) {
        setAnimateDrawable(getContext().getResources().getDrawable(i));
    }

    public void setAnimateDrawable(Drawable drawable) {
        this.mAnimateDrawable = drawable;
        notifyChanged();
    }

    public void setAnimateFinish(boolean z) {
        updateAnimateState(4, z);
    }

    public void setAnimateFinishDrawable(int i) {
        setAnimateFinishDrawable(getContext().getResources().getDrawable(i));
    }

    public void setAnimateFinishDrawable(Drawable drawable) {
        this.mAnimateFinishDrawable = drawable;
        notifyChanged();
    }

    public void setAnimateVisible(boolean z) {
        updateAnimateState(1, z);
    }

    public void setAnimating(boolean z) {
        updateAnimateState(3, z);
    }
}

package miui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.miui.internal.R;

public class DropDownImageView extends ImageView {
    private AnimatorSet mAnimCollapse;
    private AnimatorSet mAnimExpand;
    private Context mContext;

    public DropDownImageView(Context context) {
        this(context, null);
    }

    public DropDownImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DropDownImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, R.attr.dropdownImageViewStyle);
        this.mAnimExpand = null;
        this.mAnimCollapse = null;
        this.mContext = context;
        setupAnimations();
    }

    private void setupAnimations() {
        this.mAnimCollapse = new AnimatorSet();
        this.mAnimCollapse.setDuration(150);
        String str = "rotationX";
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, str, new float[]{0.0f, -90.0f});
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ImageView imageView = (ImageView) ((ObjectAnimator) animator).getTarget();
                imageView.setRotationX(-90.0f);
                imageView.setImageLevel(0);
                imageView.setRotationX(90.0f);
            }
        });
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, str, new float[]{90.0f, 0.0f});
        ofFloat2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ((ImageView) ((ObjectAnimator) animator).getTarget()).setRotationX(0.0f);
            }
        });
        this.mAnimCollapse.play(ofFloat).before(ofFloat2);
        this.mAnimCollapse.setDuration(150);
        this.mAnimExpand = new AnimatorSet();
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this, str, new float[]{0.0f, 90.0f});
        ofFloat3.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ImageView imageView = (ImageView) ((ObjectAnimator) animator).getTarget();
                imageView.setRotationX(90.0f);
                imageView.setImageLevel(1);
                imageView.setRotationX(-90.0f);
            }
        });
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this, str, new float[]{-90.0f, 0.0f});
        ofFloat4.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ((ImageView) ((ObjectAnimator) animator).getTarget()).setRotationX(0.0f);
            }
        });
        this.mAnimExpand.play(ofFloat3).before(ofFloat4);
    }

    public void collapse() {
        this.mAnimExpand.cancel();
        this.mAnimCollapse.start();
    }

    public void expand() {
        this.mAnimCollapse.cancel();
        this.mAnimExpand.start();
    }
}

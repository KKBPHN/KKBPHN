package miuix.preference;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import miuix.animation.Folme;
import miuix.animation.ITouchStyle.TouchType;
import miuix.animation.base.AnimConfig;

public class ConnectPreferenceHelper {
    public static final float BACKGROUND_ANIM_FACTOR = 1.5f;
    public static final int BACKGROUND_ANIM_TIME = 300;
    private static final int[] STATE_ATTR_CONNECTED;
    private static final int[] STATE_ATTR_DISCONNECTED;
    public static final int STATE_CONNECTED = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_DISCONNECTED = 0;
    public static final String TAG = "ConnectPreferenceHelper";
    private LayerDrawable BgDrawableParent;
    /* access modifiers changed from: private */
    public Drawable bgDrawableConnected;
    /* access modifiers changed from: private */
    public AnimatedVectorDrawable connectingAnimDrawable;
    private ColorStateList iconColorList;
    private Context mContext;
    private ValueAnimator mDisConnectedToConnectedBgAnim;
    private ValueAnimator mDisConnectedToConnectedIconAnim;
    private ValueAnimator mDisConnectedToConnectedSummaryAnim;
    private ValueAnimator mDisConnectedToConnectedTitleAnim;
    /* access modifiers changed from: private */
    public boolean mIconAnimEnabled = true;
    private int mLastState = 0;
    /* access modifiers changed from: private */
    public Preference mPreference;
    private int mState = 0;
    /* access modifiers changed from: private */
    public TextView mSummaryView;
    /* access modifiers changed from: private */
    public TextView mTitleView;
    private View mWidgetView;
    private ColorStateList summaryColorList;
    private ColorStateList titleColorList;

    static {
        int i = R.attr.state_connected;
        STATE_ATTR_CONNECTED = new int[]{i};
        STATE_ATTR_DISCONNECTED = new int[]{-i};
    }

    public ConnectPreferenceHelper(Context context, Preference preference) {
        this.mContext = context;
        this.mPreference = preference;
        this.titleColorList = ContextCompat.getColorStateList(context, R.color.miuix_preference_connect_title_color);
        this.summaryColorList = ContextCompat.getColorStateList(context, R.color.miuix_preference_connect_summary_color);
        this.iconColorList = ContextCompat.getColorStateList(context, R.color.miuix_preference_connect_icon_color);
        initAnim(context);
    }

    private void initAnim(Context context) {
        this.BgDrawableParent = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.miuix_preference_ic_bg_connect);
        LayerDrawable layerDrawable = this.BgDrawableParent;
        if (layerDrawable != null && VERSION.SDK_INT >= 21) {
            this.connectingAnimDrawable = (AnimatedVectorDrawable) layerDrawable.findDrawableByLayerId(R.id.anim_preference_connecting);
            this.bgDrawableConnected = this.BgDrawableParent.findDrawableByLayerId(R.id.shape_preference_connected);
            int colorForState = this.titleColorList.getColorForState(STATE_ATTR_DISCONNECTED, R.color.miuix_preference_connect_title_disconnected_color);
            int colorForState2 = this.titleColorList.getColorForState(STATE_ATTR_CONNECTED, R.color.miuix_preference_connect_title_connected_color);
            int colorForState3 = this.summaryColorList.getColorForState(STATE_ATTR_DISCONNECTED, R.color.miuix_preference_connect_summary_disconnected_color);
            int colorForState4 = this.summaryColorList.getColorForState(STATE_ATTR_CONNECTED, R.color.miuix_preference_connect_summary_connected_color);
            this.mDisConnectedToConnectedIconAnim = ValueAnimator.ofArgb(new int[]{this.iconColorList.getColorForState(STATE_ATTR_DISCONNECTED, R.color.miuix_preference_connect_icon_disconnected_color), this.iconColorList.getColorForState(STATE_ATTR_CONNECTED, R.color.miuix_preference_connect_icon_connected_color)});
            this.mDisConnectedToConnectedIconAnim.setDuration(300);
            this.mDisConnectedToConnectedIconAnim.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Drawable icon = ConnectPreferenceHelper.this.mPreference.getIcon();
                    if (icon != null && ConnectPreferenceHelper.this.mIconAnimEnabled) {
                        DrawableCompat.setTint(icon, ((Integer) valueAnimator.getAnimatedValue()).intValue());
                    }
                }
            });
            this.mDisConnectedToConnectedTitleAnim = ValueAnimator.ofArgb(new int[]{colorForState, colorForState2});
            this.mDisConnectedToConnectedTitleAnim.setDuration(300);
            this.mDisConnectedToConnectedTitleAnim.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (ConnectPreferenceHelper.this.mTitleView != null) {
                        ConnectPreferenceHelper.this.mTitleView.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                    }
                }
            });
            this.mDisConnectedToConnectedSummaryAnim = ValueAnimator.ofArgb(new int[]{colorForState3, colorForState4});
            this.mDisConnectedToConnectedSummaryAnim.setDuration(300);
            this.mDisConnectedToConnectedSummaryAnim.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (ConnectPreferenceHelper.this.mSummaryView != null) {
                        ConnectPreferenceHelper.this.mSummaryView.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                    }
                }
            });
            this.mDisConnectedToConnectedBgAnim = ValueAnimator.ofInt(new int[]{0, 255});
            this.mDisConnectedToConnectedBgAnim.setDuration(300);
            this.mDisConnectedToConnectedBgAnim.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ConnectPreferenceHelper.this.bgDrawableConnected.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            });
            this.mDisConnectedToConnectedBgAnim.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    if (ConnectPreferenceHelper.this.connectingAnimDrawable != null && ConnectPreferenceHelper.this.connectingAnimDrawable.isRunning()) {
                        ConnectPreferenceHelper.this.connectingAnimDrawable.stop();
                    }
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }
            });
        }
    }

    private static void setAlphaFolme(View view) {
        if (view != null) {
            Folme.useAt(view).touch().setAlpha(0.6f, TouchType.DOWN).handleTouchOf(view, new AnimConfig[0]);
        }
    }

    private void startConnectedToDisConnectedAnim() {
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator(1.5f);
        if (this.mDisConnectedToConnectedBgAnim.isRunning()) {
            this.mDisConnectedToConnectedBgAnim.cancel();
        }
        this.mDisConnectedToConnectedBgAnim.setInterpolator(accelerateInterpolator);
        this.mDisConnectedToConnectedBgAnim.reverse();
        if (this.mDisConnectedToConnectedTitleAnim.isRunning()) {
            this.mDisConnectedToConnectedTitleAnim.cancel();
        }
        this.mDisConnectedToConnectedTitleAnim.setInterpolator(accelerateInterpolator);
        this.mDisConnectedToConnectedTitleAnim.reverse();
        if (this.mDisConnectedToConnectedSummaryAnim.isRunning()) {
            this.mDisConnectedToConnectedSummaryAnim.cancel();
        }
        this.mDisConnectedToConnectedSummaryAnim.setInterpolator(accelerateInterpolator);
        this.mDisConnectedToConnectedSummaryAnim.reverse();
        if (this.mDisConnectedToConnectedIconAnim.isRunning()) {
            this.mDisConnectedToConnectedIconAnim.cancel();
        }
        this.mDisConnectedToConnectedIconAnim.setInterpolator(accelerateInterpolator);
        this.mDisConnectedToConnectedIconAnim.reverse();
    }

    private void startDisConnectedToConnectedAnim() {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(1.5f);
        if (this.mDisConnectedToConnectedBgAnim.isRunning()) {
            this.mDisConnectedToConnectedBgAnim.cancel();
        }
        this.mDisConnectedToConnectedBgAnim.setInterpolator(decelerateInterpolator);
        this.mDisConnectedToConnectedBgAnim.start();
        if (this.mDisConnectedToConnectedTitleAnim.isRunning()) {
            this.mDisConnectedToConnectedTitleAnim.cancel();
        }
        this.mDisConnectedToConnectedTitleAnim.setInterpolator(decelerateInterpolator);
        this.mDisConnectedToConnectedTitleAnim.start();
        if (this.mDisConnectedToConnectedSummaryAnim.isRunning()) {
            this.mDisConnectedToConnectedSummaryAnim.cancel();
        }
        this.mDisConnectedToConnectedSummaryAnim.setInterpolator(decelerateInterpolator);
        this.mDisConnectedToConnectedSummaryAnim.start();
        if (this.mDisConnectedToConnectedIconAnim.isRunning()) {
            this.mDisConnectedToConnectedIconAnim.cancel();
        }
        this.mDisConnectedToConnectedIconAnim.setInterpolator(decelerateInterpolator);
        this.mDisConnectedToConnectedIconAnim.start();
    }

    private void updateState(boolean z) {
        int i = this.mState;
        if (i == 0) {
            updateStateDisconnected(z);
        } else if (i == 1) {
            updateStateConnected(z);
        } else if (i == 2) {
            updateStateConnecting(z);
        }
    }

    private void updateStateConnected(boolean z) {
        if (z) {
            startDisConnectedToConnectedAnim();
        } else {
            this.bgDrawableConnected.setAlpha(255);
            updateViewColorList(STATE_ATTR_CONNECTED);
        }
        updateWidgetDrawable(STATE_ATTR_CONNECTED);
    }

    private void updateStateConnecting(boolean z) {
        this.bgDrawableConnected.setAlpha(0);
        AnimatedVectorDrawable animatedVectorDrawable = this.connectingAnimDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.setAlpha(255);
            if (!this.connectingAnimDrawable.isRunning()) {
                this.connectingAnimDrawable.start();
            }
        }
        if (!z) {
            updateViewColorList(STATE_ATTR_DISCONNECTED);
        }
        updateWidgetDrawable(STATE_ATTR_DISCONNECTED);
    }

    private void updateStateDisconnected(boolean z) {
        if (z) {
            int i = this.mLastState;
            if (i == 1) {
                startConnectedToDisConnectedAnim();
            } else if (i == 2) {
                AnimatedVectorDrawable animatedVectorDrawable = this.connectingAnimDrawable;
                if (animatedVectorDrawable != null && animatedVectorDrawable.isRunning()) {
                    this.connectingAnimDrawable.stop();
                }
            }
        } else {
            this.bgDrawableConnected.setAlpha(0);
            updateViewColorList(STATE_ATTR_DISCONNECTED);
        }
        AnimatedVectorDrawable animatedVectorDrawable2 = this.connectingAnimDrawable;
        if (animatedVectorDrawable2 != null) {
            animatedVectorDrawable2.setAlpha(0);
        }
        updateWidgetDrawable(STATE_ATTR_DISCONNECTED);
    }

    private void updateViewColorList(int[] iArr) {
        Drawable icon = this.mPreference.getIcon();
        if (icon != null && this.mIconAnimEnabled) {
            DrawableCompat.setTint(icon, this.iconColorList.getColorForState(iArr, R.color.miuix_preference_connect_icon_disconnected_color));
        }
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setTextColor(this.titleColorList.getColorForState(iArr, R.color.miuix_preference_connect_title_disconnected_color));
        }
        TextView textView2 = this.mSummaryView;
        if (textView2 != null) {
            textView2.setTextColor(this.summaryColorList.getColorForState(iArr, R.color.miuix_preference_connect_summary_disconnected_color));
        }
    }

    private void updateWidgetDrawable(int[] iArr) {
        int i;
        Context context;
        ImageView imageView;
        View view = this.mWidgetView;
        if (view instanceof ImageView) {
            if (iArr == STATE_ATTR_CONNECTED) {
                imageView = (ImageView) view;
                context = this.mContext;
                i = R.drawable.miuix_preference_ic_detail_connected;
            } else {
                TypedValue typedValue = new TypedValue();
                this.mContext.getTheme().resolveAttribute(R.attr.connectDetailDisconnectedDrawable, typedValue, true);
                imageView = (ImageView) this.mWidgetView;
                context = this.mContext;
                i = typedValue.resourceId;
            }
            imageView.setImageDrawable(ContextCompat.getDrawable(context, i));
        }
    }

    public int getConnectState() {
        return this.mState;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder, View view) {
        if (view != null && preferenceViewHolder != null) {
            view.setBackground(this.BgDrawableParent);
            preferenceViewHolder.itemView.setBackground(null);
            this.mTitleView = (TextView) preferenceViewHolder.findViewById(16908310);
            this.mSummaryView = (TextView) preferenceViewHolder.findViewById(16908304);
            this.mWidgetView = preferenceViewHolder.findViewById(R.id.preference_detail);
            setAlphaFolme(this.mWidgetView);
            updateState(false);
        }
    }

    public void setConnectState(int i) {
        this.mLastState = this.mState;
        this.mState = i;
        updateState(true);
    }

    public void setIconAnimEnabled(boolean z) {
        this.mIconAnimEnabled = z;
    }
}

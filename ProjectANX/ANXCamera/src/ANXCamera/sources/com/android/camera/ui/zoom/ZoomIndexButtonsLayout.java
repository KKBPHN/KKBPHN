package com.android.camera.ui.zoom;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.core.view.ViewCompat;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import java.util.ArrayList;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class ZoomIndexButtonsLayout extends ViewGroup implements OnClickListener {
    private static final String TAG = "ZoomIndexButtonsLayout";
    private int mBackGroundColor;
    private int mChildSize;
    private int mDegree;
    private boolean mIsRLT;
    private OnIndexButtonClickListener mOnItemClickListener;
    private float mPadding;
    private int mSelectIndex;
    private List mZoomIndexes;

    public interface OnIndexButtonClickListener {
        void onIndexButtonClick(View view);
    }

    public ZoomIndexButtonsLayout(Context context) {
        this(context, null, -1, -1);
    }

    public ZoomIndexButtonsLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1, -1);
    }

    public ZoomIndexButtonsLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, -1);
    }

    public ZoomIndexButtonsLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mSelectIndex = -1;
        this.mZoomIndexes = new ArrayList();
        this.mIsRLT = Util.isLayoutRTL(context);
        this.mChildSize = context.getResources().getDimensionPixelSize(R.dimen.manually_indicator_layout_height) - (context.getResources().getDimensionPixelSize(R.dimen.manually_indicator_background_margin_top_bottom) * 2);
    }

    private void toScaleAnimator(ZoomTextImageView zoomTextImageView, boolean z) {
        zoomTextImageView.clearAnimation();
        zoomTextImageView.toToggle(true, z);
        if (z) {
            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.zoom_index_button_zoom_in_out);
            animatorSet.setTarget(zoomTextImageView);
            animatorSet.setInterpolator(new CubicEaseOutInterpolator());
            animatorSet.start();
        }
    }

    public void onClick(View view) {
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null || !cameraAction.isDoingAction()) {
            int i = this.mSelectIndex;
            if (i != -1) {
                ((ZoomTextImageView) getChildAt(i)).toToggle(false, false);
            }
            ((ZoomTextImageView) view).toToggle(true, false);
            this.mSelectIndex = indexOfChild(view);
            this.mOnItemClickListener.onIndexButtonClick(view);
            return;
        }
        Log.d(TAG, "ignore click due to doing action");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        this.mPadding = (float) getResources().getDimensionPixelSize(childCount <= 4 ? R.dimen.zoom_index_buttons_padding_left_for_little : R.dimen.zoom_index_buttons_padding_left);
        int round = Math.round(((((((float) getWidth()) - (this.mPadding * 2.0f)) - ((float) this.mChildSize)) / ((float) (getChildCount() - 1))) - ((float) this.mChildSize)) / 2.0f);
        int height = getHeight();
        int i5 = this.mChildSize;
        float f = (float) ((height - i5) / 2);
        float f2 = ((float) i5) + (2.0f * f);
        float f3 = this.mPadding - ((float) round);
        int i6 = 0;
        while (i6 < childCount) {
            View childAt = getChildAt(this.mIsRLT ? (getChildCount() - 1) - i6 : i6);
            float f4 = (float) (this.mChildSize + (round * 2));
            childAt.setLayoutParams(new LayoutParams(Math.round(f4), this.mChildSize));
            int i7 = (int) f;
            childAt.setPadding(round, i7, round, i7);
            int i8 = (int) f3;
            f3 += f4;
            childAt.layout(i8, 0, (int) f3, (int) f2);
            i6++;
        }
    }

    public void provideRotateItem(List list, int i) {
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            list.add(getChildAt(i2));
        }
        this.mDegree = i;
    }

    public void setBackgroundColor(int i) {
        this.mBackGroundColor = i;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            ((ZoomTextImageView) getChildAt(i2)).setNormalBackgroundColor(i);
        }
    }

    public void setDegree(int i) {
        this.mDegree = i;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            ViewCompat.setRotation(getChildAt(i2), (float) i);
        }
    }

    public void setSelect(float f, boolean z) {
        int indexOf = this.mZoomIndexes.indexOf(Float.valueOf(HybridZoomingSystem.toDecimal(f)));
        if (!z || this.mSelectIndex != indexOf) {
            if (indexOf != -1) {
                int i = this.mSelectIndex;
                if (i != -1) {
                    ((ZoomTextImageView) getChildAt(i)).toToggle(false, z);
                }
                toScaleAnimator((ZoomTextImageView) getChildAt(indexOf), z);
                this.mSelectIndex = indexOf;
            } else {
                int i2 = this.mSelectIndex;
                if (i2 != -1) {
                    ((ZoomTextImageView) getChildAt(i2)).toToggle(false, z);
                }
                this.mSelectIndex = -1;
            }
        }
    }

    public void setZoomIndexButtons(List list, OnIndexButtonClickListener onIndexButtonClickListener) {
        this.mOnItemClickListener = onIndexButtonClickListener;
        List list2 = this.mZoomIndexes;
        if (list2 == null || list2.size() <= 0 || !this.mZoomIndexes.containsAll(list) || !list.containsAll(this.mZoomIndexes)) {
            removeAllViews();
            this.mZoomIndexes = list;
            this.mSelectIndex = -1;
            for (int i = 0; i < list.size(); i++) {
                ZoomTextImageView zoomTextImageView = new ZoomTextImageView(getContext());
                zoomTextImageView.setVisibility(0);
                float floatValue = ((Float) list.get(i)).floatValue();
                String valueOf = ((floatValue * 10.0f) % 10.0f == 0.0f || floatValue >= 100.0f) ? String.valueOf((int) floatValue) : String.valueOf(floatValue);
                zoomTextImageView.setTag(Float.valueOf(floatValue));
                zoomTextImageView.setText(3, valueOf);
                int i2 = this.mChildSize;
                addView(zoomTextImageView, new LayoutParams(i2, i2));
                zoomTextImageView.setRotation((float) this.mDegree);
                zoomTextImageView.setNormalBackgroundColor(this.mBackGroundColor);
                zoomTextImageView.setContentDescription(zoomTextImageView.getContext().getResources().getString(R.string.accessibility_focus_status, new Object[]{valueOf}));
                zoomTextImageView.setOnClickListener(this);
                FolmeUtils.touchItemScale(zoomTextImageView);
            }
        }
    }
}

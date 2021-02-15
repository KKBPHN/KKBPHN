package com.android.camera.fragment.beauty;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import java.util.List;

public class EyeLightSingleCheckAdapter extends Adapter {
    /* access modifiers changed from: private */
    public int mBackgroundColorNormal;
    /* access modifiers changed from: private */
    public int mBackgroundColorPressed;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mItemHorizontalMargin = 0;
    /* access modifiers changed from: private */
    public int mPreSelectedItem = 0;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public int mSelectedItem = 0;
    private List mSingleCheckList;
    private OnItemClickListener onItemClickListener;

    public class EyeLightItem {
        private int mImageResource;
        private int mTextResource;
        private String mType;

        public EyeLightItem(String str, int i, int i2) {
            this.mImageResource = i;
            this.mTextResource = i2;
            this.mType = str;
        }

        public int getImageResource() {
            return this.mImageResource;
        }

        public int getTextResource() {
            return this.mTextResource;
        }

        public String getType() {
            return this.mType;
        }
    }

    class SingleCheckViewHolder extends ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public View itemView;
        private EyeLightSingleCheckAdapter mAdapter;
        private ImageView mBase;

        public SingleCheckViewHolder(View view, EyeLightSingleCheckAdapter eyeLightSingleCheckAdapter) {
            super(view);
            this.mAdapter = eyeLightSingleCheckAdapter;
            this.itemView = view;
            this.mBase = (ImageView) view.findViewById(R.id.eye_light_item_icon);
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mBase.getLayoutParams();
            marginLayoutParams.setMarginStart(EyeLightSingleCheckAdapter.this.mItemHorizontalMargin);
            marginLayoutParams.setMarginEnd(EyeLightSingleCheckAdapter.this.mItemHorizontalMargin);
            view.setOnClickListener(this);
        }

        private void colorAnimate(final ImageView imageView, int i, int i2) {
            ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            ofObject.setDuration(200);
            ofObject.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    imageView.setBackgroundTintList(ColorStateList.valueOf(((Integer) valueAnimator.getAnimatedValue()).intValue()));
                }
            });
            ofObject.start();
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != EyeLightSingleCheckAdapter.this.mSelectedItem) {
                EyeLightSingleCheckAdapter eyeLightSingleCheckAdapter = EyeLightSingleCheckAdapter.this;
                eyeLightSingleCheckAdapter.mPreSelectedItem = eyeLightSingleCheckAdapter.mSelectedItem;
                EyeLightSingleCheckAdapter.this.mSelectedItem = adapterPosition;
                SingleCheckViewHolder singleCheckViewHolder = (SingleCheckViewHolder) EyeLightSingleCheckAdapter.this.mRecyclerView.findViewHolderForAdapterPosition(EyeLightSingleCheckAdapter.this.mPreSelectedItem);
                SingleCheckViewHolder singleCheckViewHolder2 = (SingleCheckViewHolder) EyeLightSingleCheckAdapter.this.mRecyclerView.findViewHolderForAdapterPosition(EyeLightSingleCheckAdapter.this.mSelectedItem);
                EyeLightSingleCheckAdapter.this.mContext.getResources();
                if (singleCheckViewHolder != null) {
                    colorAnimate(singleCheckViewHolder.mBase, EyeLightSingleCheckAdapter.this.mBackgroundColorPressed, EyeLightSingleCheckAdapter.this.mBackgroundColorNormal);
                }
                colorAnimate(singleCheckViewHolder2.mBase, EyeLightSingleCheckAdapter.this.mBackgroundColorNormal, EyeLightSingleCheckAdapter.this.mBackgroundColorPressed);
                if (singleCheckViewHolder == null) {
                    this.mAdapter.notifyItemChanged(EyeLightSingleCheckAdapter.this.mPreSelectedItem);
                }
                this.mAdapter.onItemHolderClick(this);
            }
        }

        public void setDataToView(EyeLightItem eyeLightItem, int i) {
            this.itemView.setTag(eyeLightItem);
            EyeLightSingleCheckAdapter.this.setAccessible(this.itemView, eyeLightItem.getTextResource(), i == EyeLightSingleCheckAdapter.this.mSelectedItem);
            this.mBase.setImageResource(eyeLightItem.getImageResource());
            ImageView imageView = this.mBase;
            int access$200 = EyeLightSingleCheckAdapter.this.mSelectedItem;
            EyeLightSingleCheckAdapter eyeLightSingleCheckAdapter = EyeLightSingleCheckAdapter.this;
            imageView.setBackgroundTintList(ColorStateList.valueOf(i == access$200 ? eyeLightSingleCheckAdapter.mBackgroundColorPressed : eyeLightSingleCheckAdapter.mBackgroundColorNormal));
        }
    }

    public EyeLightSingleCheckAdapter(Context context, List list, int i) {
        int i2;
        Resources resources;
        this.mContext = context;
        this.mSingleCheckList = list;
        this.mItemHorizontalMargin = i;
        this.mBackgroundColorPressed = this.mContext.getResources().getColor(R.color.beautycamera_eye_light_item_background_pressed);
        int uiStyle = DataRepository.dataItemRunning().getUiStyle();
        if (uiStyle == 1 || uiStyle == 3) {
            resources = this.mContext.getResources();
            i2 = R.color.beautycamera_eye_light_item_background_normal_full_screen;
        } else {
            resources = this.mContext.getResources();
            i2 = R.color.beautycamera_eye_light_item_background_normal_half_screen;
        }
        this.mBackgroundColorNormal = resources.getColor(i2);
    }

    public int getItemCount() {
        return this.mSingleCheckList.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    public void onBindViewHolder(SingleCheckViewHolder singleCheckViewHolder, int i) {
        try {
            singleCheckViewHolder.setDataToView((EyeLightItem) this.mSingleCheckList.get(i), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SingleCheckViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SingleCheckViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.eye_light_item, viewGroup, false), this);
    }

    public void onItemHolderClick(SingleCheckViewHolder singleCheckViewHolder) {
        OnItemClickListener onItemClickListener2 = this.onItemClickListener;
        if (onItemClickListener2 != null) {
            onItemClickListener2.onItemClick(null, singleCheckViewHolder.itemView, singleCheckViewHolder.getAdapterPosition(), singleCheckViewHolder.getItemId());
        }
    }

    public void setAccessible(View view, int i, boolean z) {
        if (view != null) {
            if (z) {
                StringBuilder sb = new StringBuilder();
                sb.append(view.getContext().getString(i));
                sb.append(", ");
                sb.append(view.getContext().getString(R.string.accessibility_selected));
                view.setContentDescription(sb.toString());
                if (Util.isAccessible()) {
                    view.postDelayed(new O000000o(view), 100);
                }
            } else {
                view.setContentDescription(view.getContext().getString(i));
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public void setSelectedPosition(int i) {
        this.mSelectedItem = i;
    }
}

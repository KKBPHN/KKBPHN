package com.android.camera.fragment.beauty;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.ui.ColorImageView;
import java.util.List;

public class SingleCheckAdapter extends Adapter {
    /* access modifiers changed from: private */
    public ArgbEvaluator mArgbEvaluator;
    /* access modifiers changed from: private */
    public Context mContext;
    private int mDegree;
    /* access modifiers changed from: private */
    public boolean mEnableClick = true;
    private FloatEvaluator mFloatEvaluator;
    /* access modifiers changed from: private */
    public int mItemHorizontalMargin = 0;
    private OnItemClickListener mOnItemClickListener;
    /* access modifiers changed from: private */
    public int mPreSelectedItem = 0;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public int mSelectedItem = 0;
    private List mSingleCheckList;

    public class LevelItem {
        /* access modifiers changed from: private */
        public String mText;
        private int mTextResource;

        public LevelItem(int i) {
            this.mTextResource = i;
        }

        public LevelItem(String str) {
            this.mText = str;
        }

        public String getText() {
            return this.mText;
        }

        public int getTextResource() {
            return this.mTextResource;
        }
    }

    class SingleCheckViewHolder extends ViewHolder implements OnClickListener {
        private SingleCheckAdapter mAdapter;
        private ColorImageView mBase;
        private ImageView mImageView;
        private TextView mText;

        public SingleCheckViewHolder(View view, SingleCheckAdapter singleCheckAdapter) {
            super(view);
            this.mAdapter = singleCheckAdapter;
            this.mText = (TextView) view.findViewById(R.id.second_text);
            this.mImageView = (ImageView) view.findViewById(R.id.second_image);
            this.mBase = (ColorImageView) view.findViewById(R.id.second_base);
            this.mBase.setIsNeedTransparent(false, true);
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mBase.getLayoutParams();
            marginLayoutParams.setMarginStart(SingleCheckAdapter.this.mItemHorizontalMargin);
            marginLayoutParams.setMarginEnd(SingleCheckAdapter.this.mItemHorizontalMargin);
            view.setOnClickListener(this);
            FolmeUtils.handleListItemTouch(view);
        }

        private void animateIn(final ColorImageView colorImageView, final TextView textView, final ImageView imageView) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.setDuration(100);
            ofFloat.setInterpolator(new LinearInterpolator() {
                public float getInterpolation(float f) {
                    float interpolation = super.getInterpolation(f);
                    colorImageView.setColorAndRefresh(((Integer) SingleCheckAdapter.this.mArgbEvaluator.evaluate(interpolation, Integer.valueOf(SingleCheckAdapter.this.baseNormalColor()), Integer.valueOf(TintColor.tintColor()))).intValue());
                    textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    ((ColorImageView) imageView).setColorAndRefresh(ViewCompat.MEASURED_STATE_MASK);
                    return interpolation;
                }
            });
            ofFloat.start();
        }

        private void animateOut(final ColorImageView colorImageView, final TextView textView, final ImageView imageView) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.setDuration(100);
            ofFloat.setInterpolator(new LinearInterpolator() {
                public float getInterpolation(float f) {
                    float interpolation = super.getInterpolation(f);
                    colorImageView.setColorAndRefresh(((Integer) SingleCheckAdapter.this.mArgbEvaluator.evaluate(interpolation, Integer.valueOf(TintColor.tintColor()), Integer.valueOf(SingleCheckAdapter.this.baseNormalColor()))).intValue());
                    textView.setTextColor(-1);
                    ((ColorImageView) imageView).setColorAndRefresh(-1);
                    return interpolation;
                }
            });
            ofFloat.start();
        }

        private void colorAnimate(final ColorImageView colorImageView, int i, int i2) {
            ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            ofObject.setDuration(200);
            ofObject.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    colorImageView.setColorAndRefresh(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            });
            ofObject.start();
        }

        private void setAccessible(View view, int i, boolean z) {
            StringBuilder sb = new StringBuilder(SingleCheckAdapter.this.mContext.getString(R.string.beauty_tab_name_live_beauty));
            sb.append(i == 0 ? SingleCheckAdapter.this.mContext.getString(R.string.face_beauty_close) : Integer.valueOf(i));
            if (z) {
                sb.append(", ");
                sb.append(SingleCheckAdapter.this.mContext.getString(R.string.accessibility_selected));
            }
            view.setContentDescription(sb.toString());
            if (z && Util.isAccessible()) {
                view.postDelayed(new O00000o(view), 100);
            }
        }

        public void onClick(View view) {
            if (SingleCheckAdapter.this.mEnableClick) {
                CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                if (cameraAction != null && !cameraAction.isDoingAction() && !cameraAction.isRecording()) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != SingleCheckAdapter.this.mSelectedItem) {
                        SingleCheckAdapter singleCheckAdapter = SingleCheckAdapter.this;
                        singleCheckAdapter.mPreSelectedItem = singleCheckAdapter.mSelectedItem;
                        SingleCheckAdapter.this.mSelectedItem = adapterPosition;
                        SingleCheckViewHolder singleCheckViewHolder = (SingleCheckViewHolder) SingleCheckAdapter.this.mRecyclerView.findViewHolderForAdapterPosition(SingleCheckAdapter.this.mPreSelectedItem);
                        SingleCheckViewHolder singleCheckViewHolder2 = (SingleCheckViewHolder) SingleCheckAdapter.this.mRecyclerView.findViewHolderForAdapterPosition(SingleCheckAdapter.this.mSelectedItem);
                        animateOut(singleCheckViewHolder.mBase, singleCheckViewHolder.mText, singleCheckViewHolder.mImageView);
                        animateIn(singleCheckViewHolder2.mBase, singleCheckViewHolder2.mText, singleCheckViewHolder2.mImageView);
                        setAccessible(singleCheckViewHolder.itemView, SingleCheckAdapter.this.mPreSelectedItem, false);
                        setAccessible(singleCheckViewHolder2.itemView, SingleCheckAdapter.this.mSelectedItem, true);
                        this.mAdapter.onItemHolderClick(this);
                    }
                }
            }
        }

        public void setDateToView(SingleCheckViewHolder singleCheckViewHolder, LevelItem levelItem, int i) {
            boolean z = false;
            if (TextUtils.isEmpty(levelItem.mText)) {
                this.mText.setVisibility(8);
                this.mImageView.setVisibility(0);
                this.mImageView.setImageResource(levelItem.getTextResource());
            } else {
                this.mText.setVisibility(0);
                this.mImageView.setVisibility(8);
                this.mText.setTextSize(0, (float) SingleCheckAdapter.this.mContext.getResources().getDimensionPixelSize(R.dimen.beauty_level_text_size));
                this.mText.setText(levelItem.getText());
            }
            this.mText.setAlpha(1.0f);
            TextView textView = this.mText;
            int access$300 = SingleCheckAdapter.this.mSelectedItem;
            int i2 = ViewCompat.MEASURED_STATE_MASK;
            textView.setTextColor(i == access$300 ? -16777216 : -1);
            this.mBase.setColor(i == SingleCheckAdapter.this.mSelectedItem ? TintColor.tintColor() : SingleCheckAdapter.this.baseNormalColor());
            ColorImageView colorImageView = (ColorImageView) this.mImageView;
            if (i != SingleCheckAdapter.this.mSelectedItem) {
                i2 = -1;
            }
            colorImageView.setColor(i2);
            View view = singleCheckViewHolder.itemView;
            if (i == SingleCheckAdapter.this.mSelectedItem) {
                z = true;
            }
            setAccessible(view, i, z);
        }
    }

    public SingleCheckAdapter(Context context, List list, int i) {
        this.mSingleCheckList = list;
        this.mItemHorizontalMargin = i;
        this.mArgbEvaluator = new ArgbEvaluator();
        this.mFloatEvaluator = new FloatEvaluator();
        this.mContext = context;
    }

    public int baseNormalColor() {
        float f;
        float f2;
        if (DataRepository.dataItemGlobal().getCurrentMode() == 165) {
            f = 0.15f;
            f2 = 1.0f;
        } else {
            f = 0.3f;
            f2 = 0.0f;
        }
        return Color.argb(f, f2, f2, f2);
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
            singleCheckViewHolder.setDateToView(singleCheckViewHolder, (LevelItem) this.mSingleCheckList.get(i), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewCompat.setRotation(singleCheckViewHolder.itemView, (float) this.mDegree);
    }

    public SingleCheckViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SingleCheckViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beauty_level_item, viewGroup, false), this);
    }

    public void onItemHolderClick(SingleCheckViewHolder singleCheckViewHolder) {
        OnItemClickListener onItemClickListener = this.mOnItemClickListener;
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, singleCheckViewHolder.itemView, singleCheckViewHolder.getAdapterPosition(), singleCheckViewHolder.getItemId());
        }
    }

    public void setEnableClick(boolean z) {
        this.mEnableClick = z;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setRotation(int i) {
        this.mDegree = i;
    }

    public void setSelectedPosition(int i) {
        this.mSelectedItem = i;
    }
}

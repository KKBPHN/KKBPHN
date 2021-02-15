package com.android.camera.fragment.beauty;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.TypeItem;
import com.android.camera.log.Log;
import com.android.camera.ui.ColorImageView;
import java.util.List;

public class MakeupSingleCheckAdapter extends Adapter {
    private static final String TAG = "MakeupSingleCheckAdapter";
    /* access modifiers changed from: private */
    public Context mContext;
    private int mDegree;
    private int mPositionFirstItem;
    private int mPositionLastItem;
    /* access modifiers changed from: private */
    public int mPreSelectedItem = 0;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public int mSelectedItem = 0;
    /* access modifiers changed from: private */
    public List mSingleCheckList;
    private OnItemClickListener onItemClickListener;

    class SingleCheckViewHolder extends ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public View itemView;
        private MakeupSingleCheckAdapter mAdapter;
        private ColorImageView mBase;
        private TextView mText;

        public SingleCheckViewHolder(View view, MakeupSingleCheckAdapter makeupSingleCheckAdapter) {
            super(view);
            this.mAdapter = makeupSingleCheckAdapter;
            this.itemView = view;
            this.mText = (TextView) view.findViewById(R.id.makeup_item_name);
            this.mBase = (ColorImageView) view.findViewById(R.id.makeup_item_icon);
            view.setOnClickListener(this);
            FolmeUtils.handleListItemTouch(view);
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

        private void textColorAnimate(final TextView textView, int i, int i2) {
            ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            ofObject.setDuration(200);
            ofObject.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    textView.setTextColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            });
            ofObject.start();
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != MakeupSingleCheckAdapter.this.mSelectedItem) {
                MakeupSingleCheckAdapter makeupSingleCheckAdapter = MakeupSingleCheckAdapter.this;
                makeupSingleCheckAdapter.mPreSelectedItem = makeupSingleCheckAdapter.mSelectedItem;
                MakeupSingleCheckAdapter.this.mSelectedItem = adapterPosition;
                SingleCheckViewHolder singleCheckViewHolder = (SingleCheckViewHolder) MakeupSingleCheckAdapter.this.mRecyclerView.findViewHolderForAdapterPosition(MakeupSingleCheckAdapter.this.mPreSelectedItem);
                SingleCheckViewHolder singleCheckViewHolder2 = (SingleCheckViewHolder) MakeupSingleCheckAdapter.this.mRecyclerView.findViewHolderForAdapterPosition(MakeupSingleCheckAdapter.this.mSelectedItem);
                if (singleCheckViewHolder != null) {
                    MakeupSingleCheckAdapter makeupSingleCheckAdapter2 = MakeupSingleCheckAdapter.this;
                    if (makeupSingleCheckAdapter2.normalItem(makeupSingleCheckAdapter2.mPreSelectedItem)) {
                        MakeupSingleCheckAdapter makeupSingleCheckAdapter3 = MakeupSingleCheckAdapter.this;
                        if (makeupSingleCheckAdapter3.normalItem(makeupSingleCheckAdapter3.mSelectedItem)) {
                            colorAnimate(singleCheckViewHolder.mBase, TintColor.tintColor(), -1);
                            textColorAnimate(singleCheckViewHolder.mText, TintColor.tintColor(), MakeupSingleCheckAdapter.this.mContext.getColor(R.color.beautycamera_beauty_advanced_item_text_normal));
                            singleCheckViewHolder.mText.setContentDescription(MakeupSingleCheckAdapter.this.mContext.getString(((TypeItem) MakeupSingleCheckAdapter.this.mSingleCheckList.get(MakeupSingleCheckAdapter.this.mPreSelectedItem)).getTextResource()));
                        }
                    }
                }
                if (singleCheckViewHolder2 != null) {
                    MakeupSingleCheckAdapter makeupSingleCheckAdapter4 = MakeupSingleCheckAdapter.this;
                    if (makeupSingleCheckAdapter4.normalItem(makeupSingleCheckAdapter4.mPreSelectedItem)) {
                        MakeupSingleCheckAdapter makeupSingleCheckAdapter5 = MakeupSingleCheckAdapter.this;
                        if (makeupSingleCheckAdapter5.normalItem(makeupSingleCheckAdapter5.mSelectedItem)) {
                            colorAnimate(singleCheckViewHolder2.mBase, -1, TintColor.tintColor());
                            textColorAnimate(singleCheckViewHolder2.mText, MakeupSingleCheckAdapter.this.mContext.getColor(R.color.beautycamera_beauty_advanced_item_text_normal), TintColor.tintColor());
                            String string = MakeupSingleCheckAdapter.this.mContext.getString(((TypeItem) MakeupSingleCheckAdapter.this.mSingleCheckList.get(MakeupSingleCheckAdapter.this.mSelectedItem)).getTextResource());
                            TextView textView = singleCheckViewHolder2.mText;
                            StringBuilder sb = new StringBuilder();
                            sb.append(string);
                            sb.append(MakeupSingleCheckAdapter.this.mContext.getString(R.string.accessibility_selected));
                            textView.setContentDescription(sb.toString());
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onClick position=");
                            sb2.append(MakeupSingleCheckAdapter.this.mSelectedItem);
                            sb2.append(", name=");
                            sb2.append(string);
                            Log.u(MakeupSingleCheckAdapter.TAG, sb2.toString());
                        }
                    }
                }
                if (singleCheckViewHolder == null) {
                    this.mAdapter.notifyItemChanged(MakeupSingleCheckAdapter.this.mPreSelectedItem);
                }
            }
            this.mAdapter.onItemHolderClick(this);
        }

        public void setDataToView(TypeItem typeItem, int i) {
            int i2;
            View view;
            this.itemView.setTag(typeItem);
            if (DataRepository.dataItemGlobal().getCurrentMode() == 165) {
                view = this.itemView;
                i2 = R.drawable.makeup_item_bg_square;
            } else {
                view = this.itemView;
                i2 = R.drawable.makeup_item_bg;
            }
            view.setBackgroundResource(i2);
            this.mText.setText(typeItem.getTextResource());
            TextView textView = this.mText;
            int color = (i != MakeupSingleCheckAdapter.this.mSelectedItem || !MakeupSingleCheckAdapter.this.normalItem(i)) ? MakeupSingleCheckAdapter.this.mContext.getColor(R.color.beautycamera_beauty_advanced_item_text_normal) : TintColor.tintColor();
            textView.setTextColor(color);
            MakeupSingleCheckAdapter.this.setAccessible(this.itemView, typeItem.getTextResource(), i == MakeupSingleCheckAdapter.this.mSelectedItem);
            this.mBase.setImageResource(typeItem.getImageResource());
            ColorImageView colorImageView = this.mBase;
            int tintColor = (i != MakeupSingleCheckAdapter.this.mSelectedItem || !MakeupSingleCheckAdapter.this.normalItem(i)) ? -1 : TintColor.tintColor();
            colorImageView.setColor(tintColor);
        }
    }

    public MakeupSingleCheckAdapter(Context context, List list) {
        this.mContext = context;
        this.mSingleCheckList = list;
        this.mPositionFirstItem = 0;
        this.mPositionLastItem = list.size() - 1;
    }

    public MakeupSingleCheckAdapter(Context context, List list, int i, int i2) {
        this.mContext = context;
        this.mSingleCheckList = list;
        this.mPositionFirstItem = i;
        this.mPositionLastItem = i2;
    }

    /* access modifiers changed from: private */
    public boolean normalItem(int i) {
        return i >= this.mPositionFirstItem && i <= this.mPositionLastItem;
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
            singleCheckViewHolder.setDataToView((TypeItem) this.mSingleCheckList.get(i), i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        singleCheckViewHolder.itemView.findViewById(R.id.container_wrapper).setRotation((float) this.mDegree);
    }

    public void onBindViewHolder(@NonNull SingleCheckViewHolder singleCheckViewHolder, int i, @NonNull List list) {
        if (list.isEmpty()) {
            super.onBindViewHolder(singleCheckViewHolder, i, list);
        } else {
            onBindViewHolder(singleCheckViewHolder, i);
        }
    }

    public SingleCheckViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SingleCheckViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.makeup_item, viewGroup, false), this);
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
                    view.postDelayed(new O00000Oo(view), 100);
                }
            } else {
                view.setContentDescription(view.getContext().getString(i));
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public void setRotation(int i) {
        this.mDegree = i;
    }

    public void setSelectedPosition(int i) {
        this.mSelectedItem = i;
    }
}

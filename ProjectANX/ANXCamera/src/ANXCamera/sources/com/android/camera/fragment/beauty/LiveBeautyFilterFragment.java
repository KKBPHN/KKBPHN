package com.android.camera.fragment.beauty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.EffectController;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.LiveFilterChangers;
import com.android.camera.ui.ScrollTextview;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class LiveBeautyFilterFragment extends Fragment implements OnClickListener {
    public static final int LIVE_FILTER_NONE_ID = 0;
    private static final String TAG = "LiveBeautyFilterFragment";
    /* access modifiers changed from: private */
    public boolean isAnimation = false;
    /* access modifiers changed from: private */
    public CubicEaseOutInterpolator mCubicEaseOut;
    /* access modifiers changed from: private */
    public int mCurrentIndex = 0;
    private FilterItemAdapter mFilterItemAdapter;
    /* access modifiers changed from: private */
    public List mFilters;
    private int mHolderWidth;
    /* access modifiers changed from: private */
    public int mLastIndex = -1;
    private LinearLayoutManagerWrapper mLayoutManager;
    private RecyclerView mRecyclerView;
    private int mTotalWidth;
    private View mView;

    public class EffectItemPadding extends ItemDecoration {
        protected int mEffectListLeft;
        protected int mHorizontalPadding;
        protected int mVerticalPadding;

        public EffectItemPadding() {
            this.mHorizontalPadding = LiveBeautyFilterFragment.this.getContext().getResources().getDimensionPixelSize(R.dimen.effect_item_padding_horizontal);
            this.mVerticalPadding = LiveBeautyFilterFragment.this.getContext().getResources().getDimensionPixelSize(R.dimen.effect_item_padding_vertical);
            this.mEffectListLeft = LiveBeautyFilterFragment.this.getContext().getResources().getDimensionPixelSize(R.dimen.effect_list_padding_left);
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            int i = recyclerView.getChildPosition(view) == 0 ? this.mEffectListLeft : 0;
            int i2 = this.mVerticalPadding;
            rect.set(i, i2, this.mHorizontalPadding, i2);
        }
    }

    public class FilterItemAdapter extends Adapter {
        protected LayoutInflater mLayoutInflater;

        public FilterItemAdapter(Context context) {
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        public int getItemCount() {
            return LiveBeautyFilterFragment.this.mFilters.size();
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            LiveFilterItem liveFilterItem = (LiveFilterItem) LiveBeautyFilterFragment.this.mFilters.get(i);
            FilterItemHolder filterItemHolder = (FilterItemHolder) viewHolder;
            filterItemHolder.itemView.setTag(Integer.valueOf(i));
            filterItemHolder.bindEffectIndex(i, liveFilterItem);
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = this.mLayoutInflater.inflate(R.layout.live_filter_item, viewGroup, false);
            FilterStillItemHolder filterStillItemHolder = new FilterStillItemHolder(inflate);
            inflate.setOnClickListener(LiveBeautyFilterFragment.this);
            return filterStillItemHolder;
        }
    }

    public abstract class FilterItemHolder extends ViewHolder {
        protected int mEffectIndex;
        protected ScrollTextview mTextView;

        public FilterItemHolder(View view) {
            super(view);
            this.mTextView = (ScrollTextview) view.findViewById(R.id.effect_item_text);
        }

        public void bindEffectIndex(int i, LiveFilterItem liveFilterItem) {
            this.mEffectIndex = i;
            this.mTextView.setText(liveFilterItem.name);
        }
    }

    public class FilterStillItemHolder extends FilterItemHolder {
        private ImageView mImageView;
        private ImageView mSelectedOuterIndicator;

        public FilterStillItemHolder(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(R.id.effect_item_image);
            this.mSelectedOuterIndicator = (ImageView) view.findViewById(R.id.effect_item_selected_indicator);
        }

        private void normalAnim(View view) {
            ViewCompat.setAlpha(view, 1.0f);
            ViewCompat.animate(view).setDuration(500).alpha(0.0f).setInterpolator(LiveBeautyFilterFragment.this.mCubicEaseOut).setListener(new ViewPropertyAnimatorListener() {
                public void onAnimationCancel(View view) {
                }

                public void onAnimationEnd(View view) {
                    view.setVisibility(8);
                }

                public void onAnimationStart(View view) {
                }
            }).start();
        }

        private void selectAnim(View view) {
            ViewCompat.setAlpha(this.mSelectedOuterIndicator, 0.0f);
            ViewCompat.animate(view).setDuration(500).alpha(1.0f).setInterpolator(LiveBeautyFilterFragment.this.mCubicEaseOut).setListener(new ViewPropertyAnimatorListener() {
                public void onAnimationCancel(View view) {
                }

                public void onAnimationEnd(View view) {
                }

                public void onAnimationStart(View view) {
                    view.setVisibility(0);
                }
            }).start();
        }

        public void bindEffectIndex(int i, LiveFilterItem liveFilterItem) {
            float f;
            ImageView imageView;
            super.bindEffectIndex(i, liveFilterItem);
            this.mImageView.setImageDrawable(liveFilterItem.imageViewRes);
            if (i == LiveBeautyFilterFragment.this.mCurrentIndex) {
                this.itemView.setActivated(true);
                if (Util.isAccessible()) {
                    this.itemView.postDelayed(new Runnable() {
                        public void run() {
                            if (LiveBeautyFilterFragment.this.isAdded()) {
                                FilterStillItemHolder.this.itemView.sendAccessibilityEvent(128);
                            }
                        }
                    }, 100);
                }
                if (LiveBeautyFilterFragment.this.isAnimation) {
                    selectAnim(this.mSelectedOuterIndicator);
                    return;
                }
                this.mSelectedOuterIndicator.setVisibility(0);
                imageView = this.mSelectedOuterIndicator;
                f = 1.0f;
            } else {
                this.itemView.setActivated(false);
                if (!LiveBeautyFilterFragment.this.isAnimation || i != LiveBeautyFilterFragment.this.mLastIndex) {
                    this.mSelectedOuterIndicator.setVisibility(8);
                    imageView = this.mSelectedOuterIndicator;
                    f = 0.0f;
                } else {
                    normalAnim(this.mSelectedOuterIndicator);
                    return;
                }
            }
            imageView.setAlpha(f);
        }
    }

    public class LiveFilterItem {
        public String directoryName;
        public int id;
        public Drawable imageViewRes;
        public String name;
    }

    private int findIndex(int i) {
        for (int i2 = 0; i2 < this.mFilters.size(); i2++) {
            if (((LiveFilterItem) this.mFilters.get(i2)).id == i) {
                return i2;
            }
        }
        return 0;
    }

    private void initData() {
        this.mFilters = EffectController.getInstance().getLiveFilterList(getContext());
        this.mCurrentIndex = findIndex(DataRepository.dataItemLive().getLiveFilter());
    }

    private void initView() {
        this.mRecyclerView = (RecyclerView) this.mView.findViewById(R.id.effect_list);
        this.mRecyclerView.setFocusable(false);
        this.mCubicEaseOut = new CubicEaseOutInterpolator();
        this.mFilterItemAdapter = new FilterItemAdapter(getContext());
        this.mLayoutManager = new LinearLayoutManagerWrapper(getContext(), "effect_list");
        this.mLayoutManager.setOrientation(0);
        this.mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, EffectController.getInstance().getEffectCount(1));
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.addItemDecoration(new EffectItemPadding());
        this.mRecyclerView.setAdapter(this.mFilterItemAdapter);
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                LiveBeautyFilterFragment.this.isAnimation = false;
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setChangeDuration(150);
        defaultItemAnimator.setMoveDuration(150);
        defaultItemAnimator.setAddDuration(150);
        this.mRecyclerView.setItemAnimator(defaultItemAnimator);
        this.mTotalWidth = getResources().getDisplayMetrics().widthPixels;
        this.mHolderWidth = getResources().getDimensionPixelSize(R.dimen.effect_item_width);
    }

    private void notifyItemChanged(int i, int i2) {
        if (i > -1) {
            this.mFilterItemAdapter.notifyItemChanged(i);
        }
        if (i2 > -1) {
            this.mFilterItemAdapter.notifyItemChanged(i2);
        }
    }

    private void onItemSelected(int i, boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onItemSelected: index = ");
        sb.append(i);
        sb.append(", fromClick = ");
        sb.append(z);
        Log.u(str, sb.toString());
        if (((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)) == null) {
            Log.e(TAG, "onItemSelected: configChanges = null");
            return;
        }
        try {
            selectItem(i);
        } catch (NumberFormatException e) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("invalid filter id: ");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString());
        }
    }

    private void scrollIfNeed(int i) {
        if (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.max(0, i - 1));
        } else if (i == this.mLayoutManager.findLastVisibleItemPosition() || i == this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.min(i + 1, this.mFilterItemAdapter.getItemCount() - 1));
        }
    }

    private void selectItem(int i) {
        if (i != -1) {
            this.mLastIndex = this.mCurrentIndex;
            this.mCurrentIndex = i;
            scrollIfNeed(i);
            notifyItemChanged(this.mLastIndex, this.mCurrentIndex);
        }
    }

    private void showSelected(ImageView imageView, int i) {
        float dimension = getResources().getDimension(R.dimen.live_filter_item_mask_size);
        float dimension2 = getResources().getDimension(R.dimen.live_filter_item_corners_size);
        int i2 = (int) dimension;
        Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRoundRect(new RectF(4.0f, 4.0f, dimension, dimension), dimension2, dimension2, paint);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), i);
        Bitmap createBitmap2 = Bitmap.createBitmap(createBitmap.getWidth(), createBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap2);
        Paint paint2 = new Paint();
        canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
        paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas2.drawBitmap(decodeResource, 0.0f, 0.0f, paint2);
        paint2.setXfermode(null);
        imageView.setImageBitmap(createBitmap2);
    }

    public void onClick(View view) {
        if (this.mRecyclerView.isEnabled()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction == null || !cameraAction.isDoingAction()) {
                int intValue = ((Integer) view.getTag()).intValue();
                if (this.mCurrentIndex != intValue) {
                    this.isAnimation = false;
                    LiveFilterChangers liveFilterChangers = (LiveFilterChangers) ModeCoordinatorImpl.getInstance().getAttachProtocol(243);
                    if (liveFilterChangers != null) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("filter_path:");
                        sb.append(((LiveFilterItem) this.mFilters.get(intValue)).directoryName);
                        Log.e(str, sb.toString());
                        if (intValue != 0) {
                            liveFilterChangers.setFilter(true, ((LiveFilterItem) this.mFilters.get(intValue)).directoryName);
                        } else {
                            liveFilterChangers.setFilter(false, null);
                        }
                        DataRepository.dataItemLive().setLiveFilter(((LiveFilterItem) this.mFilters.get(intValue)).id);
                    }
                    onItemSelected(intValue, true);
                }
            }
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        if (this.mView == null) {
            this.mView = LayoutInflater.from(getContext()).inflate(R.layout.live_fragment_filter, viewGroup, false);
            initView();
            initData();
        }
        return this.mView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        int i = (this.mTotalWidth / 2) - (this.mHolderWidth / 2);
        this.mFilterItemAdapter.notifyDataSetChanged();
        this.mLayoutManager.scrollToPositionWithOffset(this.mCurrentIndex, i);
    }
}

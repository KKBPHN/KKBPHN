package com.android.camera.fragment.beauty;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.data.data.TypeItem;
import com.android.camera.data.data.runing.ComponentRunningShine.ShineType;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.EffectItemAdapter.EffectItemPadding;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.protocol.ModeProtocol.MiBeautyProtocol;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseBeautyMakeupFragment extends BaseBeautyFragment {
    protected static final int EXTRA_CLEAR = 2;
    protected static final int EXTRA_NULL = -1;
    protected static final int EXTRA_RESET = 1;
    private static final String TAG = "BaseBeautyMakeup";
    protected int mAlphaElement;
    private List mAugmentItemList;
    protected int mBetaElement;
    protected OnItemClickListener mClickListener;
    protected int mHeaderCustomWidth;
    private LinearLayout mHeaderRecyclerView;
    protected List mItemList;
    private int mItemMargin;
    private int mItemWidth;
    /* access modifiers changed from: private */
    public int mLastSelectedPosition = -1;
    protected MyLayoutManager mLayoutManager;
    protected MakeupSingleCheckAdapter mMakeupAdapter;
    private RecyclerView mMakeupItemList;
    private boolean mNeedScroll;
    /* access modifiers changed from: private */
    public int mPositionFirstItem = 0;
    /* access modifiers changed from: private */
    public int mPositionLastItem = 0;
    protected int mSelectedParam = 0;
    protected int mSelectedPosition = 0;
    private int mTotalWidth;

    public class MyLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public MyLayoutManager(Context context) {
            super(context);
        }

        public boolean canScrollHorizontally() {
            return this.isScrollEnabled && super.canScrollHorizontally();
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        public void setScrollEnabled(boolean z) {
            this.isScrollEnabled = z;
        }
    }

    private void animateView(View view) {
        view.clearAnimation();
        view.setRotation(0.0f);
        ViewCompat.animate(view).rotation(360.0f).setDuration(500).setListener(new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(View view) {
                super.onAnimationEnd(view);
                view.setRotation(0.0f);
            }
        }).start();
    }

    private void calcItemWidthAndNeedScroll() {
        this.mTotalWidth = getResources().getDisplayMetrics().widthPixels;
        int size = this.mAugmentItemList.size();
        if (size != 0) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_width);
            float integer = ((float) getResources().getInteger(R.integer.beauty_list_max_count)) + 0.5f;
            int i = this.mTotalWidth;
            int i2 = (int) (((float) (i - dimensionPixelSize)) / integer);
            int i3 = dimensionPixelSize * 2;
            int max = Math.max((i - i3) / size, i2);
            if (max == i2) {
                this.mNeedScroll = true;
            } else {
                this.mNeedScroll = false;
                max = ((this.mTotalWidth - (this.mItemMargin * 2)) - i3) / size;
            }
            this.mItemWidth = max;
        }
    }

    private boolean hasAlpha() {
        return this.mAlphaElement != -1;
    }

    private boolean hasBeta() {
        return this.mBetaElement != -1;
    }

    private TypeItem initAlphaItem() {
        int i;
        TypeItem typeItem = new TypeItem(-1, -1, (String) null, (String) null);
        int i2 = this.mAlphaElement;
        if (i2 != 1) {
            if (i2 == 2) {
                typeItem.mIconRes = R.drawable.ic_vector_beauty_reset;
                i = R.string.face_beauty_close;
            }
            return typeItem;
        }
        typeItem.mIconRes = R.drawable.ic_vector_beauty_reset;
        i = R.string.beauty_reset;
        typeItem.mDisplayNameRes = i;
        return typeItem;
    }

    private TypeItem initBetaItem() {
        int i;
        TypeItem typeItem = new TypeItem(-1, -1, (String) null, (String) null);
        int i2 = this.mBetaElement;
        if (i2 != 1) {
            if (i2 == 2) {
                typeItem.mIconRes = R.drawable.ic_vector_beauty_clear;
                i = R.string.beauty_clear;
            }
            return typeItem;
        }
        typeItem.mIconRes = R.drawable.ic_vector_beauty_reset;
        i = R.string.beauty_reset;
        typeItem.mDisplayNameRes = i;
        return typeItem;
    }

    private final List initItems() {
        MiBeautyProtocol miBeautyProtocol = (MiBeautyProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(194);
        if (miBeautyProtocol != null) {
            return miBeautyProtocol.getSupportedBeautyItems(getShineType());
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void notifyItemChanged(int i, int i2) {
        if (i > -1) {
            if (Util.isAccessible()) {
                List list = this.mAugmentItemList;
                if (list != null) {
                    int textResource = ((TypeItem) list.get(i)).getTextResource();
                    ViewHolder findViewHolderForAdapterPosition = this.mMakeupItemList.findViewHolderForAdapterPosition(i);
                    if (findViewHolderForAdapterPosition != null) {
                        View view = findViewHolderForAdapterPosition.itemView;
                        if (textResource <= 0) {
                            textResource = R.string.lighting_pattern_null;
                        }
                        view.setContentDescription(getString(textResource));
                    }
                }
            }
            this.mMakeupAdapter.notifyItemChanged(i, new Object());
        }
        if (i2 > -1) {
            if (Util.isAccessible()) {
                List list2 = this.mAugmentItemList;
                if (list2 != null) {
                    int textResource2 = ((TypeItem) list2.get(i2)).getTextResource();
                    ViewHolder findViewHolderForAdapterPosition2 = this.mMakeupItemList.findViewHolderForAdapterPosition(i2);
                    if (findViewHolderForAdapterPosition2 != null && isAdded()) {
                        this.mMakeupAdapter.setAccessible(findViewHolderForAdapterPosition2.itemView, textResource2, true);
                    }
                }
            }
            this.mMakeupAdapter.notifyItemChanged(i2, new Object());
        }
    }

    /* access modifiers changed from: private */
    public void onExtraClick(View view, int i) {
        int i2 = (!hasAlpha() || i != 0) ? (!hasBeta() || i != 1) ? -1 : this.mBetaElement : this.mAlphaElement;
        if (i2 == 1) {
            onResetClick();
        } else if (i2 == 2) {
            onClearClick();
        }
    }

    /* access modifiers changed from: private */
    public boolean scrollIfNeed(int i) {
        int i2 = (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) ? Math.max(0, i - 1) : (i == this.mLayoutManager.findLastVisibleItemPosition() || i == this.mLayoutManager.findLastCompletelyVisibleItemPosition()) ? Math.min(i + 1, this.mLayoutManager.getItemCount() - 1) : i;
        if (i2 == i) {
            return false;
        }
        this.mLayoutManager.scrollToPosition(i2);
        return true;
    }

    private void setItemInCenter(int i) {
        this.mLayoutManager.scrollToPositionWithOffset(i, (this.mTotalWidth / 2) - (this.mItemWidth / 2));
    }

    /* access modifiers changed from: protected */
    public void augmentItemList() {
        this.mAugmentItemList = new ArrayList();
        this.mPositionFirstItem = 0;
        if (hasAlpha()) {
            this.mAugmentItemList.add(initAlphaItem());
            this.mPositionFirstItem++;
        }
        if (hasBeta()) {
            this.mAugmentItemList.add(initBetaItem());
            this.mPositionFirstItem++;
        }
        for (TypeItem add : this.mItemList) {
            this.mAugmentItemList.add(add);
        }
        this.mPositionLastItem = (this.mPositionFirstItem + this.mItemList.size()) - 1;
        this.mSelectedPosition = this.mPositionFirstItem;
    }

    /* access modifiers changed from: protected */
    public View getAnimateView() {
        return this.mHeaderRecyclerView;
    }

    public abstract String getClassString();

    /* access modifiers changed from: protected */
    public int getListItemMargin() {
        if (!isNeedScroll()) {
            return getResources().getDimensionPixelSize(R.dimen.beauty_item_margin);
        }
        return 0;
    }

    @ShineType
    public abstract String getShineType();

    public abstract void initExtraType();

    /* access modifiers changed from: protected */
    public OnItemClickListener initOnItemClickListener() {
        return new OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                Object tag = view.getTag();
                if (tag != null && (tag instanceof TypeItem)) {
                    BaseBeautyMakeupFragment.this.onAdapterItemClick((TypeItem) tag);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mHeaderRecyclerView = (LinearLayout) view.findViewById(R.id.header_recyclerView);
        this.mMakeupItemList = (RecyclerView) view.findViewById(R.id.makeup_item_list);
        this.mLayoutManager = new MyLayoutManager(getActivity());
        this.mLayoutManager.setOrientation(0);
        this.mLayoutManager.setScrollEnabled(true);
        this.mMakeupItemList.setLayoutManager(this.mLayoutManager);
        this.mMakeupItemList.setFocusable(false);
        this.mItemList = initItems();
        initExtraType();
        augmentItemList();
        calcItemWidthAndNeedScroll();
        if (!isNeedScroll()) {
            this.mLayoutManager.setScrollEnabled(false);
        }
        this.mMakeupAdapter = new MakeupSingleCheckAdapter(getActivity(), this.mAugmentItemList, this.mPositionFirstItem, this.mPositionLastItem);
        this.mMakeupAdapter.setRotation(this.mDegree);
        this.mClickListener = initOnItemClickListener();
        this.mMakeupAdapter.setOnItemClickListener(new OnItemClickListener() {
            /* JADX WARNING: Code restructure failed: missing block: B:9:0x0045, code lost:
                if (com.android.camera.fragment.beauty.BaseBeautyMakeupFragment.access$400(r7, r7.mSelectedPosition) == false) goto L_0x0047;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                BaseBeautyMakeupFragment baseBeautyMakeupFragment = BaseBeautyMakeupFragment.this;
                baseBeautyMakeupFragment.mLastSelectedPosition = baseBeautyMakeupFragment.mSelectedPosition;
                BaseBeautyMakeupFragment baseBeautyMakeupFragment2 = BaseBeautyMakeupFragment.this;
                baseBeautyMakeupFragment2.mSelectedParam = i - baseBeautyMakeupFragment2.mPositionFirstItem;
                BaseBeautyMakeupFragment baseBeautyMakeupFragment3 = BaseBeautyMakeupFragment.this;
                baseBeautyMakeupFragment3.mSelectedPosition = i;
                if (i < baseBeautyMakeupFragment3.mPositionFirstItem || i > BaseBeautyMakeupFragment.this.mPositionLastItem) {
                    BaseBeautyMakeupFragment.this.onExtraClick(view, i);
                } else {
                    BaseBeautyMakeupFragment baseBeautyMakeupFragment4 = BaseBeautyMakeupFragment.this;
                    baseBeautyMakeupFragment4.mClickListener.onItemClick(adapterView, view, baseBeautyMakeupFragment4.mSelectedParam, j);
                }
                if (BaseBeautyMakeupFragment.this.isNeedScroll()) {
                    BaseBeautyMakeupFragment baseBeautyMakeupFragment5 = BaseBeautyMakeupFragment.this;
                }
                if (!Util.isAccessible()) {
                    return;
                }
                BaseBeautyMakeupFragment baseBeautyMakeupFragment6 = BaseBeautyMakeupFragment.this;
                baseBeautyMakeupFragment6.notifyItemChanged(baseBeautyMakeupFragment6.mLastSelectedPosition, BaseBeautyMakeupFragment.this.mSelectedPosition);
            }
        });
        this.mMakeupAdapter.setSelectedPosition(this.mSelectedPosition);
        this.mMakeupItemList.setAdapter(this.mMakeupAdapter);
        this.mMakeupItemList.addItemDecoration(new EffectItemPadding(getContext()));
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setChangeDuration(150);
        defaultItemAnimator.setMoveDuration(150);
        defaultItemAnimator.setAddDuration(150);
        this.mMakeupItemList.setItemAnimator(defaultItemAnimator);
        this.mMakeupAdapter.notifyDataSetChanged();
        setItemInCenter(this.mSelectedPosition);
    }

    /* access modifiers changed from: protected */
    public boolean isNeedScroll() {
        return this.mNeedScroll;
    }

    public abstract void onAdapterItemClick(TypeItem typeItem);

    public abstract void onClearClick();

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_beauty_makeup, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public abstract void onResetClick();

    /* access modifiers changed from: protected */
    public void onViewCreatedAndVisibleToUser(boolean z) {
        super.onViewCreatedAndVisibleToUser(z);
        List list = this.mItemList;
        if (list != null && !list.isEmpty()) {
            TypeItem typeItem = (TypeItem) this.mItemList.get(this.mSelectedParam);
            MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
            if (makeupProtocol != null) {
                makeupProtocol.onMakeupItemSelected(typeItem.mKeyOrType, typeItem.mDisplayNameRes, false);
            }
        }
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mMakeupItemList != null) {
            for (int i2 = 0; i2 < this.mMakeupItemList.getChildCount(); i2++) {
                list.add(this.mMakeupItemList.getChildAt(i2).findViewById(R.id.container_wrapper));
            }
        }
        MakeupSingleCheckAdapter makeupSingleCheckAdapter = this.mMakeupAdapter;
        if (makeupSingleCheckAdapter != null) {
            makeupSingleCheckAdapter.setRotation(i);
            int findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = this.mLayoutManager.findLastVisibleItemPosition();
            for (int i3 = 0; i3 < findFirstVisibleItemPosition; i3++) {
                this.mMakeupAdapter.notifyItemChanged(i3);
            }
            while (true) {
                findLastVisibleItemPosition++;
                if (findLastVisibleItemPosition < this.mMakeupAdapter.getItemCount()) {
                    this.mMakeupAdapter.notifyItemChanged(findLastVisibleItemPosition);
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void selectFirstItem() {
        this.mSelectedPosition = this.mPositionFirstItem;
        this.mSelectedParam = 0;
        this.mMakeupAdapter.setSelectedPosition(this.mSelectedPosition);
        this.mLayoutManager.scrollToPosition(this.mSelectedPosition);
        MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
        if (makeupProtocol != null) {
            makeupProtocol.onMakeupItemSelected(((TypeItem) this.mItemList.get(this.mSelectedParam)).mKeyOrType, ((TypeItem) this.mItemList.get(this.mSelectedParam)).mDisplayNameRes, true);
        }
        int i = this.mLastSelectedPosition;
        int i2 = this.mSelectedPosition;
        if (i != i2) {
            notifyItemChanged(i, i2);
        }
    }

    public void setDegree(int i) {
        super.setDegree(i);
        MakeupSingleCheckAdapter makeupSingleCheckAdapter = this.mMakeupAdapter;
        if (makeupSingleCheckAdapter != null) {
            makeupSingleCheckAdapter.setRotation(i);
            this.mMakeupAdapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void setListPadding(RecyclerView recyclerView) {
        int i;
        if (recyclerView != null) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.beauty_model_recycler_padding_left);
            if (!isNeedScroll()) {
                dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.beauty_padding_left);
                i = getResources().getDimensionPixelSize(R.dimen.beauty_padding_right);
            } else {
                i = 0;
            }
            recyclerView.setPadding(dimensionPixelSize, 0, i, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void toast(String str) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.beauty_reset_toast_margin_bottom);
        if (!Display.isFullScreenNavBarHidden()) {
            dimensionPixelSize -= Display.getNavigationBarHeight();
        }
        ToastUtils.showToast((Context) getActivity(), str, 80, 0, dimensionPixelSize - (getResources().getDimensionPixelSize(R.dimen.beauty_reset_toast_height) / 2));
    }
}

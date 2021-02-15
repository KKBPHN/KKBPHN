package com.android.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.customization.TintColor;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.global.ComponentModuleList;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.ModeChangeController;
import java.util.Arrays;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

public class ModeSelectView extends RecyclerView implements OnGlobalLayoutListener {
    private static final int BOUNCE_RANGE = 100;
    /* access modifiers changed from: private */
    public static final String TAG = "ModeSelectView";
    public static final int TYPE_HEADER_FOOTER = 1;
    public static final int TYPE_NORMAL = 0;
    private ModeSelectAdapter mAdapter;
    /* access modifiers changed from: private */
    public int mCurMode;
    private boolean mInit = false;
    private boolean mIsModeChange;
    /* access modifiers changed from: private */
    public List mItems;
    /* access modifiers changed from: private */
    public ModeLayoutManager mLayoutManager;
    /* access modifiers changed from: private */
    public onModeSelectedListener mModeSelectedListener;
    private TextView mPaintView;
    /* access modifiers changed from: private */
    public ModeSnapHelper mSnapHelper;
    private int mTempMode;

    public class ModeLayoutManager extends LinearLayoutManager {
        public ModeLayoutManager(Context context) {
            super(context);
        }

        public void onLayoutCompleted(State state) {
            super.onLayoutCompleted(state);
        }
    }

    public class ModeSelectAdapter extends Adapter {
        public ModeSelectAdapter() {
        }

        public /* synthetic */ void O00oOooO(View view) {
            if (ModeSelectView.this.canScroll()) {
                int access$700 = ModeSelectView.this.getModeFromTag(view);
                ModeSelectView modeSelectView = ModeSelectView.this;
                if (!modeSelectView.isSameMode(access$700, modeSelectView.mCurMode)) {
                    String access$100 = ModeSelectView.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mode change , mCurMode = ");
                    sb.append(ModeSelectView.this.mCurMode);
                    sb.append(", newMode = ");
                    sb.append(access$700);
                    Log.d(access$100, sb.toString());
                    ModeSelectView.this.mCurMode = access$700;
                    if (ModeSelectView.this.mModeSelectedListener != null) {
                        onModeSelectedListener access$1000 = ModeSelectView.this.mModeSelectedListener;
                        int access$800 = ModeSelectView.this.mCurMode;
                        ModeSelectView modeSelectView2 = ModeSelectView.this;
                        access$1000.onModeSelected(access$800, modeSelectView2.getItemText(modeSelectView2.getSelectPos()));
                    }
                    int[] calculateDistanceToFinalSnap = ModeSelectView.this.mSnapHelper.calculateDistanceToFinalSnap(ModeSelectView.this.mLayoutManager, view);
                    ModeSelectView.this.smoothScrollBy(calculateDistanceToFinalSnap[0], calculateDistanceToFinalSnap[1], new CubicEaseOutInterpolator());
                    ModeSelectView.this.updateSelectedItemColor(access$700, true);
                }
            }
        }

        public int getItemCount() {
            return ModeSelectView.this.mItems.size() + 2;
        }

        public int getItemViewType(int i) {
            return (i < 1 || i >= getItemCount() - 1) ? 1 : 0;
        }

        public void onBindViewHolder(@NonNull ModeSelectViewHolder modeSelectViewHolder, int i) {
            if (getItemViewType(i) == 0) {
                modeSelectViewHolder.mModeItem.setText(ModeSelectView.this.getItemText(i));
                modeSelectViewHolder.mModeItem.setTextColor(ModeSelectView.this.isItemSelected(i) ? TintColor.tintColor() : ModeSelectView.this.getResources().getColor(R.color.mode_name_color));
                modeSelectViewHolder.itemView.setTag(Integer.valueOf(ModeSelectView.this.getModeByPos(i)));
            }
        }

        @NonNull
        public ModeSelectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (i == 1) {
                View view = new View(ModeSelectView.this.getContext());
                view.setLayoutParams(new LayoutParams(ModeSelectView.this.getHeaderOffset() + 100, -1));
                Log.d(ModeSelectView.TAG, "[onCreateViewHolder] h&f");
                return new ModeSelectViewHolder(view);
            }
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(ModeSelectView.this.getContext()).inflate(R.layout.mode_select_item, viewGroup, false);
            frameLayout.setOnClickListener(new C0415O00000oO(this));
            return new ModeSelectViewHolder(frameLayout);
        }
    }

    public class ModeSelectViewHolder extends ViewHolder {
        TextView mModeItem;

        public ModeSelectViewHolder(@NonNull View view) {
            super(view);
            this.mModeItem = (TextView) view.findViewById(R.id.mode_select_item);
        }
    }

    public class ModeSnapHelper extends LinearSnapHelper {
        public ModeSnapHelper() {
        }

        public int[] calculateDistanceToFinalSnap(@NonNull LayoutManager layoutManager, @NonNull View view) {
            return super.calculateDistanceToFinalSnap(layoutManager, view);
        }

        public int[] calculateScrollDistance(int i, int i2) {
            int[] iArr = new int[2];
            View findSnapView = findSnapView(ModeSelectView.this.mLayoutManager);
            if (findSnapView != null) {
                int width = findSnapView.getWidth();
                if (i <= 0) {
                    width = -width;
                }
                iArr[0] = width;
            }
            return iArr;
        }

        public View findSnapView(LayoutManager layoutManager) {
            return super.findSnapView(layoutManager);
        }

        public int findTargetSnapPosition(LayoutManager layoutManager, int i, int i2) {
            return super.findTargetSnapPosition(layoutManager, i, i2);
        }
    }

    public interface onModeSelectedListener {
        void onModeSelected(int i, String str);
    }

    public ModeSelectView(@NonNull Context context) {
        super(context);
    }

    public ModeSelectView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ModeSelectView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: private */
    public boolean canScroll() {
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null && cameraAction.isDoingAction()) {
            return false;
        }
        ModeChangeController modeChangeController = (ModeChangeController) ModeCoordinatorImpl.getInstance().getAttachProtocol(179);
        return (modeChangeController == null || modeChangeController.canSwipeChangeMode()) && getScrollState() != 2;
    }

    /* access modifiers changed from: private */
    public int getHeaderOffset() {
        if (this.mPaintView == null) {
            this.mPaintView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.mode_select_item, null, false).findViewById(R.id.mode_select_item);
        }
        return (((Util.getScreenWidth(getContext()) - Display.getStartMargin()) - Display.getEndMargin()) - (((int) this.mPaintView.getPaint().measureText(getItemText(getSelectPos()))) + (getResources().getDimensionPixelSize(R.dimen.mode_select_item_gap) * 2))) / 2;
    }

    private int getItemMode(int i) {
        if (i == 180) {
            return 167;
        }
        if (i == 176) {
            return 166;
        }
        return i;
    }

    /* access modifiers changed from: private */
    public String getItemText(int i) {
        if (i <= 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getItemText fail, pos is ");
            sb.append(i);
            Log.w(str, sb.toString());
            return "";
        }
        ComponentDataItem componentDataItem = (ComponentDataItem) this.mItems.get(i - 1);
        if (componentDataItem.mDisplayNameRes != 0) {
            return getContext().getString(componentDataItem.mDisplayNameRes);
        }
        String str2 = componentDataItem.mDisplayNameStr;
        if (str2 != null) {
            return str2;
        }
        throw new IllegalStateException("can't find mode text.");
    }

    /* access modifiers changed from: private */
    public int getModeByPos(int i) {
        if (getAdapter() == null || i == 0 || i == getAdapter().getItemCount()) {
            return 163;
        }
        return Integer.parseInt(((ComponentDataItem) this.mItems.get(i - 1)).mValue);
    }

    /* access modifiers changed from: private */
    public int getModeFromTag(View view) {
        Object tag = view.getTag();
        if (tag != null) {
            return ((Integer) tag).intValue();
        }
        return 160;
    }

    private int getPosition(int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < this.mItems.size(); i3++) {
            if (Integer.parseInt(((ComponentDataItem) this.mItems.get(i3)).mValue) == i) {
                return i3 + 1;
            }
            if (163 == Integer.parseInt(((ComponentDataItem) this.mItems.get(i3)).mValue)) {
                i2 = i3 + 1;
            }
        }
        return i2;
    }

    /* access modifiers changed from: private */
    public int getSelectPos() {
        return getPosition(this.mCurMode);
    }

    private int getSelectedMode(int i) {
        View findSnapView = this.mSnapHelper.findSnapView(this.mLayoutManager);
        if (findSnapView != null) {
            return getModeFromTag(findSnapView);
        }
        Log.d(TAG, "target is null???");
        return i;
    }

    /* access modifiers changed from: private */
    public boolean isItemSelected(int i) {
        boolean z = false;
        if (i <= 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("isItemSelected fail, pos is ");
            sb.append(i);
            Log.w(str, sb.toString());
            return false;
        }
        if (Integer.parseInt(((ComponentDataItem) this.mItems.get(i - 1)).mValue) == this.mCurMode) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public boolean isSameMode(int i, int i2) {
        boolean z = i == i2;
        if (i == 163 && i2 == 165) {
            return true;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void updateSelectedItemColor(int i, boolean z) {
        int i2;
        TextView textView;
        if (this.mItems != null && this.mLayoutManager != null) {
            int i3 = 0;
            while (i3 < this.mItems.size()) {
                i3++;
                View findViewByPosition = this.mLayoutManager.findViewByPosition(i3);
                if (findViewByPosition != null) {
                    ModeSelectViewHolder modeSelectViewHolder = (ModeSelectViewHolder) findContainingViewHolder(findViewByPosition);
                    if (modeSelectViewHolder != null) {
                        if (modeSelectViewHolder.mModeItem != null) {
                            if (isSameMode(getModeFromTag(findViewByPosition), i)) {
                                if (modeSelectViewHolder.mModeItem.getCurrentTextColor() != TintColor.tintColor() && z) {
                                    ViberatorContext.getInstance(getContext().getApplicationContext()).performModeSwitch();
                                }
                                textView = modeSelectViewHolder.mModeItem;
                                i2 = TintColor.tintColor();
                            } else {
                                textView = modeSelectViewHolder.mModeItem;
                                i2 = getResources().getColor(R.color.mode_name_color);
                            }
                            textView.setTextColor(i2);
                        }
                    }
                }
            }
        }
    }

    public /* synthetic */ void O000Oo00() {
        updateSelectedItemColor(this.mCurMode, false);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (canScroll()) {
            return super.dispatchTouchEvent(motionEvent);
        }
        Log.v(TAG, "dispatchTouchEvent skip.");
        return false;
    }

    public boolean fling(int i, int i2) {
        boolean fling = super.fling(i, i2);
        if (!this.mIsModeChange) {
            return fling;
        }
        return false;
    }

    public int getCurSelectMode() {
        return this.mCurMode;
    }

    public void init(ComponentModuleList componentModuleList, int i, onModeSelectedListener onmodeselectedlistener) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("init curMode = ");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mModeSelectedListener = onmodeselectedlistener;
        this.mItems = componentModuleList.getCommonItems();
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("init mItems = ");
        sb2.append(Arrays.toString(this.mItems.toArray()));
        Log.d(str2, sb2.toString());
        this.mCurMode = getItemMode(i);
        this.mTempMode = this.mCurMode;
        if (this.mAdapter == null) {
            this.mAdapter = new ModeSelectAdapter();
            setAdapter(this.mAdapter);
        }
        if (this.mLayoutManager == null) {
            this.mLayoutManager = new ModeLayoutManager(getContext());
            this.mLayoutManager.setOrientation(0);
            setLayoutManager(this.mLayoutManager);
        }
        if (this.mSnapHelper == null) {
            this.mSnapHelper = new ModeSnapHelper();
            this.mSnapHelper.attachToRecyclerView(this);
        }
        this.mInit = false;
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void moveToPosition(int i) {
        if (this.mCurMode != i) {
            this.mCurMode = i;
            this.mTempMode = i;
        }
        int position = getPosition(i);
        int headerOffset = getHeaderOffset();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("moveToPosition select pos = ");
        sb.append(position);
        sb.append(", offset = ");
        sb.append(headerOffset);
        sb.append(", mode = ");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mLayoutManager.scrollToPositionWithOffset(position, headerOffset);
        post(new C0416O00000oo(this));
    }

    public void onGlobalLayout() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!this.mInit) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onGlobalLayout mCurMode ");
            sb.append(this.mCurMode);
            Log.d(str, sb.toString());
            moveToPosition(this.mCurMode);
            this.mInit = true;
        }
    }

    public void onScrollStateChanged(int i) {
        super.onScrollStateChanged(i);
        int selectedMode = getSelectedMode(this.mCurMode);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onScrollStateChanged state = ");
        sb.append(i);
        Log.d(str, sb.toString());
        if (i == 0 && this.mCurMode != selectedMode) {
            this.mIsModeChange = false;
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mode change , mCurMode = ");
            sb2.append(this.mCurMode);
            sb2.append(", newMode = ");
            sb2.append(selectedMode);
            Log.d(str2, sb2.toString());
            this.mCurMode = selectedMode;
            updateSelectedItemColor(selectedMode, true);
            onModeSelectedListener onmodeselectedlistener = this.mModeSelectedListener;
            if (onmodeselectedlistener != null) {
                onmodeselectedlistener.onModeSelected(this.mCurMode, getItemText(getSelectPos()));
            }
        }
    }

    public void onScrolled(int i, int i2) {
        super.onScrolled(i, i2);
        if (this.mInit) {
            int selectedMode = getSelectedMode(this.mTempMode);
            if (isSameMode(this.mCurMode, selectedMode)) {
                updateSelectedItemColor(this.mCurMode, false);
            }
            if (!isSameMode(selectedMode, this.mTempMode)) {
                Log.d(TAG, "onScrolled");
                this.mTempMode = selectedMode;
                this.mIsModeChange = true;
                if (getScrollState() != 2) {
                    updateSelectedItemColor(selectedMode, true);
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (canScroll()) {
            return super.onTouchEvent(motionEvent);
        }
        Log.v(TAG, "onTouchEvent skip.");
        return false;
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        updateSelectedItemColor(this.mCurMode, false);
    }

    public void refresh() {
        this.mLayoutManager.scrollToPositionWithOffset(getPosition(this.mCurMode), getHeaderOffset());
    }

    public void setItems(List list) {
        List list2 = this.mItems;
        if (list2 != null) {
            list2.clear();
            this.mItems.addAll(list);
            return;
        }
        this.mItems = list;
    }

    public void setVisibility(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setVisibility: ");
        sb.append(Util.getCallers(3));
        Log.d(str, sb.toString());
        super.setVisibility(i);
    }
}

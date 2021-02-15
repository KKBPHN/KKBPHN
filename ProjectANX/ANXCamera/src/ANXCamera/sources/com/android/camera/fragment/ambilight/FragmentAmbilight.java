package com.android.camera.fragment.ambilight;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.FragmentAnimationFactory;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.config.ComponentConfigAmbilight;
import com.android.camera.fragment.BasePanelFragment;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.top.FragmentTopAlert;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AmbilightProtocol;
import com.android.camera.protocol.ModeProtocol.AmbilightSelector;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.protocol.ModeProtocol.TopAlertEvent;
import com.android.camera.ui.ScrollTextview;
import java.util.ArrayList;
import java.util.List;

public class FragmentAmbilight extends BasePanelFragment implements OnClickListener, AmbilightSelector, HandleBackTrace, TopAlertEvent {
    private static final int FRAGMENT_INFO = 16777200;
    private static final String TAG = "FragmentAmbilight";
    private ComponentConfigAmbilight mComponentConfigAmbilight;
    /* access modifiers changed from: private */
    public int mCurrentIndex;
    private TextView mDebugInfoTextView;
    private EffectItemPadding mEffectItemPadding;
    private boolean mIgnoreSameItemClick = true;
    private ImageView mIndicatorButton;
    private boolean mIsManuallyParentHiding;
    private boolean mIsShooting;
    private int mLastIndex = -1;
    private LinearLayoutManagerWrapper mLayoutManager;
    private int mMode;
    private LinearLayout mRootLayout;
    private RecyclerView mSceneModeList;
    private LinearLayout mSceneModeSelector;
    private SceneModeItemAdapter mSceneModesItemAdapter;

    class EffectItemPadding extends ItemDecoration {
        protected int mEffectListLeft;
        protected int mHorizontalPadding;
        protected int mVerticalPadding;

        public EffectItemPadding(Context context) {
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.vv_list_item_margin);
            this.mEffectListLeft = dimensionPixelSize;
            this.mHorizontalPadding = dimensionPixelSize;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            int i = recyclerView.getChildPosition(view) == 0 ? this.mEffectListLeft : 0;
            int i2 = this.mVerticalPadding;
            rect.set(i, i2, this.mHorizontalPadding, i2);
        }
    }

    public class SceneModeItemAdapter extends Adapter {
        LayoutInflater mLayoutInflater;
        ComponentConfigAmbilight mSceneModes;

        class AmbilightModeItemHolder extends ViewHolder {
            /* access modifiers changed from: private */
            public ImageView mImageView;
            /* access modifiers changed from: private */
            public ImageView mSelectedIndicator;
            protected ScrollTextview mTextView;

            public AmbilightModeItemHolder(View view) {
                super(view);
                this.mTextView = (ScrollTextview) view.findViewById(R.id.effect_item_text);
                this.mImageView = (ImageView) view.findViewById(R.id.effect_item_image);
                this.mSelectedIndicator = (ImageView) view.findViewById(R.id.effect_item_selected_indicator);
            }

            public void bindEffectIndex(int i, ComponentDataItem componentDataItem) {
                float f;
                ImageView imageView;
                this.mTextView.setText(componentDataItem.mDisplayNameRes);
                this.mImageView.setImageResource(componentDataItem.mIconRes);
                Util.correctionSelectView(this.mImageView, i == FragmentAmbilight.this.mCurrentIndex);
                FragmentAmbilight fragmentAmbilight = FragmentAmbilight.this;
                fragmentAmbilight.setAccessible(this.itemView, componentDataItem.mDisplayNameRes, i == fragmentAmbilight.mCurrentIndex);
                if (i == FragmentAmbilight.this.mCurrentIndex) {
                    this.itemView.setActivated(true);
                    this.mSelectedIndicator.setVisibility(0);
                    imageView = this.mSelectedIndicator;
                    f = 1.0f;
                } else {
                    this.itemView.setActivated(false);
                    this.mSelectedIndicator.setVisibility(8);
                    imageView = this.mSelectedIndicator;
                    f = 0.0f;
                }
                imageView.setAlpha(f);
            }
        }

        public SceneModeItemAdapter(Context context, ComponentConfigAmbilight componentConfigAmbilight) {
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mSceneModes = componentConfigAmbilight;
        }

        public int getItemCount() {
            return this.mSceneModes.getItems().size();
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            ComponentDataItem componentDataItem = (ComponentDataItem) this.mSceneModes.getItems().get(i);
            AmbilightModeItemHolder ambilightModeItemHolder = (AmbilightModeItemHolder) viewHolder;
            ambilightModeItemHolder.itemView.setTag(Integer.valueOf(i));
            ambilightModeItemHolder.bindEffectIndex(i, componentDataItem);
        }

        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull List list) {
            if (list.isEmpty()) {
                onBindViewHolder(viewHolder, i);
            } else if (viewHolder instanceof AmbilightModeItemHolder) {
                AmbilightModeItemHolder ambilightModeItemHolder = (AmbilightModeItemHolder) viewHolder;
                if (list.get(0) instanceof Boolean) {
                    boolean booleanValue = ((Boolean) list.get(0)).booleanValue();
                    ambilightModeItemHolder.itemView.setActivated(booleanValue);
                    Util.updateSelectIndicator(ambilightModeItemHolder.mSelectedIndicator, booleanValue, true);
                    Util.correctionSelectView(ambilightModeItemHolder.mImageView, booleanValue);
                }
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = this.mLayoutInflater.inflate(R.layout.ambilight_scenes_item, viewGroup, false);
            AmbilightModeItemHolder ambilightModeItemHolder = new AmbilightModeItemHolder(inflate);
            inflate.setOnClickListener(FragmentAmbilight.this);
            FolmeUtils.handleListItemTouch(inflate);
            return ambilightModeItemHolder;
        }

        public void updateData(ComponentConfigAmbilight componentConfigAmbilight) {
            this.mSceneModes = componentConfigAmbilight;
            notifyDataSetChanged();
        }
    }

    private void animateAlphaShow(View view, int i, int i2, float f) {
        ViewCompat.setAlpha(view, f);
        ViewCompat.animate(view).setStartDelay((long) i).setDuration((long) i2).alpha(1.0f).start();
    }

    private ArrayList getAmbilightInfo() {
        ArrayList arrayList = new ArrayList(5);
        AmbilightInfo ambilightInfo = new AmbilightInfo(0, R.string.ambilight_scene_crowd_moving, R.drawable.ambilight_scene_mode_crowd_moving, 0, 4);
        arrayList.add(ambilightInfo);
        AmbilightInfo ambilightInfo2 = new AmbilightInfo(1, R.string.ambilight_scene_traffic_light, R.drawable.ambilight_scene_mode_traffic_light, 1, 0);
        arrayList.add(ambilightInfo2);
        AmbilightInfo ambilightInfo3 = new AmbilightInfo(2, R.string.ambilight_scene_silky_water, R.drawable.ambilight_scene_mode_silky_water, 2, 1);
        arrayList.add(ambilightInfo3);
        AmbilightInfo ambilightInfo4 = new AmbilightInfo(3, R.string.ambilight_scene_light_track, R.drawable.ambilight_scene_mode_light_track, 3, 2);
        arrayList.add(ambilightInfo4);
        AmbilightInfo ambilightInfo5 = new AmbilightInfo(4, R.string.ambilight_scene_magic_star, R.drawable.ambilight_scene_mode_magic_star, 4, 5);
        arrayList.add(ambilightInfo5);
        AmbilightInfo ambilightInfo6 = new AmbilightInfo(5, R.string.ambilight_scene_star_track, R.drawable.ambilight_scene_mode_star_track, 5, 3);
        arrayList.add(ambilightInfo6);
        return arrayList;
    }

    private boolean hideAmbilightLayout(int i) {
        if (this.mIsManuallyParentHiding || 3 == i || getView() == null) {
            return false;
        }
        this.mIsManuallyParentHiding = true;
        FolmeUtils.animateDeparture(this.mSceneModeSelector, new O00000Oo(this, i));
        if (i != 4) {
            animateAlphaShow(this.mIndicatorButton, 150, 300, 0.0f);
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: onHideFinished */
    public void O0000Oo(int i) {
        this.mIsManuallyParentHiding = false;
        this.mSceneModeSelector.setVisibility(8);
        if (i == 4) {
            this.mRootLayout.setVisibility(8);
        } else {
            this.mIndicatorButton.setVisibility(0);
            updateTips();
        }
        LinearLayout linearLayout = this.mSceneModeSelector;
        if (linearLayout != null) {
            FolmeUtils.clean(linearLayout);
        }
    }

    private void onItemSelected(int i, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("onItemSelected: index = ");
        sb.append(i);
        sb.append(", fromClick = ");
        sb.append(z);
        sb.append(", mCurrentMode = ");
        sb.append(this.mCurrentMode);
        sb.append(", DataRepository.dataItemGlobal().getCurrentMode() = ");
        sb.append(DataRepository.dataItemGlobal().getCurrentMode());
        String sb2 = sb.toString();
        String str = TAG;
        Log.u(str, sb2);
        AmbilightProtocol ambilightProtocol = (AmbilightProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(420);
        if (ambilightProtocol == null) {
            Log.e(str, "onItemSelected: configChanges = null");
            return;
        }
        try {
            String str2 = ((ComponentDataItem) this.mComponentConfigAmbilight.getItems().get(i)).mValue;
            this.mComponentConfigAmbilight.setAmbilightModeValue(i);
            this.mMode = Integer.parseInt(str2);
            ambilightProtocol.onSceneModeSelect(this.mMode);
            updateTips();
            selectItem(i);
        } catch (NumberFormatException e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("invalid filter id: ");
            sb3.append(e.getMessage());
            Log.e(str, sb3.toString());
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onShowFinished */
    public void O000o0o() {
        this.mRootLayout.setVisibility(0);
        int ambilightModeIndex = DataRepository.dataItemRunning().getComponentConfigAmbilight().getAmbilightModeIndex();
        selectItem(ambilightModeIndex);
        onItemSelected(ambilightModeIndex, false);
        LinearLayout linearLayout = this.mSceneModeSelector;
        if (linearLayout != null) {
            FolmeUtils.clean(linearLayout);
        }
    }

    private void scrollIfNeed(int i) {
        if (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
            int i2 = this.mEffectItemPadding.mHorizontalPadding;
            View findViewByPosition = this.mLayoutManager.findViewByPosition(i);
            if (i > 0 && findViewByPosition != null) {
                i2 = (this.mEffectItemPadding.mHorizontalPadding * 2) + findViewByPosition.getWidth();
            }
            this.mLayoutManager.scrollToPositionWithOffset(Math.max(0, i), i2);
        } else if (i == this.mLayoutManager.findLastVisibleItemPosition() || i == this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.min(i + 1, this.mSceneModesItemAdapter.getItemCount() - 1));
        }
    }

    private void selectItem(int i) {
        if (i != -1) {
            int i2 = this.mCurrentIndex;
            if (i != i2) {
                this.mLastIndex = i2;
                this.mCurrentIndex = i;
                int i3 = this.mLastIndex;
                int i4 = R.string.lighting_pattern_null;
                if (i3 > -1) {
                    if (Util.isAccessible()) {
                        ComponentConfigAmbilight componentConfigAmbilight = this.mComponentConfigAmbilight;
                        if (componentConfigAmbilight != null) {
                            int i5 = ((ComponentDataItem) componentConfigAmbilight.getItems().get(this.mLastIndex)).mDisplayNameRes;
                            ViewHolder findViewHolderForAdapterPosition = this.mSceneModeList.findViewHolderForAdapterPosition(this.mLastIndex);
                            if (findViewHolderForAdapterPosition != null) {
                                View view = findViewHolderForAdapterPosition.itemView;
                                if (i5 <= 0) {
                                    i5 = R.string.lighting_pattern_null;
                                }
                                view.setContentDescription(getString(i5));
                            }
                        }
                    }
                    this.mSceneModesItemAdapter.notifyItemChanged(this.mLastIndex, Boolean.valueOf(false));
                }
                if (this.mCurrentIndex > -1) {
                    int i6 = ((ComponentDataItem) this.mComponentConfigAmbilight.getItems().get(this.mCurrentIndex)).mDisplayNameRes;
                    StringBuilder sb = new StringBuilder();
                    sb.append("selectItem ");
                    if (i6 > 0) {
                        i4 = i6;
                    }
                    sb.append(getString(i4));
                    Log.u(TAG, sb.toString());
                    if (Util.isAccessible() && this.mComponentConfigAmbilight != null) {
                        ViewHolder findViewHolderForAdapterPosition2 = this.mSceneModeList.findViewHolderForAdapterPosition(this.mCurrentIndex);
                        if (findViewHolderForAdapterPosition2 != null && isAdded()) {
                            setAccessible(findViewHolderForAdapterPosition2.itemView, i6, true);
                        }
                    }
                    this.mSceneModesItemAdapter.notifyItemChanged(this.mCurrentIndex, Boolean.valueOf(true));
                }
                scrollIfNeed(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setAccessible(View view, int i, boolean z) {
        if (view != null) {
            if (z) {
                StringBuilder sb = new StringBuilder();
                if (i <= 0) {
                    i = R.string.lighting_pattern_null;
                }
                sb.append(getString(i));
                sb.append(", ");
                sb.append(getString(R.string.accessibility_selected));
                view.setContentDescription(sb.toString());
                if (Util.isAccessible()) {
                    view.postDelayed(new O00000o0(this, view), 100);
                }
            } else {
                view.setContentDescription(getString(i));
            }
        }
    }

    private void showSelector() {
        Log.u(TAG, "onClick: showSelector");
        FolmeUtils.animateShow(this.mSceneModeSelector);
        updateTips();
        this.mIndicatorButton.setVisibility(8);
    }

    public /* synthetic */ void O0000OOo(View view) {
        if (isAdded()) {
            view.sendAccessibilityEvent(128);
        }
    }

    /* access modifiers changed from: protected */
    public int getAnimationType() {
        return 1;
    }

    public int getFragmentInto() {
        return 16777200;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_ambilight;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.bottomMargin = Display.getBottomHeight();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        this.mRootLayout = (LinearLayout) view.findViewById(R.id.ambilight_scenes_list_root);
        this.mSceneModeList = (RecyclerView) view.findViewById(R.id.ambilight_scenes_list);
        this.mSceneModeSelector = (LinearLayout) view.findViewById(R.id.ambilight_scenes_selector);
        this.mDebugInfoTextView = (TextView) view.findViewById(R.id.debug_info);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setChangeDuration(150);
        defaultItemAnimator.setMoveDuration(150);
        defaultItemAnimator.setAddDuration(150);
        this.mSceneModeList.setItemAnimator(defaultItemAnimator);
        this.mLayoutManager = new LinearLayoutManagerWrapper(getContext(), "ambilight");
        this.mLayoutManager.setOrientation(0);
        this.mSceneModeList.setLayoutManager(this.mLayoutManager);
        Context context = getContext();
        this.mComponentConfigAmbilight = DataRepository.dataItemRunning().getComponentConfigAmbilight();
        this.mComponentConfigAmbilight.mapToItems(getAmbilightInfo(), this.mCurrentMode);
        this.mSceneModesItemAdapter = new SceneModeItemAdapter(context, this.mComponentConfigAmbilight);
        this.mSceneModeList.setAdapter(this.mSceneModesItemAdapter);
        this.mSceneModeList.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
            }
        });
        if (this.mEffectItemPadding == null) {
            this.mEffectItemPadding = new EffectItemPadding(getContext());
            this.mSceneModeList.addItemDecoration(this.mEffectItemPadding);
        }
        this.mIndicatorButton = (ImageView) view.findViewById(R.id.ambilight_indicator);
        FolmeUtils.touchScaleTint(this.mIndicatorButton);
        this.mIndicatorButton.setOnClickListener(this);
        provideAnimateElement(this.mCurrentMode, null, 2);
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mCurrentMode != 187) {
            return;
        }
        if (this.mSceneModeSelector.getVisibility() == 0) {
            updateTips();
            return;
        }
        this.mIndicatorButton.setVisibility(8);
        FolmeUtils.animateShow(this.mSceneModeSelector, new O000000o(this));
    }

    public boolean onBackEvent(int i) {
        LinearLayout linearLayout = this.mSceneModeSelector;
        if (linearLayout == null || linearLayout.getVisibility() != 0 || i == 3 || this.mIsManuallyParentHiding) {
            return false;
        }
        if ((i == 2 || i == 1) && this.mIsShooting) {
            return false;
        }
        return hideAmbilightLayout(i);
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        if (this.mSceneModeSelector.isEnabled()) {
            if (view.getId() == R.id.ambilight_indicator) {
                showSelector();
                return;
            }
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction == null || !cameraAction.isDoingAction()) {
                int intValue = ((Integer) view.getTag()).intValue();
                if (this.mCurrentIndex != intValue || !this.mIgnoreSameItemClick) {
                    onItemSelected(intValue, true);
                    return;
                }
                if (Util.isAccessible() && isAdded()) {
                    view.sendAccessibilityEvent(32768);
                }
            }
        }
    }

    public void onRecommendDescDismiss() {
        if (this.mCurrentMode == 187) {
            updateTips();
        }
    }

    public void onRecordingPrepare() {
        this.mIsShooting = true;
        this.mRootLayout.setVisibility(4);
    }

    public void onRecordingStop() {
        this.mIsShooting = false;
        this.mIndicatorButton.setVisibility(8);
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (i != 187) {
            if (this.mRootLayout.getVisibility() == 0) {
                if (i2 != 2) {
                    this.mComponentConfigAmbilight.setAmbilightModeValue(0);
                }
                selectItem(0);
                this.mLayoutManager.scrollToPosition(0);
                hideAmbilightLayout(4);
            } else if (this.mSceneModeSelector.getVisibility() == 0) {
                this.mSceneModeSelector.setVisibility(4);
            }
            return;
        }
        O000o0o();
        this.mIndicatorButton.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(161);
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(162);
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(421, this);
        modeCoordinator.attachProtocol(673, this);
        registerBackStack(modeCoordinator, this);
    }

    public void setSelectorLayoutVisible(boolean z) {
        if (!z) {
            onBackEvent(5);
        } else if (this.mIndicatorButton.getVisibility() != 0) {
            this.mIndicatorButton.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(421, this);
        modeCoordinator.detachProtocol(673, this);
        unRegisterBackStack(modeCoordinator, this);
    }

    public void updateDebugInfo(String str) {
        if (this.mDebugInfoTextView.getVisibility() != 0) {
            this.mDebugInfoTextView.setVisibility(0);
        }
        this.mDebugInfoTextView.setText(str);
    }

    public void updateTips() {
        if (!this.mIsShooting) {
            TopAlert topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
            if (topAlert != null && !topAlert.getTipsState(FragmentTopAlert.TIP_SPEECH_SHUTTER_DESC)) {
                int i = this.mMode;
                if (i != 2) {
                    if (i == 3) {
                        topAlert.alertAiDetectTipHint(0, R.string.ambilight_tips_for_longtime_using_tripod, -1);
                    } else if (i != 5) {
                        topAlert.alertAiDetectTipHint(8, R.string.ambilight_tips_for_using_tripod, -1);
                    }
                }
                topAlert.alertAiDetectTipHint(0, R.string.ambilight_tips_for_using_tripod, -1);
            }
        }
    }
}

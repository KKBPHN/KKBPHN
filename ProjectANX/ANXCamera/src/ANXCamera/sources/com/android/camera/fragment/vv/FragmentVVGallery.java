package com.android.camera.fragment.vv;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMFeature;
import com.android.camera.data.observeable.VMFeature.FeatureModule;
import com.android.camera.data.observeable.VMResource;
import com.android.camera.data.observeable.VlogViewModel;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.BasePanelFragment;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.vv.FragmentVVFeature.FeatureInstallListener;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.LiveSubVVImpl;
import com.android.camera.multi.PluginInfo;
import com.android.camera.multi.PluginInfoRequest;
import com.android.camera.network.NetworkDependencies;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.LiveVVChooser;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.MultiFeatureManager;
import com.android.camera.resource.BaseResourceItem;
import com.android.camera.resource.BaseResourceList;
import com.android.camera.resource.BaseResourceRaw;
import com.android.camera.resource.SimpleCloudResourceListRequest;
import com.android.camera.statistic.CameraStatUtils;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class FragmentVVGallery extends BasePanelFragment implements OnClickListener, LiveVVChooser, HandleBackTrace, FeatureInstallListener {
    private static final String TAG = "VVGallery";
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private VVGalleryAdapter mGalleryAdapter;
    private int mHolderWidth;
    private LinearLayoutManagerWrapper mLayoutManager;
    private int mPreviewIndex = -1;
    private ProgressBar mProgressBar;
    private View mProgressLayout;
    private TextView mProgressText;
    private RecyclerView mRecyclerView;
    private View mRecyclerViewLayout;
    private ResourceSelectedListener mResourceSelectedListener;
    /* access modifiers changed from: private */
    public VVItem mSelectedItem;
    private int mTotalWidth;
    private VMFeature mVMFeature;
    private VMResource mVMResource;
    private VVList mVVList;

    public class EffectItemPadding extends ItemDecoration {
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

    static /* synthetic */ List O00000o(Throwable th) {
        return new ArrayList();
    }

    static /* synthetic */ BaseResourceRaw O00000oO(Throwable th) {
        if (th != null) {
            th.printStackTrace();
        }
        return new BaseResourceRaw();
    }

    static /* synthetic */ void O00000oo(Throwable th) {
        if (th != null) {
            th.printStackTrace();
            Log.e("PullNewError", th.getMessage());
        }
    }

    private void downloadItem(VVItem vVItem) {
        StringBuilder sb = new StringBuilder();
        sb.append("downloadItem :");
        sb.append(vVItem.id);
        Log.u(TAG, sb.toString());
        this.mVMResource.startAndGetDownloadDisposable(vVItem, getActivity());
    }

    /* access modifiers changed from: private */
    public void handleSnapButton(VVItem vVItem) {
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        int currentState = vVItem.getCurrentState();
        if (currentState == 5 || currentState == 7) {
            actionProcessing.updateResourceState(7);
        } else {
            actionProcessing.updateResourceState(0);
        }
    }

    private void initList() {
        BaseResourceItem baseResourceItem;
        ResourceSelectedListener resourceSelectedListener;
        if (isAdded()) {
            VVItem currentVVItem = DataRepository.dataItemLive().getCurrentVVItem();
            if (currentVVItem != null) {
                this.mPreviewIndex = currentVVItem.index;
            }
            DataRepository.dataItemLive().setVVVersion(this.mVVList.version);
            this.mResourceSelectedListener = new ResourceSelectedListener() {
                public void onResourceReady() {
                }

                public void onResourceSelected(VVItem vVItem) {
                    FragmentVVGallery.this.mSelectedItem = vVItem;
                    FragmentVVGallery.this.handleSnapButton(vVItem);
                }
            };
            this.mResourceSelectedListener.onResourceReady();
            int i = this.mPreviewIndex;
            if (i < 0 || i >= this.mVVList.getSize()) {
                resourceSelectedListener = this.mResourceSelectedListener;
                baseResourceItem = this.mVVList.getItem(0);
            } else {
                resourceSelectedListener = this.mResourceSelectedListener;
                baseResourceItem = this.mVVList.getItem(this.mPreviewIndex);
            }
            resourceSelectedListener.onResourceSelected((VVItem) baseResourceItem);
            this.mRecyclerViewLayout.setVisibility(0);
            this.mProgressLayout.setVisibility(8);
            this.mLayoutManager = new LinearLayoutManagerWrapper(getContext(), "vv_gallery");
            this.mLayoutManager.setOrientation(0);
            EffectItemPadding effectItemPadding = new EffectItemPadding(getContext());
            VVGalleryAdapter vVGalleryAdapter = new VVGalleryAdapter(this.mVVList, this.mLayoutManager, this.mPreviewIndex, this, this.mResourceSelectedListener, effectItemPadding);
            this.mGalleryAdapter = vVGalleryAdapter;
            this.mRecyclerView.setLayoutManager(this.mLayoutManager);
            this.mRecyclerView.addItemDecoration(effectItemPadding);
            this.mRecyclerView.setAdapter(this.mGalleryAdapter);
            int i2 = this.mPreviewIndex;
            if (i2 >= 0) {
                setItemInCenter(i2);
            }
            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
            defaultItemAnimator.setChangeDuration(150);
            defaultItemAnimator.setMoveDuration(150);
            defaultItemAnimator.setAddDuration(150);
            this.mRecyclerView.setItemAnimator(defaultItemAnimator);
            initResource();
            pullNewList();
        }
    }

    private void initResource() {
        if (!this.mVVList.stateAllReady() && this.mVMResource == null) {
            this.mVMResource = (VMResource) DataRepository.dataItemObservable().get(VMResource.class);
            this.mVMResource.startObservable(this, new O00000o(this));
        }
    }

    private void loadItemList() {
        this.mVVList = ((VlogViewModel) DataRepository.dataItemObservable().get(VlogViewModel.class)).getVVList();
        VVList vVList = this.mVVList;
        if (vVList == null || vVList.isDeparted()) {
            this.mRecyclerViewLayout.setVisibility(8);
            MultiFeatureManager multiFeatureManager = (MultiFeatureManager) ModeCoordinatorImpl.getInstance().getAttachProtocol(929);
            String str = FeatureModule.MODULE_VLOG2;
            if (!multiFeatureManager.hasFeatureInstalled(str)) {
                FragmentVVFeature fragmentVVFeature = new FragmentVVFeature();
                fragmentVVFeature.setFeatureName(str);
                if (this.mVMFeature == null) {
                    this.mVMFeature = (VMFeature) DataRepository.dataItemObservable().get(VMFeature.class);
                    this.mVMFeature.startObservable(this, new C0334O0000Ooo(this));
                }
                FragmentUtils.addFragmentWithTag(getChildFragmentManager(), (int) R.id.vv_feature_holder, (Fragment) fragmentVVFeature, FragmentVVFeature.TAG);
                return;
            }
            onInstalled();
            return;
        }
        initList();
    }

    private void onInstallStateChanged(HashMap hashMap) {
        if (isAdded()) {
            for (Entry entry : hashMap.entrySet()) {
                if (((String) entry.getKey()).equals(FeatureModule.MODULE_VLOG2)) {
                    int intValue = ((Integer) entry.getValue()).intValue();
                    if (VMFeature.getScope(intValue) == 16) {
                        if (!(intValue == 17 || intValue == 19 || intValue == 21)) {
                            if (intValue == 22) {
                                onInstalled();
                            }
                        }
                    }
                }
            }
        }
    }

    private void onInstalled() {
        this.mProgressLayout.setVisibility(0);
        this.mProgressText.setText(R.string.live_sticker_updating);
        this.mDisposable.add(((VlogViewModel) DataRepository.dataItemObservable().get(VlogViewModel.class)).getVVListObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new O0000OOo(this), O0000O0o.INSTANCE));
    }

    private void onResourceDownloadStateChanged(HashMap hashMap) {
        if (isAdded()) {
            for (Entry key : hashMap.entrySet()) {
                String str = (String) key.getKey();
                if (str.startsWith(VVItem.ID_PREFIX)) {
                    if (this.mGalleryAdapter != null) {
                        int i = 0;
                        while (true) {
                            if (i >= this.mGalleryAdapter.getItemCount()) {
                                break;
                            } else if (this.mGalleryAdapter.getItemAt(i).id.equals(str)) {
                                this.mGalleryAdapter.notifyItemChanged(i);
                                break;
                            } else {
                                i++;
                            }
                        }
                    }
                    VVItem vVItem = this.mSelectedItem;
                    if (vVItem != null && str.equals(vVItem.id)) {
                        handleSnapButton(this.mSelectedItem);
                    }
                }
            }
        }
    }

    private void pullNewList() {
        if (NetworkDependencies.isConnected(getContext())) {
            StringBuilder sb = new StringBuilder();
            sb.append(LiveSubVVImpl.TEMPLATE_PATH);
            sb.append("vv/info.json");
            String sb2 = sb.toString();
            File file = new File(sb2);
            if (!file.exists() || System.currentTimeMillis() - file.lastModified() >= 86400000) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(LiveSubVVImpl.TEMPLATE_PATH);
                sb3.append(VVItem.ID_PREFIX);
                File file2 = new File(sb3.toString());
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                this.mDisposable.add(new SimpleCloudResourceListRequest("14689473395228832").startObservable((Object) new ArrayList()).onErrorReturn(C0332O00000oo.INSTANCE).zipWith((ObservableSource) new PluginInfoRequest("14689487697936512").startObservable(PluginInfo.class).flatMap(C0333O0000OoO.INSTANCE).onErrorReturn(O0000Oo0.INSTANCE), (BiFunction) new VVListBiFunction(sb2)).subscribe(new O0000Oo(this), C0331O00000oO.INSTANCE));
            }
        }
    }

    private void setItemInCenter(int i) {
        this.mLayoutManager.scrollToPositionWithOffset(i, (this.mTotalWidth / 2) - (this.mHolderWidth / 2));
    }

    private void transformToPreview(int i, View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("transformToPreview index=");
        sb.append(i);
        Log.u(TAG, sb.toString());
        FragmentVVPreview fragmentVVPreview = new FragmentVVPreview();
        fragmentVVPreview.setPreviewData(i, this.mVVList);
        fragmentVVPreview.setResourceSelectedListener(this.mResourceSelectedListener);
        fragmentVVPreview.registerProtocol();
        getFragmentManager().beginTransaction().addSharedElement(view, ViewCompat.getTransitionName(view)).add(R.id.bottom_beauty, fragmentVVPreview, fragmentVVPreview.getFragmentTag()).hide(this).commitAllowingStateLoss();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.updateCurrentFragments(R.id.bottom_beauty, fragmentVVPreview.getFragmentInto(), 4);
        }
    }

    public /* synthetic */ void O000000o(DataWrap dataWrap) {
        onResourceDownloadStateChanged((HashMap) dataWrap.get());
    }

    public /* synthetic */ void O000000o(VVList vVList) {
        this.mVVList = vVList;
        initList();
    }

    public /* synthetic */ void O00000Oo(DataWrap dataWrap) {
        onInstallStateChanged((HashMap) dataWrap.get());
    }

    public /* synthetic */ void O00000Oo(VVList vVList) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(vVList.version);
        Log.e("PullNewOk:", sb.toString());
        this.mVVList.compareAndMarkDeparted((BaseResourceList) vVList);
    }

    /* access modifiers changed from: protected */
    public int getAnimationType() {
        return 1;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_VV_GALLERY;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_vv_gallery;
    }

    public void hide() {
        FragmentUtils.removeFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_VV_GALLERY));
        FragmentUtils.removeFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_VV_PREVIEW));
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Util.alignPopupBottom(view);
        this.mProgressLayout = view.findViewById(R.id.vv_updating);
        this.mProgressText = (TextView) this.mProgressLayout.findViewById(R.id.vv_updating_text);
        this.mProgressBar = (ProgressBar) this.mProgressLayout.findViewById(R.id.vv_updating_progress);
        this.mRecyclerViewLayout = view.findViewById(R.id.vv_list_layout);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.vv_list);
        this.mRecyclerView.setFocusable(false);
        Context context = getContext();
        this.mTotalWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mHolderWidth = context.getResources().getDimensionPixelSize(R.dimen.vv_list_item_image_width);
        loadItemList();
    }

    public boolean isPreviewShow() {
        FragmentVVPreview fragmentVVPreview = (FragmentVVPreview) FragmentUtils.getFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_VV_PREVIEW));
        return fragmentVVPreview != null && fragmentVVPreview.isVisible();
    }

    public boolean isShow() {
        return isAdded() && getView().getVisibility() == 0;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
    }

    public boolean onBackEvent(int i) {
        boolean isVisible = isVisible();
        return false;
    }

    public void onClick(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        StringBuilder sb = new StringBuilder();
        sb.append("onClick: index=");
        sb.append(intValue);
        Log.u(TAG, sb.toString());
        VVItem vVItem = (VVItem) this.mVVList.getItem(intValue);
        int currentState = vVItem.getCurrentState();
        if (currentState == 0) {
            downloadItem(vVItem);
        } else if (currentState == 7) {
            transformToPreview(intValue, view.findViewById(R.id.vv_gallery_item_image));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mDisposable.clear();
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            int i = this.mPreviewIndex;
            if (i != -1) {
                VVGalleryAdapter vVGalleryAdapter = this.mGalleryAdapter;
                if (vVGalleryAdapter != null) {
                    vVGalleryAdapter.onSelected(i, null, false);
                    setItemInCenter(this.mPreviewIndex);
                    this.mPreviewIndex = -1;
                }
            }
        }
    }

    public void onInstalled(String str) {
        if (str.equals(FeatureModule.MODULE_VLOG2)) {
            onInstalled();
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (i != 209) {
            hide();
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(229, this);
        registerBackStack(modeCoordinator, this);
    }

    public void setPreviewData(int i) {
        this.mPreviewIndex = i;
    }

    public void show(int i) {
    }

    public boolean startShot() {
        if (this.mSelectedItem == null) {
            return false;
        }
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            return false;
        }
        if (this.mSelectedItem.isCloudItem() && this.mSelectedItem.getCurrentState() != 7) {
            return false;
        }
        CameraStatUtils.trackVVStartClick(this.mSelectedItem.name, isPreviewShow());
        configChanges.configLiveVV(this.mSelectedItem, null, true, false);
        return true;
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(229, this);
        unRegisterBackStack(modeCoordinator, this);
    }
}

package com.android.camera.fragment.vv;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMResource;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.BaseFragmentPagerAdapter;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.log.Log;
import com.android.camera.module.AudioController;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class FragmentVVPreview extends BaseFragment implements OnClickListener, HandleBackTrace {
    private static final String TAG = "VVPreview";
    private AudioController mAudioController;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private int mOldControlStream = -1;
    /* access modifiers changed from: private */
    public BaseFragmentPagerAdapter mPreviewAdapter;
    private int mPreviewIndex;
    private ViewGroup mPreviewLayout;
    /* access modifiers changed from: private */
    public ViewPager mPreviewViewPager;
    /* access modifiers changed from: private */
    public ResourceSelectedListener mResourceSelectedListener;
    private VMResource mVMResource;
    /* access modifiers changed from: private */
    public VVList mVVList;

    private void downloadItem(VVItem vVItem) {
        this.mVMResource.startAndGetDownloadDisposable(vVItem, getActivity());
    }

    private void initResource() {
        if (!this.mVVList.stateAllReady() && this.mVMResource == null) {
            this.mVMResource = (VMResource) DataRepository.dataItemObservable().get(VMResource.class);
            this.mVMResource.startObservable(this, new O0000o00(this));
        }
    }

    private void initViewPager() {
        LayoutParams layoutParams = this.mPreviewViewPager.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.vv_preview_image_height);
        int i = (int) ((((float) dimensionPixelSize) / 9.0f) * 16.0f);
        layoutParams.width = i;
        final int windowWidth = Display.getWindowWidth();
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.vv_preview_page_margin);
        final int i2 = (windowWidth - i) / 2;
        this.mPreviewViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("onPageSelected position=");
                sb.append(i);
                Log.u(FragmentVVPreview.TAG, sb.toString());
                if (FragmentVVPreview.this.mResourceSelectedListener != null) {
                    FragmentVVPreview.this.mResourceSelectedListener.onResourceSelected((VVItem) FragmentVVPreview.this.mVVList.getItem(i));
                }
            }
        });
        this.mPreviewLayout.setOnTouchListener(new OnTouchListener() {
            int nextItem = -1;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int i;
                int action = motionEvent.getAction();
                if (action != 0) {
                    if (action != 1 || FragmentVVPreview.this.mPreviewViewPager == null || FragmentVVPreview.this.mPreviewAdapter == null) {
                        return false;
                    }
                    if (this.nextItem >= 0) {
                        FragmentVVPreview.this.mPreviewViewPager.setCurrentItem(this.nextItem);
                        this.nextItem = -1;
                    }
                    return true;
                } else if (FragmentVVPreview.this.mPreviewViewPager == null || FragmentVVPreview.this.mPreviewAdapter == null) {
                    return false;
                } else {
                    float x = motionEvent.getX();
                    int i2 = i2;
                    if (x < ((float) i2)) {
                        int currentItem = FragmentVVPreview.this.mPreviewViewPager.getCurrentItem();
                        if (currentItem > 0) {
                            i = currentItem - 1;
                        }
                        return true;
                    } else if (x < ((float) (windowWidth - i2))) {
                        return false;
                    } else {
                        int currentItem2 = FragmentVVPreview.this.mPreviewViewPager.getCurrentItem();
                        if (currentItem2 < FragmentVVPreview.this.mPreviewAdapter.getCount() - 1) {
                            i = currentItem2 + 1;
                        }
                        return true;
                    }
                    this.nextItem = i;
                    return true;
                }
            }
        });
        this.mPreviewViewPager.setPageMargin(dimensionPixelSize2);
        this.mPreviewViewPager.setPageTransformer(true, new VVPreviewTransformer());
        ArrayList arrayList = new ArrayList(this.mVVList.getSize());
        for (int i3 = 0; i3 < this.mVVList.getSize(); i3++) {
            FragmentVVPreviewItem fragmentVVPreviewItem = new FragmentVVPreviewItem();
            fragmentVVPreviewItem.setData(i3, (VVItem) this.mVVList.getItem(i3), i, dimensionPixelSize, this, this.mPreviewIndex);
            arrayList.add(fragmentVVPreviewItem);
        }
        this.mPreviewAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), arrayList);
        this.mPreviewViewPager.setAdapter(this.mPreviewAdapter);
        this.mPreviewViewPager.setOffscreenPageLimit(2);
        this.mPreviewViewPager.setCurrentItem(this.mPreviewIndex, false);
    }

    private void onResourceDownloadStateChanged(HashMap hashMap) {
        if (isAdded()) {
            for (Entry key : hashMap.entrySet()) {
                String str = (String) key.getKey();
                if (str.startsWith(VVItem.ID_PREFIX)) {
                    BaseFragmentPagerAdapter baseFragmentPagerAdapter = this.mPreviewAdapter;
                    if (baseFragmentPagerAdapter != null) {
                        for (FragmentVVPreviewItem fragmentVVPreviewItem : baseFragmentPagerAdapter.getFragmentList()) {
                            if (fragmentVVPreviewItem.getVVItem().id.equals(str)) {
                                fragmentVVPreviewItem.handleDownloadStateChanged(false);
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private void restoreOuterAudio() {
        if (this.mAudioController == null) {
            this.mAudioController = new AudioController(getActivity().getApplicationContext());
        }
        this.mAudioController.restoreAudio();
        if (this.mOldControlStream != -1) {
            getActivity().setVolumeControlStream(this.mOldControlStream);
        }
    }

    private void silenceOuterAudio() {
        if (this.mAudioController == null) {
            this.mAudioController = new AudioController(getActivity().getApplicationContext());
        }
        this.mAudioController.silenceAudio();
        this.mOldControlStream = getActivity().getVolumeControlStream();
        getActivity().setVolumeControlStream(3);
    }

    private void transformToGallery(int i, View view) {
        FragmentTransaction fragmentTransaction;
        StringBuilder sb = new StringBuilder();
        sb.append("transformToGallery index=");
        sb.append(i);
        Log.u(TAG, sb.toString());
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        if (view != null) {
            beginTransaction.addSharedElement(view, ViewCompat.getTransitionName(view));
        }
        FragmentVVGallery fragmentVVGallery = (FragmentVVGallery) FragmentUtils.getFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_VV_GALLERY));
        if (fragmentVVGallery == null) {
            fragmentVVGallery = new FragmentVVGallery();
            fragmentVVGallery.registerProtocol();
            fragmentVVGallery.setPreviewData(this.mPreviewViewPager.getCurrentItem());
            fragmentTransaction = beginTransaction.replace(R.id.bottom_beauty, fragmentVVGallery, fragmentVVGallery.getFragmentTag());
        } else {
            fragmentVVGallery.setPreviewData(this.mPreviewViewPager.getCurrentItem());
            fragmentTransaction = beginTransaction.remove(this).show(fragmentVVGallery);
        }
        fragmentTransaction.commitAllowingStateLoss();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.updateCurrentFragments(R.id.bottom_beauty, fragmentVVGallery.getFragmentInto(), 5);
        }
    }

    public /* synthetic */ void O0000Oo0(DataWrap dataWrap) {
        onResourceDownloadStateChanged((HashMap) dataWrap.get());
    }

    public void controlPlay(boolean z) {
        FragmentVVPreviewItem fragmentVVPreviewItem = (FragmentVVPreviewItem) this.mPreviewAdapter.getItem(this.mPreviewViewPager.getCurrentItem());
        if (z) {
            fragmentVVPreviewItem.startPlay();
        } else {
            fragmentVVPreviewItem.stopPlay();
        }
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_VV_PREVIEW;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_vv_preview;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Util.alignPopupBottom(view);
        this.mPreviewLayout = (ViewGroup) view.findViewById(R.id.vv_preview_layout);
        this.mPreviewViewPager = (ViewPager) view.findViewById(R.id.vv_viewpager);
        initResource();
        initViewPager();
    }

    public boolean onBackEvent(int i) {
        if (i == 2) {
            return false;
        }
        transformToGallery(0, null);
        return true;
    }

    public void onClick(View view) {
        int id = view.getId();
        String str = TAG;
        if (id == R.id.vv_preview_item_collapsing) {
            Log.u(str, "onClick: vv_preview_item_collapsing");
            transformToGallery(((Integer) view.getTag()).intValue(), ((ViewGroup) view.getParent()).findViewById(R.id.vv_preview_item_image));
        } else if (id == R.id.vv_preview_item_image) {
            Log.u(str, "onClick: vv_preview_item_image");
            VVItem vVItem = (VVItem) this.mVVList.getItem(((Integer) view.getTag()).intValue());
            int currentState = vVItem.getCurrentState();
            if (currentState == 0) {
                downloadItem(vVItem);
            } else if (currentState != 5) {
            }
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(17760257));
        setSharedElementReturnTransition(null);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mDisposable.clear();
    }

    public void onPause() {
        super.onPause();
        restoreOuterAudio();
    }

    public void onResume() {
        super.onResume();
        silenceOuterAudio();
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
    }

    public void setPreviewData(int i, VVList vVList) {
        this.mPreviewIndex = i;
        this.mVVList = vVList;
    }

    public void setResourceSelectedListener(ResourceSelectedListener resourceSelectedListener) {
        this.mResourceSelectedListener = resourceSelectedListener;
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
    }
}

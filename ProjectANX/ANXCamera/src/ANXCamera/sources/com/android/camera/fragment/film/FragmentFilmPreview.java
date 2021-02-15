package com.android.camera.fragment.film;

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
import java.util.ArrayList;

public class FragmentFilmPreview extends BaseFragment implements OnClickListener, HandleBackTrace {
    private AudioController mAudioController;
    /* access modifiers changed from: private */
    public FilmList mFilmList;
    private int mOldControlStream = -1;
    /* access modifiers changed from: private */
    public BaseFragmentPagerAdapter mPreviewAdapter;
    private int mPreviewIndex;
    private ViewGroup mPreviewLayout;
    /* access modifiers changed from: private */
    public ViewPager mPreviewViewPager;
    /* access modifiers changed from: private */
    public FilmResourceSelectedListener mResourceSelectedListener;

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
                if (FragmentFilmPreview.this.mResourceSelectedListener != null) {
                    FragmentFilmPreview.this.mResourceSelectedListener.onResourceSelected((FilmItem) FragmentFilmPreview.this.mFilmList.getItem(i));
                }
            }
        });
        this.mPreviewLayout.setOnTouchListener(new OnTouchListener() {
            int nextItem = -1;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int i;
                int action = motionEvent.getAction();
                if (action != 0) {
                    if (action != 1 || FragmentFilmPreview.this.mPreviewViewPager == null || FragmentFilmPreview.this.mPreviewAdapter == null) {
                        return false;
                    }
                    if (this.nextItem >= 0) {
                        FragmentFilmPreview.this.mPreviewViewPager.setCurrentItem(this.nextItem);
                        this.nextItem = -1;
                    }
                    return true;
                } else if (FragmentFilmPreview.this.mPreviewViewPager == null || FragmentFilmPreview.this.mPreviewAdapter == null) {
                    return false;
                } else {
                    float x = motionEvent.getX();
                    int i2 = i2;
                    if (x < ((float) i2)) {
                        int currentItem = FragmentFilmPreview.this.mPreviewViewPager.getCurrentItem();
                        if (currentItem > 0) {
                            i = currentItem - 1;
                        }
                        return true;
                    } else if (x < ((float) (windowWidth - i2))) {
                        return false;
                    } else {
                        int currentItem2 = FragmentFilmPreview.this.mPreviewViewPager.getCurrentItem();
                        if (currentItem2 < FragmentFilmPreview.this.mPreviewAdapter.getCount() - 1) {
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
        this.mPreviewViewPager.setPageTransformer(true, new FilmPreviewTransformer());
        ArrayList arrayList = new ArrayList(this.mFilmList.getSize());
        for (int i3 = 0; i3 < this.mFilmList.getSize(); i3++) {
            FragmentFilmPreviewItem fragmentFilmPreviewItem = new FragmentFilmPreviewItem();
            fragmentFilmPreviewItem.setData(i3, (FilmItem) this.mFilmList.getItem(i3), i, dimensionPixelSize, this, this.mPreviewIndex);
            arrayList.add(fragmentFilmPreviewItem);
        }
        this.mPreviewAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), arrayList);
        this.mPreviewViewPager.setAdapter(this.mPreviewAdapter);
        this.mPreviewViewPager.setOffscreenPageLimit(2);
        this.mPreviewViewPager.setCurrentItem(this.mPreviewIndex, false);
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
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        if (view != null) {
            beginTransaction.addSharedElement(view, ViewCompat.getTransitionName(view));
        }
        FragmentFilmGallery fragmentFilmGallery = (FragmentFilmGallery) FragmentUtils.getFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_FILM_GALLERY));
        if (fragmentFilmGallery == null) {
            fragmentFilmGallery = new FragmentFilmGallery();
            fragmentFilmGallery.registerProtocol();
            fragmentFilmGallery.setPreviewData(this.mPreviewViewPager.getCurrentItem());
            fragmentTransaction = beginTransaction.replace(R.id.bottom_beauty, fragmentFilmGallery, fragmentFilmGallery.getFragmentTag());
        } else {
            fragmentFilmGallery.setPreviewData(this.mPreviewViewPager.getCurrentItem());
            fragmentTransaction = beginTransaction.remove(this).show(fragmentFilmGallery);
        }
        fragmentTransaction.commitAllowingStateLoss();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.updateCurrentFragments(R.id.bottom_beauty, fragmentFilmGallery.getFragmentInto(), 5);
        }
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_FILM_PREVIEW;
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
        if (view.getId() == R.id.vv_preview_item_collapsing) {
            Log.u("TAG", "vv_preview_item_collapsing");
            transformToGallery(((Integer) view.getTag()).intValue(), ((ViewGroup) view.getParent()).findViewById(R.id.vv_preview_item_image));
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(17760257));
        setSharedElementReturnTransition(null);
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

    public void setPreviewData(int i, FilmList filmList) {
        this.mPreviewIndex = i;
        this.mFilmList = filmList;
    }

    public void setResourceSelectedListener(FilmResourceSelectedListener filmResourceSelectedListener) {
        this.mResourceSelectedListener = filmResourceSelectedListener;
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
    }
}

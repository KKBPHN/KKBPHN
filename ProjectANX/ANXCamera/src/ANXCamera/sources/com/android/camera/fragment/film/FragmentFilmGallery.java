package com.android.camera.fragment.film;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FragmentAnimationFactory;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.FilmViewModel;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.LiveFilmChooser;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.resource.BaseResourceItem;
import com.android.camera.statistic.CameraStatUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class FragmentFilmGallery extends BaseFragment implements OnClickListener, LiveFilmChooser, HandleBackTrace {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private FilmList mFilmList;
    private FilmGalleryAdapter mGalleryAdapter;
    private int mHolderWidth;
    private LinearLayoutManagerWrapper mLayoutManager;
    private int mPreviewIndex = -1;
    private View mProgressView;
    private RecyclerView mRecyclerView;
    private View mRecyclerViewLayout;
    private FilmResourceSelectedListener mResourceSelectedListener;
    /* access modifiers changed from: private */
    public FilmItem mSelectedItem;
    private int mTotalWidth;

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

    private void initList() {
        BaseResourceItem baseResourceItem;
        FilmResourceSelectedListener filmResourceSelectedListener;
        if (isAdded()) {
            FilmItem currentFilmItem = DataRepository.dataItemLive().getCurrentFilmItem();
            if (currentFilmItem != null) {
                this.mPreviewIndex = currentFilmItem.index;
            }
            this.mResourceSelectedListener.onResourceReady();
            int i = this.mPreviewIndex;
            if (i < 0 || i >= this.mFilmList.getSize()) {
                filmResourceSelectedListener = this.mResourceSelectedListener;
                baseResourceItem = this.mFilmList.getItem(0);
            } else {
                filmResourceSelectedListener = this.mResourceSelectedListener;
                baseResourceItem = this.mFilmList.getItem(this.mPreviewIndex);
            }
            filmResourceSelectedListener.onResourceSelected((FilmItem) baseResourceItem);
            this.mRecyclerViewLayout.setVisibility(0);
            this.mProgressView.setVisibility(8);
            this.mLayoutManager = new LinearLayoutManagerWrapper(getContext(), "film_gallery");
            this.mLayoutManager.setOrientation(0);
            EffectItemPadding effectItemPadding = new EffectItemPadding(getContext());
            FilmGalleryAdapter filmGalleryAdapter = new FilmGalleryAdapter(this.mFilmList, this.mLayoutManager, this.mPreviewIndex, this, this.mResourceSelectedListener, effectItemPadding);
            this.mGalleryAdapter = filmGalleryAdapter;
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
        }
    }

    private void loadItemList() {
        this.mResourceSelectedListener = new FilmResourceSelectedListener() {
            public void onResourceReady() {
            }

            public void onResourceSelected(FilmItem filmItem) {
                FragmentFilmGallery.this.mSelectedItem = filmItem;
            }
        };
        this.mFilmList = ((FilmViewModel) DataRepository.dataItemObservable().get(FilmViewModel.class)).getFilmList();
        if (this.mFilmList != null) {
            initList();
            return;
        }
        this.mRecyclerViewLayout.setVisibility(8);
        this.mProgressView.setVisibility(0);
        this.mDisposable.add(((FilmViewModel) DataRepository.dataItemObservable().get(FilmViewModel.class)).getFilmListObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C0304O00000oO(this), O00000o.INSTANCE));
    }

    private void setItemInCenter(int i) {
        this.mLayoutManager.scrollToPositionWithOffset(i, (this.mTotalWidth / 2) - (this.mHolderWidth / 2));
    }

    private void transformToPreview(int i, View view) {
        FragmentFilmPreview fragmentFilmPreview = new FragmentFilmPreview();
        fragmentFilmPreview.setPreviewData(i, this.mFilmList);
        fragmentFilmPreview.setResourceSelectedListener(this.mResourceSelectedListener);
        fragmentFilmPreview.registerProtocol();
        getFragmentManager().beginTransaction().addSharedElement(view, ViewCompat.getTransitionName(view)).add(R.id.bottom_beauty, fragmentFilmPreview, fragmentFilmPreview.getFragmentTag()).hide(this).commitAllowingStateLoss();
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.updateCurrentFragments(R.id.bottom_beauty, fragmentFilmPreview.getFragmentInto(), 4);
        }
    }

    public /* synthetic */ void O000000o(FilmList filmList) {
        this.mFilmList = filmList;
        initList();
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_FILM_GALLERY;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_vv_gallery;
    }

    public void hide() {
        FragmentUtils.removeFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_FILM_GALLERY));
        FragmentUtils.removeFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_FILM_PREVIEW));
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Util.alignPopupBottom(view);
        this.mProgressView = view.findViewById(R.id.vv_updating);
        this.mRecyclerViewLayout = view.findViewById(R.id.vv_list_layout);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mRecyclerViewLayout.getLayoutParams();
        marginLayoutParams.setMarginStart(Display.getStartMargin());
        marginLayoutParams.setMarginEnd(Display.getEndMargin());
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.vv_list);
        this.mRecyclerView.setFocusable(false);
        Context context = getContext();
        this.mTotalWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mHolderWidth = context.getResources().getDimensionPixelSize(R.dimen.vv_list_item_image_width);
        loadItemList();
    }

    public boolean isPreviewShow() {
        FragmentFilmPreview fragmentFilmPreview = (FragmentFilmPreview) FragmentUtils.getFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_FILM_PREVIEW));
        return fragmentFilmPreview != null && fragmentFilmPreview.isVisible();
    }

    public boolean isShow() {
        return isAdded() && getView().getVisibility() == 0;
    }

    public boolean onBackEvent(int i) {
        boolean isVisible = isVisible();
        return false;
    }

    public void onClick(View view) {
        transformToPreview(((Integer) view.getTag()).intValue(), view.findViewById(R.id.vv_gallery_item_image));
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            int i = this.mPreviewIndex;
            if (i != -1) {
                FilmGalleryAdapter filmGalleryAdapter = this.mGalleryAdapter;
                if (filmGalleryAdapter != null) {
                    filmGalleryAdapter.onSelected(i, null, false);
                    setItemInCenter(this.mPreviewIndex);
                    this.mPreviewIndex = -1;
                }
            }
        }
    }

    public void onStop() {
        super.onStop();
        this.mDisposable.clear();
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (i != 211) {
            hide();
        }
        if (i != 212 && i != 207 && i != 208 && i != 213) {
            DataRepository.dataItemLive().setCurrentFilmItem(null);
        }
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(161);
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(675, this);
        registerBackStack(modeCoordinator, this);
    }

    public void setPreviewData(int i) {
        this.mPreviewIndex = i;
    }

    public void show(int i) {
    }

    public void startShot() {
        if (this.mSelectedItem != null) {
            ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
            if (configChanges != null) {
                CameraStatUtils.trackFilmStartClick(this.mSelectedItem.id, isPreviewShow());
                configChanges.configFilm(this.mSelectedItem, true, false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(675, this);
        unRegisterBackStack(modeCoordinator, this);
    }
}

package com.android.camera.fragment;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.fragment.clone.VideoRecyclerViewAdapter;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.videoplayer.manager.PlayerItemChangeListener;
import com.android.camera.videoplayer.manager.SingleVideoPlayerManager;
import com.android.camera.videoplayer.manager.VideoPlayerManager;
import com.android.camera.videoplayer.meta.MetaData;
import com.android.camera.visibilityutils.calculator.DefaultSingleItemCalculatorCallback;
import com.android.camera.visibilityutils.calculator.ListItemsVisibilityCalculator;
import com.android.camera.visibilityutils.calculator.SingleListViewItemActiveCalculator;
import com.android.camera.visibilityutils.scroll_utils.ItemsPositionGetter;
import com.android.camera.visibilityutils.scroll_utils.RecyclerViewItemPositionGetter;
import java.util.ArrayList;
import java.util.List;

public class BaseFragmentUseGuide extends BaseFragment implements HandleBackTrace {
    public static final String TAG = "BaseFragmentUseGuide";
    private ImageView mBackButton;
    /* access modifiers changed from: private */
    public ItemsPositionGetter mItemsPositionGetter;
    /* access modifiers changed from: private */
    public LinearLayoutManager mLayoutManager;
    /* access modifiers changed from: private */
    public final List mList = new ArrayList();
    private RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public int mScrollState = 0;
    protected final VideoPlayerManager mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        public void onPlayerItemChanged(MetaData metaData) {
        }
    });
    /* access modifiers changed from: private */
    public final ListItemsVisibilityCalculator mVideoVisibilityCalculator = new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), this.mList);

    private void initAdapter() {
        fillList(this.mList);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        this.mLayoutManager.setOrientation(1);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.setAdapter(new VideoRecyclerViewAdapter(this.mVideoPlayerManager, getActivity(), this.mList));
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                BaseFragmentUseGuide.this.mScrollState = i;
                if (i == 0 && !BaseFragmentUseGuide.this.mList.isEmpty()) {
                    BaseFragmentUseGuide.this.mVideoVisibilityCalculator.onScrollStateIdle(BaseFragmentUseGuide.this.mItemsPositionGetter, BaseFragmentUseGuide.this.mLayoutManager.findFirstVisibleItemPosition(), BaseFragmentUseGuide.this.mLayoutManager.findLastVisibleItemPosition());
                }
            }

            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                if (!BaseFragmentUseGuide.this.mList.isEmpty()) {
                    BaseFragmentUseGuide.this.mVideoVisibilityCalculator.onScroll(BaseFragmentUseGuide.this.mItemsPositionGetter, BaseFragmentUseGuide.this.mLayoutManager.findFirstVisibleItemPosition(), (BaseFragmentUseGuide.this.mLayoutManager.findLastVisibleItemPosition() - BaseFragmentUseGuide.this.mLayoutManager.findFirstVisibleItemPosition()) + 1, BaseFragmentUseGuide.this.mScrollState);
                }
            }
        });
        this.mItemsPositionGetter = new RecyclerViewItemPositionGetter(this.mLayoutManager, this.mRecyclerView);
        new PagerSnapHelper().attachToRecyclerView(this.mRecyclerView);
    }

    public /* synthetic */ void O0000O0o(View view) {
        Log.u(TAG, "onClick BackButton");
        onBackEvent(6);
    }

    public /* synthetic */ void O000o0O0() {
        this.mVideoVisibilityCalculator.onScrollStateIdle(this.mItemsPositionGetter, this.mLayoutManager.findFirstVisibleItemPosition(), this.mLayoutManager.findLastVisibleItemPosition());
        if (Util.isAccessible()) {
            ViewHolder findViewHolderForAdapterPosition = this.mRecyclerView.findViewHolderForAdapterPosition(0);
            if (findViewHolderForAdapterPosition != null && isAdded()) {
                findViewHolderForAdapterPosition.itemView.sendAccessibilityEvent(128);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void fillList(List list) {
    }

    public int getFragmentInto() {
        return 240;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_base_use_guide;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Log.d(TAG, "initView");
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.clone_guide_recyclerView);
        this.mBackButton = (ImageView) view.findViewById(R.id.clone_guide_back);
        this.mBackButton.setRotation(90.0f);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mBackButton.getLayoutParams();
        marginLayoutParams.topMargin = Display.getTopMargin();
        marginLayoutParams.height = Display.getTopBarHeight();
        this.mBackButton.setOnClickListener(new O00000o0(this));
        initAdapter();
    }

    public boolean onBackEvent(int i) {
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        this.mVideoPlayerManager.terminate();
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!this.mList.isEmpty()) {
            this.mRecyclerView.post(new O00000Oo(this));
        }
    }

    public void onStart() {
        super.onStart();
        this.mVideoPlayerManager.resumeMediaPlayer();
    }

    public void onStop() {
        super.onStop();
        this.mVideoPlayerManager.resetMediaPlayer();
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
    }
}

package com.android.camera.fragment.mode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.mode.ModeAdapter.ModeViewHolder;
import com.android.camera.fragment.mode.MoreModeListAnimation.OnSpringUpdateListener;
import com.android.camera.ui.InnerSpringImageView;
import io.reactivex.Completable;
import java.lang.ref.WeakReference;
import miuix.springback.view.SpringBackLayout;
import miuix.springback.view.SpringBackLayout.OnScrollListener;

public class FragmentMoreModeTabV2 implements IMoreMode, OnAttachStateChangeListener {
    private static final long FATE_TIME = 1500;
    private static final int MSG_FADE_IN = 100;
    private static final int MSG_FADE_OUT = 101;
    private static final String TAG = "MoreModeTabV2";
    private float mBarMinHeight;
    private float mMarginOffset;
    /* access modifiers changed from: private */
    public float mOverScrollY;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public View mScrollbar;
    private FrameLayout mScrollbarTrack;
    private float mTrackHeight;
    /* access modifiers changed from: private */
    public float mTranX;
    /* access modifiers changed from: private */
    public float mTranY;
    private UIHandler mUIHandler;

    interface ForEachCallback {
        void error();

        void run(ModeViewHolder modeViewHolder);
    }

    class UIHandler extends Handler {
        private WeakReference mBarTrack;

        public UIHandler(@NonNull Looper looper, View view) {
            super(looper);
            this.mBarTrack = new WeakReference(view);
        }

        static /* synthetic */ void O000OOOo() {
        }

        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 100) {
                View view = (View) this.mBarTrack.get();
                if (view != null && view.getVisibility() != 0) {
                    FolmeAlphaInOnSubscribe.directSetResult(view);
                }
            } else if (i == 101) {
                View view2 = (View) this.mBarTrack.get();
                if (view2 != null && view2.getVisibility() == 0 && view2.getAlpha() == 1.0f) {
                    Completable.create(new FolmeAlphaOutOnSubscribe(view2).setOnAnimationEnd(O00000o0.INSTANCE)).subscribe();
                }
            }
        }
    }

    static /* synthetic */ float access$016(FragmentMoreModeTabV2 fragmentMoreModeTabV2, float f) {
        float f2 = fragmentMoreModeTabV2.mOverScrollY + f;
        fragmentMoreModeTabV2.mOverScrollY = f2;
        return f2;
    }

    /* access modifiers changed from: private */
    public void cancelFadeOut() {
        this.mUIHandler.removeMessages(101);
    }

    /* access modifiers changed from: private */
    public void changeBarPosition() {
        int computeVerticalScrollRange = this.mRecyclerView.computeVerticalScrollRange();
        int computeVerticalScrollExtent = this.mRecyclerView.computeVerticalScrollExtent();
        int i = computeVerticalScrollRange - computeVerticalScrollExtent;
        this.mScrollbar.setY(((((float) ((int) (((float) (computeVerticalScrollExtent - this.mScrollbar.getHeight())) - this.mMarginOffset))) * 1.0f) / ((float) i)) * ((float) this.mRecyclerView.computeVerticalScrollOffset()));
    }

    /* access modifiers changed from: private */
    public void doForEach(RecyclerView recyclerView, ForEachCallback forEachCallback) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        if (gridLayoutManager == null) {
            forEachCallback.error();
            return;
        }
        int findLastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
        for (int findFirstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
            ModeViewHolder modeViewHolder = (ModeViewHolder) recyclerView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
            if (modeViewHolder != null && (modeViewHolder.mIconView instanceof InnerSpringImageView)) {
                forEachCallback.run(modeViewHolder);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateScrollbar() {
        if (this.mScrollbar != null && this.mRecyclerView.computeVerticalScrollRange() != 0) {
            int computeVerticalScrollRange = this.mRecyclerView.computeVerticalScrollRange();
            float max = Math.max(this.mBarMinHeight, this.mTrackHeight * (((float) this.mRecyclerView.computeVerticalScrollExtent()) / ((float) computeVerticalScrollRange)));
            LayoutParams layoutParams = (LayoutParams) this.mScrollbar.getLayoutParams();
            layoutParams.height = (int) max;
            this.mScrollbar.setLayoutParams(layoutParams);
        }
    }

    public /* synthetic */ boolean O00000oo(View view, MotionEvent motionEvent) {
        cancelFadeOut();
        fadeScrollbar(true);
        return false;
    }

    public LayoutManager createLayoutManager(Context context) {
        AnonymousClass4 r0 = new GridLayoutManager(context, getCountPerLine(), 1, false) {
            public /* synthetic */ void O00O0Oo0() {
                FragmentMoreModeTabV2.this.updateScrollbar();
                FragmentMoreModeTabV2.this.fadeScrollbar(true);
            }

            public void onLayoutCompleted(State state) {
                super.onLayoutCompleted(state);
                FragmentMoreModeTabV2.this.mScrollbar.post(new O00000Oo(this));
            }
        };
        return r0;
    }

    public ModeItemDecoration createModeItemDecoration(Context context, IMoreMode iMoreMode) {
        return new ModeItemDecoration(context, iMoreMode, getType());
    }

    public void fadeScrollbar(boolean z) {
        if (this.mScrollbar != null) {
            if (z) {
                this.mUIHandler.removeCallbacksAndMessages(null);
                this.mUIHandler.sendEmptyMessage(100);
                this.mUIHandler.sendEmptyMessageDelayed(101, FATE_TIME);
            } else {
                this.mUIHandler.removeCallbacksAndMessages(null);
                this.mUIHandler.sendEmptyMessage(101);
            }
        }
    }

    public int getCountPerLine() {
        return Display.getMoreModeTabCol(DataRepository.dataItemRunning().getUiStyle(), true);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public RecyclerView getModeList(View view) {
        if (this.mRecyclerView == null) {
            this.mRecyclerView = (RecyclerView) view.findViewById(R.id.modes_content_new);
            ((SpringBackLayout) view.findViewById(R.id.spring_layout)).addOnScrollListener(new OnScrollListener() {
                public void onScrolled(SpringBackLayout springBackLayout, int i, int i2) {
                    FragmentMoreModeTabV2.access$016(FragmentMoreModeTabV2.this, (float) i2);
                    MoreModeListAnimation.getInstance().startInnerSpringAnim();
                }

                public void onStateChanged(int i, int i2, boolean z) {
                    if (i2 == 0) {
                        FragmentMoreModeTabV2.this.mOverScrollY = 0.0f;
                    }
                }
            });
            this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                    super.onScrollStateChanged(recyclerView, i);
                }

                public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                    super.onScrolled(recyclerView, i, i2);
                    FragmentMoreModeTabV2.this.cancelFadeOut();
                    FragmentMoreModeTabV2.this.fadeScrollbar(true);
                    FragmentMoreModeTabV2.this.changeBarPosition();
                }
            });
            MoreModeListAnimation.getInstance().initSpring(new OnSpringUpdateListener() {
                public boolean canScrollDown() {
                    return FragmentMoreModeTabV2.this.mRecyclerView.canScrollVertically(1);
                }

                public boolean canScrollUp() {
                    return FragmentMoreModeTabV2.this.mRecyclerView.canScrollVertically(-1);
                }

                public float getOverScrollX() {
                    return 0.0f;
                }

                public float getOverScrollY() {
                    return FragmentMoreModeTabV2.this.mOverScrollY;
                }

                public float getRotate() {
                    Adapter adapter = FragmentMoreModeTabV2.this.mRecyclerView.getAdapter();
                    if (adapter instanceof ModeAdapter) {
                        return ((ModeAdapter) adapter).getRotate();
                    }
                    return 0.0f;
                }

                public void onUpdate(float f, float f2) {
                    if (FragmentMoreModeTabV2.this.mTranX != f || FragmentMoreModeTabV2.this.mTranY != f2) {
                        FragmentMoreModeTabV2.this.mTranX = f;
                        FragmentMoreModeTabV2.this.mTranY = f2;
                        FragmentMoreModeTabV2 fragmentMoreModeTabV2 = FragmentMoreModeTabV2.this;
                        fragmentMoreModeTabV2.doForEach(fragmentMoreModeTabV2.mRecyclerView, new ForEachCallback() {
                            public void error() {
                                FragmentMoreModeTabV2.this.mTranX = 0.0f;
                                FragmentMoreModeTabV2.this.mTranY = 0.0f;
                            }

                            public void run(ModeViewHolder modeViewHolder) {
                                ((InnerSpringImageView) modeViewHolder.mIconView).updateXY(FragmentMoreModeTabV2.this.mTranX, FragmentMoreModeTabV2.this.mTranY);
                            }
                        });
                    }
                }
            });
            this.mRecyclerView.setOnTouchListener(new O00000o(this));
            this.mRecyclerView.addOnAttachStateChangeListener(this);
            this.mScrollbarTrack = (FrameLayout) view.findViewById(R.id.more_mode_scrollbar_track);
            this.mScrollbar = view.findViewById(R.id.more_mode_scrollbar);
            this.mUIHandler = new UIHandler(Looper.getMainLooper(), this.mScrollbarTrack);
            this.mBarMinHeight = view.getResources().getDimension(R.dimen.more_mode_scrollbar_height);
            this.mMarginOffset = view.getResources().getDimension(R.dimen.mode_item_margin_header) + view.getResources().getDimension(R.dimen.mode_item_margin_new);
            this.mTrackHeight = ((float) Util.getDisplayRect(0).height()) - this.mMarginOffset;
        }
        return this.mRecyclerView;
    }

    public int getType() {
        return 3;
    }

    public boolean modeShouldDownload(int i) {
        return false;
    }

    public void onViewAttachedToWindow(View view) {
        MoreModeListAnimation.getInstance().startInnerEnterAnim();
    }

    public void onViewDetachedFromWindow(View view) {
        MoreModeListAnimation.getInstance().clearSpring();
        this.mRecyclerView.removeOnAttachStateChangeListener(this);
    }
}

package com.miui.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.miui.internal.R;
import com.miui.internal.app.ActionBarImpl;
import com.miui.internal.util.DeviceHelper;
import com.miui.internal.util.FolmeAnimHelper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import miui.util.ActionBarUtils;
import miui.util.AttributeResolver;
import miui.view.ActionModeAnimationListener;
import miui.view.ViewPager;
import miui.view.inputmethod.InputMethodHelper;

public class SearchActionModeView extends FrameLayout implements AnimatorListener, ActionModeView, TextWatcher, OnClickListener, IdleHandler {
    /* access modifiers changed from: private */
    public ActionBarContainer mActionBarContainer;
    /* access modifiers changed from: private */
    public int mActionBarHeight;
    /* access modifiers changed from: private */
    public int mActionBarLocation = Integer.MAX_VALUE;
    private int mActionBarTopMargin;
    private ActionBarView mActionBarView;
    /* access modifiers changed from: private */
    public WeakReference mAnchorView;
    /* access modifiers changed from: private */
    public boolean mAnimateToVisible;
    /* access modifiers changed from: private */
    public WeakReference mAnimateView;
    /* access modifiers changed from: private */
    public int mAnimateViewTranslationYLength;
    /* access modifiers changed from: private */
    public int mAnimateViewTranslationYStart;
    private boolean mAnimationCanceled;
    private List mAnimationListeners;
    private float mAnimationProgress;
    /* access modifiers changed from: private */
    public int mContentOriginPaddingBottom;
    /* access modifiers changed from: private */
    public int mContentOriginPaddingTop;
    private ObjectAnimator mCurrentAnimation;
    /* access modifiers changed from: private */
    public View mDimView;
    /* access modifiers changed from: private */
    public int mInputPaddingRight;
    /* access modifiers changed from: private */
    public int mInputPaddingTop;
    /* access modifiers changed from: private */
    public EditText mInputView;
    /* access modifiers changed from: private */
    public int mInputViewTranslationYLength;
    /* access modifiers changed from: private */
    public int mInputViewTranslationYStart;
    /* access modifiers changed from: private */
    public int[] mLocation = new int[2];
    /* access modifiers changed from: private */
    public int mOriginalPaddingTop;
    private boolean mRequestAnimation;
    private WeakReference mResultView;
    private int mResultViewOriginMarginBottom;
    private int mResultViewOriginMarginTop;
    private boolean mResultViewSet;
    /* access modifiers changed from: private */
    public SearchActionModeViewHelper mSearchActionModeViewHelper;
    private ActionBarContainer mSplitActionBarContainer;
    /* access modifiers changed from: private */
    public int mStatusBarPaddingTop;
    private int mTextLengthBeforeChanged;

    class ActionBarAnimationProcessor implements ActionModeAnimationListener {
        ActionBarAnimationProcessor() {
        }

        public void onStart(boolean z) {
        }

        public void onStop(boolean z) {
            if (z) {
                View tabContainer = SearchActionModeView.this.getActionBarContainer().getTabContainer();
                if (tabContainer != null) {
                    tabContainer.setVisibility(8);
                }
            }
        }

        public void onUpdate(boolean z, float f) {
        }
    }

    class ContentViewAnimationProcessor implements ActionModeAnimationListener {
        boolean mDimViewVisible;
        ViewPager mViewPager;

        ContentViewAnimationProcessor() {
        }

        public void onStart(boolean z) {
            View access$800 = SearchActionModeView.this.getContentView();
            if (access$800 != null && z) {
                SearchActionModeView.this.mContentOriginPaddingTop = access$800.getPaddingTop();
                SearchActionModeView.this.mContentOriginPaddingBottom = access$800.getPaddingBottom();
            }
            boolean z2 = true;
            SearchActionModeView searchActionModeView = SearchActionModeView.this;
            if (z) {
                if (searchActionModeView.mActionBarLocation == Integer.MAX_VALUE) {
                    SearchActionModeView.this.getActionBarContainer().getLocationInWindow(SearchActionModeView.this.mLocation);
                    SearchActionModeView searchActionModeView2 = SearchActionModeView.this;
                    searchActionModeView2.mActionBarLocation = searchActionModeView2.mLocation[1];
                }
                SearchActionModeView searchActionModeView3 = SearchActionModeView.this;
                searchActionModeView3.mInputViewTranslationYStart = searchActionModeView3.getActionBarContainer().getHeight();
                SearchActionModeView searchActionModeView4 = SearchActionModeView.this;
                searchActionModeView4.mInputViewTranslationYLength = -searchActionModeView4.mInputViewTranslationYStart;
                ((View) SearchActionModeView.this.mAnchorView.get()).getLocationInWindow(SearchActionModeView.this.mLocation);
                SearchActionModeView searchActionModeView5 = SearchActionModeView.this;
                searchActionModeView5.mAnimateViewTranslationYStart = (searchActionModeView5.mLocation[1] - SearchActionModeView.this.mActionBarLocation) - SearchActionModeView.this.mContentOriginPaddingTop;
                SearchActionModeView searchActionModeView6 = SearchActionModeView.this;
                searchActionModeView6.mAnimateViewTranslationYLength = searchActionModeView6.mInputViewTranslationYLength;
            } else if (searchActionModeView.mActionBarContainer == null || !SearchActionModeView.this.mActionBarContainer.isBlurEnable()) {
                SearchActionModeView searchActionModeView7 = SearchActionModeView.this;
                searchActionModeView7.setContentViewTranslation(searchActionModeView7.mStatusBarPaddingTop);
                SearchActionModeView.this.setContentViewPadding(0, 0);
            } else {
                SearchActionModeView searchActionModeView8 = SearchActionModeView.this;
                searchActionModeView8.setContentViewTranslation(this.mDimViewVisible ? searchActionModeView8.mStatusBarPaddingTop : -searchActionModeView8.mContentOriginPaddingTop);
            }
            if (SearchActionModeView.this.mDimView == null || SearchActionModeView.this.mDimView.getVisibility() != 0) {
                z2 = false;
            }
            this.mDimViewVisible = z2;
            this.mViewPager = SearchActionModeView.this.getViewPager();
            ViewPager viewPager = this.mViewPager;
            if (viewPager != null) {
                viewPager.setBottomMarginProgress(z ? 0.0f : 1.0f);
            }
        }

        public void onStop(boolean z) {
            float f = 1.0f;
            if (!z) {
                ((View) SearchActionModeView.this.mAnimateView.get()).setTranslationY(0.0f);
                ((View) SearchActionModeView.this.mAnchorView.get()).setAlpha(1.0f);
            }
            if (SearchActionModeView.this.mAnchorView.get() != null) {
                ((View) SearchActionModeView.this.mAnchorView.get()).setEnabled(!z);
            }
            if (SearchActionModeView.this.mStatusBarPaddingTop > 0) {
                SearchActionModeView.this.setContentViewTranslation(0);
                SearchActionModeView searchActionModeView = SearchActionModeView.this;
                searchActionModeView.setContentViewPadding(z ? searchActionModeView.mStatusBarPaddingTop : 0, 0);
            }
            if (z && SearchActionModeView.this.mActionBarContainer != null && SearchActionModeView.this.mActionBarContainer.isBlurEnable()) {
                SearchActionModeView searchActionModeView2 = SearchActionModeView.this;
                searchActionModeView2.setContentViewTranslation(-searchActionModeView2.mContentOriginPaddingTop);
            }
            ViewPager viewPager = this.mViewPager;
            if (viewPager != null) {
                if (!z) {
                    f = 0.0f;
                }
                viewPager.setBottomMarginProgress(f);
            }
            this.mViewPager = null;
        }

        public void onUpdate(boolean z, float f) {
            View view;
            int i;
            if (!z) {
                f = 1.0f - f;
            }
            if (SearchActionModeView.this.mActionBarContainer == null || !SearchActionModeView.this.mActionBarContainer.isBlurEnable()) {
                if (SearchActionModeView.this.mStatusBarPaddingTop > 0) {
                    SearchActionModeView searchActionModeView = SearchActionModeView.this;
                    searchActionModeView.setContentViewTranslation((int) (((float) searchActionModeView.mStatusBarPaddingTop) * f));
                    view = (View) SearchActionModeView.this.mAnimateView.get();
                    i = SearchActionModeView.this.mAnimateViewTranslationYStart;
                }
                ((View) SearchActionModeView.this.mAnchorView.get()).setAlpha(1.0f - f);
                SearchActionModeView searchActionModeView2 = SearchActionModeView.this;
                searchActionModeView2.setTranslationY(((float) searchActionModeView2.mInputViewTranslationYStart) + (f * ((float) SearchActionModeView.this.mInputViewTranslationYLength)));
            }
            int access$300 = this.mDimViewVisible ? -SearchActionModeView.this.mStatusBarPaddingTop : (-SearchActionModeView.this.mContentOriginPaddingTop) - SearchActionModeView.this.mStatusBarPaddingTop;
            if (z) {
                access$300 = SearchActionModeView.this.mStatusBarPaddingTop;
            }
            SearchActionModeView searchActionModeView3 = SearchActionModeView.this;
            int i2 = 0;
            searchActionModeView3.setContentViewTranslation((searchActionModeView3.mStatusBarPaddingTop > 0 ? (int) (((float) SearchActionModeView.this.mStatusBarPaddingTop) * f) : 0) + access$300);
            if (!this.mDimViewVisible) {
                i2 = (-SearchActionModeView.this.mStatusBarPaddingTop) - SearchActionModeView.this.mContentOriginPaddingTop;
            }
            if (z) {
                i2 = -SearchActionModeView.this.mStatusBarPaddingTop;
            }
            view = (View) SearchActionModeView.this.mAnimateView.get();
            i = SearchActionModeView.this.mAnimateViewTranslationYStart + i2;
            view.setTranslationY(((float) i) + (((float) SearchActionModeView.this.mAnimateViewTranslationYLength) * f));
            ((View) SearchActionModeView.this.mAnchorView.get()).setAlpha(1.0f - f);
            SearchActionModeView searchActionModeView22 = SearchActionModeView.this;
            searchActionModeView22.setTranslationY(((float) searchActionModeView22.mInputViewTranslationYStart) + (f * ((float) SearchActionModeView.this.mInputViewTranslationYLength)));
        }
    }

    class DimViewAnimationProcessor implements ActionModeAnimationListener {
        DimViewAnimationProcessor() {
        }

        public void onStart(boolean z) {
            if (z) {
                SearchActionModeView.this.mDimView.setOnClickListener(SearchActionModeView.this);
                SearchActionModeView.this.mDimView.setVisibility(0);
                SearchActionModeView.this.mDimView.setAlpha(0.0f);
            }
        }

        public void onStop(boolean z) {
            if (!z) {
                SearchActionModeView.this.mDimView.setVisibility(8);
                SearchActionModeView.this.mDimView.setAlpha(1.0f);
                SearchActionModeView.this.mDimView.setTranslationY(0.0f);
            } else if (SearchActionModeView.this.mInputView.getText().length() > 0) {
                SearchActionModeView.this.mDimView.setVisibility(8);
            }
        }

        public void onUpdate(boolean z, float f) {
            if (!z) {
                f = 1.0f - f;
            }
            SearchActionModeView.this.mDimView.setAlpha(f);
            if (SearchActionModeView.this.shouldAnimateContent()) {
                View access$1900 = SearchActionModeView.this.mDimView;
                float translationY = ((View) SearchActionModeView.this.mAnimateView.get()).getTranslationY();
                int access$900 = (SearchActionModeView.this.mActionBarContainer == null || !SearchActionModeView.this.mActionBarContainer.isBlurEnable()) ? 0 : SearchActionModeView.this.mContentOriginPaddingTop;
                access$1900.setTranslationY(translationY + ((float) access$900));
            }
        }
    }

    class SearchViewAnimationProcessor implements ActionModeAnimationListener {
        SearchViewAnimationProcessor() {
        }

        public void onStart(boolean z) {
            if (z) {
                SearchActionModeView.this.mInputView.getText().clear();
                SearchActionModeView.this.mInputView.addTextChangedListener(SearchActionModeView.this);
                SearchActionModeView.this.mSearchActionModeViewHelper.translationCancelView();
            }
        }

        public void onStop(boolean z) {
            if (!z) {
                SearchActionModeView.this.mInputView.removeTextChangedListener(SearchActionModeView.this);
            }
        }

        public void onUpdate(boolean z, float f) {
            if (!z) {
                f = 1.0f - f;
            }
            if (SearchActionModeView.this.mStatusBarPaddingTop > 0) {
                SearchActionModeView searchActionModeView = SearchActionModeView.this;
                searchActionModeView.setPadding(searchActionModeView.getPaddingLeft(), (int) (((float) SearchActionModeView.this.mOriginalPaddingTop) + (((float) SearchActionModeView.this.mStatusBarPaddingTop) * f) + (((float) SearchActionModeView.this.mInputPaddingTop) * f)), SearchActionModeView.this.getPaddingRight(), SearchActionModeView.this.getPaddingBottom());
                SearchActionModeView.this.getLayoutParams().height = SearchActionModeView.this.mActionBarHeight + ((int) (((float) SearchActionModeView.this.mStatusBarPaddingTop) * f)) + ((int) (((float) SearchActionModeView.this.mInputPaddingTop) * f));
                SearchActionModeView.this.requestLayout();
            }
            SearchActionModeView.this.mSearchActionModeViewHelper.updateCancelView(f, SearchActionModeView.this.mInputPaddingRight);
        }
    }

    class SplitActionBarAnimationProcessor implements ActionModeAnimationListener {
        SplitActionBarAnimationProcessor() {
        }

        public void onStart(boolean z) {
        }

        public void onStop(boolean z) {
        }

        public void onUpdate(boolean z, float f) {
            if (!z) {
                f = 1.0f - f;
            }
            ActionBarContainer splitActionBarContainer = SearchActionModeView.this.getSplitActionBarContainer();
            if (splitActionBarContainer != null) {
                splitActionBarContainer.setTranslationY(f * ((float) splitActionBarContainer.getHeight()));
            }
        }
    }

    public SearchActionModeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setAlpha(0.0f);
        this.mActionBarHeight = AttributeResolver.resolveDimensionPixelSize(context, 16843499);
        this.mSearchActionModeViewHelper = new SearchActionModeViewHelper();
        this.mInputPaddingTop = this.mSearchActionModeViewHelper.getAnimatedPaddingTop(context);
        this.mInputPaddingRight = this.mSearchActionModeViewHelper.getAnimatedPaddingHorizontal(context);
        FolmeAnimHelper.addPressAnim(this);
    }

    /* access modifiers changed from: private */
    public View getContentView() {
        ViewGroup actionBarOverlayLayout = ActionBarUtils.getActionBarOverlayLayout(this);
        if (actionBarOverlayLayout != null) {
            return actionBarOverlayLayout.findViewById(16908290);
        }
        return null;
    }

    private MessageQueue getMessageQueue() {
        return Looper.myQueue();
    }

    private void queueIdleHandler() {
        removeIdleHandler();
        getMessageQueue().addIdleHandler(this);
    }

    private void removeIdleHandler() {
        getMessageQueue().removeIdleHandler(this);
    }

    /* access modifiers changed from: private */
    public boolean shouldAnimateContent() {
        return (this.mAnchorView == null || this.mAnimateView == null) ? false : true;
    }

    public void addAnimationListener(ActionModeAnimationListener actionModeAnimationListener) {
        if (actionModeAnimationListener != null) {
            if (this.mAnimationListeners == null) {
                this.mAnimationListeners = new ArrayList();
            }
            this.mAnimationListeners.add(actionModeAnimationListener);
        }
    }

    public void afterTextChanged(Editable editable) {
        if ((editable == null ? 0 : editable.length()) == 0) {
            View view = this.mDimView;
            if (view != null) {
                view.setVisibility(0);
            }
            InputMethodHelper.getInstance().showKeyBoard(this.mInputView);
        } else if (this.mTextLengthBeforeChanged == 0) {
            View view2 = this.mDimView;
            if (view2 != null) {
                view2.setVisibility(8);
            }
        }
    }

    public void animateToVisibility(boolean z) {
        if (this.mAnimateToVisible == z) {
            this.mRequestAnimation = false;
            return;
        }
        pollViews();
        this.mAnimateToVisible = z;
        this.mCurrentAnimation = makeAnimation();
        createAnimationListeners();
        if (z) {
            setOverlayMode(true);
        }
        notifyAnimationStart(z);
        if (shouldAnimateContent()) {
            requestLayout();
            this.mRequestAnimation = true;
        } else {
            this.mCurrentAnimation.start();
        }
        if (!this.mAnimateToVisible) {
            ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mInputView.getWindowToken(), 0);
        }
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.mTextLengthBeforeChanged = charSequence == null ? 0 : charSequence.length();
    }

    public void closeMode() {
        ObjectAnimator objectAnimator = this.mCurrentAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    /* access modifiers changed from: protected */
    public void createAnimationListeners() {
        if (this.mAnimationListeners == null) {
            this.mAnimationListeners = new ArrayList();
        }
        this.mAnimationListeners.add(new SearchViewAnimationProcessor());
        if (shouldAnimateContent()) {
            this.mAnimationListeners.add(new ContentViewAnimationProcessor());
            this.mAnimationListeners.add(new ActionBarAnimationProcessor());
            this.mAnimationListeners.add(new SplitActionBarAnimationProcessor());
        }
        if (getDimView() != null) {
            this.mAnimationListeners.add(new DimViewAnimationProcessor());
        }
    }

    /* access modifiers changed from: protected */
    public void finishAnimation() {
        ObjectAnimator objectAnimator = this.mCurrentAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.mCurrentAnimation = null;
        }
    }

    /* access modifiers changed from: protected */
    public ActionBarContainer getActionBarContainer() {
        if (this.mActionBarContainer == null) {
            ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) ActionBarUtils.getActionBarOverlayLayout(this);
            if (actionBarOverlayLayout != null) {
                int i = 0;
                while (true) {
                    if (i >= actionBarOverlayLayout.getChildCount()) {
                        break;
                    }
                    View childAt = actionBarOverlayLayout.getChildAt(i);
                    if (childAt.getId() == R.id.action_bar_container && (childAt instanceof ActionBarContainer)) {
                        this.mActionBarContainer = (ActionBarContainer) childAt;
                        break;
                    }
                    i++;
                }
            }
            ActionBarContainer actionBarContainer = this.mActionBarContainer;
            if (actionBarContainer != null) {
                this.mActionBarTopMargin = ((MarginLayoutParams) actionBarContainer.getLayoutParams()).topMargin;
                if (this.mActionBarTopMargin > 0) {
                    setPadding(getPaddingLeft(), this.mOriginalPaddingTop + this.mActionBarTopMargin, getPaddingRight(), getPaddingBottom());
                }
            }
        }
        return this.mActionBarContainer;
    }

    /* access modifiers changed from: protected */
    public ActionBarView getActionBarView() {
        if (this.mActionBarView == null) {
            ViewGroup actionBarOverlayLayout = ActionBarUtils.getActionBarOverlayLayout(this);
            if (actionBarOverlayLayout != null) {
                this.mActionBarView = (ActionBarView) actionBarOverlayLayout.findViewById(R.id.action_bar);
            }
        }
        return this.mActionBarView;
    }

    public float getAnimationProgress() {
        return this.mAnimationProgress;
    }

    /* access modifiers changed from: protected */
    public View getDimView() {
        if (this.mDimView == null) {
            ViewGroup actionBarOverlayLayout = ActionBarUtils.getActionBarOverlayLayout(this);
            if (actionBarOverlayLayout != null) {
                ViewStub viewStub = (ViewStub) actionBarOverlayLayout.findViewById(R.id.search_mask_vs);
                this.mDimView = viewStub != null ? viewStub.inflate() : actionBarOverlayLayout.findViewById(R.id.search_mask);
            }
        }
        return this.mDimView;
    }

    public EditText getSearchInput() {
        return this.mInputView;
    }

    /* access modifiers changed from: protected */
    public ActionBarContainer getSplitActionBarContainer() {
        if (this.mSplitActionBarContainer == null) {
            ViewGroup actionBarOverlayLayout = ActionBarUtils.getActionBarOverlayLayout(this);
            if (actionBarOverlayLayout != null) {
                int i = 0;
                while (true) {
                    if (i >= actionBarOverlayLayout.getChildCount()) {
                        break;
                    }
                    View childAt = actionBarOverlayLayout.getChildAt(i);
                    if (childAt.getId() == R.id.split_action_bar && (childAt instanceof ActionBarContainer)) {
                        this.mSplitActionBarContainer = (ActionBarContainer) childAt;
                        break;
                    }
                    i++;
                }
            }
        }
        return this.mSplitActionBarContainer;
    }

    /* access modifiers changed from: protected */
    public ViewPager getViewPager() {
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) ActionBarUtils.getActionBarOverlayLayout(this);
        if (((ActionBarImpl) actionBarOverlayLayout.getActionBar()).isFragmentViewPagerMode()) {
            return (ViewPager) actionBarOverlayLayout.findViewById(R.id.view_pager);
        }
        return null;
    }

    public void initForMode(ActionMode actionMode) {
    }

    public void killMode() {
        finishAnimation();
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null) {
            viewGroup.removeView(this);
        }
        this.mActionBarContainer = null;
        this.mActionBarView = null;
        List list = this.mAnimationListeners;
        if (list != null) {
            list.clear();
            this.mAnimationListeners = null;
        }
        this.mSplitActionBarContainer = null;
    }

    /* access modifiers changed from: protected */
    public ObjectAnimator makeAnimation() {
        ObjectAnimator objectAnimator = this.mCurrentAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.mCurrentAnimation = null;
            removeIdleHandler();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "AnimationProgress", new float[]{0.0f, 1.0f});
        ofFloat.addListener(this);
        ofFloat.setDuration(DeviceHelper.FEATURE_WHOLE_ANIM ? 400 : 0);
        ofFloat.setInterpolator(this.mSearchActionModeViewHelper.obtainInterpolator());
        return ofFloat;
    }

    public void notifyAnimationEnd(boolean z) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list != null) {
            for (ActionModeAnimationListener onStop : list) {
                onStop.onStop(z);
            }
        }
    }

    public void notifyAnimationStart(boolean z) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list != null) {
            for (ActionModeAnimationListener onStart : list) {
                onStart.onStart(z);
            }
        }
    }

    public void notifyAnimationUpdate(boolean z, float f) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list != null) {
            for (ActionModeAnimationListener onUpdate : list) {
                onUpdate.onUpdate(z, f);
            }
        }
    }

    public void onAnimationCancel(Animator animator) {
        this.mAnimationCanceled = true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x005e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onAnimationEnd(Animator animator) {
        if (!this.mAnimationCanceled) {
            this.mCurrentAnimation = null;
            notifyAnimationEnd(this.mAnimateToVisible);
            if (this.mAnimateToVisible) {
                InputMethodHelper.getInstance().showKeyBoard(this.mInputView);
            } else {
                InputMethodHelper.getInstance().hideKeyBoard(this.mInputView);
            }
            if (DeviceHelper.FEATURE_WHOLE_ANIM) {
                setResultViewMargin(this.mAnimateToVisible);
            } else {
                post(new Runnable() {
                    public void run() {
                        SearchActionModeView searchActionModeView = SearchActionModeView.this;
                        searchActionModeView.setResultViewMargin(searchActionModeView.mAnimateToVisible);
                    }
                });
            }
            if (this.mAnimateToVisible) {
                ActionBarContainer actionBarContainer = this.mActionBarContainer;
                if (actionBarContainer != null && actionBarContainer.isBlurEnable()) {
                    setContentViewTranslation(-this.mContentOriginPaddingTop);
                    if (!this.mAnimateToVisible) {
                        setOverlayMode(false);
                        setAlpha(0.0f);
                        killMode();
                    }
                }
            }
            setContentViewTranslation(0);
            setContentViewPadding(this.mAnimateToVisible ? this.mStatusBarPaddingTop : 0, 0);
            if (!this.mAnimateToVisible) {
            }
        }
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public void onAnimationStart(Animator animator) {
        this.mAnimationCanceled = false;
        if (this.mAnimateToVisible) {
            setAlpha(1.0f);
            return;
        }
        View tabContainer = getActionBarContainer().getTabContainer();
        if (tabContainer != null) {
            tabContainer.setVisibility(0);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.search_mask) {
            this.mSearchActionModeViewHelper.performCancelViewClick();
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSearchActionModeViewHelper.initView(this);
        this.mInputView = (EditText) findViewById(16908297);
        this.mOriginalPaddingTop = getPaddingTop();
        View contentView = getContentView();
        if (contentView != null) {
            this.mContentOriginPaddingTop = contentView.getPaddingTop();
            this.mContentOriginPaddingBottom = contentView.getPaddingBottom();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mRequestAnimation) {
            if (this.mAnimateToVisible && shouldAnimateContent()) {
                ((View) this.mAnimateView.get()).setTranslationY((float) this.mAnimateViewTranslationYStart);
            }
            queueIdleHandler();
            this.mRequestAnimation = false;
        }
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void pollViews() {
        getActionBarView();
        getActionBarContainer();
        getSplitActionBarContainer();
    }

    public boolean queueIdle() {
        this.mCurrentAnimation.start();
        return false;
    }

    public void removeAnimationListener(ActionModeAnimationListener actionModeAnimationListener) {
        if (actionModeAnimationListener != null) {
            List list = this.mAnimationListeners;
            if (list != null) {
                list.remove(actionModeAnimationListener);
            }
        }
    }

    public void setAnchorView(View view) {
        if (view != null) {
            this.mAnchorView = new WeakReference(view);
        }
    }

    public void setAnimateView(View view) {
        if (view != null) {
            this.mAnimateView = new WeakReference(view);
        }
    }

    public void setAnimationProgress(float f) {
        this.mAnimationProgress = f;
        notifyAnimationUpdate(this.mAnimateToVisible, this.mAnimationProgress);
    }

    /* access modifiers changed from: protected */
    public void setContentViewPadding(int i, int i2) {
        View contentView = getContentView();
        if (contentView != null) {
            contentView.setPadding(contentView.getPaddingLeft(), i + this.mContentOriginPaddingTop, contentView.getPaddingRight(), i2 + this.mContentOriginPaddingBottom);
        }
    }

    /* access modifiers changed from: protected */
    public void setContentViewTranslation(int i) {
        View contentView = getContentView();
        if (contentView != null) {
            contentView.setTranslationY((float) i);
        }
    }

    public void setOnBackClickListener(OnClickListener onClickListener) {
        this.mSearchActionModeViewHelper.setCancelViewClickListener(onClickListener);
    }

    /* access modifiers changed from: protected */
    public void setOverlayMode(boolean z) {
        ((ActionBarOverlayLayout) ActionBarUtils.getActionBarOverlayLayout(this)).setOverlayMode(z);
    }

    public void setResultView(View view) {
        if (view != null) {
            this.mResultView = new WeakReference(view);
            LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof MarginLayoutParams) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
                this.mResultViewOriginMarginTop = marginLayoutParams.topMargin;
                this.mResultViewOriginMarginBottom = marginLayoutParams.bottomMargin;
                this.mResultViewSet = true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setResultViewMargin(boolean z) {
        int i;
        int i2;
        if (this.mResultView != null && this.mResultViewSet) {
            if (z) {
                i = (this.mActionBarContainer.getMeasuredHeight() - this.mStatusBarPaddingTop) - this.mActionBarTopMargin;
                i2 = 0;
            } else {
                i = this.mResultViewOriginMarginTop;
                i2 = this.mResultViewOriginMarginBottom;
            }
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) ((View) this.mResultView.get()).getLayoutParams();
            marginLayoutParams.topMargin = i;
            marginLayoutParams.bottomMargin = i2;
        }
    }

    public void setStatusBarPaddingTop(int i) {
        this.mStatusBarPaddingTop = i;
    }
}

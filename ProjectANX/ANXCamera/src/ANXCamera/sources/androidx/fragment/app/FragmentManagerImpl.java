package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.core.util.DebugUtils;
import androidx.core.util.LogWriter;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.app.Fragment.SavedState;
import androidx.fragment.app.FragmentManager.BackStackEntry;
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks;
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

final class FragmentManagerImpl extends FragmentManager implements Factory2 {
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
    static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    final HashMap mActive = new HashMap();
    final ArrayList mAdded = new ArrayList();
    ArrayList mAvailBackStackIndices;
    ArrayList mBackStack;
    ArrayList mBackStackChangeListeners;
    ArrayList mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit = new Runnable() {
        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    };
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList mLifecycleCallbacks = new CopyOnWriteArrayList();
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex = 0;
    private FragmentManagerViewModel mNonConfig;
    private final OnBackPressedCallback mOnBackPressedCallback = new OnBackPressedCallback(false) {
        public void handleOnBackPressed() {
            FragmentManagerImpl.this.handleOnBackPressed();
        }
    };
    private OnBackPressedDispatcher mOnBackPressedDispatcher;
    Fragment mParent;
    ArrayList mPendingActions;
    ArrayList mPostponedTransactions;
    @Nullable
    Fragment mPrimaryNav;
    SparseArray mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    boolean mStopped;
    ArrayList mTmpAddedFragments;
    ArrayList mTmpIsPop;
    ArrayList mTmpRecords;

    class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        AnimationOrAnimator(Animator animator2) {
            this.animation = null;
            this.animator = animator2;
            if (animator2 == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }

        AnimationOrAnimator(Animation animation2) {
            this.animation = animation2;
            this.animator = null;
            if (animation2 == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }
    }

    class EndViewTransitionAnimation extends AnimationSet implements Runnable {
        private boolean mAnimating = true;
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;

        EndViewTransitionAnimation(@NonNull Animation animation, @NonNull ViewGroup viewGroup, @NonNull View view) {
            super(false);
            this.mParent = viewGroup;
            this.mChild = view;
            addAnimation(animation);
            this.mParent.post(this);
        }

        public boolean getTransformation(long j, Transformation transformation) {
            this.mAnimating = true;
            if (this.mEnded) {
                return !this.mTransitionEnded;
            }
            if (!super.getTransformation(j, transformation)) {
                this.mEnded = true;
                OneShotPreDrawListener.add(this.mParent, this);
            }
            return true;
        }

        public boolean getTransformation(long j, Transformation transformation, float f) {
            this.mAnimating = true;
            if (this.mEnded) {
                return !this.mTransitionEnded;
            }
            if (!super.getTransformation(j, transformation, f)) {
                this.mEnded = true;
                OneShotPreDrawListener.add(this.mParent, this);
            }
            return true;
        }

        public void run() {
            if (this.mEnded || !this.mAnimating) {
                this.mParent.endViewTransition(this.mChild);
                this.mTransitionEnded = true;
                return;
            }
            this.mAnimating = false;
            this.mParent.post(this);
        }
    }

    final class FragmentLifecycleCallbacksHolder {
        final FragmentLifecycleCallbacks mCallback;
        final boolean mRecursive;

        FragmentLifecycleCallbacksHolder(FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean z) {
            this.mCallback = fragmentLifecycleCallbacks;
            this.mRecursive = z;
        }
    }

    class FragmentTag {
        public static final int[] Fragment = {16842755, 16842960, 16842961};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        private FragmentTag() {
        }
    }

    interface OpGenerator {
        boolean generateOps(ArrayList arrayList, ArrayList arrayList2);
    }

    class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String str, int i, int i2) {
            this.mName = str;
            this.mId = i;
            this.mFlags = i2;
        }

        public boolean generateOps(ArrayList arrayList, ArrayList arrayList2) {
            Fragment fragment = FragmentManagerImpl.this.mPrimaryNav;
            if (fragment != null && this.mId < 0 && this.mName == null) {
                FragmentManager peekChildFragmentManager = fragment.peekChildFragmentManager();
                if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManagerImpl.this.popBackStackState(arrayList, arrayList2, this.mName, this.mId, this.mFlags);
        }
    }

    class StartEnterTransitionListener implements OnStartEnterTransitionListener {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord backStackRecord, boolean z) {
            this.mIsBack = z;
            this.mRecord = backStackRecord;
        }

        public void cancelTransaction() {
            BackStackRecord backStackRecord = this.mRecord;
            backStackRecord.mManager.completeExecute(backStackRecord, this.mIsBack, false, false);
        }

        public void completeTransaction() {
            boolean z = this.mNumPostponed > 0;
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            int size = fragmentManagerImpl.mAdded.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = (Fragment) fragmentManagerImpl.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (z && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            BackStackRecord backStackRecord = this.mRecord;
            backStackRecord.mManager.completeExecute(backStackRecord, this.mIsBack, !z, true);
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        public void onStartEnterTransition() {
            this.mNumPostponed--;
            if (this.mNumPostponed == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        public void startListening() {
            this.mNumPostponed++;
        }
    }

    FragmentManagerImpl() {
    }

    private void addAddedFragments(ArraySet arraySet) {
        int i = this.mCurState;
        if (i >= 1) {
            int min = Math.min(i, 3);
            int size = this.mAdded.size();
            for (int i2 = 0; i2 < size; i2++) {
                Fragment fragment = (Fragment) this.mAdded.get(i2);
                if (fragment.mState < min) {
                    moveToState(fragment, min, fragment.getNextAnim(), fragment.getNextTransition(), false);
                    if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                        arraySet.add(fragment);
                    }
                }
            }
        }
    }

    private void animateRemoveFragment(@NonNull final Fragment fragment, @NonNull AnimationOrAnimator animationOrAnimator, int i) {
        final View view = fragment.mView;
        final ViewGroup viewGroup = fragment.mContainer;
        viewGroup.startViewTransition(view);
        fragment.setStateAfterAnimating(i);
        Animation animation = animationOrAnimator.animation;
        if (animation != null) {
            EndViewTransitionAnimation endViewTransitionAnimation = new EndViewTransitionAnimation(animation, viewGroup, view);
            fragment.setAnimatingAway(fragment.mView);
            endViewTransitionAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    viewGroup.post(new Runnable() {
                        public void run() {
                            if (fragment.getAnimatingAway() != null) {
                                fragment.setAnimatingAway(null);
                                AnonymousClass3 r6 = AnonymousClass3.this;
                                FragmentManagerImpl fragmentManagerImpl = FragmentManagerImpl.this;
                                Fragment fragment = fragment;
                                fragmentManagerImpl.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                            }
                        }
                    });
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            fragment.mView.startAnimation(endViewTransitionAnimation);
            return;
        }
        Animator animator = animationOrAnimator.animator;
        fragment.setAnimator(animator);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                viewGroup.endViewTransition(view);
                Animator animator2 = fragment.getAnimator();
                fragment.setAnimator(null);
                if (animator2 != null && viewGroup.indexOfChild(view) < 0) {
                    FragmentManagerImpl fragmentManagerImpl = FragmentManagerImpl.this;
                    Fragment fragment = fragment;
                    fragmentManagerImpl.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                }
            }
        });
        animator.setTarget(fragment.mView);
        animator.start();
    }

    private void burpActive() {
        this.mActive.values().removeAll(Collections.singleton(null));
    }

    private void checkStateLoss() {
        if (isStateSaved()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    private void dispatchOnParentPrimaryNavigationFragmentChanged(@Nullable Fragment fragment) {
        if (fragment != null) {
            FragmentManagerImpl fragmentManagerImpl = fragment.mChildFragmentManager;
            if (fragmentManagerImpl != null) {
                fragmentManagerImpl.onParentPrimaryNavigationFragmentChanged();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    private void dispatchStateChange(int i) {
        try {
            this.mExecutingActions = true;
            moveToState(i, false);
            this.mExecutingActions = false;
            execPendingActions();
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    private void endAnimatingAwayFragments() {
        for (Fragment fragment : this.mActive.values()) {
            if (fragment != null) {
                if (fragment.getAnimatingAway() != null) {
                    int stateAfterAnimating = fragment.getStateAfterAnimating();
                    View animatingAway = fragment.getAnimatingAway();
                    Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    fragment.setAnimatingAway(null);
                    moveToState(fragment, stateAfterAnimating, 0, 0, false);
                } else if (fragment.getAnimator() != null) {
                    fragment.getAnimator().end();
                }
            }
        }
    }

    private void ensureExecReady(boolean z) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (this.mHost == null) {
            throw new IllegalStateException("Fragment host has been destroyed");
        } else if (Looper.myLooper() == this.mHost.getHandler().getLooper()) {
            if (!z) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList();
                this.mTmpIsPop = new ArrayList();
            }
            this.mExecutingActions = true;
            try {
                executePostponedTransaction(null, null);
            } finally {
                this.mExecutingActions = false;
            }
        } else {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
    }

    private static void executeOps(ArrayList arrayList, ArrayList arrayList2, int i, int i2) {
        while (i < i2) {
            BackStackRecord backStackRecord = (BackStackRecord) arrayList.get(i);
            boolean z = true;
            if (((Boolean) arrayList2.get(i)).booleanValue()) {
                backStackRecord.bumpBackStackNesting(-1);
                if (i != i2 - 1) {
                    z = false;
                }
                backStackRecord.executePopOps(z);
            } else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            i++;
        }
    }

    private void executeOpsTogether(ArrayList arrayList, ArrayList arrayList2, int i, int i2) {
        int i3;
        ArrayList arrayList3 = arrayList;
        ArrayList arrayList4 = arrayList2;
        int i4 = i;
        int i5 = i2;
        boolean z = ((BackStackRecord) arrayList3.get(i4)).mReorderingAllowed;
        ArrayList arrayList5 = this.mTmpAddedFragments;
        if (arrayList5 == null) {
            this.mTmpAddedFragments = new ArrayList();
        } else {
            arrayList5.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        Fragment primaryNavigationFragment = getPrimaryNavigationFragment();
        boolean z2 = false;
        for (int i6 = i4; i6 < i5; i6++) {
            BackStackRecord backStackRecord = (BackStackRecord) arrayList3.get(i6);
            primaryNavigationFragment = !((Boolean) arrayList4.get(i6)).booleanValue() ? backStackRecord.expandOps(this.mTmpAddedFragments, primaryNavigationFragment) : backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, primaryNavigationFragment);
            z2 = z2 || backStackRecord.mAddToBackStack;
        }
        this.mTmpAddedFragments.clear();
        if (!z) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, i, i2, false);
        }
        executeOps(arrayList, arrayList2, i, i2);
        if (z) {
            ArraySet arraySet = new ArraySet();
            addAddedFragments(arraySet);
            int postponePostponableTransactions = postponePostponableTransactions(arrayList, arrayList2, i, i2, arraySet);
            makeRemovedFragmentsInvisible(arraySet);
            i3 = postponePostponableTransactions;
        } else {
            i3 = i5;
        }
        if (i3 != i4 && z) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, i, i3, true);
            moveToState(this.mCurState, true);
        }
        while (i4 < i5) {
            BackStackRecord backStackRecord2 = (BackStackRecord) arrayList3.get(i4);
            if (((Boolean) arrayList4.get(i4)).booleanValue()) {
                int i7 = backStackRecord2.mIndex;
                if (i7 >= 0) {
                    freeBackStackIndex(i7);
                    backStackRecord2.mIndex = -1;
                }
            }
            backStackRecord2.runOnCommitRunnables();
            i4++;
        }
        if (z2) {
            reportBackStackChanged();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0030, code lost:
        if (((java.lang.Boolean) r9.get(r5)).booleanValue() != false) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0032, code lost:
        r3.cancelTransaction();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006b, code lost:
        if (((java.lang.Boolean) r9.get(r5)).booleanValue() != false) goto L_0x0032;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void executePostponedTransaction(ArrayList arrayList, ArrayList arrayList2) {
        ArrayList arrayList3 = this.mPostponedTransactions;
        int size = arrayList3 == null ? 0 : arrayList3.size();
        int i = 0;
        while (i < size) {
            StartEnterTransitionListener startEnterTransitionListener = (StartEnterTransitionListener) this.mPostponedTransactions.get(i);
            if (arrayList != null && !startEnterTransitionListener.mIsBack) {
                int indexOf = arrayList.indexOf(startEnterTransitionListener.mRecord);
                if (indexOf != -1) {
                }
            }
            if (startEnterTransitionListener.isReady() || (arrayList != null && startEnterTransitionListener.mRecord.interactsWith(arrayList, 0, arrayList.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                size--;
                if (arrayList != null && !startEnterTransitionListener.mIsBack) {
                    int indexOf2 = arrayList.indexOf(startEnterTransitionListener.mRecord);
                    if (indexOf2 != -1) {
                    }
                }
                startEnterTransitionListener.completeTransaction();
            }
            i++;
        }
    }

    private Fragment findFragmentUnder(Fragment fragment) {
        ViewGroup viewGroup = fragment.mContainer;
        View view = fragment.mView;
        if (!(viewGroup == null || view == null)) {
            for (int indexOf = this.mAdded.indexOf(fragment) - 1; indexOf >= 0; indexOf--) {
                Fragment fragment2 = (Fragment) this.mAdded.get(indexOf);
                if (fragment2.mContainer == viewGroup && fragment2.mView != null) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                ((StartEnterTransitionListener) this.mPostponedTransactions.remove(0)).completeTransaction();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003b, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean generateOpsForPendingActions(ArrayList arrayList, ArrayList arrayList2) {
        synchronized (this) {
            if (this.mPendingActions != null) {
                if (this.mPendingActions.size() != 0) {
                    boolean z = false;
                    for (int i = 0; i < this.mPendingActions.size(); i++) {
                        z |= ((OpGenerator) this.mPendingActions.get(i)).generateOps(arrayList, arrayList2);
                    }
                    this.mPendingActions.clear();
                    this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                    return z;
                }
            }
        }
    }

    private boolean isPrimaryNavigation(@Nullable Fragment fragment) {
        boolean z = true;
        if (fragment == null) {
            return true;
        }
        FragmentManagerImpl fragmentManagerImpl = fragment.mFragmentManager;
        if (fragment != fragmentManagerImpl.getPrimaryNavigationFragment() || !isPrimaryNavigation(fragmentManagerImpl.mParent)) {
            z = false;
        }
        return z;
    }

    static AnimationOrAnimator makeFadeAnimation(float f, float f2) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(f, f2);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220);
        return new AnimationOrAnimator((Animation) alphaAnimation);
    }

    static AnimationOrAnimator makeOpenCloseAnimation(float f, float f2, float f3, float f4) {
        AnimationSet animationSet = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(f, f2, f, f2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(DECELERATE_QUINT);
        scaleAnimation.setDuration(220);
        animationSet.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(f3, f4);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220);
        animationSet.addAnimation(alphaAnimation);
        return new AnimationOrAnimator((Animation) animationSet);
    }

    private void makeRemovedFragmentsInvisible(ArraySet arraySet) {
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            Fragment fragment = (Fragment) arraySet.valueAt(i);
            if (!fragment.mAdded) {
                View requireView = fragment.requireView();
                fragment.mPostponedAlpha = requireView.getAlpha();
                requireView.setAlpha(0.0f);
            }
        }
    }

    private void onParentPrimaryNavigationFragmentChanged() {
        updateOnBackPressedCallbackEnabled();
        for (Fragment dispatchOnParentPrimaryNavigationFragmentChanged : this.mActive.values()) {
            dispatchOnParentPrimaryNavigationFragmentChanged(dispatchOnParentPrimaryNavigationFragmentChanged);
        }
    }

    private boolean popBackStackImmediate(String str, int i, int i2) {
        execPendingActions();
        ensureExecReady(true);
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null && i < 0 && str == null) {
            FragmentManager peekChildFragmentManager = fragment.peekChildFragmentManager();
            if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                return true;
            }
        }
        boolean popBackStackState = popBackStackState(this.mTmpRecords, this.mTmpIsPop, str, i, i2);
        if (popBackStackState) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        burpActive();
        return popBackStackState;
    }

    private int postponePostponableTransactions(ArrayList arrayList, ArrayList arrayList2, int i, int i2, ArraySet arraySet) {
        int i3 = i2;
        for (int i4 = i2 - 1; i4 >= i; i4--) {
            BackStackRecord backStackRecord = (BackStackRecord) arrayList.get(i4);
            boolean booleanValue = ((Boolean) arrayList2.get(i4)).booleanValue();
            boolean z = backStackRecord.isPostponed() && !backStackRecord.interactsWith(arrayList, i4 + 1, i2);
            if (z) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList();
                }
                StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, booleanValue);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
                if (booleanValue) {
                    backStackRecord.executeOps();
                } else {
                    backStackRecord.executePopOps(false);
                }
                i3--;
                if (i4 != i3) {
                    arrayList.remove(i4);
                    arrayList.add(i3, backStackRecord);
                }
                addAddedFragments(arraySet);
            }
        }
        return i3;
    }

    private void removeRedundantOperationsAndExecute(ArrayList arrayList, ArrayList arrayList2) {
        if (arrayList != null && !arrayList.isEmpty()) {
            if (arrayList2 == null || arrayList.size() != arrayList2.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            }
            executePostponedTransaction(arrayList, arrayList2);
            int size = arrayList.size();
            int i = 0;
            int i2 = 0;
            while (i < size) {
                if (!((BackStackRecord) arrayList.get(i)).mReorderingAllowed) {
                    if (i2 != i) {
                        executeOpsTogether(arrayList, arrayList2, i2, i);
                    }
                    i2 = i + 1;
                    if (((Boolean) arrayList2.get(i)).booleanValue()) {
                        while (i2 < size && ((Boolean) arrayList2.get(i2)).booleanValue() && !((BackStackRecord) arrayList.get(i2)).mReorderingAllowed) {
                            i2++;
                        }
                    }
                    executeOpsTogether(arrayList, arrayList2, i, i2);
                    i = i2 - 1;
                }
                i++;
            }
            if (i2 != size) {
                executeOpsTogether(arrayList, arrayList2, i2, size);
            }
        }
    }

    public static int reverseTransit(int i) {
        if (i == 4097) {
            return 8194;
        }
        if (i != 4099) {
            return i != 8194 ? 0 : 4097;
        }
        return 4099;
    }

    private void throwException(RuntimeException runtimeException) {
        String message = runtimeException.getMessage();
        String str = TAG;
        Log.e(str, message);
        Log.e(str, "Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter(str));
        FragmentHostCallback fragmentHostCallback = this.mHost;
        String str2 = "Failed dumping state";
        String str3 = "  ";
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.onDump(str3, null, printWriter, new String[0]);
            } catch (Exception e) {
                Log.e(str, str2, e);
            }
        } else {
            dump(str3, null, printWriter, new String[0]);
        }
        throw runtimeException;
    }

    public static int transitToStyleIndex(int i, boolean z) {
        if (i == 4097) {
            return z ? 1 : 2;
        }
        if (i == 4099) {
            return z ? 5 : 6;
        }
        if (i != 8194) {
            return -1;
        }
        return z ? 3 : 4;
    }

    private void updateOnBackPressedCallbackEnabled() {
        ArrayList arrayList = this.mPendingActions;
        boolean z = true;
        if (arrayList == null || arrayList.isEmpty()) {
            OnBackPressedCallback onBackPressedCallback = this.mOnBackPressedCallback;
            if (getBackStackEntryCount() <= 0 || !isPrimaryNavigation(this.mParent)) {
                z = false;
            }
            onBackPressedCallback.setEnabled(z);
            return;
        }
        this.mOnBackPressedCallback.setEnabled(true);
    }

    /* access modifiers changed from: 0000 */
    public void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(backStackRecord);
    }

    public void addFragment(Fragment fragment, boolean z) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("add: ");
            sb.append(fragment);
            Log.v(TAG, sb.toString());
        }
        makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (!this.mAdded.contains(fragment)) {
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (z) {
                moveToState(fragment);
                return;
            }
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Fragment already added: ");
        sb2.append(fragment);
        throw new IllegalStateException(sb2.toString());
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    /* access modifiers changed from: 0000 */
    public void addRetainedFragment(@NonNull Fragment fragment) {
        boolean isStateSaved = isStateSaved();
        String str = TAG;
        if (isStateSaved) {
            if (DEBUG) {
                Log.v(str, "Ignoring addRetainedFragment as the state is already saved");
            }
            return;
        }
        if (this.mNonConfig.addRetainedFragment(fragment) && DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Updating retained Fragments: Added ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
    }

    public int allocBackStackIndex(BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null) {
                if (this.mAvailBackStackIndices.size() > 0) {
                    int intValue = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
                    if (DEBUG) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Adding back stack index ");
                        sb.append(intValue);
                        sb.append(" with ");
                        sb.append(backStackRecord);
                        Log.v(str, sb.toString());
                    }
                    this.mBackStackIndices.set(intValue, backStackRecord);
                    return intValue;
                }
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int size = this.mBackStackIndices.size();
            if (DEBUG) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Setting back stack index ");
                sb2.append(size);
                sb2.append(" to ");
                sb2.append(backStackRecord);
                Log.v(str2, sb2.toString());
            }
            this.mBackStackIndices.add(backStackRecord);
            return size;
        }
    }

    /* JADX WARNING: type inference failed for: r4v7, types: [androidx.lifecycle.LifecycleOwner] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void attachController(@NonNull FragmentHostCallback fragmentHostCallback, @NonNull FragmentContainer fragmentContainer, @Nullable Fragment fragment) {
        if (this.mHost == null) {
            this.mHost = fragmentHostCallback;
            this.mContainer = fragmentContainer;
            this.mParent = fragment;
            if (this.mParent != null) {
                updateOnBackPressedCallbackEnabled();
            }
            if (fragmentHostCallback instanceof OnBackPressedDispatcherOwner) {
                OnBackPressedDispatcherOwner onBackPressedDispatcherOwner = (OnBackPressedDispatcherOwner) fragmentHostCallback;
                this.mOnBackPressedDispatcher = onBackPressedDispatcherOwner.getOnBackPressedDispatcher();
                if (fragment != 0) {
                    onBackPressedDispatcherOwner = fragment;
                }
                this.mOnBackPressedDispatcher.addCallback(onBackPressedDispatcherOwner, this.mOnBackPressedCallback);
            }
            FragmentManagerViewModel fragmentManagerViewModel = fragment != 0 ? fragment.mFragmentManager.getChildNonConfig(fragment) : fragmentHostCallback instanceof ViewModelStoreOwner ? FragmentManagerViewModel.getInstance(((ViewModelStoreOwner) fragmentHostCallback).getViewModelStore()) : new FragmentManagerViewModel(false);
            this.mNonConfig = fragmentManagerViewModel;
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    public void attachFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("attach: ");
            sb.append(fragment);
            Log.v(TAG, sb.toString());
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (fragment.mAdded) {
                return;
            }
            if (!this.mAdded.contains(fragment)) {
                if (DEBUG) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("add from attach: ");
                    sb2.append(fragment);
                    Log.v(TAG, sb2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.add(fragment);
                }
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                    return;
                }
                return;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Fragment already added: ");
            sb3.append(fragment);
            throw new IllegalStateException(sb3.toString());
        }
    }

    @NonNull
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    /* access modifiers changed from: 0000 */
    public void completeExecute(BackStackRecord backStackRecord, boolean z, boolean z2, boolean z3) {
        if (z) {
            backStackRecord.executePopOps(z3);
        } else {
            backStackRecord.executeOps();
        }
        ArrayList arrayList = new ArrayList(1);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList.add(backStackRecord);
        arrayList2.add(Boolean.valueOf(z));
        if (z2) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, 0, 1, true);
        }
        if (z3) {
            moveToState(this.mCurState, true);
        }
        for (Fragment fragment : this.mActive.values()) {
            if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && backStackRecord.interactsWith(fragment.mContainerId)) {
                float f = fragment.mPostponedAlpha;
                if (f > 0.0f) {
                    fragment.mView.setAlpha(f);
                }
                if (z3) {
                    fragment.mPostponedAlpha = 0.0f;
                } else {
                    fragment.mPostponedAlpha = -1.0f;
                    fragment.mIsNewlyAdded = false;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            AnimationOrAnimator loadAnimation = loadAnimation(fragment, fragment.getNextTransition(), !fragment.mHidden, fragment.getNextTransitionStyle());
            if (loadAnimation != null) {
                Animator animator = loadAnimation.animator;
                if (animator != null) {
                    animator.setTarget(fragment.mView);
                    if (!fragment.mHidden) {
                        fragment.mView.setVisibility(0);
                    } else if (fragment.isHideReplaced()) {
                        fragment.setHideReplaced(false);
                    } else {
                        final ViewGroup viewGroup = fragment.mContainer;
                        final View view = fragment.mView;
                        viewGroup.startViewTransition(view);
                        loadAnimation.animator.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animator) {
                                viewGroup.endViewTransition(view);
                                animator.removeListener(this);
                                View view = fragment.mView;
                                if (view != null) {
                                    view.setVisibility(8);
                                }
                            }
                        });
                    }
                    loadAnimation.animator.start();
                }
            }
            if (loadAnimation != null) {
                fragment.mView.startAnimation(loadAnimation.animation);
                loadAnimation.animation.start();
            }
            int i = (!fragment.mHidden || fragment.isHideReplaced()) ? 0 : 8;
            fragment.mView.setVisibility(i);
            if (fragment.isHideReplaced()) {
                fragment.setHideReplaced(false);
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    public void detachFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("detach: ");
            sb.append(fragment);
            Log.v(TAG, sb.toString());
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (DEBUG) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("remove from detach: ");
                    sb2.append(fragment);
                    Log.v(TAG, sb2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
            }
        }
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(2);
    }

    public void dispatchConfigurationChanged(@NonNull Configuration configuration) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.performConfigurationChanged(configuration);
            }
        }
    }

    public boolean dispatchContextItemSelected(@NonNull MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(1);
    }

    public boolean dispatchCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        if (this.mCurState < 1) {
            return false;
        }
        ArrayList arrayList = null;
        boolean z = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performCreateOptionsMenu(menu, menuInflater)) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(fragment);
                z = true;
            }
        }
        if (this.mCreatedMenus != null) {
            for (int i2 = 0; i2 < this.mCreatedMenus.size(); i2++) {
                Fragment fragment2 = (Fragment) this.mCreatedMenus.get(i2);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = arrayList;
        return z;
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
        if (this.mOnBackPressedDispatcher != null) {
            this.mOnBackPressedCallback.remove();
            this.mOnBackPressedDispatcher = null;
        }
    }

    public void dispatchDestroyView() {
        dispatchStateChange(1);
    }

    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.performLowMemory();
            }
        }
    }

    public void dispatchMultiWindowModeChanged(boolean z) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = (Fragment) this.mAdded.get(size);
            if (fragment != null) {
                fragment.performMultiWindowModeChanged(z);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentActivityCreated(@NonNull Fragment fragment, @Nullable Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentActivityCreated(fragment, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentActivityCreated(this, fragment, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentAttached(@NonNull Fragment fragment, @NonNull Context context, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentAttached(fragment, context, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentAttached(this, fragment, context);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentCreated(@NonNull Fragment fragment, @Nullable Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentCreated(fragment, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentCreated(this, fragment, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentDestroyed(@NonNull Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentDestroyed(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentDestroyed(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentDetached(@NonNull Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentDetached(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentDetached(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPaused(@NonNull Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPaused(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentPaused(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPreAttached(@NonNull Fragment fragment, @NonNull Context context, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPreAttached(fragment, context, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreAttached(this, fragment, context);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPreCreated(@NonNull Fragment fragment, @Nullable Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPreCreated(fragment, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreCreated(this, fragment, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentResumed(@NonNull Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentResumed(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentResumed(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentSaveInstanceState(@NonNull Fragment fragment, @NonNull Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentSaveInstanceState(this, fragment, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentStarted(@NonNull Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentStarted(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentStarted(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentStopped(@NonNull Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentStopped(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentStopped(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentViewCreated(@NonNull Fragment fragment, @NonNull View view, @Nullable Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewCreated(this, fragment, view, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentViewDestroyed(@NonNull Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentViewDestroyed(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder = (FragmentLifecycleCallbacksHolder) it.next();
            if (!z || fragmentLifecycleCallbacksHolder.mRecursive) {
                fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewDestroyed(this, fragment);
            }
        }
    }

    public boolean dispatchOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(@NonNull Menu menu) {
        if (this.mCurState >= 1) {
            for (int i = 0; i < this.mAdded.size(); i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    public void dispatchPause() {
        dispatchStateChange(3);
    }

    public void dispatchPictureInPictureModeChanged(boolean z) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = (Fragment) this.mAdded.get(size);
            if (fragment != null) {
                fragment.performPictureInPictureModeChanged(z);
            }
        }
    }

    public boolean dispatchPrepareOptionsMenu(@NonNull Menu menu) {
        if (this.mCurState < 1) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performPrepareOptionsMenu(menu)) {
                z = true;
            }
        }
        return z;
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(4);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(3);
    }

    public void dispatchStop() {
        this.mStopped = true;
        dispatchStateChange(2);
    }

    /* access modifiers changed from: 0000 */
    public void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            startPendingDeferredFragments();
        }
    }

    public void dump(@NonNull String str, @Nullable FileDescriptor fileDescriptor, @NonNull PrintWriter printWriter, @Nullable String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("    ");
        String sb2 = sb.toString();
        if (!this.mActive.isEmpty()) {
            printWriter.print(str);
            printWriter.print("Active Fragments in ");
            printWriter.print(Integer.toHexString(System.identityHashCode(this)));
            printWriter.println(":");
            for (Fragment fragment : this.mActive.values()) {
                printWriter.print(str);
                printWriter.println(fragment);
                if (fragment != null) {
                    fragment.dump(sb2, fileDescriptor, printWriter, strArr);
                }
            }
        }
        int size = this.mAdded.size();
        if (size > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i = 0; i < size; i++) {
                Fragment fragment2 = (Fragment) this.mAdded.get(i);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.println(fragment2.toString());
            }
        }
        ArrayList arrayList = this.mCreatedMenus;
        if (arrayList != null) {
            int size2 = arrayList.size();
            if (size2 > 0) {
                printWriter.print(str);
                printWriter.println("Fragments Created Menus:");
                for (int i2 = 0; i2 < size2; i2++) {
                    Fragment fragment3 = (Fragment) this.mCreatedMenus.get(i2);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i2);
                    printWriter.print(": ");
                    printWriter.println(fragment3.toString());
                }
            }
        }
        ArrayList arrayList2 = this.mBackStack;
        if (arrayList2 != null) {
            int size3 = arrayList2.size();
            if (size3 > 0) {
                printWriter.print(str);
                printWriter.println("Back Stack:");
                for (int i3 = 0; i3 < size3; i3++) {
                    BackStackRecord backStackRecord = (BackStackRecord) this.mBackStack.get(i3);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i3);
                    printWriter.print(": ");
                    printWriter.println(backStackRecord.toString());
                    backStackRecord.dump(sb2, printWriter);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                int size4 = this.mBackStackIndices.size();
                if (size4 > 0) {
                    printWriter.print(str);
                    printWriter.println("Back Stack Indices:");
                    for (int i4 = 0; i4 < size4; i4++) {
                        BackStackRecord backStackRecord2 = (BackStackRecord) this.mBackStackIndices.get(i4);
                        printWriter.print(str);
                        printWriter.print("  #");
                        printWriter.print(i4);
                        printWriter.print(": ");
                        printWriter.println(backStackRecord2);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(str);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        ArrayList arrayList3 = this.mPendingActions;
        if (arrayList3 != null) {
            int size5 = arrayList3.size();
            if (size5 > 0) {
                printWriter.print(str);
                printWriter.println("Pending Actions:");
                for (int i5 = 0; i5 < size5; i5++) {
                    OpGenerator opGenerator = (OpGenerator) this.mPendingActions.get(i5);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i5);
                    printWriter.print(": ");
                    printWriter.println(opGenerator);
                }
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mHost=");
        printWriter.println(this.mHost);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mStopped=");
        printWriter.print(this.mStopped);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
    }

    public void enqueueAction(OpGenerator opGenerator, boolean z) {
        if (!z) {
            checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed) {
                if (this.mHost != null) {
                    if (this.mPendingActions == null) {
                        this.mPendingActions = new ArrayList();
                    }
                    this.mPendingActions.add(opGenerator);
                    scheduleCommit();
                    return;
                }
            }
            if (!z) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void ensureInflatedFragmentView(Fragment fragment) {
        if (fragment.mFromLayout && !fragment.mPerformedCreateView) {
            fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
            View view = fragment.mView;
            if (view != null) {
                fragment.mInnerView = view;
                view.setSaveFromParentEnabled(false);
                if (fragment.mHidden) {
                    fragment.mView.setVisibility(8);
                }
                fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
                return;
            }
            fragment.mInnerView = null;
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean execPendingActions() {
        ensureExecReady(true);
        boolean z = false;
        while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                z = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
        updateOnBackPressedCallbackEnabled();
        doPendingDeferredStart();
        burpActive();
        return z;
    }

    public void execSingleAction(OpGenerator opGenerator, boolean z) {
        if (!z || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(z);
            if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    cleanupExec();
                }
            }
            updateOnBackPressedCallbackEnabled();
            doPendingDeferredStart();
            burpActive();
        }
    }

    public boolean executePendingTransactions() {
        boolean execPendingActions = execPendingActions();
        forcePostponedTransactions();
        return execPendingActions;
    }

    @Nullable
    public Fragment findFragmentById(int i) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = (Fragment) this.mAdded.get(size);
            if (fragment != null && fragment.mFragmentId == i) {
                return fragment;
            }
        }
        for (Fragment fragment2 : this.mActive.values()) {
            if (fragment2 != null && fragment2.mFragmentId == i) {
                return fragment2;
            }
        }
        return null;
    }

    @Nullable
    public Fragment findFragmentByTag(@Nullable String str) {
        if (str != null) {
            for (int size = this.mAdded.size() - 1; size >= 0; size--) {
                Fragment fragment = (Fragment) this.mAdded.get(size);
                if (fragment != null && str.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (str != null) {
            for (Fragment fragment2 : this.mActive.values()) {
                if (fragment2 != null && str.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(@NonNull String str) {
        for (Fragment fragment : this.mActive.values()) {
            if (fragment != null) {
                Fragment findFragmentByWho = fragment.findFragmentByWho(str);
                if (findFragmentByWho != null) {
                    return findFragmentByWho;
                }
            }
        }
        return null;
    }

    public void freeBackStackIndex(int i) {
        synchronized (this) {
            this.mBackStackIndices.set(i, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList();
            }
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Freeing back stack index ");
                sb.append(i);
                Log.v(str, sb.toString());
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(i));
        }
    }

    /* access modifiers changed from: 0000 */
    public int getActiveFragmentCount() {
        return this.mActive.size();
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public List getActiveFragments() {
        return new ArrayList(this.mActive.values());
    }

    public BackStackEntry getBackStackEntryAt(int i) {
        return (BackStackEntry) this.mBackStack.get(i);
    }

    public int getBackStackEntryCount() {
        ArrayList arrayList = this.mBackStack;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public FragmentManagerViewModel getChildNonConfig(@NonNull Fragment fragment) {
        return this.mNonConfig.getChildNonConfig(fragment);
    }

    @Nullable
    public Fragment getFragment(Bundle bundle, String str) {
        String string = bundle.getString(str);
        if (string == null) {
            return null;
        }
        Fragment fragment = (Fragment) this.mActive.get(string);
        if (fragment != null) {
            return fragment;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment no longer exists for key ");
        sb.append(str);
        sb.append(": unique id ");
        sb.append(string);
        throwException(new IllegalStateException(sb.toString()));
        throw null;
    }

    @NonNull
    public FragmentFactory getFragmentFactory() {
        if (super.getFragmentFactory() == FragmentManager.DEFAULT_FACTORY) {
            Fragment fragment = this.mParent;
            if (fragment != null) {
                return fragment.mFragmentManager.getFragmentFactory();
            }
            setFragmentFactory(new FragmentFactory() {
                @NonNull
                public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String str) {
                    FragmentHostCallback fragmentHostCallback = FragmentManagerImpl.this.mHost;
                    return fragmentHostCallback.instantiate(fragmentHostCallback.getContext(), str, null);
                }
            });
        }
        return super.getFragmentFactory();
    }

    public List getFragments() {
        List list;
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.mAdded) {
            list = (List) this.mAdded.clone();
        }
        return list;
    }

    /* access modifiers changed from: 0000 */
    public Factory2 getLayoutInflaterFactory() {
        return this;
    }

    @Nullable
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public ViewModelStore getViewModelStore(@NonNull Fragment fragment) {
        return this.mNonConfig.getViewModelStore(fragment);
    }

    /* access modifiers changed from: 0000 */
    public void handleOnBackPressed() {
        execPendingActions();
        if (this.mOnBackPressedCallback.isEnabled()) {
            popBackStackImmediate();
        } else {
            this.mOnBackPressedDispatcher.onBackPressed();
        }
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("hide: ");
            sb.append(fragment);
            Log.v(TAG, sb.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
        }
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    /* access modifiers changed from: 0000 */
    public boolean isStateAtLeast(int i) {
        return this.mCurState >= i;
    }

    public boolean isStateSaved() {
        return this.mStateSaved || this.mStopped;
    }

    /* access modifiers changed from: 0000 */
    public AnimationOrAnimator loadAnimation(Fragment fragment, int i, boolean z, int i2) {
        int nextAnim = fragment.getNextAnim();
        Animation onCreateAnimation = fragment.onCreateAnimation(i, z, nextAnim);
        if (onCreateAnimation != null) {
            return new AnimationOrAnimator(onCreateAnimation);
        }
        Animator onCreateAnimator = fragment.onCreateAnimator(i, z, nextAnim);
        if (onCreateAnimator != null) {
            return new AnimationOrAnimator(onCreateAnimator);
        }
        if (nextAnim != 0) {
            boolean equals = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim));
            boolean z2 = false;
            if (equals) {
                try {
                    Animation loadAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (loadAnimation != null) {
                        return new AnimationOrAnimator(loadAnimation);
                    }
                    z2 = true;
                } catch (NotFoundException e) {
                    throw e;
                } catch (RuntimeException unused) {
                }
            }
            if (!z2) {
                try {
                    Animator loadAnimator = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
                    if (loadAnimator != null) {
                        return new AnimationOrAnimator(loadAnimator);
                    }
                } catch (RuntimeException e2) {
                    if (!equals) {
                        Animation loadAnimation2 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                        if (loadAnimation2 != null) {
                            return new AnimationOrAnimator(loadAnimation2);
                        }
                    } else {
                        throw e2;
                    }
                }
            }
        }
        if (i == 0) {
            return null;
        }
        int transitToStyleIndex = transitToStyleIndex(i, z);
        if (transitToStyleIndex < 0) {
            return null;
        }
        switch (transitToStyleIndex) {
            case 1:
                return makeOpenCloseAnimation(1.125f, 1.0f, 0.0f, 1.0f);
            case 2:
                return makeOpenCloseAnimation(1.0f, 0.975f, 1.0f, 0.0f);
            case 3:
                return makeOpenCloseAnimation(0.975f, 1.0f, 0.0f, 1.0f);
            case 4:
                return makeOpenCloseAnimation(1.0f, 1.075f, 1.0f, 0.0f);
            case 5:
                return makeFadeAnimation(0.0f, 1.0f);
            case 6:
                return makeFadeAnimation(1.0f, 0.0f);
            default:
                if (i2 == 0 && this.mHost.onHasWindowAnimations()) {
                    int onGetWindowAnimations = this.mHost.onGetWindowAnimations();
                }
                return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeActive(Fragment fragment) {
        if (this.mActive.get(fragment.mWho) == null) {
            this.mActive.put(fragment.mWho, fragment);
            if (fragment.mRetainInstanceChangedWhileDetached) {
                if (fragment.mRetainInstance) {
                    addRetainedFragment(fragment);
                } else {
                    removeRetainedFragment(fragment);
                }
                fragment.mRetainInstanceChangedWhileDetached = false;
            }
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Added fragment to active set ");
                sb.append(fragment);
                Log.v(TAG, sb.toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeInactive(Fragment fragment) {
        if (this.mActive.get(fragment.mWho) != null) {
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Removed fragment from active set ");
                sb.append(fragment);
                Log.v(TAG, sb.toString());
            }
            for (Fragment fragment2 : this.mActive.values()) {
                if (fragment2 != null && fragment.mWho.equals(fragment2.mTargetWho)) {
                    fragment2.mTarget = fragment;
                    fragment2.mTargetWho = null;
                }
            }
            this.mActive.put(fragment.mWho, null);
            removeRetainedFragment(fragment);
            String str = fragment.mTargetWho;
            if (str != null) {
                fragment.mTarget = (Fragment) this.mActive.get(str);
            }
            fragment.initState();
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveFragmentToExpectedState(Fragment fragment) {
        if (fragment != null) {
            if (!this.mActive.containsKey(fragment.mWho)) {
                if (DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Ignoring moving ");
                    sb.append(fragment);
                    sb.append(" to state ");
                    sb.append(this.mCurState);
                    sb.append("since it is not added to ");
                    sb.append(this);
                    Log.v(TAG, sb.toString());
                }
                return;
            }
            int i = this.mCurState;
            if (fragment.mRemoving) {
                i = fragment.isInBackStack() ? Math.min(i, 1) : Math.min(i, 0);
            }
            moveToState(fragment, i, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
            if (fragment.mView != null) {
                Fragment findFragmentUnder = findFragmentUnder(fragment);
                if (findFragmentUnder != null) {
                    View view = findFragmentUnder.mView;
                    ViewGroup viewGroup = fragment.mContainer;
                    int indexOfChild = viewGroup.indexOfChild(view);
                    int indexOfChild2 = viewGroup.indexOfChild(fragment.mView);
                    if (indexOfChild2 < indexOfChild) {
                        viewGroup.removeViewAt(indexOfChild2);
                        viewGroup.addView(fragment.mView, indexOfChild);
                    }
                }
                if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                    float f = fragment.mPostponedAlpha;
                    if (f > 0.0f) {
                        fragment.mView.setAlpha(f);
                    }
                    fragment.mPostponedAlpha = 0.0f;
                    fragment.mIsNewlyAdded = false;
                    AnimationOrAnimator loadAnimation = loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                    if (loadAnimation != null) {
                        Animation animation = loadAnimation.animation;
                        if (animation != null) {
                            fragment.mView.startAnimation(animation);
                        } else {
                            loadAnimation.animator.setTarget(fragment.mView);
                            loadAnimation.animator.start();
                        }
                    }
                }
            }
            if (fragment.mHiddenChanged) {
                completeShowHideFragment(fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(int i, boolean z) {
        if (this.mHost == null && i != 0) {
            throw new IllegalStateException("No activity");
        } else if (z || i != this.mCurState) {
            this.mCurState = i;
            int size = this.mAdded.size();
            for (int i2 = 0; i2 < size; i2++) {
                moveFragmentToExpectedState((Fragment) this.mAdded.get(i2));
            }
            for (Fragment fragment : this.mActive.values()) {
                if (fragment != null && ((fragment.mRemoving || fragment.mDetached) && !fragment.mIsNewlyAdded)) {
                    moveFragmentToExpectedState(fragment);
                }
            }
            startPendingDeferredFragments();
            if (this.mNeedMenuInvalidate) {
                FragmentHostCallback fragmentHostCallback = this.mHost;
                if (fragmentHostCallback != null && this.mCurState == 4) {
                    fragmentHostCallback.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(Fragment fragment) {
        moveToState(fragment, this.mCurState, 0, 0, false);
    }

    /* JADX WARNING: type inference failed for: r11v1 */
    /* JADX WARNING: type inference failed for: r11v2 */
    /* JADX WARNING: type inference failed for: r11v3 */
    /* JADX WARNING: type inference failed for: r11v4 */
    /* JADX WARNING: type inference failed for: r11v5 */
    /* JADX WARNING: type inference failed for: r11v6 */
    /* JADX WARNING: type inference failed for: r11v7 */
    /* JADX WARNING: type inference failed for: r11v8 */
    /* JADX WARNING: type inference failed for: r11v9 */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x02de  */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x02ff  */
    /* JADX WARNING: Removed duplicated region for block: B:262:0x04cf  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveToState(Fragment fragment, int i, int i2, int i3, boolean z) {
        int i4;
        boolean z2;
        char c;
        char c2;
        char c3;
        ? r11;
        char c4;
        ViewGroup viewGroup;
        String str;
        String str2;
        String str3;
        Fragment fragment2 = fragment;
        boolean z3 = 1;
        if (!fragment2.mAdded || fragment2.mDetached) {
            i4 = i;
            if (i4 > 1) {
                i4 = 1;
            }
        } else {
            i4 = i;
        }
        if (fragment2.mRemoving) {
            int i5 = fragment2.mState;
            if (i4 > i5) {
                i4 = (i5 != 0 || !fragment.isInBackStack()) ? fragment2.mState : 1;
            }
        }
        if (fragment2.mDeferStart && fragment2.mState < 3 && i4 > 2) {
            i4 = 2;
        }
        State state = fragment2.mMaxState;
        int min = state == State.CREATED ? Math.min(i4, 1) : Math.min(i4, state.ordinal());
        int i6 = fragment2.mState;
        String str4 = TAG;
        if (i6 <= min) {
            if (!fragment2.mFromLayout || fragment2.mInLayout) {
                if (!(fragment.getAnimatingAway() == null && fragment.getAnimator() == null)) {
                    fragment2.setAnimatingAway(null);
                    fragment2.setAnimator(null);
                    moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, true);
                }
                int i7 = fragment2.mState;
                if (i7 != 0) {
                    if (i7 != 1) {
                        if (i7 == 2) {
                            c4 = 2;
                            if (r11 > c4) {
                                if (DEBUG) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("moveto STARTED: ");
                                    sb.append(fragment2);
                                    Log.v(str4, sb.toString());
                                }
                                fragment.performStart();
                                dispatchOnFragmentStarted(fragment2, false);
                            }
                            c3 = 3;
                            min = r11;
                            if (min > c3) {
                            }
                        } else if (i7 == 3) {
                            c3 = 3;
                            if (min > c3) {
                                if (DEBUG) {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("moveto RESUMED: ");
                                    sb2.append(fragment2);
                                    Log.v(str4, sb2.toString());
                                }
                                fragment.performResume();
                                dispatchOnFragmentResumed(fragment2, false);
                                fragment2.mSavedFragmentState = null;
                                fragment2.mSavedViewState = null;
                            }
                        }
                    }
                } else if (min > 0) {
                    if (DEBUG) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("moveto CREATED: ");
                        sb3.append(fragment2);
                        Log.v(str4, sb3.toString());
                    }
                    Bundle bundle = fragment2.mSavedFragmentState;
                    if (bundle != null) {
                        bundle.setClassLoader(this.mHost.getContext().getClassLoader());
                        fragment2.mSavedViewState = fragment2.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                        Fragment fragment3 = getFragment(fragment2.mSavedFragmentState, TARGET_STATE_TAG);
                        fragment2.mTargetWho = fragment3 != null ? fragment3.mWho : null;
                        if (fragment2.mTargetWho != null) {
                            fragment2.mTargetRequestCode = fragment2.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
                        }
                        Boolean bool = fragment2.mSavedUserVisibleHint;
                        if (bool != null) {
                            fragment2.mUserVisibleHint = bool.booleanValue();
                            fragment2.mSavedUserVisibleHint = null;
                        } else {
                            fragment2.mUserVisibleHint = fragment2.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
                        }
                        if (!fragment2.mUserVisibleHint) {
                            fragment2.mDeferStart = true;
                            if (min > 2) {
                                int i8 = 2;
                            }
                        }
                    }
                    FragmentHostCallback fragmentHostCallback = this.mHost;
                    fragment2.mHost = fragmentHostCallback;
                    Fragment fragment4 = this.mParent;
                    fragment2.mParentFragment = fragment4;
                    fragment2.mFragmentManager = fragment4 != null ? fragment4.mChildFragmentManager : fragmentHostCallback.mFragmentManager;
                    Fragment fragment5 = fragment2.mTarget;
                    String str5 = " that does not belong to this FragmentManager!";
                    String str6 = " declared target fragment ";
                    String str7 = "Fragment ";
                    if (fragment5 != null) {
                        Object obj = this.mActive.get(fragment5.mWho);
                        Fragment fragment6 = fragment2.mTarget;
                        if (obj == fragment6) {
                            if (fragment6.mState < 1) {
                                str3 = str7;
                                str2 = str6;
                                moveToState(fragment6, 1, 0, 0, true);
                            } else {
                                str3 = str7;
                                str2 = str6;
                            }
                            fragment2.mTargetWho = fragment2.mTarget.mWho;
                            fragment2.mTarget = null;
                        } else {
                            String str8 = str7;
                            String str9 = str6;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str8);
                            sb4.append(fragment2);
                            sb4.append(str9);
                            sb4.append(fragment2.mTarget);
                            sb4.append(str5);
                            throw new IllegalStateException(sb4.toString());
                        }
                    } else {
                        str3 = str7;
                        str2 = str6;
                    }
                    String str10 = fragment2.mTargetWho;
                    if (str10 != null) {
                        Fragment fragment7 = (Fragment) this.mActive.get(str10);
                        if (fragment7 == null) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(str3);
                            sb5.append(fragment2);
                            sb5.append(str2);
                            sb5.append(fragment2.mTargetWho);
                            sb5.append(str5);
                            throw new IllegalStateException(sb5.toString());
                        } else if (fragment7.mState < 1) {
                            moveToState(fragment7, 1, 0, 0, true);
                        }
                    }
                    dispatchOnFragmentPreAttached(fragment2, this.mHost.getContext(), false);
                    fragment.performAttach();
                    Fragment fragment8 = fragment2.mParentFragment;
                    if (fragment8 == null) {
                        this.mHost.onAttachFragment(fragment2);
                    } else {
                        fragment8.onAttachFragment(fragment2);
                    }
                    dispatchOnFragmentAttached(fragment2, this.mHost.getContext(), false);
                    if (!fragment2.mIsCreated) {
                        dispatchOnFragmentPreCreated(fragment2, fragment2.mSavedFragmentState, false);
                        fragment2.performCreate(fragment2.mSavedFragmentState);
                        dispatchOnFragmentCreated(fragment2, fragment2.mSavedFragmentState, false);
                    } else {
                        fragment2.restoreChildFragmentState(fragment2.mSavedFragmentState);
                        fragment2.mState = 1;
                        min = min;
                    }
                }
                if (min > 0) {
                    ensureInflatedFragmentView(fragment);
                }
                if (min > 1) {
                    if (DEBUG) {
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("moveto ACTIVITY_CREATED: ");
                        sb6.append(fragment2);
                        Log.v(str4, sb6.toString());
                    }
                    if (!fragment2.mFromLayout) {
                        int i9 = fragment2.mContainerId;
                        if (i9 == 0) {
                            viewGroup = null;
                        } else if (i9 != -1) {
                            viewGroup = (ViewGroup) this.mContainer.onFindViewById(i9);
                            if (viewGroup == null && !fragment2.mRestored) {
                                try {
                                    str = fragment.getResources().getResourceName(fragment2.mContainerId);
                                } catch (NotFoundException unused) {
                                    str = "unknown";
                                }
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append("No view found for id 0x");
                                sb7.append(Integer.toHexString(fragment2.mContainerId));
                                sb7.append(" (");
                                sb7.append(str);
                                sb7.append(") for fragment ");
                                sb7.append(fragment2);
                                throwException(new IllegalArgumentException(sb7.toString()));
                                throw null;
                            }
                        } else {
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("Cannot create fragment ");
                            sb8.append(fragment2);
                            sb8.append(" for a container view with no id");
                            throwException(new IllegalArgumentException(sb8.toString()));
                            throw null;
                        }
                        fragment2.mContainer = viewGroup;
                        fragment2.performCreateView(fragment2.performGetLayoutInflater(fragment2.mSavedFragmentState), viewGroup, fragment2.mSavedFragmentState);
                        View view = fragment2.mView;
                        if (view != null) {
                            fragment2.mInnerView = view;
                            view.setSaveFromParentEnabled(false);
                            if (viewGroup != null) {
                                viewGroup.addView(fragment2.mView);
                            }
                            if (fragment2.mHidden) {
                                fragment2.mView.setVisibility(8);
                            }
                            fragment2.onViewCreated(fragment2.mView, fragment2.mSavedFragmentState);
                            dispatchOnFragmentViewCreated(fragment2, fragment2.mView, fragment2.mSavedFragmentState, false);
                            if (fragment2.mView.getVisibility() != 0 || fragment2.mContainer == null) {
                                z3 = 0;
                            }
                            fragment2.mIsNewlyAdded = z3;
                        } else {
                            fragment2.mInnerView = null;
                        }
                    }
                    fragment2.performActivityCreated(fragment2.mSavedFragmentState);
                    dispatchOnFragmentActivityCreated(fragment2, fragment2.mSavedFragmentState, false);
                    if (fragment2.mView != null) {
                        fragment2.restoreViewState(fragment2.mSavedFragmentState);
                    }
                    fragment2.mSavedFragmentState = null;
                }
                c4 = 2;
                r11 = min;
                if (r11 > c4) {
                }
                c3 = 3;
                min = r11;
                if (min > c3) {
                }
            } else {
                return;
            }
        } else if (i6 > min) {
            if (i6 != 1) {
                if (i6 != 2) {
                    if (i6 == 3) {
                        c2 = 3;
                    } else if (i6 == 4) {
                        if (min < 4) {
                            if (DEBUG) {
                                StringBuilder sb9 = new StringBuilder();
                                sb9.append("movefrom RESUMED: ");
                                sb9.append(fragment2);
                                Log.v(str4, sb9.toString());
                            }
                            fragment.performPause();
                            dispatchOnFragmentPaused(fragment2, false);
                        }
                        c2 = 3;
                    }
                    if (min < c2) {
                        if (DEBUG) {
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append("movefrom STARTED: ");
                            sb10.append(fragment2);
                            Log.v(str4, sb10.toString());
                        }
                        fragment.performStop();
                        dispatchOnFragmentStopped(fragment2, false);
                    }
                    c = 2;
                } else {
                    c = 2;
                }
                if (min < c) {
                    if (DEBUG) {
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("movefrom ACTIVITY_CREATED: ");
                        sb11.append(fragment2);
                        Log.v(str4, sb11.toString());
                    }
                    if (fragment2.mView != null && this.mHost.onShouldSaveFragmentState(fragment2) && fragment2.mSavedViewState == null) {
                        saveFragmentViewState(fragment);
                    }
                    fragment.performDestroyView();
                    dispatchOnFragmentViewDestroyed(fragment2, false);
                    View view2 = fragment2.mView;
                    if (view2 != null) {
                        ViewGroup viewGroup2 = fragment2.mContainer;
                        if (viewGroup2 != null) {
                            viewGroup2.endViewTransition(view2);
                            fragment2.mView.clearAnimation();
                            AnimationOrAnimator loadAnimation = (this.mCurState <= 0 || this.mDestroyed || fragment2.mView.getVisibility() != 0 || fragment2.mPostponedAlpha < 0.0f) ? null : loadAnimation(fragment2, i2, false, i3);
                            fragment2.mPostponedAlpha = 0.0f;
                            if (loadAnimation != null) {
                                animateRemoveFragment(fragment2, loadAnimation, min);
                            }
                            fragment2.mContainer.removeView(fragment2.mView);
                        }
                    }
                    fragment2.mContainer = null;
                    fragment2.mView = null;
                    fragment2.mViewLifecycleOwner = null;
                    fragment2.mViewLifecycleOwnerLiveData.setValue(null);
                    fragment2.mInnerView = null;
                    fragment2.mInLayout = false;
                }
            }
            if (min < 1) {
                if (this.mDestroyed) {
                    if (fragment.getAnimatingAway() != null) {
                        View animatingAway = fragment.getAnimatingAway();
                        fragment2.setAnimatingAway(null);
                        animatingAway.clearAnimation();
                    } else if (fragment.getAnimator() != null) {
                        Animator animator = fragment.getAnimator();
                        fragment2.setAnimator(null);
                        animator.cancel();
                    }
                }
                if (fragment.getAnimatingAway() == null && fragment.getAnimator() == null) {
                    if (DEBUG) {
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("movefrom CREATED: ");
                        sb12.append(fragment2);
                        Log.v(str4, sb12.toString());
                    }
                    boolean z4 = fragment2.mRemoving && !fragment.isInBackStack();
                    if (z4 || this.mNonConfig.shouldDestroy(fragment2)) {
                        FragmentHostCallback fragmentHostCallback2 = this.mHost;
                        if (fragmentHostCallback2 instanceof ViewModelStoreOwner) {
                            z3 = this.mNonConfig.isCleared();
                        } else if (fragmentHostCallback2.getContext() instanceof Activity) {
                            z3 = true ^ ((Activity) this.mHost.getContext()).isChangingConfigurations();
                        }
                        if (z4 || z3 != 0) {
                            this.mNonConfig.clearNonConfigState(fragment2);
                        }
                        fragment.performDestroy();
                        dispatchOnFragmentDestroyed(fragment2, false);
                    } else {
                        fragment2.mState = 0;
                    }
                    fragment.performDetach();
                    dispatchOnFragmentDetached(fragment2, false);
                    if (!z) {
                        if (z4 || this.mNonConfig.shouldDestroy(fragment2)) {
                            makeInactive(fragment);
                        } else {
                            fragment2.mHost = null;
                            fragment2.mParentFragment = null;
                            fragment2.mFragmentManager = null;
                            String str11 = fragment2.mTargetWho;
                            if (str11 != null) {
                                Fragment fragment9 = (Fragment) this.mActive.get(str11);
                                if (fragment9 != null && fragment9.getRetainInstance()) {
                                    fragment2.mTarget = fragment9;
                                }
                            }
                        }
                    }
                } else {
                    fragment2.setStateAfterAnimating(min);
                    if (fragment2.mState != z2) {
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append("moveToState: Fragment state for ");
                        sb13.append(fragment2);
                        sb13.append(" not updated inline; expected state ");
                        sb13.append(z2);
                        sb13.append(" found ");
                        sb13.append(fragment2.mState);
                        Log.w(str4, sb13.toString());
                        fragment2.mState = z2;
                    }
                }
            }
        }
        z2 = min;
        if (fragment2.mState != z2) {
        }
    }

    public void noteStateNotSaved() {
        this.mStateSaved = false;
        this.mStopped = false;
        int size = this.mAdded.size();
        for (int i = 0; i < size; i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }

    @Nullable
    public View onCreateView(@Nullable View view, @NonNull String str, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        Fragment fragment;
        Fragment fragment2 = null;
        if (!"fragment".equals(str)) {
            return null;
        }
        String attributeValue = attributeSet.getAttributeValue(null, "class");
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, FragmentTag.Fragment);
        int i = 0;
        if (attributeValue == null) {
            attributeValue = obtainStyledAttributes.getString(0);
        }
        String str2 = attributeValue;
        int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        String string = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (str2 == null || !FragmentFactory.isFragmentClass(context.getClassLoader(), str2)) {
            return null;
        }
        if (view != null) {
            i = view.getId();
        }
        if (i == -1 && resourceId == -1 && string == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(attributeSet.getPositionDescription());
            sb.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            sb.append(str2);
            throw new IllegalArgumentException(sb.toString());
        }
        if (resourceId != -1) {
            fragment2 = findFragmentById(resourceId);
        }
        if (fragment2 == null && string != null) {
            fragment2 = findFragmentByTag(string);
        }
        if (fragment2 == null && i != -1) {
            fragment2 = findFragmentById(i);
        }
        if (DEBUG) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onCreateView: id=0x");
            sb2.append(Integer.toHexString(resourceId));
            sb2.append(" fname=");
            sb2.append(str2);
            sb2.append(" existing=");
            sb2.append(fragment2);
            Log.v(TAG, sb2.toString());
        }
        if (fragment2 == null) {
            Fragment instantiate = getFragmentFactory().instantiate(context.getClassLoader(), str2);
            instantiate.mFromLayout = true;
            instantiate.mFragmentId = resourceId != 0 ? resourceId : i;
            instantiate.mContainerId = i;
            instantiate.mTag = string;
            instantiate.mInLayout = true;
            instantiate.mFragmentManager = this;
            FragmentHostCallback fragmentHostCallback = this.mHost;
            instantiate.mHost = fragmentHostCallback;
            instantiate.onInflate(fragmentHostCallback.getContext(), attributeSet, instantiate.mSavedFragmentState);
            addFragment(instantiate, true);
            fragment = instantiate;
        } else if (!fragment2.mInLayout) {
            fragment2.mInLayout = true;
            FragmentHostCallback fragmentHostCallback2 = this.mHost;
            fragment2.mHost = fragmentHostCallback2;
            fragment2.onInflate(fragmentHostCallback2.getContext(), attributeSet, fragment2.mSavedFragmentState);
            fragment = fragment2;
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(attributeSet.getPositionDescription());
            sb3.append(": Duplicate id 0x");
            sb3.append(Integer.toHexString(resourceId));
            sb3.append(", tag ");
            sb3.append(string);
            sb3.append(", or parent id 0x");
            sb3.append(Integer.toHexString(i));
            sb3.append(" with another fragment for ");
            sb3.append(str2);
            throw new IllegalArgumentException(sb3.toString());
        }
        if (this.mCurState >= 1 || !fragment.mFromLayout) {
            moveToState(fragment);
        } else {
            moveToState(fragment, 1, 0, 0, false);
        }
        View view2 = fragment.mView;
        if (view2 != null) {
            if (resourceId != 0) {
                view2.setId(resourceId);
            }
            if (fragment.mView.getTag() == null) {
                fragment.mView.setTag(string);
            }
            return fragment.mView;
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Fragment ");
        sb4.append(str2);
        sb4.append(" did not create a view.");
        throw new IllegalStateException(sb4.toString());
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    public void performPendingDeferredStart(Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            moveToState(fragment, this.mCurState, 0, 0, false);
        }
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState(null, -1, 0), false);
    }

    public void popBackStack(int i, int i2) {
        if (i >= 0) {
            enqueueAction(new PopBackStackState(null, i, i2), false);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Bad id: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    public void popBackStack(@Nullable String str, int i) {
        enqueueAction(new PopBackStackState(str, -1, i), false);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        return popBackStackImmediate(null, -1, 0);
    }

    public boolean popBackStackImmediate(int i, int i2) {
        checkStateLoss();
        execPendingActions();
        if (i >= 0) {
            return popBackStackImmediate(null, i, i2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Bad id: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    public boolean popBackStackImmediate(@Nullable String str, int i) {
        checkStateLoss();
        return popBackStackImmediate(str, -1, i);
    }

    /* access modifiers changed from: 0000 */
    public boolean popBackStackState(ArrayList arrayList, ArrayList arrayList2, String str, int i, int i2) {
        int i3;
        int i4;
        ArrayList arrayList3 = this.mBackStack;
        if (arrayList3 == null) {
            return false;
        }
        if (str == null && i < 0 && (i2 & 1) == 0) {
            int size = arrayList3.size() - 1;
            if (size < 0) {
                return false;
            }
            arrayList.add(this.mBackStack.remove(size));
            arrayList2.add(Boolean.valueOf(true));
        } else {
            if (str != null || i >= 0) {
                i3 = this.mBackStack.size() - 1;
                while (i3 >= 0) {
                    BackStackRecord backStackRecord = (BackStackRecord) this.mBackStack.get(i3);
                    if ((str != null && str.equals(backStackRecord.getName())) || (i >= 0 && i == backStackRecord.mIndex)) {
                        break;
                    }
                    i3--;
                }
                if (i3 < 0) {
                    return false;
                }
                if ((i2 & 1) != 0) {
                    while (true) {
                        i3 = i4 - 1;
                        if (i3 < 0) {
                            break;
                        }
                        BackStackRecord backStackRecord2 = (BackStackRecord) this.mBackStack.get(i3);
                        if (str == null || !str.equals(backStackRecord2.getName())) {
                            if (i < 0 || i != backStackRecord2.mIndex) {
                                break;
                            }
                        }
                    }
                }
            } else {
                i3 = -1;
            }
            if (i3 == this.mBackStack.size() - 1) {
                return false;
            }
            for (int size2 = this.mBackStack.size() - 1; size2 > i3; size2--) {
                arrayList.add(this.mBackStack.remove(size2));
                arrayList2.add(Boolean.valueOf(true));
            }
        }
        return true;
    }

    public void putFragment(Bundle bundle, String str, Fragment fragment) {
        if (fragment.mFragmentManager == this) {
            bundle.putString(str, fragment.mWho);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(fragment);
        sb.append(" is not currently in the FragmentManager");
        throwException(new IllegalStateException(sb.toString()));
        throw null;
    }

    public void registerFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean z) {
        this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(fragmentLifecycleCallbacks, z));
    }

    public void removeFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("remove: ");
            sb.append(fragment);
            sb.append(" nesting=");
            sb.append(fragment.mBackStackNesting);
            Log.v(TAG, sb.toString());
        }
        boolean z = !fragment.isInBackStack();
        if (!fragment.mDetached || z) {
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        ArrayList arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(onBackStackChangedListener);
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeRetainedFragment(@NonNull Fragment fragment) {
        boolean isStateSaved = isStateSaved();
        String str = TAG;
        if (isStateSaved) {
            if (DEBUG) {
                Log.v(str, "Ignoring removeRetainedFragment as the state is already saved");
            }
            return;
        }
        if (this.mNonConfig.removeRetainedFragment(fragment) && DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Updating retained Fragments: Removed ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                ((OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void restoreAllState(Parcelable parcelable, FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (!(this.mHost instanceof ViewModelStoreOwner)) {
            this.mNonConfig.restoreFromSnapshot(fragmentManagerNonConfig);
            restoreSaveState(parcelable);
            return;
        }
        throwException(new IllegalStateException("You must use restoreSaveState when your FragmentHostCallback implements ViewModelStoreOwner"));
        throw null;
    }

    /* access modifiers changed from: 0000 */
    public void restoreSaveState(Parcelable parcelable) {
        FragmentState fragmentState;
        if (parcelable != null) {
            FragmentManagerState fragmentManagerState = (FragmentManagerState) parcelable;
            if (fragmentManagerState.mActive != null) {
                for (Fragment fragment : this.mNonConfig.getRetainedFragments()) {
                    if (DEBUG) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("restoreSaveState: re-attaching retained ");
                        sb.append(fragment);
                        Log.v(TAG, sb.toString());
                    }
                    Iterator it = fragmentManagerState.mActive.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            fragmentState = null;
                            break;
                        }
                        fragmentState = (FragmentState) it.next();
                        if (fragmentState.mWho.equals(fragment.mWho)) {
                            break;
                        }
                    }
                    if (fragmentState == null) {
                        if (DEBUG) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Discarding retained Fragment ");
                            sb2.append(fragment);
                            sb2.append(" that was not found in the set of active Fragments ");
                            sb2.append(fragmentManagerState.mActive);
                            Log.v(TAG, sb2.toString());
                        }
                        Fragment fragment2 = fragment;
                        moveToState(fragment2, 1, 0, 0, false);
                        fragment.mRemoving = true;
                        moveToState(fragment2, 0, 0, 0, false);
                    } else {
                        fragmentState.mInstance = fragment;
                        fragment.mSavedViewState = null;
                        fragment.mBackStackNesting = 0;
                        fragment.mInLayout = false;
                        fragment.mAdded = false;
                        Fragment fragment3 = fragment.mTarget;
                        fragment.mTargetWho = fragment3 != null ? fragment3.mWho : null;
                        fragment.mTarget = null;
                        Bundle bundle = fragmentState.mSavedFragmentState;
                        if (bundle != null) {
                            bundle.setClassLoader(this.mHost.getContext().getClassLoader());
                            fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                            fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
                        }
                    }
                }
                this.mActive.clear();
                Iterator it2 = fragmentManagerState.mActive.iterator();
                while (it2.hasNext()) {
                    FragmentState fragmentState2 = (FragmentState) it2.next();
                    if (fragmentState2 != null) {
                        Fragment instantiate = fragmentState2.instantiate(this.mHost.getContext().getClassLoader(), getFragmentFactory());
                        instantiate.mFragmentManager = this;
                        if (DEBUG) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("restoreSaveState: active (");
                            sb3.append(instantiate.mWho);
                            sb3.append("): ");
                            sb3.append(instantiate);
                            Log.v(TAG, sb3.toString());
                        }
                        this.mActive.put(instantiate.mWho, instantiate);
                        fragmentState2.mInstance = null;
                    }
                }
                this.mAdded.clear();
                ArrayList arrayList = fragmentManagerState.mAdded;
                if (arrayList != null) {
                    Iterator it3 = arrayList.iterator();
                    while (it3.hasNext()) {
                        String str = (String) it3.next();
                        Fragment fragment4 = (Fragment) this.mActive.get(str);
                        if (fragment4 != null) {
                            fragment4.mAdded = true;
                            if (DEBUG) {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("restoreSaveState: added (");
                                sb4.append(str);
                                sb4.append("): ");
                                sb4.append(fragment4);
                                Log.v(TAG, sb4.toString());
                            }
                            if (!this.mAdded.contains(fragment4)) {
                                synchronized (this.mAdded) {
                                    this.mAdded.add(fragment4);
                                }
                            } else {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("Already added ");
                                sb5.append(fragment4);
                                throw new IllegalStateException(sb5.toString());
                            }
                        } else {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("No instantiated fragment for (");
                            sb6.append(str);
                            sb6.append(")");
                            throwException(new IllegalStateException(sb6.toString()));
                            throw null;
                        }
                    }
                }
                BackStackState[] backStackStateArr = fragmentManagerState.mBackStack;
                if (backStackStateArr != null) {
                    this.mBackStack = new ArrayList(backStackStateArr.length);
                    int i = 0;
                    while (true) {
                        BackStackState[] backStackStateArr2 = fragmentManagerState.mBackStack;
                        if (i >= backStackStateArr2.length) {
                            break;
                        }
                        BackStackRecord instantiate2 = backStackStateArr2[i].instantiate(this);
                        if (DEBUG) {
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("restoreAllState: back stack #");
                            sb7.append(i);
                            sb7.append(" (index ");
                            sb7.append(instantiate2.mIndex);
                            sb7.append("): ");
                            sb7.append(instantiate2);
                            Log.v(TAG, sb7.toString());
                            PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
                            instantiate2.dump("  ", printWriter, false);
                            printWriter.close();
                        }
                        this.mBackStack.add(instantiate2);
                        int i2 = instantiate2.mIndex;
                        if (i2 >= 0) {
                            setBackStackIndex(i2, instantiate2);
                        }
                        i++;
                    }
                } else {
                    this.mBackStack = null;
                }
                String str2 = fragmentManagerState.mPrimaryNavActiveWho;
                if (str2 != null) {
                    this.mPrimaryNav = (Fragment) this.mActive.get(str2);
                    dispatchOnParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
                }
                this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    public FragmentManagerNonConfig retainNonConfig() {
        if (!(this.mHost instanceof ViewModelStoreOwner)) {
            return this.mNonConfig.getSnapshot();
        }
        throwException(new IllegalStateException("You cannot use retainNonConfig when your FragmentHostCallback implements ViewModelStoreOwner."));
        throw null;
    }

    /* access modifiers changed from: 0000 */
    public Parcelable saveAllState() {
        ArrayList arrayList;
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions();
        this.mStateSaved = true;
        BackStackState[] backStackStateArr = null;
        if (this.mActive.isEmpty()) {
            return null;
        }
        ArrayList arrayList2 = new ArrayList(this.mActive.size());
        Iterator it = this.mActive.values().iterator();
        boolean z = false;
        while (true) {
            boolean hasNext = it.hasNext();
            String str = ": ";
            String str2 = " was removed from the FragmentManager";
            String str3 = "Failure saving state: active ";
            String str4 = TAG;
            if (hasNext) {
                Fragment fragment = (Fragment) it.next();
                if (fragment != null) {
                    if (fragment.mFragmentManager == this) {
                        FragmentState fragmentState = new FragmentState(fragment);
                        arrayList2.add(fragmentState);
                        if (fragment.mState <= 0 || fragmentState.mSavedFragmentState != null) {
                            fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
                        } else {
                            fragmentState.mSavedFragmentState = saveFragmentBasicState(fragment);
                            String str5 = fragment.mTargetWho;
                            if (str5 != null) {
                                Fragment fragment2 = (Fragment) this.mActive.get(str5);
                                if (fragment2 != null) {
                                    if (fragmentState.mSavedFragmentState == null) {
                                        fragmentState.mSavedFragmentState = new Bundle();
                                    }
                                    putFragment(fragmentState.mSavedFragmentState, TARGET_STATE_TAG, fragment2);
                                    int i = fragment.mTargetRequestCode;
                                    if (i != 0) {
                                        fragmentState.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, i);
                                    }
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Failure saving state: ");
                                    sb.append(fragment);
                                    sb.append(" has target not in fragment manager: ");
                                    sb.append(fragment.mTargetWho);
                                    throwException(new IllegalStateException(sb.toString()));
                                    throw null;
                                }
                            }
                        }
                        if (DEBUG) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Saved state of ");
                            sb2.append(fragment);
                            sb2.append(str);
                            sb2.append(fragmentState.mSavedFragmentState);
                            Log.v(str4, sb2.toString());
                        }
                        z = true;
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str3);
                        sb3.append(fragment);
                        sb3.append(str2);
                        throwException(new IllegalStateException(sb3.toString()));
                        throw null;
                    }
                }
            } else if (!z) {
                if (DEBUG) {
                    Log.v(str4, "saveAllState: no fragments!");
                }
                return null;
            } else {
                int size = this.mAdded.size();
                if (size > 0) {
                    arrayList = new ArrayList(size);
                    Iterator it2 = this.mAdded.iterator();
                    while (it2.hasNext()) {
                        Fragment fragment3 = (Fragment) it2.next();
                        arrayList.add(fragment3.mWho);
                        if (fragment3.mFragmentManager != this) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str3);
                            sb4.append(fragment3);
                            sb4.append(str2);
                            throwException(new IllegalStateException(sb4.toString()));
                            throw null;
                        } else if (DEBUG) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("saveAllState: adding fragment (");
                            sb5.append(fragment3.mWho);
                            sb5.append("): ");
                            sb5.append(fragment3);
                            Log.v(str4, sb5.toString());
                        }
                    }
                } else {
                    arrayList = null;
                }
                ArrayList arrayList3 = this.mBackStack;
                if (arrayList3 != null) {
                    int size2 = arrayList3.size();
                    if (size2 > 0) {
                        backStackStateArr = new BackStackState[size2];
                        for (int i2 = 0; i2 < size2; i2++) {
                            backStackStateArr[i2] = new BackStackState((BackStackRecord) this.mBackStack.get(i2));
                            if (DEBUG) {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("saveAllState: adding back stack #");
                                sb6.append(i2);
                                sb6.append(str);
                                sb6.append(this.mBackStack.get(i2));
                                Log.v(str4, sb6.toString());
                            }
                        }
                    }
                }
                FragmentManagerState fragmentManagerState = new FragmentManagerState();
                fragmentManagerState.mActive = arrayList2;
                fragmentManagerState.mAdded = arrayList;
                fragmentManagerState.mBackStack = backStackStateArr;
                Fragment fragment4 = this.mPrimaryNav;
                if (fragment4 != null) {
                    fragmentManagerState.mPrimaryNavActiveWho = fragment4.mWho;
                }
                fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
                return fragmentManagerState;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Bundle saveFragmentBasicState(Fragment fragment) {
        Bundle bundle;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            bundle = this.mStateBundle;
            this.mStateBundle = null;
        } else {
            bundle = null;
        }
        if (fragment.mView != null) {
            saveFragmentViewState(fragment);
        }
        if (fragment.mSavedViewState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(VIEW_STATE_TAG, fragment.mSavedViewState);
        }
        if (!fragment.mUserVisibleHint) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean(USER_VISIBLE_HINT_TAG, fragment.mUserVisibleHint);
        }
        return bundle;
    }

    @Nullable
    public SavedState saveFragmentInstanceState(@NonNull Fragment fragment) {
        if (fragment.mFragmentManager != this) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            throwException(new IllegalStateException(sb.toString()));
            throw null;
        } else if (fragment.mState <= 0) {
            return null;
        } else {
            Bundle saveFragmentBasicState = saveFragmentBasicState(fragment);
            if (saveFragmentBasicState != null) {
                return new SavedState(saveFragmentBasicState);
            }
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void saveFragmentViewState(Fragment fragment) {
        if (fragment.mInnerView != null) {
            SparseArray sparseArray = this.mStateArray;
            if (sparseArray == null) {
                this.mStateArray = new SparseArray();
            } else {
                sparseArray.clear();
            }
            fragment.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                fragment.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void scheduleCommit() {
        synchronized (this) {
            boolean z = false;
            boolean z2 = this.mPostponedTransactions != null && !this.mPostponedTransactions.isEmpty();
            if (this.mPendingActions != null && this.mPendingActions.size() == 1) {
                z = true;
            }
            if (z2 || z) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
                updateOnBackPressedCallbackEnabled();
            }
        }
    }

    public void setBackStackIndex(int i, BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int size = this.mBackStackIndices.size();
            if (i < size) {
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Setting back stack index ");
                    sb.append(i);
                    sb.append(" to ");
                    sb.append(backStackRecord);
                    Log.v(str, sb.toString());
                }
                this.mBackStackIndices.set(i, backStackRecord);
            } else {
                while (size < i) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList();
                    }
                    if (DEBUG) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Adding available back stack index ");
                        sb2.append(size);
                        Log.v(str2, sb2.toString());
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(size));
                    size++;
                }
                if (DEBUG) {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Adding back stack index ");
                    sb3.append(i);
                    sb3.append(" with ");
                    sb3.append(backStackRecord);
                    Log.v(str3, sb3.toString());
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
    }

    public void setMaxLifecycle(Fragment fragment, State state) {
        if (this.mActive.get(fragment.mWho) == fragment && (fragment.mHost == null || fragment.getFragmentManager() == this)) {
            fragment.mMaxState = state;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(fragment);
        sb.append(" is not an active fragment of FragmentManager ");
        sb.append(this);
        throw new IllegalArgumentException(sb.toString());
    }

    public void setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment == null || (this.mActive.get(fragment.mWho) == fragment && (fragment.mHost == null || fragment.getFragmentManager() == this))) {
            Fragment fragment2 = this.mPrimaryNav;
            this.mPrimaryNav = fragment;
            dispatchOnParentPrimaryNavigationFragmentChanged(fragment2);
            dispatchOnParentPrimaryNavigationFragmentChanged(this.mPrimaryNav);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(fragment);
        sb.append(" is not an active fragment of FragmentManager ");
        sb.append(this);
        throw new IllegalArgumentException(sb.toString());
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("show: ");
            sb.append(fragment);
            Log.v(TAG, sb.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    /* access modifiers changed from: 0000 */
    public void startPendingDeferredFragments() {
        for (Fragment fragment : this.mActive.values()) {
            if (fragment != null) {
                performPendingDeferredStart(fragment);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            DebugUtils.buildShortClassTag(fragment, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    public void unregisterFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        synchronized (this.mLifecycleCallbacks) {
            int i = 0;
            int size = this.mLifecycleCallbacks.size();
            while (true) {
                if (i >= size) {
                    break;
                } else if (((FragmentLifecycleCallbacksHolder) this.mLifecycleCallbacks.get(i)).mCallback == fragmentLifecycleCallbacks) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        }
    }
}

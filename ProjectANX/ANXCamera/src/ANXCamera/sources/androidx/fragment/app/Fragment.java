package androidx.fragment.app;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.annotation.CallSuper;
import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.StringRes;
import androidx.core.app.SharedElementCallback;
import androidx.core.util.DebugUtils;
import androidx.core.view.LayoutInflaterCompat;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Fragment implements ComponentCallbacks, OnCreateContextMenuListener, LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {
    static final int ACTIVITY_CREATED = 2;
    static final int CREATED = 1;
    static final int INITIALIZING = 0;
    static final int RESUMED = 4;
    static final int STARTED = 3;
    static final Object USE_DEFAULT_TRANSITION = new Object();
    boolean mAdded;
    AnimationInfo mAnimationInfo;
    Bundle mArguments;
    int mBackStackNesting;
    private boolean mCalled;
    FragmentManagerImpl mChildFragmentManager;
    ViewGroup mContainer;
    int mContainerId;
    @LayoutRes
    private int mContentLayoutId;
    boolean mDeferStart;
    boolean mDetached;
    int mFragmentId;
    FragmentManagerImpl mFragmentManager;
    boolean mFromLayout;
    boolean mHasMenu;
    boolean mHidden;
    boolean mHiddenChanged;
    FragmentHostCallback mHost;
    boolean mInLayout;
    View mInnerView;
    boolean mIsCreated;
    boolean mIsNewlyAdded;
    LayoutInflater mLayoutInflater;
    LifecycleRegistry mLifecycleRegistry;
    State mMaxState;
    boolean mMenuVisible;
    Fragment mParentFragment;
    boolean mPerformedCreateView;
    float mPostponedAlpha;
    boolean mRemoving;
    boolean mRestored;
    boolean mRetainInstance;
    boolean mRetainInstanceChangedWhileDetached;
    Bundle mSavedFragmentState;
    SavedStateRegistryController mSavedStateRegistryController;
    @Nullable
    Boolean mSavedUserVisibleHint;
    SparseArray mSavedViewState;
    int mState;
    String mTag;
    Fragment mTarget;
    int mTargetRequestCode;
    String mTargetWho;
    boolean mUserVisibleHint;
    View mView;
    @Nullable
    FragmentViewLifecycleOwner mViewLifecycleOwner;
    MutableLiveData mViewLifecycleOwnerLiveData;
    @NonNull
    String mWho;

    class AnimationInfo {
        Boolean mAllowEnterTransitionOverlap;
        Boolean mAllowReturnTransitionOverlap;
        View mAnimatingAway;
        Animator mAnimator;
        Object mEnterTransition = null;
        SharedElementCallback mEnterTransitionCallback;
        boolean mEnterTransitionPostponed;
        Object mExitTransition;
        SharedElementCallback mExitTransitionCallback;
        boolean mIsHideReplaced;
        int mNextAnim;
        int mNextTransition;
        int mNextTransitionStyle;
        Object mReenterTransition;
        Object mReturnTransition;
        Object mSharedElementEnterTransition;
        Object mSharedElementReturnTransition;
        OnStartEnterTransitionListener mStartEnterTransitionListener;
        int mStateAfterAnimating;

        AnimationInfo() {
            Object obj = Fragment.USE_DEFAULT_TRANSITION;
            this.mReturnTransition = obj;
            this.mExitTransition = null;
            this.mReenterTransition = obj;
            this.mSharedElementEnterTransition = null;
            this.mSharedElementReturnTransition = obj;
            this.mEnterTransitionCallback = null;
            this.mExitTransitionCallback = null;
        }
    }

    public class InstantiationException extends RuntimeException {
        public InstantiationException(@NonNull String str, @Nullable Exception exc) {
            super(str, exc);
        }
    }

    interface OnStartEnterTransitionListener {
        void onStartEnterTransition();

        void startListening();
    }

    @SuppressLint({"BanParcelableUsage"})
    public class SavedState implements Parcelable {
        @NonNull
        public static final Creator CREATOR = new ClassLoaderCreator() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        final Bundle mState;

        SavedState(Bundle bundle) {
            this.mState = bundle;
        }

        SavedState(@NonNull Parcel parcel, @Nullable ClassLoader classLoader) {
            this.mState = parcel.readBundle();
            if (classLoader != null) {
                Bundle bundle = this.mState;
                if (bundle != null) {
                    bundle.setClassLoader(classLoader);
                }
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeBundle(this.mState);
        }
    }

    public Fragment() {
        this.mState = 0;
        this.mWho = UUID.randomUUID().toString();
        this.mTargetWho = null;
        this.mMenuVisible = true;
        this.mUserVisibleHint = true;
        this.mMaxState = State.RESUMED;
        this.mViewLifecycleOwnerLiveData = new MutableLiveData();
        initLifecycle();
    }

    @ContentView
    public Fragment(@LayoutRes int i) {
        this();
        this.mContentLayoutId = i;
    }

    private AnimationInfo ensureAnimationInfo() {
        if (this.mAnimationInfo == null) {
            this.mAnimationInfo = new AnimationInfo();
        }
        return this.mAnimationInfo;
    }

    private void initLifecycle() {
        this.mLifecycleRegistry = new LifecycleRegistry(this);
        this.mSavedStateRegistryController = SavedStateRegistryController.create(this);
        if (VERSION.SDK_INT >= 19) {
            this.mLifecycleRegistry.addObserver(new GenericLifecycleObserver() {
                public void onStateChanged(LifecycleOwner lifecycleOwner, Event event) {
                    if (event == Event.ON_STOP) {
                        View view = Fragment.this.mView;
                        if (view != null) {
                            view.cancelPendingInputEvents();
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Deprecated
    public static Fragment instantiate(@NonNull Context context, @NonNull String str) {
        return instantiate(context, str, null);
    }

    @NonNull
    @Deprecated
    public static Fragment instantiate(@NonNull Context context, @NonNull String str, @Nullable Bundle bundle) {
        String str2 = ": make sure class name exists, is public, and has an empty constructor that is public";
        String str3 = "Unable to instantiate fragment ";
        try {
            Fragment fragment = (Fragment) FragmentFactory.loadFragmentClass(context.getClassLoader(), str).getConstructor(new Class[0]).newInstance(new Object[0]);
            if (bundle != null) {
                bundle.setClassLoader(fragment.getClass().getClassLoader());
                fragment.setArguments(bundle);
            }
            return fragment;
        } catch (InstantiationException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(str3);
            sb.append(str);
            sb.append(str2);
            throw new InstantiationException(sb.toString(), e);
        } catch (IllegalAccessException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str3);
            sb2.append(str);
            sb2.append(str2);
            throw new InstantiationException(sb2.toString(), e2);
        } catch (NoSuchMethodException e3) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(str);
            sb3.append(": could not find Fragment constructor");
            throw new InstantiationException(sb3.toString(), e3);
        } catch (InvocationTargetException e4) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str3);
            sb4.append(str);
            sb4.append(": calling Fragment constructor caused an exception");
            throw new InstantiationException(sb4.toString(), e4);
        }
    }

    /* access modifiers changed from: 0000 */
    public void callStartTransitionListener() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        OnStartEnterTransitionListener onStartEnterTransitionListener = null;
        if (animationInfo != null) {
            animationInfo.mEnterTransitionPostponed = false;
            OnStartEnterTransitionListener onStartEnterTransitionListener2 = animationInfo.mStartEnterTransitionListener;
            animationInfo.mStartEnterTransitionListener = null;
            onStartEnterTransitionListener = onStartEnterTransitionListener2;
        }
        if (onStartEnterTransitionListener != null) {
            onStartEnterTransitionListener.onStartEnterTransition();
        }
    }

    public void dump(@NonNull String str, @Nullable FileDescriptor fileDescriptor, @NonNull PrintWriter printWriter, @Nullable String[] strArr) {
        printWriter.print(str);
        printWriter.print("mFragmentId=#");
        printWriter.print(Integer.toHexString(this.mFragmentId));
        printWriter.print(" mContainerId=#");
        printWriter.print(Integer.toHexString(this.mContainerId));
        printWriter.print(" mTag=");
        printWriter.println(this.mTag);
        printWriter.print(str);
        printWriter.print("mState=");
        printWriter.print(this.mState);
        printWriter.print(" mWho=");
        printWriter.print(this.mWho);
        printWriter.print(" mBackStackNesting=");
        printWriter.println(this.mBackStackNesting);
        printWriter.print(str);
        printWriter.print("mAdded=");
        printWriter.print(this.mAdded);
        printWriter.print(" mRemoving=");
        printWriter.print(this.mRemoving);
        printWriter.print(" mFromLayout=");
        printWriter.print(this.mFromLayout);
        printWriter.print(" mInLayout=");
        printWriter.println(this.mInLayout);
        printWriter.print(str);
        printWriter.print("mHidden=");
        printWriter.print(this.mHidden);
        printWriter.print(" mDetached=");
        printWriter.print(this.mDetached);
        printWriter.print(" mMenuVisible=");
        printWriter.print(this.mMenuVisible);
        printWriter.print(" mHasMenu=");
        printWriter.println(this.mHasMenu);
        printWriter.print(str);
        printWriter.print("mRetainInstance=");
        printWriter.print(this.mRetainInstance);
        printWriter.print(" mUserVisibleHint=");
        printWriter.println(this.mUserVisibleHint);
        if (this.mFragmentManager != null) {
            printWriter.print(str);
            printWriter.print("mFragmentManager=");
            printWriter.println(this.mFragmentManager);
        }
        if (this.mHost != null) {
            printWriter.print(str);
            printWriter.print("mHost=");
            printWriter.println(this.mHost);
        }
        if (this.mParentFragment != null) {
            printWriter.print(str);
            printWriter.print("mParentFragment=");
            printWriter.println(this.mParentFragment);
        }
        if (this.mArguments != null) {
            printWriter.print(str);
            printWriter.print("mArguments=");
            printWriter.println(this.mArguments);
        }
        if (this.mSavedFragmentState != null) {
            printWriter.print(str);
            printWriter.print("mSavedFragmentState=");
            printWriter.println(this.mSavedFragmentState);
        }
        if (this.mSavedViewState != null) {
            printWriter.print(str);
            printWriter.print("mSavedViewState=");
            printWriter.println(this.mSavedViewState);
        }
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            printWriter.print(str);
            printWriter.print("mTarget=");
            printWriter.print(targetFragment);
            printWriter.print(" mTargetRequestCode=");
            printWriter.println(this.mTargetRequestCode);
        }
        if (getNextAnim() != 0) {
            printWriter.print(str);
            printWriter.print("mNextAnim=");
            printWriter.println(getNextAnim());
        }
        if (this.mContainer != null) {
            printWriter.print(str);
            printWriter.print("mContainer=");
            printWriter.println(this.mContainer);
        }
        if (this.mView != null) {
            printWriter.print(str);
            printWriter.print("mView=");
            printWriter.println(this.mView);
        }
        if (this.mInnerView != null) {
            printWriter.print(str);
            printWriter.print("mInnerView=");
            printWriter.println(this.mView);
        }
        if (getAnimatingAway() != null) {
            printWriter.print(str);
            printWriter.print("mAnimatingAway=");
            printWriter.println(getAnimatingAway());
            printWriter.print(str);
            printWriter.print("mStateAfterAnimating=");
            printWriter.println(getStateAfterAnimating());
        }
        if (getContext() != null) {
            LoaderManager.getInstance(this).dump(str, fileDescriptor, printWriter, strArr);
        }
        if (this.mChildFragmentManager != null) {
            printWriter.print(str);
            StringBuilder sb = new StringBuilder();
            sb.append("Child ");
            sb.append(this.mChildFragmentManager);
            sb.append(":");
            printWriter.println(sb.toString());
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("  ");
            fragmentManagerImpl.dump(sb2.toString(), fileDescriptor, printWriter, strArr);
        }
    }

    public final boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Fragment findFragmentByWho(@NonNull String str) {
        if (str.equals(this.mWho)) {
            return this;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            return fragmentManagerImpl.findFragmentByWho(str);
        }
        return null;
    }

    @Nullable
    public final FragmentActivity getActivity() {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null) {
            return null;
        }
        return (FragmentActivity) fragmentHostCallback.getActivity();
    }

    public boolean getAllowEnterTransitionOverlap() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo != null) {
            Boolean bool = animationInfo.mAllowEnterTransitionOverlap;
            if (bool != null) {
                return bool.booleanValue();
            }
        }
        return true;
    }

    public boolean getAllowReturnTransitionOverlap() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo != null) {
            Boolean bool = animationInfo.mAllowReturnTransitionOverlap;
            if (bool != null) {
                return bool.booleanValue();
            }
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public View getAnimatingAway() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mAnimatingAway;
    }

    /* access modifiers changed from: 0000 */
    public Animator getAnimator() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mAnimator;
    }

    @Nullable
    public final Bundle getArguments() {
        return this.mArguments;
    }

    @NonNull
    public final FragmentManager getChildFragmentManager() {
        if (this.mChildFragmentManager == null) {
            instantiateChildFragmentManager();
            int i = this.mState;
            if (i >= 4) {
                this.mChildFragmentManager.dispatchResume();
            } else if (i >= 3) {
                this.mChildFragmentManager.dispatchStart();
            } else if (i >= 2) {
                this.mChildFragmentManager.dispatchActivityCreated();
            } else if (i >= 1) {
                this.mChildFragmentManager.dispatchCreate();
            }
        }
        return this.mChildFragmentManager;
    }

    @Nullable
    public Context getContext() {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null) {
            return null;
        }
        return fragmentHostCallback.getContext();
    }

    @Nullable
    public Object getEnterTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mEnterTransition;
    }

    /* access modifiers changed from: 0000 */
    public SharedElementCallback getEnterTransitionCallback() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mEnterTransitionCallback;
    }

    @Nullable
    public Object getExitTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mExitTransition;
    }

    /* access modifiers changed from: 0000 */
    public SharedElementCallback getExitTransitionCallback() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mExitTransitionCallback;
    }

    @Nullable
    public final FragmentManager getFragmentManager() {
        return this.mFragmentManager;
    }

    @Nullable
    public final Object getHost() {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback == null) {
            return null;
        }
        return fragmentHostCallback.onGetHost();
    }

    public final int getId() {
        return this.mFragmentId;
    }

    @NonNull
    public final LayoutInflater getLayoutInflater() {
        LayoutInflater layoutInflater = this.mLayoutInflater;
        return layoutInflater == null ? performGetLayoutInflater(null) : layoutInflater;
    }

    @NonNull
    @Deprecated
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public LayoutInflater getLayoutInflater(@Nullable Bundle bundle) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            LayoutInflater onGetLayoutInflater = fragmentHostCallback.onGetLayoutInflater();
            getChildFragmentManager();
            LayoutInflaterCompat.setFactory2(onGetLayoutInflater, this.mChildFragmentManager.getLayoutInflaterFactory());
            return onGetLayoutInflater;
        }
        throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
    }

    @NonNull
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @NonNull
    @Deprecated
    public LoaderManager getLoaderManager() {
        return LoaderManager.getInstance(this);
    }

    /* access modifiers changed from: 0000 */
    public int getNextAnim() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return 0;
        }
        return animationInfo.mNextAnim;
    }

    /* access modifiers changed from: 0000 */
    public int getNextTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return 0;
        }
        return animationInfo.mNextTransition;
    }

    /* access modifiers changed from: 0000 */
    public int getNextTransitionStyle() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return 0;
        }
        return animationInfo.mNextTransitionStyle;
    }

    @Nullable
    public final Fragment getParentFragment() {
        return this.mParentFragment;
    }

    @Nullable
    public Object getReenterTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        Object obj = animationInfo.mReenterTransition;
        if (obj == USE_DEFAULT_TRANSITION) {
            obj = getExitTransition();
        }
        return obj;
    }

    @NonNull
    public final Resources getResources() {
        return requireContext().getResources();
    }

    public final boolean getRetainInstance() {
        return this.mRetainInstance;
    }

    @Nullable
    public Object getReturnTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        Object obj = animationInfo.mReturnTransition;
        if (obj == USE_DEFAULT_TRANSITION) {
            obj = getEnterTransition();
        }
        return obj;
    }

    @NonNull
    public final SavedStateRegistry getSavedStateRegistry() {
        return this.mSavedStateRegistryController.getSavedStateRegistry();
    }

    @Nullable
    public Object getSharedElementEnterTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        return animationInfo.mSharedElementEnterTransition;
    }

    @Nullable
    public Object getSharedElementReturnTransition() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return null;
        }
        Object obj = animationInfo.mSharedElementReturnTransition;
        if (obj == USE_DEFAULT_TRANSITION) {
            obj = getSharedElementEnterTransition();
        }
        return obj;
    }

    /* access modifiers changed from: 0000 */
    public int getStateAfterAnimating() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return 0;
        }
        return animationInfo.mStateAfterAnimating;
    }

    @NonNull
    public final String getString(@StringRes int i) {
        return getResources().getString(i);
    }

    @NonNull
    public final String getString(@StringRes int i, @Nullable Object... objArr) {
        return getResources().getString(i, objArr);
    }

    @Nullable
    public final String getTag() {
        return this.mTag;
    }

    @Nullable
    public final Fragment getTargetFragment() {
        Fragment fragment = this.mTarget;
        if (fragment != null) {
            return fragment;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        if (fragmentManagerImpl != null) {
            String str = this.mTargetWho;
            if (str != null) {
                return (Fragment) fragmentManagerImpl.mActive.get(str);
            }
        }
        return null;
    }

    public final int getTargetRequestCode() {
        return this.mTargetRequestCode;
    }

    @NonNull
    public final CharSequence getText(@StringRes int i) {
        return getResources().getText(i);
    }

    @Deprecated
    public boolean getUserVisibleHint() {
        return this.mUserVisibleHint;
    }

    @Nullable
    public View getView() {
        return this.mView;
    }

    @MainThread
    @NonNull
    public LifecycleOwner getViewLifecycleOwner() {
        FragmentViewLifecycleOwner fragmentViewLifecycleOwner = this.mViewLifecycleOwner;
        if (fragmentViewLifecycleOwner != null) {
            return fragmentViewLifecycleOwner;
        }
        throw new IllegalStateException("Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()");
    }

    @NonNull
    public LiveData getViewLifecycleOwnerLiveData() {
        return this.mViewLifecycleOwnerLiveData;
    }

    @NonNull
    public ViewModelStore getViewModelStore() {
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        if (fragmentManagerImpl != null) {
            return fragmentManagerImpl.getViewModelStore(this);
        }
        throw new IllegalStateException("Can't access ViewModels from detached fragment");
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public final boolean hasOptionsMenu() {
        return this.mHasMenu;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    /* access modifiers changed from: 0000 */
    public void initState() {
        initLifecycle();
        this.mWho = UUID.randomUUID().toString();
        this.mAdded = false;
        this.mRemoving = false;
        this.mFromLayout = false;
        this.mInLayout = false;
        this.mRestored = false;
        this.mBackStackNesting = 0;
        this.mFragmentManager = null;
        this.mChildFragmentManager = null;
        this.mHost = null;
        this.mFragmentId = 0;
        this.mContainerId = 0;
        this.mTag = null;
        this.mHidden = false;
        this.mDetached = false;
    }

    /* access modifiers changed from: 0000 */
    public void instantiateChildFragmentManager() {
        if (this.mHost != null) {
            this.mChildFragmentManager = new FragmentManagerImpl();
            this.mChildFragmentManager.attachController(this.mHost, new FragmentContainer() {
                @Nullable
                public View onFindViewById(int i) {
                    View view = Fragment.this.mView;
                    if (view != null) {
                        return view.findViewById(i);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Fragment ");
                    sb.append(this);
                    sb.append(" does not have a view");
                    throw new IllegalStateException(sb.toString());
                }

                public boolean onHasView() {
                    return Fragment.this.mView != null;
                }
            }, this);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" has not been attached yet.");
        throw new IllegalStateException(sb.toString());
    }

    public final boolean isAdded() {
        return this.mHost != null && this.mAdded;
    }

    public final boolean isDetached() {
        return this.mDetached;
    }

    public final boolean isHidden() {
        return this.mHidden;
    }

    /* access modifiers changed from: 0000 */
    public boolean isHideReplaced() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return false;
        }
        return animationInfo.mIsHideReplaced;
    }

    /* access modifiers changed from: 0000 */
    public final boolean isInBackStack() {
        return this.mBackStackNesting > 0;
    }

    public final boolean isInLayout() {
        return this.mInLayout;
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public final boolean isMenuVisible() {
        return this.mMenuVisible;
    }

    /* access modifiers changed from: 0000 */
    public boolean isPostponed() {
        AnimationInfo animationInfo = this.mAnimationInfo;
        if (animationInfo == null) {
            return false;
        }
        return animationInfo.mEnterTransitionPostponed;
    }

    public final boolean isRemoving() {
        return this.mRemoving;
    }

    public final boolean isResumed() {
        return this.mState >= 4;
    }

    public final boolean isStateSaved() {
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        if (fragmentManagerImpl == null) {
            return false;
        }
        return fragmentManagerImpl.isStateSaved();
    }

    public final boolean isVisible() {
        if (isAdded() && !isHidden()) {
            View view = this.mView;
            if (!(view == null || view.getWindowToken() == null || this.mView.getVisibility() != 0)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void noteStateNotSaved() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
        }
    }

    @CallSuper
    public void onActivityCreated(@Nullable Bundle bundle) {
        this.mCalled = true;
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
    }

    @CallSuper
    @Deprecated
    public void onAttach(@NonNull Activity activity) {
        this.mCalled = true;
    }

    @CallSuper
    public void onAttach(@NonNull Context context) {
        this.mCalled = true;
        FragmentHostCallback fragmentHostCallback = this.mHost;
        Activity activity = fragmentHostCallback == null ? null : fragmentHostCallback.getActivity();
        if (activity != null) {
            this.mCalled = false;
            onAttach(activity);
        }
    }

    public void onAttachFragment(@NonNull Fragment fragment) {
    }

    @CallSuper
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        this.mCalled = true;
    }

    public boolean onContextItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @CallSuper
    public void onCreate(@Nullable Bundle bundle) {
        this.mCalled = true;
        restoreChildFragmentState(bundle);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null && !fragmentManagerImpl.isStateAtLeast(1)) {
            this.mChildFragmentManager.dispatchCreate();
        }
    }

    @Nullable
    public Animation onCreateAnimation(int i, boolean z, int i2) {
        return null;
    }

    @Nullable
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        return null;
    }

    public void onCreateContextMenu(@NonNull ContextMenu contextMenu, @NonNull View view, @Nullable ContextMenuInfo contextMenuInfo) {
        requireActivity().onCreateContextMenu(contextMenu, view, contextMenuInfo);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        int i = this.mContentLayoutId;
        if (i != 0) {
            return layoutInflater.inflate(i, viewGroup, false);
        }
        return null;
    }

    @CallSuper
    public void onDestroy() {
        this.mCalled = true;
    }

    public void onDestroyOptionsMenu() {
    }

    @CallSuper
    public void onDestroyView() {
        this.mCalled = true;
    }

    @CallSuper
    public void onDetach() {
        this.mCalled = true;
    }

    @NonNull
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle bundle) {
        return getLayoutInflater(bundle);
    }

    public void onHiddenChanged(boolean z) {
    }

    @CallSuper
    @Deprecated
    public void onInflate(@NonNull Activity activity, @NonNull AttributeSet attributeSet, @Nullable Bundle bundle) {
        this.mCalled = true;
    }

    @CallSuper
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attributeSet, @Nullable Bundle bundle) {
        this.mCalled = true;
        FragmentHostCallback fragmentHostCallback = this.mHost;
        Activity activity = fragmentHostCallback == null ? null : fragmentHostCallback.getActivity();
        if (activity != null) {
            this.mCalled = false;
            onInflate(activity, attributeSet, bundle);
        }
    }

    @CallSuper
    public void onLowMemory() {
        this.mCalled = true;
    }

    public void onMultiWindowModeChanged(boolean z) {
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void onOptionsMenuClosed(@NonNull Menu menu) {
    }

    @CallSuper
    public void onPause() {
        this.mCalled = true;
    }

    public void onPictureInPictureModeChanged(boolean z) {
    }

    public void onPrepareOptionsMenu(@NonNull Menu menu) {
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
    }

    @CallSuper
    public void onResume() {
        this.mCalled = true;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
    }

    @CallSuper
    public void onStart() {
        this.mCalled = true;
    }

    @CallSuper
    public void onStop() {
        this.mCalled = true;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
    }

    @CallSuper
    public void onViewStateRestored(@Nullable Bundle bundle) {
        this.mCalled = true;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public FragmentManager peekChildFragmentManager() {
        return this.mChildFragmentManager;
    }

    /* access modifiers changed from: 0000 */
    public void performActivityCreated(Bundle bundle) {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
        }
        this.mState = 2;
        this.mCalled = false;
        onActivityCreated(bundle);
        if (this.mCalled) {
            FragmentManagerImpl fragmentManagerImpl2 = this.mChildFragmentManager;
            if (fragmentManagerImpl2 != null) {
                fragmentManagerImpl2.dispatchActivityCreated();
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onActivityCreated()");
        throw new SuperNotCalledException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void performAttach() {
        this.mCalled = false;
        onAttach(this.mHost.getContext());
        if (!this.mCalled) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onAttach()");
            throw new SuperNotCalledException(sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public void performConfigurationChanged(@NonNull Configuration configuration) {
        onConfigurationChanged(configuration);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchConfigurationChanged(configuration);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean performContextItemSelected(@NonNull MenuItem menuItem) {
        if (!this.mHidden) {
            if (onContextItemSelected(menuItem)) {
                return true;
            }
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null && fragmentManagerImpl.dispatchContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void performCreate(Bundle bundle) {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
        }
        this.mState = 1;
        this.mCalled = false;
        this.mSavedStateRegistryController.performRestore(bundle);
        onCreate(bundle);
        this.mIsCreated = true;
        if (this.mCalled) {
            this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_CREATE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onCreate()");
        throw new SuperNotCalledException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public boolean performCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        boolean z = false;
        if (this.mHidden) {
            return false;
        }
        if (this.mHasMenu && this.mMenuVisible) {
            onCreateOptionsMenu(menu, menuInflater);
            z = true;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        return fragmentManagerImpl != null ? z | fragmentManagerImpl.dispatchCreateOptionsMenu(menu, menuInflater) : z;
    }

    /* access modifiers changed from: 0000 */
    public void performCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
        }
        this.mPerformedCreateView = true;
        this.mViewLifecycleOwner = new FragmentViewLifecycleOwner();
        this.mView = onCreateView(layoutInflater, viewGroup, bundle);
        if (this.mView != null) {
            this.mViewLifecycleOwner.initialize();
            this.mViewLifecycleOwnerLiveData.setValue(this.mViewLifecycleOwner);
        } else if (!this.mViewLifecycleOwner.isInitialized()) {
            this.mViewLifecycleOwner = null;
        } else {
            throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
        }
    }

    /* access modifiers changed from: 0000 */
    public void performDestroy() {
        this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_DESTROY);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchDestroy();
        }
        this.mState = 0;
        this.mCalled = false;
        this.mIsCreated = false;
        onDestroy();
        if (this.mCalled) {
            this.mChildFragmentManager = null;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onDestroy()");
        throw new SuperNotCalledException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void performDestroyView() {
        if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Event.ON_DESTROY);
        }
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchDestroyView();
        }
        this.mState = 1;
        this.mCalled = false;
        onDestroyView();
        if (this.mCalled) {
            LoaderManager.getInstance(this).markForRedelivery();
            this.mPerformedCreateView = false;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onDestroyView()");
        throw new SuperNotCalledException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void performDetach() {
        this.mCalled = false;
        onDetach();
        this.mLayoutInflater = null;
        if (this.mCalled) {
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null) {
                fragmentManagerImpl.dispatchDestroy();
                this.mChildFragmentManager = null;
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onDetach()");
        throw new SuperNotCalledException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public LayoutInflater performGetLayoutInflater(@Nullable Bundle bundle) {
        this.mLayoutInflater = onGetLayoutInflater(bundle);
        return this.mLayoutInflater;
    }

    /* access modifiers changed from: 0000 */
    public void performLowMemory() {
        onLowMemory();
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchLowMemory();
        }
    }

    /* access modifiers changed from: 0000 */
    public void performMultiWindowModeChanged(boolean z) {
        onMultiWindowModeChanged(z);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchMultiWindowModeChanged(z);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean performOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible && onOptionsItemSelected(menuItem)) {
                return true;
            }
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null && fragmentManagerImpl.dispatchOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public void performOptionsMenuClosed(@NonNull Menu menu) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible) {
                onOptionsMenuClosed(menu);
            }
            FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
            if (fragmentManagerImpl != null) {
                fragmentManagerImpl.dispatchOptionsMenuClosed(menu);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void performPause() {
        if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Event.ON_PAUSE);
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_PAUSE);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchPause();
        }
        this.mState = 3;
        this.mCalled = false;
        onPause();
        if (!this.mCalled) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onPause()");
            throw new SuperNotCalledException(sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public void performPictureInPictureModeChanged(boolean z) {
        onPictureInPictureModeChanged(z);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchPictureInPictureModeChanged(z);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean performPrepareOptionsMenu(@NonNull Menu menu) {
        boolean z = false;
        if (this.mHidden) {
            return false;
        }
        if (this.mHasMenu && this.mMenuVisible) {
            onPrepareOptionsMenu(menu);
            z = true;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        return fragmentManagerImpl != null ? z | fragmentManagerImpl.dispatchPrepareOptionsMenu(menu) : z;
    }

    /* access modifiers changed from: 0000 */
    public void performResume() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 4;
        this.mCalled = false;
        onResume();
        if (this.mCalled) {
            FragmentManagerImpl fragmentManagerImpl2 = this.mChildFragmentManager;
            if (fragmentManagerImpl2 != null) {
                fragmentManagerImpl2.dispatchResume();
                this.mChildFragmentManager.execPendingActions();
            }
            this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_RESUME);
            if (this.mView != null) {
                this.mViewLifecycleOwner.handleLifecycleEvent(Event.ON_RESUME);
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onResume()");
        throw new SuperNotCalledException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void performSaveInstanceState(Bundle bundle) {
        onSaveInstanceState(bundle);
        this.mSavedStateRegistryController.performSave(bundle);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            Parcelable saveAllState = fragmentManagerImpl.saveAllState();
            if (saveAllState != null) {
                bundle.putParcelable("android:support:fragments", saveAllState);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void performStart() {
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 3;
        this.mCalled = false;
        onStart();
        if (this.mCalled) {
            FragmentManagerImpl fragmentManagerImpl2 = this.mChildFragmentManager;
            if (fragmentManagerImpl2 != null) {
                fragmentManagerImpl2.dispatchStart();
            }
            this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_START);
            if (this.mView != null) {
                this.mViewLifecycleOwner.handleLifecycleEvent(Event.ON_START);
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not call through to super.onStart()");
        throw new SuperNotCalledException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void performStop() {
        if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Event.ON_STOP);
        }
        this.mLifecycleRegistry.handleLifecycleEvent(Event.ON_STOP);
        FragmentManagerImpl fragmentManagerImpl = this.mChildFragmentManager;
        if (fragmentManagerImpl != null) {
            fragmentManagerImpl.dispatchStop();
        }
        this.mState = 2;
        this.mCalled = false;
        onStop();
        if (!this.mCalled) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onStop()");
            throw new SuperNotCalledException(sb.toString());
        }
    }

    public void postponeEnterTransition() {
        ensureAnimationInfo().mEnterTransitionPostponed = true;
    }

    public final void postponeEnterTransition(long j, @NonNull TimeUnit timeUnit) {
        ensureAnimationInfo().mEnterTransitionPostponed = true;
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        (fragmentManagerImpl != null ? fragmentManagerImpl.mHost.getHandler() : new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {
            public void run() {
                Fragment.this.startPostponedEnterTransition();
            }
        }, timeUnit.toMillis(j));
    }

    public void registerForContextMenu(@NonNull View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public final void requestPermissions(@NonNull String[] strArr, int i) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            fragmentHostCallback.onRequestPermissionsFromFragment(this, strArr, i);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to Activity");
        throw new IllegalStateException(sb.toString());
    }

    @NonNull
    public final FragmentActivity requireActivity() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return activity;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to an activity.");
        throw new IllegalStateException(sb.toString());
    }

    @NonNull
    public final Bundle requireArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" does not have any arguments.");
        throw new IllegalStateException(sb.toString());
    }

    @NonNull
    public final Context requireContext() {
        Context context = getContext();
        if (context != null) {
            return context;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to a context.");
        throw new IllegalStateException(sb.toString());
    }

    @NonNull
    public final FragmentManager requireFragmentManager() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            return fragmentManager;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not associated with a fragment manager.");
        throw new IllegalStateException(sb.toString());
    }

    @NonNull
    public final Object requireHost() {
        Object host = getHost();
        if (host != null) {
            return host;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to a host.");
        throw new IllegalStateException(sb.toString());
    }

    @NonNull
    public final Fragment requireParentFragment() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            return parentFragment;
        }
        String str = "Fragment ";
        if (getContext() == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(this);
            sb.append(" is not attached to any Fragment or host");
            throw new IllegalStateException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(this);
        sb2.append(" is not a child Fragment, it is directly attached to ");
        sb2.append(getContext());
        throw new IllegalStateException(sb2.toString());
    }

    @NonNull
    public final View requireView() {
        View view = getView();
        if (view != null) {
            return view;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" did not return a View from onCreateView() or this was called before onCreateView().");
        throw new IllegalStateException(sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void restoreChildFragmentState(@Nullable Bundle bundle) {
        if (bundle != null) {
            Parcelable parcelable = bundle.getParcelable("android:support:fragments");
            if (parcelable != null) {
                if (this.mChildFragmentManager == null) {
                    instantiateChildFragmentManager();
                }
                this.mChildFragmentManager.restoreSaveState(parcelable);
                this.mChildFragmentManager.dispatchCreate();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void restoreViewState(Bundle bundle) {
        SparseArray sparseArray = this.mSavedViewState;
        if (sparseArray != null) {
            this.mInnerView.restoreHierarchyState(sparseArray);
            this.mSavedViewState = null;
        }
        this.mCalled = false;
        onViewStateRestored(bundle);
        if (!this.mCalled) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(this);
            sb.append(" did not call through to super.onViewStateRestored()");
            throw new SuperNotCalledException(sb.toString());
        } else if (this.mView != null) {
            this.mViewLifecycleOwner.handleLifecycleEvent(Event.ON_CREATE);
        }
    }

    public void setAllowEnterTransitionOverlap(boolean z) {
        ensureAnimationInfo().mAllowEnterTransitionOverlap = Boolean.valueOf(z);
    }

    public void setAllowReturnTransitionOverlap(boolean z) {
        ensureAnimationInfo().mAllowReturnTransitionOverlap = Boolean.valueOf(z);
    }

    /* access modifiers changed from: 0000 */
    public void setAnimatingAway(View view) {
        ensureAnimationInfo().mAnimatingAway = view;
    }

    /* access modifiers changed from: 0000 */
    public void setAnimator(Animator animator) {
        ensureAnimationInfo().mAnimator = animator;
    }

    public void setArguments(@Nullable Bundle bundle) {
        if (this.mFragmentManager == null || !isStateSaved()) {
            this.mArguments = bundle;
            return;
        }
        throw new IllegalStateException("Fragment already added and state has been saved");
    }

    public void setEnterSharedElementCallback(@Nullable SharedElementCallback sharedElementCallback) {
        ensureAnimationInfo().mEnterTransitionCallback = sharedElementCallback;
    }

    public void setEnterTransition(@Nullable Object obj) {
        ensureAnimationInfo().mEnterTransition = obj;
    }

    public void setExitSharedElementCallback(@Nullable SharedElementCallback sharedElementCallback) {
        ensureAnimationInfo().mExitTransitionCallback = sharedElementCallback;
    }

    public void setExitTransition(@Nullable Object obj) {
        ensureAnimationInfo().mExitTransition = obj;
    }

    public void setHasOptionsMenu(boolean z) {
        if (this.mHasMenu != z) {
            this.mHasMenu = z;
            if (isAdded() && !isHidden()) {
                this.mHost.onSupportInvalidateOptionsMenu();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setHideReplaced(boolean z) {
        ensureAnimationInfo().mIsHideReplaced = z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0008, code lost:
        if (r2 != null) goto L_0x000c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setInitialSavedState(@Nullable SavedState savedState) {
        Bundle bundle;
        if (this.mFragmentManager == null) {
            if (savedState != null) {
                bundle = savedState.mState;
            }
            bundle = null;
            this.mSavedFragmentState = bundle;
            return;
        }
        throw new IllegalStateException("Fragment already added");
    }

    public void setMenuVisibility(boolean z) {
        if (this.mMenuVisible != z) {
            this.mMenuVisible = z;
            if (this.mHasMenu && isAdded() && !isHidden()) {
                this.mHost.onSupportInvalidateOptionsMenu();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setNextAnim(int i) {
        if (this.mAnimationInfo != null || i != 0) {
            ensureAnimationInfo().mNextAnim = i;
        }
    }

    /* access modifiers changed from: 0000 */
    public void setNextTransition(int i, int i2) {
        if (this.mAnimationInfo != null || i != 0 || i2 != 0) {
            ensureAnimationInfo();
            AnimationInfo animationInfo = this.mAnimationInfo;
            animationInfo.mNextTransition = i;
            animationInfo.mNextTransitionStyle = i2;
        }
    }

    /* access modifiers changed from: 0000 */
    public void setOnStartEnterTransitionListener(OnStartEnterTransitionListener onStartEnterTransitionListener) {
        ensureAnimationInfo();
        OnStartEnterTransitionListener onStartEnterTransitionListener2 = this.mAnimationInfo.mStartEnterTransitionListener;
        if (onStartEnterTransitionListener != onStartEnterTransitionListener2) {
            if (onStartEnterTransitionListener == null || onStartEnterTransitionListener2 == null) {
                AnimationInfo animationInfo = this.mAnimationInfo;
                if (animationInfo.mEnterTransitionPostponed) {
                    animationInfo.mStartEnterTransitionListener = onStartEnterTransitionListener;
                }
                if (onStartEnterTransitionListener != null) {
                    onStartEnterTransitionListener.startListening();
                }
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to set a replacement startPostponedEnterTransition on ");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
    }

    public void setReenterTransition(@Nullable Object obj) {
        ensureAnimationInfo().mReenterTransition = obj;
    }

    public void setRetainInstance(boolean z) {
        this.mRetainInstance = z;
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        if (fragmentManagerImpl == null) {
            this.mRetainInstanceChangedWhileDetached = true;
        } else if (z) {
            fragmentManagerImpl.addRetainedFragment(this);
        } else {
            fragmentManagerImpl.removeRetainedFragment(this);
        }
    }

    public void setReturnTransition(@Nullable Object obj) {
        ensureAnimationInfo().mReturnTransition = obj;
    }

    public void setSharedElementEnterTransition(@Nullable Object obj) {
        ensureAnimationInfo().mSharedElementEnterTransition = obj;
    }

    public void setSharedElementReturnTransition(@Nullable Object obj) {
        ensureAnimationInfo().mSharedElementReturnTransition = obj;
    }

    /* access modifiers changed from: 0000 */
    public void setStateAfterAnimating(int i) {
        ensureAnimationInfo().mStateAfterAnimating = i;
    }

    public void setTargetFragment(@Nullable Fragment fragment, int i) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentManager fragmentManager2 = fragment != null ? fragment.getFragmentManager() : null;
        if (fragmentManager == null || fragmentManager2 == null || fragmentManager == fragmentManager2) {
            Fragment fragment2 = fragment;
            while (fragment2 != null) {
                if (fragment2 != this) {
                    fragment2 = fragment2.getTargetFragment();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Setting ");
                    sb.append(fragment);
                    sb.append(" as the target of ");
                    sb.append(this);
                    sb.append(" would create a target cycle");
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            if (fragment == null) {
                this.mTargetWho = null;
            } else if (this.mFragmentManager == null || fragment.mFragmentManager == null) {
                this.mTargetWho = null;
                this.mTarget = fragment;
                this.mTargetRequestCode = i;
                return;
            } else {
                this.mTargetWho = fragment.mWho;
            }
            this.mTarget = null;
            this.mTargetRequestCode = i;
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Fragment ");
        sb2.append(fragment);
        sb2.append(" must share the same FragmentManager to be set as a target fragment");
        throw new IllegalArgumentException(sb2.toString());
    }

    @Deprecated
    public void setUserVisibleHint(boolean z) {
        if (!this.mUserVisibleHint && z && this.mState < 3 && this.mFragmentManager != null && isAdded() && this.mIsCreated) {
            this.mFragmentManager.performPendingDeferredStart(this);
        }
        this.mUserVisibleHint = z;
        boolean z2 = this.mState < 3 && !z;
        this.mDeferStart = z2;
        if (this.mSavedFragmentState != null) {
            this.mSavedUserVisibleHint = Boolean.valueOf(z);
        }
    }

    public boolean shouldShowRequestPermissionRationale(@NonNull String str) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            return fragmentHostCallback.onShouldShowRequestPermissionRationale(str);
        }
        return false;
    }

    public void startActivity(@SuppressLint({"UnknownNullness"}) Intent intent) {
        startActivity(intent, null);
    }

    public void startActivity(@SuppressLint({"UnknownNullness"}) Intent intent, @Nullable Bundle bundle) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            fragmentHostCallback.onStartActivityFromFragment(this, intent, -1, bundle);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to Activity");
        throw new IllegalStateException(sb.toString());
    }

    public void startActivityForResult(@SuppressLint({"UnknownNullness"}) Intent intent, int i) {
        startActivityForResult(intent, i, null);
    }

    public void startActivityForResult(@SuppressLint({"UnknownNullness"}) Intent intent, int i, @Nullable Bundle bundle) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            fragmentHostCallback.onStartActivityFromFragment(this, intent, i, bundle);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to Activity");
        throw new IllegalStateException(sb.toString());
    }

    public void startIntentSenderForResult(@SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i, @Nullable Intent intent, int i2, int i3, int i4, @Nullable Bundle bundle) {
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            fragmentHostCallback.onStartIntentSenderFromFragment(this, intentSender, i, intent, i2, i3, i4, bundle);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(this);
        sb.append(" not attached to Activity");
        throw new IllegalStateException(sb.toString());
    }

    public void startPostponedEnterTransition() {
        FragmentManagerImpl fragmentManagerImpl = this.mFragmentManager;
        if (fragmentManagerImpl == null || fragmentManagerImpl.mHost == null) {
            ensureAnimationInfo().mEnterTransitionPostponed = false;
        } else if (Looper.myLooper() != this.mFragmentManager.mHost.getHandler().getLooper()) {
            this.mFragmentManager.mHost.getHandler().postAtFrontOfQueue(new Runnable() {
                public void run() {
                    Fragment.this.callStartTransitionListener();
                }
            });
        } else {
            callStartTransitionListener();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        DebugUtils.buildShortClassTag(this, sb);
        sb.append(" (");
        sb.append(this.mWho);
        sb.append(")");
        if (this.mFragmentId != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.mFragmentId));
        }
        if (this.mTag != null) {
            sb.append(" ");
            sb.append(this.mTag);
        }
        sb.append('}');
        return sb.toString();
    }

    public void unregisterForContextMenu(@NonNull View view) {
        view.setOnCreateContextMenuListener(null);
    }
}

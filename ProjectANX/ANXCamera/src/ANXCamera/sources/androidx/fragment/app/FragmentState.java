package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle.State;

@SuppressLint({"BanParcelableUsage"})
final class FragmentState implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public FragmentState createFromParcel(Parcel parcel) {
            return new FragmentState(parcel);
        }

        public FragmentState[] newArray(int i) {
            return new FragmentState[i];
        }
    };
    final Bundle mArguments;
    final String mClassName;
    final int mContainerId;
    final boolean mDetached;
    final int mFragmentId;
    final boolean mFromLayout;
    final boolean mHidden;
    Fragment mInstance;
    final int mMaxLifecycleState;
    final boolean mRemoving;
    final boolean mRetainInstance;
    Bundle mSavedFragmentState;
    final String mTag;
    final String mWho;

    FragmentState(Parcel parcel) {
        this.mClassName = parcel.readString();
        this.mWho = parcel.readString();
        boolean z = true;
        this.mFromLayout = parcel.readInt() != 0;
        this.mFragmentId = parcel.readInt();
        this.mContainerId = parcel.readInt();
        this.mTag = parcel.readString();
        this.mRetainInstance = parcel.readInt() != 0;
        this.mRemoving = parcel.readInt() != 0;
        this.mDetached = parcel.readInt() != 0;
        this.mArguments = parcel.readBundle();
        if (parcel.readInt() == 0) {
            z = false;
        }
        this.mHidden = z;
        this.mSavedFragmentState = parcel.readBundle();
        this.mMaxLifecycleState = parcel.readInt();
    }

    FragmentState(Fragment fragment) {
        this.mClassName = fragment.getClass().getName();
        this.mWho = fragment.mWho;
        this.mFromLayout = fragment.mFromLayout;
        this.mFragmentId = fragment.mFragmentId;
        this.mContainerId = fragment.mContainerId;
        this.mTag = fragment.mTag;
        this.mRetainInstance = fragment.mRetainInstance;
        this.mRemoving = fragment.mRemoving;
        this.mDetached = fragment.mDetached;
        this.mArguments = fragment.mArguments;
        this.mHidden = fragment.mHidden;
        this.mMaxLifecycleState = fragment.mMaxState.ordinal();
    }

    public int describeContents() {
        return 0;
    }

    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull FragmentFactory fragmentFactory) {
        Bundle bundle;
        Fragment fragment;
        if (this.mInstance == null) {
            Bundle bundle2 = this.mArguments;
            if (bundle2 != null) {
                bundle2.setClassLoader(classLoader);
            }
            this.mInstance = fragmentFactory.instantiate(classLoader, this.mClassName);
            this.mInstance.setArguments(this.mArguments);
            Bundle bundle3 = this.mSavedFragmentState;
            if (bundle3 != null) {
                bundle3.setClassLoader(classLoader);
                fragment = this.mInstance;
                bundle = this.mSavedFragmentState;
            } else {
                fragment = this.mInstance;
                bundle = new Bundle();
            }
            fragment.mSavedFragmentState = bundle;
            Fragment fragment2 = this.mInstance;
            fragment2.mWho = this.mWho;
            fragment2.mFromLayout = this.mFromLayout;
            fragment2.mRestored = true;
            fragment2.mFragmentId = this.mFragmentId;
            fragment2.mContainerId = this.mContainerId;
            fragment2.mTag = this.mTag;
            fragment2.mRetainInstance = this.mRetainInstance;
            fragment2.mRemoving = this.mRemoving;
            fragment2.mDetached = this.mDetached;
            fragment2.mHidden = this.mHidden;
            fragment2.mMaxState = State.values()[this.mMaxLifecycleState];
            if (FragmentManagerImpl.DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Instantiated fragment ");
                sb.append(this.mInstance);
                Log.v("FragmentManager", sb.toString());
            }
        }
        return this.mInstance;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentState{");
        sb.append(this.mClassName);
        sb.append(" (");
        sb.append(this.mWho);
        sb.append(")}:");
        if (this.mFromLayout) {
            sb.append(" fromLayout");
        }
        if (this.mContainerId != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.mContainerId));
        }
        String str = this.mTag;
        if (str != null && !str.isEmpty()) {
            sb.append(" tag=");
            sb.append(this.mTag);
        }
        if (this.mRetainInstance) {
            sb.append(" retainInstance");
        }
        if (this.mRemoving) {
            sb.append(" removing");
        }
        if (this.mDetached) {
            sb.append(" detached");
        }
        if (this.mHidden) {
            sb.append(" hidden");
        }
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mClassName);
        parcel.writeString(this.mWho);
        parcel.writeInt(this.mFromLayout ? 1 : 0);
        parcel.writeInt(this.mFragmentId);
        parcel.writeInt(this.mContainerId);
        parcel.writeString(this.mTag);
        parcel.writeInt(this.mRetainInstance ? 1 : 0);
        parcel.writeInt(this.mRemoving ? 1 : 0);
        parcel.writeInt(this.mDetached ? 1 : 0);
        parcel.writeBundle(this.mArguments);
        parcel.writeInt(this.mHidden ? 1 : 0);
        parcel.writeBundle(this.mSavedFragmentState);
        parcel.writeInt(this.mMaxLifecycleState);
    }
}

package com.google.android.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class BinderWrapper extends Binder implements IInterface {
    private static Empty empty;

    protected BinderWrapper(String str) {
        attachInterface(this, str);
    }

    public IBinder asBinder() {
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean dispatchTransact(int i, Parcel parcel) {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean onTransact(int i, @NonNull Parcel parcel, @Nullable Parcel parcel2, int i2) {
        boolean z;
        if (i > 16777215) {
            z = super.onTransact(i, parcel, parcel2, i2);
        } else {
            parcel.enforceInterface(getInterfaceDescriptor());
            z = false;
        }
        return z || dispatchTransact(i, parcel);
    }
}

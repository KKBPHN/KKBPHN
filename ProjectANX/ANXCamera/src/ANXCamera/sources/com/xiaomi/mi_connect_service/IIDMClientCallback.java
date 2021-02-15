package com.xiaomi.mi_connect_service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IIDMClientCallback extends IInterface {

    public abstract class Stub extends Binder implements IIDMClientCallback {
        private static final String DESCRIPTOR = "com.xiaomi.mi_connect_service.IIDMClientCallback";
        static final int TRANSACTION_onEvent = 3;
        static final int TRANSACTION_onResponse = 2;
        static final int TRANSACTION_onServiceConnectStatus = 4;
        static final int TRANSACTION_onServiceFound = 1;

        class Proxy implements IIDMClientCallback {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void onEvent(byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onResponse(byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onServiceConnectStatus(byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onServiceFound(byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIDMClientCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IIDMClientCallback)) ? new Proxy(iBinder) : (IIDMClientCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            String str = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(str);
                onServiceFound(parcel.createByteArray());
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(str);
                onResponse(parcel.createByteArray());
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(str);
                onEvent(parcel.createByteArray());
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(str);
                onServiceConnectStatus(parcel.createByteArray());
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
        }
    }

    void onEvent(byte[] bArr);

    void onResponse(byte[] bArr);

    void onServiceConnectStatus(byte[] bArr);

    void onServiceFound(byte[] bArr);
}

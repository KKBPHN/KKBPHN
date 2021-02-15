package com.xiaomi.mi_connect_service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IIDMServiceProcCallback extends IInterface {

    public abstract class Stub extends Binder implements IIDMServiceProcCallback {
        private static final String DESCRIPTOR = "com.xiaomi.mi_connect_service.IIDMServiceProcCallback";
        static final int TRANSACTION_onRequest = 1;
        static final int TRANSACTION_onSetEventCallback = 2;

        class Proxy implements IIDMServiceProcCallback {
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

            public void onRequest(byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public int onSetEventCallback(byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIDMServiceProcCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IIDMServiceProcCallback)) ? new Proxy(iBinder) : (IIDMServiceProcCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            String str = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(str);
                onRequest(parcel.createByteArray());
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(str);
                int onSetEventCallback = onSetEventCallback(parcel.createByteArray());
                parcel2.writeNoException();
                parcel2.writeInt(onSetEventCallback);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
        }
    }

    void onRequest(byte[] bArr);

    int onSetEventCallback(byte[] bArr);
}

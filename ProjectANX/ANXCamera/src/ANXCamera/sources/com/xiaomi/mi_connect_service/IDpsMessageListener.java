package com.xiaomi.mi_connect_service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IDpsMessageListener extends IInterface {
    public static final int ERROR = -1;
    public static final int OK = 0;

    public abstract class Stub extends Binder implements IDpsMessageListener {
        private static final String DESCRIPTOR = "com.xiaomi.mi_connect_service.IDpsMessageListener";
        static final int TRANSACTION_onMessage = 1;

        class Proxy implements IDpsMessageListener {
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

            public void onMessage(DpsCallbackData dpsCallbackData) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dpsCallbackData != null) {
                        obtain.writeInt(1);
                        dpsCallbackData.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDpsMessageListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IDpsMessageListener)) ? new Proxy(iBinder) : (IDpsMessageListener) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            String str = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(str);
                onMessage(parcel.readInt() != 0 ? (DpsCallbackData) DpsCallbackData.CREATOR.createFromParcel(parcel) : null);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
        }
    }

    void onMessage(DpsCallbackData dpsCallbackData);
}

package com.xiaomi.mi_connect_service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IMiConnectCallback extends IInterface {

    public abstract class Stub extends Binder implements IMiConnectCallback {
        private static final String DESCRIPTOR = "com.xiaomi.mi_connect_service.IMiConnectCallback";
        static final int TRANSACTION_onAdvertisingResult = 1;
        static final int TRANSACTION_onConnectionInitiated = 4;
        static final int TRANSACTION_onConnectionResult = 5;
        static final int TRANSACTION_onDisconnection = 6;
        static final int TRANSACTION_onDiscoveryResult = 2;
        static final int TRANSACTION_onEndpointFound = 3;
        static final int TRANSACTION_onEndpointLost = 9;
        static final int TRANSACTION_onPayloadReceived = 8;
        static final int TRANSACTION_onPayloadSentResult = 7;

        class Proxy implements IMiConnectCallback {
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

            public void onAdvertisingResult(int i, int i2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onConnectionInitiated(int i, int i2, String str, byte[] bArr, byte[] bArr2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onConnectionResult(int i, int i2, String str, int i3) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeInt(i3);
                    this.mRemote.transact(5, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onDisconnection(int i, int i2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(6, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onDiscoveryResult(int i, int i2) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onEndpointFound(int i, int i2, String str, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onEndpointLost(int i, int i2, String str) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.mRemote.transact(9, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onPayloadReceived(int i, int i2, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(8, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onPayloadSentResult(int i, int i2, int i3) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(7, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiConnectCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IMiConnectCallback)) ? new Proxy(iBinder) : (IMiConnectCallback) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            String str = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        onAdvertisingResult(parcel.readInt(), parcel.readInt());
                        return true;
                    case 2:
                        parcel.enforceInterface(str);
                        onDiscoveryResult(parcel.readInt(), parcel.readInt());
                        return true;
                    case 3:
                        parcel.enforceInterface(str);
                        onEndpointFound(parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.createByteArray());
                        return true;
                    case 4:
                        parcel.enforceInterface(str);
                        onConnectionInitiated(parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.createByteArray(), parcel.createByteArray());
                        return true;
                    case 5:
                        parcel.enforceInterface(str);
                        onConnectionResult(parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface(str);
                        onDisconnection(parcel.readInt(), parcel.readInt());
                        return true;
                    case 7:
                        parcel.enforceInterface(str);
                        onPayloadSentResult(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        return true;
                    case 8:
                        parcel.enforceInterface(str);
                        onPayloadReceived(parcel.readInt(), parcel.readInt(), parcel.createByteArray());
                        return true;
                    case 9:
                        parcel.enforceInterface(str);
                        onEndpointLost(parcel.readInt(), parcel.readInt(), parcel.readString());
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(str);
                return true;
            }
        }
    }

    void onAdvertisingResult(int i, int i2);

    void onConnectionInitiated(int i, int i2, String str, byte[] bArr, byte[] bArr2);

    void onConnectionResult(int i, int i2, String str, int i3);

    void onDisconnection(int i, int i2);

    void onDiscoveryResult(int i, int i2);

    void onEndpointFound(int i, int i2, String str, byte[] bArr);

    void onEndpointLost(int i, int i2, String str);

    void onPayloadReceived(int i, int i2, byte[] bArr);

    void onPayloadSentResult(int i, int i2, int i3);
}

package com.xiaomi.mi_connect_service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IMiConnect extends IInterface {
    public static final int ERROR = -1;
    public static final int OK = 0;

    public abstract class Stub extends Binder implements IMiConnect {
        private static final String DESCRIPTOR = "com.xiaomi.mi_connect_service.IMiConnect";
        static final int TRANSACTION_acceptConnection = 8;
        static final int TRANSACTION_connectService = 24;
        static final int TRANSACTION_createConnection = 17;
        static final int TRANSACTION_destroy = 12;
        static final int TRANSACTION_destroyConnection = 18;
        static final int TRANSACTION_deviceInfoIDM = 31;
        static final int TRANSACTION_disconnectFromEndPoint = 10;
        static final int TRANSACTION_event = 28;
        static final int TRANSACTION_getIdHash = 13;
        static final int TRANSACTION_getServiceApiVersion = 6;
        static final int TRANSACTION_publish = 15;
        static final int TRANSACTION_registerIDMClient = 19;
        static final int TRANSACTION_registerProc = 25;
        static final int TRANSACTION_rejectConnection = 9;
        static final int TRANSACTION_request = 20;
        static final int TRANSACTION_requestConnection = 7;
        static final int TRANSACTION_response = 27;
        static final int TRANSACTION_sendPayload = 11;
        static final int TRANSACTION_setCallback = 1;
        static final int TRANSACTION_setEventCallback = 22;
        static final int TRANSACTION_setIPCDataCallback = 14;
        static final int TRANSACTION_startAdvertising = 2;
        static final int TRANSACTION_startAdvertisingIDM = 26;
        static final int TRANSACTION_startDiscovery = 4;
        static final int TRANSACTION_startDiscoveryIDM = 21;
        static final int TRANSACTION_startDiscoveryV2 = 30;
        static final int TRANSACTION_stopAdvertising = 3;
        static final int TRANSACTION_stopDiscovery = 5;
        static final int TRANSACTION_subscribe = 16;
        static final int TRANSACTION_unregisterIDMClient = 23;
        static final int TRANSACTION_unregisterProc = 29;

        class Proxy implements IMiConnect {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public void acceptConnection(int i, int i2, int i3, boolean z) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int connectService(int i, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int createConnection(int i, byte[] bArr, IConnectionCallback iConnectionCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeStrongBinder(iConnectionCallback != null ? iConnectionCallback.asBinder() : null);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void destroy(int i, int i2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int destroyConnection(int i, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] deviceInfoIDM() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void disconnectFromEndPoint(int i, int i2, int i3) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int event(int i, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getIdHash() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int getServiceApiVersion() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int publish(int i, String str, String str2, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String registerIDMClient(int i, byte[] bArr, IIDMClientCallback iIDMClientCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeStrongBinder(iIDMClientCallback != null ? iIDMClientCallback.asBinder() : null);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int registerProc(int i, byte[] bArr, IIDMServiceProcCallback iIDMServiceProcCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeStrongBinder(iIDMServiceProcCallback != null ? iIDMServiceProcCallback.asBinder() : null);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void rejectConnection(int i, int i2, int i3) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] request(int i, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void requestConnection(int i, int i2, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int response(int i, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sendPayload(int i, int i2, int i3, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCallback(int i, int i2, IMiConnectCallback iMiConnectCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeStrongBinder(iMiConnectCallback != null ? iMiConnectCallback.asBinder() : null);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setEventCallback(int i, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int setIPCDataCallback(int i, String str, IIPCDataCallback iIPCDataCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iIPCDataCallback != null ? iIPCDataCallback.asBinder() : null);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startAdvertising(int i, byte[] bArr, int i2, int i3, byte[] bArr2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeByteArray(bArr2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startAdvertisingIDM(int i, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startDiscovery(int i, byte[] bArr, int i2, int i3, int i4) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int startDiscoveryIDM(int i, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startDiscoveryV2(int i, byte[] bArr, int i2, int i3, int i4, int[] iArr) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopAdvertising(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopDiscovery(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int subscribe(int i, String str, String str2, IDpsMessageListener iDpsMessageListener) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iDpsMessageListener != null ? iDpsMessageListener.asBinder() : null);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int unregisterIDMClient(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int unregisterProc(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(29, obtain, obtain2, 0);
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

        public static IMiConnect asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IMiConnect)) ? new Proxy(iBinder) : (IMiConnect) queryLocalInterface;
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
                        setCallback(parcel.readInt(), parcel.readInt(), com.xiaomi.mi_connect_service.IMiConnectCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(str);
                        startAdvertising(parcel.readInt(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(str);
                        stopAdvertising(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(str);
                        startDiscovery(parcel.readInt(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(str);
                        stopDiscovery(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(str);
                        int serviceApiVersion = getServiceApiVersion();
                        parcel2.writeNoException();
                        parcel2.writeInt(serviceApiVersion);
                        return true;
                    case 7:
                        parcel.enforceInterface(str);
                        requestConnection(parcel.readInt(), parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(str);
                        acceptConnection(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                        parcel2.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(str);
                        rejectConnection(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(str);
                        disconnectFromEndPoint(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(str);
                        sendPayload(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(str);
                        destroy(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(str);
                        byte[] idHash = getIdHash();
                        parcel2.writeNoException();
                        parcel2.writeByteArray(idHash);
                        return true;
                    case 14:
                        parcel.enforceInterface(str);
                        int iPCDataCallback = setIPCDataCallback(parcel.readInt(), parcel.readString(), com.xiaomi.mi_connect_service.IIPCDataCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel2.writeInt(iPCDataCallback);
                        return true;
                    case 15:
                        parcel.enforceInterface(str);
                        int publish = publish(parcel.readInt(), parcel.readString(), parcel.readString(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(publish);
                        return true;
                    case 16:
                        parcel.enforceInterface(str);
                        int subscribe = subscribe(parcel.readInt(), parcel.readString(), parcel.readString(), com.xiaomi.mi_connect_service.IDpsMessageListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel2.writeInt(subscribe);
                        return true;
                    case 17:
                        parcel.enforceInterface(str);
                        int createConnection = createConnection(parcel.readInt(), parcel.createByteArray(), com.xiaomi.mi_connect_service.IConnectionCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel2.writeInt(createConnection);
                        return true;
                    case 18:
                        parcel.enforceInterface(str);
                        int destroyConnection = destroyConnection(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(destroyConnection);
                        return true;
                    case 19:
                        parcel.enforceInterface(str);
                        String registerIDMClient = registerIDMClient(parcel.readInt(), parcel.createByteArray(), com.xiaomi.mi_connect_service.IIDMClientCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel2.writeString(registerIDMClient);
                        return true;
                    case 20:
                        parcel.enforceInterface(str);
                        byte[] request = request(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeByteArray(request);
                        return true;
                    case 21:
                        parcel.enforceInterface(str);
                        int startDiscoveryIDM = startDiscoveryIDM(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(startDiscoveryIDM);
                        return true;
                    case 22:
                        parcel.enforceInterface(str);
                        int eventCallback = setEventCallback(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(eventCallback);
                        return true;
                    case 23:
                        parcel.enforceInterface(str);
                        int unregisterIDMClient = unregisterIDMClient(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(unregisterIDMClient);
                        return true;
                    case 24:
                        parcel.enforceInterface(str);
                        int connectService = connectService(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(connectService);
                        return true;
                    case 25:
                        parcel.enforceInterface(str);
                        int registerProc = registerProc(parcel.readInt(), parcel.createByteArray(), com.xiaomi.mi_connect_service.IIDMServiceProcCallback.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel2.writeInt(registerProc);
                        return true;
                    case 26:
                        parcel.enforceInterface(str);
                        int startAdvertisingIDM = startAdvertisingIDM(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(startAdvertisingIDM);
                        return true;
                    case 27:
                        parcel.enforceInterface(str);
                        int response = response(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(response);
                        return true;
                    case 28:
                        parcel.enforceInterface(str);
                        int event = event(parcel.readInt(), parcel.createByteArray());
                        parcel2.writeNoException();
                        parcel2.writeInt(event);
                        return true;
                    case 29:
                        parcel.enforceInterface(str);
                        int unregisterProc = unregisterProc(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(unregisterProc);
                        return true;
                    case 30:
                        parcel.enforceInterface(str);
                        startDiscoveryV2(parcel.readInt(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.createIntArray());
                        parcel2.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(str);
                        byte[] deviceInfoIDM = deviceInfoIDM();
                        parcel2.writeNoException();
                        parcel2.writeByteArray(deviceInfoIDM);
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

    void acceptConnection(int i, int i2, int i3, boolean z);

    int connectService(int i, byte[] bArr);

    int createConnection(int i, byte[] bArr, IConnectionCallback iConnectionCallback);

    void destroy(int i, int i2);

    int destroyConnection(int i, byte[] bArr);

    byte[] deviceInfoIDM();

    void disconnectFromEndPoint(int i, int i2, int i3);

    int event(int i, byte[] bArr);

    byte[] getIdHash();

    int getServiceApiVersion();

    int publish(int i, String str, String str2, byte[] bArr);

    String registerIDMClient(int i, byte[] bArr, IIDMClientCallback iIDMClientCallback);

    int registerProc(int i, byte[] bArr, IIDMServiceProcCallback iIDMServiceProcCallback);

    void rejectConnection(int i, int i2, int i3);

    byte[] request(int i, byte[] bArr);

    void requestConnection(int i, int i2, byte[] bArr);

    int response(int i, byte[] bArr);

    void sendPayload(int i, int i2, int i3, byte[] bArr);

    void setCallback(int i, int i2, IMiConnectCallback iMiConnectCallback);

    int setEventCallback(int i, byte[] bArr);

    int setIPCDataCallback(int i, String str, IIPCDataCallback iIPCDataCallback);

    void startAdvertising(int i, byte[] bArr, int i2, int i3, byte[] bArr2);

    int startAdvertisingIDM(int i, byte[] bArr);

    void startDiscovery(int i, byte[] bArr, int i2, int i3, int i4);

    int startDiscoveryIDM(int i, byte[] bArr);

    void startDiscoveryV2(int i, byte[] bArr, int i2, int i3, int i4, int[] iArr);

    void stopAdvertising(int i);

    void stopDiscovery(int i);

    int subscribe(int i, String str, String str2, IDpsMessageListener iDpsMessageListener);

    int unregisterIDMClient(int i);

    int unregisterProc(int i);
}

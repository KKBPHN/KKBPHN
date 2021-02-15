package com.xiaomi.camera.rcs;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IRemoteControlClient extends IInterface {

    public class Default implements IRemoteControlClient {
        public IBinder asBinder() {
            return null;
        }

        public void customCallback(String str, Bundle bundle) {
        }

        public void streamingServerStatus(int i, Bundle bundle) {
        }

        public void streamingSessionStatus(int i, Bundle bundle) {
        }
    }

    public abstract class Stub extends Binder implements IRemoteControlClient {
        private static final String DESCRIPTOR = "com.xiaomi.camera.rcs.IRemoteControlClient";
        static final int TRANSACTION_customCallback = 3;
        static final int TRANSACTION_streamingServerStatus = 1;
        static final int TRANSACTION_streamingSessionStatus = 2;

        class Proxy implements IRemoteControlClient {
            public static IRemoteControlClient sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void customCallback(String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(3, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().customCallback(str, bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void streamingServerStatus(int i, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().streamingServerStatus(i, bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void streamingSessionStatus(int i, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(2, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().streamingSessionStatus(i, bundle);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteControlClient asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IRemoteControlClient)) ? new Proxy(iBinder) : (IRemoteControlClient) queryLocalInterface;
        }

        public static IRemoteControlClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IRemoteControlClient iRemoteControlClient) {
            if (Proxy.sDefaultImpl != null || iRemoteControlClient == null) {
                return false;
            }
            Proxy.sDefaultImpl = iRemoteControlClient;
            return true;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            Bundle bundle;
            Bundle bundle2 = null;
            String str = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(str);
                int readInt = parcel.readInt();
                if (parcel.readInt() != 0) {
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                }
                streamingServerStatus(readInt, bundle);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(str);
                int readInt2 = parcel.readInt();
                if (parcel.readInt() != 0) {
                    bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                }
                streamingSessionStatus(readInt2, bundle2);
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(str);
                String readString = parcel.readString();
                if (parcel.readInt() != 0) {
                    bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                }
                customCallback(readString, bundle2);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
        }
    }

    void customCallback(String str, Bundle bundle);

    void streamingServerStatus(int i, Bundle bundle);

    void streamingSessionStatus(int i, Bundle bundle);
}

package com.iqiyi.android.qigsaw.core.splitinstall.protocol;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.List;

public interface ISplitInstallServiceCallback extends IInterface {

    public class Default implements ISplitInstallServiceCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onCancelInstall(int i, Bundle bundle) {
        }

        public void onCompleteInstall(int i) {
        }

        public void onDeferredInstall(Bundle bundle) {
        }

        public void onDeferredUninstall(Bundle bundle) {
        }

        public void onError(Bundle bundle) {
        }

        public void onGetSession(int i, Bundle bundle) {
        }

        public void onGetSessionStates(List list) {
        }

        public void onStartInstall(int i, Bundle bundle) {
        }
    }

    public abstract class Stub extends Binder implements ISplitInstallServiceCallback {
        private static final String DESCRIPTOR = "com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback";
        static final int TRANSACTION_onCancelInstall = 3;
        static final int TRANSACTION_onCompleteInstall = 2;
        static final int TRANSACTION_onDeferredInstall = 6;
        static final int TRANSACTION_onDeferredUninstall = 5;
        static final int TRANSACTION_onError = 8;
        static final int TRANSACTION_onGetSession = 4;
        static final int TRANSACTION_onGetSessionStates = 7;
        static final int TRANSACTION_onStartInstall = 1;

        class Proxy implements ISplitInstallServiceCallback {
            public static ISplitInstallServiceCallback sDefaultImpl;
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

            public void onCancelInstall(int i, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(3, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onCancelInstall(i, bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onCompleteInstall(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onCompleteInstall(i);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onDeferredInstall(Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(6, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onDeferredInstall(bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onDeferredUninstall(Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(5, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onDeferredUninstall(bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onError(Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(8, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onError(bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onGetSession(int i, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(4, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onGetSession(i, bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onGetSessionStates(List list) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    if (this.mRemote.transact(7, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onGetSessionStates(list);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onStartInstall(int i, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onStartInstall(i, bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISplitInstallServiceCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISplitInstallServiceCallback)) ? new Proxy(iBinder) : (ISplitInstallServiceCallback) queryLocalInterface;
        }

        public static ISplitInstallServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ISplitInstallServiceCallback iSplitInstallServiceCallback) {
            if (Proxy.sDefaultImpl != null || iSplitInstallServiceCallback == null) {
                return false;
            }
            Proxy.sDefaultImpl = iSplitInstallServiceCallback;
            return true;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            Bundle bundle;
            Bundle bundle2;
            Bundle bundle3;
            Bundle bundle4;
            String str = DESCRIPTOR;
            if (i != 1598968902) {
                Bundle bundle5 = null;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        int readInt = parcel.readInt();
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        onStartInstall(readInt, bundle);
                        break;
                    case 2:
                        parcel.enforceInterface(str);
                        onCompleteInstall(parcel.readInt());
                        break;
                    case 3:
                        parcel.enforceInterface(str);
                        int readInt2 = parcel.readInt();
                        if (parcel.readInt() != 0) {
                            bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        onCancelInstall(readInt2, bundle2);
                        break;
                    case 4:
                        parcel.enforceInterface(str);
                        int readInt3 = parcel.readInt();
                        if (parcel.readInt() != 0) {
                            bundle3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        onGetSession(readInt3, bundle3);
                        break;
                    case 5:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            bundle4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        onDeferredUninstall(bundle4);
                        break;
                    case 6:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            bundle5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        onDeferredInstall(bundle5);
                        break;
                    case 7:
                        parcel.enforceInterface(str);
                        onGetSessionStates(parcel.createTypedArrayList(Bundle.CREATOR));
                        break;
                    case 8:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            bundle5 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        onError(bundle5);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                parcel2.writeNoException();
                return true;
            }
            parcel2.writeString(str);
            return true;
        }
    }

    void onCancelInstall(int i, Bundle bundle);

    void onCompleteInstall(int i);

    void onDeferredInstall(Bundle bundle);

    void onDeferredUninstall(Bundle bundle);

    void onError(Bundle bundle);

    void onGetSession(int i, Bundle bundle);

    void onGetSessionStates(List list);

    void onStartInstall(int i, Bundle bundle);
}

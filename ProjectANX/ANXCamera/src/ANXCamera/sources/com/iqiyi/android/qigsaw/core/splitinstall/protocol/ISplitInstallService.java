package com.iqiyi.android.qigsaw.core.splitinstall.protocol;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.ArrayList;
import java.util.List;

public interface ISplitInstallService extends IInterface {

    public class Default implements ISplitInstallService {
        public IBinder asBinder() {
            return null;
        }

        public void cancelInstall(String str, int i, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
        }

        public void deferredInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
        }

        public void deferredUninstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
        }

        public void getSessionState(String str, int i, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
        }

        public void getSessionStates(String str, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
        }

        public void startInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
        }
    }

    public abstract class Stub extends Binder implements ISplitInstallService {
        private static final String DESCRIPTOR = "com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallService";
        static final int TRANSACTION_cancelInstall = 2;
        static final int TRANSACTION_deferredInstall = 5;
        static final int TRANSACTION_deferredUninstall = 6;
        static final int TRANSACTION_getSessionState = 3;
        static final int TRANSACTION_getSessionStates = 4;
        static final int TRANSACTION_startInstall = 1;

        class Proxy implements ISplitInstallService {
            public static ISplitInstallService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void cancelInstall(String str, int i, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSplitInstallServiceCallback != null ? iSplitInstallServiceCallback.asBinder() : null);
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelInstall(str, i, bundle, iSplitInstallServiceCallback);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void deferredInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSplitInstallServiceCallback != null ? iSplitInstallServiceCallback.asBinder() : null);
                    if (this.mRemote.transact(5, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deferredInstall(str, list, bundle, iSplitInstallServiceCallback);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void deferredUninstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSplitInstallServiceCallback != null ? iSplitInstallServiceCallback.asBinder() : null);
                    if (this.mRemote.transact(6, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deferredUninstall(str, list, bundle, iSplitInstallServiceCallback);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void getSessionState(String str, int i, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iSplitInstallServiceCallback != null ? iSplitInstallServiceCallback.asBinder() : null);
                    if (this.mRemote.transact(3, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getSessionState(str, i, iSplitInstallServiceCallback);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getSessionStates(String str, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iSplitInstallServiceCallback != null ? iSplitInstallServiceCallback.asBinder() : null);
                    if (this.mRemote.transact(4, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getSessionStates(str, iSplitInstallServiceCallback);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedList(list);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSplitInstallServiceCallback != null ? iSplitInstallServiceCallback.asBinder() : null);
                    if (this.mRemote.transact(1, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startInstall(str, list, bundle, iSplitInstallServiceCallback);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISplitInstallService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ISplitInstallService)) ? new Proxy(iBinder) : (ISplitInstallService) queryLocalInterface;
        }

        public static ISplitInstallService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(ISplitInstallService iSplitInstallService) {
            if (Proxy.sDefaultImpl != null || iSplitInstallService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iSplitInstallService;
            return true;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            Bundle bundle;
            Bundle bundle2;
            String str = DESCRIPTOR;
            if (i != 1598968902) {
                Bundle bundle3 = null;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        String readString = parcel.readString();
                        ArrayList createTypedArrayList = parcel.createTypedArrayList(Bundle.CREATOR);
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        startInstall(readString, createTypedArrayList, bundle, com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 2:
                        parcel.enforceInterface(str);
                        String readString2 = parcel.readString();
                        int readInt = parcel.readInt();
                        if (parcel.readInt() != 0) {
                            bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        cancelInstall(readString2, readInt, bundle2, com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 3:
                        parcel.enforceInterface(str);
                        getSessionState(parcel.readString(), parcel.readInt(), com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 4:
                        parcel.enforceInterface(str);
                        getSessionStates(parcel.readString(), com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 5:
                        parcel.enforceInterface(str);
                        String readString3 = parcel.readString();
                        ArrayList createTypedArrayList2 = parcel.createTypedArrayList(Bundle.CREATOR);
                        if (parcel.readInt() != 0) {
                            bundle3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        deferredInstall(readString3, createTypedArrayList2, bundle3, com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 6:
                        parcel.enforceInterface(str);
                        String readString4 = parcel.readString();
                        ArrayList createTypedArrayList3 = parcel.createTypedArrayList(Bundle.CREATOR);
                        if (parcel.readInt() != 0) {
                            bundle3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        deferredUninstall(readString4, createTypedArrayList3, bundle3, com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
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

    void cancelInstall(String str, int i, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback);

    void deferredInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback);

    void deferredUninstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback);

    void getSessionState(String str, int i, ISplitInstallServiceCallback iSplitInstallServiceCallback);

    void getSessionStates(String str, ISplitInstallServiceCallback iSplitInstallServiceCallback);

    void startInstall(String str, List list, Bundle bundle, ISplitInstallServiceCallback iSplitInstallServiceCallback);
}

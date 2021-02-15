package com.xiaomi.camera.rcs;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.view.KeyEvent;
import android.view.MotionEvent;

public interface IRemoteControl extends IInterface {

    public class Default implements IRemoteControl {
        public IBinder asBinder() {
            return null;
        }

        public Bundle customClientRequest(IRemoteControlClient iRemoteControlClient, String str, Bundle bundle) {
            return null;
        }

        public Bundle customRequest(String str, Bundle bundle) {
            return null;
        }

        public void injectKeyEvent(IRemoteControlClient iRemoteControlClient, KeyEvent keyEvent) {
        }

        public void injectMotionEvent(IRemoteControlClient iRemoteControlClient, MotionEvent motionEvent) {
        }

        public boolean isStreaming(IRemoteControlClient iRemoteControlClient) {
            return false;
        }

        public int registerRemoteController(IRemoteControlClient iRemoteControlClient) {
            return 0;
        }

        public void startStreaming(IRemoteControlClient iRemoteControlClient, Bundle bundle) {
        }

        public void stopStreaming(IRemoteControlClient iRemoteControlClient, Bundle bundle) {
        }

        public void unregisterRemoteController(IRemoteControlClient iRemoteControlClient) {
        }
    }

    public abstract class Stub extends Binder implements IRemoteControl {
        private static final String DESCRIPTOR = "com.xiaomi.camera.rcs.IRemoteControl";
        static final int TRANSACTION_customClientRequest = 8;
        static final int TRANSACTION_customRequest = 9;
        static final int TRANSACTION_injectKeyEvent = 3;
        static final int TRANSACTION_injectMotionEvent = 4;
        static final int TRANSACTION_isStreaming = 6;
        static final int TRANSACTION_registerRemoteController = 1;
        static final int TRANSACTION_startStreaming = 5;
        static final int TRANSACTION_stopStreaming = 7;
        static final int TRANSACTION_unregisterRemoteController = 2;

        class Proxy implements IRemoteControl {
            public static IRemoteControl sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public Bundle customClientRequest(IRemoteControlClient iRemoteControlClient, String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    Bundle bundle2 = null;
                    obtain.writeStrongBinder(iRemoteControlClient != null ? iRemoteControlClient.asBinder() : null);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().customClientRequest(iRemoteControlClient, str, bundle);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(obtain2);
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle customRequest(String str, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(9, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().customRequest(str, bundle);
                    }
                    obtain2.readException();
                    Bundle bundle2 = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void injectKeyEvent(IRemoteControlClient iRemoteControlClient, KeyEvent keyEvent) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRemoteControlClient != null ? iRemoteControlClient.asBinder() : null);
                    if (keyEvent != null) {
                        obtain.writeInt(1);
                        keyEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(3, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().injectKeyEvent(iRemoteControlClient, keyEvent);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void injectMotionEvent(IRemoteControlClient iRemoteControlClient, MotionEvent motionEvent) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRemoteControlClient != null ? iRemoteControlClient.asBinder() : null);
                    if (motionEvent != null) {
                        obtain.writeInt(1);
                        motionEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(4, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().injectMotionEvent(iRemoteControlClient, motionEvent);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isStreaming(IRemoteControlClient iRemoteControlClient) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRemoteControlClient != null ? iRemoteControlClient.asBinder() : null);
                    boolean z = false;
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStreaming(iRemoteControlClient);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int registerRemoteController(IRemoteControlClient iRemoteControlClient) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRemoteControlClient != null ? iRemoteControlClient.asBinder() : null);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerRemoteController(iRemoteControlClient);
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startStreaming(IRemoteControlClient iRemoteControlClient, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRemoteControlClient != null ? iRemoteControlClient.asBinder() : null);
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
                    Stub.getDefaultImpl().startStreaming(iRemoteControlClient, bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopStreaming(IRemoteControlClient iRemoteControlClient, Bundle bundle) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRemoteControlClient != null ? iRemoteControlClient.asBinder() : null);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(7, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopStreaming(iRemoteControlClient, bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterRemoteController(IRemoteControlClient iRemoteControlClient) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iRemoteControlClient != null ? iRemoteControlClient.asBinder() : null);
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterRemoteController(iRemoteControlClient);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteControl asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IRemoteControl)) ? new Proxy(iBinder) : (IRemoteControl) queryLocalInterface;
        }

        public static IRemoteControl getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IRemoteControl iRemoteControl) {
            if (Proxy.sDefaultImpl != null || iRemoteControl == null) {
                return false;
            }
            Proxy.sDefaultImpl = iRemoteControl;
            return true;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX WARNING: type inference failed for: r3v0 */
        /* JADX WARNING: type inference failed for: r3v1, types: [android.view.KeyEvent] */
        /* JADX WARNING: type inference failed for: r3v3, types: [android.view.KeyEvent] */
        /* JADX WARNING: type inference failed for: r3v4, types: [android.view.MotionEvent] */
        /* JADX WARNING: type inference failed for: r3v6, types: [android.view.MotionEvent] */
        /* JADX WARNING: type inference failed for: r3v7, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r3v9, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r3v10, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r3v12, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r3v13, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r3v15, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r3v16, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r3v18, types: [android.os.Bundle] */
        /* JADX WARNING: type inference failed for: r3v19 */
        /* JADX WARNING: type inference failed for: r3v20 */
        /* JADX WARNING: type inference failed for: r3v21 */
        /* JADX WARNING: type inference failed for: r3v22 */
        /* JADX WARNING: type inference failed for: r3v23 */
        /* JADX WARNING: type inference failed for: r3v24 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v19
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.os.Bundle]
  uses: [android.view.KeyEvent, android.os.Bundle]
  mth insns count: 117
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 11 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            ? r3;
            ? r32;
            ? r33;
            ? r34;
            ? r35;
            String str = DESCRIPTOR;
            if (i != 1598968902) {
                int i3 = 0;
                ? r36 = 0;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        int registerRemoteController = registerRemoteController(com.xiaomi.camera.rcs.IRemoteControlClient.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        parcel2.writeInt(registerRemoteController);
                        return true;
                    case 2:
                        parcel.enforceInterface(str);
                        unregisterRemoteController(com.xiaomi.camera.rcs.IRemoteControlClient.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(str);
                        IRemoteControlClient asInterface = com.xiaomi.camera.rcs.IRemoteControlClient.Stub.asInterface(parcel.readStrongBinder());
                        if (parcel.readInt() != 0) {
                            r3 = (KeyEvent) KeyEvent.CREATOR.createFromParcel(parcel);
                        }
                        injectKeyEvent(asInterface, r3);
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(str);
                        IRemoteControlClient asInterface2 = com.xiaomi.camera.rcs.IRemoteControlClient.Stub.asInterface(parcel.readStrongBinder());
                        if (parcel.readInt() != 0) {
                            r32 = (MotionEvent) MotionEvent.CREATOR.createFromParcel(parcel);
                        }
                        injectMotionEvent(asInterface2, r32);
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(str);
                        IRemoteControlClient asInterface3 = com.xiaomi.camera.rcs.IRemoteControlClient.Stub.asInterface(parcel.readStrongBinder());
                        if (parcel.readInt() != 0) {
                            r33 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        startStreaming(asInterface3, r33);
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(str);
                        boolean isStreaming = isStreaming(com.xiaomi.camera.rcs.IRemoteControlClient.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        if (isStreaming) {
                            i3 = 1;
                        }
                        parcel2.writeInt(i3);
                        return true;
                    case 7:
                        parcel.enforceInterface(str);
                        IRemoteControlClient asInterface4 = com.xiaomi.camera.rcs.IRemoteControlClient.Stub.asInterface(parcel.readStrongBinder());
                        if (parcel.readInt() != 0) {
                            r34 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        stopStreaming(asInterface4, r34);
                        parcel2.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(str);
                        IRemoteControlClient asInterface5 = com.xiaomi.camera.rcs.IRemoteControlClient.Stub.asInterface(parcel.readStrongBinder());
                        String readString = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r35 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        Bundle customClientRequest = customClientRequest(asInterface5, readString, r35);
                        parcel2.writeNoException();
                        if (customClientRequest != null) {
                            parcel2.writeInt(1);
                            customClientRequest.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(str);
                        String readString2 = parcel.readString();
                        if (parcel.readInt() != 0) {
                            r36 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        Bundle customRequest = customRequest(readString2, r36);
                        parcel2.writeNoException();
                        if (customRequest != null) {
                            parcel2.writeInt(1);
                            customRequest.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
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

    Bundle customClientRequest(IRemoteControlClient iRemoteControlClient, String str, Bundle bundle);

    Bundle customRequest(String str, Bundle bundle);

    void injectKeyEvent(IRemoteControlClient iRemoteControlClient, KeyEvent keyEvent);

    void injectMotionEvent(IRemoteControlClient iRemoteControlClient, MotionEvent motionEvent);

    boolean isStreaming(IRemoteControlClient iRemoteControlClient);

    int registerRemoteController(IRemoteControlClient iRemoteControlClient);

    void startStreaming(IRemoteControlClient iRemoteControlClient, Bundle bundle);

    void stopStreaming(IRemoteControlClient iRemoteControlClient, Bundle bundle);

    void unregisterRemoteController(IRemoteControlClient iRemoteControlClient);
}

package miui.hardware.display;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IMiuiMultiDisplayManager extends IInterface {

    public class Default implements IMiuiMultiDisplayManager {
        public IBinder asBinder() {
            return null;
        }

        public int[] getScreenEffectAvailableDisplay() {
            return null;
        }

        public boolean setDisplayStateIgnoreFold(int i, boolean z) {
            return false;
        }
    }

    public abstract class Stub extends Binder implements IMiuiMultiDisplayManager {
        private static final String DESCRIPTOR = "miui.hardware.display.IMiuiMultiDisplayManager";
        static final int TRANSACTION_getScreenEffectAvailableDisplay = 1;
        static final int TRANSACTION_setDisplayStateIgnoreFold = 2;

        class Proxy implements IMiuiMultiDisplayManager {
            public static IMiuiMultiDisplayManager sDefaultImpl;
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

            public int[] getScreenEffectAvailableDisplay() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getScreenEffectAvailableDisplay();
                    }
                    obtain2.readException();
                    int[] createIntArray = obtain2.createIntArray();
                    obtain2.recycle();
                    obtain.recycle();
                    return createIntArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setDisplayStateIgnoreFold(int i, boolean z) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    boolean z2 = true;
                    obtain.writeInt(z ? 1 : 0);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setDisplayStateIgnoreFold(i, z);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z2 = false;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiMultiDisplayManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IMiuiMultiDisplayManager)) ? new Proxy(iBinder) : (IMiuiMultiDisplayManager) queryLocalInterface;
        }

        public static IMiuiMultiDisplayManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IMiuiMultiDisplayManager iMiuiMultiDisplayManager) {
            if (Proxy.sDefaultImpl != null || iMiuiMultiDisplayManager == null) {
                return false;
            }
            Proxy.sDefaultImpl = iMiuiMultiDisplayManager;
            return true;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            String str = DESCRIPTOR;
            if (i == 1) {
                parcel.enforceInterface(str);
                int[] screenEffectAvailableDisplay = getScreenEffectAvailableDisplay();
                parcel2.writeNoException();
                parcel2.writeIntArray(screenEffectAvailableDisplay);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(str);
                int i3 = 0;
                boolean displayStateIgnoreFold = setDisplayStateIgnoreFold(parcel.readInt(), parcel.readInt() != 0);
                parcel2.writeNoException();
                if (displayStateIgnoreFold) {
                    i3 = 1;
                }
                parcel2.writeInt(i3);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
        }
    }

    int[] getScreenEffectAvailableDisplay();

    boolean setDisplayStateIgnoreFold(int i, boolean z);
}

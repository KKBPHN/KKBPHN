package O00000Oo.O00000o.O000000o;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ResultReceiver;

public abstract class O00000o0 extends Binder implements O00000o {
    private static final String DESCRIPTOR = "com.market.pm.IMarketInstallerService";
    static final int TRANSACTION_installPackage = 1;

    public O00000o0() {
        attachInterface(this, DESCRIPTOR);
    }

    public static boolean O000000o(O00000o o00000o) {
        if (O00000Oo.sDefaultImpl != null || o00000o == null) {
            return false;
        }
        O00000Oo.sDefaultImpl = o00000o;
        return true;
    }

    public static O00000o asInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
        return (queryLocalInterface == null || !(queryLocalInterface instanceof O00000o)) ? new O00000Oo(iBinder) : (O00000o) queryLocalInterface;
    }

    public static O00000o getDefaultImpl() {
        return O00000Oo.sDefaultImpl;
    }

    public IBinder asBinder() {
        return this;
    }

    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
        String str = DESCRIPTOR;
        if (i == 1) {
            parcel.enforceInterface(str);
            Bundle bundle = null;
            Uri uri = parcel.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(parcel) : null;
            ResultReceiver resultReceiver = parcel.readInt() != 0 ? (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel) : null;
            if (parcel.readInt() != 0) {
                bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
            }
            O000000o(uri, resultReceiver, bundle);
            parcel2.writeNoException();
            return true;
        } else if (i != 1598968902) {
            return super.onTransact(i, parcel, parcel2, i2);
        } else {
            parcel2.writeString(str);
            return true;
        }
    }
}

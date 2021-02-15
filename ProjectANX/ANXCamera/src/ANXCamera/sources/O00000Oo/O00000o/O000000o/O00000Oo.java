package O00000Oo.O00000o.O000000o;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ResultReceiver;

class O00000Oo implements O00000o {
    public static O00000o sDefaultImpl;
    private IBinder mRemote;

    O00000Oo(IBinder iBinder) {
        this.mRemote = iBinder;
    }

    public void O000000o(Uri uri, ResultReceiver resultReceiver, Bundle bundle) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.market.pm.IMarketInstallerService");
            if (uri != null) {
                obtain.writeInt(1);
                uri.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            if (resultReceiver != null) {
                obtain.writeInt(1);
                resultReceiver.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            if (bundle != null) {
                obtain.writeInt(1);
                bundle.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            if (this.mRemote.transact(1, obtain, obtain2, 0) || O00000o0.getDefaultImpl() == null) {
                obtain2.readException();
                obtain2.recycle();
                obtain.recycle();
                return;
            }
            O00000o0.getDefaultImpl().O000000o(uri, resultReceiver, bundle);
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    public IBinder asBinder() {
        return this.mRemote;
    }

    public String getInterfaceDescriptor() {
        return "com.market.pm.IMarketInstallerService";
    }
}

package O000000o.O00000Oo.O000000o.O000000o;

import android.os.HwBinder;
import android.os.IHwBinder;
import android.os.IHwBinder.DeathRecipient;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: O000000o.O00000Oo.O000000o.O000000o.O00000oO reason: case insensitive filesystem */
public interface C0005O00000oO extends IHwInterface {
    public static final String kInterfaceName = "android.hidl.base@1.0::IBase";

    static C0005O00000oO asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        String str = kInterfaceName;
        C0005O00000oO queryLocalInterface = iHwBinder.queryLocalInterface(str);
        if (queryLocalInterface != null && (queryLocalInterface instanceof C0005O00000oO)) {
            return queryLocalInterface;
        }
        O00000o0 o00000o0 = new O00000o0(iHwBinder);
        try {
            Iterator it = o00000o0.interfaceChain().iterator();
            while (it.hasNext()) {
                if (((String) it.next()).equals(str)) {
                    return o00000o0;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    static C0005O00000oO castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static C0005O00000oO getService() {
        return getService("default");
    }

    static C0005O00000oO getService(String str) {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    static C0005O00000oO getService(String str, boolean z) {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static C0005O00000oO getService(boolean z) {
        return getService("default", z);
    }

    IHwBinder asBinder();

    void debug(NativeHandle nativeHandle, ArrayList arrayList);

    O00000Oo getDebugInfo();

    ArrayList getHashChain();

    ArrayList interfaceChain();

    String interfaceDescriptor();

    boolean linkToDeath(DeathRecipient deathRecipient, long j);

    void notifySyspropsChanged();

    void ping();

    void setHALInstrumentation();

    boolean unlinkToDeath(DeathRecipient deathRecipient);
}

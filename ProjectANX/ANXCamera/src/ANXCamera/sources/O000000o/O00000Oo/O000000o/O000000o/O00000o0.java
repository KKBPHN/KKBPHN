package O000000o.O00000Oo.O000000o.O000000o;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwBinder.DeathRecipient;
import android.os.NativeHandle;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

public final class O00000o0 implements C0005O00000oO {
    private IHwBinder mRemote;

    public O00000o0(IHwBinder iHwBinder) {
        this.mRemote = (IHwBinder) Objects.requireNonNull(iHwBinder);
    }

    public IHwBinder asBinder() {
        return this.mRemote;
    }

    public void debug(NativeHandle nativeHandle, ArrayList arrayList) {
        HwParcel hwParcel = new HwParcel();
        hwParcel.writeInterfaceToken(C0005O00000oO.kInterfaceName);
        hwParcel.writeNativeHandle(nativeHandle);
        hwParcel.writeStringVector(arrayList);
        HwParcel hwParcel2 = new HwParcel();
        try {
            this.mRemote.transact(256131655, hwParcel, hwParcel2, 0);
            hwParcel2.verifySuccess();
            hwParcel.releaseTemporaryStorage();
        } finally {
            hwParcel2.release();
        }
    }

    public final boolean equals(Object obj) {
        return HidlSupport.interfacesEqual(this, obj);
    }

    public O00000Oo getDebugInfo() {
        HwParcel hwParcel = new HwParcel();
        hwParcel.writeInterfaceToken(C0005O00000oO.kInterfaceName);
        HwParcel hwParcel2 = new HwParcel();
        try {
            this.mRemote.transact(257049926, hwParcel, hwParcel2, 0);
            hwParcel2.verifySuccess();
            hwParcel.releaseTemporaryStorage();
            O00000Oo o00000Oo = new O00000Oo();
            o00000Oo.readFromParcel(hwParcel2);
            return o00000Oo;
        } finally {
            hwParcel2.release();
        }
    }

    public ArrayList getHashChain() {
        HwParcel hwParcel = new HwParcel();
        hwParcel.writeInterfaceToken(C0005O00000oO.kInterfaceName);
        HwParcel hwParcel2 = new HwParcel();
        try {
            this.mRemote.transact(256398152, hwParcel, hwParcel2, 0);
            hwParcel2.verifySuccess();
            hwParcel.releaseTemporaryStorage();
            ArrayList arrayList = new ArrayList();
            HwBlob readBuffer = hwParcel2.readBuffer(16);
            int int32 = readBuffer.getInt32(8);
            HwBlob readEmbeddedBuffer = hwParcel2.readEmbeddedBuffer((long) (int32 * 32), readBuffer.handle(), 0, true);
            arrayList.clear();
            for (int i = 0; i < int32; i++) {
                byte[] bArr = new byte[32];
                readEmbeddedBuffer.copyToInt8Array((long) (i * 32), bArr, 32);
                arrayList.add(bArr);
            }
            return arrayList;
        } finally {
            hwParcel2.release();
        }
    }

    public final int hashCode() {
        return asBinder().hashCode();
    }

    public ArrayList interfaceChain() {
        HwParcel hwParcel = new HwParcel();
        hwParcel.writeInterfaceToken(C0005O00000oO.kInterfaceName);
        HwParcel hwParcel2 = new HwParcel();
        try {
            this.mRemote.transact(256067662, hwParcel, hwParcel2, 0);
            hwParcel2.verifySuccess();
            hwParcel.releaseTemporaryStorage();
            return hwParcel2.readStringVector();
        } finally {
            hwParcel2.release();
        }
    }

    public String interfaceDescriptor() {
        HwParcel hwParcel = new HwParcel();
        hwParcel.writeInterfaceToken(C0005O00000oO.kInterfaceName);
        HwParcel hwParcel2 = new HwParcel();
        try {
            this.mRemote.transact(256136003, hwParcel, hwParcel2, 0);
            hwParcel2.verifySuccess();
            hwParcel.releaseTemporaryStorage();
            return hwParcel2.readString();
        } finally {
            hwParcel2.release();
        }
    }

    public boolean linkToDeath(DeathRecipient deathRecipient, long j) {
        return this.mRemote.linkToDeath(deathRecipient, j);
    }

    public void notifySyspropsChanged() {
        HwParcel hwParcel = new HwParcel();
        hwParcel.writeInterfaceToken(C0005O00000oO.kInterfaceName);
        HwParcel hwParcel2 = new HwParcel();
        try {
            this.mRemote.transact(257120595, hwParcel, hwParcel2, 1);
            hwParcel.releaseTemporaryStorage();
        } finally {
            hwParcel2.release();
        }
    }

    public void ping() {
        HwParcel hwParcel = new HwParcel();
        hwParcel.writeInterfaceToken(C0005O00000oO.kInterfaceName);
        HwParcel hwParcel2 = new HwParcel();
        try {
            this.mRemote.transact(256921159, hwParcel, hwParcel2, 0);
            hwParcel2.verifySuccess();
            hwParcel.releaseTemporaryStorage();
        } finally {
            hwParcel2.release();
        }
    }

    public void setHALInstrumentation() {
        HwParcel hwParcel = new HwParcel();
        hwParcel.writeInterfaceToken(C0005O00000oO.kInterfaceName);
        HwParcel hwParcel2 = new HwParcel();
        try {
            this.mRemote.transact(256462420, hwParcel, hwParcel2, 1);
            hwParcel.releaseTemporaryStorage();
        } finally {
            hwParcel2.release();
        }
    }

    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(interfaceDescriptor());
            sb.append("@Proxy");
            return sb.toString();
        } catch (RemoteException unused) {
            return "[class or subclass of android.hidl.base@1.0::IBase]@Proxy";
        }
    }

    public boolean unlinkToDeath(DeathRecipient deathRecipient) {
        return this.mRemote.unlinkToDeath(deathRecipient);
    }
}

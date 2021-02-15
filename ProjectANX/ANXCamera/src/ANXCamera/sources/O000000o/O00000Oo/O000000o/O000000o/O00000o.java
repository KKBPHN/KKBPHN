package O000000o.O00000Oo.O000000o.O000000o;

import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwBinder.DeathRecipient;
import android.os.IHwInterface;
import android.os.NativeHandle;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class O00000o extends HwBinder implements C0005O00000oO {
    public IHwBinder asBinder() {
        return this;
    }

    public void debug(NativeHandle nativeHandle, ArrayList arrayList) {
    }

    public final O00000Oo getDebugInfo() {
        O00000Oo o00000Oo = new O00000Oo();
        o00000Oo.pid = HidlSupport.getPidIfSharable();
        o00000Oo.ptr = 0;
        o00000Oo.arch = 0;
        return o00000Oo;
    }

    public final ArrayList getHashChain() {
        return new ArrayList(Arrays.asList(new byte[][]{new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}}));
    }

    public final ArrayList interfaceChain() {
        return new ArrayList(Arrays.asList(new String[]{C0005O00000oO.kInterfaceName}));
    }

    public final String interfaceDescriptor() {
        return C0005O00000oO.kInterfaceName;
    }

    public final boolean linkToDeath(DeathRecipient deathRecipient, long j) {
        return true;
    }

    public final void notifySyspropsChanged() {
        HwBinder.enableInstrumentation();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00f1, code lost:
        r9.writeStatus(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x010e, code lost:
        r9.send();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTransact(int i, HwParcel hwParcel, HwParcel hwParcel2, int i2) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        int i3;
        int i4;
        int i5;
        String str = C0005O00000oO.kInterfaceName;
        int i6 = 0;
        boolean z5 = true;
        switch (i) {
            case 256067662:
                if ((i2 & 1) == 0) {
                    z = false;
                }
                if (!z) {
                    hwParcel.enforceInterface(str);
                    ArrayList interfaceChain = interfaceChain();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeStringVector(interfaceChain);
                    break;
                }
            case 256131655:
                if ((i2 & 1) == 0) {
                    z2 = false;
                }
                if (!z2) {
                    hwParcel.enforceInterface(str);
                    debug(hwParcel.readNativeHandle(), hwParcel.readStringVector());
                    break;
                }
                hwParcel2.writeStatus(Integer.MIN_VALUE);
                break;
            case 256136003:
                if ((i2 & 1) == 0) {
                    z3 = false;
                }
                if (!z3) {
                    hwParcel.enforceInterface(str);
                    String interfaceDescriptor = interfaceDescriptor();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeString(interfaceDescriptor);
                    break;
                }
            case 256398152:
                if ((i2 & 1) == 0) {
                    z4 = false;
                }
                if (!z4) {
                    hwParcel.enforceInterface(str);
                    ArrayList hashChain = getHashChain();
                    hwParcel2.writeStatus(0);
                    HwBlob hwBlob = new HwBlob(16);
                    int size = hashChain.size();
                    hwBlob.putInt32(8, size);
                    hwBlob.putBool(12, false);
                    HwBlob hwBlob2 = new HwBlob(size * 32);
                    while (i3 < size) {
                        long j = (long) (i3 * 32);
                        byte[] bArr = (byte[]) hashChain.get(i3);
                        if (bArr == null || bArr.length != 32) {
                            throw new IllegalArgumentException("Array element is not of the expected length");
                        }
                        hwBlob2.putInt8Array(j, bArr);
                        i3++;
                    }
                    hwBlob.putBlob(0, hwBlob2);
                    hwParcel2.writeBuffer(hwBlob);
                    break;
                }
                break;
            case 256462420:
                if ((i2 & 1) != 0) {
                    i4 = 1;
                }
                if (i4 == 1) {
                    hwParcel.enforceInterface(str);
                    setHALInstrumentation();
                    return;
                }
                hwParcel2.writeStatus(Integer.MIN_VALUE);
                break;
            case 256660548:
                if ((i2 & 1) != 0) {
                    i5 = 1;
                }
                if (i5 == 0) {
                    return;
                }
                hwParcel2.writeStatus(Integer.MIN_VALUE);
                break;
            case 256921159:
                if ((i2 & 1) == 0) {
                    z5 = false;
                }
                if (!z5) {
                    hwParcel.enforceInterface(str);
                    ping();
                    break;
                }
                hwParcel2.writeStatus(Integer.MIN_VALUE);
                break;
            case 257049926:
                if ((i2 & 1) == 0) {
                    z5 = false;
                }
                if (!z5) {
                    hwParcel.enforceInterface(str);
                    O00000Oo debugInfo = getDebugInfo();
                    hwParcel2.writeStatus(0);
                    debugInfo.writeToParcel(hwParcel2);
                    break;
                }
            case 257120595:
                if ((i2 & 1) != 0) {
                    i6 = 1;
                }
                if (i6 == 1) {
                    hwParcel.enforceInterface(str);
                    notifySyspropsChanged();
                    return;
                }
                hwParcel2.writeStatus(Integer.MIN_VALUE);
                break;
            case 257250372:
                if ((i2 & 1) != 0) {
                    i6 = 1;
                }
                if (i6 == 0) {
                    return;
                }
                hwParcel2.writeStatus(Integer.MIN_VALUE);
                break;
            default:
                return;
        }
    }

    public final void ping() {
    }

    public IHwInterface queryLocalInterface(String str) {
        if (C0005O00000oO.kInterfaceName.equals(str)) {
            return this;
        }
        return null;
    }

    public void registerAsService(String str) {
        registerService(str);
    }

    public final void setHALInstrumentation() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(interfaceDescriptor());
        sb.append("@Stub");
        return sb.toString();
    }

    public final boolean unlinkToDeath(DeathRecipient deathRecipient) {
        return true;
    }
}

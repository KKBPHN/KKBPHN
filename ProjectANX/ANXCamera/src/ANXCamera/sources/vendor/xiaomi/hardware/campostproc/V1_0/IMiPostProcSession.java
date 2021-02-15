package vendor.xiaomi.hardware.campostproc.V1_0;

import O000000o.O00000Oo.O000000o.O000000o.C0005O00000oO;
import O000000o.O00000Oo.O000000o.O000000o.O00000Oo;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwBinder.DeathRecipient;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public interface IMiPostProcSession extends C0005O00000oO {
    public static final String kInterfaceName = "vendor.xiaomi.hardware.campostproc@1.0::IMiPostProcSession";

    public final class Proxy implements IMiPostProcSession {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(iHwBinder);
        }

        public int abort() {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IMiPostProcSession.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
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

        public void process(SessionProcessParams sessionProcessParams, processCallback processcallback) {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(IMiPostProcSession.kInterfaceName);
            sessionProcessParams.writeToParcel(hwParcel);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                processcallback.onValues(hwParcel2.readInt32(), hwParcel2.readInt32());
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
                return "[class or subclass of vendor.xiaomi.hardware.campostproc@1.0::IMiPostProcSession]@Proxy";
            }
        }

        public boolean unlinkToDeath(DeathRecipient deathRecipient) {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    public abstract class Stub extends HwBinder implements IMiPostProcSession {
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
            return new ArrayList(Arrays.asList(new byte[][]{new byte[]{-24, -41, 62, 37, 67, 78, -13, -18, -7, -77, -80, 98, -100, 64, -100, -66, 57, -86, -22, -25, -24, -38, 98, -48, -42, -27, 24, 30, -46, 20, 68, 72}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}}));
        }

        public final ArrayList interfaceChain() {
            return new ArrayList(Arrays.asList(new String[]{IMiPostProcSession.kInterfaceName, C0005O00000oO.kInterfaceName}));
        }

        public final String interfaceDescriptor() {
            return IMiPostProcSession.kInterfaceName;
        }

        public final boolean linkToDeath(DeathRecipient deathRecipient, long j) {
            return true;
        }

        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:58:0x00fa, code lost:
            r9.writeStatus(0);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onTransact(int i, HwParcel hwParcel, final HwParcel hwParcel2, int i2) {
            int i3;
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            boolean z5;
            int i4;
            int i5;
            int i6;
            String str = IMiPostProcSession.kInterfaceName;
            int i7 = 0;
            boolean z6 = true;
            if (i == 1) {
                if ((i2 & 1) != 0) {
                    i3 = 1;
                }
                if (i3 == 0) {
                    hwParcel.enforceInterface(str);
                    SessionProcessParams sessionProcessParams = new SessionProcessParams();
                    sessionProcessParams.readFromParcel(hwParcel);
                    process(sessionProcessParams, new processCallback() {
                        public void onValues(int i, int i2) {
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeInt32(i);
                            hwParcel2.writeInt32(i2);
                            hwParcel2.send();
                        }
                    });
                    return;
                }
            } else if (i != 2) {
                String str2 = C0005O00000oO.kInterfaceName;
                switch (i) {
                    case 256067662:
                        if ((i2 & 1) == 0) {
                            z2 = false;
                        }
                        if (!z2) {
                            hwParcel.enforceInterface(str2);
                            ArrayList interfaceChain = interfaceChain();
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeStringVector(interfaceChain);
                            break;
                        }
                    case 256131655:
                        if ((i2 & 1) == 0) {
                            z3 = false;
                        }
                        if (!z3) {
                            hwParcel.enforceInterface(str2);
                            debug(hwParcel.readNativeHandle(), hwParcel.readStringVector());
                            break;
                        }
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        break;
                    case 256136003:
                        if ((i2 & 1) == 0) {
                            z4 = false;
                        }
                        if (!z4) {
                            hwParcel.enforceInterface(str2);
                            String interfaceDescriptor = interfaceDescriptor();
                            hwParcel2.writeStatus(0);
                            hwParcel2.writeString(interfaceDescriptor);
                            break;
                        }
                    case 256398152:
                        if ((i2 & 1) == 0) {
                            z5 = false;
                        }
                        if (!z5) {
                            hwParcel.enforceInterface(str2);
                            ArrayList hashChain = getHashChain();
                            hwParcel2.writeStatus(0);
                            HwBlob hwBlob = new HwBlob(16);
                            int size = hashChain.size();
                            hwBlob.putInt32(8, size);
                            hwBlob.putBool(12, false);
                            HwBlob hwBlob2 = new HwBlob(size * 32);
                            while (i4 < size) {
                                long j = (long) (i4 * 32);
                                byte[] bArr = (byte[]) hashChain.get(i4);
                                if (bArr == null || bArr.length != 32) {
                                    throw new IllegalArgumentException("Array element is not of the expected length");
                                }
                                hwBlob2.putInt8Array(j, bArr);
                                i4++;
                            }
                            hwBlob.putBlob(0, hwBlob2);
                            hwParcel2.writeBuffer(hwBlob);
                            break;
                        }
                        break;
                    case 256462420:
                        if ((i2 & 1) != 0) {
                            i5 = 1;
                        }
                        if (i5 == 1) {
                            hwParcel.enforceInterface(str2);
                            setHALInstrumentation();
                            return;
                        }
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        break;
                    case 256660548:
                        if ((i2 & 1) != 0) {
                            i6 = 1;
                        }
                        if (i6 == 0) {
                            return;
                        }
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        break;
                    case 256921159:
                        if ((i2 & 1) == 0) {
                            z6 = false;
                        }
                        if (!z6) {
                            hwParcel.enforceInterface(str2);
                            ping();
                            break;
                        }
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        break;
                    case 257049926:
                        if ((i2 & 1) == 0) {
                            z6 = false;
                        }
                        if (!z6) {
                            hwParcel.enforceInterface(str2);
                            O00000Oo debugInfo = getDebugInfo();
                            hwParcel2.writeStatus(0);
                            debugInfo.writeToParcel(hwParcel2);
                            break;
                        }
                    case 257120595:
                        if ((i2 & 1) != 0) {
                            i7 = 1;
                        }
                        if (i7 == 1) {
                            hwParcel.enforceInterface(str2);
                            notifySyspropsChanged();
                            return;
                        }
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        break;
                    case 257250372:
                        if ((i2 & 1) != 0) {
                            i7 = 1;
                        }
                        if (i7 == 0) {
                            return;
                        }
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        break;
                    default:
                        return;
                }
            } else {
                if ((i2 & 1) == 0) {
                    z = false;
                }
                if (!z) {
                    hwParcel.enforceInterface(str);
                    int abort = abort();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeInt32(abort);
                    hwParcel2.send();
                }
            }
            hwParcel2.writeStatus(Integer.MIN_VALUE);
            hwParcel2.send();
        }

        public final void ping() {
        }

        public IHwInterface queryLocalInterface(String str) {
            if (IMiPostProcSession.kInterfaceName.equals(str)) {
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

    @FunctionalInterface
    public interface processCallback {
        void onValues(int i, int i2);
    }

    static IMiPostProcSession asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        String str = kInterfaceName;
        IMiPostProcSession queryLocalInterface = iHwBinder.queryLocalInterface(str);
        if (queryLocalInterface != null && (queryLocalInterface instanceof IMiPostProcSession)) {
            return queryLocalInterface;
        }
        Proxy proxy = new Proxy(iHwBinder);
        try {
            Iterator it = proxy.interfaceChain().iterator();
            while (it.hasNext()) {
                if (((String) it.next()).equals(str)) {
                    return proxy;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    static IMiPostProcSession castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static IMiPostProcSession getService() {
        return getService("default");
    }

    static IMiPostProcSession getService(String str) {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    static IMiPostProcSession getService(String str, boolean z) {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static IMiPostProcSession getService(boolean z) {
        return getService("default", z);
    }

    int abort();

    IHwBinder asBinder();

    void debug(NativeHandle nativeHandle, ArrayList arrayList);

    O00000Oo getDebugInfo();

    ArrayList getHashChain();

    ArrayList interfaceChain();

    String interfaceDescriptor();

    boolean linkToDeath(DeathRecipient deathRecipient, long j);

    void notifySyspropsChanged();

    void ping();

    void process(SessionProcessParams sessionProcessParams, processCallback processcallback);

    void setHALInstrumentation();

    boolean unlinkToDeath(DeathRecipient deathRecipient);
}

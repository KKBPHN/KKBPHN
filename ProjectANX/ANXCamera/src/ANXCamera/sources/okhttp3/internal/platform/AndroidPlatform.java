package okhttp3.internal.platform;

import android.util.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.Security;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okhttp3.internal.tls.TrustRootIndex;

class AndroidPlatform extends Platform {
    private static final int MAX_LOG_LENGTH = 4000;
    private final CloseGuard closeGuard = CloseGuard.get();
    private final OptionalMethod getAlpnSelectedProtocol;
    private final OptionalMethod setAlpnProtocols;
    private final OptionalMethod setHostname;
    private final OptionalMethod setUseSessionTickets;
    private final Class sslParametersClass;

    final class AndroidCertificateChainCleaner extends CertificateChainCleaner {
        private final Method checkServerTrusted;
        private final Object x509TrustManagerExtensions;

        AndroidCertificateChainCleaner(Object obj, Method method) {
            this.x509TrustManagerExtensions = obj;
            this.checkServerTrusted = method;
        }

        public List clean(List list, String str) {
            try {
                X509Certificate[] x509CertificateArr = (X509Certificate[]) list.toArray(new X509Certificate[list.size()]);
                return (List) this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, new Object[]{x509CertificateArr, "RSA", str});
            } catch (InvocationTargetException e) {
                SSLPeerUnverifiedException sSLPeerUnverifiedException = new SSLPeerUnverifiedException(e.getMessage());
                sSLPeerUnverifiedException.initCause(e);
                throw sSLPeerUnverifiedException;
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            }
        }

        public boolean equals(Object obj) {
            return obj instanceof AndroidCertificateChainCleaner;
        }

        public int hashCode() {
            return 0;
        }
    }

    final class AndroidTrustRootIndex implements TrustRootIndex {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;

        AndroidTrustRootIndex(X509TrustManager x509TrustManager, Method method) {
            this.findByIssuerAndSignatureMethod = method;
            this.trustManager = x509TrustManager;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof AndroidTrustRootIndex)) {
                return false;
            }
            AndroidTrustRootIndex androidTrustRootIndex = (AndroidTrustRootIndex) obj;
            if (!this.trustManager.equals(androidTrustRootIndex.trustManager) || !this.findByIssuerAndSignatureMethod.equals(androidTrustRootIndex.findByIssuerAndSignatureMethod)) {
                z = false;
            }
            return z;
        }

        public X509Certificate findByIssuerAndSignature(X509Certificate x509Certificate) {
            try {
                TrustAnchor trustAnchor = (TrustAnchor) this.findByIssuerAndSignatureMethod.invoke(this.trustManager, new Object[]{x509Certificate});
                if (trustAnchor != null) {
                    return trustAnchor.getTrustedCert();
                }
                return null;
            } catch (IllegalAccessException e) {
                throw Util.assertionError("unable to get issues and signature", e);
            } catch (InvocationTargetException unused) {
                return null;
            }
        }

        public int hashCode() {
            return this.trustManager.hashCode() + (this.findByIssuerAndSignatureMethod.hashCode() * 31);
        }
    }

    final class CloseGuard {
        private final Method getMethod;
        private final Method openMethod;
        private final Method warnIfOpenMethod;

        CloseGuard(Method method, Method method2, Method method3) {
            this.getMethod = method;
            this.openMethod = method2;
            this.warnIfOpenMethod = method3;
        }

        static CloseGuard get() {
            Method method;
            Method method2;
            Method method3 = null;
            try {
                Class cls = Class.forName("dalvik.system.CloseGuard");
                Method method4 = cls.getMethod("get", new Class[0]);
                method = cls.getMethod(BaseEvent.VALUE_OPEN, new Class[]{String.class});
                method3 = cls.getMethod("warnIfOpen", new Class[0]);
                method2 = method3;
                method3 = method4;
            } catch (Exception unused) {
                method2 = method3;
                method = method2;
            }
            return new CloseGuard(method3, method, method2);
        }

        /* access modifiers changed from: 0000 */
        public Object createAndOpen(String str) {
            Method method = this.getMethod;
            if (method != null) {
                try {
                    Object invoke = method.invoke(null, new Object[0]);
                    this.openMethod.invoke(invoke, new Object[]{str});
                    return invoke;
                } catch (Exception unused) {
                }
            }
            return null;
        }

        /* access modifiers changed from: 0000 */
        public boolean warnIfOpen(Object obj) {
            if (obj == null) {
                return false;
            }
            try {
                this.warnIfOpenMethod.invoke(obj, new Object[0]);
                return true;
            } catch (Exception unused) {
                return false;
            }
        }
    }

    AndroidPlatform(Class cls, OptionalMethod optionalMethod, OptionalMethod optionalMethod2, OptionalMethod optionalMethod3, OptionalMethod optionalMethod4) {
        this.sslParametersClass = cls;
        this.setUseSessionTickets = optionalMethod;
        this.setHostname = optionalMethod2;
        this.getAlpnSelectedProtocol = optionalMethod3;
        this.setAlpnProtocols = optionalMethod4;
    }

    /* JADX WARNING: type inference failed for: r3v1, types: [okhttp3.internal.platform.Platform] */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean api23IsCleartextTrafficPermitted(String str, Class cls, Object obj) {
        try {
            this = this;
            this = ((Boolean) cls.getMethod("isCleartextTrafficPermitted", new Class[0]).invoke(obj, new Object[0])).booleanValue();
            r3 = this;
            return this;
        } catch (NoSuchMethodException unused) {
            return super.isCleartextTrafficPermitted(str);
        }
    }

    private boolean api24IsCleartextTrafficPermitted(String str, Class cls, Object obj) {
        try {
            this = this;
            this = ((Boolean) cls.getMethod("isCleartextTrafficPermitted", new Class[]{String.class}).invoke(obj, new Object[]{str})).booleanValue();
            r5 = this;
            return this;
        } catch (NoSuchMethodException unused) {
            return r5.api23IsCleartextTrafficPermitted(str, cls, obj);
        }
    }

    public static Platform buildIfSupported() {
        Class cls;
        OptionalMethod optionalMethod;
        OptionalMethod optionalMethod2;
        try {
            cls = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
        } catch (ClassNotFoundException unused) {
            try {
                cls = Class.forName("org.apache.harmony.xnet.provider.jsse.SSLParametersImpl");
            } catch (ClassNotFoundException unused2) {
                return null;
            }
        }
        Class cls2 = cls;
        OptionalMethod optionalMethod3 = new OptionalMethod(null, "setUseSessionTickets", Boolean.TYPE);
        OptionalMethod optionalMethod4 = new OptionalMethod(null, "setHostname", String.class);
        if (supportsAlpn()) {
            OptionalMethod optionalMethod5 = new OptionalMethod(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
            optionalMethod = new OptionalMethod(null, "setAlpnProtocols", byte[].class);
            optionalMethod2 = optionalMethod5;
        } else {
            optionalMethod2 = null;
            optionalMethod = null;
        }
        AndroidPlatform androidPlatform = new AndroidPlatform(cls2, optionalMethod3, optionalMethod4, optionalMethod2, optionalMethod);
        return androidPlatform;
    }

    private static boolean supportsAlpn() {
        if (Security.getProvider("GMSCore_OpenSSL") != null) {
            return true;
        }
        try {
            Class.forName("android.net.Network");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager x509TrustManager) {
        try {
            Class cls = Class.forName("android.net.http.X509TrustManagerExtensions");
            return new AndroidCertificateChainCleaner(cls.getConstructor(new Class[]{X509TrustManager.class}).newInstance(new Object[]{x509TrustManager}), cls.getMethod("checkServerTrusted", new Class[]{X509Certificate[].class, String.class, String.class}));
        } catch (Exception unused) {
            return super.buildCertificateChainCleaner(x509TrustManager);
        }
    }

    public TrustRootIndex buildTrustRootIndex(X509TrustManager x509TrustManager) {
        try {
            Method declaredMethod = x509TrustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", new Class[]{X509Certificate.class});
            declaredMethod.setAccessible(true);
            return new AndroidTrustRootIndex(x509TrustManager, declaredMethod);
        } catch (NoSuchMethodException unused) {
            return super.buildTrustRootIndex(x509TrustManager);
        }
    }

    public void configureTlsExtensions(SSLSocket sSLSocket, String str, List list) {
        if (str != null) {
            this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sSLSocket, Boolean.valueOf(true));
            this.setHostname.invokeOptionalWithoutCheckedException(sSLSocket, str);
        }
        OptionalMethod optionalMethod = this.setAlpnProtocols;
        if (optionalMethod != null && optionalMethod.isSupported(sSLSocket)) {
            this.setAlpnProtocols.invokeWithoutCheckedException(sSLSocket, Platform.concatLengthPrefixed(list));
        }
    }

    public void connectSocket(Socket socket, InetSocketAddress inetSocketAddress, int i) {
        try {
            socket.connect(inetSocketAddress, i);
        } catch (AssertionError e) {
            if (Util.isAndroidGetsocknameError(e)) {
                throw new IOException(e);
            }
            throw e;
        } catch (SecurityException e2) {
            IOException iOException = new IOException("Exception in connect");
            iOException.initCause(e2);
            throw iOException;
        }
    }

    public String getSelectedProtocol(SSLSocket sSLSocket) {
        OptionalMethod optionalMethod = this.getAlpnSelectedProtocol;
        String str = null;
        if (optionalMethod == null || !optionalMethod.isSupported(sSLSocket)) {
            return null;
        }
        byte[] bArr = (byte[]) this.getAlpnSelectedProtocol.invokeWithoutCheckedException(sSLSocket, new Object[0]);
        if (bArr != null) {
            str = new String(bArr, Util.UTF_8);
        }
        return str;
    }

    public Object getStackTraceForCloseable(String str) {
        return this.closeGuard.createAndOpen(str);
    }

    /* JADX WARNING: type inference failed for: r4v1, types: [okhttp3.internal.platform.Platform] */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r4v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isCleartextTrafficPermitted(String str) {
        try {
            this = this;
            Class cls = Class.forName("android.security.NetworkSecurityPolicy");
            this = api24IsCleartextTrafficPermitted(str, cls, cls.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]));
            r4 = this;
            return this;
        } catch (ClassNotFoundException | NoSuchMethodException unused) {
            return super.isCleartextTrafficPermitted(str);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw Util.assertionError("unable to determine cleartext support", e);
        }
    }

    public void log(int i, String str, Throwable th) {
        int min;
        int i2 = 5;
        if (i != 5) {
            i2 = 3;
        }
        if (th != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(10);
            sb.append(Log.getStackTraceString(th));
            str = sb.toString();
        }
        int i3 = 0;
        int length = str.length();
        while (i3 < length) {
            int indexOf = str.indexOf(10, i3);
            if (indexOf == -1) {
                indexOf = length;
            }
            while (true) {
                min = Math.min(indexOf, i3 + MAX_LOG_LENGTH);
                Log.println(i2, "OkHttp", str.substring(i3, min));
                if (min >= indexOf) {
                    break;
                }
                i3 = min;
            }
            i3 = min + 1;
        }
    }

    public void logCloseableLeak(String str, Object obj) {
        if (!this.closeGuard.warnIfOpen(obj)) {
            log(5, str, null);
        }
    }

    public X509TrustManager trustManager(SSLSocketFactory sSLSocketFactory) {
        String str = "sslParameters";
        Object readFieldOrNull = Platform.readFieldOrNull(sSLSocketFactory, this.sslParametersClass, str);
        if (readFieldOrNull == null) {
            try {
                readFieldOrNull = Platform.readFieldOrNull(sSLSocketFactory, Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, sSLSocketFactory.getClass().getClassLoader()), str);
            } catch (ClassNotFoundException unused) {
                return super.trustManager(sSLSocketFactory);
            }
        }
        X509TrustManager x509TrustManager = (X509TrustManager) Platform.readFieldOrNull(readFieldOrNull, X509TrustManager.class, "x509TrustManager");
        return x509TrustManager != null ? x509TrustManager : (X509TrustManager) Platform.readFieldOrNull(readFieldOrNull, X509TrustManager.class, "trustManager");
    }
}

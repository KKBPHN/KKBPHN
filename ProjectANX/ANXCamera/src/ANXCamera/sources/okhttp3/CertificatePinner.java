package okhttp3;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okio.ByteString;

public final class CertificatePinner {
    public static final CertificatePinner DEFAULT = new Builder().build();
    @Nullable
    private final CertificateChainCleaner certificateChainCleaner;
    private final Set pins;

    public final class Builder {
        private final List pins = new ArrayList();

        public Builder add(String str, String... strArr) {
            if (str != null) {
                for (String pin : strArr) {
                    this.pins.add(new Pin(str, pin));
                }
                return this;
            }
            throw new NullPointerException("pattern == null");
        }

        public CertificatePinner build() {
            return new CertificatePinner(new LinkedHashSet(this.pins), null);
        }
    }

    final class Pin {
        private static final String WILDCARD = "*.";
        final String canonicalHostname;
        final ByteString hash;
        final String hashAlgorithm;
        final String pattern;

        Pin(String str, String str2) {
            StringBuilder sb;
            int i;
            this.pattern = str;
            String str3 = "http://";
            if (str.startsWith(WILDCARD)) {
                sb = new StringBuilder();
                sb.append(str3);
                str = str.substring(2);
            } else {
                sb = new StringBuilder();
                sb.append(str3);
            }
            sb.append(str);
            this.canonicalHostname = HttpUrl.parse(sb.toString()).host();
            String str4 = "sha1/";
            if (str2.startsWith(str4)) {
                this.hashAlgorithm = str4;
                i = 5;
            } else {
                String str5 = "sha256/";
                if (str2.startsWith(str5)) {
                    this.hashAlgorithm = str5;
                    i = 7;
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("pins must start with 'sha256/' or 'sha1/': ");
                    sb2.append(str2);
                    throw new IllegalArgumentException(sb2.toString());
                }
            }
            this.hash = ByteString.decodeBase64(str2.substring(i));
            if (this.hash == null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("pins must be base64: ");
                sb3.append(str2);
                throw new IllegalArgumentException(sb3.toString());
            }
        }

        public boolean equals(Object obj) {
            if (obj instanceof Pin) {
                Pin pin = (Pin) obj;
                if (this.pattern.equals(pin.pattern) && this.hashAlgorithm.equals(pin.hashAlgorithm) && this.hash.equals(pin.hash)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return ((((527 + this.pattern.hashCode()) * 31) + this.hashAlgorithm.hashCode()) * 31) + this.hash.hashCode();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x002e, code lost:
            if (r11.regionMatches(false, r6, r7, 0, r7.length()) != false) goto L_0x0032;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean matches(String str) {
            if (!this.pattern.startsWith(WILDCARD)) {
                return str.equals(this.canonicalHostname);
            }
            int indexOf = str.indexOf(46);
            boolean z = true;
            if ((str.length() - indexOf) - 1 == this.canonicalHostname.length()) {
                int i = indexOf + 1;
                String str2 = this.canonicalHostname;
            }
            z = false;
            return z;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.hashAlgorithm);
            sb.append(this.hash.base64());
            return sb.toString();
        }
    }

    CertificatePinner(Set set, @Nullable CertificateChainCleaner certificateChainCleaner2) {
        this.pins = set;
        this.certificateChainCleaner = certificateChainCleaner2;
    }

    public static String pin(Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            StringBuilder sb = new StringBuilder();
            sb.append("sha256/");
            sb.append(sha256((X509Certificate) certificate).base64());
            return sb.toString();
        }
        throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
    }

    static ByteString sha1(X509Certificate x509Certificate) {
        return ByteString.of(x509Certificate.getPublicKey().getEncoded()).sha1();
    }

    static ByteString sha256(X509Certificate x509Certificate) {
        return ByteString.of(x509Certificate.getPublicKey().getEncoded()).sha256();
    }

    public void check(String str, List list) {
        String str2;
        List findMatchingPins = findMatchingPins(str);
        if (!findMatchingPins.isEmpty()) {
            CertificateChainCleaner certificateChainCleaner2 = this.certificateChainCleaner;
            if (certificateChainCleaner2 != null) {
                list = certificateChainCleaner2.clean(list, str);
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                X509Certificate x509Certificate = (X509Certificate) list.get(i);
                int size2 = findMatchingPins.size();
                ByteString byteString = null;
                Object obj = null;
                for (int i2 = 0; i2 < size2; i2++) {
                    Pin pin = (Pin) findMatchingPins.get(i2);
                    if (pin.hashAlgorithm.equals("sha256/")) {
                        if (byteString == null) {
                            byteString = sha256(x509Certificate);
                        }
                        if (pin.hash.equals(byteString)) {
                            return;
                        }
                    } else if (pin.hashAlgorithm.equals("sha1/")) {
                        if (obj == null) {
                            obj = sha1(x509Certificate);
                        }
                        if (pin.hash.equals(obj)) {
                            return;
                        }
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("unsupported hashAlgorithm: ");
                        sb.append(pin.hashAlgorithm);
                        throw new AssertionError(sb.toString());
                    }
                }
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Certificate pinning failure!");
            sb2.append("\n  Peer certificate chain:");
            int size3 = list.size();
            int i3 = 0;
            while (true) {
                str2 = "\n    ";
                if (i3 >= size3) {
                    break;
                }
                X509Certificate x509Certificate2 = (X509Certificate) list.get(i3);
                sb2.append(str2);
                sb2.append(pin(x509Certificate2));
                sb2.append(": ");
                sb2.append(x509Certificate2.getSubjectDN().getName());
                i3++;
            }
            sb2.append("\n  Pinned certificates for ");
            sb2.append(str);
            sb2.append(":");
            int size4 = findMatchingPins.size();
            for (int i4 = 0; i4 < size4; i4++) {
                Pin pin2 = (Pin) findMatchingPins.get(i4);
                sb2.append(str2);
                sb2.append(pin2);
            }
            throw new SSLPeerUnverifiedException(sb2.toString());
        }
    }

    public void check(String str, Certificate... certificateArr) {
        check(str, Arrays.asList(certificateArr));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001c, code lost:
        if (r3.pins.equals(r4.pins) != false) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj instanceof CertificatePinner) {
            CertificatePinner certificatePinner = (CertificatePinner) obj;
            if (Util.equal(this.certificateChainCleaner, certificatePinner.certificateChainCleaner)) {
            }
        }
        z = false;
        return z;
    }

    /* access modifiers changed from: 0000 */
    public List findMatchingPins(String str) {
        List emptyList = Collections.emptyList();
        for (Pin pin : this.pins) {
            if (pin.matches(str)) {
                if (emptyList.isEmpty()) {
                    emptyList = new ArrayList();
                }
                emptyList.add(pin);
            }
        }
        return emptyList;
    }

    public int hashCode() {
        CertificateChainCleaner certificateChainCleaner2 = this.certificateChainCleaner;
        return ((certificateChainCleaner2 != null ? certificateChainCleaner2.hashCode() : 0) * 31) + this.pins.hashCode();
    }

    /* access modifiers changed from: 0000 */
    public CertificatePinner withCertificateChainCleaner(@Nullable CertificateChainCleaner certificateChainCleaner2) {
        return Util.equal(this.certificateChainCleaner, certificateChainCleaner2) ? this : new CertificatePinner(this.pins, certificateChainCleaner2);
    }
}

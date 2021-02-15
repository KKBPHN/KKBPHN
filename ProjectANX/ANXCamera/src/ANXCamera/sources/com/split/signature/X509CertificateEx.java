package com.split.signature;

import java.security.cert.X509Certificate;

final class X509CertificateEx extends X509CertificateWrapper {
    private byte[] a;

    X509CertificateEx(X509Certificate x509Certificate, byte[] bArr) {
        super(x509Certificate);
        this.a = bArr;
    }

    public byte[] getEncoded() {
        return this.a;
    }
}

package com.split.signature;

import java.math.BigInteger;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

class X509CertificateWrapper extends X509Certificate {
    private final X509Certificate a;

    X509CertificateWrapper(X509Certificate x509Certificate) {
        this.a = x509Certificate;
    }

    public void checkValidity() {
        this.a.checkValidity();
    }

    public void checkValidity(Date date) {
        this.a.checkValidity(date);
    }

    public int getBasicConstraints() {
        return this.a.getBasicConstraints();
    }

    public Set getCriticalExtensionOIDs() {
        return this.a.getCriticalExtensionOIDs();
    }

    public byte[] getEncoded() {
        return this.a.getEncoded();
    }

    public byte[] getExtensionValue(String str) {
        return this.a.getExtensionValue(str);
    }

    public Principal getIssuerDN() {
        return this.a.getIssuerDN();
    }

    public boolean[] getIssuerUniqueID() {
        return this.a.getIssuerUniqueID();
    }

    public boolean[] getKeyUsage() {
        return this.a.getKeyUsage();
    }

    public Set getNonCriticalExtensionOIDs() {
        return this.a.getNonCriticalExtensionOIDs();
    }

    public Date getNotAfter() {
        return this.a.getNotAfter();
    }

    public Date getNotBefore() {
        return this.a.getNotBefore();
    }

    public PublicKey getPublicKey() {
        return this.a.getPublicKey();
    }

    public BigInteger getSerialNumber() {
        return this.a.getSerialNumber();
    }

    public String getSigAlgName() {
        return this.a.getSigAlgName();
    }

    public String getSigAlgOID() {
        return this.a.getSigAlgOID();
    }

    public byte[] getSigAlgParams() {
        return this.a.getSigAlgParams();
    }

    public byte[] getSignature() {
        return this.a.getSignature();
    }

    public Principal getSubjectDN() {
        return this.a.getSubjectDN();
    }

    public boolean[] getSubjectUniqueID() {
        return this.a.getSubjectUniqueID();
    }

    public byte[] getTBSCertificate() {
        return this.a.getTBSCertificate();
    }

    public int getVersion() {
        return this.a.getVersion();
    }

    public boolean hasUnsupportedCriticalExtension() {
        return this.a.hasUnsupportedCriticalExtension();
    }

    public String toString() {
        return this.a.toString();
    }

    public void verify(PublicKey publicKey) {
        this.a.verify(publicKey);
    }

    public void verify(PublicKey publicKey, String str) {
        this.a.verify(publicKey, str);
    }
}

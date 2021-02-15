package com.iqiyi.android.qigsaw.core.splitinstall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.Signature;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.split.signature.O00000o;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class SignatureValidator {
    private static final String TAG = "SignatureValidator";

    private SignatureValidator() {
    }

    private static boolean a(String str, List list) {
        boolean z;
        String str2 = " is not signed.";
        String str3 = "Downloaded split ";
        String str4 = TAG;
        try {
            X509Certificate[][] a = O00000o.a(str);
            if (a == null || a.length == 0 || a[0].length == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(str);
                sb.append(str2);
                SplitLog.e(str4, sb.toString(), new Object[0]);
                return false;
            } else if (list.isEmpty()) {
                SplitLog.e(str4, "No certificates found for app.", new Object[0]);
                return false;
            } else {
                Iterator it = list.iterator();
                do {
                    z = true;
                    if (it.hasNext()) {
                        X509Certificate x509Certificate = (X509Certificate) it.next();
                        int length = a.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                z = false;
                                continue;
                                break;
                            } else if (a[i][0].equals(x509Certificate)) {
                                continue;
                                break;
                            } else {
                                i++;
                            }
                        }
                    } else {
                        return true;
                    }
                } while (z);
                SplitLog.i(str4, "There's an app certificate that doesn't sign the split.", new Object[0]);
                return false;
            }
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str3);
            sb2.append(str);
            sb2.append(str2);
            SplitLog.e(str4, sb2.toString(), (Throwable) e);
            return false;
        }
    }

    private static X509Certificate decodeCertificate(Signature signature) {
        try {
            return (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(signature.toByteArray()));
        } catch (CertificateException e) {
            SplitLog.e(TAG, "Cannot decode certificate.", (Throwable) e);
            return null;
        }
    }

    @SuppressLint({"PackageManagerGetSignatures"})
    private static Signature[] getAppSignature(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures;
        } catch (Throwable unused) {
            return null;
        }
    }

    static boolean validateSplit(Context context, File file) {
        ArrayList arrayList;
        Signature[] appSignature = getAppSignature(context);
        if (appSignature == null) {
            arrayList = null;
        } else {
            ArrayList arrayList2 = new ArrayList();
            for (Signature decodeCertificate : appSignature) {
                X509Certificate decodeCertificate2 = decodeCertificate(decodeCertificate);
                if (decodeCertificate2 != null) {
                    arrayList2.add(decodeCertificate2);
                }
            }
            arrayList = arrayList2;
        }
        if (arrayList == null || arrayList.isEmpty()) {
            return false;
        }
        return a(file.getAbsolutePath(), arrayList);
    }
}

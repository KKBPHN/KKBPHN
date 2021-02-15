package defpackage;

import java.io.IOException;

/* renamed from: cm reason: default package */
public final class cm extends IOException {
    private static final long serialVersionUID = -6947486886997889499L;

    cm() {
        super("CodedOutputStream was writing to a flat byte array and ran out of space.");
    }

    public cm(String str, Throwable th) {
        String valueOf = String.valueOf(str);
        String str2 = "CodedOutputStream was writing to a flat byte array and ran out of space.: ";
        super(valueOf.length() == 0 ? new String(str2) : str2.concat(valueOf), th);
    }

    public cm(Throwable th) {
        super("CodedOutputStream was writing to a flat byte array and ran out of space.", th);
    }
}

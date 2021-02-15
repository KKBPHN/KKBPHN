package defpackage;

import java.io.IOException;

/* renamed from: dl reason: default package */
public class dl extends IOException {
    private static final long serialVersionUID = -1616151763072450476L;

    public dl(String str) {
        super(str);
    }

    static dl a() {
        return new dl("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either that the input has been truncated or that an embedded message misreported its own length.");
    }

    static dl b() {
        return new dl("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    static dl c() {
        return new dl("Protocol message contained an invalid tag (zero).");
    }

    static dk d() {
        return new dk("Protocol message tag had invalid wire type.");
    }

    static dl e() {
        return new dl("Failed to parse the message.");
    }

    static dl f() {
        return new dl("Protocol message had invalid UTF-8.");
    }
}

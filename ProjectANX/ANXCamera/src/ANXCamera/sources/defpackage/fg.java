package defpackage;

/* renamed from: fg reason: default package */
public final class fg extends RuntimeException {
    private static final long serialVersionUID = -7466929953374883507L;

    public fg() {
        super("Message was missing required fields.  (Lite runtime could not determine which fields were missing).");
    }
}

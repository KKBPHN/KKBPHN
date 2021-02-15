package defpackage;

/* renamed from: bt reason: default package */
public abstract class bt implements eg {
    public abstract bt O000000o(bu buVar);

    public final /* bridge */ /* synthetic */ eg O000000o(eh ehVar) {
        if (i().getClass().isInstance(ehVar)) {
            return O000000o((bu) ehVar);
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }

    /* renamed from: a */
    public abstract bt clone();
}

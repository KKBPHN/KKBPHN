.class final Lco;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lgb;


# instance fields
.field public final a:Lcn;


# direct methods
.method public constructor <init>(Lcn;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const-string v0, "output"

    invoke-static {p1, v0}, Ldj;->a(Ljava/lang/Object;Ljava/lang/String;)V

    iput-object p1, p0, Lco;->a:Lcn;

    iget-object p1, p0, Lco;->a:Lcn;

    iput-object p0, p1, Lcn;->b:Lco;

    return-void
.end method


# virtual methods
.method public final O000000o(ID)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2, p3}, Lcn;->O000000o(ID)V

    return-void
.end method

.method public final O000000o(IF)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O000000o(IF)V

    return-void
.end method

.method public final O000000o(II)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O0000O0o(II)V

    return-void
.end method

.method public final O000000o(IJ)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2, p3}, Lcn;->O00000o0(IJ)V

    return-void
.end method

.method public final O000000o(ILck;)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O00000Oo(ILck;)V

    return-void
.end method

.method public final O000000o(ILjava/lang/Object;)V
    .locals 1

    instance-of v0, p2, Lck;

    iget-object p0, p0, Lco;->a:Lcn;

    if-eqz v0, :cond_0

    check-cast p2, Lck;

    invoke-virtual {p0, p1, p2}, Lcn;->O00000o0(ILck;)V

    return-void

    :cond_0
    check-cast p2, Leh;

    invoke-virtual {p0, p1, p2}, Lcn;->O000000o(ILeh;)V

    return-void
.end method

.method public final O000000o(ILjava/lang/Object;Les;)V
    .locals 1

    iget-object p0, p0, Lco;->a:Lcn;

    check-cast p2, Leh;

    const/4 v0, 0x3

    invoke-virtual {p0, p1, v0}, Lcn;->O00000o(II)V

    iget-object v0, p0, Lcn;->b:Lco;

    invoke-interface {p3, p2, v0}, Les;->O000000o(Ljava/lang/Object;Lgb;)V

    const/4 p2, 0x4

    invoke-virtual {p0, p1, p2}, Lcn;->O00000o(II)V

    return-void
.end method

.method public final O000000o(IZ)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O000000o(IZ)V

    return-void
.end method

.method public final O00000Oo(II)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O0000Oo0(II)V

    return-void
.end method

.method public final O00000Oo(IJ)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2, p3}, Lcn;->O00000oO(IJ)V

    return-void
.end method

.method public final O00000Oo(ILjava/lang/Object;Les;)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    check-cast p2, Leh;

    invoke-virtual {p0, p1, p2, p3}, Lcn;->O00000Oo(ILeh;Les;)V

    return-void
.end method

.method public final O00000o(II)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->a(II)V

    return-void
.end method

.method public final O00000o(IJ)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2, p3}, Lcn;->O00000oO(IJ)V

    return-void
.end method

.method public final O00000o0(II)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O0000OOo(II)V

    return-void
.end method

.method public final O00000o0(IJ)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2, p3}, Lcn;->a(IJ)V

    return-void
.end method

.method public final O00000oO(II)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O0000O0o(II)V

    return-void
.end method

.method public final a(I)V
    .locals 1

    iget-object p0, p0, Lco;->a:Lcn;

    const/4 v0, 0x3

    invoke-virtual {p0, p1, v0}, Lcn;->O00000o(II)V

    return-void
.end method

.method public final a(II)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O0000Oo0(II)V

    return-void
.end method

.method public final a(IJ)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2, p3}, Lcn;->O00000o0(IJ)V

    return-void
.end method

.method public final a(ILjava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lco;->a:Lcn;

    invoke-virtual {p0, p1, p2}, Lcn;->O00000Oo(ILjava/lang/String;)V

    return-void
.end method

.method public final b(I)V
    .locals 1

    iget-object p0, p0, Lco;->a:Lcn;

    const/4 v0, 0x4

    invoke-virtual {p0, p1, v0}, Lcn;->O00000o(II)V

    return-void
.end method

.class public Lcom/airbnb/lottie/O00000oO/O0000OOo;
.super Lcom/airbnb/lottie/O00000oO/O0000Oo;
.source ""


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lcom/airbnb/lottie/O00000oO/O0000Oo;-><init>()V

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O00000oO/O00000Oo;)Ljava/lang/Integer;
    .locals 3

    invoke-virtual {p1}, Lcom/airbnb/lottie/O00000oO/O00000Oo;->getStartValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    invoke-virtual {p1}, Lcom/airbnb/lottie/O00000oO/O00000Oo;->getEndValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Integer;->intValue()I

    move-result v1

    invoke-virtual {p1}, Lcom/airbnb/lottie/O00000oO/O00000Oo;->O00o0OOo()F

    move-result v2

    invoke-static {v0, v1, v2}, Lcom/airbnb/lottie/O00000o/O0000O0o;->O000000o(IIF)I

    move-result v0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000oO/O0000OOo;->O00000o0(Lcom/airbnb/lottie/O00000oO/O00000Oo;)Ljava/lang/Integer;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    add-int/2addr v0, p0

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic O000000o(Lcom/airbnb/lottie/O00000oO/O00000Oo;)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000oO/O0000OOo;->O000000o(Lcom/airbnb/lottie/O00000oO/O00000Oo;)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

.method public O00000o0(Lcom/airbnb/lottie/O00000oO/O00000Oo;)Ljava/lang/Integer;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->value:Ljava/lang/Object;

    if-eqz p0, :cond_0

    check-cast p0, Ljava/lang/Integer;

    return-object p0

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "You must provide a static value in the constructor , call setValue, or override getValue."

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

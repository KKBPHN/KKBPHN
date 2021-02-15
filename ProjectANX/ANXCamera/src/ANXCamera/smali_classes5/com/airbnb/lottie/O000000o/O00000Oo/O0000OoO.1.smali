.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000OoO;
.super Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Ooo;
.source ""


# direct methods
.method public constructor <init>(Ljava/util/List;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Ooo;-><init>(Ljava/util/List;)V

    return-void
.end method


# virtual methods
.method O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Integer;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000OoO;->O00000Oo(Lcom/airbnb/lottie/O00000oO/O000000o;F)I

    move-result p0

    invoke-static {p0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

.method bridge synthetic O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000OoO;->O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

.method O00000Oo(Lcom/airbnb/lottie/O00000oO/O000000o;F)I
    .locals 9

    iget-object v0, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    if-eqz v0, :cond_1

    iget-object v0, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    if-eqz v0, :cond_1

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;

    if-eqz v1, :cond_0

    iget v2, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0O:F

    iget-object v0, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0o:Ljava/lang/Float;

    invoke-virtual {v0}, Ljava/lang/Float;->floatValue()F

    move-result v3

    iget-object v4, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    iget-object v5, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOoO()F

    move-result v7

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v8

    move v6, p2

    invoke-virtual/range {v1 .. v8}, Lcom/airbnb/lottie/O00000oO/O0000Oo;->O00000Oo(FFLjava/lang/Object;Ljava/lang/Object;FFF)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Integer;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    return p0

    :cond_0
    invoke-virtual {p1}, Lcom/airbnb/lottie/O00000oO/O000000o;->O00o0OO()I

    move-result p0

    invoke-virtual {p1}, Lcom/airbnb/lottie/O00000oO/O000000o;->O00o0O()I

    move-result p1

    invoke-static {p0, p1, p2}, Lcom/airbnb/lottie/O00000o/O0000O0o;->O000000o(IIF)I

    move-result p0

    return p0

    :cond_1
    new-instance p0, Ljava/lang/IllegalStateException;

    const-string p1, "Missing values for keyframe."

    invoke-direct {p0, p1}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public getIntValue()I
    .locals 2

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o()Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object v0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOo()F

    move-result v1

    invoke-virtual {p0, v0, v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000OoO;->O00000Oo(Lcom/airbnb/lottie/O00000oO/O000000o;F)I

    move-result p0

    return p0
.end method

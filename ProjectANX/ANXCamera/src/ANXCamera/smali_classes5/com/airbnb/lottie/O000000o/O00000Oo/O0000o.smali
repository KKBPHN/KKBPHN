.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o;
.super Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Ooo;
.source ""


# instance fields
.field private final O00o00oo:Lcom/airbnb/lottie/O00000oO/O0000OoO;


# direct methods
.method public constructor <init>(Ljava/util/List;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Ooo;-><init>(Ljava/util/List;)V

    new-instance p1, Lcom/airbnb/lottie/O00000oO/O0000OoO;

    invoke-direct {p1}, Lcom/airbnb/lottie/O00000oO/O0000OoO;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o;->O00o00oo:Lcom/airbnb/lottie/O00000oO/O0000OoO;

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Lcom/airbnb/lottie/O00000oO/O0000OoO;
    .locals 10

    iget-object v0, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    if-eqz v0, :cond_1

    iget-object v1, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    if-eqz v1, :cond_1

    check-cast v0, Lcom/airbnb/lottie/O00000oO/O0000OoO;

    check-cast v1, Lcom/airbnb/lottie/O00000oO/O0000OoO;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;

    if-eqz v2, :cond_0

    iget v3, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0O:F

    iget-object p1, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0o:Ljava/lang/Float;

    invoke-virtual {p1}, Ljava/lang/Float;->floatValue()F

    move-result v4

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOoO()F

    move-result v8

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v9

    move-object v5, v0

    move-object v6, v1

    move v7, p2

    invoke-virtual/range {v2 .. v9}, Lcom/airbnb/lottie/O00000oO/O0000Oo;->O00000Oo(FFLjava/lang/Object;Ljava/lang/Object;FFF)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lcom/airbnb/lottie/O00000oO/O0000OoO;

    if-eqz p1, :cond_0

    return-object p1

    :cond_0
    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o;->O00o00oo:Lcom/airbnb/lottie/O00000oO/O0000OoO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000oO/O0000OoO;->getScaleX()F

    move-result v2

    invoke-virtual {v1}, Lcom/airbnb/lottie/O00000oO/O0000OoO;->getScaleX()F

    move-result v3

    invoke-static {v2, v3, p2}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result v2

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000oO/O0000OoO;->getScaleY()F

    move-result v0

    invoke-virtual {v1}, Lcom/airbnb/lottie/O00000oO/O0000OoO;->getScaleY()F

    move-result v1

    invoke-static {v0, v1, p2}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result p2

    invoke-virtual {p1, v2, p2}, Lcom/airbnb/lottie/O00000oO/O0000OoO;->set(FF)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o;->O00o00oo:Lcom/airbnb/lottie/O00000oO/O0000OoO;

    return-object p0

    :cond_1
    new-instance p0, Ljava/lang/IllegalStateException;

    const-string p1, "Missing values for keyframe."

    invoke-direct {p0, p1}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public bridge synthetic O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o;->O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Lcom/airbnb/lottie/O00000oO/O0000OoO;

    move-result-object p0

    return-object p0
.end method

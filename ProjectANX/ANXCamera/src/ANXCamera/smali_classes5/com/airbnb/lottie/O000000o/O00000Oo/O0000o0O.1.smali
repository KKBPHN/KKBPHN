.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;
.super Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Ooo;
.source ""


# instance fields
.field private O00o00o:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;

.field private final O00o00o0:Landroid/graphics/PointF;

.field private O00o00oO:Landroid/graphics/PathMeasure;

.field private final pos:[F


# direct methods
.method public constructor <init>(Ljava/util/List;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Ooo;-><init>(Ljava/util/List;)V

    new-instance p1, Landroid/graphics/PointF;

    invoke-direct {p1}, Landroid/graphics/PointF;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O00o00o0:Landroid/graphics/PointF;

    const/4 p1, 0x2

    new-array p1, p1, [F

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->pos:[F

    new-instance p1, Landroid/graphics/PathMeasure;

    invoke-direct {p1}, Landroid/graphics/PathMeasure;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O00o00oO:Landroid/graphics/PathMeasure;

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Landroid/graphics/PointF;
    .locals 10

    move-object v0, p1

    check-cast v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;->getPath()Landroid/graphics/Path;

    move-result-object v1

    if-nez v1, :cond_0

    iget-object p0, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    check-cast p0, Landroid/graphics/PointF;

    return-object p0

    :cond_0
    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;

    if-eqz v2, :cond_1

    iget v3, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0O:F

    iget-object p1, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0o:Ljava/lang/Float;

    invoke-virtual {p1}, Ljava/lang/Float;->floatValue()F

    move-result v4

    iget-object v5, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    iget-object v6, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOoO()F

    move-result v7

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v9

    move v8, p2

    invoke-virtual/range {v2 .. v9}, Lcom/airbnb/lottie/O00000oO/O0000Oo;->O00000Oo(FFLjava/lang/Object;Ljava/lang/Object;FFF)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/graphics/PointF;

    if-eqz p1, :cond_1

    return-object p1

    :cond_1
    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O00o00o:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;

    const/4 v2, 0x0

    if-eq p1, v0, :cond_2

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O00o00oO:Landroid/graphics/PathMeasure;

    invoke-virtual {p1, v1, v2}, Landroid/graphics/PathMeasure;->setPath(Landroid/graphics/Path;Z)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O00o00o:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;

    :cond_2
    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O00o00oO:Landroid/graphics/PathMeasure;

    invoke-virtual {p1}, Landroid/graphics/PathMeasure;->getLength()F

    move-result v0

    mul-float/2addr p2, v0

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->pos:[F

    const/4 v1, 0x0

    invoke-virtual {p1, p2, v0, v1}, Landroid/graphics/PathMeasure;->getPosTan(F[F[F)Z

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O00o00o0:Landroid/graphics/PointF;

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->pos:[F

    aget v0, p2, v2

    const/4 v1, 0x1

    aget p2, p2, v1

    invoke-virtual {p1, v0, p2}, Landroid/graphics/PointF;->set(FF)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O00o00o0:Landroid/graphics/PointF;

    return-object p0
.end method

.method public bridge synthetic O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;->O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Landroid/graphics/PointF;

    move-result-object p0

    return-object p0
.end method

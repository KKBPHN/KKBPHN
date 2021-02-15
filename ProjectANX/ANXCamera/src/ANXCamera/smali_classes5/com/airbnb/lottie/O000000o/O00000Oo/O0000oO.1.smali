.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;
.super Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
.source ""


# instance fields
.field private final O00o00o0:Landroid/graphics/PointF;

.field private final O00o0O0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final O00oo000:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 1

    invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;-><init>(Ljava/util/List;)V

    new-instance v0, Landroid/graphics/PointF;

    invoke-direct {v0}, Landroid/graphics/PointF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00o00o0:Landroid/graphics/PointF;

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00o0O0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iput-object p2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00oo000:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result p1

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->setProgress(F)V

    return-void
.end method


# virtual methods
.method O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Landroid/graphics/PointF;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00o00o0:Landroid/graphics/PointF;

    return-object p0
.end method

.method bridge synthetic O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Landroid/graphics/PointF;

    move-result-object p0

    return-object p0
.end method

.method public getValue()Landroid/graphics/PointF;
    .locals 2

    const/4 v0, 0x0

    const/4 v1, 0x0

    invoke-virtual {p0, v0, v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Landroid/graphics/PointF;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic getValue()Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->getValue()Landroid/graphics/PointF;

    move-result-object p0

    return-object p0
.end method

.method public setProgress(F)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00o0O0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->setProgress(F)V

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00oo000:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->setProgress(F)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00o00o0:Landroid/graphics/PointF;

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00o0O0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Float;

    invoke-virtual {v0}, Ljava/lang/Float;->floatValue()F

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;->O00oo000:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Float;

    invoke-virtual {v1}, Ljava/lang/Float;->floatValue()F

    move-result v1

    invoke-virtual {p1, v0, v1}, Landroid/graphics/PointF;->set(FF)V

    const/4 p1, 0x0

    :goto_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->listeners:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-ge p1, v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->listeners:Ljava/util/List;

    invoke-interface {v0, p1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;

    invoke-interface {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;->O00000oO()V

    add-int/lit8 p1, p1, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

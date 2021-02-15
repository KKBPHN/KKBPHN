.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;
.super Lcom/airbnb/lottie/O00000oO/O000000o;
.source ""


# instance fields
.field private final O0OOo00:Lcom/airbnb/lottie/O00000oO/O000000o;

.field private path:Landroid/graphics/Path;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000oO/O000000o;)V
    .locals 7

    iget-object v2, p2, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    iget-object v3, p2, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    iget-object v4, p2, Lcom/airbnb/lottie/O00000oO/O000000o;->interpolator:Landroid/view/animation/Interpolator;

    iget v5, p2, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0O:F

    iget-object v6, p2, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0o:Ljava/lang/Float;

    move-object v0, p0

    move-object v1, p1

    invoke-direct/range {v0 .. v6}, Lcom/airbnb/lottie/O00000oO/O000000o;-><init>(Lcom/airbnb/lottie/O0000o0O;Ljava/lang/Object;Ljava/lang/Object;Landroid/view/animation/Interpolator;FLjava/lang/Float;)V

    iput-object p2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;->O0OOo00:Lcom/airbnb/lottie/O00000oO/O000000o;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;->O00o0OOO()V

    return-void
.end method


# virtual methods
.method public O00o0OOO()V
    .locals 4

    iget-object v0, p0, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    if-eqz v0, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    if-eqz v1, :cond_0

    check-cast v1, Landroid/graphics/PointF;

    move-object v2, v0

    check-cast v2, Landroid/graphics/PointF;

    iget v2, v2, Landroid/graphics/PointF;->x:F

    check-cast v0, Landroid/graphics/PointF;

    iget v0, v0, Landroid/graphics/PointF;->y:F

    invoke-virtual {v1, v2, v0}, Landroid/graphics/PointF;->equals(FF)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    if-eqz v1, :cond_1

    if-nez v0, :cond_1

    iget-object v0, p0, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    check-cast v0, Landroid/graphics/PointF;

    check-cast v1, Landroid/graphics/PointF;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;->O0OOo00:Lcom/airbnb/lottie/O00000oO/O000000o;

    iget-object v3, v2, Lcom/airbnb/lottie/O00000oO/O000000o;->O0OOOo0:Landroid/graphics/PointF;

    iget-object v2, v2, Lcom/airbnb/lottie/O00000oO/O000000o;->O0OOOo:Landroid/graphics/PointF;

    invoke-static {v0, v1, v3, v2}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/PointF;Landroid/graphics/PointF;Landroid/graphics/PointF;Landroid/graphics/PointF;)Landroid/graphics/Path;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;->path:Landroid/graphics/Path;

    :cond_1
    return-void
.end method

.method getPath()Landroid/graphics/Path;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;->path:Landroid/graphics/Path;

    return-object p0
.end method

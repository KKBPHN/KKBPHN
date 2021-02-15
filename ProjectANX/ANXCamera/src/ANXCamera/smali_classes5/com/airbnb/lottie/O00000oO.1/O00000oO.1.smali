.class public Lcom/airbnb/lottie/O00000oO/O00000oO;
.super Lcom/airbnb/lottie/O00000oO/O00000oo;
.source ""


# instance fields
.field private final O00o00o0:Landroid/graphics/PointF;


# direct methods
.method public constructor <init>(Landroid/graphics/PointF;Landroid/graphics/PointF;)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lcom/airbnb/lottie/O00000oO/O00000oo;-><init>(Ljava/lang/Object;Ljava/lang/Object;)V

    new-instance p1, Landroid/graphics/PointF;

    invoke-direct {p1}, Landroid/graphics/PointF;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O00000oO/O00000oO;->O00o00o0:Landroid/graphics/PointF;

    return-void
.end method

.method public constructor <init>(Landroid/graphics/PointF;Landroid/graphics/PointF;Landroid/view/animation/Interpolator;)V
    .locals 0

    invoke-direct {p0, p1, p2, p3}, Lcom/airbnb/lottie/O00000oO/O00000oo;-><init>(Ljava/lang/Object;Ljava/lang/Object;Landroid/view/animation/Interpolator;)V

    new-instance p1, Landroid/graphics/PointF;

    invoke-direct {p1}, Landroid/graphics/PointF;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O00000oO/O00000oO;->O00o00o0:Landroid/graphics/PointF;

    return-void
.end method


# virtual methods
.method O000000o(Landroid/graphics/PointF;Landroid/graphics/PointF;F)Landroid/graphics/PointF;
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/O00000oO/O00000oO;->O00o00o0:Landroid/graphics/PointF;

    iget v1, p1, Landroid/graphics/PointF;->x:F

    iget v2, p2, Landroid/graphics/PointF;->x:F

    invoke-static {v1, v2, p3}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result v1

    iget p1, p1, Landroid/graphics/PointF;->y:F

    iget p2, p2, Landroid/graphics/PointF;->y:F

    invoke-static {p1, p2, p3}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result p1

    invoke-virtual {v0, v1, p1}, Landroid/graphics/PointF;->set(FF)V

    iget-object p0, p0, Lcom/airbnb/lottie/O00000oO/O00000oO;->O00o00o0:Landroid/graphics/PointF;

    return-object p0
.end method

.method bridge synthetic O000000o(Ljava/lang/Object;Ljava/lang/Object;F)Ljava/lang/Object;
    .locals 0

    check-cast p1, Landroid/graphics/PointF;

    check-cast p2, Landroid/graphics/PointF;

    invoke-virtual {p0, p1, p2, p3}, Lcom/airbnb/lottie/O00000oO/O00000oO;->O000000o(Landroid/graphics/PointF;Landroid/graphics/PointF;F)Landroid/graphics/PointF;

    move-result-object p0

    return-object p0
.end method

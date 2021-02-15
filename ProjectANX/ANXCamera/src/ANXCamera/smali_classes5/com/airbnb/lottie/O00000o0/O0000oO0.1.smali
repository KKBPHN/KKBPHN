.class Lcom/airbnb/lottie/O00000o0/O0000oO0;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final LINEAR_INTERPOLATOR:Landroid/view/animation/Interpolator;

.field static NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o; = null

.field private static O0O0o0:Landroidx/collection/SparseArrayCompat; = null

.field private static final O0O0o00:F = 100.0f


# direct methods
.method static constructor <clinit>()V
    .locals 9

    new-instance v0, Landroid/view/animation/LinearInterpolator;

    invoke-direct {v0}, Landroid/view/animation/LinearInterpolator;-><init>()V

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;->LINEAR_INTERPOLATOR:Landroid/view/animation/Interpolator;

    const-string v1, "t"

    const-string v2, "s"

    const-string v3, "e"

    const-string v4, "o"

    const-string v5, "i"

    const-string v6, "h"

    const-string v7, "to"

    const-string v8, "ti"

    filled-new-array/range {v1 .. v8}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method private static O000000o(Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/parser/moshi/O00000Oo;FLcom/airbnb/lottie/O00000o0/O000OO;)Lcom/airbnb/lottie/O00000oO/O000000o;
    .locals 16

    move-object/from16 v0, p1

    move/from16 v1, p2

    move-object/from16 v2, p3

    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    const/4 v3, 0x0

    const/4 v4, 0x0

    const/4 v5, 0x0

    move-object v6, v4

    move-object v7, v6

    move-object v8, v7

    move-object v9, v8

    move-object v14, v9

    move-object v15, v14

    move v12, v5

    :cond_0
    move v5, v3

    :goto_0
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v10

    if-eqz v10, :cond_1

    sget-object v10, Lcom/airbnb/lottie/O00000o0/O0000oO0;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {v0, v10}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v10

    packed-switch v10, :pswitch_data_0

    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :pswitch_0
    invoke-static/range {p1 .. p2}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;

    move-result-object v15

    goto :goto_0

    :pswitch_1
    invoke-static/range {p1 .. p2}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;

    move-result-object v14

    goto :goto_0

    :pswitch_2
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result v5

    const/4 v10, 0x1

    if-ne v5, v10, :cond_0

    move v5, v10

    goto :goto_0

    :pswitch_3
    invoke-static/range {p1 .. p2}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;

    move-result-object v7

    goto :goto_0

    :pswitch_4
    invoke-static/range {p1 .. p2}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;

    move-result-object v6

    goto :goto_0

    :pswitch_5
    invoke-interface {v2, v0, v1}, Lcom/airbnb/lottie/O00000o0/O000OO;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Object;

    move-result-object v8

    goto :goto_0

    :pswitch_6
    invoke-interface {v2, v0, v1}, Lcom/airbnb/lottie/O00000o0/O000OO;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Object;

    move-result-object v9

    goto :goto_0

    :pswitch_7
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v10

    double-to-float v12, v10

    goto :goto_0

    :cond_1
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    if-eqz v5, :cond_2

    sget-object v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;->LINEAR_INTERPOLATOR:Landroid/view/animation/Interpolator;

    move-object v11, v0

    move-object v10, v9

    goto :goto_2

    :cond_2
    if-eqz v6, :cond_6

    if-eqz v7, :cond_6

    iget v0, v6, Landroid/graphics/PointF;->x:F

    neg-float v2, v1

    invoke-static {v0, v2, v1}, Lcom/airbnb/lottie/O00000o/O0000O0o;->clamp(FFF)F

    move-result v0

    iput v0, v6, Landroid/graphics/PointF;->x:F

    iget v0, v6, Landroid/graphics/PointF;->y:F

    const/high16 v3, 0x42c80000    # 100.0f

    const/high16 v5, -0x3d380000    # -100.0f

    invoke-static {v0, v5, v3}, Lcom/airbnb/lottie/O00000o/O0000O0o;->clamp(FFF)F

    move-result v0

    iput v0, v6, Landroid/graphics/PointF;->y:F

    iget v0, v7, Landroid/graphics/PointF;->x:F

    invoke-static {v0, v2, v1}, Lcom/airbnb/lottie/O00000o/O0000O0o;->clamp(FFF)F

    move-result v0

    iput v0, v7, Landroid/graphics/PointF;->x:F

    iget v0, v7, Landroid/graphics/PointF;->y:F

    invoke-static {v0, v5, v3}, Lcom/airbnb/lottie/O00000o/O0000O0o;->clamp(FFF)F

    move-result v0

    iput v0, v7, Landroid/graphics/PointF;->y:F

    iget v0, v6, Landroid/graphics/PointF;->x:F

    iget v2, v6, Landroid/graphics/PointF;->y:F

    iget v3, v7, Landroid/graphics/PointF;->x:F

    iget v5, v7, Landroid/graphics/PointF;->y:F

    invoke-static {v0, v2, v3, v5}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(FFFF)I

    move-result v0

    invoke-static {v0}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->getInterpolator(I)Ljava/lang/ref/WeakReference;

    move-result-object v2

    if-eqz v2, :cond_3

    invoke-virtual {v2}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v3

    move-object v4, v3

    check-cast v4, Landroid/view/animation/Interpolator;

    :cond_3
    if-eqz v2, :cond_4

    if-nez v4, :cond_5

    :cond_4
    iget v2, v6, Landroid/graphics/PointF;->x:F

    div-float/2addr v2, v1

    iget v3, v6, Landroid/graphics/PointF;->y:F

    div-float/2addr v3, v1

    iget v4, v7, Landroid/graphics/PointF;->x:F

    div-float/2addr v4, v1

    iget v5, v7, Landroid/graphics/PointF;->y:F

    div-float/2addr v5, v1

    invoke-static {v2, v3, v4, v5}, Landroidx/core/view/animation/PathInterpolatorCompat;->create(FFFF)Landroid/view/animation/Interpolator;

    move-result-object v4

    :try_start_0
    new-instance v1, Ljava/lang/ref/WeakReference;

    invoke-direct {v1, v4}, Ljava/lang/ref/WeakReference;-><init>(Ljava/lang/Object;)V

    invoke-static {v0, v1}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O000000o(ILjava/lang/ref/WeakReference;)V
    :try_end_0
    .catch Ljava/lang/ArrayIndexOutOfBoundsException; {:try_start_0 .. :try_end_0} :catch_0

    :catch_0
    :cond_5
    move-object v11, v4

    goto :goto_1

    :cond_6
    sget-object v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;->LINEAR_INTERPOLATOR:Landroid/view/animation/Interpolator;

    move-object v11, v0

    :goto_1
    move-object v10, v8

    :goto_2
    new-instance v0, Lcom/airbnb/lottie/O00000oO/O000000o;

    const/4 v13, 0x0

    move-object v7, v0

    move-object/from16 v8, p0

    invoke-direct/range {v7 .. v13}, Lcom/airbnb/lottie/O00000oO/O000000o;-><init>(Lcom/airbnb/lottie/O0000o0O;Ljava/lang/Object;Ljava/lang/Object;Landroid/view/animation/Interpolator;FLjava/lang/Float;)V

    iput-object v14, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->O0OOOo0:Landroid/graphics/PointF;

    iput-object v15, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->O0OOOo:Landroid/graphics/PointF;

    return-object v0

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;FLcom/airbnb/lottie/O00000o0/O000OO;)Lcom/airbnb/lottie/O00000oO/O000000o;
    .locals 0

    invoke-interface {p2, p0, p1}, Lcom/airbnb/lottie/O00000o0/O000OO;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Object;

    move-result-object p0

    new-instance p1, Lcom/airbnb/lottie/O00000oO/O000000o;

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/O00000oO/O000000o;-><init>(Ljava/lang/Object;)V

    return-object p1
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;FLcom/airbnb/lottie/O00000o0/O000OO;Z)Lcom/airbnb/lottie/O00000oO/O000000o;
    .locals 0

    if-eqz p4, :cond_0

    invoke-static {p1, p0, p2, p3}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O000000o(Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/parser/moshi/O00000Oo;FLcom/airbnb/lottie/O00000o0/O000OO;)Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-static {p0, p2, p3}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;FLcom/airbnb/lottie/O00000o0/O000OO;)Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object p0

    return-object p0
.end method

.method private static O000000o(ILjava/lang/ref/WeakReference;)V
    .locals 2

    const-class v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;

    monitor-enter v0

    :try_start_0
    sget-object v1, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O0O0o0:Landroidx/collection/SparseArrayCompat;

    invoke-virtual {v1, p0, p1}, Landroidx/collection/SparseArrayCompat;->put(ILjava/lang/Object;)V

    monitor-exit v0

    return-void

    :catchall_0
    move-exception p0

    monitor-exit v0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    throw p0
.end method

.method private static Oo0oooO()Landroidx/collection/SparseArrayCompat;
    .locals 1

    sget-object v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O0O0o0:Landroidx/collection/SparseArrayCompat;

    if-nez v0, :cond_0

    new-instance v0, Landroidx/collection/SparseArrayCompat;

    invoke-direct {v0}, Landroidx/collection/SparseArrayCompat;-><init>()V

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O0O0o0:Landroidx/collection/SparseArrayCompat;

    :cond_0
    sget-object v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O0O0o0:Landroidx/collection/SparseArrayCompat;

    return-object v0
.end method

.method private static getInterpolator(I)Ljava/lang/ref/WeakReference;
    .locals 2
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    const-class v0, Lcom/airbnb/lottie/O00000o0/O0000oO0;

    monitor-enter v0

    :try_start_0
    invoke-static {}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->Oo0oooO()Landroidx/collection/SparseArrayCompat;

    move-result-object v1

    invoke-virtual {v1, p0}, Landroidx/collection/SparseArrayCompat;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/ref/WeakReference;

    monitor-exit v0

    return-object p0

    :catchall_0
    move-exception p0

    monitor-exit v0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    throw p0
.end method

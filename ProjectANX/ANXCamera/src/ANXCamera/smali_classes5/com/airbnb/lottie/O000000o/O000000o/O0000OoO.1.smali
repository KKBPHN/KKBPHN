.class public Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;
.super Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;
.source ""


# static fields
.field private static final O00OOoo:I = 0x20


# instance fields
.field private final O00O0ooO:Landroidx/collection/LongSparseArray;

.field private final O00O0ooo:Landroidx/collection/LongSparseArray;

.field private final O00OO0O:Landroid/graphics/RectF;

.field private final O00OO0o:I

.field private final O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final type:Lcom/airbnb/lottie/model/content/GradientType;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O00000oO;)V
    .locals 11

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->O00OoOo()Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;->Oo0o0oO()Landroid/graphics/Paint$Cap;

    move-result-object v4

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->O00OoOoo()Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;->Oo0o0oo()Landroid/graphics/Paint$Join;

    move-result-object v5

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->O00Ooo0()F

    move-result v6

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object v7

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->getWidth()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v8

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->O00Ooo00()Ljava/util/List;

    move-result-object v9

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->O00OoOoO()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v10

    move-object v1, p0

    move-object v2, p1

    move-object v3, p2

    invoke-direct/range {v1 .. v10}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Landroid/graphics/Paint$Cap;Landroid/graphics/Paint$Join;FLcom/airbnb/lottie/model/O000000o/O00000o;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Ljava/util/List;Lcom/airbnb/lottie/model/O000000o/O00000Oo;)V

    new-instance v0, Landroidx/collection/LongSparseArray;

    invoke-direct {v0}, Landroidx/collection/LongSparseArray;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00O0ooO:Landroidx/collection/LongSparseArray;

    new-instance v0, Landroidx/collection/LongSparseArray;

    invoke-direct {v0}, Landroidx/collection/LongSparseArray;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00O0ooo:Landroidx/collection/LongSparseArray;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OO0O:Landroid/graphics/RectF;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->getName()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->name:Ljava/lang/String;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->getGradientType()Lcom/airbnb/lottie/model/content/GradientType;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->type:Lcom/airbnb/lottie/model/content/GradientType;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->isHidden()Z

    move-result v0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->hidden:Z

    invoke-virtual {p1}, Lcom/airbnb/lottie/O000OoO0;->O000O0OO()Lcom/airbnb/lottie/O0000o0O;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/O0000o0O;->getDuration()F

    move-result p1

    const/high16 v0, 0x42000000    # 32.0f

    div-float/2addr p1, v0

    float-to-int p1, p1

    iput p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OO0o:I

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->O00OoOO0()Lcom/airbnb/lottie/model/O000000o/O00000o0;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000o0;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->getStartPoint()Lcom/airbnb/lottie/model/O000000o/O00000oo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000oO;->getEndPoint()Lcom/airbnb/lottie/model/O000000o/O00000oo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    return-void
.end method

.method private O00000Oo([I)[I
    .locals 3

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    if-eqz p0, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;->getValue()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, [Ljava/lang/Integer;

    array-length v0, p1

    array-length v1, p0

    const/4 v2, 0x0

    if-ne v0, v1, :cond_0

    :goto_0
    array-length v0, p1

    if-ge v2, v0, :cond_1

    aget-object v0, p0, v2

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    aput v0, p1, v2

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_0
    array-length p1, p0

    new-array p1, p1, [I

    :goto_1
    array-length v0, p0

    if-ge v2, v0, :cond_1

    aget-object v0, p0, v2

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    aput v0, p1, v2

    add-int/lit8 v2, v2, 0x1

    goto :goto_1

    :cond_1
    return-object p1
.end method

.method private Oo0oOO0()I
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v0

    iget v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OO0o:I

    int-to-float v1, v1

    mul-float/2addr v0, v1

    invoke-static {v0}, Ljava/lang/Math;->round(F)I

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v1

    iget v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OO0o:I

    int-to-float v2, v2

    mul-float/2addr v1, v2

    invoke-static {v1}, Ljava/lang/Math;->round(F)I

    move-result v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v2

    iget p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OO0o:I

    int-to-float p0, p0

    mul-float/2addr v2, p0

    invoke-static {v2}, Ljava/lang/Math;->round(F)I

    move-result p0

    if-eqz v0, :cond_0

    const/16 v2, 0x20f

    mul-int/2addr v2, v0

    goto :goto_0

    :cond_0
    const/16 v2, 0x11

    :goto_0
    if-eqz v1, :cond_1

    mul-int/lit8 v2, v2, 0x1f

    mul-int/2addr v2, v1

    :cond_1
    if-eqz p0, :cond_2

    mul-int/lit8 v2, v2, 0x1f

    mul-int/2addr v2, p0

    :cond_2
    return v2
.end method

.method private Oo0oOOO()Landroid/graphics/LinearGradient;
    .locals 14

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->Oo0oOO0()I

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00O0ooO:Landroidx/collection/LongSparseArray;

    int-to-long v2, v0

    invoke-virtual {v1, v2, v3}, Landroidx/collection/LongSparseArray;->get(J)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/LinearGradient;

    if-eqz v0, :cond_0

    return-object v0

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/PointF;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/graphics/PointF;

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lcom/airbnb/lottie/model/content/O00000o0;

    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000o0;->getColors()[I

    move-result-object v5

    invoke-direct {p0, v5}, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00000Oo([I)[I

    move-result-object v11

    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000o0;->getPositions()[F

    move-result-object v12

    iget v7, v0, Landroid/graphics/PointF;->x:F

    iget v8, v0, Landroid/graphics/PointF;->y:F

    iget v9, v1, Landroid/graphics/PointF;->x:F

    iget v10, v1, Landroid/graphics/PointF;->y:F

    new-instance v0, Landroid/graphics/LinearGradient;

    sget-object v13, Landroid/graphics/Shader$TileMode;->CLAMP:Landroid/graphics/Shader$TileMode;

    move-object v6, v0

    invoke-direct/range {v6 .. v13}, Landroid/graphics/LinearGradient;-><init>(FFFF[I[FLandroid/graphics/Shader$TileMode;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00O0ooO:Landroidx/collection/LongSparseArray;

    invoke-virtual {p0, v2, v3, v0}, Landroidx/collection/LongSparseArray;->put(JLjava/lang/Object;)V

    return-object v0
.end method

.method private Oo0oOOo()Landroid/graphics/RadialGradient;
    .locals 13

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->Oo0oOO0()I

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00O0ooo:Landroidx/collection/LongSparseArray;

    int-to-long v2, v0

    invoke-virtual {v1, v2, v3}, Landroidx/collection/LongSparseArray;->get(J)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/RadialGradient;

    if-eqz v0, :cond_0

    return-object v0

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/PointF;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/graphics/PointF;

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lcom/airbnb/lottie/model/content/O00000o0;

    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000o0;->getColors()[I

    move-result-object v5

    invoke-direct {p0, v5}, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00000Oo([I)[I

    move-result-object v10

    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000o0;->getPositions()[F

    move-result-object v11

    iget v7, v0, Landroid/graphics/PointF;->x:F

    iget v8, v0, Landroid/graphics/PointF;->y:F

    iget v0, v1, Landroid/graphics/PointF;->x:F

    iget v1, v1, Landroid/graphics/PointF;->y:F

    sub-float/2addr v0, v7

    float-to-double v4, v0

    sub-float/2addr v1, v8

    float-to-double v0, v1

    invoke-static {v4, v5, v0, v1}, Ljava/lang/Math;->hypot(DD)D

    move-result-wide v0

    double-to-float v9, v0

    new-instance v0, Landroid/graphics/RadialGradient;

    sget-object v12, Landroid/graphics/Shader$TileMode;->CLAMP:Landroid/graphics/Shader$TileMode;

    move-object v6, v0

    invoke-direct/range {v6 .. v12}, Landroid/graphics/RadialGradient;-><init>(FFF[I[FLandroid/graphics/Shader$TileMode;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00O0ooo:Landroidx/collection/LongSparseArray;

    invoke-virtual {p0, v2, v3, v0}, Landroidx/collection/LongSparseArray;->put(JLjava/lang/Object;)V

    return-object v0
.end method


# virtual methods
.method public O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 2

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->hidden:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OO0O:Landroid/graphics/RectF;

    const/4 v1, 0x0

    invoke-virtual {p0, v0, p2, v1}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->type:Lcom/airbnb/lottie/model/content/GradientType;

    sget-object v1, Lcom/airbnb/lottie/model/content/GradientType;->LINEAR:Lcom/airbnb/lottie/model/content/GradientType;

    if-ne v0, v1, :cond_1

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->Oo0oOOO()Landroid/graphics/LinearGradient;

    move-result-object v0

    goto :goto_0

    :cond_1
    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->Oo0oOOo()Landroid/graphics/RadialGradient;

    move-result-object v0

    :goto_0
    invoke-virtual {v0, p2}, Landroid/graphics/Shader;->setLocalMatrix(Landroid/graphics/Matrix;)V

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->paint:Landroid/graphics/Paint;

    invoke-virtual {v1, v0}, Landroid/graphics/Paint;->setShader(Landroid/graphics/Shader;)Landroid/graphics/Shader;

    invoke-super {p0, p1, p2, p3}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    return-void
.end method

.method public O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 1
    .param p2    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-super {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00ooo:[Ljava/lang/Integer;

    if-ne p1, v0, :cond_2

    if-nez p2, :cond_1

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    if-eqz p1, :cond_0

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    :cond_0
    const/4 p1, 0x0

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    goto :goto_0

    :cond_1
    new-instance p1, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-direct {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;-><init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    :cond_2
    :goto_0
    return-void
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;->name:Ljava/lang/String;

    return-object p0
.end method

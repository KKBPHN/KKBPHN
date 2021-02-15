.class public Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000o00;


# static fields
.field private static final O00OOoo:I = 0x20


# instance fields
.field private final O000OoO0:Lcom/airbnb/lottie/O000OoO0;

.field private final O00O0o00:Ljava/util/List;

.field private final O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

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

.field private final layer:Lcom/airbnb/lottie/model/layer/O00000o0;

.field private final name:Ljava/lang/String;
    .annotation build Landroidx/annotation/NonNull;
    .end annotation
.end field

.field private final paint:Landroid/graphics/Paint;

.field private final path:Landroid/graphics/Path;

.field private final type:Lcom/airbnb/lottie/model/content/GradientType;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O00000o;)V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Landroidx/collection/LongSparseArray;

    invoke-direct {v0}, Landroidx/collection/LongSparseArray;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0ooO:Landroidx/collection/LongSparseArray;

    new-instance v0, Landroidx/collection/LongSparseArray;

    invoke-direct {v0}, Landroidx/collection/LongSparseArray;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0ooo:Landroidx/collection/LongSparseArray;

    new-instance v0, Landroid/graphics/Path;

    invoke-direct {v0}, Landroid/graphics/Path;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o;

    const/4 v1, 0x1

    invoke-direct {v0, v1}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>(I)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->paint:Landroid/graphics/Paint;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OO0O:Landroid/graphics/RectF;

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0o00:Ljava/util/List;

    iput-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000o;->getName()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->name:Ljava/lang/String;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000o;->isHidden()Z

    move-result v0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->hidden:Z

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000o;->getGradientType()Lcom/airbnb/lottie/model/content/GradientType;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->type:Lcom/airbnb/lottie/model/content/GradientType;

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000o;->getFillType()Landroid/graphics/Path$FillType;

    move-result-object v1

    invoke-virtual {v0, v1}, Landroid/graphics/Path;->setFillType(Landroid/graphics/Path$FillType;)V

    invoke-virtual {p1}, Lcom/airbnb/lottie/O000OoO0;->O000O0OO()Lcom/airbnb/lottie/O0000o0O;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/O0000o0O;->getDuration()F

    move-result p1

    const/high16 v0, 0x42000000    # 32.0f

    div-float/2addr p1, v0

    float-to-int p1, p1

    iput p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OO0o:I

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000o;->O00OoOO0()Lcom/airbnb/lottie/model/O000000o/O00000o0;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000o0;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000o;->getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000o;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000o;->getStartPoint()Lcom/airbnb/lottie/model/O000000o/O00000oo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O00000o;->getEndPoint()Lcom/airbnb/lottie/model/O000000o/O00000oo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    return-void
.end method

.method private O00000Oo([I)[I
    .locals 3

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

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

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v0

    iget v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OO0o:I

    int-to-float v1, v1

    mul-float/2addr v0, v1

    invoke-static {v0}, Ljava/lang/Math;->round(F)I

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v1

    iget v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OO0o:I

    int-to-float v2, v2

    mul-float/2addr v1, v2

    invoke-static {v1}, Ljava/lang/Math;->round(F)I

    move-result v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v2

    iget p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OO0o:I

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

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->Oo0oOO0()I

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0ooO:Landroidx/collection/LongSparseArray;

    int-to-long v2, v0

    invoke-virtual {v1, v2, v3}, Landroidx/collection/LongSparseArray;->get(J)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/LinearGradient;

    if-eqz v0, :cond_0

    return-object v0

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/PointF;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/graphics/PointF;

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lcom/airbnb/lottie/model/content/O00000o0;

    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000o0;->getColors()[I

    move-result-object v5

    invoke-direct {p0, v5}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00000Oo([I)[I

    move-result-object v11

    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000o0;->getPositions()[F

    move-result-object v12

    new-instance v4, Landroid/graphics/LinearGradient;

    iget v7, v0, Landroid/graphics/PointF;->x:F

    iget v8, v0, Landroid/graphics/PointF;->y:F

    iget v9, v1, Landroid/graphics/PointF;->x:F

    iget v10, v1, Landroid/graphics/PointF;->y:F

    sget-object v13, Landroid/graphics/Shader$TileMode;->CLAMP:Landroid/graphics/Shader$TileMode;

    move-object v6, v4

    invoke-direct/range {v6 .. v13}, Landroid/graphics/LinearGradient;-><init>(FFFF[I[FLandroid/graphics/Shader$TileMode;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0ooO:Landroidx/collection/LongSparseArray;

    invoke-virtual {p0, v2, v3, v4}, Landroidx/collection/LongSparseArray;->put(JLjava/lang/Object;)V

    return-object v4
.end method

.method private Oo0oOOo()Landroid/graphics/RadialGradient;
    .locals 13

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->Oo0oOO0()I

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0ooo:Landroidx/collection/LongSparseArray;

    int-to-long v2, v0

    invoke-virtual {v1, v2, v3}, Landroidx/collection/LongSparseArray;->get(J)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/RadialGradient;

    if-eqz v0, :cond_0

    return-object v0

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/PointF;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/graphics/PointF;

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lcom/airbnb/lottie/model/content/O00000o0;

    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000o0;->getColors()[I

    move-result-object v5

    invoke-direct {p0, v5}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00000Oo([I)[I

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

    double-to-float v0, v0

    const/4 v1, 0x0

    cmpg-float v1, v0, v1

    if-gtz v1, :cond_1

    const v0, 0x3a83126f    # 0.001f

    :cond_1
    move v9, v0

    new-instance v0, Landroid/graphics/RadialGradient;

    sget-object v12, Landroid/graphics/Shader$TileMode;->CLAMP:Landroid/graphics/Shader$TileMode;

    move-object v6, v0

    invoke-direct/range {v6 .. v12}, Landroid/graphics/RadialGradient;-><init>(FFF[I[FLandroid/graphics/Shader$TileMode;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0ooo:Landroidx/collection/LongSparseArray;

    invoke-virtual {p0, v2, v3, v0}, Landroidx/collection/LongSparseArray;->put(JLjava/lang/Object;)V

    return-object v0
.end method


# virtual methods
.method public O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 5

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->hidden:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    const-string v0, "GradientFillContent#draw"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    invoke-virtual {v1}, Landroid/graphics/Path;->reset()V

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0o00:Ljava/util/List;

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v3

    if-ge v2, v3, :cond_1

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0o00:Ljava/util/List;

    invoke-interface {v4, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    invoke-interface {v4}, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;->getPath()Landroid/graphics/Path;

    move-result-object v4

    invoke-virtual {v3, v4, p2}, Landroid/graphics/Path;->addPath(Landroid/graphics/Path;Landroid/graphics/Matrix;)V

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_1
    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OO0O:Landroid/graphics/RectF;

    invoke-virtual {v2, v3, v1}, Landroid/graphics/Path;->computeBounds(Landroid/graphics/RectF;Z)V

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->type:Lcom/airbnb/lottie/model/content/GradientType;

    sget-object v3, Lcom/airbnb/lottie/model/content/GradientType;->LINEAR:Lcom/airbnb/lottie/model/content/GradientType;

    if-ne v2, v3, :cond_2

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->Oo0oOOO()Landroid/graphics/LinearGradient;

    move-result-object v2

    goto :goto_1

    :cond_2
    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->Oo0oOOo()Landroid/graphics/RadialGradient;

    move-result-object v2

    :goto_1
    invoke-virtual {v2, p2}, Landroid/graphics/Shader;->setLocalMatrix(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->paint:Landroid/graphics/Paint;

    invoke-virtual {p2, v2}, Landroid/graphics/Paint;->setShader(Landroid/graphics/Shader;)Landroid/graphics/Shader;

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    if-eqz p2, :cond_3

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->paint:Landroid/graphics/Paint;

    invoke-virtual {p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Landroid/graphics/ColorFilter;

    invoke-virtual {v2, p2}, Landroid/graphics/Paint;->setColorFilter(Landroid/graphics/ColorFilter;)Landroid/graphics/ColorFilter;

    :cond_3
    int-to-float p2, p3

    const/high16 p3, 0x437f0000    # 255.0f

    div-float/2addr p2, p3

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Integer;

    invoke-virtual {v2}, Ljava/lang/Integer;->intValue()I

    move-result v2

    int-to-float v2, v2

    mul-float/2addr p2, v2

    const/high16 v2, 0x42c80000    # 100.0f

    div-float/2addr p2, v2

    mul-float/2addr p2, p3

    float-to-int p2, p2

    iget-object p3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->paint:Landroid/graphics/Paint;

    const/16 v2, 0xff

    invoke-static {p2, v1, v2}, Lcom/airbnb/lottie/O00000o/O0000O0o;->clamp(III)I

    move-result p2

    invoke-virtual {p3, p2}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->paint:Landroid/graphics/Paint;

    invoke-virtual {p1, p2, p0}, Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-void
.end method

.method public O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V
    .locals 3

    iget-object p3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    invoke-virtual {p3}, Landroid/graphics/Path;->reset()V

    const/4 p3, 0x0

    move v0, p3

    :goto_0
    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0o00:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0o00:Ljava/util/List;

    invoke-interface {v2, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    invoke-interface {v2}, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;->getPath()Landroid/graphics/Path;

    move-result-object v2

    invoke-virtual {v1, v2, p2}, Landroid/graphics/Path;->addPath(Landroid/graphics/Path;Landroid/graphics/Matrix;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->path:Landroid/graphics/Path;

    invoke-virtual {p0, p1, p3}, Landroid/graphics/Path;->computeBounds(Landroid/graphics/RectF;Z)V

    iget p0, p1, Landroid/graphics/RectF;->left:F

    const/high16 p2, 0x3f800000    # 1.0f

    sub-float/2addr p0, p2

    iget p3, p1, Landroid/graphics/RectF;->top:F

    sub-float/2addr p3, p2

    iget v0, p1, Landroid/graphics/RectF;->right:F

    add-float/2addr v0, p2

    iget v1, p1, Landroid/graphics/RectF;->bottom:F

    add-float/2addr v1, p2

    invoke-virtual {p1, p0, p3, v0, v1}, Landroid/graphics/RectF;->set(FFFF)V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V
    .locals 0

    invoke-static {p1, p2, p3, p4, p0}, Lcom/airbnb/lottie/O00000o/O0000O0o;->O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;Lcom/airbnb/lottie/O000000o/O000000o/O0000o00;)V

    return-void
.end method

.method public O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 2
    .param p2    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO000oO:Ljava/lang/Integer;

    if-ne p1, v0, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p0, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    goto :goto_1

    :cond_0
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00ooO:Landroid/graphics/ColorFilter;

    const/4 v1, 0x0

    if-ne p1, v0, :cond_2

    if-nez p2, :cond_1

    iput-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    goto :goto_1

    :cond_1
    new-instance p1, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-direct {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;-><init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    :goto_0
    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    goto :goto_1

    :cond_2
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00ooo:[Ljava/lang/Integer;

    if-ne p1, v0, :cond_5

    if-nez p2, :cond_4

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    if-eqz p1, :cond_3

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    :cond_3
    iput-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    goto :goto_1

    :cond_4
    new-instance p1, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-direct {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;-><init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00OOoO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    goto :goto_0

    :cond_5
    :goto_1
    return-void
.end method

.method public O000000o(Ljava/util/List;Ljava/util/List;)V
    .locals 2

    const/4 p1, 0x0

    :goto_0
    invoke-interface {p2}, Ljava/util/List;->size()I

    move-result v0

    if-ge p1, v0, :cond_1

    invoke-interface {p2, p1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    instance-of v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    if-eqz v1, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O00O0o00:Ljava/util/List;

    check-cast v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    invoke-interface {v1, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_0
    add-int/lit8 p1, p1, 0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method public O00000oO()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;->name:Ljava/lang/String;

    return-object p0
.end method

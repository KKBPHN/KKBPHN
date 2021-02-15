.class public Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000o00;
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000o;


# instance fields
.field private final O000OoO0:Lcom/airbnb/lottie/O000OoO0;

.field private final O00Oo0Oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final O00Oo0o0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private O00Oo0oO:Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

.field private final O00Ooo00:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private O00OooOO:Z

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final path:Landroid/graphics/Path;

.field private final rect:Landroid/graphics/RectF;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000Oo0;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Landroid/graphics/Path;

    invoke-direct {v0}, Landroid/graphics/Path;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

    invoke-direct {v0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0oO:Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo0;->getName()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->name:Ljava/lang/String;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo0;->isHidden()Z

    move-result v0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->hidden:Z

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo0;->getPosition()Lcom/airbnb/lottie/model/O000000o/O0000o00;

    move-result-object p1

    invoke-interface {p1}, Lcom/airbnb/lottie/model/O000000o/O0000o00;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0o0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo0;->getSize()Lcom/airbnb/lottie/model/O000000o/O00000oo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0Oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo0;->getCornerRadius()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Ooo00:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0o0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0Oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Ooo00:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0o0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0Oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Ooo00:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    return-void
.end method

.method private invalidate()V
    .locals 1

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00OooOO:Z

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V
    .locals 0

    invoke-static {p1, p2, p3, p4, p0}, Lcom/airbnb/lottie/O00000o/O0000O0o;->O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;Lcom/airbnb/lottie/O000000o/O000000o/O0000o00;)V

    return-void
.end method

.method public O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 1
    .param p2    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00O0o:Landroid/graphics/PointF;

    if-ne p1, v0, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0Oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    :goto_0
    invoke-virtual {p0, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    goto :goto_1

    :cond_0
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->POSITION:Landroid/graphics/PointF;

    if-ne p1, v0, :cond_1

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0o0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    goto :goto_0

    :cond_1
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00O:Ljava/lang/Float;

    if-ne p1, v0, :cond_2

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Ooo00:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    goto :goto_0

    :cond_2
    :goto_1
    return-void
.end method

.method public O000000o(Ljava/util/List;Ljava/util/List;)V
    .locals 3

    const/4 p2, 0x0

    :goto_0
    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    if-ge p2, v0, :cond_1

    invoke-interface {p1, p2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    instance-of v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;

    if-eqz v1, :cond_0

    check-cast v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->getType()Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;

    move-result-object v1

    sget-object v2, Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;->O0ooo:Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;

    if-ne v1, v2, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0oO:Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

    invoke-virtual {v1, v0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o;->O000000o(Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;)V

    invoke-virtual {v0, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    :cond_0
    add-int/lit8 p2, p2, 0x1

    goto :goto_0

    :cond_1
    return-void
.end method

.method public O00000oO()V
    .locals 0

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->invalidate()V

    return-void
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getPath()Landroid/graphics/Path;
    .locals 17

    move-object/from16 v0, p0

    iget-boolean v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00OooOO:Z

    if-eqz v1, :cond_0

    iget-object v0, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    return-object v0

    :cond_0
    iget-object v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    invoke-virtual {v1}, Landroid/graphics/Path;->reset()V

    iget-boolean v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->hidden:Z

    const/4 v2, 0x1

    if-eqz v1, :cond_1

    :goto_0
    iput-boolean v2, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00OooOO:Z

    iget-object v0, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    return-object v0

    :cond_1
    iget-object v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0Oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/graphics/PointF;

    iget v3, v1, Landroid/graphics/PointF;->x:F

    const/high16 v4, 0x40000000    # 2.0f

    div-float/2addr v3, v4

    iget v1, v1, Landroid/graphics/PointF;->y:F

    div-float/2addr v1, v4

    iget-object v5, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Ooo00:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    const/4 v6, 0x0

    if-nez v5, :cond_2

    move v5, v6

    goto :goto_1

    :cond_2
    check-cast v5, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;

    invoke-virtual {v5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;->getFloatValue()F

    move-result v5

    :goto_1
    invoke-static {v3, v1}, Ljava/lang/Math;->min(FF)F

    move-result v7

    cmpl-float v8, v5, v7

    if-lez v8, :cond_3

    move v5, v7

    :cond_3
    iget-object v7, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0o0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v7}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Landroid/graphics/PointF;

    iget-object v8, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget v9, v7, Landroid/graphics/PointF;->x:F

    add-float/2addr v9, v3

    iget v10, v7, Landroid/graphics/PointF;->y:F

    sub-float/2addr v10, v1

    add-float/2addr v10, v5

    invoke-virtual {v8, v9, v10}, Landroid/graphics/Path;->moveTo(FF)V

    iget-object v8, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget v9, v7, Landroid/graphics/PointF;->x:F

    add-float/2addr v9, v3

    iget v10, v7, Landroid/graphics/PointF;->y:F

    add-float/2addr v10, v1

    sub-float/2addr v10, v5

    invoke-virtual {v8, v9, v10}, Landroid/graphics/Path;->lineTo(FF)V

    cmpl-float v8, v5, v6

    const/4 v9, 0x0

    const/high16 v10, 0x42b40000    # 90.0f

    if-lez v8, :cond_4

    iget-object v11, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    iget v12, v7, Landroid/graphics/PointF;->x:F

    add-float v13, v12, v3

    mul-float v14, v5, v4

    sub-float/2addr v13, v14

    iget v15, v7, Landroid/graphics/PointF;->y:F

    add-float v16, v15, v1

    sub-float v14, v16, v14

    add-float/2addr v12, v3

    add-float/2addr v15, v1

    invoke-virtual {v11, v13, v14, v12, v15}, Landroid/graphics/RectF;->set(FFFF)V

    iget-object v11, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget-object v12, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    invoke-virtual {v11, v12, v6, v10, v9}, Landroid/graphics/Path;->arcTo(Landroid/graphics/RectF;FFZ)V

    :cond_4
    iget-object v6, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget v11, v7, Landroid/graphics/PointF;->x:F

    sub-float/2addr v11, v3

    add-float/2addr v11, v5

    iget v12, v7, Landroid/graphics/PointF;->y:F

    add-float/2addr v12, v1

    invoke-virtual {v6, v11, v12}, Landroid/graphics/Path;->lineTo(FF)V

    if-lez v8, :cond_5

    iget-object v6, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    iget v11, v7, Landroid/graphics/PointF;->x:F

    sub-float v12, v11, v3

    iget v13, v7, Landroid/graphics/PointF;->y:F

    add-float v14, v13, v1

    mul-float v15, v5, v4

    sub-float/2addr v14, v15

    sub-float/2addr v11, v3

    add-float/2addr v11, v15

    add-float/2addr v13, v1

    invoke-virtual {v6, v12, v14, v11, v13}, Landroid/graphics/RectF;->set(FFFF)V

    iget-object v6, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget-object v11, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    invoke-virtual {v6, v11, v10, v10, v9}, Landroid/graphics/Path;->arcTo(Landroid/graphics/RectF;FFZ)V

    :cond_5
    iget-object v6, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget v11, v7, Landroid/graphics/PointF;->x:F

    sub-float/2addr v11, v3

    iget v12, v7, Landroid/graphics/PointF;->y:F

    sub-float/2addr v12, v1

    add-float/2addr v12, v5

    invoke-virtual {v6, v11, v12}, Landroid/graphics/Path;->lineTo(FF)V

    if-lez v8, :cond_6

    iget-object v6, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    iget v11, v7, Landroid/graphics/PointF;->x:F

    sub-float v12, v11, v3

    iget v13, v7, Landroid/graphics/PointF;->y:F

    sub-float v14, v13, v1

    sub-float/2addr v11, v3

    mul-float v15, v5, v4

    add-float/2addr v11, v15

    sub-float/2addr v13, v1

    add-float/2addr v13, v15

    invoke-virtual {v6, v12, v14, v11, v13}, Landroid/graphics/RectF;->set(FFFF)V

    iget-object v6, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget-object v11, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    const/high16 v12, 0x43340000    # 180.0f

    invoke-virtual {v6, v11, v12, v10, v9}, Landroid/graphics/Path;->arcTo(Landroid/graphics/RectF;FFZ)V

    :cond_6
    iget-object v6, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget v11, v7, Landroid/graphics/PointF;->x:F

    add-float/2addr v11, v3

    sub-float/2addr v11, v5

    iget v12, v7, Landroid/graphics/PointF;->y:F

    sub-float/2addr v12, v1

    invoke-virtual {v6, v11, v12}, Landroid/graphics/Path;->lineTo(FF)V

    if-lez v8, :cond_7

    iget-object v6, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    iget v8, v7, Landroid/graphics/PointF;->x:F

    add-float v11, v8, v3

    mul-float/2addr v5, v4

    sub-float/2addr v11, v5

    iget v4, v7, Landroid/graphics/PointF;->y:F

    sub-float v7, v4, v1

    add-float/2addr v8, v3

    sub-float/2addr v4, v1

    add-float/2addr v4, v5

    invoke-virtual {v6, v11, v7, v8, v4}, Landroid/graphics/RectF;->set(FFFF)V

    iget-object v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    iget-object v3, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->rect:Landroid/graphics/RectF;

    const/high16 v4, 0x43870000    # 270.0f

    invoke-virtual {v1, v3, v4, v10, v9}, Landroid/graphics/Path;->arcTo(Landroid/graphics/RectF;FFZ)V

    :cond_7
    iget-object v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    invoke-virtual {v1}, Landroid/graphics/Path;->close()V

    iget-object v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->O00Oo0oO:Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

    iget-object v3, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;->path:Landroid/graphics/Path;

    invoke-virtual {v1, v3}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o;->O000000o(Landroid/graphics/Path;)V

    goto/16 :goto_0
.end method

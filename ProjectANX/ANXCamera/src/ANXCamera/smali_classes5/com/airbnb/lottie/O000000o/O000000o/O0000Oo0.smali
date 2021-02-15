.class public Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000o00;


# instance fields
.field private final O000OoO0:Lcom/airbnb/lottie/O000OoO0;

.field private final O00O0o00:Ljava/util/List;

.field private final O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final hidden:Z

.field private final layer:Lcom/airbnb/lottie/model/layer/O00000o0;

.field private final name:Ljava/lang/String;

.field private final paint:Landroid/graphics/Paint;

.field private final path:Landroid/graphics/Path;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000Ooo;)V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Landroid/graphics/Path;

    invoke-direct {v0}, Landroid/graphics/Path;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->path:Landroid/graphics/Path;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o;

    const/4 v1, 0x1

    invoke-direct {v0, v1}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>(I)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->paint:Landroid/graphics/Paint;

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0o00:Ljava/util/List;

    iput-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Ooo;->getName()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->name:Ljava/lang/String;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Ooo;->isHidden()Z

    move-result v0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->hidden:Z

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Ooo;->getColor()Lcom/airbnb/lottie/model/O000000o/O000000o;

    move-result-object p1

    if-eqz p1, :cond_1

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Ooo;->getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object p1

    if-nez p1, :cond_0

    goto :goto_0

    :cond_0
    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->path:Landroid/graphics/Path;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Ooo;->getFillType()Landroid/graphics/Path$FillType;

    move-result-object v0

    invoke-virtual {p1, v0}, Landroid/graphics/Path;->setFillType(Landroid/graphics/Path$FillType;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Ooo;->getColor()Lcom/airbnb/lottie/model/O000000o/O000000o;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O000000o;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Ooo;->getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000o;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    return-void

    :cond_1
    :goto_0
    const/4 p1, 0x0

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    return-void
.end method


# virtual methods
.method public O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 4

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->hidden:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    const-string v0, "FillContent#draw"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->paint:Landroid/graphics/Paint;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    check-cast v2, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000OOo;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000OOo;->getIntValue()I

    move-result v2

    invoke-virtual {v1, v2}, Landroid/graphics/Paint;->setColor(I)V

    int-to-float p3, p3

    const/high16 v1, 0x437f0000    # 255.0f

    div-float/2addr p3, v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Integer;

    invoke-virtual {v2}, Ljava/lang/Integer;->intValue()I

    move-result v2

    int-to-float v2, v2

    mul-float/2addr p3, v2

    const/high16 v2, 0x42c80000    # 100.0f

    div-float/2addr p3, v2

    mul-float/2addr p3, v1

    float-to-int p3, p3

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->paint:Landroid/graphics/Paint;

    const/16 v2, 0xff

    const/4 v3, 0x0

    invoke-static {p3, v3, v2}, Lcom/airbnb/lottie/O00000o/O0000O0o;->clamp(III)I

    move-result p3

    invoke-virtual {v1, p3}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object p3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    if-eqz p3, :cond_1

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->paint:Landroid/graphics/Paint;

    invoke-virtual {p3}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Landroid/graphics/ColorFilter;

    invoke-virtual {v1, p3}, Landroid/graphics/Paint;->setColorFilter(Landroid/graphics/ColorFilter;)Landroid/graphics/ColorFilter;

    :cond_1
    iget-object p3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->path:Landroid/graphics/Path;

    invoke-virtual {p3}, Landroid/graphics/Path;->reset()V

    :goto_0
    iget-object p3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0o00:Ljava/util/List;

    invoke-interface {p3}, Ljava/util/List;->size()I

    move-result p3

    if-ge v3, p3, :cond_2

    iget-object p3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->path:Landroid/graphics/Path;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0o00:Ljava/util/List;

    invoke-interface {v1, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    invoke-interface {v1}, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;->getPath()Landroid/graphics/Path;

    move-result-object v1

    invoke-virtual {p3, v1, p2}, Landroid/graphics/Path;->addPath(Landroid/graphics/Path;Landroid/graphics/Matrix;)V

    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_2
    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->path:Landroid/graphics/Path;

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->paint:Landroid/graphics/Paint;

    invoke-virtual {p1, p2, p0}, Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-void
.end method

.method public O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V
    .locals 3

    iget-object p3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->path:Landroid/graphics/Path;

    invoke-virtual {p3}, Landroid/graphics/Path;->reset()V

    const/4 p3, 0x0

    move v0, p3

    :goto_0
    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0o00:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->path:Landroid/graphics/Path;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0o00:Ljava/util/List;

    invoke-interface {v2, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    invoke-interface {v2}, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;->getPath()Landroid/graphics/Path;

    move-result-object v2

    invoke-virtual {v1, v2, p2}, Landroid/graphics/Path;->addPath(Landroid/graphics/Path;Landroid/graphics/Matrix;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->path:Landroid/graphics/Path;

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
    .locals 1
    .param p2    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->COLOR:Ljava/lang/Integer;

    if-ne p1, v0, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    :goto_0
    invoke-virtual {p0, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    goto :goto_1

    :cond_0
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO000oO:Ljava/lang/Integer;

    if-ne p1, v0, :cond_1

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oOO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    goto :goto_0

    :cond_1
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00ooO:Landroid/graphics/ColorFilter;

    if-ne p1, v0, :cond_3

    if-nez p2, :cond_2

    const/4 p1, 0x0

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    goto :goto_1

    :cond_2
    new-instance p1, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-direct {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;-><init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    :cond_3
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

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O00O0o00:Ljava/util/List;

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

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;->name:Ljava/lang/String;

    return-object p0
.end method

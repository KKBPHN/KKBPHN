.class public Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000o;
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000Ooo;
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000o00;


# instance fields
.field private final O000OoO0:Lcom/airbnb/lottie/O000OoO0;

.field private final O00Ooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

.field private O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

.field private final hidden:Z

.field private final layer:Lcom/airbnb/lottie/model/layer/O00000o0;

.field private final matrix:Landroid/graphics/Matrix;

.field private final name:Ljava/lang/String;

.field private final offset:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final path:Landroid/graphics/Path;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000Oo;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Landroid/graphics/Matrix;

    invoke-direct {v0}, Landroid/graphics/Matrix;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->matrix:Landroid/graphics/Matrix;

    new-instance v0, Landroid/graphics/Path;

    invoke-direct {v0}, Landroid/graphics/Path;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->path:Landroid/graphics/Path;

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    iput-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo;->getName()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->name:Ljava/lang/String;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo;->isHidden()Z

    move-result p1

    iput-boolean p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->hidden:Z

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo;->getCopies()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo;->getOffset()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->offset:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->offset:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->offset:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000Oo;->getTransform()Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O0000Ooo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O000000o(Lcom/airbnb/lottie/model/layer/O00000o0;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    return-void
.end method


# virtual methods
.method public O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 9

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Float;

    invoke-virtual {v0}, Ljava/lang/Float;->floatValue()F

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->offset:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Float;

    invoke-virtual {v1}, Ljava/lang/Float;->floatValue()F

    move-result v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O00Oo0o0()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v2

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Float;

    invoke-virtual {v2}, Ljava/lang/Float;->floatValue()F

    move-result v2

    const/high16 v3, 0x42c80000    # 100.0f

    div-float/2addr v2, v3

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O00Oo0Oo()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v4

    invoke-virtual {v4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/Float;

    invoke-virtual {v4}, Ljava/lang/Float;->floatValue()F

    move-result v4

    div-float/2addr v4, v3

    float-to-int v3, v0

    add-int/lit8 v3, v3, -0x1

    :goto_0
    if-ltz v3, :cond_0

    iget-object v5, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v5, p2}, Landroid/graphics/Matrix;->set(Landroid/graphics/Matrix;)V

    iget-object v5, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->matrix:Landroid/graphics/Matrix;

    iget-object v6, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    int-to-float v7, v3

    add-float v8, v7, v1

    invoke-virtual {v6, v8}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O0000Ooo(F)Landroid/graphics/Matrix;

    move-result-object v6

    invoke-virtual {v5, v6}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    int-to-float v5, p3

    div-float/2addr v7, v0

    invoke-static {v2, v4, v7}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result v6

    mul-float/2addr v5, v6

    iget-object v6, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    iget-object v7, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->matrix:Landroid/graphics/Matrix;

    float-to-int v5, v5

    invoke-virtual {v6, p1, v7, v5}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    add-int/lit8 v3, v3, -0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    invoke-virtual {p0, p1, p2, p3}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

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

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O00000Oo(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)Z

    move-result v0

    if-eqz v0, :cond_0

    return-void

    :cond_0
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00OoO:Ljava/lang/Float;

    if-ne p1, v0, :cond_1

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    :goto_0
    invoke-virtual {p0, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    goto :goto_1

    :cond_1
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00Ooo:Ljava/lang/Float;

    if-ne p1, v0, :cond_2

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->offset:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    goto :goto_0

    :cond_2
    :goto_1
    return-void
.end method

.method public O000000o(Ljava/util/List;Ljava/util/List;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Ljava/util/List;Ljava/util/List;)V

    return-void
.end method

.method public O000000o(Ljava/util/ListIterator;)V
    .locals 8

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    if-eqz v0, :cond_0

    return-void

    :cond_0
    :goto_0
    invoke-interface {p1}, Ljava/util/ListIterator;->hasPrevious()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-interface {p1}, Ljava/util/ListIterator;->previous()Ljava/lang/Object;

    move-result-object v0

    if-eq v0, p0, :cond_1

    goto :goto_0

    :cond_1
    new-instance v6, Ljava/util/ArrayList;

    invoke-direct {v6}, Ljava/util/ArrayList;-><init>()V

    :goto_1
    invoke-interface {p1}, Ljava/util/ListIterator;->hasPrevious()Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-interface {p1}, Ljava/util/ListIterator;->previous()Ljava/lang/Object;

    move-result-object v0

    invoke-interface {v6, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    invoke-interface {p1}, Ljava/util/ListIterator;->remove()V

    goto :goto_1

    :cond_2
    invoke-static {v6}, Ljava/util/Collections;->reverse(Ljava/util/List;)V

    new-instance p1, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-boolean v5, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->hidden:Z

    const/4 v7, 0x0

    const-string v4, "Repeater"

    move-object v1, p1

    invoke-direct/range {v1 .. v7}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Ljava/lang/String;ZLjava/util/List;Lcom/airbnb/lottie/model/O000000o/O0000Ooo;)V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    return-void
.end method

.method public O00000oO()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getPath()Landroid/graphics/Path;
    .locals 6

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->getPath()Landroid/graphics/Path;

    move-result-object v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->path:Landroid/graphics/Path;

    invoke-virtual {v1}, Landroid/graphics/Path;->reset()V

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Float;

    invoke-virtual {v1}, Ljava/lang/Float;->floatValue()F

    move-result v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->offset:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Float;

    invoke-virtual {v2}, Ljava/lang/Float;->floatValue()F

    move-result v2

    float-to-int v1, v1

    add-int/lit8 v1, v1, -0x1

    :goto_0
    if-ltz v1, :cond_0

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->matrix:Landroid/graphics/Matrix;

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    int-to-float v5, v1

    add-float/2addr v5, v2

    invoke-virtual {v4, v5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O0000Ooo(F)Landroid/graphics/Matrix;

    move-result-object v4

    invoke-virtual {v3, v4}, Landroid/graphics/Matrix;->set(Landroid/graphics/Matrix;)V

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->path:Landroid/graphics/Path;

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v3, v0, v4}, Landroid/graphics/Path;->addPath(Landroid/graphics/Path;Landroid/graphics/Matrix;)V

    add-int/lit8 v1, v1, -0x1

    goto :goto_0

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;->path:Landroid/graphics/Path;

    return-object p0
.end method

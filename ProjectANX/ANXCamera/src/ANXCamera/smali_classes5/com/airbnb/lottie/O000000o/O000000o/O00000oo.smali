.class public Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000o;
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;
.implements Lcom/airbnb/lottie/model/O00000oo;


# instance fields
.field private final O000OoO0:Lcom/airbnb/lottie/O000OoO0;

.field private O00Oo0:Ljava/util/List;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O00Oo00:Landroid/graphics/Paint;

.field private O00Oo00o:Landroid/graphics/RectF;

.field private O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final contents:Ljava/util/List;

.field private final hidden:Z

.field private final matrix:Landroid/graphics/Matrix;

.field private final name:Ljava/lang/String;

.field private final path:Landroid/graphics/Path;

.field private final rect:Landroid/graphics/RectF;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000o00;)V
    .locals 7

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o00;->getName()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o00;->isHidden()Z

    move-result v4

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o00;->getItems()Ljava/util/List;

    move-result-object v0

    invoke-static {p1, p2, v0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Ljava/util/List;)Ljava/util/List;

    move-result-object v5

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o00;->getItems()Ljava/util/List;

    move-result-object p3

    invoke-static {p3}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00000Oo(Ljava/util/List;)Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

    move-result-object v6

    move-object v0, p0

    move-object v1, p1

    move-object v2, p2

    invoke-direct/range {v0 .. v6}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Ljava/lang/String;ZLjava/util/List;Lcom/airbnb/lottie/model/O000000o/O0000Ooo;)V

    return-void
.end method

.method constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Ljava/lang/String;ZLjava/util/List;Lcom/airbnb/lottie/model/O000000o/O0000Ooo;)V
    .locals 1
    .param p6    # Lcom/airbnb/lottie/model/O000000o/O0000Ooo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o;

    invoke-direct {v0}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo00:Landroid/graphics/Paint;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo00o:Landroid/graphics/RectF;

    new-instance v0, Landroid/graphics/Matrix;

    invoke-direct {v0}, Landroid/graphics/Matrix;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    new-instance v0, Landroid/graphics/Path;

    invoke-direct {v0}, Landroid/graphics/Path;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->path:Landroid/graphics/Path;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->rect:Landroid/graphics/RectF;

    iput-object p3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->name:Ljava/lang/String;

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    iput-boolean p4, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->hidden:Z

    iput-object p5, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    if-eqz p6, :cond_0

    invoke-virtual {p6}, Lcom/airbnb/lottie/model/O000000o/O0000Ooo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O000000o(Lcom/airbnb/lottie/model/layer/O00000o0;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    :cond_0
    new-instance p0, Ljava/util/ArrayList;

    invoke-direct {p0}, Ljava/util/ArrayList;-><init>()V

    invoke-interface {p5}, Ljava/util/List;->size()I

    move-result p1

    add-int/lit8 p1, p1, -0x1

    :goto_0
    if-ltz p1, :cond_2

    invoke-interface {p5, p1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    instance-of p3, p2, Lcom/airbnb/lottie/O000000o/O000000o/O0000Ooo;

    if-eqz p3, :cond_1

    check-cast p2, Lcom/airbnb/lottie/O000000o/O000000o/O0000Ooo;

    invoke-interface {p0, p2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_1
    add-int/lit8 p1, p1, -0x1

    goto :goto_0

    :cond_2
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p1

    add-int/lit8 p1, p1, -0x1

    :goto_1
    if-ltz p1, :cond_3

    invoke-interface {p0, p1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Lcom/airbnb/lottie/O000000o/O000000o/O0000Ooo;

    invoke-interface {p5}, Ljava/util/List;->size()I

    move-result p3

    invoke-interface {p5, p3}, Ljava/util/List;->listIterator(I)Ljava/util/ListIterator;

    move-result-object p3

    invoke-interface {p2, p3}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Ooo;->O000000o(Ljava/util/ListIterator;)V

    add-int/lit8 p1, p1, -0x1

    goto :goto_1

    :cond_3
    return-void
.end method

.method private static O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Ljava/util/List;)Ljava/util/List;
    .locals 3

    new-instance v0, Ljava/util/ArrayList;

    invoke-interface {p2}, Ljava/util/List;->size()I

    move-result v1

    invoke-direct {v0, v1}, Ljava/util/ArrayList;-><init>(I)V

    const/4 v1, 0x0

    :goto_0
    invoke-interface {p2}, Ljava/util/List;->size()I

    move-result v2

    if-ge v1, v2, :cond_1

    invoke-interface {p2, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/airbnb/lottie/model/content/O00000Oo;

    invoke-interface {v2, p0, p1}, Lcom/airbnb/lottie/model/content/O00000Oo;->O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    move-result-object v2

    if-eqz v2, :cond_0

    invoke-interface {v0, v2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_0
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    return-object v0
.end method

.method static O00000Oo(Ljava/util/List;)Lcom/airbnb/lottie/model/O000000o/O0000Ooo;
    .locals 3
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    const/4 v0, 0x0

    :goto_0
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_1

    invoke-interface {p0, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/model/content/O00000Oo;

    instance-of v2, v1, Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

    if-eqz v2, :cond_0

    check-cast v1, Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

    return-object v1

    :cond_0
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    const/4 p0, 0x0

    return-object p0
.end method

.method private Oo0oOo0()Z
    .locals 4

    const/4 v0, 0x0

    move v1, v0

    move v2, v1

    :goto_0
    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v3}, Ljava/util/List;->size()I

    move-result v3

    if-ge v1, v3, :cond_1

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v3, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    instance-of v3, v3, Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;

    if-eqz v3, :cond_0

    add-int/lit8 v2, v2, 0x1

    const/4 v3, 0x2

    if-lt v2, v3, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_1
    return v0
.end method


# virtual methods
.method public O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 4

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->hidden:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v0, p2}, Landroid/graphics/Matrix;->set(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    if-eqz p2, :cond_2

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object p2

    invoke-virtual {v0, p2}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getOpacity()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p2

    if-nez p2, :cond_1

    const/16 p2, 0x64

    goto :goto_0

    :cond_1
    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getOpacity()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p2

    invoke-virtual {p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result p2

    :goto_0
    int-to-float p2, p2

    const/high16 v0, 0x42c80000    # 100.0f

    div-float/2addr p2, v0

    int-to-float p3, p3

    mul-float/2addr p2, p3

    const/high16 p3, 0x437f0000    # 255.0f

    div-float/2addr p2, p3

    mul-float/2addr p2, p3

    float-to-int p3, p2

    :cond_2
    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p2}, Lcom/airbnb/lottie/O000OoO0;->O000O0oo()Z

    move-result p2

    const/16 v0, 0xff

    const/4 v1, 0x1

    if-eqz p2, :cond_3

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->Oo0oOo0()Z

    move-result p2

    if-eqz p2, :cond_3

    if-eq p3, v0, :cond_3

    move p2, v1

    goto :goto_1

    :cond_3
    const/4 p2, 0x0

    :goto_1
    if-eqz p2, :cond_4

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo00o:Landroid/graphics/RectF;

    const/4 v3, 0x0

    invoke-virtual {v2, v3, v3, v3, v3}, Landroid/graphics/RectF;->set(FFFF)V

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo00o:Landroid/graphics/RectF;

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {p0, v2, v3, v1}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo00:Landroid/graphics/Paint;

    invoke-virtual {v2, p3}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo00o:Landroid/graphics/RectF;

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo00:Landroid/graphics/Paint;

    invoke-static {p1, v2, v3}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    :cond_4
    if-eqz p2, :cond_5

    move p3, v0

    :cond_5
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    sub-int/2addr v0, v1

    :goto_2
    if-ltz v0, :cond_7

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    instance-of v2, v1, Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;

    if-eqz v2, :cond_6

    check-cast v1, Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-interface {v1, p1, v2, p3}, Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    :cond_6
    add-int/lit8 v0, v0, -0x1

    goto :goto_2

    :cond_7
    if-eqz p2, :cond_8

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    :cond_8
    return-void
.end method

.method public O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v0, p2}, Landroid/graphics/Matrix;->set(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    if-eqz p2, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object p2

    invoke-virtual {v0, p2}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    :cond_0
    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->rect:Landroid/graphics/RectF;

    const/4 v0, 0x0

    invoke-virtual {p2, v0, v0, v0, v0}, Landroid/graphics/RectF;->set(FFFF)V

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {p2}, Ljava/util/List;->size()I

    move-result p2

    add-int/lit8 p2, p2, -0x1

    :goto_0
    if-ltz p2, :cond_2

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v0, p2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    instance-of v1, v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;

    if-eqz v1, :cond_1

    check-cast v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->rect:Landroid/graphics/RectF;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-interface {v0, v1, v2, p3}, Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->rect:Landroid/graphics/RectF;

    invoke-virtual {p1, v0}, Landroid/graphics/RectF;->union(Landroid/graphics/RectF;)V

    :cond_1
    add-int/lit8 p2, p2, -0x1

    goto :goto_0

    :cond_2
    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V
    .locals 3

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0, p2}, Lcom/airbnb/lottie/model/O00000oO;->O00000o(Ljava/lang/String;I)Z

    move-result v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->getName()Ljava/lang/String;

    move-result-object v0

    const-string v1, "__container"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p4, v0}, Lcom/airbnb/lottie/model/O00000oO;->O000O0Oo(Ljava/lang/String;)Lcom/airbnb/lottie/model/O00000oO;

    move-result-object p4

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0, p2}, Lcom/airbnb/lottie/model/O00000oO;->O00000Oo(Ljava/lang/String;I)Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-virtual {p4, p0}, Lcom/airbnb/lottie/model/O00000oO;->O000000o(Lcom/airbnb/lottie/model/O00000oo;)Lcom/airbnb/lottie/model/O00000oO;

    move-result-object v0

    invoke-interface {p3, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0, p2}, Lcom/airbnb/lottie/model/O00000oO;->O00000oO(Ljava/lang/String;I)Z

    move-result v0

    if-eqz v0, :cond_3

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0, p2}, Lcom/airbnb/lottie/model/O00000oO;->O00000o0(Ljava/lang/String;I)I

    move-result v0

    add-int/2addr p2, v0

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_3

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    instance-of v2, v1, Lcom/airbnb/lottie/model/O00000oo;

    if-eqz v2, :cond_2

    check-cast v1, Lcom/airbnb/lottie/model/O00000oo;

    invoke-interface {v1, p1, p2, p3, p4}, Lcom/airbnb/lottie/model/O00000oo;->O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V

    :cond_2
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_3
    return-void
.end method

.method public O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 0
    .param p2    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O00000Oo(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)Z

    :cond_0
    return-void
.end method

.method public O000000o(Ljava/util/List;Ljava/util/List;)V
    .locals 3

    new-instance p2, Ljava/util/ArrayList;

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    add-int/2addr v0, v1

    invoke-direct {p2, v0}, Ljava/util/ArrayList;-><init>(I)V

    invoke-interface {p2, p1}, Ljava/util/List;->addAll(Ljava/util/Collection;)Z

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result p1

    add-int/lit8 p1, p1, -0x1

    :goto_0
    if-ltz p1, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v0, p1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    const/4 v2, 0x0

    invoke-interface {v1, v2, p1}, Ljava/util/List;->subList(II)Ljava/util/List;

    move-result-object v1

    invoke-interface {v0, p2, v1}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;->O000000o(Ljava/util/List;Ljava/util/List;)V

    invoke-interface {p2, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 p1, p1, -0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public O00000oO()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method

.method O00OOOo()Ljava/util/List;
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0:Ljava/util/List;

    if-nez v0, :cond_1

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0:Ljava/util/List;

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_1

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    instance-of v2, v1, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    if-eqz v2, :cond_0

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0:Ljava/util/List;

    check-cast v1, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    invoke-interface {v2, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_0
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0:Ljava/util/List;

    return-object p0
.end method

.method O00OOo0()Landroid/graphics/Matrix;
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object p0

    return-object p0

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v0}, Landroid/graphics/Matrix;->reset()V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getPath()Landroid/graphics/Path;
    .locals 4

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v0}, Landroid/graphics/Matrix;->reset()V

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O00Oo0OO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    if-eqz v0, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object v0

    invoke-virtual {v1, v0}, Landroid/graphics/Matrix;->set(Landroid/graphics/Matrix;)V

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->path:Landroid/graphics/Path;

    invoke-virtual {v0}, Landroid/graphics/Path;->reset()V

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->hidden:Z

    if-eqz v0, :cond_1

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->path:Landroid/graphics/Path;

    return-object p0

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    :goto_0
    if-ltz v0, :cond_3

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->contents:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;

    instance-of v2, v1, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    if-eqz v2, :cond_2

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->path:Landroid/graphics/Path;

    check-cast v1, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;

    invoke-interface {v1}, Lcom/airbnb/lottie/O000000o/O000000o/O0000o;->getPath()Landroid/graphics/Path;

    move-result-object v1

    iget-object v3, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v2, v1, v3}, Landroid/graphics/Path;->addPath(Landroid/graphics/Path;Landroid/graphics/Matrix;)V

    :cond_2
    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_3
    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->path:Landroid/graphics/Path;

    return-object p0
.end method

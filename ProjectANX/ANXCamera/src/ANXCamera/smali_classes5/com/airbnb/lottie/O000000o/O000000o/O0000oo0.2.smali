.class public Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000o;
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;


# instance fields
.field private final O000OoO0:Lcom/airbnb/lottie/O000OoO0;

.field private O00Oo0oO:Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

.field private final O00OooO0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private O00OooOO:Z

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final path:Landroid/graphics/Path;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000o0;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Landroid/graphics/Path;

    invoke-direct {v0}, Landroid/graphics/Path;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->path:Landroid/graphics/Path;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

    invoke-direct {v0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00Oo0oO:Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0;->getName()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->name:Ljava/lang/String;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0;->isHidden()Z

    move-result v0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->hidden:Z

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0;->O00OoooO()Lcom/airbnb/lottie/model/O000000o/O0000OOo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O0000OOo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00OooO0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00OooO0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00OooO0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    return-void
.end method

.method private invalidate()V
    .locals 1

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00OooOO:Z

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method


# virtual methods
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

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00Oo0oO:Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

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

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->invalidate()V

    return-void
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getPath()Landroid/graphics/Path;
    .locals 3

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00OooOO:Z

    if-eqz v0, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->path:Landroid/graphics/Path;

    return-object p0

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->path:Landroid/graphics/Path;

    invoke-virtual {v0}, Landroid/graphics/Path;->reset()V

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->hidden:Z

    const/4 v1, 0x1

    if-eqz v0, :cond_1

    :goto_0
    iput-boolean v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00OooOO:Z

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->path:Landroid/graphics/Path;

    return-object p0

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->path:Landroid/graphics/Path;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00OooO0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/graphics/Path;

    invoke-virtual {v0, v2}, Landroid/graphics/Path;->set(Landroid/graphics/Path;)V

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->path:Landroid/graphics/Path;

    sget-object v2, Landroid/graphics/Path$FillType;->EVEN_ODD:Landroid/graphics/Path$FillType;

    invoke-virtual {v0, v2}, Landroid/graphics/Path;->setFillType(Landroid/graphics/Path$FillType;)V

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->O00Oo0oO:Lcom/airbnb/lottie/O000000o/O000000o/O00000o;

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo0;->path:Landroid/graphics/Path;

    invoke-virtual {v0, v2}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o;->O000000o(Landroid/graphics/Path;)V

    goto :goto_0
.end method

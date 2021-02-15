.class public abstract Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
.super Ljava/lang/Object;
.source ""


# instance fields
.field protected O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O00o000O:Z

.field private final O00o000o:Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

.field private O00o00O:F

.field private O00o00O0:Ljava/lang/Object;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O00o00OO:F

.field final listeners:Ljava/util/List;

.field private progress:F


# direct methods
.method constructor <init>(Ljava/util/List;)V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/ArrayList;

    const/4 v1, 0x1

    invoke-direct {v0, v1}, Ljava/util/ArrayList;-><init>(I)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->listeners:Ljava/util/List;

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000O:Z

    const/4 v0, 0x0

    iput v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->progress:F

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00O0:Ljava/lang/Object;

    const/high16 v0, -0x40800000    # -1.0f

    iput v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00O:F

    iput v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00OO:F

    invoke-static {p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O0000Ooo(Ljava/util/List;)Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000o:Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

    return-void
.end method

.method private O00000o0()F
    .locals 2
    .annotation build Landroidx/annotation/FloatRange;
        from = 0.0
        to = 1.0
    .end annotation

    iget v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00O:F

    const/high16 v1, -0x40800000    # -1.0f

    cmpl-float v0, v0, v1

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000o:Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

    invoke-interface {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;->O00000o0()F

    move-result v0

    iput v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00O:F

    :cond_0
    iget p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00O:F

    return p0
.end method

.method private static O0000Ooo(Ljava/util/List;)Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;
    .locals 2

    invoke-interface {p0}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-eqz v0, :cond_0

    new-instance p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o0;

    const/4 v0, 0x0

    invoke-direct {p0, v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o0;-><init>(Lcom/airbnb/lottie/O000000o/O00000Oo/O000000o;)V

    return-object p0

    :cond_0
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v0

    const/4 v1, 0x1

    if-ne v0, v1, :cond_1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;-><init>(Ljava/util/List;)V

    return-object v0

    :cond_1
    new-instance v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oO;

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oO;-><init>(Ljava/util/List;)V

    return-object v0
.end method


# virtual methods
.method protected O000000o()Lcom/airbnb/lottie/O00000oO/O000000o;
    .locals 1

    const-string v0, "BaseKeyframeAnimation#getCurrentKeyframe"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000o:Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

    invoke-interface {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;->O000000o()Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object p0

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-object p0
.end method

.method abstract O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;
.end method

.method public O000000o(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 2
    .param p1    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;

    if-eqz v0, :cond_0

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Lcom/airbnb/lottie/O00000oO/O0000Oo;->O00000o0(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    :cond_0
    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;

    if-eqz p1, :cond_1

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O00000oO/O0000Oo;->O00000o0(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    :cond_1
    return-void
.end method

.method public O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->listeners:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method O0000Oo0()F
    .locals 2
    .annotation build Landroidx/annotation/FloatRange;
        from = 0.0
        to = 1.0
    .end annotation

    iget v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00OO:F

    const/high16 v1, -0x40800000    # -1.0f

    cmpl-float v0, v0, v1

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000o:Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

    invoke-interface {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;->O0000Oo0()F

    move-result v0

    iput v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00OO:F

    :cond_0
    iget p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00OO:F

    return p0
.end method

.method protected O00OOo()F
    .locals 2

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o()Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000oO/O000000o;->isStatic()Z

    move-result v1

    if-eqz v1, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object v0, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->interpolator:Landroid/view/animation/Interpolator;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOoO()F

    move-result p0

    invoke-interface {v0, p0}, Landroid/view/animation/Interpolator;->getInterpolation(F)F

    move-result p0

    return p0
.end method

.method O00OOoO()F
    .locals 3

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000O:Z

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o()Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000oO/O000000o;->isStatic()Z

    move-result v2

    if-eqz v2, :cond_1

    return v1

    :cond_1
    iget p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->progress:F

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000oO/O000000o;->O00o00o()F

    move-result v1

    sub-float/2addr p0, v1

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000oO/O000000o;->O0000Oo0()F

    move-result v1

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000oO/O000000o;->O00o00o()F

    move-result v0

    sub-float/2addr v1, v0

    div-float/2addr p0, v1

    return p0
.end method

.method public O00OOoo()V
    .locals 2

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->listeners:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->listeners:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;

    invoke-interface {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;->O00000oO()V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public O00Oo00()V
    .locals 1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000O:Z

    return-void
.end method

.method public getProgress()F
    .locals 0

    iget p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->progress:F

    return p0
.end method

.method public getValue()Ljava/lang/Object;
    .locals 2

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOo()F

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;

    if-nez v1, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000o:Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

    invoke-interface {v1, v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;->O000000o(F)Z

    move-result v1

    if-eqz v1, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00O0:Ljava/lang/Object;

    return-object p0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o()Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object v1

    invoke-virtual {p0, v1, v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00O0:Ljava/lang/Object;

    return-object v0
.end method

.method public setProgress(F)V
    .locals 1
    .param p1    # F
        .annotation build Landroidx/annotation/FloatRange;
            from = 0.0
            to = 1.0
        .end annotation
    .end param

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000o:Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

    invoke-interface {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;->isEmpty()Z

    move-result v0

    if-eqz v0, :cond_0

    return-void

    :cond_0
    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000o0()F

    move-result v0

    cmpg-float v0, p1, v0

    if-gez v0, :cond_1

    invoke-direct {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000o0()F

    move-result p1

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O0000Oo0()F

    move-result v0

    cmpl-float v0, p1, v0

    if-lez v0, :cond_2

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O0000Oo0()F

    move-result p1

    :cond_2
    :goto_0
    iget v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->progress:F

    cmpl-float v0, p1, v0

    if-nez v0, :cond_3

    return-void

    :cond_3
    iput p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->progress:F

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o000o:Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;

    invoke-interface {v0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;->O00000Oo(F)Z

    move-result p1

    if-eqz p1, :cond_4

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOoo()V

    :cond_4
    return-void
.end method

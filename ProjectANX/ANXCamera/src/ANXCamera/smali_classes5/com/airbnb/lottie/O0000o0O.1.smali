.class public Lcom/airbnb/lottie/O0000o0O;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private O000o:Ljava/util/List;

.field private final O000o0Oo:Lcom/airbnb/lottie/O000o0;

.field private O000o0o:Ljava/util/Map;

.field private final O000o0o0:Ljava/util/HashSet;

.field private O000o0oo:Ljava/util/Map;

.field private O000oO:F

.field private O000oO0:Landroidx/collection/LongSparseArray;

.field private O000oO00:Landroidx/collection/SparseArrayCompat;

.field private O000oO0O:F

.field private O000oO0o:F

.field private O000oOO:I

.field private O000oOO0:Z

.field private bounds:Landroid/graphics/Rect;

.field private images:Ljava/util/Map;

.field private layers:Ljava/util/List;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lcom/airbnb/lottie/O000o0;

    invoke-direct {v0}, Lcom/airbnb/lottie/O000o0;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0Oo:Lcom/airbnb/lottie/O000o0;

    new-instance v0, Ljava/util/HashSet;

    invoke-direct {v0}, Ljava/util/HashSet;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0o0:Ljava/util/HashSet;

    const/4 v0, 0x0

    iput v0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oOO:I

    return-void
.end method


# virtual methods
.method public O000000o(J)Lcom/airbnb/lottie/model/layer/O0000O0o;
    .locals 0
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO0:Landroidx/collection/LongSparseArray;

    invoke-virtual {p0, p1, p2}, Landroidx/collection/LongSparseArray;->get(J)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/airbnb/lottie/model/layer/O0000O0o;

    return-object p0
.end method

.method public O000000o(Landroid/graphics/Rect;FFFLjava/util/List;Landroidx/collection/LongSparseArray;Ljava/util/Map;Ljava/util/Map;Landroidx/collection/SparseArrayCompat;Ljava/util/Map;Ljava/util/List;)V
    .locals 0
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iput-object p1, p0, Lcom/airbnb/lottie/O0000o0O;->bounds:Landroid/graphics/Rect;

    iput p2, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO0O:F

    iput p3, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO0o:F

    iput p4, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO:F

    iput-object p5, p0, Lcom/airbnb/lottie/O0000o0O;->layers:Ljava/util/List;

    iput-object p6, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO0:Landroidx/collection/LongSparseArray;

    iput-object p7, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0o:Ljava/util/Map;

    iput-object p8, p0, Lcom/airbnb/lottie/O0000o0O;->images:Ljava/util/Map;

    iput-object p9, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO00:Landroidx/collection/SparseArrayCompat;

    iput-object p10, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0oo:Ljava/util/Map;

    iput-object p11, p0, Lcom/airbnb/lottie/O0000o0O;->O000o:Ljava/util/List;

    return-void
.end method

.method public O0000Oo0(Z)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0Oo:Lcom/airbnb/lottie/O000o0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000o0;->setEnabled(Z)V

    return-void
.end method

.method public O0000Ooo(I)V
    .locals 1
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iget v0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oOO:I

    add-int/2addr v0, p1

    iput v0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oOO:I

    return-void
.end method

.method public O0000oO0(Z)V
    .locals 0
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iput-boolean p1, p0, Lcom/airbnb/lottie/O0000o0O;->O000oOO0:Z

    return-void
.end method

.method public O0000oo(Ljava/lang/String;)V
    .locals 0
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    invoke-static {p1}, Lcom/airbnb/lottie/O00000o/O00000o;->warning(Ljava/lang/String;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0o0:Ljava/util/HashSet;

    invoke-virtual {p0, p1}, Ljava/util/HashSet;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public O0000ooO(Ljava/lang/String;)Lcom/airbnb/lottie/model/O0000OOo;
    .locals 3
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object v0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lcom/airbnb/lottie/O0000o0O;->O000o:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_1

    iget-object v1, p0, Lcom/airbnb/lottie/O0000o0O;->O000o:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/model/O0000OOo;

    invoke-virtual {v1, p1}, Lcom/airbnb/lottie/model/O0000OOo;->O00oOoOo(Ljava/lang/String;)Z

    move-result v2

    if-eqz v2, :cond_0

    return-object v1

    :cond_0
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    const/4 p0, 0x0

    return-object p0
.end method

.method public O0000ooo(Ljava/lang/String;)Ljava/util/List;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0o:Ljava/util/Map;

    invoke-interface {p0, p1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/util/List;

    return-object p0
.end method

.method public O00O0o()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o:Ljava/util/List;

    return-object p0
.end method

.method public O00O0o0()F
    .locals 1

    iget v0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO0o:F

    iget p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO0O:F

    sub-float/2addr v0, p0

    return v0
.end method

.method public O00O0o0O()F
    .locals 0
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iget p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO0o:F

    return p0
.end method

.method public O00O0o0o()Ljava/util/Map;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->images:Ljava/util/Map;

    return-object p0
.end method

.method public O00O0oO0()I
    .locals 0
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iget p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oOO:I

    return p0
.end method

.method public O00O0oOO()F
    .locals 0
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iget p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO0O:F

    return p0
.end method

.method public O00O0oOo()Z
    .locals 0
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iget-boolean p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oOO0:Z

    return p0
.end method

.method public O00O0oo0()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->images:Ljava/util/Map;

    invoke-interface {p0}, Ljava/util/Map;->isEmpty()Z

    move-result p0

    xor-int/lit8 p0, p0, 0x1

    return p0
.end method

.method public O00oOoOo()Lcom/airbnb/lottie/O000o0;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0Oo:Lcom/airbnb/lottie/O000o0;

    return-object p0
.end method

.method public getBounds()Landroid/graphics/Rect;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->bounds:Landroid/graphics/Rect;

    return-object p0
.end method

.method public getCharacters()Landroidx/collection/SparseArrayCompat;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO00:Landroidx/collection/SparseArrayCompat;

    return-object p0
.end method

.method public getDuration()F
    .locals 2

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000o0O;->O00O0o0()F

    move-result v0

    iget p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO:F

    div-float/2addr v0, p0

    const/high16 p0, 0x447a0000    # 1000.0f

    mul-float/2addr v0, p0

    float-to-long v0, v0

    long-to-float p0, v0

    return p0
.end method

.method public getFonts()Ljava/util/Map;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0oo:Ljava/util/Map;

    return-object p0
.end method

.method public getFrameRate()F
    .locals 0

    iget p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000oO:F

    return p0
.end method

.method public getLayers()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->layers:Ljava/util/List;

    return-object p0
.end method

.method public getWarnings()Ljava/util/ArrayList;
    .locals 2

    new-instance v0, Ljava/util/ArrayList;

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->O000o0o0:Ljava/util/HashSet;

    invoke-virtual {p0}, Ljava/util/HashSet;->size()I

    move-result v1

    new-array v1, v1, [Ljava/lang/String;

    invoke-virtual {p0, v1}, Ljava/util/HashSet;->toArray([Ljava/lang/Object;)[Ljava/lang/Object;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Ljava/util/ArrayList;-><init>(Ljava/util/Collection;)V

    return-object v0
.end method

.method public toString()Ljava/lang/String;
    .locals 3

    new-instance v0, Ljava/lang/StringBuilder;

    const-string v1, "LottieComposition:\n"

    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0O;->layers:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/model/layer/O0000O0o;

    const-string v2, "\t"

    invoke-virtual {v1, v2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->toString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_0

    :cond_0
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

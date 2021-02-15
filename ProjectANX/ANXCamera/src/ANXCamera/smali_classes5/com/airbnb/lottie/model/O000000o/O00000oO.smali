.class public Lcom/airbnb/lottie/model/O000000o/O00000oO;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/O000000o/O0000o00;


# instance fields
.field private final O00Oooo:Ljava/util/List;


# direct methods
.method public constructor <init>()V
    .locals 3

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lcom/airbnb/lottie/O00000oO/O000000o;

    new-instance v1, Landroid/graphics/PointF;

    const/4 v2, 0x0

    invoke-direct {v1, v2, v2}, Landroid/graphics/PointF;-><init>(FF)V

    invoke-direct {v0, v1}, Lcom/airbnb/lottie/O00000oO/O000000o;-><init>(Ljava/lang/Object;)V

    invoke-static {v0}, Ljava/util/Collections;->singletonList(Ljava/lang/Object;)Ljava/util/List;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;->O00Oooo:Ljava/util/List;

    return-void
.end method

.method public constructor <init>(Ljava/util/List;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;->O00Oooo:Ljava/util/List;

    return-void
.end method


# virtual methods
.method public O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;->O00Oooo:Ljava/util/List;

    const/4 v1, 0x0

    invoke-interface {v0, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O00000oO/O000000o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000oO/O000000o;->isStatic()Z

    move-result v0

    if-eqz v0, :cond_0

    new-instance v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0o;

    iget-object p0, p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;->O00Oooo:Ljava/util/List;

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0o;-><init>(Ljava/util/List;)V

    return-object v0

    :cond_0
    new-instance v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;

    iget-object p0, p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;->O00Oooo:Ljava/util/List;

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0O;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method public getKeyframes()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;->O00Oooo:Ljava/util/List;

    return-object p0
.end method

.method public isStatic()Z
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;->O00Oooo:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    const/4 v1, 0x0

    const/4 v2, 0x1

    if-ne v0, v2, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;->O00Oooo:Ljava/util/List;

    invoke-interface {p0, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/airbnb/lottie/O00000oO/O000000o;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000oO/O000000o;->isStatic()Z

    move-result p0

    if-eqz p0, :cond_0

    move v1, v2

    :cond_0
    return v1
.end method

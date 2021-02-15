.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;
.super Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
.source ""


# instance fields
.field private final O00o0O0O:Ljava/lang/Object;

.field private final frameInfo:Lcom/airbnb/lottie/O00000oO/O00000Oo;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 1

    const/4 v0, 0x0

    invoke-direct {p0, p1, v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;-><init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;Ljava/lang/Object;)V
    .locals 1
    .param p2    # Ljava/lang/Object;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;-><init>(Ljava/util/List;)V

    new-instance v0, Lcom/airbnb/lottie/O00000oO/O00000Oo;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000oO/O00000Oo;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;->frameInfo:Lcom/airbnb/lottie/O00000oO/O00000Oo;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    iput-object p2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;->O00o0O0O:Ljava/lang/Object;

    return-void
.end method


# virtual methods
.method O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;->getValue()Ljava/lang/Object;

    move-result-object p0

    return-object p0
.end method

.method O0000Oo0()F
    .locals 0

    const/high16 p0, 0x3f800000    # 1.0f

    return p0
.end method

.method public O00OOoo()V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;

    if-eqz v0, :cond_0

    invoke-super {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOoo()V

    :cond_0
    return-void
.end method

.method public getValue()Ljava/lang/Object;
    .locals 8

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00o00:Lcom/airbnb/lottie/O00000oO/O0000Oo;

    iget-object v4, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;->O00o0O0O:Ljava/lang/Object;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v5

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v6

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getProgress()F

    move-result v7

    const/4 v1, 0x0

    const/4 v2, 0x0

    move-object v3, v4

    invoke-virtual/range {v0 .. v7}, Lcom/airbnb/lottie/O00000oO/O0000Oo;->O00000Oo(FFLjava/lang/Object;Ljava/lang/Object;FFF)Ljava/lang/Object;

    move-result-object p0

    return-object p0
.end method

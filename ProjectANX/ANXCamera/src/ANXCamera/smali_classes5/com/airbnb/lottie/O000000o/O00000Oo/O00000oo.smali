.class final Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000o;


# instance fields
.field private final O00o000:Lcom/airbnb/lottie/O00000oO/O000000o;
    .annotation build Landroidx/annotation/NonNull;
    .end annotation
.end field

.field private O00o0000:F


# direct methods
.method constructor <init>(Ljava/util/List;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/high16 v0, -0x40800000    # -1.0f

    iput v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;->O00o0000:F

    const/4 v0, 0x0

    invoke-interface {p1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lcom/airbnb/lottie/O00000oO/O000000o;

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;->O00o000:Lcom/airbnb/lottie/O00000oO/O000000o;

    return-void
.end method


# virtual methods
.method public O000000o()Lcom/airbnb/lottie/O00000oO/O000000o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;->O00o000:Lcom/airbnb/lottie/O00000oO/O000000o;

    return-object p0
.end method

.method public O000000o(F)Z
    .locals 1

    iget v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;->O00o0000:F

    cmpl-float v0, v0, p1

    if-nez v0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    iput p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;->O00o0000:F

    const/4 p0, 0x0

    return p0
.end method

.method public O00000Oo(F)Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;->O00o000:Lcom/airbnb/lottie/O00000oO/O000000o;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000oO/O000000o;->isStatic()Z

    move-result p0

    xor-int/lit8 p0, p0, 0x1

    return p0
.end method

.method public O00000o0()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;->O00o000:Lcom/airbnb/lottie/O00000oO/O000000o;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000oO/O000000o;->O00o00o()F

    move-result p0

    return p0
.end method

.method public O0000Oo0()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000oo;->O00o000:Lcom/airbnb/lottie/O00000oO/O000000o;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000oO/O000000o;->O0000Oo0()F

    move-result p0

    return p0
.end method

.method public isEmpty()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.class public Lcom/airbnb/lottie/O00000oO/O00000o0;
.super Lcom/airbnb/lottie/O00000oO/O00000oo;
.source ""


# direct methods
.method public constructor <init>(Ljava/lang/Float;Ljava/lang/Float;)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lcom/airbnb/lottie/O00000oO/O00000oo;-><init>(Ljava/lang/Object;Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/lang/Float;Ljava/lang/Float;Landroid/view/animation/Interpolator;)V
    .locals 0

    invoke-direct {p0, p1, p2, p3}, Lcom/airbnb/lottie/O00000oO/O00000oo;-><init>(Ljava/lang/Object;Ljava/lang/Object;Landroid/view/animation/Interpolator;)V

    return-void
.end method


# virtual methods
.method O000000o(Ljava/lang/Float;Ljava/lang/Float;F)Ljava/lang/Float;
    .locals 0

    invoke-virtual {p1}, Ljava/lang/Float;->floatValue()F

    move-result p0

    invoke-virtual {p2}, Ljava/lang/Float;->floatValue()F

    move-result p1

    invoke-static {p0, p1, p3}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result p0

    invoke-static {p0}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object p0

    return-object p0
.end method

.method bridge synthetic O000000o(Ljava/lang/Object;Ljava/lang/Object;F)Ljava/lang/Object;
    .locals 0

    check-cast p1, Ljava/lang/Float;

    check-cast p2, Ljava/lang/Float;

    invoke-virtual {p0, p1, p2, p3}, Lcom/airbnb/lottie/O00000oO/O00000o0;->O000000o(Ljava/lang/Float;Ljava/lang/Float;F)Ljava/lang/Float;

    move-result-object p0

    return-object p0
.end method

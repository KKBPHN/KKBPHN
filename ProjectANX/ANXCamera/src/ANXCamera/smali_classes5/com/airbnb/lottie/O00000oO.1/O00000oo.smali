.class abstract Lcom/airbnb/lottie/O00000oO/O00000oo;
.super Lcom/airbnb/lottie/O00000oO/O0000Oo;
.source ""


# instance fields
.field private final endValue:Ljava/lang/Object;

.field private final interpolator:Landroid/view/animation/Interpolator;

.field private final startValue:Ljava/lang/Object;


# direct methods
.method constructor <init>(Ljava/lang/Object;Ljava/lang/Object;)V
    .locals 1

    new-instance v0, Landroid/view/animation/LinearInterpolator;

    invoke-direct {v0}, Landroid/view/animation/LinearInterpolator;-><init>()V

    invoke-direct {p0, p1, p2, v0}, Lcom/airbnb/lottie/O00000oO/O00000oo;-><init>(Ljava/lang/Object;Ljava/lang/Object;Landroid/view/animation/Interpolator;)V

    return-void
.end method

.method constructor <init>(Ljava/lang/Object;Ljava/lang/Object;Landroid/view/animation/Interpolator;)V
    .locals 0

    invoke-direct {p0}, Lcom/airbnb/lottie/O00000oO/O0000Oo;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O00000oO/O00000oo;->startValue:Ljava/lang/Object;

    iput-object p2, p0, Lcom/airbnb/lottie/O00000oO/O00000oo;->endValue:Ljava/lang/Object;

    iput-object p3, p0, Lcom/airbnb/lottie/O00000oO/O00000oo;->interpolator:Landroid/view/animation/Interpolator;

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O00000oO/O00000Oo;)Ljava/lang/Object;
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O00000oO/O00000oo;->interpolator:Landroid/view/animation/Interpolator;

    invoke-virtual {p1}, Lcom/airbnb/lottie/O00000oO/O00000Oo;->O00o0Oo()F

    move-result p1

    invoke-interface {v0, p1}, Landroid/view/animation/Interpolator;->getInterpolation(F)F

    move-result p1

    iget-object v0, p0, Lcom/airbnb/lottie/O00000oO/O00000oo;->startValue:Ljava/lang/Object;

    iget-object v1, p0, Lcom/airbnb/lottie/O00000oO/O00000oo;->endValue:Ljava/lang/Object;

    invoke-virtual {p0, v0, v1, p1}, Lcom/airbnb/lottie/O00000oO/O00000oo;->O000000o(Ljava/lang/Object;Ljava/lang/Object;F)Ljava/lang/Object;

    move-result-object p0

    return-object p0
.end method

.method abstract O000000o(Ljava/lang/Object;Ljava/lang/Object;F)Ljava/lang/Object;
.end method

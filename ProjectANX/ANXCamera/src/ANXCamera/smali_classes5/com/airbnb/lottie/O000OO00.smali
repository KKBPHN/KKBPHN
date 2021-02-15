.class Lcom/airbnb/lottie/O000OO00;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Landroid/animation/ValueAnimator$AnimatorUpdateListener;


# instance fields
.field final synthetic this$0:Lcom/airbnb/lottie/O000OoO0;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/O000OoO0;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O000OO00;->this$0:Lcom/airbnb/lottie/O000OoO0;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onAnimationUpdate(Landroid/animation/ValueAnimator;)V
    .locals 0

    iget-object p1, p0, Lcom/airbnb/lottie/O000OO00;->this$0:Lcom/airbnb/lottie/O000OoO0;

    invoke-static {p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/O000OoO0;)Lcom/airbnb/lottie/model/layer/O00000oO;

    move-result-object p1

    if-eqz p1, :cond_0

    iget-object p1, p0, Lcom/airbnb/lottie/O000OO00;->this$0:Lcom/airbnb/lottie/O000OoO0;

    invoke-static {p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/O000OoO0;)Lcom/airbnb/lottie/model/layer/O00000oO;

    move-result-object p1

    iget-object p0, p0, Lcom/airbnb/lottie/O000OO00;->this$0:Lcom/airbnb/lottie/O000OoO0;

    invoke-static {p0}, Lcom/airbnb/lottie/O000OoO0;->O00000Oo(Lcom/airbnb/lottie/O000OoO0;)Lcom/airbnb/lottie/O00000o/O00000oO;

    move-result-object p0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000o0O()F

    move-result p0

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/model/layer/O00000oO;->setProgress(F)V

    :cond_0
    return-void
.end method

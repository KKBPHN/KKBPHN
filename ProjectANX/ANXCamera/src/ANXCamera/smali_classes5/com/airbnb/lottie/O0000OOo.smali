.class Lcom/airbnb/lottie/O0000OOo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000OoO;


# instance fields
.field final synthetic this$0:Lcom/airbnb/lottie/LottieAnimationView;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/LottieAnimationView;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O0000OOo;->this$0:Lcom/airbnb/lottie/LottieAnimationView;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic O000000o(Ljava/lang/Object;)V
    .locals 0

    check-cast p1, Ljava/lang/Throwable;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O0000OOo;->O0000OOo(Ljava/lang/Throwable;)V

    return-void
.end method

.method public O0000OOo(Ljava/lang/Throwable;)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O0000OOo;->this$0:Lcom/airbnb/lottie/LottieAnimationView;

    invoke-static {v0}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Lcom/airbnb/lottie/LottieAnimationView;)I

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O0000OOo;->this$0:Lcom/airbnb/lottie/LottieAnimationView;

    invoke-static {v0}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Lcom/airbnb/lottie/LottieAnimationView;)I

    move-result v1

    invoke-virtual {v0, v1}, Lcom/airbnb/lottie/LottieAnimationView;->setImageResource(I)V

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O0000OOo;->this$0:Lcom/airbnb/lottie/LottieAnimationView;

    invoke-static {v0}, Lcom/airbnb/lottie/LottieAnimationView;->O00000Oo(Lcom/airbnb/lottie/LottieAnimationView;)Lcom/airbnb/lottie/O000OoO;

    move-result-object v0

    if-nez v0, :cond_1

    invoke-static {}, Lcom/airbnb/lottie/LottieAnimationView;->access$200()Lcom/airbnb/lottie/O000OoO;

    move-result-object p0

    goto :goto_0

    :cond_1
    iget-object p0, p0, Lcom/airbnb/lottie/O0000OOo;->this$0:Lcom/airbnb/lottie/LottieAnimationView;

    invoke-static {p0}, Lcom/airbnb/lottie/LottieAnimationView;->O00000Oo(Lcom/airbnb/lottie/LottieAnimationView;)Lcom/airbnb/lottie/O000OoO;

    move-result-object p0

    :goto_0
    invoke-interface {p0, p1}, Lcom/airbnb/lottie/O000OoO;->O000000o(Ljava/lang/Object;)V

    return-void
.end method

.class Lcom/airbnb/lottie/O000OooO;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field final synthetic this$0:Lcom/airbnb/lottie/O000o000;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/O000o000;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O000OooO;->this$0:Lcom/airbnb/lottie/O000o000;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public run()V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OooO;->this$0:Lcom/airbnb/lottie/O000o000;

    invoke-static {v0}, Lcom/airbnb/lottie/O000o000;->O000000o(Lcom/airbnb/lottie/O000o000;)Lcom/airbnb/lottie/O000Ooo;

    move-result-object v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000OooO;->this$0:Lcom/airbnb/lottie/O000o000;

    invoke-static {v0}, Lcom/airbnb/lottie/O000o000;->O000000o(Lcom/airbnb/lottie/O000o000;)Lcom/airbnb/lottie/O000Ooo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000Ooo;->getValue()Ljava/lang/Object;

    move-result-object v1

    iget-object p0, p0, Lcom/airbnb/lottie/O000OooO;->this$0:Lcom/airbnb/lottie/O000o000;

    if-eqz v1, :cond_1

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000Ooo;->getValue()Ljava/lang/Object;

    move-result-object v0

    invoke-static {p0, v0}, Lcom/airbnb/lottie/O000o000;->O000000o(Lcom/airbnb/lottie/O000o000;Ljava/lang/Object;)V

    goto :goto_0

    :cond_1
    invoke-virtual {v0}, Lcom/airbnb/lottie/O000Ooo;->getException()Ljava/lang/Throwable;

    move-result-object v0

    invoke-static {p0, v0}, Lcom/airbnb/lottie/O000o000;->O000000o(Lcom/airbnb/lottie/O000o000;Ljava/lang/Throwable;)V

    :goto_0
    return-void
.end method

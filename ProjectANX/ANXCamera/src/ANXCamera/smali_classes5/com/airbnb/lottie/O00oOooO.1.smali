.class Lcom/airbnb/lottie/O00oOooO;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/util/concurrent/Callable;


# instance fields
.field final synthetic O000oo0O:Lcom/airbnb/lottie/O0000o0O;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/O0000o0O;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O00oOooO;->O000oo0O:Lcom/airbnb/lottie/O0000o0O;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public call()Lcom/airbnb/lottie/O000Ooo;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000Ooo;

    iget-object p0, p0, Lcom/airbnb/lottie/O00oOooO;->O000oo0O:Lcom/airbnb/lottie/O0000o0O;

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/O000Ooo;-><init>(Ljava/lang/Object;)V

    return-object v0
.end method

.method public bridge synthetic call()Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00oOooO;->call()Lcom/airbnb/lottie/O000Ooo;

    move-result-object p0

    return-object p0
.end method

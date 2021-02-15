.class Lcom/airbnb/lottie/O0000oo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/util/concurrent/Callable;


# instance fields
.field final synthetic O000oOoo:Ljava/lang/String;

.field final synthetic val$cacheKey:Ljava/lang/String;


# direct methods
.method constructor <init>(Ljava/lang/String;Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O0000oo;->O000oOoo:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/O0000oo;->val$cacheKey:Ljava/lang/String;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public call()Lcom/airbnb/lottie/O000Ooo;
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O0000oo;->O000oOoo:Ljava/lang/String;

    iget-object p0, p0, Lcom/airbnb/lottie/O0000oo;->val$cacheKey:Ljava/lang/String;

    invoke-static {v0, p0}, Lcom/airbnb/lottie/O00oOooo;->O00000o(Ljava/lang/String;Ljava/lang/String;)Lcom/airbnb/lottie/O000Ooo;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic call()Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000oo;->call()Lcom/airbnb/lottie/O000Ooo;

    move-result-object p0

    return-object p0
.end method

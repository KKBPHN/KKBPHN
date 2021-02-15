.class Lcom/airbnb/lottie/O0000oO0;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/util/concurrent/Callable;


# instance fields
.field final synthetic O000oOOO:Ljava/lang/String;

.field final synthetic val$context:Landroid/content/Context;


# direct methods
.method constructor <init>(Landroid/content/Context;Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O0000oO0;->val$context:Landroid/content/Context;

    iput-object p2, p0, Lcom/airbnb/lottie/O0000oO0;->O000oOOO:Ljava/lang/String;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public call()Lcom/airbnb/lottie/O000Ooo;
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O0000oO0;->val$context:Landroid/content/Context;

    iget-object p0, p0, Lcom/airbnb/lottie/O0000oO0;->O000oOOO:Ljava/lang/String;

    invoke-static {v0, p0}, Lcom/airbnb/lottie/network/O00000Oo;->O00000oo(Landroid/content/Context;Ljava/lang/String;)Lcom/airbnb/lottie/O000Ooo;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic call()Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000oO0;->call()Lcom/airbnb/lottie/O000Ooo;

    move-result-object p0

    return-object p0
.end method

.class Lcom/airbnb/lottie/O0000oO;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/util/concurrent/Callable;


# instance fields
.field final synthetic O000oOOo:Ljava/lang/String;

.field final synthetic val$appContext:Landroid/content/Context;

.field final synthetic val$cacheKey:Ljava/lang/String;


# direct methods
.method constructor <init>(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O0000oO;->val$appContext:Landroid/content/Context;

    iput-object p2, p0, Lcom/airbnb/lottie/O0000oO;->O000oOOo:Ljava/lang/String;

    iput-object p3, p0, Lcom/airbnb/lottie/O0000oO;->val$cacheKey:Ljava/lang/String;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public call()Lcom/airbnb/lottie/O000Ooo;
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O0000oO;->val$appContext:Landroid/content/Context;

    iget-object v1, p0, Lcom/airbnb/lottie/O0000oO;->O000oOOo:Ljava/lang/String;

    iget-object p0, p0, Lcom/airbnb/lottie/O0000oO;->val$cacheKey:Ljava/lang/String;

    invoke-static {v0, v1, p0}, Lcom/airbnb/lottie/O00oOooo;->O00000Oo(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)Lcom/airbnb/lottie/O000Ooo;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic call()Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000oO;->call()Lcom/airbnb/lottie/O000Ooo;

    move-result-object p0

    return-object p0
.end method

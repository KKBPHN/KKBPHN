.class Lcom/airbnb/lottie/O00000oo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000OoO;


# direct methods
.method constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic O000000o(Ljava/lang/Object;)V
    .locals 0

    check-cast p1, Ljava/lang/Throwable;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000oo;->O0000OOo(Ljava/lang/Throwable;)V

    return-void
.end method

.method public O0000OOo(Ljava/lang/Throwable;)V
    .locals 1

    invoke-static {p1}, Lcom/airbnb/lottie/O00000o/O0000OOo;->isNetworkException(Ljava/lang/Throwable;)Z

    move-result p0

    if-eqz p0, :cond_0

    const-string p0, "Unable to load composition."

    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o/O00000o;->O00000Oo(Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalStateException;

    const-string v0, "Unable to parse composition"

    invoke-direct {p0, v0, p1}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;Ljava/lang/Throwable;)V

    throw p0
.end method

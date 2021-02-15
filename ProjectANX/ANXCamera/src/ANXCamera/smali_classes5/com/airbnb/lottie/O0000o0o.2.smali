.class Lcom/airbnb/lottie/O0000o0o;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000OoO;


# instance fields
.field final synthetic val$cacheKey:Ljava/lang/String;


# direct methods
.method constructor <init>(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O0000o0o;->val$cacheKey:Ljava/lang/String;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic O000000o(Ljava/lang/Object;)V
    .locals 0

    check-cast p1, Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O0000o0o;->O00000o(Lcom/airbnb/lottie/O0000o0O;)V

    return-void
.end method

.method public O00000o(Lcom/airbnb/lottie/O0000o0O;)V
    .locals 0

    invoke-static {}, Lcom/airbnb/lottie/O00oOooo;->access$000()Ljava/util/Map;

    move-result-object p1

    iget-object p0, p0, Lcom/airbnb/lottie/O0000o0o;->val$cacheKey:Ljava/lang/String;

    invoke-interface {p1, p0}, Ljava/util/Map;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

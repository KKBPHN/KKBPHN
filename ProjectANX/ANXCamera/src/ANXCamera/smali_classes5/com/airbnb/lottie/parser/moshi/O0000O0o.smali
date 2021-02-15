.class Lcom/airbnb/lottie/parser/moshi/O0000O0o;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private O0OO0o0:Lcom/airbnb/lottie/parser/moshi/O0000o00;


# direct methods
.method constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method O00000Oo(Lcom/airbnb/lottie/parser/moshi/O0000o00;)V
    .locals 2

    const/4 v0, 0x0

    :goto_0
    move-object v1, v0

    move-object v0, p1

    move-object p1, v1

    if-eqz v0, :cond_0

    iput-object p1, v0, Lcom/airbnb/lottie/parser/moshi/O0000o00;->parent:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iget-object p1, v0, Lcom/airbnb/lottie/parser/moshi/O0000o00;->left:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    goto :goto_0

    :cond_0
    iput-object p1, p0, Lcom/airbnb/lottie/parser/moshi/O0000O0o;->O0OO0o0:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    return-void
.end method

.method public next()Lcom/airbnb/lottie/parser/moshi/O0000o00;
    .locals 4

    iget-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000O0o;->O0OO0o0:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    const/4 v1, 0x0

    if-nez v0, :cond_0

    return-object v1

    :cond_0
    iget-object v2, v0, Lcom/airbnb/lottie/parser/moshi/O0000o00;->parent:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iput-object v1, v0, Lcom/airbnb/lottie/parser/moshi/O0000o00;->parent:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iget-object v1, v0, Lcom/airbnb/lottie/parser/moshi/O0000o00;->right:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    :goto_0
    move-object v3, v2

    move-object v2, v1

    move-object v1, v3

    if-eqz v2, :cond_1

    iput-object v1, v2, Lcom/airbnb/lottie/parser/moshi/O0000o00;->parent:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iget-object v1, v2, Lcom/airbnb/lottie/parser/moshi/O0000o00;->left:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    goto :goto_0

    :cond_1
    iput-object v1, p0, Lcom/airbnb/lottie/parser/moshi/O0000O0o;->O0OO0o0:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    return-object v0
.end method

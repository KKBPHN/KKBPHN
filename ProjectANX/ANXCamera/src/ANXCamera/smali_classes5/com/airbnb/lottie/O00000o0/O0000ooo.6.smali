.class Lcom/airbnb/lottie/O00000o0/O0000ooo;
.super Ljava/lang/Object;
.source ""


# direct methods
.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;
    .locals 3

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object v0

    sget-object v1, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->BEGIN_OBJECT:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne v0, v1, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    invoke-static {}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00o0O0O()F

    move-result v1

    sget-object v2, Lcom/airbnb/lottie/O00000o0/O00oOooO;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O00oOooO;

    invoke-static {p0, p1, v1, v2, v0}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;FLcom/airbnb/lottie/O00000o0/O000OO;Z)Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object p0

    new-instance v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;

    invoke-direct {v0, p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;-><init>(Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000oO/O000000o;)V

    return-object v0
.end method

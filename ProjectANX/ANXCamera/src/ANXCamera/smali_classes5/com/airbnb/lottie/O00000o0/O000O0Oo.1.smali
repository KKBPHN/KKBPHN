.class Lcom/airbnb/lottie/O00000o0/O000O0Oo;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 5

    const-string v0, "nm"

    const-string v1, "c"

    const-string v2, "o"

    const-string v3, "tr"

    const-string v4, "hd"

    filled-new-array {v0, v1, v2, v3, v4}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O000O0Oo;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/content/O0000Oo;
    .locals 8

    const/4 v0, 0x0

    const/4 v1, 0x0

    move v7, v0

    move-object v3, v1

    move-object v4, v3

    move-object v5, v4

    move-object v6, v5

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_5

    sget-object v1, Lcom/airbnb/lottie/O00000o0/O000O0Oo;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v1

    if-eqz v1, :cond_4

    const/4 v2, 0x1

    if-eq v1, v2, :cond_3

    const/4 v2, 0x2

    if-eq v1, v2, :cond_2

    const/4 v2, 0x3

    if-eq v1, v2, :cond_1

    const/4 v2, 0x4

    if-eq v1, v2, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v7

    goto :goto_0

    :cond_1
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o0;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

    move-result-object v6

    goto :goto_0

    :cond_2
    invoke-static {p0, p1, v0}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Z)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v5

    goto :goto_0

    :cond_3
    invoke-static {p0, p1, v0}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Z)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v4

    goto :goto_0

    :cond_4
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v3

    goto :goto_0

    :cond_5
    new-instance p0, Lcom/airbnb/lottie/model/content/O0000Oo;

    move-object v2, p0

    invoke-direct/range {v2 .. v7}, Lcom/airbnb/lottie/model/content/O0000Oo;-><init>(Ljava/lang/String;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O0000Ooo;Z)V

    return-object p0
.end method

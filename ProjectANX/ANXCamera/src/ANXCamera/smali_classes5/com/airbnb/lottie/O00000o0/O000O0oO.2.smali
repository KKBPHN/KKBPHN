.class Lcom/airbnb/lottie/O00000o0/O000O0oO;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 3

    const-string v0, "nm"

    const-string v1, "hd"

    const-string v2, "it"

    filled-new-array {v0, v1, v2}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O000O0oO;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/content/O0000o00;
    .locals 5

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    const/4 v1, 0x0

    const/4 v2, 0x0

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v3

    if-eqz v3, :cond_5

    sget-object v3, Lcom/airbnb/lottie/O00000o0/O000O0oO;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v3}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v3

    if-eqz v3, :cond_4

    const/4 v4, 0x1

    if-eq v3, v4, :cond_3

    const/4 v4, 0x2

    if-eq v3, v4, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginArray()V

    :cond_1
    :goto_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O0000O0o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/content/O00000Oo;

    move-result-object v3

    if-eqz v3, :cond_1

    invoke-interface {v0, v3}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto :goto_1

    :cond_2
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endArray()V

    goto :goto_0

    :cond_3
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v2

    goto :goto_0

    :cond_4
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v1

    goto :goto_0

    :cond_5
    new-instance p0, Lcom/airbnb/lottie/model/content/O0000o00;

    invoke-direct {p0, v1, v0, v2}, Lcom/airbnb/lottie/model/content/O0000o00;-><init>(Ljava/lang/String;Ljava/util/List;Z)V

    return-object p0
.end method

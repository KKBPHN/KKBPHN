.class Lcom/airbnb/lottie/O00000o0/O00000oO;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 5

    const-string v0, "nm"

    const-string v1, "p"

    const-string v2, "s"

    const-string v3, "hd"

    const-string v4, "d"

    filled-new-array {v0, v1, v2, v3, v4}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O00000oO;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;I)Lcom/airbnb/lottie/model/content/O000000o;
    .locals 10

    const/4 v0, 0x1

    const/4 v1, 0x0

    const/4 v2, 0x3

    if-ne p2, v2, :cond_0

    move p2, v0

    goto :goto_0

    :cond_0
    move p2, v1

    :goto_0
    const/4 v3, 0x0

    move v8, p2

    move v9, v1

    move-object v5, v3

    move-object v6, v5

    move-object v7, v6

    :goto_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result p2

    if-eqz p2, :cond_7

    sget-object p2, Lcom/airbnb/lottie/O00000o0/O00000oO;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, p2}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result p2

    if-eqz p2, :cond_6

    if-eq p2, v0, :cond_5

    const/4 v3, 0x2

    if-eq p2, v3, :cond_4

    if-eq p2, v2, :cond_3

    const/4 v3, 0x4

    if-eq p2, v3, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_1

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result p2

    if-ne p2, v2, :cond_2

    move v8, v0

    goto :goto_1

    :cond_2
    move v8, v1

    goto :goto_1

    :cond_3
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v9

    goto :goto_1

    :cond_4
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O0000O0o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000oo;

    move-result-object v7

    goto :goto_1

    :cond_5
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O000000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000o00;

    move-result-object v6

    goto :goto_1

    :cond_6
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v5

    goto :goto_1

    :cond_7
    new-instance p0, Lcom/airbnb/lottie/model/content/O000000o;

    move-object v4, p0

    invoke-direct/range {v4 .. v9}, Lcom/airbnb/lottie/model/content/O000000o;-><init>(Ljava/lang/String;Lcom/airbnb/lottie/model/O000000o/O0000o00;Lcom/airbnb/lottie/model/O000000o/O00000oo;ZZ)V

    return-object p0
.end method

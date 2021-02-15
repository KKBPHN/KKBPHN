.class Lcom/airbnb/lottie/O00000o0/O0000OoO;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 4

    const-string v0, "fFamily"

    const-string v1, "fName"

    const-string v2, "fStyle"

    const-string v3, "ascent"

    filled-new-array {v0, v1, v2, v3}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000OoO;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;)Lcom/airbnb/lottie/model/O00000o0;
    .locals 6

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    const/4 v0, 0x0

    const/4 v1, 0x0

    move-object v2, v0

    move v3, v1

    move-object v1, v2

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v4

    if-eqz v4, :cond_4

    sget-object v4, Lcom/airbnb/lottie/O00000o0/O0000OoO;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v4}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v4

    if-eqz v4, :cond_3

    const/4 v5, 0x1

    if-eq v4, v5, :cond_2

    const/4 v5, 0x2

    if-eq v4, v5, :cond_1

    const/4 v5, 0x3

    if-eq v4, v5, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v3

    double-to-float v3, v3

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v2

    goto :goto_0

    :cond_2
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v1

    goto :goto_0

    :cond_3
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_4
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    new-instance p0, Lcom/airbnb/lottie/model/O00000o0;

    invoke-direct {p0, v0, v1, v2, v3}, Lcom/airbnb/lottie/model/O00000o0;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;F)V

    return-object p0
.end method

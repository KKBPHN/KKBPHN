.class public Lcom/airbnb/lottie/O00000o0/O00000Oo;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static O0O0O:Lcom/airbnb/lottie/parser/moshi/O000000o;

.field private static O0O0O0o:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 4

    const-string v0, "a"

    filled-new-array {v0}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O00000Oo;->O0O0O0o:Lcom/airbnb/lottie/parser/moshi/O000000o;

    const-string v0, "fc"

    const-string v1, "sc"

    const-string v2, "sw"

    const-string v3, "t"

    filled-new-array {v0, v1, v2, v3}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O00000Oo;->O0O0O:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000OoO;
    .locals 3

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    const/4 v0, 0x0

    move-object v1, v0

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_1

    sget-object v2, Lcom/airbnb/lottie/O00000o0/O00000Oo;->O0O0O0o:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v2}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v2

    if-eqz v2, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_0
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000Oo;->O0000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000OoO;

    move-result-object v1

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    if-nez v1, :cond_2

    new-instance p0, Lcom/airbnb/lottie/model/O000000o/O0000OoO;

    invoke-direct {p0, v0, v0, v0, v0}, Lcom/airbnb/lottie/model/O000000o/O0000OoO;-><init>(Lcom/airbnb/lottie/model/O000000o/O000000o;Lcom/airbnb/lottie/model/O000000o/O000000o;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;)V

    return-object p0

    :cond_2
    return-object v1
.end method

.method private static O0000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000OoO;
    .locals 6

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    const/4 v0, 0x0

    move-object v1, v0

    move-object v2, v1

    move-object v3, v2

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v4

    if-eqz v4, :cond_4

    sget-object v4, Lcom/airbnb/lottie/O00000o0/O00000Oo;->O0O0O:Lcom/airbnb/lottie/parser/moshi/O000000o;

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
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oO(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v3

    goto :goto_0

    :cond_1
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oO(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v2

    goto :goto_0

    :cond_2
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000o0(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O000000o;

    move-result-object v1

    goto :goto_0

    :cond_3
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000o0(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O000000o;

    move-result-object v0

    goto :goto_0

    :cond_4
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    new-instance p0, Lcom/airbnb/lottie/model/O000000o/O0000OoO;

    invoke-direct {p0, v0, v1, v2, v3}, Lcom/airbnb/lottie/model/O000000o/O0000OoO;-><init>(Lcom/airbnb/lottie/model/O000000o/O000000o;Lcom/airbnb/lottie/model/O000000o/O000000o;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;)V

    return-object p0
.end method

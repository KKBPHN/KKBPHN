.class public Lcom/airbnb/lottie/O00000o0/O000000o;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 3

    const-string v0, "k"

    const-string v1, "x"

    const-string v2, "y"

    filled-new-array {v0, v1, v2}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O000000o;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000oO;
    .locals 3

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object v1

    sget-object v2, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->BEGIN_ARRAY:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne v1, v2, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginArray()V

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O0000ooo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;

    move-result-object v1

    invoke-interface {v0, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endArray()V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000o0/O0000oO;->O00000o0(Ljava/util/List;)V

    goto :goto_1

    :cond_1
    new-instance p1, Lcom/airbnb/lottie/O00000oO/O000000o;

    invoke-static {}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00o0O0O()F

    move-result v1

    invoke-static {p0, v1}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;

    move-result-object p0

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/O00000oO/O000000o;-><init>(Ljava/lang/Object;)V

    invoke-interface {v0, p1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :goto_1
    new-instance p0, Lcom/airbnb/lottie/model/O000000o/O00000oO;

    invoke-direct {p0, v0}, Lcom/airbnb/lottie/model/O000000o/O00000oO;-><init>(Ljava/util/List;)V

    return-object p0
.end method

.method static O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000o00;
    .locals 7

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    const/4 v0, 0x1

    const/4 v1, 0x0

    const/4 v2, 0x0

    move-object v3, v1

    move-object v4, v3

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object v5

    sget-object v6, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->END_OBJECT:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-eq v5, v6, :cond_5

    sget-object v5, Lcom/airbnb/lottie/O00000o0/O000000o;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v5}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v5

    if-eqz v5, :cond_4

    if-eq v5, v0, :cond_2

    const/4 v6, 0x2

    if-eq v5, v6, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object v5

    sget-object v6, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->STRING:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne v5, v6, :cond_1

    goto :goto_1

    :cond_1
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oO(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v4

    goto :goto_0

    :cond_2
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object v5

    sget-object v6, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->STRING:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne v5, v6, :cond_3

    :goto_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    move v2, v0

    goto :goto_0

    :cond_3
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oO(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v3

    goto :goto_0

    :cond_4
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O000000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000oO;

    move-result-object v1

    goto :goto_0

    :cond_5
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    if-eqz v2, :cond_6

    const-string p0, "Lottie doesn\'t support expressions."

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O0000o0O;->O0000oo(Ljava/lang/String;)V

    :cond_6
    if-eqz v1, :cond_7

    return-object v1

    :cond_7
    new-instance p0, Lcom/airbnb/lottie/model/O000000o/O0000Oo0;

    invoke-direct {p0, v3, v4}, Lcom/airbnb/lottie/model/O000000o/O0000Oo0;-><init>(Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;)V

    return-object p0
.end method

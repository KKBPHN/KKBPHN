.class Lcom/airbnb/lottie/O00000o0/O0000oO;
.super Ljava/lang/Object;
.source ""


# static fields
.field static NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    const-string v0, "k"

    filled-new-array {v0}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000oO;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;FLcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;
    .locals 4

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object v1

    sget-object v2, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->STRING:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne v1, v2, :cond_0

    const-string p0, "Lottie doesn\'t support expressions."

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O0000o0O;->O0000oo(Ljava/lang/String;)V

    return-object v0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_5

    sget-object v1, Lcom/airbnb/lottie/O00000o0/O0000oO;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v1

    if-eqz v1, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object v1

    sget-object v2, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->BEGIN_ARRAY:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    const/4 v3, 0x0

    if-ne v1, v2, :cond_4

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginArray()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object v1

    sget-object v2, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->NUMBER:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne v1, v2, :cond_2

    invoke-static {p0, p1, p2, p3, v3}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;FLcom/airbnb/lottie/O00000o0/O000OO;Z)Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object v1

    invoke-interface {v0, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto :goto_2

    :cond_2
    :goto_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_3

    const/4 v1, 0x1

    invoke-static {p0, p1, p2, p3, v1}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;FLcom/airbnb/lottie/O00000o0/O000OO;Z)Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object v1

    invoke-interface {v0, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto :goto_1

    :cond_3
    :goto_2
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endArray()V

    goto :goto_0

    :cond_4
    invoke-static {p0, p1, p2, p3, v3}, Lcom/airbnb/lottie/O00000o0/O0000oO0;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;FLcom/airbnb/lottie/O00000o0/O000OO;Z)Lcom/airbnb/lottie/O00000oO/O000000o;

    move-result-object v1

    invoke-interface {v0, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto :goto_0

    :cond_5
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000o0/O0000oO;->O00000o0(Ljava/util/List;)V

    return-object v0
.end method

.method public static O00000o0(Ljava/util/List;)V
    .locals 5

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v0

    const/4 v1, 0x0

    :cond_0
    :goto_0
    const/4 v2, 0x1

    add-int/lit8 v3, v0, -0x1

    if-ge v1, v3, :cond_1

    invoke-interface {p0, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/airbnb/lottie/O00000oO/O000000o;

    add-int/lit8 v1, v1, 0x1

    invoke-interface {p0, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lcom/airbnb/lottie/O00000oO/O000000o;

    iget v4, v3, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0O:F

    invoke-static {v4}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v4

    iput-object v4, v2, Lcom/airbnb/lottie/O00000oO/O000000o;->O000oO0o:Ljava/lang/Float;

    iget-object v4, v2, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    if-nez v4, :cond_0

    iget-object v3, v3, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    if-eqz v3, :cond_0

    iput-object v3, v2, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    instance-of v3, v2, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;

    if-eqz v3, :cond_0

    check-cast v2, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o0;->O00o0OOO()V

    goto :goto_0

    :cond_1
    invoke-interface {p0, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O00000oO/O000000o;

    iget-object v1, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    if-eqz v1, :cond_2

    iget-object v1, v0, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    if-nez v1, :cond_3

    :cond_2
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v1

    if-le v1, v2, :cond_3

    invoke-interface {p0, v0}, Ljava/util/List;->remove(Ljava/lang/Object;)Z

    :cond_3
    return-void
.end method

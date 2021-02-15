.class Lcom/airbnb/lottie/O00000o0/O0000Oo;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

.field private static final O0O0OO:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 6

    const-string v0, "ch"

    const-string v1, "size"

    const-string v2, "w"

    const-string v3, "style"

    const-string v4, "fFamily"

    const-string v5, "data"

    filled-new-array/range {v0 .. v5}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000Oo;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    const-string v0, "shapes"

    filled-new-array {v0}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000Oo;->O0O0OO:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O00000o;
    .locals 11

    new-instance v1, Ljava/util/ArrayList;

    invoke-direct {v1}, Ljava/util/ArrayList;-><init>()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    const/4 v0, 0x0

    const-wide/16 v2, 0x0

    const/4 v4, 0x0

    move-object v9, v0

    move-object v10, v9

    move-wide v5, v2

    move-wide v7, v5

    move v2, v4

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_9

    sget-object v0, Lcom/airbnb/lottie/O00000o0/O0000Oo;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v0

    if-eqz v0, :cond_8

    const/4 v3, 0x1

    if-eq v0, v3, :cond_7

    const/4 v3, 0x2

    if-eq v0, v3, :cond_6

    const/4 v3, 0x3

    if-eq v0, v3, :cond_5

    const/4 v3, 0x4

    if-eq v0, v3, :cond_4

    const/4 v3, 0x5

    if-eq v0, v3, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    :goto_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_3

    sget-object v0, Lcom/airbnb/lottie/O00000o0/O0000Oo;->O0O0OO:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v0

    if-eqz v0, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_1

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginArray()V

    :goto_2
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O0000O0o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/content/O00000Oo;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/model/content/O0000o00;

    invoke-interface {v1, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto :goto_2

    :cond_2
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endArray()V

    goto :goto_1

    :cond_3
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    goto :goto_0

    :cond_4
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v10

    goto :goto_0

    :cond_5
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v9

    goto :goto_0

    :cond_6
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v7

    goto :goto_0

    :cond_7
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v5

    goto :goto_0

    :cond_8
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v4}, Ljava/lang/String;->charAt(I)C

    move-result v2

    goto :goto_0

    :cond_9
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    new-instance p0, Lcom/airbnb/lottie/model/O00000o;

    move-object v0, p0

    move-wide v3, v5

    move-wide v5, v7

    move-object v7, v9

    move-object v8, v10

    invoke-direct/range {v0 .. v8}, Lcom/airbnb/lottie/model/O00000o;-><init>(Ljava/util/List;CDDLjava/lang/String;Ljava/lang/String;)V

    return-object p0
.end method

.class Lcom/airbnb/lottie/O00000o0/O0000oo;
.super Ljava/lang/Object;
.source ""


# direct methods
.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/content/O00000oo;
    .locals 12

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    const/4 v0, 0x0

    const/4 v1, 0x0

    move v4, v0

    move-object v2, v1

    move-object v3, v2

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v5

    if-eqz v5, :cond_12

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/String;->hashCode()I

    move-result v6

    const/16 v7, 0x6f

    const/4 v8, -0x1

    const/4 v9, 0x3

    const/4 v10, 0x2

    const/4 v11, 0x1

    if-eq v6, v7, :cond_3

    const/16 v7, 0xe04

    if-eq v6, v7, :cond_2

    const v7, 0x197f1

    if-eq v6, v7, :cond_1

    const v7, 0x3339a3

    if-eq v6, v7, :cond_0

    goto :goto_1

    :cond_0
    const-string v6, "mode"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_4

    move v6, v0

    goto :goto_2

    :cond_1
    const-string v6, "inv"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_4

    move v6, v9

    goto :goto_2

    :cond_2
    const-string v6, "pt"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_4

    move v6, v11

    goto :goto_2

    :cond_3
    const-string v6, "o"

    invoke-virtual {v5, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_4

    move v6, v10

    goto :goto_2

    :cond_4
    :goto_1
    move v6, v8

    :goto_2
    if-eqz v6, :cond_8

    if-eq v6, v11, :cond_7

    if-eq v6, v10, :cond_6

    if-eq v6, v9, :cond_5

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_5
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v4

    goto :goto_0

    :cond_6
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object v3

    goto :goto_0

    :cond_7
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O0000Oo0(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000OOo;

    move-result-object v2

    goto :goto_0

    :cond_8
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/String;->hashCode()I

    move-result v6

    const/16 v7, 0x61

    if-eq v6, v7, :cond_c

    const/16 v7, 0x69

    if-eq v6, v7, :cond_b

    const/16 v7, 0x6e

    if-eq v6, v7, :cond_a

    const/16 v7, 0x73

    if-eq v6, v7, :cond_9

    goto :goto_3

    :cond_9
    const-string v6, "s"

    invoke-virtual {v1, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_d

    move v8, v11

    goto :goto_3

    :cond_a
    const-string v6, "n"

    invoke-virtual {v1, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_d

    move v8, v10

    goto :goto_3

    :cond_b
    const-string v6, "i"

    invoke-virtual {v1, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_d

    move v8, v9

    goto :goto_3

    :cond_c
    const-string v6, "a"

    invoke-virtual {v1, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_d

    move v8, v0

    :cond_d
    :goto_3
    if-eqz v8, :cond_e

    if-eq v8, v11, :cond_11

    if-eq v8, v10, :cond_10

    if-eq v8, v9, :cond_f

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "Unknown mask mode "

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v5, ". Defaulting to Add."

    invoke-virtual {v1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Lcom/airbnb/lottie/O00000o/O00000o;->warning(Ljava/lang/String;)V

    :cond_e
    sget-object v1, Lcom/airbnb/lottie/model/content/Mask$MaskMode;->O0ooOo0:Lcom/airbnb/lottie/model/content/Mask$MaskMode;

    goto/16 :goto_0

    :cond_f
    const-string v1, "Animation contains intersect masks. They are not supported but will be treated like add masks."

    invoke-virtual {p1, v1}, Lcom/airbnb/lottie/O0000o0O;->O0000oo(Ljava/lang/String;)V

    sget-object v1, Lcom/airbnb/lottie/model/content/Mask$MaskMode;->O0ooOoo:Lcom/airbnb/lottie/model/content/Mask$MaskMode;

    goto/16 :goto_0

    :cond_10
    sget-object v1, Lcom/airbnb/lottie/model/content/Mask$MaskMode;->O0ooo00:Lcom/airbnb/lottie/model/content/Mask$MaskMode;

    goto/16 :goto_0

    :cond_11
    sget-object v1, Lcom/airbnb/lottie/model/content/Mask$MaskMode;->O0ooOoO:Lcom/airbnb/lottie/model/content/Mask$MaskMode;

    goto/16 :goto_0

    :cond_12
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    new-instance p0, Lcom/airbnb/lottie/model/content/O00000oo;

    invoke-direct {p0, v1, v2, v3, v4}, Lcom/airbnb/lottie/model/content/O00000oo;-><init>(Lcom/airbnb/lottie/model/content/Mask$MaskMode;Lcom/airbnb/lottie/model/O000000o/O0000OOo;Lcom/airbnb/lottie/model/O000000o/O00000o;Z)V

    return-object p0
.end method

.class public Lcom/airbnb/lottie/O00000o0/O0000OOo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O00000o0/O000OO;


# static fields
.field public static final INSTANCE:Lcom/airbnb/lottie/O00000o0/O0000OOo;

.field private static final NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 12

    new-instance v0, Lcom/airbnb/lottie/O00000o0/O0000OOo;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000o0/O0000OOo;-><init>()V

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000OOo;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O0000OOo;

    const-string v1, "t"

    const-string v2, "f"

    const-string v3, "s"

    const-string v4, "j"

    const-string v5, "tr"

    const-string v6, "lh"

    const-string v7, "ls"

    const-string v8, "fc"

    const-string v9, "sc"

    const-string v10, "sw"

    const-string v11, "of"

    filled-new-array/range {v1 .. v11}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000OOo;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Lcom/airbnb/lottie/model/O00000Oo;
    .locals 17

    sget-object v0, Lcom/airbnb/lottie/model/DocumentData$Justification;->CENTER:Lcom/airbnb/lottie/model/DocumentData$Justification;

    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    const/4 v1, 0x0

    const/4 v2, 0x0

    const/4 v3, 0x0

    const/4 v4, 0x1

    move-object v9, v0

    move-object v6, v1

    move-object v7, v6

    move v10, v2

    move v13, v10

    move v14, v13

    move v8, v3

    move v11, v8

    move v12, v11

    move v15, v12

    move/from16 v16, v4

    :goto_0
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_2

    sget-object v0, Lcom/airbnb/lottie/O00000o0/O0000OOo;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-object/from16 v1, p1

    invoke-virtual {v1, v0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v0

    packed-switch v0, :pswitch_data_0

    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :pswitch_0
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v16

    goto :goto_0

    :pswitch_1
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v2

    double-to-float v15, v2

    goto :goto_0

    :pswitch_2
    invoke-static/range {p1 .. p1}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000o0(Lcom/airbnb/lottie/parser/moshi/O00000Oo;)I

    move-result v14

    goto :goto_0

    :pswitch_3
    invoke-static/range {p1 .. p1}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000o0(Lcom/airbnb/lottie/parser/moshi/O00000Oo;)I

    move-result v13

    goto :goto_0

    :pswitch_4
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v2

    double-to-float v12, v2

    goto :goto_0

    :pswitch_5
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v2

    double-to-float v11, v2

    goto :goto_0

    :pswitch_6
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result v10

    goto :goto_0

    :pswitch_7
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result v0

    sget-object v2, Lcom/airbnb/lottie/model/DocumentData$Justification;->CENTER:Lcom/airbnb/lottie/model/DocumentData$Justification;

    invoke-virtual {v2}, Ljava/lang/Enum;->ordinal()I

    move-result v2

    if-gt v0, v2, :cond_1

    if-gez v0, :cond_0

    goto :goto_1

    :cond_0
    invoke-static {}, Lcom/airbnb/lottie/model/DocumentData$Justification;->values()[Lcom/airbnb/lottie/model/DocumentData$Justification;

    move-result-object v2

    aget-object v9, v2, v0

    goto :goto_0

    :cond_1
    :goto_1
    sget-object v9, Lcom/airbnb/lottie/model/DocumentData$Justification;->CENTER:Lcom/airbnb/lottie/model/DocumentData$Justification;

    goto :goto_0

    :pswitch_8
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v2

    double-to-float v8, v2

    goto :goto_0

    :pswitch_9
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v7

    goto :goto_0

    :pswitch_a
    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v6

    goto :goto_0

    :cond_2
    move-object/from16 v1, p1

    invoke-virtual/range {p1 .. p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    new-instance v0, Lcom/airbnb/lottie/model/O00000Oo;

    move-object v5, v0

    invoke-direct/range {v5 .. v16}, Lcom/airbnb/lottie/model/O00000Oo;-><init>(Ljava/lang/String;Ljava/lang/String;FLcom/airbnb/lottie/model/DocumentData$Justification;IFFIIFZ)V

    return-object v0

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method public bridge synthetic O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O00000o0/O0000OOo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Lcom/airbnb/lottie/model/O00000Oo;

    move-result-object p0

    return-object p0
.end method

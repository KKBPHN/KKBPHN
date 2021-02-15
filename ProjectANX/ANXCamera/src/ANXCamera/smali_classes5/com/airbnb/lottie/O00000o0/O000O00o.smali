.class Lcom/airbnb/lottie/O00000o0/O000O00o;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 10

    const-string v0, "nm"

    const-string v1, "sy"

    const-string v2, "pt"

    const-string v3, "p"

    const-string v4, "r"

    const-string v5, "or"

    const-string v6, "os"

    const-string v7, "ir"

    const-string v8, "is"

    const-string v9, "hd"

    filled-new-array/range {v0 .. v9}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O000O00o;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/content/O0000OOo;
    .locals 13

    const/4 v0, 0x0

    const/4 v1, 0x0

    move v12, v0

    move-object v3, v1

    move-object v4, v3

    move-object v5, v4

    move-object v6, v5

    move-object v7, v6

    move-object v8, v7

    move-object v9, v8

    move-object v10, v9

    move-object v11, v10

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_0

    sget-object v1, Lcom/airbnb/lottie/O00000o0/O000O00o;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v1

    packed-switch v1, :pswitch_data_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :pswitch_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v12

    goto :goto_0

    :pswitch_1
    invoke-static {p0, p1, v0}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Z)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v10

    goto :goto_0

    :pswitch_2
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oO(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v8

    goto :goto_0

    :pswitch_3
    invoke-static {p0, p1, v0}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Z)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v11

    goto :goto_0

    :pswitch_4
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oO(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v9

    goto :goto_0

    :pswitch_5
    invoke-static {p0, p1, v0}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Z)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v7

    goto :goto_0

    :pswitch_6
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O000000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000o00;

    move-result-object v6

    goto :goto_0

    :pswitch_7
    invoke-static {p0, p1, v0}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Z)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v5

    goto :goto_0

    :pswitch_8
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result v1

    invoke-static {v1}, Lcom/airbnb/lottie/model/content/PolystarShape$Type;->O000OOo(I)Lcom/airbnb/lottie/model/content/PolystarShape$Type;

    move-result-object v4

    goto :goto_0

    :pswitch_9
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v3

    goto :goto_0

    :cond_0
    new-instance p0, Lcom/airbnb/lottie/model/content/O0000OOo;

    move-object v2, p0

    invoke-direct/range {v2 .. v12}, Lcom/airbnb/lottie/model/content/O0000OOo;-><init>(Ljava/lang/String;Lcom/airbnb/lottie/model/content/PolystarShape$Type;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O0000o00;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Z)V

    return-object p0

    nop

    :pswitch_data_0
    .packed-switch 0x0
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

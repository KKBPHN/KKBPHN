.class Lcom/airbnb/lottie/O00000o0/O0000o00;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

.field private static final O0O0OOo:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 8

    const-string v0, "nm"

    const-string v1, "g"

    const-string v2, "o"

    const-string v3, "t"

    const-string v4, "s"

    const-string v5, "e"

    const-string v6, "r"

    const-string v7, "hd"

    filled-new-array/range {v0 .. v7}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000o00;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    const-string v0, "p"

    const-string v1, "k"

    filled-new-array {v0, v1}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000o00;->O0O0OOo:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/content/O00000o;
    .locals 14

    sget-object v0, Landroid/graphics/Path$FillType;->WINDING:Landroid/graphics/Path$FillType;

    const/4 v1, 0x0

    const/4 v2, 0x0

    move-object v6, v0

    move-object v4, v1

    move-object v5, v4

    move-object v7, v5

    move-object v8, v7

    move-object v9, v8

    move-object v10, v9

    move v13, v2

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_5

    sget-object v0, Lcom/airbnb/lottie/O00000o0/O0000o00;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v0

    const/4 v1, 0x1

    packed-switch v0, :pswitch_data_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :pswitch_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v13

    goto :goto_0

    :pswitch_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result v0

    if-ne v0, v1, :cond_0

    sget-object v0, Landroid/graphics/Path$FillType;->WINDING:Landroid/graphics/Path$FillType;

    goto :goto_1

    :cond_0
    sget-object v0, Landroid/graphics/Path$FillType;->EVEN_ODD:Landroid/graphics/Path$FillType;

    :goto_1
    move-object v6, v0

    goto :goto_0

    :pswitch_2
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O0000O0o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000oo;

    move-result-object v10

    goto :goto_0

    :pswitch_3
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O0000O0o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000oo;

    move-result-object v9

    goto :goto_0

    :pswitch_4
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result v0

    if-ne v0, v1, :cond_1

    sget-object v0, Lcom/airbnb/lottie/model/content/GradientType;->LINEAR:Lcom/airbnb/lottie/model/content/GradientType;

    goto :goto_2

    :cond_1
    sget-object v0, Lcom/airbnb/lottie/model/content/GradientType;->O0ooOOo:Lcom/airbnb/lottie/model/content/GradientType;

    :goto_2
    move-object v5, v0

    goto :goto_0

    :pswitch_5
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object v8

    goto :goto_0

    :pswitch_6
    const/4 v0, -0x1

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginObject()V

    :goto_3
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_4

    sget-object v2, Lcom/airbnb/lottie/O00000o0/O0000o00;->O0O0OOo:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v2}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v2

    if-eqz v2, :cond_3

    if-eq v2, v1, :cond_2

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_3

    :cond_2
    invoke-static {p0, p1, v0}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;I)Lcom/airbnb/lottie/model/O000000o/O00000o0;

    move-result-object v7

    goto :goto_3

    :cond_3
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result v0

    goto :goto_3

    :cond_4
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endObject()V

    goto :goto_0

    :pswitch_7
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v4

    goto :goto_0

    :cond_5
    new-instance p0, Lcom/airbnb/lottie/model/content/O00000o;

    const/4 v11, 0x0

    const/4 v12, 0x0

    move-object v3, p0

    invoke-direct/range {v3 .. v13}, Lcom/airbnb/lottie/model/content/O00000o;-><init>(Ljava/lang/String;Lcom/airbnb/lottie/model/content/GradientType;Landroid/graphics/Path$FillType;Lcom/airbnb/lottie/model/O000000o/O00000o0;Lcom/airbnb/lottie/model/O000000o/O00000o;Lcom/airbnb/lottie/model/O000000o/O00000oo;Lcom/airbnb/lottie/model/O000000o/O00000oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Z)V

    return-object p0

    :pswitch_data_0
    .packed-switch 0x0
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

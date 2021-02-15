.class Lcom/airbnb/lottie/O00000o0/O000O0o;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;


# direct methods
.method static constructor <clinit>()V
    .locals 6

    const-string v0, "nm"

    const-string v1, "c"

    const-string v2, "o"

    const-string v3, "fillEnabled"

    const-string v4, "r"

    const-string v5, "hd"

    filled-new-array/range {v0 .. v5}, [Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/parser/moshi/O000000o;->of([Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/O000000o;

    move-result-object v0

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O000O0o;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/content/O0000Ooo;
    .locals 9

    const/4 v0, 0x0

    const/4 v1, 0x1

    const/4 v2, 0x0

    move v4, v0

    move v8, v4

    move v0, v1

    move-object v3, v2

    move-object v6, v3

    move-object v7, v6

    :goto_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_6

    sget-object v2, Lcom/airbnb/lottie/O00000o0/O000O0o;->NAMES:Lcom/airbnb/lottie/parser/moshi/O000000o;

    invoke-virtual {p0, v2}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I

    move-result v2

    if-eqz v2, :cond_5

    if-eq v2, v1, :cond_4

    const/4 v5, 0x2

    if-eq v2, v5, :cond_3

    const/4 v5, 0x3

    if-eq v2, v5, :cond_2

    const/4 v5, 0x4

    if-eq v2, v5, :cond_1

    const/4 v5, 0x5

    if-eq v2, v5, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00o0O0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v8

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextInt()I

    move-result v0

    goto :goto_0

    :cond_2
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextBoolean()Z

    move-result v4

    goto :goto_0

    :cond_3
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object v7

    goto :goto_0

    :cond_4
    invoke-static {p0, p1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O00000o0(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O000000o;

    move-result-object v6

    goto :goto_0

    :cond_5
    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextString()Ljava/lang/String;

    move-result-object v3

    goto :goto_0

    :cond_6
    if-ne v0, v1, :cond_7

    sget-object p0, Landroid/graphics/Path$FillType;->WINDING:Landroid/graphics/Path$FillType;

    goto :goto_1

    :cond_7
    sget-object p0, Landroid/graphics/Path$FillType;->EVEN_ODD:Landroid/graphics/Path$FillType;

    :goto_1
    move-object v5, p0

    new-instance p0, Lcom/airbnb/lottie/model/content/O0000Ooo;

    move-object v2, p0

    invoke-direct/range {v2 .. v8}, Lcom/airbnb/lottie/model/content/O0000Ooo;-><init>(Ljava/lang/String;ZLandroid/graphics/Path$FillType;Lcom/airbnb/lottie/model/O000000o/O000000o;Lcom/airbnb/lottie/model/O000000o/O00000o;Z)V

    return-object p0
.end method

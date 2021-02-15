.class public Lcom/airbnb/lottie/O00000o0/O00oOoOo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O00000o0/O000OO;


# static fields
.field public static final INSTANCE:Lcom/airbnb/lottie/O00000o0/O00oOoOo;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O00000o0/O00oOoOo;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000o0/O00oOoOo;-><init>()V

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O00oOoOo;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O00oOoOo;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Lcom/airbnb/lottie/O00000oO/O0000OoO;
    .locals 3

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object p0

    sget-object v0, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->BEGIN_ARRAY:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    if-eqz p0, :cond_1

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->beginArray()V

    :cond_1
    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v0

    double-to-float v0, v0

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v1

    double-to-float v1, v1

    :goto_1
    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_2

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_1

    :cond_2
    if-eqz p0, :cond_3

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->endArray()V

    :cond_3
    new-instance p0, Lcom/airbnb/lottie/O00000oO/O0000OoO;

    const/high16 p1, 0x42c80000    # 100.0f

    div-float/2addr v0, p1

    mul-float/2addr v0, p2

    div-float/2addr v1, p1

    mul-float/2addr v1, p2

    invoke-direct {p0, v0, v1}, Lcom/airbnb/lottie/O00000oO/O0000OoO;-><init>(FF)V

    return-object p0
.end method

.method public bridge synthetic O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O00000o0/O00oOoOo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Lcom/airbnb/lottie/O00000oO/O0000OoO;

    move-result-object p0

    return-object p0
.end method

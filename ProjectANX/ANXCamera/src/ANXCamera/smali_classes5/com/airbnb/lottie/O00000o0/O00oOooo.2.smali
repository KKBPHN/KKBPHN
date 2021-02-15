.class public Lcom/airbnb/lottie/O00000o0/O00oOooo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O00000o0/O000OO;


# static fields
.field public static final INSTANCE:Lcom/airbnb/lottie/O00000o0/O00oOooo;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O00000o0/O00oOooo;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000o0/O00oOooo;-><init>()V

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O00oOooo;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O00oOooo;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;
    .locals 3

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    move-result-object p0

    sget-object v0, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->BEGIN_ARRAY:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne p0, v0, :cond_0

    invoke-static {p1, p2}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;

    move-result-object p0

    return-object p0

    :cond_0
    sget-object v0, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->BEGIN_OBJECT:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne p0, v0, :cond_1

    invoke-static {p1, p2}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000Oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;

    move-result-object p0

    return-object p0

    :cond_1
    sget-object v0, Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;->NUMBER:Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;

    if-ne p0, v0, :cond_3

    new-instance p0, Landroid/graphics/PointF;

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v0

    double-to-float v0, v0

    mul-float/2addr v0, p2

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->nextDouble()D

    move-result-wide v1

    double-to-float v1, v1

    mul-float/2addr v1, p2

    invoke-direct {p0, v0, v1}, Landroid/graphics/PointF;-><init>(FF)V

    :goto_0
    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->hasNext()Z

    move-result p2

    if-eqz p2, :cond_2

    invoke-virtual {p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->skipValue()V

    goto :goto_0

    :cond_2
    return-object p0

    :cond_3
    new-instance p1, Ljava/lang/IllegalArgumentException;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "Cannot convert json to point. Next token is "

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, p0}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p1
.end method

.method public bridge synthetic O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O00000o0/O00oOooo;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Landroid/graphics/PointF;

    move-result-object p0

    return-object p0
.end method

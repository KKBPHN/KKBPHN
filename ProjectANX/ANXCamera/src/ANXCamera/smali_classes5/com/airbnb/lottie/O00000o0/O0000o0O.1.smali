.class public Lcom/airbnb/lottie/O00000o0/O0000o0O;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O00000o0/O000OO;


# static fields
.field public static final INSTANCE:Lcom/airbnb/lottie/O00000o0/O0000o0O;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O00000o0/O0000o0O;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000o0/O0000o0O;-><init>()V

    sput-object v0, Lcom/airbnb/lottie/O00000o0/O0000o0O;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O0000o0O;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Integer;
    .locals 0

    invoke-static {p1}, Lcom/airbnb/lottie/O00000o0/O0000o;->O00000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;)F

    move-result p0

    mul-float/2addr p0, p2

    invoke-static {p0}, Ljava/lang/Math;->round(F)I

    move-result p0

    invoke-static {p0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O00000o0/O0000o0O;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;F)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

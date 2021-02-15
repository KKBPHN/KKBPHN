.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo;
.super Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Ooo;
.source ""


# instance fields
.field private final O00o00Oo:Lcom/airbnb/lottie/model/content/O00000o0;


# direct methods
.method public constructor <init>(Ljava/util/List;)V
    .locals 2

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Ooo;-><init>(Ljava/util/List;)V

    const/4 v0, 0x0

    invoke-interface {p1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lcom/airbnb/lottie/O00000oO/O000000o;

    iget-object p1, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    check-cast p1, Lcom/airbnb/lottie/model/content/O00000o0;

    if-nez p1, :cond_0

    goto :goto_0

    :cond_0
    invoke-virtual {p1}, Lcom/airbnb/lottie/model/content/O00000o0;->getSize()I

    move-result v0

    :goto_0
    new-instance p1, Lcom/airbnb/lottie/model/content/O00000o0;

    new-array v1, v0, [F

    new-array v0, v0, [I

    invoke-direct {p1, v1, v0}, Lcom/airbnb/lottie/model/content/O00000o0;-><init>([F[I)V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo;->O00o00Oo:Lcom/airbnb/lottie/model/content/O00000o0;

    return-void
.end method


# virtual methods
.method O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Lcom/airbnb/lottie/model/content/O00000o0;
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo;->O00o00Oo:Lcom/airbnb/lottie/model/content/O00000o0;

    iget-object v1, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    check-cast v1, Lcom/airbnb/lottie/model/content/O00000o0;

    iget-object p1, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    check-cast p1, Lcom/airbnb/lottie/model/content/O00000o0;

    invoke-virtual {v0, v1, p1, p2}, Lcom/airbnb/lottie/model/content/O00000o0;->O000000o(Lcom/airbnb/lottie/model/content/O00000o0;Lcom/airbnb/lottie/model/content/O00000o0;F)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo;->O00o00Oo:Lcom/airbnb/lottie/model/content/O00000o0;

    return-object p0
.end method

.method bridge synthetic O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo;->O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Lcom/airbnb/lottie/model/content/O00000o0;

    move-result-object p0

    return-object p0
.end method

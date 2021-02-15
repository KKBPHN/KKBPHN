.class public Lcom/airbnb/lottie/model/O000000o/O0000O0o;
.super Lcom/airbnb/lottie/model/O000000o/O0000o0;
.source ""


# direct methods
.method constructor <init>()V
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/O00000oO/O0000OoO;

    const/high16 v1, 0x3f800000    # 1.0f

    invoke-direct {v0, v1, v1}, Lcom/airbnb/lottie/O00000oO/O0000OoO;-><init>(FF)V

    invoke-direct {p0, v0}, Lcom/airbnb/lottie/model/O000000o/O0000O0o;-><init>(Lcom/airbnb/lottie/O00000oO/O0000OoO;)V

    return-void
.end method

.method public constructor <init>(Lcom/airbnb/lottie/O00000oO/O0000OoO;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/model/O000000o/O0000o0;-><init>(Ljava/lang/Object;)V

    return-void
.end method

.method public constructor <init>(Ljava/util/List;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/model/O000000o/O0000o0;-><init>(Ljava/util/List;)V

    return-void
.end method


# virtual methods
.method public O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o;

    iget-object p0, p0, Lcom/airbnb/lottie/model/O000000o/O0000o0;->O00Oooo:Ljava/util/List;

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method public bridge synthetic getKeyframes()Ljava/util/List;
    .locals 0

    invoke-super {p0}, Lcom/airbnb/lottie/model/O000000o/O0000o0;->getKeyframes()Ljava/util/List;

    move-result-object p0

    return-object p0
.end method

.method public bridge synthetic isStatic()Z
    .locals 0

    invoke-super {p0}, Lcom/airbnb/lottie/model/O000000o/O0000o0;->isStatic()Z

    move-result p0

    return p0
.end method

.method public bridge synthetic toString()Ljava/lang/String;
    .locals 0

    invoke-super {p0}, Lcom/airbnb/lottie/model/O000000o/O0000o0;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.class public Lcom/airbnb/lottie/model/O000000o/O0000Oo0;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/O000000o/O0000o00;


# instance fields
.field private final O00oO0O:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

.field private final O00ooO00:Lcom/airbnb/lottie/model/O000000o/O00000Oo;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/O000000o/O0000Oo0;->O00oO0O:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p2, p0, Lcom/airbnb/lottie/model/O000000o/O0000Oo0;->O00ooO00:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-void
.end method


# virtual methods
.method public O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;

    iget-object v1, p0, Lcom/airbnb/lottie/model/O000000o/O0000Oo0;->O00oO0O:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    invoke-virtual {v1}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v1

    iget-object p0, p0, Lcom/airbnb/lottie/model/O000000o/O0000Oo0;->O00ooO00:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p0

    invoke-direct {v0, v1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO;-><init>(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    return-object v0
.end method

.method public getKeyframes()Ljava/util/List;
    .locals 1

    new-instance p0, Ljava/lang/UnsupportedOperationException;

    const-string v0, "Cannot call getKeyframes on AnimatableSplitDimensionPathValue."

    invoke-direct {p0, v0}, Ljava/lang/UnsupportedOperationException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public isStatic()Z
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/model/O000000o/O0000Oo0;->O00oO0O:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->isStatic()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/model/O000000o/O0000Oo0;->O00ooO00:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->isStatic()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

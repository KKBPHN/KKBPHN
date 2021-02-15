.class public Lcom/airbnb/lottie/model/content/O0000Oo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final O00Ooo0:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

.field private final O00Ooo0O:Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;


# direct methods
.method public constructor <init>(Ljava/lang/String;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O0000Ooo;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->name:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->O00Ooo0:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p3, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p4, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->O00Ooo0O:Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

    iput-boolean p5, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 1
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;

    invoke-direct {v0, p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000Oo;)V

    return-object v0
.end method

.method public getCopies()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->O00Ooo0:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getOffset()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public getTransform()Lcom/airbnb/lottie/model/O000000o/O0000Ooo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->O00Ooo0O:Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo;->hidden:Z

    return p0
.end method

.class public Lcom/airbnb/lottie/model/content/O000000o;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final O00oO0Oo:Z

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final position:Lcom/airbnb/lottie/model/O000000o/O0000o00;

.field private final size:Lcom/airbnb/lottie/model/O000000o/O00000oo;


# direct methods
.method public constructor <init>(Ljava/lang/String;Lcom/airbnb/lottie/model/O000000o/O0000o00;Lcom/airbnb/lottie/model/O000000o/O00000oo;ZZ)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O000000o;->name:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/model/content/O000000o;->position:Lcom/airbnb/lottie/model/O000000o/O0000o00;

    iput-object p3, p0, Lcom/airbnb/lottie/model/content/O000000o;->size:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    iput-boolean p4, p0, Lcom/airbnb/lottie/model/content/O000000o;->O00oO0Oo:Z

    iput-boolean p5, p0, Lcom/airbnb/lottie/model/content/O000000o;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OOo;

    invoke-direct {v0, p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000OOo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O000000o;)V

    return-object v0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O000000o;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getPosition()Lcom/airbnb/lottie/model/O000000o/O0000o00;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O000000o;->position:Lcom/airbnb/lottie/model/O000000o/O0000o00;

    return-object p0
.end method

.method public getSize()Lcom/airbnb/lottie/model/O000000o/O00000oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O000000o;->size:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O000000o;->hidden:Z

    return p0
.end method

.method public isReversed()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O000000o;->O00oO0Oo:Z

    return p0
.end method

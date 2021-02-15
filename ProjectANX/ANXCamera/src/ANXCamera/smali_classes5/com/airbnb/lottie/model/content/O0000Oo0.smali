.class public Lcom/airbnb/lottie/model/content/O0000Oo0;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final cornerRadius:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final position:Lcom/airbnb/lottie/model/O000000o/O0000o00;

.field private final size:Lcom/airbnb/lottie/model/O000000o/O00000oo;


# direct methods
.method public constructor <init>(Ljava/lang/String;Lcom/airbnb/lottie/model/O000000o/O0000o00;Lcom/airbnb/lottie/model/O000000o/O00000oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->name:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->position:Lcom/airbnb/lottie/model/O000000o/O0000o00;

    iput-object p3, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->size:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    iput-object p4, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->cornerRadius:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-boolean p5, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;

    invoke-direct {v0, p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000oOO;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000Oo0;)V

    return-object v0
.end method

.method public getCornerRadius()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->cornerRadius:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getPosition()Lcom/airbnb/lottie/model/O000000o/O0000o00;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->position:Lcom/airbnb/lottie/model/O000000o/O0000o00;

    return-object p0
.end method

.method public getSize()Lcom/airbnb/lottie/model/O000000o/O00000oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->size:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->hidden:Z

    return p0
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "RectangleShape{position="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->position:Lcom/airbnb/lottie/model/O000000o/O0000o00;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v1, ", size="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Oo0;->size:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const/16 p0, 0x7d

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

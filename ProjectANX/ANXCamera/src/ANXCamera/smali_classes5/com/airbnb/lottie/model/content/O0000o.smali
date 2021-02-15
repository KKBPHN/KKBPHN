.class public Lcom/airbnb/lottie/model/content/O0000o;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final end:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

.field private final start:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

.field private final type:Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;


# direct methods
.method public constructor <init>(Ljava/lang/String;Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O0000o;->name:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/model/content/O0000o;->type:Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;

    iput-object p3, p0, Lcom/airbnb/lottie/model/content/O0000o;->start:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p4, p0, Lcom/airbnb/lottie/model/content/O0000o;->end:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p5, p0, Lcom/airbnb/lottie/model/content/O0000o;->offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-boolean p6, p0, Lcom/airbnb/lottie/model/content/O0000o;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 0

    new-instance p1, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;

    invoke-direct {p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;-><init>(Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000o;)V

    return-object p1
.end method

.method public getEnd()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o;->end:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getOffset()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o;->offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public getStart()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o;->start:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public getType()Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o;->type:Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O0000o;->hidden:Z

    return p0
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Trim Path: {start: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lcom/airbnb/lottie/model/content/O0000o;->start:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v1, ", end: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lcom/airbnb/lottie/model/content/O0000o;->end:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v1, ", offset: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o;->offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string p0, "}"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

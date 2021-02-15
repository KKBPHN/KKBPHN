.class public Lcom/airbnb/lottie/model/content/O0000o00;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final hidden:Z

.field private final items:Ljava/util/List;

.field private final name:Ljava/lang/String;


# direct methods
.method public constructor <init>(Ljava/lang/String;Ljava/util/List;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O0000o00;->name:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/model/content/O0000o00;->items:Ljava/util/List;

    iput-boolean p3, p0, Lcom/airbnb/lottie/model/content/O0000o00;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    invoke-direct {v0, p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000o00;)V

    return-object v0
.end method

.method public getItems()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o00;->items:Ljava/util/List;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o00;->name:Ljava/lang/String;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O0000o00;->hidden:Z

    return p0
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "ShapeGroup{name=\'"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lcom/airbnb/lottie/model/content/O0000o00;->name:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "\' Shapes: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o00;->items:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->toArray()[Ljava/lang/Object;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->toString([Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/16 p0, 0x7d

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

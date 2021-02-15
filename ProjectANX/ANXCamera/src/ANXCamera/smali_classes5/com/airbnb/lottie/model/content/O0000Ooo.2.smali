.class public Lcom/airbnb/lottie/model/content/O0000Ooo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final color:Lcom/airbnb/lottie/model/O000000o/O000000o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final fillEnabled:Z

.field private final fillType:Landroid/graphics/Path$FillType;

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field


# direct methods
.method public constructor <init>(Ljava/lang/String;ZLandroid/graphics/Path$FillType;Lcom/airbnb/lottie/model/O000000o/O000000o;Lcom/airbnb/lottie/model/O000000o/O00000o;Z)V
    .locals 0
    .param p4    # Lcom/airbnb/lottie/model/O000000o/O000000o;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param
    .param p5    # Lcom/airbnb/lottie/model/O000000o/O00000o;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->name:Ljava/lang/String;

    iput-boolean p2, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->fillEnabled:Z

    iput-object p3, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->fillType:Landroid/graphics/Path$FillType;

    iput-object p4, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->color:Lcom/airbnb/lottie/model/O000000o/O000000o;

    iput-object p5, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

    iput-boolean p6, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;

    invoke-direct {v0, p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo0;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000Ooo;)V

    return-object v0
.end method

.method public getColor()Lcom/airbnb/lottie/model/O000000o/O000000o;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->color:Lcom/airbnb/lottie/model/O000000o/O000000o;

    return-object p0
.end method

.method public getFillType()Landroid/graphics/Path$FillType;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->fillType:Landroid/graphics/Path$FillType;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->hidden:Z

    return p0
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "ShapeFill{color=, fillEnabled="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O0000Ooo;->fillEnabled:Z

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const/16 p0, 0x7d

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

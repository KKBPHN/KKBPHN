.class public Lcom/airbnb/lottie/model/O00000o;
.super Ljava/lang/Object;
.source ""


# annotations
.annotation build Landroidx/annotation/RestrictTo;
    value = {
        .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
    }
.end annotation


# instance fields
.field private final O00oO00O:Ljava/util/List;

.field private final O00oO0OO:C

.field private final fontFamily:Ljava/lang/String;

.field private final size:D

.field private final style:Ljava/lang/String;

.field private final width:D


# direct methods
.method public constructor <init>(Ljava/util/List;CDDLjava/lang/String;Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/O00000o;->O00oO00O:Ljava/util/List;

    iput-char p2, p0, Lcom/airbnb/lottie/model/O00000o;->O00oO0OO:C

    iput-wide p3, p0, Lcom/airbnb/lottie/model/O00000o;->size:D

    iput-wide p5, p0, Lcom/airbnb/lottie/model/O00000o;->width:D

    iput-object p7, p0, Lcom/airbnb/lottie/model/O00000o;->style:Ljava/lang/String;

    iput-object p8, p0, Lcom/airbnb/lottie/model/O00000o;->fontFamily:Ljava/lang/String;

    return-void
.end method

.method public static O000000o(CLjava/lang/String;Ljava/lang/String;)I
    .locals 1

    const/4 v0, 0x0

    add-int/2addr v0, p0

    mul-int/lit8 v0, v0, 0x1f

    invoke-virtual {p1}, Ljava/lang/String;->hashCode()I

    move-result p0

    add-int/2addr v0, p0

    mul-int/lit8 v0, v0, 0x1f

    invoke-virtual {p2}, Ljava/lang/String;->hashCode()I

    move-result p0

    add-int/2addr v0, p0

    return v0
.end method


# virtual methods
.method public O00Oo()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/O00000o;->O00oO00O:Ljava/util/List;

    return-object p0
.end method

.method getSize()D
    .locals 2

    iget-wide v0, p0, Lcom/airbnb/lottie/model/O00000o;->size:D

    return-wide v0
.end method

.method getStyle()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/O00000o;->style:Ljava/lang/String;

    return-object p0
.end method

.method public getWidth()D
    .locals 2

    iget-wide v0, p0, Lcom/airbnb/lottie/model/O00000o;->width:D

    return-wide v0
.end method

.method public hashCode()I
    .locals 2

    iget-char v0, p0, Lcom/airbnb/lottie/model/O00000o;->O00oO0OO:C

    iget-object v1, p0, Lcom/airbnb/lottie/model/O00000o;->fontFamily:Ljava/lang/String;

    iget-object p0, p0, Lcom/airbnb/lottie/model/O00000o;->style:Ljava/lang/String;

    invoke-static {v0, v1, p0}, Lcom/airbnb/lottie/model/O00000o;->O000000o(CLjava/lang/String;Ljava/lang/String;)I

    move-result p0

    return p0
.end method

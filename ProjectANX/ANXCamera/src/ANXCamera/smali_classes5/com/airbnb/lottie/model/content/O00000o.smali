.class public Lcom/airbnb/lottie/model/content/O00000o;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final O00o00Oo:Lcom/airbnb/lottie/model/O000000o/O00000o0;

.field private final O00oO:Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final O00oO0oO:Lcom/airbnb/lottie/model/content/GradientType;

.field private final O00oO0oo:Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final endPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

.field private final fillType:Landroid/graphics/Path$FillType;

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

.field private final startPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;


# direct methods
.method public constructor <init>(Ljava/lang/String;Lcom/airbnb/lottie/model/content/GradientType;Landroid/graphics/Path$FillType;Lcom/airbnb/lottie/model/O000000o/O00000o0;Lcom/airbnb/lottie/model/O000000o/O00000o;Lcom/airbnb/lottie/model/O000000o/O00000oo;Lcom/airbnb/lottie/model/O000000o/O00000oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p2, p0, Lcom/airbnb/lottie/model/content/O00000o;->O00oO0oO:Lcom/airbnb/lottie/model/content/GradientType;

    iput-object p3, p0, Lcom/airbnb/lottie/model/content/O00000o;->fillType:Landroid/graphics/Path$FillType;

    iput-object p4, p0, Lcom/airbnb/lottie/model/content/O00000o;->O00o00Oo:Lcom/airbnb/lottie/model/O000000o/O00000o0;

    iput-object p5, p0, Lcom/airbnb/lottie/model/content/O00000o;->opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

    iput-object p6, p0, Lcom/airbnb/lottie/model/content/O00000o;->startPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    iput-object p7, p0, Lcom/airbnb/lottie/model/content/O00000o;->endPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O00000o;->name:Ljava/lang/String;

    iput-object p8, p0, Lcom/airbnb/lottie/model/content/O00000o;->O00oO0oo:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p9, p0, Lcom/airbnb/lottie/model/content/O00000o;->O00oO:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-boolean p10, p0, Lcom/airbnb/lottie/model/content/O00000o;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;

    invoke-direct {v0, p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000Oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O00000o;)V

    return-object v0
.end method

.method O00OoOO()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->O00oO:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public O00OoOO0()Lcom/airbnb/lottie/model/O000000o/O00000o0;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->O00o00Oo:Lcom/airbnb/lottie/model/O000000o/O00000o0;

    return-object p0
.end method

.method O00OoOo0()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->O00oO0oo:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public getEndPoint()Lcom/airbnb/lottie/model/O000000o/O00000oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->endPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    return-object p0
.end method

.method public getFillType()Landroid/graphics/Path$FillType;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->fillType:Landroid/graphics/Path$FillType;

    return-object p0
.end method

.method public getGradientType()Lcom/airbnb/lottie/model/content/GradientType;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->O00oO0oO:Lcom/airbnb/lottie/model/content/GradientType;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

    return-object p0
.end method

.method public getStartPoint()Lcom/airbnb/lottie/model/O000000o/O00000oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->startPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O00000o;->hidden:Z

    return p0
.end method

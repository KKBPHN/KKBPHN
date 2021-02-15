.class public Lcom/airbnb/lottie/model/content/O00000oO;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final O00o00Oo:Lcom/airbnb/lottie/model/O000000o/O00000o0;

.field private final O00oO0oO:Lcom/airbnb/lottie/model/content/GradientType;

.field private final O00oOO0:F

.field private final O00oOO00:Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;

.field private final O00oOO0O:Ljava/util/List;

.field private final O00oOO0o:Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;

.field private final O00oOo:Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final endPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

.field private final startPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

.field private final width:Lcom/airbnb/lottie/model/O000000o/O00000Oo;


# direct methods
.method public constructor <init>(Ljava/lang/String;Lcom/airbnb/lottie/model/content/GradientType;Lcom/airbnb/lottie/model/O000000o/O00000o0;Lcom/airbnb/lottie/model/O000000o/O00000o;Lcom/airbnb/lottie/model/O000000o/O00000oo;Lcom/airbnb/lottie/model/O000000o/O00000oo;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;FLjava/util/List;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Z)V
    .locals 0
    .param p12    # Lcom/airbnb/lottie/model/O000000o/O00000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O00000oO;->name:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oO0oO:Lcom/airbnb/lottie/model/content/GradientType;

    iput-object p3, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00o00Oo:Lcom/airbnb/lottie/model/O000000o/O00000o0;

    iput-object p4, p0, Lcom/airbnb/lottie/model/content/O00000oO;->opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

    iput-object p5, p0, Lcom/airbnb/lottie/model/content/O00000oO;->startPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    iput-object p6, p0, Lcom/airbnb/lottie/model/content/O00000oO;->endPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    iput-object p7, p0, Lcom/airbnb/lottie/model/content/O00000oO;->width:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p8, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOO0o:Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;

    iput-object p9, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOO00:Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;

    iput p10, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOO0:F

    iput-object p11, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOO0O:Ljava/util/List;

    iput-object p12, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOo:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-boolean p13, p0, Lcom/airbnb/lottie/model/content/O00000oO;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;

    invoke-direct {v0, p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000OoO;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O00000oO;)V

    return-object v0
.end method

.method public O00OoOO0()Lcom/airbnb/lottie/model/O000000o/O00000o0;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00o00Oo:Lcom/airbnb/lottie/model/O000000o/O00000o0;

    return-object p0
.end method

.method public O00OoOo()Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOO0o:Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;

    return-object p0
.end method

.method public O00OoOoO()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOo:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public O00OoOoo()Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOO00:Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;

    return-object p0
.end method

.method public O00Ooo0()F
    .locals 0

    iget p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOO0:F

    return p0
.end method

.method public O00Ooo00()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oOO0O:Ljava/util/List;

    return-object p0
.end method

.method public getEndPoint()Lcom/airbnb/lottie/model/O000000o/O00000oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->endPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    return-object p0
.end method

.method public getGradientType()Lcom/airbnb/lottie/model/content/GradientType;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->O00oO0oO:Lcom/airbnb/lottie/model/content/GradientType;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

    return-object p0
.end method

.method public getStartPoint()Lcom/airbnb/lottie/model/O000000o/O00000oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->startPoint:Lcom/airbnb/lottie/model/O000000o/O00000oo;

    return-object p0
.end method

.method public getWidth()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->width:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O00000oO;->hidden:Z

    return p0
.end method

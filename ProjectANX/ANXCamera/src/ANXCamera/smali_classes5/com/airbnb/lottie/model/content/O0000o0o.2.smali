.class public Lcom/airbnb/lottie/model/content/O0000o0o;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/model/content/O00000Oo;


# instance fields
.field private final O00oOO0:F

.field private final O00oOO00:Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;

.field private final O00oOO0O:Ljava/util/List;

.field private final O00oOO0o:Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;

.field private final color:Lcom/airbnb/lottie/model/O000000o/O000000o;

.field private final hidden:Z

.field private final name:Ljava/lang/String;

.field private final offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

.field private final width:Lcom/airbnb/lottie/model/O000000o/O00000Oo;


# direct methods
.method public constructor <init>(Ljava/lang/String;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Ljava/util/List;Lcom/airbnb/lottie/model/O000000o/O000000o;Lcom/airbnb/lottie/model/O000000o/O00000o;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;FZ)V
    .locals 0
    .param p2    # Lcom/airbnb/lottie/model/O000000o/O00000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->name:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p3, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->O00oOO0O:Ljava/util/List;

    iput-object p4, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->color:Lcom/airbnb/lottie/model/O000000o/O000000o;

    iput-object p5, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

    iput-object p6, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->width:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    iput-object p7, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->O00oOO0o:Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;

    iput-object p8, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->O00oOO00:Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;

    iput p9, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->O00oOO0:F

    iput-boolean p10, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->hidden:Z

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;)Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;

    invoke-direct {v0, p1, p2, p0}, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000o0o;)V

    return-object v0
.end method

.method public O00OoOo()Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->O00oOO0o:Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;

    return-object p0
.end method

.method public O00OoOoO()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->offset:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public O00OoOoo()Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->O00oOO00:Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;

    return-object p0
.end method

.method public O00Ooo0()F
    .locals 0

    iget p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->O00oOO0:F

    return p0
.end method

.method public O00Ooo00()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->O00oOO0O:Ljava/util/List;

    return-object p0
.end method

.method public getColor()Lcom/airbnb/lottie/model/O000000o/O000000o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->color:Lcom/airbnb/lottie/model/O000000o/O000000o;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->opacity:Lcom/airbnb/lottie/model/O000000o/O00000o;

    return-object p0
.end method

.method public getWidth()Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->width:Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/model/content/O0000o0o;->hidden:Z

    return p0
.end method

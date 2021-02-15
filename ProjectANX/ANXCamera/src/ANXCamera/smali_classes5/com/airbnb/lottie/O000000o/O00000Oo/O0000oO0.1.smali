.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO0;
.super Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
.source ""


# instance fields
.field private final O00o0:Lcom/airbnb/lottie/model/content/O0000OoO;

.field private final O00o0O00:Landroid/graphics/Path;


# direct methods
.method public constructor <init>(Ljava/util/List;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;-><init>(Ljava/util/List;)V

    new-instance p1, Lcom/airbnb/lottie/model/content/O0000OoO;

    invoke-direct {p1}, Lcom/airbnb/lottie/model/content/O0000OoO;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO0;->O00o0:Lcom/airbnb/lottie/model/content/O0000OoO;

    new-instance p1, Landroid/graphics/Path;

    invoke-direct {p1}, Landroid/graphics/Path;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO0;->O00o0O00:Landroid/graphics/Path;

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Landroid/graphics/Path;
    .locals 2

    iget-object v0, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->startValue:Ljava/lang/Object;

    check-cast v0, Lcom/airbnb/lottie/model/content/O0000OoO;

    iget-object p1, p1, Lcom/airbnb/lottie/O00000oO/O000000o;->endValue:Ljava/lang/Object;

    check-cast p1, Lcom/airbnb/lottie/model/content/O0000OoO;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO0;->O00o0:Lcom/airbnb/lottie/model/content/O0000OoO;

    invoke-virtual {v1, v0, p1, p2}, Lcom/airbnb/lottie/model/content/O0000OoO;->O000000o(Lcom/airbnb/lottie/model/content/O0000OoO;Lcom/airbnb/lottie/model/content/O0000OoO;F)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO0;->O00o0:Lcom/airbnb/lottie/model/content/O0000OoO;

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO0;->O00o0O00:Landroid/graphics/Path;

    invoke-static {p1, p2}, Lcom/airbnb/lottie/O00000o/O0000O0o;->O000000o(Lcom/airbnb/lottie/model/content/O0000OoO;Landroid/graphics/Path;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO0;->O00o0O00:Landroid/graphics/Path;

    return-object p0
.end method

.method public bridge synthetic O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oO0;->O000000o(Lcom/airbnb/lottie/O00000oO/O000000o;F)Landroid/graphics/Path;

    move-result-object p0

    return-object p0
.end method

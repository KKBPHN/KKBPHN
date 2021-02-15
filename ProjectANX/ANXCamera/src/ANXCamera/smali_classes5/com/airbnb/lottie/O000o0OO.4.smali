.class public Lcom/airbnb/lottie/O000o0OO;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private final O00O0Oo0:Ljava/util/Map;

.field private final O00O0OoO:Lcom/airbnb/lottie/LottieAnimationView;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O00O0Ooo:Z

.field private final drawable:Lcom/airbnb/lottie/O000OoO0;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field


# direct methods
.method constructor <init>()V
    .locals 1
    .annotation build Landroidx/annotation/VisibleForTesting;
    .end annotation

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Ooo:Z

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0OoO:Lcom/airbnb/lottie/LottieAnimationView;

    iput-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->drawable:Lcom/airbnb/lottie/O000OoO0;

    return-void
.end method

.method public constructor <init>(Lcom/airbnb/lottie/LottieAnimationView;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Ooo:Z

    iput-object p1, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0OoO:Lcom/airbnb/lottie/LottieAnimationView;

    const/4 p1, 0x0

    iput-object p1, p0, Lcom/airbnb/lottie/O000o0OO;->drawable:Lcom/airbnb/lottie/O000OoO0;

    return-void
.end method

.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Ooo:Z

    iput-object p1, p0, Lcom/airbnb/lottie/O000o0OO;->drawable:Lcom/airbnb/lottie/O000OoO0;

    const/4 p1, 0x0

    iput-object p1, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0OoO:Lcom/airbnb/lottie/LottieAnimationView;

    return-void
.end method

.method private getText(Ljava/lang/String;)Ljava/lang/String;
    .locals 0

    return-object p1
.end method

.method private invalidate()V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0OoO:Lcom/airbnb/lottie/LottieAnimationView;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Landroid/widget/ImageView;->invalidate()V

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O000o0OO;->drawable:Lcom/airbnb/lottie/O000OoO0;

    if-eqz p0, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    :cond_1
    return-void
.end method


# virtual methods
.method public O00000oO(Ljava/lang/String;Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    invoke-interface {v0, p1, p2}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-direct {p0}, Lcom/airbnb/lottie/O000o0OO;->invalidate()V

    return-void
.end method

.method public O0000oO(Z)V
    .locals 0

    iput-boolean p1, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Ooo:Z

    return-void
.end method

.method public O00OO0o()V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    invoke-interface {v0}, Ljava/util/Map;->clear()V

    invoke-direct {p0}, Lcom/airbnb/lottie/O000o0OO;->invalidate()V

    return-void
.end method

.method public final O00oOooO(Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Ooo:Z

    if-eqz v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    invoke-interface {v0, p1}, Ljava/util/Map;->containsKey(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    invoke-interface {p0, p1}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/String;

    return-object p0

    :cond_0
    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000o0OO;->getText(Ljava/lang/String;)Ljava/lang/String;

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Ooo:Z

    if-eqz v0, :cond_1

    iget-object p0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    invoke-interface {p0, p1, p1}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_1
    return-object p1
.end method

.method public O00oOooo(Ljava/lang/String;)V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000o0OO;->O00O0Oo0:Ljava/util/Map;

    invoke-interface {v0, p1}, Ljava/util/Map;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    invoke-direct {p0}, Lcom/airbnb/lottie/O000o0OO;->invalidate()V

    return-void
.end method

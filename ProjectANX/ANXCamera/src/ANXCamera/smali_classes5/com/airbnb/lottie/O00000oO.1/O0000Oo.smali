.class public Lcom/airbnb/lottie/O00000oO/O0000Oo;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private animation:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final frameInfo:Lcom/airbnb/lottie/O00000oO/O00000Oo;

.field protected value:Ljava/lang/Object;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lcom/airbnb/lottie/O00000oO/O00000Oo;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000oO/O00000Oo;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->frameInfo:Lcom/airbnb/lottie/O00000oO/O00000Oo;

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->value:Ljava/lang/Object;

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;)V
    .locals 1
    .param p1    # Ljava/lang/Object;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Lcom/airbnb/lottie/O00000oO/O00000Oo;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000oO/O00000Oo;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->frameInfo:Lcom/airbnb/lottie/O00000oO/O00000Oo;

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->value:Ljava/lang/Object;

    iput-object p1, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->value:Ljava/lang/Object;

    return-void
.end method


# virtual methods
.method public O000000o(Lcom/airbnb/lottie/O00000oO/O00000Oo;)Ljava/lang/Object;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->value:Ljava/lang/Object;

    return-object p0
.end method

.method public final O00000Oo(FFLjava/lang/Object;Ljava/lang/Object;FFF)Ljava/lang/Object;
    .locals 8
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iget-object v0, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->frameInfo:Lcom/airbnb/lottie/O00000oO/O00000Oo;

    move v1, p1

    move v2, p2

    move-object v3, p3

    move-object v4, p4

    move v5, p5

    move v6, p6

    move v7, p7

    invoke-virtual/range {v0 .. v7}, Lcom/airbnb/lottie/O00000oO/O00000Oo;->O000000o(FFLjava/lang/Object;Ljava/lang/Object;FFF)Lcom/airbnb/lottie/O00000oO/O00000Oo;

    move-result-object p1

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000oO/O0000Oo;->O000000o(Lcom/airbnb/lottie/O00000oO/O00000Oo;)Ljava/lang/Object;

    move-result-object p0

    return-object p0
.end method

.method public final O00000o0(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 0
    .param p1    # Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param
    .annotation build Landroidx/annotation/RestrictTo;
        value = {
            .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
        }
    .end annotation

    iput-object p1, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->animation:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    return-void
.end method

.method public final setValue(Ljava/lang/Object;)V
    .locals 0
    .param p1    # Ljava/lang/Object;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    iput-object p1, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->value:Ljava/lang/Object;

    iget-object p0, p0, Lcom/airbnb/lottie/O00000oO/O0000Oo;->animation:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00OOoo()V

    :cond_0
    return-void
.end method

.class public Lcom/airbnb/lottie/O000OoO0;
.super Landroid/graphics/drawable/Drawable;
.source ""

# interfaces
.implements Landroid/graphics/drawable/Drawable$Callback;
.implements Landroid/graphics/drawable/Animatable;


# static fields
.field public static final INFINITE:I = -0x1

.field public static final RESTART:I = 0x1

.field public static final REVERSE:I = 0x2

.field private static final TAG:Ljava/lang/String; = "O000OoO0"


# instance fields
.field private O00000oo:Lcom/airbnb/lottie/O0000o0O;

.field private O0000O0o:Z

.field private O0000OOo:Z

.field private final O0000Oo:Ljava/util/ArrayList;

.field private final O0000Oo0:Ljava/util/Set;

.field private final O0000OoO:Landroid/animation/ValueAnimator$AnimatorUpdateListener;

.field private O0000Ooo:Lcom/airbnb/lottie/O00000Oo/O00000Oo;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field O0000o:Lcom/airbnb/lottie/O000o0OO;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O0000o0:Lcom/airbnb/lottie/O00000o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O0000o00:Ljava/lang/String;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O0000o0O:Lcom/airbnb/lottie/O00000Oo/O000000o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field O0000o0o:Lcom/airbnb/lottie/O00000o0;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O0000oO0:Z

.field private O0000oOO:Z

.field private O0000oOo:Z

.field private O0000oo0:Z

.field private alpha:I

.field private final animator:Lcom/airbnb/lottie/O00000o/O00000oO;

.field private isDirty:Z

.field private final matrix:Landroid/graphics/Matrix;

.field private scale:F

.field private scaleType:Landroid/widget/ImageView$ScaleType;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 3

    invoke-direct {p0}, Landroid/graphics/drawable/Drawable;-><init>()V

    new-instance v0, Landroid/graphics/Matrix;

    invoke-direct {v0}, Landroid/graphics/Matrix;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->matrix:Landroid/graphics/Matrix;

    new-instance v0, Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000o/O00000oO;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    const/high16 v0, 0x3f800000    # 1.0f

    iput v0, p0, Lcom/airbnb/lottie/O000OoO0;->scale:F

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000O0o:Z

    const/4 v1, 0x0

    iput-boolean v1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000OOo:Z

    new-instance v2, Ljava/util/HashSet;

    invoke-direct {v2}, Ljava/util/HashSet;-><init>()V

    iput-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo0:Ljava/util/Set;

    new-instance v2, Ljava/util/ArrayList;

    invoke-direct {v2}, Ljava/util/ArrayList;-><init>()V

    iput-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v2, Lcom/airbnb/lottie/O000OO00;

    invoke-direct {v2, p0}, Lcom/airbnb/lottie/O000OO00;-><init>(Lcom/airbnb/lottie/O000OoO0;)V

    iput-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->O0000OoO:Landroid/animation/ValueAnimator$AnimatorUpdateListener;

    const/16 v2, 0xff

    iput v2, p0, Lcom/airbnb/lottie/O000OoO0;->alpha:I

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oo0:Z

    iput-boolean v1, p0, Lcom/airbnb/lottie/O000OoO0;->isDirty:Z

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000OoO:Landroid/animation/ValueAnimator$AnimatorUpdateListener;

    invoke-virtual {v0, p0}, Lcom/airbnb/lottie/O00000o/O000000o;->addUpdateListener(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V

    return-void
.end method

.method static synthetic O000000o(Lcom/airbnb/lottie/O000OoO0;)Lcom/airbnb/lottie/model/layer/O00000oO;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    return-object p0
.end method

.method private O000000o(Landroid/graphics/Canvas;)V
    .locals 2
    .param p1    # Landroid/graphics/Canvas;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    sget-object v0, Landroid/widget/ImageView$ScaleType;->FIT_XY:Landroid/widget/ImageView$ScaleType;

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->scaleType:Landroid/widget/ImageView$ScaleType;

    if-ne v0, v1, :cond_0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000Oo(Landroid/graphics/Canvas;)V

    goto :goto_0

    :cond_0
    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000o0(Landroid/graphics/Canvas;)V

    :goto_0
    return-void
.end method

.method static synthetic O00000Oo(Lcom/airbnb/lottie/O000OoO0;)Lcom/airbnb/lottie/O00000o/O00000oO;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    return-object p0
.end method

.method private O00000Oo(Landroid/graphics/Canvas;)V
    .locals 8

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    if-nez v0, :cond_0

    return-void

    :cond_0
    const/4 v0, -0x1

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getBounds()Landroid/graphics/Rect;

    move-result-object v1

    invoke-virtual {v1}, Landroid/graphics/Rect;->width()I

    move-result v2

    int-to-float v2, v2

    iget-object v3, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v3}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v3

    invoke-virtual {v3}, Landroid/graphics/Rect;->width()I

    move-result v3

    int-to-float v3, v3

    div-float/2addr v2, v3

    invoke-virtual {v1}, Landroid/graphics/Rect;->height()I

    move-result v3

    int-to-float v3, v3

    iget-object v4, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v4}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v4

    invoke-virtual {v4}, Landroid/graphics/Rect;->height()I

    move-result v4

    int-to-float v4, v4

    div-float/2addr v3, v4

    iget-boolean v4, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oo0:Z

    if-eqz v4, :cond_2

    invoke-static {v2, v3}, Ljava/lang/Math;->min(FF)F

    move-result v4

    const/high16 v5, 0x3f800000    # 1.0f

    cmpg-float v6, v4, v5

    if-gez v6, :cond_1

    div-float v6, v5, v4

    div-float/2addr v2, v6

    div-float/2addr v3, v6

    goto :goto_0

    :cond_1
    move v6, v5

    :goto_0
    cmpl-float v5, v6, v5

    if-lez v5, :cond_2

    invoke-virtual {p1}, Landroid/graphics/Canvas;->save()I

    move-result v0

    invoke-virtual {v1}, Landroid/graphics/Rect;->width()I

    move-result v5

    int-to-float v5, v5

    const/high16 v7, 0x40000000    # 2.0f

    div-float/2addr v5, v7

    invoke-virtual {v1}, Landroid/graphics/Rect;->height()I

    move-result v1

    int-to-float v1, v1

    div-float/2addr v1, v7

    mul-float v7, v5, v4

    mul-float/2addr v4, v1

    sub-float/2addr v5, v7

    sub-float/2addr v1, v4

    invoke-virtual {p1, v5, v1}, Landroid/graphics/Canvas;->translate(FF)V

    invoke-virtual {p1, v6, v6, v7, v4}, Landroid/graphics/Canvas;->scale(FFFF)V

    :cond_2
    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v1}, Landroid/graphics/Matrix;->reset()V

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v1, v2, v3}, Landroid/graphics/Matrix;->preScale(FF)Z

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    iget-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->matrix:Landroid/graphics/Matrix;

    iget p0, p0, Lcom/airbnb/lottie/O000OoO0;->alpha:I

    invoke-virtual {v1, p1, v2, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    if-lez v0, :cond_3

    invoke-virtual {p1, v0}, Landroid/graphics/Canvas;->restoreToCount(I)V

    :cond_3
    return-void
.end method

.method private O00000o(Landroid/graphics/Canvas;)F
    .locals 2
    .param p1    # Landroid/graphics/Canvas;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-virtual {p1}, Landroid/graphics/Canvas;->getWidth()I

    move-result v0

    int-to-float v0, v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v1

    invoke-virtual {v1}, Landroid/graphics/Rect;->width()I

    move-result v1

    int-to-float v1, v1

    div-float/2addr v0, v1

    invoke-virtual {p1}, Landroid/graphics/Canvas;->getHeight()I

    move-result p1

    int-to-float p1, p1

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object p0

    invoke-virtual {p0}, Landroid/graphics/Rect;->height()I

    move-result p0

    int-to-float p0, p0

    div-float/2addr p1, p0

    invoke-static {v0, p1}, Ljava/lang/Math;->min(FF)F

    move-result p0

    return p0
.end method

.method private O00000o0(Landroid/graphics/Canvas;)V
    .locals 8

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    if-nez v0, :cond_0

    return-void

    :cond_0
    iget v0, p0, Lcom/airbnb/lottie/O000OoO0;->scale:F

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000o(Landroid/graphics/Canvas;)F

    move-result v1

    cmpl-float v2, v0, v1

    const/high16 v3, 0x3f800000    # 1.0f

    if-lez v2, :cond_1

    iget v0, p0, Lcom/airbnb/lottie/O000OoO0;->scale:F

    div-float/2addr v0, v1

    goto :goto_0

    :cond_1
    move v1, v0

    move v0, v3

    :goto_0
    const/4 v2, -0x1

    cmpl-float v3, v0, v3

    if-lez v3, :cond_2

    invoke-virtual {p1}, Landroid/graphics/Canvas;->save()I

    move-result v2

    iget-object v3, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v3}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v3

    invoke-virtual {v3}, Landroid/graphics/Rect;->width()I

    move-result v3

    int-to-float v3, v3

    const/high16 v4, 0x40000000    # 2.0f

    div-float/2addr v3, v4

    iget-object v5, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v5}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v5

    invoke-virtual {v5}, Landroid/graphics/Rect;->height()I

    move-result v5

    int-to-float v5, v5

    div-float/2addr v5, v4

    mul-float v4, v3, v1

    mul-float v6, v5, v1

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getScale()F

    move-result v7

    mul-float/2addr v7, v3

    sub-float/2addr v7, v4

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getScale()F

    move-result v3

    mul-float/2addr v3, v5

    sub-float/2addr v3, v6

    invoke-virtual {p1, v7, v3}, Landroid/graphics/Canvas;->translate(FF)V

    invoke-virtual {p1, v0, v0, v4, v6}, Landroid/graphics/Canvas;->scale(FFFF)V

    :cond_2
    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v0}, Landroid/graphics/Matrix;->reset()V

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v0, v1, v1}, Landroid/graphics/Matrix;->preScale(FF)Z

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->matrix:Landroid/graphics/Matrix;

    iget p0, p0, Lcom/airbnb/lottie/O000OoO0;->alpha:I

    invoke-virtual {v0, p1, v1, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    if-lez v2, :cond_3

    invoke-virtual {p1, v2}, Landroid/graphics/Canvas;->restoreToCount(I)V

    :cond_3
    return-void
.end method

.method private Oo0oO0()Lcom/airbnb/lottie/O00000Oo/O000000o;
    .locals 3

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object v0

    if-nez v0, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o0O:Lcom/airbnb/lottie/O00000Oo/O000000o;

    if-nez v0, :cond_1

    new-instance v0, Lcom/airbnb/lottie/O00000Oo/O000000o;

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o0o:Lcom/airbnb/lottie/O00000o0;

    invoke-direct {v0, v1, v2}, Lcom/airbnb/lottie/O00000Oo/O000000o;-><init>(Landroid/graphics/drawable/Drawable$Callback;Lcom/airbnb/lottie/O00000o0;)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o0O:Lcom/airbnb/lottie/O00000Oo/O000000o;

    :cond_1
    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o0O:Lcom/airbnb/lottie/O00000Oo/O000000o;

    return-object p0
.end method

.method private Oo0oO0O()Lcom/airbnb/lottie/O00000Oo/O00000Oo;
    .locals 5

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object v0

    const/4 v1, 0x0

    if-nez v0, :cond_0

    return-object v1

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Ooo:Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    if-eqz v0, :cond_1

    invoke-direct {p0}, Lcom/airbnb/lottie/O000OoO0;->getContext()Landroid/content/Context;

    move-result-object v2

    invoke-virtual {v0, v2}, Lcom/airbnb/lottie/O00000Oo/O00000Oo;->O000000o(Landroid/content/Context;)Z

    move-result v0

    if-nez v0, :cond_1

    iput-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Ooo:Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Ooo:Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    if-nez v0, :cond_2

    new-instance v0, Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o00:Ljava/lang/String;

    iget-object v3, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o0:Lcom/airbnb/lottie/O00000o;

    iget-object v4, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v4}, Lcom/airbnb/lottie/O0000o0O;->O00O0o0o()Ljava/util/Map;

    move-result-object v4

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/airbnb/lottie/O00000Oo/O00000Oo;-><init>(Landroid/graphics/drawable/Drawable$Callback;Ljava/lang/String;Lcom/airbnb/lottie/O00000o;Ljava/util/Map;)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Ooo:Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    :cond_2
    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Ooo:Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    return-object p0
.end method

.method private Oo0ooo()V
    .locals 4

    new-instance v0, Lcom/airbnb/lottie/model/layer/O00000oO;

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-static {v1}, Lcom/airbnb/lottie/O00000o0/O0000oOO;->O00000oO(Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/layer/O0000O0o;

    move-result-object v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O0000o0O;->getLayers()Ljava/util/List;

    move-result-object v2

    iget-object v3, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-direct {v0, p0, v1, v2, v3}, Lcom/airbnb/lottie/model/layer/O00000oO;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;Ljava/util/List;Lcom/airbnb/lottie/O0000o0O;)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    return-void
.end method

.method private getContext()Landroid/content/Context;
    .locals 2
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object p0

    const/4 v0, 0x0

    if-nez p0, :cond_0

    return-object v0

    :cond_0
    instance-of v1, p0, Landroid/view/View;

    if-eqz v1, :cond_1

    check-cast p0, Landroid/view/View;

    invoke-virtual {p0}, Landroid/view/View;->getContext()Landroid/content/Context;

    move-result-object p0

    return-object p0

    :cond_1
    return-object v0
.end method

.method private updateBounds()V
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getScale()F

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v1

    invoke-virtual {v1}, Landroid/graphics/Rect;->width()I

    move-result v1

    int-to-float v1, v1

    mul-float/2addr v1, v0

    float-to-int v1, v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v2

    invoke-virtual {v2}, Landroid/graphics/Rect;->height()I

    move-result v2

    int-to-float v2, v2

    mul-float/2addr v2, v0

    float-to-int v0, v2

    const/4 v2, 0x0

    invoke-virtual {p0, v2, v2, v1, v0}, Landroid/graphics/drawable/Drawable;->setBounds(IIII)V

    return-void
.end method


# virtual methods
.method public O000000o(Ljava/lang/String;)Landroid/graphics/Bitmap;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    invoke-direct {p0}, Lcom/airbnb/lottie/O000OoO0;->Oo0oO0O()Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    move-result-object p0

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000Oo/O00000Oo;->O000O0OO(Ljava/lang/String;)Landroid/graphics/Bitmap;

    move-result-object p0

    return-object p0

    :cond_0
    const/4 p0, 0x0

    return-object p0
.end method

.method public O000000o(Ljava/lang/String;Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;
    .locals 1
    .param p2    # Landroid/graphics/Bitmap;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    invoke-direct {p0}, Lcom/airbnb/lottie/O000OoO0;->Oo0oO0O()Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    move-result-object v0

    if-nez v0, :cond_0

    const-string p0, "Cannot update bitmap. Most likely the drawable is not added to a View which prevents Lottie from getting a Context."

    invoke-static {p0}, Lcom/airbnb/lottie/O00000o/O00000o;->warning(Ljava/lang/String;)V

    const/4 p0, 0x0

    return-object p0

    :cond_0
    invoke-virtual {v0, p1, p2}, Lcom/airbnb/lottie/O00000Oo/O00000Oo;->O000000o(Ljava/lang/String;Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;

    move-result-object p1

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-object p1
.end method

.method public O000000o(Ljava/lang/String;Ljava/lang/String;)Landroid/graphics/Typeface;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    invoke-direct {p0}, Lcom/airbnb/lottie/O000OoO0;->Oo0oO0()Lcom/airbnb/lottie/O00000Oo/O000000o;

    move-result-object p0

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O00000Oo/O000000o;->O000000o(Ljava/lang/String;Ljava/lang/String;)Landroid/graphics/Typeface;

    move-result-object p0

    return-object p0

    :cond_0
    const/4 p0, 0x0

    return-object p0
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;)Ljava/util/List;
    .locals 4

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    if-nez v0, :cond_0

    const-string p0, "Cannot resolve KeyPath. Composition is not set yet."

    invoke-static {p0}, Lcom/airbnb/lottie/O00000o/O00000o;->warning(Ljava/lang/String;)V

    invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;

    move-result-object p0

    return-object p0

    :cond_0
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    new-instance v1, Lcom/airbnb/lottie/model/O00000oO;

    const/4 v2, 0x0

    new-array v3, v2, [Ljava/lang/String;

    invoke-direct {v1, v3}, Lcom/airbnb/lottie/model/O00000oO;-><init>([Ljava/lang/String;)V

    invoke-virtual {p0, p1, v2, v0, v1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V

    return-object v0
.end method

.method public O000000o(I)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000OOOo;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000OOOo;-><init>(Lcom/airbnb/lottie/O000OoO0;I)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O00000oO;->O000000o(I)V

    return-void
.end method

.method public O000000o(Landroid/animation/Animator$AnimatorListener;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O000000o;->addListener(Landroid/animation/Animator$AnimatorListener;)V

    return-void
.end method

.method public O000000o(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O000000o;->addUpdateListener(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O00000o0;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o0o:Lcom/airbnb/lottie/O00000o0;

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o0O:Lcom/airbnb/lottie/O00000Oo/O000000o;

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000Oo/O000000o;->O00000Oo(Lcom/airbnb/lottie/O00000o0;)V

    :cond_0
    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O00000o;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o0:Lcom/airbnb/lottie/O00000o;

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Ooo:Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000Oo/O00000Oo;->O00000Oo(Lcom/airbnb/lottie/O00000o;)V

    :cond_0
    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O000o0OO;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o:Lcom/airbnb/lottie/O000o0OO;

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000O0oO;

    invoke-direct {v1, p0, p1, p2, p3}, Lcom/airbnb/lottie/O000O0oO;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O00000oO;->O00OoO0()Lcom/airbnb/lottie/model/O00000oo;

    move-result-object v0

    const/4 v1, 0x1

    if-eqz v0, :cond_1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O00000oO;->O00OoO0()Lcom/airbnb/lottie/model/O00000oo;

    move-result-object p1

    invoke-interface {p1, p2, p3}, Lcom/airbnb/lottie/model/O00000oo;->O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    goto :goto_1

    :cond_1
    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/model/O00000oO;)Ljava/util/List;

    move-result-object p1

    const/4 v0, 0x0

    :goto_0
    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v2

    if-ge v0, v2, :cond_2

    invoke-interface {p1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/airbnb/lottie/model/O00000oO;

    invoke-virtual {v2}, Lcom/airbnb/lottie/model/O00000oO;->O00OoO0()Lcom/airbnb/lottie/model/O00000oo;

    move-result-object v2

    invoke-interface {v2, p2, p3}, Lcom/airbnb/lottie/model/O00000oo;->O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result p1

    xor-int/2addr v1, p1

    :goto_1
    if-eqz v1, :cond_3

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    sget-object p1, Lcom/airbnb/lottie/O000Ooo0;->OO00oo:Ljava/lang/Float;

    if-ne p2, p1, :cond_3

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getProgress()F

    move-result p1

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->setProgress(F)V

    :cond_3
    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Ooo;)V
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O000O0oo;

    invoke-direct {v0, p0, p3}, Lcom/airbnb/lottie/O000O0oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/O00000oO/O0000Ooo;)V

    invoke-virtual {p0, p1, p2, v0}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    return-void
.end method

.method O000000o(Ljava/lang/Boolean;)V
    .locals 0

    invoke-virtual {p1}, Ljava/lang/Boolean;->booleanValue()Z

    move-result p1

    iput-boolean p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000O0o:Z

    return-void
.end method

.method public O000000o(Ljava/lang/String;Ljava/lang/String;Z)V
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000O0OO;

    invoke-direct {v1, p0, p1, p2, p3}, Lcom/airbnb/lottie/O000O0OO;-><init>(Lcom/airbnb/lottie/O000OoO0;Ljava/lang/String;Ljava/lang/String;Z)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O0000o0O;->O0000ooO(Ljava/lang/String;)Lcom/airbnb/lottie/model/O0000OOo;

    move-result-object v0

    const-string v1, "."

    const-string v2, "Cannot find marker with name "

    if-eqz v0, :cond_3

    iget p1, v0, Lcom/airbnb/lottie/model/O0000OOo;->O000oO0O:F

    float-to-int p1, p1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v0, p2}, Lcom/airbnb/lottie/O0000o0O;->O0000ooO(Ljava/lang/String;)Lcom/airbnb/lottie/model/O0000OOo;

    move-result-object v0

    if-eqz p2, :cond_2

    iget p2, v0, Lcom/airbnb/lottie/model/O0000OOo;->O000oO0O:F

    if-eqz p3, :cond_1

    const/high16 p3, 0x3f800000    # 1.0f

    goto :goto_0

    :cond_1
    const/4 p3, 0x0

    :goto_0
    add-float/2addr p2, p3

    float-to-int p2, p2

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(II)V

    return-void

    :cond_2
    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_3
    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public O00000Oo(FF)V
    .locals 2
    .param p1    # F
        .annotation build Landroidx/annotation/FloatRange;
            from = 0.0
            to = 1.0
        .end annotation
    .end param
    .param p2    # F
        .annotation build Landroidx/annotation/FloatRange;
            from = 0.0
            to = 1.0
        .end annotation
    .end param

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O00oOoOo;

    invoke-direct {v1, p0, p1, p2}, Lcom/airbnb/lottie/O00oOoOo;-><init>(Lcom/airbnb/lottie/O000OoO0;FF)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->O00O0oOO()F

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O0000o0O;->O00O0o0O()F

    move-result v1

    invoke-static {v0, v1, p1}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result p1

    float-to-int p1, p1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->O00O0oOO()F

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O0000o0O;->O00O0o0O()F

    move-result v1

    invoke-static {v0, v1, p2}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result p2

    float-to-int p2, p2

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(II)V

    return-void
.end method

.method public O00000Oo(Landroid/animation/Animator$AnimatorListener;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O000000o;->removeListener(Landroid/animation/Animator$AnimatorListener;)V

    return-void
.end method

.method public O00000Oo(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O000000o;->removeUpdateListener(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V

    return-void
.end method

.method public O00000Oo(Ljava/lang/String;)V
    .locals 0
    .param p1    # Ljava/lang/String;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    iput-object p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o00:Ljava/lang/String;

    return-void
.end method

.method public O00000o(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000O00o;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000O00o;-><init>(Lcom/airbnb/lottie/O000OoO0;Ljava/lang/String;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O0000o0O;->O0000ooO(Ljava/lang/String;)Lcom/airbnb/lottie/model/O0000OOo;

    move-result-object v0

    if-eqz v0, :cond_1

    iget p1, v0, Lcom/airbnb/lottie/model/O0000OOo;->O000oO0O:F

    float-to-int p1, p1

    iget v0, v0, Lcom/airbnb/lottie/model/O0000OOo;->O00oO0:F

    float-to-int v0, v0

    add-int/2addr v0, p1

    invoke-virtual {p0, p1, v0}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(II)V

    return-void

    :cond_1
    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Cannot find marker with name "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "."

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public O00000o0(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000Oo00;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000Oo00;-><init>(Lcom/airbnb/lottie/O000OoO0;Ljava/lang/String;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O0000o0O;->O0000ooO(Ljava/lang/String;)Lcom/airbnb/lottie/model/O0000OOo;

    move-result-object v0

    if-eqz v0, :cond_1

    iget p1, v0, Lcom/airbnb/lottie/model/O0000OOo;->O000oO0O:F

    iget v0, v0, Lcom/airbnb/lottie/model/O0000OOo;->O00oO0:F

    add-float/2addr p1, v0

    float-to-int p1, p1

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(I)V

    return-void

    :cond_1
    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Cannot find marker with name "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "."

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public O00000o0(Lcom/airbnb/lottie/O0000o0O;)Z
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    const/4 v1, 0x0

    if-ne v0, p1, :cond_0

    return v1

    :cond_0
    iput-boolean v1, p0, Lcom/airbnb/lottie/O000OoO0;->isDirty:Z

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000o0()V

    iput-object p1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-direct {p0}, Lcom/airbnb/lottie/O000OoO0;->Oo0ooo()V

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O00000o/O00000oO;->O00000o0(Lcom/airbnb/lottie/O0000o0O;)V

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000o/O00000oO;->getAnimatedFraction()F

    move-result v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/O000OoO0;->setProgress(F)V

    iget v0, p0, Lcom/airbnb/lottie/O000OoO0;->scale:F

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/O000OoO0;->setScale(F)V

    invoke-direct {p0}, Lcom/airbnb/lottie/O000OoO0;->updateBounds()V

    new-instance v0, Ljava/util/ArrayList;

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    invoke-direct {v0, v1}, Ljava/util/ArrayList;-><init>(Ljava/util/Collection;)V

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :goto_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_1

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/O000Oo0O;

    invoke-interface {v1, p1}, Lcom/airbnb/lottie/O000Oo0O;->O00000Oo(Lcom/airbnb/lottie/O0000o0O;)V

    invoke-interface {v0}, Ljava/util/Iterator;->remove()V

    goto :goto_0

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->clear()V

    iget-boolean p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oOO:Z

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O0000o0O;->O0000Oo0(Z)V

    const/4 p0, 0x1

    return p0
.end method

.method public O00000oO(Ljava/lang/String;)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000OOoo;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000OOoo;-><init>(Lcom/airbnb/lottie/O000OoO0;Ljava/lang/String;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O0000o0O;->O0000ooO(Ljava/lang/String;)Lcom/airbnb/lottie/model/O0000OOo;

    move-result-object v0

    if-eqz v0, :cond_1

    iget p1, v0, Lcom/airbnb/lottie/model/O0000OOo;->O000oO0O:F

    float-to-int p1, p1

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(I)V

    return-void

    :cond_1
    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Cannot find marker with name "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "."

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public O00000oo(F)V
    .locals 2
    .param p1    # F
        .annotation build Landroidx/annotation/FloatRange;
            from = 0.0
            to = 1.0
        .end annotation
    .end param

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000OOoO;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000OOoO;-><init>(Lcom/airbnb/lottie/O000OoO0;F)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->O00O0oOO()F

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O0000o0O;->O00O0o0O()F

    move-result v1

    invoke-static {v0, v1, p1}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result p1

    float-to-int p1, p1

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(I)V

    return-void
.end method

.method public O00000oo(I)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000OOo;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000OOo;-><init>(Lcom/airbnb/lottie/O000OoO0;I)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    int-to-float p1, p1

    const v0, 0x3f7d70a4    # 0.99f

    add-float/2addr p1, v0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O00000oO;->O00000oO(F)V

    return-void
.end method

.method public O00000oo(II)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000O0Oo;

    invoke-direct {v1, p0, p1, p2}, Lcom/airbnb/lottie/O000O0Oo;-><init>(Lcom/airbnb/lottie/O000OoO0;II)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    int-to-float p1, p1

    int-to-float p2, p2

    const v0, 0x3f7d70a4    # 0.99f

    add-float/2addr p2, v0

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O00000o/O00000oO;->O000000o(FF)V

    return-void
.end method

.method public O00000oo(Z)V
    .locals 2

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO0:Z

    if-ne v0, p1, :cond_0

    return-void

    :cond_0
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x13

    if-ge v0, v1, :cond_1

    const-string p0, "Merge paths are not supported pre-Kit Kat."

    invoke-static {p0}, Lcom/airbnb/lottie/O00000o/O00000o;->warning(Ljava/lang/String;)V

    return-void

    :cond_1
    iput-boolean p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO0:Z

    iget-object p1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-eqz p1, :cond_2

    invoke-direct {p0}, Lcom/airbnb/lottie/O000OoO0;->Oo0ooo()V

    :cond_2
    return-void
.end method

.method public O0000O0o(F)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000OOo0;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000OOo0;-><init>(Lcom/airbnb/lottie/O000OoO0;F)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->O00O0oOO()F

    move-result v0

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O0000o0O;->O00O0o0O()F

    move-result v1

    invoke-static {v0, v1, p1}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result p1

    float-to-int p1, p1

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(I)V

    return-void
.end method

.method public O0000O0o(Z)V
    .locals 0
    .annotation runtime Ljava/lang/Deprecated;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    if-eqz p1, :cond_0

    const/4 p1, -0x1

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    invoke-virtual {p0, p1}, Landroid/animation/ValueAnimator;->setRepeatCount(I)V

    return-void
.end method

.method public O0000OOo(Z)V
    .locals 0

    iput-boolean p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oOo:Z

    return-void
.end method

.method public O0000Oo(Z)V
    .locals 0

    iput-boolean p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000OOo:Z

    return-void
.end method

.method public O0000Oo0(Z)V
    .locals 0

    iput-boolean p1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oOO:Z

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O0000o0O;->O0000Oo0(Z)V

    :cond_0
    return-void
.end method

.method public O0000o()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000o()F

    move-result p0

    return p0
.end method

.method public O0000o0()V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000o/O00000oO;->isRunning()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000o/O00000oO;->cancel()V

    :cond_0
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    iput-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    iput-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Ooo:Lcom/airbnb/lottie/O00000Oo/O00000Oo;

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000o0()V

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method

.method public O0000o0o()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000o0o()F

    move-result p0

    return p0
.end method

.method public O0000oO()V
    .locals 2
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000OO0o;

    invoke-direct {v1, p0}, Lcom/airbnb/lottie/O000OO0o;-><init>(Lcom/airbnb/lottie/O000OoO0;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    iget-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000O0o:Z

    if-nez v0, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getRepeatCount()I

    move-result v0

    if-nez v0, :cond_2

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000oO()V

    :cond_2
    iget-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000O0o:Z

    if-nez v0, :cond_4

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getSpeed()F

    move-result v0

    const/4 v1, 0x0

    cmpg-float v0, v0, v1

    if-gez v0, :cond_3

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000o()F

    move-result v0

    goto :goto_0

    :cond_3
    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000o0o()F

    move-result v0

    :goto_0
    float-to-int v0, v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/O000OoO0;->setFrame(I)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->endAnimation()V

    :cond_4
    return-void
.end method

.method public O0000oO0()V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->clear()V

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000oO0()V

    return-void
.end method

.method public O0000oOo()V
    .locals 2
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000OO;

    invoke-direct {v1, p0}, Lcom/airbnb/lottie/O000OO;-><init>(Lcom/airbnb/lottie/O000OoO0;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    iget-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000O0o:Z

    if-nez v0, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getRepeatCount()I

    move-result v0

    if-nez v0, :cond_2

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000oOo()V

    :cond_2
    iget-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000O0o:Z

    if-nez v0, :cond_4

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getSpeed()F

    move-result v0

    const/4 v1, 0x0

    cmpg-float v0, v0, v1

    if-gez v0, :cond_3

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000o()F

    move-result v0

    goto :goto_0

    :cond_3
    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000o0o()F

    move-result v0

    :goto_0
    float-to-int v0, v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/O000OoO0;->setFrame(I)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->endAnimation()V

    :cond_4
    return-void
.end method

.method public O0000oo0()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000oo0()V

    return-void
.end method

.method public O000O00o()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO0:Z

    return p0
.end method

.method public O000O0OO()Lcom/airbnb/lottie/O0000o0O;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    return-object p0
.end method

.method public O000O0Oo()Ljava/lang/String;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o00:Ljava/lang/String;

    return-object p0
.end method

.method public O000O0o()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000oO;->O000O0o()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public O000O0o0()Lcom/airbnb/lottie/O000o0OO;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o:Lcom/airbnb/lottie/O000o0OO;

    return-object p0
.end method

.method public O000O0oO()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO:Lcom/airbnb/lottie/model/layer/O00000oO;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000oO;->O000O0oO()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public O000O0oo()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oOo:Z

    return p0
.end method

.method public O000OO()Z
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000o:Lcom/airbnb/lottie/O000o0OO;

    if-nez v0, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000o0O;->getCharacters()Landroidx/collection/SparseArrayCompat;

    move-result-object p0

    invoke-virtual {p0}, Landroidx/collection/SparseArrayCompat;->size()I

    move-result p0

    if-lez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public O000OO00()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oO0:Z

    return p0
.end method

.method public O000OO0o()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O000000o;->removeAllListeners()V

    return-void
.end method

.method public O00oOoOo()Lcom/airbnb/lottie/O000o0;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000o0O;->O00oOoOo()Lcom/airbnb/lottie/O000o0;

    move-result-object p0

    return-object p0

    :cond_0
    const/4 p0, 0x0

    return-object p0
.end method

.method public O00oOooo()V
    .locals 1

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000oo0:Z

    return-void
.end method

.method public cancelAnimation()V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->clear()V

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->cancel()V

    return-void
.end method

.method public draw(Landroid/graphics/Canvas;)V
    .locals 2
    .param p1    # Landroid/graphics/Canvas;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->isDirty:Z

    const-string v0, "Drawable#draw"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-boolean v1, p0, Lcom/airbnb/lottie/O000OoO0;->O0000OOo:Z

    if-eqz v1, :cond_0

    :try_start_0
    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Landroid/graphics/Canvas;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    goto :goto_0

    :catchall_0
    move-exception p0

    const-string p1, "Lottie crashed in draw!"

    invoke-static {p1, p0}, Lcom/airbnb/lottie/O00000o/O00000o;->O000000o(Ljava/lang/String;Ljava/lang/Throwable;)V

    goto :goto_0

    :cond_0
    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Landroid/graphics/Canvas;)V

    :goto_0
    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-void
.end method

.method public endAnimation()V
    .locals 1
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->clear()V

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->endAnimation()V

    return-void
.end method

.method public getAlpha()I
    .locals 0

    iget p0, p0, Lcom/airbnb/lottie/O000OoO0;->alpha:I

    return p0
.end method

.method public getFrame()I
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->getFrame()F

    move-result p0

    float-to-int p0, p0

    return p0
.end method

.method public getIntrinsicHeight()I
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    const/4 p0, -0x1

    goto :goto_0

    :cond_0
    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v0

    invoke-virtual {v0}, Landroid/graphics/Rect;->height()I

    move-result v0

    int-to-float v0, v0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getScale()F

    move-result p0

    mul-float/2addr v0, p0

    float-to-int p0, v0

    :goto_0
    return p0
.end method

.method public getIntrinsicWidth()I
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    const/4 p0, -0x1

    goto :goto_0

    :cond_0
    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->getBounds()Landroid/graphics/Rect;

    move-result-object v0

    invoke-virtual {v0}, Landroid/graphics/Rect;->width()I

    move-result v0

    int-to-float v0, v0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getScale()F

    move-result p0

    mul-float/2addr v0, p0

    float-to-int p0, v0

    :goto_0
    return p0
.end method

.method public getOpacity()I
    .locals 0

    const/4 p0, -0x3

    return p0
.end method

.method public getProgress()F
    .locals 0
    .annotation build Landroidx/annotation/FloatRange;
        from = 0.0
        to = 1.0
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O0000o0O()F

    move-result p0

    return p0
.end method

.method public getRepeatCount()I
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Landroid/animation/ValueAnimator;->getRepeatCount()I

    move-result p0

    return p0
.end method

.method public getRepeatMode()I
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Landroid/animation/ValueAnimator;->getRepeatMode()I

    move-result p0

    return p0
.end method

.method public getScale()F
    .locals 0

    iget p0, p0, Lcom/airbnb/lottie/O000OoO0;->scale:F

    return p0
.end method

.method public getSpeed()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->getSpeed()F

    move-result p0

    return p0
.end method

.method public invalidateDrawable(Landroid/graphics/drawable/Drawable;)V
    .locals 0
    .param p1    # Landroid/graphics/drawable/Drawable;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object p1

    if-nez p1, :cond_0

    return-void

    :cond_0
    invoke-interface {p1, p0}, Landroid/graphics/drawable/Drawable$Callback;->invalidateDrawable(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method public invalidateSelf()V
    .locals 1

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->isDirty:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000OoO0;->isDirty:Z

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object v0

    if-eqz v0, :cond_1

    invoke-interface {v0, p0}, Landroid/graphics/drawable/Drawable$Callback;->invalidateDrawable(Landroid/graphics/drawable/Drawable;)V

    :cond_1
    return-void
.end method

.method public isAnimating()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    if-nez p0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->isRunning()Z

    move-result p0

    return p0
.end method

.method public isLooping()Z
    .locals 1

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0}, Landroid/animation/ValueAnimator;->getRepeatCount()I

    move-result p0

    const/4 v0, -0x1

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public isRunning()Z
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->isAnimating()Z

    move-result p0

    return p0
.end method

.method public removeAllUpdateListeners()V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O00000o/O000000o;->removeAllUpdateListeners()V

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000OoO:Landroid/animation/ValueAnimator$AnimatorUpdateListener;

    invoke-virtual {v0, p0}, Lcom/airbnb/lottie/O00000o/O000000o;->addUpdateListener(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V

    return-void
.end method

.method public scheduleDrawable(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;J)V
    .locals 0
    .param p1    # Landroid/graphics/drawable/Drawable;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Ljava/lang/Runnable;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object p1

    if-nez p1, :cond_0

    return-void

    :cond_0
    invoke-interface {p1, p0, p2, p3, p4}, Landroid/graphics/drawable/Drawable$Callback;->scheduleDrawable(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;J)V

    return-void
.end method

.method public setAlpha(I)V
    .locals 0
    .param p1    # I
        .annotation build Landroidx/annotation/IntRange;
            from = 0x0L
            to = 0xffL
        .end annotation
    .end param

    iput p1, p0, Lcom/airbnb/lottie/O000OoO0;->alpha:I

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method

.method public setColorFilter(Landroid/graphics/ColorFilter;)V
    .locals 0
    .param p1    # Landroid/graphics/ColorFilter;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    const-string p0, "Use addColorFilter instead."

    invoke-static {p0}, Lcom/airbnb/lottie/O00000o/O00000o;->warning(Ljava/lang/String;)V

    return-void
.end method

.method public setFrame(I)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000O0o0;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000O0o0;-><init>(Lcom/airbnb/lottie/O000OoO0;I)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    int-to-float p1, p1

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O00000oO;->O00000o(F)V

    return-void
.end method

.method public setProgress(F)V
    .locals 3
    .param p1    # F
        .annotation build Landroidx/annotation/FloatRange;
            from = 0.0
            to = 1.0
        .end annotation
    .end param

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-nez v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/O000OoO0;->O0000Oo:Ljava/util/ArrayList;

    new-instance v1, Lcom/airbnb/lottie/O000O0o;

    invoke-direct {v1, p0, p1}, Lcom/airbnb/lottie/O000O0o;-><init>(Lcom/airbnb/lottie/O000OoO0;F)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void

    :cond_0
    const-string v0, "Drawable#setProgress"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v1, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    iget-object v2, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O0000o0O;->O00O0oOO()F

    move-result v2

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000o0O;->O00O0o0O()F

    move-result p0

    invoke-static {v2, p0, p1}, Lcom/airbnb/lottie/O00000o/O0000O0o;->lerp(FFF)F

    move-result p0

    invoke-virtual {v1, p0}, Lcom/airbnb/lottie/O00000o/O00000oO;->O00000o(F)V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-void
.end method

.method public setRepeatCount(I)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0, p1}, Landroid/animation/ValueAnimator;->setRepeatCount(I)V

    return-void
.end method

.method public setRepeatMode(I)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O00000oO;->setRepeatMode(I)V

    return-void
.end method

.method public setScale(F)V
    .locals 0

    iput p1, p0, Lcom/airbnb/lottie/O000OoO0;->scale:F

    invoke-direct {p0}, Lcom/airbnb/lottie/O000OoO0;->updateBounds()V

    return-void
.end method

.method setScaleType(Landroid/widget/ImageView$ScaleType;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/O000OoO0;->scaleType:Landroid/widget/ImageView$ScaleType;

    return-void
.end method

.method public setSpeed(F)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000OoO0;->animator:Lcom/airbnb/lottie/O00000o/O00000oO;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O00000o/O00000oO;->setSpeed(F)V

    return-void
.end method

.method public start()V
    .locals 0
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000oO()V

    return-void
.end method

.method public stop()V
    .locals 0
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->endAnimation()V

    return-void
.end method

.method public unscheduleDrawable(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;)V
    .locals 0
    .param p1    # Landroid/graphics/drawable/Drawable;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Ljava/lang/Runnable;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-virtual {p0}, Landroid/graphics/drawable/Drawable;->getCallback()Landroid/graphics/drawable/Drawable$Callback;

    move-result-object p1

    if-nez p1, :cond_0

    return-void

    :cond_0
    invoke-interface {p1, p0, p2}, Landroid/graphics/drawable/Drawable$Callback;->unscheduleDrawable(Landroid/graphics/drawable/Drawable;Ljava/lang/Runnable;)V

    return-void
.end method

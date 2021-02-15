.class public Lcom/airbnb/lottie/model/layer/O00000oo;
.super Lcom/airbnb/lottie/model/layer/O00000o0;
.source ""


# instance fields
.field private O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final O00ooOO:Landroid/graphics/Rect;

.field private final paint:Landroid/graphics/Paint;

.field private final src:Landroid/graphics/Rect;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lcom/airbnb/lottie/model/layer/O00000o0;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V

    new-instance p1, Lcom/airbnb/lottie/O000000o/O000000o;

    const/4 p2, 0x3

    invoke-direct {p1, p2}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>(I)V

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->paint:Landroid/graphics/Paint;

    new-instance p1, Landroid/graphics/Rect;

    invoke-direct {p1}, Landroid/graphics/Rect;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->src:Landroid/graphics/Rect;

    new-instance p1, Landroid/graphics/Rect;

    invoke-direct {p1}, Landroid/graphics/Rect;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->O00ooOO:Landroid/graphics/Rect;

    return-void
.end method

.method private getBitmap()Landroid/graphics/Bitmap;
    .locals 1
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o00OO()Ljava/lang/String;

    move-result-object v0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Ljava/lang/String;)Landroid/graphics/Bitmap;

    move-result-object p0

    return-object p0
.end method


# virtual methods
.method public O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V
    .locals 1

    invoke-super {p0, p1, p2, p3}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

    invoke-direct {p0}, Lcom/airbnb/lottie/model/layer/O00000oo;->getBitmap()Landroid/graphics/Bitmap;

    move-result-object p2

    if-eqz p2, :cond_0

    invoke-virtual {p2}, Landroid/graphics/Bitmap;->getWidth()I

    move-result p3

    int-to-float p3, p3

    invoke-static {}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00o0O0O()F

    move-result v0

    mul-float/2addr p3, v0

    invoke-virtual {p2}, Landroid/graphics/Bitmap;->getHeight()I

    move-result p2

    int-to-float p2, p2

    invoke-static {}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00o0O0O()F

    move-result v0

    mul-float/2addr p2, v0

    const/4 v0, 0x0

    invoke-virtual {p1, v0, v0, p3, p2}, Landroid/graphics/RectF;->set(FFFF)V

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0OO:Landroid/graphics/Matrix;

    invoke-virtual {p0, p1}, Landroid/graphics/Matrix;->mapRect(Landroid/graphics/RectF;)Z

    :cond_0
    return-void
.end method

.method public O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 1
    .param p2    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-super {p0, p1, p2}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00ooO:Landroid/graphics/ColorFilter;

    if-ne p1, v0, :cond_1

    if-nez p2, :cond_0

    const/4 p1, 0x0

    goto :goto_0

    :cond_0
    new-instance p1, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-direct {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;-><init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    :goto_0
    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    :cond_1
    return-void
.end method

.method public O00000Oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 4
    .param p1    # Landroid/graphics/Canvas;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-direct {p0}, Lcom/airbnb/lottie/model/layer/O00000oo;->getBitmap()Landroid/graphics/Bitmap;

    move-result-object v0

    if-eqz v0, :cond_2

    invoke-virtual {v0}, Landroid/graphics/Bitmap;->isRecycled()Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_0

    :cond_0
    invoke-static {}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00o0O0O()F

    move-result v1

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->paint:Landroid/graphics/Paint;

    invoke-virtual {v2, p3}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    if-eqz p3, :cond_1

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->paint:Landroid/graphics/Paint;

    invoke-virtual {p3}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Landroid/graphics/ColorFilter;

    invoke-virtual {v2, p3}, Landroid/graphics/Paint;->setColorFilter(Landroid/graphics/ColorFilter;)Landroid/graphics/ColorFilter;

    :cond_1
    invoke-virtual {p1}, Landroid/graphics/Canvas;->save()I

    invoke-virtual {p1, p2}, Landroid/graphics/Canvas;->concat(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->src:Landroid/graphics/Rect;

    invoke-virtual {v0}, Landroid/graphics/Bitmap;->getWidth()I

    move-result p3

    invoke-virtual {v0}, Landroid/graphics/Bitmap;->getHeight()I

    move-result v2

    const/4 v3, 0x0

    invoke-virtual {p2, v3, v3, p3, v2}, Landroid/graphics/Rect;->set(IIII)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->O00ooOO:Landroid/graphics/Rect;

    invoke-virtual {v0}, Landroid/graphics/Bitmap;->getWidth()I

    move-result p3

    int-to-float p3, p3

    mul-float/2addr p3, v1

    float-to-int p3, p3

    invoke-virtual {v0}, Landroid/graphics/Bitmap;->getHeight()I

    move-result v2

    int-to-float v2, v2

    mul-float/2addr v2, v1

    float-to-int v1, v2

    invoke-virtual {p2, v3, v3, p3, v1}, Landroid/graphics/Rect;->set(IIII)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->src:Landroid/graphics/Rect;

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->O00ooOO:Landroid/graphics/Rect;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000oo;->paint:Landroid/graphics/Paint;

    invoke-virtual {p1, v0, p2, p3, p0}, Landroid/graphics/Canvas;->drawBitmap(Landroid/graphics/Bitmap;Landroid/graphics/Rect;Landroid/graphics/Rect;Landroid/graphics/Paint;)V

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    :cond_2
    :goto_0
    return-void
.end method

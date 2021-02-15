.class public Lcom/airbnb/lottie/model/layer/O0000Oo0;
.super Lcom/airbnb/lottie/model/layer/O00000o0;
.source ""


# instance fields
.field private final O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V
    .locals 3

    invoke-direct {p0, p1, p2}, Lcom/airbnb/lottie/model/layer/O00000o0;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V

    new-instance v0, Lcom/airbnb/lottie/model/content/O0000o00;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00Oo()Ljava/util/List;

    move-result-object p2

    const-string v1, "__container"

    const/4 v2, 0x0

    invoke-direct {v0, v1, p2, v2}, Lcom/airbnb/lottie/model/content/O0000o00;-><init>(Ljava/lang/String;Ljava/util/List;Z)V

    new-instance p2, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    invoke-direct {p2, p1, p0, v0}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000o00;)V

    iput-object p2, p0, Lcom/airbnb/lottie/model/layer/O0000Oo0;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O0000Oo0;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;

    move-result-object p1

    invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;

    move-result-object p2

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Ljava/util/List;Ljava/util/List;)V

    return-void
.end method


# virtual methods
.method public O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V
    .locals 0

    invoke-super {p0, p1, p2, p3}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O0000Oo0;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0OO:Landroid/graphics/Matrix;

    invoke-virtual {p2, p1, p0, p3}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

    return-void
.end method

.method O00000Oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 0
    .param p1    # Landroid/graphics/Canvas;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O0000Oo0;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    invoke-virtual {p0, p1, p2, p3}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    return-void
.end method

.method protected O00000Oo(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O0000Oo0;->O00Ooo0o:Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;

    invoke-virtual {p0, p1, p2, p3, p4}, Lcom/airbnb/lottie/O000000o/O000000o/O00000oo;->O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V

    return-void
.end method

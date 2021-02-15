.class public Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;
.super Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;
.source ""


# instance fields
.field private O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final hidden:Z

.field private final layer:Lcom/airbnb/lottie/model/layer/O00000o0;

.field private final name:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000o0o;)V
    .locals 11

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->O00OoOo()Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/content/ShapeStroke$LineCapType;->Oo0o0oO()Landroid/graphics/Paint$Cap;

    move-result-object v4

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->O00OoOoo()Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/content/ShapeStroke$LineJoinType;->Oo0o0oo()Landroid/graphics/Paint$Join;

    move-result-object v5

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->O00Ooo0()F

    move-result v6

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object v7

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->getWidth()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v8

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->O00Ooo00()Ljava/util/List;

    move-result-object v9

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->O00OoOoO()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v10

    move-object v1, p0

    move-object v2, p1

    move-object v3, p2

    invoke-direct/range {v1 .. v10}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O00000o0;Landroid/graphics/Paint$Cap;Landroid/graphics/Paint$Join;FLcom/airbnb/lottie/model/O000000o/O00000o;Lcom/airbnb/lottie/model/O000000o/O00000Oo;Ljava/util/List;Lcom/airbnb/lottie/model/O000000o/O00000Oo;)V

    iput-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->getName()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->name:Ljava/lang/String;

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->isHidden()Z

    move-result p1

    iput-boolean p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->hidden:Z

    invoke-virtual {p3}, Lcom/airbnb/lottie/model/content/O0000o0o;->getColor()Lcom/airbnb/lottie/model/O000000o/O000000o;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O000000o;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    return-void
.end method


# virtual methods
.method public O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 2

    iget-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->hidden:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->paint:Landroid/graphics/Paint;

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    check-cast v1, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000OOo;

    invoke-virtual {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000OOo;->getIntValue()I

    move-result v1

    invoke-virtual {v0, v1}, Landroid/graphics/Paint;->setColor(I)V

    iget-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    if-eqz v0, :cond_1

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->paint:Landroid/graphics/Paint;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/graphics/ColorFilter;

    invoke-virtual {v1, v0}, Landroid/graphics/Paint;->setColorFilter(Landroid/graphics/ColorFilter;)Landroid/graphics/ColorFilter;

    :cond_1
    invoke-super {p0, p1, p2, p3}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    return-void
.end method

.method public O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 1
    .param p2    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-super {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O000000o/O00000o0;->O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->STROKE_COLOR:Ljava/lang/Integer;

    if-ne p1, v0, :cond_0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p0, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O000000o(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    goto :goto_0

    :cond_0
    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00ooO:Landroid/graphics/ColorFilter;

    if-ne p1, v0, :cond_2

    if-nez p2, :cond_1

    const/4 p1, 0x0

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    goto :goto_0

    :cond_1
    new-instance p1, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;

    invoke-direct {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oo0;-><init>(Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00O0oo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->layer:Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->O00OOOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    :cond_2
    :goto_0
    return-void
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000oo;->name:Ljava/lang/String;

    return-object p0
.end method

.class public abstract Lcom/airbnb/lottie/model/layer/O00000o0;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O000000o/O0000O0o;
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;
.implements Lcom/airbnb/lottie/model/O00000oo;


# static fields
.field private static final CLIP_SAVE_FLAG:I = 0x2

.field private static final CLIP_TO_LAYER_SAVE_FLAG:I = 0x10

.field private static final MATRIX_SAVE_FLAG:I = 0x1

.field private static final O00ooO0:I = 0x13


# instance fields
.field final O000OoO0:Lcom/airbnb/lottie/O000OoO0;

.field final O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

.field private final O00oOoOO:Landroid/graphics/Paint;

.field private final O00oOoo:Landroid/graphics/Paint;

.field private final O00oOoo0:Landroid/graphics/Paint;

.field private final O00oo:Ljava/util/List;

.field private final O00oo0:Landroid/graphics/RectF;

.field private final O00oo00:Landroid/graphics/Paint;

.field private final O00oo00O:Landroid/graphics/Paint;

.field private final O00oo00o:Landroid/graphics/RectF;

.field private final O00oo0O:Ljava/lang/String;

.field private final O00oo0O0:Landroid/graphics/RectF;

.field final O00oo0OO:Landroid/graphics/Matrix;

.field final O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

.field private O00oo0o:Lcom/airbnb/lottie/model/layer/O00000o0;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O00oo0o0:Lcom/airbnb/lottie/model/layer/O00000o0;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O00oo0oo:Ljava/util/List;

.field private mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private final matrix:Landroid/graphics/Matrix;

.field private final path:Landroid/graphics/Path;

.field private final rect:Landroid/graphics/RectF;

.field private visible:Z


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V
    .locals 3

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Landroid/graphics/Path;

    invoke-direct {v0}, Landroid/graphics/Path;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    new-instance v0, Landroid/graphics/Matrix;

    invoke-direct {v0}, Landroid/graphics/Matrix;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o;

    const/4 v1, 0x1

    invoke-direct {v0, v1}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>(I)V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o;

    sget-object v2, Landroid/graphics/PorterDuff$Mode;->DST_IN:Landroid/graphics/PorterDuff$Mode;

    invoke-direct {v0, v1, v2}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>(ILandroid/graphics/PorterDuff$Mode;)V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo0:Landroid/graphics/Paint;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o;

    sget-object v2, Landroid/graphics/PorterDuff$Mode;->DST_OUT:Landroid/graphics/PorterDuff$Mode;

    invoke-direct {v0, v1, v2}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>(ILandroid/graphics/PorterDuff$Mode;)V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo:Landroid/graphics/Paint;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o;

    invoke-direct {v0, v1}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>(I)V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00:Landroid/graphics/Paint;

    new-instance v0, Lcom/airbnb/lottie/O000000o/O000000o;

    sget-object v2, Landroid/graphics/PorterDuff$Mode;->CLEAR:Landroid/graphics/PorterDuff$Mode;

    invoke-direct {v0, v2}, Lcom/airbnb/lottie/O000000o/O000000o;-><init>(Landroid/graphics/PorterDuff$Mode;)V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00O:Landroid/graphics/Paint;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00o:Landroid/graphics/RectF;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0:Landroid/graphics/RectF;

    new-instance v0, Landroid/graphics/RectF;

    invoke-direct {v0}, Landroid/graphics/RectF;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O0:Landroid/graphics/RectF;

    new-instance v0, Landroid/graphics/Matrix;

    invoke-direct {v0}, Landroid/graphics/Matrix;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0OO:Landroid/graphics/Matrix;

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo:Ljava/util/List;

    iput-boolean v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->visible:Z

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    iput-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v0, "#draw"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O:Ljava/lang/String;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o00()Lcom/airbnb/lottie/model/layer/Layer$MatteType;

    move-result-object p1

    sget-object v0, Lcom/airbnb/lottie/model/layer/Layer$MatteType;->O0ooooO:Lcom/airbnb/lottie/model/layer/Layer$MatteType;

    if-ne p1, v0, :cond_0

    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00:Landroid/graphics/Paint;

    new-instance v0, Landroid/graphics/PorterDuffXfermode;

    sget-object v1, Landroid/graphics/PorterDuff$Mode;->DST_OUT:Landroid/graphics/PorterDuff$Mode;

    invoke-direct {v0, v1}, Landroid/graphics/PorterDuffXfermode;-><init>(Landroid/graphics/PorterDuff$Mode;)V

    goto :goto_0

    :cond_0
    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00:Landroid/graphics/Paint;

    new-instance v0, Landroid/graphics/PorterDuffXfermode;

    sget-object v1, Landroid/graphics/PorterDuff$Mode;->DST_IN:Landroid/graphics/PorterDuff$Mode;

    invoke-direct {v0, v1}, Landroid/graphics/PorterDuffXfermode;-><init>(Landroid/graphics/PorterDuff$Mode;)V

    :goto_0
    invoke-virtual {p1, v0}, Landroid/graphics/Paint;->setXfermode(Landroid/graphics/Xfermode;)Landroid/graphics/Xfermode;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->getTransform()Lcom/airbnb/lottie/model/O000000o/O0000Ooo;

    move-result-object p1

    invoke-virtual {p1}, Lcom/airbnb/lottie/model/O000000o/O0000Ooo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00Oo0()Ljava/util/List;

    move-result-object p1

    if-eqz p1, :cond_2

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00Oo0()Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->isEmpty()Z

    move-result p1

    if-nez p1, :cond_2

    new-instance p1, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00Oo0()Ljava/util/List;

    move-result-object p2

    invoke-direct {p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;-><init>(Ljava/util/List;)V

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo00o()Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :goto_1
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result p2

    if-eqz p2, :cond_1

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    goto :goto_1

    :cond_1
    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo0OO()Ljava/util/List;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :goto_2
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result p2

    if-eqz p2, :cond_2

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p0, p2}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    invoke-virtual {p2, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    goto :goto_2

    :cond_2
    invoke-direct {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->Oo0ooO0()V

    return-void
.end method

.method static O000000o(Lcom/airbnb/lottie/model/layer/O0000O0o;Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/layer/O00000o0;
    .locals 2
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    sget-object v0, Lcom/airbnb/lottie/model/layer/O00000Oo;->O00oOo0o:[I

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->getLayerType()Lcom/airbnb/lottie/model/layer/Layer$LayerType;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/Enum;->ordinal()I

    move-result v1

    aget v0, v0, v1

    packed-switch v0, :pswitch_data_0

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string p2, "Unknown layer type "

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->getLayerType()Lcom/airbnb/lottie/model/layer/Layer$LayerType;

    move-result-object p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Lcom/airbnb/lottie/O00000o/O00000o;->warning(Ljava/lang/String;)V

    const/4 p0, 0x0

    return-object p0

    :pswitch_0
    new-instance p2, Lcom/airbnb/lottie/model/layer/O0000o0;

    invoke-direct {p2, p1, p0}, Lcom/airbnb/lottie/model/layer/O0000o0;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V

    return-object p2

    :pswitch_1
    new-instance p2, Lcom/airbnb/lottie/model/layer/O0000OOo;

    invoke-direct {p2, p1, p0}, Lcom/airbnb/lottie/model/layer/O0000OOo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V

    return-object p2

    :pswitch_2
    new-instance p2, Lcom/airbnb/lottie/model/layer/O00000oo;

    invoke-direct {p2, p1, p0}, Lcom/airbnb/lottie/model/layer/O00000oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V

    return-object p2

    :pswitch_3
    new-instance p2, Lcom/airbnb/lottie/model/layer/O0000Oo;

    invoke-direct {p2, p1, p0}, Lcom/airbnb/lottie/model/layer/O0000Oo;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V

    return-object p2

    :pswitch_4
    new-instance v0, Lcom/airbnb/lottie/model/layer/O00000oO;

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o00OO()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p2, v1}, Lcom/airbnb/lottie/O0000o0O;->O0000ooo(Ljava/lang/String;)Ljava/util/List;

    move-result-object v1

    invoke-direct {v0, p1, p0, v1, p2}, Lcom/airbnb/lottie/model/layer/O00000oO;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;Ljava/util/List;Lcom/airbnb/lottie/O0000o0O;)V

    return-object v0

    :pswitch_5
    new-instance p2, Lcom/airbnb/lottie/model/layer/O0000Oo0;

    invoke-direct {p2, p1, p0}, Lcom/airbnb/lottie/model/layer/O0000Oo0;-><init>(Lcom/airbnb/lottie/O000OoO0;Lcom/airbnb/lottie/model/layer/O0000O0o;)V

    return-object p2

    nop

    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;)V
    .locals 9

    const-string v0, "Layer#saveLayer"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo0:Landroid/graphics/Paint;

    const/16 v3, 0x13

    invoke-static {p1, v1, v2, v3}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/RectF;Landroid/graphics/Paint;I)V

    sget v1, Landroid/os/Build$VERSION;->SDK_INT:I

    const/4 v2, 0x0

    const/16 v3, 0x1c

    if-ge v1, v3, :cond_0

    invoke-virtual {p1, v2}, Landroid/graphics/Canvas;->drawColor(I)V

    :cond_0
    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    :goto_0
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo0()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-ge v2, v0, :cond_a

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo0()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    move-object v6, v0

    check-cast v6, Lcom/airbnb/lottie/model/content/O00000oo;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo00o()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    move-object v7, v0

    check-cast v7, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo0OO()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    move-object v8, v0

    check-cast v8, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    sget-object v0, Lcom/airbnb/lottie/model/layer/O00000Oo;->O00oOoO:[I

    invoke-virtual {v6}, Lcom/airbnb/lottie/model/content/O00000oo;->O00Ooo0O()Lcom/airbnb/lottie/model/content/Mask$MaskMode;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/Enum;->ordinal()I

    move-result v1

    aget v0, v0, v1

    const/16 v1, 0xff

    const/4 v3, 0x1

    if-eq v0, v3, :cond_8

    const/4 v3, 0x2

    if-eq v0, v3, :cond_5

    const/4 v1, 0x3

    if-eq v0, v1, :cond_3

    const/4 v1, 0x4

    if-eq v0, v1, :cond_1

    goto/16 :goto_1

    :cond_1
    invoke-virtual {v6}, Lcom/airbnb/lottie/model/content/O00000oo;->O00OooO0()Z

    move-result v0

    move-object v3, p0

    move-object v4, p1

    move-object v5, p2

    if-eqz v0, :cond_2

    invoke-direct/range {v3 .. v8}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000o0(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    goto :goto_1

    :cond_2
    invoke-direct/range {v3 .. v8}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    goto :goto_1

    :cond_3
    invoke-virtual {v6}, Lcom/airbnb/lottie/model/content/O00000oo;->O00OooO0()Z

    move-result v0

    move-object v3, p0

    move-object v4, p1

    move-object v5, p2

    if-eqz v0, :cond_4

    invoke-direct/range {v3 .. v8}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    goto :goto_1

    :cond_4
    invoke-direct/range {v3 .. v8}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000Oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    goto :goto_1

    :cond_5
    if-nez v2, :cond_6

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    const/high16 v3, -0x1000000

    invoke-virtual {v0, v3}, Landroid/graphics/Paint;->setColor(I)V

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {v0, v1}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p1, v0, v1}, Landroid/graphics/Canvas;->drawRect(Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    :cond_6
    invoke-virtual {v6}, Lcom/airbnb/lottie/model/content/O00000oo;->O00OooO0()Z

    move-result v0

    move-object v3, p0

    move-object v4, p1

    move-object v5, p2

    if-eqz v0, :cond_7

    invoke-direct/range {v3 .. v8}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000oO(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    goto :goto_1

    :cond_7
    invoke-direct/range {v3 .. v8}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    goto :goto_1

    :cond_8
    invoke-direct {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->Oo0oo0o()Z

    move-result v0

    if-eqz v0, :cond_9

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {v0, v1}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p1, v0, v1}, Landroid/graphics/Canvas;->drawRect(Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    :cond_9
    :goto_1
    add-int/lit8 v2, v2, 0x1

    goto/16 :goto_0

    :cond_a
    const-string p0, "Layer#restoreLayer"

    invoke-static {p0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    invoke-static {p0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-void
.end method

.method private O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 0

    invoke-virtual {p4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Landroid/graphics/Path;

    iget-object p4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p4, p3}, Landroid/graphics/Path;->set(Landroid/graphics/Path;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p3, p2}, Landroid/graphics/Path;->transform(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result p3

    int-to-float p3, p3

    const p4, 0x40233333    # 2.55f

    mul-float/2addr p3, p4

    float-to-int p3, p3

    invoke-virtual {p2, p3}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p1, p2, p0}, Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V

    return-void
.end method

.method private O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;)V
    .locals 10

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00o:Landroid/graphics/RectF;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1, v1, v1}, Landroid/graphics/RectF;->set(FFFF)V

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00o000()Z

    move-result v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo0()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    const/4 v2, 0x0

    move v3, v2

    :goto_0
    if-ge v3, v0, :cond_5

    iget-object v4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo0()Ljava/util/List;

    move-result-object v4

    invoke-interface {v4, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lcom/airbnb/lottie/model/content/O00000oo;

    iget-object v5, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo00o()Ljava/util/List;

    move-result-object v5

    invoke-interface {v5, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/graphics/Path;

    iget-object v6, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {v6, v5}, Landroid/graphics/Path;->set(Landroid/graphics/Path;)V

    iget-object v5, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {v5, p2}, Landroid/graphics/Path;->transform(Landroid/graphics/Matrix;)V

    sget-object v5, Lcom/airbnb/lottie/model/layer/O00000Oo;->O00oOoO:[I

    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000oo;->O00Ooo0O()Lcom/airbnb/lottie/model/content/Mask$MaskMode;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/Enum;->ordinal()I

    move-result v6

    aget v5, v5, v6

    const/4 v6, 0x1

    if-eq v5, v6, :cond_4

    const/4 v6, 0x2

    if-eq v5, v6, :cond_4

    const/4 v6, 0x3

    if-eq v5, v6, :cond_1

    const/4 v6, 0x4

    if-eq v5, v6, :cond_1

    goto :goto_1

    :cond_1
    invoke-virtual {v4}, Lcom/airbnb/lottie/model/content/O00000oo;->O00OooO0()Z

    move-result v4

    if-eqz v4, :cond_2

    return-void

    :cond_2
    :goto_1
    iget-object v4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    iget-object v5, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O0:Landroid/graphics/RectF;

    invoke-virtual {v4, v5, v2}, Landroid/graphics/Path;->computeBounds(Landroid/graphics/RectF;Z)V

    iget-object v4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00o:Landroid/graphics/RectF;

    if-nez v3, :cond_3

    iget-object v5, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O0:Landroid/graphics/RectF;

    invoke-virtual {v4, v5}, Landroid/graphics/RectF;->set(Landroid/graphics/RectF;)V

    goto :goto_2

    :cond_3
    iget v5, v4, Landroid/graphics/RectF;->left:F

    iget-object v6, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O0:Landroid/graphics/RectF;

    iget v6, v6, Landroid/graphics/RectF;->left:F

    invoke-static {v5, v6}, Ljava/lang/Math;->min(FF)F

    move-result v5

    iget-object v6, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00o:Landroid/graphics/RectF;

    iget v6, v6, Landroid/graphics/RectF;->top:F

    iget-object v7, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O0:Landroid/graphics/RectF;

    iget v7, v7, Landroid/graphics/RectF;->top:F

    invoke-static {v6, v7}, Ljava/lang/Math;->min(FF)F

    move-result v6

    iget-object v7, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00o:Landroid/graphics/RectF;

    iget v7, v7, Landroid/graphics/RectF;->right:F

    iget-object v8, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O0:Landroid/graphics/RectF;

    iget v8, v8, Landroid/graphics/RectF;->right:F

    invoke-static {v7, v8}, Ljava/lang/Math;->max(FF)F

    move-result v7

    iget-object v8, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00o:Landroid/graphics/RectF;

    iget v8, v8, Landroid/graphics/RectF;->bottom:F

    iget-object v9, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O0:Landroid/graphics/RectF;

    iget v9, v9, Landroid/graphics/RectF;->bottom:F

    invoke-static {v8, v9}, Ljava/lang/Math;->max(FF)F

    move-result v8

    invoke-virtual {v4, v5, v6, v7, v8}, Landroid/graphics/RectF;->set(FFFF)V

    :goto_2
    add-int/lit8 v3, v3, 0x1

    goto/16 :goto_0

    :cond_4
    return-void

    :cond_5
    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00o:Landroid/graphics/RectF;

    invoke-virtual {p1, p0}, Landroid/graphics/RectF;->intersect(Landroid/graphics/RectF;)Z

    move-result p0

    if-nez p0, :cond_6

    invoke-virtual {p1, v1, v1, v1, v1}, Landroid/graphics/RectF;->set(FFFF)V

    :cond_6
    return-void
.end method

.method static synthetic O000000o(Lcom/airbnb/lottie/model/layer/O00000o0;Z)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->setVisible(Z)V

    return-void
.end method

.method private O00000Oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 1

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo0:Landroid/graphics/Paint;

    invoke-static {p1, p3, v0}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    invoke-virtual {p4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Landroid/graphics/Path;

    iget-object p4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p4, p3}, Landroid/graphics/Path;->set(Landroid/graphics/Path;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p3, p2}, Landroid/graphics/Path;->transform(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result p3

    int-to-float p3, p3

    const p4, 0x40233333    # 2.55f

    mul-float/2addr p3, p4

    float-to-int p3, p3

    invoke-virtual {p2, p3}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p1, p2, p0}, Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    return-void
.end method

.method private O00000Oo(Landroid/graphics/RectF;Landroid/graphics/Matrix;)V
    .locals 4

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00o000O()Z

    move-result v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o00()Lcom/airbnb/lottie/model/layer/Layer$MatteType;

    move-result-object v0

    sget-object v1, Lcom/airbnb/lottie/model/layer/Layer$MatteType;->O0ooooO:Lcom/airbnb/lottie/model/layer/Layer$MatteType;

    if-ne v0, v1, :cond_1

    return-void

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0:Landroid/graphics/RectF;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1, v1, v1}, Landroid/graphics/RectF;->set(FFFF)V

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o0:Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0:Landroid/graphics/RectF;

    const/4 v3, 0x1

    invoke-virtual {v0, v2, p2, v3}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0:Landroid/graphics/RectF;

    invoke-virtual {p1, p0}, Landroid/graphics/RectF;->intersect(Landroid/graphics/RectF;)Z

    move-result p0

    if-nez p0, :cond_2

    invoke-virtual {p1, v1, v1, v1, v1}, Landroid/graphics/RectF;->set(FFFF)V

    :cond_2
    return-void
.end method

.method private O00000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 1

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo0:Landroid/graphics/Paint;

    invoke-static {p1, p3, v0}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p1, p3, v0}, Landroid/graphics/Canvas;->drawRect(Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo:Landroid/graphics/Paint;

    invoke-virtual {p5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p5

    check-cast p5, Ljava/lang/Integer;

    invoke-virtual {p5}, Ljava/lang/Integer;->intValue()I

    move-result p5

    int-to-float p5, p5

    const v0, 0x40233333    # 2.55f

    mul-float/2addr p5, v0

    float-to-int p5, p5

    invoke-virtual {p3, p5}, Landroid/graphics/Paint;->setAlpha(I)V

    invoke-virtual {p4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Landroid/graphics/Path;

    iget-object p4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p4, p3}, Landroid/graphics/Path;->set(Landroid/graphics/Path;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p3, p2}, Landroid/graphics/Path;->transform(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo:Landroid/graphics/Paint;

    invoke-virtual {p1, p2, p0}, Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    return-void
.end method

.method private O00000o0(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 1

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-static {p1, p3, v0}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p1, p3, v0}, Landroid/graphics/Canvas;->drawRect(Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    invoke-virtual {p4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Landroid/graphics/Path;

    iget-object p4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p4, p3}, Landroid/graphics/Path;->set(Landroid/graphics/Path;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p3, p2}, Landroid/graphics/Path;->transform(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result p3

    int-to-float p3, p3

    const p4, 0x40233333    # 2.55f

    mul-float/2addr p3, p4

    float-to-int p3, p3

    invoke-virtual {p2, p3}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo:Landroid/graphics/Paint;

    invoke-virtual {p1, p2, p0}, Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    return-void
.end method

.method private O00000oO(Landroid/graphics/Canvas;)V
    .locals 10

    const-string v0, "Layer#clearLayer"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget v2, v1, Landroid/graphics/RectF;->left:F

    const/high16 v3, 0x3f800000    # 1.0f

    sub-float v5, v2, v3

    iget v2, v1, Landroid/graphics/RectF;->top:F

    sub-float v6, v2, v3

    iget v2, v1, Landroid/graphics/RectF;->right:F

    add-float v7, v2, v3

    iget v1, v1, Landroid/graphics/RectF;->bottom:F

    add-float v8, v1, v3

    iget-object v9, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00O:Landroid/graphics/Paint;

    move-object v4, p1

    invoke-virtual/range {v4 .. v9}, Landroid/graphics/Canvas;->drawRect(FFFFLandroid/graphics/Paint;)V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-void
.end method

.method private O00000oO(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 1

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo:Landroid/graphics/Paint;

    invoke-static {p1, p3, v0}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-virtual {p1, p3, v0}, Landroid/graphics/Canvas;->drawRect(Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo:Landroid/graphics/Paint;

    invoke-virtual {p5}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p5

    check-cast p5, Ljava/lang/Integer;

    invoke-virtual {p5}, Ljava/lang/Integer;->intValue()I

    move-result p5

    int-to-float p5, p5

    const v0, 0x40233333    # 2.55f

    mul-float/2addr p5, v0

    float-to-int p5, p5

    invoke-virtual {p3, p5}, Landroid/graphics/Paint;->setAlpha(I)V

    invoke-virtual {p4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Landroid/graphics/Path;

    iget-object p4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p4, p3}, Landroid/graphics/Path;->set(Landroid/graphics/Path;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p3, p2}, Landroid/graphics/Path;->transform(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo:Landroid/graphics/Paint;

    invoke-virtual {p1, p2, p0}, Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    return-void
.end method

.method private O00000oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;Lcom/airbnb/lottie/model/content/O00000oo;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 0

    invoke-virtual {p4}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Landroid/graphics/Path;

    iget-object p4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p4, p3}, Landroid/graphics/Path;->set(Landroid/graphics/Path;)V

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    invoke-virtual {p3, p2}, Landroid/graphics/Path;->transform(Landroid/graphics/Matrix;)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->path:Landroid/graphics/Path;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoo:Landroid/graphics/Paint;

    invoke-virtual {p1, p2, p0}, Landroid/graphics/Canvas;->drawPath(Landroid/graphics/Path;Landroid/graphics/Paint;)V

    return-void
.end method

.method private O0000o0O(F)V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->O000O0OO()Lcom/airbnb/lottie/O0000o0O;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->O00oOoOo()Lcom/airbnb/lottie/O000o0;

    move-result-object v0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0, p1}, Lcom/airbnb/lottie/O000o0;->O000000o(Ljava/lang/String;F)V

    return-void
.end method

.method private Oo0oo()V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0oo:Ljava/util/List;

    if-eqz v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o:Lcom/airbnb/lottie/model/layer/O00000o0;

    if-nez v0, :cond_1

    invoke-static {}, Ljava/util/Collections;->emptyList()Ljava/util/List;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0oo:Ljava/util/List;

    return-void

    :cond_1
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0oo:Ljava/util/List;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o:Lcom/airbnb/lottie/model/layer/O00000o0;

    :goto_0
    if-eqz v0, :cond_2

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0oo:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    iget-object v0, v0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o:Lcom/airbnb/lottie/model/layer/O00000o0;

    goto :goto_0

    :cond_2
    return-void
.end method

.method private Oo0oo0o()Z
    .locals 4

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo00o()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    move v0, v1

    :goto_0
    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo0()Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v2

    if-ge v0, v2, :cond_2

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo0()Ljava/util/List;

    move-result-object v2

    invoke-interface {v2, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/airbnb/lottie/model/content/O00000oo;

    invoke-virtual {v2}, Lcom/airbnb/lottie/model/content/O00000oo;->O00Ooo0O()Lcom/airbnb/lottie/model/content/Mask$MaskMode;

    move-result-object v2

    sget-object v3, Lcom/airbnb/lottie/model/content/Mask$MaskMode;->O0ooo00:Lcom/airbnb/lottie/model/content/Mask$MaskMode;

    if-eq v2, v3, :cond_1

    return v1

    :cond_1
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    const/4 p0, 0x1

    return p0
.end method

.method private Oo0ooO0()V
    .locals 4

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o000o()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v0

    const/4 v1, 0x1

    if-nez v0, :cond_1

    new-instance v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {v2}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o000o()Ljava/util/List;

    move-result-object v2

    invoke-direct {v0, v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;-><init>(Ljava/util/List;)V

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00Oo00()V

    new-instance v2, Lcom/airbnb/lottie/model/layer/O000000o;

    invoke-direct {v2, p0, v0}, Lcom/airbnb/lottie/model/layer/O000000o;-><init>(Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;)V

    invoke-virtual {v0, v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Float;

    invoke-virtual {v2}, Ljava/lang/Float;->floatValue()F

    move-result v2

    const/high16 v3, 0x3f800000    # 1.0f

    cmpl-float v2, v2, v3

    if-nez v2, :cond_0

    goto :goto_0

    :cond_0
    const/4 v1, 0x0

    :goto_0
    invoke-direct {p0, v1}, Lcom/airbnb/lottie/model/layer/O00000o0;->setVisible(Z)V

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    goto :goto_1

    :cond_1
    invoke-direct {p0, v1}, Lcom/airbnb/lottie/model/layer/O00000o0;->setVisible(Z)V

    :goto_1
    return-void
.end method

.method private invalidateSelf()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->invalidateSelf()V

    return-void
.end method

.method private setVisible(Z)V
    .locals 1

    iget-boolean v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->visible:Z

    if-eq p1, v0, :cond_0

    iput-boolean p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->visible:Z

    invoke-direct {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->invalidateSelf()V

    :cond_0
    return-void
.end method


# virtual methods
.method public O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
    .locals 6

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O:Ljava/lang/String;

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-boolean v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->visible:Z

    if-eqz v0, :cond_8

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->isHidden()Z

    move-result v0

    if-eqz v0, :cond_0

    goto/16 :goto_2

    :cond_0
    invoke-direct {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->Oo0oo()V

    const-string v0, "Layer#parentMatrix"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v1}, Landroid/graphics/Matrix;->reset()V

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {v1, p2}, Landroid/graphics/Matrix;->set(Landroid/graphics/Matrix;)V

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0oo:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    add-int/lit8 v1, v1, -0x1

    :goto_0
    if-ltz v1, :cond_1

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    iget-object v3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0oo:Ljava/util/List;

    invoke-interface {v3, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object v3, v3, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v3}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    add-int/lit8 v1, v1, -0x1

    goto :goto_0

    :cond_1
    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getOpacity()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v0

    if-nez v0, :cond_2

    const/16 v0, 0x64

    goto :goto_1

    :cond_2
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getOpacity()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    :goto_1
    int-to-float p3, p3

    const/high16 v1, 0x437f0000    # 255.0f

    div-float/2addr p3, v1

    int-to-float v0, v0

    mul-float/2addr p3, v0

    const/high16 v0, 0x42c80000    # 100.0f

    div-float/2addr p3, v0

    mul-float/2addr p3, v1

    float-to-int p3, p3

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00o000O()Z

    move-result v0

    const-string v1, "Layer#drawLayer"

    if-nez v0, :cond_3

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00o000()Z

    move-result v0

    if-nez v0, :cond_3

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object v0

    invoke-virtual {p2, v0}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    invoke-static {v1}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {p0, p1, p2, p3}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000Oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    invoke-static {v1}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O:Ljava/lang/String;

    invoke-static {p1}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    move-result p1

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O0000o0O(F)V

    return-void

    :cond_3
    const-string v0, "Layer#computeBounds"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    const/4 v4, 0x0

    invoke-virtual {p0, v2, v3, v4}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    invoke-direct {p0, v2, p2}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000Oo(Landroid/graphics/RectF;Landroid/graphics/Matrix;)V

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    iget-object v3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v3}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    invoke-direct {p0, v2, v3}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;)V

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    invoke-virtual {p1}, Landroid/graphics/Canvas;->getWidth()I

    move-result v3

    int-to-float v3, v3

    invoke-virtual {p1}, Landroid/graphics/Canvas;->getHeight()I

    move-result v4

    int-to-float v4, v4

    const/4 v5, 0x0

    invoke-virtual {v2, v5, v5, v3, v4}, Landroid/graphics/RectF;->intersect(FFFF)Z

    move-result v2

    if-nez v2, :cond_4

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    invoke-virtual {v2, v5, v5, v5, v5}, Landroid/graphics/RectF;->set(FFFF)V

    :cond_4
    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    invoke-virtual {v0}, Landroid/graphics/RectF;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_7

    const-string v0, "Layer#saveLayer"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    const/16 v3, 0xff

    invoke-virtual {v2, v3}, Landroid/graphics/Paint;->setAlpha(I)V

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oOoOO:Landroid/graphics/Paint;

    invoke-static {p1, v2, v3}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/RectF;Landroid/graphics/Paint;)V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000oO(Landroid/graphics/Canvas;)V

    invoke-static {v1}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    invoke-virtual {p0, p1, v2, p3}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000Oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    invoke-static {v1}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00o000()Z

    move-result v1

    if-eqz v1, :cond_5

    iget-object v1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->matrix:Landroid/graphics/Matrix;

    invoke-direct {p0, p1, v1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;)V

    :cond_5
    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00o000O()Z

    move-result v1

    const-string v2, "Layer#restoreLayer"

    if-eqz v1, :cond_6

    const-string v1, "Layer#drawMatte"

    invoke-static {v1}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget-object v3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    iget-object v4, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo00:Landroid/graphics/Paint;

    const/16 v5, 0x13

    invoke-static {p1, v3, v4, v5}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/RectF;Landroid/graphics/Paint;I)V

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000oO(Landroid/graphics/Canvas;)V

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o0:Lcom/airbnb/lottie/model/layer/O00000o0;

    invoke-virtual {v0, p1, p2, p3}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V

    invoke-static {v2}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    invoke-static {v2}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    invoke-static {v1}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    :cond_6
    invoke-static {v2}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    invoke-virtual {p1}, Landroid/graphics/Canvas;->restore()V

    invoke-static {v2}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    :cond_7
    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O:Ljava/lang/String;

    invoke-static {p1}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    move-result p1

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/model/layer/O00000o0;->O0000o0O(F)V

    return-void

    :cond_8
    :goto_2
    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0O:Ljava/lang/String;

    invoke-static {p0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-void
.end method

.method public O000000o(Landroid/graphics/RectF;Landroid/graphics/Matrix;Z)V
    .locals 1
    .annotation build Landroidx/annotation/CallSuper;
    .end annotation

    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->rect:Landroid/graphics/RectF;

    const/4 v0, 0x0

    invoke-virtual {p1, v0, v0, v0, v0}, Landroid/graphics/RectF;->set(FFFF)V

    invoke-direct {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->Oo0oo()V

    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0OO:Landroid/graphics/Matrix;

    invoke-virtual {p1, p2}, Landroid/graphics/Matrix;->set(Landroid/graphics/Matrix;)V

    if-eqz p3, :cond_1

    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0oo:Ljava/util/List;

    if-eqz p1, :cond_0

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result p1

    add-int/lit8 p1, p1, -0x1

    :goto_0
    if-ltz p1, :cond_1

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0OO:Landroid/graphics/Matrix;

    iget-object p3, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0oo:Ljava/util/List;

    invoke-interface {p3, p1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p3

    check-cast p3, Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object p3, p3, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p3}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object p3

    invoke-virtual {p2, p3}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    add-int/lit8 p1, p1, -0x1

    goto :goto_0

    :cond_0
    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o:Lcom/airbnb/lottie/model/layer/O00000o0;

    if-eqz p1, :cond_1

    iget-object p2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0OO:Landroid/graphics/Matrix;

    iget-object p1, p1, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object p1

    invoke-virtual {p2, p1}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    :cond_1
    iget-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0OO:Landroid/graphics/Matrix;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->getMatrix()Landroid/graphics/Matrix;

    move-result-object p0

    invoke-virtual {p1, p0}, Landroid/graphics/Matrix;->preConcat(Landroid/graphics/Matrix;)Z

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 0
    .param p1    # Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    if-nez p1, :cond_0

    return-void

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V
    .locals 2

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0, p2}, Lcom/airbnb/lottie/model/O00000oO;->O00000o(Ljava/lang/String;I)Z

    move-result v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->getName()Ljava/lang/String;

    move-result-object v0

    const-string v1, "__container"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p4, v0}, Lcom/airbnb/lottie/model/O00000oO;->O000O0Oo(Ljava/lang/String;)Lcom/airbnb/lottie/model/O00000oO;

    move-result-object p4

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0, p2}, Lcom/airbnb/lottie/model/O00000oO;->O00000Oo(Ljava/lang/String;I)Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-virtual {p4, p0}, Lcom/airbnb/lottie/model/O00000oO;->O000000o(Lcom/airbnb/lottie/model/O00000oo;)Lcom/airbnb/lottie/model/O00000oO;

    move-result-object v0

    invoke-interface {p3, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0, p2}, Lcom/airbnb/lottie/model/O00000oO;->O00000oO(Ljava/lang/String;I)Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0, p2}, Lcom/airbnb/lottie/model/O00000oO;->O00000o0(Ljava/lang/String;I)I

    move-result v0

    add-int/2addr p2, v0

    invoke-virtual {p0, p1, p2, p3, p4}, Lcom/airbnb/lottie/model/layer/O00000o0;->O00000Oo(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V

    :cond_2
    return-void
.end method

.method public O000000o(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 0
    .param p2    # Lcom/airbnb/lottie/O00000oO/O0000Oo;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param
    .annotation build Landroidx/annotation/CallSuper;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->O00000Oo(Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)Z

    return-void
.end method

.method public O000000o(Ljava/util/List;Ljava/util/List;)V
    .locals 0

    return-void
.end method

.method abstract O00000Oo(Landroid/graphics/Canvas;Landroid/graphics/Matrix;I)V
.end method

.method public O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->remove(Ljava/lang/Object;)Z

    return-void
.end method

.method O00000Oo(Lcom/airbnb/lottie/model/O00000oO;ILjava/util/List;Lcom/airbnb/lottie/model/O00000oO;)V
    .locals 0

    return-void
.end method

.method O00000Oo(Lcom/airbnb/lottie/model/layer/O00000o0;)V
    .locals 0
    .param p1    # Lcom/airbnb/lottie/model/layer/O00000o0;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o0:Lcom/airbnb/lottie/model/layer/O00000o0;

    return-void
.end method

.method O00000o0(Lcom/airbnb/lottie/model/layer/O00000o0;)V
    .locals 0
    .param p1    # Lcom/airbnb/lottie/model/layer/O00000o0;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o:Lcom/airbnb/lottie/model/layer/O00000o0;

    return-void
.end method

.method public O00000oO()V
    .locals 0

    invoke-direct {p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->invalidateSelf()V

    return-void
.end method

.method O00o000()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo00o()Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->isEmpty()Z

    move-result p0

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method O00o0000()Lcom/airbnb/lottie/model/layer/O0000O0o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    return-object p0
.end method

.method O00o000O()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o0:Lcom/airbnb/lottie/model/layer/O00000o0;

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {p0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->getName()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method setProgress(F)V
    .locals 3
    .param p1    # F
        .annotation build Landroidx/annotation/FloatRange;
            from = 0.0
            to = 1.0
        .end annotation
    .end param

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00Ooo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;

    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000oOo;->setProgress(F)V

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    move v0, v1

    :goto_0
    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo00o()Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v2

    if-ge v0, v2, :cond_0

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->mask:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;

    invoke-virtual {v2}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00Oo00o()Ljava/util/List;

    move-result-object v2

    invoke-interface {v2, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v2, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->setProgress(F)V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o0()F

    move-result v0

    const/4 v2, 0x0

    cmpl-float v0, v0, v2

    if-eqz v0, :cond_1

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o0()F

    move-result v0

    div-float/2addr p1, v0

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o0:Lcom/airbnb/lottie/model/layer/O00000o0;

    if-eqz v0, :cond_2

    iget-object v0, v0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0Oo:Lcom/airbnb/lottie/model/layer/O0000O0o;

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/layer/O0000O0o;->O00o0()F

    move-result v0

    iget-object v2, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo0o0:Lcom/airbnb/lottie/model/layer/O00000o0;

    mul-float/2addr v0, p1

    invoke-virtual {v2, v0}, Lcom/airbnb/lottie/model/layer/O00000o0;->setProgress(F)V

    :cond_2
    :goto_1
    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-ge v1, v0, :cond_3

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O00000o0;->O00oo:Ljava/util/List;

    invoke-interface {v0, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->setProgress(F)V

    add-int/lit8 v1, v1, 0x1

    goto :goto_1

    :cond_3
    return-void
.end method

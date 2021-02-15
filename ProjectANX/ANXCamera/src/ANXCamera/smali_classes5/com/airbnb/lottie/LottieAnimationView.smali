.class public Lcom/airbnb/lottie/LottieAnimationView;
.super Landroidx/appcompat/widget/AppCompatImageView;
.source ""


# static fields
.field private static final O000o00:Lcom/airbnb/lottie/O000OoO;

.field private static final TAG:Ljava/lang/String; = "LottieAnimationView"


# instance fields
.field private O00000oo:Lcom/airbnb/lottie/O0000o0O;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O000OOo:Ljava/lang/String;

.field private O000OOoO:I
    .annotation build Landroidx/annotation/RawRes;
    .end annotation
.end field

.field private final O000Oo0:Lcom/airbnb/lottie/O000OoO;

.field private final O000Oo00:Lcom/airbnb/lottie/O000OoO;

.field private O000Oo0O:Lcom/airbnb/lottie/O000OoO;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O000Oo0o:I
    .annotation build Landroidx/annotation/DrawableRes;
    .end annotation
.end field

.field private O000OoO:Z

.field private final O000OoO0:Lcom/airbnb/lottie/O000OoO0;

.field private O000OoOO:Z

.field private O000OoOo:Z

.field private O000Ooo:Lcom/airbnb/lottie/RenderMode;

.field private O000Ooo0:Z

.field private O000OooO:Ljava/util/Set;

.field private O000Oooo:I

.field private O000o000:Lcom/airbnb/lottie/O000o000;
    .annotation build Landroidx/annotation/Nullable;
    .end annotation
.end field

.field private O00O0Oo:Z


# direct methods
.method static constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/O00000oo;

    invoke-direct {v0}, Lcom/airbnb/lottie/O00000oo;-><init>()V

    sput-object v0, Lcom/airbnb/lottie/LottieAnimationView;->O000o00:Lcom/airbnb/lottie/O000OoO;

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;)V
    .locals 1

    invoke-direct {p0, p1}, Landroidx/appcompat/widget/AppCompatImageView;-><init>(Landroid/content/Context;)V

    new-instance p1, Lcom/airbnb/lottie/O0000O0o;

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/O0000O0o;-><init>(Lcom/airbnb/lottie/LottieAnimationView;)V

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo00:Lcom/airbnb/lottie/O000OoO;

    new-instance p1, Lcom/airbnb/lottie/O0000OOo;

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/O0000OOo;-><init>(Lcom/airbnb/lottie/LottieAnimationView;)V

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0:Lcom/airbnb/lottie/O000OoO;

    const/4 p1, 0x0

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0o:I

    new-instance v0, Lcom/airbnb/lottie/O000OoO0;

    invoke-direct {v0}, Lcom/airbnb/lottie/O000OoO0;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOo:Z

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo0:Z

    sget-object v0, Lcom/airbnb/lottie/RenderMode;->AUTOMATIC:Lcom/airbnb/lottie/RenderMode;

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo:Lcom/airbnb/lottie/RenderMode;

    new-instance v0, Ljava/util/HashSet;

    invoke-direct {v0}, Ljava/util/HashSet;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OooO:Ljava/util/Set;

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oooo:I

    const/4 p1, 0x0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Landroid/util/AttributeSet;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;)V
    .locals 1

    invoke-direct {p0, p1, p2}, Landroidx/appcompat/widget/AppCompatImageView;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V

    new-instance p1, Lcom/airbnb/lottie/O0000O0o;

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/O0000O0o;-><init>(Lcom/airbnb/lottie/LottieAnimationView;)V

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo00:Lcom/airbnb/lottie/O000OoO;

    new-instance p1, Lcom/airbnb/lottie/O0000OOo;

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/O0000OOo;-><init>(Lcom/airbnb/lottie/LottieAnimationView;)V

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0:Lcom/airbnb/lottie/O000OoO;

    const/4 p1, 0x0

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0o:I

    new-instance v0, Lcom/airbnb/lottie/O000OoO0;

    invoke-direct {v0}, Lcom/airbnb/lottie/O000OoO0;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOo:Z

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo0:Z

    sget-object v0, Lcom/airbnb/lottie/RenderMode;->AUTOMATIC:Lcom/airbnb/lottie/RenderMode;

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo:Lcom/airbnb/lottie/RenderMode;

    new-instance v0, Ljava/util/HashSet;

    invoke-direct {v0}, Ljava/util/HashSet;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OooO:Ljava/util/Set;

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oooo:I

    invoke-direct {p0, p2}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Landroid/util/AttributeSet;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V
    .locals 0

    invoke-direct {p0, p1, p2, p3}, Landroidx/appcompat/widget/AppCompatImageView;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V

    new-instance p1, Lcom/airbnb/lottie/O0000O0o;

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/O0000O0o;-><init>(Lcom/airbnb/lottie/LottieAnimationView;)V

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo00:Lcom/airbnb/lottie/O000OoO;

    new-instance p1, Lcom/airbnb/lottie/O0000OOo;

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/O0000OOo;-><init>(Lcom/airbnb/lottie/LottieAnimationView;)V

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0:Lcom/airbnb/lottie/O000OoO;

    const/4 p1, 0x0

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0o:I

    new-instance p3, Lcom/airbnb/lottie/O000OoO0;

    invoke-direct {p3}, Lcom/airbnb/lottie/O000OoO0;-><init>()V

    iput-object p3, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOo:Z

    const/4 p3, 0x1

    iput-boolean p3, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo0:Z

    sget-object p3, Lcom/airbnb/lottie/RenderMode;->AUTOMATIC:Lcom/airbnb/lottie/RenderMode;

    iput-object p3, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo:Lcom/airbnb/lottie/RenderMode;

    new-instance p3, Ljava/util/HashSet;

    invoke-direct {p3}, Ljava/util/HashSet;-><init>()V

    iput-object p3, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OooO:Ljava/util/Set;

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oooo:I

    invoke-direct {p0, p2}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Landroid/util/AttributeSet;)V

    return-void
.end method

.method static synthetic O000000o(Lcom/airbnb/lottie/LottieAnimationView;)I
    .locals 0

    iget p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0o:I

    return p0
.end method

.method private O000000o(Landroid/util/AttributeSet;)V
    .locals 7
    .param p1    # Landroid/util/AttributeSet;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-virtual {p0}, Landroid/widget/ImageView;->getContext()Landroid/content/Context;

    move-result-object v0

    sget-object v1, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView:[I

    invoke-virtual {v0, p1, v1}, Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;

    move-result-object p1

    invoke-virtual {p0}, Landroid/widget/ImageView;->isInEditMode()Z

    move-result v0

    const/4 v1, 0x1

    const/4 v2, 0x0

    if-nez v0, :cond_5

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_cacheComposition:I

    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getBoolean(IZ)Z

    move-result v0

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo0:Z

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_rawRes:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v0

    sget v3, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_fileName:I

    invoke-virtual {p1, v3}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v3

    sget v4, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_url:I

    invoke-virtual {p1, v4}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v4

    if-eqz v0, :cond_1

    if-nez v3, :cond_0

    goto :goto_0

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once."

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_1
    :goto_0
    if-eqz v0, :cond_2

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_rawRes:I

    invoke-virtual {p1, v0, v2}, Landroid/content/res/TypedArray;->getResourceId(II)I

    move-result v0

    if-eqz v0, :cond_4

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000O0o(I)V

    goto :goto_1

    :cond_2
    if-eqz v3, :cond_3

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_fileName:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getString(I)Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_4

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000Oo(Ljava/lang/String;)V

    goto :goto_1

    :cond_3
    if-eqz v4, :cond_4

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_url:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getString(I)Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_4

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000Ooo(Ljava/lang/String;)V

    :cond_4
    :goto_1
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_fallbackRes:I

    invoke-virtual {p1, v0, v2}, Landroid/content/res/TypedArray;->getResourceId(II)I

    move-result v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000OOo(I)V

    :cond_5
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_autoPlay:I

    invoke-virtual {p1, v0, v2}, Landroid/content/res/TypedArray;->getBoolean(IZ)Z

    move-result v0

    if-eqz v0, :cond_6

    iput-boolean v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    iput-boolean v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOo:Z

    :cond_6
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_loop:I

    invoke-virtual {p1, v0, v2}, Landroid/content/res/TypedArray;->getBoolean(IZ)Z

    move-result v0

    const/4 v3, -0x1

    if-eqz v0, :cond_7

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0, v3}, Lcom/airbnb/lottie/O000OoO0;->setRepeatCount(I)V

    :cond_7
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_repeatMode:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v0

    if-eqz v0, :cond_8

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_repeatMode:I

    invoke-virtual {p1, v0, v1}, Landroid/content/res/TypedArray;->getInt(II)I

    move-result v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setRepeatMode(I)V

    :cond_8
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_repeatCount:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v0

    if-eqz v0, :cond_9

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_repeatCount:I

    invoke-virtual {p1, v0, v3}, Landroid/content/res/TypedArray;->getInt(II)I

    move-result v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setRepeatCount(I)V

    :cond_9
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_speed:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v0

    const/high16 v3, 0x3f800000    # 1.0f

    if-eqz v0, :cond_a

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_speed:I

    invoke-virtual {p1, v0, v3}, Landroid/content/res/TypedArray;->getFloat(IF)F

    move-result v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setSpeed(F)V

    :cond_a
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_imageAssetsFolder:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->getString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000o00(Ljava/lang/String;)V

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_progress:I

    const/4 v4, 0x0

    invoke-virtual {p1, v0, v4}, Landroid/content/res/TypedArray;->getFloat(IF)F

    move-result v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setProgress(F)V

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_enableMergePathsForKitKatAndAbove:I

    invoke-virtual {p1, v0, v2}, Landroid/content/res/TypedArray;->getBoolean(IZ)Z

    move-result v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O00000oo(Z)V

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_colorFilter:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v0

    if-eqz v0, :cond_b

    new-instance v0, Lcom/airbnb/lottie/O000o0O;

    sget v5, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_colorFilter:I

    invoke-virtual {p1, v5, v2}, Landroid/content/res/TypedArray;->getColor(II)I

    move-result v5

    invoke-direct {v0, v5}, Lcom/airbnb/lottie/O000o0O;-><init>(I)V

    new-instance v5, Lcom/airbnb/lottie/model/O00000oO;

    const-string v6, "**"

    filled-new-array {v6}, [Ljava/lang/String;

    move-result-object v6

    invoke-direct {v5, v6}, Lcom/airbnb/lottie/model/O00000oO;-><init>([Ljava/lang/String;)V

    new-instance v6, Lcom/airbnb/lottie/O00000oO/O0000Oo;

    invoke-direct {v6, v0}, Lcom/airbnb/lottie/O00000oO/O0000Oo;-><init>(Ljava/lang/Object;)V

    sget-object v0, Lcom/airbnb/lottie/O000Ooo0;->OO00ooO:Landroid/graphics/ColorFilter;

    invoke-virtual {p0, v5, v0, v6}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    :cond_b
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_scale:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v0

    if-eqz v0, :cond_c

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    sget v5, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_scale:I

    invoke-virtual {p1, v5, v3}, Landroid/content/res/TypedArray;->getFloat(IF)F

    move-result v3

    invoke-virtual {v0, v3}, Lcom/airbnb/lottie/O000OoO0;->setScale(F)V

    :cond_c
    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_renderMode:I

    invoke-virtual {p1, v0}, Landroid/content/res/TypedArray;->hasValue(I)Z

    move-result v0

    if-eqz v0, :cond_e

    sget v0, Lcom/airbnb/lottie/R$styleable;->LottieAnimationView_lottie_renderMode:I

    sget-object v3, Lcom/airbnb/lottie/RenderMode;->AUTOMATIC:Lcom/airbnb/lottie/RenderMode;

    invoke-virtual {v3}, Ljava/lang/Enum;->ordinal()I

    move-result v3

    invoke-virtual {p1, v0, v3}, Landroid/content/res/TypedArray;->getInt(II)I

    move-result v0

    invoke-static {}, Lcom/airbnb/lottie/RenderMode;->values()[Lcom/airbnb/lottie/RenderMode;

    move-result-object v3

    array-length v3, v3

    if-lt v0, v3, :cond_d

    sget-object v0, Lcom/airbnb/lottie/RenderMode;->AUTOMATIC:Lcom/airbnb/lottie/RenderMode;

    invoke-virtual {v0}, Ljava/lang/Enum;->ordinal()I

    move-result v0

    :cond_d
    invoke-static {}, Lcom/airbnb/lottie/RenderMode;->values()[Lcom/airbnb/lottie/RenderMode;

    move-result-object v3

    aget-object v0, v3, v0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Lcom/airbnb/lottie/RenderMode;)V

    :cond_e
    invoke-virtual {p0}, Landroid/widget/ImageView;->getScaleType()Landroid/widget/ImageView$ScaleType;

    move-result-object v0

    if-eqz v0, :cond_f

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Landroid/widget/ImageView;->getScaleType()Landroid/widget/ImageView$ScaleType;

    move-result-object v3

    invoke-virtual {v0, v3}, Lcom/airbnb/lottie/O000OoO0;->setScaleType(Landroid/widget/ImageView$ScaleType;)V

    :cond_f
    invoke-virtual {p1}, Landroid/content/res/TypedArray;->recycle()V

    iget-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Landroid/widget/ImageView;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-static {v0}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00000Oo(Landroid/content/Context;)F

    move-result v0

    cmpl-float v0, v0, v4

    if-eqz v0, :cond_10

    move v2, v1

    :cond_10
    invoke-static {v2}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v0

    invoke-virtual {p1, v0}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Ljava/lang/Boolean;)V

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO()V

    iput-boolean v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00O0Oo:Z

    return-void
.end method

.method static synthetic O00000Oo(Lcom/airbnb/lottie/LottieAnimationView;)Lcom/airbnb/lottie/O000OoO;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0O:Lcom/airbnb/lottie/O000OoO;

    return-object p0
.end method

.method private O00000Oo(Lcom/airbnb/lottie/O000o000;)V
    .locals 1

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000o0()V

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO0o()V

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo00:Lcom/airbnb/lottie/O000OoO;

    invoke-virtual {p1, v0}, Lcom/airbnb/lottie/O000o000;->O00000o0(Lcom/airbnb/lottie/O000OoO;)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0:Lcom/airbnb/lottie/O000OoO;

    invoke-virtual {p1, v0}, Lcom/airbnb/lottie/O000o000;->O00000Oo(Lcom/airbnb/lottie/O000OoO;)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000o000:Lcom/airbnb/lottie/O000o000;

    return-void
.end method

.method private O0000o0()V
    .locals 1

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000o0()V

    return-void
.end method

.method private Oo0oO()V
    .locals 5

    sget-object v0, Lcom/airbnb/lottie/O0000Oo;->O000o0OO:[I

    iget-object v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo:Lcom/airbnb/lottie/RenderMode;

    invoke-virtual {v1}, Ljava/lang/Enum;->ordinal()I

    move-result v1

    aget v0, v0, v1

    const/4 v1, 0x2

    const/4 v2, 0x1

    if-eq v0, v2, :cond_5

    if-eq v0, v1, :cond_0

    const/4 v3, 0x3

    if-eq v0, v3, :cond_1

    :cond_0
    move v1, v2

    goto :goto_1

    :cond_1
    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    const/4 v3, 0x0

    if-eqz v0, :cond_2

    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->O00O0oOo()Z

    move-result v0

    if-eqz v0, :cond_2

    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v4, 0x1c

    if-ge v0, v4, :cond_2

    goto :goto_0

    :cond_2
    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-eqz v0, :cond_3

    invoke-virtual {v0}, Lcom/airbnb/lottie/O0000o0O;->O00O0oO0()I

    move-result v0

    const/4 v4, 0x4

    if-le v0, v4, :cond_3

    goto :goto_0

    :cond_3
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v4, 0x15

    if-ge v0, v4, :cond_4

    goto :goto_0

    :cond_4
    move v3, v2

    :goto_0
    if-eqz v3, :cond_0

    :cond_5
    :goto_1
    invoke-virtual {p0}, Landroid/widget/ImageView;->getLayerType()I

    move-result v0

    if-eq v1, v0, :cond_6

    const/4 v0, 0x0

    invoke-virtual {p0, v1, v0}, Landroid/widget/ImageView;->setLayerType(ILandroid/graphics/Paint;)V

    :cond_6
    return-void
.end method

.method private Oo0oO0o()V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000o000:Lcom/airbnb/lottie/O000o000;

    if-eqz v0, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo00:Lcom/airbnb/lottie/O000OoO;

    invoke-virtual {v0, v1}, Lcom/airbnb/lottie/O000o000;->O00000oO(Lcom/airbnb/lottie/O000OoO;)Lcom/airbnb/lottie/O000o000;

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000o000:Lcom/airbnb/lottie/O000o000;

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0:Lcom/airbnb/lottie/O000OoO;

    invoke-virtual {v0, p0}, Lcom/airbnb/lottie/O000o000;->O00000o(Lcom/airbnb/lottie/O000OoO;)Lcom/airbnb/lottie/O000o000;

    :cond_0
    return-void
.end method

.method static synthetic access$200()Lcom/airbnb/lottie/O000OoO;
    .locals 1

    sget-object v0, Lcom/airbnb/lottie/LottieAnimationView;->O000o00:Lcom/airbnb/lottie/O000OoO;

    return-object v0
.end method


# virtual methods
.method public O000000o(Ljava/lang/String;Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;
    .locals 0
    .param p2    # Landroid/graphics/Bitmap;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Ljava/lang/String;Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;

    move-result-object p0

    return-object p0
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;)Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/model/O00000oO;)Ljava/util/List;

    move-result-object p0

    return-object p0
.end method

.method public O000000o(I)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(I)V

    return-void
.end method

.method public O000000o(Landroid/animation/Animator$AnimatorListener;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Landroid/animation/Animator$AnimatorListener;)V

    return-void
.end method

.method public O000000o(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O00000o0;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/O00000o0;)V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O00000o;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/O00000o;)V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O000OoO;)V
    .locals 0
    .param p1    # Lcom/airbnb/lottie/O000OoO;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0O:Lcom/airbnb/lottie/O000OoO;

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O000o0OO;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/O000o0OO;)V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/RenderMode;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo:Lcom/airbnb/lottie/RenderMode;

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO()V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1, p2, p3}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Ooo;)V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    new-instance v1, Lcom/airbnb/lottie/O0000Oo0;

    invoke-direct {v1, p0, p3}, Lcom/airbnb/lottie/O0000Oo0;-><init>(Lcom/airbnb/lottie/LottieAnimationView;Lcom/airbnb/lottie/O00000oO/O0000Ooo;)V

    invoke-virtual {v0, p1, p2, v1}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Lcom/airbnb/lottie/model/O00000oO;Ljava/lang/Object;Lcom/airbnb/lottie/O00000oO/O0000Oo;)V

    return-void
.end method

.method public O000000o(Ljava/io/InputStream;Ljava/lang/String;)V
    .locals 0
    .param p2    # Ljava/lang/String;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-static {p1, p2}, Lcom/airbnb/lottie/O00oOooo;->O00000Oo(Ljava/io/InputStream;Ljava/lang/String;)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->O00000Oo(Lcom/airbnb/lottie/O000o000;)V

    return-void
.end method

.method public O000000o(Ljava/lang/String;Ljava/lang/String;Z)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1, p2, p3}, Lcom/airbnb/lottie/O000OoO0;->O000000o(Ljava/lang/String;Ljava/lang/String;Z)V

    return-void
.end method

.method public O000000o(Lcom/airbnb/lottie/O000OoOo;)Z
    .locals 1
    .param p1    # Lcom/airbnb/lottie/O000OoOo;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-eqz v0, :cond_0

    invoke-interface {p1, v0}, Lcom/airbnb/lottie/O000OoOo;->O000000o(Lcom/airbnb/lottie/O0000o0O;)V

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OooO:Ljava/util/Set;

    invoke-interface {p0, p1}, Ljava/util/Set;->add(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public O00000Oo(FF)V
    .locals 0
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

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000OoO0;->O00000Oo(FF)V

    return-void
.end method

.method public O00000Oo(Landroid/animation/Animator$AnimatorListener;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000Oo(Landroid/animation/Animator$AnimatorListener;)V

    return-void
.end method

.method public O00000Oo(Ljava/lang/String;Ljava/lang/String;)V
    .locals 1
    .param p2    # Ljava/lang/String;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    new-instance v0, Ljava/io/ByteArrayInputStream;

    invoke-virtual {p1}, Ljava/lang/String;->getBytes()[B

    move-result-object p1

    invoke-direct {v0, p1}, Ljava/io/ByteArrayInputStream;-><init>([B)V

    invoke-virtual {p0, v0, p2}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Ljava/io/InputStream;Ljava/lang/String;)V

    return-void
.end method

.method public O00000Oo(Lcom/airbnb/lottie/O000OoOo;)Z
    .locals 0
    .param p1    # Lcom/airbnb/lottie/O000OoOo;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OooO:Ljava/util/Set;

    invoke-interface {p0, p1}, Ljava/util/Set;->remove(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public O00000o(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000o(Ljava/lang/String;)V

    return-void
.end method

.method public O00000o0(Lcom/airbnb/lottie/O0000o0O;)V
    .locals 3
    .param p1    # Lcom/airbnb/lottie/O0000o0O;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    sget-boolean v0, Lcom/airbnb/lottie/O00000oO;->DBG:Z

    if-eqz v0, :cond_0

    sget-object v0, Lcom/airbnb/lottie/LottieAnimationView;->TAG:Ljava/lang/String;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "Set Composition \n"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v1}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    :cond_0
    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0, p0}, Landroid/graphics/drawable/Drawable;->setCallback(Landroid/graphics/drawable/Drawable$Callback;)V

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000o0(Lcom/airbnb/lottie/O0000o0O;)Z

    move-result v0

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO()V

    invoke-virtual {p0}, Landroid/widget/ImageView;->getDrawable()Landroid/graphics/drawable/Drawable;

    move-result-object v1

    iget-object v2, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    if-ne v1, v2, :cond_1

    if-nez v0, :cond_1

    return-void

    :cond_1
    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    invoke-virtual {p0}, Landroid/widget/ImageView;->getVisibility()I

    move-result v0

    invoke-virtual {p0, p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->onVisibilityChanged(Landroid/view/View;I)V

    invoke-virtual {p0}, Landroid/widget/ImageView;->requestLayout()V

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OooO:Ljava/util/Set;

    invoke-interface {p0}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/airbnb/lottie/O000OoOo;

    invoke-interface {v0, p1}, Lcom/airbnb/lottie/O000OoOo;->O000000o(Lcom/airbnb/lottie/O0000o0O;)V

    goto :goto_0

    :cond_2
    return-void
.end method

.method public O00000o0(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000o0(Ljava/lang/String;)V

    return-void
.end method

.method public O00000oO(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000oO(Ljava/lang/String;)V

    return-void
.end method

.method public O00000oo(F)V
    .locals 0
    .param p1    # F
        .annotation build Landroidx/annotation/FloatRange;
            from = 0.0
            to = 1.0
        .end annotation
    .end param

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(F)V

    return-void
.end method

.method public O00000oo(I)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(I)V

    return-void
.end method

.method public O00000oo(II)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1, p2}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(II)V

    return-void
.end method

.method public O00000oo(Z)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000oo(Z)V

    return-void
.end method

.method public O0000O0o(F)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O0000O0o(F)V

    return-void
.end method

.method public O0000O0o(I)V
    .locals 2
    .param p1    # I
        .annotation build Landroidx/annotation/RawRes;
        .end annotation
    .end param

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOoO:I

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOo:Ljava/lang/String;

    iget-boolean v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo0:Z

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Landroid/widget/ImageView;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-static {v0, p1}, Lcom/airbnb/lottie/O00oOooo;->O000000o(Landroid/content/Context;I)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Landroid/widget/ImageView;->getContext()Landroid/content/Context;

    move-result-object v1

    invoke-static {v1, p1, v0}, Lcom/airbnb/lottie/O00oOooo;->O000000o(Landroid/content/Context;ILjava/lang/String;)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    :goto_0
    invoke-direct {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->O00000Oo(Lcom/airbnb/lottie/O000o000;)V

    return-void
.end method

.method public O0000O0o(Z)V
    .locals 0
    .annotation runtime Ljava/lang/Deprecated;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    if-eqz p1, :cond_0

    const/4 p1, -0x1

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    :goto_0
    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->setRepeatCount(I)V

    return-void
.end method

.method public O0000OOo(I)V
    .locals 0
    .param p1    # I
        .annotation build Landroidx/annotation/DrawableRes;
        .end annotation
    .end param

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oo0o:I

    return-void
.end method

.method public O0000OOo(Z)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O0000OOo(Z)V

    return-void
.end method

.method public O0000Oo(Ljava/lang/String;)V
    .locals 2

    iput-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOo:Ljava/lang/String;

    const/4 v0, 0x0

    iput v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOoO:I

    iget-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo0:Z

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Landroid/widget/ImageView;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-static {v0, p1}, Lcom/airbnb/lottie/O00oOooo;->O00000Oo(Landroid/content/Context;Ljava/lang/String;)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Landroid/widget/ImageView;->getContext()Landroid/content/Context;

    move-result-object v0

    const/4 v1, 0x0

    invoke-static {v0, p1, v1}, Lcom/airbnb/lottie/O00oOooo;->O000000o(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    :goto_0
    invoke-direct {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->O00000Oo(Lcom/airbnb/lottie/O000o000;)V

    return-void
.end method

.method public O0000Oo(Z)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O0000Oo(Z)V

    return-void
.end method

.method public O0000Oo0(Z)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O0000Oo0(Z)V

    return-void
.end method

.method public O0000OoO(Ljava/lang/String;)V
    .locals 1
    .annotation runtime Ljava/lang/Deprecated;
    .end annotation

    const/4 v0, 0x0

    invoke-virtual {p0, p1, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O00000Oo(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public O0000OoO(Z)V
    .locals 0

    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo0:Z

    return-void
.end method

.method public O0000Ooo(Ljava/lang/String;)V
    .locals 2

    iget-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Ooo0:Z

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Landroid/widget/ImageView;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-static {v0, p1}, Lcom/airbnb/lottie/O00oOooo;->O00000o(Landroid/content/Context;Ljava/lang/String;)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Landroid/widget/ImageView;->getContext()Landroid/content/Context;

    move-result-object v0

    const/4 v1, 0x0

    invoke-static {v0, p1, v1}, Lcom/airbnb/lottie/O00oOooo;->O00000o0(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)Lcom/airbnb/lottie/O000o000;

    move-result-object p1

    :goto_0
    invoke-direct {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->O00000Oo(Lcom/airbnb/lottie/O000o000;)V

    return-void
.end method

.method public O0000o()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000o()F

    move-result p0

    return p0
.end method

.method public O0000o00(Ljava/lang/String;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000Oo(Ljava/lang/String;)V

    return-void
.end method

.method public O0000o0o()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000o0o()F

    move-result p0

    return p0
.end method

.method public O0000oO()V
    .locals 1
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    invoke-virtual {p0}, Landroid/widget/ImageView;->isShown()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->O0000oO()V

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO()V

    goto :goto_0

    :cond_0
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    :goto_0
    return-void
.end method

.method public O0000oO0()V
    .locals 1
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOo:Z

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->O0000oO0()V

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO()V

    return-void
.end method

.method public O0000oOo()V
    .locals 1
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    invoke-virtual {p0}, Landroid/widget/ImageView;->isShown()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->O0000oOo()V

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO()V

    goto :goto_0

    :cond_0
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    :goto_0
    return-void
.end method

.method public O0000oo0()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O0000oo0()V

    return-void
.end method

.method public O000O0OO()Lcom/airbnb/lottie/O0000o0O;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    return-object p0
.end method

.method public O000O0Oo()Ljava/lang/String;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O000O0Oo()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O000O0o()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O000O0o()Z

    move-result p0

    return p0
.end method

.method public O000O0oO()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O000O0oO()Z

    move-result p0

    return p0
.end method

.method public O000OO00()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O000OO00()Z

    move-result p0

    return p0
.end method

.method public O000OO0o()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O000OO0o()V

    return-void
.end method

.method public O000Oo0()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OooO:Ljava/util/Set;

    invoke-interface {p0}, Ljava/util/Set;->clear()V

    return-void
.end method

.method public O00oOoOo()Lcom/airbnb/lottie/O000o0;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O00oOoOo()Lcom/airbnb/lottie/O000o0;

    move-result-object p0

    return-object p0
.end method

.method public O00oOooo()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->O00oOooo()V

    return-void
.end method

.method public buildDrawingCache(Z)V
    .locals 3

    const-string v0, "buildDrawingCache"

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->beginSection(Ljava/lang/String;)V

    iget v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oooo:I

    const/4 v2, 0x1

    add-int/2addr v1, v2

    iput v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oooo:I

    invoke-super {p0, p1}, Landroid/widget/ImageView;->buildDrawingCache(Z)V

    iget v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oooo:I

    if-ne v1, v2, :cond_0

    invoke-virtual {p0}, Landroid/widget/ImageView;->getWidth()I

    move-result v1

    if-lez v1, :cond_0

    invoke-virtual {p0}, Landroid/widget/ImageView;->getHeight()I

    move-result v1

    if-lez v1, :cond_0

    invoke-virtual {p0}, Landroid/widget/ImageView;->getLayerType()I

    move-result v1

    if-ne v1, v2, :cond_0

    invoke-virtual {p0, p1}, Landroid/widget/ImageView;->getDrawingCache(Z)Landroid/graphics/Bitmap;

    move-result-object p1

    if-nez p1, :cond_0

    sget-object p1, Lcom/airbnb/lottie/RenderMode;->HARDWARE:Lcom/airbnb/lottie/RenderMode;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->O000000o(Lcom/airbnb/lottie/RenderMode;)V

    :cond_0
    iget p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oooo:I

    sub-int/2addr p1, v2

    iput p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000Oooo:I

    invoke-static {v0}, Lcom/airbnb/lottie/O00000oO;->O0000oOo(Ljava/lang/String;)F

    return-void
.end method

.method public cancelAnimation()V
    .locals 1
    .annotation build Landroidx/annotation/MainThread;
    .end annotation

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->cancelAnimation()V

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO()V

    return-void
.end method

.method public getDuration()J
    .locals 2

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00000oo:Lcom/airbnb/lottie/O0000o0O;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/O0000o0O;->getDuration()F

    move-result p0

    float-to-long v0, p0

    goto :goto_0

    :cond_0
    const-wide/16 v0, 0x0

    :goto_0
    return-wide v0
.end method

.method public getFrame()I
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getFrame()I

    move-result p0

    return p0
.end method

.method public getProgress()F
    .locals 0
    .annotation build Landroidx/annotation/FloatRange;
        from = 0.0
        to = 1.0
    .end annotation

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getProgress()F

    move-result p0

    return p0
.end method

.method public getRepeatCount()I
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getRepeatCount()I

    move-result p0

    return p0
.end method

.method public getRepeatMode()I
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getRepeatMode()I

    move-result p0

    return p0
.end method

.method public getScale()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getScale()F

    move-result p0

    return p0
.end method

.method public getSpeed()F
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getSpeed()F

    move-result p0

    return p0
.end method

.method public invalidateDrawable(Landroid/graphics/drawable/Drawable;)V
    .locals 2
    .param p1    # Landroid/graphics/drawable/Drawable;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-virtual {p0}, Landroid/widget/ImageView;->getDrawable()Landroid/graphics/drawable/Drawable;

    move-result-object v0

    iget-object v1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    if-ne v0, v1, :cond_0

    invoke-super {p0, v1}, Landroid/widget/ImageView;->invalidateDrawable(Landroid/graphics/drawable/Drawable;)V

    goto :goto_0

    :cond_0
    invoke-super {p0, p1}, Landroid/widget/ImageView;->invalidateDrawable(Landroid/graphics/drawable/Drawable;)V

    :goto_0
    return-void
.end method

.method public isAnimating()Z
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->isAnimating()Z

    move-result p0

    return p0
.end method

.method protected onAttachedToWindow()V
    .locals 2

    invoke-super {p0}, Landroid/widget/ImageView;->onAttachedToWindow()V

    iget-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOo:Z

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    if-eqz v0, :cond_1

    :cond_0
    invoke-virtual {p0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000oO()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOo:Z

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    :cond_1
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x17

    if-ge v0, v1, :cond_2

    invoke-virtual {p0}, Landroid/widget/ImageView;->getVisibility()I

    move-result v0

    invoke-virtual {p0, p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->onVisibilityChanged(Landroid/view/View;I)V

    :cond_2
    return-void
.end method

.method protected onDetachedFromWindow()V
    .locals 1

    invoke-virtual {p0}, Lcom/airbnb/lottie/LottieAnimationView;->isAnimating()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Lcom/airbnb/lottie/LottieAnimationView;->cancelAnimation()V

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    :cond_0
    invoke-super {p0}, Landroid/widget/ImageView;->onDetachedFromWindow()V

    return-void
.end method

.method protected onRestoreInstanceState(Landroid/os/Parcelable;)V
    .locals 2

    instance-of v0, p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;

    if-nez v0, :cond_0

    invoke-super {p0, p1}, Landroid/widget/ImageView;->onRestoreInstanceState(Landroid/os/Parcelable;)V

    return-void

    :cond_0
    check-cast p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;

    invoke-virtual {p1}, Landroid/view/View$BaseSavedState;->getSuperState()Landroid/os/Parcelable;

    move-result-object v0

    invoke-super {p0, v0}, Landroid/widget/ImageView;->onRestoreInstanceState(Landroid/os/Parcelable;)V

    iget-object v0, p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->O000OOo:Ljava/lang/String;

    iput-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOo:Ljava/lang/String;

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOo:Ljava/lang/String;

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_1

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOo:Ljava/lang/String;

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000Oo(Ljava/lang/String;)V

    :cond_1
    iget v0, p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->O000OOoO:I

    iput v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOoO:I

    iget v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOoO:I

    if-eqz v0, :cond_2

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000O0o(I)V

    :cond_2
    iget v0, p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->progress:F

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setProgress(F)V

    iget-boolean v0, p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->O000OOoo:Z

    if-eqz v0, :cond_3

    invoke-virtual {p0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000oO()V

    :cond_3
    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    iget-object v1, p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->O0000o00:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lcom/airbnb/lottie/O000OoO0;->O00000Oo(Ljava/lang/String;)V

    iget v0, p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->repeatMode:I

    invoke-virtual {p0, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setRepeatMode(I)V

    iget p1, p1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->repeatCount:I

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->setRepeatCount(I)V

    return-void
.end method

.method protected onSaveInstanceState()Landroid/os/Parcelable;
    .locals 2

    invoke-super {p0}, Landroid/widget/ImageView;->onSaveInstanceState()Landroid/os/Parcelable;

    move-result-object v0

    new-instance v1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;

    invoke-direct {v1, v0}, Lcom/airbnb/lottie/LottieAnimationView$SavedState;-><init>(Landroid/os/Parcelable;)V

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOo:Ljava/lang/String;

    iput-object v0, v1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->O000OOo:Ljava/lang/String;

    iget v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OOoO:I

    iput v0, v1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->O000OOoO:I

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->getProgress()F

    move-result v0

    iput v0, v1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->progress:F

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->isAnimating()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-static {p0}, Landroidx/core/view/ViewCompat;->isAttachedToWindow(Landroid/view/View;)Z

    move-result v0

    if-nez v0, :cond_0

    iget-boolean v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoOO:Z

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, 0x1

    :goto_1
    iput-boolean v0, v1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->O000OOoo:Z

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->O000O0Oo()Ljava/lang/String;

    move-result-object v0

    iput-object v0, v1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->O0000o00:Ljava/lang/String;

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0}, Lcom/airbnb/lottie/O000OoO0;->getRepeatMode()I

    move-result v0

    iput v0, v1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->repeatMode:I

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->getRepeatCount()I

    move-result p0

    iput p0, v1, Lcom/airbnb/lottie/LottieAnimationView$SavedState;->repeatCount:I

    return-object v1
.end method

.method protected onVisibilityChanged(Landroid/view/View;I)V
    .locals 0
    .param p1    # Landroid/view/View;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    iget-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O00O0Oo:Z

    if-nez p1, :cond_0

    return-void

    :cond_0
    invoke-virtual {p0}, Landroid/widget/ImageView;->isShown()Z

    move-result p1

    if-eqz p1, :cond_1

    iget-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    if-eqz p1, :cond_2

    invoke-virtual {p0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000oOo()V

    const/4 p1, 0x0

    goto :goto_0

    :cond_1
    invoke-virtual {p0}, Lcom/airbnb/lottie/LottieAnimationView;->isAnimating()Z

    move-result p1

    if-eqz p1, :cond_2

    invoke-virtual {p0}, Lcom/airbnb/lottie/LottieAnimationView;->O0000oO0()V

    const/4 p1, 0x1

    :goto_0
    iput-boolean p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO:Z

    :cond_2
    return-void
.end method

.method public removeAllUpdateListeners()V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000OoO0;->removeAllUpdateListeners()V

    return-void
.end method

.method public removeUpdateListener(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->O00000Oo(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V

    return-void
.end method

.method public setFrame(I)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->setFrame(I)V

    return-void
.end method

.method public setImageBitmap(Landroid/graphics/Bitmap;)V
    .locals 0

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO0o()V

    invoke-super {p0, p1}, Landroidx/appcompat/widget/AppCompatImageView;->setImageBitmap(Landroid/graphics/Bitmap;)V

    return-void
.end method

.method public setImageDrawable(Landroid/graphics/drawable/Drawable;)V
    .locals 0

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO0o()V

    invoke-super {p0, p1}, Landroidx/appcompat/widget/AppCompatImageView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    return-void
.end method

.method public setImageResource(I)V
    .locals 0

    invoke-direct {p0}, Lcom/airbnb/lottie/LottieAnimationView;->Oo0oO0o()V

    invoke-super {p0, p1}, Landroidx/appcompat/widget/AppCompatImageView;->setImageResource(I)V

    return-void
.end method

.method public setProgress(F)V
    .locals 0
    .param p1    # F
        .annotation build Landroidx/annotation/FloatRange;
            from = 0.0
            to = 1.0
        .end annotation
    .end param

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->setProgress(F)V

    return-void
.end method

.method public setRepeatCount(I)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->setRepeatCount(I)V

    return-void
.end method

.method public setRepeatMode(I)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->setRepeatMode(I)V

    return-void
.end method

.method public setScale(F)V
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {v0, p1}, Lcom/airbnb/lottie/O000OoO0;->setScale(F)V

    invoke-virtual {p0}, Landroid/widget/ImageView;->getDrawable()Landroid/graphics/drawable/Drawable;

    move-result-object p1

    iget-object v0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    if-ne p1, v0, :cond_0

    const/4 p1, 0x0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    iget-object p1, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/LottieAnimationView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    :cond_0
    return-void
.end method

.method public setScaleType(Landroid/widget/ImageView$ScaleType;)V
    .locals 0

    invoke-super {p0, p1}, Landroid/widget/ImageView;->setScaleType(Landroid/widget/ImageView$ScaleType;)V

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->setScaleType(Landroid/widget/ImageView$ScaleType;)V

    :cond_0
    return-void
.end method

.method public setSpeed(F)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/LottieAnimationView;->O000OoO0:Lcom/airbnb/lottie/O000OoO0;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O000OoO0;->setSpeed(F)V

    return-void
.end method

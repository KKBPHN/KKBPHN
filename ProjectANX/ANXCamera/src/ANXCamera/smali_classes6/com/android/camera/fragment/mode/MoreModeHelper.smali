.class public Lcom/android/camera/fragment/mode/MoreModeHelper;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final TAG:Ljava/lang/String; = "MoreModeHelper"


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static getHeaderHeightForNormal(Landroid/content/Context;III)I
    .locals 6

    invoke-static {}, Lcom/android/camera/fragment/mode/MoreModeHelper;->isLandscapeMode()Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    const/4 v0, 0x3

    if-eq p1, v0, :cond_1

    if-eqz p1, :cond_1

    return v1

    :cond_1
    invoke-static {}, Lcom/android/camera/Display;->getMaxViewFinderRect()Landroid/graphics/Rect;

    move-result-object v2

    invoke-virtual {v2}, Landroid/graphics/Rect;->height()I

    move-result v2

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v3

    invoke-virtual {v3}, Lcom/android/camera/data/data/runing/DataItemRunning;->getUiStyle()I

    move-result v3

    invoke-static {v3}, Lcom/android/camera/Display;->getDisplayRect(I)Landroid/graphics/Rect;

    move-result-object v3

    invoke-virtual {v3}, Landroid/graphics/Rect;->height()I

    move-result v3

    invoke-static {v2, v3}, Ljava/lang/Math;->min(II)I

    move-result v2

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    if-ne p1, v0, :cond_2

    const v4, 0x7f0703fd

    goto :goto_0

    :cond_2
    const v4, 0x7f070407

    :goto_0
    invoke-virtual {v3, v4}, Landroid/content/res/Resources;->getDimensionPixelOffset(I)I

    move-result v3

    sub-int/2addr v2, v3

    div-int v3, p3, p2

    rem-int v4, p3, p2

    const/4 v5, 0x1

    if-nez v4, :cond_3

    move v4, v1

    goto :goto_1

    :cond_3
    move v4, v5

    :goto_1
    add-int/2addr v3, v4

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v4

    invoke-virtual {v4}, Lcom/android/camera/data/data/runing/DataItemRunning;->getUiStyle()I

    move-result v4

    if-ne p1, v0, :cond_4

    goto :goto_2

    :cond_4
    move v5, v1

    :goto_2
    invoke-static {v4, v5}, Lcom/android/camera/Display;->getMoreModeTabRow(IZ)I

    move-result v4

    if-lt v3, v4, :cond_6

    if-ne p1, v0, :cond_5

    return v1

    :cond_5
    move v3, v4

    :cond_6
    invoke-static {p0, p1}, Lcom/android/camera/fragment/mode/MoreModeHelper;->getModeHeight(Landroid/content/Context;I)I

    move-result p0

    sget-object v0, Lcom/android/camera/fragment/mode/MoreModeHelper;->TAG:Ljava/lang/String;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "getHeaderHeightForNormal "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    mul-int/2addr v3, p0

    sub-int/2addr v2, v3

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, ", type = "

    invoke-virtual {v4, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, ", preLine = "

    invoke-virtual {v4, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p0, ", size = "

    invoke-virtual {v4, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, p3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-static {v0, p0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {v2, v1}, Ljava/lang/Math;->max(II)I

    move-result p0

    return p0
.end method

.method public static getModeHeight(Landroid/content/Context;I)I
    .locals 1

    if-nez p1, :cond_0

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object p1

    invoke-virtual {p1}, Lcom/android/camera/data/data/runing/DataItemRunning;->getUiStyle()I

    move-result p1

    const/4 v0, 0x0

    invoke-static {p1, v0}, Lcom/android/camera/Display;->getMoreModeTabMarginVer(IZ)I

    move-result p1

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p0

    const v0, 0x7f070400

    invoke-virtual {p0, v0}, Landroid/content/res/Resources;->getDimensionPixelOffset(I)I

    move-result p0

    add-int/2addr p1, p0

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p0

    const p1, 0x7f0703f9

    invoke-virtual {p0, p1}, Landroid/content/res/Resources;->getDimensionPixelOffset(I)I

    move-result p0

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object p1

    invoke-virtual {p1}, Lcom/android/camera/data/data/runing/DataItemRunning;->getUiStyle()I

    move-result p1

    const/4 v0, 0x1

    invoke-static {p1, v0}, Lcom/android/camera/Display;->getMoreModeTabMarginVer(IZ)I

    move-result p1

    add-int/2addr p1, p0

    :goto_0
    return p1
.end method

.method public static getPanelWidth(Landroid/content/Context;Z)I
    .locals 1

    const v0, 0x3faaaaaa

    invoke-static {v0}, Lcom/android/camera/Display;->fitDisplayFull(F)Z

    move-result v0

    if-eqz v0, :cond_0

    if-eqz p1, :cond_0

    invoke-static {p0}, Lcom/android/camera/Util;->getScreenWidth(Landroid/content/Context;)I

    move-result p1

    invoke-static {}, Lcom/android/camera/Display;->getStartMargin()I

    move-result v0

    sub-int/2addr p1, v0

    invoke-static {}, Lcom/android/camera/Display;->getEndMargin()I

    move-result v0

    sub-int/2addr p1, v0

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p0

    const v0, 0x7f070406

    invoke-virtual {p0, v0}, Landroid/content/res/Resources;->getDimensionPixelOffset(I)I

    move-result p0

    mul-int/lit8 p0, p0, 0x2

    sub-int/2addr p1, p0

    return p1

    :cond_0
    invoke-static {p0}, Lcom/android/camera/Util;->getScreenWidth(Landroid/content/Context;)I

    move-result p0

    invoke-static {}, Lcom/android/camera/Display;->getStartMargin()I

    move-result p1

    sub-int/2addr p0, p1

    invoke-static {}, Lcom/android/camera/Display;->getEndMargin()I

    move-result p1

    sub-int/2addr p0, p1

    return p0
.end method

.method public static getRegion(Landroid/content/Context;IIIII)Landroid/graphics/Rect;
    .locals 7

    const/4 v0, 0x3

    if-eq p1, v0, :cond_0

    if-eqz p1, :cond_0

    new-instance p0, Landroid/graphics/Rect;

    invoke-direct {p0}, Landroid/graphics/Rect;-><init>()V

    return-object p0

    :cond_0
    invoke-static {p0}, Lcom/android/camera/Util;->getScreenWidth(Landroid/content/Context;)I

    move-result v1

    const v2, 0x7f0703f9

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    if-ne p1, v0, :cond_1

    invoke-virtual {v3, v2}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v3

    goto :goto_0

    :cond_1
    const v4, 0x7f0703f8

    invoke-virtual {v3, v4}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v3

    :goto_0
    if-ne p1, v0, :cond_2

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v4

    invoke-virtual {v4, v2}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v2

    goto :goto_1

    :cond_2
    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    const v4, 0x7f070400

    invoke-virtual {v2, v4}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v2

    :goto_1
    sub-int v4, v2, v3

    div-int/lit8 v4, v4, 0x2

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v5

    if-nez p1, :cond_3

    const v6, 0x7f070404

    goto :goto_2

    :cond_3
    const v6, 0x7f070405

    :goto_2
    invoke-virtual {v5, v6}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v5

    rem-int v6, p4, p3

    div-int/2addr p4, p3

    mul-int p3, p2, v2

    sub-int/2addr v1, p3

    mul-int/lit8 v5, v5, 0x2

    sub-int/2addr v1, v5

    mul-int/lit8 p3, p2, 0x2

    div-int/2addr v1, p3

    invoke-static {p0, p1, p2, p5}, Lcom/android/camera/fragment/mode/MoreModeHelper;->getHeaderHeightForNormal(Landroid/content/Context;III)I

    move-result p2

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p3

    if-ne p1, v0, :cond_4

    const p5, 0x7f0703fd

    goto :goto_3

    :cond_4
    const p5, 0x7f070407

    :goto_3
    invoke-virtual {p3, p5}, Landroid/content/res/Resources;->getDimensionPixelOffset(I)I

    move-result p3

    add-int/2addr p2, p3

    new-instance p3, Landroid/util/Size;

    mul-int/lit8 p5, v1, 0x2

    add-int/2addr v2, p5

    invoke-static {p0, p1}, Lcom/android/camera/fragment/mode/MoreModeHelper;->getModeHeight(Landroid/content/Context;I)I

    move-result p0

    invoke-direct {p3, v2, p0}, Landroid/util/Size;-><init>(II)V

    invoke-virtual {p3}, Landroid/util/Size;->getWidth()I

    move-result p0

    mul-int/2addr p0, v6

    add-int/2addr v1, p0

    invoke-virtual {p3}, Landroid/util/Size;->getHeight()I

    move-result p0

    mul-int/2addr p0, p4

    add-int/2addr p2, p0

    add-int/2addr v4, v1

    add-int/2addr v4, v3

    add-int/2addr v3, p2

    new-instance p0, Landroid/graphics/Rect;

    invoke-direct {p0, v1, p2, v4, v3}, Landroid/graphics/Rect;-><init>(IIII)V

    return-object p0
.end method

.method public static getRow4PopupStyle(I)I
    .locals 2

    const v0, 0x3faaaaaa

    invoke-static {v0}, Lcom/android/camera/Display;->fitDisplayFull(F)Z

    move-result v0

    const/4 v1, 0x3

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/16 v0, 0x9

    if-gt p0, v0, :cond_1

    goto :goto_0

    :cond_1
    const/4 v1, 0x4

    :goto_0
    return v1
.end method

.method public static getTopMarginForNormal(Landroid/content/Context;I)I
    .locals 2

    invoke-static {}, Lcom/android/camera/fragment/mode/MoreModeHelper;->isLandscapeMode()Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    const/4 v0, 0x3

    if-ne p1, v0, :cond_1

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p0

    const p1, 0x7f0703fd

    invoke-virtual {p0, p1}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v1

    :cond_1
    return v1
.end method

.method public static isFooter4PopupStyle(II)Z
    .locals 5

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/DataItemRunning;->getUiStyle()I

    move-result v0

    const/4 v1, 0x0

    invoke-static {v0, v1}, Lcom/android/camera/Display;->getMoreModeTabCol(IZ)I

    move-result v0

    rem-int v2, p1, v0

    const/4 v3, 0x1

    if-nez v2, :cond_0

    move v4, v3

    goto :goto_0

    :cond_0
    move v4, v1

    :goto_0
    if-eqz v4, :cond_2

    sub-int/2addr p1, v0

    sub-int/2addr p1, v3

    if-le p0, p1, :cond_1

    move v1, v3

    :cond_1
    return v1

    :cond_2
    sub-int/2addr p1, v2

    sub-int/2addr p1, v3

    if-le p0, p1, :cond_3

    move v1, v3

    :cond_3
    return v1
.end method

.method private static isLandscapeMode()Z
    .locals 3

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0xa0

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$BaseDelegate;

    if-eqz v0, :cond_1

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$BaseDelegate;->getAnimationComposite()Lcom/android/camera/animation/AnimationComposite;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/animation/AnimationComposite;->getTargetDegree()I

    move-result v1

    const/16 v2, 0x5a

    if-eq v1, v2, :cond_0

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$BaseDelegate;->getAnimationComposite()Lcom/android/camera/animation/AnimationComposite;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/animation/AnimationComposite;->getTargetDegree()I

    move-result v0

    const/16 v1, 0x10e

    if-ne v0, v1, :cond_1

    :cond_0
    const v0, 0x3faaaaaa

    invoke-static {v0}, Lcom/android/camera/Display;->fitDisplayFull(F)Z

    move-result v0

    if-eqz v0, :cond_1

    const/4 v0, 0x1

    return v0

    :cond_1
    const/4 v0, 0x0

    return v0
.end method

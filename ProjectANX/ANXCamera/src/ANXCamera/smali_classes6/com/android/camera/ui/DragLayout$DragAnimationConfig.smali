.class public Lcom/android/camera/ui/DragLayout$DragAnimationConfig;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private mAlphaThreshold:F

.field private mBgAlphaRange:Landroid/util/Range;

.field private mCornerRadiusRange:Landroid/util/Range;

.field private mDisappearAlphaRange:Landroid/util/Range;

.field private mDisappearDistance:F

.field private mDisappearRange:Landroid/util/Range;

.field private mDisplayAlphaRange:Landroid/util/Range;

.field private mDisplayDistance:F

.field private mDisplayRange:Landroid/util/Range;

.field private mDragThreshold:F

.field private mDuration:I

.field private mSpringDistance:F

.field private mTotalDragDistance:F


# direct methods
.method public constructor <init>(Landroid/content/Context;)V
    .locals 6

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/high16 v0, 0x41400000    # 12.0f

    iput v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDragThreshold:F

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v1, 0x7f0a000d

    invoke-virtual {v0, v1}, Landroid/content/res/Resources;->getInteger(I)I

    move-result v0

    iput v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDuration:I

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v1, 0x7f070431

    invoke-virtual {v0, v1}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    iput v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mTotalDragDistance:F

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v1, 0x7f070200

    invoke-virtual {v0, v1}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    iput v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDragThreshold:F

    new-instance v0, Landroid/util/Range;

    const/4 v1, 0x0

    invoke-static {v1}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v1

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    const v3, 0x7f07042f

    invoke-virtual {v2, v3}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v2

    invoke-static {v2}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v2

    invoke-direct {v0, v1, v2}, Landroid/util/Range;-><init>(Ljava/lang/Comparable;Ljava/lang/Comparable;)V

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisappearRange:Landroid/util/Range;

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisappearRange:Landroid/util/Range;

    invoke-virtual {v0}, Landroid/util/Range;->getUpper()Ljava/lang/Comparable;

    move-result-object v0

    check-cast v0, Ljava/lang/Float;

    invoke-virtual {v0}, Ljava/lang/Float;->floatValue()F

    move-result v0

    iget-object v2, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisappearRange:Landroid/util/Range;

    invoke-virtual {v2}, Landroid/util/Range;->getLower()Ljava/lang/Comparable;

    move-result-object v2

    check-cast v2, Ljava/lang/Float;

    invoke-virtual {v2}, Ljava/lang/Float;->floatValue()F

    move-result v2

    sub-float/2addr v0, v2

    iput v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisappearDistance:F

    new-instance v0, Landroid/util/Range;

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    invoke-virtual {v2, v3}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v2

    invoke-static {v2}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v2

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v4

    const v5, 0x7f070430

    invoke-virtual {v4, v5}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v4

    invoke-static {v4}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v4

    invoke-direct {v0, v2, v4}, Landroid/util/Range;-><init>(Ljava/lang/Comparable;Ljava/lang/Comparable;)V

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisplayRange:Landroid/util/Range;

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisplayRange:Landroid/util/Range;

    invoke-virtual {v0}, Landroid/util/Range;->getUpper()Ljava/lang/Comparable;

    move-result-object v0

    check-cast v0, Ljava/lang/Float;

    invoke-virtual {v0}, Ljava/lang/Float;->floatValue()F

    move-result v0

    iget-object v2, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisplayRange:Landroid/util/Range;

    invoke-virtual {v2}, Landroid/util/Range;->getLower()Ljava/lang/Comparable;

    move-result-object v2

    check-cast v2, Ljava/lang/Float;

    invoke-virtual {v2}, Ljava/lang/Float;->floatValue()F

    move-result v2

    sub-float/2addr v0, v2

    iput v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisplayDistance:F

    new-instance v0, Landroid/util/Range;

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    const v4, 0x7f07042e

    invoke-virtual {v2, v4}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v2

    invoke-static {v2}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v2

    invoke-direct {v0, v1, v2}, Landroid/util/Range;-><init>(Ljava/lang/Comparable;Ljava/lang/Comparable;)V

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mCornerRadiusRange:Landroid/util/Range;

    new-instance v0, Landroid/util/Range;

    const/high16 v2, 0x3f800000    # 1.0f

    invoke-static {v2}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v2

    invoke-direct {v0, v1, v2}, Landroid/util/Range;-><init>(Ljava/lang/Comparable;Ljava/lang/Comparable;)V

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisappearAlphaRange:Landroid/util/Range;

    new-instance v0, Landroid/util/Range;

    const v4, 0x3dcccccd    # 0.1f

    invoke-static {v4}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v4

    invoke-direct {v0, v4, v2}, Landroid/util/Range;-><init>(Ljava/lang/Comparable;Ljava/lang/Comparable;)V

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisplayAlphaRange:Landroid/util/Range;

    new-instance v0, Landroid/util/Range;

    invoke-direct {v0, v1, v2}, Landroid/util/Range;-><init>(Ljava/lang/Comparable;Ljava/lang/Comparable;)V

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mBgAlphaRange:Landroid/util/Range;

    iget v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mTotalDragDistance:F

    const/high16 v1, 0x40000000    # 2.0f

    div-float/2addr v0, v1

    iput v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mSpringDistance:F

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    invoke-virtual {p1, v3}, Landroid/content/res/Resources;->getDimension(I)F

    move-result p1

    iput p1, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mAlphaThreshold:F

    return-void
.end method


# virtual methods
.method public calDragLayoutHeight(Landroid/content/Context;I)V
    .locals 7

    if-nez p1, :cond_0

    return-void

    :cond_0
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v1, 0x7f070400

    invoke-virtual {v0, v1}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v0

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    const v2, 0x7f0703fa

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v1

    sub-int/2addr v1, v0

    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    const v3, 0x7f07040b

    invoke-virtual {v2, v3}, Landroid/content/res/Resources;->getDimensionPixelOffset(I)I

    move-result v2

    invoke-static {p2}, Lcom/android/camera/fragment/mode/MoreModeHelper;->getRow4PopupStyle(I)I

    move-result p2

    const v3, 0x3faaaaaa

    invoke-static {v3}, Lcom/android/camera/Display;->fitDisplayFull(F)Z

    move-result v3

    if-eqz v3, :cond_3

    invoke-static {}, Lcom/android/camera/Display;->getScreenOrientation()I

    move-result v3

    const v4, 0x7f070402

    const v5, 0x7f070401

    const/4 v6, 0x1

    if-eqz v3, :cond_1

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v3

    invoke-virtual {v3}, Lcom/android/camera/data/data/runing/DataItemRunning;->getUiStyle()I

    move-result v3

    if-ne v3, v6, :cond_2

    goto :goto_0

    :cond_1
    invoke-static {}, Lcom/android/camera/module/ModuleManager;->isSquareModule()Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_0
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    invoke-virtual {p1, v4}, Landroid/content/res/Resources;->getDimensionPixelOffset(I)I

    move-result p1

    invoke-static {}, Lcom/android/camera/Display;->getBottomBarHeight()I

    move-result v3

    add-int/2addr p1, v3

    goto :goto_1

    :cond_2
    invoke-virtual {p1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p1

    invoke-virtual {p1, v5}, Landroid/content/res/Resources;->getDimensionPixelOffset(I)I

    move-result p1

    :goto_1
    mul-int/2addr v0, p2

    sub-int/2addr p2, v6

    mul-int/2addr v1, p2

    add-int/2addr v0, v1

    mul-int/lit8 v2, v2, 0x2

    add-int/2addr v0, v2

    add-int/2addr v0, p1

    invoke-static {}, Lcom/android/camera/Display;->getDragDistanceFix()I

    move-result p1

    sub-int/2addr v0, p1

    int-to-float p1, v0

    goto :goto_2

    :cond_3
    invoke-static {p1}, Lcom/android/camera/Display;->getNavigationBarHeight(Landroid/content/Context;)I

    move-result p1

    add-int/2addr v0, v1

    mul-int/2addr v0, p2

    add-int/2addr p1, v0

    add-int/2addr p1, v2

    invoke-static {}, Lcom/android/camera/Display;->getDragDistanceFix()I

    move-result p2

    sub-int/2addr p1, p2

    int-to-float p1, p1

    :goto_2
    iput p1, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mTotalDragDistance:F

    return-void
.end method

.method public getBgAlphaRange()Landroid/util/Range;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mBgAlphaRange:Landroid/util/Range;

    return-object p0
.end method

.method public getCornerRadiusRange()Landroid/util/Range;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mCornerRadiusRange:Landroid/util/Range;

    return-object p0
.end method

.method public getDisappearAlphaRange()Landroid/util/Range;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisappearAlphaRange:Landroid/util/Range;

    return-object p0
.end method

.method public getDisappearDistance()F
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisappearDistance:F

    return p0
.end method

.method public getDisappearRange()Landroid/util/Range;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisappearRange:Landroid/util/Range;

    return-object p0
.end method

.method public getDisplayAlphaRange()Landroid/util/Range;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisplayAlphaRange:Landroid/util/Range;

    return-object p0
.end method

.method public getDisplayDistance()F
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisplayDistance:F

    return p0
.end method

.method public getDisplayRange()Landroid/util/Range;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDisplayRange:Landroid/util/Range;

    return-object p0
.end method

.method public getDragThreshold()F
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDragThreshold:F

    return p0
.end method

.method public getDuration()I
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mDuration:I

    return p0
.end method

.method public getMaxDragDistance()F
    .locals 1

    iget v0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mSpringDistance:F

    iget p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mTotalDragDistance:F

    add-float/2addr v0, p0

    return v0
.end method

.method public getSpringDistance()F
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mSpringDistance:F

    return p0
.end method

.method public getTotalDragDistance()F
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->mTotalDragDistance:F

    return p0
.end method

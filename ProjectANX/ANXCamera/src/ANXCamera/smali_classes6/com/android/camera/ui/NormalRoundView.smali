.class public Lcom/android/camera/ui/NormalRoundView;
.super Landroid/widget/FrameLayout;
.source ""


# instance fields
.field private mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

.field private mIsFill:Z

.field private mRadius:F

.field private mStrokeColor:I

.field private mStrokeWidth:I


# direct methods
.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;)V
    .locals 2

    invoke-direct {p0, p1, p2}, Landroid/widget/FrameLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V

    sget-object v0, Lcom/android/camera/R$styleable;->NormalRoundView:[I

    invoke-virtual {p1, p2, v0}, Landroid/content/Context;->obtainStyledAttributes(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;

    move-result-object p1

    const/4 p2, 0x0

    const/4 v0, 0x0

    invoke-virtual {p1, v0, p2}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result v1

    iput v1, p0, Lcom/android/camera/ui/NormalRoundView;->mRadius:F

    const/4 v1, 0x2

    invoke-virtual {p1, v1, p2}, Landroid/content/res/TypedArray;->getDimension(IF)F

    move-result p2

    float-to-int p2, p2

    iput p2, p0, Lcom/android/camera/ui/NormalRoundView;->mStrokeWidth:I

    const/4 p2, 0x1

    invoke-virtual {p1, p2, v0}, Landroid/content/res/TypedArray;->getColor(II)I

    move-result p2

    iput p2, p0, Lcom/android/camera/ui/NormalRoundView;->mStrokeColor:I

    invoke-virtual {p1}, Landroid/content/res/TypedArray;->recycle()V

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getContext()Landroid/content/Context;

    move-result-object p1

    const p2, 0x7f08009c

    invoke-virtual {p1, p2}, Landroid/content/Context;->getDrawable(I)Landroid/graphics/drawable/Drawable;

    move-result-object p1

    check-cast p1, Landroid/graphics/drawable/GradientDrawable;

    iput-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    if-eqz p1, :cond_0

    iget p2, p0, Lcom/android/camera/ui/NormalRoundView;->mStrokeWidth:I

    iget v0, p0, Lcom/android/camera/ui/NormalRoundView;->mStrokeColor:I

    invoke-virtual {p1, p2, v0}, Landroid/graphics/drawable/GradientDrawable;->setStroke(II)V

    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    iget p2, p0, Lcom/android/camera/ui/NormalRoundView;->mRadius:F

    invoke-virtual {p1, p2}, Landroid/graphics/drawable/GradientDrawable;->setCornerRadius(F)V

    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    invoke-virtual {p0, p1}, Landroid/widget/FrameLayout;->setBackground(Landroid/graphics/drawable/Drawable;)V

    :cond_0
    return-void
.end method


# virtual methods
.method public getCornerRadius()F
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/NormalRoundView;->mRadius:F

    return p0
.end method

.method protected onSizeChanged(IIII)V
    .locals 0

    invoke-super {p0, p1, p2, p3, p4}, Landroid/widget/FrameLayout;->onSizeChanged(IIII)V

    iget-object p0, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    if-eqz p0, :cond_0

    invoke-virtual {p0, p1, p2}, Landroid/graphics/drawable/GradientDrawable;->setSize(II)V

    :cond_0
    return-void
.end method

.method public setCornerRadius(F)V
    .locals 2

    const/4 v0, 0x0

    cmpg-float v1, p1, v0

    if-gez v1, :cond_0

    move p1, v0

    :cond_0
    iput p1, p0, Lcom/android/camera/ui/NormalRoundView;->mRadius:F

    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    if-eqz p1, :cond_1

    iget v0, p0, Lcom/android/camera/ui/NormalRoundView;->mRadius:F

    invoke-virtual {p1, v0}, Landroid/graphics/drawable/GradientDrawable;->setCornerRadius(F)V

    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    invoke-virtual {p0, p1}, Landroid/widget/FrameLayout;->invalidateDrawable(Landroid/graphics/drawable/Drawable;)V

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->invalidate()V

    :cond_1
    return-void
.end method

.method public setFill(Z)V
    .locals 3

    iput-boolean p1, p0, Lcom/android/camera/ui/NormalRoundView;->mIsFill:Z

    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    if-eqz p1, :cond_2

    iget-boolean v0, p0, Lcom/android/camera/ui/NormalRoundView;->mIsFill:Z

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v2, 0x7f060137

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getColor(I)I

    move-result v0

    goto :goto_0

    :cond_0
    move v0, v1

    :goto_0
    invoke-virtual {p1, v0}, Landroid/graphics/drawable/GradientDrawable;->setColor(I)V

    iget-boolean p1, p0, Lcom/android/camera/ui/NormalRoundView;->mIsFill:Z

    if-eqz p1, :cond_1

    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    invoke-virtual {p1, v1, v1}, Landroid/graphics/drawable/GradientDrawable;->setStroke(II)V

    goto :goto_1

    :cond_1
    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    iget v0, p0, Lcom/android/camera/ui/NormalRoundView;->mStrokeWidth:I

    iget v1, p0, Lcom/android/camera/ui/NormalRoundView;->mStrokeColor:I

    invoke-virtual {p1, v0, v1}, Landroid/graphics/drawable/GradientDrawable;->setStroke(II)V

    :goto_1
    iget-object p1, p0, Lcom/android/camera/ui/NormalRoundView;->mBgDrawable:Landroid/graphics/drawable/GradientDrawable;

    invoke-virtual {p0, p1}, Landroid/widget/FrameLayout;->invalidateDrawable(Landroid/graphics/drawable/Drawable;)V

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->invalidate()V

    :cond_2
    return-void
.end method

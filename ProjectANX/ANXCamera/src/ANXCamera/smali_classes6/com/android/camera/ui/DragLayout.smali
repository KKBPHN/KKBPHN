.class public Lcom/android/camera/ui/DragLayout;
.super Landroid/widget/FrameLayout;
.source ""


# static fields
.field private static final STATE_DRAGGING:I = 0x1

.field private static final STATE_EXPEND:I = 0x4

.field private static final STATE_IDLE:I = 0x0

.field private static final STATE_SETTLING:I = 0x2

.field private static final STATE_SHRINK:I = 0x3

.field public static final TAG:Ljava/lang/String; = "DragLayout"

.field private static debug:Z

.field private static sAnimationConfig:Lcom/android/camera/ui/DragLayout$DragAnimationConfig;


# instance fields
.field private mBar:Lcom/android/camera/ui/DragLayout$Bar;

.field private mCatchDrag:Z

.field private mChildren:Landroid/widget/FrameLayout;

.field private mCurDirectionIsUp:Z

.field private final mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

.field private mDragEnable:Z

.field private mDragInterpolator:Landroid/view/animation/Interpolator;

.field private mOffset:F

.field private mOnDragListeners:Ljava/util/List;

.field private mScreenOrientation:I

.field private mSkipDragUpDown:Z

.field private mStartDirectionIsUp:Z

.field private mStartX:F

.field private mStartY:F

.field private mState:I

.field private mValueAnimatorListener:Lcom/android/camera/animation/FolmeUtils$IPhyAnimatorListener;

.field private mVelocityTracker:Landroid/view/VelocityTracker;


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;)V
    .locals 1
    .param p1    # Landroid/content/Context;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    const/4 v0, 0x0

    invoke-direct {p0, p1, v0}, Lcom/android/camera/ui/DragLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;)V
    .locals 1
    .param p1    # Landroid/content/Context;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Landroid/util/AttributeSet;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    const/4 v0, 0x0

    invoke-direct {p0, p1, p2, v0}, Lcom/android/camera/ui/DragLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V
    .locals 0
    .param p1    # Landroid/content/Context;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param
    .param p2    # Landroid/util/AttributeSet;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-direct {p0, p1, p2, p3}, Landroid/widget/FrameLayout;-><init>(Landroid/content/Context;Landroid/util/AttributeSet;I)V

    const/4 p2, 0x0

    iput-boolean p2, p0, Lcom/android/camera/ui/DragLayout;->mDragEnable:Z

    iput-boolean p2, p0, Lcom/android/camera/ui/DragLayout;->mSkipDragUpDown:Z

    iput-boolean p2, p0, Lcom/android/camera/ui/DragLayout;->mCatchDrag:Z

    iput p2, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    iput p2, p0, Lcom/android/camera/ui/DragLayout;->mScreenOrientation:I

    new-instance p2, Ljava/util/ArrayList;

    invoke-direct {p2}, Ljava/util/ArrayList;-><init>()V

    iput-object p2, p0, Lcom/android/camera/ui/DragLayout;->mOnDragListeners:Ljava/util/List;

    sget-object p2, Lcom/android/camera/ui/DragLayout;->sAnimationConfig:Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    if-nez p2, :cond_0

    new-instance p2, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    invoke-direct {p2, p1}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;-><init>(Landroid/content/Context;)V

    sput-object p2, Lcom/android/camera/ui/DragLayout;->sAnimationConfig:Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    :cond_0
    new-instance p1, Lcom/android/camera/ui/DragLayout$Dependency;

    const/4 p2, 0x0

    invoke-direct {p1, p0, p2}, Lcom/android/camera/ui/DragLayout$Dependency;-><init>(Lcom/android/camera/ui/DragLayout;Lcom/android/camera/ui/DragLayout$1;)V

    iput-object p1, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    new-instance p1, Landroid/view/animation/LinearInterpolator;

    invoke-direct {p1}, Landroid/view/animation/LinearInterpolator;-><init>()V

    iput-object p1, p0, Lcom/android/camera/ui/DragLayout;->mDragInterpolator:Landroid/view/animation/Interpolator;

    new-instance p1, Lcom/android/camera/ui/DragLayout$1;

    invoke-direct {p1, p0}, Lcom/android/camera/ui/DragLayout$1;-><init>(Lcom/android/camera/ui/DragLayout;)V

    iput-object p1, p0, Lcom/android/camera/ui/DragLayout;->mValueAnimatorListener:Lcom/android/camera/animation/FolmeUtils$IPhyAnimatorListener;

    return-void
.end method

.method private LogD(Ljava/lang/String;)V
    .locals 0

    sget-object p0, Lcom/android/camera/ui/DragLayout;->TAG:Ljava/lang/String;

    invoke-static {p0, p1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method private LogV(Ljava/lang/String;)V
    .locals 0

    sget-boolean p0, Lcom/android/camera/ui/DragLayout;->debug:Z

    if-eqz p0, :cond_0

    sget-object p0, Lcom/android/camera/ui/DragLayout;->TAG:Ljava/lang/String;

    invoke-static {p0, p1}, Lcom/android/camera/log/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    :cond_0
    return-void
.end method

.method static synthetic access$100(Lcom/android/camera/ui/DragLayout;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    return p0
.end method

.method static synthetic access$1000(Lcom/android/camera/ui/DragLayout;Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    return-void
.end method

.method static synthetic access$1100(Lcom/android/camera/ui/DragLayout;Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/android/camera/ui/DragLayout;->LogD(Ljava/lang/String;)V

    return-void
.end method

.method static synthetic access$1200(Lcom/android/camera/ui/DragLayout;)Lcom/android/camera/ui/DragLayout$Bar;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    return-object p0
.end method

.method static synthetic access$200(Lcom/android/camera/ui/DragLayout;)F
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    return p0
.end method

.method static synthetic access$202(Lcom/android/camera/ui/DragLayout;F)F
    .locals 0

    iput p1, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    return p1
.end method

.method static synthetic access$300(Lcom/android/camera/ui/DragLayout;)Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    return p0
.end method

.method static synthetic access$302(Lcom/android/camera/ui/DragLayout;Z)Z
    .locals 0

    iput-boolean p1, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    return p1
.end method

.method static synthetic access$400(Lcom/android/camera/ui/DragLayout;)Lcom/android/camera/ui/DragLayout$Dependency;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    return-object p0
.end method

.method static synthetic access$500(Lcom/android/camera/ui/DragLayout;)Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/ui/DragLayout;->mStartDirectionIsUp:Z

    return p0
.end method

.method static synthetic access$502(Lcom/android/camera/ui/DragLayout;Z)Z
    .locals 0

    iput-boolean p1, p0, Lcom/android/camera/ui/DragLayout;->mStartDirectionIsUp:Z

    return p1
.end method

.method static synthetic access$600(Lcom/android/camera/ui/DragLayout;I)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/android/camera/ui/DragLayout;->setState(I)V

    return-void
.end method

.method static synthetic access$900(Lcom/android/camera/ui/DragLayout;)Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout;->mOnDragListeners:Ljava/util/List;

    return-object p0
.end method

.method private ensureView()V
    .locals 4

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    if-nez v0, :cond_1

    new-instance v0, Lcom/android/camera/ui/DragLayout$Bar;

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getContext()Landroid/content/Context;

    move-result-object v1

    invoke-direct {v0, v1}, Lcom/android/camera/ui/DragLayout$Bar;-><init>(Landroid/content/Context;)V

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    new-instance v0, Landroid/widget/FrameLayout$LayoutParams;

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    const v2, 0x7f070420

    invoke-virtual {v1, v2}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v1

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    invoke-virtual {v3, v2}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v2

    int-to-float v2, v2

    const v3, 0x3f19999a    # 0.6f

    mul-float/2addr v2, v3

    float-to-int v2, v2

    invoke-direct {v0, v1, v2}, Landroid/widget/FrameLayout$LayoutParams;-><init>(II)V

    const/4 v1, 0x1

    iput v1, v0, Landroid/widget/FrameLayout$LayoutParams;->gravity:I

    invoke-virtual {p0}, Landroid/widget/FrameLayout;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    const v3, 0x7f07041e

    invoke-virtual {v2, v3}, Landroid/content/res/Resources;->getDimensionPixelSize(I)I

    move-result v2

    iput v2, v0, Landroid/widget/FrameLayout$LayoutParams;->topMargin:I

    invoke-virtual {p0}, Lcom/android/camera/ui/DragLayout;->getDragChildren()Landroid/widget/FrameLayout;

    move-result-object v2

    iget-object v3, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    invoke-virtual {v2, v3, v1, v0}, Landroid/widget/FrameLayout;->addView(Landroid/view/View;ILandroid/view/ViewGroup$LayoutParams;)V

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    iget-boolean v1, p0, Lcom/android/camera/ui/DragLayout;->mDragEnable:Z

    if-eqz v1, :cond_0

    const/4 v1, 0x0

    goto :goto_0

    :cond_0
    const/16 v1, 0x8

    :goto_0
    invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V

    :cond_1
    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-nez v0, :cond_2

    const/4 v0, 0x3

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->setState(I)V

    :cond_2
    return-void
.end method

.method public static getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;
    .locals 1

    sget-object v0, Lcom/android/camera/ui/DragLayout;->sAnimationConfig:Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    return-object v0
.end method

.method private isInvalidMoveEvent(ZLandroid/view/MotionEvent;)Z
    .locals 5

    invoke-virtual {p2}, Landroid/view/MotionEvent;->getRawY()F

    move-result v0

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mStartY:F

    sub-float/2addr v0, v1

    invoke-virtual {p2}, Landroid/view/MotionEvent;->getRawX()F

    move-result p2

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mStartX:F

    sub-float/2addr p2, v1

    iget p0, p0, Lcom/android/camera/ui/DragLayout;->mScreenOrientation:I

    const/4 v1, 0x0

    const/4 v2, 0x0

    const/4 v3, 0x1

    if-eq p0, v3, :cond_7

    const/4 v4, 0x2

    if-eq p0, v4, :cond_3

    if-eqz p1, :cond_1

    cmpl-float p0, v0, v2

    if-lez p0, :cond_0

    move v1, v3

    :cond_0
    return v1

    :cond_1
    cmpg-float p0, v0, v2

    if-gez p0, :cond_2

    move v1, v3

    :cond_2
    return v1

    :cond_3
    if-eqz p1, :cond_5

    cmpl-float p0, p2, v2

    if-lez p0, :cond_4

    move v1, v3

    :cond_4
    return v1

    :cond_5
    cmpg-float p0, p2, v2

    if-gez p0, :cond_6

    move v1, v3

    :cond_6
    return v1

    :cond_7
    if-eqz p1, :cond_9

    cmpg-float p0, p2, v2

    if-gez p0, :cond_8

    move v1, v3

    :cond_8
    return v1

    :cond_9
    cmpl-float p0, p2, v2

    if-lez p0, :cond_a

    move v1, v3

    :cond_a
    return v1
.end method

.method private isLeftLandscape()Z
    .locals 1

    iget p0, p0, Lcom/android/camera/ui/DragLayout;->mScreenOrientation:I

    const/4 v0, 0x1

    if-ne p0, v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method private isPortrait()Z
    .locals 0

    iget p0, p0, Lcom/android/camera/ui/DragLayout;->mScreenOrientation:I

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method private isRightLandscape()Z
    .locals 1

    iget p0, p0, Lcom/android/camera/ui/DragLayout;->mScreenOrientation:I

    const/4 v0, 0x2

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method private onDrag(Landroid/view/MotionEvent;)Z
    .locals 6

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "onDrag skip ?"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean v1, p0, Lcom/android/camera/ui/DragLayout;->mSkipDragUpDown:Z

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string v1, ", mState "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    const/4 v1, 0x3

    const/4 v2, 0x0

    const/4 v3, 0x0

    const/4 v4, 0x1

    if-eq v0, v1, :cond_9

    const/4 v5, 0x4

    if-ne v0, v5, :cond_0

    goto/16 :goto_2

    :cond_0
    if-ne v0, v4, :cond_8

    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mStartDirectionIsUp:Z

    invoke-direct {p0, v0, p1}, Lcom/android/camera/ui/DragLayout;->isInvalidMoveEvent(ZLandroid/view/MotionEvent;)Z

    move-result v0

    if-eqz v0, :cond_1

    return v4

    :cond_1
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getRawY()F

    move-result v0

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mStartY:F

    sub-float/2addr v0, v1

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getRawX()F

    move-result p1

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mStartX:F

    sub-float/2addr p1, v1

    invoke-direct {p0}, Lcom/android/camera/ui/DragLayout;->isPortrait()Z

    move-result v1

    if-eqz v1, :cond_2

    move p1, v0

    :cond_2
    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    invoke-static {v0}, Ljava/lang/Math;->abs(F)F

    move-result v0

    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getTotalDragDistance()F

    move-result v1

    cmpg-float v0, v0, v1

    if-gez v0, :cond_7

    invoke-static {p1}, Ljava/lang/Math;->abs(F)F

    move-result v0

    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getTotalDragDistance()F

    move-result v1

    invoke-static {v0, v1}, Ljava/lang/Math;->min(FF)F

    move-result v0

    invoke-static {v0, v3}, Ljava/lang/Math;->max(FF)F

    move-result v0

    iget-object v1, p0, Lcom/android/camera/ui/DragLayout;->mDragInterpolator:Landroid/view/animation/Interpolator;

    invoke-interface {v1, v0}, Landroid/view/animation/Interpolator;->getInterpolation(F)F

    move-result v0

    invoke-direct {p0}, Lcom/android/camera/ui/DragLayout;->isLeftLandscape()Z

    move-result v1

    if-eqz v1, :cond_3

    cmpl-float p1, p1, v3

    if-lez p1, :cond_4

    :goto_0
    neg-float v0, v0

    goto :goto_1

    :cond_3
    cmpg-float p1, p1, v3

    if-gez p1, :cond_4

    goto :goto_0

    :cond_4
    :goto_1
    iget p1, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    cmpl-float v1, v0, p1

    if-eqz v1, :cond_6

    cmpg-float p1, v0, p1

    if-gez p1, :cond_5

    move v2, v4

    :cond_5
    iput-boolean v2, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    :cond_6
    iput v0, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    iget-object p1, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    float-to-int v0, v0

    iget-boolean p0, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    invoke-virtual {p1, v0, p0}, Lcom/android/camera/ui/DragLayout$Dependency;->onDragProgress(IZ)V

    :cond_7
    return v4

    :cond_8
    return v2

    :cond_9
    :goto_2
    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-ne v0, v1, :cond_a

    move v2, v4

    :cond_a
    iput-boolean v2, p0, Lcom/android/camera/ui/DragLayout;->mStartDirectionIsUp:Z

    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mStartDirectionIsUp:Z

    iput-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    iget-object v1, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    invoke-virtual {v1, v0}, Lcom/android/camera/ui/DragLayout$Dependency;->onDragStart(Z)V

    invoke-direct {p0, v4}, Lcom/android/camera/ui/DragLayout;->setState(I)V

    iput v3, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getRawY()F

    move-result v0

    iput v0, p0, Lcom/android/camera/ui/DragLayout;->mStartY:F

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getRawX()F

    move-result p1

    iput p1, p0, Lcom/android/camera/ui/DragLayout;->mStartX:F

    return v4
.end method

.method private setState(I)V
    .locals 2

    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-eq v0, p1, :cond_0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "setState "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogD(Ljava/lang/String;)V

    iput p1, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    :cond_0
    return-void
.end method


# virtual methods
.method public synthetic O000000o(Landroid/view/View;Landroid/view/MotionEvent;)Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/ui/DragLayout;->mDragEnable:Z

    return p0
.end method

.method public addOnDragListener(Lcom/android/camera/ui/DragLayout$OnDragListener;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "addOnDragListener "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mOnDragListeners:Ljava/util/List;

    invoke-interface {v0, p1}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout;->mOnDragListeners:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_0
    return-void
.end method

.method public dispatchTouchEvent(Landroid/view/MotionEvent;)Z
    .locals 11

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "dispatchTouchEvent "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getActionMasked()I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mDragEnable:Z

    if-nez v0, :cond_0

    invoke-super {p0, p1}, Landroid/widget/FrameLayout;->dispatchTouchEvent(Landroid/view/MotionEvent;)Z

    move-result p0

    return p0

    :cond_0
    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    const/4 v1, 0x2

    const/4 v2, 0x1

    if-ne v0, v1, :cond_1

    return v2

    :cond_1
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getActionMasked()I

    move-result v0

    const/4 v3, -0x1

    const/4 v4, 0x4

    const/4 v5, 0x3

    const/4 v6, 0x0

    if-eqz v0, :cond_23

    const/4 v7, 0x0

    if-eq v0, v2, :cond_1b

    if-eq v0, v1, :cond_2

    goto/16 :goto_c

    :cond_2
    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mVelocityTracker:Landroid/view/VelocityTracker;

    if-eqz v0, :cond_3

    invoke-virtual {v0, p1}, Landroid/view/VelocityTracker;->addMovement(Landroid/view/MotionEvent;)V

    :cond_3
    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mSkipDragUpDown:Z

    if-eqz v0, :cond_4

    invoke-super {p0, p1}, Landroid/widget/FrameLayout;->dispatchTouchEvent(Landroid/view/MotionEvent;)Z

    move-result p0

    return p0

    :cond_4
    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mCatchDrag:Z

    if-eqz v0, :cond_5

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    invoke-virtual {v0}, Lcom/android/camera/ui/DragLayout$Dependency;->onInterceptDrag()Z

    move-result v0

    if-eqz v0, :cond_5

    const-string v0, "skip drag caz dependency intercept."

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    iput-boolean v2, p0, Lcom/android/camera/ui/DragLayout;->mSkipDragUpDown:Z

    :goto_0
    invoke-super {p0, p1}, Landroid/widget/FrameLayout;->dispatchTouchEvent(Landroid/view/MotionEvent;)Z

    move-result p0

    return p0

    :cond_5
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getRawX()F

    move-result v0

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mStartX:F

    sub-float/2addr v0, v1

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getRawY()F

    move-result v1

    iget v8, p0, Lcom/android/camera/ui/DragLayout;->mStartY:F

    sub-float/2addr v1, v8

    iget v8, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-eq v8, v5, :cond_6

    if-ne v8, v4, :cond_7

    :cond_6
    invoke-static {v0}, Ljava/lang/Math;->abs(F)F

    move-result v8

    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object v9

    invoke-virtual {v9}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getDragThreshold()F

    move-result v9

    cmpg-float v8, v8, v9

    if-gez v8, :cond_7

    invoke-static {v1}, Ljava/lang/Math;->abs(F)F

    move-result v8

    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object v9

    invoke-virtual {v9}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getDragThreshold()F

    move-result v9

    cmpg-float v8, v8, v9

    if-gez v8, :cond_7

    return v2

    :cond_7
    iget v8, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-ne v8, v5, :cond_d

    invoke-direct {p0}, Lcom/android/camera/ui/DragLayout;->isLeftLandscape()Z

    move-result v8

    if-eqz v8, :cond_9

    invoke-static {v1}, Ljava/lang/Math;->abs(F)F

    move-result v8

    invoke-static {v0}, Ljava/lang/Math;->abs(F)F

    move-result v9

    cmpl-float v8, v8, v9

    if-gtz v8, :cond_8

    cmpg-float v8, v0, v7

    if-gez v8, :cond_b

    :cond_8
    :goto_1
    move v8, v2

    goto :goto_2

    :cond_9
    invoke-direct {p0}, Lcom/android/camera/ui/DragLayout;->isRightLandscape()Z

    move-result v8

    if-eqz v8, :cond_a

    invoke-static {v1}, Ljava/lang/Math;->abs(F)F

    move-result v8

    invoke-static {v0}, Ljava/lang/Math;->abs(F)F

    move-result v9

    cmpl-float v8, v8, v9

    if-gtz v8, :cond_8

    cmpl-float v8, v0, v7

    if-lez v8, :cond_b

    goto :goto_1

    :cond_a
    invoke-static {v0}, Ljava/lang/Math;->abs(F)F

    move-result v8

    invoke-static {v1}, Ljava/lang/Math;->abs(F)F

    move-result v9

    cmpl-float v8, v8, v9

    if-gtz v8, :cond_8

    cmpl-float v8, v1, v7

    if-lez v8, :cond_b

    goto :goto_1

    :cond_b
    move v8, v6

    :goto_2
    if-eqz v8, :cond_e

    iput-boolean v2, p0, Lcom/android/camera/ui/DragLayout;->mSkipDragUpDown:Z

    const-string v0, "skip drag up."

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogD(Ljava/lang/String;)V

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    if-eqz v0, :cond_c

    invoke-static {v0, v6}, Lcom/android/camera/ui/DragLayout$Bar;->access$800(Lcom/android/camera/ui/DragLayout$Bar;I)Z

    :cond_c
    invoke-super {p0, p1}, Landroid/widget/FrameLayout;->dispatchTouchEvent(Landroid/view/MotionEvent;)Z

    move-result p0

    return p0

    :cond_d
    move v8, v6

    :cond_e
    iget v9, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-ne v9, v4, :cond_15

    invoke-direct {p0}, Lcom/android/camera/ui/DragLayout;->isPortrait()Z

    move-result v9

    const-string v10, "skip drag down."

    if-eqz v9, :cond_11

    cmpl-float v0, v1, v7

    if-nez v0, :cond_f

    :goto_3
    invoke-direct {p0, v10}, Lcom/android/camera/ui/DragLayout;->LogD(Ljava/lang/String;)V

    goto/16 :goto_0

    :cond_f
    cmpg-float v0, v1, v7

    if-gez v0, :cond_14

    :cond_10
    :goto_4
    move v8, v2

    goto :goto_5

    :cond_11
    cmpl-float v1, v0, v7

    if-nez v1, :cond_12

    goto :goto_3

    :cond_12
    invoke-direct {p0}, Lcom/android/camera/ui/DragLayout;->isLeftLandscape()Z

    move-result v9

    if-eqz v9, :cond_13

    if-gtz v1, :cond_10

    :cond_13
    invoke-direct {p0}, Lcom/android/camera/ui/DragLayout;->isRightLandscape()Z

    move-result v1

    if-eqz v1, :cond_14

    cmpg-float v0, v0, v7

    if-gez v0, :cond_14

    goto :goto_4

    :cond_14
    :goto_5
    if-eqz v8, :cond_15

    iput-boolean v2, p0, Lcom/android/camera/ui/DragLayout;->mSkipDragUpDown:Z

    goto :goto_3

    :cond_15
    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    if-eqz v0, :cond_18

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-eq v1, v5, :cond_17

    if-eq v1, v4, :cond_16

    goto :goto_6

    :cond_16
    invoke-static {v0, v3}, Lcom/android/camera/ui/DragLayout$Bar;->access$800(Lcom/android/camera/ui/DragLayout$Bar;I)Z

    goto :goto_6

    :cond_17
    invoke-static {v0, v2}, Lcom/android/camera/ui/DragLayout$Bar;->access$800(Lcom/android/camera/ui/DragLayout$Bar;I)Z

    :cond_18
    :goto_6
    invoke-direct {p0, p1}, Lcom/android/camera/ui/DragLayout;->onDrag(Landroid/view/MotionEvent;)Z

    move-result v0

    if-nez v0, :cond_1a

    invoke-super {p0, p1}, Landroid/widget/FrameLayout;->dispatchTouchEvent(Landroid/view/MotionEvent;)Z

    move-result p0

    if-eqz p0, :cond_19

    goto :goto_7

    :cond_19
    move v2, v6

    :cond_1a
    :goto_7
    return v2

    :cond_1b
    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mVelocityTracker:Landroid/view/VelocityTracker;

    if-eqz v0, :cond_1c

    invoke-virtual {v0, p1}, Landroid/view/VelocityTracker;->addMovement(Landroid/view/MotionEvent;)V

    :cond_1c
    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    if-eqz v0, :cond_1d

    invoke-static {v0, v6}, Lcom/android/camera/ui/DragLayout$Bar;->access$800(Lcom/android/camera/ui/DragLayout$Bar;I)Z

    :cond_1d
    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-ne v0, v2, :cond_28

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "onUp "

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean v3, p0, Lcom/android/camera/ui/DragLayout;->mStartDirectionIsUp:Z

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string v3, " "

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-boolean v3, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string v3, ", mOffset = "

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(F)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    invoke-static {v0}, Ljava/lang/Math;->abs(F)F

    move-result v0

    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object v3

    invoke-virtual {v3}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getTotalDragDistance()F

    move-result v3

    cmpg-float v0, v0, v3

    if-gez v0, :cond_21

    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mStartDirectionIsUp:Z

    iget-boolean v3, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    if-ne v0, v3, :cond_1f

    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    if-eqz v3, :cond_1e

    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object v3

    invoke-virtual {v3}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getTotalDragDistance()F

    move-result v3

    neg-float v3, v3

    goto :goto_8

    :cond_1e
    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object v3

    invoke-virtual {v3}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getTotalDragDistance()F

    move-result v3

    goto :goto_8

    :cond_1f
    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mOffset:F

    move v3, v7

    :goto_8
    iget-object v4, p0, Lcom/android/camera/ui/DragLayout;->mVelocityTracker:Landroid/view/VelocityTracker;

    if-eqz v4, :cond_20

    const/16 v5, 0x3e8

    invoke-virtual {v4, v5}, Landroid/view/VelocityTracker;->computeCurrentVelocity(I)V

    iget-object v4, p0, Lcom/android/camera/ui/DragLayout;->mVelocityTracker:Landroid/view/VelocityTracker;

    invoke-virtual {v4}, Landroid/view/VelocityTracker;->getYVelocity()F

    move-result v7

    :cond_20
    iget-object v4, p0, Lcom/android/camera/ui/DragLayout;->mValueAnimatorListener:Lcom/android/camera/animation/FolmeUtils$IPhyAnimatorListener;

    invoke-static {p0, v0, v3, v7, v4}, Lcom/android/camera/animation/FolmeUtils;->popup(Landroid/view/View;FFFLcom/android/camera/animation/FolmeUtils$IPhyAnimatorListener;)Lmiuix/animation/IStateStyle;

    invoke-direct {p0, v1}, Lcom/android/camera/ui/DragLayout;->setState(I)V

    goto :goto_a

    :cond_21
    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    iget-boolean v1, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    invoke-virtual {v0, v1}, Lcom/android/camera/ui/DragLayout$Dependency;->onDragDone(Z)V

    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mCurDirectionIsUp:Z

    if-eqz v0, :cond_22

    goto :goto_9

    :cond_22
    move v4, v5

    :goto_9
    invoke-direct {p0, v4}, Lcom/android/camera/ui/DragLayout;->setState(I)V

    :goto_a
    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mCatchDrag:Z

    if-nez v0, :cond_28

    return v2

    :cond_23
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "onDown "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getActionMasked()I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mVelocityTracker:Landroid/view/VelocityTracker;

    if-eqz v0, :cond_24

    invoke-virtual {v0}, Landroid/view/VelocityTracker;->clear()V

    goto :goto_b

    :cond_24
    invoke-static {}, Landroid/view/VelocityTracker;->obtain()Landroid/view/VelocityTracker;

    move-result-object v0

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout;->mVelocityTracker:Landroid/view/VelocityTracker;

    :goto_b
    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mVelocityTracker:Landroid/view/VelocityTracker;

    invoke-virtual {v0, p1}, Landroid/view/VelocityTracker;->addMovement(Landroid/view/MotionEvent;)V

    iput-boolean v6, p0, Lcom/android/camera/ui/DragLayout;->mSkipDragUpDown:Z

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getRawX()F

    move-result v0

    iput v0, p0, Lcom/android/camera/ui/DragLayout;->mStartX:F

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getRawY()F

    move-result v0

    iput v0, p0, Lcom/android/camera/ui/DragLayout;->mStartY:F

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mStartX:F

    float-to-int v1, v1

    iget v6, p0, Lcom/android/camera/ui/DragLayout;->mStartY:F

    float-to-int v6, v6

    invoke-virtual {v0, v1, v6}, Lcom/android/camera/ui/DragLayout$Dependency;->catchDrag(II)Z

    move-result v0

    iput-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mCatchDrag:Z

    iget-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mCatchDrag:Z

    if-eqz v0, :cond_25

    const-string v0, "dependency wanna catch drag event."

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    :cond_25
    invoke-virtual {p0}, Lcom/android/camera/ui/DragLayout;->getDragChildren()Landroid/widget/FrameLayout;

    move-result-object v0

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mStartX:F

    float-to-int v1, v1

    iget v6, p0, Lcom/android/camera/ui/DragLayout;->mStartY:F

    float-to-int v6, v6

    invoke-static {v0, v1, v6}, Lcom/android/camera/Util;->isInViewRegion(Landroid/view/View;II)Z

    move-result v0

    if-eqz v0, :cond_28

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mStartX:F

    float-to-int v1, v1

    iget v6, p0, Lcom/android/camera/ui/DragLayout;->mStartY:F

    float-to-int v6, v6

    invoke-virtual {v0, v1, v6}, Lcom/android/camera/ui/DragLayout$Dependency;->showPress(II)Z

    move-result v0

    if-eqz v0, :cond_28

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    if-eqz v0, :cond_28

    iget v1, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-eq v1, v5, :cond_27

    if-eq v1, v4, :cond_26

    goto :goto_c

    :cond_26
    invoke-static {v0, v3}, Lcom/android/camera/ui/DragLayout$Bar;->access$800(Lcom/android/camera/ui/DragLayout$Bar;I)Z

    goto :goto_c

    :cond_27
    invoke-static {v0, v2}, Lcom/android/camera/ui/DragLayout$Bar;->access$800(Lcom/android/camera/ui/DragLayout$Bar;I)Z

    :cond_28
    :goto_c
    invoke-super {p0, p1}, Landroid/widget/FrameLayout;->dispatchTouchEvent(Landroid/view/MotionEvent;)Z

    move-result p0

    return p0
.end method

.method public getDragChildren()Landroid/widget/FrameLayout;
    .locals 2

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mChildren:Landroid/widget/FrameLayout;

    if-nez v0, :cond_0

    const v0, 0x7f0900fb

    invoke-virtual {p0, v0}, Landroid/widget/FrameLayout;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/FrameLayout;

    iput-object v0, p0, Lcom/android/camera/ui/DragLayout;->mChildren:Landroid/widget/FrameLayout;

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mChildren:Landroid/widget/FrameLayout;

    new-instance v1, Lcom/android/camera/ui/O00000Oo;

    invoke-direct {v1, p0}, Lcom/android/camera/ui/O00000Oo;-><init>(Lcom/android/camera/ui/DragLayout;)V

    invoke-virtual {v0, v1}, Landroid/widget/FrameLayout;->setOnTouchListener(Landroid/view/View$OnTouchListener;)V

    :cond_0
    iget-object p0, p0, Lcom/android/camera/ui/DragLayout;->mChildren:Landroid/widget/FrameLayout;

    return-object p0
.end method

.method public isExpanded()Z
    .locals 1

    iget p0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    const/4 v0, 0x4

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public isShrink()Z
    .locals 1

    iget p0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    const/4 v0, 0x3

    if-eq p0, v0, :cond_1

    if-nez p0, :cond_0

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 p0, 0x1

    :goto_1
    return p0
.end method

.method protected onFinishInflate()V
    .locals 0

    invoke-super {p0}, Landroid/widget/FrameLayout;->onFinishInflate()V

    return-void
.end method

.method protected onLayout(ZIIII)V
    .locals 0

    invoke-super/range {p0 .. p5}, Landroid/widget/FrameLayout;->onLayout(ZIIII)V

    return-void
.end method

.method protected onMeasure(II)V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/ui/DragLayout;->ensureView()V

    invoke-super {p0, p1, p2}, Landroid/widget/FrameLayout;->onMeasure(II)V

    return-void
.end method

.method public removeOnDragListener(Lcom/android/camera/ui/DragLayout$OnDragListener;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "removeOnDragListener "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/DragLayout;->LogV(Ljava/lang/String;)V

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout;->mOnDragListeners:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->remove(Ljava/lang/Object;)Z

    return-void
.end method

.method public reset()V
    .locals 2

    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    if-eqz v0, :cond_0

    const/4 v1, 0x3

    if-eq v0, v1, :cond_0

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Lcom/android/camera/ui/DragLayout$Dependency;->onDragStart(Z)V

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    invoke-virtual {v0, v1, v1}, Lcom/android/camera/ui/DragLayout$Dependency;->onDragProgress(IZ)V

    iget-object v0, p0, Lcom/android/camera/ui/DragLayout;->mDependency:Lcom/android/camera/ui/DragLayout$Dependency;

    invoke-virtual {v0, v1}, Lcom/android/camera/ui/DragLayout$Dependency;->onDragDone(Z)V

    invoke-direct {p0, v1}, Lcom/android/camera/ui/DragLayout;->setState(I)V

    :cond_0
    return-void
.end method

.method public setDragEnable(Z)V
    .locals 4

    invoke-static {}, Lcom/android/camera/CameraSettings;->isPopupMoreStyle()Z

    move-result v0

    const/4 v1, 0x0

    if-nez v0, :cond_0

    move v0, v1

    goto :goto_0

    :cond_0
    move v0, p1

    :goto_0
    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object v2

    invoke-virtual {v2}, Lcom/android/camera/data/data/global/DataItemGlobal;->isNormalIntent()Z

    move-result v2

    if-nez v2, :cond_1

    move v0, v1

    :cond_1
    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object v2

    invoke-virtual {v2}, Lcom/android/camera/data/data/global/DataItemGlobal;->getComponentModuleList()Lcom/android/camera/data/data/global/ComponentModuleList;

    move-result-object v2

    invoke-static {}, Lcom/android/camera/module/ModuleManager;->getActiveModuleIndex()I

    move-result v3

    invoke-virtual {v2, v3}, Lcom/android/camera/data/data/global/ComponentModuleList;->isCommonMode(I)Z

    move-result v2

    if-nez v2, :cond_2

    move v0, v1

    :cond_2
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "setDragEnable : "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    const-string p1, ", force : "

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lcom/android/camera/ui/DragLayout;->LogD(Ljava/lang/String;)V

    iget-boolean p1, p0, Lcom/android/camera/ui/DragLayout;->mDragEnable:Z

    if-eq p1, v0, :cond_4

    iput-boolean v0, p0, Lcom/android/camera/ui/DragLayout;->mDragEnable:Z

    iget-object p1, p0, Lcom/android/camera/ui/DragLayout;->mBar:Lcom/android/camera/ui/DragLayout$Bar;

    if-eqz p1, :cond_4

    iget-boolean p0, p0, Lcom/android/camera/ui/DragLayout;->mDragEnable:Z

    if-eqz p0, :cond_3

    new-instance p0, Lcom/android/camera/animation/folme/FolmeAlphaToOnSubscribe;

    const/4 v0, 0x0

    invoke-static {p1}, Lcom/android/camera/ui/DragLayout$Bar;->access$700(Lcom/android/camera/ui/DragLayout$Bar;)F

    move-result v1

    invoke-direct {p0, p1, v0, v1}, Lcom/android/camera/animation/folme/FolmeAlphaToOnSubscribe;-><init>(Landroid/view/View;FF)V

    invoke-static {p0}, Lio/reactivex/Completable;->create(Lio/reactivex/CompletableOnSubscribe;)Lio/reactivex/Completable;

    move-result-object p0

    invoke-virtual {p0}, Lio/reactivex/Completable;->subscribe()Lio/reactivex/disposables/Disposable;

    goto :goto_1

    :cond_3
    const/16 p0, 0x8

    invoke-virtual {p1, p0}, Landroid/view/View;->setVisibility(I)V

    :cond_4
    :goto_1
    return-void
.end method

.method public setScreenOrientation(I)V
    .locals 1

    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mScreenOrientation:I

    if-eq v0, p1, :cond_0

    iput p1, p0, Lcom/android/camera/ui/DragLayout;->mScreenOrientation:I

    iget p1, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    const/4 v0, 0x4

    if-ne p1, v0, :cond_0

    invoke-virtual {p0}, Lcom/android/camera/ui/DragLayout;->getDragChildren()Landroid/widget/FrameLayout;

    move-result-object p0

    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object p1

    invoke-virtual {p1}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getTotalDragDistance()F

    move-result p1

    neg-float p1, p1

    invoke-virtual {p0, p1}, Landroid/widget/FrameLayout;->setTranslationY(F)V

    :cond_0
    return-void
.end method

.method public shrink(Z)Z
    .locals 2

    iget p1, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    const/4 v0, 0x4

    if-eq p1, v0, :cond_0

    const/4 v0, 0x2

    if-eq p1, v0, :cond_0

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "shrink fail, state error. now state :"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v0, p0, Lcom/android/camera/ui/DragLayout;->mState:I

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lcom/android/camera/ui/DragLayout;->LogD(Ljava/lang/String;)V

    const/4 p0, 0x0

    return p0

    :cond_0
    const/4 p1, 0x0

    invoke-static {}, Lcom/android/camera/ui/DragLayout;->getAnimationConfig()Lcom/android/camera/ui/DragLayout$DragAnimationConfig;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/ui/DragLayout$DragAnimationConfig;->getTotalDragDistance()F

    move-result v0

    iget-object p0, p0, Lcom/android/camera/ui/DragLayout;->mValueAnimatorListener:Lcom/android/camera/animation/FolmeUtils$IPhyAnimatorListener;

    const/4 v1, 0x0

    invoke-static {p1, v1, v0, v1, p0}, Lcom/android/camera/animation/FolmeUtils;->popup(Landroid/view/View;FFFLcom/android/camera/animation/FolmeUtils$IPhyAnimatorListener;)Lmiuix/animation/IStateStyle;

    const/4 p0, 0x1

    return p0
.end method

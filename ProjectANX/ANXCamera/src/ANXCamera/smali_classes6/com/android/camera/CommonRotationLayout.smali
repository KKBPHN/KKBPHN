.class public Lcom/android/camera/CommonRotationLayout;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private mBottomRotationLayout:Landroid/view/View;

.field private mContentRotationLayout:Landroid/view/View;

.field private mLastModuleIndex:I

.field private mLastTargetViewDegree:I

.field private mScreenRotationLayout:Landroid/view/View;

.field private mTopRotationLayout:Landroid/view/View;

.field private mWindow:Landroid/view/Window;


# direct methods
.method public constructor <init>(Landroid/view/Window;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/CommonRotationLayout;->mWindow:Landroid/view/Window;

    invoke-direct {p0}, Lcom/android/camera/CommonRotationLayout;->initView()V

    return-void
.end method

.method private findViewById(I)Landroid/view/View;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/CommonRotationLayout;->mWindow:Landroid/view/Window;

    invoke-virtual {p0, p1}, Landroid/view/Window;->findViewById(I)Landroid/view/View;

    move-result-object p0

    return-object p0
.end method

.method private initView()V
    .locals 1

    const v0, 0x7f0900d5

    invoke-direct {p0, v0}, Lcom/android/camera/CommonRotationLayout;->findViewById(I)Landroid/view/View;

    move-result-object v0

    iput-object v0, p0, Lcom/android/camera/CommonRotationLayout;->mContentRotationLayout:Landroid/view/View;

    const v0, 0x7f090353

    invoke-direct {p0, v0}, Lcom/android/camera/CommonRotationLayout;->findViewById(I)Landroid/view/View;

    move-result-object v0

    iput-object v0, p0, Lcom/android/camera/CommonRotationLayout;->mTopRotationLayout:Landroid/view/View;

    const v0, 0x7f090088

    invoke-direct {p0, v0}, Lcom/android/camera/CommonRotationLayout;->findViewById(I)Landroid/view/View;

    move-result-object v0

    iput-object v0, p0, Lcom/android/camera/CommonRotationLayout;->mBottomRotationLayout:Landroid/view/View;

    const v0, 0x7f09016f

    invoke-direct {p0, v0}, Lcom/android/camera/CommonRotationLayout;->findViewById(I)Landroid/view/View;

    move-result-object v0

    iput-object v0, p0, Lcom/android/camera/CommonRotationLayout;->mScreenRotationLayout:Landroid/view/View;

    return-void
.end method

.method private isForceHorizontalOrientation(I)Z
    .locals 1

    const/4 p0, 0x1

    const/16 v0, 0xb9

    if-ne p1, v0, :cond_0

    return p0

    :cond_0
    const/16 v0, 0xb3

    if-ne p1, v0, :cond_1

    return p0

    :cond_1
    const/16 v0, 0xd4

    if-ne p1, v0, :cond_2

    return p0

    :cond_2
    const/16 v0, 0xbd

    if-ne p1, v0, :cond_3

    return p0

    :cond_3
    const/16 v0, 0xcf

    if-ne p1, v0, :cond_4

    return p0

    :cond_4
    const/16 v0, 0xd5

    if-ne p1, v0, :cond_5

    return p0

    :cond_5
    const/16 v0, 0xd0

    if-ne p1, v0, :cond_6

    return p0

    :cond_6
    const/4 p0, 0x0

    return p0
.end method

.method public static varargs updateOrientationLayoutRotation(I[Landroid/view/View;)V
    .locals 8

    if-nez p1, :cond_0

    return-void

    :cond_0
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    array-length v1, p1

    const/4 v2, 0x0

    move v3, v2

    :goto_0
    if-ge v3, v1, :cond_4

    aget-object v4, p1, v3

    const/4 v5, 0x0

    invoke-virtual {v4, v5}, Landroid/view/View;->setAlpha(F)V

    new-instance v5, Lcom/android/camera/animation/folme/FolmeAlphaInOnSubscribe;

    invoke-direct {v5, v4}, Lcom/android/camera/animation/folme/FolmeAlphaInOnSubscribe;-><init>(Landroid/view/View;)V

    invoke-static {v5}, Lio/reactivex/Completable;->create(Lio/reactivex/CompletableOnSubscribe;)Lio/reactivex/Completable;

    move-result-object v5

    invoke-interface {v0, v5}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    invoke-virtual {v4}, Landroid/view/View;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v5

    check-cast v5, Landroid/widget/FrameLayout$LayoutParams;

    invoke-static {}, Lcom/android/camera/Display;->getScreenOrientation()I

    move-result v6

    if-nez p0, :cond_1

    move v6, v2

    :cond_1
    if-eqz v6, :cond_3

    const/4 v7, 0x1

    if-eq v6, v7, :cond_2

    const/4 v7, 0x2

    if-eq v6, v7, :cond_2

    goto :goto_1

    :cond_2
    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v6

    iput v6, v5, Landroid/widget/FrameLayout$LayoutParams;->width:I

    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v6

    iput v6, v5, Landroid/widget/FrameLayout$LayoutParams;->height:I

    const/16 v6, 0x10

    iput v6, v5, Landroid/widget/FrameLayout$LayoutParams;->gravity:I

    goto :goto_1

    :cond_3
    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v6

    iput v6, v5, Landroid/widget/FrameLayout$LayoutParams;->width:I

    invoke-static {}, Lcom/android/camera/Display;->getWindowHeight()I

    move-result v6

    iput v6, v5, Landroid/widget/FrameLayout$LayoutParams;->height:I

    iput v2, v5, Landroid/widget/FrameLayout$LayoutParams;->gravity:I

    :goto_1
    int-to-float v6, p0

    invoke-virtual {v4, v6}, Landroid/view/View;->setRotation(F)V

    invoke-virtual {v4, v5}, Landroid/view/View;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_4
    invoke-static {v0}, Lio/reactivex/Completable;->merge(Ljava/lang/Iterable;)Lio/reactivex/Completable;

    move-result-object p0

    invoke-virtual {p0}, Lio/reactivex/Completable;->subscribe()Lio/reactivex/disposables/Disposable;

    return-void
.end method


# virtual methods
.method public provideOrientationChanged(II)V
    .locals 4

    rsub-int p2, p2, 0x168

    rem-int/lit16 p2, p2, 0x168

    iget v0, p0, Lcom/android/camera/CommonRotationLayout;->mLastModuleIndex:I

    invoke-direct {p0, v0}, Lcom/android/camera/CommonRotationLayout;->isForceHorizontalOrientation(I)Z

    move-result v0

    if-nez v0, :cond_0

    invoke-direct {p0, p1}, Lcom/android/camera/CommonRotationLayout;->isForceHorizontalOrientation(I)Z

    move-result v0

    if-nez v0, :cond_0

    iget v0, p0, Lcom/android/camera/CommonRotationLayout;->mLastTargetViewDegree:I

    if-ne p2, v0, :cond_0

    return-void

    :cond_0
    iget v0, p0, Lcom/android/camera/CommonRotationLayout;->mLastModuleIndex:I

    invoke-direct {p0, v0}, Lcom/android/camera/CommonRotationLayout;->isForceHorizontalOrientation(I)Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-direct {p0, p1}, Lcom/android/camera/CommonRotationLayout;->isForceHorizontalOrientation(I)Z

    move-result v0

    if-eqz v0, :cond_1

    return-void

    :cond_1
    invoke-direct {p0, p1}, Lcom/android/camera/CommonRotationLayout;->isForceHorizontalOrientation(I)Z

    move-result v0

    const/4 v1, 0x1

    const/4 v2, 0x0

    if-eqz v0, :cond_2

    new-array v0, v1, [Landroid/view/View;

    iget-object v3, p0, Lcom/android/camera/CommonRotationLayout;->mTopRotationLayout:Landroid/view/View;

    aput-object v3, v0, v2

    invoke-static {v2, v0}, Lcom/android/camera/CommonRotationLayout;->updateOrientationLayoutRotation(I[Landroid/view/View;)V

    goto :goto_0

    :cond_2
    new-array v0, v1, [Landroid/view/View;

    iget-object v3, p0, Lcom/android/camera/CommonRotationLayout;->mTopRotationLayout:Landroid/view/View;

    aput-object v3, v0, v2

    invoke-static {p2, v0}, Lcom/android/camera/CommonRotationLayout;->updateOrientationLayoutRotation(I[Landroid/view/View;)V

    :goto_0
    const/4 v0, 0x3

    new-array v0, v0, [Landroid/view/View;

    iget-object v3, p0, Lcom/android/camera/CommonRotationLayout;->mContentRotationLayout:Landroid/view/View;

    aput-object v3, v0, v2

    iget-object v2, p0, Lcom/android/camera/CommonRotationLayout;->mBottomRotationLayout:Landroid/view/View;

    aput-object v2, v0, v1

    const/4 v1, 0x2

    iget-object v2, p0, Lcom/android/camera/CommonRotationLayout;->mScreenRotationLayout:Landroid/view/View;

    aput-object v2, v0, v1

    invoke-static {p2, v0}, Lcom/android/camera/CommonRotationLayout;->updateOrientationLayoutRotation(I[Landroid/view/View;)V

    iput p2, p0, Lcom/android/camera/CommonRotationLayout;->mLastTargetViewDegree:I

    iput p1, p0, Lcom/android/camera/CommonRotationLayout;->mLastModuleIndex:I

    return-void
.end method

.class public Lcom/android/camera/fragment/top/ExtraAdapter;
.super Landroidx/recyclerview/widget/RecyclerView$Adapter;
.source ""

# interfaces
.implements Landroid/view/View$OnClickListener;


# static fields
.field public static final ITEM_TYPE_MULTI:I = 0x1

.field public static final ITEM_TYPE_TOGGLE:I = 0x2


# instance fields
.field private mAnimateHeight:I

.field private mAnimationScaleSize:F

.field private mAnimationStartTime:J

.field private mContext:Landroid/content/Context;

.field private mCurrentRow:I

.field private mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

.field private mDataItemLive:Lcom/android/camera/data/data/extra/DataItemLive;

.field private mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

.field private mOnClickListener:Landroid/view/View$OnClickListener;

.field private mRowLists:Ljava/util/List;

.field private mSlideSwitchListener:Lcom/android/camera/ui/SlideSwitchButton$SlideSwitchListener;

.field private mSupportedConfigs:Lcom/android/camera/data/data/config/SupportedConfigs;


# direct methods
.method public constructor <init>(Landroid/content/Context;Lcom/android/camera/data/data/config/SupportedConfigs;Landroid/view/View$OnClickListener;Lcom/android/camera/ui/SlideSwitchButton$SlideSwitchListener;)V
    .locals 1

    invoke-direct {p0}, Landroidx/recyclerview/widget/RecyclerView$Adapter;-><init>()V

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mRowLists:Ljava/util/List;

    const/4 v0, -0x1

    iput v0, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mCurrentRow:I

    iput-object p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mContext:Landroid/content/Context;

    iput-object p2, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mSupportedConfigs:Lcom/android/camera/data/data/config/SupportedConfigs;

    iput-object p3, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mOnClickListener:Landroid/view/View$OnClickListener;

    iput-object p4, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mSlideSwitchListener:Lcom/android/camera/ui/SlideSwitchButton$SlideSwitchListener;

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object p1

    iput-object p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemConfig()Lcom/android/camera/data/data/config/DataItemConfig;

    move-result-object p1

    iput-object p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemLive()Lcom/android/camera/data/data/extra/DataItemLive;

    move-result-object p1

    iput-object p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemLive:Lcom/android/camera/data/data/extra/DataItemLive;

    const p1, 0x7f070355

    const p2, 0x3ec28f5c    # 0.38f

    invoke-static {p1, p2}, Lcom/android/camera/CameraSettings;->getResourceFloat(IF)F

    move-result p1

    iput p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mAnimationScaleSize:F

    return-void
.end method

.method private getRow(I)I
    .locals 5

    const/4 v0, 0x0

    const/4 v1, -0x1

    move v2, v1

    move v1, v0

    :goto_0
    invoke-virtual {p0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemCount()I

    move-result v3

    if-ge v0, v3, :cond_4

    invoke-virtual {p0, v0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemViewType(I)I

    move-result v3

    const/4 v4, 0x1

    if-ne v3, v4, :cond_0

    :goto_1
    add-int/lit8 v2, v2, 0x1

    goto :goto_2

    :cond_0
    if-nez v1, :cond_1

    add-int/lit8 v1, v2, 0x1

    :cond_1
    sub-int v3, v0, v1

    rem-int/lit8 v3, v3, 0x4

    if-nez v3, :cond_2

    goto :goto_1

    :cond_2
    :goto_2
    if-ne p1, v0, :cond_3

    goto :goto_3

    :cond_3
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_4
    :goto_3
    return v2
.end method

.method private getTotalRow()I
    .locals 5

    const/4 v0, 0x0

    move v1, v0

    move v2, v1

    :goto_0
    invoke-virtual {p0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemCount()I

    move-result v3

    if-ge v0, v3, :cond_3

    invoke-virtual {p0, v0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemViewType(I)I

    move-result v3

    const/4 v4, 0x1

    if-ne v3, v4, :cond_0

    :goto_1
    add-int/lit8 v1, v1, 0x1

    goto :goto_2

    :cond_0
    if-nez v2, :cond_1

    move v2, v1

    :cond_1
    sub-int v3, v0, v2

    rem-int/lit8 v3, v3, 0x4

    if-nez v3, :cond_2

    goto :goto_1

    :cond_2
    :goto_2
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_3
    return v1
.end method

.method private setAnimation(Ljava/util/List;II)V
    .locals 8

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    iput-wide v0, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mAnimationStartTime:J

    invoke-interface {p1}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :goto_0
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/view/View;

    const v1, 0x3e3851ec    # 0.18f

    add-int/lit8 v2, p3, -0x1

    sub-int v3, v2, p2

    int-to-float v3, v3

    const v4, 0x3c23d70a    # 0.01f

    mul-float/2addr v4, v3

    sub-float/2addr v1, v4

    const v4, 0x3fae147b    # 1.36f

    const v5, 0x3ca3d70a    # 0.02f

    mul-float/2addr v3, v5

    sub-float/2addr v4, v3

    iget v3, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mAnimateHeight:I

    neg-int v3, v3

    int-to-float v3, v3

    invoke-static {v0, v3}, Landroidx/core/view/ViewCompat;->setTranslationY(Landroid/view/View;F)V

    invoke-static {v0}, Landroidx/core/view/ViewCompat;->animate(Landroid/view/View;)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v3

    new-instance v5, Landroid/view/animation/PathInterpolator;

    const v6, 0x3e99999a    # 0.3f

    const/high16 v7, 0x3f800000    # 1.0f

    invoke-direct {v5, v1, v4, v6, v7}, Landroid/view/animation/PathInterpolator;-><init>(FFFF)V

    invoke-virtual {v3, v5}, Landroidx/core/view/ViewPropertyAnimatorCompat;->setInterpolator(Landroid/view/animation/Interpolator;)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v1

    const/4 v3, 0x0

    invoke-virtual {v1, v3}, Landroidx/core/view/ViewPropertyAnimatorCompat;->translationY(F)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v1

    mul-int/lit8 v4, v2, 0x14

    mul-int/lit8 v5, p2, 0x14

    sub-int/2addr v4, v5

    rsub-int v5, v4, 0x208

    int-to-long v5, v5

    invoke-virtual {v1, v5, v6}, Landroidx/core/view/ViewPropertyAnimatorCompat;->setDuration(J)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v1

    int-to-long v4, v4

    invoke-virtual {v1, v4, v5}, Landroidx/core/view/ViewPropertyAnimatorCompat;->setStartDelay(J)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v1

    invoke-virtual {v1}, Landroidx/core/view/ViewPropertyAnimatorCompat;->start()V

    invoke-static {v0, v3}, Landroidx/core/view/ViewCompat;->setAlpha(Landroid/view/View;F)V

    invoke-static {v0}, Landroidx/core/view/ViewCompat;->animate(Landroid/view/View;)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v0

    new-instance v1, Lmiui/view/animation/CubicEaseOutInterpolator;

    invoke-direct {v1}, Lmiui/view/animation/CubicEaseOutInterpolator;-><init>()V

    invoke-virtual {v0, v1}, Landroidx/core/view/ViewPropertyAnimatorCompat;->setInterpolator(Landroid/view/animation/Interpolator;)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v0

    invoke-virtual {v0, v7}, Landroidx/core/view/ViewPropertyAnimatorCompat;->alpha(F)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v0

    const-wide/16 v3, 0x190

    invoke-virtual {v0, v3, v4}, Landroidx/core/view/ViewPropertyAnimatorCompat;->setDuration(J)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v0

    mul-int/lit8 v2, v2, 0x32

    mul-int/lit8 v1, p2, 0x32

    sub-int/2addr v2, v1

    int-to-long v1, v2

    invoke-virtual {v0, v1, v2}, Landroidx/core/view/ViewPropertyAnimatorCompat;->setStartDelay(J)Landroidx/core/view/ViewPropertyAnimatorCompat;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/core/view/ViewPropertyAnimatorCompat;->start()V

    goto :goto_0

    :cond_0
    return-void
.end method


# virtual methods
.method public animationRunning()Z
    .locals 4

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    iget-wide v2, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mAnimationStartTime:J

    sub-long/2addr v0, v2

    const-wide/16 v2, 0x1f4

    cmp-long p0, v0, v2

    if-gez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public getItemCount()I
    .locals 0

    iget-object p0, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mSupportedConfigs:Lcom/android/camera/data/data/config/SupportedConfigs;

    invoke-virtual {p0}, Lcom/android/camera/data/data/config/SupportedConfigs;->getLength()I

    move-result p0

    return p0
.end method

.method public getItemCount(I)I
    .locals 3

    const/4 v0, 0x0

    move v1, v0

    :goto_0
    iget-object v2, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mSupportedConfigs:Lcom/android/camera/data/data/config/SupportedConfigs;

    invoke-virtual {v2}, Lcom/android/camera/data/data/config/SupportedConfigs;->getLength()I

    move-result v2

    if-ge v0, v2, :cond_1

    invoke-virtual {p0, v0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemViewType(I)I

    move-result v2

    if-ne p1, v2, :cond_0

    add-int/lit8 v1, v1, 0x1

    :cond_0
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    return v1
.end method

.method public getItemViewType(I)I
    .locals 0

    iget-object p0, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mSupportedConfigs:Lcom/android/camera/data/data/config/SupportedConfigs;

    invoke-virtual {p0, p1}, Lcom/android/camera/data/data/config/SupportedConfigs;->getConfig(I)I

    move-result p0

    const/16 p1, 0xad

    if-eq p0, p1, :cond_0

    const/16 p1, 0xae

    if-eq p0, p1, :cond_0

    const/16 p1, 0xbb

    if-eq p0, p1, :cond_0

    const/16 p1, 0xcc

    if-eq p0, p1, :cond_0

    const/16 p1, 0xd0

    if-eq p0, p1, :cond_0

    const/16 p1, 0xd2

    if-eq p0, p1, :cond_0

    const/16 p1, 0xd5

    if-eq p0, p1, :cond_0

    const/16 p1, 0xe2

    if-eq p0, p1, :cond_0

    const/4 p0, 0x2

    return p0

    :cond_0
    const/4 p0, 0x1

    return p0
.end method

.method public getMuitiContentDescriptionString(I)I
    .locals 0
    .annotation build Landroidx/annotation/StringRes;
    .end annotation

    iget-object p0, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mSupportedConfigs:Lcom/android/camera/data/data/config/SupportedConfigs;

    invoke-virtual {p0, p1}, Lcom/android/camera/data/data/config/SupportedConfigs;->getConfig(I)I

    move-result p0

    const/16 p1, 0xad

    if-eq p0, p1, :cond_4

    const/16 p1, 0xae

    if-eq p0, p1, :cond_3

    const/16 p1, 0xbb

    if-eq p0, p1, :cond_2

    const/16 p1, 0xcc

    if-eq p0, p1, :cond_3

    const/16 p1, 0xd0

    if-eq p0, p1, :cond_2

    const/16 p1, 0xd2

    if-eq p0, p1, :cond_1

    const/16 p1, 0xd5

    if-eq p0, p1, :cond_4

    const/16 p1, 0xe2

    if-eq p0, p1, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    const p0, 0x7f1004b1

    return p0

    :cond_1
    const p0, 0x7f10058b

    return p0

    :cond_2
    const p0, 0x7f100685

    return p0

    :cond_3
    const p0, 0x7f100674

    return p0

    :cond_4
    const p0, 0x7f10068e

    return p0
.end method

.method public getTotalRow(I)I
    .locals 5

    const/4 v0, 0x0

    const/4 v1, 0x1

    if-ne p1, v1, :cond_0

    move v1, v0

    :goto_0
    invoke-virtual {p0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemCount()I

    move-result v2

    if-ge v0, v2, :cond_5

    invoke-virtual {p0, v0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemViewType(I)I

    move-result v2

    if-ne v2, p1, :cond_5

    add-int/lit8 v1, v1, 0x1

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    const/4 p1, -0x1

    move v3, p1

    move v2, v0

    :goto_1
    invoke-virtual {p0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemCount()I

    move-result v4

    if-ge v0, v4, :cond_4

    invoke-virtual {p0, v0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemViewType(I)I

    move-result v4

    if-ne v4, v1, :cond_1

    :goto_2
    add-int/lit8 v2, v2, 0x1

    goto :goto_3

    :cond_1
    if-ne v3, p1, :cond_2

    move v3, v2

    :cond_2
    sub-int v4, v0, v3

    rem-int/lit8 v4, v4, 0x4

    if-nez v4, :cond_3

    goto :goto_2

    :cond_3
    :goto_3
    add-int/lit8 v0, v0, 0x1

    goto :goto_1

    :cond_4
    sub-int v1, v2, v3

    :cond_5
    return v1
.end method

.method public bridge synthetic onBindViewHolder(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;I)V
    .locals 0

    check-cast p1, Lcom/android/camera/fragment/CommonRecyclerViewHolder;

    invoke-virtual {p0, p1, p2}, Lcom/android/camera/fragment/top/ExtraAdapter;->onBindViewHolder(Lcom/android/camera/fragment/CommonRecyclerViewHolder;I)V

    return-void
.end method

.method public onBindViewHolder(Lcom/android/camera/fragment/CommonRecyclerViewHolder;I)V
    .locals 19

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move/from16 v2, p2

    iget-object v3, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mSupportedConfigs:Lcom/android/camera/data/data/config/SupportedConfigs;

    invoke-virtual {v3, v2}, Lcom/android/camera/data/data/config/SupportedConfigs;->getConfig(I)I

    move-result v3

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object v4

    invoke-virtual {v4}, Lcom/android/camera/data/data/global/DataItemGlobal;->getCurrentMode()I

    move-result v4

    const/4 v7, 0x1

    const/4 v8, 0x0

    sparse-switch v3, :sswitch_data_0

    :cond_0
    move v5, v7

    move v10, v8

    move v12, v10

    move v13, v12

    move v14, v13

    move v15, v14

    const/4 v4, 0x0

    :goto_0
    const/4 v9, -0x1

    :goto_1
    const/4 v11, 0x0

    goto/16 :goto_12

    :sswitch_0
    const v4, 0x7f08020a

    const v9, 0x7f0f0014

    const v10, 0x7f1006e0

    iget-object v11, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    const-string v12, "pref_speech_shutter"

    goto :goto_2

    :sswitch_1
    invoke-static {v4}, Lcom/android/camera/CameraSettings;->isProVideoHistogramOpen(I)Z

    move-result v4

    const v9, 0x7f08035f

    const v10, 0x7f0f000e

    const v11, 0x7f100415

    goto/16 :goto_9

    :sswitch_2
    invoke-static {v4}, Lcom/android/camera/CameraSettings;->isProVideoLogOpen(I)Z

    move-result v4

    const v9, 0x7f080365

    const v10, 0x7f0f0011

    const v11, 0x7f10034d

    goto/16 :goto_9

    :sswitch_3
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    const-string v9, "pref_camera_exposure_feedback"

    invoke-virtual {v4, v9}, Lcom/android/camera/data/data/runing/DataItemRunning;->isSwitchOn(Ljava/lang/String;)Z

    move-result v4

    const v9, 0x7f0801ed

    const v10, 0x7f0f0009

    const v11, 0x7f10048a

    goto/16 :goto_9

    :sswitch_4
    iget-object v9, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningMacroMode()Lcom/android/camera/data/data/config/ComponentRunningMacroMode;

    move-result-object v9

    invoke-virtual {v9, v4}, Lcom/android/camera/data/data/config/ComponentRunningMacroMode;->isSwitchOn(I)Z

    move-result v4

    const v10, 0x7f08038b

    const v11, 0x7f0f0012

    invoke-virtual {v9}, Lcom/android/camera/data/data/config/ComponentRunningMacroMode;->getResText()I

    move-result v9

    goto/16 :goto_7

    :sswitch_5
    iget-object v9, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningAutoZoom()Lcom/android/camera/data/data/runing/ComponentRunningAutoZoom;

    move-result-object v9

    invoke-virtual {v9, v4}, Lcom/android/camera/data/data/runing/ComponentRunningAutoZoom;->isSwitchOn(I)Z

    move-result v4

    const v10, 0x7f08034d

    const v11, 0x7f0f0007

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/ComponentRunningAutoZoom;->getResText()I

    move-result v9

    goto/16 :goto_7

    :sswitch_6
    const v4, 0x7f0801f7

    const v9, 0x7f0f000d

    const v10, 0x7f1002eb

    iget-object v11, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    const-string v12, "pref_hand_gesture"

    :goto_2
    invoke-virtual {v11, v12}, Lcom/android/camera/data/data/runing/DataItemRunning;->isSwitchOn(Ljava/lang/String;)Z

    move-result v11

    goto/16 :goto_8

    :sswitch_7
    invoke-static {v4}, Lcom/android/camera/CameraSettings;->isCinematicAspectRatioEnabled(I)Z

    move-result v4

    const v9, 0x7f080358

    const v10, 0x7f0f0008

    const v11, 0x7f1003eb

    goto/16 :goto_9

    :sswitch_8
    invoke-static {}, Lcom/android/camera/Util;->isGlobalVersion()Z

    move-result v4

    if-eqz v4, :cond_0

    const v4, 0x7f0801e1

    const v9, 0x7f100627

    const v10, 0x7f0f000b

    move v15, v4

    move v5, v7

    move v12, v8

    move v13, v12

    move v14, v10

    goto :goto_3

    :sswitch_9
    const v9, 0x7f100585

    iget-object v10, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

    invoke-virtual {v10}, Lcom/android/camera/data/data/config/DataItemConfig;->getComponentConfigRaw()Lcom/android/camera/data/data/config/ComponentConfigRaw;

    move-result-object v10

    invoke-virtual {v10, v4}, Lcom/android/camera/data/data/config/ComponentConfigRaw;->isSwitchOn(I)Z

    move-result v4

    const v10, 0x7f080327

    goto/16 :goto_b

    :sswitch_a
    const v4, 0x7f0801f0

    const v9, 0x7f100693

    invoke-static {}, Lcom/android/camera/module/ModuleManager;->isFastMotionModule()Z

    move-result v10

    move v15, v4

    move v5, v7

    move v12, v8

    move v14, v12

    move v13, v10

    goto/16 :goto_c

    :sswitch_b
    const v9, 0x7f080361

    const v10, 0x7f0f000c

    const v11, 0x7f100243

    invoke-static {}, Lcom/android/camera/CameraSettings;->isGradienterOn()Z

    move-result v12

    invoke-static {v4}, Lcom/android/camera/CameraSettings;->isAutoZoomEnabled(I)Z

    move-result v4

    move v5, v7

    if-eqz v4, :cond_1

    move v12, v8

    move v13, v12

    move v15, v9

    move v14, v10

    move v9, v11

    :goto_3
    const/4 v4, 0x0

    const/4 v11, 0x0

    move v10, v13

    goto/16 :goto_12

    :cond_1
    move v15, v9

    move v14, v10

    move v9, v11

    move v13, v12

    const/4 v4, 0x0

    const/4 v11, 0x0

    move v10, v8

    move v12, v10

    goto/16 :goto_12

    :sswitch_c
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v4}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningTiltValue()Lcom/android/camera/data/data/runing/ComponentRunningTiltValue;

    move-result-object v4

    const/16 v9, 0xa0

    invoke-virtual {v4, v9}, Lcom/android/camera/data/data/runing/ComponentRunningTiltValue;->isSwitchOn(I)Z

    move-result v4

    const v9, 0x7f100245

    const v10, 0x7f080363

    const v11, 0x7f0f0016

    goto/16 :goto_7

    :sswitch_d
    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v9

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningColorEnhance()Lcom/android/camera/data/data/runing/ComponentRunningColorEnhance;

    move-result-object v9

    invoke-virtual {v9, v4}, Lcom/android/camera/data/data/runing/ComponentRunningColorEnhance;->isEnabled(I)Z

    move-result v4

    invoke-virtual {v9, v4}, Lcom/android/camera/data/data/runing/ComponentRunningColorEnhance;->getResIcon(Z)I

    move-result v10

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/ComponentRunningColorEnhance;->getDisplayTitleString()I

    move-result v9

    goto/16 :goto_b

    :sswitch_e
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v4}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningTimer()Lcom/android/camera/data/data/runing/ComponentRunningTimer;

    move-result-object v4

    goto/16 :goto_e

    :sswitch_f
    const v4, 0x7f08035e

    const v9, 0x7f100241

    move v15, v4

    move v5, v7

    move v10, v8

    move v12, v10

    move v13, v12

    move v14, v13

    const/4 v4, 0x0

    goto/16 :goto_1

    :sswitch_10
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v4}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningAIWatermark()Lcom/android/camera/data/data/runing/ComponentRunningAIWatermark;

    move-result-object v4

    const v9, 0x7f100126

    invoke-static {}, Lcom/android/camera/CameraSettings;->getCameraId()I

    move-result v10

    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object v11

    invoke-virtual {v11}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getUltraWideCameraId()I

    move-result v11

    if-eq v11, v10, :cond_3

    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object v11

    invoke-virtual {v11}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getStandaloneMacroCameraId()I

    move-result v11

    if-ne v11, v10, :cond_2

    goto :goto_4

    :cond_2
    move v10, v8

    goto :goto_5

    :cond_3
    :goto_4
    move v10, v7

    :goto_5
    if-eqz v10, :cond_4

    iget-object v10, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v10}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningAIWatermark()Lcom/android/camera/data/data/runing/ComponentRunningAIWatermark;

    move-result-object v10

    invoke-virtual {v10, v8}, Lcom/android/camera/data/data/runing/ComponentRunningAIWatermark;->setAIWatermarkEnable(Z)V

    move v10, v8

    goto :goto_6

    :cond_4
    move v10, v7

    :goto_6
    invoke-virtual {v4}, Lcom/android/camera/data/data/runing/ComponentRunningAIWatermark;->getAIWatermarkEnable()Z

    move-result v4

    const v11, 0x7f08019d

    move v13, v4

    move v12, v8

    move v14, v12

    move v5, v10

    move v15, v11

    goto/16 :goto_c

    :sswitch_11
    iget-object v9, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningDocument()Lcom/android/camera/data/data/runing/ComponentRunningDocument;

    move-result-object v9

    invoke-virtual {v9, v4}, Lcom/android/camera/data/data/runing/ComponentRunningDocument;->isSwitchOn(I)Z

    move-result v4

    const v9, 0x7f080126

    const v10, 0x7f100615

    move v13, v4

    move v5, v7

    move v12, v8

    move v14, v12

    move v15, v9

    move v9, v10

    goto/16 :goto_c

    :sswitch_12
    iget-object v9, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningSubtitle()Lcom/android/camera/data/data/runing/ComponentRunningSubtitle;

    move-result-object v9

    invoke-virtual {v9, v4}, Lcom/android/camera/data/data/runing/ComponentRunningSubtitle;->isSwitchOn(I)Z

    move-result v4

    const v10, 0x7f080362

    const v11, 0x7f0f0015

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/ComponentRunningSubtitle;->getResText()I

    move-result v9

    :goto_7
    move v13, v4

    move v5, v7

    move v12, v8

    move v15, v10

    move v14, v11

    goto/16 :goto_a

    :sswitch_13
    const v4, 0x7f080360

    const v9, 0x7f0f0010

    const v10, 0x7f10059c

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object v11

    const-string v12, "pref_camera_referenceline_key"

    invoke-virtual {v11, v12, v8}, Lcom/android/camera/data/data/DataItemBase;->getBoolean(Ljava/lang/String;Z)Z

    move-result v11

    :goto_8
    move v15, v4

    move v5, v7

    move v12, v8

    move v14, v9

    move v9, v10

    move v13, v11

    goto/16 :goto_a

    :sswitch_14
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

    invoke-virtual {v4}, Lcom/android/camera/data/data/config/DataItemConfig;->getComponentConfigSlowMotionQuality()Lcom/android/camera/data/data/config/ComponentConfigSlowMotionQuality;

    move-result-object v4

    goto/16 :goto_e

    :sswitch_15
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

    invoke-virtual {v4}, Lcom/android/camera/data/data/config/DataItemConfig;->getComponentConfigRatio()Lcom/android/camera/data/data/config/ComponentConfigRatio;

    move-result-object v4

    move v5, v7

    move v12, v5

    move v10, v8

    move v13, v10

    goto/16 :goto_f

    :sswitch_16
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v4}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentUltraPixel()Lcom/android/camera/data/data/config/ComponentRunningUltraPixel;

    move-result-object v4

    invoke-virtual {v4}, Lcom/android/camera/data/data/config/ComponentRunningUltraPixel;->getMenuDrawable()I

    move-result v9

    invoke-virtual {v4}, Lcom/android/camera/data/data/config/ComponentRunningUltraPixel;->getMenuString()Ljava/lang/String;

    move-result-object v4

    invoke-static {}, Lcom/android/camera/CameraSettings;->isUltraPixelOn()Z

    move-result v10

    move-object v11, v4

    move v5, v7

    move v12, v8

    move v14, v12

    move v15, v9

    move v13, v10

    const/4 v4, 0x0

    const/4 v9, -0x1

    goto :goto_d

    :sswitch_17
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

    invoke-virtual {v4}, Lcom/android/camera/data/data/config/DataItemConfig;->getComponentConfigVideoQuality()Lcom/android/camera/data/data/config/ComponentConfigVideoQuality;

    move-result-object v4

    goto/16 :goto_e

    :sswitch_18
    invoke-static {}, Lcom/android/camera/CameraSettings;->isLiveShotOn()Z

    move-result v4

    const v9, 0x7f080392

    const v10, 0x7f0f0013

    const v11, 0x7f1001dc

    goto :goto_9

    :sswitch_19
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

    invoke-virtual {v4}, Lcom/android/camera/data/data/config/DataItemConfig;->getComponentConfigSlowMotion()Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;

    move-result-object v4

    goto :goto_e

    :sswitch_1a
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    const-string v9, "pref_camera_peak_key"

    invoke-virtual {v4, v9}, Lcom/android/camera/data/data/runing/DataItemRunning;->isSwitchOn(Ljava/lang/String;)Z

    move-result v4

    const v9, 0x7f0801f1

    const v10, 0x7f0f000a

    const v11, 0x7f10048b

    :goto_9
    move v13, v4

    move v5, v7

    move v12, v8

    move v15, v9

    move v14, v10

    move v9, v11

    :goto_a
    const/4 v4, 0x0

    const/4 v11, 0x0

    move v10, v12

    goto/16 :goto_12

    :sswitch_1b
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemLive:Lcom/android/camera/data/data/extra/DataItemLive;

    invoke-virtual {v4}, Lcom/android/camera/data/data/extra/DataItemLive;->getComponentLiveVideoQuality()Lcom/android/camera/data/data/extra/ComponentLiveVideoQuality;

    move-result-object v4

    goto :goto_e

    :sswitch_1c
    iget-object v9, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemRunning:Lcom/android/camera/data/data/runing/DataItemRunning;

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningAiEnhancedVideo()Lcom/android/camera/data/data/runing/ComponentRunningAiEnhancedVideo;

    move-result-object v9

    invoke-virtual {v9, v4}, Lcom/android/camera/data/data/runing/ComponentRunningAiEnhancedVideo;->isSwitchOn(I)Z

    move-result v4

    const v10, 0x7f08038a

    invoke-virtual {v9}, Lcom/android/camera/data/data/runing/ComponentRunningAiEnhancedVideo;->getResText()I

    move-result v9

    :goto_b
    move v13, v4

    move v5, v7

    move v12, v8

    move v14, v12

    move v15, v10

    :goto_c
    const/4 v4, 0x0

    const/4 v11, 0x0

    :goto_d
    move v10, v14

    goto/16 :goto_12

    :sswitch_1d
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

    invoke-virtual {v4}, Lcom/android/camera/data/data/config/DataItemConfig;->getComponentConfigVideoSubFps()Lcom/android/camera/data/data/config/ComponentConfigVideoSubFPS;

    move-result-object v4

    goto :goto_e

    :sswitch_1e
    iget-object v4, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mDataItemConfig:Lcom/android/camera/data/data/config/DataItemConfig;

    invoke-virtual {v4}, Lcom/android/camera/data/data/config/DataItemConfig;->getComponentConfigVideoSubQuality()Lcom/android/camera/data/data/config/ComponentConfigVideoSubQuality;

    move-result-object v4

    :goto_e
    move v5, v7

    move v10, v8

    move v12, v10

    move v13, v12

    :goto_f
    move v14, v13

    move v15, v14

    goto/16 :goto_0

    :sswitch_1f
    invoke-static {}, Lcom/android/camera/CameraSettings;->isTimerBurstEnable()Z

    move-result v4

    const v9, 0x7f0805bb

    const v10, 0x7f0f0017

    if-eqz v4, :cond_6

    invoke-static {}, Lcom/android/camera/CameraSettings;->getTimerBurstTotalCount()I

    move-result v11

    iget-object v12, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mContext:Landroid/content/Context;

    invoke-virtual {v12}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v12

    const v13, 0x7f0e000a

    invoke-virtual {v12, v13, v11}, Landroid/content/res/Resources;->getQuantityString(II)Ljava/lang/String;

    move-result-object v12

    new-array v13, v7, [Ljava/lang/Object;

    invoke-static {v11}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v11

    aput-object v11, v13, v8

    invoke-static {v12, v13}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v11

    invoke-static {}, Lcom/android/camera/Util;->isLocaleChinese()Z

    move-result v12

    const-string v13, "S"

    if-eqz v12, :cond_5

    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    goto :goto_10

    :cond_5
    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    const-string v14, " "

    invoke-virtual {v12, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    :goto_10
    invoke-static {}, Lcom/android/camera/CameraSettings;->getTimerBurstInterval()I

    move-result v14

    invoke-virtual {v12, v14}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v12

    new-instance v13, Ljava/lang/StringBuilder;

    invoke-direct {v13}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v13, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    move v13, v4

    move v5, v7

    move v12, v8

    move v15, v9

    move v14, v10

    const/4 v4, 0x0

    const/4 v9, -0x1

    goto :goto_11

    :cond_6
    const v11, 0x7f100710

    move v13, v4

    move v5, v7

    move v12, v8

    move v15, v9

    move v14, v10

    move v9, v11

    const/4 v4, 0x0

    const/4 v11, 0x0

    :goto_11
    move v10, v5

    :goto_12
    iget-object v8, v1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-virtual {v8}, Landroid/view/View;->getResources()Landroid/content/res/Resources;

    move-result-object v8

    invoke-virtual {v0, v2}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemViewType(I)I

    move-result v6

    if-ne v6, v7, :cond_7

    invoke-virtual {v4}, Lcom/android/camera/data/data/ComponentData;->disableUpdate()Z

    move-result v5

    xor-int/2addr v5, v7

    const v6, 0x7f090352

    invoke-virtual {v1, v6}, Lcom/android/camera/fragment/CommonRecyclerViewHolder;->getView(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Lcom/android/camera/ui/SlideSwitchButton;

    invoke-virtual {v1, v4, v3, v12}, Lcom/android/camera/ui/SlideSwitchButton;->setComponentData(Lcom/android/camera/data/data/ComponentData;IZ)V

    invoke-virtual {v1, v5}, Landroid/view/ViewGroup;->setEnabled(Z)V

    iget-object v3, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mSlideSwitchListener:Lcom/android/camera/ui/SlideSwitchButton$SlideSwitchListener;

    invoke-virtual {v1, v3}, Lcom/android/camera/ui/SlideSwitchButton;->setSlideSwitchListener(Lcom/android/camera/ui/SlideSwitchButton$SlideSwitchListener;)V

    invoke-virtual {v0, v2}, Lcom/android/camera/fragment/top/ExtraAdapter;->getMuitiContentDescriptionString(I)I

    move-result v0

    if-lez v0, :cond_12

    invoke-virtual {v8, v0}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Landroid/view/ViewGroup;->setContentDescription(Ljava/lang/CharSequence;)V

    goto/16 :goto_1b

    :cond_7
    const v2, 0x7f09012b

    invoke-virtual {v1, v2}, Lcom/android/camera/fragment/CommonRecyclerViewHolder;->getView(I)Landroid/view/View;

    move-result-object v2

    const v4, 0x7f09012a

    invoke-virtual {v1, v4}, Lcom/android/camera/fragment/CommonRecyclerViewHolder;->getView(I)Landroid/view/View;

    move-result-object v4

    check-cast v4, Lcom/android/camera/ui/ColorCircleBackgroundView;

    const v6, 0x7f090129

    invoke-virtual {v1, v6}, Lcom/android/camera/fragment/CommonRecyclerViewHolder;->getView(I)Landroid/view/View;

    move-result-object v6

    check-cast v6, Lcom/airbnb/lottie/LottieAnimationView;

    const v12, 0x7f0902cd

    invoke-virtual {v1, v12}, Lcom/android/camera/fragment/CommonRecyclerViewHolder;->getView(I)Landroid/view/View;

    move-result-object v12

    const v7, 0x7f09012c

    invoke-virtual {v1, v7}, Lcom/android/camera/fragment/CommonRecyclerViewHolder;->getView(I)Landroid/view/View;

    move-result-object v7

    check-cast v7, Landroid/widget/TextView;

    move/from16 v18, v15

    const/4 v15, 0x1

    invoke-virtual {v7, v15}, Landroid/widget/TextView;->setSelected(Z)V

    const v15, 0x7f0901ac

    invoke-virtual {v1, v15}, Lcom/android/camera/fragment/CommonRecyclerViewHolder;->getView(I)Landroid/view/View;

    move-result-object v15

    check-cast v15, Landroid/widget/ImageView;

    move-object/from16 p2, v6

    const/4 v6, -0x1

    if-eq v9, v6, :cond_8

    invoke-virtual {v7, v9}, Landroid/widget/TextView;->setText(I)V

    goto :goto_13

    :cond_8
    invoke-virtual {v7, v11}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    :goto_13
    const/4 v6, 0x2

    const-string v9, ","

    if-eqz v10, :cond_b

    invoke-static {}, Lcom/android/camera/Util;->isAccessible()Z

    move-result v7

    if-eqz v7, :cond_9

    iget-object v7, v1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-virtual {v7, v6}, Landroid/view/View;->setImportantForAccessibility(I)V

    const/4 v6, 0x1

    invoke-virtual {v2, v6}, Landroid/view/View;->setImportantForAccessibility(I)V

    invoke-virtual {v12, v6}, Landroid/view/View;->setImportantForAccessibility(I)V

    :cond_9
    const/4 v6, 0x0

    invoke-virtual {v15, v6}, Landroid/widget/ImageView;->setVisibility(I)V

    iget-object v6, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mOnClickListener:Landroid/view/View$OnClickListener;

    invoke-virtual {v2, v6}, Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    iget-object v6, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mOnClickListener:Landroid/view/View$OnClickListener;

    invoke-virtual {v12, v6}, Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    if-eqz v13, :cond_a

    invoke-static {}, Lcom/android/camera/CameraSettings;->getTimerBurstTotalCount()I

    move-result v6

    invoke-static {}, Lcom/android/camera/CameraSettings;->getTimerBurstInterval()I

    move-result v7

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    const v11, 0x7f1000bc

    invoke-virtual {v8, v11}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/4 v15, 0x1

    new-array v11, v15, [Ljava/lang/Object;

    invoke-static {v6}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v17

    const/16 v16, 0x0

    aput-object v17, v11, v16

    const v15, 0x7f0e0001

    invoke-virtual {v8, v15, v6, v11}, Landroid/content/res/Resources;->getQuantityString(II[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v10, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const v6, 0x7f0e0002

    const/4 v9, 0x1

    new-array v11, v9, [Ljava/lang/Object;

    invoke-static {v7}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v9

    aput-object v9, v11, v16

    invoke-virtual {v8, v6, v7, v11}, Landroid/content/res/Resources;->getQuantityString(II[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v10, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    goto :goto_14

    :cond_a
    const v6, 0x7f1000bb

    invoke-virtual {v8, v6}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v6

    :goto_14
    invoke-virtual {v2, v6}, Landroid/view/View;->setContentDescription(Ljava/lang/CharSequence;)V

    const v6, 0x7f1000bd

    invoke-virtual {v8, v6}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v12, v6}, Landroid/view/View;->setContentDescription(Ljava/lang/CharSequence;)V

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    invoke-virtual {v12, v6}, Landroid/view/View;->setTag(Ljava/lang/Object;)V

    const/4 v15, 0x1

    goto :goto_17

    :cond_b
    const/16 v10, 0x8

    invoke-virtual {v15, v10}, Landroid/widget/ImageView;->setVisibility(I)V

    iget-object v10, v1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v11

    invoke-virtual {v10, v11}, Landroid/view/View;->setTag(Ljava/lang/Object;)V

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v7}, Landroid/widget/TextView;->getText()Ljava/lang/CharSequence;

    move-result-object v7

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/CharSequence;)Ljava/lang/StringBuilder;

    if-eqz v13, :cond_c

    invoke-virtual {v10, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const v7, 0x7f10008b

    :goto_15
    invoke-virtual {v8, v7}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v10, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    goto :goto_16

    :cond_c
    const/16 v7, 0xe1

    if-eq v3, v7, :cond_d

    invoke-virtual {v10, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const v7, 0x7f100041

    goto :goto_15

    :cond_d
    :goto_16
    invoke-virtual {v2, v10}, Landroid/view/View;->setContentDescription(Ljava/lang/CharSequence;)V

    invoke-static {}, Lcom/android/camera/Util;->isAccessible()Z

    move-result v7

    if-eqz v7, :cond_e

    iget-object v7, v1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    const/4 v15, 0x1

    invoke-virtual {v7, v15}, Landroid/view/View;->setImportantForAccessibility(I)V

    invoke-virtual {v2, v6}, Landroid/view/View;->setImportantForAccessibility(I)V

    invoke-virtual {v12, v6}, Landroid/view/View;->setImportantForAccessibility(I)V

    iget-object v6, v1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    iget-object v7, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mOnClickListener:Landroid/view/View$OnClickListener;

    invoke-virtual {v6, v7}, Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    iget-object v6, v1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-virtual {v6, v10}, Landroid/view/View;->setContentDescription(Ljava/lang/CharSequence;)V

    goto :goto_17

    :cond_e
    const/4 v15, 0x1

    iget-object v6, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mOnClickListener:Landroid/view/View$OnClickListener;

    invoke-virtual {v2, v6}, Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    :goto_17
    if-eqz v13, :cond_f

    invoke-virtual {v4}, Lcom/android/camera/ui/ColorCircleBackgroundView;->isSwitchOn()Z

    move-result v6

    if-nez v6, :cond_f

    invoke-virtual {v2}, Landroid/view/View;->getTag()Ljava/lang/Object;

    move-result-object v6

    if-eqz v6, :cond_f

    if-eqz v14, :cond_f

    goto :goto_18

    :cond_f
    const/4 v15, 0x0

    :goto_18
    invoke-static {v2}, Lcom/android/camera/animation/FolmeUtils;->touchItemScale(Landroid/view/View;)V

    iget-object v1, v1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-virtual {v1, v5}, Landroid/view/View;->setEnabled(Z)V

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    invoke-virtual {v2, v1}, Landroid/view/View;->setTag(Ljava/lang/Object;)V

    const/4 v1, 0x0

    invoke-virtual {v4, v13, v1}, Lcom/android/camera/ui/ColorCircleBackgroundView;->setSwitchOn(ZZ)V

    if-eqz v13, :cond_11

    if-nez v15, :cond_10

    invoke-virtual/range {p2 .. p2}, Lcom/airbnb/lottie/LottieAnimationView;->cancelAnimation()V

    move-object/from16 v6, p2

    move/from16 v9, v18

    invoke-virtual {v6, v9}, Lcom/airbnb/lottie/LottieAnimationView;->setImageResource(I)V

    const/high16 v1, -0x1000000

    goto :goto_19

    :cond_10
    move-object/from16 v6, p2

    goto :goto_1a

    :cond_11
    move-object/from16 v6, p2

    move/from16 v9, v18

    invoke-virtual {v6}, Lcom/airbnb/lottie/LottieAnimationView;->cancelAnimation()V

    invoke-virtual {v6, v9}, Lcom/airbnb/lottie/LottieAnimationView;->setImageResource(I)V

    :goto_19
    invoke-virtual {v6, v1}, Landroid/widget/ImageView;->setColorFilter(I)V

    :goto_1a
    if-eqz v15, :cond_12

    const/4 v1, 0x0

    invoke-virtual {v6, v1}, Lcom/airbnb/lottie/LottieAnimationView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    iget v0, v0, Lcom/android/camera/fragment/top/ExtraAdapter;->mAnimationScaleSize:F

    invoke-virtual {v6, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setScale(F)V

    invoke-virtual {v6, v14}, Lcom/airbnb/lottie/LottieAnimationView;->O0000O0o(I)V

    const/high16 v0, 0x3f800000    # 1.0f

    invoke-virtual {v6, v0}, Lcom/airbnb/lottie/LottieAnimationView;->setProgress(F)V

    invoke-virtual {v6}, Lcom/airbnb/lottie/LottieAnimationView;->O0000oO()V

    :cond_12
    :goto_1b
    return-void

    nop

    :sswitch_data_0
    .sparse-switch
        0xaa -> :sswitch_1f
        0xad -> :sswitch_1e
        0xae -> :sswitch_1d
        0xaf -> :sswitch_1c
        0xbb -> :sswitch_1b
        0xc7 -> :sswitch_1a
        0xcc -> :sswitch_19
        0xce -> :sswitch_18
        0xd0 -> :sswitch_17
        0xd1 -> :sswitch_16
        0xd2 -> :sswitch_15
        0xd5 -> :sswitch_14
        0xdb -> :sswitch_13
        0xdc -> :sswitch_12
        0xdd -> :sswitch_11
        0xdf -> :sswitch_10
        0xe1 -> :sswitch_f
        0xe2 -> :sswitch_e
        0xe3 -> :sswitch_d
        0xe4 -> :sswitch_c
        0xe5 -> :sswitch_b
        0xe9 -> :sswitch_a
        0xed -> :sswitch_9
        0xf2 -> :sswitch_8
        0xfb -> :sswitch_7
        0xfc -> :sswitch_6
        0xfd -> :sswitch_5
        0xff -> :sswitch_4
        0x102 -> :sswitch_3
        0x104 -> :sswitch_2
        0x105 -> :sswitch_1
        0x106 -> :sswitch_0
    .end sparse-switch
.end method

.method public onClick(Landroid/view/View;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mOnClickListener:Landroid/view/View$OnClickListener;

    invoke-interface {p0, p1}, Landroid/view/View$OnClickListener;->onClick(Landroid/view/View;)V

    return-void
.end method

.method public bridge synthetic onCreateViewHolder(Landroid/view/ViewGroup;I)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
    .locals 0

    invoke-virtual {p0, p1, p2}, Lcom/android/camera/fragment/top/ExtraAdapter;->onCreateViewHolder(Landroid/view/ViewGroup;I)Lcom/android/camera/fragment/CommonRecyclerViewHolder;

    move-result-object p0

    return-object p0
.end method

.method public onCreateViewHolder(Landroid/view/ViewGroup;I)Lcom/android/camera/fragment/CommonRecyclerViewHolder;
    .locals 1

    const/4 p0, 0x0

    const/4 v0, 0x1

    if-ne p2, v0, :cond_0

    invoke-virtual {p1}, Landroid/view/ViewGroup;->getContext()Landroid/content/Context;

    move-result-object p2

    invoke-static {p2}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;

    move-result-object p2

    const v0, 0x7f0c009f

    :goto_0
    invoke-virtual {p2, v0, p1, p0}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object p0

    goto :goto_1

    :cond_0
    const/4 v0, 0x2

    if-ne p2, v0, :cond_1

    invoke-virtual {p1}, Landroid/view/ViewGroup;->getContext()Landroid/content/Context;

    move-result-object p2

    invoke-static {p2}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;

    move-result-object p2

    const v0, 0x7f0c00a0

    goto :goto_0

    :cond_1
    const/4 p0, 0x0

    :goto_1
    new-instance p1, Lcom/android/camera/fragment/CommonRecyclerViewHolder;

    invoke-direct {p1, p0}, Lcom/android/camera/fragment/CommonRecyclerViewHolder;-><init>(Landroid/view/View;)V

    return-object p1
.end method

.method public bridge synthetic onViewAttachedToWindow(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)V
    .locals 0

    check-cast p1, Lcom/android/camera/fragment/CommonRecyclerViewHolder;

    invoke-virtual {p0, p1}, Lcom/android/camera/fragment/top/ExtraAdapter;->onViewAttachedToWindow(Lcom/android/camera/fragment/CommonRecyclerViewHolder;)V

    return-void
.end method

.method public onViewAttachedToWindow(Lcom/android/camera/fragment/CommonRecyclerViewHolder;)V
    .locals 4

    invoke-super {p0, p1}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->onViewAttachedToWindow(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)V

    invoke-direct {p0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getTotalRow()I

    move-result v0

    iget v1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mCurrentRow:I

    if-ne v1, v0, :cond_0

    return-void

    :cond_0
    iget-object v1, p1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    const/4 v2, 0x0

    invoke-static {v1, v2}, Landroidx/core/view/ViewCompat;->setAlpha(Landroid/view/View;F)V

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getAdapterPosition()I

    move-result v1

    invoke-direct {p0, v1}, Lcom/android/camera/fragment/top/ExtraAdapter;->getRow(I)I

    move-result v1

    iget v2, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mCurrentRow:I

    if-le v1, v2, :cond_2

    const/4 v3, -0x1

    if-le v2, v3, :cond_1

    iget-object v3, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mRowLists:Ljava/util/List;

    invoke-direct {p0, v3, v2, v0}, Lcom/android/camera/fragment/top/ExtraAdapter;->setAnimation(Ljava/util/List;II)V

    :cond_1
    iget-object v2, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mRowLists:Ljava/util/List;

    invoke-interface {v2}, Ljava/util/List;->clear()V

    iget-object v2, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mRowLists:Ljava/util/List;

    iget-object v3, p1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-interface {v2, v3}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    iput v1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mCurrentRow:I

    goto :goto_0

    :cond_2
    if-ne v1, v2, :cond_3

    iget-object v1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mRowLists:Ljava/util/List;

    iget-object v2, p1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-interface {v1, v2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_3
    :goto_0
    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getAdapterPosition()I

    move-result p1

    invoke-virtual {p0}, Lcom/android/camera/fragment/top/ExtraAdapter;->getItemCount()I

    move-result v1

    add-int/lit8 v1, v1, -0x1

    if-ne p1, v1, :cond_4

    iget-object p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mRowLists:Ljava/util/List;

    iget v1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mCurrentRow:I

    invoke-direct {p0, p1, v1, v0}, Lcom/android/camera/fragment/top/ExtraAdapter;->setAnimation(Ljava/util/List;II)V

    iget p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mCurrentRow:I

    add-int/lit8 p1, p1, 0x1

    iput p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mCurrentRow:I

    :cond_4
    return-void
.end method

.method public setAnimateHeight(I)V
    .locals 0

    iput p1, p0, Lcom/android/camera/fragment/top/ExtraAdapter;->mAnimateHeight:I

    return-void
.end method

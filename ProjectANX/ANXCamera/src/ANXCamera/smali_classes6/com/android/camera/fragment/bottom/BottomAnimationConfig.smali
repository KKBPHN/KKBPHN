.class public Lcom/android/camera/fragment/bottom/BottomAnimationConfig;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public mCurrentMode:I

.field public mDuration:I

.field public mIsFPS960:Z

.field public mIsInMimojiCreate:Z

.field public mIsLongExpose:Z

.field public mIsPostProcessing:Z

.field public mIsRecordingCircle:Z

.field public mIsRoundingCircle:Z

.field public mIsStart:Z

.field public mIsTimerBurstCircle:Z

.field public mIsVertical:Z

.field public mIsVideoBokeh:Z

.field public mNeedFinishRecord:Z

.field public mSecondPaintHintEnable:Z

.field public mShouldRepeat:Z

.field public mStopButtonEnabled:Z


# direct methods
.method private constructor <init>(ZIZZZ)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mSecondPaintHintEnable:Z

    iput-boolean p1, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsPostProcessing:Z

    iput p2, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    iput-boolean p3, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsStart:Z

    iput-boolean p4, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsFPS960:Z

    iput-boolean p5, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsVideoBokeh:Z

    invoke-direct {p0}, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->isLongExpose()Z

    move-result p1

    iput-boolean p1, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsLongExpose:Z

    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mStopButtonEnabled:Z

    return-void
.end method

.method public static generate(ZIZZZ)Lcom/android/camera/fragment/bottom/BottomAnimationConfig;
    .locals 7

    new-instance v6, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;

    move-object v0, v6

    move v1, p0

    move v2, p1

    move v3, p2

    move v4, p3

    move v5, p4

    invoke-direct/range {v0 .. v5}, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;-><init>(ZIZZZ)V

    return-object v6
.end method

.method private isLongExpose()Z
    .locals 2

    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    const/16 v1, 0xa7

    if-eq v0, v1, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemConfig()Lcom/android/camera/data/data/config/DataItemConfig;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/config/DataItemConfig;->getmComponentManuallyET()Lcom/android/camera/data/data/config/ComponentManuallyET;

    move-result-object v0

    iget p0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    invoke-virtual {v0, p0}, Lcom/android/camera/data/data/config/ComponentManuallyET;->isLongExpose(I)Z

    move-result p0

    return p0
.end method


# virtual methods
.method public configVariables()Lcom/android/camera/fragment/bottom/BottomAnimationConfig;
    .locals 14

    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsFPS960:Z

    const/16 v1, 0xd4

    const/16 v2, 0xb1

    const/16 v3, 0xb8

    const/16 v4, 0xa1

    const/16 v5, 0xa3

    const/16 v6, 0xa2

    const/16 v7, 0x7d0

    const/16 v8, 0xad

    const/4 v9, 0x1

    if-eqz v0, :cond_1

    :cond_0
    :goto_0
    iput v7, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mDuration:I

    goto/16 :goto_2

    :cond_1
    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    if-ne v0, v8, :cond_2

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/DataItemRunning;->isSuperNightCaptureWithKnownDuration()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsStart:Z

    xor-int/2addr v0, v9

    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mSecondPaintHintEnable:Z

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/DataItemRunning;->getMultiFrameTotalCaptureDuration()I

    move-result v0

    goto :goto_1

    :cond_2
    if-ne v0, v6, :cond_3

    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsVideoBokeh:Z

    if-eqz v0, :cond_3

    const/16 v0, 0x7530

    :goto_1
    iput v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mDuration:I

    goto :goto_2

    :cond_3
    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    const/16 v7, 0x3a98

    if-ne v0, v4, :cond_4

    iput v7, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mDuration:I

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v7, 0xc9

    invoke-virtual {v0, v7}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$LiveSpeedChanges;

    if-eqz v0, :cond_a

    iget v7, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mDuration:I

    int-to-float v7, v7

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$LiveSpeedChanges;->getRecordSpeed()F

    move-result v0

    div-float/2addr v7, v0

    float-to-int v0, v7

    goto :goto_1

    :cond_4
    if-ne v0, v3, :cond_5

    invoke-static {}, Lcom/android/camera/CameraSettings;->isGifOn()Z

    move-result v0

    if-eqz v0, :cond_0

    const/16 v0, 0x1388

    goto :goto_1

    :cond_5
    const/16 v10, 0xae

    if-ne v0, v10, :cond_6

    const/16 v0, 0x3c28

    goto :goto_1

    :cond_6
    const/16 v10, 0xb7

    if-ne v0, v10, :cond_7

    goto :goto_0

    :cond_7
    if-eq v0, v5, :cond_0

    if-ne v0, v2, :cond_8

    goto :goto_0

    :cond_8
    const/16 v7, 0xa7

    if-ne v0, v7, :cond_9

    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsLongExpose:Z

    if-eqz v0, :cond_9

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemConfig()Lcom/android/camera/data/data/config/DataItemConfig;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/config/DataItemConfig;->getmComponentManuallyET()Lcom/android/camera/data/data/config/ComponentManuallyET;

    move-result-object v0

    iget v7, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    invoke-virtual {v0, v7}, Lcom/android/camera/data/data/config/ComponentManuallyET;->getComponentValue(I)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Long;->parseLong(Ljava/lang/String;)J

    move-result-wide v10

    const-wide/32 v12, 0xf4240

    div-long/2addr v10, v12

    long-to-int v0, v10

    goto :goto_1

    :cond_9
    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    const/16 v7, 0x2710

    goto/16 :goto_0

    :cond_a
    :goto_2
    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    const/4 v7, 0x0

    if-eq v0, v5, :cond_c

    if-eq v0, v4, :cond_c

    if-eq v0, v2, :cond_c

    if-eq v0, v3, :cond_c

    if-ne v0, v6, :cond_b

    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsVideoBokeh:Z

    if-nez v0, :cond_c

    :cond_b
    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    if-eq v0, v8, :cond_c

    iget-boolean v2, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsFPS960:Z

    if-nez v2, :cond_c

    const/16 v2, 0xbd

    if-eq v0, v2, :cond_c

    if-eq v0, v1, :cond_c

    move v0, v9

    goto :goto_3

    :cond_c
    move v0, v7

    :goto_3
    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mShouldRepeat:Z

    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    if-ne v0, v8, :cond_d

    move v0, v9

    goto :goto_4

    :cond_d
    move v0, v7

    :goto_4
    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsRecordingCircle:Z

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemLive()Lcom/android/camera/data/data/extra/DataItemLive;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/extra/DataItemLive;->getTimerBurstController()Lcom/android/camera/timerburst/TimerBurstController;

    move-result-object v0

    invoke-static {}, Lcom/android/camera/CameraSettings;->isTimerBurstEnable()Z

    move-result v1

    if-eqz v1, :cond_e

    iget v1, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    invoke-static {v1}, Lcom/android/camera/timerburst/TimerBurstController;->isSupportTimerBurst(I)Z

    move-result v1

    if-eqz v1, :cond_e

    invoke-virtual {v0}, Lcom/android/camera/timerburst/TimerBurstController;->isInTimerBurstShotting()Z

    move-result v0

    if-eqz v0, :cond_e

    move v0, v9

    goto :goto_5

    :cond_e
    move v0, v7

    :goto_5
    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsTimerBurstCircle:Z

    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsTimerBurstCircle:Z

    if-nez v0, :cond_f

    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsLongExpose:Z

    if-eqz v0, :cond_10

    :cond_f
    iput-boolean v7, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mShouldRepeat:Z

    :cond_10
    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    if-eq v0, v8, :cond_11

    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsFPS960:Z

    if-eqz v0, :cond_12

    :cond_11
    iget-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsPostProcessing:Z

    if-nez v0, :cond_12

    move v0, v9

    goto :goto_6

    :cond_12
    move v0, v7

    :goto_6
    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mNeedFinishRecord:Z

    iput-boolean v9, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mStopButtonEnabled:Z

    iget v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mCurrentMode:I

    const/16 v1, 0xbb

    if-ne v0, v1, :cond_13

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x1a4

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$AmbilightProtocol;

    if-eqz v0, :cond_14

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$AmbilightProtocol;->getDuration()I

    move-result v1

    iput v1, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mDuration:I

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$AmbilightProtocol;->shouldDisableStopButton()Z

    move-result v1

    xor-int/2addr v1, v9

    iput-boolean v1, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mStopButtonEnabled:Z

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$AmbilightProtocol;->getAutoFinish()Z

    move-result v1

    xor-int/2addr v1, v9

    iput-boolean v1, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mShouldRepeat:Z

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$AmbilightProtocol;->getAutoFinish()Z

    move-result v0

    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsRecordingCircle:Z

    goto :goto_7

    :cond_13
    const/16 v1, 0xd0

    if-ne v0, v1, :cond_14

    iput-boolean v7, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mStopButtonEnabled:Z

    :cond_14
    :goto_7
    const v0, 0x3faaaaaa

    invoke-static {v0}, Lcom/android/camera/Display;->fitDisplayFull(F)Z

    move-result v0

    iput-boolean v0, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mIsVertical:Z

    return-object p0
.end method

.method public setSpecifiedDuration(I)V
    .locals 0

    iput p1, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mDuration:I

    const/4 p1, 0x0

    iput-boolean p1, p0, Lcom/android/camera/fragment/bottom/BottomAnimationConfig;->mShouldRepeat:Z

    return-void
.end method

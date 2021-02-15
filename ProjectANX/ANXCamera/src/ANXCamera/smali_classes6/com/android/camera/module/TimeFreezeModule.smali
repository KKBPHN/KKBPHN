.class public Lcom/android/camera/module/TimeFreezeModule;
.super Lcom/android/camera/module/CloneModule;
.source ""


# static fields
.field public static final DURATION_VIDEO_RECORDING:I = 0x3a98

.field private static final MAX_VIDEO_SUBJECT_COUNT:I = 0x1

.field private static final TAG:Ljava/lang/String; = "TimeFreezeModule"


# instance fields
.field private mDetectedPerson:Z

.field private mInFreezing:Z

.field private mInPausing:Z

.field private mInPlaying:Z

.field private mInSaving:Z

.field private mTimeFreezeCountDownTimer:Landroid/os/CountDownTimer;


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lcom/android/camera/module/CloneModule;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPlaying:Z

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPausing:Z

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInSaving:Z

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mDetectedPerson:Z

    return-void
.end method

.method static synthetic access$000(Lcom/android/camera/module/TimeFreezeModule;)V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/module/TimeFreezeModule;->resetTipHint()V

    return-void
.end method

.method private cancelTimeFreezeCountDown()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mTimeFreezeCountDownTimer:Landroid/os/CountDownTimer;

    if-eqz v0, :cond_0

    sget-object v0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string v1, "cancelTimeFreezeCountDown"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mTimeFreezeCountDownTimer:Landroid/os/CountDownTimer;

    invoke-virtual {v0}, Landroid/os/CountDownTimer;->cancel()V

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mTimeFreezeCountDownTimer:Landroid/os/CountDownTimer;

    :cond_0
    iget-boolean v0, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    if-nez v0, :cond_1

    iget-object v0, p0, Lcom/android/camera/module/BaseModule;->mHandler:Landroid/os/Handler;

    new-instance v1, Lcom/android/camera/module/TimeFreezeModule$3;

    invoke-direct {v1, p0}, Lcom/android/camera/module/TimeFreezeModule$3;-><init>(Lcom/android/camera/module/TimeFreezeModule;)V

    invoke-virtual {v0, v1}, Landroid/os/Handler;->post(Ljava/lang/Runnable;)Z

    :cond_1
    return-void
.end method

.method private resetTipHint()V
    .locals 2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p0

    const/16 v0, 0x1a2

    invoke-virtual {p0, v0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p0

    check-cast p0, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    if-eqz p0, :cond_1

    invoke-interface {p0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->getStatus()Lcom/android/camera/fragment/clone/Status;

    move-result-object v0

    sget-object v1, Lcom/android/camera/fragment/clone/Status;->CAPTURING:Lcom/android/camera/fragment/clone/Status;

    if-eq v0, v1, :cond_0

    sget-object p0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string v0, "onPictureTakenImageConsumed not capturing"

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->w(Ljava/lang/String;Ljava/lang/String;)I

    return-void

    :cond_0
    const/4 v0, -0x1

    const/4 v1, 0x0

    invoke-interface {p0, v0, v1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->updateCaptureMessage(IZ)V

    :cond_1
    return-void
.end method

.method private startFenShenCam()V
    .locals 2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p0

    const/16 v0, 0x1a2

    invoke-virtual {p0, v0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p0

    check-cast p0, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    if-eqz p0, :cond_0

    const v0, 0x7f100206

    const/4 v1, 0x0

    invoke-interface {p0, v0, v1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->updateCaptureMessage(IZ)V

    invoke-interface {p0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->getStatus()Lcom/android/camera/fragment/clone/Status;

    move-result-object p0

    sget-object v0, Lcom/android/camera/fragment/clone/Status;->SHARE:Lcom/android/camera/fragment/clone/Status;

    if-ne p0, v0, :cond_0

    return-void

    :cond_0
    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->start()V

    return-void
.end method

.method private startTimeFreezeCountDown()V
    .locals 7

    iget-object v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mTimeFreezeCountDownTimer:Landroid/os/CountDownTimer;

    if-nez v0, :cond_0

    new-instance v0, Lcom/android/camera/module/TimeFreezeModule$2;

    const-wide/16 v3, 0x154a

    const-wide/16 v5, 0x3e8

    move-object v1, v0

    move-object v2, p0

    invoke-direct/range {v1 .. v6}, Lcom/android/camera/module/TimeFreezeModule$2;-><init>(Lcom/android/camera/module/TimeFreezeModule;JJ)V

    iput-object v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mTimeFreezeCountDownTimer:Landroid/os/CountDownTimer;

    :cond_0
    iget-object v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mTimeFreezeCountDownTimer:Landroid/os/CountDownTimer;

    invoke-virtual {v0}, Landroid/os/CountDownTimer;->cancel()V

    iget-object p0, p0, Lcom/android/camera/module/TimeFreezeModule;->mTimeFreezeCountDownTimer:Landroid/os/CountDownTimer;

    invoke-virtual {p0}, Landroid/os/CountDownTimer;->start()Landroid/os/CountDownTimer;

    return-void
.end method

.method private switchTimeFreeze(Lcom/android/camera/protocol/ModeProtocol$CloneProcess;)V
    .locals 2

    iget-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInFreezing:Z

    const/4 v1, 0x0

    if-eqz v0, :cond_1

    iput-boolean v1, p0, Lcom/android/camera/module/TimeFreezeModule;->mInFreezing:Z

    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->stopTimeFreeze()V

    iget-boolean p1, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    if-nez p1, :cond_0

    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->cancelVideo()V

    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->start()V

    iget-boolean p1, p0, Lcom/android/camera/module/TimeFreezeModule;->mDetectedPerson:Z

    if-nez p1, :cond_0

    sget-object p1, Lcom/xiaomi/fenshen/FenShenCam$Message;->NO_PERSON:Lcom/xiaomi/fenshen/FenShenCam$Message;

    invoke-virtual {p0, p1}, Lcom/android/camera/module/CloneModule;->onCloneMessage(Lcom/xiaomi/fenshen/FenShenCam$Message;)V

    :cond_0
    invoke-direct {p0}, Lcom/android/camera/module/TimeFreezeModule;->cancelTimeFreezeCountDown()V

    goto :goto_0

    :cond_1
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInFreezing:Z

    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->startTimeFreeze()V

    iget-boolean v0, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    if-nez v0, :cond_2

    invoke-direct {p0}, Lcom/android/camera/module/TimeFreezeModule;->startTimeFreezeCountDown()V

    if-eqz p1, :cond_2

    const p0, 0x7f1002d6

    invoke-interface {p1, p0, v1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->updateCaptureMessage(IZ)V

    :cond_2
    :goto_0
    return-void
.end method


# virtual methods
.method protected cancelPhotoOrVideo()V
    .locals 0

    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->cancelVideo()V

    return-void
.end method

.method public dispatchConfigChange(I)Z
    .locals 1

    const/16 p0, 0xfb

    if-ne p1, p0, :cond_0

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemLive()Lcom/android/camera/data/data/extra/DataItemLive;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/data/data/extra/DataItemLive;->getTimeFreezeFilmRatioEnabled()Z

    move-result p0

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemLive()Lcom/android/camera/data/data/extra/DataItemLive;

    move-result-object p1

    xor-int/lit8 v0, p0, 0x1

    invoke-virtual {p1, v0}, Lcom/android/camera/data/data/extra/DataItemLive;->setTimeFreezeFilmRatioEnabled(Z)V

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p1

    const/16 v0, 0x1a2

    invoke-virtual {p1, v0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p1

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    if-eqz p1, :cond_0

    const/4 v0, 0x1

    xor-int/2addr p0, v0

    invoke-interface {p1, p0, v0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->onFilmRatioChanged(ZZ)V

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public fillFeatureControl(Lcom/android/camera/module/loader/StartControl;)V
    .locals 1

    invoke-virtual {p1}, Lcom/android/camera/module/loader/StartControl;->getFeatureDetail()Lcom/android/camera/module/loader/StartControlFeatureDetail;

    move-result-object p0

    const p1, 0x7f09016d

    const v0, 0xfffff9

    invoke-virtual {p0, p1, v0}, Lcom/android/camera/module/loader/StartControlFeatureDetail;->addFragmentInfo(II)V

    const p1, 0x7f090079

    invoke-virtual {p0, p1}, Lcom/android/camera/module/loader/StartControlFeatureDetail;->hideFragment(I)V

    const p1, 0x7f090085

    invoke-virtual {p0, p1}, Lcom/android/camera/module/loader/StartControlFeatureDetail;->hideFragment(I)V

    const p1, 0x7f090083

    invoke-virtual {p0, p1}, Lcom/android/camera/module/loader/StartControlFeatureDetail;->hideFragment(I)V

    return-void
.end method

.method protected getDurationVideoRecording()I
    .locals 0

    const/16 p0, 0x3a98

    return p0
.end method

.method protected getMaxVideoSubjectCount()I
    .locals 0

    const/4 p0, 0x1

    return p0
.end method

.method protected getOperatingMode()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public getTag()Ljava/lang/String;
    .locals 0

    sget-object p0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    return-object p0
.end method

.method protected initCloneMode(Lcom/xiaomi/fenshen/FenShenCam$Mode;)V
    .locals 1

    sget-object v0, Lcom/xiaomi/fenshen/FenShenCam$Mode;->TIMEFREEZE:Lcom/xiaomi/fenshen/FenShenCam$Mode;

    if-ne p1, v0, :cond_0

    invoke-direct {p0}, Lcom/android/camera/module/TimeFreezeModule;->startFenShenCam()V

    const/4 p1, 0x0

    iput-boolean p1, p0, Lcom/android/camera/module/CloneModule;->mPendingStart:Z

    :cond_0
    return-void
.end method

.method protected isVideoMode()Z
    .locals 1

    iget-object p0, p0, Lcom/android/camera/module/CloneModule;->mMode:Lcom/xiaomi/fenshen/FenShenCam$Mode;

    sget-object v0, Lcom/xiaomi/fenshen/FenShenCam$Mode;->TIMEFREEZE:Lcom/xiaomi/fenshen/FenShenCam$Mode;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method protected onCloneMessage(Lcom/xiaomi/fenshen/FenShenCam$Mode;Lcom/xiaomi/fenshen/FenShenCam$Message;Lcom/android/camera/protocol/ModeProtocol$CloneProcess;I)Z
    .locals 4

    invoke-interface {p3}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->getStatus()Lcom/android/camera/fragment/clone/Status;

    move-result-object p1

    sget-object v0, Lcom/android/camera/fragment/clone/Status;->SHARE:Lcom/android/camera/fragment/clone/Status;

    const/4 v1, 0x0

    if-ne p1, v0, :cond_0

    return v1

    :cond_0
    iget-object p1, p0, Lcom/android/camera/module/BaseModule;->mMainProtocol:Lcom/android/camera/protocol/ModeProtocol$MainContentProtocol;

    const/4 v0, 0x7

    invoke-interface {p1, v0}, Lcom/android/camera/protocol/ModeProtocol$IndicatorProtocol;->clearFocusView(I)V

    iput-boolean v1, p0, Lcom/android/camera/module/TimeFreezeModule;->mDetectedPerson:Z

    sget-object p1, Lcom/xiaomi/fenshen/FenShenCam$Message;->ALIGN_TOO_LARGE_OR_FAILED:Lcom/xiaomi/fenshen/FenShenCam$Message;

    const/4 v0, 0x1

    if-ne p2, p1, :cond_3

    sget-object p1, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string p2, "too much movement, stop capture"

    invoke-static {p1, p2}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-boolean p1, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    if-eqz p1, :cond_1

    const p1, 0x7f10020c

    invoke-interface {p3, p1, v1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->updateCaptureMessage(IZ)V

    const/16 p1, 0xa

    invoke-virtual {p0, p1, v0}, Lcom/android/camera/module/CloneModule;->onShutterButtonClick(IZ)V

    goto :goto_0

    :cond_1
    iget-boolean p0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInFreezing:Z

    if-eqz p0, :cond_2

    invoke-interface {p3}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->onCountDownFinished()V

    :cond_2
    :goto_0
    return v1

    :cond_3
    sget-object p1, Lcom/xiaomi/fenshen/FenShenCam$Message;->PREVIEW_NO_PERSON:Lcom/xiaomi/fenshen/FenShenCam$Message;

    if-eq p2, p1, :cond_6

    sget-object p1, Lcom/xiaomi/fenshen/FenShenCam$Message;->NO_PERSON:Lcom/xiaomi/fenshen/FenShenCam$Message;

    if-ne p2, p1, :cond_4

    goto :goto_1

    :cond_4
    sget-object p1, Lcom/xiaomi/fenshen/FenShenCam$Message;->START:Lcom/xiaomi/fenshen/FenShenCam$Message;

    if-eq p2, p1, :cond_5

    sget-object p1, Lcom/xiaomi/fenshen/FenShenCam$Message;->PREVIEW_PERSON:Lcom/xiaomi/fenshen/FenShenCam$Message;

    if-ne p2, p1, :cond_7

    :cond_5
    invoke-interface {p3, v0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->setDetectedPersonInPreview(Z)V

    const/4 p1, -0x1

    invoke-interface {p3, p1, v1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->updateCaptureMessage(IZ)V

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mDetectedPerson:Z

    return v1

    :cond_6
    :goto_1
    iget-boolean p1, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    if-nez p1, :cond_7

    invoke-interface {p3, v1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->setDetectedPersonInPreview(Z)V

    :cond_7
    sget-object p1, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "updateCaptureMessage "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/Enum;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {p1, v2}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-boolean p1, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    if-eqz p1, :cond_8

    sget-object p1, Lcom/xiaomi/fenshen/FenShenCam$Message;->NO_PERSON:Lcom/xiaomi/fenshen/FenShenCam$Message;

    if-ne p2, p1, :cond_8

    return v0

    :cond_8
    iget-boolean p1, p0, Lcom/android/camera/module/TimeFreezeModule;->mInFreezing:Z

    if-nez p1, :cond_a

    iget-boolean p0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPlaying:Z

    if-eqz p0, :cond_9

    goto :goto_2

    :cond_9
    invoke-interface {p3, p4, v1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->updateCaptureMessage(IZ)V

    :cond_a
    :goto_2
    return v0
.end method

.method protected onCreate(Lcom/xiaomi/fenshen/FenShenCam$Mode;)V
    .locals 0

    const/4 p1, 0x0

    iput-boolean p1, p0, Lcom/android/camera/module/TimeFreezeModule;->mInFreezing:Z

    return-void
.end method

.method protected onError()V
    .locals 2

    sget-object v0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string v1, "onError"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, p0, Lcom/android/camera/module/BaseModule;->mHandler:Landroid/os/Handler;

    new-instance v1, Lcom/android/camera/module/O000OoO;

    invoke-direct {v1, p0}, Lcom/android/camera/module/O000OoO;-><init>(Lcom/android/camera/module/TimeFreezeModule;)V

    invoke-virtual {v0, v1}, Landroid/os/Handler;->post(Ljava/lang/Runnable;)Z

    iget-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPlaying:Z

    if-eqz v0, :cond_0

    iget-object v0, p0, Lcom/android/camera/module/BaseModule;->mHandler:Landroid/os/Handler;

    new-instance v1, Lcom/android/camera/module/O000oO;

    invoke-direct {v1, p0}, Lcom/android/camera/module/O000oO;-><init>(Lcom/android/camera/module/TimeFreezeModule;)V

    invoke-virtual {v0, v1}, Landroid/os/Handler;->post(Ljava/lang/Runnable;)Z

    :cond_0
    return-void
.end method

.method public onExitClicked()V
    .locals 0

    invoke-super {p0}, Lcom/android/camera/module/CloneModule;->onExitClicked()V

    const-string/jumbo p0, "value_time_freeze_exit_confirm"

    invoke-static {p0}, Lcom/android/camera/statistic/CameraStatUtils;->trackFilmTimeFreezeClick(Ljava/lang/String;)V

    return-void
.end method

.method public onHostStopAndNotifyActionStop(Lcom/xiaomi/fenshen/FenShenCam$Mode;)V
    .locals 0

    iget-boolean p1, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    if-eqz p1, :cond_0

    const/4 p1, 0x0

    iput-boolean p1, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    const/4 p1, 0x1

    invoke-virtual {p0, p1}, Lcom/android/camera/module/CloneModule;->stopVideoRecording(Z)V

    :cond_0
    return-void
.end method

.method public onPause()V
    .locals 2

    iget-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInFreezing:Z

    if-eqz v0, :cond_0

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x1a2

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    invoke-direct {p0, v0}, Lcom/android/camera/module/TimeFreezeModule;->switchTimeFreeze(Lcom/android/camera/protocol/ModeProtocol$CloneProcess;)V

    :cond_0
    invoke-super {p0}, Lcom/android/camera/module/CloneModule;->onPause()V

    return-void
.end method

.method public onPlayClicked()V
    .locals 2

    sget-object v0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string v1, "onPlayClicked"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPausing:Z

    if-eqz v0, :cond_0

    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->resumePlayEffect()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPausing:Z

    :cond_0
    return-void
.end method

.method public onResume()V
    .locals 1

    invoke-super {p0}, Lcom/android/camera/module/CloneModule;->onResume()V

    iget-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mDetectedPerson:Z

    if-nez v0, :cond_0

    sget-object v0, Lcom/xiaomi/fenshen/FenShenCam$Message;->NO_PERSON:Lcom/xiaomi/fenshen/FenShenCam$Message;

    invoke-virtual {p0, v0}, Lcom/android/camera/module/CloneModule;->onCloneMessage(Lcom/xiaomi/fenshen/FenShenCam$Message;)V

    :cond_0
    return-void
.end method

.method public onReviewDoneClicked()V
    .locals 3

    const/4 v0, 0x0

    invoke-static {v0}, Lcom/android/camera/fragment/clone/Config;->setCloneMode(Lcom/xiaomi/fenshen/FenShenCam$Mode;)V

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v1

    const/16 v2, 0x1a2

    invoke-virtual {v1, v2}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v1

    check-cast v1, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    if-eqz v1, :cond_0

    invoke-interface {v1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->quit()V

    :cond_0
    iget-object p0, p0, Lcom/android/camera/module/BaseModule;->mTopAlert:Lcom/android/camera/protocol/ModeProtocol$TopAlert;

    if-eqz p0, :cond_1

    invoke-interface {p0}, Lcom/android/camera/protocol/ModeProtocol$TopAlert;->showConfigMenu()V

    :cond_1
    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p0

    const/16 v1, 0xa4

    invoke-virtual {p0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p0

    check-cast p0, Lcom/android/camera/protocol/ModeProtocol$ConfigChanges;

    if-eqz p0, :cond_2

    const/4 v1, 0x0

    const/4 v2, 0x1

    invoke-interface {p0, v0, v1, v2}, Lcom/android/camera/protocol/ModeProtocol$ConfigChanges;->configFilm(Lcom/android/camera/fragment/film/FilmItem;ZZ)V

    :cond_2
    return-void
.end method

.method protected onShutterButtonClick(Lcom/xiaomi/fenshen/FenShenCam$Mode;)V
    .locals 4

    iget-boolean p1, p0, Lcom/android/camera/module/TimeFreezeModule;->mInSaving:Z

    if-eqz p1, :cond_0

    return-void

    :cond_0
    iget-boolean p1, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    const/4 v0, 0x0

    if-nez p1, :cond_2

    iget-object p1, p0, Lcom/android/camera/module/BaseModule;->mTopAlert:Lcom/android/camera/protocol/ModeProtocol$TopAlert;

    if-eqz p1, :cond_1

    const/4 v1, 0x1

    new-array v2, v1, [I

    const/16 v3, 0xfb

    aput v3, v2, v0

    invoke-interface {p1, v1, v2}, Lcom/android/camera/protocol/ModeProtocol$TopAlert;->disableMenuItem(Z[I)V

    :cond_1
    invoke-virtual {p0}, Lcom/android/camera/module/CloneModule;->delayTriggerShooting()V

    goto :goto_0

    :cond_2
    iget p1, p0, Lcom/android/camera/module/CloneModule;->mFrameCount:I

    const/16 v1, 0xf

    if-ge p1, v1, :cond_3

    sget-object p0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string p1, "ignore onShutterButtonClick cause frameCount < 15"

    invoke-static {p0, p1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void

    :cond_3
    iput-boolean v0, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    invoke-virtual {p0, v0}, Lcom/android/camera/module/CloneModule;->stopVideoRecording(Z)V

    invoke-virtual {p0}, Lcom/android/camera/module/CloneModule;->stopCaptureToPreviewResult()V

    :goto_0
    return-void
.end method

.method public onSingleTapUp(IIZ)V
    .locals 0

    return-void
.end method

.method public onStop()V
    .locals 0

    invoke-super {p0}, Lcom/android/camera/module/CloneModule;->onStop()V

    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->cancelVideo()V

    return-void
.end method

.method public onTimeFreezeClicked()V
    .locals 3

    sget-object v0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string v1, "onTimeFreezeClicked"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x1a2

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    if-eqz v0, :cond_0

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->getStatus()Lcom/android/camera/fragment/clone/Status;

    move-result-object v1

    sget-object v2, Lcom/android/camera/fragment/clone/Status;->CAPTURING:Lcom/android/camera/fragment/clone/Status;

    if-eq v1, v2, :cond_0

    sget-object p0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string v0, "onPictureTakenImageConsumed not capturing"

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->w(Ljava/lang/String;Ljava/lang/String;)I

    return-void

    :cond_0
    invoke-direct {p0, v0}, Lcom/android/camera/module/TimeFreezeModule;->switchTimeFreeze(Lcom/android/camera/protocol/ModeProtocol$CloneProcess;)V

    return-void
.end method

.method public onTouchDown(FF)Z
    .locals 8

    iget-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPlaying:Z

    const/4 v1, 0x0

    if-nez v0, :cond_0

    sget-object p0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "onTouchDown  ("

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(F)Ljava/lang/StringBuilder;

    const-string v2, ","

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(F)Ljava/lang/StringBuilder;

    const-string v2, ")"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v2, Lcom/xiaomi/fenshen/FenShenCam$TEventType;->CLICK_DOWN:Lcom/xiaomi/fenshen/FenShenCam$TEventType;

    const/4 v3, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x0

    move v4, p1

    move v5, p2

    invoke-static/range {v2 .. v7}, Lcom/xiaomi/fenshen/FenShenCam;->sendTouchEvent(Lcom/xiaomi/fenshen/FenShenCam$TEventType;FFFFF)V

    goto :goto_0

    :cond_0
    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p1

    const/16 p2, 0x1a2

    invoke-virtual {p1, p2}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p1

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    iget-boolean p2, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPausing:Z

    if-eqz p2, :cond_1

    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->resumePlayEffect()V

    iput-boolean v1, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPausing:Z

    if-eqz p1, :cond_2

    invoke-interface {p1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->onReplayResume()V

    goto :goto_0

    :cond_1
    invoke-static {}, Lcom/xiaomi/fenshen/FenShenCam;->pausePlayEffect()V

    const/4 p2, 0x1

    iput-boolean p2, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPausing:Z

    if-eqz p1, :cond_2

    invoke-interface {p1}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->onReplayPause()V

    :cond_2
    :goto_0
    return v1
.end method

.method protected onVideoSaveFinish()V
    .locals 1

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInSaving:Z

    invoke-super {p0}, Lcom/android/camera/module/CloneModule;->onVideoSaveFinish()V

    iput-boolean v0, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    return-void
.end method

.method public playFocusSound(I)V
    .locals 0

    return-void
.end method

.method protected resumePreviewIfNeeded()V
    .locals 2

    sget-object v0, Lcom/android/camera/module/TimeFreezeModule;->TAG:Ljava/lang/String;

    const-string v1, "resumePreviewIfNeeded"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/module/CloneModule;->mIsFinished:Z

    invoke-virtual {p0}, Lcom/android/camera/module/CloneModule;->resumePreview()V

    invoke-virtual {p0}, Lcom/android/camera/module/TimeFreezeModule;->cancelPhotoOrVideo()V

    return-void
.end method

.method protected saveVideo()V
    .locals 1

    invoke-super {p0}, Lcom/android/camera/module/CloneModule;->saveVideo()V

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInSaving:Z

    return-void
.end method

.method public setFrameAvailable(Z)V
    .locals 3

    invoke-super {p0, p1}, Lcom/android/camera/module/CloneModule;->setFrameAvailable(Z)V

    if-eqz p1, :cond_0

    iget-object p1, p0, Lcom/android/camera/module/BaseModule;->mHandler:Landroid/os/Handler;

    new-instance v0, Lcom/android/camera/module/TimeFreezeModule$1;

    invoke-direct {v0, p0}, Lcom/android/camera/module/TimeFreezeModule$1;-><init>(Lcom/android/camera/module/TimeFreezeModule;)V

    const-wide/16 v1, 0x190

    invoke-virtual {p1, v0, v1, v2}, Landroid/os/Handler;->postDelayed(Ljava/lang/Runnable;J)Z

    invoke-direct {p0}, Lcom/android/camera/module/TimeFreezeModule;->startFenShenCam()V

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p0

    const/16 p1, 0x1a2

    invoke-virtual {p0, p1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p0

    check-cast p0, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    if-eqz p0, :cond_0

    invoke-interface {p0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->onFrameAvailable()V

    :cond_0
    return-void
.end method

.method protected setVideoCodec()V
    .locals 1

    invoke-static {}, Lcom/android/camera/CameraSettings;->getVideoEncoder()I

    move-result p0

    const/4 v0, 0x5

    if-ne p0, v0, :cond_0

    const-string/jumbo p0, "video/hevc"

    goto :goto_0

    :cond_0
    const-string/jumbo p0, "video/avc"

    :goto_0
    invoke-static {p0}, Lcom/xiaomi/fenshen/FenShenCam;->setVideoCodec(Ljava/lang/String;)V

    return-void
.end method

.method public shouldReleaseLater()Z
    .locals 1

    iget-boolean v0, p0, Lcom/android/camera/module/CloneModule;->mInRecording:Z

    if-nez v0, :cond_1

    iget-boolean p0, p0, Lcom/android/camera/module/CloneModule;->mIsFinished:Z

    if-eqz p0, :cond_0

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

.method protected startPreviewSession()V
    .locals 2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x1a2

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;

    if-eqz v0, :cond_0

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->getStatus()Lcom/android/camera/fragment/clone/Status;

    move-result-object v0

    sget-object v1, Lcom/android/camera/fragment/clone/Status;->SHARE:Lcom/android/camera/fragment/clone/Status;

    if-ne v0, v1, :cond_0

    return-void

    :cond_0
    invoke-super {p0}, Lcom/android/camera/module/CloneModule;->startPreviewSession()V

    return-void
.end method

.method protected startVideoRecording(Lcom/xiaomi/fenshen/FenShenCam$Mode;Lcom/android/camera/protocol/ModeProtocol$CloneProcess;)V
    .locals 1

    invoke-super {p0, p1, p2}, Lcom/android/camera/module/CloneModule;->startVideoRecording(Lcom/xiaomi/fenshen/FenShenCam$Mode;Lcom/android/camera/protocol/ModeProtocol$CloneProcess;)V

    invoke-direct {p0}, Lcom/android/camera/module/TimeFreezeModule;->cancelTimeFreezeCountDown()V

    if-eqz p2, :cond_0

    const p1, 0x7f1001f0

    const/4 v0, 0x0

    invoke-interface {p2, p1, v0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->updateCaptureMessage(IZ)V

    :cond_0
    iget-boolean p0, p0, Lcom/android/camera/module/TimeFreezeModule;->mInFreezing:Z

    invoke-static {p0}, Lcom/android/camera/statistic/CameraStatUtils;->trackFilmTimeFreezeRecord(Z)V

    return-void
.end method

.method protected stopCaptureToPreviewResult(Lcom/xiaomi/fenshen/FenShenCam$Mode;)V
    .locals 0

    const/4 p1, 0x3

    invoke-virtual {p0, p1}, Lcom/android/camera/module/BaseModule;->playCameraSound(I)V

    return-void
.end method

.method protected stopVideoRecording(ZLcom/xiaomi/fenshen/FenShenCam$Mode;Lcom/android/camera/protocol/ModeProtocol$CloneProcess;)V
    .locals 1

    invoke-super {p0, p1, p2, p3}, Lcom/android/camera/module/CloneModule;->stopVideoRecording(ZLcom/xiaomi/fenshen/FenShenCam$Mode;Lcom/android/camera/protocol/ModeProtocol$CloneProcess;)V

    if-eqz p3, :cond_0

    const/4 p2, -0x1

    const/4 v0, 0x0

    invoke-interface {p3, p2, v0}, Lcom/android/camera/protocol/ModeProtocol$CloneProcess;->updateCaptureMessage(IZ)V

    :cond_0
    if-nez p1, :cond_1

    const/4 p1, 0x1

    iput-boolean p1, p0, Lcom/android/camera/module/TimeFreezeModule;->mInPlaying:Z

    :cond_1
    return-void
.end method

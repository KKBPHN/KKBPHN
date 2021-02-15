.class public Lcom/android/camera/dualvideo/DualVideoSelectModule;
.super Lcom/android/camera/dualvideo/DualVideoModuleBase;
.source ""


# instance fields
.field private mSelectDataBak:Ljava/util/ArrayList;


# direct methods
.method public constructor <init>()V
    .locals 1

    const-class v0, Lcom/android/camera/dualvideo/DualVideoSelectModule;

    invoke-virtual {v0}, Ljava/lang/Class;->getSimpleName()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;-><init>(Ljava/lang/String;)V

    return-void
.end method

.method public static synthetic O000000o(Lcom/android/camera/dualvideo/DualVideoSelectModule;)V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->addBottomControl()V

    return-void
.end method

.method public static synthetic O000000o(Lcom/android/camera/dualvideo/DualVideoSelectModule;Landroid/view/View;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->onConfirmClicked(Landroid/view/View;)V

    return-void
.end method

.method static synthetic O0000OOo(Lcom/android/camera/dualvideo/render/RenderManager;)V
    .locals 1

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/render/RenderManager;->enableContinuousRender(Z)V

    return-void
.end method

.method private addBottomControl()V
    .locals 6

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x1af

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$ModuleContent;

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$ModuleContent;->getParent()Landroid/view/ViewGroup;

    move-result-object v0

    invoke-static {}, Lcom/android/camera/dualvideo/ModuleUtil;->isFatScreen()Z

    move-result v1

    const v2, 0x7f090103

    const/4 v3, 0x1

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Lcom/android/camera/module/BaseModule;->getActivity()Lcom/android/camera/Camera;

    move-result-object v1

    invoke-static {v1}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;

    move-result-object v1

    const v4, 0x7f0c0031

    invoke-virtual {v1, v4, v0, v3}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object v0

    invoke-virtual {v0, v2}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroid/widget/RelativeLayout;

    invoke-virtual {v1}, Landroid/view/ViewGroup;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v1

    check-cast v1, Landroid/view/ViewGroup$MarginLayoutParams;

    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v2

    int-to-float v2, v2

    invoke-static {}, Lcom/android/camera/Display;->getWindowHeight()I

    move-result v4

    int-to-float v4, v4

    const/high16 v5, 0x41800000    # 16.0f

    div-float/2addr v4, v5

    const/high16 v5, 0x41100000    # 9.0f

    mul-float/2addr v4, v5

    sub-float/2addr v2, v4

    const/high16 v4, 0x40000000    # 2.0f

    div-float/2addr v2, v4

    float-to-int v2, v2

    iput v2, v1, Landroid/view/ViewGroup$MarginLayoutParams;->width:I

    goto :goto_0

    :cond_0
    invoke-virtual {p0}, Lcom/android/camera/module/BaseModule;->getActivity()Lcom/android/camera/Camera;

    move-result-object v1

    invoke-static {v1}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;

    move-result-object v1

    const v4, 0x7f0c0030

    invoke-virtual {v1, v4, v0, v3}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object v0

    invoke-virtual {v0, v2}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroid/widget/RelativeLayout;

    invoke-virtual {v1}, Landroid/view/ViewGroup;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v1

    check-cast v1, Landroid/view/ViewGroup$MarginLayoutParams;

    invoke-static {}, Lcom/android/camera/Display;->getBottomHeight()I

    move-result v2

    iput v2, v1, Landroid/view/ViewGroup$MarginLayoutParams;->height:I

    :goto_0
    const v1, 0x7f090105

    invoke-virtual {v0, v1}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/ImageView;

    const v1, 0x7f090104

    invoke-virtual {p0, v1}, Lcom/android/camera/module/BaseModule;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroid/widget/ImageView;

    new-instance v2, Lcom/android/camera/dualvideo/O000OoOO;

    invoke-direct {v2, p0}, Lcom/android/camera/dualvideo/O000OoOO;-><init>(Lcom/android/camera/dualvideo/DualVideoSelectModule;)V

    invoke-virtual {v0, v2}, Landroid/widget/ImageView;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    new-instance v2, Lcom/android/camera/dualvideo/O000OoOo;

    invoke-direct {v2, p0}, Lcom/android/camera/dualvideo/O000OoOo;-><init>(Lcom/android/camera/dualvideo/DualVideoSelectModule;)V

    invoke-virtual {v1, v2}, Landroid/widget/ImageView;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    invoke-static {v1}, Lcom/android/camera/animation/FolmeUtils;->touchTint(Landroid/view/View;)V

    const/4 v2, 0x2

    new-array v2, v2, [Landroid/view/View;

    const/4 v4, 0x0

    aput-object v0, v2, v4

    aput-object v1, v2, v3

    invoke-static {v2}, Lcom/android/camera/animation/FolmeUtils;->touchScale([Landroid/view/View;)V

    iget-object v2, p0, Lcom/android/camera/dualvideo/DualVideoModuleBase;->mRotateAnimator:Lcom/android/camera/dualvideo/view/RotateAnimator;

    invoke-virtual {v2, v0}, Lcom/android/camera/dualvideo/view/RotateAnimator;->addView(Landroid/view/View;)V

    iget-object p0, p0, Lcom/android/camera/dualvideo/DualVideoModuleBase;->mRotateAnimator:Lcom/android/camera/dualvideo/view/RotateAnimator;

    invoke-virtual {p0, v1}, Lcom/android/camera/dualvideo/view/RotateAnimator;->addView(Landroid/view/View;)V

    return-void
.end method

.method private cancelSelsectAndBackRecording()V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->restoreSelectData()V

    invoke-direct {p0}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->confirmAndBackRecording()V

    const-string p0, "value_cancel_select"

    invoke-static {p0}, Lcom/android/camera/statistic/CameraStatUtils;->trackDualVideoCommonClick(Ljava/lang/String;)V

    return-void
.end method

.method private confirmAndBackRecording()V
    .locals 2

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/android/camera/dualvideo/DualVideoModuleBase;->mKeepRecorderWhenSwitching:Z

    const/4 v1, -0x1

    invoke-direct {p0, v1}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->switchToRecordWindow(I)V

    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->showOrHideBottom(Z)V

    return-void
.end method

.method private copySelectData()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/DualVideoModuleBase;->mMultiRecorderManager:Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->isRecording()Z

    move-result v0

    if-eqz v0, :cond_1

    iget-object v0, p0, Lcom/android/camera/dualvideo/DualVideoSelectModule;->mSelectDataBak:Ljava/util/ArrayList;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/android/camera/dualvideo/DualVideoSelectModule;->mSelectDataBak:Ljava/util/ArrayList;

    :cond_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/DualVideoSelectModule;->mSelectDataBak:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->clear()V

    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/O000Ooo0;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/O000Ooo0;-><init>(Lcom/android/camera/dualvideo/DualVideoSelectModule;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    :cond_1
    return-void
.end method

.method private onCancelViewClicked()V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->cancelSelsectAndBackRecording()V

    return-void
.end method

.method private onConfirmClicked(Landroid/view/View;)V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->confirmAndBackRecording()V

    const-string p0, "value_confirm_select"

    invoke-static {p0}, Lcom/android/camera/statistic/CameraStatUtils;->trackDualVideoCommonClick(Ljava/lang/String;)V

    return-void
.end method

.method private restoreSelectData()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/DualVideoSelectModule;->mSelectDataBak:Ljava/util/ArrayList;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object v0

    invoke-virtual {v0}, Ljava/util/ArrayList;->clear()V

    iget-object v1, p0, Lcom/android/camera/dualvideo/DualVideoSelectModule;->mSelectDataBak:Ljava/util/ArrayList;

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->addAll(Ljava/util/Collection;)Z

    iget-object v0, p0, Lcom/android/camera/dualvideo/DualVideoSelectModule;->mSelectDataBak:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->clear()V

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/android/camera/dualvideo/DualVideoSelectModule;->mSelectDataBak:Ljava/util/ArrayList;

    return-void
.end method

.method private switchToRecordWindow(I)V
    .locals 3

    sget-object v0, Lcom/android/camera/module/VideoBase;->TAG:Ljava/lang/String;

    const-string v1, "switchSelecteWindowToRecord: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->isSwitching()Z

    move-result v0

    if-nez v0, :cond_1

    iget-boolean v0, p0, Lcom/android/camera/dualvideo/DualVideoModuleBase;->mMainFrameIsAvailable:Z

    if-nez v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->switchThumbnailFunction(Z)V

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningDualVideo()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v1

    invoke-virtual {v1, v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->setmDrawSelectWindow(Z)V

    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->showModeSwitchLayout(Z)V

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->getRenderManager()Ljava/util/Optional;

    move-result-object v1

    sget-object v2, Lcom/android/camera/dualvideo/O000o00;->INSTANCE:Lcom/android/camera/dualvideo/O000o00;

    invoke-virtual {v1, v2}, Ljava/util/Optional;->ifPresent(Ljava/util/function/Consumer;)V

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->getRenderManager()Ljava/util/Optional;

    move-result-object v1

    sget-object v2, Lcom/android/camera/dualvideo/O000Ooo;->INSTANCE:Lcom/android/camera/dualvideo/O000Ooo;

    invoke-virtual {v1, v2}, Ljava/util/Optional;->ifPresent(Ljava/util/function/Consumer;)V

    invoke-virtual {p0}, Lcom/android/camera/module/BaseModule;->getActivity()Lcom/android/camera/Camera;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/ActivityBase;->getGLView()Lcom/android/camera/ui/V6CameraGLSurfaceView;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/ui/V6CameraGLSurfaceView;->requestRender()V

    invoke-virtual {p0, v0, v0, v0, p1}, Lcom/android/camera/module/BaseModule;->switchCameraLens(ZZZI)V

    :cond_1
    :goto_0
    return-void
.end method


# virtual methods
.method public synthetic O0000Oo(Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/DualVideoSelectModule;->mSelectDataBak:Ljava/util/ArrayList;

    new-instance v0, Lcom/android/camera/dualvideo/util/UserSelectData;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/util/UserSelectData;-><init>(Lcom/android/camera/dualvideo/util/UserSelectData;)V

    invoke-virtual {p0, v0}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public synthetic O000O0OO(Landroid/view/View;)V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->onCancelViewClicked()V

    return-void
.end method

.method public addUserGuide()V
    .locals 4

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p0

    const/16 v0, 0xac

    invoke-virtual {p0, v0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p0

    check-cast p0, Lcom/android/camera/protocol/ModeProtocol$TopAlert;

    instance-of v0, p0, Lcom/android/camera/fragment/BaseFragment;

    if-eqz v0, :cond_1

    check-cast p0, Lcom/android/camera/fragment/BaseFragment;

    sget-object v0, Lcom/android/camera/dualvideo/user_guide/FragmentDualVideoUserGuide;->TAG:Ljava/lang/String;

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getFragmentManager()Landroidx/fragment/app/FragmentManager;

    move-result-object v1

    invoke-static {v1, v0}, Lcom/android/camera/fragment/FragmentUtils;->getFragmentByTag(Landroidx/fragment/app/FragmentManager;Ljava/lang/String;)Landroidx/fragment/app/Fragment;

    move-result-object v1

    if-nez v1, :cond_0

    new-instance v1, Lcom/android/camera/dualvideo/user_guide/FragmentDualVideoUserGuide;

    invoke-direct {v1}, Lcom/android/camera/dualvideo/user_guide/FragmentDualVideoUserGuide;-><init>()V

    const/4 v2, 0x2

    const v3, 0x7f1100b9

    invoke-virtual {v1, v2, v3}, Landroidx/fragment/app/DialogFragment;->setStyle(II)V

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getFragmentManager()Landroidx/fragment/app/FragmentManager;

    move-result-object p0

    invoke-virtual {p0}, Landroidx/fragment/app/FragmentManager;->beginTransaction()Landroidx/fragment/app/FragmentTransaction;

    move-result-object p0

    invoke-virtual {p0, v1, v0}, Landroidx/fragment/app/FragmentTransaction;->add(Landroidx/fragment/app/Fragment;Ljava/lang/String;)Landroidx/fragment/app/FragmentTransaction;

    move-result-object p0

    invoke-virtual {p0}, Landroidx/fragment/app/FragmentTransaction;->commitAllowingStateLoss()I

    :cond_0
    const-string/jumbo p0, "value_user_guide"

    invoke-static {p0}, Lcom/android/camera/statistic/CameraStatUtils;->trackDualVideoCommonClick(Ljava/lang/String;)V

    return-void

    :cond_1
    sget-object p0, Lcom/android/camera/module/VideoBase;->TAG:Ljava/lang/String;

    const-string v0, "addUserGuide:  failed "

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method public onBackPressed()Z
    .locals 2

    invoke-virtual {p0}, Lcom/android/camera/module/BaseModule;->isFrameAvailable()Z

    move-result v0

    if-nez v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-boolean v0, p0, Lcom/android/camera/module/BaseModule;->mPaused:Z

    const/4 v1, 0x1

    if-nez v0, :cond_3

    iget-object v0, p0, Lcom/android/camera/module/BaseModule;->mActivity:Lcom/android/camera/Camera;

    invoke-virtual {v0}, Lcom/android/camera/ActivityBase;->isActivityPaused()Z

    move-result v0

    if-eqz v0, :cond_1

    goto :goto_0

    :cond_1
    iget-object v0, p0, Lcom/android/camera/dualvideo/DualVideoModuleBase;->mMultiRecorderManager:Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->isRecording()Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-direct {p0}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->cancelSelsectAndBackRecording()V

    return v1

    :cond_2
    invoke-super {p0}, Lcom/android/camera/module/VideoBase;->onBackPressed()Z

    move-result p0

    return p0

    :cond_3
    :goto_0
    return v1
.end method

.method public onResume()V
    .locals 2

    invoke-super {p0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->onResume()V

    invoke-direct {p0}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->copySelectData()V

    iget-object v0, p0, Lcom/android/camera/dualvideo/DualVideoModuleBase;->mMultiRecorderManager:Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->isRecordingPaused()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->showModeSwitchLayout(Z)V

    invoke-virtual {p0}, Lcom/android/camera/module/BaseModule;->getActivity()Lcom/android/camera/Camera;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/O000OooO;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/O000OooO;-><init>(Lcom/android/camera/dualvideo/DualVideoSelectModule;)V

    invoke-virtual {v0, v1}, Landroid/app/Activity;->runOnUiThread(Ljava/lang/Runnable;)V

    goto :goto_0

    :cond_0
    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->showModeSwitchLayout(Z)V

    :goto_0
    return-void
.end method

.method public onShutterButtonClick(I)V
    .locals 2

    sget-object p1, Lcom/android/camera/module/VideoBase;->TAG:Ljava/lang/String;

    const/4 v0, 0x3

    const-string v1, "onShutterButtonClick"

    invoke-static {v0, p1, v1}, Lcom/android/camera/log/Log;->k(ILjava/lang/String;Ljava/lang/String;)I

    const/4 p1, 0x2

    invoke-direct {p0, p1}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->switchToRecordWindow(I)V

    return-void
.end method

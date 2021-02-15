.class public Lcom/android/camera/module/impl/component/PresentationDisplayImpl;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/android/camera/protocol/ModeProtocol$PresentationDisplay;


# static fields
.field private static final MIUI_MULTI_DMS_NAME:Ljava/lang/String; = "miui_multi_display"

.field private static final TAG:Ljava/lang/String; = "PresentationDisplayImpl"


# instance fields
.field private SUB_SCREEN_DISPLAY_NUM:I

.field private mActivity:Lcom/android/camera/Camera;

.field private mPresentationDisplay:Lcom/android/camera/PresentationDisplay;


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Lcom/android/camera/ActivityBase;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x1

    iput v0, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->SUB_SCREEN_DISPLAY_NUM:I

    check-cast p1, Lcom/android/camera/Camera;

    iput-object p1, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mActivity:Lcom/android/camera/Camera;

    iget-object p1, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mPresentationDisplay:Lcom/android/camera/PresentationDisplay;

    if-nez p1, :cond_0

    new-instance p1, Lcom/android/camera/PresentationDisplay;

    iget-object v0, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mActivity:Lcom/android/camera/Camera;

    invoke-direct {p1, v0}, Lcom/android/camera/PresentationDisplay;-><init>(Landroid/content/Context;)V

    iput-object p1, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mPresentationDisplay:Lcom/android/camera/PresentationDisplay;

    :cond_0
    return-void
.end method

.method public static create(Lcom/android/camera/ActivityBase;)Lcom/android/camera/module/impl/component/PresentationDisplayImpl;
    .locals 1

    new-instance v0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;

    invoke-direct {v0, p0}, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;-><init>(Lcom/android/camera/ActivityBase;)V

    return-object v0
.end method

.method private getActivity()Ljava/util/Optional;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mActivity:Lcom/android/camera/Camera;

    if-nez p0, :cond_0

    invoke-static {}, Ljava/util/Optional;->empty()Ljava/util/Optional;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-static {p0}, Ljava/util/Optional;->ofNullable(Ljava/lang/Object;)Ljava/util/Optional;

    move-result-object p0

    return-object p0
.end method


# virtual methods
.method public cancel()V
    .locals 2

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOOO0()Z

    move-result v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mPresentationDisplay:Lcom/android/camera/PresentationDisplay;

    if-eqz v0, :cond_1

    invoke-virtual {v0}, Landroid/app/Presentation;->isShowing()Z

    move-result v0

    if-eqz v0, :cond_1

    iget-object v0, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mPresentationDisplay:Lcom/android/camera/PresentationDisplay;

    invoke-virtual {v0}, Landroid/app/Presentation;->dismiss()V

    sget-object v0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v1, "presentation display cancel"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :cond_1
    invoke-virtual {p0}, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->closePresentationDisplay()V

    return-void
.end method

.method public closePresentationDisplay()V
    .locals 2

    sget-object v0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v1, "E: closeSubDisplay"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const-string v0, "miui_multi_display"

    invoke-static {v0}, Landroid/os/ServiceManager;->getService(Ljava/lang/String;)Landroid/os/IBinder;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-static {v0}, Lmiui/hardware/display/IMiuiMultiDisplayManager$Stub;->asInterface(Landroid/os/IBinder;)Lmiui/hardware/display/IMiuiMultiDisplayManager;

    move-result-object v0

    :try_start_0
    iget p0, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->SUB_SCREEN_DISPLAY_NUM:I

    const/4 v1, 0x0

    invoke-interface {v0, p0, v1}, Lmiui/hardware/display/IMiuiMultiDisplayManager;->setDisplayStateIgnoreFold(IZ)Z

    sget-object p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v0, "X: closeSubDisplay, multi display manager service Success!"

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I
    :try_end_0
    .catch Landroid/os/RemoteException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    sget-object p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v0, "open sub display manager service connect fail!"

    goto :goto_0

    :cond_0
    sget-object p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v0, "multi display manager service no found! "

    :goto_0
    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :goto_1
    return-void
.end method

.method public isModeSupportPresentation(I)Z
    .locals 0

    const/16 p0, 0xa3

    if-eq p0, p1, :cond_1

    const/16 p0, 0xa5

    if-ne p0, p1, :cond_0

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

.method public openPresentationDisplay()V
    .locals 2

    sget-object v0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v1, "E: openSubDisplay"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const-string v0, "miui_multi_display"

    invoke-static {v0}, Landroid/os/ServiceManager;->getService(Ljava/lang/String;)Landroid/os/IBinder;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-static {v0}, Lmiui/hardware/display/IMiuiMultiDisplayManager$Stub;->asInterface(Landroid/os/IBinder;)Lmiui/hardware/display/IMiuiMultiDisplayManager;

    move-result-object v0

    :try_start_0
    iget p0, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->SUB_SCREEN_DISPLAY_NUM:I

    const/4 v1, 0x1

    invoke-interface {v0, p0, v1}, Lmiui/hardware/display/IMiuiMultiDisplayManager;->setDisplayStateIgnoreFold(IZ)Z

    sget-object p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v0, "X: openSubDisplay, open sub display sucess!"

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I
    :try_end_0
    .catch Landroid/os/RemoteException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    sget-object p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v0, "open sub display manager service connect fail!"

    goto :goto_0

    :cond_0
    sget-object p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->TAG:Ljava/lang/String;

    const-string v0, "multi display manager service no found! "

    :goto_0
    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :goto_1
    return-void
.end method

.method public registerProtocol()V
    .locals 2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x3b1

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->attachProtocol(ILcom/android/camera/protocol/ModeProtocol$BaseProtocol;)V

    return-void
.end method

.method public show(I)V
    .locals 0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object p1

    invoke-virtual {p1}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOOO0()Z

    move-result p1

    if-nez p1, :cond_0

    return-void

    :cond_0
    invoke-direct {p0}, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->getActivity()Ljava/util/Optional;

    move-result-object p1

    invoke-virtual {p1}, Ljava/util/Optional;->isPresent()Z

    move-result p1

    if-nez p1, :cond_1

    return-void

    :cond_1
    iget-object p1, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mActivity:Lcom/android/camera/Camera;

    invoke-virtual {p1}, Lcom/android/camera/Camera;->getCurrentModuleIndex()I

    move-result p1

    invoke-virtual {p0, p1}, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->isModeSupportPresentation(I)Z

    move-result p0

    return-void
.end method

.method public unRegisterProtocol()V
    .locals 2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x3b1

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->detachProtocol(ILcom/android/camera/protocol/ModeProtocol$BaseProtocol;)V

    return-void
.end method

.method public updateTextureSize()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/module/impl/component/PresentationDisplayImpl;->mPresentationDisplay:Lcom/android/camera/PresentationDisplay;

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Lcom/android/camera/PresentationDisplay;->updateTextureSize()V

    :cond_0
    return-void
.end method

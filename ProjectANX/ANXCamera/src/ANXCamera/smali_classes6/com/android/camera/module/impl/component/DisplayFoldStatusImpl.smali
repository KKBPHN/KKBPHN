.class public Lcom/android/camera/module/impl/component/DisplayFoldStatusImpl;
.super Landroid/view/IDisplayFoldListener$Stub;
.source ""

# interfaces
.implements Lcom/android/camera/protocol/ModeProtocol$DisplayFoldStatus;


# static fields
.field private static final TAG:Ljava/lang/String; = "DisplayFoldStatusImpl"


# instance fields
.field private final mActivity:Lcom/android/camera/ActivityBase;

.field private mIsFolded:Z


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Lcom/android/camera/ActivityBase;)V
    .locals 0

    invoke-direct {p0}, Landroid/view/IDisplayFoldListener$Stub;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/impl/component/DisplayFoldStatusImpl;->mActivity:Lcom/android/camera/ActivityBase;

    return-void
.end method

.method public static create(Lcom/android/camera/ActivityBase;)Lcom/android/camera/module/impl/component/DisplayFoldStatusImpl;
    .locals 1

    new-instance v0, Lcom/android/camera/module/impl/component/DisplayFoldStatusImpl;

    invoke-direct {v0, p0}, Lcom/android/camera/module/impl/component/DisplayFoldStatusImpl;-><init>(Lcom/android/camera/ActivityBase;)V

    return-object v0
.end method


# virtual methods
.method public isDisplayFold()Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/module/impl/component/DisplayFoldStatusImpl;->mIsFolded:Z

    return p0
.end method

.method public onDisplayFoldChanged(IZ)V
    .locals 3

    sget-object v0, Lcom/android/camera/module/impl/component/DisplayFoldStatusImpl;->TAG:Ljava/lang/String;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "displayId:"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, ",folded:"

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-static {v0, p1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iput-boolean p2, p0, Lcom/android/camera/module/impl/component/DisplayFoldStatusImpl;->mIsFolded:Z

    return-void
.end method

.method public registerProtocol()V
    .locals 2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x3aa

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->attachProtocol(ILcom/android/camera/protocol/ModeProtocol$BaseProtocol;)V

    invoke-static {}, Lcom/android/camera/Display;->getWindowManager()Landroid/view/IWindowManager;

    move-result-object v0

    if-eqz v0, :cond_0

    :try_start_0
    invoke-static {}, Lcom/android/camera/Display;->getWindowManager()Landroid/view/IWindowManager;

    move-result-object v0

    invoke-interface {v0, p0}, Landroid/view/IWindowManager;->registerDisplayFoldListener(Landroid/view/IDisplayFoldListener;)V
    :try_end_0
    .catch Landroid/os/RemoteException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception p0

    invoke-virtual {p0}, Landroid/os/RemoteException;->printStackTrace()V

    :cond_0
    :goto_0
    return-void
.end method

.method public unRegisterProtocol()V
    .locals 2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x3aa

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->detachProtocol(ILcom/android/camera/protocol/ModeProtocol$BaseProtocol;)V

    invoke-static {}, Lcom/android/camera/Display;->getWindowManager()Landroid/view/IWindowManager;

    move-result-object v0

    if-eqz v0, :cond_0

    :try_start_0
    invoke-static {}, Lcom/android/camera/Display;->getWindowManager()Landroid/view/IWindowManager;

    move-result-object v0

    invoke-interface {v0, p0}, Landroid/view/IWindowManager;->unregisterDisplayFoldListener(Landroid/view/IDisplayFoldListener;)V
    :try_end_0
    .catch Landroid/os/RemoteException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception p0

    invoke-virtual {p0}, Landroid/os/RemoteException;->printStackTrace()V

    :cond_0
    :goto_0
    return-void
.end method

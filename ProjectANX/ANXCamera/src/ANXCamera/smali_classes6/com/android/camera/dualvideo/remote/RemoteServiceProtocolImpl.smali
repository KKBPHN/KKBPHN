.class public Lcom/android/camera/dualvideo/remote/RemoteServiceProtocolImpl;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/android/camera/protocol/ModeProtocol$RemoteCameraServiceProtocol;


# static fields
.field private static final TAG:Ljava/lang/String; = "RemoteServiceProtocolIm"


# instance fields
.field private mRemoteService:Lcom/android/camera/dualvideo/remote/RemoteService;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public getService()Lcom/android/camera/dualvideo/remote/RemoteService;
    .locals 2

    const-string v0, "RemoteServiceProtocolIm"

    const-string v1, "getService: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, p0, Lcom/android/camera/dualvideo/remote/RemoteServiceProtocolImpl;->mRemoteService:Lcom/android/camera/dualvideo/remote/RemoteService;

    if-nez v0, :cond_0

    new-instance v0, Lcom/android/camera/dualvideo/remote/RemoteService;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/remote/RemoteService;-><init>()V

    iput-object v0, p0, Lcom/android/camera/dualvideo/remote/RemoteServiceProtocolImpl;->mRemoteService:Lcom/android/camera/dualvideo/remote/RemoteService;

    :cond_0
    iget-object p0, p0, Lcom/android/camera/dualvideo/remote/RemoteServiceProtocolImpl;->mRemoteService:Lcom/android/camera/dualvideo/remote/RemoteService;

    return-object p0
.end method

.method public registerProtocol()V
    .locals 2

    const-string v0, "RemoteServiceProtocolIm"

    const-string v1, "registerProtocol: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x1a6

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->attachProtocol(ILcom/android/camera/protocol/ModeProtocol$BaseProtocol;)V

    return-void
.end method

.method public unRegisterProtocol()V
    .locals 2

    const-string v0, "RemoteServiceProtocolIm"

    const-string v1, "unRegisterProtocol: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x1a6

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->detachProtocol(ILcom/android/camera/protocol/ModeProtocol$BaseProtocol;)V

    iget-object v0, p0, Lcom/android/camera/dualvideo/remote/RemoteServiceProtocolImpl;->mRemoteService:Lcom/android/camera/dualvideo/remote/RemoteService;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/remote/RemoteService;->stopStreaming()V

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/android/camera/dualvideo/remote/RemoteServiceProtocolImpl;->mRemoteService:Lcom/android/camera/dualvideo/remote/RemoteService;

    :cond_0
    return-void
.end method

.class public Lcom/android/camera/dualvideo/remote/RemoteService;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final TAG:Ljava/lang/String; = "RemoteService"


# instance fields
.field private ijkMediaPlayer:Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

.field private mIsStreaming:Z


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->mIsStreaming:Z

    return-void
.end method


# virtual methods
.method public isStreaming()Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->mIsStreaming:Z

    return p0
.end method

.method public startStreaming(Landroid/view/Surface;)V
    .locals 4

    iget-boolean v0, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->mIsStreaming:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    const-string v0, "RemoteService"

    const-string v1, "startStreaming"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Ljava/util/Locale;->US:Ljava/util/Locale;

    const/4 v1, 0x2

    new-array v1, v1, [Ljava/lang/Object;

    const/4 v2, 0x0

    sget-object v3, Lcom/android/camera/dualvideo/render/RenderUtil;->REMOTE_SIZE:Landroid/util/Size;

    invoke-virtual {v3}, Landroid/util/Size;->getWidth()I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    aput-object v3, v1, v2

    sget-object v2, Lcom/android/camera/dualvideo/render/RenderUtil;->REMOTE_SIZE:Landroid/util/Size;

    invoke-virtual {v2}, Landroid/util/Size;->getHeight()I

    move-result v2

    invoke-static {v2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    const/4 v3, 0x1

    aput-object v2, v1, v3

    const-string v2, "rtsp://192.168.43.1:8086?h264=50000-30-%d-%d"

    invoke-static {v0, v2, v1}, Ljava/lang/String;->format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    :try_start_0
    new-instance v1, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    invoke-direct {v1}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;-><init>()V

    iput-object v1, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->ijkMediaPlayer:Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    iget-object v1, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->ijkMediaPlayer:Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    invoke-virtual {v1, p1}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setSurface(Landroid/view/Surface;)V

    iget-object p1, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->ijkMediaPlayer:Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    invoke-virtual {p1, v0}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->setDataSource(Ljava/lang/String;)V

    iget-object p1, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->ijkMediaPlayer:Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    invoke-virtual {p1}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->prepareAsync()V

    iget-object p1, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->ijkMediaPlayer:Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    invoke-virtual {p1}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->start()V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception p1

    invoke-virtual {p1}, Ljava/io/IOException;->printStackTrace()V

    :goto_0
    iput-boolean v3, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->mIsStreaming:Z

    return-void
.end method

.method public stopStreaming()V
    .locals 2

    const-string v0, "RemoteService"

    const-string v1, "stopStreaming"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->mIsStreaming:Z

    iget-object v0, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->ijkMediaPlayer:Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->stop()V

    iget-object p0, p0, Lcom/android/camera/dualvideo/remote/RemoteService;->ijkMediaPlayer:Ltv/danmaku/ijk/media/player/IjkMediaPlayer;

    invoke-virtual {p0}, Ltv/danmaku/ijk/media/player/IjkMediaPlayer;->release()V

    :cond_0
    return-void
.end method

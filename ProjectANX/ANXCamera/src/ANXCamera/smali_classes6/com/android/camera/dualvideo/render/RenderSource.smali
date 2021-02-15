.class public Lcom/android/camera/dualvideo/render/RenderSource;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final SKIP_FRAMES:I = 0x2

.field private static final TAG:Ljava/lang/String; = "RenderSource"


# instance fields
.field private mCanDraw:Z

.field private mFrameReady:Z

.field private mFramesNeedSkip:I

.field private mHandler:Landroid/os/Handler;

.field private mListener:Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;

.field private final mSourceDeviceId:I

.field private mSurface:Landroid/view/Surface;

.field private mSurfaceTexture:Landroid/graphics/SurfaceTexture;

.field private mTexture:Lcom/android/gallery3d/ui/ExtTexture;

.field private mTextureSize:Landroid/util/Size;


# direct methods
.method public constructor <init>(ILandroid/os/Handler;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x2

    iput v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mFramesNeedSkip:I

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mCanDraw:Z

    iput p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSourceDeviceId:I

    iput-object p2, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mHandler:Landroid/os/Handler;

    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/RenderSource;->resetStatus()V

    return-void
.end method

.method static synthetic access$000(Lcom/android/camera/dualvideo/render/RenderSource;)Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mCanDraw:Z

    return p0
.end method

.method static synthetic access$002(Lcom/android/camera/dualvideo/render/RenderSource;Z)Z
    .locals 0

    iput-boolean p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mCanDraw:Z

    return p1
.end method

.method static synthetic access$100(Lcom/android/camera/dualvideo/render/RenderSource;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mFramesNeedSkip:I

    return p0
.end method

.method static synthetic access$110(Lcom/android/camera/dualvideo/render/RenderSource;)I
    .locals 2

    iget v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mFramesNeedSkip:I

    add-int/lit8 v1, v0, -0x1

    iput v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mFramesNeedSkip:I

    return v0
.end method

.method static synthetic access$200(Lcom/android/camera/dualvideo/render/RenderSource;)Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mListener:Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;

    return-object p0
.end method

.method static synthetic access$300(Lcom/android/camera/dualvideo/render/RenderSource;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSourceDeviceId:I

    return p0
.end method

.method private createSurfaceTexture()V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    if-nez v0, :cond_0

    new-instance v0, Landroid/graphics/SurfaceTexture;

    const/4 v1, 0x0

    invoke-direct {v0, v1}, Landroid/graphics/SurfaceTexture;-><init>(I)V

    iput-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    :cond_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    iget-object v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTextureSize:Landroid/util/Size;

    invoke-virtual {v1}, Landroid/util/Size;->getWidth()I

    move-result v1

    iget-object v2, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTextureSize:Landroid/util/Size;

    invoke-virtual {v2}, Landroid/util/Size;->getHeight()I

    move-result v2

    invoke-virtual {v0, v1, v2}, Landroid/graphics/SurfaceTexture;->setDefaultBufferSize(II)V

    new-instance v0, Landroid/view/Surface;

    iget-object v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    invoke-direct {v0, v1}, Landroid/view/Surface;-><init>(Landroid/graphics/SurfaceTexture;)V

    iput-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurface:Landroid/view/Surface;

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    new-instance v1, Lcom/android/camera/dualvideo/render/RenderSource$1;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/RenderSource$1;-><init>(Lcom/android/camera/dualvideo/render/RenderSource;)V

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mHandler:Landroid/os/Handler;

    invoke-virtual {v0, v1, p0}, Landroid/graphics/SurfaceTexture;->setOnFrameAvailableListener(Landroid/graphics/SurfaceTexture$OnFrameAvailableListener;Landroid/os/Handler;)V

    return-void
.end method

.method private resetStatus()V
    .locals 1

    const/4 v0, 0x2

    iput v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mFramesNeedSkip:I

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mFrameReady:Z

    iput-boolean v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mCanDraw:Z

    return-void
.end method


# virtual methods
.method public attachToGL(Lcom/android/gallery3d/ui/GLCanvas;)V
    .locals 2

    const-string v0, "RenderSource"

    const-string v1, "attachToGL: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    invoke-static {v0}, Lcom/android/camera/dualvideo/util/Assert;->check(Z)V

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    if-eqz v0, :cond_1

    return-void

    :cond_1
    new-instance v0, Lcom/android/gallery3d/ui/ExtTexture;

    invoke-direct {v0}, Lcom/android/gallery3d/ui/ExtTexture;-><init>()V

    iput-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    invoke-virtual {v0, p1}, Lcom/android/gallery3d/ui/ExtTexture;->onBind(Lcom/android/gallery3d/ui/GLCanvas;)Z

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTextureSize:Landroid/util/Size;

    invoke-virtual {v0}, Landroid/util/Size;->getWidth()I

    move-result v0

    iget-object v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTextureSize:Landroid/util/Size;

    invoke-virtual {v1}, Landroid/util/Size;->getHeight()I

    move-result v1

    invoke-virtual {p1, v0, v1}, Lcom/android/gallery3d/ui/BasicTexture;->setSize(II)V

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    invoke-virtual {p1}, Landroid/graphics/SurfaceTexture;->detachFromGLContext()V

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    invoke-virtual {p0}, Lcom/android/gallery3d/ui/BasicTexture;->getId()I

    move-result p0

    invoke-virtual {p1, p0}, Landroid/graphics/SurfaceTexture;->attachToGLContext(I)V

    return-void
.end method

.method public canDraw()Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mCanDraw:Z

    return p0
.end method

.method public getSourceDeviceId()I
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSourceDeviceId:I

    return p0
.end method

.method public getSurface()Landroid/view/Surface;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurface:Landroid/view/Surface;

    return-object p0
.end method

.method public getSurfaceTexture()Landroid/graphics/SurfaceTexture;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    return-object p0
.end method

.method public getTexId()I
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    invoke-virtual {p0}, Lcom/android/gallery3d/ui/BasicTexture;->getId()I

    move-result p0

    return p0
.end method

.method public getTexture()Lcom/android/gallery3d/ui/ExtTexture;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    return-object p0
.end method

.method public prepare(Landroid/util/Size;)V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTextureSize:Landroid/util/Size;

    if-nez v0, :cond_0

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTextureSize:Landroid/util/Size;

    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/RenderSource;->createSurfaceTexture()V

    goto :goto_0

    :cond_0
    invoke-virtual {v0, p1}, Landroid/util/Size;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTextureSize:Landroid/util/Size;

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    invoke-virtual {p1}, Landroid/util/Size;->getWidth()I

    move-result v1

    invoke-virtual {p1}, Landroid/util/Size;->getHeight()I

    move-result p1

    invoke-virtual {v0, v1, p1}, Landroid/graphics/SurfaceTexture;->setDefaultBufferSize(II)V

    :cond_1
    :goto_0
    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/RenderSource;->resetStatus()V

    return-void
.end method

.method public release(Lcom/android/gallery3d/ui/GLCanvas;)V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    if-nez v0, :cond_0

    return-void

    :cond_0
    const/4 v1, 0x0

    iput-boolean v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mCanDraw:Z

    const/4 v1, 0x0

    iput-object v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mHandler:Landroid/os/Handler;

    iput-object v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mListener:Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;

    if-eqz p1, :cond_1

    invoke-interface {p1, v0}, Lcom/android/gallery3d/ui/GLCanvas;->deleteSurfaceTexture(Landroid/graphics/SurfaceTexture;)V

    goto :goto_0

    :cond_1
    invoke-virtual {v0}, Landroid/graphics/SurfaceTexture;->release()V

    :goto_0
    iput-object v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurfaceTexture:Landroid/graphics/SurfaceTexture;

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurface:Landroid/view/Surface;

    if-eqz p1, :cond_2

    invoke-virtual {p1}, Landroid/view/Surface;->release()V

    iput-object v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mSurface:Landroid/view/Surface;

    :cond_2
    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    if-eqz p1, :cond_3

    invoke-virtual {p1}, Lcom/android/gallery3d/ui/BasicTexture;->recycle()V

    iput-object v1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mTexture:Lcom/android/gallery3d/ui/ExtTexture;

    :cond_3
    return-void
.end method

.method public setListener(Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource;->mListener:Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;

    return-void
.end method

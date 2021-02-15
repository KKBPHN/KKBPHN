.class public Lcom/android/camera/PresentationDisplay;
.super Landroid/app/Presentation;
.source ""

# interfaces
.implements Landroid/opengl/GLSurfaceView$Renderer;
.implements Lcom/android/camera/CameraScreenNail$RequestRenderListener;


# static fields
.field private static final TAG:Ljava/lang/String; = "PresentationDisplay"


# instance fields
.field private mCameraActivity:Lcom/android/camera/Camera;

.field private mFullFrameTexture:Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;

.field private mHeight:I

.field private mTextureView:Lcom/android/camera/ui/GLTextureView;

.field private mWidth:I


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;)V
    .locals 1

    invoke-static {p1}, Lcom/android/camera/PresentationDisplay;->getDisplay(Landroid/content/Context;)Landroid/view/Display;

    move-result-object v0

    invoke-direct {p0, p1, v0}, Lcom/android/camera/PresentationDisplay;-><init>(Landroid/content/Context;Landroid/view/Display;)V

    check-cast p1, Lcom/android/camera/Camera;

    iput-object p1, p0, Lcom/android/camera/PresentationDisplay;->mCameraActivity:Lcom/android/camera/Camera;

    return-void
.end method

.method private constructor <init>(Landroid/content/Context;Landroid/view/Display;)V
    .locals 0

    invoke-direct {p0, p1, p2}, Landroid/app/Presentation;-><init>(Landroid/content/Context;Landroid/view/Display;)V

    const/16 p1, 0x370

    iput p1, p0, Lcom/android/camera/PresentationDisplay;->mWidth:I

    const/16 p1, 0x495

    iput p1, p0, Lcom/android/camera/PresentationDisplay;->mHeight:I

    return-void
.end method

.method static synthetic access$000(Lcom/android/camera/PresentationDisplay;)Lcom/android/camera/Camera;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/PresentationDisplay;->mCameraActivity:Lcom/android/camera/Camera;

    return-object p0
.end method

.method private static getDisplay(Landroid/content/Context;)Landroid/view/Display;
    .locals 1

    const-string v0, "display"

    invoke-virtual {p0, v0}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Landroid/hardware/display/DisplayManager;

    invoke-virtual {p0}, Landroid/hardware/display/DisplayManager;->getDisplays()[Landroid/view/Display;

    move-result-object p0

    const/4 v0, 0x1

    aget-object p0, p0, v0

    return-object p0
.end method

.method private initStillPreviewRender(Lcom/android/camera/ui/GLTextureView;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/PresentationDisplay;->mCameraActivity:Lcom/android/camera/Camera;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Lcom/android/camera/ActivityBase;->getCameraScreenNail()Lcom/android/camera/CameraScreenNail;

    move-result-object v0

    invoke-virtual {v0, p0}, Lcom/android/camera/CameraScreenNail;->addRequestListener(Lcom/android/camera/CameraScreenNail$RequestRenderListener;)V

    :cond_0
    invoke-virtual {p1}, Lcom/android/camera/ui/GLTextureView;->getRenderer()Landroid/opengl/GLSurfaceView$Renderer;

    move-result-object v0

    if-nez v0, :cond_1

    const/4 v0, 0x2

    invoke-virtual {p1, v0}, Lcom/android/camera/ui/GLTextureView;->setEGLContextClientVersion(I)V

    new-instance v0, Lcom/android/camera/PresentationDisplay$1;

    invoke-direct {v0, p0}, Lcom/android/camera/PresentationDisplay$1;-><init>(Lcom/android/camera/PresentationDisplay;)V

    invoke-virtual {p1, v0}, Lcom/android/camera/ui/GLTextureView;->setEGLShareContextGetter(Lcom/android/camera/ui/GLTextureView$EGLShareContextGetter;)V

    invoke-virtual {p1, p0}, Lcom/android/camera/ui/GLTextureView;->setRenderer(Landroid/opengl/GLSurfaceView$Renderer;)V

    const/4 p0, 0x0

    invoke-virtual {p1, p0}, Lcom/android/camera/ui/GLTextureView;->setRenderMode(I)V

    :cond_1
    return-void
.end method


# virtual methods
.method protected onCreate(Landroid/os/Bundle;)V
    .locals 0

    invoke-super {p0, p1}, Landroid/app/Presentation;->onCreate(Landroid/os/Bundle;)V

    const p1, 0x7f0c001e

    invoke-virtual {p0, p1}, Landroid/app/Presentation;->setContentView(I)V

    const p1, 0x7f0902b6

    invoke-virtual {p0, p1}, Landroid/app/Presentation;->findViewById(I)Landroid/view/View;

    move-result-object p1

    check-cast p1, Lcom/android/camera/ui/GLTextureView;

    iput-object p1, p0, Lcom/android/camera/PresentationDisplay;->mTextureView:Lcom/android/camera/ui/GLTextureView;

    iget-object p1, p0, Lcom/android/camera/PresentationDisplay;->mTextureView:Lcom/android/camera/ui/GLTextureView;

    invoke-direct {p0, p1}, Lcom/android/camera/PresentationDisplay;->initStillPreviewRender(Lcom/android/camera/ui/GLTextureView;)V

    invoke-virtual {p0}, Lcom/android/camera/PresentationDisplay;->updateTextureSize()V

    return-void
.end method

.method public onDrawFrame(Ljavax/microedition/khronos/opengles/GL10;)V
    .locals 0

    iget-object p1, p0, Lcom/android/camera/PresentationDisplay;->mCameraActivity:Lcom/android/camera/Camera;

    invoke-virtual {p1}, Lcom/android/camera/ActivityBase;->getCameraScreenNail()Lcom/android/camera/CameraScreenNail;

    move-result-object p1

    iget-object p0, p0, Lcom/android/camera/PresentationDisplay;->mFullFrameTexture:Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;

    invoke-virtual {p1}, Lcom/android/camera/SurfaceTextureScreenNail;->getExtTexture()Lcom/android/gallery3d/ui/ExtTexture;

    move-result-object p1

    invoke-virtual {p1}, Lcom/android/gallery3d/ui/BasicTexture;->getId()I

    move-result p1

    invoke-virtual {p0, p1}, Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;->draw(I)V

    return-void
.end method

.method public onSurfaceChanged(Ljavax/microedition/khronos/opengles/GL10;II)V
    .locals 0

    return-void
.end method

.method public onSurfaceCreated(Ljavax/microedition/khronos/opengles/GL10;Ljavax/microedition/khronos/egl/EGLConfig;)V
    .locals 0

    iget-object p1, p0, Lcom/android/camera/PresentationDisplay;->mFullFrameTexture:Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;

    if-nez p1, :cond_0

    new-instance p1, Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;

    invoke-direct {p1}, Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;-><init>()V

    iput-object p1, p0, Lcom/android/camera/PresentationDisplay;->mFullFrameTexture:Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;

    iget-object p0, p0, Lcom/android/camera/PresentationDisplay;->mFullFrameTexture:Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;

    invoke-virtual {p0}, Lcom/android/camera/effect/framework/gles/FullFramenOESTexture;->enableMirror()V

    :cond_0
    return-void
.end method

.method public requestRender()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/PresentationDisplay;->mTextureView:Lcom/android/camera/ui/GLTextureView;

    invoke-virtual {p0}, Lcom/android/camera/ui/GLTextureView;->requestRender()V

    return-void
.end method

.method public updateTextureSize()V
    .locals 2

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemConfig()Lcom/android/camera/data/data/config/DataItemConfig;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/config/DataItemConfig;->getComponentConfigRatio()Lcom/android/camera/data/data/config/ComponentConfigRatio;

    move-result-object v0

    iget-object v1, p0, Lcom/android/camera/PresentationDisplay;->mCameraActivity:Lcom/android/camera/Camera;

    invoke-virtual {v1}, Lcom/android/camera/Camera;->getCurrentModuleIndex()I

    move-result v1

    invoke-virtual {v0, v1}, Lcom/android/camera/data/data/config/ComponentConfigRatio;->getPictureSizeRatioString(I)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/android/camera/Util;->getRatio(Ljava/lang/String;)F

    move-result v0

    iget v1, p0, Lcom/android/camera/PresentationDisplay;->mWidth:I

    int-to-float v1, v1

    mul-float/2addr v1, v0

    float-to-int v0, v1

    iput v0, p0, Lcom/android/camera/PresentationDisplay;->mHeight:I

    iget-object v0, p0, Lcom/android/camera/PresentationDisplay;->mTextureView:Lcom/android/camera/ui/GLTextureView;

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Landroid/view/TextureView;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroid/view/ViewGroup$MarginLayoutParams;

    iget v1, p0, Lcom/android/camera/PresentationDisplay;->mWidth:I

    iput v1, v0, Landroid/view/ViewGroup$MarginLayoutParams;->width:I

    iget v1, p0, Lcom/android/camera/PresentationDisplay;->mHeight:I

    iput v1, v0, Landroid/view/ViewGroup$MarginLayoutParams;->height:I

    invoke-static {}, Lcom/android/camera/Display;->getTopHeight()I

    move-result v1

    iput v1, v0, Landroid/view/ViewGroup$MarginLayoutParams;->topMargin:I

    iget-object p0, p0, Lcom/android/camera/PresentationDisplay;->mTextureView:Lcom/android/camera/ui/GLTextureView;

    invoke-virtual {p0, v0}, Landroid/view/TextureView;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    :cond_0
    return-void
.end method

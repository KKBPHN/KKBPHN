.class Lcom/android/camera/PresentationDisplay$1;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/android/camera/ui/GLTextureView$EGLShareContextGetter;


# instance fields
.field final synthetic this$0:Lcom/android/camera/PresentationDisplay;


# direct methods
.method constructor <init>(Lcom/android/camera/PresentationDisplay;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/PresentationDisplay$1;->this$0:Lcom/android/camera/PresentationDisplay;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public getShareContext()Ljavax/microedition/khronos/egl/EGLContext;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/PresentationDisplay$1;->this$0:Lcom/android/camera/PresentationDisplay;

    invoke-static {p0}, Lcom/android/camera/PresentationDisplay;->access$000(Lcom/android/camera/PresentationDisplay;)Lcom/android/camera/Camera;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/ActivityBase;->getGLView()Lcom/android/camera/ui/V6CameraGLSurfaceView;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/ui/V6CameraGLSurfaceView;->getEGLContext()Ljavax/microedition/khronos/egl/EGLContext;

    move-result-object p0

    return-object p0
.end method

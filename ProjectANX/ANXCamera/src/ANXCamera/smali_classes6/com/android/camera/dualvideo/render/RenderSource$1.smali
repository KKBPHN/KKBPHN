.class Lcom/android/camera/dualvideo/render/RenderSource$1;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Landroid/graphics/SurfaceTexture$OnFrameAvailableListener;


# instance fields
.field final synthetic this$0:Lcom/android/camera/dualvideo/render/RenderSource;


# direct methods
.method constructor <init>(Lcom/android/camera/dualvideo/render/RenderSource;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource$1;->this$0:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onFrameAvailable(Landroid/graphics/SurfaceTexture;)V
    .locals 1

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource$1;->this$0:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderSource;->access$000(Lcom/android/camera/dualvideo/render/RenderSource;)Z

    move-result p1

    if-nez p1, :cond_1

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource$1;->this$0:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderSource;->access$100(Lcom/android/camera/dualvideo/render/RenderSource;)I

    move-result p1

    const-string v0, "RenderSource"

    if-lez p1, :cond_0

    const-string p1, "frame skipped: "

    invoke-static {v0, p1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource$1;->this$0:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderSource;->access$110(Lcom/android/camera/dualvideo/render/RenderSource;)I

    goto :goto_0

    :cond_0
    const-string p1, "subFrameReady"

    invoke-static {v0, p1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource$1;->this$0:Lcom/android/camera/dualvideo/render/RenderSource;

    const/4 v0, 0x1

    invoke-static {p1, v0}, Lcom/android/camera/dualvideo/render/RenderSource;->access$002(Lcom/android/camera/dualvideo/render/RenderSource;Z)Z

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource$1;->this$0:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderSource;->access$200(Lcom/android/camera/dualvideo/render/RenderSource;)Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;

    move-result-object p1

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;->onNewStreamAvailable()V

    :cond_1
    :goto_0
    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderSource$1;->this$0:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderSource;->access$200(Lcom/android/camera/dualvideo/render/RenderSource;)Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;

    move-result-object p1

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderSource$1;->this$0:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-static {p0}, Lcom/android/camera/dualvideo/render/RenderSource;->access$300(Lcom/android/camera/dualvideo/render/RenderSource;)I

    move-result p0

    invoke-interface {p1, p0}, Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;->onImageUpdated(I)V

    return-void
.end method

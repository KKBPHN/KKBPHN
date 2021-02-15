.class Lcom/android/camera/dualvideo/render/RenderManager$2;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/android/camera/dualvideo/render/RenderSource$SourceListener;


# instance fields
.field final synthetic this$0:Lcom/android/camera/dualvideo/render/RenderManager;

.field final synthetic val$source:Lcom/android/camera/dualvideo/render/RenderSource;


# direct methods
.method constructor <init>(Lcom/android/camera/dualvideo/render/RenderManager;Lcom/android/camera/dualvideo/render/RenderSource;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/RenderManager$2;->this$0:Lcom/android/camera/dualvideo/render/RenderManager;

    iput-object p2, p0, Lcom/android/camera/dualvideo/render/RenderManager$2;->val$source:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onImageUpdated(I)V
    .locals 0

    iget-object p1, p0, Lcom/android/camera/dualvideo/render/RenderManager$2;->val$source:Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/render/RenderSource;->canDraw()Z

    move-result p1

    if-eqz p1, :cond_0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderManager$2;->this$0:Lcom/android/camera/dualvideo/render/RenderManager;

    invoke-static {p0}, Lcom/android/camera/dualvideo/render/RenderManager;->access$000(Lcom/android/camera/dualvideo/render/RenderManager;)Lcom/android/camera/dualvideo/render/RenderManager$DualVideoRenderListener;

    move-result-object p0

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/RenderManager$DualVideoRenderListener;->onAuxSourceImageAvailable()V

    :cond_0
    return-void
.end method

.method public onNewStreamAvailable()V
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RenderManager$2;->this$0:Lcom/android/camera/dualvideo/render/RenderManager;

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/render/RenderManager;->enableContinuousRender(Z)V

    return-void
.end method

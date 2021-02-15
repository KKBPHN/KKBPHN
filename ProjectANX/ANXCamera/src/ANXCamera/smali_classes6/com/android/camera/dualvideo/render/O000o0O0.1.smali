.class public final synthetic Lcom/android/camera/dualvideo/render/O000o0O0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

.field private final synthetic O0OOoOO:Lcom/android/gallery3d/ui/GLCanvas;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/render/RenderManager;Lcom/android/gallery3d/ui/GLCanvas;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/O000o0O0;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    iput-object p2, p0, Lcom/android/camera/dualvideo/render/O000o0O0;->O0OOoOO:Lcom/android/gallery3d/ui/GLCanvas;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/O000o0O0;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/O000o0O0;->O0OOoOO:Lcom/android/gallery3d/ui/GLCanvas;

    check-cast p1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-virtual {v0, p0, p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O000000o(Lcom/android/gallery3d/ui/GLCanvas;Lcom/android/camera/dualvideo/render/CameraItemInterface;)V

    return-void
.end method

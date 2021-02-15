.class public final synthetic Lcom/android/camera/dualvideo/render/O000Oo0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Landroid/media/ImageReader$OnImageAvailableListener;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/render/RenderManager;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/O000Oo0;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    return-void
.end method


# virtual methods
.method public final onImageAvailable(Landroid/media/ImageReader;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/O000Oo0;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    invoke-virtual {p0, p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O000000o(Landroid/media/ImageReader;)V

    return-void
.end method

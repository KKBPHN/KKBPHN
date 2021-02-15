.class public final synthetic Lcom/android/camera/dualvideo/render/O0000ooO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItemManager;

.field private final synthetic O0OOoOO:F

.field private final synthetic O0OOoOo:Ljava/util/List;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/render/CameraItemManager;FLjava/util/List;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/O0000ooO;->O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItemManager;

    iput p2, p0, Lcom/android/camera/dualvideo/render/O0000ooO;->O0OOoOO:F

    iput-object p3, p0, Lcom/android/camera/dualvideo/render/O0000ooO;->O0OOoOo:Ljava/util/List;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/O0000ooO;->O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItemManager;

    iget v1, p0, Lcom/android/camera/dualvideo/render/O0000ooO;->O0OOoOO:F

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/O0000ooO;->O0OOoOo:Ljava/util/List;

    check-cast p1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-virtual {v0, v1, p0, p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->O000000o(FLjava/util/List;Lcom/android/camera/dualvideo/render/CameraItemInterface;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/dualvideo/render/O000OoOo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

.field private final synthetic O0OOoOO:I

.field private final synthetic O0OOoOo:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/render/RenderManager;II)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/O000OoOo;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    iput p2, p0, Lcom/android/camera/dualvideo/render/O000OoOo;->O0OOoOO:I

    iput p3, p0, Lcom/android/camera/dualvideo/render/O000OoOo;->O0OOoOo:I

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/O000OoOo;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    iget v1, p0, Lcom/android/camera/dualvideo/render/O000OoOo;->O0OOoOO:I

    iget p0, p0, Lcom/android/camera/dualvideo/render/O000OoOo;->O0OOoOo:I

    check-cast p1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-virtual {v0, v1, p0, p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O000000o(IILcom/android/camera/dualvideo/render/CameraItemInterface;)Z

    move-result p0

    return p0
.end method

.class public final synthetic Lcom/android/camera/dualvideo/view/O00000oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;

.field private final synthetic O0OOoOO:F

.field private final synthetic O0OOoOo:F


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;FF)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/view/O00000oo;->O0OOoO0:Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;

    iput p2, p0, Lcom/android/camera/dualvideo/view/O00000oo;->O0OOoOO:F

    iput p3, p0, Lcom/android/camera/dualvideo/view/O00000oo;->O0OOoOo:F

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/view/O00000oo;->O0OOoO0:Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;

    iget v1, p0, Lcom/android/camera/dualvideo/view/O00000oo;->O0OOoOO:F

    iget p0, p0, Lcom/android/camera/dualvideo/view/O00000oo;->O0OOoOo:F

    check-cast p1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-virtual {v0, v1, p0, p1}, Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;->O000000o(FFLcom/android/camera/dualvideo/render/CameraItemInterface;)Z

    move-result p0

    return p0
.end method

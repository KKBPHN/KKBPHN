.class public final synthetic Lcom/android/camera/dualvideo/view/O0000OOo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/view/O0000OOo;->O0OOoO0:Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/view/O0000OOo;->O0OOoO0:Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;

    check-cast p1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-virtual {p0, p1}, Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;->O000000o(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z

    move-result p0

    return p0
.end method

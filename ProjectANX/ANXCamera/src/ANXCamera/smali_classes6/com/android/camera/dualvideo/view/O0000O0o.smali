.class public final synthetic Lcom/android/camera/dualvideo/view/O0000O0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Function;


# instance fields
.field private final synthetic O0OOoO0:F

.field private final synthetic O0OOoOO:F


# direct methods
.method public synthetic constructor <init>(FF)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/android/camera/dualvideo/view/O0000O0o;->O0OOoO0:F

    iput p2, p0, Lcom/android/camera/dualvideo/view/O0000O0o;->O0OOoOO:F

    return-void
.end method


# virtual methods
.method public final apply(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    iget v0, p0, Lcom/android/camera/dualvideo/view/O0000O0o;->O0OOoO0:F

    iget p0, p0, Lcom/android/camera/dualvideo/view/O0000O0o;->O0OOoOO:F

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$DualVideoRenderProtocol;

    invoke-static {v0, p0, p1}, Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;->O000000o(FFLcom/android/camera/protocol/ModeProtocol$DualVideoRenderProtocol;)Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    return-object p0
.end method

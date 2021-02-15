.class public final synthetic Lcom/android/camera/dualvideo/view/O0000OoO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:I

.field private final synthetic O0OOoOO:Landroid/graphics/Rect;


# direct methods
.method public synthetic constructor <init>(ILandroid/graphics/Rect;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/android/camera/dualvideo/view/O0000OoO;->O0OOoO0:I

    iput-object p2, p0, Lcom/android/camera/dualvideo/view/O0000OoO;->O0OOoOO:Landroid/graphics/Rect;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 1

    iget v0, p0, Lcom/android/camera/dualvideo/view/O0000OoO;->O0OOoO0:I

    iget-object p0, p0, Lcom/android/camera/dualvideo/view/O0000OoO;->O0OOoOO:Landroid/graphics/Rect;

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$DualVideoRenderProtocol;

    invoke-static {v0, p0, p1}, Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;->O000000o(ILandroid/graphics/Rect;Lcom/android/camera/protocol/ModeProtocol$DualVideoRenderProtocol;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/dualvideo/view/O0000o0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Function;


# instance fields
.field private final synthetic O0OOoO0:I


# direct methods
.method public synthetic constructor <init>(I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/android/camera/dualvideo/view/O0000o0;->O0OOoO0:I

    return-void
.end method


# virtual methods
.method public final apply(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/view/O0000o0;->O0OOoO0:I

    check-cast p1, Ljava/util/ArrayList;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/view/TouchEventView$TouchHelper;->O000000o(ILjava/util/ArrayList;)Lcom/android/camera/dualvideo/render/CameraItemInterface;

    move-result-object p0

    return-object p0
.end method

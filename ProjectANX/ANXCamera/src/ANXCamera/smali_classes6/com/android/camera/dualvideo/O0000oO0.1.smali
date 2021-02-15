.class public final synthetic Lcom/android/camera/dualvideo/O0000oO0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

.field private final synthetic O0OOoOO:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/DualVideoModuleBase;I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O0000oO0;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

    iput p2, p0, Lcom/android/camera/dualvideo/O0000oO0;->O0OOoOO:I

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/dualvideo/O0000oO0;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

    iget p0, p0, Lcom/android/camera/dualvideo/O0000oO0;->O0OOoOO:I

    check-cast p1, Lcom/android/camera2/Camera2Proxy;

    invoke-virtual {v0, p0, p1}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->O000000o(ILcom/android/camera2/Camera2Proxy;)V

    return-void
.end method

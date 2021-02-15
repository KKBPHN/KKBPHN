.class public final synthetic Lcom/android/camera/dualvideo/O0000OoO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lcom/android/camera2/Camera2Proxy$FocusCallback;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/DualVideoModuleBase;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O0000OoO;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

    return-void
.end method


# virtual methods
.method public final onFocusStateChanged(Lcom/android/camera/module/loader/camera2/FocusTask;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/O0000OoO;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

    invoke-virtual {p0, p1}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->O000000o(Lcom/android/camera/module/loader/camera2/FocusTask;)V

    return-void
.end method

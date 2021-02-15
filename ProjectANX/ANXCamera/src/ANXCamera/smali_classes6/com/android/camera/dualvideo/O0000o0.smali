.class public final synthetic Lcom/android/camera/dualvideo/O0000o0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera2/Camera2Proxy;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera2/Camera2Proxy;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O0000o0;->O0OOoO0:Lcom/android/camera2/Camera2Proxy;

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/O0000o0;->O0OOoO0:Lcom/android/camera2/Camera2Proxy;

    check-cast p1, Lcom/android/camera/dualvideo/util/UserSelectData;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->O000000o(Lcom/android/camera2/Camera2Proxy;Lcom/android/camera/dualvideo/util/UserSelectData;)Z

    move-result p0

    return p0
.end method

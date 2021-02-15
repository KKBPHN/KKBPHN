.class public final synthetic Lcom/android/camera/dualvideo/render/O0000oOo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItem;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/render/CameraItem;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/O0000oOo;->O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItem;

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/O0000oOo;->O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItem;

    check-cast p1, Lcom/android/camera/dualvideo/util/UserSelectData;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->O000000o(Lcom/android/camera/dualvideo/render/CameraItem;Lcom/android/camera/dualvideo/util/UserSelectData;)Z

    move-result p0

    return p0
.end method

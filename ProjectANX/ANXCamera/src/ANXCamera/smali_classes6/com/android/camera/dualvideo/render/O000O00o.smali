.class public final synthetic Lcom/android/camera/dualvideo/render/O000O00o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItemManager;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/O000O00o;->O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItemManager;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/O000O00o;->O0OOoO0:Lcom/android/camera/dualvideo/render/CameraItemManager;

    check-cast p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-virtual {p0, p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->O0000O0o(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)V

    return-void
.end method

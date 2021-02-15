.class public final synthetic Lcom/android/camera/dualvideo/util/O0000o00;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/render/LayoutType;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/render/LayoutType;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/util/O0000o00;->O0OOoO0:Lcom/android/camera/dualvideo/render/LayoutType;

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/O0000o00;->O0OOoO0:Lcom/android/camera/dualvideo/render/LayoutType;

    check-cast p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->O00000o0(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z

    move-result p0

    return p0
.end method

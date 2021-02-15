.class public final synthetic Lcom/android/camera/dualvideo/O0000oOO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Landroid/util/Range;


# direct methods
.method public synthetic constructor <init>(Landroid/util/Range;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O0000oOO;->O0OOoO0:Landroid/util/Range;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/O0000oOO;->O0OOoO0:Landroid/util/Range;

    check-cast p1, Lcom/android/camera2/Camera2Proxy;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->O000000o(Landroid/util/Range;Lcom/android/camera2/Camera2Proxy;)V

    return-void
.end method

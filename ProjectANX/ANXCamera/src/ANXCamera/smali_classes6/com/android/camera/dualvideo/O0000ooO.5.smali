.class public final synthetic Lcom/android/camera/dualvideo/O0000ooO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

.field private final synthetic O0OOoOO:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/DualVideoModuleBase;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O0000ooO;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

    iput-boolean p2, p0, Lcom/android/camera/dualvideo/O0000ooO;->O0OOoOO:Z

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/dualvideo/O0000ooO;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

    iget-boolean p0, p0, Lcom/android/camera/dualvideo/O0000ooO;->O0OOoOO:Z

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;

    invoke-virtual {v0, p0, p1}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->O000000o(ZLcom/android/camera/protocol/ModeProtocol$ActionProcessing;)V

    return-void
.end method

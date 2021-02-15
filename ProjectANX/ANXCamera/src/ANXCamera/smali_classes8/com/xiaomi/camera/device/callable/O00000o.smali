.class public final synthetic Lcom/xiaomi/camera/device/callable/O00000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/xiaomi/camera/device/callable/OpenCameraCallable$1;

.field private final synthetic O0OOoOO:Ljava/lang/String;

.field private final synthetic O0OOoOo:I


# direct methods
.method public synthetic constructor <init>(Lcom/xiaomi/camera/device/callable/OpenCameraCallable$1;Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/xiaomi/camera/device/callable/O00000o;->O0OOoO0:Lcom/xiaomi/camera/device/callable/OpenCameraCallable$1;

    iput-object p2, p0, Lcom/xiaomi/camera/device/callable/O00000o;->O0OOoOO:Ljava/lang/String;

    iput p3, p0, Lcom/xiaomi/camera/device/callable/O00000o;->O0OOoOo:I

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-object v0, p0, Lcom/xiaomi/camera/device/callable/O00000o;->O0OOoO0:Lcom/xiaomi/camera/device/callable/OpenCameraCallable$1;

    iget-object v1, p0, Lcom/xiaomi/camera/device/callable/O00000o;->O0OOoOO:Ljava/lang/String;

    iget p0, p0, Lcom/xiaomi/camera/device/callable/O00000o;->O0OOoOo:I

    invoke-virtual {v0, v1, p0}, Lcom/xiaomi/camera/device/callable/OpenCameraCallable$1;->O000000o(Ljava/lang/String;I)V

    return-void
.end method

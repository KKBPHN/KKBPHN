.class public final synthetic Lcom/android/camera/module/O0000oOO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;

.field private final synthetic O0OOoOO:Landroid/graphics/Bitmap;

.field private final synthetic O0OOoOo:[F

.field private final synthetic O0OOoo0:Landroid/util/Size;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;Landroid/graphics/Bitmap;[FLandroid/util/Size;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O0000oOO;->O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;

    iput-object p2, p0, Lcom/android/camera/module/O0000oOO;->O0OOoOO:Landroid/graphics/Bitmap;

    iput-object p3, p0, Lcom/android/camera/module/O0000oOO;->O0OOoOo:[F

    iput-object p4, p0, Lcom/android/camera/module/O0000oOO;->O0OOoo0:Landroid/util/Size;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/module/O0000oOO;->O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;

    iget-object v1, p0, Lcom/android/camera/module/O0000oOO;->O0OOoOO:Landroid/graphics/Bitmap;

    iget-object v2, p0, Lcom/android/camera/module/O0000oOO;->O0OOoOo:[F

    iget-object p0, p0, Lcom/android/camera/module/O0000oOO;->O0OOoo0:Landroid/util/Size;

    invoke-static {v0, v1, v2, p0}, Lcom/android/camera/module/Camera2Module;->O000000o(Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;Landroid/graphics/Bitmap;[FLandroid/util/Size;)V

    return-void
.end method

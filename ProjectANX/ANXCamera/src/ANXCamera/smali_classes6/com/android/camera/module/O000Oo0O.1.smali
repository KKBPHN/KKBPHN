.class public final synthetic Lcom/android/camera/module/O000Oo0O;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

.field private final synthetic O0OOoOO:Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/DollyZoomModule;Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000Oo0O;->O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

    iput-object p2, p0, Lcom/android/camera/module/O000Oo0O;->O0OOoOO:Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/module/O000Oo0O;->O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

    iget-object p0, p0, Lcom/android/camera/module/O000Oo0O;->O0OOoOO:Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;

    invoke-virtual {v0, p0}, Lcom/android/camera/module/DollyZoomModule;->O000000o(Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;)V

    return-void
.end method

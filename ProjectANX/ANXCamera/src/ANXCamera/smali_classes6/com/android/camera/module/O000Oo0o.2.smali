.class public final synthetic Lcom/android/camera/module/O000Oo0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000Oo0o;->O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/module/O000Oo0o;->O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;

    invoke-static {p0}, Lcom/android/camera/module/DollyZoomModule;->O00000Oo(Lcom/android/camera/protocol/ModeProtocol$DollyZoomProcess;)V

    return-void
.end method

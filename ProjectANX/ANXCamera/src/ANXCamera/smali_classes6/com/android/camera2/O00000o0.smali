.class public final synthetic Lcom/android/camera2/O00000o0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera2/Camera2Proxy$ScreenLightCallback;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera2/Camera2Proxy$ScreenLightCallback;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera2/O00000o0;->O0OOoO0:Lcom/android/camera2/Camera2Proxy$ScreenLightCallback;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera2/O00000o0;->O0OOoO0:Lcom/android/camera2/Camera2Proxy$ScreenLightCallback;

    invoke-static {p0}, Lcom/android/camera2/MiCamera2;->O000000o(Lcom/android/camera2/Camera2Proxy$ScreenLightCallback;)V

    return-void
.end method

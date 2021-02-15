.class public final synthetic Lcom/android/camera/module/O000OOoo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

.field private final synthetic O0OOoOO:Lcom/xiaomi/dollyzoomprocess/MediaEffectCamera;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/DollyZoomModule;Lcom/xiaomi/dollyzoomprocess/MediaEffectCamera;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000OOoo;->O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

    iput-object p2, p0, Lcom/android/camera/module/O000OOoo;->O0OOoOO:Lcom/xiaomi/dollyzoomprocess/MediaEffectCamera;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/module/O000OOoo;->O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

    iget-object p0, p0, Lcom/android/camera/module/O000OOoo;->O0OOoOO:Lcom/xiaomi/dollyzoomprocess/MediaEffectCamera;

    invoke-virtual {v0, p0}, Lcom/android/camera/module/DollyZoomModule;->O000000o(Lcom/xiaomi/dollyzoomprocess/MediaEffectCamera;)V

    return-void
.end method

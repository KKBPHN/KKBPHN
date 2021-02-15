.class public final synthetic Lcom/android/camera/O00000oO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lcom/google/lens/sdk/LensApi$LensLaunchStatusCallback;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/Camera;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/Camera;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/O00000oO;->O0OOoO0:Lcom/android/camera/Camera;

    return-void
.end method


# virtual methods
.method public final onLaunchStatusFetched(I)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/O00000oO;->O0OOoO0:Lcom/android/camera/Camera;

    invoke-virtual {p0, p1}, Lcom/android/camera/Camera;->O00000oO(I)V

    return-void
.end method

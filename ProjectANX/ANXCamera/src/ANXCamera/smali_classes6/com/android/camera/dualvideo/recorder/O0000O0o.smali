.class public final synthetic Lcom/android/camera/dualvideo/recorder/O0000O0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/ObservableOnSubscribe;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;

.field private final synthetic O0OOoOO:Lcom/android/camera/dualvideo/recorder/MiRecorder;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;Lcom/android/camera/dualvideo/recorder/MiRecorder;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/recorder/O0000O0o;->O0OOoO0:Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;

    iput-object p2, p0, Lcom/android/camera/dualvideo/recorder/O0000O0o;->O0OOoOO:Lcom/android/camera/dualvideo/recorder/MiRecorder;

    return-void
.end method


# virtual methods
.method public final subscribe(Lio/reactivex/ObservableEmitter;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/dualvideo/recorder/O0000O0o;->O0OOoO0:Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;

    iget-object p0, p0, Lcom/android/camera/dualvideo/recorder/O0000O0o;->O0OOoOO:Lcom/android/camera/dualvideo/recorder/MiRecorder;

    invoke-virtual {v0, p0, p1}, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->O000000o(Lcom/android/camera/dualvideo/recorder/MiRecorder;Lio/reactivex/ObservableEmitter;)V

    return-void
.end method

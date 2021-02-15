.class public Lcom/android/camera/multi/SplitUpdateListenerAdapter;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/google/android/play/core/splitinstall/SplitInstallStateUpdatedListener;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onStateUpdate(Lcom/google/android/play/core/splitinstall/SplitInstallSessionState;)V
    .locals 0

    invoke-virtual {p1}, Lcom/google/android/play/core/splitinstall/SplitInstallSessionState;->status()I

    move-result p0

    const-string p1, "SplitTest:"

    packed-switch p0, :pswitch_data_0

    goto :goto_1

    :pswitch_0
    const-string p0, "FAILED"

    goto :goto_0

    :pswitch_1
    const-string p0, "INSTALLED"

    goto :goto_0

    :pswitch_2
    const-string p0, "INSTALLING"

    goto :goto_0

    :pswitch_3
    const-string p0, "DOWNLOADED"

    goto :goto_0

    :pswitch_4
    const-string p0, "DOWNLOADING"

    :goto_0
    invoke-static {p1, p0}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    :goto_1
    :pswitch_5
    return-void

    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
        :pswitch_5
        :pswitch_5
        :pswitch_5
    .end packed-switch
.end method

.method public bridge synthetic onStateUpdate(Ljava/lang/Object;)V
    .locals 0

    check-cast p1, Lcom/google/android/play/core/splitinstall/SplitInstallSessionState;

    invoke-virtual {p0, p1}, Lcom/android/camera/multi/SplitUpdateListenerAdapter;->onStateUpdate(Lcom/google/android/play/core/splitinstall/SplitInstallSessionState;)V

    return-void
.end method

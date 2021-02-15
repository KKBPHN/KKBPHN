.class Lcom/android/camera/fragment/mode/FragmentMoreModeEdit$2;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field final synthetic this$0:Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;


# direct methods
.method constructor <init>(Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeEdit$2;->this$0:Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public run()V
    .locals 1

    const-string p0, "MoreModeEdit"

    const-string v0, "showExitConfirm onClick negative"

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    const-string p0, "value_edit_mode_click_exit_cancel"

    invoke-static {p0}, Lcom/android/camera/statistic/CameraStatUtils;->trackModeEditClick(Ljava/lang/String;)V

    return-void
.end method

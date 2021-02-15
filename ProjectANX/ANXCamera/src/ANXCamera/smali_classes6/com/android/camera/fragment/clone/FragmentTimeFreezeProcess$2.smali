.class Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess$2;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field final synthetic this$0:Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;


# direct methods
.method constructor <init>(Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess$2;->this$0:Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public run()V
    .locals 2

    iget-object p0, p0, Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess$2;->this$0:Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/fragment/clone/FragmentCloneProcess;->mCaptureHintPined:Z

    const/4 v1, -0x1

    invoke-virtual {p0, v1, v0}, Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;->updateCaptureMessage(IZ)V

    return-void
.end method

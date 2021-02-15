.class public final synthetic Lcom/android/camera/fragment/clone/O00000oO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;

.field private final synthetic O0OOoOO:I

.field private final synthetic O0OOoOo:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;IZ)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/clone/O00000oO;->O0OOoO0:Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;

    iput p2, p0, Lcom/android/camera/fragment/clone/O00000oO;->O0OOoOO:I

    iput-boolean p3, p0, Lcom/android/camera/fragment/clone/O00000oO;->O0OOoOo:Z

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/fragment/clone/O00000oO;->O0OOoO0:Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;

    iget v1, p0, Lcom/android/camera/fragment/clone/O00000oO;->O0OOoOO:I

    iget-boolean p0, p0, Lcom/android/camera/fragment/clone/O00000oO;->O0OOoOo:Z

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/fragment/clone/FragmentTimeFreezeProcess;->O00000Oo(IZ)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/fragment/bottom/action/O00000o0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/ui/AdjustAnimationView;

.field private final synthetic O0OOoOO:Lcom/android/camera/ui/AnimationView;

.field private final synthetic O0OOoOo:Landroid/view/View;

.field private final synthetic O0OOoo0:F

.field private final synthetic O0OOooO:F


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/ui/AdjustAnimationView;Lcom/android/camera/ui/AnimationView;Landroid/view/View;FF)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOoO0:Lcom/android/camera/ui/AdjustAnimationView;

    iput-object p2, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOoOO:Lcom/android/camera/ui/AnimationView;

    iput-object p3, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOoOo:Landroid/view/View;

    iput p4, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOoo0:F

    iput p5, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOooO:F

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 4

    iget-object v0, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOoO0:Lcom/android/camera/ui/AdjustAnimationView;

    iget-object v1, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOoOO:Lcom/android/camera/ui/AnimationView;

    iget-object v2, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOoOo:Landroid/view/View;

    iget v3, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOoo0:F

    iget p0, p0, Lcom/android/camera/fragment/bottom/action/O00000o0;->O0OOooO:F

    invoke-static {v0, v1, v2, v3, p0}, Lcom/android/camera/fragment/bottom/action/FragmentBottomAction;->O000000o(Lcom/android/camera/ui/AdjustAnimationView;Lcom/android/camera/ui/AnimationView;Landroid/view/View;FF)V

    return-void
.end method

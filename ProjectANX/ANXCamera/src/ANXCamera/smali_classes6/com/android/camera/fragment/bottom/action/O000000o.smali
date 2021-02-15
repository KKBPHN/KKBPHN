.class public final synthetic Lcom/android/camera/fragment/bottom/action/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Landroid/animation/ValueAnimator$AnimatorUpdateListener;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/ui/AnimationView;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/ui/AnimationView;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/bottom/action/O000000o;->O0OOoO0:Lcom/android/camera/ui/AnimationView;

    return-void
.end method


# virtual methods
.method public final onAnimationUpdate(Landroid/animation/ValueAnimator;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/fragment/bottom/action/O000000o;->O0OOoO0:Lcom/android/camera/ui/AnimationView;

    invoke-static {p0, p1}, Lcom/android/camera/fragment/bottom/action/FragmentBottomAction;->O000000o(Lcom/android/camera/ui/AnimationView;Landroid/animation/ValueAnimator;)V

    return-void
.end method

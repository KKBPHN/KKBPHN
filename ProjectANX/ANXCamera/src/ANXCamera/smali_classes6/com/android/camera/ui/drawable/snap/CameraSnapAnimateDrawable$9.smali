.class Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable$9;
.super Landroid/animation/AnimatorListenerAdapter;
.source ""


# instance fields
.field final synthetic this$0:Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable;

.field final synthetic val$animationConfig:Lcom/android/camera/fragment/bottom/BottomAnimationConfig;


# direct methods
.method constructor <init>(Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable;Lcom/android/camera/fragment/bottom/BottomAnimationConfig;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable$9;->this$0:Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable;

    iput-object p2, p0, Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable$9;->val$animationConfig:Lcom/android/camera/fragment/bottom/BottomAnimationConfig;

    invoke-direct {p0}, Landroid/animation/AnimatorListenerAdapter;-><init>()V

    return-void
.end method


# virtual methods
.method public onAnimationEnd(Landroid/animation/Animator;)V
    .locals 1

    invoke-super {p0, p1}, Landroid/animation/AnimatorListenerAdapter;->onAnimationEnd(Landroid/animation/Animator;)V

    iget-object p1, p0, Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable$9;->this$0:Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable;

    const/4 v0, 0x0

    invoke-static {p1, v0}, Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable;->access$1002(Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable;Landroid/animation/ValueAnimator;)Landroid/animation/ValueAnimator;

    iget-object p1, p0, Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable$9;->this$0:Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable;

    iget-object p0, p0, Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable$9;->val$animationConfig:Lcom/android/camera/fragment/bottom/BottomAnimationConfig;

    invoke-virtual {p1, p0}, Lcom/android/camera/ui/drawable/snap/CameraSnapAnimateDrawable;->stopRecord(Lcom/android/camera/fragment/bottom/BottomAnimationConfig;)V

    return-void
.end method

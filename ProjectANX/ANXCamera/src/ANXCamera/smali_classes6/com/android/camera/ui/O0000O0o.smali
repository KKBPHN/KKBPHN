.class public final synthetic Lcom/android/camera/ui/O0000O0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Landroid/animation/ValueAnimator$AnimatorUpdateListener;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/ui/MutiStateButton;

.field private final synthetic O0OOoOO:F

.field private final synthetic O0OOoOo:F

.field private final synthetic O0OOoo0:F

.field private final synthetic O0OOooO:F


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/ui/MutiStateButton;FFFF)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/ui/O0000O0o;->O0OOoO0:Lcom/android/camera/ui/MutiStateButton;

    iput p2, p0, Lcom/android/camera/ui/O0000O0o;->O0OOoOO:F

    iput p3, p0, Lcom/android/camera/ui/O0000O0o;->O0OOoOo:F

    iput p4, p0, Lcom/android/camera/ui/O0000O0o;->O0OOoo0:F

    iput p5, p0, Lcom/android/camera/ui/O0000O0o;->O0OOooO:F

    return-void
.end method


# virtual methods
.method public final onAnimationUpdate(Landroid/animation/ValueAnimator;)V
    .locals 6

    iget-object v0, p0, Lcom/android/camera/ui/O0000O0o;->O0OOoO0:Lcom/android/camera/ui/MutiStateButton;

    iget v1, p0, Lcom/android/camera/ui/O0000O0o;->O0OOoOO:F

    iget v2, p0, Lcom/android/camera/ui/O0000O0o;->O0OOoOo:F

    iget v3, p0, Lcom/android/camera/ui/O0000O0o;->O0OOoo0:F

    iget v4, p0, Lcom/android/camera/ui/O0000O0o;->O0OOooO:F

    move-object v5, p1

    invoke-virtual/range {v0 .. v5}, Lcom/android/camera/ui/MutiStateButton;->O000000o(FFFFLandroid/animation/ValueAnimator;)V

    return-void
.end method

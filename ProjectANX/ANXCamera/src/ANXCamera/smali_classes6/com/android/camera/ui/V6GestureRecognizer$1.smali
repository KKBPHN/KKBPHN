.class Lcom/android/camera/ui/V6GestureRecognizer$1;
.super Lcom/android/camera/ui/zoom/ScaleGestureDetector;
.source ""


# instance fields
.field final synthetic this$0:Lcom/android/camera/ui/V6GestureRecognizer;

.field final synthetic val$camera:Lcom/android/camera/Camera;


# direct methods
.method constructor <init>(Lcom/android/camera/ui/V6GestureRecognizer;Landroid/content/Context;Lcom/android/camera/ui/zoom/ScaleGestureDetector$OnScaleGestureListener;Lcom/android/camera/Camera;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/ui/V6GestureRecognizer$1;->this$0:Lcom/android/camera/ui/V6GestureRecognizer;

    iput-object p4, p0, Lcom/android/camera/ui/V6GestureRecognizer$1;->val$camera:Lcom/android/camera/Camera;

    invoke-direct {p0, p2, p3}, Lcom/android/camera/ui/zoom/ScaleGestureDetector;-><init>(Landroid/content/Context;Lcom/android/camera/ui/zoom/ScaleGestureDetector$OnScaleGestureListener;)V

    return-void
.end method


# virtual methods
.method public getScaledMinimumScalingSpan()I
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo0oo()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, Lcom/android/camera/ui/V6GestureRecognizer$1;->val$camera:Lcom/android/camera/Camera;

    invoke-static {p0}, Lcom/android/camera/lib/compatibility/util/CompatibilityUtils;->getScaledMinimumScalingSpan(Landroid/content/Context;)I

    move-result p0

    return p0
.end method

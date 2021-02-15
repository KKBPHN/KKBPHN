.class Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess$3;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Landroid/view/View$OnClickListener;


# instance fields
.field final synthetic this$0:Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess;


# direct methods
.method constructor <init>(Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess$3;->this$0:Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 0

    const-string p1, "value_dolly_zoom_click_play_cancel"

    invoke-static {p1}, Lcom/android/camera/statistic/CameraStatUtils;->trackDollyZoomClick(Ljava/lang/String;)V

    iget-object p0, p0, Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess$3;->this$0:Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess;

    invoke-static {p0}, Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess;->access$200(Lcom/android/camera/fragment/dollyZoom/FragmentDollyZoomProcess;)Z

    return-void
.end method

.class Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Landroid/content/DialogInterface$OnDismissListener;


# instance fields
.field final synthetic this$0:Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;

.field final synthetic val$index:I

.field final synthetic val$item:Lcom/android/camera/aiwatermark/data/WatermarkItem;

.field final synthetic val$key:Ljava/lang/String;


# direct methods
.method constructor <init>(Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;Ljava/lang/String;ILcom/android/camera/aiwatermark/data/WatermarkItem;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->this$0:Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;

    iput-object p2, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->val$key:Ljava/lang/String;

    iput p3, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->val$index:I

    iput-object p4, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->val$item:Lcom/android/camera/aiwatermark/data/WatermarkItem;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onDismiss(Landroid/content/DialogInterface;)V
    .locals 3

    invoke-static {}, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;->access$000()Ljava/lang/String;

    move-result-object p1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "showCTADialog onDismiss mAllowLocationAccess="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v1, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->this$0:Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;

    invoke-static {v1}, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;->access$100(Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;)Z

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {p1, v0}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    iget-object p1, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->this$0:Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;

    invoke-static {p1}, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;->access$100(Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;)Z

    move-result p1

    if-eqz p1, :cond_0

    iget-object p1, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->this$0:Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;

    iget-object v0, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->val$key:Ljava/lang/String;

    iget v1, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->val$index:I

    iget-object v2, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->val$item:Lcom/android/camera/aiwatermark/data/WatermarkItem;

    invoke-static {p1, v0, v1, v2}, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;->access$200(Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;Ljava/lang/String;ILcom/android/camera/aiwatermark/data/WatermarkItem;)V

    goto :goto_0

    :cond_0
    const/4 p1, 0x0

    invoke-static {p1}, Lcom/android/camera/CameraSettings;->updateRecordLocationPreference(Z)V

    :goto_0
    iget-object p1, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->this$0:Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;

    invoke-static {p1}, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;->access$300(Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;)Landroid/app/AlertDialog;

    move-result-object p1

    const/4 v0, 0x0

    invoke-virtual {p1, v0}, Landroid/app/AlertDialog;->setOnDismissListener(Landroid/content/DialogInterface$OnDismissListener;)V

    iget-object p0, p0, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter$3;->this$0:Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;

    invoke-static {p0, v0}, Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;->access$302(Lcom/android/camera/fragment/aiwatermark/adapter/WatermarkAdapter;Landroid/app/AlertDialog;)Landroid/app/AlertDialog;

    return-void
.end method

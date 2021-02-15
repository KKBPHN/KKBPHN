.class Lcom/android/camera/module/Camera2Module$23;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field final synthetic this$0:Lcom/android/camera/module/Camera2Module;


# direct methods
.method constructor <init>(Lcom/android/camera/module/Camera2Module;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/module/Camera2Module$23;->this$0:Lcom/android/camera/module/Camera2Module;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public run()V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/module/Camera2Module$23;->this$0:Lcom/android/camera/module/Camera2Module;

    const/4 v1, 0x1

    const/4 v2, 0x0

    invoke-static {v0, v2, v1}, Lcom/android/camera/module/Camera2Module;->access$5800(Lcom/android/camera/module/Camera2Module;IZ)V

    iget-object p0, p0, Lcom/android/camera/module/Camera2Module$23;->this$0:Lcom/android/camera/module/Camera2Module;

    invoke-static {p0, v1}, Lcom/android/camera/module/Camera2Module;->access$6002(Lcom/android/camera/module/Camera2Module;Z)Z

    return-void
.end method

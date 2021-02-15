.class public final synthetic Lcom/android/camera/module/O0000oOo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/Camera2Module;

.field private final synthetic O0OOoOO:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/Camera2Module;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O0000oOo;->O0OOoO0:Lcom/android/camera/module/Camera2Module;

    iput-boolean p2, p0, Lcom/android/camera/module/O0000oOo;->O0OOoOO:Z

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/module/O0000oOo;->O0OOoO0:Lcom/android/camera/module/Camera2Module;

    iget-boolean p0, p0, Lcom/android/camera/module/O0000oOo;->O0OOoOO:Z

    invoke-virtual {v0, p0}, Lcom/android/camera/module/Camera2Module;->O00oOooO(Z)V

    return-void
.end method

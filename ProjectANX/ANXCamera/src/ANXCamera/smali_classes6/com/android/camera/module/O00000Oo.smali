.class public final synthetic Lcom/android/camera/module/O00000Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/AmbilightModule;

.field private final synthetic O0OOoOO:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/AmbilightModule;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O00000Oo;->O0OOoO0:Lcom/android/camera/module/AmbilightModule;

    iput-boolean p2, p0, Lcom/android/camera/module/O00000Oo;->O0OOoOO:Z

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/module/O00000Oo;->O0OOoO0:Lcom/android/camera/module/AmbilightModule;

    iget-boolean p0, p0, Lcom/android/camera/module/O00000Oo;->O0OOoOO:Z

    invoke-virtual {v0, p0}, Lcom/android/camera/module/AmbilightModule;->O0000ooO(Z)V

    return-void
.end method

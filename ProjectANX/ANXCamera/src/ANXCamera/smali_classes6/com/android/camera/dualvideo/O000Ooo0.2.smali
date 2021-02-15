.class public final synthetic Lcom/android/camera/dualvideo/O000Ooo0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/DualVideoSelectModule;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/DualVideoSelectModule;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O000Ooo0;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoSelectModule;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/O000Ooo0;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoSelectModule;

    check-cast p1, Lcom/android/camera/dualvideo/util/UserSelectData;

    invoke-virtual {p0, p1}, Lcom/android/camera/dualvideo/DualVideoSelectModule;->O0000Oo(Lcom/android/camera/dualvideo/util/UserSelectData;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/dualvideo/recorder/O00000oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Landroid/util/SparseArray;


# direct methods
.method public synthetic constructor <init>(Landroid/util/SparseArray;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/recorder/O00000oo;->O0OOoO0:Landroid/util/SparseArray;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/recorder/O00000oo;->O0OOoO0:Landroid/util/SparseArray;

    check-cast p1, Lcom/android/camera/dualvideo/recorder/MiRecorder;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->O000000o(Landroid/util/SparseArray;Lcom/android/camera/dualvideo/recorder/MiRecorder;)V

    return-void
.end method

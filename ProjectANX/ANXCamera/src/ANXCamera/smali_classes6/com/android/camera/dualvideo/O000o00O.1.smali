.class public final synthetic Lcom/android/camera/dualvideo/O000o00O;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Landroid/view/ViewGroup;


# direct methods
.method public synthetic constructor <init>(Landroid/view/ViewGroup;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O000o00O;->O0OOoO0:Landroid/view/ViewGroup;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/O000o00O;->O0OOoO0:Landroid/view/ViewGroup;

    invoke-virtual {p0}, Landroid/view/ViewGroup;->removeAllViews()V

    return-void
.end method

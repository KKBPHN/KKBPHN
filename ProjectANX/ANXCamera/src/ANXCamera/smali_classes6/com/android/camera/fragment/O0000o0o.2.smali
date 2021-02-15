.class public final synthetic Lcom/android/camera/fragment/O0000o0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/aiwatermark/data/WatermarkItem;

.field private final synthetic O0OOoOO:Z

.field private final synthetic O0OOoOo:Landroid/view/View;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/aiwatermark/data/WatermarkItem;ZLandroid/view/View;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/O0000o0o;->O0OOoO0:Lcom/android/camera/aiwatermark/data/WatermarkItem;

    iput-boolean p2, p0, Lcom/android/camera/fragment/O0000o0o;->O0OOoOO:Z

    iput-object p3, p0, Lcom/android/camera/fragment/O0000o0o;->O0OOoOo:Landroid/view/View;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/fragment/O0000o0o;->O0OOoO0:Lcom/android/camera/aiwatermark/data/WatermarkItem;

    iget-boolean v1, p0, Lcom/android/camera/fragment/O0000o0o;->O0OOoOO:Z

    iget-object p0, p0, Lcom/android/camera/fragment/O0000o0o;->O0OOoOo:Landroid/view/View;

    invoke-static {v0, v1, p0}, Lcom/android/camera/fragment/FragmentMainContent;->O000000o(Lcom/android/camera/aiwatermark/data/WatermarkItem;ZLandroid/view/View;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/fragment/O0000o0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/fragment/FragmentMainContent;

.field private final synthetic O0OOoOO:Landroid/view/View;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/fragment/FragmentMainContent;Landroid/view/View;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/O0000o0;->O0OOoO0:Lcom/android/camera/fragment/FragmentMainContent;

    iput-object p2, p0, Lcom/android/camera/fragment/O0000o0;->O0OOoOO:Landroid/view/View;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/fragment/O0000o0;->O0OOoO0:Lcom/android/camera/fragment/FragmentMainContent;

    iget-object p0, p0, Lcom/android/camera/fragment/O0000o0;->O0OOoOO:Landroid/view/View;

    invoke-virtual {v0, p0}, Lcom/android/camera/fragment/FragmentMainContent;->O0000Oo0(Landroid/view/View;)V

    return-void
.end method

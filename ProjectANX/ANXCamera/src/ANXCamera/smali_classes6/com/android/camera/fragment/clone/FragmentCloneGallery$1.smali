.class Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Landroid/view/View$OnLayoutChangeListener;


# instance fields
.field final synthetic this$0:Lcom/android/camera/fragment/clone/FragmentCloneGallery;

.field final synthetic val$animation:Z

.field final synthetic val$toX:I


# direct methods
.method constructor <init>(Lcom/android/camera/fragment/clone/FragmentCloneGallery;ZI)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->this$0:Lcom/android/camera/fragment/clone/FragmentCloneGallery;

    iput-boolean p2, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->val$animation:Z

    iput p3, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->val$toX:I

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onLayoutChange(Landroid/view/View;IIIIIIII)V
    .locals 0

    iget-object p1, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->this$0:Lcom/android/camera/fragment/clone/FragmentCloneGallery;

    invoke-static {p1}, Lcom/android/camera/fragment/clone/FragmentCloneGallery;->access$000(Lcom/android/camera/fragment/clone/FragmentCloneGallery;)Landroid/widget/HorizontalScrollView;

    move-result-object p1

    invoke-virtual {p1, p0}, Landroid/widget/HorizontalScrollView;->removeOnLayoutChangeListener(Landroid/view/View$OnLayoutChangeListener;)V

    iget-boolean p1, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->val$animation:Z

    const/4 p2, 0x0

    if-eqz p1, :cond_0

    iget-object p1, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->this$0:Lcom/android/camera/fragment/clone/FragmentCloneGallery;

    invoke-static {p1}, Lcom/android/camera/fragment/clone/FragmentCloneGallery;->access$000(Lcom/android/camera/fragment/clone/FragmentCloneGallery;)Landroid/widget/HorizontalScrollView;

    move-result-object p1

    iget p0, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->val$toX:I

    invoke-virtual {p1, p0, p2}, Landroid/widget/HorizontalScrollView;->smoothScrollTo(II)V

    goto :goto_0

    :cond_0
    iget-object p1, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->this$0:Lcom/android/camera/fragment/clone/FragmentCloneGallery;

    invoke-static {p1}, Lcom/android/camera/fragment/clone/FragmentCloneGallery;->access$000(Lcom/android/camera/fragment/clone/FragmentCloneGallery;)Landroid/widget/HorizontalScrollView;

    move-result-object p1

    iget p0, p0, Lcom/android/camera/fragment/clone/FragmentCloneGallery$1;->val$toX:I

    invoke-virtual {p1, p0, p2}, Landroid/widget/HorizontalScrollView;->scrollTo(II)V

    :goto_0
    return-void
.end method

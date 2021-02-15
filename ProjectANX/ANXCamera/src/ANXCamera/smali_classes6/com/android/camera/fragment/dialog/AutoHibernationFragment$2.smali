.class Lcom/android/camera/fragment/dialog/AutoHibernationFragment$2;
.super Lmiuix/animation/listener/TransitionListener;
.source ""


# instance fields
.field final synthetic this$0:Lcom/android/camera/fragment/dialog/AutoHibernationFragment;

.field final synthetic val$view:Landroid/view/View;


# direct methods
.method constructor <init>(Lcom/android/camera/fragment/dialog/AutoHibernationFragment;Landroid/view/View;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/dialog/AutoHibernationFragment$2;->this$0:Lcom/android/camera/fragment/dialog/AutoHibernationFragment;

    iput-object p2, p0, Lcom/android/camera/fragment/dialog/AutoHibernationFragment$2;->val$view:Landroid/view/View;

    invoke-direct {p0}, Lmiuix/animation/listener/TransitionListener;-><init>()V

    return-void
.end method


# virtual methods
.method public onComplete(Ljava/lang/Object;)V
    .locals 0

    invoke-super {p0, p1}, Lmiuix/animation/listener/TransitionListener;->onComplete(Ljava/lang/Object;)V

    iget-object p0, p0, Lcom/android/camera/fragment/dialog/AutoHibernationFragment$2;->val$view:Landroid/view/View;

    invoke-static {p0}, Lcom/android/camera/animation/FolmeUtils;->clean(Landroid/view/View;)V

    return-void
.end method

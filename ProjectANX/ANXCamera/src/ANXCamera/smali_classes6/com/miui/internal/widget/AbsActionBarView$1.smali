.class Lcom/miui/internal/widget/AbsActionBarView$1;
.super Lmiui/animation/listener/TransitionListener;
.source ""


# instance fields
.field final synthetic this$0:Lcom/miui/internal/widget/AbsActionBarView;


# direct methods
.method constructor <init>(Lcom/miui/internal/widget/AbsActionBarView;)V
    .locals 0

    iput-object p1, p0, Lcom/miui/internal/widget/AbsActionBarView$1;->this$0:Lcom/miui/internal/widget/AbsActionBarView;

    invoke-direct {p0}, Lmiui/animation/listener/TransitionListener;-><init>()V

    return-void
.end method


# virtual methods
.method public onBegin(Ljava/lang/Object;)V
    .locals 0

    invoke-super {p0, p1}, Lmiui/animation/listener/TransitionListener;->onBegin(Ljava/lang/Object;)V

    iget-object p0, p0, Lcom/miui/internal/widget/AbsActionBarView$1;->this$0:Lcom/miui/internal/widget/AbsActionBarView;

    iget-object p0, p0, Lcom/miui/internal/widget/AbsActionBarView;->mTransitionListener:Lmiui/app/ActionBarTransitionListener;

    if-eqz p0, :cond_0

    invoke-interface {p0, p1}, Lmiui/app/ActionBarTransitionListener;->onTransitionBegin(Ljava/lang/Object;)V

    :cond_0
    return-void
.end method

.method public onComplete(Ljava/lang/Object;)V
    .locals 0

    invoke-super {p0, p1}, Lmiui/animation/listener/TransitionListener;->onComplete(Ljava/lang/Object;)V

    iget-object p0, p0, Lcom/miui/internal/widget/AbsActionBarView$1;->this$0:Lcom/miui/internal/widget/AbsActionBarView;

    iget-object p0, p0, Lcom/miui/internal/widget/AbsActionBarView;->mTransitionListener:Lmiui/app/ActionBarTransitionListener;

    if-eqz p0, :cond_0

    invoke-interface {p0, p1}, Lmiui/app/ActionBarTransitionListener;->onTransitionComplete(Ljava/lang/Object;)V

    :cond_0
    return-void
.end method

.method public onUpdate(Ljava/lang/Object;Ljava/util/Collection;)V
    .locals 0

    invoke-super {p0, p1, p2}, Lmiui/animation/listener/TransitionListener;->onUpdate(Ljava/lang/Object;Ljava/util/Collection;)V

    iget-object p0, p0, Lcom/miui/internal/widget/AbsActionBarView$1;->this$0:Lcom/miui/internal/widget/AbsActionBarView;

    iget-object p0, p0, Lcom/miui/internal/widget/AbsActionBarView;->mTransitionListener:Lmiui/app/ActionBarTransitionListener;

    if-eqz p0, :cond_0

    invoke-interface {p0, p1, p2}, Lmiui/app/ActionBarTransitionListener;->onTransitionUpdate(Ljava/lang/Object;Ljava/util/Collection;)V

    :cond_0
    return-void
.end method

.class public Lcom/android/camera/fragment/dialog/IDCardModeNewbieDialogFragment;
.super Lcom/android/camera/fragment/dialog/AiSceneNewbieDialogFragment;
.source ""


# static fields
.field public static final TAG:Ljava/lang/String; = "IDCardModeHint"


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/fragment/dialog/AiSceneNewbieDialogFragment;-><init>()V

    return-void
.end method


# virtual methods
.method public synthetic O00000o(Landroid/view/View;)V
    .locals 0

    const/4 p1, 0x5

    invoke-virtual {p0, p1}, Lcom/android/camera/fragment/dialog/IDCardModeNewbieDialogFragment;->onBackEvent(I)Z

    return-void
.end method

.method public onBackEvent(I)Z
    .locals 2

    const-string v0, "IDCardModeHint"

    const-string v1, "onBackEvent"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    invoke-super {p0, p1}, Lcom/android/camera/fragment/dialog/AiSceneNewbieDialogFragment;->onBackEvent(I)Z

    move-result p0

    return p0
.end method

.method public onCreateView(Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;
    .locals 2
    .param p2    # Landroid/view/ViewGroup;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param
    .param p3    # Landroid/os/Bundle;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    const/4 p3, 0x0

    const v0, 0x7f0c0059

    invoke-virtual {p1, v0, p2, p3}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object p1

    invoke-virtual {p0, p1}, Lcom/android/camera/fragment/dialog/BaseDialogFragment;->initViewOnTouchListener(Landroid/view/View;)V

    const p2, 0x7f090189

    invoke-virtual {p1, p2}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object v0

    invoke-virtual {p0, v0}, Lcom/android/camera/fragment/dialog/BaseDialogFragment;->adjustViewHeight(Landroid/view/View;)V

    invoke-static {p3}, Lcom/android/camera/Util;->getDisplayRect(I)Landroid/graphics/Rect;

    move-result-object p3

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v1, 0x7f0702bf

    invoke-virtual {v0, v1}, Landroid/content/res/Resources;->getDimension(I)F

    move-result v0

    const v1, 0x7f090186

    invoke-virtual {p1, v1}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object v1

    invoke-virtual {v1}, Landroid/view/View;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v1

    check-cast v1, Landroid/widget/LinearLayout$LayoutParams;

    iget p3, p3, Landroid/graphics/Rect;->top:I

    int-to-float p3, p3

    sub-float/2addr v0, p3

    float-to-int p3, v0

    iput p3, v1, Landroid/widget/LinearLayout$LayoutParams;->topMargin:I

    invoke-static {}, Lcom/android/camera/Util;->isAccessible()Z

    move-result p3

    if-eqz p3, :cond_0

    invoke-virtual {p1, p2}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object p2

    new-instance p3, Lcom/android/camera/fragment/dialog/O0000Oo;

    invoke-direct {p3, p0}, Lcom/android/camera/fragment/dialog/O0000Oo;-><init>(Lcom/android/camera/fragment/dialog/IDCardModeNewbieDialogFragment;)V

    invoke-virtual {p2, p3}, Landroid/view/View;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    :cond_0
    return-object p1
.end method

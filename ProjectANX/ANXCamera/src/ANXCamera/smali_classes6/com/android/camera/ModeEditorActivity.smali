.class public Lcom/android/camera/ModeEditorActivity;
.super Landroidx/fragment/app/FragmentActivity;
.source ""


# static fields
.field public static final FROM_WHERE:Ljava/lang/String; = "from_where"

.field private static final TAG:Ljava/lang/String; = "ModeEditor"


# instance fields
.field private mDisplayRotation:I

.field private mEditFragment:Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;

.field private mFromWhere:I

.field private mLocked:Z

.field private mMyOrientationListener:Lcom/android/camera/ModeEditorActivity$MyOrientationListener;

.field private mOrientation:I

.field private mOrientationCompensation:I

.field private mRootView:Landroid/view/View;

.field private mSupportOrientation:Z


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Landroidx/fragment/app/FragmentActivity;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/ModeEditorActivity;->mLocked:Z

    return-void
.end method

.method static synthetic access$000(Lcom/android/camera/ModeEditorActivity;)Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/ModeEditorActivity;->mSupportOrientation:Z

    return p0
.end method

.method static synthetic access$100(Lcom/android/camera/ModeEditorActivity;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/ModeEditorActivity;->mOrientation:I

    return p0
.end method

.method static synthetic access$102(Lcom/android/camera/ModeEditorActivity;I)I
    .locals 0

    iput p1, p0, Lcom/android/camera/ModeEditorActivity;->mOrientation:I

    return p1
.end method

.method static synthetic access$200(Lcom/android/camera/ModeEditorActivity;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/ModeEditorActivity;->mDisplayRotation:I

    return p0
.end method

.method static synthetic access$202(Lcom/android/camera/ModeEditorActivity;I)I
    .locals 0

    iput p1, p0, Lcom/android/camera/ModeEditorActivity;->mDisplayRotation:I

    return p1
.end method

.method static synthetic access$300(Lcom/android/camera/ModeEditorActivity;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/ModeEditorActivity;->mOrientationCompensation:I

    return p0
.end method

.method static synthetic access$302(Lcom/android/camera/ModeEditorActivity;I)I
    .locals 0

    iput p1, p0, Lcom/android/camera/ModeEditorActivity;->mOrientationCompensation:I

    return p1
.end method

.method static synthetic access$400(Lcom/android/camera/ModeEditorActivity;)Landroid/view/View;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ModeEditorActivity;->mRootView:Landroid/view/View;

    return-object p0
.end method


# virtual methods
.method public onBackPressed()V
    .locals 1

    iget-object p0, p0, Lcom/android/camera/ModeEditorActivity;->mEditFragment:Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;->onBackEvent(I)Z

    move-result p0

    if-eqz p0, :cond_0

    return-void

    :cond_0
    const-string p0, "ModeEditor"

    const-string v0, "onBackPressed"

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 2
    .param p1    # Landroid/os/Bundle;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-super {p0, p1}, Landroidx/fragment/app/FragmentActivity;->onCreate(Landroid/os/Bundle;)V

    invoke-virtual {p0}, Landroid/app/Activity;->getIntent()Landroid/content/Intent;

    move-result-object p1

    const-string v0, "from_where"

    const/16 v1, 0xa0

    invoke-virtual {p1, v0, v1}, Landroid/content/Intent;->getIntExtra(Ljava/lang/String;I)I

    move-result p1

    iput p1, p0, Lcom/android/camera/ModeEditorActivity;->mFromWhere:I

    invoke-virtual {p0}, Landroid/app/Activity;->getIntent()Landroid/content/Intent;

    move-result-object p1

    const-string v0, "StartActivityWhenLocked"

    const/4 v1, 0x0

    invoke-virtual {p1, v0, v1}, Landroid/content/Intent;->getBooleanExtra(Ljava/lang/String;Z)Z

    move-result p1

    iput-boolean p1, p0, Lcom/android/camera/ModeEditorActivity;->mLocked:Z

    iget-boolean p1, p0, Lcom/android/camera/ModeEditorActivity;->mLocked:Z

    if-eqz p1, :cond_0

    const/4 p1, 0x1

    invoke-virtual {p0, p1}, Landroid/app/Activity;->setShowWhenLocked(Z)V

    :cond_0
    const p1, 0x7f0c001d

    invoke-virtual {p0, p1}, Landroid/app/Activity;->setContentView(I)V

    const p1, 0x7f090244

    invoke-virtual {p0, p1}, Landroid/app/Activity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    iput-object v0, p0, Lcom/android/camera/ModeEditorActivity;->mRootView:Landroid/view/View;

    new-instance v0, Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;

    invoke-direct {v0}, Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;-><init>()V

    iput-object v0, p0, Lcom/android/camera/ModeEditorActivity;->mEditFragment:Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;

    invoke-virtual {p0}, Landroidx/fragment/app/FragmentActivity;->getSupportFragmentManager()Landroidx/fragment/app/FragmentManager;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/fragment/app/FragmentManager;->beginTransaction()Landroidx/fragment/app/FragmentTransaction;

    move-result-object v0

    iget-object v1, p0, Lcom/android/camera/ModeEditorActivity;->mEditFragment:Lcom/android/camera/fragment/mode/FragmentMoreModeEdit;

    invoke-virtual {v0, p1, v1}, Landroidx/fragment/app/FragmentTransaction;->add(ILandroidx/fragment/app/Fragment;)Landroidx/fragment/app/FragmentTransaction;

    move-result-object p1

    invoke-virtual {p1}, Landroidx/fragment/app/FragmentTransaction;->commit()I

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object p1

    invoke-static {}, Lcom/android/camera/Display;->getDisplayRatio()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, LO00000Oo/O00000oO/O000000o/O00000o;->O000OOoo(Ljava/lang/String;)Z

    move-result p1

    iput-boolean p1, p0, Lcom/android/camera/ModeEditorActivity;->mSupportOrientation:Z

    new-instance p1, Lcom/android/camera/ModeEditorActivity$MyOrientationListener;

    invoke-direct {p1, p0, p0}, Lcom/android/camera/ModeEditorActivity$MyOrientationListener;-><init>(Lcom/android/camera/ModeEditorActivity;Landroid/content/Context;)V

    iput-object p1, p0, Lcom/android/camera/ModeEditorActivity;->mMyOrientationListener:Lcom/android/camera/ModeEditorActivity$MyOrientationListener;

    return-void
.end method

.method protected onPause()V
    .locals 0

    invoke-super {p0}, Landroidx/fragment/app/FragmentActivity;->onPause()V

    iget-object p0, p0, Lcom/android/camera/ModeEditorActivity;->mMyOrientationListener:Lcom/android/camera/ModeEditorActivity$MyOrientationListener;

    invoke-virtual {p0}, Landroid/view/OrientationEventListener;->disable()V

    return-void
.end method

.method protected onResume()V
    .locals 0

    invoke-super {p0}, Landroidx/fragment/app/FragmentActivity;->onResume()V

    iget-object p0, p0, Lcom/android/camera/ModeEditorActivity;->mMyOrientationListener:Lcom/android/camera/ModeEditorActivity$MyOrientationListener;

    invoke-virtual {p0}, Landroid/view/OrientationEventListener;->enable()V

    return-void
.end method

.method protected onStop()V
    .locals 1

    invoke-super {p0}, Landroidx/fragment/app/FragmentActivity;->onStop()V

    iget-boolean v0, p0, Lcom/android/camera/ModeEditorActivity;->mLocked:Z

    if-eqz v0, :cond_0

    invoke-virtual {p0}, Landroid/app/Activity;->finish()V

    :cond_0
    return-void
.end method

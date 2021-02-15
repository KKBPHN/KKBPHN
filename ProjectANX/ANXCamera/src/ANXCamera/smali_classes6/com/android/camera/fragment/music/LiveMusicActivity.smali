.class public Lcom/android/camera/fragment/music/LiveMusicActivity;
.super Lmiui/app/Activity;
.source ""


# static fields
.field public static final LOCAL:I = 0x1

.field public static final ONLINE:I = 0x0

.field public static final TAG:Ljava/lang/String; = "LiveMusicActivity"


# instance fields
.field private mOldOriginVolumeStream:I


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lmiui/app/Activity;-><init>()V

    return-void
.end method

.method private init()V
    .locals 9

    invoke-virtual {p0}, Lmiui/app/Activity;->getActionBar()Lmiui/app/ActionBar;

    move-result-object v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    const v1, 0x7f10032c

    invoke-virtual {v0, v1}, Landroid/app/ActionBar;->setTitle(I)V

    invoke-virtual {p0}, Landroid/app/Activity;->getFragmentManager()Landroid/app/FragmentManager;

    move-result-object v1

    const/4 v2, 0x0

    invoke-virtual {v0, p0, v1, v2}, Lmiui/app/ActionBar;->setFragmentViewPagerMode(Landroid/content/Context;Landroid/app/FragmentManager;Z)V

    invoke-static {}, Lcom/android/camera/Util;->isGlobalVersion()Z

    move-result v1

    xor-int/lit8 v1, v1, 0x1

    if-eqz v1, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v2

    invoke-virtual {v2}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO0o0O()Z

    move-result v2

    if-nez v2, :cond_1

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemCloud()Lcom/android/camera/data/cloud/DataCloudItemFeature;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/data/cloud/DataCloudItemFeature;->supportTMMusic()Z

    move-result v1

    :cond_1
    new-instance v2, Ljava/util/ArrayList;

    invoke-direct {v2}, Ljava/util/ArrayList;-><init>()V

    if-eqz v1, :cond_2

    const v1, 0x7f100331

    invoke-virtual {p0, v1}, Landroid/app/Activity;->getString(I)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0}, Landroid/app/ActionBar;->newTab()Landroid/app/ActionBar$Tab;

    move-result-object v1

    invoke-virtual {v1, v4}, Landroid/app/ActionBar$Tab;->setText(Ljava/lang/CharSequence;)Landroid/app/ActionBar$Tab;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    invoke-virtual {p0}, Lmiui/app/Activity;->getActionBar()Lmiui/app/ActionBar;

    move-result-object v3

    const-class v6, Lcom/android/camera/fragment/music/FragmentLiveMusicOnline;

    const/4 v7, 0x0

    const/4 v8, 0x0

    invoke-virtual/range {v3 .. v8}, Lmiui/app/ActionBar;->addFragmentTab(Ljava/lang/String;Landroid/app/ActionBar$Tab;Ljava/lang/Class;Landroid/os/Bundle;Z)I

    :cond_2
    const v1, 0x7f100332

    invoke-virtual {p0, v1}, Landroid/app/Activity;->getString(I)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v0}, Landroid/app/ActionBar;->newTab()Landroid/app/ActionBar$Tab;

    move-result-object v0

    invoke-virtual {v0, v4}, Landroid/app/ActionBar$Tab;->setText(Ljava/lang/CharSequence;)Landroid/app/ActionBar$Tab;

    move-result-object v5

    invoke-virtual {v2, v5}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    invoke-virtual {p0}, Lmiui/app/Activity;->getActionBar()Lmiui/app/ActionBar;

    move-result-object v3

    const-class v6, Lcom/android/camera/fragment/music/FragmentLiveMusicLocal;

    const/4 v7, 0x0

    const/4 v8, 0x0

    invoke-virtual/range {v3 .. v8}, Lmiui/app/ActionBar;->addFragmentTab(Ljava/lang/String;Landroid/app/ActionBar$Tab;Ljava/lang/Class;Landroid/os/Bundle;Z)I

    invoke-virtual {p0}, Lmiui/app/Activity;->getActionBar()Lmiui/app/ActionBar;

    move-result-object v0

    new-instance v1, Lcom/android/camera/fragment/music/LiveMusicActivity$1;

    invoke-direct {v1, p0, v2}, Lcom/android/camera/fragment/music/LiveMusicActivity$1;-><init>(Lcom/android/camera/fragment/music/LiveMusicActivity;Ljava/util/ArrayList;)V

    invoke-virtual {v0, v1}, Lmiui/app/ActionBar;->addOnFragmentViewPagerChangeListener(Lmiui/app/ActionBar$FragmentViewPagerChangeListener;)V

    return-void
.end method


# virtual methods
.method protected onCreate(Landroid/os/Bundle;)V
    .locals 2

    invoke-super {p0, p1}, Lmiui/app/Activity;->onCreate(Landroid/os/Bundle;)V

    invoke-virtual {p0}, Landroid/app/Activity;->getVolumeControlStream()I

    move-result p1

    iput p1, p0, Lcom/android/camera/fragment/music/LiveMusicActivity;->mOldOriginVolumeStream:I

    const/4 p1, 0x3

    invoke-virtual {p0, p1}, Landroid/app/Activity;->setVolumeControlStream(I)V

    invoke-virtual {p0}, Landroid/app/Activity;->getIntent()Landroid/content/Intent;

    move-result-object p1

    const-string v0, "StartActivityWhenLocked"

    const/4 v1, 0x0

    invoke-virtual {p1, v0, v1}, Landroid/content/Intent;->getBooleanExtra(Ljava/lang/String;Z)Z

    move-result p1

    if-eqz p1, :cond_0

    const/4 p1, 0x1

    invoke-virtual {p0, p1}, Landroid/app/Activity;->setShowWhenLocked(Z)V

    :cond_0
    invoke-direct {p0}, Lcom/android/camera/fragment/music/LiveMusicActivity;->init()V

    return-void
.end method

.method protected onDestroy()V
    .locals 1

    invoke-super {p0}, Landroid/app/Activity;->onDestroy()V

    iget v0, p0, Lcom/android/camera/fragment/music/LiveMusicActivity;->mOldOriginVolumeStream:I

    invoke-virtual {p0, v0}, Landroid/app/Activity;->setVolumeControlStream(I)V

    return-void
.end method

.method protected onPause()V
    .locals 0

    invoke-super {p0}, Landroid/app/Activity;->onPause()V

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/data/data/global/DataItemGlobal;->resetTimeOut()V

    return-void
.end method

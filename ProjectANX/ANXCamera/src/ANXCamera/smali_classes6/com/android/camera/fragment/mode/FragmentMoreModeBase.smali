.class public abstract Lcom/android/camera/fragment/mode/FragmentMoreModeBase;
.super Lcom/android/camera/fragment/BaseFragment;
.source ""

# interfaces
.implements Lcom/android/camera/protocol/ModeProtocol$HandleBackTrace;
.implements Landroid/view/View$OnClickListener;
.implements Lcom/android/camera/fragment/mode/IMoreMode;


# static fields
.field private static final DOWN_POS_IDLE:I = -0x1

.field private static final DOWN_POS_NOT_FOUND:I = -0x2

.field private static final TAG:Ljava/lang/String; = "MoreModeBase"


# instance fields
.field protected mComponentModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

.field private mDownloadCancelDialog:Landroid/app/AlertDialog;

.field private mDownloadConfirmDialog:Landroid/app/AlertDialog;

.field private mDownloaderPosition:I

.field protected mDownloadingFeature:Ljava/lang/String;

.field protected mRootView:Landroid/view/View;

.field private mVMFeature:Lcom/android/camera/data/observeable/VMFeature;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lcom/android/camera/fragment/BaseFragment;-><init>()V

    const/4 v0, -0x1

    iput v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    return-void
.end method

.method static synthetic access$000(Lcom/android/camera/fragment/mode/FragmentMoreModeBase;Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->onDownloadAndInstallStart(Ljava/lang/String;)V

    return-void
.end method

.method static synthetic access$102(Lcom/android/camera/fragment/mode/FragmentMoreModeBase;Landroid/app/AlertDialog;)Landroid/app/AlertDialog;
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadConfirmDialog:Landroid/app/AlertDialog;

    return-object p1
.end method

.method static synthetic access$202(Lcom/android/camera/fragment/mode/FragmentMoreModeBase;Landroid/app/AlertDialog;)Landroid/app/AlertDialog;
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    return-object p1
.end method

.method private checkInstallState(I)Z
    .locals 3

    invoke-static {p1}, Lcom/android/camera/data/observeable/VMFeature;->getFeatureNameByLocalMode(I)Ljava/lang/String;

    move-result-object p1

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    const/4 v1, 0x1

    if-eqz v0, :cond_0

    return v1

    :cond_0
    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v2, 0x3a1

    invoke-virtual {v0, v2}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;

    invoke-interface {v0, p1}, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;->hasFeatureInstalled(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_1

    return v1

    :cond_1
    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->confirmDownload(Ljava/lang/String;)V

    const/4 p0, 0x0

    return p0
.end method

.method private confirmDownload(Ljava/lang/String;)V
    .locals 4

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "confirmDownload: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const-string v1, "MoreModeBase"

    invoke-static {v1, v0}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x3a1

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;

    move-result-object v1

    new-instance v2, Lcom/android/camera/fragment/mode/FragmentMoreModeBase$1;

    invoke-direct {v2, p0, p1}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase$1;-><init>(Lcom/android/camera/fragment/mode/FragmentMoreModeBase;Ljava/lang/String;)V

    const/4 v3, 0x1

    invoke-interface {v0, p1, v1, v3, v2}, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;->checkAndShowDownloadConfirmDialog(Ljava/lang/String;Landroid/content/Context;ZLjava/lang/Runnable;)Landroid/app/AlertDialog;

    move-result-object p1

    iput-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadConfirmDialog:Landroid/app/AlertDialog;

    iget-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadConfirmDialog:Landroid/app/AlertDialog;

    if-eqz p1, :cond_0

    new-instance v0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase$2;

    invoke-direct {v0, p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase$2;-><init>(Lcom/android/camera/fragment/mode/FragmentMoreModeBase;)V

    invoke-virtual {p1, v0}, Landroid/app/AlertDialog;->setOnDismissListener(Landroid/content/DialogInterface$OnDismissListener;)V

    :cond_0
    return-void
.end method

.method private createModeAdapter()Lcom/android/camera/fragment/mode/ModeAdapter;
    .locals 2

    new-instance v0, Lcom/android/camera/fragment/mode/ModeAdapter;

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;

    move-result-object v1

    invoke-direct {v0, v1, p0}, Lcom/android/camera/fragment/mode/ModeAdapter;-><init>(Landroid/content/Context;Lcom/android/camera/fragment/mode/FragmentMoreModeBase;)V

    return-object v0
.end method

.method private getDownloadPosition(I)I
    .locals 3

    const/4 v0, 0x0

    :goto_0
    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getComponentModuleList()Lcom/android/camera/data/data/global/ComponentModuleList;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/data/data/global/ComponentModuleList;->getMoreItems()Ljava/util/List;

    move-result-object v1

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_3

    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getComponentModuleList()Lcom/android/camera/data/data/global/ComponentModuleList;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/data/data/global/ComponentModuleList;->getMoreItems()Ljava/util/List;

    move-result-object v1

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/android/camera/data/data/ComponentDataItem;

    iget-object v1, v1, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {p1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_2

    invoke-interface {p0}, Lcom/android/camera/fragment/mode/IMoreMode;->getType()I

    move-result p1

    if-eqz p1, :cond_1

    invoke-interface {p0}, Lcom/android/camera/fragment/mode/IMoreMode;->getType()I

    move-result p1

    const/4 v1, 0x3

    if-ne p1, v1, :cond_0

    goto :goto_1

    :cond_0
    invoke-interface {p0}, Lcom/android/camera/fragment/mode/IMoreMode;->getType()I

    move-result p0

    const/4 p1, 0x1

    if-ne p0, p1, :cond_3

    goto :goto_2

    :cond_1
    :goto_1
    add-int/lit8 v0, v0, 0x1

    goto :goto_2

    :cond_2
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_3
    const/4 v0, -0x2

    :goto_2
    return v0
.end method

.method private notifyDownloadProgressChange(II)V
    .locals 2

    new-instance v0, Lcom/android/camera/fragment/mode/ModeAdapter$PlayLoad;

    const/4 v1, 0x1

    invoke-direct {v0, v1}, Lcom/android/camera/fragment/mode/ModeAdapter$PlayLoad;-><init>(I)V

    iput p2, v0, Lcom/android/camera/fragment/mode/ModeAdapter$PlayLoad;->progress:I

    iget p2, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    const/4 v1, -0x1

    if-ne p2, v1, :cond_0

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getDownloadPosition(I)I

    move-result p1

    iput p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string p2, "start down position "

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget p2, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    invoke-virtual {p1, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    const-string p2, "MoreModeBase"

    invoke-static {p2, p1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :cond_0
    iget p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    if-eq p1, v1, :cond_1

    const/4 p2, -0x2

    if-eq p1, p2, :cond_1

    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getModeAdapter()Lcom/android/camera/fragment/mode/ModeAdapter;

    move-result-object p1

    iget p0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    invoke-virtual {p1, p0, v0}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->notifyItemChanged(ILjava/lang/Object;)V

    :cond_1
    return-void
.end method

.method private onDownloadAndInstallStart(Ljava/lang/String;)V
    .locals 2

    const-string v0, "MoreModeBase"

    const-string v1, "onDownloadStart"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iput-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadingFeature:Ljava/lang/String;

    return-void
.end method

.method private onInstallFailed()V
    .locals 2

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadingFeature:Ljava/lang/String;

    iget-object v1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    if-eqz v1, :cond_0

    invoke-virtual {v1}, Landroid/app/AlertDialog;->dismiss()V

    iput-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    :cond_0
    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;

    move-result-object p0

    const v0, 0x7f100333

    const/4 v1, 0x0

    invoke-static {p0, v0, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;II)Landroid/widget/Toast;

    move-result-object p0

    invoke-virtual {p0}, Landroid/widget/Toast;->show()V

    return-void
.end method

.method private onInstallStateChanged(Ljava/util/HashMap;)V
    .locals 8

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->isAdded()Z

    move-result v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-virtual {p1}, Ljava/util/HashMap;->entrySet()Ljava/util/Set;

    move-result-object p1

    invoke-interface {p1}, Ljava/util/Set;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_1
    :goto_0
    :pswitch_0
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_5

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/Map$Entry;

    invoke-interface {v0}, Ljava/util/Map$Entry;->getKey()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    invoke-interface {v0}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    invoke-static {v1}, Lcom/android/camera/data/observeable/VMFeature;->getLocalModeByFeatureName(Ljava/lang/String;)I

    move-result v2

    invoke-static {v0}, Lcom/android/camera/data/observeable/VMFeature;->getScope(I)I

    move-result v3

    const/16 v4, 0x10

    const/4 v5, 0x0

    const-string v6, "MoreModeBase"

    const/4 v7, -0x1

    if-eq v3, v4, :cond_4

    const/16 v4, 0x100

    if-eq v3, v4, :cond_3

    const/16 v4, 0x1000

    if-eq v3, v4, :cond_2

    goto :goto_0

    :cond_2
    invoke-static {v0}, Lcom/android/camera/data/observeable/VMFeature;->getDownloadingProgress(I)I

    move-result v0

    invoke-direct {p0, v2, v0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->notifyDownloadProgressChange(II)V

    iget-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadingFeature:Ljava/lang/String;

    if-nez v0, :cond_1

    invoke-direct {p0, v1}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->onDownloadAndInstallStart(Ljava/lang/String;)V

    goto :goto_0

    :cond_3
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "onStateError: "

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v6, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-direct {p0, v2, v5}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->notifyDownloadProgressChange(II)V

    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getModeAdapter()Lcom/android/camera/fragment/mode/ModeAdapter;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->notifyDataSetChanged()V

    iput v7, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    invoke-direct {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->onInstallFailed()V

    goto :goto_0

    :cond_4
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "onStateChange = "

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v3, ", mode = "

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v6, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    packed-switch v0, :pswitch_data_0

    :pswitch_1
    goto/16 :goto_0

    :pswitch_2
    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getModeAdapter()Lcom/android/camera/fragment/mode/ModeAdapter;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->notifyDataSetChanged()V

    iput v7, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    const/4 v0, 0x1

    invoke-direct {p0, v2, v0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->onInstalledOK(IZ)V

    goto/16 :goto_0

    :pswitch_3
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadingFeature:Ljava/lang/String;

    invoke-direct {p0, v2, v5}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->notifyDownloadProgressChange(II)V

    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getModeAdapter()Lcom/android/camera/fragment/mode/ModeAdapter;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->notifyDataSetChanged()V

    iput v7, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    goto/16 :goto_0

    :pswitch_4
    iget v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    if-ne v0, v7, :cond_1

    invoke-direct {p0, v2}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getDownloadPosition(I)I

    move-result v0

    iput v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloaderPosition:I

    goto/16 :goto_0

    :cond_5
    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x11
        :pswitch_4
        :pswitch_3
        :pswitch_0
        :pswitch_1
        :pswitch_2
    .end packed-switch
.end method

.method private onInstalledOK(IZ)V
    .locals 2

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadingFeature:Ljava/lang/String;

    iget-object v1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    if-eqz v1, :cond_0

    invoke-virtual {v1}, Landroid/app/AlertDialog;->dismiss()V

    iput-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    :cond_0
    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->isResumed()Z

    move-result v0

    if-nez v0, :cond_1

    return-void

    :cond_1
    if-eqz p2, :cond_2

    return-void

    :cond_2
    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->hide()Z

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p2

    const/16 v0, 0xb3

    invoke-virtual {p2, v0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p2

    check-cast p2, Lcom/android/camera/protocol/ModeProtocol$ModeChangeController;

    if-eqz p2, :cond_3

    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getComponentModuleList()Lcom/android/camera/data/data/global/ComponentModuleList;

    move-result-object p0

    const/4 v0, 0x1

    invoke-virtual {p0, p1, v0}, Lcom/android/camera/data/data/global/ComponentModuleList;->geItemStringName(IZ)Ljava/lang/String;

    move-result-object p0

    const/4 v0, 0x0

    invoke-interface {p2, p1, p0, v0}, Lcom/android/camera/protocol/ModeProtocol$ModeChangeController;->changeModeByNewMode(ILjava/lang/String;I)V

    :cond_3
    return-void
.end method


# virtual methods
.method public synthetic O00000oo(Lcom/android/camera/data/observeable/RxData$DataWrap;)V
    .locals 0

    invoke-virtual {p1}, Lcom/android/camera/data/observeable/RxData$DataWrap;->get()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Ljava/util/HashMap;

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->onInstallStateChanged(Ljava/util/HashMap;)V

    return-void
.end method

.method public getComponentModuleList()Lcom/android/camera/data/data/global/ComponentModuleList;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mComponentModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    return-object p0
.end method

.method public getModeAdapter()Lcom/android/camera/fragment/mode/ModeAdapter;
    .locals 1

    iget-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mRootView:Landroid/view/View;

    invoke-interface {p0, v0}, Lcom/android/camera/fragment/mode/IMoreMode;->getModeList(Landroid/view/View;)Landroidx/recyclerview/widget/RecyclerView;

    move-result-object p0

    invoke-virtual {p0}, Landroidx/recyclerview/widget/RecyclerView;->getAdapter()Landroidx/recyclerview/widget/RecyclerView$Adapter;

    move-result-object p0

    check-cast p0, Lcom/android/camera/fragment/mode/ModeAdapter;

    return-object p0
.end method

.method protected abstract hide()Z
.end method

.method protected initView(Landroid/view/View;)V
    .locals 1

    iput-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mRootView:Landroid/view/View;

    invoke-interface {p0, p1}, Lcom/android/camera/fragment/mode/IMoreMode;->getModeList(Landroid/view/View;)Landroidx/recyclerview/widget/RecyclerView;

    move-result-object p1

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-interface {p0, v0}, Lcom/android/camera/fragment/mode/IMoreMode;->createLayoutManager(Landroid/content/Context;)Landroidx/recyclerview/widget/RecyclerView$LayoutManager;

    move-result-object v0

    invoke-virtual {p1, v0}, Landroidx/recyclerview/widget/RecyclerView;->setLayoutManager(Landroidx/recyclerview/widget/RecyclerView$LayoutManager;)V

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView;->getItemDecorationCount()I

    move-result v0

    if-nez v0, :cond_0

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-interface {p0, v0, p0}, Lcom/android/camera/fragment/mode/IMoreMode;->createModeItemDecoration(Landroid/content/Context;Lcom/android/camera/fragment/mode/IMoreMode;)Lcom/android/camera/fragment/mode/ModeItemDecoration;

    move-result-object v0

    invoke-virtual {p1, v0}, Landroidx/recyclerview/widget/RecyclerView;->addItemDecoration(Landroidx/recyclerview/widget/RecyclerView$ItemDecoration;)V

    :cond_0
    invoke-direct {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->createModeAdapter()Lcom/android/camera/fragment/mode/ModeAdapter;

    move-result-object v0

    iget p0, p0, Lcom/android/camera/fragment/BaseFragment;->mDegree:I

    invoke-virtual {v0, p0}, Lcom/android/camera/fragment/mode/ModeAdapter;->setRotate(I)V

    invoke-virtual {p1, v0}, Landroidx/recyclerview/widget/RecyclerView;->setAdapter(Landroidx/recyclerview/widget/RecyclerView$Adapter;)V

    const/4 p0, 0x0

    invoke-virtual {p1, p0}, Landroid/view/ViewGroup;->setClickable(Z)V

    return-void
.end method

.method public modeShouldDownload(I)Z
    .locals 3

    invoke-static {p1}, Lcom/android/camera/data/observeable/VMFeature;->getFeatureNameByLocalMode(I)Ljava/lang/String;

    move-result-object p1

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v2, 0x3a1

    invoke-virtual {v0, v2}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;

    if-eqz v0, :cond_4

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;->getInstalledFeatures()Ljava/util/List;

    move-result-object v2

    if-eqz v2, :cond_4

    invoke-interface {v0}, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;->getInstalledFeatures()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, p1}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1

    goto :goto_1

    :cond_1
    iget-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mVMFeature:Lcom/android/camera/data/observeable/VMFeature;

    invoke-virtual {v0}, Lcom/android/camera/data/observeable/VMFeature;->getState()Ljava/util/HashMap;

    move-result-object v0

    invoke-virtual {v0, p1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    if-eqz v0, :cond_3

    iget-object p0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mVMFeature:Lcom/android/camera/data/observeable/VMFeature;

    invoke-virtual {p0}, Lcom/android/camera/data/observeable/VMFeature;->getState()Ljava/util/HashMap;

    move-result-object p0

    invoke-virtual {p0, p1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Integer;

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    const/16 p1, 0x15

    if-eq p0, p1, :cond_2

    goto :goto_0

    :cond_2
    return v1

    :cond_3
    :goto_0
    const/4 p0, 0x1

    return p0

    :cond_4
    :goto_1
    return v1
.end method

.method public notifyDataChanged(II)V
    .locals 0

    invoke-super {p0, p1, p2}, Lcom/android/camera/fragment/BaseFragment;->notifyDataChanged(II)V

    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getModeAdapter()Lcom/android/camera/fragment/mode/ModeAdapter;

    move-result-object p0

    invoke-virtual {p0}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->notifyDataSetChanged()V

    return-void
.end method

.method public onBackEvent(I)Z
    .locals 1

    const/4 v0, 0x1

    if-ne p1, v0, :cond_0

    iget-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadingFeature:Ljava/lang/String;

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result p1

    if-nez p1, :cond_0

    invoke-virtual {p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->showDownloadCancelDialog()V

    return v0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public onClick(Landroid/view/View;)V
    .locals 7

    invoke-virtual {p1}, Landroid/view/View;->getId()I

    move-result v0

    packed-switch v0, :pswitch_data_0

    goto/16 :goto_1

    :pswitch_0
    iget-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadingFeature:Ljava/lang/String;

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    const/4 v1, 0x1

    const/4 v2, 0x0

    const-string v3, "MoreModeBase"

    if-nez v0, :cond_2

    const v0, 0x7f09023e

    invoke-virtual {p1, v0}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object v0

    instance-of v4, v0, Lcom/android/camera/ui/ModeBackground;

    const/16 v5, 0x64

    if-eqz v4, :cond_0

    check-cast v0, Lcom/android/camera/ui/ModeBackground;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "old click downloading: "

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Lcom/android/camera/ui/ModeBackground;->getProgress()I

    move-result v6

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-static {v3, v4}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {v0}, Lcom/android/camera/ui/ModeBackground;->getProgress()I

    move-result v0

    goto :goto_0

    :cond_0
    instance-of v4, v0, Lcom/android/camera/ui/WaterBox;

    if-eqz v4, :cond_1

    check-cast v0, Lcom/android/camera/ui/WaterBox;

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "new click downloading: "

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Lcom/android/camera/ui/WaterBox;->getValue()F

    move-result v6

    invoke-virtual {v4, v6}, Ljava/lang/StringBuilder;->append(F)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-static {v3, v4}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {v0}, Lcom/android/camera/ui/WaterBox;->getValue()F

    move-result v0

    const/high16 v4, 0x42c80000    # 100.0f

    mul-float/2addr v0, v4

    float-to-int v0, v0

    goto :goto_0

    :cond_1
    move v0, v5

    :goto_0
    if-ge v0, v5, :cond_2

    const p1, 0x7f100733

    new-array v1, v1, [Ljava/lang/Object;

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    aput-object v0, v1, v2

    invoke-virtual {p0, p1, v1}, Landroidx/fragment/app/Fragment;->getString(I[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;

    move-result-object p0

    const/16 v0, 0x50

    invoke-static {p0, p1, v0}, Lcom/android/camera/ToastUtils;->showToast(Landroid/content/Context;Ljava/lang/String;I)V

    return-void

    :cond_2
    invoke-virtual {p1}, Landroid/view/View;->getTag()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lcom/android/camera/data/data/ComponentDataItem;

    iget-object p1, p1, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {p1}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p1

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    new-array v1, v1, [Ljava/lang/Object;

    invoke-static {p1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    aput-object v4, v1, v2

    const-string v4, "onClick mode_item 0x%x"

    invoke-static {v0, v4, v1}, Ljava/lang/String;->format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v3, v0}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    const/16 v0, 0xff

    if-ne p1, v0, :cond_3

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p0

    const/16 p1, 0xa4

    invoke-virtual {p0, p1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p0

    check-cast p0, Lcom/android/camera/protocol/ModeProtocol$ConfigChanges;

    if-eqz p0, :cond_5

    invoke-interface {p0}, Lcom/android/camera/protocol/ModeProtocol$ConfigChanges;->configModeEdit()V

    goto :goto_1

    :cond_3
    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->checkInstallState(I)Z

    move-result v0

    if-nez v0, :cond_4

    return-void

    :cond_4
    invoke-direct {p0, p1, v2}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->onInstalledOK(IZ)V

    :cond_5
    :goto_1
    return-void

    :pswitch_data_0
    .packed-switch 0x7f09024a
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public onCreate(Landroid/os/Bundle;)V
    .locals 1
    .param p1    # Landroid/os/Bundle;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-super {p0, p1}, Landroidx/fragment/app/Fragment;->onCreate(Landroid/os/Bundle;)V

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object p1

    invoke-virtual {p1}, Lcom/android/camera/data/data/global/DataItemGlobal;->getComponentModuleList()Lcom/android/camera/data/data/global/ComponentModuleList;

    move-result-object p1

    iput-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mComponentModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p1

    const/16 v0, 0x3a1

    invoke-virtual {p1, v0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p1

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;

    if-eqz p1, :cond_0

    invoke-interface {p1}, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;->init()V

    :cond_0
    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemObservable()Lcom/android/camera/data/observeable/DataItemObservable;

    move-result-object p1

    const-class v0, Lcom/android/camera/data/observeable/VMFeature;

    invoke-virtual {p1, v0}, Lcom/android/camera/data/observeable/DataItemObservable;->get(Ljava/lang/Class;)Lcom/android/camera/data/observeable/VMBase;

    move-result-object p1

    check-cast p1, Lcom/android/camera/data/observeable/VMFeature;

    iput-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mVMFeature:Lcom/android/camera/data/observeable/VMFeature;

    iget-object p1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mVMFeature:Lcom/android/camera/data/observeable/VMFeature;

    new-instance v0, Lcom/android/camera/fragment/mode/O000000o;

    invoke-direct {v0, p0}, Lcom/android/camera/fragment/mode/O000000o;-><init>(Lcom/android/camera/fragment/mode/FragmentMoreModeBase;)V

    invoke-virtual {p1, p0, v0}, Lcom/android/camera/data/observeable/VMFeature;->startObservable(Landroidx/lifecycle/LifecycleOwner;Lio/reactivex/functions/Consumer;)V

    return-void
.end method

.method public onCreateAnimation(IZI)Landroid/view/animation/Animation;
    .locals 0

    const/4 p0, 0x0

    return-object p0
.end method

.method public onPause()V
    .locals 2

    invoke-super {p0}, Landroidx/fragment/app/Fragment;->onPause()V

    iget-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadConfirmDialog:Landroid/app/AlertDialog;

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Landroid/app/AlertDialog;->dismiss()V

    iput-object v1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadConfirmDialog:Landroid/app/AlertDialog;

    :cond_0
    iget-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    if-eqz v0, :cond_1

    invoke-virtual {v0}, Landroid/app/AlertDialog;->dismiss()V

    iput-object v1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    :cond_1
    return-void
.end method

.method public provideOrientationChanged(ILjava/util/List;I)V
    .locals 0

    invoke-super {p0, p1, p2, p3}, Lcom/android/camera/fragment/BaseFragment;->provideOrientationChanged(ILjava/util/List;I)V

    return-void
.end method

.method public provideRotateItem(Ljava/util/List;I)V
    .locals 0

    invoke-super {p0, p1, p2}, Lcom/android/camera/fragment/BaseFragment;->provideRotateItem(Ljava/util/List;I)V

    iput p2, p0, Lcom/android/camera/fragment/BaseFragment;->mDegree:I

    return-void
.end method

.method protected register(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;)V
    .locals 0

    invoke-super {p0, p1}, Lcom/android/camera/fragment/BaseFragment;->register(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;)V

    invoke-virtual {p0, p1, p0}, Lcom/android/camera/fragment/BaseFragment;->registerBackStack(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;Lcom/android/camera/protocol/ModeProtocol$HandleBackTrace;)V

    return-void
.end method

.method protected showDownloadCancelDialog()V
    .locals 5

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0x3a1

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;

    iget-object v1, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadingFeature:Ljava/lang/String;

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getContext()Landroid/content/Context;

    move-result-object v2

    new-instance v3, Lcom/android/camera/fragment/mode/FragmentMoreModeBase$3;

    invoke-direct {v3, p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase$3;-><init>(Lcom/android/camera/fragment/mode/FragmentMoreModeBase;)V

    const/4 v4, 0x1

    invoke-interface {v0, v1, v2, v4, v3}, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;->showDownloadCancelDialog(Ljava/lang/String;Landroid/content/Context;ZLjava/lang/Runnable;)Landroid/app/AlertDialog;

    move-result-object v0

    iput-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    iget-object v0, p0, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->mDownloadCancelDialog:Landroid/app/AlertDialog;

    if-eqz v0, :cond_0

    new-instance v1, Lcom/android/camera/fragment/mode/FragmentMoreModeBase$4;

    invoke-direct {v1, p0}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase$4;-><init>(Lcom/android/camera/fragment/mode/FragmentMoreModeBase;)V

    invoke-virtual {v0, v1}, Landroid/app/AlertDialog;->setOnDismissListener(Landroid/content/DialogInterface$OnDismissListener;)V

    :cond_0
    return-void
.end method

.method protected unRegister(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;)V
    .locals 0

    invoke-super {p0, p1}, Lcom/android/camera/fragment/BaseFragment;->unRegister(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;)V

    invoke-virtual {p0, p1, p0}, Lcom/android/camera/fragment/BaseFragment;->unRegisterBackStack(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;Lcom/android/camera/protocol/ModeProtocol$HandleBackTrace;)V

    return-void
.end method

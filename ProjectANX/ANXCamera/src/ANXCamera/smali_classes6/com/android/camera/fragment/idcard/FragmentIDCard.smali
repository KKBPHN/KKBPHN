.class public Lcom/android/camera/fragment/idcard/FragmentIDCard;
.super Lcom/android/camera/fragment/BaseFragment;
.source ""

# interfaces
.implements Lcom/android/camera/protocol/ModeProtocol$IDCardModeProtocol;
.implements Lcom/android/camera/protocol/ModeProtocol$HandleBackTrace;


# instance fields
.field private mCurrentPictureName:Ljava/lang/String;

.field private mRoot:Landroid/view/ViewGroup;

.field private mVMFeature:Lcom/android/camera/data/observeable/VMFeature;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Lcom/android/camera/fragment/BaseFragment;-><init>()V

    const-string v0, "ID_CARD_PICTURE_1"

    iput-object v0, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mCurrentPictureName:Ljava/lang/String;

    return-void
.end method

.method private changeIDCardView(Z)V
    .locals 2

    new-instance v0, Lcom/android/camera/fragment/idcard/IDCardView;

    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getActivity()Landroidx/fragment/app/FragmentActivity;

    move-result-object v1

    invoke-direct {v0, v1, p1}, Lcom/android/camera/fragment/idcard/IDCardView;-><init>(Landroid/content/Context;Z)V

    iget-object v1, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mRoot:Landroid/view/ViewGroup;

    invoke-virtual {v1}, Landroid/view/ViewGroup;->removeAllViews()V

    iget-object v1, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mRoot:Landroid/view/ViewGroup;

    invoke-virtual {v1, v0}, Landroid/view/ViewGroup;->addView(Landroid/view/View;)V

    invoke-static {}, Lcom/android/camera/Util;->isAccessible()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0xac

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$TopAlert;

    if-eqz v0, :cond_1

    if-eqz p1, :cond_0

    const v1, 0x7f1002fd

    goto :goto_0

    :cond_0
    const v1, 0x7f1002fc

    :goto_0
    invoke-interface {v0, v1}, Lcom/android/camera/protocol/ModeProtocol$TopAlert;->alertDocumentTip(I)V

    :cond_1
    if-eqz p1, :cond_2

    const-string p1, "ID_CARD_PICTURE_1"

    goto :goto_1

    :cond_2
    const-string p1, "ID_CARD_PICTURE_2"

    :goto_1
    invoke-direct {p0, p1}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->setCurrentPictureName(Ljava/lang/String;)V

    return-void
.end method

.method private onInstallStateChanged(Ljava/util/HashMap;)V
    .locals 3

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
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_5

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/Map$Entry;

    invoke-interface {v0}, Ljava/util/Map$Entry;->getKey()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    const-string v2, "document"

    invoke-virtual {v1, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_2

    goto :goto_0

    :cond_2
    invoke-interface {v0}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    invoke-static {v0}, Lcom/android/camera/data/observeable/VMFeature;->getScope(I)I

    move-result v1

    const/16 v2, 0x10

    if-eq v1, v2, :cond_3

    const/16 v0, 0x100

    goto :goto_0

    :cond_3
    const/16 v1, 0x11

    if-eq v0, v1, :cond_1

    const/16 v1, 0x13

    if-eq v0, v1, :cond_1

    const/16 v1, 0x15

    if-eq v0, v1, :cond_1

    const/16 v1, 0x16

    if-eq v0, v1, :cond_4

    goto :goto_0

    :cond_4
    invoke-direct {p0}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->onInstalled()V

    goto :goto_0

    :cond_5
    return-void
.end method

.method private onInstalled()V
    .locals 0

    return-void
.end method

.method private setCurrentPictureName(Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mCurrentPictureName:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public synthetic O00000oO(Lcom/android/camera/data/observeable/RxData$DataWrap;)V
    .locals 0

    invoke-virtual {p1}, Lcom/android/camera/data/observeable/RxData$DataWrap;->get()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Ljava/util/HashMap;

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->onInstallStateChanged(Ljava/util/HashMap;)V

    return-void
.end method

.method public callBackEvent()V
    .locals 3

    invoke-virtual {p0}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->getCurrentPictureName()Ljava/lang/String;

    move-result-object v0

    const-string v1, "ID_CARD_PICTURE_2"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    invoke-direct {p0, v0}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->changeIDCardView(Z)V

    goto :goto_0

    :cond_0
    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v0

    const/16 v1, 0xba

    invoke-virtual {v0, v1}, Lcom/android/camera/data/data/runing/DataItemRunning;->getEntranceMode(I)I

    move-result v0

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v1

    const/16 v2, 0xb3

    invoke-virtual {v1, v2}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v1

    check-cast v1, Lcom/android/camera/protocol/ModeProtocol$ModeChangeController;

    const v2, 0x7f100615

    invoke-virtual {p0, v2}, Landroidx/fragment/app/Fragment;->getString(I)Ljava/lang/String;

    move-result-object p0

    const/4 v2, 0x0

    invoke-interface {v1, v0, p0, v2}, Lcom/android/camera/protocol/ModeProtocol$ModeChangeController;->changeModeByNewMode(ILjava/lang/String;I)V

    :goto_0
    return-void
.end method

.method public getCurrentPictureName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mCurrentPictureName:Ljava/lang/String;

    return-object p0
.end method

.method public getFragmentInto()I
    .locals 0

    const p0, 0xffff0

    return p0
.end method

.method protected getLayoutResourceId()I
    .locals 0

    const p0, 0x7f0c0070

    return p0
.end method

.method protected initView(Landroid/view/View;)V
    .locals 2

    const v0, 0x7f090188

    invoke-virtual {p1, v0}, Landroid/view/View;->findViewById(I)Landroid/view/View;

    move-result-object p1

    check-cast p1, Landroid/view/ViewGroup;

    iput-object p1, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mRoot:Landroid/view/ViewGroup;

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object p1

    invoke-virtual {p1}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0ooo()Z

    move-result p1

    if-eqz p1, :cond_2

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object p1

    invoke-virtual {p1}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO00()Z

    move-result p1

    if-eqz p1, :cond_2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p1

    const/16 v0, 0x3a1

    invoke-virtual {p1, v0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p1

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;

    const-string v0, "document"

    invoke-interface {p1, v0}, Lcom/android/camera/protocol/ModeProtocol$MultiFeatureManager;->hasFeatureInstalled(Ljava/lang/String;)Z

    move-result p1

    if-nez p1, :cond_1

    new-instance p1, Lcom/android/camera/fragment/vv/FragmentVVFeature;

    invoke-direct {p1}, Lcom/android/camera/fragment/vv/FragmentVVFeature;-><init>()V

    invoke-virtual {p1, v0}, Lcom/android/camera/fragment/vv/FragmentVVFeature;->setFeatureName(Ljava/lang/String;)V

    invoke-static {}, Lcom/android/camera/Display;->getBottomHeight()I

    move-result v0

    invoke-virtual {p1, v0}, Lcom/android/camera/fragment/vv/FragmentVVFeature;->setFixedMargin(I)V

    iget-object v0, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mVMFeature:Lcom/android/camera/data/observeable/VMFeature;

    if-nez v0, :cond_0

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemObservable()Lcom/android/camera/data/observeable/DataItemObservable;

    move-result-object v0

    const-class v1, Lcom/android/camera/data/observeable/VMFeature;

    invoke-virtual {v0, v1}, Lcom/android/camera/data/observeable/DataItemObservable;->get(Ljava/lang/Class;)Lcom/android/camera/data/observeable/VMBase;

    move-result-object v0

    check-cast v0, Lcom/android/camera/data/observeable/VMFeature;

    iput-object v0, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mVMFeature:Lcom/android/camera/data/observeable/VMFeature;

    iget-object v0, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mVMFeature:Lcom/android/camera/data/observeable/VMFeature;

    new-instance v1, Lcom/android/camera/fragment/idcard/O000000o;

    invoke-direct {v1, p0}, Lcom/android/camera/fragment/idcard/O000000o;-><init>(Lcom/android/camera/fragment/idcard/FragmentIDCard;)V

    invoke-virtual {v0, p0, v1}, Lcom/android/camera/data/observeable/VMFeature;->startObservable(Landroidx/lifecycle/LifecycleOwner;Lio/reactivex/functions/Consumer;)V

    :cond_0
    invoke-virtual {p0}, Landroidx/fragment/app/Fragment;->getChildFragmentManager()Landroidx/fragment/app/FragmentManager;

    move-result-object p0

    const v0, 0x7f090187

    const-string v1, "VVFeature"

    invoke-static {p0, v0, p1, v1}, Lcom/android/camera/fragment/FragmentUtils;->addFragmentWithTag(Landroidx/fragment/app/FragmentManager;ILandroidx/fragment/app/Fragment;Ljava/lang/String;)V

    goto :goto_0

    :cond_1
    invoke-direct {p0}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->onInstalled()V

    :cond_2
    :goto_0
    return-void
.end method

.method public notifyAfterFrameAvailable(I)V
    .locals 1

    invoke-super {p0, p1}, Lcom/android/camera/fragment/BaseFragment;->notifyAfterFrameAvailable(I)V

    iget p1, p0, Lcom/android/camera/fragment/BaseFragment;->mCurrentMode:I

    const/16 v0, 0xb6

    if-ne p1, v0, :cond_0

    iget-object p1, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mRoot:Landroid/view/ViewGroup;

    const/4 v0, 0x0

    invoke-virtual {p1, v0}, Landroid/view/ViewGroup;->setVisibility(I)V

    const/4 p1, 0x1

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->changeIDCardView(Z)V

    :cond_0
    return-void
.end method

.method public onBackEvent(I)Z
    .locals 2

    const/4 v0, 0x1

    if-ne p1, v0, :cond_0

    iget p1, p0, Lcom/android/camera/fragment/BaseFragment;->mCurrentMode:I

    const/16 v1, 0xb6

    if-ne p1, v1, :cond_0

    invoke-virtual {p0}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->callBackEvent()V

    return v0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public onDestroyView()V
    .locals 2

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object v0

    const/16 v1, 0xab

    invoke-virtual {v0, v1}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object v0

    check-cast v0, Lcom/android/camera/protocol/ModeProtocol$BackStack;

    if-eqz v0, :cond_0

    invoke-interface {v0, p0}, Lcom/android/camera/protocol/ModeProtocol$BackStack;->removeBackStack(Lcom/android/camera/protocol/ModeProtocol$HandleBackTrace;)V

    :cond_0
    invoke-super {p0}, Lcom/android/camera/fragment/BaseFragment;->onDestroyView()V

    return-void
.end method

.method public onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V
    .locals 0
    .param p2    # Landroid/os/Bundle;
        .annotation build Landroidx/annotation/Nullable;
        .end annotation
    .end param

    invoke-super {p0, p1, p2}, Lcom/android/camera/fragment/BaseFragment;->onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p1

    const/16 p2, 0xab

    invoke-virtual {p1, p2}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getAttachProtocol(I)Lcom/android/camera/protocol/ModeProtocol$BaseProtocol;

    move-result-object p1

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$BackStack;

    if-eqz p1, :cond_0

    invoke-interface {p1, p0}, Lcom/android/camera/protocol/ModeProtocol$BackStack;->addInBackStack(Lcom/android/camera/protocol/ModeProtocol$HandleBackTrace;)V

    :cond_0
    return-void
.end method

.method public provideAnimateElement(ILjava/util/List;I)V
    .locals 0

    invoke-super {p0, p1, p2, p3}, Lcom/android/camera/fragment/BaseFragment;->provideAnimateElement(ILjava/util/List;I)V

    const/16 p2, 0xb6

    if-eq p1, p2, :cond_0

    iget-object p0, p0, Lcom/android/camera/fragment/idcard/FragmentIDCard;->mRoot:Landroid/view/ViewGroup;

    const/16 p1, 0x8

    invoke-virtual {p0, p1}, Landroid/view/ViewGroup;->setVisibility(I)V

    :cond_0
    return-void
.end method

.method protected register(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;)V
    .locals 1

    invoke-super {p0, p1}, Lcom/android/camera/fragment/BaseFragment;->register(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;)V

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p1

    const/16 v0, 0xe9

    invoke-virtual {p1, v0, p0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->attachProtocol(ILcom/android/camera/protocol/ModeProtocol$BaseProtocol;)V

    return-void
.end method

.method public switchNextPage()V
    .locals 1

    invoke-virtual {p0}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->getCurrentPictureName()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/android/camera/storage/Storage;->isIdCardPictureOne(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x0

    invoke-direct {p0, v0}, Lcom/android/camera/fragment/idcard/FragmentIDCard;->changeIDCardView(Z)V

    :cond_0
    return-void
.end method

.method protected unRegister(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;)V
    .locals 1

    invoke-super {p0, p1}, Lcom/android/camera/fragment/BaseFragment;->unRegister(Lcom/android/camera/protocol/ModeProtocol$ModeCoordinator;)V

    invoke-static {}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->getInstance()Lcom/android/camera/protocol/ModeCoordinatorImpl;

    move-result-object p1

    const/16 v0, 0xe9

    invoke-virtual {p1, v0, p0}, Lcom/android/camera/protocol/ModeCoordinatorImpl;->detachProtocol(ILcom/android/camera/protocol/ModeProtocol$BaseProtocol;)V

    return-void
.end method

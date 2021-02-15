.class public Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;
.super Lcom/android/camera/data/data/ComponentData;
.source ""


# static fields
.field static final synthetic $assertionsDisabled:Z = false

.field private static final TAG:Ljava/lang/String; = "ComponentRunningDualVideo"


# instance fields
.field private mDrawSelectWindow:Z

.field private mRecordType:Lcom/android/camera/dualvideo/recorder/RecordType;

.field private mUserSelectDataList:Ljava/util/ArrayList;


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Lcom/android/camera/data/data/DataItemBase;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/android/camera/data/data/ComponentData;-><init>(Lcom/android/camera/data/data/DataItemBase;)V

    new-instance p1, Ljava/util/ArrayList;

    invoke-direct {p1}, Ljava/util/ArrayList;-><init>()V

    iput-object p1, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-direct {p0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->initUserSelectionDataIfNeed()V

    sget-object p1, Lcom/android/camera/dualvideo/recorder/RecordType;->MERGED:Lcom/android/camera/dualvideo/recorder/RecordType;

    invoke-virtual {p0, p1}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->setRecordType(Lcom/android/camera/dualvideo/recorder/RecordType;)V

    return-void
.end method

.method static synthetic O000000o(Ljava/util/ArrayList;Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 1

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/data/data/runing/O00000oo;->INSTANCE:Lcom/android/camera/data/data/runing/O00000oo;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    new-instance v0, Lcom/android/camera/data/data/runing/O0000Oo0;

    invoke-direct {v0, p1}, Lcom/android/camera/data/data/runing/O0000Oo0;-><init>(Lcom/android/camera/dualvideo/util/UserSelectData;)V

    invoke-virtual {p0, v0}, Ljava/util/Optional;->ifPresent(Ljava/util/function/Consumer;)V

    return-void
.end method

.method static synthetic O000000o(Ljava/util/concurrent/ConcurrentHashMap;Ljava/lang/Integer;)V
    .locals 1

    invoke-virtual {p0}, Ljava/util/concurrent/ConcurrentHashMap;->clear()V

    sget-object v0, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {p0, v0, p1}, Ljava/util/concurrent/ConcurrentHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 1

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_REMOTE:Lcom/android/camera/dualvideo/render/FaceType;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;Lcom/android/camera/dualvideo/util/UserSelectData;)Z
    .locals 0

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getmSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/util/UserSelectData;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/UserSelectData;->getmSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000Oo(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Lcom/android/camera/dualvideo/render/LayoutType;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    return-object p0
.end method

.method static synthetic O00000Oo(Lcom/android/camera/dualvideo/util/UserSelectData;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)V
    .locals 0

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-virtual {p0, p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->setSelectWindowLayoutType(Lcom/android/camera/dualvideo/render/LayoutType;)V

    return-void
.end method

.method static synthetic O00000Oo(Lcom/android/camera/dualvideo/util/UserSelectData;)Z
    .locals 1

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v0

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/UserSelectData;->getmSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    invoke-virtual {v0, p0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getFaceType(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_BACK:Lcom/android/camera/dualvideo/render/FaceType;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000o(Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 2

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/UserSelectData;->getSelectIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne v0, v1, :cond_0

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->UP:Lcom/android/camera/dualvideo/render/LayoutType;

    :goto_0
    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/util/UserSelectData;->setmRecordLayoutType(Lcom/android/camera/dualvideo/render/LayoutType;)V

    goto :goto_1

    :cond_0
    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/UserSelectData;->getSelectIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne v0, v1, :cond_1

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_0

    :cond_1
    :goto_1
    return-void
.end method

.method static synthetic O00000o(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 1

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_REMOTE:Lcom/android/camera/dualvideo/render/FaceType;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000o(Ljava/lang/Integer;)Z
    .locals 1

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    const/16 v0, 0x3e8

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000o0(Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 2

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/UserSelectData;->getSelectIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne v0, v1, :cond_0

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->UP_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    :goto_0
    invoke-virtual {p0, v0}, Lcom/android/camera/dualvideo/util/UserSelectData;->setmRecordLayoutType(Lcom/android/camera/dualvideo/render/LayoutType;)V

    goto :goto_1

    :cond_0
    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/UserSelectData;->getSelectIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne v0, v1, :cond_1

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_0

    :cond_1
    :goto_1
    return-void
.end method

.method static synthetic O00000oO(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 1

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_REMOTE:Lcom/android/camera/dualvideo/render/FaceType;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000oO(Ljava/lang/Integer;)Z
    .locals 1

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    const/16 v0, 0x3e8

    if-eq p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O0000o0O(I)[Ljava/lang/Integer;
    .locals 0

    new-array p0, p0, [Ljava/lang/Integer;

    return-object p0
.end method

.method private getLayoutForSelect()Lcom/android/camera/dualvideo/render/LayoutType;
    .locals 2

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getConfigs()Ljava/util/ArrayList;

    move-result-object v0

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v0

    sget-object v1, Lcom/android/camera/data/data/runing/O0000oO0;->INSTANCE:Lcom/android/camera/data/data/runing/O0000oO0;

    invoke-static {v1}, Ljava/util/stream/Collectors;->toCollection(Ljava/util/function/Supplier;)Ljava/util/stream/Collector;

    move-result-object v1

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->collect(Ljava/util/stream/Collector;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/data/data/runing/O0000o0O;

    invoke-direct {v1, p0}, Lcom/android/camera/data/data/runing/O0000o0O;-><init>(Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->removeIf(Ljava/util/function/Predicate;)Z

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v1, Lcom/android/camera/data/data/runing/O0000OoO;->INSTANCE:Lcom/android/camera/data/data/runing/O0000OoO;

    invoke-interface {p0, v1}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    sget-object v1, Lcom/android/camera/data/data/runing/O00000o0;->INSTANCE:Lcom/android/camera/data/data/runing/O00000o0;

    invoke-virtual {p0, v1}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object p0

    const/4 v1, 0x0

    invoke-virtual {p0, v1}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/render/LayoutType;

    if-eqz p0, :cond_0

    return-object p0

    :cond_0
    const/4 p0, 0x0

    invoke-virtual {v0, p0}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    return-object p0
.end method

.method private initUserSelectionDataIfNeed()V
    .locals 5

    iget-object v0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO000o()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    invoke-virtual {p0, v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->setmDrawSelectWindow(Z)V

    iget-object v0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/dualvideo/util/UserSelectData;

    sget-object v2, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->UP:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-direct {v1, v2, v3, v4}, Lcom/android/camera/dualvideo/util/UserSelectData;-><init>(Lcom/android/camera/dualvideo/util/SelectIndex;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/LayoutType;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    new-instance v0, Lcom/android/camera/dualvideo/util/UserSelectData;

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    sget-object v2, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_3:Lcom/android/camera/dualvideo/render/LayoutType;

    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-direct {v0, v1, v2, v3}, Lcom/android/camera/dualvideo/util/UserSelectData;-><init>(Lcom/android/camera/dualvideo/util/SelectIndex;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/LayoutType;)V

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->setmDrawSelectWindow(Z)V

    iget-object v0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/dualvideo/util/UserSelectData;

    sget-object v2, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->UP:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-direct {v1, v2, v3, v4}, Lcom/android/camera/dualvideo/util/UserSelectData;-><init>(Lcom/android/camera/dualvideo/util/SelectIndex;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/LayoutType;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    new-instance v0, Lcom/android/camera/dualvideo/util/UserSelectData;

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    sget-object v2, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_1:Lcom/android/camera/dualvideo/render/LayoutType;

    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-direct {v0, v1, v2, v3}, Lcom/android/camera/dualvideo/util/UserSelectData;-><init>(Lcom/android/camera/dualvideo/util/SelectIndex;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/LayoutType;)V

    :goto_0
    invoke-virtual {p0, v0}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    :cond_1
    return-void
.end method


# virtual methods
.method public synthetic O00000o0(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 1

    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/data/data/runing/O0000Oo;

    invoke-direct {v0, p1}, Lcom/android/camera/data/data/runing/O0000Oo;-><init>(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result p0

    return p0
.end method

.method public getDefaultValue(I)Ljava/lang/String;
    .locals 0
    .annotation build Landroidx/annotation/NonNull;
    .end annotation

    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mRecordType:Lcom/android/camera/dualvideo/recorder/RecordType;

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/recorder/RecordType;->getName()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getDisplayTitleString()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public getIds()Ljava/util/concurrent/ConcurrentHashMap;
    .locals 1

    invoke-virtual {p0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->ismDrawSelectWindow()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getDefaultDualVideoCameraIds()Ljava/util/concurrent/ConcurrentHashMap;

    move-result-object p0

    return-object p0

    :cond_0
    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/data/data/runing/O000000o;->INSTANCE:Lcom/android/camera/data/data/runing/O000000o;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->map(Ljava/util/function/Function;)Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/data/data/runing/O00000Oo;->INSTANCE:Lcom/android/camera/data/data/runing/O00000Oo;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->toArray(Ljava/util/function/IntFunction;)[Ljava/lang/Object;

    move-result-object p0

    check-cast p0, [Ljava/lang/Integer;

    invoke-static {p0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->sortId([Ljava/lang/Integer;)Ljava/util/concurrent/ConcurrentHashMap;

    move-result-object p0

    return-object p0
.end method

.method public getItems()Ljava/util/List;
    .locals 15

    new-instance p0, Ljava/util/ArrayList;

    invoke-direct {p0}, Ljava/util/ArrayList;-><init>()V

    new-instance v7, Lcom/android/camera/data/data/ComponentDataItem;

    sget-object v0, Lcom/android/camera/dualvideo/recorder/RecordType;->MERGED:Lcom/android/camera/dualvideo/recorder/RecordType;

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/recorder/RecordType;->getName()Ljava/lang/String;

    move-result-object v6

    const/4 v1, -0x1

    const/4 v2, -0x1

    const/4 v3, -0x1

    const v4, 0x7f100292

    const v5, 0x7f100051

    move-object v0, v7

    invoke-direct/range {v0 .. v6}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IIIIILjava/lang/String;)V

    invoke-interface {p0, v7}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v0, Lcom/android/camera/data/data/ComponentDataItem;

    sget-object v1, Lcom/android/camera/dualvideo/recorder/RecordType;->STANDALONE:Lcom/android/camera/dualvideo/recorder/RecordType;

    invoke-virtual {v1}, Lcom/android/camera/dualvideo/recorder/RecordType;->getName()Ljava/lang/String;

    move-result-object v14

    const/4 v9, -0x1

    const/4 v10, -0x1

    const/4 v11, -0x1

    const v12, 0x7f100293

    const v13, 0x7f100052

    move-object v8, v0

    invoke-direct/range {v8 .. v14}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IIIIILjava/lang/String;)V

    invoke-interface {p0, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    invoke-static {p0}, Ljava/util/Collections;->unmodifiableList(Ljava/util/List;)Ljava/util/List;

    move-result-object p0

    return-object p0
.end method

.method public getKey(I)Ljava/lang/String;
    .locals 0

    const/4 p0, 0x0

    return-object p0
.end method

.method public getLocalCameraId()Ljava/util/concurrent/ConcurrentHashMap;
    .locals 3

    invoke-virtual {p0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getIds()Ljava/util/concurrent/ConcurrentHashMap;

    move-result-object p0

    invoke-virtual {p0}, Ljava/util/concurrent/ConcurrentHashMap;->values()Ljava/util/Collection;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/Collection;->stream()Ljava/util/stream/Stream;

    move-result-object v1

    sget-object v2, Lcom/android/camera/data/data/runing/O0000o0;->INSTANCE:Lcom/android/camera/data/data/runing/O0000o0;

    invoke-interface {v1, v2}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-interface {v0}, Ljava/util/Collection;->stream()Ljava/util/stream/Stream;

    move-result-object v0

    sget-object v1, Lcom/android/camera/data/data/runing/O0000O0o;->INSTANCE:Lcom/android/camera/data/data/runing/O0000O0o;

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object v0

    new-instance v1, Lcom/android/camera/data/data/runing/O0000o;

    invoke-direct {v1, p0}, Lcom/android/camera/data/data/runing/O0000o;-><init>(Ljava/util/concurrent/ConcurrentHashMap;)V

    invoke-virtual {v0, v1}, Ljava/util/Optional;->ifPresent(Ljava/util/function/Consumer;)V

    :cond_0
    return-object p0
.end method

.method public getRecordType()Lcom/android/camera/dualvideo/recorder/RecordType;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mRecordType:Lcom/android/camera/dualvideo/recorder/RecordType;

    return-object p0
.end method

.method public getSelectedData()Ljava/util/ArrayList;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    return-object p0
.end method

.method public ismDrawSelectWindow()Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mDrawSelectWindow:Z

    return p0
.end method

.method public reInit()V
    .locals 1

    sget-object v0, Lcom/android/camera/dualvideo/recorder/RecordType;->MERGED:Lcom/android/camera/dualvideo/recorder/RecordType;

    invoke-virtual {p0, v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->setRecordType(Lcom/android/camera/dualvideo/recorder/RecordType;)V

    iget-object v0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->clear()V

    invoke-direct {p0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->initUserSelectionDataIfNeed()V

    return-void
.end method

.method public refreshSelectData()V
    .locals 5

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getConfigs()Ljava/util/ArrayList;

    move-result-object v0

    iget-object v1, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-virtual {v1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v1

    :cond_0
    :goto_0
    invoke-interface {v1}, Ljava/util/Iterator;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_1

    invoke-interface {v1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/android/camera/dualvideo/util/UserSelectData;

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v3

    new-instance v4, Lcom/android/camera/data/data/runing/O0000o00;

    invoke-direct {v4, v2}, Lcom/android/camera/data/data/runing/O0000o00;-><init>(Lcom/android/camera/dualvideo/util/UserSelectData;)V

    invoke-interface {v3, v4}, Ljava/util/stream/Stream;->noneMatch(Ljava/util/function/Predicate;)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-direct {p0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getLayoutForSelect()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v3

    invoke-virtual {v2, v3}, Lcom/android/camera/dualvideo/util/UserSelectData;->setSelectWindowLayoutType(Lcom/android/camera/dualvideo/render/LayoutType;)V

    goto :goto_0

    :cond_1
    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v1

    sget-object v2, Lcom/android/camera/data/data/runing/O0000o0o;->INSTANCE:Lcom/android/camera/data/data/runing/O0000o0o;

    invoke-interface {v1, v2}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result v1

    if-eqz v1, :cond_2

    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v1, Lcom/android/camera/data/data/runing/O0000Ooo;->INSTANCE:Lcom/android/camera/data/data/runing/O0000Ooo;

    invoke-interface {p0, v1}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    new-instance v1, Lcom/android/camera/data/data/runing/O00000o;

    invoke-direct {v1, v0}, Lcom/android/camera/data/data/runing/O00000o;-><init>(Ljava/util/ArrayList;)V

    invoke-virtual {p0, v1}, Ljava/util/Optional;->ifPresent(Ljava/util/function/Consumer;)V

    :cond_2
    return-void
.end method

.method public setRecordType(Lcom/android/camera/dualvideo/recorder/RecordType;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "setRecordType: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const-string v1, "ComponentRunningDualVideo"

    invoke-static {v1, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mRecordType:Lcom/android/camera/dualvideo/recorder/RecordType;

    if-ne v0, p1, :cond_0

    return-void

    :cond_0
    iput-object p1, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mRecordType:Lcom/android/camera/dualvideo/recorder/RecordType;

    sget-object p1, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo$1;->$SwitchMap$com$android$camera$dualvideo$recorder$RecordType:[I

    iget-object v0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mRecordType:Lcom/android/camera/dualvideo/recorder/RecordType;

    invoke-virtual {v0}, Ljava/lang/Enum;->ordinal()I

    move-result v0

    aget p1, p1, v0

    const/4 v0, 0x1

    if-eq p1, v0, :cond_2

    const/4 v0, 0x2

    if-eq p1, v0, :cond_1

    goto :goto_1

    :cond_1
    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object p1, Lcom/android/camera/data/data/runing/O00000oO;->INSTANCE:Lcom/android/camera/data/data/runing/O00000oO;

    goto :goto_0

    :cond_2
    iget-object p0, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mUserSelectDataList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object p1, Lcom/android/camera/data/data/runing/O0000OOo;->INSTANCE:Lcom/android/camera/data/data/runing/O0000OOo;

    :goto_0
    invoke-interface {p0, p1}, Ljava/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    :goto_1
    return-void
.end method

.method public setmDrawSelectWindow(Z)V
    .locals 0

    iput-boolean p1, p0, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->mDrawSelectWindow:Z

    return-void
.end method

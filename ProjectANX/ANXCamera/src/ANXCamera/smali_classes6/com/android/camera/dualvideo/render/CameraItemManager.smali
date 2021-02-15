.class public Lcom/android/camera/dualvideo/render/CameraItemManager;
.super Ljava/lang/Object;
.source ""


# static fields
.field static final synthetic $assertionsDisabled:Z = false

.field private static final TAG:Ljava/lang/String; = "CameraItemManager"


# instance fields
.field private final mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

.field private final mRenderLocker:Ljava/lang/Object;

.field private final mRenderableList:Ljava/util/ArrayList;

.field private mTextureMap:Ljava/util/HashMap;


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Ljava/lang/Object;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    iput-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderLocker:Ljava/lang/Object;

    new-instance p1, Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-static {}, Lcom/android/camera/dualvideo/ModuleUtil;->getUIStyle()I

    move-result v0

    invoke-static {v0}, Lcom/android/camera/Util;->getDisplayRect(I)Landroid/graphics/Rect;

    move-result-object v0

    invoke-direct {p1, v0}, Lcom/android/camera/dualvideo/render/RegionHelper;-><init>(Landroid/graphics/Rect;)V

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    return-void
.end method

.method public static synthetic O000000o(Lcom/android/camera/dualvideo/render/CameraItemManager;)V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->updateRenderableList()V

    return-void
.end method

.method static synthetic O000000o(Ljava/util/ArrayList;Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 1

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/render/O0000o;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/render/O0000o;-><init>(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/render/O0000OOo;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/render/O0000OOo;-><init>(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V

    invoke-virtual {p0, v0}, Ljava/util/Optional;->ifPresent(Ljava/util/function/Consumer;)V

    return-void
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/render/CameraItem;Lcom/android/camera/dualvideo/util/UserSelectData;)Z
    .locals 0

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getmSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p1

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItem;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/render/CameraItemInterface;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/render/CameraItemInterface;Lcom/android/camera/dualvideo/util/UserSelectData;)Z
    .locals 0

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getSelectIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p1

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p0

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/util/UserSelectData;)Z
    .locals 0

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getmSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p1

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z
    .locals 0

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

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

.method static synthetic O000000o(Lcom/android/camera/dualvideo/util/SelectIndex;Lcom/android/camera/dualvideo/util/UserSelectData;)Z
    .locals 0

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getSelectIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p1

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000Oo(Lcom/android/camera/dualvideo/render/CameraItem;Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 1

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getSelectIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, v0}, Lcom/android/camera/dualvideo/render/CameraItem;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    return-void
.end method

.method static synthetic O00000Oo(Lcom/android/camera/dualvideo/render/CameraItemInterface;Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 0

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    invoke-virtual {p1, p0}, Lcom/android/camera/dualvideo/util/UserSelectData;->setSelectWindowLayoutType(Lcom/android/camera/dualvideo/render/LayoutType;)V

    return-void
.end method

.method static synthetic O00000Oo(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z
    .locals 1

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-eq p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000o(Lcom/android/camera/dualvideo/render/CameraItemInterface;Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 2

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getmSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v0

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v1

    if-ne v0, v1, :cond_0

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getSelectIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p1

    const/4 v0, 0x1

    invoke-interface {p0, p1, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    :cond_0
    return-void
.end method

.method static synthetic O00000o(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z
    .locals 1

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000o0(ILcom/android/camera/dualvideo/render/CameraItemInterface;)Z
    .locals 2

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-eq v0, v1, :cond_0

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v0

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getCameraId(Lcom/android/camera/dualvideo/render/LayoutType;)I

    move-result v0

    if-ne v0, p0, :cond_0

    sget-object p0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v0, 0x1

    invoke-interface {p1, p0, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    return v0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method static synthetic O00000o0(Lcom/android/camera/dualvideo/render/CameraItemInterface;Lcom/android/camera/dualvideo/util/UserSelectData;)Z
    .locals 0

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getmSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p1

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000oo(Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "userdata: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/UserSelectData;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    const-string v0, "CameraItemManager"

    invoke-static {v0, p0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method static synthetic O00000oo(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z
    .locals 2

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne v0, v1, :cond_0

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->isVisible()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O0000O0o(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z
    .locals 1

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O0000OOo(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 2

    sget-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v1, 0x1

    invoke-interface {p0, v0, v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    return-void
.end method

.method static synthetic O0000Oo(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 3

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v2, 0x1

    if-ne v0, v1, :cond_0

    sget-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    :goto_0
    invoke-interface {p0, v0, v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    goto :goto_1

    :cond_0
    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne v0, v1, :cond_1

    sget-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    goto :goto_0

    :cond_1
    :goto_1
    return-void
.end method

.method static synthetic O0000Oo0(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 2

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-eq v0, v1, :cond_0

    sget-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v1, 0x1

    invoke-interface {p0, v0, v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    :cond_0
    return-void
.end method

.method static synthetic O0000OoO(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "printRenderList: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/Object;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    const-string v0, "CameraItemManager"

    invoke-static {v0, p0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method static synthetic O0000o0O(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z
    .locals 2

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getConfigs()Ljava/util/ArrayList;

    move-result-object v0

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/render/O0000o0o;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O0000o0o;-><init>(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->noneMatch(Ljava/util/function/Predicate;)Z

    move-result p0

    return p0
.end method

.method static synthetic O0000o0o(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z
    .locals 1

    invoke-interface {p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method private createCameraItem(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/CameraItem;
    .locals 3

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO000o()Z

    move-result v0

    if-eqz v0, :cond_0

    new-instance v0, Lcom/android/camera/dualvideo/render/CameraItem;

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v1

    invoke-virtual {v1, p1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getFaceType(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object v1

    invoke-direct {v0, p1, p1, v1}, Lcom/android/camera/dualvideo/render/CameraItem;-><init>(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/FaceType;)V

    const/4 p1, 0x1

    invoke-virtual {v0, p1}, Lcom/android/camera/dualvideo/render/CameraItem;->alphaInSelectWindowFlag(Z)V

    goto :goto_0

    :cond_0
    new-instance v0, Lcom/android/camera/dualvideo/render/CameraItem;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderUtil;->getRecordType(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v1

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v2

    invoke-virtual {v2, p1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getFaceType(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object v2

    invoke-direct {v0, p1, v1, v2}, Lcom/android/camera/dualvideo/render/CameraItem;-><init>(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/FaceType;)V

    :goto_0
    invoke-direct {p0, v0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->initCameraItemAttri(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V

    invoke-direct {p0, v0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->initSelected(Lcom/android/camera/dualvideo/render/CameraItem;)V

    return-object v0
.end method

.method private getFaceTypeByCameraTag(Lcom/android/camera/dualvideo/util/RenderSourceType;)Lcom/android/camera/dualvideo/render/FaceType;
    .locals 1

    sget-object p0, Lcom/android/camera/dualvideo/util/RenderSourceType;->REMOTE:Lcom/android/camera/dualvideo/util/RenderSourceType;

    if-ne p1, p0, :cond_0

    sget-object p0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_REMOTE:Lcom/android/camera/dualvideo/render/FaceType;

    return-object p0

    :cond_0
    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getIds()Ljava/util/concurrent/ConcurrentHashMap;

    move-result-object v0

    invoke-virtual {v0, p1}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Ljava/lang/Integer;

    invoke-virtual {p1}, Ljava/lang/Integer;->intValue()I

    move-result p1

    invoke-virtual {p0, p1}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->isFrontCameraId(I)Z

    move-result p0

    if-eqz p0, :cond_1

    sget-object p0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_FRONT:Lcom/android/camera/dualvideo/render/FaceType;

    goto :goto_0

    :cond_1
    sget-object p0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_BACK:Lcom/android/camera/dualvideo/render/FaceType;

    :goto_0
    return-object p0
.end method

.method private getIndexBySelelectType(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/util/SelectIndex;
    .locals 1

    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object p0

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/render/O00000oO;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/render/O00000oO;-><init>(Lcom/android/camera/dualvideo/render/LayoutType;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findAny()Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/render/O00000Oo;->INSTANCE:Lcom/android/camera/dualvideo/render/O00000Oo;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/util/SelectIndex;

    return-object p0
.end method

.method private getRenderTypeBySelectIndex(Lcom/android/camera/dualvideo/util/SelectIndex;)Lcom/android/camera/dualvideo/render/LayoutType;
    .locals 1

    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object p0

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/render/O00oOooO;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/render/O00oOooO;-><init>(Lcom/android/camera/dualvideo/util/SelectIndex;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/render/O000ooO0;->INSTANCE:Lcom/android/camera/dualvideo/render/O000ooO0;

    invoke-interface {p0, p1}, Ljava/util/stream/Stream;->map(Ljava/util/function/Function;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findAny()Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/render/LayoutType;->UNDEFINED:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/render/LayoutType;

    return-object p0
.end method

.method private initCameraItemAttri(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 8

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/android/camera/dualvideo/render/RegionHelper;->getRenderAreaFor(Lcom/android/camera/dualvideo/render/LayoutType;)Landroid/graphics/Rect;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/render/CameraItemManager$1;->$SwitchMap$com$android$camera$dualvideo$render$FaceType:[I

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/Enum;->ordinal()I

    move-result v2

    aget v1, v1, v2

    const/4 v2, 0x1

    const-string v3, "CameraItemManager"

    if-eq v1, v2, :cond_2

    const/4 v2, 0x2

    if-eq v1, v2, :cond_1

    const/4 v2, 0x3

    if-eq v1, v2, :cond_0

    const/4 p0, 0x0

    :goto_0
    move-object v2, p0

    goto :goto_2

    :cond_0
    const-string v1, "initCameraItemAttri: remote"

    invoke-static {v3, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    sget-object v1, Lcom/android/camera/dualvideo/util/RenderSourceType;->REMOTE:Lcom/android/camera/dualvideo/util/RenderSourceType;

    goto :goto_1

    :cond_1
    const-string v1, "initCameraItemAttri: front"

    invoke-static {v3, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    sget-object v1, Lcom/android/camera/dualvideo/util/RenderSourceType;->SUB:Lcom/android/camera/dualvideo/util/RenderSourceType;

    goto :goto_1

    :cond_2
    const-string v1, "initCameraItemAttri: back"

    invoke-static {v3, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    sget-object v1, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    :goto_1
    invoke-virtual {p0, v1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/gallery3d/ui/ExtTexture;

    goto :goto_0

    :goto_2
    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p0

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v1

    invoke-static {p0, v1, v2, v0}, Lcom/android/camera/dualvideo/render/RenderUtil;->generatePreviewTransMatrix(Lcom/android/camera/dualvideo/render/FaceType;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/gallery3d/ui/BasicTexture;Landroid/graphics/Rect;)[F

    move-result-object v3

    new-instance p0, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;

    iget v4, v0, Landroid/graphics/Rect;->left:I

    iget v5, v0, Landroid/graphics/Rect;->top:I

    invoke-virtual {v0}, Landroid/graphics/Rect;->width()I

    move-result v6

    invoke-virtual {v0}, Landroid/graphics/Rect;->height()I

    move-result v7

    move-object v1, p0

    invoke-direct/range {v1 .. v7}, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;-><init>(Lcom/android/gallery3d/ui/ExtTexture;[FIIII)V

    const/16 v0, 0x65

    invoke-interface {p1, p0, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setRenderAttri(Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;I)V

    return-void
.end method

.method private initRenderableList()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO000o()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getConfigs()Ljava/util/ArrayList;

    move-result-object v0

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/render/O0000ooo;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O0000ooo;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    goto :goto_0

    :cond_0
    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/render/O0000oo0;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O0000oo0;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    :cond_1
    :goto_0
    return-void
.end method

.method private initSelectIndexFromSelectData()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/dualvideo/render/O00oOoOo;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O00oOoOo;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    return-void
.end method

.method private initSelected(Lcom/android/camera/dualvideo/render/CameraItem;)V
    .locals 1

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningDualVideo()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object p0

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/render/O0000oOo;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/render/O0000oOo;-><init>(Lcom/android/camera/dualvideo/render/CameraItem;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/render/O000OO0o;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/render/O000OO0o;-><init>(Lcom/android/camera/dualvideo/render/CameraItem;)V

    invoke-virtual {p0, v0}, Ljava/util/Optional;->ifPresent(Ljava/util/function/Consumer;)V

    const-string p0, "CameraItemManager"

    const-string p1, "initSelected: "

    invoke-static {p0, p1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method private onTouched(II)Z
    .locals 8

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    const/4 v2, 0x0

    if-eqz v1, :cond_7

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    iget-object v3, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v4

    invoke-virtual {v3, v4}, Lcom/android/camera/dualvideo/render/RegionHelper;->getRenderAreaFor(Lcom/android/camera/dualvideo/render/LayoutType;)Landroid/graphics/Rect;

    move-result-object v3

    invoke-virtual {v3, p1, p2}, Landroid/graphics/Rect;->contains(II)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-interface {v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p1

    sget-object p2, Lcom/android/camera/dualvideo/render/FaceType;->FACE_FRONT:Lcom/android/camera/dualvideo/render/FaceType;

    const/4 v0, 0x1

    if-ne p1, p2, :cond_1

    move v2, v0

    :cond_1
    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object p1

    invoke-interface {v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p2

    invoke-virtual {p1, p2}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getPresentZoom(Lcom/android/camera/dualvideo/render/LayoutType;)F

    move-result p1

    invoke-interface {v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p2

    sget-object v3, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    const-string v4, "front"

    const-string v5, "X"

    const-string v6, "CameraItemManager"

    if-ne p2, v3, :cond_3

    iget-object p2, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {p2}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p2

    sget-object v3, Lcom/android/camera/dualvideo/render/O0000O0o;->INSTANCE:Lcom/android/camera/dualvideo/render/O0000O0o;

    invoke-interface {p2, v3}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p2

    sget-object v3, Lcom/android/camera/dualvideo/render/O0000oOO;->INSTANCE:Lcom/android/camera/dualvideo/render/O0000oOO;

    invoke-interface {p2, v3}, Ljava/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    sget-object p2, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    invoke-interface {v1, p2, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    if-eqz v2, :cond_2

    goto :goto_0

    :cond_2
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(F)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    :goto_0
    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ", index from 1 to 2"

    :goto_1
    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-static {v6, p1}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_4

    :cond_3
    invoke-interface {v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p2

    sget-object v3, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne p2, v3, :cond_6

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object p2

    invoke-interface {v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v3

    invoke-virtual {p2, v3}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getCameraId(Lcom/android/camera/dualvideo/render/LayoutType;)I

    move-result p2

    iget-object v3, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v3}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v3

    new-instance v7, Lcom/android/camera/dualvideo/render/O000O0oo;

    invoke-direct {v7, p2}, Lcom/android/camera/dualvideo/render/O000O0oo;-><init>(I)V

    invoke-interface {v3, v7}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result p2

    if-eqz p2, :cond_4

    iget-object p2, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    sget-object v3, Lcom/android/camera/dualvideo/render/O0000o00;->INSTANCE:Lcom/android/camera/dualvideo/render/O0000o00;

    goto :goto_2

    :cond_4
    iget-object p2, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    sget-object v3, Lcom/android/camera/dualvideo/render/O000O0o0;->INSTANCE:Lcom/android/camera/dualvideo/render/O000O0o0;

    :goto_2
    invoke-virtual {p2, v3}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    sget-object p2, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    invoke-interface {v1, p2, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    if-eqz v2, :cond_5

    goto :goto_3

    :cond_5
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(F)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    :goto_3
    invoke-virtual {p2, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, ", index from 0 to 2"

    goto :goto_1

    :cond_6
    :goto_4
    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->saveSelectedStatus()V

    return v0

    :cond_7
    return v2
.end method

.method private saveSelectedStatus()V
    .locals 2

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/DataItemRunning;->getComponentRunningDualVideo()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object v0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/dualvideo/render/O0000oO0;

    invoke-direct {v1, v0}, Lcom/android/camera/dualvideo/render/O0000oO0;-><init>(Ljava/util/ArrayList;)V

    invoke-virtual {p0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    return-void
.end method

.method private updateRenderableList()V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderLocker:Ljava/lang/Object;

    monitor-enter v0

    :try_start_0
    iget-object v1, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v1}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v1

    sget-object v2, Lcom/android/camera/dualvideo/render/O0000Ooo;->INSTANCE:Lcom/android/camera/dualvideo/render/O0000Ooo;

    invoke-interface {v1, v2}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object v1

    new-instance v2, Lcom/android/camera/dualvideo/render/O0000Oo;

    invoke-direct {v2, p0}, Lcom/android/camera/dualvideo/render/O0000Oo;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-interface {v1, v2}, Ljava/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    monitor-exit v0

    return-void

    :catchall_0
    move-exception p0

    monitor-exit v0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    throw p0
.end method


# virtual methods
.method public synthetic O000000o(FLjava/util/List;Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 8

    const/16 v0, 0x65

    invoke-interface {p3, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderAttri(I)Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;

    move-result-object p3

    check-cast p3, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;

    new-instance v7, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;

    iget-object v1, p3, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;->mExtTexture:Lcom/android/gallery3d/ui/ExtTexture;

    iget-object v2, p3, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;->mTextureTransform:[F

    iget v0, p3, Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;->mX:I

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/RegionHelper;->mDrawRect:Landroid/graphics/Rect;

    iget v3, p0, Landroid/graphics/Rect;->left:I

    sub-int/2addr v0, v3

    int-to-float v0, v0

    mul-float/2addr v0, p1

    float-to-int v3, v0

    iget v0, p3, Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;->mY:I

    iget p0, p0, Landroid/graphics/Rect;->top:I

    sub-int/2addr v0, p0

    int-to-float p0, v0

    mul-float/2addr p0, p1

    float-to-int v4, p0

    iget p0, p3, Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;->mWidth:I

    int-to-float p0, p0

    mul-float/2addr p0, p1

    float-to-int v5, p0

    iget p0, p3, Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;->mHeight:I

    int-to-float p0, p0

    mul-float/2addr p0, p1

    float-to-int v6, p0

    move-object v0, v7

    invoke-direct/range {v0 .. v6}, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;-><init>(Lcom/android/gallery3d/ui/ExtTexture;[FIIII)V

    invoke-interface {p2, v7}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public synthetic O00000o0(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 5

    const/16 v0, 0x65

    invoke-interface {p1, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderAttri(I)Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;

    move-result-object v0

    check-cast v0, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;

    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->ismDrawSelectWindow()Z

    move-result v1

    const/4 v2, 0x1

    if-eqz v1, :cond_0

    sget-object v1, Lcom/android/camera/dualvideo/render/CameraItemManager$1;->$SwitchMap$com$android$camera$dualvideo$render$FaceType:[I

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p1

    invoke-virtual {p1}, Ljava/lang/Enum;->ordinal()I

    move-result p1

    aget p1, v1, p1

    if-eq p1, v2, :cond_3

    const/4 v1, 0x2

    if-eq p1, v1, :cond_5

    const/4 v1, 0x3

    if-eq p1, v1, :cond_6

    goto/16 :goto_2

    :cond_0
    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v1

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p1

    invoke-virtual {v1, p1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getCameraId(Lcom/android/camera/dualvideo/render/LayoutType;)I

    move-result p1

    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getLocalCameraId()Ljava/util/concurrent/ConcurrentHashMap;

    move-result-object v1

    const/16 v3, 0x3e8

    if-ne p1, v3, :cond_1

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    sget-object p1, Lcom/android/camera/dualvideo/util/RenderSourceType;->REMOTE:Lcom/android/camera/dualvideo/util/RenderSourceType;

    :goto_0
    invoke-virtual {p0, p1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/gallery3d/ui/ExtTexture;

    iput-object p0, v0, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;->mExtTexture:Lcom/android/gallery3d/ui/ExtTexture;

    return-void

    :cond_1
    invoke-virtual {v1}, Ljava/util/concurrent/ConcurrentHashMap;->size()I

    move-result v3

    if-ne v3, v2, :cond_2

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    sget-object p1, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    goto :goto_0

    :cond_2
    sget-object v2, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {v1, v2}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Integer;

    invoke-virtual {v2}, Ljava/lang/Integer;->intValue()I

    move-result v2

    sget-object v3, Lcom/android/camera/dualvideo/util/RenderSourceType;->SUB:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {v1, v3}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Integer;->intValue()I

    move-result v1

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "changeTexture: "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " main: "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, " sub "

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    const-string v4, "CameraItemManager"

    invoke-static {v4, v3}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    if-ne p1, v2, :cond_4

    :cond_3
    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    sget-object p1, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    :goto_1
    invoke-virtual {p0, p1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/gallery3d/ui/ExtTexture;

    iput-object p0, v0, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;->mExtTexture:Lcom/android/gallery3d/ui/ExtTexture;

    goto :goto_2

    :cond_4
    if-ne p1, v1, :cond_6

    :cond_5
    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    sget-object p1, Lcom/android/camera/dualvideo/util/RenderSourceType;->SUB:Lcom/android/camera/dualvideo/util/RenderSourceType;

    goto :goto_1

    :cond_6
    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    sget-object p1, Lcom/android/camera/dualvideo/util/RenderSourceType;->REMOTE:Lcom/android/camera/dualvideo/util/RenderSourceType;

    goto :goto_1

    :goto_2
    return-void
.end method

.method public synthetic O00000oO(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 1

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->getIndexBySelelectType(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object p0

    const/4 v0, 0x0

    invoke-interface {p1, p0, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    return-void
.end method

.method public synthetic O00000oO(Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/util/UserSelectData;->getmSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p1

    invoke-direct {p0, p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->createCameraItem(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/CameraItem;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public synthetic O00000oo(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-direct {p0, p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->createCameraItem(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/CameraItem;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public synthetic O0000O0o(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/render/O000O0oO;

    invoke-direct {v1, p1}, Lcom/android/camera/dualvideo/render/O000O0oO;-><init>(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)V

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->noneMatch(Ljava/util/function/Predicate;)Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-direct {p0, p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->createCameraItem(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/CameraItem;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    :cond_0
    return-void
.end method

.method public synthetic O0000Ooo(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 2

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-eq v0, v1, :cond_0

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne v0, v1, :cond_1

    :cond_0
    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    const/4 v1, 0x1

    invoke-interface {p1, v0, p0, v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setRenderLayoutTypeWithAnim(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/RegionHelper;Z)V

    :cond_1
    return-void
.end method

.method public synthetic O0000o(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {p1, p0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->updateRenderAttri(Lcom/android/camera/dualvideo/render/RegionHelper;)V

    return-void
.end method

.method public synthetic O0000o0(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 4

    const/4 v0, 0x0

    invoke-interface {p1, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->alphaInSelectWindowFlag(Z)V

    sget-object v1, Lcom/android/camera/dualvideo/render/CameraItemManager$1;->$SwitchMap$com$android$camera$dualvideo$util$SelectIndex:[I

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/Enum;->ordinal()I

    move-result v2

    aget v1, v1, v2

    const/4 v2, 0x2

    const/4 v3, 0x1

    if-eq v1, v2, :cond_0

    const/4 v2, 0x3

    if-eq v1, v2, :cond_0

    invoke-interface {p1, v0, v3}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setVisibilityWithAnim(ZZ)V

    goto :goto_0

    :cond_0
    invoke-interface {p1, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->alphaInSelectedFrame(Z)V

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->getRenderTypeBySelectIndex(Lcom/android/camera/dualvideo/util/SelectIndex;)Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {p1, v0, p0, v3}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setRenderLayoutTypeWithAnim(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/RegionHelper;Z)V

    :goto_0
    return-void
.end method

.method public synthetic O0000o00(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V
    .locals 3

    const/4 v0, 0x1

    invoke-interface {p1, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->alphaInSelectWindowFlag(Z)V

    sget-object v1, Lcom/android/camera/dualvideo/render/CameraItemManager$1;->$SwitchMap$com$android$camera$dualvideo$util$SelectIndex:[I

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/Enum;->ordinal()I

    move-result v2

    aget v1, v1, v2

    if-eq v1, v0, :cond_1

    const/4 v2, 0x2

    if-eq v1, v2, :cond_0

    const/4 v2, 0x3

    if-eq v1, v2, :cond_0

    goto :goto_0

    :cond_0
    invoke-interface {p1, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->alphaInSelectedFrame(Z)V

    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectWindowLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v1

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {p1, v1, p0, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setRenderLayoutTypeWithAnim(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/RegionHelper;Z)V

    goto :goto_0

    :cond_1
    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v1

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    const/4 v2, 0x0

    invoke-interface {p1, v1, p0, v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setRenderLayoutTypeWithAnim(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/RegionHelper;Z)V

    :goto_0
    invoke-interface {p1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->isVisible()Z

    move-result p0

    if-nez p0, :cond_2

    invoke-interface {p1, v0, v0}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setVisibilityWithAnim(ZZ)V

    :cond_2
    return-void
.end method

.method public changeTexture()V
    .locals 2

    const-string v0, "CameraItemManager"

    const-string v1, "changeTexture: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/render/O000O0o;->INSTANCE:Lcom/android/camera/dualvideo/render/O000O0o;

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/render/O00000oo;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O00000oo;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    return-void
.end method

.method public expandBottom()V
    .locals 5
    .annotation build Landroid/annotation/SuppressLint;
        value = {
            "SwitchIntDef"
        }
    .end annotation

    const-string v0, "CameraItemManager"

    const-string v1, "expandBottom: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->isAnimating()Z

    move-result v0

    if-eqz v0, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :goto_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_4

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-interface {v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->isVisible()Z

    move-result v2

    if-nez v2, :cond_1

    goto :goto_0

    :cond_1
    sget-object v2, Lcom/android/camera/dualvideo/render/CameraItemManager$1;->$SwitchMap$com$android$camera$dualvideo$render$LayoutType:[I

    invoke-interface {v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/Enum;->ordinal()I

    move-result v3

    aget v2, v2, v3

    const/4 v3, 0x1

    if-eq v2, v3, :cond_3

    const/4 v4, 0x2

    if-eq v2, v4, :cond_3

    const/4 v4, 0x3

    if-eq v2, v4, :cond_2

    const/4 v4, 0x4

    if-eq v2, v4, :cond_2

    goto :goto_0

    :cond_2
    sget-object v2, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_1

    :cond_3
    sget-object v2, Lcom/android/camera/dualvideo/render/LayoutType;->FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    :goto_1
    iget-object v4, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {v1, v2, v4, v3}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setRenderLayoutTypeWithAnim(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/RegionHelper;Z)V

    goto :goto_0

    :cond_4
    return-void
.end method

.method public expandOrShrinkTop()V
    .locals 7
    .annotation build Landroid/annotation/SuppressLint;
        value = {
            "SwitchIntDef"
        }
    .end annotation

    const-string v0, "CameraItemManager"

    const-string v1, "expandOrShrinkTop: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->u(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->isAnimating()Z

    move-result v0

    if-nez v0, :cond_8

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v0

    if-eqz v0, :cond_0

    goto/16 :goto_3

    :cond_0
    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getRecordType()Lcom/android/camera/dualvideo/recorder/RecordType;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/recorder/RecordType;->MERGED:Lcom/android/camera/dualvideo/recorder/RecordType;

    const/4 v2, 0x1

    if-ne v0, v1, :cond_1

    move v0, v2

    goto :goto_0

    :cond_1
    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v1

    :goto_1
    invoke-interface {v1}, Ljava/util/Iterator;->hasNext()Z

    move-result v3

    if-eqz v3, :cond_8

    invoke-interface {v1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-interface {v3}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->isVisible()Z

    move-result v4

    if-nez v4, :cond_2

    goto :goto_1

    :cond_2
    invoke-interface {v3}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getLastRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v4

    sget-object v5, Lcom/android/camera/dualvideo/render/CameraItemManager$1;->$SwitchMap$com$android$camera$dualvideo$render$LayoutType:[I

    invoke-interface {v3}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/Enum;->ordinal()I

    move-result v6

    aget v5, v5, v6

    packed-switch v5, :pswitch_data_0

    goto :goto_1

    :pswitch_0
    sget-object v5, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    if-eq v4, v5, :cond_3

    sget-object v5, Lcom/android/camera/dualvideo/render/LayoutType;->FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    if-eq v4, v5, :cond_3

    invoke-virtual {v4}, Lcom/android/camera/dualvideo/render/LayoutType;->isSelectWindowType()Z

    move-result v5

    if-eqz v5, :cond_7

    :cond_3
    if-eqz v0, :cond_4

    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_2

    :cond_4
    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_2

    :pswitch_1
    sget-object v5, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    if-eq v4, v5, :cond_5

    sget-object v5, Lcom/android/camera/dualvideo/render/LayoutType;->FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    if-eq v4, v5, :cond_5

    invoke-virtual {v4}, Lcom/android/camera/dualvideo/render/LayoutType;->isSelectWindowType()Z

    move-result v5

    if-eqz v5, :cond_7

    :cond_5
    if-eqz v0, :cond_6

    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->UP:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_2

    :cond_6
    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->UP_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_2

    :pswitch_2
    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_2

    :pswitch_3
    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    :cond_7
    :goto_2
    iget-object v5, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {v3, v4, v5, v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setRenderLayoutTypeWithAnim(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/RegionHelper;Z)V

    goto :goto_1

    :cond_8
    :goto_3
    return-void

    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_3
        :pswitch_3
        :pswitch_2
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method public getFullTypeRecordAttri(Lcom/android/camera/dualvideo/util/RenderSourceType;)Lcom/android/camera/effect/draw_mode/DrawAttribute;
    .locals 9

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    invoke-virtual {v0, p1}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    move-object v2, v0

    check-cast v2, Lcom/android/gallery3d/ui/ExtTexture;

    new-instance v0, Landroid/graphics/Rect;

    sget-object v1, Lcom/android/camera/dualvideo/render/RenderUtil;->OUTPUT_SIZE:Landroid/util/Size;

    invoke-virtual {v1}, Landroid/util/Size;->getWidth()I

    move-result v1

    sget-object v3, Lcom/android/camera/dualvideo/render/RenderUtil;->OUTPUT_SIZE:Landroid/util/Size;

    invoke-virtual {v3}, Landroid/util/Size;->getHeight()I

    move-result v3

    const/4 v4, 0x0

    invoke-direct {v0, v4, v4, v1, v3}, Landroid/graphics/Rect;-><init>(IIII)V

    new-instance v8, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;

    invoke-direct {p0, p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->getFaceTypeByCameraTag(Lcom/android/camera/dualvideo/util/RenderSourceType;)Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/render/LayoutType;->FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-static {p0, p1, v2, v0}, Lcom/android/camera/dualvideo/render/RenderUtil;->generatePreviewTransMatrix(Lcom/android/camera/dualvideo/render/FaceType;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/gallery3d/ui/BasicTexture;Landroid/graphics/Rect;)[F

    move-result-object v3

    const/4 v5, 0x0

    sget-object p0, Lcom/android/camera/dualvideo/render/RenderUtil;->OUTPUT_SIZE:Landroid/util/Size;

    invoke-virtual {p0}, Landroid/util/Size;->getWidth()I

    move-result v6

    sget-object p0, Lcom/android/camera/dualvideo/render/RenderUtil;->OUTPUT_SIZE:Landroid/util/Size;

    invoke-virtual {p0}, Landroid/util/Size;->getHeight()I

    move-result v7

    move-object v1, v8

    invoke-direct/range {v1 .. v7}, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;-><init>(Lcom/android/gallery3d/ui/ExtTexture;[FIIII)V

    return-object v8
.end method

.method public getRenderableList()Ljava/util/ArrayList;
    .locals 1

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->initRenderableList()V

    :cond_0
    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    return-object p0
.end method

.method public getRenderableListForRecord()Ljava/util/List;
    .locals 4

    const/4 v0, 0x1

    invoke-static {v0}, Lcom/android/camera/Util;->getDisplayRect(I)Landroid/graphics/Rect;

    move-result-object v0

    invoke-virtual {v0}, Landroid/graphics/Rect;->width()I

    move-result v0

    sget-object v1, Lcom/android/camera/dualvideo/render/RenderUtil;->OUTPUT_SIZE:Landroid/util/Size;

    invoke-virtual {v1}, Landroid/util/Size;->getWidth()I

    move-result v1

    if-eq v0, v1, :cond_0

    int-to-float v1, v1

    int-to-float v0, v0

    div-float/2addr v1, v0

    goto :goto_0

    :cond_0
    const/high16 v1, 0x3f800000    # 1.0f

    :goto_0
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->getRenderableList()Ljava/util/ArrayList;

    move-result-object v2

    invoke-virtual {v2}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v2

    sget-object v3, Lcom/android/camera/dualvideo/render/O000OOoo;->INSTANCE:Lcom/android/camera/dualvideo/render/O000OOoo;

    invoke-interface {v2, v3}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object v2

    sget-object v3, Lcom/android/camera/dualvideo/render/O000ooO;->INSTANCE:Lcom/android/camera/dualvideo/render/O000ooO;

    invoke-interface {v2, v3}, Ljava/util/stream/Stream;->sorted(Ljava/util/Comparator;)Ljava/util/stream/Stream;

    move-result-object v2

    new-instance v3, Lcom/android/camera/dualvideo/render/O0000ooO;

    invoke-direct {v3, p0, v1, v0}, Lcom/android/camera/dualvideo/render/O0000ooO;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;FLjava/util/List;)V

    invoke-interface {v2, v3}, Ljava/util/stream/Stream;->forEachOrdered(Ljava/util/function/Consumer;)V

    return-object v0
.end method

.method public getVisibleRenderList()Ljava/util/ArrayList;
    .locals 1

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->getRenderableList()Ljava/util/ArrayList;

    move-result-object p0

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/O000OOoo;->INSTANCE:Lcom/android/camera/dualvideo/render/O000OOoo;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/O000Oo00;->INSTANCE:Lcom/android/camera/dualvideo/render/O000Oo00;

    invoke-static {v0}, Ljava/util/stream/Collectors;->toCollection(Ljava/util/function/Supplier;)Ljava/util/stream/Collector;

    move-result-object v0

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->collect(Ljava/util/stream/Collector;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/util/ArrayList;

    return-object p0
.end method

.method public hasMiniCameraItem()Z
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/O0000oO;->INSTANCE:Lcom/android/camera/dualvideo/render/O0000oO;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result p0

    return p0
.end method

.method public is6PatchWindow()Z
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/O0000o0;->INSTANCE:Lcom/android/camera/dualvideo/render/O0000o0;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result p0

    return p0
.end method

.method public isAnimating()Z
    .locals 1

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/render/O000000o;->INSTANCE:Lcom/android/camera/dualvideo/render/O000000o;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result p0

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public printRenderList()V
    .locals 2

    const-string v0, "CameraItemManager"

    const-string v1, "printRenderList: start"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    sget-object v0, Lcom/android/camera/dualvideo/render/O00000o;->INSTANCE:Lcom/android/camera/dualvideo/render/O00000o;

    invoke-virtual {p0, v0}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    :cond_0
    return-void
.end method

.method public reinitRenderableList()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->clear()V

    return-void
.end method

.method public selectItem(Landroid/view/MotionEvent;)V
    .locals 5

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "selectItem: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getAction()I

    move-result v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const-string v1, "CameraItemManager"

    invoke-static {v1, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getActionMasked()I

    move-result v0

    if-eqz v0, :cond_1

    const/4 v1, 0x1

    if-eq v0, v1, :cond_0

    const/4 v1, 0x3

    if-eq v0, v1, :cond_0

    goto :goto_1

    :cond_0
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

    move-result v0

    float-to-int v0, v0

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

    move-result p1

    float-to-int p1, p1

    invoke-direct {p0, v0, p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->onTouched(II)Z

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/render/O000oo0O;->INSTANCE:Lcom/android/camera/dualvideo/render/O000oo0O;

    invoke-interface {p0, p1}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/render/O000OO;->INSTANCE:Lcom/android/camera/dualvideo/render/O000OO;

    invoke-interface {p0, p1}, Ljava/util/stream/Stream;->forEachOrdered(Ljava/util/function/Consumer;)V

    goto :goto_1

    :cond_1
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

    move-result v0

    float-to-int v0, v0

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

    move-result p1

    float-to-int p1, p1

    iget-object v1, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v1}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v1

    :cond_2
    :goto_0
    invoke-interface {v1}, Ljava/util/Iterator;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_3

    invoke-interface {v1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    iget-object v3, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v4

    invoke-virtual {v3, v4}, Lcom/android/camera/dualvideo/render/RegionHelper;->getRenderAreaFor(Lcom/android/camera/dualvideo/render/LayoutType;)Landroid/graphics/Rect;

    move-result-object v3

    invoke-virtual {v3, v0, p1}, Landroid/graphics/Rect;->contains(II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-interface {v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->onKeyDown()V

    goto :goto_0

    :cond_3
    :goto_1
    return-void
.end method

.method public setTexture(Lcom/android/camera/dualvideo/util/RenderSourceType;Lcom/android/gallery3d/ui/ExtTexture;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mTextureMap:Ljava/util/HashMap;

    if-nez p2, :cond_0

    invoke-virtual {p0, p1}, Ljava/util/HashMap;->remove(Ljava/lang/Object;)Ljava/lang/Object;

    goto :goto_0

    :cond_0
    invoke-virtual {p0, p1, p2}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :goto_0
    return-void
.end method

.method public switchRecordToSelectWindow()V
    .locals 2

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->isAnimating()Z

    move-result v0

    if-eqz v0, :cond_0

    return-void

    :cond_0
    const-string v0, "CameraItemManager"

    const-string v1, "switchPreviewTo6Patch: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-static {}, Lcom/android/camera/dualvideo/ModuleUtil;->getUIStyle()I

    move-result v1

    invoke-static {v1}, Lcom/android/camera/Util;->getDisplayRect(I)Landroid/graphics/Rect;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/android/camera/dualvideo/render/RegionHelper;->setDrawRect(Landroid/graphics/Rect;)V

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/dualvideo/render/O000O0Oo;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O000O0Oo;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/dualvideo/render/O0000o0O;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O0000o0O;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    return-void
.end method

.method public switchSelectToRecordWindow()V
    .locals 2

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->isAnimating()Z

    move-result v0

    if-eqz v0, :cond_0

    return-void

    :cond_0
    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->printRenderList()V

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-static {}, Lcom/android/camera/dualvideo/ModuleUtil;->getUIStyle()I

    move-result v1

    invoke-static {v1}, Lcom/android/camera/Util;->getDisplayRect(I)Landroid/graphics/Rect;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/android/camera/dualvideo/render/RegionHelper;->setDrawRect(Landroid/graphics/Rect;)V

    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object v0

    sget-object v1, Lcom/android/camera/dualvideo/render/O0000OoO;->INSTANCE:Lcom/android/camera/dualvideo/render/O0000OoO;

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->initSelectIndexFromSelectData()V

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/dualvideo/render/O00oOooo;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O00oOooo;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    return-void
.end method

.method public switchTopBottom()Z
    .locals 5
    .annotation build Landroid/annotation/SuppressLint;
        value = {
            "SwitchIntDef"
        }
    .end annotation

    const-string v0, "CameraItemManager"

    const-string v1, "switchTopBottom "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->isAnimating()Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_1
    :goto_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_4

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-interface {v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->isVisible()Z

    move-result v3

    if-nez v3, :cond_2

    goto :goto_0

    :cond_2
    sget-object v3, Lcom/android/camera/dualvideo/render/CameraItemManager$1;->$SwitchMap$com$android$camera$dualvideo$render$LayoutType:[I

    invoke-interface {v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v4

    invoke-virtual {v4}, Ljava/lang/Enum;->ordinal()I

    move-result v4

    aget v3, v3, v4

    packed-switch v3, :pswitch_data_0

    goto :goto_2

    :pswitch_0
    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_1

    :pswitch_1
    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_1

    :pswitch_2
    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_1

    :pswitch_3
    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_1

    :pswitch_4
    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->UP_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    goto :goto_1

    :pswitch_5
    sget-object v3, Lcom/android/camera/dualvideo/render/LayoutType;->UP:Lcom/android/camera/dualvideo/render/LayoutType;

    :goto_1
    iget-object v4, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-interface {v2, v3, v4, v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setRenderLayoutTypeWithAnim(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/render/RegionHelper;Z)V

    :goto_2
    invoke-interface {v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v3

    sget-object v4, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne v3, v4, :cond_3

    sget-object v3, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    :goto_3
    invoke-interface {v2, v3, v1}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    goto :goto_0

    :cond_3
    invoke-interface {v2}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->getSelectedIndex()Lcom/android/camera/dualvideo/util/SelectIndex;

    move-result-object v3

    sget-object v4, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    if-ne v3, v4, :cond_1

    sget-object v3, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    goto :goto_3

    :cond_4
    const/4 p0, 0x1

    return p0

    nop

    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method public updateCameraItemList()V
    .locals 4

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->size()I

    move-result v0

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getConfigs()Ljava/util/ArrayList;

    move-result-object v1

    invoke-virtual {v1}, Ljava/util/ArrayList;->size()I

    move-result v1

    if-ne v0, v1, :cond_0

    return-void

    :cond_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->size()I

    move-result v0

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getConfigs()Ljava/util/ArrayList;

    move-result-object v1

    invoke-virtual {v1}, Ljava/util/ArrayList;->size()I

    move-result v1

    if-le v0, v1, :cond_1

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    sget-object v1, Lcom/android/camera/dualvideo/render/O000OO00;->INSTANCE:Lcom/android/camera/dualvideo/render/O000OO00;

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->removeIf(Ljava/util/function/Predicate;)Z

    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->updateRenderableList()V

    :cond_1
    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->size()I

    move-result v0

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getConfigs()Ljava/util/ArrayList;

    move-result-object v1

    invoke-virtual {v1}, Ljava/util/ArrayList;->size()I

    move-result v1

    if-ge v0, v1, :cond_2

    invoke-direct {p0}, Lcom/android/camera/dualvideo/render/CameraItemManager;->updateRenderableList()V

    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getConfigs()Ljava/util/ArrayList;

    move-result-object v0

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/render/O000O00o;

    invoke-direct {v1, p0}, Lcom/android/camera/dualvideo/render/O000O00o;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->forEachOrdered(Ljava/util/function/Consumer;)V

    :cond_2
    invoke-static {}, Lcom/android/camera/CameraSettings;->getDualVideoConfig()Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->getSelectedData()Ljava/util/ArrayList;

    move-result-object v0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRenderableList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_4

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v2

    new-instance v3, Lcom/android/camera/dualvideo/render/O000O0OO;

    invoke-direct {v3, v1}, Lcom/android/camera/dualvideo/render/O000O0OO;-><init>(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V

    invoke-interface {v2, v3}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result v2

    if-eqz v2, :cond_3

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v2

    new-instance v3, Lcom/android/camera/dualvideo/render/O0000Oo0;

    invoke-direct {v3, v1}, Lcom/android/camera/dualvideo/render/O0000Oo0;-><init>(Lcom/android/camera/dualvideo/render/CameraItemInterface;)V

    invoke-interface {v2, v3}, Ljava/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    goto :goto_0

    :cond_3
    sget-object v2, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v3, 0x1

    invoke-interface {v1, v2, v3}, Lcom/android/camera/dualvideo/render/CameraItemInterface;->setSelectTypeWithAnim(Lcom/android/camera/dualvideo/util/SelectIndex;Z)V

    goto :goto_0

    :cond_4
    return-void
.end method

.method public updateMiniWindowLocation(Landroid/view/MotionEvent;)Z
    .locals 7

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getActionMasked()I

    move-result v0

    const/4 v1, 0x0

    const/4 v2, 0x1

    if-eqz v0, :cond_4

    if-eq v0, v2, :cond_2

    const/4 v3, 0x2

    if-eq v0, v3, :cond_0

    const/4 p1, 0x3

    if-eq v0, p1, :cond_2

    return v1

    :cond_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    iget-boolean v0, v0, Lcom/android/camera/dualvideo/render/RegionHelper;->mIsHovering:Z

    if-eqz v0, :cond_1

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

    move-result v0

    iget-object v1, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    iget v1, v1, Lcom/android/camera/dualvideo/render/RegionHelper;->mStartX:F

    sub-float/2addr v0, v1

    float-to-int v0, v0

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

    move-result v1

    iget-object v3, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    iget v4, v3, Lcom/android/camera/dualvideo/render/RegionHelper;->mStartY:F

    sub-float/2addr v1, v4

    float-to-int v1, v1

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

    move-result v4

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

    move-result p1

    invoke-virtual {v3, v4, p1}, Lcom/android/camera/dualvideo/render/RegionHelper;->setStartPosition(FF)V

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-virtual {p0, v0, v1}, Lcom/android/camera/dualvideo/render/RegionHelper;->updateMarginOffset(II)V

    return v2

    :cond_1
    return v1

    :cond_2
    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    iget-boolean p1, p0, Lcom/android/camera/dualvideo/render/RegionHelper;->mIsHovering:Z

    if-eqz p1, :cond_3

    iput-boolean v1, p0, Lcom/android/camera/dualvideo/render/RegionHelper;->mIsHovering:Z

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/render/RegionHelper;->moveToEdge()V

    return v2

    :cond_3
    return v1

    :cond_4
    invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

    move-result v0

    invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

    move-result p1

    iget-object v3, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    sget-object v4, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-virtual {v3, v4}, Lcom/android/camera/dualvideo/render/RegionHelper;->getRenderAreaFor(Lcom/android/camera/dualvideo/render/LayoutType;)Landroid/graphics/Rect;

    move-result-object v3

    iget v4, v3, Landroid/graphics/Rect;->left:I

    int-to-float v5, v4

    cmpl-float v5, v0, v5

    if-lez v5, :cond_5

    invoke-virtual {v3}, Landroid/graphics/Rect;->width()I

    move-result v5

    add-int/2addr v4, v5

    int-to-float v4, v4

    cmpg-float v4, v0, v4

    if-gez v4, :cond_5

    move v4, v2

    goto :goto_0

    :cond_5
    move v4, v1

    :goto_0
    iget v5, v3, Landroid/graphics/Rect;->top:I

    int-to-float v6, v5

    cmpl-float v6, p1, v6

    if-lez v6, :cond_6

    invoke-virtual {v3}, Landroid/graphics/Rect;->height()I

    move-result v3

    add-int/2addr v5, v3

    int-to-float v3, v5

    cmpg-float v3, p1, v3

    if-gez v3, :cond_6

    move v3, v2

    goto :goto_1

    :cond_6
    move v3, v1

    :goto_1
    if-eqz v4, :cond_7

    if-eqz v3, :cond_7

    iget-object v1, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    iput-boolean v2, v1, Lcom/android/camera/dualvideo/render/RegionHelper;->mIsHovering:Z

    new-instance v3, Lcom/android/camera/dualvideo/render/O0000oo;

    invoke-direct {v3, p0}, Lcom/android/camera/dualvideo/render/O0000oo;-><init>(Lcom/android/camera/dualvideo/render/CameraItemManager;)V

    invoke-virtual {v1, v3}, Lcom/android/camera/dualvideo/render/RegionHelper;->setListener(Lcom/android/camera/dualvideo/render/RegionHelper$UpdatedListener;)V

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItemManager;->mRegionHelper:Lcom/android/camera/dualvideo/render/RegionHelper;

    invoke-virtual {p0, v0, p1}, Lcom/android/camera/dualvideo/render/RegionHelper;->setStartPosition(FF)V

    return v2

    :cond_7
    return v1
.end method

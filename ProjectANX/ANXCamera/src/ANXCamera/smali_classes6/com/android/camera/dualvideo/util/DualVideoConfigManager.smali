.class public Lcom/android/camera/dualvideo/util/DualVideoConfigManager;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final FRONT_NAME:Ljava/lang/String; = "front"

.field public static final REMOTE_CAMERA_ID:I = 0x3e8

.field public static final REMOTE_NAME:Ljava/lang/String; = "remote"

.field private static final TAG:Ljava/lang/String; = "CameraIDManager"

.field private static mManager:Lcom/android/camera/dualvideo/util/DualVideoConfigManager;


# instance fields
.field private mConfigs:Ljava/util/ArrayList;


# direct methods
.method private constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-direct {p0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->initConfig()V

    return-void
.end method

.method static synthetic O000000o(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mPresentZoom:F

    iget p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mPresentZoom:F

    invoke-static {p0, p1}, Ljava/lang/Float;->compare(FF)I

    move-result p0

    return p0
.end method

.method static synthetic O000000o(ILcom/android/camera/dualvideo/render/LayoutType;)Z
    .locals 1

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/render/LayoutType;->getIndex()I

    move-result p1

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/render/LayoutType;->getIndex()I

    move-result v0

    add-int/2addr v0, p0

    if-ne p1, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O000000o(ILcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mCameraId:I

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000Oo(Ljava/util/concurrent/ConcurrentHashMap;Ljava/lang/Integer;)V
    .locals 2

    invoke-virtual {p1}, Ljava/lang/Integer;->intValue()I

    move-result v0

    const/16 v1, 0x3e8

    if-ne v0, v1, :cond_0

    sget-object v0, Lcom/android/camera/dualvideo/util/RenderSourceType;->REMOTE:Lcom/android/camera/dualvideo/util/RenderSourceType;

    goto :goto_0

    :cond_0
    sget-object v0, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    :goto_0
    invoke-virtual {p0, v0, p1}, Ljava/util/concurrent/ConcurrentHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method

.method static synthetic O00000Oo(ILcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mCameraId:I

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000Oo(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000o(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000o0(ILcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mCameraId:I

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000o0(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O00000oO(Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 0

    iget-object p1, p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O0000O0o(Ljava/lang/Integer;)Z
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

.method static synthetic O0000OOo(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Ljava/lang/Integer;
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mCameraId:I

    invoke-static {p0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

.method static synthetic O0000Oo(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Lcom/android/camera/dualvideo/render/FaceType;
    .locals 0

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object p0

    return-object p0
.end method

.method static synthetic O0000Oo0(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mDescription:Ljava/lang/String;

    return-object p0
.end method

.method static synthetic O0000OoO(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method static synthetic O0000Ooo(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Ljava/lang/Float;
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mRelativeZoom:F

    invoke-static {p0}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object p0

    return-object p0
.end method

.method static synthetic O0000o0(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Ljava/lang/Float;
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mRelativeZoom:F

    invoke-static {p0}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object p0

    return-object p0
.end method

.method static synthetic O0000o00(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Ljava/lang/Float;
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mPresentZoom:F

    invoke-static {p0}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object p0

    return-object p0
.end method

.method private getDescription(Ljava/util/List;)Ljava/lang/String;
    .locals 8

    const/4 p0, 0x1

    invoke-interface {p1, p0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->hashCode()I

    move-result v1

    const/16 v2, 0xe9f

    const/4 v3, 0x0

    const-string v4, "front"

    const/4 v5, 0x4

    const/4 v6, 0x3

    const/4 v7, 0x2

    if-eq v1, v2, :cond_4

    const/16 v2, 0xea2

    if-eq v1, v2, :cond_3

    const v2, 0x3643aa

    if-eq v1, v2, :cond_2

    const v2, 0x37aed3

    if-eq v1, v2, :cond_1

    const v2, 0x5d2de29

    if-eq v1, v2, :cond_0

    goto :goto_0

    :cond_0
    invoke-virtual {v0, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    move v0, v5

    goto :goto_1

    :cond_1
    const-string/jumbo v1, "wide"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    move v0, p0

    goto :goto_1

    :cond_2
    const-string v1, "tele"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    move v0, v7

    goto :goto_1

    :cond_3
    const-string v1, "uw"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    move v0, v3

    goto :goto_1

    :cond_4
    const-string v1, "ut"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    move v0, v6

    goto :goto_1

    :cond_5
    :goto_0
    const/4 v0, -0x1

    :goto_1
    if-eqz v0, :cond_7

    if-eq v0, p0, :cond_7

    if-eq v0, v7, :cond_7

    if-eq v0, v6, :cond_7

    if-ne v0, v5, :cond_6

    return-object v4

    :cond_6
    new-instance v0, Ljava/lang/RuntimeException;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "config error: "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-interface {p1, p0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/String;

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw v0

    :cond_7
    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-interface {p1, v3}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Ljava/lang/String;

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, "X"

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method private getIDByConfig(Ljava/lang/String;)I
    .locals 5

    invoke-virtual {p1}, Ljava/lang/String;->hashCode()I

    move-result p0

    const/16 v0, 0xe9f

    const/4 v1, 0x4

    const/4 v2, 0x3

    const/4 v3, 0x2

    const/4 v4, 0x1

    if-eq p0, v0, :cond_4

    const/16 v0, 0xea2

    if-eq p0, v0, :cond_3

    const v0, 0x3643aa

    if-eq p0, v0, :cond_2

    const v0, 0x37aed3

    if-eq p0, v0, :cond_1

    const v0, 0x5d2de29

    if-eq p0, v0, :cond_0

    goto :goto_0

    :cond_0
    const-string p0, "front"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_5

    move p0, v1

    goto :goto_1

    :cond_1
    const-string/jumbo p0, "wide"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_5

    move p0, v4

    goto :goto_1

    :cond_2
    const-string p0, "tele"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_5

    move p0, v3

    goto :goto_1

    :cond_3
    const-string p0, "uw"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_5

    const/4 p0, 0x0

    goto :goto_1

    :cond_4
    const-string p0, "ut"

    invoke-virtual {p1, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_5

    move p0, v2

    goto :goto_1

    :cond_5
    :goto_0
    const/4 p0, -0x1

    :goto_1
    if-eqz p0, :cond_a

    if-eq p0, v4, :cond_9

    if-eq p0, v3, :cond_8

    if-eq p0, v2, :cond_7

    if-ne p0, v1, :cond_6

    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getFrontCameraId()I

    move-result p0

    return p0

    :cond_6
    new-instance p0, Ljava/lang/RuntimeException;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "config error: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_7
    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getUltraTeleCameraId()I

    move-result p0

    return p0

    :cond_8
    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getAuxCameraId()I

    move-result p0

    return p0

    :cond_9
    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getMainBackCameraId()I

    move-result p0

    return p0

    :cond_a
    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getUltraWideCameraId()I

    move-result p0

    return p0
.end method

.method private getLayoutType(I)Lcom/android/camera/dualvideo/render/LayoutType;
    .locals 1

    invoke-static {}, Lcom/android/camera/dualvideo/render/LayoutType;->values()[Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    invoke-static {p0}, Ljava/util/stream/Stream;->of([Ljava/lang/Object;)Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/util/O0000o;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/util/O0000o;-><init>(I)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    invoke-virtual {p0}, Ljava/util/Optional;->get()Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/render/LayoutType;

    return-object p0
.end method

.method private getZoomById(I)F
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/util/O00000oo;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/util/O00000oo;-><init>(I)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/util/O00000o0;->INSTANCE:Lcom/android/camera/dualvideo/util/O00000o0;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object p0

    const/high16 p1, 0x3f800000    # 1.0f

    invoke-static {p1}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Float;

    invoke-virtual {p0}, Ljava/lang/Float;->floatValue()F

    move-result p0

    return p0
.end method

.method private initConfig()V
    .locals 14

    iget-object v0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0ooOOO()Ljava/lang/String;

    move-result-object v0

    const/4 v1, 0x1

    const/4 v2, 0x0

    if-nez v0, :cond_1

    iget-object v0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    new-instance v10, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-direct {p0, v2}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getLayoutType(I)Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v5

    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object v2

    invoke-virtual {v2}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getMainBackCameraId()I

    move-result v6

    const/high16 v7, 0x3f800000    # 1.0f

    const/high16 v8, 0x3f800000    # 1.0f

    const-string v9, "1X"

    move-object v3, v10

    move-object v4, p0

    invoke-direct/range {v3 .. v9}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;-><init>(Lcom/android/camera/dualvideo/util/DualVideoConfigManager;Lcom/android/camera/dualvideo/render/LayoutType;IFFLjava/lang/String;)V

    invoke-virtual {v0, v10}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    iget-object v0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    new-instance v9, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-direct {p0, v1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getLayoutType(I)Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v4

    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object v1

    invoke-virtual {v1}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getFrontCameraId()I

    move-result v5

    const/high16 v6, 0x3f800000    # 1.0f

    const-string v8, "front"

    move-object v2, v9

    move-object v3, p0

    invoke-direct/range {v2 .. v8}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;-><init>(Lcom/android/camera/dualvideo/util/DualVideoConfigManager;Lcom/android/camera/dualvideo/render/LayoutType;IFFLjava/lang/String;)V

    invoke-virtual {v0, v9}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    goto :goto_1

    :cond_1
    sget-object v3, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {v0, v3}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v0

    const-string v3, ";"

    invoke-virtual {v0, v3}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v0

    move v3, v2

    :goto_0
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v4

    if-eqz v4, :cond_2

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/String;

    sget-object v5, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {v4, v5}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v4

    const-string v5, ":"

    invoke-virtual {v4, v5}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v4

    invoke-static {v4}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object v4

    iget-object v5, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    new-instance v13, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-direct {p0, v3}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getLayoutType(I)Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v8

    invoke-interface {v4, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/lang/String;

    invoke-direct {p0, v6}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getIDByConfig(Ljava/lang/String;)I

    move-result v9

    invoke-interface {v4, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/lang/String;

    invoke-static {v6}, Ljava/lang/Float;->parseFloat(Ljava/lang/String;)F

    move-result v10

    const/4 v6, 0x2

    invoke-interface {v4, v6}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/lang/String;

    invoke-static {v6}, Ljava/lang/Float;->parseFloat(Ljava/lang/String;)F

    move-result v11

    invoke-direct {p0, v4}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getDescription(Ljava/util/List;)Ljava/lang/String;

    move-result-object v12

    move-object v6, v13

    move-object v7, p0

    invoke-direct/range {v6 .. v12}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;-><init>(Lcom/android/camera/dualvideo/util/DualVideoConfigManager;Lcom/android/camera/dualvideo/render/LayoutType;IFFLjava/lang/String;)V

    invoke-virtual {v5, v13}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    add-int/2addr v3, v1

    goto :goto_0

    :cond_2
    :goto_1
    iget-object v0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    sget-object v1, Lcom/android/camera/dualvideo/util/O0000OoO;->INSTANCE:Lcom/android/camera/dualvideo/util/O0000OoO;

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->sort(Ljava/util/Comparator;)V

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "getSelectWindowConfig:"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    const-string v0, "CameraIDManager"

    invoke-static {v0, p0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method public static declared-synchronized instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;
    .locals 2

    const-class v0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    monitor-enter v0

    :try_start_0
    sget-object v1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mManager:Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    if-nez v1, :cond_0

    new-instance v1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    invoke-direct {v1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;-><init>()V

    sput-object v1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mManager:Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    :cond_0
    sget-object v1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mManager:Lcom/android/camera/dualvideo/util/DualVideoConfigManager;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    monitor-exit v0

    return-object v1

    :catchall_0
    move-exception v1

    monitor-exit v0

    throw v1
.end method

.method public static sortId([Ljava/lang/Integer;)Ljava/util/concurrent/ConcurrentHashMap;
    .locals 5

    array-length v0, p0

    const/4 v1, 0x1

    const/4 v2, 0x0

    const/4 v3, 0x2

    if-ne v0, v3, :cond_0

    move v0, v1

    goto :goto_0

    :cond_0
    move v0, v2

    :goto_0
    invoke-static {v0}, Lcom/android/camera/dualvideo/util/Assert;->check(Z)V

    new-instance v0, Ljava/util/concurrent/ConcurrentHashMap;

    invoke-direct {v0}, Ljava/util/concurrent/ConcurrentHashMap;-><init>()V

    invoke-static {p0}, Ljava/util/stream/Stream;->of([Ljava/lang/Object;)Ljava/util/stream/Stream;

    move-result-object v3

    sget-object v4, Lcom/android/camera/dualvideo/util/O0000Oo;->INSTANCE:Lcom/android/camera/dualvideo/util/O0000Oo;

    invoke-interface {v3, v4}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result v3

    if-eqz v3, :cond_1

    invoke-static {p0}, Ljava/util/stream/Stream;->of([Ljava/lang/Object;)Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v1, Lcom/android/camera/dualvideo/util/O0000O0o;

    invoke-direct {v1, v0}, Lcom/android/camera/dualvideo/util/O0000O0o;-><init>(Ljava/util/concurrent/ConcurrentHashMap;)V

    invoke-interface {p0, v1}, Ljava/util/stream/Stream;->forEach(Ljava/util/function/Consumer;)V

    return-object v0

    :cond_1
    sget-object v3, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    aget-object v2, p0, v2

    invoke-virtual {v0, v3, v2}, Ljava/util/concurrent/ConcurrentHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    sget-object v2, Lcom/android/camera/dualvideo/util/RenderSourceType;->SUB:Lcom/android/camera/dualvideo/util/RenderSourceType;

    aget-object p0, p0, v1

    invoke-virtual {v0, v2, p0}, Ljava/util/concurrent/ConcurrentHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    sget-object v1, Lcom/android/camera/dualvideo/util/RenderSourceType;->SUB:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {v0, v1}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Integer;->intValue()I

    move-result v1

    invoke-virtual {p0, v1}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->isFrontCameraId(I)Z

    move-result p0

    if-eqz p0, :cond_2

    return-object v0

    :cond_2
    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object p0

    sget-object v1, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {v0, v1}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Integer;->intValue()I

    move-result v1

    invoke-virtual {p0, v1}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->isFrontCameraId(I)Z

    move-result p0

    if-eqz p0, :cond_3

    :goto_1
    invoke-static {v0}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->switchMainSubId(Ljava/util/concurrent/ConcurrentHashMap;)V

    goto :goto_2

    :cond_3
    invoke-static {}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->instance()Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    move-result-object p0

    sget-object v1, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {v0, v1}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Integer;->intValue()I

    move-result v1

    invoke-direct {p0, v1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getZoomById(I)F

    move-result v1

    sget-object v2, Lcom/android/camera/dualvideo/util/RenderSourceType;->SUB:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {v0, v2}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Integer;

    invoke-virtual {v2}, Ljava/lang/Integer;->intValue()I

    move-result v2

    invoke-direct {p0, v2}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getZoomById(I)F

    move-result p0

    cmpl-float p0, p0, v1

    if-lez p0, :cond_4

    goto :goto_1

    :cond_4
    :goto_2
    return-object v0
.end method

.method private static switchMainSubId(Ljava/util/concurrent/ConcurrentHashMap;)V
    .locals 3

    sget-object v0, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {p0, v0}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    sget-object v1, Lcom/android/camera/dualvideo/util/RenderSourceType;->SUB:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-virtual {p0, v1}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Integer;->intValue()I

    move-result v1

    sget-object v2, Lcom/android/camera/dualvideo/util/RenderSourceType;->MAIN:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    invoke-virtual {p0, v2, v1}, Ljava/util/concurrent/ConcurrentHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    sget-object v1, Lcom/android/camera/dualvideo/util/RenderSourceType;->SUB:Lcom/android/camera/dualvideo/util/RenderSourceType;

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    invoke-virtual {p0, v1, v0}, Ljava/util/concurrent/ConcurrentHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    return-void
.end method


# virtual methods
.method public addExternalConfig(ILjava/lang/String;)Z
    .locals 9

    iget-object v0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {v0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v0

    new-instance v1, Lcom/android/camera/dualvideo/util/O00000Oo;

    invoke-direct {v1, p1}, Lcom/android/camera/dualvideo/util/O00000Oo;-><init>(I)V

    invoke-interface {v0, v1}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    new-instance v8, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-virtual {v0}, Ljava/util/ArrayList;->size()I

    move-result v1

    invoke-direct {p0, v1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->getLayoutType(I)Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v3

    const/high16 v5, 0x3f800000    # 1.0f

    const/high16 v6, 0x3f800000    # 1.0f

    move-object v1, v8

    move-object v2, p0

    move v4, p1

    move-object v7, p2

    invoke-direct/range {v1 .. v7}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;-><init>(Lcom/android/camera/dualvideo/util/DualVideoConfigManager;Lcom/android/camera/dualvideo/render/LayoutType;IFFLjava/lang/String;)V

    invoke-virtual {v0, v8}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    const/4 p0, 0x1

    return p0
.end method

.method public getCameraId(Lcom/android/camera/dualvideo/render/LayoutType;)I
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/util/O0000OOo;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/util/O0000OOo;-><init>(Lcom/android/camera/dualvideo/render/LayoutType;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/util/O00000o;->INSTANCE:Lcom/android/camera/dualvideo/util/O00000o;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object p0

    const/4 p1, -0x1

    invoke-static {p1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Integer;

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    return p0
.end method

.method public getConfigDescription(Lcom/android/camera/dualvideo/render/LayoutType;)Ljava/lang/String;
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/util/O0000o00;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/util/O0000o00;-><init>(Lcom/android/camera/dualvideo/render/LayoutType;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/util/O0000Ooo;->INSTANCE:Lcom/android/camera/dualvideo/util/O0000Ooo;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object p0

    const/4 p1, 0x0

    invoke-virtual {p0, p1}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/String;

    return-object p0
.end method

.method public getConfigs()Ljava/util/ArrayList;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    return-object p0
.end method

.method public getFaceType(Lcom/android/camera/dualvideo/render/LayoutType;)Lcom/android/camera/dualvideo/render/FaceType;
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/util/O0000o0O;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/util/O0000o0O;-><init>(Lcom/android/camera/dualvideo/render/LayoutType;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/util/O0000o0;->INSTANCE:Lcom/android/camera/dualvideo/util/O0000o0;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/render/FaceType;->FACE_BACK:Lcom/android/camera/dualvideo/render/FaceType;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/render/FaceType;

    return-object p0
.end method

.method public getMinZoom()F
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/util/O00000oO;->INSTANCE:Lcom/android/camera/dualvideo/util/O00000oO;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/util/O000000o;->INSTANCE:Lcom/android/camera/dualvideo/util/O000000o;

    invoke-virtual {p0, v0}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object p0

    const v0, 0x3f19999a    # 0.6f

    invoke-static {v0}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v0

    invoke-virtual {p0, v0}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Float;

    invoke-virtual {p0}, Ljava/lang/Float;->floatValue()F

    move-result p0

    return p0
.end method

.method public getPresentZoom(Lcom/android/camera/dualvideo/render/LayoutType;)F
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    new-instance v0, Lcom/android/camera/dualvideo/util/O0000o0o;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/util/O0000o0o;-><init>(Lcom/android/camera/dualvideo/render/LayoutType;)V

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/stream/Stream;->findFirst()Ljava/util/Optional;

    move-result-object p0

    sget-object p1, Lcom/android/camera/dualvideo/util/O0000oO0;->INSTANCE:Lcom/android/camera/dualvideo/util/O0000oO0;

    invoke-virtual {p0, p1}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object p0

    const/high16 p1, 0x3f800000    # 1.0f

    invoke-static {p1}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object p1

    invoke-virtual {p0, p1}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Float;

    invoke-virtual {p0}, Ljava/lang/Float;->floatValue()F

    move-result p0

    return p0
.end method

.method public removeExternalConfig(I)V
    .locals 1

    iget-object p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->mConfigs:Ljava/util/ArrayList;

    new-instance v0, Lcom/android/camera/dualvideo/util/O0000Oo0;

    invoke-direct {v0, p1}, Lcom/android/camera/dualvideo/util/O0000Oo0;-><init>(I)V

    invoke-virtual {p0, v0}, Ljava/util/ArrayList;->removeIf(Ljava/util/function/Predicate;)Z

    return-void
.end method

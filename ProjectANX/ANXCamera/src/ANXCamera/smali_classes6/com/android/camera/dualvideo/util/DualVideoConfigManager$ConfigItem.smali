.class public Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;
.super Ljava/lang/Object;
.source ""


# instance fields
.field public mCameraId:I

.field public mDescription:Ljava/lang/String;

.field public mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

.field public mPresentZoom:F

.field public mRelativeZoom:F

.field final synthetic this$0:Lcom/android/camera/dualvideo/util/DualVideoConfigManager;


# direct methods
.method constructor <init>(Lcom/android/camera/dualvideo/util/DualVideoConfigManager;Lcom/android/camera/dualvideo/render/LayoutType;IFFLjava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->this$0:Lcom/android/camera/dualvideo/util/DualVideoConfigManager;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p4, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mPresentZoom:F

    iput p3, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mCameraId:I

    iput p5, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mRelativeZoom:F

    iput-object p6, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mDescription:Ljava/lang/String;

    iput-object p2, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mLayoutType:Lcom/android/camera/dualvideo/render/LayoutType;

    return-void
.end method


# virtual methods
.method public getFaceType()Lcom/android/camera/dualvideo/render/FaceType;
    .locals 2

    iget v0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mCameraId:I

    const/16 v1, 0x3e8

    if-ne v0, v1, :cond_0

    sget-object p0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_REMOTE:Lcom/android/camera/dualvideo/render/FaceType;

    return-object p0

    :cond_0
    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object v0

    iget p0, p0, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;->mCameraId:I

    invoke-virtual {v0, p0}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->isFrontCameraId(I)Z

    move-result p0

    if-eqz p0, :cond_1

    sget-object p0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_FRONT:Lcom/android/camera/dualvideo/render/FaceType;

    return-object p0

    :cond_1
    sget-object p0, Lcom/android/camera/dualvideo/render/FaceType;->FACE_BACK:Lcom/android/camera/dualvideo/render/FaceType;

    return-object p0
.end method

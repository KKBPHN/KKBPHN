.class public Lcom/android/camera2/PortraitDepthMap;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final BLUR_LEVEL_START_ADDR:I = 0x10

.field private static final DAPTH_MAP_START_ADDR:I = 0x98

.field private static final DATA_LENGTH_4:I = 0x4

.field private static final DATA_LENGTH_START_ADDR:I = 0x94

.field private static final HEADER_LENGTH_START_ADDR:I = 0x4

.field private static final HEADER_START_ADDR:I = 0x0

.field private static final POINT_X_START_ADDR:I = 0x8

.field private static final POINT_Y_START_ADDR:I = 0xc

.field private static final TAG:Ljava/lang/String; = "PortraitDepthMap"

.field public static final TAG_DEPTH_MAP_BLUR_LEVEL:I
    .annotation runtime Ljava/lang/Deprecated;
    .end annotation
.end field

.field public static final TAG_DEPTH_MAP_FOCUS_POINT:I
    .annotation runtime Ljava/lang/Deprecated;
    .end annotation
.end field


# instance fields
.field private mDepthMapHeader:[B

.field private mDepthMapOriginalData:[B


# direct methods
.method private static synthetic $closeResource(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    .locals 0

    if-eqz p0, :cond_0

    :try_start_0
    invoke-interface {p1}, Ljava/lang/AutoCloseable;->close()V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    goto :goto_0

    :catchall_0
    move-exception p1

    invoke-virtual {p0, p1}, Ljava/lang/Throwable;->addSuppressed(Ljava/lang/Throwable;)V

    goto :goto_0

    :cond_0
    invoke-interface {p1}, Ljava/lang/AutoCloseable;->close()V

    :goto_0
    return-void
.end method

.method static constructor <clinit>()V
    .locals 2

    const/4 v0, 0x2

    const/16 v1, -0x7770

    invoke-static {v0, v1}, Lcom/android/gallery3d/exif/ExifInterface;->defineTag(IS)I

    move-result v1

    sput v1, Lcom/android/camera2/PortraitDepthMap;->TAG_DEPTH_MAP_FOCUS_POINT:I

    const/16 v1, -0x776f

    invoke-static {v0, v1}, Lcom/android/gallery3d/exif/ExifInterface;->defineTag(IS)I

    move-result v0

    sput v0, Lcom/android/camera2/PortraitDepthMap;->TAG_DEPTH_MAP_BLUR_LEVEL:I

    return-void
.end method

.method public constructor <init>([B)V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    if-eqz p1, :cond_1

    invoke-static {p1}, Lcom/android/camera2/PortraitDepthMap;->getHeaderTag([B)I

    move-result v0

    const/16 v1, 0x80

    if-ne v0, v1, :cond_0

    iput-object p1, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapOriginalData:[B

    invoke-virtual {p0}, Lcom/android/camera2/PortraitDepthMap;->getDepthMapHeader()[B

    move-result-object p1

    iput-object p1, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapHeader:[B

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Illegal depth format! 0x80 != "

    invoke-virtual {p1, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0

    :cond_1
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "Null depth data!"

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method private static getBytes([BII)[B
    .locals 2

    if-lez p2, :cond_0

    if-ltz p1, :cond_0

    array-length v0, p0

    sub-int/2addr v0, p1

    if-gt p2, v0, :cond_0

    new-array v0, p2, [B

    const/4 v1, 0x0

    invoke-static {p0, p1, v0, v1, p2}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    return-object v0

    :cond_0
    new-instance p0, Ljava/lang/IllegalArgumentException;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "WRONG ARGUMENT: from ="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p1, ", length = "

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method private static getHeaderTag([B)I
    .locals 2

    const/4 v0, 0x0

    const/4 v1, 0x4

    invoke-static {p0, v0, v1}, Lcom/android/camera2/PortraitDepthMap;->getBytes([BII)[B

    move-result-object p0

    invoke-static {p0}, Lcom/android/camera2/PortraitDepthMap;->getInteger([B)I

    move-result p0

    return p0
.end method

.method private static getInteger([B)I
    .locals 5

    array-length v0, p0

    const/4 v1, 0x4

    if-ne v0, v1, :cond_1

    const/4 v0, 0x0

    move v2, v0

    :goto_0
    if-ge v0, v1, :cond_0

    aget-byte v3, p0, v0

    and-int/lit16 v3, v3, 0xff

    mul-int/lit8 v4, v0, 0x8

    shl-int/2addr v3, v4

    add-int/2addr v2, v3

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return v2

    :cond_1
    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string v0, "bytes can not covert to a integer value!"

    invoke-direct {p0, v0}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public static isDepthMapData([B)Z
    .locals 2

    if-eqz p0, :cond_0

    array-length v0, p0

    const/4 v1, 0x4

    if-le v0, v1, :cond_0

    invoke-static {p0}, Lcom/android/camera2/PortraitDepthMap;->getHeaderTag([B)I

    move-result p0

    const/16 v0, 0x80

    if-ne p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    if-nez p0, :cond_1

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    const-string v1, "Illegal depthmap format"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    :cond_1
    return p0
.end method


# virtual methods
.method public getBlurLevel()I
    .locals 2

    iget-object p0, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapHeader:[B

    const/16 v0, 0x10

    const/4 v1, 0x4

    invoke-static {p0, v0, v1}, Lcom/android/camera2/PortraitDepthMap;->getBytes([BII)[B

    move-result-object p0

    invoke-static {p0}, Lcom/android/camera2/PortraitDepthMap;->getInteger([B)I

    move-result p0

    return p0
.end method

.method public getDepthMapData()[B
    .locals 2

    iget-object v0, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapOriginalData:[B

    invoke-virtual {p0}, Lcom/android/camera2/PortraitDepthMap;->getDepthMapLength()I

    move-result p0

    const/16 v1, 0x98

    invoke-static {v0, v1, p0}, Lcom/android/camera2/PortraitDepthMap;->getBytes([BII)[B

    move-result-object p0

    return-object p0
.end method

.method public getDepthMapHeader()[B
    .locals 2

    iget-object v0, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapOriginalData:[B

    const/4 v1, 0x4

    invoke-static {v0, v1, v1}, Lcom/android/camera2/PortraitDepthMap;->getBytes([BII)[B

    move-result-object v0

    invoke-static {v0}, Lcom/android/camera2/PortraitDepthMap;->getInteger([B)I

    move-result v0

    iget-object p0, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapOriginalData:[B

    const/4 v1, 0x0

    invoke-static {p0, v1, v0}, Lcom/android/camera2/PortraitDepthMap;->getBytes([BII)[B

    move-result-object p0

    return-object p0
.end method

.method public getDepthMapLength()I
    .locals 2

    iget-object p0, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapHeader:[B

    const/16 v0, 0x94

    const/4 v1, 0x4

    invoke-static {p0, v0, v1}, Lcom/android/camera2/PortraitDepthMap;->getBytes([BII)[B

    move-result-object p0

    invoke-static {p0}, Lcom/android/camera2/PortraitDepthMap;->getInteger([B)I

    move-result p0

    return p0
.end method

.method public getFocusPoint()Landroid/graphics/Point;
    .locals 3

    iget-object v0, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapHeader:[B

    const/4 v1, 0x4

    const/16 v2, 0x8

    invoke-static {v0, v2, v1}, Lcom/android/camera2/PortraitDepthMap;->getBytes([BII)[B

    move-result-object v0

    invoke-static {v0}, Lcom/android/camera2/PortraitDepthMap;->getInteger([B)I

    move-result v0

    iget-object p0, p0, Lcom/android/camera2/PortraitDepthMap;->mDepthMapHeader:[B

    const/16 v2, 0xc

    invoke-static {p0, v2, v1}, Lcom/android/camera2/PortraitDepthMap;->getBytes([BII)[B

    move-result-object p0

    invoke-static {p0}, Lcom/android/camera2/PortraitDepthMap;->getInteger([B)I

    move-result p0

    new-instance v1, Landroid/graphics/Point;

    invoke-direct {v1, v0, p0}, Landroid/graphics/Point;-><init>(II)V

    return-object v1
.end method

.method public getVendor()I
    .locals 0

    const/4 p0, 0x1

    return p0
.end method

.method public writePortraitExif(Lcom/android/camera2/PortraitDepthMapExif;)[B
    .locals 31

    invoke-virtual/range {p0 .. p0}, Lcom/android/camera2/PortraitDepthMap;->getFocusPoint()Landroid/graphics/Point;

    move-result-object v1

    invoke-virtual/range {p0 .. p0}, Lcom/android/camera2/PortraitDepthMap;->getBlurLevel()I

    move-result v2

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getPortraitLightingVersioin()I

    move-result v3

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCameraPreferredMode()I

    move-result v4

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getPictureInfo()Lcom/xiaomi/camera/core/PictureInfo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/xiaomi/camera/core/PictureInfo;->isFrontCamera()Z

    move-result v0

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->isSupportZeroDegreeOrientationImage()Z

    move-result v5

    if-eqz v5, :cond_0

    const/4 v5, 0x2

    goto :goto_0

    :cond_0
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v5

    invoke-virtual {v5}, LO00000Oo/O00000oO/O000000o/O00000o;->O0ooOO0()I

    move-result v5

    :goto_0
    const/16 v7, 0xa

    const/4 v8, 0x5

    if-lez v5, :cond_3

    if-eqz v0, :cond_2

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getPictureInfo()Lcom/xiaomi/camera/core/PictureInfo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/xiaomi/camera/core/PictureInfo;->isAiEnabled()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getPictureInfo()Lcom/xiaomi/camera/core/PictureInfo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/xiaomi/camera/core/PictureInfo;->getAiType()I

    move-result v0

    if-ne v0, v7, :cond_1

    const/16 v7, 0x46

    goto :goto_1

    :cond_1
    const/16 v7, 0x28

    goto :goto_1

    :cond_2
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getPictureInfo()Lcom/xiaomi/camera/core/PictureInfo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/xiaomi/camera/core/PictureInfo;->isAiEnabled()Z

    move-result v0

    if-eqz v0, :cond_4

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getPictureInfo()Lcom/xiaomi/camera/core/PictureInfo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/xiaomi/camera/core/PictureInfo;->getAiType()I

    move-result v0

    if-ne v0, v7, :cond_4

    const/16 v7, 0x1e

    goto :goto_1

    :cond_3
    const/4 v8, -0x1

    const/4 v7, -0x1

    :cond_4
    :goto_1
    const/4 v9, 0x1

    if-le v5, v9, :cond_5

    invoke-virtual/range {p0 .. p0}, Lcom/android/camera2/PortraitDepthMap;->getVendor()I

    move-result v0

    move v10, v0

    goto :goto_2

    :cond_5
    move v10, v9

    :goto_2
    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: focusPoint: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: blurLevel: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: shineThreshold: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: shineLevel: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: lightingPattern: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getLightingPattern()I

    move-result v12

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: isCinematicAspectRatio: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->isCinematicAspectRatio()Z

    move-result v12

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: rotation: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRotation()I

    move-result v12

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: vendor: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11, v10}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: portraitLightingVersion: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v12, "writePortraitExif: cameraPreferredMode: "

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v11, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    invoke-static {v0, v11}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const/4 v11, 0x0

    const/4 v12, 0x0

    :try_start_0
    new-instance v13, Ljava/io/ByteArrayOutputStream;

    invoke-direct {v13}, Ljava/io/ByteArrayOutputStream;-><init>()V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    :try_start_1
    new-instance v0, Lcom/android/gallery3d/exif/ExifInterface;

    invoke-direct {v0}, Lcom/android/gallery3d/exif/ExifInterface;-><init>()V

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpeg()[B

    move-result-object v14

    invoke-virtual {v0, v14}, Lcom/android/gallery3d/exif/ExifInterface;->readExif([B)V

    invoke-virtual {v0, v5}, Lcom/android/gallery3d/exif/ExifInterface;->addXiaomiDepthmapVersion(I)Z

    invoke-virtual {v0, v2}, Lcom/android/gallery3d/exif/ExifInterface;->addDepthMapBlurLevel(I)Z

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getLightingPattern()I

    move-result v14

    invoke-virtual {v0, v14}, Lcom/android/gallery3d/exif/ExifInterface;->addPortraitLighting(I)Z

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->isBokehFrontCamera()Z

    move-result v14

    if-eqz v14, :cond_7

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->isFrontMirror()Z

    move-result v14

    if-eqz v14, :cond_6

    move v14, v9

    goto :goto_3

    :cond_6
    move v14, v11

    :goto_3
    invoke-virtual {v0, v14}, Lcom/android/gallery3d/exif/ExifInterface;->addFrontMirror(I)Z

    :cond_7
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpeg()[B

    move-result-object v14

    invoke-virtual {v0, v14, v13}, Lcom/android/gallery3d/exif/ExifInterface;->writeExif([BLjava/io/OutputStream;)V

    invoke-virtual {v13}, Ljava/io/ByteArrayOutputStream;->toByteArray()[B

    move-result-object v0
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    :try_start_2
    invoke-static {v12, v13}, Lcom/android/camera2/PortraitDepthMap;->$closeResource(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    :try_end_2
    .catch Ljava/io/IOException; {:try_start_2 .. :try_end_2} :catch_1

    goto :goto_4

    :catchall_0
    move-exception v0

    move-object v14, v0

    :try_start_3
    throw v14
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_1

    :catchall_1
    move-exception v0

    move-object v15, v0

    :try_start_4
    invoke-static {v14, v13}, Lcom/android/camera2/PortraitDepthMap;->$closeResource(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V

    throw v15
    :try_end_4
    .catch Ljava/io/IOException; {:try_start_4 .. :try_end_4} :catch_0

    :catch_0
    move-object v0, v12

    :catch_1
    sget-object v13, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    const-string/jumbo v14, "writePortraitExif(): Failed to write depthmap associated exif metadata"

    invoke-static {v13, v14}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    :goto_4
    move-object v13, v0

    if-eqz v13, :cond_1c

    array-length v0, v13

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpeg()[B

    move-result-object v14

    array-length v14, v14

    if-gt v0, v14, :cond_8

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->isMTKPlatform()Z

    move-result v0

    if-eqz v0, :cond_1c

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo0oOO()Z

    move-result v0

    if-nez v0, :cond_8

    goto/16 :goto_12

    :cond_8
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v14, "sdcard/DCIM/Camera/evZeroMainImage"

    invoke-virtual {v0, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeStamp()J

    move-result-wide v14

    invoke-virtual {v0, v14, v15}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    const-string v14, ".yuv"

    invoke-virtual {v0, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    new-instance v15, Ljava/lang/StringBuilder;

    invoke-direct {v15}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "sdcard/DCIM/Camera/evZeroSubImage"

    invoke-virtual {v15, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-object v6, v13

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeStamp()J

    move-result-wide v12

    invoke-virtual {v15, v12, v13}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v15, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v15}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v12

    new-instance v13, Ljava/lang/StringBuilder;

    invoke-direct {v13}, Ljava/lang/StringBuilder;-><init>()V

    const-string v15, "sdcard/DCIM/Camera/evMinusMainImage"

    invoke-virtual {v13, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move/from16 v16, v10

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeStamp()J

    move-result-wide v9

    invoke-virtual {v13, v9, v10}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v9

    new-instance v10, Ljava/io/File;

    invoke-direct {v10, v0}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    new-instance v0, Ljava/io/File;

    invoke-direct {v0, v12}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v10}, Ljava/io/File;->exists()Z

    move-result v12

    const-wide/16 v13, 0x8

    const/4 v15, 0x4

    const-wide/16 v18, 0x0

    if-eqz v12, :cond_9

    invoke-virtual {v0}, Ljava/io/File;->exists()Z

    move-result v12

    if-eqz v12, :cond_9

    invoke-static {v10, v11}, Lcom/android/camera/Util;->getHeader2Int(Ljava/io/File;I)I

    move-result v12

    invoke-static {v10, v15}, Lcom/android/camera/Util;->getHeader2Int(Ljava/io/File;I)I

    move-result v11

    invoke-virtual {v10}, Ljava/io/File;->length()J

    move-result-wide v20

    sub-long v20, v20, v13

    const/4 v13, 0x0

    invoke-static {v0, v13}, Lcom/android/camera/Util;->getHeader2Int(Ljava/io/File;I)I

    move-result v14

    invoke-static {v0, v15}, Lcom/android/camera/Util;->getHeader2Int(Ljava/io/File;I)I

    move-result v13

    invoke-virtual {v0}, Ljava/io/File;->length()J

    move-result-wide v24

    const-wide/16 v22, 0x8

    sub-long v24, v24, v22

    sget-object v15, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    move-object/from16 v26, v0

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v27, v10

    const-string v10, "main width = "

    invoke-virtual {v0, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v12}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v10, ", main height = "

    invoke-virtual {v0, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v11}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v10, ", sub width ="

    invoke-virtual {v0, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v14}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v10, ", sub height = "

    invoke-virtual {v0, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v13}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v15, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_5

    :cond_9
    move-object/from16 v26, v0

    move-object/from16 v27, v10

    move-wide/from16 v20, v18

    move-wide/from16 v24, v20

    const/4 v11, 0x0

    const/4 v12, 0x0

    const/4 v13, 0x0

    const/4 v14, 0x0

    :goto_5
    new-instance v0, Ljava/io/File;

    invoke-direct {v0, v9}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    invoke-virtual {v0}, Ljava/io/File;->exists()Z

    move-result v9

    if-eqz v9, :cond_a

    const/4 v9, 0x0

    invoke-static {v0, v9}, Lcom/android/camera/Util;->getHeader2Int(Ljava/io/File;I)I

    move-result v10

    const/4 v9, 0x4

    invoke-static {v0, v9}, Lcom/android/camera/Util;->getHeader2Int(Ljava/io/File;I)I

    move-result v15

    invoke-virtual {v0}, Ljava/io/File;->length()J

    move-result-wide v28

    const-wide/16 v22, 0x8

    sub-long v22, v28, v22

    goto :goto_6

    :cond_a
    move-wide/from16 v22, v18

    const/4 v10, 0x0

    const/4 v15, 0x0

    :goto_6
    :try_start_5
    invoke-static {}, Landroid/util/Xml;->newSerializer()Lorg/xmlpull/v1/XmlSerializer;

    move-result-object v9
    :try_end_5
    .catch Ljava/io/IOException; {:try_start_5 .. :try_end_5} :catch_2

    move-object/from16 v28, v0

    :try_start_6
    new-instance v0, Ljava/io/StringWriter;

    invoke-direct {v0}, Ljava/io/StringWriter;-><init>()V

    invoke-interface {v9, v0}, Lorg/xmlpull/v1/XmlSerializer;->setOutput(Ljava/io/Writer;)V
    :try_end_6
    .catch Ljava/io/IOException; {:try_start_6 .. :try_end_6} :catch_3

    move-object/from16 v29, v6

    :try_start_7
    const-string v6, "UTF-8"

    move-object/from16 v30, v0

    const/16 v17, 0x1

    invoke-static/range {v17 .. v17}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v0

    invoke-interface {v9, v6, v0}, Lorg/xmlpull/v1/XmlSerializer;->startDocument(Ljava/lang/String;Ljava/lang/Boolean;)V

    const-string v0, "depthmap"

    const/4 v6, 0x0

    invoke-interface {v9, v6, v0}, Lorg/xmlpull/v1/XmlSerializer;->startTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string/jumbo v0, "version"

    invoke-static {v5}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v5

    invoke-interface {v9, v6, v0, v5}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "focuspoint"

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    iget v6, v1, Landroid/graphics/Point;->x:I

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v6, ","

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v1, v1, Landroid/graphics/Point;->y:I

    invoke-virtual {v5, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    const/4 v5, 0x0

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "blurlevel"

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "shinethreshold"

    invoke-static {v8}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "shinelevel"

    invoke-static {v7}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "rawlength"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRawLength()I

    move-result v1

    invoke-static {v1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "depthlength"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDepthLength()I

    move-result v1

    invoke-static {v1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "mimovie"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->isCinematicAspectRatio()Z

    move-result v1

    invoke-static {v1}, Ljava/lang/String;->valueOf(Z)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "depthOrientation"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRotation()I

    move-result v1

    invoke-static {v1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string/jumbo v0, "vendor"

    invoke-static/range {v16 .. v16}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "portraitLightingVersion"

    invoke-static {v3}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "cameraPreferredMode"

    invoke-static {v4}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-interface {v9, v5, v0, v1}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "depthmap"

    invoke-interface {v9, v5, v0}, Lorg/xmlpull/v1/XmlSerializer;->endTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
    :try_end_7
    .catch Ljava/io/IOException; {:try_start_7 .. :try_end_7} :catch_4

    add-long v0, v20, v24

    add-long v0, v0, v22

    cmp-long v2, v0, v18

    const-string v3, "height"

    const-string/jumbo v4, "width"

    const-string v5, "length"

    const-string v6, "offset"

    if-eqz v2, :cond_b

    :try_start_8
    const-string v2, "mainyuv"

    const/4 v7, 0x0

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->startTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRawLength()I

    move-result v2

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDepthLength()I

    move-result v7

    add-int/2addr v2, v7

    int-to-long v7, v2

    add-long/2addr v7, v0

    invoke-static {v7, v8}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    const/4 v7, 0x0

    invoke-interface {v9, v7, v6, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static/range {v20 .. v21}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v5, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static {v12}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v4, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static {v11}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v3, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "mainyuv"

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->endTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "subyuv"

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->startTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRawLength()I

    move-result v2

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDepthLength()I

    move-result v7

    add-int/2addr v2, v7

    int-to-long v7, v2

    add-long v7, v7, v24

    add-long v7, v7, v22

    invoke-static {v7, v8}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    const/4 v7, 0x0

    invoke-interface {v9, v7, v6, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static/range {v24 .. v25}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v5, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static {v14}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v4, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static {v13}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v3, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "subyuv"

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->endTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    :cond_b
    cmp-long v2, v22, v18

    if-eqz v2, :cond_c

    const-string v2, "evminusyuv"

    const/4 v7, 0x0

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->startTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRawLength()I

    move-result v2

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDepthLength()I

    move-result v7

    add-int/2addr v2, v7

    int-to-long v7, v2

    add-long v7, v7, v22

    invoke-static {v7, v8}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    const/4 v7, 0x0

    invoke-interface {v9, v7, v6, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static/range {v22 .. v23}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v5, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static {v10}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v4, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-static {v15}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v3, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "evminusyuv"

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->endTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    :cond_c
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpegDataOfTheRegionUnderWatermarks()[B

    move-result-object v2

    if-eqz v2, :cond_f

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpegDataOfTheRegionUnderWatermarks()[B

    move-result-object v2

    array-length v2, v2

    if-lez v2, :cond_f

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCoordinatesOfOfTheRegionUnderWatermarks()[I

    move-result-object v2

    if-eqz v2, :cond_f

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCoordinatesOfOfTheRegionUnderWatermarks()[I

    move-result-object v2

    array-length v2, v2

    const/4 v7, 0x4

    if-lt v2, v7, :cond_f

    const-string v2, "subimage"

    const/4 v7, 0x0

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->startTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpegDataOfTheRegionUnderWatermarks()[B

    move-result-object v2

    array-length v2, v2

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulWaterMark()[B

    move-result-object v7

    if-eqz v7, :cond_d

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulWaterMark()[B

    move-result-object v7

    array-length v11, v7

    goto :goto_7

    :cond_d
    const/4 v11, 0x0

    :goto_7
    add-int/2addr v2, v11

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v7

    if-eqz v7, :cond_e

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v7

    array-length v11, v7

    goto :goto_8

    :cond_e
    const/4 v11, 0x0

    :goto_8
    add-int/2addr v2, v11

    int-to-long v7, v2

    add-long/2addr v7, v0

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRawLength()I

    move-result v2

    int-to-long v10, v2

    add-long/2addr v7, v10

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDepthLength()I

    move-result v2

    int-to-long v10, v2

    add-long/2addr v7, v10

    invoke-static {v7, v8}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    const/4 v7, 0x0

    invoke-interface {v9, v7, v6, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpegDataOfTheRegionUnderWatermarks()[B

    move-result-object v2

    array-length v2, v2

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v5, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "paddingx"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCoordinatesOfOfTheRegionUnderWatermarks()[I

    move-result-object v8

    const/4 v10, 0x0

    aget v8, v8, v10

    invoke-static {v8}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v8

    invoke-interface {v9, v7, v2, v8}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "paddingy"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCoordinatesOfOfTheRegionUnderWatermarks()[I

    move-result-object v8

    const/4 v10, 0x1

    aget v8, v8, v10

    invoke-static {v8}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v8

    invoke-interface {v9, v7, v2, v8}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCoordinatesOfOfTheRegionUnderWatermarks()[I

    move-result-object v2

    const/4 v8, 0x2

    aget v2, v2, v8

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v4, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCoordinatesOfOfTheRegionUnderWatermarks()[I

    move-result-object v2

    const/4 v8, 0x3

    aget v2, v2, v8

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v3, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "rotation"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRotation()I

    move-result v8

    invoke-static {v8}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v8

    invoke-interface {v9, v7, v2, v8}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "subimage"

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->endTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    :cond_f
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulWaterMark()[B

    move-result-object v2

    if-eqz v2, :cond_11

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulWaterMark()[B

    move-result-object v2

    array-length v2, v2

    if-lez v2, :cond_11

    const-string v2, "lenswatermark"

    const/4 v7, 0x0

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->startTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulWaterMark()[B

    move-result-object v2

    array-length v2, v2

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v7

    if-eqz v7, :cond_10

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v7

    array-length v11, v7

    goto :goto_9

    :cond_10
    const/4 v11, 0x0

    :goto_9
    add-int/2addr v2, v11

    int-to-long v7, v2

    add-long/2addr v7, v0

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRawLength()I

    move-result v2

    int-to-long v11, v2

    add-long/2addr v7, v11

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDepthLength()I

    move-result v2

    int-to-long v11, v2

    add-long/2addr v7, v11

    invoke-static {v7, v8}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    const/4 v7, 0x0

    invoke-interface {v9, v7, v6, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulWaterMark()[B

    move-result-object v2

    array-length v2, v2

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v5, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulCameraWaterMarkLocation()[I

    move-result-object v2

    const/4 v8, 0x0

    aget v2, v2, v8

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v4, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulCameraWaterMarkLocation()[I

    move-result-object v2

    const/4 v8, 0x1

    aget v2, v2, v8

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v7, v3, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "paddingx"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulCameraWaterMarkLocation()[I

    move-result-object v10

    const/4 v11, 0x2

    aget v10, v10, v11

    invoke-static {v10}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v10

    invoke-interface {v9, v7, v2, v10}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "paddingy"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulCameraWaterMarkLocation()[I

    move-result-object v10

    const/4 v11, 0x3

    aget v10, v10, v11

    invoke-static {v10}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v10

    invoke-interface {v9, v7, v2, v10}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v2, "lenswatermark"

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->endTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    :cond_11
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v2

    if-eqz v2, :cond_12

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v2

    array-length v2, v2

    if-lez v2, :cond_12

    const-string v2, "timewatermark"

    const/4 v7, 0x0

    invoke-interface {v9, v7, v2}, Lorg/xmlpull/v1/XmlSerializer;->startTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v2

    array-length v2, v2

    int-to-long v10, v2

    add-long/2addr v10, v0

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getRawLength()I

    move-result v0

    int-to-long v0, v0

    add-long/2addr v10, v0

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDepthLength()I

    move-result v0

    int-to-long v0, v0

    add-long/2addr v10, v0

    invoke-static {v10, v11}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v0

    const/4 v1, 0x0

    invoke-interface {v9, v1, v6, v0}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v0

    array-length v0, v0

    invoke-static {v0}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v0

    invoke-interface {v9, v1, v5, v0}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMarkLocation()[I

    move-result-object v0

    const/4 v2, 0x0

    aget v0, v0, v2

    invoke-static {v0}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v0

    invoke-interface {v9, v1, v4, v0}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMarkLocation()[I

    move-result-object v0

    const/4 v2, 0x1

    aget v0, v0, v2

    invoke-static {v0}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v0

    invoke-interface {v9, v1, v3, v0}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "paddingx"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMarkLocation()[I

    move-result-object v2

    const/4 v3, 0x2

    aget v2, v2, v3

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v1, v0, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "paddingy"

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMarkLocation()[I

    move-result-object v2

    const/4 v3, 0x3

    aget v2, v2, v3

    invoke-static {v2}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v2

    invoke-interface {v9, v1, v0, v2}, Lorg/xmlpull/v1/XmlSerializer;->attribute(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    const-string v0, "timewatermark"

    invoke-interface {v9, v1, v0}, Lorg/xmlpull/v1/XmlSerializer;->endTag(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;

    :cond_12
    invoke-interface {v9}, Lorg/xmlpull/v1/XmlSerializer;->endDocument()V

    invoke-virtual/range {v30 .. v30}, Ljava/io/StringWriter;->toString()Ljava/lang/String;

    move-result-object v12
    :try_end_8
    .catch Ljava/io/IOException; {:try_start_8 .. :try_end_8} :catch_4

    goto :goto_a

    :catch_2
    move-object/from16 v28, v0

    :catch_3
    move-object/from16 v29, v6

    :catch_4
    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    const-string/jumbo v1, "writePortraitExif(): Failed to generate depthmap associated xmp metadata"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    const/4 v12, 0x0

    :goto_a
    if-nez v12, :cond_13

    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    const-string/jumbo v1, "writePortraitExif(): #2: return original jpeg"

    :goto_b
    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpeg()[B

    move-result-object v0

    return-object v0

    :cond_13
    :try_start_9
    new-instance v1, Ljava/io/ByteArrayInputStream;
    :try_end_9
    .catch Ljava/io/IOException; {:try_start_9 .. :try_end_9} :catch_5
    .catch Lcom/adobe/xmp/XMPException; {:try_start_9 .. :try_end_9} :catch_5

    move-object/from16 v2, v29

    :try_start_a
    invoke-direct {v1, v2}, Ljava/io/ByteArrayInputStream;-><init>([B)V
    :try_end_a
    .catch Ljava/io/IOException; {:try_start_a .. :try_end_a} :catch_6
    .catch Lcom/adobe/xmp/XMPException; {:try_start_a .. :try_end_a} :catch_6

    :try_start_b
    new-instance v3, Ljava/io/ByteArrayOutputStream;

    invoke-direct {v3}, Ljava/io/ByteArrayOutputStream;-><init>()V
    :try_end_b
    .catchall {:try_start_b .. :try_end_b} :catchall_7

    :try_start_c
    invoke-static {}, Lcom/android/camera/XmpHelper;->createXMPMeta()Lcom/adobe/xmp/XMPMeta;

    move-result-object v0

    const-string v4, "http://ns.xiaomi.com/photos/1.0/camera/"

    const-string v5, "XMPMeta"

    invoke-interface {v0, v4, v5, v12}, Lcom/adobe/xmp/XMPMeta;->setProperty(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)V

    invoke-static {v1, v3, v0}, Lcom/android/camera/XmpHelper;->writeXMPMeta(Ljava/io/InputStream;Ljava/io/OutputStream;Lcom/adobe/xmp/XMPMeta;)Z

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpegDataOfTheRegionUnderWatermarks()[B

    move-result-object v0
    :try_end_c
    .catchall {:try_start_c .. :try_end_c} :catchall_4

    if-eqz v0, :cond_14

    :try_start_d
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpegDataOfTheRegionUnderWatermarks()[B

    move-result-object v0

    array-length v0, v0

    if-lez v0, :cond_14

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCoordinatesOfOfTheRegionUnderWatermarks()[I

    move-result-object v0

    if-eqz v0, :cond_14

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getCoordinatesOfOfTheRegionUnderWatermarks()[I

    move-result-object v0

    array-length v0, v0

    const/4 v4, 0x4

    if-lt v0, v4, :cond_14

    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getJpegDataOfTheRegionUnderWatermarks()[B

    move-result-object v0

    invoke-virtual {v3, v0}, Ljava/io/ByteArrayOutputStream;->write([B)V
    :try_end_d
    .catchall {:try_start_d .. :try_end_d} :catchall_2

    goto :goto_c

    :catchall_2
    move-exception v0

    move-object v4, v0

    const/4 v6, 0x0

    goto :goto_d

    :cond_14
    :goto_c
    :try_start_e
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulWaterMark()[B

    move-result-object v0
    :try_end_e
    .catchall {:try_start_e .. :try_end_e} :catchall_4

    if-eqz v0, :cond_15

    :try_start_f
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getDulWaterMark()[B

    move-result-object v0

    invoke-virtual {v3, v0}, Ljava/io/ByteArrayOutputStream;->write([B)V
    :try_end_f
    .catchall {:try_start_f .. :try_end_f} :catchall_2

    :cond_15
    :try_start_10
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v0
    :try_end_10
    .catchall {:try_start_10 .. :try_end_10} :catchall_4

    if-eqz v0, :cond_16

    :try_start_11
    invoke-virtual/range {p1 .. p1}, Lcom/android/camera2/PortraitDepthMapExif;->getTimeWaterMark()[B

    move-result-object v0

    invoke-virtual {v3, v0}, Ljava/io/ByteArrayOutputStream;->write([B)V

    :cond_16
    cmp-long v0, v20, v18

    const/16 v4, 0x8

    if-eqz v0, :cond_17

    move-object/from16 v0, v27

    invoke-static {v0, v3, v4}, Lcom/android/camera/Util;->writeFile2Stream(Ljava/io/File;Ljava/io/ByteArrayOutputStream;I)V

    invoke-virtual {v0}, Ljava/io/File;->delete()Z

    :cond_17
    cmp-long v0, v24, v18

    if-eqz v0, :cond_18

    move-object/from16 v0, v26

    invoke-static {v0, v3, v4}, Lcom/android/camera/Util;->writeFile2Stream(Ljava/io/File;Ljava/io/ByteArrayOutputStream;I)V

    invoke-virtual {v0}, Ljava/io/File;->delete()Z

    :cond_18
    cmp-long v0, v22, v18

    if-eqz v0, :cond_19

    move-object/from16 v0, v28

    invoke-static {v0, v3, v4}, Lcom/android/camera/Util;->writeFile2Stream(Ljava/io/File;Ljava/io/ByteArrayOutputStream;I)V

    invoke-virtual {v0}, Ljava/io/File;->delete()Z
    :try_end_11
    .catchall {:try_start_11 .. :try_end_11} :catchall_2

    :cond_19
    :try_start_12
    invoke-virtual {v3}, Ljava/io/ByteArrayOutputStream;->flush()V

    invoke-virtual {v3}, Ljava/io/ByteArrayOutputStream;->toByteArray()[B

    move-result-object v12
    :try_end_12
    .catchall {:try_start_12 .. :try_end_12} :catchall_4

    const/4 v6, 0x0

    :try_start_13
    invoke-static {v6, v3}, Lcom/android/camera2/PortraitDepthMap;->$closeResource(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    :try_end_13
    .catchall {:try_start_13 .. :try_end_13} :catchall_3

    :try_start_14
    invoke-static {v6, v1}, Lcom/android/camera2/PortraitDepthMap;->$closeResource(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    :try_end_14
    .catch Ljava/io/IOException; {:try_start_14 .. :try_end_14} :catch_7
    .catch Lcom/adobe/xmp/XMPException; {:try_start_14 .. :try_end_14} :catch_7

    goto :goto_10

    :catchall_3
    move-exception v0

    move-object v3, v0

    goto :goto_f

    :catchall_4
    move-exception v0

    const/4 v6, 0x0

    move-object v4, v0

    :goto_d
    :try_start_15
    throw v4
    :try_end_15
    .catchall {:try_start_15 .. :try_end_15} :catchall_5

    :catchall_5
    move-exception v0

    move-object v5, v0

    :try_start_16
    invoke-static {v4, v3}, Lcom/android/camera2/PortraitDepthMap;->$closeResource(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V

    throw v5
    :try_end_16
    .catchall {:try_start_16 .. :try_end_16} :catchall_6

    :catchall_6
    move-exception v0

    goto :goto_e

    :catchall_7
    move-exception v0

    const/4 v6, 0x0

    :goto_e
    move-object v3, v0

    move-object v12, v6

    :goto_f
    :try_start_17
    throw v3
    :try_end_17
    .catchall {:try_start_17 .. :try_end_17} :catchall_8

    :catchall_8
    move-exception v0

    move-object v4, v0

    :try_start_18
    invoke-static {v3, v1}, Lcom/android/camera2/PortraitDepthMap;->$closeResource(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V

    throw v4
    :try_end_18
    .catch Ljava/io/IOException; {:try_start_18 .. :try_end_18} :catch_7
    .catch Lcom/adobe/xmp/XMPException; {:try_start_18 .. :try_end_18} :catch_7

    :catch_5
    move-object/from16 v2, v29

    :catch_6
    const/4 v6, 0x0

    move-object v12, v6

    :catch_7
    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    const-string/jumbo v1, "writePortraitExif(): Failed to insert depthmap associated xmp metadata"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    :goto_10
    if-eqz v12, :cond_1b

    array-length v0, v12

    array-length v1, v2

    if-gt v0, v1, :cond_1a

    goto :goto_11

    :cond_1a
    return-object v12

    :cond_1b
    :goto_11
    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    const-string/jumbo v1, "writePortraitExif(): #3: return original jpeg"

    goto/16 :goto_b

    :cond_1c
    :goto_12
    sget-object v0, Lcom/android/camera2/PortraitDepthMap;->TAG:Ljava/lang/String;

    const-string/jumbo v1, "writePortraitExif(): #1: return original jpeg"

    goto/16 :goto_b
.end method

.class public Lcom/android/camera2/vendortag/struct/SuperNightEvValue;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private mSequenceNum:I

.field private mValue:[I


# direct methods
.method private constructor <init>(I[I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/android/camera2/vendortag/struct/SuperNightEvValue;->mSequenceNum:I

    iput-object p2, p0, Lcom/android/camera2/vendortag/struct/SuperNightEvValue;->mValue:[I

    return-void
.end method

.method public static getTotalExposureTime([B)I
    .locals 5

    const/4 v0, 0x0

    if-nez p0, :cond_0

    return v0

    :cond_0
    array-length v1, p0

    const/16 v2, 0x44

    if-le v1, v2, :cond_1

    int-to-long v0, v0

    array-length v2, p0

    add-int/lit8 v2, v2, -0x1

    aget-byte v2, p0, v2

    invoke-static {v2}, Ljava/lang/Byte;->toUnsignedLong(B)J

    move-result-wide v2

    const/16 v4, 0x18

    shl-long/2addr v2, v4

    add-long/2addr v0, v2

    long-to-int v0, v0

    int-to-long v0, v0

    array-length v2, p0

    add-int/lit8 v2, v2, -0x2

    aget-byte v2, p0, v2

    invoke-static {v2}, Ljava/lang/Byte;->toUnsignedLong(B)J

    move-result-wide v2

    const/16 v4, 0x10

    shl-long/2addr v2, v4

    add-long/2addr v0, v2

    long-to-int v0, v0

    int-to-long v0, v0

    array-length v2, p0

    add-int/lit8 v2, v2, -0x3

    aget-byte v2, p0, v2

    invoke-static {v2}, Ljava/lang/Byte;->toUnsignedLong(B)J

    move-result-wide v2

    const/16 v4, 0x8

    shl-long/2addr v2, v4

    add-long/2addr v0, v2

    long-to-int v0, v0

    int-to-long v0, v0

    array-length v2, p0

    add-int/lit8 v2, v2, -0x4

    aget-byte p0, p0, v2

    invoke-static {p0}, Ljava/lang/Byte;->toUnsignedLong(B)J

    move-result-wide v2

    add-long/2addr v0, v2

    long-to-int v0, v0

    :cond_1
    return v0
.end method

.method public static parseSuperNightEvValue([BLjava/lang/String;Z)Lcom/android/camera2/vendortag/struct/SuperNightEvValue;
    .locals 3

    const/4 v0, 0x0

    const/16 v1, 0x8

    if-eqz p1, :cond_0

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v2

    if-lez v2, :cond_0

    const-string p0, ","

    invoke-virtual {p1, p0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v1, p0

    new-array p1, v1, [I

    :goto_0
    array-length p2, p0

    if-ge v0, p2, :cond_3

    :try_start_0
    aget-object p2, p0, v0

    invoke-static {p2}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p2

    aput p2, p1, v0
    :try_end_0
    .catch Ljava/lang/NumberFormatException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception p2

    invoke-virtual {p2}, Ljava/lang/NumberFormatException;->printStackTrace()V

    :goto_1
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    if-eqz p0, :cond_1

    array-length p1, p0

    if-lez p1, :cond_1

    aget-byte v1, p0, v0

    new-array p1, v1, [I

    const/4 p2, 0x1

    :goto_2
    add-int/lit8 v0, v1, 0x1

    if-ge p2, v0, :cond_3

    add-int/lit8 v0, p2, -0x1

    mul-int/lit8 v2, p2, 0x4

    aget-byte v2, p0, v2

    aput v2, p1, v0

    add-int/lit8 p2, p2, 0x1

    goto :goto_2

    :cond_1
    new-array p1, v1, [I

    if-eqz p2, :cond_2

    fill-array-data p1, :array_0

    goto :goto_3

    :cond_2
    fill-array-data p1, :array_1

    :cond_3
    :goto_3
    new-instance p0, Lcom/android/camera2/vendortag/struct/SuperNightEvValue;

    invoke-direct {p0, v1, p1}, Lcom/android/camera2/vendortag/struct/SuperNightEvValue;-><init>(I[I)V

    return-object p0

    nop

    :array_0
    .array-data 4
        -0x12
        -0xc
        -0x6
        0x0
        0x0
        0x0
        0x0
        0x0
    .end array-data

    :array_1
    .array-data 4
        -0x12
        -0xc
        -0x6
        0x0
        0x6
        0x6
        0x6
        0x6
    .end array-data
.end method


# virtual methods
.method public getSequenceNum()I
    .locals 0

    iget p0, p0, Lcom/android/camera2/vendortag/struct/SuperNightEvValue;->mSequenceNum:I

    return p0
.end method

.method public getValue()[I
    .locals 0

    iget-object p0, p0, Lcom/android/camera2/vendortag/struct/SuperNightEvValue;->mValue:[I

    return-object p0
.end method

.method public toString()Ljava/lang/String;
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "SuperNightEvValue{mSequenceNum="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v1, p0, Lcom/android/camera2/vendortag/struct/SuperNightEvValue;->mSequenceNum:I

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, ", mValue="

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lcom/android/camera2/vendortag/struct/SuperNightEvValue;->mValue:[I

    invoke-static {p0}, Ljava/util/Arrays;->toString([I)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const/16 p0, 0x7d

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

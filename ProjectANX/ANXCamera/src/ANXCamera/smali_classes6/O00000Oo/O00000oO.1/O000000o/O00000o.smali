.class public LO00000Oo/O00000oO/O000000o/O00000o;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements LO00000Oo/O00000oO/O000000o/O0000OOo;


# static fields
.field private static final O0Ooo0:Z

.field private static final O0Ooo0O:Z = false

.field private static final PARALLEL_PERFORMANCE_SETTING:I = 0x0

.field private static final PARALLEL_QUALITY_SETTING:I = 0x1

.field private static final PARALLEL_QUEUE_SIZE:I = 0x2

.field private static final TAG:Ljava/lang/String; = "O00000o"


# instance fields
.field private O0Ooo00:Ljava/lang/String;

.field private mConfig:Lcom/mi/device/Common;


# direct methods
.method static constructor <clinit>()V
    .locals 2

    sget-boolean v0, Lcom/android/camera/Util;->DEBUG:Z

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    const-string v0, "camera.feature.clone"

    invoke-static {v0, v1}, Lcom/xiaomi/camera/util/SystemProperties;->getBoolean(Ljava/lang/String;Z)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v1, 0x1

    :cond_0
    sput-boolean v1, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo0:Z

    return-void
.end method

.method private constructor <init>()V
    .locals 3

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->getClassName()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, LO00000o0/O00000Oo;->O000Oo0(Ljava/lang/String;)Lcom/mi/device/Common;

    move-result-object v0

    iput-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000o;->TAG:Ljava/lang/String;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "init proxy = "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-static {v0, p0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method synthetic constructor <init>(LO00000Oo/O00000oO/O000000o/O00000Oo;)V
    .locals 0

    invoke-direct {p0}, LO00000Oo/O00000oO/O000000o/O00000o;-><init>()V

    return-void
.end method

.method private O000000o(ZLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    if-eqz p1, :cond_0

    invoke-direct {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->Oo0OOo0()Ljava/lang/String;

    move-result-object p0

    goto :goto_0

    :cond_0
    invoke-direct {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->Oo0OOoo()Ljava/lang/String;

    move-result-object p0

    :goto_0
    invoke-virtual {p0, p2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result p1

    if-nez p1, :cond_1

    return-object p3

    :cond_1
    sget-object p1, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, p1}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string p1, ";"

    invoke-virtual {p0, p1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :cond_2
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result p1

    if-eqz p1, :cond_3

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Ljava/lang/String;

    invoke-virtual {p1, p2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p2, ":"

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    sget-object p2, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p1, p2}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p1

    invoke-virtual {p1}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object p1

    const-string p2, ""

    invoke-virtual {p1, p0, p2}, Ljava/lang/String;->replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;

    move-result-object p3

    :cond_3
    return-object p3
.end method

.method static synthetic O00000Oo(Lcom/android/camera2/CameraCapabilities;)Ljava/lang/Boolean;
    .locals 0

    invoke-virtual {p0}, Lcom/android/camera2/CameraCapabilities;->isMtkPipDevicesSupported()Z

    move-result p0

    invoke-static {p0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object p0

    return-object p0
.end method

.method private O00000o0(Lcom/android/camera2/CameraCapabilities;)Z
    .locals 4

    const/4 v0, 0x0

    if-nez p1, :cond_0

    return v0

    :cond_0
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO00OoO()Landroid/util/Size;

    move-result-object p0

    if-nez p0, :cond_1

    return v0

    :cond_1
    const/16 v1, 0x20

    const v2, 0x80f3

    invoke-virtual {p1, v1, v2}, Lcom/android/camera2/CameraCapabilities;->getSupportedOutputSizeWithAssignedMode(II)Ljava/util/List;

    move-result-object p1

    if-eqz p1, :cond_3

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_3

    invoke-interface {p1}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p1

    :cond_2
    invoke-interface {p1}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_3

    invoke-interface {p1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/android/camera/CameraSize;

    iget v2, v1, Lcom/android/camera/CameraSize;->width:I

    invoke-virtual {p0}, Landroid/util/Size;->getWidth()I

    move-result v3

    if-lt v2, v3, :cond_2

    iget v2, v1, Lcom/android/camera/CameraSize;->height:I

    invoke-virtual {p0}, Landroid/util/Size;->getHeight()I

    move-result v3

    if-lt v2, v3, :cond_2

    sget-object p0, LO00000Oo/O00000oO/O000000o/O00000o;->TAG:Ljava/lang/String;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "isSupportUltraPixelRaw size:"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Lcom/android/camera/CameraSize;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-static {p0, p1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const/4 p0, 0x1

    return p0

    :cond_3
    return v0
.end method

.method private O000OoOO(Ljava/lang/String;)I
    .locals 0

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result p0

    if-nez p0, :cond_0

    const/4 p0, 0x0

    invoke-virtual {p1, p0}, Ljava/lang/String;->charAt(I)C

    move-result p0

    invoke-static {p0}, Ljava/lang/Character;->isDigit(C)Z

    move-result p1

    if-eqz p1, :cond_0

    invoke-static {p0}, Ljava/lang/String;->valueOf(C)Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p0

    return p0

    :cond_0
    const/4 p0, -0x1

    return p0
.end method

.method private O000OoOo(Ljava/lang/String;)Landroid/util/Size;
    .locals 2

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result p0

    if-nez p0, :cond_0

    const/16 p0, 0x3a

    invoke-virtual {p1, p0}, Ljava/lang/String;->indexOf(I)I

    move-result p0

    const/4 v0, 0x1

    add-int/2addr p0, v0

    invoke-virtual {p1, p0}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result p1

    if-nez p1, :cond_0

    const-string p1, " "

    const-string v1, ""

    invoke-virtual {p0, p1, v1}, Ljava/lang/String;->replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;

    move-result-object p0

    const-string/jumbo p1, "x"

    invoke-virtual {p0, p1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length p1, p0

    const/4 v1, 0x2

    if-lt p1, v1, :cond_0

    const/4 p1, 0x0

    aget-object p1, p0, p1

    invoke-static {p1}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object p1

    invoke-virtual {p1}, Ljava/lang/Integer;->intValue()I

    move-result p1

    aget-object p0, p0, v0

    invoke-static {p0}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    new-instance v0, Landroid/util/Size;

    invoke-direct {v0, p1, p0}, Landroid/util/Size;-><init>(II)V

    return-object v0

    :cond_0
    const/4 p0, 0x0

    return-object p0
.end method

.method public static O0ooOO()Ljava/lang/String;
    .locals 3

    const/4 v0, 0x1

    const-string v1, "ro.boot.camera.config"

    invoke-static {v1, v0}, Lcom/xiaomi/camera/util/SystemProperties;->getInt(Ljava/lang/String;I)I

    move-result v1

    if-eqz v1, :cond_0

    const-string v2, ""

    return-object v2

    :cond_0
    const-string v0, "_pro"

    return-object v0
.end method

.method private Oo0OOo0()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OOo0()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method private Oo0OOoo()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OOoo()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method private Oo0Oo00()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0Oo00()I

    move-result p0

    return p0
.end method

.method private Oo0Oooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0Oooo()Z

    move-result p0

    return p0
.end method

.method public static instance()LO00000Oo/O00000oO/O000000o/O00000o;
    .locals 1

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000o0;->INSTANCE:LO00000Oo/O00000oO/O000000o/O00000o;

    return-object v0
.end method


# virtual methods
.method public O000000o(ZZLjava/lang/String;)Ljava/util/List;
    .locals 2

    const/4 v0, 0x0

    const-string v1, "capture_inner"

    invoke-direct {p0, v0, v1, p3}, LO00000Oo/O00000oO/O000000o/O00000o;->O000000o(ZLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p3

    if-eqz p2, :cond_0

    const-string/jumbo p2, "video_inner"

    invoke-direct {p0, v0, p2, p3}, LO00000Oo/O00000oO/O000000o/O00000o;->O000000o(ZLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p3

    :cond_0
    if-eqz p1, :cond_1

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OOOo()[Ljava/lang/String;

    move-result-object p0

    aget-object p3, p0, v0

    :cond_1
    const-string p0, ":"

    invoke-virtual {p3, p0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    new-instance p1, Ljava/util/ArrayList;

    invoke-direct {p1}, Ljava/util/ArrayList;-><init>()V

    :goto_0
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p2

    if-ge v0, p2, :cond_2

    invoke-interface {p0, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Ljava/lang/String;

    invoke-static {p2}, Ljava/lang/Float;->parseFloat(Ljava/lang/String;)F

    move-result p2

    invoke-static {p2}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object p2

    invoke-interface {p1, p2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    return-object p1
.end method

.method public O000000o(Lcom/android/camera2/CameraCapabilities;)Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0oo0O()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-direct {p0, p1}, LO00000Oo/O00000oO/O000000o/O00000o;->O00000o0(Lcom/android/camera2/CameraCapabilities;)Z

    move-result p0

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public O00000Oo(ZZLjava/lang/String;)Ljava/util/List;
    .locals 2

    const/4 v0, 0x0

    const-string v1, "capture_ruler"

    invoke-direct {p0, v0, v1, p3}, LO00000Oo/O00000oO/O000000o/O00000o;->O000000o(ZLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p3

    if-eqz p2, :cond_0

    const-string/jumbo p2, "video_ruler"

    invoke-direct {p0, v0, p2, p3}, LO00000Oo/O00000oO/O000000o/O00000o;->O000000o(ZLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p3

    :cond_0
    if-eqz p1, :cond_1

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OOOo()[Ljava/lang/String;

    move-result-object p0

    const/4 p1, 0x1

    aget-object p3, p0, p1

    :cond_1
    const-string p0, ":"

    invoke-virtual {p3, p0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    new-instance p1, Ljava/util/ArrayList;

    invoke-direct {p1}, Ljava/util/ArrayList;-><init>()V

    :goto_0
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p2

    if-ge v0, p2, :cond_2

    invoke-interface {p0, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p2

    check-cast p2, Ljava/lang/String;

    invoke-static {p2}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p2

    invoke-static {p2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p2

    invoke-interface {p1, p2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_2
    return-object p1
.end method

.method public O0000O0o()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public O000O00o(I)Z
    .locals 1

    const/16 v0, 0xb4

    if-eq p1, v0, :cond_0

    const/16 v0, 0xa2

    if-eq p1, v0, :cond_0

    const/16 v0, 0xd6

    if-eq p1, v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0o00O()Z

    move-result p0

    return p0
.end method

.method public O000O0Oo(Z)Z
    .locals 0

    if-eqz p1, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OO00()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public O000O0o0(Z)Ljava/lang/String;
    .locals 0

    if-eqz p1, :cond_0

    const-string p0, "4x3"

    return-object p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OO0()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O000OOoO(Ljava/lang/String;)Ljava/lang/String;
    .locals 1

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OOOO()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_0

    return-object p1

    :cond_0
    return-object p0
.end method

.method public O000OOoo(Ljava/lang/String;)Z
    .locals 6

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0o00()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    const/4 v1, 0x0

    if-nez v0, :cond_2

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_0

    goto :goto_1

    :cond_0
    const-string v0, ";"

    invoke-virtual {p0, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v0, p0

    move v2, v1

    :goto_0
    if-ge v2, v0, :cond_2

    aget-object v3, p0, v2

    const-string v4, ","

    invoke-virtual {v3, v4}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v3

    array-length v4, v3

    const/4 v5, 0x2

    if-lt v4, v5, :cond_1

    aget-object v4, v3, v1

    invoke-virtual {v4, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_1

    const/4 p0, 0x1

    aget-object p0, v3, p0

    invoke-static {p0}, Ljava/lang/Boolean;->valueOf(Ljava/lang/String;)Ljava/lang/Boolean;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/Boolean;->booleanValue()Z

    move-result p0

    return p0

    :cond_1
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_2
    :goto_1
    return v1
.end method

.method public O00oOoOo(Z)Ljava/lang/String;
    .locals 0

    if-eqz p1, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0ooOoO()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0ooO0()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O00oOooO(I)I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OOoO()I

    move-result p0

    if-gez p0, :cond_0

    return p1

    :cond_0
    return p0
.end method

.method public O00oOooo(I)[F
    .locals 5

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->isSupportedOpticalZoom()Z

    move-result v0

    if-eqz v0, :cond_0

    const-string v0, "1.0:2.0"

    goto :goto_0

    :cond_0
    const-string v0, "1.0"

    :goto_0
    const/16 v1, 0xa1

    const/4 v2, 0x0

    const-string v3, "capture"

    const/4 v4, 0x1

    if-eq p1, v1, :cond_2

    const/16 v1, 0xa2

    if-eq p1, v1, :cond_2

    const/16 v1, 0xa9

    if-eq p1, v1, :cond_2

    const/16 v1, 0xb4

    if-eq p1, v1, :cond_2

    const/16 v1, 0xb7

    if-eq p1, v1, :cond_2

    const/16 v1, 0xbc

    if-eq p1, v1, :cond_1

    const/16 v1, 0xcc

    if-eq p1, v1, :cond_2

    packed-switch p1, :pswitch_data_0

    :goto_1
    move p1, v2

    goto :goto_2

    :pswitch_0
    const-string v3, "pixel"

    const-string v0, "1:2"

    goto :goto_1

    :pswitch_1
    const-string v3, "supernight"

    const-string v0, "0.6:1:2"

    goto :goto_1

    :cond_1
    const-string v3, "supermoon"

    const-string v0, "5:60"

    goto :goto_1

    :cond_2
    :pswitch_2
    move p1, v4

    :goto_2
    invoke-direct {p0, v4, v3, v0}, LO00000Oo/O00000oO/O000000o/O00000o;->O000000o(ZLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    if-eqz p1, :cond_3

    const-string p1, ""

    const-string/jumbo v1, "video"

    invoke-direct {p0, v4, v1, p1}, LO00000Oo/O00000oO/O000000o/O00000o;->O000000o(ZLjava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p0, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-nez p1, :cond_3

    goto :goto_3

    :cond_3
    move-object p0, v0

    :goto_3
    const-string p1, ":"

    invoke-virtual {p0, p1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p1

    new-array p1, p1, [F

    :goto_4
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v0

    if-ge v2, v0, :cond_4

    invoke-interface {p0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    invoke-static {v0}, Ljava/lang/Float;->parseFloat(Ljava/lang/String;)F

    move-result v0

    aput v0, p1, v2

    add-int/lit8 v2, v2, 0x1

    goto :goto_4

    :cond_4
    return-object p1

    nop

    :pswitch_data_0
    .packed-switch 0xac
        :pswitch_2
        :pswitch_1
        :pswitch_2
        :pswitch_0
    .end packed-switch
.end method

.method public O0oOoo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oOoo()Z

    move-result p0

    return p0
.end method

.method public O0oOooO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oOooO()Z

    move-result p0

    return p0
.end method

.method public O0oOooo()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oOooo()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0oo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo()I

    move-result p0

    return p0
.end method

.method public O0oo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo0()Z

    move-result p0

    return p0
.end method

.method public O0oo00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo00()Z

    move-result p0

    return p0
.end method

.method public O0oo000()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo000()Z

    move-result p0

    return p0
.end method

.method public O0oo00O()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo00O()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0oo00o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo00o()Z

    move-result p0

    return p0
.end method

.method public O0oo0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo0O()Z

    move-result p0

    return p0
.end method

.method public O0oo0O0()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo0O0()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0oo0OO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo0OO()Z

    move-result p0

    return p0
.end method

.method public O0oo0Oo()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo0Oo()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0oo0o0()J
    .locals 2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo0o0()J

    move-result-wide v0

    return-wide v0
.end method

.method public O0oo0oO()J
    .locals 2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo0oO()J

    move-result-wide v0

    return-wide v0
.end method

.method public O0oo0oo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oo0oo()I

    move-result p0

    return p0
.end method

.method public O0ooO()[I
    .locals 4

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooO()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return-object v1

    :cond_0
    :try_start_0
    const-string v0, ":"

    invoke-virtual {p0, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v0, p0

    new-array v0, v0, [I

    const/4 v2, 0x0

    :goto_0
    array-length v3, p0

    if-ge v2, v3, :cond_1

    aget-object v3, p0, v2

    invoke-virtual {v3}, Ljava/lang/String;->trim()Ljava/lang/String;

    move-result-object v3

    invoke-static {v3}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v3

    aput v3, v0, v2
    :try_end_0
    .catch Ljava/util/regex/PatternSyntaxException; {:try_start_0 .. :try_end_0} :catch_0
    .catch Ljava/lang/NumberFormatException; {:try_start_0 .. :try_end_0} :catch_0

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_1
    return-object v0

    :catch_0
    sget-object p0, LO00000Oo/O00000oO/O000000o/O00000o;->TAG:Ljava/lang/String;

    const-string v0, "get default favorite modes fails."

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->w(Ljava/lang/String;Ljava/lang/String;)I

    return-object v1
.end method

.method public O0ooO0()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooO0()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0ooO00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooO00()Z

    move-result p0

    return p0
.end method

.method public O0ooO0O()F
    .locals 2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooO0O()F

    move-result p0

    invoke-static {}, Lcom/android/camera/Util;->getDumpCropFrontZoomRatio()Ljava/lang/Float;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Float;->floatValue()F

    move-result v0

    const/4 v1, 0x0

    cmpl-float v0, v0, v1

    if-eqz v0, :cond_0

    invoke-static {}, Lcom/android/camera/Util;->getDumpCropFrontZoomRatio()Ljava/lang/Float;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Float;->floatValue()F

    move-result v0

    cmpl-float v0, v0, p0

    if-eqz v0, :cond_0

    invoke-static {}, Lcom/android/camera/Util;->getDumpCropFrontZoomRatio()Ljava/lang/Float;

    move-result-object p0

    invoke-virtual {p0}, Ljava/lang/Float;->floatValue()F

    move-result p0

    :cond_0
    return p0
.end method

.method public O0ooO0o()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooO0o()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0ooOO0()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooOO0()I

    move-result p0

    return p0
.end method

.method public O0ooOOO()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooOOO()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0ooOOo()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooOOo()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0ooOo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooOo()I

    move-result p0

    return p0
.end method

.method public O0ooOo0()I
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OOO:Z

    if-eqz v0, :cond_0

    invoke-static {}, Lcom/android/camera/CameraSettings;->isHighQualityPreferred()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, -0x1

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooOo0()I

    move-result p0

    return p0
.end method

.method public O0ooOoO()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooOoO()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0ooOoo()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooOoo()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0ooo()[I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooo()[I

    move-result-object p0

    return-object p0
.end method

.method public O0ooo0()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OO0O()I

    move-result p0

    return p0
.end method

.method public O0ooo00()I
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0ooOoo()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, LO00000Oo/O00000oO/O000000o/O00000o;->O000OoOO(Ljava/lang/String;)I

    move-result p0

    return p0
.end method

.method public O0ooo0O()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooo0O()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0ooo0o()I
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooOoO()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo000O()Z

    move-result v0

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooo0o()I

    move-result p0

    return p0

    :cond_1
    :goto_0
    const/16 p0, 0xf

    return p0
.end method

.method public O0oooO()Landroid/util/Size;
    .locals 3

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0oo0Oo()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_0

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v0

    const-string v1, ":"

    invoke-virtual {v0, v1}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_0

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p0, v1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v0, p0

    const/4 v1, 0x1

    if-le v0, v1, :cond_0

    aget-object p0, p0, v1

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string/jumbo v0, "x"

    invoke-virtual {p0, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v0, p0

    const/4 v2, 0x2

    if-ne v0, v2, :cond_0

    new-instance v0, Landroid/util/Size;

    const/4 v2, 0x0

    aget-object v2, p0, v2

    invoke-static {v2}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v2

    aget-object p0, p0, v1

    invoke-static {p0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p0

    invoke-direct {v0, v2, p0}, Landroid/util/Size;-><init>(II)V

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return-object v0
.end method

.method public O0oooO0()F
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oooO0()F

    move-result p0

    return p0
.end method

.method public O0oooOO()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oooOO()I

    move-result p0

    return p0
.end method

.method public O0oooOo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oooOo()I

    move-result p0

    return p0
.end method

.method public O0oooo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oooo()I

    move-result p0

    return p0
.end method

.method public O0oooo0()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0oooo0()I

    move-result p0

    return p0
.end method

.method public O0ooooO()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooooO()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public O0ooooo()F
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooooo()F

    move-result p0

    return p0
.end method

.method public OO0000()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0000()I

    move-result p0

    return p0
.end method

.method public OO0000o()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0000o()I

    move-result p0

    return p0
.end method

.method public OO000OO()Ljava/util/Map;
    .locals 13

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO000OO()Ljava/lang/String;

    move-result-object p0

    new-instance v0, Ljava/util/HashMap;

    invoke-direct {v0}, Ljava/util/HashMap;-><init>()V

    const/4 v1, 0x0

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-interface {v0, v2, v2}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const/4 v3, 0x1

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    invoke-interface {v0, v4, v2}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    const/4 v5, 0x2

    invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    const/4 v6, 0x3

    invoke-static {v6}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    invoke-interface {v0, v5, v6}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {p0}, Ljava/lang/String;->length()I

    move-result v6

    if-nez v6, :cond_0

    return-object v0

    :cond_0
    sget-object v6, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v6}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v6, ";"

    invoke-virtual {p0, v6}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v6, p0

    move v7, v1

    :goto_0
    if-ge v7, v6, :cond_2

    aget-object v8, p0, v7

    sget-object v9, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {v8, v9}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v8

    const-string v9, ","

    invoke-virtual {v8, v9}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v8

    sget-wide v9, Lcom/android/camera/Util;->TOTAL_MEMORY_GB:J

    aget-object v11, v8, v1

    invoke-static {v11}, Ljava/lang/Long;->parseLong(Ljava/lang/String;)J

    move-result-wide v11

    cmp-long v9, v9, v11

    if-gez v9, :cond_1

    aget-object p0, v8, v3

    sget-object v6, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v6}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v6, "!"

    invoke-virtual {p0, v6}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    aget-object v6, p0, v1

    invoke-static {v6}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v6

    invoke-static {v6}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    invoke-interface {v0, v2, v6}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    aget-object p0, p0, v3

    sget-object v2, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v2}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v2, ":"

    invoke-virtual {p0, v2}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    aget-object v1, p0, v1

    invoke-static {v1}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v1

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    invoke-interface {v0, v4, v1}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    aget-object p0, p0, v3

    invoke-static {p0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result p0

    invoke-static {p0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p0

    invoke-interface {v0, v5, p0}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    goto :goto_1

    :cond_1
    add-int/lit8 v7, v7, 0x1

    goto :goto_0

    :cond_2
    :goto_1
    return-object v0
.end method

.method public OO000Oo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO000Oo()I

    move-result p0

    return p0
.end method

.method public OO000o()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO000o()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public OO000o0()I
    .locals 4

    sget-wide v0, Lcom/android/camera/Util;->TOTAL_MEMORY_GB:J

    const-wide/16 v2, 0x6

    cmp-long v0, v0, v2

    if-gez v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OO0o()I

    move-result p0

    return p0

    :cond_0
    const/16 p0, 0xb

    return p0
.end method

.method public OO000oO()I
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooO0()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, -0x1

    return p0

    :cond_0
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooOOO()Z

    move-result v0

    if-eqz v0, :cond_1

    const/4 p0, 0x1

    return p0

    :cond_1
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO000o()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, LO00000Oo/O00000oO/O000000o/O00000o;->O000OoOO(Ljava/lang/String;)I

    move-result p0

    return p0
.end method

.method public OO000oo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO000oo()I

    move-result p0

    return p0
.end method

.method public OO00O()[F
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00O()[F

    move-result-object p0

    return-object p0
.end method

.method public OO00O0()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00O0()I

    move-result p0

    return p0
.end method

.method public OO00O0o()[F
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00O0o()[F

    move-result-object p0

    return-object p0
.end method

.method public OO00OO()I
    .locals 4

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0000o()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x6

    return p0

    :cond_0
    invoke-static {}, Lcom/android/camera/CameraSettings;->isUltraPixelOn()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOoooO()Z

    move-result v0

    if-eqz v0, :cond_1

    const/4 p0, 0x5

    return p0

    :cond_1
    invoke-static {}, Lcom/android/camera/CameraSettings;->isHighQualityPreferred()Z

    move-result v0

    if-nez v0, :cond_2

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO00O0()I

    move-result p0

    return p0

    :cond_2
    sget-wide v0, Lcom/android/camera/Util;->TOTAL_MEMORY_GB:J

    const-wide/16 v2, 0x6

    cmp-long v0, v0, v2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    if-gez v0, :cond_3

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0OOO0()I

    move-result p0

    return p0

    :cond_3
    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00OO()I

    move-result p0

    return p0
.end method

.method public OO00OOO()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00OOO()I

    move-result p0

    return p0
.end method

.method public OO00OOo()J
    .locals 2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00OOo()J

    move-result-wide v0

    return-wide v0
.end method

.method public OO00Oo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00Oo()I

    move-result p0

    return p0
.end method

.method public OO00Oo0()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00Oo0()I

    move-result p0

    return p0
.end method

.method public OO00OoO()Landroid/util/Size;
    .locals 2

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooOOO()Z

    move-result v0

    if-eqz v0, :cond_0

    new-instance p0, Landroid/util/Size;

    const/16 v0, 0x1f40

    const/16 v1, 0x1770

    invoke-direct {p0, v0, v1}, Landroid/util/Size;-><init>(II)V

    return-object p0

    :cond_0
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO000o()Ljava/lang/String;

    move-result-object v0

    invoke-direct {p0, v0}, LO00000Oo/O00000oO/O000000o/O00000o;->O000OoOo(Ljava/lang/String;)Landroid/util/Size;

    move-result-object p0

    return-object p0
.end method

.method public OO00Ooo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00Ooo()I

    move-result p0

    return p0
.end method

.method public OO00o00()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00o00()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public OO00o0O()[F
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00o0O()[F

    move-result-object p0

    return-object p0
.end method

.method public OO00o0o()[F
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00o0o()[F

    move-result-object p0

    return-object p0
.end method

.method public OO00oO()I
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOoooo()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0O0OO()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0OOoo()Z

    move-result v0

    if-eqz v0, :cond_1

    const/4 p0, 0x2

    return p0

    :cond_1
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00oO()I

    move-result p0

    return p0
.end method

.method public OO00oO0()Ljava/lang/String;
    .locals 1

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00oO0()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    :cond_0
    return-object p0
.end method

.method public OO00oOO()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00oOO()I

    move-result p0

    return p0
.end method

.method public OO00oOo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00oOo()I

    move-result p0

    return p0
.end method

.method public OO00oo()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO000oO()I

    move-result v0

    if-ltz v0, :cond_1

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00oo()Z

    move-result p0

    if-eqz p0, :cond_0

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 p0, 0x1

    :goto_1
    return p0
.end method

.method public OO00oo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00oo0()Z

    move-result p0

    return p0
.end method

.method public OO00ooO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00ooO()Z

    move-result p0

    return p0
.end method

.method public OO00ooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO00ooo()Z

    move-result p0

    return p0
.end method

.method public OO0O00o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0O00o()Z

    move-result p0

    return p0
.end method

.method public OO0O0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0O0O()Z

    move-result p0

    return p0
.end method

.method public OO0O0OO()Z
    .locals 1

    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo00:Ljava/lang/String;

    if-nez v0, :cond_0

    const-string v0, "ro.boot.hwc"

    invoke-static {v0}, Lcom/xiaomi/camera/util/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo00:Ljava/lang/String;

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo00:Ljava/lang/String;

    const-string v0, "cn"

    invoke-virtual {v0, p0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result p0

    return p0
.end method

.method public OO0O0Oo()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo00O0()Ljava/lang/String;

    move-result-object p0

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v0, ":"

    invoke-virtual {p0, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    const-string v0, "CAPTURE_INTENT"

    invoke-interface {p0, v0}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public OO0O0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0O0o()Z

    move-result p0

    return p0
.end method

.method public OO0O0o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0O0o0()Z

    move-result p0

    return p0
.end method

.method public OO0O0oO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0O0oO()Z

    move-result p0

    return p0
.end method

.method public OO0O0oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0O0oo()Z

    move-result p0

    return p0
.end method

.method public OO0OO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OO()Z

    move-result p0

    return p0
.end method

.method public OO0OO0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OO0o()Z

    move-result p0

    return p0
.end method

.method public OO0OOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OOO()Z

    move-result p0

    return p0
.end method

.method public OO0OOOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OOOO()Z

    move-result p0

    return p0
.end method

.method public OO0OOOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OOOo()Z

    move-result p0

    return p0
.end method

.method public OO0OOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OOo()Z

    move-result p0

    return p0
.end method

.method public OO0OOo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OOo0()Z

    move-result p0

    return p0
.end method

.method public OO0OOoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OOoO()Z

    move-result p0

    return p0
.end method

.method public OO0OOoo()Z
    .locals 2

    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo00:Ljava/lang/String;

    if-nez v0, :cond_0

    const-string v0, "ro.boot.hwc"

    invoke-static {v0}, Lcom/xiaomi/camera/util/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo00:Ljava/lang/String;

    :cond_0
    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo00:Ljava/lang/String;

    const-string v1, "india"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    const/4 v1, 0x1

    if-eqz v0, :cond_1

    return v1

    :cond_1
    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo00:Ljava/lang/String;

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo00:Ljava/lang/String;

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v0, "india_"

    invoke-virtual {p0, v0}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z

    move-result p0

    if-eqz p0, :cond_2

    return v1

    :cond_2
    const/4 p0, 0x0

    return p0
.end method

.method public OO0Oo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0Oo0()Z

    move-result p0

    return p0
.end method

.method public OO0Oo00()Z
    .locals 1

    invoke-static {}, Lmiui/os/Build;->getRegion()Ljava/lang/String;

    move-result-object p0

    const-string v0, "IN"

    invoke-virtual {p0, v0}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result p0

    return p0
.end method

.method public OO0Oo0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0Oo0O()Z

    move-result p0

    return p0
.end method

.method public OO0Oo0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0Oo0o()Z

    move-result p0

    return p0
.end method

.method public OO0OoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OoO()Z

    move-result p0

    return p0
.end method

.method public OO0OoO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OoO0()Z

    move-result p0

    return p0
.end method

.method public OO0OoOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OoOO()Z

    move-result p0

    return p0
.end method

.method public OO0OoOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OoOo()Z

    move-result p0

    return p0
.end method

.method public OO0Ooo()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O0:Z

    if-eqz v0, :cond_0

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0ooO:Z

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo00O0()Ljava/lang/String;

    move-result-object p0

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v0, ":"

    invoke-virtual {p0, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    const-string v0, "PRO"

    invoke-interface {p0, v0}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public OO0Ooo0()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo00O0()Ljava/lang/String;

    move-result-object p0

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v0, ":"

    invoke-virtual {p0, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    const-string v0, "NO_PIXEL"

    invoke-interface {p0, v0}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public OO0OooO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0OooO()Z

    move-result p0

    return p0
.end method

.method public OO0Oooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0Oooo()Z

    move-result p0

    return p0
.end method

.method public OO0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o()Z

    move-result p0

    return p0
.end method

.method public OO0o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o0()Z

    move-result p0

    return p0
.end method

.method public OO0o00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o00()Z

    move-result p0

    return p0
.end method

.method public OO0o000()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0ooO:Z

    if-eqz v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o000()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OO0o00O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o00O()Z

    move-result p0

    return p0
.end method

.method public OO0o00o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o00o()Z

    move-result p0

    return p0
.end method

.method public OO0o0O0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o0O0()Z

    move-result p0

    return p0
.end method

.method public OO0o0OO()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo00O0()Ljava/lang/String;

    move-result-object p0

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v0, ":"

    invoke-virtual {p0, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    const-string v0, "MACRO"

    invoke-interface {p0, v0}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public OO0o0Oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o0Oo()Z

    move-result p0

    return p0
.end method

.method public OO0o0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o0o()Z

    move-result p0

    return p0
.end method

.method public OO0o0o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o0o0()Z

    move-result p0

    return p0
.end method

.method public OO0o0oO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o0oO()Z

    move-result p0

    return p0
.end method

.method public OO0o0oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0o0oo()Z

    move-result p0

    return p0
.end method

.method public OO0oO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oO0()Z

    move-result p0

    return p0
.end method

.method public OO0oO00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oO00()Z

    move-result p0

    return p0
.end method

.method public OO0oO0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oO0O()Z

    move-result p0

    return p0
.end method

.method public OO0oO0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oO0o()Z

    move-result p0

    return p0
.end method

.method public OO0oOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oOO()Z

    move-result p0

    return p0
.end method

.method public OO0oOO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oOO0()Z

    move-result p0

    return p0
.end method

.method public OO0oOOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oOOO()Z

    move-result p0

    return p0
.end method

.method public OO0oOOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oOOo()Z

    move-result p0

    return p0
.end method

.method public OO0oOo()Z
    .locals 2

    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x1c

    if-ge v0, v1, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oOo()Z

    move-result p0

    return p0
.end method

.method public OO0oOo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oOo0()Z

    move-result p0

    return p0
.end method

.method public OO0oOoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oOoO()Z

    move-result p0

    return p0
.end method

.method public OO0oOoo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oOoo()Z

    move-result p0

    return p0
.end method

.method public OO0oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oo()Z

    move-result p0

    return p0
.end method

.method public OO0oo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oo0()Z

    move-result p0

    return p0
.end method

.method public OO0oo00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oo00()Z

    move-result p0

    if-nez p0, :cond_1

    sget-boolean p0, LO00000Oo/O00000oO/O000000o/O00000o;->O0Ooo0:Z

    if-eqz p0, :cond_0

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 p0, 0x1

    :goto_1
    return p0
.end method

.method public OO0oo0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oo0O()Z

    move-result p0

    return p0
.end method

.method public OO0oo0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oo0o()Z

    move-result p0

    return p0
.end method

.method public OO0ooO()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0ooO()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public OO0ooO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0ooO0()Z

    move-result p0

    return p0
.end method

.method public OO0ooOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0ooOO()Z

    move-result p0

    return p0
.end method

.method public OO0ooOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0ooOo()Z

    move-result p0

    return p0
.end method

.method public OO0ooo()Z
    .locals 3

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooOoo()Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v2, 0x1c

    if-ne v0, v2, :cond_1

    return v1

    :cond_1
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0ooo()Z

    move-result p0

    return p0
.end method

.method public OO0ooo0()Z
    .locals 2

    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x1c

    if-ne v0, v1, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0ooo0()Z

    move-result p0

    return p0
.end method

.method public OO0oooO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oooO()Z

    move-result p0

    return p0
.end method

.method public OO0oooo()Z
    .locals 5

    invoke-static {}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getInstance()Lcom/android/camera/module/loader/camera2/Camera2DataContainer;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getMainBackCameraId()I

    move-result v1

    invoke-virtual {v0, v1}, Lcom/android/camera/module/loader/camera2/Camera2DataContainer;->getCapabilities(I)Lcom/android/camera2/CameraCapabilities;

    move-result-object v0

    invoke-static {v0}, Ljava/util/Optional;->ofNullable(Ljava/lang/Object;)Ljava/util/Optional;

    move-result-object v0

    sget-object v1, LO00000Oo/O00000oO/O000000o/O000000o;->INSTANCE:LO00000Oo/O00000oO/O000000o/O000000o;

    invoke-virtual {v0, v1}, Ljava/util/Optional;->map(Ljava/util/function/Function;)Ljava/util/Optional;

    move-result-object v0

    const/4 v1, 0x0

    invoke-static {v1}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/util/Optional;->orElse(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Boolean;

    invoke-virtual {v0}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v0

    const-string v2, "miuicamera.dualvideo.show"

    invoke-static {v2, v1}, Lcom/xiaomi/camera/util/SystemProperties;->getBoolean(Ljava/lang/String;Z)Z

    move-result v2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OO0oooo()Z

    move-result p0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->isMTKPlatform()Z

    move-result v3

    const/4 v4, 0x1

    if-eqz v3, :cond_2

    if-nez v2, :cond_0

    if-eqz v0, :cond_1

    if-eqz p0, :cond_1

    :cond_0
    move v1, v4

    :cond_1
    return v1

    :cond_2
    if-nez v2, :cond_3

    if-eqz p0, :cond_4

    :cond_3
    move v1, v4

    :cond_4
    return v1
.end method

.method public OOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO()Z

    move-result p0

    return p0
.end method

.method public OOO00()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0ooO:Z

    if-nez v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO00()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOO000o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O0ooOOO()Ljava/lang/String;

    move-result-object p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOO00O0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO00O0()Z

    move-result p0

    return p0
.end method

.method public OOO00Oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO00Oo()Z

    move-result p0

    return p0
.end method

.method public OOO00o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->O00000oo()Z

    move-result p0

    return p0
.end method

.method public OOO00o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO00o0()Z

    move-result p0

    return p0
.end method

.method public OOO00oO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO00oO()Z

    move-result p0

    return p0
.end method

.method public OOO00oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO00oo()Z

    move-result p0

    return p0
.end method

.method public OOO0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0O()Z

    move-result p0

    return p0
.end method

.method public OOO0O0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0O0O()Z

    move-result p0

    return p0
.end method

.method public OOO0O0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0O0o()Z

    move-result p0

    return p0
.end method

.method public OOO0OO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0OO0()Z

    move-result p0

    return p0
.end method

.method public OOO0OOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0OOO()Z

    move-result p0

    return p0
.end method

.method public OOO0OOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0OOo()Z

    move-result p0

    return p0
.end method

.method public OOO0Oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0Oo()Z

    move-result p0

    return p0
.end method

.method public OOO0Oo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0Oo0()Z

    move-result p0

    if-eqz p0, :cond_0

    sget-boolean p0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOO0OoO()Z
    .locals 1

    invoke-static {}, Lcom/android/camera/Util;->isGlobalVersion()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/global/DataItemGlobal;->isNormalIntent()Z

    move-result v0

    if-nez v0, :cond_0

    goto :goto_0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0OoO()Z

    move-result p0

    return p0

    :cond_1
    :goto_0
    const/4 p0, 0x0

    return p0
.end method

.method public OOO0Ooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0Ooo()Z

    move-result p0

    return p0
.end method

.method public OOO0o()Z
    .locals 2

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO00o()Z

    move-result v0

    const/4 v1, 0x1

    if-eqz v0, :cond_0

    invoke-direct {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->Oo0Oo00()I

    move-result p0

    if-ne p0, v1, :cond_0

    goto :goto_0

    :cond_0
    const/4 v1, 0x0

    :goto_0
    return v1
.end method

.method public OOO0o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0o0()Z

    move-result p0

    return p0
.end method

.method public OOO0o00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0o00()Z

    move-result p0

    return p0
.end method

.method public OOO0o0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0o0O()Z

    move-result p0

    return p0
.end method

.method public OOO0o0o()Z
    .locals 1

    const-string p0, "ro.product.cpu.abi"

    const-string v0, ""

    invoke-static {p0, v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    const-string v0, "64"

    invoke-virtual {p0, v0}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result p0

    if-eqz p0, :cond_0

    sget-object p0, Landroid/os/Build;->DEVICE:Ljava/lang/String;

    const-string v0, "laurus"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-nez p0, :cond_0

    sget-object p0, Landroid/os/Build;->DEVICE:Ljava/lang/String;

    const-string v0, "ginkgo"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-nez p0, :cond_0

    sget-object p0, Landroid/os/Build;->DEVICE:Ljava/lang/String;

    const-string/jumbo v0, "willow"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOO0oO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0oO()Z

    move-result p0

    return p0
.end method

.method public OOO0oO0()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO00o()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-direct {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->Oo0Oo00()I

    move-result p0

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOO0oOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0oOO()Z

    move-result p0

    return p0
.end method

.method public OOO0oOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0oOo()Z

    move-result p0

    return p0
.end method

.method public OOO0oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0oo()Z

    move-result p0

    return p0
.end method

.method public OOO0oo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0oo0()Z

    move-result p0

    return p0
.end method

.method public OOO0ooO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0ooO()Z

    move-result p0

    return p0
.end method

.method public OOO0ooo()Z
    .locals 3

    const/4 v0, 0x0

    const-string v1, "miuicamera.mimoji.show"

    invoke-static {v1, v0}, Lcom/xiaomi/camera/util/SystemProperties;->getBoolean(Ljava/lang/String;Z)Z

    move-result v1

    const/4 v2, 0x1

    if-eqz v1, :cond_0

    return v2

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOO0ooo()Z

    move-result p0

    if-eqz p0, :cond_1

    sget-boolean p0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-nez p0, :cond_1

    move v0, v2

    :cond_1
    return v0
.end method

.method public OOOO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO0()Z

    move-result p0

    return p0
.end method

.method public OOOO00O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO00O()Z

    move-result p0

    return p0
.end method

.method public OOOO00o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO00o()Z

    move-result p0

    return p0
.end method

.method public OOOO0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO0O()Z

    move-result p0

    return p0
.end method

.method public OOOO0OO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO0OO()Z

    move-result p0

    return p0
.end method

.method public OOOO0Oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO0Oo()Z

    move-result p0

    return p0
.end method

.method public OOOO0o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO0o0()Z

    move-result p0

    return p0
.end method

.method public OOOO0oO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO0oO()Z

    move-result p0

    return p0
.end method

.method public OOOO0oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOO0oo()Z

    move-result p0

    return p0
.end method

.method public OOOOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOO()Z

    move-result p0

    return p0
.end method

.method public OOOOO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOO0()Z

    move-result p0

    return p0
.end method

.method public OOOOO00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOO00()Z

    move-result p0

    return p0
.end method

.method public OOOOO0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOO0o()Z

    move-result p0

    return p0
.end method

.method public OOOOOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOOO()Z

    move-result p0

    return p0
.end method

.method public OOOOOOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOOOO()Z

    move-result p0

    return p0
.end method

.method public OOOOOOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOOOo()Z

    move-result p0

    return p0
.end method

.method public OOOOOo()Z
    .locals 1

    invoke-static {}, Lcom/android/camera/Util;->isGlobalVersion()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-static {}, Lcom/android/camera/Util;->isLocaleChinese()Z

    move-result v0

    if-nez v0, :cond_0

    goto :goto_0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOOo()Z

    move-result p0

    return p0

    :cond_1
    :goto_0
    const/4 p0, 0x0

    return p0
.end method

.method public OOOOOo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOOo0()Z

    move-result p0

    return p0
.end method

.method public OOOOOoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOOoO()Z

    move-result p0

    return p0
.end method

.method public OOOOOoo()Z
    .locals 3

    invoke-static {}, Lcom/android/camera/Util;->isGlobalVersion()Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00o:Z

    if-eqz v0, :cond_1

    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v2, 0x1c

    if-ne v0, v2, :cond_1

    return v1

    :cond_1
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOOoo()Z

    move-result p0

    return p0
.end method

.method public OOOOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOo()Z

    move-result p0

    return p0
.end method

.method public OOOOo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOo0()Z

    move-result p0

    return p0
.end method

.method public OOOOo00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOo00()Z

    move-result p0

    return p0
.end method

.method public OOOOo0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOo0O()Z

    move-result p0

    return p0
.end method

.method public OOOOo0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOo0o()Z

    move-result p0

    return p0
.end method

.method public OOOOoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOoO()Z

    move-result p0

    return p0
.end method

.method public OOOOoO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOoO0()Z

    move-result p0

    return p0
.end method

.method public OOOOoOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOoOO()Z

    move-result p0

    return p0
.end method

.method public OOOOoOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOoOo()Z

    move-result p0

    return p0
.end method

.method public OOOOoo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOoo()Z

    move-result p0

    return p0
.end method

.method public OOOOoo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOoo0()Z

    move-result p0

    return p0
.end method

.method public OOOOooO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOooO()Z

    move-result p0

    return p0
.end method

.method public OOOOooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOOooo()Z

    move-result p0

    return p0
.end method

.method public OOOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo()Z

    move-result p0

    return p0
.end method

.method public OOOo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0()Z

    move-result p0

    return p0
.end method

.method public OOOo00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo00()Z

    move-result p0

    return p0
.end method

.method public OOOo000()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo000()Z

    move-result p0

    return p0
.end method

.method public OOOo00O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo00O()Z

    move-result p0

    return p0
.end method

.method public OOOo00o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo00o()Z

    move-result p0

    return p0
.end method

.method public OOOo0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0O()Z

    move-result p0

    return p0
.end method

.method public OOOo0O0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0O0()Z

    move-result p0

    return p0
.end method

.method public OOOo0OO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0OO()Z

    move-result p0

    return p0
.end method

.method public OOOo0Oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0Oo()Z

    move-result p0

    return p0
.end method

.method public OOOo0o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooO00()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0o()Z

    move-result p0

    return p0
.end method

.method public OOOo0o0()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0O0()Z

    move-result v0

    if-nez v0, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->ooOOo00()Z

    move-result v0

    if-nez v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0o0()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOOo0oO()Z
    .locals 3

    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/4 v1, 0x0

    const/16 v2, 0x1e

    if-ne v0, v2, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0oO0:Z

    if-nez v0, :cond_0

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0oO:Z

    if-eqz v0, :cond_1

    :cond_0
    return v1

    :cond_1
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v2, 0x1c

    if-lt v0, v2, :cond_2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0oO()Z

    move-result p0

    if-eqz p0, :cond_2

    const/4 v1, 0x1

    :cond_2
    return v1
.end method

.method public OOOo0oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOo0oo()Z

    move-result p0

    return p0
.end method

.method public OOOoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoO()Z

    move-result p0

    return p0
.end method

.method public OOOoO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoO0()Z

    move-result p0

    return p0
.end method

.method public OOOoO00()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O0:Z

    if-eqz v0, :cond_0

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0ooO:Z

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo00O0()Ljava/lang/String;

    move-result-object p0

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toUpperCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v0, ":"

    invoke-virtual {p0, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Ljava/util/Arrays;->asList([Ljava/lang/Object;)Ljava/util/List;

    move-result-object p0

    const-string v0, "ULTRA_WIDE"

    invoke-interface {p0, v0}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public OOOoO0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoO0O()Z

    move-result p0

    return p0
.end method

.method public OOOoO0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoO0o()Z

    move-result p0

    return p0
.end method

.method public OOOoOO()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoOO()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public OOOoOO0()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoOO0()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public OOOoOOO()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoOOO()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public OOOoOOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoOOo()Z

    move-result p0

    return p0
.end method

.method public OOOoOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoOo()Z

    move-result p0

    return p0
.end method

.method public OOOoOo0()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0OOoo()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO0Ooo()Z

    move-result v0

    if-nez v0, :cond_1

    :cond_0
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0oo0()Z

    move-result p0

    if-eqz p0, :cond_2

    :cond_1
    const/4 p0, 0x1

    goto :goto_0

    :cond_2
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOOoOoO()Z
    .locals 2

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOoOoO0()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOoOoOo()Z

    move-result p0

    xor-int/lit8 p0, p0, 0x1

    return p0

    :cond_0
    invoke-static {v0}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result p0

    return p0
.end method

.method public OOOoOoo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO()Z

    move-result p0

    return p0
.end method

.method public OOOoo()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoo()I

    move-result p0

    return p0
.end method

.method public OOOoo0()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoo0()I

    move-result p0

    return p0
.end method

.method public OOOoo00()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoo00()I

    move-result p0

    return p0
.end method

.method public OOOoo0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoo0O()Z

    move-result p0

    return p0
.end method

.method public OOOoo0o()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoo0o()I

    move-result p0

    return p0
.end method

.method public OOOooO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOooO()Z

    move-result p0

    return p0
.end method

.method public OOOooO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOooO0()Z

    move-result p0

    return p0
.end method

.method public OOOooOO()Z
    .locals 2

    invoke-static {}, Lcom/android/camera/Display;->getWindowHeight()I

    move-result v0

    int-to-float v0, v0

    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v1

    int-to-float v1, v1

    div-float/2addr v0, v1

    const v1, 0x400aaaab

    cmpl-float v0, v0, v1

    if-ltz v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOooOO()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOOooOo()Z
    .locals 4

    invoke-static {}, Lcom/android/camera/Display;->getWindowHeight()I

    move-result v0

    int-to-float v0, v0

    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v1

    int-to-float v1, v1

    div-float/2addr v0, v1

    const v1, 0x40071c72

    sub-float/2addr v0, v1

    invoke-static {v0}, Ljava/lang/Math;->abs(F)F

    move-result v0

    float-to-double v0, v0

    const-wide v2, 0x3f947ae147ae147bL    # 0.02

    cmpg-double v0, v0, v2

    if-gtz v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOooOo()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOOooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOooo()Z

    move-result p0

    return p0
.end method

.method public OOOooo0()Z
    .locals 2

    invoke-static {}, Lcom/android/camera/Display;->getWindowHeight()I

    move-result v0

    int-to-float v0, v0

    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v1

    int-to-float v1, v1

    div-float/2addr v0, v1

    const v1, 0x400e38e4

    cmpl-float v0, v0, v1

    if-ltz v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOooo0()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOOoooO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOOoooO()Z

    move-result p0

    return p0
.end method

.method public OOOoooo()Z
    .locals 1

    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOOoooo()Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0OOoo()Z

    move-result v0

    if-nez v0, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0O0OO()Z

    move-result v0

    if-eqz v0, :cond_1

    :cond_0
    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo00O()Z

    move-result p0

    if-eqz p0, :cond_1

    const/4 p0, 0x1

    goto :goto_0

    :cond_1
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0()Z

    move-result p0

    return p0
.end method

.method public OOo00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo00()Z

    move-result p0

    return p0
.end method

.method public OOo000()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo000()Z

    move-result p0

    return p0
.end method

.method public OOo0000()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0000()Z

    move-result p0

    return p0
.end method

.method public OOo000O()Z
    .locals 4

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo000o()Z

    move-result v0

    if-nez v0, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo00O()Z

    move-result p0

    return p0

    :cond_0
    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOo000O()Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo000o()Z

    move-result p0

    if-eqz p0, :cond_2

    const/16 p0, 0xa3

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/global/DataItemGlobal;->getCurrentMode()I

    move-result v0

    if-eq p0, v0, :cond_1

    const/16 p0, 0xa5

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/global/DataItemGlobal;->getCurrentMode()I

    move-result v0

    if-ne p0, v0, :cond_2

    :cond_1
    invoke-static {}, Lcom/android/camera/CameraSettings;->getCameraId()I

    move-result p0

    if-nez p0, :cond_2

    invoke-static {}, Lcom/android/camera/CameraSettings;->isUltraPixelOn()Z

    move-result p0

    if-nez p0, :cond_2

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object p0

    invoke-virtual {p0}, Lcom/android/camera/data/data/global/DataItemGlobal;->getCurrentMode()I

    move-result p0

    invoke-static {p0}, Lcom/android/camera/CameraSettings;->getRetainZoom(I)F

    move-result p0

    float-to-double v0, p0

    const-wide/high16 v2, 0x3ff0000000000000L    # 1.0

    cmpl-double p0, v0, v2

    if-ltz p0, :cond_2

    sget-object p0, LO00000Oo/O00000oO/O000000o/O00000o;->TAG:Ljava/lang/String;

    const-string v0, "Algo up enabled for mm-camera"

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    const/4 p0, 0x1

    return p0

    :cond_2
    sget-object p0, LO00000Oo/O00000oO/O000000o/O00000o;->TAG:Ljava/lang/String;

    const-string v0, "Algo up disabled for mm-camera"

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    const/4 p0, 0x0

    return p0
.end method

.method public OOo000o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo000o()Z

    move-result p0

    return p0
.end method

.method public OOo00O()Z
    .locals 2

    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x1c

    if-le v0, v1, :cond_0

    invoke-direct {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->Oo0Oooo()Z

    move-result p0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo000O()Z

    move-result p0

    return p0
.end method

.method public OOo00O0()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo00O0()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public OOo00OO()Z
    .locals 1

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo00OO()Z

    move-result p0

    const/4 v0, 0x0

    if-eqz p0, :cond_0

    const-string p0, "close.append.yuv"

    invoke-static {p0, v0}, Lcom/xiaomi/camera/util/SystemProperties;->getBoolean(Ljava/lang/String;Z)Z

    move-result p0

    if-nez p0, :cond_0

    const/4 v0, 0x1

    :cond_0
    return v0
.end method

.method public OOo00Oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo00Oo()Z

    move-result p0

    return p0
.end method

.method public OOo00o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo00o()Z

    move-result p0

    return p0
.end method

.method public OOo00o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo00o0()Z

    move-result p0

    return p0
.end method

.method public OOo00oO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo00oO()Z

    move-result p0

    return p0
.end method

.method public OOo00oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo00oo()Z

    move-result p0

    return p0
.end method

.method public OOo0O0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0O0()Z

    move-result p0

    if-eqz p0, :cond_0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->isMTKPlatform()Z

    move-result p0

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOo0O00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0O00()Z

    move-result p0

    return p0
.end method

.method public OOo0O0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0O0O()Z

    move-result p0

    return p0
.end method

.method public OOo0O0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0O0o()Z

    move-result p0

    return p0
.end method

.method public OOo0OO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooo0O()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0OO()Z

    move-result p0

    return p0
.end method

.method public OOo0OO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0OO0()Z

    move-result p0

    return p0
.end method

.method public OOo0OOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0OOO()Z

    move-result p0

    return p0
.end method

.method public OOo0OOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0OOo()Z

    move-result p0

    return p0
.end method

.method public OOo0Oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0Oo()Z

    move-result p0

    return p0
.end method

.method public OOo0Oo0()Z
    .locals 1

    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOo0Oo0()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0OOoo()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOo0OoO()Z
    .locals 1

    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOo0OoO()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0OOoo()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOo0Ooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0o000()Z

    move-result p0

    return p0
.end method

.method public OOo0o()Z
    .locals 6

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0oo0O0()Ljava/lang/String;

    move-result-object p0

    const/4 v0, 0x0

    if-nez p0, :cond_0

    return v0

    :cond_0
    sget-object v1, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v1}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v1

    const-string v2, "notelemfnr"

    invoke-virtual {v1, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_1

    return v0

    :cond_1
    sget-object v1, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v1}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v1, ";"

    invoke-virtual {p0, v1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v1, p0

    if-lez v1, :cond_3

    array-length v1, p0

    move v3, v0

    :goto_0
    if-ge v3, v1, :cond_3

    aget-object v4, p0, v3

    sget-object v5, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {v4, v5}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v4

    const-string v5, ":"

    invoke-virtual {v4, v5}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v4

    aget-object v5, v4, v0

    invoke-virtual {v5, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    const/4 p0, 0x1

    aget-object p0, v4, p0

    const-string v0, "1"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0

    :cond_2
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_3
    return v0
.end method

.method public OOo0o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0o0()Z

    move-result p0

    return p0
.end method

.method public OOo0o00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0o00()Z

    move-result p0

    return p0
.end method

.method public OOo0o0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0o0O()Z

    move-result p0

    return p0
.end method

.method public OOo0o0o()Z
    .locals 6

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0oo0O0()Ljava/lang/String;

    move-result-object p0

    const/4 v0, 0x0

    if-nez p0, :cond_0

    return v0

    :cond_0
    sget-object v1, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v1}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v1

    const-string v2, "mfnr"

    invoke-virtual {v1, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_1

    return v0

    :cond_1
    sget-object v1, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v1}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v1, ";"

    invoke-virtual {p0, v1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v1, p0

    if-lez v1, :cond_3

    array-length v1, p0

    move v3, v0

    :goto_0
    if-ge v3, v1, :cond_3

    aget-object v4, p0, v3

    sget-object v5, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {v4, v5}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v4

    const-string v5, ":"

    invoke-virtual {v4, v5}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v4

    aget-object v5, v4, v0

    invoke-virtual {v5, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    const/4 p0, 0x1

    aget-object p0, v4, p0

    const-string v0, "1"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0

    :cond_2
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_3
    return v0
.end method

.method public OOo0oO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0oO()Z

    move-result p0

    return p0
.end method

.method public OOo0oO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0oO0()Z

    move-result p0

    return p0
.end method

.method public OOo0oOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0oOO()Z

    move-result p0

    return p0
.end method

.method public OOo0oOo()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public OOo0oo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0oo()Z

    move-result p0

    return p0
.end method

.method public OOo0oo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0oo0()Z

    move-result p0

    return p0
.end method

.method public OOo0ooO()Z
    .locals 0

    const/4 p0, 0x1

    return p0
.end method

.method public OOo0ooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOo0ooo()Z

    move-result p0

    return p0
.end method

.method public OOoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO()Z

    move-result p0

    return p0
.end method

.method public OOoO0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO0()Z

    move-result p0

    return p0
.end method

.method public OOoO00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO00()Z

    move-result p0

    return p0
.end method

.method public OOoO000()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO000()Z

    move-result p0

    return p0
.end method

.method public OOoO00O()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0OOoo()Z

    move-result v0

    if-nez v0, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0Oo00()Z

    move-result v0

    if-eqz v0, :cond_1

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO00O()Z

    move-result p0

    if-eqz p0, :cond_1

    const/4 p0, 0x1

    goto :goto_0

    :cond_1
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOoO00o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO00o()Z

    move-result p0

    return p0
.end method

.method public OOoO0O()Z
    .locals 4

    sget-wide v0, Lcom/android/camera/Util;->TOTAL_MEMORY_GB:J

    const-wide/16 v2, 0x6

    cmp-long v0, v0, v2

    if-lez v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO0O()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public OOoO0O0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO0O0()Z

    move-result p0

    return p0
.end method

.method public OOoO0OO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO0OO()Z

    move-result p0

    return p0
.end method

.method public OOoO0Oo()Z
    .locals 2

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemGlobal()Lcom/android/camera/data/data/global/DataItemGlobal;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/global/DataItemGlobal;->getIntentType()I

    move-result v0

    const/4 v1, 0x1

    if-ne v0, v1, :cond_0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->isMTKPlatform()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO0Oo()Z

    move-result p0

    return p0
.end method

.method public OOoO0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO0o()Z

    move-result p0

    return p0
.end method

.method public OOoO0o0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO0o0()Z

    move-result p0

    return p0
.end method

.method public OOoO0oO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoO0oO()Z

    move-result p0

    return p0
.end method

.method public OOoO0oo()Z
    .locals 6

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0oo0O0()Ljava/lang/String;

    move-result-object p0

    const/4 v0, 0x0

    if-nez p0, :cond_0

    return v0

    :cond_0
    sget-object v1, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v1}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v1

    const-string v2, "telesr"

    invoke-virtual {v1, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_1

    return v0

    :cond_1
    sget-object v1, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v1}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    const-string v1, ";"

    invoke-virtual {p0, v1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v1, p0

    if-lez v1, :cond_3

    array-length v1, p0

    move v3, v0

    :goto_0
    if-ge v3, v1, :cond_3

    aget-object v4, p0, v3

    sget-object v5, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {v4, v5}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v4

    const-string v5, ":"

    invoke-virtual {v4, v5}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v4

    aget-object v5, v4, v0

    invoke-virtual {v5, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_2

    const/4 p0, 0x1

    aget-object p0, v4, p0

    const-string v0, "1"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0

    :cond_2
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_3
    return v0
.end method

.method public OOoOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOO()Z

    move-result p0

    return p0
.end method

.method public OOoOO00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOO00()Z

    move-result p0

    return p0
.end method

.method public OOoOO0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOO0O()Z

    move-result p0

    return p0
.end method

.method public OOoOO0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOO0o()Z

    move-result p0

    return p0
.end method

.method public OOoOOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOOO()Z

    move-result p0

    return p0
.end method

.method public OOoOOO0()Z
    .locals 4

    sget-wide v0, Lcom/android/camera/Util;->TOTAL_MEMORY_GB:J

    const-wide/16 v2, 0x6

    cmp-long v0, v0, v2

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    if-gez v0, :cond_0

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0o00o()Z

    move-result p0

    return p0

    :cond_0
    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOOO0()Z

    move-result p0

    return p0
.end method

.method public OOoOOOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOOOO()Z

    move-result p0

    return p0
.end method

.method public OOoOOOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOOOo()Z

    move-result p0

    return p0
.end method

.method public OOoOOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOOo()Z

    move-result p0

    return p0
.end method

.method public OOoOOo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOOo0()Z

    move-result p0

    return p0
.end method

.method public OOoOOoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOOoO()Z

    move-result p0

    return p0
.end method

.method public OOoOOoo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOOoo()Z

    move-result p0

    return p0
.end method

.method public OOoOo()Z
    .locals 3

    const/4 v0, 0x0

    const-string v1, "miuicamera.sat.video"

    invoke-static {v1, v0}, Lcom/xiaomi/camera/util/SystemProperties;->getBoolean(Ljava/lang/String;Z)Z

    move-result v1

    if-eqz v1, :cond_0

    sget-object p0, LO00000Oo/O00000oO/O000000o/O00000o;->TAG:Ljava/lang/String;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "sat video debug prop:"

    invoke-virtual {v0, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return v1

    :cond_0
    sget-boolean v1, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0ooO:Z

    if-eqz v1, :cond_1

    sget-boolean v1, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O0O:Z

    if-nez v1, :cond_1

    return v0

    :cond_1
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOo()Z

    move-result p0

    return p0
.end method

.method public OOoOo0()Z
    .locals 1

    sget-boolean v0, Lcom/android/camera/HybridZoomingSystem;->IS_3_OR_MORE_SAT:Z

    if-nez v0, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOo0()Z

    move-result p0

    return p0
.end method

.method public OOoOo00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOo00()Z

    move-result p0

    return p0
.end method

.method public OOoOo0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOo0O()Z

    move-result p0

    return p0
.end method

.method public OOoOo0o()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOo0o()Z

    move-result p0

    return p0
.end method

.method public OOoOoO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOoO()Z

    move-result p0

    return p0
.end method

.method public OOoOoO0()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOoO0()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public OOoOoOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOoOO()Z

    move-result p0

    return p0
.end method

.method public OOoOoOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoOoOo()Z

    move-result p0

    return p0
.end method

.method public OOoOoo0()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0O0()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->ooOOo00()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0o0()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0OO()Z

    move-result p0

    if-eqz p0, :cond_0

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 p0, 0x1

    :goto_1
    return p0
.end method

.method public OOoo00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoo00()Z

    move-result p0

    return p0
.end method

.method public OOooOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOooOo()Z

    move-result p0

    return p0
.end method

.method public OOoooo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OOoooo()Z

    move-result p0

    return p0
.end method

.method public Oo0Oo0O()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0Oo0O()Z

    move-result p0

    return p0
.end method

.method public Oo0Ooo0()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->Oo0Ooo0()Z

    move-result p0

    return p0
.end method

.method public OoOOO()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OoOOO()I

    move-result p0

    return p0
.end method

.method public OoOOoOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->OoOOoOo()Z

    move-result p0

    return p0
.end method

.method public getClassName()Ljava/lang/String;
    .locals 2

    sget-object p0, Landroid/os/Build;->DEVICE:Ljava/lang/String;

    invoke-virtual {p0}, Ljava/lang/String;->hashCode()I

    move-result v0

    const v1, 0x32afd5

    if-eq v0, v1, :cond_0

    goto :goto_0

    :cond_0
    const-string v0, "lime"

    invoke-virtual {p0, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1

    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, -0x1

    :goto_1
    if-eqz v0, :cond_2

    goto :goto_2

    :cond_2
    invoke-static {}, Lcom/android/camera/Util;->isInternationalBuild()Z

    move-result v0

    if-eqz v0, :cond_3

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, "gl"

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    :cond_3
    :goto_2
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->O0ooOO()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public getConfig()Lcom/mi/device/Common;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    return-object p0
.end method

.method public getRawSuperNightImpl()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->getRawSuperNightImpl()I

    move-result p0

    return p0
.end method

.method public is4K30FpsEISSupported()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->is4K30FpsEISSupported()Z

    move-result p0

    return p0
.end method

.method public isCinematicPhotoSupported()Z
    .locals 2

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00o:Z

    if-eqz v0, :cond_0

    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x1c

    if-ne v0, v1, :cond_0

    const/4 p0, 0x0

    return p0

    :cond_0
    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isCinematicPhotoSupported()Z

    move-result p0

    return p0
.end method

.method public isHighQualityPreferred()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isHighQualityPreferred()Z

    move-result p0

    return p0
.end method

.method public isPad()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isPad()Z

    move-result p0

    return p0
.end method

.method public isSRRequireReprocess()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSRRequireReprocess()Z

    move-result p0

    return p0
.end method

.method public isSupportBeautyBody()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSupportBeautyBody()Z

    move-result p0

    return p0
.end method

.method public isSupportBokehAdjust()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSupportBokehAdjust()Z

    move-result p0

    return p0
.end method

.method public isSupportMacroMode()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSupportMacroMode()Z

    move-result p0

    return p0
.end method

.method public isSupportNormalWideLDC()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSupportNormalWideLDC()Z

    move-result p0

    return p0
.end method

.method public isSupportShortVideoBeautyBody()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSupportShortVideoBeautyBody()Z

    move-result p0

    return p0
.end method

.method public isSupportSlowMotionVideoEditor()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSupportSlowMotionVideoEditor()Z

    move-result p0

    return p0
.end method

.method public isSupportUltraWide()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSupportUltraWide()Z

    move-result p0

    return p0
.end method

.method public isSupportUltraWideLDC()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->isSupportUltraWideLDC()Z

    move-result p0

    return p0
.end method

.method public o00O0oO0()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->o00O0oO0()I

    move-result p0

    return p0
.end method

.method public o00OOOOO()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->o00OOOOO()Z

    move-result p0

    return p0
.end method

.method public o0OOoOoo()Z
    .locals 4

    invoke-static {}, Lcom/android/camera/Display;->getWindowHeight()I

    move-result v0

    int-to-float v0, v0

    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v1

    int-to-float v1, v1

    div-float/2addr v0, v1

    const v1, 0x40055555

    sub-float/2addr v0, v1

    invoke-static {v0}, Ljava/lang/Math;->abs(F)F

    move-result v0

    float-to-double v0, v0

    const-wide v2, 0x3f947ae147ae147bL    # 0.02

    cmpg-double v0, v0, v2

    if-gez v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->o0OOoOoo()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public o0ooo0OO()Ljava/lang/Boolean;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->o0ooo0OO()Z

    move-result p0

    invoke-static {p0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object p0

    return-object p0
.end method

.method public oO0OO0()Z
    .locals 3

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->O0oo0Oo()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    const/4 v1, 0x0

    if-nez v0, :cond_1

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object v0

    const-string v2, ":"

    invoke-virtual {v0, v2}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_0

    sget-object v0, Ljava/util/Locale;->ENGLISH:Ljava/util/Locale;

    invoke-virtual {p0, v0}, Ljava/lang/String;->toLowerCase(Ljava/util/Locale;)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {p0, v2}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object p0

    array-length v0, p0

    if-lez v0, :cond_1

    aget-object p0, p0, v1

    invoke-static {p0}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result p0

    move v1, p0

    goto :goto_0

    :cond_0
    invoke-static {p0}, Ljava/lang/Boolean;->parseBoolean(Ljava/lang/String;)Z

    move-result v1

    :cond_1
    :goto_0
    return v1
.end method

.method public oOo00()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->oOo00()Z

    move-result p0

    return p0
.end method

.method public ooO00O0()I
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->ooO00O0()I

    move-result p0

    return p0
.end method

.method public ooOOo00()Z
    .locals 1

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0O0()Z

    move-result v0

    if-nez v0, :cond_0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->ooOOo00()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public ooOo()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->ooOo()Z

    move-result p0

    return p0
.end method

.method public shouldCheckSatFallbackState()Z
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {p0}, Lcom/mi/device/Common;->shouldCheckSatFallbackState()Z

    move-result p0

    return p0
.end method

.method public supportColorEnhance()Z
    .locals 1

    iget-object v0, p0, LO00000Oo/O00000oO/O000000o/O00000o;->mConfig:Lcom/mi/device/Common;

    invoke-virtual {v0}, Lcom/mi/device/Common;->supportColorEnhance()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0OOoo()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

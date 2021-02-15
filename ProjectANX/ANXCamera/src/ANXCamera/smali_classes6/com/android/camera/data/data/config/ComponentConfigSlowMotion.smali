.class public Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;
.super Lcom/android/camera/data/data/ComponentData;
.source ""


# static fields
.field public static final DATA_CONFIG_NEW_SLOW_MOTION_120:Ljava/lang/String; = "slow_motion_120"

.field public static final DATA_CONFIG_NEW_SLOW_MOTION_1920:Ljava/lang/String; = "slow_motion_1920"

.field public static final DATA_CONFIG_NEW_SLOW_MOTION_240:Ljava/lang/String; = "slow_motion_240"

.field public static final DATA_CONFIG_NEW_SLOW_MOTION_480:Ljava/lang/String; = "slow_motion_480"

.field public static final DATA_CONFIG_NEW_SLOW_MOTION_480_DIRECT:Ljava/lang/String; = "slow_motion_480_direct"

.field public static final DATA_CONFIG_NEW_SLOW_MOTION_960:Ljava/lang/String; = "slow_motion_960"

.field private static final TAG:Ljava/lang/String; = "ComponentConfigSlowMotion"


# instance fields
.field public mCurrentQuality:Ljava/lang/String;


# direct methods
.method public constructor <init>(Lcom/android/camera/data/data/config/DataItemConfig;I)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/android/camera/data/data/ComponentData;-><init>(Lcom/android/camera/data/data/DataItemBase;)V

    const-string p1, "5"

    iput-object p1, p0, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->mCurrentQuality:Ljava/lang/String;

    return-void
.end method

.method private getSupportMaxValue(Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    iget-object v0, p0, Lcom/android/camera/data/data/ComponentData;->mItems:Ljava/util/List;

    if-eqz v0, :cond_2

    iget-object v0, p0, Lcom/android/camera/data/data/ComponentData;->mItems:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    if-nez v0, :cond_0

    goto :goto_1

    :cond_0
    iget-object v0, p0, Lcom/android/camera/data/data/ComponentData;->mItems:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    add-int/lit8 v0, v0, -0x1

    :goto_0
    if-ltz v0, :cond_2

    iget-object v1, p0, Lcom/android/camera/data/data/ComponentData;->mItems:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/android/camera/data/data/ComponentDataItem;

    iget-boolean v1, v1, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    if-nez v1, :cond_1

    iget-object p0, p0, Lcom/android/camera/data/data/ComponentData;->mItems:Ljava/util/List;

    invoke-interface {p0, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/data/data/ComponentDataItem;

    iget-object p0, p0, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    return-object p0

    :cond_1
    add-int/lit8 v0, v0, -0x1

    goto :goto_0

    :cond_2
    :goto_1
    return-object p1
.end method

.method private getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;
    .locals 7

    invoke-static {}, Lcom/android/camera/CameraAppImpl;->getAndroidContext()Landroid/content/Context;

    move-result-object p0

    invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object p0

    invoke-virtual {p1}, Ljava/lang/String;->hashCode()I

    move-result v0

    const/4 v1, 0x5

    const/4 v2, 0x4

    const/4 v3, 0x3

    const/4 v4, 0x2

    const/4 v5, 0x0

    const/4 v6, 0x1

    sparse-switch v0, :sswitch_data_0

    goto :goto_0

    :sswitch_0
    const-string v0, "slow_motion_480_direct"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_0

    move p1, v3

    goto :goto_1

    :sswitch_1
    const-string v0, "slow_motion_960"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_0

    move p1, v2

    goto :goto_1

    :sswitch_2
    const-string v0, "slow_motion_480"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_0

    move p1, v4

    goto :goto_1

    :sswitch_3
    const-string v0, "slow_motion_240"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_0

    move p1, v6

    goto :goto_1

    :sswitch_4
    const-string v0, "slow_motion_120"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_0

    move p1, v5

    goto :goto_1

    :sswitch_5
    const-string v0, "slow_motion_1920"

    invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p1

    if-eqz p1, :cond_0

    move p1, v1

    goto :goto_1

    :cond_0
    :goto_0
    const/4 p1, -0x1

    :goto_1
    if-eqz p1, :cond_5

    if-eq p1, v6, :cond_4

    const v0, 0x7f100040

    if-eq p1, v4, :cond_3

    if-eq p1, v3, :cond_3

    if-eq p1, v2, :cond_2

    if-eq p1, v1, :cond_1

    const/4 p0, 0x0

    return-object p0

    :cond_1
    new-array p1, v6, [Ljava/lang/Object;

    const/16 v1, 0x780

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    aput-object v1, p1, v5

    invoke-virtual {p0, v0, p1}, Landroid/content/res/Resources;->getString(I[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_2
    const p1, 0x7f10003d

    invoke-virtual {p0, p1}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_3
    new-array p1, v6, [Ljava/lang/Object;

    const/16 v1, 0x1e0

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    aput-object v1, p1, v5

    invoke-virtual {p0, v0, p1}, Landroid/content/res/Resources;->getString(I[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_4
    const p1, 0x7f10003c

    invoke-virtual {p0, p1}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_5
    const p1, 0x7f10003b

    invoke-virtual {p0, p1}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object p0

    return-object p0

    :sswitch_data_0
    .sparse-switch
        -0x4d7933ef -> :sswitch_5
        -0x44904cdc -> :sswitch_4
        -0x449048dd -> :sswitch_3
        -0x449040df -> :sswitch_2
        -0x44902e58 -> :sswitch_1
        0x1043c2c7 -> :sswitch_0
    .end sparse-switch
.end method

.method private isDefaultValue(I)Z
    .locals 1

    invoke-virtual {p0, p1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getComponentValue(I)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {p0, p1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getDefaultValue(I)Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method


# virtual methods
.method protected checkValueValid(ILjava/lang/String;)Z
    .locals 1

    invoke-virtual {p0}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getItems()Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :cond_0
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result p1

    if-eqz p1, :cond_1

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Lcom/android/camera/data/data/ComponentDataItem;

    iget-object v0, p1, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {p2, v0}, Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_0

    iget-boolean p1, p1, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    if-nez p1, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_1
    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    const-string p1, "checkValueValid: invalid value: "

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    const-string p1, "ComponentConfigSlowMotion"

    invoke-static {p1, p0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    const/4 p0, 0x0

    return p0
.end method

.method public getComponentValue(I)Ljava/lang/String;
    .locals 4

    invoke-virtual {p0, p1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getDefaultValue(I)Ljava/lang/String;

    move-result-object v0

    iget-object v1, p0, Lcom/android/camera/data/data/ComponentData;->mParentDataItem:Lcom/android/camera/data/data/DataItemBase;

    invoke-virtual {p0, p1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getKey(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2, v0}, Lcom/android/camera/data/data/DataItemBase;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    if-eqz v1, :cond_0

    invoke-virtual {p0, p1, v1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->checkValueValid(ILjava/lang/String;)Z

    move-result p1

    if-nez p1, :cond_0

    const-class p1, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;

    invoke-virtual {p1}, Ljava/lang/Class;->getSimpleName()Ljava/lang/String;

    move-result-object p1

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "reset invalid value "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {p1, v1}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    invoke-direct {p0, v0}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getSupportMaxValue(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    :cond_0
    return-object v1
.end method

.method public getDefaultValue(I)Ljava/lang/String;
    .locals 0
    .annotation build Landroidx/annotation/NonNull;
    .end annotation

    iget-object p0, p0, Lcom/android/camera/data/data/ComponentData;->mItems:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    if-nez p0, :cond_0

    const-string p0, ""

    goto :goto_0

    :cond_0
    const-string p0, "slow_motion_120"

    :goto_0
    return-object p0
.end method

.method public getDisplayTitleString()I
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public getItems()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/data/data/ComponentData;->mItems:Ljava/util/List;

    return-object p0
.end method

.method public getKey(I)Ljava/lang/String;
    .locals 0

    const-string p0, "key_new_slow_motion"

    return-object p0
.end method

.method public getValueSelectedStringIdIgnoreClose(I)Ljava/lang/String;
    .locals 1

    invoke-direct {p0, p1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->isDefaultValue(I)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    invoke-virtual {p0, p1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getComponentValue(I)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public isSlowMotionFps1920()Z
    .locals 1

    const/16 v0, 0xac

    invoke-virtual {p0, v0}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getComponentValue(I)Ljava/lang/String;

    move-result-object p0

    const-string v0, "slow_motion_1920"

    invoke-virtual {v0, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public isSlowMotionFps480()Z
    .locals 1

    const/16 v0, 0xac

    invoke-virtual {p0, v0}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getComponentValue(I)Ljava/lang/String;

    move-result-object p0

    const-string v0, "slow_motion_480"

    invoke-virtual {v0, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public isSlowMotionFps960()Z
    .locals 1

    const/16 v0, 0xac

    invoke-virtual {p0, v0}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getComponentValue(I)Ljava/lang/String;

    move-result-object p0

    const-string v0, "slow_motion_960"

    invoke-virtual {v0, p0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method public reInit(IILcom/android/camera/data/data/config/ComponentConfigSlowMotionQuality;Lcom/android/camera2/CameraCapabilities;)V
    .locals 16

    move-object/from16 v0, p0

    move/from16 v1, p1

    new-instance v2, Ljava/util/ArrayList;

    invoke-direct {v2}, Ljava/util/ArrayList;-><init>()V

    const/16 v3, 0xac

    if-eq v1, v3, :cond_0

    goto/16 :goto_4

    :cond_0
    invoke-static/range {p4 .. p4}, Lcom/android/camera/CameraSettings;->getSupportedHfrSettings(Lcom/android/camera2/CameraCapabilities;)Ljava/util/ArrayList;

    move-result-object v3

    move-object/from16 v4, p3

    invoke-virtual {v4, v1}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotionQuality;->getComponentValue(I)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->mCurrentQuality:Ljava/lang/String;

    new-instance v1, Ljava/lang/StringBuffer;

    invoke-direct {v1}, Ljava/lang/StringBuffer;-><init>()V

    iget-object v4, v0, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->mCurrentQuality:Ljava/lang/String;

    const-string v5, "5"

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    const-string v5, "1280x720:"

    if-eqz v4, :cond_1

    goto :goto_0

    :cond_1
    iget-object v4, v0, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->mCurrentQuality:Ljava/lang/String;

    const-string v6, "6"

    invoke-virtual {v4, v6}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_2

    const-string v5, "1920x1080:"

    :cond_2
    :goto_0
    const-string v4, "120"

    const-string v6, "slow_motion_120"

    const v7, 0x7f080301

    const/4 v8, 0x0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v9

    if-nez p2, :cond_7

    invoke-virtual {v9}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0()Z

    move-result v9

    const-string v10, "slow_motion_960"

    const v12, 0x7f080304

    const-string v13, "240"

    const-string v14, "slow_motion_240"

    const v15, 0x7f080303

    if-eqz v9, :cond_3

    new-instance v9, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v6}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v11

    invoke-direct {v9, v7, v7, v11, v6}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-interface {v3, v4}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v4

    xor-int/lit8 v4, v4, 0x1

    iput-boolean v4, v9, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v9}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v4, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v14}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    invoke-direct {v4, v15, v15, v6, v14}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v13}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-interface {v3, v6}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v6

    xor-int/lit8 v6, v6, 0x1

    iput-boolean v6, v4, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v4}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v4, Lcom/android/camera/data/data/ComponentDataItem;

    const-string v6, "slow_motion_480_direct"

    invoke-direct {v0, v6}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    invoke-direct {v4, v12, v12, v7, v6}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    const-string v6, "480"

    invoke-virtual {v1, v6}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-interface {v3, v7}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v7

    xor-int/lit8 v7, v7, 0x1

    iput-boolean v7, v4, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v4}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v4, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v10}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    const v9, 0x7f080305

    invoke-direct {v4, v9, v9, v7, v10}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v13}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-interface {v3, v7}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v7

    xor-int/lit8 v7, v7, 0x1

    iput-boolean v7, v4, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v4}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v4, Lcom/android/camera/data/data/ComponentDataItem;

    const v7, 0x7f080302

    const v9, 0x7f080302

    const-string v10, "slow_motion_1920"

    invoke-direct {v0, v10}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v10

    const-string v11, "slow_motion_1920"

    invoke-direct {v4, v7, v9, v10, v11}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v6}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    goto/16 :goto_2

    :cond_3
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v9

    invoke-virtual {v9}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0O0()Z

    move-result v9

    if-eqz v9, :cond_4

    new-instance v9, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v6}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v11

    invoke-direct {v9, v7, v7, v11, v6}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-interface {v3, v4}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v4

    xor-int/lit8 v4, v4, 0x1

    iput-boolean v4, v9, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v9}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v4, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v14}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    invoke-direct {v4, v15, v15, v6, v14}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v13}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-interface {v3, v6}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v6

    xor-int/lit8 v6, v6, 0x1

    iput-boolean v6, v4, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v4}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v4, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v10}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    const v7, 0x7f080305

    invoke-direct {v4, v7, v7, v6, v10}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    goto/16 :goto_1

    :cond_4
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v9

    invoke-virtual {v9}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v9

    invoke-virtual {v9}, Lcom/mi/device/Common;->Oo0OoO()Z

    move-result v9

    if-eqz v9, :cond_5

    new-instance v9, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v6}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v10

    invoke-direct {v9, v7, v7, v10, v6}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-interface {v3, v6}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v6

    xor-int/lit8 v6, v6, 0x1

    iput-boolean v6, v9, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v9}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v6, Lcom/android/camera/data/data/ComponentDataItem;

    const-string v7, "slow_motion_480"

    invoke-direct {v0, v7}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v9

    invoke-direct {v6, v12, v12, v9, v7}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-interface {v3, v1}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v1

    xor-int/lit8 v1, v1, 0x1

    iput-boolean v1, v6, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v6}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto/16 :goto_4

    :cond_5
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v9

    invoke-virtual {v9}, LO00000Oo/O00000oO/O000000o/O00000o;->ooOOo00()Z

    move-result v9

    if-eqz v9, :cond_6

    new-instance v9, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v6}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v10

    invoke-direct {v9, v7, v7, v10, v6}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-interface {v3, v4}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v4

    xor-int/lit8 v4, v4, 0x1

    iput-boolean v4, v9, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v9}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    new-instance v4, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v14}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v6

    invoke-direct {v4, v15, v15, v6, v14}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    :goto_1
    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v13}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    :goto_2
    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-interface {v3, v1}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v1

    xor-int/lit8 v1, v1, 0x1

    iput-boolean v1, v4, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v4}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto :goto_4

    :cond_6
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v9

    invoke-virtual {v9}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0o0()Z

    move-result v9

    if-eqz v9, :cond_8

    new-instance v9, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v6}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v10

    invoke-direct {v9, v7, v7, v10, v6}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    goto :goto_3

    :cond_7
    invoke-virtual {v9}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOo0OO()Z

    move-result v9

    if-eqz v9, :cond_8

    new-instance v9, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-direct {v0, v6}, Lcom/android/camera/data/data/config/ComponentConfigSlowMotion;->getValueSelectedString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v10

    invoke-direct {v9, v7, v7, v10, v6}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(IILjava/lang/String;Ljava/lang/String;)V

    :goto_3
    invoke-virtual {v1, v8}, Ljava/lang/StringBuffer;->setLength(I)V

    invoke-virtual {v1, v5}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;

    invoke-virtual {v1}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-interface {v3, v1}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v1

    xor-int/lit8 v1, v1, 0x1

    iput-boolean v1, v9, Lcom/android/camera/data/data/ComponentDataItem;->mIsDisabled:Z

    invoke-interface {v2, v9}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_8
    :goto_4
    invoke-static {v2}, Ljava/util/Collections;->unmodifiableList(Ljava/util/List;)Ljava/util/List;

    move-result-object v1

    iput-object v1, v0, Lcom/android/camera/data/data/ComponentData;->mItems:Ljava/util/List;

    return-void
.end method

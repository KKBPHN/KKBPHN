.class public Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static DSP_WAKEUP_INFO_PATTERN:Ljava/util/regex/Pattern;


# instance fields
.field mIsValideInfo:Z

.field private mMxeWakeupAec:Z

.field private mMxeWakeupEndTime:J

.field private mMxeWakeupSocre:I

.field private mMxeWakeupStartTime:J

.field private mPcmLength:I

.field private mWakeupRequiredLeadingSilence:J


# direct methods
.method static constructor <clinit>()V
    .locals 1

    const-string v0, "^FMT:(\\d+);VENDOR:NUANCE;START:(\\d+);FTT_LENGTH:(\\d+);SCORE:(\\d+);AEC:(\\d+);PCM_LENGTH:(\\d+)$"

    invoke-static {v0}, Ljava/util/regex/Pattern;->compile(Ljava/lang/String;)Ljava/util/regex/Pattern;

    move-result-object v0

    sput-object v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->DSP_WAKEUP_INFO_PATTERN:Ljava/util/regex/Pattern;

    return-void
.end method

.method public constructor <init>()V
    .locals 3

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const-wide/16 v0, -0x1

    iput-wide v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupStartTime:J

    iput-wide v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupEndTime:J

    const/4 v2, -0x1

    iput v2, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupSocre:I

    iput-wide v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mWakeupRequiredLeadingSilence:J

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupAec:Z

    iput-boolean v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mIsValideInfo:Z

    iput v2, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mPcmLength:I

    return-void
.end method


# virtual methods
.method public getPcmLength()I
    .locals 0

    iget p0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mPcmLength:I

    return p0
.end method

.method public getWakeupEndTime()J
    .locals 2

    iget-wide v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupEndTime:J

    return-wide v0
.end method

.method public getWakeupScore()I
    .locals 0

    iget p0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupSocre:I

    return p0
.end method

.method public getWakeupStartTime()J
    .locals 2

    iget-wide v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupStartTime:J

    return-wide v0
.end method

.method public isInfoValid()Z
    .locals 0

    iget-boolean p0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mIsValideInfo:Z

    return p0
.end method

.method public isWakeupAec()Z
    .locals 0

    iget-boolean p0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupAec:Z

    return p0
.end method

.method public parse([S)V
    .locals 23

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    array-length v2, v1

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    array-length v4, v1

    const/4 v5, -0x1

    const-wide/16 v6, -0x1

    move v12, v5

    move v13, v12

    move-wide v14, v6

    move-wide/from16 v16, v14

    const/4 v5, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x0

    const/4 v9, 0x0

    const/4 v10, 0x0

    const/4 v11, 0x0

    :goto_0
    const-string v8, "DSPWakeupInfoParser"

    move/from16 v18, v12

    if-ge v5, v4, :cond_9

    aget-short v12, v1, v5

    int-to-byte v1, v12

    move/from16 v19, v4

    shr-int/lit8 v4, v12, 0x8

    int-to-byte v4, v4

    move/from16 v20, v13

    if-nez v6, :cond_6

    if-nez v1, :cond_0

    :goto_1
    const/4 v6, 0x1

    goto :goto_2

    :cond_0
    int-to-char v1, v1

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    if-nez v4, :cond_1

    goto :goto_1

    :cond_1
    int-to-char v1, v4

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(C)Ljava/lang/StringBuilder;

    :goto_2
    if-eqz v6, :cond_5

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v13, "Dsp header is "

    invoke-virtual {v4, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    invoke-static {v8, v4}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    sget-object v4, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->DSP_WAKEUP_INFO_PATTERN:Ljava/util/regex/Pattern;

    invoke-virtual {v4, v1}, Ljava/util/regex/Pattern;->matcher(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;

    move-result-object v1

    invoke-virtual {v1}, Ljava/util/regex/Matcher;->find()Z

    move-result v4

    if-nez v4, :cond_2

    const-string v1, "format pattern not valid"

    invoke-static {v8, v1}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    goto/16 :goto_6

    :cond_2
    const/4 v4, 0x1

    invoke-virtual {v1, v4}, Ljava/util/regex/Matcher;->group(I)Ljava/lang/String;

    move-result-object v13

    invoke-static {v13}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v4

    const/4 v13, 0x2

    invoke-virtual {v1, v13}, Ljava/util/regex/Matcher;->group(I)Ljava/lang/String;

    move-result-object v14

    invoke-static {v14}, Ljava/lang/Long;->parseLong(Ljava/lang/String;)J

    move-result-wide v14

    const/4 v13, 0x3

    invoke-virtual {v1, v13}, Ljava/util/regex/Matcher;->group(I)Ljava/lang/String;

    move-result-object v13

    invoke-static {v13}, Ljava/lang/Long;->parseLong(Ljava/lang/String;)J

    move-result-wide v16

    const/4 v13, 0x4

    invoke-virtual {v1, v13}, Ljava/util/regex/Matcher;->group(I)Ljava/lang/String;

    move-result-object v13

    invoke-static {v13}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v13

    move-object/from16 v21, v3

    const/4 v3, 0x5

    invoke-virtual {v1, v3}, Ljava/util/regex/Matcher;->group(I)Ljava/lang/String;

    move-result-object v3

    invoke-static {v3}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v3

    move/from16 v22, v6

    const/4 v6, 0x6

    invoke-virtual {v1, v6}, Ljava/util/regex/Matcher;->group(I)Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v1

    if-lez v3, :cond_3

    const/4 v3, 0x1

    iput-boolean v3, v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupAec:Z

    const/4 v6, 0x0

    goto :goto_3

    :cond_3
    const/4 v3, 0x1

    const/4 v6, 0x0

    iput-boolean v6, v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupAec:Z

    :goto_3
    if-eq v4, v3, :cond_4

    const-string v2, "format version not valid"

    invoke-static {v8, v2}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_7

    :cond_4
    move/from16 v18, v13

    move v13, v1

    move v1, v6

    move/from16 v6, v22

    goto :goto_4

    :cond_5
    move-object/from16 v21, v3

    move/from16 v22, v6

    move/from16 v13, v20

    const/4 v1, 0x0

    goto :goto_4

    :cond_6
    move-object/from16 v21, v3

    const/4 v1, 0x0

    move/from16 v13, v20

    :goto_4
    rem-int/lit8 v3, v9, 0x2

    if-nez v3, :cond_7

    move v7, v12

    goto :goto_5

    :cond_7
    shl-int/lit8 v3, v7, 0x10

    const/high16 v4, -0x10000

    and-int/2addr v3, v4

    const v4, 0xffff

    and-int/2addr v4, v12

    or-int/2addr v3, v4

    const/4 v4, 0x2

    add-int/lit8 v4, v2, -0x2

    if-ge v9, v4, :cond_8

    xor-int/2addr v3, v10

    move v10, v3

    goto :goto_5

    :cond_8
    move v11, v3

    :goto_5
    add-int/lit8 v9, v9, 0x1

    add-int/lit8 v5, v5, 0x1

    move-object/from16 v1, p1

    move/from16 v12, v18

    move/from16 v4, v19

    move-object/from16 v3, v21

    goto/16 :goto_0

    :cond_9
    move/from16 v20, v13

    :goto_6
    move/from16 v13, v18

    move/from16 v1, v20

    :goto_7
    if-eqz v11, :cond_a

    const-wide/16 v2, 0x0

    cmp-long v4, v14, v2

    if-lez v4, :cond_a

    cmp-long v2, v16, v2

    if-lez v2, :cond_a

    if-ltz v13, :cond_a

    if-lez v1, :cond_a

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "format valid "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2, v11}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v8, v2}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    const/4 v2, 0x1

    iput-boolean v2, v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mIsValideInfo:Z

    iput-wide v14, v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupStartTime:J

    iget-wide v2, v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupStartTime:J

    add-long v2, v2, v16

    iput-wide v2, v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupEndTime:J

    iput v13, v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupSocre:I

    iput v1, v0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mPcmLength:I

    goto :goto_8

    :cond_a
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "format check sum not valid verify "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v10}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v1, " checksum "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0, v11}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v8, v0}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    :goto_8
    return-void
.end method

.method public reset()Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;
    .locals 4

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mIsValideInfo:Z

    const-wide/16 v1, -0x1

    iput-wide v1, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupStartTime:J

    iput-wide v1, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupEndTime:J

    const/4 v3, -0x1

    iput v3, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupSocre:I

    iput-wide v1, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mWakeupRequiredLeadingSilence:J

    iput-boolean v0, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mMxeWakeupAec:Z

    iput v3, p0, Lcom/xiaomi/asr/engine/utils/DSPWakeupInfoParser;->mPcmLength:I

    return-object p0
.end method

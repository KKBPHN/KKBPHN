.class public Lcom/android/camera/log/FileLogger$Config;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private logDir:Ljava/lang/String;

.field private logName:Ljava/lang/String;

.field private maxBackUpCount:I

.field private maxFileSize:I


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x4

    iput v0, p0, Lcom/android/camera/log/FileLogger$Config;->maxBackUpCount:I

    const/high16 v0, 0x100000

    iput v0, p0, Lcom/android/camera/log/FileLogger$Config;->maxFileSize:I

    return-void
.end method

.method static synthetic access$000(Lcom/android/camera/log/FileLogger$Config;)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/log/FileLogger$Config;->logDir:Ljava/lang/String;

    return-object p0
.end method

.method static synthetic access$002(Lcom/android/camera/log/FileLogger$Config;Ljava/lang/String;)Ljava/lang/String;
    .locals 0

    iput-object p1, p0, Lcom/android/camera/log/FileLogger$Config;->logDir:Ljava/lang/String;

    return-object p1
.end method

.method static synthetic access$100(Lcom/android/camera/log/FileLogger$Config;)Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/log/FileLogger$Config;->logName:Ljava/lang/String;

    return-object p0
.end method

.method static synthetic access$102(Lcom/android/camera/log/FileLogger$Config;Ljava/lang/String;)Ljava/lang/String;
    .locals 0

    iput-object p1, p0, Lcom/android/camera/log/FileLogger$Config;->logName:Ljava/lang/String;

    return-object p1
.end method

.method static synthetic access$200(Lcom/android/camera/log/FileLogger$Config;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/log/FileLogger$Config;->maxBackUpCount:I

    return p0
.end method

.method static synthetic access$202(Lcom/android/camera/log/FileLogger$Config;I)I
    .locals 0

    iput p1, p0, Lcom/android/camera/log/FileLogger$Config;->maxBackUpCount:I

    return p1
.end method

.method static synthetic access$300(Lcom/android/camera/log/FileLogger$Config;)I
    .locals 0

    iget p0, p0, Lcom/android/camera/log/FileLogger$Config;->maxFileSize:I

    return p0
.end method

.method static synthetic access$302(Lcom/android/camera/log/FileLogger$Config;I)I
    .locals 0

    iput p1, p0, Lcom/android/camera/log/FileLogger$Config;->maxFileSize:I

    return p1
.end method

.method public static newBuild()Lcom/android/camera/log/FileLogger$Config$Build;
    .locals 1

    new-instance v0, Lcom/android/camera/log/FileLogger$Config$Build;

    invoke-direct {v0}, Lcom/android/camera/log/FileLogger$Config$Build;-><init>()V

    return-object v0
.end method


# virtual methods
.method public getLogDir()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/log/FileLogger$Config;->logDir:Ljava/lang/String;

    return-object p0
.end method

.method public getLogName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/log/FileLogger$Config;->logName:Ljava/lang/String;

    return-object p0
.end method

.method public getMaxBackUpCount()I
    .locals 0

    iget p0, p0, Lcom/android/camera/log/FileLogger$Config;->maxBackUpCount:I

    return p0
.end method

.method public getMaxFileSize()J
    .locals 2

    iget p0, p0, Lcom/android/camera/log/FileLogger$Config;->maxFileSize:I

    int-to-long v0, p0

    return-wide v0
.end method

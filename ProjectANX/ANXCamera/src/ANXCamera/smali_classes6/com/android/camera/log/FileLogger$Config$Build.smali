.class public Lcom/android/camera/log/FileLogger$Config$Build;
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

    iput v0, p0, Lcom/android/camera/log/FileLogger$Config$Build;->maxBackUpCount:I

    const/high16 v0, 0x100000

    iput v0, p0, Lcom/android/camera/log/FileLogger$Config$Build;->maxFileSize:I

    return-void
.end method


# virtual methods
.method public build()Lcom/android/camera/log/FileLogger$Config;
    .locals 2

    new-instance v0, Lcom/android/camera/log/FileLogger$Config;

    invoke-direct {v0}, Lcom/android/camera/log/FileLogger$Config;-><init>()V

    iget-object v1, p0, Lcom/android/camera/log/FileLogger$Config$Build;->logDir:Ljava/lang/String;

    invoke-static {v0, v1}, Lcom/android/camera/log/FileLogger$Config;->access$002(Lcom/android/camera/log/FileLogger$Config;Ljava/lang/String;)Ljava/lang/String;

    iget-object v1, p0, Lcom/android/camera/log/FileLogger$Config$Build;->logName:Ljava/lang/String;

    invoke-static {v0, v1}, Lcom/android/camera/log/FileLogger$Config;->access$102(Lcom/android/camera/log/FileLogger$Config;Ljava/lang/String;)Ljava/lang/String;

    iget v1, p0, Lcom/android/camera/log/FileLogger$Config$Build;->maxBackUpCount:I

    invoke-static {v0, v1}, Lcom/android/camera/log/FileLogger$Config;->access$202(Lcom/android/camera/log/FileLogger$Config;I)I

    iget p0, p0, Lcom/android/camera/log/FileLogger$Config$Build;->maxFileSize:I

    invoke-static {v0, p0}, Lcom/android/camera/log/FileLogger$Config;->access$302(Lcom/android/camera/log/FileLogger$Config;I)I

    return-object v0
.end method

.method public setLogDir(Ljava/lang/String;)Lcom/android/camera/log/FileLogger$Config$Build;
    .locals 0

    iput-object p1, p0, Lcom/android/camera/log/FileLogger$Config$Build;->logDir:Ljava/lang/String;

    return-object p0
.end method

.method public setLogName(Ljava/lang/String;)Lcom/android/camera/log/FileLogger$Config$Build;
    .locals 0

    iput-object p1, p0, Lcom/android/camera/log/FileLogger$Config$Build;->logName:Ljava/lang/String;

    return-object p0
.end method

.method public setMaxBackUpCount(I)Lcom/android/camera/log/FileLogger$Config$Build;
    .locals 0

    iput p1, p0, Lcom/android/camera/log/FileLogger$Config$Build;->maxBackUpCount:I

    return-object p0
.end method

.method public setMaxFileSize(I)Lcom/android/camera/log/FileLogger$Config$Build;
    .locals 0

    iput p1, p0, Lcom/android/camera/log/FileLogger$Config$Build;->maxFileSize:I

    return-object p0
.end method

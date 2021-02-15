.class public Lcom/android/camera/log/FileLogger;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final CAMERA_FILE_LOG_PATH:Ljava/lang/String;

.field public static final MIUI_284_LOG_DIR_PATH:Ljava/lang/String;

.field public static final MIUI_FEEDBACK_LOG_DIR_PATH:Ljava/lang/String;

.field private static final TAG:Ljava/lang/String; = "FileLogger"

.field private static mConfig:Lcom/android/camera/log/FileLogger$Config;

.field private static final mExecutor:Ljava/util/concurrent/ExecutorService;

.field private static mLogger:Lcom/miui/internal/log/Logger;

.field private static final sThreadFactory:Ljava/util/concurrent/ThreadFactory;


# direct methods
.method static constructor <clinit>()V
    .locals 10

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-static {}, Landroid/os/Environment;->getExternalStorageDirectory()Ljava/io/File;

    move-result-object v1

    invoke-virtual {v1}, Ljava/io/File;->getPath()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v1, "/MIUI/debug_log/common/"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {}, Lcom/android/camera/CameraAppImpl;->getAndroidContext()Landroid/content/Context;

    move-result-object v1

    invoke-virtual {v1}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcom/android/camera/log/FileLogger;->MIUI_284_LOG_DIR_PATH:Ljava/lang/String;

    invoke-static {}, Lcom/android/camera/CameraAppImpl;->getAndroidContext()Landroid/content/Context;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/miui/internal/log/util/Config;->getDefaultSdcardLogDir(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcom/android/camera/log/FileLogger;->MIUI_FEEDBACK_LOG_DIR_PATH:Ljava/lang/String;

    invoke-static {}, Lcom/android/camera/CameraAppImpl;->getAndroidContext()Landroid/content/Context;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Lcom/miui/internal/log/util/Config;->getDefaultCacheLogDir(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcom/android/camera/log/FileLogger;->CAMERA_FILE_LOG_PATH:Ljava/lang/String;

    sget-object v0, Lcom/android/camera/log/O0000o00;->INSTANCE:Lcom/android/camera/log/O0000o00;

    sput-object v0, Lcom/android/camera/log/FileLogger;->sThreadFactory:Ljava/util/concurrent/ThreadFactory;

    new-instance v0, Ljava/util/concurrent/ThreadPoolExecutor;

    sget-object v6, Ljava/util/concurrent/TimeUnit;->SECONDS:Ljava/util/concurrent/TimeUnit;

    new-instance v7, Ljava/util/concurrent/LinkedBlockingQueue;

    invoke-direct {v7}, Ljava/util/concurrent/LinkedBlockingQueue;-><init>()V

    sget-object v8, Lcom/android/camera/log/FileLogger;->sThreadFactory:Ljava/util/concurrent/ThreadFactory;

    sget-object v9, Lcom/android/camera/log/O00000o;->INSTANCE:Lcom/android/camera/log/O00000o;

    const/4 v2, 0x1

    const/4 v3, 0x1

    const-wide/16 v4, 0x0

    move-object v1, v0

    invoke-direct/range {v1 .. v9}, Ljava/util/concurrent/ThreadPoolExecutor;-><init>(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;)V

    sput-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static synthetic O000000o(Ljava/lang/Runnable;)Ljava/lang/Thread;
    .locals 2

    new-instance v0, Ljava/lang/Thread;

    const-string v1, "file-logger"

    invoke-direct {v0, p0, v1}, Ljava/lang/Thread;-><init>(Ljava/lang/Runnable;Ljava/lang/String;)V

    const/4 p0, 0x1

    invoke-virtual {v0, p0}, Ljava/lang/Thread;->setPriority(I)V

    return-object v0
.end method

.method static synthetic O000000o(Ljava/lang/Runnable;Ljava/util/concurrent/ThreadPoolExecutor;)V
    .locals 1

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "rejectedExecution "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    const-string p1, "FileLogger"

    invoke-static {p1, p0}, Landroid/util/Log;->w(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method static synthetic O000000o(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->DEBUG:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1, p2}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

.method static synthetic O00000Oo(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->ERROR:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1, p2}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

.method static synthetic O00000o(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->VERBOSE:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1, p2}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

.method static synthetic O00000o(Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 3

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->WARNING:Lcom/miui/internal/log/Level;

    const-string v2, ""

    invoke-virtual {v0, v1, p0, v2, p1}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

.method static synthetic O00000o0(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->INFO:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1, p2}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

.method static synthetic O00000oO(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->WARNING:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1, p2}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

.method static synthetic O00000oo(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->VERBOSE:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method static synthetic O0000O0o(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->DEBUG:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method static synthetic O0000OOo(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->ERROR:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method static synthetic O0000Oo(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->VERBOSE:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method static synthetic O0000Oo0(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->INFO:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method static synthetic O0000OoO(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    sget-object v1, Lcom/miui/internal/log/Level;->WARNING:Lcom/miui/internal/log/Level;

    invoke-virtual {v0, v1, p0, p1}, Lcom/miui/internal/log/Logger;->log(Lcom/miui/internal/log/Level;Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public static c(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    return-void

    :cond_1
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O0000Oo;

    invoke-direct {v1, p0, p1}, Lcom/android/camera/log/O0000Oo;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static d(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    return-void

    :cond_1
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O00000Oo;

    invoke-direct {v1, p0, p1}, Lcom/android/camera/log/O00000Oo;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static d(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    if-nez p2, :cond_1

    return-void

    :cond_1
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-virtual {p2}, Ljava/lang/Throwable;->getMessage()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    return-void

    :cond_2
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O0000OoO;

    invoke-direct {v1, p0, p1, p2}, Lcom/android/camera/log/O0000OoO;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static varargs d(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    .locals 0

    invoke-static {p1, p2}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p1

    invoke-static {p0, p1}, Lcom/android/camera/log/FileLogger;->d(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public static e(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    return-void

    :cond_1
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O00000oo;

    invoke-direct {v1, p0, p1}, Lcom/android/camera/log/O00000oo;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static e(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    if-nez p2, :cond_1

    return-void

    :cond_1
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-virtual {p2}, Ljava/lang/Throwable;->getMessage()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    return-void

    :cond_2
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O0000O0o;

    invoke-direct {v1, p0, p1, p2}, Lcom/android/camera/log/O0000O0o;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static varargs e(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    .locals 0

    invoke-static {p1, p2}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p1

    invoke-static {p0, p1}, Lcom/android/camera/log/FileLogger;->e(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public static i(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    return-void

    :cond_1
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O00000oO;

    invoke-direct {v1, p0, p1}, Lcom/android/camera/log/O00000oO;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static i(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    if-nez p2, :cond_1

    return-void

    :cond_1
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-virtual {p2}, Ljava/lang/Throwable;->getMessage()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    return-void

    :cond_2
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O0000Ooo;

    invoke-direct {v1, p0, p1, p2}, Lcom/android/camera/log/O0000Ooo;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static init(Landroid/content/Context;Lcom/android/camera/log/FileLogger$Config;)V
    .locals 4

    sput-object p1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    sget-object p1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-static {p1}, Lcom/android/camera/log/FileLogger$Config;->access$000(Lcom/android/camera/log/FileLogger$Config;)Ljava/lang/String;

    move-result-object p1

    if-nez p1, :cond_0

    sget-object p1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    sget-object v0, Lcom/android/camera/log/FileLogger;->CAMERA_FILE_LOG_PATH:Ljava/lang/String;

    invoke-static {p1, v0}, Lcom/android/camera/log/FileLogger$Config;->access$002(Lcom/android/camera/log/FileLogger$Config;Ljava/lang/String;)Ljava/lang/String;

    :cond_0
    sget-object p1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-static {p1}, Lcom/android/camera/log/FileLogger$Config;->access$100(Lcom/android/camera/log/FileLogger$Config;)Ljava/lang/String;

    move-result-object p1

    if-nez p1, :cond_1

    sget-object p1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-virtual {p0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object p0

    invoke-static {p1, p0}, Lcom/android/camera/log/FileLogger$Config;->access$102(Lcom/android/camera/log/FileLogger$Config;Ljava/lang/String;)Ljava/lang/String;

    :cond_1
    new-instance p0, Lcom/miui/internal/log/Logger;

    sget-object p1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-static {p1}, Lcom/android/camera/log/FileLogger$Config;->access$100(Lcom/android/camera/log/FileLogger$Config;)Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Lcom/miui/internal/log/Logger;-><init>(Ljava/lang/String;)V

    new-instance p1, Lcom/miui/internal/log/appender/FileAppender;

    invoke-direct {p1}, Lcom/miui/internal/log/appender/FileAppender;-><init>()V

    new-instance v0, Lcom/miui/internal/log/format/SimpleFormatter;

    invoke-direct {v0}, Lcom/miui/internal/log/format/SimpleFormatter;-><init>()V

    invoke-virtual {p1, v0}, Lcom/miui/internal/log/appender/FileAppender;->setFormatter(Lcom/miui/internal/log/format/Formatter;)V

    new-instance v0, Lcom/miui/internal/log/appender/rolling/FileRolloverStrategy;

    invoke-direct {v0}, Lcom/miui/internal/log/appender/rolling/FileRolloverStrategy;-><init>()V

    sget-object v1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-static {v1}, Lcom/android/camera/log/FileLogger$Config;->access$200(Lcom/android/camera/log/FileLogger$Config;)I

    move-result v1

    invoke-virtual {v0, v1}, Lcom/miui/internal/log/appender/rolling/FileRolloverStrategy;->setMaxBackupIndex(I)V

    sget-object v1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-static {v1}, Lcom/android/camera/log/FileLogger$Config;->access$300(Lcom/android/camera/log/FileLogger$Config;)I

    move-result v1

    invoke-virtual {v0, v1}, Lcom/miui/internal/log/appender/rolling/FileRolloverStrategy;->setMaxFileSize(I)V

    new-instance v1, Lcom/miui/internal/log/appender/rolling/RollingFileManager;

    sget-object v2, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-static {v2}, Lcom/android/camera/log/FileLogger$Config;->access$000(Lcom/android/camera/log/FileLogger$Config;)Ljava/lang/String;

    move-result-object v2

    sget-object v3, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-static {v3}, Lcom/android/camera/log/FileLogger$Config;->access$100(Lcom/android/camera/log/FileLogger$Config;)Ljava/lang/String;

    move-result-object v3

    invoke-direct {v1, v2, v3}, Lcom/miui/internal/log/appender/rolling/RollingFileManager;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-virtual {v1, v0}, Lcom/miui/internal/log/appender/rolling/RollingFileManager;->setRolloverStrategy(Lcom/miui/internal/log/appender/rolling/RolloverStrategy;)V

    invoke-virtual {p1, v1}, Lcom/miui/internal/log/appender/FileAppender;->setFileManager(Lcom/miui/internal/log/appender/FileManager;)V

    invoke-virtual {p0, p1}, Lcom/miui/internal/log/Logger;->addAppender(Lcom/miui/internal/log/appender/Appender;)V

    invoke-static {}, Lcom/android/camera/Util;->isDebugOsBuild()Z

    move-result p1

    if-eqz p1, :cond_2

    sget-object p1, Lcom/miui/internal/log/Level;->DEBUG:Lcom/miui/internal/log/Level;

    goto :goto_0

    :cond_2
    sget-object p1, Lcom/miui/internal/log/Level;->INFO:Lcom/miui/internal/log/Level;

    :goto_0
    invoke-virtual {p0, p1}, Lcom/miui/internal/log/Logger;->setLevel(Lcom/miui/internal/log/Level;)V

    invoke-static {}, Lcom/android/camera/log/FileLogger;->registerDumpLogReceiver()V

    sput-object p0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    return-void
.end method

.method private static registerDumpLogReceiver()V
    .locals 5

    const-string v0, "FileLogger"

    const-string v1, "registerDumpLogReceiver: "

    invoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    new-instance v0, Lcom/miui/internal/log/receiver/DumpReceiver;

    sget-object v1, Lcom/android/camera/log/FileLogger;->mConfig:Lcom/android/camera/log/FileLogger$Config;

    invoke-static {v1}, Lcom/android/camera/log/FileLogger$Config;->access$000(Lcom/android/camera/log/FileLogger$Config;)Ljava/lang/String;

    move-result-object v1

    sget-object v2, Lcom/android/camera/log/FileLogger;->MIUI_FEEDBACK_LOG_DIR_PATH:Ljava/lang/String;

    invoke-direct {v0, v1, v2}, Lcom/miui/internal/log/receiver/DumpReceiver;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-static {}, Lcom/android/camera/CameraAppImpl;->getAndroidContext()Landroid/content/Context;

    move-result-object v1

    new-instance v2, Landroid/content/IntentFilter;

    const-string v3, "com.miui.core.intent.ACTION_DUMP_CACHED_LOG"

    invoke-direct {v2, v3}, Landroid/content/IntentFilter;-><init>(Ljava/lang/String;)V

    const/4 v3, 0x0

    const-string v4, "miui.permission.DUMP_CACHED_LOG"

    invoke-virtual {v1, v0, v2, v4, v3}, Landroid/content/Context;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;)Landroid/content/Intent;

    new-instance v0, Lcom/android/camera/log/Dump284LogReceiver;

    invoke-direct {v0}, Lcom/android/camera/log/Dump284LogReceiver;-><init>()V

    new-instance v1, Landroid/content/IntentFilter;

    const-string v2, "android.provider.Telephony.SECRET_CODE"

    invoke-direct {v1, v2}, Landroid/content/IntentFilter;-><init>(Ljava/lang/String;)V

    const-string v2, "android_secret_code"

    invoke-virtual {v1, v2}, Landroid/content/IntentFilter;->addDataScheme(Ljava/lang/String;)V

    const-string v2, "284"

    invoke-virtual {v1, v2, v3}, Landroid/content/IntentFilter;->addDataAuthority(Ljava/lang/String;Ljava/lang/String;)V

    invoke-static {}, Lcom/android/camera/CameraAppImpl;->getAndroidContext()Landroid/content/Context;

    move-result-object v2

    invoke-virtual {v2, v0, v1}, Landroid/content/Context;->registerReceiver(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;

    return-void
.end method

.method public static v(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    return-void

    :cond_1
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O000000o;

    invoke-direct {v1, p0, p1}, Lcom/android/camera/log/O000000o;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static v(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    return-void

    :cond_1
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O0000OOo;

    invoke-direct {v1, p0, p1, p2}, Lcom/android/camera/log/O0000OOo;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static w(Ljava/lang/String;Ljava/lang/String;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    return-void

    :cond_1
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O0000Oo0;

    invoke-direct {v1, p0, p1}, Lcom/android/camera/log/O0000Oo0;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static w(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    if-nez p2, :cond_1

    return-void

    :cond_1
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-virtual {p2}, Ljava/lang/Throwable;->getMessage()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_2

    return-void

    :cond_2
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O0000o0;

    invoke-direct {v1, p0, p1, p2}, Lcom/android/camera/log/O0000o0;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

.method public static varargs w(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    .locals 0

    invoke-static {p1, p2}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object p1

    invoke-static {p0, p1}, Lcom/android/camera/log/FileLogger;->w(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

.method public static w(Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 2

    sget-object v0, Lcom/android/camera/log/FileLogger;->mLogger:Lcom/miui/internal/log/Logger;

    if-nez v0, :cond_0

    return-void

    :cond_0
    if-nez p1, :cond_1

    return-void

    :cond_1
    sget-object v0, Lcom/android/camera/log/FileLogger;->mExecutor:Ljava/util/concurrent/ExecutorService;

    new-instance v1, Lcom/android/camera/log/O00000o0;

    invoke-direct {v1, p0, p1}, Lcom/android/camera/log/O00000o0;-><init>(Ljava/lang/String;Ljava/lang/Throwable;)V

    invoke-interface {v0, v1}, Ljava/util/concurrent/ExecutorService;->execute(Ljava/lang/Runnable;)V

    return-void
.end method

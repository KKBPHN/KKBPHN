.class public final synthetic Lcom/android/camera/log/O00000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/concurrent/RejectedExecutionHandler;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/log/O00000o;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/log/O00000o;

    invoke-direct {v0}, Lcom/android/camera/log/O00000o;-><init>()V

    sput-object v0, Lcom/android/camera/log/O00000o;->INSTANCE:Lcom/android/camera/log/O00000o;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final rejectedExecution(Ljava/lang/Runnable;Ljava/util/concurrent/ThreadPoolExecutor;)V
    .locals 0

    invoke-static {p1, p2}, Lcom/android/camera/log/FileLogger;->O000000o(Ljava/lang/Runnable;Ljava/util/concurrent/ThreadPoolExecutor;)V

    return-void
.end method

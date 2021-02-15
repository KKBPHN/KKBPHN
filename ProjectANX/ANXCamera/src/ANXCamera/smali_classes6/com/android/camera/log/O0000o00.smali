.class public final synthetic Lcom/android/camera/log/O0000o00;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/concurrent/ThreadFactory;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/log/O0000o00;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/log/O0000o00;

    invoke-direct {v0}, Lcom/android/camera/log/O0000o00;-><init>()V

    sput-object v0, Lcom/android/camera/log/O0000o00;->INSTANCE:Lcom/android/camera/log/O0000o00;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final newThread(Ljava/lang/Runnable;)Ljava/lang/Thread;
    .locals 0

    invoke-static {p1}, Lcom/android/camera/log/FileLogger;->O000000o(Ljava/lang/Runnable;)Ljava/lang/Thread;

    move-result-object p0

    return-object p0
.end method

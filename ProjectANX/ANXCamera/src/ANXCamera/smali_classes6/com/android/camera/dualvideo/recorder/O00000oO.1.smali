.class public final synthetic Lcom/android/camera/dualvideo/recorder/O00000oO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/recorder/O00000oO;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/recorder/O00000oO;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/recorder/O00000oO;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/recorder/O00000oO;->INSTANCE:Lcom/android/camera/dualvideo/recorder/O00000oO;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 0

    invoke-static {p1}, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->O00000oo(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

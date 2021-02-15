.class public final synthetic Lcom/android/camera/dualvideo/util/O0000Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/util/O0000Oo;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/util/O0000Oo;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/util/O0000Oo;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/util/O0000Oo;->INSTANCE:Lcom/android/camera/dualvideo/util/O0000Oo;

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

    check-cast p1, Ljava/lang/Integer;

    invoke-static {p1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->O0000O0o(Ljava/lang/Integer;)Z

    move-result p0

    return p0
.end method

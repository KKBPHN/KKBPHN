.class public final synthetic Lcom/android/camera/dualvideo/util/O0000OoO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/Comparator;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/util/O0000OoO;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/util/O0000OoO;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/util/O0000OoO;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/util/O0000OoO;->INSTANCE:Lcom/android/camera/dualvideo/util/O0000OoO;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final compare(Ljava/lang/Object;Ljava/lang/Object;)I
    .locals 0

    check-cast p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    check-cast p2, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-static {p1, p2}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->O000000o(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)I

    move-result p0

    return p0
.end method

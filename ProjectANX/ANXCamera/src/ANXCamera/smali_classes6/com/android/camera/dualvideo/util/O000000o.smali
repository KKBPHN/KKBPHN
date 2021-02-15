.class public final synthetic Lcom/android/camera/dualvideo/util/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Function;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/util/O000000o;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/util/O000000o;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/util/O000000o;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/util/O000000o;->INSTANCE:Lcom/android/camera/dualvideo/util/O000000o;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final apply(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0

    check-cast p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-static {p1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->O0000Ooo(Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Ljava/lang/Float;

    move-result-object p0

    return-object p0
.end method

.class public final synthetic Lcom/android/camera/dualvideo/render/O000OO00;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/render/O000OO00;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/render/O000OO00;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/render/O000OO00;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/render/O000OO00;->INSTANCE:Lcom/android/camera/dualvideo/render/O000OO00;

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

    check-cast p1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/CameraItemManager;->O0000o0O(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z

    move-result p0

    return p0
.end method

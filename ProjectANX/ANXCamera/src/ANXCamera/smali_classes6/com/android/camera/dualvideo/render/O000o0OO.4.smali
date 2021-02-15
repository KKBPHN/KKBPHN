.class public final synthetic Lcom/android/camera/dualvideo/render/O000o0OO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Function;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/render/O000o0OO;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/render/O000o0OO;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/render/O000o0OO;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/render/O000o0OO;->INSTANCE:Lcom/android/camera/dualvideo/render/O000o0OO;

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

    check-cast p1, Lcom/android/camera/dualvideo/render/CameraItemInterface;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O0000oO(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

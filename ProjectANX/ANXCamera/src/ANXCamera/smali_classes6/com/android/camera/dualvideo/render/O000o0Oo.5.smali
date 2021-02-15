.class public final synthetic Lcom/android/camera/dualvideo/render/O000o0Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/render/O000o0Oo;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/render/O000o0Oo;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/render/O000o0Oo;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/render/O000o0Oo;->INSTANCE:Lcom/android/camera/dualvideo/render/O000o0Oo;

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

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O0000ooO(Lcom/android/camera/dualvideo/render/CameraItemInterface;)Z

    move-result p0

    return p0
.end method

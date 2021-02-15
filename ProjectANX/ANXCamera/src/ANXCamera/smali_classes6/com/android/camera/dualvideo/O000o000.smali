.class public final synthetic Lcom/android/camera/dualvideo/O000o000;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Function;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/O000o000;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/O000o000;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/O000o000;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/O000o000;->INSTANCE:Lcom/android/camera/dualvideo/O000o000;

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

    check-cast p1, Lcom/android/camera/dualvideo/render/RenderManager;

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/render/RenderManager;->isRecording()Z

    move-result p0

    invoke-static {p0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object p0

    return-object p0
.end method

.class public final synthetic Lcom/android/camera/dualvideo/render/O000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/render/O000o;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/render/O000o;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/render/O000o;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/render/O000o;->INSTANCE:Lcom/android/camera/dualvideo/render/O000o;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O000000o(Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/dualvideo/O000Oo00;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/O000Oo00;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/O000Oo00;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/O000Oo00;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/O000Oo00;->INSTANCE:Lcom/android/camera/dualvideo/O000Oo00;

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

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$TopAlert;

    invoke-static {p1}, Lcom/android/camera/dualvideo/DualVideoRecordModule;->O000000o(Lcom/android/camera/protocol/ModeProtocol$TopAlert;)V

    return-void
.end method

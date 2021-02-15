.class public final synthetic Lcom/android/camera/dualvideo/render/O000o000;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/render/O000o000;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/render/O000o000;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/render/O000o000;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/render/O000o000;->INSTANCE:Lcom/android/camera/dualvideo/render/O000o000;

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

    check-cast p1, Lcom/android/camera/dualvideo/util/UserSelectData;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O0000O0o(Lcom/android/camera/dualvideo/util/UserSelectData;)V

    return-void
.end method

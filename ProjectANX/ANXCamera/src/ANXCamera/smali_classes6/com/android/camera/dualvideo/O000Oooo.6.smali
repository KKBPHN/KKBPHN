.class public final synthetic Lcom/android/camera/dualvideo/O000Oooo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/O000Oooo;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/O000Oooo;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/O000Oooo;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/O000Oooo;->INSTANCE:Lcom/android/camera/dualvideo/O000Oooo;

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

    check-cast p1, Lcom/android/camera/dualvideo/render/RenderManager;

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/render/RenderManager;->reStartRenderWindow()V

    return-void
.end method

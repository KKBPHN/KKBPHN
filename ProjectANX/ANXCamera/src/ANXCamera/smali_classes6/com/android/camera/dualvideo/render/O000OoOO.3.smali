.class public final synthetic Lcom/android/camera/dualvideo/render/O000OoOO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/render/O000OoOO;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/render/O000OoOO;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/render/O000OoOO;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/render/O000OoOO;->INSTANCE:Lcom/android/camera/dualvideo/render/O000OoOO;

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

    check-cast p1, Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O00000Oo(Lcom/android/camera/dualvideo/render/RenderSource;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/dualvideo/render/O000oOoo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Function;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/dualvideo/render/O000oOoo;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/render/O000oOoo;

    invoke-direct {v0}, Lcom/android/camera/dualvideo/render/O000oOoo;-><init>()V

    sput-object v0, Lcom/android/camera/dualvideo/render/O000oOoo;->INSTANCE:Lcom/android/camera/dualvideo/render/O000oOoo;

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

    check-cast p1, Lcom/android/camera/dualvideo/util/UserSelectData;

    invoke-static {p1}, Lcom/android/camera/dualvideo/render/RenderUtil;->O0000Oo0(Lcom/android/camera/dualvideo/util/UserSelectData;)Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p0

    return-object p0
.end method

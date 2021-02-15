.class public final synthetic Lcom/android/camera/module/loader/camera2/O00000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/IntFunction;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/module/loader/camera2/O00000o;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/module/loader/camera2/O00000o;

    invoke-direct {v0}, Lcom/android/camera/module/loader/camera2/O00000o;-><init>()V

    sput-object v0, Lcom/android/camera/module/loader/camera2/O00000o;->INSTANCE:Lcom/android/camera/module/loader/camera2/O00000o;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final apply(I)Ljava/lang/Object;
    .locals 0

    invoke-static {p1}, Lcom/android/camera/module/loader/camera2/Camera2OpenManager;->O0000oOo(I)[Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

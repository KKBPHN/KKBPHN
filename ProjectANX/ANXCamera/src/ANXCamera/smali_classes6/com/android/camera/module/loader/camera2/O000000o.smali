.class public final synthetic Lcom/android/camera/module/loader/camera2/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/functions/Function;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/module/loader/camera2/O000000o;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/module/loader/camera2/O000000o;

    invoke-direct {v0}, Lcom/android/camera/module/loader/camera2/O000000o;-><init>()V

    sput-object v0, Lcom/android/camera/module/loader/camera2/O000000o;->INSTANCE:Lcom/android/camera/module/loader/camera2/O000000o;

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

    check-cast p1, Ljava/lang/Throwable;

    invoke-static {p1}, Lcom/android/camera/module/loader/camera2/Camera2OpenManager;->O0000Ooo(Ljava/lang/Throwable;)Lio/reactivex/ObservableSource;

    move-result-object p0

    return-object p0
.end method

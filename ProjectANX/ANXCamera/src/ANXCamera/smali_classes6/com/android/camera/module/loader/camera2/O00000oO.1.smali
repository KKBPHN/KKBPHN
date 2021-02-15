.class public final synthetic Lcom/android/camera/module/loader/camera2/O00000oO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/functions/Consumer;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/module/loader/camera2/O00000oO;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/module/loader/camera2/O00000oO;

    invoke-direct {v0}, Lcom/android/camera/module/loader/camera2/O00000oO;-><init>()V

    sput-object v0, Lcom/android/camera/module/loader/camera2/O00000oO;->INSTANCE:Lcom/android/camera/module/loader/camera2/O00000oO;

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

    check-cast p1, Lcom/android/camera/data/cloud/DataCloudItemFeature;

    invoke-static {p1}, Lcom/android/camera/module/loader/camera2/CompletablePreFixCamera2Setup;->O000000o(Lcom/android/camera/data/cloud/DataCloudItemFeature;)V

    return-void
.end method

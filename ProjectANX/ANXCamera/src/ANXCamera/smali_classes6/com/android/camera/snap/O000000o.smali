.class public final synthetic Lcom/android/camera/snap/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lcom/android/camera/ThermalDetector$OnThermalNotificationListener;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/snap/O000000o;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/snap/O000000o;

    invoke-direct {v0}, Lcom/android/camera/snap/O000000o;-><init>()V

    sput-object v0, Lcom/android/camera/snap/O000000o;->INSTANCE:Lcom/android/camera/snap/O000000o;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final onThermalNotification(I)V
    .locals 0

    invoke-static {p1}, Lcom/android/camera/snap/SnapService;->O00000Oo(I)V

    return-void
.end method

.class public final synthetic LO00000Oo/O00000oO/O000000o/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Function;


# static fields
.field public static final synthetic INSTANCE:LO00000Oo/O00000oO/O000000o/O000000o;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, LO00000Oo/O00000oO/O000000o/O000000o;

    invoke-direct {v0}, LO00000Oo/O00000oO/O000000o/O000000o;-><init>()V

    sput-object v0, LO00000Oo/O00000oO/O000000o/O000000o;->INSTANCE:LO00000Oo/O00000oO/O000000o/O000000o;

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

    check-cast p1, Lcom/android/camera2/CameraCapabilities;

    invoke-static {p1}, LO00000Oo/O00000oO/O000000o/O00000o;->O00000Oo(Lcom/android/camera2/CameraCapabilities;)Ljava/lang/Boolean;

    move-result-object p0

    return-object p0
.end method

.class public final synthetic Lcom/android/camera/fragment/O0000O0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Landroid/view/View$OnTouchListener;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/fragment/O0000O0o;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/fragment/O0000O0o;

    invoke-direct {v0}, Lcom/android/camera/fragment/O0000O0o;-><init>()V

    sput-object v0, Lcom/android/camera/fragment/O0000O0o;->INSTANCE:Lcom/android/camera/fragment/O0000O0o;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final onTouch(Landroid/view/View;Landroid/view/MotionEvent;)Z
    .locals 0

    invoke-static {p1, p2}, Lcom/android/camera/fragment/FragmentBeauty;->O00000o0(Landroid/view/View;Landroid/view/MotionEvent;)Z

    move-result p0

    return p0
.end method

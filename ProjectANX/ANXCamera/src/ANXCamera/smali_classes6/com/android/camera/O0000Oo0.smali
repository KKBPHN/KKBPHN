.class public final synthetic Lcom/android/camera/O0000Oo0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Landroid/content/DialogInterface$OnKeyListener;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/O0000Oo0;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/O0000Oo0;

    invoke-direct {v0}, Lcom/android/camera/O0000Oo0;-><init>()V

    sput-object v0, Lcom/android/camera/O0000Oo0;->INSTANCE:Lcom/android/camera/O0000Oo0;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final onKey(Landroid/content/DialogInterface;ILandroid/view/KeyEvent;)Z
    .locals 0

    invoke-static {p1, p2, p3}, Lcom/android/camera/RotateDialogController;->O00000o0(Landroid/content/DialogInterface;ILandroid/view/KeyEvent;)Z

    move-result p0

    return p0
.end method

.class public final synthetic Lcom/android/camera/tts/O00000oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Landroid/media/AudioManager$OnAudioFocusChangeListener;


# static fields
.field public static final synthetic INSTANCE:Lcom/android/camera/tts/O00000oo;


# direct methods
.method static synthetic constructor <clinit>()V
    .locals 1

    new-instance v0, Lcom/android/camera/tts/O00000oo;

    invoke-direct {v0}, Lcom/android/camera/tts/O00000oo;-><init>()V

    sput-object v0, Lcom/android/camera/tts/O00000oo;->INSTANCE:Lcom/android/camera/tts/O00000oo;

    return-void
.end method

.method private synthetic constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final onAudioFocusChange(I)V
    .locals 0

    invoke-static {p1}, Lcom/android/camera/tts/TTSHelper;->O00000Oo(I)V

    return-void
.end method

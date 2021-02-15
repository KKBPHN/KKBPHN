.class public final synthetic Lcom/android/camera/tts/O0000O0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/tts/TTSHelper$Listener;

.field private final synthetic O0OOoOO:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/tts/TTSHelper$Listener;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/tts/O0000O0o;->O0OOoO0:Lcom/android/camera/tts/TTSHelper$Listener;

    iput-boolean p2, p0, Lcom/android/camera/tts/O0000O0o;->O0OOoOO:Z

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/tts/O0000O0o;->O0OOoO0:Lcom/android/camera/tts/TTSHelper$Listener;

    iget-boolean p0, p0, Lcom/android/camera/tts/O0000O0o;->O0OOoOO:Z

    invoke-static {v0, p0}, Lcom/android/camera/tts/TTSHelper;->O000000o(Lcom/android/camera/tts/TTSHelper$Listener;Z)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/tts/O00000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/tts/TTSHelper$2;

.field private final synthetic O0OOoOO:Ljava/lang/String;

.field private final synthetic O0OOoOo:Ljava/util/function/BiConsumer;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/tts/TTSHelper$2;Ljava/lang/String;Ljava/util/function/BiConsumer;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/tts/O00000o;->O0OOoO0:Lcom/android/camera/tts/TTSHelper$2;

    iput-object p2, p0, Lcom/android/camera/tts/O00000o;->O0OOoOO:Ljava/lang/String;

    iput-object p3, p0, Lcom/android/camera/tts/O00000o;->O0OOoOo:Ljava/util/function/BiConsumer;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/tts/O00000o;->O0OOoO0:Lcom/android/camera/tts/TTSHelper$2;

    iget-object v1, p0, Lcom/android/camera/tts/O00000o;->O0OOoOO:Ljava/lang/String;

    iget-object p0, p0, Lcom/android/camera/tts/O00000o;->O0OOoOo:Ljava/util/function/BiConsumer;

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/tts/TTSHelper$2;->O000000o(Ljava/lang/String;Ljava/util/function/BiConsumer;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/module/O000o0OO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$RecordState;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/protocol/ModeProtocol$RecordState;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000o0OO;->O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$RecordState;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/module/O000o0OO;->O0OOoO0:Lcom/android/camera/protocol/ModeProtocol$RecordState;

    invoke-interface {p0}, Lcom/android/camera/protocol/ModeProtocol$RecordState;->onFinish()V

    return-void
.end method

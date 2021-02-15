.class public final synthetic Lcom/android/camera/dualvideo/O0000oOo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Z


# direct methods
.method public synthetic constructor <init>(Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-boolean p1, p0, Lcom/android/camera/dualvideo/O0000oOo;->O0OOoO0:Z

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/dualvideo/O0000oOo;->O0OOoO0:Z

    check-cast p1, Lcom/android/camera/protocol/ModeProtocol$ActionProcessing;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->O00000Oo(ZLcom/android/camera/protocol/ModeProtocol$ActionProcessing;)V

    return-void
.end method

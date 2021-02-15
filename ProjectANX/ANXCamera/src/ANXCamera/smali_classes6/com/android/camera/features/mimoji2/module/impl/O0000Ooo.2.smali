.class public final synthetic Lcom/android/camera/features/mimoji2/module/impl/O0000Ooo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/features/mimoji2/module/protocol/MimojiModeProtocol$MimojiBottomList;

.field private final synthetic O0OOoOO:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/features/mimoji2/module/protocol/MimojiModeProtocol$MimojiBottomList;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000Ooo;->O0OOoO0:Lcom/android/camera/features/mimoji2/module/protocol/MimojiModeProtocol$MimojiBottomList;

    iput-boolean p2, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000Ooo;->O0OOoOO:Z

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000Ooo;->O0OOoO0:Lcom/android/camera/features/mimoji2/module/protocol/MimojiModeProtocol$MimojiBottomList;

    iget-boolean p0, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000Ooo;->O0OOoOO:Z

    invoke-static {v0, p0}, Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;->O000000o(Lcom/android/camera/features/mimoji2/module/protocol/MimojiModeProtocol$MimojiBottomList;Z)V

    return-void
.end method

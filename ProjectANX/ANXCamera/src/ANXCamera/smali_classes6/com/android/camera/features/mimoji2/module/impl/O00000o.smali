.class public final synthetic Lcom/android/camera/features/mimoji2/module/impl/O00000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;

.field private final synthetic O0OOoOO:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/features/mimoji2/module/impl/O00000o;->O0OOoO0:Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;

    iput p2, p0, Lcom/android/camera/features/mimoji2/module/impl/O00000o;->O0OOoOO:I

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/features/mimoji2/module/impl/O00000o;->O0OOoO0:Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;

    iget p0, p0, Lcom/android/camera/features/mimoji2/module/impl/O00000o;->O0OOoOO:I

    invoke-virtual {v0, p0}, Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;->O0000o(I)V

    return-void
.end method

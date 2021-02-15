.class public final synthetic Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;

.field private final synthetic O0OOoOO:Ljava/nio/ByteBuffer;

.field private final synthetic O0OOoOo:I

.field private final synthetic O0OOoo0:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;Ljava/nio/ByteBuffer;II)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;->O0OOoO0:Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;

    iput-object p2, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;->O0OOoOO:Ljava/nio/ByteBuffer;

    iput p3, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;->O0OOoOo:I

    iput p4, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;->O0OOoo0:I

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;->O0OOoO0:Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;

    iget-object v1, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;->O0OOoOO:Ljava/nio/ByteBuffer;

    iget v2, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;->O0OOoOo:I

    iget p0, p0, Lcom/android/camera/features/mimoji2/module/impl/O0000O0o;->O0OOoo0:I

    invoke-virtual {v0, v1, v2, p0}, Lcom/android/camera/features/mimoji2/module/impl/MimojiAvatarEngine2Impl;->O000000o(Ljava/nio/ByteBuffer;II)V

    return-void
.end method

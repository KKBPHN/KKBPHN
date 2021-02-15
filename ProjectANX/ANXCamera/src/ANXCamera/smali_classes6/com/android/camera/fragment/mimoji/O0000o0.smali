.class public final synthetic Lcom/android/camera/fragment/mimoji/O0000o0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/fragment/mimoji/MimojiThumbnailRecyclerAdapter$ThumbnailViewViewHolder;

.field private final synthetic O0OOoOO:Ljava/lang/String;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/fragment/mimoji/MimojiThumbnailRecyclerAdapter$ThumbnailViewViewHolder;Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/mimoji/O0000o0;->O0OOoO0:Lcom/android/camera/fragment/mimoji/MimojiThumbnailRecyclerAdapter$ThumbnailViewViewHolder;

    iput-object p2, p0, Lcom/android/camera/fragment/mimoji/O0000o0;->O0OOoOO:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/fragment/mimoji/O0000o0;->O0OOoO0:Lcom/android/camera/fragment/mimoji/MimojiThumbnailRecyclerAdapter$ThumbnailViewViewHolder;

    iget-object p0, p0, Lcom/android/camera/fragment/mimoji/O0000o0;->O0OOoOO:Ljava/lang/String;

    invoke-virtual {v0, p0}, Lcom/android/camera/fragment/mimoji/MimojiThumbnailRecyclerAdapter$ThumbnailViewViewHolder;->O0000oO0(Ljava/lang/String;)V

    return-void
.end method

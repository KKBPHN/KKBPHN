.class public final synthetic Lcom/android/camera/features/mimoji2/fragment/O00000oO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/FragmentMimojiFullScreen;

.field private final synthetic O0OOoOO:I

.field private final synthetic O0OOoOo:Ljava/lang/String;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/features/mimoji2/fragment/FragmentMimojiFullScreen;ILjava/lang/String;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/features/mimoji2/fragment/O00000oO;->O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/FragmentMimojiFullScreen;

    iput p2, p0, Lcom/android/camera/features/mimoji2/fragment/O00000oO;->O0OOoOO:I

    iput-object p3, p0, Lcom/android/camera/features/mimoji2/fragment/O00000oO;->O0OOoOo:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/features/mimoji2/fragment/O00000oO;->O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/FragmentMimojiFullScreen;

    iget v1, p0, Lcom/android/camera/features/mimoji2/fragment/O00000oO;->O0OOoOO:I

    iget-object p0, p0, Lcom/android/camera/features/mimoji2/fragment/O00000oO;->O0OOoOo:Ljava/lang/String;

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/features/mimoji2/fragment/FragmentMimojiFullScreen;->O000000o(ILjava/lang/String;)V

    return-void
.end method

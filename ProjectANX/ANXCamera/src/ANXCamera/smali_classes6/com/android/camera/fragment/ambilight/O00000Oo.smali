.class public final synthetic Lcom/android/camera/fragment/ambilight/O00000Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/fragment/ambilight/FragmentAmbilight;

.field private final synthetic O0OOoOO:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/fragment/ambilight/FragmentAmbilight;I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/ambilight/O00000Oo;->O0OOoO0:Lcom/android/camera/fragment/ambilight/FragmentAmbilight;

    iput p2, p0, Lcom/android/camera/fragment/ambilight/O00000Oo;->O0OOoOO:I

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/fragment/ambilight/O00000Oo;->O0OOoO0:Lcom/android/camera/fragment/ambilight/FragmentAmbilight;

    iget p0, p0, Lcom/android/camera/fragment/ambilight/O00000Oo;->O0OOoOO:I

    invoke-virtual {v0, p0}, Lcom/android/camera/fragment/ambilight/FragmentAmbilight;->O0000Oo(I)V

    return-void
.end method

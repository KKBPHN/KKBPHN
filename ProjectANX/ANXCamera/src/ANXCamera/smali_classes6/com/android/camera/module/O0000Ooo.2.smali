.class public final synthetic Lcom/android/camera/module/O0000Ooo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lcom/android/camera/fragment/GoogleLensFragment$OnClickListener;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/Camera2Module;

.field private final synthetic O0OOoOO:F

.field private final synthetic O0OOoOo:F

.field private final synthetic O0OOoo0:I

.field private final synthetic O0OOooO:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/Camera2Module;FFII)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O0000Ooo;->O0OOoO0:Lcom/android/camera/module/Camera2Module;

    iput p2, p0, Lcom/android/camera/module/O0000Ooo;->O0OOoOO:F

    iput p3, p0, Lcom/android/camera/module/O0000Ooo;->O0OOoOo:F

    iput p4, p0, Lcom/android/camera/module/O0000Ooo;->O0OOoo0:I

    iput p5, p0, Lcom/android/camera/module/O0000Ooo;->O0OOooO:I

    return-void
.end method


# virtual methods
.method public final onOptionClick(I)V
    .locals 6

    iget-object v0, p0, Lcom/android/camera/module/O0000Ooo;->O0OOoO0:Lcom/android/camera/module/Camera2Module;

    iget v1, p0, Lcom/android/camera/module/O0000Ooo;->O0OOoOO:F

    iget v2, p0, Lcom/android/camera/module/O0000Ooo;->O0OOoOo:F

    iget v3, p0, Lcom/android/camera/module/O0000Ooo;->O0OOoo0:I

    iget v4, p0, Lcom/android/camera/module/O0000Ooo;->O0OOooO:I

    move v5, p1

    invoke-virtual/range {v0 .. v5}, Lcom/android/camera/module/Camera2Module;->O000000o(FFIII)V

    return-void
.end method

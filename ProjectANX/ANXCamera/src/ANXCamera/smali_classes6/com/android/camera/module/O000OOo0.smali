.class public final synthetic Lcom/android/camera/module/O000OOo0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

.field private final synthetic O0OOoOO:I

.field private final synthetic O0OOoOo:F

.field private final synthetic O0OOoo0:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/DollyZoomModule;IFI)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000OOo0;->O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

    iput p2, p0, Lcom/android/camera/module/O000OOo0;->O0OOoOO:I

    iput p3, p0, Lcom/android/camera/module/O000OOo0;->O0OOoOo:F

    iput p4, p0, Lcom/android/camera/module/O000OOo0;->O0OOoo0:I

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/module/O000OOo0;->O0OOoO0:Lcom/android/camera/module/DollyZoomModule;

    iget v1, p0, Lcom/android/camera/module/O000OOo0;->O0OOoOO:I

    iget v2, p0, Lcom/android/camera/module/O000OOo0;->O0OOoOo:F

    iget p0, p0, Lcom/android/camera/module/O000OOo0;->O0OOoo0:I

    invoke-virtual {v0, v1, v2, p0}, Lcom/android/camera/module/DollyZoomModule;->O000000o(IFI)V

    return-void
.end method

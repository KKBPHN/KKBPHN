.class public final synthetic Lcom/android/camera/dualvideo/render/O000OoO0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:I

.field private final synthetic O0OOoOO:Landroid/util/Size;


# direct methods
.method public synthetic constructor <init>(ILandroid/util/Size;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/android/camera/dualvideo/render/O000OoO0;->O0OOoO0:I

    iput-object p2, p0, Lcom/android/camera/dualvideo/render/O000OoO0;->O0OOoOO:Landroid/util/Size;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 1

    iget v0, p0, Lcom/android/camera/dualvideo/render/O000OoO0;->O0OOoO0:I

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/O000OoO0;->O0OOoOO:Landroid/util/Size;

    check-cast p1, Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-static {v0, p0, p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O000000o(ILandroid/util/Size;Lcom/android/camera/dualvideo/render/RenderSource;)V

    return-void
.end method

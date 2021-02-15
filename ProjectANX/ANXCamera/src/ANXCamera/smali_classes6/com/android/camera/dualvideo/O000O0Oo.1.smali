.class public final synthetic Lcom/android/camera/dualvideo/O000O0Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

.field private final synthetic O0OOoOO:Lcom/android/camera/dualvideo/render/LayoutType;

.field private final synthetic O0OOoOo:Landroid/graphics/Point;

.field private final synthetic O0OOoo0:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/DualVideoModuleBase;Lcom/android/camera/dualvideo/render/LayoutType;Landroid/graphics/Point;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O000O0Oo;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

    iput-object p2, p0, Lcom/android/camera/dualvideo/O000O0Oo;->O0OOoOO:Lcom/android/camera/dualvideo/render/LayoutType;

    iput-object p3, p0, Lcom/android/camera/dualvideo/O000O0Oo;->O0OOoOo:Landroid/graphics/Point;

    iput-boolean p4, p0, Lcom/android/camera/dualvideo/O000O0Oo;->O0OOoo0:Z

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/dualvideo/O000O0Oo;->O0OOoO0:Lcom/android/camera/dualvideo/DualVideoModuleBase;

    iget-object v1, p0, Lcom/android/camera/dualvideo/O000O0Oo;->O0OOoOO:Lcom/android/camera/dualvideo/render/LayoutType;

    iget-object v2, p0, Lcom/android/camera/dualvideo/O000O0Oo;->O0OOoOo:Landroid/graphics/Point;

    iget-boolean p0, p0, Lcom/android/camera/dualvideo/O000O0Oo;->O0OOoo0:Z

    check-cast p1, Lcom/android/camera/module/loader/camera2/FocusManager2;

    invoke-virtual {v0, v1, v2, p0, p1}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->O000000o(Lcom/android/camera/dualvideo/render/LayoutType;Landroid/graphics/Point;ZLcom/android/camera/module/loader/camera2/FocusManager2;)V

    return-void
.end method

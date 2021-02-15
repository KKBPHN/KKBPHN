.class public final synthetic Lcom/android/camera/dualvideo/render/O000oOo0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Function;


# instance fields
.field private final synthetic O0OOoO0:F


# direct methods
.method public synthetic constructor <init>(F)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/android/camera/dualvideo/render/O000oOo0;->O0OOoO0:F

    return-void
.end method


# virtual methods
.method public final apply(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/render/O000oOo0;->O0OOoO0:F

    check-cast p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/render/RenderUtil;->O000000o(FLcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Ljava/lang/Float;

    move-result-object p0

    return-object p0
.end method

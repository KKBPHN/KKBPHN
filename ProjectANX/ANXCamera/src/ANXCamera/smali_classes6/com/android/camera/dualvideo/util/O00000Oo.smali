.class public final synthetic Lcom/android/camera/dualvideo/util/O00000Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:I


# direct methods
.method public synthetic constructor <init>(I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/android/camera/dualvideo/util/O00000Oo;->O0OOoO0:I

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/util/O00000Oo;->O0OOoO0:I

    check-cast p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/util/DualVideoConfigManager;->O000000o(ILcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z

    move-result p0

    return p0
.end method

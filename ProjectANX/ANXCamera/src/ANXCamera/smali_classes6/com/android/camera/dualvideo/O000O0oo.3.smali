.class public final synthetic Lcom/android/camera/dualvideo/O000O0oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/util/UserSelectData;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/util/UserSelectData;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/O000O0oo;->O0OOoO0:Lcom/android/camera/dualvideo/util/UserSelectData;

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/O000O0oo;->O0OOoO0:Lcom/android/camera/dualvideo/util/UserSelectData;

    check-cast p1, Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/DualVideoModuleBase;->O00000o0(Lcom/android/camera/dualvideo/util/UserSelectData;Lcom/android/camera/dualvideo/util/DualVideoConfigManager$ConfigItem;)Z

    move-result p0

    return p0
.end method

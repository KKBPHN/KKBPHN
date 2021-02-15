.class public final synthetic Lcom/android/camera/module/impl/component/O0000ooO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/impl/component/ConfigChangeImpl;

.field private final synthetic O0OOoOO:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/impl/component/ConfigChangeImpl;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/impl/component/O0000ooO;->O0OOoO0:Lcom/android/camera/module/impl/component/ConfigChangeImpl;

    iput-boolean p2, p0, Lcom/android/camera/module/impl/component/O0000ooO;->O0OOoOO:Z

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/module/impl/component/O0000ooO;->O0OOoO0:Lcom/android/camera/module/impl/component/ConfigChangeImpl;

    iget-boolean p0, p0, Lcom/android/camera/module/impl/component/O0000ooO;->O0OOoOO:Z

    check-cast p1, Lcom/android/camera/module/BaseModule;

    invoke-virtual {v0, p0, p1}, Lcom/android/camera/module/impl/component/ConfigChangeImpl;->O000000o(ZLcom/android/camera/module/BaseModule;)V

    return-void
.end method

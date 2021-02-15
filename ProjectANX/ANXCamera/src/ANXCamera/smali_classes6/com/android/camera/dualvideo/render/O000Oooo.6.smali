.class public final synthetic Lcom/android/camera/dualvideo/render/O000Oooo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/dualvideo/render/RenderManager;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/O000Oooo;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/O000Oooo;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    check-cast p1, Lcom/android/camera/dualvideo/util/UserSelectData;

    invoke-virtual {p0, p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O0000OOo(Lcom/android/camera/dualvideo/util/UserSelectData;)V

    return-void
.end method

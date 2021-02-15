.class public final synthetic Lcom/android/camera/dualvideo/render/O000o0O;
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

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/O000o0O;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/O000o0O;->O0OOoO0:Lcom/android/camera/dualvideo/render/RenderManager;

    check-cast p1, Lcom/android/camera/dualvideo/render/RenderSource;

    invoke-virtual {p0, p1}, Lcom/android/camera/dualvideo/render/RenderManager;->O00000o0(Lcom/android/camera/dualvideo/render/RenderSource;)V

    return-void
.end method

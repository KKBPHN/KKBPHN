.class public final synthetic Lcom/android/camera/data/observeable/O00000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/functions/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/data/observeable/VMResource;

.field private final synthetic O0OOoOO:Lcom/android/camera/resource/BaseResourceItem;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/data/observeable/VMResource;Lcom/android/camera/resource/BaseResourceItem;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/data/observeable/O00000o;->O0OOoO0:Lcom/android/camera/data/observeable/VMResource;

    iput-object p2, p0, Lcom/android/camera/data/observeable/O00000o;->O0OOoOO:Lcom/android/camera/resource/BaseResourceItem;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/data/observeable/O00000o;->O0OOoO0:Lcom/android/camera/data/observeable/VMResource;

    iget-object p0, p0, Lcom/android/camera/data/observeable/O00000o;->O0OOoOO:Lcom/android/camera/resource/BaseResourceItem;

    check-cast p1, Ljava/lang/Throwable;

    invoke-virtual {v0, p0, p1}, Lcom/android/camera/data/observeable/VMResource;->O000000o(Lcom/android/camera/resource/BaseResourceItem;Ljava/lang/Throwable;)V

    return-void
.end method

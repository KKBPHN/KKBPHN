.class public final synthetic Lcom/android/camera/module/O000o00;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/FlowableOnSubscribe;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/MiLiveModule;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/MiLiveModule;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000o00;->O0OOoO0:Lcom/android/camera/module/MiLiveModule;

    return-void
.end method


# virtual methods
.method public final subscribe(Lio/reactivex/FlowableEmitter;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/module/O000o00;->O0OOoO0:Lcom/android/camera/module/MiLiveModule;

    invoke-virtual {p0, p1}, Lcom/android/camera/module/MiLiveModule;->O000000o(Lio/reactivex/FlowableEmitter;)V

    return-void
.end method

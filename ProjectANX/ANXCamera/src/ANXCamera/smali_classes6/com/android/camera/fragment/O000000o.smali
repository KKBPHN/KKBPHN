.class public final synthetic Lcom/android/camera/fragment/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/functions/Action;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/fragment/lifeCircle/BaseLifecycleListener;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/fragment/lifeCircle/BaseLifecycleListener;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/O000000o;->O0OOoO0:Lcom/android/camera/fragment/lifeCircle/BaseLifecycleListener;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/fragment/O000000o;->O0OOoO0:Lcom/android/camera/fragment/lifeCircle/BaseLifecycleListener;

    invoke-static {p0}, Lcom/android/camera/fragment/BaseFragmentDelegate;->O000000o(Lcom/android/camera/fragment/lifeCircle/BaseLifecycleListener;)V

    return-void
.end method

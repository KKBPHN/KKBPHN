.class public final synthetic Lcom/android/camera/module/O000OOoO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Landroid/net/Uri;


# direct methods
.method public synthetic constructor <init>(Landroid/net/Uri;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000OOoO;->O0OOoO0:Landroid/net/Uri;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/module/O000OOoO;->O0OOoO0:Landroid/net/Uri;

    invoke-static {p0}, Lcom/android/camera/module/DollyZoomModule;->O00000Oo(Landroid/net/Uri;)V

    return-void
.end method

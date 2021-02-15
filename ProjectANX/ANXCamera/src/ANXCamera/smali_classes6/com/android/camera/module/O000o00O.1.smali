.class public final synthetic Lcom/android/camera/module/O000o00O;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/MiLiveModule;

.field private final synthetic O0OOoOO:Ljava/lang/String;

.field private final synthetic O0OOoOo:Landroid/net/Uri;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/MiLiveModule;Ljava/lang/String;Landroid/net/Uri;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000o00O;->O0OOoO0:Lcom/android/camera/module/MiLiveModule;

    iput-object p2, p0, Lcom/android/camera/module/O000o00O;->O0OOoOO:Ljava/lang/String;

    iput-object p3, p0, Lcom/android/camera/module/O000o00O;->O0OOoOo:Landroid/net/Uri;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/module/O000o00O;->O0OOoO0:Lcom/android/camera/module/MiLiveModule;

    iget-object v1, p0, Lcom/android/camera/module/O000o00O;->O0OOoOO:Ljava/lang/String;

    iget-object p0, p0, Lcom/android/camera/module/O000o00O;->O0OOoOo:Landroid/net/Uri;

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/module/MiLiveModule;->O000000o(Ljava/lang/String;Landroid/net/Uri;)V

    return-void
.end method

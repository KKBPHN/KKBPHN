.class public final synthetic Lcom/android/camera/fragment/top/O00000oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/fragment/top/FragmentTopAlert;

.field private final synthetic O0OOoOO:[F


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/fragment/top/FragmentTopAlert;[F)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/top/O00000oo;->O0OOoO0:Lcom/android/camera/fragment/top/FragmentTopAlert;

    iput-object p2, p0, Lcom/android/camera/fragment/top/O00000oo;->O0OOoOO:[F

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/fragment/top/O00000oo;->O0OOoO0:Lcom/android/camera/fragment/top/FragmentTopAlert;

    iget-object p0, p0, Lcom/android/camera/fragment/top/O00000oo;->O0OOoOO:[F

    invoke-virtual {v0, p0}, Lcom/android/camera/fragment/top/FragmentTopAlert;->O000000o([F)V

    return-void
.end method

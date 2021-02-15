.class public final synthetic Lcom/android/camera/upgrade/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:J


# direct methods
.method public synthetic constructor <init>(J)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-wide p1, p0, Lcom/android/camera/upgrade/O000000o;->O0OOoO0:J

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-wide v0, p0, Lcom/android/camera/upgrade/O000000o;->O0OOoO0:J

    invoke-static {v0, v1}, Lcom/android/camera/upgrade/UpgradeManager;->O00000o0(J)V

    return-void
.end method

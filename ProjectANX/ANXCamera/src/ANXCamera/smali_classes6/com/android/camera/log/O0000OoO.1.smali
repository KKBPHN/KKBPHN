.class public final synthetic Lcom/android/camera/log/O0000OoO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Ljava/lang/String;

.field private final synthetic O0OOoOO:Ljava/lang/String;

.field private final synthetic O0OOoOo:Ljava/lang/Throwable;


# direct methods
.method public synthetic constructor <init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/log/O0000OoO;->O0OOoO0:Ljava/lang/String;

    iput-object p2, p0, Lcom/android/camera/log/O0000OoO;->O0OOoOO:Ljava/lang/String;

    iput-object p3, p0, Lcom/android/camera/log/O0000OoO;->O0OOoOo:Ljava/lang/Throwable;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/log/O0000OoO;->O0OOoO0:Ljava/lang/String;

    iget-object v1, p0, Lcom/android/camera/log/O0000OoO;->O0OOoOO:Ljava/lang/String;

    iget-object p0, p0, Lcom/android/camera/log/O0000OoO;->O0OOoOo:Ljava/lang/Throwable;

    invoke-static {v0, v1, p0}, Lcom/android/camera/log/FileLogger;->O000000o(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

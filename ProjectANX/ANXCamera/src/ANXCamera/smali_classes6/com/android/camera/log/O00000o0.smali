.class public final synthetic Lcom/android/camera/log/O00000o0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Ljava/lang/String;

.field private final synthetic O0OOoOO:Ljava/lang/Throwable;


# direct methods
.method public synthetic constructor <init>(Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/log/O00000o0;->O0OOoO0:Ljava/lang/String;

    iput-object p2, p0, Lcom/android/camera/log/O00000o0;->O0OOoOO:Ljava/lang/Throwable;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/log/O00000o0;->O0OOoO0:Ljava/lang/String;

    iget-object p0, p0, Lcom/android/camera/log/O00000o0;->O0OOoOO:Ljava/lang/Throwable;

    invoke-static {v0, p0}, Lcom/android/camera/log/FileLogger;->O00000o(Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

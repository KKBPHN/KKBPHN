.class public final synthetic Lcom/android/camera/log/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Ljava/lang/String;

.field private final synthetic O0OOoOO:Ljava/lang/String;


# direct methods
.method public synthetic constructor <init>(Ljava/lang/String;Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/log/O000000o;->O0OOoO0:Ljava/lang/String;

    iput-object p2, p0, Lcom/android/camera/log/O000000o;->O0OOoOO:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/log/O000000o;->O0OOoO0:Ljava/lang/String;

    iget-object p0, p0, Lcom/android/camera/log/O000000o;->O0OOoOO:Ljava/lang/String;

    invoke-static {v0, p0}, Lcom/android/camera/log/FileLogger;->O0000Oo(Ljava/lang/String;Ljava/lang/String;)V

    return-void
.end method

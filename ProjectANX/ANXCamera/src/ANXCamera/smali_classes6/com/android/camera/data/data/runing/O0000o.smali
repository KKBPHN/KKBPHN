.class public final synthetic Lcom/android/camera/data/data/runing/O0000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Ljava/util/concurrent/ConcurrentHashMap;


# direct methods
.method public synthetic constructor <init>(Ljava/util/concurrent/ConcurrentHashMap;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/data/data/runing/O0000o;->O0OOoO0:Ljava/util/concurrent/ConcurrentHashMap;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/data/data/runing/O0000o;->O0OOoO0:Ljava/util/concurrent/ConcurrentHashMap;

    check-cast p1, Ljava/lang/Integer;

    invoke-static {p0, p1}, Lcom/android/camera/data/data/runing/ComponentRunningDualVideo;->O000000o(Ljava/util/concurrent/ConcurrentHashMap;Ljava/lang/Integer;)V

    return-void
.end method

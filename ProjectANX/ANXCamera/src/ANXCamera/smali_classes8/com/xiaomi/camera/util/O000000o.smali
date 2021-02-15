.class public final synthetic Lcom/xiaomi/camera/util/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Predicate;


# instance fields
.field private final synthetic O0OOoO0:Lcom/xiaomi/camera/util/StateMachine$SmHandler$StateInfo;


# direct methods
.method public synthetic constructor <init>(Lcom/xiaomi/camera/util/StateMachine$SmHandler$StateInfo;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/xiaomi/camera/util/O000000o;->O0OOoO0:Lcom/xiaomi/camera/util/StateMachine$SmHandler$StateInfo;

    return-void
.end method


# virtual methods
.method public final test(Ljava/lang/Object;)Z
    .locals 0

    iget-object p0, p0, Lcom/xiaomi/camera/util/O000000o;->O0OOoO0:Lcom/xiaomi/camera/util/StateMachine$SmHandler$StateInfo;

    check-cast p1, Lcom/xiaomi/camera/util/StateMachine$SmHandler$StateInfo;

    invoke-static {p0, p1}, Lcom/xiaomi/camera/util/StateMachine$SmHandler;->O000000o(Lcom/xiaomi/camera/util/StateMachine$SmHandler$StateInfo;Lcom/xiaomi/camera/util/StateMachine$SmHandler$StateInfo;)Z

    move-result p0

    return p0
.end method

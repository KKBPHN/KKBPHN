.class Lmiui/animation/utils/LogUtils$1;
.super Ljava/lang/Thread;
.source ""


# direct methods
.method constructor <init>(Ljava/lang/String;)V
    .locals 0

    invoke-direct {p0, p1}, Ljava/lang/Thread;-><init>(Ljava/lang/String;)V

    return-void
.end method


# virtual methods
.method public run()V
    .locals 3

    const-string p0, "miuisdk_anim"

    const-string v0, ""

    :try_start_0
    const-string v1, "log.tag.folme.level"

    invoke-static {v1}, Lmiui/animation/utils/CommonUtils;->readProp(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    if-nez v1, :cond_0

    goto :goto_0

    :cond_0
    move-object v0, v1

    goto :goto_0

    :catch_0
    move-exception v1

    const-string v2, "can not access property log.tag.folme.level, no log"

    invoke-static {p0, v2, v1}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    :goto_0
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "logLevel = "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {p0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {}, Lmiui/animation/utils/LogUtils;->access$000()Ljava/util/concurrent/atomic/AtomicBoolean;

    move-result-object p0

    const-string v1, "D"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    invoke-virtual {p0, v0}, Ljava/util/concurrent/atomic/AtomicBoolean;->set(Z)V

    return-void
.end method

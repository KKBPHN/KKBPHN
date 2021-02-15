.class final Lmiuix/net/ConnectivityHelper$1;
.super Lmiuix/core/util/SoftReferenceSingleton;
.source ""


# direct methods
.method constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lmiuix/core/util/SoftReferenceSingleton;-><init>()V

    return-void
.end method


# virtual methods
.method protected bridge synthetic createInstance(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0, p1}, Lmiuix/net/ConnectivityHelper$1;->createInstance(Ljava/lang/Object;)Lmiuix/net/ConnectivityHelper;

    move-result-object p0

    return-object p0
.end method

.method protected createInstance(Ljava/lang/Object;)Lmiuix/net/ConnectivityHelper;
    .locals 1

    new-instance p0, Lmiuix/net/ConnectivityHelper;

    check-cast p1, Landroid/content/Context;

    const/4 v0, 0x0

    invoke-direct {p0, p1, v0}, Lmiuix/net/ConnectivityHelper;-><init>(Landroid/content/Context;Lmiuix/net/ConnectivityHelper$1;)V

    return-object p0
.end method

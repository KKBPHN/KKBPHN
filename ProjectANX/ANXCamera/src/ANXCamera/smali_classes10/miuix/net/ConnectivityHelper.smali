.class public Lmiuix/net/ConnectivityHelper;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final INSTANCE:Lmiuix/core/util/SoftReferenceSingleton;

.field private static final TAG:Ljava/lang/String; = "ConnectivityHelper"


# instance fields
.field private mConnectivityManager:Landroid/net/ConnectivityManager;

.field private mMacAddress:Ljava/lang/String;

.field private mWifiManager:Landroid/net/wifi/WifiManager;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    new-instance v0, Lmiuix/net/ConnectivityHelper$1;

    invoke-direct {v0}, Lmiuix/net/ConnectivityHelper$1;-><init>()V

    sput-object v0, Lmiuix/net/ConnectivityHelper;->INSTANCE:Lmiuix/core/util/SoftReferenceSingleton;

    return-void
.end method

.method private constructor <init>(Landroid/content/Context;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const-string v0, "connectivity"

    invoke-virtual {p1, v0}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/net/ConnectivityManager;

    iput-object p1, p0, Lmiuix/net/ConnectivityHelper;->mConnectivityManager:Landroid/net/ConnectivityManager;

    return-void
.end method

.method synthetic constructor <init>(Landroid/content/Context;Lmiuix/net/ConnectivityHelper$1;)V
    .locals 0

    invoke-direct {p0, p1}, Lmiuix/net/ConnectivityHelper;-><init>(Landroid/content/Context;)V

    return-void
.end method

.method public static getInstance(Landroid/content/Context;)Lmiuix/net/ConnectivityHelper;
    .locals 1

    sget-object v0, Lmiuix/net/ConnectivityHelper;->INSTANCE:Lmiuix/core/util/SoftReferenceSingleton;

    invoke-virtual {v0, p0}, Lmiuix/core/util/SoftReferenceSingleton;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lmiuix/net/ConnectivityHelper;

    return-object p0
.end method


# virtual methods
.method public getMacAddress(Landroid/content/Context;)Ljava/lang/String;
    .locals 1
    .annotation build Landroidx/annotation/RequiresPermission;
        value = "android.permission.ACCESS_WIFI_STATE"
    .end annotation

    iget-object v0, p0, Lmiuix/net/ConnectivityHelper;->mMacAddress:Ljava/lang/String;

    if-eqz v0, :cond_0

    return-object v0

    :cond_0
    iget-object v0, p0, Lmiuix/net/ConnectivityHelper;->mWifiManager:Landroid/net/wifi/WifiManager;

    if-nez v0, :cond_1

    invoke-virtual {p1}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object p1

    const-string v0, "wifi"

    invoke-virtual {p1, v0}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object p1

    check-cast p1, Landroid/net/wifi/WifiManager;

    iput-object p1, p0, Lmiuix/net/ConnectivityHelper;->mWifiManager:Landroid/net/wifi/WifiManager;

    :cond_1
    iget-object p1, p0, Lmiuix/net/ConnectivityHelper;->mWifiManager:Landroid/net/wifi/WifiManager;

    invoke-virtual {p1}, Landroid/net/wifi/WifiManager;->getConnectionInfo()Landroid/net/wifi/WifiInfo;

    move-result-object p1

    if-eqz p1, :cond_2

    invoke-virtual {p1}, Landroid/net/wifi/WifiInfo;->getMacAddress()Ljava/lang/String;

    move-result-object p1

    iput-object p1, p0, Lmiuix/net/ConnectivityHelper;->mMacAddress:Ljava/lang/String;

    :cond_2
    iget-object p0, p0, Lmiuix/net/ConnectivityHelper;->mMacAddress:Ljava/lang/String;

    return-object p0
.end method

.method public getManager()Landroid/net/ConnectivityManager;
    .locals 0

    iget-object p0, p0, Lmiuix/net/ConnectivityHelper;->mConnectivityManager:Landroid/net/ConnectivityManager;

    return-object p0
.end method

.method public isNetworkConnected()Z
    .locals 0
    .annotation build Landroidx/annotation/RequiresPermission;
        value = "android.permission.ACCESS_NETWORK_STATE"
    .end annotation

    iget-object p0, p0, Lmiuix/net/ConnectivityHelper;->mConnectivityManager:Landroid/net/ConnectivityManager;

    invoke-virtual {p0}, Landroid/net/ConnectivityManager;->getActiveNetworkInfo()Landroid/net/NetworkInfo;

    move-result-object p0

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Landroid/net/NetworkInfo;->isConnected()Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public isUnmeteredNetworkConnected()Z
    .locals 1
    .annotation build Landroidx/annotation/RequiresPermission;
        value = "android.permission.ACCESS_NETWORK_STATE"
    .end annotation

    iget-object v0, p0, Lmiuix/net/ConnectivityHelper;->mConnectivityManager:Landroid/net/ConnectivityManager;

    invoke-virtual {v0}, Landroid/net/ConnectivityManager;->getActiveNetworkInfo()Landroid/net/NetworkInfo;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {v0}, Landroid/net/NetworkInfo;->isConnected()Z

    move-result v0

    if-eqz v0, :cond_0

    iget-object p0, p0, Lmiuix/net/ConnectivityHelper;->mConnectivityManager:Landroid/net/ConnectivityManager;

    invoke-virtual {p0}, Landroid/net/ConnectivityManager;->isActiveNetworkMetered()Z

    move-result p0

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public isWifiConnected()Z
    .locals 2
    .annotation build Landroidx/annotation/RequiresPermission;
        value = "android.permission.ACCESS_NETWORK_STATE"
    .end annotation

    iget-object p0, p0, Lmiuix/net/ConnectivityHelper;->mConnectivityManager:Landroid/net/ConnectivityManager;

    invoke-virtual {p0}, Landroid/net/ConnectivityManager;->getActiveNetworkInfo()Landroid/net/NetworkInfo;

    move-result-object p0

    const/4 v0, 0x1

    if-eqz p0, :cond_0

    invoke-virtual {p0}, Landroid/net/NetworkInfo;->isConnected()Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-virtual {p0}, Landroid/net/NetworkInfo;->getType()I

    move-result p0

    if-ne p0, v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public isWifiOnly()Z
    .locals 2

    invoke-static {}, Lmiuix/core/util/ConnectivityManagerWrapper;->getInstance()Lmiuix/core/util/ConnectivityManagerWrapper;

    move-result-object v0

    iget-object p0, p0, Lmiuix/net/ConnectivityHelper;->mConnectivityManager:Landroid/net/ConnectivityManager;

    const/4 v1, 0x0

    invoke-virtual {v0, p0, v1}, Lmiuix/core/util/ConnectivityManagerWrapper;->isNetworkSupported(Landroid/net/ConnectivityManager;I)Z

    move-result p0

    xor-int/lit8 p0, p0, 0x1

    return p0
.end method

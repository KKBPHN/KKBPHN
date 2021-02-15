.class public LO00000Oo/O00000o0/O000000o/O00000o;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final TAG:Ljava/lang/String; = "IotDeviceInfo"


# instance fields
.field private O0OoO:Ljava/lang/String;

.field private O0OoOO0:Ljava/lang/String;

.field private O0OoOOO:I

.field private O0OoOOo:I

.field private O0OoOo0:Ljava/lang/String;

.field private O0OoOoO:I

.field private OoO0o:Ljava/lang/String;

.field private bssid:Ljava/lang/String;

.field private isOnline:Z

.field private latitude:Ljava/lang/String;

.field private longitude:Ljava/lang/String;

.field private mac:Ljava/lang/String;

.field private model:Ljava/lang/String;

.field private name:Ljava/lang/String;

.field private pid:I

.field private rssi:I

.field private ssid:Ljava/lang/String;

.field private token:Ljava/lang/String;

.field private uid:J


# direct methods
.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static final buildFromJson(Ljava/lang/String;)LO00000Oo/O00000o0/O000000o/O00000o;
    .locals 2

    :try_start_0
    new-instance v0, Lorg/json/JSONObject;

    invoke-direct {v0, p0}, Lorg/json/JSONObject;-><init>(Ljava/lang/String;)V

    invoke-static {v0}, LO00000Oo/O00000o0/O000000o/O00000o;->buildFromJson(Lorg/json/JSONObject;)LO00000Oo/O00000o0/O000000o/O00000o;

    move-result-object p0
    :try_end_0
    .catch Lorg/json/JSONException; {:try_start_0 .. :try_end_0} :catch_0

    return-object p0

    :catch_0
    move-exception p0

    const-string v0, "IotDeviceInfo"

    const-string v1, ""

    invoke-static {v0, v1, p0}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    const/4 p0, 0x0

    return-object p0
.end method

.method public static final buildFromJson(Lorg/json/JSONObject;)LO00000Oo/O00000o0/O000000o/O00000o;
    .locals 3

    :try_start_0
    new-instance v0, LO00000Oo/O00000o0/O000000o/O00000o;

    invoke-direct {v0}, LO00000Oo/O00000o0/O000000o/O00000o;-><init>()V

    const-string v1, "did"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->OoO0o:Ljava/lang/String;

    const-string v1, "uid"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getLong(Ljava/lang/String;)J

    move-result-wide v1

    iput-wide v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->uid:J

    const-string v1, "token"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->token:Ljava/lang/String;

    const-string v1, "name"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->name:Ljava/lang/String;

    const-string v1, "pid"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getInt(Ljava/lang/String;)I

    move-result v1

    iput v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->pid:I

    const-string v1, "localip"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->optString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoO:Ljava/lang/String;

    const-string v1, "mac"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->mac:Ljava/lang/String;

    const-string v1, "ssid"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->optString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->ssid:Ljava/lang/String;

    const-string v1, "bssid"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->bssid:Ljava/lang/String;

    const-string v1, "rssi"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->optInt(Ljava/lang/String;)I

    move-result v1

    iput v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->rssi:I

    const-string v1, "longitude"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->optString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->longitude:Ljava/lang/String;

    const-string v1, "latitude"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->optString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->latitude:Ljava/lang/String;

    const-string v1, "parent_id"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->optString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOO0:Ljava/lang/String;

    const-string v1, "show_mode"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getInt(Ljava/lang/String;)I

    move-result v1

    iput v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOOO:I

    const-string v1, "model"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->model:Ljava/lang/String;

    const-string v1, "permitLevel"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getInt(Ljava/lang/String;)I

    move-result v1

    iput v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOOo:I

    const-string v1, "isOnline"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->getBoolean(Ljava/lang/String;)Z

    move-result v1

    iput-boolean v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->isOnline:Z

    const-string v1, "spec_type"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->optString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    iput-object v1, v0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOo0:Ljava/lang/String;

    const-string/jumbo v1, "voice_ctrl"

    invoke-virtual {p0, v1}, Lorg/json/JSONObject;->optInt(Ljava/lang/String;)I

    move-result p0

    iput p0, v0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOoO:I
    :try_end_0
    .catch Lorg/json/JSONException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception p0

    const-string v0, "IotDeviceInfo"

    const-string v1, "Error when create IotDeviceInfo"

    invoke-static {v0, v1, p0}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    const/4 v0, 0x0

    :goto_0
    return-object v0
.end method

.method private static setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V
    .locals 0

    if-eqz p2, :cond_0

    invoke-virtual {p0, p1, p2}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

    :cond_0
    return-void
.end method


# virtual methods
.method public O0oOo()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoO:Ljava/lang/String;

    return-object p0
.end method

.method public O0oOo0o()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->OoO0o:Ljava/lang/String;

    return-object p0
.end method

.method public O0oOoO()I
    .locals 0

    iget p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOOo:I

    return p0
.end method

.method public O0oOoO0()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOO0:Ljava/lang/String;

    return-object p0
.end method

.method public O0oOoOO()I
    .locals 0

    iget p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOOO:I

    return p0
.end method

.method public O0oOoOo()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOo0:Ljava/lang/String;

    return-object p0
.end method

.method public O0oOoo0()Z
    .locals 0

    iget-boolean p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->isOnline:Z

    return p0
.end method

.method public OOoOoo()I
    .locals 0

    iget p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOoO:I

    return p0
.end method

.method public getBssid()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->bssid:Ljava/lang/String;

    return-object p0
.end method

.method public getLatitude()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->latitude:Ljava/lang/String;

    return-object p0
.end method

.method public getLongitude()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->longitude:Ljava/lang/String;

    return-object p0
.end method

.method public getMac()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->mac:Ljava/lang/String;

    return-object p0
.end method

.method public getModel()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->model:Ljava/lang/String;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getPid()I
    .locals 0

    iget p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->pid:I

    return p0
.end method

.method public getRssi()I
    .locals 0

    iget p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->rssi:I

    return p0
.end method

.method public getSsid()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->ssid:Ljava/lang/String;

    return-object p0
.end method

.method public getToken()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->token:Ljava/lang/String;

    return-object p0
.end method

.method public getUid()J
    .locals 2

    iget-wide v0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->uid:J

    return-wide v0
.end method

.method public toJson()Lorg/json/JSONObject;
    .locals 4

    new-instance v0, Lorg/json/JSONObject;

    invoke-direct {v0}, Lorg/json/JSONObject;-><init>()V

    :try_start_0
    const-string v1, "did"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->OoO0o:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "uid"

    iget-wide v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->uid:J

    invoke-static {v2, v3}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v2

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "token"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->token:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "name"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->name:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "pid"

    iget v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->pid:I

    invoke-static {v2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "localip"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoO:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "mac"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->mac:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "ssid"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->ssid:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "bssid"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->bssid:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "rssi"

    iget v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->rssi:I

    invoke-static {v2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "longitude"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->longitude:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "latitude"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->latitude:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "parent_id"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOO0:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "show_mode"

    iget v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOOO:I

    invoke-static {v2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "model"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->model:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "permitLevel"

    iget v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOOo:I

    invoke-static {v2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "isOnline"

    iget-boolean v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->isOnline:Z

    invoke-static {v2}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v2

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string v1, "spec_type"

    iget-object v2, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOo0:Ljava/lang/String;

    invoke-static {v0, v1, v2}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V

    const-string/jumbo v1, "voice_ctrl"

    iget p0, p0, LO00000Oo/O00000o0/O000000o/O00000o;->O0OoOoO:I

    invoke-static {p0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p0

    invoke-static {v0, v1, p0}, LO00000Oo/O00000o0/O000000o/O00000o;->setJsonValue(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/Object;)V
    :try_end_0
    .catch Lorg/json/JSONException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    :catch_0
    move-exception p0

    const-string v1, "IotDeviceInfo"

    const-string v2, ""

    invoke-static {v1, v2, p0}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    :goto_0
    return-object v0
.end method

.method public toJsonString()Ljava/lang/String;
    .locals 0

    invoke-virtual {p0}, LO00000Oo/O00000o0/O000000o/O00000o;->toJson()Lorg/json/JSONObject;

    move-result-object p0

    invoke-virtual {p0}, Lorg/json/JSONObject;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

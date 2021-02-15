.class final enum Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

.field public static final enum LOGIN_FINISHED:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

.field public static final enum LOGIN_INPROGRESS:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

.field public static final enum LOGIN_START:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;


# direct methods
.method static constructor <clinit>()V
    .locals 5

    new-instance v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    const/4 v1, 0x0

    const-string v2, "LOGIN_START"

    invoke-direct {v0, v2, v1}, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->LOGIN_START:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    new-instance v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    const/4 v2, 0x1

    const-string v3, "LOGIN_INPROGRESS"

    invoke-direct {v0, v3, v2}, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->LOGIN_INPROGRESS:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    new-instance v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    const/4 v3, 0x2

    const-string v4, "LOGIN_FINISHED"

    invoke-direct {v0, v4, v3}, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->LOGIN_FINISHED:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    const/4 v0, 0x3

    new-array v0, v0, [Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    sget-object v4, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->LOGIN_START:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    aput-object v4, v0, v1

    sget-object v1, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->LOGIN_INPROGRESS:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    aput-object v1, v0, v2

    sget-object v1, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->LOGIN_FINISHED:Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    aput-object v1, v0, v3

    sput-object v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->$VALUES:[Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;
    .locals 1

    const-class v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    return-object p0
.end method

.method public static values()[Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;
    .locals 1

    sget-object v0, Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->$VALUES:[Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    invoke-virtual {v0}, [Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/miui/internal/webkit/WebViewClientDelegate$LoginState;

    return-object v0
.end method

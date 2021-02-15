.class public final enum Lcom/miui/internal/log/Level;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lcom/miui/internal/log/Level;

.field public static final enum DEBUG:Lcom/miui/internal/log/Level;

.field public static final enum ERROR:Lcom/miui/internal/log/Level;

.field public static final enum FATAL:Lcom/miui/internal/log/Level;

.field public static final enum INFO:Lcom/miui/internal/log/Level;

.field public static final enum VERBOSE:Lcom/miui/internal/log/Level;

.field public static final enum WARNING:Lcom/miui/internal/log/Level;


# direct methods
.method static constructor <clinit>()V
    .locals 8

    new-instance v0, Lcom/miui/internal/log/Level;

    const/4 v1, 0x0

    const-string v2, "VERBOSE"

    invoke-direct {v0, v2, v1}, Lcom/miui/internal/log/Level;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/log/Level;->VERBOSE:Lcom/miui/internal/log/Level;

    new-instance v0, Lcom/miui/internal/log/Level;

    const/4 v2, 0x1

    const-string v3, "DEBUG"

    invoke-direct {v0, v3, v2}, Lcom/miui/internal/log/Level;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/log/Level;->DEBUG:Lcom/miui/internal/log/Level;

    new-instance v0, Lcom/miui/internal/log/Level;

    const/4 v3, 0x2

    const-string v4, "INFO"

    invoke-direct {v0, v4, v3}, Lcom/miui/internal/log/Level;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/log/Level;->INFO:Lcom/miui/internal/log/Level;

    new-instance v0, Lcom/miui/internal/log/Level;

    const/4 v4, 0x3

    const-string v5, "WARNING"

    invoke-direct {v0, v5, v4}, Lcom/miui/internal/log/Level;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/log/Level;->WARNING:Lcom/miui/internal/log/Level;

    new-instance v0, Lcom/miui/internal/log/Level;

    const/4 v5, 0x4

    const-string v6, "ERROR"

    invoke-direct {v0, v6, v5}, Lcom/miui/internal/log/Level;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/log/Level;->ERROR:Lcom/miui/internal/log/Level;

    new-instance v0, Lcom/miui/internal/log/Level;

    const/4 v6, 0x5

    const-string v7, "FATAL"

    invoke-direct {v0, v7, v6}, Lcom/miui/internal/log/Level;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/miui/internal/log/Level;->FATAL:Lcom/miui/internal/log/Level;

    const/4 v0, 0x6

    new-array v0, v0, [Lcom/miui/internal/log/Level;

    sget-object v7, Lcom/miui/internal/log/Level;->VERBOSE:Lcom/miui/internal/log/Level;

    aput-object v7, v0, v1

    sget-object v1, Lcom/miui/internal/log/Level;->DEBUG:Lcom/miui/internal/log/Level;

    aput-object v1, v0, v2

    sget-object v1, Lcom/miui/internal/log/Level;->INFO:Lcom/miui/internal/log/Level;

    aput-object v1, v0, v3

    sget-object v1, Lcom/miui/internal/log/Level;->WARNING:Lcom/miui/internal/log/Level;

    aput-object v1, v0, v4

    sget-object v1, Lcom/miui/internal/log/Level;->ERROR:Lcom/miui/internal/log/Level;

    aput-object v1, v0, v5

    sget-object v1, Lcom/miui/internal/log/Level;->FATAL:Lcom/miui/internal/log/Level;

    aput-object v1, v0, v6

    sput-object v0, Lcom/miui/internal/log/Level;->$VALUES:[Lcom/miui/internal/log/Level;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/miui/internal/log/Level;
    .locals 1

    const-class v0, Lcom/miui/internal/log/Level;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lcom/miui/internal/log/Level;

    return-object p0
.end method

.method public static values()[Lcom/miui/internal/log/Level;
    .locals 1

    sget-object v0, Lcom/miui/internal/log/Level;->$VALUES:[Lcom/miui/internal/log/Level;

    invoke-virtual {v0}, [Lcom/miui/internal/log/Level;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/miui/internal/log/Level;

    return-object v0
.end method

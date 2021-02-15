.class public final enum Lmiui/hybrid/HybridFeature$Mode;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lmiui/hybrid/HybridFeature$Mode;

.field public static final enum ASYNC:Lmiui/hybrid/HybridFeature$Mode;

.field public static final enum CALLBACK:Lmiui/hybrid/HybridFeature$Mode;

.field public static final enum SYNC:Lmiui/hybrid/HybridFeature$Mode;


# direct methods
.method static constructor <clinit>()V
    .locals 5

    new-instance v0, Lmiui/hybrid/HybridFeature$Mode;

    const/4 v1, 0x0

    const-string v2, "SYNC"

    invoke-direct {v0, v2, v1}, Lmiui/hybrid/HybridFeature$Mode;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/hybrid/HybridFeature$Mode;->SYNC:Lmiui/hybrid/HybridFeature$Mode;

    new-instance v0, Lmiui/hybrid/HybridFeature$Mode;

    const/4 v2, 0x1

    const-string v3, "ASYNC"

    invoke-direct {v0, v3, v2}, Lmiui/hybrid/HybridFeature$Mode;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/hybrid/HybridFeature$Mode;->ASYNC:Lmiui/hybrid/HybridFeature$Mode;

    new-instance v0, Lmiui/hybrid/HybridFeature$Mode;

    const/4 v3, 0x2

    const-string v4, "CALLBACK"

    invoke-direct {v0, v4, v3}, Lmiui/hybrid/HybridFeature$Mode;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/hybrid/HybridFeature$Mode;->CALLBACK:Lmiui/hybrid/HybridFeature$Mode;

    const/4 v0, 0x3

    new-array v0, v0, [Lmiui/hybrid/HybridFeature$Mode;

    sget-object v4, Lmiui/hybrid/HybridFeature$Mode;->SYNC:Lmiui/hybrid/HybridFeature$Mode;

    aput-object v4, v0, v1

    sget-object v1, Lmiui/hybrid/HybridFeature$Mode;->ASYNC:Lmiui/hybrid/HybridFeature$Mode;

    aput-object v1, v0, v2

    sget-object v1, Lmiui/hybrid/HybridFeature$Mode;->CALLBACK:Lmiui/hybrid/HybridFeature$Mode;

    aput-object v1, v0, v3

    sput-object v0, Lmiui/hybrid/HybridFeature$Mode;->$VALUES:[Lmiui/hybrid/HybridFeature$Mode;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lmiui/hybrid/HybridFeature$Mode;
    .locals 1

    const-class v0, Lmiui/hybrid/HybridFeature$Mode;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lmiui/hybrid/HybridFeature$Mode;

    return-object p0
.end method

.method public static values()[Lmiui/hybrid/HybridFeature$Mode;
    .locals 1

    sget-object v0, Lmiui/hybrid/HybridFeature$Mode;->$VALUES:[Lmiui/hybrid/HybridFeature$Mode;

    invoke-virtual {v0}, [Lmiui/hybrid/HybridFeature$Mode;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lmiui/hybrid/HybridFeature$Mode;

    return-object v0
.end method

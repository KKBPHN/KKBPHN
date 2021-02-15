.class public final enum Lmiui/util/async/Task$Priority;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lmiui/util/async/Task$Priority;

.field public static final enum High:Lmiui/util/async/Task$Priority;

.field public static final enum Low:Lmiui/util/async/Task$Priority;

.field public static final enum Normal:Lmiui/util/async/Task$Priority;

.field public static final enum RealTime:Lmiui/util/async/Task$Priority;


# direct methods
.method static constructor <clinit>()V
    .locals 6

    new-instance v0, Lmiui/util/async/Task$Priority;

    const/4 v1, 0x0

    const-string v2, "Low"

    invoke-direct {v0, v2, v1}, Lmiui/util/async/Task$Priority;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Priority;->Low:Lmiui/util/async/Task$Priority;

    new-instance v0, Lmiui/util/async/Task$Priority;

    const/4 v2, 0x1

    const-string v3, "Normal"

    invoke-direct {v0, v3, v2}, Lmiui/util/async/Task$Priority;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Priority;->Normal:Lmiui/util/async/Task$Priority;

    new-instance v0, Lmiui/util/async/Task$Priority;

    const/4 v3, 0x2

    const-string v4, "High"

    invoke-direct {v0, v4, v3}, Lmiui/util/async/Task$Priority;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Priority;->High:Lmiui/util/async/Task$Priority;

    new-instance v0, Lmiui/util/async/Task$Priority;

    const/4 v4, 0x3

    const-string v5, "RealTime"

    invoke-direct {v0, v5, v4}, Lmiui/util/async/Task$Priority;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Priority;->RealTime:Lmiui/util/async/Task$Priority;

    const/4 v0, 0x4

    new-array v0, v0, [Lmiui/util/async/Task$Priority;

    sget-object v5, Lmiui/util/async/Task$Priority;->Low:Lmiui/util/async/Task$Priority;

    aput-object v5, v0, v1

    sget-object v1, Lmiui/util/async/Task$Priority;->Normal:Lmiui/util/async/Task$Priority;

    aput-object v1, v0, v2

    sget-object v1, Lmiui/util/async/Task$Priority;->High:Lmiui/util/async/Task$Priority;

    aput-object v1, v0, v3

    sget-object v1, Lmiui/util/async/Task$Priority;->RealTime:Lmiui/util/async/Task$Priority;

    aput-object v1, v0, v4

    sput-object v0, Lmiui/util/async/Task$Priority;->$VALUES:[Lmiui/util/async/Task$Priority;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lmiui/util/async/Task$Priority;
    .locals 1

    const-class v0, Lmiui/util/async/Task$Priority;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lmiui/util/async/Task$Priority;

    return-object p0
.end method

.method public static values()[Lmiui/util/async/Task$Priority;
    .locals 1

    sget-object v0, Lmiui/util/async/Task$Priority;->$VALUES:[Lmiui/util/async/Task$Priority;

    invoke-virtual {v0}, [Lmiui/util/async/Task$Priority;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lmiui/util/async/Task$Priority;

    return-object v0
.end method

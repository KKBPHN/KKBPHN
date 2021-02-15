.class public final enum Lmiui/util/async/Task$Status;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lmiui/util/async/Task$Status;

.field public static final enum Canceled:Lmiui/util/async/Task$Status;

.field public static final enum Done:Lmiui/util/async/Task$Status;

.field public static final enum Executing:Lmiui/util/async/Task$Status;

.field public static final enum New:Lmiui/util/async/Task$Status;

.field public static final enum Queued:Lmiui/util/async/Task$Status;


# direct methods
.method static constructor <clinit>()V
    .locals 7

    new-instance v0, Lmiui/util/async/Task$Status;

    const/4 v1, 0x0

    const-string v2, "New"

    invoke-direct {v0, v2, v1}, Lmiui/util/async/Task$Status;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Status;->New:Lmiui/util/async/Task$Status;

    new-instance v0, Lmiui/util/async/Task$Status;

    const/4 v2, 0x1

    const-string v3, "Queued"

    invoke-direct {v0, v3, v2}, Lmiui/util/async/Task$Status;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Status;->Queued:Lmiui/util/async/Task$Status;

    new-instance v0, Lmiui/util/async/Task$Status;

    const/4 v3, 0x2

    const-string v4, "Executing"

    invoke-direct {v0, v4, v3}, Lmiui/util/async/Task$Status;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Status;->Executing:Lmiui/util/async/Task$Status;

    new-instance v0, Lmiui/util/async/Task$Status;

    const/4 v4, 0x3

    const-string v5, "Done"

    invoke-direct {v0, v5, v4}, Lmiui/util/async/Task$Status;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Status;->Done:Lmiui/util/async/Task$Status;

    new-instance v0, Lmiui/util/async/Task$Status;

    const/4 v5, 0x4

    const-string v6, "Canceled"

    invoke-direct {v0, v6, v5}, Lmiui/util/async/Task$Status;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/Task$Status;->Canceled:Lmiui/util/async/Task$Status;

    const/4 v0, 0x5

    new-array v0, v0, [Lmiui/util/async/Task$Status;

    sget-object v6, Lmiui/util/async/Task$Status;->New:Lmiui/util/async/Task$Status;

    aput-object v6, v0, v1

    sget-object v1, Lmiui/util/async/Task$Status;->Queued:Lmiui/util/async/Task$Status;

    aput-object v1, v0, v2

    sget-object v1, Lmiui/util/async/Task$Status;->Executing:Lmiui/util/async/Task$Status;

    aput-object v1, v0, v3

    sget-object v1, Lmiui/util/async/Task$Status;->Done:Lmiui/util/async/Task$Status;

    aput-object v1, v0, v4

    sget-object v1, Lmiui/util/async/Task$Status;->Canceled:Lmiui/util/async/Task$Status;

    aput-object v1, v0, v5

    sput-object v0, Lmiui/util/async/Task$Status;->$VALUES:[Lmiui/util/async/Task$Status;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lmiui/util/async/Task$Status;
    .locals 1

    const-class v0, Lmiui/util/async/Task$Status;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lmiui/util/async/Task$Status;

    return-object p0
.end method

.method public static values()[Lmiui/util/async/Task$Status;
    .locals 1

    sget-object v0, Lmiui/util/async/Task$Status;->$VALUES:[Lmiui/util/async/Task$Status;

    invoke-virtual {v0}, [Lmiui/util/async/Task$Status;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lmiui/util/async/Task$Status;

    return-object v0
.end method

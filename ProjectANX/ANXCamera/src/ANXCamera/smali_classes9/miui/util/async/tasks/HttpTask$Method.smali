.class public final enum Lmiui/util/async/tasks/HttpTask$Method;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lmiui/util/async/tasks/HttpTask$Method;

.field public static final enum Get:Lmiui/util/async/tasks/HttpTask$Method;

.field public static final enum Post:Lmiui/util/async/tasks/HttpTask$Method;


# direct methods
.method static constructor <clinit>()V
    .locals 4

    new-instance v0, Lmiui/util/async/tasks/HttpTask$Method;

    const/4 v1, 0x0

    const-string v2, "Get"

    invoke-direct {v0, v2, v1}, Lmiui/util/async/tasks/HttpTask$Method;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/tasks/HttpTask$Method;->Get:Lmiui/util/async/tasks/HttpTask$Method;

    new-instance v0, Lmiui/util/async/tasks/HttpTask$Method;

    const/4 v2, 0x1

    const-string v3, "Post"

    invoke-direct {v0, v3, v2}, Lmiui/util/async/tasks/HttpTask$Method;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/async/tasks/HttpTask$Method;->Post:Lmiui/util/async/tasks/HttpTask$Method;

    const/4 v0, 0x2

    new-array v0, v0, [Lmiui/util/async/tasks/HttpTask$Method;

    sget-object v3, Lmiui/util/async/tasks/HttpTask$Method;->Get:Lmiui/util/async/tasks/HttpTask$Method;

    aput-object v3, v0, v1

    sget-object v1, Lmiui/util/async/tasks/HttpTask$Method;->Post:Lmiui/util/async/tasks/HttpTask$Method;

    aput-object v1, v0, v2

    sput-object v0, Lmiui/util/async/tasks/HttpTask$Method;->$VALUES:[Lmiui/util/async/tasks/HttpTask$Method;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lmiui/util/async/tasks/HttpTask$Method;
    .locals 1

    const-class v0, Lmiui/util/async/tasks/HttpTask$Method;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lmiui/util/async/tasks/HttpTask$Method;

    return-object p0
.end method

.method public static values()[Lmiui/util/async/tasks/HttpTask$Method;
    .locals 1

    sget-object v0, Lmiui/util/async/tasks/HttpTask$Method;->$VALUES:[Lmiui/util/async/tasks/HttpTask$Method;

    invoke-virtual {v0}, [Lmiui/util/async/tasks/HttpTask$Method;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lmiui/util/async/tasks/HttpTask$Method;

    return-object v0
.end method

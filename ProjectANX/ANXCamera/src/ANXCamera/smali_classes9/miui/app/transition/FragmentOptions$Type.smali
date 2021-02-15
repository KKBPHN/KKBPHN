.class public final enum Lmiui/app/transition/FragmentOptions$Type;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lmiui/app/transition/FragmentOptions$Type;

.field public static final enum EXIT_FADE:Lmiui/app/transition/FragmentOptions$Type;

.field public static final enum PHYSIC_FADE:Lmiui/app/transition/FragmentOptions$Type;

.field public static final enum SCALE_DOWN:Lmiui/app/transition/FragmentOptions$Type;

.field public static final enum SCALE_UP:Lmiui/app/transition/FragmentOptions$Type;


# direct methods
.method static constructor <clinit>()V
    .locals 6

    new-instance v0, Lmiui/app/transition/FragmentOptions$Type;

    const/4 v1, 0x0

    const-string v2, "SCALE_UP"

    invoke-direct {v0, v2, v1}, Lmiui/app/transition/FragmentOptions$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/app/transition/FragmentOptions$Type;->SCALE_UP:Lmiui/app/transition/FragmentOptions$Type;

    new-instance v0, Lmiui/app/transition/FragmentOptions$Type;

    const/4 v2, 0x1

    const-string v3, "SCALE_DOWN"

    invoke-direct {v0, v3, v2}, Lmiui/app/transition/FragmentOptions$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/app/transition/FragmentOptions$Type;->SCALE_DOWN:Lmiui/app/transition/FragmentOptions$Type;

    new-instance v0, Lmiui/app/transition/FragmentOptions$Type;

    const/4 v3, 0x2

    const-string v4, "EXIT_FADE"

    invoke-direct {v0, v4, v3}, Lmiui/app/transition/FragmentOptions$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/app/transition/FragmentOptions$Type;->EXIT_FADE:Lmiui/app/transition/FragmentOptions$Type;

    new-instance v0, Lmiui/app/transition/FragmentOptions$Type;

    const/4 v4, 0x3

    const-string v5, "PHYSIC_FADE"

    invoke-direct {v0, v5, v4}, Lmiui/app/transition/FragmentOptions$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/app/transition/FragmentOptions$Type;->PHYSIC_FADE:Lmiui/app/transition/FragmentOptions$Type;

    const/4 v0, 0x4

    new-array v0, v0, [Lmiui/app/transition/FragmentOptions$Type;

    sget-object v5, Lmiui/app/transition/FragmentOptions$Type;->SCALE_UP:Lmiui/app/transition/FragmentOptions$Type;

    aput-object v5, v0, v1

    sget-object v1, Lmiui/app/transition/FragmentOptions$Type;->SCALE_DOWN:Lmiui/app/transition/FragmentOptions$Type;

    aput-object v1, v0, v2

    sget-object v1, Lmiui/app/transition/FragmentOptions$Type;->EXIT_FADE:Lmiui/app/transition/FragmentOptions$Type;

    aput-object v1, v0, v3

    sget-object v1, Lmiui/app/transition/FragmentOptions$Type;->PHYSIC_FADE:Lmiui/app/transition/FragmentOptions$Type;

    aput-object v1, v0, v4

    sput-object v0, Lmiui/app/transition/FragmentOptions$Type;->$VALUES:[Lmiui/app/transition/FragmentOptions$Type;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lmiui/app/transition/FragmentOptions$Type;
    .locals 1

    const-class v0, Lmiui/app/transition/FragmentOptions$Type;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lmiui/app/transition/FragmentOptions$Type;

    return-object p0
.end method

.method public static values()[Lmiui/app/transition/FragmentOptions$Type;
    .locals 1

    sget-object v0, Lmiui/app/transition/FragmentOptions$Type;->$VALUES:[Lmiui/app/transition/FragmentOptions$Type;

    invoke-virtual {v0}, [Lmiui/app/transition/FragmentOptions$Type;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lmiui/app/transition/FragmentOptions$Type;

    return-object v0
.end method

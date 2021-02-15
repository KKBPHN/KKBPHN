.class public final enum Lcom/android/camera/dualvideo/render/LayoutType;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum DOWN:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum DOWN_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum FULL:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum MINI:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum PATCH_1:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum PATCH_2:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum PATCH_3:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum PATCH_4:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum PATCH_5:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum PATCH_REMOTE:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum UNDEFINED:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum UP:Lcom/android/camera/dualvideo/render/LayoutType;

.field public static final enum UP_FULL:Lcom/android/camera/dualvideo/render/LayoutType;


# instance fields
.field private final mIndex:I

.field private final mWeight:I


# direct methods
.method static constructor <clinit>()V
    .locals 16

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/4 v1, 0x0

    const-string v2, "UNDEFINED"

    invoke-direct {v0, v2, v1, v1, v1}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->UNDEFINED:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/16 v2, 0xa

    const/4 v3, 0x1

    const-string v4, "MINI"

    const/16 v5, 0x1e

    invoke-direct {v0, v4, v3, v2, v5}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/4 v4, 0x2

    const/16 v5, 0x32

    const-string v6, "DOWN_FULL"

    invoke-direct {v0, v6, v4, v3, v5}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/4 v6, 0x3

    const-string v7, "UP_FULL"

    invoke-direct {v0, v7, v6, v4, v5}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->UP_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/4 v7, 0x4

    const/16 v8, 0xb

    const-string v9, "UP"

    invoke-direct {v0, v9, v7, v8, v5}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->UP:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/4 v9, 0x5

    const/16 v10, 0xc

    const-string v11, "DOWN"

    invoke-direct {v0, v11, v9, v10, v5}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/4 v5, 0x6

    const/16 v11, 0xd

    const-string v12, "FULL"

    const/16 v13, 0x50

    invoke-direct {v0, v12, v5, v11, v13}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/4 v12, 0x7

    const/16 v13, 0x64

    const-string v14, "PATCH_0"

    const/16 v15, 0x14

    invoke-direct {v0, v14, v12, v15, v13}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const/16 v14, 0x8

    const-string v15, "PATCH_1"

    const/16 v12, 0x15

    invoke-direct {v0, v15, v14, v12, v13}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_1:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const-string v12, "PATCH_2"

    const/16 v15, 0x9

    const/16 v14, 0x16

    invoke-direct {v0, v12, v15, v14, v13}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_2:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const-string v12, "PATCH_3"

    const/16 v14, 0x17

    invoke-direct {v0, v12, v2, v14, v13}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_3:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const-string v12, "PATCH_4"

    const/16 v14, 0x18

    invoke-direct {v0, v12, v8, v14, v13}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_4:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const-string v12, "PATCH_5"

    const/16 v14, 0x19

    invoke-direct {v0, v12, v10, v14, v13}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_5:Lcom/android/camera/dualvideo/render/LayoutType;

    new-instance v0, Lcom/android/camera/dualvideo/render/LayoutType;

    const-string v12, "PATCH_REMOTE"

    const/16 v14, 0x1a

    invoke-direct {v0, v12, v11, v14, v13}, Lcom/android/camera/dualvideo/render/LayoutType;-><init>(Ljava/lang/String;III)V

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_REMOTE:Lcom/android/camera/dualvideo/render/LayoutType;

    const/16 v0, 0xe

    new-array v0, v0, [Lcom/android/camera/dualvideo/render/LayoutType;

    sget-object v12, Lcom/android/camera/dualvideo/render/LayoutType;->UNDEFINED:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v12, v0, v1

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->MINI:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v3

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v4

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->UP_FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v6

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->UP:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v7

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->DOWN:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v9

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->FULL:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v5

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

    const/4 v3, 0x7

    aput-object v1, v0, v3

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_1:Lcom/android/camera/dualvideo/render/LayoutType;

    const/16 v3, 0x8

    aput-object v1, v0, v3

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_2:Lcom/android/camera/dualvideo/render/LayoutType;

    const/16 v3, 0x9

    aput-object v1, v0, v3

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_3:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v2

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_4:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v8

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_5:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v10

    sget-object v1, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_REMOTE:Lcom/android/camera/dualvideo/render/LayoutType;

    aput-object v1, v0, v11

    sput-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->$VALUES:[Lcom/android/camera/dualvideo/render/LayoutType;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;III)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    iput p3, p0, Lcom/android/camera/dualvideo/render/LayoutType;->mIndex:I

    iput p4, p0, Lcom/android/camera/dualvideo/render/LayoutType;->mWeight:I

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/android/camera/dualvideo/render/LayoutType;
    .locals 1

    const-class v0, Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/render/LayoutType;

    return-object p0
.end method

.method public static values()[Lcom/android/camera/dualvideo/render/LayoutType;
    .locals 1

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->$VALUES:[Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-virtual {v0}, [Lcom/android/camera/dualvideo/render/LayoutType;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/android/camera/dualvideo/render/LayoutType;

    return-object v0
.end method


# virtual methods
.method public getIndex()I
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/render/LayoutType;->mIndex:I

    return p0
.end method

.method public getWeight()I
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/render/LayoutType;->mWeight:I

    return p0
.end method

.method public isSelectWindowType()Z
    .locals 1

    iget p0, p0, Lcom/android/camera/dualvideo/render/LayoutType;->mIndex:I

    sget-object v0, Lcom/android/camera/dualvideo/render/LayoutType;->PATCH_0:Lcom/android/camera/dualvideo/render/LayoutType;

    invoke-virtual {v0}, Lcom/android/camera/dualvideo/render/LayoutType;->getIndex()I

    move-result v0

    if-lt p0, v0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

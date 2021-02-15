.class public final enum Lcom/android/camera/dualvideo/util/SelectIndex;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lcom/android/camera/dualvideo/util/SelectIndex;

.field public static final enum INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

.field public static final enum INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

.field public static final enum INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;


# instance fields
.field private mIndex:I


# direct methods
.method static constructor <clinit>()V
    .locals 5

    new-instance v0, Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v1, 0x0

    const-string v2, "INDEX_0"

    invoke-direct {v0, v2, v1, v1}, Lcom/android/camera/dualvideo/util/SelectIndex;-><init>(Ljava/lang/String;II)V

    sput-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    new-instance v0, Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v2, 0x1

    const-string v3, "INDEX_1"

    invoke-direct {v0, v3, v2, v2}, Lcom/android/camera/dualvideo/util/SelectIndex;-><init>(Ljava/lang/String;II)V

    sput-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    new-instance v0, Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v3, 0x2

    const-string v4, "INDEX_2"

    invoke-direct {v0, v4, v3, v3}, Lcom/android/camera/dualvideo/util/SelectIndex;-><init>(Ljava/lang/String;II)V

    sput-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    const/4 v0, 0x3

    new-array v0, v0, [Lcom/android/camera/dualvideo/util/SelectIndex;

    sget-object v4, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_0:Lcom/android/camera/dualvideo/util/SelectIndex;

    aput-object v4, v0, v1

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_1:Lcom/android/camera/dualvideo/util/SelectIndex;

    aput-object v1, v0, v2

    sget-object v1, Lcom/android/camera/dualvideo/util/SelectIndex;->INDEX_2:Lcom/android/camera/dualvideo/util/SelectIndex;

    aput-object v1, v0, v3

    sput-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->$VALUES:[Lcom/android/camera/dualvideo/util/SelectIndex;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;II)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    iput p3, p0, Lcom/android/camera/dualvideo/util/SelectIndex;->mIndex:I

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/android/camera/dualvideo/util/SelectIndex;
    .locals 1

    const-class v0, Lcom/android/camera/dualvideo/util/SelectIndex;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/util/SelectIndex;

    return-object p0
.end method

.method public static values()[Lcom/android/camera/dualvideo/util/SelectIndex;
    .locals 1

    sget-object v0, Lcom/android/camera/dualvideo/util/SelectIndex;->$VALUES:[Lcom/android/camera/dualvideo/util/SelectIndex;

    invoke-virtual {v0}, [Lcom/android/camera/dualvideo/util/SelectIndex;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/android/camera/dualvideo/util/SelectIndex;

    return-object v0
.end method


# virtual methods
.method public getIndex()I
    .locals 0

    iget p0, p0, Lcom/android/camera/dualvideo/util/SelectIndex;->mIndex:I

    return p0
.end method

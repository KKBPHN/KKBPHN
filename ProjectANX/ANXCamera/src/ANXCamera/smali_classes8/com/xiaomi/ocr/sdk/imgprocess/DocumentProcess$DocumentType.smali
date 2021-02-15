.class public final enum Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

.field public static final enum DEFAULT:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

.field public static final enum ID_CARD:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

.field public static final enum PPT:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;


# direct methods
.method static constructor <clinit>()V
    .locals 5

    new-instance v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    const/4 v1, 0x0

    const-string v2, "DEFAULT"

    invoke-direct {v0, v2, v1}, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->DEFAULT:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    new-instance v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    const/4 v2, 0x1

    const-string v3, "ID_CARD"

    invoke-direct {v0, v3, v2}, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->ID_CARD:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    new-instance v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    const/4 v3, 0x2

    const-string v4, "PPT"

    invoke-direct {v0, v4, v3}, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->PPT:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    const/4 v0, 0x3

    new-array v0, v0, [Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    sget-object v4, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->DEFAULT:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    aput-object v4, v0, v1

    sget-object v1, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->ID_CARD:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    aput-object v1, v0, v2

    sget-object v1, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->PPT:Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    aput-object v1, v0, v3

    sput-object v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->$VALUES:[Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;
    .locals 1

    const-class v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    return-object p0
.end method

.method public static values()[Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;
    .locals 1

    sget-object v0, Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->$VALUES:[Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    invoke-virtual {v0}, [Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/xiaomi/ocr/sdk/imgprocess/DocumentProcess$DocumentType;

    return-object v0
.end method

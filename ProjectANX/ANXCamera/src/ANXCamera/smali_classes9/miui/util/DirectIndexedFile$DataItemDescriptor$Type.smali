.class final enum Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum BYTE:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum BYTE_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum INTEGER:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum INTEGER_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum LONG:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum LONG_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum SHORT:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum SHORT_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

.field public static final enum STRING:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;


# direct methods
.method static constructor <clinit>()V
    .locals 11

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/4 v1, 0x0

    const-string v2, "BYTE"

    invoke-direct {v0, v2, v1}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->BYTE:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/4 v2, 0x1

    const-string v3, "SHORT"

    invoke-direct {v0, v3, v2}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->SHORT:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/4 v3, 0x2

    const-string v4, "INTEGER"

    invoke-direct {v0, v4, v3}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->INTEGER:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/4 v4, 0x3

    const-string v5, "LONG"

    invoke-direct {v0, v5, v4}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->LONG:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/4 v5, 0x4

    const-string v6, "STRING"

    invoke-direct {v0, v6, v5}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->STRING:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/4 v6, 0x5

    const-string v7, "BYTE_ARRAY"

    invoke-direct {v0, v7, v6}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->BYTE_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/4 v7, 0x6

    const-string v8, "SHORT_ARRAY"

    invoke-direct {v0, v8, v7}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->SHORT_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/4 v8, 0x7

    const-string v9, "INTEGER_ARRAY"

    invoke-direct {v0, v9, v8}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->INTEGER_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    new-instance v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/16 v9, 0x8

    const-string v10, "LONG_ARRAY"

    invoke-direct {v0, v10, v9}, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->LONG_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    const/16 v0, 0x9

    new-array v0, v0, [Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    sget-object v10, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->BYTE:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v10, v0, v1

    sget-object v1, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->SHORT:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v1, v0, v2

    sget-object v1, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->INTEGER:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v1, v0, v3

    sget-object v1, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->LONG:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v1, v0, v4

    sget-object v1, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->STRING:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v1, v0, v5

    sget-object v1, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->BYTE_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v1, v0, v6

    sget-object v1, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->SHORT_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v1, v0, v7

    sget-object v1, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->INTEGER_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v1, v0, v8

    sget-object v1, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->LONG_ARRAY:Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    aput-object v1, v0, v9

    sput-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->$VALUES:[Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;
    .locals 1

    const-class v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    return-object p0
.end method

.method public static values()[Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;
    .locals 1

    sget-object v0, Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->$VALUES:[Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    invoke-virtual {v0}, [Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lmiui/util/DirectIndexedFile$DataItemDescriptor$Type;

    return-object v0
.end method

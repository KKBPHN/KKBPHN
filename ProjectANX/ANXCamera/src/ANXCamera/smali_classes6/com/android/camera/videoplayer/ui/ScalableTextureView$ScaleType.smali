.class public final enum Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;
.super Ljava/lang/Enum;
.source ""


# static fields
.field private static final synthetic $VALUES:[Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

.field public static final enum BOTTOM:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

.field public static final enum CENTER_CROP:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

.field public static final enum FILL:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

.field public static final enum TOP:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;


# direct methods
.method static constructor <clinit>()V
    .locals 6

    new-instance v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    const/4 v1, 0x0

    const-string v2, "CENTER_CROP"

    invoke-direct {v0, v2, v1}, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->CENTER_CROP:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    new-instance v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    const/4 v2, 0x1

    const-string v3, "TOP"

    invoke-direct {v0, v3, v2}, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->TOP:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    new-instance v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    const/4 v3, 0x2

    const-string v4, "BOTTOM"

    invoke-direct {v0, v4, v3}, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->BOTTOM:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    new-instance v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    const/4 v4, 0x3

    const-string v5, "FILL"

    invoke-direct {v0, v5, v4}, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->FILL:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    const/4 v0, 0x4

    new-array v0, v0, [Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    sget-object v5, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->CENTER_CROP:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    aput-object v5, v0, v1

    sget-object v1, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->TOP:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    aput-object v1, v0, v2

    sget-object v1, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->BOTTOM:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    aput-object v1, v0, v3

    sget-object v1, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->FILL:Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    aput-object v1, v0, v4

    sput-object v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->$VALUES:[Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0

    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;
    .locals 1

    const-class v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object p0

    check-cast p0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    return-object p0
.end method

.method public static values()[Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;
    .locals 1

    sget-object v0, Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->$VALUES:[Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    invoke-virtual {v0}, [Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/android/camera/videoplayer/ui/ScalableTextureView$ScaleType;

    return-object v0
.end method

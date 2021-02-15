.class public Lcom/airbnb/lottie/model/O00000o0;
.super Ljava/lang/Object;
.source ""


# annotations
.annotation build Landroidx/annotation/RestrictTo;
    value = {
        .enum Landroidx/annotation/RestrictTo$Scope;->LIBRARY:Landroidx/annotation/RestrictTo$Scope;
    }
.end annotation


# instance fields
.field private final O00oO00:Ljava/lang/String;

.field private final ascent:F

.field private final name:Ljava/lang/String;

.field private final style:Ljava/lang/String;


# direct methods
.method public constructor <init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;F)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/model/O00000o0;->O00oO00:Ljava/lang/String;

    iput-object p2, p0, Lcom/airbnb/lottie/model/O00000o0;->name:Ljava/lang/String;

    iput-object p3, p0, Lcom/airbnb/lottie/model/O00000o0;->style:Ljava/lang/String;

    iput p4, p0, Lcom/airbnb/lottie/model/O00000o0;->ascent:F

    return-void
.end method


# virtual methods
.method O00Oo0oo()F
    .locals 0

    iget p0, p0, Lcom/airbnb/lottie/model/O00000o0;->ascent:F

    return p0
.end method

.method public getFamily()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/O00000o0;->O00oO00:Ljava/lang/String;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/O00000o0;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getStyle()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/model/O00000o0;->style:Ljava/lang/String;

    return-object p0
.end method

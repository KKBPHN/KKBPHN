.class public Lcom/airbnb/lottie/O00000o0/O00000o;
.super Ljava/lang/Object;
.source ""


# direct methods
.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Z)Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    if-eqz p2, :cond_0

    invoke-static {}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00o0O0O()F

    move-result p2

    goto :goto_0

    :cond_0
    const/high16 p2, 0x3f800000    # 1.0f

    :goto_0
    sget-object v1, Lcom/airbnb/lottie/O00000o0/O0000Oo0;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O0000Oo0;

    invoke-static {p0, p2, p1, v1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;FLcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;I)Lcom/airbnb/lottie/model/O000000o/O00000o0;
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/model/O000000o/O00000o0;

    new-instance v1, Lcom/airbnb/lottie/O00000o0/O0000Ooo;

    invoke-direct {v1, p2}, Lcom/airbnb/lottie/O00000o0/O0000Ooo;-><init>(I)V

    invoke-static {p0, p1, v1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/model/O000000o/O00000o0;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method private static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;FLcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;
    .locals 0
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    invoke-static {p0, p2, p1, p3}, Lcom/airbnb/lottie/O00000o0/O0000oO;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;FLcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    return-object p0
.end method

.method private static O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;
    .locals 1
    .annotation build Landroidx/annotation/Nullable;
    .end annotation

    const/high16 v0, 0x3f800000    # 1.0f

    invoke-static {p0, p1, v0, p2}, Lcom/airbnb/lottie/O00000o0/O0000oO;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;FLcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    return-object p0
.end method

.method static O00000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000Oo;
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/model/O000000o/O0000Oo;

    sget-object v1, Lcom/airbnb/lottie/O00000o0/O0000OOo;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O0000OOo;

    invoke-static {p0, p1, v1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/model/O000000o/O0000Oo;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method static O00000o0(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O000000o;
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/model/O000000o/O000000o;

    sget-object v1, Lcom/airbnb/lottie/O00000o0/O00000oo;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O00000oo;

    invoke-static {p0, p1, v1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/model/O000000o/O000000o;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method public static O00000oO(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000Oo;
    .locals 1

    const/4 v0, 0x1

    invoke-static {p0, p1, v0}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Z)Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object p0

    return-object p0
.end method

.method static O00000oo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000o;
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/model/O000000o/O00000o;

    sget-object v1, Lcom/airbnb/lottie/O00000o0/O0000o0O;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O0000o0O;

    invoke-static {p0, p1, v1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/model/O000000o/O00000o;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method static O0000O0o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O00000oo;
    .locals 3

    new-instance v0, Lcom/airbnb/lottie/model/O000000o/O00000oo;

    invoke-static {}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00o0O0O()F

    move-result v1

    sget-object v2, Lcom/airbnb/lottie/O00000o0/O00oOooo;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O00oOooo;

    invoke-static {p0, v1, p1, v2}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;FLcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/model/O000000o/O00000oo;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method static O0000OOo(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000O0o;
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/model/O000000o/O0000O0o;

    sget-object v1, Lcom/airbnb/lottie/O00000o0/O00oOoOo;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O00oOoOo;

    invoke-static {p0, p1, v1}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/model/O000000o/O0000O0o;-><init>(Ljava/util/List;)V

    return-object v0
.end method

.method static O0000Oo0(Lcom/airbnb/lottie/parser/moshi/O00000Oo;Lcom/airbnb/lottie/O0000o0O;)Lcom/airbnb/lottie/model/O000000o/O0000OOo;
    .locals 3

    new-instance v0, Lcom/airbnb/lottie/model/O000000o/O0000OOo;

    invoke-static {}, Lcom/airbnb/lottie/O00000o/O0000OOo;->O00o0O0O()F

    move-result v1

    sget-object v2, Lcom/airbnb/lottie/O00000o0/O000O0o0;->INSTANCE:Lcom/airbnb/lottie/O00000o0/O000O0o0;

    invoke-static {p0, v1, p1, v2}, Lcom/airbnb/lottie/O00000o0/O00000o;->O000000o(Lcom/airbnb/lottie/parser/moshi/O00000Oo;FLcom/airbnb/lottie/O0000o0O;Lcom/airbnb/lottie/O00000o0/O000OO;)Ljava/util/List;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/model/O000000o/O0000OOo;-><init>(Ljava/util/List;)V

    return-object v0
.end method

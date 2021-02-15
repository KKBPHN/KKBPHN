.class public Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O000000o/O00000oO;
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;


# instance fields
.field private final O00OooO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final O00OooOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final O00Oooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

.field private final hidden:Z

.field private final listeners:Ljava/util/List;

.field private final name:Ljava/lang/String;

.field private final type:Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;


# direct methods
.method public constructor <init>(Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/model/content/O0000o;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->listeners:Ljava/util/List;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/content/O0000o;->getName()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->name:Ljava/lang/String;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/content/O0000o;->isHidden()Z

    move-result v0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->hidden:Z

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/content/O0000o;->getType()Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->type:Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/content/O0000o;->getStart()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00OooO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/content/O0000o;->getEnd()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object v0

    invoke-virtual {v0}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00OooOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/content/O0000o;->getOffset()Lcom/airbnb/lottie/model/O000000o/O00000Oo;

    move-result-object p2

    invoke-virtual {p2}, Lcom/airbnb/lottie/model/O000000o/O00000Oo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object p2

    iput-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00Oooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00OooO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p2}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00OooOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p2}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p2, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00Oooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p2}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00OooO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00OooOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    iget-object p1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00Oooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    invoke-virtual {p1, p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;->O00000Oo(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V

    return-void
.end method


# virtual methods
.method O000000o(Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;)V
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->listeners:Ljava/util/List;

    invoke-interface {p0, p1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public O000000o(Ljava/util/List;Ljava/util/List;)V
    .locals 0

    return-void
.end method

.method public O00000oO()V
    .locals 2

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->listeners:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->listeners:Ljava/util/List;

    invoke-interface {v1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;

    invoke-interface {v1}, Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;->O00000oO()V

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method

.method public getEnd()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00OooOo:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    return-object p0
.end method

.method public getName()Ljava/lang/String;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->name:Ljava/lang/String;

    return-object p0
.end method

.method public getOffset()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00Oooo0:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    return-object p0
.end method

.method public getStart()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->O00OooO:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    return-object p0
.end method

.method getType()Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->type:Lcom/airbnb/lottie/model/content/ShapeTrimPath$Type;

    return-object p0
.end method

.method public isHidden()Z
    .locals 0

    iget-boolean p0, p0, Lcom/airbnb/lottie/O000000o/O000000o/O0000ooO;->hidden:Z

    return p0
.end method

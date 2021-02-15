.class public Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;
.super Ljava/lang/Object;
.source ""


# instance fields
.field private final O00o0O:Ljava/util/List;

.field private final O00o0O0o:Ljava/util/List;

.field private final O00o0OO0:Ljava/util/List;


# direct methods
.method public constructor <init>(Ljava/util/List;)V
    .locals 3

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00o0OO0:Ljava/util/List;

    new-instance v0, Ljava/util/ArrayList;

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v1

    invoke-direct {v0, v1}, Ljava/util/ArrayList;-><init>(I)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00o0O0o:Ljava/util/List;

    new-instance v0, Ljava/util/ArrayList;

    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v1

    invoke-direct {v0, v1}, Ljava/util/ArrayList;-><init>(I)V

    iput-object v0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00o0O:Ljava/util/List;

    const/4 v0, 0x0

    :goto_0
    invoke-interface {p1}, Ljava/util/List;->size()I

    move-result v1

    if-ge v0, v1, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00o0O0o:Ljava/util/List;

    invoke-interface {p1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/airbnb/lottie/model/content/O00000oo;

    invoke-virtual {v2}, Lcom/airbnb/lottie/model/content/O00000oo;->O00Ooo0o()Lcom/airbnb/lottie/model/O000000o/O0000OOo;

    move-result-object v2

    invoke-virtual {v2}, Lcom/airbnb/lottie/model/O000000o/O0000OOo;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v2

    invoke-interface {v1, v2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    invoke-interface {p1, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/airbnb/lottie/model/content/O00000oo;

    invoke-virtual {v1}, Lcom/airbnb/lottie/model/content/O00000oo;->getOpacity()Lcom/airbnb/lottie/model/O000000o/O00000o;

    move-result-object v1

    iget-object v2, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00o0O:Ljava/util/List;

    invoke-virtual {v1}, Lcom/airbnb/lottie/model/O000000o/O00000o;->O00000o()Lcom/airbnb/lottie/O000000o/O00000Oo/O0000O0o;

    move-result-object v1

    invoke-interface {v2, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_0
    return-void
.end method


# virtual methods
.method public O00Oo0()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00o0OO0:Ljava/util/List;

    return-object p0
.end method

.method public O00Oo00o()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00o0O0o:Ljava/util/List;

    return-object p0
.end method

.method public O00Oo0OO()Ljava/util/List;
    .locals 0

    iget-object p0, p0, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000o00;->O00o0O:Ljava/util/List;

    return-object p0
.end method

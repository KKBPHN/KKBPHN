.class public Lcom/android/camera/data/observeable/FilmViewModel;
.super Lcom/android/camera/data/observeable/VMBase;
.source ""


# instance fields
.field private mFilmList:Lcom/android/camera/fragment/film/FilmList;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Lcom/android/camera/data/observeable/VMBase;-><init>()V

    return-void
.end method


# virtual methods
.method public synthetic O00000Oo(Lcom/android/camera/fragment/film/FilmList;)Lcom/android/camera/fragment/film/FilmList;
    .locals 0

    iput-object p1, p0, Lcom/android/camera/data/observeable/FilmViewModel;->mFilmList:Lcom/android/camera/fragment/film/FilmList;

    iget-object p0, p0, Lcom/android/camera/data/observeable/FilmViewModel;->mFilmList:Lcom/android/camera/fragment/film/FilmList;

    return-object p0
.end method

.method protected achieveEndOfCycle()Z
    .locals 0

    const/4 p0, 0x0

    return p0
.end method

.method public getFilmList()Lcom/android/camera/fragment/film/FilmList;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/data/observeable/FilmViewModel;->mFilmList:Lcom/android/camera/fragment/film/FilmList;

    return-object p0
.end method

.method public getFilmListObservable()Lio/reactivex/Observable;
    .locals 6

    new-instance v0, Ljava/util/LinkedHashMap;

    invoke-direct {v0}, Ljava/util/LinkedHashMap;-><init>()V

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v1

    invoke-virtual {v1}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0oooO()Z

    move-result v1

    const-string v2, "info.json"

    if-eqz v1, :cond_0

    const-string v1, "dollyzoom/"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v5, Lcom/android/camera/module/impl/component/FilmDreamImpl;->TEMPLATE_PATH:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v3, v1}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_0
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v1

    invoke-virtual {v1}, LO00000Oo/O00000oO/O000000o/O00000o;->OOooOo()Z

    move-result v1

    if-eqz v1, :cond_1

    const-string v1, "slowshutter/"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v5, Lcom/android/camera/module/impl/component/FilmDreamImpl;->TEMPLATE_PATH:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v3, v1}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_1
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v1

    invoke-virtual {v1}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOOoO0()Z

    move-result v1

    if-eqz v1, :cond_2

    const-string v1, "timestop/"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v5, Lcom/android/camera/module/impl/component/FilmDreamImpl;->TEMPLATE_PATH:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v3, v1}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_2
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v1

    invoke-virtual {v1}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO0oOO()Z

    move-result v1

    if-eqz v1, :cond_3

    const-string v1, "longexposuredelay/"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v5, Lcom/android/camera/module/impl/component/FilmDreamImpl;->TEMPLATE_PATH:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v3, v1}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_3
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v1

    invoke-virtual {v1}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOO0o0()Z

    move-result v1

    if-eqz v1, :cond_4

    const-string v1, "paralleldream/"

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    sget-object v4, Lcom/android/camera/module/impl/component/FilmDreamImpl;->TEMPLATE_PATH:Ljava/lang/String;

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v2, v1}, Ljava/util/LinkedHashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    :cond_4
    new-instance v1, Lcom/android/camera/resource/SimpleMultiJsonInfoRequest;

    const-class v2, Lcom/android/camera/fragment/film/FilmList;

    invoke-direct {v1, v0, v2}, Lcom/android/camera/resource/SimpleMultiJsonInfoRequest;-><init>(Ljava/util/LinkedHashMap;Ljava/lang/Class;)V

    const-class v0, Lcom/android/camera/fragment/film/FilmList;

    invoke-virtual {v1, v0}, Lcom/android/camera/resource/BaseObservableRequest;->startObservable(Ljava/lang/Class;)Lio/reactivex/Observable;

    move-result-object v0

    new-instance v1, Lcom/android/camera/data/observeable/O000000o;

    invoke-direct {v1, p0}, Lcom/android/camera/data/observeable/O000000o;-><init>(Lcom/android/camera/data/observeable/FilmViewModel;)V

    invoke-virtual {v0, v1}, Lio/reactivex/Observable;->map(Lio/reactivex/functions/Function;)Lio/reactivex/Observable;

    move-result-object p0

    return-object p0
.end method

.method protected rollbackData()V
    .locals 1

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/android/camera/data/observeable/FilmViewModel;->mFilmList:Lcom/android/camera/fragment/film/FilmList;

    return-void
.end method

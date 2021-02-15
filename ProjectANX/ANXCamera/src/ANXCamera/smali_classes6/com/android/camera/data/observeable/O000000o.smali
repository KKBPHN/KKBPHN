.class public final synthetic Lcom/android/camera/data/observeable/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/functions/Function;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/data/observeable/FilmViewModel;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/data/observeable/FilmViewModel;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/data/observeable/O000000o;->O0OOoO0:Lcom/android/camera/data/observeable/FilmViewModel;

    return-void
.end method


# virtual methods
.method public final apply(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0

    iget-object p0, p0, Lcom/android/camera/data/observeable/O000000o;->O0OOoO0:Lcom/android/camera/data/observeable/FilmViewModel;

    check-cast p1, Lcom/android/camera/fragment/film/FilmList;

    invoke-virtual {p0, p1}, Lcom/android/camera/data/observeable/FilmViewModel;->O00000Oo(Lcom/android/camera/fragment/film/FilmList;)Lcom/android/camera/fragment/film/FilmList;

    move-result-object p0

    return-object p0
.end method

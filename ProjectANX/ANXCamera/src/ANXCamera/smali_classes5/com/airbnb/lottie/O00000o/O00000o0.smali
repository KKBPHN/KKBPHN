.class public Lcom/airbnb/lottie/O00000o/O00000o0;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000OoOO;


# static fields
.field private static final O0OO0oO:Ljava/util/Set;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    new-instance v0, Ljava/util/HashSet;

    invoke-direct {v0}, Ljava/util/HashSet;-><init>()V

    sput-object v0, Lcom/airbnb/lottie/O00000o/O00000o0;->O0OO0oO:Ljava/util/Set;

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public O000000o(Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 0

    sget-boolean p0, Lcom/airbnb/lottie/O00000oO;->DBG:Z

    if-eqz p0, :cond_0

    const-string p0, "LOTTIE"

    invoke-static {p0, p1, p2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    :cond_0
    return-void
.end method

.method public O00000Oo(Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 0

    sget-object p0, Lcom/airbnb/lottie/O00000o/O00000o0;->O0OO0oO:Ljava/util/Set;

    invoke-interface {p0, p1}, Ljava/util/Set;->contains(Ljava/lang/Object;)Z

    move-result p0

    if-eqz p0, :cond_0

    return-void

    :cond_0
    const-string p0, "LOTTIE"

    invoke-static {p0, p1, p2}, Landroid/util/Log;->w(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    sget-object p0, Lcom/airbnb/lottie/O00000o/O00000o0;->O0OO0oO:Ljava/util/Set;

    invoke-interface {p0, p1}, Ljava/util/Set;->add(Ljava/lang/Object;)Z

    return-void
.end method

.method public O00000o0(Ljava/lang/String;Ljava/lang/Throwable;)V
    .locals 0

    sget-boolean p0, Lcom/airbnb/lottie/O00000oO;->DBG:Z

    if-eqz p0, :cond_0

    const-string p0, "LOTTIE"

    invoke-static {p0, p1, p2}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I

    :cond_0
    return-void
.end method

.method public debug(Ljava/lang/String;)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, v0}, Lcom/airbnb/lottie/O00000o/O00000o0;->O00000o0(Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

.method public warning(Ljava/lang/String;)V
    .locals 1

    const/4 v0, 0x0

    invoke-virtual {p0, p1, v0}, Lcom/airbnb/lottie/O00000o/O00000o0;->O00000Oo(Ljava/lang/String;Ljava/lang/Throwable;)V

    return-void
.end method

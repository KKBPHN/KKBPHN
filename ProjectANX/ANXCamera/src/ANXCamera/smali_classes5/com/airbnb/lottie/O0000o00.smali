.class final Lcom/airbnb/lottie/O0000o00;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000OoO;
.implements Lcom/airbnb/lottie/O00000Oo;


# instance fields
.field private cancelled:Z

.field private final listener:Lcom/airbnb/lottie/O000o00;


# direct methods
.method private constructor <init>(Lcom/airbnb/lottie/O000o00;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/airbnb/lottie/O0000o00;->cancelled:Z

    iput-object p1, p0, Lcom/airbnb/lottie/O0000o00;->listener:Lcom/airbnb/lottie/O000o00;

    return-void
.end method

.method synthetic constructor <init>(Lcom/airbnb/lottie/O000o00;Lcom/airbnb/lottie/O0000Ooo;)V
    .locals 0

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/O0000o00;-><init>(Lcom/airbnb/lottie/O000o00;)V

    return-void
.end method


# virtual methods
.method public bridge synthetic O000000o(Ljava/lang/Object;)V
    .locals 0

    check-cast p1, Lcom/airbnb/lottie/O0000o0O;

    invoke-virtual {p0, p1}, Lcom/airbnb/lottie/O0000o00;->O00000o(Lcom/airbnb/lottie/O0000o0O;)V

    return-void
.end method

.method public O00000o(Lcom/airbnb/lottie/O0000o0O;)V
    .locals 1

    iget-boolean v0, p0, Lcom/airbnb/lottie/O0000o00;->cancelled:Z

    if-eqz v0, :cond_0

    return-void

    :cond_0
    iget-object p0, p0, Lcom/airbnb/lottie/O0000o00;->listener:Lcom/airbnb/lottie/O000o00;

    invoke-interface {p0, p1}, Lcom/airbnb/lottie/O000o00;->O000000o(Lcom/airbnb/lottie/O0000o0O;)V

    return-void
.end method

.method public cancel()V
    .locals 1

    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/airbnb/lottie/O0000o00;->cancelled:Z

    return-void
.end method

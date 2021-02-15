.class Lcom/airbnb/lottie/model/layer/O000000o;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Lcom/airbnb/lottie/O000000o/O00000Oo/O00000Oo;


# instance fields
.field final synthetic O00oOo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;

.field final synthetic this$0:Lcom/airbnb/lottie/model/layer/O00000o0;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/model/layer/O00000o0;Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O000000o;->this$0:Lcom/airbnb/lottie/model/layer/O00000o0;

    iput-object p2, p0, Lcom/airbnb/lottie/model/layer/O000000o;->O00oOo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public O00000oO()V
    .locals 2

    iget-object v0, p0, Lcom/airbnb/lottie/model/layer/O000000o;->this$0:Lcom/airbnb/lottie/model/layer/O00000o0;

    iget-object p0, p0, Lcom/airbnb/lottie/model/layer/O000000o;->O00oOo0O:Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;

    invoke-virtual {p0}, Lcom/airbnb/lottie/O000000o/O00000Oo/O0000Oo0;->getFloatValue()F

    move-result p0

    const/high16 v1, 0x3f800000    # 1.0f

    cmpl-float p0, p0, v1

    if-nez p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    invoke-static {v0, p0}, Lcom/airbnb/lottie/model/layer/O00000o0;->O000000o(Lcom/airbnb/lottie/model/layer/O00000o0;Z)V

    return-void
.end method

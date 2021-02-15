.class Lcom/airbnb/lottie/parser/moshi/O0000Oo;
.super Lcom/airbnb/lottie/parser/moshi/O0000Ooo;
.source ""


# instance fields
.field final synthetic this$1:Lcom/airbnb/lottie/parser/moshi/O0000OoO;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/parser/moshi/O0000OoO;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/parser/moshi/O0000Oo;->this$1:Lcom/airbnb/lottie/parser/moshi/O0000OoO;

    iget-object p1, p1, Lcom/airbnb/lottie/parser/moshi/O0000OoO;->this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;-><init>(Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;)V

    return-void
.end method


# virtual methods
.method public next()Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->nextNode()Lcom/airbnb/lottie/parser/moshi/O0000o00;

    move-result-object p0

    iget-object p0, p0, Lcom/airbnb/lottie/parser/moshi/O0000o00;->key:Ljava/lang/Object;

    return-object p0
.end method

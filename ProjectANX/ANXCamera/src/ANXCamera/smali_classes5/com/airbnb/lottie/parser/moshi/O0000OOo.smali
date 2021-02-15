.class Lcom/airbnb/lottie/parser/moshi/O0000OOo;
.super Lcom/airbnb/lottie/parser/moshi/O0000Ooo;
.source ""


# instance fields
.field final synthetic this$1:Lcom/airbnb/lottie/parser/moshi/O0000Oo0;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/parser/moshi/O0000Oo0;)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/parser/moshi/O0000OOo;->this$1:Lcom/airbnb/lottie/parser/moshi/O0000Oo0;

    iget-object p1, p1, Lcom/airbnb/lottie/parser/moshi/O0000Oo0;->this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;

    invoke-direct {p0, p1}, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;-><init>(Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;)V

    return-void
.end method


# virtual methods
.method public bridge synthetic next()Ljava/lang/Object;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O0000OOo;->next()Ljava/util/Map$Entry;

    move-result-object p0

    return-object p0
.end method

.method public next()Ljava/util/Map$Entry;
    .locals 0

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->nextNode()Lcom/airbnb/lottie/parser/moshi/O0000o00;

    move-result-object p0

    return-object p0
.end method

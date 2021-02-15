.class abstract Lcom/airbnb/lottie/parser/moshi/O0000Ooo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/util/Iterator;


# instance fields
.field expectedModCount:I

.field lastReturned:Lcom/airbnb/lottie/parser/moshi/O0000o00;

.field next:Lcom/airbnb/lottie/parser/moshi/O0000o00;

.field final synthetic this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;)V
    .locals 1

    iput-object p1, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iget-object p1, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;

    iget-object v0, p1, Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;->header:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iget-object v0, v0, Lcom/airbnb/lottie/parser/moshi/O0000o00;->next:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iput-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->next:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->lastReturned:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iget p1, p1, Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;->modCount:I

    iput p1, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->expectedModCount:I

    return-void
.end method


# virtual methods
.method public final hasNext()Z
    .locals 1

    iget-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->next:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iget-object p0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;

    iget-object p0, p0, Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;->header:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    if-eq v0, p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method final nextNode()Lcom/airbnb/lottie/parser/moshi/O0000o00;
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->next:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iget-object v1, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;

    iget-object v2, v1, Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;->header:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    if-eq v0, v2, :cond_1

    iget v1, v1, Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;->modCount:I

    iget v2, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->expectedModCount:I

    if-ne v1, v2, :cond_0

    iget-object v1, v0, Lcom/airbnb/lottie/parser/moshi/O0000o00;->next:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iput-object v1, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->next:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iput-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->lastReturned:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    return-object v0

    :cond_0
    new-instance p0, Ljava/util/ConcurrentModificationException;

    invoke-direct {p0}, Ljava/util/ConcurrentModificationException;-><init>()V

    throw p0

    :cond_1
    new-instance p0, Ljava/util/NoSuchElementException;

    invoke-direct {p0}, Ljava/util/NoSuchElementException;-><init>()V

    throw p0
.end method

.method public final remove()V
    .locals 3

    iget-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->lastReturned:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    if-eqz v0, :cond_0

    iget-object v1, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;

    const/4 v2, 0x1

    invoke-virtual {v1, v0, v2}, Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;->O000000o(Lcom/airbnb/lottie/parser/moshi/O0000o00;Z)V

    const/4 v0, 0x0

    iput-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->lastReturned:Lcom/airbnb/lottie/parser/moshi/O0000o00;

    iget-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->this$0:Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;

    iget v0, v0, Lcom/airbnb/lottie/parser/moshi/LinkedHashTreeMap;->modCount:I

    iput v0, p0, Lcom/airbnb/lottie/parser/moshi/O0000Ooo;->expectedModCount:I

    return-void

    :cond_0
    new-instance p0, Ljava/lang/IllegalStateException;

    invoke-direct {p0}, Ljava/lang/IllegalStateException;-><init>()V

    throw p0
.end method

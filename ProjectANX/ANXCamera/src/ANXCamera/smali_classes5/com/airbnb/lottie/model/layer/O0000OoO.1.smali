.class Lcom/airbnb/lottie/model/layer/O0000OoO;
.super Landroid/graphics/Paint;
.source ""


# instance fields
.field final synthetic this$0:Lcom/airbnb/lottie/model/layer/O0000o0;


# direct methods
.method constructor <init>(Lcom/airbnb/lottie/model/layer/O0000o0;I)V
    .locals 0

    iput-object p1, p0, Lcom/airbnb/lottie/model/layer/O0000OoO;->this$0:Lcom/airbnb/lottie/model/layer/O0000o0;

    invoke-direct {p0, p2}, Landroid/graphics/Paint;-><init>(I)V

    sget-object p1, Landroid/graphics/Paint$Style;->FILL:Landroid/graphics/Paint$Style;

    invoke-virtual {p0, p1}, Landroid/graphics/Paint;->setStyle(Landroid/graphics/Paint$Style;)V

    return-void
.end method

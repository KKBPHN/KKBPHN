.class public final synthetic Lcom/android/zxing/O00000Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/functions/Function;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/zxing/DocumentDecoder;


# direct methods
.method public synthetic constructor <init>(Lcom/android/zxing/DocumentDecoder;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/zxing/O00000Oo;->O0OOoO0:Lcom/android/zxing/DocumentDecoder;

    return-void
.end method


# virtual methods
.method public final apply(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0

    iget-object p0, p0, Lcom/android/zxing/O00000Oo;->O0OOoO0:Lcom/android/zxing/DocumentDecoder;

    check-cast p1, Lcom/android/zxing/PreviewImage;

    invoke-virtual {p0, p1}, Lcom/android/zxing/DocumentDecoder;->O000000o(Lcom/android/zxing/PreviewImage;)Landroid/util/Pair;

    move-result-object p0

    return-object p0
.end method

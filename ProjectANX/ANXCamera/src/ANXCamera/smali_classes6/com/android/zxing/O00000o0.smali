.class public final synthetic Lcom/android/zxing/O00000o0;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/functions/Function;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/zxing/HandGestureDecoder;


# direct methods
.method public synthetic constructor <init>(Lcom/android/zxing/HandGestureDecoder;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/zxing/O00000o0;->O0OOoO0:Lcom/android/zxing/HandGestureDecoder;

    return-void
.end method


# virtual methods
.method public final apply(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0

    iget-object p0, p0, Lcom/android/zxing/O00000o0;->O0OOoO0:Lcom/android/zxing/HandGestureDecoder;

    check-cast p1, Lcom/android/zxing/PreviewImage;

    invoke-virtual {p0, p1}, Lcom/android/zxing/HandGestureDecoder;->O00000Oo(Lcom/android/zxing/PreviewImage;)Ljava/lang/Integer;

    move-result-object p0

    return-object p0
.end method

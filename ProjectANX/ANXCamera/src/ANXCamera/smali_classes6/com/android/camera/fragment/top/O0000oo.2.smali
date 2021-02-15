.class public final synthetic Lcom/android/camera/fragment/top/O0000oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/util/function/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Ljava/util/List;


# direct methods
.method public synthetic constructor <init>(Ljava/util/List;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/top/O0000oo;->O0OOoO0:Ljava/util/List;

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/fragment/top/O0000oo;->O0OOoO0:Ljava/util/List;

    check-cast p1, Landroid/widget/ImageView;

    invoke-static {p0, p1}, Lcom/android/camera/fragment/top/FragmentTopConfig;->O000000o(Ljava/util/List;Landroid/widget/ImageView;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/preferences/O000000o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Landroid/media/MediaPlayer$OnInfoListener;


# instance fields
.field private final synthetic O0OOoO0:Landroid/widget/VideoView;

.field private final synthetic O0OOoOO:Landroid/view/View;


# direct methods
.method public synthetic constructor <init>(Landroid/widget/VideoView;Landroid/view/View;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/preferences/O000000o;->O0OOoO0:Landroid/widget/VideoView;

    iput-object p2, p0, Lcom/android/camera/preferences/O000000o;->O0OOoOO:Landroid/view/View;

    return-void
.end method


# virtual methods
.method public final onInfo(Landroid/media/MediaPlayer;II)Z
    .locals 1

    iget-object v0, p0, Lcom/android/camera/preferences/O000000o;->O0OOoO0:Landroid/widget/VideoView;

    iget-object p0, p0, Lcom/android/camera/preferences/O000000o;->O0OOoOO:Landroid/view/View;

    invoke-static {v0, p0, p1, p2, p3}, Lcom/android/camera/preferences/EffectComparisonPreference;->O000000o(Landroid/widget/VideoView;Landroid/view/View;Landroid/media/MediaPlayer;II)Z

    move-result p0

    return p0
.end method

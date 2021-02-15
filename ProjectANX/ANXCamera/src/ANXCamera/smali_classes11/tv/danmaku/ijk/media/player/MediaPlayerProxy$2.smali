.class Ltv/danmaku/ijk/media/player/MediaPlayerProxy$2;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;


# instance fields
.field final synthetic this$0:Ltv/danmaku/ijk/media/player/MediaPlayerProxy;

.field final synthetic val$finalListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;


# direct methods
.method constructor <init>(Ltv/danmaku/ijk/media/player/MediaPlayerProxy;Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;)V
    .locals 0

    iput-object p1, p0, Ltv/danmaku/ijk/media/player/MediaPlayerProxy$2;->this$0:Ltv/danmaku/ijk/media/player/MediaPlayerProxy;

    iput-object p2, p0, Ltv/danmaku/ijk/media/player/MediaPlayerProxy$2;->val$finalListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onCompletion(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V
    .locals 0

    iget-object p1, p0, Ltv/danmaku/ijk/media/player/MediaPlayerProxy$2;->val$finalListener:Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;

    iget-object p0, p0, Ltv/danmaku/ijk/media/player/MediaPlayerProxy$2;->this$0:Ltv/danmaku/ijk/media/player/MediaPlayerProxy;

    invoke-interface {p1, p0}, Ltv/danmaku/ijk/media/player/IMediaPlayer$OnCompletionListener;->onCompletion(Ltv/danmaku/ijk/media/player/IMediaPlayer;)V

    return-void
.end method

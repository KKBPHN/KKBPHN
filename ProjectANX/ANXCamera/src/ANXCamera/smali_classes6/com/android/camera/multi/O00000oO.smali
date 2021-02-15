.class public final synthetic Lcom/android/camera/multi/O00000oO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lio/reactivex/functions/Consumer;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/multi/SampleDownloader;

.field private final synthetic O0OOoOO:Lcom/iqiyi/android/qigsaw/core/splitdownload/DownloadCallback;

.field private final synthetic O0OOoOo:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/multi/SampleDownloader;Lcom/iqiyi/android/qigsaw/core/splitdownload/DownloadCallback;I)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/multi/O00000oO;->O0OOoO0:Lcom/android/camera/multi/SampleDownloader;

    iput-object p2, p0, Lcom/android/camera/multi/O00000oO;->O0OOoOO:Lcom/iqiyi/android/qigsaw/core/splitdownload/DownloadCallback;

    iput p3, p0, Lcom/android/camera/multi/O00000oO;->O0OOoOo:I

    return-void
.end method


# virtual methods
.method public final accept(Ljava/lang/Object;)V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/multi/O00000oO;->O0OOoO0:Lcom/android/camera/multi/SampleDownloader;

    iget-object v1, p0, Lcom/android/camera/multi/O00000oO;->O0OOoOO:Lcom/iqiyi/android/qigsaw/core/splitdownload/DownloadCallback;

    iget p0, p0, Lcom/android/camera/multi/O00000oO;->O0OOoOo:I

    check-cast p1, Ljava/lang/Throwable;

    invoke-virtual {v0, v1, p0, p1}, Lcom/android/camera/multi/SampleDownloader;->O000000o(Lcom/iqiyi/android/qigsaw/core/splitdownload/DownloadCallback;ILjava/lang/Throwable;)V

    return-void
.end method

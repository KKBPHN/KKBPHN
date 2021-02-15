.class public Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static final SUCCESS:I = 0x1

.field private static final TAG:Ljava/lang/String; = "MultiRecorderManager"


# instance fields
.field private final mImageSaver:Lcom/android/camera/storage/ImageSaver;

.field private mIsRecording:Z

.field private final mLock:Ljava/lang/Object;

.field private final mRecorderList:Ljava/util/ArrayList;

.field public mStatPausedTimes:I

.field public mStatResumeTimes:I


# direct methods
.method public constructor <init>(Lcom/android/camera/storage/ImageSaver;)V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/lang/Object;

    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    iput-object v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mLock:Ljava/lang/Object;

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    const/4 v0, 0x0

    iput v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mStatPausedTimes:I

    iput v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mStatResumeTimes:I

    iput-object p1, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mImageSaver:Lcom/android/camera/storage/ImageSaver;

    return-void
.end method

.method static synthetic O000000o([Ljava/lang/Object;)Ljava/lang/Boolean;
    .locals 1

    invoke-static {p0}, Ljava/util/Arrays;->stream([Ljava/lang/Object;)Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/recorder/O00000oO;->INSTANCE:Lcom/android/camera/dualvideo/recorder/O00000oO;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->allMatch(Ljava/util/function/Predicate;)Z

    move-result p0

    invoke-static {p0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object p0

    return-object p0
.end method

.method static synthetic O000000o(Landroid/util/SparseArray;Lcom/android/camera/dualvideo/recorder/MiRecorder;)V
    .locals 1

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/recorder/MiRecorder;->getId()I

    move-result v0

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/recorder/MiRecorder;->getSurface()Landroid/view/Surface;

    move-result-object p1

    invoke-virtual {p0, v0, p1}, Landroid/util/SparseArray;->put(ILjava/lang/Object;)V

    return-void
.end method

.method static synthetic O00000oo(Ljava/lang/Object;)Z
    .locals 1

    check-cast p0, Ljava/lang/Integer;

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    const/4 v0, 0x1

    if-ne p0, v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method


# virtual methods
.method public synthetic O000000o(Lcom/android/camera/dualvideo/recorder/MiRecorder;)Lio/reactivex/Observable;
    .locals 1

    new-instance v0, Lcom/android/camera/dualvideo/recorder/O0000O0o;

    invoke-direct {v0, p0, p1}, Lcom/android/camera/dualvideo/recorder/O0000O0o;-><init>(Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;Lcom/android/camera/dualvideo/recorder/MiRecorder;)V

    invoke-static {v0}, Lio/reactivex/Observable;->create(Lio/reactivex/ObservableOnSubscribe;)Lio/reactivex/Observable;

    move-result-object p0

    invoke-static {}, Lio/reactivex/schedulers/Schedulers;->computation()Lio/reactivex/Scheduler;

    move-result-object p1

    invoke-virtual {p0, p1}, Lio/reactivex/Observable;->subscribeOn(Lio/reactivex/Scheduler;)Lio/reactivex/Observable;

    move-result-object p0

    return-object p0
.end method

.method public synthetic O000000o(Lcom/android/camera/dualvideo/recorder/MiRecorder;Lio/reactivex/ObservableEmitter;)V
    .locals 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "stopRecorder: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/recorder/MiRecorder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const-string v1, "MultiRecorderManager"

    invoke-static {v1, v0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/recorder/MiRecorder;->stop()V

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/recorder/MiRecorder;->release()V

    iget-object p0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mImageSaver:Lcom/android/camera/storage/ImageSaver;

    invoke-virtual {p1, p0}, Lcom/android/camera/dualvideo/recorder/MiRecorder;->save(Lcom/android/camera/storage/ImageSaver;)V

    const/4 p0, 0x1

    invoke-static {p0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object p0

    invoke-interface {p2, p0}, Lio/reactivex/Emitter;->onNext(Ljava/lang/Object;)V

    return-void
.end method

.method public synthetic O000000o(Lio/reactivex/SingleEmitter;JLjava/lang/Boolean;)V
    .locals 2

    invoke-virtual {p4}, Ljava/lang/Boolean;->booleanValue()Z

    move-result p4

    if-eqz p4, :cond_0

    if-eqz p1, :cond_0

    invoke-interface {p1}, Lio/reactivex/SingleEmitter;->isDisposed()Z

    move-result p4

    if-nez p4, :cond_0

    const/4 p4, 0x1

    invoke-static {p4}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object p4

    invoke-interface {p1, p4}, Lio/reactivex/SingleEmitter;->onSuccess(Ljava/lang/Object;)V

    :cond_0
    iget-object p0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->clear()V

    new-instance p0, Ljava/lang/StringBuilder;

    invoke-direct {p0}, Ljava/lang/StringBuilder;-><init>()V

    const-string p1, "stopRecorder: time spent(ms): "

    invoke-virtual {p0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    sub-long/2addr v0, p2

    invoke-virtual {p0, v0, v1}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    const-string p1, "MultiRecorderManager"

    invoke-static {p1, p0}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    return-void
.end method

.method public getDuration()J
    .locals 2

    iget-object p0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/dualvideo/recorder/MiRecorder;

    invoke-virtual {p0}, Lcom/android/camera/dualvideo/recorder/MiRecorder;->getDuration()J

    move-result-wide v0

    return-wide v0
.end method

.method public getRecorderSurface()Landroid/util/SparseArray;
    .locals 2

    new-instance v0, Landroid/util/SparseArray;

    invoke-direct {v0}, Landroid/util/SparseArray;-><init>()V

    iget-object p0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    new-instance v1, Lcom/android/camera/dualvideo/recorder/O00000oo;

    invoke-direct {v1, v0}, Lcom/android/camera/dualvideo/recorder/O00000oo;-><init>(Landroid/util/SparseArray;)V

    invoke-virtual {p0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    return-object v0
.end method

.method public isRecording()Z
    .locals 0

    iget-boolean p0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mIsRecording:Z

    return p0
.end method

.method public isRecordingPaused()Z
    .locals 1

    iget-boolean v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mIsRecording:Z

    if-eqz v0, :cond_0

    iget-object p0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    invoke-virtual {p0}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object p0

    sget-object v0, Lcom/android/camera/dualvideo/recorder/O0000Ooo;->INSTANCE:Lcom/android/camera/dualvideo/recorder/O0000Ooo;

    invoke-interface {p0, v0}, Ljava/util/stream/Stream;->anyMatch(Ljava/util/function/Predicate;)Z

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    :goto_0
    return p0
.end method

.method public declared-synchronized pauseRecorder()V
    .locals 2

    monitor-enter p0

    :try_start_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    sget-object v1, Lcom/android/camera/dualvideo/recorder/O0000OoO;->INSTANCE:Lcom/android/camera/dualvideo/recorder/O0000OoO;

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V
    :try_end_0
    .catch Ljava/lang/IllegalArgumentException; {:try_start_0 .. :try_end_0} :catch_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    goto :goto_0

    :catchall_0
    move-exception v0

    goto :goto_1

    :catch_0
    move-exception v0

    :try_start_1
    const-string v1, "MultiRecorderManager"

    invoke-virtual {v0}, Ljava/lang/IllegalArgumentException;->getMessage()Ljava/lang/String;

    move-result-object v0

    invoke-static {v1, v0}, Lcom/android/camera/log/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    :goto_0
    iget v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mStatPausedTimes:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mStatPausedTimes:I
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    monitor-exit p0

    return-void

    :goto_1
    monitor-exit p0

    throw v0
.end method

.method public declared-synchronized release()V
    .locals 2

    monitor-enter p0

    :try_start_0
    const-string v0, "MultiRecorderManager"

    const-string v1, "releaseRecorder"

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->v(Ljava/lang/String;Ljava/lang/String;)I

    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mIsRecording:Z

    iget-object v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    sget-object v1, Lcom/android/camera/dualvideo/recorder/O0000Oo;->INSTANCE:Lcom/android/camera/dualvideo/recorder/O0000Oo;

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    monitor-exit p0

    return-void

    :catchall_0
    move-exception v0

    monitor-exit p0

    throw v0
.end method

.method public declared-synchronized resume()V
    .locals 2

    monitor-enter p0

    :try_start_0
    iget-object v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    sget-object v1, Lcom/android/camera/dualvideo/recorder/O00000Oo;->INSTANCE:Lcom/android/camera/dualvideo/recorder/O00000Oo;

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    iget v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mStatResumeTimes:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mStatResumeTimes:I
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    monitor-exit p0

    return-void

    :catchall_0
    move-exception v0

    monitor-exit p0

    throw v0
.end method

.method public declared-synchronized startRecorder([ILandroid/location/Location;Lcom/android/camera/CameraSize;Lcom/android/camera/dualvideo/recorder/MiRecorder$MiRecorderListener;JI)V
    .locals 16

    move-object/from16 v1, p0

    move-object/from16 v0, p1

    monitor-enter p0

    :try_start_0
    const-string v2, "MultiRecorderManager"

    const-string v3, "startRecorder: "

    invoke-static {v2, v3}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v2

    iget-object v4, v1, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mLock:Ljava/lang/Object;

    monitor-enter v4
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_1

    :try_start_1
    iget-object v5, v1, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    invoke-virtual {v5}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v5

    if-eqz v5, :cond_0

    array-length v5, v0

    const/4 v7, 0x0

    :goto_0
    if-ge v7, v5, :cond_0

    aget v9, v0, v7

    iget-object v15, v1, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    new-instance v14, Lcom/android/camera/dualvideo/recorder/MiRecorder;

    move-object v8, v14

    move-object/from16 v10, p2

    move-wide/from16 v11, p5

    move/from16 v13, p7

    move-object v6, v14

    move-object/from16 v14, p4

    move-object v0, v15

    move-object/from16 v15, p3

    invoke-direct/range {v8 .. v15}, Lcom/android/camera/dualvideo/recorder/MiRecorder;-><init>(ILandroid/location/Location;JILcom/android/camera/dualvideo/recorder/MiRecorder$MiRecorderListener;Lcom/android/camera/CameraSize;)V

    invoke-virtual {v0, v6}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    add-int/lit8 v7, v7, 0x1

    move-object/from16 v0, p1

    goto :goto_0

    :cond_0
    monitor-exit v4
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    const/4 v0, 0x1

    :try_start_2
    iput-boolean v0, v1, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mIsRecording:Z

    iget-object v0, v1, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    sget-object v4, Lcom/android/camera/dualvideo/recorder/O000000o;->INSTANCE:Lcom/android/camera/dualvideo/recorder/O000000o;

    invoke-virtual {v0, v4}, Ljava/util/ArrayList;->forEach(Ljava/util/function/Consumer;)V

    const/4 v0, 0x0

    iput v0, v1, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mStatPausedTimes:I

    iput v0, v1, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mStatResumeTimes:I

    const-string v0, "MultiRecorderManager"

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "startRecorder: time spent(ms): "

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v5

    sub-long/2addr v5, v2

    invoke-virtual {v4, v5, v6}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v0, v2}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I
    :try_end_2
    .catchall {:try_start_2 .. :try_end_2} :catchall_1

    monitor-exit p0

    return-void

    :catchall_0
    move-exception v0

    :try_start_3
    monitor-exit v4
    :try_end_3
    .catchall {:try_start_3 .. :try_end_3} :catchall_0

    :try_start_4
    throw v0
    :try_end_4
    .catchall {:try_start_4 .. :try_end_4} :catchall_1

    :catchall_1
    move-exception v0

    monitor-exit p0

    throw v0
.end method

.method public declared-synchronized stopRecorder(Lio/reactivex/SingleEmitter;)V
    .locals 4
    .annotation build Landroid/annotation/SuppressLint;
        value = {
            "CheckResult"
        }
    .end annotation

    monitor-enter p0

    :try_start_0
    iget-boolean v0, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mIsRecording:Z
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    if-nez v0, :cond_0

    monitor-exit p0

    return-void

    :cond_0
    :try_start_1
    const-string v0, "MultiRecorderManager"

    const-string v1, "stopRecorder: "

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    const/4 v2, 0x0

    iput-boolean v2, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mIsRecording:Z

    iget-object v2, p0, Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;->mRecorderList:Ljava/util/ArrayList;

    invoke-virtual {v2}, Ljava/util/ArrayList;->stream()Ljava/util/stream/Stream;

    move-result-object v2

    new-instance v3, Lcom/android/camera/dualvideo/recorder/O00000o;

    invoke-direct {v3, p0}, Lcom/android/camera/dualvideo/recorder/O00000o;-><init>(Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;)V

    invoke-interface {v2, v3}, Ljava/util/stream/Stream;->map(Ljava/util/function/Function;)Ljava/util/stream/Stream;

    move-result-object v2

    invoke-static {}, Ljava/util/stream/Collectors;->toList()Ljava/util/stream/Collector;

    move-result-object v3

    invoke-interface {v2, v3}, Ljava/util/stream/Stream;->collect(Ljava/util/stream/Collector;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/util/List;

    sget-object v3, Lcom/android/camera/dualvideo/recorder/O00000o0;->INSTANCE:Lcom/android/camera/dualvideo/recorder/O00000o0;

    invoke-static {v2, v3}, Lio/reactivex/Observable;->zip(Ljava/lang/Iterable;Lio/reactivex/functions/Function;)Lio/reactivex/Observable;

    move-result-object v2

    new-instance v3, Lcom/android/camera/dualvideo/recorder/O0000OOo;

    invoke-direct {v3, p0, p1, v0, v1}, Lcom/android/camera/dualvideo/recorder/O0000OOo;-><init>(Lcom/android/camera/dualvideo/recorder/MultiRecorderManager;Lio/reactivex/SingleEmitter;J)V

    invoke-virtual {v2, v3}, Lio/reactivex/Observable;->subscribe(Lio/reactivex/functions/Consumer;)Lio/reactivex/disposables/Disposable;
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_0

    monitor-exit p0

    return-void

    :catchall_0
    move-exception p1

    monitor-exit p0

    throw p1
.end method

.class Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;
.super Landroid/os/Handler;
.source ""


# instance fields
.field final synthetic this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;


# direct methods
.method private constructor <init>(Lcom/xiaomi/mediacodec/MiHWDecoder;Landroid/os/Looper;)V
    .locals 0

    iput-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-direct {p0, p2}, Landroid/os/Handler;-><init>(Landroid/os/Looper;)V

    return-void
.end method

.method synthetic constructor <init>(Lcom/xiaomi/mediacodec/MiHWDecoder;Landroid/os/Looper;Lcom/xiaomi/mediacodec/MiHWDecoder$1;)V
    .locals 0

    invoke-direct {p0, p1, p2}, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;-><init>(Lcom/xiaomi/mediacodec/MiHWDecoder;Landroid/os/Looper;)V

    return-void
.end method


# virtual methods
.method public handleMessage(Landroid/os/Message;)V
    .locals 13

    iget v0, p1, Landroid/os/Message;->what:I

    const/4 v1, 0x1

    const-wide/16 v2, 0x3e8

    const/4 v4, 0x0

    packed-switch v0, :pswitch_data_0

    :pswitch_0
    goto/16 :goto_3

    :pswitch_1
    iget-object p1, p1, Landroid/os/Message;->obj:Ljava/lang/Object;

    check-cast p1, Lcom/xiaomi/mediacodec/MoviePlayer$MediaFrame;

    iget-object v0, p1, Lcom/xiaomi/mediacodec/MoviePlayer$MediaFrame;->buffer:Ljava/nio/ByteBuffer;

    invoke-virtual {v0}, Ljava/nio/ByteBuffer;->remaining()I

    move-result v0

    new-array v8, v0, [B

    iget-object v0, p1, Lcom/xiaomi/mediacodec/MoviePlayer$MediaFrame;->buffer:Ljava/nio/ByteBuffer;

    array-length v1, v8

    invoke-virtual {v0, v8, v4, v1}, Ljava/nio/ByteBuffer;->get([BII)Ljava/nio/ByteBuffer;

    iget-object v5, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v5}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1300(Lcom/xiaomi/mediacodec/MiHWDecoder;)J

    move-result-wide v6

    iget-object p0, p1, Lcom/xiaomi/mediacodec/MoviePlayer$MediaFrame;->info:Landroid/media/MediaCodec$BufferInfo;

    iget v9, p0, Landroid/media/MediaCodec$BufferInfo;->size:I

    iget-wide p0, p0, Landroid/media/MediaCodec$BufferInfo;->presentationTimeUs:J

    div-long v10, p0, v2

    invoke-virtual/range {v5 .. v11}, Lcom/xiaomi/mediacodec/MiHWDecoder;->onAudioFrameJni(J[BIJ)V

    goto/16 :goto_3

    :pswitch_2
    iget-object p1, p1, Landroid/os/Message;->obj:Ljava/lang/Object;

    check-cast p1, Landroid/media/MediaFormat;

    const-string v0, "channel-count"

    invoke-virtual {p1, v0}, Landroid/media/MediaFormat;->getInteger(Ljava/lang/String;)I

    move-result v0

    const-string v1, "sample-rate"

    invoke-virtual {p1, v1}, Landroid/media/MediaFormat;->getInteger(Ljava/lang/String;)I

    move-result p1

    iget-object p0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1300(Lcom/xiaomi/mediacodec/MiHWDecoder;)J

    move-result-wide v1

    invoke-virtual {p0, v1, v2, v0, p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->onAudioFormatJni(JII)V

    goto/16 :goto_3

    :pswitch_3
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object p1

    if-eqz p1, :cond_0

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$300(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result p1

    iget-object v0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object v0

    invoke-virtual {v0}, Lcom/xiaomi/mediacodec/OriginalRenderDrawer;->getOutputTextureId()I

    move-result v0

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/GlesUtil;->deleteFrameBuffer(II)V

    new-array p1, v1, [I

    iget-object v0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$700(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result v0

    aput v0, p1, v4

    invoke-static {v1, p1, v4}, Landroid/opengl/GLES30;->glDeleteTextures(I[II)V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object p1

    invoke-virtual {p1}, Lcom/xiaomi/mediacodec/BaseRenderDrawer;->destroy()V

    const-string p1, " detete frame "

    invoke-static {p1}, Lcom/xiaomi/mediacodec/Logg;->LogI(Ljava/lang/String;)V

    :cond_0
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$800(Lcom/xiaomi/mediacodec/MiHWDecoder;)Landroid/graphics/SurfaceTexture;

    move-result-object p1

    const/4 v0, 0x0

    if-eqz p1, :cond_1

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$800(Lcom/xiaomi/mediacodec/MiHWDecoder;)Landroid/graphics/SurfaceTexture;

    move-result-object p1

    invoke-virtual {p1}, Landroid/graphics/SurfaceTexture;->release()V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$802(Lcom/xiaomi/mediacodec/MiHWDecoder;Landroid/graphics/SurfaceTexture;)Landroid/graphics/SurfaceTexture;

    :cond_1
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$602(Lcom/xiaomi/mediacodec/MiHWDecoder;Lcom/xiaomi/mediacodec/OriginalRenderDrawer;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$200(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/EglBase;

    move-result-object p1

    if-eqz p1, :cond_2

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$200(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/EglBase;

    move-result-object p1

    invoke-virtual {p1}, Lcom/xiaomi/mediacodec/EglBase;->release()V

    :cond_2
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1, v1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1402(Lcom/xiaomi/mediacodec/MiHWDecoder;Z)Z

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$202(Lcom/xiaomi/mediacodec/MiHWDecoder;Lcom/xiaomi/mediacodec/EglBase;)Lcom/xiaomi/mediacodec/EglBase;

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, " recoder end "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object p0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1400(Lcom/xiaomi/mediacodec/MiHWDecoder;)Z

    move-result p0

    invoke-virtual {p1, p0}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-static {p0}, Lcom/xiaomi/mediacodec/Logg;->LogI(Ljava/lang/String;)V

    goto/16 :goto_3

    :pswitch_4
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$800(Lcom/xiaomi/mediacodec/MiHWDecoder;)Landroid/graphics/SurfaceTexture;

    move-result-object p1

    invoke-virtual {p1}, Landroid/graphics/SurfaceTexture;->updateTexImage()V

    invoke-static {}, Lcom/xiaomi/mediacodec/GlesUtil;->checkError()V

    const/16 p1, 0x10

    new-array p1, p1, [F

    iget-object v0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$800(Lcom/xiaomi/mediacodec/MiHWDecoder;)Landroid/graphics/SurfaceTexture;

    move-result-object v0

    invoke-virtual {v0, p1}, Landroid/graphics/SurfaceTexture;->getTransformMatrix([F)V

    iget-object v0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$800(Lcom/xiaomi/mediacodec/MiHWDecoder;)Landroid/graphics/SurfaceTexture;

    move-result-object v0

    invoke-virtual {v0}, Landroid/graphics/SurfaceTexture;->getTimestamp()J

    move-result-wide v0

    iget-object v5, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v5}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$300(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result v5

    iget-object v6, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v6}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object v6

    invoke-virtual {v6}, Lcom/xiaomi/mediacodec/OriginalRenderDrawer;->getOutputTextureId()I

    move-result v6

    invoke-static {v5, v6}, Lcom/xiaomi/mediacodec/GlesUtil;->bindFrameBuffer(II)V

    invoke-static {}, Lcom/xiaomi/mediacodec/GlesUtil;->checkError()V

    iget-object v5, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v5}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object v5

    invoke-virtual {v5, v0, v1, p1}, Lcom/xiaomi/mediacodec/BaseRenderDrawer;->draw(J[F)V

    invoke-static {}, Lcom/xiaomi/mediacodec/GlesUtil;->checkError()V

    invoke-static {}, Landroid/opengl/GLES30;->glFlush()V

    invoke-static {}, Lcom/xiaomi/mediacodec/GlesUtil;->checkError()V

    invoke-static {}, Lcom/xiaomi/mediacodec/GlesUtil;->unBindFrameBuffer()V

    invoke-static {}, Lcom/xiaomi/mediacodec/GlesUtil;->checkError()V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$900(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result p1

    if-gez p1, :cond_3

    sget p1, Lcom/xiaomi/mediacodec/GlUtil;->mWidht:I

    sget v5, Lcom/xiaomi/mediacodec/GlUtil;->mHeight:I

    mul-int/2addr p1, v5

    mul-int/lit8 p1, p1, 0x4

    invoke-static {p1}, Ljava/nio/ByteBuffer;->allocateDirect(I)Ljava/nio/ByteBuffer;

    move-result-object p1

    iget-object v5, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v5}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$300(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result v5

    const v12, 0x8d40

    invoke-static {v12, v5}, Landroid/opengl/GLES30;->glBindFramebuffer(II)V

    sget-object v5, Ljava/nio/ByteOrder;->LITTLE_ENDIAN:Ljava/nio/ByteOrder;

    invoke-virtual {p1, v5}, Ljava/nio/ByteBuffer;->order(Ljava/nio/ByteOrder;)Ljava/nio/ByteBuffer;

    const/4 v5, 0x0

    const/4 v6, 0x0

    sget v7, Lcom/xiaomi/mediacodec/GlUtil;->mWidht:I

    sget v8, Lcom/xiaomi/mediacodec/GlUtil;->mHeight:I

    const/16 v9, 0x1908

    const/16 v10, 0x1401

    move-object v11, p1

    invoke-static/range {v5 .. v11}, Landroid/opengl/GLES30;->glReadPixels(IIIIIILjava/nio/Buffer;)V

    invoke-virtual {p1}, Ljava/nio/ByteBuffer;->rewind()Ljava/nio/Buffer;

    sget v5, Lcom/xiaomi/mediacodec/GlUtil;->mWidht:I

    sget v6, Lcom/xiaomi/mediacodec/GlUtil;->mHeight:I

    sget-object v7, Landroid/graphics/Bitmap$Config;->ARGB_8888:Landroid/graphics/Bitmap$Config;

    invoke-static {v5, v6, v7}, Landroid/graphics/Bitmap;->createBitmap(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;

    move-result-object v5

    invoke-virtual {v5, p1}, Landroid/graphics/Bitmap;->copyPixelsFromBuffer(Ljava/nio/Buffer;)V

    invoke-static {v12, v4}, Landroid/opengl/GLES30;->glBindFramebuffer(II)V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$908(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "kkk"

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v4, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v4}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$900(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result v4

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v4, ".jpeg"

    invoke-virtual {p1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    const-string v4, "/sdcard/kk"

    invoke-static {v5, v4, p1}, Lcom/xiaomi/mediacodec/GlUtil;->saveFile(Landroid/graphics/Bitmap;Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;

    :cond_3
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1008(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1100(Lcom/xiaomi/mediacodec/MiHWDecoder;)J

    move-result-wide v4

    const-wide/16 v6, -0x1

    cmp-long p1, v4, v6

    if-eqz p1, :cond_4

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1100(Lcom/xiaomi/mediacodec/MiHWDecoder;)J

    move-result-wide v4

    mul-long/2addr v4, v2

    mul-long/2addr v4, v2

    cmp-long p1, v0, v4

    if-ltz p1, :cond_5

    :cond_4
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1200(Lcom/xiaomi/mediacodec/MiHWDecoder;)J

    move-result-wide v4

    cmp-long p1, v4, v6

    if-eqz p1, :cond_6

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1200(Lcom/xiaomi/mediacodec/MiHWDecoder;)J

    move-result-wide v4

    mul-long/2addr v4, v2

    mul-long/2addr v4, v2

    cmp-long p1, v0, v4

    if-gtz p1, :cond_5

    goto :goto_0

    :cond_5
    iget-object p0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-virtual {p0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->getPlayer()Lcom/xiaomi/mediacodec/MoviePlayer;

    move-result-object p0

    invoke-virtual {p0}, Lcom/xiaomi/mediacodec/MoviePlayer;->getOneFrame()V

    goto/16 :goto_3

    :cond_6
    :goto_0
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$1300(Lcom/xiaomi/mediacodec/MiHWDecoder;)J

    move-result-wide v2

    iget-object p0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object p0

    invoke-virtual {p0}, Lcom/xiaomi/mediacodec/OriginalRenderDrawer;->getOutputTextureId()I

    move-result p0

    sget v4, Lcom/xiaomi/mediacodec/GlUtil;->mWidht:I

    sget v5, Lcom/xiaomi/mediacodec/GlUtil;->mHeight:I

    const-wide/32 v6, 0xf4240

    div-long v6, v0, v6

    move-object v0, p1

    move-wide v1, v2

    move v3, p0

    invoke-virtual/range {v0 .. v7}, Lcom/xiaomi/mediacodec/MiHWDecoder;->onVideoFrameJni(JIIIJ)V

    goto/16 :goto_3

    :pswitch_5
    new-instance p1, Ljava/lang/StringBuilder;

    invoke-direct {p1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, " createPbufferSurface width "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget v0, Lcom/xiaomi/mediacodec/GlUtil;->mWidht:I

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, " height "

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    sget v0, Lcom/xiaomi/mediacodec/GlUtil;->mHeight:I

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string v0, " shader_egl_context:"

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$100(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/EglBase$Context;

    move-result-object v0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-static {p1}, Lcom/xiaomi/mediacodec/Logg;->LogI(Ljava/lang/String;)V

    :try_start_0
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    iget-object v0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$100(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/EglBase$Context;

    move-result-object v0

    invoke-static {v0}, Lcom/xiaomi/mediacodec/EglBase;->create(Lcom/xiaomi/mediacodec/EglBase$Context;)Lcom/xiaomi/mediacodec/EglBase;

    move-result-object v0

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$202(Lcom/xiaomi/mediacodec/MiHWDecoder;Lcom/xiaomi/mediacodec/EglBase;)Lcom/xiaomi/mediacodec/EglBase;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    :catch_0
    move-exception p1

    invoke-virtual {p1}, Ljava/lang/Exception;->printStackTrace()V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {}, Lcom/xiaomi/mediacodec/EglBase;->create()Lcom/xiaomi/mediacodec/EglBase;

    move-result-object v0

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$202(Lcom/xiaomi/mediacodec/MiHWDecoder;Lcom/xiaomi/mediacodec/EglBase;)Lcom/xiaomi/mediacodec/EglBase;

    :goto_1
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$200(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/EglBase;

    move-result-object p1

    sget v0, Lcom/xiaomi/mediacodec/GlUtil;->mWidht:I

    sget v2, Lcom/xiaomi/mediacodec/GlUtil;->mHeight:I

    invoke-virtual {p1, v0, v2}, Lcom/xiaomi/mediacodec/EglBase;->createPbufferSurface(II)V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$200(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/EglBase;

    move-result-object p1

    invoke-virtual {p1}, Lcom/xiaomi/mediacodec/EglBase;->makeCurrent()V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {}, Lcom/xiaomi/mediacodec/GlesUtil;->createFrameBuffer()I

    move-result v0

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$302(Lcom/xiaomi/mediacodec/MiHWDecoder;I)I

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$400(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result p1

    sget v0, Lcom/xiaomi/mediacodec/GlUtil;->mWidht:I

    if-ne p1, v0, :cond_7

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$500(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result p1

    sget v0, Lcom/xiaomi/mediacodec/GlUtil;->mHeight:I

    if-ne p1, v0, :cond_7

    goto :goto_2

    :cond_7
    move v1, v4

    :goto_2
    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object p1

    invoke-virtual {p1, v1}, Lcom/xiaomi/mediacodec/OriginalRenderDrawer;->setReserverResolution(Z)V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v1}, Lcom/xiaomi/mediacodec/GlesUtil;->createCameraTexture(Z)I

    move-result v0

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$702(Lcom/xiaomi/mediacodec/MiHWDecoder;I)I

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    new-instance v0, Landroid/graphics/SurfaceTexture;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$700(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result v1

    invoke-direct {v0, v1}, Landroid/graphics/SurfaceTexture;-><init>(I)V

    invoke-static {p1, v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$802(Lcom/xiaomi/mediacodec/MiHWDecoder;Landroid/graphics/SurfaceTexture;)Landroid/graphics/SurfaceTexture;

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$800(Lcom/xiaomi/mediacodec/MiHWDecoder;)Landroid/graphics/SurfaceTexture;

    move-result-object p1

    iget-object v0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-virtual {p1, v0}, Landroid/graphics/SurfaceTexture;->setOnFrameAvailableListener(Landroid/graphics/SurfaceTexture$OnFrameAvailableListener;)V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-virtual {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->Play()V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object p1

    invoke-virtual {p1}, Lcom/xiaomi/mediacodec/BaseRenderDrawer;->create()V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object p1

    iget-object v0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {v0}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$700(Lcom/xiaomi/mediacodec/MiHWDecoder;)I

    move-result v0

    invoke-virtual {p1, v0}, Lcom/xiaomi/mediacodec/OriginalRenderDrawer;->setInputTextureId(I)V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object p1

    sget v0, Lcom/xiaomi/mediacodec/GlUtil;->mWidht:I

    sget v1, Lcom/xiaomi/mediacodec/GlUtil;->mHeight:I

    invoke-virtual {p1, v0, v1}, Lcom/xiaomi/mediacodec/BaseRenderDrawer;->surfaceChangedSize(II)V

    iget-object p1, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    invoke-static {p1}, Lcom/xiaomi/mediacodec/MiHWDecoder;->access$600(Lcom/xiaomi/mediacodec/MiHWDecoder;)Lcom/xiaomi/mediacodec/OriginalRenderDrawer;

    move-result-object p1

    invoke-virtual {p1}, Lcom/xiaomi/mediacodec/OriginalRenderDrawer;->getOutputTextureId()I

    iget-object p0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder$GLHandler;->this$0:Lcom/xiaomi/mediacodec/MiHWDecoder;

    iget-object p0, p0, Lcom/xiaomi/mediacodec/MiHWDecoder;->mPlayTask:Lcom/xiaomi/mediacodec/MoviePlayer$PlayTask;

    invoke-virtual {p0}, Lcom/xiaomi/mediacodec/MoviePlayer$PlayTask;->execute()V

    :goto_3
    :pswitch_6
    return-void

    :pswitch_data_0
    .packed-switch 0x1
        :pswitch_5
        :pswitch_4
        :pswitch_6
        :pswitch_3
        :pswitch_6
        :pswitch_2
        :pswitch_1
        :pswitch_0
        :pswitch_6
    .end packed-switch
.end method

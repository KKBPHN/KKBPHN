.class Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;
.super Lmiuix/animation/listener/TransitionListener;
.source ""


# instance fields
.field private final dstRenderArea:Landroid/graphics/Rect;

.field private final srcRenderArea:Landroid/graphics/Rect;

.field final synthetic this$0:Lcom/android/camera/dualvideo/render/CameraItem;


# direct methods
.method public constructor <init>(Lcom/android/camera/dualvideo/render/CameraItem;Lcom/android/camera/dualvideo/render/RegionHelper;)V
    .locals 5

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->this$0:Lcom/android/camera/dualvideo/render/CameraItem;

    invoke-direct {p0}, Lmiuix/animation/listener/TransitionListener;-><init>()V

    const/16 v0, 0x65

    invoke-virtual {p1, v0}, Lcom/android/camera/dualvideo/render/CameraItem;->getRenderAttri(I)Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;

    move-result-object v0

    check-cast v0, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;

    new-instance v1, Landroid/graphics/Rect;

    iget v2, v0, Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;->mX:I

    iget v3, v0, Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;->mY:I

    iget v4, v0, Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;->mWidth:I

    add-int/2addr v4, v2

    iget v0, v0, Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;->mHeight:I

    add-int/2addr v0, v3

    invoke-direct {v1, v2, v3, v4, v0}, Landroid/graphics/Rect;-><init>(IIII)V

    iput-object v1, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->srcRenderArea:Landroid/graphics/Rect;

    invoke-virtual {p1}, Lcom/android/camera/dualvideo/render/CameraItem;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object p1

    invoke-virtual {p2, p1}, Lcom/android/camera/dualvideo/render/RegionHelper;->getRenderAreaFor(Lcom/android/camera/dualvideo/render/LayoutType;)Landroid/graphics/Rect;

    move-result-object p1

    iput-object p1, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->dstRenderArea:Landroid/graphics/Rect;

    return-void
.end method


# virtual methods
.method public animatePreview(I)V
    .locals 12

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->srcRenderArea:Landroid/graphics/Rect;

    iget-object v1, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->dstRenderArea:Landroid/graphics/Rect;

    int-to-float p1, p1

    const/high16 v2, 0x447a0000    # 1000.0f

    div-float/2addr p1, v2

    invoke-static {v0, v1, p1}, Lcom/android/camera/dualvideo/render/RenderUtil;->mixArea(Landroid/graphics/Rect;Landroid/graphics/Rect;F)Landroid/graphics/Rect;

    move-result-object p1

    iget-object v0, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->this$0:Lcom/android/camera/dualvideo/render/CameraItem;

    const/16 v1, 0x65

    invoke-virtual {v0, v1}, Lcom/android/camera/dualvideo/render/CameraItem;->getRenderAttri(I)Lcom/android/camera/effect/draw_mode/DrawRectShapeAttributeBase;

    move-result-object v0

    check-cast v0, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;

    iget-object v2, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->this$0:Lcom/android/camera/dualvideo/render/CameraItem;

    invoke-virtual {v2}, Lcom/android/camera/dualvideo/render/CameraItem;->getFaceType()Lcom/android/camera/dualvideo/render/FaceType;

    move-result-object v2

    iget-object v3, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->this$0:Lcom/android/camera/dualvideo/render/CameraItem;

    invoke-virtual {v3}, Lcom/android/camera/dualvideo/render/CameraItem;->getRenderLayoutType()Lcom/android/camera/dualvideo/render/LayoutType;

    move-result-object v3

    iget-object v4, v0, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;->mExtTexture:Lcom/android/gallery3d/ui/ExtTexture;

    invoke-static {v2, v3, v4, p1}, Lcom/android/camera/dualvideo/render/RenderUtil;->generatePreviewTransMatrix(Lcom/android/camera/dualvideo/render/FaceType;Lcom/android/camera/dualvideo/render/LayoutType;Lcom/android/gallery3d/ui/BasicTexture;Landroid/graphics/Rect;)[F

    move-result-object v7

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->this$0:Lcom/android/camera/dualvideo/render/CameraItem;

    new-instance v2, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;

    iget-object v6, v0, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;->mExtTexture:Lcom/android/gallery3d/ui/ExtTexture;

    iget v8, p1, Landroid/graphics/Rect;->left:I

    iget v9, p1, Landroid/graphics/Rect;->top:I

    invoke-virtual {p1}, Landroid/graphics/Rect;->width()I

    move-result v10

    invoke-virtual {p1}, Landroid/graphics/Rect;->height()I

    move-result v11

    move-object v5, v2

    invoke-direct/range {v5 .. v11}, Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;-><init>(Lcom/android/gallery3d/ui/ExtTexture;[FIIII)V

    invoke-virtual {p0, v2, v1}, Lcom/android/camera/dualvideo/render/CameraItem;->setRenderAttri(Lcom/android/camera/effect/draw_mode/DrawExtTexAttribute;I)V

    return-void
.end method

.method public onUpdate(Ljava/lang/Object;Lmiuix/animation/property/IIntValueProperty;IFZ)V
    .locals 0

    invoke-virtual {p0, p3}, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->animatePreview(I)V

    if-eqz p5, :cond_0

    iget-object p0, p0, Lcom/android/camera/dualvideo/render/CameraItem$RenderTypeChangeAnimListener;->this$0:Lcom/android/camera/dualvideo/render/CameraItem;

    const/4 p1, 0x0

    invoke-static {p0, p1}, Lcom/android/camera/dualvideo/render/CameraItem;->access$202(Lcom/android/camera/dualvideo/render/CameraItem;Z)Z

    :cond_0
    return-void
.end method

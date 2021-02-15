.class public Lcom/android/camera/lib/compatibility/related/v30/V30Utils;
.super Ljava/lang/Object;
.source ""


# annotations
.annotation build Landroid/annotation/TargetApi;
    value = 0x1e
.end annotation


# static fields
.field private static final TAG:Ljava/lang/String; = "V30Utils"


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static applyExtendSceneMode(Landroid/hardware/camera2/CaptureRequest$Builder;I)V
    .locals 0

    new-instance p0, Ljava/lang/RuntimeException;

    const-string p1, "could not find control_extended_scene_mode"

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public static applySessionKey(Landroid/hardware/camera2/CaptureRequest$Builder;Ljava/lang/String;Ljava/lang/Object;)V
    .locals 1

    new-instance p0, Ljava/lang/RuntimeException;

    new-instance p2, Ljava/lang/StringBuilder;

    invoke-direct {p2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v0, "could not find tag "

    invoke-virtual {p2, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p1

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public static applyZoomRatio(Landroid/hardware/camera2/CaptureRequest$Builder;F)V
    .locals 0

    new-instance p0, Ljava/lang/RuntimeException;

    const-string p1, "could not apply control_zoom_ratio below Android R"

    invoke-direct {p0, p1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public static setCameraAudioRestriction(Landroid/hardware/camera2/CameraDevice;I)V
    .locals 0

    return-void
.end method

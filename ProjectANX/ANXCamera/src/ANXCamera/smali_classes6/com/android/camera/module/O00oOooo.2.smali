.class public final synthetic Lcom/android/camera/module/O00oOooo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/Camera2Module;

.field private final synthetic O0OOoOO:Ljava/lang/String;

.field private final synthetic O0OOoOo:Landroid/graphics/Bitmap;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/Camera2Module;Ljava/lang/String;Landroid/graphics/Bitmap;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O00oOooo;->O0OOoO0:Lcom/android/camera/module/Camera2Module;

    iput-object p2, p0, Lcom/android/camera/module/O00oOooo;->O0OOoOO:Ljava/lang/String;

    iput-object p3, p0, Lcom/android/camera/module/O00oOooo;->O0OOoOo:Landroid/graphics/Bitmap;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 2

    iget-object v0, p0, Lcom/android/camera/module/O00oOooo;->O0OOoO0:Lcom/android/camera/module/Camera2Module;

    iget-object v1, p0, Lcom/android/camera/module/O00oOooo;->O0OOoOO:Ljava/lang/String;

    iget-object p0, p0, Lcom/android/camera/module/O00oOooo;->O0OOoOo:Landroid/graphics/Bitmap;

    invoke-virtual {v0, v1, p0}, Lcom/android/camera/module/Camera2Module;->O00000Oo(Ljava/lang/String;Landroid/graphics/Bitmap;)V

    return-void
.end method

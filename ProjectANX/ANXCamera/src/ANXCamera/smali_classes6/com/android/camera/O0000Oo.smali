.class public final synthetic Lcom/android/camera/O0000Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/ActivityBase;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/ActivityBase;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/O0000Oo;->O0OOoO0:Lcom/android/camera/ActivityBase;

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/O0000Oo;->O0OOoO0:Lcom/android/camera/ActivityBase;

    invoke-virtual {p0}, Landroid/app/Activity;->finish()V

    return-void
.end method

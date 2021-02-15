.class public final synthetic Lcom/android/camera/module/O000oO0O;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lcom/android/camera/module/VideoBase$OnTagsListener;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/VideoModule;

.field private final synthetic O0OOoOO:Lcom/android/camera/module/VideoBase$OnTagsListener;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/VideoModule;Lcom/android/camera/module/VideoBase$OnTagsListener;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000oO0O;->O0OOoO0:Lcom/android/camera/module/VideoModule;

    iput-object p2, p0, Lcom/android/camera/module/O000oO0O;->O0OOoOO:Lcom/android/camera/module/VideoBase$OnTagsListener;

    return-void
.end method


# virtual methods
.method public final onTagsReady(Ljava/util/List;)V
    .locals 1

    iget-object v0, p0, Lcom/android/camera/module/O000oO0O;->O0OOoO0:Lcom/android/camera/module/VideoModule;

    iget-object p0, p0, Lcom/android/camera/module/O000oO0O;->O0OOoOO:Lcom/android/camera/module/VideoBase$OnTagsListener;

    invoke-virtual {v0, p0, p1}, Lcom/android/camera/module/VideoModule;->O000000o(Lcom/android/camera/module/VideoBase$OnTagsListener;Ljava/util/List;)V

    return-void
.end method

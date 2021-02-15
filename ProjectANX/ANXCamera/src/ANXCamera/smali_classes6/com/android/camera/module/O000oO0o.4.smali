.class public final synthetic Lcom/android/camera/module/O000oO0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lcom/android/camera/module/VideoBase$OnTagsListener;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/module/VideoModule;

.field private final synthetic O0OOoOO:Ljava/lang/String;

.field private final synthetic O0OOoOo:Landroid/content/ContentValues;

.field private final synthetic O0OOoo0:Z


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/module/VideoModule;Ljava/lang/String;Landroid/content/ContentValues;Z)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/module/O000oO0o;->O0OOoO0:Lcom/android/camera/module/VideoModule;

    iput-object p2, p0, Lcom/android/camera/module/O000oO0o;->O0OOoOO:Ljava/lang/String;

    iput-object p3, p0, Lcom/android/camera/module/O000oO0o;->O0OOoOo:Landroid/content/ContentValues;

    iput-boolean p4, p0, Lcom/android/camera/module/O000oO0o;->O0OOoo0:Z

    return-void
.end method


# virtual methods
.method public final onTagsReady(Ljava/util/List;)V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/module/O000oO0o;->O0OOoO0:Lcom/android/camera/module/VideoModule;

    iget-object v1, p0, Lcom/android/camera/module/O000oO0o;->O0OOoOO:Ljava/lang/String;

    iget-object v2, p0, Lcom/android/camera/module/O000oO0o;->O0OOoOo:Landroid/content/ContentValues;

    iget-boolean p0, p0, Lcom/android/camera/module/O000oO0o;->O0OOoo0:Z

    invoke-virtual {v0, v1, v2, p0, p1}, Lcom/android/camera/module/VideoModule;->O000000o(Ljava/lang/String;Landroid/content/ContentValues;ZLjava/util/List;)V

    return-void
.end method

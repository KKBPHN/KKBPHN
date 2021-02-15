.class public final synthetic Lcom/android/camera/ui/O00000Oo;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Landroid/view/View$OnTouchListener;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/ui/DragLayout;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/ui/DragLayout;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/ui/O00000Oo;->O0OOoO0:Lcom/android/camera/ui/DragLayout;

    return-void
.end method


# virtual methods
.method public final onTouch(Landroid/view/View;Landroid/view/MotionEvent;)Z
    .locals 0

    iget-object p0, p0, Lcom/android/camera/ui/O00000Oo;->O0OOoO0:Lcom/android/camera/ui/DragLayout;

    invoke-virtual {p0, p1, p2}, Lcom/android/camera/ui/DragLayout;->O000000o(Landroid/view/View;Landroid/view/MotionEvent;)Z

    move-result p0

    return p0
.end method

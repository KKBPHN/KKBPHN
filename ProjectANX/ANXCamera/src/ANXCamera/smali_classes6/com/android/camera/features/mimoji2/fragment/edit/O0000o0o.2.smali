.class public final synthetic Lcom/android/camera/features/mimoji2/fragment/edit/O0000o0o;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Lcom/android/camera/features/mimoji2/widget/baseview/OnRecyclerItemClickListener;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000o0o;->O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;

    return-void
.end method


# virtual methods
.method public final OnRecyclerItemClickListener(Ljava/lang/Object;ILandroid/view/View;)V
    .locals 0

    iget-object p0, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000o0o;->O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;

    check-cast p1, Lcom/android/camera/features/mimoji2/bean/MimojiEmoticonInfo;

    invoke-virtual {p0, p1, p2, p3}, Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;->O000000o(Lcom/android/camera/features/mimoji2/bean/MimojiEmoticonInfo;ILandroid/view/View;)V

    return-void
.end method

.class public final synthetic Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;
.super Ljava/lang/Object;
.source "lambda"

# interfaces
.implements Ljava/lang/Runnable;


# instance fields
.field private final synthetic O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;

.field private final synthetic O0OOoOO:Lcom/arcsoft/avatar2/emoticon/EmoInfo;

.field private final synthetic O0OOoOo:I

.field private final synthetic O0OOoo0:I


# direct methods
.method public synthetic constructor <init>(Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;Lcom/arcsoft/avatar2/emoticon/EmoInfo;II)V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;->O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;

    iput-object p2, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;->O0OOoOO:Lcom/arcsoft/avatar2/emoticon/EmoInfo;

    iput p3, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;->O0OOoOo:I

    iput p4, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;->O0OOoo0:I

    return-void
.end method


# virtual methods
.method public final run()V
    .locals 3

    iget-object v0, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;->O0OOoO0:Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;

    iget-object v1, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;->O0OOoOO:Lcom/arcsoft/avatar2/emoticon/EmoInfo;

    iget v2, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;->O0OOoOo:I

    iget p0, p0, Lcom/android/camera/features/mimoji2/fragment/edit/O0000OoO;->O0OOoo0:I

    invoke-virtual {v0, v1, v2, p0}, Lcom/android/camera/features/mimoji2/fragment/edit/FragmentMimojiEmoticon;->O000000o(Lcom/arcsoft/avatar2/emoticon/EmoInfo;II)V

    return-void
.end method

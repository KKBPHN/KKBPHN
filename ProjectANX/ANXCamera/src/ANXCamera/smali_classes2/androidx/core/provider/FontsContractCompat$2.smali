.class final Landroidx/core/provider/FontsContractCompat$2;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Landroidx/core/provider/SelfDestructiveThread$ReplyCallback;


# instance fields
.field final synthetic val$fontCallback:Landroidx/core/content/res/ResourcesCompat$FontCallback;


# direct methods
.method constructor <init>(Landroidx/core/content/res/ResourcesCompat$FontCallback;)V
    .locals 0

    iput-object p1, p0, Landroidx/core/provider/FontsContractCompat$2;->val$fontCallback:Landroidx/core/content/res/ResourcesCompat$FontCallback;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onReply(Landroidx/core/provider/FontsContractCompat$TypefaceResult;)V
    .locals 1

    if-nez p1, :cond_0

    iget-object p0, p0, Landroidx/core/provider/FontsContractCompat$2;->val$fontCallback:Landroidx/core/content/res/ResourcesCompat$FontCallback;

    const/4 p1, 0x1

    invoke-virtual {p0, p1}, Landroidx/core/content/res/ResourcesCompat$FontCallback;->onFontRetrievalFailed(I)V

    goto :goto_0

    :cond_0
    iget v0, p1, Landroidx/core/provider/FontsContractCompat$TypefaceResult;->mResult:I

    iget-object p0, p0, Landroidx/core/provider/FontsContractCompat$2;->val$fontCallback:Landroidx/core/content/res/ResourcesCompat$FontCallback;

    if-nez v0, :cond_1

    iget-object p1, p1, Landroidx/core/provider/FontsContractCompat$TypefaceResult;->mTypeface:Landroid/graphics/Typeface;

    invoke-virtual {p0, p1}, Landroidx/core/content/res/ResourcesCompat$FontCallback;->onFontRetrieved(Landroid/graphics/Typeface;)V

    goto :goto_0

    :cond_1
    invoke-virtual {p0, v0}, Landroidx/core/content/res/ResourcesCompat$FontCallback;->onFontRetrievalFailed(I)V

    :goto_0
    return-void
.end method

.method public bridge synthetic onReply(Ljava/lang/Object;)V
    .locals 0

    check-cast p1, Landroidx/core/provider/FontsContractCompat$TypefaceResult;

    invoke-virtual {p0, p1}, Landroidx/core/provider/FontsContractCompat$2;->onReply(Landroidx/core/provider/FontsContractCompat$TypefaceResult;)V

    return-void
.end method

.class public Lcom/android/camera/fragment/mode/ModeAdapter;
.super Landroidx/recyclerview/widget/RecyclerView$Adapter;
.source ""


# static fields
.field public static final FLAG_ENTER:I = 0x2

.field public static final FLAG_SWITCH:I = 0x4

.field private static final MAX_TOP_ITEM_COUNT:I = 0xa

.field private static final TAG:Ljava/lang/String; = "ModeAdapter"

.field public static final TYPE_CAPTURE:I = 0x3

.field public static final TYPE_EDIT:I = 0x5

.field public static final TYPE_FOOTER:I = 0x6

.field public static final TYPE_HEADER:I = 0x1

.field public static final TYPE_LINE:I = 0x2

.field public static final TYPE_PENDING_DOWNLOAD:I = 0x7

.field public static final TYPE_VIDEO:I = 0x4


# instance fields
.field private mAnimFlags:I

.field protected mClickListener:Landroid/view/View$OnClickListener;

.field protected mContext:Landroid/content/Context;

.field private mDegree:F

.field protected mFragmentType:I
    .annotation build Lcom/android/camera/fragment/mode/IMoreMode$Type;
    .end annotation
.end field

.field protected mItems:Ljava/util/List;

.field protected mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

.field private mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;


# direct methods
.method static constructor <clinit>()V
    .locals 0

    return-void
.end method

.method public constructor <init>(Landroid/content/Context;Lcom/android/camera/fragment/mode/FragmentMoreModeBase;)V
    .locals 2

    invoke-direct {p0}, Landroidx/recyclerview/widget/RecyclerView$Adapter;-><init>()V

    iput-object p1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-virtual {p2}, Lcom/android/camera/fragment/mode/FragmentMoreModeBase;->getComponentModuleList()Lcom/android/camera/data/data/global/ComponentModuleList;

    move-result-object p1

    iput-object p1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    iput-object p2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mClickListener:Landroid/view/View$OnClickListener;

    invoke-interface {p2}, Lcom/android/camera/fragment/mode/IMoreMode;->getType()I

    move-result p1

    iput p1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    iput-object p2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    iget p1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    const/4 p2, 0x2

    if-ne p1, p2, :cond_0

    new-instance p1, Ljava/util/ArrayList;

    iget-object p2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    invoke-virtual {p2}, Lcom/android/camera/data/data/global/ComponentModuleList;->getItems()Ljava/util/List;

    move-result-object p2

    const/4 v0, 0x0

    iget-object v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    invoke-virtual {v1}, Lcom/android/camera/data/data/global/ComponentModuleList;->getItems()Ljava/util/List;

    move-result-object v1

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v1

    add-int/lit8 v1, v1, -0x1

    invoke-interface {p2, v0, v1}, Ljava/util/List;->subList(II)Ljava/util/List;

    move-result-object p2

    invoke-direct {p1, p2}, Ljava/util/ArrayList;-><init>(Ljava/util/Collection;)V

    goto :goto_0

    :cond_0
    iget-object p1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    invoke-virtual {p1}, Lcom/android/camera/data/data/global/ComponentModuleList;->getMoreItems()Ljava/util/List;

    move-result-object p1

    :goto_0
    iput-object p1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    return-void
.end method

.method static synthetic access$000()Ljava/lang/String;
    .locals 1

    sget-object v0, Lcom/android/camera/fragment/mode/ModeAdapter;->TAG:Ljava/lang/String;

    return-object v0
.end method

.method private addModeBackground(Landroid/view/ViewGroup;)Lcom/android/camera/ui/ModeBackground;
    .locals 2

    new-instance p0, Lcom/android/camera/ui/ModeBackground;

    invoke-virtual {p1}, Landroid/view/ViewGroup;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/ModeBackground;-><init>(Landroid/content/Context;)V

    const v0, 0x7f09023e

    invoke-virtual {p0, v0}, Landroid/view/View;->setId(I)V

    new-instance v0, Landroid/widget/FrameLayout$LayoutParams;

    const/4 v1, -0x1

    invoke-direct {v0, v1, v1}, Landroid/widget/FrameLayout$LayoutParams;-><init>(II)V

    const/4 v1, 0x0

    invoke-virtual {p1, p0, v1, v0}, Landroid/view/ViewGroup;->addView(Landroid/view/View;ILandroid/view/ViewGroup$LayoutParams;)V

    return-object p0
.end method

.method private addWaterBox(Landroid/view/ViewGroup;)Lcom/android/camera/ui/WaterBox;
    .locals 3

    new-instance p0, Lcom/android/camera/ui/WaterBox;

    invoke-virtual {p1}, Landroid/view/ViewGroup;->getContext()Landroid/content/Context;

    move-result-object v0

    invoke-direct {p0, v0}, Lcom/android/camera/ui/WaterBox;-><init>(Landroid/content/Context;)V

    invoke-virtual {p1}, Landroid/view/ViewGroup;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v1, 0x7f060138

    invoke-virtual {v0, v1}, Landroid/content/res/Resources;->getColor(I)I

    move-result v0

    invoke-virtual {p0, v0}, Lcom/android/camera/ui/WaterBox;->setColor(I)V

    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Landroid/widget/RelativeLayout;->setBackgroundColor(I)V

    const/4 v1, 0x0

    invoke-virtual {p0, v1, v0}, Lcom/android/camera/ui/WaterBox;->setValue(FZ)V

    const v1, 0x7f09023e

    invoke-virtual {p0, v1}, Landroid/widget/RelativeLayout;->setId(I)V

    new-instance v1, Landroid/widget/FrameLayout$LayoutParams;

    const/4 v2, -0x1

    invoke-direct {v1, v2, v2}, Landroid/widget/FrameLayout$LayoutParams;-><init>(II)V

    invoke-virtual {p1, p0, v0, v1}, Landroid/view/ViewGroup;->addView(Landroid/view/View;ILandroid/view/ViewGroup$LayoutParams;)V

    return-object p0
.end method

.method private createTouchHelper()Landroidx/recyclerview/widget/ItemTouchHelper;
    .locals 2

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    const/4 v1, 0x2

    if-eq v0, v1, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    new-instance v0, Landroidx/recyclerview/widget/ItemTouchHelper;

    new-instance v1, Lcom/android/camera/fragment/mode/ModeAdapter$2;

    invoke-direct {v1, p0}, Lcom/android/camera/fragment/mode/ModeAdapter$2;-><init>(Lcom/android/camera/fragment/mode/ModeAdapter;)V

    invoke-direct {v0, v1}, Landroidx/recyclerview/widget/ItemTouchHelper;-><init>(Landroidx/recyclerview/widget/ItemTouchHelper$Callback;)V

    return-object v0
.end method

.method private getItem(I)Lcom/android/camera/data/data/ComponentDataItem;
    .locals 2

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    if-eqz v0, :cond_0

    const/4 v1, 0x2

    if-eq v0, v1, :cond_0

    const/4 v1, 0x3

    if-eq v0, v1, :cond_0

    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    :goto_0
    invoke-interface {p0, p1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/data/data/ComponentDataItem;

    goto :goto_1

    :cond_0
    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    add-int/lit8 p1, p1, -0x1

    goto :goto_0

    :goto_1
    return-object p0
.end method


# virtual methods
.method public createChangeItems()Ljava/util/List;
    .locals 3

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    const/4 v1, 0x2

    if-eq v0, v1, :cond_0

    const/4 p0, 0x0

    return-object p0

    :cond_0
    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v1

    add-int/lit8 v1, v1, -0x1

    invoke-interface {v0, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/android/camera/data/data/ComponentDataItem;

    iget-object v0, v0, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {v0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v0

    const/16 v1, 0xff

    if-eq v0, v1, :cond_1

    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    iget-object v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    invoke-virtual {v1}, Lcom/android/camera/data/data/global/ComponentModuleList;->getItems()Ljava/util/List;

    move-result-object v1

    iget-object v2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    invoke-virtual {v2}, Lcom/android/camera/data/data/global/ComponentModuleList;->getItems()Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v2

    add-int/lit8 v2, v2, -0x1

    invoke-interface {v1, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-interface {v0, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_1
    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    return-object p0
.end method

.method public getItemCount()I
    .locals 3

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    const/4 v1, 0x1

    if-ne v0, v1, :cond_0

    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    return p0

    :cond_0
    if-eqz v0, :cond_2

    const/4 v2, 0x3

    if-ne v0, v2, :cond_1

    goto :goto_0

    :cond_1
    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    add-int/2addr p0, v1

    return p0

    :cond_2
    :goto_0
    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    add-int/2addr p0, v1

    return p0
.end method

.method public getItemViewType(I)I
    .locals 7

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    const/4 v1, 0x5

    const/16 v2, 0xff

    const/4 v3, 0x7

    const/4 v4, 0x1

    if-eqz v0, :cond_8

    if-eq v0, v4, :cond_6

    const/4 v5, 0x3

    const/4 v6, 0x2

    if-eq v0, v6, :cond_0

    if-eq v0, v5, :cond_8

    goto/16 :goto_0

    :cond_0
    if-nez p1, :cond_1

    return v4

    :cond_1
    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v0

    add-int/2addr v0, v4

    if-ne p1, v0, :cond_2

    const/4 p0, 0x6

    return p0

    :cond_2
    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItem(I)Lcom/android/camera/data/data/ComponentDataItem;

    move-result-object v0

    iget-object v0, v0, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {v0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v0

    const/16 v1, 0xfe

    if-ne v0, v1, :cond_3

    return v6

    :cond_3
    const/16 v1, 0xa3

    if-ne v0, v1, :cond_4

    return v5

    :cond_4
    const/16 v1, 0xa2

    if-ne v0, v1, :cond_5

    const/4 p0, 0x4

    return p0

    :cond_5
    iget-object v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    invoke-interface {v1, v0}, Lcom/android/camera/fragment/mode/IMoreMode;->modeShouldDownload(I)Z

    move-result v0

    if-eqz v0, :cond_b

    return v3

    :cond_6
    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItem(I)Lcom/android/camera/data/data/ComponentDataItem;

    move-result-object v0

    iget-object v0, v0, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {v0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v0

    if-ne v0, v2, :cond_7

    return v1

    :cond_7
    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItem(I)Lcom/android/camera/data/data/ComponentDataItem;

    move-result-object v1

    iget-object v1, v1, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {v1}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v1

    invoke-interface {v0, v1}, Lcom/android/camera/fragment/mode/IMoreMode;->modeShouldDownload(I)Z

    move-result v0

    if-eqz v0, :cond_b

    return v3

    :cond_8
    if-nez p1, :cond_9

    return v4

    :cond_9
    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItem(I)Lcom/android/camera/data/data/ComponentDataItem;

    move-result-object v0

    iget-object v0, v0, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {v0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v0

    if-ne v0, v2, :cond_a

    return v1

    :cond_a
    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItem(I)Lcom/android/camera/data/data/ComponentDataItem;

    move-result-object v1

    iget-object v1, v1, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-static {v1}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v1

    invoke-interface {v0, v1}, Lcom/android/camera/fragment/mode/IMoreMode;->modeShouldDownload(I)Z

    move-result v0

    if-eqz v0, :cond_b

    return v3

    :cond_b
    :goto_0
    invoke-super {p0, p1}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->getItemViewType(I)I

    move-result p0

    return p0
.end method

.method public getRotate()F
    .locals 0

    iget p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mDegree:F

    return p0
.end method

.method public isDataChange()Z
    .locals 6

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    const/4 v1, 0x0

    const/4 v2, 0x2

    if-eq v0, v2, :cond_0

    return v1

    :cond_0
    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    iget-object v2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    invoke-virtual {v2}, Lcom/android/camera/data/data/global/ComponentModuleList;->getItems()Ljava/util/List;

    move-result-object v2

    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mModuleList:Lcom/android/camera/data/data/global/ComponentModuleList;

    invoke-virtual {p0}, Lcom/android/camera/data/data/global/ComponentModuleList;->getItems()Ljava/util/List;

    move-result-object p0

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    const/4 v3, 0x1

    sub-int/2addr p0, v3

    invoke-interface {v2, v1, p0}, Ljava/util/List;->subList(II)Ljava/util/List;

    move-result-object p0

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v2

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v4

    if-ne v2, v4, :cond_3

    move v2, v1

    :goto_0
    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v4

    if-ge v2, v4, :cond_2

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Lcom/android/camera/data/data/ComponentDataItem;

    iget-object v4, v4, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-interface {p0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Lcom/android/camera/data/data/ComponentDataItem;

    iget-object v5, v5, Lcom/android/camera/data/data/ComponentDataItem;->mValue:Ljava/lang/String;

    invoke-virtual {v4, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-nez v4, :cond_1

    return v3

    :cond_1
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_2
    return v1

    :cond_3
    new-instance p0, Ljava/lang/IllegalStateException;

    const-string v0, "data lost."

    invoke-direct {p0, v0}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw p0
.end method

.method public onAttachedToRecyclerView(Landroidx/recyclerview/widget/RecyclerView;)V
    .locals 1
    .param p1    # Landroidx/recyclerview/widget/RecyclerView;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-super {p0, p1}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->onAttachedToRecyclerView(Landroidx/recyclerview/widget/RecyclerView;)V

    invoke-direct {p0}, Lcom/android/camera/fragment/mode/ModeAdapter;->createTouchHelper()Landroidx/recyclerview/widget/ItemTouchHelper;

    move-result-object v0

    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Landroidx/recyclerview/widget/ItemTouchHelper;->attachToRecyclerView(Landroidx/recyclerview/widget/RecyclerView;)V

    :cond_0
    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView;->getLayoutManager()Landroidx/recyclerview/widget/RecyclerView$LayoutManager;

    move-result-object p1

    instance-of v0, p1, Landroidx/recyclerview/widget/GridLayoutManager;

    if-eqz v0, :cond_1

    check-cast p1, Landroidx/recyclerview/widget/GridLayoutManager;

    new-instance v0, Lcom/android/camera/fragment/mode/ModeAdapter$1;

    invoke-direct {v0, p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter$1;-><init>(Lcom/android/camera/fragment/mode/ModeAdapter;Landroidx/recyclerview/widget/GridLayoutManager;)V

    invoke-virtual {p1, v0}, Landroidx/recyclerview/widget/GridLayoutManager;->setSpanSizeLookup(Landroidx/recyclerview/widget/GridLayoutManager$SpanSizeLookup;)V

    :cond_1
    return-void
.end method

.method public bridge synthetic onBindViewHolder(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;I)V
    .locals 0
    .param p1    # Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    check-cast p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    invoke-virtual {p0, p1, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->onBindViewHolder(Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;I)V

    return-void
.end method

.method public bridge synthetic onBindViewHolder(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;ILjava/util/List;)V
    .locals 0
    .param p1    # Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param
    .param p3    # Ljava/util/List;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    check-cast p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    invoke-virtual {p0, p1, p2, p3}, Lcom/android/camera/fragment/mode/ModeAdapter;->onBindViewHolder(Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;ILjava/util/List;)V

    return-void
.end method

.method public onBindViewHolder(Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;I)V
    .locals 11
    .param p1    # Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    const/4 v1, 0x6

    const/4 v2, 0x4

    const/4 v3, 0x3

    const/4 v4, 0x1

    if-eqz v0, :cond_0

    if-ne v0, v3, :cond_2

    :cond_0
    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    if-eq v0, v4, :cond_1

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    if-ne v0, v1, :cond_2

    :cond_1
    iget-object v0, p1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-virtual {v0, v2}, Landroid/view/View;->setVisibility(I)V

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    if-ne v0, v4, :cond_2

    iget-object v0, p1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-virtual {v0}, Landroid/view/View;->getLayoutParams()Landroid/view/ViewGroup$LayoutParams;

    move-result-object v0

    check-cast v0, Landroidx/recyclerview/widget/RecyclerView$LayoutParams;

    iget-object v5, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    iget v6, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    iget-object v7, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    invoke-interface {v7}, Lcom/android/camera/fragment/mode/IMoreMode;->getCountPerLine()I

    move-result v7

    iget-object v8, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    invoke-interface {v8}, Ljava/util/List;->size()I

    move-result v8

    invoke-static {v5, v6, v7, v8}, Lcom/android/camera/fragment/mode/MoreModeHelper;->getHeaderHeightForNormal(Landroid/content/Context;III)I

    move-result v5

    iput v5, v0, Landroid/view/ViewGroup$MarginLayoutParams;->height:I

    :cond_2
    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    const/4 v5, 0x2

    if-eq v0, v5, :cond_12

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    if-eq v0, v4, :cond_12

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    if-eq v0, v1, :cond_12

    invoke-direct {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItem(I)Lcom/android/camera/data/data/ComponentDataItem;

    move-result-object v0

    iget-object v1, p1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    invoke-virtual {v1, v0}, Landroid/view/View;->setTag(Ljava/lang/Object;)V

    iget v1, v0, Lcom/android/camera/data/data/ComponentDataItem;->mIconRes:I

    const/4 v6, -0x1

    const/4 v7, 0x7

    const/4 v8, 0x5

    const/4 v9, 0x0

    if-eq v1, v6, :cond_6

    iget-object v6, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mIconView:Landroid/widget/ImageView;

    iget v10, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    if-ne v10, v3, :cond_3

    iget v1, v0, Lcom/android/camera/data/data/ComponentDataItem;->mNewIconRes:I

    :cond_3
    invoke-virtual {v6, v1}, Landroid/widget/ImageView;->setImageResource(I)V

    iget v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mDegree:F

    invoke-virtual {p1, v1}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->setRotation(F)V

    invoke-virtual {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItemViewType(I)I

    move-result v1

    if-ne v1, v8, :cond_4

    :goto_0
    invoke-virtual {p1, v9, v9}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->setProgress(IZ)V

    goto :goto_1

    :cond_4
    invoke-virtual {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItemViewType(I)I

    move-result v1

    if-ne v1, v7, :cond_5

    goto :goto_0

    :cond_5
    const/16 v1, 0x64

    invoke-virtual {p1, v1, v9}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->setProgress(IZ)V

    :cond_6
    :goto_1
    iget v1, v0, Lcom/android/camera/data/data/ComponentDataItem;->mDisplayNameRes:I

    if-eqz v1, :cond_7

    iget-object v1, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mNameView:Landroid/widget/TextView;

    invoke-static {v9}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    invoke-virtual {v1, v6}, Landroid/widget/TextView;->setTag(Ljava/lang/Object;)V

    iget-object v1, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mNameView:Landroid/widget/TextView;

    iget v0, v0, Lcom/android/camera/data/data/ComponentDataItem;->mDisplayNameRes:I

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setText(I)V

    goto :goto_2

    :cond_7
    iget-object v1, v0, Lcom/android/camera/data/data/ComponentDataItem;->mDisplayNameStr:Ljava/lang/String;

    if-eqz v1, :cond_8

    iget-object v1, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mNameView:Landroid/widget/TextView;

    invoke-static {v9}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    invoke-virtual {v1, v6}, Landroid/widget/TextView;->setTag(Ljava/lang/Object;)V

    iget-object v1, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mNameView:Landroid/widget/TextView;

    iget-object v0, v0, Lcom/android/camera/data/data/ComponentDataItem;->mDisplayNameStr:Ljava/lang/String;

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    :cond_8
    :goto_2
    sget-object v0, Lcom/android/camera/fragment/mode/ModeAdapter;->TAG:Ljava/lang/String;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "onBindViewHolder "

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget-object v6, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mNameView:Landroid/widget/TextView;

    invoke-virtual {v6}, Landroid/widget/TextView;->getText()Ljava/lang/CharSequence;

    move-result-object v6

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    const-string v6, ", viewType = "

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v6

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v1}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    if-eq v0, v3, :cond_b

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    if-eq v0, v2, :cond_b

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    if-ne v0, v5, :cond_9

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result v0

    if-ne v0, v7, :cond_9

    goto :goto_3

    :cond_9
    iget-object v0, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mIconView:Landroid/widget/ImageView;

    const/high16 v1, 0x3f800000    # 1.0f

    invoke-virtual {v0, v1}, Landroid/widget/ImageView;->setAlpha(F)V

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    if-ne v0, v3, :cond_a

    iget-object v0, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mNameView:Landroid/widget/TextView;

    iget-object v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    const v5, 0x7f06013d

    goto :goto_4

    :cond_a
    iget-object v0, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mNameView:Landroid/widget/TextView;

    iget-object v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    const v5, 0x7f060133

    goto :goto_4

    :cond_b
    :goto_3
    iget-object v0, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mIconView:Landroid/widget/ImageView;

    const v1, 0x3e99999a    # 0.3f

    invoke-virtual {v0, v1}, Landroid/widget/ImageView;->setAlpha(F)V

    iget-object v0, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mNameView:Landroid/widget/TextView;

    iget-object v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    const v5, 0x7f060132

    :goto_4
    invoke-virtual {v1, v5}, Landroid/content/res/Resources;->getColor(I)I

    move-result v1

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setTextColor(I)V

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mAnimFlags:I

    and-int/2addr v0, v2

    if-eqz v0, :cond_12

    invoke-virtual {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItemViewType(I)I

    move-result v0

    if-eqz v0, :cond_c

    invoke-virtual {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItemViewType(I)I

    move-result v0

    if-eq v0, v7, :cond_c

    invoke-virtual {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItemViewType(I)I

    move-result v0

    if-ne v0, v8, :cond_12

    :cond_c
    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    invoke-interface {v0}, Lcom/android/camera/fragment/mode/IMoreMode;->getType()I

    move-result v0

    if-ne v0, v3, :cond_f

    invoke-virtual {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItemViewType(I)I

    move-result v0

    if-eq v0, v8, :cond_d

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/DataItemRunning;->getUiStyle()I

    move-result v0

    invoke-static {v0, v4}, Lcom/android/camera/Display;->getMoreModeTabRow(IZ)I

    move-result v0

    iget-object v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    invoke-interface {v1}, Lcom/android/camera/fragment/mode/IMoreMode;->getCountPerLine()I

    move-result v1

    mul-int/2addr v0, v1

    if-lt p2, v0, :cond_e

    :cond_d
    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mAnimFlags:I

    and-int/lit8 v0, v0, -0x5

    iput v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mAnimFlags:I

    :cond_e
    invoke-static {}, Lcom/android/camera/fragment/mode/MoreModeListAnimation;->getInstance()Lcom/android/camera/fragment/mode/MoreModeListAnimation;

    move-result-object p0

    iget-object p1, p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mIconLayout:Landroid/widget/FrameLayout;

    sub-int/2addr p2, v4

    invoke-virtual {p0, p1, p2}, Lcom/android/camera/fragment/mode/MoreModeListAnimation;->startShowNewAnimation(Landroid/view/View;I)V

    goto :goto_5

    :cond_f
    invoke-virtual {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->getItemViewType(I)I

    move-result v0

    if-eq v0, v8, :cond_10

    invoke-static {}, Lcom/android/camera/data/DataRepository;->dataItemRunning()Lcom/android/camera/data/data/runing/DataItemRunning;

    move-result-object v0

    invoke-virtual {v0}, Lcom/android/camera/data/data/runing/DataItemRunning;->getUiStyle()I

    move-result v0

    invoke-static {v0, v9}, Lcom/android/camera/Display;->getMoreModeTabRow(IZ)I

    move-result v0

    iget-object v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    invoke-interface {v1}, Lcom/android/camera/fragment/mode/IMoreMode;->getCountPerLine()I

    move-result v1

    mul-int/2addr v0, v1

    if-lt p2, v0, :cond_11

    :cond_10
    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mAnimFlags:I

    and-int/lit8 v0, v0, -0x5

    iput v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mAnimFlags:I

    :cond_11
    invoke-static {}, Lcom/android/camera/fragment/mode/MoreModeListAnimation;->getInstance()Lcom/android/camera/fragment/mode/MoreModeListAnimation;

    move-result-object p0

    iget-object p1, p1, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->itemView:Landroid/view/View;

    sub-int/2addr p2, v4

    invoke-virtual {p0, p1, p2}, Lcom/android/camera/fragment/mode/MoreModeListAnimation;->startShowOldAnimation(Landroid/view/View;I)V

    :cond_12
    :goto_5
    return-void
.end method

.method public onBindViewHolder(Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;ILjava/util/List;)V
    .locals 3
    .param p1    # Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param
    .param p3    # Ljava/util/List;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-interface {p3}, Ljava/util/List;->isEmpty()Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-virtual {p0, p1, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->onBindViewHolder(Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;I)V

    goto :goto_0

    :cond_0
    const/4 p0, 0x0

    invoke-interface {p3, p0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    instance-of v0, v0, Lcom/android/camera/fragment/mode/ModeAdapter$PlayLoad;

    if-eqz v0, :cond_2

    invoke-interface {p3, p0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Lcom/android/camera/fragment/mode/ModeAdapter$PlayLoad;

    iget p3, p0, Lcom/android/camera/fragment/mode/ModeAdapter$PlayLoad;->type:I

    const/4 v0, 0x1

    if-eq p3, v0, :cond_1

    goto :goto_0

    :cond_1
    sget-object p3, Lcom/android/camera/fragment/mode/ModeAdapter;->TAG:Ljava/lang/String;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "onBindViewHolderPayloads ,position = "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p2, ", viewType = "

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p1}, Landroidx/recyclerview/widget/RecyclerView$ViewHolder;->getItemViewType()I

    move-result p2

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    const-string p2, ", payloads = "

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget p2, p0, Lcom/android/camera/fragment/mode/ModeAdapter$PlayLoad;->progress:I

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p2

    invoke-static {p3, p2}, Lcom/android/camera/log/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    iget p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter$PlayLoad;->progress:I

    invoke-virtual {p1, p0, v0}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->setProgress(IZ)V

    :cond_2
    :goto_0
    return-void
.end method

.method public bridge synthetic onCreateViewHolder(Landroid/view/ViewGroup;I)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
    .locals 0
    .param p1    # Landroid/view/ViewGroup;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param
    .annotation build Landroidx/annotation/NonNull;
    .end annotation

    invoke-virtual {p0, p1, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->onCreateViewHolder(Landroid/view/ViewGroup;I)Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    move-result-object p0

    return-object p0
.end method

.method public onCreateViewHolder(Landroid/view/ViewGroup;I)Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;
    .locals 6
    .param p1    # Landroid/view/ViewGroup;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param
    .annotation build Landroidx/annotation/NonNull;
    .end annotation

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    const v1, 0x7f0c00e0

    const/4 v2, 0x3

    const/4 v3, 0x2

    const/4 v4, 0x1

    const/4 v5, 0x0

    if-ne v0, v3, :cond_2

    if-ne p2, v3, :cond_0

    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-static {p0}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;

    move-result-object p0

    const p2, 0x7f0c00de

    invoke-virtual {p0, p2, p1, v5}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object p0

    new-instance p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    invoke-direct {p1, p0}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;-><init>(Landroid/view/View;)V

    return-object p1

    :cond_0
    if-ne p2, v4, :cond_1

    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-static {p0}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;

    move-result-object p0

    const p2, 0x7f0c00df

    invoke-virtual {p0, p2, p1, v5}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object p0

    new-instance p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    invoke-direct {p1, p0}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;-><init>(Landroid/view/View;)V

    return-object p1

    :cond_1
    const/4 v0, 0x6

    if-ne p2, v0, :cond_4

    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-static {p0}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;

    move-result-object p0

    invoke-virtual {p0, v1, p1, v5}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object p0

    const/4 p1, 0x4

    invoke-virtual {p0, p1}, Landroid/view/View;->setVisibility(I)V

    new-instance p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    invoke-direct {p1, p0}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;-><init>(Landroid/view/View;)V

    return-object p1

    :cond_2
    if-eqz v0, :cond_3

    if-ne v0, v2, :cond_4

    :cond_3
    if-ne p2, v4, :cond_4

    new-instance p1, Landroid/view/View;

    iget-object p2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-direct {p1, p2}, Landroid/view/View;-><init>(Landroid/content/Context;)V

    new-instance p2, Landroidx/recyclerview/widget/RecyclerView$LayoutParams;

    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    iget v1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    iget-object v2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mMoreMode:Lcom/android/camera/fragment/mode/IMoreMode;

    invoke-interface {v2}, Lcom/android/camera/fragment/mode/IMoreMode;->getCountPerLine()I

    move-result v2

    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mItems:Ljava/util/List;

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result p0

    invoke-static {v0, v1, v2, p0}, Lcom/android/camera/fragment/mode/MoreModeHelper;->getHeaderHeightForNormal(Landroid/content/Context;III)I

    move-result p0

    invoke-direct {p2, v4, p0}, Landroidx/recyclerview/widget/RecyclerView$LayoutParams;-><init>(II)V

    invoke-virtual {p1, p2}, Landroid/view/View;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    new-instance p0, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;-><init>(Landroid/view/View;)V

    return-object p0

    :cond_4
    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    if-ne v0, v2, :cond_5

    const v1, 0x7f0c00e1

    :cond_5
    iget-object v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mContext:Landroid/content/Context;

    invoke-static {v0}, Landroid/view/LayoutInflater;->from(Landroid/content/Context;)Landroid/view/LayoutInflater;

    move-result-object v0

    invoke-virtual {v0, v1, p1, v5}, Landroid/view/LayoutInflater;->inflate(ILandroid/view/ViewGroup;Z)Landroid/view/View;

    move-result-object p1

    check-cast p1, Landroid/view/ViewGroup;

    new-instance v0, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    invoke-direct {v0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;-><init>(Landroid/view/View;)V

    const/4 v1, 0x7

    if-ne p2, v1, :cond_8

    iget p2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    if-ne p2, v2, :cond_6

    invoke-direct {p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter;->addWaterBox(Landroid/view/ViewGroup;)Lcom/android/camera/ui/WaterBox;

    move-result-object p2

    :goto_0
    invoke-virtual {v0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->setProgressView(Landroid/view/View;)V

    goto :goto_1

    :cond_6
    if-eqz p2, :cond_7

    if-ne p2, v4, :cond_8

    :cond_7
    const p2, 0x7f090249

    invoke-virtual {p1, p2}, Landroid/view/ViewGroup;->findViewById(I)Landroid/view/View;

    move-result-object p2

    check-cast p2, Landroid/view/ViewGroup;

    invoke-direct {p0, p2}, Lcom/android/camera/fragment/mode/ModeAdapter;->addModeBackground(Landroid/view/ViewGroup;)Lcom/android/camera/ui/ModeBackground;

    move-result-object p2

    goto :goto_0

    :cond_8
    :goto_1
    iget p2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mFragmentType:I

    if-ne p2, v3, :cond_9

    iget-object p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mClickListener:Landroid/view/View$OnClickListener;

    invoke-virtual {p1, p0}, Landroid/view/ViewGroup;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    goto :goto_2

    :cond_9
    iget-object p2, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mClickListener:Landroid/view/View$OnClickListener;

    invoke-virtual {p1, p2}, Landroid/view/ViewGroup;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    iget p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mDegree:F

    invoke-virtual {v0, p0}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->setRotation(F)V

    new-array p0, v4, [Landroid/view/View;

    iget-object p2, v0, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->mIconLayout:Landroid/widget/FrameLayout;

    aput-object p2, p0, v5

    invoke-static {p0}, Lmiuix/animation/Folme;->useAt([Landroid/view/View;)Lmiuix/animation/IFolme;

    move-result-object p0

    invoke-interface {p0}, Lmiuix/animation/IFolme;->touch()Lmiuix/animation/ITouchStyle;

    move-result-object p0

    new-array p2, v5, [Lmiuix/animation/base/AnimConfig;

    invoke-interface {p0, p1, p2}, Lmiuix/animation/ITouchStyle;->handleTouchOf(Landroid/view/View;[Lmiuix/animation/base/AnimConfig;)V

    :goto_2
    return-object v0
.end method

.method public bridge synthetic onViewAttachedToWindow(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)V
    .locals 0
    .param p1    # Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    check-cast p1, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;

    invoke-virtual {p0, p1}, Lcom/android/camera/fragment/mode/ModeAdapter;->onViewAttachedToWindow(Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;)V

    return-void
.end method

.method public onViewAttachedToWindow(Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;)V
    .locals 0
    .param p1    # Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;
        .annotation build Landroidx/annotation/NonNull;
        .end annotation
    .end param

    invoke-super {p0, p1}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->onViewAttachedToWindow(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)V

    iget p0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mDegree:F

    invoke-virtual {p1, p0}, Lcom/android/camera/fragment/mode/ModeAdapter$ModeViewHolder;->setRotation(F)V

    return-void
.end method

.method public setAnimFlags(I)V
    .locals 1

    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mAnimFlags:I

    or-int/2addr p1, v0

    iput p1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mAnimFlags:I

    return-void
.end method

.method public setRotate(I)V
    .locals 1

    const v0, 0x3faaaaaa

    invoke-static {v0}, Lcom/android/camera/Display;->fitDisplayFull(F)Z

    move-result v0

    if-nez v0, :cond_0

    int-to-float p1, p1

    iput p1, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mDegree:F

    goto :goto_0

    :cond_0
    iget v0, p0, Lcom/android/camera/fragment/mode/ModeAdapter;->mDegree:F

    int-to-float p1, p1

    cmpl-float p1, v0, p1

    if-eqz p1, :cond_1

    invoke-virtual {p0}, Landroidx/recyclerview/widget/RecyclerView$Adapter;->notifyDataSetChanged()V

    :cond_1
    :goto_0
    return-void
.end method

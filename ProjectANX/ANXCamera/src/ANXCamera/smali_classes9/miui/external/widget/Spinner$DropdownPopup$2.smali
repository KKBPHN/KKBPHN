.class Lmiui/external/widget/Spinner$DropdownPopup$2;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Landroid/widget/PopupWindow$OnDismissListener;


# instance fields
.field final synthetic this$1:Lmiui/external/widget/Spinner$DropdownPopup;


# direct methods
.method constructor <init>(Lmiui/external/widget/Spinner$DropdownPopup;)V
    .locals 0

    iput-object p1, p0, Lmiui/external/widget/Spinner$DropdownPopup$2;->this$1:Lmiui/external/widget/Spinner$DropdownPopup;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onDismiss()V
    .locals 0

    iget-object p0, p0, Lmiui/external/widget/Spinner$DropdownPopup$2;->this$1:Lmiui/external/widget/Spinner$DropdownPopup;

    iget-object p0, p0, Lmiui/external/widget/Spinner$DropdownPopup;->this$0:Lmiui/external/widget/Spinner;

    invoke-static {p0}, Lmiui/external/widget/Spinner;->access$200(Lmiui/external/widget/Spinner;)V

    return-void
.end method

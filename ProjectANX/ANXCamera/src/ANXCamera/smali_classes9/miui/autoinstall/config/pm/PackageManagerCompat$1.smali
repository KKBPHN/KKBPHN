.class Lmiui/autoinstall/config/pm/PackageManagerCompat$1;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Landroid/content/ServiceConnection;


# instance fields
.field final synthetic this$0:Lmiui/autoinstall/config/pm/PackageManagerCompat;


# direct methods
.method constructor <init>(Lmiui/autoinstall/config/pm/PackageManagerCompat;)V
    .locals 0

    iput-object p1, p0, Lmiui/autoinstall/config/pm/PackageManagerCompat$1;->this$0:Lmiui/autoinstall/config/pm/PackageManagerCompat;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onServiceConnected(Landroid/content/ComponentName;Landroid/os/IBinder;)V
    .locals 0

    iget-object p0, p0, Lmiui/autoinstall/config/pm/PackageManagerCompat$1;->this$0:Lmiui/autoinstall/config/pm/PackageManagerCompat;

    invoke-static {p2}, LO00000Oo/O00000o/O000000o/O00000o0;->asInterface(Landroid/os/IBinder;)LO00000Oo/O00000o/O000000o/O00000o;

    move-result-object p1

    invoke-static {p0, p1}, Lmiui/autoinstall/config/pm/PackageManagerCompat;->access$002(Lmiui/autoinstall/config/pm/PackageManagerCompat;LO00000Oo/O00000o/O000000o/O00000o;)LO00000Oo/O00000o/O000000o/O00000o;

    return-void
.end method

.method public onServiceDisconnected(Landroid/content/ComponentName;)V
    .locals 0

    iget-object p0, p0, Lmiui/autoinstall/config/pm/PackageManagerCompat$1;->this$0:Lmiui/autoinstall/config/pm/PackageManagerCompat;

    const/4 p1, 0x0

    invoke-static {p0, p1}, Lmiui/autoinstall/config/pm/PackageManagerCompat;->access$002(Lmiui/autoinstall/config/pm/PackageManagerCompat;LO00000Oo/O00000o/O000000o/O00000o;)LO00000Oo/O00000o/O000000o/O00000o;

    return-void
.end method

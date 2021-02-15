.class public LO00000Oo/O00000oO/O000000o/O00000oO;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final IS_HONGMI:Z

.field private static final IS_MTK_PLATFORM:Ljava/util/concurrent/atomic/AtomicReference;

.field public static final IS_XIAOMI:Z

.field public static final MODULE:Ljava/lang/String;

.field public static final O0Ooo:Ljava/lang/String; = "qcom"

.field public static final O0Ooo0o:Ljava/lang/String;

.field private static final O0OooO:I = 0x64

.field public static final O0OooO0:Ljava/lang/String; = "mediatek"

.field public static final O0OooOO:Z

.field public static final O0OooOo:Z

.field public static final O0Oooo0:Z

.field public static final O0OoooO:Z

.field public static final O0Ooooo:Z

.field public static final O0o:Z

.field public static final O0o0:Z

.field public static final O0o00:Z

.field public static final O0o000:Z

.field public static final O0o0000:Z

.field public static final O0o000O:Z

.field public static final O0o000o:Z

.field public static final O0o00O:Z

.field public static final O0o00O0:Z

.field public static final O0o00OO:Z

.field public static final O0o00Oo:Z

.field public static final O0o00o:Z

.field public static final O0o00o0:Z

.field public static final O0o00oO:Z

.field public static final O0o00oo:Z

.field public static final O0o0O:Z

.field public static final O0o0O0:Z

.field public static final O0o0O00:Z

.field public static final O0o0O0O:Z

.field public static final O0o0O0o:Z

.field public static final O0o0OO:Z

.field public static final O0o0OO0:Z

.field public static final O0o0OOO:Z

.field public static final O0o0OOo:Z

.field public static final O0o0Oo:Z

.field public static final O0o0Oo0:Z

.field public static final O0o0OoO:Z

.field public static final O0o0Ooo:Z

.field public static final O0o0o0:Z

.field public static final O0o0o00:Z

.field public static final O0o0o0O:Z

.field public static final O0o0o0o:Z

.field public static final O0o0oO:Z

.field public static final O0o0oO0:Z

.field public static final O0o0oOO:Z

.field public static final O0o0oOo:Z

.field public static final O0o0oo:Z

.field public static final O0o0ooO:Z

.field private static O0oO0:Ljava/util/ArrayList; = null

.field public static final O0oO00:Z

.field public static final O0oO000:Z

.field private static final O0oO00O:I = 0x4

.field private static final O0oO00o:I = 0x8

.field private static final O0oO0O:Ljava/lang/String; = "ro.boot.hwversion"

.field private static final O0oO0O0:[Ljava/lang/String;

.field private static final O0oO0oO:I = 0x1

.field public static final O0oo0o:Z

.field public static final oOOoOO:Z


# direct methods
.method static constructor <clinit>()V
    .locals 5

    sget-object v0, Lmiui/os/Build;->DEVICE:Ljava/lang/String;

    sput-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    sget-object v0, Lmiui/os/Build;->MODEL:Ljava/lang/String;

    sput-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->MODULE:Ljava/lang/String;

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "beryllium"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0OooOO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "lavender"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0OooOo:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string/jumbo v1, "violet"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Oooo0:Z

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOoooo0()Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_HONGMI:Z

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0O0OO()Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_XIAOMI:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "polaris"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0OoooO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "sirius"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooooo:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "dipper"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0000:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "andromeda"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o000:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "perseus"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o000O:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "cepheus"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o000o:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "raphael"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "grus"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00O0:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "begonia"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00O:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "phoenix"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/4 v1, 0x0

    const/4 v2, 0x1

    if-nez v0, :cond_1

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "phoenixin"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    move v0, v1

    goto :goto_1

    :cond_1
    :goto_0
    move v0, v2

    :goto_1
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00OO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "begoniain"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00Oo:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "ginkgo"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00o0:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "pyxis"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00o:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string/jumbo v3, "vela"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00oO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "laurus"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00oo:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "laurel_sprout"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "tucana"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O00:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "umi"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O0:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "cmi"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O0O:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "cas"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O0o:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "apollo"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_3

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "apolloin"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_2

    goto :goto_2

    :cond_2
    move v0, v1

    goto :goto_3

    :cond_3
    :goto_2
    move v0, v2

    :goto_3
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "atom"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_5

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "apricot"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_4

    goto :goto_4

    :cond_4
    move v0, v1

    goto :goto_5

    :cond_5
    :goto_4
    move v0, v2

    :goto_5
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OO0:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "bomb"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_7

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "banana"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_6

    goto :goto_6

    :cond_6
    move v0, v1

    goto :goto_7

    :cond_7
    :goto_6
    move v0, v2

    :goto_7
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oo0o:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "lmi"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_9

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "lmiin"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_8

    goto :goto_8

    :cond_8
    move v0, v1

    goto :goto_9

    :cond_9
    :goto_8
    move v0, v2

    :goto_9
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "lmipro"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_b

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "lmiinpro"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_a

    goto :goto_a

    :cond_a
    move v0, v1

    goto :goto_b

    :cond_b
    :goto_a
    move v0, v2

    :goto_b
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OOO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "draco"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OOo:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "picasso"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_d

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v4, "picassoin"

    invoke-virtual {v4, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_c

    goto :goto_c

    :cond_c
    move v0, v1

    goto :goto_d

    :cond_d
    :goto_c
    move v0, v2

    :goto_d
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0Oo0:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v4, "monet"

    invoke-virtual {v4, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_f

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v4, "monetin"

    invoke-virtual {v4, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_e

    goto :goto_e

    :cond_e
    move v0, v1

    goto :goto_f

    :cond_f
    :goto_e
    move v0, v2

    :goto_f
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0Oo:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string/jumbo v4, "vangogh"

    invoke-virtual {v4, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OoO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0Ooo:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "gauguin"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_11

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "gauguinpro"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_11

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "gauguininpro"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_10

    goto :goto_10

    :cond_10
    move v0, v1

    goto :goto_11

    :cond_11
    :goto_10
    move v0, v2

    :goto_11
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0o00:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "cezanne"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0o0:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "crux"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0o0O:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "curtana"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_13

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "durandal"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_13

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "excalibur"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_13

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "joyeuse"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_13

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "gram"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_12

    goto :goto_12

    :cond_12
    move v0, v1

    goto :goto_13

    :cond_13
    :goto_12
    move v0, v2

    :goto_13
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0o0o:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "dandelion"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0oO0:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "angelica"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_15

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "angelican"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_15

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "angelicain"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_15

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "cattail"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_14

    goto :goto_14

    :cond_14
    move v0, v1

    goto :goto_15

    :cond_15
    :goto_14
    move v0, v2

    :goto_15
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0oO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "merlin"

    invoke-static {v0, v3}, Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_17

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "merlinnfc"

    invoke-static {v0, v3}, Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_16

    goto :goto_16

    :cond_16
    move v0, v1

    goto :goto_17

    :cond_17
    :goto_16
    move v0, v2

    :goto_17
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0oOO:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "cannon"

    invoke-static {v0, v3}, Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_19

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "cannong"

    invoke-static {v0, v3}, Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_18

    goto :goto_18

    :cond_18
    move v0, v1

    goto :goto_19

    :cond_19
    :goto_18
    move v0, v2

    :goto_19
    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0oOo:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "lime"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1a

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "lemon"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1a

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "pomelo"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-nez v0, :cond_1a

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v3, "citrus"

    invoke-virtual {v3, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1b

    :cond_1a
    move v1, v2

    :cond_1b
    sput-boolean v1, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0oo:Z

    sget-boolean v0, Lmiui/os/Build;->IS_STABLE_VERSION:Z

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0ooO:Z

    sget-boolean v0, Lmiui/os/Build;->IS_CM_CUSTOMIZATION_TEST:Z

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->oOOoOO:Z

    sget-boolean v0, Lmiui/os/Build;->IS_CM_CUSTOMIZATION:Z

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string/jumbo v1, "venus"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oO000:Z

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "haydn"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    sput-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oO00:Z

    const-string v0, "KR"

    const-string v1, "JP"

    filled-new-array {v0, v1}, [Ljava/lang/String;

    move-result-object v0

    sput-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oO0O0:[Ljava/lang/String;

    new-instance v0, Ljava/util/concurrent/atomic/AtomicReference;

    invoke-static {}, Ljava/util/Optional;->empty()Ljava/util/Optional;

    move-result-object v1

    invoke-direct {v0, v1}, Ljava/util/concurrent/atomic/AtomicReference;-><init>(Ljava/lang/Object;)V

    sput-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_MTK_PLATFORM:Ljava/util/concurrent/atomic/AtomicReference;

    return-void
.end method

.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static O000O0o(Z)Z
    .locals 3

    const-string v0, "ro.miui.customized.region"

    invoke-static {v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    const-string v1, "fr_sfr"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    const/4 v2, 0x0

    if-nez v1, :cond_2

    const-string v1, "fr_orange"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_2

    const-string v1, "jp_kd"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_0

    :cond_0
    const-string v1, "es_vodafone"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->getCountry()Ljava/lang/String;

    move-result-object v0

    const-string v1, "NL"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1

    return v2

    :cond_1
    return p0

    :cond_2
    :goto_0
    return v2
.end method

.method private static O000Ooo0(Ljava/lang/String;)Z
    .locals 5

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oO0O0:[Ljava/lang/String;

    array-length v1, v0

    const/4 v2, 0x0

    move v3, v2

    :goto_0
    if-ge v3, v1, :cond_1

    aget-object v4, v0, v3

    invoke-static {p0, v4}, Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z

    move-result v4

    if-eqz v4, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_1
    return v2
.end method

.method public static OOO0Oo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO0Oo()Z

    move-result v0

    return v0
.end method

.method public static OOOOOo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOOOo()Z

    move-result v0

    return v0
.end method

.method public static OOo0oOo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo0oOo()Z

    move-result v0

    return v0
.end method

.method public static OOoOooO()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->oOOoOO:Z

    if-nez v0, :cond_0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoOooO()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOoOooo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoOooo()Z

    move-result v0

    return v0
.end method

.method public static OOoo()Z
    .locals 2

    const-string v0, "ro.boot.hwversion"

    invoke-static {v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    sget-boolean v1, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0o0O:Z

    if-eqz v1, :cond_1

    const-string v1, "7.1.7"

    invoke-static {v0, v1}, Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_0

    const-string v1, "7.2.0"

    invoke-static {v0, v1}, Landroid/text/TextUtils;->equals(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z

    move-result v0

    if-eqz v0, :cond_1

    :cond_0
    const/4 v0, 0x1

    goto :goto_0

    :cond_1
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOoo0()[Ljava/lang/String;
    .locals 2

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoo0()Ljava/lang/String;

    move-result-object v0

    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v0

    return-object v0
.end method

.method public static OOoo000()F
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoo000()F

    move-result v0

    return v0
.end method

.method public static OOoo00O()I
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoo00O()I

    move-result v0

    return v0
.end method

.method public static OOoo00o()Ljava/util/ArrayList;
    .locals 2

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oO0:Ljava/util/ArrayList;

    if-nez v0, :cond_0

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    sput-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oO0:Ljava/util/ArrayList;

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOoo0()[Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_0

    sget-object v1, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oO0:Ljava/util/ArrayList;

    invoke-static {v1, v0}, Ljava/util/Collections;->addAll(Ljava/util/Collection;[Ljava/lang/Object;)Z

    :cond_0
    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0oO0:Ljava/util/ArrayList;

    return-object v0
.end method

.method public static OOoo0O()[I
    .locals 4

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoo0O()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    const/4 v2, 0x0

    if-eqz v1, :cond_0

    new-array v0, v2, [I

    return-object v0

    :cond_0
    const-string v1, ","

    invoke-virtual {v0, v1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v0

    array-length v1, v0

    new-array v1, v1, [I

    :goto_0
    array-length v3, v0

    if-ge v2, v3, :cond_1

    aget-object v3, v0, v2

    invoke-static {v3}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v3

    aput v3, v1, v2

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_1
    return-object v1
.end method

.method public static OOoo0O0()I
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoo0O0()I

    move-result v0

    return v0
.end method

.method public static OOoo0OO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoo0OO()Z

    move-result v0

    return v0
.end method

.method public static OOoo0Oo()Z
    .locals 2

    invoke-static {}, Lcom/android/camera/Display;->getWindowHeight()I

    move-result v0

    int-to-float v0, v0

    invoke-static {}, Lcom/android/camera/Display;->getWindowWidth()I

    move-result v1

    int-to-float v1, v1

    div-float/2addr v0, v1

    const/high16 v1, 0x40000000    # 2.0f

    cmpl-float v0, v0, v1

    if-ltz v0, :cond_0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoo0Oo()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOoo0o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo000o()Z

    move-result v0

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00OO:Z

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, 0x1

    :goto_1
    return v0
.end method

.method public static OOoo0o0()Z
    .locals 2

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0Oo0o()Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    return v1

    :cond_0
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO00oO()Z

    move-result v0

    if-nez v0, :cond_1

    return v1

    :cond_1
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOoo00o()Ljava/util/ArrayList;

    move-result-object v0

    if-eqz v0, :cond_2

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOoo00o()Ljava/util/ArrayList;

    move-result-object v0

    invoke-virtual {v0}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v0

    if-nez v0, :cond_2

    const/4 v0, 0x1

    return v0

    :cond_2
    return v1
.end method

.method public static OOoo0oO()Z
    .locals 1

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->getCountry()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, LO00000Oo/O00000oO/O000000o/O00000oO;->O000Ooo0(Ljava/lang/String;)Z

    move-result v0

    return v0

    :cond_0
    const/4 v0, 0x0

    return v0
.end method

.method public static OOoo0oo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoo0oo()Z

    move-result v0

    return v0
.end method

.method public static OOooO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooOoO()Z

    move-result v0

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00o:Z

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00O0:Z

    if-nez v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOo000o()Z

    move-result v0

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, 0x1

    :goto_1
    return v0
.end method

.method public static OOooO0()Z
    .locals 2

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0OooOo:Z

    if-eqz v0, :cond_0

    const-string v0, "ro.boot.hwc"

    invoke-static {v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    const-string v1, "India"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooO00()Z
    .locals 3

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "onc"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    const-string v0, "ro.boot.hwversion"

    invoke-static {v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v2

    if-nez v2, :cond_0

    const/16 v2, 0x32

    invoke-virtual {v0, v1}, Ljava/lang/String;->charAt(I)C

    move-result v0

    if-ne v2, v0, :cond_0

    const/4 v1, 0x1

    :cond_0
    return v1
.end method

.method public static OOooO0O()Z
    .locals 2

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0OooOo:Z

    if-eqz v0, :cond_0

    const-string v0, "ro.boot.hwc"

    invoke-static {v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    const-string v1, "India_48_5"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooO0o()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0OoooO:Z

    return v0
.end method

.method public static OOooOO()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00O:Z

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00Oo:Z

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, 0x1

    :goto_1
    return v0
.end method

.method public static OOooOO0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOooOO0()Z

    move-result v0

    return v0
.end method

.method public static OOooOOO()Z
    .locals 2

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0Ooo:Z

    if-nez v0, :cond_0

    const/4 v0, 0x0

    return v0

    :cond_0
    const-string v0, "ro.product.name"

    invoke-static {v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    const-string v1, "picasso_48m"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    return v0
.end method

.method public static OOooOOo()Z
    .locals 6

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0Ooo:Z

    const/4 v1, 0x0

    if-nez v0, :cond_0

    return v1

    :cond_0
    const-string v0, "3.9.3"

    const-string v2, "3.9.5"

    filled-new-array {v0, v2}, [Ljava/lang/String;

    move-result-object v0

    const-string v2, "ro.boot.hwversion"

    invoke-static {v2}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    array-length v3, v0

    move v4, v1

    :goto_0
    if-ge v4, v3, :cond_2

    aget-object v5, v0, v4

    invoke-virtual {v5, v2}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v5

    if-eqz v5, :cond_1

    const/4 v0, 0x1

    return v0

    :cond_1
    add-int/lit8 v4, v4, 0x1

    goto :goto_0

    :cond_2
    return v1
.end method

.method public static OOooOo0()Z
    .locals 2

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "equuleus"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooOoO()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o000O:Z

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooOoo()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o000o:Z

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooo()Z
    .locals 2

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "toco"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    return v0
.end method

.method public static OOooo0()Z
    .locals 2

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "raphael"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooo00()Z
    .locals 2

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "davinci"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooo0O()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o00o:Z

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooo0o()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O00:Z

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOoooO()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0oOO:Z

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOoooO0()Z
    .locals 2

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "lmi"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOoooOO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoooOO()Z

    move-result v0

    return v0
.end method

.method public static OOoooOo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoooOo()Z

    move-result v0

    return v0
.end method

.method public static OOoooo0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOoooo0()Z

    move-result v0

    return v0
.end method

.method public static OOooooO()Z
    .locals 2

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0OooOO:Z

    if-eqz v0, :cond_0

    const-string v0, "ro.boot.hwc"

    invoke-static {v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    const-string v1, "India"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static OOooooo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->OOooooo()Z

    move-result v0

    return v0
.end method

.method private static Oo()Z
    .locals 2

    const-string v0, "ro.hardware.fp.fod"

    const/4 v1, 0x0

    invoke-static {v0, v1}, Landroid/os/SystemProperties;->getBoolean(Ljava/lang/String;Z)Z

    move-result v0

    return v0
.end method

.method public static Oo0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0()Z

    move-result v0

    return v0
.end method

.method public static Oo00()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00()Z

    move-result v0

    return v0
.end method

.method public static Oo000()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo000()Z

    move-result v0

    return v0
.end method

.method public static Oo00000()Z
    .locals 2

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0o0o:Z

    if-eqz v0, :cond_0

    const-string v0, "ro.boot.hwc"

    invoke-static {v0}, Landroid/os/SystemProperties;->get(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    const-string v1, "India"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static Oo0000O()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OO:Z

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OOO:Z

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, 0x1

    :goto_1
    return v0
.end method

.method public static Oo0000o()Z
    .locals 3

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O00:Z

    if-nez v0, :cond_0

    const/4 v0, 0x0

    return v0

    :cond_0
    sget v0, Landroid/os/Build$VERSION;->SDK_INT:I

    const/16 v1, 0x1d

    const-string v2, "03"

    if-lt v0, v1, :cond_1

    const-string v0, "persist.vendor.camera.rearMain.vendorID"

    :goto_0
    invoke-static {v0, v2}, Landroid/os/SystemProperties;->get(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    return v0

    :cond_1
    const-string v0, "persist.camera.rearMain.vendorID"

    goto :goto_0
.end method

.method public static Oo000O()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O00:Z

    if-eqz v0, :cond_0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0000o()Z

    move-result v0

    if-nez v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static Oo000O0()Z
    .locals 2

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0Ooo0o:Ljava/lang/String;

    const-string v1, "raphael"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    sget-object v0, Lmiui/os/Build;->MODEL:Ljava/lang/String;

    const-string v1, "Premium Edition"

    invoke-virtual {v0, v1}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static Oo000OO()Z
    .locals 2

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0O()Ljava/lang/String;

    move-result-object v0

    const-string v1, "qcom"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    return v0
.end method

.method public static Oo000Oo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo000Oo()Z

    move-result v0

    return v0
.end method

.method public static Oo000o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo000o()Z

    move-result v0

    return v0
.end method

.method public static Oo000o0()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O0O:Z

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0O0:Z

    if-nez v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0000O()Z

    move-result v0

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0o0o:Z

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0o00:Z

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0Oo:Z

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0OoO:Z

    if-nez v0, :cond_1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0Oo0:Z

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, 0x1

    :goto_1
    return v0
.end method

.method public static Oo000oO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo000oO()Z

    move-result v0

    return v0
.end method

.method public static Oo000oo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo000oo()Z

    move-result v0

    return v0
.end method

.method public static Oo00O()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo000oo()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo00O0o()Z

    move-result v0

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, 0x1

    :goto_1
    return v0
.end method

.method public static Oo00O0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00O0()Z

    move-result v0

    return v0
.end method

.method public static Oo00O00()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00O00()Z

    move-result v0

    return v0
.end method

.method public static Oo00O0O()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00O0O()Z

    move-result v0

    return v0
.end method

.method public static Oo00O0o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00O0o()Z

    move-result v0

    return v0
.end method

.method public static Oo00OO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0O0o()I

    move-result v0

    and-int/lit8 v0, v0, 0xd

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static Oo00OO0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00OO0()Z

    move-result v0

    return v0
.end method

.method public static Oo00OOO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00OOO()Z

    move-result v0

    return v0
.end method

.method public static Oo00OOo()Z
    .locals 2

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0O0o()I

    move-result v0

    const/4 v1, 0x1

    and-int/2addr v0, v1

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v1, 0x0

    :goto_0
    return v1
.end method

.method public static Oo00Oo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00Oo()Z

    move-result v0

    return v0
.end method

.method public static Oo00Oo0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00Oo0()Z

    move-result v0

    return v0
.end method

.method public static Oo00OoO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00OoO()Z

    move-result v0

    return v0
.end method

.method public static Oo00Ooo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00Ooo()Z

    move-result v0

    return v0
.end method

.method public static Oo00o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00o()Z

    move-result v0

    return v0
.end method

.method public static Oo00o0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooO00()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x0

    return v0

    :cond_0
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00o0()Z

    move-result v0

    return v0
.end method

.method public static Oo00o00()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00o00()Z

    move-result v0

    return v0
.end method

.method public static Oo00o0O()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOoo0oO()Z

    move-result v0

    xor-int/lit8 v0, v0, 0x1

    return v0
.end method

.method public static Oo00o0o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00o0o()Z

    move-result v0

    return v0
.end method

.method public static Oo00oO0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00oO0()Z

    move-result v0

    return v0
.end method

.method public static Oo00oOO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00oOO()Z

    move-result v0

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static Oo00oo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00oo()Z

    move-result v0

    return v0
.end method

.method public static Oo00oo0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00oo0()Z

    move-result v0

    return v0
.end method

.method public static Oo00ooO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00ooO()Z

    move-result v0

    return v0
.end method

.method public static Oo00ooo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo00ooo()Z

    move-result v0

    return v0
.end method

.method public static Oo0O()Ljava/lang/String;
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O()Ljava/lang/String;

    move-result-object v0

    return-object v0
.end method

.method public static Oo0O0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O0()Z

    move-result v0

    return v0
.end method

.method public static Oo0O00()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O00()Z

    move-result v0

    return v0
.end method

.method public static Oo0O000()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O000()Z

    move-result v0

    return v0
.end method

.method public static Oo0O00O()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O00O()Z

    move-result v0

    return v0
.end method

.method public static Oo0O00o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O00o()Z

    move-result v0

    return v0
.end method

.method public static Oo0O0O()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O0O()Z

    move-result v0

    return v0
.end method

.method public static Oo0O0O0()Z
    .locals 1

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_XIAOMI:Z

    if-nez v0, :cond_0

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_HONGMI:Z

    if-nez v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static Oo0O0OO()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O0OO()Z

    move-result v0

    return v0
.end method

.method public static Oo0O0Oo()Z
    .locals 4

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0OooO()Z

    move-result v0

    if-eqz v0, :cond_0

    sget-wide v0, Lcom/android/camera/Util;->TOTAL_MEMORY_GB:J

    const-wide/16 v2, 0x6

    cmp-long v0, v0, v2

    if-gez v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static Oo0O0o()I
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O0o()I

    move-result v0

    return v0
.end method

.method public static Oo0O0o0()I
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0O0o0()I

    move-result v0

    return v0
.end method

.method public static Oo0O0oO()Z
    .locals 2

    const/4 v0, -0x1

    const-string v1, "ro.boot.camera.config"

    invoke-static {v1, v0}, Landroid/os/SystemProperties;->getInt(Ljava/lang/String;I)I

    move-result v1

    if-eq v1, v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static Oo0O0oo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOoo0O()[I

    move-result-object v0

    if-eqz v0, :cond_0

    array-length v0, v0

    if-lez v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method private static Oo0Oo0o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0Oo0o()Z

    move-result v0

    if-nez v0, :cond_1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo()Z

    move-result v0

    if-eqz v0, :cond_0

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    goto :goto_1

    :cond_1
    :goto_0
    const/4 v0, 0x1

    :goto_1
    return v0
.end method

.method public static Oo0o0O0()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0o0O0()Z

    move-result v0

    return v0
.end method

.method public static Oo0o0Oo()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->Oo0o0Oo()Z

    move-result v0

    return v0
.end method

.method public static Oo0o0o()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OO0oO0o()Z

    move-result v0

    if-nez v0, :cond_0

    sget-boolean v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_HONGMI:Z

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    goto :goto_0

    :cond_0
    const/4 v0, 0x0

    :goto_0
    return v0
.end method

.method public static getCountry()Ljava/lang/String;
    .locals 2

    sget-object v0, Lcom/android/camera/Util;->sRegion:Ljava/lang/String;

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_0

    return-object v0

    :cond_0
    invoke-static {}, Ljava/util/Locale;->getDefault()Ljava/util/Locale;

    move-result-object v0

    invoke-virtual {v0}, Ljava/util/Locale;->getCountry()Ljava/lang/String;

    move-result-object v0

    return-object v0
.end method

.method public static isGlobal()Z
    .locals 1

    sget-boolean v0, Lmiui/os/Build;->IS_INTERNATIONAL_BUILD:Z

    return v0
.end method

.method public static isMTKPlatform()Z
    .locals 4

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_MTK_PLATFORM:Ljava/util/concurrent/atomic/AtomicReference;

    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/Optional;

    invoke-virtual {v0}, Ljava/util/Optional;->isPresent()Z

    move-result v0

    if-nez v0, :cond_1

    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_MTK_PLATFORM:Ljava/util/concurrent/atomic/AtomicReference;

    monitor-enter v0

    :try_start_0
    sget-object v1, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_MTK_PLATFORM:Ljava/util/concurrent/atomic/AtomicReference;

    invoke-virtual {v1}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/util/Optional;

    invoke-virtual {v1}, Ljava/util/Optional;->isPresent()Z

    move-result v1

    if-nez v1, :cond_0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->Oo0O()Ljava/lang/String;

    move-result-object v1

    sget-object v2, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_MTK_PLATFORM:Ljava/util/concurrent/atomic/AtomicReference;

    const-string v3, "mediatek"

    invoke-virtual {v3, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v1

    invoke-static {v1}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v1

    invoke-static {v1}, Ljava/util/Optional;->of(Ljava/lang/Object;)Ljava/util/Optional;

    move-result-object v1

    invoke-virtual {v2, v1}, Ljava/util/concurrent/atomic/AtomicReference;->set(Ljava/lang/Object;)V

    :cond_0
    monitor-exit v0

    goto :goto_0

    :catchall_0
    move-exception v1

    monitor-exit v0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    throw v1

    :cond_1
    :goto_0
    sget-object v0, LO00000Oo/O00000oO/O000000o/O00000oO;->IS_MTK_PLATFORM:Ljava/util/concurrent/atomic/AtomicReference;

    invoke-virtual {v0}, Ljava/util/concurrent/atomic/AtomicReference;->get()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/Optional;

    invoke-virtual {v0}, Ljava/util/Optional;->get()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Boolean;

    invoke-virtual {v0}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v0

    return v0
.end method

.method public static isPad()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->isPad()Z

    move-result v0

    return v0
.end method

.method public static isSupportSuperResolution()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->isSupportSuperResolution()Z

    move-result v0

    return v0
.end method

.method public static isSupportedOpticalZoom()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->isSupportedOpticalZoom()Z

    move-result v0

    return v0
.end method

.method public static o00OO00()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->o00OO00()Z

    move-result v0

    return v0
.end method

.method public static o00oO00()Z
    .locals 1

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->getConfig()Lcom/mi/device/Common;

    move-result-object v0

    invoke-virtual {v0}, Lcom/mi/device/Common;->o00oO00()Z

    move-result v0

    return v0
.end method

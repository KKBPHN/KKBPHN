.class public Lcom/android/camera/data/data/global/ModeDataFactory;
.super Ljava/lang/Object;
.source ""


# static fields
.field private static volatile singleton:Lcom/android/camera/data/data/global/ModeDataFactory;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static getInstance()Lcom/android/camera/data/data/global/ModeDataFactory;
    .locals 2

    sget-object v0, Lcom/android/camera/data/data/global/ModeDataFactory;->singleton:Lcom/android/camera/data/data/global/ModeDataFactory;

    if-nez v0, :cond_1

    const-class v0, Lcom/android/camera/data/data/global/ModeDataFactory;

    monitor-enter v0

    :try_start_0
    sget-object v1, Lcom/android/camera/data/data/global/ModeDataFactory;->singleton:Lcom/android/camera/data/data/global/ModeDataFactory;

    if-nez v1, :cond_0

    new-instance v1, Lcom/android/camera/data/data/global/ModeDataFactory;

    invoke-direct {v1}, Lcom/android/camera/data/data/global/ModeDataFactory;-><init>()V

    sput-object v1, Lcom/android/camera/data/data/global/ModeDataFactory;->singleton:Lcom/android/camera/data/data/global/ModeDataFactory;

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
    sget-object v0, Lcom/android/camera/data/data/global/ModeDataFactory;->singleton:Lcom/android/camera/data/data/global/ModeDataFactory;

    return-object v0
.end method

.method private ultraPixelModuleName()Ljava/lang/String;
    .locals 1

    sget-boolean p0, LO00000Oo/O00000oO/O000000o/O00000oO;->O0o0Ooo:Z

    if-nez p0, :cond_0

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object p0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOoOOO()Ljava/lang/String;

    move-result-object p0

    return-object p0

    :cond_0
    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000oO;->OOooOOO()Z

    move-result p0

    if-eqz p0, :cond_1

    const-string p0, "48"

    goto :goto_0

    :cond_1
    const-string p0, "64"

    :goto_0
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object p0

    invoke-virtual {p0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOOoOOO()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method


# virtual methods
.method public createModeData(I)Lcom/android/camera/data/data/ComponentDataItem;
    .locals 3

    invoke-static {}, LO00000Oo/O00000oO/O000000o/O00000o;->instance()LO00000Oo/O00000oO/O000000o/O00000o;

    move-result-object v0

    invoke-static {p1}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    const/16 v2, 0xa6

    if-eq p1, v2, :cond_a

    const/16 v2, 0xa7

    if-eq p1, v2, :cond_9

    const/16 v2, 0xa9

    if-eq p1, v2, :cond_8

    const/16 v2, 0xb1

    if-eq p1, v2, :cond_7

    const/16 v2, 0xd6

    if-eq p1, v2, :cond_6

    const/16 v2, 0xb7

    if-eq p1, v2, :cond_5

    const/16 v2, 0xb8

    if-eq p1, v2, :cond_7

    const/16 v2, 0xcc

    if-eq p1, v2, :cond_3

    const/16 v0, 0xcd

    if-eq p1, v0, :cond_2

    const/16 v0, 0xfe

    const/4 v2, -0x1

    if-eq p1, v0, :cond_1

    const/16 v0, 0xff

    if-eq p1, v0, :cond_0

    packed-switch p1, :pswitch_data_0

    packed-switch p1, :pswitch_data_1

    packed-switch p1, :pswitch_data_2

    packed-switch p1, :pswitch_data_3

    new-instance p0, Ljava/lang/IllegalArgumentException;

    const-string p1, "unSupport mode."

    invoke-direct {p0, p1}, Ljava/lang/IllegalArgumentException;-><init>(Ljava/lang/String;)V

    throw p0

    :pswitch_0
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802c1

    const v0, 0x7f080546

    const v2, 0x7f100429

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_1
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802ab

    const v0, 0x7f080541

    const v2, 0x7f1003d7

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_2
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802c2

    const v0, 0x7f080550

    const v2, 0x7f1003ea

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_3
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802bd

    const v0, 0x7f08054f

    const v2, 0x7f1003e8

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_4
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802a9

    const v0, 0x7f080540

    const v2, 0x7f100406

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_5
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802ac

    const v0, 0x7f080542

    const v2, 0x7f100615

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_6
    new-instance p1, Lcom/android/camera/data/data/ComponentDataItem;

    invoke-static {}, Lcom/android/camera/data/data/config/ComponentRunningUltraPixel;->getUltraPixelIcon()I

    move-result v0

    const v2, 0x7f08054b

    invoke-direct {p0}, Lcom/android/camera/data/data/global/ModeDataFactory;->ultraPixelModuleName()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, v1, v0, v2, p0}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;IILjava/lang/String;)V

    return-object p1

    :pswitch_7
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802bc

    const v0, 0x7f08054e

    const v2, 0x7f1003e3

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_8
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802ba

    const v0, 0x7f08054c

    const v2, 0x7f1003e5

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_9
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802aa

    const v0, 0x7f1003d6

    invoke-direct {p0, v1, p1, v2, v0}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_a
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802c0

    const v0, 0x7f1003e9

    invoke-direct {p0, v1, p1, v2, v0}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_0
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802ae

    const v0, 0x7f080544

    const v2, 0x7f1003d8

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_1
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f1003e1

    invoke-direct {p0, v1, v2, v2, p1}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_2
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802b4

    const v0, 0x7f08053f

    const v2, 0x7f100126

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_3
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802ad

    const v2, 0x7f080543

    invoke-virtual {v0}, LO00000Oo/O00000oO/O000000o/O00000o;->OOO000o()Z

    move-result v0

    if-eqz v0, :cond_4

    const v0, 0x7f1003e2

    goto :goto_0

    :cond_4
    const v0, 0x7f100616

    :goto_0
    invoke-direct {p0, v1, p1, v2, v0}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_5
    :pswitch_b
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802b1

    const v0, 0x7f080547

    const v2, 0x7f1003de

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_6
    :pswitch_c
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802b5

    const v0, 0x7f080549

    const v2, 0x7f1005a5

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_7
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802b3

    const v0, 0x7f080548

    const v2, 0x7f1003df

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_8
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802be

    const v0, 0x7f080545

    const v2, 0x7f100693

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_9
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802bb

    const v0, 0x7f08054d

    const v2, 0x7f1003e6

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :cond_a
    new-instance p0, Lcom/android/camera/data/data/ComponentDataItem;

    const p1, 0x7f0802b6

    const v0, 0x7f08054a

    const v2, 0x7f1003e4

    invoke-direct {p0, v1, p1, v0, v2}, Lcom/android/camera/data/data/ComponentDataItem;-><init>(Ljava/lang/String;III)V

    return-object p0

    :pswitch_data_0
    .packed-switch 0xa1
        :pswitch_b
        :pswitch_a
        :pswitch_9
    .end packed-switch

    :pswitch_data_1
    .packed-switch 0xab
        :pswitch_8
        :pswitch_7
        :pswitch_c
        :pswitch_b
        :pswitch_6
    .end packed-switch

    :pswitch_data_2
    .packed-switch 0xba
        :pswitch_5
        :pswitch_4
        :pswitch_3
    .end packed-switch

    :pswitch_data_3
    .packed-switch 0xd1
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

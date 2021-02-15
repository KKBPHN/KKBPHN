.class final Lfr;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final a:Z

.field public static final b:Z

.field static final c:J

.field static final d:Z

.field private static final e:Ljava/util/logging/Logger;

.field private static final f:Lsun/misc/Unsafe;

.field private static final g:Ljava/lang/Class;

.field private static final h:Z

.field private static final i:Z

.field private static final j:Lfq;


# direct methods
.method static constructor <clinit>()V
    .locals 17

    const-class v0, Lfr;

    invoke-virtual {v0}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/util/logging/Logger;->getLogger(Ljava/lang/String;)Ljava/util/logging/Logger;

    move-result-object v0

    sput-object v0, Lfr;->e:Ljava/util/logging/Logger;

    invoke-static {}, Lfr;->a()Lsun/misc/Unsafe;

    move-result-object v0

    sput-object v0, Lfr;->f:Lsun/misc/Unsafe;

    sget-object v0, Lbx;->a:Ljava/lang/Class;

    sput-object v0, Lfr;->g:Ljava/lang/Class;

    sget-object v0, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    invoke-static {v0}, Lfr;->O00000o0(Ljava/lang/Class;)Z

    move-result v0

    sput-boolean v0, Lfr;->h:Z

    sget-object v0, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    invoke-static {v0}, Lfr;->O00000o0(Ljava/lang/Class;)Z

    move-result v0

    sput-boolean v0, Lfr;->i:Z

    sget-object v0, Lfr;->f:Lsun/misc/Unsafe;

    const/4 v1, 0x0

    if-eqz v0, :cond_2

    invoke-static {}, Lbx;->a()Z

    move-result v0

    if-nez v0, :cond_0

    new-instance v1, Lfp;

    sget-object v0, Lfr;->f:Lsun/misc/Unsafe;

    invoke-direct {v1, v0}, Lfp;-><init>(Lsun/misc/Unsafe;)V

    goto :goto_0

    :cond_0
    sget-boolean v0, Lfr;->h:Z

    if-eqz v0, :cond_1

    new-instance v1, Lfo;

    sget-object v0, Lfr;->f:Lsun/misc/Unsafe;

    invoke-direct {v1, v0}, Lfo;-><init>(Lsun/misc/Unsafe;)V

    goto :goto_0

    :cond_1
    sget-boolean v0, Lfr;->i:Z

    if-eqz v0, :cond_2

    new-instance v1, Lfn;

    sget-object v0, Lfr;->f:Lsun/misc/Unsafe;

    invoke-direct {v1, v0}, Lfn;-><init>(Lsun/misc/Unsafe;)V

    :cond_2
    :goto_0
    sput-object v1, Lfr;->j:Lfq;

    sget-object v0, Lfr;->f:Lsun/misc/Unsafe;

    const-string v1, "putLong"

    const-string v2, "putInt"

    const-string v3, "putByte"

    const-string v4, "getInt"

    const-string v5, "getByte"

    const-string v6, "objectFieldOffset"

    const-string v7, "com.google.protobuf.UnsafeUtil"

    const-string v8, "platform method missing - proto runtime falling back to safer methods: "

    const-string v9, "getLong"

    const/4 v10, 0x3

    const/4 v11, 0x2

    const/4 v12, 0x1

    const/4 v13, 0x0

    if-eqz v0, :cond_4

    :try_start_0
    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v0

    new-array v14, v12, [Ljava/lang/Class;

    const-class v15, Ljava/lang/reflect/Field;

    aput-object v15, v14, v13

    invoke-virtual {v0, v6, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v14, v11, [Ljava/lang/Class;

    const-class v15, Ljava/lang/Object;

    aput-object v15, v14, v13

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v12

    invoke-virtual {v0, v9, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    invoke-static {}, Lfr;->b()Ljava/lang/reflect/Field;

    move-result-object v14

    if-eqz v14, :cond_4

    invoke-static {}, Lbx;->a()Z

    move-result v14

    if-nez v14, :cond_3

    new-array v14, v12, [Ljava/lang/Class;

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v13

    invoke-virtual {v0, v5, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v14, v11, [Ljava/lang/Class;

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v13

    sget-object v15, Ljava/lang/Byte;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v12

    invoke-virtual {v0, v3, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v14, v12, [Ljava/lang/Class;

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v13

    invoke-virtual {v0, v4, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v14, v11, [Ljava/lang/Class;

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v13

    sget-object v15, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v12

    invoke-virtual {v0, v2, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v14, v12, [Ljava/lang/Class;

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v13

    invoke-virtual {v0, v9, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v14, v11, [Ljava/lang/Class;

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v13

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v12

    invoke-virtual {v0, v1, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v14, v10, [Ljava/lang/Class;

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v13

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v12

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v11

    const-string v15, "copyMemory"

    invoke-virtual {v0, v15, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v14, 0x5

    new-array v14, v14, [Ljava/lang/Class;

    const-class v15, Ljava/lang/Object;

    aput-object v15, v14, v13

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v12

    const-class v15, Ljava/lang/Object;

    aput-object v15, v14, v11

    sget-object v15, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v15, v14, v10

    const/4 v15, 0x4

    sget-object v16, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v16, v14, v15

    const-string v15, "copyMemory"

    invoke-virtual {v0, v15, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    :cond_3
    move v0, v12

    goto :goto_1

    :catchall_0
    move-exception v0

    sget-object v14, Lfr;->e:Ljava/util/logging/Logger;

    sget-object v15, Ljava/util/logging/Level;->WARNING:Ljava/util/logging/Level;

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v16

    invoke-virtual/range {v16 .. v16}, Ljava/lang/String;->length()I

    move-result v16

    new-instance v10, Ljava/lang/StringBuilder;

    add-int/lit8 v11, v16, 0x47

    invoke-direct {v10, v11}, Ljava/lang/StringBuilder;-><init>(I)V

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v10}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const-string v10, "supportsUnsafeByteBufferOperations"

    invoke-virtual {v14, v15, v7, v10, v0}, Ljava/util/logging/Logger;->logp(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    :cond_4
    move v0, v13

    :goto_1
    sput-boolean v0, Lfr;->a:Z

    sget-object v0, Lfr;->f:Lsun/misc/Unsafe;

    if-eqz v0, :cond_6

    :try_start_1
    invoke-virtual {v0}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v0

    new-array v10, v12, [Ljava/lang/Class;

    const-class v11, Ljava/lang/reflect/Field;

    aput-object v11, v10, v13

    invoke-virtual {v0, v6, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v6, v12, [Ljava/lang/Class;

    const-class v10, Ljava/lang/Class;

    aput-object v10, v6, v13

    const-string v10, "arrayBaseOffset"

    invoke-virtual {v0, v10, v6}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v6, v12, [Ljava/lang/Class;

    const-class v10, Ljava/lang/Class;

    aput-object v10, v6, v13

    const-string v10, "arrayIndexScale"

    invoke-virtual {v0, v10, v6}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v6, 0x2

    new-array v10, v6, [Ljava/lang/Class;

    const-class v6, Ljava/lang/Object;

    aput-object v6, v10, v13

    sget-object v6, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v6, v10, v12

    invoke-virtual {v0, v4, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v4, 0x3

    new-array v6, v4, [Ljava/lang/Class;

    const-class v4, Ljava/lang/Object;

    aput-object v4, v6, v13

    sget-object v4, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v4, v6, v12

    sget-object v4, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v10, 0x2

    aput-object v4, v6, v10

    invoke-virtual {v0, v2, v6}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v2, v10, [Ljava/lang/Class;

    const-class v4, Ljava/lang/Object;

    aput-object v4, v2, v13

    sget-object v4, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v4, v2, v12

    invoke-virtual {v0, v9, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v2, 0x3

    new-array v4, v2, [Ljava/lang/Class;

    const-class v2, Ljava/lang/Object;

    aput-object v2, v4, v13

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v2, v4, v12

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x2

    aput-object v2, v4, v6

    invoke-virtual {v0, v1, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v1, v6, [Ljava/lang/Class;

    const-class v2, Ljava/lang/Object;

    aput-object v2, v1, v13

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v2, v1, v12

    const-string v2, "getObject"

    invoke-virtual {v0, v2, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v1, 0x3

    new-array v2, v1, [Ljava/lang/Class;

    const-class v1, Ljava/lang/Object;

    aput-object v1, v2, v13

    sget-object v1, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v1, v2, v12

    const-class v1, Ljava/lang/Object;

    const/4 v4, 0x2

    aput-object v1, v2, v4

    const-string v1, "putObject"

    invoke-virtual {v0, v1, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    invoke-static {}, Lbx;->a()Z

    move-result v1

    if-nez v1, :cond_5

    new-array v1, v4, [Ljava/lang/Class;

    const-class v2, Ljava/lang/Object;

    aput-object v2, v1, v13

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v2, v1, v12

    invoke-virtual {v0, v5, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v1, 0x3

    new-array v2, v1, [Ljava/lang/Class;

    const-class v1, Ljava/lang/Object;

    aput-object v1, v2, v13

    sget-object v1, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v1, v2, v12

    sget-object v1, Ljava/lang/Byte;->TYPE:Ljava/lang/Class;

    const/4 v4, 0x2

    aput-object v1, v2, v4

    invoke-virtual {v0, v3, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v1, v4, [Ljava/lang/Class;

    const-class v2, Ljava/lang/Object;

    aput-object v2, v1, v13

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v2, v1, v12

    const-string v2, "getBoolean"

    invoke-virtual {v0, v2, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v1, 0x3

    new-array v2, v1, [Ljava/lang/Class;

    const-class v1, Ljava/lang/Object;

    aput-object v1, v2, v13

    sget-object v1, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v1, v2, v12

    sget-object v1, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    const/4 v3, 0x2

    aput-object v1, v2, v3

    const-string v1, "putBoolean"

    invoke-virtual {v0, v1, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v1, v3, [Ljava/lang/Class;

    const-class v2, Ljava/lang/Object;

    aput-object v2, v1, v13

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v2, v1, v12

    const-string v2, "getFloat"

    invoke-virtual {v0, v2, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v1, 0x3

    new-array v2, v1, [Ljava/lang/Class;

    const-class v1, Ljava/lang/Object;

    aput-object v1, v2, v13

    sget-object v1, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v1, v2, v12

    sget-object v1, Ljava/lang/Float;->TYPE:Ljava/lang/Class;

    const/4 v3, 0x2

    aput-object v1, v2, v3

    const-string v1, "putFloat"

    invoke-virtual {v0, v1, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v1, v3, [Ljava/lang/Class;

    const-class v2, Ljava/lang/Object;

    aput-object v2, v1, v13

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v2, v1, v12

    const-string v2, "getDouble"

    invoke-virtual {v0, v2, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v1, 0x3

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Ljava/lang/Object;

    aput-object v2, v1, v13

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v2, v1, v12

    sget-object v2, Ljava/lang/Double;->TYPE:Ljava/lang/Class;

    const/4 v3, 0x2

    aput-object v2, v1, v3

    const-string v2, "putDouble"

    invoke-virtual {v0, v2, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    :try_end_1
    .catchall {:try_start_1 .. :try_end_1} :catchall_1

    :cond_5
    move v0, v12

    goto :goto_2

    :catchall_1
    move-exception v0

    sget-object v1, Lfr;->e:Ljava/util/logging/Logger;

    sget-object v2, Ljava/util/logging/Level;->WARNING:Ljava/util/logging/Level;

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v3

    new-instance v4, Ljava/lang/StringBuilder;

    add-int/lit8 v3, v3, 0x47

    invoke-direct {v4, v3}, Ljava/lang/StringBuilder;-><init>(I)V

    invoke-virtual {v4, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v4}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    const-string v3, "supportsUnsafeArrayOperations"

    invoke-virtual {v1, v2, v7, v3, v0}, Ljava/util/logging/Logger;->logp(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    :cond_6
    move v0, v13

    :goto_2
    sput-boolean v0, Lfr;->b:Z

    const-class v0, [B

    invoke-static {v0}, Lfr;->O000000o(Ljava/lang/Class;)I

    move-result v0

    int-to-long v0, v0

    sput-wide v0, Lfr;->c:J

    const-class v0, [Z

    invoke-static {v0}, Lfr;->O000000o(Ljava/lang/Class;)I

    const-class v0, [Z

    invoke-static {v0}, Lfr;->O00000o(Ljava/lang/Class;)V

    const-class v0, [I

    invoke-static {v0}, Lfr;->O000000o(Ljava/lang/Class;)I

    const-class v0, [I

    invoke-static {v0}, Lfr;->O00000o(Ljava/lang/Class;)V

    const-class v0, [J

    invoke-static {v0}, Lfr;->O000000o(Ljava/lang/Class;)I

    const-class v0, [J

    invoke-static {v0}, Lfr;->O00000o(Ljava/lang/Class;)V

    const-class v0, [F

    invoke-static {v0}, Lfr;->O000000o(Ljava/lang/Class;)I

    const-class v0, [F

    invoke-static {v0}, Lfr;->O00000o(Ljava/lang/Class;)V

    const-class v0, [D

    invoke-static {v0}, Lfr;->O000000o(Ljava/lang/Class;)I

    const-class v0, [D

    invoke-static {v0}, Lfr;->O00000o(Ljava/lang/Class;)V

    const-class v0, [Ljava/lang/Object;

    invoke-static {v0}, Lfr;->O000000o(Ljava/lang/Class;)I

    const-class v0, [Ljava/lang/Object;

    invoke-static {v0}, Lfr;->O00000o(Ljava/lang/Class;)V

    invoke-static {}, Lfr;->b()Ljava/lang/reflect/Field;

    move-result-object v0

    if-nez v0, :cond_7

    goto :goto_3

    :cond_7
    sget-object v1, Lfr;->j:Lfq;

    if-eqz v1, :cond_8

    invoke-virtual {v1, v0}, Lfq;->O000000o(Ljava/lang/reflect/Field;)V

    :cond_8
    :goto_3
    invoke-static {}, Ljava/nio/ByteOrder;->nativeOrder()Ljava/nio/ByteOrder;

    move-result-object v0

    sget-object v1, Ljava/nio/ByteOrder;->BIG_ENDIAN:Ljava/nio/ByteOrder;

    if-ne v0, v1, :cond_9

    goto :goto_4

    :cond_9
    move v12, v13

    :goto_4
    sput-boolean v12, Lfr;->d:Z

    return-void
.end method

.method private constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static O000000o([BJ)B
    .locals 3

    sget-object v0, Lfr;->j:Lfq;

    sget-wide v1, Lfr;->c:J

    add-long/2addr v1, p1

    invoke-virtual {v0, p0, v1, v2}, Lfq;->O000000o(Ljava/lang/Object;J)B

    move-result p0

    return p0
.end method

.method private static O000000o(Ljava/lang/Class;)I
    .locals 1

    sget-boolean v0, Lfr;->b:Z

    if-eqz v0, :cond_0

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0}, Lfq;->O00000Oo(Ljava/lang/Class;)I

    move-result p0

    return p0

    :cond_0
    const/4 p0, -0x1

    return p0
.end method

.method static O000000o(Ljava/lang/Object;J)I
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2}, Lfq;->O00000oO(Ljava/lang/Object;J)I

    move-result p0

    return p0
.end method

.method public static O000000o(Ljava/lang/Object;JB)V
    .locals 4

    long-to-int v0, p1

    not-int v0, v0

    and-int/lit8 v0, v0, 0x3

    shl-int/lit8 v0, v0, 0x3

    const-wide/16 v1, -0x4

    and-long/2addr p1, v1

    invoke-static {p0, p1, p2}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v1

    const/16 v2, 0xff

    shl-int v3, v2, v0

    not-int v3, v3

    and-int/2addr v1, v3

    and-int/2addr p3, v2

    shl-int/2addr p3, v0

    or-int/2addr p3, v1

    invoke-static {p0, p1, p2, p3}, Lfr;->O000000o(Ljava/lang/Object;JI)V

    return-void
.end method

.method static O000000o(Ljava/lang/Object;JD)V
    .locals 6

    sget-object v0, Lfr;->j:Lfq;

    move-object v1, p0

    move-wide v2, p1

    move-wide v4, p3

    invoke-virtual/range {v0 .. v5}, Lfq;->O000000o(Ljava/lang/Object;JD)V

    return-void
.end method

.method static O000000o(Ljava/lang/Object;JF)V
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2, p3}, Lfq;->O000000o(Ljava/lang/Object;JF)V

    return-void
.end method

.method static O000000o(Ljava/lang/Object;JI)V
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2, p3}, Lfq;->O000000o(Ljava/lang/Object;JI)V

    return-void
.end method

.method static O000000o(Ljava/lang/Object;JJ)V
    .locals 6

    sget-object v0, Lfr;->j:Lfq;

    move-object v1, p0

    move-wide v2, p1

    move-wide v4, p3

    invoke-virtual/range {v0 .. v5}, Lfq;->O000000o(Ljava/lang/Object;JJ)V

    return-void
.end method

.method static O000000o(Ljava/lang/Object;JLjava/lang/Object;)V
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2, p3}, Lfq;->O000000o(Ljava/lang/Object;JLjava/lang/Object;)V

    return-void
.end method

.method static O000000o(Ljava/lang/Object;JZ)V
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2, p3}, Lfq;->O000000o(Ljava/lang/Object;JZ)V

    return-void
.end method

.method static O000000o([BJB)V
    .locals 3

    sget-object v0, Lfr;->j:Lfq;

    sget-wide v1, Lfr;->c:J

    add-long/2addr v1, p1

    invoke-virtual {v0, p0, v1, v2, p3}, Lfq;->O000000o(Ljava/lang/Object;JB)V

    return-void
.end method

.method static O00000Oo(Ljava/lang/Object;J)J
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2}, Lfq;->O00000oo(Ljava/lang/Object;J)J

    move-result-wide p0

    return-wide p0
.end method

.method static O00000Oo(Ljava/lang/Class;)Ljava/lang/Object;
    .locals 1

    :try_start_0
    sget-object v0, Lfr;->f:Lsun/misc/Unsafe;

    invoke-virtual {v0, p0}, Lsun/misc/Unsafe;->allocateInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object p0
    :try_end_0
    .catch Ljava/lang/InstantiationException; {:try_start_0 .. :try_end_0} :catch_0

    return-object p0

    :catch_0
    move-exception p0

    new-instance v0, Ljava/lang/IllegalStateException;

    invoke-direct {v0, p0}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/Throwable;)V

    throw v0
.end method

.method public static O00000Oo(Ljava/lang/Object;JB)V
    .locals 4

    long-to-int v0, p1

    and-int/lit8 v0, v0, 0x3

    shl-int/lit8 v0, v0, 0x3

    const-wide/16 v1, -0x4

    and-long/2addr p1, v1

    invoke-static {p0, p1, p2}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v1

    const/16 v2, 0xff

    shl-int v3, v2, v0

    not-int v3, v3

    and-int/2addr v1, v3

    and-int/2addr p3, v2

    shl-int/2addr p3, v0

    or-int/2addr p3, v1

    invoke-static {p0, p1, p2, p3}, Lfr;->O000000o(Ljava/lang/Object;JI)V

    return-void
.end method

.method public static O00000Oo(Ljava/lang/Object;JZ)V
    .locals 0

    invoke-static {p0, p1, p2, p3}, Lfr;->O000000o(Ljava/lang/Object;JB)V

    return-void
.end method

.method static O00000o(Ljava/lang/Object;J)F
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2}, Lfq;->O00000o0(Ljava/lang/Object;J)F

    move-result p0

    return p0
.end method

.method private static O00000o(Ljava/lang/Class;)V
    .locals 1

    sget-boolean v0, Lfr;->b:Z

    if-eqz v0, :cond_0

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0}, Lfq;->O000000o(Ljava/lang/Class;)I

    :cond_0
    return-void
.end method

.method public static O00000o0(Ljava/lang/Object;JZ)V
    .locals 0

    invoke-static {p0, p1, p2, p3}, Lfr;->O00000Oo(Ljava/lang/Object;JB)V

    return-void
.end method

.method private static O00000o0(Ljava/lang/Class;)Z
    .locals 8

    invoke-static {}, Lbx;->a()Z

    move-result v0

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    :try_start_0
    sget-object v0, Lfr;->g:Ljava/lang/Class;

    const/4 v2, 0x2

    new-array v3, v2, [Ljava/lang/Class;

    aput-object p0, v3, v1

    sget-object v4, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x1

    aput-object v4, v3, v5

    const-string v4, "peekLong"

    invoke-virtual {v0, v4, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v3, 0x3

    new-array v4, v3, [Ljava/lang/Class;

    aput-object p0, v4, v1

    sget-object v6, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    aput-object v6, v4, v5

    sget-object v6, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    aput-object v6, v4, v2

    const-string v6, "pokeLong"

    invoke-virtual {v0, v6, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v4, v3, [Ljava/lang/Class;

    aput-object p0, v4, v1

    sget-object v6, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object v6, v4, v5

    sget-object v6, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    aput-object v6, v4, v2

    const-string v6, "pokeInt"

    invoke-virtual {v0, v6, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v4, v2, [Ljava/lang/Class;

    aput-object p0, v4, v1

    sget-object v6, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    aput-object v6, v4, v5

    const-string v6, "peekInt"

    invoke-virtual {v0, v6, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v4, v2, [Ljava/lang/Class;

    aput-object p0, v4, v1

    sget-object v6, Ljava/lang/Byte;->TYPE:Ljava/lang/Class;

    aput-object v6, v4, v5

    const-string v6, "pokeByte"

    invoke-virtual {v0, v6, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v4, v5, [Ljava/lang/Class;

    aput-object p0, v4, v1

    const-string v6, "peekByte"

    invoke-virtual {v0, v6, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    const/4 v4, 0x4

    new-array v6, v4, [Ljava/lang/Class;

    aput-object p0, v6, v1

    const-class v7, [B

    aput-object v7, v6, v5

    sget-object v7, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object v7, v6, v2

    sget-object v7, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object v7, v6, v3

    const-string v7, "pokeByteArray"

    invoke-virtual {v0, v7, v6}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    new-array v4, v4, [Ljava/lang/Class;

    aput-object p0, v4, v1

    const-class p0, [B

    aput-object p0, v4, v5

    sget-object p0, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object p0, v4, v2

    sget-object p0, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object p0, v4, v3

    const-string p0, "peekByteArray"

    invoke-virtual {v0, p0, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    return v5

    :catchall_0
    :cond_0
    return v1
.end method

.method static O00000o0(Ljava/lang/Object;J)Z
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2}, Lfq;->O00000Oo(Ljava/lang/Object;J)Z

    move-result p0

    return p0
.end method

.method static O00000oO(Ljava/lang/Object;J)D
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2}, Lfq;->O00000o(Ljava/lang/Object;J)D

    move-result-wide p0

    return-wide p0
.end method

.method static O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;
    .locals 1

    sget-object v0, Lfr;->j:Lfq;

    invoke-virtual {v0, p0, p1, p2}, Lfq;->O0000O0o(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    return-object p0
.end method

.method public static O0000O0o(Ljava/lang/Object;J)B
    .locals 2

    const-wide/16 v0, -0x4

    and-long/2addr v0, p1

    invoke-static {p0, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    not-long p1, p1

    const-wide/16 v0, 0x3

    and-long/2addr p1, v0

    const/4 v0, 0x3

    shl-long/2addr p1, v0

    long-to-int p1, p1

    ushr-int/2addr p0, p1

    and-int/lit16 p0, p0, 0xff

    int-to-byte p0, p0

    return p0
.end method

.method public static O0000OOo(Ljava/lang/Object;J)B
    .locals 2

    const-wide/16 v0, -0x4

    and-long/2addr v0, p1

    invoke-static {p0, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    const-wide/16 v0, 0x3

    and-long/2addr p1, v0

    const/4 v0, 0x3

    shl-long/2addr p1, v0

    long-to-int p1, p1

    ushr-int/2addr p0, p1

    and-int/lit16 p0, p0, 0xff

    int-to-byte p0, p0

    return p0
.end method

.method public static O0000Oo(Ljava/lang/Object;J)Z
    .locals 0

    invoke-static {p0, p1, p2}, Lfr;->O0000OOo(Ljava/lang/Object;J)B

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method public static O0000Oo0(Ljava/lang/Object;J)Z
    .locals 0

    invoke-static {p0, p1, p2}, Lfr;->O0000O0o(Ljava/lang/Object;J)B

    move-result p0

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method private static a(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;
    .locals 0

    :try_start_0
    invoke-virtual {p0, p1}, Ljava/lang/Class;->getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object p0
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    goto :goto_0

    :catchall_0
    const/4 p0, 0x0

    :goto_0
    return-object p0
.end method

.method static a()Lsun/misc/Unsafe;
    .locals 1

    :try_start_0
    new-instance v0, Lfm;

    invoke-direct {v0}, Lfm;-><init>()V

    invoke-static {v0}, Ljava/security/AccessController;->doPrivileged(Ljava/security/PrivilegedExceptionAction;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lsun/misc/Unsafe;
    :try_end_0
    .catchall {:try_start_0 .. :try_end_0} :catchall_0

    goto :goto_0

    :catchall_0
    const/4 v0, 0x0

    :goto_0
    return-object v0
.end method

.method private static b()Ljava/lang/reflect/Field;
    .locals 3

    invoke-static {}, Lbx;->a()Z

    move-result v0

    if-eqz v0, :cond_1

    const-class v0, Ljava/nio/Buffer;

    const-string v1, "effectiveDirectAddress"

    invoke-static {v0, v1}, Lfr;->a(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v0

    if-nez v0, :cond_0

    goto :goto_0

    :cond_0
    return-object v0

    :cond_1
    :goto_0
    const-class v0, Ljava/nio/Buffer;

    const-string v1, "address"

    invoke-static {v0, v1}, Lfr;->a(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v0

    if-eqz v0, :cond_2

    invoke-virtual {v0}, Ljava/lang/reflect/Field;->getType()Ljava/lang/Class;

    move-result-object v1

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    if-ne v1, v2, :cond_2

    return-object v0

    :cond_2
    const/4 v0, 0x0

    return-object v0
.end method

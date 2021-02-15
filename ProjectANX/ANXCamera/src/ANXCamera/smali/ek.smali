.class final Lek;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Les;


# static fields
.field private static final a:[I

.field private static final b:Lsun/misc/Unsafe;


# instance fields
.field private final c:[I

.field private final d:[Ljava/lang/Object;

.field private final e:I

.field private final f:I

.field private final g:Leh;

.field private final h:Z

.field private final i:Z

.field private final j:Z

.field private final k:[I

.field private final l:I

.field private final m:I

.field private final n:Ldv;

.field private final o:Lfh;

.field private final p:Lej;

.field private final q:Lfs;

.field private final r:Lff;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    const/4 v0, 0x0

    new-array v0, v0, [I

    sput-object v0, Lek;->a:[I

    invoke-static {}, Lfr;->a()Lsun/misc/Unsafe;

    move-result-object v0

    sput-object v0, Lek;->b:Lsun/misc/Unsafe;

    return-void
.end method

.method private constructor <init>([I[Ljava/lang/Object;IILeh;ZZ[IIILfs;Ldv;Lfh;Lej;Lff;[B)V
    .locals 4

    move-object v0, p0

    move-object/from16 v1, p14

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    move-object v2, p1

    iput-object v2, v0, Lek;->c:[I

    move-object v2, p2

    iput-object v2, v0, Lek;->d:[Ljava/lang/Object;

    move v2, p3

    iput v2, v0, Lek;->e:I

    move v2, p4

    iput v2, v0, Lek;->f:I

    move v2, p6

    iput-boolean v2, v0, Lek;->i:Z

    const/4 v2, 0x0

    if-eqz v1, :cond_0

    invoke-static {p5}, Lej;->O000000o(Leh;)Z

    move-result v3

    if-eqz v3, :cond_0

    const/4 v3, 0x1

    goto :goto_0

    :cond_0
    move v3, v2

    :goto_0
    iput-boolean v3, v0, Lek;->h:Z

    iput-boolean v2, v0, Lek;->j:Z

    move-object v2, p8

    iput-object v2, v0, Lek;->k:[I

    move v2, p9

    iput v2, v0, Lek;->l:I

    move v2, p10

    iput v2, v0, Lek;->m:I

    move-object v2, p11

    iput-object v2, v0, Lek;->q:Lfs;

    move-object/from16 v2, p12

    iput-object v2, v0, Lek;->n:Ldv;

    move-object/from16 v2, p13

    iput-object v2, v0, Lek;->o:Lfh;

    iput-object v1, v0, Lek;->p:Lej;

    move-object v1, p5

    iput-object v1, v0, Lek;->g:Leh;

    move-object/from16 v1, p15

    iput-object v1, v0, Lek;->r:Lff;

    return-void
.end method

.method private static final O000000o(Lfh;Ljava/lang/Object;)I
    .locals 0

    invoke-virtual {p0, p1}, Lfh;->O00000Oo(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    invoke-static {p0}, Lfh;->O00000o(Ljava/lang/Object;)I

    move-result p0

    return p0
.end method

.method private final O000000o(Ljava/lang/Object;[BIIIIIIIJILby;)I
    .locals 16

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move-object/from16 v3, p2

    move/from16 v4, p3

    move/from16 v2, p5

    move/from16 v8, p6

    move/from16 v5, p7

    move-wide/from16 v9, p10

    move/from16 v6, p12

    move-object/from16 v11, p13

    sget-object v12, Lek;->b:Lsun/misc/Unsafe;

    iget-object v7, v0, Lek;->c:[I

    add-int/lit8 v13, v6, 0x2

    aget v7, v7, v13

    const v13, 0xfffff

    and-int/2addr v7, v13

    int-to-long v13, v7

    const/4 v7, 0x5

    const/4 v15, 0x2

    packed-switch p9, :pswitch_data_0

    goto/16 :goto_8

    :pswitch_0
    const/4 v7, 0x3

    if-ne v5, v7, :cond_12

    invoke-direct {v0, v6}, Lek;->a(I)Les;

    move-result-object v0

    and-int/lit8 v2, v2, -0x8

    or-int/lit8 v6, v2, 0x4

    move-object v2, v0

    move-object/from16 v3, p2

    move/from16 v4, p3

    move/from16 v5, p4

    move-object/from16 v7, p13

    invoke-static/range {v2 .. v7}, Leb;->O000000o(Les;[BIIILby;)I

    move-result v0

    invoke-virtual {v12, v1, v13, v14}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v2

    if-ne v2, v8, :cond_0

    invoke-virtual {v12, v1, v9, v10}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v15

    goto :goto_0

    :cond_0
    const/4 v15, 0x0

    :goto_0
    iget-object v2, v11, Lby;->c:Ljava/lang/Object;

    if-eqz v15, :cond_7

    invoke-static {v15, v2}, Ldj;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    goto/16 :goto_4

    :pswitch_1
    if-eqz v5, :cond_1

    goto/16 :goto_8

    :cond_1
    invoke-static {v3, v4, v11}, Leb;->O00000Oo([BILby;)I

    move-result v0

    iget-wide v2, v11, Lby;->b:J

    invoke-static {v2, v3}, Lcl;->a(J)J

    move-result-wide v2

    invoke-static {v2, v3}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v2

    invoke-virtual {v12, v1, v9, v10, v2}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    return v0

    :pswitch_2
    if-eqz v5, :cond_2

    goto/16 :goto_8

    :cond_2
    invoke-static {v3, v4, v11}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v2, v11, Lby;->a:I

    invoke-static {v2}, Lcl;->a(I)I

    move-result v2

    invoke-static {v2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-virtual {v12, v1, v9, v10, v2}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    return v0

    :pswitch_3
    if-nez v5, :cond_12

    invoke-static {v3, v4, v11}, Leb;->O000000o([BILby;)I

    move-result v3

    iget v4, v11, Lby;->a:I

    invoke-direct {v0, v6}, Lek;->c(I)Ldh;

    move-result-object v0

    if-eqz v0, :cond_4

    invoke-interface {v0, v4}, Ldh;->a(I)Z

    move-result v0

    if-eqz v0, :cond_3

    goto :goto_1

    :cond_3
    invoke-static/range {p1 .. p1}, Lek;->O0000O0o(Ljava/lang/Object;)Lfi;

    move-result-object v0

    int-to-long v4, v4

    invoke-static {v4, v5}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v1

    invoke-virtual {v0, v2, v1}, Lfi;->O000000o(ILjava/lang/Object;)V

    goto :goto_2

    :cond_4
    :goto_1
    invoke-static {v4}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    invoke-virtual {v12, v1, v9, v10, v0}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    :goto_2
    move v0, v3

    goto/16 :goto_9

    :pswitch_4
    if-eq v5, v15, :cond_5

    goto/16 :goto_8

    :cond_5
    invoke-static {v3, v4, v11}, Leb;->O00000oO([BILby;)I

    move-result v0

    iget-object v2, v11, Lby;->c:Ljava/lang/Object;

    invoke-virtual {v12, v1, v9, v10, v2}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    return v0

    :pswitch_5
    if-ne v5, v15, :cond_12

    invoke-direct {v0, v6}, Lek;->a(I)Les;

    move-result-object v0

    move/from16 v2, p4

    invoke-static {v0, v3, v4, v2, v11}, Leb;->O000000o(Les;[BIILby;)I

    move-result v0

    invoke-virtual {v12, v1, v13, v14}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v2

    if-ne v2, v8, :cond_6

    invoke-virtual {v12, v1, v9, v10}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v15

    goto :goto_3

    :cond_6
    const/4 v15, 0x0

    :goto_3
    iget-object v2, v11, Lby;->c:Ljava/lang/Object;

    if-eqz v15, :cond_7

    invoke-static {v15, v2}, Ldj;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    goto :goto_4

    :pswitch_6
    if-ne v5, v15, :cond_12

    invoke-static {v3, v4, v11}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v2, v11, Lby;->a:I

    if-nez v2, :cond_8

    const-string v2, ""

    :cond_7
    :goto_4
    invoke-virtual {v12, v1, v9, v10, v2}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    goto :goto_6

    :cond_8
    const/high16 v4, 0x20000000

    and-int v4, p8, v4

    if-nez v4, :cond_9

    goto :goto_5

    :cond_9
    add-int v4, v0, v2

    invoke-static {v3, v0, v4}, Lfx;->O000000o([BII)Z

    move-result v4

    if-eqz v4, :cond_a

    :goto_5
    new-instance v4, Ljava/lang/String;

    sget-object v5, Ldj;->a:Ljava/nio/charset/Charset;

    invoke-direct {v4, v3, v0, v2, v5}, Ljava/lang/String;-><init>([BIILjava/nio/charset/Charset;)V

    invoke-virtual {v12, v1, v9, v10, v4}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    add-int/2addr v0, v2

    :goto_6
    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    goto/16 :goto_9

    :cond_a
    invoke-static {}, Ldl;->f()Ldl;

    move-result-object v0

    throw v0

    :pswitch_7
    if-nez v5, :cond_12

    invoke-static {v3, v4, v11}, Leb;->O00000Oo([BILby;)I

    move-result v0

    iget-wide v2, v11, Lby;->b:J

    const-wide/16 v4, 0x0

    cmp-long v2, v2, v4

    if-eqz v2, :cond_b

    const/4 v15, 0x1

    goto :goto_7

    :cond_b
    const/4 v15, 0x0

    :goto_7
    invoke-static {v15}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v2

    invoke-virtual {v12, v1, v9, v10, v2}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    return v0

    :pswitch_8
    if-eq v5, v7, :cond_c

    goto/16 :goto_8

    :cond_c
    invoke-static/range {p2 .. p3}, Leb;->O000000o([BI)I

    move-result v0

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    invoke-virtual {v12, v1, v9, v10, v0}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    add-int/lit8 v0, v4, 0x4

    return v0

    :pswitch_9
    const/4 v0, 0x1

    if-eq v5, v0, :cond_d

    goto :goto_8

    :cond_d
    invoke-static/range {p2 .. p3}, Leb;->O00000Oo([BI)J

    move-result-wide v2

    invoke-static {v2, v3}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    invoke-virtual {v12, v1, v9, v10, v0}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    add-int/lit8 v0, v4, 0x8

    return v0

    :pswitch_a
    if-eqz v5, :cond_e

    goto :goto_8

    :cond_e
    invoke-static {v3, v4, v11}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v2, v11, Lby;->a:I

    invoke-static {v2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-virtual {v12, v1, v9, v10, v2}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    return v0

    :pswitch_b
    if-eqz v5, :cond_f

    goto :goto_8

    :cond_f
    invoke-static {v3, v4, v11}, Leb;->O00000Oo([BILby;)I

    move-result v0

    iget-wide v2, v11, Lby;->b:J

    invoke-static {v2, v3}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v2

    invoke-virtual {v12, v1, v9, v10, v2}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    return v0

    :pswitch_c
    if-eq v5, v7, :cond_10

    goto :goto_8

    :cond_10
    invoke-static/range {p2 .. p3}, Leb;->O00000o([BI)F

    move-result v0

    invoke-static {v0}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v0

    invoke-virtual {v12, v1, v9, v10, v0}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    add-int/lit8 v0, v4, 0x4

    return v0

    :pswitch_d
    const/4 v0, 0x1

    if-eq v5, v0, :cond_11

    goto :goto_8

    :cond_11
    invoke-static/range {p2 .. p3}, Leb;->O00000o0([BI)D

    move-result-wide v2

    invoke-static {v2, v3}, Ljava/lang/Double;->valueOf(D)Ljava/lang/Double;

    move-result-object v0

    invoke-virtual {v12, v1, v9, v10, v0}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-virtual {v12, v1, v13, v14, v8}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    add-int/lit8 v0, v4, 0x8

    return v0

    :cond_12
    :goto_8
    move v0, v4

    :goto_9
    return v0

    :pswitch_data_0
    .packed-switch 0x33
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_a
        :pswitch_3
        :pswitch_8
        :pswitch_9
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private final O000000o(Ljava/lang/Object;[BIIIIIIJIJLby;)I
    .locals 15

    move-object v0, p0

    move-object/from16 v1, p1

    move-object/from16 v3, p2

    move/from16 v4, p3

    move/from16 v5, p4

    move/from16 v2, p5

    move/from16 v6, p7

    move/from16 v8, p8

    move-wide/from16 v9, p12

    move-object/from16 v7, p14

    sget-object v11, Lek;->b:Lsun/misc/Unsafe;

    invoke-virtual {v11, v1, v9, v10}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v11

    check-cast v11, Ldi;

    invoke-interface {v11}, Ldi;->a()Z

    move-result v12

    if-nez v12, :cond_1

    invoke-interface {v11}, Ljava/util/List;->size()I

    move-result v12

    if-eqz v12, :cond_0

    add-int/2addr v12, v12

    goto :goto_0

    :cond_0
    const/16 v12, 0xa

    :goto_0
    invoke-interface {v11, v12}, Ldi;->a(I)Ldi;

    move-result-object v11

    sget-object v12, Lek;->b:Lsun/misc/Unsafe;

    invoke-virtual {v12, v1, v9, v10, v11}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    :cond_1
    const/4 v9, 0x5

    const-wide/16 v12, 0x0

    const/4 v10, 0x1

    const/4 v14, 0x2

    packed-switch p11, :pswitch_data_0

    const/4 v1, 0x3

    if-ne v6, v1, :cond_2b

    invoke-direct {p0, v8}, Lek;->a(I)Les;

    move-result-object v0

    and-int/lit8 v1, v2, -0x8

    or-int/lit8 v1, v1, 0x4

    move-object/from16 p6, v0

    move-object/from16 p7, p2

    move/from16 p8, p3

    :goto_1
    move/from16 p9, p4

    move/from16 p10, v1

    move-object/from16 p11, p14

    invoke-static/range {p6 .. p11}, Leb;->O000000o(Les;[BIIILby;)I

    move-result v4

    iget-object v6, v7, Lby;->c:Ljava/lang/Object;

    invoke-interface {v11, v6}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto/16 :goto_12

    :pswitch_0
    if-ne v6, v14, :cond_2

    invoke-static {v3, v4, v11, v7}, Leb;->O0000Oo0([BILdi;Lby;)I

    move-result v0

    goto/16 :goto_14

    :cond_2
    if-nez v6, :cond_2b

    check-cast v11, Ldw;

    invoke-static {v3, v4, v7}, Leb;->O00000Oo([BILby;)I

    move-result v0

    :goto_2
    iget-wide v8, v7, Lby;->b:J

    invoke-static {v8, v9}, Lcl;->a(J)J

    move-result-wide v8

    invoke-virtual {v11, v8, v9}, Ldw;->a(J)V

    if-ge v0, v5, :cond_3

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_3

    invoke-static {v3, v1, v7}, Leb;->O00000Oo([BILby;)I

    move-result v0

    goto :goto_2

    :cond_3
    return v0

    :pswitch_1
    if-ne v6, v14, :cond_4

    invoke-static {v3, v4, v11, v7}, Leb;->O0000OOo([BILdi;Lby;)I

    move-result v0

    goto/16 :goto_14

    :cond_4
    if-nez v6, :cond_2b

    check-cast v11, Ldf;

    invoke-static {v3, v4, v7}, Leb;->O000000o([BILby;)I

    move-result v0

    :goto_3
    iget v1, v7, Lby;->a:I

    invoke-static {v1}, Lcl;->a(I)I

    move-result v1

    invoke-virtual {v11, v1}, Ldf;->c(I)V

    if-ge v0, v5, :cond_5

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_5

    invoke-static {v3, v1, v7}, Leb;->O000000o([BILby;)I

    move-result v0

    goto :goto_3

    :cond_5
    return v0

    :pswitch_2
    if-ne v6, v14, :cond_6

    invoke-static {v3, v4, v11, v7}, Leb;->O000000o([BILdi;Lby;)I

    move-result v2

    goto :goto_4

    :cond_6
    if-nez v6, :cond_2b

    move/from16 v2, p5

    move-object/from16 v3, p2

    move/from16 v4, p3

    move/from16 v5, p4

    move-object v6, v11

    move-object/from16 v7, p14

    invoke-static/range {v2 .. v7}, Leb;->O000000o(I[BIILdi;Lby;)I

    move-result v2

    :goto_4
    check-cast v1, Lde;

    iget-object v3, v1, Lde;->h:Lfi;

    sget-object v4, Lfi;->a:Lfi;

    if-eq v3, v4, :cond_7

    goto :goto_5

    :cond_7
    const/4 v3, 0x0

    :goto_5
    invoke-direct {p0, v8}, Lek;->c(I)Ldh;

    move-result-object v4

    iget-object v0, v0, Lek;->o:Lfh;

    move/from16 v5, p6

    invoke-static {v5, v11, v4, v3, v0}, Leu;->O000000o(ILjava/util/List;Ldh;Ljava/lang/Object;Lfh;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lfi;

    if-nez v0, :cond_8

    move v0, v2

    goto/16 :goto_14

    :cond_8
    iput-object v0, v1, Lde;->h:Lfi;

    return v2

    :pswitch_3
    if-ne v6, v14, :cond_2b

    invoke-static {v3, v4, v7}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v1, v7, Lby;->a:I

    if-ltz v1, :cond_e

    array-length v4, v3

    sub-int/2addr v4, v0

    if-gt v1, v4, :cond_d

    if-eqz v1, :cond_9

    :goto_6
    invoke-static {v3, v0, v1}, Lck;->O000000o([BII)Lck;

    move-result-object v4

    invoke-interface {v11, v4}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/2addr v0, v1

    goto :goto_7

    :cond_9
    sget-object v1, Lck;->b:Lck;

    invoke-interface {v11, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :goto_7
    if-ge v0, v5, :cond_c

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_c

    invoke-static {v3, v1, v7}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v1, v7, Lby;->a:I

    if-ltz v1, :cond_b

    array-length v4, v3

    sub-int/2addr v4, v0

    if-gt v1, v4, :cond_a

    if-eqz v1, :cond_9

    goto :goto_6

    :cond_a
    invoke-static {}, Ldl;->a()Ldl;

    move-result-object v0

    throw v0

    :cond_b
    invoke-static {}, Ldl;->b()Ldl;

    move-result-object v0

    throw v0

    :cond_c
    return v0

    :cond_d
    invoke-static {}, Ldl;->a()Ldl;

    move-result-object v0

    throw v0

    :cond_e
    invoke-static {}, Ldl;->b()Ldl;

    move-result-object v0

    throw v0

    :pswitch_4
    if-eq v6, v14, :cond_f

    goto/16 :goto_13

    :cond_f
    invoke-direct {p0, v8}, Lek;->a(I)Les;

    move-result-object v0

    move-object/from16 p6, v0

    move/from16 p7, p5

    move-object/from16 p8, p2

    move/from16 p9, p3

    move/from16 p10, p4

    move-object/from16 p11, v11

    move-object/from16 p12, p14

    invoke-static/range {p6 .. p12}, Leb;->O000000o(Les;I[BIILdi;Lby;)I

    move-result v0

    return v0

    :pswitch_5
    if-ne v6, v14, :cond_2b

    const-wide/32 v0, 0x20000000

    and-long v0, p9, v0

    cmp-long v0, v0, v12

    const-string v1, ""

    if-eqz v0, :cond_17

    invoke-static {v3, v4, v7}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v4, v7, Lby;->a:I

    if-ltz v4, :cond_16

    if-nez v4, :cond_10

    invoke-interface {v11, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    move v6, v0

    goto :goto_8

    :cond_10
    add-int v6, v0, v4

    invoke-static {v3, v0, v6}, Lfx;->O000000o([BII)Z

    move-result v8

    if-eqz v8, :cond_15

    new-instance v8, Ljava/lang/String;

    sget-object v9, Ldj;->a:Ljava/nio/charset/Charset;

    invoke-direct {v8, v3, v0, v4, v9}, Ljava/lang/String;-><init>([BIILjava/nio/charset/Charset;)V

    invoke-interface {v11, v8}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :goto_8
    if-ge v6, v5, :cond_14

    invoke-static {v3, v6, v7}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_14

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v6

    iget v0, v7, Lby;->a:I

    if-ltz v0, :cond_13

    if-nez v0, :cond_11

    invoke-interface {v11, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    goto :goto_8

    :cond_11
    add-int v4, v6, v0

    invoke-static {v3, v6, v4}, Lfx;->O000000o([BII)Z

    move-result v8

    if-eqz v8, :cond_12

    new-instance v8, Ljava/lang/String;

    sget-object v9, Ldj;->a:Ljava/nio/charset/Charset;

    invoke-direct {v8, v3, v6, v0, v9}, Ljava/lang/String;-><init>([BIILjava/nio/charset/Charset;)V

    invoke-interface {v11, v8}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    move v6, v4

    goto :goto_8

    :cond_12
    invoke-static {}, Ldl;->f()Ldl;

    move-result-object v0

    throw v0

    :cond_13
    invoke-static {}, Ldl;->b()Ldl;

    move-result-object v0

    throw v0

    :cond_14
    move v0, v6

    goto/16 :goto_14

    :cond_15
    invoke-static {}, Ldl;->f()Ldl;

    move-result-object v0

    throw v0

    :cond_16
    invoke-static {}, Ldl;->b()Ldl;

    move-result-object v0

    throw v0

    :cond_17
    invoke-static {v3, v4, v7}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v4, v7, Lby;->a:I

    if-ltz v4, :cond_1a

    if-eqz v4, :cond_18

    new-instance v6, Ljava/lang/String;

    sget-object v8, Ldj;->a:Ljava/nio/charset/Charset;

    invoke-direct {v6, v3, v0, v4, v8}, Ljava/lang/String;-><init>([BIILjava/nio/charset/Charset;)V

    :goto_9
    invoke-interface {v11, v6}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/2addr v0, v4

    goto :goto_a

    :cond_18
    invoke-interface {v11, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :goto_a
    if-ge v0, v5, :cond_2c

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v4

    iget v6, v7, Lby;->a:I

    if-ne v2, v6, :cond_2c

    invoke-static {v3, v4, v7}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v4, v7, Lby;->a:I

    if-ltz v4, :cond_19

    if-eqz v4, :cond_18

    new-instance v6, Ljava/lang/String;

    sget-object v8, Ldj;->a:Ljava/nio/charset/Charset;

    invoke-direct {v6, v3, v0, v4, v8}, Ljava/lang/String;-><init>([BIILjava/nio/charset/Charset;)V

    goto :goto_9

    :cond_19
    invoke-static {}, Ldl;->b()Ldl;

    move-result-object v0

    throw v0

    :cond_1a
    invoke-static {}, Ldl;->b()Ldl;

    move-result-object v0

    throw v0

    :pswitch_6
    if-ne v6, v14, :cond_1b

    invoke-static {v3, v4, v11, v7}, Leb;->O0000O0o([BILdi;Lby;)I

    move-result v0

    goto/16 :goto_14

    :cond_1b
    if-nez v6, :cond_2b

    check-cast v11, Lca;

    invoke-static {v3, v4, v7}, Leb;->O00000Oo([BILby;)I

    move-result v0

    iget-wide v8, v7, Lby;->b:J

    cmp-long v1, v8, v12

    const/4 v4, 0x0

    if-eqz v1, :cond_1c

    :goto_b
    move v1, v10

    goto :goto_c

    :cond_1c
    move v1, v4

    :goto_c
    invoke-virtual {v11, v1}, Lca;->a(Z)V

    if-ge v0, v5, :cond_1d

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v6, v7, Lby;->a:I

    if-ne v2, v6, :cond_1d

    invoke-static {v3, v1, v7}, Leb;->O00000Oo([BILby;)I

    move-result v0

    iget-wide v8, v7, Lby;->b:J

    cmp-long v1, v8, v12

    if-eqz v1, :cond_1c

    goto :goto_b

    :cond_1d
    return v0

    :pswitch_7
    if-ne v6, v14, :cond_1e

    invoke-static {v3, v4, v11, v7}, Leb;->O00000o0([BILdi;Lby;)I

    move-result v0

    goto/16 :goto_14

    :cond_1e
    if-ne v6, v9, :cond_2b

    check-cast v11, Ldf;

    invoke-static/range {p2 .. p3}, Leb;->O000000o([BI)I

    move-result v0

    invoke-virtual {v11, v0}, Ldf;->c(I)V

    add-int/lit8 v0, v4, 0x4

    :goto_d
    if-ge v0, v5, :cond_1f

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_1f

    invoke-static {v3, v1}, Leb;->O000000o([BI)I

    move-result v0

    invoke-virtual {v11, v0}, Ldf;->c(I)V

    add-int/lit8 v0, v1, 0x4

    goto :goto_d

    :cond_1f
    return v0

    :pswitch_8
    if-ne v6, v14, :cond_20

    invoke-static {v3, v4, v11, v7}, Leb;->O00000o([BILdi;Lby;)I

    move-result v0

    goto/16 :goto_14

    :cond_20
    if-ne v6, v10, :cond_2b

    check-cast v11, Ldw;

    invoke-static/range {p2 .. p3}, Leb;->O00000Oo([BI)J

    move-result-wide v0

    invoke-virtual {v11, v0, v1}, Ldw;->a(J)V

    add-int/lit8 v0, v4, 0x8

    :goto_e
    if-ge v0, v5, :cond_21

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_21

    invoke-static {v3, v1}, Leb;->O00000Oo([BI)J

    move-result-wide v8

    invoke-virtual {v11, v8, v9}, Ldw;->a(J)V

    add-int/lit8 v0, v1, 0x8

    goto :goto_e

    :cond_21
    return v0

    :pswitch_9
    if-ne v6, v14, :cond_22

    invoke-static {v3, v4, v11, v7}, Leb;->O000000o([BILdi;Lby;)I

    move-result v0

    goto/16 :goto_14

    :cond_22
    if-eqz v6, :cond_23

    goto/16 :goto_13

    :cond_23
    move-object/from16 p6, p2

    move/from16 p7, p3

    move/from16 p8, p4

    move-object/from16 p9, v11

    move-object/from16 p10, p14

    invoke-static/range {p5 .. p10}, Leb;->O000000o(I[BIILdi;Lby;)I

    move-result v0

    return v0

    :pswitch_a
    if-ne v6, v14, :cond_24

    invoke-static {v3, v4, v11, v7}, Leb;->O00000Oo([BILdi;Lby;)I

    move-result v0

    goto/16 :goto_14

    :cond_24
    if-nez v6, :cond_2b

    check-cast v11, Ldw;

    invoke-static {v3, v4, v7}, Leb;->O00000Oo([BILby;)I

    move-result v0

    :goto_f
    iget-wide v8, v7, Lby;->b:J

    invoke-virtual {v11, v8, v9}, Ldw;->a(J)V

    if-ge v0, v5, :cond_25

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_25

    invoke-static {v3, v1, v7}, Leb;->O00000Oo([BILby;)I

    move-result v0

    goto :goto_f

    :cond_25
    return v0

    :pswitch_b
    if-ne v6, v14, :cond_26

    invoke-static {v3, v4, v11, v7}, Leb;->O00000oO([BILdi;Lby;)I

    move-result v0

    goto :goto_14

    :cond_26
    if-ne v6, v9, :cond_2b

    check-cast v11, Lcx;

    invoke-static/range {p2 .. p3}, Leb;->O00000o([BI)F

    move-result v0

    invoke-virtual {v11, v0}, Lcx;->a(F)V

    add-int/lit8 v0, v4, 0x4

    :goto_10
    if-ge v0, v5, :cond_27

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_27

    invoke-static {v3, v1}, Leb;->O00000o([BI)F

    move-result v0

    invoke-virtual {v11, v0}, Lcx;->a(F)V

    add-int/lit8 v0, v1, 0x4

    goto :goto_10

    :cond_27
    return v0

    :pswitch_c
    if-ne v6, v14, :cond_28

    invoke-static {v3, v4, v11, v7}, Leb;->O00000oo([BILdi;Lby;)I

    move-result v0

    goto :goto_14

    :cond_28
    if-ne v6, v10, :cond_2b

    check-cast v11, Lcp;

    invoke-static/range {p2 .. p3}, Leb;->O00000o0([BI)D

    move-result-wide v0

    invoke-virtual {v11, v0, v1}, Lcp;->O000000o(D)V

    add-int/lit8 v0, v4, 0x8

    :goto_11
    if-ge v0, v5, :cond_29

    invoke-static {v3, v0, v7}, Leb;->O000000o([BILby;)I

    move-result v1

    iget v4, v7, Lby;->a:I

    if-ne v2, v4, :cond_29

    invoke-static {v3, v1}, Leb;->O00000o0([BI)D

    move-result-wide v8

    invoke-virtual {v11, v8, v9}, Lcp;->O000000o(D)V

    add-int/lit8 v0, v1, 0x8

    goto :goto_11

    :cond_29
    return v0

    :goto_12
    if-ge v4, v5, :cond_2a

    invoke-static {v3, v4, v7}, Leb;->O000000o([BILby;)I

    move-result v6

    iget v8, v7, Lby;->a:I

    if-ne v2, v8, :cond_2a

    move-object/from16 p6, v0

    move-object/from16 p7, p2

    move/from16 p8, v6

    goto/16 :goto_1

    :cond_2a
    return v4

    :cond_2b
    :goto_13
    move v0, v4

    :cond_2c
    :goto_14
    return v0

    nop

    :pswitch_data_0
    .packed-switch 0x12
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_9
        :pswitch_2
        :pswitch_7
        :pswitch_8
        :pswitch_1
        :pswitch_0
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_9
        :pswitch_2
        :pswitch_7
        :pswitch_8
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private final O000000o(Ljava/lang/Object;[BIIIJLby;)I
    .locals 0

    sget-object p2, Lek;->b:Lsun/misc/Unsafe;

    invoke-direct {p0, p5}, Lek;->b(I)Ljava/lang/Object;

    move-result-object p0

    invoke-virtual {p2, p1, p6, p7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p3

    invoke-static {p3}, Lff;->O00000o(Ljava/lang/Object;)Z

    move-result p4

    if-eqz p4, :cond_0

    invoke-static {}, Lff;->a()Ljava/lang/Object;

    move-result-object p4

    invoke-static {p4, p3}, Lff;->O00000Oo(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    invoke-virtual {p2, p1, p6, p7, p4}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    :cond_0
    invoke-static {p0}, Lff;->O00000Oo(Ljava/lang/Object;)Lea;

    const/4 p0, 0x0

    throw p0
.end method

.method static O000000o(Lee;Lfs;Ldv;Lfh;Lej;Lff;)Lek;
    .locals 7

    instance-of v0, p0, Ler;

    if-eqz v0, :cond_0

    move-object v1, p0

    check-cast v1, Ler;

    move-object v2, p1

    move-object v3, p2

    move-object v4, p3

    move-object v5, p4

    move-object v6, p5

    invoke-static/range {v1 .. v6}, Lek;->O000000o(Ler;Lfs;Ldv;Lfh;Lej;Lff;)Lek;

    move-result-object p0

    return-object p0

    :cond_0
    check-cast p0, Lfd;

    const/4 p0, 0x0

    throw p0
.end method

.method static O000000o(Ler;Lfs;Ldv;Lfh;Lej;Lff;)Lek;
    .locals 36

    move-object/from16 v0, p0

    invoke-virtual/range {p0 .. p0}, Ler;->c()I

    move-result v1

    const/4 v2, 0x0

    const/4 v4, 0x2

    if-ne v1, v4, :cond_0

    const/4 v11, 0x1

    goto :goto_0

    :cond_0
    move v11, v2

    :goto_0
    iget-object v1, v0, Ler;->b:Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->length()I

    move-result v4

    invoke-virtual {v1, v2}, Ljava/lang/String;->charAt(I)C

    move-result v5

    const v6, 0xd800

    if-ge v5, v6, :cond_1

    const/4 v7, 0x1

    goto :goto_2

    :cond_1
    const/4 v5, 0x1

    :goto_1
    add-int/lit8 v7, v5, 0x1

    invoke-virtual {v1, v5}, Ljava/lang/String;->charAt(I)C

    move-result v5

    if-ge v5, v6, :cond_35

    :goto_2
    add-int/lit8 v5, v7, 0x1

    invoke-virtual {v1, v7}, Ljava/lang/String;->charAt(I)C

    move-result v7

    if-lt v7, v6, :cond_3

    and-int/lit16 v7, v7, 0x1fff

    const/16 v9, 0xd

    :goto_3
    add-int/lit8 v10, v5, 0x1

    invoke-virtual {v1, v5}, Ljava/lang/String;->charAt(I)C

    move-result v5

    if-lt v5, v6, :cond_2

    and-int/lit16 v5, v5, 0x1fff

    shl-int/2addr v5, v9

    or-int/2addr v7, v5

    add-int/lit8 v9, v9, 0xd

    move v5, v10

    goto :goto_3

    :cond_2
    shl-int/2addr v5, v9

    or-int/2addr v7, v5

    goto :goto_4

    :cond_3
    move v10, v5

    :goto_4
    if-nez v7, :cond_4

    sget-object v5, Lek;->a:[I

    move v7, v2

    move v9, v7

    move v13, v9

    move v14, v13

    move/from16 v16, v14

    move/from16 v17, v16

    move-object v15, v5

    move/from16 v5, v17

    goto/16 :goto_12

    :cond_4
    add-int/lit8 v5, v10, 0x1

    invoke-virtual {v1, v10}, Ljava/lang/String;->charAt(I)C

    move-result v7

    if-lt v7, v6, :cond_6

    and-int/lit16 v7, v7, 0x1fff

    const/16 v9, 0xd

    :goto_5
    add-int/lit8 v10, v5, 0x1

    invoke-virtual {v1, v5}, Ljava/lang/String;->charAt(I)C

    move-result v5

    if-lt v5, v6, :cond_5

    and-int/lit16 v5, v5, 0x1fff

    shl-int/2addr v5, v9

    or-int/2addr v7, v5

    add-int/lit8 v9, v9, 0xd

    move v5, v10

    goto :goto_5

    :cond_5
    shl-int/2addr v5, v9

    or-int/2addr v5, v7

    goto :goto_6

    :cond_6
    move v10, v5

    move v5, v7

    :goto_6
    add-int/lit8 v7, v10, 0x1

    invoke-virtual {v1, v10}, Ljava/lang/String;->charAt(I)C

    move-result v9

    if-lt v9, v6, :cond_8

    and-int/lit16 v9, v9, 0x1fff

    const/16 v10, 0xd

    :goto_7
    add-int/lit8 v12, v7, 0x1

    invoke-virtual {v1, v7}, Ljava/lang/String;->charAt(I)C

    move-result v7

    if-lt v7, v6, :cond_7

    and-int/lit16 v7, v7, 0x1fff

    shl-int/2addr v7, v10

    or-int/2addr v9, v7

    add-int/lit8 v10, v10, 0xd

    move v7, v12

    goto :goto_7

    :cond_7
    shl-int/2addr v7, v10

    or-int/2addr v9, v7

    goto :goto_8

    :cond_8
    move v12, v7

    :goto_8
    add-int/lit8 v7, v12, 0x1

    invoke-virtual {v1, v12}, Ljava/lang/String;->charAt(I)C

    move-result v10

    if-lt v10, v6, :cond_a

    and-int/lit16 v10, v10, 0x1fff

    const/16 v12, 0xd

    :goto_9
    add-int/lit8 v13, v7, 0x1

    invoke-virtual {v1, v7}, Ljava/lang/String;->charAt(I)C

    move-result v7

    if-lt v7, v6, :cond_9

    and-int/lit16 v7, v7, 0x1fff

    shl-int/2addr v7, v12

    or-int/2addr v10, v7

    add-int/lit8 v12, v12, 0xd

    move v7, v13

    goto :goto_9

    :cond_9
    shl-int/2addr v7, v12

    or-int/2addr v7, v10

    move v10, v7

    goto :goto_a

    :cond_a
    move v13, v7

    :goto_a
    add-int/lit8 v7, v13, 0x1

    invoke-virtual {v1, v13}, Ljava/lang/String;->charAt(I)C

    move-result v12

    if-lt v12, v6, :cond_c

    and-int/lit16 v12, v12, 0x1fff

    const/16 v13, 0xd

    :goto_b
    add-int/lit8 v14, v7, 0x1

    invoke-virtual {v1, v7}, Ljava/lang/String;->charAt(I)C

    move-result v7

    if-lt v7, v6, :cond_b

    and-int/lit16 v7, v7, 0x1fff

    shl-int/2addr v7, v13

    or-int/2addr v12, v7

    add-int/lit8 v13, v13, 0xd

    move v7, v14

    goto :goto_b

    :cond_b
    shl-int/2addr v7, v13

    or-int/2addr v7, v12

    move v12, v7

    goto :goto_c

    :cond_c
    move v14, v7

    :goto_c
    add-int/lit8 v7, v14, 0x1

    invoke-virtual {v1, v14}, Ljava/lang/String;->charAt(I)C

    move-result v13

    if-lt v13, v6, :cond_e

    and-int/lit16 v13, v13, 0x1fff

    const/16 v14, 0xd

    :goto_d
    add-int/lit8 v15, v7, 0x1

    invoke-virtual {v1, v7}, Ljava/lang/String;->charAt(I)C

    move-result v7

    if-lt v7, v6, :cond_d

    and-int/lit16 v7, v7, 0x1fff

    shl-int/2addr v7, v14

    or-int/2addr v13, v7

    add-int/lit8 v14, v14, 0xd

    move v7, v15

    goto :goto_d

    :cond_d
    shl-int/2addr v7, v14

    or-int/2addr v7, v13

    move v13, v7

    goto :goto_e

    :cond_e
    move v15, v7

    :goto_e
    add-int/lit8 v7, v15, 0x1

    invoke-virtual {v1, v15}, Ljava/lang/String;->charAt(I)C

    move-result v14

    if-lt v14, v6, :cond_10

    and-int/lit16 v14, v14, 0x1fff

    const/16 v15, 0xd

    :goto_f
    add-int/lit8 v16, v7, 0x1

    invoke-virtual {v1, v7}, Ljava/lang/String;->charAt(I)C

    move-result v7

    if-lt v7, v6, :cond_f

    and-int/lit16 v7, v7, 0x1fff

    shl-int/2addr v7, v15

    or-int/2addr v14, v7

    add-int/lit8 v15, v15, 0xd

    move/from16 v7, v16

    goto :goto_f

    :cond_f
    shl-int/2addr v7, v15

    or-int/2addr v7, v14

    move v14, v7

    move/from16 v7, v16

    :cond_10
    add-int/lit8 v15, v7, 0x1

    invoke-virtual {v1, v7}, Ljava/lang/String;->charAt(I)C

    move-result v7

    if-lt v7, v6, :cond_12

    and-int/lit16 v7, v7, 0x1fff

    const/16 v16, 0xd

    :goto_10
    add-int/lit8 v17, v15, 0x1

    invoke-virtual {v1, v15}, Ljava/lang/String;->charAt(I)C

    move-result v15

    if-lt v15, v6, :cond_11

    and-int/lit16 v15, v15, 0x1fff

    shl-int v15, v15, v16

    or-int/2addr v7, v15

    add-int/lit8 v16, v16, 0xd

    move/from16 v15, v17

    goto :goto_10

    :cond_11
    shl-int v15, v15, v16

    or-int/2addr v7, v15

    move/from16 v15, v17

    :cond_12
    add-int/lit8 v16, v15, 0x1

    invoke-virtual {v1, v15}, Ljava/lang/String;->charAt(I)C

    move-result v15

    if-lt v15, v6, :cond_14

    and-int/lit16 v15, v15, 0x1fff

    const/16 v17, 0xd

    move/from16 v34, v16

    move/from16 v16, v15

    move/from16 v15, v34

    :goto_11
    add-int/lit8 v18, v15, 0x1

    invoke-virtual {v1, v15}, Ljava/lang/String;->charAt(I)C

    move-result v15

    if-lt v15, v6, :cond_13

    and-int/lit16 v15, v15, 0x1fff

    shl-int v15, v15, v17

    or-int v16, v16, v15

    add-int/lit8 v17, v17, 0xd

    move/from16 v15, v18

    goto :goto_11

    :cond_13
    shl-int v15, v15, v17

    or-int v15, v16, v15

    move/from16 v16, v18

    :cond_14
    add-int v17, v15, v14

    add-int v7, v17, v7

    new-array v7, v7, [I

    add-int v17, v5, v5

    add-int v9, v17, v9

    move/from16 v17, v12

    move-object/from16 v34, v7

    move v7, v5

    move v5, v14

    move v14, v15

    move-object/from16 v15, v34

    move/from16 v35, v16

    move/from16 v16, v10

    move/from16 v10, v35

    :goto_12
    sget-object v12, Lek;->b:Lsun/misc/Unsafe;

    iget-object v2, v0, Ler;->c:[Ljava/lang/Object;

    iget-object v8, v0, Ler;->a:Leh;

    invoke-virtual {v8}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v8

    mul-int/lit8 v3, v13, 0x3

    new-array v3, v3, [I

    add-int/2addr v13, v13

    new-array v13, v13, [Ljava/lang/Object;

    add-int v22, v14, v5

    move/from16 v21, v9

    move/from16 v23, v14

    move/from16 v24, v22

    const/4 v5, 0x0

    const/4 v9, 0x0

    :goto_13
    if-ge v10, v4, :cond_34

    add-int/lit8 v25, v10, 0x1

    invoke-virtual {v1, v10}, Ljava/lang/String;->charAt(I)C

    move-result v10

    if-lt v10, v6, :cond_16

    and-int/lit16 v10, v10, 0x1fff

    const/16 v26, 0xd

    move/from16 v34, v25

    move/from16 v25, v10

    move/from16 v10, v34

    :goto_14
    add-int/lit8 v27, v10, 0x1

    invoke-virtual {v1, v10}, Ljava/lang/String;->charAt(I)C

    move-result v10

    if-lt v10, v6, :cond_15

    and-int/lit16 v10, v10, 0x1fff

    shl-int v10, v10, v26

    or-int v25, v25, v10

    add-int/lit8 v26, v26, 0xd

    move/from16 v10, v27

    goto :goto_14

    :cond_15
    shl-int v10, v10, v26

    or-int v10, v25, v10

    move/from16 v25, v10

    move/from16 v10, v27

    goto :goto_15

    :cond_16
    move/from16 v34, v25

    move/from16 v25, v10

    move/from16 v10, v34

    :goto_15
    add-int/lit8 v26, v10, 0x1

    invoke-virtual {v1, v10}, Ljava/lang/String;->charAt(I)C

    move-result v10

    if-lt v10, v6, :cond_18

    and-int/lit16 v10, v10, 0x1fff

    const/16 v27, 0xd

    move/from16 v34, v26

    move/from16 v26, v10

    move/from16 v10, v34

    :goto_16
    add-int/lit8 v28, v10, 0x1

    invoke-virtual {v1, v10}, Ljava/lang/String;->charAt(I)C

    move-result v10

    if-lt v10, v6, :cond_17

    and-int/lit16 v10, v10, 0x1fff

    shl-int v10, v10, v27

    or-int v26, v26, v10

    add-int/lit8 v27, v27, 0xd

    move/from16 v10, v28

    goto :goto_16

    :cond_17
    shl-int v10, v10, v27

    or-int v10, v26, v10

    move/from16 v6, v28

    goto :goto_17

    :cond_18
    move/from16 v6, v26

    :goto_17
    move/from16 v27, v4

    and-int/lit16 v4, v10, 0xff

    move/from16 v28, v14

    and-int/lit16 v14, v10, 0x400

    if-eqz v14, :cond_19

    add-int/lit8 v14, v5, 0x1

    aput v9, v15, v5

    move v5, v14

    :cond_19
    const/16 v14, 0x33

    if-ge v4, v14, :cond_29

    add-int/lit8 v14, v21, 0x1

    aget-object v21, v2, v21

    move/from16 v29, v5

    move-object/from16 v5, v21

    check-cast v5, Ljava/lang/String;

    invoke-static {v8, v5}, Lek;->a(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v5

    const/16 v0, 0x9

    if-ne v4, v0, :cond_1b

    :cond_1a
    const/16 v20, 0x1

    goto :goto_1a

    :cond_1b
    const/16 v0, 0x11

    if-eq v4, v0, :cond_1a

    const/16 v0, 0x1b

    if-ne v4, v0, :cond_1d

    :cond_1c
    const/16 v20, 0x1

    goto :goto_18

    :cond_1d
    const/16 v0, 0x31

    if-eq v4, v0, :cond_1c

    const/16 v0, 0xc

    if-eq v4, v0, :cond_1f

    const/16 v0, 0x1e

    if-eq v4, v0, :cond_1f

    const/16 v0, 0x2c

    if-eq v4, v0, :cond_1f

    const/16 v0, 0x32

    if-ne v4, v0, :cond_21

    add-int/lit8 v0, v23, 0x1

    aput v9, v15, v23

    div-int/lit8 v23, v9, 0x3

    add-int/lit8 v30, v14, 0x1

    add-int v23, v23, v23

    aget-object v14, v2, v14

    aput-object v14, v13, v23

    and-int/lit16 v14, v10, 0x800

    if-eqz v14, :cond_1e

    add-int/lit8 v14, v30, 0x1

    add-int/lit8 v23, v23, 0x1

    aget-object v30, v2, v30

    aput-object v30, v13, v23

    move/from16 v23, v0

    goto :goto_1b

    :cond_1e
    move/from16 v23, v0

    goto :goto_19

    :cond_1f
    if-nez v11, :cond_20

    div-int/lit8 v0, v9, 0x3

    add-int/lit8 v30, v14, 0x1

    add-int/2addr v0, v0

    const/16 v20, 0x1

    add-int/lit8 v0, v0, 0x1

    aget-object v14, v2, v14

    aput-object v14, v13, v0

    goto :goto_19

    :cond_20
    const/16 v20, 0x1

    goto :goto_1b

    :goto_18
    div-int/lit8 v0, v9, 0x3

    add-int/lit8 v30, v14, 0x1

    add-int/2addr v0, v0

    add-int/lit8 v0, v0, 0x1

    aget-object v14, v2, v14

    aput-object v14, v13, v0

    :goto_19
    move-object v0, v13

    goto :goto_1c

    :goto_1a
    div-int/lit8 v0, v9, 0x3

    add-int/2addr v0, v0

    add-int/lit8 v0, v0, 0x1

    invoke-virtual {v5}, Ljava/lang/reflect/Field;->getType()Ljava/lang/Class;

    move-result-object v30

    aput-object v30, v13, v0

    :cond_21
    :goto_1b
    move-object v0, v13

    move/from16 v30, v14

    :goto_1c
    invoke-virtual {v12, v5}, Lsun/misc/Unsafe;->objectFieldOffset(Ljava/lang/reflect/Field;)J

    move-result-wide v13

    long-to-int v5, v13

    and-int/lit16 v13, v10, 0x1000

    const/16 v14, 0x1000

    if-eq v13, v14, :cond_23

    :cond_22
    move/from16 v33, v7

    const/4 v7, 0x0

    const v31, 0xfffff

    goto :goto_1f

    :cond_23
    const/16 v13, 0x11

    if-gt v4, v13, :cond_22

    add-int/lit8 v13, v6, 0x1

    invoke-virtual {v1, v6}, Ljava/lang/String;->charAt(I)C

    move-result v6

    const v14, 0xd800

    if-lt v6, v14, :cond_25

    and-int/lit16 v6, v6, 0x1fff

    const/16 v31, 0xd

    :goto_1d
    add-int/lit8 v32, v13, 0x1

    invoke-virtual {v1, v13}, Ljava/lang/String;->charAt(I)C

    move-result v13

    if-lt v13, v14, :cond_24

    and-int/lit16 v13, v13, 0x1fff

    shl-int v13, v13, v31

    or-int/2addr v6, v13

    add-int/lit8 v31, v31, 0xd

    move/from16 v13, v32

    const v14, 0xd800

    goto :goto_1d

    :cond_24
    shl-int v13, v13, v31

    or-int/2addr v6, v13

    move/from16 v13, v32

    :cond_25
    add-int v14, v7, v7

    div-int/lit8 v31, v6, 0x20

    add-int v14, v14, v31

    move/from16 v33, v7

    aget-object v7, v2, v14

    move/from16 v31, v13

    instance-of v13, v7, Ljava/lang/reflect/Field;

    if-eqz v13, :cond_26

    check-cast v7, Ljava/lang/reflect/Field;

    goto :goto_1e

    :cond_26
    check-cast v7, Ljava/lang/String;

    invoke-static {v8, v7}, Lek;->a(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v7

    aput-object v7, v2, v14

    :goto_1e
    invoke-virtual {v12, v7}, Lsun/misc/Unsafe;->objectFieldOffset(Ljava/lang/reflect/Field;)J

    move-result-wide v13

    long-to-int v14, v13

    rem-int/lit8 v6, v6, 0x20

    move v7, v6

    move/from16 v6, v31

    move/from16 v31, v14

    :goto_1f
    const/16 v13, 0x12

    if-ge v4, v13, :cond_27

    goto :goto_20

    :cond_27
    const/16 v13, 0x31

    if-gt v4, v13, :cond_28

    add-int/lit8 v13, v24, 0x1

    aput v5, v15, v24

    move/from16 v24, v13

    :cond_28
    :goto_20
    move/from16 v21, v30

    const/16 v20, 0x1

    goto/16 :goto_29

    :cond_29
    move/from16 v29, v5

    move/from16 v33, v7

    move-object v0, v13

    add-int/lit8 v5, v6, 0x1

    invoke-virtual {v1, v6}, Ljava/lang/String;->charAt(I)C

    move-result v6

    const v13, 0xd800

    if-lt v6, v13, :cond_2b

    and-int/lit16 v6, v6, 0x1fff

    const/16 v7, 0xd

    :goto_21
    add-int/lit8 v14, v5, 0x1

    invoke-virtual {v1, v5}, Ljava/lang/String;->charAt(I)C

    move-result v5

    if-lt v5, v13, :cond_2a

    and-int/lit16 v5, v5, 0x1fff

    shl-int/2addr v5, v7

    or-int/2addr v6, v5

    add-int/lit8 v7, v7, 0xd

    move v5, v14

    goto :goto_21

    :cond_2a
    shl-int/2addr v5, v7

    or-int/2addr v6, v5

    goto :goto_22

    :cond_2b
    move v14, v5

    :goto_22
    add-int/lit8 v5, v4, -0x33

    const/16 v7, 0x9

    if-ne v5, v7, :cond_2c

    goto :goto_25

    :cond_2c
    const/16 v7, 0x11

    if-eq v5, v7, :cond_2f

    const/16 v7, 0xc

    if-eq v5, v7, :cond_2d

    goto :goto_23

    :cond_2d
    if-nez v11, :cond_2e

    div-int/lit8 v5, v9, 0x3

    add-int/lit8 v7, v21, 0x1

    add-int/2addr v5, v5

    const/16 v20, 0x1

    add-int/lit8 v5, v5, 0x1

    aget-object v21, v2, v21

    aput-object v21, v0, v5

    goto :goto_24

    :cond_2e
    :goto_23
    move/from16 v7, v21

    :goto_24
    const/16 v20, 0x1

    goto :goto_26

    :cond_2f
    :goto_25
    div-int/lit8 v5, v9, 0x3

    add-int/lit8 v7, v21, 0x1

    add-int/2addr v5, v5

    const/16 v20, 0x1

    add-int/lit8 v5, v5, 0x1

    aget-object v21, v2, v21

    aput-object v21, v0, v5

    :goto_26
    add-int/2addr v6, v6

    aget-object v5, v2, v6

    instance-of v13, v5, Ljava/lang/reflect/Field;

    if-eqz v13, :cond_30

    check-cast v5, Ljava/lang/reflect/Field;

    goto :goto_27

    :cond_30
    check-cast v5, Ljava/lang/String;

    invoke-static {v8, v5}, Lek;->a(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v5

    aput-object v5, v2, v6

    :goto_27
    move/from16 v21, v14

    invoke-virtual {v12, v5}, Lsun/misc/Unsafe;->objectFieldOffset(Ljava/lang/reflect/Field;)J

    move-result-wide v13

    long-to-int v5, v13

    add-int/lit8 v6, v6, 0x1

    aget-object v13, v2, v6

    instance-of v14, v13, Ljava/lang/reflect/Field;

    if-eqz v14, :cond_31

    check-cast v13, Ljava/lang/reflect/Field;

    goto :goto_28

    :cond_31
    check-cast v13, Ljava/lang/String;

    invoke-static {v8, v13}, Lek;->a(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v13

    aput-object v13, v2, v6

    :goto_28
    invoke-virtual {v12, v13}, Lsun/misc/Unsafe;->objectFieldOffset(Ljava/lang/reflect/Field;)J

    move-result-wide v13

    long-to-int v6, v13

    move/from16 v31, v6

    move/from16 v6, v21

    move/from16 v21, v7

    const/4 v7, 0x0

    :goto_29
    add-int/lit8 v13, v9, 0x1

    aput v25, v3, v9

    add-int/lit8 v9, v13, 0x1

    and-int/lit16 v14, v10, 0x200

    if-eqz v14, :cond_32

    const/high16 v14, 0x20000000

    goto :goto_2a

    :cond_32
    const/4 v14, 0x0

    :goto_2a
    and-int/lit16 v10, v10, 0x100

    if-eqz v10, :cond_33

    const/high16 v10, 0x10000000

    goto :goto_2b

    :cond_33
    const/4 v10, 0x0

    :goto_2b
    or-int/2addr v10, v14

    shl-int/lit8 v4, v4, 0x14

    or-int/2addr v4, v10

    or-int/2addr v4, v5

    aput v4, v3, v13

    add-int/lit8 v4, v9, 0x1

    shl-int/lit8 v5, v7, 0x14

    or-int v5, v5, v31

    aput v5, v3, v9

    move-object v13, v0

    move v9, v4

    move v10, v6

    move/from16 v4, v27

    move/from16 v14, v28

    move/from16 v5, v29

    move/from16 v7, v33

    const v6, 0xd800

    move-object/from16 v0, p0

    goto/16 :goto_13

    :cond_34
    move-object v0, v13

    move/from16 v28, v14

    new-instance v1, Lek;

    move-object v5, v1

    move-object/from16 v2, p0

    iget-object v10, v2, Ler;->a:Leh;

    const/4 v12, 0x0

    const/16 v21, 0x0

    move-object v6, v3

    move-object v7, v0

    move/from16 v8, v16

    move/from16 v9, v17

    move-object v13, v15

    move/from16 v15, v22

    move-object/from16 v16, p1

    move-object/from16 v17, p2

    move-object/from16 v18, p3

    move-object/from16 v19, p4

    move-object/from16 v20, p5

    invoke-direct/range {v5 .. v21}, Lek;-><init>([I[Ljava/lang/Object;IILeh;ZZ[IIILfs;Ldv;Lfh;Lej;Lff;[B)V

    return-object v1

    :cond_35
    move v5, v7

    goto/16 :goto_1
.end method

.method private static O000000o(Ljava/lang/Object;J)Ljava/util/List;
    .locals 0

    invoke-static {p0, p1, p2}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/util/List;

    return-object p0
.end method

.method private static final O000000o(ILjava/lang/Object;Lgb;)V
    .locals 1

    instance-of v0, p1, Ljava/lang/String;

    if-eqz v0, :cond_0

    check-cast p1, Ljava/lang/String;

    invoke-interface {p2, p0, p1}, Lgb;->a(ILjava/lang/String;)V

    return-void

    :cond_0
    check-cast p1, Lck;

    invoke-interface {p2, p0, p1}, Lgb;->O000000o(ILck;)V

    return-void
.end method

.method private static final O000000o(Lfh;Ljava/lang/Object;Lgb;)V
    .locals 0

    invoke-virtual {p0, p1}, Lfh;->O00000Oo(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    invoke-static {p0, p2}, Lfh;->O00000Oo(Ljava/lang/Object;Lgb;)V

    return-void
.end method

.method private final O000000o(Lgb;ILjava/lang/Object;I)V
    .locals 0

    if-nez p3, :cond_0

    return-void

    :cond_0
    invoke-direct {p0, p4}, Lek;->b(I)Ljava/lang/Object;

    move-result-object p0

    invoke-static {p0}, Lff;->O00000Oo(Ljava/lang/Object;)Lea;

    const/4 p0, 0x0

    throw p0
.end method

.method private final O000000o(Ljava/lang/Object;Ljava/lang/Object;I)V
    .locals 3

    invoke-direct {p0, p3}, Lek;->e(I)I

    move-result v0

    invoke-static {v0}, Lek;->O00oOoOo(I)J

    move-result-wide v0

    invoke-direct {p0, p2, p3}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v2

    if-eqz v2, :cond_2

    invoke-static {p1, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v2

    invoke-static {p2, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p2

    if-eqz v2, :cond_1

    if-nez p2, :cond_0

    goto :goto_1

    :cond_0
    invoke-static {v2, p2}, Ldj;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p2

    :goto_0
    invoke-static {p1, v0, v1, p2}, Lfr;->O000000o(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-direct {p0, p1, p3}, Lek;->O00000Oo(Ljava/lang/Object;I)V

    return-void

    :cond_1
    :goto_1
    if-eqz p2, :cond_2

    goto :goto_0

    :cond_2
    return-void
.end method

.method private final O000000o(Ljava/lang/Object;I)Z
    .locals 6

    invoke-direct {p0, p2}, Lek;->O000O0OO(I)I

    move-result v0

    const v1, 0xfffff

    and-int/2addr v1, v0

    int-to-long v1, v1

    const-wide/32 v3, 0xfffff

    cmp-long v3, v1, v3

    const/4 v4, 0x0

    const/4 v5, 0x1

    if-nez v3, :cond_14

    invoke-direct {p0, p2}, Lek;->e(I)I

    move-result p0

    invoke-static {p0}, Lek;->O00oOoOo(I)J

    move-result-wide v0

    invoke-static {p0}, Lek;->O000O0Oo(I)I

    move-result p0

    const-wide/16 v2, 0x0

    packed-switch p0, :pswitch_data_0

    new-instance p0, Ljava/lang/IllegalArgumentException;

    invoke-direct {p0}, Ljava/lang/IllegalArgumentException;-><init>()V

    throw p0

    :pswitch_0
    invoke-static {p1, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    if-eqz p0, :cond_0

    return v5

    :cond_0
    return v4

    :pswitch_1
    invoke-static {p1, v0, v1}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide p0

    cmp-long p0, p0, v2

    if-eqz p0, :cond_1

    return v5

    :cond_1
    return v4

    :pswitch_2
    invoke-static {p1, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    if-eqz p0, :cond_2

    return v5

    :cond_2
    return v4

    :pswitch_3
    invoke-static {p1, v0, v1}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide p0

    cmp-long p0, p0, v2

    if-eqz p0, :cond_3

    return v5

    :cond_3
    return v4

    :pswitch_4
    invoke-static {p1, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    if-eqz p0, :cond_4

    return v5

    :cond_4
    return v4

    :pswitch_5
    invoke-static {p1, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    if-eqz p0, :cond_5

    return v5

    :cond_5
    return v4

    :pswitch_6
    invoke-static {p1, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    if-eqz p0, :cond_6

    return v5

    :cond_6
    return v4

    :pswitch_7
    sget-object p0, Lck;->b:Lck;

    invoke-static {p1, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p1

    invoke-virtual {p0, p1}, Lck;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-nez p0, :cond_7

    return v5

    :cond_7
    return v4

    :pswitch_8
    invoke-static {p1, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    if-eqz p0, :cond_8

    return v5

    :cond_8
    return v4

    :pswitch_9
    invoke-static {p1, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    instance-of p1, p0, Ljava/lang/String;

    if-eqz p1, :cond_a

    check-cast p0, Ljava/lang/String;

    invoke-virtual {p0}, Ljava/lang/String;->isEmpty()Z

    move-result p0

    if-nez p0, :cond_9

    return v5

    :cond_9
    return v4

    :cond_a
    instance-of p1, p0, Lck;

    if-eqz p1, :cond_c

    sget-object p1, Lck;->b:Lck;

    invoke-virtual {p1, p0}, Lck;->equals(Ljava/lang/Object;)Z

    move-result p0

    if-nez p0, :cond_b

    return v5

    :cond_b
    return v4

    :cond_c
    new-instance p0, Ljava/lang/IllegalArgumentException;

    invoke-direct {p0}, Ljava/lang/IllegalArgumentException;-><init>()V

    throw p0

    :pswitch_a
    invoke-static {p1, v0, v1}, Lfr;->O00000o0(Ljava/lang/Object;J)Z

    move-result p0

    return p0

    :pswitch_b
    invoke-static {p1, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    if-eqz p0, :cond_d

    return v5

    :cond_d
    return v4

    :pswitch_c
    invoke-static {p1, v0, v1}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide p0

    cmp-long p0, p0, v2

    if-eqz p0, :cond_e

    return v5

    :cond_e
    return v4

    :pswitch_d
    invoke-static {p1, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    if-eqz p0, :cond_f

    return v5

    :cond_f
    return v4

    :pswitch_e
    invoke-static {p1, v0, v1}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide p0

    cmp-long p0, p0, v2

    if-eqz p0, :cond_10

    return v5

    :cond_10
    return v4

    :pswitch_f
    invoke-static {p1, v0, v1}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide p0

    cmp-long p0, p0, v2

    if-eqz p0, :cond_11

    return v5

    :cond_11
    return v4

    :pswitch_10
    invoke-static {p1, v0, v1}, Lfr;->O00000o(Ljava/lang/Object;J)F

    move-result p0

    const/4 p1, 0x0

    cmpl-float p0, p0, p1

    if-eqz p0, :cond_12

    return v5

    :cond_12
    return v4

    :pswitch_11
    invoke-static {p1, v0, v1}, Lfr;->O00000oO(Ljava/lang/Object;J)D

    move-result-wide p0

    const-wide/16 v0, 0x0

    cmpl-double p0, p0, v0

    if-eqz p0, :cond_13

    return v5

    :cond_13
    return v4

    :cond_14
    invoke-static {p1, v1, v2}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    ushr-int/lit8 p1, v0, 0x14

    shl-int p1, v5, p1

    and-int/2addr p0, p1

    if-eqz p0, :cond_15

    return v5

    :cond_15
    return v4

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private final O000000o(Ljava/lang/Object;II)Z
    .locals 2

    invoke-direct {p0, p3}, Lek;->O000O0OO(I)I

    move-result p0

    const p3, 0xfffff

    and-int/2addr p0, p3

    int-to-long v0, p0

    invoke-static {p1, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p0

    if-ne p0, p2, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method private final O000000o(Ljava/lang/Object;IIII)Z
    .locals 1

    const v0, 0xfffff

    if-eq p3, v0, :cond_1

    and-int p0, p4, p5

    if-eqz p0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0

    :cond_1
    invoke-direct {p0, p1, p2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result p0

    return p0
.end method

.method private static O000000o(Ljava/lang/Object;ILes;)Z
    .locals 2

    invoke-static {p1}, Lek;->O00oOoOo(I)J

    move-result-wide v0

    invoke-static {p0, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    invoke-interface {p2, p0}, Les;->O00000o0(Ljava/lang/Object;)Z

    move-result p0

    return p0
.end method

.method private static O00000Oo(Ljava/lang/Object;J)D
    .locals 0

    invoke-static {p0, p1, p2}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Double;

    invoke-virtual {p0}, Ljava/lang/Double;->doubleValue()D

    move-result-wide p0

    return-wide p0
.end method

.method private final O00000Oo(Ljava/lang/Object;I)V
    .locals 4

    invoke-direct {p0, p2}, Lek;->O000O0OO(I)I

    move-result p0

    const p2, 0xfffff

    and-int/2addr p2, p0

    int-to-long v0, p2

    const-wide/32 v2, 0xfffff

    cmp-long p2, v0, v2

    if-eqz p2, :cond_0

    invoke-static {p1, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result p2

    const/4 v2, 0x1

    ushr-int/lit8 p0, p0, 0x14

    shl-int p0, v2, p0

    or-int/2addr p0, p2

    invoke-static {p1, v0, v1, p0}, Lfr;->O000000o(Ljava/lang/Object;JI)V

    :cond_0
    return-void
.end method

.method private final O00000Oo(Ljava/lang/Object;II)V
    .locals 2

    invoke-direct {p0, p3}, Lek;->O000O0OO(I)I

    move-result p0

    const p3, 0xfffff

    and-int/2addr p0, p3

    int-to-long v0, p0

    invoke-static {p1, v0, v1, p2}, Lfr;->O000000o(Ljava/lang/Object;JI)V

    return-void
.end method

.method private final O00000Oo(Ljava/lang/Object;Lgb;)V
    .locals 18

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move-object/from16 v2, p2

    iget-boolean v3, v0, Lek;->h:Z

    if-eqz v3, :cond_0

    invoke-static/range {p1 .. p1}, Lej;->O00000Oo(Ljava/lang/Object;)Lcu;

    move-result-object v3

    invoke-virtual {v3}, Lcu;->a()Z

    move-result v5

    if-nez v5, :cond_0

    invoke-virtual {v3}, Lcu;->d()Ljava/util/Iterator;

    move-result-object v3

    invoke-interface {v3}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Ljava/util/Map$Entry;

    goto :goto_0

    :cond_0
    const/4 v3, 0x0

    const/4 v5, 0x0

    :goto_0
    iget-object v6, v0, Lek;->c:[I

    array-length v6, v6

    sget-object v7, Lek;->b:Lsun/misc/Unsafe;

    const v8, 0xfffff

    move-object v12, v5

    move v10, v8

    const/4 v5, 0x0

    const/4 v11, 0x0

    :goto_1
    if-ge v5, v6, :cond_9

    invoke-direct {v0, v5}, Lek;->e(I)I

    move-result v13

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v14

    invoke-static {v13}, Lek;->O000O0Oo(I)I

    move-result v15

    iget-boolean v4, v0, Lek;->i:Z

    if-eqz v4, :cond_2

    :cond_1
    const/4 v9, 0x0

    goto :goto_3

    :cond_2
    const/16 v4, 0x11

    if-gt v15, v4, :cond_1

    iget-object v4, v0, Lek;->c:[I

    add-int/lit8 v16, v5, 0x2

    aget v4, v4, v16

    and-int v9, v4, v8

    if-eq v9, v10, :cond_3

    int-to-long v10, v9

    invoke-virtual {v7, v1, v10, v11}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v11

    goto :goto_2

    :cond_3
    move v9, v10

    :goto_2
    ushr-int/lit8 v4, v4, 0x14

    const/4 v10, 0x1

    shl-int v4, v10, v4

    move v10, v9

    move v9, v4

    :goto_3
    if-nez v12, :cond_5

    :cond_4
    move/from16 v17, v9

    goto :goto_4

    :cond_5
    invoke-static {v12}, Lej;->O000000o(Ljava/util/Map$Entry;)I

    move-result v4

    if-gt v4, v14, :cond_4

    invoke-static {v2, v12}, Lej;->O000000o(Lgb;Ljava/util/Map$Entry;)V

    invoke-interface {v3}, Ljava/util/Iterator;->hasNext()Z

    move-result v4

    if-eqz v4, :cond_6

    invoke-interface {v3}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v4

    move-object v12, v4

    check-cast v12, Ljava/util/Map$Entry;

    goto :goto_3

    :cond_6
    const/4 v12, 0x0

    goto :goto_3

    :goto_4
    invoke-static {v13}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    packed-switch v15, :pswitch_data_0

    :cond_7
    :goto_5
    const/4 v15, 0x0

    goto/16 :goto_7

    :pswitch_0
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    invoke-direct {v0, v5}, Lek;->a(I)Les;

    move-result-object v9

    invoke-interface {v2, v14, v8, v9}, Lgb;->O000000o(ILjava/lang/Object;Les;)V

    goto :goto_5

    :pswitch_1
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O00000o0(IJ)V

    goto :goto_5

    :pswitch_2
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O00000o(II)V

    goto :goto_5

    :pswitch_3
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O00000o(IJ)V

    goto :goto_5

    :pswitch_4
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->a(II)V

    goto :goto_5

    :pswitch_5
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O00000oO(II)V

    goto :goto_5

    :pswitch_6
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O00000o0(II)V

    goto :goto_5

    :pswitch_7
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Lck;

    invoke-interface {v2, v14, v8}, Lgb;->O000000o(ILck;)V

    goto :goto_5

    :pswitch_8
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    invoke-direct {v0, v5}, Lek;->a(I)Les;

    move-result-object v9

    invoke-interface {v2, v14, v8, v9}, Lgb;->O00000Oo(ILjava/lang/Object;Les;)V

    goto/16 :goto_5

    :pswitch_9
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    invoke-static {v14, v8, v2}, Lek;->O000000o(ILjava/lang/Object;Lgb;)V

    goto/16 :goto_5

    :pswitch_a
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000oo(Ljava/lang/Object;J)Z

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O000000o(IZ)V

    goto/16 :goto_5

    :pswitch_b
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O00000Oo(II)V

    goto/16 :goto_5

    :pswitch_c
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O00000Oo(IJ)V

    goto/16 :goto_5

    :pswitch_d
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O000000o(II)V

    goto/16 :goto_5

    :pswitch_e
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O000000o(IJ)V

    goto/16 :goto_5

    :pswitch_f
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->a(IJ)V

    goto/16 :goto_5

    :pswitch_10
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000o0(Ljava/lang/Object;J)F

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O000000o(IF)V

    goto/16 :goto_5

    :pswitch_11
    invoke-direct {v0, v1, v14, v5}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v13

    if-eqz v13, :cond_7

    invoke-static {v1, v8, v9}, Lek;->O00000Oo(Ljava/lang/Object;J)D

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O000000o(ID)V

    goto/16 :goto_5

    :pswitch_12
    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    invoke-direct {v0, v2, v14, v8, v5}, Lek;->O000000o(Lgb;ILjava/lang/Object;I)V

    goto/16 :goto_5

    :pswitch_13
    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-direct {v0, v5}, Lek;->a(I)Les;

    move-result-object v9

    invoke-static {v13, v8, v2, v9}, Leu;->O00000Oo(ILjava/util/List;Lgb;Les;)V

    goto/16 :goto_5

    :pswitch_14
    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    const/4 v14, 0x1

    invoke-static {v13, v8, v2, v14}, Leu;->O00000oO(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_15
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000Oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_16
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000O0o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_17
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000Ooo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_18
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000o00(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_19
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000Oo0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_1a
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000o0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_1b
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000OoO(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_1c
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O00000oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_1d
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000OOo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_1e
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O00000o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_1f
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O00000o0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_20
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O00000Oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_21
    const/4 v14, 0x1

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O000000o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_5

    :pswitch_22
    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    const/4 v14, 0x0

    invoke-static {v13, v8, v2, v14}, Leu;->O00000oO(ILjava/util/List;Lgb;Z)V

    goto :goto_6

    :pswitch_23
    const/4 v14, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000Oo(ILjava/util/List;Lgb;Z)V

    goto :goto_6

    :pswitch_24
    const/4 v14, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000O0o(ILjava/util/List;Lgb;Z)V

    goto :goto_6

    :pswitch_25
    const/4 v14, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000Ooo(ILjava/util/List;Lgb;Z)V

    goto :goto_6

    :pswitch_26
    const/4 v14, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000o00(ILjava/util/List;Lgb;Z)V

    goto :goto_6

    :pswitch_27
    const/4 v14, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v14}, Leu;->O0000Oo0(ILjava/util/List;Lgb;Z)V

    :goto_6
    move v15, v14

    goto/16 :goto_7

    :pswitch_28
    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2}, Leu;->O00000Oo(ILjava/util/List;Lgb;)V

    goto/16 :goto_5

    :pswitch_29
    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-direct {v0, v5}, Lek;->a(I)Les;

    move-result-object v9

    invoke-static {v13, v8, v2, v9}, Leu;->O000000o(ILjava/util/List;Lgb;Les;)V

    goto/16 :goto_5

    :pswitch_2a
    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2}, Leu;->O000000o(ILjava/util/List;Lgb;)V

    goto/16 :goto_5

    :pswitch_2b
    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    const/4 v15, 0x0

    invoke-static {v13, v8, v2, v15}, Leu;->O0000o0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_7

    :pswitch_2c
    const/4 v15, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v15}, Leu;->O0000OoO(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_7

    :pswitch_2d
    const/4 v15, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v15}, Leu;->O00000oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_7

    :pswitch_2e
    const/4 v15, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v15}, Leu;->O0000OOo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_7

    :pswitch_2f
    const/4 v15, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v15}, Leu;->O00000o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_7

    :pswitch_30
    const/4 v15, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v15}, Leu;->O00000o0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_7

    :pswitch_31
    const/4 v15, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v15}, Leu;->O00000Oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_7

    :pswitch_32
    const/4 v15, 0x0

    invoke-direct {v0, v5}, Lek;->d(I)I

    move-result v13

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Ljava/util/List;

    invoke-static {v13, v8, v2, v15}, Leu;->O000000o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_7

    :pswitch_33
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    invoke-direct {v0, v5}, Lek;->a(I)Les;

    move-result-object v9

    invoke-interface {v2, v14, v8, v9}, Lgb;->O000000o(ILjava/lang/Object;Les;)V

    goto/16 :goto_7

    :pswitch_34
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getLong(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O00000o0(IJ)V

    goto/16 :goto_7

    :pswitch_35
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O00000o(II)V

    goto/16 :goto_7

    :pswitch_36
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getLong(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O00000o(IJ)V

    goto/16 :goto_7

    :pswitch_37
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->a(II)V

    goto/16 :goto_7

    :pswitch_38
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O00000oO(II)V

    goto/16 :goto_7

    :pswitch_39
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O00000o0(II)V

    goto/16 :goto_7

    :pswitch_3a
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Lck;

    invoke-interface {v2, v14, v8}, Lgb;->O000000o(ILck;)V

    goto/16 :goto_7

    :pswitch_3b
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    invoke-direct {v0, v5}, Lek;->a(I)Les;

    move-result-object v9

    invoke-interface {v2, v14, v8, v9}, Lgb;->O00000Oo(ILjava/lang/Object;Les;)V

    goto/16 :goto_7

    :pswitch_3c
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v8

    invoke-static {v14, v8, v2}, Lek;->O000000o(ILjava/lang/Object;Lgb;)V

    goto/16 :goto_7

    :pswitch_3d
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-static {v1, v8, v9}, Lfr;->O00000o0(Ljava/lang/Object;J)Z

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O000000o(IZ)V

    goto :goto_7

    :pswitch_3e
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O00000Oo(II)V

    goto :goto_7

    :pswitch_3f
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getLong(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O00000Oo(IJ)V

    goto :goto_7

    :pswitch_40
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O000000o(II)V

    goto :goto_7

    :pswitch_41
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getLong(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O000000o(IJ)V

    goto :goto_7

    :pswitch_42
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-virtual {v7, v1, v8, v9}, Lsun/misc/Unsafe;->getLong(Ljava/lang/Object;J)J

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->a(IJ)V

    goto :goto_7

    :pswitch_43
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-static {v1, v8, v9}, Lfr;->O00000o(Ljava/lang/Object;J)F

    move-result v8

    invoke-interface {v2, v14, v8}, Lgb;->O000000o(IF)V

    goto :goto_7

    :pswitch_44
    const/4 v15, 0x0

    and-int v13, v11, v17

    if-eqz v13, :cond_8

    invoke-static {v1, v8, v9}, Lfr;->O00000oO(Ljava/lang/Object;J)D

    move-result-wide v8

    invoke-interface {v2, v14, v8, v9}, Lgb;->O000000o(ID)V

    :cond_8
    :goto_7
    add-int/lit8 v5, v5, 0x3

    const v8, 0xfffff

    goto/16 :goto_1

    :cond_9
    :goto_8
    if-eqz v12, :cond_b

    invoke-static {v2, v12}, Lej;->O000000o(Lgb;Ljava/util/Map$Entry;)V

    invoke-interface {v3}, Ljava/util/Iterator;->hasNext()Z

    move-result v4

    if-eqz v4, :cond_a

    invoke-interface {v3}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v4

    move-object v12, v4

    check-cast v12, Ljava/util/Map$Entry;

    goto :goto_8

    :cond_a
    const/4 v12, 0x0

    goto :goto_8

    :cond_b
    iget-object v0, v0, Lek;->o:Lfh;

    invoke-static {v0, v1, v2}, Lek;->O000000o(Lfh;Ljava/lang/Object;Lgb;)V

    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_44
        :pswitch_43
        :pswitch_42
        :pswitch_41
        :pswitch_40
        :pswitch_3f
        :pswitch_3e
        :pswitch_3d
        :pswitch_3c
        :pswitch_3b
        :pswitch_3a
        :pswitch_39
        :pswitch_38
        :pswitch_37
        :pswitch_36
        :pswitch_35
        :pswitch_34
        :pswitch_33
        :pswitch_32
        :pswitch_31
        :pswitch_30
        :pswitch_2f
        :pswitch_2e
        :pswitch_2d
        :pswitch_2c
        :pswitch_2b
        :pswitch_2a
        :pswitch_29
        :pswitch_28
        :pswitch_27
        :pswitch_26
        :pswitch_25
        :pswitch_24
        :pswitch_23
        :pswitch_22
        :pswitch_21
        :pswitch_20
        :pswitch_1f
        :pswitch_1e
        :pswitch_1d
        :pswitch_1c
        :pswitch_1b
        :pswitch_1a
        :pswitch_19
        :pswitch_18
        :pswitch_17
        :pswitch_16
        :pswitch_15
        :pswitch_14
        :pswitch_13
        :pswitch_12
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private final O00000Oo(Ljava/lang/Object;Ljava/lang/Object;I)V
    .locals 4

    invoke-direct {p0, p3}, Lek;->e(I)I

    move-result v0

    invoke-direct {p0, p3}, Lek;->d(I)I

    move-result v1

    invoke-static {v0}, Lek;->O00oOoOo(I)J

    move-result-wide v2

    invoke-direct {p0, p2, v1, p3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-static {p1, v2, v3}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v0

    invoke-static {p2, v2, v3}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p2

    if-eqz v0, :cond_1

    if-nez p2, :cond_0

    goto :goto_1

    :cond_0
    invoke-static {v0, p2}, Ldj;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p2

    :goto_0
    invoke-static {p1, v2, v3, p2}, Lfr;->O000000o(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-direct {p0, p1, v1, p3}, Lek;->O00000Oo(Ljava/lang/Object;II)V

    return-void

    :cond_1
    :goto_1
    if-eqz p2, :cond_2

    goto :goto_0

    :cond_2
    return-void
.end method

.method private final O00000Oo(Ljava/lang/Object;[BIILby;)V
    .locals 30

    move-object/from16 v15, p0

    move-object/from16 v14, p1

    move-object/from16 v12, p2

    move/from16 v13, p4

    move-object/from16 v11, p5

    sget-object v9, Lek;->b:Lsun/misc/Unsafe;

    const/4 v10, -0x1

    const/16 v16, 0x0

    move/from16 v0, p3

    move v1, v10

    move/from16 v2, v16

    move v6, v2

    const v7, 0xfffff

    :goto_0
    if-ge v0, v13, :cond_17

    add-int/lit8 v3, v0, 0x1

    aget-byte v0, v12, v0

    if-gez v0, :cond_0

    invoke-static {v0, v12, v3, v11}, Leb;->O000000o(I[BILby;)I

    move-result v0

    iget v3, v11, Lby;->a:I

    move v4, v0

    move/from16 v17, v3

    goto :goto_1

    :cond_0
    move/from16 v17, v0

    move v4, v3

    :goto_1
    ushr-int/lit8 v5, v17, 0x3

    and-int/lit8 v3, v17, 0x7

    if-le v5, v1, :cond_1

    div-int/lit8 v2, v2, 0x3

    invoke-direct {v15, v5, v2}, Lek;->a(II)I

    move-result v0

    goto :goto_2

    :cond_1
    invoke-direct {v15, v5}, Lek;->O000O0o0(I)I

    move-result v0

    :goto_2
    move v2, v0

    if-eq v2, v10, :cond_16

    iget-object v0, v15, Lek;->c:[I

    add-int/lit8 v1, v2, 0x1

    aget v1, v0, v1

    invoke-static {v1}, Lek;->O000O0Oo(I)I

    move-result v0

    move-object/from16 v18, v9

    invoke-static {v1}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    const/16 v10, 0x11

    move/from16 p3, v5

    if-gt v0, v10, :cond_d

    iget-object v10, v15, Lek;->c:[I

    add-int/lit8 v21, v2, 0x2

    aget v10, v10, v21

    ushr-int/lit8 v21, v10, 0x14

    const/4 v5, 0x1

    shl-int v21, v5, v21

    const v13, 0xfffff

    and-int/2addr v10, v13

    move/from16 v23, v1

    move/from16 v19, v2

    if-ne v10, v7, :cond_2

    move v10, v7

    move-object/from16 v7, v18

    goto :goto_4

    :cond_2
    if-eq v7, v13, :cond_3

    int-to-long v1, v7

    move-object/from16 v7, v18

    invoke-virtual {v7, v14, v1, v2, v6}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    goto :goto_3

    :cond_3
    move-object/from16 v7, v18

    :goto_3
    if-eq v10, v13, :cond_4

    int-to-long v1, v10

    invoke-virtual {v7, v14, v1, v2}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v6

    :cond_4
    :goto_4
    const/4 v1, 0x5

    packed-switch v0, :pswitch_data_0

    move/from16 v18, v13

    move/from16 v13, v19

    move/from16 v19, p3

    goto/16 :goto_f

    :pswitch_0
    if-nez v3, :cond_5

    invoke-static {v12, v4, v11}, Leb;->O00000Oo([BILby;)I

    move-result v17

    iget-wide v0, v11, Lby;->b:J

    invoke-static {v0, v1}, Lcl;->a(J)J

    move-result-wide v4

    move-object v0, v7

    move-object/from16 v1, p1

    move/from16 v13, v19

    move-wide v2, v8

    move/from16 v19, p3

    goto/16 :goto_c

    :cond_5
    move/from16 v13, v19

    move/from16 v19, p3

    goto :goto_5

    :pswitch_1
    move/from16 v13, v19

    move/from16 v19, p3

    if-nez v3, :cond_8

    invoke-static {v12, v4, v11}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v1, v11, Lby;->a:I

    invoke-static {v1}, Lcl;->a(I)I

    move-result v1

    goto/16 :goto_b

    :pswitch_2
    move/from16 v13, v19

    move/from16 v19, p3

    if-nez v3, :cond_8

    goto/16 :goto_a

    :pswitch_3
    move/from16 v13, v19

    const/4 v0, 0x2

    move/from16 v19, p3

    if-ne v3, v0, :cond_8

    invoke-static {v12, v4, v11}, Leb;->O00000oO([BILby;)I

    move-result v0

    iget-object v1, v11, Lby;->c:Ljava/lang/Object;

    invoke-virtual {v7, v14, v8, v9, v1}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    goto/16 :goto_d

    :pswitch_4
    move/from16 v13, v19

    const/4 v0, 0x2

    move/from16 v19, p3

    if-ne v3, v0, :cond_7

    invoke-direct {v15, v13}, Lek;->a(I)Les;

    move-result-object v0

    move/from16 v2, p4

    const v18, 0xfffff

    invoke-static {v0, v12, v4, v2, v11}, Leb;->O000000o(Les;[BIILby;)I

    move-result v0

    invoke-virtual {v7, v14, v8, v9}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_6

    iget-object v1, v11, Lby;->c:Ljava/lang/Object;

    goto :goto_7

    :cond_6
    iget-object v3, v11, Lby;->c:Ljava/lang/Object;

    invoke-static {v1, v3}, Ldj;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    goto :goto_7

    :cond_7
    move/from16 v2, p4

    :cond_8
    :goto_5
    const v18, 0xfffff

    goto/16 :goto_f

    :pswitch_5
    move/from16 v2, p4

    move/from16 v18, v13

    move/from16 v13, v19

    const/4 v0, 0x2

    move/from16 v19, p3

    if-eq v3, v0, :cond_9

    goto/16 :goto_f

    :cond_9
    const/high16 v0, 0x20000000

    and-int v0, v23, v0

    if-eqz v0, :cond_a

    invoke-static {v12, v4, v11}, Leb;->O00000o([BILby;)I

    move-result v0

    goto :goto_6

    :cond_a
    invoke-static {v12, v4, v11}, Leb;->O00000o0([BILby;)I

    move-result v0

    :goto_6
    iget-object v1, v11, Lby;->c:Ljava/lang/Object;

    :goto_7
    invoke-virtual {v7, v14, v8, v9, v1}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    goto :goto_9

    :pswitch_6
    move/from16 v2, p4

    move/from16 v18, v13

    move/from16 v13, v19

    move/from16 v19, p3

    if-nez v3, :cond_c

    invoke-static {v12, v4, v11}, Leb;->O00000Oo([BILby;)I

    move-result v0

    iget-wide v3, v11, Lby;->b:J

    const-wide/16 v22, 0x0

    cmp-long v1, v3, v22

    if-eqz v1, :cond_b

    goto :goto_8

    :cond_b
    move/from16 v5, v16

    :goto_8
    invoke-static {v14, v8, v9, v5}, Lfr;->O000000o(Ljava/lang/Object;JZ)V

    goto :goto_9

    :pswitch_7
    move/from16 v2, p4

    move/from16 v18, v13

    move/from16 v13, v19

    move/from16 v19, p3

    if-ne v3, v1, :cond_c

    invoke-static {v12, v4}, Leb;->O000000o([BI)I

    move-result v0

    invoke-virtual {v7, v14, v8, v9, v0}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    add-int/lit8 v0, v4, 0x4

    :goto_9
    or-int v6, v6, v21

    move-object v9, v7

    move v7, v10

    move/from16 v1, v19

    const/4 v10, -0x1

    move/from16 v29, v13

    move v13, v2

    move/from16 v2, v29

    goto/16 :goto_0

    :pswitch_8
    move/from16 v2, p4

    move/from16 v18, v13

    move/from16 v13, v19

    move/from16 v19, p3

    if-ne v3, v5, :cond_c

    invoke-static {v12, v4}, Leb;->O00000Oo([BI)J

    move-result-wide v22

    move-object v0, v7

    move-object/from16 v1, p1

    move-wide v2, v8

    move v8, v4

    move-wide/from16 v4, v22

    invoke-virtual/range {v0 .. v5}, Lsun/misc/Unsafe;->putLong(Ljava/lang/Object;JJ)V

    add-int/lit8 v0, v8, 0x8

    goto :goto_d

    :pswitch_9
    move/from16 v18, v13

    move/from16 v13, v19

    move/from16 v19, p3

    if-nez v3, :cond_c

    :goto_a
    invoke-static {v12, v4, v11}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v1, v11, Lby;->a:I

    :goto_b
    invoke-virtual {v7, v14, v8, v9, v1}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    goto :goto_d

    :pswitch_a
    move/from16 v18, v13

    move/from16 v13, v19

    move/from16 v19, p3

    if-nez v3, :cond_c

    invoke-static {v12, v4, v11}, Leb;->O00000Oo([BILby;)I

    move-result v17

    iget-wide v4, v11, Lby;->b:J

    move-object v0, v7

    move-object/from16 v1, p1

    move-wide v2, v8

    :goto_c
    invoke-virtual/range {v0 .. v5}, Lsun/misc/Unsafe;->putLong(Ljava/lang/Object;JJ)V

    or-int v6, v6, v21

    move-object v9, v7

    move v7, v10

    move v2, v13

    move/from16 v0, v17

    goto :goto_e

    :pswitch_b
    move/from16 v18, v13

    move/from16 v13, v19

    move/from16 v19, p3

    if-ne v3, v1, :cond_c

    invoke-static {v12, v4}, Leb;->O00000o([BI)F

    move-result v0

    invoke-static {v14, v8, v9, v0}, Lfr;->O000000o(Ljava/lang/Object;JF)V

    add-int/lit8 v0, v4, 0x4

    goto :goto_d

    :pswitch_c
    move/from16 v18, v13

    move/from16 v13, v19

    move/from16 v19, p3

    if-ne v3, v5, :cond_c

    invoke-static {v12, v4}, Leb;->O00000o0([BI)D

    move-result-wide v0

    invoke-static {v14, v8, v9, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;JD)V

    add-int/lit8 v0, v4, 0x8

    :goto_d
    or-int v6, v6, v21

    move-object v9, v7

    move v7, v10

    move v2, v13

    :goto_e
    move/from16 v1, v19

    const/4 v10, -0x1

    move/from16 v13, p4

    goto/16 :goto_0

    :cond_c
    :goto_f
    move v2, v4

    move-object v8, v7

    move/from16 v25, v10

    move/from16 v20, v13

    move-object v7, v14

    move-object v9, v15

    const/16 v18, -0x1

    goto/16 :goto_13

    :cond_d
    move/from16 v19, p3

    move/from16 v23, v1

    move v13, v2

    move-object/from16 v10, v18

    const v18, 0xfffff

    const/16 v1, 0x1b

    if-eq v0, v1, :cond_12

    const/16 v1, 0x31

    if-gt v0, v1, :cond_f

    move/from16 v1, v23

    int-to-long v1, v1

    move v5, v0

    move-object/from16 v0, p0

    move-wide/from16 v21, v1

    move-object/from16 v1, p1

    move-object/from16 v2, p2

    move/from16 p3, v3

    move v3, v4

    move v15, v4

    move/from16 v4, p4

    move/from16 v23, v5

    move/from16 v5, v17

    move/from16 v24, v6

    move/from16 v6, v19

    move/from16 v25, v7

    move/from16 v7, p3

    move-wide/from16 v26, v8

    move/from16 v9, v18

    move v8, v13

    move-object/from16 v28, v10

    const/16 v18, -0x1

    move-wide/from16 v9, v21

    move/from16 v11, v23

    move/from16 v20, v13

    move-wide/from16 v12, v26

    move-object/from16 v14, p5

    invoke-direct/range {v0 .. v14}, Lek;->O000000o(Ljava/lang/Object;[BIIIIIIJIJLby;)I

    move-result v0

    if-eq v0, v15, :cond_e

    :goto_10
    move-object/from16 v15, p0

    move-object/from16 v14, p1

    move-object/from16 v12, p2

    move/from16 v13, p4

    move-object/from16 v11, p5

    move/from16 v10, v18

    move/from16 v1, v19

    move/from16 v2, v20

    move/from16 v6, v24

    move/from16 v7, v25

    move-object/from16 v9, v28

    goto/16 :goto_0

    :cond_e
    move-object/from16 v9, p0

    move-object/from16 v7, p1

    move v2, v0

    move/from16 v6, v24

    move-object/from16 v8, v28

    goto/16 :goto_13

    :cond_f
    move/from16 p3, v3

    move v15, v4

    move/from16 v24, v6

    move/from16 v25, v7

    move-wide/from16 v26, v8

    move-object/from16 v28, v10

    move/from16 v20, v13

    move/from16 v1, v23

    const/16 v18, -0x1

    move/from16 v23, v0

    const/16 v0, 0x32

    move/from16 v9, v23

    if-eq v9, v0, :cond_10

    move-object/from16 v0, p0

    move v8, v1

    move-object/from16 v1, p1

    move-object/from16 v2, p2

    move v3, v15

    move/from16 v4, p4

    move/from16 v5, v17

    move/from16 v6, v19

    move/from16 v7, p3

    move-wide/from16 v10, v26

    move/from16 v12, v20

    move-object/from16 v13, p5

    invoke-direct/range {v0 .. v13}, Lek;->O000000o(Ljava/lang/Object;[BIIIIIIIJILby;)I

    move-result v0

    if-eq v0, v15, :cond_e

    goto :goto_10

    :cond_10
    move/from16 v0, p3

    const/4 v1, 0x2

    if-eq v0, v1, :cond_11

    goto/16 :goto_12

    :cond_11
    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move-object/from16 v2, p2

    move v3, v15

    move/from16 v4, p4

    move/from16 v5, v20

    move-wide/from16 v6, v26

    move-object/from16 v8, p5

    invoke-direct/range {v0 .. v8}, Lek;->O000000o(Ljava/lang/Object;[BIIIJLby;)I

    const/4 v0, 0x0

    throw v0

    :cond_12
    move v0, v3

    move v15, v4

    move/from16 v24, v6

    move/from16 v25, v7

    move-wide/from16 v26, v8

    move-object/from16 v28, v10

    move/from16 v20, v13

    const/4 v1, 0x2

    const/16 v18, -0x1

    if-ne v0, v1, :cond_15

    move-object/from16 v7, p1

    move-wide/from16 v0, v26

    move-object/from16 v8, v28

    invoke-virtual {v8, v7, v0, v1}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ldi;

    invoke-interface {v2}, Ldi;->a()Z

    move-result v3

    if-nez v3, :cond_14

    invoke-interface {v2}, Ljava/util/List;->size()I

    move-result v3

    if-eqz v3, :cond_13

    add-int/2addr v3, v3

    goto :goto_11

    :cond_13
    const/16 v3, 0xa

    :goto_11
    invoke-interface {v2, v3}, Ldi;->a(I)Ldi;

    move-result-object v2

    invoke-virtual {v8, v7, v0, v1, v2}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    :cond_14
    move-object/from16 v9, p0

    move-object v5, v2

    move v3, v15

    move/from16 v10, v20

    invoke-direct {v9, v10}, Lek;->a(I)Les;

    move-result-object v0

    move/from16 v1, v17

    move-object/from16 v2, p2

    move/from16 v4, p4

    move-object/from16 v6, p5

    invoke-static/range {v0 .. v6}, Leb;->O000000o(Les;I[BIILdi;Lby;)I

    move-result v0

    move-object/from16 v12, p2

    move/from16 v13, p4

    move-object/from16 v11, p5

    move-object v14, v7

    move-object v15, v9

    move v2, v10

    move/from16 v10, v18

    move/from16 v1, v19

    move/from16 v6, v24

    goto :goto_14

    :cond_15
    :goto_12
    move-object/from16 v9, p0

    move-object/from16 v7, p1

    move v3, v15

    move/from16 v10, v20

    move-object/from16 v8, v28

    move v2, v3

    move/from16 v20, v10

    move/from16 v6, v24

    goto :goto_13

    :cond_16
    move v3, v4

    move/from16 v19, v5

    move/from16 v24, v6

    move/from16 v25, v7

    move-object v8, v9

    move/from16 v18, v10

    move-object v7, v14

    move-object v9, v15

    move v2, v3

    move/from16 v20, v16

    :goto_13
    invoke-static/range {p1 .. p1}, Lek;->O0000O0o(Ljava/lang/Object;)Lfi;

    move-result-object v4

    move/from16 v0, v17

    move-object/from16 v1, p2

    move/from16 v3, p4

    move-object/from16 v5, p5

    invoke-static/range {v0 .. v5}, Leb;->O000000o(I[BIILfi;Lby;)I

    move-result v0

    move-object/from16 v12, p2

    move/from16 v13, p4

    move-object/from16 v11, p5

    move-object v14, v7

    move-object v15, v9

    move/from16 v10, v18

    move/from16 v1, v19

    move/from16 v2, v20

    :goto_14
    move/from16 v7, v25

    move-object v9, v8

    goto/16 :goto_0

    :cond_17
    move/from16 v24, v6

    move v10, v7

    move-object v8, v9

    move-object v7, v14

    const v1, 0xfffff

    if-eq v10, v1, :cond_18

    int-to-long v1, v10

    move/from16 v6, v24

    invoke-virtual {v8, v7, v1, v2, v6}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    :cond_18
    move/from16 v1, p4

    if-ne v0, v1, :cond_19

    return-void

    :cond_19
    invoke-static {}, Ldl;->e()Ldl;

    move-result-object v0

    throw v0

    nop

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_9
        :pswitch_2
        :pswitch_7
        :pswitch_8
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private static O00000o(Ljava/lang/Object;J)I
    .locals 0

    invoke-static {p0, p1, p2}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Integer;

    invoke-virtual {p0}, Ljava/lang/Integer;->intValue()I

    move-result p0

    return p0
.end method

.method private static O00000o0(Ljava/lang/Object;J)F
    .locals 0

    invoke-static {p0, p1, p2}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Float;

    invoke-virtual {p0}, Ljava/lang/Float;->floatValue()F

    move-result p0

    return p0
.end method

.method private final O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z
    .locals 0

    invoke-direct {p0, p1, p3}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result p1

    invoke-direct {p0, p2, p3}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result p0

    if-ne p1, p0, :cond_0

    const/4 p0, 0x1

    return p0

    :cond_0
    const/4 p0, 0x0

    return p0
.end method

.method private final O00000oO(II)I
    .locals 5

    iget-object v0, p0, Lek;->c:[I

    array-length v0, v0

    div-int/lit8 v0, v0, 0x3

    const/4 v1, -0x1

    add-int/2addr v0, v1

    :goto_0
    if-gt p2, v0, :cond_2

    add-int v2, v0, p2

    ushr-int/lit8 v2, v2, 0x1

    mul-int/lit8 v3, v2, 0x3

    invoke-direct {p0, v3}, Lek;->d(I)I

    move-result v4

    if-eq p1, v4, :cond_1

    if-lt p1, v4, :cond_0

    add-int/lit8 v2, v2, 0x1

    move p2, v2

    goto :goto_0

    :cond_0
    add-int/lit8 v2, v2, -0x1

    move v0, v2

    goto :goto_0

    :cond_1
    return v3

    :cond_2
    return v1
.end method

.method private static O00000oO(Ljava/lang/Object;J)J
    .locals 0

    invoke-static {p0, p1, p2}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Long;

    invoke-virtual {p0}, Ljava/lang/Long;->longValue()J

    move-result-wide p0

    return-wide p0
.end method

.method private static O00000oo(Ljava/lang/Object;J)Z
    .locals 0

    invoke-static {p0, p1, p2}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Ljava/lang/Boolean;

    invoke-virtual {p0}, Ljava/lang/Boolean;->booleanValue()Z

    move-result p0

    return p0
.end method

.method static O0000O0o(Ljava/lang/Object;)Lfi;
    .locals 2

    check-cast p0, Lde;

    iget-object v0, p0, Lde;->h:Lfi;

    sget-object v1, Lfi;->a:Lfi;

    if-ne v0, v1, :cond_0

    invoke-static {}, Lfi;->a()Lfi;

    move-result-object v0

    iput-object v0, p0, Lde;->h:Lfi;

    :cond_0
    return-object v0
.end method

.method private final O0000Oo(Ljava/lang/Object;)I
    .locals 9

    sget-object v0, Lek;->b:Lsun/misc/Unsafe;

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    iget-object v3, p0, Lek;->c:[I

    array-length v3, v3

    if-ge v1, v3, :cond_3

    invoke-direct {p0, v1}, Lek;->e(I)I

    move-result v3

    invoke-static {v3}, Lek;->O000O0Oo(I)I

    move-result v4

    invoke-direct {p0, v1}, Lek;->d(I)I

    move-result v5

    invoke-static {v3}, Lek;->O00oOoOo(I)J

    move-result-wide v6

    sget-object v3, Lcw;->J:Lcw;

    iget v3, v3, Lcw;->Z:I

    if-lt v4, v3, :cond_0

    sget-object v3, Lcw;->W:Lcw;

    iget v3, v3, Lcw;->Z:I

    if-gt v4, v3, :cond_0

    iget-object v3, p0, Lek;->c:[I

    add-int/lit8 v8, v1, 0x2

    aget v3, v3, v8

    :cond_0
    packed-switch v4, :pswitch_data_0

    goto/16 :goto_16

    :pswitch_0
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_3

    :pswitch_1
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v3

    goto/16 :goto_4

    :pswitch_2
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v3

    goto/16 :goto_5

    :pswitch_3
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_6

    :pswitch_4
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_7

    :pswitch_5
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v3

    goto/16 :goto_8

    :pswitch_6
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v3

    goto/16 :goto_9

    :pswitch_7
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_a

    :pswitch_8
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_c

    :pswitch_9
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    instance-of v4, v3, Lck;

    if-eqz v4, :cond_1

    goto/16 :goto_d

    :pswitch_a
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_e

    :pswitch_b
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_f

    :pswitch_c
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_10

    :pswitch_d
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v3

    goto/16 :goto_11

    :pswitch_e
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v3

    goto/16 :goto_12

    :pswitch_f
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v3

    goto/16 :goto_13

    :pswitch_10
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_14

    :pswitch_11
    invoke-direct {p0, p1, v5, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_2

    goto/16 :goto_15

    :pswitch_12
    invoke-static {p1, v6, v7}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-direct {p0, v1}, Lek;->b(I)Ljava/lang/Object;

    move-result-object v4

    invoke-static {v3, v4}, Lff;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)I

    goto/16 :goto_16

    :pswitch_13
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-direct {p0, v1}, Lek;->a(I)Les;

    move-result-object v4

    invoke-static {v5, v3, v4}, Leu;->O00000Oo(ILjava/util/List;Les;)I

    move-result v3

    goto/16 :goto_2

    :pswitch_14
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O00000o(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto/16 :goto_1

    :pswitch_15
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000OOo(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto/16 :goto_1

    :pswitch_16
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000Oo(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto/16 :goto_1

    :pswitch_17
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000Oo0(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto/16 :goto_1

    :pswitch_18
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O00000oO(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto/16 :goto_1

    :pswitch_19
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000O0o(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto/16 :goto_1

    :pswitch_1a
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000OoO(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto :goto_1

    :pswitch_1b
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000Oo0(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto :goto_1

    :pswitch_1c
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000Oo(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto :goto_1

    :pswitch_1d
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O00000oo(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto :goto_1

    :pswitch_1e
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->b(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto :goto_1

    :pswitch_1f
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->a(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto :goto_1

    :pswitch_20
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000Oo0(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    goto :goto_1

    :pswitch_21
    invoke-virtual {v0, p1, v6, v7}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/util/List;

    invoke-static {v3}, Leu;->O0000Oo(Ljava/util/List;)I

    move-result v3

    if-lez v3, :cond_2

    :goto_1
    invoke-static {v5}, Lcn;->a(I)I

    move-result v4

    invoke-static {v3}, Lcn;->c(I)I

    move-result v5

    add-int/2addr v4, v5

    add-int/2addr v4, v3

    add-int/2addr v2, v4

    goto/16 :goto_16

    :pswitch_22
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O0000Oo(ILjava/util/List;)I

    move-result v3

    goto/16 :goto_2

    :pswitch_23
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O0000Oo0(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_24
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O00000o(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_25
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O0000OoO(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_26
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O00000Oo(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_27
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-direct {p0, v1}, Lek;->a(I)Les;

    move-result-object v4

    invoke-static {v5, v3, v4}, Leu;->O000000o(ILjava/util/List;Les;)I

    move-result v3

    goto :goto_2

    :pswitch_28
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O000000o(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_29
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O00000o0(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_2a
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O0000O0o(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_2b
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O0000Ooo(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_2c
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O0000OOo(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_2d
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O00000oO(ILjava/util/List;)I

    move-result v3

    goto :goto_2

    :pswitch_2e
    invoke-static {p1, v6, v7}, Lek;->O000000o(Ljava/lang/Object;J)Ljava/util/List;

    move-result-object v3

    invoke-static {v5, v3}, Leu;->O00000oo(ILjava/util/List;)I

    move-result v3

    :goto_2
    add-int/2addr v2, v3

    goto/16 :goto_16

    :pswitch_2f
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_3
    invoke-static {p1, v6, v7}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Leh;

    invoke-direct {p0, v1}, Lek;->a(I)Les;

    move-result-object v4

    invoke-static {v5, v3, v4}, Lcn;->O000000o(ILeh;Les;)I

    move-result v3

    goto :goto_2

    :pswitch_30
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    :goto_4
    invoke-static {v5, v3, v4}, Lcn;->O00000Oo(IJ)I

    move-result v3

    goto :goto_2

    :pswitch_31
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    :goto_5
    invoke-static {v5, v3}, Lcn;->O00000Oo(II)I

    move-result v3

    goto :goto_2

    :pswitch_32
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_6
    invoke-static {v5}, Lcn;->O000OOOo(I)I

    move-result v3

    goto :goto_2

    :pswitch_33
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_7
    invoke-static {v5}, Lcn;->O000OO(I)I

    move-result v3

    goto :goto_2

    :pswitch_34
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    :goto_8
    invoke-static {v5, v3}, Lcn;->O00000o0(II)I

    move-result v3

    goto :goto_2

    :pswitch_35
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    :goto_9
    invoke-static {v5, v3}, Lcn;->O000000o(II)I

    move-result v3

    goto :goto_2

    :pswitch_36
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_a
    invoke-static {p1, v6, v7}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    :goto_b
    check-cast v3, Lck;

    invoke-static {v5, v3}, Lcn;->O000000o(ILck;)I

    move-result v3

    goto :goto_2

    :pswitch_37
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_c
    invoke-static {p1, v6, v7}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-direct {p0, v1}, Lek;->a(I)Les;

    move-result-object v4

    invoke-static {v5, v3, v4}, Leu;->O00000Oo(ILjava/lang/Object;Les;)I

    move-result v3

    goto/16 :goto_2

    :pswitch_38
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    instance-of v4, v3, Lck;

    if-eqz v4, :cond_1

    :goto_d
    goto :goto_b

    :cond_1
    check-cast v3, Ljava/lang/String;

    invoke-static {v5, v3}, Lcn;->a(ILjava/lang/String;)I

    move-result v3

    goto/16 :goto_2

    :pswitch_39
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_e
    invoke-static {v5}, Lcn;->O000O0o(I)I

    move-result v3

    goto/16 :goto_2

    :pswitch_3a
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_f
    invoke-static {v5}, Lcn;->O000O0oo(I)I

    move-result v3

    goto/16 :goto_2

    :pswitch_3b
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_10
    invoke-static {v5}, Lcn;->O000OO00(I)I

    move-result v3

    goto/16 :goto_2

    :pswitch_3c
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    :goto_11
    invoke-static {v5, v3}, Lcn;->O00000oO(II)I

    move-result v3

    goto/16 :goto_2

    :pswitch_3d
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    :goto_12
    invoke-static {v5, v3, v4}, Lcn;->O000000o(IJ)I

    move-result v3

    goto/16 :goto_2

    :pswitch_3e
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    invoke-static {p1, v6, v7}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    :goto_13
    invoke-static {v5, v3, v4}, Lcn;->O00000o(IJ)I

    move-result v3

    goto/16 :goto_2

    :pswitch_3f
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_14
    invoke-static {v5}, Lcn;->O000OO0o(I)I

    move-result v3

    goto/16 :goto_2

    :pswitch_40
    invoke-direct {p0, p1, v1}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_2

    :goto_15
    invoke-static {v5}, Lcn;->O000O0oO(I)I

    move-result v3

    goto/16 :goto_2

    :cond_2
    :goto_16
    add-int/lit8 v1, v1, 0x3

    goto/16 :goto_0

    :cond_3
    iget-object p0, p0, Lek;->o:Lfh;

    invoke-static {p0, p1}, Lek;->O000000o(Lfh;Ljava/lang/Object;)I

    move-result p0

    add-int/2addr v2, p0

    return v2

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_40
        :pswitch_3f
        :pswitch_3e
        :pswitch_3d
        :pswitch_3c
        :pswitch_3b
        :pswitch_3a
        :pswitch_39
        :pswitch_38
        :pswitch_37
        :pswitch_36
        :pswitch_35
        :pswitch_34
        :pswitch_33
        :pswitch_32
        :pswitch_31
        :pswitch_30
        :pswitch_2f
        :pswitch_2e
        :pswitch_2d
        :pswitch_2c
        :pswitch_2b
        :pswitch_2a
        :pswitch_2e
        :pswitch_2d
        :pswitch_29
        :pswitch_28
        :pswitch_27
        :pswitch_26
        :pswitch_25
        :pswitch_24
        :pswitch_2d
        :pswitch_2e
        :pswitch_23
        :pswitch_22
        :pswitch_21
        :pswitch_20
        :pswitch_1f
        :pswitch_1e
        :pswitch_1d
        :pswitch_1c
        :pswitch_1b
        :pswitch_1a
        :pswitch_19
        :pswitch_18
        :pswitch_17
        :pswitch_16
        :pswitch_15
        :pswitch_14
        :pswitch_13
        :pswitch_12
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private final O0000Oo0(Ljava/lang/Object;)I
    .locals 13

    sget-object v0, Lek;->b:Lsun/misc/Unsafe;

    const v1, 0xfffff

    const/4 v2, 0x0

    move v6, v1

    move v3, v2

    move v4, v3

    move v5, v4

    :goto_0
    iget-object v7, p0, Lek;->c:[I

    array-length v7, v7

    if-ge v3, v7, :cond_4

    invoke-direct {p0, v3}, Lek;->e(I)I

    move-result v7

    invoke-direct {p0, v3}, Lek;->d(I)I

    move-result v8

    invoke-static {v7}, Lek;->O000O0Oo(I)I

    move-result v9

    const/16 v10, 0x11

    if-gt v9, v10, :cond_0

    iget-object v10, p0, Lek;->c:[I

    add-int/lit8 v11, v3, 0x2

    aget v10, v10, v11

    and-int v11, v10, v1

    ushr-int/lit8 v10, v10, 0x14

    const/4 v12, 0x1

    shl-int v10, v12, v10

    if-eq v11, v6, :cond_1

    int-to-long v5, v11

    invoke-virtual {v0, p1, v5, v6}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v5

    move v6, v11

    goto :goto_1

    :cond_0
    move v10, v2

    :cond_1
    :goto_1
    invoke-static {v7}, Lek;->O00oOoOo(I)J

    move-result-wide v11

    packed-switch v9, :pswitch_data_0

    goto/16 :goto_17

    :pswitch_0
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_4

    :pswitch_1
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    invoke-static {p1, v11, v12}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v9

    goto/16 :goto_5

    :pswitch_2
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    invoke-static {p1, v11, v12}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v7

    goto/16 :goto_6

    :pswitch_3
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_7

    :pswitch_4
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_8

    :pswitch_5
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    invoke-static {p1, v11, v12}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v7

    goto/16 :goto_9

    :pswitch_6
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    invoke-static {p1, v11, v12}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v7

    goto/16 :goto_a

    :pswitch_7
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_b

    :pswitch_8
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_d

    :pswitch_9
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    instance-of v9, v7, Lck;

    if-eqz v9, :cond_2

    goto/16 :goto_e

    :pswitch_a
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_f

    :pswitch_b
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_10

    :pswitch_c
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_11

    :pswitch_d
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    invoke-static {p1, v11, v12}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v7

    goto/16 :goto_12

    :pswitch_e
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    invoke-static {p1, v11, v12}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v9

    goto/16 :goto_13

    :pswitch_f
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    invoke-static {p1, v11, v12}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v9

    goto/16 :goto_14

    :pswitch_10
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_15

    :pswitch_11
    invoke-direct {p0, p1, v8, v3}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v7

    if-eqz v7, :cond_3

    goto/16 :goto_16

    :pswitch_12
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    invoke-direct {p0, v3}, Lek;->b(I)Ljava/lang/Object;

    move-result-object v8

    invoke-static {v7, v8}, Lff;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)I

    goto/16 :goto_17

    :pswitch_13
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-direct {p0, v3}, Lek;->a(I)Les;

    move-result-object v9

    invoke-static {v8, v7, v9}, Leu;->O00000Oo(ILjava/util/List;Les;)I

    move-result v7

    goto/16 :goto_3

    :pswitch_14
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O00000o(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto/16 :goto_2

    :pswitch_15
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000OOo(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto/16 :goto_2

    :pswitch_16
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000Oo(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto/16 :goto_2

    :pswitch_17
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000Oo0(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto/16 :goto_2

    :pswitch_18
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O00000oO(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto/16 :goto_2

    :pswitch_19
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000O0o(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto/16 :goto_2

    :pswitch_1a
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000OoO(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto :goto_2

    :pswitch_1b
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000Oo0(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto :goto_2

    :pswitch_1c
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000Oo(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto :goto_2

    :pswitch_1d
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O00000oo(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto :goto_2

    :pswitch_1e
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->b(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto :goto_2

    :pswitch_1f
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->a(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto :goto_2

    :pswitch_20
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000Oo0(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    goto :goto_2

    :pswitch_21
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v7}, Leu;->O0000Oo(Ljava/util/List;)I

    move-result v7

    if-lez v7, :cond_3

    :goto_2
    invoke-static {v8}, Lcn;->a(I)I

    move-result v8

    invoke-static {v7}, Lcn;->c(I)I

    move-result v9

    add-int/2addr v8, v9

    add-int/2addr v8, v7

    add-int/2addr v4, v8

    goto/16 :goto_17

    :pswitch_22
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O0000Oo(ILjava/util/List;)I

    move-result v7

    goto/16 :goto_3

    :pswitch_23
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O0000Oo0(ILjava/util/List;)I

    move-result v7

    goto/16 :goto_3

    :pswitch_24
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O00000o(ILjava/util/List;)I

    move-result v7

    goto/16 :goto_3

    :pswitch_25
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O0000OoO(ILjava/util/List;)I

    move-result v7

    goto :goto_3

    :pswitch_26
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O00000Oo(ILjava/util/List;)I

    move-result v7

    goto :goto_3

    :pswitch_27
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-direct {p0, v3}, Lek;->a(I)Les;

    move-result-object v9

    invoke-static {v8, v7, v9}, Leu;->O000000o(ILjava/util/List;Les;)I

    move-result v7

    goto :goto_3

    :pswitch_28
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O000000o(ILjava/util/List;)I

    move-result v7

    goto :goto_3

    :pswitch_29
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O00000o0(ILjava/util/List;)I

    move-result v7

    goto :goto_3

    :pswitch_2a
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O0000O0o(ILjava/util/List;)I

    move-result v7

    goto :goto_3

    :pswitch_2b
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O0000Ooo(ILjava/util/List;)I

    move-result v7

    goto :goto_3

    :pswitch_2c
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O0000OOo(ILjava/util/List;)I

    move-result v7

    goto :goto_3

    :pswitch_2d
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O00000oO(ILjava/util/List;)I

    move-result v7

    goto :goto_3

    :pswitch_2e
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Ljava/util/List;

    invoke-static {v8, v7}, Leu;->O00000oo(ILjava/util/List;)I

    move-result v7

    :goto_3
    add-int/2addr v4, v7

    goto/16 :goto_17

    :pswitch_2f
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_4
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Leh;

    invoke-direct {p0, v3}, Lek;->a(I)Les;

    move-result-object v9

    invoke-static {v8, v7, v9}, Lcn;->O000000o(ILeh;Les;)I

    move-result v7

    goto :goto_3

    :pswitch_30
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getLong(Ljava/lang/Object;J)J

    move-result-wide v9

    :goto_5
    invoke-static {v8, v9, v10}, Lcn;->O00000Oo(IJ)I

    move-result v7

    goto :goto_3

    :pswitch_31
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v7

    :goto_6
    invoke-static {v8, v7}, Lcn;->O00000Oo(II)I

    move-result v7

    goto :goto_3

    :pswitch_32
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_7
    invoke-static {v8}, Lcn;->O000OOOo(I)I

    move-result v7

    goto :goto_3

    :pswitch_33
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_8
    invoke-static {v8}, Lcn;->O000OO(I)I

    move-result v7

    goto :goto_3

    :pswitch_34
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v7

    :goto_9
    invoke-static {v8, v7}, Lcn;->O00000o0(II)I

    move-result v7

    goto :goto_3

    :pswitch_35
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v7

    :goto_a
    invoke-static {v8, v7}, Lcn;->O000000o(II)I

    move-result v7

    goto :goto_3

    :pswitch_36
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_b
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    :goto_c
    check-cast v7, Lck;

    invoke-static {v8, v7}, Lcn;->O000000o(ILck;)I

    move-result v7

    goto :goto_3

    :pswitch_37
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_d
    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    invoke-direct {p0, v3}, Lek;->a(I)Les;

    move-result-object v9

    invoke-static {v8, v7, v9}, Leu;->O00000Oo(ILjava/lang/Object;Les;)I

    move-result v7

    goto :goto_3

    :pswitch_38
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v7

    instance-of v9, v7, Lck;

    if-eqz v9, :cond_2

    :goto_e
    goto :goto_c

    :cond_2
    check-cast v7, Ljava/lang/String;

    invoke-static {v8, v7}, Lcn;->a(ILjava/lang/String;)I

    move-result v7

    goto/16 :goto_3

    :pswitch_39
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_f
    invoke-static {v8}, Lcn;->O000O0o(I)I

    move-result v7

    goto/16 :goto_3

    :pswitch_3a
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_10
    invoke-static {v8}, Lcn;->O000O0oo(I)I

    move-result v7

    goto/16 :goto_3

    :pswitch_3b
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_11
    invoke-static {v8}, Lcn;->O000OO00(I)I

    move-result v7

    goto/16 :goto_3

    :pswitch_3c
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v7

    :goto_12
    invoke-static {v8, v7}, Lcn;->O00000oO(II)I

    move-result v7

    goto/16 :goto_3

    :pswitch_3d
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getLong(Ljava/lang/Object;J)J

    move-result-wide v9

    :goto_13
    invoke-static {v8, v9, v10}, Lcn;->O000000o(IJ)I

    move-result v7

    goto/16 :goto_3

    :pswitch_3e
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    invoke-virtual {v0, p1, v11, v12}, Lsun/misc/Unsafe;->getLong(Ljava/lang/Object;J)J

    move-result-wide v9

    :goto_14
    invoke-static {v8, v9, v10}, Lcn;->O00000o(IJ)I

    move-result v7

    goto/16 :goto_3

    :pswitch_3f
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_15
    invoke-static {v8}, Lcn;->O000OO0o(I)I

    move-result v7

    goto/16 :goto_3

    :pswitch_40
    and-int v7, v5, v10

    if-eqz v7, :cond_3

    :goto_16
    invoke-static {v8}, Lcn;->O000O0oO(I)I

    move-result v7

    goto/16 :goto_3

    :cond_3
    :goto_17
    add-int/lit8 v3, v3, 0x3

    goto/16 :goto_0

    :cond_4
    iget-object v0, p0, Lek;->o:Lfh;

    invoke-static {v0, p1}, Lek;->O000000o(Lfh;Ljava/lang/Object;)I

    move-result v0

    add-int/2addr v4, v0

    iget-boolean p0, p0, Lek;->h:Z

    if-eqz p0, :cond_7

    invoke-static {p1}, Lej;->O00000Oo(Ljava/lang/Object;)Lcu;

    move-result-object p0

    move p1, v2

    :goto_18
    iget-object v0, p0, Lcu;->a:Lfc;

    invoke-virtual {v0}, Lfc;->a()I

    move-result v0

    if-ge v2, v0, :cond_5

    iget-object v0, p0, Lcu;->a:Lfc;

    invoke-virtual {v0, v2}, Lfc;->b(I)Ljava/util/Map$Entry;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/Map$Entry;->getKey()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ldd;

    invoke-interface {v0}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v0

    invoke-static {v1, v0}, Lcu;->O00000Oo(Ldd;Ljava/lang/Object;)I

    move-result v0

    add-int/2addr p1, v0

    add-int/lit8 v2, v2, 0x1

    goto :goto_18

    :cond_5
    iget-object p0, p0, Lcu;->a:Lfc;

    invoke-virtual {p0}, Lfc;->b()Ljava/lang/Iterable;

    move-result-object p0

    invoke-interface {p0}, Ljava/lang/Iterable;->iterator()Ljava/util/Iterator;

    move-result-object p0

    :goto_19
    invoke-interface {p0}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_6

    invoke-interface {p0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/Map$Entry;

    invoke-interface {v0}, Ljava/util/Map$Entry;->getKey()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ldd;

    invoke-interface {v0}, Ljava/util/Map$Entry;->getValue()Ljava/lang/Object;

    move-result-object v0

    invoke-static {v1, v0}, Lcu;->O00000Oo(Ldd;Ljava/lang/Object;)I

    move-result v0

    add-int/2addr p1, v0

    goto :goto_19

    :cond_6
    add-int/2addr v4, p1

    :cond_7
    return v4

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_40
        :pswitch_3f
        :pswitch_3e
        :pswitch_3d
        :pswitch_3c
        :pswitch_3b
        :pswitch_3a
        :pswitch_39
        :pswitch_38
        :pswitch_37
        :pswitch_36
        :pswitch_35
        :pswitch_34
        :pswitch_33
        :pswitch_32
        :pswitch_31
        :pswitch_30
        :pswitch_2f
        :pswitch_2e
        :pswitch_2d
        :pswitch_2c
        :pswitch_2b
        :pswitch_2a
        :pswitch_2e
        :pswitch_2d
        :pswitch_29
        :pswitch_28
        :pswitch_27
        :pswitch_26
        :pswitch_25
        :pswitch_24
        :pswitch_2d
        :pswitch_2e
        :pswitch_23
        :pswitch_22
        :pswitch_21
        :pswitch_20
        :pswitch_1f
        :pswitch_1e
        :pswitch_1d
        :pswitch_1c
        :pswitch_1b
        :pswitch_1a
        :pswitch_19
        :pswitch_18
        :pswitch_17
        :pswitch_16
        :pswitch_15
        :pswitch_14
        :pswitch_13
        :pswitch_12
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method private final O000O0OO(I)I
    .locals 0

    iget-object p0, p0, Lek;->c:[I

    add-int/lit8 p1, p1, 0x2

    aget p0, p0, p1

    return p0
.end method

.method private static O000O0Oo(I)I
    .locals 0

    ushr-int/lit8 p0, p0, 0x14

    and-int/lit16 p0, p0, 0xff

    return p0
.end method

.method private final O000O0o0(I)I
    .locals 1

    iget v0, p0, Lek;->e:I

    if-lt p1, v0, :cond_0

    iget v0, p0, Lek;->f:I

    if-gt p1, v0, :cond_0

    const/4 v0, 0x0

    invoke-direct {p0, p1, v0}, Lek;->O00000oO(II)I

    move-result p0

    return p0

    :cond_0
    const/4 p0, -0x1

    return p0
.end method

.method private static O00oOoOo(I)J
    .locals 2

    const v0, 0xfffff

    and-int/2addr p0, v0

    int-to-long v0, p0

    return-wide v0
.end method

.method private final a(II)I
    .locals 1

    iget v0, p0, Lek;->e:I

    if-lt p1, v0, :cond_0

    iget v0, p0, Lek;->f:I

    if-gt p1, v0, :cond_0

    invoke-direct {p0, p1, p2}, Lek;->O00000oO(II)I

    move-result p0

    return p0

    :cond_0
    const/4 p0, -0x1

    return p0
.end method

.method private final a(I)Les;
    .locals 3

    div-int/lit8 p1, p1, 0x3

    add-int/2addr p1, p1

    iget-object v0, p0, Lek;->d:[Ljava/lang/Object;

    aget-object v1, v0, p1

    check-cast v1, Les;

    if-nez v1, :cond_0

    sget-object v1, Lep;->a:Lep;

    add-int/lit8 v2, p1, 0x1

    aget-object v0, v0, v2

    check-cast v0, Ljava/lang/Class;

    invoke-virtual {v1, v0}, Lep;->O00000Oo(Ljava/lang/Class;)Les;

    move-result-object v0

    iget-object p0, p0, Lek;->d:[Ljava/lang/Object;

    aput-object v0, p0, p1

    return-object v0

    :cond_0
    return-object v1
.end method

.method private static a(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Field;
    .locals 6

    :try_start_0
    invoke-virtual {p0, p1}, Ljava/lang/Class;->getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object p0
    :try_end_0
    .catch Ljava/lang/NoSuchFieldException; {:try_start_0 .. :try_end_0} :catch_0

    return-object p0

    :catch_0
    invoke-virtual {p0}, Ljava/lang/Class;->getDeclaredFields()[Ljava/lang/reflect/Field;

    move-result-object v0

    array-length v1, v0

    const/4 v2, 0x0

    :goto_0
    if-ge v2, v1, :cond_1

    aget-object v3, v0, v2

    invoke-virtual {v3}, Ljava/lang/reflect/Field;->getName()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {p1, v4}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-nez v4, :cond_0

    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    :cond_0
    return-object v3

    :cond_1
    new-instance v1, Ljava/lang/RuntimeException;

    invoke-virtual {p0}, Ljava/lang/Class;->getName()Ljava/lang/String;

    move-result-object p0

    invoke-static {v0}, Ljava/util/Arrays;->toString([Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-static {p1}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v2

    invoke-static {p0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v3

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v4}, Ljava/lang/String;->length()I

    move-result v4

    new-instance v5, Ljava/lang/StringBuilder;

    add-int/lit8 v2, v2, 0x28

    add-int/2addr v2, v3

    add-int/2addr v2, v4

    invoke-direct {v5, v2}, Ljava/lang/StringBuilder;-><init>(I)V

    const-string v2, "Field "

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " for "

    invoke-virtual {v5, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p0, " not found. Known fields are "

    invoke-virtual {v5, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v1, p0}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    throw v1
.end method

.method private final b(I)Ljava/lang/Object;
    .locals 0

    div-int/lit8 p1, p1, 0x3

    iget-object p0, p0, Lek;->d:[Ljava/lang/Object;

    add-int/2addr p1, p1

    aget-object p0, p0, p1

    return-object p0
.end method

.method private final c(I)Ldh;
    .locals 0

    div-int/lit8 p1, p1, 0x3

    iget-object p0, p0, Lek;->d:[Ljava/lang/Object;

    add-int/2addr p1, p1

    add-int/lit8 p1, p1, 0x1

    aget-object p0, p0, p1

    check-cast p0, Ldh;

    return-object p0
.end method

.method private final d(I)I
    .locals 0

    iget-object p0, p0, Lek;->c:[I

    aget p0, p0, p1

    return p0
.end method

.method private final e(I)I
    .locals 0

    iget-object p0, p0, Lek;->c:[I

    add-int/lit8 p1, p1, 0x1

    aget p0, p0, p1

    return p0
.end method


# virtual methods
.method final O000000o(Ljava/lang/Object;[BIIILby;)I
    .locals 29

    move-object/from16 v15, p0

    move-object/from16 v14, p1

    move-object/from16 v12, p2

    move/from16 v13, p4

    move/from16 v11, p5

    move-object/from16 v9, p6

    sget-object v10, Lek;->b:Lsun/misc/Unsafe;

    const/16 v16, 0x0

    move/from16 v0, p3

    move/from16 v1, v16

    move v3, v1

    move v5, v3

    const/4 v2, -0x1

    const v6, 0xfffff

    :goto_0
    const/16 v17, 0x0

    if-ge v0, v13, :cond_1d

    add-int/lit8 v1, v0, 0x1

    aget-byte v0, v12, v0

    if-gez v0, :cond_0

    invoke-static {v0, v12, v1, v9}, Leb;->O000000o(I[BILby;)I

    move-result v0

    iget v1, v9, Lby;->a:I

    move v4, v1

    move v1, v0

    goto :goto_1

    :cond_0
    move v4, v0

    :goto_1
    ushr-int/lit8 v0, v4, 0x3

    and-int/lit8 v7, v4, 0x7

    const/4 v8, 0x3

    if-le v0, v2, :cond_1

    div-int/2addr v3, v8

    invoke-direct {v15, v0, v3}, Lek;->a(II)I

    move-result v2

    goto :goto_2

    :cond_1
    invoke-direct {v15, v0}, Lek;->O000O0o0(I)I

    move-result v2

    :goto_2
    const/4 v3, -0x1

    if-eq v2, v3, :cond_17

    iget-object v3, v15, Lek;->c:[I

    add-int/lit8 v20, v2, 0x1

    aget v3, v3, v20

    invoke-static {v3}, Lek;->O000O0Oo(I)I

    move-result v8

    move/from16 v21, v0

    move/from16 v20, v1

    invoke-static {v3}, Lek;->O00oOoOo(I)J

    move-result-wide v0

    move-wide/from16 v22, v0

    const/16 v0, 0x11

    if-gt v8, v0, :cond_e

    iget-object v0, v15, Lek;->c:[I

    add-int/lit8 v24, v2, 0x2

    aget v0, v0, v24

    ushr-int/lit8 v24, v0, 0x14

    const/4 v1, 0x1

    shl-int v24, v1, v24

    const v11, 0xfffff

    and-int/2addr v0, v11

    move/from16 v18, v2

    if-ne v0, v6, :cond_2

    move/from16 v27, v6

    move v6, v5

    goto :goto_4

    :cond_2
    if-ne v6, v11, :cond_3

    goto :goto_3

    :cond_3
    int-to-long v1, v6

    invoke-virtual {v10, v14, v1, v2, v5}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    :goto_3
    int-to-long v1, v0

    invoke-virtual {v10, v14, v1, v2}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v1

    move/from16 v27, v0

    move v6, v1

    :goto_4
    const/4 v0, 0x5

    packed-switch v8, :pswitch_data_0

    move v11, v4

    move/from16 v8, v18

    move/from16 v2, v20

    move-wide/from16 v4, v22

    const/4 v0, 0x3

    const/16 v18, -0x1

    if-ne v7, v0, :cond_d

    invoke-direct {v15, v8}, Lek;->a(I)Les;

    move-result-object v0

    shl-int/lit8 v1, v21, 0x3

    or-int/lit8 v7, v1, 0x4

    move-object/from16 v1, p2

    move/from16 v3, p4

    move-wide v12, v4

    move v4, v7

    move-object/from16 v5, p6

    invoke-static/range {v0 .. v5}, Leb;->O000000o(Les;[BIIILby;)I

    move-result v0

    and-int v1, v6, v24

    if-nez v1, :cond_c

    iget-object v1, v9, Lby;->c:Ljava/lang/Object;

    goto/16 :goto_d

    :pswitch_0
    if-nez v7, :cond_4

    move/from16 v1, v20

    invoke-static {v12, v1, v9}, Leb;->O00000Oo([BILby;)I

    move-result v7

    iget-wide v0, v9, Lby;->b:J

    invoke-static {v0, v1}, Lcl;->a(J)J

    move-result-wide v25

    move/from16 v8, v21

    move-wide/from16 v2, v22

    move-object v0, v10

    move-object/from16 v1, p1

    move/from16 v5, v18

    const/16 v18, -0x1

    move v11, v4

    move v8, v5

    move-wide/from16 v4, v25

    goto/16 :goto_a

    :cond_4
    move v11, v4

    move/from16 v8, v18

    const/16 v18, -0x1

    move/from16 v2, v20

    goto/16 :goto_e

    :pswitch_1
    move v11, v4

    move/from16 v8, v18

    move/from16 v1, v20

    move-wide/from16 v2, v22

    const/16 v18, -0x1

    if-nez v7, :cond_b

    invoke-static {v12, v1, v9}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v1, v9, Lby;->a:I

    invoke-static {v1}, Lcl;->a(I)I

    move-result v1

    :cond_5
    :goto_5
    invoke-virtual {v10, v14, v2, v3, v1}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    goto/16 :goto_b

    :pswitch_2
    move v11, v4

    move/from16 v8, v18

    move/from16 v1, v20

    move-wide/from16 v2, v22

    const/16 v18, -0x1

    if-nez v7, :cond_b

    invoke-static {v12, v1, v9}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v1, v9, Lby;->a:I

    invoke-direct {v15, v8}, Lek;->c(I)Ldh;

    move-result-object v4

    if-eqz v4, :cond_5

    invoke-interface {v4, v1}, Ldh;->a(I)Z

    move-result v4

    if-eqz v4, :cond_6

    goto :goto_5

    :cond_6
    invoke-static/range {p1 .. p1}, Lek;->O0000O0o(Ljava/lang/Object;)Lfi;

    move-result-object v2

    int-to-long v3, v1

    invoke-static {v3, v4}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v1

    invoke-virtual {v2, v11, v1}, Lfi;->O000000o(ILjava/lang/Object;)V

    move v5, v6

    goto/16 :goto_c

    :pswitch_3
    move v11, v4

    move/from16 v8, v18

    move/from16 v1, v20

    move-wide/from16 v2, v22

    const/4 v0, 0x2

    const/16 v18, -0x1

    if-ne v7, v0, :cond_b

    invoke-static {v12, v1, v9}, Leb;->O00000oO([BILby;)I

    move-result v0

    iget-object v1, v9, Lby;->c:Ljava/lang/Object;

    :goto_6
    invoke-virtual {v10, v14, v2, v3, v1}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    goto/16 :goto_b

    :pswitch_4
    move v11, v4

    move/from16 v8, v18

    move/from16 v1, v20

    move-wide/from16 v2, v22

    const/4 v0, 0x2

    const/16 v18, -0x1

    if-ne v7, v0, :cond_b

    invoke-direct {v15, v8}, Lek;->a(I)Les;

    move-result-object v0

    invoke-static {v0, v12, v1, v13, v9}, Leb;->O000000o(Les;[BIILby;)I

    move-result v0

    and-int v1, v6, v24

    if-nez v1, :cond_7

    iget-object v1, v9, Lby;->c:Ljava/lang/Object;

    goto :goto_6

    :cond_7
    invoke-virtual {v10, v14, v2, v3}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v1

    iget-object v4, v9, Lby;->c:Ljava/lang/Object;

    invoke-static {v1, v4}, Ldj;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    goto :goto_6

    :pswitch_5
    move v11, v4

    move/from16 v8, v18

    move/from16 v1, v20

    move-wide/from16 v4, v22

    const/4 v0, 0x2

    const/16 v18, -0x1

    if-eq v7, v0, :cond_8

    goto/16 :goto_9

    :cond_8
    const/high16 v0, 0x20000000

    and-int/2addr v0, v3

    if-eqz v0, :cond_9

    invoke-static {v12, v1, v9}, Leb;->O00000o([BILby;)I

    move-result v0

    goto :goto_7

    :cond_9
    invoke-static {v12, v1, v9}, Leb;->O00000o0([BILby;)I

    move-result v0

    :goto_7
    iget-object v1, v9, Lby;->c:Ljava/lang/Object;

    invoke-virtual {v10, v14, v4, v5, v1}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    goto/16 :goto_b

    :pswitch_6
    move v11, v4

    move/from16 v8, v18

    move/from16 v1, v20

    move-wide/from16 v4, v22

    const/16 v18, -0x1

    if-nez v7, :cond_b

    invoke-static {v12, v1, v9}, Leb;->O00000Oo([BILby;)I

    move-result v0

    iget-wide v1, v9, Lby;->b:J

    const-wide/16 v19, 0x0

    cmp-long v1, v1, v19

    if-eqz v1, :cond_a

    const/4 v1, 0x1

    goto :goto_8

    :cond_a
    move/from16 v1, v16

    :goto_8
    invoke-static {v14, v4, v5, v1}, Lfr;->O000000o(Ljava/lang/Object;JZ)V

    goto/16 :goto_b

    :pswitch_7
    move v11, v4

    move/from16 v8, v18

    move/from16 v1, v20

    move-wide/from16 v4, v22

    const/16 v18, -0x1

    if-ne v7, v0, :cond_b

    invoke-static {v12, v1}, Leb;->O000000o([BI)I

    move-result v0

    invoke-virtual {v10, v14, v4, v5, v0}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    add-int/lit8 v0, v1, 0x4

    goto/16 :goto_b

    :pswitch_8
    move v11, v4

    move/from16 v8, v18

    move/from16 v1, v20

    move-wide/from16 v4, v22

    const/4 v0, 0x1

    const/16 v18, -0x1

    if-ne v7, v0, :cond_b

    invoke-static {v12, v1}, Leb;->O00000Oo([BI)J

    move-result-wide v19

    move-object v0, v10

    move v7, v1

    move-object/from16 v1, p1

    move-wide v2, v4

    move-wide/from16 v4, v19

    invoke-virtual/range {v0 .. v5}, Lsun/misc/Unsafe;->putLong(Ljava/lang/Object;JJ)V

    add-int/lit8 v0, v7, 0x8

    goto/16 :goto_b

    :cond_b
    :goto_9
    move v2, v1

    goto/16 :goto_e

    :pswitch_9
    move v11, v4

    move/from16 v8, v18

    move/from16 v2, v20

    move-wide/from16 v4, v22

    const/16 v18, -0x1

    if-nez v7, :cond_d

    invoke-static {v12, v2, v9}, Leb;->O000000o([BILby;)I

    move-result v0

    iget v1, v9, Lby;->a:I

    invoke-virtual {v10, v14, v4, v5, v1}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    goto :goto_b

    :pswitch_a
    move v11, v4

    move/from16 v8, v18

    move/from16 v2, v20

    move-wide/from16 v4, v22

    const/16 v18, -0x1

    if-nez v7, :cond_d

    invoke-static {v12, v2, v9}, Leb;->O00000Oo([BILby;)I

    move-result v7

    iget-wide v2, v9, Lby;->b:J

    move-object v0, v10

    move-object/from16 v1, p1

    move-wide/from16 v19, v2

    move-wide v2, v4

    move-wide/from16 v4, v19

    :goto_a
    invoke-virtual/range {v0 .. v5}, Lsun/misc/Unsafe;->putLong(Ljava/lang/Object;JJ)V

    or-int v5, v6, v24

    move v0, v7

    goto :goto_c

    :pswitch_b
    move v11, v4

    move/from16 v8, v18

    move/from16 v2, v20

    move-wide/from16 v4, v22

    const/16 v18, -0x1

    if-ne v7, v0, :cond_d

    invoke-static {v12, v2}, Leb;->O00000o([BI)F

    move-result v0

    invoke-static {v14, v4, v5, v0}, Lfr;->O000000o(Ljava/lang/Object;JF)V

    add-int/lit8 v0, v2, 0x4

    goto :goto_b

    :pswitch_c
    move v11, v4

    move/from16 v8, v18

    move/from16 v2, v20

    move-wide/from16 v4, v22

    const/4 v0, 0x1

    const/16 v18, -0x1

    if-ne v7, v0, :cond_d

    invoke-static {v12, v2}, Leb;->O00000o0([BI)D

    move-result-wide v0

    invoke-static {v14, v4, v5, v0, v1}, Lfr;->O000000o(Ljava/lang/Object;JD)V

    add-int/lit8 v0, v2, 0x8

    :goto_b
    or-int v5, v6, v24

    :goto_c
    move v3, v8

    move v1, v11

    move/from16 v2, v21

    move/from16 v6, v27

    move/from16 v11, p5

    goto/16 :goto_0

    :cond_c
    invoke-virtual {v10, v14, v12, v13}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v1

    iget-object v2, v9, Lby;->c:Ljava/lang/Object;

    invoke-static {v1, v2}, Ldj;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    :goto_d
    invoke-virtual {v10, v14, v12, v13, v1}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    or-int v5, v6, v24

    move-object/from16 v12, p2

    move/from16 v13, p4

    goto :goto_c

    :cond_d
    :goto_e
    move/from16 v9, p5

    move/from16 v23, v6

    move-object v7, v15

    move/from16 v22, v21

    move-object v15, v14

    move/from16 v28, v11

    move v11, v8

    move-object v8, v10

    move/from16 v10, v28

    goto/16 :goto_12

    :cond_e
    move v11, v4

    move v1, v8

    move/from16 v4, v21

    move-wide/from16 v12, v22

    const/16 v18, -0x1

    move v8, v2

    move/from16 v2, v20

    const/16 v0, 0x1b

    if-eq v1, v0, :cond_13

    const/16 v0, 0x31

    if-gt v1, v0, :cond_10

    move-object/from16 v19, v10

    int-to-long v9, v3

    move-object/from16 v0, p0

    move/from16 v20, v1

    move-object/from16 v1, p1

    move v3, v2

    move-object/from16 v2, p2

    move/from16 v21, v3

    move/from16 v22, v4

    move/from16 v4, p4

    move/from16 v23, v5

    move v5, v11

    move/from16 v27, v6

    move/from16 v6, v22

    move/from16 p3, v8

    move-object/from16 v15, v19

    move/from16 v19, v11

    move/from16 v11, v20

    move-object/from16 v24, v15

    move-object v15, v14

    move-object/from16 v14, p6

    invoke-direct/range {v0 .. v14}, Lek;->O000000o(Ljava/lang/Object;[BIIIIIIJIJLby;)I

    move-result v0

    move/from16 v14, v21

    if-eq v0, v14, :cond_f

    :goto_f
    move-object/from16 v12, p2

    move/from16 v3, p3

    move/from16 v13, p4

    move/from16 v11, p5

    move-object/from16 v9, p6

    move-object v14, v15

    move/from16 v1, v19

    move/from16 v2, v22

    move/from16 v5, v23

    move-object/from16 v10, v24

    move/from16 v6, v27

    move-object/from16 v15, p0

    goto/16 :goto_0

    :cond_f
    move-object/from16 v7, p0

    move/from16 v11, p3

    move/from16 v9, p5

    move v2, v0

    move/from16 v10, v19

    move-object/from16 v8, v24

    goto/16 :goto_12

    :cond_10
    move/from16 v20, v1

    move/from16 v22, v4

    move/from16 v23, v5

    move/from16 v27, v6

    move/from16 p3, v8

    move-object/from16 v24, v10

    move/from16 v19, v11

    move-object v15, v14

    move v14, v2

    const/16 v0, 0x32

    move/from16 v9, v20

    if-eq v9, v0, :cond_11

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move-object/from16 v2, p2

    move v8, v3

    move v3, v14

    move/from16 v4, p4

    move/from16 v5, v19

    move/from16 v6, v22

    move-wide v10, v12

    move/from16 v12, p3

    move-object/from16 v13, p6

    invoke-direct/range {v0 .. v13}, Lek;->O000000o(Ljava/lang/Object;[BIIIIIIIJILby;)I

    move-result v0

    if-eq v0, v14, :cond_f

    goto :goto_f

    :cond_11
    const/4 v0, 0x2

    if-eq v7, v0, :cond_12

    goto/16 :goto_11

    :cond_12
    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move-object/from16 v2, p2

    move v3, v14

    move/from16 v4, p4

    move/from16 v5, p3

    move-wide v6, v12

    move-object/from16 v8, p6

    invoke-direct/range {v0 .. v8}, Lek;->O000000o(Ljava/lang/Object;[BIIIJLby;)I

    throw v17

    :cond_13
    move/from16 v22, v4

    move/from16 v23, v5

    move/from16 v27, v6

    move/from16 p3, v8

    move-object/from16 v24, v10

    move/from16 v19, v11

    move-object v15, v14

    const/4 v0, 0x2

    move v14, v2

    if-ne v7, v0, :cond_16

    move-object/from16 v0, v24

    invoke-virtual {v0, v15, v12, v13}, Lsun/misc/Unsafe;->getObject(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ldi;

    invoke-interface {v1}, Ldi;->a()Z

    move-result v2

    if-nez v2, :cond_15

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v2

    if-eqz v2, :cond_14

    add-int/2addr v2, v2

    goto :goto_10

    :cond_14
    const/16 v2, 0xa

    :goto_10
    invoke-interface {v1, v2}, Ldi;->a(I)Ldi;

    move-result-object v1

    invoke-virtual {v0, v15, v12, v13, v1}, Lsun/misc/Unsafe;->putObject(Ljava/lang/Object;JLjava/lang/Object;)V

    :cond_15
    move-object/from16 v7, p0

    move/from16 v9, p3

    move-object v8, v0

    move-object v5, v1

    invoke-direct {v7, v9}, Lek;->a(I)Les;

    move-result-object v0

    move/from16 v1, v19

    move-object/from16 v2, p2

    move v3, v14

    move/from16 v4, p4

    move-object/from16 v6, p6

    invoke-static/range {v0 .. v6}, Leb;->O000000o(Les;I[BIILdi;Lby;)I

    move-result v0

    move-object/from16 v12, p2

    move/from16 v13, p4

    move/from16 v11, p5

    move-object v10, v8

    move v3, v9

    move-object v14, v15

    move/from16 v2, v22

    move/from16 v5, v23

    move/from16 v6, v27

    move-object/from16 v9, p6

    move-object v15, v7

    goto/16 :goto_0

    :cond_16
    :goto_11
    move-object/from16 v7, p0

    move/from16 v9, p3

    move-object/from16 v8, v24

    move v11, v9

    move v2, v14

    move/from16 v10, v19

    move/from16 v9, p5

    goto :goto_12

    :cond_17
    move/from16 v22, v0

    move/from16 v18, v3

    move/from16 v23, v5

    move/from16 v27, v6

    move-object v8, v10

    move-object v7, v15

    move-object v15, v14

    move v14, v1

    move/from16 v9, p5

    move v10, v4

    move v2, v14

    move/from16 v11, v16

    :goto_12
    if-eq v10, v9, :cond_18

    goto :goto_13

    :cond_18
    if-eqz v9, :cond_19

    goto/16 :goto_16

    :cond_19
    :goto_13
    iget-boolean v0, v7, Lek;->h:Z

    move-object/from16 v12, p6

    if-nez v0, :cond_1b

    :cond_1a
    move/from16 v13, v22

    goto :goto_14

    :cond_1b
    iget-object v0, v12, Lby;->d:Lcs;

    invoke-static {}, Lcs;->a()Lcs;

    move-result-object v1

    if-eq v0, v1, :cond_1a

    iget-object v0, v7, Lek;->g:Leh;

    iget-object v1, v12, Lby;->d:Lcs;

    move/from16 v13, v22

    invoke-virtual {v1, v0, v13}, Lcs;->O000000o(Leh;I)Lcq;

    move-result-object v5

    if-nez v5, :cond_1c

    goto :goto_14

    :cond_1c
    move-object v4, v15

    check-cast v4, Ldc;

    invoke-virtual {v4}, Ldc;->d()Lcu;

    move v0, v10

    move-object/from16 v1, p2

    move/from16 v3, p4

    move-object/from16 v6, p6

    invoke-static/range {v0 .. v6}, Leb;->O000000o(I[BIILdc;Lcq;Lby;)I

    move-result v0

    goto :goto_15

    :goto_14
    invoke-static/range {p1 .. p1}, Lek;->O0000O0o(Ljava/lang/Object;)Lfi;

    move-result-object v4

    move v0, v10

    move-object/from16 v1, p2

    move/from16 v3, p4

    move-object/from16 v5, p6

    invoke-static/range {v0 .. v5}, Leb;->O000000o(I[BIILfi;Lby;)I

    move-result v0

    :goto_15
    move v1, v10

    move v3, v11

    move v2, v13

    move-object v14, v15

    move/from16 v5, v23

    move/from16 v6, v27

    move/from16 v13, p4

    move-object v15, v7

    move-object v10, v8

    move v11, v9

    move-object v9, v12

    move-object/from16 v12, p2

    goto/16 :goto_0

    :cond_1d
    move/from16 v23, v5

    move/from16 v27, v6

    move-object v8, v10

    move v9, v11

    move-object v7, v15

    move-object v15, v14

    move v2, v0

    move v10, v1

    :goto_16
    move/from16 v1, v23

    move/from16 v0, v27

    const v3, 0xfffff

    if-ne v0, v3, :cond_1e

    goto :goto_17

    :cond_1e
    int-to-long v3, v0

    invoke-virtual {v8, v15, v3, v4, v1}, Lsun/misc/Unsafe;->putInt(Ljava/lang/Object;JI)V

    :goto_17
    iget v0, v7, Lek;->l:I

    :goto_18
    iget v1, v7, Lek;->m:I

    if-ge v0, v1, :cond_21

    iget-object v1, v7, Lek;->k:[I

    aget v1, v1, v0

    invoke-direct {v7, v1}, Lek;->d(I)I

    invoke-direct {v7, v1}, Lek;->e(I)I

    move-result v3

    invoke-static {v3}, Lek;->O00oOoOo(I)J

    move-result-wide v3

    invoke-static {v15, v3, v4}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    if-nez v3, :cond_1f

    goto :goto_19

    :cond_1f
    invoke-direct {v7, v1}, Lek;->c(I)Ldh;

    move-result-object v4

    if-nez v4, :cond_20

    :goto_19
    add-int/lit8 v0, v0, 0x1

    goto :goto_18

    :cond_20
    invoke-static {v3}, Lff;->O0000O0o(Ljava/lang/Object;)Ljava/util/Map;

    invoke-direct {v7, v1}, Lek;->b(I)Ljava/lang/Object;

    move-result-object v0

    invoke-static {v0}, Lff;->O00000Oo(Ljava/lang/Object;)Lea;

    throw v17

    :cond_21
    move/from16 v0, p4

    if-eqz v9, :cond_23

    if-gt v2, v0, :cond_22

    if-ne v10, v9, :cond_22

    goto :goto_1a

    :cond_22
    invoke-static {}, Ldl;->e()Ldl;

    move-result-object v0

    throw v0

    :cond_23
    if-ne v2, v0, :cond_24

    :goto_1a
    return v2

    :cond_24
    invoke-static {}, Ldl;->e()Ldl;

    move-result-object v0

    throw v0

    nop

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_9
        :pswitch_2
        :pswitch_7
        :pswitch_8
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method public final O000000o(Ljava/lang/Object;Lgb;)V
    .locals 12

    iget-boolean v0, p0, Lek;->i:Z

    if-eqz v0, :cond_8

    iget-boolean v0, p0, Lek;->h:Z

    const/4 v1, 0x0

    if-eqz v0, :cond_0

    invoke-static {p1}, Lej;->O00000Oo(Ljava/lang/Object;)Lcu;

    move-result-object v0

    invoke-virtual {v0}, Lcu;->a()Z

    move-result v2

    if-nez v2, :cond_0

    invoke-virtual {v0}, Lcu;->d()Ljava/util/Iterator;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/util/Map$Entry;

    goto :goto_0

    :cond_0
    move-object v0, v1

    move-object v2, v0

    :goto_0
    iget-object v3, p0, Lek;->c:[I

    array-length v3, v3

    const/4 v4, 0x0

    move-object v5, v2

    move v2, v4

    :goto_1
    if-ge v2, v3, :cond_5

    invoke-direct {p0, v2}, Lek;->e(I)I

    move-result v6

    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    :goto_2
    if-nez v5, :cond_1

    goto :goto_3

    :cond_1
    invoke-static {v5}, Lej;->O000000o(Ljava/util/Map$Entry;)I

    move-result v8

    if-gt v8, v7, :cond_3

    invoke-static {p2, v5}, Lej;->O000000o(Lgb;Ljava/util/Map$Entry;)V

    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v5

    if-eqz v5, :cond_2

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Ljava/util/Map$Entry;

    goto :goto_2

    :cond_2
    move-object v5, v1

    goto :goto_2

    :cond_3
    :goto_3
    invoke-static {v6}, Lek;->O000O0Oo(I)I

    move-result v8

    const/4 v9, 0x1

    packed-switch v8, :pswitch_data_0

    goto/16 :goto_16

    :pswitch_0
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    goto/16 :goto_4

    :pswitch_1
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    goto/16 :goto_5

    :pswitch_2
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v6

    goto/16 :goto_6

    :pswitch_3
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    goto/16 :goto_7

    :pswitch_4
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v6

    goto/16 :goto_8

    :pswitch_5
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v6

    goto/16 :goto_9

    :pswitch_6
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v6

    goto/16 :goto_a

    :pswitch_7
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    goto/16 :goto_b

    :pswitch_8
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    goto/16 :goto_c

    :pswitch_9
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    goto/16 :goto_d

    :pswitch_a
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000oo(Ljava/lang/Object;J)Z

    move-result v6

    goto/16 :goto_e

    :pswitch_b
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v6

    goto/16 :goto_f

    :pswitch_c
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    goto/16 :goto_10

    :pswitch_d
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v6

    goto/16 :goto_11

    :pswitch_e
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    goto/16 :goto_12

    :pswitch_f
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v8

    goto/16 :goto_13

    :pswitch_10
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000o0(Ljava/lang/Object;J)F

    move-result v6

    goto/16 :goto_14

    :pswitch_11
    invoke-direct {p0, p1, v7, v2}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lek;->O00000Oo(Ljava/lang/Object;J)D

    move-result-wide v8

    goto/16 :goto_15

    :pswitch_12
    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    invoke-direct {p0, p2, v7, v6, v2}, Lek;->O000000o(Lgb;ILjava/lang/Object;I)V

    goto/16 :goto_16

    :pswitch_13
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-direct {p0, v2}, Lek;->a(I)Les;

    move-result-object v8

    invoke-static {v7, v6, p2, v8}, Leu;->O00000Oo(ILjava/util/List;Lgb;Les;)V

    goto/16 :goto_16

    :pswitch_14
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O00000oO(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_15
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O0000Oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_16
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O0000O0o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_17
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O0000Ooo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_18
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O0000o00(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_19
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O0000Oo0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_1a
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O0000o0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_1b
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O0000OoO(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_1c
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O00000oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_1d
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O0000OOo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_1e
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O00000o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_1f
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O00000o0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_20
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O00000Oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_21
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v10

    invoke-static {p1, v10, v11}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v9}, Leu;->O000000o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_22
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O00000oO(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_23
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O0000Oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_24
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O0000O0o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_25
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O0000Ooo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_26
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O0000o00(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_27
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O0000Oo0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_28
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2}, Leu;->O00000Oo(ILjava/util/List;Lgb;)V

    goto/16 :goto_16

    :pswitch_29
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-direct {p0, v2}, Lek;->a(I)Les;

    move-result-object v8

    invoke-static {v7, v6, p2, v8}, Leu;->O000000o(ILjava/util/List;Lgb;Les;)V

    goto/16 :goto_16

    :pswitch_2a
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2}, Leu;->O000000o(ILjava/util/List;Lgb;)V

    goto/16 :goto_16

    :pswitch_2b
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O0000o0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_2c
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O0000OoO(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_2d
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O00000oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_2e
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O0000OOo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_2f
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O00000o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_30
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O00000o0(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_31
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O00000Oo(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_32
    invoke-direct {p0, v2}, Lek;->d(I)I

    move-result v7

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Ljava/util/List;

    invoke-static {v7, v6, p2, v4}, Leu;->O000000o(ILjava/util/List;Lgb;Z)V

    goto/16 :goto_16

    :pswitch_33
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    :goto_4
    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    invoke-direct {p0, v2}, Lek;->a(I)Les;

    move-result-object v8

    invoke-interface {p2, v7, v6, v8}, Lgb;->O000000o(ILjava/lang/Object;Les;)V

    goto/16 :goto_16

    :pswitch_34
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v8

    :goto_5
    invoke-interface {p2, v7, v8, v9}, Lgb;->O00000o0(IJ)V

    goto/16 :goto_16

    :pswitch_35
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v6

    :goto_6
    invoke-interface {p2, v7, v6}, Lgb;->O00000o(II)V

    goto/16 :goto_16

    :pswitch_36
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v8

    :goto_7
    invoke-interface {p2, v7, v8, v9}, Lgb;->O00000o(IJ)V

    goto/16 :goto_16

    :pswitch_37
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v6

    :goto_8
    invoke-interface {p2, v7, v6}, Lgb;->a(II)V

    goto/16 :goto_16

    :pswitch_38
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v6

    :goto_9
    invoke-interface {p2, v7, v6}, Lgb;->O00000oO(II)V

    goto/16 :goto_16

    :pswitch_39
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v6

    :goto_a
    invoke-interface {p2, v7, v6}, Lgb;->O00000o0(II)V

    goto/16 :goto_16

    :pswitch_3a
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    :goto_b
    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lck;

    invoke-interface {p2, v7, v6}, Lgb;->O000000o(ILck;)V

    goto/16 :goto_16

    :pswitch_3b
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    :goto_c
    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    invoke-direct {p0, v2}, Lek;->a(I)Les;

    move-result-object v8

    invoke-interface {p2, v7, v6, v8}, Lgb;->O00000Oo(ILjava/lang/Object;Les;)V

    goto/16 :goto_16

    :pswitch_3c
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    :goto_d
    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v6

    invoke-static {v7, v6, p2}, Lek;->O000000o(ILjava/lang/Object;Lgb;)V

    goto/16 :goto_16

    :pswitch_3d
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000o0(Ljava/lang/Object;J)Z

    move-result v6

    :goto_e
    invoke-interface {p2, v7, v6}, Lgb;->O000000o(IZ)V

    goto/16 :goto_16

    :pswitch_3e
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v6

    :goto_f
    invoke-interface {p2, v7, v6}, Lgb;->O00000Oo(II)V

    goto :goto_16

    :pswitch_3f
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v8

    :goto_10
    invoke-interface {p2, v7, v8, v9}, Lgb;->O00000Oo(IJ)V

    goto :goto_16

    :pswitch_40
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v6

    :goto_11
    invoke-interface {p2, v7, v6}, Lgb;->O000000o(II)V

    goto :goto_16

    :pswitch_41
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v8

    :goto_12
    invoke-interface {p2, v7, v8, v9}, Lgb;->O000000o(IJ)V

    goto :goto_16

    :pswitch_42
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v8

    :goto_13
    invoke-interface {p2, v7, v8, v9}, Lgb;->a(IJ)V

    goto :goto_16

    :pswitch_43
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000o(Ljava/lang/Object;J)F

    move-result v6

    :goto_14
    invoke-interface {p2, v7, v6}, Lgb;->O000000o(IF)V

    goto :goto_16

    :pswitch_44
    invoke-direct {p0, p1, v2}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v8

    if-eqz v8, :cond_4

    invoke-static {v6}, Lek;->O00oOoOo(I)J

    move-result-wide v8

    invoke-static {p1, v8, v9}, Lfr;->O00000oO(Ljava/lang/Object;J)D

    move-result-wide v8

    :goto_15
    invoke-interface {p2, v7, v8, v9}, Lgb;->O000000o(ID)V

    :cond_4
    :goto_16
    add-int/lit8 v2, v2, 0x3

    goto/16 :goto_1

    :cond_5
    :goto_17
    if-eqz v5, :cond_7

    invoke-static {p2, v5}, Lej;->O000000o(Lgb;Ljava/util/Map$Entry;)V

    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_6

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v2

    move-object v5, v2

    check-cast v5, Ljava/util/Map$Entry;

    goto :goto_17

    :cond_6
    move-object v5, v1

    goto :goto_17

    :cond_7
    iget-object p0, p0, Lek;->o:Lfh;

    invoke-static {p0, p1, p2}, Lek;->O000000o(Lfh;Ljava/lang/Object;Lgb;)V

    return-void

    :cond_8
    invoke-direct {p0, p1, p2}, Lek;->O00000Oo(Ljava/lang/Object;Lgb;)V

    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_44
        :pswitch_43
        :pswitch_42
        :pswitch_41
        :pswitch_40
        :pswitch_3f
        :pswitch_3e
        :pswitch_3d
        :pswitch_3c
        :pswitch_3b
        :pswitch_3a
        :pswitch_39
        :pswitch_38
        :pswitch_37
        :pswitch_36
        :pswitch_35
        :pswitch_34
        :pswitch_33
        :pswitch_32
        :pswitch_31
        :pswitch_30
        :pswitch_2f
        :pswitch_2e
        :pswitch_2d
        :pswitch_2c
        :pswitch_2b
        :pswitch_2a
        :pswitch_29
        :pswitch_28
        :pswitch_27
        :pswitch_26
        :pswitch_25
        :pswitch_24
        :pswitch_23
        :pswitch_22
        :pswitch_21
        :pswitch_20
        :pswitch_1f
        :pswitch_1e
        :pswitch_1d
        :pswitch_1c
        :pswitch_1b
        :pswitch_1a
        :pswitch_19
        :pswitch_18
        :pswitch_17
        :pswitch_16
        :pswitch_15
        :pswitch_14
        :pswitch_13
        :pswitch_12
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method public final O000000o(Ljava/lang/Object;[BIILby;)V
    .locals 8

    iget-boolean v0, p0, Lek;->i:Z

    if-nez v0, :cond_0

    const/4 v6, 0x0

    move-object v1, p0

    move-object v2, p1

    move-object v3, p2

    move v4, p3

    move v5, p4

    move-object v7, p5

    invoke-virtual/range {v1 .. v7}, Lek;->O000000o(Ljava/lang/Object;[BIIILby;)I

    return-void

    :cond_0
    invoke-direct/range {p0 .. p5}, Lek;->O00000Oo(Ljava/lang/Object;[BIILby;)V

    return-void
.end method

.method public final O000000o(Ljava/lang/Object;Ljava/lang/Object;)Z
    .locals 8

    iget-object v0, p0, Lek;->c:[I

    array-length v0, v0

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    if-ge v2, v0, :cond_2

    invoke-direct {p0, v2}, Lek;->e(I)I

    move-result v3

    invoke-static {v3}, Lek;->O00oOoOo(I)J

    move-result-wide v4

    invoke-static {v3}, Lek;->O000O0Oo(I)I

    move-result v3

    packed-switch v3, :pswitch_data_0

    goto/16 :goto_2

    :pswitch_0
    invoke-direct {p0, v2}, Lek;->O000O0OO(I)I

    move-result v3

    const v6, 0xfffff

    and-int/2addr v3, v6

    int-to-long v6, v3

    invoke-static {p1, v6, v7}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    invoke-static {p2, v6, v7}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v6

    if-ne v3, v6, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-static {p2, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v4

    invoke-static {v3, v4}, Leu;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_1

    goto/16 :goto_1

    :pswitch_1
    invoke-static {p1, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-static {p2, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v4

    invoke-static {v3, v4}, Leu;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_0

    goto/16 :goto_2

    :pswitch_2
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-static {p2, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v4

    invoke-static {v3, v4}, Leu;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_0

    goto/16 :goto_2

    :pswitch_3
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v6

    invoke-static {p2, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    cmp-long v3, v6, v3

    if-nez v3, :cond_0

    goto/16 :goto_2

    :pswitch_4
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    invoke-static {p2, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v4

    if-ne v3, v4, :cond_0

    goto/16 :goto_2

    :pswitch_5
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v6

    invoke-static {p2, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    cmp-long v3, v6, v3

    if-nez v3, :cond_0

    goto/16 :goto_2

    :pswitch_6
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    invoke-static {p2, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v4

    if-ne v3, v4, :cond_0

    goto/16 :goto_2

    :pswitch_7
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    invoke-static {p2, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v4

    if-ne v3, v4, :cond_0

    goto/16 :goto_2

    :pswitch_8
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    invoke-static {p2, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v4

    if-ne v3, v4, :cond_0

    goto/16 :goto_2

    :pswitch_9
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-static {p2, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v4

    invoke-static {v3, v4}, Leu;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_0

    goto/16 :goto_2

    :pswitch_a
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-static {p2, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v4

    invoke-static {v3, v4}, Leu;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_0

    goto/16 :goto_2

    :pswitch_b
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-static {p2, v4, v5}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v4

    invoke-static {v3, v4}, Leu;->O000000o(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_0

    goto/16 :goto_2

    :pswitch_c
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000o0(Ljava/lang/Object;J)Z

    move-result v3

    invoke-static {p2, v4, v5}, Lfr;->O00000o0(Ljava/lang/Object;J)Z

    move-result v4

    if-ne v3, v4, :cond_0

    goto/16 :goto_2

    :pswitch_d
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    invoke-static {p2, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v4

    if-ne v3, v4, :cond_0

    goto/16 :goto_2

    :pswitch_e
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v6

    invoke-static {p2, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    cmp-long v3, v6, v3

    if-nez v3, :cond_0

    goto/16 :goto_2

    :pswitch_f
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    invoke-static {p2, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v4

    if-ne v3, v4, :cond_0

    goto :goto_2

    :pswitch_10
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v6

    invoke-static {p2, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    cmp-long v3, v6, v3

    if-nez v3, :cond_0

    goto :goto_2

    :pswitch_11
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v6

    invoke-static {p2, v4, v5}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    cmp-long v3, v6, v3

    if-nez v3, :cond_0

    goto :goto_2

    :pswitch_12
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000o(Ljava/lang/Object;J)F

    move-result v3

    invoke-static {v3}, Ljava/lang/Float;->floatToIntBits(F)I

    move-result v3

    invoke-static {p2, v4, v5}, Lfr;->O00000o(Ljava/lang/Object;J)F

    move-result v4

    invoke-static {v4}, Ljava/lang/Float;->floatToIntBits(F)I

    move-result v4

    if-ne v3, v4, :cond_0

    goto :goto_2

    :pswitch_13
    invoke-direct {p0, p1, p2, v2}, Lek;->O00000o0(Ljava/lang/Object;Ljava/lang/Object;I)Z

    move-result v3

    if-eqz v3, :cond_0

    invoke-static {p1, v4, v5}, Lfr;->O00000oO(Ljava/lang/Object;J)D

    move-result-wide v6

    invoke-static {v6, v7}, Ljava/lang/Double;->doubleToLongBits(D)J

    move-result-wide v6

    invoke-static {p2, v4, v5}, Lfr;->O00000oO(Ljava/lang/Object;J)D

    move-result-wide v3

    invoke-static {v3, v4}, Ljava/lang/Double;->doubleToLongBits(D)J

    move-result-wide v3

    cmp-long v3, v6, v3

    if-nez v3, :cond_0

    goto :goto_2

    :cond_0
    :goto_1
    return v1

    :cond_1
    :goto_2
    add-int/lit8 v2, v2, 0x3

    goto/16 :goto_0

    :cond_2
    iget-object v0, p0, Lek;->o:Lfh;

    invoke-virtual {v0, p1}, Lfh;->O00000Oo(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    iget-object v2, p0, Lek;->o:Lfh;

    invoke-virtual {v2, p2}, Lfh;->O00000Oo(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_4

    iget-boolean p0, p0, Lek;->h:Z

    if-eqz p0, :cond_3

    invoke-static {p1}, Lej;->O00000Oo(Ljava/lang/Object;)Lcu;

    move-result-object p0

    invoke-static {p2}, Lej;->O00000Oo(Ljava/lang/Object;)Lcu;

    move-result-object p1

    invoke-virtual {p0, p1}, Lcu;->equals(Ljava/lang/Object;)Z

    move-result p0

    return p0

    :cond_3
    const/4 p0, 0x1

    return p0

    :cond_4
    return v1

    nop

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_13
        :pswitch_12
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_1
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
    .end packed-switch
.end method

.method public final O00000Oo(Ljava/lang/Object;)I
    .locals 1

    iget-boolean v0, p0, Lek;->i:Z

    if-nez v0, :cond_0

    invoke-direct {p0, p1}, Lek;->O0000Oo0(Ljava/lang/Object;)I

    move-result p0

    goto :goto_0

    :cond_0
    invoke-direct {p0, p1}, Lek;->O0000Oo(Ljava/lang/Object;)I

    move-result p0

    :goto_0
    return p0
.end method

.method public final O00000Oo(Ljava/lang/Object;Ljava/lang/Object;)V
    .locals 6

    if-eqz p2, :cond_3

    const/4 v0, 0x0

    :goto_0
    iget-object v1, p0, Lek;->c:[I

    array-length v1, v1

    if-ge v0, v1, :cond_1

    invoke-direct {p0, v0}, Lek;->e(I)I

    move-result v1

    invoke-static {v1}, Lek;->O00oOoOo(I)J

    move-result-wide v2

    invoke-direct {p0, v0}, Lek;->d(I)I

    move-result v4

    invoke-static {v1}, Lek;->O000O0Oo(I)I

    move-result v1

    packed-switch v1, :pswitch_data_0

    goto/16 :goto_7

    :pswitch_0
    invoke-direct {p0, p2, v4, v0}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_1

    :pswitch_1
    invoke-direct {p0, p1, p2, v0}, Lek;->O00000Oo(Ljava/lang/Object;Ljava/lang/Object;I)V

    goto/16 :goto_7

    :pswitch_2
    invoke-direct {p0, p2, v4, v0}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v1

    if-eqz v1, :cond_0

    :goto_1
    invoke-static {p2, v2, v3}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v1

    invoke-static {p1, v2, v3, v1}, Lfr;->O000000o(Ljava/lang/Object;JLjava/lang/Object;)V

    invoke-direct {p0, p1, v4, v0}, Lek;->O00000Oo(Ljava/lang/Object;II)V

    goto/16 :goto_7

    :pswitch_3
    iget-object v1, p0, Lek;->r:Lff;

    invoke-static {v1, p1, p2, v2, v3}, Leu;->O000000o(Lff;Ljava/lang/Object;Ljava/lang/Object;J)V

    goto/16 :goto_7

    :pswitch_4
    iget-object v1, p0, Lek;->n:Ldv;

    invoke-virtual {v1, p1, p2, v2, v3}, Ldv;->O000000o(Ljava/lang/Object;Ljava/lang/Object;J)V

    goto/16 :goto_7

    :pswitch_5
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto/16 :goto_5

    :pswitch_6
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_2

    :pswitch_7
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto/16 :goto_5

    :pswitch_8
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_2

    :pswitch_9
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    :goto_2
    goto :goto_4

    :pswitch_a
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_4

    :pswitch_b
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_3

    :pswitch_c
    invoke-direct {p0, p1, p2, v0}, Lek;->O000000o(Ljava/lang/Object;Ljava/lang/Object;I)V

    goto/16 :goto_7

    :pswitch_d
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    :goto_3
    invoke-static {p2, v2, v3}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v1

    invoke-static {p1, v2, v3, v1}, Lfr;->O000000o(Ljava/lang/Object;JLjava/lang/Object;)V

    goto :goto_6

    :pswitch_e
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-static {p2, v2, v3}, Lfr;->O00000o0(Ljava/lang/Object;J)Z

    move-result v1

    invoke-static {p1, v2, v3, v1}, Lfr;->O000000o(Ljava/lang/Object;JZ)V

    goto :goto_6

    :pswitch_f
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_4

    :pswitch_10
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_5

    :pswitch_11
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    :goto_4
    invoke-static {p2, v2, v3}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v1

    invoke-static {p1, v2, v3, v1}, Lfr;->O000000o(Ljava/lang/Object;JI)V

    goto :goto_6

    :pswitch_12
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    goto :goto_5

    :pswitch_13
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    :goto_5
    invoke-static {p2, v2, v3}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v4

    invoke-static {p1, v2, v3, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;JJ)V

    goto :goto_6

    :pswitch_14
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-static {p2, v2, v3}, Lfr;->O00000o(Ljava/lang/Object;J)F

    move-result v1

    invoke-static {p1, v2, v3, v1}, Lfr;->O000000o(Ljava/lang/Object;JF)V

    goto :goto_6

    :pswitch_15
    invoke-direct {p0, p2, v0}, Lek;->O000000o(Ljava/lang/Object;I)Z

    move-result v1

    if-eqz v1, :cond_0

    invoke-static {p2, v2, v3}, Lfr;->O00000oO(Ljava/lang/Object;J)D

    move-result-wide v4

    invoke-static {p1, v2, v3, v4, v5}, Lfr;->O000000o(Ljava/lang/Object;JD)V

    :goto_6
    invoke-direct {p0, p1, v0}, Lek;->O00000Oo(Ljava/lang/Object;I)V

    :cond_0
    :goto_7
    add-int/lit8 v0, v0, 0x3

    goto/16 :goto_0

    :cond_1
    iget-object v0, p0, Lek;->o:Lfh;

    invoke-static {v0, p1, p2}, Leu;->O000000o(Lfh;Ljava/lang/Object;Ljava/lang/Object;)V

    iget-boolean v0, p0, Lek;->h:Z

    if-eqz v0, :cond_2

    iget-object p0, p0, Lek;->p:Lej;

    invoke-static {p0, p1, p2}, Leu;->O000000o(Lej;Ljava/lang/Object;Ljava/lang/Object;)V

    :cond_2
    return-void

    :cond_3
    const/4 p0, 0x0

    throw p0

    nop

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_15
        :pswitch_14
        :pswitch_13
        :pswitch_12
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_c
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_2
        :pswitch_1
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_0
        :pswitch_1
    .end packed-switch
.end method

.method public final O00000o(Ljava/lang/Object;)V
    .locals 5

    iget v0, p0, Lek;->l:I

    :goto_0
    iget v1, p0, Lek;->m:I

    if-ge v0, v1, :cond_1

    iget-object v1, p0, Lek;->k:[I

    aget v1, v1, v0

    invoke-direct {p0, v1}, Lek;->e(I)I

    move-result v1

    invoke-static {v1}, Lek;->O00oOoOo(I)J

    move-result-wide v1

    invoke-static {p1, v1, v2}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    if-nez v3, :cond_0

    goto :goto_1

    :cond_0
    invoke-static {v3}, Lff;->O00000o0(Ljava/lang/Object;)V

    invoke-static {p1, v1, v2, v3}, Lfr;->O000000o(Ljava/lang/Object;JLjava/lang/Object;)V

    :goto_1
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    :cond_1
    iget-object v0, p0, Lek;->k:[I

    array-length v0, v0

    :goto_2
    if-ge v1, v0, :cond_2

    iget-object v2, p0, Lek;->n:Ldv;

    iget-object v3, p0, Lek;->k:[I

    aget v3, v3, v1

    int-to-long v3, v3

    invoke-virtual {v2, p1, v3, v4}, Ldv;->O000000o(Ljava/lang/Object;J)V

    add-int/lit8 v1, v1, 0x1

    goto :goto_2

    :cond_2
    iget-object v0, p0, Lek;->o:Lfh;

    invoke-virtual {v0, p1}, Lfh;->O00000oO(Ljava/lang/Object;)V

    iget-boolean v0, p0, Lek;->h:Z

    if-eqz v0, :cond_3

    iget-object p0, p0, Lek;->p:Lej;

    invoke-virtual {p0, p1}, Lej;->O00000oO(Ljava/lang/Object;)V

    :cond_3
    return-void
.end method

.method public final O00000o0(Ljava/lang/Object;)Z
    .locals 17

    move-object/from16 v6, p0

    move-object/from16 v7, p1

    const v8, 0xfffff

    const/4 v9, 0x0

    move v0, v8

    move v1, v9

    move v10, v1

    :goto_0
    iget v2, v6, Lek;->l:I

    const/4 v3, 0x1

    if-ge v10, v2, :cond_b

    iget-object v2, v6, Lek;->k:[I

    aget v11, v2, v10

    invoke-direct {v6, v11}, Lek;->d(I)I

    move-result v12

    invoke-direct {v6, v11}, Lek;->e(I)I

    move-result v13

    iget-object v2, v6, Lek;->c:[I

    add-int/lit8 v4, v11, 0x2

    aget v2, v2, v4

    and-int v4, v2, v8

    ushr-int/lit8 v2, v2, 0x14

    shl-int v14, v3, v2

    if-ne v4, v0, :cond_0

    move v15, v0

    move/from16 v16, v1

    goto :goto_2

    :cond_0
    if-eq v4, v8, :cond_1

    sget-object v0, Lek;->b:Lsun/misc/Unsafe;

    int-to-long v1, v4

    invoke-virtual {v0, v7, v1, v2}, Lsun/misc/Unsafe;->getInt(Ljava/lang/Object;J)I

    move-result v0

    move/from16 v16, v0

    goto :goto_1

    :cond_1
    move/from16 v16, v1

    :goto_1
    move v15, v4

    :goto_2
    const/high16 v0, 0x10000000

    and-int/2addr v0, v13

    if-nez v0, :cond_2

    goto :goto_3

    :cond_2
    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move v2, v11

    move v3, v15

    move/from16 v4, v16

    move v5, v14

    invoke-direct/range {v0 .. v5}, Lek;->O000000o(Ljava/lang/Object;IIII)Z

    move-result v0

    if-nez v0, :cond_3

    return v9

    :cond_3
    :goto_3
    invoke-static {v13}, Lek;->O000O0Oo(I)I

    move-result v0

    const/16 v1, 0x9

    if-eq v0, v1, :cond_9

    const/16 v1, 0x11

    if-eq v0, v1, :cond_9

    const/16 v1, 0x1b

    if-eq v0, v1, :cond_7

    const/16 v1, 0x3c

    if-eq v0, v1, :cond_6

    const/16 v1, 0x44

    if-eq v0, v1, :cond_6

    const/16 v1, 0x31

    if-eq v0, v1, :cond_7

    const/16 v1, 0x32

    if-eq v0, v1, :cond_4

    goto/16 :goto_5

    :cond_4
    invoke-static {v13}, Lek;->O00oOoOo(I)J

    move-result-wide v0

    invoke-static {v7, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v0

    invoke-static {v0}, Lff;->O00000oO(Ljava/lang/Object;)Ljava/util/Map;

    move-result-object v0

    invoke-interface {v0}, Ljava/util/Map;->isEmpty()Z

    move-result v0

    if-eqz v0, :cond_5

    goto :goto_5

    :cond_5
    invoke-direct {v6, v11}, Lek;->b(I)Ljava/lang/Object;

    move-result-object v0

    invoke-static {v0}, Lff;->O00000Oo(Ljava/lang/Object;)Lea;

    const/4 v0, 0x0

    throw v0

    :cond_6
    invoke-direct {v6, v7, v12, v11}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v0

    if-eqz v0, :cond_a

    invoke-direct {v6, v11}, Lek;->a(I)Les;

    move-result-object v0

    invoke-static {v7, v13, v0}, Lek;->O000000o(Ljava/lang/Object;ILes;)Z

    move-result v0

    if-nez v0, :cond_a

    return v9

    :cond_7
    invoke-static {v13}, Lek;->O00oOoOo(I)J

    move-result-wide v0

    invoke-static {v7, v0, v1}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/util/List;

    invoke-interface {v0}, Ljava/util/List;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_a

    invoke-direct {v6, v11}, Lek;->a(I)Les;

    move-result-object v1

    move v2, v9

    :goto_4
    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v3

    if-ge v2, v3, :cond_a

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    invoke-interface {v1, v3}, Les;->O00000o0(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_8

    add-int/lit8 v2, v2, 0x1

    goto :goto_4

    :cond_8
    return v9

    :cond_9
    move-object/from16 v0, p0

    move-object/from16 v1, p1

    move v2, v11

    move v3, v15

    move/from16 v4, v16

    move v5, v14

    invoke-direct/range {v0 .. v5}, Lek;->O000000o(Ljava/lang/Object;IIII)Z

    move-result v0

    if-eqz v0, :cond_a

    invoke-direct {v6, v11}, Lek;->a(I)Les;

    move-result-object v0

    invoke-static {v7, v13, v0}, Lek;->O000000o(Ljava/lang/Object;ILes;)Z

    move-result v0

    if-nez v0, :cond_a

    return v9

    :cond_a
    :goto_5
    add-int/lit8 v10, v10, 0x1

    move v0, v15

    move/from16 v1, v16

    goto/16 :goto_0

    :cond_b
    iget-boolean v0, v6, Lek;->h:Z

    if-eqz v0, :cond_c

    invoke-static/range {p1 .. p1}, Lej;->O00000Oo(Ljava/lang/Object;)Lcu;

    move-result-object v0

    invoke-virtual {v0}, Lcu;->e()Z

    move-result v0

    if-nez v0, :cond_c

    return v9

    :cond_c
    return v3
.end method

.method public final O00000oO(Ljava/lang/Object;)I
    .locals 8

    iget-object v0, p0, Lek;->c:[I

    array-length v0, v0

    const/4 v1, 0x0

    move v2, v1

    :goto_0
    if-ge v1, v0, :cond_2

    invoke-direct {p0, v1}, Lek;->e(I)I

    move-result v3

    invoke-direct {p0, v1}, Lek;->d(I)I

    move-result v4

    invoke-static {v3}, Lek;->O00oOoOo(I)J

    move-result-wide v5

    invoke-static {v3}, Lek;->O000O0Oo(I)I

    move-result v3

    const/16 v7, 0x25

    packed-switch v3, :pswitch_data_0

    goto/16 :goto_c

    :pswitch_0
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto/16 :goto_4

    :pswitch_1
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto/16 :goto_3

    :pswitch_2
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto :goto_1

    :pswitch_3
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto/16 :goto_3

    :pswitch_4
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto :goto_1

    :pswitch_5
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    :goto_1
    goto :goto_2

    :pswitch_6
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto :goto_2

    :pswitch_7
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto/16 :goto_4

    :pswitch_8
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto/16 :goto_4

    :pswitch_9
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto/16 :goto_6

    :pswitch_a
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lek;->O00000oo(Ljava/lang/Object;J)Z

    move-result v3

    goto/16 :goto_7

    :pswitch_b
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto :goto_2

    :pswitch_c
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto :goto_3

    :pswitch_d
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    :goto_2
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lek;->O00000o(Ljava/lang/Object;J)I

    move-result v3

    goto/16 :goto_b

    :pswitch_e
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    goto :goto_3

    :pswitch_f
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    :goto_3
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lek;->O00000oO(Ljava/lang/Object;J)J

    move-result-wide v3

    goto/16 :goto_a

    :pswitch_10
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lek;->O00000o0(Ljava/lang/Object;J)F

    move-result v3

    goto :goto_8

    :pswitch_11
    invoke-direct {p0, p1, v4, v1}, Lek;->O000000o(Ljava/lang/Object;II)Z

    move-result v3

    if-eqz v3, :cond_1

    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lek;->O00000Oo(Ljava/lang/Object;J)D

    move-result-wide v3

    goto :goto_9

    :pswitch_12
    invoke-static {p1, v5, v6}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    if-eqz v3, :cond_0

    goto :goto_5

    :goto_4
    :pswitch_13
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/Object;->hashCode()I

    move-result v3

    goto :goto_b

    :pswitch_14
    invoke-static {p1, v5, v6}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    if-eqz v3, :cond_0

    :goto_5
    invoke-virtual {v3}, Ljava/lang/Object;->hashCode()I

    move-result v7

    :cond_0
    mul-int/lit8 v2, v2, 0x35

    add-int/2addr v2, v7

    goto :goto_c

    :goto_6
    :pswitch_15
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lfr;->O00000oo(Ljava/lang/Object;J)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Ljava/lang/String;

    invoke-virtual {v3}, Ljava/lang/String;->hashCode()I

    move-result v3

    goto :goto_b

    :pswitch_16
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lfr;->O00000o0(Ljava/lang/Object;J)Z

    move-result v3

    :goto_7
    invoke-static {v3}, Ldj;->a(Z)I

    move-result v3

    goto :goto_b

    :pswitch_17
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lfr;->O000000o(Ljava/lang/Object;J)I

    move-result v3

    goto :goto_b

    :pswitch_18
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lfr;->O00000Oo(Ljava/lang/Object;J)J

    move-result-wide v3

    goto :goto_a

    :pswitch_19
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lfr;->O00000o(Ljava/lang/Object;J)F

    move-result v3

    :goto_8
    invoke-static {v3}, Ljava/lang/Float;->floatToIntBits(F)I

    move-result v3

    goto :goto_b

    :pswitch_1a
    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1, v5, v6}, Lfr;->O00000oO(Ljava/lang/Object;J)D

    move-result-wide v3

    :goto_9
    invoke-static {v3, v4}, Ljava/lang/Double;->doubleToLongBits(D)J

    move-result-wide v3

    :goto_a
    invoke-static {v3, v4}, Ldj;->a(J)I

    move-result v3

    :goto_b
    add-int/2addr v2, v3

    :cond_1
    :goto_c
    add-int/lit8 v1, v1, 0x3

    goto/16 :goto_0

    :cond_2
    mul-int/lit8 v2, v2, 0x35

    iget-object v0, p0, Lek;->o:Lfh;

    invoke-virtual {v0, p1}, Lfh;->O00000Oo(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Object;->hashCode()I

    move-result v0

    add-int/2addr v2, v0

    iget-boolean p0, p0, Lek;->h:Z

    if-eqz p0, :cond_3

    mul-int/lit8 v2, v2, 0x35

    invoke-static {p1}, Lej;->O00000Oo(Ljava/lang/Object;)Lcu;

    move-result-object p0

    invoke-virtual {p0}, Lcu;->hashCode()I

    move-result p0

    add-int/2addr v2, p0

    :cond_3
    return v2

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_1a
        :pswitch_19
        :pswitch_18
        :pswitch_18
        :pswitch_17
        :pswitch_18
        :pswitch_17
        :pswitch_16
        :pswitch_15
        :pswitch_14
        :pswitch_13
        :pswitch_17
        :pswitch_17
        :pswitch_17
        :pswitch_18
        :pswitch_17
        :pswitch_18
        :pswitch_12
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_13
        :pswitch_11
        :pswitch_10
        :pswitch_f
        :pswitch_e
        :pswitch_d
        :pswitch_c
        :pswitch_b
        :pswitch_a
        :pswitch_9
        :pswitch_8
        :pswitch_7
        :pswitch_6
        :pswitch_5
        :pswitch_4
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method public final a()Ljava/lang/Object;
    .locals 0

    iget-object p0, p0, Lek;->g:Leh;

    invoke-static {p0}, Lfs;->O00000oO(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    return-object p0
.end method

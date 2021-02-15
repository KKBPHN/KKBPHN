.class final Lep;
.super Ljava/lang/Object;
.source ""


# static fields
.field public static final a:Lep;


# instance fields
.field private final b:Let;

.field private final c:Ljava/util/concurrent/ConcurrentMap;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    new-instance v0, Lep;

    invoke-direct {v0}, Lep;-><init>()V

    sput-object v0, Lep;->a:Lep;

    return-void
.end method

.method private constructor <init>()V
    .locals 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    new-instance v0, Ljava/util/concurrent/ConcurrentHashMap;

    invoke-direct {v0}, Ljava/util/concurrent/ConcurrentHashMap;-><init>()V

    iput-object v0, p0, Lep;->c:Ljava/util/concurrent/ConcurrentMap;

    new-instance v0, Ldz;

    invoke-direct {v0}, Ldz;-><init>()V

    iput-object v0, p0, Lep;->b:Let;

    return-void
.end method


# virtual methods
.method public final O00000Oo(Ljava/lang/Class;)Les;
    .locals 8

    const-string v0, "messageType"

    invoke-static {p1, v0}, Ldj;->a(Ljava/lang/Object;Ljava/lang/String;)V

    iget-object v1, p0, Lep;->c:Ljava/util/concurrent/ConcurrentMap;

    invoke-interface {v1, p1}, Ljava/util/concurrent/ConcurrentMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Les;

    if-nez v1, :cond_6

    iget-object v1, p0, Lep;->b:Let;

    invoke-static {p1}, Leu;->O00000Oo(Ljava/lang/Class;)V

    check-cast v1, Ldz;

    iget-object v1, v1, Ldz;->a:Lef;

    invoke-interface {v1, p1}, Lef;->O000000o(Ljava/lang/Class;)Lee;

    move-result-object v2

    invoke-interface {v2}, Lee;->a()Z

    move-result v1

    if-eqz v1, :cond_1

    const-class v1, Lde;

    invoke-virtual {v1, p1}, Ljava/lang/Class;->isAssignableFrom(Ljava/lang/Class;)Z

    move-result v1

    if-eqz v1, :cond_0

    sget-object v1, Leu;->c:Lfh;

    sget-object v3, Lct;->a:Lej;

    goto :goto_0

    :cond_0
    sget-object v1, Leu;->a:Lfh;

    invoke-static {}, Lct;->a()Lej;

    move-result-object v3

    :goto_0
    invoke-interface {v2}, Lee;->b()Leh;

    move-result-object v2

    invoke-static {v1, v3, v2}, Lel;->O000000o(Lfh;Lej;Leh;)Lel;

    move-result-object v1

    goto :goto_4

    :cond_1
    const-class v1, Lde;

    invoke-virtual {v1, p1}, Ljava/lang/Class;->isAssignableFrom(Ljava/lang/Class;)Z

    move-result v1

    if-eqz v1, :cond_3

    invoke-static {v2}, Ldz;->O000000o(Lee;)Z

    move-result v1

    sget-object v3, Len;->b:Lfs;

    sget-object v4, Ldv;->b:Ldv;

    sget-object v5, Leu;->c:Lfh;

    if-eqz v1, :cond_2

    sget-object v6, Lct;->a:Lej;

    goto :goto_1

    :cond_2
    const/4 v6, 0x0

    :goto_1
    sget-object v7, Led;->b:Lff;

    goto :goto_3

    :cond_3
    invoke-static {v2}, Ldz;->O000000o(Lee;)Z

    move-result v1

    sget-object v3, Len;->a:Lfs;

    sget-object v4, Ldv;->a:Ldv;

    if-eqz v1, :cond_4

    sget-object v5, Leu;->a:Lfh;

    invoke-static {}, Lct;->a()Lej;

    move-result-object v6

    goto :goto_2

    :cond_4
    sget-object v5, Leu;->b:Lfh;

    const/4 v6, 0x0

    :goto_2
    sget-object v7, Led;->a:Lff;

    :goto_3
    invoke-static/range {v2 .. v7}, Lek;->O000000o(Lee;Lfs;Ldv;Lfh;Lej;Lff;)Lek;

    move-result-object v1

    :goto_4
    invoke-static {p1, v0}, Ldj;->a(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v0, "schema"

    invoke-static {v1, v0}, Ldj;->a(Ljava/lang/Object;Ljava/lang/String;)V

    iget-object p0, p0, Lep;->c:Ljava/util/concurrent/ConcurrentMap;

    invoke-interface {p0, p1, v1}, Ljava/util/concurrent/ConcurrentMap;->putIfAbsent(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object p0

    check-cast p0, Les;

    if-nez p0, :cond_5

    goto :goto_5

    :cond_5
    return-object p0

    :cond_6
    :goto_5
    return-object v1
.end method

.method public final O00000oO(Ljava/lang/Object;)Les;
    .locals 0

    invoke-virtual {p1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object p1

    invoke-virtual {p0, p1}, Lep;->O00000Oo(Ljava/lang/Class;)Les;

    move-result-object p0

    return-object p0
.end method

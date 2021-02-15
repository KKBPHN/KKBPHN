.class public abstract Lcom/airbnb/lottie/parser/moshi/O00000Oo;
.super Ljava/lang/Object;
.source ""

# interfaces
.implements Ljava/io/Closeable;


# static fields
.field private static final REPLACEMENT_CHARS:[Ljava/lang/String;


# instance fields
.field O0O0oOo:[I

.field O0O0oo:[I

.field O0O0oo0:[Ljava/lang/String;

.field O0O0ooO:Z

.field lenient:Z

.field stackSize:I


# direct methods
.method static constructor <clinit>()V
    .locals 5

    const/16 v0, 0x80

    new-array v0, v0, [Ljava/lang/String;

    sput-object v0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->REPLACEMENT_CHARS:[Ljava/lang/String;

    const/4 v0, 0x0

    move v1, v0

    :goto_0
    const/16 v2, 0x1f

    if-gt v1, v2, :cond_0

    sget-object v2, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->REPLACEMENT_CHARS:[Ljava/lang/String;

    const/4 v3, 0x1

    new-array v3, v3, [Ljava/lang/Object;

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    aput-object v4, v3, v0

    const-string v4, "\\u%04x"

    invoke-static {v4, v3}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v3

    aput-object v3, v2, v1

    add-int/lit8 v1, v1, 0x1

    goto :goto_0

    :cond_0
    sget-object v0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->REPLACEMENT_CHARS:[Ljava/lang/String;

    const/16 v1, 0x22

    const-string v2, "\\\""

    aput-object v2, v0, v1

    const/16 v1, 0x5c

    const-string v2, "\\\\"

    aput-object v2, v0, v1

    const/16 v1, 0x9

    const-string v2, "\\t"

    aput-object v2, v0, v1

    const/16 v1, 0x8

    const-string v2, "\\b"

    aput-object v2, v0, v1

    const/16 v1, 0xa

    const-string v2, "\\n"

    aput-object v2, v0, v1

    const/16 v1, 0xd

    const-string v2, "\\r"

    aput-object v2, v0, v1

    const/16 v1, 0xc

    const-string v2, "\\f"

    aput-object v2, v0, v1

    return-void
.end method

.method constructor <init>()V
    .locals 2

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    const/16 v0, 0x20

    new-array v1, v0, [I

    iput-object v1, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oOo:[I

    new-array v1, v0, [Ljava/lang/String;

    iput-object v1, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oo0:[Ljava/lang/String;

    new-array v0, v0, [I

    iput-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oo:[I

    return-void
.end method

.method public static O000000o(Lokio/BufferedSource;)Lcom/airbnb/lottie/parser/moshi/O00000Oo;
    .locals 1

    new-instance v0, Lcom/airbnb/lottie/parser/moshi/O00000o;

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/parser/moshi/O00000o;-><init>(Lokio/BufferedSource;)V

    return-object v0
.end method

.method static synthetic O000000o(Lokio/BufferedSink;Ljava/lang/String;)V
    .locals 0

    invoke-static {p0, p1}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O00000Oo(Lokio/BufferedSink;Ljava/lang/String;)V

    return-void
.end method

.method private static O00000Oo(Lokio/BufferedSink;Ljava/lang/String;)V
    .locals 7

    sget-object v0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->REPLACEMENT_CHARS:[Ljava/lang/String;

    const/16 v1, 0x22

    invoke-interface {p0, v1}, Lokio/BufferedSink;->writeByte(I)Lokio/BufferedSink;

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v2

    const/4 v3, 0x0

    move v4, v3

    :goto_0
    if-ge v3, v2, :cond_5

    invoke-virtual {p1, v3}, Ljava/lang/String;->charAt(I)C

    move-result v5

    const/16 v6, 0x80

    if-ge v5, v6, :cond_0

    aget-object v5, v0, v5

    if-nez v5, :cond_2

    goto :goto_2

    :cond_0
    const/16 v6, 0x2028

    if-ne v5, v6, :cond_1

    const-string v5, "\\u2028"

    goto :goto_1

    :cond_1
    const/16 v6, 0x2029

    if-ne v5, v6, :cond_4

    const-string v5, "\\u2029"

    :cond_2
    :goto_1
    if-ge v4, v3, :cond_3

    invoke-interface {p0, p1, v4, v3}, Lokio/BufferedSink;->writeUtf8(Ljava/lang/String;II)Lokio/BufferedSink;

    :cond_3
    invoke-interface {p0, v5}, Lokio/BufferedSink;->writeUtf8(Ljava/lang/String;)Lokio/BufferedSink;

    add-int/lit8 v4, v3, 0x1

    :cond_4
    :goto_2
    add-int/lit8 v3, v3, 0x1

    goto :goto_0

    :cond_5
    if-ge v4, v2, :cond_6

    invoke-interface {p0, p1, v4, v2}, Lokio/BufferedSink;->writeUtf8(Ljava/lang/String;II)Lokio/BufferedSink;

    :cond_6
    invoke-interface {p0, v1}, Lokio/BufferedSink;->writeByte(I)Lokio/BufferedSink;

    return-void
.end method


# virtual methods
.method public abstract O000000o(Lcom/airbnb/lottie/parser/moshi/O000000o;)I
.end method

.method final O0000o0(I)V
    .locals 3

    iget v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->stackSize:I

    iget-object v1, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oOo:[I

    array-length v2, v1

    if-ne v0, v2, :cond_1

    const/16 v2, 0x100

    if-eq v0, v2, :cond_0

    array-length v0, v1

    mul-int/lit8 v0, v0, 0x2

    invoke-static {v1, v0}, Ljava/util/Arrays;->copyOf([II)[I

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oOo:[I

    iget-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oo0:[Ljava/lang/String;

    array-length v1, v0

    mul-int/lit8 v1, v1, 0x2

    invoke-static {v0, v1}, Ljava/util/Arrays;->copyOf([Ljava/lang/Object;I)[Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/String;

    iput-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oo0:[Ljava/lang/String;

    iget-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oo:[I

    array-length v1, v0

    mul-int/lit8 v1, v1, 0x2

    invoke-static {v0, v1}, Ljava/util/Arrays;->copyOf([II)[I

    move-result-object v0

    iput-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oo:[I

    goto :goto_0

    :cond_0
    new-instance p1, Lcom/airbnb/lottie/parser/moshi/JsonDataException;

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "Nesting too deep at "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->getPath()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v0, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {p1, p0}, Lcom/airbnb/lottie/parser/moshi/JsonDataException;-><init>(Ljava/lang/String;)V

    throw p1

    :cond_1
    :goto_0
    iget-object v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oOo:[I

    iget v1, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->stackSize:I

    add-int/lit8 v2, v1, 0x1

    iput v2, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->stackSize:I

    aput p1, v0, v1

    return-void
.end method

.method public abstract O00o0O0()V
.end method

.method public abstract beginArray()V
.end method

.method public abstract beginObject()V
.end method

.method public abstract endArray()V
.end method

.method public abstract endObject()V
.end method

.method public final getPath()Ljava/lang/String;
    .locals 3

    iget v0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->stackSize:I

    iget-object v1, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oOo:[I

    iget-object v2, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oo0:[Ljava/lang/String;

    iget-object p0, p0, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->O0O0oo:[I

    invoke-static {v0, v1, v2, p0}, Lcom/airbnb/lottie/parser/moshi/O00000o0;->O000000o(I[I[Ljava/lang/String;[I)Ljava/lang/String;

    move-result-object p0

    return-object p0
.end method

.method public abstract hasNext()Z
.end method

.method public abstract nextBoolean()Z
.end method

.method public abstract nextDouble()D
.end method

.method public abstract nextInt()I
.end method

.method public abstract nextName()Ljava/lang/String;
.end method

.method public abstract nextString()Ljava/lang/String;
.end method

.method public abstract peek()Lcom/airbnb/lottie/parser/moshi/JsonReader$Token;
.end method

.method public abstract skipValue()V
.end method

.method final syntaxError(Ljava/lang/String;)Lcom/airbnb/lottie/parser/moshi/JsonEncodingException;
    .locals 2

    new-instance v0, Lcom/airbnb/lottie/parser/moshi/JsonEncodingException;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string p1, " at path "

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {p0}, Lcom/airbnb/lottie/parser/moshi/O00000Oo;->getPath()Ljava/lang/String;

    move-result-object p0

    invoke-virtual {v1, p0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object p0

    invoke-direct {v0, p0}, Lcom/airbnb/lottie/parser/moshi/JsonEncodingException;-><init>(Ljava/lang/String;)V

    throw v0
.end method

package miui.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import miui.util.Pools.Manager;
import miui.util.Pools.Pool;

public class IOUtils {
    private static final Pool BYTE_ARRAY_OUTPUT_STREAM_POOL = Pools.createSoftReferencePool(new Manager() {
        public ByteArrayOutputStream createInstance() {
            return new ByteArrayOutputStream();
        }

        public void onRelease(ByteArrayOutputStream byteArrayOutputStream) {
            byteArrayOutputStream.reset();
        }
    }, 2);
    private static final Pool CHAR_ARRAY_WRITER_POOL = Pools.createSoftReferencePool(new Manager() {
        public CharArrayWriter createInstance() {
            return new CharArrayWriter();
        }

        public void onRelease(CharArrayWriter charArrayWriter) {
            charArrayWriter.reset();
        }
    }, 2);
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final String LINE_SEPARATOR;
    private static final Pool STRING_WRITER_POOL = Pools.createSoftReferencePool(new Manager() {
        public StringWriter createInstance() {
            return new StringWriter();
        }

        public void onRelease(StringWriter stringWriter) {
            stringWriter.getBuffer().setLength(0);
        }
    }, 2);
    private static final ThreadLocal THREAD_LOCAL_BYTE_BUFFER = new ThreadLocal();
    private static final ThreadLocal THREAD_LOCAL_CHAR_BUFFER = new ThreadLocal();

    static {
        StringWriter stringWriter = (StringWriter) STRING_WRITER_POOL.acquire();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println();
        printWriter.flush();
        LINE_SEPARATOR = stringWriter.toString();
        printWriter.close();
        STRING_WRITER_POOL.release(stringWriter);
    }

    protected IOUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void closeQuietly(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException unused) {
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:1|2|3|4|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0005 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void closeQuietly(OutputStream outputStream) {
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
    }

    public static void closeQuietly(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException unused) {
            }
        }
    }

    public static void closeQuietly(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException unused) {
            }
        }
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) {
        byte[] byteArrayBuffer = getByteArrayBuffer();
        long j = 0;
        while (true) {
            int read = inputStream.read(byteArrayBuffer);
            if (read != -1) {
                outputStream.write(byteArrayBuffer, 0, read);
                j += (long) read;
            } else {
                outputStream.flush();
                return j;
            }
        }
    }

    public static long copy(Reader reader, Writer writer) {
        char[] charArrayBuffer = getCharArrayBuffer();
        long j = 0;
        while (true) {
            int read = reader.read(charArrayBuffer);
            if (read != -1) {
                writer.write(charArrayBuffer, 0, read);
                j += (long) read;
            } else {
                writer.flush();
                return j;
            }
        }
    }

    public static void copy(InputStream inputStream, Writer writer) {
        copy((Reader) new InputStreamReader(inputStream), writer);
    }

    public static void copy(InputStream inputStream, Writer writer, String str) {
        InputStreamReader inputStreamReader = (str == null || str.length() == 0) ? new InputStreamReader(inputStream) : new InputStreamReader(inputStream, str);
        copy((Reader) inputStreamReader, writer);
    }

    public static void copy(Reader reader, OutputStream outputStream) {
        copy(reader, (Writer) new OutputStreamWriter(outputStream));
    }

    public static void copy(Reader reader, OutputStream outputStream, String str) {
        OutputStreamWriter outputStreamWriter = (str == null || str.length() == 0) ? new OutputStreamWriter(outputStream) : new OutputStreamWriter(outputStream, str);
        copy(reader, (Writer) outputStreamWriter);
    }

    private static byte[] getByteArrayBuffer() {
        SoftReference softReference = (SoftReference) THREAD_LOCAL_BYTE_BUFFER.get();
        byte[] bArr = softReference != null ? (byte[]) softReference.get() : null;
        if (bArr != null) {
            return bArr;
        }
        byte[] bArr2 = new byte[4096];
        THREAD_LOCAL_BYTE_BUFFER.set(new SoftReference(bArr2));
        return bArr2;
    }

    private static char[] getCharArrayBuffer() {
        SoftReference softReference = (SoftReference) THREAD_LOCAL_CHAR_BUFFER.get();
        char[] cArr = softReference != null ? (char[]) softReference.get() : null;
        if (cArr != null) {
            return cArr;
        }
        char[] cArr2 = new char[4096];
        THREAD_LOCAL_CHAR_BUFFER.set(new SoftReference(cArr2));
        return cArr2;
    }

    public static List readLines(InputStream inputStream) {
        return readLines((Reader) new InputStreamReader(inputStream));
    }

    public static List readLines(InputStream inputStream, String str) {
        InputStreamReader inputStreamReader = (str == null || str.length() == 0) ? new InputStreamReader(inputStream) : new InputStreamReader(inputStream, str);
        return readLines((Reader) inputStreamReader);
    }

    public static List readLines(Reader reader) {
        BufferedReader bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        ArrayList arrayList = new ArrayList();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return arrayList;
            }
            arrayList.add(readLine);
        }
    }

    public static byte[] toByteArray(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) BYTE_ARRAY_OUTPUT_STREAM_POOL.acquire();
        copy(inputStream, (OutputStream) byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        BYTE_ARRAY_OUTPUT_STREAM_POOL.release(byteArrayOutputStream);
        return byteArray;
    }

    public static byte[] toByteArray(Reader reader) {
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) BYTE_ARRAY_OUTPUT_STREAM_POOL.acquire();
        copy(reader, (OutputStream) byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        BYTE_ARRAY_OUTPUT_STREAM_POOL.release(byteArrayOutputStream);
        return byteArray;
    }

    public static byte[] toByteArray(Reader reader, String str) {
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) BYTE_ARRAY_OUTPUT_STREAM_POOL.acquire();
        copy(reader, (OutputStream) byteArrayOutputStream, str);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        BYTE_ARRAY_OUTPUT_STREAM_POOL.release(byteArrayOutputStream);
        return byteArray;
    }

    public static char[] toCharArray(InputStream inputStream) {
        CharArrayWriter charArrayWriter = (CharArrayWriter) CHAR_ARRAY_WRITER_POOL.acquire();
        copy(inputStream, (Writer) charArrayWriter);
        char[] charArray = charArrayWriter.toCharArray();
        CHAR_ARRAY_WRITER_POOL.release(charArrayWriter);
        return charArray;
    }

    public static char[] toCharArray(InputStream inputStream, String str) {
        CharArrayWriter charArrayWriter = (CharArrayWriter) CHAR_ARRAY_WRITER_POOL.acquire();
        copy(inputStream, (Writer) charArrayWriter, str);
        char[] charArray = charArrayWriter.toCharArray();
        CHAR_ARRAY_WRITER_POOL.release(charArrayWriter);
        return charArray;
    }

    public static char[] toCharArray(Reader reader) {
        CharArrayWriter charArrayWriter = (CharArrayWriter) CHAR_ARRAY_WRITER_POOL.acquire();
        copy(reader, (Writer) charArrayWriter);
        char[] charArray = charArrayWriter.toCharArray();
        CHAR_ARRAY_WRITER_POOL.release(charArrayWriter);
        return charArray;
    }

    public static InputStream toInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static InputStream toInputStream(String str, String str2) {
        byte[] bytes = (str2 == null || str2.length() == 0) ? str.getBytes() : str.getBytes(str2);
        return new ByteArrayInputStream(bytes);
    }

    public static String toString(InputStream inputStream) {
        StringWriter stringWriter = (StringWriter) STRING_WRITER_POOL.acquire();
        copy(inputStream, (Writer) stringWriter);
        String stringWriter2 = stringWriter.toString();
        STRING_WRITER_POOL.release(stringWriter);
        return stringWriter2;
    }

    public static String toString(InputStream inputStream, String str) {
        StringWriter stringWriter = (StringWriter) STRING_WRITER_POOL.acquire();
        copy(inputStream, (Writer) stringWriter, str);
        String stringWriter2 = stringWriter.toString();
        STRING_WRITER_POOL.release(stringWriter);
        return stringWriter2;
    }

    public static String toString(Reader reader) {
        StringWriter stringWriter = (StringWriter) STRING_WRITER_POOL.acquire();
        copy(reader, (Writer) stringWriter);
        String stringWriter2 = stringWriter.toString();
        STRING_WRITER_POOL.release(stringWriter);
        return stringWriter2;
    }

    public static void write(OutputStream outputStream, String str) {
        if (str != null) {
            outputStream.write(str.getBytes());
        }
    }

    public static void write(OutputStream outputStream, String str, String str2) {
        if (str != null) {
            byte[] bytes = (str2 == null || str2.length() == 0) ? str.getBytes() : str.getBytes(str2);
            outputStream.write(bytes);
        }
    }

    public static void write(OutputStream outputStream, byte[] bArr) {
        if (bArr != null) {
            outputStream.write(bArr);
        }
    }

    public static void write(OutputStream outputStream, char[] cArr) {
        if (cArr != null) {
            outputStream.write(new String(cArr).getBytes());
        }
    }

    public static void write(OutputStream outputStream, char[] cArr, String str) {
        if (cArr != null) {
            byte[] bytes = (str == null || str.length() == 0) ? new String(cArr).getBytes() : new String(cArr).getBytes(str);
            outputStream.write(bytes);
        }
    }

    public static void write(Writer writer, String str) {
        if (str != null) {
            writer.write(str);
        }
    }

    public static void write(Writer writer, byte[] bArr) {
        if (bArr != null) {
            writer.write(new String(bArr));
        }
    }

    public static void write(Writer writer, byte[] bArr, String str) {
        if (bArr != null) {
            String str2 = (str == null || str.length() == 0) ? new String(bArr) : new String(bArr, str);
            writer.write(str2);
        }
    }

    public static void write(Writer writer, char[] cArr) {
        if (cArr != null) {
            writer.write(cArr);
        }
    }

    public static void writeLines(OutputStream outputStream, Collection collection, String str) {
        if (collection != null) {
            if (str == null) {
                str = LINE_SEPARATOR;
            }
            for (Object next : collection) {
                if (next != null) {
                    outputStream.write(next.toString().getBytes());
                }
                outputStream.write(str.getBytes());
            }
        }
    }

    public static void writeLines(OutputStream outputStream, Collection collection, String str, String str2) {
        if (collection != null) {
            if (str == null) {
                str = LINE_SEPARATOR;
            }
            for (Object next : collection) {
                if (next != null) {
                    outputStream.write(next.toString().getBytes(str2));
                }
                outputStream.write(str.getBytes(str2));
            }
        }
    }

    public static void writeLines(Writer writer, Collection collection, String str) {
        if (collection != null) {
            if (str == null) {
                str = LINE_SEPARATOR;
            }
            for (Object next : collection) {
                if (next != null) {
                    writer.write(next.toString());
                }
                writer.write(str);
            }
        }
    }
}

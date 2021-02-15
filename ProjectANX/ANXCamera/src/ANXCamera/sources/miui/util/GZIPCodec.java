package miui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class GZIPCodec {
    protected GZIPCodec() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static byte[] decode(byte[] bArr) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        InputStream inputStream = null;
        try {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            try {
                byte[] byteArray = IOUtils.toByteArray((InputStream) gZIPInputStream);
                IOUtils.closeQuietly((InputStream) gZIPInputStream);
                return byteArray;
            } catch (Throwable th) {
                th = th;
                inputStream = gZIPInputStream;
                IOUtils.closeQuietly(inputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            IOUtils.closeQuietly(inputStream);
            throw th;
        }
    }

    public static byte[] encode(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = null;
        try {
            GZIPOutputStream gZIPOutputStream2 = new GZIPOutputStream(byteArrayOutputStream);
            try {
                gZIPOutputStream2.write(bArr);
                IOUtils.closeQuietly((OutputStream) gZIPOutputStream2);
                return byteArrayOutputStream.toByteArray();
            } catch (Throwable th) {
                th = th;
                gZIPOutputStream = gZIPOutputStream2;
                IOUtils.closeQuietly((OutputStream) gZIPOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            IOUtils.closeQuietly((OutputStream) gZIPOutputStream);
            throw th;
        }
    }

    public static String getID() {
        return "gzip";
    }
}

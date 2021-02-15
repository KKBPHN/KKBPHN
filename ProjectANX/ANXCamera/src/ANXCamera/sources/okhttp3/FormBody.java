package okhttp3;

import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

public final class FormBody extends RequestBody {
    private static final MediaType CONTENT_TYPE = MediaType.parse(HttpRequest.CONTENT_TYPE_FORM);
    private final List encodedNames;
    private final List encodedValues;

    public final class Builder {
        private final Charset charset;
        private final List names;
        private final List values;

        public Builder() {
            this(null);
        }

        public Builder(Charset charset2) {
            this.names = new ArrayList();
            this.values = new ArrayList();
            this.charset = charset2;
        }

        public Builder add(String str, String str2) {
            String str3 = str;
            this.names.add(HttpUrl.canonicalize(str3, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true, this.charset));
            String str4 = str2;
            this.values.add(HttpUrl.canonicalize(str4, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true, this.charset));
            return this;
        }

        public Builder addEncoded(String str, String str2) {
            String str3 = str;
            this.names.add(HttpUrl.canonicalize(str3, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true, this.charset));
            String str4 = str2;
            this.values.add(HttpUrl.canonicalize(str4, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true, this.charset));
            return this;
        }

        public FormBody build() {
            return new FormBody(this.names, this.values);
        }
    }

    FormBody(List list, List list2) {
        this.encodedNames = Util.immutableList(list);
        this.encodedValues = Util.immutableList(list2);
    }

    private long writeOrCountBytes(@Nullable BufferedSink bufferedSink, boolean z) {
        Buffer buffer = z ? new Buffer() : bufferedSink.buffer();
        int size = this.encodedNames.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                buffer.writeByte(38);
            }
            buffer.writeUtf8((String) this.encodedNames.get(i));
            buffer.writeByte(61);
            buffer.writeUtf8((String) this.encodedValues.get(i));
        }
        if (!z) {
            return 0;
        }
        long size2 = buffer.size();
        buffer.clear();
        return size2;
    }

    public long contentLength() {
        return writeOrCountBytes(null, true);
    }

    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    public String encodedName(int i) {
        return (String) this.encodedNames.get(i);
    }

    public String encodedValue(int i) {
        return (String) this.encodedValues.get(i);
    }

    public String name(int i) {
        return HttpUrl.percentDecode(encodedName(i), true);
    }

    public int size() {
        return this.encodedNames.size();
    }

    public String value(int i) {
        return HttpUrl.percentDecode(encodedValue(i), true);
    }

    public void writeTo(BufferedSink bufferedSink) {
        writeOrCountBytes(bufferedSink, false);
    }
}

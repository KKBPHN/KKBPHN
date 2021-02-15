package com.xiaomi.idm.service.iot.proto;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.GeneratedMessageLite.DefaultInstanceBasedParser;
import com.google.protobuf.GeneratedMessageLite.MethodToInvoke;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class DataProto {

    /* renamed from: com.xiaomi.idm.service.iot.proto.DataProto$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke = new int[MethodToInvoke.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[MethodToInvoke.NEW_MUTABLE_INSTANCE.ordinal()] = 1;
            $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[MethodToInvoke.NEW_BUILDER.ordinal()] = 2;
            $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[MethodToInvoke.BUILD_MESSAGE_INFO.ordinal()] = 3;
            $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[MethodToInvoke.GET_DEFAULT_INSTANCE.ordinal()] = 4;
            $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[MethodToInvoke.GET_PARSER.ordinal()] = 5;
            $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[MethodToInvoke.GET_MEMOIZED_IS_INITIALIZED.ordinal()] = 6;
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[MethodToInvoke.SET_MEMOIZED_IS_INITIALIZED.ordinal()] = 7;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public final class Response extends GeneratedMessageLite implements ResponseOrBuilder {
        public static final int CODE_FIELD_NUMBER = 1;
        /* access modifiers changed from: private */
        public static final Response DEFAULT_INSTANCE;
        public static final int MESSAGE_FIELD_NUMBER = 2;
        private static volatile Parser PARSER = null;
        public static final int RESPONSE_FIELD_NUMBER = 3;
        private int code_;
        private String message_;
        private String response_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements ResponseOrBuilder {
            private Builder() {
                super(Response.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearCode() {
                copyOnWrite();
                this.instance.clearCode();
                return this;
            }

            public Builder clearMessage() {
                copyOnWrite();
                this.instance.clearMessage();
                return this;
            }

            public Builder clearResponse() {
                copyOnWrite();
                this.instance.clearResponse();
                return this;
            }

            public int getCode() {
                return this.instance.getCode();
            }

            public String getMessage() {
                return this.instance.getMessage();
            }

            public ByteString getMessageBytes() {
                return this.instance.getMessageBytes();
            }

            public String getResponse() {
                return this.instance.getResponse();
            }

            public ByteString getResponseBytes() {
                return this.instance.getResponseBytes();
            }

            public Builder setCode(int i) {
                copyOnWrite();
                this.instance.setCode(i);
                return this;
            }

            public Builder setMessage(String str) {
                copyOnWrite();
                this.instance.setMessage(str);
                return this;
            }

            public Builder setMessageBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setMessageBytes(byteString);
                return this;
            }

            public Builder setResponse(String str) {
                copyOnWrite();
                this.instance.setResponse(str);
                return this;
            }

            public Builder setResponseBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setResponseBytes(byteString);
                return this;
            }
        }

        static {
            Response response = new Response();
            DEFAULT_INSTANCE = response;
            GeneratedMessageLite.registerDefaultInstance(Response.class, response);
        }

        private Response() {
            String str = "";
            this.message_ = str;
            this.response_ = str;
        }

        /* access modifiers changed from: private */
        public void clearCode() {
            this.code_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearMessage() {
            this.message_ = getDefaultInstance().getMessage();
        }

        /* access modifiers changed from: private */
        public void clearResponse() {
            this.response_ = getDefaultInstance().getResponse();
        }

        public static Response getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(Response response) {
            return DEFAULT_INSTANCE.createBuilder(response);
        }

        public static Response parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static Response parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static Response parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static Response parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static Response parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static Response parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Response parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static Response parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static Response parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static Response parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static Response parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static Response parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setCode(int i) {
            this.code_ = i;
        }

        /* access modifiers changed from: private */
        public void setMessage(String str) {
            if (str != null) {
                this.message_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setMessageBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.message_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setResponse(String str) {
            if (str != null) {
                this.response_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setResponseBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.response_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new Response();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ", new Object[]{"code_", "message_", "response_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (Response.class) {
                            defaultInstanceBasedParser = PARSER;
                            if (defaultInstanceBasedParser == null) {
                                defaultInstanceBasedParser = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                                PARSER = defaultInstanceBasedParser;
                            }
                        }
                    }
                    return defaultInstanceBasedParser;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public int getCode() {
            return this.code_;
        }

        public String getMessage() {
            return this.message_;
        }

        public ByteString getMessageBytes() {
            return ByteString.copyFromUtf8(this.message_);
        }

        public String getResponse() {
            return this.response_;
        }

        public ByteString getResponseBytes() {
            return ByteString.copyFromUtf8(this.response_);
        }
    }

    public interface ResponseOrBuilder extends MessageLiteOrBuilder {
        int getCode();

        String getMessage();

        ByteString getMessageBytes();

        String getResponse();

        ByteString getResponseBytes();
    }

    private DataProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }
}

package com.xiaomi.idm.service.test.localetestservice.proto;

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

public final class ActionsProto {

    /* renamed from: com.xiaomi.idm.service.test.localetestservice.proto.ActionsProto$1 reason: invalid class name */
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

    public final class GetSomeString extends GeneratedMessageLite implements GetSomeStringOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        /* access modifiers changed from: private */
        public static final GetSomeString DEFAULT_INSTANCE;
        public static final int PARAM_FIELD_NUMBER = 2;
        private static volatile Parser PARSER;
        private int aid_;
        private String param_ = "";

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements GetSomeStringOrBuilder {
            private Builder() {
                super(GetSomeString.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearAid() {
                copyOnWrite();
                this.instance.clearAid();
                return this;
            }

            public Builder clearParam() {
                copyOnWrite();
                this.instance.clearParam();
                return this;
            }

            public int getAid() {
                return this.instance.getAid();
            }

            public String getParam() {
                return this.instance.getParam();
            }

            public ByteString getParamBytes() {
                return this.instance.getParamBytes();
            }

            public Builder setAid(int i) {
                copyOnWrite();
                this.instance.setAid(i);
                return this;
            }

            public Builder setParam(String str) {
                copyOnWrite();
                this.instance.setParam(str);
                return this;
            }

            public Builder setParamBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setParamBytes(byteString);
                return this;
            }
        }

        static {
            GetSomeString getSomeString = new GetSomeString();
            DEFAULT_INSTANCE = getSomeString;
            GeneratedMessageLite.registerDefaultInstance(GetSomeString.class, getSomeString);
        }

        private GetSomeString() {
        }

        /* access modifiers changed from: private */
        public void clearAid() {
            this.aid_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearParam() {
            this.param_ = getDefaultInstance().getParam();
        }

        public static GetSomeString getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(GetSomeString getSomeString) {
            return DEFAULT_INSTANCE.createBuilder(getSomeString);
        }

        public static GetSomeString parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetSomeString parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetSomeString parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static GetSomeString parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static GetSomeString parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static GetSomeString parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static GetSomeString parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetSomeString parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetSomeString parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static GetSomeString parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static GetSomeString parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static GetSomeString parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setAid(int i) {
            this.aid_ = i;
        }

        /* access modifiers changed from: private */
        public void setParam(String str) {
            if (str != null) {
                this.param_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setParamBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.param_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new GetSomeString();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0004\u0002Ȉ", new Object[]{"aid_", "param_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (GetSomeString.class) {
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

        public int getAid() {
            return this.aid_;
        }

        public String getParam() {
            return this.param_;
        }

        public ByteString getParamBytes() {
            return ByteString.copyFromUtf8(this.param_);
        }
    }

    public interface GetSomeStringOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getParam();

        ByteString getParamBytes();
    }

    private ActionsProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }
}

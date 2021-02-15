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

public final class EventsProto {

    /* renamed from: com.xiaomi.idm.service.test.localetestservice.proto.EventsProto$1 reason: invalid class name */
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

    public final class SomeEvent extends GeneratedMessageLite implements SomeEventOrBuilder {
        /* access modifiers changed from: private */
        public static final SomeEvent DEFAULT_INSTANCE;
        public static final int PARAM_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private int param_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements SomeEventOrBuilder {
            private Builder() {
                super(SomeEvent.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearParam() {
                copyOnWrite();
                this.instance.clearParam();
                return this;
            }

            public int getParam() {
                return this.instance.getParam();
            }

            public Builder setParam(int i) {
                copyOnWrite();
                this.instance.setParam(i);
                return this;
            }
        }

        static {
            SomeEvent someEvent = new SomeEvent();
            DEFAULT_INSTANCE = someEvent;
            GeneratedMessageLite.registerDefaultInstance(SomeEvent.class, someEvent);
        }

        private SomeEvent() {
        }

        /* access modifiers changed from: private */
        public void clearParam() {
            this.param_ = 0;
        }

        public static SomeEvent getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(SomeEvent someEvent) {
            return DEFAULT_INSTANCE.createBuilder(someEvent);
        }

        public static SomeEvent parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SomeEvent parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SomeEvent parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static SomeEvent parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static SomeEvent parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static SomeEvent parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static SomeEvent parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SomeEvent parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SomeEvent parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static SomeEvent parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static SomeEvent parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static SomeEvent parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setParam(int i) {
            this.param_ = i;
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new SomeEvent();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\u0004", new Object[]{"param_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (SomeEvent.class) {
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

        public int getParam() {
            return this.param_;
        }
    }

    public interface SomeEventOrBuilder extends MessageLiteOrBuilder {
        int getParam();
    }

    private EventsProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }
}

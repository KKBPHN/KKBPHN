package com.xiaomi.idm.service.ipcamera.proto;

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

    /* renamed from: com.xiaomi.idm.service.ipcamera.proto.ActionsProto$1 reason: invalid class name */
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

    public final class GetIpcSkeleton extends GeneratedMessageLite implements GetIpcSkeletonOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final GetIpcSkeleton DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements GetIpcSkeletonOrBuilder {
            private Builder() {
                super(GetIpcSkeleton.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearAid() {
                copyOnWrite();
                this.instance.clearAid();
                return this;
            }

            public Builder clearAppId() {
                copyOnWrite();
                this.instance.clearAppId();
                return this;
            }

            public Builder clearServiceToken() {
                copyOnWrite();
                this.instance.clearServiceToken();
                return this;
            }

            public int getAid() {
                return this.instance.getAid();
            }

            public String getAppId() {
                return this.instance.getAppId();
            }

            public ByteString getAppIdBytes() {
                return this.instance.getAppIdBytes();
            }

            public String getServiceToken() {
                return this.instance.getServiceToken();
            }

            public ByteString getServiceTokenBytes() {
                return this.instance.getServiceTokenBytes();
            }

            public Builder setAid(int i) {
                copyOnWrite();
                this.instance.setAid(i);
                return this;
            }

            public Builder setAppId(String str) {
                copyOnWrite();
                this.instance.setAppId(str);
                return this;
            }

            public Builder setAppIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setAppIdBytes(byteString);
                return this;
            }

            public Builder setServiceToken(String str) {
                copyOnWrite();
                this.instance.setServiceToken(str);
                return this;
            }

            public Builder setServiceTokenBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setServiceTokenBytes(byteString);
                return this;
            }
        }

        static {
            GetIpcSkeleton getIpcSkeleton = new GetIpcSkeleton();
            DEFAULT_INSTANCE = getIpcSkeleton;
            GeneratedMessageLite.registerDefaultInstance(GetIpcSkeleton.class, getIpcSkeleton);
        }

        private GetIpcSkeleton() {
            String str = "";
            this.serviceToken_ = str;
            this.appId_ = str;
        }

        /* access modifiers changed from: private */
        public void clearAid() {
            this.aid_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearAppId() {
            this.appId_ = getDefaultInstance().getAppId();
        }

        /* access modifiers changed from: private */
        public void clearServiceToken() {
            this.serviceToken_ = getDefaultInstance().getServiceToken();
        }

        public static GetIpcSkeleton getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(GetIpcSkeleton getIpcSkeleton) {
            return DEFAULT_INSTANCE.createBuilder(getIpcSkeleton);
        }

        public static GetIpcSkeleton parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetIpcSkeleton parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetIpcSkeleton parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static GetIpcSkeleton parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static GetIpcSkeleton parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static GetIpcSkeleton parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static GetIpcSkeleton parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetIpcSkeleton parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetIpcSkeleton parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static GetIpcSkeleton parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static GetIpcSkeleton parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static GetIpcSkeleton parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setAppId(String str) {
            if (str != null) {
                this.appId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setAppIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.appId_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setServiceToken(String str) {
            if (str != null) {
                this.serviceToken_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setServiceTokenBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.serviceToken_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new GetIpcSkeleton();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ", new Object[]{"aid_", "serviceToken_", "appId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (GetIpcSkeleton.class) {
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

        public String getAppId() {
            return this.appId_;
        }

        public ByteString getAppIdBytes() {
            return ByteString.copyFromUtf8(this.appId_);
        }

        public String getServiceToken() {
            return this.serviceToken_;
        }

        public ByteString getServiceTokenBytes() {
            return ByteString.copyFromUtf8(this.serviceToken_);
        }
    }

    public interface GetIpcSkeletonOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    private ActionsProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }
}

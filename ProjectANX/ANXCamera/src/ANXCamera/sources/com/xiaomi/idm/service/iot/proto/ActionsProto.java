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

public final class ActionsProto {

    /* renamed from: com.xiaomi.idm.service.iot.proto.ActionsProto$1 reason: invalid class name */
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

    public final class ExeScenes extends GeneratedMessageLite implements ExeScenesOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final ExeScenes DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int SCENEID_FIELD_NUMBER = 4;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String sceneId_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements ExeScenesOrBuilder {
            private Builder() {
                super(ExeScenes.DEFAULT_INSTANCE);
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

            public Builder clearSceneId() {
                copyOnWrite();
                this.instance.clearSceneId();
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

            public String getSceneId() {
                return this.instance.getSceneId();
            }

            public ByteString getSceneIdBytes() {
                return this.instance.getSceneIdBytes();
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

            public Builder setSceneId(String str) {
                copyOnWrite();
                this.instance.setSceneId(str);
                return this;
            }

            public Builder setSceneIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setSceneIdBytes(byteString);
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
            ExeScenes exeScenes = new ExeScenes();
            DEFAULT_INSTANCE = exeScenes;
            GeneratedMessageLite.registerDefaultInstance(ExeScenes.class, exeScenes);
        }

        private ExeScenes() {
            String str = "";
            this.serviceToken_ = str;
            this.appId_ = str;
            this.sceneId_ = str;
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
        public void clearSceneId() {
            this.sceneId_ = getDefaultInstance().getSceneId();
        }

        /* access modifiers changed from: private */
        public void clearServiceToken() {
            this.serviceToken_ = getDefaultInstance().getServiceToken();
        }

        public static ExeScenes getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(ExeScenes exeScenes) {
            return DEFAULT_INSTANCE.createBuilder(exeScenes);
        }

        public static ExeScenes parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ExeScenes parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ExeScenes parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static ExeScenes parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static ExeScenes parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static ExeScenes parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static ExeScenes parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ExeScenes parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ExeScenes parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static ExeScenes parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static ExeScenes parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static ExeScenes parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setSceneId(String str) {
            if (str != null) {
                this.sceneId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setSceneIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.sceneId_ = byteString.toStringUtf8();
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
                    return new ExeScenes();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ\u0004Ȉ", new Object[]{"aid_", "serviceToken_", "appId_", "sceneId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (ExeScenes.class) {
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

        public String getSceneId() {
            return this.sceneId_;
        }

        public ByteString getSceneIdBytes() {
            return ByteString.copyFromUtf8(this.sceneId_);
        }

        public String getServiceToken() {
            return this.serviceToken_;
        }

        public ByteString getServiceTokenBytes() {
            return ByteString.copyFromUtf8(this.serviceToken_);
        }
    }

    public interface ExeScenesOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getSceneId();

        ByteString getSceneIdBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    public final class GetDeviceInformations extends GeneratedMessageLite implements GetDeviceInformationsOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final GetDeviceInformations DEFAULT_INSTANCE;
        public static final int DEVICEID_FIELD_NUMBER = 4;
        private static volatile Parser PARSER = null;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String deviceId_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements GetDeviceInformationsOrBuilder {
            private Builder() {
                super(GetDeviceInformations.DEFAULT_INSTANCE);
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

            public Builder clearDeviceId() {
                copyOnWrite();
                this.instance.clearDeviceId();
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

            public String getDeviceId() {
                return this.instance.getDeviceId();
            }

            public ByteString getDeviceIdBytes() {
                return this.instance.getDeviceIdBytes();
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

            public Builder setDeviceId(String str) {
                copyOnWrite();
                this.instance.setDeviceId(str);
                return this;
            }

            public Builder setDeviceIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setDeviceIdBytes(byteString);
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
            GetDeviceInformations getDeviceInformations = new GetDeviceInformations();
            DEFAULT_INSTANCE = getDeviceInformations;
            GeneratedMessageLite.registerDefaultInstance(GetDeviceInformations.class, getDeviceInformations);
        }

        private GetDeviceInformations() {
            String str = "";
            this.serviceToken_ = str;
            this.appId_ = str;
            this.deviceId_ = str;
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
        public void clearDeviceId() {
            this.deviceId_ = getDefaultInstance().getDeviceId();
        }

        /* access modifiers changed from: private */
        public void clearServiceToken() {
            this.serviceToken_ = getDefaultInstance().getServiceToken();
        }

        public static GetDeviceInformations getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(GetDeviceInformations getDeviceInformations) {
            return DEFAULT_INSTANCE.createBuilder(getDeviceInformations);
        }

        public static GetDeviceInformations parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetDeviceInformations parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetDeviceInformations parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static GetDeviceInformations parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static GetDeviceInformations parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static GetDeviceInformations parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static GetDeviceInformations parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetDeviceInformations parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetDeviceInformations parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static GetDeviceInformations parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static GetDeviceInformations parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static GetDeviceInformations parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setDeviceId(String str) {
            if (str != null) {
                this.deviceId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setDeviceIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.deviceId_ = byteString.toStringUtf8();
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
                    return new GetDeviceInformations();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ\u0004Ȉ", new Object[]{"aid_", "serviceToken_", "appId_", "deviceId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (GetDeviceInformations.class) {
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

        public String getDeviceId() {
            return this.deviceId_;
        }

        public ByteString getDeviceIdBytes() {
            return ByteString.copyFromUtf8(this.deviceId_);
        }

        public String getServiceToken() {
            return this.serviceToken_;
        }

        public ByteString getServiceTokenBytes() {
            return ByteString.copyFromUtf8(this.serviceToken_);
        }
    }

    public interface GetDeviceInformationsOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getDeviceId();

        ByteString getDeviceIdBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    public final class GetDeviceProperties extends GeneratedMessageLite implements GetDevicePropertiesOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final GetDeviceProperties DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int PROPERTYID_FIELD_NUMBER = 4;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String propertyId_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements GetDevicePropertiesOrBuilder {
            private Builder() {
                super(GetDeviceProperties.DEFAULT_INSTANCE);
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

            public Builder clearPropertyId() {
                copyOnWrite();
                this.instance.clearPropertyId();
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

            public String getPropertyId() {
                return this.instance.getPropertyId();
            }

            public ByteString getPropertyIdBytes() {
                return this.instance.getPropertyIdBytes();
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

            public Builder setPropertyId(String str) {
                copyOnWrite();
                this.instance.setPropertyId(str);
                return this;
            }

            public Builder setPropertyIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setPropertyIdBytes(byteString);
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
            GetDeviceProperties getDeviceProperties = new GetDeviceProperties();
            DEFAULT_INSTANCE = getDeviceProperties;
            GeneratedMessageLite.registerDefaultInstance(GetDeviceProperties.class, getDeviceProperties);
        }

        private GetDeviceProperties() {
            String str = "";
            this.serviceToken_ = str;
            this.appId_ = str;
            this.propertyId_ = str;
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
        public void clearPropertyId() {
            this.propertyId_ = getDefaultInstance().getPropertyId();
        }

        /* access modifiers changed from: private */
        public void clearServiceToken() {
            this.serviceToken_ = getDefaultInstance().getServiceToken();
        }

        public static GetDeviceProperties getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(GetDeviceProperties getDeviceProperties) {
            return DEFAULT_INSTANCE.createBuilder(getDeviceProperties);
        }

        public static GetDeviceProperties parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetDeviceProperties parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetDeviceProperties parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static GetDeviceProperties parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static GetDeviceProperties parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static GetDeviceProperties parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static GetDeviceProperties parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetDeviceProperties parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetDeviceProperties parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static GetDeviceProperties parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static GetDeviceProperties parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static GetDeviceProperties parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setPropertyId(String str) {
            if (str != null) {
                this.propertyId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setPropertyIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.propertyId_ = byteString.toStringUtf8();
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
                    return new GetDeviceProperties();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ\u0004Ȉ", new Object[]{"aid_", "serviceToken_", "appId_", "propertyId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (GetDeviceProperties.class) {
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

        public String getPropertyId() {
            return this.propertyId_;
        }

        public ByteString getPropertyIdBytes() {
            return ByteString.copyFromUtf8(this.propertyId_);
        }

        public String getServiceToken() {
            return this.serviceToken_;
        }

        public ByteString getServiceTokenBytes() {
            return ByteString.copyFromUtf8(this.serviceToken_);
        }
    }

    public interface GetDevicePropertiesOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getPropertyId();

        ByteString getPropertyIdBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    public final class GetDevices extends GeneratedMessageLite implements GetDevicesOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final GetDevices DEFAULT_INSTANCE;
        public static final int ISLOCAL_FIELD_NUMBER = 4;
        private static volatile Parser PARSER = null;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private boolean isLocal_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements GetDevicesOrBuilder {
            private Builder() {
                super(GetDevices.DEFAULT_INSTANCE);
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

            public Builder clearIsLocal() {
                copyOnWrite();
                this.instance.clearIsLocal();
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

            public boolean getIsLocal() {
                return this.instance.getIsLocal();
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

            public Builder setIsLocal(boolean z) {
                copyOnWrite();
                this.instance.setIsLocal(z);
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
            GetDevices getDevices = new GetDevices();
            DEFAULT_INSTANCE = getDevices;
            GeneratedMessageLite.registerDefaultInstance(GetDevices.class, getDevices);
        }

        private GetDevices() {
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
        public void clearIsLocal() {
            this.isLocal_ = false;
        }

        /* access modifiers changed from: private */
        public void clearServiceToken() {
            this.serviceToken_ = getDefaultInstance().getServiceToken();
        }

        public static GetDevices getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(GetDevices getDevices) {
            return DEFAULT_INSTANCE.createBuilder(getDevices);
        }

        public static GetDevices parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetDevices parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetDevices parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static GetDevices parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static GetDevices parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static GetDevices parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static GetDevices parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetDevices parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetDevices parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static GetDevices parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static GetDevices parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static GetDevices parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setIsLocal(boolean z) {
            this.isLocal_ = z;
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
                    return new GetDevices();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0004\u0000\u0000\u0001\u0004\u0004\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ\u0004\u0007", new Object[]{"aid_", "serviceToken_", "appId_", "isLocal_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (GetDevices.class) {
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

        public boolean getIsLocal() {
            return this.isLocal_;
        }

        public String getServiceToken() {
            return this.serviceToken_;
        }

        public ByteString getServiceTokenBytes() {
            return ByteString.copyFromUtf8(this.serviceToken_);
        }
    }

    public interface GetDevicesOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        boolean getIsLocal();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    public final class GetHomeFastCommands extends GeneratedMessageLite implements GetHomeFastCommandsOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final GetHomeFastCommands DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements GetHomeFastCommandsOrBuilder {
            private Builder() {
                super(GetHomeFastCommands.DEFAULT_INSTANCE);
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
            GetHomeFastCommands getHomeFastCommands = new GetHomeFastCommands();
            DEFAULT_INSTANCE = getHomeFastCommands;
            GeneratedMessageLite.registerDefaultInstance(GetHomeFastCommands.class, getHomeFastCommands);
        }

        private GetHomeFastCommands() {
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

        public static GetHomeFastCommands getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(GetHomeFastCommands getHomeFastCommands) {
            return DEFAULT_INSTANCE.createBuilder(getHomeFastCommands);
        }

        public static GetHomeFastCommands parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetHomeFastCommands parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetHomeFastCommands parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static GetHomeFastCommands parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static GetHomeFastCommands parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static GetHomeFastCommands parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static GetHomeFastCommands parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetHomeFastCommands parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetHomeFastCommands parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static GetHomeFastCommands parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static GetHomeFastCommands parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static GetHomeFastCommands parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
                    return new GetHomeFastCommands();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ", new Object[]{"aid_", "serviceToken_", "appId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (GetHomeFastCommands.class) {
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

    public interface GetHomeFastCommandsOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    public final class GetHomes extends GeneratedMessageLite implements GetHomesOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final GetHomes DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements GetHomesOrBuilder {
            private Builder() {
                super(GetHomes.DEFAULT_INSTANCE);
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
            GetHomes getHomes = new GetHomes();
            DEFAULT_INSTANCE = getHomes;
            GeneratedMessageLite.registerDefaultInstance(GetHomes.class, getHomes);
        }

        private GetHomes() {
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

        public static GetHomes getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(GetHomes getHomes) {
            return DEFAULT_INSTANCE.createBuilder(getHomes);
        }

        public static GetHomes parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetHomes parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetHomes parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static GetHomes parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static GetHomes parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static GetHomes parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static GetHomes parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetHomes parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetHomes parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static GetHomes parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static GetHomes parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static GetHomes parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
                    return new GetHomes();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ", new Object[]{"aid_", "serviceToken_", "appId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (GetHomes.class) {
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

    public interface GetHomesOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    public final class GetScenes extends GeneratedMessageLite implements GetScenesOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final GetScenes DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements GetScenesOrBuilder {
            private Builder() {
                super(GetScenes.DEFAULT_INSTANCE);
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
            GetScenes getScenes = new GetScenes();
            DEFAULT_INSTANCE = getScenes;
            GeneratedMessageLite.registerDefaultInstance(GetScenes.class, getScenes);
        }

        private GetScenes() {
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

        public static GetScenes getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(GetScenes getScenes) {
            return DEFAULT_INSTANCE.createBuilder(getScenes);
        }

        public static GetScenes parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetScenes parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetScenes parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static GetScenes parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static GetScenes parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static GetScenes parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static GetScenes parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static GetScenes parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static GetScenes parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static GetScenes parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static GetScenes parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static GetScenes parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
                    return new GetScenes();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ", new Object[]{"aid_", "serviceToken_", "appId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (GetScenes.class) {
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

    public interface GetScenesOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    public final class SetDeviceProperties extends GeneratedMessageLite implements SetDevicePropertiesOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final SetDeviceProperties DEFAULT_INSTANCE;
        public static final int ISSORT_FIELD_NUMBER = 5;
        private static volatile Parser PARSER = null;
        public static final int PROPERTYBODY_FIELD_NUMBER = 4;
        public static final int SERVICETOKEN_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private boolean isSort_;
        private String propertyBody_;
        private String serviceToken_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements SetDevicePropertiesOrBuilder {
            private Builder() {
                super(SetDeviceProperties.DEFAULT_INSTANCE);
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

            public Builder clearIsSort() {
                copyOnWrite();
                this.instance.clearIsSort();
                return this;
            }

            public Builder clearPropertyBody() {
                copyOnWrite();
                this.instance.clearPropertyBody();
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

            public boolean getIsSort() {
                return this.instance.getIsSort();
            }

            public String getPropertyBody() {
                return this.instance.getPropertyBody();
            }

            public ByteString getPropertyBodyBytes() {
                return this.instance.getPropertyBodyBytes();
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

            public Builder setIsSort(boolean z) {
                copyOnWrite();
                this.instance.setIsSort(z);
                return this;
            }

            public Builder setPropertyBody(String str) {
                copyOnWrite();
                this.instance.setPropertyBody(str);
                return this;
            }

            public Builder setPropertyBodyBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setPropertyBodyBytes(byteString);
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
            SetDeviceProperties setDeviceProperties = new SetDeviceProperties();
            DEFAULT_INSTANCE = setDeviceProperties;
            GeneratedMessageLite.registerDefaultInstance(SetDeviceProperties.class, setDeviceProperties);
        }

        private SetDeviceProperties() {
            String str = "";
            this.serviceToken_ = str;
            this.appId_ = str;
            this.propertyBody_ = str;
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
        public void clearIsSort() {
            this.isSort_ = false;
        }

        /* access modifiers changed from: private */
        public void clearPropertyBody() {
            this.propertyBody_ = getDefaultInstance().getPropertyBody();
        }

        /* access modifiers changed from: private */
        public void clearServiceToken() {
            this.serviceToken_ = getDefaultInstance().getServiceToken();
        }

        public static SetDeviceProperties getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(SetDeviceProperties setDeviceProperties) {
            return DEFAULT_INSTANCE.createBuilder(setDeviceProperties);
        }

        public static SetDeviceProperties parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SetDeviceProperties parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SetDeviceProperties parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static SetDeviceProperties parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static SetDeviceProperties parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static SetDeviceProperties parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static SetDeviceProperties parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SetDeviceProperties parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SetDeviceProperties parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static SetDeviceProperties parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static SetDeviceProperties parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static SetDeviceProperties parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setIsSort(boolean z) {
            this.isSort_ = z;
        }

        /* access modifiers changed from: private */
        public void setPropertyBody(String str) {
            if (str != null) {
                this.propertyBody_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setPropertyBodyBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.propertyBody_ = byteString.toStringUtf8();
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
                    return new SetDeviceProperties();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0005\u0000\u0000\u0001\u0005\u0005\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ\u0004Ȉ\u0005\u0007", new Object[]{"aid_", "serviceToken_", "appId_", "propertyBody_", "isSort_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (SetDeviceProperties.class) {
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

        public boolean getIsSort() {
            return this.isSort_;
        }

        public String getPropertyBody() {
            return this.propertyBody_;
        }

        public ByteString getPropertyBodyBytes() {
            return ByteString.copyFromUtf8(this.propertyBody_);
        }

        public String getServiceToken() {
            return this.serviceToken_;
        }

        public ByteString getServiceTokenBytes() {
            return ByteString.copyFromUtf8(this.serviceToken_);
        }
    }

    public interface SetDevicePropertiesOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        boolean getIsSort();

        String getPropertyBody();

        ByteString getPropertyBodyBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();
    }

    public final class SetToken extends GeneratedMessageLite implements SetTokenOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final SetToken DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int TOKENPARAMS_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String tokenParams_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements SetTokenOrBuilder {
            private Builder() {
                super(SetToken.DEFAULT_INSTANCE);
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

            public Builder clearTokenParams() {
                copyOnWrite();
                this.instance.clearTokenParams();
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

            public String getTokenParams() {
                return this.instance.getTokenParams();
            }

            public ByteString getTokenParamsBytes() {
                return this.instance.getTokenParamsBytes();
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

            public Builder setTokenParams(String str) {
                copyOnWrite();
                this.instance.setTokenParams(str);
                return this;
            }

            public Builder setTokenParamsBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setTokenParamsBytes(byteString);
                return this;
            }
        }

        static {
            SetToken setToken = new SetToken();
            DEFAULT_INSTANCE = setToken;
            GeneratedMessageLite.registerDefaultInstance(SetToken.class, setToken);
        }

        private SetToken() {
            String str = "";
            this.tokenParams_ = str;
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
        public void clearTokenParams() {
            this.tokenParams_ = getDefaultInstance().getTokenParams();
        }

        public static SetToken getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(SetToken setToken) {
            return DEFAULT_INSTANCE.createBuilder(setToken);
        }

        public static SetToken parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SetToken parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SetToken parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static SetToken parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static SetToken parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static SetToken parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static SetToken parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SetToken parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SetToken parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static SetToken parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static SetToken parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static SetToken parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setTokenParams(String str) {
            if (str != null) {
                this.tokenParams_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setTokenParamsBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.tokenParams_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new SetToken();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ", new Object[]{"aid_", "tokenParams_", "appId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (SetToken.class) {
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

        public String getTokenParams() {
            return this.tokenParams_;
        }

        public ByteString getTokenParamsBytes() {
            return ByteString.copyFromUtf8(this.tokenParams_);
        }
    }

    public interface SetTokenOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getTokenParams();

        ByteString getTokenParamsBytes();
    }

    public final class StopToken extends GeneratedMessageLite implements StopTokenOrBuilder {
        public static final int AID_FIELD_NUMBER = 1;
        public static final int APPID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final StopToken DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int TOKENPARAMS_FIELD_NUMBER = 2;
        private int aid_;
        private String appId_;
        private String tokenParams_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements StopTokenOrBuilder {
            private Builder() {
                super(StopToken.DEFAULT_INSTANCE);
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

            public Builder clearTokenParams() {
                copyOnWrite();
                this.instance.clearTokenParams();
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

            public String getTokenParams() {
                return this.instance.getTokenParams();
            }

            public ByteString getTokenParamsBytes() {
                return this.instance.getTokenParamsBytes();
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

            public Builder setTokenParams(String str) {
                copyOnWrite();
                this.instance.setTokenParams(str);
                return this;
            }

            public Builder setTokenParamsBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setTokenParamsBytes(byteString);
                return this;
            }
        }

        static {
            StopToken stopToken = new StopToken();
            DEFAULT_INSTANCE = stopToken;
            GeneratedMessageLite.registerDefaultInstance(StopToken.class, stopToken);
        }

        private StopToken() {
            String str = "";
            this.tokenParams_ = str;
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
        public void clearTokenParams() {
            this.tokenParams_ = getDefaultInstance().getTokenParams();
        }

        public static StopToken getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(StopToken stopToken) {
            return DEFAULT_INSTANCE.createBuilder(stopToken);
        }

        public static StopToken parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static StopToken parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static StopToken parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static StopToken parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static StopToken parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static StopToken parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static StopToken parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static StopToken parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static StopToken parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static StopToken parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static StopToken parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static StopToken parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setTokenParams(String str) {
            if (str != null) {
                this.tokenParams_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setTokenParamsBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.tokenParams_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new StopToken();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ", new Object[]{"aid_", "tokenParams_", "appId_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (StopToken.class) {
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

        public String getTokenParams() {
            return this.tokenParams_;
        }

        public ByteString getTokenParamsBytes() {
            return ByteString.copyFromUtf8(this.tokenParams_);
        }
    }

    public interface StopTokenOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getAppId();

        ByteString getAppIdBytes();

        String getTokenParams();

        ByteString getTokenParamsBytes();
    }

    private ActionsProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }
}

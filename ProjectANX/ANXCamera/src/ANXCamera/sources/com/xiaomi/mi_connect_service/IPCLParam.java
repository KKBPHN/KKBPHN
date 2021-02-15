package com.xiaomi.mi_connect_service;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.GeneratedMessageLite.DefaultInstanceBasedParser;
import com.google.protobuf.GeneratedMessageLite.MethodToInvoke;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.Internal.EnumLiteMap;
import com.google.protobuf.Internal.EnumVerifier;
import com.google.protobuf.Internal.ProtobufList;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse;
import com.xiaomi.idm.api.proto.IDMServiceProto.IDMService;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public final class IPCLParam {

    /* renamed from: com.xiaomi.mi_connect_service.IPCLParam$1 reason: invalid class name */
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

    public final class ConnParam extends GeneratedMessageLite implements ConnParamOrBuilder {
        public static final int CONFIG_FIELD_NUMBER = 15;
        public static final int CONNTYPE_FIELD_NUMBER = 1;
        /* access modifiers changed from: private */
        public static final ConnParam DEFAULT_INSTANCE;
        public static final int ERRCODE_FIELD_NUMBER = 2;
        public static final int ERRMSG_FIELD_NUMBER = 3;
        private static volatile Parser PARSER;
        private ByteString config_ = ByteString.EMPTY;
        private int connType_;
        private int errCode_;
        private String errMsg_ = "";

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements ConnParamOrBuilder {
            private Builder() {
                super(ConnParam.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearConfig() {
                copyOnWrite();
                this.instance.clearConfig();
                return this;
            }

            public Builder clearConnType() {
                copyOnWrite();
                this.instance.clearConnType();
                return this;
            }

            public Builder clearErrCode() {
                copyOnWrite();
                this.instance.clearErrCode();
                return this;
            }

            public Builder clearErrMsg() {
                copyOnWrite();
                this.instance.clearErrMsg();
                return this;
            }

            public ByteString getConfig() {
                return this.instance.getConfig();
            }

            public ConnType getConnType() {
                return this.instance.getConnType();
            }

            public int getConnTypeValue() {
                return this.instance.getConnTypeValue();
            }

            public int getErrCode() {
                return this.instance.getErrCode();
            }

            public String getErrMsg() {
                return this.instance.getErrMsg();
            }

            public ByteString getErrMsgBytes() {
                return this.instance.getErrMsgBytes();
            }

            public Builder setConfig(ByteString byteString) {
                copyOnWrite();
                this.instance.setConfig(byteString);
                return this;
            }

            public Builder setConnType(ConnType connType) {
                copyOnWrite();
                this.instance.setConnType(connType);
                return this;
            }

            public Builder setConnTypeValue(int i) {
                copyOnWrite();
                this.instance.setConnTypeValue(i);
                return this;
            }

            public Builder setErrCode(int i) {
                copyOnWrite();
                this.instance.setErrCode(i);
                return this;
            }

            public Builder setErrMsg(String str) {
                copyOnWrite();
                this.instance.setErrMsg(str);
                return this;
            }

            public Builder setErrMsgBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setErrMsgBytes(byteString);
                return this;
            }
        }

        public enum ConnType implements EnumLite {
            WIFI_P2P_GO(0),
            WIFI_P2P_GC(1),
            WIFI_SOFTAP(2),
            WIFI_STATION(3),
            UNRECOGNIZED(-1);
            
            public static final int WIFI_P2P_GC_VALUE = 1;
            public static final int WIFI_P2P_GO_VALUE = 0;
            public static final int WIFI_SOFTAP_VALUE = 2;
            public static final int WIFI_STATION_VALUE = 3;
            private static final EnumLiteMap internalValueMap = null;
            private final int value;

            final class ConnTypeVerifier implements EnumVerifier {
                static final EnumVerifier INSTANCE = null;

                static {
                    INSTANCE = new ConnTypeVerifier();
                }

                private ConnTypeVerifier() {
                }

                public boolean isInRange(int i) {
                    return ConnType.forNumber(i) != null;
                }
            }

            static {
                internalValueMap = new EnumLiteMap() {
                    public ConnType findValueByNumber(int i) {
                        return ConnType.forNumber(i);
                    }
                };
            }

            private ConnType(int i) {
                this.value = i;
            }

            public static ConnType forNumber(int i) {
                if (i == 0) {
                    return WIFI_P2P_GO;
                }
                if (i == 1) {
                    return WIFI_P2P_GC;
                }
                if (i == 2) {
                    return WIFI_SOFTAP;
                }
                if (i != 3) {
                    return null;
                }
                return WIFI_STATION;
            }

            public static EnumLiteMap internalGetValueMap() {
                return internalValueMap;
            }

            public static EnumVerifier internalGetVerifier() {
                return ConnTypeVerifier.INSTANCE;
            }

            @Deprecated
            public static ConnType valueOf(int i) {
                return forNumber(i);
            }

            public final int getNumber() {
                if (this != UNRECOGNIZED) {
                    return this.value;
                }
                throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
            }
        }

        static {
            ConnParam connParam = new ConnParam();
            DEFAULT_INSTANCE = connParam;
            GeneratedMessageLite.registerDefaultInstance(ConnParam.class, connParam);
        }

        private ConnParam() {
        }

        /* access modifiers changed from: private */
        public void clearConfig() {
            this.config_ = getDefaultInstance().getConfig();
        }

        /* access modifiers changed from: private */
        public void clearConnType() {
            this.connType_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearErrCode() {
            this.errCode_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearErrMsg() {
            this.errMsg_ = getDefaultInstance().getErrMsg();
        }

        public static ConnParam getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(ConnParam connParam) {
            return DEFAULT_INSTANCE.createBuilder(connParam);
        }

        public static ConnParam parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ConnParam parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ConnParam parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static ConnParam parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static ConnParam parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static ConnParam parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static ConnParam parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ConnParam parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ConnParam parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static ConnParam parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static ConnParam parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static ConnParam parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setConfig(ByteString byteString) {
            if (byteString != null) {
                this.config_ = byteString;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setConnType(ConnType connType) {
            if (connType != null) {
                this.connType_ = connType.getNumber();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setConnTypeValue(int i) {
            this.connType_ = i;
        }

        /* access modifiers changed from: private */
        public void setErrCode(int i) {
            this.errCode_ = i;
        }

        /* access modifiers changed from: private */
        public void setErrMsg(String str) {
            if (str != null) {
                this.errMsg_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setErrMsgBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.errMsg_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new ConnParam();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0004\u0000\u0000\u0001\u000f\u0004\u0000\u0000\u0000\u0001\f\u0002\u0004\u0003Ȉ\u000f\n", new Object[]{"connType_", "errCode_", "errMsg_", "config_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (ConnParam.class) {
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

        public ByteString getConfig() {
            return this.config_;
        }

        public ConnType getConnType() {
            ConnType forNumber = ConnType.forNumber(this.connType_);
            return forNumber == null ? ConnType.UNRECOGNIZED : forNumber;
        }

        public int getConnTypeValue() {
            return this.connType_;
        }

        public int getErrCode() {
            return this.errCode_;
        }

        public String getErrMsg() {
            return this.errMsg_;
        }

        public ByteString getErrMsgBytes() {
            return ByteString.copyFromUtf8(this.errMsg_);
        }
    }

    public interface ConnParamOrBuilder extends MessageLiteOrBuilder {
        ByteString getConfig();

        ConnType getConnType();

        int getConnTypeValue();

        int getErrCode();

        String getErrMsg();

        ByteString getErrMsgBytes();
    }

    public final class ConnectService extends GeneratedMessageLite implements ConnectServiceOrBuilder {
        /* access modifiers changed from: private */
        public static final ConnectService DEFAULT_INSTANCE;
        public static final int IDMSERVICE_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMService idmService_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements ConnectServiceOrBuilder {
            private Builder() {
                super(ConnectService.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmService() {
                copyOnWrite();
                this.instance.clearIdmService();
                return this;
            }

            public IDMService getIdmService() {
                return this.instance.getIdmService();
            }

            public boolean hasIdmService() {
                return this.instance.hasIdmService();
            }

            public Builder mergeIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.mergeIdmService(iDMService);
                return this;
            }

            public Builder setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
                copyOnWrite();
                this.instance.setIdmService(builder);
                return this;
            }

            public Builder setIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.setIdmService(iDMService);
                return this;
            }
        }

        static {
            ConnectService connectService = new ConnectService();
            DEFAULT_INSTANCE = connectService;
            GeneratedMessageLite.registerDefaultInstance(ConnectService.class, connectService);
        }

        private ConnectService() {
        }

        /* access modifiers changed from: private */
        public void clearIdmService() {
            this.idmService_ = null;
        }

        public static ConnectService getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmService(IDMService iDMService) {
            if (iDMService != null) {
                IDMService iDMService2 = this.idmService_;
                if (!(iDMService2 == null || iDMService2 == IDMService.getDefaultInstance())) {
                    iDMService = (IDMService) IDMService.newBuilder(this.idmService_).mergeFrom(iDMService).buildPartial();
                }
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(ConnectService connectService) {
            return DEFAULT_INSTANCE.createBuilder(connectService);
        }

        public static ConnectService parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ConnectService parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ConnectService parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static ConnectService parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static ConnectService parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static ConnectService parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static ConnectService parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static ConnectService parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static ConnectService parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static ConnectService parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static ConnectService parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static ConnectService parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
            this.idmService_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmService(IDMService iDMService) {
            if (iDMService != null) {
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new ConnectService();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmService_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (ConnectService.class) {
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

        public IDMService getIdmService() {
            IDMService iDMService = this.idmService_;
            return iDMService == null ? IDMService.getDefaultInstance() : iDMService;
        }

        public boolean hasIdmService() {
            return this.idmService_ != null;
        }
    }

    public interface ConnectServiceOrBuilder extends MessageLiteOrBuilder {
        IDMService getIdmService();

        boolean hasIdmService();
    }

    public final class Event extends GeneratedMessageLite implements EventOrBuilder {
        /* access modifiers changed from: private */
        public static final Event DEFAULT_INSTANCE;
        public static final int IDMEVENT_FIELD_NUMBER = 2;
        public static final int IDMSERVICE_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMEvent idmEvent_;
        private IDMService idmService_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements EventOrBuilder {
            private Builder() {
                super(Event.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmEvent() {
                copyOnWrite();
                this.instance.clearIdmEvent();
                return this;
            }

            public Builder clearIdmService() {
                copyOnWrite();
                this.instance.clearIdmService();
                return this;
            }

            public IDMEvent getIdmEvent() {
                return this.instance.getIdmEvent();
            }

            public IDMService getIdmService() {
                return this.instance.getIdmService();
            }

            public boolean hasIdmEvent() {
                return this.instance.hasIdmEvent();
            }

            public boolean hasIdmService() {
                return this.instance.hasIdmService();
            }

            public Builder mergeIdmEvent(IDMEvent iDMEvent) {
                copyOnWrite();
                this.instance.mergeIdmEvent(iDMEvent);
                return this;
            }

            public Builder mergeIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.mergeIdmService(iDMService);
                return this;
            }

            public Builder setIdmEvent(com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent.Builder builder) {
                copyOnWrite();
                this.instance.setIdmEvent(builder);
                return this;
            }

            public Builder setIdmEvent(IDMEvent iDMEvent) {
                copyOnWrite();
                this.instance.setIdmEvent(iDMEvent);
                return this;
            }

            public Builder setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
                copyOnWrite();
                this.instance.setIdmService(builder);
                return this;
            }

            public Builder setIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.setIdmService(iDMService);
                return this;
            }
        }

        static {
            Event event = new Event();
            DEFAULT_INSTANCE = event;
            GeneratedMessageLite.registerDefaultInstance(Event.class, event);
        }

        private Event() {
        }

        /* access modifiers changed from: private */
        public void clearIdmEvent() {
            this.idmEvent_ = null;
        }

        /* access modifiers changed from: private */
        public void clearIdmService() {
            this.idmService_ = null;
        }

        public static Event getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmEvent(IDMEvent iDMEvent) {
            if (iDMEvent != null) {
                IDMEvent iDMEvent2 = this.idmEvent_;
                if (!(iDMEvent2 == null || iDMEvent2 == IDMEvent.getDefaultInstance())) {
                    iDMEvent = (IDMEvent) IDMEvent.newBuilder(this.idmEvent_).mergeFrom(iDMEvent).buildPartial();
                }
                this.idmEvent_ = iDMEvent;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void mergeIdmService(IDMService iDMService) {
            if (iDMService != null) {
                IDMService iDMService2 = this.idmService_;
                if (!(iDMService2 == null || iDMService2 == IDMService.getDefaultInstance())) {
                    iDMService = (IDMService) IDMService.newBuilder(this.idmService_).mergeFrom(iDMService).buildPartial();
                }
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(Event event) {
            return DEFAULT_INSTANCE.createBuilder(event);
        }

        public static Event parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static Event parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static Event parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static Event parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static Event parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static Event parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Event parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static Event parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static Event parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static Event parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static Event parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static Event parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmEvent(com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent.Builder builder) {
            this.idmEvent_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmEvent(IDMEvent iDMEvent) {
            if (iDMEvent != null) {
                this.idmEvent_ = iDMEvent;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
            this.idmService_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmService(IDMService iDMService) {
            if (iDMService != null) {
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new Event();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\t\u0002\t", new Object[]{"idmService_", "idmEvent_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (Event.class) {
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

        public IDMEvent getIdmEvent() {
            IDMEvent iDMEvent = this.idmEvent_;
            return iDMEvent == null ? IDMEvent.getDefaultInstance() : iDMEvent;
        }

        public IDMService getIdmService() {
            IDMService iDMService = this.idmService_;
            return iDMService == null ? IDMService.getDefaultInstance() : iDMService;
        }

        public boolean hasIdmEvent() {
            return this.idmEvent_ != null;
        }

        public boolean hasIdmService() {
            return this.idmService_ != null;
        }
    }

    public interface EventOrBuilder extends MessageLiteOrBuilder {
        IDMEvent getIdmEvent();

        IDMService getIdmService();

        boolean hasIdmEvent();

        boolean hasIdmService();
    }

    public final class IdentifyParam extends GeneratedMessageLite implements IdentifyParamOrBuilder {
        public static final int CUSERID_FIELD_NUMBER = 3;
        /* access modifiers changed from: private */
        public static final IdentifyParam DEFAULT_INSTANCE;
        public static final int DOMAIN_FIELD_NUMBER = 7;
        private static volatile Parser PARSER = null;
        public static final int SERVICETOKEN_FIELD_NUMBER = 4;
        public static final int SID_FIELD_NUMBER = 2;
        public static final int SSECURITY_FIELD_NUMBER = 5;
        public static final int TIMEDIFF_FIELD_NUMBER = 6;
        public static final int USERID_FIELD_NUMBER = 1;
        private String cUserId_;
        private String domain_;
        private String serviceToken_;
        private String sid_;
        private String ssecurity_;
        private String timeDiff_;
        private String userId_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements IdentifyParamOrBuilder {
            private Builder() {
                super(IdentifyParam.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearCUserId() {
                copyOnWrite();
                this.instance.clearCUserId();
                return this;
            }

            public Builder clearDomain() {
                copyOnWrite();
                this.instance.clearDomain();
                return this;
            }

            public Builder clearServiceToken() {
                copyOnWrite();
                this.instance.clearServiceToken();
                return this;
            }

            public Builder clearSid() {
                copyOnWrite();
                this.instance.clearSid();
                return this;
            }

            public Builder clearSsecurity() {
                copyOnWrite();
                this.instance.clearSsecurity();
                return this;
            }

            public Builder clearTimeDiff() {
                copyOnWrite();
                this.instance.clearTimeDiff();
                return this;
            }

            public Builder clearUserId() {
                copyOnWrite();
                this.instance.clearUserId();
                return this;
            }

            public String getCUserId() {
                return this.instance.getCUserId();
            }

            public ByteString getCUserIdBytes() {
                return this.instance.getCUserIdBytes();
            }

            public String getDomain() {
                return this.instance.getDomain();
            }

            public ByteString getDomainBytes() {
                return this.instance.getDomainBytes();
            }

            public String getServiceToken() {
                return this.instance.getServiceToken();
            }

            public ByteString getServiceTokenBytes() {
                return this.instance.getServiceTokenBytes();
            }

            public String getSid() {
                return this.instance.getSid();
            }

            public ByteString getSidBytes() {
                return this.instance.getSidBytes();
            }

            public String getSsecurity() {
                return this.instance.getSsecurity();
            }

            public ByteString getSsecurityBytes() {
                return this.instance.getSsecurityBytes();
            }

            public String getTimeDiff() {
                return this.instance.getTimeDiff();
            }

            public ByteString getTimeDiffBytes() {
                return this.instance.getTimeDiffBytes();
            }

            public String getUserId() {
                return this.instance.getUserId();
            }

            public ByteString getUserIdBytes() {
                return this.instance.getUserIdBytes();
            }

            public Builder setCUserId(String str) {
                copyOnWrite();
                this.instance.setCUserId(str);
                return this;
            }

            public Builder setCUserIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setCUserIdBytes(byteString);
                return this;
            }

            public Builder setDomain(String str) {
                copyOnWrite();
                this.instance.setDomain(str);
                return this;
            }

            public Builder setDomainBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setDomainBytes(byteString);
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

            public Builder setSid(String str) {
                copyOnWrite();
                this.instance.setSid(str);
                return this;
            }

            public Builder setSidBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setSidBytes(byteString);
                return this;
            }

            public Builder setSsecurity(String str) {
                copyOnWrite();
                this.instance.setSsecurity(str);
                return this;
            }

            public Builder setSsecurityBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setSsecurityBytes(byteString);
                return this;
            }

            public Builder setTimeDiff(String str) {
                copyOnWrite();
                this.instance.setTimeDiff(str);
                return this;
            }

            public Builder setTimeDiffBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setTimeDiffBytes(byteString);
                return this;
            }

            public Builder setUserId(String str) {
                copyOnWrite();
                this.instance.setUserId(str);
                return this;
            }

            public Builder setUserIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setUserIdBytes(byteString);
                return this;
            }
        }

        static {
            IdentifyParam identifyParam = new IdentifyParam();
            DEFAULT_INSTANCE = identifyParam;
            GeneratedMessageLite.registerDefaultInstance(IdentifyParam.class, identifyParam);
        }

        private IdentifyParam() {
            String str = "";
            this.userId_ = str;
            this.sid_ = str;
            this.cUserId_ = str;
            this.serviceToken_ = str;
            this.ssecurity_ = str;
            this.timeDiff_ = str;
            this.domain_ = str;
        }

        /* access modifiers changed from: private */
        public void clearCUserId() {
            this.cUserId_ = getDefaultInstance().getCUserId();
        }

        /* access modifiers changed from: private */
        public void clearDomain() {
            this.domain_ = getDefaultInstance().getDomain();
        }

        /* access modifiers changed from: private */
        public void clearServiceToken() {
            this.serviceToken_ = getDefaultInstance().getServiceToken();
        }

        /* access modifiers changed from: private */
        public void clearSid() {
            this.sid_ = getDefaultInstance().getSid();
        }

        /* access modifiers changed from: private */
        public void clearSsecurity() {
            this.ssecurity_ = getDefaultInstance().getSsecurity();
        }

        /* access modifiers changed from: private */
        public void clearTimeDiff() {
            this.timeDiff_ = getDefaultInstance().getTimeDiff();
        }

        /* access modifiers changed from: private */
        public void clearUserId() {
            this.userId_ = getDefaultInstance().getUserId();
        }

        public static IdentifyParam getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(IdentifyParam identifyParam) {
            return DEFAULT_INSTANCE.createBuilder(identifyParam);
        }

        public static IdentifyParam parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IdentifyParam parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IdentifyParam parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static IdentifyParam parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static IdentifyParam parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static IdentifyParam parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static IdentifyParam parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IdentifyParam parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IdentifyParam parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static IdentifyParam parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static IdentifyParam parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static IdentifyParam parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setCUserId(String str) {
            if (str != null) {
                this.cUserId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setCUserIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.cUserId_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setDomain(String str) {
            if (str != null) {
                this.domain_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setDomainBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.domain_ = byteString.toStringUtf8();
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

        /* access modifiers changed from: private */
        public void setSid(String str) {
            if (str != null) {
                this.sid_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setSidBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.sid_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setSsecurity(String str) {
            if (str != null) {
                this.ssecurity_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setSsecurityBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.ssecurity_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setTimeDiff(String str) {
            if (str != null) {
                this.timeDiff_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setTimeDiffBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.timeDiff_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUserId(String str) {
            if (str != null) {
                this.userId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUserIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.userId_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new IdentifyParam();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0007\u0000\u0000\u0001\u0007\u0007\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ\u0003Ȉ\u0004Ȉ\u0005Ȉ\u0006Ȉ\u0007Ȉ", new Object[]{"userId_", "sid_", "cUserId_", "serviceToken_", "ssecurity_", "timeDiff_", "domain_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (IdentifyParam.class) {
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

        public String getCUserId() {
            return this.cUserId_;
        }

        public ByteString getCUserIdBytes() {
            return ByteString.copyFromUtf8(this.cUserId_);
        }

        public String getDomain() {
            return this.domain_;
        }

        public ByteString getDomainBytes() {
            return ByteString.copyFromUtf8(this.domain_);
        }

        public String getServiceToken() {
            return this.serviceToken_;
        }

        public ByteString getServiceTokenBytes() {
            return ByteString.copyFromUtf8(this.serviceToken_);
        }

        public String getSid() {
            return this.sid_;
        }

        public ByteString getSidBytes() {
            return ByteString.copyFromUtf8(this.sid_);
        }

        public String getSsecurity() {
            return this.ssecurity_;
        }

        public ByteString getSsecurityBytes() {
            return ByteString.copyFromUtf8(this.ssecurity_);
        }

        public String getTimeDiff() {
            return this.timeDiff_;
        }

        public ByteString getTimeDiffBytes() {
            return ByteString.copyFromUtf8(this.timeDiff_);
        }

        public String getUserId() {
            return this.userId_;
        }

        public ByteString getUserIdBytes() {
            return ByteString.copyFromUtf8(this.userId_);
        }
    }

    public interface IdentifyParamOrBuilder extends MessageLiteOrBuilder {
        String getCUserId();

        ByteString getCUserIdBytes();

        String getDomain();

        ByteString getDomainBytes();

        String getServiceToken();

        ByteString getServiceTokenBytes();

        String getSid();

        ByteString getSidBytes();

        String getSsecurity();

        ByteString getSsecurityBytes();

        String getTimeDiff();

        ByteString getTimeDiffBytes();

        String getUserId();

        ByteString getUserIdBytes();
    }

    public final class OnEvent extends GeneratedMessageLite implements OnEventOrBuilder {
        /* access modifiers changed from: private */
        public static final OnEvent DEFAULT_INSTANCE;
        public static final int IDMEVENT_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMEvent idmEvent_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements OnEventOrBuilder {
            private Builder() {
                super(OnEvent.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmEvent() {
                copyOnWrite();
                this.instance.clearIdmEvent();
                return this;
            }

            public IDMEvent getIdmEvent() {
                return this.instance.getIdmEvent();
            }

            public boolean hasIdmEvent() {
                return this.instance.hasIdmEvent();
            }

            public Builder mergeIdmEvent(IDMEvent iDMEvent) {
                copyOnWrite();
                this.instance.mergeIdmEvent(iDMEvent);
                return this;
            }

            public Builder setIdmEvent(com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent.Builder builder) {
                copyOnWrite();
                this.instance.setIdmEvent(builder);
                return this;
            }

            public Builder setIdmEvent(IDMEvent iDMEvent) {
                copyOnWrite();
                this.instance.setIdmEvent(iDMEvent);
                return this;
            }
        }

        static {
            OnEvent onEvent = new OnEvent();
            DEFAULT_INSTANCE = onEvent;
            GeneratedMessageLite.registerDefaultInstance(OnEvent.class, onEvent);
        }

        private OnEvent() {
        }

        /* access modifiers changed from: private */
        public void clearIdmEvent() {
            this.idmEvent_ = null;
        }

        public static OnEvent getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmEvent(IDMEvent iDMEvent) {
            if (iDMEvent != null) {
                IDMEvent iDMEvent2 = this.idmEvent_;
                if (!(iDMEvent2 == null || iDMEvent2 == IDMEvent.getDefaultInstance())) {
                    iDMEvent = (IDMEvent) IDMEvent.newBuilder(this.idmEvent_).mergeFrom(iDMEvent).buildPartial();
                }
                this.idmEvent_ = iDMEvent;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(OnEvent onEvent) {
            return DEFAULT_INSTANCE.createBuilder(onEvent);
        }

        public static OnEvent parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnEvent parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnEvent parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static OnEvent parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static OnEvent parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static OnEvent parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static OnEvent parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnEvent parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnEvent parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static OnEvent parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static OnEvent parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static OnEvent parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmEvent(com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent.Builder builder) {
            this.idmEvent_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmEvent(IDMEvent iDMEvent) {
            if (iDMEvent != null) {
                this.idmEvent_ = iDMEvent;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new OnEvent();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmEvent_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (OnEvent.class) {
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

        public IDMEvent getIdmEvent() {
            IDMEvent iDMEvent = this.idmEvent_;
            return iDMEvent == null ? IDMEvent.getDefaultInstance() : iDMEvent;
        }

        public boolean hasIdmEvent() {
            return this.idmEvent_ != null;
        }
    }

    public interface OnEventOrBuilder extends MessageLiteOrBuilder {
        IDMEvent getIdmEvent();

        boolean hasIdmEvent();
    }

    public final class OnRequest extends GeneratedMessageLite implements OnRequestOrBuilder {
        /* access modifiers changed from: private */
        public static final OnRequest DEFAULT_INSTANCE;
        public static final int IDMREQUEST_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMRequest idmRequest_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements OnRequestOrBuilder {
            private Builder() {
                super(OnRequest.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmRequest() {
                copyOnWrite();
                this.instance.clearIdmRequest();
                return this;
            }

            public IDMRequest getIdmRequest() {
                return this.instance.getIdmRequest();
            }

            public boolean hasIdmRequest() {
                return this.instance.hasIdmRequest();
            }

            public Builder mergeIdmRequest(IDMRequest iDMRequest) {
                copyOnWrite();
                this.instance.mergeIdmRequest(iDMRequest);
                return this;
            }

            public Builder setIdmRequest(com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest.Builder builder) {
                copyOnWrite();
                this.instance.setIdmRequest(builder);
                return this;
            }

            public Builder setIdmRequest(IDMRequest iDMRequest) {
                copyOnWrite();
                this.instance.setIdmRequest(iDMRequest);
                return this;
            }
        }

        static {
            OnRequest onRequest = new OnRequest();
            DEFAULT_INSTANCE = onRequest;
            GeneratedMessageLite.registerDefaultInstance(OnRequest.class, onRequest);
        }

        private OnRequest() {
        }

        /* access modifiers changed from: private */
        public void clearIdmRequest() {
            this.idmRequest_ = null;
        }

        public static OnRequest getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmRequest(IDMRequest iDMRequest) {
            if (iDMRequest != null) {
                IDMRequest iDMRequest2 = this.idmRequest_;
                if (!(iDMRequest2 == null || iDMRequest2 == IDMRequest.getDefaultInstance())) {
                    iDMRequest = (IDMRequest) IDMRequest.newBuilder(this.idmRequest_).mergeFrom(iDMRequest).buildPartial();
                }
                this.idmRequest_ = iDMRequest;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(OnRequest onRequest) {
            return DEFAULT_INSTANCE.createBuilder(onRequest);
        }

        public static OnRequest parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnRequest parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnRequest parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static OnRequest parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static OnRequest parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static OnRequest parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static OnRequest parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnRequest parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnRequest parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static OnRequest parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static OnRequest parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static OnRequest parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmRequest(com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest.Builder builder) {
            this.idmRequest_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmRequest(IDMRequest iDMRequest) {
            if (iDMRequest != null) {
                this.idmRequest_ = iDMRequest;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new OnRequest();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmRequest_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (OnRequest.class) {
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

        public IDMRequest getIdmRequest() {
            IDMRequest iDMRequest = this.idmRequest_;
            return iDMRequest == null ? IDMRequest.getDefaultInstance() : iDMRequest;
        }

        public boolean hasIdmRequest() {
            return this.idmRequest_ != null;
        }
    }

    public interface OnRequestOrBuilder extends MessageLiteOrBuilder {
        IDMRequest getIdmRequest();

        boolean hasIdmRequest();
    }

    public final class OnResponse extends GeneratedMessageLite implements OnResponseOrBuilder {
        /* access modifiers changed from: private */
        public static final OnResponse DEFAULT_INSTANCE;
        public static final int IDMRESPONSE_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMResponse idmResponse_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements OnResponseOrBuilder {
            private Builder() {
                super(OnResponse.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmResponse() {
                copyOnWrite();
                this.instance.clearIdmResponse();
                return this;
            }

            public IDMResponse getIdmResponse() {
                return this.instance.getIdmResponse();
            }

            public boolean hasIdmResponse() {
                return this.instance.hasIdmResponse();
            }

            public Builder mergeIdmResponse(IDMResponse iDMResponse) {
                copyOnWrite();
                this.instance.mergeIdmResponse(iDMResponse);
                return this;
            }

            public Builder setIdmResponse(com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse.Builder builder) {
                copyOnWrite();
                this.instance.setIdmResponse(builder);
                return this;
            }

            public Builder setIdmResponse(IDMResponse iDMResponse) {
                copyOnWrite();
                this.instance.setIdmResponse(iDMResponse);
                return this;
            }
        }

        static {
            OnResponse onResponse = new OnResponse();
            DEFAULT_INSTANCE = onResponse;
            GeneratedMessageLite.registerDefaultInstance(OnResponse.class, onResponse);
        }

        private OnResponse() {
        }

        /* access modifiers changed from: private */
        public void clearIdmResponse() {
            this.idmResponse_ = null;
        }

        public static OnResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmResponse(IDMResponse iDMResponse) {
            if (iDMResponse != null) {
                IDMResponse iDMResponse2 = this.idmResponse_;
                if (!(iDMResponse2 == null || iDMResponse2 == IDMResponse.getDefaultInstance())) {
                    iDMResponse = (IDMResponse) IDMResponse.newBuilder(this.idmResponse_).mergeFrom(iDMResponse).buildPartial();
                }
                this.idmResponse_ = iDMResponse;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(OnResponse onResponse) {
            return DEFAULT_INSTANCE.createBuilder(onResponse);
        }

        public static OnResponse parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnResponse parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnResponse parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static OnResponse parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static OnResponse parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static OnResponse parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static OnResponse parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnResponse parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnResponse parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static OnResponse parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static OnResponse parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static OnResponse parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmResponse(com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse.Builder builder) {
            this.idmResponse_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmResponse(IDMResponse iDMResponse) {
            if (iDMResponse != null) {
                this.idmResponse_ = iDMResponse;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new OnResponse();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmResponse_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (OnResponse.class) {
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

        public IDMResponse getIdmResponse() {
            IDMResponse iDMResponse = this.idmResponse_;
            return iDMResponse == null ? IDMResponse.getDefaultInstance() : iDMResponse;
        }

        public boolean hasIdmResponse() {
            return this.idmResponse_ != null;
        }
    }

    public interface OnResponseOrBuilder extends MessageLiteOrBuilder {
        IDMResponse getIdmResponse();

        boolean hasIdmResponse();
    }

    public final class OnServiceConnectStatus extends GeneratedMessageLite implements OnServiceConnectStatusOrBuilder {
        public static final int CONNECTED_FIELD_NUMBER = 2;
        /* access modifiers changed from: private */
        public static final OnServiceConnectStatus DEFAULT_INSTANCE;
        public static final int IDMSERVICE_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private boolean connected_;
        private IDMService idmService_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements OnServiceConnectStatusOrBuilder {
            private Builder() {
                super(OnServiceConnectStatus.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearConnected() {
                copyOnWrite();
                this.instance.clearConnected();
                return this;
            }

            public Builder clearIdmService() {
                copyOnWrite();
                this.instance.clearIdmService();
                return this;
            }

            public boolean getConnected() {
                return this.instance.getConnected();
            }

            public IDMService getIdmService() {
                return this.instance.getIdmService();
            }

            public boolean hasIdmService() {
                return this.instance.hasIdmService();
            }

            public Builder mergeIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.mergeIdmService(iDMService);
                return this;
            }

            public Builder setConnected(boolean z) {
                copyOnWrite();
                this.instance.setConnected(z);
                return this;
            }

            public Builder setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
                copyOnWrite();
                this.instance.setIdmService(builder);
                return this;
            }

            public Builder setIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.setIdmService(iDMService);
                return this;
            }
        }

        static {
            OnServiceConnectStatus onServiceConnectStatus = new OnServiceConnectStatus();
            DEFAULT_INSTANCE = onServiceConnectStatus;
            GeneratedMessageLite.registerDefaultInstance(OnServiceConnectStatus.class, onServiceConnectStatus);
        }

        private OnServiceConnectStatus() {
        }

        /* access modifiers changed from: private */
        public void clearConnected() {
            this.connected_ = false;
        }

        /* access modifiers changed from: private */
        public void clearIdmService() {
            this.idmService_ = null;
        }

        public static OnServiceConnectStatus getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmService(IDMService iDMService) {
            if (iDMService != null) {
                IDMService iDMService2 = this.idmService_;
                if (!(iDMService2 == null || iDMService2 == IDMService.getDefaultInstance())) {
                    iDMService = (IDMService) IDMService.newBuilder(this.idmService_).mergeFrom(iDMService).buildPartial();
                }
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(OnServiceConnectStatus onServiceConnectStatus) {
            return DEFAULT_INSTANCE.createBuilder(onServiceConnectStatus);
        }

        public static OnServiceConnectStatus parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnServiceConnectStatus parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnServiceConnectStatus parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static OnServiceConnectStatus parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static OnServiceConnectStatus parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static OnServiceConnectStatus parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static OnServiceConnectStatus parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnServiceConnectStatus parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnServiceConnectStatus parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static OnServiceConnectStatus parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static OnServiceConnectStatus parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static OnServiceConnectStatus parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setConnected(boolean z) {
            this.connected_ = z;
        }

        /* access modifiers changed from: private */
        public void setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
            this.idmService_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmService(IDMService iDMService) {
            if (iDMService != null) {
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new OnServiceConnectStatus();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\t\u0002\u0007", new Object[]{"idmService_", "connected_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (OnServiceConnectStatus.class) {
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

        public boolean getConnected() {
            return this.connected_;
        }

        public IDMService getIdmService() {
            IDMService iDMService = this.idmService_;
            return iDMService == null ? IDMService.getDefaultInstance() : iDMService;
        }

        public boolean hasIdmService() {
            return this.idmService_ != null;
        }
    }

    public interface OnServiceConnectStatusOrBuilder extends MessageLiteOrBuilder {
        boolean getConnected();

        IDMService getIdmService();

        boolean hasIdmService();
    }

    public final class OnServiceFound extends GeneratedMessageLite implements OnServiceFoundOrBuilder {
        /* access modifiers changed from: private */
        public static final OnServiceFound DEFAULT_INSTANCE;
        public static final int IDMSERVICE_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMService idmService_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements OnServiceFoundOrBuilder {
            private Builder() {
                super(OnServiceFound.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmService() {
                copyOnWrite();
                this.instance.clearIdmService();
                return this;
            }

            public IDMService getIdmService() {
                return this.instance.getIdmService();
            }

            public boolean hasIdmService() {
                return this.instance.hasIdmService();
            }

            public Builder mergeIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.mergeIdmService(iDMService);
                return this;
            }

            public Builder setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
                copyOnWrite();
                this.instance.setIdmService(builder);
                return this;
            }

            public Builder setIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.setIdmService(iDMService);
                return this;
            }
        }

        static {
            OnServiceFound onServiceFound = new OnServiceFound();
            DEFAULT_INSTANCE = onServiceFound;
            GeneratedMessageLite.registerDefaultInstance(OnServiceFound.class, onServiceFound);
        }

        private OnServiceFound() {
        }

        /* access modifiers changed from: private */
        public void clearIdmService() {
            this.idmService_ = null;
        }

        public static OnServiceFound getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmService(IDMService iDMService) {
            if (iDMService != null) {
                IDMService iDMService2 = this.idmService_;
                if (!(iDMService2 == null || iDMService2 == IDMService.getDefaultInstance())) {
                    iDMService = (IDMService) IDMService.newBuilder(this.idmService_).mergeFrom(iDMService).buildPartial();
                }
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(OnServiceFound onServiceFound) {
            return DEFAULT_INSTANCE.createBuilder(onServiceFound);
        }

        public static OnServiceFound parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnServiceFound parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnServiceFound parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static OnServiceFound parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static OnServiceFound parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static OnServiceFound parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static OnServiceFound parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnServiceFound parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnServiceFound parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static OnServiceFound parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static OnServiceFound parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static OnServiceFound parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
            this.idmService_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmService(IDMService iDMService) {
            if (iDMService != null) {
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new OnServiceFound();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmService_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (OnServiceFound.class) {
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

        public IDMService getIdmService() {
            IDMService iDMService = this.idmService_;
            return iDMService == null ? IDMService.getDefaultInstance() : iDMService;
        }

        public boolean hasIdmService() {
            return this.idmService_ != null;
        }
    }

    public interface OnServiceFoundOrBuilder extends MessageLiteOrBuilder {
        IDMService getIdmService();

        boolean hasIdmService();
    }

    public final class OnSetEventCallback extends GeneratedMessageLite implements OnSetEventCallbackOrBuilder {
        /* access modifiers changed from: private */
        public static final OnSetEventCallback DEFAULT_INSTANCE;
        public static final int IDMEVENT_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMEvent idmEvent_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements OnSetEventCallbackOrBuilder {
            private Builder() {
                super(OnSetEventCallback.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmEvent() {
                copyOnWrite();
                this.instance.clearIdmEvent();
                return this;
            }

            public IDMEvent getIdmEvent() {
                return this.instance.getIdmEvent();
            }

            public boolean hasIdmEvent() {
                return this.instance.hasIdmEvent();
            }

            public Builder mergeIdmEvent(IDMEvent iDMEvent) {
                copyOnWrite();
                this.instance.mergeIdmEvent(iDMEvent);
                return this;
            }

            public Builder setIdmEvent(com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent.Builder builder) {
                copyOnWrite();
                this.instance.setIdmEvent(builder);
                return this;
            }

            public Builder setIdmEvent(IDMEvent iDMEvent) {
                copyOnWrite();
                this.instance.setIdmEvent(iDMEvent);
                return this;
            }
        }

        static {
            OnSetEventCallback onSetEventCallback = new OnSetEventCallback();
            DEFAULT_INSTANCE = onSetEventCallback;
            GeneratedMessageLite.registerDefaultInstance(OnSetEventCallback.class, onSetEventCallback);
        }

        private OnSetEventCallback() {
        }

        /* access modifiers changed from: private */
        public void clearIdmEvent() {
            this.idmEvent_ = null;
        }

        public static OnSetEventCallback getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmEvent(IDMEvent iDMEvent) {
            if (iDMEvent != null) {
                IDMEvent iDMEvent2 = this.idmEvent_;
                if (!(iDMEvent2 == null || iDMEvent2 == IDMEvent.getDefaultInstance())) {
                    iDMEvent = (IDMEvent) IDMEvent.newBuilder(this.idmEvent_).mergeFrom(iDMEvent).buildPartial();
                }
                this.idmEvent_ = iDMEvent;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(OnSetEventCallback onSetEventCallback) {
            return DEFAULT_INSTANCE.createBuilder(onSetEventCallback);
        }

        public static OnSetEventCallback parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnSetEventCallback parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnSetEventCallback parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static OnSetEventCallback parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static OnSetEventCallback parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static OnSetEventCallback parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static OnSetEventCallback parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static OnSetEventCallback parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static OnSetEventCallback parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static OnSetEventCallback parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static OnSetEventCallback parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static OnSetEventCallback parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmEvent(com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent.Builder builder) {
            this.idmEvent_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmEvent(IDMEvent iDMEvent) {
            if (iDMEvent != null) {
                this.idmEvent_ = iDMEvent;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new OnSetEventCallback();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmEvent_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (OnSetEventCallback.class) {
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

        public IDMEvent getIdmEvent() {
            IDMEvent iDMEvent = this.idmEvent_;
            return iDMEvent == null ? IDMEvent.getDefaultInstance() : iDMEvent;
        }

        public boolean hasIdmEvent() {
            return this.idmEvent_ != null;
        }
    }

    public interface OnSetEventCallbackOrBuilder extends MessageLiteOrBuilder {
        IDMEvent getIdmEvent();

        boolean hasIdmEvent();
    }

    public final class RegisterIDMClient extends GeneratedMessageLite implements RegisterIDMClientOrBuilder {
        /* access modifiers changed from: private */
        public static final RegisterIDMClient DEFAULT_INSTANCE;
        private static volatile Parser PARSER;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements RegisterIDMClientOrBuilder {
            private Builder() {
                super(RegisterIDMClient.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }
        }

        static {
            RegisterIDMClient registerIDMClient = new RegisterIDMClient();
            DEFAULT_INSTANCE = registerIDMClient;
            GeneratedMessageLite.registerDefaultInstance(RegisterIDMClient.class, registerIDMClient);
        }

        private RegisterIDMClient() {
        }

        public static RegisterIDMClient getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(RegisterIDMClient registerIDMClient) {
            return DEFAULT_INSTANCE.createBuilder(registerIDMClient);
        }

        public static RegisterIDMClient parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static RegisterIDMClient parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static RegisterIDMClient parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static RegisterIDMClient parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static RegisterIDMClient parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static RegisterIDMClient parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static RegisterIDMClient parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static RegisterIDMClient parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static RegisterIDMClient parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static RegisterIDMClient parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static RegisterIDMClient parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static RegisterIDMClient parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new RegisterIDMClient();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0000", null);
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (RegisterIDMClient.class) {
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
    }

    public interface RegisterIDMClientOrBuilder extends MessageLiteOrBuilder {
    }

    public final class RegisterProc extends GeneratedMessageLite implements RegisterProcOrBuilder {
        /* access modifiers changed from: private */
        public static final RegisterProc DEFAULT_INSTANCE;
        private static volatile Parser PARSER;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements RegisterProcOrBuilder {
            private Builder() {
                super(RegisterProc.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }
        }

        static {
            RegisterProc registerProc = new RegisterProc();
            DEFAULT_INSTANCE = registerProc;
            GeneratedMessageLite.registerDefaultInstance(RegisterProc.class, registerProc);
        }

        private RegisterProc() {
        }

        public static RegisterProc getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(RegisterProc registerProc) {
            return DEFAULT_INSTANCE.createBuilder(registerProc);
        }

        public static RegisterProc parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static RegisterProc parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static RegisterProc parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static RegisterProc parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static RegisterProc parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static RegisterProc parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static RegisterProc parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static RegisterProc parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static RegisterProc parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static RegisterProc parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static RegisterProc parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static RegisterProc parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new RegisterProc();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0000", null);
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (RegisterProc.class) {
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
    }

    public interface RegisterProcOrBuilder extends MessageLiteOrBuilder {
    }

    public final class Request extends GeneratedMessageLite implements RequestOrBuilder {
        /* access modifiers changed from: private */
        public static final Request DEFAULT_INSTANCE;
        public static final int IDMREQUEST_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMRequest idmRequest_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements RequestOrBuilder {
            private Builder() {
                super(Request.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmRequest() {
                copyOnWrite();
                this.instance.clearIdmRequest();
                return this;
            }

            public IDMRequest getIdmRequest() {
                return this.instance.getIdmRequest();
            }

            public boolean hasIdmRequest() {
                return this.instance.hasIdmRequest();
            }

            public Builder mergeIdmRequest(IDMRequest iDMRequest) {
                copyOnWrite();
                this.instance.mergeIdmRequest(iDMRequest);
                return this;
            }

            public Builder setIdmRequest(com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest.Builder builder) {
                copyOnWrite();
                this.instance.setIdmRequest(builder);
                return this;
            }

            public Builder setIdmRequest(IDMRequest iDMRequest) {
                copyOnWrite();
                this.instance.setIdmRequest(iDMRequest);
                return this;
            }
        }

        static {
            Request request = new Request();
            DEFAULT_INSTANCE = request;
            GeneratedMessageLite.registerDefaultInstance(Request.class, request);
        }

        private Request() {
        }

        /* access modifiers changed from: private */
        public void clearIdmRequest() {
            this.idmRequest_ = null;
        }

        public static Request getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmRequest(IDMRequest iDMRequest) {
            if (iDMRequest != null) {
                IDMRequest iDMRequest2 = this.idmRequest_;
                if (!(iDMRequest2 == null || iDMRequest2 == IDMRequest.getDefaultInstance())) {
                    iDMRequest = (IDMRequest) IDMRequest.newBuilder(this.idmRequest_).mergeFrom(iDMRequest).buildPartial();
                }
                this.idmRequest_ = iDMRequest;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(Request request) {
            return DEFAULT_INSTANCE.createBuilder(request);
        }

        public static Request parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static Request parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static Request parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static Request parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static Request parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static Request parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static Request parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static Request parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static Request parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static Request parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static Request parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static Request parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmRequest(com.xiaomi.idm.api.proto.IDMServiceProto.IDMRequest.Builder builder) {
            this.idmRequest_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmRequest(IDMRequest iDMRequest) {
            if (iDMRequest != null) {
                this.idmRequest_ = iDMRequest;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new Request();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmRequest_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (Request.class) {
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

        public IDMRequest getIdmRequest() {
            IDMRequest iDMRequest = this.idmRequest_;
            return iDMRequest == null ? IDMRequest.getDefaultInstance() : iDMRequest;
        }

        public boolean hasIdmRequest() {
            return this.idmRequest_ != null;
        }
    }

    public interface RequestOrBuilder extends MessageLiteOrBuilder {
        IDMRequest getIdmRequest();

        boolean hasIdmRequest();
    }

    public final class Response extends GeneratedMessageLite implements ResponseOrBuilder {
        /* access modifiers changed from: private */
        public static final Response DEFAULT_INSTANCE;
        public static final int IDMRESPONSE_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMResponse idmResponse_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements ResponseOrBuilder {
            private Builder() {
                super(Response.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmResponse() {
                copyOnWrite();
                this.instance.clearIdmResponse();
                return this;
            }

            public IDMResponse getIdmResponse() {
                return this.instance.getIdmResponse();
            }

            public boolean hasIdmResponse() {
                return this.instance.hasIdmResponse();
            }

            public Builder mergeIdmResponse(IDMResponse iDMResponse) {
                copyOnWrite();
                this.instance.mergeIdmResponse(iDMResponse);
                return this;
            }

            public Builder setIdmResponse(com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse.Builder builder) {
                copyOnWrite();
                this.instance.setIdmResponse(builder);
                return this;
            }

            public Builder setIdmResponse(IDMResponse iDMResponse) {
                copyOnWrite();
                this.instance.setIdmResponse(iDMResponse);
                return this;
            }
        }

        static {
            Response response = new Response();
            DEFAULT_INSTANCE = response;
            GeneratedMessageLite.registerDefaultInstance(Response.class, response);
        }

        private Response() {
        }

        /* access modifiers changed from: private */
        public void clearIdmResponse() {
            this.idmResponse_ = null;
        }

        public static Response getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmResponse(IDMResponse iDMResponse) {
            if (iDMResponse != null) {
                IDMResponse iDMResponse2 = this.idmResponse_;
                if (!(iDMResponse2 == null || iDMResponse2 == IDMResponse.getDefaultInstance())) {
                    iDMResponse = (IDMResponse) IDMResponse.newBuilder(this.idmResponse_).mergeFrom(iDMResponse).buildPartial();
                }
                this.idmResponse_ = iDMResponse;
                return;
            }
            throw new NullPointerException();
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
        public void setIdmResponse(com.xiaomi.idm.api.proto.IDMServiceProto.IDMResponse.Builder builder) {
            this.idmResponse_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmResponse(IDMResponse iDMResponse) {
            if (iDMResponse != null) {
                this.idmResponse_ = iDMResponse;
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
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmResponse_"});
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

        public IDMResponse getIdmResponse() {
            IDMResponse iDMResponse = this.idmResponse_;
            return iDMResponse == null ? IDMResponse.getDefaultInstance() : iDMResponse;
        }

        public boolean hasIdmResponse() {
            return this.idmResponse_ != null;
        }
    }

    public interface ResponseOrBuilder extends MessageLiteOrBuilder {
        IDMResponse getIdmResponse();

        boolean hasIdmResponse();
    }

    public final class SetEventCallback extends GeneratedMessageLite implements SetEventCallbackOrBuilder {
        /* access modifiers changed from: private */
        public static final SetEventCallback DEFAULT_INSTANCE;
        public static final int IDMEVENT_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMEvent idmEvent_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements SetEventCallbackOrBuilder {
            private Builder() {
                super(SetEventCallback.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmEvent() {
                copyOnWrite();
                this.instance.clearIdmEvent();
                return this;
            }

            public IDMEvent getIdmEvent() {
                return this.instance.getIdmEvent();
            }

            public boolean hasIdmEvent() {
                return this.instance.hasIdmEvent();
            }

            public Builder mergeIdmEvent(IDMEvent iDMEvent) {
                copyOnWrite();
                this.instance.mergeIdmEvent(iDMEvent);
                return this;
            }

            public Builder setIdmEvent(com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent.Builder builder) {
                copyOnWrite();
                this.instance.setIdmEvent(builder);
                return this;
            }

            public Builder setIdmEvent(IDMEvent iDMEvent) {
                copyOnWrite();
                this.instance.setIdmEvent(iDMEvent);
                return this;
            }
        }

        static {
            SetEventCallback setEventCallback = new SetEventCallback();
            DEFAULT_INSTANCE = setEventCallback;
            GeneratedMessageLite.registerDefaultInstance(SetEventCallback.class, setEventCallback);
        }

        private SetEventCallback() {
        }

        /* access modifiers changed from: private */
        public void clearIdmEvent() {
            this.idmEvent_ = null;
        }

        public static SetEventCallback getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmEvent(IDMEvent iDMEvent) {
            if (iDMEvent != null) {
                IDMEvent iDMEvent2 = this.idmEvent_;
                if (!(iDMEvent2 == null || iDMEvent2 == IDMEvent.getDefaultInstance())) {
                    iDMEvent = (IDMEvent) IDMEvent.newBuilder(this.idmEvent_).mergeFrom(iDMEvent).buildPartial();
                }
                this.idmEvent_ = iDMEvent;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(SetEventCallback setEventCallback) {
            return DEFAULT_INSTANCE.createBuilder(setEventCallback);
        }

        public static SetEventCallback parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SetEventCallback parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SetEventCallback parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static SetEventCallback parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static SetEventCallback parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static SetEventCallback parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static SetEventCallback parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static SetEventCallback parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static SetEventCallback parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static SetEventCallback parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static SetEventCallback parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static SetEventCallback parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmEvent(com.xiaomi.idm.api.proto.IDMServiceProto.IDMEvent.Builder builder) {
            this.idmEvent_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmEvent(IDMEvent iDMEvent) {
            if (iDMEvent != null) {
                this.idmEvent_ = iDMEvent;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new SetEventCallback();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmEvent_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (SetEventCallback.class) {
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

        public IDMEvent getIdmEvent() {
            IDMEvent iDMEvent = this.idmEvent_;
            return iDMEvent == null ? IDMEvent.getDefaultInstance() : iDMEvent;
        }

        public boolean hasIdmEvent() {
            return this.idmEvent_ != null;
        }
    }

    public interface SetEventCallbackOrBuilder extends MessageLiteOrBuilder {
        IDMEvent getIdmEvent();

        boolean hasIdmEvent();
    }

    public final class StartAdvertisingIDM extends GeneratedMessageLite implements StartAdvertisingIDMOrBuilder {
        /* access modifiers changed from: private */
        public static final StartAdvertisingIDM DEFAULT_INSTANCE;
        public static final int IDMSERVICE_FIELD_NUMBER = 1;
        private static volatile Parser PARSER;
        private IDMService idmService_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements StartAdvertisingIDMOrBuilder {
            private Builder() {
                super(StartAdvertisingIDM.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearIdmService() {
                copyOnWrite();
                this.instance.clearIdmService();
                return this;
            }

            public IDMService getIdmService() {
                return this.instance.getIdmService();
            }

            public boolean hasIdmService() {
                return this.instance.hasIdmService();
            }

            public Builder mergeIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.mergeIdmService(iDMService);
                return this;
            }

            public Builder setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
                copyOnWrite();
                this.instance.setIdmService(builder);
                return this;
            }

            public Builder setIdmService(IDMService iDMService) {
                copyOnWrite();
                this.instance.setIdmService(iDMService);
                return this;
            }
        }

        static {
            StartAdvertisingIDM startAdvertisingIDM = new StartAdvertisingIDM();
            DEFAULT_INSTANCE = startAdvertisingIDM;
            GeneratedMessageLite.registerDefaultInstance(StartAdvertisingIDM.class, startAdvertisingIDM);
        }

        private StartAdvertisingIDM() {
        }

        /* access modifiers changed from: private */
        public void clearIdmService() {
            this.idmService_ = null;
        }

        public static StartAdvertisingIDM getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        /* access modifiers changed from: private */
        public void mergeIdmService(IDMService iDMService) {
            if (iDMService != null) {
                IDMService iDMService2 = this.idmService_;
                if (!(iDMService2 == null || iDMService2 == IDMService.getDefaultInstance())) {
                    iDMService = (IDMService) IDMService.newBuilder(this.idmService_).mergeFrom(iDMService).buildPartial();
                }
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(StartAdvertisingIDM startAdvertisingIDM) {
            return DEFAULT_INSTANCE.createBuilder(startAdvertisingIDM);
        }

        public static StartAdvertisingIDM parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static StartAdvertisingIDM parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static StartAdvertisingIDM parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static StartAdvertisingIDM parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static StartAdvertisingIDM parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static StartAdvertisingIDM parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static StartAdvertisingIDM parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static StartAdvertisingIDM parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static StartAdvertisingIDM parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static StartAdvertisingIDM parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static StartAdvertisingIDM parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static StartAdvertisingIDM parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setIdmService(com.xiaomi.idm.api.proto.IDMServiceProto.IDMService.Builder builder) {
            this.idmService_ = builder.build();
        }

        /* access modifiers changed from: private */
        public void setIdmService(IDMService iDMService) {
            if (iDMService != null) {
                this.idmService_ = iDMService;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new StartAdvertisingIDM();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0000\u0000\u0001\t", new Object[]{"idmService_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (StartAdvertisingIDM.class) {
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

        public IDMService getIdmService() {
            IDMService iDMService = this.idmService_;
            return iDMService == null ? IDMService.getDefaultInstance() : iDMService;
        }

        public boolean hasIdmService() {
            return this.idmService_ != null;
        }
    }

    public interface StartAdvertisingIDMOrBuilder extends MessageLiteOrBuilder {
        IDMService getIdmService();

        boolean hasIdmService();
    }

    public final class StartDiscovery extends GeneratedMessageLite implements StartDiscoveryOrBuilder {
        /* access modifiers changed from: private */
        public static final StartDiscovery DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int SERVICETYPES_FIELD_NUMBER = 1;
        public static final int SERVICEUUIDS_FIELD_NUMBER = 2;
        private ProtobufList serviceTypes_ = GeneratedMessageLite.emptyProtobufList();
        private ProtobufList serviceUuids_ = GeneratedMessageLite.emptyProtobufList();

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements StartDiscoveryOrBuilder {
            private Builder() {
                super(StartDiscovery.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder addAllServiceTypes(Iterable iterable) {
                copyOnWrite();
                this.instance.addAllServiceTypes(iterable);
                return this;
            }

            public Builder addAllServiceUuids(Iterable iterable) {
                copyOnWrite();
                this.instance.addAllServiceUuids(iterable);
                return this;
            }

            public Builder addServiceTypes(String str) {
                copyOnWrite();
                this.instance.addServiceTypes(str);
                return this;
            }

            public Builder addServiceTypesBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.addServiceTypesBytes(byteString);
                return this;
            }

            public Builder addServiceUuids(String str) {
                copyOnWrite();
                this.instance.addServiceUuids(str);
                return this;
            }

            public Builder addServiceUuidsBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.addServiceUuidsBytes(byteString);
                return this;
            }

            public Builder clearServiceTypes() {
                copyOnWrite();
                this.instance.clearServiceTypes();
                return this;
            }

            public Builder clearServiceUuids() {
                copyOnWrite();
                this.instance.clearServiceUuids();
                return this;
            }

            public String getServiceTypes(int i) {
                return this.instance.getServiceTypes(i);
            }

            public ByteString getServiceTypesBytes(int i) {
                return this.instance.getServiceTypesBytes(i);
            }

            public int getServiceTypesCount() {
                return this.instance.getServiceTypesCount();
            }

            public List getServiceTypesList() {
                return Collections.unmodifiableList(this.instance.getServiceTypesList());
            }

            public String getServiceUuids(int i) {
                return this.instance.getServiceUuids(i);
            }

            public ByteString getServiceUuidsBytes(int i) {
                return this.instance.getServiceUuidsBytes(i);
            }

            public int getServiceUuidsCount() {
                return this.instance.getServiceUuidsCount();
            }

            public List getServiceUuidsList() {
                return Collections.unmodifiableList(this.instance.getServiceUuidsList());
            }

            public Builder setServiceTypes(int i, String str) {
                copyOnWrite();
                this.instance.setServiceTypes(i, str);
                return this;
            }

            public Builder setServiceUuids(int i, String str) {
                copyOnWrite();
                this.instance.setServiceUuids(i, str);
                return this;
            }
        }

        static {
            StartDiscovery startDiscovery = new StartDiscovery();
            DEFAULT_INSTANCE = startDiscovery;
            GeneratedMessageLite.registerDefaultInstance(StartDiscovery.class, startDiscovery);
        }

        private StartDiscovery() {
        }

        /* access modifiers changed from: private */
        public void addAllServiceTypes(Iterable iterable) {
            ensureServiceTypesIsMutable();
            AbstractMessageLite.addAll(iterable, this.serviceTypes_);
        }

        /* access modifiers changed from: private */
        public void addAllServiceUuids(Iterable iterable) {
            ensureServiceUuidsIsMutable();
            AbstractMessageLite.addAll(iterable, this.serviceUuids_);
        }

        /* access modifiers changed from: private */
        public void addServiceTypes(String str) {
            if (str != null) {
                ensureServiceTypesIsMutable();
                this.serviceTypes_.add(str);
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void addServiceTypesBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                ensureServiceTypesIsMutable();
                this.serviceTypes_.add(byteString.toStringUtf8());
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void addServiceUuids(String str) {
            if (str != null) {
                ensureServiceUuidsIsMutable();
                this.serviceUuids_.add(str);
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void addServiceUuidsBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                ensureServiceUuidsIsMutable();
                this.serviceUuids_.add(byteString.toStringUtf8());
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void clearServiceTypes() {
            this.serviceTypes_ = GeneratedMessageLite.emptyProtobufList();
        }

        /* access modifiers changed from: private */
        public void clearServiceUuids() {
            this.serviceUuids_ = GeneratedMessageLite.emptyProtobufList();
        }

        private void ensureServiceTypesIsMutable() {
            if (!this.serviceTypes_.isModifiable()) {
                this.serviceTypes_ = GeneratedMessageLite.mutableCopy(this.serviceTypes_);
            }
        }

        private void ensureServiceUuidsIsMutable() {
            if (!this.serviceUuids_.isModifiable()) {
                this.serviceUuids_ = GeneratedMessageLite.mutableCopy(this.serviceUuids_);
            }
        }

        public static StartDiscovery getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(StartDiscovery startDiscovery) {
            return DEFAULT_INSTANCE.createBuilder(startDiscovery);
        }

        public static StartDiscovery parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static StartDiscovery parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static StartDiscovery parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static StartDiscovery parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static StartDiscovery parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static StartDiscovery parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static StartDiscovery parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static StartDiscovery parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static StartDiscovery parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static StartDiscovery parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static StartDiscovery parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static StartDiscovery parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setServiceTypes(int i, String str) {
            if (str != null) {
                ensureServiceTypesIsMutable();
                this.serviceTypes_.set(i, str);
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setServiceUuids(int i, String str) {
            if (str != null) {
                ensureServiceUuidsIsMutable();
                this.serviceUuids_.set(i, str);
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new StartDiscovery();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0002\u0000\u0001Ț\u0002Ț", new Object[]{"serviceTypes_", "serviceUuids_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (StartDiscovery.class) {
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

        public String getServiceTypes(int i) {
            return (String) this.serviceTypes_.get(i);
        }

        public ByteString getServiceTypesBytes(int i) {
            return ByteString.copyFromUtf8((String) this.serviceTypes_.get(i));
        }

        public int getServiceTypesCount() {
            return this.serviceTypes_.size();
        }

        public List getServiceTypesList() {
            return this.serviceTypes_;
        }

        public String getServiceUuids(int i) {
            return (String) this.serviceUuids_.get(i);
        }

        public ByteString getServiceUuidsBytes(int i) {
            return ByteString.copyFromUtf8((String) this.serviceUuids_.get(i));
        }

        public int getServiceUuidsCount() {
            return this.serviceUuids_.size();
        }

        public List getServiceUuidsList() {
            return this.serviceUuids_;
        }
    }

    public final class StartDiscoveryIDM extends GeneratedMessageLite implements StartDiscoveryIDMOrBuilder {
        /* access modifiers changed from: private */
        public static final StartDiscoveryIDM DEFAULT_INSTANCE;
        private static volatile Parser PARSER;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements StartDiscoveryIDMOrBuilder {
            private Builder() {
                super(StartDiscoveryIDM.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }
        }

        static {
            StartDiscoveryIDM startDiscoveryIDM = new StartDiscoveryIDM();
            DEFAULT_INSTANCE = startDiscoveryIDM;
            GeneratedMessageLite.registerDefaultInstance(StartDiscoveryIDM.class, startDiscoveryIDM);
        }

        private StartDiscoveryIDM() {
        }

        public static StartDiscoveryIDM getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(StartDiscoveryIDM startDiscoveryIDM) {
            return DEFAULT_INSTANCE.createBuilder(startDiscoveryIDM);
        }

        public static StartDiscoveryIDM parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static StartDiscoveryIDM parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static StartDiscoveryIDM parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static StartDiscoveryIDM parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static StartDiscoveryIDM parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static StartDiscoveryIDM parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static StartDiscoveryIDM parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static StartDiscoveryIDM parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static StartDiscoveryIDM parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static StartDiscoveryIDM parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static StartDiscoveryIDM parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static StartDiscoveryIDM parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new StartDiscoveryIDM();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0000", null);
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (StartDiscoveryIDM.class) {
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
    }

    public interface StartDiscoveryIDMOrBuilder extends MessageLiteOrBuilder {
    }

    public interface StartDiscoveryOrBuilder extends MessageLiteOrBuilder {
        String getServiceTypes(int i);

        ByteString getServiceTypesBytes(int i);

        int getServiceTypesCount();

        List getServiceTypesList();

        String getServiceUuids(int i);

        ByteString getServiceUuidsBytes(int i);

        int getServiceUuidsCount();

        List getServiceUuidsList();
    }

    public final class WifiConfig extends GeneratedMessageLite implements WifiConfigOrBuilder {
        public static final int CHANNEL_FIELD_NUMBER = 4;
        /* access modifiers changed from: private */
        public static final WifiConfig DEFAULT_INSTANCE;
        public static final int LOCALIP_FIELD_NUMBER = 7;
        public static final int MACADDR_FIELD_NUMBER = 5;
        private static volatile Parser PARSER = null;
        public static final int PWD_FIELD_NUMBER = 2;
        public static final int REMOTEIP_FIELD_NUMBER = 6;
        public static final int SSID_FIELD_NUMBER = 1;
        public static final int USE5GBAND_FIELD_NUMBER = 3;
        private int channel_;
        private String localIp_;
        private String macAddr_;
        private String pwd_;
        private String remoteIp_;
        private String ssid_;
        private boolean use5GBand_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements WifiConfigOrBuilder {
            private Builder() {
                super(WifiConfig.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearChannel() {
                copyOnWrite();
                this.instance.clearChannel();
                return this;
            }

            public Builder clearLocalIp() {
                copyOnWrite();
                this.instance.clearLocalIp();
                return this;
            }

            public Builder clearMacAddr() {
                copyOnWrite();
                this.instance.clearMacAddr();
                return this;
            }

            public Builder clearPwd() {
                copyOnWrite();
                this.instance.clearPwd();
                return this;
            }

            public Builder clearRemoteIp() {
                copyOnWrite();
                this.instance.clearRemoteIp();
                return this;
            }

            public Builder clearSsid() {
                copyOnWrite();
                this.instance.clearSsid();
                return this;
            }

            public Builder clearUse5GBand() {
                copyOnWrite();
                this.instance.clearUse5GBand();
                return this;
            }

            public int getChannel() {
                return this.instance.getChannel();
            }

            public String getLocalIp() {
                return this.instance.getLocalIp();
            }

            public ByteString getLocalIpBytes() {
                return this.instance.getLocalIpBytes();
            }

            public String getMacAddr() {
                return this.instance.getMacAddr();
            }

            public ByteString getMacAddrBytes() {
                return this.instance.getMacAddrBytes();
            }

            public String getPwd() {
                return this.instance.getPwd();
            }

            public ByteString getPwdBytes() {
                return this.instance.getPwdBytes();
            }

            public String getRemoteIp() {
                return this.instance.getRemoteIp();
            }

            public ByteString getRemoteIpBytes() {
                return this.instance.getRemoteIpBytes();
            }

            public String getSsid() {
                return this.instance.getSsid();
            }

            public ByteString getSsidBytes() {
                return this.instance.getSsidBytes();
            }

            public boolean getUse5GBand() {
                return this.instance.getUse5GBand();
            }

            public Builder setChannel(int i) {
                copyOnWrite();
                this.instance.setChannel(i);
                return this;
            }

            public Builder setLocalIp(String str) {
                copyOnWrite();
                this.instance.setLocalIp(str);
                return this;
            }

            public Builder setLocalIpBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setLocalIpBytes(byteString);
                return this;
            }

            public Builder setMacAddr(String str) {
                copyOnWrite();
                this.instance.setMacAddr(str);
                return this;
            }

            public Builder setMacAddrBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setMacAddrBytes(byteString);
                return this;
            }

            public Builder setPwd(String str) {
                copyOnWrite();
                this.instance.setPwd(str);
                return this;
            }

            public Builder setPwdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setPwdBytes(byteString);
                return this;
            }

            public Builder setRemoteIp(String str) {
                copyOnWrite();
                this.instance.setRemoteIp(str);
                return this;
            }

            public Builder setRemoteIpBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setRemoteIpBytes(byteString);
                return this;
            }

            public Builder setSsid(String str) {
                copyOnWrite();
                this.instance.setSsid(str);
                return this;
            }

            public Builder setSsidBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setSsidBytes(byteString);
                return this;
            }

            public Builder setUse5GBand(boolean z) {
                copyOnWrite();
                this.instance.setUse5GBand(z);
                return this;
            }
        }

        static {
            WifiConfig wifiConfig = new WifiConfig();
            DEFAULT_INSTANCE = wifiConfig;
            GeneratedMessageLite.registerDefaultInstance(WifiConfig.class, wifiConfig);
        }

        private WifiConfig() {
            String str = "";
            this.ssid_ = str;
            this.pwd_ = str;
            this.macAddr_ = str;
            this.remoteIp_ = str;
            this.localIp_ = str;
        }

        /* access modifiers changed from: private */
        public void clearChannel() {
            this.channel_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearLocalIp() {
            this.localIp_ = getDefaultInstance().getLocalIp();
        }

        /* access modifiers changed from: private */
        public void clearMacAddr() {
            this.macAddr_ = getDefaultInstance().getMacAddr();
        }

        /* access modifiers changed from: private */
        public void clearPwd() {
            this.pwd_ = getDefaultInstance().getPwd();
        }

        /* access modifiers changed from: private */
        public void clearRemoteIp() {
            this.remoteIp_ = getDefaultInstance().getRemoteIp();
        }

        /* access modifiers changed from: private */
        public void clearSsid() {
            this.ssid_ = getDefaultInstance().getSsid();
        }

        /* access modifiers changed from: private */
        public void clearUse5GBand() {
            this.use5GBand_ = false;
        }

        public static WifiConfig getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(WifiConfig wifiConfig) {
            return DEFAULT_INSTANCE.createBuilder(wifiConfig);
        }

        public static WifiConfig parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static WifiConfig parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static WifiConfig parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static WifiConfig parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static WifiConfig parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static WifiConfig parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static WifiConfig parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static WifiConfig parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static WifiConfig parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static WifiConfig parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static WifiConfig parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static WifiConfig parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setChannel(int i) {
            this.channel_ = i;
        }

        /* access modifiers changed from: private */
        public void setLocalIp(String str) {
            if (str != null) {
                this.localIp_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setLocalIpBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.localIp_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setMacAddr(String str) {
            if (str != null) {
                this.macAddr_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setMacAddrBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.macAddr_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setPwd(String str) {
            if (str != null) {
                this.pwd_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setPwdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.pwd_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setRemoteIp(String str) {
            if (str != null) {
                this.remoteIp_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setRemoteIpBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.remoteIp_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setSsid(String str) {
            if (str != null) {
                this.ssid_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setSsidBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.ssid_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUse5GBand(boolean z) {
            this.use5GBand_ = z;
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new WifiConfig();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0007\u0000\u0000\u0001\u0007\u0007\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ\u0003\u0007\u0004\u0004\u0005Ȉ\u0006Ȉ\u0007Ȉ", new Object[]{"ssid_", "pwd_", "use5GBand_", "channel_", "macAddr_", "remoteIp_", "localIp_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (WifiConfig.class) {
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

        public int getChannel() {
            return this.channel_;
        }

        public String getLocalIp() {
            return this.localIp_;
        }

        public ByteString getLocalIpBytes() {
            return ByteString.copyFromUtf8(this.localIp_);
        }

        public String getMacAddr() {
            return this.macAddr_;
        }

        public ByteString getMacAddrBytes() {
            return ByteString.copyFromUtf8(this.macAddr_);
        }

        public String getPwd() {
            return this.pwd_;
        }

        public ByteString getPwdBytes() {
            return ByteString.copyFromUtf8(this.pwd_);
        }

        public String getRemoteIp() {
            return this.remoteIp_;
        }

        public ByteString getRemoteIpBytes() {
            return ByteString.copyFromUtf8(this.remoteIp_);
        }

        public String getSsid() {
            return this.ssid_;
        }

        public ByteString getSsidBytes() {
            return ByteString.copyFromUtf8(this.ssid_);
        }

        public boolean getUse5GBand() {
            return this.use5GBand_;
        }
    }

    public interface WifiConfigOrBuilder extends MessageLiteOrBuilder {
        int getChannel();

        String getLocalIp();

        ByteString getLocalIpBytes();

        String getMacAddr();

        ByteString getMacAddrBytes();

        String getPwd();

        ByteString getPwdBytes();

        String getRemoteIp();

        ByteString getRemoteIpBytes();

        String getSsid();

        ByteString getSsidBytes();

        boolean getUse5GBand();
    }

    private IPCLParam() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }
}

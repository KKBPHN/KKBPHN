package com.xiaomi.idm.api.proto;

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

public final class IDMServiceProto {

    /* renamed from: com.xiaomi.idm.api.proto.IDMServiceProto$1 reason: invalid class name */
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

    public final class IDMEvent extends GeneratedMessageLite implements IDMEventOrBuilder {
        /* access modifiers changed from: private */
        public static final IDMEvent DEFAULT_INSTANCE;
        public static final int EID_FIELD_NUMBER = 2;
        public static final int ENABLE_FIELD_NUMBER = 3;
        public static final int EVENT_FIELD_NUMBER = 15;
        private static volatile Parser PARSER = null;
        public static final int UUID_FIELD_NUMBER = 1;
        private int eid_;
        private boolean enable_;
        private ByteString event_ = ByteString.EMPTY;
        private String uuid_ = "";

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements IDMEventOrBuilder {
            private Builder() {
                super(IDMEvent.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearEid() {
                copyOnWrite();
                this.instance.clearEid();
                return this;
            }

            public Builder clearEnable() {
                copyOnWrite();
                this.instance.clearEnable();
                return this;
            }

            public Builder clearEvent() {
                copyOnWrite();
                this.instance.clearEvent();
                return this;
            }

            public Builder clearUuid() {
                copyOnWrite();
                this.instance.clearUuid();
                return this;
            }

            public int getEid() {
                return this.instance.getEid();
            }

            public boolean getEnable() {
                return this.instance.getEnable();
            }

            public ByteString getEvent() {
                return this.instance.getEvent();
            }

            public String getUuid() {
                return this.instance.getUuid();
            }

            public ByteString getUuidBytes() {
                return this.instance.getUuidBytes();
            }

            public Builder setEid(int i) {
                copyOnWrite();
                this.instance.setEid(i);
                return this;
            }

            public Builder setEnable(boolean z) {
                copyOnWrite();
                this.instance.setEnable(z);
                return this;
            }

            public Builder setEvent(ByteString byteString) {
                copyOnWrite();
                this.instance.setEvent(byteString);
                return this;
            }

            public Builder setUuid(String str) {
                copyOnWrite();
                this.instance.setUuid(str);
                return this;
            }

            public Builder setUuidBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setUuidBytes(byteString);
                return this;
            }
        }

        static {
            IDMEvent iDMEvent = new IDMEvent();
            DEFAULT_INSTANCE = iDMEvent;
            GeneratedMessageLite.registerDefaultInstance(IDMEvent.class, iDMEvent);
        }

        private IDMEvent() {
        }

        /* access modifiers changed from: private */
        public void clearEid() {
            this.eid_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearEnable() {
            this.enable_ = false;
        }

        /* access modifiers changed from: private */
        public void clearEvent() {
            this.event_ = getDefaultInstance().getEvent();
        }

        /* access modifiers changed from: private */
        public void clearUuid() {
            this.uuid_ = getDefaultInstance().getUuid();
        }

        public static IDMEvent getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(IDMEvent iDMEvent) {
            return DEFAULT_INSTANCE.createBuilder(iDMEvent);
        }

        public static IDMEvent parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IDMEvent parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IDMEvent parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static IDMEvent parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static IDMEvent parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static IDMEvent parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static IDMEvent parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IDMEvent parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IDMEvent parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static IDMEvent parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static IDMEvent parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static IDMEvent parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setEid(int i) {
            this.eid_ = i;
        }

        /* access modifiers changed from: private */
        public void setEnable(boolean z) {
            this.enable_ = z;
        }

        /* access modifiers changed from: private */
        public void setEvent(ByteString byteString) {
            if (byteString != null) {
                this.event_ = byteString;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUuid(String str) {
            if (str != null) {
                this.uuid_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUuidBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.uuid_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new IDMEvent();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0004\u0000\u0000\u0001\u000f\u0004\u0000\u0000\u0000\u0001Ȉ\u0002\u0004\u0003\u0007\u000f\n", new Object[]{"uuid_", "eid_", "enable_", "event_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (IDMEvent.class) {
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

        public int getEid() {
            return this.eid_;
        }

        public boolean getEnable() {
            return this.enable_;
        }

        public ByteString getEvent() {
            return this.event_;
        }

        public String getUuid() {
            return this.uuid_;
        }

        public ByteString getUuidBytes() {
            return ByteString.copyFromUtf8(this.uuid_);
        }
    }

    public interface IDMEventOrBuilder extends MessageLiteOrBuilder {
        int getEid();

        boolean getEnable();

        ByteString getEvent();

        String getUuid();

        ByteString getUuidBytes();
    }

    public final class IDMRequest extends GeneratedMessageLite implements IDMRequestOrBuilder {
        public static final int AID_FIELD_NUMBER = 2;
        public static final int CLIENTID_FIELD_NUMBER = 4;
        /* access modifiers changed from: private */
        public static final IDMRequest DEFAULT_INSTANCE;
        private static volatile Parser PARSER = null;
        public static final int REQUESTID_FIELD_NUMBER = 3;
        public static final int REQUEST_FIELD_NUMBER = 15;
        public static final int UUID_FIELD_NUMBER = 1;
        private int aid_;
        private String clientId_;
        private String requestId_;
        private ByteString request_ = ByteString.EMPTY;
        private String uuid_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements IDMRequestOrBuilder {
            private Builder() {
                super(IDMRequest.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearAid() {
                copyOnWrite();
                this.instance.clearAid();
                return this;
            }

            public Builder clearClientId() {
                copyOnWrite();
                this.instance.clearClientId();
                return this;
            }

            public Builder clearRequest() {
                copyOnWrite();
                this.instance.clearRequest();
                return this;
            }

            public Builder clearRequestId() {
                copyOnWrite();
                this.instance.clearRequestId();
                return this;
            }

            public Builder clearUuid() {
                copyOnWrite();
                this.instance.clearUuid();
                return this;
            }

            public int getAid() {
                return this.instance.getAid();
            }

            public String getClientId() {
                return this.instance.getClientId();
            }

            public ByteString getClientIdBytes() {
                return this.instance.getClientIdBytes();
            }

            public ByteString getRequest() {
                return this.instance.getRequest();
            }

            public String getRequestId() {
                return this.instance.getRequestId();
            }

            public ByteString getRequestIdBytes() {
                return this.instance.getRequestIdBytes();
            }

            public String getUuid() {
                return this.instance.getUuid();
            }

            public ByteString getUuidBytes() {
                return this.instance.getUuidBytes();
            }

            public Builder setAid(int i) {
                copyOnWrite();
                this.instance.setAid(i);
                return this;
            }

            public Builder setClientId(String str) {
                copyOnWrite();
                this.instance.setClientId(str);
                return this;
            }

            public Builder setClientIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setClientIdBytes(byteString);
                return this;
            }

            public Builder setRequest(ByteString byteString) {
                copyOnWrite();
                this.instance.setRequest(byteString);
                return this;
            }

            public Builder setRequestId(String str) {
                copyOnWrite();
                this.instance.setRequestId(str);
                return this;
            }

            public Builder setRequestIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setRequestIdBytes(byteString);
                return this;
            }

            public Builder setUuid(String str) {
                copyOnWrite();
                this.instance.setUuid(str);
                return this;
            }

            public Builder setUuidBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setUuidBytes(byteString);
                return this;
            }
        }

        static {
            IDMRequest iDMRequest = new IDMRequest();
            DEFAULT_INSTANCE = iDMRequest;
            GeneratedMessageLite.registerDefaultInstance(IDMRequest.class, iDMRequest);
        }

        private IDMRequest() {
            String str = "";
            this.uuid_ = str;
            this.requestId_ = str;
            this.clientId_ = str;
        }

        /* access modifiers changed from: private */
        public void clearAid() {
            this.aid_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearClientId() {
            this.clientId_ = getDefaultInstance().getClientId();
        }

        /* access modifiers changed from: private */
        public void clearRequest() {
            this.request_ = getDefaultInstance().getRequest();
        }

        /* access modifiers changed from: private */
        public void clearRequestId() {
            this.requestId_ = getDefaultInstance().getRequestId();
        }

        /* access modifiers changed from: private */
        public void clearUuid() {
            this.uuid_ = getDefaultInstance().getUuid();
        }

        public static IDMRequest getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(IDMRequest iDMRequest) {
            return DEFAULT_INSTANCE.createBuilder(iDMRequest);
        }

        public static IDMRequest parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IDMRequest parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IDMRequest parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static IDMRequest parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static IDMRequest parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static IDMRequest parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static IDMRequest parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IDMRequest parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IDMRequest parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static IDMRequest parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static IDMRequest parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static IDMRequest parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
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
        public void setClientId(String str) {
            if (str != null) {
                this.clientId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setClientIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.clientId_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setRequest(ByteString byteString) {
            if (byteString != null) {
                this.request_ = byteString;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setRequestId(String str) {
            if (str != null) {
                this.requestId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setRequestIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.requestId_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUuid(String str) {
            if (str != null) {
                this.uuid_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUuidBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.uuid_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new IDMRequest();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0005\u0000\u0000\u0001\u000f\u0005\u0000\u0000\u0000\u0001Ȉ\u0002\u0004\u0003Ȉ\u0004Ȉ\u000f\n", new Object[]{"uuid_", "aid_", "requestId_", "clientId_", "request_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (IDMRequest.class) {
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

        public String getClientId() {
            return this.clientId_;
        }

        public ByteString getClientIdBytes() {
            return ByteString.copyFromUtf8(this.clientId_);
        }

        public ByteString getRequest() {
            return this.request_;
        }

        public String getRequestId() {
            return this.requestId_;
        }

        public ByteString getRequestIdBytes() {
            return ByteString.copyFromUtf8(this.requestId_);
        }

        public String getUuid() {
            return this.uuid_;
        }

        public ByteString getUuidBytes() {
            return ByteString.copyFromUtf8(this.uuid_);
        }
    }

    public interface IDMRequestOrBuilder extends MessageLiteOrBuilder {
        int getAid();

        String getClientId();

        ByteString getClientIdBytes();

        ByteString getRequest();

        String getRequestId();

        ByteString getRequestIdBytes();

        String getUuid();

        ByteString getUuidBytes();
    }

    public final class IDMResponse extends GeneratedMessageLite implements IDMResponseOrBuilder {
        public static final int CLIENTID_FIELD_NUMBER = 5;
        public static final int CODE_FIELD_NUMBER = 1;
        /* access modifiers changed from: private */
        public static final IDMResponse DEFAULT_INSTANCE;
        public static final int MSG_FIELD_NUMBER = 2;
        private static volatile Parser PARSER = null;
        public static final int REQUESTID_FIELD_NUMBER = 3;
        public static final int RESPONSE_FIELD_NUMBER = 15;
        public static final int UUID_FIELD_NUMBER = 4;
        private String clientId_;
        private int code_;
        private String msg_;
        private String requestId_;
        private ByteString response_ = ByteString.EMPTY;
        private String uuid_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements IDMResponseOrBuilder {
            private Builder() {
                super(IDMResponse.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearClientId() {
                copyOnWrite();
                this.instance.clearClientId();
                return this;
            }

            public Builder clearCode() {
                copyOnWrite();
                this.instance.clearCode();
                return this;
            }

            public Builder clearMsg() {
                copyOnWrite();
                this.instance.clearMsg();
                return this;
            }

            public Builder clearRequestId() {
                copyOnWrite();
                this.instance.clearRequestId();
                return this;
            }

            public Builder clearResponse() {
                copyOnWrite();
                this.instance.clearResponse();
                return this;
            }

            public Builder clearUuid() {
                copyOnWrite();
                this.instance.clearUuid();
                return this;
            }

            public String getClientId() {
                return this.instance.getClientId();
            }

            public ByteString getClientIdBytes() {
                return this.instance.getClientIdBytes();
            }

            public int getCode() {
                return this.instance.getCode();
            }

            public String getMsg() {
                return this.instance.getMsg();
            }

            public ByteString getMsgBytes() {
                return this.instance.getMsgBytes();
            }

            public String getRequestId() {
                return this.instance.getRequestId();
            }

            public ByteString getRequestIdBytes() {
                return this.instance.getRequestIdBytes();
            }

            public ByteString getResponse() {
                return this.instance.getResponse();
            }

            public String getUuid() {
                return this.instance.getUuid();
            }

            public ByteString getUuidBytes() {
                return this.instance.getUuidBytes();
            }

            public Builder setClientId(String str) {
                copyOnWrite();
                this.instance.setClientId(str);
                return this;
            }

            public Builder setClientIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setClientIdBytes(byteString);
                return this;
            }

            public Builder setCode(int i) {
                copyOnWrite();
                this.instance.setCode(i);
                return this;
            }

            public Builder setMsg(String str) {
                copyOnWrite();
                this.instance.setMsg(str);
                return this;
            }

            public Builder setMsgBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setMsgBytes(byteString);
                return this;
            }

            public Builder setRequestId(String str) {
                copyOnWrite();
                this.instance.setRequestId(str);
                return this;
            }

            public Builder setRequestIdBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setRequestIdBytes(byteString);
                return this;
            }

            public Builder setResponse(ByteString byteString) {
                copyOnWrite();
                this.instance.setResponse(byteString);
                return this;
            }

            public Builder setUuid(String str) {
                copyOnWrite();
                this.instance.setUuid(str);
                return this;
            }

            public Builder setUuidBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setUuidBytes(byteString);
                return this;
            }
        }

        static {
            IDMResponse iDMResponse = new IDMResponse();
            DEFAULT_INSTANCE = iDMResponse;
            GeneratedMessageLite.registerDefaultInstance(IDMResponse.class, iDMResponse);
        }

        private IDMResponse() {
            String str = "";
            this.msg_ = str;
            this.requestId_ = str;
            this.uuid_ = str;
            this.clientId_ = str;
        }

        /* access modifiers changed from: private */
        public void clearClientId() {
            this.clientId_ = getDefaultInstance().getClientId();
        }

        /* access modifiers changed from: private */
        public void clearCode() {
            this.code_ = 0;
        }

        /* access modifiers changed from: private */
        public void clearMsg() {
            this.msg_ = getDefaultInstance().getMsg();
        }

        /* access modifiers changed from: private */
        public void clearRequestId() {
            this.requestId_ = getDefaultInstance().getRequestId();
        }

        /* access modifiers changed from: private */
        public void clearResponse() {
            this.response_ = getDefaultInstance().getResponse();
        }

        /* access modifiers changed from: private */
        public void clearUuid() {
            this.uuid_ = getDefaultInstance().getUuid();
        }

        public static IDMResponse getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(IDMResponse iDMResponse) {
            return DEFAULT_INSTANCE.createBuilder(iDMResponse);
        }

        public static IDMResponse parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IDMResponse parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IDMResponse parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static IDMResponse parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static IDMResponse parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static IDMResponse parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static IDMResponse parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IDMResponse parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IDMResponse parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static IDMResponse parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static IDMResponse parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static IDMResponse parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setClientId(String str) {
            if (str != null) {
                this.clientId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setClientIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.clientId_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setCode(int i) {
            this.code_ = i;
        }

        /* access modifiers changed from: private */
        public void setMsg(String str) {
            if (str != null) {
                this.msg_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setMsgBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.msg_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setRequestId(String str) {
            if (str != null) {
                this.requestId_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setRequestIdBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.requestId_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setResponse(ByteString byteString) {
            if (byteString != null) {
                this.response_ = byteString;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUuid(String str) {
            if (str != null) {
                this.uuid_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUuidBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.uuid_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new IDMResponse();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0006\u0000\u0000\u0001\u000f\u0006\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003Ȉ\u0004Ȉ\u0005Ȉ\u000f\n", new Object[]{"code_", "msg_", "requestId_", "uuid_", "clientId_", "response_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (IDMResponse.class) {
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

        public String getClientId() {
            return this.clientId_;
        }

        public ByteString getClientIdBytes() {
            return ByteString.copyFromUtf8(this.clientId_);
        }

        public int getCode() {
            return this.code_;
        }

        public String getMsg() {
            return this.msg_;
        }

        public ByteString getMsgBytes() {
            return ByteString.copyFromUtf8(this.msg_);
        }

        public String getRequestId() {
            return this.requestId_;
        }

        public ByteString getRequestIdBytes() {
            return ByteString.copyFromUtf8(this.requestId_);
        }

        public ByteString getResponse() {
            return this.response_;
        }

        public String getUuid() {
            return this.uuid_;
        }

        public ByteString getUuidBytes() {
            return ByteString.copyFromUtf8(this.uuid_);
        }
    }

    public interface IDMResponseOrBuilder extends MessageLiteOrBuilder {
        String getClientId();

        ByteString getClientIdBytes();

        int getCode();

        String getMsg();

        ByteString getMsgBytes();

        String getRequestId();

        ByteString getRequestIdBytes();

        ByteString getResponse();

        String getUuid();

        ByteString getUuidBytes();
    }

    public final class IDMService extends GeneratedMessageLite implements IDMServiceOrBuilder {
        /* access modifiers changed from: private */
        public static final IDMService DEFAULT_INSTANCE;
        public static final int NAME_FIELD_NUMBER = 3;
        private static volatile Parser PARSER = null;
        public static final int TYPE_FIELD_NUMBER = 2;
        public static final int UUID_FIELD_NUMBER = 1;
        private String name_;
        private String type_;
        private String uuid_;

        public final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder implements IDMServiceOrBuilder {
            private Builder() {
                super(IDMService.DEFAULT_INSTANCE);
            }

            /* synthetic */ Builder(AnonymousClass1 r1) {
                this();
            }

            public Builder clearName() {
                copyOnWrite();
                this.instance.clearName();
                return this;
            }

            public Builder clearType() {
                copyOnWrite();
                this.instance.clearType();
                return this;
            }

            public Builder clearUuid() {
                copyOnWrite();
                this.instance.clearUuid();
                return this;
            }

            public String getName() {
                return this.instance.getName();
            }

            public ByteString getNameBytes() {
                return this.instance.getNameBytes();
            }

            public String getType() {
                return this.instance.getType();
            }

            public ByteString getTypeBytes() {
                return this.instance.getTypeBytes();
            }

            public String getUuid() {
                return this.instance.getUuid();
            }

            public ByteString getUuidBytes() {
                return this.instance.getUuidBytes();
            }

            public Builder setName(String str) {
                copyOnWrite();
                this.instance.setName(str);
                return this;
            }

            public Builder setNameBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setNameBytes(byteString);
                return this;
            }

            public Builder setType(String str) {
                copyOnWrite();
                this.instance.setType(str);
                return this;
            }

            public Builder setTypeBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setTypeBytes(byteString);
                return this;
            }

            public Builder setUuid(String str) {
                copyOnWrite();
                this.instance.setUuid(str);
                return this;
            }

            public Builder setUuidBytes(ByteString byteString) {
                copyOnWrite();
                this.instance.setUuidBytes(byteString);
                return this;
            }
        }

        static {
            IDMService iDMService = new IDMService();
            DEFAULT_INSTANCE = iDMService;
            GeneratedMessageLite.registerDefaultInstance(IDMService.class, iDMService);
        }

        private IDMService() {
            String str = "";
            this.uuid_ = str;
            this.type_ = str;
            this.name_ = str;
        }

        /* access modifiers changed from: private */
        public void clearName() {
            this.name_ = getDefaultInstance().getName();
        }

        /* access modifiers changed from: private */
        public void clearType() {
            this.type_ = getDefaultInstance().getType();
        }

        /* access modifiers changed from: private */
        public void clearUuid() {
            this.uuid_ = getDefaultInstance().getUuid();
        }

        public static IDMService getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(IDMService iDMService) {
            return DEFAULT_INSTANCE.createBuilder(iDMService);
        }

        public static IDMService parseDelimitedFrom(InputStream inputStream) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IDMService parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return parseDelimitedFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IDMService parseFrom(ByteString byteString) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString);
        }

        public static IDMService parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }

        public static IDMService parseFrom(CodedInputStream codedInputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream);
        }

        public static IDMService parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }

        public static IDMService parseFrom(InputStream inputStream) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream);
        }

        public static IDMService parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }

        public static IDMService parseFrom(ByteBuffer byteBuffer) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer);
        }

        public static IDMService parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }

        public static IDMService parseFrom(byte[] bArr) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr);
        }

        public static IDMService parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
            return GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, bArr, extensionRegistryLite);
        }

        public static Parser parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }

        /* access modifiers changed from: private */
        public void setName(String str) {
            if (str != null) {
                this.name_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setNameBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.name_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setType(String str) {
            if (str != null) {
                this.type_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setTypeBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.type_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUuid(String str) {
            if (str != null) {
                this.uuid_ = str;
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: private */
        public void setUuidBytes(ByteString byteString) {
            if (byteString != null) {
                checkByteStringIsUtf8(byteString);
                this.uuid_ = byteString.toStringUtf8();
                return;
            }
            throw new NullPointerException();
        }

        /* access modifiers changed from: protected */
        public final Object dynamicMethod(MethodToInvoke methodToInvoke, Object obj, Object obj2) {
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[methodToInvoke.ordinal()]) {
                case 1:
                    return new IDMService();
                case 2:
                    return new Builder(null);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0000\u0003\u0000\u0000\u0001\u0003\u0003\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ\u0003Ȉ", new Object[]{"uuid_", "type_", "name_"});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (IDMService.class) {
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

        public String getName() {
            return this.name_;
        }

        public ByteString getNameBytes() {
            return ByteString.copyFromUtf8(this.name_);
        }

        public String getType() {
            return this.type_;
        }

        public ByteString getTypeBytes() {
            return ByteString.copyFromUtf8(this.type_);
        }

        public String getUuid() {
            return this.uuid_;
        }

        public ByteString getUuidBytes() {
            return ByteString.copyFromUtf8(this.uuid_);
        }
    }

    public interface IDMServiceOrBuilder extends MessageLiteOrBuilder {
        String getName();

        ByteString getNameBytes();

        String getType();

        ByteString getTypeBytes();

        String getUuid();

        ByteString getUuidBytes();
    }

    private IDMServiceProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }
}

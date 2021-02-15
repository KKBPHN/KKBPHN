package com.xiaomi.camera.rcs.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import com.android.camera.data.DataRepository;
import com.xiaomi.camera.rcs.RemoteControlContract;
import com.xiaomi.camera.rcs.util.RCSDebug;
import com.xiaomi.camera.util.IState;
import com.xiaomi.camera.util.State;
import com.xiaomi.camera.util.StateMachine;
import com.xiaomi.mi_connect_sdk.api.AppConfig.Builder;
import com.xiaomi.mi_connect_sdk.api.ConnectionConfig;
import com.xiaomi.mi_connect_sdk.api.MiApp;
import com.xiaomi.mi_connect_sdk.api.MiAppCallback;
import com.xiaomi.mi_connect_sdk.api.MiConnect;
import com.xiaomi.mi_connect_sdk.api.PayloadConfig;
import com.xiaomi.mi_connect_sdk.api.ResultCode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class NetworkStateMachine extends StateMachine {
    private static final long ADVERTISING_RETRY_DELAY = 1000;
    public static final int CMD_SEND_PAYLOAD = 259;
    public static final int CMD_START_ADVERTISING = 257;
    public static final int CMD_START_CONNECTING = 258;
    public static final int CMD_START_DISCOVERING = 256;
    public static final int CMD_START_SERVICE = 47806;
    public static final int CMD_STOP_SERVICE = 57005;
    private static final long CONNECTING_RETRY_DELAY = 1000;
    private static final long DISCOVERING_RETRY_DELAY = 1000;
    private static final int EVT_ADVERTISING_FAILURE = 768;
    private static final int EVT_ADVERTISING_SUCCESS = 769;
    private static final int EVT_CONNECTION_COMPLETED = 1283;
    private static final int EVT_CONNECTION_FAILURE = 1281;
    private static final int EVT_CONNECTION_INITIATED = 1282;
    private static final int EVT_CONNECTION_LOST = 1284;
    private static final int EVT_DISCOVERY_FAILURE = 512;
    private static final int EVT_DISCOVERY_SUCCESS = 513;
    private static final int EVT_ENDPOINT_FOUND = 1024;
    private static final int EVT_ENDPOINT_LOST = 1025;
    private static final int EVT_SERVICE_BOUND = 1536;
    private static final int EVT_SERVICE_ERROR = 1538;
    private static final int EVT_SERVICE_UNBOUND = 1537;
    private static final boolean NETWORK_STATE_MACHINE_DEBUGGING = true;
    /* access modifiers changed from: private */
    public static final String TAG = RCSDebug.createTag(NetworkStateMachine.class);
    /* access modifiers changed from: private */
    public final AdvertisingState mAdvertisingState = new AdvertisingState();
    private MiApp mApi;
    private final int mAppId;
    /* access modifiers changed from: private */
    public final BindingCompleteState mBindingCompleteState = new BindingCompleteState();
    /* access modifiers changed from: private */
    public final BindingInitiateState mBindingInitiateState = new BindingInitiateState();
    private final MiAppCallback mCallback;
    private final int mCommDataType;
    private final int mCommType;
    /* access modifiers changed from: private */
    public final ConnectingCompleteState mConnectingCompleteState = new ConnectingCompleteState();
    /* access modifiers changed from: private */
    public final ConnectingInitiateState mConnectingInitiateState = new ConnectingInitiateState();
    private final Context mContext;
    private final int mDiscType;
    /* access modifiers changed from: private */
    public final DiscoveringState mDiscoveringState = new DiscoveringState();
    /* access modifiers changed from: private */
    public final EndpointFoundState mEndpointFoundState = new EndpointFoundState();
    /* access modifiers changed from: private */
    public int mEndpointId = -1;
    /* access modifiers changed from: private */
    public final LinkedList mListeners = new LinkedList();
    /* access modifiers changed from: private */
    public final int mRoleType;
    /* access modifiers changed from: private */
    public final StandbyState mStandbyState = new StandbyState();
    /* access modifiers changed from: private */
    public boolean mStateMachineQuitting = true;

    /* renamed from: com.xiaomi.camera.rcs.network.NetworkStateMachine$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode = new int[ResultCode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.STOP_ADVERTISING_SUCCESS.ordinal()] = 1;
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.STOP_ADVERTIDING_ERROR.ordinal()] = 2;
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.START_ADVERTISING_SUCCESS.ordinal()] = 3;
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.ALREADY_ADVERTISING.ordinal()] = 4;
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.STOP_DISCOVERY_ERROR.ordinal()] = 5;
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.STOP_DISCOVERY_SUCCESS.ordinal()] = 6;
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.START_DISCOVERY_SUCCESS.ordinal()] = 7;
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.ALREADY_DISCOVERY.ordinal()] = 8;
            $SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.GENERAL_SUCCESS.ordinal()] = 9;
        }
    }

    class AdvertisingState extends State {
        AdvertisingState() {
        }

        public void enter() {
            NetworkStateMachine.this.log("entering advertising state");
        }

        public boolean processMessage(Message message) {
            int i = message.what;
            if (i != NetworkStateMachine.EVT_ADVERTISING_FAILURE) {
                if (i != 1538) {
                    if (i != 47806) {
                        if (i != 57005) {
                            switch (i) {
                                case 1282:
                                    NetworkStateMachine networkStateMachine = NetworkStateMachine.this;
                                    networkStateMachine.acceptConnection(networkStateMachine.mEndpointId);
                                    break;
                                case 1283:
                                    NetworkStateMachine networkStateMachine2 = NetworkStateMachine.this;
                                    networkStateMachine2.transitionTo(networkStateMachine2.mConnectingCompleteState);
                                    return true;
                                case 1284:
                                    break;
                                default:
                                    return false;
                            }
                        }
                    }
                    return true;
                }
                NetworkStateMachine.this.stopAdvertising();
                NetworkStateMachine.this.stopService();
                NetworkStateMachine networkStateMachine3 = NetworkStateMachine.this;
                networkStateMachine3.transitionTo(networkStateMachine3.mStandbyState);
                return true;
            }
            NetworkStateMachine.this.sendMessageDelayed(257, 1000);
            return true;
        }
    }

    class BindingCompleteState extends State {
        BindingCompleteState() {
        }

        public void enter() {
            NetworkStateMachine.this.log("entering binding completed state");
        }

        public boolean processMessage(Message message) {
            NetworkStateMachine networkStateMachine;
            IState access$1700;
            int i = message.what;
            if (i == 256) {
                NetworkStateMachine.this.startDiscovery();
                networkStateMachine = NetworkStateMachine.this;
                access$1700 = networkStateMachine.mDiscoveringState;
            } else if (i != 257) {
                if (!(i == 1284 || i == 1538)) {
                    if (i == 47806) {
                        return true;
                    }
                    if (i != 57005) {
                        return false;
                    }
                }
                NetworkStateMachine.this.stopService();
                networkStateMachine = NetworkStateMachine.this;
                access$1700 = networkStateMachine.mStandbyState;
            } else {
                NetworkStateMachine.this.startAdvertising();
                networkStateMachine = NetworkStateMachine.this;
                access$1700 = networkStateMachine.mAdvertisingState;
            }
            networkStateMachine.transitionTo(access$1700);
            return true;
        }
    }

    class BindingInitiateState extends State {
        BindingInitiateState() {
        }

        public void enter() {
            NetworkStateMachine.this.log("entering binding initiate state");
        }

        public boolean processMessage(Message message) {
            NetworkStateMachine networkStateMachine;
            IState access$900;
            NetworkStateMachine networkStateMachine2;
            int i;
            int i2 = message.what;
            if (i2 != 1284) {
                if (i2 == NetworkStateMachine.EVT_SERVICE_BOUND) {
                    if (NetworkStateMachine.this.mRoleType == 2) {
                        NetworkStateMachine.this.log("send CMD_START_DISCOVERING");
                        networkStateMachine2 = NetworkStateMachine.this;
                        i = 256;
                    } else {
                        networkStateMachine2 = NetworkStateMachine.this;
                        i = 257;
                    }
                    networkStateMachine2.sendMessage(i);
                    networkStateMachine = NetworkStateMachine.this;
                    access$900 = networkStateMachine.mBindingCompleteState;
                    networkStateMachine.transitionTo(access$900);
                    return true;
                } else if (i2 != 1538) {
                    if (i2 == 47806) {
                        return true;
                    }
                    if (i2 != 57005) {
                        return false;
                    }
                }
            }
            NetworkStateMachine.this.stopService();
            networkStateMachine = NetworkStateMachine.this;
            access$900 = networkStateMachine.mStandbyState;
            networkStateMachine.transitionTo(access$900);
            return true;
        }
    }

    class ConnectingCompleteState extends State {
        ConnectingCompleteState() {
        }

        public void enter() {
            NetworkStateMachine.this.log("entering connecting complete state");
        }

        public boolean processMessage(Message message) {
            int i = message.what;
            if (i != 259) {
                if (!(i == 1284 || i == 1538)) {
                    if (i == 47806) {
                        return true;
                    }
                    if (i != 57005) {
                        return false;
                    }
                }
                if (NetworkStateMachine.this.getRoleType() == 1) {
                    NetworkStateMachine.this.stopAdvertising();
                } else {
                    NetworkStateMachine.this.stopDiscovery();
                }
                NetworkStateMachine.this.stopService();
                NetworkStateMachine networkStateMachine = NetworkStateMachine.this;
                networkStateMachine.transitionTo(networkStateMachine.mStandbyState);
                return true;
            }
            NetworkStateMachine.this.sendPayload(0, (byte[]) message.obj);
            return true;
        }
    }

    class ConnectingInitiateState extends State {
        ConnectingInitiateState() {
        }

        private void sendAuthorisationRequest(int i) {
            int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
            Bundle bundle = new Bundle();
            RemoteControlContract.setAuthorisationRequired(bundle, true);
            RemoteControlContract.setPreviousCapturingMode(bundle, 160);
            RemoteControlContract.setCurrentCapturingMode(bundle, currentMode);
            RemoteControlContract.setStreamingServerPort(bundle, 8086);
            NetworkStateMachine.this.sendPayload(i, RemoteControlContract.jsonify(bundle).getBytes());
        }

        public void enter() {
            NetworkStateMachine.this.log("entering connecting initiate state");
        }

        public boolean processMessage(Message message) {
            int i = message.what;
            if (i != 1538) {
                if (i != 47806) {
                    if (i != 57005) {
                        switch (i) {
                            case 1281:
                                NetworkStateMachine.this.sendMessageDelayed(258, 1000);
                                break;
                            case 1282:
                                if (NetworkStateMachine.this.getRoleType() == 1) {
                                    NetworkStateMachine.this.stopAdvertising();
                                } else {
                                    NetworkStateMachine.this.stopDiscovery();
                                }
                                NetworkStateMachine networkStateMachine = NetworkStateMachine.this;
                                networkStateMachine.acceptConnection(networkStateMachine.mEndpointId);
                                sendAuthorisationRequest(NetworkStateMachine.this.mEndpointId);
                                return true;
                            case 1283:
                                NetworkStateMachine networkStateMachine2 = NetworkStateMachine.this;
                                networkStateMachine2.transitionTo(networkStateMachine2.mConnectingCompleteState);
                                return true;
                            case 1284:
                                break;
                            default:
                                return false;
                        }
                    }
                }
                return true;
            }
            if (NetworkStateMachine.this.getRoleType() == 1) {
                NetworkStateMachine.this.stopAdvertising();
            } else {
                NetworkStateMachine.this.stopDiscovery();
            }
            NetworkStateMachine.this.stopService();
            NetworkStateMachine networkStateMachine3 = NetworkStateMachine.this;
            networkStateMachine3.transitionTo(networkStateMachine3.mStandbyState);
            return true;
        }
    }

    class DiscoveringState extends State {
        DiscoveringState() {
        }

        public void enter() {
            NetworkStateMachine.this.log("entering discovering state");
        }

        public boolean processMessage(Message message) {
            int i = message.what;
            if (i != 512) {
                if (i != 513) {
                    if (i != 1024) {
                        if (!(i == 1284 || i == 1538)) {
                            if (i == 47806) {
                                return true;
                            }
                            if (i != 57005) {
                                return false;
                            }
                        }
                        NetworkStateMachine.this.stopDiscovery();
                        NetworkStateMachine.this.stopService();
                        NetworkStateMachine networkStateMachine = NetworkStateMachine.this;
                        networkStateMachine.transitionTo(networkStateMachine.mStandbyState);
                        return true;
                    }
                    NetworkStateMachine networkStateMachine2 = NetworkStateMachine.this;
                    networkStateMachine2.transitionTo(networkStateMachine2.mEndpointFoundState);
                    NetworkStateMachine.this.deferMessage(message);
                }
                return true;
            }
            NetworkStateMachine.this.sendMessageDelayed(256, 1000);
            return true;
        }
    }

    class EndpointFoundState extends State {
        private final Set mEndpoints = new HashSet();

        EndpointFoundState() {
        }

        public void enter() {
            NetworkStateMachine.this.log("entering endpoint found state");
            this.mEndpoints.clear();
        }

        public boolean processMessage(Message message) {
            int i = message.what;
            if (i != 1024) {
                if (!(i == 1284 || i == 1538)) {
                    if (i == 47806) {
                        return true;
                    }
                    if (i != 57005) {
                        return false;
                    }
                }
                if (NetworkStateMachine.this.getRoleType() == 1) {
                    NetworkStateMachine.this.stopAdvertising();
                } else {
                    NetworkStateMachine.this.stopDiscovery();
                }
                NetworkStateMachine.this.stopService();
                NetworkStateMachine networkStateMachine = NetworkStateMachine.this;
                networkStateMachine.transitionTo(networkStateMachine.mStandbyState);
                return true;
            }
            this.mEndpoints.add(Integer.valueOf(message.arg1));
            int i2 = message.arg1;
            if (i2 == 0) {
                NetworkStateMachine.this.mEndpointId = i2;
                NetworkStateMachine networkStateMachine2 = NetworkStateMachine.this;
                networkStateMachine2.requestConnection(networkStateMachine2.mEndpointId);
                NetworkStateMachine networkStateMachine3 = NetworkStateMachine.this;
                networkStateMachine3.transitionTo(networkStateMachine3.mConnectingInitiateState);
            }
            return true;
        }
    }

    class NetworkEventDispatcher implements MiAppCallback {
        private NetworkEventDispatcher() {
        }

        /* synthetic */ NetworkEventDispatcher(NetworkStateMachine networkStateMachine, AnonymousClass1 r2) {
            this();
        }

        public void onAdvertingResult(int i, int i2) {
            NetworkStateMachine networkStateMachine;
            int i3;
            ResultCode fromInt = ResultCode.fromInt(i2);
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onAdvertingResult:\n\tAppId = ");
            sb.append(i);
            sb.append("\n\tResult = ");
            sb.append(fromInt.name());
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                int i4 = AnonymousClass1.$SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.fromInt(i2).ordinal()];
                if (!(i4 == 1 || i4 == 2)) {
                    if (i4 == 3 || i4 == 4) {
                        networkStateMachine = NetworkStateMachine.this;
                        i3 = 769;
                    } else {
                        networkStateMachine = NetworkStateMachine.this;
                        i3 = NetworkStateMachine.EVT_ADVERTISING_FAILURE;
                    }
                    networkStateMachine.sendMessage(i3);
                }
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onAdvertingResult(i, i2);
                        }
                    }
                }
            }
        }

        public void onConnectionInitiated(int i, int i2, String str, byte[] bArr, byte[] bArr2) {
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onConnectionInitiated:\n\tAppId = ");
            sb.append(i);
            sb.append("\n\tEndPointId = ");
            sb.append(i2);
            sb.append("\n\tEndPointInfo = ");
            sb.append(str);
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                NetworkStateMachine.this.sendMessage(1282);
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onConnectionInitiated(i, i2, str, bArr, bArr2);
                        }
                    }
                }
            }
        }

        public void onConnectionResult(int i, int i2, String str, int i3) {
            NetworkStateMachine networkStateMachine;
            int i4;
            ResultCode fromInt = ResultCode.fromInt(i3);
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onConnectionResult:\n\tAppId = ");
            sb.append(i);
            sb.append("\n\tEndPointId = ");
            sb.append(i2);
            sb.append("\n\tEndPointInfo = ");
            sb.append(str);
            sb.append("\n\tResult = ");
            sb.append(fromInt.name());
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                if (AnonymousClass1.$SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.fromInt(i3).ordinal()] != 9) {
                    networkStateMachine = NetworkStateMachine.this;
                    i4 = 1281;
                } else {
                    networkStateMachine = NetworkStateMachine.this;
                    i4 = 1283;
                }
                networkStateMachine.sendMessage(i4);
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onConnectionResult(i, i2, str, i3);
                        }
                    }
                }
            }
        }

        public void onDisconnection(int i, int i2) {
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onDisconnection:\n\tAppId = ");
            sb.append(i);
            sb.append(" \n\tEndPointId = ");
            sb.append(i2);
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                NetworkStateMachine.this.sendMessage(1284);
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onDisconnection(i, i2);
                        }
                    }
                }
            }
        }

        public void onDiscoveryResult(int i, int i2) {
            NetworkStateMachine networkStateMachine;
            int i3;
            ResultCode fromInt = ResultCode.fromInt(i2);
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onDiscoveryResult:\n\tAppId = ");
            sb.append(i);
            sb.append("\n\tResult = ");
            sb.append(fromInt.name());
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                int i4 = AnonymousClass1.$SwitchMap$com$xiaomi$mi_connect_sdk$api$ResultCode[ResultCode.fromInt(i2).ordinal()];
                if (!(i4 == 5 || i4 == 6)) {
                    if (i4 == 7 || i4 == 8) {
                        networkStateMachine = NetworkStateMachine.this;
                        i3 = 513;
                    } else {
                        networkStateMachine = NetworkStateMachine.this;
                        i3 = 512;
                    }
                    networkStateMachine.sendMessage(i3);
                }
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onDiscoveryResult(i, i2);
                        }
                    }
                }
            }
        }

        public void onEndpointFound(int i, int i2, String str, byte[] bArr) {
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEndpointFound:\n\tAppId = ");
            sb.append(i);
            sb.append("\n\tEndPointId = ");
            sb.append(i2);
            sb.append("\n\tEndPointInfo = ");
            sb.append(str);
            sb.append("\n\tEndPointData = ");
            sb.append(new String(bArr));
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                NetworkStateMachine.this.sendMessage(1024, i2);
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onEndpointFound(i, i2, str, bArr);
                        }
                    }
                }
            }
        }

        public void onEndpointLost(int i, int i2, String str) {
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onEndpointLost:\n\tAppId = ");
            sb.append(i);
            sb.append("\n\tEndPointId = ");
            sb.append(i2);
            sb.append("\n\tEndPointInfo = ");
            sb.append(str);
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                NetworkStateMachine.this.sendMessage(1025);
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onEndpointLost(i, i2, str);
                        }
                    }
                }
            }
        }

        public void onPayloadReceived(int i, int i2, byte[] bArr) {
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPayloadReceived:\n\tAppId = ");
            sb.append(i);
            sb.append("\n\tEndPointId = ");
            sb.append(i2);
            sb.append("\n\tAppData = ");
            sb.append(bArr == null ? "null" : Integer.valueOf(bArr.length));
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onPayloadReceived(i, i2, bArr);
                        }
                    }
                }
            }
        }

        public void onPayloadSentResult(int i, int i2, int i3) {
            ResultCode fromInt = ResultCode.fromInt(i3);
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPayloadSentResult:\n\tAppId = ");
            sb.append(i);
            sb.append("\n\tEndPointId = ");
            sb.append(i2);
            sb.append("\n\tResult = ");
            sb.append(fromInt.name());
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onPayloadSentResult(i, i2, i3);
                        }
                    }
                }
            }
        }

        public void onServiceBind() {
            RCSDebug.d(NetworkStateMachine.TAG, "onServiceBind");
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                NetworkStateMachine.this.sendMessage((int) NetworkStateMachine.EVT_SERVICE_BOUND);
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onServiceBind();
                        }
                    }
                }
            }
        }

        public void onServiceError(int i) {
            ResultCode fromInt = ResultCode.fromInt(i);
            String access$100 = NetworkStateMachine.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onServiceError: ");
            sb.append(fromInt);
            RCSDebug.d(access$100, sb.toString());
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                NetworkStateMachine.this.sendMessage(1538);
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onServiceError(i);
                        }
                    }
                }
            }
        }

        public void onServiceUnbind() {
            RCSDebug.d(NetworkStateMachine.TAG, "onServiceUnbind");
            if (!NetworkStateMachine.this.mStateMachineQuitting) {
                NetworkStateMachine.this.sendMessage(1537);
                synchronized (NetworkStateMachine.this.mListeners) {
                    Iterator it = NetworkStateMachine.this.mListeners.iterator();
                    while (it.hasNext()) {
                        MiAppCallback miAppCallback = (MiAppCallback) it.next();
                        if (miAppCallback != null) {
                            miAppCallback.onServiceUnbind();
                        }
                    }
                }
            }
        }
    }

    class StandbyState extends State {
        StandbyState() {
        }

        public void enter() {
            NetworkStateMachine.this.log("entering standby state");
        }

        public boolean processMessage(Message message) {
            int i = message.what;
            if (!(i == 512 || i == 513 || i == NetworkStateMachine.EVT_ADVERTISING_FAILURE || i == 769 || i == 1024 || i == 1025)) {
                if (i == 47806) {
                    NetworkStateMachine.this.startService();
                    NetworkStateMachine networkStateMachine = NetworkStateMachine.this;
                    networkStateMachine.transitionTo(networkStateMachine.mBindingInitiateState);
                } else if (i != 57005) {
                    switch (i) {
                        case 256:
                        case 257:
                        case 258:
                        case 259:
                            break;
                        default:
                            switch (i) {
                                case 1281:
                                case 1282:
                                case 1283:
                                case 1284:
                                    break;
                                default:
                                    switch (i) {
                                        case NetworkStateMachine.EVT_SERVICE_BOUND /*1536*/:
                                        case 1537:
                                        case 1538:
                                            break;
                                        default:
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("Unknown command or event received: ");
                                            sb.append(message.what);
                                            throw new RuntimeException(sb.toString());
                                    }
                            }
                    }
                }
            }
            return true;
        }
    }

    public NetworkStateMachine(Context context, int i, int i2, int i3, int i4) {
        super(TAG);
        this.mRoleType = i;
        this.mAppId = i2;
        this.mDiscType = i3;
        this.mCommType = 8;
        if (i4 == 8) {
            this.mCommDataType = 4;
        } else {
            this.mCommDataType = 0;
        }
        this.mContext = context;
        this.mCallback = new NetworkEventDispatcher(this, null);
        addState(this.mStandbyState);
        addState(this.mBindingInitiateState, this.mStandbyState);
        addState(this.mBindingCompleteState, this.mStandbyState);
        addState(this.mRoleType == 1 ? this.mAdvertisingState : this.mDiscoveringState, this.mBindingCompleteState);
        addState(this.mEndpointFoundState, this.mRoleType == 1 ? this.mAdvertisingState : this.mDiscoveringState);
        addState(this.mConnectingInitiateState, this.mEndpointFoundState);
        addState(this.mConnectingCompleteState, this.mEndpointFoundState);
        setDbg(true);
        setLogRecSize(100);
        setLogOnlyTransitions(false);
    }

    /* access modifiers changed from: private */
    public void acceptConnection(int i) {
        String str;
        String sb;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        String str3 = "acceptConnection(";
        sb2.append(str3);
        sb2.append(i);
        sb2.append("): E");
        RCSDebug.d(str2, sb2.toString());
        if (this.mApi == null) {
            str = TAG;
            sb = "acceptConnection: not started yet";
        } else {
            ConnectionConfig connectionConfig = new ConnectionConfig();
            connectionConfig.setEndPointId(i);
            connectionConfig.setEndPointTrusted(true);
            connectionConfig.setRoleType(getRoleType());
            this.mApi.acceptConnection(connectionConfig);
            str = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(i);
            sb3.append("): X");
            sb = sb3.toString();
        }
        RCSDebug.d(str, sb);
    }

    private void disconnectFromEndPoint(int i) {
        String str;
        String sb;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        String str3 = "disconnectFromEndPoint(";
        sb2.append(str3);
        sb2.append(i);
        sb2.append("): E");
        RCSDebug.d(str2, sb2.toString());
        if (this.mApi == null) {
            str = TAG;
            sb = "disconnectFromEndPoint: not started yet";
        } else {
            ConnectionConfig connectionConfig = new ConnectionConfig();
            connectionConfig.setEndPointId(i);
            connectionConfig.setRoleType(getRoleType());
            this.mApi.disconnectFromEndPoint(connectionConfig);
            str = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(i);
            sb3.append("): X");
            sb = sb3.toString();
        }
        RCSDebug.d(str, sb);
    }

    private void rejectConnection(int i) {
        String str;
        String sb;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        String str3 = "rejectConnection(";
        sb2.append(str3);
        sb2.append(i);
        sb2.append("): E");
        RCSDebug.d(str2, sb2.toString());
        if (this.mApi == null) {
            str = TAG;
            sb = "rejectConnection: not started yet";
        } else {
            ConnectionConfig connectionConfig = new ConnectionConfig();
            connectionConfig.setEndPointId(i);
            connectionConfig.setRoleType(getRoleType());
            this.mApi.rejectConnection(connectionConfig);
            str = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(i);
            sb3.append("): X");
            sb = sb3.toString();
        }
        RCSDebug.d(str, sb);
    }

    /* access modifiers changed from: private */
    public void requestConnection(int i) {
        String str;
        String sb;
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        String str3 = "requestConnection(";
        sb2.append(str3);
        sb2.append(i);
        sb2.append("): E");
        RCSDebug.d(str2, sb2.toString());
        if (this.mApi == null) {
            str = TAG;
            sb = "requestConnection: not started yet";
        } else {
            ConnectionConfig connectionConfig = new ConnectionConfig();
            connectionConfig.setEndPointId(0);
            connectionConfig.setRoleType(getRoleType());
            this.mApi.requestConnection(connectionConfig);
            str = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(i);
            sb3.append("): X");
            sb = sb3.toString();
        }
        RCSDebug.d(str, sb);
    }

    /* access modifiers changed from: private */
    public void sendPayload(int i, byte[] bArr) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("sendPayload(");
        sb.append(i);
        sb.append("):\n\t");
        sb.append(new String(bArr));
        RCSDebug.d(str, sb.toString());
        if (this.mApi == null) {
            RCSDebug.d(TAG, "sendPayload: not started yet");
            return;
        }
        PayloadConfig payloadConfig = new PayloadConfig();
        payloadConfig.setEndPointId(i);
        payloadConfig.setPayload(bArr);
        payloadConfig.setRoleType(getRoleType());
        this.mApi.sendPayload(payloadConfig);
    }

    /* access modifiers changed from: private */
    public void startAdvertising() {
        String str;
        String str2;
        RCSDebug.d(TAG, "startAdvertising: E");
        if (this.mApi == null) {
            str = TAG;
            str2 = "startAdvertising: not started yet";
        } else {
            this.mApi.startAdvertising(new Builder().discType(this.mDiscType).commType(this.mCommType).commDataType(this.mCommDataType).advData(new byte[]{1, 2, 4}).build());
            str = TAG;
            str2 = "startAdvertising: X";
        }
        RCSDebug.d(str, str2);
    }

    /* access modifiers changed from: private */
    public void startDiscovery() {
        String str;
        String str2;
        RCSDebug.d(TAG, "startDiscovery: E");
        if (this.mApi == null) {
            str = TAG;
            str2 = "startDiscovery: not started yet";
        } else {
            this.mApi.startDiscovery(new Builder().discType(this.mDiscType).commType(this.mCommType).commDataType(this.mCommDataType).build());
            str = TAG;
            str2 = "startDiscovery: X";
        }
        RCSDebug.d(str, str2);
    }

    /* access modifiers changed from: private */
    public void startService() {
        String str;
        String str2;
        RCSDebug.d(TAG, "startService: E");
        if (this.mApi != null) {
            str = TAG;
            str2 = "startService: already started";
        } else {
            this.mApi = MiConnect.newApp(this.mContext.getApplicationContext(), this.mCallback, this.mAppId);
            str = TAG;
            str2 = "startService: X";
        }
        RCSDebug.d(str, str2);
    }

    /* access modifiers changed from: private */
    public void stopDiscovery() {
        String str;
        String str2;
        RCSDebug.d(TAG, "stopDiscovery: E");
        MiApp miApp = this.mApi;
        if (miApp == null) {
            str = TAG;
            str2 = "stopDiscovery: not started yet";
        } else {
            miApp.stopDiscovery();
            str = TAG;
            str2 = "stopDiscovery: X";
        }
        RCSDebug.d(str, str2);
    }

    /* access modifiers changed from: private */
    public void stopService() {
        String str;
        String str2;
        RCSDebug.d(TAG, "stopService: E");
        MiApp miApp = this.mApi;
        if (miApp == null) {
            str = TAG;
            str2 = "stopService: not started yet";
        } else {
            MiConnect.delApp(miApp, getRoleType());
            this.mApi = null;
            str = TAG;
            str2 = "stopService: X";
        }
        RCSDebug.d(str, str2);
    }

    public void addListener(MiAppCallback miAppCallback) {
        synchronized (this.mListeners) {
            if (!this.mListeners.isEmpty()) {
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    if (((MiAppCallback) it.next()) == miAppCallback) {
                        return;
                    }
                }
            }
            this.mListeners.add(miAppCallback);
        }
    }

    public int getAppId() {
        return this.mAppId;
    }

    public int getEndpointId() {
        return this.mEndpointId;
    }

    public int getRoleType() {
        return this.mRoleType;
    }

    /* access modifiers changed from: protected */
    public String getWhatToString(int i) {
        if (i == 512) {
            return "<discovery failure>";
        }
        if (i == 513) {
            return "<discovery success>";
        }
        if (i == EVT_ADVERTISING_FAILURE) {
            return "advertising failure>";
        }
        if (i == 769) {
            return "<advertising success>";
        }
        if (i == 1024) {
            return "<endpoint found>";
        }
        if (i == 1025) {
            return "<endpoint lost>";
        }
        if (i == 47806) {
            return "<start service>";
        }
        if (i == 57005) {
            return "<stop service>";
        }
        switch (i) {
            case 256:
                return "<start discovery>";
            case 257:
                return "<start advertising>";
            case 258:
                return "<start connecting>";
            case 259:
                return "<send payload>";
            default:
                switch (i) {
                    case 1281:
                        return "<connection failure>";
                    case 1282:
                        return "<connection initiated>";
                    case 1283:
                        return "<connection completed>";
                    case 1284:
                        return "<connection lost>";
                    default:
                        switch (i) {
                            case EVT_SERVICE_BOUND /*1536*/:
                                return "<service bound>";
                            case 1537:
                                return "<service unbound>";
                            case 1538:
                                return "<service error>";
                            default:
                                return super.getWhatToString(i);
                        }
                }
        }
    }

    /* access modifiers changed from: protected */
    public void onQuitting() {
        RCSDebug.d(TAG, "onQuitting: E");
        super.onQuitting();
        RCSDebug.d(TAG, "onQuitting: X");
    }

    public void removeListener(MiAppCallback miAppCallback) {
        synchronized (this.mListeners) {
            this.mListeners.remove(miAppCallback);
        }
    }

    public synchronized void start() {
        RCSDebug.d(TAG, "start: E");
        this.mStateMachineQuitting = false;
        setInitialState(this.mStandbyState);
        super.start();
        RCSDebug.d(TAG, "start: X");
    }

    public synchronized void stop() {
        RCSDebug.d(TAG, "stop: E");
        this.mStateMachineQuitting = true;
        sendMessage((int) CMD_STOP_SERVICE);
        super.quit();
        RCSDebug.d(TAG, "stop: X");
    }

    public void stopAdvertising() {
        String str;
        String str2;
        RCSDebug.d(TAG, "stopAdvertising: E");
        MiApp miApp = this.mApi;
        if (miApp == null) {
            str = TAG;
            str2 = "stopAdvertising: not started yet";
        } else {
            miApp.stopAdvertising();
            str = TAG;
            str2 = "stopAdvertising: X";
        }
        RCSDebug.d(str, str2);
    }
}

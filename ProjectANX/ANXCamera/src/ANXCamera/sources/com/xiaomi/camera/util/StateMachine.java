package com.xiaomi.camera.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.camera.log.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class StateMachine {
    public static final boolean HANDLED = true;
    public static final boolean NOT_HANDLED = false;
    private static final int SM_INIT_CMD = -2;
    private static final int SM_QUIT_CMD = -1;
    /* access modifiers changed from: private */
    public String mName;
    /* access modifiers changed from: private */
    public SmHandler mSmHandler;
    /* access modifiers changed from: private */
    public HandlerThread mSmThread;

    public class LogRec {
        private IState mDstState;
        private String mInfo;
        private IState mOrgState;
        private StateMachine mSm;
        private IState mState;
        private long mTime;
        private int mWhat;

        LogRec(StateMachine stateMachine, Message message, String str, IState iState, IState iState2, IState iState3) {
            update(stateMachine, message, str, iState, iState2, iState3);
        }

        public IState getDestState() {
            return this.mDstState;
        }

        public String getInfo() {
            return this.mInfo;
        }

        public IState getOriginalState() {
            return this.mOrgState;
        }

        public IState getState() {
            return this.mState;
        }

        public long getTime() {
            return this.mTime;
        }

        public long getWhat() {
            return (long) this.mWhat;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("time=");
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(this.mTime);
            sb.append(String.format("%tm-%td %tH:%tM:%tS.%tL", new Object[]{instance, instance, instance, instance, instance, instance}));
            sb.append(" processed=");
            IState iState = this.mState;
            String str = "<null>";
            sb.append(iState == null ? str : iState.getName());
            sb.append(" org=");
            IState iState2 = this.mOrgState;
            sb.append(iState2 == null ? str : iState2.getName());
            sb.append(" dest=");
            IState iState3 = this.mDstState;
            if (iState3 != null) {
                str = iState3.getName();
            }
            sb.append(str);
            sb.append(" what=");
            StateMachine stateMachine = this.mSm;
            String whatToString = stateMachine != null ? stateMachine.getWhatToString(this.mWhat) : "";
            if (TextUtils.isEmpty(whatToString)) {
                sb.append(this.mWhat);
                sb.append("(0x");
                sb.append(Integer.toHexString(this.mWhat));
                whatToString = ")";
            }
            sb.append(whatToString);
            if (!TextUtils.isEmpty(this.mInfo)) {
                sb.append(" ");
                sb.append(this.mInfo);
            }
            return sb.toString();
        }

        public void update(StateMachine stateMachine, Message message, String str, IState iState, IState iState2, IState iState3) {
            this.mSm = stateMachine;
            this.mTime = System.currentTimeMillis();
            this.mWhat = message != null ? message.what : 0;
            this.mInfo = str;
            this.mState = iState;
            this.mOrgState = iState2;
            this.mDstState = iState3;
        }
    }

    class LogRecords {
        private static final int DEFAULT_SIZE = 20;
        private int mCount;
        private boolean mLogOnlyTransitions;
        /* access modifiers changed from: private */
        public Vector mLogRecVector;
        /* access modifiers changed from: private */
        public int mMaxSize;
        private int mOldestIndex;

        private LogRecords() {
            this.mLogRecVector = new Vector();
            this.mMaxSize = 20;
            this.mOldestIndex = 0;
            this.mCount = 0;
            this.mLogOnlyTransitions = false;
        }

        /* access modifiers changed from: 0000 */
        public synchronized void add(StateMachine stateMachine, Message message, String str, IState iState, IState iState2, IState iState3) {
            this.mCount++;
            if (this.mLogRecVector.size() < this.mMaxSize) {
                Vector vector = this.mLogRecVector;
                LogRec logRec = new LogRec(stateMachine, message, str, iState, iState2, iState3);
                vector.add(logRec);
            } else {
                LogRec logRec2 = (LogRec) this.mLogRecVector.get(this.mOldestIndex);
                this.mOldestIndex++;
                if (this.mOldestIndex >= this.mMaxSize) {
                    this.mOldestIndex = 0;
                }
                logRec2.update(stateMachine, message, str, iState, iState2, iState3);
            }
        }

        /* access modifiers changed from: 0000 */
        public synchronized void cleanup() {
            this.mLogRecVector.clear();
        }

        /* access modifiers changed from: 0000 */
        public synchronized int count() {
            return this.mCount;
        }

        /* access modifiers changed from: 0000 */
        public synchronized LogRec get(int i) {
            int i2 = this.mOldestIndex + i;
            if (i2 >= this.mMaxSize) {
                i2 -= this.mMaxSize;
            }
            if (i2 >= size()) {
                return null;
            }
            return (LogRec) this.mLogRecVector.get(i2);
        }

        /* access modifiers changed from: 0000 */
        public synchronized boolean logOnlyTransitions() {
            return this.mLogOnlyTransitions;
        }

        /* access modifiers changed from: 0000 */
        public synchronized void setLogOnlyTransitions(boolean z) {
            this.mLogOnlyTransitions = z;
        }

        /* access modifiers changed from: 0000 */
        public synchronized void setSize(int i) {
            this.mMaxSize = i;
            this.mOldestIndex = 0;
            this.mCount = 0;
            this.mLogRecVector.clear();
        }

        /* access modifiers changed from: 0000 */
        public synchronized int size() {
            return this.mLogRecVector.size();
        }
    }

    class SmHandler extends Handler {
        private static final Object mSmHandlerObj = new Object();
        /* access modifiers changed from: private */
        public boolean mDbg;
        /* access modifiers changed from: private */
        public ArrayList mDeferredMessages;
        /* access modifiers changed from: private */
        public State mDestState;
        /* access modifiers changed from: private */
        public HaltingState mHaltingState;
        private boolean mHasQuit;
        private State mInitialState;
        private boolean mIsConstructionCompleted;
        /* access modifiers changed from: private */
        public LogRecords mLogRecords;
        private Message mMsg;
        private QuittingState mQuittingState;
        /* access modifiers changed from: private */
        public StateMachine mSm;
        private HashMap mStateInfo;
        /* access modifiers changed from: private */
        public StateInfo[] mStateStack;
        /* access modifiers changed from: private */
        public int mStateStackTopIndex;
        private StateInfo[] mTempStateStack;
        private int mTempStateStackCount;
        private boolean mTransitionInProgress;

        class HaltingState extends State {
            private HaltingState() {
            }

            public boolean processMessage(Message message) {
                SmHandler.this.mSm.haltedProcessMessage(message);
                return true;
            }
        }

        class QuittingState extends State {
            private QuittingState() {
            }

            public boolean processMessage(Message message) {
                return false;
            }
        }

        class StateInfo {
            boolean active;
            StateInfo parentStateInfo;
            State state;

            private StateInfo() {
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("state=");
                sb.append(this.state.getName());
                sb.append(",active=");
                sb.append(this.active);
                sb.append(",parent=");
                StateInfo stateInfo = this.parentStateInfo;
                sb.append(stateInfo == null ? "null" : stateInfo.state.getName());
                return sb.toString();
            }
        }

        private SmHandler(Looper looper, StateMachine stateMachine) {
            super(looper);
            this.mHasQuit = false;
            this.mDbg = false;
            this.mLogRecords = new LogRecords();
            this.mStateStackTopIndex = -1;
            this.mHaltingState = new HaltingState();
            this.mQuittingState = new QuittingState();
            this.mStateInfo = new HashMap();
            this.mTransitionInProgress = false;
            this.mDeferredMessages = new ArrayList();
            this.mSm = stateMachine;
            addState(this.mHaltingState, null);
            addState(this.mQuittingState, null);
        }

        static /* synthetic */ boolean O000000o(StateInfo stateInfo, StateInfo stateInfo2) {
            return stateInfo2.parentStateInfo == stateInfo;
        }

        /* access modifiers changed from: private */
        public final StateInfo addState(State state, State state2) {
            StateInfo stateInfo;
            if (this.mDbg) {
                StateMachine stateMachine = this.mSm;
                StringBuilder sb = new StringBuilder();
                sb.append("addStateInternal: E state=");
                sb.append(state.getName());
                sb.append(",parent=");
                sb.append(state2 == null ? "" : state2.getName());
                stateMachine.log(sb.toString());
            }
            if (state2 != null) {
                StateInfo stateInfo2 = (StateInfo) this.mStateInfo.get(state2);
                stateInfo = stateInfo2 == null ? addState(state2, null) : stateInfo2;
            } else {
                stateInfo = null;
            }
            StateInfo stateInfo3 = (StateInfo) this.mStateInfo.get(state);
            if (stateInfo3 == null) {
                stateInfo3 = new StateInfo();
                this.mStateInfo.put(state, stateInfo3);
            }
            StateInfo stateInfo4 = stateInfo3.parentStateInfo;
            if (stateInfo4 == null || stateInfo4 == stateInfo) {
                stateInfo3.state = state;
                stateInfo3.parentStateInfo = stateInfo;
                stateInfo3.active = false;
                if (this.mDbg) {
                    StateMachine stateMachine2 = this.mSm;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("addStateInternal: X stateInfo: ");
                    sb2.append(stateInfo3);
                    stateMachine2.log(sb2.toString());
                }
                return stateInfo3;
            }
            throw new RuntimeException("state already added");
        }

        private final void cleanupAfterQuitting() {
            if (this.mSm.mSmThread != null) {
                getLooper().quit();
                this.mSm.mSmThread = null;
            }
            this.mSm.mSmHandler = null;
            this.mSm = null;
            this.mMsg = null;
            this.mLogRecords.cleanup();
            this.mStateStack = null;
            this.mTempStateStack = null;
            this.mStateInfo.clear();
            this.mInitialState = null;
            this.mDestState = null;
            this.mDeferredMessages.clear();
            this.mHasQuit = true;
        }

        /* access modifiers changed from: private */
        public final void completeConstruction() {
            if (this.mDbg) {
                this.mSm.log("completeConstruction: E");
            }
            int i = 0;
            for (StateInfo stateInfo : this.mStateInfo.values()) {
                int i2 = 0;
                while (stateInfo != null) {
                    stateInfo = stateInfo.parentStateInfo;
                    i2++;
                }
                if (i < i2) {
                    i = i2;
                }
            }
            if (this.mDbg) {
                StateMachine stateMachine = this.mSm;
                StringBuilder sb = new StringBuilder();
                sb.append("completeConstruction: maxDepth=");
                sb.append(i);
                stateMachine.log(sb.toString());
            }
            this.mStateStack = new StateInfo[i];
            this.mTempStateStack = new StateInfo[i];
            setupInitialStateStack();
            sendMessageAtFrontOfQueue(obtainMessage(-2, mSmHandlerObj));
            if (this.mDbg) {
                this.mSm.log("completeConstruction: X");
            }
        }

        /* access modifiers changed from: private */
        public final void deferMessage(Message message) {
            if (this.mDbg) {
                StateMachine stateMachine = this.mSm;
                StringBuilder sb = new StringBuilder();
                sb.append("deferMessage: msg=");
                sb.append(message.what);
                stateMachine.log(sb.toString());
            }
            Message obtainMessage = obtainMessage();
            obtainMessage.copyFrom(message);
            this.mDeferredMessages.add(obtainMessage);
        }

        /* access modifiers changed from: private */
        public final Message getCurrentMessage() {
            return this.mMsg;
        }

        /* access modifiers changed from: private */
        public final IState getCurrentState() {
            int i = this.mStateStackTopIndex;
            if (i < 0) {
                return null;
            }
            return this.mStateStack[i].state;
        }

        private final void invokeEnterMethods(int i) {
            int i2 = i;
            while (true) {
                int i3 = this.mStateStackTopIndex;
                if (i2 <= i3) {
                    if (i == i3) {
                        this.mTransitionInProgress = false;
                    }
                    if (this.mDbg) {
                        StateMachine stateMachine = this.mSm;
                        StringBuilder sb = new StringBuilder();
                        sb.append("invokeEnterMethods: ");
                        sb.append(this.mStateStack[i2].state.getName());
                        stateMachine.log(sb.toString());
                    }
                    this.mStateStack[i2].state.enter();
                    this.mStateStack[i2].active = true;
                    i2++;
                } else {
                    this.mTransitionInProgress = false;
                    return;
                }
            }
        }

        private final void invokeExitMethods(StateInfo stateInfo) {
            while (true) {
                int i = this.mStateStackTopIndex;
                if (i >= 0) {
                    StateInfo[] stateInfoArr = this.mStateStack;
                    if (stateInfoArr[i] != stateInfo) {
                        State state = stateInfoArr[i].state;
                        if (this.mDbg) {
                            StateMachine stateMachine = this.mSm;
                            StringBuilder sb = new StringBuilder();
                            sb.append("invokeExitMethods: ");
                            sb.append(state.getName());
                            stateMachine.log(sb.toString());
                        }
                        state.exit();
                        StateInfo[] stateInfoArr2 = this.mStateStack;
                        int i2 = this.mStateStackTopIndex;
                        stateInfoArr2[i2].active = false;
                        this.mStateStackTopIndex = i2 - 1;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: private */
        public final boolean isDbg() {
            return this.mDbg;
        }

        /* access modifiers changed from: private */
        public final boolean isQuit(Message message) {
            return message.what == -1 && message.obj == mSmHandlerObj;
        }

        private final void moveDeferredMessageAtFrontOfQueue() {
            for (int size = this.mDeferredMessages.size() - 1; size >= 0; size--) {
                Message message = (Message) this.mDeferredMessages.get(size);
                if (this.mDbg) {
                    StateMachine stateMachine = this.mSm;
                    StringBuilder sb = new StringBuilder();
                    sb.append("moveDeferredMessageAtFrontOfQueue; what=");
                    sb.append(message.what);
                    stateMachine.log(sb.toString());
                }
                sendMessageAtFrontOfQueue(message);
            }
            this.mDeferredMessages.clear();
        }

        private final int moveTempStateStackToStateStack() {
            int i = this.mStateStackTopIndex + 1;
            int i2 = i;
            for (int i3 = this.mTempStateStackCount - 1; i3 >= 0; i3--) {
                if (this.mDbg) {
                    StateMachine stateMachine = this.mSm;
                    StringBuilder sb = new StringBuilder();
                    sb.append("moveTempStackToStateStack: i=");
                    sb.append(i3);
                    sb.append(",j=");
                    sb.append(i2);
                    stateMachine.log(sb.toString());
                }
                this.mStateStack[i2] = this.mTempStateStack[i3];
                i2++;
            }
            this.mStateStackTopIndex = i2 - 1;
            if (this.mDbg) {
                StateMachine stateMachine2 = this.mSm;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("moveTempStackToStateStack: X mStateStackTop=");
                sb2.append(this.mStateStackTopIndex);
                sb2.append(",startingIndex=");
                sb2.append(i);
                sb2.append(",Top=");
                sb2.append(this.mStateStack[this.mStateStackTopIndex].state.getName());
                stateMachine2.log(sb2.toString());
            }
            return i;
        }

        private void performTransitions(State state, Message message) {
            State state2 = this.mStateStack[this.mStateStackTopIndex].state;
            boolean z = this.mSm.recordLogRec(this.mMsg) && message.obj != mSmHandlerObj;
            if (!this.mLogRecords.logOnlyTransitions() ? z : this.mDestState != null) {
                LogRecords logRecords = this.mLogRecords;
                StateMachine stateMachine = this.mSm;
                Message message2 = this.mMsg;
                logRecords.add(stateMachine, message2, stateMachine.getLogRecString(message2), state, state2, this.mDestState);
            }
            State state3 = this.mDestState;
            if (state3 != null) {
                while (true) {
                    if (this.mDbg) {
                        this.mSm.log("handleMessage: new destination call exit/enter");
                    }
                    StateInfo stateInfo = setupTempStateStackWithStatesToEnter(state3);
                    this.mTransitionInProgress = true;
                    invokeExitMethods(stateInfo);
                    invokeEnterMethods(moveTempStateStackToStateStack());
                    moveDeferredMessageAtFrontOfQueue();
                    State state4 = this.mDestState;
                    if (state3 == state4) {
                        break;
                    }
                    state3 = state4;
                }
                this.mDestState = null;
            }
            if (state3 == null) {
                return;
            }
            if (state3 == this.mQuittingState) {
                this.mSm.onQuitting();
                cleanupAfterQuitting();
            } else if (state3 == this.mHaltingState) {
                this.mSm.onHalting();
            }
        }

        private final State processMsg(Message message) {
            StateInfo stateInfo = this.mStateStack[this.mStateStackTopIndex];
            String str = "processMsg: ";
            if (this.mDbg) {
                StateMachine stateMachine = this.mSm;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(stateInfo.state.getName());
                stateMachine.log(sb.toString());
            }
            if (!isQuit(message)) {
                while (true) {
                    if (stateInfo.state.processMessage(message)) {
                        break;
                    }
                    stateInfo = stateInfo.parentStateInfo;
                    if (stateInfo == null) {
                        this.mSm.unhandledMessage(message);
                        break;
                    } else if (this.mDbg) {
                        StateMachine stateMachine2 = this.mSm;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(stateInfo.state.getName());
                        stateMachine2.log(sb2.toString());
                    }
                }
            } else {
                transitionTo(this.mQuittingState);
            }
            if (stateInfo != null) {
                return stateInfo.state;
            }
            return null;
        }

        /* access modifiers changed from: private */
        public final void quit() {
            if (this.mDbg) {
                this.mSm.log("quit:");
            }
            sendMessage(obtainMessage(-1, mSmHandlerObj));
        }

        /* access modifiers changed from: private */
        public final void quitNow() {
            if (this.mDbg) {
                this.mSm.log("quitNow:");
            }
            sendMessageAtFrontOfQueue(obtainMessage(-1, mSmHandlerObj));
        }

        /* access modifiers changed from: private */
        public void removeState(State state) {
            StateInfo stateInfo = (StateInfo) this.mStateInfo.get(state);
            if (stateInfo != null && !stateInfo.active && !this.mStateInfo.values().stream().filter(new O000000o(stateInfo)).findAny().isPresent()) {
                this.mStateInfo.remove(state);
            }
        }

        /* access modifiers changed from: private */
        public final void setDbg(boolean z) {
            this.mDbg = z;
        }

        /* access modifiers changed from: private */
        public final void setInitialState(State state) {
            if (this.mDbg) {
                StateMachine stateMachine = this.mSm;
                StringBuilder sb = new StringBuilder();
                sb.append("setInitialState: initialState=");
                sb.append(state.getName());
                stateMachine.log(sb.toString());
            }
            this.mInitialState = state;
        }

        private final void setupInitialStateStack() {
            if (this.mDbg) {
                StateMachine stateMachine = this.mSm;
                StringBuilder sb = new StringBuilder();
                sb.append("setupInitialStateStack: E mInitialState=");
                sb.append(this.mInitialState.getName());
                stateMachine.log(sb.toString());
            }
            StateInfo stateInfo = (StateInfo) this.mStateInfo.get(this.mInitialState);
            this.mTempStateStackCount = 0;
            while (stateInfo != null) {
                StateInfo[] stateInfoArr = this.mTempStateStack;
                int i = this.mTempStateStackCount;
                stateInfoArr[i] = stateInfo;
                stateInfo = stateInfo.parentStateInfo;
                this.mTempStateStackCount = i + 1;
            }
            this.mStateStackTopIndex = -1;
            moveTempStateStackToStateStack();
        }

        private final StateInfo setupTempStateStackWithStatesToEnter(State state) {
            this.mTempStateStackCount = 0;
            StateInfo stateInfo = (StateInfo) this.mStateInfo.get(state);
            do {
                StateInfo[] stateInfoArr = this.mTempStateStack;
                int i = this.mTempStateStackCount;
                this.mTempStateStackCount = i + 1;
                stateInfoArr[i] = stateInfo;
                stateInfo = stateInfo.parentStateInfo;
                if (stateInfo == null) {
                    break;
                }
            } while (!stateInfo.active);
            if (this.mDbg) {
                StateMachine stateMachine = this.mSm;
                StringBuilder sb = new StringBuilder();
                sb.append("setupTempStateStackWithStatesToEnter: X mTempStateStackCount=");
                sb.append(this.mTempStateStackCount);
                sb.append(",curStateInfo: ");
                sb.append(stateInfo);
                stateMachine.log(sb.toString());
            }
            return stateInfo;
        }

        /* access modifiers changed from: private */
        public final void transitionTo(IState iState) {
            if (this.mTransitionInProgress) {
                String access$700 = this.mSm.mName;
                StringBuilder sb = new StringBuilder();
                sb.append("transitionTo called while transition already in progress to ");
                sb.append(this.mDestState);
                sb.append(", new target state=");
                sb.append(iState);
                Log.wtf(access$700, sb.toString());
            }
            this.mDestState = (State) iState;
            if (this.mDbg) {
                StateMachine stateMachine = this.mSm;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("transitionTo: destState=");
                sb2.append(this.mDestState.getName());
                stateMachine.log(sb2.toString());
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:25:0x0077  */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x0084  */
        /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(Message message) {
            StateMachine stateMachine;
            if (!this.mHasQuit) {
                StateMachine stateMachine2 = this.mSm;
                if (stateMachine2 != null) {
                    int i = message.what;
                    if (!(i == -2 || i == -1)) {
                        stateMachine2.onPreHandleMessage(message);
                    }
                }
                if (this.mDbg) {
                    StateMachine stateMachine3 = this.mSm;
                    StringBuilder sb = new StringBuilder();
                    sb.append("handleMessage: E msg.what=");
                    sb.append(this.mSm.getWhatToString(message.what));
                    stateMachine3.log(sb.toString());
                }
                this.mMsg = message;
                State state = null;
                boolean z = this.mIsConstructionCompleted;
                if (!z) {
                    Message message2 = this.mMsg;
                    int i2 = message2.what;
                    if (i2 != -1) {
                        if (!z && i2 == -2 && message2.obj == mSmHandlerObj) {
                            this.mIsConstructionCompleted = true;
                            invokeEnterMethods(0);
                            performTransitions(state, message);
                            if (this.mDbg) {
                                StateMachine stateMachine4 = this.mSm;
                                if (stateMachine4 != null) {
                                    stateMachine4.log("handleMessage: X");
                                }
                            }
                            stateMachine = this.mSm;
                            if (stateMachine == null) {
                                int i3 = message.what;
                                if (i3 != -2 && i3 != -1) {
                                    stateMachine.onPostHandleMessage(message);
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("StateMachine.handleMessage: The start method not called, received msg: ");
                        sb2.append(message);
                        throw new RuntimeException(sb2.toString());
                    }
                }
                state = processMsg(message);
                performTransitions(state, message);
                if (this.mDbg) {
                }
                stateMachine = this.mSm;
                if (stateMachine == null) {
                }
            }
        }
    }

    protected StateMachine(String str) {
        this.mSmThread = new HandlerThread(str);
        this.mSmThread.start();
        initStateMachine(str, this.mSmThread.getLooper());
    }

    protected StateMachine(String str, Handler handler) {
        initStateMachine(str, handler.getLooper());
    }

    protected StateMachine(String str, Looper looper) {
        initStateMachine(str, looper);
    }

    private void initStateMachine(String str, Looper looper) {
        this.mName = str;
        this.mSmHandler = new SmHandler(looper, this);
    }

    public void addLogRec(String str) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.mLogRecords.add(this, smHandler.getCurrentMessage(), str, smHandler.getCurrentState(), smHandler.mStateStack[smHandler.mStateStackTopIndex].state, smHandler.mDestState);
        }
    }

    public final void addState(State state) {
        this.mSmHandler.addState(state, null);
    }

    public final void addState(State state, State state2) {
        this.mSmHandler.addState(state, state2);
    }

    public final Collection copyLogRecs() {
        Vector vector = new Vector();
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            Iterator it = smHandler.mLogRecords.mLogRecVector.iterator();
            while (it.hasNext()) {
                vector.add((LogRec) it.next());
            }
        }
        return vector;
    }

    public final void deferMessage(Message message) {
        this.mSmHandler.deferMessage(message);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append(":");
        printWriter.println(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" total records=");
        sb2.append(getLogRecCount());
        printWriter.println(sb2.toString());
        for (int i = 0; i < getLogRecSize(); i++) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(" rec[");
            sb3.append(i);
            sb3.append("]: ");
            sb3.append(getLogRec(i).toString());
            printWriter.println(sb3.toString());
            printWriter.flush();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("curState=");
        sb4.append(getCurrentState().getName());
        printWriter.println(sb4.toString());
    }

    public final Message getCurrentMessage() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return null;
        }
        return smHandler.getCurrentMessage();
    }

    public final IState getCurrentState() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return null;
        }
        return smHandler.getCurrentState();
    }

    public final Handler getHandler() {
        return this.mSmHandler;
    }

    public final LogRec getLogRec(int i) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return null;
        }
        return smHandler.mLogRecords.get(i);
    }

    public final int getLogRecCount() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return 0;
        }
        return smHandler.mLogRecords.count();
    }

    public final int getLogRecMaxSize() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return 0;
        }
        return smHandler.mLogRecords.mMaxSize;
    }

    public final int getLogRecSize() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return 0;
        }
        return smHandler.mLogRecords.size();
    }

    /* access modifiers changed from: protected */
    public String getLogRecString(Message message) {
        return "";
    }

    public final String getName() {
        return this.mName;
    }

    /* access modifiers changed from: protected */
    public String getWhatToString(int i) {
        return i != -2 ? i != -1 ? String.valueOf(i) : "sm quit" : "sm init";
    }

    /* access modifiers changed from: protected */
    public void haltedProcessMessage(Message message) {
    }

    /* access modifiers changed from: protected */
    public final boolean hasDeferredMessages(int i) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return false;
        }
        Iterator it = smHandler.mDeferredMessages.iterator();
        while (it.hasNext()) {
            if (((Message) it.next()).what == i) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean hasMessages(int i) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return false;
        }
        return smHandler.hasMessages(i);
    }

    public boolean isDbg() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler == null) {
            return false;
        }
        return smHandler.isDbg();
    }

    /* access modifiers changed from: protected */
    public final boolean isQuit(Message message) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            return smHandler.isQuit(message);
        }
        return message.what == -1;
    }

    /* access modifiers changed from: protected */
    public void log(String str) {
        Log.d(this.mName, str);
    }

    /* access modifiers changed from: protected */
    public void logAndAddLogRec(String str) {
        addLogRec(str);
        log(str);
    }

    /* access modifiers changed from: protected */
    public void logd(String str) {
        Log.d(this.mName, str);
    }

    /* access modifiers changed from: protected */
    public void loge(String str) {
        Log.e(this.mName, str);
    }

    /* access modifiers changed from: protected */
    public void loge(String str, Throwable th) {
        Log.e(this.mName, str, th);
    }

    /* access modifiers changed from: protected */
    public void logi(String str) {
        Log.i(this.mName, str);
    }

    /* access modifiers changed from: protected */
    public void logv(String str) {
        Log.v(this.mName, str);
    }

    /* access modifiers changed from: protected */
    public void logw(String str) {
        Log.w(this.mName, str);
    }

    public final Message obtainMessage() {
        return Message.obtain(this.mSmHandler);
    }

    public final Message obtainMessage(int i) {
        return Message.obtain(this.mSmHandler, i);
    }

    public final Message obtainMessage(int i, int i2) {
        return Message.obtain(this.mSmHandler, i, i2, 0);
    }

    public final Message obtainMessage(int i, int i2, int i3) {
        return Message.obtain(this.mSmHandler, i, i2, i3);
    }

    public final Message obtainMessage(int i, int i2, int i3, Object obj) {
        return Message.obtain(this.mSmHandler, i, i2, i3, obj);
    }

    public final Message obtainMessage(int i, Object obj) {
        return Message.obtain(this.mSmHandler, i, obj);
    }

    /* access modifiers changed from: protected */
    public void onHalting() {
    }

    /* access modifiers changed from: protected */
    public void onPostHandleMessage(Message message) {
    }

    /* access modifiers changed from: protected */
    public void onPreHandleMessage(Message message) {
    }

    /* access modifiers changed from: protected */
    public void onQuitting() {
    }

    public final void quit() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.quit();
        }
    }

    public final void quitNow() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.quitNow();
        }
    }

    /* access modifiers changed from: protected */
    public boolean recordLogRec(Message message) {
        return true;
    }

    /* access modifiers changed from: protected */
    public final void removeDeferredMessages(int i) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            Iterator it = smHandler.mDeferredMessages.iterator();
            while (it.hasNext()) {
                if (((Message) it.next()).what == i) {
                    it.remove();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void removeMessages(int i) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.removeMessages(i);
        }
    }

    public final void removeState(State state) {
        this.mSmHandler.removeState(state);
    }

    public void sendMessage(int i) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessage(obtainMessage(i));
        }
    }

    public void sendMessage(int i, int i2) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessage(obtainMessage(i, i2));
        }
    }

    public void sendMessage(int i, int i2, int i3) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessage(obtainMessage(i, i2, i3));
        }
    }

    public void sendMessage(int i, int i2, int i3, Object obj) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessage(obtainMessage(i, i2, i3, obj));
        }
    }

    public void sendMessage(int i, Object obj) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessage(obtainMessage(i, obj));
        }
    }

    public void sendMessage(Message message) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessage(message);
        }
    }

    /* access modifiers changed from: protected */
    public final void sendMessageAtFrontOfQueue(int i) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageAtFrontOfQueue(obtainMessage(i));
        }
    }

    /* access modifiers changed from: protected */
    public final void sendMessageAtFrontOfQueue(int i, int i2) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageAtFrontOfQueue(obtainMessage(i, i2));
        }
    }

    /* access modifiers changed from: protected */
    public final void sendMessageAtFrontOfQueue(int i, int i2, int i3) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageAtFrontOfQueue(obtainMessage(i, i2, i3));
        }
    }

    /* access modifiers changed from: protected */
    public final void sendMessageAtFrontOfQueue(int i, int i2, int i3, Object obj) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageAtFrontOfQueue(obtainMessage(i, i2, i3, obj));
        }
    }

    /* access modifiers changed from: protected */
    public final void sendMessageAtFrontOfQueue(int i, Object obj) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageAtFrontOfQueue(obtainMessage(i, obj));
        }
    }

    /* access modifiers changed from: protected */
    public final void sendMessageAtFrontOfQueue(Message message) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageAtFrontOfQueue(message);
        }
    }

    public void sendMessageDelayed(int i, int i2, int i3, long j) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageDelayed(obtainMessage(i, i2, i3), j);
        }
    }

    public void sendMessageDelayed(int i, int i2, int i3, Object obj, long j) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageDelayed(obtainMessage(i, i2, i3, obj), j);
        }
    }

    public void sendMessageDelayed(int i, int i2, long j) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageDelayed(obtainMessage(i, i2), j);
        }
    }

    public void sendMessageDelayed(int i, long j) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageDelayed(obtainMessage(i), j);
        }
    }

    public void sendMessageDelayed(int i, Object obj, long j) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageDelayed(obtainMessage(i, obj), j);
        }
    }

    public void sendMessageDelayed(Message message, long j) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.sendMessageDelayed(message, j);
        }
    }

    public void setDbg(boolean z) {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.setDbg(z);
        }
    }

    public final void setInitialState(State state) {
        this.mSmHandler.setInitialState(state);
    }

    public final void setLogOnlyTransitions(boolean z) {
        this.mSmHandler.mLogRecords.setLogOnlyTransitions(z);
    }

    public final void setLogRecSize(int i) {
        this.mSmHandler.mLogRecords.setSize(i);
    }

    public void start() {
        SmHandler smHandler = this.mSmHandler;
        if (smHandler != null) {
            smHandler.completeConstruction();
        }
    }

    public String toString() {
        String str;
        String str2 = "(null)";
        try {
            str = this.mName.toString();
            try {
                str2 = this.mSmHandler.getCurrentState().getName().toString();
            } catch (ArrayIndexOutOfBoundsException | NullPointerException unused) {
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException unused2) {
            str = str2;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("name=");
        sb.append(str);
        sb.append(" state=");
        sb.append(str2);
        return sb.toString();
    }

    public final void transitionTo(IState iState) {
        this.mSmHandler.transitionTo(iState);
    }

    public final void transitionToHaltingState() {
        SmHandler smHandler = this.mSmHandler;
        smHandler.transitionTo(smHandler.mHaltingState);
    }

    /* access modifiers changed from: protected */
    public void unhandledMessage(Message message) {
        if (this.mSmHandler.mDbg) {
            StringBuilder sb = new StringBuilder();
            sb.append(" - unhandledMessage: msg.what=");
            sb.append(message.what);
            loge(sb.toString());
        }
    }
}

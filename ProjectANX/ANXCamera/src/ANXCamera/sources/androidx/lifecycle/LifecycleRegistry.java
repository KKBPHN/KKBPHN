package androidx.lifecycle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.Lifecycle.State;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class LifecycleRegistry extends Lifecycle {
    private int mAddingObserverCounter = 0;
    private boolean mHandlingEvent = false;
    private final WeakReference mLifecycleOwner;
    private boolean mNewEventOccurred = false;
    private FastSafeIterableMap mObserverMap = new FastSafeIterableMap();
    private ArrayList mParentStates = new ArrayList();
    private State mState;

    /* renamed from: androidx.lifecycle.LifecycleRegistry$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$androidx$lifecycle$Lifecycle$Event = new int[Event.values().length];
        static final /* synthetic */ int[] $SwitchMap$androidx$lifecycle$Lifecycle$State = new int[State.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(27:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|(2:17|18)|19|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|(2:17|18)|19|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(30:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0053 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x005d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0067 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0071 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x007b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x0086 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$State[State.INITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$State[State.CREATED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$State[State.STARTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$State[State.RESUMED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$State[State.DESTROYED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_CREATE.ordinal()] = 1;
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_STOP.ordinal()] = 2;
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_START.ordinal()] = 3;
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_PAUSE.ordinal()] = 4;
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_RESUME.ordinal()] = 5;
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_DESTROY.ordinal()] = 6;
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_ANY.ordinal()] = 7;
        }
    }

    class ObserverWithState {
        LifecycleEventObserver mLifecycleObserver;
        State mState;

        ObserverWithState(LifecycleObserver lifecycleObserver, State state) {
            this.mLifecycleObserver = Lifecycling.lifecycleEventObserver(lifecycleObserver);
            this.mState = state;
        }

        /* access modifiers changed from: 0000 */
        public void dispatchEvent(LifecycleOwner lifecycleOwner, Event event) {
            State stateAfter = LifecycleRegistry.getStateAfter(event);
            this.mState = LifecycleRegistry.min(this.mState, stateAfter);
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, event);
            this.mState = stateAfter;
        }
    }

    public LifecycleRegistry(@NonNull LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = new WeakReference(lifecycleOwner);
        this.mState = State.INITIALIZED;
    }

    private void backwardPass(LifecycleOwner lifecycleOwner) {
        Iterator descendingIterator = this.mObserverMap.descendingIterator();
        while (descendingIterator.hasNext() && !this.mNewEventOccurred) {
            Entry entry = (Entry) descendingIterator.next();
            ObserverWithState observerWithState = (ObserverWithState) entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(entry.getKey())) {
                Event downEvent = downEvent(observerWithState.mState);
                pushParentState(getStateAfter(downEvent));
                observerWithState.dispatchEvent(lifecycleOwner, downEvent);
                popParentState();
            }
        }
    }

    private State calculateTargetState(LifecycleObserver lifecycleObserver) {
        Entry ceil = this.mObserverMap.ceil(lifecycleObserver);
        State state = null;
        State state2 = ceil != null ? ((ObserverWithState) ceil.getValue()).mState : null;
        if (!this.mParentStates.isEmpty()) {
            ArrayList arrayList = this.mParentStates;
            state = (State) arrayList.get(arrayList.size() - 1);
        }
        return min(min(this.mState, state2), state);
    }

    private static Event downEvent(State state) {
        int i = AnonymousClass1.$SwitchMap$androidx$lifecycle$Lifecycle$State[state.ordinal()];
        if (i == 1) {
            throw new IllegalArgumentException();
        } else if (i == 2) {
            return Event.ON_DESTROY;
        } else {
            if (i == 3) {
                return Event.ON_STOP;
            }
            if (i == 4) {
                return Event.ON_PAUSE;
            }
            if (i != 5) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected state value ");
                sb.append(state);
                throw new IllegalArgumentException(sb.toString());
            }
            throw new IllegalArgumentException();
        }
    }

    private void forwardPass(LifecycleOwner lifecycleOwner) {
        IteratorWithAdditions iteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
        while (iteratorWithAdditions.hasNext() && !this.mNewEventOccurred) {
            Entry entry = (Entry) iteratorWithAdditions.next();
            ObserverWithState observerWithState = (ObserverWithState) entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains(entry.getKey())) {
                pushParentState(observerWithState.mState);
                observerWithState.dispatchEvent(lifecycleOwner, upEvent(observerWithState.mState));
                popParentState();
            }
        }
    }

    static State getStateAfter(Event event) {
        switch (AnonymousClass1.$SwitchMap$androidx$lifecycle$Lifecycle$Event[event.ordinal()]) {
            case 1:
            case 2:
                return State.CREATED;
            case 3:
            case 4:
                return State.STARTED;
            case 5:
                return State.RESUMED;
            case 6:
                return State.DESTROYED;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected event value ");
                sb.append(event);
                throw new IllegalArgumentException(sb.toString());
        }
    }

    private boolean isSynced() {
        boolean z = true;
        if (this.mObserverMap.size() == 0) {
            return true;
        }
        State state = ((ObserverWithState) this.mObserverMap.eldest().getValue()).mState;
        State state2 = ((ObserverWithState) this.mObserverMap.newest().getValue()).mState;
        if (!(state == state2 && this.mState == state2)) {
            z = false;
        }
        return z;
    }

    static State min(@NonNull State state, @Nullable State state2) {
        return (state2 == null || state2.compareTo(state) >= 0) ? state : state2;
    }

    private void moveToState(State state) {
        if (this.mState != state) {
            this.mState = state;
            if (this.mHandlingEvent || this.mAddingObserverCounter != 0) {
                this.mNewEventOccurred = true;
                return;
            }
            this.mHandlingEvent = true;
            sync();
            this.mHandlingEvent = false;
        }
    }

    private void popParentState() {
        ArrayList arrayList = this.mParentStates;
        arrayList.remove(arrayList.size() - 1);
    }

    private void pushParentState(State state) {
        this.mParentStates.add(state);
    }

    private void sync() {
        LifecycleOwner lifecycleOwner = (LifecycleOwner) this.mLifecycleOwner.get();
        if (lifecycleOwner != null) {
            while (true) {
                boolean isSynced = isSynced();
                this.mNewEventOccurred = false;
                if (!isSynced) {
                    if (this.mState.compareTo(((ObserverWithState) this.mObserverMap.eldest().getValue()).mState) < 0) {
                        backwardPass(lifecycleOwner);
                    }
                    Entry newest = this.mObserverMap.newest();
                    if (!this.mNewEventOccurred && newest != null && this.mState.compareTo(((ObserverWithState) newest.getValue()).mState) > 0) {
                        forwardPass(lifecycleOwner);
                    }
                } else {
                    return;
                }
            }
        } else {
            throw new IllegalStateException("LifecycleOwner of this LifecycleRegistry is alreadygarbage collected. It is too late to change lifecycle state.");
        }
    }

    private static Event upEvent(State state) {
        int i = AnonymousClass1.$SwitchMap$androidx$lifecycle$Lifecycle$State[state.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return Event.ON_START;
            }
            if (i == 3) {
                return Event.ON_RESUME;
            }
            if (i == 4) {
                throw new IllegalArgumentException();
            } else if (i != 5) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected state value ");
                sb.append(state);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        return Event.ON_CREATE;
    }

    public void addObserver(@NonNull LifecycleObserver lifecycleObserver) {
        State state = this.mState;
        State state2 = State.DESTROYED;
        if (state != state2) {
            state2 = State.INITIALIZED;
        }
        ObserverWithState observerWithState = new ObserverWithState(lifecycleObserver, state2);
        if (((ObserverWithState) this.mObserverMap.putIfAbsent(lifecycleObserver, observerWithState)) == null) {
            LifecycleOwner lifecycleOwner = (LifecycleOwner) this.mLifecycleOwner.get();
            if (lifecycleOwner != null) {
                boolean z = this.mAddingObserverCounter != 0 || this.mHandlingEvent;
                State calculateTargetState = calculateTargetState(lifecycleObserver);
                this.mAddingObserverCounter++;
                while (observerWithState.mState.compareTo(calculateTargetState) < 0 && this.mObserverMap.contains(lifecycleObserver)) {
                    pushParentState(observerWithState.mState);
                    observerWithState.dispatchEvent(lifecycleOwner, upEvent(observerWithState.mState));
                    popParentState();
                    calculateTargetState = calculateTargetState(lifecycleObserver);
                }
                if (!z) {
                    sync();
                }
                this.mAddingObserverCounter--;
            }
        }
    }

    @NonNull
    public State getCurrentState() {
        return this.mState;
    }

    public int getObserverCount() {
        return this.mObserverMap.size();
    }

    public void handleLifecycleEvent(@NonNull Event event) {
        moveToState(getStateAfter(event));
    }

    @MainThread
    @Deprecated
    public void markState(@NonNull State state) {
        setCurrentState(state);
    }

    public void removeObserver(@NonNull LifecycleObserver lifecycleObserver) {
        this.mObserverMap.remove(lifecycleObserver);
    }

    @MainThread
    public void setCurrentState(@NonNull State state) {
        moveToState(state);
    }
}

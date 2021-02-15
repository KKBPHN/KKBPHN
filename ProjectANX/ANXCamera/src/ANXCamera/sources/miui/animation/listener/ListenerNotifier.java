package miui.animation.listener;

import android.util.ArrayMap;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import miui.animation.IAnimTarget;
import miui.animation.base.AnimConfig;
import miui.animation.base.AnimConfigLink;
import miui.animation.internal.AnimRunner;
import miui.animation.property.FloatProperty;
import miui.animation.property.IIntValueProperty;
import miui.animation.utils.CommonUtils;
import miui.animation.utils.LogUtils;

public class ListenerNotifier {
    private static final int NOTIFY_ANIM_CANCEL = 6;
    private static final int NOTIFY_ANIM_END = 5;
    private static final int NOTIFY_BEGIN = 0;
    private static final int NOTIFY_CANCEL_ALL = 7;
    private static final int NOTIFY_END_ALL = 8;
    private static final int NOTIFY_PROPERTY_BEGIN = 1;
    private static final int NOTIFY_PROPERTY_END = 4;
    private static final int NOTIFY_UPDATE = 2;
    private static final int NOTIFY_UPDATE_LIST = 3;
    private static BaseNotifier[] sNotifiers = {new BaseNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, List list) {
            listenerNode.listener.onBegin(obj);
        }
    }, new SingleNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, UpdateInfo updateInfo) {
            listenerNode.listener.onBegin(obj, updateInfo);
        }
    }, new SingleNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, UpdateInfo updateInfo) {
            if (!updateInfo.isCompleted || updateInfo.endTime == AnimRunner.getInst().getRunningTime()) {
                ListenerNotifier.notifyOnUpdate(listenerNode, obj, updateInfo);
            }
        }
    }, new BaseNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, List list) {
            listenerNode.listener.onUpdate(obj, list);
        }
    }, new SingleNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, UpdateInfo updateInfo) {
            if (updateInfo.isCompleted && !updateInfo.isCanceled && !updateInfo.isEndByUser && updateInfo.endTime == AnimRunner.getInst().getRunningTime()) {
                listenerNode.listener.onComplete(obj, updateInfo);
            }
        }
    }, new SingleNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, UpdateInfo updateInfo) {
            listenerNode.listener.onComplete(obj, updateInfo);
        }
    }, new SingleNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, UpdateInfo updateInfo) {
            listenerNode.listener.onCancel(obj, updateInfo);
        }
    }, new BaseNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, List list) {
            listenerNode.listener.onCancel(obj);
            ListenerNotifier.removeNode(listenerNode, obj);
        }
    }, new BaseNotifier() {
        /* access modifiers changed from: 0000 */
        public void doNotify(ListenerNode listenerNode, Object obj, List list) {
            listenerNode.listener.onComplete(obj);
            ListenerNotifier.removeNode(listenerNode, obj);
        }
    }};
    private PendingNotify mCurNotify;
    private List mExistListeners = new ArrayList();
    private ListenerNode mHead = new ListenerNode();
    private ArrayMap mNotifyData = new ArrayMap();
    private List mPendingList = new ArrayList();
    private final IAnimTarget mTarget;

    abstract class BaseNotifier {
        private List mUpdateList;

        private BaseNotifier() {
            this.mUpdateList = new ArrayList();
        }

        public abstract void doNotify(ListenerNode listenerNode, Object obj, List list);

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<miui.animation.listener.UpdateInfo>, for r6v0, types: [java.util.List, java.util.List<miui.animation.listener.UpdateInfo>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void notify(ListenerNode listenerNode, Object obj, List<UpdateInfo> list) {
            if (CommonUtils.isArrayEmpty(listenerNode.relatedProperty)) {
                doNotify(listenerNode, obj, list);
                return;
            }
            for (UpdateInfo updateInfo : list) {
                FloatProperty[] floatPropertyArr = listenerNode.relatedProperty;
                if (floatPropertyArr == null || CommonUtils.inArray(floatPropertyArr, updateInfo.property)) {
                    this.mUpdateList.add(updateInfo);
                }
            }
            doNotify(listenerNode, obj, this.mUpdateList);
            this.mUpdateList.clear();
        }
    }

    class ListenerNode {
        Object bindTag;
        TransitionListener listener;
        ListenerNode next;
        ListenerNode prev;
        FloatProperty[] relatedProperty;
        Object toTag;

        private ListenerNode() {
        }
    }

    class NotifyData {
        List updateList = new ArrayList();

        NotifyData() {
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<miui.animation.listener.UpdateInfo>, for r2v0, types: [java.util.Collection, java.util.Collection<miui.animation.listener.UpdateInfo>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void addList(Collection<UpdateInfo> collection) {
            for (UpdateInfo addUpdate : collection) {
                addUpdate(addUpdate);
            }
        }

        /* access modifiers changed from: 0000 */
        public void addUpdate(UpdateInfo updateInfo) {
            if (!this.updateList.contains(updateInfo)) {
                this.updateList.add(updateInfo);
            }
        }
    }

    class PendingNotify {
        Object tag;
        int what = -1;

        PendingNotify(Object obj, int i) {
            this.tag = obj;
            this.what = i;
        }

        /* access modifiers changed from: 0000 */
        public boolean isSame(Object obj, int i) {
            Object obj2 = this.tag;
            return obj2 != null && this.what == i && obj2.equals(obj);
        }
    }

    abstract class SingleNotifier extends BaseNotifier {
        private SingleNotifier() {
            super();
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<miui.animation.listener.UpdateInfo>, for r4v0, types: [java.util.List, java.util.List<miui.animation.listener.UpdateInfo>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void doNotify(ListenerNode listenerNode, Object obj, List<UpdateInfo> list) {
            for (UpdateInfo doNotify : list) {
                doNotify(listenerNode, obj, doNotify);
            }
        }

        public abstract void doNotify(ListenerNode listenerNode, Object obj, UpdateInfo updateInfo);
    }

    public ListenerNotifier(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
    }

    private void addNode(Object obj, AnimConfig animConfig) {
        if (!animConfig.listeners.isEmpty()) {
            ListenerNode listenerNode = this.mHead;
            while (true) {
                ListenerNode listenerNode2 = listenerNode.next;
                if (listenerNode2 == null) {
                    break;
                }
                if (animConfig.listeners.contains(listenerNode2.listener)) {
                    this.mExistListeners.add(listenerNode2.listener);
                    updateNodeInfo(listenerNode2, obj, animConfig);
                }
                listenerNode = listenerNode2;
            }
            Iterator it = animConfig.listeners.iterator();
            while (it.hasNext()) {
                TransitionListener transitionListener = (TransitionListener) it.next();
                if (!this.mExistListeners.contains(transitionListener)) {
                    ListenerNode listenerNode3 = new ListenerNode();
                    listenerNode3.listener = transitionListener;
                    updateNodeInfo(listenerNode3, obj, animConfig);
                    listenerNode3.prev = listenerNode;
                    listenerNode.next = listenerNode3;
                    listenerNode = listenerNode3;
                }
            }
            this.mExistListeners.clear();
        }
    }

    private int getNodeCount() {
        ListenerNode listenerNode = this.mHead;
        int i = 0;
        while (true) {
            listenerNode = listenerNode.next;
            if (listenerNode == null) {
                return i;
            }
            i++;
        }
    }

    private NotifyData getNotifyData(int i, Object obj) {
        SparseArray sparseArray = (SparseArray) this.mNotifyData.get(obj);
        if (sparseArray == null) {
            sparseArray = new SparseArray();
            this.mNotifyData.put(obj, sparseArray);
        }
        NotifyData notifyData = (NotifyData) sparseArray.get(i);
        if (notifyData != null) {
            return notifyData;
        }
        NotifyData notifyData2 = new NotifyData();
        sparseArray.put(i, notifyData2);
        return notifyData2;
    }

    private void notifyByType(int i, Object obj, UpdateInfo updateInfo, Collection collection) {
        if (getNodeCount() != 0) {
            NotifyData notifyData = getNotifyData(i, obj);
            if (collection != null) {
                notifyData.addList(collection);
            } else if (updateInfo != null) {
                notifyData.addUpdate(updateInfo);
            }
            PendingNotify pendingNotify = this.mCurNotify;
            if (pendingNotify == null) {
                this.mCurNotify = new PendingNotify(obj, i);
                notifyListener(obj, i, notifyData);
                for (PendingNotify pendingNotify2 : this.mPendingList) {
                    notifyListener(pendingNotify2.tag, pendingNotify2.what, getNotifyData(pendingNotify2.what, pendingNotify2.tag));
                }
                this.mCurNotify = null;
                this.mPendingList.clear();
            } else if (!pendingNotify.isSame(obj, i)) {
                for (PendingNotify isSame : this.mPendingList) {
                    if (isSame.isSame(obj, i)) {
                        return;
                    }
                }
                this.mPendingList.add(new PendingNotify(obj, i));
            }
        }
    }

    private void notifyListener(Object obj, int i, NotifyData notifyData) {
        ListenerNode listenerNode = this.mHead;
        while (true) {
            listenerNode = listenerNode.next;
            if (listenerNode != null) {
                Object obj2 = listenerNode.bindTag;
                if (obj2 == null || obj2.equals(obj)) {
                    sNotifiers[i].notify(listenerNode, obj, notifyData.updateList);
                }
            } else {
                notifyData.updateList.clear();
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public static void notifyOnUpdate(ListenerNode listenerNode, Object obj, UpdateInfo updateInfo) {
        TransitionListener transitionListener = listenerNode.listener;
        transitionListener.onUpdate(obj, updateInfo.property, updateInfo.getFloatValue(), updateInfo.isCompleted);
        FloatProperty floatProperty = updateInfo.property;
        if (floatProperty instanceof IIntValueProperty) {
            transitionListener.onUpdate(obj, (IIntValueProperty) floatProperty, updateInfo.getIntValue(), updateInfo.velocity, updateInfo.isCompleted);
            return;
        }
        transitionListener.onUpdate(obj, floatProperty, updateInfo.getFloatValue(), updateInfo.velocity, updateInfo.isCompleted);
    }

    /* access modifiers changed from: private */
    public static void removeNode(ListenerNode listenerNode, Object obj) {
        if (listenerNode.toTag.equals(obj)) {
            ListenerNode listenerNode2 = listenerNode.prev;
            listenerNode2.next = listenerNode.next;
            ListenerNode listenerNode3 = listenerNode.next;
            if (listenerNode3 != null) {
                listenerNode3.prev = listenerNode2;
            }
        }
    }

    private void updateNodeInfo(ListenerNode listenerNode, Object obj, AnimConfig animConfig) {
        listenerNode.toTag = obj;
        listenerNode.bindTag = animConfig.tag;
        listenerNode.relatedProperty = animConfig.relatedProperty;
    }

    public void clear() {
        ListenerNode listenerNode = this.mHead.next;
        if (listenerNode != null) {
            listenerNode.prev = null;
        }
        this.mHead.next = null;
        this.mNotifyData.clear();
        this.mPendingList.clear();
        this.mCurNotify = null;
    }

    public void notifyAnimCancel(Object obj, UpdateInfo updateInfo) {
        notifyByType(6, obj, updateInfo, null);
    }

    public void notifyAnimEnd(Object obj, UpdateInfo updateInfo) {
        notifyByType(5, obj, updateInfo, null);
    }

    public void notifyBegin(Object obj) {
        notifyByType(0, obj, null, null);
    }

    public void notifyCancelAll(Object obj) {
        notifyByType(7, obj, null, null);
    }

    public void notifyEndAll(Object obj) {
        notifyByType(8, obj, null, null);
    }

    public void notifyPropertyBegin(Object obj, UpdateInfo updateInfo) {
        notifyByType(1, obj, updateInfo, null);
    }

    public void notifyPropertyEnd(Object obj, List list) {
        notifyByType(4, obj, null, list);
    }

    public void notifyUpdate(Object obj, List list) {
        notifyByType(2, obj, null, list);
    }

    public void notifyUpdateList(Object obj, List list) {
        notifyByType(3, obj, null, list);
    }

    public boolean setListeners(Object obj, AnimConfigLink animConfigLink) {
        for (AnimConfig addNode : animConfigLink.configList) {
            addNode(obj, addNode);
        }
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("setListeners for ");
            sb.append(this.mTarget.getTargetObject());
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("toTag = ");
            sb3.append(obj);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("listeners = ");
            sb4.append(getNodeCount());
            LogUtils.debug(sb2, sb3.toString(), sb4.toString());
        }
        return this.mHead.next != null;
    }
}

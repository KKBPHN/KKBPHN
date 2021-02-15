package miui.animation.internal;

import android.util.ArrayMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import miui.animation.IAnimTarget;
import miui.animation.base.AnimConfigLink;
import miui.animation.controller.AnimState;
import miui.animation.listener.ListenerNotifier;
import miui.animation.listener.UpdateInfo;
import miui.animation.property.FloatProperty;
import miui.animation.property.IIntValueProperty;
import miui.animation.utils.CommonUtils;
import miui.animation.utils.LogUtils;
import miui.animation.utils.ObjectPool;

public class AnimTask {
    private List mNotRunList = new ArrayList();
    private ListenerNotifier mNotifier;
    private List mQueueList = new ArrayList();
    private ConcurrentLinkedQueue mRunningList = new ConcurrentLinkedQueue();
    private ListenerNotifier mSetToNotifier;
    private IAnimTarget mTarget;
    private Map mUpdateMap = new ArrayMap();

    public AnimTask(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
        this.mNotifier = new ListenerNotifier(iAnimTarget);
    }

    private void cancelAnim(AnimRunningInfo animRunningInfo) {
        animRunningInfo.pending = null;
        animRunningInfo.stop();
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("cancelAnim, cancel ");
            sb.append(animRunningInfo.property.getName());
            LogUtils.debug(sb.toString(), new Object[0]);
        }
    }

    private boolean checkAndNotifyEnd(Object obj, boolean z) {
        if (isTagRunningIn(this.mRunningList, obj) || isTagRunningIn(this.mNotRunList, obj)) {
            return false;
        }
        ListenerNotifier listenerNotifier = this.mNotifier;
        if (z) {
            listenerNotifier.notifyCancelAll(obj);
        } else {
            listenerNotifier.notifyEndAll(obj);
        }
        return true;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<miui.animation.internal.AnimRunningInfo>, for r8v0, types: [java.util.Collection, java.util.Collection<miui.animation.internal.AnimRunningInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void collectUpdateInfo(Collection<AnimRunningInfo> collection) {
        for (AnimRunningInfo animRunningInfo : collection) {
            if (!collection.isEmpty()) {
                if (animRunningInfo.status != 0) {
                    UpdateInfo updateToDate = animRunningInfo.updateToDate();
                    if (updateToDate != null) {
                        List updateList = getUpdateList(animRunningInfo.toTag);
                        if (!updateList.contains(updateToDate)) {
                            updateList.add(updateToDate);
                        }
                        if (updateToDate.isCompleted && LogUtils.isLogEnabled()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("anim end, tag = ");
                            sb.append(animRunningInfo.toTag);
                            String sb2 = sb.toString();
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("property = ");
                            sb3.append(animRunningInfo.property.getName());
                            LogUtils.debug(sb2, sb3.toString());
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    private void endQueuedTransitions(boolean z, FloatProperty... floatPropertyArr) {
        ArrayList arrayList = new ArrayList();
        for (TransitionInfo transitionInfo : this.mQueueList) {
            if (endTransitionInfo(transitionInfo, z, floatPropertyArr)) {
                arrayList.add(transitionInfo);
            }
        }
        this.mQueueList.removeAll(arrayList);
    }

    private boolean endTransitionInfo(TransitionInfo transitionInfo, boolean z, FloatProperty... floatPropertyArr) {
        for (FloatProperty floatProperty : transitionInfo.toPropValues.keySet()) {
            if (floatPropertyArr.length <= 0 || !isNotIn(floatProperty, floatPropertyArr)) {
                if (z) {
                    setTargetValue(transitionInfo.target, floatProperty, (Number) transitionInfo.toPropValues.get(floatProperty));
                }
                transitionInfo.toPropValues.remove(floatProperty);
            }
        }
        return transitionInfo.toPropValues.keySet().isEmpty();
    }

    private void findAndHandleSameAnim(AnimRunningInfo animRunningInfo) {
        Iterator it = this.mRunningList.iterator();
        while (it.hasNext()) {
            AnimRunningInfo animRunningInfo2 = (AnimRunningInfo) it.next();
            if (animRunningInfo2 != animRunningInfo && !CommonUtils.hasFlags(animRunningInfo2.flags, 2) && animRunningInfo2.property.equals(animRunningInfo.property)) {
                handleSameAnim(animRunningInfo2, animRunningInfo);
                return;
            }
        }
    }

    private void findByStatus(int i, List list) {
        Iterator it = this.mRunningList.iterator();
        while (it.hasNext()) {
            AnimRunningInfo animRunningInfo = (AnimRunningInfo) it.next();
            if (animRunningInfo.status == i) {
                list.add(animRunningInfo);
            }
        }
    }

    private List getTotalList() {
        Collection collection;
        ArrayList arrayList = new ArrayList();
        if (this.mNotRunList.isEmpty()) {
            collection = this.mRunningList;
        } else {
            arrayList.addAll(this.mRunningList);
            collection = this.mNotRunList;
        }
        arrayList.addAll(collection);
        return arrayList;
    }

    private List getUpdateList(Object obj) {
        List list = (List) this.mUpdateMap.get(obj);
        if (list != null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        this.mUpdateMap.put(obj, arrayList);
        return arrayList;
    }

    private void handleBegin(IAnimTarget iAnimTarget, long j, long j2) {
        List<AnimRunningInfo> list = (List) ObjectPool.acquire(ArrayList.class, new Object[0]);
        List list2 = (List) ObjectPool.acquire(ArrayList.class, new Object[0]);
        try {
            findByStatus(0, list);
            if (!list.isEmpty()) {
                for (AnimRunningInfo animRunningInfo : list) {
                    if (j - animRunningInfo.initTime >= animRunningInfo.config.getDelay(animRunningInfo.toTag, animRunningInfo.property)) {
                        Object obj = animRunningInfo.toTag;
                        if (!list2.contains(obj)) {
                            list2.add(obj);
                            this.mNotifier.notifyBegin(animRunningInfo.toTag);
                        }
                        animRunningInfo.begin(iAnimTarget, j);
                        if (!stopSameRunningAnim(animRunningInfo)) {
                            this.mNotifier.notifyPropertyBegin(animRunningInfo.toTag, animRunningInfo.updateToDate());
                        }
                    }
                }
                updateAndStop(list, j, j2);
                notifyUpdateAndEnd(list);
                ObjectPool.release(list2);
                ObjectPool.release(list);
            }
        } finally {
            ObjectPool.release(list2);
            ObjectPool.release(list);
        }
    }

    private void handlePendingAnim(long j) {
        List<AnimRunningInfo> list = (List) ObjectPool.acquire(ArrayList.class, new Object[0]);
        try {
            findByStatus(2, list);
            if (!list.isEmpty()) {
                for (AnimRunningInfo animRunningInfo : list) {
                    long minDuration = animRunningInfo.config.getMinDuration(animRunningInfo.toTag, animRunningInfo.property);
                    long runningTime = animRunningInfo.getRunningTime();
                    if (runningTime >= minDuration || minDuration - runningTime < j) {
                        animRunningInfo.stop();
                    }
                }
                ObjectPool.release(list);
            }
        } finally {
            ObjectPool.release(list);
        }
    }

    private void handleQueue(long j) {
        if (this.mRunningList.isEmpty() && !this.mQueueList.isEmpty()) {
            startTransition(j, (TransitionInfo) this.mQueueList.remove(0));
        }
    }

    private void handleSameAnim(AnimRunningInfo animRunningInfo, AnimRunningInfo animRunningInfo2) {
        int i = animRunningInfo.status;
        if (i == 0) {
            animRunningInfo.status = 3;
        } else if (i == 1) {
            long minDuration = animRunningInfo.config.getMinDuration(animRunningInfo.toTag, animRunningInfo.property);
            if (animRunningInfo.getRunningTime() < minDuration) {
                if (LogUtils.isLogEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("prev.config.minDuration = ");
                    sb.append(minDuration);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("prev.runningTime = ");
                    sb2.append(animRunningInfo.getRunningTime());
                    LogUtils.debug("handleSameAnim", animRunningInfo.property.getName(), sb.toString(), sb2.toString(), "pending current info");
                }
                animRunningInfo.setPending(animRunningInfo2);
                return;
            }
            animRunningInfo.stop();
        } else if (i == 2) {
            animRunningInfo.pending.stop();
            animRunningInfo.pending = animRunningInfo2;
        }
    }

    private boolean isAnimEnd(AnimRunningInfo animRunningInfo, long j) {
        return animRunningInfo.isAnimEnd(j);
    }

    private boolean isNotIn(FloatProperty floatProperty, FloatProperty... floatPropertyArr) {
        for (FloatProperty equals : floatPropertyArr) {
            if (floatProperty.equals(equals)) {
                return false;
            }
        }
        return true;
    }

    private boolean isRunningTag(AnimRunningInfo animRunningInfo, Object obj) {
        return animRunningInfo.status != 3 && animRunningInfo.toTag.equals(obj);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<miui.animation.internal.AnimRunningInfo>, for r2v0, types: [java.util.Collection, java.util.Collection<miui.animation.internal.AnimRunningInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isTagRunningIn(Collection<AnimRunningInfo> collection, Object obj) {
        for (AnimRunningInfo isRunningTag : collection) {
            if (isRunningTag(isRunningTag, obj)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTransGoing(long j) {
        Iterator it = this.mRunningList.iterator();
        while (it.hasNext()) {
            if (((AnimRunningInfo) it.next()).transId == j) {
                return true;
            }
        }
        return false;
    }

    private void notifyForUpdateAndEnd(ListenerNotifier listenerNotifier, Object obj, List list) {
        listenerNotifier.notifyUpdate(obj, list);
        listenerNotifier.notifyUpdateList(obj, list);
        listenerNotifier.notifyPropertyEnd(obj, list);
    }

    private void notifyUpdateAndEnd(Collection collection) {
        collectUpdateInfo(collection);
        ArrayList<Object> arrayList = new ArrayList<>();
        removeUpdateInfoOfStoppedTrans(arrayList);
        for (Entry entry : this.mUpdateMap.entrySet()) {
            Object key = entry.getKey();
            List list = (List) entry.getValue();
            if (!list.isEmpty()) {
                notifyForUpdateAndEnd(this.mNotifier, key, list);
                if (!checkAndNotifyEnd(key, false)) {
                }
            }
            arrayList.add(key);
        }
        for (Object remove : arrayList) {
            this.mUpdateMap.remove(remove);
        }
    }

    private void removeStoppedAnim() {
        Iterator it = this.mRunningList.iterator();
        while (it.hasNext()) {
            AnimRunningInfo animRunningInfo = (AnimRunningInfo) it.next();
            if (animRunningInfo.status == 3) {
                this.mRunningList.remove(animRunningInfo);
            }
        }
    }

    private void removeUpdateInfoOfStoppedTrans(List list) {
        for (List<UpdateInfo> list2 : this.mUpdateMap.values()) {
            list.clear();
            for (UpdateInfo updateInfo : list2) {
                if (!isTransGoing(updateInfo.transId)) {
                    list.add(updateInfo);
                }
            }
            list2.removeAll(list);
        }
    }

    private void restoreAnim() {
        this.mRunningList.addAll(this.mNotRunList);
        this.mNotRunList.clear();
    }

    private void saveNotAllowedAnim(long... jArr) {
        long j = jArr.length > 0 ? jArr[0] : 0;
        if (j > 0) {
            Iterator it = this.mRunningList.iterator();
            while (it.hasNext()) {
                AnimRunningInfo animRunningInfo = (AnimRunningInfo) it.next();
                if (!CommonUtils.hasFlags(animRunningInfo.flags, j)) {
                    this.mNotRunList.add(animRunningInfo);
                }
            }
            this.mRunningList.removeAll(this.mNotRunList);
        }
    }

    private void setTargetValue(IAnimTarget iAnimTarget, FloatProperty floatProperty, Number number) {
        if (floatProperty instanceof IIntValueProperty) {
            int intValue = number.intValue();
            if (intValue != Integer.MAX_VALUE) {
                iAnimTarget.setIntValue((IIntValueProperty) floatProperty, intValue);
                return;
            }
            return;
        }
        float value = AnimValueUtils.getValue(iAnimTarget, floatProperty, number.floatValue());
        if (value != Float.MAX_VALUE) {
            iAnimTarget.setValue(floatProperty, value);
        }
    }

    private void startTransition(long j, TransitionInfo transitionInfo) {
        ArrayList<FloatProperty> arrayList = new ArrayList<>();
        Iterator it = this.mRunningList.iterator();
        while (it.hasNext()) {
            AnimRunningInfo animRunningInfo = (AnimRunningInfo) it.next();
            if (animRunningInfo.isRunning() && animRunningInfo.toTag.equals(transitionInfo.toTag) && transitionInfo.toPropValues.get(animRunningInfo.property) != null && transitionInfo.config.getDelay(animRunningInfo.toTag, animRunningInfo.property) == 0) {
                tryUpdate(animRunningInfo, transitionInfo, arrayList, j);
            }
        }
        for (FloatProperty remove : arrayList) {
            transitionInfo.toPropValues.remove(remove);
        }
        if (transitionInfo.toPropValues.isEmpty()) {
            if (LogUtils.isLogEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("target = ");
                sb.append(this.mTarget.getTargetObject());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("trans.tag = ");
                sb2.append(transitionInfo.toTag);
                LogUtils.debug("startTransition, trans.toPropValues.isEmpty", sb.toString(), sb2.toString());
            }
            return;
        }
        this.mNotifier.setListeners(transitionInfo.toTag, transitionInfo.config);
        for (AnimRunningInfo animRunningInfo2 : AnimCompositor.createAnimInfo(this.mTarget, transitionInfo).values()) {
            animRunningInfo2.initTime = j;
            if (animRunningInfo2.config.getDelay(animRunningInfo2.toTag, animRunningInfo2.property) > 0) {
                animRunningInfo2.flags |= 2;
            }
            if (!CommonUtils.hasFlags(animRunningInfo2.flags, 2)) {
                findAndHandleSameAnim(animRunningInfo2);
            }
            this.mRunningList.add(animRunningInfo2);
        }
    }

    private boolean stopSameRunningAnim(AnimRunningInfo animRunningInfo) {
        Iterator it = this.mRunningList.iterator();
        boolean z = false;
        while (it.hasNext()) {
            AnimRunningInfo animRunningInfo2 = (AnimRunningInfo) it.next();
            if (animRunningInfo2 != animRunningInfo && animRunningInfo2.property.equals(animRunningInfo.property) && animRunningInfo2.isRunning()) {
                z = true;
                animRunningInfo2.stop();
                this.mRunningList.remove(animRunningInfo2);
            }
        }
        return z;
    }

    private void tryUpdate(AnimRunningInfo animRunningInfo, TransitionInfo transitionInfo, List list, long j) {
        for (FloatProperty floatProperty : transitionInfo.toPropValues.keySet()) {
            if (animRunningInfo.property.equals(floatProperty)) {
                animRunningInfo.update(transitionInfo, j);
                list.add(floatProperty);
                return;
            }
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Collection, code=java.util.Collection<miui.animation.internal.AnimRunningInfo>, for r3v0, types: [java.util.Collection, java.util.Collection<miui.animation.internal.AnimRunningInfo>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateAndStop(Collection<AnimRunningInfo> collection, long j, long j2) {
        for (AnimRunningInfo animRunningInfo : collection) {
            if (animRunningInfo.isRunning()) {
                animRunningInfo.run(j2);
                if (isAnimEnd(animRunningInfo, j)) {
                    animRunningInfo.stop(!animRunningInfo.updateToDate().isCanceled);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void addTransition(long j, TransitionInfo transitionInfo) {
        if (CommonUtils.hasFlags(transitionInfo.config.getFlags(null, null), 1)) {
            this.mQueueList.add(transitionInfo);
        } else {
            startTransition(j, transitionInfo);
        }
    }

    public void cancel(FloatProperty... floatPropertyArr) {
        ArrayList<Object> arrayList = new ArrayList<>();
        boolean z = false;
        for (AnimRunningInfo animRunningInfo : getTotalList()) {
            if (animRunningInfo.isRunning()) {
                if (floatPropertyArr.length <= 0 || !isNotIn(animRunningInfo.property, floatPropertyArr)) {
                    cancelAnim(animRunningInfo);
                    Object obj = animRunningInfo.toTag;
                    if (!arrayList.contains(obj)) {
                        arrayList.add(obj);
                    }
                    this.mNotifier.notifyAnimCancel(obj, animRunningInfo.updateToDate());
                    z = true;
                }
            }
        }
        if (z) {
            removeStoppedAnim();
            for (Object checkAndNotifyEnd : arrayList) {
                checkAndNotifyEnd(checkAndNotifyEnd, true);
            }
            arrayList.clear();
        }
        if (floatPropertyArr.length > 0) {
            endQueuedTransitions(false, floatPropertyArr);
        }
    }

    /* access modifiers changed from: 0000 */
    public void end(FloatProperty... floatPropertyArr) {
        ArrayList<Object> arrayList = new ArrayList<>();
        boolean z = !this.mRunningList.isEmpty();
        for (AnimRunningInfo animRunningInfo : getTotalList()) {
            if (floatPropertyArr.length <= 0 || !isNotIn(animRunningInfo.property, floatPropertyArr)) {
                if (animRunningInfo.isRunning()) {
                    animRunningInfo.stop(true);
                    animRunningInfo.updateToDate().isEndByUser = true;
                } else {
                    setTargetValue(animRunningInfo.target, animRunningInfo.property, animRunningInfo.toValue);
                }
                if (!arrayList.contains(animRunningInfo.toTag)) {
                    arrayList.add(animRunningInfo.toTag);
                }
                this.mNotifier.notifyAnimEnd(animRunningInfo.toTag, animRunningInfo.updateToDate());
            }
        }
        endQueuedTransitions(true, floatPropertyArr);
        removeStoppedAnim();
        if (z) {
            for (Object checkAndNotifyEnd : arrayList) {
                checkAndNotifyEnd(checkAndNotifyEnd, false);
            }
        }
        arrayList.clear();
    }

    public boolean isFinished() {
        return this.mRunningList.isEmpty();
    }

    public boolean isRunning(FloatProperty floatProperty) {
        Iterator it = this.mRunningList.iterator();
        while (it.hasNext()) {
            if (((AnimRunningInfo) it.next()).property.equals(floatProperty)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid() {
        return !this.mRunningList.isEmpty() || !this.mQueueList.isEmpty();
    }

    public void run(long j, long j2, long... jArr) {
        saveNotAllowedAnim(jArr);
        if (!this.mRunningList.isEmpty()) {
            long j3 = j;
            long j4 = j2;
            updateAndStop(this.mRunningList, j3, j4);
            handlePendingAnim(j2);
            notifyUpdateAndEnd(this.mRunningList);
            removeStoppedAnim();
            handleBegin(this.mTarget, j3, j4);
            handleQueue(j);
        }
        restoreAnim();
    }

    public void setToNotify(AnimState animState, AnimConfigLink animConfigLink) {
        Object tag = animState.getTag();
        if (this.mSetToNotifier == null) {
            this.mSetToNotifier = new ListenerNotifier(this.mTarget);
        }
        if (this.mSetToNotifier.setListeners(tag, animConfigLink)) {
            this.mSetToNotifier.notifyBegin(tag);
            ArrayList<UpdateInfo> arrayList = new ArrayList<>();
            for (FloatProperty floatProperty : animState.keySet()) {
                UpdateInfo updateInfo = new UpdateInfo();
                updateInfo.property = floatProperty;
                updateInfo.velocity = (float) this.mTarget.getVelocity(floatProperty);
                updateInfo.setValue(Float.valueOf(floatProperty instanceof IIntValueProperty ? (float) this.mTarget.getIntValue((IIntValueProperty) floatProperty) : this.mTarget.getValue(floatProperty)));
                arrayList.add(updateInfo);
                updateInfo.setComplete(true);
            }
            for (UpdateInfo notifyPropertyBegin : arrayList) {
                this.mSetToNotifier.notifyPropertyBegin(tag, notifyPropertyBegin);
            }
            notifyForUpdateAndEnd(this.mSetToNotifier, tag, arrayList);
            this.mSetToNotifier.notifyEndAll(tag);
        }
    }
}

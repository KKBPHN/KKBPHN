package miuix.animation;

import android.content.Context;
import android.provider.Settings.Global;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import miuix.animation.controller.FolmeFont;
import miuix.animation.controller.FolmeState;
import miuix.animation.controller.FolmeTouch;
import miuix.animation.controller.FolmeVisible;
import miuix.animation.controller.ListViewTouchListener;
import miuix.animation.utils.CommonUtils;
import miuix.folme.R;

public class Folme {
    public static final long FLAG_IN_TOUCH = 4;
    public static final long FLAG_PARALLEL = 2;
    public static final long FLAG_QUEUE = 1;
    private static final ConcurrentHashMap sImplMap = new ConcurrentHashMap();
    private static AtomicReference sTimeRatio = new AtomicReference(Float.valueOf(1.0f));

    class FolmeImpl implements IFolme {
        private IStateStyle mState;
        private IAnimTarget[] mTargets;
        private ITouchStyle mTouch;
        private IVisibleStyle mVisible;

        private FolmeImpl(IAnimTarget... iAnimTargetArr) {
            this.mTargets = iAnimTargetArr;
        }

        /* access modifiers changed from: 0000 */
        public void clean() {
            ITouchStyle iTouchStyle = this.mTouch;
            if (iTouchStyle != null) {
                iTouchStyle.clean();
            }
            IVisibleStyle iVisibleStyle = this.mVisible;
            if (iVisibleStyle != null) {
                iVisibleStyle.clean();
            }
            IStateStyle iStateStyle = this.mState;
            if (iStateStyle != null) {
                iStateStyle.clean();
            }
        }

        /* access modifiers changed from: 0000 */
        public void end() {
            ITouchStyle iTouchStyle = this.mTouch;
            if (iTouchStyle != null) {
                iTouchStyle.end(new Object[0]);
            }
            IVisibleStyle iVisibleStyle = this.mVisible;
            if (iVisibleStyle != null) {
                iVisibleStyle.end(new Object[0]);
            }
            IStateStyle iStateStyle = this.mState;
            if (iStateStyle != null) {
                iStateStyle.end(new Object[0]);
            }
        }

        public IStateStyle state() {
            if (this.mState == null) {
                this.mState = FolmeState.composeStyle(this.mTargets);
            }
            return this.mState;
        }

        public ITouchStyle touch() {
            if (this.mTouch == null) {
                FolmeTouch folmeTouch = new FolmeTouch(this.mTargets);
                folmeTouch.setFontStyle(new FolmeFont());
                this.mTouch = folmeTouch;
            }
            return this.mTouch;
        }

        public IVisibleStyle visible() {
            if (this.mVisible == null) {
                this.mVisible = new FolmeVisible(this.mTargets);
            }
            return this.mVisible;
        }
    }

    public interface FontType {
        public static final int MITYPE = 1;
        public static final int MITYPE_MONO = 2;
        public static final int MIUI = 0;
    }

    public interface FontWeight {
        public static final int BOLD = 8;
        public static final int DEMI_BOLD = 6;
        public static final int EXTRA_LIGHT = 1;
        public static final int HEAVY = 9;
        public static final int LIGHT = 2;
        public static final int MEDIUM = 5;
        public static final int NORMAL = 3;
        public static final int REGULAR = 4;
        public static final int SEMI_BOLD = 7;
        public static final int THIN = 0;
    }

    @SafeVarargs
    public static void clean(Object... objArr) {
        if (CommonUtils.isArrayEmpty(objArr)) {
            Iterator it = new ArrayList(sImplMap.keySet()).iterator();
            while (it.hasNext()) {
                cleanAnimTarget((IAnimTarget) it.next());
            }
            return;
        }
        for (Object doClean : objArr) {
            doClean(doClean);
        }
    }

    private static void cleanAnimTarget(IAnimTarget iAnimTarget) {
        if (iAnimTarget != null) {
            FolmeImpl folmeImpl = (FolmeImpl) sImplMap.remove(iAnimTarget);
            if (folmeImpl != null) {
                folmeImpl.clean();
            }
        }
    }

    private static void doClean(Object obj) {
        cleanAnimTarget(getTarget(obj, null));
    }

    public static void end(Object... objArr) {
        for (Object target : objArr) {
            IAnimTarget target2 = getTarget(target, null);
            if (target2 != null) {
                FolmeImpl folmeImpl = (FolmeImpl) sImplMap.get(target2);
                if (folmeImpl != null) {
                    folmeImpl.end();
                }
            }
        }
    }

    private static FolmeImpl fillTargetArrayAndGetImpl(View[] viewArr, IAnimTarget[] iAnimTargetArr) {
        FolmeImpl folmeImpl = null;
        boolean z = false;
        for (int i = 0; i < viewArr.length; i++) {
            iAnimTargetArr[i] = getTarget(viewArr[i], ViewTarget.sCreator);
            FolmeImpl folmeImpl2 = (FolmeImpl) sImplMap.get(iAnimTargetArr[i]);
            if (folmeImpl == null) {
                folmeImpl = folmeImpl2;
            } else if (folmeImpl != folmeImpl2) {
                z = true;
            }
        }
        if (z) {
            return null;
        }
        return folmeImpl;
    }

    public static IAnimTarget getTarget(Object obj, ITargetCreator iTargetCreator) {
        if (obj instanceof IAnimTarget) {
            return (IAnimTarget) obj;
        }
        HashSet hashSet = new HashSet();
        getTargets(hashSet);
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            IAnimTarget iAnimTarget = (IAnimTarget) it.next();
            Object targetObject = iAnimTarget.getTargetObject();
            if (targetObject != null && targetObject.equals(obj)) {
                return iAnimTarget;
            }
        }
        if (iTargetCreator != null) {
            IAnimTarget createTarget = iTargetCreator.createTarget(obj);
            if (createTarget != null) {
                useAt(createTarget);
                return createTarget;
            }
        }
        return null;
    }

    public static void getTargets(Collection collection) {
        for (IAnimTarget iAnimTarget : sImplMap.keySet()) {
            if (iAnimTarget.isValid()) {
                collection.add(iAnimTarget);
            } else {
                sImplMap.remove(iAnimTarget);
            }
        }
    }

    public static float getTimeRatio() {
        return ((Float) sTimeRatio.get()).floatValue();
    }

    public static ValueTarget getValueTarget(Object obj) {
        return (ValueTarget) getTarget(obj, ValueTarget.sCreator);
    }

    public static boolean isInDraggingState(View view) {
        return view.getTag(R.id.miuix_animation_tag_is_dragging) != null;
    }

    public static void onListViewTouchEvent(AbsListView absListView, MotionEvent motionEvent) {
        ListViewTouchListener listViewTouchListener = FolmeTouch.getListViewTouchListener(absListView);
        if (listViewTouchListener != null) {
            listViewTouchListener.onTouch(absListView, motionEvent);
        }
    }

    public static void post(Object obj, Runnable runnable) {
        IAnimTarget target = getTarget(obj, null);
        if (target != null) {
            target.post(runnable);
        }
    }

    public static void setAnimPlayRatio(float f) {
        sTimeRatio.set(Float.valueOf(f));
    }

    public static void setDraggingState(View view, boolean z) {
        int i;
        Boolean bool;
        if (z) {
            i = R.id.miuix_animation_tag_is_dragging;
            bool = Boolean.valueOf(true);
        } else {
            i = R.id.miuix_animation_tag_is_dragging;
            bool = null;
        }
        view.setTag(i, bool);
    }

    public static IFolme useAt(IAnimTarget iAnimTarget) {
        FolmeImpl folmeImpl = (FolmeImpl) sImplMap.get(iAnimTarget);
        if (folmeImpl != null) {
            return folmeImpl;
        }
        FolmeImpl folmeImpl2 = new FolmeImpl(new IAnimTarget[]{iAnimTarget});
        sImplMap.put(iAnimTarget, folmeImpl2);
        return folmeImpl2;
    }

    public static IFolme useAt(View... viewArr) {
        if (viewArr.length != 0) {
            if (viewArr.length == 1) {
                return useAt(getTarget(viewArr[0], ViewTarget.sCreator));
            }
            IAnimTarget[] iAnimTargetArr = new IAnimTarget[viewArr.length];
            FolmeImpl fillTargetArrayAndGetImpl = fillTargetArrayAndGetImpl(viewArr, iAnimTargetArr);
            if (fillTargetArrayAndGetImpl == null) {
                fillTargetArrayAndGetImpl = new FolmeImpl(iAnimTargetArr);
                for (IAnimTarget put : iAnimTargetArr) {
                    sImplMap.put(put, fillTargetArrayAndGetImpl);
                }
            }
            return fillTargetArrayAndGetImpl;
        }
        throw new IllegalArgumentException("useAt can not be applied to empty views array");
    }

    public static void useSystemAnimatorDurationScale(Context context) {
        sTimeRatio.set(Float.valueOf(Global.getFloat(context.getContentResolver(), "animator_duration_scale", 1.0f)));
    }

    public static IStateStyle useValue(Object... objArr) {
        IAnimTarget iAnimTarget;
        if (objArr.length > 0) {
            iAnimTarget = getTarget(objArr[0], ValueTarget.sCreator);
        } else {
            iAnimTarget = new ValueTarget();
            iAnimTarget.setFlags(1);
        }
        return useAt(iAnimTarget).state();
    }

    public static IVarFontStyle useVarFontAt(TextView textView, int i, int i2) {
        return new FolmeFont().useAt(textView, i, i2);
    }
}

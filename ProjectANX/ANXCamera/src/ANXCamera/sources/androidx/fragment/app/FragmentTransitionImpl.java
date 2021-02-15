package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressLint({"UnknownNullness"})
@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public abstract class FragmentTransitionImpl {
    protected static void bfsAddViewChildren(List list, View view) {
        int size = list.size();
        if (!containedBeforeIndex(list, view, size)) {
            list.add(view);
            for (int i = size; i < list.size(); i++) {
                View view2 = (View) list.get(i);
                if (view2 instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view2;
                    int childCount = viewGroup.getChildCount();
                    for (int i2 = 0; i2 < childCount; i2++) {
                        View childAt = viewGroup.getChildAt(i2);
                        if (!containedBeforeIndex(list, childAt, size)) {
                            list.add(childAt);
                        }
                    }
                }
            }
        }
    }

    private static boolean containedBeforeIndex(List list, View view, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (list.get(i2) == view) {
                return true;
            }
        }
        return false;
    }

    static String findKeyForValue(Map map, String str) {
        for (Entry entry : map.entrySet()) {
            if (str.equals(entry.getValue())) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    protected static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public abstract void addTarget(Object obj, View view);

    public abstract void addTargets(Object obj, ArrayList arrayList);

    public abstract void beginDelayedTransition(ViewGroup viewGroup, Object obj);

    public abstract boolean canHandle(Object obj);

    /* JADX WARNING: type inference failed for: r5v0, types: [android.view.View] */
    /* JADX WARNING: type inference failed for: r5v1, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r5v2, types: [android.view.ViewGroup] */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=android.view.View, code=null, for r5v0, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r5v2, types: [android.view.ViewGroup]
  assigns: [android.view.ViewGroup, android.view.View]
  uses: [android.view.ViewGroup, java.lang.Object, android.view.View, ?[OBJECT, ARRAY]]
  mth insns count: 17
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void captureTransitioningViews(ArrayList arrayList, View r5) {
        if (r5.getVisibility() == 0) {
            if (r5 instanceof ViewGroup) {
                r5 = (ViewGroup) r5;
                if (!ViewGroupCompat.isTransitionGroup(r5)) {
                    int childCount = r5.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        captureTransitioningViews(arrayList, r5.getChildAt(i));
                    }
                    return;
                }
            }
            arrayList.add(r5);
        }
    }

    public abstract Object cloneTransition(Object obj);

    /* access modifiers changed from: 0000 */
    public void findNamedViews(Map map, @NonNull View view) {
        if (view.getVisibility() == 0) {
            String transitionName = ViewCompat.getTransitionName(view);
            if (transitionName != null) {
                map.put(transitionName, view);
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    findNamedViews(map, viewGroup.getChildAt(i));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void getBoundsOnScreen(View view, Rect rect) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        rect.set(iArr[0], iArr[1], iArr[0] + view.getWidth(), iArr[1] + view.getHeight());
    }

    public abstract Object mergeTransitionsInSequence(Object obj, Object obj2, Object obj3);

    public abstract Object mergeTransitionsTogether(Object obj, Object obj2, Object obj3);

    /* access modifiers changed from: 0000 */
    public ArrayList prepareSetNameOverridesReordered(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            View view = (View) arrayList.get(i);
            arrayList2.add(ViewCompat.getTransitionName(view));
            ViewCompat.setTransitionName(view, null);
        }
        return arrayList2;
    }

    public abstract void removeTarget(Object obj, View view);

    public abstract void replaceTargets(Object obj, ArrayList arrayList, ArrayList arrayList2);

    public abstract void scheduleHideFragmentView(Object obj, View view, ArrayList arrayList);

    /* access modifiers changed from: 0000 */
    public void scheduleNameReset(ViewGroup viewGroup, final ArrayList arrayList, final Map map) {
        OneShotPreDrawListener.add(viewGroup, new Runnable() {
            public void run() {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    View view = (View) arrayList.get(i);
                    ViewCompat.setTransitionName(view, (String) map.get(ViewCompat.getTransitionName(view)));
                }
            }
        });
    }

    public abstract void scheduleRemoveTargets(Object obj, Object obj2, ArrayList arrayList, Object obj3, ArrayList arrayList2, Object obj4, ArrayList arrayList3);

    public abstract void setEpicenter(Object obj, Rect rect);

    public abstract void setEpicenter(Object obj, View view);

    /* access modifiers changed from: 0000 */
    public void setNameOverridesOrdered(View view, final ArrayList arrayList, final Map map) {
        OneShotPreDrawListener.add(view, new Runnable() {
            public void run() {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    View view = (View) arrayList.get(i);
                    String transitionName = ViewCompat.getTransitionName(view);
                    if (transitionName != null) {
                        ViewCompat.setTransitionName(view, FragmentTransitionImpl.findKeyForValue(map, transitionName));
                    }
                }
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void setNameOverridesReordered(View view, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, Map map) {
        final int size = arrayList2.size();
        final ArrayList arrayList4 = new ArrayList();
        for (int i = 0; i < size; i++) {
            View view2 = (View) arrayList.get(i);
            String transitionName = ViewCompat.getTransitionName(view2);
            arrayList4.add(transitionName);
            if (transitionName != null) {
                ViewCompat.setTransitionName(view2, null);
                String str = (String) map.get(transitionName);
                int i2 = 0;
                while (true) {
                    if (i2 >= size) {
                        break;
                    } else if (str.equals(arrayList3.get(i2))) {
                        ViewCompat.setTransitionName((View) arrayList2.get(i2), transitionName);
                        break;
                    } else {
                        i2++;
                    }
                }
            }
        }
        final ArrayList arrayList5 = arrayList2;
        final ArrayList arrayList6 = arrayList3;
        final ArrayList arrayList7 = arrayList;
        AnonymousClass1 r0 = new Runnable() {
            public void run() {
                for (int i = 0; i < size; i++) {
                    ViewCompat.setTransitionName((View) arrayList5.get(i), (String) arrayList6.get(i));
                    ViewCompat.setTransitionName((View) arrayList7.get(i), (String) arrayList4.get(i));
                }
            }
        };
        OneShotPreDrawListener.add(view, r0);
    }

    public abstract void setSharedElementTargets(Object obj, View view, ArrayList arrayList);

    public abstract void swapSharedElementTargets(Object obj, ArrayList arrayList, ArrayList arrayList2);

    public abstract Object wrapTransitionInSet(Object obj);
}

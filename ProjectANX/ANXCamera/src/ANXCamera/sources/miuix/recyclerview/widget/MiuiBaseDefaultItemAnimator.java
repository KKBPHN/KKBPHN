package miuix.recyclerview.widget;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.ItemAnimator.ItemHolderInfo;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.SimpleItemAnimator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miuix.animation.Folme;

public abstract class MiuiBaseDefaultItemAnimator extends SimpleItemAnimator {
    private static final long ADD_DELAY = 50;
    private static final long REMOVE_DELAY = 100;
    private static final List sEmptyAddList = new ArrayList();
    private static final List sEmptyChangeList = new ArrayList();
    private static final List sEmptyMoveList = new ArrayList();
    private ArrayList mAddAnimations = new ArrayList();
    private ArrayList mAdditionsList = new ArrayList();
    private ArrayList mChangeAnimations = new ArrayList();
    private ArrayList mChangesList = new ArrayList();
    private ArrayList mMoveAnimations = new ArrayList();
    private ArrayList mMovesList = new ArrayList();
    private ArrayList mPendingAdditions = new ArrayList();
    private ArrayList mPendingChanges = new ArrayList();
    private ArrayList mPendingMoves = new ArrayList();
    private ArrayList mPendingRemovals = new ArrayList();
    private ArrayList mRemoveAnimations = new ArrayList();

    class ChangeInfo {
        int fromX;
        int fromY;
        ViewHolder newHolder;
        ViewHolder oldHolder;
        int toX;
        int toY;

        private ChangeInfo(ViewHolder viewHolder, ViewHolder viewHolder2) {
            this.oldHolder = viewHolder;
            this.newHolder = viewHolder2;
        }

        ChangeInfo(ViewHolder viewHolder, ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
            this(viewHolder, viewHolder2);
            this.fromX = i;
            this.fromY = i2;
            this.toX = i3;
            this.toY = i4;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ChangeInfo{oldHolder=");
            sb.append(this.oldHolder);
            sb.append(", newHolder=");
            sb.append(this.newHolder);
            sb.append(", fromX=");
            sb.append(this.fromX);
            sb.append(", fromY=");
            sb.append(this.fromY);
            sb.append(", toX=");
            sb.append(this.toX);
            sb.append(", toY=");
            sb.append(this.toY);
            sb.append('}');
            return sb.toString();
        }
    }

    class MoveInfo {
        int fromX;
        int fromY;
        ViewHolder holder;
        int toX;
        int toY;

        MoveInfo(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            this.holder = viewHolder;
            this.fromX = i;
            this.fromY = i2;
            this.toX = i3;
            this.toY = i4;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MoveInfo{holder=");
            sb.append(this.holder);
            sb.append(", fromX=");
            sb.append(this.fromX);
            sb.append(", fromY=");
            sb.append(this.fromY);
            sb.append(", toX=");
            sb.append(this.toX);
            sb.append(", toY=");
            sb.append(this.toY);
            sb.append('}');
            return sb.toString();
        }
    }

    private void cancelAll(List list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            cancelAnimate(((ViewHolder) list.get(size)).itemView);
        }
        list.clear();
    }

    private void cancelAnimate(View view) {
        Folme.end(view);
    }

    private void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }

    /* access modifiers changed from: private */
    public void doOtherAnimations() {
        List<MoveInfo> list = this.mMovesList.isEmpty() ? sEmptyMoveList : (List) this.mMovesList.remove(0);
        List<ChangeInfo> list2 = this.mChangesList.isEmpty() ? sEmptyChangeList : (List) this.mChangesList.remove(0);
        final List list3 = this.mAdditionsList.isEmpty() ? sEmptyAddList : (List) this.mAdditionsList.remove(0);
        for (MoveInfo animateMoveImpl : list) {
            animateMoveImpl(animateMoveImpl);
        }
        for (ChangeInfo animateChangeImpl : list2) {
            animateChangeImpl(animateChangeImpl);
        }
        if (!list3.isEmpty()) {
            AnonymousClass2 r4 = new Runnable() {
                public void run() {
                    for (ViewHolder animateAddImpl : list3) {
                        MiuiBaseDefaultItemAnimator.this.animateAddImpl(animateAddImpl);
                    }
                }
            };
            if (!list.isEmpty() || !list2.isEmpty()) {
                ((ViewHolder) list3.get(0)).itemView.postDelayed(r4, ADD_DELAY);
            } else {
                r4.run();
            }
        }
    }

    private void endChangeAnimation(List list, ViewHolder viewHolder) {
        for (int size = list.size() - 1; size >= 0; size--) {
            ChangeInfo changeInfo = (ChangeInfo) list.get(size);
            if (endChangeAnimationIfNecessary(changeInfo, viewHolder) && changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                list.remove(changeInfo);
            }
        }
    }

    private void endChangeAnimationIfNecessary(ChangeInfo changeInfo) {
        ViewHolder viewHolder = changeInfo.oldHolder;
        if (viewHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, viewHolder);
        }
        ViewHolder viewHolder2 = changeInfo.newHolder;
        if (viewHolder2 != null) {
            endChangeAnimationIfNecessary(changeInfo, viewHolder2);
        }
    }

    private boolean endChangeAnimationIfNecessary(ChangeInfo changeInfo, ViewHolder viewHolder) {
        boolean z = false;
        if (changeInfo.newHolder == viewHolder) {
            changeInfo.newHolder = null;
        } else if (changeInfo.oldHolder != viewHolder) {
            return false;
        } else {
            changeInfo.oldHolder = null;
            z = true;
        }
        viewHolder.itemView.setAlpha(1.0f);
        viewHolder.itemView.setTranslationX(0.0f);
        viewHolder.itemView.setTranslationY(0.0f);
        dispatchChangeFinished(viewHolder, z);
        return true;
    }

    static void resetAnimation(View view) {
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
        view.setAlpha(1.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
    }

    public boolean animateAdd(ViewHolder viewHolder) {
        prepareAdd(viewHolder);
        this.mPendingAdditions.add(viewHolder);
        return true;
    }

    public abstract void animateAddImpl(ViewHolder viewHolder);

    public boolean animateAppearance(@NonNull ViewHolder viewHolder, @Nullable ItemHolderInfo itemHolderInfo, @NonNull ItemHolderInfo itemHolderInfo2) {
        if (itemHolderInfo == null || (itemHolderInfo.left == itemHolderInfo2.left && itemHolderInfo.top == itemHolderInfo2.top)) {
            return animateAdd(viewHolder);
        }
        return animateMove(viewHolder, itemHolderInfo.left, itemHolderInfo.top, itemHolderInfo2.left, itemHolderInfo2.top);
    }

    public boolean animateChange(ViewHolder viewHolder, ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
        if (viewHolder == viewHolder2) {
            return animateMove(viewHolder2, i, i2, i3, i4);
        }
        ChangeInfo changeInfo = new ChangeInfo(viewHolder, viewHolder2, i, i2, i3, i4);
        prepareChange(changeInfo);
        animateChangeImpl(changeInfo);
        return true;
    }

    public abstract void animateChangeImpl(ChangeInfo changeInfo);

    public boolean animateMove(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
        resetAnimation(viewHolder);
        int i5 = i4 - i2;
        if (i3 - i == 0 && i5 == 0) {
            dispatchMoveFinished(viewHolder);
            return false;
        }
        MoveInfo moveInfo = new MoveInfo(viewHolder, i, i2, i3, i4);
        prepareMove(moveInfo);
        this.mPendingMoves.add(moveInfo);
        return true;
    }

    public abstract void animateMoveImpl(MoveInfo moveInfo);

    public boolean animateRemove(ViewHolder viewHolder) {
        resetAnimation(viewHolder);
        this.mPendingRemovals.add(viewHolder);
        return true;
    }

    public abstract void animateRemoveImpl(ViewHolder viewHolder);

    public boolean canReuseUpdatedViewHolder(@NonNull ViewHolder viewHolder, @NonNull List list) {
        return !list.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, list);
    }

    public void endAnimation(@NonNull ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        cancelAnimate(view);
        int size = this.mPendingMoves.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            } else if (((MoveInfo) this.mPendingMoves.get(size)).holder == viewHolder) {
                view.setTranslationY(0.0f);
                view.setTranslationX(0.0f);
                dispatchMoveFinished(viewHolder);
                this.mPendingMoves.remove(size);
            }
        }
        endChangeAnimation(this.mPendingChanges, viewHolder);
        if (this.mPendingRemovals.remove(viewHolder)) {
            view.setAlpha(1.0f);
            dispatchRemoveFinished(viewHolder);
        }
        if (this.mPendingAdditions.remove(viewHolder)) {
            view.setAlpha(1.0f);
            dispatchAddFinished(viewHolder);
        }
        for (int size2 = this.mChangesList.size() - 1; size2 >= 0; size2--) {
            ArrayList arrayList = (ArrayList) this.mChangesList.get(size2);
            endChangeAnimation(arrayList, viewHolder);
            if (arrayList.isEmpty()) {
                this.mChangesList.remove(size2);
            }
        }
        for (int size3 = this.mMovesList.size() - 1; size3 >= 0; size3--) {
            ArrayList arrayList2 = (ArrayList) this.mMovesList.get(size3);
            int size4 = arrayList2.size() - 1;
            while (true) {
                if (size4 < 0) {
                    break;
                } else if (((MoveInfo) arrayList2.get(size4)).holder == viewHolder) {
                    view.setTranslationY(0.0f);
                    view.setTranslationX(0.0f);
                    dispatchMoveFinished(viewHolder);
                    arrayList2.remove(size4);
                    if (arrayList2.isEmpty()) {
                        this.mMovesList.remove(size3);
                    }
                } else {
                    size4--;
                }
            }
        }
        for (int size5 = this.mAdditionsList.size() - 1; size5 >= 0; size5--) {
            ArrayList arrayList3 = (ArrayList) this.mAdditionsList.get(size5);
            if (arrayList3.remove(viewHolder)) {
                view.setAlpha(1.0f);
                dispatchAddFinished(viewHolder);
                if (arrayList3.isEmpty()) {
                    this.mAdditionsList.remove(size5);
                }
            }
        }
        this.mRemoveAnimations.remove(viewHolder);
        this.mAddAnimations.remove(viewHolder);
        this.mChangeAnimations.remove(viewHolder);
        this.mMoveAnimations.remove(viewHolder);
        dispatchFinishedWhenDone();
    }

    public void endAnimations() {
        int size = this.mPendingMoves.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            MoveInfo moveInfo = (MoveInfo) this.mPendingMoves.get(size);
            View view = moveInfo.holder.itemView;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            dispatchMoveFinished(moveInfo.holder);
            this.mPendingMoves.remove(size);
        }
        for (int size2 = this.mPendingRemovals.size() - 1; size2 >= 0; size2--) {
            dispatchRemoveFinished((ViewHolder) this.mPendingRemovals.get(size2));
            this.mPendingRemovals.remove(size2);
        }
        int size3 = this.mPendingAdditions.size();
        while (true) {
            size3--;
            if (size3 < 0) {
                break;
            }
            ViewHolder viewHolder = (ViewHolder) this.mPendingAdditions.get(size3);
            viewHolder.itemView.setAlpha(1.0f);
            dispatchAddFinished(viewHolder);
            this.mPendingAdditions.remove(size3);
        }
        for (int size4 = this.mPendingChanges.size() - 1; size4 >= 0; size4--) {
            endChangeAnimationIfNecessary((ChangeInfo) this.mPendingChanges.get(size4));
        }
        this.mPendingChanges.clear();
        if (isRunning()) {
            for (int size5 = this.mMovesList.size() - 1; size5 >= 0; size5--) {
                ArrayList arrayList = (ArrayList) this.mMovesList.get(size5);
                for (int size6 = arrayList.size() - 1; size6 >= 0; size6--) {
                    MoveInfo moveInfo2 = (MoveInfo) arrayList.get(size6);
                    View view2 = moveInfo2.holder.itemView;
                    view2.setTranslationY(0.0f);
                    view2.setTranslationX(0.0f);
                    dispatchMoveFinished(moveInfo2.holder);
                    arrayList.remove(size6);
                    if (arrayList.isEmpty()) {
                        this.mMovesList.remove(arrayList);
                    }
                }
            }
            for (int size7 = this.mAdditionsList.size() - 1; size7 >= 0; size7--) {
                ArrayList arrayList2 = (ArrayList) this.mAdditionsList.get(size7);
                for (int size8 = arrayList2.size() - 1; size8 >= 0; size8--) {
                    ViewHolder viewHolder2 = (ViewHolder) arrayList2.get(size8);
                    viewHolder2.itemView.setAlpha(1.0f);
                    dispatchAddFinished(viewHolder2);
                    arrayList2.remove(size8);
                    if (arrayList2.isEmpty()) {
                        this.mAdditionsList.remove(arrayList2);
                    }
                }
            }
            for (int size9 = this.mChangesList.size() - 1; size9 >= 0; size9--) {
                ArrayList arrayList3 = (ArrayList) this.mChangesList.get(size9);
                for (int size10 = arrayList3.size() - 1; size10 >= 0; size10--) {
                    endChangeAnimationIfNecessary((ChangeInfo) arrayList3.get(size10));
                    if (arrayList3.isEmpty()) {
                        this.mChangesList.remove(arrayList3);
                    }
                }
            }
            cancelAll(this.mRemoveAnimations);
            cancelAll(this.mMoveAnimations);
            cancelAll(this.mAddAnimations);
            cancelAll(this.mChangeAnimations);
            dispatchAnimationsFinished();
        }
    }

    public boolean isRunning() {
        return !this.mPendingAdditions.isEmpty() || !this.mPendingChanges.isEmpty() || !this.mPendingMoves.isEmpty() || !this.mPendingRemovals.isEmpty() || !this.mMoveAnimations.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mAddAnimations.isEmpty() || !this.mChangeAnimations.isEmpty() || !this.mMovesList.isEmpty() || !this.mAdditionsList.isEmpty() || !this.mChangesList.isEmpty();
    }

    /* access modifiers changed from: 0000 */
    public void notifyAddFinished(ViewHolder viewHolder) {
        dispatchAddFinished(viewHolder);
        this.mAddAnimations.remove(viewHolder);
        dispatchFinishedWhenDone();
    }

    /* access modifiers changed from: 0000 */
    public void notifyAddStarting(ViewHolder viewHolder) {
        this.mAddAnimations.add(viewHolder);
        dispatchAddStarting(viewHolder);
    }

    /* access modifiers changed from: 0000 */
    public void notifyChangeFinished(ViewHolder viewHolder, boolean z) {
        dispatchChangeFinished(viewHolder, z);
        this.mChangeAnimations.remove(viewHolder);
        dispatchFinishedWhenDone();
    }

    /* access modifiers changed from: 0000 */
    public void notifyChangeStarting(ViewHolder viewHolder, boolean z) {
        this.mChangeAnimations.add(viewHolder);
        dispatchChangeStarting(viewHolder, z);
    }

    /* access modifiers changed from: 0000 */
    public void notifyMoveFinished(ViewHolder viewHolder) {
        dispatchMoveFinished(viewHolder);
        this.mMoveAnimations.remove(viewHolder);
        dispatchFinishedWhenDone();
    }

    /* access modifiers changed from: 0000 */
    public void notifyMoveStarting(ViewHolder viewHolder) {
        this.mMoveAnimations.add(viewHolder);
        dispatchMoveStarting(viewHolder);
    }

    /* access modifiers changed from: 0000 */
    public void notifyRemoveFinished(ViewHolder viewHolder) {
        dispatchRemoveFinished(viewHolder);
        this.mRemoveAnimations.remove(viewHolder);
        dispatchFinishedWhenDone();
    }

    /* access modifiers changed from: 0000 */
    public void notifyRemoveStarting(ViewHolder viewHolder) {
        this.mRemoveAnimations.add(viewHolder);
        dispatchRemoveStarting(viewHolder);
    }

    public abstract void prepareAdd(ViewHolder viewHolder);

    public abstract void prepareChange(ChangeInfo changeInfo);

    public abstract void prepareMove(MoveInfo moveInfo);

    /* access modifiers changed from: 0000 */
    public void resetAnimation(ViewHolder viewHolder) {
        endAnimation(viewHolder);
        resetAnimation(viewHolder.itemView);
    }

    public void runPendingAnimations() {
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z3 || z4) {
            this.mMovesList.add(new ArrayList(this.mPendingMoves));
            this.mPendingMoves.clear();
            this.mChangesList.add(new ArrayList(this.mPendingChanges));
            this.mPendingChanges.clear();
            this.mAdditionsList.add(new ArrayList(this.mPendingAdditions));
            this.mPendingAdditions.clear();
            AnonymousClass1 r1 = new Runnable() {
                public void run() {
                    MiuiBaseDefaultItemAnimator.this.doOtherAnimations();
                }
            };
            if (z) {
                Iterator it = this.mPendingRemovals.iterator();
                while (it.hasNext()) {
                    animateRemoveImpl((ViewHolder) it.next());
                }
                ((ViewHolder) this.mPendingRemovals.get(0)).itemView.postDelayed(r1, REMOVE_DELAY);
                this.mPendingRemovals.clear();
            } else {
                r1.run();
            }
        }
    }
}

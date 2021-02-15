package miuix.recyclerview.widget;

import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.property.ViewProperty;

public class MiuiDefaultItemAnimator extends MiuiBaseDefaultItemAnimator {
    public static OnAttachStateChangeListener sAttachedListener = new OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View view) {
            MiuiBaseDefaultItemAnimator.resetAnimation(view);
        }

        public void onViewDetachedFromWindow(View view) {
        }
    };
    public static AnimConfig sSpeedConfig = new AnimConfig().setFromSpeed(0.0f);

    /* access modifiers changed from: 0000 */
    public void animateAddImpl(final ViewHolder viewHolder) {
        notifyAddStarting(viewHolder);
        IStateStyle state = Folme.useAt(viewHolder.itemView).state();
        Float valueOf = Float.valueOf(1.0f);
        state.to(ViewProperty.ALPHA, valueOf, sSpeedConfig);
        viewHolder.itemView.postDelayed(new Runnable() {
            public void run() {
                MiuiDefaultItemAnimator.this.notifyAddFinished(viewHolder);
            }
        }, Folme.useAt(viewHolder.itemView).state().predictDuration(ViewProperty.ALPHA, valueOf));
    }

    /* access modifiers changed from: 0000 */
    public void animateChangeImpl(ChangeInfo changeInfo) {
        final ViewHolder viewHolder = changeInfo.oldHolder;
        View view = null;
        View view2 = viewHolder == null ? null : viewHolder.itemView;
        final ViewHolder viewHolder2 = changeInfo.newHolder;
        if (viewHolder2 != null) {
            view = viewHolder2.itemView;
        }
        if (view2 != null) {
            notifyChangeStarting(viewHolder, true);
            view2.addOnAttachStateChangeListener(sAttachedListener);
            Folme.useAt(view2).state().to(ViewProperty.TRANSLATION_X, Integer.valueOf(changeInfo.toX - changeInfo.fromX), ViewProperty.TRANSLATION_Y, Integer.valueOf(changeInfo.toY - changeInfo.fromY), sSpeedConfig);
            view2.postDelayed(new Runnable() {
                public void run() {
                    MiuiDefaultItemAnimator.this.notifyChangeFinished(viewHolder, true);
                }
            }, Folme.useAt(view2).state().predictDuration(ViewProperty.TRANSLATION_X, Integer.valueOf(changeInfo.toX - changeInfo.fromX), ViewProperty.TRANSLATION_Y, Integer.valueOf(changeInfo.toY - changeInfo.fromY)));
        }
        if (view != null) {
            notifyChangeStarting(viewHolder2, false);
            Folme.useAt(view).state().to(ViewProperty.TRANSLATION_X, Integer.valueOf(0), ViewProperty.TRANSLATION_Y, Integer.valueOf(0), sSpeedConfig);
            view.postDelayed(new Runnable() {
                public void run() {
                    MiuiDefaultItemAnimator.this.notifyChangeFinished(viewHolder2, false);
                }
            }, Folme.useAt(view).state().predictDuration(ViewProperty.TRANSLATION_X, Integer.valueOf(0), ViewProperty.TRANSLATION_Y, Integer.valueOf(0)));
        }
    }

    /* access modifiers changed from: 0000 */
    public void animateMoveImpl(MoveInfo moveInfo) {
        notifyMoveStarting(moveInfo.holder);
        final ViewHolder viewHolder = moveInfo.holder;
        View view = viewHolder.itemView;
        Integer valueOf = Integer.valueOf(0);
        Folme.useAt(view).state().to(ViewProperty.TRANSLATION_X, valueOf, ViewProperty.TRANSLATION_Y, valueOf, sSpeedConfig);
        moveInfo.holder.itemView.postDelayed(new Runnable() {
            public void run() {
                MiuiDefaultItemAnimator.this.notifyMoveFinished(viewHolder);
            }
        }, Folme.useAt(moveInfo.holder.itemView).state().predictDuration(ViewProperty.TRANSLATION_X, valueOf, ViewProperty.TRANSLATION_Y, valueOf));
    }

    /* access modifiers changed from: 0000 */
    public void animateRemoveImpl(final ViewHolder viewHolder) {
        notifyRemoveStarting(viewHolder);
        viewHolder.itemView.addOnAttachStateChangeListener(sAttachedListener);
        IStateStyle state = Folme.useAt(viewHolder.itemView).state();
        Float valueOf = Float.valueOf(0.0f);
        state.to(ViewProperty.ALPHA, valueOf, sSpeedConfig);
        viewHolder.itemView.postDelayed(new Runnable() {
            public void run() {
                MiuiDefaultItemAnimator.this.notifyRemoveFinished(viewHolder);
            }
        }, Folme.useAt(viewHolder.itemView).state().predictDuration(ViewProperty.ALPHA, valueOf));
    }

    public long getAddDuration() {
        return 300;
    }

    public long getChangeDuration() {
        return 300;
    }

    public long getMoveDuration() {
        return 300;
    }

    public long getRemoveDuration() {
        return 300;
    }

    /* access modifiers changed from: 0000 */
    public void prepareAdd(ViewHolder viewHolder) {
        resetAnimation(viewHolder);
        viewHolder.itemView.setAlpha(0.0f);
    }

    /* access modifiers changed from: 0000 */
    public void prepareChange(ChangeInfo changeInfo) {
        float translationX = changeInfo.oldHolder.itemView.getTranslationX();
        float translationY = changeInfo.oldHolder.itemView.getTranslationY();
        resetAnimation(changeInfo.oldHolder);
        int i = (int) (((float) (changeInfo.toX - changeInfo.fromX)) - translationX);
        int i2 = (int) (((float) (changeInfo.toY - changeInfo.fromY)) - translationY);
        changeInfo.oldHolder.itemView.setTranslationX(translationX);
        changeInfo.oldHolder.itemView.setTranslationY(translationY);
        ViewHolder viewHolder = changeInfo.newHolder;
        if (viewHolder != null) {
            resetAnimation(viewHolder);
            changeInfo.newHolder.itemView.setTranslationX((float) (-i));
            changeInfo.newHolder.itemView.setTranslationY((float) (-i2));
        }
    }

    /* access modifiers changed from: 0000 */
    public void prepareMove(MoveInfo moveInfo) {
        moveInfo.holder.itemView.setTranslationX((float) (moveInfo.fromX - moveInfo.toX));
        moveInfo.holder.itemView.setTranslationY((float) (moveInfo.fromY - moveInfo.toY));
    }

    /* access modifiers changed from: 0000 */
    public void resetAnimation(ViewHolder viewHolder) {
        if (viewHolder != null) {
            Folme.useAt(viewHolder.itemView).state().cancel(ViewProperty.TRANSLATION_X, ViewProperty.TRANSLATION_Y, ViewProperty.ALPHA);
            MiuiBaseDefaultItemAnimator.resetAnimation(viewHolder.itemView);
        }
    }
}

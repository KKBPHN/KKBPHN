package androidx.recyclerview.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import miuix.animation.Folme;
import miuix.recyclerview.widget.RecyclerView;

public class SpringItemTouchHelper extends ItemTouchHelper {
    boolean mSpringEnabled;

    public SpringItemTouchHelper(@NonNull Callback callback) {
        super(callback);
    }

    /* access modifiers changed from: 0000 */
    public void select(@Nullable ViewHolder viewHolder, int i) {
        if (i == 2) {
            if (viewHolder != null) {
                Folme.setDraggingState(viewHolder.itemView, true);
            }
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView instanceof RecyclerView) {
                this.mSpringEnabled = ((RecyclerView) recyclerView).getSpringEnabled();
                ((RecyclerView) this.mRecyclerView).setSpringEnabled(false);
            }
        } else if (i == 0) {
            ViewHolder viewHolder2 = this.mSelected;
            if (viewHolder2 != null) {
                Folme.setDraggingState(viewHolder2.itemView, false);
                RecyclerView recyclerView2 = this.mRecyclerView;
                if (recyclerView2 instanceof RecyclerView) {
                    ((RecyclerView) recyclerView2).setSpringEnabled(this.mSpringEnabled);
                }
            }
        }
        super.select(viewHolder, i);
    }
}

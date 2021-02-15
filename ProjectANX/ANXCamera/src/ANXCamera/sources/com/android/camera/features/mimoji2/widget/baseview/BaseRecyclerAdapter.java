package com.android.camera.features.mimoji2.widget.baseview;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public abstract class BaseRecyclerAdapter extends Adapter {
    public static final String TAG = "BaseRecyclerAdapter";
    private CubicEaseOutInterpolator mCubicEaseOut = new CubicEaseOutInterpolator();
    private List mDdataList;
    private int mDegree;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public BaseRecyclerAdapter(List list) {
        this.mDdataList = list;
    }

    public synchronized void addData(Object obj) {
        if (this.mDdataList == null) {
            this.mDdataList = new ArrayList();
        }
        int size = this.mDdataList.size();
        if (this.mDdataList.add(obj)) {
            notifyItemInserted(size);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0019, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void addData(Object obj, int i) {
        if (this.mDdataList != null) {
            if (i <= this.mDdataList.size()) {
                this.mDdataList.add(i, obj);
                notifyItemInserted(i);
            }
        }
    }

    public CubicEaseOutInterpolator getCubicEaseOut() {
        return this.mCubicEaseOut;
    }

    public List getDataList() {
        return this.mDdataList;
    }

    public int getDegree() {
        return this.mDegree;
    }

    public int getItemCount() {
        List list = this.mDdataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public boolean isAvailablePosion(int i) {
        return getDataList() != null && i < getItemCount() && i >= 0;
    }

    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder baseRecyclerViewHolder, int i) {
    }

    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder baseRecyclerViewHolder, int i, @NonNull List list) {
        List list2 = this.mDdataList;
        if (list2 != null && i <= list2.size() - 1) {
            Object obj = this.mDdataList.get(i);
            if (obj == null) {
                Log.e(TAG, "data null error");
            }
            if ((list == null || list.isEmpty()) && baseRecyclerViewHolder.getRotateViews() != null) {
                for (View rotation : baseRecyclerViewHolder.getRotateViews()) {
                    rotation.setRotation((float) getDegree());
                }
            }
            baseRecyclerViewHolder.setData(obj, i);
            OnRecyclerItemClickListener onRecyclerItemClickListener2 = this.onRecyclerItemClickListener;
            if (onRecyclerItemClickListener2 != null) {
                baseRecyclerViewHolder.setClickListener(onRecyclerItemClickListener2, obj, i, baseRecyclerViewHolder.itemView);
            }
        }
    }

    public abstract BaseRecyclerViewHolder onCreateBaseRecyclerViewHolder(@NonNull ViewGroup viewGroup, int i);

    @NonNull
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return onCreateBaseRecyclerViewHolder(viewGroup, i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void removeData(int i) {
        if (this.mDdataList != null && i >= 0) {
            if (i <= this.mDdataList.size() - 1) {
                this.mDdataList.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public synchronized void setDataList(List list) {
        this.mDdataList = list;
        notifyDataSetChanged();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener2) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener2;
    }

    public void setRotation(int i) {
        this.mDegree = i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x002f, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void updateData(int i, Object obj) {
        if (this.mDdataList == null) {
            if (i == 0) {
                addData(obj);
            }
        } else if (i >= 0) {
            if (i <= this.mDdataList.size()) {
                if (i == this.mDdataList.size()) {
                    addData(obj);
                    return;
                }
                this.mDdataList.set(i, obj);
                notifyItemChanged(i);
            }
        }
    }
}

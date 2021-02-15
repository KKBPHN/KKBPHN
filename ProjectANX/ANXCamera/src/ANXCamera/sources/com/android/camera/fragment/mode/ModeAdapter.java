package com.android.camera.fragment.mode;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.ToastUtils;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.global.ComponentModuleList;
import com.android.camera.fragment.mode.IMoreMode.Type;
import com.android.camera.log.Log;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.ui.ModeBackground;
import com.android.camera.ui.NormalRoundView;
import com.android.camera.ui.WaterBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;

public class ModeAdapter extends Adapter {
    public static final int FLAG_ENTER = 2;
    public static final int FLAG_SWITCH = 4;
    private static final int MAX_TOP_ITEM_COUNT = 10;
    /* access modifiers changed from: private */
    public static final String TAG = "ModeAdapter";
    public static final int TYPE_CAPTURE = 3;
    public static final int TYPE_EDIT = 5;
    public static final int TYPE_FOOTER = 6;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_LINE = 2;
    public static final int TYPE_PENDING_DOWNLOAD = 7;
    public static final int TYPE_VIDEO = 4;
    private int mAnimFlags;
    protected OnClickListener mClickListener;
    protected Context mContext;
    private float mDegree;
    @Type
    protected int mFragmentType;
    protected List mItems;
    protected ComponentModuleList mModuleList;
    private IMoreMode mMoreMode;

    public class ModeViewHolder extends ViewHolder {
        private View mFgView;
        public FrameLayout mIconLayout;
        public ImageView mIconView;
        public TextView mNameView;
        private View mProgressView;

        public ModeViewHolder(@NonNull View view) {
            View findViewById;
            super(view);
            if (view.getId() == R.id.mode_item) {
                this.mIconLayout = (FrameLayout) view.findViewById(R.id.mode_icon_layout);
                findViewById = this.mIconLayout.findViewById(R.id.mode_fg);
            } else if (view.getId() == R.id.mode_item_new) {
                this.mIconLayout = (FrameLayout) view.findViewById(R.id.mode_item_new);
                findViewById = view.findViewById(R.id.mode_fg);
            } else {
                return;
            }
            this.mFgView = findViewById;
            this.mIconView = (ImageView) this.mIconLayout.findViewById(R.id.mode_icon);
            this.mNameView = (TextView) view.findViewById(R.id.mode_name);
        }

        public void setProgress(int i, boolean z) {
            String access$000 = ModeAdapter.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("setProgress ");
            sb.append(i);
            sb.append(",anim ");
            sb.append(z);
            Log.d(access$000, sb.toString());
            int id = this.itemView.getId();
            Integer valueOf = Integer.valueOf(1);
            if (id == R.id.mode_item) {
                FrameLayout frameLayout = this.mIconLayout;
                if (i == 100) {
                    if (frameLayout.indexOfChild(this.mProgressView) != -1) {
                        this.mIconLayout.removeView(this.mProgressView);
                        this.mProgressView = null;
                    }
                    FrameLayout frameLayout2 = this.mIconLayout;
                    if (frameLayout2 instanceof NormalRoundView) {
                        ((NormalRoundView) frameLayout2).setFill(true);
                    }
                } else if (frameLayout instanceof NormalRoundView) {
                    ((NormalRoundView) frameLayout).setFill(false);
                }
                if (i < 100 && i >= 0) {
                    View view = this.mProgressView;
                    if (view != null) {
                        ((ModeBackground) view).setProgress(i);
                    }
                    if (this.mNameView.getTag() != null && ((Integer) this.mNameView.getTag()).intValue() == 1) {
                        return;
                    }
                } else {
                    return;
                }
            } else if (this.itemView.getId() == R.id.mode_item_new) {
                if (i == 100) {
                    if (this.mIconLayout.indexOfChild(this.mProgressView) != -1) {
                        this.mIconLayout.removeView(this.mProgressView);
                        this.mProgressView = null;
                    }
                    this.mIconView.setBackgroundResource(R.color.mode_icon_bg_new);
                } else {
                    this.mIconView.setBackgroundColor(0);
                }
                if (i < 100 && i >= 0) {
                    View view2 = this.mProgressView;
                    if (view2 != null) {
                        ((WaterBox) view2).setValue(((float) i) / 100.0f, z);
                    }
                    if (this.mNameView.getTag() != null && ((Integer) this.mNameView.getTag()).intValue() == 1) {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
            this.mNameView.setTag(valueOf);
            this.mNameView.setText(R.string.download_in_progress);
        }

        public void setProgressView(View view) {
            this.mProgressView = view;
        }

        public void setRotation(float f) {
            View view;
            if (this.itemView.getId() == R.id.mode_item) {
                view = this.itemView;
            } else if (this.itemView.getId() == R.id.mode_item_new) {
                this.mFgView.setRotation(f);
                if (this.mIconLayout.indexOfChild(this.mProgressView) != -1) {
                    view = this.mProgressView;
                } else {
                    return;
                }
            } else {
                return;
            }
            view.setRotation(f);
        }
    }

    public class PlayLoad {
        public static final int STATE_NORMAL_NAME = 0;
        public static final int STATE_UPDATE_PROGRESS = 1;
        int progress;
        int type;

        public PlayLoad(int i) {
            this.type = i;
        }
    }

    public ModeAdapter(Context context, FragmentMoreModeBase fragmentMoreModeBase) {
        this.mContext = context;
        this.mModuleList = fragmentMoreModeBase.getComponentModuleList();
        this.mClickListener = fragmentMoreModeBase;
        this.mFragmentType = fragmentMoreModeBase.getType();
        this.mMoreMode = fragmentMoreModeBase;
        this.mItems = this.mFragmentType == 2 ? new ArrayList(this.mModuleList.getItems().subList(0, this.mModuleList.getItems().size() - 1)) : this.mModuleList.getMoreItems();
    }

    private ModeBackground addModeBackground(ViewGroup viewGroup) {
        ModeBackground modeBackground = new ModeBackground(viewGroup.getContext());
        modeBackground.setId(R.id.mode_bg);
        viewGroup.addView(modeBackground, 0, new LayoutParams(-1, -1));
        return modeBackground;
    }

    private WaterBox addWaterBox(ViewGroup viewGroup) {
        WaterBox waterBox = new WaterBox(viewGroup.getContext());
        waterBox.setColor(viewGroup.getResources().getColor(R.color.mode_icon_bg_new));
        waterBox.setBackgroundColor(0);
        waterBox.setValue(0.0f, false);
        waterBox.setId(R.id.mode_bg);
        viewGroup.addView(waterBox, 0, new LayoutParams(-1, -1));
        return waterBox;
    }

    private ItemTouchHelper createTouchHelper() {
        if (this.mFragmentType != 2) {
            return null;
        }
        return new ItemTouchHelper(new Callback() {
            private boolean isFull = false;
            private ModeViewHolder mSelectViewHolder;

            public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, @NonNull ViewHolder viewHolder2) {
                if (viewHolder2.getItemViewType() == 1 || viewHolder2.getItemViewType() == 6) {
                    return false;
                }
                if (viewHolder2.getItemViewType() == 4 && viewHolder.getLayoutPosition() < viewHolder2.getLayoutPosition()) {
                    return false;
                }
                if (viewHolder2.getItemViewType() == 3 && viewHolder.getLayoutPosition() > viewHolder2.getLayoutPosition()) {
                    return false;
                }
                if (recyclerView.getAdapter() != null && viewHolder.getLayoutPosition() >= 12 && viewHolder2.getLayoutPosition() < 12) {
                    int i = 0;
                    while (i < recyclerView.getAdapter().getItemCount()) {
                        if (recyclerView.getAdapter().getItemViewType(i) != 2 || i <= 10) {
                            i++;
                        } else {
                            if (!this.isFull) {
                                Log.d(ModeAdapter.TAG, "favorite mode full!");
                                this.isFull = true;
                                ToastUtils.showToast(ModeAdapter.this.mContext, (int) R.string.module_name_edit_full_toast, true);
                                CameraStatUtils.trackCommonModeFull();
                            }
                            return false;
                        }
                    }
                }
                return super.canDropOver(recyclerView, viewHolder, viewHolder2);
            }

            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder) {
                int i = (viewHolder.getItemViewType() == 1 || viewHolder.getItemViewType() == 6 || viewHolder.getItemViewType() == 2 || viewHolder.getItemViewType() == 3 || viewHolder.getItemViewType() == 4 || viewHolder.getItemViewType() == 7) ? 0 : 15;
                return Callback.makeMovementFlags(i, 0);
            }

            public boolean isLongPressDragEnabled() {
                return true;
            }

            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, @NonNull ViewHolder viewHolder2) {
                String access$000 = ModeAdapter.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("[onMove] current ");
                sb.append(viewHolder);
                sb.append(",target = ");
                sb.append(viewHolder2);
                Log.u(access$000, sb.toString());
                int adapterPosition = viewHolder.getAdapterPosition();
                int adapterPosition2 = viewHolder2.getAdapterPosition();
                int i = adapterPosition;
                if (adapterPosition < adapterPosition2) {
                    while (i < adapterPosition2) {
                        Collections.swap(ModeAdapter.this.mItems, i - 1, i);
                        i++;
                    }
                } else {
                    while (i > adapterPosition2) {
                        Collections.swap(ModeAdapter.this.mItems, i - 1, i - 2);
                        i--;
                    }
                }
                ModeAdapter.this.notifyItemMoved(adapterPosition, adapterPosition2);
                return true;
            }

            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder, int i, @NonNull ViewHolder viewHolder2, int i2, int i3, int i4) {
                super.onMoved(recyclerView, viewHolder, i, viewHolder2, i2, i3, i4);
                Log.u(ModeAdapter.TAG, "[onMoved]");
            }

            public void onSelectedChanged(@Nullable ViewHolder viewHolder, int i) {
                super.onSelectedChanged(viewHolder, i);
                String access$000 = ModeAdapter.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("[onSelectedChanged] viewHolder = ");
                sb.append(viewHolder);
                sb.append(", actionState = ");
                sb.append(i);
                Log.u(access$000, sb.toString());
                if (viewHolder instanceof ModeViewHolder) {
                    this.mSelectViewHolder = (ModeViewHolder) viewHolder;
                    FolmeUtils.animateHide(this.mSelectViewHolder.mNameView);
                    FolmeUtils.animationScale(this.mSelectViewHolder.itemView, 1.0f, 1.2f);
                } else {
                    FolmeUtils.animateShow(this.mSelectViewHolder.mNameView);
                    FolmeUtils.animationScale(this.mSelectViewHolder.itemView, 1.2f, 1.0f);
                    this.mSelectViewHolder = null;
                }
                if (viewHolder == null && this.isFull) {
                    this.isFull = false;
                }
            }

            public void onSwiped(@NonNull ViewHolder viewHolder, int i) {
            }
        });
    }

    private ComponentDataItem getItem(int i) {
        List list;
        int i2 = this.mFragmentType;
        if (i2 == 0 || i2 == 2 || i2 == 3) {
            list = this.mItems;
            i--;
        } else {
            list = this.mItems;
        }
        return (ComponentDataItem) list.get(i);
    }

    public List createChangeItems() {
        if (this.mFragmentType != 2) {
            return null;
        }
        List list = this.mItems;
        if (Integer.parseInt(((ComponentDataItem) list.get(list.size() - 1)).mValue) != 255) {
            this.mItems.add((ComponentDataItem) this.mModuleList.getItems().get(this.mModuleList.getItems().size() - 1));
        }
        return this.mItems;
    }

    public int getItemCount() {
        int i = this.mFragmentType;
        return i == 1 ? this.mItems.size() : (i == 0 || i == 3) ? this.mItems.size() + 1 : this.mItems.size() + 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000f, code lost:
        if (r0 != 3) goto L_0x0087;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getItemViewType(int i) {
        int i2 = this.mFragmentType;
        if (i2 != 0) {
            if (i2 != 1) {
                if (i2 == 2) {
                    if (i == 0) {
                        return 1;
                    }
                    if (i == this.mItems.size() + 1) {
                        return 6;
                    }
                    int parseInt = Integer.parseInt(getItem(i).mValue);
                    if (parseInt == 254) {
                        return 2;
                    }
                    if (parseInt == 163) {
                        return 3;
                    }
                    if (parseInt == 162) {
                        return 4;
                    }
                    if (this.mMoreMode.modeShouldDownload(parseInt)) {
                        return 7;
                    }
                }
            } else if (Integer.parseInt(getItem(i).mValue) == 255) {
                return 5;
            } else {
                if (this.mMoreMode.modeShouldDownload(Integer.parseInt(getItem(i).mValue))) {
                    return 7;
                }
            }
            return super.getItemViewType(i);
        }
        if (i == 0) {
            return 1;
        }
        if (Integer.parseInt(getItem(i).mValue) == 255) {
            return 5;
        }
        if (this.mMoreMode.modeShouldDownload(Integer.parseInt(getItem(i).mValue))) {
            return 7;
        }
        return super.getItemViewType(i);
    }

    public float getRotate() {
        return this.mDegree;
    }

    public boolean isDataChange() {
        if (this.mFragmentType != 2) {
            return false;
        }
        List list = this.mItems;
        List subList = this.mModuleList.getItems().subList(0, this.mModuleList.getItems().size() - 1);
        if (list.size() == subList.size()) {
            for (int i = 0; i < list.size(); i++) {
                if (!((ComponentDataItem) list.get(i)).mValue.equals(((ComponentDataItem) subList.get(i)).mValue)) {
                    return true;
                }
            }
            return false;
        }
        throw new IllegalStateException("data lost.");
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemTouchHelper createTouchHelper = createTouchHelper();
        if (createTouchHelper != null) {
            createTouchHelper.attachToRecyclerView(recyclerView);
        }
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
                public int getSpanSize(int i) {
                    int itemViewType = ModeAdapter.this.getItemViewType(i);
                    int i2 = ModeAdapter.this.mFragmentType;
                    if (i2 != 2 ? (i2 == 0 || i2 == 3) && (itemViewType == 1 || itemViewType == 6) : itemViewType == 2 || itemViewType == 1) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    public void onBindViewHolder(@NonNull ModeViewHolder modeViewHolder, int i) {
        int i2;
        Resources resources;
        TextView textView;
        int i3 = this.mFragmentType;
        if ((i3 == 0 || i3 == 3) && (modeViewHolder.getItemViewType() == 1 || modeViewHolder.getItemViewType() == 6)) {
            modeViewHolder.itemView.setVisibility(4);
            if (modeViewHolder.getItemViewType() == 1) {
                ((RecyclerView.LayoutParams) modeViewHolder.itemView.getLayoutParams()).height = MoreModeHelper.getHeaderHeightForNormal(this.mContext, this.mFragmentType, this.mMoreMode.getCountPerLine(), this.mItems.size());
            }
        }
        if (modeViewHolder.getItemViewType() != 2 && modeViewHolder.getItemViewType() != 1 && modeViewHolder.getItemViewType() != 6) {
            ComponentDataItem item = getItem(i);
            modeViewHolder.itemView.setTag(item);
            int i4 = item.mIconRes;
            if (i4 != -1) {
                ImageView imageView = modeViewHolder.mIconView;
                if (this.mFragmentType == 3) {
                    i4 = item.mNewIconRes;
                }
                imageView.setImageResource(i4);
                modeViewHolder.setRotation(this.mDegree);
                if (getItemViewType(i) == 5 || getItemViewType(i) == 7) {
                    modeViewHolder.setProgress(0, false);
                } else {
                    modeViewHolder.setProgress(100, false);
                }
            }
            if (item.mDisplayNameRes != 0) {
                modeViewHolder.mNameView.setTag(Integer.valueOf(0));
                modeViewHolder.mNameView.setText(item.mDisplayNameRes);
            } else if (item.mDisplayNameStr != null) {
                modeViewHolder.mNameView.setTag(Integer.valueOf(0));
                modeViewHolder.mNameView.setText(item.mDisplayNameStr);
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onBindViewHolder ");
            sb.append(modeViewHolder.mNameView.getText());
            sb.append(", viewType = ");
            sb.append(modeViewHolder.getItemViewType());
            Log.d(str, sb.toString());
            if (modeViewHolder.getItemViewType() == 3 || modeViewHolder.getItemViewType() == 4 || (this.mFragmentType == 2 && modeViewHolder.getItemViewType() == 7)) {
                modeViewHolder.mIconView.setAlpha(0.3f);
                textView = modeViewHolder.mNameView;
                resources = this.mContext.getResources();
                i2 = R.color.mode_edit_icon_disable_name_color;
            } else {
                modeViewHolder.mIconView.setAlpha(1.0f);
                if (this.mFragmentType == 3) {
                    textView = modeViewHolder.mNameView;
                    resources = this.mContext.getResources();
                    i2 = R.color.mode_name_color;
                } else {
                    textView = modeViewHolder.mNameView;
                    resources = this.mContext.getResources();
                    i2 = R.color.mode_edit_icon_name_color;
                }
            }
            textView.setTextColor(resources.getColor(i2));
            if ((this.mAnimFlags & 4) == 0) {
                return;
            }
            if (getItemViewType(i) != 0 && getItemViewType(i) != 7 && getItemViewType(i) != 5) {
                return;
            }
            if (this.mMoreMode.getType() == 3) {
                if (getItemViewType(i) == 5 || i >= Display.getMoreModeTabRow(DataRepository.dataItemRunning().getUiStyle(), true) * this.mMoreMode.getCountPerLine()) {
                    this.mAnimFlags &= -5;
                }
                MoreModeListAnimation.getInstance().startShowNewAnimation(modeViewHolder.mIconLayout, i - 1);
                return;
            }
            if (getItemViewType(i) == 5 || i >= Display.getMoreModeTabRow(DataRepository.dataItemRunning().getUiStyle(), false) * this.mMoreMode.getCountPerLine()) {
                this.mAnimFlags &= -5;
            }
            MoreModeListAnimation.getInstance().startShowOldAnimation(modeViewHolder.itemView, i - 1);
        }
    }

    public void onBindViewHolder(@NonNull ModeViewHolder modeViewHolder, int i, @NonNull List list) {
        if (list.isEmpty()) {
            onBindViewHolder(modeViewHolder, i);
        } else if (list.get(0) instanceof PlayLoad) {
            PlayLoad playLoad = (PlayLoad) list.get(0);
            if (playLoad.type == 1) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onBindViewHolderPayloads ,position = ");
                sb.append(i);
                sb.append(", viewType = ");
                sb.append(modeViewHolder.getItemViewType());
                sb.append(", payloads = ");
                sb.append(playLoad.progress);
                Log.d(str, sb.toString());
                modeViewHolder.setProgress(playLoad.progress, true);
            }
        }
    }

    @NonNull
    public ModeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View addModeBackground;
        int i2 = this.mFragmentType;
        int i3 = R.layout.mode_item;
        if (i2 == 2) {
            if (i == 2) {
                return new ModeViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.mode_edit_dividing_line, viewGroup, false));
            }
            if (i == 1) {
                return new ModeViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.mode_edit_header, viewGroup, false));
            }
            if (i == 6) {
                View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.mode_item, viewGroup, false);
                inflate.setVisibility(4);
                return new ModeViewHolder(inflate);
            }
        } else if ((i2 == 0 || i2 == 3) && i == 1) {
            View view = new View(this.mContext);
            view.setLayoutParams(new RecyclerView.LayoutParams(1, MoreModeHelper.getHeaderHeightForNormal(this.mContext, this.mFragmentType, this.mMoreMode.getCountPerLine(), this.mItems.size())));
            return new ModeViewHolder(view);
        }
        if (this.mFragmentType == 3) {
            i3 = R.layout.mode_item_new;
        }
        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(this.mContext).inflate(i3, viewGroup, false);
        ModeViewHolder modeViewHolder = new ModeViewHolder(viewGroup2);
        if (i == 7) {
            int i4 = this.mFragmentType;
            if (i4 == 3) {
                addModeBackground = addWaterBox(viewGroup2);
            } else if (i4 == 0 || i4 == 1) {
                addModeBackground = addModeBackground((ViewGroup) viewGroup2.findViewById(R.id.mode_icon_layout));
            }
            modeViewHolder.setProgressView(addModeBackground);
        }
        if (this.mFragmentType == 2) {
            viewGroup2.setOnClickListener(this.mClickListener);
        } else {
            viewGroup2.setOnClickListener(this.mClickListener);
            modeViewHolder.setRotation(this.mDegree);
            Folme.useAt(modeViewHolder.mIconLayout).touch().handleTouchOf(viewGroup2, new AnimConfig[0]);
        }
        return modeViewHolder;
    }

    public void onViewAttachedToWindow(@NonNull ModeViewHolder modeViewHolder) {
        super.onViewAttachedToWindow(modeViewHolder);
        modeViewHolder.setRotation(this.mDegree);
    }

    public void setAnimFlags(int i) {
        this.mAnimFlags = i | this.mAnimFlags;
    }

    public void setRotate(int i) {
        if (!Display.fitDisplayFull(1.3333333f)) {
            this.mDegree = (float) i;
        } else if (this.mDegree != ((float) i)) {
            notifyDataSetChanged();
        }
    }
}

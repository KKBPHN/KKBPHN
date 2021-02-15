package com.android.camera.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.log.Log;
import com.android.camera.ui.ScrollTextview;
import com.android.gallery3d.ui.EglWindowSurface;
import com.xiaomi.camera.liveshot.gles.EglCore;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class EffectItemAdapter extends Adapter {
    /* access modifiers changed from: private */
    public static final String TAG = "EffectItemAdapter";
    protected ComponentData mComponentData;
    protected final Context mContext;
    private int mDegree;
    private int mDisplayRotation;
    /* access modifiers changed from: private */
    public IEffectItemListener mEffectItemListener;
    private EglCore mEglCore;
    protected LayoutInflater mLayoutInflater;
    private OnClickListener mOnClickListener;
    private boolean mSupportRealtimeEffect;

    abstract class EffectItemHolder extends ViewHolder {
        protected int mEffectIndex;
        protected ImageView mSelectedIndicator;
        protected ScrollTextview mTextView;

        public EffectItemHolder(View view) {
            super(view);
            this.mTextView = (ScrollTextview) view.findViewById(R.id.effect_item_text);
        }

        public void bindEffectIndex(int i, ComponentDataItem componentDataItem) {
            this.mEffectIndex = getRenderId(i, componentDataItem);
            int i2 = componentDataItem.mDisplayNameRes;
            ScrollTextview scrollTextview = this.mTextView;
            if (i2 > 0) {
                scrollTextview.setText(i2);
            } else {
                scrollTextview.setText("");
            }
        }

        public EglWindowSurface getEglSurface() {
            return null;
        }

        /* access modifiers changed from: protected */
        public int getRenderId(int i, ComponentDataItem componentDataItem) {
            return Integer.parseInt(componentDataItem.mValue);
        }
    }

    public class EffectItemPadding extends ItemDecoration {
        protected boolean mIsRTL = false;
        protected int padding;

        public EffectItemPadding(Context context) {
            this.padding = context.getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_margin);
            this.mIsRTL = Util.isLayoutRTL(context);
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            int childPosition = recyclerView.getChildPosition(view);
            if (this.mIsRTL) {
                int i = this.padding;
                rect.set(i, 0, childPosition == 0 ? i : 0, 0);
                return;
            }
            rect.set(childPosition == 0 ? this.padding : 0, 0, this.padding, 0);
        }
    }

    class EffectRealtimeItemHolder extends EffectItemHolder implements SurfaceTextureListener {
        private EglCore mEglCore;
        private EglWindowSurface mEglSurface;
        private TextureView mTextureView;

        public EffectRealtimeItemHolder(View view, EglCore eglCore) {
            super(view);
            this.mTextureView = (TextureView) view.findViewById(R.id.effect_item_realtime);
            this.mSelectedIndicator = (ImageView) view.findViewById(R.id.effect_item_selected_indicator);
            this.mEglCore = eglCore;
            this.mTextureView.setSurfaceTextureListener(this);
        }

        public void bindEffectIndex(int i, ComponentDataItem componentDataItem) {
            ViewPropertyAnimatorCompat interpolator;
            ViewPropertyAnimatorListener r8;
            super.bindEffectIndex(i, componentDataItem);
            EffectItemAdapter effectItemAdapter = EffectItemAdapter.this;
            View view = this.itemView;
            int i2 = componentDataItem.mDisplayNameRes;
            boolean z = effectItemAdapter.mEffectItemListener != null && EffectItemAdapter.this.mEffectItemListener.getCurrentIndex() == i;
            effectItemAdapter.setAccessible(view, i2, z);
            if (EffectItemAdapter.this.mEffectItemListener == null || EffectItemAdapter.this.mEffectItemListener.getCurrentIndex() != i) {
                this.itemView.setActivated(false);
                if (EffectItemAdapter.this.mEffectItemListener == null || !EffectItemAdapter.this.mEffectItemListener.isAnimation() || EffectItemAdapter.this.mEffectItemListener == null || EffectItemAdapter.this.mEffectItemListener.getLastIndex() != i) {
                    this.mSelectedIndicator.setVisibility(8);
                    this.mSelectedIndicator.setAlpha(0.0f);
                    return;
                }
                ViewCompat.setAlpha(this.mSelectedIndicator, 1.0f);
                interpolator = ViewCompat.animate(this.mSelectedIndicator).setDuration(500).alpha(0.0f).setInterpolator(new CubicEaseOutInterpolator());
                r8 = new ViewPropertyAnimatorListener() {
                    public void onAnimationCancel(View view) {
                    }

                    public void onAnimationEnd(View view) {
                        EffectRealtimeItemHolder.this.mSelectedIndicator.setVisibility(8);
                    }

                    public void onAnimationStart(View view) {
                    }
                };
            } else {
                this.itemView.setActivated(true);
                if (EffectItemAdapter.this.mEffectItemListener == null || !EffectItemAdapter.this.mEffectItemListener.isAnimation()) {
                    this.mSelectedIndicator.setVisibility(0);
                    this.mSelectedIndicator.setAlpha(1.0f);
                    return;
                }
                ViewCompat.setAlpha(this.mSelectedIndicator, 0.0f);
                interpolator = ViewCompat.animate(this.mSelectedIndicator).setDuration(500).alpha(1.0f).setInterpolator(new CubicEaseOutInterpolator());
                r8 = new ViewPropertyAnimatorListener() {
                    public void onAnimationCancel(View view) {
                    }

                    public void onAnimationEnd(View view) {
                    }

                    public void onAnimationStart(View view) {
                        EffectRealtimeItemHolder.this.mSelectedIndicator.setVisibility(0);
                    }
                };
            }
            interpolator.setListener(r8).start();
        }

        public EglWindowSurface getEglSurface() {
            return this.mEglSurface;
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            EglCore eglCore = this.mEglCore;
            if (eglCore != null) {
                this.mEglSurface = new EglWindowSurface(eglCore, surfaceTexture);
            }
            String access$100 = EffectItemAdapter.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onSurfaceTextureAvailable width:");
            sb.append(i);
            sb.append(" height:");
            sb.append(i2);
            Log.d(access$100, sb.toString());
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            this.mEglSurface.releaseEglSurface();
            this.mEglSurface = null;
            Log.d(EffectItemAdapter.TAG, "onSurfaceTextureDestroyed");
            return false;
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            String access$100 = EffectItemAdapter.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onSurfaceTextureSizeChanged width:");
            sb.append(i);
            sb.append(" height:");
            sb.append(i2);
            Log.d(access$100, sb.toString());
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
    }

    public class EffectStillItemHolder extends EffectItemHolder {
        private ImageView mImageView;

        public EffectStillItemHolder(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(R.id.effect_item_image);
            this.mSelectedIndicator = (ImageView) view.findViewById(R.id.effect_item_selected_indicator);
        }

        public void bindEffectIndex(int i, ComponentDataItem componentDataItem) {
            ViewPropertyAnimatorCompat interpolator;
            ViewPropertyAnimatorListener r8;
            super.bindEffectIndex(i, componentDataItem);
            this.mImageView.setImageResource(componentDataItem.mIconRes);
            EffectItemAdapter effectItemAdapter = EffectItemAdapter.this;
            View view = this.itemView;
            int i2 = componentDataItem.mDisplayNameRes;
            boolean z = effectItemAdapter.mEffectItemListener != null && EffectItemAdapter.this.mEffectItemListener.getCurrentIndex() == i;
            effectItemAdapter.setAccessible(view, i2, z);
            if (EffectItemAdapter.this.mEffectItemListener == null || EffectItemAdapter.this.mEffectItemListener.getCurrentIndex() != i) {
                this.itemView.setActivated(false);
                if (EffectItemAdapter.this.mEffectItemListener == null || !EffectItemAdapter.this.mEffectItemListener.isAnimation() || EffectItemAdapter.this.mEffectItemListener == null || EffectItemAdapter.this.mEffectItemListener.getLastIndex() != i) {
                    this.mSelectedIndicator.setVisibility(8);
                    this.mSelectedIndicator.setAlpha(0.0f);
                    return;
                }
                ViewCompat.setAlpha(this.mSelectedIndicator, 1.0f);
                interpolator = ViewCompat.animate(this.mSelectedIndicator).setDuration(500).alpha(0.0f).setInterpolator(new CubicEaseOutInterpolator());
                r8 = new ViewPropertyAnimatorListener() {
                    public void onAnimationCancel(View view) {
                    }

                    public void onAnimationEnd(View view) {
                        EffectStillItemHolder.this.mSelectedIndicator.setVisibility(8);
                    }

                    public void onAnimationStart(View view) {
                    }
                };
            } else {
                this.itemView.setActivated(true);
                if (EffectItemAdapter.this.mEffectItemListener == null || !EffectItemAdapter.this.mEffectItemListener.isAnimation()) {
                    this.mSelectedIndicator.setVisibility(0);
                    this.mSelectedIndicator.setAlpha(1.0f);
                    return;
                }
                ViewCompat.setAlpha(this.mSelectedIndicator, 0.0f);
                interpolator = ViewCompat.animate(this.mSelectedIndicator).setDuration(500).alpha(1.0f).setInterpolator(new CubicEaseOutInterpolator());
                r8 = new ViewPropertyAnimatorListener() {
                    public void onAnimationCancel(View view) {
                    }

                    public void onAnimationEnd(View view) {
                    }

                    public void onAnimationStart(View view) {
                        EffectStillItemHolder.this.mSelectedIndicator.setVisibility(0);
                    }
                };
            }
            interpolator.setListener(r8).start();
        }

        public /* bridge */ /* synthetic */ EglWindowSurface getEglSurface() {
            return super.getEglSurface();
        }
    }

    public interface IEffectItemListener {
        int getCurrentIndex();

        int getHolderHeight();

        int getHolderWidth();

        int getLastIndex();

        int getTextureHeight();

        int getTextureOffsetX();

        int getTextureOffsetY();

        int getTextureWidth();

        int getTotalWidth();

        boolean isAdded();

        boolean isAnimation();
    }

    public class ItemChangeData {
        int index;
        boolean select;

        public ItemChangeData(boolean z, int i) {
            this.select = z;
            this.index = i;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ItemChangeData{select=");
            sb.append(this.select);
            sb.append(", index=");
            sb.append(this.index);
            sb.append('}');
            return sb.toString();
        }
    }

    public EffectItemAdapter(Context context, ComponentData componentData) {
        this.mContext = context;
        this.mComponentData = componentData;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public EffectItemAdapter(Context context, ComponentData componentData, boolean z) {
        this.mContext = context;
        this.mComponentData = componentData;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mSupportRealtimeEffect = z;
    }

    public EffectItemAdapter(Context context, ComponentData componentData, boolean z, EglCore eglCore) {
        this.mContext = context;
        this.mComponentData = componentData;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mSupportRealtimeEffect = z;
        this.mEglCore = eglCore;
    }

    public /* synthetic */ void O0000ooO(View view) {
        IEffectItemListener iEffectItemListener = this.mEffectItemListener;
        if (iEffectItemListener != null && iEffectItemListener.isAdded()) {
            view.sendAccessibilityEvent(128);
        }
    }

    public int getItemCount() {
        String str;
        String str2;
        ComponentData componentData = this.mComponentData;
        if (componentData == null) {
            str = TAG;
            str2 = "mComponentData = null ";
        } else if (componentData.getItems() != null) {
            return this.mComponentData.getItems().size();
        } else {
            str = TAG;
            str2 = " getItems() = null ";
        }
        Log.e(str, str2);
        return 0;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        View view;
        int i2;
        ComponentDataItem componentDataItem = (ComponentDataItem) this.mComponentData.getItems().get(i);
        EffectItemHolder effectItemHolder = (EffectItemHolder) viewHolder;
        effectItemHolder.itemView.setTag(Integer.valueOf(i));
        effectItemHolder.bindEffectIndex(i, componentDataItem);
        if (this.mSupportRealtimeEffect) {
            viewHolder.itemView.findViewById(R.id.effect_item_text).setRotation((float) this.mDegree);
            view = viewHolder.itemView.findViewById(R.id.effect_item_realtime);
            i2 = this.mDisplayRotation;
        } else {
            view = viewHolder.itemView;
            i2 = this.mDegree;
        }
        view.setRotation((float) i2);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull List list) {
        if (list.isEmpty()) {
            onBindViewHolder(viewHolder, i);
        } else if (viewHolder instanceof EffectItemHolder) {
            EffectItemHolder effectItemHolder = (EffectItemHolder) viewHolder;
            if (list.get(0) instanceof ItemChangeData) {
                ItemChangeData itemChangeData = (ItemChangeData) list.get(0);
                if (itemChangeData.select) {
                    IEffectItemListener iEffectItemListener = this.mEffectItemListener;
                    if (!(iEffectItemListener == null || iEffectItemListener.getCurrentIndex() == itemChangeData.index)) {
                        itemChangeData.select = false;
                    }
                }
                effectItemHolder.itemView.setActivated(itemChangeData.select);
                ImageView imageView = effectItemHolder.mSelectedIndicator;
                if (imageView != null) {
                    Util.updateSelectIndicator(imageView, itemChangeData.select, true);
                }
            }
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder;
        View view;
        if (this.mSupportRealtimeEffect) {
            view = this.mLayoutInflater.inflate(R.layout.effect_realtime_item, viewGroup, false);
            viewHolder = new EffectRealtimeItemHolder(view, this.mEglCore);
        } else {
            view = this.mLayoutInflater.inflate(R.layout.effect_still_item, viewGroup, false);
            viewHolder = new EffectStillItemHolder(view);
        }
        view.setOnClickListener(this.mOnClickListener);
        FolmeUtils.handleListItemTouch(view);
        return viewHolder;
    }

    public void setAccessible(View view, int i, boolean z) {
        if (view != null) {
            String string = view.getContext().getString(R.string.lighting_pattern_null);
            if (i > 0) {
                string = view.getContext().getString(i);
            }
            if (z) {
                StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append(", ");
                sb.append(view.getContext().getString(R.string.accessibility_selected));
                view.setContentDescription(sb.toString());
                if (Util.isAccessible()) {
                    view.postDelayed(new C0282O00000oo(this, view), 100);
                }
            } else {
                view.setContentDescription(string);
            }
        }
    }

    public void setDisplayRotation(int i) {
        this.mDisplayRotation = i;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnEffectItemListener(IEffectItemListener iEffectItemListener) {
        this.mEffectItemListener = iEffectItemListener;
    }

    public void setRotation(int i) {
        this.mDegree = i;
    }

    public void updateData(ComponentData componentData) {
        this.mComponentData = componentData;
        notifyDataSetChanged();
    }
}

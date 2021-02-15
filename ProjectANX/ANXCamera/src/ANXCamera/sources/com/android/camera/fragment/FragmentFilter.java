package com.android.camera.fragment;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.ActivityBase;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraScreenNail.RequestRenderListener;
import com.android.camera.CameraSettings;
import com.android.camera.NoClipChildrenLayout;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.config.ComponentConfigFilter;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FrameBuffer;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.framework.gles.OpenGlUtils;
import com.android.camera.fragment.EffectItemAdapter.EffectItemPadding;
import com.android.camera.fragment.EffectItemAdapter.IEffectItemListener;
import com.android.camera.fragment.EffectItemAdapter.ItemChangeData;
import com.android.camera.fragment.beauty.BaseBeautyFragment;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.statistic.CameraStatUtils;
import com.android.gallery3d.ui.EglWindowSurface;
import com.android.gallery3d.ui.FilterCanvasImpl;
import com.android.gallery3d.ui.GLCanvasImpl;
import com.android.gallery3d.ui.GLThread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.view.animation.CubicEaseOutInterpolator;

public class FragmentFilter extends BaseBeautyFragment implements OnClickListener, RequestRenderListener, NoClipChildrenLayout, IEffectItemListener {
    public static final int FRAGMENT_INFO = 250;
    private static final String TAG = "FragmentFilter";
    private boolean isAnimation = false;
    private DrawBasicTexAttribute mBasicTextureAttr = new DrawBasicTexAttribute();
    private ComponentConfigFilter mComponentConfigFilter;
    private CubicEaseOutInterpolator mCubicEaseOut;
    private int mCurrentIndex = -1;
    private int mCurrentMode;
    private EffectItemAdapter mEffectItemAdapter;
    private EffectItemPadding mEffectItemPadding;
    private DrawExtTexAttribute mExtTextureAttr = new DrawExtTexAttribute(true);
    private Map mFilterCanvasMap = new HashMap(16);
    private GLThread mFilterGLThread;
    private FrameBuffer mFrameBuffer;
    private int mHolderHeight;
    private int mHolderWidth;
    private boolean mIgnoreSameItemClick = true;
    private int mIsShowIndex = -1;
    private int mLastIndex = -1;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private boolean mSupportRealtimeEffect;
    private boolean mTargetClipChildren;
    private int mTextureHeight;
    private int mTextureOffsetX;
    private int mTextureOffsetY;
    private int mTextureWidth;
    private int mTotalWidth;

    private void drawRealtimeFilterOnGLThread() {
        GLThread gLThread = this.mFilterGLThread;
        if (gLThread != null) {
            gLThread.getHandler().post(new O0000Oo(this));
        }
    }

    private FilterCanvasImpl getCanvasById(int i) {
        FilterCanvasImpl filterCanvasImpl = (FilterCanvasImpl) this.mFilterCanvasMap.get(Integer.valueOf(i));
        if (filterCanvasImpl != null) {
            return filterCanvasImpl;
        }
        FilterCanvasImpl filterCanvasImpl2 = new FilterCanvasImpl();
        filterCanvasImpl2.prepareEffectRenders(false, i);
        this.mFilterCanvasMap.put(Integer.valueOf(i), filterCanvasImpl2);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("add id: ");
        sb.append(i);
        sb.append(" to map, size:");
        sb.append(this.mFilterCanvasMap.size());
        Log.d(str, sb.toString());
        return filterCanvasImpl2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
        r1 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0032, code lost:
        if (com.android.camera.CameraSettings.isFrontCamera() != false) goto L_0x0034;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ArrayList getFilterInfo() {
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        int i = 2;
        if (currentMode != 165) {
            if (currentMode != 169) {
                if (currentMode != 171) {
                    if (currentMode != 180) {
                        if (currentMode != 183) {
                            if (currentMode != 205) {
                                switch (currentMode) {
                                    case 161:
                                        i = 3;
                                        break;
                                    case 162:
                                        break;
                                    case 163:
                                        break;
                                }
                            }
                        } else {
                            i = 8;
                        }
                    }
                }
                return EffectController.getInstance().getFilterInfo(i);
            }
            i = 7;
            return EffectController.getInstance().getFilterInfo(i);
        }
    }

    private void initGL() {
        Log.d(TAG, "initGL start");
        if (supportsRealtimeEffect() && this.mFilterGLThread == null) {
            this.mFilterGLThread = ((ActivityBase) getContext()).getGLView().getFilterGLThread();
        }
        Log.d(TAG, "initGL end");
    }

    private void initView(View view) {
        this.mSupportRealtimeEffect = supportsRealtimeEffect();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("initView ");
        sb.append(this.mTargetClipChildren);
        Log.v(str, sb.toString());
        if (this.mTargetClipChildren) {
            this.mTargetClipChildren = false;
            ((ViewGroup) view).setClipChildren(false);
        }
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.effect_list);
        this.mRecyclerView.setFocusable(false);
        ArrayList filterInfo = getFilterInfo();
        this.mComponentConfigFilter = DataRepository.dataItemRunning().getComponentConfigFilter();
        this.mComponentConfigFilter.mapToItems(filterInfo, this.mCurrentMode);
        Context context = getContext();
        this.mTotalWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mHolderWidth = context.getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_width);
        this.mHolderHeight = this.mHolderWidth;
        GLThread gLThread = this.mFilterGLThread;
        if (gLThread != null) {
            this.mEffectItemAdapter = new EffectItemAdapter(context, this.mComponentConfigFilter, this.mSupportRealtimeEffect, gLThread.getEglCore());
        } else {
            this.mEffectItemAdapter = new EffectItemAdapter(context, this.mComponentConfigFilter, this.mSupportRealtimeEffect);
        }
        this.mEffectItemAdapter.setOnClickListener(this);
        this.mEffectItemAdapter.setOnEffectItemListener(this);
        this.mEffectItemAdapter.setRotation(this.mDegree);
        if (C0124O00000oO.OOooO0o()) {
            this.mEffectItemAdapter.setDisplayRotation(Util.getDisplayRotation(getActivity()));
        }
        this.mLayoutManager = new LinearLayoutManagerWrapper(context, "effect_list");
        this.mLayoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        if (this.mEffectItemPadding == null) {
            this.mEffectItemPadding = new EffectItemPadding(getContext());
            this.mRecyclerView.addItemDecoration(this.mEffectItemPadding);
        }
        this.mRecyclerView.setAdapter(this.mEffectItemAdapter);
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                FragmentFilter.this.setIsAnimation(false);
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setChangeDuration(150);
        defaultItemAnimator.setMoveDuration(150);
        defaultItemAnimator.setAddDuration(150);
        this.mRecyclerView.setItemAnimator(defaultItemAnimator);
        this.mCubicEaseOut = new CubicEaseOutInterpolator();
        measure();
    }

    private void measure() {
        CameraScreenNail cameraScreenNail = ((ActivityBase) getContext()).getCameraScreenNail();
        int width = cameraScreenNail.getWidth();
        int height = cameraScreenNail.getHeight();
        this.mTextureOffsetX = 0;
        this.mTextureOffsetY = 0;
        int i = this.mHolderWidth;
        this.mTextureWidth = i;
        int i2 = this.mHolderHeight;
        this.mTextureHeight = i2;
        if (height * i > width * i2) {
            this.mTextureHeight = (i * height) / width;
            this.mTextureOffsetY = (-(this.mTextureHeight - i2)) / 2;
            return;
        }
        this.mTextureWidth = (i2 * width) / height;
        this.mTextureOffsetX = (-(this.mTextureWidth - i)) / 2;
    }

    private void notifyItemChanged(int i, int i2) {
        ItemChangeData itemChangeData = new ItemChangeData(false, i);
        ItemChangeData itemChangeData2 = new ItemChangeData(true, i2);
        if (i > -1) {
            if (Util.isAccessible()) {
                ComponentConfigFilter componentConfigFilter = this.mComponentConfigFilter;
                if (componentConfigFilter != null) {
                    int i3 = ((ComponentDataItem) componentConfigFilter.getItems().get(i)).mDisplayNameRes;
                    ViewHolder findViewHolderForAdapterPosition = this.mRecyclerView.findViewHolderForAdapterPosition(i);
                    if (findViewHolderForAdapterPosition != null) {
                        View view = findViewHolderForAdapterPosition.itemView;
                        if (i3 <= 0) {
                            i3 = R.string.lighting_pattern_null;
                        }
                        view.setContentDescription(getString(i3));
                    }
                }
            }
            this.mEffectItemAdapter.notifyItemChanged(i, itemChangeData);
        }
        if (i2 > -1) {
            if (Util.isAccessible()) {
                ComponentConfigFilter componentConfigFilter2 = this.mComponentConfigFilter;
                if (componentConfigFilter2 != null) {
                    int i4 = ((ComponentDataItem) componentConfigFilter2.getItems().get(i2)).mDisplayNameRes;
                    ViewHolder findViewHolderForAdapterPosition2 = this.mRecyclerView.findViewHolderForAdapterPosition(i2);
                    if (findViewHolderForAdapterPosition2 != null && isAdded()) {
                        this.mEffectItemAdapter.setAccessible(findViewHolderForAdapterPosition2.itemView, i4, true);
                    }
                }
            }
            this.mEffectItemAdapter.notifyItemChanged(i2, itemChangeData2);
        }
    }

    private void onItemSelected(int i, boolean z) {
        String str = "0";
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onItemSelected: index = ");
        sb.append(i);
        sb.append(", fromClick = ");
        sb.append(z);
        sb.append(", mCurrentMode = ");
        sb.append(this.mCurrentMode);
        sb.append(", DataRepository.dataItemGlobal().getCurrentMode() = ");
        sb.append(DataRepository.dataItemGlobal().getCurrentMode());
        Log.u(str2, sb.toString());
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            Log.e(TAG, "onItemSelected: configChanges = null");
            return;
        }
        try {
            String str3 = ((ComponentDataItem) this.mComponentConfigFilter.getItems().get(i)).mValue;
            int i2 = ((ComponentDataItem) this.mComponentConfigFilter.getItems().get(i)).mDisplayNameRes;
            if (i2 > 0) {
                String str4 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("onItemSelected: filterId = ");
                sb2.append(str3);
                sb2.append(" filterName = ");
                sb2.append(CameraApplicationDelegate.getAndroidContext().getString(i2));
                Log.u(str4, sb2.toString());
            }
            this.mComponentConfigFilter.setClosed(false, DataRepository.dataItemGlobal().getCurrentMode());
            int intValue = Integer.valueOf(str3).intValue();
            CameraStatUtils.trackFilterChanged(intValue, z);
            selectItem(i);
            if (DataRepository.dataItemRunning().getComponentRunningLighting().getPortraitLightVersion() > 1) {
                DataRepository.dataItemRunning().getComponentRunningLighting().setComponentValue(this.mCurrentMode, str);
                configChanges.setLighting(false, str, str, false);
            }
            configChanges.setFilter(intValue);
        } catch (NumberFormatException e) {
            String str5 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("invalid filter id: ");
            sb3.append(e.getMessage());
            Log.e(str5, sb3.toString());
        }
    }

    private void releaseGL() {
        Log.d(TAG, "releaseGL start");
        GLThread gLThread = this.mFilterGLThread;
        Handler handler = gLThread != null ? gLThread.getHandler() : null;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler.post(new C0283O0000OoO(this));
        }
        Log.d(TAG, "releaseGL end");
    }

    private void scrollIfNeed(int i) {
        if (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
            int i2 = this.mEffectItemPadding.padding;
            View findViewByPosition = this.mLayoutManager.findViewByPosition(i);
            if (i > 0 && findViewByPosition != null) {
                i2 = (this.mEffectItemPadding.padding * 2) + findViewByPosition.getWidth();
            }
            this.mLayoutManager.scrollToPositionWithOffset(Math.max(0, i), i2);
        } else if (i == this.mLayoutManager.findLastVisibleItemPosition() || i == this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.min(i + 1, this.mEffectItemAdapter.getItemCount() - 1));
        }
    }

    private void selectItem(int i) {
        if (i != -1) {
            this.mLastIndex = this.mCurrentIndex;
            this.mCurrentIndex = i;
            scrollIfNeed(i);
            notifyItemChanged(this.mLastIndex, this.mCurrentIndex);
        }
    }

    /* access modifiers changed from: private */
    public void setIsAnimation(boolean z) {
        this.isAnimation = z;
    }

    private void setItemInCenter(int i) {
        this.mCurrentIndex = i;
        this.mIsShowIndex = i;
        int i2 = (this.mTotalWidth / 2) - (this.mHolderWidth / 2);
        this.mEffectItemAdapter.notifyDataSetChanged();
        this.mLayoutManager.scrollToPositionWithOffset(i, i2);
    }

    private void showSelected(ImageView imageView, int i) {
        if (isAdded()) {
            Canvas canvas = new Canvas();
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.filter_item_selected_view);
            Bitmap decodeResource2 = BitmapFactory.decodeResource(getResources(), i);
            Bitmap createBitmap = Bitmap.createBitmap(decodeResource.getWidth(), decodeResource.getHeight(), Config.ARGB_8888);
            canvas.setBitmap(createBitmap);
            Paint paint = new Paint();
            paint.setFilterBitmap(false);
            canvas.drawBitmap(decodeResource, 0.0f, 0.0f, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(decodeResource2, 0.0f, 0.0f, paint);
            paint.setXfermode(null);
            imageView.setImageBitmap(createBitmap);
        }
    }

    private boolean supportsRealtimeEffect() {
        if (!C0122O00000o.instance().OOoO0Oo()) {
            return false;
        }
        int i = this.mCurrentMode;
        return (i == 180 || i == 162 || i == 169) ? false : true;
    }

    private void updateCurrentIndex() {
        CameraSettings.getShaderEffect();
        String componentValue = this.mComponentConfigFilter.getComponentValue(DataRepository.dataItemGlobal().getCurrentMode());
        int findIndexOfValue = this.mComponentConfigFilter.findIndexOfValue(componentValue);
        if (findIndexOfValue == -1) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("invalid filter ");
            sb.append(componentValue);
            Log.w(str, sb.toString());
            findIndexOfValue = 0;
        }
        setItemInCenter(findIndexOfValue);
    }

    private void updateFrameBuffer(CameraScreenNail cameraScreenNail) {
        FilterCanvasImpl canvasById = getCanvasById(Integer.parseInt(((ComponentDataItem) this.mComponentConfigFilter.getItems().get(0)).mValue));
        if (this.mFrameBuffer == null) {
            this.mFrameBuffer = new FrameBuffer(null, this.mTextureWidth, this.mTextureHeight, 0);
        }
        canvasById.beginBindFrameBuffer(this.mFrameBuffer);
        canvasById.getState().pushState();
        canvasById.draw(this.mExtTextureAttr.init(cameraScreenNail.getExtTexture(), cameraScreenNail.getCurrentTransform(), this.mTextureOffsetX, this.mTextureOffsetY, this.mTextureWidth, this.mTextureHeight));
        canvasById.getState().popState();
        canvasById.endBindFrameBuffer();
        canvasById.recycledResources();
    }

    public /* synthetic */ void O00O0OO() {
        ActivityBase activityBase = (ActivityBase) getContext();
        GLCanvasImpl gLCanvasImpl = null;
        CameraScreenNail cameraScreenNail = activityBase != null ? activityBase.getCameraScreenNail() : null;
        if (activityBase != null) {
            gLCanvasImpl = activityBase.getGLView().getGLCanvas();
        }
        if (cameraScreenNail != null && gLCanvasImpl != null && cameraScreenNail.getSurfaceTexture() != null) {
            synchronized (gLCanvasImpl) {
                updateFrameBuffer(cameraScreenNail);
            }
            for (int i = 0; i < this.mLayoutManager.getChildCount(); i++) {
                View childAt = this.mLayoutManager.getChildAt(i);
                if (childAt != null) {
                    EffectItemHolder effectItemHolder = (EffectItemHolder) this.mRecyclerView.getChildViewHolder(childAt);
                    if (!(effectItemHolder == null || effectItemHolder.getEglSurface() == null)) {
                        EglWindowSurface eglSurface = effectItemHolder.getEglSurface();
                        FilterCanvasImpl canvasById = getCanvasById(Integer.parseInt(((ComponentDataItem) this.mComponentConfigFilter.getItems().get(effectItemHolder.getPosition())).mValue));
                        int width = eglSurface.getWidth();
                        int height = eglSurface.getHeight();
                        if (eglSurface.makeCurrent()) {
                            canvasById.setSize(width, height);
                            canvasById.getState().pushState();
                            canvasById.draw(this.mBasicTextureAttr.init(this.mFrameBuffer.getTexture(), 0, 0, this.mFrameBuffer.getWidth(), this.mFrameBuffer.getHeight()));
                            eglSurface.swapBuffers();
                            canvasById.getState().popState();
                            canvasById.recycledResources();
                        }
                    }
                }
            }
        }
    }

    public /* synthetic */ void O00O0OOo() {
        for (Integer intValue : this.mFilterCanvasMap.keySet()) {
            FilterCanvasImpl filterCanvasImpl = (FilterCanvasImpl) this.mFilterCanvasMap.get(Integer.valueOf(intValue.intValue()));
            if (filterCanvasImpl != null) {
                filterCanvasImpl.deleteProgram();
            }
        }
        this.mFilterCanvasMap.clear();
        FrameBuffer frameBuffer = this.mFrameBuffer;
        if (frameBuffer != null) {
            frameBuffer.delete();
            OpenGlUtils.safeDeleteTexture(this.mFrameBuffer.getTexture().getId());
            this.mFrameBuffer = null;
        }
    }

    /* access modifiers changed from: protected */
    public View getAnimateView() {
        return this.mRecyclerView;
    }

    public int getCurrentIndex() {
        return this.mCurrentIndex;
    }

    public int getHolderHeight() {
        return this.mHolderHeight;
    }

    public int getHolderWidth() {
        return this.mHolderWidth;
    }

    public int getLastIndex() {
        return this.mLastIndex;
    }

    public int getTextureHeight() {
        return this.mTextureHeight;
    }

    public int getTextureOffsetX() {
        return this.mTextureOffsetX;
    }

    public int getTextureOffsetY() {
        return this.mTextureOffsetY;
    }

    public int getTextureWidth() {
        return this.mTextureWidth;
    }

    public int getTotalWidth() {
        return this.mTotalWidth;
    }

    public boolean isAnimation() {
        return this.isAnimation;
    }

    public void isShowAnimation(List list) {
        setIsAnimation(list != null);
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        if (this.mRecyclerView.isEnabled()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction == null || !cameraAction.isDoingAction()) {
                int intValue = ((Integer) view.getTag()).intValue();
                if (this.mCurrentIndex != intValue || !this.mIgnoreSameItemClick) {
                    setIsAnimation(false);
                    onItemSelected(intValue, true);
                    return;
                }
                if (Util.isAccessible() && isAdded()) {
                    view.sendAccessibilityEvent(32768);
                }
            }
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        this.mCurrentMode = DataRepository.dataItemGlobal().getCurrentMode();
        View inflate = layoutInflater.inflate(R.layout.fragment_filter, viewGroup, false);
        initGL();
        initView(inflate);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        ActivityBase activityBase = (ActivityBase) getActivity();
        if (activityBase != null) {
            activityBase.getCameraScreenNail().removeRequestListener(this);
        }
        releaseGL();
    }

    public void onResume() {
        super.onResume();
        ActivityBase activityBase = (ActivityBase) getActivity();
        if (activityBase != null) {
            activityBase.getCameraScreenNail().addRequestListener(this);
        }
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        updateCurrentIndex();
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (this.mRecyclerView != null) {
            for (int i2 = 0; i2 < this.mRecyclerView.getChildCount(); i2++) {
                View childAt = this.mRecyclerView.getChildAt(i2);
                if (this.mSupportRealtimeEffect) {
                    if (C0124O00000oO.OOooO0o()) {
                        int displayRotation = Util.getDisplayRotation(getActivity());
                        ((TextureView) childAt.findViewById(R.id.effect_item_realtime)).setRotation((float) displayRotation);
                        EffectItemAdapter effectItemAdapter = this.mEffectItemAdapter;
                        if (effectItemAdapter != null) {
                            effectItemAdapter.setDisplayRotation(displayRotation);
                        }
                    }
                    childAt = childAt.findViewById(R.id.effect_item_text);
                }
                list.add(childAt);
            }
        }
        EffectItemAdapter effectItemAdapter2 = this.mEffectItemAdapter;
        if (effectItemAdapter2 != null) {
            effectItemAdapter2.setRotation(i);
            int findFirstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = this.mLayoutManager.findLastVisibleItemPosition();
            for (int i3 = 0; i3 < findFirstVisibleItemPosition; i3++) {
                this.mEffectItemAdapter.notifyItemChanged(i3);
            }
            while (true) {
                findLastVisibleItemPosition++;
                if (findLastVisibleItemPosition < this.mEffectItemAdapter.getItemCount()) {
                    this.mEffectItemAdapter.notifyItemChanged(findLastVisibleItemPosition);
                } else {
                    return;
                }
            }
        }
    }

    public void reInit() {
        setItemInCenter(this.mComponentConfigFilter.findIndexOfValue(this.mComponentConfigFilter.getComponentValue(this.mCurrentMode)));
    }

    public void requestRender() {
        if (this.mSupportRealtimeEffect) {
            drawRealtimeFilterOnGLThread();
        }
    }

    public void setDegree(int i) {
        super.setDegree(i);
        EffectItemAdapter effectItemAdapter = this.mEffectItemAdapter;
        if (effectItemAdapter != null) {
            effectItemAdapter.setRotation(i);
            if (C0124O00000oO.OOooO0o()) {
                this.mEffectItemAdapter.setDisplayRotation(Util.getDisplayRotation(getActivity()));
            }
            this.mEffectItemAdapter.notifyDataSetChanged();
        }
    }

    public void setNoClip(boolean z) {
        if (getView() != null && (getView() instanceof ViewGroup)) {
            ((ViewGroup) getView()).setClipChildren(!z);
        }
        if (getView() == null) {
            this.mTargetClipChildren = z;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void switchFilter(int i) {
        int i2;
        if (i == 3) {
            int i3 = this.mCurrentIndex;
            if (i3 > 0) {
                i2 = i3 - 1;
                if (i2 > -1) {
                }
            }
        } else if (i != 5) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("unexpected gravity ");
            sb.append(i);
            Log.e(str, sb.toString());
        } else if (this.mCurrentIndex < this.mComponentConfigFilter.getItems().size() - 1) {
            i2 = this.mCurrentIndex + 1;
            if (i2 > -1) {
                onItemSelected(i2, false);
                return;
            }
            return;
        }
        i2 = -1;
        if (i2 > -1) {
        }
    }

    public void updateFilterData() {
        ArrayList filterInfo = getFilterInfo();
        this.mComponentConfigFilter = DataRepository.dataItemRunning().getComponentConfigFilter();
        this.mComponentConfigFilter.mapToItems(filterInfo, this.mCurrentMode);
        this.mEffectItemAdapter.updateData(this.mComponentConfigFilter);
        updateCurrentIndex();
    }
}

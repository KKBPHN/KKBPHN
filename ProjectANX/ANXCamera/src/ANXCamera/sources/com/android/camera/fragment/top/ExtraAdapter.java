package com.android.camera.fragment.top;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.config.ComponentRunningMacroMode;
import com.android.camera.data.data.config.ComponentRunningUltraPixel;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.config.SupportedConfigs;
import com.android.camera.data.data.extra.DataItemLive;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.data.data.runing.ComponentRunningAiEnhancedVideo;
import com.android.camera.data.data.runing.ComponentRunningAutoZoom;
import com.android.camera.data.data.runing.ComponentRunningColorEnhance;
import com.android.camera.data.data.runing.ComponentRunningSubtitle;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.ui.SlideSwitchButton.SlideSwitchListener;
import java.util.ArrayList;
import java.util.List;
import miui.view.animation.CubicEaseOutInterpolator;

public class ExtraAdapter extends Adapter implements OnClickListener {
    public static final int ITEM_TYPE_MULTI = 1;
    public static final int ITEM_TYPE_TOGGLE = 2;
    private int mAnimateHeight;
    private float mAnimationScaleSize;
    private long mAnimationStartTime;
    private Context mContext;
    private int mCurrentRow = -1;
    private DataItemConfig mDataItemConfig;
    private DataItemLive mDataItemLive;
    private DataItemRunning mDataItemRunning;
    private OnClickListener mOnClickListener;
    private List mRowLists = new ArrayList();
    private SlideSwitchListener mSlideSwitchListener;
    private SupportedConfigs mSupportedConfigs;

    public ExtraAdapter(Context context, SupportedConfigs supportedConfigs, OnClickListener onClickListener, SlideSwitchListener slideSwitchListener) {
        this.mContext = context;
        this.mSupportedConfigs = supportedConfigs;
        this.mOnClickListener = onClickListener;
        this.mSlideSwitchListener = slideSwitchListener;
        this.mDataItemRunning = DataRepository.dataItemRunning();
        this.mDataItemConfig = DataRepository.dataItemConfig();
        this.mDataItemLive = DataRepository.dataItemLive();
        this.mAnimationScaleSize = CameraSettings.getResourceFloat(R.dimen.menu_item_scale_size, 0.38f);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001c, code lost:
        if (((r0 - r1) % 4) == 0) goto L_0x0011;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getRow(int i) {
        int i2;
        int i3 = 0;
        int i4 = -1;
        int i5 = 0;
        while (i3 < getItemCount()) {
            if (getItemViewType(i3) != 1) {
                if (i5 == 0) {
                    i5 = i4 + 1;
                }
            }
            i4++;
            if (i == i3) {
                break;
            }
            i3++;
        }
        return i2;
    }

    private int getTotalRow() {
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < getItemCount(); i3++) {
            if (getItemViewType(i3) != 1) {
                if (i2 == 0) {
                    i2 = i;
                }
                if ((i3 - i2) % 4 != 0) {
                }
            }
            i++;
        }
        return i;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<android.view.View>, for r9v0, types: [java.util.List, java.util.List<android.view.View>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setAnimation(List<View> list, int i, int i2) {
        this.mAnimationStartTime = System.currentTimeMillis();
        for (View view : list) {
            int i3 = i2 - 1;
            float f = (float) (i3 - i);
            float f2 = 0.18f - (0.01f * f);
            float f3 = 1.36f - (f * 0.02f);
            ViewCompat.setTranslationY(view, (float) (-this.mAnimateHeight));
            int i4 = (i3 * 20) - (i * 20);
            ViewCompat.animate(view).setInterpolator(new PathInterpolator(f2, f3, 0.3f, 1.0f)).translationY(0.0f).setDuration((long) (520 - i4)).setStartDelay((long) i4).start();
            ViewCompat.setAlpha(view, 0.0f);
            ViewCompat.animate(view).setInterpolator(new CubicEaseOutInterpolator()).alpha(1.0f).setDuration(400).setStartDelay((long) ((i3 * 50) - (i * 50))).start();
        }
    }

    public boolean animationRunning() {
        return System.currentTimeMillis() - this.mAnimationStartTime < 500;
    }

    public int getItemCount() {
        return this.mSupportedConfigs.getLength();
    }

    public int getItemCount(int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < this.mSupportedConfigs.getLength(); i3++) {
            if (i == getItemViewType(i3)) {
                i2++;
            }
        }
        return i2;
    }

    public int getItemViewType(int i) {
        int config = this.mSupportedConfigs.getConfig(i);
        return (config == 173 || config == 174 || config == 187 || config == 204 || config == 208 || config == 210 || config == 213 || config == 226) ? 1 : 2;
    }

    @StringRes
    public int getMuitiContentDescriptionString(int i) {
        int config = this.mSupportedConfigs.getConfig(i);
        if (config != 173) {
            if (config != 174) {
                if (config != 187) {
                    if (config != 204) {
                        if (config != 208) {
                            if (config == 210) {
                                return R.string.pref_camera_picturesize_title_simple_mode;
                            }
                            if (config != 213) {
                                if (config != 226) {
                                    return 0;
                                }
                                return R.string.pref_camera_delay_capture_title;
                            }
                        }
                    }
                }
                return R.string.pref_video_quality_hfr_title;
            }
            return R.string.pref_video_hfr_title;
        }
        return R.string.pref_video_quality_title;
    }

    public int getTotalRow(int i) {
        int i2 = 0;
        if (i == 1) {
            int i3 = 0;
            while (i2 < getItemCount() && getItemViewType(i2) == i) {
                i3++;
                i2++;
            }
            return i3;
        }
        int i4 = -1;
        int i5 = 0;
        while (i2 < getItemCount()) {
            if (getItemViewType(i2) != 1) {
                if (i4 == -1) {
                    i4 = i5;
                }
                if ((i2 - i4) % 4 != 0) {
                    i2++;
                }
            }
            i5++;
            i2++;
        }
        return i5 - i4;
    }

    /* JADX WARNING: type inference failed for: r15v0 */
    /* JADX WARNING: type inference failed for: r14v0, types: [int] */
    /* JADX WARNING: type inference failed for: r13v0, types: [boolean] */
    /* JADX WARNING: type inference failed for: r12v0, types: [boolean] */
    /* JADX WARNING: type inference failed for: r10v0 */
    /* JADX WARNING: type inference failed for: r9v0, types: [int] */
    /* JADX WARNING: type inference failed for: r5v0, types: [boolean] */
    /* JADX WARNING: type inference failed for: r18v0 */
    /* JADX WARNING: type inference failed for: r9v2, types: [int] */
    /* JADX WARNING: type inference failed for: r9v3, types: [int] */
    /* JADX WARNING: type inference failed for: r4v6, types: [boolean] */
    /* JADX WARNING: type inference failed for: r15v13 */
    /* JADX WARNING: type inference failed for: r14v1 */
    /* JADX WARNING: type inference failed for: r13v1 */
    /* JADX WARNING: type inference failed for: r12v3 */
    /* JADX WARNING: type inference failed for: r9v7 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r10v6 */
    /* JADX WARNING: type inference failed for: r13v2 */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: type inference failed for: r12v4 */
    /* JADX WARNING: type inference failed for: r15v14 */
    /* JADX WARNING: type inference failed for: r14v2 */
    /* JADX WARNING: type inference failed for: r9v8 */
    /* JADX WARNING: type inference failed for: r13v7 */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r12v11 */
    /* JADX WARNING: type inference failed for: r15v15 */
    /* JADX WARNING: type inference failed for: r14v4 */
    /* JADX WARNING: type inference failed for: r9v9 */
    /* JADX WARNING: type inference failed for: r13v8 */
    /* JADX WARNING: type inference failed for: r12v14 */
    /* JADX WARNING: type inference failed for: r10v7 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r14v6 */
    /* JADX WARNING: type inference failed for: r15v16 */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r10v8 */
    /* JADX WARNING: type inference failed for: r12v15 */
    /* JADX WARNING: type inference failed for: r13v9 */
    /* JADX WARNING: type inference failed for: r15v17 */
    /* JADX WARNING: type inference failed for: r14v7 */
    /* JADX WARNING: type inference failed for: r13v10 */
    /* JADX WARNING: type inference failed for: r12v16 */
    /* JADX WARNING: type inference failed for: r9v10 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r10v9 */
    /* JADX WARNING: type inference failed for: r15v18 */
    /* JADX WARNING: type inference failed for: r14v8 */
    /* JADX WARNING: type inference failed for: r13v11 */
    /* JADX WARNING: type inference failed for: r12v17 */
    /* JADX WARNING: type inference failed for: r9v11 */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: type inference failed for: r10v10 */
    /* JADX WARNING: type inference failed for: r9v12 */
    /* JADX WARNING: type inference failed for: r4v18 */
    /* JADX WARNING: type inference failed for: r13v12 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r12v18 */
    /* JADX WARNING: type inference failed for: r14v9 */
    /* JADX WARNING: type inference failed for: r15v19 */
    /* JADX WARNING: type inference failed for: r4v19, types: [boolean] */
    /* JADX WARNING: type inference failed for: r10v11 */
    /* JADX WARNING: type inference failed for: r9v15, types: [int] */
    /* JADX WARNING: type inference failed for: r15v20 */
    /* JADX WARNING: type inference failed for: r14v10 */
    /* JADX WARNING: type inference failed for: r13v13 */
    /* JADX WARNING: type inference failed for: r12v19 */
    /* JADX WARNING: type inference failed for: r9v16 */
    /* JADX WARNING: type inference failed for: r5v11 */
    /* JADX WARNING: type inference failed for: r10v12 */
    /* JADX WARNING: type inference failed for: r11v16 */
    /* JADX WARNING: type inference failed for: r10v13 */
    /* JADX WARNING: type inference failed for: r9v17 */
    /* JADX WARNING: type inference failed for: r4v23 */
    /* JADX WARNING: type inference failed for: r13v14 */
    /* JADX WARNING: type inference failed for: r5v12 */
    /* JADX WARNING: type inference failed for: r12v20 */
    /* JADX WARNING: type inference failed for: r15v21 */
    /* JADX WARNING: type inference failed for: r14v11 */
    /* JADX WARNING: type inference failed for: r9v18 */
    /* JADX WARNING: type inference failed for: r4v25, types: [boolean] */
    /* JADX WARNING: type inference failed for: r9v20 */
    /* JADX WARNING: type inference failed for: r10v14 */
    /* JADX WARNING: type inference failed for: r11v17 */
    /* JADX WARNING: type inference failed for: r4v28, types: [boolean] */
    /* JADX WARNING: type inference failed for: r9v21 */
    /* JADX WARNING: type inference failed for: r10v15 */
    /* JADX WARNING: type inference failed for: r11v18 */
    /* JADX WARNING: type inference failed for: r9v22, types: [int] */
    /* JADX WARNING: type inference failed for: r10v16, types: [boolean] */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: type inference failed for: r12v21 */
    /* JADX WARNING: type inference failed for: r14v12 */
    /* JADX WARNING: type inference failed for: r15v22 */
    /* JADX WARNING: type inference failed for: r13v15 */
    /* JADX WARNING: type inference failed for: r9v23 */
    /* JADX WARNING: type inference failed for: r5v14 */
    /* JADX WARNING: type inference failed for: r12v22 */
    /* JADX WARNING: type inference failed for: r10v17 */
    /* JADX WARNING: type inference failed for: r13v16 */
    /* JADX WARNING: type inference failed for: r11v20 */
    /* JADX WARNING: type inference failed for: r10v18 */
    /* JADX WARNING: type inference failed for: r9v24 */
    /* JADX WARNING: type inference failed for: r4v39 */
    /* JADX WARNING: type inference failed for: r15v23 */
    /* JADX WARNING: type inference failed for: r5v15 */
    /* JADX WARNING: type inference failed for: r12v23 */
    /* JADX WARNING: type inference failed for: r14v13 */
    /* JADX WARNING: type inference failed for: r9v25 */
    /* JADX WARNING: type inference failed for: r13v17 */
    /* JADX WARNING: type inference failed for: r4v40 */
    /* JADX WARNING: type inference failed for: r9v26 */
    /* JADX WARNING: type inference failed for: r10v19 */
    /* JADX WARNING: type inference failed for: r11v22, types: [boolean] */
    /* JADX WARNING: type inference failed for: r11v23 */
    /* JADX WARNING: type inference failed for: r10v20 */
    /* JADX WARNING: type inference failed for: r9v27 */
    /* JADX WARNING: type inference failed for: r4v41 */
    /* JADX WARNING: type inference failed for: r13v18 */
    /* JADX WARNING: type inference failed for: r5v16 */
    /* JADX WARNING: type inference failed for: r12v25 */
    /* JADX WARNING: type inference failed for: r15v24 */
    /* JADX WARNING: type inference failed for: r14v14 */
    /* JADX WARNING: type inference failed for: r4v42, types: [boolean] */
    /* JADX WARNING: type inference failed for: r10v21 */
    /* JADX WARNING: type inference failed for: r11v24 */
    /* JADX WARNING: type inference failed for: r9v30, types: [int] */
    /* JADX WARNING: type inference failed for: r4v43, types: [boolean] */
    /* JADX WARNING: type inference failed for: r13v19 */
    /* JADX WARNING: type inference failed for: r5v17 */
    /* JADX WARNING: type inference failed for: r12v26 */
    /* JADX WARNING: type inference failed for: r14v15 */
    /* JADX WARNING: type inference failed for: r15v25 */
    /* JADX WARNING: type inference failed for: r9v34 */
    /* JADX WARNING: type inference failed for: r9v35 */
    /* JADX WARNING: type inference failed for: r10v25 */
    /* JADX WARNING: type inference failed for: r4v46, types: [boolean] */
    /* JADX WARNING: type inference failed for: r13v20 */
    /* JADX WARNING: type inference failed for: r12v27 */
    /* JADX WARNING: type inference failed for: r14v16 */
    /* JADX WARNING: type inference failed for: r5v18 */
    /* JADX WARNING: type inference failed for: r15v26 */
    /* JADX WARNING: type inference failed for: r10v26 */
    /* JADX WARNING: type inference failed for: r10v29 */
    /* JADX WARNING: type inference failed for: r9v36 */
    /* JADX WARNING: type inference failed for: r15v27 */
    /* JADX WARNING: type inference failed for: r5v19 */
    /* JADX WARNING: type inference failed for: r10v32 */
    /* JADX WARNING: type inference failed for: r12v28 */
    /* JADX WARNING: type inference failed for: r13v21 */
    /* JADX WARNING: type inference failed for: r14v17 */
    /* JADX WARNING: type inference failed for: r4v51, types: [boolean] */
    /* JADX WARNING: type inference failed for: r10v33, types: [int] */
    /* JADX WARNING: type inference failed for: r9v39, types: [int] */
    /* JADX WARNING: type inference failed for: r4v54, types: [boolean] */
    /* JADX WARNING: type inference failed for: r9v41 */
    /* JADX WARNING: type inference failed for: r10v34 */
    /* JADX WARNING: type inference failed for: r11v30 */
    /* JADX WARNING: type inference failed for: r15v28 */
    /* JADX WARNING: type inference failed for: r14v18 */
    /* JADX WARNING: type inference failed for: r13v22 */
    /* JADX WARNING: type inference failed for: r12v29 */
    /* JADX WARNING: type inference failed for: r9v42 */
    /* JADX WARNING: type inference failed for: r5v20 */
    /* JADX WARNING: type inference failed for: r10v35 */
    /* JADX WARNING: type inference failed for: r12v30, types: [boolean] */
    /* JADX WARNING: type inference failed for: r5v21 */
    /* JADX WARNING: type inference failed for: r15v29 */
    /* JADX WARNING: type inference failed for: r14v19 */
    /* JADX WARNING: type inference failed for: r9v44 */
    /* JADX WARNING: type inference failed for: r13v23 */
    /* JADX WARNING: type inference failed for: r10v37 */
    /* JADX WARNING: type inference failed for: r12v31 */
    /* JADX WARNING: type inference failed for: r12v32 */
    /* JADX WARNING: type inference failed for: r13v24 */
    /* JADX WARNING: type inference failed for: r15v30 */
    /* JADX WARNING: type inference failed for: r14v20 */
    /* JADX WARNING: type inference failed for: r9v45 */
    /* JADX WARNING: type inference failed for: r9v46 */
    /* JADX WARNING: type inference failed for: r10v38, types: [boolean] */
    /* JADX WARNING: type inference failed for: r15v31 */
    /* JADX WARNING: type inference failed for: r5v22 */
    /* JADX WARNING: type inference failed for: r12v33 */
    /* JADX WARNING: type inference failed for: r14v21 */
    /* JADX WARNING: type inference failed for: r13v25 */
    /* JADX WARNING: type inference failed for: r9v47 */
    /* JADX WARNING: type inference failed for: r4v59, types: [boolean] */
    /* JADX WARNING: type inference failed for: r10v41 */
    /* JADX WARNING: type inference failed for: r9v48 */
    /* JADX WARNING: type inference failed for: r15v32 */
    /* JADX WARNING: type inference failed for: r5v23 */
    /* JADX WARNING: type inference failed for: r12v34 */
    /* JADX WARNING: type inference failed for: r13v26 */
    /* JADX WARNING: type inference failed for: r14v22 */
    /* JADX WARNING: type inference failed for: r4v62, types: [boolean] */
    /* JADX WARNING: type inference failed for: r9v49 */
    /* JADX WARNING: type inference failed for: r10v43 */
    /* JADX WARNING: type inference failed for: r11v34 */
    /* JADX WARNING: type inference failed for: r10v44 */
    /* JADX WARNING: type inference failed for: r9v50 */
    /* JADX WARNING: type inference failed for: r4v63 */
    /* JADX WARNING: type inference failed for: r11v36, types: [boolean] */
    /* JADX WARNING: type inference failed for: r4v64 */
    /* JADX WARNING: type inference failed for: r9v51 */
    /* JADX WARNING: type inference failed for: r10v45 */
    /* JADX WARNING: type inference failed for: r4v65, types: [boolean] */
    /* JADX WARNING: type inference failed for: r10v46 */
    /* JADX WARNING: type inference failed for: r11v38 */
    /* JADX WARNING: type inference failed for: r9v54, types: [int] */
    /* JADX WARNING: type inference failed for: r4v66, types: [boolean] */
    /* JADX WARNING: type inference failed for: r10v47 */
    /* JADX WARNING: type inference failed for: r11v39 */
    /* JADX WARNING: type inference failed for: r9v57, types: [int] */
    /* JADX WARNING: type inference failed for: r4v68, types: [boolean] */
    /* JADX WARNING: type inference failed for: r9v59 */
    /* JADX WARNING: type inference failed for: r10v48 */
    /* JADX WARNING: type inference failed for: r11v40 */
    /* JADX WARNING: type inference failed for: r4v69, types: [boolean] */
    /* JADX WARNING: type inference failed for: r9v60 */
    /* JADX WARNING: type inference failed for: r10v49 */
    /* JADX WARNING: type inference failed for: r11v41 */
    /* JADX WARNING: type inference failed for: r4v70, types: [boolean] */
    /* JADX WARNING: type inference failed for: r9v61 */
    /* JADX WARNING: type inference failed for: r10v50 */
    /* JADX WARNING: type inference failed for: r11v42 */
    /* JADX WARNING: type inference failed for: r4v71 */
    /* JADX WARNING: type inference failed for: r9v62 */
    /* JADX WARNING: type inference failed for: r10v51 */
    /* JADX WARNING: type inference failed for: r15v33 */
    /* JADX WARNING: type inference failed for: r14v23 */
    /* JADX WARNING: type inference failed for: r13v27 */
    /* JADX WARNING: type inference failed for: r12v38 */
    /* JADX WARNING: type inference failed for: r10v52 */
    /* JADX WARNING: type inference failed for: r9v63 */
    /* JADX WARNING: type inference failed for: r5v24 */
    /* JADX WARNING: type inference failed for: r15v34 */
    /* JADX WARNING: type inference failed for: r14v24 */
    /* JADX WARNING: type inference failed for: r13v28 */
    /* JADX WARNING: type inference failed for: r12v39 */
    /* JADX WARNING: type inference failed for: r10v53 */
    /* JADX WARNING: type inference failed for: r5v25 */
    /* JADX WARNING: type inference failed for: r9v64 */
    /* JADX WARNING: type inference failed for: r5v26 */
    /* JADX WARNING: type inference failed for: r10v54 */
    /* JADX WARNING: type inference failed for: r12v40 */
    /* JADX WARNING: type inference failed for: r13v29 */
    /* JADX WARNING: type inference failed for: r14v25 */
    /* JADX WARNING: type inference failed for: r15v35 */
    /* JADX WARNING: type inference failed for: r15v36 */
    /* JADX WARNING: type inference failed for: r14v26 */
    /* JADX WARNING: type inference failed for: r13v30 */
    /* JADX WARNING: type inference failed for: r12v41 */
    /* JADX WARNING: type inference failed for: r9v65 */
    /* JADX WARNING: type inference failed for: r5v27 */
    /* JADX WARNING: type inference failed for: r9v66 */
    /* JADX WARNING: type inference failed for: r13v31 */
    /* JADX WARNING: type inference failed for: r12v42 */
    /* JADX WARNING: type inference failed for: r10v55 */
    /* JADX WARNING: type inference failed for: r5v28 */
    /* JADX WARNING: type inference failed for: r14v27 */
    /* JADX WARNING: type inference failed for: r15v37 */
    /* JADX WARNING: type inference failed for: r14v28 */
    /* JADX WARNING: type inference failed for: r13v32 */
    /* JADX WARNING: type inference failed for: r12v43 */
    /* JADX WARNING: type inference failed for: r9v67 */
    /* JADX WARNING: type inference failed for: r5v29 */
    /* JADX WARNING: type inference failed for: r15v38 */
    /* JADX WARNING: type inference failed for: r14v29 */
    /* JADX WARNING: type inference failed for: r13v33 */
    /* JADX WARNING: type inference failed for: r12v44 */
    /* JADX WARNING: type inference failed for: r9v68 */
    /* JADX WARNING: type inference failed for: r5v30 */
    /* JADX WARNING: type inference failed for: r9v69 */
    /* JADX WARNING: type inference failed for: r4v75 */
    /* JADX WARNING: type inference failed for: r10v56 */
    /* JADX WARNING: type inference failed for: r9v70 */
    /* JADX WARNING: type inference failed for: r15v39 */
    /* JADX WARNING: type inference failed for: r14v30 */
    /* JADX WARNING: type inference failed for: r13v34 */
    /* JADX WARNING: type inference failed for: r12v45 */
    /* JADX WARNING: type inference failed for: r9v71 */
    /* JADX WARNING: type inference failed for: r5v31 */
    /* JADX WARNING: type inference failed for: r4v76 */
    /* JADX WARNING: type inference failed for: r9v72 */
    /* JADX WARNING: type inference failed for: r10v57 */
    /* JADX WARNING: type inference failed for: r11v45 */
    /* JADX WARNING: type inference failed for: r4v77 */
    /* JADX WARNING: type inference failed for: r9v73 */
    /* JADX WARNING: type inference failed for: r10v58 */
    /* JADX WARNING: type inference failed for: r11v46 */
    /* JADX WARNING: type inference failed for: r9v74 */
    /* JADX WARNING: type inference failed for: r4v78 */
    /* JADX WARNING: type inference failed for: r9v75 */
    /* JADX WARNING: type inference failed for: r10v59 */
    /* JADX WARNING: type inference failed for: r11v47 */
    /* JADX WARNING: type inference failed for: r9v76 */
    /* JADX WARNING: type inference failed for: r4v79 */
    /* JADX WARNING: type inference failed for: r10v60 */
    /* JADX WARNING: type inference failed for: r11v48 */
    /* JADX WARNING: type inference failed for: r9v77 */
    /* JADX WARNING: type inference failed for: r9v78 */
    /* JADX WARNING: type inference failed for: r9v79 */
    /* JADX WARNING: type inference failed for: r4v80 */
    /* JADX WARNING: type inference failed for: r10v61 */
    /* JADX WARNING: type inference failed for: r9v80 */
    /* JADX WARNING: type inference failed for: r4v81 */
    /* JADX WARNING: type inference failed for: r9v81 */
    /* JADX WARNING: type inference failed for: r10v62 */
    /* JADX WARNING: type inference failed for: r11v49 */
    /* JADX WARNING: type inference failed for: r15v40 */
    /* JADX WARNING: type inference failed for: r14v31 */
    /* JADX WARNING: type inference failed for: r13v35 */
    /* JADX WARNING: type inference failed for: r12v46 */
    /* JADX WARNING: type inference failed for: r9v82 */
    /* JADX WARNING: type inference failed for: r5v32 */
    /* JADX WARNING: type inference failed for: r5v33 */
    /* JADX WARNING: type inference failed for: r9v83 */
    /* JADX WARNING: type inference failed for: r9v84 */
    /* JADX WARNING: type inference failed for: r4v82 */
    /* JADX WARNING: type inference failed for: r10v63 */
    /* JADX WARNING: type inference failed for: r9v85 */
    /* JADX WARNING: type inference failed for: r4v83 */
    /* JADX WARNING: type inference failed for: r9v86 */
    /* JADX WARNING: type inference failed for: r10v64 */
    /* JADX WARNING: type inference failed for: r11v50 */
    /* JADX WARNING: type inference failed for: r10v65 */
    /* JADX WARNING: type inference failed for: r9v87 */
    /* JADX WARNING: type inference failed for: r4v84 */
    /* JADX WARNING: type inference failed for: r11v51 */
    /* JADX WARNING: type inference failed for: r4v85 */
    /* JADX WARNING: type inference failed for: r9v88 */
    /* JADX WARNING: type inference failed for: r10v66 */
    /* JADX WARNING: type inference failed for: r4v86 */
    /* JADX WARNING: type inference failed for: r10v67 */
    /* JADX WARNING: type inference failed for: r11v52 */
    /* JADX WARNING: type inference failed for: r9v89 */
    /* JADX WARNING: type inference failed for: r4v87 */
    /* JADX WARNING: type inference failed for: r10v68 */
    /* JADX WARNING: type inference failed for: r11v53 */
    /* JADX WARNING: type inference failed for: r9v90 */
    /* JADX WARNING: type inference failed for: r4v88 */
    /* JADX WARNING: type inference failed for: r9v91 */
    /* JADX WARNING: type inference failed for: r10v69 */
    /* JADX WARNING: type inference failed for: r11v54 */
    /* JADX WARNING: type inference failed for: r4v89 */
    /* JADX WARNING: type inference failed for: r9v92 */
    /* JADX WARNING: type inference failed for: r10v70 */
    /* JADX WARNING: type inference failed for: r11v55 */
    /* JADX WARNING: type inference failed for: r4v90 */
    /* JADX WARNING: type inference failed for: r9v93 */
    /* JADX WARNING: type inference failed for: r10v71 */
    /* JADX WARNING: type inference failed for: r11v56 */
    /* JADX WARNING: type inference failed for: r4v91 */
    /* JADX WARNING: type inference failed for: r9v94 */
    /* JADX WARNING: type inference failed for: r10v72 */
    /* JADX WARNING: type inference failed for: r15v41 */
    /* JADX WARNING: type inference failed for: r14v32 */
    /* JADX WARNING: type inference failed for: r13v36 */
    /* JADX WARNING: type inference failed for: r12v47 */
    /* JADX WARNING: type inference failed for: r10v73 */
    /* JADX WARNING: type inference failed for: r9v95 */
    /* JADX WARNING: type inference failed for: r5v34 */
    /* JADX WARNING: type inference failed for: r15v42 */
    /* JADX WARNING: type inference failed for: r14v33 */
    /* JADX WARNING: type inference failed for: r13v37 */
    /* JADX WARNING: type inference failed for: r12v48 */
    /* JADX WARNING: type inference failed for: r10v74 */
    /* JADX WARNING: type inference failed for: r5v35 */
    /* JADX WARNING: type inference failed for: r9v96 */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0458, code lost:
        if (r13 == 0) goto L_0x0469;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x045e, code lost:
        if (r4.isSwitchOn() != false) goto L_0x0469;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0464, code lost:
        if (r2.getTag() == null) goto L_0x0469;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0466, code lost:
        if (r14 == 0) goto L_0x0469;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x0469, code lost:
        r15 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x046a, code lost:
        com.android.camera.animation.FolmeUtils.touchItemScale(r2);
        r1.itemView.setEnabled(r5);
        r2.setTag(java.lang.Integer.valueOf(r3));
        r1 = 0;
        r4.setSwitchOn(r13, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x047d, code lost:
        if (r13 == 0) goto L_0x0491;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x047f, code lost:
        if (r15 != false) goto L_0x048e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0481, code lost:
        r21.cancelAnimation();
        r6 = r21;
        r6.setImageResource(r18);
        r1 = androidx.core.view.ViewCompat.MEASURED_STATE_MASK;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x048e, code lost:
        r6 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0491, code lost:
        r6 = r21;
        r9 = r18;
        r6.cancelAnimation();
        r6.setImageResource(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x049b, code lost:
        r6.setColorFilter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x049e, code lost:
        if (r15 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x04a0, code lost:
        r6.setImageDrawable(null);
        r6.setScale(r0.mAnimationScaleSize);
        r6.O0000O0o((int) r14);
        r6.setProgress(1.0f);
        r6.O0000oO();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x009c, code lost:
        r10 = r10;
        r9 = r9;
        r4 = r4;
        r11 = r11.isSwitchOn(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0019, code lost:
        r5 = 1;
        r10 = 0;
        r12 = 0;
        r13 = 0;
        r14 = 0;
        r15 = 0;
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0020, code lost:
        r15 = r15;
        r14 = r14;
        r13 = r13;
        r12 = r12;
        r10 = r10;
        r5 = r5;
        r9 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x01c2, code lost:
        r13 = r4;
        r5 = 1;
        r12 = 0;
        r15 = r10;
        r14 = r11;
        r9 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x01dc, code lost:
        r15 = r4;
        r5 = 1;
        r12 = 0;
        r14 = r9;
        r9 = r10;
        r13 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0021, code lost:
        r11 = null;
        r15 = r15;
        r14 = r14;
        r13 = r13;
        r12 = r12;
        r10 = r10;
        r9 = r9;
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0241, code lost:
        r13 = r4;
        r5 = 1;
        r12 = 0;
        r15 = r9;
        r14 = r10;
        r9 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0247, code lost:
        r4 = null;
        r11 = null;
        r10 = r12;
        r15 = r15;
        r14 = r14;
        r13 = r13;
        r12 = r12;
        r9 = r9;
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0264, code lost:
        r13 = r4;
        r5 = 1;
        r12 = 0;
        r14 = 0;
        r15 = r10;
        r9 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0269, code lost:
        r4 = null;
        r11 = null;
        r15 = r15;
        r14 = r14;
        r13 = r13;
        r12 = r12;
        r9 = r9;
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x026b, code lost:
        r10 = r14;
        r15 = r15;
        r14 = r14;
        r13 = r13;
        r12 = r12;
        r9 = r9;
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x027b, code lost:
        r5 = 1;
        r10 = 0;
        r12 = 0;
        r13 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x027f, code lost:
        r14 = r13;
        r15 = r14;
        r13 = r13;
        r12 = r12;
        r10 = r10;
        r5 = r5;
        r14 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x02f5, code lost:
        r8 = r1.itemView.getResources();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x02ff, code lost:
        if (getItemViewType(r2) != 1) goto L_0x0329;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0301, code lost:
        r5 = !r4.disableUpdate();
        r1 = (com.android.camera.ui.SlideSwitchButton) r1.getView(com.android.camera.R.id.top_config_ratio);
        r1.setComponentData(r4, r3, r12);
        r1.setEnabled(r5);
        r1.setSlideSwitchListener(r0.mSlideSwitchListener);
        r0 = getMuitiContentDescriptionString(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x031e, code lost:
        if (r0 <= 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0320, code lost:
        r1.setContentDescription(r8.getString(r0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0329, code lost:
        r2 = r1.getView(com.android.camera.R.id.extra_tootle_item_layout);
        r4 = (com.android.camera.ui.ColorCircleBackgroundView) r1.getView(com.android.camera.R.id.extra_tootle_item_image_bg);
        r6 = (com.airbnb.lottie.LottieAnimationView) r1.getView(com.android.camera.R.id.extra_tootle_item_image);
        r12 = r1.getView(com.android.camera.R.id.rl_item_top_config_description);
        r7 = (android.widget.TextView) r1.getView(com.android.camera.R.id.extra_tootle_item_text);
        r18 = r15;
        r7.setSelected(true);
        r15 = (android.widget.ImageView) r1.getView(com.android.camera.R.id.iv_tag_config);
        r21 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0364, code lost:
        if (r9 == -1) goto L_0x036a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0366, code lost:
        r7.setText(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x036a, code lost:
        r7.setText(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x036d, code lost:
        r9 = ",";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0370, code lost:
        if (r10 == 0) goto L_0x03fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0376, code lost:
        if (com.android.camera.Util.isAccessible() == false) goto L_0x0384;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0378, code lost:
        r1.itemView.setImportantForAccessibility(2);
        r2.setImportantForAccessibility(1);
        r12.setImportantForAccessibility(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0384, code lost:
        r15.setVisibility(0);
        r2.setOnClickListener(r0.mOnClickListener);
        r12.setOnClickListener(r0.mOnClickListener);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0392, code lost:
        if (r13 == 0) goto L_0x03de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0394, code lost:
        r6 = com.android.camera.CameraSettings.getTimerBurstTotalCount();
        r7 = com.android.camera.CameraSettings.getTimerBurstInterval();
        r10 = new java.lang.StringBuilder();
        r10.append(r8.getString(com.android.camera.R.string.accessibility_timer_burst_image_on));
        r10.append(r9);
        r10.append(r8.getQuantityString(com.android.camera.R.plurals.accessibility_timer_burst_count, r6, new java.lang.Object[]{java.lang.Integer.valueOf(r6)}));
        r10.append(r9);
        r10.append(r8.getQuantityString(com.android.camera.R.plurals.accessibility_timer_burst_interval, r7, new java.lang.Object[]{java.lang.Integer.valueOf(r7)}));
        r6 = r10.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x03de, code lost:
        r6 = r8.getString(com.android.camera.R.string.accessibility_timer_burst_image_off);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x03e5, code lost:
        r2.setContentDescription(r6);
        r12.setContentDescription(r8.getString(com.android.camera.R.string.accessibility_timer_burst_text));
        r12.setTag(java.lang.Integer.valueOf(r3));
        r15 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x03fb, code lost:
        r15.setVisibility(8);
        r1.itemView.setTag(java.lang.Integer.valueOf(r3));
        r10 = new java.lang.StringBuilder();
        r10.append(r7.getText());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0415, code lost:
        if (r13 == 0) goto L_0x0425;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x0417, code lost:
        r10.append(r9);
        r7 = com.android.camera.R.string.accessibility_open;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x041d, code lost:
        r10.append(r8.getString(r7));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0427, code lost:
        if (r3 == 225) goto L_0x0430;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0429, code lost:
        r10.append(r9);
        r7 = com.android.camera.R.string.accessibility_closed;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x0430, code lost:
        r2.setContentDescription(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0437, code lost:
        if (com.android.camera.Util.isAccessible() == false) goto L_0x0452;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0439, code lost:
        r15 = true;
        r1.itemView.setImportantForAccessibility(1);
        r2.setImportantForAccessibility(2);
        r12.setImportantForAccessibility(2);
        r1.itemView.setOnClickListener(r0.mOnClickListener);
        r1.itemView.setContentDescription(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0452, code lost:
        r15 = true;
        r2.setOnClickListener(r0.mOnClickListener);
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r15v13
  assigns: []
  uses: []
  mth insns count: 546
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
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 157 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBindViewHolder(CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        ? r15;
        ? r14;
        ? r13;
        ? r12;
        CharSequence charSequence;
        ? r10;
        ? r9;
        ? r5;
        ComponentData componentData;
        ? r152;
        ? r142;
        ? r132;
        ? r122;
        ? r92;
        ? r52;
        ? r133;
        ? r53;
        ? r123;
        ? r153;
        ? r143;
        ComponentData componentData2;
        CharSequence charSequence2;
        StringBuilder sb;
        ComponentData componentData3;
        ? r154;
        ? r144;
        ? r134;
        ? r124;
        ? r93;
        ? r54;
        ? r102;
        ? r94;
        ? r4;
        ? r11;
        ? r103;
        ? r95;
        ? r42;
        ? r112;
        ? r104;
        ? r96;
        ? r43;
        ? r105;
        ? r155;
        ? r145;
        ? r135;
        ? r125;
        ? r97;
        ? r55;
        String str;
        DataItemRunning dataItemRunning;
        ? r106;
        ? r98;
        ? r44;
        CommonRecyclerViewHolder commonRecyclerViewHolder2 = commonRecyclerViewHolder;
        int i2 = i;
        int config = this.mSupportedConfigs.getConfig(i2);
        int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
        switch (config) {
            case 170:
                ? isTimerBurstEnable = CameraSettings.isTimerBurstEnable();
                if (isTimerBurstEnable != 0) {
                    int timerBurstTotalCount = CameraSettings.getTimerBurstTotalCount();
                    String format = String.format(this.mContext.getResources().getQuantityString(R.plurals.timer_burst_setting_total_count, timerBurstTotalCount), new Object[]{String.valueOf(timerBurstTotalCount)});
                    String str2 = "S";
                    if (Util.isLocaleChinese()) {
                        sb = new StringBuilder();
                    } else {
                        sb = new StringBuilder();
                        sb.append(" ");
                    }
                    sb.append(CameraSettings.getTimerBurstInterval());
                    sb.append(str2);
                    String sb2 = sb.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(format);
                    sb3.append(sb2);
                    charSequence2 = sb3.toString();
                    r133 = isTimerBurstEnable;
                    r53 = 1;
                    r123 = 0;
                    r153 = R.drawable.vector_drawable_ic_timer_burst;
                    r143 = R.raw.extra_anim_timer_burst;
                    componentData2 = null;
                    r92 = -1;
                } else {
                    r133 = isTimerBurstEnable;
                    r53 = 1;
                    r123 = 0;
                    r153 = R.drawable.vector_drawable_ic_timer_burst;
                    r143 = R.raw.extra_anim_timer_burst;
                    r92 = R.string.timer_burst;
                    componentData2 = null;
                    charSequence2 = null;
                }
                r10 = r52;
                r15 = r152;
                r14 = r142;
                r13 = r132;
                r12 = r122;
                r9 = r92;
                r5 = r52;
                break;
            case 173:
                componentData3 = this.mDataItemConfig.getComponentConfigVideoSubQuality();
                break;
            case 174:
                componentData3 = this.mDataItemConfig.getComponentConfigVideoSubFps();
                break;
            case 175:
                ComponentRunningAiEnhancedVideo componentRunningAiEnhancedVideo = this.mDataItemRunning.getComponentRunningAiEnhancedVideo();
                r4 = componentRunningAiEnhancedVideo.isSwitchOn(currentMode);
                r102 = 2131231626;
                r94 = componentRunningAiEnhancedVideo.getResText();
                break;
            case 187:
                componentData3 = this.mDataItemLive.getComponentLiveVideoQuality();
                break;
            case 199:
                r42 = this.mDataItemRunning.isSwitchOn("pref_camera_peak_key");
                r95 = 2131231217;
                r103 = 2131689482;
                r11 = 2131756171;
                break;
            case 204:
                componentData3 = this.mDataItemConfig.getComponentConfigSlowMotion();
                break;
            case 206:
                r42 = CameraSettings.isLiveShotOn();
                r95 = 2131231634;
                r103 = 2131689491;
                r11 = 2131755484;
                break;
            case 208:
                componentData3 = this.mDataItemConfig.getComponentConfigVideoQuality();
                break;
            case 209:
                ComponentRunningUltraPixel componentUltraPixel = this.mDataItemRunning.getComponentUltraPixel();
                ? menuDrawable = componentUltraPixel.getMenuDrawable();
                charSequence = componentUltraPixel.getMenuString();
                ? r56 = 1;
                ? r126 = 0;
                ? r146 = 0;
                ? r156 = menuDrawable;
                ? r136 = CameraSettings.isUltraPixelOn();
                componentData = null;
                ? r99 = -1;
                break;
            case 210:
                componentData3 = this.mDataItemConfig.getComponentConfigRatio();
                ? r57 = 1;
                ? r127 = 1;
                ? r107 = 0;
                ? r137 = 0;
                break;
            case 213:
                componentData3 = this.mDataItemConfig.getComponentConfigSlowMotionQuality();
                break;
            case 219:
                ? r45 = 2131231584;
                ? r910 = 2131689488;
                ? r108 = 2131756444;
                ? r113 = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_REFERENCE_LINE, false);
                break;
            case 220:
                ComponentRunningSubtitle componentRunningSubtitle = this.mDataItemRunning.getComponentRunningSubtitle();
                r43 = componentRunningSubtitle.isSwitchOn(currentMode);
                r104 = 2131231586;
                r112 = 2131689493;
                r96 = componentRunningSubtitle.getResText();
                break;
            case 221:
                r134 = this.mDataItemRunning.getComponentRunningDocument().isSwitchOn(currentMode);
                r54 = 1;
                r124 = 0;
                r144 = 0;
                r154 = R.drawable.document_mode_normal;
                r93 = R.string.pref_document_mode;
                break;
            case 223:
                ComponentRunningAIWatermark componentRunningAIWatermark = this.mDataItemRunning.getComponentRunningAIWatermark();
                ? r911 = 2131755302;
                int cameraId = CameraSettings.getCameraId();
                boolean z = Camera2DataContainer.getInstance().getUltraWideCameraId() == cameraId || Camera2DataContainer.getInstance().getStandaloneMacroCameraId() == cameraId;
                if (z) {
                    this.mDataItemRunning.getComponentRunningAIWatermark().setAIWatermarkEnable(false);
                    r105 = 0;
                } else {
                    r105 = 1;
                }
                r134 = componentRunningAIWatermark.getAIWatermarkEnable();
                r124 = 0;
                r144 = 0;
                r54 = r105;
                r154 = R.drawable.ic_ai_watermark_enter_normal;
                r93 = r911;
                break;
            case 225:
                ? r157 = R.drawable.ic_vector_config_extra_setting;
                ? r58 = 1;
                ? r109 = 0;
                ? r128 = 0;
                ? r138 = 0;
                ? r147 = 0;
                componentData3 = null;
                ? r912 = 2131755585;
                break;
            case 226:
                componentData3 = this.mDataItemRunning.getComponentRunningTimer();
                break;
            case 227:
                ComponentRunningColorEnhance componentRunningColorEnhance = DataRepository.dataItemRunning().getComponentRunningColorEnhance();
                ? isEnabled = componentRunningColorEnhance.isEnabled(currentMode);
                ? resIcon = componentRunningColorEnhance.getResIcon(isEnabled);
                r4 = isEnabled;
                r102 = resIcon;
                r94 = componentRunningColorEnhance.getDisplayTitleString();
                break;
            case 228:
                r43 = this.mDataItemRunning.getComponentRunningTiltValue().isSwitchOn(160);
                r96 = 2131755589;
                r104 = 2131231587;
                r112 = 2131689494;
                break;
            case 229:
                ? isGradienterOn = CameraSettings.isGradienterOn();
                r5 = 1;
                if (!CameraSettings.isAutoZoomEnabled(currentMode)) {
                    r15 = R.drawable.ic_vector_config_straighten;
                    r14 = R.raw.extra_anim_gradienter;
                    r9 = R.string.config_name_straighten;
                    r13 = isGradienterOn;
                    componentData = null;
                    charSequence = null;
                    r10 = 0;
                    r12 = 0;
                    break;
                } else {
                    r122 = 0;
                    r132 = 0;
                    r152 = R.drawable.ic_vector_config_straighten;
                    r142 = R.raw.extra_anim_gradienter;
                    r97 = R.string.config_name_straighten;
                    r55 = r5;
                }
            case 233:
                r154 = R.drawable.ic_config_fast_motion;
                r54 = 1;
                r124 = 0;
                r144 = 0;
                r134 = ModuleManager.isFastMotionModule();
                r93 = 2131756691;
                break;
            case 237:
                r94 = 2131756421;
                r4 = this.mDataItemConfig.getComponentConfigRaw().isSwitchOn(currentMode);
                r102 = 2131231527;
                break;
            case 242:
                if (Util.isGlobalVersion()) {
                    r152 = R.drawable.ic_config_ai_glens;
                    r55 = 1;
                    r122 = 0;
                    r132 = 0;
                    r142 = R.raw.extra_anim_google;
                    r97 = 2131756583;
                    componentData = null;
                    charSequence = null;
                    r10 = r135;
                    r15 = r155;
                    r14 = r145;
                    r13 = r135;
                    r12 = r125;
                    r9 = r97;
                    r5 = r55;
                    break;
                }
                break;
            case 251:
                r42 = CameraSettings.isCinematicAspectRatioEnabled(currentMode);
                r95 = 2131231576;
                r103 = 2131689480;
                r11 = 2131756011;
                break;
            case 252:
                dataItemRunning = this.mDataItemRunning;
                str = "pref_hand_gesture";
                r44 = 2131231223;
                r98 = 2131689485;
                r106 = 2131755755;
                break;
            case 253:
                ComponentRunningAutoZoom componentRunningAutoZoom = this.mDataItemRunning.getComponentRunningAutoZoom();
                r43 = componentRunningAutoZoom.isSwitchOn(currentMode);
                r104 = 2131231565;
                r112 = 2131689479;
                r96 = componentRunningAutoZoom.getResText();
                break;
            case 255:
                ComponentRunningMacroMode componentRunningMacroMode = this.mDataItemRunning.getComponentRunningMacroMode();
                r43 = componentRunningMacroMode.isSwitchOn(currentMode);
                r104 = 2131231627;
                r112 = 2131689490;
                r96 = componentRunningMacroMode.getResText();
                break;
            case 258:
                r42 = this.mDataItemRunning.isSwitchOn("pref_camera_exposure_feedback");
                r95 = 2131231213;
                r103 = 2131689481;
                r11 = 2131756170;
                break;
            case 260:
                r42 = CameraSettings.isProVideoLogOpen(currentMode);
                r95 = 2131231589;
                r103 = 2131689489;
                r11 = 2131755853;
                break;
            case 261:
                r42 = CameraSettings.isProVideoHistogramOpen(currentMode);
                r95 = 2131231583;
                r103 = 2131689486;
                r11 = 2131756053;
                break;
            case 262:
                dataItemRunning = this.mDataItemRunning;
                str = "pref_speech_shutter";
                r44 = 2131231242;
                r98 = 2131689492;
                r106 = 2131756768;
                break;
        }
    }

    public void onClick(View view) {
        this.mOnClickListener.onClick(view);
    }

    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater from;
        int i2;
        if (i == 1) {
            from = LayoutInflater.from(viewGroup.getContext());
            i2 = R.layout.fragment_top_config_extra_mulit_item;
        } else if (i == 2) {
            from = LayoutInflater.from(viewGroup.getContext());
            i2 = R.layout.fragment_top_config_extra_toggle_item;
        } else {
            view = null;
            return new CommonRecyclerViewHolder(view);
        }
        view = from.inflate(i2, viewGroup, false);
        return new CommonRecyclerViewHolder(view);
    }

    public void onViewAttachedToWindow(CommonRecyclerViewHolder commonRecyclerViewHolder) {
        super.onViewAttachedToWindow(commonRecyclerViewHolder);
        int totalRow = getTotalRow();
        if (this.mCurrentRow != totalRow) {
            ViewCompat.setAlpha(commonRecyclerViewHolder.itemView, 0.0f);
            int row = getRow(commonRecyclerViewHolder.getAdapterPosition());
            int i = this.mCurrentRow;
            if (row > i) {
                if (i > -1) {
                    setAnimation(this.mRowLists, i, totalRow);
                }
                this.mRowLists.clear();
                this.mRowLists.add(commonRecyclerViewHolder.itemView);
                this.mCurrentRow = row;
            } else if (row == i) {
                this.mRowLists.add(commonRecyclerViewHolder.itemView);
            }
            if (commonRecyclerViewHolder.getAdapterPosition() == getItemCount() - 1) {
                setAnimation(this.mRowLists, this.mCurrentRow, totalRow);
                this.mCurrentRow++;
            }
        }
    }

    public void setAnimateHeight(int i) {
        this.mAnimateHeight = i;
    }
}

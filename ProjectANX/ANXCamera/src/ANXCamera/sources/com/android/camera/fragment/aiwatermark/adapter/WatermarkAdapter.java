package com.android.camera.fragment.aiwatermark.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.CameraSettings;
import com.android.camera.LocationManager;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.aiwatermark.lisenter.IPermissionListener;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;
import com.android.camera.fragment.aiwatermark.holder.WatermarkHolder;
import com.android.camera.log.Log;
import com.android.camera.permission.PermissionManager;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AIWatermarkDetect;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.MainContentProtocol;
import com.android.camera.protocol.ModeProtocol.WatermarkProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.AIWatermark;
import com.android.camera.statistic.MistatsConstants.SuperMoon;
import java.util.ArrayList;
import java.util.List;

public class WatermarkAdapter extends Adapter implements OnClickListener {
    /* access modifiers changed from: private */
    public static final String TAG = "WatermarkAdapter";
    /* access modifiers changed from: private */
    public boolean mAllowLocationAccess = false;
    private ComponentRunningAIWatermark mComponentRunningAIWatermark;
    private int mDegree;
    /* access modifiers changed from: private */
    public Fragment mFragment;
    private int mHorizontalPadding = 0;
    protected List mItems = new ArrayList();
    protected LayoutInflater mLayoutInflater;
    private LinearLayoutManager mLayoutManager;
    /* access modifiers changed from: private */
    public AlertDialog mLocationCTADialog = null;
    /* access modifiers changed from: private */
    public AlertDialog mPermissionNotAskDialog = null;
    private int mSelectedIndex;

    public WatermarkAdapter(Context context, LinearLayoutManager linearLayoutManager, int i, List list, Fragment fragment) {
        this.mItems = list;
        this.mLayoutManager = linearLayoutManager;
        this.mSelectedIndex = i;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mHorizontalPadding = context.getResources().getDimensionPixelSize(R.dimen.beautycamera_makeup_item_margin);
        this.mComponentRunningAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
        this.mFragment = fragment;
    }

    /* access modifiers changed from: private */
    public void apply(String str, int i, WatermarkItem watermarkItem) {
        String str2 = WatermarkConstant.AI_TRIGGER;
        if (TextUtils.equals(str, str2) || TextUtils.equals(this.mComponentRunningAIWatermark.getCurrentKey(), str2)) {
            updateASDStatus();
        }
        this.mComponentRunningAIWatermark.setCurrentKey(str);
        boolean equals = TextUtils.equals(((WatermarkItem) this.mItems.get(i)).getKey(), str2);
        DataRepository.dataItemRunning().getComponentRunningAIWatermark().setIWatermarkEnable(equals);
        if (equals) {
            ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).showWatermarkSample(true);
        } else {
            updateWatermark(watermarkItem, true);
        }
        onSelected(i, true);
        int intValue = Integer.valueOf(this.mComponentRunningAIWatermark.getCurrentType()).intValue();
        if (intValue == 11 || intValue == 12) {
            CameraStatUtils.trackSuperMoonClick(SuperMoon.PARAM_SUPER_MOON_EFFECT_SELECT);
            CameraStatUtils.trackSuperMoonEffectKey(str);
            return;
        }
        CameraStatUtils.trackAIWatermarkClick(AIWatermark.AI_WATERMARK_SELECT);
        CameraStatUtils.trackAIWatermarkKey(str);
    }

    /* access modifiers changed from: private */
    public void checkLocationPermission(String str, int i, WatermarkItem watermarkItem) {
        if (!PermissionManager.checkCameraLocationPermissions()) {
            WatermarkProtocol watermarkProtocol = (WatermarkProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(253);
            if (watermarkProtocol != null) {
                watermarkProtocol.requestLocationPermission(getListener(str, i, watermarkItem));
                return;
            }
            return;
        }
        LocationManager.instance().recordLocation(true);
        apply(str, i, watermarkItem);
    }

    private IPermissionListener getListener(final String str, final int i, final WatermarkItem watermarkItem) {
        return new IPermissionListener() {
            public void onPermissionResult(boolean z) {
                String access$000 = WatermarkAdapter.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onPermissionResult result =");
                sb.append(z);
                Log.u(access$000, sb.toString());
                if (z) {
                    LocationManager.instance().recordLocation(true);
                    WatermarkAdapter.this.apply(str, i, watermarkItem);
                    return;
                }
                final FragmentActivity activity = WatermarkAdapter.this.mFragment.getActivity();
                if (activity != null && WatermarkAdapter.this.mPermissionNotAskDialog == null) {
                    WatermarkAdapter.this.mPermissionNotAskDialog = RotateDialogController.showSystemAlertDialog(activity, null, activity.getString(R.string.location_permission_not_ask_again), activity.getString(R.string.location_permission_not_ask_again_go_to_settings), new Runnable() {
                        public void run() {
                            Log.u(WatermarkAdapter.TAG, "onClick startActivity Settings.ACTION_APPLICATION_DETAILS_SETTINGS positive");
                            FragmentActivity fragmentActivity = activity;
                            StringBuilder sb = new StringBuilder();
                            sb.append("package:");
                            sb.append(activity.getPackageName());
                            fragmentActivity.startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse(sb.toString())));
                        }
                    }, null, null, activity.getString(17039360), O000000o.INSTANCE);
                    WatermarkAdapter.this.mPermissionNotAskDialog.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialogInterface) {
                            WatermarkAdapter.this.mPermissionNotAskDialog = null;
                        }
                    });
                }
            }
        };
    }

    private void notifyItemChanged(int i, int i2) {
        if (i > -1) {
            notifyItemChanged(i, Boolean.valueOf(false));
        }
        if (i2 > -1) {
            notifyItemChanged(i2, Boolean.valueOf(true));
        }
    }

    private void scrollIfNeed(int i) {
        if (i == this.mLayoutManager.findFirstVisibleItemPosition() || i == this.mLayoutManager.findFirstCompletelyVisibleItemPosition()) {
            int i2 = this.mHorizontalPadding;
            View findViewByPosition = this.mLayoutManager.findViewByPosition(i);
            if (i > 0 && findViewByPosition != null) {
                i2 = (this.mHorizontalPadding * 2) + findViewByPosition.getWidth();
            }
            this.mLayoutManager.scrollToPositionWithOffset(Math.max(0, i), i2);
        } else if (i == this.mLayoutManager.findLastVisibleItemPosition() || i == this.mLayoutManager.findLastCompletelyVisibleItemPosition()) {
            this.mLayoutManager.scrollToPosition(Math.min(i + 1, getItemCount() - 1));
        }
    }

    private void setAccessible(View view, int i, boolean z) {
        int i2;
        if (view != null) {
            List list = this.mItems;
            if (list != null && list.get(i) != null) {
                WatermarkItem watermarkItem = (WatermarkItem) this.mItems.get(i);
                if (watermarkItem.getText() == null) {
                    i2 = 0;
                } else {
                    i2 = view.getContext().getResources().getIdentifier(watermarkItem.getText(), "string", view.getContext().getPackageName());
                }
                Context context = view.getContext();
                if (i2 <= 0) {
                    i2 = R.string.lighting_pattern_null;
                }
                String string = context.getString(i2);
                if (z) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(string);
                    sb.append(", ");
                    sb.append(view.getContext().getString(R.string.accessibility_selected));
                    view.setContentDescription(sb.toString());
                    if (Util.isAccessible()) {
                        view.postDelayed(new O00000Oo(view), 100);
                        return;
                    }
                    return;
                }
                view.setContentDescription(string);
            }
        }
    }

    private void showCTADialog(final String str, final int i, final WatermarkItem watermarkItem) {
        if (this.mLocationCTADialog == null) {
            if (CameraSettings.isAllowCTA()) {
                checkLocationPermission(str, i, watermarkItem);
                return;
            }
            this.mLocationCTADialog = RotateDialogController.showCTADialog(this.mFragment.getActivity(), this.mFragment.getActivity().getString(R.string.user_agree), new Runnable() {
                public void run() {
                    Log.u(WatermarkAdapter.TAG, "showCTADialog onClick positive");
                    WatermarkAdapter.this.mAllowLocationAccess = true;
                    CameraSettings.updateCTAPreference(true);
                }
            }, this.mFragment.getActivity().getString(R.string.user_disagree), new Runnable() {
                public void run() {
                    Log.u(WatermarkAdapter.TAG, "showCTADialog onClick negative");
                    WatermarkAdapter.this.mAllowLocationAccess = false;
                    CameraSettings.updateCTAPreference(false);
                }
            }, this.mLayoutInflater.inflate(R.layout.dialog_cta, null));
            this.mLocationCTADialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    String access$000 = WatermarkAdapter.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("showCTADialog onDismiss mAllowLocationAccess=");
                    sb.append(WatermarkAdapter.this.mAllowLocationAccess);
                    Log.u(access$000, sb.toString());
                    if (WatermarkAdapter.this.mAllowLocationAccess) {
                        WatermarkAdapter.this.checkLocationPermission(str, i, watermarkItem);
                    } else {
                        CameraSettings.updateRecordLocationPreference(false);
                    }
                    WatermarkAdapter.this.mLocationCTADialog.setOnDismissListener(null);
                    WatermarkAdapter.this.mLocationCTADialog = null;
                }
            });
        }
    }

    private void updateASDStatus() {
        AIWatermarkDetect aIWatermarkDetect = (AIWatermarkDetect) ModeCoordinatorImpl.getInstance().getAttachProtocol(254);
        if (aIWatermarkDetect != null) {
            aIWatermarkDetect.resetResult();
        }
        ((ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164)).updateASDForWatermark();
    }

    private void updateSelectItem(WatermarkHolder watermarkHolder, int i) {
        boolean z = false;
        int i2 = TextUtils.equals(((WatermarkItem) this.mItems.get(i)).getKey(), this.mComponentRunningAIWatermark.getCurrentKey()) ? 0 : 8;
        watermarkHolder.updateSelectItem(i2);
        View view = watermarkHolder.itemView;
        if (i2 == 0) {
            z = true;
        }
        setAccessible(view, i, z);
    }

    private void updateWatermark(WatermarkItem watermarkItem) {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.updateWatermarkSample(watermarkItem);
        }
    }

    private void updateWatermark(WatermarkItem watermarkItem, boolean z) {
        MainContentProtocol mainContentProtocol = (MainContentProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(166);
        if (mainContentProtocol != null) {
            mainContentProtocol.updateWatermarkSample(watermarkItem, z);
        }
    }

    public int getItemCount() {
        List list = this.mItems;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public List getItems() {
        return this.mItems;
    }

    public int getSelectedIndex() {
        return this.mSelectedIndex;
    }

    public void onBindViewHolder(@NonNull WatermarkHolder watermarkHolder, int i) {
        watermarkHolder.bindHolder(i, (WatermarkItem) this.mItems.get(i));
        watermarkHolder.itemView.setTag(Integer.valueOf(i));
        watermarkHolder.itemView.setOnClickListener(this);
        updateSelectItem(watermarkHolder, i);
        watermarkHolder.itemView.setRotation((float) this.mDegree);
    }

    public void onBindViewHolder(@NonNull WatermarkHolder watermarkHolder, int i, @NonNull List list) {
        if (list.isEmpty()) {
            onBindViewHolder(watermarkHolder, i);
        } else if (list.get(0) instanceof Boolean) {
            boolean booleanValue = ((Boolean) list.get(list.size() - 1)).booleanValue();
            watermarkHolder.itemView.setActivated(booleanValue);
            if (watermarkHolder.getSelectedIndicator() != null) {
                Util.updateSelectIndicator(watermarkHolder.getSelectedIndicator(), booleanValue, true);
                Util.correctionSelectView(watermarkHolder.getImageView(), booleanValue);
            }
            setAccessible(watermarkHolder.itemView, i, booleanValue);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0066, code lost:
        if (r2.equals("location") != false) goto L_0x0088;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        int i;
        boolean z = false;
        try {
            i = Integer.parseInt(view.getTag().toString());
        } catch (NumberFormatException unused) {
            Log.e(TAG, "Object can not cast to Integer");
            i = 0;
        }
        WatermarkItem watermarkItem = (WatermarkItem) this.mItems.get(i);
        String key = watermarkItem.getKey();
        if (TextUtils.equals(key, this.mComponentRunningAIWatermark.getCurrentKey())) {
            Log.d(TAG, "user touch the same item. do nothing.");
            return;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onClick: index=");
        sb.append(i);
        sb.append(" key=");
        sb.append(key);
        Log.u(str, sb.toString());
        switch (key.hashCode()) {
            case 37820169:
                if (key.equals(WatermarkConstant.LOCATION_TIME_1)) {
                    z = true;
                    break;
                }
            case 37820170:
                if (key.equals(WatermarkConstant.LOCATION_TIME_2)) {
                    z = true;
                    break;
                }
            case 1888438524:
                if (key.equals(WatermarkConstant.LONGITUDE_LATITUDE)) {
                    z = true;
                    break;
                }
            case 1901043637:
                break;
            default:
                z = true;
                break;
        }
        if (!z || z || z || z) {
            showCTADialog(key, i, watermarkItem);
        } else {
            apply(key, i, watermarkItem);
        }
    }

    @NonNull
    public WatermarkHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreateViewHolder i = ");
        sb.append(i);
        Log.d(str, sb.toString());
        View inflate = this.mLayoutInflater.inflate(R.layout.watermark_recyclerview_item, viewGroup, false);
        WatermarkHolder watermarkHolder = new WatermarkHolder(inflate);
        FolmeUtils.touchScale(inflate);
        return watermarkHolder;
    }

    public void onSelected(int i, boolean z) {
        int i2 = this.mSelectedIndex;
        if (i2 != i) {
            this.mSelectedIndex = i;
            if (z) {
                scrollIfNeed(i);
                notifyItemChanged(i2, this.mSelectedIndex);
                return;
            }
            notifyDataSetChanged();
        }
    }

    public void setRotation(int i) {
        this.mDegree = i;
    }
}

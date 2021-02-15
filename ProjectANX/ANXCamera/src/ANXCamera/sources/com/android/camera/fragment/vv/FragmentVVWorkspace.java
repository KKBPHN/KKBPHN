package com.android.camera.fragment.vv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.folme.FolmeAlphaInOnSubscribe;
import com.android.camera.animation.folme.FolmeAlphaOutOnSubscribe;
import com.android.camera.animation.type.SlideInOnSubscribe;
import com.android.camera.animation.type.SlideOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.VlogViewModel;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.dialog.BaseDialogFragment;
import com.android.camera.log.Log;
import com.android.camera.module.AudioController;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.VLogAttr;
import com.android.camera.visibilityutils.calculator.DefaultSingleItemCalculatorCallback;
import com.android.camera.visibilityutils.calculator.ListItemsVisibilityCalculator;
import com.android.camera.visibilityutils.calculator.SingleListViewItemActiveCalculator;
import com.android.camera.visibilityutils.scroll_utils.ItemsPositionGetter;
import com.android.camera.visibilityutils.scroll_utils.RecyclerViewItemPositionGetter;
import io.reactivex.Completable;
import java.util.ArrayList;
import java.util.List;
import miuix.recyclerview.widget.RecyclerView;

public class FragmentVVWorkspace extends BaseDialogFragment implements OnClickListener {
    public static final String TAG = "FragmentVVWorkspace";
    /* access modifiers changed from: private */
    public VVWorkspaceAdapter mAdapter;
    private AudioController mAudioController;
    private ImageView mBackImage;
    /* access modifiers changed from: private */
    public AlertDialog mDeleteDialog;
    private View mDeleteLayout;
    private View mDeleteParentLayout;
    private ImageView mEditExitImage;
    private ImageView mEditImage;
    private View mEmptyView;
    /* access modifiers changed from: private */
    public FragmentVVWorkspaceFooterItemDecoration mFooterItemDecoration;
    /* access modifiers changed from: private */
    public ItemsPositionGetter mItemsPositionGetter;
    /* access modifiers changed from: private */
    public LinearLayoutManagerWrapper mLayoutManager;
    /* access modifiers changed from: private */
    public List mPlayerItemList;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public int mScrollState = 0;
    private ImageView mSelectAllImage;
    private TextView mTitleText;
    private VVList mVVList;
    /* access modifiers changed from: private */
    public ListItemsVisibilityCalculator mVideoVisibilityCalculator;
    private VVWorkspace vvWorkspace;

    class FragmentVVWorkspaceFooterItemDecoration extends ItemDecoration {
        private int offset;

        public FragmentVVWorkspaceFooterItemDecoration(int i) {
            this.offset = i;
        }

        public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull State state) {
            super.getItemOffsets(rect, view, recyclerView, state);
            if (recyclerView.getChildAdapterPosition(view) == recyclerView.getAdapter().getItemCount() - 1) {
                rect.set(0, 0, 0, this.offset);
            } else {
                rect.set(0, 0, 0, 0);
            }
        }
    }

    class FragmentVVWorkspaceItemDecoration extends ItemDecoration {
        private int commonBottomOffset;
        private int firstTopOffset;
        private int lastBottomOffset;

        public FragmentVVWorkspaceItemDecoration(int i, int i2, int i3) {
            this.firstTopOffset = i;
            this.commonBottomOffset = i2;
            this.lastBottomOffset = i3;
        }

        public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull State state) {
            super.getItemOffsets(rect, view, recyclerView, state);
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            boolean z = true;
            boolean z2 = childAdapterPosition == 0;
            if (childAdapterPosition != recyclerView.getAdapter().getItemCount() - 1) {
                z = false;
            }
            if (z2) {
                rect.set(0, this.firstTopOffset, 0, this.commonBottomOffset);
            } else {
                rect.set(0, 0, 0, z ? this.lastBottomOffset : this.commonBottomOffset);
            }
        }
    }

    private void exit() {
        Log.u(TAG, "exit");
        FragmentVVPreview fragmentVVPreview = (FragmentVVPreview) FragmentUtils.getFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_VV_PREVIEW));
        if (fragmentVVPreview != null && fragmentVVPreview.isVisible()) {
            fragmentVVPreview.controlPlay(true);
        }
        dismiss();
    }

    private boolean inEditMode() {
        VVWorkspaceAdapter vVWorkspaceAdapter = this.mAdapter;
        if (vVWorkspaceAdapter == null) {
            return false;
        }
        return vVWorkspaceAdapter.isEditMode();
    }

    private void initCalculator() {
        this.mVideoVisibilityCalculator = new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), this.mPlayerItemList);
        this.mItemsPositionGetter = new RecyclerViewItemPositionGetter(this.mLayoutManager, this.mRecyclerView);
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(androidx.recyclerview.widget.RecyclerView recyclerView, int i) {
                FragmentVVWorkspace.this.mScrollState = i;
                if (i == 0 && !FragmentVVWorkspace.this.mPlayerItemList.isEmpty()) {
                    FragmentVVWorkspace.this.mVideoVisibilityCalculator.onScrollStateIdle(FragmentVVWorkspace.this.mItemsPositionGetter, FragmentVVWorkspace.this.mLayoutManager.findFirstVisibleItemPosition(), FragmentVVWorkspace.this.mLayoutManager.findLastVisibleItemPosition());
                }
            }

            public void onScrolled(androidx.recyclerview.widget.RecyclerView recyclerView, int i, int i2) {
                if (!FragmentVVWorkspace.this.mPlayerItemList.isEmpty()) {
                    FragmentVVWorkspace.this.mVideoVisibilityCalculator.onScroll(FragmentVVWorkspace.this.mItemsPositionGetter, FragmentVVWorkspace.this.mLayoutManager.findFirstVisibleItemPosition(), (FragmentVVWorkspace.this.mLayoutManager.findLastVisibleItemPosition() - FragmentVVWorkspace.this.mLayoutManager.findFirstVisibleItemPosition()) + 1, FragmentVVWorkspace.this.mScrollState);
                }
            }
        });
    }

    private void initView(View view) {
        View findViewById = view.findViewById(R.id.vv_work_space_head);
        ((MarginLayoutParams) findViewById.getLayoutParams()).topMargin = Display.getTopMargin();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.vv_workspace_delete_layout_fix);
        boolean isFullScreenNavBarHidden = Util.isFullScreenNavBarHidden(getContext());
        this.mDeleteParentLayout = view.findViewById(R.id.vv_delete_parent_layout);
        this.mDeleteParentLayout.setTag(Integer.valueOf(-1));
        this.mDeleteLayout = view.findViewById(R.id.vv_delete_layout);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mDeleteLayout.getLayoutParams();
        marginLayoutParams.bottomMargin = isFullScreenNavBarHidden ? dimensionPixelSize : Display.getNavigationBarHeight();
        ((MarginLayoutParams) view.findViewById(R.id.vv_delete_bottom).getLayoutParams()).height = marginLayoutParams.bottomMargin;
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.vv_work_space_list);
        this.mEmptyView = view.findViewById(R.id.vv_workspace_empty_view);
        this.mBackImage = (ImageView) findViewById.findViewById(R.id.vv_workspace_back);
        this.mEditImage = (ImageView) findViewById.findViewById(R.id.vv_workspace_edit);
        this.mTitleText = (TextView) findViewById.findViewById(R.id.vv_workspace_title);
        this.mBackImage.setOnClickListener(this);
        this.mEditImage.setOnClickListener(this);
        this.mDeleteLayout.setOnClickListener(this);
        FolmeUtils.touchTint(this.mBackImage, this.mEditImage, this.mDeleteLayout);
        if (Util.isLayoutRTL(getContext())) {
            this.mBackImage.setRotation(180.0f);
        }
        this.vvWorkspace = new VVWorkspace();
        this.vvWorkspace.restoreWorkspace();
        if (!showEmptyView()) {
            this.mVVList = ((VlogViewModel) DataRepository.dataItemObservable().get(VlogViewModel.class)).getVVList();
            this.mPlayerItemList = new ArrayList(this.vvWorkspace.getList().size());
            for (VVWorkspaceItem vVWorkspaceItem : this.vvWorkspace.getList()) {
                this.mPlayerItemList.add(new VVWorkspacePlayerItem(vVWorkspaceItem, (VVItem) this.mVVList.getItemById(vVWorkspaceItem.mTemplateId)));
            }
            this.mPlayerItemList.add(new VVWorkspacePlayerItem(null, null));
            this.mAdapter = new VVWorkspaceAdapter(this, this.vvWorkspace.getList(), this.mPlayerItemList, this);
            this.mLayoutManager = new LinearLayoutManagerWrapper(getContext(), "vv_workspace");
            this.mLayoutManager.setOrientation(1);
            this.mRecyclerView.setLayoutManager(this.mLayoutManager);
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mRecyclerView.getLayoutParams();
            int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.mimoji_edit_config_bottom);
            if (isFullScreenNavBarHidden) {
                dimensionPixelSize2 = dimensionPixelSize;
            }
            marginLayoutParams2.bottomMargin = dimensionPixelSize2;
            this.mRecyclerView.addItemDecoration(new FragmentVVWorkspaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.vv_workspace_list_margin_top), getResources().getDimensionPixelSize(R.dimen.vv_workspace_margin_bottom), getResources().getDimensionPixelSize(R.dimen.vv_workspace_margin_bottom)));
            this.mRecyclerView.setAdapter(this.mAdapter);
            initCalculator();
        }
    }

    /* access modifiers changed from: private */
    public void restoreOuterAudio() {
        if (this.mAudioController == null) {
            this.mAudioController = new AudioController(getActivity().getApplicationContext());
        }
        this.mAudioController.restoreMusicSteam(getActivity());
        getActivity().setVolumeControlStream(1);
    }

    /* access modifiers changed from: private */
    public void setEditMode(boolean z, boolean z2) {
        ImageView imageView;
        int i;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setEditMode ");
        sb.append(z2);
        Log.u(str, sb.toString());
        this.mAdapter.setEditMode(z2);
        if (!z2) {
            this.mAdapter.selected(false);
        }
        this.mAdapter.stopAll();
        this.mAdapter.notifyDataSetChanged();
        if (z) {
            FolmeAlphaOutOnSubscribe.directSetResult(this.mTitleText);
            FolmeAlphaOutOnSubscribe.directSetResult(this.mBackImage);
            FolmeAlphaOutOnSubscribe.directSetResult(this.mEditImage);
        }
        if (z2) {
            this.mTitleText.setText(R.string.live_workspace_title_edit);
            this.mBackImage.setImageResource(R.drawable.ic_vector_workspace_cancel);
            imageView = this.mEditImage;
            i = R.drawable.ic_vector_workspace_select;
        } else {
            this.mTitleText.setText(R.string.live_workspace_title);
            updateSelectedCount();
            this.mBackImage.setImageResource(R.drawable.ic_fastmotion_description_back);
            imageView = this.mEditImage;
            i = R.drawable.ic_vector_workspace_delete;
        }
        imageView.setImageResource(i);
        if (z) {
            Completable.create(new FolmeAlphaInOnSubscribe(this.mTitleText).setStartDelayTime(100)).subscribe();
            Completable.create(new FolmeAlphaInOnSubscribe(this.mBackImage).setStartDelayTime(100)).subscribe();
            Completable.create(new FolmeAlphaInOnSubscribe(this.mEditImage).setStartDelayTime(100)).subscribe();
        }
    }

    /* access modifiers changed from: private */
    public boolean showEmptyView() {
        if (!this.vvWorkspace.isEmpty()) {
            return false;
        }
        this.mEmptyView.setVisibility(0);
        this.mEditImage.setVisibility(8);
        return true;
    }

    private void silenceOuterAudio() {
        if (this.mAudioController == null) {
            this.mAudioController = new AudioController(getActivity().getApplicationContext());
        }
        this.mAudioController.requestMusicSteam(getActivity());
    }

    private void updateSelectedCount() {
        int selectedCount = this.mAdapter.getSelectedCount();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateSelectedCount ");
        sb.append(selectedCount);
        Log.u(str, sb.toString());
        boolean z = true;
        boolean z2 = selectedCount > 0;
        int i = z2 ? 1 : -1;
        if (this.mDeleteParentLayout.getTag() == null || ((Integer) this.mDeleteParentLayout.getTag()).intValue() != i) {
            this.mDeleteParentLayout.setTag(Integer.valueOf(i));
            Completable.create(z2 ? new SlideInOnSubscribe(this.mDeleteParentLayout, 80) : new SlideOutOnSubscribe(this.mDeleteParentLayout, 80)).subscribe();
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mDeleteLayout.getLayoutParams();
            int dimensionPixelSize = ((marginLayoutParams.height + marginLayoutParams.bottomMargin) - ((MarginLayoutParams) this.mRecyclerView.getLayoutParams()).bottomMargin) - getResources().getDimensionPixelSize(R.dimen.vv_workspace_margin_top);
            if (this.mRecyclerView.getAdapter().getItemCount() >= 3 && !this.mRecyclerView.canScrollVertically(1)) {
                z = false;
            }
            if (z2) {
                this.mFooterItemDecoration = new FragmentVVWorkspaceFooterItemDecoration(dimensionPixelSize);
                this.mRecyclerView.addItemDecoration(this.mFooterItemDecoration);
            } else {
                FragmentVVWorkspaceFooterItemDecoration fragmentVVWorkspaceFooterItemDecoration = this.mFooterItemDecoration;
                if (fragmentVVWorkspaceFooterItemDecoration != null) {
                    if (!z) {
                        this.mRecyclerView.smoothScrollBy(0, -dimensionPixelSize);
                        this.mRecyclerView.postDelayed(new Runnable() {
                            public void run() {
                                if (FragmentVVWorkspace.this.isAdded()) {
                                    FragmentVVWorkspace.this.mRecyclerView.removeItemDecoration(FragmentVVWorkspace.this.mFooterItemDecoration);
                                }
                            }
                        }, 300);
                    } else {
                        this.mRecyclerView.removeItemDecoration(fragmentVVWorkspaceFooterItemDecoration);
                    }
                }
            }
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vv_delete_layout /*2131297216*/:
                Log.u(TAG, "onClick: vv_delete_layout");
                CameraStatUtils.trackVVWorkspaceClick(VLogAttr.VALUE_VV_CLICK_WORKSPACE_DELETE);
                this.mDeleteDialog = RotateDialogController.showSystemAlertDialog(getActivity(), getResources().getQuantityString(R.plurals.live_workspace_delete_dialog_title, this.mAdapter.getSelectedCount(), new Object[]{Integer.valueOf(this.mAdapter.getSelectedCount())}), null, getText(R.string.live_reverse_confirm), new Runnable() {
                    public void run() {
                        Log.u(FragmentVVWorkspace.TAG, "mDeleteDialog onClick positive");
                        CameraStatUtils.trackVVWorkspaceDeleteConfirm(FragmentVVWorkspace.this.mAdapter.getSelectedCount());
                        FragmentVVWorkspace.this.mAdapter.deleteSelected();
                        if (FragmentVVWorkspace.this.showEmptyView()) {
                            FragmentVVWorkspace.this.setEditMode(true, false);
                        }
                    }
                }, null, null, getString(R.string.mimoji_cancle), O0000o.INSTANCE);
                this.mDeleteDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        FragmentVVWorkspace.this.restoreOuterAudio();
                        FragmentVVWorkspace.this.mDeleteDialog = null;
                    }
                });
                break;
            case R.id.vv_workspace_back /*2131297279*/:
                Log.u(TAG, "onClick: vv_workspace_back");
                if (!inEditMode()) {
                    exit();
                    break;
                } else {
                    setEditMode(true, false);
                    break;
                }
            case R.id.vv_workspace_delete /*2131297281*/:
            case R.id.vv_workspace_video /*2131297290*/:
                Log.u(TAG, "onClick: vv_workspace_delete");
            case R.id.vv_workspace_edit /*2131297282*/:
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onClick: vv_workspace_edit, inEditMode=");
                sb.append(inEditMode());
                Log.u(str, sb.toString());
                if (!inEditMode()) {
                    CameraStatUtils.trackVVWorkspaceClick(VLogAttr.VALUE_VV_CLICK_WORKSPACE_EDIT);
                    setEditMode(true, true);
                    break;
                } else {
                    CameraStatUtils.trackVVWorkspaceClick(VLogAttr.VALUE_VV_CLICK_WORKSPACE_SELECT_ALL);
                    this.mAdapter.selected(!this.mAdapter.isSelectedAll());
                }
            case R.id.vv_workspace_shot /*2131297288*/:
                Log.u(TAG, "onClick: vv_workspace_shot");
                CameraStatUtils.trackVVWorkspaceClick(VLogAttr.VALUE_VV_CLICK_WORKSPACE_CONTINUE);
                dismiss();
                VVWorkspaceItem vVWorkspaceItem = (VVWorkspaceItem) view.getTag();
                ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
                if (configChanges != null) {
                    VVItem vVItem = (VVItem) this.mVVList.getItemById(vVWorkspaceItem.mTemplateId);
                    if (vVItem == null) {
                        Log.d(TAG, "createFromRawInfo");
                        vVItem = VVItem.createFromRawInfo(vVWorkspaceItem.getRawInfoPath());
                        if (vVItem == null) {
                            Log.d(TAG, "create failed");
                            return;
                        }
                    }
                    configChanges.configLiveVV(vVItem, vVWorkspaceItem, true, false);
                    break;
                } else {
                    return;
                }
        }
        updateSelectedCount();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_vv_workspace, viewGroup, false);
        initView(inflate);
        getDialog().setCanceledOnTouchOutside(false);
        silenceOuterAudio();
        return inflate;
    }

    public void onDestroy() {
        super.onDestroy();
        FolmeUtils.clean(this.mBackImage);
        FolmeUtils.clean(this.mEditImage);
        FolmeUtils.clean(this.mDeleteLayout);
    }

    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getAction() == 1) {
            if (inEditMode()) {
                setEditMode(true, false);
                return true;
            }
            exit();
            return true;
        } else if (i == 25 || i == 24) {
            return super.onKey(dialogInterface, i, keyEvent);
        } else {
            return false;
        }
    }

    public void onPause() {
        super.onPause();
        AlertDialog alertDialog = this.mDeleteDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        VVWorkspaceAdapter vVWorkspaceAdapter = this.mAdapter;
        if (vVWorkspaceAdapter != null) {
            vVWorkspaceAdapter.stopAll();
        }
        dismiss();
    }

    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(this);
        List list = this.mPlayerItemList;
        if (list != null && !list.isEmpty()) {
            this.mRecyclerView.post(new Runnable() {
                public void run() {
                    FragmentVVWorkspace.this.mVideoVisibilityCalculator.onScrollStateIdle(FragmentVVWorkspace.this.mItemsPositionGetter, FragmentVVWorkspace.this.mLayoutManager.findFirstVisibleItemPosition(), FragmentVVWorkspace.this.mLayoutManager.findLastVisibleItemPosition());
                }
            });
        }
    }
}

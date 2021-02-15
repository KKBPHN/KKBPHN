package com.android.camera.fragment.vv;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.android.camera.log.Log;
import com.android.camera.module.loader.NullHolder;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.VLogAttr;
import com.android.camera.ui.TextureVideoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VVWorkspaceAdapter extends Adapter implements OnClickListener, OnCheckedChangeListener {
    public static final String TAG = "VVWorkspaceAdapter";
    private boolean mEditMode;
    private Fragment mFragment;
    private RequestOptions mGlideOptions = new RequestOptions();
    private OnClickListener mOnClickListener;
    private List mPlayerItemList;
    private List mWorkspaceList;

    public VVWorkspaceAdapter(Fragment fragment, List list, List list2, OnClickListener onClickListener) {
        this.mFragment = fragment;
        this.mWorkspaceList = list;
        this.mOnClickListener = onClickListener;
        this.mGlideOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        this.mPlayerItemList = list2;
    }

    private String formatTime(long j) {
        return DateFormat.format("MM/dd kk:mm", j).toString();
    }

    private String getDurationString(long j) {
        int floor = (int) Math.floor((double) (((float) j) / 1000.0f));
        return String.format(Locale.ENGLISH, "%02d", new Object[]{Integer.valueOf(Math.abs(floor))});
    }

    private void loadThumbnailByLastSegment(String str, final ImageView imageView) {
        Object tag = imageView.getTag();
        if (tag != null && (tag instanceof Disposable)) {
            ((Disposable) tag).dispose();
        }
        Glide.with(this.mFragment).clear((View) imageView);
        imageView.setTag(Single.just(str).map(new Function() {
            public NullHolder apply(String str) {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(str);
                return NullHolder.ofNullable(mediaMetadataRetriever.getFrameAtTime(0));
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
            public void accept(NullHolder nullHolder) {
                if (nullHolder.isPresent()) {
                    imageView.setImageBitmap((Bitmap) nullHolder.get());
                }
            }
        }, new Consumer() {
            public void accept(Throwable th) {
                th.printStackTrace();
            }
        }));
    }

    private boolean loadThumbnailByThumbFile(String str, ImageView imageView) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        if (!file.exists() || file.length() < 50) {
            return false;
        }
        Object tag = imageView.getTag();
        if (tag != null && (tag instanceof Disposable)) {
            ((Disposable) tag).dispose();
            imageView.setTag(null);
        }
        Glide.with(this.mFragment).load(str).apply((BaseRequestOptions) this.mGlideOptions).into(imageView);
        return true;
    }

    public void deleteSelected() {
        stopAll();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < this.mWorkspaceList.size(); i++) {
            VVWorkspaceItem vVWorkspaceItem = (VVWorkspaceItem) this.mWorkspaceList.get(i);
            if (vVWorkspaceItem.mMarked) {
                vVWorkspaceItem.removeSelf();
                arrayList.add(vVWorkspaceItem);
                arrayList2.add((VVWorkspacePlayerItem) this.mPlayerItemList.get(i));
            }
        }
        this.mWorkspaceList.removeAll(arrayList);
        this.mPlayerItemList.removeAll(arrayList2);
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.mWorkspaceList.size();
    }

    public int getSelectedCount() {
        int i = 0;
        for (VVWorkspaceItem vVWorkspaceItem : this.mWorkspaceList) {
            if (vVWorkspaceItem.mMarked) {
                i++;
            }
        }
        return i;
    }

    public boolean isEditMode() {
        return this.mEditMode;
    }

    public boolean isSelectedAll() {
        for (VVWorkspaceItem vVWorkspaceItem : this.mWorkspaceList) {
            if (!vVWorkspaceItem.mMarked) {
                return false;
            }
        }
        return true;
    }

    public void onBindViewHolder(@NonNull CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        commonRecyclerViewHolder.getView(R.id.vv_workspace_image_layout);
        ImageView imageView = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_workspace_image);
        TextureVideoView textureVideoView = (TextureVideoView) commonRecyclerViewHolder.getView(R.id.vv_workspace_video);
        ImageView imageView2 = (ImageView) commonRecyclerViewHolder.getView(R.id.vv_workspace_play);
        TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.vv_workspace_create_time);
        TextView textView2 = (TextView) commonRecyclerViewHolder.getView(R.id.vv_workspace_hint);
        TextView textView3 = (TextView) commonRecyclerViewHolder.getView(R.id.vv_workspace_shot);
        CheckBox checkBox = (CheckBox) commonRecyclerViewHolder.getView(R.id.vv_workspace_delete);
        VVWorkspaceItem vVWorkspaceItem = (VVWorkspaceItem) this.mWorkspaceList.get(i);
        VVWorkspacePlayerItem vVWorkspacePlayerItem = (VVWorkspacePlayerItem) this.mPlayerItemList.get(i);
        VVItem vVItem = vVWorkspacePlayerItem.getVVItem();
        if (vVItem != null) {
            vVWorkspacePlayerItem.updateTargetVideoView(imageView, textureVideoView, imageView2);
            imageView2.setVisibility(0);
            if (this.mEditMode) {
                textView3.setVisibility(8);
                textureVideoView.stop();
                checkBox.setVisibility(0);
                checkBox.setChecked(vVWorkspaceItem.mMarked);
            } else {
                textView3.setVisibility(0);
                checkBox.setVisibility(8);
                checkBox.setChecked(false);
            }
            if (!loadThumbnailByThumbFile(vVWorkspaceItem.getTargetThumbPath(), imageView)) {
                Glide.with(this.mFragment).load(vVWorkspaceItem.getLastPath()).apply((BaseRequestOptions) this.mGlideOptions).into(imageView);
            }
            textView.setText(formatTime(vVWorkspaceItem.mLastModifiedTime));
            textView2.setText(commonRecyclerViewHolder.itemView.getContext().getResources().getString(R.string.vv_duartion_hint, new Object[]{vVItem.name, Integer.valueOf(vVItem.getEssentialFragmentSize()), getDurationString(vVItem.getTotalDuration())}));
            imageView2.setTag(vVWorkspacePlayerItem);
            imageView2.setOnClickListener(this);
            textureVideoView.setTag(vVWorkspacePlayerItem);
            textureVideoView.setOnClickListener(this);
            textView3.setTag(vVWorkspaceItem);
            textView3.setOnClickListener(this);
            checkBox.setTag(vVWorkspaceItem);
            checkBox.setOnClickListener(this);
            FolmeUtils.handleListItemTouch(imageView2);
            FolmeUtils.handleListItemTouch(checkBox);
            FolmeUtils.touchTint((View) textView3);
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
    }

    public void onClick(View view) {
        VVWorkspaceItem vVWorkspaceItem;
        switch (view.getId()) {
            case R.id.vv_workspace_delete /*2131297281*/:
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onClick: vv_workspace_delete, isEditMode()=");
                sb.append(isEditMode());
                Log.u(str, sb.toString());
                if (isEditMode()) {
                    vVWorkspaceItem = (VVWorkspaceItem) view.getTag();
                    break;
                } else {
                    return;
                }
            case R.id.vv_workspace_play /*2131297287*/:
                Log.u(TAG, "onClick: vv_workspace_play");
                CameraStatUtils.trackVVWorkspaceClick(VLogAttr.VALUE_VV_CLICK_WORKSPACE_PLAY);
                stopAll();
                ((VVWorkspacePlayerItem) view.getTag()).startPlay();
                return;
            case R.id.vv_workspace_shot /*2131297288*/:
                Log.u(TAG, "onClick: vv_workspace_shot");
                break;
            case R.id.vv_workspace_video /*2131297290*/:
                Log.u(TAG, "onClick: vv_workspace_video");
                VVWorkspacePlayerItem vVWorkspacePlayerItem = (VVWorkspacePlayerItem) view.getTag();
                if (vVWorkspacePlayerItem.isPlaying()) {
                    vVWorkspacePlayerItem.stopPlay();
                    return;
                } else if (isEditMode()) {
                    vVWorkspaceItem = vVWorkspacePlayerItem.getVVWorkspaceItem();
                    break;
                } else {
                    return;
                }
            default:
                return;
        }
        vVWorkspaceItem.mMarked = !vVWorkspaceItem.mMarked;
        notifyDataSetChanged();
        this.mOnClickListener.onClick(view);
    }

    @NonNull
    public CommonRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(this.mFragment.getContext()).inflate(R.layout.fragment_vv_workspace_item, viewGroup, false));
    }

    public void selected(boolean z) {
        for (VVWorkspaceItem vVWorkspaceItem : this.mWorkspaceList) {
            vVWorkspaceItem.mMarked = z;
        }
        notifyDataSetChanged();
    }

    public void setEditMode(boolean z) {
        this.mEditMode = z;
    }

    public void stopAll() {
        for (VVWorkspacePlayerItem stopPlay : this.mPlayerItemList) {
            stopPlay.stopPlay();
        }
    }
}

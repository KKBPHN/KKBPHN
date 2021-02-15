package com.android.camera.fragment.mimoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerAdapter;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerViewHolder;
import com.android.camera.fragment.music.RoundedCornersTransformation;
import com.android.camera.log.Log;
import com.arcsoft.avatar.AvatarConfig.ASAvatarConfigInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory.Builder;
import java.util.ArrayList;

public class MimojiThumbnailRecyclerAdapter extends BaseRecyclerAdapter {
    private Context mContext;
    private String mName;
    private float selectIndex;

    class ThumbnailViewViewHolder extends BaseRecyclerViewHolder {
        ImageView imageView;
        String name;

        public ThumbnailViewViewHolder(@NonNull View view, String str) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail_image_view);
            this.name = str;
        }

        public /* synthetic */ void O0000oO0(String str) {
            this.itemView.announceForAccessibility(str);
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x0074  */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0082  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0088  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x00c7  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setData(ASAvatarConfigInfo aSAvatarConfigInfo, int i) {
            boolean z;
            new Builder(300).setCrossFadeEnabled(true).build();
            if (aSAvatarConfigInfo != null) {
                Bitmap bitmap = aSAvatarConfigInfo.thum;
                if (bitmap != null && !bitmap.isRecycled()) {
                    Glide.with(this.itemView.getContext()).load(aSAvatarConfigInfo.thum).apply(new RequestOptions().placeholder(this.imageView.getDrawable())).apply((BaseRequestOptions) RequestOptions.bitmapTransform(new RoundedCornersTransformation(20, 1))).into(this.imageView);
                    z = aSAvatarConfigInfo == null && MimojiThumbnailRecyclerAdapter.getSelectItem(aSAvatarConfigInfo.configType) == ((float) aSAvatarConfigInfo.configID);
                    this.imageView.setBackground(!z ? this.itemView.getContext().getDrawable(R.drawable.bg_mimoji_thumbnail_selected) : null);
                    if (!z) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(this.name);
                        sb.append(i + 1);
                        sb.append(", ");
                        sb.append(this.imageView.getContext().getString(R.string.accessibility_selected));
                        String sb2 = sb.toString();
                        this.itemView.setContentDescription(sb2);
                        if (Util.isAccessible()) {
                            this.itemView.postDelayed(new O0000o0(this, sb2), 100);
                            return;
                        }
                        return;
                    }
                    View view = this.itemView;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(this.name);
                    sb3.append(i + 1);
                    view.setContentDescription(sb3.toString());
                    return;
                }
            }
            Log.e(ThumbnailViewViewHolder.class.getSimpleName(), "fmoji bitmap isRecycled");
            if (aSAvatarConfigInfo == null) {
            }
            this.imageView.setBackground(!z ? this.itemView.getContext().getDrawable(R.drawable.bg_mimoji_thumbnail_selected) : null);
            if (!z) {
            }
        }
    }

    public MimojiThumbnailRecyclerAdapter(Context context, int i, String str) {
        this(null);
        this.mContext = context;
        this.selectIndex = getSelectItem(i);
        this.mName = str;
    }

    public MimojiThumbnailRecyclerAdapter(ArrayList arrayList) {
        super(arrayList);
        this.selectIndex = -1.0f;
    }

    public static float getSelectItem(int i) {
        return AvatarEngineManager.getInstance().getInnerConfigSelectIndex(i);
    }

    /* access modifiers changed from: protected */
    public BaseRecyclerViewHolder onCreateBaseRecyclerViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ThumbnailViewViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mimoji_thumbnail_view, viewGroup, false), this.mName);
    }

    public void setSelectItem(int i, int i2) {
        AvatarEngineManager instance = AvatarEngineManager.getInstance();
        if (instance != null) {
            instance.setInnerConfigSelectIndex(i, (float) i2);
        }
    }
}

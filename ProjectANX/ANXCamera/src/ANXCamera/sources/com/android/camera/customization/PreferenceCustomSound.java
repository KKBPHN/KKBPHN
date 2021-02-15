package com.android.camera.customization;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsWrapper;
import java.util.ArrayList;
import java.util.List;

public class PreferenceCustomSound extends Preference implements OnLoadCompleteListener, OnClickListener {
    private static final String TAG = "PrefCustomSound";
    /* access modifiers changed from: private */
    public List mAvailableSounds;
    private int mColum;
    private View mLastItemView;
    private List mPreviewSoundIds;
    private int mSelectedPosition;
    private SoundPool mSoundPool;
    private int mSoundToPlay = -1;

    class MyViewHolder extends ViewHolder {
        public MyViewHolder(@NonNull View view) {
            super(view);
        }
    }

    public PreferenceCustomSound(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PreferenceCustomSound(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public PreferenceCustomSound(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    /* access modifiers changed from: private */
    public void bindItem(View view, int i) {
        ImageView imageView = (ImageView) view.findViewById(R.id.indicator);
        ShutterSound shutterSound = (ShutterSound) this.mAvailableSounds.get(i);
        ((ImageView) view.findViewById(R.id.thumbnail)).setImageResource(shutterSound.cover());
        imageView.setVisibility(i == this.mSelectedPosition ? 0 : 8);
        ((TextView) view.findViewById(R.id.item_text)).setText(shutterSound.title());
        view.setTag(Integer.valueOf(i));
        view.setOnClickListener(this);
        FolmeUtils.handleListItemTouch(view);
        if (i == this.mSelectedPosition) {
            this.mLastItemView = view;
        }
    }

    private void init() {
        this.mAvailableSounds = ShutterSound.loadAvailableSounds();
        this.mSelectedPosition = ShutterSound.read();
        Builder builder = new Builder();
        int i = 1;
        builder.setMaxStreams(1);
        AudioAttributes.Builder builder2 = new AudioAttributes.Builder();
        if (!C0124O00000oO.Oo00o0O()) {
            i = 7;
        }
        builder.setAudioAttributes(builder2.setInternalLegacyStreamType(i).build());
        this.mSoundPool = builder.build();
        this.mSoundPool.setOnLoadCompleteListener(this);
        this.mPreviewSoundIds = new ArrayList(this.mAvailableSounds.size());
        for (int i2 = 0; i2 < this.mAvailableSounds.size(); i2++) {
            this.mPreviewSoundIds.add(Integer.valueOf(-1));
        }
        this.mColum = getContext().getResources().getInteger(R.integer.custom_sound_recyclerview_column);
    }

    private void refreshItemView(View view) {
        if (view != null) {
            ((ImageView) view.findViewById(R.id.indicator)).setVisibility(((Integer) view.getTag()).intValue() == this.mSelectedPosition ? 0 : 8);
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        RecyclerView recyclerView = (RecyclerView) preferenceViewHolder.itemView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), this.mColum));
        recyclerView.setAdapter(new Adapter() {
            public int getItemCount() {
                return PreferenceCustomSound.this.mAvailableSounds.size();
            }

            public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
                PreferenceCustomSound.this.bindItem(viewHolder.itemView, i);
            }

            @NonNull
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new MyViewHolder(LayoutInflater.from(PreferenceCustomSound.this.getContext()).inflate(R.layout.shutter_sound_item, null));
            }
        });
    }

    public void onClick(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        StringBuilder sb = new StringBuilder();
        sb.append("You selected ");
        sb.append(this.mAvailableSounds.get(intValue));
        sb.append(", position ");
        sb.append(intValue);
        Log.u(TAG, sb.toString());
        this.mSelectedPosition = intValue;
        ShutterSound.persist(intValue);
        refreshItemView(this.mLastItemView);
        refreshItemView(view);
        if (((Integer) this.mPreviewSoundIds.get(intValue)).intValue() == -1) {
            int loadFromAsset = ShutterSound.loadFromAsset(((ShutterSound) this.mAvailableSounds.get(intValue)).soundPath(0), this.mSoundPool);
            this.mSoundToPlay = loadFromAsset;
            this.mPreviewSoundIds.set(intValue, Integer.valueOf(loadFromAsset));
        } else {
            this.mSoundToPlay = -1;
            this.mSoundPool.play(((Integer) this.mPreviewSoundIds.get(intValue)).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
        }
        this.mLastItemView = view;
        MistatsWrapper.settingClickEvent("attr_edit_sound", ((ShutterSound) this.mAvailableSounds.get(intValue)).getFolderName());
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null) {
            soundPool.release();
            this.mSoundPool = null;
        }
    }

    public void onLoadComplete(SoundPool soundPool, int i, int i2) {
        if (this.mSoundToPlay == i && i2 == 0) {
            soundPool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
            this.mSoundToPlay = -1;
        }
    }
}

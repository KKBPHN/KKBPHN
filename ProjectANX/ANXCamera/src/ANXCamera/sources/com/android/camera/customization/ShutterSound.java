package com.android.camera.customization;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShutterSound {
    private static final String BASE_DIR = "sounds/";
    public static final String KEY_SHUTTER_SOUND = "key_shutter_sound";
    private static final String LOG_TAG = "ShutterSound";
    private static int[] commonSoundIds = new int[commonSounds.length];
    private static final String[] commonSounds = {"sounds/sound_shuter_delay_bee.ogg", "/system/media/audio/ui/NumberPickerValueChange.ogg", "sounds/audio_capture.ogg"};
    private static List sAvailableSounds = loadAvailableSounds();
    private static final String[] soundNames = {"camera_click.ogg", "camera_focus.ogg", "video_record_start.ogg", "video_record_end.ogg", "camera_fast_burst.ogg"};
    private final String folderName;
    private final int mCover;
    private final int mTitle;
    private int[] soundIds = new int[soundNames.length];

    static {
        int[] iArr = commonSoundIds;
        iArr[0] = -1;
        iArr[1] = -1;
        iArr[2] = -1;
    }

    public ShutterSound(int i, String str, int i2) {
        this.mCover = i2;
        this.mTitle = i;
        this.folderName = str;
        int i3 = 0;
        while (true) {
            int[] iArr = this.soundIds;
            if (i3 < iArr.length) {
                iArr[i3] = -1;
                i3++;
            } else {
                return;
            }
        }
    }

    private static boolean isCommonSound(int i) {
        return i >= 5 && i <= 7;
    }

    public static List loadAvailableSounds() {
        List list = sAvailableSounds;
        if (list == null || list.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new ShutterSound(R.string.custom_sound_default, "default", R.drawable.sound_cover_default));
            arrayList.add(new ShutterSound(R.string.custom_sound_art, "art", R.drawable.sound_cover_art));
            arrayList.add(new ShutterSound(R.string.custom_sound_old, "old", R.drawable.sound_cover_old));
            arrayList.add(new ShutterSound(R.string.custom_sound_modern, "modern", R.drawable.sound_cover_modern));
            sAvailableSounds = arrayList;
        }
        return sAvailableSounds;
    }

    private static int loadCommonSound(int i, SoundPool soundPool) {
        int loadFromAsset = !commonSounds[i].startsWith("/") ? loadFromAsset(commonSounds[i], soundPool) : soundPool.load(commonSounds[i], 1);
        commonSoundIds[i] = loadFromAsset;
        return loadFromAsset;
    }

    public static int loadFromAsset(String str, SoundPool soundPool) {
        AssetFileDescriptor assetFileDescriptor = null;
        int i = -1;
        try {
            assetFileDescriptor = CameraAppImpl.getAndroidContext().getAssets().openFd(str);
            i = soundPool.load(assetFileDescriptor, 1);
            if (assetFileDescriptor != null) {
                assetFileDescriptor.close();
            }
        } catch (IOException unused) {
            Log.e(LOG_TAG, "IOException occurs when closing Camera Sound AssetFileDescriptor.");
        } catch (Throwable th) {
            if (assetFileDescriptor != null) {
                assetFileDescriptor.close();
            }
            throw th;
        }
        return i;
    }

    public static int loadSound(SoundPool soundPool, int i) {
        if (isCommonSound(i)) {
            int i2 = i - 5;
            int[] iArr = commonSoundIds;
            return iArr[i2] == -1 ? loadCommonSound(i2, soundPool) : iArr[i2];
        }
        ShutterSound shutterSound = (ShutterSound) sAvailableSounds.get(read());
        if (!shutterSound.loaded(i)) {
            shutterSound.setSoundId(i, loadFromAsset(shutterSound.soundPath(i), soundPool));
        }
        return shutterSound.soundId(i);
    }

    private boolean loaded(int i) {
        return this.soundIds[i] != -1;
    }

    public static void persist(int i) {
        DataRepository.dataItemGlobal().editor().putInt(KEY_SHUTTER_SOUND, i).apply();
    }

    public static int read() {
        int i = DataRepository.dataItemGlobal().getInt(KEY_SHUTTER_SOUND, 0);
        if (i < 0 || i >= sAvailableSounds.size()) {
            return 0;
        }
        return i;
    }

    public static String readSoundName(Context context) {
        return context.getString(((ShutterSound) sAvailableSounds.get(read())).mTitle);
    }

    public static void release() {
        int i = 0;
        while (true) {
            int[] iArr = commonSoundIds;
            if (i >= iArr.length) {
                break;
            }
            iArr[i] = -1;
            i++;
        }
        for (ShutterSound releaseSounds : sAvailableSounds) {
            releaseSounds.releaseSounds();
        }
    }

    private void releaseSounds() {
        int i = 0;
        while (true) {
            int[] iArr = this.soundIds;
            if (i < iArr.length) {
                iArr[i] = -1;
                i++;
            } else {
                return;
            }
        }
    }

    private void setSoundId(int i, int i2) {
        this.soundIds[i] = i2;
    }

    private int soundId(int i) {
        return this.soundIds[i];
    }

    public static int tryPlaySound(int i, SoundPool soundPool, float f, int i2, ArrayList arrayList) {
        if (isCommonSound(i)) {
            int i3 = i - 5;
            int[] iArr = commonSoundIds;
            if (iArr[i3] == -1) {
                return loadCommonSound(i3, soundPool);
            }
            soundPool.play(iArr[i3], f, f, 0, i2 - 1, 1.0f);
            return -1;
        }
        ShutterSound shutterSound = (ShutterSound) sAvailableSounds.get(read());
        if (shutterSound.loaded(i)) {
            soundPool.play(shutterSound.soundId(i), f, f, 0, i2 - 1, 1.0f);
            if (arrayList.isEmpty() || !arrayList.contains(Integer.valueOf(shutterSound.soundId(i)))) {
                return shutterSound.soundId(i);
            }
            return -1;
        }
        shutterSound.setSoundId(i, loadFromAsset(shutterSound.soundPath(i), soundPool));
        return shutterSound.soundId(i);
    }

    public int cover() {
        return this.mCover;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public String soundPath(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_DIR);
        sb.append(this.folderName);
        sb.append("/");
        sb.append(soundNames[i]);
        return sb.toString();
    }

    public int title() {
        return this.mTitle;
    }
}

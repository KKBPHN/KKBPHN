package com.android.camera.fragment.music;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.storage.Storage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    private static final String[] LOCAL_MUSIC_LIST_CN = {"Innervation", "Cheerful"};
    private static final String[] LOCAL_MUSIC_LIST_GLOBAL = {"Smooth", "Autumn", "City", "Sports", "Cheerful", "Morning", "Lovely", "Dynamic", "Fashion", "Chic"};

    public static List getMusicListFromLocalFolder(String str, Context context) {
        ArrayList arrayList = new ArrayList();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        File file = new File(str);
        String string = context.getResources().getString(R.string.live_music_author);
        String[] strArr = Util.isGlobalVersion() ? LOCAL_MUSIC_LIST_GLOBAL : LOCAL_MUSIC_LIST_CN;
        String str2 = Util.isGlobalVersion() ? ".aac" : ".mp3";
        if (file.listFiles() != null) {
            for (String str3 : strArr) {
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(str2);
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(sb2);
                File file2 = new File(sb3.toString());
                if (file2.exists()) {
                    LiveMusicInfo liveMusicInfo = new LiveMusicInfo();
                    mediaMetadataRetriever.setDataSource(file2.getAbsolutePath());
                    String extractMetadata = mediaMetadataRetriever.extractMetadata(7);
                    if (extractMetadata == null) {
                        extractMetadata = sb2.substring(0, sb2.length() - 4);
                    }
                    liveMusicInfo.mTitle = extractMetadata;
                    String extractMetadata2 = mediaMetadataRetriever.extractMetadata(1);
                    if (extractMetadata2 == null) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(FileUtils.MUSIC_LOCAL);
                        sb4.append(extractMetadata);
                        sb4.append(Storage.JPEG_SUFFIX);
                        extractMetadata2 = sb4.toString();
                    }
                    liveMusicInfo.mThumbnailUrl = extractMetadata2;
                    String extractMetadata3 = mediaMetadataRetriever.extractMetadata(2);
                    if (extractMetadata3 == null) {
                        extractMetadata3 = string;
                    }
                    liveMusicInfo.mAuthor = extractMetadata3;
                    String extractMetadata4 = mediaMetadataRetriever.extractMetadata(9);
                    liveMusicInfo.mDurationText = extractMetadata4.substring(0, extractMetadata4.length() - 3);
                    liveMusicInfo.mPlayUrl = file2.getAbsolutePath();
                    arrayList.add(liveMusicInfo);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(liveMusicInfo.mAuthor);
                    String str4 = ", ";
                    sb5.append(str4);
                    sb5.append(liveMusicInfo.mTitle);
                    sb5.append(str4);
                    sb5.append(liveMusicInfo.mPlayUrl);
                    String str5 = ",";
                    sb5.append(str5);
                    sb5.append(liveMusicInfo.mThumbnailUrl);
                    sb5.append(str5);
                    sb5.append(liveMusicInfo.mDurationText);
                    Log.d("LiveMusicInfo", sb5.toString());
                }
            }
        }
        mediaMetadataRetriever.release();
        return arrayList;
    }
}

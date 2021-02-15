package com.android.camera.storage;

import android.content.Context;
import android.text.TextUtils;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.internal.util.CollectionUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.MetaBox;
import org.jcodec.containers.mp4.boxes.MetaValue;
import org.jcodec.containers.mp4.boxes.MovieBox;
import org.jcodec.containers.mp4.boxes.MovieFragmentBox;
import org.jcodec.containers.mp4.boxes.MsrtBox;
import org.jcodec.containers.mp4.boxes.MtagBox;
import org.jcodec.containers.mp4.boxes.NodeBox;
import org.jcodec.containers.mp4.boxes.UdtaBox;
import org.jcodec.movtool.MP4Edit;
import org.jcodec.movtool.RelocateMP4Editor;

@Deprecated
public class VideoTagSaveRequest implements SaveRequest {
    private static final String TAG = "VideoTagSaveRequest";
    private String mFileName;
    /* access modifiers changed from: private */
    public List mTags;

    public class VideoTag {
        private String boxType;
        private byte[] data;
        private String key;

        public VideoTag(String str, byte[] bArr, String str2) {
            this.key = str;
            this.data = bArr;
            this.boxType = str2;
        }

        public String getBoxType() {
            return this.boxType;
        }

        public byte[] getData() {
            return this.data;
        }

        public String getKey() {
            return this.key;
        }
    }

    /* access modifiers changed from: private */
    public void addBox(MovieBox movieBox, String str, byte[] bArr) {
        UdtaBox udtaBox = (UdtaBox) NodeBox.findFirst(movieBox, UdtaBox.class, UdtaBox.fourcc());
        if (udtaBox == null) {
            udtaBox = UdtaBox.createUdtaBox();
            movieBox.add(udtaBox);
        }
        Box box = null;
        if (MsrtBox.fourcc().equals(str)) {
            box = MsrtBox.createMsrtBox(bArr);
        } else if (MtagBox.fourcc().equals(str)) {
            box = MtagBox.createMtagBox(bArr);
        }
        if (box != null) {
            udtaBox.add(box);
        }
    }

    public int getSize() {
        return 0;
    }

    public boolean isFinal() {
        return false;
    }

    public void onFinish() {
    }

    public void run() {
        save();
    }

    public void save() {
        String str;
        if (!CollectionUtils.isEmpty(this.mTags)) {
            boolean isEmpty = TextUtils.isEmpty(this.mFileName);
            String str2 = TAG;
            if (isEmpty) {
                str = "file path is null";
            } else {
                File file = new File(this.mFileName);
                StringBuilder sb = new StringBuilder();
                sb.append("mCurrentVideoFilename: ");
                sb.append(this.mFileName);
                Log.d(str2, sb.toString());
                if (!file.exists()) {
                    str = "file is not exists";
                } else if (Util.getDuration(this.mFileName) == 0) {
                    str = " video file is illegal";
                } else {
                    try {
                        new RelocateMP4Editor().modifyOrRelocate(file, new MP4Edit() {
                            public void apply(MovieBox movieBox) {
                                MetaBox metaBox = (MetaBox) NodeBox.findFirst(movieBox, MetaBox.class, MetaBox.fourcc());
                                if (metaBox == null) {
                                    metaBox = MetaBox.createMetaBox();
                                    movieBox.add(metaBox);
                                }
                                Map keyedMeta = metaBox.getKeyedMeta();
                                if (keyedMeta == null) {
                                    keyedMeta = new HashMap();
                                }
                                for (VideoTag videoTag : VideoTagSaveRequest.this.mTags) {
                                    keyedMeta.put(videoTag.getKey(), MetaValue.createInt(1));
                                    if (videoTag.getData() != null) {
                                        VideoTagSaveRequest.this.addBox(movieBox, videoTag.getBoxType(), videoTag.getData());
                                    }
                                }
                                metaBox.setKeyedMeta(keyedMeta);
                            }

                            public void applyToFragment(MovieBox movieBox, MovieFragmentBox[] movieFragmentBoxArr) {
                            }
                        });
                    } catch (IOException | ClassCastException e) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("apply error  ");
                        sb2.append(e);
                        Log.e(str2, sb2.toString());
                    }
                    return;
                }
            }
            Log.e(str2, str);
        }
    }

    public void setContextAndCallback(Context context, SaverCallback saverCallback) {
    }

    public void setTags(String str, List list) {
        this.mFileName = str;
        this.mTags = list;
    }
}

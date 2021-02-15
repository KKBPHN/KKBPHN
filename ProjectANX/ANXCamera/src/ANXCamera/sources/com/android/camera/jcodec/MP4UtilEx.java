package com.android.camera.jcodec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.android.camera.CameraSize;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.internal.util.CollectionUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.MP4Util.Atom;
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

public class MP4UtilEx {
    private static final String TAG = "MP4UtilEx";

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

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("VideoTag{key='");
            sb.append(this.key);
            sb.append('\'');
            sb.append(", boxType='");
            sb.append(this.boxType);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /* access modifiers changed from: private */
    public static void addBox(MovieBox movieBox, String str, byte[] bArr) {
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
        } else if (MCoverBox.fourcc().equals(str)) {
            box = NodeBox.findFirst(udtaBox, MCoverBox.class, MCoverBox.fourcc());
            if (box == null) {
                box = MCoverBox.createCoverBox(bArr);
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("add cover ");
                sb.append(box);
                Log.d(str2, sb.toString());
            }
        }
        if (box != null) {
            udtaBox.add(box);
        }
    }

    public static boolean getCover(String str, CameraSize cameraSize, Bitmap[] bitmapArr) {
        bitmapArr[0] = readCover(str);
        if (bitmapArr[0] != null) {
            return true;
        }
        if (cameraSize.width * cameraSize.height > 2073600) {
            cameraSize = new CameraSize(1920, 1080);
        }
        bitmapArr[0] = Thumbnail.createVideoThumbnailBitmap(str, cameraSize.width, cameraSize.height);
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static UdtaBox parseUdta(File file) {
        FileChannelWrapper fileChannelWrapper;
        try {
            fileChannelWrapper = NIOUtils.readableChannel(file);
            try {
                UdtaBox parseUdtaChannel = parseUdtaChannel(fileChannelWrapper);
                if (fileChannelWrapper != null) {
                    fileChannelWrapper.close();
                }
                return parseUdtaChannel;
            } catch (Throwable th) {
                th = th;
                if (fileChannelWrapper != null) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileChannelWrapper = null;
            if (fileChannelWrapper != null) {
                fileChannelWrapper.close();
            }
            throw th;
        }
    }

    public static UdtaBox parseUdtaChannel(SeekableByteChannel seekableByteChannel) {
        Atom findFirstAtom = MP4Util.findFirstAtom("moov", seekableByteChannel, 0, seekableByteChannel.size());
        if (findFirstAtom == null) {
            return null;
        }
        long headerSize = findFirstAtom.getHeader().headerSize();
        Atom findFirstAtom2 = MP4Util.findFirstAtom(UdtaBox.fourcc(), seekableByteChannel, findFirstAtom.getOffset() + headerSize, findFirstAtom.getHeader().getSize() - headerSize);
        if (findFirstAtom2 == null) {
            return null;
        }
        return (UdtaBox) findFirstAtom2.parseBox(seekableByteChannel);
    }

    public static Bitmap readCover(String str) {
        Log.d(TAG, "readCover E");
        long currentTimeMillis = System.currentTimeMillis();
        Bitmap bitmap = null;
        try {
            MCoverBox mCoverBox = (MCoverBox) NodeBox.findFirstPath(parseUdta(new File(str)), MCoverBox.class, new String[]{MCoverBox.fourcc()});
            if (mCoverBox != null) {
                bitmap = BitmapFactory.decodeByteArray(mCoverBox.getData(), 0, mCoverBox.getData().length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("readCover X , duration = ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.d(str2, sb.toString());
        return bitmap;
    }

    public static void writeTags(String str, final List list) {
        Log.d(TAG, "writeTags E");
        long currentTimeMillis = System.currentTimeMillis();
        if (!CollectionUtils.isEmpty(list)) {
            if (TextUtils.isEmpty(str)) {
                android.util.Log.e(TAG, "file path is null");
                return;
            }
            File file = new File(str);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mCurrentVideoFilename: ");
            sb.append(str);
            android.util.Log.d(str2, sb.toString());
            if (!file.exists()) {
                android.util.Log.e(TAG, "file is not exists");
            } else if (Util.getDuration(str) == 0) {
                android.util.Log.e(TAG, " video file is illegal");
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
                            for (VideoTag videoTag : list) {
                                if (videoTag.getKey() != null) {
                                    keyedMeta.put(videoTag.getKey(), MetaValue.createInt(1));
                                }
                                if (videoTag.getData() != null) {
                                    MP4UtilEx.addBox(movieBox, videoTag.getBoxType(), videoTag.getData());
                                }
                            }
                            metaBox.setKeyedMeta(keyedMeta);
                        }

                        public void applyToFragment(MovieBox movieBox, MovieFragmentBox[] movieFragmentBoxArr) {
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("writeTags X , duration = ");
                sb2.append(System.currentTimeMillis() - currentTimeMillis);
                Log.d(str3, sb2.toString());
            }
        }
    }
}

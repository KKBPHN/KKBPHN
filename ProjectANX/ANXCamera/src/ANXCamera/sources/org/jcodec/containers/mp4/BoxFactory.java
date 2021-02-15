package org.jcodec.containers.mp4;

import org.jcodec.containers.mp4.boxes.AudioSampleEntry;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.Box.LeafBox;
import org.jcodec.containers.mp4.boxes.DataRefBox;
import org.jcodec.containers.mp4.boxes.Header;
import org.jcodec.containers.mp4.boxes.NodeBox;
import org.jcodec.containers.mp4.boxes.SampleDescriptionBox;
import org.jcodec.containers.mp4.boxes.TimecodeSampleEntry;
import org.jcodec.containers.mp4.boxes.VideoSampleEntry;
import org.jcodec.containers.mp4.boxes.WaveExtension;
import org.jcodec.platform.Platform;

public class BoxFactory implements IBoxFactory {
    private static IBoxFactory audio = new BoxFactory(new AudioBoxes());
    private static IBoxFactory data = new BoxFactory(new DataBoxes());
    private static IBoxFactory instance = new BoxFactory(new DefaultBoxes());
    private static IBoxFactory sample = new BoxFactory(new SampleBoxes());
    private static IBoxFactory timecode = new BoxFactory(new TimecodeBoxes());
    private static IBoxFactory video = new BoxFactory(new VideoBoxes());
    private static IBoxFactory waveext = new BoxFactory(new WaveExtBoxes());
    private Boxes boxes;

    public BoxFactory(Boxes boxes2) {
        this.boxes = boxes2;
    }

    public static IBoxFactory getDefault() {
        return instance;
    }

    /* JADX WARNING: type inference failed for: r3v1, types: [org.jcodec.containers.mp4.IBoxFactory] */
    /* JADX WARNING: type inference failed for: r3v2, types: [org.jcodec.containers.mp4.IBoxFactory] */
    /* JADX WARNING: type inference failed for: r3v3, types: [org.jcodec.containers.mp4.IBoxFactory] */
    /* JADX WARNING: type inference failed for: r3v4, types: [org.jcodec.containers.mp4.IBoxFactory] */
    /* JADX WARNING: type inference failed for: r3v5, types: [org.jcodec.containers.mp4.IBoxFactory] */
    /* JADX WARNING: type inference failed for: r3v7, types: [org.jcodec.containers.mp4.IBoxFactory] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Box newBox(Header header) {
        Class cls = this.boxes.toClass(header.getFourcc());
        if (cls == null) {
            return new LeafBox(header);
        }
        Box box = (Box) Platform.newInstance(cls, new Object[]{header});
        if (box instanceof NodeBox) {
            NodeBox nodeBox = (NodeBox) box;
            if (nodeBox instanceof SampleDescriptionBox) {
                r3 = sample;
            } else if (nodeBox instanceof VideoSampleEntry) {
                r3 = video;
            } else if (nodeBox instanceof AudioSampleEntry) {
                r3 = audio;
            } else if (nodeBox instanceof TimecodeSampleEntry) {
                r3 = timecode;
            } else if (nodeBox instanceof DataRefBox) {
                r3 = data;
            } else if (nodeBox instanceof WaveExtension) {
                r3 = waveext;
            }
            nodeBox.setFactory(r3);
        }
        return box;
    }
}

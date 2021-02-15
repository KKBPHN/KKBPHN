package com.arcsoft.avatar.emoticon;

import android.util.Size;
import com.arcsoft.avatar.AvatarConfig.ASAvatarProcessInfo;
import com.arcsoft.avatar.util.ASVLOFFSCREEN;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class EmoInfo {
    private static final String a = "EmoInfo";
    private boolean b = true;
    private String c;
    private String d;
    private FileDescriptor e;
    private int f;
    private float g;
    private float h;
    private boolean i;
    private boolean j;
    private ByteBuffer k;
    private byte[] l;
    private String m;
    private ArrayList n;
    private Size o;
    private int p;
    private float q;
    private String r;
    private String s;

    public class EmoExtraInfo {
        public ASVLOFFSCREEN asBackGround;
        public ASVLOFFSCREEN asForeGround;
        public String backGroundPath;
        public String foreGroundPath;
        public int index;
        public ASAvatarProcessInfo processInfo;
        public float time;

        public EmoExtraInfo() {
            this.index = 0;
            this.time = 0.0f;
            this.asForeGround = null;
            this.asBackGround = null;
            this.processInfo = null;
            this.processInfo = new ASAvatarProcessInfo(256, 256, 0, false);
        }
    }

    public EmoInfo() {
        String str = "";
        this.c = str;
        this.f = 0;
        this.g = 0.0f;
        this.h = 0.0f;
        this.i = false;
        this.j = false;
        this.k = null;
        this.l = null;
        this.m = str;
        this.o = null;
        this.p = 25;
        this.q = 1.0f;
        this.n = new ArrayList();
    }

    public ArrayList getEmoExtraInfoList() {
        return this.n;
    }

    public Size getEmoImageSize() {
        return this.o;
    }

    public int getEmoMaxCount() {
        return this.f;
    }

    public String getEmoName() {
        return this.c;
    }

    public FileDescriptor getFileDescriptor() {
        return this.e;
    }

    public String getFilePath() {
        return this.m;
    }

    public float getScaleLevel() {
        return this.q;
    }

    public boolean getSelect() {
        return this.b;
    }

    public String getThumbBgGroundPath() {
        return this.s;
    }

    public String getThumbForGroundPath() {
        return this.r;
    }

    public int getThumbFrameIndex() {
        return this.p;
    }

    public ByteBuffer getThumbnail() {
        return this.k;
    }

    public byte[] getThumbnailData() {
        return this.l;
    }

    public float getTranslationX() {
        return this.g;
    }

    public float getTranslationY() {
        return this.h;
    }

    public String getVideoPath() {
        return this.d;
    }

    public boolean isMultipleBG() {
        return this.j;
    }

    public boolean isNeedFaceColor() {
        return this.i;
    }

    public void needFaceColor(boolean z) {
        this.i = z;
    }

    public void setEmoExtraInfoList(ArrayList arrayList) {
        this.n = arrayList;
    }

    public void setEmoImageSize(Size size) {
        this.o = size;
    }

    public void setEmoMaxCount(int i2) {
        this.f = i2;
    }

    public void setEmoName(String str) {
        this.c = str;
    }

    public void setFileDescriptor(FileDescriptor fileDescriptor) {
        this.e = fileDescriptor;
    }

    public void setFilePath(String str) {
        this.m = str;
    }

    public void setMultipleBG(boolean z) {
        this.j = z;
    }

    public void setScaleLevel(float f2) {
        this.q = f2;
    }

    public void setSelect(boolean z) {
        this.b = z;
    }

    public void setThumbBgGroundPath(String str) {
        this.s = str;
    }

    public void setThumbForGroundPath(String str) {
        this.r = str;
    }

    public void setThumbFrameIndex(int i2) {
        this.p = i2;
    }

    public void setThumbnail(ByteBuffer byteBuffer) {
        this.k = byteBuffer;
    }

    public void setThumbnailData(byte[] bArr) {
        this.l = bArr;
    }

    public void setTranslationX(float f2) {
        this.g = f2;
    }

    public void setTranslationY(float f2) {
        this.h = f2;
    }

    public void setVideoPath(String str) {
        this.d = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("emoName = ");
        sb.append(this.c);
        sb.append(", emoMaxCount = ");
        sb.append(this.f);
        sb.append("\n");
        String sb2 = sb.toString();
        Iterator it = this.n.iterator();
        String str = "";
        while (it.hasNext()) {
            EmoExtraInfo emoExtraInfo = (EmoExtraInfo) it.next();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("index = ");
            sb3.append(emoExtraInfo.index);
            sb3.append(", time = ");
            sb3.append(emoExtraInfo.time);
            sb3.append(", foreGroundPath = ");
            sb3.append(emoExtraInfo.foreGroundPath);
            sb3.append("tongueStatus = ");
            sb3.append(emoExtraInfo.processInfo.getTongueStatus());
            sb3.append(", Orientations = ");
            sb3.append(Arrays.toString(emoExtraInfo.processInfo.getOrientations()));
            sb3.append(", LeftEyes = ");
            sb3.append(Arrays.toString(emoExtraInfo.processInfo.getOrientationLeftEyes()));
            sb3.append(", RightEyes = ");
            sb3.append(Arrays.toString(emoExtraInfo.processInfo.getOrientationRightEyes()));
            sb3.append(", ExpWeights = ");
            sb3.append(Arrays.toString(emoExtraInfo.processInfo.getExpWeights()));
            str = sb3.toString();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append(sb2);
        sb4.append(str);
        return sb4.toString();
    }
}

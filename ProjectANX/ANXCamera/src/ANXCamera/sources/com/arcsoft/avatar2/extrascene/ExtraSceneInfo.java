package com.arcsoft.avatar2.extrascene;

import java.util.ArrayList;

public class ExtraSceneInfo {
    public static final String ANGEL_WINGS_CAP = "AngelWingsCap";
    public static final String ASTRONAUT_HELMET = "AstronautHelmet";
    public static final String CHICKEN_HAT = "ChickenHat";
    public static final String CLOWN_HAT = "ClownHat";
    public static final String EMOJI_HELMET = "EmojiHelmet";
    public static final String EXTRA_ANGELWINGS_NAME = "AngelWings";
    public static final String EXTRA_CLOWNHAT_NAME = "ClownHat";
    public static final String EXTRA_RABBITTEETH_NAME = "RabbitTeeth";
    public static final int EXTRA_STATUS_AGAIN = 4;
    public static final int EXTRA_STATUS_BEGIN = 1;
    public static final int EXTRA_STATUS_END = 3;
    public static final int EXTRA_STATUS_RUN = 2;
    public static final int HEAD_PITCH_STATUS_DOWN = 2;
    public static final int HEAD_PITCH_STATUS_NORMAL = 0;
    public static final int HEAD_PITCH_STATUS_UP = 1;
    public static final int LOOP_LAST_EXP_ID_STATUS = 2;
    public static final int LOOP_NORAML_STATUS = 1;
    public static final int LOOP_ONCE_STATUS = 3;
    public static final String MI_RABBIT_HAT = "MiRabbitHat";
    public static final String PRINCESS_HAT = "PrincessHat";
    public static final String RABBIT_EARS = "RabbitEars";
    public static final String SHIBAINU_HAT = "ShibaInuHat";
    public static final String WIZARD_HAT = "WizardHat";
    private ArrayList A;
    private int[] B;
    private int[] C;
    private String a;
    private int b = -1;
    private int c = 0;
    private boolean d = false;
    private ArrayList e = null;
    private int f = 0;
    private String g;
    private ArrayList h;
    private String i;
    private String j;
    private ArrayList k;
    private ArrayList l;
    private int m;
    private int n;
    private boolean o;
    private int p;
    private int q;
    private int r;
    private int s;
    private int t;
    private int u;
    private int v;
    private int w;
    private int x;
    private int y;
    private String z;

    public class ExpressInfo {
        public static final int LOOP_BEGIN_FRAME = 1;
        public static final int LOOP_END_FRAME = 2;
        private int[] b = null;
        private ArrayList c = null;
        private boolean d = false;
        private int e = 0;
        private boolean f = false;

        public ExpressInfo() {
        }

        public int[] getExpressIndex() {
            return this.b;
        }

        public ArrayList getExpressValue() {
            return this.c;
        }

        public int getLoopStatus() {
            return this.e;
        }

        public boolean isLoopFrame() {
            return this.d;
        }

        public boolean isPauseFrame() {
            return this.f;
        }

        public void setExpressIndex(int[] iArr) {
            this.b = iArr;
        }

        public void setExpressValue(float f2) {
            if (this.c == null) {
                this.c = new ArrayList();
            }
            this.c.add(Float.valueOf(f2));
        }

        public void setLoopFrame(boolean z) {
            this.d = z;
        }

        public void setLoopStatus(int i) {
            this.e = i;
        }

        public void setPauseStatus(boolean z) {
            this.f = z;
        }
    }

    public class HairMaskInfo {
        private int b = -1;
        private int c = -1;
        private boolean d = false;
        private boolean e = false;

        public HairMaskInfo() {
        }

        public boolean getBlendShape() {
            return this.d;
        }

        public int getExpId() {
            return this.c;
        }

        public int getHairId() {
            return this.b;
        }

        public boolean getMask() {
            return this.e;
        }

        public void setBlendShape(boolean z) {
            this.d = z;
        }

        public void setExpId(int i) {
            this.c = i;
        }

        public void setHairId(int i) {
            this.b = i;
        }

        public void setMask(boolean z) {
            this.e = z;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("mHairId = ");
            sb.append(this.b);
            sb.append(", mExpId = ");
            sb.append(this.c);
            sb.append(", bBlendShape = ");
            sb.append(this.d);
            sb.append(", bMask = ");
            sb.append(this.e);
            return sb.toString();
        }
    }

    public ExtraSceneInfo() {
        String str = "";
        this.a = str;
        this.g = str;
        this.h = null;
        this.i = str;
        this.j = str;
        this.k = null;
        this.l = null;
        this.m = 0;
        this.n = 1;
        this.o = true;
        this.p = -1;
        this.q = 0;
        this.r = 0;
        this.s = 0;
        this.t = 0;
        this.u = 0;
        this.v = 1;
        this.w = 0;
        this.x = 0;
        this.y = -1;
        this.z = str;
        this.A = null;
        this.B = null;
        this.C = null;
    }

    public String getAccPath() {
        return this.z;
    }

    public int getDuration() {
        return this.f;
    }

    public int getEmojiHelmetLeftRandomNum() {
        return this.u;
    }

    public int getEmojiHelmetRightRandomNum() {
        return this.v;
    }

    public ArrayList getExpCheckIdList() {
        return this.k;
    }

    public ArrayList getExpCheckValueList() {
        return this.l;
    }

    public int getExpCount() {
        return this.c;
    }

    public int[] getExpIdInPut() {
        return this.B;
    }

    public int[] getExpIdOutPut() {
        return this.C;
    }

    public ArrayList getExpressInfoList() {
        return this.e;
    }

    public int getExtraStatus() {
        return this.n;
    }

    public ArrayList getHairMaskInfoList() {
        return this.A;
    }

    public int getHeadPitchStatus() {
        return this.x;
    }

    public int getHeadWear() {
        return this.b;
    }

    public int getHeadWearColor() {
        return this.p;
    }

    public int getLoopBeginFrameIndex() {
        return this.q;
    }

    public int getLoopCount() {
        return this.s;
    }

    public int getLoopEndFrameIndex() {
        return this.r;
    }

    public int getLoopNum() {
        return this.t;
    }

    public int getLoopType() {
        return this.m;
    }

    public String getName() {
        return this.a;
    }

    public ArrayList getNewHeadWearPathList() {
        return this.h;
    }

    public String getNewToothPath() {
        return this.j;
    }

    public String getOldHeadWearPath() {
        return this.g;
    }

    public String getOldToothPath() {
        return this.i;
    }

    public int getPauseIndex() {
        return this.y;
    }

    public int getRandom() {
        return this.w;
    }

    public boolean isNeedOutLine() {
        return this.d;
    }

    public boolean isOldHeadWearAcc() {
        return this.o;
    }

    public void setAccPath(String str) {
        this.z = str;
    }

    public void setDuration(int i2) {
        this.f = i2;
    }

    public void setEmojiHelmetLeftRandomNum(int i2) {
        this.u = i2;
    }

    public void setEmojiHelmetRightRandomNum(int i2) {
        this.v = i2;
    }

    public void setExpCheckId(int[] iArr) {
        if (this.k == null) {
            this.k = new ArrayList();
        }
        this.k.add(iArr);
    }

    public void setExpCheckValue(float[] fArr) {
        if (this.l == null) {
            this.l = new ArrayList();
        }
        this.l.add(fArr);
    }

    public void setExpCount(int i2) {
        this.c = i2;
    }

    public void setExpIdInPut(int[] iArr) {
        this.B = iArr;
    }

    public void setExpIdOutPut(int[] iArr) {
        this.C = iArr;
    }

    public void setExpressInfo(ExpressInfo expressInfo) {
        if (this.e == null) {
            this.e = new ArrayList();
        }
        this.e.add(expressInfo);
    }

    public void setExtraStatus(int i2) {
        this.n = i2;
    }

    public void setHairMaskInfo(HairMaskInfo hairMaskInfo) {
        if (this.A == null) {
            this.A = new ArrayList();
        }
        this.A.add(hairMaskInfo);
    }

    public void setHeadPitchStatus(int i2) {
        this.x = i2;
    }

    public void setHeadWear(int i2) {
        this.b = i2;
    }

    public void setHeadWearColor(int i2) {
        this.p = i2;
    }

    public void setIsOldHeadWearAcc(boolean z2) {
        this.o = z2;
    }

    public void setLoopBeginFrameIndex(int i2) {
        this.q = i2;
    }

    public void setLoopCount(int i2) {
        this.s = i2;
    }

    public void setLoopEndFrameIndex(int i2) {
        this.r = i2;
    }

    public void setLoopNum(int i2) {
        this.t = i2;
    }

    public void setLoopType(int i2) {
        this.m = i2;
    }

    public void setName(String str) {
        this.a = str;
    }

    public void setNeedOutLine(boolean z2) {
        this.d = z2;
    }

    public void setNewHeadWearPath(String str) {
        if (this.h == null) {
            this.h = new ArrayList();
        }
        this.h.add(str);
    }

    public void setNewToothPath(String str) {
        this.j = str;
    }

    public void setOldHeadWearPath(String str) {
        this.g = str;
    }

    public void setOldToothPath(String str) {
        this.i = str;
    }

    public void setPauseIndex(int i2) {
        this.y = i2;
    }

    public void setRandom(int i2) {
        this.w = i2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name = ");
        sb.append(this.a);
        sb.append(", headWear = ");
        sb.append(this.b);
        sb.append(", mNeedOutLine = ");
        sb.append(this.d);
        sb.append(", mExpressIndex = , mExpressValue = ");
        return sb.toString();
    }
}

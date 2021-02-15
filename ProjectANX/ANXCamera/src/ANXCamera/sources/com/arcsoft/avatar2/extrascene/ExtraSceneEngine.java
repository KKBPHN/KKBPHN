package com.arcsoft.avatar2.extrascene;

import android.text.TextUtils;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarConfigValue;
import com.arcsoft.avatar2.AvatarConfig.ASAvatarProcessInfo;
import com.arcsoft.avatar2.AvatarEngine;
import com.arcsoft.avatar2.BackgroundInfo;
import com.arcsoft.avatar2.extrascene.ExtraSceneInfo.ExpressInfo;
import com.arcsoft.avatar2.extrascene.ExtraSceneInfo.HairMaskInfo;
import com.arcsoft.avatar2.util.LOG;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ExtraSceneEngine {
    private static final String A = "EmojiHelmet";
    private static final String B = "AngelWings";
    private static final String C = "ClownHat";
    private static final String D = "MagicHat";
    private static final String E = "AstronautHat";
    private static final String F = "DogHat";
    private static final String G = "PrincessHat";
    private static final String H = "hairId";
    private static final String I = "expId";
    private static final String J = "blendshape";
    private static final String K = "mask";
    private static final int L = 2;
    private static final int M = 1;
    private static final int N = 2;
    private static final int O = 3;
    private static final String a = "ExtraSceneEngine";
    private static final String b = "extraScene.xml";
    private static final String c = "info";
    private static final String d = "name";
    private static final String e = "headwear";
    private static final String f = "headwearcolor";
    private static final String g = "expid";
    private static final String h = "expvalue";
    private static final String i = "outline";
    private static final String j = "oldheadwear";
    private static final String k = "newheadwear";
    private static final String l = "expcheckid";
    private static final String m = "expcheckvalue";
    private static final String n = "oldtooth";
    private static final String o = "newtooth";
    private static final String p = "loopstyle";
    private static final String q = "render";
    private static final String r = "loop";
    private static final String s = "random";
    private static final String t = "pause";
    private static final String u = "expidin";
    private static final String v = "expidout";
    private static final String w = "RabbitTeeth";
    private static final String x = "RabbitEars";
    private static final String y = "ChickHat";
    private static final String z = "WizardHat";
    private String P;
    private AvatarEngine Q;
    private ASAvatarConfigValue R;
    private ExtraSceneInfo S = null;
    private ArrayList T = null;
    private String U;
    private int V = 0;
    private int W = 0;
    private int X = 0;
    private int Y = 0;
    private int Z = 0;
    private int aa = 0;
    private int ab = 0;
    private Random ac = null;
    private List ad = null;
    private AvatarExtraSceneTriggerCallback ae = null;

    public interface AvatarExtraSceneTriggerCallback {
        void onExtraSceneTrigger(String str, int i);
    }

    public ExtraSceneEngine(String str, AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback) {
        this.U = str;
        this.ae = avatarExtraSceneTriggerCallback;
        this.R = new ASAvatarConfigValue();
        this.T = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/");
        sb.append(b);
        this.P = sb.toString();
        a();
        this.ad = new ArrayList();
    }

    private void a() {
        String str;
        String str2 = a;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("getXmlInfo = ");
            sb.append(this.P);
            LOG.d(str2, sb.toString());
            FileInputStream fileInputStream = new FileInputStream(this.P);
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(fileInputStream, "UTF-8");
            String str3 = null;
            ExtraSceneInfo extraSceneInfo = null;
            ExpressInfo expressInfo = null;
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType != 0) {
                    if (eventType == 2) {
                        str = newPullParser.getName();
                        if (str.equals(c)) {
                            extraSceneInfo = new ExtraSceneInfo();
                        }
                    } else if (eventType == 3) {
                        if (newPullParser.getName().equals(BackgroundInfo.getXMLInfoTag())) {
                            this.T.add(extraSceneInfo);
                        }
                        str = "";
                    } else if (eventType == 4) {
                        if (str3.equals(d)) {
                            extraSceneInfo.setName(newPullParser.getText());
                        } else if (str3.equals(e)) {
                            extraSceneInfo.setHeadWear(Integer.parseInt(newPullParser.getText()));
                        } else if (str3.equals(i)) {
                            extraSceneInfo.setNeedOutLine(newPullParser.getText().equals("1"));
                        } else if (str3.equals(g)) {
                            extraSceneInfo.getClass();
                            ExpressInfo expressInfo2 = new ExpressInfo();
                            expressInfo2.setExpressIndex(a(newPullParser.getText()));
                            expressInfo = expressInfo2;
                        } else if (str3.equals(h)) {
                            a(newPullParser.getText(), expressInfo, extraSceneInfo);
                        } else {
                            String str4 = "/";
                            if (str3.equals(j)) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(this.U);
                                sb2.append(str4);
                                sb2.append(newPullParser.getText());
                                extraSceneInfo.setOldHeadWearPath(sb2.toString());
                            } else if (str3.equals(k)) {
                                if (extraSceneInfo.getRandom() > 0) {
                                    a(extraSceneInfo, newPullParser.getText());
                                } else {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(this.U);
                                    sb3.append(str4);
                                    sb3.append(newPullParser.getText());
                                    extraSceneInfo.setNewHeadWearPath(sb3.toString());
                                }
                            } else if (str3.equals(l)) {
                                extraSceneInfo.setExpCheckId(a(newPullParser.getText()));
                            } else if (str3.equals(m)) {
                                extraSceneInfo.setExpCheckValue(b(newPullParser.getText()));
                            } else if (str3.equals(n)) {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(this.U);
                                sb4.append(str4);
                                sb4.append(newPullParser.getText());
                                extraSceneInfo.setOldToothPath(sb4.toString());
                            } else if (str3.equals(o)) {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(this.U);
                                sb5.append(str4);
                                sb5.append(newPullParser.getText());
                                extraSceneInfo.setNewToothPath(sb5.toString());
                            } else if (str3.equals(p)) {
                                extraSceneInfo.setLoopType(Integer.parseInt(newPullParser.getText()));
                            } else if (str3.equals(r)) {
                                a(newPullParser.getText(), extraSceneInfo);
                            } else if (str3.equals(f)) {
                                extraSceneInfo.setHeadWearColor(Integer.parseInt(newPullParser.getText()));
                            } else if (str3.equals(s)) {
                                extraSceneInfo.setRandom(Integer.parseInt(newPullParser.getText()));
                            } else if (str3.equals("pause")) {
                                b(newPullParser.getText(), extraSceneInfo);
                            } else if (str3.equals(q)) {
                                c(newPullParser.getText(), extraSceneInfo);
                            } else if (str3.equals(u)) {
                                extraSceneInfo.setExpIdInPut(a(newPullParser.getText()));
                            } else if (str3.equals(v)) {
                                extraSceneInfo.setExpIdOutPut(a(newPullParser.getText()));
                            }
                        }
                    }
                    str3 = str;
                }
            }
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    private void a(ASAvatarProcessInfo aSAvatarProcessInfo) {
        int i2;
        if (this.S.getExtraStatus() == 1) {
            boolean m2 = m(aSAvatarProcessInfo);
            if (m2) {
                i2 = this.X + 1;
                this.X = i2;
            } else {
                i2 = 0;
            }
            this.X = i2;
            StringBuilder sb = new StringBuilder();
            sb.append("check = ");
            sb.append(m2);
            sb.append(", mCheckFrameCount = ");
            sb.append(this.X);
            LOG.d("rabbitEars", sb.toString());
            if (this.X < 3) {
                return;
            }
        }
        float[] expWeights = aSAvatarProcessInfo.getExpWeights();
        if (this.S.getExtraStatus() == 3) {
            this.V = this.S.getExpressInfoList().size() - 1;
        }
        if (this.S.getExtraStatus() == 1) {
            AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
            if (avatarExtraSceneTriggerCallback != null) {
                avatarExtraSceneTriggerCallback.onExtraSceneTrigger(ExtraSceneInfo.MI_RABBIT_HAT, this.S.getHeadWear());
            }
        }
        a((ExpressInfo) this.S.getExpressInfoList().get(this.V), expWeights);
        if (this.S.getExtraStatus() != 3) {
            if (this.V >= this.S.getExpressInfoList().size() - 1) {
                this.S.setExtraStatus(3);
                this.V = 0;
            } else {
                this.V++;
                this.S.setExtraStatus(2);
            }
        }
        aSAvatarProcessInfo.setExpWeights(expWeights);
    }

    private void a(ExpressInfo expressInfo, float[] fArr) {
        int i2;
        StringBuilder sb;
        int[] expressIndex = expressInfo.getExpressIndex();
        ArrayList expressValue = expressInfo.getExpressValue();
        if (this.ad.size() <= 0) {
            ArrayList hairMaskInfoList = this.S.getHairMaskInfoList();
            if (hairMaskInfoList != null) {
                for (int i3 = 0; i3 < hairMaskInfoList.size(); i3++) {
                    HairMaskInfo hairMaskInfo = (HairMaskInfo) hairMaskInfoList.get(i3);
                    if (this.R.configHairStyleID == hairMaskInfo.getHairId()) {
                        this.ad.add(hairMaskInfo);
                    }
                }
            }
        }
        for (int i4 = 0; i4 < expressValue.size(); i4++) {
            String str = "] = ";
            String str2 = "set express[";
            String str3 = "expWeights";
            if (expressIndex.length == expressValue.size()) {
                fArr[expressIndex[i4]] = ((Float) expressValue.get(i4)).floatValue();
                sb = new StringBuilder();
                sb.append(str2);
                i2 = expressIndex[i4];
            } else {
                fArr[expressIndex[0]] = ((Float) expressValue.get(i4)).floatValue();
                sb = new StringBuilder();
                sb.append(str2);
                i2 = expressIndex[0];
            }
            sb.append(i2);
            sb.append(str);
            sb.append(expressValue.get(i4));
            LOG.d(str3, sb.toString());
            Iterator it = this.ad.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                HairMaskInfo hairMaskInfo2 = (HairMaskInfo) it.next();
                if (hairMaskInfo2.getExpId() == expressIndex[i4]) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("expid -> ");
                    sb2.append(expressIndex[i4]);
                    sb2.append(", HairInfo -> ");
                    sb2.append(hairMaskInfo2.toString());
                    LOG.d("setHairRenderParam", sb2.toString());
                    this.Q.setHairRenderParam(hairMaskInfo2.getBlendShape(), hairMaskInfo2.getMask());
                    break;
                }
            }
        }
        String str4 = "rabbitEars";
        if (this.S.getNewHeadWearPathList() != null) {
            String str5 = (String) this.S.getNewHeadWearPathList().get(0);
            if (!TextUtils.isEmpty(str5)) {
                if (!this.S.getAccPath().equals(str5)) {
                    this.Q.setHeadWearTexture(str5);
                    this.S.setAccPath(str5);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("new headwear path = ");
                    sb3.append(str5);
                    LOG.d(str4, sb3.toString());
                } else {
                    return;
                }
            }
        }
        if (!TextUtils.isEmpty(this.S.getNewToothPath())) {
            this.Q.setToothTexture(this.S.getNewToothPath());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("new tooth path = ");
            sb4.append(this.S.getNewToothPath());
            LOG.d(str4, sb4.toString());
        }
    }

    private void a(ExtraSceneInfo extraSceneInfo, String str) {
        for (int i2 = 0; i2 < extraSceneInfo.getRandom(); i2++) {
            for (int i3 = 0; i3 < extraSceneInfo.getRandom(); i3++) {
                if (i2 != i3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.U);
                    sb.append("/");
                    sb.append(str);
                    sb.append("Tex_");
                    sb.append(i2);
                    sb.append("-");
                    sb.append(i3);
                    sb.append("_acc.png");
                    extraSceneInfo.setNewHeadWearPath(sb.toString());
                }
            }
        }
    }

    private void a(String str, ExpressInfo expressInfo, ExtraSceneInfo extraSceneInfo) {
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split(",");
            int i2 = 0;
            for (String parseInt : split) {
                i2 = Integer.parseInt(parseInt);
                expressInfo.setExpressValue(1.0f);
            }
            if (i2 > 1) {
                for (int i3 = 0; i3 < i2; i3++) {
                    extraSceneInfo.setExpressInfo(expressInfo);
                }
            } else {
                extraSceneInfo.setExpressInfo(expressInfo);
            }
        }
    }

    private void a(String str, ExtraSceneInfo extraSceneInfo) {
        if (!TextUtils.isEmpty(str)) {
            int indexOf = str.indexOf("-");
            int indexOf2 = str.indexOf("*");
            int parseInt = Integer.parseInt(str.substring(0, indexOf));
            int parseInt2 = Integer.parseInt(str.substring(indexOf + 1, indexOf2));
            int parseInt3 = Integer.parseInt(str.substring(indexOf2 + 1, str.length()));
            StringBuilder sb = new StringBuilder();
            sb.append("begin =");
            sb.append(parseInt);
            sb.append(", end =");
            sb.append(parseInt2);
            sb.append(", num =");
            sb.append(parseInt3);
            LOG.d("expWeights", sb.toString());
            ArrayList expressInfoList = extraSceneInfo.getExpressInfoList();
            if (expressInfoList != null && expressInfoList.size() > 0) {
                for (int i2 = 0; i2 < expressInfoList.size(); i2++) {
                    ExpressInfo expressInfo = (ExpressInfo) expressInfoList.get(i2);
                    if (expressInfo.getExpressIndex()[0] == parseInt) {
                        extraSceneInfo.setLoopBeginFrameIndex(i2);
                    }
                    if (expressInfo.getExpressIndex()[0] == parseInt2) {
                        expressInfo.setLoopStatus(2);
                        extraSceneInfo.setLoopEndFrameIndex(i2);
                        extraSceneInfo.setLoopNum(parseInt3);
                    }
                }
            }
        }
    }

    private int[] a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(",");
        int[] iArr = new int[split.length];
        for (int i2 = 0; i2 < split.length; i2++) {
            iArr[i2] = Integer.parseInt(split[i2].trim());
        }
        return iArr;
    }

    private void b(ASAvatarProcessInfo aSAvatarProcessInfo) {
        int i2;
        float[] expWeights = aSAvatarProcessInfo.getExpWeights();
        a((ExpressInfo) this.S.getExpressInfoList().get(this.V), expWeights);
        if (this.S.getExtraStatus() == 1 && this.ae != null) {
            this.S.setExtraStatus(2);
            this.ae.onExtraSceneTrigger("RabbitEars", this.S.getHeadWear());
        }
        if (this.V >= this.S.getExpressInfoList().size() - 1) {
            i2 = 0;
        } else {
            i2 = this.V + 1;
            this.V = i2;
        }
        this.V = i2;
        if (!(this.S.getExpIdInPut() == null || this.S.getExpIdOutPut() == null)) {
            for (int i3 = 0; i3 < this.S.getExpIdInPut().length; i3++) {
                expWeights[this.S.getExpIdInPut()[i3]] = -expWeights[this.S.getExpIdOutPut()[i3]];
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.S.getExpIdInPut()[i3]);
                sb.append(", = ");
                sb.append(this.S.getExpIdOutPut()[i3]);
                LOG.d("rabbitTeeth", sb.toString());
            }
        }
        aSAvatarProcessInfo.setExpWeights(expWeights);
    }

    private void b(String str, ExtraSceneInfo extraSceneInfo) {
        if (!TextUtils.isEmpty(str)) {
            int parseInt = Integer.parseInt(str);
            StringBuilder sb = new StringBuilder();
            sb.append("index =");
            sb.append(parseInt);
            LOG.d("expWeights", sb.toString());
            ArrayList expressInfoList = extraSceneInfo.getExpressInfoList();
            if (expressInfoList != null && expressInfoList.size() > 0) {
                int i2 = 0;
                while (true) {
                    if (i2 >= expressInfoList.size()) {
                        break;
                    }
                    ExpressInfo expressInfo = (ExpressInfo) expressInfoList.get(i2);
                    if (expressInfo.getExpressIndex()[0] == parseInt) {
                        expressInfo.setPauseStatus(true);
                        extraSceneInfo.setPauseIndex(i2);
                        break;
                    }
                    i2++;
                }
            }
        }
    }

    private float[] b(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(",");
        float[] fArr = new float[split.length];
        for (int i2 = 0; i2 < split.length; i2++) {
            fArr[i2] = Float.parseFloat(split[i2].trim());
        }
        return fArr;
    }

    private void c(ASAvatarProcessInfo aSAvatarProcessInfo) {
        int i2;
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[54] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[54]);
        sb2.append(", [55] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[55]);
        LOG.d("check_expweight", sb2.toString());
        String str = "[55] = ";
        String str2 = "clownHat";
        int i3 = 0;
        if (m(aSAvatarProcessInfo)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(aSAvatarProcessInfo.getExpWeights()[55]);
            LOG.d(str2, sb3.toString());
            if (this.S.getExtraStatus() != 3) {
                this.X++;
            } else {
                return;
            }
        } else if (this.S.getExtraStatus() != 2) {
            if (this.S.getExtraStatus() == 3) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(aSAvatarProcessInfo.getExpWeights()[55]);
                LOG.d(str2, sb4.toString());
                this.S.setExtraStatus(1);
                this.X = 0;
                if (this.S.getNewHeadWearPathList() != null) {
                    if (this.W < this.S.getNewHeadWearPathList().size() - 1) {
                        i3 = this.W + 1;
                        this.W = i3;
                    }
                    this.W = i3;
                }
                return;
            }
            this.X = 0;
            return;
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("mCheckFrameCount = ");
        sb5.append(this.X);
        LOG.d(str2, sb5.toString());
        if (this.X > 3) {
            if (TextUtils.isEmpty(this.S.getAccPath())) {
                ExtraSceneInfo extraSceneInfo = this.S;
                extraSceneInfo.setAccPath(extraSceneInfo.getOldHeadWearPath());
                this.W = 0;
            }
            String str3 = this.S.getNewHeadWearPathList() != null ? (String) this.S.getNewHeadWearPathList().get(this.W) : "";
            if (!TextUtils.isEmpty(str3) && !this.S.getAccPath().equals(str3)) {
                this.Q.setHeadWearTexture(str3);
                this.S.setAccPath(str3);
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Set Acc ->  = ");
                sb6.append(str3);
                LOG.d(str2, sb6.toString());
            }
            if (this.S.getExtraStatus() != 3) {
                float[] expWeights = aSAvatarProcessInfo.getExpWeights();
                ExpressInfo expressInfo = (ExpressInfo) this.S.getExpressInfoList().get(this.V);
                int[] expressIndex = expressInfo.getExpressIndex();
                ArrayList expressValue = expressInfo.getExpressValue();
                for (int i4 = 0; i4 < expressValue.size(); i4++) {
                    String str4 = "] = ";
                    String str5 = "set express[";
                    if (expressIndex.length == expressValue.size()) {
                        expWeights[expressIndex[i4]] = ((Float) expressValue.get(i4)).floatValue();
                        sb = new StringBuilder();
                        sb.append(str5);
                        i2 = expressIndex[i4];
                    } else {
                        expWeights[expressIndex[0]] = ((Float) expressValue.get(i4)).floatValue();
                        sb = new StringBuilder();
                        sb.append(str5);
                        i2 = expressIndex[0];
                    }
                    sb.append(i2);
                    sb.append(str4);
                    sb.append(expressValue.get(i4));
                    LOG.d(str2, sb.toString());
                }
                aSAvatarProcessInfo.setExpWeights(expWeights);
                if (this.S.getExtraStatus() == 1) {
                    AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
                    if (avatarExtraSceneTriggerCallback != null) {
                        avatarExtraSceneTriggerCallback.onExtraSceneTrigger("ClownHat", this.S.getHeadWear());
                    }
                }
                if (this.V >= this.S.getExpressInfoList().size() - 1) {
                    this.S.setExtraStatus(3);
                    this.V = 0;
                } else {
                    this.V++;
                    this.S.setExtraStatus(2);
                }
            }
        }
    }

    private void c(String str, ExtraSceneInfo extraSceneInfo) {
        if (!TextUtils.isEmpty(str)) {
            extraSceneInfo.getClass();
            HairMaskInfo hairMaskInfo = new HairMaskInfo();
            String[] split = str.split(";");
            for (int i2 = 0; i2 < split.length; i2++) {
                boolean z2 = true;
                int parseInt = Integer.parseInt(split[i2].substring(split[i2].indexOf("=") + 1));
                if (split[i2].indexOf(H) != -1) {
                    hairMaskInfo.setHairId(parseInt);
                } else if (split[i2].indexOf(I) != -1) {
                    hairMaskInfo.setExpId(parseInt);
                } else if (split[i2].indexOf(J) != -1) {
                    if (parseInt != 1) {
                        z2 = false;
                    }
                    hairMaskInfo.setBlendShape(z2);
                } else if (split[i2].indexOf(K) != -1) {
                    if (parseInt != 1) {
                        z2 = false;
                    }
                    hairMaskInfo.setMask(z2);
                }
            }
            extraSceneInfo.setHairMaskInfo(hairMaskInfo);
        }
    }

    private void d(ASAvatarProcessInfo aSAvatarProcessInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("[54] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[54]);
        sb.append(", [55] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[55]);
        LOG.d("check_expweight", sb.toString());
        if (!m(aSAvatarProcessInfo)) {
            if (this.S.getExtraStatus() != 2) {
                if (this.S.getExtraStatus() == 3) {
                    this.S.setExtraStatus(1);
                    this.X = 0;
                    return;
                }
                this.X = 0;
                return;
            }
        } else if (this.S.getExtraStatus() != 3) {
            this.X++;
        } else {
            return;
        }
        if (this.X > 3) {
            float[] expWeights = aSAvatarProcessInfo.getExpWeights();
            a((ExpressInfo) this.S.getExpressInfoList().get(this.V), expWeights);
            if (this.S.getExtraStatus() == 1) {
                AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
                if (avatarExtraSceneTriggerCallback != null) {
                    avatarExtraSceneTriggerCallback.onExtraSceneTrigger(ExtraSceneInfo.ANGEL_WINGS_CAP, this.S.getHeadWear());
                }
            }
            if (this.S.getExtraStatus() != 3) {
                if (this.V >= this.S.getExpressInfoList().size() - 1) {
                    this.S.setExtraStatus(3);
                    this.V = 0;
                } else {
                    this.V++;
                    this.S.setExtraStatus(2);
                }
            }
            aSAvatarProcessInfo.setExpWeights(expWeights);
        }
    }

    private void e(ASAvatarProcessInfo aSAvatarProcessInfo) {
        String str;
        float[] orientations = aSAvatarProcessInfo.getOrientations();
        boolean z2 = false;
        String str2 = "emojiHelmet";
        if (orientations != null) {
            if (orientations[1] < -15.0f && orientations[0] < 10.0f) {
                LOG.d(str2, "--- left---");
                this.aa++;
            } else if (orientations[1] <= 15.0f || orientations[0] >= 10.0f) {
                this.aa = 0;
            } else {
                LOG.d(str2, "--- right---");
                this.ab++;
                this.aa = 0;
            }
            this.ab = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mCheckLeftFrameCount -> ");
        sb.append(this.aa);
        sb.append(", mCheckRightFrameCount -> ");
        sb.append(this.ab);
        LOG.d(str2, sb.toString());
        if (this.aa >= 3 || this.ab >= 3) {
            z2 = true;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("check -> ");
        sb2.append(z2);
        LOG.d(str2, sb2.toString());
        if (!z2) {
            this.S.setExtraStatus(1);
        } else if (this.S.getExtraStatus() != 3) {
            if (this.ac == null) {
                this.ac = new Random();
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("old left = ");
            sb3.append(this.S.getEmojiHelmetLeftRandomNum());
            sb3.append(", old right = ");
            sb3.append(this.S.getEmojiHelmetRightRandomNum());
            LOG.d(str2, sb3.toString());
            while (true) {
                int nextInt = this.ac.nextInt(this.S.getRandom());
                StringBuilder sb4 = new StringBuilder();
                sb4.append("random = ");
                sb4.append(nextInt);
                LOG.d(str2, sb4.toString());
                if (this.aa < 3) {
                    if (!(this.ab < 3 || this.S.getEmojiHelmetLeftRandomNum() == nextInt || this.S.getEmojiHelmetRightRandomNum() == nextInt)) {
                        this.S.setEmojiHelmetRightRandomNum(nextInt);
                        break;
                    }
                } else if (!(this.S.getEmojiHelmetLeftRandomNum() == nextInt || this.S.getEmojiHelmetRightRandomNum() == nextInt)) {
                    this.S.setEmojiHelmetLeftRandomNum(nextInt);
                    break;
                }
            }
            StringBuilder sb5 = new StringBuilder();
            sb5.append("new left = ");
            sb5.append(this.S.getEmojiHelmetLeftRandomNum());
            sb5.append(", new right = ");
            sb5.append(this.S.getEmojiHelmetRightRandomNum());
            LOG.d(str2, sb5.toString());
            if (this.aa >= 3 || this.ab >= 3) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Tex_");
                sb6.append(this.S.getEmojiHelmetLeftRandomNum());
                sb6.append("-");
                sb6.append(this.S.getEmojiHelmetRightRandomNum());
                sb6.append("_acc.png");
                str = sb6.toString();
            } else {
                str = "";
            }
            Iterator it = this.S.getNewHeadWearPathList().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String str3 = (String) it.next();
                if (str3.indexOf(str) != -1) {
                    if (!this.S.getAccPath().equals(str3)) {
                        this.Q.setHeadWearTexture(str3);
                        this.S.setAccPath(str3);
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("acc path = ");
                        sb7.append(str3);
                        LOG.d(str2, sb7.toString());
                    } else {
                        return;
                    }
                }
            }
            AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
            if (avatarExtraSceneTriggerCallback != null) {
                avatarExtraSceneTriggerCallback.onExtraSceneTrigger("EmojiHelmet", this.S.getHeadWear());
            }
            this.S.setExtraStatus(3);
        }
    }

    private void f(ASAvatarProcessInfo aSAvatarProcessInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("[54] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[54]);
        sb.append(", [55] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[55]);
        LOG.d("check_expweight", sb.toString());
        if (!m(aSAvatarProcessInfo)) {
            if (this.S.getExtraStatus() != 2) {
                if (this.S.getExtraStatus() == 3) {
                    this.S.setExtraStatus(1);
                    this.X = 0;
                    return;
                }
                this.X = 0;
                return;
            }
        } else if (this.S.getExtraStatus() != 3) {
            this.X++;
        } else {
            return;
        }
        if (this.X > 3) {
            float[] expWeights = aSAvatarProcessInfo.getExpWeights();
            ExpressInfo expressInfo = (ExpressInfo) this.S.getExpressInfoList().get(this.V);
            a(expressInfo, expWeights);
            if (this.S.getExtraStatus() == 1) {
                AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
                if (avatarExtraSceneTriggerCallback != null) {
                    avatarExtraSceneTriggerCallback.onExtraSceneTrigger(ExtraSceneInfo.CHICKEN_HAT, this.S.getHeadWear());
                }
            }
            if (this.S.getExtraStatus() != 3) {
                String str = "expWeights";
                if (expressInfo.getLoopStatus() == 2) {
                    int loopCount = this.S.getLoopCount();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("-- loopCount = ");
                    sb2.append(loopCount);
                    sb2.append(", number = ");
                    sb2.append(this.S.getLoopNum());
                    LOG.d(str, sb2.toString());
                    if (loopCount < this.S.getLoopNum()) {
                        this.V = this.S.getLoopBeginFrameIndex();
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("-- mExtraSceneIndex = ");
                        sb3.append(this.V);
                        LOG.d(str, sb3.toString());
                        this.S.setLoopCount(loopCount + 1);
                        aSAvatarProcessInfo.setExpWeights(expWeights);
                        return;
                    }
                }
                if (this.V >= this.S.getExpressInfoList().size() - 1) {
                    LOG.d(str, "--- end ---");
                    this.S.setExtraStatus(3);
                    this.S.setLoopCount(0);
                    this.V = 0;
                    this.Z = 0;
                } else {
                    this.V++;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("--> mExtraSceneIndex = ");
                    sb4.append(this.V);
                    LOG.d(str, sb4.toString());
                    this.S.setExtraStatus(2);
                }
            }
            aSAvatarProcessInfo.setExpWeights(expWeights);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0070 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void g(ASAvatarProcessInfo aSAvatarProcessInfo) {
        boolean z2;
        if (this.S.getExtraStatus() == 1) {
            if (aSAvatarProcessInfo.getExpWeights()[0] > 0.8f && aSAvatarProcessInfo.getExpWeights()[1] < 0.2f) {
                this.aa++;
            } else if (aSAvatarProcessInfo.getExpWeights()[1] <= 0.8f || aSAvatarProcessInfo.getExpWeights()[0] >= 0.2f) {
                this.aa = 0;
            } else {
                this.ab++;
                this.aa = 0;
                z2 = this.aa < 3 || this.ab > 3;
                StringBuilder sb = new StringBuilder();
                sb.append("check = ");
                sb.append(z2);
                LOG.d("wizardHat", sb.toString());
                if (!z2) {
                    return;
                }
            }
            this.ab = 0;
            if (this.aa < 3) {
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("check = ");
            sb2.append(z2);
            LOG.d("wizardHat", sb2.toString());
            if (!z2) {
            }
        }
        float[] expWeights = aSAvatarProcessInfo.getExpWeights();
        ExpressInfo expressInfo = (ExpressInfo) this.S.getExpressInfoList().get(this.V);
        a(expressInfo, expWeights);
        if (this.S.getExtraStatus() == 1) {
            AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
            if (avatarExtraSceneTriggerCallback != null) {
                avatarExtraSceneTriggerCallback.onExtraSceneTrigger("WizardHat", this.S.getHeadWear());
            }
        }
        if (this.S.getExtraStatus() != 3) {
            String str = "expWeights";
            if (expressInfo.getLoopStatus() == 2) {
                int loopCount = this.S.getLoopCount();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("-- loopCount = ");
                sb3.append(loopCount);
                sb3.append(", number = ");
                sb3.append(this.S.getLoopNum());
                LOG.d(str, sb3.toString());
                if (loopCount < this.S.getLoopNum()) {
                    this.V = this.S.getLoopBeginFrameIndex();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("-- mExtraSceneIndex = ");
                    sb4.append(this.V);
                    LOG.d(str, sb4.toString());
                    this.S.setLoopCount(loopCount + 1);
                    aSAvatarProcessInfo.setExpWeights(expWeights);
                    return;
                }
            }
            if (this.V >= this.S.getExpressInfoList().size() - 1) {
                LOG.d(str, "--- end ---");
                this.S.setExtraStatus(1);
                this.S.setLoopCount(0);
                this.V = 0;
                this.Z = 0;
                this.aa = 0;
                this.ab = 0;
            } else {
                this.V++;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("--> mExtraSceneIndex = ");
                sb5.append(this.V);
                LOG.d(str, sb5.toString());
                this.S.setExtraStatus(2);
            }
        }
        aSAvatarProcessInfo.setExpWeights(expWeights);
    }

    private void h(ASAvatarProcessInfo aSAvatarProcessInfo) {
        boolean m2 = m(aSAvatarProcessInfo);
        StringBuilder sb = new StringBuilder();
        sb.append("[58] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[58]);
        LOG.d("check_expweight", sb.toString());
        if (!m2) {
            if (this.S.getExtraStatus() != 2) {
                if (this.S.getExtraStatus() == 3) {
                    this.S.setExtraStatus(1);
                }
                return;
            }
        } else if (this.S.getExtraStatus() == 3) {
            return;
        }
        float[] expWeights = aSAvatarProcessInfo.getExpWeights();
        a((ExpressInfo) this.S.getExpressInfoList().get(this.V), expWeights);
        if (this.S.getExtraStatus() != 3) {
            if (this.V >= this.S.getExpressInfoList().size() - 1) {
                this.S.setExtraStatus(3);
                this.V = 0;
            } else {
                this.V++;
                this.S.setExtraStatus(2);
            }
        }
        aSAvatarProcessInfo.setExpWeights(expWeights);
    }

    private void i(ASAvatarProcessInfo aSAvatarProcessInfo) {
        int i2;
        StringBuilder sb;
        float[] orientations = aSAvatarProcessInfo.getOrientations();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[2] = ");
        sb2.append(aSAvatarProcessInfo.getOrientations()[2]);
        LOG.d("astronautHat", sb2.toString());
        if (orientations != null && orientations.length >= 3) {
            int i3 = orientations[2] > 15.0f ? 1 : orientations[2] < -15.0f ? 2 : 0;
            if (i3 == 0) {
                if (this.S.getExtraStatus() != 2) {
                    if (this.S.getExtraStatus() == 3) {
                        this.S.setExtraStatus(1);
                    }
                    return;
                }
            } else if ((i3 == 1 && this.S.getExtraStatus() == 1) || this.S.getExtraStatus() == 3) {
                return;
            }
            float[] expWeights = aSAvatarProcessInfo.getExpWeights();
            ExpressInfo expressInfo = (ExpressInfo) this.S.getExpressInfoList().get(this.V);
            int[] expressIndex = expressInfo.getExpressIndex();
            ArrayList expressValue = expressInfo.getExpressValue();
            for (int i4 = 0; i4 < expressValue.size(); i4++) {
                String str = "] = ";
                String str2 = "set express[";
                String str3 = "expWeights";
                if (expressIndex.length == expressValue.size()) {
                    expWeights[expressIndex[i4]] = ((Float) expressValue.get(i4)).floatValue();
                    sb = new StringBuilder();
                    sb.append(str2);
                    i2 = expressIndex[i4];
                } else {
                    expWeights[expressIndex[0]] = ((Float) expressValue.get(i4)).floatValue();
                    sb = new StringBuilder();
                    sb.append(str2);
                    i2 = expressIndex[0];
                }
                sb.append(i2);
                sb.append(str);
                sb.append(expressValue.get(i4));
                LOG.d(str3, sb.toString());
            }
            if (i3 != 0) {
                this.S.setHeadPitchStatus(i3);
            }
            if (this.S.getExtraStatus() == 1) {
                AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
                if (avatarExtraSceneTriggerCallback != null) {
                    avatarExtraSceneTriggerCallback.onExtraSceneTrigger(ExtraSceneInfo.ASTRONAUT_HELMET, this.S.getHeadWear());
                }
            }
            if (!expressInfo.isPauseFrame() || this.S.getHeadPitchStatus() != 2) {
                if (this.S.getHeadPitchStatus() == 1) {
                    String oldHeadWearPath = this.S.getOldHeadWearPath();
                    if (!TextUtils.isEmpty(oldHeadWearPath) && !this.S.getAccPath().equals(oldHeadWearPath)) {
                        this.Q.setHeadWearTexture(oldHeadWearPath);
                        this.S.setAccPath(oldHeadWearPath);
                    }
                }
                if (this.S.getExtraStatus() != 3) {
                    if (this.V >= this.S.getExpressInfoList().size() - 1) {
                        this.S.setExtraStatus(3);
                        this.V = 0;
                    } else {
                        this.V++;
                        this.S.setExtraStatus(2);
                    }
                }
                aSAvatarProcessInfo.setExpWeights(expWeights);
            } else {
                j(aSAvatarProcessInfo);
            }
        }
    }

    private void j(ASAvatarProcessInfo aSAvatarProcessInfo) {
        String str;
        StringBuilder sb;
        float f2;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[48] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[48]);
        sb2.append(", [49] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[49]);
        sb2.append(", [50] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[50]);
        sb2.append(", [51] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[51]);
        sb2.append(", [52] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[52]);
        sb2.append(", [54] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[54]);
        sb2.append(", [55] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[55]);
        sb2.append(", [21] = ");
        sb2.append(aSAvatarProcessInfo.getExpWeights()[21]);
        String str2 = "check_expweight";
        LOG.d(str2, sb2.toString());
        int i2 = 0;
        int i3 = 0;
        boolean z2 = false;
        while (true) {
            if (i3 >= this.S.getExpCheckIdList().size()) {
                i3 = 0;
                break;
            }
            int[] iArr = (int[]) this.S.getExpCheckIdList().get(i3);
            float[] fArr = (float[]) this.S.getExpCheckValueList().get(i3);
            boolean z3 = z2;
            int i4 = 0;
            while (true) {
                if (i4 >= iArr.length) {
                    z2 = z3;
                    break;
                }
                String str3 = ", exp[";
                String str4 = "[";
                str = "check_astronautHat";
                String str5 = "] = ";
                if (fArr[i4] >= 0.0f && aSAvatarProcessInfo.getExpWeights()[iArr[i4]] > fArr[i4]) {
                    sb = new StringBuilder();
                    sb.append(str4);
                    sb.append(iArr[i4]);
                    sb.append(str5);
                    sb.append(fArr[i4]);
                    sb.append(str3);
                    sb.append(iArr[i4]);
                    sb.append(str5);
                    f2 = aSAvatarProcessInfo.getExpWeights()[iArr[i4]];
                } else if (fArr[i4] >= 0.0f || aSAvatarProcessInfo.getExpWeights()[iArr[i4]] >= (-fArr[i4])) {
                    LOG.d(str, "--- false ---");
                    z2 = false;
                } else {
                    sb = new StringBuilder();
                    sb.append(str4);
                    sb.append(iArr[i4]);
                    sb.append(str5);
                    sb.append(-fArr[i4]);
                    sb.append(str3);
                    sb.append(iArr[i4]);
                    sb.append(str5);
                    f2 = aSAvatarProcessInfo.getExpWeights()[iArr[i4]];
                }
                sb.append(f2);
                LOG.d(str, sb.toString());
                i4++;
                z3 = true;
            }
            LOG.d(str, "--- false ---");
            z2 = false;
            if (z2) {
                break;
            }
            i3++;
        }
        if (this.S.getNewHeadWearPathList() != null && i3 >= 0) {
            String str6 = (String) this.S.getNewHeadWearPathList().get(i3);
            if (!TextUtils.isEmpty(str6) && !this.S.getAccPath().equals(str6)) {
                if (i3 == this.Y) {
                    i2 = this.X + 1;
                } else {
                    this.Y = i3;
                }
                this.X = i2;
                if (this.X > 4) {
                    this.Q.setHeadWearTexture(str6);
                    this.S.setAccPath(str6);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("new headwear path = ");
                    sb3.append(str6);
                    LOG.d(str2, sb3.toString());
                }
            }
        }
    }

    private void k(ASAvatarProcessInfo aSAvatarProcessInfo) {
        if (TextUtils.isEmpty(this.S.getAccPath())) {
            ExtraSceneInfo extraSceneInfo = this.S;
            extraSceneInfo.setAccPath(extraSceneInfo.getOldHeadWearPath());
        }
        float[] expWeights = aSAvatarProcessInfo.getExpWeights();
        StringBuilder sb = new StringBuilder();
        sb.append("[48] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[48]);
        sb.append(", [49] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[49]);
        sb.append(", [50] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[50]);
        sb.append(", [51] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[51]);
        sb.append(", [52] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[52]);
        sb.append(", [54] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[54]);
        sb.append(", [55] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[55]);
        sb.append(", [21] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[21]);
        sb.append(", [58] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[58]);
        String str = "check_expweight";
        LOG.d(str, sb.toString());
        if (!(this.S.getExpIdInPut() == null || this.S.getExpIdOutPut() == null)) {
            for (int i2 = 0; i2 < this.S.getExpIdInPut().length; i2++) {
                expWeights[this.S.getExpIdInPut()[i2]] = expWeights[this.S.getExpIdOutPut()[i2]];
                StringBuilder sb2 = new StringBuilder();
                sb2.append("[");
                sb2.append(this.S.getExpIdInPut()[i2]);
                String str2 = "] = ";
                sb2.append(str2);
                sb2.append(expWeights[this.S.getExpIdInPut()[i2]]);
                sb2.append(", [");
                sb2.append(this.S.getExpIdOutPut()[i2]);
                sb2.append(str2);
                sb2.append(expWeights[this.S.getExpIdOutPut()[i2]]);
                LOG.d("dogHat", sb2.toString());
            }
            aSAvatarProcessInfo.setExpWeights(expWeights);
        }
        boolean z2 = false;
        for (int i3 = 0; i3 < this.S.getExpCheckIdList().size(); i3++) {
            int[] iArr = (int[]) this.S.getExpCheckIdList().get(i3);
            float[] fArr = (float[]) this.S.getExpCheckValueList().get(i3);
            boolean z3 = z2;
            int i4 = 0;
            while (true) {
                if (i4 >= iArr.length) {
                    z2 = z3;
                    break;
                } else if ((fArr[i4] < 0.0f || aSAvatarProcessInfo.getExpWeights()[iArr[i4]] < fArr[i4]) && (fArr[i4] >= 0.0f || aSAvatarProcessInfo.getExpWeights()[iArr[i4]] > (-fArr[i4]))) {
                    z2 = false;
                } else {
                    i4++;
                    z3 = true;
                }
            }
            z2 = false;
            if (z2) {
                break;
            }
        }
        ExtraSceneInfo extraSceneInfo2 = this.S;
        String oldHeadWearPath = z2 ? (String) extraSceneInfo2.getNewHeadWearPathList().get(0) : extraSceneInfo2.getOldHeadWearPath();
        if (!TextUtils.isEmpty(oldHeadWearPath) && !this.S.getAccPath().equals(oldHeadWearPath)) {
            AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
            if (avatarExtraSceneTriggerCallback != null) {
                avatarExtraSceneTriggerCallback.onExtraSceneTrigger(ExtraSceneInfo.SHIBAINU_HAT, this.S.getHeadWear());
            }
            this.Q.setHeadWearTexture(oldHeadWearPath);
            this.S.setAccPath(oldHeadWearPath);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("new headwear path = ");
            sb3.append(oldHeadWearPath);
            LOG.d(str, sb3.toString());
        }
    }

    private void l(ASAvatarProcessInfo aSAvatarProcessInfo) {
        if (!m(aSAvatarProcessInfo)) {
            if (this.S.getExtraStatus() != 2) {
                if (this.S.getExtraStatus() == 3) {
                    this.S.setExtraStatus(1);
                    this.X = 0;
                    return;
                }
                this.X = 0;
                return;
            }
        } else if (this.S.getExtraStatus() != 3) {
            this.X++;
        } else {
            return;
        }
        if (this.X > 3) {
            float[] expWeights = aSAvatarProcessInfo.getExpWeights();
            ExpressInfo expressInfo = (ExpressInfo) this.S.getExpressInfoList().get(this.V);
            a(expressInfo, expWeights);
            if (this.S.getExtraStatus() == 1) {
                AvatarExtraSceneTriggerCallback avatarExtraSceneTriggerCallback = this.ae;
                if (avatarExtraSceneTriggerCallback != null) {
                    avatarExtraSceneTriggerCallback.onExtraSceneTrigger("PrincessHat", this.S.getHeadWear());
                }
            }
            if (this.S.getExtraStatus() != 3) {
                String str = "expWeights";
                if (expressInfo.getLoopStatus() == 2) {
                    int loopCount = this.S.getLoopCount();
                    StringBuilder sb = new StringBuilder();
                    sb.append("-- loopCount = ");
                    sb.append(loopCount);
                    sb.append(", number = ");
                    sb.append(this.S.getLoopNum());
                    LOG.d(str, sb.toString());
                    if (loopCount < this.S.getLoopNum()) {
                        this.V = this.S.getLoopBeginFrameIndex();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("-- mExtraSceneIndex = ");
                        sb2.append(this.V);
                        LOG.d(str, sb2.toString());
                        this.S.setLoopCount(loopCount + 1);
                        aSAvatarProcessInfo.setExpWeights(expWeights);
                        return;
                    }
                }
                if (this.V >= this.S.getExpressInfoList().size() - 1) {
                    LOG.d(str, "--- end ---");
                    this.S.setExtraStatus(3);
                    this.S.setLoopCount(0);
                    this.V = 0;
                    this.Z = 0;
                } else {
                    this.V++;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("--> mExtraSceneIndex = ");
                    sb3.append(this.V);
                    LOG.d(str, sb3.toString());
                    this.S.setExtraStatus(2);
                }
            }
            aSAvatarProcessInfo.setExpWeights(expWeights);
        }
    }

    private boolean m(ASAvatarProcessInfo aSAvatarProcessInfo) {
        if (this.S.getExpCheckIdList() == null || this.S.getExpCheckValueList() == null) {
            return false;
        }
        boolean z2 = false;
        for (int i2 = 0; i2 < this.S.getExpCheckIdList().size(); i2++) {
            int[] iArr = (int[]) this.S.getExpCheckIdList().get(i2);
            float[] fArr = (float[]) this.S.getExpCheckValueList().get(i2);
            boolean z3 = z2;
            int i3 = 0;
            while (true) {
                if (i3 >= iArr.length) {
                    z2 = z3;
                    break;
                } else if ((fArr[i3] <= 0.0f || aSAvatarProcessInfo.getExpWeights()[iArr[i3]] < fArr[i3]) && (fArr[i3] > 0.0f || aSAvatarProcessInfo.getExpWeights()[iArr[i3]] > (-fArr[i3]))) {
                    z2 = false;
                } else {
                    i3++;
                    z3 = true;
                }
            }
            if (z2) {
                break;
            }
        }
        return z2;
    }

    public void checkExtraScene(ASAvatarProcessInfo aSAvatarProcessInfo) {
        String str;
        ExtraSceneInfo extraSceneInfo;
        String str2 = a;
        if (aSAvatarProcessInfo == null) {
            str = "info is null";
        } else if (this.R == null) {
            str = "mConfigValue is null";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("mConfigValue -> ");
            sb.append(this.R.toString());
            LOG.d("ConfigValue", sb.toString());
            char c2 = 65535;
            if (this.R.configHeadwearStyleID != -1) {
                if (this.S == null) {
                    Iterator it = this.T.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        extraSceneInfo = (ExtraSceneInfo) it.next();
                        if (!(this.R.configHeadwearStyleID == extraSceneInfo.getHeadWear() && (extraSceneInfo.getHeadWearColor() == -1 || this.R.configHeadwearColorID == extraSceneInfo.getHeadWearColor()))) {
                        }
                    }
                    this.S = extraSceneInfo;
                }
                ExtraSceneInfo extraSceneInfo2 = this.S;
                if (extraSceneInfo2 == null) {
                    str = "mExtraSceneInfo is null";
                } else {
                    String name = extraSceneInfo2.getName();
                    switch (name.hashCode()) {
                        case -1528869837:
                            if (name.equals("EmojiHelmet")) {
                                c2 = 7;
                                break;
                            }
                            break;
                        case -531597040:
                            if (name.equals("WizardHat")) {
                                c2 = 6;
                                break;
                            }
                            break;
                        case -353674158:
                            if (name.equals("PrincessHat")) {
                                c2 = 10;
                                break;
                            }
                            break;
                        case -5252658:
                            if (name.equals(D)) {
                                c2 = 2;
                                break;
                            }
                            break;
                        case 5541034:
                            if (name.equals(E)) {
                                c2 = 8;
                                break;
                            }
                            break;
                        case 599957095:
                            if (name.equals("AngelWings")) {
                                c2 = 0;
                                break;
                            }
                            break;
                        case 729448247:
                            if (name.equals("RabbitEars")) {
                                c2 = 4;
                                break;
                            }
                            break;
                        case 1152018798:
                            if (name.equals("RabbitTeeth")) {
                                c2 = 3;
                                break;
                            }
                            break;
                        case 1161409278:
                            if (name.equals("ClownHat")) {
                                c2 = 1;
                                break;
                            }
                            break;
                        case 1716027151:
                            if (name.equals(y)) {
                                c2 = 5;
                                break;
                            }
                            break;
                        case 2052433887:
                            if (name.equals(F)) {
                                c2 = 9;
                                break;
                            }
                            break;
                    }
                    switch (c2) {
                        case 0:
                            d(aSAvatarProcessInfo);
                            break;
                        case 1:
                            c(aSAvatarProcessInfo);
                            break;
                        case 2:
                            h(aSAvatarProcessInfo);
                            break;
                        case 3:
                            b(aSAvatarProcessInfo);
                            break;
                        case 4:
                            a(aSAvatarProcessInfo);
                            break;
                        case 5:
                            f(aSAvatarProcessInfo);
                            break;
                        case 6:
                            g(aSAvatarProcessInfo);
                            break;
                        case 7:
                            e(aSAvatarProcessInfo);
                            break;
                        case 8:
                            i(aSAvatarProcessInfo);
                            break;
                        case 9:
                            k(aSAvatarProcessInfo);
                            break;
                        case 10:
                            l(aSAvatarProcessInfo);
                            break;
                    }
                    return;
                }
            } else {
                return;
            }
        }
        LOG.d(str2, str);
    }

    public void resetExtraScene() {
        this.W = 0;
        this.X = 0;
        this.Y = -1;
        this.V = 0;
        this.aa = 0;
        this.ab = 0;
        this.Z = 0;
        if (this.Q != null) {
            ExtraSceneInfo extraSceneInfo = this.S;
            if (extraSceneInfo != null) {
                if (extraSceneInfo != null) {
                    if (!TextUtils.isEmpty(extraSceneInfo.getOldToothPath())) {
                        this.Q.setToothTexture(this.S.getOldToothPath());
                    }
                    if (!TextUtils.isEmpty(this.S.getOldHeadWearPath())) {
                        this.Q.setHeadWearTexture(this.S.getOldHeadWearPath());
                        ExtraSceneInfo extraSceneInfo2 = this.S;
                        extraSceneInfo2.setAccPath(extraSceneInfo2.getOldHeadWearPath());
                    }
                    this.S.setIsOldHeadWearAcc(true);
                    this.S.setExtraStatus(1);
                    this.S.setLoopCount(0);
                    this.S.setEmojiHelmetLeftRandomNum(0);
                    this.S.setEmojiHelmetRightRandomNum(1);
                    this.S.setHeadPitchStatus(0);
                    List list = this.ad;
                    if (list != null && list.size() > 0) {
                        this.Q.setHairRenderParam(true, true);
                    }
                    this.ad.clear();
                }
                this.S = null;
            }
        }
    }

    public void setAvatarEngine(AvatarEngine avatarEngine) {
        LOG.d(a, "-- setAvatarEngine --");
        this.Q = avatarEngine;
        this.Q.getConfigValue(this.R);
        this.ad.clear();
        this.S = null;
    }
}

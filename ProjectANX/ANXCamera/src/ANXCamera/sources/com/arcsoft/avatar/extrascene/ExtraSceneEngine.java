package com.arcsoft.avatar.extrascene;

import android.text.TextUtils;
import com.arcsoft.avatar.AvatarConfig.ASAvatarConfigValue;
import com.arcsoft.avatar.AvatarConfig.ASAvatarProcessInfo;
import com.arcsoft.avatar.AvatarEngine;
import com.arcsoft.avatar.BackgroundInfo;
import com.arcsoft.avatar.extrascene.ExtraSceneInfo.ExpressInfo;
import com.arcsoft.avatar.extrascene.ExtraSceneInfo.HairMaskInfo;
import com.arcsoft.avatar.util.LOG;
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
    private static final String G = "hairId";
    private static final String H = "expId";
    private static final String I = "blendshape";
    private static final String J = "mask";
    private static final int K = 2;
    private static final int L = 1;
    private static final int M = 2;
    private static final int N = 3;
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
    private String O;
    private AvatarEngine P;
    private ASAvatarConfigValue Q;
    private ExtraSceneInfo R = null;
    private ArrayList S = null;
    private String T;
    private int U = 0;
    private int V = 0;
    private int W = 0;
    private int X = 0;
    private int Y = 0;
    private int Z = 0;
    private int aa = 0;
    private Random ab = null;
    private List ac = null;

    public ExtraSceneEngine(String str) {
        this.T = str;
        this.Q = new ASAvatarConfigValue();
        this.S = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/");
        sb.append(b);
        this.O = sb.toString();
        a();
        this.ac = new ArrayList();
    }

    private void a() {
        String str;
        String str2 = a;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("getXmlInfo = ");
            sb.append(this.O);
            LOG.d(str2, sb.toString());
            FileInputStream fileInputStream = new FileInputStream(this.O);
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
                            this.S.add(extraSceneInfo);
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
                                sb2.append(this.T);
                                sb2.append(str4);
                                sb2.append(newPullParser.getText());
                                extraSceneInfo.setOldHeadWearPath(sb2.toString());
                            } else if (str3.equals(k)) {
                                if (extraSceneInfo.getRandom() > 0) {
                                    a(extraSceneInfo, newPullParser.getText());
                                } else {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(this.T);
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
                                sb4.append(this.T);
                                sb4.append(str4);
                                sb4.append(newPullParser.getText());
                                extraSceneInfo.setOldToothPath(sb4.toString());
                            } else if (str3.equals(o)) {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(this.T);
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
        if (this.R.getExtraStatus() == 1) {
            boolean l2 = l(aSAvatarProcessInfo);
            if (l2) {
                i2 = this.W + 1;
                this.W = i2;
            } else {
                i2 = 0;
            }
            this.W = i2;
            StringBuilder sb = new StringBuilder();
            sb.append("check = ");
            sb.append(l2);
            sb.append(", mCheckFrameCount = ");
            sb.append(this.W);
            LOG.d("rabbitEars", sb.toString());
            if (this.W < 3) {
                return;
            }
        }
        float[] expWeights = aSAvatarProcessInfo.getExpWeights();
        if (this.R.getExtraStatus() == 3) {
            this.U = this.R.getExpressInfoList().size() - 1;
        }
        a((ExpressInfo) this.R.getExpressInfoList().get(this.U), expWeights);
        if (this.R.getExtraStatus() != 3) {
            if (this.U >= this.R.getExpressInfoList().size() - 1) {
                this.R.setExtraStatus(3);
                this.U = 0;
            } else {
                this.U++;
                this.R.setExtraStatus(2);
            }
        }
        aSAvatarProcessInfo.setExpWeights(expWeights);
    }

    private void a(ExpressInfo expressInfo, float[] fArr) {
        int i2;
        StringBuilder sb;
        int[] expressIndex = expressInfo.getExpressIndex();
        ArrayList expressValue = expressInfo.getExpressValue();
        if (this.ac.size() <= 0) {
            ArrayList hairMaskInfoList = this.R.getHairMaskInfoList();
            if (hairMaskInfoList != null) {
                for (int i3 = 0; i3 < hairMaskInfoList.size(); i3++) {
                    HairMaskInfo hairMaskInfo = (HairMaskInfo) hairMaskInfoList.get(i3);
                    if (this.Q.configHairStyleID == hairMaskInfo.getHairId()) {
                        this.ac.add(hairMaskInfo);
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
            Iterator it = this.ac.iterator();
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
                    this.P.setHairRenderParam(hairMaskInfo2.getBlendShape(), hairMaskInfo2.getMask());
                    break;
                }
            }
        }
        String str4 = "rabbitEars";
        if (this.R.getNewHeadWearPathList() != null) {
            String str5 = (String) this.R.getNewHeadWearPathList().get(0);
            if (!TextUtils.isEmpty(str5)) {
                if (!this.R.getAccPath().equals(str5)) {
                    this.P.setHeadWearTexture(str5);
                    this.R.setAccPath(str5);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("new headwear path = ");
                    sb3.append(str5);
                    LOG.d(str4, sb3.toString());
                } else {
                    return;
                }
            }
        }
        if (!TextUtils.isEmpty(this.R.getNewToothPath())) {
            this.P.setToothTexture(this.R.getNewToothPath());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("new tooth path = ");
            sb4.append(this.R.getNewToothPath());
            LOG.d(str4, sb4.toString());
        }
    }

    private void a(ExtraSceneInfo extraSceneInfo, String str) {
        for (int i2 = 0; i2 < extraSceneInfo.getRandom(); i2++) {
            for (int i3 = 0; i3 < extraSceneInfo.getRandom(); i3++) {
                if (i2 != i3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.T);
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
        a((ExpressInfo) this.R.getExpressInfoList().get(this.U), expWeights);
        if (this.U >= this.R.getExpressInfoList().size() - 1) {
            i2 = 0;
        } else {
            i2 = this.U + 1;
            this.U = i2;
        }
        this.U = i2;
        if (!(this.R.getExpIdInPut() == null || this.R.getExpIdOutPut() == null)) {
            for (int i3 = 0; i3 < this.R.getExpIdInPut().length; i3++) {
                expWeights[this.R.getExpIdInPut()[i3]] = -expWeights[this.R.getExpIdOutPut()[i3]];
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.R.getExpIdInPut()[i3]);
                sb.append(", = ");
                sb.append(this.R.getExpIdOutPut()[i3]);
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
        if (l(aSAvatarProcessInfo)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(aSAvatarProcessInfo.getExpWeights()[55]);
            LOG.d(str2, sb3.toString());
            if (this.R.getExtraStatus() != 3) {
                this.W++;
            } else {
                return;
            }
        } else if (this.R.getExtraStatus() != 2) {
            if (this.R.getExtraStatus() == 3) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(aSAvatarProcessInfo.getExpWeights()[55]);
                LOG.d(str2, sb4.toString());
                this.R.setExtraStatus(1);
                this.W = 0;
                if (this.R.getNewHeadWearPathList() != null) {
                    if (this.V < this.R.getNewHeadWearPathList().size() - 1) {
                        i3 = this.V + 1;
                        this.V = i3;
                    }
                    this.V = i3;
                }
                return;
            }
            this.W = 0;
            return;
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("mCheckFrameCount = ");
        sb5.append(this.W);
        LOG.d(str2, sb5.toString());
        if (this.W > 3) {
            if (TextUtils.isEmpty(this.R.getAccPath())) {
                ExtraSceneInfo extraSceneInfo = this.R;
                extraSceneInfo.setAccPath(extraSceneInfo.getOldHeadWearPath());
                this.V = 0;
            }
            String str3 = this.R.getNewHeadWearPathList() != null ? (String) this.R.getNewHeadWearPathList().get(this.V) : "";
            if (!TextUtils.isEmpty(str3) && !this.R.getAccPath().equals(str3)) {
                this.P.setHeadWearTexture(str3);
                this.R.setAccPath(str3);
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Set Acc ->  = ");
                sb6.append(str3);
                LOG.d(str2, sb6.toString());
            }
            if (this.R.getExtraStatus() != 3) {
                float[] expWeights = aSAvatarProcessInfo.getExpWeights();
                ExpressInfo expressInfo = (ExpressInfo) this.R.getExpressInfoList().get(this.U);
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
                if (this.U >= this.R.getExpressInfoList().size() - 1) {
                    this.R.setExtraStatus(3);
                    this.U = 0;
                } else {
                    this.U++;
                    this.R.setExtraStatus(2);
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
                if (split[i2].indexOf(G) != -1) {
                    hairMaskInfo.setHairId(parseInt);
                } else if (split[i2].indexOf(H) != -1) {
                    hairMaskInfo.setExpId(parseInt);
                } else if (split[i2].indexOf(I) != -1) {
                    if (parseInt != 1) {
                        z2 = false;
                    }
                    hairMaskInfo.setBlendShape(z2);
                } else if (split[i2].indexOf(J) != -1) {
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
        if (!l(aSAvatarProcessInfo)) {
            if (this.R.getExtraStatus() != 2) {
                if (this.R.getExtraStatus() == 3) {
                    this.R.setExtraStatus(1);
                    this.W = 0;
                    return;
                }
                this.W = 0;
                return;
            }
        } else if (this.R.getExtraStatus() != 3) {
            this.W++;
        } else {
            return;
        }
        if (this.W > 3) {
            float[] expWeights = aSAvatarProcessInfo.getExpWeights();
            a((ExpressInfo) this.R.getExpressInfoList().get(this.U), expWeights);
            if (this.R.getExtraStatus() != 3) {
                if (this.U >= this.R.getExpressInfoList().size() - 1) {
                    this.R.setExtraStatus(3);
                    this.U = 0;
                } else {
                    this.U++;
                    this.R.setExtraStatus(2);
                }
            }
            aSAvatarProcessInfo.setExpWeights(expWeights);
        }
    }

    private void e(ASAvatarProcessInfo aSAvatarProcessInfo) {
        String str;
        float[] orientations = aSAvatarProcessInfo.getOrientations();
        String str2 = "emojiHelmet";
        boolean z2 = false;
        if (orientations != null) {
            if (orientations[1] >= -15.0f || orientations[0] >= 10.0f) {
                if (orientations[1] <= 15.0f || orientations[0] >= 10.0f) {
                    this.Z = 0;
                } else {
                    LOG.d(str2, "--- left---");
                    this.Z++;
                }
                this.aa = 0;
            } else {
                LOG.d(str2, "--- right---");
                this.aa++;
                this.Z = 0;
            }
        }
        if (this.Z >= 3 || this.aa >= 3) {
            z2 = true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("check -> ");
        sb.append(z2);
        LOG.d(str2, sb.toString());
        if (!z2) {
            this.R.setExtraStatus(1);
        } else if (this.R.getExtraStatus() != 3) {
            if (this.ab == null) {
                this.ab = new Random();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("old left = ");
            sb2.append(this.R.getEmojiHelmetLeftRandomNum());
            sb2.append(", old right = ");
            sb2.append(this.R.getEmojiHelmetRightRandomNum());
            LOG.d(str2, sb2.toString());
            while (true) {
                int nextInt = this.ab.nextInt(this.R.getRandom());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("random = ");
                sb3.append(nextInt);
                LOG.d(str2, sb3.toString());
                if (this.Z < 3) {
                    if (!(this.aa < 3 || this.R.getEmojiHelmetLeftRandomNum() == nextInt || this.R.getEmojiHelmetRightRandomNum() == nextInt)) {
                        this.R.setEmojiHelmetRightRandomNum(nextInt);
                        break;
                    }
                } else if (!(this.R.getEmojiHelmetLeftRandomNum() == nextInt || this.R.getEmojiHelmetRightRandomNum() == nextInt)) {
                    this.R.setEmojiHelmetLeftRandomNum(nextInt);
                    break;
                }
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append("new left = ");
            sb4.append(this.R.getEmojiHelmetLeftRandomNum());
            sb4.append(", new right = ");
            sb4.append(this.R.getEmojiHelmetRightRandomNum());
            LOG.d(str2, sb4.toString());
            if (this.Z >= 3 || this.aa >= 3) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Tex_");
                sb5.append(this.R.getEmojiHelmetRightRandomNum());
                sb5.append("-");
                sb5.append(this.R.getEmojiHelmetLeftRandomNum());
                sb5.append("_acc.png");
                str = sb5.toString();
            } else {
                str = "";
            }
            Iterator it = this.R.getNewHeadWearPathList().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String str3 = (String) it.next();
                if (str3.indexOf(str) != -1) {
                    if (!this.R.getAccPath().equals(str3)) {
                        this.P.setHeadWearTexture(str3);
                        this.R.setAccPath(str3);
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("acc path = ");
                        sb6.append(str3);
                        LOG.d(str2, sb6.toString());
                    } else {
                        return;
                    }
                }
            }
            this.R.setExtraStatus(3);
        }
    }

    private void f(ASAvatarProcessInfo aSAvatarProcessInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("[54] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[54]);
        sb.append(", [55] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[55]);
        LOG.d("check_expweight", sb.toString());
        if (!l(aSAvatarProcessInfo)) {
            if (this.R.getExtraStatus() != 2) {
                if (this.R.getExtraStatus() == 3) {
                    this.R.setExtraStatus(1);
                    this.W = 0;
                    return;
                }
                this.W = 0;
                return;
            }
        } else if (this.R.getExtraStatus() != 3) {
            this.W++;
        } else {
            return;
        }
        if (this.W > 3) {
            float[] expWeights = aSAvatarProcessInfo.getExpWeights();
            ExpressInfo expressInfo = (ExpressInfo) this.R.getExpressInfoList().get(this.U);
            a(expressInfo, expWeights);
            if (this.R.getExtraStatus() != 3) {
                String str = "expWeights";
                if (expressInfo.getLoopStatus() == 2) {
                    int loopCount = this.R.getLoopCount();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("-- loopCount = ");
                    sb2.append(loopCount);
                    sb2.append(", number = ");
                    sb2.append(this.R.getLoopNum());
                    LOG.d(str, sb2.toString());
                    if (loopCount < this.R.getLoopNum()) {
                        this.U = this.R.getLoopBeginFrameIndex();
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("-- mExtraSceneIndex = ");
                        sb3.append(this.U);
                        LOG.d(str, sb3.toString());
                        this.R.setLoopCount(loopCount + 1);
                        aSAvatarProcessInfo.setExpWeights(expWeights);
                        return;
                    }
                }
                if (this.U >= this.R.getExpressInfoList().size() - 1) {
                    LOG.d(str, "--- end ---");
                    this.R.setExtraStatus(3);
                    this.R.setLoopCount(0);
                    this.U = 0;
                    this.Y = 0;
                } else {
                    this.U++;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("--> mExtraSceneIndex = ");
                    sb4.append(this.U);
                    LOG.d(str, sb4.toString());
                    this.R.setExtraStatus(2);
                }
            }
            aSAvatarProcessInfo.setExpWeights(expWeights);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0070 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void g(ASAvatarProcessInfo aSAvatarProcessInfo) {
        boolean z2;
        if (this.R.getExtraStatus() == 1) {
            if (aSAvatarProcessInfo.getExpWeights()[0] > 0.8f && aSAvatarProcessInfo.getExpWeights()[1] < 0.2f) {
                this.Z++;
            } else if (aSAvatarProcessInfo.getExpWeights()[1] <= 0.8f || aSAvatarProcessInfo.getExpWeights()[0] >= 0.2f) {
                this.Z = 0;
            } else {
                this.aa++;
                this.Z = 0;
                z2 = this.Z < 3 || this.aa > 3;
                StringBuilder sb = new StringBuilder();
                sb.append("check = ");
                sb.append(z2);
                LOG.d("wizardHat", sb.toString());
                if (!z2) {
                    return;
                }
            }
            this.aa = 0;
            if (this.Z < 3) {
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("check = ");
            sb2.append(z2);
            LOG.d("wizardHat", sb2.toString());
            if (!z2) {
            }
        }
        float[] expWeights = aSAvatarProcessInfo.getExpWeights();
        ExpressInfo expressInfo = (ExpressInfo) this.R.getExpressInfoList().get(this.U);
        a(expressInfo, expWeights);
        if (this.R.getExtraStatus() != 3) {
            String str = "expWeights";
            if (expressInfo.getLoopStatus() == 2) {
                int loopCount = this.R.getLoopCount();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("-- loopCount = ");
                sb3.append(loopCount);
                sb3.append(", number = ");
                sb3.append(this.R.getLoopNum());
                LOG.d(str, sb3.toString());
                if (loopCount < this.R.getLoopNum()) {
                    this.U = this.R.getLoopBeginFrameIndex();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("-- mExtraSceneIndex = ");
                    sb4.append(this.U);
                    LOG.d(str, sb4.toString());
                    this.R.setLoopCount(loopCount + 1);
                    aSAvatarProcessInfo.setExpWeights(expWeights);
                    return;
                }
            }
            if (this.U >= this.R.getExpressInfoList().size() - 1) {
                LOG.d(str, "--- end ---");
                this.R.setExtraStatus(1);
                this.R.setLoopCount(0);
                this.U = 0;
                this.Y = 0;
                this.Z = 0;
                this.aa = 0;
            } else {
                this.U++;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("--> mExtraSceneIndex = ");
                sb5.append(this.U);
                LOG.d(str, sb5.toString());
                this.R.setExtraStatus(2);
            }
        }
        aSAvatarProcessInfo.setExpWeights(expWeights);
    }

    private void h(ASAvatarProcessInfo aSAvatarProcessInfo) {
        boolean l2 = l(aSAvatarProcessInfo);
        StringBuilder sb = new StringBuilder();
        sb.append("[58] = ");
        sb.append(aSAvatarProcessInfo.getExpWeights()[58]);
        LOG.d("check_expweight", sb.toString());
        if (!l2) {
            if (this.R.getExtraStatus() != 2) {
                if (this.R.getExtraStatus() == 3) {
                    this.R.setExtraStatus(1);
                }
                return;
            }
        } else if (this.R.getExtraStatus() == 3) {
            return;
        }
        float[] expWeights = aSAvatarProcessInfo.getExpWeights();
        a((ExpressInfo) this.R.getExpressInfoList().get(this.U), expWeights);
        if (this.R.getExtraStatus() != 3) {
            if (this.U >= this.R.getExpressInfoList().size() - 1) {
                this.R.setExtraStatus(3);
                this.U = 0;
            } else {
                this.U++;
                this.R.setExtraStatus(2);
            }
        }
        aSAvatarProcessInfo.setExpWeights(expWeights);
    }

    private void i(ASAvatarProcessInfo aSAvatarProcessInfo) {
        int i2;
        StringBuilder sb;
        ASAvatarProcessInfo aSAvatarProcessInfo2 = aSAvatarProcessInfo;
        float[] orientations = aSAvatarProcessInfo.getOrientations();
        if (orientations != null && orientations.length >= 3) {
            int i3 = orientations[2] > 15.0f ? 1 : orientations[2] < -15.0f ? 2 : 0;
            if (i3 == 0 && this.R.getHeadPitchStatus() == 0) {
                j(aSAvatarProcessInfo);
                return;
            }
            if (i3 == 0) {
                if (this.R.getExtraStatus() != 2) {
                    if (this.R.getExtraStatus() == 3) {
                        this.R.setHeadPitchStatus(0);
                        this.R.setExtraStatus(1);
                    }
                    return;
                }
            } else if (i3 == 2 && this.R.getExtraStatus() == 1) {
                j(aSAvatarProcessInfo);
                return;
            } else if (this.R.getExtraStatus() == 3) {
                j(aSAvatarProcessInfo);
                return;
            }
            float[] expWeights = aSAvatarProcessInfo.getExpWeights();
            ExpressInfo expressInfo = (ExpressInfo) this.R.getExpressInfoList().get(this.U);
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
                this.R.setHeadPitchStatus(i3);
            }
            if (this.R.getHeadPitchStatus() != 2 && this.R.getHeadPitchStatus() == 1) {
                if (expressInfo.isPauseFrame()) {
                    aSAvatarProcessInfo2.setExpWeights(expWeights);
                    return;
                }
                String oldHeadWearPath = this.R.getOldHeadWearPath();
                if (!TextUtils.isEmpty(oldHeadWearPath) && !this.R.getAccPath().equals(oldHeadWearPath)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("-> ");
                    sb2.append(oldHeadWearPath);
                    LOG.d("check_expweight", sb2.toString());
                    this.P.setHeadWearTexture(oldHeadWearPath);
                    this.R.setAccPath(oldHeadWearPath);
                }
            }
            if (this.R.getExtraStatus() != 3) {
                if (this.U >= this.R.getExpressInfoList().size() - 1) {
                    this.R.setExtraStatus(3);
                    this.U = 0;
                } else {
                    this.U++;
                    this.R.setExtraStatus(2);
                }
            }
            aSAvatarProcessInfo2.setExpWeights(expWeights);
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
            if (i3 >= this.R.getExpCheckIdList().size()) {
                i3 = 0;
                break;
            }
            int[] iArr = (int[]) this.R.getExpCheckIdList().get(i3);
            float[] fArr = (float[]) this.R.getExpCheckValueList().get(i3);
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
        if (this.R.getNewHeadWearPathList() != null && i3 >= 0) {
            String str6 = (String) this.R.getNewHeadWearPathList().get(i3);
            if (!TextUtils.isEmpty(str6) && !this.R.getAccPath().equals(str6)) {
                if (i3 == this.X) {
                    i2 = this.W + 1;
                } else {
                    this.X = i3;
                }
                this.W = i2;
                if (this.W > 3) {
                    this.P.setHeadWearTexture(str6);
                    this.R.setAccPath(str6);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("new headwear path = ");
                    sb3.append(str6);
                    LOG.d(str2, sb3.toString());
                }
            }
        }
    }

    private void k(ASAvatarProcessInfo aSAvatarProcessInfo) {
        if (TextUtils.isEmpty(this.R.getAccPath())) {
            ExtraSceneInfo extraSceneInfo = this.R;
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
        if (!(this.R.getExpIdInPut() == null || this.R.getExpIdOutPut() == null)) {
            for (int i2 = 0; i2 < this.R.getExpIdInPut().length; i2++) {
                expWeights[this.R.getExpIdInPut()[i2]] = expWeights[this.R.getExpIdOutPut()[i2]];
                StringBuilder sb2 = new StringBuilder();
                sb2.append("[");
                sb2.append(this.R.getExpIdInPut()[i2]);
                String str2 = "] = ";
                sb2.append(str2);
                sb2.append(expWeights[this.R.getExpIdInPut()[i2]]);
                sb2.append(", [");
                sb2.append(this.R.getExpIdOutPut()[i2]);
                sb2.append(str2);
                sb2.append(expWeights[this.R.getExpIdOutPut()[i2]]);
                LOG.d("dogHat", sb2.toString());
            }
            aSAvatarProcessInfo.setExpWeights(expWeights);
        }
        boolean z2 = false;
        for (int i3 = 0; i3 < this.R.getExpCheckIdList().size(); i3++) {
            int[] iArr = (int[]) this.R.getExpCheckIdList().get(i3);
            float[] fArr = (float[]) this.R.getExpCheckValueList().get(i3);
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
        ExtraSceneInfo extraSceneInfo2 = this.R;
        String oldHeadWearPath = z2 ? (String) extraSceneInfo2.getNewHeadWearPathList().get(0) : extraSceneInfo2.getOldHeadWearPath();
        if (!TextUtils.isEmpty(oldHeadWearPath) && !this.R.getAccPath().equals(oldHeadWearPath)) {
            this.P.setHeadWearTexture(oldHeadWearPath);
            this.R.setAccPath(oldHeadWearPath);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("new headwear path = ");
            sb3.append(oldHeadWearPath);
            LOG.d(str, sb3.toString());
        }
    }

    private boolean l(ASAvatarProcessInfo aSAvatarProcessInfo) {
        if (this.R.getExpCheckIdList() == null || this.R.getExpCheckValueList() == null) {
            return false;
        }
        boolean z2 = false;
        for (int i2 = 0; i2 < this.R.getExpCheckIdList().size(); i2++) {
            int[] iArr = (int[]) this.R.getExpCheckIdList().get(i2);
            float[] fArr = (float[]) this.R.getExpCheckValueList().get(i2);
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
        } else if (this.Q == null) {
            str = "mConfigValue is null";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("mConfigValue -> ");
            sb.append(this.Q.toString());
            LOG.d("ConfigValue", sb.toString());
            char c2 = 65535;
            if (this.Q.configHeadwearStyleID != -1) {
                if (this.R == null) {
                    Iterator it = this.S.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        extraSceneInfo = (ExtraSceneInfo) it.next();
                        if (!(this.Q.configHeadwearStyleID == extraSceneInfo.getHeadWear() && (extraSceneInfo.getHeadWearColor() == -1 || this.Q.configHeadwearColorID == extraSceneInfo.getHeadWearColor()))) {
                        }
                    }
                    this.R = extraSceneInfo;
                }
                ExtraSceneInfo extraSceneInfo2 = this.R;
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
        this.V = 0;
        this.W = 0;
        this.X = -1;
        this.U = 0;
        this.Z = 0;
        this.aa = 0;
        this.Y = 0;
        if (this.P != null) {
            ExtraSceneInfo extraSceneInfo = this.R;
            if (extraSceneInfo != null) {
                if (extraSceneInfo != null) {
                    if (!TextUtils.isEmpty(extraSceneInfo.getOldToothPath())) {
                        this.P.setToothTexture(this.R.getOldToothPath());
                    }
                    if (!TextUtils.isEmpty(this.R.getOldHeadWearPath())) {
                        this.P.setHeadWearTexture(this.R.getOldHeadWearPath());
                        ExtraSceneInfo extraSceneInfo2 = this.R;
                        extraSceneInfo2.setAccPath(extraSceneInfo2.getOldHeadWearPath());
                    }
                    this.R.setIsOldHeadWearAcc(true);
                    this.R.setExtraStatus(1);
                    this.R.setLoopCount(0);
                    this.R.setEmojiHelmetLeftRandomNum(0);
                    this.R.setEmojiHelmetRightRandomNum(0);
                    this.R.setHeadPitchStatus(0);
                    List list = this.ac;
                    if (list != null && list.size() > 0) {
                        this.P.setHairRenderParam(true, true);
                    }
                    this.ac.clear();
                }
                this.R = null;
            }
        }
    }

    public void setAvatarEngine(AvatarEngine avatarEngine) {
        LOG.d(a, "-- setAvatarEngine --");
        this.P = avatarEngine;
        this.P.getConfigValue(this.Q);
        this.ac.clear();
        this.R = null;
    }
}

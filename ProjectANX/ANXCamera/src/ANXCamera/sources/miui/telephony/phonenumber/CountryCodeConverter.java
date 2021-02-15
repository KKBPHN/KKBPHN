package miui.telephony.phonenumber;

import android.content.res.Resources;
import com.miui.internal.R;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import miui.util.AppConstants;

public class CountryCodeConverter {
    public static final String AC = "247";
    public static final String AD = "376";
    public static final String AE = "971";
    public static final String AF = "93";
    public static final String AG = "1268";
    public static final String AI = "1264";
    public static final String AL = "355";
    public static final String AM = "374";
    public static final String AN = "599";
    public static final String AO = "244";
    public static final String AR = "54";
    public static final String AS = "1684";
    public static final String AT = "43";
    public static final String AU = "61";
    public static final String AW = "297";
    public static final String AZ = "994";
    public static final String BA = "387";
    public static final String BB = "1246";
    public static final String BD = "880";
    public static final String BE = "32";
    public static final String BF = "226";
    public static final String BG = "359";
    public static final String BH = "973";
    public static final String BI = "257";
    public static final String BJ = "229";
    public static final String BM = "1441";
    public static final String BN = "673";
    public static final String BO = "591";
    public static final String BR = "55";
    public static final String BS = "1242";
    public static final String BT = "975";
    public static final String BW = "267";
    public static final String BY = "375";
    public static final String BZ = "501";
    public static final String CD = "243";
    public static final String CF = "236";
    public static final String CG = "242";
    public static final String CH = "41";
    public static final String CI = "225";
    public static final String CK = "682";
    public static final String CL = "56";
    public static final String CM = "237";
    public static final String CN = "86";
    public static final String CO = "57";
    public static final String CR = "506";
    public static final String CU = "53";
    public static final String CV = "238";
    public static final String CY = "357";
    public static final String CZ = "420";
    public static final String DE = "49";
    public static final String DJ = "253";
    public static final String DK = "45";
    public static final String DM = "1767";
    public static final String DO = "1809";
    public static final String DO1 = "1829";
    public static final String DO2 = "1849";
    public static final String DZ = "213";
    public static final String EC = "593";
    public static final String EE = "372";
    public static final String EG = "20";
    private static final String EMPTY = "";
    public static final String ER = "291";
    public static final String ES = "34";
    public static final String ET = "251";
    public static final String FI = "358";
    public static final String FJ = "679";
    public static final String FK = "500";
    public static final String FM = "691";
    public static final String FO = "298";
    public static final String FR = "33";
    public static final String GA = "241";
    public static final String GD = "1473";
    public static final String GE = "995";
    public static final String GF = "594";
    public static final String GG = "441481";
    public static final String GG1 = "447781";
    public static final String GG2 = "447839";
    public static final String GG3 = "447911";
    public static final String GH = "233";
    public static final String GI = "350";
    public static final String GL = "299";
    public static final String GM = "220";
    public static final String GN = "224";
    public static final String GP_BL_MF = "590";
    public static final String GQ = "240";
    public static final String GR = "30";
    public static final String GT = "502";
    public static final String GU = "1671";
    public static final String GW = "245";
    public static final String GY = "592";
    public static final String HK = "852";
    public static final String HN = "504";
    public static final String HR = "385";
    public static final String HT = "509";
    public static final String HU = "36";
    public static final String ID = "62";
    public static final String IE = "353";
    public static final String IL = "972";
    public static final String IM = "441624";
    public static final String IM1 = "447524";
    public static final String IM2 = "447624";
    public static final String IM3 = "447924";
    public static final String IN = "91";
    public static final String IO = "246";
    public static final String IQ = "964";
    public static final String IR = "98";
    public static final String IS = "354";
    public static final String IT = "39";
    public static final String JE = "441534";
    public static final String JE1 = "447509";
    public static final String JE2 = "447700";
    public static final String JE3 = "447797";
    public static final String JE4 = "447829";
    public static final String JM = "1876";
    public static final String JO = "962";
    public static final String JP = "81";
    public static final String KE = "254";
    public static final String KG = "996";
    public static final String KH = "855";
    public static final String KI = "686";
    public static final String KM = "269";
    public static final String KN = "1869";
    public static final String KP = "850";
    public static final String KR = "82";
    public static final String KW = "965";
    public static final String KY = "1345";
    public static final String LA = "856";
    public static final String LB = "961";
    public static final String LC = "1758";
    public static final String LI = "423";
    public static final String LK = "94";
    public static final String LR = "231";
    public static final String LS = "266";
    public static final String LT = "370";
    public static final String LU = "352";
    public static final String LV = "371";
    public static final String LY = "218";
    public static final String MA = "212";
    public static final String MC = "377";
    public static final String MD = "373";
    public static final String ME = "382";
    public static final String MG = "261";
    public static final String MH = "692";
    public static final String MK = "389";
    public static final String ML = "223";
    public static final String MM = "95";
    public static final String MN = "976";
    public static final String MO = "853";
    public static final String MP = "1670";
    public static final String MQ = "596";
    public static final String MR = "222";
    public static final String MS = "1664";
    public static final String MT = "356";
    public static final String MU = "230";
    public static final String MV = "960";
    public static final String MW = "265";
    public static final String MX = "52";
    public static final String MY = "60";
    public static final String MZ = "258";
    public static final String NA = "264";
    public static final String NC = "687";
    public static final String NE = "227";
    public static final String NG = "234";
    public static final String NI = "505";
    public static final String NL = "31";
    public static final String NO = "47";
    public static final String NP = "977";
    public static final String NR = "674";
    public static final String NU = "683";
    public static final String NZ = "64";
    public static final String OM = "968";
    public static final String PA = "507";
    public static final String PE = "51";
    public static final String PF = "689";
    public static final String PG = "675";
    public static final String PH = "63";
    public static final String PK = "92";
    public static final String PL = "48";
    public static final String PM = "508";
    public static final String PR = "1787";
    public static final String PR1 = "1939";
    public static final String PS = "970";
    public static final String PT = "351";
    public static final String PW = "680";
    public static final String PY = "595";
    public static final String QA = "974";
    public static final String RE_YT = "262";
    public static final String RO = "40";
    public static final String RS = "381";
    public static final String RU_KZ = "7";
    public static final String RW = "250";
    public static final String SA = "966";
    public static final String SB = "677";
    public static final String SC = "248";
    public static final String SD = "249";
    public static final String SE = "46";
    public static final String SG = "65";
    public static final String SH = "290";
    public static final String SI = "386";
    public static final String SK = "421";
    public static final String SL = "232";
    public static final String SM = "378";
    public static final String SN = "221";
    public static final String SO = "252";
    public static final String SR = "597";
    public static final String ST = "239";
    public static final String SV = "503";
    public static final String SX = "1721";
    public static final String SY = "963";
    public static final String SZ = "268";
    public static final String TC = "1649";
    public static final String TD = "235";
    public static final String TG = "228";
    public static final String TH = "66";
    public static final String TJ = "992";
    public static final String TK = "690";
    public static final String TL = "670";
    public static final String TM = "993";
    public static final String TN = "216";
    public static final String TO = "676";
    public static final String TR = "90";
    public static final String TT = "1868";
    public static final String TV = "688";
    public static final String TW = "886";
    public static final String TZ = "255";
    public static final String UA = "380";
    public static final String UG = "256";
    public static final String UK = "44";
    public static final String US_CA = "1";
    public static final String UY = "598";
    public static final String UZ = "998";
    public static final String VA = "3906698";
    public static final String VC = "1784";
    public static final String VE = "58";
    public static final String VG = "1284";
    public static final String VI = "1340";
    public static final String VN = "84";
    public static final String VU = "678";
    public static final String WF = "681";
    public static final String WS = "685";
    public static final String YE = "967";
    public static final String ZA = "27";
    public static final String ZM = "260";
    public static final String ZW = "263";
    private static final HashMap sMcc2Idd = new HashMap();
    private static final HashMap sMccMap = new HashMap();
    private static final HashMap sNameMap = new HashMap();

    static {
        sMccMap.put("412", AF);
        sMccMap.put("276", AL);
        HashMap hashMap = sMccMap;
        String str = DZ;
        hashMap.put("603", str);
        sMccMap.put("544", AS);
        HashMap hashMap2 = sMccMap;
        String str2 = AD;
        hashMap2.put(str, str2);
        HashMap hashMap3 = sMccMap;
        String str3 = AO;
        hashMap3.put("631", str3);
        sMccMap.put("365", AI);
        sMccMap.put("344", AG);
        sMccMap.put("722", AR);
        HashMap hashMap4 = sMccMap;
        String str4 = AM;
        hashMap4.put("283", str4);
        HashMap hashMap5 = sMccMap;
        String str5 = AW;
        hashMap5.put("363", str5);
        HashMap hashMap6 = sMccMap;
        String str6 = NI;
        hashMap6.put(str6, AU);
        HashMap hashMap7 = sMccMap;
        String str7 = SL;
        hashMap7.put(str7, AT);
        sMccMap.put("400", AZ);
        sMccMap.put("364", BS);
        sMccMap.put("426", BH);
        String str8 = "470";
        sMccMap.put(str8, BD);
        sMccMap.put("342", BB);
        HashMap hashMap8 = sMccMap;
        String str9 = BI;
        hashMap8.put(str9, BY);
        sMccMap.put("206", BE);
        sMccMap.put("702", BZ);
        sMccMap.put("616", BJ);
        HashMap hashMap9 = sMccMap;
        String str10 = GI;
        hashMap9.put(str10, BM);
        sMccMap.put("402", BT);
        sMccMap.put("736", BO);
        sMccMap.put(LY, BA);
        sMccMap.put("652", BW);
        sMccMap.put("724", BR);
        sMccMap.put("348", VG);
        sMccMap.put("528", BN);
        sMccMap.put("284", BG);
        sMccMap.put("613", BF);
        sMccMap.put("642", str9);
        sMccMap.put("456", KH);
        sMccMap.put("624", CM);
        String str11 = "1";
        sMccMap.put("302", str11);
        sMccMap.put("625", CV);
        sMccMap.put("346", KY);
        sMccMap.put("623", CF);
        sMccMap.put("622", TD);
        sMccMap.put("730", CL);
        sMccMap.put("460", CN);
        sMccMap.put("732", CO);
        sMccMap.put("654", KM);
        sMccMap.put("548", CK);
        sMccMap.put("712", CR);
        sMccMap.put("612", CI);
        sMccMap.put("219", HR);
        sMccMap.put("368", CU);
        sMccMap.put("280", CY);
        sMccMap.put(MU, CZ);
        sMccMap.put("630", CD);
        sMccMap.put(CV, DK);
        sMccMap.put("638", DJ);
        sMccMap.put("366", DM);
        sMccMap.put(LT, DO);
        sMccMap.put("740", EC);
        sMccMap.put("602", "20");
        sMccMap.put("706", SV);
        sMccMap.put("627", "240");
        sMccMap.put("657", ER);
        sMccMap.put(SC, EE);
        sMccMap.put("636", ET);
        sMccMap.put("750", "500");
        sMccMap.put("288", FO);
        sMccMap.put("542", FJ);
        sMccMap.put(str3, FI);
        sMccMap.put("208", FR);
        sMccMap.put("742", GF);
        sMccMap.put("547", PF);
        sMccMap.put("628", GA);
        sMccMap.put("607", GM);
        sMccMap.put("282", GE);
        sMccMap.put(RE_YT, DE);
        sMccMap.put("620", GH);
        sMccMap.put(LS, str10);
        sMccMap.put("202", "30");
        sMccMap.put(SH, GL);
        sMccMap.put(LU, GD);
        sMccMap.put("535", GU);
        sMccMap.put("704", GT);
        sMccMap.put("611", GN);
        sMccMap.put("632", GW);
        sMccMap.put("738", GY);
        sMccMap.put(EE, HT);
        sMccMap.put("708", HN);
        sMccMap.put("454", HK);
        sMccMap.put(TN, HU);
        sMccMap.put("274", IS);
        sMccMap.put("404", IN);
        sMccMap.put("405", IN);
        sMccMap.put("510", ID);
        sMccMap.put("432", IR);
        sMccMap.put("418", IQ);
        sMccMap.put("272", IE);
        sMccMap.put("425", IL);
        sMccMap.put(MR, IT);
        sMccMap.put("338", JM);
        sMccMap.put("440", JP);
        sMccMap.put("441", JP);
        sMccMap.put("416", JO);
        sMccMap.put("401", "7");
        sMccMap.put("639", KE);
        sMccMap.put("545", KI);
        sMccMap.put("467", KP);
        sMccMap.put("450", KR);
        sMccMap.put("419", KW);
        sMccMap.put("437", KG);
        sMccMap.put("457", LA);
        sMccMap.put(AC, LV);
        sMccMap.put("415", LB);
        sMccMap.put("651", LS);
        sMccMap.put("618", LR);
        sMccMap.put("606", LY);
        sMccMap.put("295", LI);
        sMccMap.put(IO, LT);
        sMccMap.put("270", LU);
        sMccMap.put("455", MO);
        sMccMap.put("294", MK);
        sMccMap.put("646", MG);
        sMccMap.put("650", MW);
        sMccMap.put(GT, "60");
        sMccMap.put("472", MV);
        sMccMap.put("610", ML);
        sMccMap.put("278", MT);
        sMccMap.put("551", MH);
        sMccMap.put("340", MQ);
        sMccMap.put("609", MR);
        sMccMap.put("617", MU);
        sMccMap.put("334", MX);
        sMccMap.put("550", FM);
        sMccMap.put("259", MD);
        sMccMap.put(MA, MC);
        sMccMap.put("428", MN);
        sMccMap.put(str5, ME);
        sMccMap.put(IS, MS);
        sMccMap.put("604", MA);
        sMccMap.put("643", MZ);
        sMccMap.put("414", MM);
        sMccMap.put("649", NA);
        sMccMap.put("536", NR);
        sMccMap.put("429", NP);
        sMccMap.put("204", NL);
        sMccMap.put("362", AN);
        sMccMap.put("546", NC);
        sMccMap.put("530", NZ);
        sMccMap.put("710", str6);
        sMccMap.put("614", NE);
        sMccMap.put("621", NG);
        sMccMap.put("534", MP);
        sMccMap.put(CG, NO);
        sMccMap.put("422", OM);
        sMccMap.put("410", PK);
        sMccMap.put("552", PW);
        sMccMap.put(LI, PS);
        sMccMap.put("714", PA);
        sMccMap.put("537", PG);
        sMccMap.put("744", PY);
        sMccMap.put("716", PE);
        sMccMap.put("515", PH);
        sMccMap.put(ZM, PL);
        sMccMap.put(SZ, PT);
        sMccMap.put("330", PR);
        sMccMap.put("427", QA);
        sMccMap.put("629", CG);
        sMccMap.put("647", RE_YT);
        sMccMap.put(BF, "40");
        sMccMap.put(RW, "7");
        sMccMap.put("635", RW);
        sMccMap.put(MT, KN);
        sMccMap.put(FI, LC);
        sMccMap.put("308", PM);
        sMccMap.put("360", VC);
        sMccMap.put("549", WS);
        sMccMap.put("292", SM);
        sMccMap.put("626", ST);
        sMccMap.put(CZ, SA);
        sMccMap.put("608", SN);
        sMccMap.put(GM, RS);
        sMccMap.put("633", SC);
        sMccMap.put("619", str7);
        sMccMap.put("525", SG);
        sMccMap.put(LR, SK);
        sMccMap.put("293", SI);
        sMccMap.put("540", SB);
        sMccMap.put("637", SO);
        sMccMap.put("655", ZA);
        sMccMap.put("214", ES);
        sMccMap.put("413", LK);
        sMccMap.put("634", SD);
        sMccMap.put("746", SR);
        sMccMap.put("653", SZ);
        sMccMap.put("240", SE);
        sMccMap.put(TG, CH);
        sMccMap.put("417", SY);
        sMccMap.put("466", TW);
        sMccMap.put("436", TJ);
        sMccMap.put("640", TZ);
        sMccMap.put("520", TH);
        sMccMap.put("514", NZ);
        sMccMap.put("615", TG);
        sMccMap.put("539", TO);
        sMccMap.put(str4, TT);
        sMccMap.put("605", TN);
        sMccMap.put("286", TR);
        sMccMap.put("438", TM);
        sMccMap.put(str2, TC);
        sMccMap.put("641", UG);
        sMccMap.put(NG, UK);
        sMccMap.put(TD, UK);
        sMccMap.put(TZ, UA);
        HashMap hashMap10 = sMccMap;
        String str12 = AE;
        hashMap10.put("424", str12);
        sMccMap.put("430", str12);
        sMccMap.put("431", str12);
        sMccMap.put("310", str11);
        sMccMap.put("316", str11);
        sMccMap.put("311", str11);
        sMccMap.put("748", UY);
        sMccMap.put("332", VI);
        sMccMap.put("434", UZ);
        sMccMap.put("541", VU);
        sMccMap.put(CI, VA);
        sMccMap.put("734", VE);
        sMccMap.put("452", VN);
        sMccMap.put("543", WF);
        sMccMap.put(SK, YE);
        sMccMap.put("645", ZM);
        sMccMap.put("648", ZW);
        String str13 = "011";
        sMcc2Idd.put("302", Arrays.asList(new String[]{str13}));
        sMcc2Idd.put("310", Arrays.asList(new String[]{str13}));
        sMcc2Idd.put("311", Arrays.asList(new String[]{str13}));
        sMcc2Idd.put("316", Arrays.asList(new String[]{str13}));
        String str14 = "00";
        sMcc2Idd.put("334", Arrays.asList(new String[]{str14}));
        sMcc2Idd.put("404", Arrays.asList(new String[]{str14}));
        sMcc2Idd.put("405", Arrays.asList(new String[]{str14}));
        sMcc2Idd.put("425", Arrays.asList(new String[]{str14}));
        String str15 = "001";
        sMcc2Idd.put("428", Arrays.asList(new String[]{str15}));
        sMcc2Idd.put("440", Arrays.asList(new String[]{"010"}));
        sMcc2Idd.put("441", Arrays.asList(new String[]{"010"}));
        sMcc2Idd.put("450", Arrays.asList(new String[]{"00700", "001", "002", "005", "006", "008", "00300", "00780"}));
        sMcc2Idd.put("452", Arrays.asList(new String[]{str14}));
        sMcc2Idd.put("454", Arrays.asList(new String[]{"001", "002", "003", "0050", "0059", "0060", "007", "008", "009", "1666"}));
        sMcc2Idd.put("455", Arrays.asList(new String[]{str14}));
        sMcc2Idd.put("460", Arrays.asList(new String[]{str14}));
        sMcc2Idd.put("466", Arrays.asList(new String[]{"002", "005", "006", "007", "009", "019"}));
        sMcc2Idd.put(str8, Arrays.asList(new String[]{str14}));
        sMcc2Idd.put("510", Arrays.asList(new String[]{str15}));
        sMcc2Idd.put("530", Arrays.asList(new String[]{str14}));
        sMcc2Idd.put("535", Arrays.asList(new String[]{str13}));
        sMcc2Idd.put("520", Arrays.asList(new String[]{str15}));
        sMcc2Idd.put("724", Arrays.asList(new String[]{str14}));
    }

    public static String getCountryCode(String str) {
        return (String) sMccMap.get(str);
    }

    public static String getIddCode(String str) {
        if (sMcc2Idd.get(str) != null) {
            return (String) ((List) sMcc2Idd.get(str)).get(0);
        }
        return null;
    }

    public static List getIddCodes(String str) {
        return (List) sMcc2Idd.get(str);
    }

    private static void initNameMap() {
        if (sNameMap.isEmpty()) {
            Resources resources = AppConstants.getCurrentApplication().getResources();
            sNameMap.put(BS, resources.getString(R.string.country_name_bs));
            sNameMap.put(BB, resources.getString(R.string.country_name_bb));
            sNameMap.put(AI, resources.getString(R.string.country_name_ai));
            sNameMap.put(AG, resources.getString(R.string.country_name_ag));
            sNameMap.put(VG, resources.getString(R.string.country_name_vg));
            sNameMap.put(VI, resources.getString(R.string.country_name_vi));
            sNameMap.put(KY, resources.getString(R.string.country_name_ky));
            sNameMap.put(BM, resources.getString(R.string.country_name_bm));
            sNameMap.put(GD, resources.getString(R.string.country_name_gd));
            sNameMap.put(TC, resources.getString(R.string.country_name_tc));
            sNameMap.put(MS, resources.getString(R.string.country_name_ms));
            sNameMap.put(MP, resources.getString(R.string.country_name_mp));
            sNameMap.put(GU, resources.getString(R.string.country_name_gu));
            sNameMap.put(AS, resources.getString(R.string.country_name_as));
            sNameMap.put(SX, resources.getString(R.string.country_name_sx));
            sNameMap.put(LC, resources.getString(R.string.country_name_lc));
            sNameMap.put(DM, resources.getString(R.string.country_name_dm));
            sNameMap.put(VC, resources.getString(R.string.country_name_vc));
            sNameMap.put(PR, resources.getString(R.string.country_name_pr));
            sNameMap.put(DO, resources.getString(R.string.country_name_do));
            sNameMap.put(DO1, resources.getString(R.string.country_name_do));
            sNameMap.put(DO2, resources.getString(R.string.country_name_do));
            sNameMap.put(TT, resources.getString(R.string.country_name_tt));
            sNameMap.put(KN, resources.getString(R.string.country_name_kn));
            sNameMap.put(JM, resources.getString(R.string.country_name_jm));
            sNameMap.put(PR1, resources.getString(R.string.country_name_pr));
            sNameMap.put("1", resources.getString(R.string.country_name_us_ca));
            sNameMap.put("20", resources.getString(R.string.country_name_eg));
            sNameMap.put(MA, resources.getString(R.string.country_name_ma));
            sNameMap.put(DZ, resources.getString(R.string.country_name_dz));
            sNameMap.put(TN, resources.getString(R.string.country_name_tn));
            sNameMap.put(LY, resources.getString(R.string.country_name_ly));
            sNameMap.put(GM, resources.getString(R.string.country_name_gm));
            sNameMap.put(SN, resources.getString(R.string.country_name_sn));
            sNameMap.put(MR, resources.getString(R.string.country_name_mr));
            sNameMap.put(ML, resources.getString(R.string.country_name_ml));
            sNameMap.put(GN, resources.getString(R.string.country_name_gn));
            sNameMap.put(CI, resources.getString(R.string.country_name_ci));
            sNameMap.put(BF, resources.getString(R.string.country_name_bf));
            sNameMap.put(NE, resources.getString(R.string.country_name_ne));
            sNameMap.put(TG, resources.getString(R.string.country_name_tg));
            sNameMap.put(BJ, resources.getString(R.string.country_name_bj));
            sNameMap.put(MU, resources.getString(R.string.country_name_mu));
            sNameMap.put(LR, resources.getString(R.string.country_name_lr));
            sNameMap.put(SL, resources.getString(R.string.country_name_sl));
            sNameMap.put(GH, resources.getString(R.string.country_name_gh));
            sNameMap.put(NG, resources.getString(R.string.country_name_ng));
            sNameMap.put(TD, resources.getString(R.string.country_name_td));
            sNameMap.put(CF, resources.getString(R.string.country_name_cf));
            sNameMap.put(CM, resources.getString(R.string.country_name_cm));
            sNameMap.put(CV, resources.getString(R.string.country_name_cv));
            sNameMap.put(ST, resources.getString(R.string.country_name_st));
            sNameMap.put("240", resources.getString(R.string.country_name_gq));
            sNameMap.put(GA, resources.getString(R.string.country_name_ga));
            sNameMap.put(CG, resources.getString(R.string.country_name_cg));
            sNameMap.put(CD, resources.getString(R.string.country_name_cd));
            sNameMap.put(AO, resources.getString(R.string.country_name_ao));
            sNameMap.put(GW, resources.getString(R.string.country_name_gw));
            sNameMap.put(IO, resources.getString(R.string.country_name_io));
            sNameMap.put(AC, resources.getString(R.string.country_name_ac));
            sNameMap.put(SC, resources.getString(R.string.country_name_sc));
            sNameMap.put(SD, resources.getString(R.string.country_name_sd));
            sNameMap.put(RW, resources.getString(R.string.country_name_rw));
            sNameMap.put(ET, resources.getString(R.string.country_name_et));
            sNameMap.put(SO, resources.getString(R.string.country_name_so));
            sNameMap.put(DJ, resources.getString(R.string.country_name_dj));
            sNameMap.put(KE, resources.getString(R.string.country_name_ke));
            sNameMap.put(TZ, resources.getString(R.string.country_name_tz));
            sNameMap.put(UG, resources.getString(R.string.country_name_ug));
            sNameMap.put(BI, resources.getString(R.string.country_name_bi));
            sNameMap.put(MZ, resources.getString(R.string.country_name_mz));
            sNameMap.put(ZM, resources.getString(R.string.country_name_zm));
            sNameMap.put(MG, resources.getString(R.string.country_name_mg));
            sNameMap.put(RE_YT, resources.getString(R.string.country_name_re_yt));
            sNameMap.put(ZW, resources.getString(R.string.country_name_zw));
            sNameMap.put(NA, resources.getString(R.string.country_name_na));
            sNameMap.put(MW, resources.getString(R.string.country_name_mw));
            sNameMap.put(LS, resources.getString(R.string.country_name_ls));
            sNameMap.put(BW, resources.getString(R.string.country_name_bw));
            sNameMap.put(SZ, resources.getString(R.string.country_name_sz));
            sNameMap.put(KM, resources.getString(R.string.country_name_km));
            sNameMap.put(ZA, resources.getString(R.string.country_name_za));
            sNameMap.put(SH, resources.getString(R.string.country_name_sh));
            sNameMap.put(ER, resources.getString(R.string.country_name_er));
            sNameMap.put(AW, resources.getString(R.string.country_name_aw));
            sNameMap.put(FO, resources.getString(R.string.country_name_fo));
            sNameMap.put(GL, resources.getString(R.string.country_name_gl));
            sNameMap.put("30", resources.getString(R.string.country_name_gr));
            sNameMap.put(NL, resources.getString(R.string.country_name_nl));
            sNameMap.put(BE, resources.getString(R.string.country_name_be));
            sNameMap.put(FR, resources.getString(R.string.country_name_fr));
            sNameMap.put(ES, resources.getString(R.string.country_name_es));
            sNameMap.put(GI, resources.getString(R.string.country_name_gi));
            sNameMap.put(PT, resources.getString(R.string.country_name_pt));
            sNameMap.put(LU, resources.getString(R.string.country_name_lu));
            sNameMap.put(IE, resources.getString(R.string.country_name_ie));
            sNameMap.put(IS, resources.getString(R.string.country_name_is));
            sNameMap.put(AL, resources.getString(R.string.country_name_al));
            sNameMap.put(MT, resources.getString(R.string.country_name_mt));
            sNameMap.put(CY, resources.getString(R.string.country_name_cy));
            sNameMap.put(FI, resources.getString(R.string.country_name_fi));
            sNameMap.put(BG, resources.getString(R.string.country_name_bg));
            sNameMap.put(HU, resources.getString(R.string.country_name_hu));
            sNameMap.put(LT, resources.getString(R.string.country_name_lt));
            sNameMap.put(LV, resources.getString(R.string.country_name_lv));
            sNameMap.put(EE, resources.getString(R.string.country_name_ee));
            sNameMap.put(MD, resources.getString(R.string.country_name_md));
            sNameMap.put(AM, resources.getString(R.string.country_name_am));
            sNameMap.put(BY, resources.getString(R.string.country_name_by));
            sNameMap.put(AD, resources.getString(R.string.country_name_ad));
            sNameMap.put(MC, resources.getString(R.string.country_name_mc));
            sNameMap.put(SM, resources.getString(R.string.country_name_sm));
            sNameMap.put(UA, resources.getString(R.string.country_name_ua));
            sNameMap.put(RS, resources.getString(R.string.country_name_rs));
            sNameMap.put(ME, resources.getString(R.string.country_name_me));
            sNameMap.put(HR, resources.getString(R.string.country_name_hr));
            sNameMap.put(SI, resources.getString(R.string.country_name_si));
            sNameMap.put(BA, resources.getString(R.string.country_name_ba));
            sNameMap.put(MK, resources.getString(R.string.country_name_mk));
            sNameMap.put(VA, resources.getString(R.string.country_name_va));
            sNameMap.put(IT, resources.getString(R.string.country_name_it));
            sNameMap.put("40", resources.getString(R.string.country_name_ro));
            sNameMap.put(CH, resources.getString(R.string.country_name_ch));
            sNameMap.put(CZ, resources.getString(R.string.country_name_cz));
            sNameMap.put(SK, resources.getString(R.string.country_name_sk));
            sNameMap.put(LI, resources.getString(R.string.country_name_li));
            sNameMap.put(AT, resources.getString(R.string.country_name_at));
            sNameMap.put(GG, resources.getString(R.string.country_name_gg));
            sNameMap.put(JE, resources.getString(R.string.country_name_je));
            sNameMap.put(IM, resources.getString(R.string.country_name_im));
            sNameMap.put(JE1, resources.getString(R.string.country_name_je));
            sNameMap.put(IM1, resources.getString(R.string.country_name_im));
            sNameMap.put(IM2, resources.getString(R.string.country_name_im));
            sNameMap.put(JE2, resources.getString(R.string.country_name_je));
            sNameMap.put(GG1, resources.getString(R.string.country_name_gg));
            sNameMap.put(JE3, resources.getString(R.string.country_name_je));
            sNameMap.put(JE4, resources.getString(R.string.country_name_je));
            sNameMap.put(GG2, resources.getString(R.string.country_name_gg));
            sNameMap.put(GG3, resources.getString(R.string.country_name_gg));
            sNameMap.put(IM3, resources.getString(R.string.country_name_im));
            sNameMap.put(UK, resources.getString(R.string.country_name_uk));
            sNameMap.put(DK, resources.getString(R.string.country_name_dk));
            sNameMap.put(SE, resources.getString(R.string.country_name_se));
            sNameMap.put(NO, resources.getString(R.string.country_name_no));
            sNameMap.put(PL, resources.getString(R.string.country_name_pl));
            sNameMap.put(DE, resources.getString(R.string.country_name_de));
            sNameMap.put("500", resources.getString(R.string.country_name_fk));
            sNameMap.put(BZ, resources.getString(R.string.country_name_bz));
            sNameMap.put(GT, resources.getString(R.string.country_name_gt));
            sNameMap.put(SV, resources.getString(R.string.country_name_sv));
            sNameMap.put(HN, resources.getString(R.string.country_name_hn));
            sNameMap.put(NI, resources.getString(R.string.country_name_ni));
            sNameMap.put(CR, resources.getString(R.string.country_name_cr));
            sNameMap.put(PA, resources.getString(R.string.country_name_pa));
            sNameMap.put(PM, resources.getString(R.string.country_name_pm));
            sNameMap.put(HT, resources.getString(R.string.country_name_ht));
            sNameMap.put(PE, resources.getString(R.string.country_name_pe));
            sNameMap.put(MX, resources.getString(R.string.country_name_mx));
            sNameMap.put(CU, resources.getString(R.string.country_name_cu));
            sNameMap.put(AR, resources.getString(R.string.country_name_ar));
            sNameMap.put(BR, resources.getString(R.string.country_name_br));
            sNameMap.put(CL, resources.getString(R.string.country_name_cl));
            sNameMap.put(CO, resources.getString(R.string.country_name_co));
            sNameMap.put(VE, resources.getString(R.string.country_name_ve));
            sNameMap.put(GP_BL_MF, resources.getString(R.string.country_name_gp_bl_mf));
            sNameMap.put(BO, resources.getString(R.string.country_name_bo));
            sNameMap.put(GY, resources.getString(R.string.country_name_gy));
            sNameMap.put(EC, resources.getString(R.string.country_name_ec));
            sNameMap.put(GF, resources.getString(R.string.country_name_gf));
            sNameMap.put(PY, resources.getString(R.string.country_name_py));
            sNameMap.put(MQ, resources.getString(R.string.country_name_mq));
            sNameMap.put(SR, resources.getString(R.string.country_name_sr));
            sNameMap.put(UY, resources.getString(R.string.country_name_uy));
            sNameMap.put(AN, resources.getString(R.string.country_name_an));
            sNameMap.put("60", resources.getString(R.string.country_name_my));
            sNameMap.put(AU, resources.getString(R.string.country_name_au));
            sNameMap.put(ID, resources.getString(R.string.country_name_id));
            sNameMap.put(PH, resources.getString(R.string.country_name_ph));
            sNameMap.put(NZ, resources.getString(R.string.country_name_nz));
            sNameMap.put(SG, resources.getString(R.string.country_name_sg));
            sNameMap.put(TH, resources.getString(R.string.country_name_th));
            sNameMap.put(TL, resources.getString(R.string.country_name_tl));
            sNameMap.put(BN, resources.getString(R.string.country_name_bn));
            sNameMap.put(NR, resources.getString(R.string.country_name_nr));
            sNameMap.put(PG, resources.getString(R.string.country_name_pg));
            sNameMap.put(TO, resources.getString(R.string.country_name_to));
            sNameMap.put(SB, resources.getString(R.string.country_name_sb));
            sNameMap.put(VU, resources.getString(R.string.country_name_vu));
            sNameMap.put(FJ, resources.getString(R.string.country_name_fj));
            sNameMap.put(PW, resources.getString(R.string.country_name_pw));
            sNameMap.put(WF, resources.getString(R.string.country_name_wf));
            sNameMap.put(CK, resources.getString(R.string.country_name_ck));
            sNameMap.put(NU, resources.getString(R.string.country_name_nu));
            sNameMap.put(WS, resources.getString(R.string.country_name_ws));
            sNameMap.put(KI, resources.getString(R.string.country_name_ki));
            sNameMap.put(NC, resources.getString(R.string.country_name_nc));
            sNameMap.put(TV, resources.getString(R.string.country_name_tv));
            sNameMap.put(PF, resources.getString(R.string.country_name_pf));
            sNameMap.put(TK, resources.getString(R.string.country_name_tk));
            sNameMap.put(FM, resources.getString(R.string.country_name_fm));
            sNameMap.put(MH, resources.getString(R.string.country_name_mh));
            sNameMap.put("7", resources.getString(R.string.country_name_ru_kz));
            sNameMap.put(JP, resources.getString(R.string.country_name_jp));
            sNameMap.put(KR, resources.getString(R.string.country_name_kr));
            sNameMap.put(VN, resources.getString(R.string.country_name_vn));
            sNameMap.put(KP, resources.getString(R.string.country_name_kp));
            sNameMap.put(HK, resources.getString(R.string.country_name_hk));
            sNameMap.put(MO, resources.getString(R.string.country_name_mo));
            sNameMap.put(KH, resources.getString(R.string.country_name_kh));
            sNameMap.put(LA, resources.getString(R.string.country_name_la));
            sNameMap.put(CN, resources.getString(R.string.country_name_cn));
            sNameMap.put(BD, resources.getString(R.string.country_name_bd));
            sNameMap.put(TW, resources.getString(R.string.country_name_tw));
            sNameMap.put(TR, resources.getString(R.string.country_name_tr));
            sNameMap.put(IN, resources.getString(R.string.country_name_in));
            sNameMap.put(PK, resources.getString(R.string.country_name_pk));
            sNameMap.put(AF, resources.getString(R.string.country_name_af));
            sNameMap.put(LK, resources.getString(R.string.country_name_lk));
            sNameMap.put(MM, resources.getString(R.string.country_name_mm));
            sNameMap.put(MV, resources.getString(R.string.country_name_mv));
            sNameMap.put(LB, resources.getString(R.string.country_name_lb));
            sNameMap.put(JO, resources.getString(R.string.country_name_jo));
            sNameMap.put(SY, resources.getString(R.string.country_name_sy));
            sNameMap.put(IQ, resources.getString(R.string.country_name_iq));
            sNameMap.put(KW, resources.getString(R.string.country_name_kw));
            sNameMap.put(SA, resources.getString(R.string.country_name_sa));
            sNameMap.put(YE, resources.getString(R.string.country_name_ye));
            sNameMap.put(OM, resources.getString(R.string.country_name_om));
            sNameMap.put(PS, resources.getString(R.string.country_name_ps));
            sNameMap.put(AE, resources.getString(R.string.country_name_ae));
            sNameMap.put(IL, resources.getString(R.string.country_name_il));
            sNameMap.put(BH, resources.getString(R.string.country_name_bh));
            sNameMap.put(QA, resources.getString(R.string.country_name_qa));
            sNameMap.put(BT, resources.getString(R.string.country_name_bt));
            sNameMap.put(MN, resources.getString(R.string.country_name_mn));
            sNameMap.put(NP, resources.getString(R.string.country_name_np));
            sNameMap.put(IR, resources.getString(R.string.country_name_ir));
            sNameMap.put(TJ, resources.getString(R.string.country_name_tj));
            sNameMap.put(TM, resources.getString(R.string.country_name_tm));
            sNameMap.put(AZ, resources.getString(R.string.country_name_az));
            sNameMap.put(GE, resources.getString(R.string.country_name_ge));
            sNameMap.put(KG, resources.getString(R.string.country_name_kg));
            sNameMap.put(UZ, resources.getString(R.string.country_name_uz));
        }
    }

    public static boolean isChinaEnvironment(String str, String str2) {
        String str3 = CN;
        return str3.equals(str) || str3.equals(str2);
    }

    public static boolean isValidCountryCode(String str) {
        initNameMap();
        return sNameMap.containsKey(str);
    }

    public static String parse(StringBuffer stringBuffer, int i, int i2) {
        StringBuffer stringBuffer2 = stringBuffer;
        int i3 = i2;
        String str = "";
        if (i3 <= 0) {
            return str;
        }
        switch (stringBuffer.charAt(i)) {
            case '1':
                if (i3 > 3) {
                    char charAt = stringBuffer2.charAt(i + 2);
                    char charAt2 = stringBuffer2.charAt(i + 3);
                    switch (stringBuffer2.charAt(i + 1)) {
                        case '2':
                            if (charAt == '4') {
                                if (charAt2 == '2') {
                                    return BS;
                                }
                                if (charAt2 == '6') {
                                    return BB;
                                }
                            } else if (charAt == '6') {
                                if (charAt2 == '4') {
                                    return AI;
                                }
                                if (charAt2 == '8') {
                                    return AG;
                                }
                            } else if (charAt == '8' && charAt2 == '4') {
                                return VG;
                            }
                            break;
                        case '3':
                            if (charAt == '4') {
                                if (charAt2 == '0') {
                                    return VI;
                                }
                                if (charAt2 == '5') {
                                    return KY;
                                }
                            }
                            break;
                        case '4':
                            if (charAt == '4' && charAt2 == '1') {
                                return BM;
                            }
                            if (charAt == '7' && charAt2 == '3') {
                                return GD;
                            }
                            break;
                        case '6':
                            if (charAt == '4' && charAt2 == '9') {
                                return TC;
                            }
                            if (charAt == '6' && charAt2 == '4') {
                                return MS;
                            }
                            if (charAt == '7') {
                                if (charAt2 == '0') {
                                    return MP;
                                }
                                if (charAt2 == '1') {
                                    return GU;
                                }
                            }
                            if (charAt == '8' && charAt2 == '4') {
                                return AS;
                            }
                            break;
                        case '7':
                            if (charAt == '2' && charAt2 == '1') {
                                return SX;
                            }
                            if (charAt == '5' && charAt2 == '8') {
                                return LC;
                            }
                            if (charAt == '6' && charAt2 == '7') {
                                return DM;
                            }
                            if (charAt == '8') {
                                if (charAt2 == '4') {
                                    return VC;
                                }
                                if (charAt2 == '7') {
                                    return PR;
                                }
                            }
                            break;
                        case '8':
                            if (charAt == '0' && charAt2 == '9') {
                                return DO;
                            }
                            if (charAt == '2' && charAt2 == '9') {
                                return DO1;
                            }
                            if (charAt == '4' && charAt2 == '9') {
                                return DO2;
                            }
                            if (charAt == '6') {
                                if (charAt2 == '8') {
                                    return TT;
                                }
                                if (charAt2 == '9') {
                                    return KN;
                                }
                            }
                            if (charAt == '7' && charAt2 == '6') {
                                return JM;
                            }
                            break;
                        case '9':
                            if (charAt == '3' && charAt2 == '9') {
                                return PR1;
                            }
                    }
                }
                return "1";
            case '2':
                if (i3 > 1) {
                    switch (stringBuffer2.charAt(i + 1)) {
                        case '0':
                            return "20";
                        case '1':
                            if (i3 > 2) {
                                char charAt3 = stringBuffer2.charAt(i + 2);
                                if (charAt3 == '2') {
                                    return MA;
                                }
                                if (charAt3 == '3') {
                                    return DZ;
                                }
                                if (charAt3 == '6') {
                                    return TN;
                                }
                                if (charAt3 == '8') {
                                    return LY;
                                }
                            }
                            break;
                        case '2':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return GM;
                                    case '1':
                                        return SN;
                                    case '2':
                                        return MR;
                                    case '3':
                                        return ML;
                                    case '4':
                                        return GN;
                                    case '5':
                                        return CI;
                                    case '6':
                                        return BF;
                                    case '7':
                                        return NE;
                                    case '8':
                                        return TG;
                                    case '9':
                                        return BJ;
                                }
                            }
                            break;
                        case '3':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return MU;
                                    case '1':
                                        return LR;
                                    case '2':
                                        return SL;
                                    case '3':
                                        return GH;
                                    case '4':
                                        return NG;
                                    case '5':
                                        return TD;
                                    case '6':
                                        return CF;
                                    case '7':
                                        return CM;
                                    case '8':
                                        return CV;
                                    case '9':
                                        return ST;
                                }
                            }
                            break;
                        case '4':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return "240";
                                    case '1':
                                        return GA;
                                    case '2':
                                        return CG;
                                    case '3':
                                        return CD;
                                    case '4':
                                        return AO;
                                    case '5':
                                        return GW;
                                    case '6':
                                        return IO;
                                    case '7':
                                        return AC;
                                    case '8':
                                        return SC;
                                    case '9':
                                        return SD;
                                }
                            }
                            break;
                        case '5':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return RW;
                                    case '1':
                                        return ET;
                                    case '2':
                                        return SO;
                                    case '3':
                                        return DJ;
                                    case '4':
                                        return KE;
                                    case '5':
                                        return TZ;
                                    case '6':
                                        return UG;
                                    case '7':
                                        return BI;
                                    case '8':
                                        return MZ;
                                }
                            }
                            break;
                        case '6':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return ZM;
                                    case '1':
                                        return MG;
                                    case '2':
                                        return RE_YT;
                                    case '3':
                                        return ZW;
                                    case '4':
                                        return NA;
                                    case '5':
                                        return MW;
                                    case '6':
                                        return LS;
                                    case '7':
                                        return BW;
                                    case '8':
                                        return SZ;
                                    case '9':
                                        return KM;
                                }
                            }
                            break;
                        case '7':
                            return ZA;
                        case '9':
                            if (i3 > 2) {
                                char charAt4 = stringBuffer2.charAt(i + 2);
                                if (charAt4 == '0') {
                                    return SH;
                                }
                                if (charAt4 == '1') {
                                    return ER;
                                }
                                switch (charAt4) {
                                    case '7':
                                        return AW;
                                    case '8':
                                        return FO;
                                    case '9':
                                        return GL;
                                }
                            }
                            break;
                    }
                }
                break;
            case '3':
                if (i3 > 1) {
                    switch (stringBuffer2.charAt(i + 1)) {
                        case '0':
                            return "30";
                        case '1':
                            return NL;
                        case '2':
                            return BE;
                        case '3':
                            return FR;
                        case '4':
                            return ES;
                        case '5':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return GI;
                                    case '1':
                                        return PT;
                                    case '2':
                                        return LU;
                                    case '3':
                                        return IE;
                                    case '4':
                                        return IS;
                                    case '5':
                                        return AL;
                                    case '6':
                                        return MT;
                                    case '7':
                                        return CY;
                                    case '8':
                                        return FI;
                                    case '9':
                                        return BG;
                                }
                            }
                            break;
                        case '6':
                            return HU;
                        case '7':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return LT;
                                    case '1':
                                        return LV;
                                    case '2':
                                        return EE;
                                    case '3':
                                        return MD;
                                    case '4':
                                        return AM;
                                    case '5':
                                        return BY;
                                    case '6':
                                        return AD;
                                    case '7':
                                        return MC;
                                    case '8':
                                        return SM;
                                }
                            }
                            break;
                        case '8':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return UA;
                                    case '1':
                                        return RS;
                                    case '2':
                                        return ME;
                                    case '5':
                                        return HR;
                                    case '6':
                                        return SI;
                                    case '7':
                                        return BA;
                                    case '9':
                                        return MK;
                                }
                            }
                            break;
                        case '9':
                            return (i3 > 6 && stringBuffer2.charAt(i + 2) == '0' && stringBuffer2.charAt(i + 3) == '6' && stringBuffer2.charAt(i + 4) == '6' && stringBuffer2.charAt(i + 5) == '9' && stringBuffer2.charAt(i + 6) == '8') ? VA : IT;
                    }
                }
                break;
            case '4':
                if (i3 > 1) {
                    switch (stringBuffer2.charAt(i + 1)) {
                        case '0':
                            return "40";
                        case '1':
                            return CH;
                        case '2':
                            if (i3 > 2) {
                                char charAt5 = stringBuffer2.charAt(i + 2);
                                if (charAt5 == '0') {
                                    return CZ;
                                }
                                if (charAt5 == '1') {
                                    return SK;
                                }
                                if (charAt5 == '3') {
                                    return LI;
                                }
                            }
                            break;
                        case '3':
                            return AT;
                        case '4':
                            if (i3 > 5) {
                                char charAt6 = stringBuffer2.charAt(i + 3);
                                char charAt7 = stringBuffer2.charAt(i + 4);
                                char charAt8 = stringBuffer2.charAt(i + 5);
                                char charAt9 = stringBuffer2.charAt(i + 2);
                                if (charAt9 != '1') {
                                    if (charAt9 == '7') {
                                        if (charAt6 == '5' && charAt7 == '0' && charAt8 == '9') {
                                            return JE1;
                                        }
                                        if (charAt6 == '5' && charAt7 == '2' && charAt8 == '4') {
                                            return IM1;
                                        }
                                        if (charAt6 == '6' && charAt7 == '2' && charAt8 == '4') {
                                            return IM2;
                                        }
                                        if (charAt6 == '7' && charAt7 == '0' && charAt8 == '0') {
                                            return JE2;
                                        }
                                        if (charAt6 == '7' && charAt7 == '8' && charAt8 == '1') {
                                            return GG1;
                                        }
                                        if (charAt6 == '7' && charAt7 == '9' && charAt8 == '7') {
                                            return JE3;
                                        }
                                        if (charAt6 == '8' && charAt7 == '2' && charAt8 == '9') {
                                            return JE4;
                                        }
                                        if (charAt6 == '8' && charAt7 == '3' && charAt8 == '9') {
                                            return GG2;
                                        }
                                        if (charAt6 == '9' && charAt7 == '1' && charAt8 == '1') {
                                            return GG3;
                                        }
                                        if (charAt6 == '9' && charAt7 == '2' && charAt8 == '4') {
                                            return IM3;
                                        }
                                    }
                                } else if (charAt6 == '4' && charAt7 == '8' && charAt8 == '1') {
                                    return GG;
                                } else {
                                    if (charAt6 == '5' && charAt7 == '3' && charAt8 == '4') {
                                        return JE;
                                    }
                                    if (charAt6 == '6' && charAt7 == '2' && charAt8 == '4') {
                                        return IM;
                                    }
                                }
                            }
                            return UK;
                        case '5':
                            return DK;
                        case '6':
                            return SE;
                        case '7':
                            return NO;
                        case '8':
                            return PL;
                        case '9':
                            return DE;
                    }
                }
                break;
            case '5':
                if (i3 > 1) {
                    switch (stringBuffer2.charAt(i + 1)) {
                        case '0':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return "500";
                                    case '1':
                                        return BZ;
                                    case '2':
                                        return GT;
                                    case '3':
                                        return SV;
                                    case '4':
                                        return HN;
                                    case '5':
                                        return NI;
                                    case '6':
                                        return CR;
                                    case '7':
                                        return PA;
                                    case '8':
                                        return PM;
                                    case '9':
                                        return HT;
                                }
                            }
                            break;
                        case '1':
                            return PE;
                        case '2':
                            return MX;
                        case '3':
                            return CU;
                        case '4':
                            return AR;
                        case '5':
                            return BR;
                        case '6':
                            return CL;
                        case '7':
                            return CO;
                        case '8':
                            return VE;
                        case '9':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return GP_BL_MF;
                                    case '1':
                                        return BO;
                                    case '2':
                                        return GY;
                                    case '3':
                                        return EC;
                                    case '4':
                                        return GF;
                                    case '5':
                                        return PY;
                                    case '6':
                                        return MQ;
                                    case '7':
                                        return SR;
                                    case '8':
                                        return UY;
                                    case '9':
                                        return AN;
                                }
                            }
                            break;
                    }
                }
                break;
            case '6':
                if (i3 > 1) {
                    switch (stringBuffer2.charAt(i + 1)) {
                        case '0':
                            return "60";
                        case '1':
                            return AU;
                        case '2':
                            return ID;
                        case '3':
                            return PH;
                        case '4':
                            return NZ;
                        case '5':
                            return SG;
                        case '6':
                            return TH;
                        case '7':
                            if (i3 > 2) {
                                char charAt10 = stringBuffer2.charAt(i + 2);
                                if (charAt10 == '0') {
                                    return TL;
                                }
                                switch (charAt10) {
                                    case '3':
                                        return BN;
                                    case '4':
                                        return NR;
                                    case '5':
                                        return PG;
                                    case '6':
                                        return TO;
                                    case '7':
                                        return SB;
                                    case '8':
                                        return VU;
                                    case '9':
                                        return FJ;
                                }
                            }
                            break;
                        case '8':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return PW;
                                    case '1':
                                        return WF;
                                    case '2':
                                        return CK;
                                    case '3':
                                        return NU;
                                    case '5':
                                        return WS;
                                    case '6':
                                        return KI;
                                    case '7':
                                        return NC;
                                    case '8':
                                        return TV;
                                    case '9':
                                        return PF;
                                }
                            }
                            break;
                        case '9':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return TK;
                                    case '1':
                                        return FM;
                                    case '2':
                                        return MH;
                                }
                            }
                            break;
                    }
                }
                break;
            case '7':
                return "7";
            case '8':
                if (i3 > 1) {
                    switch (stringBuffer2.charAt(i + 1)) {
                        case '1':
                            return JP;
                        case '2':
                            return KR;
                        case '4':
                            return VN;
                        case '5':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return KP;
                                    case '2':
                                        return HK;
                                    case '3':
                                        return MO;
                                    case '5':
                                        return KH;
                                    case '6':
                                        return LA;
                                }
                            }
                            break;
                        case '6':
                            return CN;
                        case '8':
                            if (i3 > 2) {
                                char charAt11 = stringBuffer2.charAt(i + 2);
                                if (charAt11 == '0') {
                                    return BD;
                                }
                                if (charAt11 == '6') {
                                    return TW;
                                }
                            }
                            break;
                    }
                }
                break;
            case '9':
                if (i3 > 1) {
                    switch (stringBuffer2.charAt(i + 1)) {
                        case '0':
                            return TR;
                        case '1':
                            return IN;
                        case '2':
                            return PK;
                        case '3':
                            return AF;
                        case '4':
                            return LK;
                        case '5':
                            return MM;
                        case '6':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return MV;
                                    case '1':
                                        return LB;
                                    case '2':
                                        return JO;
                                    case '3':
                                        return SY;
                                    case '4':
                                        return IQ;
                                    case '5':
                                        return KW;
                                    case '6':
                                        return SA;
                                    case '7':
                                        return YE;
                                    case '8':
                                        return OM;
                                }
                            }
                            break;
                        case '7':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '0':
                                        return PS;
                                    case '1':
                                        return AE;
                                    case '2':
                                        return IL;
                                    case '3':
                                        return BH;
                                    case '4':
                                        return QA;
                                    case '5':
                                        return BT;
                                    case '6':
                                        return MN;
                                    case '7':
                                        return NP;
                                }
                            }
                            break;
                        case '8':
                            return IR;
                        case '9':
                            if (i3 > 2) {
                                switch (stringBuffer2.charAt(i + 2)) {
                                    case '2':
                                        return TJ;
                                    case '3':
                                        return TM;
                                    case '4':
                                        return AZ;
                                    case '5':
                                        return GE;
                                    case '6':
                                        return KG;
                                    case '8':
                                        return UZ;
                                }
                            }
                            break;
                    }
                }
                break;
        }
        return str;
    }

    public static String toName(String str) {
        initNameMap();
        String str2 = (String) sNameMap.get(str);
        return str2 == null ? "" : str2;
    }
}

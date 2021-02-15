package miui.util;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.miui.internal.util.DirectIndexedFileExtractor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import miui.util.DirectIndexedFile.Reader;

public class AlmanacConsPros {
    private static final String[] DATA = {"立碑", "祭祀", "起基", "移徙", "开仓", "破屋", "作厕", "合帐", "入学", "交易", "造仓", "挂匾", "破土", "合脊", "启攒", "斋醮", "教牛马", "捕捉", "安葬", "余事勿取", "安香", "安门", "上梁", "合寿木", "行丧", "订盟", "经络", "结网", "造桥", "安床", "取渔", "沐浴", "移柩", "造畜椆栖", "酬神", "进人口", "开生坟", "栽种", "作梁", "普渡", "掘井", "谢土", "立券", "割蜜", "乘船", "入宅", "分居", "架马", "归宁", "安碓磑", "雕刻", "塑绘", "祈福", "开光", "断蚁", "出火", "入殓", "造屋", "修饰垣墙", "造车器", "出行", "裁衣", "整手足甲", "平治道涂", "造船", "归岫", "纳婿", "问名", "雇庸", "冠笄", "开柱眼", "修门", "理发", "习艺", "纳畜", "开渠", "置产", "纳财", "安机械", "出货财", "拆卸", "纳采", "修坟", "扫舍", "求嗣", "放水", "补垣", "探病", "求医", "针灸", "竖柱", "成服", "开池", "伐木", "作灶", "词讼", "赴任", "坏垣", "塞穴", "筑堤", "会亲友", "牧养", "诸事不宜", "造庙", "解除", "除服", "畋猎", "治病", "定磉", "开市", "动土", "嫁娶", "修造"};
    private static final String DATA_ASSET_NAME = "huangli.idf";
    private static final Long END_TIME = Long.valueOf(new GregorianCalendar(2100, 11, 31).getTimeInMillis());
    private static final Long START_TIME = Long.valueOf(new GregorianCalendar(1901, 0, 1).getTimeInMillis());
    private static final String TAG = "AlmanacConsPros";
    private static AlmanacConsPros sSingleton;
    private Reader mReader;

    private AlmanacConsPros() {
        initResource();
    }

    public static synchronized AlmanacConsPros getInstance() {
        AlmanacConsPros almanacConsPros;
        synchronized (AlmanacConsPros.class) {
            if (sSingleton == null) {
                sSingleton = new AlmanacConsPros();
            }
            almanacConsPros = sSingleton;
        }
        return almanacConsPros;
    }

    private void initResource() {
        Application currentApplication = AppConstants.getCurrentApplication();
        String str = DATA_ASSET_NAME;
        String directIndexedFilePath = DirectIndexedFileExtractor.getDirectIndexedFilePath(currentApplication, str);
        if (directIndexedFilePath != null && new File(directIndexedFilePath).exists()) {
            try {
                this.mReader = DirectIndexedFile.open(directIndexedFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.mReader == null) {
            try {
                this.mReader = DirectIndexedFile.open(AppConstants.getCurrentApplication().getResources().getAssets().open(str, 1));
            } catch (IOException unused) {
                Log.w(TAG, "Can't read huangli.idf, NO huangli supported!");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Reader reader = this.mReader;
        if (reader != null) {
            reader.close();
        }
        super.finalize();
    }

    public String[] getConsPros(long j) {
        String[] strArr = null;
        if (this.mReader == null) {
            return null;
        }
        if (j >= START_TIME.longValue() && j <= END_TIME.longValue()) {
            strArr = new String[2];
            String[] split = ((String) this.mReader.get(0, (int) ((j - START_TIME.longValue()) / 86400000), 0)).split(",", -1);
            ArrayList arrayList = new ArrayList();
            if (split.length == 2) {
                for (int i = 0; i < split.length; i++) {
                    arrayList.clear();
                    if (!TextUtils.isEmpty(split[i])) {
                        for (String parseInt : split[i].split("、")) {
                            int parseInt2 = Integer.parseInt(parseInt);
                            String[] strArr2 = DATA;
                            if (parseInt2 < strArr2.length) {
                                arrayList.add(strArr2[parseInt2]);
                            }
                        }
                    }
                    strArr[i] = TextUtils.join(" ", arrayList);
                }
            }
        }
        return strArr;
    }
}

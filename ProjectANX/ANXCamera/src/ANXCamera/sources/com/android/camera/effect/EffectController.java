package com.android.camera.effect;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.EffectChangedListenerController;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.effect.renders.BeautificationWrapperRender;
import com.android.camera.effect.renders.CinematicRender;
import com.android.camera.effect.renders.FocusPeakingRender;
import com.android.camera.effect.renders.GaussianMaskEffectRender;
import com.android.camera.effect.renders.GradienterEffectRender;
import com.android.camera.effect.renders.GradienterSnapshotEffectRender;
import com.android.camera.effect.renders.KaleidoscopeRender;
import com.android.camera.effect.renders.LightEffectRender;
import com.android.camera.effect.renders.NoneEffectRender;
import com.android.camera.effect.renders.PipeRenderPair;
import com.android.camera.effect.renders.Render;
import com.android.camera.effect.renders.RenderGroup;
import com.android.camera.effect.renders.RgbToYuvRender;
import com.android.camera.effect.renders.TiltShiftMaskEffectRender;
import com.android.camera.effect.renders.WrapperRender;
import com.android.camera.effect.renders.XBlurEffectRender;
import com.android.camera.effect.renders.XGaussianEffectRender;
import com.android.camera.effect.renders.XTiltShiftEffectRender;
import com.android.camera.effect.renders.YBlurEffectRender;
import com.android.camera.effect.renders.YGaussianEffectRender;
import com.android.camera.effect.renders.YTiltShiftEffectRender;
import com.android.camera.effect.renders.Yuv444ToRgbRender;
import com.android.camera.effect.renders.YuvToRgbRender;
import com.android.camera.effect.renders.ZebraRender;
import com.android.camera.fragment.beauty.BeautyParameters;
import com.android.camera.fragment.beauty.LiveBeautyFilterFragment.LiveFilterItem;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.gallery3d.ui.GLCanvas;
import com.miui.filtersdk.filter.NewBeautificationFilter;
import com.miui.filtersdk.filter.base.ColorLookupFilter;
import com.miui.filtersdk.filter.helper.FilterCategory;
import com.miui.filtersdk.filter.helper.FilterFactory;
import com.miui.filtersdk.filter.helper.FilterType;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EffectController {
    private static final int BLUR_ALPHA_FRAME_NUM = 8;
    public static final int COLOR_RETENTION_FILTER_ID = 200;
    public static final int[] EFFECT_ALL_CHANGE_TYPES = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int EFFECT_CHANGE_BEAUTY = 3;
    public static final int EFFECT_CHANGE_CINEMATIC = 9;
    public static final int EFFECT_CHANGE_EXPOSURE_FEEDBACK = 7;
    public static final int EFFECT_CHANGE_FILTER = 1;
    public static final int EFFECT_CHANGE_FOCUS_PEAK = 4;
    public static final int EFFECT_CHANGE_GRADIENTER = 6;
    public static final int EFFECT_CHANGE_KALEIDOSCOPE = 8;
    public static final int EFFECT_CHANGE_STICKER = 2;
    public static final int EFFECT_CHANGE_TILT = 5;
    private static final int MAX_BLUR_ALPHA = 212;
    private static final String TAG = "EffectController";
    private static EffectController sInstance;
    private int mAiColorCorrectionVersion;
    private boolean mBeautyEnable = false;
    private boolean mBeautyFrameReady = false;
    private boolean mBlur = false;
    private int mBlurStep = -1;
    private List mChangedListeners = new ArrayList();
    private String mCurrentSticker;
    private float mDeviceRotation;
    private boolean mDrawCinematic;
    private boolean mDrawExposure;
    private boolean mDrawGradienter;
    private String mDrawKaleidoscope = "0";
    private boolean mDrawPeaking;
    private boolean mDrawTilt;
    private int mEffectId = FilterInfo.FILTER_ID_NONE;
    private EffectRectAttribute mEffectRectAttribute = new EffectRectAttribute((AnonymousClass1) null);
    private SparseArray mFilterInfoMap;
    private boolean mIsDrawMainFrame = true;
    private boolean mIsIndiaColorLookupTableAvailable;
    private List mLiveFilters;
    private Object mLock = new Object();
    private boolean mNeedDestroyMakeup = false;
    private int mOrientation;
    private int mOverrideAiEffectIndex = -1;
    private int mOverrideEffectIndex = -1;
    private float mTiltShiftMaskAlpha;

    /* renamed from: com.android.camera.effect.EffectController$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$miui$filtersdk$filter$helper$FilterType = new int[FilterType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(170:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|78|79|80|81|82|83|84|85|86|87|88|89|90|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|108|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|(3:169|170|172)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(172:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|78|79|80|81|82|83|84|85|86|87|88|89|90|91|92|93|94|95|96|97|98|99|100|101|102|103|104|105|106|107|108|109|110|111|112|113|114|115|116|117|118|119|120|121|122|123|124|125|126|127|128|129|130|131|132|133|134|135|136|137|138|139|140|141|142|143|144|145|146|147|148|149|150|151|152|153|154|155|156|157|158|159|160|161|162|163|164|165|166|167|168|169|170|172) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:101:0x025a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:103:0x0266 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:105:0x0272 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:107:0x027e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:109:0x028a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:111:0x0296 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:113:0x02a2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:115:0x02ae */
        /* JADX WARNING: Missing exception handler attribute for start block: B:117:0x02ba */
        /* JADX WARNING: Missing exception handler attribute for start block: B:119:0x02c6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:121:0x02d2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:123:0x02de */
        /* JADX WARNING: Missing exception handler attribute for start block: B:125:0x02ea */
        /* JADX WARNING: Missing exception handler attribute for start block: B:127:0x02f6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:129:0x0302 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:131:0x030e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:133:0x031a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:135:0x0326 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:137:0x0332 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:139:0x033e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:141:0x034a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:143:0x0356 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:145:0x0362 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:147:0x036e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:149:0x037a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:151:0x0386 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:153:0x0392 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:155:0x039e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:157:0x03aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:159:0x03b6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:161:0x03c2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:163:0x03ce */
        /* JADX WARNING: Missing exception handler attribute for start block: B:165:0x03da */
        /* JADX WARNING: Missing exception handler attribute for start block: B:167:0x03e6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:169:0x03f2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x007a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0086 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0092 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x009e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00b6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00c2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00ce */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00da */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00e6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00f2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00fe */
        /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x010a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:47:0x0116 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x0122 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x012e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x013a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x0146 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x0152 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:59:0x015e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:61:0x016a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:63:0x0176 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:65:0x0182 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:67:0x018e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:69:0x019a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:71:0x01a6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:73:0x01b2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:75:0x01be */
        /* JADX WARNING: Missing exception handler attribute for start block: B:77:0x01ca */
        /* JADX WARNING: Missing exception handler attribute for start block: B:79:0x01d6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:81:0x01e2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:83:0x01ee */
        /* JADX WARNING: Missing exception handler attribute for start block: B:85:0x01fa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:87:0x0206 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:89:0x0212 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:91:0x021e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:93:0x022a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:95:0x0236 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:97:0x0242 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:99:0x024e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_NATURE.ordinal()] = 1;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_JAPANESE.ordinal()] = 2;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_PINK.ordinal()] = 3;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_STORY.ordinal()] = 4;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_FAIRYTALE.ordinal()] = 5;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_MAZE.ordinal()] = 6;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_RIDDLE.ordinal()] = 7;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_MOVIE.ordinal()] = 8;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_M_TEA.ordinal()] = 9;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_M_LILT.ordinal()] = 10;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_M_SEPIA.ordinal()] = 11;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.B_M_WHITEANDBLACK.ordinal()] = 12;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_SUNNY.ordinal()] = 13;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_PINK.ordinal()] = 14;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_MEMORY.ordinal()] = 15;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_STRONG.ordinal()] = 16;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_WARM.ordinal()] = 17;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_RETRO.ordinal()] = 18;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_ROMANTIC.ordinal()] = 19;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_M_DUSK.ordinal()] = 20;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_M_LILT.ordinal()] = 21;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_M_TEA.ordinal()] = 22;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_M_SEPIA.ordinal()] = 23;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.BI_M_WHITEANDBLACK.ordinal()] = 24;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_NATURE.ordinal()] = 25;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_STAGE.ordinal()] = 26;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_MOVIE.ordinal()] = 27;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_RAINBOW.ordinal()] = 28;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_SHUTTER.ordinal()] = 29;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_DOT.ordinal()] = 30;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_LEAF.ordinal()] = 31;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_HOLI.ordinal()] = 32;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_NEON.ordinal()] = 33;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_PHANTOM.ordinal()] = 34;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_NOSTALGIA.ordinal()] = 35;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_RAINBOW.ordinal()] = 36;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_LANSHAN.ordinal()] = 37;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_DAZZLING.ordinal()] = 38;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_GORGEOUS.ordinal()] = 39;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_BRIGHT_RED.ordinal()] = 40;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.L_2_DREAMLAND.ordinal()] = 41;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_LIVELY.ordinal()] = 42;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_BLACKGOLD.ordinal()] = 43;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_ORANGE.ordinal()] = 44;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_DELICACY.ordinal()] = 45;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_FILM.ordinal()] = 46;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_KOIZORA.ordinal()] = 47;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_MOVIE.ordinal()] = 48;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_SODA.ordinal()] = 49;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_JAPANESE.ordinal()] = 50;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_NATURE.ordinal()] = 51;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_PINK.ordinal()] = 52;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_FAIRYTALE.ordinal()] = 53;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_LILT.ordinal()] = 54;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_TRAVEL.ordinal()] = 55;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_FIRST.ordinal()] = 56;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_SIBOPENK.ordinal()] = 57;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_BLACKICE.ordinal()] = 58;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_WHITEANDBLACK.ordinal()] = 59;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.N_CLASSIC.ordinal()] = 60;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.S_FILM.ordinal()] = 61;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.S_YEARS.ordinal()] = 62;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.S_POLAROID.ordinal()] = 63;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.S_FOREST.ordinal()] = 64;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.S_BYGONE.ordinal()] = 65;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.S_WHITEANDBLACK.ordinal()] = 66;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.ML_FAIR.ordinal()] = 67;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.ML_MOUSSE.ordinal()] = 68;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.ML_SOLAR.ordinal()] = 69;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.ML_BLUE.ordinal()] = 70;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.ML_CONTRAST.ordinal()] = 71;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.ML_YEARS.ordinal()] = 72;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.ML_DEEPBLACK.ordinal()] = 73;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_BLACKGOLD.ordinal()] = 74;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_ORANGE.ordinal()] = 75;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_SUMMER_DAY.ordinal()] = 76;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_FANTASY.ordinal()] = 77;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_MEET.ordinal()] = 78;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_WIND_SING.ordinal()] = 79;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_LOST.ordinal()] = 80;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_CENTRAL.ordinal()] = 81;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_NORTHERN_EUROPE.ordinal()] = 82;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_CYBERPINK.ordinal()] = 83;
            $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_BLACKICE.ordinal()] = 84;
            try {
                $SwitchMap$com$miui$filtersdk$filter$helper$FilterType[FilterType.V_ROME.ordinal()] = 85;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EffectChangeType {
    }

    public interface EffectChangedListener {
        void onEffectChanged(int... iArr);

        void onRealtimePreviewFilterChanged(int i);
    }

    public class EffectRectAttribute {
        public int mInvertFlag;
        public PointF mPoint1;
        public PointF mPoint2;
        public float mRangeWidth;
        public RectF mRectF;

        private EffectRectAttribute() {
            this.mRectF = new RectF();
            this.mPoint1 = new PointF();
            this.mPoint2 = new PointF();
        }

        /* synthetic */ EffectRectAttribute(AnonymousClass1 r1) {
            this();
        }

        private EffectRectAttribute(EffectRectAttribute effectRectAttribute) {
            this.mRectF = new RectF();
            this.mPoint1 = new PointF();
            this.mPoint2 = new PointF();
            this.mRectF.set(effectRectAttribute.mRectF);
            this.mPoint1.set(effectRectAttribute.mPoint1);
            this.mPoint2.set(effectRectAttribute.mPoint2);
            this.mInvertFlag = effectRectAttribute.mInvertFlag;
            this.mRangeWidth = effectRectAttribute.mRangeWidth;
        }

        /* synthetic */ EffectRectAttribute(EffectRectAttribute effectRectAttribute, AnonymousClass1 r2) {
            this(effectRectAttribute);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("mRectF=");
            sb.append(this.mRectF);
            sb.append(" mPoint1=");
            sb.append(this.mPoint1);
            sb.append(" mPoint2=");
            sb.append(this.mPoint2);
            sb.append(" mInvertFlag=");
            sb.append(this.mInvertFlag);
            sb.append(" mRangeWidth=");
            sb.append(this.mRangeWidth);
            return sb.toString();
        }
    }

    private EffectController() {
        initialize();
    }

    private FilterCategory convertToFilterCategory(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 5 ? i != 6 ? i != 8 ? FilterCategory.UNKNOWN : FilterCategory.MI_LIVE : FilterCategory.LIGHTING : FilterCategory.AI : FilterCategory.STICKER : FilterCategory.BEAUTY : FilterCategory.NORMAL;
    }

    public static int createAiSceneEffectId(FilterType filterType) {
        return FilterInfo.getId(5, filterType.ordinal());
    }

    private RenderGroup getAiSceneRenderNew(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, boolean z2, int i) {
        if (z) {
            getRenderByCategory(gLCanvas, renderGroup, 5, z2);
            return renderGroup;
        }
        getRenderById(gLCanvas, renderGroup, z2, i);
        return renderGroup;
    }

    private RenderGroup getBeautyRender(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, int i) {
        if (z) {
            getRenderByCategory(gLCanvas, renderGroup, 2, false);
            return renderGroup;
        }
        getRenderById(gLCanvas, renderGroup, false, i);
        return renderGroup;
    }

    private int getDegree(FilterType filterType) {
        return 100;
    }

    public static synchronized EffectController getInstance() {
        EffectController effectController;
        synchronized (EffectController.class) {
            if (sInstance == null) {
                sInstance = new EffectController();
            }
            effectController = sInstance;
        }
        return effectController;
    }

    private RenderGroup getLightingRenderNew(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, boolean z2, int i) {
        if (z) {
            getRenderByCategory(gLCanvas, renderGroup, 6, z2);
            return renderGroup;
        }
        getRenderById(gLCanvas, renderGroup, z2, i);
        return renderGroup;
    }

    private RenderGroup getMakeupRender(GLCanvas gLCanvas, RenderGroup renderGroup) {
        NewBeautificationFilter newBeautificationFilter = new NewBeautificationFilter();
        ArcsoftBeautyProcessor arcsoftBeautyProcessor = new ArcsoftBeautyProcessor();
        arcsoftBeautyProcessor.setRotation(CameraSettings.isFrontCamera() ? 270 : 90);
        BeautyParameters.getInstance().setMakeupProcessor(arcsoftBeautyProcessor);
        newBeautificationFilter.setBeautyProcessor(arcsoftBeautyProcessor);
        newBeautificationFilter.initBeautyProcessor(Display.getWindowWidth(), Display.getWindowHeight());
        renderGroup.addRender(new BeautificationWrapperRender(gLCanvas, FilterInfo.RENDER_ID_MAKEUP, newBeautificationFilter, CameraSettings.isFrontCamera()));
        return renderGroup;
    }

    private RenderGroup getMiLiveRender(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, int i) {
        if (z) {
            getRenderByCategory(gLCanvas, renderGroup, 8, false);
            return renderGroup;
        }
        getRenderById(gLCanvas, renderGroup, false, i);
        return renderGroup;
    }

    private RenderGroup getNormalRenderNew(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, boolean z2, int i) {
        if (z) {
            getRenderByCategory(gLCanvas, renderGroup, 1, z2);
            return renderGroup;
        }
        getRenderById(gLCanvas, renderGroup, z2, i);
        return renderGroup;
    }

    private RenderGroup getPrivateRender(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, boolean z2, int i) {
        boolean z3;
        Render gaussianMaskEffectRender;
        Render tiltShiftMaskEffectRender;
        Render yBlurEffectRender;
        GLCanvas gLCanvas2 = gLCanvas;
        RenderGroup renderGroup2 = renderGroup;
        int i2 = i;
        boolean z4 = true;
        if (renderGroup2.getRender(FilterInfo.FILTER_ID_BLUR) != null || (!z && i2 != FilterInfo.FILTER_ID_BLUR && i2 >= 0)) {
            z3 = false;
        } else {
            if (z || i2 == FilterInfo.FILTER_ID_BLUR || renderGroup2.isPartComplete(2)) {
                boolean z5 = renderGroup2.getPartRender(0) != null && (renderGroup2.getPartRender(0) instanceof XBlurEffectRender);
                boolean z6 = renderGroup2.getPartRender(1) != null && (renderGroup2.getPartRender(1) instanceof YBlurEffectRender);
                PipeRenderPair pipeRenderPair = new PipeRenderPair(gLCanvas, FilterInfo.FILTER_ID_BLUR, z5 ? renderGroup2.getPartRender(0) : new XBlurEffectRender(gLCanvas2), z6 ? renderGroup2.getPartRender(1) : new YBlurEffectRender(gLCanvas2), false);
                renderGroup2.addRender(pipeRenderPair);
                if (z5 || z6) {
                    renderGroup.clearPartRenders();
                }
            } else {
                if (renderGroup2.getPartRender(0) == null) {
                    yBlurEffectRender = new XBlurEffectRender(gLCanvas2);
                } else if (renderGroup2.getPartRender(1) == null) {
                    yBlurEffectRender = new YBlurEffectRender(gLCanvas2);
                }
                renderGroup2.addPartRender(yBlurEffectRender);
            }
            z3 = true;
        }
        if (renderGroup2.getRender(FilterInfo.FILTER_ID_GRADIENTER) == null && (z || i2 == FilterInfo.FILTER_ID_GRADIENTER || (i2 < 0 && !z3))) {
            renderGroup2.addRender(z2 ? new GradienterSnapshotEffectRender(gLCanvas2, FilterInfo.FILTER_ID_GRADIENTER) : new GradienterEffectRender(gLCanvas2, FilterInfo.FILTER_ID_GRADIENTER));
            z3 = true;
        }
        if (renderGroup2.getRender(FilterInfo.FILTER_ID_CINEMATIC) == null && (z || i2 == FilterInfo.FILTER_ID_CINEMATIC || (i2 < 0 && !z3))) {
            renderGroup2.addRender(new CinematicRender(gLCanvas2, FilterInfo.FILTER_ID_CINEMATIC));
            z3 = true;
        }
        if (renderGroup2.getRender(FilterInfo.FILTER_ID_TILTSHIFT) == null && C0124O00000oO.Oo0() && (z || i2 == FilterInfo.FILTER_ID_TILTSHIFT || (i2 < 0 && !z3))) {
            if (z || i2 == FilterInfo.FILTER_ID_TILTSHIFT || renderGroup2.isPartComplete(3)) {
                PipeRenderPair pipeRenderPair2 = new PipeRenderPair(gLCanvas, FilterInfo.FILTER_ID_TILTSHIFT, new PipeRenderPair(gLCanvas2, renderGroup2.getPartRender(0) != null ? renderGroup2.getPartRender(0) : new XTiltShiftEffectRender(gLCanvas2), renderGroup2.getPartRender(1) != null ? renderGroup2.getPartRender(1) : new YTiltShiftEffectRender(gLCanvas2), false), renderGroup2.getPartRender(2) != null ? renderGroup2.getPartRender(2) : new TiltShiftMaskEffectRender(gLCanvas2), false);
                renderGroup2.addRender(pipeRenderPair2);
                renderGroup.clearPartRenders();
            } else {
                if (renderGroup2.getPartRender(0) == null) {
                    tiltShiftMaskEffectRender = new XTiltShiftEffectRender(gLCanvas2);
                } else if (renderGroup2.getPartRender(1) == null) {
                    tiltShiftMaskEffectRender = new YTiltShiftEffectRender(gLCanvas2);
                } else if (renderGroup2.getPartRender(2) == null) {
                    tiltShiftMaskEffectRender = new TiltShiftMaskEffectRender(gLCanvas2);
                }
                renderGroup2.addPartRender(tiltShiftMaskEffectRender);
            }
            z3 = true;
        }
        if (C0124O00000oO.Oo000o() || renderGroup2.getRender(FilterInfo.FILTER_ID_GAUSSIAN) != null || (!z && i2 != FilterInfo.FILTER_ID_GAUSSIAN && (i2 >= 0 || z3))) {
            z4 = z3;
        } else if (z || i2 == FilterInfo.FILTER_ID_GAUSSIAN || renderGroup2.isPartComplete(3)) {
            PipeRenderPair pipeRenderPair3 = new PipeRenderPair(gLCanvas, FilterInfo.FILTER_ID_GAUSSIAN, new PipeRenderPair(gLCanvas2, renderGroup2.getPartRender(0) != null ? renderGroup2.getPartRender(0) : new XGaussianEffectRender(gLCanvas2), renderGroup2.getPartRender(1) != null ? renderGroup2.getPartRender(1) : new YGaussianEffectRender(gLCanvas2), false), renderGroup2.getPartRender(2) != null ? renderGroup2.getPartRender(2) : new GaussianMaskEffectRender(gLCanvas2), false);
            renderGroup2.addRender(pipeRenderPair3);
            renderGroup.clearPartRenders();
        } else {
            if (renderGroup2.getPartRender(0) == null) {
                gaussianMaskEffectRender = new XGaussianEffectRender(gLCanvas2);
            } else if (renderGroup2.getPartRender(1) == null) {
                gaussianMaskEffectRender = new YGaussianEffectRender(gLCanvas2);
            } else if (renderGroup2.getPartRender(2) == null) {
                gaussianMaskEffectRender = new GaussianMaskEffectRender(gLCanvas2);
            }
            renderGroup2.addPartRender(gaussianMaskEffectRender);
        }
        if (renderGroup2.getRender(FilterInfo.FILTER_ID_PEAKINGMF) == null && C0124O00000oO.Oo00oO0() && !z2 && (z || i2 == FilterInfo.FILTER_ID_PEAKINGMF || (i2 < 0 && !z4))) {
            renderGroup2.addRender(new FocusPeakingRender(gLCanvas2, FilterInfo.FILTER_ID_PEAKINGMF));
        }
        if (renderGroup2.getRender(FilterInfo.FILTER_ID_KALEIDOSCOPE) == null && (z || i2 == FilterInfo.FILTER_ID_KALEIDOSCOPE)) {
            renderGroup2.addRender(new KaleidoscopeRender(gLCanvas2, FilterInfo.FILTER_ID_KALEIDOSCOPE, getCurrentKaleidoscope()));
        }
        if (renderGroup2.getRender(FilterInfo.FILTER_ID_ZEBRA) == null && !z2 && (z || i2 == FilterInfo.FILTER_ID_ZEBRA || (i2 < 0 && !z4))) {
            renderGroup2.addRender(new ZebraRender(gLCanvas2, FilterInfo.FILTER_ID_ZEBRA));
        }
        if ((z || i2 == FilterInfo.FILTER_ID_YUV2RGB || (i2 < 0 && !z4)) && renderGroup2.getRender(FilterInfo.FILTER_ID_YUV2RGB) == null) {
            renderGroup2.addRender(new YuvToRgbRender(gLCanvas2, i2));
        }
        if ((z || i2 == FilterInfo.FILTER_ID_YUV4442RGB || (i2 < 0 && !z4)) && renderGroup2.getRender(FilterInfo.FILTER_ID_YUV4442RGB) == null) {
            renderGroup2.addRender(new Yuv444ToRgbRender(gLCanvas2, i2));
        }
        if ((z || i2 == FilterInfo.FILTER_ID_RGB2YUV || (i2 < 0 && !z4)) && renderGroup2.getRender(FilterInfo.FILTER_ID_RGB2YUV) == null) {
            renderGroup2.addRender(new RgbToYuvRender(gLCanvas2, i2));
        }
        return renderGroup2;
    }

    private RenderGroup getRenderByCategory(GLCanvas gLCanvas, RenderGroup renderGroup, int i, boolean z) {
        if (convertToFilterCategory(i) == FilterCategory.UNKNOWN) {
            return renderGroup;
        }
        ArrayList filterInfo = getFilterInfo(i);
        if (filterInfo != null) {
            Iterator it = filterInfo.iterator();
            while (it.hasNext()) {
                getRenderById(gLCanvas, renderGroup, z, ((FilterInfo) it.next()).getId());
            }
        }
        return renderGroup;
    }

    /* JADX WARNING: type inference failed for: r7v3, types: [com.android.camera.effect.renders.Render] */
    /* JADX WARNING: type inference failed for: r7v4, types: [com.android.camera.effect.renders.WrapperRender] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private RenderGroup getRenderById(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, int i) {
        PipeRenderPair pipeRenderPair;
        NoneEffectRender noneEffectRender;
        if (i < 0) {
            return renderGroup;
        }
        int i2 = FilterInfo.FILTER_ID_NONE;
        NoneEffectRender noneEffectRender2 = null;
        if (i != i2 || !renderGroup.isNeedInit(i2)) {
            int i3 = FilterInfo.AI_SCENE_FILTER_ID_0_NONE;
            if (i != i3 || !renderGroup.isNeedInit(i3)) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("getRenderById: id = ");
                sb.append(i);
                Log.d(str, sb.toString());
                if (renderGroup.getRender(i) == null) {
                    int index = FilterInfo.getIndex(i);
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("getRenderById: index = ");
                    sb2.append(index);
                    Log.d(str2, sb2.toString());
                    if (index > -1 && index < FilterType.values().length) {
                        FilterType filterType = FilterType.values()[index];
                        if (filterType.getFilterCategory() == FilterCategory.AI) {
                            int i4 = this.mAiColorCorrectionVersion;
                            if (i4 >= 1 && !filterType.isAiCCLookupTableSupported(i4)) {
                                String str3 = TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("getRenderById: ");
                                sb3.append(filterType);
                                sb3.append(" does not support light color correction, reset to NONE");
                                Log.d(str3, sb3.toString());
                                renderGroup.removeRender(i);
                                if (renderGroup.isNeedInit(FilterInfo.AI_SCENE_FILTER_ID_0_NONE)) {
                                    if (z) {
                                        noneEffectRender = new NoneEffectRender(gLCanvas, FilterInfo.AI_SCENE_FILTER_ID_0_NONE);
                                    }
                                    renderGroup.addRender(noneEffectRender);
                                }
                                return renderGroup;
                            }
                        }
                        ColorLookupFilter filter = FilterFactory.getFilter(filterType, this.mIsIndiaColorLookupTableAvailable, this.mAiColorCorrectionVersion);
                        if (filter != null) {
                            filter.setDegree(getDegree(filterType));
                        }
                        if (index < FilterType.L_2_NEON.ordinal() || index > FilterType.L_2_DREAMLAND.ordinal()) {
                            pipeRenderPair = new WrapperRender(gLCanvas, i, filter);
                        } else {
                            PipeRenderPair pipeRenderPair2 = new PipeRenderPair(gLCanvas, i, new WrapperRender(gLCanvas, i, filter), new LightEffectRender(gLCanvas, index), false);
                            pipeRenderPair = pipeRenderPair2;
                        }
                        renderGroup.addRender(pipeRenderPair);
                    }
                }
                return renderGroup;
            }
            if (z) {
                noneEffectRender2 = new NoneEffectRender(gLCanvas, FilterInfo.AI_SCENE_FILTER_ID_0_NONE);
            }
            renderGroup.addRender(noneEffectRender2);
            return renderGroup;
        }
        if (z) {
            noneEffectRender2 = new NoneEffectRender(gLCanvas, FilterInfo.FILTER_ID_NONE);
        }
        renderGroup.addRender(noneEffectRender2);
        return renderGroup;
    }

    private RenderGroup getStickerRender(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, int i) {
        if (z) {
            getRenderByCategory(gLCanvas, renderGroup, 3, false);
            return renderGroup;
        }
        getRenderById(gLCanvas, renderGroup, false, i);
        return renderGroup;
    }

    private ArrayList initAiSceneFilterInfo() {
        ArrayList arrayList = new ArrayList();
        FilterType[] filtersByCategory = FilterFactory.getFiltersByCategory(FilterCategory.AI);
        arrayList.add(new FilterInfo(FilterInfo.AI_SCENE_FILTER_ID_0_NONE, 0));
        int i = 1;
        for (FilterType filterType : filtersByCategory) {
            if (filterType != FilterType.A_COMMON) {
                int i2 = i + 1;
                arrayList.add(new FilterInfo(createAiSceneEffectId(filterType), i));
                i = i2;
            }
        }
        return arrayList;
    }

    private ArrayList initAppVideoFilterInfo() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_NONE, R.string.pref_camera_coloreffect_entry_none, R.drawable.video_filter_image_none, 0));
        FilterType[] filtersByCategory = FilterFactory.getFiltersByCategory(FilterCategory.VIDEO);
        int length = filtersByCategory.length;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i6 < length) {
            FilterType filterType = filtersByCategory[i6];
            switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$filter$helper$FilterType[filterType.ordinal()]) {
                case 74:
                    i3 = 7;
                    i5 = R.string.color_effect_entry_blackgold;
                    i4 = R.drawable.video_filter_blackgold;
                    break;
                case 75:
                    i3 = 8;
                    i5 = R.string.color_effect_entry_orange;
                    i4 = R.drawable.video_filter_orange;
                    break;
                case 76:
                    i3 = 10;
                    i5 = R.string.video_effect_entry_summer_day;
                    i4 = R.drawable.video_filter_summer_day;
                    break;
                case 77:
                    i3 = 20;
                    i5 = R.string.video_effect_entry_fantasy;
                    i4 = R.drawable.video_filter_fantasy;
                    break;
                case 78:
                    i3 = 30;
                    i5 = R.string.video_effect_entry_meet;
                    i4 = R.drawable.video_filter_meet;
                    break;
                case 79:
                    i3 = 40;
                    i5 = R.string.video_effect_entry_wind_sing;
                    i4 = R.drawable.video_filter_wind_sing;
                    break;
                case 80:
                    i3 = 50;
                    i5 = R.string.video_effect_entry_lost;
                    i4 = R.drawable.video_filter_lost;
                    break;
                case 81:
                    i3 = 60;
                    i5 = R.string.video_effect_entry_central;
                    i4 = R.drawable.video_filter_central;
                    break;
                case 82:
                    i3 = 70;
                    i5 = R.string.video_effect_entry_northern_europe;
                    i4 = R.drawable.video_filter_northern_europe;
                    break;
                case 83:
                    i3 = 71;
                    i5 = R.string.color_effect_entry_sibopenk;
                    i4 = R.drawable.video_filter_cyberpink;
                    break;
                case 84:
                    i3 = 72;
                    i5 = R.string.color_effect_entry_blackice;
                    i4 = R.drawable.video_filter_blackice;
                    break;
                case 85:
                    i3 = 80;
                    i5 = R.string.video_effect_entry_rome;
                    i4 = R.drawable.video_filter_rome;
                    break;
                default:
                    i2 = i7;
                    i = i8;
                    i3 = i9;
                    break;
            }
            i2 = i5;
            i = i4;
            if (i2 == 0 || i == 0) {
                i8 = i;
            } else {
                FilterInfo filterInfo = new FilterInfo(8, filterType.ordinal(), i2, i, i3);
                arrayList.add(filterInfo);
                i8 = 0;
                i2 = 0;
            }
            i6++;
            i9 = i3;
            i7 = i2;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static ArrayList initBeautyFilterInfo() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_NONE, R.string.pref_camera_coloreffect_entry_none, R.drawable.portait_effect_image_none, 0));
        FilterType[] filtersByCategory = FilterFactory.getFiltersByCategory(FilterCategory.BEAUTY);
        int length = filtersByCategory.length;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i6 < length) {
            FilterType filterType = filtersByCategory[i6];
            switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$filter$helper$FilterType[filterType.ordinal()]) {
                case 1:
                    i3 = 10;
                    i5 = R.string.portait_effect_entry_nature;
                    i4 = R.drawable.portait_effect_image_nature;
                    break;
                case 2:
                    i3 = 20;
                    i5 = R.string.portait_effect_entry_japanese;
                    i4 = R.drawable.portait_effect_image_japanese;
                    break;
                case 3:
                    i3 = 30;
                    i5 = R.string.portait_effect_entry_pink;
                    i4 = R.drawable.portait_effect_image_pink;
                    break;
                case 4:
                    i3 = 40;
                    i5 = R.string.portait_effect_entry_story;
                    i4 = R.drawable.portait_effect_image_story;
                    break;
                case 5:
                    i3 = 50;
                    i5 = R.string.portait_effect_entry_fairytale;
                    i4 = R.drawable.portait_effect_image_fairytale;
                    break;
                case 6:
                    i3 = 80;
                    i5 = R.string.portait_effect_entry_maze;
                    i4 = R.drawable.portait_effect_image_maze;
                    break;
                case 7:
                    i3 = 100;
                    i5 = R.string.portait_effect_entry_riddle;
                    i4 = R.drawable.portait_effect_image_riddle;
                    break;
                case 8:
                    i3 = 110;
                    i5 = R.string.portait_effect_entry_movie;
                    i4 = R.drawable.portait_effect_image_movie;
                    break;
                case 9:
                    i3 = 120;
                    i5 = R.string.portait_effect_entry_tea;
                    i4 = R.drawable.portait_effect_image_m_tea;
                    break;
                case 10:
                    i3 = 130;
                    i5 = R.string.portait_effect_entry_lilt;
                    i4 = R.drawable.portait_effect_image_m_lilt;
                    break;
                case 11:
                    i3 = m.aD;
                    i5 = R.string.portait_effect_entry_sepia;
                    i4 = R.drawable.portait_effect_image_m_sepia;
                    break;
                case 12:
                    i3 = 150;
                    i5 = R.string.portait_effect_entry_blackwhite;
                    i4 = R.drawable.portait_effect_image_m_blackwhite;
                    break;
                default:
                    i2 = i7;
                    i = i8;
                    i3 = i9;
                    break;
            }
            i2 = i5;
            i = i4;
            if (i2 == 0 || i == 0) {
                i8 = i;
            } else {
                FilterInfo filterInfo = new FilterInfo(2, filterType.ordinal(), i2, i, i3);
                arrayList.add(filterInfo);
                i8 = 0;
                i2 = 0;
            }
            i6++;
            i9 = i3;
            i7 = i2;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static ArrayList initIndiaBeautyFilterInfo() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_NONE, R.string.pref_camera_coloreffect_entry_none, R.drawable.portrait_india_effect_image_none, 0));
        FilterType[] filtersByCategory = FilterFactory.getFiltersByCategory(FilterCategory.BEAUTY_INDIA);
        int length = filtersByCategory.length;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i6 < length) {
            FilterType filterType = filtersByCategory[i6];
            switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$filter$helper$FilterType[filterType.ordinal()]) {
                case 13:
                    i3 = 10;
                    i5 = R.string.portrait_india_effect_entry_sunny;
                    i4 = R.drawable.portrait_india_effect_image_sunny;
                    break;
                case 14:
                    i3 = 20;
                    i5 = R.string.portrait_india_effect_entry_pink;
                    i4 = R.drawable.portrait_india_effect_image_pink;
                    break;
                case 15:
                    i3 = 30;
                    i5 = R.string.portrait_india_effect_entry_memory;
                    i4 = R.drawable.portrait_india_effect_image_memory;
                    break;
                case 16:
                    i3 = 40;
                    i5 = R.string.portrait_india_effect_entry_strong;
                    i4 = R.drawable.portrait_india_effect_image_strong;
                    break;
                case 17:
                    i3 = 50;
                    i5 = R.string.portrait_india_effect_entry_warm;
                    i4 = R.drawable.portrait_india_effect_image_warm;
                    break;
                case 18:
                    i3 = 80;
                    i5 = R.string.portrait_india_effect_entry_retro;
                    i4 = R.drawable.portrait_india_effect_image_retro;
                    break;
                case 19:
                    i3 = 100;
                    i5 = R.string.portrait_india_effect_entry_romantic;
                    i4 = R.drawable.portrait_india_effect_image_romantic;
                    break;
                case 20:
                    i3 = 120;
                    i5 = R.string.portait_effect_entry_dusk;
                    i4 = R.drawable.portrait_india_effect_image_m_dusk;
                    break;
                case 21:
                    i3 = 130;
                    i5 = R.string.portait_effect_entry_lilt;
                    i4 = R.drawable.portrait_india_effect_image_m_lilt;
                    break;
                case 22:
                    i3 = m.aD;
                    i5 = R.string.portait_effect_entry_tea;
                    i4 = R.drawable.portrait_india_effect_image_m_tea;
                    break;
                case 23:
                    i3 = 150;
                    i5 = R.string.portait_effect_entry_sepia;
                    i4 = R.drawable.portrait_india_effect_image_m_sepia;
                    break;
                case 24:
                    i3 = 160;
                    i5 = R.string.portait_effect_entry_blackwhite;
                    i4 = R.drawable.portrait_india_effect_image_m_blackwhite;
                    break;
                default:
                    i2 = i7;
                    i = i8;
                    i3 = i9;
                    break;
            }
            i2 = i5;
            i = i4;
            if (i2 == 0 || i == 0) {
                i8 = i;
            } else {
                FilterInfo filterInfo = new FilterInfo(2, filterType.ordinal(), i2, i, i3);
                arrayList.add(filterInfo);
                i8 = 0;
                i2 = 0;
            }
            i6++;
            i9 = i3;
            i7 = i2;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private ArrayList initLightingFilterInfo() {
        ArrayList arrayList = new ArrayList();
        FilterType[] filtersByCategory = FilterFactory.getFiltersByCategory(FilterCategory.LIGHTING);
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_NONE, 0));
        String str = "0";
        for (FilterType filterType : filtersByCategory) {
            switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$filter$helper$FilterType[filterType.ordinal()]) {
                case 25:
                    str = "1";
                    break;
                case 26:
                    str = "2";
                    break;
                case 27:
                    str = "3";
                    break;
                case 28:
                    str = "4";
                    break;
                case 29:
                    str = "5";
                    break;
                case 30:
                    str = "6";
                    break;
                case 31:
                    str = "7";
                    break;
                case 32:
                    str = "8";
                    break;
                case 33:
                    str = "9";
                    break;
                case 34:
                    str = "10";
                    break;
                case 35:
                    str = "11";
                    break;
                case 36:
                    str = "12";
                    break;
                case 37:
                    str = "13";
                    break;
                case 38:
                    str = "14";
                    break;
                case 39:
                    str = "15";
                    break;
                case 40:
                    str = "16";
                    break;
                case 41:
                    str = "17";
                    break;
            }
            arrayList.add(new FilterInfo(FilterInfo.getId(6, filterType.ordinal()), Integer.valueOf(str).intValue()));
        }
        return arrayList;
    }

    private ArrayList initMiLiveFilterInfo() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_NONE, R.string.color_effect_live_none, R.drawable.color_effect_live_none, 0));
        FilterType[] filtersByCategory = FilterFactory.getFiltersByCategory(FilterCategory.MI_LIVE);
        int length = filtersByCategory.length;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i6 < length) {
            FilterType filterType = filtersByCategory[i6];
            switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$filter$helper$FilterType[filterType.ordinal()]) {
                case 67:
                    i3 = 10;
                    i5 = R.string.color_effect_live_fair;
                    i4 = R.drawable.color_effect_live_fair;
                    break;
                case 68:
                    i3 = 20;
                    i5 = R.string.color_effect_live_mousse;
                    i4 = R.drawable.color_effect_live_mousse;
                    break;
                case 69:
                    i3 = 30;
                    i5 = R.string.color_effect_live_solar;
                    i4 = R.drawable.color_effect_live_solar;
                    break;
                case 70:
                    i3 = 40;
                    i5 = R.string.color_effect_live_blue;
                    i4 = R.drawable.color_effect_live_blue;
                    break;
                case 71:
                    i3 = 50;
                    i5 = R.string.color_effect_live_contrast;
                    i4 = R.drawable.color_effect_live_contrast;
                    break;
                case 72:
                    i3 = 60;
                    i5 = R.string.color_effect_live_years;
                    i4 = R.drawable.color_effect_live_years;
                    break;
                case 73:
                    i3 = 70;
                    i5 = R.string.color_effect_live_deepblack;
                    i4 = R.drawable.color_effect_live_deepblack;
                    break;
                default:
                    i2 = i7;
                    i = i8;
                    i3 = i9;
                    break;
            }
            i2 = i5;
            i = i4;
            if (i2 == 0 || i == 0) {
                i8 = i;
            } else {
                FilterInfo filterInfo = new FilterInfo(8, filterType.ordinal(), i2, i, i3);
                arrayList.add(filterInfo);
                i8 = 0;
                i2 = 0;
            }
            i6++;
            i9 = i3;
            i7 = i2;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private ArrayList initNormalFilterInfoNew() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_NONE, R.string.pref_camera_coloreffect_entry_none, R.drawable.color_effect_image_none, 0));
        FilterType[] filtersByCategory = FilterFactory.getFiltersByCategory(FilterCategory.NORMAL);
        int length = filtersByCategory.length;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i6 < length) {
            FilterType filterType = filtersByCategory[i6];
            switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$filter$helper$FilterType[filterType.ordinal()]) {
                case 42:
                    i3 = 10;
                    i5 = R.string.color_effect_entry_vivid;
                    i4 = R.drawable.color_effect_image_vivid;
                    break;
                case 43:
                    i3 = 20;
                    i5 = R.string.color_effect_entry_blackgold;
                    i4 = R.drawable.color_effect_image_blackgold;
                    break;
                case 44:
                    i3 = 30;
                    i5 = R.string.color_effect_entry_orange;
                    i4 = R.drawable.color_effect_image_orange;
                    break;
                case 45:
                    i3 = 40;
                    i5 = R.string.color_effect_entry_delicacy;
                    i4 = R.drawable.color_effect_image_delicacy;
                    break;
                case 46:
                    i3 = 50;
                    i5 = R.string.color_effect_entry_film;
                    i4 = R.drawable.color_effect_image_film;
                    break;
                case 47:
                    i3 = 60;
                    i5 = R.string.color_effect_entry_koizora;
                    i4 = R.drawable.color_effect_image_koizora;
                    break;
                case 48:
                    i3 = 70;
                    i5 = R.string.portait_effect_entry_movie;
                    i4 = R.drawable.color_effect_image_movie;
                    break;
                case 49:
                    i3 = 80;
                    i5 = R.string.color_effect_entry_soda;
                    i4 = R.drawable.color_effect_image_soda;
                    break;
                case 50:
                    i3 = 90;
                    i5 = R.string.portait_effect_entry_japanese;
                    i4 = R.drawable.color_effect_image_japanese;
                    break;
                case 51:
                    i3 = 100;
                    i5 = R.string.portait_effect_entry_nature;
                    i4 = R.drawable.color_effect_image_nature;
                    break;
                case 52:
                    i3 = 110;
                    i5 = R.string.portait_effect_entry_pink;
                    i4 = R.drawable.color_effect_image_pink;
                    break;
                case 53:
                    i3 = 120;
                    i5 = R.string.portait_effect_entry_fairytale;
                    i4 = R.drawable.color_effect_image_fairytale;
                    break;
                case 54:
                    i3 = 130;
                    i5 = R.string.portait_effect_entry_lilt;
                    i4 = R.drawable.color_effect_image_lilt;
                    break;
                case 55:
                    i3 = m.aD;
                    i5 = R.string.color_effect_entry_travel;
                    i4 = R.drawable.color_effect_image_travel;
                    break;
                case 56:
                    i3 = 150;
                    i5 = R.string.color_effect_entry_first;
                    i4 = R.drawable.color_effect_image_first;
                    break;
                case 57:
                    i3 = 160;
                    i5 = R.string.color_effect_entry_sibopenk;
                    i4 = R.drawable.color_effect_image_sibopenk;
                    break;
                case 58:
                    i3 = 170;
                    i5 = R.string.color_effect_entry_blackice;
                    i4 = R.drawable.color_effect_image_blackice;
                    break;
                case 59:
                    i3 = 180;
                    i5 = R.string.color_effect_entry_blackwhite;
                    i4 = R.drawable.color_effect_image_blackwhite;
                    break;
                case 60:
                    i3 = m.bp;
                    i5 = R.string.color_effect_entry_classic;
                    i4 = R.drawable.color_effect_image_classic;
                    break;
                default:
                    i2 = i7;
                    i = i8;
                    i3 = i9;
                    break;
            }
            i2 = i5;
            i = i4;
            if (i2 == 0 || i == 0) {
                i8 = i;
            } else {
                FilterInfo filterInfo = new FilterInfo(1, filterType.ordinal(), i2, i, i3);
                arrayList.add(filterInfo);
                i8 = 0;
                i2 = 0;
            }
            i6++;
            i9 = i3;
            i7 = i2;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private ArrayList initPrivateFilterInfo() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_BLUR, 0));
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_GRADIENTER, 1));
        FilterInfo filterInfo = new FilterInfo(FilterInfo.FILTER_ID_TILTSHIFT, 2);
        filterInfo.setNeedRect(true);
        arrayList.add(filterInfo);
        FilterInfo filterInfo2 = new FilterInfo(FilterInfo.FILTER_ID_GAUSSIAN, 3);
        filterInfo2.setNeedRect(true);
        arrayList.add(filterInfo2);
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_PEAKINGMF, 4));
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_ZEBRA, 5));
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_CINEMATIC, 6));
        return arrayList;
    }

    private ArrayList initStickerFilterInfo() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new FilterInfo(FilterInfo.FILTER_ID_NONE, R.string.pref_camera_coloreffect_entry_none, R.drawable.video_effect_image_none, 0));
        FilterType[] filtersByCategory = FilterFactory.getFiltersByCategory(FilterCategory.STICKER);
        int length = filtersByCategory.length;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i6 < length) {
            FilterType filterType = filtersByCategory[i6];
            switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$filter$helper$FilterType[filterType.ordinal()]) {
                case 61:
                    i3 = 10;
                    i5 = R.string.video_effect_entry_film;
                    i4 = R.drawable.video_effect_image_film;
                    break;
                case 62:
                    i3 = 20;
                    i5 = R.string.video_effect_entry_holiday;
                    i4 = R.drawable.video_effect_image_holiday;
                    break;
                case 63:
                    i3 = 30;
                    i5 = R.string.video_effect_entry_polaroid;
                    i4 = R.drawable.video_effect_image_polaroid;
                    break;
                case 64:
                    i3 = 40;
                    i5 = R.string.video_effect_entry_forest;
                    i4 = R.drawable.video_effect_image_forest;
                    break;
                case 65:
                    i3 = 45;
                    i5 = R.string.video_effect_entry_bygone;
                    i4 = R.drawable.video_effect_image_bygone;
                    break;
                case 66:
                    i3 = 50;
                    i5 = R.string.video_effect_entry_blackwhite;
                    i4 = R.drawable.video_effect_image_blackwhite;
                    break;
                default:
                    i2 = i7;
                    i = i8;
                    i3 = i9;
                    break;
            }
            i2 = i5;
            i = i4;
            if (i2 == 0 || i == 0) {
                i8 = i;
            } else {
                FilterInfo filterInfo = new FilterInfo(3, filterType.ordinal(), i2, i, i3);
                arrayList.add(filterInfo);
                i8 = 0;
                i2 = 0;
            }
            i6++;
            i9 = i3;
            i7 = i2;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private ArrayList initVideoFilterInfo() {
        ArrayList arrayList = new ArrayList();
        FilterInfo filterInfo = new FilterInfo(7, 0, R.string.pref_camera_coloreffect_entry_none, R.drawable.video_filter_image_none, 0);
        filterInfo.setTagUniqueFilterId(0);
        arrayList.add(filterInfo);
        FilterInfo filterInfo2 = new FilterInfo(7, 5, R.string.video_effect_entry_color_retention, R.drawable.video_filter_color_retention, 5);
        filterInfo2.setTagUniqueFilterId(200);
        arrayList.add(filterInfo2);
        FilterInfo filterInfo3 = new FilterInfo(7, 6, R.string.color_effect_entry_blackgold, R.drawable.video_filter_blackgold, 6);
        filterInfo3.setTagUniqueFilterId(110);
        arrayList.add(filterInfo3);
        FilterInfo filterInfo4 = new FilterInfo(7, 7, R.string.color_effect_entry_orange, R.drawable.video_filter_orange, 7);
        filterInfo4.setTagUniqueFilterId(111);
        arrayList.add(filterInfo4);
        FilterInfo filterInfo5 = new FilterInfo(7, 10, R.string.video_effect_entry_summer_day, R.drawable.video_filter_summer_day, 10);
        filterInfo5.setTagUniqueFilterId(102);
        arrayList.add(filterInfo5);
        FilterInfo filterInfo6 = new FilterInfo(7, 20, R.string.video_effect_entry_fantasy, R.drawable.video_filter_fantasy, 20);
        filterInfo6.setTagUniqueFilterId(103);
        arrayList.add(filterInfo6);
        FilterInfo filterInfo7 = new FilterInfo(7, 30, R.string.video_effect_entry_meet, R.drawable.video_filter_meet, 30);
        filterInfo7.setTagUniqueFilterId(104);
        arrayList.add(filterInfo7);
        FilterInfo filterInfo8 = new FilterInfo(7, 40, R.string.video_effect_entry_wind_sing, R.drawable.video_filter_wind_sing, 40);
        filterInfo8.setTagUniqueFilterId(105);
        arrayList.add(filterInfo8);
        FilterInfo filterInfo9 = new FilterInfo(7, 50, R.string.video_effect_entry_lost, R.drawable.video_filter_lost, 50);
        filterInfo9.setTagUniqueFilterId(106);
        arrayList.add(filterInfo9);
        FilterInfo filterInfo10 = new FilterInfo(7, 60, R.string.video_effect_entry_central, R.drawable.video_filter_central, 60);
        filterInfo10.setTagUniqueFilterId(107);
        arrayList.add(filterInfo10);
        FilterInfo filterInfo11 = new FilterInfo(7, 70, R.string.video_effect_entry_northern_europe, R.drawable.video_filter_northern_europe, 70);
        filterInfo11.setTagUniqueFilterId(108);
        arrayList.add(filterInfo11);
        FilterInfo filterInfo12 = new FilterInfo(7, 71, R.string.color_effect_entry_sibopenk, R.drawable.video_filter_cyberpink, 71);
        filterInfo12.setTagUniqueFilterId(112);
        arrayList.add(filterInfo12);
        FilterInfo filterInfo13 = new FilterInfo(7, 72, R.string.color_effect_entry_blackice, R.drawable.video_filter_blackice, 72);
        filterInfo13.setTagUniqueFilterId(113);
        arrayList.add(filterInfo13);
        FilterInfo filterInfo14 = new FilterInfo(7, 80, R.string.video_effect_entry_rome, R.drawable.video_filter_rome, 80);
        filterInfo14.setTagUniqueFilterId(109);
        arrayList.add(filterInfo14);
        Collections.sort(arrayList);
        return arrayList;
    }

    private ArrayList initVideoMasterFilterInfo() {
        ArrayList arrayList = new ArrayList();
        FilterInfo filterInfo = new FilterInfo(7, 0, R.string.pref_camera_coloreffect_entry_none, R.drawable.video_master_filter_image_none, 0);
        filterInfo.setTagUniqueFilterId(0);
        arrayList.add(filterInfo);
        FilterInfo filterInfo2 = new FilterInfo(7, 5, R.string.video_effect_entry_color_retention, R.drawable.video_master_filter_color_retention, 5);
        filterInfo2.setTagUniqueFilterId(200);
        arrayList.add(filterInfo2);
        FilterInfo filterInfo3 = new FilterInfo(7, 10, R.string.color_effect_entry_new_bbp, R.drawable.video_master_filter_bbp, 10);
        filterInfo3.setTagUniqueFilterId(121);
        arrayList.add(filterInfo3);
        FilterInfo filterInfo4 = new FilterInfo(7, 15, R.string.color_effect_entry_new_1, R.drawable.video_master_filter_wind_sing, 15);
        filterInfo4.setTagUniqueFilterId(122);
        arrayList.add(filterInfo4);
        FilterInfo filterInfo5 = new FilterInfo(7, 20, R.string.color_effect_entry_orange, R.drawable.video_master_filter_orange, 20);
        filterInfo5.setTagUniqueFilterId(123);
        arrayList.add(filterInfo5);
        FilterInfo filterInfo6 = new FilterInfo(7, 30, R.string.color_effect_entry_new_2, R.drawable.video_master_filter_fantasy, 30);
        filterInfo6.setTagUniqueFilterId(125);
        arrayList.add(filterInfo6);
        FilterInfo filterInfo7 = new FilterInfo(7, 50, R.string.color_effect_entry_new_3, R.drawable.video_master_filter_lost, 50);
        filterInfo7.setTagUniqueFilterId(126);
        arrayList.add(filterInfo7);
        FilterInfo filterInfo8 = new FilterInfo(7, 60, R.string.color_effect_entry_new_4, R.drawable.video_master_filter_memory, 60);
        filterInfo8.setTagUniqueFilterId(127);
        arrayList.add(filterInfo8);
        FilterInfo filterInfo9 = new FilterInfo(7, 70, R.string.color_effect_entry_new_5, R.drawable.video_master_filter_central, 70);
        filterInfo9.setTagUniqueFilterId(128);
        arrayList.add(filterInfo9);
        FilterInfo filterInfo10 = new FilterInfo(7, 75, R.string.video_effect_entry_summer_day, R.drawable.video_master_filter_summer_day, 75);
        filterInfo10.setTagUniqueFilterId(124);
        arrayList.add(filterInfo10);
        FilterInfo filterInfo11 = new FilterInfo(7, 80, R.string.color_effect_entry_blackgold, R.drawable.video_master_filter_blackgold, 80);
        filterInfo11.setTagUniqueFilterId(110);
        arrayList.add(filterInfo11);
        FilterInfo filterInfo12 = new FilterInfo(7, 90, R.string.video_effect_entry_meet, R.drawable.video_master_filter_meet, 90);
        filterInfo12.setTagUniqueFilterId(104);
        arrayList.add(filterInfo12);
        FilterInfo filterInfo13 = new FilterInfo(7, 100, R.string.video_effect_entry_northern_europe, R.drawable.video_master_filter_northern_europe, 100);
        filterInfo13.setTagUniqueFilterId(108);
        arrayList.add(filterInfo13);
        FilterInfo filterInfo14 = new FilterInfo(7, 110, R.string.video_effect_entry_rome, R.drawable.video_master_filter_rome, 110);
        filterInfo14.setTagUniqueFilterId(109);
        arrayList.add(filterInfo14);
        Collections.sort(arrayList);
        return arrayList;
    }

    private void initialize() {
        ArrayList arrayList;
        SparseArray sparseArray;
        this.mIsIndiaColorLookupTableAvailable = isIndiaColorLookupTableAvailable();
        this.mFilterInfoMap = new SparseArray(6);
        this.mFilterInfoMap.put(5, initAiSceneFilterInfo());
        this.mFilterInfoMap.put(0, initPrivateFilterInfo());
        this.mFilterInfoMap.put(1, initNormalFilterInfoNew());
        this.mFilterInfoMap.put(6, initLightingFilterInfo());
        if (this.mIsIndiaColorLookupTableAvailable) {
            sparseArray = this.mFilterInfoMap;
            arrayList = initIndiaBeautyFilterInfo();
        } else {
            sparseArray = this.mFilterInfoMap;
            arrayList = initNormalFilterInfoNew();
        }
        sparseArray.put(2, arrayList);
        this.mFilterInfoMap.put(3, initAppVideoFilterInfo());
        this.mFilterInfoMap.put(7, initVideoFilterInfo());
        this.mFilterInfoMap.put(8, initAppVideoFilterInfo());
        this.mFilterInfoMap.put(9, initVideoMasterFilterInfo());
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isIndiaColorLookupTableAvailable() {
        int i;
        boolean z = false;
        if (!C0122O00000o.instance().OOo0OoO()) {
            return false;
        }
        try {
            String[] list = CameraAppImpl.getAndroidContext().getAssets().list("filter/beauty_india");
            if (list == null) {
                i = 0;
                if (i == FilterFactory.getFiltersByCategory(FilterCategory.BEAUTY_INDIA).length) {
                    z = true;
                }
                return z;
            }
            i = list.length;
            if (i == FilterFactory.getFiltersByCategory(FilterCategory.BEAUTY_INDIA).length) {
            }
            return z;
        } catch (IOException unused) {
            Log.d(TAG, "Failed to list files in directory 'filter/beauty_india'");
        }
    }

    private void postNotifyEffectChanged(int... iArr) {
        synchronized (this.mLock) {
            for (EffectChangedListener onEffectChanged : this.mChangedListeners) {
                onEffectChanged.onEffectChanged(iArr);
            }
        }
    }

    public static synchronized void releaseInstance() {
        synchronized (EffectController.class) {
            if (sInstance != null && sInstance.mChangedListeners.size() == 0) {
                sInstance = null;
            }
        }
    }

    public void addChangeListener(EffectChangedListener effectChangedListener) {
        synchronized (this.mLock) {
            this.mChangedListeners.add(effectChangedListener);
            EffectChangedListenerController.addEffectChangedListener(effectChangedListener);
        }
    }

    public void clearEffectAttribute() {
        this.mEffectRectAttribute.mRectF.set(0.0f, 0.0f, 0.0f, 0.0f);
        this.mEffectRectAttribute.mPoint1.set(0.0f, 0.0f);
        this.mEffectRectAttribute.mPoint2.set(0.0f, 0.0f);
        this.mEffectRectAttribute.mRangeWidth = 0.0f;
    }

    public EffectRectAttribute copyEffectRectAttribute() {
        return new EffectRectAttribute(this.mEffectRectAttribute, null);
    }

    public void enableMakeup(boolean z) {
        this.mBeautyEnable = z;
        if (!z) {
            this.mNeedDestroyMakeup = true;
        }
        postNotifyEffectChanged(3);
    }

    public LiveFilterItem findLiveFilter(Context context, int i) {
        List<LiveFilterItem> liveFilterList = getLiveFilterList(context);
        if (liveFilterList == null) {
            return null;
        }
        for (LiveFilterItem liveFilterItem : liveFilterList) {
            if (liveFilterItem.id == i) {
                return liveFilterItem;
            }
        }
        return null;
    }

    public int getAiColorCorrectionVersion() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getAiColorCorrectionVersion: ");
        sb.append(this.mAiColorCorrectionVersion);
        Log.d(str, sb.toString());
        return this.mAiColorCorrectionVersion;
    }

    public int getBlurAnimationValue() {
        int i = this.mBlurStep;
        if (i >= 0 && i <= 8) {
            this.mBlurStep = i + (this.mBlur ? 1 : -1);
            if (8 <= this.mBlurStep && this.mBlur) {
                this.mOverrideEffectIndex = FilterInfo.FILTER_ID_BLUR;
            }
            int i2 = this.mBlurStep;
            if (i2 >= 0 && i2 <= 8) {
                return (i2 * 212) / 8;
            }
        }
        return -1;
    }

    public String getCurrentKaleidoscope() {
        return this.mDrawKaleidoscope;
    }

    public String getCurrentSticker() {
        return this.mCurrentSticker;
    }

    public float getDeviceRotation() {
        return this.mDeviceRotation;
    }

    public EffectRectAttribute getEffectAttribute() {
        return this.mEffectRectAttribute;
    }

    public int getEffectCount(int i) {
        ArrayList arrayList = (ArrayList) this.mFilterInfoMap.get(i);
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public int getEffectForPreview(boolean z) {
        synchronized (this) {
            if (z) {
                if (this.mOverrideEffectIndex != -1) {
                    int i = this.mOverrideEffectIndex;
                    return i;
                }
            }
            if (this.mEffectId != FilterInfo.FILTER_ID_NONE || this.mOverrideAiEffectIndex == -1) {
                int i2 = this.mEffectId;
                return i2;
            }
            int i3 = this.mOverrideAiEffectIndex;
            return i3;
        }
    }

    public int getEffectForSaving(boolean z) {
        int i = this.mEffectId;
        boolean z2 = (i == FilterInfo.FILTER_ID_NONE || FilterInfo.getCategory(i) == 6) ? false : true;
        if (!z2) {
            return FilterInfo.FILTER_ID_NONE;
        }
        if (this.mAiColorCorrectionVersion == 2) {
            int i2 = this.mEffectId;
            if (i2 != FilterInfo.FILTER_ID_NONE && FilterInfo.getCategory(i2) == 5) {
                return FilterInfo.FILTER_ID_NONE;
            }
        }
        return getEffectForPreview(z);
    }

    public RenderGroup getEffectGroup(GLCanvas gLCanvas, RenderGroup renderGroup, boolean z, boolean z2, int i) {
        if (!C0124O00000oO.Oo00oo()) {
            return null;
        }
        if (!z && !renderGroup.isNeedInit(i)) {
            return renderGroup;
        }
        int i2 = 1;
        if (i > -1) {
            i2 = FilterInfo.getCategory(i);
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getEffectGroup: renderId = ");
        sb.append(i);
        Log.d(str, sb.toString());
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getEffectGroup: category = ");
        sb2.append(i2);
        Log.d(str2, sb2.toString());
        switch (i2) {
            case 0:
                getPrivateRender(gLCanvas, renderGroup, z, z2, i);
                break;
            case 1:
                getNormalRenderNew(gLCanvas, renderGroup, z, z2, i);
                break;
            case 2:
                getBeautyRender(gLCanvas, renderGroup, z, i);
                break;
            case 3:
                getStickerRender(gLCanvas, renderGroup, z, i);
                break;
            case 4:
                getMakeupRender(gLCanvas, renderGroup);
                break;
            case 5:
                getAiSceneRenderNew(gLCanvas, renderGroup, z, z2, i);
                break;
            case 6:
                getLightingRenderNew(gLCanvas, renderGroup, z, z2, i);
                break;
            case 8:
                getMiLiveRender(gLCanvas, renderGroup, z, i);
                break;
            default:
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("invalid renderId ");
                sb3.append(Integer.toHexString(i));
                Log.e(str3, sb3.toString());
                break;
        }
        return renderGroup;
    }

    public RectF getEffectRectF() {
        return new RectF(this.mEffectRectAttribute.mRectF);
    }

    public ArrayList getFilterInfo(int i) {
        return (ArrayList) this.mFilterInfoMap.get(i);
    }

    public int getInvertFlag() {
        return this.mEffectRectAttribute.mInvertFlag;
    }

    public List getLiveFilterList(Context context) {
        if (this.mLiveFilters == null) {
            TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.live_filter_icon);
            String[] stringArray = context.getResources().getStringArray(R.array.live_filter_name);
            String[] stringArray2 = context.getResources().getStringArray(R.array.live_filter_directory_name);
            this.mLiveFilters = new ArrayList();
            for (int i = 0; i < obtainTypedArray.length(); i++) {
                LiveFilterItem liveFilterItem = new LiveFilterItem();
                liveFilterItem.id = i;
                liveFilterItem.imageViewRes = obtainTypedArray.getDrawable(i);
                liveFilterItem.name = stringArray[i];
                liveFilterItem.directoryName = stringArray2[i];
                this.mLiveFilters.add(liveFilterItem);
            }
            obtainTypedArray.recycle();
        }
        return this.mLiveFilters;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public float getTiltShiftMaskAlpha() {
        return this.mTiltShiftMaskAlpha;
    }

    public boolean hasEffect() {
        return hasEffect(true);
    }

    public boolean hasEffect(boolean z) {
        boolean z2;
        boolean Oo00oo = C0124O00000oO.Oo00oo();
        boolean isSquareModule = ModuleManager.isSquareModule();
        boolean isTiltShiftOn = CameraSettings.isTiltShiftOn();
        boolean OOo0ooO = C0122O00000o.instance().OOo0ooO();
        boolean isDualCameraWaterMarkOpen = CameraSettings.isDualCameraWaterMarkOpen();
        boolean isFrontCameraWaterMarkOpen = CameraSettings.isFrontCameraWaterMarkOpen();
        boolean isTimeWaterMarkOpen = CameraSettings.isTimeWaterMarkOpen();
        boolean showGenderAge = CameraSettings.showGenderAge();
        boolean isMagicMirrorOn = CameraSettings.isMagicMirrorOn();
        boolean isCinematicAspectRatioEnabled = CameraSettings.isCinematicAspectRatioEnabled(DataRepository.dataItemGlobal().getCurrentMode());
        boolean z3 = isDualCameraWaterMarkOpen || isFrontCameraWaterMarkOpen || isTimeWaterMarkOpen || showGenderAge || isMagicMirrorOn;
        boolean z4 = OOo0ooO && z3;
        if (z) {
            synchronized (this) {
                z2 = (this.mEffectId == FilterInfo.FILTER_ID_NONE || FilterInfo.getCategory(this.mEffectId) == 6) ? false : true;
            }
        } else {
            z2 = false;
        }
        return Oo00oo && (z2 || isSquareModule || isTiltShiftOn || z4 || isCinematicAspectRatioEnabled);
    }

    public boolean isBackGroundBlur() {
        return getEffectForPreview(true) == FilterInfo.FILTER_ID_BLUR;
    }

    public boolean isBeautyFrameReady() {
        return this.mBeautyFrameReady;
    }

    public boolean isBlurAnimationDone() {
        int i = this.mBlurStep;
        return i > 8 || i < 0;
    }

    public boolean isCinematicEnable() {
        return this.mDrawCinematic;
    }

    public boolean isDrawGradienter() {
        return this.mDrawGradienter;
    }

    public boolean isDrawTilt() {
        return this.mDrawTilt;
    }

    public boolean isEffectPageSelected() {
        return this.mEffectId != FilterInfo.FILTER_ID_NONE;
    }

    public boolean isKaleidoscopeEnable() {
        if (TextUtils.isEmpty(this.mDrawKaleidoscope)) {
            return false;
        }
        return !"0".equals(this.mDrawKaleidoscope);
    }

    public boolean isMainFrameDisplay() {
        return this.mIsDrawMainFrame;
    }

    public boolean isMakeupEnable() {
        return this.mBeautyEnable;
    }

    public boolean isNeedDrawExposure() {
        return this.mDrawExposure;
    }

    public boolean isNeedDrawPeaking() {
        return this.mDrawPeaking;
    }

    public boolean isNeedRect(int i) {
        ArrayList arrayList = (ArrayList) this.mFilterInfoMap.get(FilterInfo.getCategory(i));
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                FilterInfo filterInfo = (FilterInfo) it.next();
                if (filterInfo.getId() == i) {
                    return filterInfo.isNeedRect();
                }
            }
        }
        return false;
    }

    public boolean isStickerEnable() {
        return !TextUtils.isEmpty(this.mCurrentSticker);
    }

    public boolean needDestroyMakeup() {
        return this.mNeedDestroyMakeup;
    }

    public void notifyRealtimePreviewFilterChanged(int i) {
        synchronized (this.mLock) {
            for (EffectChangedListener onRealtimePreviewFilterChanged : this.mChangedListeners) {
                onRealtimePreviewFilterChanged.onRealtimePreviewFilterChanged(i);
            }
        }
    }

    public boolean removeChangeListener(EffectChangedListener effectChangedListener) {
        synchronized (this.mLock) {
            if (this.mChangedListeners.size() == 0) {
                return true;
            }
            boolean remove = this.mChangedListeners.remove(effectChangedListener);
            return remove;
        }
    }

    public void reset() {
        this.mBeautyEnable = false;
        this.mNeedDestroyMakeup = true;
        this.mCurrentSticker = null;
        this.mDrawPeaking = false;
        this.mDrawTilt = false;
        this.mDrawGradienter = false;
        this.mDrawExposure = false;
        this.mDrawCinematic = false;
        this.mDrawKaleidoscope = "0";
        postNotifyEffectChanged(EFFECT_ALL_CHANGE_TYPES);
    }

    public void setAiColorCorrectionVersion(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setAiColorCorrectionVersion: ");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mAiColorCorrectionVersion = i;
    }

    public void setAiSceneEffect(int i, boolean z) {
        if (FilterInfo.getCategory(i) == 5) {
            this.mOverrideAiEffectIndex = i;
        } else if (i == FilterInfo.FILTER_ID_NONE) {
            this.mOverrideAiEffectIndex = -1;
            if (!z) {
                return;
            }
        } else {
            return;
        }
        setEffect(i);
    }

    public void setBeautyFrameReady(boolean z) {
        this.mBeautyFrameReady = z;
        postNotifyEffectChanged(3);
    }

    public void setBlurEffect(boolean z) {
        if (z != this.mBlur) {
            if (!z) {
                this.mOverrideEffectIndex = -1;
            }
            int i = this.mBlurStep;
            int i2 = 8;
            if (i < 0 || 8 < i) {
                if (z) {
                    i2 = 0;
                }
                this.mBlurStep = i2;
            }
            this.mIsDrawMainFrame = true;
        }
        this.mBlur = z;
    }

    public void setCinematicEnable(boolean z) {
        this.mDrawCinematic = z;
    }

    public void setCurrentSticker(String str) {
        this.mCurrentSticker = str;
        postNotifyEffectChanged(2);
    }

    public void setDestroyMakeup(boolean z) {
        this.mNeedDestroyMakeup = z;
    }

    public void setDeviceRotation(boolean z, float f) {
        this.mDeviceRotation = f;
    }

    public void setDrawExposure(boolean z) {
        this.mDrawExposure = z;
        postNotifyEffectChanged(7);
    }

    public void setDrawGradienter(boolean z) {
        this.mDrawGradienter = z;
        postNotifyEffectChanged(6);
    }

    public void setDrawPeaking(boolean z) {
        this.mDrawPeaking = z;
        postNotifyEffectChanged(4);
    }

    public void setDrawTilt(boolean z) {
        this.mDrawTilt = z;
        postNotifyEffectChanged(5);
    }

    public void setEffect(int i) {
        synchronized (this) {
            if (i == FilterInfo.FILTER_ID_NONE && this.mOverrideAiEffectIndex != -1) {
                i = this.mOverrideAiEffectIndex;
            }
            this.mEffectId = i;
            postNotifyEffectChanged(1);
        }
    }

    public void setEffectAttribute(RectF rectF, PointF pointF, PointF pointF2, float f) {
        this.mEffectRectAttribute.mRectF.set(rectF);
        this.mEffectRectAttribute.mPoint1.set(pointF);
        this.mEffectRectAttribute.mPoint2.set(pointF2);
        this.mEffectRectAttribute.mRangeWidth = f;
    }

    public void setInvertFlag(int i) {
        this.mEffectRectAttribute.mInvertFlag = i;
    }

    public void setKaleidoscope(String str) {
        this.mDrawKaleidoscope = str;
        postNotifyEffectChanged(8);
    }

    public void setLightingEffect(int i) {
        FilterInfo.getCategory(i);
        setEffect(i);
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }

    public void setTiltShiftMaskAlpha(float f) {
        this.mTiltShiftMaskAlpha = f;
    }
}

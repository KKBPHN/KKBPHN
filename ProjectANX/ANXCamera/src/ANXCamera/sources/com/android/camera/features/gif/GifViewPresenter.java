package com.android.camera.features.gif;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Mimoji;

public class GifViewPresenter implements OnClickListener {
    public static final int GIF_EDIT_SHOW = 303;
    public static final int Gif_EDIT_HIDE = 202;
    public static final String TAG = "MimojiFragmentGifEdit";
    public Context mContext;
    private GifMediaPlayer mGifMediaPlayer;
    public boolean mIsAccelerateOpen = false;
    public boolean mIsClearBgOpen = false;
    public boolean mIsReverseOpen = false;
    private LinearLayout mLlAccelerate;
    private LinearLayout mLlAccelerate2;
    private LinearLayout mLlClearBg;
    private LinearLayout mLlClearBgOut;
    private LinearLayout mLlReverse;
    private LinearLayout mLlReverse2;
    private LinearLayout mLlThreeButton;
    private LinearLayout mLlTwoButton;
    private RelativeLayout mRlGifBottomOperate;
    private RelativeLayout rlGifConsume;

    public GifViewPresenter(Context context) {
        this.mContext = context;
    }

    private void doAccelerateGif() {
        this.mGifMediaPlayer.enableSpeedFilter(this.mIsAccelerateOpen);
    }

    private void doClearGifBackgroud() {
        this.mGifMediaPlayer.enableVideoSegmentFilter(this.mIsClearBgOpen);
    }

    private void doReverseGif() {
        this.mGifMediaPlayer.enableReverseFilter(this.mIsReverseOpen);
    }

    private void initGifView(View view) {
        this.mRlGifBottomOperate = (RelativeLayout) view.findViewById(R.id.rl_operate_gif_bottom_panel);
        this.rlGifConsume = (RelativeLayout) view.findViewById(R.id.rl_gif_full_screen_consume_click);
        this.rlGifConsume.setVisibility(0);
        this.mLlThreeButton = (LinearLayout) view.findViewById(R.id.ll_fuc_opera);
        this.mLlTwoButton = (LinearLayout) view.findViewById(R.id.ll_fuc_opera2);
        this.mLlClearBg = (LinearLayout) view.findViewById(R.id.ll_clear_bg);
        this.mLlClearBgOut = (LinearLayout) view.findViewById(R.id.ll_clear_bg_out);
        this.mLlClearBg.setOnClickListener(this);
        this.mLlAccelerate = (LinearLayout) view.findViewById(R.id.ll_accelerate);
        this.mLlAccelerate.setOnClickListener(this);
        this.mLlAccelerate2 = (LinearLayout) view.findViewById(R.id.ll_accelerate2);
        this.mLlAccelerate2.setOnClickListener(this);
        this.mLlReverse = (LinearLayout) view.findViewById(R.id.ll_reverse);
        this.mLlReverse.setOnClickListener(this);
        this.mLlReverse2 = (LinearLayout) view.findViewById(R.id.ll_reverse2);
        this.mLlReverse2.setOnClickListener(this);
        FolmeUtils.touchScale(this.mLlClearBg, this.mLlAccelerate, this.mLlAccelerate2, this.mLlReverse, this.mLlReverse2);
        updateOperateState();
    }

    public void initView(View view) {
        initGifView(view);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        String str;
        GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
        if (gifMediaPlayer != null && gifMediaPlayer.isEnable() && !this.mGifMediaPlayer.isComposing()) {
            switch (view.getId()) {
                case R.id.ll_accelerate /*2131296737*/:
                case R.id.ll_accelerate2 /*2131296738*/:
                    this.mIsAccelerateOpen = !this.mIsAccelerateOpen;
                    updateOperateState();
                    doAccelerateGif();
                    str = Mimoji.GIF_SPEED;
                    break;
                case R.id.ll_clear_bg /*2131296743*/:
                    this.mIsClearBgOpen = !this.mIsClearBgOpen;
                    updateOperateState();
                    doClearGifBackgroud();
                    str = Mimoji.GIF_REMOVE_BG;
                    break;
                case R.id.ll_reverse /*2131296750*/:
                case R.id.ll_reverse2 /*2131296751*/:
                    this.mIsReverseOpen = !this.mIsReverseOpen;
                    updateOperateState();
                    doReverseGif();
                    str = Mimoji.GIF_REMOVE_REVERSE;
                    break;
                default:
                    if (!Util.isAccessible()) {
                        view.postDelayed(new O0000o(view), 100);
                        return;
                    }
                    return;
            }
            CameraStatUtils.trackMimoji2Click(null, str);
            if (!Util.isAccessible()) {
            }
        }
    }

    public void operateGifPannelVisibleState(int i) {
        int i2;
        RelativeLayout relativeLayout;
        if (i == 202) {
            relativeLayout = this.mRlGifBottomOperate;
            i2 = 8;
        } else if (i == 303) {
            relativeLayout = this.mRlGifBottomOperate;
            i2 = 0;
        } else {
            return;
        }
        relativeLayout.setVisibility(i2);
    }

    public void setGifMediaPlayer(GifMediaPlayer gifMediaPlayer) {
        this.mGifMediaPlayer = gifMediaPlayer;
    }

    public void updateOperateState() {
        String str;
        LinearLayout linearLayout;
        String str2;
        LinearLayout linearLayout2;
        StringBuilder sb = new StringBuilder();
        sb.append(", ");
        sb.append(this.mContext.getString(R.string.accessibility_selected));
        String sb2 = sb.toString();
        if (this.mIsAccelerateOpen) {
            this.mLlAccelerate.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_selected));
            this.mLlAccelerate2.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_selected));
            LinearLayout linearLayout3 = this.mLlAccelerate;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.mContext.getString(R.string.mimoji_gif_accelerate));
            sb3.append(sb2);
            linearLayout3.setContentDescription(sb3.toString());
            linearLayout = this.mLlAccelerate2;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(this.mContext.getString(R.string.mimoji_gif_accelerate));
            sb4.append(sb2);
            str = sb4.toString();
        } else {
            this.mLlAccelerate.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_default));
            this.mLlAccelerate2.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_default));
            this.mLlAccelerate.setContentDescription(this.mContext.getString(R.string.mimoji_gif_accelerate));
            linearLayout = this.mLlAccelerate2;
            str = this.mContext.getString(R.string.mimoji_gif_accelerate);
        }
        linearLayout.setContentDescription(str);
        if (!C0122O00000o.instance().OOo0O0() || DataRepository.dataItemLive().getMimojiStatusManager2().getCurrentMimojiInfo() != null) {
            this.mLlThreeButton.setVisibility(8);
            this.mLlTwoButton.setVisibility(0);
        } else {
            this.mLlThreeButton.setVisibility(0);
            this.mLlTwoButton.setVisibility(8);
            if (this.mIsClearBgOpen) {
                this.mLlClearBg.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_selected));
                linearLayout2 = this.mLlClearBg;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(this.mContext.getString(R.string.mimoji_gif_clear_bg));
                sb5.append(sb2);
                str2 = sb5.toString();
            } else {
                this.mLlClearBg.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_default));
                linearLayout2 = this.mLlClearBg;
                str2 = this.mContext.getString(R.string.mimoji_gif_clear_bg);
            }
            linearLayout2.setContentDescription(str2);
        }
        if (this.mIsReverseOpen) {
            this.mLlReverse.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_selected));
            this.mLlReverse2.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_selected));
            LinearLayout linearLayout4 = this.mLlReverse;
            StringBuilder sb6 = new StringBuilder();
            sb6.append(this.mContext.getString(R.string.mimoji_gif_reverse));
            sb6.append(sb2);
            linearLayout4.setContentDescription(sb6.toString());
            LinearLayout linearLayout5 = this.mLlReverse2;
            StringBuilder sb7 = new StringBuilder();
            sb7.append(this.mContext.getString(R.string.mimoji_gif_reverse));
            sb7.append(sb2);
            linearLayout5.setContentDescription(sb7.toString());
        } else {
            this.mLlReverse.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_default));
            this.mLlReverse2.setBackground(this.mContext.getResources().getDrawable(R.drawable.gif_round_corner_default));
            this.mLlReverse.setContentDescription(this.mContext.getString(R.string.mimoji_gif_reverse));
            this.mLlReverse2.setContentDescription(this.mContext.getString(R.string.mimoji_gif_reverse));
        }
        this.mRlGifBottomOperate.invalidate();
    }
}

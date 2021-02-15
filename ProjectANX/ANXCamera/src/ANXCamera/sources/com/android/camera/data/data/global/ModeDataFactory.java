package com.android.camera.data.data.global;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import com.android.camera.R;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.config.ComponentRunningUltraPixel;
import miui.telephony.phonenumber.CountryCodeConverter;

public class ModeDataFactory {
    private static volatile ModeDataFactory singleton;

    public static ModeDataFactory getInstance() {
        if (singleton == null) {
            synchronized (ModeDataFactory.class) {
                if (singleton == null) {
                    singleton = new ModeDataFactory();
                }
            }
        }
        return singleton;
    }

    private String ultraPixelModuleName() {
        if (!C0124O00000oO.O0o0Ooo) {
            return C0122O00000o.instance().OOOoOOO();
        }
        String str = C0124O00000oO.OOooOOO() ? CountryCodeConverter.PL : CountryCodeConverter.NZ;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(C0122O00000o.instance().OOOoOOO());
        return sb.toString();
    }

    public ComponentDataItem createModeData(int i) {
        C0122O00000o instance = C0122O00000o.instance();
        String valueOf = String.valueOf(i);
        if (i == 166) {
            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_panorama, (int) R.drawable.mode_icon_pano, (int) R.string.module_name_panorama);
        }
        if (i == 167) {
            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_pro, (int) R.drawable.mode_icon_pro, (int) R.string.module_name_professional);
        }
        if (i == 169) {
            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_time_lapse, (int) R.drawable.mode_icon_fast_motion, (int) R.string.pref_video_speed_fast_title);
        }
        if (i != 177) {
            if (i != 214) {
                if (i != 183) {
                    if (i != 184) {
                        if (i == 204) {
                            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_dual_video, (int) R.drawable.mode_icon_dual_video, instance.OOO000o() ? R.string.module_name_multi_camera : R.string.pref_dual_camera_video);
                        } else if (i == 205) {
                            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_more_ai_watermark, (int) R.drawable.mode_icon_ai_watermaker, (int) R.string.ai_watermark_title);
                        } else {
                            if (i == 254) {
                                return new ComponentDataItem(valueOf, -1, -1, (int) R.string.module_name_more);
                            }
                            if (i == 255) {
                                return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_edit, (int) R.drawable.mode_icon_edit, (int) R.string.module_name_edit);
                            }
                            switch (i) {
                                case 161:
                                    break;
                                case 162:
                                    return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_video, -1, (int) R.string.module_name_video);
                                case 163:
                                    return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_capture, -1, (int) R.string.module_name_capture);
                                default:
                                    switch (i) {
                                        case 171:
                                            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_portrait, (int) R.drawable.mode_icon_portrait, (int) R.string.module_name_portrait);
                                        case 172:
                                            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_slow_motion, (int) R.drawable.mode_icon_slow_motion, (int) R.string.module_name_new_slow_motion);
                                        case 173:
                                            break;
                                        case 174:
                                            break;
                                        case 175:
                                            return new ComponentDataItem(valueOf, ComponentRunningUltraPixel.getUltraPixelIcon(), (int) R.drawable.mode_icon_piexl, ultraPixelModuleName());
                                        default:
                                            switch (i) {
                                                case 186:
                                                    return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_doc, (int) R.drawable.mode_icon_doc, (int) R.string.pref_document_mode);
                                                case 187:
                                                    return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_ambilight, (int) R.drawable.mode_icon_awbilight, (int) R.string.parameter_ambilight_title);
                                                case 188:
                                                    return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_super_moon, (int) R.drawable.mode_icon_super_moon, (int) R.string.module_name_super_moon);
                                                default:
                                                    switch (i) {
                                                        case 209:
                                                            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_vlog, (int) R.drawable.mode_icon_vlog, (int) R.string.module_name_vlog);
                                                        case 210:
                                                            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_clone, (int) R.drawable.mode_icon_clone, (int) R.string.module_name_clone);
                                                        case 211:
                                                            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_videos, (int) R.drawable.mode_icon_film, (int) R.string.parameter_videos_title);
                                                        default:
                                                            throw new IllegalArgumentException("unSupport mode.");
                                                    }
                                            }
                                    }
                            }
                        }
                    }
                }
                return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_live, (int) R.drawable.mode_icon_live, (int) R.string.module_name_fun);
            }
            return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_night, (int) R.drawable.mode_icon_night, (int) R.string.pref_camera_scenemode_entry_night);
        }
        return new ComponentDataItem(valueOf, (int) R.drawable.ic_mode_mimoji, (int) R.drawable.mode_icon_mimoji, (int) R.string.module_name_fun_ar);
    }
}

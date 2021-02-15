package net.majorkernelpanic.streaming.rtsp;

import android.content.ContentValues;
import com.android.camera.fragment.top.FragmentTopAlert;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.video.VideoQuality;
import tv.danmaku.ijk.media.player.misc.IjkMediaFormat;

public class UriParser {
    public static final String TAG = "UriParser";

    public static Session parse(String str) {
        byte b;
        int i;
        SessionBuilder sessionBuilder;
        String str2;
        String str3 = "Invalid multicast address !";
        SessionBuilder copyOf = SessionBuilder.copyOf(SessionBuilder.getInstance());
        String query = URI.create(str).getQuery();
        byte b2 = 0;
        String[] split = query == null ? new String[0] : query.split("&");
        ContentValues contentValues = new ContentValues();
        for (String split2 : split) {
            String[] split3 = split2.split("=");
            try {
                str2 = split3[1];
            } catch (ArrayIndexOutOfBoundsException unused) {
                str2 = "";
            }
            String str4 = "UTF-8";
            contentValues.put(URLEncoder.encode(split3[0], str4), URLEncoder.encode(str2, str4));
        }
        if (contentValues.size() > 0) {
            copyOf.setAudioEncoder(0).setVideoEncoder(0);
            byte b3 = 0;
            b = 0;
            for (String str5 : contentValues.keySet()) {
                String asString = contentValues.getAsString(str5);
                if (str5.equalsIgnoreCase(FragmentTopAlert.TIP_FLASH)) {
                    if (asString.equalsIgnoreCase("on")) {
                        copyOf.setFlashEnabled(true);
                    } else {
                        copyOf.setFlashEnabled(false);
                    }
                } else if (str5.equalsIgnoreCase("camera")) {
                    if (asString.equalsIgnoreCase("back")) {
                        copyOf.setCamera(0);
                    } else if (asString.equalsIgnoreCase("front")) {
                        copyOf.setCamera(1);
                    }
                } else if (str5.equalsIgnoreCase("multicast")) {
                    if (asString != null) {
                        try {
                            if (InetAddress.getByName(asString).isMulticastAddress()) {
                                copyOf.setDestination(asString);
                            } else {
                                throw new IllegalStateException(str3);
                            }
                        } catch (UnknownHostException unused2) {
                            throw new IllegalStateException(str3);
                        }
                    } else {
                        copyOf.setDestination("228.5.6.7");
                    }
                } else if (!str5.equalsIgnoreCase("unicast")) {
                    String str6 = "mc";
                    String str7 = "mr";
                    if (str5.equalsIgnoreCase("videoapi")) {
                        if (asString != null) {
                            if (asString.equalsIgnoreCase(str7)) {
                                b3 = 1;
                            } else if (asString.equalsIgnoreCase(str6)) {
                                b3 = 2;
                            }
                        }
                    } else if (str5.equalsIgnoreCase("audioapi")) {
                        if (asString != null) {
                            if (asString.equalsIgnoreCase(str7)) {
                                b = 1;
                            } else if (asString.equalsIgnoreCase(str6)) {
                                b = 2;
                            }
                        }
                    } else if (str5.equalsIgnoreCase("ttl")) {
                        if (asString != null) {
                            try {
                                int parseInt = Integer.parseInt(asString);
                                if (parseInt >= 0) {
                                    copyOf.setTimeToLive(parseInt);
                                } else {
                                    throw new IllegalStateException();
                                }
                            } catch (Exception unused3) {
                                throw new IllegalStateException("The TTL must be a positive integer !");
                            }
                        } else {
                            continue;
                        }
                    } else if (str5.equalsIgnoreCase(IjkMediaFormat.CODEC_NAME_H264)) {
                        copyOf.setVideoQuality(VideoQuality.parseQuality(asString)).setVideoEncoder(1);
                    } else if (str5.equalsIgnoreCase("h263")) {
                        copyOf.setVideoQuality(VideoQuality.parseQuality(asString)).setVideoEncoder(2);
                    } else {
                        if (str5.equalsIgnoreCase("amrnb") || str5.equalsIgnoreCase("amr")) {
                            sessionBuilder = copyOf.setAudioQuality(AudioQuality.parseQuality(asString));
                            i = 3;
                        } else if (str5.equalsIgnoreCase("aac")) {
                            sessionBuilder = copyOf.setAudioQuality(AudioQuality.parseQuality(asString));
                            i = 5;
                        }
                        sessionBuilder.setAudioEncoder(i);
                    }
                } else if (asString != null) {
                    copyOf.setDestination(asString);
                }
            }
            b2 = b3;
        } else {
            b = 0;
        }
        if (copyOf.getVideoEncoder() == 0 && copyOf.getAudioEncoder() == 0) {
            SessionBuilder instance = SessionBuilder.getInstance();
            copyOf.setVideoEncoder(instance.getVideoEncoder());
            copyOf.setAudioEncoder(instance.getAudioEncoder());
        }
        Session build = copyOf.build();
        if (b2 > 0 && build.getVideoTrack() != null) {
            build.getVideoTrack().setStreamingMethod(b2);
        }
        if (b > 0 && build.getAudioTrack() != null) {
            build.getAudioTrack().setStreamingMethod(b);
        }
        return build;
    }
}

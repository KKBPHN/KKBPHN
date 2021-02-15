package miui.net;

import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.android.camera.network.net.base.HTTP;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.storage.Storage;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.xiaomi.stat.d;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class MimeUtils {
    private static final Map extensionToMimeTypeMap = new HashMap();
    private static final Map mimeTypeToExtensionMap = new HashMap();

    static {
        add("application/andrew-inset", "ez");
        add("application/dsptype", "tsp");
        add("application/futuresplash", "spl");
        add("application/hta", "hta");
        add("application/mac-binhex40", "hqx");
        add("application/mac-compactpro", "cpt");
        add("application/mathematica", "nb");
        add("application/msaccess", "mdb");
        add("application/oda", "oda");
        add("application/ogg", "ogg");
        add("application/pdf", "pdf");
        add("application/pgp-keys", WatermarkConstant.ITEM_KEY);
        add("application/pgp-signature", "pgp");
        add("application/pics-rules", "prf");
        add("application/rar", "rar");
        add("application/rdf+xml", "rdf");
        add("application/rss+xml", "rss");
        add("application/zip", "zip");
        add("application/vnd.android.package-archive", SplitConstants.KEY_APK);
        add("application/vnd.cinderella", "cdy");
        add("application/vnd.ms-pki.stl", "stl");
        add("application/vnd.oasis.opendocument.database", "odb");
        add("application/vnd.oasis.opendocument.formula", "odf");
        add("application/vnd.oasis.opendocument.graphics", "odg");
        add("application/vnd.oasis.opendocument.graphics-template", "otg");
        add("application/vnd.oasis.opendocument.image", "odi");
        add("application/vnd.oasis.opendocument.spreadsheet", "ods");
        add("application/vnd.oasis.opendocument.spreadsheet-template", "ots");
        add("application/vnd.oasis.opendocument.text", "odt");
        add("application/vnd.oasis.opendocument.text-master", "odm");
        add("application/vnd.oasis.opendocument.text-template", "ott");
        add("application/vnd.oasis.opendocument.text-web", "oth");
        add("application/vnd.google-earth.kml+xml", "kml");
        add("application/vnd.google-earth.kmz", "kmz");
        add("application/msword", "doc");
        add("application/msword", "dot");
        add("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        add("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "dotx");
        add("application/vnd.ms-excel", "xls");
        add("application/vnd.ms-excel", "xlt");
        add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        add("application/vnd.openxmlformats-officedocument.spreadsheetml.template", "xltx");
        String str = "application/vnd.ms-powerpoint";
        add(str, "ppt");
        add(str, "pot");
        add(str, "pps");
        add("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
        add("application/vnd.openxmlformats-officedocument.presentationml.template", "potx");
        add("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "ppsx");
        add("application/vnd.rim.cod", "cod");
        add("application/vnd.smaf", "mmf");
        add("application/vnd.stardivision.calc", "sdc");
        add("application/vnd.stardivision.draw", "sda");
        add("application/vnd.stardivision.impress", "sdd");
        add("application/vnd.stardivision.impress", "sdp");
        add("application/vnd.stardivision.math", "smf");
        add("application/vnd.stardivision.writer", "sdw");
        add("application/vnd.stardivision.writer", "vor");
        add("application/vnd.stardivision.writer-global", "sgl");
        add("application/vnd.sun.xml.calc", "sxc");
        add("application/vnd.sun.xml.calc.template", "stc");
        add("application/vnd.sun.xml.draw", "sxd");
        add("application/vnd.sun.xml.draw.template", "std");
        add("application/vnd.sun.xml.impress", "sxi");
        add("application/vnd.sun.xml.impress.template", "sti");
        add("application/vnd.sun.xml.math", "sxm");
        add("application/vnd.sun.xml.writer", "sxw");
        add("application/vnd.sun.xml.writer.global", "sxg");
        add("application/vnd.sun.xml.writer.template", "stw");
        add("application/vnd.visio", "vsd");
        add("application/x-abiword", "abw");
        add("application/x-apple-diskimage", "dmg");
        add("application/x-bcpio", "bcpio");
        add("application/x-bittorrent", "torrent");
        add("application/x-cdf", "cdf");
        add("application/x-cdlink", "vcd");
        add("application/x-chess-pgn", "pgn");
        add("application/x-cpio", "cpio");
        add("application/x-debian-package", "deb");
        add("application/x-debian-package", "udeb");
        String str2 = "application/x-director";
        add(str2, "dcr");
        add(str2, "dir");
        add(str2, "dxr");
        add("application/x-dms", "dms");
        add("application/x-doom", "wad");
        add("application/x-dvi", "dvi");
        add("application/x-flac", "flac");
        String str3 = "application/x-font";
        add(str3, "pfa");
        add(str3, "pfb");
        add(str3, "gsf");
        add(str3, "pcf");
        add(str3, "pcf.Z");
        add("application/x-freemind", SupportedConfigFactory.CLOSE_BY_MANUAL_MODE);
        add("application/x-futuresplash", "spl");
        add("application/x-gnumeric", "gnumeric");
        add("application/x-go-sgf", "sgf");
        add("application/x-graphing-calculator", "gcf");
        String str4 = "application/x-gtar";
        add(str4, "gtar");
        add(str4, "tgz");
        add(str4, "taz");
        add("application/x-hdf", "hdf");
        add("application/x-ica", "ica");
        add("application/x-internet-signup", "ins");
        add("application/x-internet-signup", "isp");
        add("application/x-iphone", "iii");
        add("application/x-iso9660-image", Manual.ISO);
        add("application/x-jmol", "jmz");
        add("application/x-kchart", "chrt");
        add("application/x-killustrator", "kil");
        String str5 = "application/x-koan";
        add(str5, "skp");
        add(str5, "skd");
        add(str5, "skt");
        add(str5, "skm");
        add("application/x-kpresenter", "kpr");
        add("application/x-kpresenter", "kpt");
        add("application/x-kspread", "ksp");
        add("application/x-kword", "kwd");
        add("application/x-kword", "kwt");
        add("application/x-latex", "latex");
        add("application/x-lha", "lha");
        add("application/x-lzh", "lzh");
        add("application/x-lzx", "lzx");
        String str6 = "application/x-maker";
        add(str6, "frm");
        add(str6, "maker");
        add(str6, "frame");
        add(str6, "fb");
        add(str6, "book");
        add(str6, "fbdoc");
        add("application/x-mif", "mif");
        add("application/x-ms-wmd", "wmd");
        add("application/x-ms-wmz", "wmz");
        add("application/x-msi", "msi");
        add("application/x-ns-proxy-autoconfig", "pac");
        add("application/x-nwc", "nwc");
        add("application/x-object", SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT);
        add("application/x-oz-application", "oza");
        add("application/x-pkcs12", "p12");
        add("application/x-pkcs12", "pfx");
        add("application/x-pkcs7-certreqresp", "p7r");
        add("application/x-pkcs7-crl", "crl");
        add("application/x-quicktimeplayer", "qtl");
        add("application/x-shar", "shar");
        add("application/x-shockwave-flash", "swf");
        add("application/x-stuffit", "sit");
        add("application/x-sv4cpio", "sv4cpio");
        add("application/x-sv4crc", "sv4crc");
        add("application/x-tar", "tar");
        add("application/x-texinfo", "texinfo");
        add("application/x-texinfo", "texi");
        add("application/x-troff", "t");
        add("application/x-troff", "roff");
        add("application/x-troff-man", "man");
        add("application/x-ustar", "ustar");
        add("application/x-wais-source", "src");
        add("application/x-wingz", "wz");
        add("application/x-webarchive", "webarchive");
        add("application/x-webarchive-xml", "webarchivexml");
        add("application/x-x509-ca-cert", "crt");
        add("application/x-x509-user-cert", "crt");
        add("application/x-xcf", "xcf");
        add("application/x-xfig", "fig");
        add("application/xhtml+xml", "xhtml");
        add("audio/3gpp", "3gpp");
        add("audio/amr", "amr");
        add("audio/basic", "snd");
        String str7 = "audio/midi";
        add(str7, "mid");
        add(str7, "midi");
        add(str7, "kar");
        add(str7, "xmf");
        add("audio/mobile-xmf", "mxmf");
        String str8 = "audio/mpeg";
        add(str8, "mpga");
        add(str8, "mpega");
        add(str8, "mp2");
        add(str8, "mp3");
        add(str8, "m4a");
        add("audio/mpegurl", "m3u");
        add("audio/prs.sid", d.g);
        String str9 = "audio/x-aiff";
        add(str9, "aif");
        add(str9, "aiff");
        add(str9, "aifc");
        add("audio/x-gsm", "gsm");
        add("audio/x-mpegurl", "m3u");
        add("audio/x-ms-wma", "wma");
        add("audio/x-ms-wax", "wax");
        String str10 = "audio/x-pn-realaudio";
        add(str10, "ra");
        add(str10, "rm");
        add(str10, "ram");
        add("audio/x-realaudio", "ra");
        add("audio/x-scpls", "pls");
        add("audio/x-sd2", "sd2");
        add("audio/x-wav", "wav");
        add("image/bmp", "bmp");
        add(Storage.MIME_GIF, "gif");
        add("image/ico", "cur");
        add("image/ico", "ico");
        add("image/ief", "ief");
        String str11 = Storage.MIME_JPEG;
        add(str11, "jpeg");
        add(str11, "jpg");
        add(str11, "jpe");
        add("image/pcx", "pcx");
        add("image/png", "png");
        add("image/svg+xml", "svg");
        add("image/svg+xml", "svgz");
        add("image/tiff", "tiff");
        add("image/tiff", "tif");
        add("image/vnd.djvu", "djvu");
        add("image/vnd.djvu", "djv");
        add("image/vnd.wap.wbmp", "wbmp");
        add("image/x-cmu-raster", "ras");
        add("image/x-coreldraw", "cdr");
        add("image/x-coreldrawpattern", "pat");
        add("image/x-coreldrawtemplate", "cdt");
        add("image/x-corelphotopaint", "cpt");
        add("image/x-icon", "ico");
        add("image/x-jg", "art");
        add("image/x-jng", "jng");
        add("image/x-ms-bmp", "bmp");
        add("image/x-photoshop", "psd");
        add("image/x-portable-anymap", "pnm");
        add("image/x-portable-bitmap", "pbm");
        add("image/x-portable-graymap", "pgm");
        add("image/x-portable-pixmap", "ppm");
        add("image/x-rgb", "rgb");
        add("image/x-xbitmap", "xbm");
        add("image/x-xpixmap", "xpm");
        add("image/x-xwindowdump", "xwd");
        add("model/iges", "igs");
        add("model/iges", "iges");
        add("model/mesh", "msh");
        add("model/mesh", "mesh");
        add("model/mesh", "silo");
        add("text/calendar", "ics");
        add("text/calendar", "icz");
        add("text/comma-separated-values", "csv");
        add("text/css", "css");
        add("text/html", "htm");
        add("text/html", "html");
        add("text/h323", "323");
        add("text/iuls", "uls");
        add("text/mathml", "mml");
        String str12 = HTTP.PLAIN_TEXT_TYPE;
        add(str12, "txt");
        add(str12, "asc");
        add(str12, "text");
        add(str12, "diff");
        add(str12, "po");
        add("text/richtext", "rtx");
        add("text/rtf", "rtf");
        add("text/texmacs", "ts");
        add("text/text", "phps");
        add("text/tab-separated-values", "tsv");
        add("text/xml", "xml");
        add("text/x-bibtex", "bib");
        add("text/x-boo", "boo");
        String str13 = "text/x-c++hdr";
        add(str13, "h++");
        add(str13, "hpp");
        add(str13, "hxx");
        add(str13, "hh");
        add("text/x-c++src", "c++");
        add("text/x-c++src", "cpp");
        add("text/x-c++src", "cxx");
        add("text/x-chdr", SupportedConfigFactory.CLOSE_BY_VIDEO);
        add("text/x-component", "htc");
        add("text/x-csh", "csh");
        add("text/x-csrc", SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION);
        add("text/x-dsrc", SupportedConfigFactory.CLOSE_BY_BURST_SHOOT);
        add("text/x-haskell", "hs");
        add("text/x-java", "java");
        add("text/x-literate-haskell", "lhs");
        add("text/x-moc", "moc");
        add("text/x-pascal", "p");
        add("text/x-pascal", "pas");
        add("text/x-pcs-gcd", "gcd");
        add("text/x-setext", "etx");
        add("text/x-tcl", "tcl");
        String str14 = "text/x-tex";
        add(str14, "tex");
        add(str14, "ltx");
        add(str14, "sty");
        add(str14, "cls");
        add("text/x-vcalendar", "vcs");
        add("text/x-vcard", "vcf");
        add("video/3gpp", "3gpp");
        add("video/3gpp", "3gp");
        add("video/3gpp", "3g2");
        add("video/dl", "dl");
        add("video/dv", "dif");
        add("video/dv", "dv");
        add("video/fli", "fli");
        add("video/m4v", "m4v");
        String str15 = "video/mpeg";
        add(str15, "mpeg");
        add(str15, "mpg");
        add(str15, "mpe");
        add("video/mp4", "mp4");
        add(str15, "VOB");
        add("video/quicktime", "qt");
        add("video/quicktime", "mov");
        add("video/vnd.mpegurl", "mxu");
        add("video/x-la-asf", "lsf");
        add("video/x-la-asf", "lsx");
        add("video/x-mng", "mng");
        add("video/x-ms-asf", "asf");
        add("video/x-ms-asf", "asx");
        add("video/x-ms-wm", "wm");
        add("video/x-ms-wmv", "wmv");
        add("video/x-ms-wmx", "wmx");
        add("video/x-ms-wvx", "wvx");
        add("video/x-msvideo", "avi");
        add("video/x-sgi-movie", "movie");
        add("x-conference/x-cooltalk", "ice");
        add("x-epoc/x-sisx-app", "sisx");
        applyOverrides();
    }

    protected MimeUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    private static void add(String str, String str2) {
        if (!mimeTypeToExtensionMap.containsKey(str)) {
            mimeTypeToExtensionMap.put(str, str2);
        }
        extensionToMimeTypeMap.put(str2, str);
    }

    private static void applyOverrides() {
        InputStream contentTypesPropertiesStream = getContentTypesPropertiesStream();
        if (contentTypesPropertiesStream != null) {
            try {
                Properties properties = new Properties();
                properties.load(contentTypesPropertiesStream);
                for (Entry entry : properties.entrySet()) {
                    add((String) entry.getValue(), (String) entry.getKey());
                }
                contentTypesPropertiesStream.close();
            } catch (IOException unused) {
            } catch (Throwable th) {
                contentTypesPropertiesStream.close();
                throw th;
            }
        }
    }

    private static InputStream getContentTypesPropertiesStream() {
        String property = System.getProperty("content.types.user.table");
        if (property != null) {
            File file = new File(property);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (IOException unused) {
                }
            }
        }
        String property2 = System.getProperty("java.home");
        StringBuilder sb = new StringBuilder();
        sb.append("lib");
        sb.append(File.separator);
        sb.append("content-types.properties");
        File file2 = new File(property2, sb.toString());
        if (file2.exists()) {
            try {
                return new FileInputStream(file2);
            } catch (IOException unused2) {
            }
        }
        return null;
    }

    public static String guessExtensionFromMimeType(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return (String) mimeTypeToExtensionMap.get(str);
    }

    public static String guessMimeTypeFromExtension(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return (String) extensionToMimeTypeMap.get(str);
    }

    public static boolean hasExtension(String str) {
        return str != null && !str.isEmpty() && extensionToMimeTypeMap.containsKey(str);
    }

    public static boolean hasMimeType(String str) {
        return str != null && !str.isEmpty() && mimeTypeToExtensionMap.containsKey(str);
    }
}

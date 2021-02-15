package com.arcsoft.camera.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Media;
import android.provider.MediaStore.Video.Thumbnails;
import com.android.camera.storage.Storage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class O0000Oo0 {
    private static final String[] D = {"max(_id) as newId", "_data", "_size", "datetaken", O0000O0o.e, "bucket_id", "mime_type", "date_modified", "media_type", "resolution", "tags", "width", "height", "orientation", "duration"};
    public static final String a = e(t);
    public static final Uri b = Files.getContentUri("external");
    public static final int c = 1;
    public static final int d = 3;
    public static final int e = 0;
    public static final int f = 1;
    public static final int g = 2;
    public static final int h = 3;
    public static final int i = 4;
    public static final int j = 5;
    public static final int k = 6;
    public static final int l = 7;
    public static final int m = 8;
    public static final int n = 9;
    public static final int o = 10;
    public static final int p = 11;
    public static final int q = 12;
    public static final int r = 13;
    public static final int s = 14;
    private static String t;
    private static O0000Oo0 u = null;
    private final String A = "video/mp4";
    private final String[] B = {"_id", "bucket_id", "bucket_display_name", "_data", "_display_name", "width", "height", "_size", "mime_type", "datetaken", "date_modified", "date_added", "latitude", "longitude", "duration", "resolution"};
    private final String[] C = {"_id", "bucket_id", "bucket_display_name", "_data", "_display_name", "width", "height", "_size", "mime_type", "datetaken", "date_modified", "date_added", "latitude", "longitude", "orientation"};
    private Context v = null;
    private ContentResolver w = null;
    private final String x = Storage.MIME_JPEG;
    private final String y = Storage.MIME_GIF;
    private final String z = "video/3gpp";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().toString());
        sb.append("/DCIM/WideSelfie/");
        t = sb.toString();
    }

    private O0000Oo0(Context context) {
        this.v = context;
        this.w = this.v.getContentResolver();
    }

    public static Cursor O000000o(ContentResolver contentResolver) {
        return contentResolver.query(b, D, "(media_type=? or media_type=?) and bucket_id=? ", new String[]{String.valueOf(1), String.valueOf(3), a}, "_id DESC");
    }

    private static Uri a(Cursor cursor) {
        return ContentUris.withAppendedId(Uri.parse(cursor.getString(4)), cursor.getLong(0));
    }

    private O0000OOo a(Cursor cursor, boolean z2) {
        Cursor cursor2 = cursor;
        boolean z3 = z2;
        if (cursor2 == null || cursor.getCount() <= 0) {
            return null;
        }
        O0000OOo o0000OOo = new O0000OOo();
        o0000OOo.a = z3;
        String str = "date_added";
        String str2 = "date_modified";
        String str3 = "datetaken";
        String str4 = "mime_type";
        String str5 = "_size";
        String str6 = "height";
        String str7 = "width";
        String str8 = "_display_name";
        String str9 = "_data";
        String str10 = "bucket_display_name";
        String str11 = "longitude";
        String str12 = "bucket_id";
        String str13 = "latitude";
        String str14 = "_id";
        if (z3) {
            String str15 = str;
            o0000OOo.c = cursor2.getLong(O00000o.O00000Oo(this.B, str14));
            o0000OOo.b = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, o0000OOo.c);
            o0000OOo.d = (long) cursor2.getInt(O00000o.O00000Oo(this.B, str12));
            o0000OOo.e = cursor2.getString(O00000o.O00000Oo(this.B, str10));
            o0000OOo.f = cursor2.getString(O00000o.O00000Oo(this.B, str9));
            o0000OOo.g = cursor2.getString(O00000o.O00000Oo(this.B, str8));
            o0000OOo.h = cursor2.getInt(O00000o.O00000Oo(this.B, str7));
            o0000OOo.i = cursor2.getInt(O00000o.O00000Oo(this.B, str6));
            o0000OOo.j = cursor2.getLong(O00000o.O00000Oo(this.B, str5));
            o0000OOo.k = cursor2.getString(O00000o.O00000Oo(this.B, str4));
            o0000OOo.l = cursor2.getString(O00000o.O00000Oo(this.B, str3));
            o0000OOo.m = cursor2.getString(O00000o.O00000Oo(this.B, str2));
            o0000OOo.n = cursor2.getString(O00000o.O00000Oo(this.B, str15));
            o0000OOo.o = cursor2.getDouble(O00000o.O00000Oo(this.B, str13));
            o0000OOo.p = cursor2.getDouble(O00000o.O00000Oo(this.B, str11));
            o0000OOo.r = cursor2.getLong(O00000o.O00000Oo(this.B, "duration"));
            o0000OOo.s = cursor2.getString(O00000o.O00000Oo(this.B, "resolution"));
        } else {
            String str16 = str;
            o0000OOo.c = cursor2.getLong(O00000o.O00000Oo(this.C, str14));
            o0000OOo.b = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, o0000OOo.c);
            o0000OOo.d = (long) cursor2.getInt(O00000o.O00000Oo(this.C, str12));
            o0000OOo.e = cursor2.getString(O00000o.O00000Oo(this.C, str10));
            o0000OOo.f = cursor2.getString(O00000o.O00000Oo(this.C, str9));
            o0000OOo.g = cursor2.getString(O00000o.O00000Oo(this.C, str8));
            o0000OOo.h = cursor2.getInt(O00000o.O00000Oo(this.C, str7));
            o0000OOo.i = cursor2.getInt(O00000o.O00000Oo(this.C, str6));
            o0000OOo.j = cursor2.getLong(O00000o.O00000Oo(this.C, str5));
            o0000OOo.k = cursor2.getString(O00000o.O00000Oo(this.C, str4));
            o0000OOo.l = cursor2.getString(O00000o.O00000Oo(this.C, str3));
            o0000OOo.m = cursor2.getString(O00000o.O00000Oo(this.C, str2));
            o0000OOo.n = cursor2.getString(O00000o.O00000Oo(this.C, str16));
            o0000OOo.o = cursor2.getDouble(O00000o.O00000Oo(this.C, str13));
            o0000OOo.p = cursor2.getDouble(O00000o.O00000Oo(this.C, str11));
            o0000OOo.q = cursor2.getInt(O00000o.O00000Oo(this.C, "orientation"));
        }
        return o0000OOo;
    }

    public static O0000Oo0 a(Context context) {
        if (u == null) {
            synchronized (O0000Oo0.class) {
                if (u == null) {
                    u = new O0000Oo0(context);
                }
            }
        }
        return u;
    }

    private String d(String str) {
        String substring = str.substring(str.lastIndexOf(46));
        return (Storage.JPEG_SUFFIX.equalsIgnoreCase(substring) || ".jpeg".equalsIgnoreCase(substring)) ? Storage.MIME_JPEG : ".gif".equalsIgnoreCase(substring) ? Storage.MIME_GIF : (".3gp".equalsIgnoreCase(substring) || ".3gpp".equalsIgnoreCase(substring)) ? "video/3gpp" : ".mp4".equalsIgnoreCase(substring) ? "video/mp4" : "";
    }

    private static String e(String str) {
        return String.valueOf(str.substring(0, str.lastIndexOf("/")).toLowerCase().hashCode());
    }

    public Bitmap O000000o(String str, Options options) {
        O0000OOo a2 = a(str);
        if (a2 == null) {
            int i2 = 20;
            while (i2 > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                O0000OOo a3 = a(str);
                if (a3 != null) {
                    a2 = a3;
                } else {
                    i2--;
                }
            }
            return null;
        }
        boolean O00000Oo2 = a2.a;
        ContentResolver contentResolver = this.w;
        long O000000o2 = a2.c;
        return O00000Oo2 ? Thumbnails.getThumbnail(contentResolver, O000000o2, 3, options) : Images.Thumbnails.getThumbnail(contentResolver, O000000o2, 3, options);
    }

    public Uri O000000o(String str, int i2, int i3) {
        return O000000o(str, i2, i3, null, 0);
    }

    public Uri O000000o(String str, int i2, int i3, int i4) {
        return O000000o(str, i2, i3, null, i4);
    }

    public Uri O000000o(String str, int i2, int i3, Location location) {
        return O000000o(str, i2, i3, location, 0);
    }

    public Uri O000000o(String str, int i2, int i3, Location location, int i4) {
        O000000o.isVideoFile(str);
        String d2 = d(str);
        File file = new File(str);
        String name = file.getName();
        String substring = name.substring(0, name.lastIndexOf("."));
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", file.getPath());
        contentValues.put("_display_name", name);
        contentValues.put("title", substring);
        contentValues.put("width", Integer.valueOf(i2));
        contentValues.put("height", Integer.valueOf(i3));
        contentValues.put("_size", Long.valueOf(file.length()));
        contentValues.put("mime_type", d2);
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        if (location != null) {
            contentValues.put("latitude", Double.valueOf(location.getLatitude()));
            contentValues.put("longitude", Double.valueOf(location.getLongitude()));
        }
        contentValues.put("orientation", Integer.valueOf(i4));
        Uri insert = this.w.insert(Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (VERSION.SDK_INT >= 14) {
            this.v.sendBroadcast(new Intent("android.hardware.action.NEW_PICTURE", insert));
        }
        return insert;
    }

    public Bitmap a(String str, float f2) {
        if (str == null) {
            str = a();
        }
        if (str == null) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = false;
        int i2 = (int) (((float) options.outHeight) / f2);
        if (i2 <= 0) {
            i2 = 1;
        }
        options.inSampleSize = i2;
        return BitmapFactory.decodeFile(str, options);
    }

    public O0000OOo a(String str) {
        Cursor cursor;
        O0000OOo o0000OOo = null;
        if (str == null) {
            return null;
        }
        boolean isVideoFile = O000000o.isVideoFile(str);
        if (isVideoFile) {
            cursor = this.w.query(Media.EXTERNAL_CONTENT_URI, this.B, "_data=?", new String[]{str}, "_id DESC");
        } else {
            cursor = this.w.query(Images.Media.EXTERNAL_CONTENT_URI, this.C, "_data=?", new String[]{str}, "_id DESC");
        }
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            o0000OOo = a(cursor, isVideoFile);
        }
        if (cursor != null) {
            cursor.close();
        }
        return o0000OOo;
    }

    public String a() {
        String str;
        Cursor O000000o2 = O000000o(this.w);
        if (O000000o2 == null || O000000o2.getCount() <= 0) {
            str = null;
        } else {
            O000000o2.moveToFirst();
            str = O000000o2.getString(1);
        }
        if (O000000o2 != null) {
            O000000o2.close();
        }
        return str;
    }

    public List a(String str, boolean z2) {
        Cursor cursor;
        if (str == null) {
            return Collections.emptyList();
        }
        if (str.endsWith(File.separator)) {
            str = str.substring(0, str.length() - 1);
        }
        int lastIndexOf = str.lastIndexOf(File.separator);
        if (-1 != lastIndexOf) {
            str = str.substring(lastIndexOf + 1);
        }
        if (z2) {
            cursor = this.w.query(Media.EXTERNAL_CONTENT_URI, this.B, "bucket_display_name=?", new String[]{str}, "_id ASC");
        } else {
            cursor = this.w.query(Images.Media.EXTERNAL_CONTENT_URI, this.C, "bucket_display_name=?", new String[]{str}, "_id ASC");
        }
        ArrayList arrayList = null;
        if (cursor != null && cursor.getCount() > 0) {
            arrayList = new ArrayList();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                arrayList.add(a(cursor, z2));
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return arrayList;
    }

    public boolean a(String str, String str2) {
        String str3;
        O0000OOo a2 = a(str);
        if (a2 == null) {
            return false;
        }
        int lastIndexOf = str2.lastIndexOf(File.separator);
        if (-1 != lastIndexOf) {
            str2 = str2.substring(lastIndexOf + 1);
        }
        String str4 = ".";
        int lastIndexOf2 = str2.lastIndexOf(str4);
        if (-1 != lastIndexOf2) {
            String str5 = str2;
            str2 = str2.substring(0, lastIndexOf2);
            str3 = str5;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(str4);
            sb.append(O000000o.getExtension(str));
            str3 = sb.toString();
        }
        ContentValues contentValues = new ContentValues();
        boolean O00000Oo2 = a2.a;
        contentValues.put("_display_name", str3);
        contentValues.put("title", str2);
        return this.w.update(a2.b, contentValues, null, null) > 0;
    }

    public Uri b() {
        Uri uri;
        Cursor O000000o2 = O000000o(this.w);
        if (O000000o2 == null || O000000o2.getCount() <= 0) {
            uri = null;
        } else {
            O000000o2.moveToFirst();
            uri = a(O000000o2);
        }
        if (O000000o2 != null) {
            O000000o2.close();
        }
        return uri;
    }

    public boolean b(String str) {
        O0000OOo a2 = a(str);
        return a2 != null && this.w.delete(a2.b, null, null) > 0;
    }

    public int c(String str) {
        int i2 = 0;
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt == 3) {
                i2 = 180;
            } else if (attributeInt == 6) {
                i2 = 90;
            } else if (attributeInt == 8) {
                i2 = 270;
            }
            return i2;
        } catch (IOException e2) {
            e2.printStackTrace();
            return 0;
        }
    }
}

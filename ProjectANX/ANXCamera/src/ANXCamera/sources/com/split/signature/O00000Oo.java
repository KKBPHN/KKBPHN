package com.split.signature;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;

final class O00000Oo implements O000000o {
    private final FileChannel a;
    private final long b;
    private final long c;

    O00000Oo(FileChannel fileChannel, long j, long j2) {
        this.a = fileChannel;
        this.b = j;
        this.c = j2;
    }

    public void O000000o(MessageDigest[] messageDigestArr, long j, int i) {
        MappedByteBuffer map = this.a.map(MapMode.READ_ONLY, this.b + j, (long) i);
        map.load();
        for (MessageDigest messageDigest : messageDigestArr) {
            map.position(0);
            messageDigest.update(map);
        }
    }

    public long a() {
        return this.c;
    }
}

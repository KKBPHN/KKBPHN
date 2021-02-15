package com.iqiyi.android.qigsaw.core.common;

import com.android.camera.network.net.base.HTTP;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

class SplitElfFile implements Closeable {
    public static final int FILE_TYPE_ELF = 1;
    public static final int FILE_TYPE_ODEX = 0;
    public static final int FILE_TYPE_OTHERS = -1;
    public ElfHeader elfHeader;
    private final FileInputStream fis;
    public ProgramHeader[] programHeaders;
    public SectionHeader[] sectionHeaders;
    private final Map sectionNameToHeaderMap = new HashMap();

    public class ElfHeader {
        public static final int EI_CLASS = 4;
        public static final int EI_DATA = 5;
        private static final int EI_NINDENT = 16;
        public static final int EI_VERSION = 6;
        public static final int ELFCLASS32 = 1;
        public static final int ELFCLASS64 = 2;
        public static final int ELFDATA2LSB = 1;
        public static final int ELFDATA2MSB = 2;
        public static final int ET_CORE = 4;
        public static final int ET_DYN = 3;
        public static final int ET_EXEC = 2;
        public static final int ET_HIPROC = 65535;
        public static final int ET_LOPROC = 65280;
        public static final int ET_NONE = 0;
        public static final int ET_REL = 1;
        public static final int EV_CURRENT = 1;
        public final short eEhSize;
        public final long eEntry;
        public final int eFlags;
        public final byte[] eIndent;
        public final short eMachine;
        public final short ePhEntSize;
        public final short ePhNum;
        public final long ePhOff;
        public final short eShEntSize;
        public final short eShNum;
        public final long eShOff;
        public final short eShStrNdx;
        public final short eType;
        public final int eVersion;

        private ElfHeader(FileChannel fileChannel) {
            long j;
            this.eIndent = new byte[16];
            fileChannel.position(0);
            fileChannel.read(ByteBuffer.wrap(this.eIndent));
            byte[] bArr = this.eIndent;
            if (bArr[0] == Byte.MAX_VALUE && bArr[1] == 69 && bArr[2] == 76 && bArr[3] == 70) {
                byte b = bArr[4];
                StringBuilder sb = new StringBuilder();
                sb.append("bad elf class: ");
                sb.append(this.eIndent[4]);
                SplitElfFile.assertInRange(b, 1, 2, sb.toString());
                byte b2 = this.eIndent[5];
                StringBuilder sb2 = new StringBuilder();
                sb2.append("bad elf data encoding: ");
                sb2.append(this.eIndent[5]);
                SplitElfFile.assertInRange(b2, 1, 2, sb2.toString());
                ByteBuffer allocate = ByteBuffer.allocate(this.eIndent[4] == 1 ? 36 : 48);
                allocate.order(this.eIndent[5] == 1 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
                SplitElfFile.readUntilLimit(fileChannel, allocate, "failed to read rest part of ehdr.");
                this.eType = allocate.getShort();
                this.eMachine = allocate.getShort();
                this.eVersion = allocate.getInt();
                int i = this.eVersion;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("bad elf version: ");
                sb3.append(this.eVersion);
                SplitElfFile.assertInRange(i, 1, 1, sb3.toString());
                byte b3 = this.eIndent[4];
                if (b3 == 1) {
                    this.eEntry = (long) allocate.getInt();
                    this.ePhOff = (long) allocate.getInt();
                    j = (long) allocate.getInt();
                } else if (b3 == 2) {
                    this.eEntry = allocate.getLong();
                    this.ePhOff = allocate.getLong();
                    j = allocate.getLong();
                } else {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Unexpected elf class: ");
                    sb4.append(this.eIndent[4]);
                    throw new IOException(sb4.toString());
                }
                this.eShOff = j;
                this.eFlags = allocate.getInt();
                this.eEhSize = allocate.getShort();
                this.ePhEntSize = allocate.getShort();
                this.ePhNum = allocate.getShort();
                this.eShEntSize = allocate.getShort();
                this.eShNum = allocate.getShort();
                this.eShStrNdx = allocate.getShort();
                return;
            }
            throw new IOException(String.format("bad elf magic: %x %x %x %x.", new Object[]{Byte.valueOf(this.eIndent[0]), Byte.valueOf(this.eIndent[1]), Byte.valueOf(this.eIndent[2]), Byte.valueOf(this.eIndent[3])}));
        }
    }

    public class ProgramHeader {
        public static final int PF_R = 4;
        public static final int PF_W = 2;
        public static final int PF_X = 1;
        public static final int PT_DYNAMIC = 2;
        public static final int PT_HIPROC = Integer.MAX_VALUE;
        public static final int PT_INTERP = 3;
        public static final int PT_LOAD = 1;
        public static final int PT_LOPROC = 1879048192;
        public static final int PT_NOTE = 4;
        public static final int PT_NULL = 0;
        public static final int PT_PHDR = 6;
        public static final int PT_SHLIB = 5;
        public final long pAlign;
        public final long pFileSize;
        public final int pFlags;
        public final long pMemSize;
        public final long pOffset;
        public final long pPddr;
        public final int pType;
        public final long pVddr;

        private ProgramHeader(ByteBuffer byteBuffer, int i) {
            long j;
            if (i == 1) {
                this.pType = byteBuffer.getInt();
                this.pOffset = (long) byteBuffer.getInt();
                this.pVddr = (long) byteBuffer.getInt();
                this.pPddr = (long) byteBuffer.getInt();
                this.pFileSize = (long) byteBuffer.getInt();
                this.pMemSize = (long) byteBuffer.getInt();
                this.pFlags = byteBuffer.getInt();
                j = (long) byteBuffer.getInt();
            } else if (i == 2) {
                this.pType = byteBuffer.getInt();
                this.pFlags = byteBuffer.getInt();
                this.pOffset = byteBuffer.getLong();
                this.pVddr = byteBuffer.getLong();
                this.pPddr = byteBuffer.getLong();
                this.pFileSize = byteBuffer.getLong();
                this.pMemSize = byteBuffer.getLong();
                j = byteBuffer.getLong();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected elf class: ");
                sb.append(i);
                throw new IOException(sb.toString());
            }
            this.pAlign = j;
        }
    }

    public class SectionHeader {
        public static final int SHF_ALLOC = 2;
        public static final int SHF_EXECINSTR = 4;
        public static final int SHF_MASKPROC = -268435456;
        public static final int SHF_WRITE = 1;
        public static final int SHN_ABS = 65521;
        public static final int SHN_COMMON = 65522;
        public static final int SHN_HIPROC = 65311;
        public static final int SHN_HIRESERVE = 65535;
        public static final int SHN_LOPROC = 65280;
        public static final int SHN_LORESERVE = 65280;
        public static final int SHN_UNDEF = 0;
        public static final int SHT_DYNAMIC = 6;
        public static final int SHT_DYNSYM = 11;
        public static final int SHT_HASH = 5;
        public static final int SHT_HIPROC = Integer.MAX_VALUE;
        public static final int SHT_HIUSER = -1;
        public static final int SHT_LOPROC = 1879048192;
        public static final int SHT_LOUSER = Integer.MIN_VALUE;
        public static final int SHT_NOBITS = 8;
        public static final int SHT_NOTE = 7;
        public static final int SHT_NULL = 0;
        public static final int SHT_PROGBITS = 1;
        public static final int SHT_REL = 9;
        public static final int SHT_RELA = 4;
        public static final int SHT_SHLIB = 10;
        public static final int SHT_STRTAB = 3;
        public static final int SHT_SYMTAB = 2;
        public final long shAddr;
        public final long shAddrAlign;
        public final long shEntSize;
        public final long shFlags;
        public final int shInfo;
        public final int shLink;
        public final int shName;
        public String shNameStr;
        public final long shOffset;
        public final long shSize;
        public final int shType;

        private SectionHeader(ByteBuffer byteBuffer, int i) {
            long j;
            if (i == 1) {
                this.shName = byteBuffer.getInt();
                this.shType = byteBuffer.getInt();
                this.shFlags = (long) byteBuffer.getInt();
                this.shAddr = (long) byteBuffer.getInt();
                this.shOffset = (long) byteBuffer.getInt();
                this.shSize = (long) byteBuffer.getInt();
                this.shLink = byteBuffer.getInt();
                this.shInfo = byteBuffer.getInt();
                this.shAddrAlign = (long) byteBuffer.getInt();
                j = (long) byteBuffer.getInt();
            } else if (i == 2) {
                this.shName = byteBuffer.getInt();
                this.shType = byteBuffer.getInt();
                this.shFlags = byteBuffer.getLong();
                this.shAddr = byteBuffer.getLong();
                this.shOffset = byteBuffer.getLong();
                this.shSize = byteBuffer.getLong();
                this.shLink = byteBuffer.getInt();
                this.shInfo = byteBuffer.getInt();
                this.shAddrAlign = byteBuffer.getLong();
                j = byteBuffer.getLong();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected elf class: ");
                sb.append(i);
                throw new IOException(sb.toString());
            }
            this.shEntSize = j;
            this.shNameStr = null;
        }
    }

    public SplitElfFile(File file) {
        SectionHeader[] sectionHeaderArr;
        SectionHeader[] sectionHeaderArr2;
        this.fis = new FileInputStream(file);
        FileChannel channel = this.fis.getChannel();
        this.elfHeader = new ElfHeader(channel);
        ByteBuffer allocate = ByteBuffer.allocate(128);
        allocate.limit(this.elfHeader.ePhEntSize);
        allocate.order(this.elfHeader.eIndent[5] == 1 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
        channel.position(this.elfHeader.ePhOff);
        this.programHeaders = new ProgramHeader[this.elfHeader.ePhNum];
        for (int i = 0; i < this.programHeaders.length; i++) {
            readUntilLimit(channel, allocate, "failed to read phdr.");
            this.programHeaders[i] = new ProgramHeader(allocate, this.elfHeader.eIndent[4]);
        }
        channel.position(this.elfHeader.eShOff);
        allocate.limit(this.elfHeader.eShEntSize);
        this.sectionHeaders = new SectionHeader[this.elfHeader.eShNum];
        int i2 = 0;
        while (true) {
            sectionHeaderArr = this.sectionHeaders;
            if (i2 >= sectionHeaderArr.length) {
                break;
            }
            readUntilLimit(channel, allocate, "failed to read shdr.");
            this.sectionHeaders[i2] = new SectionHeader(allocate, this.elfHeader.eIndent[4]);
            i2++;
        }
        short s = this.elfHeader.eShStrNdx;
        if (s > 0) {
            ByteBuffer section = getSection(sectionHeaderArr[s]);
            for (SectionHeader sectionHeader : this.sectionHeaders) {
                section.position(sectionHeader.shName);
                sectionHeader.shNameStr = readCString(section);
                this.sectionNameToHeaderMap.put(sectionHeader.shNameStr, sectionHeader);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void assertInRange(int i, int i2, int i3, String str) {
        if (i < i2 || i > i3) {
            throw new IOException(str);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x0053 A[SYNTHETIC, Splitter:B:40:0x0053] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getFileTypeByMagic(File file) {
        FileInputStream fileInputStream;
        try {
            byte[] bArr = new byte[4];
            fileInputStream = new FileInputStream(file);
            try {
                fileInputStream.read(bArr);
                if (bArr[0] == 100 && bArr[1] == 101 && bArr[2] == 121 && bArr[3] == 10) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable unused) {
                    }
                    return 0;
                } else if (bArr[0] == Byte.MAX_VALUE && bArr[1] == 69 && bArr[2] == 76 && bArr[3] == 70) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable unused2) {
                    }
                    return 1;
                } else {
                    try {
                        fileInputStream.close();
                    } catch (Throwable unused3) {
                    }
                    return -1;
                }
            } catch (Throwable th) {
                th = th;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable unused4) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            if (fileInputStream != null) {
            }
            throw th;
        }
    }

    public static String readCString(ByteBuffer byteBuffer) {
        byte[] array = byteBuffer.array();
        int position = byteBuffer.position();
        while (byteBuffer.hasRemaining() && array[byteBuffer.position()] != 0) {
            byteBuffer.position(byteBuffer.position() + 1);
        }
        byteBuffer.position(byteBuffer.position() + 1);
        return new String(array, position, (byteBuffer.position() - position) - 1, Charset.forName(HTTP.ASCII));
    }

    public static void readUntilLimit(FileChannel fileChannel, ByteBuffer byteBuffer, String str) {
        byteBuffer.rewind();
        int read = fileChannel.read(byteBuffer);
        if (read == byteBuffer.limit()) {
            byteBuffer.flip();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" Rest bytes insufficient, expect to read ");
        sb.append(byteBuffer.limit());
        sb.append(" bytes but only ");
        sb.append(read);
        sb.append(" bytes were read.");
        throw new IOException(sb.toString());
    }

    public void close() {
        this.fis.close();
        this.sectionNameToHeaderMap.clear();
        this.programHeaders = null;
        this.sectionHeaders = null;
    }

    public FileChannel getChannel() {
        return this.fis.getChannel();
    }

    public ByteOrder getDataOrder() {
        return this.elfHeader.eIndent[5] == 1 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }

    public ByteBuffer getSection(SectionHeader sectionHeader) {
        ByteBuffer allocate = ByteBuffer.allocate((int) sectionHeader.shSize);
        this.fis.getChannel().position(sectionHeader.shOffset);
        FileChannel channel = this.fis.getChannel();
        StringBuilder sb = new StringBuilder();
        sb.append("failed to read section: ");
        sb.append(sectionHeader.shNameStr);
        readUntilLimit(channel, allocate, sb.toString());
        return allocate;
    }

    public SectionHeader getSectionHeaderByName(String str) {
        return (SectionHeader) this.sectionNameToHeaderMap.get(str);
    }

    public ByteBuffer getSegment(ProgramHeader programHeader) {
        ByteBuffer allocate = ByteBuffer.allocate((int) programHeader.pFileSize);
        this.fis.getChannel().position(programHeader.pOffset);
        FileChannel channel = this.fis.getChannel();
        StringBuilder sb = new StringBuilder();
        sb.append("failed to read segment (type: ");
        sb.append(programHeader.pType);
        sb.append(").");
        readUntilLimit(channel, allocate, sb.toString());
        return allocate;
    }

    public boolean is32BitElf() {
        return this.elfHeader.eIndent[4] == 1;
    }
}

package miui.util;

import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DirectIndexedFile {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "DensityIndexFile: ";

    /* renamed from: miui.util.DirectIndexedFile$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type = new int[Type.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|20) */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.STRING.ordinal()] = 1;
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.BYTE_ARRAY.ordinal()] = 2;
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.SHORT_ARRAY.ordinal()] = 3;
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.INTEGER_ARRAY.ordinal()] = 4;
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.LONG_ARRAY.ordinal()] = 5;
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.BYTE.ordinal()] = 6;
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.SHORT.ordinal()] = 7;
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.INTEGER.ordinal()] = 8;
            $SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[Type.LONG.ordinal()] = 9;
        }
    }

    public class Builder {
        private IndexData mCurrentIndexData;
        private int mFileDataVersion;
        private FileHeader mFileHeader;
        private ArrayList mIndexDataList;

        class DataItemHolder {
            private ArrayList mList;
            private HashMap mMap;

            private DataItemHolder() {
                this.mMap = new HashMap();
                this.mList = new ArrayList();
            }

            /* synthetic */ DataItemHolder(Builder builder, AnonymousClass1 r2) {
                this();
            }

            /* access modifiers changed from: private */
            public ArrayList getAll() {
                return this.mList;
            }

            /* access modifiers changed from: private */
            public Integer put(Object obj) {
                Integer num = (Integer) this.mMap.get(obj);
                if (num != null) {
                    return num;
                }
                Integer valueOf = Integer.valueOf(this.mList.size());
                this.mMap.put(obj, valueOf);
                this.mList.add(obj);
                return valueOf;
            }

            /* access modifiers changed from: private */
            public int size() {
                return this.mList.size();
            }
        }

        class IndexData {
            /* access modifiers changed from: private */
            public DataItemDescriptor[] mDataItemDescriptions;
            /* access modifiers changed from: private */
            public ArrayList mDataItemHolders;
            /* access modifiers changed from: private */
            public HashMap mDataMap;
            /* access modifiers changed from: private */
            public Item mDefaultValue;
            /* access modifiers changed from: private */
            public ArrayList mIndexDataGroups;
            /* access modifiers changed from: private */
            public IndexGroupDescriptor[] mIndexGroupDescriptions;

            private IndexData(int i) {
                this.mDataMap = new HashMap();
                this.mDataItemHolders = new ArrayList();
                this.mIndexDataGroups = new ArrayList();
                this.mDataItemDescriptions = new DataItemDescriptor[i];
            }

            /* synthetic */ IndexData(int i, AnonymousClass1 r2) {
                this(i);
            }
        }

        class Item implements Comparable {
            /* access modifiers changed from: private */
            public int mIndex;
            /* access modifiers changed from: private */
            public Object[] mObjects;

            private Item(int i, Object[] objArr) {
                this.mIndex = i;
                this.mObjects = objArr;
            }

            /* synthetic */ Item(Builder builder, int i, Object[] objArr, AnonymousClass1 r4) {
                this(i, objArr);
            }

            public int compareTo(Item item) {
                return this.mIndex - item.mIndex;
            }

            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                return (obj instanceof Item) && this.mIndex == ((Item) obj).mIndex;
            }

            public int hashCode() {
                return this.mIndex;
            }
        }

        private Builder(int i) {
            this.mIndexDataList = new ArrayList();
            this.mFileDataVersion = i;
        }

        /* synthetic */ Builder(int i, AnonymousClass1 r2) {
            this(i);
        }

        private void build() {
            int size = this.mIndexDataList.size();
            this.mFileHeader = new FileHeader(size, this.mFileDataVersion, null);
            for (int i = 0; i < size; i++) {
                IndexData indexData = (IndexData) this.mIndexDataList.get(i);
                DescriptionPair[] access$300 = this.mFileHeader.mDescriptionOffsets;
                DescriptionPair descriptionPair = new DescriptionPair(0, 0, null);
                access$300[i] = descriptionPair;
                int i2 = 0;
                while (i2 < indexData.mIndexDataGroups.size() && ((ArrayList) indexData.mIndexDataGroups.get(i2)).size() != 0) {
                    i2++;
                }
                indexData.mIndexGroupDescriptions = new IndexGroupDescriptor[i2];
                for (int i3 = 0; i3 < indexData.mIndexGroupDescriptions.length; i3++) {
                    ArrayList arrayList = (ArrayList) indexData.mIndexDataGroups.get(i3);
                    Collections.sort(arrayList);
                    int access$3500 = ((Item) arrayList.get(0)).mIndex;
                    int access$35002 = ((Item) arrayList.get(arrayList.size() - 1)).mIndex + 1;
                    IndexGroupDescriptor[] access$3400 = indexData.mIndexGroupDescriptions;
                    IndexGroupDescriptor indexGroupDescriptor = new IndexGroupDescriptor(access$3500, access$35002, 0, null);
                    access$3400[i3] = indexGroupDescriptor;
                }
            }
            try {
                writeAll(null);
            } catch (IOException unused) {
            }
        }

        private void checkCurrentIndexingDataKind() {
            if (this.mCurrentIndexData == null) {
                throw new IllegalArgumentException("Please add a data kind before adding group");
            }
        }

        private void checkCurrentIndexingGroup() {
            checkCurrentIndexingDataKind();
            if (this.mCurrentIndexData.mIndexDataGroups.size() == 0) {
                throw new IllegalArgumentException("Please add a data group before adding data");
            }
        }

        private int writeAll(DataOutput dataOutput) {
            int i = 0;
            int i2 = 0;
            while (i < this.mFileHeader.mDescriptionOffsets.length) {
                int access$3700 = i2 + this.mFileHeader.write(dataOutput);
                this.mFileHeader.mDescriptionOffsets[i].mIndexGroupDescriptionOffset = (long) access$3700;
                IndexData indexData = (IndexData) this.mIndexDataList.get(i);
                if (dataOutput != null) {
                    dataOutput.writeInt(indexData.mIndexGroupDescriptions.length);
                }
                int i3 = access$3700 + 4;
                for (IndexGroupDescriptor access$3800 : indexData.mIndexGroupDescriptions) {
                    i3 += access$3800.write(dataOutput);
                }
                this.mFileHeader.mDescriptionOffsets[i].mDataItemDescriptionOffset = (long) i3;
                if (dataOutput != null) {
                    dataOutput.writeInt(indexData.mDataItemDescriptions.length);
                }
                int i4 = i3 + 4;
                for (DataItemDescriptor access$3900 : indexData.mDataItemDescriptions) {
                    i4 += access$3900.write(dataOutput);
                }
                for (int i5 = 0; i5 < indexData.mDataItemDescriptions.length; i5++) {
                    indexData.mDataItemDescriptions[i5].mOffset = (long) i4;
                    i4 += indexData.mDataItemDescriptions[i5].writeDataItems(dataOutput, ((DataItemHolder) indexData.mDataItemHolders.get(i5)).getAll());
                }
                for (int i6 = 0; i6 < indexData.mIndexGroupDescriptions.length; i6++) {
                    indexData.mIndexGroupDescriptions[i6].mOffset = (long) i4;
                    if (dataOutput == null) {
                        int i7 = 0;
                        for (DataItemDescriptor access$1200 : indexData.mDataItemDescriptions) {
                            i7 += access$1200.mIndexSize;
                        }
                        i4 += (indexData.mIndexGroupDescriptions[i6].mMaxIndex - indexData.mIndexGroupDescriptions[i6].mMinIndex) * i7;
                    } else {
                        int i8 = indexData.mIndexGroupDescriptions[i6].mMinIndex;
                        while (i8 < indexData.mIndexGroupDescriptions[i6].mMaxIndex) {
                            Item item = (Item) indexData.mDataMap.get(Integer.valueOf(i8));
                            if (item == null) {
                                item = indexData.mDefaultValue;
                            }
                            int i9 = i4;
                            for (int i10 = 0; i10 < indexData.mDataItemDescriptions.length; i10++) {
                                if (indexData.mDataItemDescriptions[i10].mIndexSize == 1) {
                                    dataOutput.writeByte(((Integer) item.mObjects[i10]).intValue());
                                } else if (indexData.mDataItemDescriptions[i10].mIndexSize == 2) {
                                    dataOutput.writeShort(((Integer) item.mObjects[i10]).intValue());
                                } else if (indexData.mDataItemDescriptions[i10].mIndexSize == 4) {
                                    dataOutput.writeInt(((Integer) item.mObjects[i10]).intValue());
                                } else if (indexData.mDataItemDescriptions[i10].mIndexSize == 8) {
                                    dataOutput.writeLong(((Long) item.mObjects[i10]).longValue());
                                }
                                i9 += indexData.mDataItemDescriptions[i10].mIndexSize;
                            }
                            i8++;
                            i4 = i9;
                        }
                    }
                }
                i++;
                i2 = i4;
            }
            return i2;
        }

        public void add(int i, Object... objArr) {
            DataItemDescriptor dataItemDescriptor;
            checkCurrentIndexingGroup();
            if (this.mCurrentIndexData.mDataItemDescriptions.length == objArr.length) {
                for (int i2 = 0; i2 < objArr.length; i2++) {
                    String str = "Object[";
                    switch (AnonymousClass1.$SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[this.mCurrentIndexData.mDataItemDescriptions[i2].mType.ordinal()]) {
                        case 1:
                            if (objArr[i2] instanceof String) {
                                objArr[i2] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i2)).put(objArr[i2]);
                                dataItemDescriptor = this.mCurrentIndexData.mDataItemDescriptions[i2];
                                break;
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append(str);
                                sb.append(i2);
                                sb.append("] should be String");
                                throw new IllegalArgumentException(sb.toString());
                            }
                        case 2:
                            if (objArr[i2] instanceof byte[]) {
                                objArr[i2] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i2)).put(objArr[i2]);
                                dataItemDescriptor = this.mCurrentIndexData.mDataItemDescriptions[i2];
                                break;
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str);
                                sb2.append(i2);
                                sb2.append("] should be byte[]");
                                throw new IllegalArgumentException(sb2.toString());
                            }
                        case 3:
                            if (objArr[i2] instanceof short[]) {
                                objArr[i2] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i2)).put(objArr[i2]);
                                dataItemDescriptor = this.mCurrentIndexData.mDataItemDescriptions[i2];
                                break;
                            } else {
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str);
                                sb3.append(i2);
                                sb3.append("] should be short[]");
                                throw new IllegalArgumentException(sb3.toString());
                            }
                        case 4:
                            if (objArr[i2] instanceof int[]) {
                                objArr[i2] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i2)).put(objArr[i2]);
                                dataItemDescriptor = this.mCurrentIndexData.mDataItemDescriptions[i2];
                                break;
                            } else {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(str);
                                sb4.append(i2);
                                sb4.append("] should be int[]");
                                throw new IllegalArgumentException(sb4.toString());
                            }
                        case 5:
                            if (objArr[i2] instanceof long[]) {
                                objArr[i2] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i2)).put(objArr[i2]);
                                dataItemDescriptor = this.mCurrentIndexData.mDataItemDescriptions[i2];
                                break;
                            } else {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(str);
                                sb5.append(i2);
                                sb5.append("] should be long[]");
                                throw new IllegalArgumentException(sb5.toString());
                            }
                        case 6:
                            if (objArr[i2] instanceof Byte) {
                                continue;
                            } else {
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append(str);
                                sb6.append(i2);
                                sb6.append("] should be byte");
                                throw new IllegalArgumentException(sb6.toString());
                            }
                        case 7:
                            if (objArr[i2] instanceof Short) {
                                continue;
                            } else {
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append(str);
                                sb7.append(i2);
                                sb7.append("] should be short");
                                throw new IllegalArgumentException(sb7.toString());
                            }
                        case 8:
                            if (objArr[i2] instanceof Integer) {
                                continue;
                            } else {
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append(str);
                                sb8.append(i2);
                                sb8.append("] should be int");
                                throw new IllegalArgumentException(sb8.toString());
                            }
                        case 9:
                            if (objArr[i2] instanceof Long) {
                                continue;
                            } else {
                                StringBuilder sb9 = new StringBuilder();
                                sb9.append(str);
                                sb9.append(i2);
                                sb9.append("] should be long");
                                throw new IllegalArgumentException(sb9.toString());
                            }
                        default:
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append("Unsupported type of objects ");
                            sb10.append(i2);
                            sb10.append(", ");
                            sb10.append(this.mCurrentIndexData.mDataItemDescriptions[i2].mType);
                            sb10.append(" expected");
                            throw new IllegalArgumentException(sb10.toString());
                    }
                    dataItemDescriptor.mIndexSize = DataItemDescriptor.getSizeOf(((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i2)).size());
                }
                Item item = new Item(this, i, objArr, null);
                this.mCurrentIndexData.mDataMap.put(Integer.valueOf(i), item);
                ((ArrayList) this.mCurrentIndexData.mIndexDataGroups.get(this.mCurrentIndexData.mIndexDataGroups.size() - 1)).add(item);
                return;
            }
            StringBuilder sb11 = new StringBuilder();
            sb11.append("Different number of objects inputted, ");
            sb11.append(this.mCurrentIndexData.mDataItemDescriptions.length);
            sb11.append(" data expected");
            throw new IllegalArgumentException(sb11.toString());
        }

        public void addGroup(int[] iArr, Object[][] objArr) {
            checkCurrentIndexingDataKind();
            if (iArr.length == objArr.length) {
                newGroup();
                for (int i = 0; i < iArr.length; i++) {
                    add(iArr[i], objArr[i]);
                }
                return;
            }
            throw new IllegalArgumentException("Different number between indexes and objects");
        }

        public void addKind(Object... objArr) {
            Type type;
            DataItemHolder dataItemHolder;
            Object obj;
            this.mCurrentIndexData = new IndexData(objArr.length, null);
            this.mIndexDataList.add(this.mCurrentIndexData);
            for (int i = 0; i < objArr.length; i++) {
                this.mCurrentIndexData.mDataItemHolders.add(new DataItemHolder(this, null));
                byte b = 1;
                if (objArr[i] instanceof Byte) {
                    type = Type.BYTE;
                    dataItemHolder = (DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i);
                    obj = objArr[i];
                } else if (objArr[i] instanceof Short) {
                    type = Type.SHORT;
                    b = 2;
                    dataItemHolder = (DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i);
                    obj = objArr[i];
                } else if (objArr[i] instanceof Integer) {
                    type = Type.INTEGER;
                    b = 4;
                    dataItemHolder = (DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i);
                    obj = objArr[i];
                } else if (objArr[i] instanceof Long) {
                    type = Type.LONG;
                    b = 8;
                    dataItemHolder = (DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i);
                    obj = objArr[i];
                } else {
                    if (objArr[i] instanceof String) {
                        type = Type.STRING;
                        objArr[i] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i)).put(objArr[i]);
                    } else if (objArr[i] instanceof byte[]) {
                        type = Type.BYTE_ARRAY;
                        objArr[i] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i)).put(objArr[i]);
                    } else if (objArr[i] instanceof short[]) {
                        type = Type.SHORT_ARRAY;
                        objArr[i] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i)).put(objArr[i]);
                    } else if (objArr[i] instanceof int[]) {
                        type = Type.INTEGER_ARRAY;
                        objArr[i] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i)).put(objArr[i]);
                    } else if (objArr[i] instanceof long[]) {
                        type = Type.LONG_ARRAY;
                        objArr[i] = ((DataItemHolder) this.mCurrentIndexData.mDataItemHolders.get(i)).put(objArr[i]);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unsupported type of the [");
                        sb.append(i);
                        sb.append("] argument");
                        throw new IllegalArgumentException(sb.toString());
                    }
                    Type type2 = type;
                    byte b2 = b;
                    DataItemDescriptor[] access$2400 = this.mCurrentIndexData.mDataItemDescriptions;
                    DataItemDescriptor dataItemDescriptor = new DataItemDescriptor(type2, b2, 0, 0, 0, null);
                    access$2400[i] = dataItemDescriptor;
                }
                dataItemHolder.put(obj);
                Type type22 = type;
                byte b22 = b;
                DataItemDescriptor[] access$24002 = this.mCurrentIndexData.mDataItemDescriptions;
                DataItemDescriptor dataItemDescriptor2 = new DataItemDescriptor(type22, b22, 0, 0, 0, null);
                access$24002[i] = dataItemDescriptor2;
            }
            this.mCurrentIndexData.mDefaultValue = new Item(this, -1, objArr, null);
        }

        public void newGroup() {
            if (this.mCurrentIndexData.mIndexDataGroups.size() == 0 || ((ArrayList) this.mCurrentIndexData.mIndexDataGroups.get(this.mCurrentIndexData.mIndexDataGroups.size() - 1)).size() != 0) {
                this.mCurrentIndexData.mIndexDataGroups.add(new ArrayList());
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:11:0x0020  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x002e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void write(String str) {
            build();
            DataOutputStream dataOutputStream = null;
            try {
                DataOutputStream dataOutputStream2 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(str)));
                try {
                    writeAll(dataOutputStream2);
                    dataOutputStream2.close();
                } catch (Throwable th) {
                    th = th;
                    dataOutputStream = dataOutputStream2;
                    if (dataOutputStream != null) {
                        dataOutputStream.close();
                    }
                    if (new File(str).delete()) {
                        PrintStream printStream = System.err;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Cannot delete file ");
                        sb.append(str);
                        printStream.println(sb.toString());
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (dataOutputStream != null) {
                }
                if (new File(str).delete()) {
                }
                throw th;
            }
        }
    }

    class DataInputRandom implements InputFile {
        private RandomAccessFile mFile;

        DataInputRandom(RandomAccessFile randomAccessFile) {
            this.mFile = randomAccessFile;
        }

        public void close() {
            this.mFile.close();
        }

        public long getFilePointer() {
            return this.mFile.getFilePointer();
        }

        public boolean readBoolean() {
            return this.mFile.readBoolean();
        }

        public byte readByte() {
            return this.mFile.readByte();
        }

        public char readChar() {
            return this.mFile.readChar();
        }

        public double readDouble() {
            return this.mFile.readDouble();
        }

        public float readFloat() {
            return this.mFile.readFloat();
        }

        public void readFully(byte[] bArr) {
            this.mFile.readFully(bArr);
        }

        public void readFully(byte[] bArr, int i, int i2) {
            this.mFile.readFully(bArr, i, i2);
        }

        public int readInt() {
            return this.mFile.readInt();
        }

        public String readLine() {
            return this.mFile.readLine();
        }

        public long readLong() {
            return this.mFile.readLong();
        }

        public short readShort() {
            return this.mFile.readShort();
        }

        public String readUTF() {
            return this.mFile.readUTF();
        }

        public int readUnsignedByte() {
            return this.mFile.readUnsignedByte();
        }

        public int readUnsignedShort() {
            return this.mFile.readUnsignedShort();
        }

        public void seek(long j) {
            this.mFile.seek(j);
        }

        public int skipBytes(int i) {
            return this.mFile.skipBytes(i);
        }
    }

    class DataInputStream implements InputFile {
        private InputStream mInputFile;
        private long mInputPos = 0;

        DataInputStream(InputStream inputStream) {
            this.mInputFile = inputStream;
            this.mInputFile.mark(0);
        }

        public void close() {
            this.mInputFile.close();
        }

        public long getFilePointer() {
            return this.mInputPos;
        }

        public boolean readBoolean() {
            this.mInputPos++;
            return this.mInputFile.read() != 0;
        }

        public byte readByte() {
            this.mInputPos++;
            return (byte) this.mInputFile.read();
        }

        public char readChar() {
            byte[] bArr = new byte[2];
            this.mInputPos += 2;
            if (this.mInputFile.read(bArr) == 2) {
                return (char) (((char) (bArr[1] & -1)) | ((bArr[0] << 8) & 65280));
            }
            return 0;
        }

        public double readDouble() {
            throw new IOException();
        }

        public float readFloat() {
            throw new IOException();
        }

        public void readFully(byte[] bArr) {
            this.mInputPos += (long) this.mInputFile.read(bArr);
        }

        public void readFully(byte[] bArr, int i, int i2) {
            this.mInputPos += (long) this.mInputFile.read(bArr, i, i2);
        }

        public int readInt() {
            byte[] bArr = new byte[4];
            this.mInputPos += 4;
            if (this.mInputFile.read(bArr) == 4) {
                return (bArr[3] & -1) | ((bArr[2] << 8) & 0) | ((bArr[1] << 16) & 0) | ((bArr[0] << 24) & 0);
            }
            return 0;
        }

        public String readLine() {
            throw new IOException();
        }

        public long readLong() {
            byte[] bArr = new byte[8];
            this.mInputPos += 8;
            if (this.mInputFile.read(bArr) != 8) {
                return 0;
            }
            return ((((long) bArr[0]) << 56) & -72057594037927936L) | ((long) (bArr[7] & -1)) | (((long) (bArr[6] << 8)) & 65280) | (((long) (bArr[5] << 16)) & 16711680) | (((long) (bArr[4] << 24)) & 4278190080L) | ((((long) bArr[3]) << 32) & 1095216660480L) | ((((long) bArr[2]) << 40) & 280375465082880L) | ((((long) bArr[1]) << 48) & 71776119061217280L);
        }

        public short readShort() {
            byte[] bArr = new byte[2];
            this.mInputPos += 2;
            if (this.mInputFile.read(bArr) == 2) {
                return (short) (((short) (bArr[1] & -1)) | ((bArr[0] << 8) & -256));
            }
            return 0;
        }

        public String readUTF() {
            throw new IOException();
        }

        public int readUnsignedByte() {
            this.mInputPos++;
            return (byte) this.mInputFile.read();
        }

        public int readUnsignedShort() {
            byte[] bArr = new byte[2];
            this.mInputPos += 2;
            if (this.mInputFile.read(bArr) == 2) {
                return (short) (((short) (bArr[1] & -1)) | ((bArr[0] << 8) & -256));
            }
            return 0;
        }

        public void seek(long j) {
            this.mInputFile.reset();
            if (this.mInputFile.skip(j) == j) {
                this.mInputPos = j;
                return;
            }
            throw new IOException("Skip failed");
        }

        public int skipBytes(int i) {
            int skip = (int) this.mInputFile.skip((long) i);
            this.mInputPos += (long) skip;
            return skip;
        }
    }

    class DataItemDescriptor {
        private static byte[] sBuffer = new byte[1024];
        /* access modifiers changed from: private */
        public byte mIndexSize;
        private byte mLengthSize;
        /* access modifiers changed from: private */
        public long mOffset;
        private byte mOffsetSize;
        /* access modifiers changed from: private */
        public Type mType;

        enum Type {
            BYTE,
            SHORT,
            INTEGER,
            LONG,
            STRING,
            BYTE_ARRAY,
            SHORT_ARRAY,
            INTEGER_ARRAY,
            LONG_ARRAY
        }

        private DataItemDescriptor(Type type, byte b, byte b2, byte b3, long j) {
            this.mType = type;
            this.mIndexSize = b;
            this.mLengthSize = b2;
            this.mOffsetSize = b3;
            this.mOffset = j;
        }

        /* synthetic */ DataItemDescriptor(Type type, byte b, byte b2, byte b3, long j, AnonymousClass1 r7) {
            this(type, b, b2, b3, j);
        }

        private static byte[] aquireBuffer(int i) {
            byte[] bArr;
            synchronized (DataItemDescriptor.class) {
                if (sBuffer == null || sBuffer.length < i) {
                    sBuffer = new byte[i];
                }
                bArr = sBuffer;
                sBuffer = null;
            }
            return bArr;
        }

        /* access modifiers changed from: private */
        public static byte getSizeOf(int i) {
            byte b = 0;
            for (long j = (long) (i * 2); j > 0; j >>= 8) {
                b = (byte) (b + 1);
            }
            if (b == 3) {
                return 4;
            }
            if (b <= 4 || b >= 8) {
                return b;
            }
            return 8;
        }

        /* access modifiers changed from: private */
        public static DataItemDescriptor read(DataInput dataInput) {
            DataItemDescriptor dataItemDescriptor = new DataItemDescriptor(Type.values()[dataInput.readByte()], dataInput.readByte(), dataInput.readByte(), dataInput.readByte(), dataInput.readLong());
            return dataItemDescriptor;
        }

        /* access modifiers changed from: private */
        public static long readAccordingToSize(DataInput dataInput, int i) {
            int i2;
            if (i == 1) {
                i2 = dataInput.readByte();
            } else if (i == 2) {
                i2 = dataInput.readShort();
            } else if (i == 4) {
                i2 = dataInput.readInt();
            } else if (i == 8) {
                return dataInput.readLong();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unsuppoert size ");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
            }
            return (long) i2;
        }

        /* access modifiers changed from: private */
        public Object[] readDataItems(InputFile inputFile) {
            switch (AnonymousClass1.$SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[this.mType.ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    Object[] objArr = new Object[inputFile.readInt()];
                    objArr[0] = readSingleDataItem(inputFile, 0);
                    return objArr;
                case 6:
                    return new Object[]{Byte.valueOf(inputFile.readByte())};
                case 7:
                    return new Object[]{Short.valueOf(inputFile.readShort())};
                case 8:
                    return new Object[]{Integer.valueOf(inputFile.readInt())};
                case 9:
                    return new Object[]{Long.valueOf(inputFile.readLong())};
                default:
                    return null;
            }
        }

        /* access modifiers changed from: private */
        public Object readSingleDataItem(InputFile inputFile, int i) {
            Object obj;
            int i2;
            long filePointer = inputFile.getFilePointer();
            if (i != 0) {
                inputFile.seek(((long) (this.mOffsetSize * i)) + filePointer);
            }
            inputFile.seek(filePointer + readAccordingToSize(inputFile, this.mOffsetSize));
            int i3 = AnonymousClass1.$SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[this.mType.ordinal()];
            byte[] bArr = null;
            int i4 = 0;
            if (i3 == 1) {
                int readAccordingToSize = (int) readAccordingToSize(inputFile, this.mLengthSize);
                bArr = aquireBuffer(readAccordingToSize);
                inputFile.readFully(bArr, 0, readAccordingToSize);
                obj = new String(bArr, 0, readAccordingToSize);
            } else if (i3 == 2) {
                byte[] bArr2 = new byte[((int) readAccordingToSize(inputFile, this.mLengthSize))];
                inputFile.readFully(bArr2);
                obj = bArr2;
            } else if (i3 == 3) {
                short[] sArr = new short[((int) readAccordingToSize(inputFile, this.mLengthSize))];
                while (i2 < sArr.length) {
                    sArr[i2] = inputFile.readShort();
                    i2++;
                }
                obj = sArr;
            } else if (i3 == 4) {
                int[] iArr = new int[((int) readAccordingToSize(inputFile, this.mLengthSize))];
                while (i4 < iArr.length) {
                    iArr[i4] = inputFile.readInt();
                    i4++;
                }
                obj = iArr;
            } else if (i3 != 5) {
                obj = 0;
            } else {
                long[] jArr = new long[((int) readAccordingToSize(inputFile, this.mLengthSize))];
                while (i4 < jArr.length) {
                    jArr[i4] = inputFile.readLong();
                    i4++;
                }
                obj = jArr;
            }
            releaseBuffer(bArr);
            return obj;
        }

        private static void releaseBuffer(byte[] bArr) {
            synchronized (DataItemDescriptor.class) {
                if (bArr != null) {
                    if (sBuffer == null || sBuffer.length < bArr.length) {
                        sBuffer = bArr;
                    }
                }
            }
        }

        /* access modifiers changed from: private */
        public int write(DataOutput dataOutput) {
            if (dataOutput != null) {
                dataOutput.writeByte(this.mType.ordinal());
                dataOutput.writeByte(this.mIndexSize);
                dataOutput.writeByte(this.mLengthSize);
                dataOutput.writeByte(this.mOffsetSize);
                dataOutput.writeLong(this.mOffset);
            }
            return 12;
        }

        private static void writeAccordingToSize(DataOutput dataOutput, int i, long j) {
            if (i == 1) {
                dataOutput.writeByte((int) j);
            } else if (i == 2) {
                dataOutput.writeShort((int) j);
            } else if (i == 4) {
                dataOutput.writeInt((int) j);
            } else if (i == 8) {
                dataOutput.writeLong(j);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unsuppoert size ");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
            }
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r10v0, types: [java.util.List, java.util.List<java.lang.String>] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int writeDataItems(DataOutput dataOutput, List<String> list) {
            int i = 4;
            switch (AnonymousClass1.$SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[this.mType.ordinal()]) {
                case 1:
                    if (dataOutput != null) {
                        dataOutput.writeInt(list.size());
                    }
                    int writeOffsets = 4 + writeOffsets(dataOutput, list);
                    int i2 = writeOffsets;
                    for (String bytes : list) {
                        byte[] bytes2 = bytes.getBytes();
                        if (dataOutput != null) {
                            writeAccordingToSize(dataOutput, this.mLengthSize, (long) bytes2.length);
                            for (byte writeByte : bytes2) {
                                dataOutput.writeByte(writeByte);
                            }
                        }
                        i2 += this.mLengthSize + bytes2.length;
                    }
                    return i2;
                case 2:
                    if (dataOutput != null) {
                        dataOutput.writeInt(list.size());
                    }
                    int writeOffsets2 = 4 + writeOffsets(dataOutput, list);
                    Iterator it = list.iterator();
                    int i3 = writeOffsets2;
                    while (it.hasNext()) {
                        byte[] bArr = (byte[]) it.next();
                        if (dataOutput != null) {
                            writeAccordingToSize(dataOutput, this.mLengthSize, (long) bArr.length);
                            dataOutput.write(bArr);
                        }
                        i3 += this.mLengthSize + bArr.length;
                    }
                    return i3;
                case 3:
                    if (dataOutput != null) {
                        dataOutput.writeInt(list.size());
                    }
                    int writeOffsets3 = 4 + writeOffsets(dataOutput, list);
                    Iterator it2 = list.iterator();
                    int i4 = writeOffsets3;
                    while (it2.hasNext()) {
                        short[] sArr = (short[]) it2.next();
                        if (dataOutput != null) {
                            writeAccordingToSize(dataOutput, this.mLengthSize, (long) sArr.length);
                            for (short writeShort : sArr) {
                                dataOutput.writeShort(writeShort);
                            }
                        }
                        i4 += this.mLengthSize + (sArr.length * 2);
                    }
                    return i4;
                case 4:
                    if (dataOutput != null) {
                        dataOutput.writeInt(list.size());
                    }
                    int writeOffsets4 = writeOffsets(dataOutput, list) + 4;
                    Iterator it3 = list.iterator();
                    int i5 = writeOffsets4;
                    while (it3.hasNext()) {
                        int[] iArr = (int[]) it3.next();
                        if (dataOutput != null) {
                            writeAccordingToSize(dataOutput, this.mLengthSize, (long) iArr.length);
                            for (int writeInt : iArr) {
                                dataOutput.writeInt(writeInt);
                            }
                        }
                        i5 += this.mLengthSize + (iArr.length * 4);
                    }
                    return i5;
                case 5:
                    if (dataOutput != null) {
                        dataOutput.writeInt(list.size());
                    }
                    i = 4 + writeOffsets(dataOutput, list);
                    Iterator it4 = list.iterator();
                    while (it4.hasNext()) {
                        long[] jArr = (long[]) it4.next();
                        if (dataOutput != null) {
                            writeAccordingToSize(dataOutput, this.mLengthSize, (long) jArr.length);
                            for (long writeLong : jArr) {
                                dataOutput.writeLong(writeLong);
                            }
                        }
                        i += this.mLengthSize + (jArr.length * 8);
                    }
                    break;
                case 6:
                    if (dataOutput != null) {
                        dataOutput.writeByte(((Byte) list.get(0)).byteValue());
                    }
                    return 1;
                case 7:
                    if (dataOutput != null) {
                        dataOutput.writeShort(((Short) list.get(0)).shortValue());
                    }
                    return 2;
                case 8:
                    if (dataOutput != null) {
                        dataOutput.writeInt(((Integer) list.get(0)).intValue());
                        break;
                    }
                    break;
                case 9:
                    if (dataOutput == null) {
                        return 8;
                    }
                    dataOutput.writeLong(((Long) list.get(0)).longValue());
                    return 8;
                default:
                    return 0;
            }
            return i;
        }

        /* JADX WARNING: Removed duplicated region for block: B:24:0x005c  */
        /* JADX WARNING: Removed duplicated region for block: B:47:0x001b A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int writeOffsets(DataOutput dataOutput, List list) {
            byte b;
            int i;
            int i2;
            int length;
            int i3;
            if (dataOutput == null || this.mOffsetSize == 0 || this.mLengthSize == 0) {
                int size = list.size() * 4;
                int i4 = size;
                int i5 = 0;
                for (Object next : list) {
                    int i6 = AnonymousClass1.$SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[this.mType.ordinal()];
                    if (i6 == 1) {
                        length = ((String) next).getBytes().length;
                    } else if (i6 != 2) {
                        if (i6 == 3) {
                            i2 = ((short[]) next).length;
                            i3 = i2 * 2;
                        } else if (i6 == 4) {
                            i2 = ((int[]) next).length;
                            i3 = i2 * 4;
                        } else if (i6 != 5) {
                            i2 = 0;
                            if (i5 < i2) {
                                i5 = i2;
                            }
                        } else {
                            i2 = ((long[]) next).length;
                            i3 = i2 * 8;
                        }
                        i4 += i3;
                        if (i5 < i2) {
                        }
                    } else {
                        length = ((byte[]) next).length;
                    }
                    i4 += i2;
                    if (i5 < i2) {
                    }
                }
                this.mLengthSize = getSizeOf(i5);
                this.mOffsetSize = getSizeOf(i4 + (this.mLengthSize * list.size()));
            }
            int size2 = this.mOffsetSize * list.size();
            if (dataOutput != null) {
                int i7 = size2;
                for (Object next2 : list) {
                    writeAccordingToSize(dataOutput, this.mOffsetSize, (long) i7);
                    int i8 = AnonymousClass1.$SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[this.mType.ordinal()];
                    if (i8 == 1) {
                        b = this.mLengthSize;
                        i = ((String) next2).getBytes().length;
                    } else if (i8 == 2) {
                        b = this.mLengthSize;
                        i = ((byte[]) next2).length;
                    } else if (i8 == 3) {
                        b = this.mLengthSize;
                        i = ((short[]) next2).length * 2;
                    } else if (i8 == 4) {
                        b = this.mLengthSize;
                        i = ((int[]) next2).length * 4;
                    } else if (i8 == 5) {
                        b = this.mLengthSize;
                        i = ((long[]) next2).length * 8;
                    }
                    i7 += b + i;
                }
            }
            return size2;
        }
    }

    class DescriptionPair {
        /* access modifiers changed from: private */
        public long mDataItemDescriptionOffset;
        /* access modifiers changed from: private */
        public long mIndexGroupDescriptionOffset;

        private DescriptionPair(long j, long j2) {
            this.mIndexGroupDescriptionOffset = j;
            this.mDataItemDescriptionOffset = j2;
        }

        /* synthetic */ DescriptionPair(long j, long j2, AnonymousClass1 r5) {
            this(j, j2);
        }

        /* access modifiers changed from: private */
        public static DescriptionPair read(DataInput dataInput) {
            return new DescriptionPair(dataInput.readLong(), dataInput.readLong());
        }

        /* access modifiers changed from: private */
        public int write(DataOutput dataOutput) {
            if (dataOutput != null) {
                dataOutput.writeLong(this.mIndexGroupDescriptionOffset);
                dataOutput.writeLong(this.mDataItemDescriptionOffset);
            }
            return 16;
        }
    }

    class FileHeader {
        private static final int CURRENT_VERSION = 2;
        private static final byte[] FILE_TAG = {73, 68, 70, 32};
        /* access modifiers changed from: private */
        public int mDataVersion;
        /* access modifiers changed from: private */
        public DescriptionPair[] mDescriptionOffsets;

        private FileHeader(int i, int i2) {
            this.mDescriptionOffsets = new DescriptionPair[i];
            this.mDataVersion = i2;
        }

        /* synthetic */ FileHeader(int i, int i2, AnonymousClass1 r3) {
            this(i, i2);
        }

        /* access modifiers changed from: private */
        public static FileHeader read(DataInput dataInput) {
            byte[] bArr = new byte[FILE_TAG.length];
            for (int i = 0; i < bArr.length; i++) {
                bArr[i] = dataInput.readByte();
            }
            if (!Arrays.equals(bArr, FILE_TAG)) {
                throw new IOException("File tag unmatched, file may be corrupt");
            } else if (dataInput.readInt() == 2) {
                int readInt = dataInput.readInt();
                FileHeader fileHeader = new FileHeader(readInt, dataInput.readInt());
                for (int i2 = 0; i2 < readInt; i2++) {
                    fileHeader.mDescriptionOffsets[i2] = DescriptionPair.read(dataInput);
                }
                return fileHeader;
            } else {
                throw new IOException("File version unmatched, please upgrade your reader");
            }
        }

        /* access modifiers changed from: private */
        public int write(DataOutput dataOutput) {
            byte[] bArr = FILE_TAG;
            int length = bArr.length + 4 + 4 + 4;
            if (dataOutput != null) {
                dataOutput.write(bArr);
                dataOutput.writeInt(2);
                dataOutput.writeInt(this.mDescriptionOffsets.length);
                dataOutput.writeInt(this.mDataVersion);
            }
            for (DescriptionPair access$100 : this.mDescriptionOffsets) {
                length += access$100.write(dataOutput);
            }
            return length;
        }
    }

    class IndexGroupDescriptor {
        int mMaxIndex;
        int mMinIndex;
        long mOffset;

        private IndexGroupDescriptor(int i, int i2, long j) {
            this.mMinIndex = i;
            this.mMaxIndex = i2;
            this.mOffset = j;
        }

        /* synthetic */ IndexGroupDescriptor(int i, int i2, long j, AnonymousClass1 r5) {
            this(i, i2, j);
        }

        /* access modifiers changed from: private */
        public static IndexGroupDescriptor read(DataInput dataInput) {
            return new IndexGroupDescriptor(dataInput.readInt(), dataInput.readInt(), dataInput.readLong());
        }

        /* access modifiers changed from: private */
        public int write(DataOutput dataOutput) {
            if (dataOutput != null) {
                dataOutput.writeInt(this.mMinIndex);
                dataOutput.writeInt(this.mMaxIndex);
                dataOutput.writeLong(this.mOffset);
            }
            return 16;
        }
    }

    interface InputFile extends DataInput {
        void close();

        long getFilePointer();

        void seek(long j);
    }

    public class Reader {
        private InputFile mFile;
        private FileHeader mHeader;
        private IndexData[] mIndexData;

        class IndexData {
            /* access modifiers changed from: private */
            public DataItemDescriptor[] mDataItemDescriptions;
            /* access modifiers changed from: private */
            public Object[][] mDataItems;
            /* access modifiers changed from: private */
            public IndexGroupDescriptor[] mIndexGroupDescriptions;
            /* access modifiers changed from: private */
            public int mSizeOfItems;

            private IndexData() {
            }

            /* synthetic */ IndexData(AnonymousClass1 r1) {
                this();
            }

            static /* synthetic */ int access$912(IndexData indexData, int i) {
                int i2 = indexData.mSizeOfItems + i;
                indexData.mSizeOfItems = i2;
                return i2;
            }
        }

        private Reader(InputStream inputStream) {
            this.mFile = new DataInputStream(inputStream);
            constructFromFile("assets");
        }

        /* synthetic */ Reader(InputStream inputStream, AnonymousClass1 r2) {
            this(inputStream);
        }

        private Reader(String str) {
            this.mFile = new DataInputRandom(new RandomAccessFile(str, "r"));
            constructFromFile(str);
        }

        /* synthetic */ Reader(String str, AnonymousClass1 r2) {
            this(str);
        }

        private void constructFromFile(String str) {
            System.currentTimeMillis();
            try {
                this.mFile.seek(0);
                this.mHeader = FileHeader.read(this.mFile);
                this.mIndexData = new IndexData[this.mHeader.mDescriptionOffsets.length];
                for (int i = 0; i < this.mHeader.mDescriptionOffsets.length; i++) {
                    this.mIndexData[i] = new IndexData(null);
                    this.mFile.seek(this.mHeader.mDescriptionOffsets[i].mIndexGroupDescriptionOffset);
                    int readInt = this.mFile.readInt();
                    this.mIndexData[i].mIndexGroupDescriptions = new IndexGroupDescriptor[readInt];
                    for (int i2 = 0; i2 < readInt; i2++) {
                        this.mIndexData[i].mIndexGroupDescriptions[i2] = IndexGroupDescriptor.read(this.mFile);
                    }
                    this.mFile.seek(this.mHeader.mDescriptionOffsets[i].mDataItemDescriptionOffset);
                    int readInt2 = this.mFile.readInt();
                    this.mIndexData[i].mSizeOfItems = 0;
                    this.mIndexData[i].mDataItemDescriptions = new DataItemDescriptor[readInt2];
                    for (int i3 = 0; i3 < readInt2; i3++) {
                        this.mIndexData[i].mDataItemDescriptions[i3] = DataItemDescriptor.read(this.mFile);
                        IndexData.access$912(this.mIndexData[i], this.mIndexData[i].mDataItemDescriptions[i3].mIndexSize);
                    }
                    this.mIndexData[i].mDataItems = new Object[readInt2][];
                    for (int i4 = 0; i4 < readInt2; i4++) {
                        this.mFile.seek(this.mIndexData[i].mDataItemDescriptions[i4].mOffset);
                        this.mIndexData[i].mDataItems[i4] = this.mIndexData[i].mDataItemDescriptions[i4].readDataItems(this.mFile);
                    }
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }

        private long offset(int i, int i2) {
            IndexGroupDescriptor indexGroupDescriptor;
            int length = this.mIndexData[i].mIndexGroupDescriptions.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    indexGroupDescriptor = null;
                    break;
                }
                int i4 = (length + i3) / 2;
                if (this.mIndexData[i].mIndexGroupDescriptions[i4].mMinIndex <= i2) {
                    if (this.mIndexData[i].mIndexGroupDescriptions[i4].mMaxIndex > i2) {
                        indexGroupDescriptor = this.mIndexData[i].mIndexGroupDescriptions[i4];
                        break;
                    }
                    i3 = i4 + 1;
                } else {
                    length = i4;
                }
            }
            if (indexGroupDescriptor != null) {
                return indexGroupDescriptor.mOffset + ((long) ((i2 - indexGroupDescriptor.mMinIndex) * this.mIndexData[i].mSizeOfItems));
            }
            return -1;
        }

        private Object readSingleDataItem(int i, int i2, int i3) {
            if (this.mIndexData[i].mDataItems[i2][i3] == null) {
                this.mFile.seek(this.mIndexData[i].mDataItemDescriptions[i2].mOffset + 4);
                this.mIndexData[i].mDataItems[i2][i3] = this.mIndexData[i].mDataItemDescriptions[i2].readSingleDataItem(this.mFile, i3);
            }
            return this.mIndexData[i].mDataItems[i2][i3];
        }

        public synchronized void close() {
            if (this.mFile != null) {
                try {
                    this.mFile.close();
                } catch (IOException unused) {
                }
            }
            this.mFile = null;
            this.mHeader = null;
            this.mIndexData = null;
        }

        public synchronized Object get(int i, int i2, int i3) {
            Object obj;
            if (this.mFile == null) {
                throw new IllegalStateException("Get data from a corrupt file");
            } else if (i < 0 || i >= this.mIndexData.length) {
                StringBuilder sb = new StringBuilder();
                sb.append("Kind ");
                sb.append(i);
                sb.append(" out of range[0, ");
                sb.append(this.mIndexData.length);
                sb.append(")");
                throw new IllegalArgumentException(sb.toString());
            } else if (i3 < 0 || i3 >= this.mIndexData[i].mDataItemDescriptions.length) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("DataIndex ");
                sb2.append(i3);
                sb2.append(" out of range[0, ");
                sb2.append(this.mIndexData[i].mDataItemDescriptions.length);
                sb2.append(")");
                throw new IllegalArgumentException(sb2.toString());
            } else {
                System.currentTimeMillis();
                long offset = offset(i, i2);
                Object obj2 = null;
                if (offset < 0) {
                    obj = this.mIndexData[i].mDataItems[i3][0];
                } else {
                    try {
                        this.mFile.seek(offset);
                        for (int i4 = 0; i4 <= i3; i4++) {
                            switch (AnonymousClass1.$SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[this.mIndexData[i].mDataItemDescriptions[i4].mType.ordinal()]) {
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                    int access$1800 = (int) DataItemDescriptor.readAccordingToSize(this.mFile, this.mIndexData[i].mDataItemDescriptions[i4].mIndexSize);
                                    if (i4 != i3) {
                                        break;
                                    } else {
                                        obj2 = readSingleDataItem(i, i3, access$1800);
                                        break;
                                    }
                                case 6:
                                    obj2 = Byte.valueOf(this.mFile.readByte());
                                    break;
                                case 7:
                                    obj2 = Short.valueOf(this.mFile.readShort());
                                    break;
                                case 8:
                                    obj2 = Integer.valueOf(this.mFile.readInt());
                                    break;
                                case 9:
                                    obj2 = Long.valueOf(this.mFile.readLong());
                                    break;
                                default:
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Unknown type ");
                                    sb3.append(this.mIndexData[i].mDataItemDescriptions[i4].mType);
                                    throw new IllegalStateException(sb3.toString());
                            }
                        }
                        obj = obj2;
                    } catch (IOException e) {
                        throw new IllegalStateException("File may be corrupt due to invalid data index size", e);
                    } catch (IOException e2) {
                        throw new IllegalStateException("Seek data from a corrupt file", e2);
                    }
                }
            }
            return obj;
        }

        public synchronized Object[] get(int i, int i2) {
            if (this.mFile == null) {
                throw new IllegalStateException("Get data from a corrupt file");
            } else if (i < 0 || i >= this.mIndexData.length) {
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot get data kind ");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
            } else {
                System.currentTimeMillis();
                long offset = offset(i, i2);
                Object[] objArr = new Object[this.mIndexData[i].mDataItemDescriptions.length];
                if (offset < 0) {
                    for (int i3 = 0; i3 < objArr.length; i3++) {
                        objArr[i3] = this.mIndexData[i].mDataItems[i3][0];
                    }
                    return objArr;
                }
                try {
                    this.mFile.seek(offset);
                    for (int i4 = 0; i4 < objArr.length; i4++) {
                        switch (AnonymousClass1.$SwitchMap$miui$util$DirectIndexedFile$DataItemDescriptor$Type[this.mIndexData[i].mDataItemDescriptions[i4].mType.ordinal()]) {
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                int access$1800 = (int) DataItemDescriptor.readAccordingToSize(this.mFile, this.mIndexData[i].mDataItemDescriptions[i4].mIndexSize);
                                long filePointer = this.mFile.getFilePointer();
                                objArr[i4] = readSingleDataItem(i, i4, access$1800);
                                this.mFile.seek(filePointer);
                                break;
                            case 6:
                                objArr[i4] = Byte.valueOf(this.mFile.readByte());
                                break;
                            case 7:
                                objArr[i4] = Short.valueOf(this.mFile.readShort());
                                break;
                            case 8:
                                objArr[i4] = Integer.valueOf(this.mFile.readInt());
                                break;
                            case 9:
                                objArr[i4] = Long.valueOf(this.mFile.readLong());
                                break;
                            default:
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Unknown type ");
                                sb2.append(this.mIndexData[i].mDataItemDescriptions[i4].mType);
                                throw new IllegalStateException(sb2.toString());
                        }
                    }
                    return objArr;
                } catch (IOException e) {
                    throw new IllegalStateException("File may be corrupt due to invalid data index size", e);
                } catch (IOException e2) {
                    throw new IllegalStateException("Seek data from a corrupt file", e2);
                }
            }
        }

        public int getDataVersion() {
            FileHeader fileHeader = this.mHeader;
            if (fileHeader == null) {
                return -1;
            }
            return fileHeader.mDataVersion;
        }
    }

    protected DirectIndexedFile() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static Builder build(int i) {
        return new Builder(i, null);
    }

    public static Reader open(InputStream inputStream) {
        return new Reader(inputStream, (AnonymousClass1) null);
    }

    public static Reader open(String str) {
        return new Reader(str, (AnonymousClass1) null);
    }
}

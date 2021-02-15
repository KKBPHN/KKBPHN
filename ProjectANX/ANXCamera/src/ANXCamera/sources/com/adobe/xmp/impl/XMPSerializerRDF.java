package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPSchemaRegistry;
import com.adobe.xmp.options.SerializeOptions;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class XMPSerializerRDF {
    private static final int DEFAULT_PAD = 2048;
    private static final String PACKET_HEADER = "<?xpacket begin=\"﻿\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>";
    private static final String PACKET_TRAILER = "<?xpacket end=\"";
    private static final String PACKET_TRAILER2 = "\"?>";
    static final Set RDF_ATTR_QUALIFIER = new HashSet(Arrays.asList(new String[]{XMPConst.XML_LANG, "rdf:resource", "rdf:ID", "rdf:bagID", "rdf:nodeID"}));
    private static final String RDF_RDF_END = "</rdf:RDF>";
    private static final String RDF_RDF_START = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">";
    private static final String RDF_SCHEMA_END = "</rdf:Description>";
    private static final String RDF_SCHEMA_START = "<rdf:Description rdf:about=";
    private static final String RDF_STRUCT_END = "</rdf:Description>";
    private static final String RDF_STRUCT_START = "<rdf:Description";
    private static final String RDF_XMPMETA_END = "</x:xmpmeta>";
    private static final String RDF_XMPMETA_START = "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"";
    private SerializeOptions options;
    private CountOutputStream outputStream;
    private int padding;
    private int unicodeSize = 1;
    private OutputStreamWriter writer;
    private XMPMetaImpl xmp;

    private void addPadding(int i) {
        if (this.options.getExactPacketLength()) {
            int bytesWritten = this.outputStream.getBytesWritten() + (i * this.unicodeSize);
            int i2 = this.padding;
            if (bytesWritten <= i2) {
                this.padding = i2 - bytesWritten;
            } else {
                throw new XMPException("Can't fit into specified packet size", 107);
            }
        }
        this.padding /= this.unicodeSize;
        int length = this.options.getNewline().length();
        int i3 = this.padding;
        if (i3 >= length) {
            int i4 = i3 - length;
            while (true) {
                this.padding = i4;
                int i5 = this.padding;
                int i6 = length + 100;
                if (i5 >= i6) {
                    writeChars(100, ' ');
                    writeNewline();
                    i4 = this.padding - i6;
                } else {
                    writeChars(i5, ' ');
                    writeNewline();
                    return;
                }
            }
        } else {
            writeChars(i3, ' ');
        }
    }

    private void appendNodeValue(String str, boolean z) {
        write(Utils.escapeXML(str, z, true));
    }

    private boolean canBeRDFAttrProp(XMPNode xMPNode) {
        if (!xMPNode.hasQualifier() && !xMPNode.getOptions().isURI() && !xMPNode.getOptions().isCompositeProperty()) {
            if (!XMPConst.ARRAY_ITEM_NAME.equals(xMPNode.getName())) {
                return true;
            }
        }
        return false;
    }

    private void declareNamespace(String str, String str2, Set set, int i) {
        if (str2 == null) {
            QName qName = new QName(str);
            if (qName.hasPrefix()) {
                str = qName.getPrefix();
                XMPSchemaRegistry schemaRegistry = XMPMetaFactory.getSchemaRegistry();
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(":");
                str2 = schemaRegistry.getNamespaceURI(sb.toString());
                declareNamespace(str, str2, set, i);
            } else {
                return;
            }
        }
        if (!set.contains(str)) {
            writeNewline();
            writeIndent(i);
            write("xmlns:");
            write(str);
            write("=\"");
            write(str2);
            write(34);
            set.add(str);
        }
    }

    private void declareUsedNamespaces(XMPNode xMPNode, Set set, int i) {
        if (xMPNode.getOptions().isSchemaNode()) {
            declareNamespace(xMPNode.getValue().substring(0, xMPNode.getValue().length() - 1), xMPNode.getName(), set, i);
        } else if (xMPNode.getOptions().isStruct()) {
            Iterator iterateChildren = xMPNode.iterateChildren();
            while (iterateChildren.hasNext()) {
                declareNamespace(((XMPNode) iterateChildren.next()).getName(), null, set, i);
            }
        }
        Iterator iterateChildren2 = xMPNode.iterateChildren();
        while (iterateChildren2.hasNext()) {
            declareUsedNamespaces((XMPNode) iterateChildren2.next(), set, i);
        }
        Iterator iterateQualifier = xMPNode.iterateQualifier();
        while (iterateQualifier.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) iterateQualifier.next();
            declareNamespace(xMPNode2.getName(), null, set, i);
            declareUsedNamespaces(xMPNode2, set, i);
        }
    }

    private void emitRDFArrayTag(XMPNode xMPNode, boolean z, int i) {
        if (z || xMPNode.hasChildren()) {
            writeIndent(i);
            write(z ? "<rdf:" : "</rdf:");
            String str = xMPNode.getOptions().isArrayAlternate() ? "Alt" : xMPNode.getOptions().isArrayOrdered() ? "Seq" : "Bag";
            write(str);
            String str2 = (!z || xMPNode.hasChildren()) ? ">" : "/>";
            write(str2);
            writeNewline();
        }
    }

    private String serializeAsRDF() {
        if (!this.options.getOmitPacketWrapper()) {
            writeIndent(0);
            write(PACKET_HEADER);
            writeNewline();
        }
        writeIndent(0);
        write(RDF_XMPMETA_START);
        if (!this.options.getOmitVersionAttribute()) {
            write(XMPMetaFactory.getVersionInfo().getMessage());
        }
        write("\">");
        writeNewline();
        writeIndent(1);
        write(RDF_RDF_START);
        writeNewline();
        if (this.options.getUseCompactFormat()) {
            serializeCompactRDFSchemas();
        } else {
            serializePrettyRDFSchemas();
        }
        writeIndent(1);
        write(RDF_RDF_END);
        writeNewline();
        writeIndent(0);
        write(RDF_XMPMETA_END);
        writeNewline();
        String str = "";
        if (this.options.getOmitPacketWrapper()) {
            return str;
        }
        for (int baseIndent = this.options.getBaseIndent(); baseIndent > 0; baseIndent--) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(this.options.getIndent());
            str = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(PACKET_TRAILER);
        String sb3 = sb2.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(sb3);
        sb4.append(this.options.getReadOnlyPacket() ? 'r' : 'w');
        String sb5 = sb4.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(sb5);
        sb6.append(PACKET_TRAILER2);
        return sb6.toString();
    }

    private void serializeCompactRDFArrayProp(XMPNode xMPNode, int i) {
        write(62);
        writeNewline();
        int i2 = i + 1;
        emitRDFArrayTag(xMPNode, true, i2);
        if (xMPNode.getOptions().isArrayAltText()) {
            XMPNodeUtils.normalizeLangArray(xMPNode);
        }
        serializeCompactRDFElementProps(xMPNode, i + 2);
        emitRDFArrayTag(xMPNode, false, i2);
    }

    private boolean serializeCompactRDFAttrProps(XMPNode xMPNode, int i) {
        Iterator iterateChildren = xMPNode.iterateChildren();
        boolean z = true;
        while (iterateChildren.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) iterateChildren.next();
            if (canBeRDFAttrProp(xMPNode2)) {
                writeNewline();
                writeIndent(i);
                write(xMPNode2.getName());
                write("=\"");
                appendNodeValue(xMPNode2.getValue(), true);
                write(34);
            } else {
                z = false;
            }
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0004 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void serializeCompactRDFElementProps(XMPNode xMPNode, int i) {
        boolean z;
        boolean z2;
        Iterator iterateChildren = xMPNode.iterateChildren();
        while (iterateChildren.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) iterateChildren.next();
            if (!canBeRDFAttrProp(xMPNode2)) {
                String name = xMPNode2.getName();
                if (XMPConst.ARRAY_ITEM_NAME.equals(name)) {
                    name = "rdf:li";
                }
                writeIndent(i);
                write(60);
                write(name);
                Iterator iterateQualifier = xMPNode2.iterateQualifier();
                boolean z3 = false;
                boolean z4 = false;
                while (true) {
                    z = true;
                    if (!iterateQualifier.hasNext()) {
                        break;
                    }
                    XMPNode xMPNode3 = (XMPNode) iterateQualifier.next();
                    if (!RDF_ATTR_QUALIFIER.contains(xMPNode3.getName())) {
                        z3 = true;
                    } else {
                        z4 = "rdf:resource".equals(xMPNode3.getName());
                        write(32);
                        write(xMPNode3.getName());
                        write("=\"");
                        appendNodeValue(xMPNode3.getValue(), true);
                        write(34);
                    }
                }
                if (z3) {
                    serializeCompactRDFGeneralQualifier(i, xMPNode2);
                } else {
                    if (!xMPNode2.getOptions().isCompositeProperty()) {
                        Object[] serializeCompactRDFSimpleProp = serializeCompactRDFSimpleProp(xMPNode2);
                        boolean booleanValue = ((Boolean) serializeCompactRDFSimpleProp[0]).booleanValue();
                        z = ((Boolean) serializeCompactRDFSimpleProp[1]).booleanValue();
                        z2 = booleanValue;
                    } else if (xMPNode2.getOptions().isArray()) {
                        serializeCompactRDFArrayProp(xMPNode2, i);
                    } else {
                        z2 = serializeCompactRDFStructProp(xMPNode2, i, z4);
                    }
                    if (z2) {
                        if (z) {
                            writeIndent(i);
                        }
                        write("</");
                        write(name);
                        write(62);
                        writeNewline();
                    }
                }
                z2 = true;
                if (z2) {
                }
            }
        }
    }

    private void serializeCompactRDFGeneralQualifier(int i, XMPNode xMPNode) {
        write(" rdf:parseType=\"Resource\">");
        writeNewline();
        int i2 = i + 1;
        serializePrettyRDFProperty(xMPNode, true, i2);
        Iterator iterateQualifier = xMPNode.iterateQualifier();
        while (iterateQualifier.hasNext()) {
            serializePrettyRDFProperty((XMPNode) iterateQualifier.next(), false, i2);
        }
    }

    private void serializeCompactRDFSchemas() {
        String str;
        writeIndent(2);
        write(RDF_SCHEMA_START);
        writeTreeName();
        HashSet hashSet = new HashSet();
        hashSet.add("xml");
        hashSet.add("rdf");
        Iterator iterateChildren = this.xmp.getRoot().iterateChildren();
        while (iterateChildren.hasNext()) {
            declareUsedNamespaces((XMPNode) iterateChildren.next(), hashSet, 4);
        }
        boolean z = true;
        Iterator iterateChildren2 = this.xmp.getRoot().iterateChildren();
        while (iterateChildren2.hasNext()) {
            z &= serializeCompactRDFAttrProps((XMPNode) iterateChildren2.next(), 3);
        }
        if (!z) {
            write(62);
            writeNewline();
            Iterator iterateChildren3 = this.xmp.getRoot().iterateChildren();
            while (iterateChildren3.hasNext()) {
                serializeCompactRDFElementProps((XMPNode) iterateChildren3.next(), 3);
            }
            writeIndent(2);
            str = "</rdf:Description>";
        } else {
            str = "/>";
        }
        write(str);
        writeNewline();
    }

    private Object[] serializeCompactRDFSimpleProp(XMPNode xMPNode) {
        Boolean bool;
        String str;
        Boolean bool2 = Boolean.TRUE;
        if (xMPNode.getOptions().isURI()) {
            write(" rdf:resource=\"");
            appendNodeValue(xMPNode.getValue(), true);
            str = "\"/>";
        } else if (xMPNode.getValue() == null || xMPNode.getValue().length() == 0) {
            str = "/>";
        } else {
            write(62);
            appendNodeValue(xMPNode.getValue(), false);
            Boolean bool3 = bool2;
            bool2 = Boolean.FALSE;
            bool = bool3;
            return new Object[]{bool, bool2};
        }
        write(str);
        writeNewline();
        bool = Boolean.FALSE;
        return new Object[]{bool, bool2};
    }

    private boolean serializeCompactRDFStructProp(XMPNode xMPNode, int i, boolean z) {
        String str;
        Iterator iterateChildren = xMPNode.iterateChildren();
        boolean z2 = false;
        boolean z3 = false;
        while (iterateChildren.hasNext()) {
            if (canBeRDFAttrProp((XMPNode) iterateChildren.next())) {
                z2 = true;
            } else {
                z3 = true;
            }
            if (z2 && z3) {
                break;
            }
        }
        if (!z || !z3) {
            if (!xMPNode.hasChildren()) {
                str = " rdf:parseType=\"Resource\"/>";
            } else if (!z3) {
                serializeCompactRDFAttrProps(xMPNode, i + 1);
                str = "/>";
            } else {
                if (!z2) {
                    write(" rdf:parseType=\"Resource\">");
                    writeNewline();
                    serializeCompactRDFElementProps(xMPNode, i + 1);
                } else {
                    write(62);
                    writeNewline();
                    int i2 = i + 1;
                    writeIndent(i2);
                    write(RDF_STRUCT_START);
                    serializeCompactRDFAttrProps(xMPNode, i + 2);
                    write(">");
                    writeNewline();
                    serializeCompactRDFElementProps(xMPNode, i2);
                    writeIndent(i2);
                    write("</rdf:Description>");
                    writeNewline();
                }
                return true;
            }
            write(str);
            writeNewline();
            return false;
        }
        throw new XMPException("Can't mix rdf:resource qualifier and element fields", 202);
    }

    /* JADX WARNING: Removed duplicated region for block: B:68:0x01a6  */
    /* JADX WARNING: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void serializePrettyRDFProperty(XMPNode xMPNode, boolean z, int i) {
        String str;
        boolean z2;
        String str2;
        XMPNode xMPNode2 = xMPNode;
        int i2 = i;
        String name = xMPNode.getName();
        if (z) {
            name = "rdf:value";
        } else if (XMPConst.ARRAY_ITEM_NAME.equals(name)) {
            name = "rdf:li";
        }
        writeIndent(i2);
        write(60);
        write(name);
        Iterator iterateQualifier = xMPNode.iterateQualifier();
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        while (true) {
            str = "=\"";
            z2 = true;
            if (!iterateQualifier.hasNext()) {
                break;
            }
            XMPNode xMPNode3 = (XMPNode) iterateQualifier.next();
            if (!RDF_ATTR_QUALIFIER.contains(xMPNode3.getName())) {
                z4 = true;
            } else {
                z5 = "rdf:resource".equals(xMPNode3.getName());
                if (!z) {
                    write(32);
                    write(xMPNode3.getName());
                    write(str);
                    appendNodeValue(xMPNode3.getValue(), true);
                    write(34);
                }
            }
        }
        String str3 = " rdf:parseType=\"Resource\">";
        if (!z4 || z) {
            String str4 = "/>";
            if (!xMPNode.getOptions().isCompositeProperty()) {
                if (xMPNode.getOptions().isURI()) {
                    write(" rdf:resource=\"");
                    appendNodeValue(xMPNode.getValue(), true);
                    str2 = "\"/>";
                } else {
                    if (xMPNode.getValue() != null) {
                        if (!"".equals(xMPNode.getValue())) {
                            write(62);
                            appendNodeValue(xMPNode.getValue(), false);
                            z2 = false;
                            z3 = true;
                            if (z3) {
                                if (z2) {
                                    writeIndent(i2);
                                }
                                write("</");
                                write(name);
                                write(62);
                                writeNewline();
                                return;
                            }
                            return;
                        }
                    }
                    write(str4);
                    writeNewline();
                    if (z3) {
                    }
                }
            } else if (xMPNode.getOptions().isArray()) {
                write(62);
                writeNewline();
                int i3 = i2 + 1;
                emitRDFArrayTag(xMPNode2, true, i3);
                if (xMPNode.getOptions().isArrayAltText()) {
                    XMPNodeUtils.normalizeLangArray(xMPNode);
                }
                Iterator iterateChildren = xMPNode.iterateChildren();
                while (iterateChildren.hasNext()) {
                    serializePrettyRDFProperty((XMPNode) iterateChildren.next(), false, i2 + 2);
                }
                emitRDFArrayTag(xMPNode2, false, i3);
            } else if (z5) {
                Iterator iterateChildren2 = xMPNode.iterateChildren();
                while (iterateChildren2.hasNext()) {
                    XMPNode xMPNode4 = (XMPNode) iterateChildren2.next();
                    if (canBeRDFAttrProp(xMPNode4)) {
                        writeNewline();
                        writeIndent(i2 + 1);
                        write(32);
                        write(xMPNode4.getName());
                        write(str);
                        appendNodeValue(xMPNode4.getValue(), true);
                        write(34);
                    } else {
                        throw new XMPException("Can't mix rdf:resource and complex fields", 202);
                    }
                }
                write(str4);
                writeNewline();
                if (z3) {
                }
            } else if (!xMPNode.hasChildren()) {
                str2 = " rdf:parseType=\"Resource\"/>";
            } else {
                write(str3);
                writeNewline();
                Iterator iterateChildren3 = xMPNode.iterateChildren();
                while (iterateChildren3.hasNext()) {
                    serializePrettyRDFProperty((XMPNode) iterateChildren3.next(), false, i2 + 1);
                }
            }
            write(str2);
            writeNewline();
            if (z3) {
            }
        } else if (!z5) {
            write(str3);
            writeNewline();
            int i4 = i2 + 1;
            serializePrettyRDFProperty(xMPNode2, true, i4);
            Iterator iterateQualifier2 = xMPNode.iterateQualifier();
            while (iterateQualifier2.hasNext()) {
                XMPNode xMPNode5 = (XMPNode) iterateQualifier2.next();
                if (!RDF_ATTR_QUALIFIER.contains(xMPNode5.getName())) {
                    serializePrettyRDFProperty(xMPNode5, false, i4);
                }
            }
        } else {
            throw new XMPException("Can't mix rdf:resource and general qualifiers", 202);
        }
        z3 = true;
        if (z3) {
        }
    }

    private void serializePrettyRDFSchema(XMPNode xMPNode) {
        writeIndent(2);
        write(RDF_SCHEMA_START);
        writeTreeName();
        HashSet hashSet = new HashSet();
        hashSet.add("xml");
        hashSet.add("rdf");
        declareUsedNamespaces(xMPNode, hashSet, 4);
        write(62);
        writeNewline();
        Iterator iterateChildren = xMPNode.iterateChildren();
        while (iterateChildren.hasNext()) {
            serializePrettyRDFProperty((XMPNode) iterateChildren.next(), false, 3);
        }
        writeIndent(2);
        write("</rdf:Description>");
        writeNewline();
    }

    private void serializePrettyRDFSchemas() {
        if (this.xmp.getRoot().getChildrenLength() > 0) {
            Iterator iterateChildren = this.xmp.getRoot().iterateChildren();
            while (iterateChildren.hasNext()) {
                serializePrettyRDFSchema((XMPNode) iterateChildren.next());
            }
            return;
        }
        writeIndent(2);
        write(RDF_SCHEMA_START);
        writeTreeName();
        write("/>");
        writeNewline();
    }

    private void write(int i) {
        this.writer.write(i);
    }

    private void write(String str) {
        this.writer.write(str);
    }

    private void writeChars(int i, char c) {
        while (i > 0) {
            this.writer.write(c);
            i--;
        }
    }

    private void writeIndent(int i) {
        for (int baseIndent = this.options.getBaseIndent() + i; baseIndent > 0; baseIndent--) {
            this.writer.write(this.options.getIndent());
        }
    }

    private void writeNewline() {
        this.writer.write(this.options.getNewline());
    }

    private void writeTreeName() {
        write(34);
        String name = this.xmp.getRoot().getName();
        if (name != null) {
            appendNodeValue(name, true);
        }
        write(34);
    }

    /* access modifiers changed from: protected */
    public void checkOptionsConsistence() {
        if (this.options.getEncodeUTF16BE() || this.options.getEncodeUTF16LE()) {
            this.unicodeSize = 2;
        }
        if (!this.options.getExactPacketLength()) {
            if (this.options.getReadOnlyPacket()) {
                if (this.options.getOmitPacketWrapper() || this.options.getIncludeThumbnailPad()) {
                    throw new XMPException("Inconsistent options for read-only packet", 103);
                }
            } else if (!this.options.getOmitPacketWrapper()) {
                if (this.padding == 0) {
                    this.padding = this.unicodeSize * 2048;
                }
                if (this.options.getIncludeThumbnailPad() && !this.xmp.doesPropertyExist(XMPConst.NS_XMP, "Thumbnails")) {
                    this.padding += this.unicodeSize * 10000;
                    return;
                }
                return;
            } else if (this.options.getIncludeThumbnailPad()) {
                throw new XMPException("Inconsistent options for non-packet serialize", 103);
            }
            this.padding = 0;
        } else if (!this.options.getOmitPacketWrapper() && !this.options.getIncludeThumbnailPad()) {
            if (((this.unicodeSize - 1) & this.options.getPadding()) != 0) {
                throw new XMPException("Exact size must be a multiple of the Unicode element", 103);
            }
        } else {
            throw new XMPException("Inconsistent options for exact size serialize", 103);
        }
    }

    public void serialize(XMPMeta xMPMeta, OutputStream outputStream2, SerializeOptions serializeOptions) {
        try {
            this.outputStream = new CountOutputStream(outputStream2);
            this.writer = new OutputStreamWriter(this.outputStream, serializeOptions.getEncoding());
            this.xmp = (XMPMetaImpl) xMPMeta;
            this.options = serializeOptions;
            this.padding = serializeOptions.getPadding();
            this.writer = new OutputStreamWriter(this.outputStream, serializeOptions.getEncoding());
            checkOptionsConsistence();
            String serializeAsRDF = serializeAsRDF();
            this.writer.flush();
            addPadding(serializeAsRDF.length());
            write(serializeAsRDF);
            this.writer.flush();
            this.outputStream.close();
        } catch (IOException unused) {
            throw new XMPException("Error writing to the OutputStream", 0);
        }
    }
}

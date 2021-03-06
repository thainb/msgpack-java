package org.msgpack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.msgpack.MessagePack;
import org.msgpack.value.Value;
import org.msgpack.packer.Packer;
import org.msgpack.packer.BufferPacker;
import org.msgpack.unpacker.Unpacker;
import org.msgpack.unpacker.BufferUnpacker;

import org.junit.Test;

public class TestSimplePackable {
    // all files are REQUIRED
    public static class Sample01 implements MessagePackable {
        public String f0;
        public int[] f1;
        public List<String> f2;

        public Sample01() { }

        public void writeTo(Packer pk) throws IOException {
            pk.writeArrayBegin(3);
                pk.writeString(f0);
                pk.writeArrayBegin(f1.length);
                    for(int e : f1) {
                        pk.writeInt(e);
                    }
                pk.writeArrayEnd();
                pk.writeArrayBegin(f2.size());
                    for(String e : f2) {
                        pk.writeString(e);
                    }
                pk.writeArrayEnd();
            pk.writeArrayEnd();
        }

        public void readFrom(Unpacker u) throws IOException {
            u.readArrayBegin();
                f0 = u.readString();
                int nf1 = u.readArrayBegin();
                    f1 = new int[nf1];
                    for(int i=0; i < nf1; i++) {
                        f1[i] = u.readInt();
                    }
                u.readArrayEnd();
                int nf2 = u.readArrayBegin();
                    f2 = new ArrayList<String>(nf2);
                    for(int i=0; i < nf2; i++) {
                        f2.add(u.readString());
                    }
                u.readArrayEnd();
            u.readArrayEnd();
        }
    }

    @Test
    public void testSample01() throws IOException {
        Sample01 a = new Sample01();
        a.f0 = "aaa";
        a.f1 = new int[3];
        a.f1[0] = 1010;
        a.f1[1] = 2020;
        a.f1[2] = 3030;
        a.f2 = new ArrayList<String>();
        a.f2.add("xx");
        a.f2.add("yy");

        BufferPacker pk = new BufferPacker();
        a.writeTo(pk);

        byte[] raw = pk.toByteArray();

        BufferUnpacker u = new BufferUnpacker().wrap(raw);
        Sample01 b = new Sample01();
        b.readFrom(u);

        assertEquals(a.f0, b.f0);
        assertArrayEquals(a.f1, b.f1);
        assertEquals(a.f2, b.f2);
    }
}


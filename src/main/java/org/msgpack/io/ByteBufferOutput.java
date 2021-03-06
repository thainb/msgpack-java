//
// MessagePack for Java
//
// Copyright (C) 2009-2011 FURUHASHI Sadayuki
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package org.msgpack.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.BufferOverflowException;

public class ByteBufferOutput implements Output {
    public static interface ExpandBufferCallback {
        ByteBuffer call(ByteBuffer buffer, int len) throws IOException;
    }

    private ByteBuffer buffer;
    private ExpandBufferCallback callback;

    public ByteBufferOutput(ByteBuffer buffer) {
        this(buffer, null);
    }

    public ByteBufferOutput(ByteBuffer buffer, ExpandBufferCallback callback) {
        this.buffer = buffer;
        this.callback = callback;
    }

    private void reserve(int len) throws IOException {
        if(len <= buffer.remaining()) {
            return;
        }
        if(callback == null) {
            throw new BufferOverflowException();
        }
        buffer = callback.call(buffer, len);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        reserve(len);
        buffer.put(b, off, len);
    }

    public void writeByte(byte v) throws IOException {
        reserve(1);
        buffer.put(v);
    }

    public void writeShort(short v) throws IOException {
        reserve(2);
        buffer.putShort(v);
    }

    public void writeInt(int v) throws IOException {
        reserve(4);
        buffer.putInt(v);
    }

    public void writeLong(long v) throws IOException {
        reserve(8);
        buffer.putLong(v);
    }

    public void writeFloat(float v) throws IOException {
        reserve(4);
        buffer.putFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        reserve(8);
        buffer.putDouble(v);
    }

    public void writeByteAndByte(byte b, byte v) throws IOException {
        reserve(2);
        buffer.put(b);
        buffer.put(v);
    }

    public void writeByteAndShort(byte b, short v) throws IOException {
        reserve(3);
        buffer.put(b);
        buffer.putShort(v);
    }

    public void writeByteAndInt(byte b, int v) throws IOException {
        reserve(5);
        buffer.put(b);
        buffer.putInt(v);
    }

    public void writeByteAndLong(byte b, long v) throws IOException {
        reserve(9);
        buffer.put(b);
        buffer.putLong(v);
    }

    public void writeByteAndFloat(byte b, float v) throws IOException {
        reserve(5);
        buffer.put(b);
        buffer.putFloat(v);
    }

    public void writeByteAndDouble(byte b, double v) throws IOException {
        reserve(9);
        buffer.put(b);
        buffer.putDouble(v);
    }

    public void flush() throws IOException {
    }
}


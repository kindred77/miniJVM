package com.kindred.mir.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class MyRandomAccessFile extends RandomAccessFile {

    public MyRandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

    public final void writeShortLE(short v) throws IOException {
		write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
	}

    public final void writeUnsignedShortLE(int v) throws IOException {
		write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
	}

    public final void writeCharLE(short v) throws IOException {
		write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
	}

    public final void writeIntLE(int v) throws IOException {
        write((v >>>  0) & 0xFF);
        write((v >>>  8) & 0xFF);
        write((v >>> 16) & 0xFF);
        write((v >>> 24) & 0xFF);
    }

    public final void writeLongLE(long v) throws IOException {
        write((int)(v >>>  0) & 0xFF);
        write((int)(v >>>  8) & 0xFF);
        write((int)(v >>> 16) & 0xFF);
        write((int)(v >>> 24) & 0xFF);
        write((int)(v >>> 32) & 0xFF);
        write((int)(v >>> 40) & 0xFF);
        write((int)(v >>> 48) & 0xFF);
        write((int)(v >>> 56) & 0xFF);
    }

    public final void writeFloatLE(float v) throws IOException {
        writeIntLE(Float.floatToIntBits(v));
    }

    public final void writeDoubleLE(double v) throws IOException {
        writeLongLE(Double.doubleToLongBits(v));
    }

    public final short readShortLE() throws IOException {
        // byte[] bytes = new byte[2];
        // readFully(bytes);
        // short num = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        // return num;

		int ch1 = this.read();
		int ch2 = this.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short) ((ch2 << 8) + (ch1 << 0));
	}

    public final int readUnsignedShortLE() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (ch2 << 8) + (ch1 << 0);
	}

    public final char readCharLE() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (char) ((ch2 << 8) + (ch1 << 0));
	}

    public final int readIntLE() throws IOException {
        // byte[] bytes = new byte[4];
        // readFully(bytes);
        // int num = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        // return num;

		int ch1 = this.read();
		int ch2 = this.read();
		int ch3 = this.read();
		int ch4 = this.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
	}

    public final long readUnsignedIntLE() throws IOException {
		return ((long) (readIntLE()) & 0xFFFFFFFFL);
	}

    public final long readLongLE() throws IOException {
		return ((long) (readIntLE()) & 0xFFFFFFFFL) + (readIntLE() << 32);
	}

    public final float readFloatLE() throws IOException {
		return Float.intBitsToFloat(readIntLE());
	}

    public final double readDoubleLE() throws IOException {
		return Double.longBitsToDouble(readLongLE());
	}

}
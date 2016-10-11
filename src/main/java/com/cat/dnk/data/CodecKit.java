package com.cat.dnk.data;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class CodecKit {

	private static byte getByteIndex(int a, int i) {
		byte[] b = new byte[4];
		b[3] = (byte) (a & 0xff);
		b[2] = (byte) (a >> 8 & 0xff);
		b[1] = (byte) (a >> 16 & 0xff);
		b[0] = (byte) (a >> 24 & 0xff);
		return b[i];
	}

	private static byte[] getBytesByInt(int i) {
		byte[] bytes = new byte[4];
		for (int k = 0; k < bytes.length; k++) {
			bytes[k] = (byte) (i >> ((3 - k) << 3) & 0xff);
		}
		return bytes;
	}

	private static byte[] getBytesByTiny(int i) {
		byte[] bytes = new byte[2];
		/*bytes[0] = getBytesByInt(i)[2];
		bytes[1] = getBytesByInt(i)[3];*/
		bytes[0] = (byte) (i >> 8 & 0xff);
		bytes[1] = (byte) (i & 0xff);
		return bytes;
	}

	private static int verify(byte[] bytes) {
		int count = bytes.length;
		for (int i = 0; i < bytes.length; i++) {
			count += bytes[i] & 0xff;
		}
		return count;
	}

	private static byte[] getCheckBytes(int length, byte[] tempArray) {
		int totalValue = 0 + length;
		for (int i = 0; i < tempArray.length; i++) {
			totalValue = (totalValue + (tempArray[i] & 0xff));
		}

		byte[] targets = new byte[2];
		targets[0] = (byte) ((totalValue >> 8) & 0xff);// 次低位
		targets[1] = (byte) (totalValue & 0xff);// 最低位
		return targets;
	}

	public static byte[] encode(String cmd) {
		byte[] data = cmd.getBytes();
		byte[] result = new byte[data.length + 8];

		result[0] = (byte) 0x5A;
		result[1] = (byte) 0xA5;

		int length = data.length + 4;
		result[2] = getByteIndex(length, 2);
		result[3] = getByteIndex(length, 3);
		System.arraycopy(data, 0, result, 4, data.length);

		byte[] checkBytes = getCheckBytes(length, data);

		result[result.length - 4] = checkBytes[0];
		result[result.length - 3] = checkBytes[1];

		result[result.length - 2] = (byte) 0xA5;
		result[result.length - 1] = 0x5A;

		return result;
	}

	public static byte[] encode3(String cmd) {
		byte[] data = cmd.getBytes();

		byte[] result = new byte[data.length + 8];

		int index = 0;
		result[index++] = 0x5A;
		result[index++] = (byte) 0xA5;

		byte[] lengthBytes = getBytesByTiny(data.length + 4);
		result[index++] = lengthBytes[0];
		result[index++] = lengthBytes[1];

		System.arraycopy(data, 0, result, index, data.length);

		index += data.length;
		byte[] verifyArray = getBytesByTiny(verify(data));
		result[index++] = verifyArray[0];
		result[index++] = verifyArray[1];

		result[index++] = (byte) 0xA5;
		result[index] = 0x5A;
		return result;
	}

	public static byte[] encode2(String cmd) {
		byte[] data = cmd.getBytes();
		ByteBuf buffer = Unpooled.buffer(data.length + 8);

		//header
		buffer.writeByte(0x5A);
		buffer.writeByte(0xA5);

		//length
		byte[] length = getBytesByTiny(data.length + 4);
		buffer.writeBytes(length);

		//data
		buffer.writeBytes(data);

		//verify
		int verify = verify(data);
		buffer.writeBytes(getBytesByTiny(verify));

		//footer
		buffer.writeByte(0xA5);
		buffer.writeByte(0x5A);

		return buffer.array();
	}

	public static void main(String[] args) {
		String s = "action";
		byte[] r1 = encode(s);
		test(r1);

		byte[] r2 = encode2(s);
		test(r2);

		byte[] r3 = encode3(s);
		test(r3);

	}

	private static void test(byte[] bs) {
		for (byte b : bs) {
			System.out.print(b + " ");
		}
		System.out.println();
	}

}

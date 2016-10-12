package com.cat.dnk.data;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EncoderKit {

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

	//验证校验码
	public static boolean validateVerify(byte[] data, byte[] verifyArr) {
		int length = 2;
		byte[] result = getBytesByTiny(verify(data));
		if (result.length != length || verifyArr.length != length) {
			return false;
		}
		for (int i = 0; i < length; i++) {
			if (data[i] != verifyArr[i]) {
				return false;
			}
		}
		return true;
	}

	public static byte[] encode(String cmd) {
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

}

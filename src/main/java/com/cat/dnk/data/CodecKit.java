package com.cat.dnk.data;

public class CodecKit {

	private static final int MARK = 0xff;
	private static final String HEXES = "0123456789ABCDEF";

//	@Deprecated
//	public static int byteArrayToInt2(byte[] bytes) {
//		if (bytes.length > 4) {
//			throw new RuntimeException("byte array too long.");
//		}
//		return bytes[3] & MARK | (bytes[2] & MARK) << 8 | (bytes[1] & MARK) << 16 | (bytes[0] & MARK) << 24;
//	}
//
//	public static byte[] intToByteArray2(int i) {
//		return new byte[]{(byte) ((i >> 24) & MARK), (byte) ((i >> 16) & MARK), (byte) ((i >> 8) & MARK), (byte) (i & MARK)};
//	}

	public static int byteArrayToInt(byte[] bytes) {
		if (bytes.length > 4) {
			throw new RuntimeException("byte array too long.");
		}
		int result = 0, offset = 0;
		for (int i = bytes.length - 1; i >= 0; i--) {
			result |= (bytes[i] & MARK) << (offset++ << 3);
		}
		return result;
	}

	private static byte[] intToByteArray(int i) {
		byte[] bytes = new byte[4];
		for (int k = 0; k < bytes.length; k++) {
			bytes[k] = (byte) (i >> ((3 - k) << 3) & MARK);
		}
		return bytes;
	}

	public static String byteArrayToHex(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			builder.append(String.format("%02x", b & MARK));
		}
		return builder.toString();
	}

//	public static String byteArrayToHex2(byte[] bytes) {
//		if (bytes == null) {
//			return null;
//		}
//		StringBuilder builder = new StringBuilder(bytes.length * 2);
//		for (byte b : bytes) {
//			builder.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
//		}
//		return builder.toString();
//	}
//
//	public static String byteArrayToHex3(byte[] bytes) {
//		char[] hexArray = HEXES.toCharArray();
//		char[] hexChars = new char[bytes.length * 2];
//		for (int i = 0; i < bytes.length; i++) {
//			int v = bytes[i] & MARK;
//			hexChars[i * 2] = hexArray[v >>> 4];
//			hexChars[i * 2 + 1] = hexArray[v & 0x0F];
//		}
//		return new String(hexChars);
//	}

	//TODO
	public static String formatHex(String s) {
		return null;
	}

}

package com.cat.dnk.data;

public class CodecKitTest {

	public static void main(String[] args) {
//		int min = -128, max = 127;
//		for (int a = min; a < max; a++) {
//			for (int b = min; b < max; b++) {
//				for (int c = min; c < max; c++) {
//					for (int d = min; d < max; d++) {
//						byte[] bytes = new byte[]{(byte) a, (byte) b, (byte) c, (byte) d};
//						if (byteArrayToInt(bytes) != byteArrayToInt2(bytes)) {
//							System.err.println("wrong.");
//						}
//					}
//				}
//			}
//		}

//		byte[] bs = new byte[]{0x3b, 0x19};
//		System.out.println(byteArrayToHex2(bs));
//		System.out.println(byteArrayToHex(bs));
//		System.out.println(byteArrayToHex3(bs));

		for (int i = 40; i < 127; i++) {
			System.out.print(i + ":");
			System.out.println((char) (byte) i);
		}
	}
}
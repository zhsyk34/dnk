package com.cat.dnk.data;

public class CodecKit {

    private static final int MARK = 0xff;

    public static int byteArrayToInt(byte[] bytes) {
        return bytes[3] & MARK |
                (bytes[2] & MARK) << 8 |
                (bytes[1] & MARK) << 16 |
                (bytes[0] & MARK) << 24;
    }

    public static int byteArrayToInt2(byte[] bytes) {
        if (bytes.length > 4) {
            throw new RuntimeException("byte array too long.");
        }
        int result = 0, offset = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            System.out.println("byte>>>" + bytes[i]);
            result |= (bytes[i] & MARK) << (offset++ << 3);
            System.out.println(">>>" + result);
        }
        return result;
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((i >> 24) & MARK), (byte) ((i >> 16) & MARK), (byte) ((i >> 8) & MARK), (byte) (i & MARK)};
    }

    private static byte[] intToByteArray2(int i) {
        byte[] bytes = new byte[4];
        for (int k = 0; k < bytes.length; k++) {
            bytes[k] = (byte) (i >> ((3 - k) << 3) & 0xff);
        }
        return bytes;
    }

    public static void main(String[] args) {
        int min = -128, max = 127;
        for (int a = min; a < max; a++) {
            for (int b = min; b < max; b++) {
                for (int c = min; c < max; c++) {
                    for (int d = min; d < max; d++) {
                        byte[] bytes = new byte[]{(byte) a, (byte) b, (byte) c, (byte) d};
                        if (byteArrayToInt(bytes) != byteArrayToInt2(bytes)) {
                            System.err.println("wrong.");
                        }
                    }
                }
            }
        }
    }

}

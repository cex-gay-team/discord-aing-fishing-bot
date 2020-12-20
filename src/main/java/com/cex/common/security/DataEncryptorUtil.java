package com.cex.common.security;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

public class DataEncryptorUtil {
    private static final String HEX_PREFIX = "0";

    private static final int AND_BIT = 0xFF;

    private static final int HEX_NUMBER_SIZE = 2;

    private static final int NUMBER_SYSTEM = 16;

    private static final int START_INDEX = 0;

    public static byte[] getDecryptByteData(String data) {
        byte[] decipherBytes = new byte[data.length() / HEX_NUMBER_SIZE];

        int startIndex = START_INDEX;

        for (String hexNumber : Splitter.fixedLength(HEX_NUMBER_SIZE).split(data)) {
            decipherBytes[startIndex++] = (byte)Integer.parseInt(hexNumber, NUMBER_SYSTEM);
        }

        return decipherBytes;
    }

    public static String getEncryptStringData(byte[] byteResult) {
        StringBuilder cipheringTextBuilder = new StringBuilder();

        for (byte byteCiphered : byteResult) {
            String hex = StringUtils.leftPad(Integer.toHexString(byteCiphered & AND_BIT), HEX_NUMBER_SIZE, HEX_PREFIX);
            cipheringTextBuilder.append(hex);
        }

        return cipheringTextBuilder.toString();
    }
}

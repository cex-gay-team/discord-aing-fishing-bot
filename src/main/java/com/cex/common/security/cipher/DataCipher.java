package com.cex.common.security.cipher;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.util.Objects;

public class DataCipher {
    private Cipher cipher;

    private DataCipher(Cipher cipher) {
        this.cipher = cipher;
    }

    public static DataCipher newInstance(Cipher cipher) {
        if(Objects.isNull(cipher)) {
            throw new IllegalStateException("");
        }
        return new DataCipher(cipher);
    }

    public byte[] doFinal(byte[] input) throws GeneralSecurityException {
        return cipher.doFinal(input);
    }

}

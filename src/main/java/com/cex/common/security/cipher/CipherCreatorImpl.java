package com.cex.common.security.cipher;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;

@Component
public class CipherCreatorImpl implements CipherCreator {
    @Override
    public DataCipher createEncryptCipher(String key) throws Exception {
        return createCipher(key, Cipher.ENCRYPT_MODE);
    }

    @Override
    public DataCipher createDecryptCipher(String key) throws Exception {
        return createCipher(key, Cipher.DECRYPT_MODE);
    }

    private DataCipher createCipher(String securityKey, int mode) throws Exception {
        byte[] securityKeyByte = getSecurityKeyBytes(securityKey);

        Key keySpec = new SecretKeySpec(securityKeyByte, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(securityKeyByte);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, keySpec, ivParameterSpec);

        return DataCipher.newInstance(cipher);
    }

    private byte[] getSecurityKeyBytes(String securityKey) throws UnsupportedEncodingException {
        byte[] securityKeyBytes = securityKey.getBytes("UTF-8");
        int securityKeyLength = securityKeyBytes.length > 16 ? 16 : securityKeyBytes.length;
        byte[] keyBytes = new byte[16];
        System.arraycopy(keyBytes, 0, securityKeyBytes, 0, securityKeyLength);

        return keyBytes;
    }
}

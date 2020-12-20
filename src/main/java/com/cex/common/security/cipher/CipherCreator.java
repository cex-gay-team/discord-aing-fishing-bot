package com.cex.common.security.cipher;

public interface CipherCreator {
    DataCipher createEncryptCipher(String key) throws Exception;
    DataCipher createDecryptCipher(String key)throws Exception;
}

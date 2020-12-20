package com.cex.common.security.dataCryptor;

public interface DataCryptor {
    String encryptData(String data);
    String decryptData(String data);
}

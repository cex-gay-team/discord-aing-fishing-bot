package com.cex.common.security.dataCryptor;

import com.cex.common.security.cipher.CipherCreator;
import com.cex.common.security.cipher.DataCipher;
import com.cex.common.security.DataEncryptorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mysqlDataCryptor")
@Slf4j
public class MysqlDataCryptorImpl implements DataCryptor {
    private static final String CHARSET_NAME = "UTF-8";

    @Value("${mysql.db.key}")
    private String mysqlDbKey;

    @Autowired
    private CipherCreator cipherCreator;

    @Override
    public String encryptData(String data) {
        String result = data;
        try {
            DataCipher dataCipher = cipherCreator.createEncryptCipher(mysqlDbKey);
            byte[] byteResult = dataCipher.doFinal(data.getBytes(CHARSET_NAME));
            result = DataEncryptorUtil.getEncryptStringData(byteResult);
        } catch (Exception exception) {
            log.error("Mysql DataCryptor encrypt fail data : {0}", data ,exception);
        }

        return result;
    }

    @Override
    public String decryptData(String data) {
        String result = data;
        try {
            byte[] decipherBytes = DataEncryptorUtil.getDecryptByteData(data);
            DataCipher dataCipher = cipherCreator.createDecryptCipher(mysqlDbKey);
            result = new String(dataCipher.doFinal(decipherBytes), CHARSET_NAME);
        } catch (Exception exception) {
            log.error("Mysql DataCryptor decrypt fail data : {0}", data ,exception);
        }

        return result;
    }
}

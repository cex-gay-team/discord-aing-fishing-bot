package com.cex.config;

import com.cex.common.security.cipher.CipherCreator;
import com.cex.common.security.cipher.CipherCreatorImpl;
import com.cex.common.security.cipher.DataCipher;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class FishingBotWebApplicationConfig implements WebApplicationInitializer {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String HEX_PREFIX = "0";

    private static final int AND_BIT = 0xFF;

    private static final int HEX_NUMBER_SIZE = 2;

    private static final int NUMBER_SYSTEM = 16;

    private static final int START_INDEX = 0;

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(FishingBotWebContext.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);

        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(FishingBotRootContext.class);

        ContextLoaderListener listener = new ContextLoaderListener(rootContext);
        servletContext.addListener(listener);

        FilterRegistration.Dynamic filter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        filter.setInitParameter("encoding", "UTF-8");
        filter.addMappingForServletNames(null, false, "dispatcher");
//TODO 나중에 지울거임. 일단 접속정보들 암호화 하는데 로컬에서 돌려서 넣을려고함
// tc 짜서 암호화 해도 되는데 귀찮아서 여기에 구현해둠.
//        try {
//            decruptTest("");
//
//
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
    }

    private void decruptTest(String data) throws Exception {
        CipherCreator cipherCreator = new CipherCreatorImpl();
        DataCipher dataCipher = cipherCreator.createEncryptCipher("cexcex");

        byte[] byteResult = dataCipher.doFinal(data.getBytes(CHARSET_NAME));

        StringBuilder cipheringTextBuilder = new StringBuilder();

        for (byte byteCiphered : byteResult) {
            String hex = StringUtils.leftPad(Integer.toHexString(byteCiphered & AND_BIT), HEX_NUMBER_SIZE, HEX_PREFIX);
            cipheringTextBuilder.append(hex);
        }

        String result = cipheringTextBuilder.toString();
        byte[] decipherBytes = new byte[result.length() / HEX_NUMBER_SIZE]; //str to bytes

        int startIndex = START_INDEX;

        for (String hexNumber : Splitter.fixedLength(HEX_NUMBER_SIZE).split(result)) {
            decipherBytes[startIndex++] = (byte) Integer.parseInt(hexNumber, NUMBER_SYSTEM);
        }

        DataCipher dataCipher2 = cipherCreator.createDecryptCipher("cexcex");
        String decruptResult = new String(dataCipher2.doFinal(decipherBytes), CHARSET_NAME);

    }
}

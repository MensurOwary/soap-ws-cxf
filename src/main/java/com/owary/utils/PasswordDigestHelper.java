package com.owary.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import org.apache.wss4j.dom.message.token.UsernameToken;
import org.apache.xml.security.utils.XMLUtils;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class PasswordDigestHelper {

    public static String getPass() throws NoSuchAlgorithmException {
        String oPass = "admin";
        String oTime = ISO_INSTANT.format(Instant.now());
        String oNonce = getNonce();
        String s = UsernameToken.doPasswordDigest(oNonce, oTime, oPass);
        System.out.println(s);
        System.out.println(oNonce);
        System.out.println(oTime);
        String template = "" +
                "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <Header>\n" +
                "        <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" mustUnderstand=\"1\">\n" +
                "            <wsse:UsernameToken>\n" +
                "                  <wsse:Username>%s</wsse:Username>\n" +
                "                  <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">%s</wsse:Password>\n" +
                "                  <wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">%s</wsse:Nonce>\n" +
                "                  <Created xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">%s</Created>\n" +
                "            </wsse:UsernameToken>\n" +
                "        </wsse:Security>\n" +
                "    </Header>\n" +
                "    <Body>\n" +
                "        <retrieveAll xmlns=\"http://endpoints.owary.com/\"/>\n" +
                "    </Body>\n" +
                "</Envelope>";
        String admin = String.format(template, "admin", s, oNonce, oTime);
        System.out.println(admin);
        return s;
    }

    private static String getNonce() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(System.currentTimeMillis());
        byte[] nonceBytes = new byte[16];
        random.nextBytes(nonceBytes);
        return XMLUtils.encodeToString(nonceBytes);
    }

}

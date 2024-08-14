package com.lime.limeEduApi.framework.common.encrypt;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512 {
    public static String encodeSha512(String planeText) {
        String encodingText = "";
        try {
            // SHA-512, SHA-256, SHA1 등 다양한 방식으로 활용
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(planeText.getBytes(StandardCharsets.UTF_8));
            encodingText = DatatypeConverter.printBase64Binary(md.digest());
        } catch (NoSuchAlgorithmException e) {
            // throws가 싫어서 RuntimeException을 사용
            throw new RuntimeException(e);
        }
        return encodingText;
    }
}

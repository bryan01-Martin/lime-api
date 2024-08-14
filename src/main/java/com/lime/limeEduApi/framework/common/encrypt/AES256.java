package com.lime.limeEduApi.framework.common.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES256 {
    public static String alg = "AES/CBC/PKCS5Padding";
    private static final String key = "01234567890123456789012345678901";
    private static final String iv = key.substring(0, 16); // 16byte

    public static String encrypt(String text) throws Exception {
        if (text.isEmpty() || text.equals("")) {
            return text;
        }

        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String cipherText) throws Exception {
        if (cipherText.isEmpty() || cipherText.equals("")) {
            return cipherText;
        }
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }

    public static String decryptArr(String str, String delimiter) throws Exception{
        return decryptArr(str,delimiter,",");
    }

    public static String decryptArr(String str, String delimiter, String toDelimiter) throws Exception{
        String result = "";
        if(str == null || str.length() == 0 || str.indexOf(delimiter.replaceAll("\\\\","")) < 0)
            return result;
        String[] arr = str.split(delimiter);

        if(arr.length == 0)
            return result;

        String[] tmpArr = new String[arr.length];
        for(int i=0; i<arr.length; i++){
            try {
                tmpArr[i] = decrypt(arr[i].trim());
            }catch (Exception e){
                if(arr[i] == null || arr[i] == "" || arr[i] == " "){
                    continue;
                }
                tmpArr[i] = arr[i];
            }
        }
        result = String.join(toDelimiter, tmpArr);
        return result;
    }



}

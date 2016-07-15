package com.example.zeningzhang.client;

/**
 * Created by ZeningZhang on 7/5/16.
 */
import android.util.Log;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;




/**
 * Created by HiArno on 2016/5/14.
 */
public class RSA {



    private static String message = "test";
    private static String pub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs9H4KMp+jf1lHdf5z7BB\n" +
            "Zw59KYsbd9FUKyNVYpNZaJKDOEHuFlkGCs41Tu/62n0KNGudRVEyo69Avy19G328\n" +
            "V6sZMrxpgrdhgClXi7R1UxEa35O8GyuYjm8t1bmHC4xBWHZitHE5UaTw7g6W7GOR\n" +
            "p2QlTn7qPsbwMGGSl63XlehbMb1BuJmLZJJuStEW1cJHmatTPMHMOokNSkK6I0ka\n" +
            "45OueG9QMPam7JY7X9g7YwLutSgLB4Vo0n1VXF6t7sqDm/CMUeys75VY+G8BBGvS\n" +
            "aFEZNSrb0ltlepuPDVUmgBY5GOTN53xEdKF4/LRI8iNPhGl90SrpDui9WmIf1C27\n" +
            "fQIDAQAB";
    private static String pri = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEA36fIF5iBZAKybYUaluhK7/9OrzPn4jXhlYbSc0t+A4KMRat70Ms23Lveq19+YKx/GXlNTH/yv59yBzNtJnyEFQIDAQABAkEAq5Fn20HwMCopef5bUVxM8CJn19r3jP5MWIoeMre8qS8zD1jje2KqBhzpnjcJkRz7vx3kM/hOTlaLxdyirtOEWQIhAPEk8daESt8+HiIk848uyqZWOIAsF8SUoQhvMmzIp7u7AiEA7W8FgOYuFmqcZLtLatcgynQSqQEERaOkPbfbpJ6xem8CIC1Ipc2F7FV94cTBsac6vz17RYL0RMZart7ZmtuI1s7PAiEAlnKt4xJ3i568QJvpWua8EWzhUhOh2FqrvKve0n336aMCIQDfxshpODreDUN/Dhq5/kos5cN2EIlOUtOI9lVbfmB1Gg==";

    public static String runRSA(Map<String,String> info, String status) throws Exception{
        try {
            // 初始化密钥
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(512);
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
//            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 私钥加密、公钥解密——私钥加密
            JSONObject jsonObject = new JSONObject();
            Iterator iter = info.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if(key.toString().equals("password"))
                {
                    String[] res = PasswordHash.createHash(val.toString());

                    jsonObject.put("password",res[0]);
                    jsonObject.put("salt",res[1]);
                }
                else{
                    jsonObject.put(key.toString(),val.toString());
                }
            }

            Log.d("zznmizzou",jsonObject.toString());

            Cipher cipher =Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE,getPublicKey(pub));
            byte[] result = cipher.doFinal(jsonObject.toString().getBytes());
            Log.d("zznmizzou","传入字符串为" + jsonObject.toString());
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(result));

            // 私钥加密、公钥解密——解密
//            cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.DECRYPT_MODE,getPrivateKey(pri));
//            result = cipher.doFinal(result);
//            Log.d("zznmizzou","解密结果: " + new String(result));



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;

        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

}




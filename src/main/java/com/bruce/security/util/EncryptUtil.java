package com.bruce.security.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Copyright Copyright Unicom (Zhejiang) Industrial Internet Co., Ltd. 2019
 * @Desc
 * @ProjectName daily_java
 * @Date 2020/9/29 16:23
 * @Author fzh
 */
public class EncryptUtil {

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";
    public static final String HmacMD5 = "HmacMD5";
    public static final String HmacSHA1 = "HmacSHA1";
    public static final String DES = "DES";
    public static final String AES = "AES";
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * 编码格式；默认使用uft-8
     */
    public static String charset = "utf-8";
    /**
     * DES
     */
    public static int keySizeDES = 0;
    /**
     * AES
     */
    public static int keySizeAES = 128;


    /**
     * 使用MessageDigest进行单向加密（无密码）
     *
     * @param res       被加密的文本
     * @param algorithm 加密算法名称
     * @return 加密
     */
    private static String messageDigest(String res, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
            return base64(md.digest(resBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static char[] chs = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
     *
     * @param res       加密的原文
     * @param algorithm 加密使用的算法名称
     * @param key       加密的秘钥
     * @param keySize   密钥长度
     * @param isEncode  加密/解密
     * @return 加密/解密
     */
    private static String keyGeneratorES(String res, String algorithm, String key, int keySize, boolean isEncode) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            if (keySize == 0) {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                kg.init(new SecureRandom(keyBytes));
            } else if (key == null) {
                kg.init(keySize);
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                kg.init(keySize, new SecureRandom(keyBytes));
            }
            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
                return parseByte2HexStr(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                return new String(cipher.doFinal(parseHexStr2Byte(res)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String base64(byte[] res) {
        return Base64.encode(res);
    }

    /**
     * 将二进制转换成16进制
     */
    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return new byte[]{};
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * md5加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @return md5 不带密钥加密
     */
    public static String md5(String res) {
        return messageDigest(res, MD5);
    }

    /**
     * md5加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return md5 带密钥加密
     */
    public static String md5(String res, String key) {
        return keyGeneratorMac(res, HmacMD5, key);
    }

    /**
     * 使用SHA1加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @return sha1 不带密钥加密
     */
    public static String sha1(String res) {
        return messageDigest(res, SHA1);
    }

    /**
     * 使用SHA1加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return sha1 带密钥加密
     */
    public static String sha1(String res, String key) {
        return keyGeneratorMac(res, HmacSHA1, key);
    }

    /**
     * 使用DES加密算法进行加密（可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return DES 加密
     */
    public static String encodeDES(String res, String key) {
        return keyGeneratorES(res, DES, key, keySizeDES, true);
    }

    /**
     * 对使用DES加密算法的密文进行解密（可逆）
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return DES 解密
     */
    public static String decodeDES(String res, String key) {
        return keyGeneratorES(res, DES, key, keySizeDES, false);
    }

    /**
     * 使用AES加密算法经行加密（可逆）
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return AES 加密
     */
    public static String encodeAES(String res, String key) {
        return keyGeneratorES(res, AES, key, keySizeAES, true);
    }

    /**
     * 对使用AES加密算法的密文进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return AES 解密
     */
    public static String decodeAES(String res, String key) {
        return keyGeneratorES(res, AES, key, keySizeAES, false);
    }

    /**
     * 使用异或进行加密
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return 异或加密
     */
    public static String encodeXOR(String res, String key) {
        byte[] bs = res.getBytes();
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return parseByte2HexStr(bs);
    }

    /**
     * 使用异或进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return 异或解密
     */
    public static String decodeXOR(String res, String key) {
        byte[] bs = parseHexStr2Byte(res);
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return new String(bs);
    }

    /**
     * 使用Base64进行加密
     *
     * @param res 原文
     * @return 使用 base64 加密
     */
    public static String encodeBase64(String res) {
        return Base64.encode(res.getBytes());
    }

    /**
     * 使用Base64进行解密
     *
     * @param res 加密后的内容
     * @return 原文
     */
    public static String decodeBase64(String res) {
        return new String(Base64.decode(res));
    }

    /**
     * 使用KeyGenerator进行单向/双向加密（可设密码）
     *
     * @param res       被加密的原文
     * @param algorithm 加密使用的算法名称
     * @param key       加密使用的秘钥
     * @return 加密
     */
    private static String keyGeneratorMac(String res, String algorithm, String key) {
        try {
            SecretKey sk;
            if (key == null) {
                KeyGenerator kg = KeyGenerator.getInstance(algorithm);
                sk = kg.generateKey();
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                sk = new SecretKeySpec(keyBytes, algorithm);
            }
            Mac mac = Mac.getInstance(algorithm);
            mac.init(sk);
            byte[] result = mac.doFinal(res.getBytes());
            return base64(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用凯撒加密方式加密数据
     *
     * @param res 原文
     * @param key 偏移量
     * @return 加密后的字符
     */
    public static String encodeKaisa(String res, int key) {
        //将字符串转换为数组
        char[] chars = res.toCharArray();
        StringBuilder sb = new StringBuilder();
        //遍历数组
        for (char aChar : chars) {
            //获取字符的ASCII编码
            int asciiCode = aChar;
            //偏移数据
            asciiCode += key;
            //将偏移后的数据转为字符
            char result = (char) asciiCode;
            //拼接数据
            sb.append(result);
        }
        return sb.toString();
    }

    /**
     * 使用凯撒加密方式解密数据
     *
     * @param res 密文
     * @param key 偏移量
     * @return : 源数据
     */
    public static String decodeKaisa(String res, int key) {
        // 将字符串转为字符数组
        char[] chars = res.toCharArray();
        StringBuilder sb = new StringBuilder();
        // 遍历数组
        for (char aChar : chars) {
            // 获取字符的ASCII编码
            int asciiCode = aChar;
            // 偏移数据
            asciiCode -= key;
            // 将偏移后的数据转为字符
            char result = (char) asciiCode;
            // 拼接数据
            sb.append(result);
        }
        return sb.toString();
    }

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decrypt(PublicKey publicKey, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(encrypted);
    }

    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = Base64.encode(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.encode((privateKey.getEncoded()));
        // 将公钥和私钥保存到Map
        Map<String, String> map = new HashMap<>();
        map.put("publicKey", publicKeyString);
        map.put("privateKey", privateKeyString);
        return map;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String publicKeyEncrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").
                generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encode(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String privateKeyDecrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decode(str);
        //base64编码的私钥
        byte[] decoded = Base64.decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * 十进制数转成其他进制 若超过范围，默认为 10 进制
     * 不支持 负数及小数
     *
     * @param num   十进制
     * @param radix 进制   2 <= radix <= 62
     * @return radix 进制表示
     */
    public static String tenToAny(String num, int radix) {
        BigDecimal integer = new BigDecimal(num);
        if (radix > 62 || radix < 2) {
            radix = 10;
        }
        StringBuilder sb = new StringBuilder();
        while (integer.compareTo(BigDecimal.valueOf(0)) > 0) {
            BigDecimal[] result = integer.divideAndRemainder(BigDecimal.valueOf(radix));
            sb.append(chs[result[1].intValue()]);
            integer = result[0];
        }
        return sb.reverse().toString();
    }

    /**
     * 十进制数转成其他进制 若超过范围，默认为 10 进制
     * 不支持小数
     *
     * @param num   十进制
     * @param radix 进制   2 <= radix <= 36
     * @return radix 进制表示
     */
    public static String tenToAnyV2(int num, int radix) {
        return Integer.toString(num, radix);
    }

    /**
     * 其他进制转 10 进制
     *
     * @param origin origin 进制
     * @param radix  radix 进制
     * @return 十进制表示
     */
    public static BigDecimal anyToTen(String origin, int radix) {
        BigDecimal result = BigDecimal.valueOf(0);
        char[] chars = origin.toCharArray();
        int length = chars.length;
        for (int i = 0; i < length; i++) {
            result = result.add(BigDecimal.valueOf(radix).pow(length - i - 1).multiply(BigDecimal.valueOf(getIndex(chars[i]))));
        }
        return result;
    }

    private static int getIndex(char character) {
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] == character) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 其他进制转 10 进制
     *
     * @param origin origin 进制
     * @param radix  radix 进制
     * @return 十进制表示
     */
    public static int anyToTenV2(String origin, int radix) {
        return Integer.parseInt(origin, radix);
    }

    /**
     * number   要转换的数
     * from     原数的进制
     * to       要转换成的进制
     */
    public static String change(String number, int from, int to) {
        return new BigInteger(number, from).toString(to);
    }

    public static void main(String[] args) throws Exception {
        String str = "Thisistheoriginstr";
        String key = "bruce";
        System.out.println("MD5:" + md5(str));
        System.out.println("MD5 带密钥:" + md5(str, key));
        System.out.println("SHA1:" + sha1(str));
        System.out.println("SHA1 带密钥:" + sha1(str, key));
        System.out.println("DES加密:" + encodeDES(str, key));
        System.out.println("DES解密:" + decodeDES(encodeDES(str, key), key));
        System.out.println("AES加密:" + encodeAES(str, key));
        System.out.println("AES解密:" + decodeAES(encodeAES(str, key), key));
        System.out.println("XOR加密:" + encodeXOR(str, key));
        System.out.println("XOR解密:" + decodeXOR(encodeXOR(str, key), key));
        System.out.println("BASE64加密:" + encodeBase64(str));
        System.out.println("BASE64解密:" + decodeBase64(encodeBase64(str)));
        System.out.println("凯撒加密:" + encodeKaisa(str, 1));
        System.out.println("凯撒解密:" + decodeKaisa(encodeKaisa(str, 1), 1));
        Map<String, String> map = genKeyPair();
        String publicKey = map.get("publicKey");
        String privateKey = map.get("privateKey");
        String encrypt = publicKeyEncrypt(str, publicKey);
        System.out.println("RSA加密:" + encrypt);
        String origin = privateKeyDecrypt(encrypt, privateKey);
        System.out.println("RSA解密:" + origin);

        System.out.println("十进制转62进制V1:" + tenToAny("123456", 62));
        System.out.println("62进制转十进制V1:" + anyToTen(tenToAny("123456", 62), 62));
        System.out.println("62进制转十进制V1:" + anyToTen("askldhflahkKAHFSLsakdfaldfakflh123123aksdflaf", 62));

        System.out.println("十进制转2进制V1:" + tenToAny("6", 2));
        System.out.println("2进制转十进制V1:" + anyToTen(tenToAny("6", 2), 2));

        System.out.println("十进制转其他进制V2:" + tenToAnyV2(-6, 2));
        System.out.println("其他进制转十进制V2:" + anyToTenV2(tenToAnyV2(-6, 2), 2));

        System.out.println("36进制转10进制:" + change("ABCDE1236124617", 36, 10));
    }
}

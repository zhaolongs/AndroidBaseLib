package com.studyyoun.androidbaselibrary.aes;

import android.annotation.SuppressLint;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AesUtils {
    private final static String HEX = "0123456789ABCDEF";
    //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String CBC_PKCS5_PADDING = "AES/ECB/PKCS5Padding";
    //AES 加密
    private static final String AES = "AES";
    // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    private static final String SHA1PRNG = "SHA1PRNG";




    public static void main(String[] args) throws Exception {
        //私密
        String key = "AD2FF87425CA729C";
        //明文
        String content = "{\"password\":\"123456\",\"username\":\"145\"}";
        //加密
        String lEncrypt =AesUtils.encrypt(key, content);
        //密文
        System.out.println("" + lEncrypt);
        //解密
        String lDecrypt = AesUtils.decrypt(key, lEncrypt);
        System.out.println(lDecrypt);

    }







    /*
     * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
     */
    public static String generateKey() {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);
            String str_key = toHex(bytes_key);
            str_key = str_key.substring(0, 16);
            return str_key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 对密钥进行处理
    @SuppressLint("DeletedProvider")
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        //for android
        SecureRandom sr = null;
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            sr = SecureRandom.getInstance(SHA1PRNG, "Crypto");
        } else {
            sr = SecureRandom.getInstance(SHA1PRNG);
        }
        // for Java
        // secureRandom = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed);
        kgen.init(128, sr); //256 bits or 128 bits,192bits
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    //二进制转字符
    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }


    /*
     * 解密
     */
    public static String decrypt(String key, String encrypted) throws Exception {
        if (encrypted == null || encrypted.equals("")) {
            return null;
        }
        //获取加密工具
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        //初始化加密工具
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
        //将密文转为 byte[]
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64Decoder.decodeToBytes(encrypted);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /*
     * 加密
     */
    public static String encrypt(String key, String cleartext) {
        if (cleartext == null || cleartext.equals("")) {
            return null;
        }
        try {
            //加密
            byte[] result = encrypt(key, cleartext.getBytes());
            return Base64Encoder.encode(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
    Java 对称加密使用DES / 3DES / AES
    对称密码算法的加密密钥和解密密钥相同，对于大多数对称密码算法，加解密过程互逆。
    对称密码有流密码和分组密码两种.

    分组密码工作模式
    1）ECB：电子密码本（最常用的，每次加密均产生独立的密文分组，并且对其他的密文分组不会产生影响，也就是相同的明文加密后产生相同的密文）
    2）CBC：密文链接（常用的，明文加密前需要先和前面的密文进行异或运算，也就是相同的明文加密后产生不同的密文）
    3）CFB：密文反馈
    4）OFB：输出反馈
    5）CTR：计数器

    分组密码填充方式
    1）NoPadding：无填充
    2）PKCS5Padding：
    3）ISO10126Padding：
     */

    /*
     * 加密
     */
    private static byte[] encrypt(String key, byte[] input) throws Exception {
        //获取加密工具
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        //初始化加密工具 初始化 模式为加密模式，并指定密匙
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
        //放入我们要加密的内容 并加密 input为需要加密的byte数组
        byte[] encrypted = cipher.doFinal(input);
        // 返回加密后的密文（byte数组)
        return encrypted;
    }



}

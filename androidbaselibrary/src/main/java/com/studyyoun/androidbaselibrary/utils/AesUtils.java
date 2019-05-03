package com.studyyoun.androidbaselibrary.utils;

import com.alibaba.fastjson.util.Base64;
import com.studyyoun.androidbaselibrary.okhttp.exce.OkhttpException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;



public class AesUtils {

    static final String algorithmStr = "AES/ECB/PKCS5Padding";

    private static final Object TAG = "AES";

    static private KeyGenerator keyGen;

    static private Cipher cipher;

    static boolean isInited = false;

    //注意: 这里的password(秘钥必须是16位的)
    private static final String keyBytes = "abcdefgabcdefg12";

    private static void init() {
        try {
            /**为指定算法生成一个 KeyGenerator 对象。
             *此类提供（对称）密钥生成器的功能。
             *密钥生成器是使用此类的某个 getInstance 类方法构造的。
             *KeyGenerator 对象可重复使用，也就是说，在生成密钥后，
             *可以重复使用同一 KeyGenerator 对象来生成进一步的密钥。
             *生成密钥的方式有两种：与算法无关的方式，以及特定于算法的方式。
             *两者之间的惟一不同是对象的初始化：
             *与算法无关的初始化
             *所有密钥生成器都具有密钥长度 和随机源 的概念。
             *此 KeyGenerator 类中有一个 init 方法，它可采用这两个通用概念的参数。
             *还有一个只带 keysize 参数的 init 方法，
             *它使用具有最高优先级的提供程序的 SecureRandom 实现作为随机源
             *（如果安装的提供程序都不提供 SecureRandom 实现，则使用系统提供的随机源）。
             *此 KeyGenerator 类还提供一个只带随机源参数的 inti 方法。
             *因为调用上述与算法无关的 init 方法时未指定其他参数，
             *所以由提供程序决定如何处理将与每个密钥相关的特定于算法的参数（如果有）。
             *特定于算法的初始化
             *在已经存在特定于算法的参数集的情况下，
             *有两个具有 AlgorithmParameterSpec 参数的 init 方法。
             *其中一个方法还有一个 SecureRandom 参数，
             *而另一个方法将已安装的高优先级提供程序的 SecureRandom 实现用作随机源
             *（或者作为系统提供的随机源，如果安装的提供程序都不提供 SecureRandom 实现）。
             *如果客户端没有显式地初始化 KeyGenerator（通过调用 init 方法），
             *每个提供程序必须提供（和记录）默认初始化。
             */
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 初始化此密钥生成器，使其具有确定的密钥长度。
        keyGen.init(128);//128位的AES加密
        try {
            // 生成一个实现指定转换的 Cipher 对象。
            cipher = Cipher.getInstance(algorithmStr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        //标识已经初始化过了的字段
        isInited = true;
    }

    private static byte[] genKey() {
        if (!isInited) {
            init();
        }
        //首先 生成一个密钥(SecretKey),
        //然后,通过这个密钥,返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
        return keyGen.generateKey().getEncoded();
    }

    private static byte[] encrypt(byte[] content, byte[] keyBytes) {
        byte[] encryptedText = null;
        if (!isInited) {
            init();
        }
        /**
         *类 SecretKeySpec
         *可以使用此类来根据一个字节数组构造一个 SecretKey，
         *而无须通过一个（基于 provider 的）SecretKeyFactory。
         *此类仅对能表示为一个字节数组并且没有任何与之相关联的钥参数的原始密钥有用
         *构造方法根据给定的字节数组构造一个密钥。
         *此构造方法不检查给定的字节数组是否指定了一个算法的密钥。
         */
        Key key = new SecretKeySpec(keyBytes, "AES");
        try {
            // 用密钥初始化此 cipher。
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            // 按单部分操作加密或解密数据，或者结束一个多部分操作。
            encryptedText = cipher.doFinal(content);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encryptedText;
    }

    private static byte[] encrypt(String content, String password) {
        try {
            byte[] keyStr = getKey(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);//   ʼ
            byte[] result = cipher.doFinal(byteContent);
            return result;//
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decrypt(byte[] content, String password) {
        try {
            byte[] keyStr = getKey(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
            cipher.init(Cipher.DECRYPT_MODE, key);//   ʼ
            byte[] result = cipher.doFinal(content);
            return result;//
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getKey(String password) {
        byte[] rByte = null;
        if (password != null) {
            rByte = password.getBytes();
        } else {
            rByte = new byte[24];
        }
        return rByte;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 加密
     *
     * @param content 要加密的内容
     * @param aesKey  加密所需的密钥 长度为：8位 如：EWERWE23
     * @return 加密结果返回
     */
    public static String encode(String content, String aesKey) {
        // 把字符串aesKey转化成16进制，然后作为 AES密钥Key
        aesKey = parseByte2HexStr(aesKey.getBytes());
        //加密之后的字节数组,转成16进制的字符串形式输出
        return parseByte2HexStr(encrypt(content, aesKey));
    }

    /**
     * 解密
     *
     * @param content 要解密密的内容
     * @param aesKey  解密所需的密钥 长度为：8位 如：EWERWE23
     * @return 解密成功后的原文返回
     */
    public static String decode(String content, String aesKey) {
        // 把字符串aesKey转化成16进制，然后作为 AES密钥Key
        aesKey = parseByte2HexStr(aesKey.getBytes());
        //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
        byte[] b = decrypt(parseHexStr2Byte(content), aesKey);
        return new String(b);
    }









    //测试用例
    public static void test1() {
        String content = "hello3<><>3@!#@我的爱人   ssss";

        // 字符串必须是8位的长度；
        // 注意：中文算2个字节（而转化16进制的一个字节的会生产3个值，所以一个中文转换成的16进制结果是2x3=6位）
        String aesKey = "EWERWE23";
        System.out.println("AES密钥：" + aesKey);
        String pStr = encode(content, aesKey);
        System.out.println("加密前：" + content);
        System.out.println("加密后:" + pStr);

        String postStr = decode(pStr, aesKey);
        System.out.println("解密后：" + postStr);


        String token = "U3S7mcksczfku6C4";
        String data = "{\"code\":\"872232\",\"phone\":\"15728024110\",\"password\":\"123456\",\"againPassword\":\"123456\"}";
        String aes = AesUtils.toAESForSplit(data, token);
        System.out.println(aes);
        System.out.println(AesUtils.AESToStringForSplit(aes, token));
    }

    /**
     * AES加密
     *
     * @param text  要加密的数据
     * @param token 约定密串
     * @return 加密后的密文
     */
    public static String toAESForSplit(String text, String token) {
        try {
            return toAESForSplit(text, token, null);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * AES加密
     *
     * @param text    要加密的数据
     * @param token   约定密串
     * @param charset 原数据字符集
     * @return 加密后的密文
     */
    public static String toAESForSplit(String text, String token, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//    		KeyGenerator kgen = KeyGenerator.getInstance("AES");
//    		kgen.init(128, new SecureRandom(charset==null?token.getBytes():token.getBytes(charset)));
//    		SecretKey secretKey = kgen.generateKey();
//    		byte[] enCodeFormat = secretKey.getEncoded();
//    		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//    		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
//    		byte[] btText = charset==null?text.getBytes():text.getBytes(charset);
//    		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
//    		byte[] bts = cipher.doFinal(btText);
        byte[] data = charset == null ? text.getBytes() : text.getBytes(charset);
        int maxDataSize = 127;
        String result = "";
        char chs[] = text.toCharArray();

        int size = 0;
        int lastIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            int length = charset == null ? String.valueOf(chs[i]).getBytes().length : String.valueOf(chs[i]).getBytes(charset).length;
            if (size + length > maxDataSize) {
                i--;
            } else if (i == text.length() - 1) {
                size += length;
            } else {
                size += length;
                continue;
            }
            byte[] onceBts = Arrays.copyOfRange(data, lastIndex, lastIndex + size);
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(token.getBytes(), "AES"));
            byte[] tempBts = cipher.doFinal(onceBts);
            result += toBase64(tempBts) + "_";
            lastIndex += size;
            size = 0;
        }
        return result.trim().replace("\r", "").replace("\n", "");
    }

    /**
     * AES解密
     *
     * @param text  要加密的数据
     * @param token 约定密串
     * @return 解密后的原文
     */
    public static String AESToStringForSplit(String text, String token) {
        try {
            return AESToStringForSplit(text, token, null);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 将字节数据处理成base64字符串<br/>
     *
     * @param bts 字节数据
     * @return base64编码后的字符串(用于传输)
     * @throws IOException
     */
    public static String toBase64(byte[] bts) {
        if (bts == null || bts.length == 0) {
            return null;
        }

        String base64Str = "";;
        base64Str = base64Str.replace("\r", "").replace("\n", "");
        return base64Str;
    }
    /**
     * AES解密
     *
     * @param text    要加密的数据
     * @param token   约定密串
     * @param charset 原数据字符集
     * @return 解密后的原文
     */
    public static String AESToStringForSplit(String text, String token, String charset) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        text = text.replace("\r", "").replace("\n", "");
        String[] datas = text.split("_");
        String result = "";
        for (String data : datas) {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(token.getBytes(), "AES"));
            byte[] bts = cipher.doFinal(base64ToByte(data));
            result += charset == null ? new String(bts) : new String(bts, charset);
        }
        return result;
    }

    /**
     * 将base64字符串处理成String字节<br/>
     *
     * @param str base64的字符串
     * @return 原字节数据
     * @throws IOException
     */
    public static byte[] base64ToByte(String str) throws IOException {
        if (str == null) {
            return null;
        }
        return android.util.Base64.decode(str, Integer.parseInt(""));
    }
    public static void main(String[] args) {
        test1();
    }

}

package cn.modoumama.common.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * DES安全编码组件
 * <p/>
 * <pre>
 * 支持 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR)
 * DES                  key size must be equal to 56
 * DESede(TripleDES)     key size must be equal to 112 or 168
 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
 * Blowfish          key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
 * RC2                  key size must be between 40 and 1024 bits
 * RC4(ARCFOUR)      key size must be between 40 and 1024 bits
 * 具体内容 需要关注 JDK Document http://.../docs/technotes/guides/security/SunProviders.html
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
public abstract class DESUtil extends Code {
    /**
     * ALGORITHM 算法 <br>
     * 可替换为以下任意一种算法，同时key值的size相应改变。
     * <p/>
     * <pre>
     * DES                  key size must be equal to 56
     * DESede(TripleDES)     key size must be equal to 112 or 168
     * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
     * Blowfish          key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
     * RC2                  key size must be between 40 and 1024 bits
     * RC4(ARCFOUR)      key size must be between 40 and 1024 bits
     * </pre>
     * <p/>
     * 在Key toKey(byte[] key)方法中使用下述代码
     * <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> 替换
     * <code>
     * DESKeySpec dks = new DESKeySpec(key);
     * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
     * SecretKey secretKey = keyFactory.generateSecret(dks);
     * </code>
     */
    public static final String ALGORITHM = "DES";
    private static final String ENCODING = "UTF-8";
    // 生成密钥的16位长度种子
    public static final String DEFAULTSEED = "abycdoefughlije1";

    /**
     * 转换密钥<br>
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String key) throws Exception {
        Key k = toKey(decryptBASE64(key));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }


    public static String decrypt(String data) throws Exception {
        String key = DESUtil.initKey(DEFAULTSEED);
        System.out.println(data);
        return new String(decrypt(Code.decryptBASE64(data), key), ENCODING);
    }


    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String key) throws Exception {
        Key k = toKey(decryptBASE64(key));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);

        return cipher.doFinal(data);
    }

    public static String encrypt(String data) throws Exception {
        String key = DESUtil.initKey(DEFAULTSEED);
        byte[] encodedData = encrypt(data.getBytes(ENCODING), key);
        return encryptBASE64(encodedData);
    }

    /**
     * 生成密钥
     *
     * @return
     * @throws Exception
     */
    public static String initKey() throws Exception {
        return initKey(null);
    }

    /**
     * 生成密钥
     *
     * @param seed
     * @return
     * @throws Exception
     */
    public static String initKey(String seed) throws Exception {
//        SecureRandom secureRandom = null;
//
//        if (seed != null) {
//            secureRandom = new SecureRandom(decryptBASE64(seed));
//        } else {
//            secureRandom = new SecureRandom();
//        }
//
//        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
//        kg.init(secureRandom);
//
//        SecretKey secretKey = kg.generateKey();
//
//
//
//
//


        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        DESKeySpec keySpec = new DESKeySpec(seed.getBytes());
        keyFactory.generateSecret(keySpec);
//        return ;


        return encryptBASE64(keyFactory.generateSecret(keySpec).getEncoded());
    }

    public static void main(String[] ss) {
        // 加密
        String mw = "13244918809-918809";

        try {
            String aa = encrypt(mw);
            System.out.println(aa);

            System.out.println(decrypt(aa));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
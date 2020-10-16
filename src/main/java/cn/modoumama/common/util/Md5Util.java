package cn.modoumama.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class Md5Util {
	
	private static final String hex_chr = "0123456789abcdef";

	private static String rhex(int num) {
		String str = "";
		for (int j = 0; j <= 3; j++)
			str = str + hex_chr.charAt((num >> (j * 8 + 4)) & 0x0F)
					+ hex_chr.charAt((num >> (j * 8)) & 0x0F);
		return str;
	}

	/*
	 * Convert a string to a sequence of 16-word blocks, stored as an array.
	 * Append padding bits and the length, as described in the MD5 standard.
	 */
	private static int[] str2blks_MD5(String str) {
		int nblk = ((str.length() + 8) >> 6) + 1;
		int[] blks = new int[nblk * 16];
		int i = 0;
		for (i = 0; i < nblk * 16; i++) {
			blks[i] = 0;
		}
		for (i = 0; i < str.length(); i++) {
			blks[i >> 2] |= str.charAt(i) << ((i % 4) * 8);
		}
		blks[i >> 2] |= 0x80 << ((i % 4) * 8);
		blks[nblk * 16 - 2] = str.length() * 8;

		return blks;
	}

	/*
	 * Add integers, wrapping at 2^32
	 */
	private static int add(int x, int y) {
		return ((x & 0x7FFFFFFF) + (y & 0x7FFFFFFF)) ^ (x & 0x80000000)
				^ (y & 0x80000000);
	}

	/*
	 * Bitwise rotate a 32-bit number to the left
	 */
	private static int rol(int num, int cnt) {
		return (num << cnt) | (num >>> (32 - cnt));
	}

	/*
	 * These functions implement the basic operation for each round of the
	 * algorithm.
	 */
	private static int cmn(int q, int a, int b, int x, int s, int t) {
		return add(rol(add(add(a, q), add(x, t)), s), b);
	}

	private static int ff(int a, int b, int c, int d, int x, int s, int t) {
		return cmn((b & c) | ((~b) & d), a, b, x, s, t);
	}

	private static int gg(int a, int b, int c, int d, int x, int s, int t) {
		return cmn((b & d) | (c & (~d)), a, b, x, s, t);
	}

	private static int hh(int a, int b, int c, int d, int x, int s, int t) {
		return cmn(b ^ c ^ d, a, b, x, s, t);
	}

	private static int ii(int a, int b, int c, int d, int x, int s, int t) {
		return cmn(c ^ (b | (~d)), a, b, x, s, t);
	}

	
	/*
	 * Take a string and return the hex representation of its MD5.
	 */
	public static String getMD5String(String str) {
		str = "#chainStoreplatform{*@@&*)" + str+ "#%^&@#!$(=}" ;
		int[] x = str2blks_MD5(str);
		int a = 0x67452301;
		int b = 0xEFCDAB89;
		int c = 0x98BADCFE;
		int d = 0x10325476;

		for (int i = 0; i < x.length; i += 16) {
			int olda = a;
			int oldb = b;
			int oldc = c;
			int oldd = d;

			a = ff(a, b, c, d, x[i + 0], 7, 0xD76AA478);
			d = ff(d, a, b, c, x[i + 1], 12, 0xE8C7B756);
			c = ff(c, d, a, b, x[i + 2], 17, 0x242070DB);
			b = ff(b, c, d, a, x[i + 3], 22, 0xC1BDCEEE);
			a = ff(a, b, c, d, x[i + 4], 7, 0xF57C0FAF);
			d = ff(d, a, b, c, x[i + 5], 12, 0x4787C62A);
			c = ff(c, d, a, b, x[i + 6], 17, 0xA8304613);
			b = ff(b, c, d, a, x[i + 7], 22, 0xFD469501);
			a = ff(a, b, c, d, x[i + 8], 7, 0x698098D8);
			d = ff(d, a, b, c, x[i + 9], 12, 0x8B44F7AF);
			c = ff(c, d, a, b, x[i + 10], 17, 0xFFFF5BB1);
			b = ff(b, c, d, a, x[i + 11], 22, 0x895CD7BE);
			a = ff(a, b, c, d, x[i + 12], 7, 0x6B901122);
			d = ff(d, a, b, c, x[i + 13], 12, 0xFD987193);
			c = ff(c, d, a, b, x[i + 14], 17, 0xA679438E);
			b = ff(b, c, d, a, x[i + 15], 22, 0x49B40821);

			a = gg(a, b, c, d, x[i + 1], 5, 0xF61E2562);
			d = gg(d, a, b, c, x[i + 6], 9, 0xC040B340);
			c = gg(c, d, a, b, x[i + 11], 14, 0x265E5A51);
			b = gg(b, c, d, a, x[i + 0], 20, 0xE9B6C7AA);
			a = gg(a, b, c, d, x[i + 5], 5, 0xD62F105D);
			d = gg(d, a, b, c, x[i + 10], 9, 0x02441453);
			c = gg(c, d, a, b, x[i + 15], 14, 0xD8A1E681);
			b = gg(b, c, d, a, x[i + 4], 20, 0xE7D3FBC8);
			a = gg(a, b, c, d, x[i + 9], 5, 0x21E1CDE6);
			d = gg(d, a, b, c, x[i + 14], 9, 0xC33707D6);
			c = gg(c, d, a, b, x[i + 3], 14, 0xF4D50D87);
			b = gg(b, c, d, a, x[i + 8], 20, 0x455A14ED);
			a = gg(a, b, c, d, x[i + 13], 5, 0xA9E3E905);
			d = gg(d, a, b, c, x[i + 2], 9, 0xFCEFA3F8);
			c = gg(c, d, a, b, x[i + 7], 14, 0x676F02D9);
			b = gg(b, c, d, a, x[i + 12], 20, 0x8D2A4C8A);

			a = hh(a, b, c, d, x[i + 5], 4, 0xFFFA3942);
			d = hh(d, a, b, c, x[i + 8], 11, 0x8771F681);
			c = hh(c, d, a, b, x[i + 11], 16, 0x6D9D6122);
			b = hh(b, c, d, a, x[i + 14], 23, 0xFDE5380C);
			a = hh(a, b, c, d, x[i + 1], 4, 0xA4BEEA44);
			d = hh(d, a, b, c, x[i + 4], 11, 0x4BDECFA9);
			c = hh(c, d, a, b, x[i + 7], 16, 0xF6BB4B60);
			b = hh(b, c, d, a, x[i + 10], 23, 0xBEBFBC70);
			a = hh(a, b, c, d, x[i + 13], 4, 0x289B7EC6);
			d = hh(d, a, b, c, x[i + 0], 11, 0xEAA127FA);
			c = hh(c, d, a, b, x[i + 3], 16, 0xD4EF3085);
			b = hh(b, c, d, a, x[i + 6], 23, 0x04881D05);
			a = hh(a, b, c, d, x[i + 9], 4, 0xD9D4D039);
			d = hh(d, a, b, c, x[i + 12], 11, 0xE6DB99E5);
			c = hh(c, d, a, b, x[i + 15], 16, 0x1FA27CF8);
			b = hh(b, c, d, a, x[i + 2], 23, 0xC4AC5665);

			a = ii(a, b, c, d, x[i + 0], 6, 0xF4292244);
			d = ii(d, a, b, c, x[i + 7], 10, 0x432AFF97);
			c = ii(c, d, a, b, x[i + 14], 15, 0xAB9423A7);
			b = ii(b, c, d, a, x[i + 5], 21, 0xFC93A039);
			a = ii(a, b, c, d, x[i + 12], 6, 0x655B59C3);
			d = ii(d, a, b, c, x[i + 3], 10, 0x8F0CCC92);
			c = ii(c, d, a, b, x[i + 10], 15, 0xFFEFF47D);
			b = ii(b, c, d, a, x[i + 1], 21, 0x85845DD1);
			a = ii(a, b, c, d, x[i + 8], 6, 0x6FA87E4F);
			d = ii(d, a, b, c, x[i + 15], 10, 0xFE2CE6E0);
			c = ii(c, d, a, b, x[i + 6], 15, 0xA3014314);
			b = ii(b, c, d, a, x[i + 13], 21, 0x4E0811A1);
			a = ii(a, b, c, d, x[i + 4], 6, 0xF7537E82);
			d = ii(d, a, b, c, x[i + 11], 10, 0xBD3AF235);
			c = ii(c, d, a, b, x[i + 2], 15, 0x2AD7D2BB);
			b = ii(b, c, d, a, x[i + 9], 21, 0xEB86D391);

			a = add(a, olda);
			b = add(b, oldb);
			c = add(c, oldc);
			d = add(d, oldd);
		}
		return rhex(a) + rhex(b) + rhex(c) + rhex(d);
	} 
	
	public static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	/**
	 * 先用MD5加密，在用Base64转码
	 * 
	 * @param srcContent
	 * @return
	 */
	public static String makeMd5Sum(byte[] srcContent) {
		if (srcContent == null) {
			return null;
		}

		String strDes = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(srcContent);
			strDes = bytes2Hex(md5.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}
	private static String bytes2Hex(byte[] byteArray) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if ((byteArray[i] >= 0) && (byteArray[i] < 16)) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(byteArray[i] & 0xFF));
		}
		return strBuf.toString();
	}
	
	public static String md5Signature(TreeMap<String, String> params,
			String secret) {
		String result = null;
		StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));
		if (orgin == null)
			return result;
		orgin.append(secret);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = bytes2Hex(md.digest(orgin.toString().getBytes("utf-8")));
		} catch (Exception e) {
			throw new RuntimeException("sign error !");
		}
		return result;
	}
	
	private static StringBuffer getBeforeSign(TreeMap<String, String> params,
			StringBuffer orgin) {
		if (params == null)
			return null;
		Map<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(params);
		Iterator<String> iter = treeMap.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			orgin.append(name).append(params.get(name));
		}
		return orgin;
	}
}
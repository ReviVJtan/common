package cn.modoumama.common.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {
	static Logger logger = LoggerFactory.getLogger(StringUtils.class);
	static char ARR[] = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	public static String toUpperCase(String str) {
		return trim(str).toUpperCase();
	}

	public static String toLowerCase(String str) {
		return trim(str).toLowerCase();
	}

	public static String trim(String s) {
		return getNotNullString(s);
	}

	public static String None2Null(String s) {
		if (s != null) {
			if ("None".equals(s)) {
				return "";
			}
			return s.trim();
		}

		return "";
	}

	public static String NullString2Null(String s) throws Exception {
		if (s != null) {
			if ("null".equalsIgnoreCase(s)) {
				return "";
			}
			return s.trim();
		}

		return "";
	}

	public static String blankToBracket(String str) {
		if ((str == null) || (str.equals("")))
			return "{}";
		return str;
	}

	public static String blankToZero(String str) {
		if ((str == null) || (str.equals("")))
			return "0";
		return str;
	}

	public static String getTrimString(String src, int n, String var) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < n; i++) {
			buf.append(var);
		}
		buf.append(src);

		return buf.toString();
	}

	public static String getTrimString(String conzt, String var) {
		String ret = conzt + var;
		return ret.substring(var.length());
	}

	public static String getTrimRight(String src, int n) {
		StringBuilder buf = new StringBuilder();

		int lngth = src != null ? src.length() : 0;
		buf.append(src);
		for (int i = 0; i < n - lngth; i++) {
			buf.append(" ");
		}

		return buf.toString();
	}

	public static String getTrimString(String conzt, int var) {
		String ret = conzt + var;
		return ret.substring(ret.length() - conzt.length());
	}

	public static String getNotNullString(String s, String sdefault) {
		return s != null ? s.trim() : sdefault;
	}

	public static String getNotNullString(String s) {
		return s != null ? s.trim() : "";
	}
	
	/**
	 * ???????????????????????????
	 * @param value	String
	 * @return boolean
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.equals(""));
	}
	
	public static boolean isBlank(String in) {
		
		if (isEmpty(in))
			return true;
		//??????None?????????
		if ((in.trim().equals("")) || (in.trim().equalsIgnoreCase("null")))
			return true;
		return false;
	}
	
	public static boolean isNotBlank(String in) {
		return !isBlank(in);
	}
	
	public static String format(String intval, int length) {
		char[] c = new char[length];
		for (int i = 0; i < c.length; i++)
			c[i] = '0';
		StringBuffer buf = new StringBuffer(String.valueOf(c));
		buf.replace(length - intval.length(), length, intval);
		return buf.toString();
	}
	
	
	public static String replace(String strSource, String strFrom, String strTo) {
		StringBuffer strDest = new StringBuffer();
		int intFromLen = strFrom.length();
		int intPos;
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			strDest = strDest.append(strSource.substring(0, intPos)).append(
					strTo);
			strSource = strSource.substring(intPos + intFromLen);
		}
		strDest = strDest.append(strSource);

		return strDest.toString();
	}
	
	// / ??????????????????(SBC case) ///
		// /??????????????? /// ??????????????? ///
		// /???????????????12288,???????????????32
		// /??????????????????(33-126)?????????(65281-65374)??????????????????????????????65248 ///
		public static String toSBC(String input) {
			// ??????????????????
			char[] c = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == 32) {
					c[i] = (char) 12288;
					continue;
				}
				if (c[i] < 127)
					c[i] = (char) (c[i] + 65248);
			}
			return new String(c);
		}

		// / /// ??????????????????(DBC case) ///
		// /???????????????
		// / ??????????????? ///
		// /???????????????12288??????????????????32
		// /??????????????????(33-126)?????????(65281-65374)??????????????????????????????65248 ///
		public static String toDBC(String input) {
			char[] c = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == 12288) {
					c[i] = (char) 32;
					continue;
				}
				if (c[i] > 65280 && c[i] < 65375)
					c[i] = (char) (c[i] - 65248);
			}
			return new String(c);
		}
		
		/**
		 * ??????????????????list
		 * @param childStr
		 * @return
		 */
		public static List<Long> stringtoLong(String childStr) {
			String arr[] = childStr.split(",");
			List<Long> result = new ArrayList<Long>();
			for(String item : arr){
				result.add(Long.parseLong(item));
			}
			return result;			
		 } 
		/**
		 * ??????????????????
		 * @param userNo
		 * @param sxh
		 * @return
		 */
	public static String getUserNoSuffix(String userNo, Integer sxh) {
		Integer mod = 0;
		StringBuffer result = new StringBuffer();
		int size = ARR.length;
		while (sxh > (size - 1)) {
			mod = sxh % size;
			sxh = sxh / size;
			result.insert(0, ARR[mod]);
		}
		result.insert(0, ARR[sxh - 1]);
		result.insert(0,userNo);
		return result.toString();
	}
	
	/**
	 * ??????????????????
	 * @param userNo
	 * @param sxh
	 * @return
	 */
public static String getFileSuffix(String fileName) {
	return toLowerCase(fileName.substring(fileName.lastIndexOf(".")));
}

	/**
	 * ???????????????
	 * @param ss
	 * @return
	 */
	public static String firstToLowerCase(String ss) throws Exception{
		ss = trim(ss);
		if(isEmpty(ss)){
			return "";
		}else {
			return toLowerCase(ss.substring(0, 1)) + ss.substring(1);
		}
	}
	
	/**
	 * ???????????????
	 * @param ss
	 * @return
	 */
	public static String firstToUpperCase(String ss) throws Exception{
		ss = trim(ss);
		if(isEmpty(ss)){
			return "";
		}else {
			ss.toUpperCase();
			return toUpperCase(ss.substring(0, 1)) + ss.substring(1);
		}
	}
	

	/**
	 * ???????????????<br>
	 * <br>  
	 * ??????????????????   <br>
	 * ???????????????2017???10???30??? ??????10:39:22    <br> 
	 * ????????????  <br>
	 * ???????????????2017???10???30??? ??????10:39:22   <br>  
	 * ???????????????     <br> 
	 * @return
	 */
	public static String getUnique() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * @FuncName getToken
	 * @description ??????token
	 * @return strong token
	 * @author xiaoyong
	 * @throws Exception
	 */
	public static String getToken() {
		logger.info("??????token");
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(getUnique().getBytes());
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}

				buf.append(Integer.toHexString(i));
			}

		} catch (Exception e) {
			logger.error("??????Token??????" + e);
		}
		return buf.toString();
	}
	
	public static String changePhone(String phone) {
		if (phone.length()==11) {
			return phone.substring(0,3)+"****"+phone.substring(7, 11);
		}else {
			return "";
		}
	}
	
	/**
	 * ??????????????????/????????????
	 * @param path
	 * @return
	 */
	public static String getPathStart(String path){
		 if(!path.startsWith("/")){
			 path = "/"+path;
	        }
		 return path;
	}
	
	/**
	 * ?????????????????????/????????????
	 * @param path
	 * @return
	 */
	public static String getPathNotStart(String path){
		path = path.replaceAll("^\\/", "");
		 
		 return path;
	}
	
	/**
	 * ??????????????????/????????????
	 * @param path
	 * @return
	 */
	public static String getPathEnd(String path){
		 if(!path.endsWith("/")){
			 path = path+"/";
	        }
		 return path;
	}
	
	/**
	 * ?????????????????????/????????????
	 * @param path
	 * @return
	 */
	public static String getPathNotEnd(String path){
		path = path.replaceAll("\\/$", "");
		 
		 return path;
	}
	
	/**
	 * ????????????html????????????
	 * ??????html??????a??????
	 * ??????html??????js??????
	 * @param source
	 * @return
	 */
	public static String filterJsAndAString(String source){
		String rString = source.toString();
		rString = rString.replaceAll("<\\s*(/)?\\s*(a|(script))[^>]*>", "");
		rString = rString.replaceAll("[\"'][\\s]*javascript:([^>]*)[\"']", "''"); 
		return rString;
	}
	
	/**
	 * ????????????html????????????
	 * ??????html??????????????????
	 * @param source
	 * @return
	 */
	public static String filterLabelString(String source){
		String rString = source.toString();
		rString = rString.replaceAll("<[^>]*>", "");
		return rString;
	}
}

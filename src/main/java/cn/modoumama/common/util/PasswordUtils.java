package cn.modoumama.common.util;

public class PasswordUtils {
	/**
	 * @param userType 1总管理员，2管理员，3.用户，4游客
	 * @param password 未加密的密码
	 * @return
	 */
	public static String getPassword(Integer userType, String password){
		String md5 = "";
		if(userType!=null){
			switch (userType) {
			case 0:
				md5 = MD5Encoder.encode(password);
				md5 = MD5Encoder.encode(md5+"f");
				md5 = MD5Encoder.encode(md5+"g");
				md5 = MD5Encoder.encode(md5+"d");
				break;
			case 1:
				md5 = MD5Encoder.encode(password);
				md5 = MD5Encoder.encode(md5+"w");
				md5 = MD5Encoder.encode(md5+"8");
				break;
			case 2:
				md5 = MD5Encoder.encode(password);
				md5 = MD5Encoder.encode(md5+"f");
				md5 = MD5Encoder.encode(md5+"d");
				break;

			default:
				md5 = MD5Encoder.encode(password);
				md5 = MD5Encoder.encode(md5+"s");
				break;
			}
		}else{
			md5 = MD5Encoder.encode(password);
			md5 = MD5Encoder.encode(md5+"s");
		}
		
		return md5;
		
	}
}

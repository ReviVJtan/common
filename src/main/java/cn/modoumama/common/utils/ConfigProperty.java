package cn.modoumama.common.utils;
 

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigProperty {
	private static ResourceBundle resourceBundle;
	private static ResourceBundle quickResourceBundle;

	private static final Log log = LogFactory.getLog(ConfigProperty.class);
	
	static {
		try {
			resourceBundle = ResourceBundle.getBundle("config");
			/*quickResourceBundle = ResourceBundle.getBundle("quickMoney");*/
		} catch (Exception e) {
			log.error("  Getting config is failed! ",e);
			e.printStackTrace();
		}
	}
	public static String getQuickProperty(String key) {
		return quickResourceBundle.getString(key);
	}
	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}
	
	public static Integer getIntegerProperty(String key){
		return Integer.parseInt(getProperty(key));
	}
	
	public static Long getLongProperty(String key){
		return Long.parseLong(getProperty(key));
	}
	
	public static String getProperty(String key,Object ... args) {
		String result = getProperty(key);
		if(args != null){
			MessageFormat format = new MessageFormat(result,Locale.getDefault());
			return format.format(args);
		}
		return result;
	}
	
	/**
	 * 获取多条配置<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月29日 下午3:06:10    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月29日 下午3:06:10   <br>  
	 * 修改备注：     <br> 
	 * @param regex  正则:.*asb.*
	 * @return
	 */
	public static Map<String, String> getPropertys(String regex){
		Map<String, String> configValue = new HashMap<>();
		Set<String> keys = resourceBundle.keySet();
		if(StringUtils.isNotBlank(regex)){
			
			for (String key : keys) {
				 if(key.matches(regex)){
					 configValue.put(key, resourceBundle.getString(key));
		         }
			}
		}else{
			for (String key : keys) {
				configValue.put(key, resourceBundle.getString(key));
			}
		}
		return configValue;
	}
	
	public static void main(String args[]){
		//System.out.println(getProperty("DBurl"));
		System.out.println(getIntegerProperty("user.sendCash"));
	}
}

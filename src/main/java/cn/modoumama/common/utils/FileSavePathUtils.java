package cn.modoumama.common.utils;
/** 
 * 类描述：文件本地保存路径<br>
 * <br/>
 * 创建人：邓强   <br>
 * 创建时间：2017年11月28日 下午3:45:38    <br> 
 * 修改人：  <br>
 * 修改时间：2017年11月28日 下午3:45:38   <br>  
 * 修改备注：     <br>
 * @version   V1.0      
 */
public class FileSavePathUtils {

	/** 文件保存地址*/
	public static String FILE_SAVE_PATH;
	/** 文件访问域名带协议头*/
	public static String FILE_URL_PATH;
	
	static{
		String savePath = null;
		String urlPath = null;
		try {
			savePath = ConfigProperty.getProperty("file.save.path");
			urlPath = StringUtils.getPathEnd(ConfigProperty.getProperty("file.url.path"));
			savePath = StringUtils.getPathNotStart(StringUtils.getPathEnd(savePath));
	        FILE_SAVE_PATH = PathUtil.ROOT_PATH+savePath;
	        FILE_URL_PATH=urlPath+savePath;
		} catch (Exception e) {
			StringBuffer oss = new StringBuffer();
			oss.append("需要在config.propertiesz中配置一下设置！").append("\r\n");
			oss.append("file.save.path=文件保存路径，项目发布目录为根目录").append("\r\n");
			oss.append("file.url.path=url访问路径").append("\r\n");
			throw new RuntimeException(oss.toString());
		}
		
	}
}

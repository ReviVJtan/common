package cn.modoumama.common.utils;

public class PathUtil {
	/** 项目存放启动地址*/
	public static String ROOT_PATH;
	/** 项目地址*/
	public static String PROJECT_PATH;
	/** 文件保存地址*/
	public static String FILE_SAVE_PATH;
	/** 文件访问域名带协议头*/
	public static String FILE_URL_PATH;
	static{
		String savePath = StringUtils.getPathNotStart(ConfigProperty.getProperty("file.save.path"));
		savePath = StringUtils.getPathEnd(savePath);
		String urlPath = StringUtils.getPathEnd(ConfigProperty.getProperty("file.url.path"));
		String path = PathUtil.class.getClassLoader().getResource("/").getPath();
		//操作系统
		String os = System.getProperty("os.name").toLowerCase();
		if(os.indexOf("windows")>=0){
			path = path.replaceAll("^/", "");
		}
        path = path.replace("%20"," ");
        path = path.replaceAll("/WEB-INF/classes/", "");
        PROJECT_PATH = StringUtils.getPathEnd(path);
        path = path.replaceAll("([^/]+)$", "");
        ROOT_PATH = StringUtils.getPathEnd(path);
        FILE_SAVE_PATH = path+savePath;
        FILE_URL_PATH=urlPath+savePath;
	}
}

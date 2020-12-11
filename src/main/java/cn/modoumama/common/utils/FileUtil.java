package cn.modoumama.common.utils;

import java.io.File;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作公共方法
 * @ClassName: FileUtil
 */
public class FileUtil {
	public static final Long M = 1024 * 1024L;
	/**
	 * 保存压缩文件
	 * @param file
	 * @return
	 */
	public static String saveCompressFile(MultipartFile file){
		final long maxSize = 300 * M;// 设置上传文件最大值  
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] { ".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso"}; 
		return saveFile(file, maxSize, allowedExt);
	}
	
	/**
	 * 保存文档文件
	 * @param file
	 * @return
	 */
	public static String saveDocFile(MultipartFile file){
		final long maxSize = 300 * M;// 设置上传文件最大值  
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] {".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt", ".md", ".xml"}; 
		return saveFile(file, maxSize, allowedExt);
	}
	
	/**
	 * 保存视频文件
	 * @param file
	 * @return
	 */
	public static String saveVideoFile(MultipartFile file){
		final long maxSize = 100 * M;// 设置上传文件最大值  
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",
		        ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid"}; 
		return saveFile(file, maxSize, allowedExt);
	}
	
	
	/**
	 * 保存图片
	 * @param file
	 * @return
	 */
	public static String saveImgageFile(MultipartFile file){
		final long maxSize = 30 * M;// 设置上传文件最大值  
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "png",  "swf", "bmp"}; 
		return saveFile(file, maxSize, allowedExt);
	}
	
	public static String saveFile(MultipartFile file){
		return saveFile(file, null, null);
	}
	
   /**
    * 保存文件到本地
    * @param file 文件
    * @param path 路径本地
    * @param url 网络路径
    * @return
    */
	public static String saveFile(MultipartFile file,final Long maxSize,final String[] allowedExt){
		String hUrl = null;
		if(file!=null){
			String fileName =  file.getOriginalFilename();
			
			if(maxSize != null && maxSize >0){
				if(file.getSize() > maxSize){
					return hUrl;
				}
			}
			
			//获取后缀
			String fileEx;
			try {
				fileEx = StringUtils.getFileSuffix(fileName);
			} catch (Exception e1) {
				return hUrl;
			}
			
			if(allowedExt != null && allowedExt.length>0){
				if(!(ArrayUtil.isContain(fileEx.substring(1), allowedExt))){
					return hUrl;
				}
			}
			

			fileName = UUID.randomUUID().toString().replace("-", "") + fileEx;
	        File targetFile = new File(PathUtil.FILE_SAVE_PATH, fileName);
	        if(!targetFile.exists()){  
	            targetFile.mkdirs();  
	        }
	        //保存  
	        try {  
	            file.transferTo(targetFile);
	            hUrl = PathUtil.FILE_URL_PATH+fileName;
	        } catch (Exception e) {  
	            e.printStackTrace();
	        }
		}
		
		return hUrl;
	}
}

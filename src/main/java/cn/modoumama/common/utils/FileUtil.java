package cn.modoumama.common.utils;

import java.io.File;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作公共方法
 * @ClassName: FileUtil
 */
public class FileUtil {
   /**
    * 保存文件到本地
    * @param file 文件
    * @param path 路径本地
    * @param url 网络路径
    * @return
    */
	public static String saveImgageFile(MultipartFile file){
		final long MAX_SIZE = 300 * 1024 * 1024;// 设置上传文件最大值  
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "png",  "swf", "bmp"}; 
		String hUrl = null;
		if(file!=null){
			String fileName =  file.getOriginalFilename();
			
			if(file.getSize() > MAX_SIZE){
				return hUrl;
			}
			//获取后缀
			String fileEx;
			try {
				fileEx = StringUtils.getFileSuffix(fileName);
			} catch (Exception e1) {
				return hUrl;
			}
			
			if(!(ArrayUtil.isContain(fileEx.substring(1), allowedExt))){
				return hUrl;
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

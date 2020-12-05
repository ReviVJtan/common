package cn.modoumama.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import cn.modoumama.common.exception.RequiredException;

/**
 * @author 马正正
 * @ClassName: FileUtil
 * @Description: 文件操作公共方法
 * @date 2015年6月24日
 */
public class AliyunOOSFileUtil {
    private static Log logger = LogFactory.getLog(AliyunOOSFileUtil.class);
    private static String bucketName = ConfigProperty.getProperty("oss.bucketName");
    private static String bucketUrl = ConfigProperty.getProperty("oss.url");
    /**
     * OSSClient对象
     */
    private static OSSClient client;

    static {
        init();
    }

    /**
     * @Description: 初始化
     */
    private static void init() {
        String endpoint = "http://"+ConfigProperty.getProperty("oss.endpoint")+".aliyuncs.com";
        String accessKeyId = ConfigProperty.getProperty("oss.accessKeyId");
        String accessKeySecret = ConfigProperty.getProperty("oss.accessKeySecret");
        client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }
    
    /**上传到阿里云</p> 
	 * @param file
	 * @return   
	 * @author 邓强
	 * @date 2017年2月23日下午1:12:04
	 */
	public static String getUpImgUrl(MultipartFile file){
		final long MAX_SIZE = 300 * 1024 * 1024;// 设置上传文件最大值  
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "png", "bmp"}; 
		String hUrl = null;
		if(file!=null){
			String fileName =  file.getOriginalFilename();
			if(!StringUtils.isBlank(fileName)){
				if(file.getSize() > MAX_SIZE){
					return hUrl;
				}
				//获取后缀
				String fileEx;
				try {
					fileEx = fileName.substring(fileName.lastIndexOf("."));
				} catch (Exception e1) {
					throw new RequiredException("：上传文件没有后缀名");
				}
				
				if(!(ArrayUtil.isContain(fileEx.substring(1), allowedExt))){
					 throw new RequiredException("：上传文件格式不合法");
				}
				//新的文件名
				fileName = "healthy-plan/" + UUID.randomUUID().toString().replace("-", "") + fileEx;
				
				//新闻图标，存储到oss
				try {
					hUrl = AliyunOOSFileUtil.uploadFile(file.getInputStream(), fileName, file.getBytes().length * 1L);
				} catch (IOException e) {
					logger.error("上传图片失败：", e);
					return  "false" ;
				}
			}
		}
		return hUrl;
	}

    /**
     * @param input    输入流
     * @param fileName 文件名(可以是url形式a/b/c.txt,开头不能是/)
     * @param lenth    文件大小(字节数)
     * @return 文件url
     * @Description: 文件上传
     */
    public static String uploadFile(InputStream input, String fileName, long lenth) {
        logger.info("文件上传到阿里云");

        String result = bucketUrl + fileName;                    //返回OSS文件地址 绝对地址

        ObjectMetadata objectMeta = new ObjectMetadata();    //OSS上传
        objectMeta.setContentLength(lenth);

        long startTime = System.currentTimeMillis();

        PutObjectResult put = client.putObject(bucketName, fileName, input, objectMeta);

        logger.info("返回的结果" + put.getETag() + "---文件上传到oss的时间 :" + (System.currentTimeMillis() - startTime));

        return result;
    }

    public static String uploadFile(MultipartFile image, String dir) throws IOException {
        if (image != null && org.apache.commons.lang3.StringUtils.isNotBlank(image.getOriginalFilename())) {
            String fileName = image.getOriginalFilename();
            //获取后缀
            String fileEx = fileName.substring(fileName.lastIndexOf("."));
            //新的文件名
            fileName = dir + UUID.randomUUID().toString().replace("-", "") + fileEx;
            //存储到oss
            return uploadFile(image.getInputStream(), fileName, image.getBytes().length * 1L);
        }
        return "";
    }

    /**
     * @param content  文件
     * @param fileName 文件名(可以是url形式a/b/c.txt,开头不能是/)
     * @return
     * @Description: 文件上传
     */
    public static String uploadFile(byte[] content, String fileName) {
        return uploadFile(new ByteArrayInputStream(content), fileName, content.length);
    }

    public static String uploadFile(File file, String fileName) {
        String path = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            path = uploadFile(is, fileName, file.length());
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
        return path;
    }

    /**
     * @param fileUrl 文件路径
     * @Description: 文件删除
     */
    public static void deleteFileOSS(String fileUrl) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(fileUrl)) {
            fileUrl = fileUrl.replaceFirst("http://[^/]+/(.+)", "$1");
            client.deleteObject(bucketName, fileUrl);
        }
    }

    /**
     * 批量删除文件
     *
     * @param fileList
     */
    public static void deleteFileListOSS(List<String> fileList) {
        if (fileList != null && fileList.size() > 0) {
            for (String fileName : fileList) {
                deleteFileOSS(fileName);
            }
        }
    }
}

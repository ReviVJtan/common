package cn.modoumama.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SimpleTimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.dsig.SignatureMethod;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import cn.modoumama.common.exception.RequiredException;

/**
 * 类描述：阿里云工具类<br>
 * 创建人：邓强   <br>
 * 创建时间：2017年11月20日 上午10:25:26    <br> 
 * 修改人：  <br>
 * 修改时间：2017年11月20日 上午10:25:26   <br>  
 * 修改备注：     <br>
 * @version   V1.0
 */
public class AliyunOOSUtil {
	private static Logger logger = LoggerFactory.getLogger(AliyunOOSUtil.class);
    // OSS域名，如http://oss-cn-hangzhou.aliyuncs.com
    public static final String endpoint = ConfigProperty.getProperty("oss.endpoint");
    // AccessKey请登录https://ak-console.aliyun.com/#/查看
    private static final String accessKeyId = ConfigProperty.getProperty("oss.accessKeyId");
    private static final String accessKeySecret = ConfigProperty.getProperty("oss.accessKeySecret");
    // 你之前创建的bucket，确保这个bucket已经创建
    public static final String bucketName = ConfigProperty.getProperty("oss.bucketName");
    private static String bucketUrl = ConfigProperty.getProperty("oss.url");
    //上传时间戳
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static final String ENCODE_TYPE = "UTF-8";

    private static AliyunOOSUtil aliyunOOSUtil = null;
    
    /**
     * OSSClient对象
     */
    private static OSSClient client;


    private AliyunOOSUtil() {
    }

    public static AliyunOOSUtil getInstance() {
        if (aliyunOOSUtil == null) {
            synchronized (AliyunOOSUtil.class) {
                if (aliyunOOSUtil == null) {
                    aliyunOOSUtil = new AliyunOOSUtil();
                }
            }
        }
        return aliyunOOSUtil;
    }
    
    static{
    	getInstance();
    	init();
    }
    
    /**
     * @Description: 初始化
     */
    private static void init() {
        String endpoint = "http://"+ConfigProperty.getProperty("oss.endpoint")+".aliyuncs.com";
        client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * ********************************** 文件上传签名start ******************************************
     */
    public static Map<String, String> getSigna() throws Exception {
        // 提交表单的URL为bucket域名
        String urlStr = "http://" + bucketName + "." + endpoint + ".aliyuncs.com";

        // 表单域
        Map<String, String> formFields = new LinkedHashMap<String, String>();

        formFields.put("host", urlStr);
        // OSSAccessKeyId
        formFields.put("accessid", accessKeyId);
        //失效时间时间30分钟
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        String strDate = formatIso8601Date(calendar.getTime());
        // policy
        String policy = "{\"expiration\": \"" + strDate + "\",\"conditions\": [[\"content-length-range\", 0, 104857600]]}";
        String encodePolicy = new String(Base64.encodeBase64(policy.getBytes()));
        formFields.put("policy", encodePolicy);
        // Signature
        String signaturecom = computeSignature(accessKeySecret, encodePolicy);
        formFields.put("signature", signaturecom);

        return formFields;
    }
    
    public static String getSubmitSnapshotJobSigna(String url) throws Exception {
        JSONObject input = new JSONObject();
        input.put("Bucket", bucketName);
        input.put("Location", endpoint);
        input.put("Object", url);
        JSONObject outputFile = new JSONObject();
        outputFile.put("Bucket", bucketName);
        outputFile.put("Location", endpoint);
        outputFile.put("Object", url.replace("/\\w+$/", ".jpg"));
        JSONObject snapshotConfig = new JSONObject();
        snapshotConfig.put("OutputFile", outputFile);
        snapshotConfig.put("Time", "5");

        Map<String, String> pram = new HashMap<>();
        pram.put("Input", input.toString());
        pram.put("SnapshotConfig", snapshotConfig.toString());
        pram.put("Action", "SubmitJobs");

        return getMediaSignaUrl(pram);
    }

    /**
     * ******************************************** 文件上传签名end ****************************************
     */


    private static String formatIso8601Date(Date date) {

        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);

        df.setTimeZone(new SimpleTimeZone(0, "GMT"));

        return df.format(date);

    }

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        if (value == null) return null;
        return URLEncoder.encode(value, ENCODE_TYPE).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    private static String computeSignature(String accessKeySecret, String encodePolicy)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        // convert to UTF-8
        byte[] key = accessKeySecret.getBytes("UTF-8");
        byte[] data = encodePolicy.getBytes("UTF-8");

        // hmac-sha1
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key, "HmacSHA1"));
        byte[] sha = mac.doFinal(data);

        // base64
        return new String(Base64.encodeBase64(sha));
    }

    /**************************************************** 媒体服务start *****************************************************/
    /**
     * 前端js获取签名调用示例：
     * <code>
     * $.ajax(ctx + "/adver/getSnapshotUrl", {
     * type: "POST",
     * dataType: "json",
     * data: {
     * Action: "SubmitSnapshotJob",
     * Input: JSON.stringify({
     * Bucket: bucket,
     * Location: endpoint,
     * Object: fileName
     * }),
     * SnapshotConfig: JSON.stringify({
     * OutputFile: {
     * Bucket: bucket,
     * Location: endpoint,
     * Object: outputName
     * },
     * Time: 1000
     * })
     * },
     * success: function (__data__) {
     * todo something
     * },
     * error: function (__error__) {
     * console.log(__error__);
     * }
     * });
     * </code>
     */

    private static final String HTTP_METHOD = "GET";
    private static final String SEPARATOR = "&";
    private static final String EQUAL = "=";
    private static final String ALGORITHM = "HmacSHA1";

    /**
     * 获取带签名的媒体服务接口请求URL
     *
     * @param map 媒体服务接口特有的参数
     * @return
     * @throws Exception
     */
    public static String getMediaSignaUrl(Map<String, String> map) throws Exception {
        Map<String, String> parameterMap = new HashMap<String, String>();
        // 加入请求公共参数
        parameterMap.put("Version", "2014-06-18");
        parameterMap.put("AccessKeyId", accessKeyId); //此处请替换成您自己的AccessKeyId
        parameterMap.put("Timestamp", formatIso8601Date(new Date()));
        parameterMap.put("SignatureMethod", "HMAC-SHA1");
        parameterMap.put("SignatureVersion", "1.0");
        parameterMap.put("SignatureNonce", UUID.randomUUID().toString());
        parameterMap.put("Format", "json");

        // 加入方法特有参数
        if (map != null && map.size() > 0) {
            for (Entry<String, String> e : map.entrySet()) {
                parameterMap.put(e.getKey(), e.getValue());
            }
        }

        // 对参数进行排序
        List<String> sortedKeys = new ArrayList<String>(parameterMap.keySet());
        Collections.sort(sortedKeys);

        // 生成stringToSign字符
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(HTTP_METHOD).append(SEPARATOR);
        stringToSign.append(percentEncode("/")).append(SEPARATOR);
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            // 此处需要对key和value进行编码
            String value = parameterMap.get(key);
            canonicalizedQueryString.append(SEPARATOR).append(percentEncode(key)).append(EQUAL).append(percentEncode(value));
        }

        // 此处需要对canonicalizedQueryString进行编码
        stringToSign.append(percentEncode(canonicalizedQueryString.toString().substring(1)));

        SecretKey key = new SecretKeySpec((accessKeySecret + SEPARATOR).getBytes(ENCODE_TYPE), SignatureMethod.HMAC_SHA1);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(key);
        String signature = URLEncoder.encode(new String(
                new Base64().encode(mac.doFinal(stringToSign.toString().getBytes(ENCODE_TYPE))),
                ENCODE_TYPE), ENCODE_TYPE);

        // 生成请求URL
        StringBuilder requestURL;
        requestURL = new StringBuilder("http://mts.aliyuncs.com?");
        requestURL.append(URLEncoder.encode("Signature", ENCODE_TYPE)).append("=").append(signature);
        for (Entry<String, String> e : parameterMap.entrySet()) {
            requestURL.append("&").append(percentEncode(e.getKey())).append("=").append(percentEncode(e.getValue()));
        }

        return requestURL.toString();
    }


    /**************************************************** 媒体服务end *****************************************************/
    public static final Long M = 1024 * 1024L;
    
    /**
     * 保存压缩文件<br>
     * <br>  
     * 创建人：邓强   <br>
     * 创建时间：2017年11月20日 上午10:42:52    <br> 
     * 修改人：  <br>
     * 修改时间：2017年11月20日 上午10:42:52   <br>  
     * 修改备注：     <br> 
     * @param file
     * @return
     */
    public static String saveCompressFile(MultipartFile file){
    	return saveCompressFile(file, null, null);
    }

    /**
     * 保存压缩文件<br>
     * <br>  
     * 创建人：邓强   <br>
     * 创建时间：2017年11月20日 上午10:34:47    <br> 
     * 修改人：  <br>
     * 修改时间：2017年11月20日 上午10:34:47   <br>  
     * 修改备注：     <br> 
     * @param file
     * @param dir	保存文件的路径
     * @return
     */
    public static String saveCompressFile(MultipartFile file, String dir){
    	return saveCompressFile(file, null, dir);
    }
    
    /**
     * 保存压缩文件<br>
     * <br>  
     * 创建人：邓强   <br>
     * 创建时间：2017年11月20日 上午10:31:48    <br> 
     * 修改人：  <br>
     * 修改时间：2017年11月20日 上午10:31:48   <br>  
     * 修改备注：     <br> 
     * @param file
     * @param maxSize	设置上传文件最大值  
     * @param dir		保存文件的路径
     * @return
     */
	public static String saveCompressFile(MultipartFile file, Long maxSize, String dir){
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] { ".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso"}; 
		return uploadFile(file, maxSize, allowedExt, dir);
	}
	
	/**
	 * 保存文档文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:43:20    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:43:20   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @return
	 */
	public static String saveDocFile(MultipartFile file){
		return saveDocFile(file, null, null);
	}
	/**
	 * 保存文档文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:35:04    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:35:04   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param dir	保存文件的路径
	 * @return
	 */
	public static String saveDocFile(MultipartFile file, String dir){
		return saveDocFile(file, null, dir);
	}
	
	/**
	 * 保存文档文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:35:15    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:35:15   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param maxSize	文件的最大值
	 * @param dir	保存文件的路径
	 * @return
	 */
	public static String saveDocFile(MultipartFile file, Long maxSize, String dir){
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] {".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt", ".md", ".xml"}; 
		return uploadFile(file, maxSize, allowedExt, dir);
	}
	
	/**
	 * 保存视频文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:43:46    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:43:46   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @return
	 */
	public static String saveVideoFile(MultipartFile file){
		return saveVideoFile(file, null, null);
	}
	
	/**
	 * 保存视频文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:35:23    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:35:23   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param dir	保存文件的路径
	 * @return
	 */
	public static String saveVideoFile(MultipartFile file, String dir){
		return saveVideoFile(file, null, dir);
	}
	
	/**
	 * 保存视频文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:35:35    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:35:35   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param maxSize	文件的最大值
	 * @param dir	保存文件的路径
	 * @return
	 */
	public static String saveVideoFile(MultipartFile file, Long maxSize, String dir){
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] { ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",
		        ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid"}; 
		return uploadFile(file, maxSize, allowedExt, dir);
	}
	
	/**
	 * 保存图片<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:44:22    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:44:22   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @return
	 */
	public static String saveImgageFile(MultipartFile file){
		return saveImgageFile(file, null, null);
	}
	
	/**
	 * 保存图片<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:35:42    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:35:42   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param dir	保存文件的路径
	 * @return
	 */
	public static String saveImgageFile(MultipartFile file, String dir){
		return saveImgageFile(file, null, dir);
	}
	
	/**
	 * 保存图片<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:35:55    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:35:55   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param maxSize	文件的最大值
	 * @param dir	保存文件的路径
	 * @return
	 */
	public static String saveImgageFile(MultipartFile file, Long maxSize, String dir){
        // 允许上传的文件格式的列表  
		final String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "png",  "swf", "bmp"}; 
		return uploadFile(file, maxSize, allowedExt, dir);
	}
	
	/**
	 * 保存文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:45:12    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:45:12   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @return
	 */
	public static String saveFile(MultipartFile file){
		return uploadFile(file, null, null, null);
	}
	
	/**
	 * 保存文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:36:04    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:36:04   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param dir	保存文件的路径
	 * @return
	 */
	public static String saveFile(MultipartFile file, String dir){
		return uploadFile(file, null, null, dir);
	}
	
	/**
	 * 保存文件<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:36:20    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:36:20   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param maxSize	文件的最大值
	 * @param dir	保存文件的路径
	 * @return
	 */
	public static String saveFile(MultipartFile file, Long maxSize , String dir){
		return uploadFile(file, maxSize, null, dir);
	}
    
    /**
     * 上传到阿里云上传文件到<br>
     * <br>  
     * 创建人：邓强   <br>
     * 创建时间：2017年11月20日 上午9:14:29    <br> 
     * 修改人：  <br>
     * 修改时间：2017年11月20日 上午9:14:29   <br>  
     * 修改备注：     <br> 
     * @param file			上传的文件
     * @param maxSize		文件的最大值
     * @param allowedExt	允许上传的文件格式的列表  
     * @return
     */
	public static String uploadFile(MultipartFile file, Long maxSize, String[] allowedExt, String dir){
		String hUrl = null;
		if(file!=null){
			if(verification(file, maxSize, allowedExt)){
				hUrl = uploadFile(file,dir);
			}
		}
		
		return hUrl;
	}

	/**
	 * 验证文件大小，和后缀名<br>
	 * <br>  
	 * 创建人：邓强   <br>
	 * 创建时间：2017年11月20日 上午10:27:29    <br> 
	 * 修改人：  <br>
	 * 修改时间：2017年11月20日 上午10:27:29   <br>  
	 * 修改备注：     <br> 
	 * @param file
	 * @param maxSize
	 * @param allowedExt
	 * @return
	 */
	public static boolean verification(MultipartFile file, Long maxSize, String[] allowedExt){
		boolean flag = false;
		if(file!=null){
			String fileName =  file.getOriginalFilename();
			if(!StringUtils.isBlank(fileName)){
				if(file.getSize() <= maxSize){
					
					if(allowedExt != null){
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
					}
					
					flag = true;
				}
			}
		}
		return flag;
	}
	
   

    public static String uploadFile(MultipartFile file, String dir){
    	String fileName = file.getOriginalFilename();
        if (file != null && StringUtils.isNotBlank(fileName)) {
            //获取后缀
            String fileEx = "";
            if(fileName.lastIndexOf(".") != -1){
            	fileEx = fileName.substring(fileName.lastIndexOf("."));
            }
           
            if(StringUtils.isNotBlank(dir)){
            	dir =DateUtils.dateFormat(new Date(), "yyyy/MM/dd");
            }
            
            //新的文件名
            fileName = dir + UUID.randomUUID().toString().replace("-", "") + fileEx;
            //存储到oss
            try {
				fileName = uploadFile(file.getInputStream(), fileName, file.getBytes().length * 1L);
				return fileName;
			} catch (IOException e) {
				throw new RequiredException("文件非法",e);
			}
            
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
     * @param input    输入流
     * @param fileName 文件名(可以是url形式a/b/c.txt,开头不能是/)
     * @param lenth    文件大小(字节数)
     * @return 文件url
     * @Description: 文件上传
     */
    public static String uploadBaidu(String baiduEdit,String dir) {
        ////{"state": "SUCCESS","title": "20170303_1488502564030022030.jpg","original": "joinplan1.jpg","type": ".jpg","url": "/static/upload/file/20170303_1488502564030022030.jpg","size": "38928"}
    	JSONObject jsonB = JSONObject.parseObject(baiduEdit);
    	System.out.println(baiduEdit);
    	JSONArray list = jsonB.getJSONArray("list");
    	if(list != null){
    		for (Object object : list) {
    			JSONObject jsonF = (JSONObject) object;
    			String url = jsonF.getString("url");
        		url = uploadBaiduFiles(url, dir);
        		if(StringUtils.isNotBlank(url)){
        			jsonF.put("url", url);
        		}
			}
    	}else{
    		String url = jsonB.getString("url");
    		url = uploadBaiduFiles(url, dir);
    		if(StringUtils.isNotBlank(url)){
    			jsonB.put("url", url);
    		}
    		
    	}
    	
    	return jsonB.toJSONString();
    }
    public static String uploadBaiduFiles(String url,String dir) {
    	if(StringUtils.isNotBlank(url)){
    		String fileUrl = StringUtils.getPathNotEnd(PathUtil.PROJECT_PATH)+url;
        	String fileName = dir+url;
        	File file = new File(fileUrl);
        	url = uploadFile(file,fileName);
        	if(StringUtils.isNotBlank(url)){
        		file.deleteOnExit();
        	}
    	}
    	return url;
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



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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * 类描述：阿里云工具类<br>
 * 创建人：邓强   <br>
 * 创建时间：2017年11月20日 上午10:25:26    <br> 
 * 修改人：  <br>
 * 修改时间：2017年11月20日 上午10:25:26   <br>  
 * 修改备注：     <br>
 * @version   V1.0
 */
public class AliyunOOSUtils {
	private static Logger logger = LoggerFactory.getLogger(AliyunOOSUtils.class);
    // OSS域名，如oss-cn-beijing
    protected static String endpoint;
    // AccessKey请登录https://ak-console.aliyun.com/#/查看
    protected static String accessKeyId;
    protected static String accessKeySecret;
    // 你之前创建的bucket，确保这个bucket已经创建
    protected static String bucketName;
    //阿里云端点的域名
    protected static String endpointUrl;
    //阿里云实例的域名
    protected static String bucketUrl;
    //自定义域名
    protected static String ourUrl;
    //文件保存的路径
    protected static String keyPath;
    //上传时间戳
    protected static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    protected static final String ENCODE_TYPE = "UTF-8";

    protected static AliyunOOSUtils aliyunOOSUtil = null;
    
    /**
     * OSSClient对象
     */
    protected static OSSClient client;

    public static AliyunOOSUtils getInstance() {
        if (aliyunOOSUtil == null) {
            synchronized (AliyunOOSUtils.class) {
                if (aliyunOOSUtil == null) {
                    aliyunOOSUtil = new AliyunOOSUtils();
                }
            }
        }
        return aliyunOOSUtil;
    }
    
    static{
    	try {
			endpoint = ConfigProperty.getProperty("oss.endpoint");
			accessKeyId = ConfigProperty.getProperty("oss.accessKeyId");
			accessKeySecret = ConfigProperty.getProperty("oss.accessKeySecret");
			keyPath = ConfigProperty.getProperty("oss.keyPath");
			bucketName = ConfigProperty.getProperty("oss.bucketName");
			endpointUrl = "http://"+endpoint+".aliyuncs.com";
	    	bucketUrl = "https://" + bucketName + "." + endpoint + ".aliyuncs.com";
		} catch (Exception e) {
			StringBuffer oss = new StringBuffer();
			oss.append("需要在config.propertiesz中配置一下设置！").append("\r\n");
			oss.append("oss.accessKeyId=您的AccessKey").append("\r\n");
			oss.append("oss.accessKeySecret=您的accessKeySecret").append("\r\n");
			oss.append("oss.keyPath=默认保存路径").append("\r\n");
			oss.append("oss.endpoint=您的OSS端点如：oss-cn-beijing").append("\r\n");
			oss.append("oss.bucketName=您的OSS实例名").append("\r\n");
			oss.append("#选填").append("\r\n");
			oss.append("oss.url=在OSS上自定义的域名").append("\r\n");
			throw new RuntimeException(oss.toString());
		}
    	String ourUrlStr;
		try {
			ourUrlStr = ConfigProperty.getProperty("oss.url");
			if(StringUtils.isBlank(ourUrlStr)){
	    		ourUrl = bucketUrl;
	    	}else{
	    		ourUrl = ourUrlStr;
	    	}
		} catch (Exception e) {
			ourUrl = bucketUrl;
		}
    	
    	
    	
    	
    	
    	getInstance();
    	init();
    }
    
    /**
     * @Description: 初始化
     */
    private static void init() {
        client = new OSSClient(endpointUrl, accessKeyId, accessKeySecret);
    }

    /**
     * ********************************** 文件上传签名start ******************************************
     */
    public static Map<String, String> getSigna() throws Exception {

        // 表单域
        Map<String, String> formFields = new LinkedHashMap<String, String>();

        formFields.put("host", ourUrl);
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

        String result = StringUtils.getPathEnd(ourUrl) + StringUtils.getPathNotStart(fileName);                    //返回OSS文件地址 绝对地址

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



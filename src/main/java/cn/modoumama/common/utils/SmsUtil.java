package cn.modoumama.common.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
/**
 * 
  * @ClassName: SmsUtil
  * @Description: 短信工具类
 */
public class SmsUtil {
	private final static Log log = LogFactory.getLog(SmsUtil.class);
	
	//初始化ascClient需要的几个参数
	private final static String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
	private final static String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
	
	/**
	  * @Description: 阿里短信发送短信
	  * @param phone  电话  				示例：15871383472
	  * @param smsTemplateCode			示例：SMS_3685120
	  * @param smsParam					示例：{\"code\":\"1234\"}
	  * @param smsSign					示例：身份验证
	  * @return  true/false(成功/失败)
	 */
	public static SendSmsResponse sendByAliyun(String phone, String smsTemplateCode, String smsParam, String smsSign){
		SendSmsResponse sendSmsResponse = null;
		try {
			//设置超时时间-可自行调整
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			//替换成你的AK
			final String accessKeyId = getText("sms.aliyun.accessKeyId");//你的accessKeyId,参考本文档步骤2
			final String accessKeySecret = getText("sms.aliyun.accessKeySecret");//你的accessKeySecret，参考本文档步骤2
			//初始化ascClient,暂时不支持多region
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
			accessKeySecret);
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			IAcsClient acsClient = new DefaultAcsClient(profile);
			 //组装请求对象
			 SendSmsRequest request = new SendSmsRequest();
			 //使用post提交
			 request.setMethod(MethodType.POST);
			 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
			 request.setPhoneNumbers(phone);
			 //必填:短信签名-可在短信控制台中找到
			 request.setSignName(smsSign);
			 //必填:短信模板-可在短信控制台中找到
			 request.setTemplateCode(smsTemplateCode);
			 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
			 request.setTemplateParam(smsParam);
			 //可选-上行短信扩展码(无特殊需求用户请忽略此字段)
			 //request.setSmsUpExtendCode("90997");
			 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			 request.setOutId("yourOutId");
			//请求失败这里会抛ClientException异常
			sendSmsResponse = acsClient.getAcsResponse(request);
		} catch (Exception e) {
			 log.error("阿里短信发送异常",e);
			 throw new RuntimeException("阿里短信发送异常");
		}
		
		return sendSmsResponse;
	}
	
	  public static QuerySendDetailsResponse querySendDetails(String phone,Date sendDate,String bizId) throws ClientException {

	        //可自助调整超时时间
	        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
	        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

	      //替换成你的AK
			final String accessKeyId = getText("sms.aliyun.accessKeyId");//你的accessKeyId,参考本文档步骤2
			final String accessKeySecret = getText("sms.aliyun.accessKeySecret");//你的accessKeySecret，参考本文档步骤2
	        
	        //初始化acsClient,暂不支持region化
	        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
	        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
	        IAcsClient acsClient = new DefaultAcsClient(profile);

	        //组装请求对象
	        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
	        //必填-号码
	        request.setPhoneNumber(phone);
	        //可选-流水号
	        request.setBizId(bizId);
	        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
	        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
	        request.setSendDate(ft.format(sendDate));
	        //必填-页大小
	        request.setPageSize(10L);
	        //必填-当前页码从1开始计数
	        request.setCurrentPage(1L);

	        //hint 此处可能会抛出异常，注意catch
	        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

	        return querySendDetailsResponse;
	    }
	  
	/**
	 * 
	  * @Description: 获取资源配置
	  * @param key
	  * @return
	 */
	private static String getText(String key){
		return ConfigProperty.getProperty(key);
	}
	
}

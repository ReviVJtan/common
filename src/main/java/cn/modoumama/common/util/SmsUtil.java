package cn.modoumama.common.util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
/**
 * 
  * @ClassName: SmsUtil
  * @Description: 短信工具类
 */
public class SmsUtil {
	private final static Log log = LogFactory.getLog(SmsUtil.class);
	
	/**
	  * @Description: 阿里短信发送短信
	  * @param phone  电话  				示例：15871383472
	  * @param smsTemplateCode			示例：SMS_3685120
	  * @param smsParam					示例：{\"code\":\"1234\"}
	  * @param smsSign					示例：身份验证
	  * @return  true/false(成功/失败)
	 */
	public static boolean sendByAliyun(String phone, String smsTemplateCode, String smsParam, String smsSign){
		boolean result = true;
		try {
			//设置超时时间-可自行调整
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			//初始化ascClient需要的几个参数
			final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
			final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
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
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				//请求成功
				result = true;
			}else{
				result = false;
			}
			
			return result;
		} catch (Exception e) {
			 log.error("阿里短信发送异常",e);
			 throw new RuntimeException("阿里短信发送异常");
		}
	}

	/**
	  * @Description: 阿里大鱼发送短信
	  * @param phone  电话  				示例：15871383472
	  * @param smsTemplateCode			示例：SMS_3685120
	  * @param smsParam					示例：{\"code\":\"1234\"}
	  * @param smsSign					示例：身份验证
	  * @return  true/false(成功/失败)
	 */
	public static boolean sendByAlidayu(String phone, String smsTemplateCode, String smsParam, String smsSign){
		boolean result = true;
		try {
			String appkey = getText("sms.alidayu.appkey");
			String secret = getText("sms.alidayu.secret");
			String addr = getText("sms.alidayu.addr");
			TaobaoClient client = new DefaultTaobaoClient(addr, appkey, secret);
			AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
			req.setSmsType("normal");
			req.setSmsFreeSignName(smsSign);
			//参数
			req.setSmsParamString(smsParam);
			req.setRecNum(phone);
			req.setSmsTemplateCode(smsTemplateCode);
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req, secret);
			
			if(rsp != null && StringUtils.isNotBlank(rsp.getBody())){
				//返回错误信息
				if(rsp.getBody().indexOf("error_response") != -1){
					log.error(rsp.getBody());
					result = false;
				}
			}else{
				result = true;
			}
			
			return result;
		} catch (Exception e) {
			 log.error("阿里大鱼发送短信异常",e);
			 throw new RuntimeException("阿里大鱼发送短信异常");
		}
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

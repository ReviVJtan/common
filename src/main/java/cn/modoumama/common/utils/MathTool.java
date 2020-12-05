package cn.modoumama.common.utils;

import java.math.BigDecimal;

/**
 * 项目名称：Remoting_Common  
 *  
 * 类名称：cn.modoumama.utils.MathTool
 * 类描述：常用数学公式
 * 创建人：邓强
 * 创建时间：2016-9-7 下午1:39:21   
 * 修改人：  
 * 修改时间：  
 * 修改备注：     
 * @version   V1.0      
 */
public class MathTool {

	/**
	 * 保留单精度小数位个数
	 * @param number  保留小数位个数
	 * @param value  需要修改的值
	 * @return  返回保留小数位之后的值
	 */
	public static Float decimal(int number,Float value){
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(number, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
	/**
	 * 保留双精度小数位个数
	 * @param number  保留小数位个数
	 * @param value 需要修改的值
	 * @return 返回保留小数位之后的值
	 */
	public static Double decimal(int number,Double value){
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(number, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * @Title: toInt  
	 * @Description: 转整形
	 * @param value
	 * @return
	 */
	public static int toInt(Float value){
		BigDecimal bd = new BigDecimal(value);
		return bd.intValue();
	}
	
	/**
	 * @Title: toInt  
	 * @Description: 转整形
	 * @param value
	 * @return
	 */
	public static int toInt(Double value){
		BigDecimal bd = new BigDecimal(value);
		return bd.intValue();
	}
}

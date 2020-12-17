package cn.modoumama.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import cn.modoumama.common.spring.converter.ObjectToDateConverter;

/**
 * 项目名称：Health_Scale_Common
 * <p/>
 * 类名称：com.daboo.utils.ObjeactUtil
 * 类描述：
 * 创建人：邓强
 * 创建时间：2016年11月25日 下午1:38:52
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */

public class ObjectUtils {
    static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);
    static ConversionService conversionService;
    static {
    	Set<Converter> converters = new HashSet<>();
    	converters.add(new ObjectToDateConverter());
    	ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
    	conversionServiceFactoryBean.setConverters(converters);
    	conversionServiceFactoryBean.afterPropertiesSet();
    	conversionService = conversionServiceFactoryBean.getObject();
	}

    @SuppressWarnings("all")
	public static  <T> T copyProperties(T target, Object source) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
    	if (target == null) {
    		throw new IllegalArgumentException("No destination bean specified");
    	}
    	if (source == null) {
    		throw new IllegalArgumentException("No origin bean specified");
    	}
    	if (logger.isDebugEnabled()) {
    		logger.debug("BeanUtils.copyProperties(" + target + ", " + source + ")");
    	}
    	if (source instanceof Map) {
    		Iterator entries = ((Map)source).entrySet().iterator();
    		while (entries.hasNext()) {
    			Map.Entry entry = (Map.Entry)entries.next();
    			String name = (String)entry.getKey();
    			copyProperty(target, name, entry.getValue());
    		}
    	}else {
        	Method read = null;
        	Object value = null;
    		Field[] fields = source.getClass().getDeclaredFields();
    		for (Field field : fields) { 
                String name = field.getName();
                try {
                	PropertyDescriptor prop = new PropertyDescriptor(name, source.getClass());
            		read = prop.getReadMethod();
            		value = read.invoke(source);
                    copyProperty(target, name, value);	

                } catch (Exception e) {
                		logger.error(e.getMessage(),e);
                }  
    		}
		}
	     return target;
     
    }
    
    public static <T> boolean copyProperty(T target,String name, Object value) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    	boolean flag = false;
    	
    	if(value != null){
    		flag = true;
    		if(value instanceof CharSequence){
    			CharSequence valueChar = (CharSequence) value;
    			if(StringUtils.isBlank(valueChar)){
    				flag = false;
    			}
    		}
    	}
    	
    	if(flag){
    		try {
				PropertyDescriptor prop = new PropertyDescriptor(name, target.getClass());
				Method write = prop.getWriteMethod();
				Class<?>[] parameterTypes = write.getParameterTypes();
				write.invoke(target, conversionService.convert(value, parameterTypes[0]));
				flag = true;
			} catch (Exception e) {
				flag = false;
			}
    	}
		return flag;
    }
    /**
     * @return 0.其他类型；1.基本类型；2.包装类型
     */
    public static int isPrimitive(Object obj){
    	int flag = 0;
    	if(obj.getClass().isPrimitive()) {
    		flag = 1;
		}else if(obj instanceof Integer 
    	   || obj instanceof Long
    	   || obj instanceof Boolean 
    	   || obj instanceof Byte 
    	   || obj instanceof Short 
    	   || obj instanceof Float 
    	   || obj instanceof Double 
    	   || obj instanceof String
    	   || obj instanceof StringBuffer 
    	   || obj instanceof StringBuilder
    	){
    		flag = 2;
    	}
    	
    	return flag;
    }
}
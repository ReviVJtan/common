package cn.modoumama.common.spring.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cn.modoumama.common.utils.DateUtils;

@Component
public class ObjectToDateConverter  implements Converter<Object,Date>{

	@Override
	public Date convert(Object source) {
		if(source != null){
			if(source instanceof String){
				return DateUtils.getDateFromString(source.toString());
			}else if(source instanceof Long){
				Long time = (Long) source;
				return new Date(time);
			}else  if(source instanceof Date){  
	            return (Date) source;
	        }
		}
		return null;
	}

}
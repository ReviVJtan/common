package cn.modoumama.common.utils.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;

public class ValidatorUtils {
	
	public static Validator validator;
	static{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	private ValidatorUtils(){}
	
	public static <T> Set<ConstraintViolation<T>> validate(T paramT, Class<?>... paramArrayOfClass){
		return validator.validate(paramT, paramArrayOfClass);
	}
	
	public static  <T> Set<ConstraintViolation<T>> validateProperty(T paramT, String paramString, Class<?>... paramArrayOfClass){
		return validator.validateProperty(paramT, paramString, paramArrayOfClass);
	}

	public static  <T> Set<ConstraintViolation<T>> validateValue(Class<T> paramClass, String paramString, Object paramObject, Class<?>... paramArrayOfClass){
		return validator.validateValue(paramClass, paramString, paramObject, paramArrayOfClass);
	}

	public static  BeanDescriptor getConstraintsForClass(Class<?> paramClass){
		return validator.getConstraintsForClass(paramClass);
	}

	public static  <T> T unwrap(Class<T> paramClass){
		return validator.unwrap(paramClass);
	}
	
}

package cn.modoumama.common.utils.validator;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

/**
 * 验证手机号，空和正确的手机号都能验证通过<br>
 * 创建人：邓强   <br>
 * 创建时间：2018年1月5日 下午3:17:12    <br> 
 * 修改人：  <br>
 * 修改时间：2018年1月5日 下午3:17:12   <br>  
 * 修改备注：     <br>
 * @version   V1.0
 */
@Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}")
@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface Phone {
    String message() default "PHONE_INVALID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

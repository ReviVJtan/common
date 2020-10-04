package cn.modoumama.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kedong on 2017/1/11.
 * <p/>
 * 防止表单重复提交令牌
 * jsp页面使用方式：<input type="hidden" name="token" value="${token}"/>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {

    /**
     * 请求表单时，生成token，保存在session中;
     * 默认值为false，即不生成token；值为true时才会生成token
     *
     * @return
     * @example: @Token(save = true)
     */
    boolean save() default false;

    /**
     * 提交表单时验证token ，remove=true 表示删除同步token
     *
     * @return
     * @example: @Token(remove = true)
     */
    boolean remove() default false;
}

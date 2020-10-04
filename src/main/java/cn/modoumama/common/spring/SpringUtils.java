package cn.modoumama.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by kedong on 2016/12/13.
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext context = null;
    
    private SpringUtils(){}

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public synchronized static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public synchronized static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }


}

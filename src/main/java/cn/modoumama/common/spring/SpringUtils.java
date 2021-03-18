package cn.modoumama.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
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

    public static <T> T getBean(String beanName,Class<T> clazz){
    	Object object = getBean(beanName);
    	if(getBean(beanName) == null){
    		//将applicationContext转换为ConfigurableApplicationContext  
    		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;  
    		
    		// 获取bean工厂并转换为DefaultListableBeanFactory  
    		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext  
    				.getBeanFactory();  
    		
    		// 通过BeanDefinitionBuilder创建bean定义  
    		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);  
    		
    		// 注册bean  
    		defaultListableBeanFactory.registerBeanDefinition(beanName,beanDefinitionBuilder.getRawBeanDefinition());
    		
    		object = getBean(beanName);
    	}
    	
   	   return (T) object; 
      }
}

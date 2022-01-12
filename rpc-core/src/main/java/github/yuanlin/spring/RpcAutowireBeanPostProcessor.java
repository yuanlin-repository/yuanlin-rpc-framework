package github.yuanlin.spring;

import github.yuanlin.annotation.RpcAutowire;
import github.yuanlin.config.ServiceConfig;
import github.yuanlin.factory.SingletonFactory;
import github.yuanlin.proxy.RpcDynamicProxy;
import github.yuanlin.transport.RpcClient;
import github.yuanlin.transport.netty.client.NettyClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 给 @RpcAutowire 标注的属性返回代理对象（用于客户端）
 *
 * @author yuanlin
 * @date 2022/01/10/21:03
 */
@Component
public class RpcAutowireBeanPostProcessor implements BeanPostProcessor {

    private final RpcClient rpcClient;

    public RpcAutowireBeanPostProcessor() {
        rpcClient = SingletonFactory.getInstance(NettyClient.class);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)  {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            RpcAutowire rpcAutowire = field.getAnnotation(RpcAutowire.class);
            if (rpcAutowire != null) {
                Class<?> interfaceClass = field.getType();
                ServiceConfig serviceConfig = ServiceConfig.builder()
                        .service(bean)
                        .group(rpcAutowire.group())
                        .version(rpcAutowire.version())
                        .build();
                RpcDynamicProxy proxy = new RpcDynamicProxy(rpcClient, serviceConfig);
                Object proxyObj = proxy.getProxy(interfaceClass);
                field.setAccessible(true);
                try {
                    field.set(bean, proxyObj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}

package github.yuanlin.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * RPC 服务注解（标注在 RPC 服务实现类上）
 *
 * @author yuanlin
 * @date 2021/12/28/11:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface RpcService {

    String version() default "";

    String group() default "";
}
